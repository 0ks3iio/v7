package net.zdsoft.bigdata.frame.data.demo.controller;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.bigdata.frame.data.impala.ImpalaClientService;
import net.zdsoft.bigdata.frame.data.kylin.KylinClientService;
import net.zdsoft.framework.entity.Json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/bigdata/frame/common/demo")
@Controller
public class KylinDemoController {

	@Autowired
	KylinClientService kylinClientService;
	
	@Autowired
	ImpalaClientService impalaClientService;

	@RequestMapping("/kylin")
	public String query(ModelMap map) {

				List<Json> resultFieldList = new ArrayList<Json>();
				Json fieldJson = new Json();
				fieldJson.put("field", "acadyear");
				fieldJson.put("dataType", "string");
				resultFieldList.add(fieldJson);
				
				Json fieldJson1 = new Json();
				fieldJson1.put("field", "semester");
				fieldJson1.put("dataType", "string");
				resultFieldList.add(fieldJson1);
				
				Json fieldJson2 = new Json();
				fieldJson2.put("field", "count");
				fieldJson2.put("dataType", "int");
				resultFieldList.add(fieldJson2);

				String sql = "select acadyear as acadyear,semester as semester,sum(question_num) as count from ik_homework_stat group by acadyear,semester";

				List<Json> paramList = new ArrayList<Json>();
				List<Json> kylinList = impalaClientService.getDataListFromImpala(
						"test", sql, paramList, resultFieldList);
				map.put("kylinList", kylinList);
				return "/bigdata/demo/kylinQuery1.ftl";
	}
	
	
//	@RequestMapping("/kylin")
//	public String query(ModelMap map) {
//
//				List<Json> resultFieldList = new ArrayList<Json>();
//				Json fieldJson = new Json();
//				fieldJson.put("field", "key");
//				fieldJson.put("dataType", "string");
//				resultFieldList.add(fieldJson);
////				Json fieldJson1 = new Json();
////				fieldJson1.put("field", "name");
////				fieldJson1.put("dataType", "string");
////				resultFieldList.add(fieldJson1);
//				// String sex_sql =
//				// "select sex as showName,count(1) as stuCount from student_profile "
//				// + " group by sex ";
//				// String region_sql =
//				// "select region as showName,count(1) as stuCount from student_profile "
//				// + " group by region ";
//				// String school_type_sql =
//				// "select  school_type as showName,count(1) as stuCount from student_profile "
//				// + " group by school_type ";
//				// String family_sql =
//				// "select  family_background as showName,count(1) as stuCount from student_profile "
//				// + " group by family_background ";
//				// String interest_sql =
//				// "select  interest as showName,count(1) as stuCount from student_profile "
//				// + " group by interest ";
//
//				String sex_sql = "select distinct nation as key  from kylin_student_stat where nation <> 'null' ";
//
//				List<Json> paramList = new ArrayList<Json>();
//				List<Json> kylinList = kylinClientService.getDataListFromKylin(
//						"multi_model_stat", sex_sql, paramList, resultFieldList);
//				map.put("kylinList", kylinList);
//				return "/bigdata/demo/kylinQuery1.ftl";
//	}

	@ResponseBody
	@RequestMapping("/kylin/data")
	public String getKylinData() {
		List<Json> resultFieldList = new ArrayList<Json>();
		Json fieldJson = new Json();
		fieldJson.put("field", "grade_name");
		fieldJson.put("dataType", "string");
		resultFieldList.add(fieldJson);

		Json fieldJson1 = new Json();
		fieldJson1.put("field", "sex");
		fieldJson1.put("dataType", "string");
		resultFieldList.add(fieldJson1);

		Json fieldJson2 = new Json();
		fieldJson2.put("field", "age");
		fieldJson2.put("dataType", "string");
		resultFieldList.add(fieldJson2);

		Json fieldJson3 = new Json();
		fieldJson3.put("field", "stuCount");
		fieldJson3.put("dataType", "string");
		resultFieldList.add(fieldJson3);

		String sql = "select grade_name as grade_name,grade_code,sex as sex,age as age,count(1) as stuCount from kylin_student_stat group by grade_name,grade_code,sex,age order by grade_code,sex,CAST(age as int)";

		List<Json> paramList = new ArrayList<Json>();

		//必须选择
		List<Json> rowDimensionList = new ArrayList<Json>();
		//可选项
		List<Json> columnDimensionList = new ArrayList<Json>();
		//必须选择
		List<Json> indexList = new ArrayList<Json>();

		Json json1 = new Json();
		json1.put("key", "grade_name");
		json1.put("name", "年级");
		rowDimensionList.add(json1);

		Json json2 = new Json();
		json2.put("key", "sex");
		json2.put("name", "性别");
		rowDimensionList.add(json2);

		Json json3 = new Json();
		json3.put("key", "age");
		json3.put("name", "年龄");
		rowDimensionList.add(json3);

		Json json4 = new Json();
		json4.put("key", "stuCount");
		json4.put("name", "人数");
		indexList.add(json4);
		String result = kylinClientService.getDataListFromKylin("student_stat",
				sql, paramList, resultFieldList, rowDimensionList,
				columnDimensionList, indexList);

		return result;
	}

