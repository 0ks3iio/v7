package net.zdsoft.bigdata.extend.data.action.customization;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.mysql.jdbc.Driver;
import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/bigdata/customization/xinjiang/common/gis")
public class XjGisViewController extends BigdataBaseAction {

    @Autowired
    private RegionRemoteService regionRemoteService;

    @Autowired
    SysOptionRemoteService sysOptionRemoteService;

    @Autowired
    MysqlClientService mysqlClientService;

    @Autowired
    OptionService optionService;

    @RequestMapping("/drill")
    public String region(String regionCode, String parentRegionCode,
                         ModelMap map) {
        OptionDto mapDto = optionService.getAllOptionParam("baidumap");
        if (mapDto != null
                && StringUtils.isNotBlank(mapDto.getFrameParamMap().get("ak"))) {
            map.put("ak", mapDto.getFrameParamMap().get("ak"));
        } else {
            return "/bigdata/v3/common/error.ftl";
        }

        Region currentRegion = SUtils.dc(
                regionRemoteService.findByFullCode(regionCode), Region.class);

        if(currentRegion.getRegionCode().length()==6){
            currentRegion = SUtils.dc(
                    regionRemoteService.findByFullCode(regionCode.substring(0,4)+"00"), Region.class);
            regionCode=currentRegion.getFullCode();
            parentRegionCode =regionCode.substring(0,2)+"0000";
        }

        if (StringUtils.isBlank(currentRegion.getLocationLatitude())) {
            currentRegion.setLocationLatitude(currentRegion.getLatitude());
        }
        if (StringUtils.isBlank(currentRegion.getLocationLongitude())) {
            currentRegion.setLocationLongitude(currentRegion.getLongitude());
        }
        List<Region> subRegionList = SUtils.dt(
                regionRemoteService.findUnderlineRegions(regionCode),
                Region.class);
        List<Region> resultList = new ArrayList<Region>();
        for (Region region : subRegionList) {
            if (!regionCode.equals(region.getFullCode())) {
                resultList.add(region);
            }
        }
        if (regionCode.equals("650000")) {
            Region shzRegion = SUtils.dc(
                    regionRemoteService.findByFullCode("659001"), Region.class);
            resultList.add(shzRegion);
        }

        map.put("regionList", resultList);
        map.put("currentRegion", currentRegion);
        map.put("regionCode", regionCode);
        if (StringUtils.isBlank(parentRegionCode))
            parentRegionCode = regionCode;
        map.put("parentRegionCode", parentRegionCode);
        return "/bigdata/gis/xj/regionMap.ftl";
    }

    @RequestMapping("/school")
    public String school(String regionCode, String parentRegionCode,String schoolName,
                         ModelMap map) throws Exception {
        OptionDto mapDto = optionService.getAllOptionParam("baidumap");
        if (mapDto != null
                && StringUtils.isNotBlank(mapDto.getFrameParamMap().get("ak"))) {
            map.put("ak", mapDto.getFrameParamMap().get("ak"));
        } else {
            return "/bigdata/v3/common/error.ftl";
        }

        Region currentRegion = SUtils.dc(
                regionRemoteService.findByFullCode(regionCode), Region.class);

        if(currentRegion.getRegionCode().length()==6){
            Region provinceRegion = SUtils.dc(
                    regionRemoteService.findByFullCode(regionCode.substring(0,2)+"0000"), Region.class);
            Region cityRegion = SUtils.dc(
                    regionRemoteService.findByFullCode(regionCode.substring(0,4)+"00"), Region.class);
            String regionName=provinceRegion.getRegionName()+"-->"+cityRegion.getRegionName()+"-->"+currentRegion.getRegionName();
            map.put("regionName", regionName);
        }else if(currentRegion.getRegionCode().length()==4){
            Region provinceRegion = SUtils.dc(
                    regionRemoteService.findByFullCode(regionCode.substring(0,2)+"0000"), Region.class);

            String regionName=provinceRegion.getRegionName()+"-->"+currentRegion.getRegionName();
            map.put("regionName", regionName);
        }else if(currentRegion.getRegionCode().length()==2){
                        String regionName=currentRegion.getRegionName();
            map.put("regionName", regionName);
        }

        List<Json> schoolList = readSchoolGisInfo(currentRegion.getRegionCode(),schoolName);
        map.put("currentRegion", currentRegion);
        map.put("regionCode", regionCode);

        map.put("parentRegionCode", parentRegionCode);
        map.put("schoolList", schoolList);
        map.put("schoolName", schoolName);
        //map.put("regionStat", getRegionStatInfo(currentRegion.getRegionCode()));
        return "/bigdata/gis/xj/schoolMap.ftl";
    }

    @RequestMapping("/regionDetail")
    public String regionDetail(String regionCode, ModelMap map) {
        Region currentRegion = SUtils.dc(
                regionRemoteService.findByFullCode(regionCode), Region.class);
        map.put("currentRegion", currentRegion);
        map.put("regionStat", getRegionStatInfo(currentRegion.getRegionCode()));
        return "/bigdata/gis/xj/regionDetail.ftl";
    }

