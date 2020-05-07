package net.zdsoft.bigdata.frame.data.demo.controller;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.bigdata.frame.data.druid.DruidAggregationParam;
import net.zdsoft.bigdata.frame.data.druid.DruidClientService;
import net.zdsoft.bigdata.frame.data.druid.DruidConstants;
import net.zdsoft.bigdata.frame.data.druid.DruidParam;
import net.zdsoft.framework.entity.Json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;

@RequestMapping("/bigdata/frame/common/demo")
@Controller
public class JstormDemoController {
	
	@Autowired
	DruidClientService druidClientService;
	
	@RequestMapping("/jstorm")
	public String list(ModelMap map) {
		
//		DruidParam druidParam=new DruidParam();
//		druidParam.setQueryType(DruidConstants.QUERY_TYPE_GROUP_BY);
//		druidParam.setDataSource("druid_khw_new_register");
//		druidParam.setGranularity(DruidConstants.GRANULARITY_DAY);
//		
//		List<String> dimensions=new ArrayList<String>();
//		//dimensions.add("org_name");
//		dimensions.add("student_source");
//		
//		druidParam.setDimensions(dimensions);
		
		/**
		"filter": {
		    "type": "and",
		    "fields": [
		      { "type": "selector", "dimension": "carrier", "value": "AT&T" },
		      { "type": "or", 
		        "fields": [
		          { "type": "selector", "dimension": "make", "value": "Apple" },
		          { "type": "selector", "dimension": "make", "value": "Samsung" }
		        ]
		      }
		    ]
		  },*/
		
////	Json filter =new Json();
////	filter.put("type", "selector");
////	filter.put("dimension", "browser_name");
////	filter.put("value", "Chrome");
////	druidParam.setFilter(filter);
//	//in
//	//"filter":{"type": "in" , "dimension" : "source" , "values":  ["ios","pc","android"] } 
////	Json filter =new Json();
////	filter.put("type", "in");
////	filter.put("dimension", "browser_name");
////	filter.put("values", new String[]{"IE","Chrome"});
////	druidParam.setFilter(filter);
//		Json filter =new Json();
//	Json filter1 =new Json();
//	filter1.put("type", "in");
//	filter1.put("dimension", "business_dept");
//	filter1.put("values", new String[]{"运营中心","智慧教育事业部"});
//	Json filter2 =new Json();
//	filter2.put("type", "in");
//	filter2.put("dimension", "student_source");
//	filter2.put("values", new String[]{"后台维护"});
//	List<Json> filterList=new ArrayList<Json>();
//	filterList.add(filter1);
//	filterList.add(filter2);
//	filter.put("type", "and");
//	filter.put("fields", filterList);
//	
//	druidParam.setFilter(filter);
//		Json filterJson=new Json();
//		filterJson.put("type", "and");
//		List<Json> filterFieldList=new ArrayList<Json>();
//		
//		Json filterFieldJson=new Json();
//		filterFieldJson.put("type", "selector");
//		filterFieldJson.put("dimension", "business_dept");
//		filterFieldJson.put("value", "运营中心");
//		filterFieldList.add(filterFieldJson);
//		filterJson.put("fields", filterFieldList);
//		druidParam.setFilter(filterJson);
		
//		List<DruidAggregationParam> aggregationParamList=new ArrayList<DruidAggregationParam>();
//		
//		DruidAggregationParam aggregationParam=new DruidAggregationParam();
//		aggregationParam.setType("thetaSketch");
//		aggregationParam.setName("unique_student_count");
//		aggregationParam.setFieldName("student_id");
		
//		aggregationParam.setType("count");
//		aggregationParam.setName("count");
//		aggregationParamList.add(aggregationParam);
//		druidParam.setAggregations(aggregationParamList);
//		List<String> intervals=new ArrayList<String>();
//		intervals.add("2000-01-01T00:00Z/3000-01-01T00:00Z");
//		druidParam.setIntervals(intervals);
		
//		List<Json> resultFieldList=new ArrayList<Json>();
//		Json field1=new Json();
//		JSONArray keys=new JSONArray();
//		//keys.add("org_name");
//		keys.add("student_source");
//		field1.put("keys", keys);
//		field1.put("resultField", "count");
//		field1.put("resultDataType", "int");
//		
		//field1.put("serials", "次数");
//		resultFieldList.add(field1);
		
//		DruidParam druidParam=new DruidParam();
//		druidParam.setQueryType(DruidConstants.QUERY_TYPE_TIMESERIES);
//		druidParam.setDataSource("druid_login_analyse_test");
//		druidParam.setGranularity(DruidConstants.GRANULARITY_DAY);
//		//=
////		Json filter =new Json();
////		filter.put("type", "selector");
////		filter.put("dimension", "browser_name");
////		filter.put("value", "Chrome");
////		druidParam.setFilter(filter);
//		//in
//		//"filter":{"type": "in" , "dimension" : "source" , "values":  ["ios","pc","android"] } 
////		Json filter =new Json();
////		filter.put("type", "in");
////		filter.put("dimension", "browser_name");
////		filter.put("values", new String[]{"IE","Chrome"});
////		druidParam.setFilter(filter);
//		
////		List<String> dimensions=new ArrayList<String>();
////		dimensions.add("browser_name");
////		druidParam.setDimensions(dimensions);
//		List<DruidAggregationParam> aggregationParamList=new ArrayList<DruidAggregationParam>();
//		
//		DruidAggregationParam aggregationParam=new DruidAggregationParam();
//		aggregationParam.setType("count");
//		aggregationParam.setName("count");
//		aggregationParamList.add(aggregationParam);
//		druidParam.setAggregations(aggregationParamList);
//		List<String> intervals=new ArrayList<String>();
//		intervals.add("2000-01-01T00:00Z/3000-01-01T00:00Z");
//		druidParam.setIntervals(intervals);
//		
//		List<Json> resultFieldList=new ArrayList<Json>();
//	
//		Json field2=new Json();
//		//field2.put("key", "browser_name");
//		field2.put("field", "count");
//		field2.put("serials", "次数");
//		resultFieldList.add(field2);
		
//		DruidParam druidParam=new DruidParam();
//		druidParam.setQueryType(DruidConstants.QUERY_TYPE_TOP_N);
//		druidParam.setDataSource("druid_khw_stu_course");
//		druidParam.setGranularity(DruidConstants.GRANULARITY_ALL);
//		druidParam.setMetric("count");
//		druidParam.setThreshold("5");
//		druidParam.setDimension("org_name");
//		List<DruidAggregationParam> aggregationParamList=new ArrayList<DruidAggregationParam>();
//		
//		DruidAggregationParam aggregationParam=new DruidAggregationParam();
//		aggregationParam.setType("thetaSketch");
//		aggregationParam.setName("unique_student_count");
//		aggregationParam.setFieldName("student_id");
//		aggregationParamList.add(aggregationParam);
//		druidParam.setAggregations(aggregationParamList);
//		List<String> intervals=new ArrayList<String>();
//		intervals.add("2000-01-01T00:00Z/3000-01-01T00:00Z");
//		druidParam.setIntervals(intervals);
//		
//		List<Json> resultFieldList=new ArrayList<Json>();
//		Json field1=new Json();
//		field1.put("key", "org_name");
//		field1.put("field", "unique_student_count");
//		resultFieldList.add(field1);
		
//		List<Json> resultList =druidClientService.getDruidQueries(druidParam, resultFieldList);
//		map.put("druidList", resultList);
		return "/bigdata/demo/jstromQuery.ftl";
	}
	
}
