package net.zdsoft.bigdata.datav.action.fixed;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * 学生情况大屏
 * @author duhuachao
 * @date 2019/6/22
 */
@Controller
@RequestMapping("/bigdata/datav/fixed/screen")
public class FixedXJScreenXSQKAction extends BigdataBaseAction {

    @Autowired
    private MysqlClientService mysqlClientService;

    @RequestMapping("/xsqk")
    public String studentCase(ModelMap map) {
        map.put("nowTime",new Date().getTime());
        String sql = "";
        String nowYear = DateUtils.date2String(new Date(),"yyyy");
        String nowMonth = DateUtils.date2String(new Date(),"MM");
        String nowTime = nowYear + nowMonth;
        StringBuilder yearSb = new StringBuilder();
        StringBuilder yearMSb = new StringBuilder();
        StringBuilder xnSb = new StringBuilder();
        for (int i=5;i>0;i--) {
            yearSb.append("," + DateUtils.date2String(org.apache.commons.lang.time.DateUtils.addYears(new Date(), -i),"yyyy"));
            yearMSb.append("," + DateUtils.date2String(org.apache.commons.lang.time.DateUtils.addYears(new Date(), -i),"yyyy") + "10");
        }
        for (int i=5;i>=0;i--) {
            xnSb.append("," + (Integer.valueOf(nowYear) - i) + "-" + (Integer.valueOf(nowYear) - i -1));
        }
        String yearStr = yearSb.toString().substring(1);
        String yearMStr = yearMSb.toString().substring(1);
        String xnStr = xnSb.toString().substring(1);
        map.put("yearStr", yearStr);
        map.put("yearMStr", yearMStr);
        map.put("xnStr", xnStr);

//        //在校学生数和班级数
//        sql = "select sum(xx_bs) as xx_bs, sum(cz_bs) as cz_bs, sum(gz_bs) as gz_bs, sum(hz_xsrs) as hz_xsrs, " +
//                "sum(wwrz_xsrs) as wwrz_xsrs, sum(qt_xsrs) as qt_xsrs, substring(region_code,1,4) as region_code " +
//                "from dm_app_xj_gis_region_view where date_format(creation_time,'%Y%m') = "+ nowTime +
//                " group by substring(region_code,1,4)";
//        List<Json> xsbjList = mysqlClientService.getDataListFromMysql(null,null,sql,null,null);
        List<Json> xsbjList = Lists.newArrayList();
        map.put("xsbjList", JsonArray.toJSON(xsbjList).toString());
//
//        //留守儿童人数
//        sql = "select sum(hj_dqls) as hj_dqls, sum(hj_sqls) as hj_sqls, sum(xxhj) as xxhj, sum(xxhj_dqls) as xxhj_dqls, " +
//                "sum(xxhj_sqls) as xxhj_sqls, sum(czhj) as czhj, sum(czhj_dqls) as czhj_dqls, sum(czhj_sqls) as czhj_sqls, " +
//                "substring(sszgjyxzdm,1,4) as xzdm from zxxs_tjbb_lsetqk where tjsj = "+ nowTime +
//                " group by substring(sszgjyxzdm,1,4)";
//        List<Json> lsetList = mysqlClientService.getDataListFromMysql(null,null,sql,null,null);
        List<Json> lsetList = Lists.newArrayList();
        map.put("lsetList", JsonArray.toJSON(lsetList).toString());
//
//        //特教学生人数
//        sql = "select sum(xss) as xss, substring(sszgjyxzdm,1,4) as xzdm from zxxs_tjbb_tsjycjxs where " +
//                "tjsj = "+ nowTime + " group by substring(sszgjyxzdm,1,4)";
//        List<Json> tjxsList = mysqlClientService.getDataListFromMysql(null,null,sql,null,null);
        List<Json> tjxsList = Lists.newArrayList();
        map.put("tjxsList", JsonArray.toJSON(tjxsList).toString());
//        //随迁子女人数
//        sql = "select sum(hj_sqzn) as hj_sqzn, substring(sszgjyxzdm,1,4) as xzdm from zxxs_tjbb_jcwgsqzn where " +
//                "tjsj = "+ nowTime + " group by substring(sszgjyxzdm,1,4)";
//        List<Json> sqznList = mysqlClientService.getDataListFromMysql(null,null,sql,null,null);
        List<Json> sqznList = Lists.newArrayList();
        map.put("sqznList", JsonArray.toJSON(sqznList).toString());
//
//        //转学异动情况
//        sql = "select sum(zr_ks) as zr_ks, sum(zr_sn_xj) as zr_sn_xj, sum(zr_snkds) as zr_snkds, " +
//                "sum(zr_dskqx) as zr_dskqx, sum(zr_qxn) as zr_qxn, sum(zc_ks) as zc_ks, sum(zc_sn_xj) as zc_sn_xj, " +
//                "sum(zc_snkds) as zc_snkds, sum(zc_dskqx) as zc_dskqx, sum(zc_qxn) as zc_qxn, xnxq, " +
//                "substring(sszgjyxzdm,1,4) as xzdm from zxxs_tjbb_zxyd where xnxq in ("+ xnStr +") " +
//                "group by xnxq,substring(sszgjyxzdm,1,4)";
//        List<Json> zxydList = mysqlClientService.getDataListFromMysql(null,null,sql,null,null);
        List<Json> zxydList = Lists.newArrayList();
        map.put("zxydList", JsonArray.toJSON(zxydList).toString());
//
//        //学生年级分布
//        sql = "select sum(xx_ynj) as xx_ynj, sum(xx_enj) as xx_enj, sum(xx_snj) as xx_snj, sum(xx_sinj) as xx_sinj, " +
//                "sum(xx_wnj) as xx_wnj, sum(xx_lnj) as xx_lnj, sum(cz_ynj) as cz_ynj, sum(cz_enj) as cz_enj, " +
//                "sum(cz_snj) as cz_snj, sum(cz_sinj) as cz_sinj, sum(gz_ynj) as gz_ynj, sum(gz_enj) as gz_enj, " +
//                "sum(gz_snj) as gz_snj, substring(sszgjyxzdm,1,4) as xzdm from zxxs_tjbb_zxssss_nj " +
//                "where tjsj = "+ nowTime +" group by substring(sszgjyxzdm,1,4)";
//        List<Json> xsnjfbList = mysqlClientService.getDataListFromMysql(null,null,sql,null,null);
        List<Json> xsnjfbList = Lists.newArrayList();
        map.put("xsnjfbList", JsonArray.toJSON(xsnjfbList).toString());
//
//        //义务教育班级数量
//        sql = "select sum(bjs_11) as bjs_11, sum(bjs_12) as bjs_12, sum(bjs_13) as bjs_13, sum(bjs_14) as bjs_14, " +
//                "sum(bjs_15) as bjs_15, sum(bjs_16) as bjs_16, sum(bjs_21) as bjs_21, sum(bjs_22) as bjs_22, " +
//                "sum(bjs_23) as bjs_23, sum(bjs_24) as bjs_24, substring(sszgjyxzdm,1,4) as xzdm " +
//                "from zxxs_tjbb_ywjybsbeqk where tjsj = "+ nowTime +" group by substring(sszgjyxzdm,1,4)";
//        List<Json> ywjybjslList = mysqlClientService.getDataListFromMysql(null,null,sql,null,null);
        List<Json> ywjybjslList = Lists.newArrayList();
        map.put("ywjybjslList", JsonArray.toJSON(ywjybjslList).toString());
//
//        //高中班级数量
//        sql = "select sum(bjs_1) as bjs_1, sum(bjs_2) as bjs_2, sum(bjs_3) as bjs_3, " +
//                "substring(sszgjyxzdm,1,4) as xzdm from zxxs_tjbb_gzbsbeqk where tjsj = "+ nowTime +
//                " group by substring(sszgjyxzdm,1,4)";
//        List<Json> gzbjslList = mysqlClientService.getDataListFromMysql(null,null,sql,null,null);
        List<Json> gzbjslList = Lists.newArrayList();
        map.put("gzbjslList", JsonArray.toJSON(gzbjslList).toString());
//
//        //历年新生人数
//        sql = "select sum(xx_ynj) as xx_ynj, sum(cz_ynj) as cz_ynj, sum(gz_ynj) as gz_ynj, tjsj, substring(sszgjyxzdm,1,4) as xzdm " +
//                "from zxxs_tjbb_zxssss_nj where tjsj in ("+ yearMStr +") group by tjsj,substring(sszgjyxzdm,1,4)";
//        List<Json> lnxsList = mysqlClientService.getDataListFromMysql(null,null,sql,null,null);
        List<Json> lnxsList = Lists.newArrayList();
        map.put("lnxsList", JsonArray.toJSON(lnxsList).toString());
//        //历年招生人数
//        sql = "select sum(xx_zsrs) as xx_zsrs, sum(cz_zsrs) as cz_zsrs, sum(gz_zsrs) as gz_zsrs, substring(tjsj,1,4) as tjsj, " +
//                "substring(sszgjyxzdm,1,4) as xzdm from zxxs_tjbb_zsqk where substring(tjsj,1,4) in ("+ yearStr +") " +
//                "group by substring(tjsj,1,4),substring(sszgjyxzdm,1,4)";
//        List<Json> lnzsList = mysqlClientService.getDataListFromMysql(null,null,sql,null,null);
        List<Json> lnzsList = Lists.newArrayList();
        map.put("lnzsList", JsonArray.toJSON(lnzsList).toString());
//
//        //义务教育独生子女人数
//        sql = "select sum(xx_xss) as xx_xss, sum(xx_dszn) as xx_dszn, sum(cz_xss) as cz_xss, sum(cz_dszn) as cz_dszn, " +
//                "substring(sszgjyxzdm,1,4) as xzdm from zxxs_tjbb_dsznqk where tjsj = "+ nowTime +
//                " group by substring(sszgjyxzdm,1,4)";
//        List<Json> ywjydsList = mysqlClientService.getDataListFromMysql(null,null,sql,null,null);
        List<Json> ywjydsList = Lists.newArrayList();
        map.put("ywjydsList", JsonArray.toJSON(ywjydsList).toString());
//
//        //义务教育民族人数和男女人数
//        sql = "select sum(ywjy) as ywjy, sum(ywjy_qzn) as ywjy_qzn, mzm, substring(region_code,1,4) as region_code " +
//                "from zxxs_tjbb_zxs_mz where where tjsj = "+ nowTime +" group by mzm,substring(region_code,1,4)";
//        List<Json> ywjymzList = mysqlClientService.getDataListFromMysql(null,null,sql,null,null);
        List<Json> ywjymzList = Lists.newArrayList();
        map.put("ywjymzList", JsonArray.toJSON(ywjymzList).toString());
//
//        //控辍保学政策情况趋势图
//        sql = "select substring(tjsj,1,4) as tjsj, substring(sszgjyxzdm,1,4) as xzdm, sum(xxcx) as xxcx, sum(xxyscx) " +
//                "as xxyscx, sum(xxfx) as xxfx, sum(czcx) as czcx, sum(czyscx) as czyscx, sum(czfx) as czfx from zxxs_tjbb_ywjykcbx " +
//                "where substring(tjsj,1,4) in ("+ yearStr +") group by substring(tjsj,1,4),substring(sszgjyxzdm,1,4)";
//        List<Json> kcbxList = mysqlClientService.getDataListFromMysql(null,null,sql,null,null);
        List<Json> kcbxList = Lists.newArrayList();
        map.put("kcbxList", JsonArray.toJSON(kcbxList).toString());
        return "/bigdata/datav/fixed/xj/xsqk-index.ftl";
    }