    private Json getRegionStatInfo(String regionCode) {

        String sql = "select * from dm_app_xj_gis_region_view where region_code =@regionCode";
        sql = sql.replace(
                "@regionCode", regionCode);
        List<Json> resultList = mysqlClientService.getDataListFromMysql(null,
                null, sql, new ArrayList<Json>(),
                new ArrayList<Json>());

        if (CollectionUtils.isEmpty(resultList)) {
            return new Json();
        }

        Json result = resultList.get(0);

        int studnationHz = result.getIntValue("hz_xsrs");
        int studnationWwez = result.getIntValue("wwrz_xsrs");
        int studnationOther = result.getIntValue("qt_xsrs");


        int teacnationHz = result.getIntValue("hz_jsrs");
        int teacnationWwez = result.getIntValue("wwrz_jsrs");
        int teacnationOther = result.getIntValue("qt_jsrs");

        if (studnationHz + studnationWwez + studnationOther != 0) {
            result.put(
                    "studnationHz",
                    getPercent(
                            studnationHz,
                            (studnationHz + studnationWwez + studnationOther)));
            result.put(
                    "studnationWwez",
                    getPercent(studnationWwez, (studnationHz
                            + studnationWwez + studnationOther)));
            result.put(
                    "studnationOther",
                    getPercent(studnationOther, (studnationHz
                            + studnationWwez + studnationOther)));
        }
        result.put("studnationHzmz", studnationHz);
        result.put("studnationSsmz", studnationOther + studnationWwez);

        if (teacnationHz + teacnationWwez + teacnationOther > 0) {
            result.put(
                    "teacnationHz",
                    getPercent(
                            teacnationHz,
                            (teacnationHz + teacnationWwez + teacnationOther)));
            result.put(
                    "teacnationWwez",
                    getPercent(teacnationWwez, (teacnationHz
                            + teacnationWwez + teacnationOther)));
            result.put(
                    "teacnationOther",
                    getPercent(teacnationOther, (teacnationHz
                            + teacnationWwez + teacnationOther)));
        }
        result.put("teacnationSsmz", teacnationOther + teacnationWwez);
        result.put("teacnationHzmz", teacnationHz);

        return result;
    }

    @RequestMapping("/schoolDetail")
    public String schoolDetail(String schoolId, String schoolName, ModelMap map) {
        map.put("schoolId", schoolName);
        map.put("schoolName", schoolName);
        map.put("schoolStat", getSchoolStatInfo(schoolId, schoolName));
        return "/bigdata/gis/xj/schoolDetail.ftl";
    }

    private Json getSchoolStatInfo(String schoolId, String schoolName) {
        if (StringUtils.isBlank(schoolId)) {
            // 说明是搜索出来的结果
        }

        Json result = new Json();
        String sql = "select * from dm_app_xj_gis_school_view where school_name ='@schoolName'";
        sql = sql.replace(
                "@schoolName", schoolName);
        List<Json> resultList = mysqlClientService.getDataListFromMysql(null,
                null, sql, new ArrayList<Json>(),
                new ArrayList<Json>());

        if (CollectionUtils.isEmpty(resultList)) {
            return new Json();
        }

        result = resultList.get(0);

        int studnationHz = result.getIntValue("hz_xsrs");
        int studnationSsmz = result.getIntValue("szmz_xsrs");
        int teacnationHz = result.getIntValue("hz_jsrs");
        int teacnationSsmz = result.getIntValue("szmz_jsrs");

        if (studnationHz + studnationSsmz > 0) {
            result.put(
                    "studnationHz",
                    getPercent(
                            studnationHz,
                            (studnationHz + studnationSsmz)));
            result.put(
                    "studnationSsmz",
                    getPercent(studnationSsmz, (studnationHz
                            + studnationSsmz)));
        } else {
            result.put(
                    "studnationHz",
                    0);
            result.put(
                    "studnationSsmz",
                    0);
        }

        if (teacnationHz + teacnationSsmz > 0) {
            result.put(
                    "teacnationHz",
                    getPercent(
                            teacnationHz,
                            (teacnationHz + teacnationSsmz)));
            result.put(
                    "teacnationSsmz",
                    getPercent(teacnationSsmz, (teacnationHz
                            + teacnationSsmz)));
        } else {
            result.put(
                    "teacnationHz",
                    0);
            result.put(
                    "teacnationSsmz",
                    0);
        }
        return result;
    }

    @SuppressWarnings("deprecation")
    private List<Json> readSchoolGisInfo(String regionCode, String schoolName) throws Exception {
        String sql = null;
        if (StringUtils.isNotBlank(schoolName)) {
            sql = "select * from dm_app_xj_school_info where region_code like '@regionCode%' and school_name like '%@schoolName%'";
            sql = sql.replace(
                    "@regionCode", regionCode).replace("@schoolName", schoolName);
        } else {
            sql = "select * from dm_app_xj_school_info where region_code like '@regionCode%'";
            sql = sql.replace(
                    "@regionCode", regionCode);
        }

        return mysqlClientService.getDataListFromMysql(null,
                null, sql, new ArrayList<Json>(),
                new ArrayList<Json>());
    }

    private static String getPercent(int number1, int number2) {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(1);
        String result = numberFormat.format((float) number1 / (float) number2
                * 100);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(DateUtils.date2String(new Date(), "YYYY/M/d"));
    }
}
