package net.zdsoft.bigdata.datav.action.fixed;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 日常办公数据监控大屏
 * @author yangkj
 * @since 2019/5/29 18:01
 */
@Controller
@RequestMapping("/bigdata/datav/fixed/screen")
public class FixedXJScreenRCBGAction {

    @Autowired
    private MysqlClientService mysqlClientService;

    @RequestMapping("/rcbg")
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

        //常用模块总览
        //近一月数据
        sql = "select sum(relenotice_num) as relenotice_num,sum(visitnotice_num) as visitnotice_num,sum(relebulletin_num) as relebulletin_num," +
                "sum(visitbulletin_num) as visitbulletin_num,sum(releeduinf_num) as releeduinf_num,sum(visiteduinf_num) as visiteduinf_num " +
                "from dwd_xj_szxy_office_dailyoff_unit_month_increment where year='"+year+"' and month='"+monthValue+"'";
        List<Json> commonModules30 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("commonModules30",checkNull(commonModules30)?null:commonModules30.get(0));

        //近三月数据
        sql = "select sum(relenotice_num) as relenotice_num,sum(visitnotice_num) as visitnotice_num,sum(relebulletin_num) as relebulletin_num," +
                "sum(visitbulletin_num) as visitbulletin_num,sum(releeduinf_num) as releeduinf_num,sum(visiteduinf_num) as visiteduinf_num " +
                "from dwd_xj_szxy_office_dailyoff_unit_month_increment where "+nearThreeMonth;
        List<Json> commonModules90 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("commonModules90",checkNull(commonModules90)?null:commonModules90.get(0));

        //全部数据
        sql = "select sum(relenotice_num) as relenotice_num,sum(visitnotice_num) as visitnotice_num,sum(relebulletin_num) as relebulletin_num," +
                "sum(visitbulletin_num) as visitbulletin_num,sum(releeduinf_num) as releeduinf_num,sum(visiteduinf_num) as visiteduinf_num " +
                "from dwd_xj_szxy_office_dailyoff_unit_month_increment";
        List<Json> commonModulesAll = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("commonModulesAll",checkNull(commonModulesAll)?null:commonModulesAll.get(0));

        //省厅通知公告阅读排名TOP10
        sql = "select sum(visitnotice_num) as visitnotice_num,sum(visitbulletin_num) as visitbulletin_num,unit_name from " +
                "dwd_xj_szxy_office_dailyoff_unit_month_increment where year='"+year+"' and month='"+monthValue+"' group by unit_name " +
                "order by sum(visitnotice_num+visitbulletin_num) desc limit 10";
        List<Json> noticeOrder30 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("noticeOrder30",noticeOrder30);
        sql = "select sum(visitnotice_num) as visitnotice_num,sum(visitbulletin_num) as visitbulletin_num,unit_name from " +
                "dwd_xj_szxy_office_dailyoff_unit_month_increment where "+nearThreeMonth+" group by unit_name " +
                "order by sum(visitnotice_num+visitbulletin_num) desc limit 10";
        List<Json> noticeOrder90 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("noticeOrder90",noticeOrder90);
        sql = "select sum(visitnotice_num) as visitnotice_num,sum(visitbulletin_num) as visitbulletin_num,unit_name from " +
                "dwd_xj_szxy_office_dailyoff_unit_month_increment group by unit_name " +
                "order by sum(visitnotice_num+visitbulletin_num) desc limit 10";
        List<Json> noticeOrderAll = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("noticeOrderAll",noticeOrderAll);

        //领导活动每日报排名TOP10
        sql = "select sum(visitleadactday_num) as visitleadactday_num,unit_name from dwd_xj_szxy_office_dailyoff_unit_month_increment " +
                "where year='"+year+"' and month='"+monthValue+"' group by unit_name order by sum(visitleadactday_num) desc limit 10";
        List<Json> leaderActivity30 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("leaderActivity30",leaderActivity30);
        sql = "select sum(visitleadactday_num) as visitleadactday_num,unit_name from dwd_xj_szxy_office_dailyoff_unit_month_increment " +
                "where "+nearThreeMonth+" group by unit_name order by sum(visitleadactday_num) desc limit 10";
        List<Json> leaderActivity90 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("leaderActivity90",leaderActivity90);
        sql = "select sum(visitleadactday_num) as visitleadactday_num,unit_name from dwd_xj_szxy_office_dailyoff_unit_month_increment group by unit_name order by sum(visitleadactday_num) desc limit 10";
        List<Json> leaderActivityAll = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("leaderActivityAll",leaderActivityAll);

