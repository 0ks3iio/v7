package net.zdsoft.bigdata.datav.action.fixed;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * 公文消息数据监控大屏
 * @author duhuachao
 * @since 2019/6/18
 */
@Controller
@RequestMapping("/bigdata/datav/fixed/screen")
public class FixedXJScreenGWXXAction {

    private static final String LIKE_CODE = "65%";
    private static final String UNIT_ID = "8AA1838525057EDA012505808B7D0000";

    @Autowired
    private MysqlClientService mysqlClientService;

    @RequestMapping("/gwxx")
    public String execute(ModelMap map) {
        String year = DateUtils.date2String(new Date(),"yyyy");
        String month = DateUtils.date2String(new Date(),"MM");
        //获取趋势月份
        List<String> timeList = Lists.newArrayList();
        for (int i=4;i>=0;i--) {
            timeList.add(DateUtils.date2String(org.apache.commons.lang.time.DateUtils.addMonths(new Date(), -i),"yyyy-MM"));
        }
        map.put("timeList",JsonArray.toJSON(timeList).toString());
        map.put("nowTime",new Date().getTime());

        // 总数据量
        String numSql = "select sum(send_num) as send_num, sum(receive_num) as receive_num, sum(receive_unsign_num) as receive_unsign_num, " +
                "sum(receive_file) as receive_file, substring(region_code,1,4) as region_code from dwd_szxy_officedoc_unit_month_increment " +
                "where year = "+ year + " and month = "+ month + " and region_code like '"+ LIKE_CODE +"' group by substring(region_code,1,4)";
        List<Json> numJsonList = mysqlClientService.getDataListFromMysql(null,null,numSql,null,null);
        map.put("allNumTitle", JsonArray.toJSON(numJsonList).toString());
        // 省厅总数据量
        String stNumSql = "select send_num, receive_num, receive_unsign_num, receive_file from dwd_szxy_officedoc_unit_month_increment " +
                "where year = "+ year + " and month = "+ month + " and unit_id = '"+ UNIT_ID +"'";
        List<Json> stNumList = mysqlClientService.getDataListFromMysql(null,null,stNumSql,null,null);
        if (CollectionUtils.isNotEmpty(stNumList)) {
            map.put("stNum", stNumList.get(0).toJSONString());
        }

        // 收文活跃覆盖人数
        String activeSql = "select sum(receive_activeuser_num) as receive_activeuser_num, substring(region_code,1,4) as region_code " +
                "from dwd_szxy_officedoc_unit_month_increment where region_code like '"+ LIKE_CODE +"' group by substring(region_code,1,4)";
        List<Json> receiveActiveList = mysqlClientService.getDataListFromMysql(null,null,activeSql,null,null);
        map.put("receiveActiveList", JsonArray.toJSON(receiveActiveList).toString());
        String stActiveSql = "select sum(receive_activeuser_num) as receive_activeuser_num from dwd_szxy_officedoc_unit_month_increment " +
                "where unit_id = '"+ UNIT_ID +"'";
        List<Json> stReceiveActiveList = mysqlClientService.getDataListFromMysql(null,null,stActiveSql,null,null);
        if (CollectionUtils.isNotEmpty(stReceiveActiveList)) {
            map.put("stReceiveActive", stReceiveActiveList.get(0).toJSONString());
        }

        // 收发文趋势
        String trendSql = "select concat(year,'-',month) as time, sum(send_month_num) as send_month_num, sum(receive_month_num) as receive_month_num, " +
                "substring(region_code,1,4) as region_code from dwd_szxy_officedoc_unit_month_increment where region_code like '"+ LIKE_CODE +"' and (";
        String stTrendSql = "select concat(year,'-',month) as time, send_month_num, receive_month_num from dwd_szxy_officedoc_unit_month_increment where ( ";
        for (int i=4;i>=0;i--) {
            if (i != 4) {
                trendSql += " or ";
                stTrendSql += "or ";
            }
            trendSql += "(year = "+ DateUtils.date2String(org.apache.commons.lang.time.DateUtils.addMonths(new Date(), -i),"yyyy") +" and month = "+ DateUtils.date2String(org.apache.commons.lang.time.DateUtils.addMonths(new Date(), -i),"MM") +") ";
            stTrendSql += "(year = "+ DateUtils.date2String(org.apache.commons.lang.time.DateUtils.addMonths(new Date(), -i),"yyyy") +" and month = "+ DateUtils.date2String(org.apache.commons.lang.time.DateUtils.addMonths(new Date(), -i),"MM") +") ";
        }
        trendSql += ")group by substring(region_code,1,4),time";
        stTrendSql += ") and unit_id = '" + UNIT_ID + "'";
        List<Json> trendList = mysqlClientService.getDataListFromMysql(null,null,trendSql,null,null);
        List<Json> stTrendList = mysqlClientService.getDataListFromMysql(null,null,stTrendSql,null,null);
        map.put("trendList", JsonArray.toJSON(trendList).toString());
        map.put("sTtrendList", JsonArray.toJSON(stTrendList).toString());

        // 查询各地区发文属性分布
        String sendDistributedSql = "select sum(send_extraurgent) as send_extraurgent, sum(send_urgent) as send_urgent, sum(send_commonly) as send_commonly," +
                "sum(send_type_resolution) as send_type_resolution, sum(send_type_decide) as send_type_decide, sum(send_type_command) as send_type_command," +
                "sum(send_type_publicreport) as send_type_publicreport, sum(send_type_pubnotice) as send_type_pubnotice, sum(send_type_announce) as send_type_announce," +
                "sum(send_type_view) as send_type_view, sum(send_type_notice) as send_type_notice, sum(send_type_bulletin) as send_type_bulletin," +
                "sum(send_type_report) as send_type_report, sum(send_type_requestinst) as send_type_requestinst, sum(send_type_approval) as send_type_approval," +
                "sum(send_type_bill) as send_type_bill, sum(send_type_letter) as send_type_letter, sum(send_type_summary) as send_type_summary," +
                "sum(send_type_instruct) as send_type_instruct, sum(send_type_rules) as send_type_rules, sum(send_type_regulations) as send_type_regulations," +
                "sum(send_type_briefing) as send_type_briefing, sum(send_type_other) as send_type_other, sum(send_prop_pereopen) as send_prop_pereopen," +
                "sum(send_prop_applipublic) as send_prop_applipublic, sum(send_prop_noapplipublic) as send_prop_noapplipublic, substring(region_code,1,4) as region_code from " +
                "dwd_szxy_officedoc_unit_month_increment where year = "+ year + " and month = "+ month + " and region_code like '"+ LIKE_CODE +"' group by substring(region_code,1,4)";
        List<Json> distributedList = mysqlClientService.getDataListFromMysql(null,null,sendDistributedSql,null,null);
        map.put("distributedList", JsonArray.toJSON(distributedList).toString());
        // 查询省厅发文属性分布
        String sendSTDistributedSql = "select send_extraurgent, send_urgent, send_commonly,send_type_resolution, send_type_decide, send_type_command, " +
                "send_type_publicreport, send_type_pubnotice, send_type_announce,send_type_view, send_type_notice, send_type_bulletin, " +
                "send_type_report, send_type_requestinst, send_type_approval, send_type_bill, send_type_letter, send_type_summary," +
                "send_type_instruct, send_type_rules, send_type_regulations, send_type_briefing, send_type_other, send_prop_pereopen," +
                "send_prop_applipublic, send_prop_noapplipublic from dwd_szxy_officedoc_unit_month_increment where " +
                "year = "+ year + " and month = "+ month + " and unit_id = '" + UNIT_ID + "'";
        List<Json> sendSTDistributedSqlList = mysqlClientService.getDataListFromMysql(null,null,sendSTDistributedSql,null,null);
        if (CollectionUtils.isNotEmpty(sendSTDistributedSqlList)) {
            map.put("sendSTDistributed",sendSTDistributedSqlList.get(0).toJSONString());
        }
        return "/bigdata/datav/fixed/xj/gwxx-index.ftl";
    }

