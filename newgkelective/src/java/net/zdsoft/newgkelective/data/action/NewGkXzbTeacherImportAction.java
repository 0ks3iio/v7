package net.zdsoft.newgkelective.data.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.TeachGroup;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.TeachGroupExRemoteService;
import net.zdsoft.basedata.remote.service.TeachGroupRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.entity.NewGkArrayItem;
import net.zdsoft.newgkelective.data.entity.NewGkClassCombineRelation;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlanEx;
import net.zdsoft.newgkelective.data.service.NewGkArrayItemService;
import net.zdsoft.newgkelective.data.service.NewGkClassCombineRelationService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanService;

@Controller
@RequestMapping("/newgkelective")
public class NewGkXzbTeacherImportAction extends DataImportAction{
	private Logger logger = Logger.getLogger(NewGkXzbTeacherImportAction.class);
	private static final String FORBIDEN_STR = "X";
	
	@Autowired
	private NewGkArrayItemService newGkArrayItemService;
	@Autowired
	private NewGkDivideService newGkDivideService;
	@Autowired
	private NewGkDivideClassService newGkDivideClassService;
	@Autowired
	private NewGkSubjectTimeService newGkSubjectTimeService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private NewGkTeacherPlanService newGkTeacherPlanService;
	@Autowired
	private TeachGroupRemoteService teachGroupRemoteService;
	@Autowired
	private NewGkClassCombineRelationService newGkClassCombineRelationService;

