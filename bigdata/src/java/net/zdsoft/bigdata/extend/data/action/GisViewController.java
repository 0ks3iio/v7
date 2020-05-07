package net.zdsoft.bigdata.extend.data.action;

import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.bigdata.v3.index.action.BigdataBiBaseAction;
import net.zdsoft.bigdata.v3.index.entity.HeadInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/bigdata/gis")
public class GisViewController extends BigdataBiBaseAction {

    @Autowired
    RegionRemoteService regionRemoteService;

    @Autowired
    MysqlClientService mysqlClientService;

    @RequestMapping("/index")
    public String index(String regionCode, String parentRegionCode,
                        ModelMap map) {
        OptionDto GisDto = optionService.getAllOptionParam("gis");
        if (GisDto != null
                && StringUtils.isNotBlank(GisDto.getFrameParamMap().get(
                "gis_url"))) {
            map.put("gisUrl", GisDto.getFrameParamMap().get("gis_url"));
            map.put("currentModuleUrl", GisDto.getFrameParamMap().get("gis_url"));
        } else {
            return "/bigdata/v3/common/error.ftl";
        }
        OptionDto styleDto = optionService.getAllOptionParam("pageStyle");
        String style = styleDto.getFrameParamMap().get("style");
        if ("2".equals(style)) {
            HeadInfo headInfo = getHeadInfo();
            headInfo.setTitle("GIS全景视图");
            map.put("headInfo", headInfo);
            return "/bigdata/v3/templates/bi/bi-transfer.ftl";
        }
        return "/bigdata/gis/index.ftl";
    }

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
        map.put("regionList", resultList);
        map.put("currentRegion", currentRegion);
        map.put("regionCode", regionCode);
        if (StringUtils.isBlank(parentRegionCode))
            parentRegionCode = regionCode;
        map.put("parentRegionCode", parentRegionCode);
        return "/bigdata/gis/regionMap.ftl";
    }

    @RequestMapping("/school")
    public String school(String regionCode, String parentRegionCode,
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
        map.put("currentRegion", currentRegion);
        map.put("regionCode", regionCode);
        map.put("parentRegionCode", parentRegionCode);
        map.put("regionStat", getRegionStatInfo(currentRegion.getRegionCode()));
        return "/bigdata/gis/schoolMap.ftl";
    }

    @RequestMapping("/regionDetail")
    public String regionDetail(String regionCode, ModelMap map) {
        Region currentRegion = SUtils.dc(
                regionRemoteService.findByFullCode(regionCode), Region.class);
        map.put("currentRegion", currentRegion);
        map.put("regionStat", getRegionStatInfo(currentRegion.getRegionCode()));
        return "/bigdata/gis/regionDetail.ftl";
    }

    private Json getRegionStatInfo(String regionCode) {
        regionCode = regionCode + "%";
        Json result = new Json();
        return result;
    }

    public static String getPercent(int number1, int number2) {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(1);
        String result = numberFormat.format((float) number1 / (float) number2
                * 100);
        return result;
    }

    @RequestMapping("/schoolDetail")
    public String schoolDetail(String schoolId, String schoolName, ModelMap map) {
        map.put("schoolId", schoolName);
        map.put("schoolName", schoolName);
        map.put("schoolStat", getSchoolStatInfo(schoolId, schoolName));
        return "/bigdata/gis/schoolDetail.ftl";
    }

    private Json getSchoolStatInfo(String schoolId, String schoolName) {
        Json result = new Json();
        return result;
    }

}
