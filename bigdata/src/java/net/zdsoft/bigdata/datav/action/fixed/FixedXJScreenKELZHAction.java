package net.zdsoft.bigdata.datav.action.fixed;

import com.alibaba.fastjson.JSON;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.framework.entity.Json;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库尔勒综合大屏
 * @author yangkj
 * @since 2019/6/24 13:59
 */
@Controller
@RequestMapping("/bigdata/datav/fixed/screen")
public class FixedXJScreenKELZHAction {

    @Autowired
    private MysqlClientService mysqlClientService;

    @RequestMapping(value = "/kelzh",method = RequestMethod.GET)
    public String execute(ModelMap map) {
        String year = getString();
        String sql  = "select unit_id from bg_dim_unit where parent_id = '00000000000000000000000000000000'";
        List<Json> searchUnitId = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        String unitId =  searchUnitId.get(0).get("unit_id").toString();
        sql = getTeacherAgeSql(unitId,year);
        List<Json> teacherAge = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        sql = "select sum(student_number) as student_num,sum(class_number) as class_num,round(sum(student_number)/sum(class_number)) as aveclass_peop_num," +
                "round(sum(schoolhouse_area)/sum(student_number)) as aveclass_instrument_num,round(sum(student_sex_1_num)/sum(student_number),2) as man," +
                "sum(student_mz_01_num) as student_mz_01_num,sum(student_mz_other_num) as student_mz_other_num from dwd_szxy_unit_month_full where parent_id = " +
                "(select unit_id from bg_dim_unit where parent_id = '00000000000000000000000000000000') and year = '"+year+"'";
        List<Json> studentData = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        sql = getTeacherDataSql(unitId,year);
        List<Json> teacherData = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        sql = "select devicetype_content,sum(instrument_count) as instrument_count from dwd_szxy_device_year_full where parent_id = (select unit_id from bg_dim_unit where parent_id = '00000000000000000000000000000000') and year = '"+year+"' group by devicetype_content desc";
        List<Json> deviceData = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        //判断学年学段
        LocalDateTime localDateTime = LocalDateTime.now();
        int monthValue = localDateTime.getMonthValue();
        String sectionYear = "";
        int semester = 0;
        if (monthValue<=8){
            sectionYear = (localDateTime.getYear()-1)+"-"+localDateTime.getYear();
            semester = 2;
        }else {
            sectionYear = localDateTime.getYear()+"-"+(localDateTime.getYear()+1);
            semester = 1;
        }
        sql = "select parent_id as unit_id,sum(teacher_num) as teacher_num,sum(student_num) as student_num,subject_name from dwc_szxy_course_semester_increment where parent_id = '"+unitId+"' and section like '%1%' and acadyear='"+sectionYear+"' and semester='"+semester+"' group by subject_name order by teacher_num desc limit 9";
        List<Json> courseData = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        map.put("teacherAge",JSON.toJSONString(teacherAge));
        map.put("studentData",JSON.toJSONString(studentData));
        map.put("teacherData",JSON.toJSONString(teacherData));
        map.put("deviceData",JSON.toJSONString(deviceData));
        map.put("courseData",JSON.toJSONString(courseData));
        return "/bigdata/datav/fixed/xj/kelzh.ftl";
    }

    private String getTeacherDataSql(String id,String year) {
        String sql;
        sql = "select sum(teacher_number) as teacher_number,round(sum(teacher_sex_1_num)/sum(teacher_number),2) as man,sum(teacher_mz_01_num) as teacher_mz_01_num,sum(teacher_mz_other_num) as teacher_mz_other_num from dwd_szxy_unit_month_full where parent_id = '"+id+"' and year = '"+year+"'";
        return sql;
    }

    private String getTeacherAgeSql(String id,String year) {
        return "select sum(workyear_5) as workyear_5,sum(workyear_6_10) as workyear_6_10,sum(workyear_11_15) as workyear_11_15,sum(workyear_16_20) as workyear_16_20,sum(workyear_21_25) as workyear_21_25,sum(workyear_26_30) as workyear_26_30,sum(workyear_31_35) as workyear_31_35," +
                    "sum(workyear_36_40) as workyear_36_40,sum(workyear_41_45) as workyear_41_45,sum(workyear_46) as workyear_46,sum(age_24) as age_24,sum(age_25_29) as age_25_29,sum(age_30_34) as age_30_34,sum(age_35_39) as age_35_39,sum(age_40_44) as age_40_44,sum(age_45_49) as age_45_49," +
                    "sum(age_50_54) as age_50_54,sum(age_55_59) as age_55_59,sum(age_60_64) as age_60_64,sum(age_65) as age_65 from dmd_szxy_teacher_unit_year_increment where parent_id = '"+id+"' and year = '"+ year +"'";
    }