	@RequestMapping("/exportXzbIndex/main")
	public String execute(String arrayItemId,String arrayId,ModelMap map) {
		NewGkArrayItem arrayItem = newGkArrayItemService.findOne(arrayItemId);
		if(arrayItem==null){
			return errorFtl(map, "排课基础特征数据不存在");
		}
		NewGkDivide divide = newGkDivideService.findOne(arrayItem.getDivideId());
		if(divide==null){
			return errorFtl(map, "对应的分班方案不存在");
		}
		map.put("divideName", divide.getDivideName());
		// 业务名称
		map.put("businessName", "教师任课信息");
		// 导入URL 
		map.put("businessUrl", "/newgkelective/exportXzbIndex/import");
		
		map.put("templateDownloadUrl", "/newgkelective/exportXzbIndex/template?arrayItemId="+arrayItemId);
        map.put("exportErrorExcelUrl", "/newgkelective/exportXzbIndex/exportErrorExcel");
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());			
		map.put("businessKey", UuidUtils.generateUuid());
		//模板校验
		map.put("validateUrl", "/newgkelective/exportXzbIndex/validate?arrayItemId="+arrayItemId);
		map.put("arrayItemId", arrayItemId);
		map.put("arrayItemName", arrayItem.getItemName());
		map.put("openType", divide.getOpenType());
		map.put("divide", divide);
		map.put("arrayId", arrayId);
		return "/newgkelective/basic/newGkXzbTeacherImport.ftl";
	}
	
	
	
	
	@Override
	public String getObjectName() {
		return "newGkXzbTeacherImport";
	}

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入文件中请确认数据是否正确</p>"
				+"<p>2、请根据实际情况填写，班级来源于分班方案下的班级</p>"
				+"<p>3、标志 \"X\" 表示此班级不上这门科目，不需要填写教师 </p>"
				+"<p>4、建议不要对科目进行增加和删除，以导出的模板为准，如遇到科目名称重复，请人工处理去除重复一列 </p>";
				//+"<p>4、如果导入任课信息时，存在科目已经不属于需要安排的行政班科目，将自动过滤，不进行保存 </p>";
	}

	@Override
	public List<String> getRowTitleList() {
		 
		return new ArrayList<>();
	}

	public List<String> getRowTitleList(String arrayItemId) {
        List<String> titleList=new  ArrayList<>();
        List<Course> courseList=findCourceByArrayItemId(arrayItemId);
        titleList.add("班级");
        for(Course c:courseList) {
            titleList.add(c.getSubjectName());
        }
        return titleList;
    }
	//不考虑重名
	public Map<String,Course> getRowTitleMap(String arrayItemId) {
		List<Course> courseList = findCourceByArrayItemId(arrayItemId);
		Map<String,Course> courseMap=new HashMap<String, Course>();
		for(Course cource:courseList) {
			courseMap.put(cource.getSubjectName(), cource);
		}
		return courseMap;
	}
	
	public List<Course> findCourceByArrayItemId(String arrayItemId){
		List<NewGkSubjectTime> sujectTimeList = newGkSubjectTimeService.findByArrayItemId(arrayItemId);
		Set<String> subIds=new HashSet<>();
		if(CollectionUtils.isNotEmpty(sujectTimeList)) {
			for(NewGkSubjectTime time:sujectTimeList) {
				subIds.add(time.getSubjectId());
			}
		}
		List<Course> courseList=new ArrayList<Course>();
		if(CollectionUtils.isNotEmpty(subIds)) {
			courseList = SUtils.dt(courseRemoteService.findBySubjectIdIn(subIds.toArray(new String[0])), new TR<List<Course>>() {});
		}
		return courseList;
	}
	
	@Override
	@RequestMapping("/exportXzbIndex/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		logger.info("导入开始......");
		JSONObject jsStr = JSONObject.parseObject(params); //将字符串{“id”：1}
		String unitId=getLoginInfo().getUnitId();
		//本单位所有教师
 		List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findByUnitId(unitId),new TR<List<Teacher>>() {});
		Map<String, List<Teacher>> teacherMap = EntityUtils.getListMap(teacherList, Teacher::getTeacherName, Function.identity());
		
		String arrayItemId = jsStr.getString("arrayItemId");
		List<Course> xzbCourseList = findCourceByArrayItemId(arrayItemId);
		Map<String,Course> xzbCourseMap=EntityUtils.getMap(xzbCourseList, e->e.getId());
		
		Map<String,List<Course>>  xzbCourseNameMap=new HashMap<>();
		if(CollectionUtils.isNotEmpty(xzbCourseList)){
			Map<String, List<Course>> xzbNameMap2 = EntityUtils.getListMap(xzbCourseList,Course::getSubjectName,e->e);
			xzbCourseNameMap.putAll(xzbNameMap2);
		}
		
		
		NewGkArrayItem arrayItem = newGkArrayItemService.findOne(arrayItemId);
		NewGkDivide divide = newGkDivideService.findOne(arrayItem.getDivideId());
		//行政班班级
		List<NewGkDivideClass> clazzlist = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divide.getId(), 
				new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_3,
						NewGkElectiveConstant.CLASS_TYPE_4}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		//不会重名的
		Map<String,NewGkDivideClass> clazzByName=EntityUtils.getMap(clazzlist, e->e.getClassName());
		Map<String,String> classIdNameMap=EntityUtils.getMap(clazzlist, e->e.getId(), e->e.getClassName());
		Map<String, String> cidBatchMap = clazzlist.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType()))
				.collect(Collectors.toMap(e->e.getId(), e->e.getSubjectType()+e.getBatch()));
		
		
		// 教研组 老师
		List<TeachGroup> teaGroupList = SUtils.dt(teachGroupRemoteService.findBySchoolId(unitId, true), TeachGroup.class);
		Map<String,Set<String>> groupSubIdTidMap = new HashMap<>();  
		for (TeachGroup teachGroup : teaGroupList) {
			String subjectId = teachGroup.getSubjectId();
			Set<String> set = groupSubIdTidMap.get(subjectId);
			if(set == null) {
				set = new HashSet<>();
				groupSubIdTidMap.put(subjectId, set);
			}
			if(CollectionUtils.isNotEmpty(teachGroup.getTeacherIdSet())) {
				set.addAll(teachGroup.getTeacherIdSet());
			}
		}
		
		
		List<String> alltitlelist = ExcelUtils.readExcelOneRow(filePath,1);
		List<String> titlelist=new ArrayList<>();
		//去空
		//去除班级
		for(int i=1;i<alltitlelist.size();i++) {
			String cellValue=StringUtils.trim(alltitlelist.get(i));
			if(i==alltitlelist.size()-1 && StringUtils.isBlank(cellValue)) {
				break;
			}
			titlelist.add(cellValue);
		}
		
		List<String[]> errorDataList=new ArrayList<String[]>();
		List<String[]> datas =new ArrayList<>();
		
		int index=2;
		//暂时不验证匹配当前行政班的科目
		if(CollectionUtils.isEmpty(titlelist)) {
			String[] errorData=new String[4];
			errorData[0]=String.valueOf(1);
			errorData[1]="";
			errorData[2]="";
			errorData[3]="表头数据不对";
			errorDataList.add(errorData);
			//总共行数 
			datas = ExcelUtils.readExcelByRow(filePath, 2, 1);
			return result(datas.size(),0,datas.size(),errorDataList,"");
		}
		datas = ExcelUtils.readExcelByRow(filePath, 2, alltitlelist.size());
		if(CollectionUtils.isEmpty(datas)) {
			String[] errorData=new String[4];
			errorData[0]=String.valueOf(1);
			errorData[1]="";
			errorData[2]="";
			errorData[3]="没有任课信息需要导入";
			errorDataList.add(errorData);
			return result(datas.size(),0,datas.size(),errorDataList,"");
		}
		//是否存在相同的列
		Set<String> titleSet=new HashSet<>();
		titleSet.addAll(titlelist);
		if(titleSet.size()!=titlelist.size()) {
			String[] errorData=new String[4];
			errorData[0]=String.valueOf(1);
			errorData[1]="";
			errorData[2]="";
			errorData[3]="导入文件中存在科目相同，请先删除相同科目，再操作";
			errorDataList.add(errorData);
            return result(datas.size(),0,datas.size(),errorDataList,"");
		}