    @ResponseBody
    @RequestMapping("/ranking")
    @ControllerInfo("排名")
    public Response ranking(Integer tabType, Integer documentType, String showRegionCode){
        String year = DateUtils.date2String(new Date(),"yyyy");
        String month = DateUtils.date2String(new Date(),"MM");
        try {
            String rankingSql = "";
            if (Objects.equals(tabType,1) && Objects.equals(documentType,1)) {
                rankingSql = "select unit_name as name,send_num as num from dwd_szxy_officedoc_unit_month_increment where year = "+ year +" and month = "+ month + " order by send_num desc limit 10";
            } else if (Objects.equals(tabType,1) && Objects.equals(documentType,2)) {
                rankingSql = "select unit_name as name,receive_num as num from dwd_szxy_officedoc_unit_month_increment where year = "+ year +" and month = "+ month + " order by receive_num desc limit 10";
            } else if (Objects.equals(tabType,2) && Objects.equals(documentType,1)) {
                rankingSql = "";
            } else if (Objects.equals(tabType,2) && Objects.equals(documentType,2)) {
                rankingSql = "";
            } else if (Objects.equals(tabType,3) && Objects.equals(documentType,1)) {
                rankingSql = "select unit_name as name,send_num as num from dwd_szxy_officedoc_unit_month_increment where year = "+ year +" and month = "+ month + " and region_code like '"+ showRegionCode.substring(0,4) +"%' order by send_num desc limit 10";
            } else if (Objects.equals(tabType,3) && Objects.equals(documentType,2)) {
                rankingSql = "select unit_name as name,receive_num as num from dwd_szxy_officedoc_unit_month_increment where year = "+ year +" and month = "+ month + " and region_code like '"+ showRegionCode.substring(0,4) +"%' order by receive_num desc limit 10";
            }
            List<Json> rankingList = Lists.newArrayList();
            if (StringUtils.isNotEmpty(rankingSql)) {
                rankingList = mysqlClientService.getDataListFromMysql(null,null,rankingSql,null,null);
            }
            return Response.ok().data(JSON.toJSONString(rankingList)).build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @ResponseBody
    @RequestMapping("/officedocnum")
    @ControllerInfo("公文量")
    public Response officedocNum(Integer tabType, Integer documentType, String showRegionCode){
        String year = DateUtils.date2String(new Date(),"yyyy");
        String month = DateUtils.date2String(new Date(),"MM");
        try {
            String sql = "";
            if (Objects.equals(tabType,1) && Objects.equals(documentType,1)) {
                sql = "select sum(send_num) as num, substring(region_code,1,4) as region_code from dwd_szxy_officedoc_unit_month_increment where " +
                        "region_code like '"+ LIKE_CODE +"' and year = "+ year +" and month = "+ month + " group by substring(region_code,1,4) order by num desc";
            } else if (Objects.equals(tabType,1) && Objects.equals(documentType,2)) {
                sql = "select sum(receive_num) as num, substring(region_code,1,4) as region_code from dwd_szxy_officedoc_unit_month_increment where " +
                        "region_code like '"+ LIKE_CODE +"' and year = "+ year +" and month = "+ month + " group by substring(region_code,1,4) order by num desc";
            } else if (Objects.equals(tabType,2) && Objects.equals(documentType,1)) {
                sql = "select send_num as num, dept_name, dept_id from dwc_szxy_officedoc_dept_month_increment where year = "+ year +
                        " and month = "+ month + " and unit_id = '"+ UNIT_ID +"' order by num desc ";
            } else if (Objects.equals(tabType,2) && Objects.equals(documentType,2)) {
                sql = "select receive_num as num, dept_name, dept_id from dwc_szxy_officedoc_dept_month_increment where year = "+ year +
                        " and month = "+ month + " and unit_id = '"+ UNIT_ID +"' order by num desc ";
            } else if (Objects.equals(tabType,3)) {
                if (Objects.equals(documentType,1)) {
                    sql = "select send_num as num, unit_name, unit_id from dwd_szxy_officedoc_unit_month_increment where year ="+ year +
                            " and month = "+ month + " and unit_class = '教育局' and  parent_id = (select unit_id from dwd_szxy_officedoc_unit_month_increment " +
                            "where year = "+ year +" and month = "+ month +" and unit_class = '教育局' and region_code = "+ showRegionCode +")";
                } else {
                    sql = "select receive_num as num, unit_name, unit_id from dwd_szxy_officedoc_unit_month_increment where year ="+ year +
                            " and month = "+ month + " and unit_class = '教育局' and  parent_id = (select unit_id from dwd_szxy_officedoc_unit_month_increment " +
                            "where year = "+ year +" and month = "+ month +" and unit_class = '教育局' and region_code = "+ showRegionCode +")";
                }
            }
            List<Json> officedocNumList = mysqlClientService.getDataListFromMysql(null,null,sql,null,null);
            return Response.ok().data(JSON.toJSONString(officedocNumList)).build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @ResponseBody
    @RequestMapping("/record")
    @ControllerInfo("流水列表")
    public Response runningWater(Integer tabType, Integer documentType, String id){
        try {
            String sql = "";
            if (Objects.equals(tabType,1) && Objects.equals(documentType,1)) {
                sql = "select send_time as time, unit_name, drafter_name as name, title from dwd_szxy_officedoc_send_increment where region_code like '"+ id.substring(0,4) +"%' order by send_time desc limit 10";
            } else if (Objects.equals(tabType,1) && Objects.equals(documentType,2)) {
                sql = "select sign_time as time, unit_name, sign_name as name, title from dwd_szxy_officedoc_receive_increment where region_code like '"+ id.substring(0,4) +"%' order by sign_time desc limit 10";
            } else if (Objects.equals(tabType,2) && Objects.equals(documentType,1)) {
                sql = "select send_time as time, unit_name, drafter_name as name, title from dwd_szxy_officedoc_send_increment where dept_id = '"+ id +"' order by send_time desc limit 10";
            } else if (Objects.equals(tabType,2) && Objects.equals(documentType,2)) {
                sql = "select sign_time as time, unit_name, sign_name as name, title from dwd_szxy_officedoc_receive_increment where dept_id = '"+ id +"' order by sign_time desc limit 10";
            } else if (Objects.equals(tabType,3) && Objects.equals(documentType,1)) {
                sql = "select send_time as time, unit_name, drafter_name as name, title from dwd_szxy_officedoc_send_increment where unit_id = '"+ id +"' order by send_time desc limit 10";
            } else if (Objects.equals(tabType,3) && Objects.equals(documentType,2)) {
                sql = "select sign_time as time, unit_name, sign_name as name, title from dwd_szxy_officedoc_receive_increment where unit_id = '"+ id +"' order by sign_time desc limit 10";
            }
            List<Json> recordList = mysqlClientService.getDataListFromMysql(null,null,sql,null,null);
            for (Json json : recordList) {
                if (json.getString("time") == null) {
                    json.putEx("time","无");
                } else {
                    json.putEx("time",DateUtils.stampToDate(json.getString("time")).substring(5,10));
                }
                if (json.getString("unit_name") == null) {
                    json.putEx("unit_name","无");
                }
                if (json.getString("name") == null) {
                    json.putEx("name","无");
                }
                if (json.getString("title") == null) {
                    json.putEx("title","无");
                }
            }
            return Response.ok().data(JSON.toJSONString(recordList)).build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }
}
