package net.zdsoft.bigdata.datav.action.fixed;

import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.framework.entity.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 公文消息数据监控大屏
 * @author yangkj
 * @since 2019/5/25 11:11
 */
@Controller
@RequestMapping("/bigdata/datav/fixed/screen")
public class FixedXJScreenOldGWXXAction {

    @Autowired
    private MysqlClientService mysqlClientService;

    @RequestMapping("/oldgwxx")
    public String execute(ModelMap map) {
        LocalDateTime localDateTime = LocalDateTime.now();
        String sql = "";
        //近一月
        String monthValue = localDateTime.getMonthValue()+"";
        if (monthValue.length()==1){
            monthValue = "0"+monthValue;
        }
        String year = localDateTime.getYear()+"";
        //近三月
        String nearThreeMonth = "DATE_FORMAT(CONCAT(YEAR,'-',MONTH,'-1'),'%Y-%m-%d')>=ADDDATE(DATE_FORMAT(CONCAT(YEAR(NOW()),'-',MONTH(NOW()),'-1'), '%Y-%m-%d'), INTERVAL -2 MONTH)";

        sql = "select send_month_num from dwd_szxy_officedoc_unit_month_increment where " +
                "year='"+year+"' and month='"+monthValue+"' and unit_id = '8AA1838525057EDA012505808B7D0000'";
        List<Json> xinjiangOfficedoc30 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        sql = "select sum(send_month_num) as send_num,sum(receive_month_num) as receive_num,convert(sum(receive_month_sign_num)/sum(receive_month_num),decimal(15,4)) as sign_num," +
                "convert(sum(signtime_interday_sum)/(sum(receive_month_num)-sum(receive_month_unsign_num)),decimal(15,2)) as average_num from dwd_szxy_officedoc_unit_month_increment where year='"+year+"' and month='"+monthValue+"'";
        List<Json> officedoc30 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);

        //获取近三月的公文数据总览