    private String getString() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.getYear() + "";
    }

    @RequestMapping(value = "/kelData",method = RequestMethod.GET)
    @ResponseBody
    public Response kelData(@RequestParam("id") String id){
        Map<String, List<Json>> listMap = new HashMap<>();
        /**
         * 时间:2019/6/26
         * 库尔勒暂时只有一个市教育局，为了满足后期出现多个教育局的情况
         * 区分教育局和学校的查询条件
         */
        String year = getString();
        String sql = "select unit_class from bg_dim_unit where unit_id = '"+id+"'";
        List<Json> unitType = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        Integer unitClass = (Integer) unitType.get(0).get("unit_class");
        sql = "select latitude,longitude,unit_name from bg_dim_unit where unit_id = '"+id+"'";
        List<Json> latiAndlongit = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        if (unitClass==1){
            sql = getTeacherAgeSql(id,year);
        }else {
            sql = "select * from dmd_szxy_teacher_unit_year_increment where unit_id = '"+id+"' and year = '"+year+"'";
        }
        List<Json> teacherAge = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        sql = "select sum(student_number) as student_num,sum(class_number) as class_num,round(sum(student_number)/sum(class_number)) as aveclass_peop_num," +
                "round(sum(schoolhouse_area)/sum(student_number)) as aveclass_instrument_num,round(sum(student_sex_1_num)/sum(student_number),2) as man," +
                "sum(student_mz_01_num) as student_mz_01_num,sum(student_mz_other_num) as student_mz_other_num from dwd_szxy_unit_month_full where year = '"+year+"' and ";
        if (unitClass==1){
            sql += "parent_id = (select unit_id from bg_dim_unit where parent_id = '00000000000000000000000000000000')";
        }else {
            sql += "unit_id = '"+id+"'";
        }
        List<Json> studentData = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        if (unitClass==1){
            sql = getTeacherDataSql(id,year);
        }else {
            sql = "select teacher_number,round(teacher_sex_1_num/teacher_number,2) as man,teacher_mz_01_num,teacher_mz_other_num from dwd_szxy_unit_month_full where unit_id = '"+id+"' and year = '"+year+"'";
        }
        List<Json> teacherData = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        if (unitClass==1){
            sql = "select devicetype_content,sum(instrument_count) as instrument_count from dwd_szxy_device_year_full where parent_id = '"+id+"' and year = '"+year+"' group by devicetype_content desc";
        }else {
            sql = "select devicetype_content,sum(instrument_count) as instrument_count from dwd_szxy_device_year_full where unit_id = '"+id+"' and year = '"+year+"' group by devicetype_content desc";
        }
        List<Json> deviceData = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        //判断学年学段
        LocalDateTime localDateTime = LocalDateTime.now();
        int monthValue = localDateTime.getMonthValue();
        String sectionYear = "";
        int semester = 0;
        if (monthValue<=8){
            sectionYear = (localDateTime.getYear()-1)+"-"+localDateTime.getYear();
            semester = 2;
        }else {
            sectionYear = localDateTime.getYear()+"-"+(localDateTime.getYear()+1);
            semester = 1;
        }
        if (unitClass==1){
            sql = "select parent_id as unit_id,sum(teacher_num) as teacher_num,sum(student_num) as student_num,subject_name from dwc_szxy_course_semester_increment where parent_id = '"+id+"' and section like '%1%' and acadyear='"+sectionYear+"' and semester='"+semester+"' group by subject_name order by teacher_num desc limit 9";
        }else {
            sql = "select unit_id,teacher_num,student_num,subject_name from dwc_szxy_course_semester_increment where unit_id = '"+id+"' and section like '%1%' and acadyear='"+sectionYear+"' and semester='"+semester+"' order by teacher_num desc limit 9";
        }
        List<Json> courseData = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        listMap.put("teacherAge",teacherAge);
        listMap.put("studentData",studentData);
        listMap.put("teacherData",teacherData);
        listMap.put("deviceData",deviceData);
        listMap.put("courseData",courseData);
        listMap.put("latiAndlongit",latiAndlongit);
        return Response.ok().data(listMap).build();
    }

    @RequestMapping(value = "/searchSchoolName",method = RequestMethod.GET)
    @ResponseBody
    public Response searchSchoolName(@RequestParam("name") String name){
        if ("".equals(name)){
            return Response.ok().data(new ArrayList<>()).build();
        }
        String sql = "select * from bg_dim_unit where unit_name like '%"+name+"%' and region_code like '6528%'";
        List<Json> searchSchoolName = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        if (CollectionUtils.isEmpty(searchSchoolName)){
            return Response.error().build();
        }
        return Response.ok().data(searchSchoolName).build();
    }

    @RequestMapping(value = "/teacherNumber",method = RequestMethod.POST)
    @ResponseBody
    public Response teacherNumber(@RequestParam("unitId") String unitId,@RequestParam("section") Integer section){
        if (StringUtils.isEmpty(unitId)){
            return Response.error().build();
        }
        String sql = "select unit_class from bg_dim_unit where unit_id = '"+unitId+"'";
        List<Json> unitType = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        Integer unitClass = (Integer) unitType.get(0).get("unit_class");
        //判断学年学段
        LocalDateTime localDateTime = LocalDateTime.now();
        int monthValue = localDateTime.getMonthValue();
        String sectionYear = "";
        int semester = 0;
        if (monthValue<=8){
            sectionYear = (localDateTime.getYear()-1)+"-"+localDateTime.getYear();
            semester = 2;
        }else {
            sectionYear = localDateTime.getYear()+"-"+(localDateTime.getYear()+1);
            semester = 1;
        }
        if (unitClass==1){
            sql = "select parent_id as unit_id,sum(teacher_num) as teacher_num,sum(student_num) as student_num,subject_name from dwc_szxy_course_semester_increment where parent_id = '"+unitId+"' and section like '%"+section+"%' and acadyear='"+sectionYear+"' and semester='"+semester+"' group by subject_name order by teacher_num desc limit 9";
        }else {
            sql = "select unit_id,teacher_num,student_num,subject_name from dwc_szxy_course_semester_increment where unit_id = '"+unitId+"' and section like '%"+section+"%' and acadyear='"+sectionYear+"' and semester='"+semester+"' order by teacher_num desc limit 9";
        }
        List<Json> courseData = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
        if (CollectionUtils.isEmpty(courseData)){
            return Response.error().build();
        }
        return Response.ok().data(courseData).build();
    }
}
