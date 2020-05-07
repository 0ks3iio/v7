package net.zdsoft.bigdata.datav.action.fixed;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 学生教师情况大屏
 * @author duhuachao
 * @date 2019/5/30
 */
@Controller
@RequestMapping("/bigdata/datav/fixed/screen")
public class FixedXJScreenxsjsAction extends BigdataBaseAction {

    @Autowired
    private MysqlClientService mysqlClientService;

    @RequestMapping("/oldxsqk")
    public String studentCase(ModelMap map) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        StringBuilder yearStr = new StringBuilder();
        String[] years = new String[6];
        String nowYear = DateUtils.date2String(new Date(),"yyyy");
        for (int i=0;i<6;i++) {
            yearStr.append("," + String.valueOf(Integer.valueOf(nowYear) - 5 + i) + "03");
            years[i] = String.valueOf(Integer.valueOf(nowYear) - 5 + i) + "03";
        }
        map.put("years",years);
        String nationSql = "select sum(total) as total,sum(total_qzn) as total_qzn,sum(xx) as xx,sum(cz) as cz," +
                "sum(gz) as gz,mzm,mzm_mc from zxxs_tjbb_zxs_mz where tjsj = " + nowYear + "03" + " group by mzm,mzm_mc";
        List<Json> nowDataList = mysqlClientService.getDataListFromMysql(null,null,nationSql,null,null);
        Integer totalCount = 0;
        if (CollectionUtils.isNotEmpty(nowDataList)) {
            //总人数，男女比例，学段人数比例
            totalCount = nowDataList.parallelStream().map(json->json.getInteger("total")).reduce(Integer::sum).get();
            Integer girlTotalCount = nowDataList.parallelStream().map(json->json.getInteger("total_qzn")).reduce(Integer::sum).get();
            Integer xxCount = nowDataList.parallelStream().map(json->json.getInteger("xx")).reduce(Integer::sum).get();
            Integer czCount = nowDataList.parallelStream().map(json->json.getInteger("cz")).reduce(Integer::sum).get();
            Integer gzCount = nowDataList.parallelStream().map(json->json.getInteger("gz")).reduce(Integer::sum).get();
            map.put("totalCount",String.valueOf(totalCount));
            map.put("girlTotalCount",numberFormat.format((float) girlTotalCount / (float) totalCount * 100));
            map.put("boyTotalCount",numberFormat.format((float) (totalCount-girlTotalCount) / (float) totalCount * 100));
            map.put("xxCount",numberFormat.format((float) xxCount / (float) totalCount * 100));
            map.put("czCount",numberFormat.format((float) czCount / (float) totalCount * 100));
            map.put("gzCount",numberFormat.format((float) gzCount / (float) totalCount * 100));
        }
        //民族分布图
        Integer otherNation = 0;
        Map<String,Integer> nationSizeMap = Maps.newLinkedHashMap();
        for (Json json : nowDataList) {
            if (totalCount * 0.01 < json.getDouble("total")) {
                nationSizeMap.put(json.getString("mzm_mc"),json.getInteger("total"));
            } else {
                otherNation += json.getInteger("total");
            }
        }
        nationSizeMap.put("其他",otherNation);
        map.put("nationSizeMap", nationSizeMap);
        // 在校生地区分布
        String areaSql = "select substring(region_code,1,4) as region_code,sum(total) as total from zxxs_tjbb_zxs_mz where tjsj = " + nowYear + "03" + " group by substring(region_code,1,4)";
        List<Json> areaDataList = mysqlClientService.getDataListFromMysql(null,null,areaSql,null,null);
        Map<String,Integer> areaSizeMap = Maps.newHashMap();
        String areaName = "";
        for (Json json : areaDataList) {
            if (Objects.equals(json.getString("region_code"),"6501")) {
                areaName = "乌鲁木齐市";
            } else if (Objects.equals(json.getString("region_code"),"6502")) {
                areaName = "克拉玛依市";
            } else if (Objects.equals(json.getString("region_code"),"6521")) {
                areaName = "吐鲁番市";
            } else if (Objects.equals(json.getString("region_code"),"6522")) {
                areaName = "哈密市";
            } else if (Objects.equals(json.getString("region_code"),"6523")) {
                areaName = "昌吉回族自治州";
            } else if (Objects.equals(json.getString("region_code"),"6527")) {
                areaName = "博尔塔拉蒙古自治州";
            } else if (Objects.equals(json.getString("region_code"),"6528")) {
                areaName = "巴音郭楞蒙古自治州";
            } else if (Objects.equals(json.getString("region_code"),"6529")) {
                areaName = "阿克苏地区";
            } else if (Objects.equals(json.getString("region_code"),"6530")) {
                areaName = "克孜勒苏柯尔克孜自治州";
            } else if (Objects.equals(json.getString("region_code"),"6531")) {
                areaName = "喀什地区";
            } else if (Objects.equals(json.getString("region_code"),"6532")) {
                areaName = "和田地区";
            } else if (Objects.equals(json.getString("region_code"),"6540")) {
                areaName = "伊犁哈萨克自治州";
            } else if (Objects.equals(json.getString("region_code"),"6542")) {
                areaName = "塔城地区";
            } else if (Objects.equals(json.getString("region_code"),"6543")) {
                areaName = "阿勒泰地区";
            } else {
                continue;
            }
            areaSizeMap.put(areaName,json.getInteger("total"));
        }
        map.put("areaSizeMap",areaSizeMap);
        //历年学生人数变化
        String studentSizeSql = "select sum(xx) as xx, sum(cz) as cz, sum(gz) as gz,tjsj from zxxs_tjbb_zxs_mz where tjsj in (" + yearStr.toString().substring(1) + ") group by tjsj order by tjsj asc";
        List<Json> studentSizeList = mysqlClientService.getDataListFromMysql(null,null,studentSizeSql,null,null);
        Integer[] xxSize = new Integer[studentSizeList.size()];
        Integer[] czSize = new Integer[studentSizeList.size()];
        Integer[] gzSize = new Integer[studentSizeList.size()];
        for (int i=0;i<studentSizeList.size();i++) {
            Json json = studentSizeList.get(i);
            xxSize[i] = json.getInteger("xx");
            czSize[i] = json.getInteger("cz");
            gzSize[i] = json.getInteger("gz");
        }
        map.put("xxSize",xxSize);
        map.put("czSize",czSize);
        map.put("gzSize",gzSize);
        //留守儿童数据
        String childrenSql = "select sum(hj) as hj, sum(hj_dqls) as hj_dqls, sum(hj_sqls) as hj_sqls, sum(xxhj_dqls) as xxhj_dqls, " +
                "sum(xxhj_sqls) as xxhj_sqls, sum(czhj_dqls) as czhj_dqls, sum(czhj_sqls) as czhj_sqls, tjsj from ZXXS_TJBB_LSETQK " +
                "where tjsj in (" + yearStr.toString().substring(1) + ") group by tjsj order by tjsj asc";
        List<Json> childrenList = mysqlClientService.getDataListFromMysql(null,null,childrenSql,null,null);
        Integer dqls = 0;
        Integer sqls = 0;
        Integer nols = 0;
        Integer[] xxlsSize = new Integer[childrenList.size()];
        Integer[] czlsSize = new Integer[childrenList.size()];
        int index = 0;
        for (Json json : childrenList) {
            if (Objects.equals(json.getString("tjsj"),nowYear+"03")) {
                dqls = json.getInteger("hj_dqls");
                sqls = json.getInteger("hj_sqls");
                nols = json.getInteger("hj") - json.getInteger("hj_dqls") - json.getInteger("hj_sqls");
            }
            xxlsSize[index] = json.getInteger("xxhj_dqls") + json.getInteger("xxhj_sqls");
            czlsSize[index] = json.getInteger("czhj_dqls") + json.getInteger("czhj_sqls");
            index++;
        }
        map.put("dqls",dqls);
        map.put("sqls",sqls);
        map.put("nols",nols);
        map.put("xxlsSize",xxlsSize);
        map.put("czlsSize",czlsSize);
        //留守儿童人数分布
        String lsfbSql = "select bxlx_mc,sum(hj_dqls) as hj_dqls,sum(hj_sqls) as hj_sqls from ZXXS_TJBB_LSETQK where tjsj = " + nowYear + "03" + " group by bxlx_mc;";
        List<Json> lsfbList = mysqlClientService.getDataListFromMysql(null,null,lsfbSql,null,null);
        List<String[]> lsfbs = Lists.newArrayList();
        String[] ls = null;
        for (int i=0;i<lsfbList.size();i++) {
            Json json = lsfbList.get(i);
            ls = new String[3];
            ls[0] = json.getString("bxlx_mc");
            ls[1] = json.getString("hj_dqls");
            ls[2] = json.getString("hj_sqls");
            lsfbs.add(ls);
        }
        map.put("lsfbs",lsfbs);
        return "/bigdata/datav/fixed/xj/xjxsqk.ftl";
    }

    @RequestMapping("/jsqk")
    public String teacherCase(ModelMap map) {
        // 教师总人数
        Integer teacherSize = 0;
        String teacherSizeSql = "select count(*) as count from xj_bg_middle_teacher";
        List<Json> teacherCount = mysqlClientService.getDataListFromMysql(null,null,teacherSizeSql,null,null);
        if (CollectionUtils.isNotEmpty(teacherCount)) {
            teacherSize = teacherCount.get(0).getInteger("count");
        }
        map.put("teacherSize",teacherSize + "");
        //教师政治面貌
        String backgroundSql = "select count(*) as count,background  from xj_bg_middle_teacher where background is not null group by background";
        List<Json> backSizeList = mysqlClientService.getDataListFromMysql(null,null,backgroundSql,null,null);
        Map<String,Integer> backSizeMap = Maps.newLinkedHashMap();
        Integer otherBackSize = 0;
        for (Json json : backSizeList) {
            if (json.getInteger("count") < teacherSize * 0.01) {
                otherBackSize = json.getInteger("count");
            } else {
                backSizeMap.put(json.getString("background"),json.getInteger("count"));
            }
        }
        backSizeMap.put("其他",otherBackSize);
        map.put("backSizeMap",backSizeMap);
        //教师民族分布
        String nationSql = "select count(*) as count,nation from xj_bg_middle_teacher where nation is not null group by nation";
        List<Json> nationSizeList = mysqlClientService.getDataListFromMysql(null,null,nationSql,null,null);
        Map<String,Integer> nationSizeMap = Maps.newLinkedHashMap();
        Integer otherNationSize = 0;
        for (Json json : nationSizeList) {
            if (json.getInteger("count") < teacherSize * 0.01) {
                otherNationSize = json.getInteger("count");
            } else {
                nationSizeMap.put(json.getString("nation"),json.getInteger("count"));
            }
        }
        nationSizeMap.put("其他",otherNationSize);
        map.put("nationSizeMap",nationSizeMap);
        //教师学位分布
        String educationSql = "select sum(bs) as bs, sum(ss) as ss, sum(xs) as xs from TB_CHART_ZRJSXWQK";
        List<Json> educationList = mysqlClientService.getDataListFromMysql(null,null,educationSql,null,null);
        if (CollectionUtils.isNotEmpty(educationList)) {
            map.put("xwJson",educationList.get(0));
        }
        //教师年龄分布
        String ageSql = "select sum(yx24s) as yx24s, sum(s2529) as s2529, sum(s3034) as s3034, sum(s3539) as s3539, sum(s4044) as s4044," +
                "sum(s4549) as s4549, sum(s5054) as s5054, sum(s5559) as s5559, sum(s6064) as s6064,sum(ys65s) as ys65s from tb_chart_zrjsnlqk";
        List<Json> ageList = mysqlClientService.getDataListFromMysql(null,null,ageSql,null,null);
        if (CollectionUtils.isNotEmpty(ageList)) {
            map.put("ageJson",ageList.get(0));
        }
        //教师工龄分布
        String workAgeSql = "select count(*) as count,work_age from xj_bg_middle_teacher where work_age is not null and work_age > 0 group by work_age";
        List<Json> workAgeList = mysqlClientService.getDataListFromMysql(null,null,workAgeSql,null,null);
        map.put("workAgeList",workAgeList);
        // 教师地区分布
        String dqfbSql = "SELECT COUNT(*) AS count,region.city_code AS city_code,region.city_name FROM xj_bg_middle_teacher AS teacher,xj_bg_dim_region_teacher AS region WHERE teacher.np_id = region.id AND np_id LIKE '65%' GROUP BY region.city_code,region.city_name";
        List<Json> dqfbList = mysqlClientService.getDataListFromMysql(null,null,dqfbSql,null,null);
        Map<String,Integer> dqfbMap = Maps.newHashMap();
        for (Json json : dqfbList) {
            if (Objects.equals("6500",json.getString("city_code")) || Objects.equals("6590",json.getString("city_code"))) {
                continue;
            }
            dqfbMap.put(json.getString("city_name"),json.getInteger("count"));
        }
        map.put("dqfbMap",dqfbMap);
        return "/bigdata/datav/fixed/xj/xjjsqk.ftl";
    }
}