	@RequestMapping("/kylin1")
	public String query1(ModelMap map) {
		// List<Json> resultFieldList = new ArrayList<Json>();
		// Json fieldJson=new Json();
		// fieldJson.put("field", "nationName");
		// fieldJson.put("dataType", "string");
		// resultFieldList.add(fieldJson);
		// Json fieldJson1=new Json();
		// fieldJson1.put("field", "stuCount");
		// fieldJson1.put("dataType", "string");
		// resultFieldList.add(fieldJson1);
		// String sql =
		// "select bdn.nation_name as nationName,bdn.nation_code,count(bs.nation_id) as stuCount from bg_student bs join bg_dim_nation bdn on bs.nation_id=bdn.nation_id "
		// + " group by bdn.nation_name,bdn.nation_code "
		// + " order by bdn.nation_code";

		List<Json> resultFieldList = new ArrayList<Json>();
		Json fieldJson = new Json();
		fieldJson.put("field", "showName");
		fieldJson.put("dataType", "string");
		resultFieldList.add(fieldJson);
		Json fieldJson1 = new Json();
		fieldJson1.put("field", "stuCount");
		fieldJson1.put("dataType", "string");
		resultFieldList.add(fieldJson1);
		// String sex_sql =
		// "select sex as showName,count(1) as stuCount from student_profile "
		// + " group by sex ";
		// String region_sql =
		// "select region as showName,count(1) as stuCount from student_profile "
		// + " group by region ";
		// String school_type_sql =
		// "select  school_type as showName,count(1) as stuCount from student_profile "
		// + " group by school_type ";
		// String family_sql =
		// "select  family_background as showName,count(1) as stuCount from student_profile "
		// + " group by family_background ";
		// String interest_sql =
		// "select  interest as showName,count(1) as stuCount from student_profile "
		// + " group by interest ";

		String sex_sql = "select sex as showName,count(1) as stuCount from kylin_student_stat "
				+ " group by sex ";
		String age_sql = "sselect age as showName,count(1) as stuCount from kylin_student_stat group by age order by CAST(age as int)";

		String nation_sql = "select nation as showName,nation_code, count(1) as stuCount from kylin_student_stat group by nation,nation_code order by nation_code";

		String location_sql = "select  location as showName,count(1) as stuCount from kylin_student_stat "
				+ " group by location ";

		String grade_sql = "select  grade_name as showName,grade_code,count(1) as stuCount from kylin_student_stat "
				+ " group by grade_name,grade_code order by  grade_code";

		// String zh_sql =
		// "select grade_name,grade_code,sex,age,count(1) as stuCount from kylin_student_stat group by grade_name,grade_code,sex,age order by grade_code,CAST(age as int)";

		// String t_sex_sql =
		// "select sex as showName,count(1) as stuCount from teacher_profile "
		// + " group by sex ";
		// String t_region_sql =
		// "select region as showName,count(1) as stuCount from teacher_profile "
		// + " group by region ";
		// String t_school_type_sql =
		// "select  school_type as showName,count(1) as stuCount from teacher_profile "
		// + " group by school_type ";
		// String t_education_sql =
		// "select  education as showName,count(1) as stuCount from teacher_profile "
		// + " group by education ";
		// String t_title_sql =
		// "select  title as showName,count(1) as stuCount from teacher_profile "
		// + " group by title ";

		// String t_sex_sql =
		// "select sex as showName,count(1) as stuCount from teacher_tag_cube "
		// + " group by sex ";
		// String t_region_sql =
		// "select region as showName,count(1) as stuCount from teacher_tag_cube "
		// + " group by region ";
		// String t_school_type_sql =
		// "select  school_type as showName,count(1) as stuCount from teacher_tag_cube "
		// + " group by school_type ";
		// String t_education_sql =
		// "select  education as showName,count(1) as stuCount from teacher_tag_cube "
		// + " group by education ";
		// String t_title_sql =
		// "select  title as showName,count(1) as stuCount from teacher_tag_cube "
		// + " group by title ";

		List<Json> paramList = new ArrayList<Json>();
		List<Json> kylinList = kylinClientService.getDataListFromKylin(
				"student_stat", sex_sql, paramList, resultFieldList);
		List<Json> kylinList1 = kylinClientService.getDataListFromKylin(
				"student_stat", age_sql, paramList, resultFieldList);
		List<Json> kylinList2 = kylinClientService.getDataListFromKylin(
				"student_stat", nation_sql, paramList, resultFieldList);
		List<Json> kylinList3 = kylinClientService.getDataListFromKylin(
				"student_stat", location_sql, paramList, resultFieldList);
		List<Json> kylinList4 = kylinClientService.getDataListFromKylin(
				"student_stat", grade_sql, paramList, resultFieldList);
		kylinList.addAll(kylinList1);
		kylinList.addAll(kylinList2);
		kylinList.addAll(kylinList3);
		kylinList.addAll(kylinList4);
		//

		// List<Json> kylinList5 = kylinClientService.getDataListFromKylin(
		// "user_profile", t_sex_sql, paramList, resultFieldList);
		// List<Json> kylinList6 = kylinClientService.getDataListFromKylin(
		// "user_profile", t_region_sql, paramList, resultFieldList);
		// List<Json> kylinList7 = kylinClientService.getDataListFromKylin(
		// "user_profile", t_school_type_sql, paramList, resultFieldList);
		// List<Json> kylinList8 = kylinClientService.getDataListFromKylin(
		// "user_profile", t_education_sql, paramList, resultFieldList);
		// List<Json> kylinList9 = kylinClientService.getDataListFromKylin(
		// "user_profile", t_title_sql, paramList, resultFieldList);
		// kylinList.addAll(kylinList5);
		// kylinList.addAll(kylinList6);
		// kylinList.addAll(kylinList7);
		// kylinList.addAll(kylinList8);
		// kylinList.addAll(kylinList9);
		map.put("kylinList", kylinList);
		return "/bigdata/demo/kylinQuery.ftl";
	}

}