        //各地区领导活动近一月每日报
        sql = "select substring(region_code,1,4) as region_code,sum(releleadactday_num) as releleadactday_num,sum(salary_num) as salary_num from " +
                "dwd_xj_szxy_office_dailyoff_unit_month_increment where year='"+year+"' and month='"+monthValue+"' group by substring(region_code,1,4)";
        List<Json> leadershipDaily = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        Map<String, Object[]> resultMap = getStringIntegerMap(leadershipDaily);
        map.put("leadershipDailyMap30",resultMap);

        //各地区领导活动近三月每日报
        sql = "select substring(region_code,1,4) as region_code,sum(releleadactday_num) as releleadactday_num,sum(salary_num) as salary_num from " +
                "dwd_xj_szxy_office_dailyoff_unit_month_increment where "+nearThreeMonth+" group by substring(region_code,1,4)";
        List<Json> leadershipDaily90 = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        Map<String, Object[]> resultMap90 = getStringIntegerMap(leadershipDaily90);
        map.put("leadershipDailyMap90",resultMap90);

        //各地区领导活动全部每日报
        sql = "select substring(region_code,1,4) as region_code,sum(releleadactday_num) as releleadactday_num,sum(salary_num) as salary_num from " +
                "dwd_xj_szxy_office_dailyoff_unit_month_increment group by substring(region_code,1,4)";
        List<Json> leadershipDailyAll = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        Map<String, Object[]> resultMapAll = getStringIntegerMap(leadershipDailyAll);
        map.put("leadershipDailyMapAll",resultMapAll);

        return "/bigdata/datav/fixed/xj/rcbg-index.ftl";
    }

    private Map<String, Object[]> getStringIntegerMap(List<Json> leadershipDaily) {
        Map<String,Object[]> resultMap = Maps.newLinkedHashMap();
        String areaName = "";
        for (Json json : leadershipDaily) {
            if (Objects.equals(json.getString("region_code"),"6501")) {
                areaName = "乌鲁木齐市";
            } else if (Objects.equals(json.getString("region_code"),"6502")) {
                areaName = "克拉玛依市";
            } else if (Objects.equals(json.getString("region_code"),"6521")) {
                areaName = "吐鲁番市";
            } else if (Objects.equals(json.getString("region_code"),"6522")) {
                areaName = "哈密市";
            } else if (Objects.equals(json.getString("region_code"),"6529")) {
                areaName = "阿克苏地区";
            } else if (Objects.equals(json.getString("region_code"),"6531")) {
                areaName = "喀什地区";
            } else if (Objects.equals(json.getString("region_code"),"6532")) {
                areaName = "和田地区";
            } else if (Objects.equals(json.getString("region_code"),"6523")) {
                areaName = "昌吉回族自治州";
            }else if (Objects.equals(json.getString("region_code"),"6527")) {
                areaName = "博尔塔拉蒙古自治州";
            }else if (Objects.equals(json.getString("region_code"),"6528")) {
                areaName = "巴音郭楞蒙古自治州";
            }else if (Objects.equals(json.getString("region_code"),"6530")) {
                areaName = "克孜勒苏柯尔克孜自治州";
            }else if (Objects.equals(json.getString("region_code"),"6540")) {
                areaName = "伊犁哈萨克自治州";
            }else if (Objects.equals(json.getString("region_code"),"6542")) {
                areaName = "塔城地区";
            }else if (Objects.equals(json.getString("region_code"),"6543")) {
                areaName = "阿勒泰地区";
            }else {
                continue;
            }
            resultMap.put(areaName,new Object[]{json.getInteger("releleadactday_num"),json.getInteger("salary_num")});
        }
        return resultMap;
    }

    public Boolean checkNull(List<Json> list){
        return list==null||list.isEmpty()||list.get(0).size()==0;
    }
}