//		if(CollectionUtils.isEmpty(xzbCourseList)) {
//			//行政班科目为空
//			String[] errorData=new String[4];
//			errorData[0]=String.valueOf(1);
//			errorData[1]="";
//			errorData[2]="";
//			errorData[3]="目前没有维护行政科目，请先去维护后，在操作导入";
//			errorDataList.add(errorData);
//			importResultJson.put("totalCount", datas.size());
//			importResultJson.put("successCount", 0);
//			importResultJson.put("errorCount", datas.size());
//			importResultJson.put("errorData", errorDataList);
//			return importResultJson.toJSONString();
//		}
		//首先判断科目数据是否正确 
		//根据名称查询科目 如果存在科目名称重复 首先如果在xzbCourseList是唯一的 也可以 除此外还重复的 就建议人工在模板上去除该科目导入
//		List<Course> courseList = SUtils.dt(courseRemoteService.findBySubjectNameIn(unitId,titlelist.toArray(new String[0])), new TR<List<Course>>() {});
		List<Course> courseList = titlelist.stream().filter(e->xzbCourseNameMap.containsKey(e)).flatMap(e->xzbCourseNameMap.get(e).stream()).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(courseList)) {
			String[] errorData=new String[4];
			errorData[0]=String.valueOf(1);
			errorData[1]="";
			errorData[2]="";
			errorData[3]="科目未找到，请确认科目名称是否准确";
			errorDataList.add(errorData);
			return result(datas.size(),0,datas.size(),errorDataList,"");
		}
		Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId, Course::getSubjectName);
		/*********  以下： 表头科目数据                  *********/
		Map<String,Course> courseByName=new HashMap<>();
		Set<String> sameCourseName=new HashSet<>();
		
		for(Course c:courseList) {
			if(sameCourseName.contains(c.getSubjectName())) {
				continue;
			}
			if(courseByName.containsKey(c.getSubjectName())) {
				Course firstCourse = courseByName.get(c.getSubjectName());
				
				List<Course> ll = xzbCourseNameMap.get(c.getSubjectName());
				if(CollectionUtils.isNotEmpty(ll)){
					if(ll.size()==1){
						courseByName.put(c.getSubjectName(), ll.get(0));
					}else{
						sameCourseName.add(c.getSubjectName());
					}
				}else{
					if(xzbCourseMap.containsKey(c.getId()) && xzbCourseMap.containsKey(firstCourse.getId())) {
						//都包括 重复
						sameCourseName.add(c.getSubjectName());
					}else if(!xzbCourseMap.containsKey(c.getId()) && !xzbCourseMap.containsKey(firstCourse.getId())){
						//都不包括 重复
						sameCourseName.add(c.getSubjectName());
					}else {
						if(xzbCourseMap.containsKey(c.getId())) {
							// 同名 优先选择 课程特征中存在的
							courseByName.put(c.getSubjectName(), c);
						}else {
							courseByName.put(firstCourse.getSubjectName(), firstCourse);
						}
					}
				}
				
			}else {
				courseByName.put(c.getSubjectName(), c);
			}
		}
		List<Course> titleListCourse=new ArrayList<>();
		for(String s:titlelist) {
			if(sameCourseName.contains(s)) {
				String[] errorData=new String[4];
				errorData[0]=String.valueOf(1);
				errorData[1]="";
				errorData[2]="";
				errorData[3]="科目："+s+" 未找到唯一的对应科目，建议删除导入文件中该科目对应的列，重新操作";
				errorDataList.add(errorData);
				return result(datas.size(),0,datas.size(),errorDataList,"");
			}
			if(!courseByName.containsKey(s)) {
				String[] errorData=new String[4];
				errorData[0]=String.valueOf(1);
				errorData[1]="";
				errorData[2]="";
				errorData[3]="科目："+s+"不存在，请先确定科目名称的准确性再操作，重新操作";
				errorDataList.add(errorData);
                return result(datas.size(),0,datas.size(),errorDataList,"");
			}else {
				if(!xzbCourseMap.containsKey(courseByName.get(s).getId())) {
					String[] errorData=new String[4];
					errorData[0]=String.valueOf(1);
					errorData[1]="";
					errorData[2]="";
					errorData[3]="科目："+s+"未开设，请先去课程特征设置添加科目";
					errorDataList.add(errorData);
					return result(datas.size(),0,datas.size(),errorDataList,"");
				}
				titleListCourse.add(courseByName.get(s));
			}
		}
		
		/*********  以上： 表头科目数据                  *********/
		//需要删除对应的科目
		Set<String> deleteSubject=EntityUtils.getSet(titleListCourse, e->e.getId());
		// k:classId V:k2:subId v2:tid
		Map<String,Map<String,String>> subjectTeacherClazz=new LinkedHashMap<>();
		Map<String, Integer> classRowMap = new HashMap<String, Integer>();
		Map<String,Set<String>> batchTeaMap = new HashMap<>();
		//导入数据clazzByName
        int t=0;
        for(String[] data:datas) {
            t++;
			index++;
			String clazzName=StringUtils.trim(data[0]);
			if(StringUtils.isBlank(clazzName)) {
				String[] errorData=new String[4];
				// errorData[0]=errorDataList.size()+1+"";
                errorData[0]=t+"";
				errorData[1]="第"+String.valueOf(index)+"行";
				errorData[2]="班级";
				errorData[3]="不能为空";
				errorDataList.add(errorData);
				continue;
			}
			if(!clazzByName.containsKey(clazzName)) {
				String[] errorData=new String[4];
                // errorData[0]=errorDataList.size()+1+"";
                errorData[0]=t+"";
				errorData[1]="第"+String.valueOf(index)+"行";
				errorData[2]="班级";
				errorData[3]="班级："+clazzName+" 不存在";
				errorDataList.add(errorData);
				continue;
			}
			String clazzId=clazzByName.get(clazzName).getId();
			if(subjectTeacherClazz.containsKey(clazzId)){
				String[] errorData=new String[4];
                // errorData[0]=errorDataList.size()+1+"";
                errorData[0]=t+"";
				errorData[1]="第"+String.valueOf(index)+"行";
				errorData[2]="班级";
				errorData[3]="班级："+clazzName+" 重复";
				errorDataList.add(errorData);
				continue;
			}
			String batch = cidBatchMap.get(clazzId);
			Map<String,String> tIdBySubject=new HashMap<>();
			boolean f=true;
			for(int ii=0;ii<titleListCourse.size();ii++) {
				String tName=StringUtils.trim(data[ii+1]);
				if(StringUtils.isBlank(tName) || Objects.equals(FORBIDEN_STR, tName)) {
					tIdBySubject.put(titleListCourse.get(ii).getId(), null);
					continue;
				}
				if(!teacherMap.containsKey(tName)) {
					f=false;
					String[] errorData=new String[4];
                    // errorData[0]=errorDataList.size()+1+"";
                    errorData[0]=t+"";
					errorData[1]="第"+String.valueOf(index)+"行";
					errorData[2]=titleListCourse.get(ii).getSubjectName();
					errorData[3]="教师："+tName+" 不存在";
					errorDataList.add(errorData);
					break;
				}
				List<Teacher> ttList = teacherMap.get(tName);
				if(ttList.size()>1) {
					f=false;
					String[] errorData=new String[4];
                    // errorData[0]=errorDataList.size()+1+"";
                    errorData[0]=t+"";
					errorData[1]="第"+String.valueOf(index)+"行";
					errorData[2]=titleListCourse.get(ii).getSubjectName();
					errorData[3]="教师："+tName+" 对应匹配教师不唯一";
					errorDataList.add(errorData);
					break;
				}
				Teacher tt = ttList.get(0);
				Course course = titleListCourse.get(ii);
				Set<String> tids = groupSubIdTidMap.get(course.getId());
				if(tids == null) {
					f=false;
					String[] errorData=new String[4];
					// errorData[0]=errorDataList.size()+1+"";
					errorData[0]=t+"";
					errorData[1]="第"+String.valueOf(index)+"行";
					errorData[2]=course.getSubjectName();
					errorData[3]=course.getSubjectName()+" 的教研组不存在";
					errorDataList.add(errorData);
					break;
				}
				if(!tids.contains(tt.getId())) {
					f=false;
					String[] errorData=new String[4];
					// errorData[0]=errorDataList.size()+1+"";
					errorData[0]=t+"";
					errorData[1]="第"+String.valueOf(index)+"行";
					errorData[2]=course.getSubjectName();
					errorData[3]=course.getSubjectName()+"教研组不存在 教师："+tName;
					errorDataList.add(errorData);
					break;
				}
				if(StringUtils.isNotBlank(batch)) {
					Set<String> tidsT = batchTeaMap.get(batch);
					if(tidsT == null) {
						tidsT = new HashSet<>();
						batchTeaMap.put(batch, tidsT);
					}
					if(tidsT.contains(tt.getId())) {
						f=false;
						String replace = batch.replace("A", "选考").replace("B", "学考");
						String[] errorData=new String[4];
						// errorData[0]=errorDataList.size()+1+"";
						errorData[0]=t+"";
						errorData[1]="第"+String.valueOf(index)+"行";
						errorData[2]=course.getSubjectName();
						errorData[3]= tName+"不应该在" +replace+ "任教多个班级";
						errorDataList.add(errorData);
						break;
					}else {
						tidsT.add(tt.getId());
					}
				}
				tIdBySubject.put(course.getId(), tt.getId());
			}
			if(!f) {
				continue;
			}
			//classId tIdBySubject
			subjectTeacherClazz.put(clazzId, tIdBySubject);
			classRowMap.put(clazzId, t);
		}
        
        if(subjectTeacherClazz.size()>0){
        	//合班和同排冲突判断
        	List<NewGkClassCombineRelation> relationList = newGkClassCombineRelationService.findByArrayItemId(unitId, arrayItemId);
        	Map<String, Map<String, List<String>>> claIdMap = new HashMap<String, Map<String, List<String>>>();//key1-classId,key2-subjectId,value-关联班级id
        	Map<String, Map<String, List<String>>> claSubMap = new HashMap<String, Map<String, List<String>>>();//key1-classId,key2-subjectId,value-关联的classId+subjectId
        	for (NewGkClassCombineRelation rela : relationList) {
        		String classSubjectIds = rela.getClassSubjectIds();
        		String[] csIdArr = classSubjectIds.split(",");
        		if(NewGkElectiveConstant.COMBINE_TYPE_1.equals(rela.getType())) {
        			//合班
        			String[] sp1 = csIdArr[0].split("-");
        			String[] sp2 = csIdArr[1].split("-");
        			String claId1 = sp1[0];
        			String claId2 = sp2[0];
        			String subjectId = sp1[1];
        			if(!claIdMap.containsKey(claId1)){
        				claIdMap.put(claId1, new HashMap<String, List<String>>());
        			}
        			if(!claIdMap.containsKey(claId2)){
        				claIdMap.put(claId2, new HashMap<String, List<String>>());
        			}
        			if(!claIdMap.get(claId1).containsKey(subjectId)){
        				claIdMap.get(claId1).put(subjectId, new ArrayList<String>());
        			}
        			if(!claIdMap.get(claId2).containsKey(subjectId)){
        				claIdMap.get(claId2).put(subjectId, new ArrayList<String>());
        			}
        			claIdMap.get(claId1).get(subjectId).add(claId2);
        			claIdMap.get(claId2).get(subjectId).add(claId1);
        		}else if(NewGkElectiveConstant.COMBINE_TYPE_2.equals(rela.getType())) {
        			//同时排课
        			String[] sp1 = csIdArr[0].split("-");
        			String[] sp2 = csIdArr[1].split("-");
        			String claId1 = sp1[0];
        			String claId2 = sp2[0];
        			String subjectId1 = sp1[1];
        			String subjectId2 = sp2[1];
    				if(!claSubMap.containsKey(claId1)){
    					claSubMap.put(claId1, new HashMap<String, List<String>>());
    				}
    				if(!claSubMap.get(claId1).containsKey(subjectId1)){
    					claSubMap.get(claId1).put(subjectId1, new ArrayList<String>());
        			}
    				claSubMap.get(claId1).get(subjectId1).add(claId2+"-"+subjectId2);
    				if(!claSubMap.containsKey(claId2)){
    					claSubMap.put(claId2, new HashMap<String, List<String>>());
    				}
    				if(!claSubMap.get(claId2).containsKey(subjectId2)){
    					claSubMap.get(claId2).put(subjectId2, new ArrayList<String>());
        			}
    				claSubMap.get(claId2).get(subjectId2).add(claId1+"-"+subjectId1);
        		}
        	}
        	// 获取原有的教师安排信息
        	List<NewGkTeacherPlan> teacherPlanList = newGkTeacherPlanService.findByArrayItemIds(
        			new String[]{arrayItemId}, true);
        	Map<String,String> claSubTeaMap = new HashMap<>();
        	for (NewGkTeacherPlan tp : teacherPlanList) {
        		List<NewGkTeacherPlanEx> teacherPlanExList = tp.getTeacherPlanExList();
        		if(CollectionUtils.isEmpty(teacherPlanExList)){
        			continue;
        		}
        		for (NewGkTeacherPlanEx tpe : teacherPlanExList) {
        			String teacherId = tpe.getTeacherId();
        			String classIds = tpe.getClassIds();
        			if(StringUtils.isNotBlank(classIds)){
        				String[] classIdArr = classIds.split(",");
        				for (String cid : classIdArr) {
        					if(deleteSubject.contains(tp.getSubjectId())&&subjectTeacherClazz.containsKey(cid)){
        	        			continue;
        	        		}
        					String key = cid+"-"+tp.getSubjectId();
        					claSubTeaMap.put(key, teacherId);
        				}
        			}
        		}
        	}
        	
        	//判断合班和同排冲突，如果不冲突，添加到教师信息中
        	for (Entry<String, Map<String, String>> entry : subjectTeacherClazz.entrySet()) {//key-classId
        		String cid = entry.getKey();
        		boolean flag1=false;
        		for (Entry<String, String> ent : entry.getValue().entrySet()) {//key-subjectId
        			//合班
        			boolean flag2=false;
        			if(claIdMap.containsKey(cid) && claIdMap.get(cid).containsKey(ent.getKey())){
						for (String claId : claIdMap.get(cid).get(ent.getKey())) {
							String key = claId+"-"+ent.getKey();
							if(StringUtils.isNotBlank(claSubTeaMap.get(key)) && !Objects.equals(ent.getValue(),claSubTeaMap.get(key))){
								String[] errorData=new String[4];
			                    errorData[0]=classRowMap.get(cid)+"";
								errorData[1]="第"+(classRowMap.get(cid)+2)+"行";
								errorData[2]="科目："+courseNameMap.get(ent.getKey());
								errorData[3]=classIdNameMap.get(cid)+"和"+classIdNameMap.get(claId)+"存在合班操作，任课老师必须相同";
								errorDataList.add(errorData);
								flag2=true;
								break;
							}
						}
						if(flag2){
							flag1=true;
							break;
						}
					}
        			//同排
					if(ent.getValue() != null &&claSubMap.containsKey(cid)&&claSubMap.get(cid).containsKey(ent.getKey())){
						for (String claSubId : claSubMap.get(cid).get(ent.getKey())) {
							if(StringUtils.isNotBlank(claSubTeaMap.get(claSubId))&& ent.getValue().equals(claSubTeaMap.get(claSubId))){
								String[] errorData=new String[4];
			                    errorData[0]=classRowMap.get(cid)+"";
								errorData[1]="第"+(classRowMap.get(cid)+2)+"行";
								errorData[2]="科目："+courseNameMap.get(ent.getKey());
								errorData[3]=classIdNameMap.get(cid)+"和"+classIdNameMap.get(claSubId.substring(0,claSubId.indexOf("-")))+"存在同时排课操作，任课老师不能相同";
								errorDataList.add(errorData);
								flag2=true;
								break;
							}
						}
						if(flag2){
							flag1=true;
							break;
						}
					}
				}
        		if(flag1){
        			continue;
        		}
        		for (Entry<String, String> ent : entry.getValue().entrySet()) {
        			claSubTeaMap.put(entry.getKey()+"-"+ent.getKey(), ent.getValue());
        		}
			}
        	
        }

        // 错误数据Excel导出
        String errorExcelPath = "";
        if(CollectionUtils.isNotEmpty(errorDataList)) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            //标题栏固定
            sheet.createFreezePane(0, 2);
            
            List<String> titleList=new ArrayList<String>();
            titleList.addAll(alltitlelist);
            