    @ResponseBody
    @RequestMapping("/stuClassNumRank")
    @ControllerInfo("各地区平均班额排行榜")
    public Response stuClassNumRank(String type, String showRegionCode){
        String nowTime = DateUtils.date2String(new Date(),"yyyyMM");
        String[] numParam = getNumParam(type).split(",");
        String stuNumParam = numParam[0];
        String classNumParam = numParam[1];
        try {
//            String sql = "";
//            if (Objects.equals(type,"高一") || Objects.equals(type,"高二") || Objects.equals(type,"高三")) {
//                sql += "select ceiling(student."+ stuNumParam +"/class."+ classNumParam +") as num, " +
//                        "student.xxmc as name from zxxs_tjbb_zxssss_nj as student inner join zxxs_tjbb_gzbsbeqk " +
//                        "as class on student.xx_jbxx_id = class.xx_jbxx_id and student.tjsj = class.tjsj " +
//                        "where student.tjsj = "+ nowTime;
//            } else {
//                sql += "select ceiling(student."+ stuNumParam +"/class."+ classNumParam +") as num, " +
//                        "student.xxmc as name from zxxs_tjbb_zxssss_nj as student inner join zxxs_tjbb_ywjybsbeqk " +
//                        "as class on student.xx_jbxx_id = class.xx_jbxx_id and student.tjsj = class.tjsj " +
//                        "where student.tjsj = "+ nowTime;
//            }
//            if (StringUtils.isNotEmpty(showRegionCode)) {
//                sql += " and substring(student.sszgjyxzdm,1,4) = "+ showRegionCode;
//            }
//            sql += " order by num desc";
//            List<Json> resultList = mysqlClientService.getDataListFromMysql(null,null,sql,null,null);
            List<Json> rankingList = Lists.newArrayList();
            return Response.ok().data(JSON.toJSONString(rankingList)).build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    private String getNumParam(String type) {
        String stuNumParam;
        String classNumParam;
        if (Objects.equals(type,"小一")) {
            stuNumParam = "xx_ynj";
            classNumParam = "bjs_11";
        } else if (Objects.equals(type,"小二")) {
            stuNumParam = "xx_enj";
            classNumParam = "bjs_12";
        } else if (Objects.equals(type,"小三")) {
            stuNumParam = "xx_snj";
            classNumParam = "bjs_13";
        } else if (Objects.equals(type,"小四")) {
            stuNumParam = "xx_sinj";
            classNumParam = "bjs_14";
        } else if (Objects.equals(type,"小五")) {
            stuNumParam = "xx_wnj";
            classNumParam = "bjs_15";
        } else if (Objects.equals(type,"小六")) {
            stuNumParam = "xx_lnj";
            classNumParam = "bjs_16";
        } else if (Objects.equals(type,"初一")) {
            stuNumParam = "cz_ynj";
            classNumParam = "bjs_21";
        } else if (Objects.equals(type,"初二")) {
            stuNumParam = "cz_enj";
            classNumParam = "bjs_22";
        } else if (Objects.equals(type,"初三")) {
            stuNumParam = "cz_snj";
            classNumParam = "bjs_23";
        } else if (Objects.equals(type,"初四")) {
            stuNumParam = "cz_sinj";
            classNumParam = "bjs_24";
        } else if (Objects.equals(type,"高一")) {
            stuNumParam = "gz_ynj";
            classNumParam = "bjs_1";
        } else if (Objects.equals(type,"高二")) {
            stuNumParam = "gz_enj";
            classNumParam = "bjs_2";
        } else {
            stuNumParam = "gz_snj";
            classNumParam = "bjs_3";
        }
        return stuNumParam+","+classNumParam;
    }

    @ResponseBody
    @RequestMapping("/intoOrOutRank")
    @ControllerInfo("本学年各地区的转入转出数量排行榜")
    public Response intoOrOutRank(String type, String showRegionCode, String showTransferType){
        Integer year = Integer.valueOf(DateUtils.date2String(new Date(),"yyyy"));
        String nowXn = year + "-" + (year-1);
        String param = getParam(type,showTransferType);
        try {
//            String sql = "select "+ param +" as num, xxmc as name from zxxs_tjbb_zxyd where xnxq = "+ nowXn;
//            if (StringUtils.isNotEmpty(showRegionCode)) {
//                sql += " and substring(sszgjyxzdm,1,4) = "+ showRegionCode;
//            }
//            sql += " order by "+ param +" desc";
//            List<Json> resultList = mysqlClientService.getDataListFromMysql(null,null,sql,null,null);
            List<Json> resultList = Lists.newArrayList();
            return Response.ok().data(JSON.toJSONString(resultList)).build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    private String getParam(String type, String showTransferType) {
        String param = "";
        if (Objects.equals(type,"转入")) {
            param += "zr_";
        } else {
            param += "zc_";
        }
        if (Objects.equals(showTransferType,"跨省")) {
            param += "ks";
        } else if (Objects.equals(showTransferType,"省内小计")) {
            param += "sn_xj";
        } else if (Objects.equals(showTransferType,"省内跨地市")) {
            param += "snkds";
        } else if (Objects.equals(showTransferType,"地市内跨县")) {
            param += "dskqx";
        } else {
            param += "qxn";
        }
        return param;
    }
}