        sql = "select sum(send_month_num) as send_month_num from dwd_szxy_officedoc_unit_month_increment where " +nearThreeMonth+
                " and unit_id = '8AA1838525057EDA012505808B7D0000'";
        List<Json> xinjiangOfficedoc90 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        sql = "select sum(send_month_num) as send_num,sum(receive_month_num) as receive_num,convert(sum(receive_month_sign_num)/sum(receive_month_num),decimal(15,4)) as sign_num," +
                "convert(sum(signtime_interday_sum)/(sum(receive_month_num)-sum(receive_month_unsign_num)),decimal(15,2)) as average_num from dwd_szxy_officedoc_unit_month_increment where "+nearThreeMonth+"";
        List<Json> officedoc90 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);

        //获取全部的公文数据总览
        sql = "select send_num from dwd_szxy_officedoc_unit_month_increment where unit_id = '8AA1838525057EDA012505808B7D0000'";
        List<Json> xinjiangOfficedocAll = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        sql = "select sum(send_month_num) as send_num,sum(receive_month_num) as receive_num,convert(sum(receive_month_sign_num)/sum(receive_month_num),decimal(15,4)) as sign_num," +
                "convert(sum(signtime_interday_sum)/(sum(receive_month_num)-sum(receive_month_unsign_num)),decimal(15,2)) as average_num from dwd_szxy_officedoc_unit_month_increment";
        List<Json> officedocAll = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);

        map.put("xinjiangOfficedoc30",checkNull(xinjiangOfficedoc30)?null:xinjiangOfficedoc30.get(0));
        map.put("officedoc30",checkNull(officedoc30)?null:officedoc30.get(0));
        map.put("xinjiangOfficedoc90",checkNull(xinjiangOfficedoc90)?null:xinjiangOfficedoc90.get(0));
        map.put("officedoc90",checkNull(officedoc90)?null:officedoc90.get(0));
        map.put("xinjiangOfficedocAll",checkNull(xinjiangOfficedocAll)?null:xinjiangOfficedocAll.get(0));
        map.put("officedocAll",checkNull(officedocAll)?null:officedocAll.get(0));

        //发文属性分布
        //发文类型
        sql="select sum(send_type_resolution) as 决议,sum(send_type_decide) as 决定,sum(send_type_command) as 命令,sum(send_type_publicreport) as 公报," +
                "sum(send_type_pubnotice) as 公告,sum(send_type_announce) as 通告,sum(send_type_view) as 意见,sum(send_type_notice) as 通知," +
                "sum(send_type_bulletin) as 通报,sum(send_type_report) as 报告,sum(send_type_requestinst) as 请示,sum(send_type_approval) as 批复," +
                "sum(send_type_bill) as 议案,sum(send_type_letter) as 函,sum(send_type_summary) as 纪要,sum(send_type_instruct) as 指示,sum(send_type_rules) as 条例," +
                "sum(send_type_regulations) as 规定,sum(send_type_briefing) as 简报,sum(send_type_other) as 其他 from dwd_szxy_officedoc_unit_month_increment";
        List<Json> sendType = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        ArrayList<GwxxDto> sendTypeList = new ArrayList<>();
        if (!checkNull(sendType)){
            for(Object orderMap:sendType.get(0).entrySet()){
                GwxxDto gwxxDto = new GwxxDto();
                gwxxDto.setName(((Map.Entry) orderMap).getKey().toString());
                gwxxDto.setValue(Integer.parseInt(((Map.Entry) orderMap).getValue().toString()));
                sendTypeList.add(gwxxDto);
            }
        }
        //降序排序
        sendTypeList.sort((x,y)->y.getValue()-x.getValue());
        map.put("sendTypeLists",sendTypeList.stream().limit(5).collect(Collectors.toList()));
        map.put("sendTypeOther",sendTypeList.stream().skip(5).map(x ->x.getValue())
                .collect(Collectors.summarizingInt(x -> x)).getSum());
        //公开属性
        sql = "select sum(send_prop_pereopen) as send_prop_pereopen,sum(send_prop_applipublic) as send_prop_applipublic" +
                ",sum(send_prop_noapplipublic) as send_prop_noapplipublic from dwd_szxy_officedoc_unit_month_increment";
        List<Json> sendProp = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("sendProp",checkNull(sendProp)?null:sendProp.get(0));

        //紧急程度
        sql = "select sum(send_month_num-send_extraurgent-send_urgent-send_commonly) as teti,sum(send_extraurgent) as send_extraurgent," +
                "sum(send_urgent) as send_urgent,sum(send_commonly) as send_commonly from dwd_szxy_officedoc_unit_month_increment";
        List<Json> urgencyDegree = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("urgencyDegree",checkNull(urgencyDegree)?null:urgencyDegree.get(0));

        //公文收发情况
        //省厅各部门近一月公文收发情况
        sql = "select sum(send_month_num) as send_num,sum(receive_month_num) as receive_num,(sum(send_month_num)+sum(receive_month_num)) as all_num,max(dept_short_name) as dept_short_name from dwc_szxy_officedoc_dept_month_increment where dept_id in" +
                "(select dept_id from dwc_szxy_officedoc_dept_month_increment where unit_id = '8AA1838525057EDA012505808B7D0000') " +
                "and year='"+year+"' and month='"+monthValue+"' group by dept_id order by all_num desc";
        List<Json> xinjiangDept30 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("xinjiangDept30",xinjiangDept30.stream().limit(15).collect(Collectors.toList()));
        //省厅各部门近三月公文收发情况
        sql = "select sum(send_month_num) as send_num,sum(receive_month_num) as receive_num,(sum(send_month_num)+sum(receive_month_num)) as all_num,max(dept_short_name) as dept_short_name  from dwc_szxy_officedoc_dept_month_increment where dept_id in" +
                "(select dept_id from dwc_szxy_officedoc_dept_month_increment where unit_id = '8AA1838525057EDA012505808B7D0000') and "+nearThreeMonth+" group by dept_id order by all_num desc";
        List<Json> xinjiangDept90 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("xinjiangDept90",xinjiangDept90.stream().limit(15).collect(Collectors.toList()));

        //省厅各部门全部公文收发情况
        sql = "select sum(send_month_num) as send_num,sum(receive_month_num) as receive_num,(sum(send_month_num)+sum(receive_month_num)) as all_num,max(dept_short_name) as dept_short_name  from dwc_szxy_officedoc_dept_month_increment where dept_id in" +
                "(select dept_id from dwc_szxy_officedoc_dept_month_increment where unit_id = '8AA1838525057EDA012505808B7D0000') group by dept_id order by all_num desc";
        List<Json> xinjiangDeptAll = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("xinjiangDeptAll",xinjiangDeptAll.stream().limit(15).collect(Collectors.toList()));

        //各地区公文收发情况
        //公文收发情况近一月
        sql = "select sum(send_month_num) as send_num,sum(receive_month_num) as receive_num,region_name " +
                "from dwd_szxy_officedoc_unit_month_increment where region_code in ('650100','650200','652100','652200','652900'," +
                "'653100','653200','652300','652700','652800','653000','654000','654200','654300') " +
                "and year='"+year+"' and month='"+monthValue+"' group by region_name";
        List<Json> xinjiangArea30 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("xinjiangArea30",xinjiangArea30);
        //公文收发情况近三月
        sql = "select sum(send_month_num) as send_num,sum(receive_month_num) as receive_num,region_name " +
                "from dwd_szxy_officedoc_unit_month_increment where region_code in ('650100','650200','652100','652200','652900'," +
                "'653100','653200','652300','652700','652800','653000','654000','654200','654300') and "+nearThreeMonth+" group by region_name";
        List<Json> xinjiangArea90 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("xinjiangArea90",xinjiangArea90);

        //公文收发情况全部
        sql = "select sum(send_month_num) as send_num,sum(receive_month_num) as receive_num,region_name " +
                "from dwd_szxy_officedoc_unit_month_increment where region_code in ('650100','650200','652100','652200','652900'," +
                "'653100','653200','652300','652700','652800','653000','654000','654200','654300') group by region_name";
        List<Json> xinjiangAreaAll = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("xinjiangAreaAll",xinjiangAreaAll);

        //消息收发情况
        //省厅各部门近一月消息收发情况
        sql = "select sum(send_month_num) as send_num,sum(receive_month_num) as receive_num,(sum(send_month_num)+sum(receive_month_num)) as all_num,max(dept_short_name) as dept_short_name  from dwc_szxy_office_msg_dept_month_increment where dept_id in" +
                "(select dept_id from dwc_szxy_office_msg_dept_month_increment where unit_id = '8AA1838525057EDA012505808B7D0000') " +
                "and year='"+year+"' and month='"+monthValue+"' group by dept_id order by all_num desc";
        List<Json> xinjiangDeptMsg30 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("xinjiangDeptMsg30",xinjiangDeptMsg30.stream().limit(15).collect(Collectors.toList()));
        //省厅各部门近三月消息收发情况
        sql = "select sum(send_month_num) as send_num,sum(receive_month_num) as receive_num,(sum(send_month_num)+sum(receive_month_num)) as all_num,max(dept_short_name) as dept_short_name  from dwc_szxy_office_msg_dept_month_increment where dept_id in" +
                "(select dept_id from dwc_szxy_office_msg_dept_month_increment where unit_id = '8AA1838525057EDA012505808B7D0000') and "+nearThreeMonth+" group by dept_id order by all_num desc";
        List<Json> xinjiangDeptMsg90 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("xinjiangDeptMsg90",xinjiangDeptMsg90.stream().limit(15).collect(Collectors.toList()));
        //省厅各部门全部消息收发情况
        sql = "select sum(send_month_num) as send_num,sum(receive_month_num) as receive_num,(sum(send_month_num)+sum(receive_month_num)) as all_num,max(dept_short_name) as dept_short_name  from dwc_szxy_office_msg_dept_month_increment where dept_id in" +
                "(select dept_id from dwc_szxy_office_msg_dept_month_increment where unit_id = '8AA1838525057EDA012505808B7D0000') group by dept_id order by all_num desc";
        List<Json> xinjiangDeptMsgAll = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("xinjiangDeptMsgAll",xinjiangDeptMsgAll.stream().limit(15).collect(Collectors.toList()));

        //各地区消息收发情况
        //消息收发情况近一月
        sql = "select sum(send_month_num) as send_num,sum(receive_month_num) as receive_num,region_name " +
                "from dwd_szxy_office_msg_unit_month_increment where region_code in ('650100','650200','652100','652200','652900'," +
                "'653100','653200','652300','652700','652800','653000','654000','654200','654300') " +
                "and year='"+year+"' and month='"+monthValue+"' group by region_name";
        List<Json> xinjiangAreaMsg30 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("xinjiangAreaMsg30",xinjiangAreaMsg30);
        //消息收发情况近三月
        sql = "select sum(send_month_num) as send_num,sum(receive_month_num) as receive_num,region_name " +
                "from dwd_szxy_office_msg_unit_month_increment where region_code in ('650100','650200','652100','652200','652900'," +
                "'653100','653200','652300','652700','652800','653000','654000','654200','654300') and "+nearThreeMonth+" group by region_name";
        List<Json> xinjiangAreaMsg90 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("xinjiangAreaMsg90",xinjiangAreaMsg90);
        //消息收发情况全部
        sql = "select sum(send_month_num) as send_num,sum(receive_month_num) as receive_num,region_name " +
                "from dwd_szxy_office_msg_unit_month_increment where region_code in ('650100','650200','652100','652200','652900'," +
                "'653100','653200','652300','652700','652800','653000','654000','654200','654300') group by region_name";
        List<Json> xinjiangAreaMsgAll = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("xinjiangAreaMsgAll",xinjiangAreaMsgAll);

        //获取近一月的消息数据总览
        sql = "select send_month_num from dwd_szxy_office_msg_unit_month_increment where " +
                "year='"+year+"' and month='"+monthValue+"' and unit_id = '8AA1838525057EDA012505808B7D0000'";
        List<Json> xinjiangMsg30 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        sql = "select sum(send_month_num) as send_num,sum(receive_month_num) as receive_num,convert(sum(receive_month_read_num)/sum(unread_num+receive_month_read_num),decimal(15,4)) as read_num," +
                "sum(send_actuser_num+receive_actuser_num) as actuser_num from dwd_szxy_office_msg_unit_month_increment where year='"+year+"' and month='"+monthValue+"'";
        List<Json> msg30 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);

        //获取近三月的消息数据总览
        sql = "select sum(send_month_num) as send_month_num from dwd_szxy_office_msg_unit_month_increment where "+nearThreeMonth+" and unit_id = '8AA1838525057EDA012505808B7D0000'";
        List<Json> xinjiangMsg90 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        sql = "select sum(send_month_num) as send_num,sum(receive_month_num) as receive_num,convert(sum(receive_month_read_num)/sum(unread_num+receive_month_read_num),decimal(15,4)) as read_num," +
                "sum(send_actuser_num+receive_actuser_num) as actuser_num from dwd_szxy_office_msg_unit_month_increment where "+nearThreeMonth+"";
        List<Json> msg90 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);

        //获取全部的消息数据总览
        sql = "select send_num from dwd_szxy_office_msg_unit_month_increment where unit_id = '8AA1838525057EDA012505808B7D0000'";
        List<Json> xinjiangMsgAll = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        sql = "select sum(send_month_num) as send_num,sum(receive_month_num) as receive_num,convert(sum(receive_month_read_num)/sum(unread_num+receive_month_read_num),decimal(15,4)) as read_num," +
                "sum(send_actuser_num+receive_actuser_num) as actuser_num from dwd_szxy_office_msg_unit_month_increment";
        List<Json> msgAll = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("xinjiangMsg30",checkNull(xinjiangMsg30)?null:xinjiangMsg30.get(0));
        map.put("msg30",checkNull(msg30)?null:msg30.get(0));
        map.put("xinjiangMsg90",checkNull(xinjiangMsg90)?null:xinjiangMsg90.get(0));
        map.put("msg90",checkNull(msg90)?null:msg90.get(0));
        map.put("xinjiangMsgAll",checkNull(xinjiangMsgAll)?null:xinjiangMsgAll.get(0));
        map.put("msgAll",checkNull(msgAll)?null:msgAll.get(0));

        //消息属性分布
        //紧急程度
        sql = "select sum(msg_commonly) as msg_commonly,sum(msg_emergency) as msg_emergency from dwd_szxy_office_msg_unit_month_increment";
        List<Json> msgProp = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("msgProp",checkNull(msgProp)?null:msgProp.get(0));
        //是否代办
        sql = "select sum(agency_yes) as agency_yes,sum(agency_no) as agency_no from dwd_szxy_office_msg_unit_month_increment";
        List<Json> msgAgency = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("msgAgency",checkNull(msgAgency)?null:msgAgency.get(0));

        //地图
        //乌鲁木齐市
        map.put("WLMQ",sumNum("6501"));
        //克拉玛依市
        map.put("KLMY",sumNum("6502"));
        //吐鲁番市
        map.put("TLF",sumNum("6521"));
        //哈密市
        map.put("HM",sumNum("6522"));
        //阿克苏地区
        map.put("AKS",sumNum("6529"));
        //喀什地区
        map.put("KS",sumNum("6531"));
        //和田地区
        map.put("HT",sumNum("6532"));
        //昌吉回族自治州
        map.put("CJHZ",sumNum("6523"));
        //博尔塔拉蒙古自治州
        map.put("BETL",sumNum("6527"));
        //巴音郭楞蒙古自治州
        map.put("BYGL",sumNum("6528"));
        //克孜勒苏柯尔克孜自治州
        map.put("KZLS",sumNum("6530"));
        //伊犁哈萨克自治州
        map.put("YLHSK",sumNum("6540"));
        //塔城地区
        map.put("TC",sumNum("6542"));
        //阿勒泰地区
        map.put("ALT",sumNum("6543"));

        return "/bigdata/datav/fixed/xj/oldgwxx-index.ftl";
    }

    public Object[] sumNum(String regionCode){

        String sql = "select sum(send_num+receive_num) as num from dwd_szxy_officedoc_unit_month_increment where region_code like '"+regionCode+"%'";
        List<Json>  officeNum = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        sql = "select sum(send_num+receive_num) as num from dwd_szxy_office_msg_unit_month_increment where region_code like '"+regionCode+"%'";
        List<Json>  msgNum = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);


        long num1=officeNum.get(0).size()==0?0:officeNum.stream().map(x->(Integer)x.get("num")).collect(Collectors.summarizingInt(x -> x)).getSum();
        long num2=msgNum.get(0).size()==0?0:msgNum.stream().map(x->(Integer)x.get("num")).collect(Collectors.summarizingInt(x -> x)).getSum();

        Object[] objects = {num1,num2,num1+num2};


        return objects;
    }

    public Boolean checkNull(List<Json> list){
        return list==null||list.isEmpty()||list.get(0).size()==0;
    }
}