//            List<String> titleList = getRowTitleList(arrayItemId);
            titleList.add("错误数据");
            titleList.add("错误原因");
            
            CellRangeAddress car = new CellRangeAddress(0,0,0,titleList.size()-1);
            sheet.addMergedRegion(car);
            
            // 注意事项样式
    	    HSSFCellStyle style = workbook.createCellStyle();
    	    HSSFFont headfont = workbook.createFont();
            headfont.setFontHeightInPoints((short) 9);// 字体大小
            headfont.setColor(HSSFFont.COLOR_RED);//字体颜色
            style.setFont(headfont);
            style.setAlignment(HorizontalAlignment.LEFT);//水平居左
            style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            style.setWrapText(true);//自动换行
            
            //第一行
            HSSFRow remarkRow = sheet.createRow(0);
            //高度：5倍默认高度
            remarkRow.setHeightInPoints(5*sheet.getDefaultRowHeightInPoints());
      
            HSSFCell remarkCell = remarkRow.createCell(0);
            remarkCell.setCellStyle(style);
            //注意事项
            String remark = getRemark();
            remarkCell.setCellValue(new HSSFRichTextString(remark));
            remarkCell.setCellStyle(style);
            HSSFCellStyle errorStyle = workbook.createCellStyle();
            HSSFFont font = workbook.createFont();
            font.setColor(HSSFFont.COLOR_RED);
            errorStyle.setFont(font);

            HSSFRow rowTitle = sheet.createRow(1);
            for(int i=0;i<titleList.size();i++){
            	sheet.setColumnWidth(i, 10 * 256);//列宽
                HSSFCell cell = rowTitle.createCell(i);
                cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
            }
            
            Map<String, String[]> errorDataMap = errorDataList.stream().collect(Collectors.toMap(e->e[0], Function.identity()));
            
            int j = 1;
			for (String[] rowData : datas) {
				j++;
				HSSFRow row = sheet.createRow(j);
				for (int k=0;k<titleList.size();k++) {
					HSSFCell cell = row.createCell(k);
					if (k<titleList.size()-2) {
						cell.setCellValue(new HSSFRichTextString(rowData[k]));
					} else if (k==titleList.size()-2) {
						cell.setCellStyle(errorStyle);
						if(errorDataMap.containsKey(String.valueOf(j-1))){
							cell.setCellValue(new HSSFRichTextString(errorDataMap.get(String.valueOf(j-1))[2]));
						}
					} else {
						cell.setCellStyle(errorStyle);
						if(errorDataMap.containsKey(String.valueOf(j-1))){
							cell.setCellValue(new HSSFRichTextString(errorDataMap.get(String.valueOf(j-1))[3]));
						}
					}
				}
			}

            errorExcelPath = saveErrorExcel(filePath, workbook);
			return result(datas.size(),0,datas.size(),errorDataList,errorExcelPath);
        }else{
        	try {
        		newGkTeacherPlanService.saveImportPlan(arrayItemId,deleteSubject.toArray(new String[] {}),subjectTeacherClazz);
        	}catch (Exception e) {
        		e.printStackTrace();
        		return result(datas.size(),0,datas.size(),errorDataList,"");
        	}
        	logger.info("导入结束......");
        	return result(datas.size(),datas.size(),0,errorDataList,"");
        }
	}
	
	private String result(int totalCount ,int successCount , int errorCount ,List<String[]> errorDataList, String errorExcelPath){
        Json importResultJson=new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        importResultJson.put("errorExcelPath", errorExcelPath);
        return importResultJson.toJSONString();
	}
	
	

	@Override
	@RequestMapping("/exportXzbIndex/template")
	public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
		String arrayItemId = request.getParameter("arrayItemId"); 
		//根据arrayItemId取得行政班科目
		NewGkArrayItem arrayItem = newGkArrayItemService.findOne(arrayItemId);
		NewGkDivide divide = newGkDivideService.findOne(arrayItem.getDivideId());
		//行政班班级
		List<NewGkDivideClass> allClazzlist = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divide.getId(), 
				new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_3,
						NewGkElectiveConstant.CLASS_TYPE_4}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<String> xzbIdList = allClazzlist.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType())).map(e->e.getId()).collect(Collectors.toList());
		List<String> fakeXzbIdList = allClazzlist.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_4.equals(e.getClassType())).map(e->e.getId()).collect(Collectors.toList());
		Map<String, List<String[]>> xzbSubjects = newGkDivideClassService.findXzbSubjects(divide.getUnitId(), divide.getId(), arrayItemId, 
				NewGkElectiveConstant.CLASS_SOURCE_TYPE1, xzbIdList);
		Map<String, List<String[]>> fakeXzbSubjects = newGkDivideClassService.findFakeXzbSubjects(divide.getUnitId(), divide.getId(), arrayItemId, 
				NewGkElectiveConstant.CLASS_SOURCE_TYPE1, fakeXzbIdList);
		
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		//标题栏固定
		sheet.createFreezePane(0, 2);
		List<Course> courseList = findCourceByArrayItemId(arrayItemId);
		List<String> titleList=new  ArrayList<>();
		titleList.add("班级");
		Map<String,Integer> subIndexMap = new HashMap<>();
		Integer index=1;
        for(Course c:courseList) {
			subIndexMap.put(c.getId(), index++);
            titleList.add(c.getSubjectName());
        }
		
		CellRangeAddress car = new CellRangeAddress(0,0,0,titleList.size()-1);
        sheet.addMergedRegion(car);
		
		// 注意事项样式
	    HSSFCellStyle style = workbook.createCellStyle();
	    HSSFFont headfont = workbook.createFont();
        headfont.setFontHeightInPoints((short) 9);// 字体大小
        headfont.setColor(HSSFFont.COLOR_RED);//字体颜色
        style.setFont(headfont);
        style.setAlignment(HorizontalAlignment.LEFT);//水平居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setWrapText(true);//自动换行
        
        //第一行
        HSSFRow remarkRow = sheet.createRow(0);
        //高度：6倍默认高度
        remarkRow.setHeightInPoints(6*sheet.getDefaultRowHeightInPoints());
  
        HSSFCell remarkCell = remarkRow.createCell(0);
        remarkCell.setCellStyle(style);
        //注意事项
        String remark = getRemark();
        remarkCell.setCellValue(new HSSFRichTextString(remark));
        remarkCell.setCellStyle(style);
        
        HSSFRow rowTitle = sheet.createRow(1);
        for(int i=0;i<titleList.size();i++){
        	sheet.setColumnWidth(i, 11 * 256);//列宽
        	HSSFCell cell = rowTitle.createCell(i);
        	cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
        }
        
        // 排序：classType 1 3 4 2
        
        Collections.sort(allClazzlist,(x,y)->{
        	String classType = x.getClassType();
        	String classType2 = y.getClassType();
        	if(NewGkElectiveConstant.CLASS_TYPE_2.equals(classType)) {
        		classType = "5";
        	}
        	if(NewGkElectiveConstant.CLASS_TYPE_2.equals(classType2)) {
        		classType2 = "5";
        	}
        	return classType.compareTo(classType2);
        });
        int j=2;
        HSSFRow row;
        HSSFCell cell;
        Map<String, List<String[]>> xzbSubjects3 = null;
        for(NewGkDivideClass clazz:allClazzlist) {
        	String classId = clazz.getId();
        	row = sheet.createRow(j);
        	cell = row.createCell(0);
        	cell.setCellValue(new HSSFRichTextString(clazz.getClassName()));
        	j++;
        	// 班级是否有某门科目的 教师
        	switch(clazz.getClassType()) {
	        case NewGkElectiveConstant.CLASS_TYPE_1:
	        	makeForbiden(xzbSubjects, subIndexMap, row, classId);
	        	break;
	        case NewGkElectiveConstant.CLASS_TYPE_2:
	        	xzbSubjects3 = new HashMap<>();
	        	xzbSubjects3.put(classId, new ArrayList<>());
	        	xzbSubjects3.get(classId).add(new String[] {clazz.getSubjectIds(),clazz.getSubjectType()});
	        	makeForbiden(xzbSubjects3, subIndexMap, row, classId);
	        	break;
	        case NewGkElectiveConstant.CLASS_TYPE_3:
	        	List<String[]> arrlist =  new ArrayList<>();
	        	if(divide.getFollowType().contains(NewGkElectiveConstant.FOLLER_TYPE_A2)) {
	        		arrlist.add(new String[] {clazz.getSubjectIds(),"A"});
	        	}
	        	if(divide.getFollowType().contains(NewGkElectiveConstant.FOLLER_TYPE_B2)) {
	        		arrlist.add(new String[] {clazz.getSubjectIdsB(),"B"});
	        	}
	        	xzbSubjects3 = new HashMap<>();
	        	xzbSubjects3.put(classId, arrlist);
				makeForbiden(xzbSubjects3, subIndexMap, row, classId);
	        	break;
	        case NewGkElectiveConstant.CLASS_TYPE_4:
	        	makeForbiden(fakeXzbSubjects, subIndexMap, row, classId);
	        	break;
	        	default: 
        	}
        }
		String fileName = "教师任课信息导入";
		
		ExportUtils.outputData(workbook, fileName, response);
	}

	private void makeForbiden(Map<String, List<String[]>> xzbSubjects, Map<String, Integer> subIndexMap, HSSFRow row,
			String classId) {
		HSSFCell cell;
		List<String[]> list = xzbSubjects.get(classId);
		Set<String> subIds = list.stream().map(e->e[0]).collect(Collectors.toSet());
		for (String subId : subIndexMap.keySet()) {
			Integer in = subIndexMap.get(subId);
			if(!subIds.contains(subId)) {
				cell = row.createCell(in);
				cell.setCellValue(new HSSFRichTextString(FORBIDEN_STR));
			}
		}
	}

	@RequestMapping("/exportXzbIndex/exportErrorExcel")
    public void exportError(HttpServletRequest request, HttpServletResponse response) {
        exportErrorExcel(request, response);
    }

    private String getRemark() {

		String remark = "填写注意：\n" 
						+ "1.根据实际填写任课老师，如果不需要任课老师，请空着\n"
						+ "2.表头所在的行的数据，请保持连续显示，不要出现某一列的科目名称为空\n"
						+ "3.标志 \"X\" 表示此班级不上这门科目，不需要填写教师 \n"
						+ "4.如果存在错误数据，则全部数据都会导入失败\n";
		return remark;
	}
	
	@RequestMapping("/exportXzbIndex/validate")
	@ResponseBody
	public String validate(String filePath, String arrayItemId, String validRowStartNo) {
	    logger.info("模板校验中......");
		if (StringUtils.isBlank(validRowStartNo)) {
			validRowStartNo = "1";
		}
		try{
			//第2行第一列
				
			List<String[]> datas = ExcelUtils.readExcelByRow(filePath,
					Integer.parseInt(validRowStartNo),1);
			//判断第一个是班级列 表头
			List<String> list = ExcelUtils.readExcelOneRow(filePath, 1);
		
			String errorDataMsg="";
			if(CollectionUtils.isNotEmpty(list)) {
				String first=list.get(0);
				if(StringUtils.isNotBlank(first) && first.equals("班级")) {
					//剩余
					if(list.size()==1) {
						errorDataMsg = "模板中表头没有科目列";
					}else {
						//最后一列之前必须有值 防止最后一列空字符
						for(int i=1;i<list.size()-1;i++) {
							if(StringUtils.isBlank(list.get(i))) {
								errorDataMsg = "模板中表头第"+(i+1)+"列为空，请保证表头连续";
								break;
							}
						}
					}
				}else {
					errorDataMsg = "模板中表头第一列不是列名：班级";
					
				}
			}else {
				errorDataMsg = "模板中不存在数据";
				
			}
			
			if (CollectionUtils.isNotEmpty(datas)) {
				String[] realTitles = datas.get(0);
				if (realTitles != null && realTitles[0].equals("班级")) {
					
				}else {
					errorDataMsg = "模板中不存在列名：班级";
				}
			} else {
				errorDataMsg = "模板中不存在数据";
			}
			return errorDataMsg;
		}catch (Exception e) {
			e.printStackTrace();
			return "上传文件不符合模板要求";
		}
	}
}
