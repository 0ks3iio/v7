package net.zdsoft.newgkelective.data.action;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.NewGkArrayResultSaveDto;
import net.zdsoft.newgkelective.data.dto.SimpleClassroomUseageDto;
import net.zdsoft.newgkelective.data.dto.StudentClassDto;
import net.zdsoft.newgkelective.data.dto.TeacherResultDto;
import net.zdsoft.newgkelective.data.dto.TimetableScheduleDto;
import net.zdsoft.newgkelective.data.entity.NewGkArray;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkOpenSubject;
import net.zdsoft.newgkelective.data.entity.NewGkPlaceItem;
import net.zdsoft.newgkelective.data.entity.NewGkTimetable;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableOther;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableTeacher;
import net.zdsoft.newgkelective.data.service.NewGkArrayService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkClassStudentService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkElectiveArrayComputeService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeService;
import net.zdsoft.newgkelective.data.service.NewGkOpenSubjectService;
import net.zdsoft.newgkelective.data.service.NewGkPlaceItemService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableOtherService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableTeacherService;

@Controller
@RequestMapping("/newgkelective/{arrayId}")
public class NewGkElectiveArrayResultAction extends BaseAction{
	@Autowired
	private NewGkArrayService newGkArrayService;
	@Autowired
	private NewGkDivideClassService newGkDivideClassService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private NewGkPlaceItemService newGkPlaceItemService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private NewGkTimetableOtherService newGkTimetableOtherService;
	@Autowired
	private NewGkTimetableTeacherService newGkTimetableTeacherService;
	@Autowired
	private NewGkTimetableService newGkTimetableService;
	@Autowired
	private TeacherRemoteService teacherRemoteService; 
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkClassStudentService newGkClassStudentService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private NewGkDivideService newGkDivideService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private NewGkOpenSubjectService newGkOpenSubjectService;
	@Autowired
	private NewGkElectiveArrayComputeService arrayComputeService;
	@Autowired
	private NewGkLessonTimeService newGkLessonTimeService;
	
	
	@RequestMapping("/arrayResult/pageIndex")
	public String toArrayResultIndex(@PathVariable String arrayId, String type, ModelMap map){
		NewGkArray array = newGkArrayService.findById(arrayId);
		if(array==null){
			return errorFtl(map, "该排课数据已不存在");
		}
		NewGkDivide divide = newGkDivideService.findById(array.getDivideId());
		if(divide==null){
			return errorFtl(map, "该排课对应分班数据已不存在");
		}
		String gradeId = array.getGradeId();
		map.put("arrayId", arrayId);
		map.put("gradeId", gradeId);
		map.put("type", type);
		map.put("openType", divide.getOpenType());
		return "/newgkelective/arrayResult/arrayResultIndex.ftl";
	}
	
	@RequestMapping("/arrayResult/newClassResult")
	public String newClassResult(@PathVariable String arrayId, ModelMap map){
		NewGkArray newGkArray = newGkArrayService.findOne(arrayId);
//		String divideId = newGkArray.getDivideId();
		String gradeId = newGkArray.getGradeId();
		String gradeName = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class).getGradeName();
		String arrayItemId = newGkArray.getPlaceArrangeId();
		List<NewGkPlaceItem> newGkPlaceItemList = newGkPlaceItemService.findByArrayItemId(arrayItemId);		
		Set<String> placeIdSet = new HashSet<String>();
		Map<String, String> clsIdAndPlaceIdMap = new HashMap<String, String>();
		for(NewGkPlaceItem item : newGkPlaceItemList){
			placeIdSet.add(item.getPlaceId());
			clsIdAndPlaceIdMap.put(item.getObjectId(), item.getPlaceId());
		}
//		Map<String, String> placeNameMap = new HashMap<String, String>();
//		if(CollectionUtils.isNotEmpty(placeIdSet)){
//			List<TeachPlace> placeList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceList(placeIdSet.toArray(new String[0])), new TR<List<TeachPlace>>(){});
//		    for(TeachPlace place : placeList){
//		    	placeNameMap.put(place.getId(), place.getPlaceName());
//		    }
//		}
		List<NewGkDivideClass> newGkDivideClassList = newGkDivideClassService.findByDivideIdAndClassType(newGkArray.getUnitId(), arrayId, new String[]{NewGkElectiveConstant.CLASS_TYPE_1},true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
		Set<String> relateIdSet = new HashSet<String>();
		Set<String> studentIdSet = new HashSet<String>();
		for(NewGkDivideClass item : newGkDivideClassList){
			if(StringUtils.isNotBlank(item.getRelateId())){
				relateIdSet.add(item.getRelateId());
			}
			for(String stuId : item.getStudentList()){
				studentIdSet.add(stuId);
			}
		}
		Map<String, Integer> sexMap = new HashMap<String, Integer>();
		if(CollectionUtils.isNotEmpty(studentIdSet)){
			List<Student> studentList = SUtils.dt(studentRemoteService.findPartStudByGradeId(newGkArray.getUnitId(), null, null, studentIdSet.toArray(new String[0])), new TR<List<Student>>(){});
		    for(Student student : studentList){
		    	sexMap.put(student.getId(), student.getSex());
		    }
		}
		Map<String, String> classNameMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(relateIdSet)){
			List<NewGkDivideClass> newGkDivideClassList2 = newGkDivideClassService.findListByIds(relateIdSet.toArray(new String[0]));
			for(NewGkDivideClass item : newGkDivideClassList2){
				classNameMap.put(item.getId(), item.getClassName());
			}
		}
		boolean isXzb=false;
		if(NewGkElectiveConstant.ARRANGE_XZB.equals(newGkArray.getArrangeType())) {
			isXzb=true;
		}
		for(NewGkDivideClass item : newGkDivideClassList){
			if(!isXzb) {
				item.setClassName(gradeName+item.getClassName());
			}
			
//			if(null != clsIdAndPlaceIdMap.get(item.getId())){
//				item.setPlaceName(placeNameMap.get(clsIdAndPlaceIdMap.get(item.getId())));
//			}else{
//				item.setPlaceName("");
//			}
			item.setRelateName(classNameMap.get(item.getRelateId()));
			int girlCount = 0;
			int boyCount = 0;
			Integer kk=new Integer(1);
			List<String> stuids= item.getStudentList();
			if(CollectionUtils.isEmpty(stuids)) {
				item.setStudentCount(0);
				continue;
			}
			item.setStudentCount(stuids.size());
			for(String studentId : stuids){
				if(kk.equals(sexMap.get(studentId))){
					boyCount = boyCount + 1;
				}else{
					girlCount = girlCount + 1;
				}
			}
			item.setGirlCount(girlCount);
			item.setBoyCount(boyCount);
		}

		newGkDivideClassList.sort((x,y)->{
			if(x.getOrderId()==null){
				return 1;
			}else if(y.getOrderId()==null){
				return -1;
			}else if(x.getOrderId().compareTo(y.getOrderId()) != 0){
				return x.getOrderId().compareTo(y.getOrderId());
			}
			return x.getClassName().compareTo(y.getClassName());
		});

		map.put("arrayId", arrayId);
		map.put("newGkDivideClassList", newGkDivideClassList);
		map.put("arrangeType",newGkArray.getArrangeType());
		return "/newgkelective/arrayResult/newClassResult.ftl";
	}	
	
	@RequestMapping("/arrayResult/newClassResultDetailList")
	public String newClassResultDetailList(@PathVariable String arrayId, String classId, String searchClsName, ModelMap map){
		NewGkArray newGkArray = newGkArrayService.findOne(arrayId);
//		String divideId = newGkArray.getDivideId();
		String gradeId = newGkArray.getGradeId();
		String gradeName = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class).getGradeName();
//		String arrayItemId = newGkArray.getPlaceArrangeId();
		//NewGkDivide newGkDivide = newGkDivideService.findOne(divideId);
		//String referScoreId = newGkDivide.getReferScoreId();
//		List<NewGkPlaceItem> newGkPlaceItemList = newGkPlaceItemService.findByArrayItemId(arrayItemId);	
		List<NewGkDivideClass> newGkDivideClassList = newGkDivideClassService.findByDivideIdAndClassType(newGkArray.getUnitId(), arrayId, new String[]{NewGkElectiveConstant.CLASS_TYPE_1}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
		NewGkDivideClass newGkDivideClass = new NewGkDivideClass();
		boolean hasName = StringUtils.isNotEmpty(searchClsName);
		boolean hasCls = StringUtils.isNotEmpty(classId);
		if (CollectionUtils.isNotEmpty(newGkDivideClassList)) {
			Iterator<NewGkDivideClass> cit = newGkDivideClassList.iterator();
			while (cit.hasNext()) {
				NewGkDivideClass item = cit.next(); 
				item.setClassName(gradeName + item.getClassName());
				if (hasName && item.getClassName().indexOf(searchClsName) == -1) {
					cit.remove();
					continue;
				}
				if (hasCls && classId.equals(item.getId())) {
					newGkDivideClass = item;
				}
			} 
		}
//		String placeId = "";
//		if(CollectionUtils.isNotEmpty(newGkPlaceItemList)){
//			for(NewGkPlaceItem item : newGkPlaceItemList){
//				if(classId.equals(item.getObjectId())){
//					placeId = item.getPlaceId();
//				}
//			}			
//		}
//		String placeName = "";
//		if(StringUtils.isNotBlank(placeId)){
//			TeachPlace place = SUtils.dc(teachPlaceRemoteService.findTeachPlaceById(placeId), TeachPlace.class);
//			placeName = place.getPlaceName();
//		}
//		map.put("placeName", placeName);
		map.put("searchClsName", searchClsName);
		map.put("newGkDivideClass", newGkDivideClass);
		map.put("newGkDivideClassList", newGkDivideClassList);
		map.put("arrayId", arrayId);
		map.put("classId", classId);
		return "/newgkelective/arrayResult/newClassResultDetailList.ftl";
	}
	
	@RequestMapping("/arrayResult/newClassResultDetailStuList")
	public String newClassResultDetailStuList(@PathVariable String arrayId, String classId, ModelMap map){
		List<String> studentIdList = new ArrayList<String>();
		String unitId=this.getLoginInfo().getUnitId();
		NewGkDivideClass newGkDivideClass = newGkDivideClassService.findById(unitId, classId, true);
		if(newGkDivideClass != null) {
			studentIdList = newGkDivideClass.getStudentList();
		}
		List<Student> studentList = new ArrayList<Student>();
		if(CollectionUtils.isNotEmpty(studentIdList)){
			List<NewGkDivideClass> newGkDivideClassList3 = newGkDivideClassService.findByDivideIdAndClassType(unitId, arrayId, new String[]{NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_2},false, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
			Set<String> yClassIdSet = new HashSet<String>();
			Map<String, String> yClsNameMap = new HashMap<String, String>();
			
			studentList = SUtils.dt(studentRemoteService.findListByIds(studentIdList.toArray(new String[0])), new TR<List<Student>>(){});
		    for(Student student : studentList){
		    	yClassIdSet.add(student.getClassId());
		    }
		    if(CollectionUtils.isNotEmpty(yClassIdSet)){
		    	List<Clazz> yClassList = SUtils.dt(classRemoteService.findListByIds(yClassIdSet.toArray(new String[0])), new TR<List<Clazz>>(){});
		    	for(Clazz cls : yClassList){
		    		yClsNameMap.put(cls.getId(), cls.getClassNameDynamic());
		    	}
		    }	
		    List<NewGkClassStudent> newGkClassStudentList = newGkClassStudentService.findListByStudentIds(getLoginInfo().getUnitId(),arrayId,studentIdList.toArray(new String[0]));
		    Set<String> nClassIdSet = new HashSet<String>();
		    for(NewGkClassStudent item : newGkClassStudentList){		    	
		    	for(NewGkDivideClass item2 : newGkDivideClassList3){
		    		if(item.getClassId().equals(item2.getId())){
		    			nClassIdSet.add(item.getClassId());
		    		}
		    	}
		    }
		    Map<String, String> clsIdSubIdMap = new HashMap<String, String>();
		    Map<String, Set<String>> clsIdSubIdsMap = new HashMap<String, Set<String>>();
		    Set<String> subIdSet = new HashSet<String>();
		    for(NewGkDivideClass item2 : newGkDivideClassList3){
		    	if(NewGkElectiveConstant.CLASS_TYPE_2.equals(item2.getClassType())){
		    		clsIdSubIdMap.put(item2.getId(), item2.getSubjectIds());
		    		subIdSet.add(item2.getSubjectIds());
		    	}else if(NewGkElectiveConstant.CLASS_TYPE_0.equals(item2.getClassType())){
		    		if(StringUtils.isNotBlank(item2.getSubjectIds())){
		    			String[] subIdArr = item2.getSubjectIds().split(",");
		    			Set<String> subIdsSet = new HashSet<String>();
		    			for(String subId : subIdArr){
		    				subIdsSet.add(subId);
		    				subIdSet.add(subId);
		    			}
		    			clsIdSubIdsMap.put(item2.getId(), subIdsSet);
		    		}
		    	}
		    }
		    Map<String, String> nClassNameMap = new HashMap<String, String>();
		    Map<String, String> nClassTypeMap = new HashMap<String, String>();
		    Map<String, String> nClassSubjectTypeMap = new HashMap<String, String>();
		    List<NewGkDivideClass> newGkDivideClassList2 = new ArrayList<NewGkDivideClass>();
		    if(CollectionUtils.isNotEmpty(nClassIdSet)){
		    	newGkDivideClassList2 = newGkDivideClassService.findListByIds(nClassIdSet.toArray(new String[0]));
		        for(NewGkDivideClass item : newGkDivideClassList2){
		        	nClassNameMap.put(item.getId(), item.getClassName());
		        	nClassTypeMap.put(item.getId(), item.getClassType());
		        	nClassSubjectTypeMap.put(item.getId(), item.getSubjectType());
		        }
		    }
		    for(Student student : studentList){
		    	student.setClassName(yClsNameMap.get(student.getClassId()));
		    	String teachAClassNames = "";
		    	String teachBClassNames = "";
		    	for(NewGkClassStudent item : newGkClassStudentList){
		    		if(student.getId().equals(item.getStudentId()) && "2".equals(nClassTypeMap.get(item.getClassId()))){//教学班
		    			if("A".equals(nClassSubjectTypeMap.get(item.getClassId()))){
		    				teachAClassNames = teachAClassNames + nClassNameMap.get(item.getClassId())+"、";
		    			}else{
		    				teachBClassNames = teachBClassNames + nClassNameMap.get(item.getClassId())+"、";
		    			}
		    		}
		    		if(StringUtils.isNotBlank(teachAClassNames)){
		    			student.setEmail(teachAClassNames.substring(0, teachAClassNames.length()-1));//教学班名称
		    		}
		    		if(StringUtils.isNotBlank(teachBClassNames)){
		    			student.setHomepage(teachBClassNames.substring(0, teachBClassNames.length()-1));//教学班名称
		    		}
		    	}
		    }
		    Collections.sort(studentList,new Comparator<Student>(){
				@Override
				public int compare(Student o1, Student o2) {
					String keyStr= o1.getStudentCode();
					if(StringUtils.isBlank(keyStr)){
						keyStr="0";
					}
					return keyStr.compareTo(o2.getStudentCode());
				}
		    	
		    });
		   
		}
		map.put("studentList", studentList);
		
		return "/newgkelective/arrayResult/newClassResultDetailStuList.ftl"; 
	}
	
	public List<Clazz> getClassList(String arrayId){
		List<Clazz> classList = new ArrayList<Clazz>();
		List<NewGkDivideClass> NewGkDivideClassList = newGkDivideClassService.findByDivideIdAndSourceType(arrayId,NewGkElectiveConstant.CLASS_SOURCE_TYPE2,true);
		Set<String> newClassIdSet = new HashSet<String>();
		for(NewGkDivideClass newClass : NewGkDivideClassList){
			newClassIdSet.add(newClass.getId());
		}
		Set<String> classIdSet = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(newClassIdSet)){
			List<NewGkClassStudent> newGkClassStudentList = newGkClassStudentService.findListByIn("classId", newClassIdSet.toArray(new String[0]));
		    Set<String> studentIdSet = new HashSet<String>();
			for(NewGkClassStudent newGkClassStudent : newGkClassStudentList){
				studentIdSet.add(newGkClassStudent.getStudentId());
		    }
			if(CollectionUtils.isNotEmpty(studentIdSet)){
				List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(studentIdSet.toArray(new String[0])), new TR<List<Student>>(){});
			    for(Student student : studentList){
			    	classIdSet.add(student.getClassId());
			    }
			}
		}
		if(CollectionUtils.isNotEmpty(classIdSet)){
			classList = SUtils.dt(classRemoteService.findListByIds(classIdSet.toArray(new String[0])), new TR<List<Clazz>>(){});
		}
		return classList;
	}	
	
	@RequestMapping("/arrayResult/studentClassResultHead")
	public String studentClassResultHead(@PathVariable String arrayId, ModelMap map){
		//直接根据年级
		NewGkArray array = newGkArrayService.findById(arrayId);
		if(array==null) {
			return errorFtl(map, "本排课方案不存在");
		}
		List<NewGkDivideClass> classList = newGkDivideClassService.findByDivideIdAndClassType(array.getUnitId(), arrayId, new String[]{NewGkElectiveConstant.CLASS_TYPE_1},false, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
		classList.sort((x,y)->{
			if(x.getOrderId()==null){
				return 1;
			}else if(y.getOrderId()==null){
				return -1;
			}else if(x.getOrderId().compareTo(y.getOrderId()) != 0){
				return x.getOrderId().compareTo(y.getOrderId());
			}
			return x.getClassName().compareTo(y.getClassName());
		});
		map.put("classList", classList);
		return "/newgkelective/arrayResult/studentClassResultHead.ftl";
	}
	
	public Map<String, String> getStuIdNewClassNameMap(Set<String> studentIdSet, List<NewGkClassStudent> newGkClassStudentList, List<NewGkDivideClass> NewGkDivideClassList){
		Map<String, String> stuIdNewClassNameMap = new HashMap<String, String>();
		Map<String, String> newClassNameMap = new HashMap<String, String>();
		for(NewGkDivideClass item : NewGkDivideClassList){
			newClassNameMap.put(item.getId(), item.getClassName());
		}
		for(String stuId : studentIdSet){
			String newClassNames = "";			
			for(NewGkClassStudent newGkClassStudent : newGkClassStudentList){
				if(stuId.equals(newGkClassStudent.getStudentId())){
					if(null != newClassNameMap.get(newGkClassStudent.getClassId())){
						newClassNames = newClassNames + newClassNameMap.get(newGkClassStudent.getClassId()) + "、";
					}
				}
			}
			if(StringUtils.isNotBlank(newClassNames)){
				stuIdNewClassNameMap.put(stuId, newClassNames.substring(0, newClassNames.length()-1));
			}else{
				stuIdNewClassNameMap.put(stuId, "");
			}
		}
		return stuIdNewClassNameMap;
	}
	
	public Map<String, String[]> getStuClassCountMap(String unitId, Set<String> studentIdSet, List<NewGkClassStudent> newGkClassStudentList, String arrayId){
		Map<String, String[]> stuIdCountMap = new HashMap<>();		
		Map<String, Set<String>> stuIdClassIdMap = new HashMap<String, Set<String>>();
		for(String stuId : studentIdSet){
			Set<String> classIdSet = new HashSet<String>();
			for(NewGkClassStudent item : newGkClassStudentList){
				if(stuId.equals(item.getStudentId())){
					classIdSet.add(item.getClassId());
				}
			}
			stuIdClassIdMap.put(stuId, classIdSet);
		}
		List<NewGkTimetable> newGkTimetableList = newGkTimetableService.findByArrayId(unitId, arrayId);
		Set<String> timetableIdSet = new HashSet<String>(); 
		for(NewGkTimetable item : newGkTimetableList){
			timetableIdSet.add(item.getId());
		}
		Map<String, Set<String>[]> classIdCountMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(timetableIdSet)){
			List<NewGkTimetableOther> newGkTimetableOtherList = newGkTimetableOtherService.findByArrayId(unitId, arrayId);
			for(NewGkTimetable item : newGkTimetableList){
				Set<String>[] arr = new HashSet[7];
				Set<String> count1 = new HashSet<>();
				Set<String> count2 = new HashSet<>();
				Set<String> count3 = new HashSet<>();
				Set<String> count4 = new HashSet<>();
				Set<String> count5 = new HashSet<>();
				Set<String> count6 = new HashSet<>();
				Set<String> count7 = new HashSet<>();
				for(NewGkTimetableOther other : newGkTimetableOtherList){
					if(item.getId().equals(other.getTimetableId())){
						String code = other.getDayOfWeek()+"-"+other.getPeriodInterval()+"-"+other.getPeriod();
						if(0 == other.getDayOfWeek()){
							count1.add(code);
						}else if(1 == other.getDayOfWeek()){
							count2.add(code);
						}else if(2 == other.getDayOfWeek()){
							count3.add(code);
						}else if(3 == other.getDayOfWeek()){
							count4.add(code);
						}else if(4 == other.getDayOfWeek()){
							count5.add(code);
						}else if(5 == other.getDayOfWeek()){
							count6.add(code);
						}else{
							count7.add(code);
						}
					}
				}
				arr[0] = count1;
				arr[1] = count2;
				arr[2] = count3;
				arr[3] = count4;
				arr[4] = count5;
				arr[5] = count6;
				arr[6] = count7;
				//classIdCountMap.put(item.getClassId(), arr);
				if(!classIdCountMap.containsKey(item.getClassId())) {
					classIdCountMap.put(item.getClassId(), arr);
				}else {
					Set<String>[] strings = classIdCountMap.get(item.getClassId());
					strings[0].addAll(arr[0]);
					strings[1].addAll(arr[1]);
					strings[2].addAll(arr[2]);
					strings[3].addAll(arr[3]);
					strings[4].addAll(arr[4]);
					strings[5].addAll(arr[5]);
					strings[6].addAll(arr[6]);
					classIdCountMap.put(item.getClassId(), strings);
				}
			}
			for(String stuId : studentIdSet){
				Set<String> classIdSet2 = stuIdClassIdMap.get(stuId);
				String[] arr = new String[7];
				int count1 = 0;
				int count2 = 0;
				int count3 = 0;
				int count4 = 0;
				int count5 = 0;
				int count6 = 0;
				int count7 = 0;
				for(String classId : classIdSet2){
					Set<String>[] classCoutArr = classIdCountMap.get(classId);
					if(null!=classCoutArr){
						count1 = count1 + classCoutArr[0].size();
						count2 = count2 + classCoutArr[1].size();
						count3 = count3 + classCoutArr[2].size();
						count4 = count4 + classCoutArr[3].size();
						count5 = count5 + classCoutArr[4].size();
						count6 = count6 + classCoutArr[5].size();						
						count7 = count7 + classCoutArr[6].size();
					}
				}
				arr[0] = String.valueOf(count1);
				arr[1] = String.valueOf(count2);
				arr[2] = String.valueOf(count3);
				arr[3] = String.valueOf(count4);
				arr[4] = String.valueOf(count5);
				arr[5] = String.valueOf(count6);
				arr[6] = String.valueOf(count7);
				stuIdCountMap.put(stuId, arr);
			}
		}
		return stuIdCountMap;
	}
	@RequestMapping("/arrayResult/studentClassResultList")
	public String studentClassResultList(@PathVariable String arrayId, String classId, String studentName, ModelMap map, HttpServletRequest request){
		List<NewGkDivideClass> NewGkDivideClassList = newGkDivideClassService.findByDivideIdAndSourceType(arrayId,NewGkElectiveConstant.CLASS_SOURCE_TYPE2,true);
		Set<String> newClassIdSet = new HashSet<String>();
		for(NewGkDivideClass newClass : NewGkDivideClassList){
			newClassIdSet.add(newClass.getId());
		}
		List<StudentClassDto> studentClassDtoList = new ArrayList<StudentClassDto>();
		List<NewGkClassStudent> newGkClassStudentList = new ArrayList<NewGkClassStudent>();
		List<Student> studentList = new ArrayList<Student>();
		Map<String, String> yClassNameMap = new HashMap<String, String>();
		Map<String, List<String>> studentClassMap = new HashMap<String, List<String>>();
		String unitId=this.getLoginInfo().getUnitId();
		if(CollectionUtils.isNotEmpty(newClassIdSet)){
			newGkClassStudentList = newGkClassStudentService.findListByClassIds(unitId,arrayId,newClassIdSet.toArray(new String[0]));
		    Set<String> studentIdSet = new HashSet<String>();
			for(NewGkClassStudent newGkClassStudent : newGkClassStudentList){
				studentIdSet.add(newGkClassStudent.getStudentId());
		    }
			studentClassMap = EntityUtils.getListMap(newGkClassStudentList, NewGkClassStudent::getStudentId,NewGkClassStudent::getClassId);
			Map<String, String> stuIdNewClassNameMap = getStuIdNewClassNameMap(studentIdSet, newGkClassStudentList, NewGkDivideClassList);
			Map<String, String[]> getStuClassCountMap = getStuClassCountMap(unitId, studentIdSet, newGkClassStudentList, arrayId);
			if(CollectionUtils.isNotEmpty(studentIdSet)){
				studentList = SUtils.dt(studentRemoteService.findPartStudByGradeId(unitId, null, null, studentIdSet.toArray(new String[0])), new TR<List<Student>>(){});
			    Set<String> yClassIdSet = new HashSet<String>();
				for(Student student : studentList){
			    	yClassIdSet.add(student.getClassId());
			    }
				if(CollectionUtils.isNotEmpty(yClassIdSet)){
					List<Clazz> classList = SUtils.dt(classRemoteService.findListByIds(yClassIdSet.toArray(new String[0])), new TR<List<Clazz>>(){});
				    for(Clazz cls : classList){
				    	yClassNameMap.put(cls.getId(), cls.getClassNameDynamic());
				    }
				}
				for(Student student : studentList){
					StudentClassDto studentClassDto = new StudentClassDto();
					studentClassDto.setyClassId(student.getClassId());
					studentClassDto.setyClassName(yClassNameMap.get(student.getClassId()));
					studentClassDto.setStudentName(student.getStudentName());
					studentClassDto.setNewClassName(stuIdNewClassNameMap.get(student.getId()));
					studentClassDto.setStudentId(student.getId());
					studentClassDto.setStudentCode(student.getStudentCode());
					studentClassDto.setArr(getStuClassCountMap.get(student.getId()));
					studentClassDtoList.add(studentClassDto);
			    }
			}
			Collections.sort(studentClassDtoList,new Comparator<StudentClassDto>(){
				@Override
				public int compare(StudentClassDto o1,StudentClassDto o2) {
					return o1.getyClassId().compareTo(o2.getyClassId());
			     }
			});
		}
		List<StudentClassDto> studentClassDtoList2 = new ArrayList<StudentClassDto>();
		if(StringUtils.isBlank(studentName)){
			if(StringUtils.isBlank(classId)){
				studentClassDtoList2 = studentClassDtoList;
			}else{
                for(StudentClassDto studentClassDto : studentClassDtoList){
                	if(studentClassMap.get(studentClassDto.getStudentId()).contains(classId)){
                		studentClassDtoList2.add(studentClassDto);
                	}
                }
			}
		}else{
			List<Student> studentListTemp = SUtils.dt(studentRemoteService.findBySchoolIdIn(studentName, new String[]{getLoginInfo().getUnitId()}), new TR<List<Student>>(){});
		    for(StudentClassDto studentClassDto1 : studentClassDtoList){
		    	for(Student stu2 : studentListTemp){
		    		if(studentClassDto1.getStudentId().equals(stu2.getId())){
		    			studentClassDto1.setyClassName(yClassNameMap.get(studentClassDto1.getyClassId()));
		    			studentClassDtoList2.add(studentClassDto1);
		    		}
		    	}
		    }
		}
		List<StudentClassDto> studentClassDtoList3 = Lists.newArrayList();
		Pagination page=createPagination();
		Map<String, String> paramMap = syncParameters(request);
		int row = NumberUtils.toInt(paramMap.get("_pageSize"));
		if(row<=0){
			page.setPageSize(50);
		}	
		//页面选人渲染较慢，做个分页
		page.setMaxRowCount(studentClassDtoList2.size());
		int index = 0;
		for(StudentClassDto dto:studentClassDtoList2){
			if((page.getPageIndex()-1)*(page.getPageSize())<=index&&index<(page.getPageIndex())*(page.getPageSize())){
				studentClassDtoList3.add(dto);
			}
			index++;
		}
		NewGkArray array = newGkArrayService.findOne(arrayId);
		Grade grade = gradeRemoteService.findOneObjectById(array.getGradeId());
		
		map.put("weekDays", grade.getWeekDays()==null?7:grade.getWeekDays());
		map.put("dayOfWeekMap2", BaseConstants.dayOfWeekMap2);
        map.put("studentClassDtoList", studentClassDtoList3);
        map.put("countList", studentClassDtoList2.size());
        map.put("arrayId", arrayId);
        sendPagination(request, map, page);
		return "/newgkelective/arrayResult/studentClassResultList.ftl";
	}
	
	@RequestMapping("/arrayResult/studentClassResultDetailLeft")
	public String studentClassResultDetailLeft(@PathVariable String arrayId, String studentId, String studentName,ModelMap map){
		map.put("arrayId", arrayId);
		map.put("studentId", studentId);
		map.put("studentName", studentName);
		return "/newgkelective/arrayResult/studentClassResultDetailLeft.ftl";
	}
	@RequestMapping("/arrayResult/studentClassResultDetail")
	public String studentClassResultDetail(@PathVariable String arrayId, String studentName, String studentId, ModelMap map){
		
		String unitId = getLoginInfo().getUnitId();
		List<NewGkClassStudent> newGkClassStudentList2 = newGkClassStudentService.findListByStudentId(getLoginInfo().getUnitId(),arrayId,studentId);
		//所有班级
		Set<String> allClazzIds=new HashSet<>();
		if(CollectionUtils.isNotEmpty(newGkClassStudentList2)) {
			allClazzIds=EntityUtils.getSet(newGkClassStudentList2,e->e.getClassId());
		}
		
		map.put("entityHead", "学生");
		map.put("entityName", studentName);
		
		return makeClassResult(unitId,arrayId, map, allClazzIds,null);
	}

	private String makeClassResult(String unitId, String arrayId, ModelMap map, Set<String> allClazzIds, Set<String> timetableIdes) {
		List<Course> courseList = new ArrayList<>();
		//begin ..............
		List<NewGkTimetableOther> newGkTimetableOtherList = newGkTimetableService.findArrayResultBy(unitId, arrayId, allClazzIds,timetableIdes, courseList);
        
		map.put("courseList", courseList);
        map.put("newGkTimetableOtherList", newGkTimetableOtherList);
        
        String newClassName=newGkTimetableOtherList.stream().map(e->e.getClassName()).distinct().collect(Collectors.joining(","));
        
        NewGkArray newGkArray = newGkArrayService.findOne(arrayId);
//		String divideId = newGkArray.getDivideId();
        String gradeId = newGkArray.getGradeId();
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
        
        map.put("newClassName", newClassName);
		
		map.put("grade", grade);
		
		Map<String, Integer> piMap = getIntervalMap(grade);
		map.put("weekDays", grade.getWeekDays());
		map.put("piMap", piMap);
		map.put("intervalNameMap", BaseConstants.PERIOD_INTERVAL_Map2);
		map.put("dayOfWeekMap", BaseConstants.dayOfWeekMap2);
		
		return "/newgkelective/arrayResult/studentClassResultDetail.ftl";
	}

	@RequestMapping("/arrayResult/subjectTimetableResult/getDetailData")
	@ControllerInfo("班级查看课表List")
	public String showTimetableList(@PathVariable String arrayId, String classId, ModelMap map) {
		HashSet<String> classIds = new HashSet<>();
		classIds.add(classId);
		
		map.put("INDEX", "CLASS");
		String unitId = getLoginInfo().getUnitId();
		return makeClassResult(unitId, arrayId, map, classIds,null);
	}
	
	private Map<String, Integer> getIntervalMap(Grade grade) {
		Integer mmCount = grade.getMornPeriods();
		Integer amCount = grade.getAmLessonCount();
		Integer pmCount = grade.getPmLessonCount();
		Integer nightCount = grade.getNightLessonCount();
	
		Map<String,Integer> piMap = new LinkedHashMap<>();
		Function<Integer,Integer> fun = e-> e!=null?e:0;
		piMap.put(BaseConstants.PERIOD_INTERVAL_1, fun.apply(mmCount));
		piMap.put(BaseConstants.PERIOD_INTERVAL_2, fun.apply(amCount));
		piMap.put(BaseConstants.PERIOD_INTERVAL_3, fun.apply(pmCount));
		piMap.put(BaseConstants.PERIOD_INTERVAL_4, fun.apply(nightCount));
		return piMap;
	}

	@RequestMapping("/arrayResult/teacher/page")
	@ControllerInfo(value = "教师上课查询列表")
	public String showTeacherResult(@PathVariable("arrayId") String arrayId, String subjectId, String teacherName,
			ModelMap map) {
		map.put("arrayId", arrayId);
		String unitId = getLoginInfo().getUnitId();
		List<NewGkTimetable> timetableList = newGkTimetableService.findByArrayId(unitId,arrayId);
		List<Course> subs = null;
		if(CollectionUtils.isNotEmpty(timetableList)){
			Set<String> sids = EntityUtils.getSet(timetableList, NewGkTimetable::getSubjectId);
			subs = SUtils.dt(courseRemoteService.findListByIds(sids.toArray(new String[0])), new TR<List<Course>>() {});
		}
		map.put("subs", subs);
		map.put("subjectId", subjectId);
		map.put("teacherName", teacherName);
		return "/newgkelective/arrayResult/teacherResultIndex.ftl";
	}
	
	@RequestMapping("/arrayResult/teacherList/page")
	@ControllerInfo(value = "教师上课查询列表")
	public String showTeacherResultList(@PathVariable("arrayId") String arrayId,String teacherName,
			String subjectId, ModelMap map) {
		map.put("arrayId", arrayId);
		if(StringUtils.isNotBlank(teacherName)){
			teacherName=teacherName.trim();
		}
		//教师   说授课科目   周课时 
		Map<String, TeacherResultDto> teacherResult = getTeacherResultDtoAll(arrayId,teacherName, subjectId);
		NewGkArray array = newGkArrayService.findOne(arrayId);
		Grade grade = gradeRemoteService.findOneObjectById(array.getGradeId());
		
		map.put("dayOfWeekMap2", BaseConstants.dayOfWeekMap2);
		map.put("weekDays", grade.getWeekDays()==null?7:grade.getWeekDays());
		map.put("size", teacherResult.size());
		map.put("teacherResult", teacherResult);
		return "/newgkelective/arrayResult/teacherResultList.ftl";
	}
	/**
	 * 组装教师上课时间列表
	 * @param arrayId
	 * @param teacherName
	 * @param subjectId
	 * @return
	 */
	private Map<String, TeacherResultDto> getTeacherResultDtoAll(String arrayId,String teacherName, String subjectId) {
		Map<String, TeacherResultDto> returnMap=new HashMap<String, TeacherResultDto>();
		//根据arrayId获取NewGkTimetable
		String unitId = getLoginInfo().getUnitId();
		List<NewGkTimetable> timetableList = newGkTimetableService.findByArrayId(unitId, arrayId);
		if(CollectionUtils.isEmpty(timetableList)){
			return returnMap;
		}
		if(StringUtils.isNotEmpty(subjectId)) {
			Iterator<NewGkTimetable> it = timetableList.iterator();
			while(it.hasNext()) {
				NewGkTimetable nt = it.next();
				if(!subjectId.equals(nt.getSubjectId())) {
					it.remove();
				}
			}
		}
		newGkTimetableService.makeTeacher(timetableList);
		newGkTimetableService.makeTime(unitId, arrayId, timetableList);
		//教师所教科目
		Map<String,Set<String>> subjectTeacher=new HashMap<String,Set<String>>();
		//节次数
		Map<String,Map<String,Set<String>>> timeTeacher=new HashMap<>();
		Map<String,Set<String>> allTimeTeacher=new HashMap<>();
		Set<String> teacherIds=new HashSet<String>();
		Set<String> subjectIds=new HashSet<String>();
		
		for(NewGkTimetable t:timetableList){
			if(CollectionUtils.isEmpty(t.getTeacherIdList())){
				continue;
			}
			//key=周，val=上课数据
			Map<String,Set<String>> map=new HashMap<>();
			if(CollectionUtils.isNotEmpty(t.getTimeList())){
				for(NewGkTimetableOther item:t.getTimeList()){
					String code = item.getDayOfWeek()+""+item.getPeriodInterval()+""+item.getPeriod();
					if(!map.containsKey(item.getDayOfWeek()+"")){
						map.put(item.getDayOfWeek()+"", new HashSet<>());
					}
					map.get(item.getDayOfWeek()+"").add(code);
				}
			}
			subjectIds.add(t.getSubjectId());
			for(String s:t.getTeacherIdList()){
				teacherIds.add(s);
				if(!subjectTeacher.containsKey(s)){
					subjectTeacher.put(s, new HashSet<String>());
				}
				if(!allTimeTeacher.containsKey(s)){
					allTimeTeacher.put(s, new HashSet<>());
				}
				subjectTeacher.get(s).add(t.getSubjectId());
				if(map.size()>0){
					//时间
					if(!timeTeacher.containsKey(s)){
						timeTeacher.put(s, new HashMap<>());
					}
					Map<String,Set<String>> map1 = timeTeacher.get(s);
					//map1 组合map
					for(Entry<String,Set<String>> itemKey:map.entrySet()){
						if(!map1.containsKey(itemKey.getKey())){
							map1.put(itemKey.getKey(), new HashSet<>());
						}
						map1.get(itemKey.getKey()).addAll(itemKey.getValue());
						allTimeTeacher.get(s).addAll(itemKey.getValue());
					}
					timeTeacher.put(s, map1);
				}
				
				
			}
			
		}
		if(teacherIds.size()<=0){
			return returnMap;
		}
		List<Teacher> teacherList=new ArrayList<Teacher>();
		if(StringUtils.isNotBlank(teacherName)){
			//teacherName
			//根据教师姓名查询老师Id 模糊查找
			teacherList = SUtils.dt(teacherRemoteService.findByNameCodeLike("%"+teacherName+"%", null, unitId),new TR<List<Teacher>>() {});
			
		}else{
			teacherList = SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[0])),new TR<List<Teacher>>() {});
		}
		if(CollectionUtils.isEmpty(teacherList)){
			return returnMap;
		}
		Map<String, String> courseMap = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
		
		TeacherResultDto dto=null;
		for(Teacher t:teacherList){
			if(!teacherIds.contains(t.getId())){
				continue;
			}
			dto=new TeacherResultDto();
			dto.setTeacherId(t.getId());
			dto.setTeacherName(t.getTeacherName());
			if(allTimeTeacher.containsKey(t.getId())){
				dto.setPeriodNums(allTimeTeacher.get(t.getId()).size());
			}else{
				dto.setPeriodNums(0);
			}
			if(timeTeacher.containsKey(t.getId())){
				HashMap<String, Integer> hashMap = new HashMap<>();
				timeTeacher.get(t.getId()).entrySet().stream().forEach(e->hashMap.put(e.getKey(), e.getValue().size()));
				dto.setSubjectNumberOfDay(hashMap);
			}
			dto.setSubjectNames(new ArrayList<String>());
			String courseNames="";
			if(subjectTeacher.containsKey(t.getId())){
				Set<String> set = subjectTeacher.get(t.getId());
				for(String s:set){
					if(courseMap.containsKey(s)){
						if(StringUtils.isBlank(dto.getOneSubjectId())) {
							dto.setOneSubjectId(s);
						}
						dto.getSubjectNames().add(courseMap.get(s));
						courseNames=courseNames+","+courseMap.get(s);
					}
				}
			}
			if(StringUtils.isNotBlank(courseNames)){
				courseNames=courseNames.substring(1);
			}
			dto.setCourseNames(courseNames);
			returnMap.put(t.getId(), dto);
		}
		return returnMap;
	}
	
	@RequestMapping("/arrayResult/{teacherId}/teacherIndex/page")
	public String showOneTeacherResultIndex(@PathVariable("arrayId") String arrayId,
			@PathVariable("teacherId") String teacherId, String subjectId, String teacherName,String oneSubjectId,
			ModelMap map) {
		map.put("arrayId", arrayId);
		String unitId = getLoginInfo().getUnitId();
		//oneSubjectId 用于后面确定老师node
		List<Teacher> teacherList=new ArrayList<Teacher>();
		List<NewGkTimetable> timetableList = newGkTimetableService.findByArrayId(unitId, arrayId);
		if(CollectionUtils.isNotEmpty(timetableList)){
			Set<String> ids = EntityUtils.getSet(timetableList,NewGkTimetable::getId);
			List<NewGkTimetableTeacher> timetableTeacherList = newGkTimetableTeacherService.findByTimetableIds(ids.toArray(new String[]{}));
			if(CollectionUtils.isNotEmpty(timetableTeacherList)){
				Set<String> tids = EntityUtils.getSet(timetableTeacherList,NewGkTimetableTeacher::getTeacherId);
				teacherList = SUtils.dt(teacherRemoteService.findListByIds(tids.toArray(new String[0])),new TR<List<Teacher>>() {});
			}
		}
		map.put("teacherId", teacherId);
		map.put("teacherList", teacherList);

		Set<String> subjectIds = timetableList.stream().map(e->e.getSubjectId()).collect(Collectors.toSet());
		List<Course> courseList = courseRemoteService.findListObjectByIds(subjectIds.toArray(new String[0]));
		map.put("courseList", courseList);
		
		map.put("subjectId", subjectId);
		map.put("teacherName", teacherName);
		map.put("oneSubjectId", oneSubjectId);
		return "/newgkelective/arrayResult/teacherScheduleIndex.ftl";
	}
	@RequestMapping("/arrayResult/{teacherId}/teacher/page")
	public String showOneTeacherResult(@PathVariable("arrayId") String arrayId,
			@PathVariable("teacherId") String teacherId,
			ModelMap map) {
		
		String unitId = getLoginInfo().getUnitId();
		List<NewGkTimetableTeacher> timeTeachers = newGkTimetableTeacherService.findByTeacherIds(Stream.of(teacherId).collect(Collectors.toSet()), arrayId);
		Set<String> timetableIds = EntityUtils.getSet(timeTeachers, NewGkTimetableTeacher::getTimetableId);
		List<NewGkTimetable> timetables = newGkTimetableService.findListBy(NewGkTimetable.class, null, null, "id", timetableIds.toArray(new String[0]), new String[] {"id"});
		Set<String> ttIds = EntityUtils.getSet(timetables, NewGkTimetable::getId);
		
		Teacher teacher = teacherRemoteService.findOneObjectById(teacherId);
		map.put("entityHead", "教师");
		map.put("entityName", teacher.getTeacherName());
		map.put("INDEX", "TEACHER");
		
		return makeClassResult(unitId, arrayId, map, null,ttIds);
	}

	@RequestMapping("/arrayResult/classroomUseage")
	public String seachClassroomUseage(@PathVariable String arrayId,ModelMap map){
		//TODO 显示教室使用情况
		
		NewGkArray array = newGkArrayService.findOne(arrayId);
		List<SimpleClassroomUseageDto> classroomUseageDtos =  newGkTimetableOtherService.seachClassroomUseage(array.getUnitId(), arrayId);
		Grade grade = gradeRemoteService.findOneObjectById(array.getGradeId());
		
		map.put("dayOfWeekMap2", BaseConstants.dayOfWeekMap2);
		map.put("weekDays", grade.getWeekDays()==null?7:grade.getWeekDays());
		map.put("useageDtos", classroomUseageDtos);
		map.put("arrayId", arrayId);
		return "/newgkelective/arrayResult/placeUseageStatus.ftl";
	}
	@RequestMapping("/arrayResult/classroomUseDetail")
	public String showClassroomDetailUI(@PathVariable String arrayId,String placeId,String placeName,ModelMap map){
		List<SimpleClassroomUseageDto> classroomUseageDtoList=new ArrayList<>();
		List<SimpleClassroomUseageDto> allClassroomUseageDtoList =  newGkTimetableOtherService.seachClassroomUseage(getLoginInfo().getUnitId(), arrayId);
		if(CollectionUtils.isNotEmpty(allClassroomUseageDtoList)) {
			if(StringUtils.isNotBlank(placeName)) {
				//过滤
				for(SimpleClassroomUseageDto dto:allClassroomUseageDtoList) {
					if(dto.getPlaceName().indexOf(placeName)>-1) {
						classroomUseageDtoList.add(dto);
					}
				}
			}else {
				classroomUseageDtoList.addAll(allClassroomUseageDtoList);
			}
		}
		map.put("arrayId", arrayId);
		map.put("placeId", placeId);
		map.put("placeName", placeName);
		map.put("classroomUseageDtoList", classroomUseageDtoList);
		return "/newgkelective/arrayResult/placeUseDetailLeft.ftl";
	}
	

	@RequestMapping("/arrayResult/classroomUseDetailList")
	public String getRoomUseageDetail(@PathVariable String arrayId, String placeId, ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		List<String> timetableIds = newGkTimetableService.findIdByPlaceIds(unitId,arrayId,new String[] {placeId});
		
		TeachPlace teachPlace = teachPlaceRemoteService.findOneObjectById(placeId);
		map.put("entityHead", "教室");
		map.put("entityName", teachPlace.getPlaceName());
		
		map.put("INDEX", "PLACE");
		return makeClassResult(unitId, arrayId, map, null, new HashSet<>(timetableIds));
	}
	
	@RequestMapping("/arrayResult/page")
	@ControllerInfo(value = "班级/教师/场地  总课表")
	public String showResultAll(@PathVariable String arrayId, String type, ModelMap map) {

		String mess=makeDate(arrayId, map);
		if(StringUtils.isNotBlank(mess)){
			return errorFtl(map, mess);
		}
		String unitId = getLoginInfo().getUnitId();
		NewGkArray array = newGkArrayService.findOne(arrayId);
		Grade grade = gradeRemoteService.findOneObjectById(array.getGradeId());
		map.put("arrayId", arrayId);
		map.put("dayOfWeekMap2", BaseConstants.dayOfWeekMap2);
		map.put("weekDays", grade.getWeekDays()==null?7:grade.getWeekDays());
		map.put("type", type);
		
		if("1".equals(type)) {
			// 班级
			showClassAll(unitId,arrayId,"1","1",map,false);
		}else if("2".equals(type)) {
			// 教师
			showTeacherAll(unitId, arrayId,"1","1",map,false);
		}else {
			// 场地
			showPlaceAll(unitId, arrayId,"1","1",map,false);
		}
		return "/newgkelective/arrayResult/showAllResult.ftl";
	}
	
	private void showClassAll(String unitId, String arrayId,String showTeacher, String showPlace, ModelMap map, boolean isExport){
		
		//行政班与教学班
		List<NewGkDivideClass> clazzList = newGkDivideClassService.findByDivideIdAndClassType(unitId, arrayId,
				new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_3,NewGkElectiveConstant.CLASS_TYPE_4}, true,
				NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
		List<NewGkDivideClass> xzbList=new ArrayList<NewGkDivideClass>();
		//key:行政班id,value:行政班下的学生所在的所有教学班
		Map<String,Set<String>> jxbIdByXzbId=new HashMap<String,Set<String>>();
		Map<String,NewGkDivideClass> classMap=new HashMap<String,NewGkDivideClass>();
		makeJxbInXzb(clazzList,xzbList,jxbIdByXzbId,classMap);

		
		//具体上课时间
		List<NewGkTimetable> timeTableList = newGkTimetableService.findByArrayId(unitId, arrayId);
		newGkTimetableService.makeTeacher(timeTableList);
		newGkTimetableService.makeTime(unitId, arrayId, timeTableList);
		//key classId
		Map<String,Set<String>> timeTableIdByClassId=new HashMap<String,Set<String>>();
		Map<String,NewGkTimetable> timeTableMap=new HashMap<String,NewGkTimetable>();
		Set<String> teacherIds=new HashSet<String>();
		Set<String> placeIds=new HashSet<String>();
		Set<String> subjectIds=new HashSet<String>();
		//不存在
		for(NewGkTimetable tt:timeTableList){
			timeTableMap.put(tt.getId(), tt);
			if("1".equals(showTeacher)&& CollectionUtils.isNotEmpty(tt.getTeacherIdList())){
				teacherIds.addAll(tt.getTeacherIdList());
			}
			if("1".equals(showPlace)&& CollectionUtils.isNotEmpty(tt.getTimeList())){
				for (NewGkTimetableOther time:tt.getTimeList()) {
					if(StringUtils.isNotBlank(time.getPlaceId())){
						placeIds.add(time.getPlaceId());
					}
				}
			}
			subjectIds.add(tt.getSubjectId());
			if(!timeTableIdByClassId.containsKey(tt.getClassId())){
				timeTableIdByClassId.put(tt.getClassId(), new HashSet<String>());
			}
			timeTableIdByClassId.get(tt.getClassId()).add(tt.getId());
		}
		
		//组装参数用的
		Map<String, Teacher> teacherMap = new HashMap<String,Teacher>();
		if(CollectionUtils.isNotEmpty(teacherIds)){
			List<Teacher> tList = SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[]{})), new TR<List<Teacher>>(){});
			teacherMap=EntityUtils.getMap(tList, e->e.getId());
		}
		Map<String, Course> courseMap = new HashMap<String,Course>();
		if(CollectionUtils.isNotEmpty(subjectIds)){
			List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{})), new TR<List<Course>>(){});
			courseMap=EntityUtils.getMap(courseList,  e->e.getId());
		}
		Map<String, TeachPlace> placeMap = new HashMap<String,TeachPlace>();
		if(CollectionUtils.isNotEmpty(placeIds)){
			List<TeachPlace> pList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceList(placeIds.toArray(new String[]{})), new TR<List<TeachPlace>>(){});
			placeMap=EntityUtils.getMap(pList,  e->e.getId());
		}
		
		//组装key1:classIds key2:time(1_1_1)
		Map<String,Map<String, TimetableScheduleDto>> scheduleDtoMap=new HashMap<String,Map<String, TimetableScheduleDto>>();
		Map<String, TimetableScheduleDto> dtoMap;
		for (NewGkDivideClass newGkDivideClass : xzbList) {
			dtoMap=new HashMap<String, TimetableScheduleDto>();
			//组装行政班课程
			makeClassShowSchedule(true, newGkDivideClass, timeTableIdByClassId, timeTableMap, teacherMap, courseMap, placeMap, dtoMap, isExport);
			if(jxbIdByXzbId.containsKey(newGkDivideClass.getId())){
				for(String jxbId:jxbIdByXzbId.get(newGkDivideClass.getId())){
					NewGkDivideClass divideClass = classMap.get(jxbId);
					makeClassShowSchedule(false, divideClass, timeTableIdByClassId, timeTableMap, teacherMap, courseMap, placeMap, dtoMap, isExport);
				}
			}
			scheduleDtoMap.put(newGkDivideClass.getId(), dtoMap);
		}
		xzbList.sort((x,y)->{
			if(x.getOrderId()==null){
				return 1;
			}else if(y.getOrderId()==null){
				return -1;
			}else if(x.getOrderId().compareTo(y.getOrderId()) != 0){
				return x.getOrderId().compareTo(y.getOrderId());
			}
			return x.getClassName().compareTo(y.getClassName());
		});
		List<String[]> entityList = xzbList.stream().map(e->new String[] {e.getId(),e.getClassName()}).collect(Collectors.toList());
		map.put("xzbList", xzbList);
		map.put("entityList", entityList);
		map.put("scheduleDtoMap", scheduleDtoMap);
	}
	
	private void makeJxbInXzb(List<NewGkDivideClass> clazzList,
			List<NewGkDivideClass> xzbList,
			Map<String, Set<String>> jxbIdByXzbId,
			Map<String, NewGkDivideClass> classMap) {
		Map<String,Set<String>> jxbclassIdByStu=new HashMap<String,Set<String>>();
		for (NewGkDivideClass newGkDivideClass : clazzList) {
			classMap.put(newGkDivideClass.getId(), newGkDivideClass);
			if(NewGkElectiveConstant.CLASS_TYPE_1.equals(newGkDivideClass.getClassType())){
				xzbList.add(newGkDivideClass);
			}else{
				if(CollectionUtils.isNotEmpty(newGkDivideClass.getStudentList())){
					for (String s : newGkDivideClass.getStudentList()) {
						if(!jxbclassIdByStu.containsKey(s)){
							jxbclassIdByStu.put(s, new HashSet<String>());
						}
						jxbclassIdByStu.get(s).add(newGkDivideClass.getId());
					}
				}
			}
		}
		for (NewGkDivideClass newGkDivideClass : xzbList) {
			if(CollectionUtils.isNotEmpty(newGkDivideClass.getStudentList())){
				if(!jxbIdByXzbId.containsKey(newGkDivideClass.getId())){
					jxbIdByXzbId.put(newGkDivideClass.getId(), new HashSet<String>());
				}
				for (String s : newGkDivideClass.getStudentList()) {
					if(jxbclassIdByStu.containsKey(s)){
						Set<String> set = jxbclassIdByStu.get(s);
						jxbIdByXzbId.get(newGkDivideClass.getId()).addAll(set);
					}
				}
			}
		
		}
		
	}

	private void makeClassShowSchedule(boolean isXZB,NewGkDivideClass newGkDivideClass,
			Map<String,Set<String>> timeTableIdByClassId,Map<String,NewGkTimetable> timeTableMap,
			Map<String, Teacher> teacherMap,Map<String, Course> courseMap,Map<String, TeachPlace> placeMap,
			Map<String, TimetableScheduleDto> dtoMap, boolean isExport){
		if(timeTableIdByClassId.containsKey(newGkDivideClass.getId())){
			Set<String> ii = timeTableIdByClassId.get(newGkDivideClass.getId());
			for(String i:ii){
				NewGkTimetable ttt = timeTableMap.get(i);
				//教师
				String tName="";
				if(CollectionUtils.isNotEmpty(ttt.getTeacherIdList())){
					for(String tn:ttt.getTeacherIdList()){
						if(teacherMap.containsKey(tn)){
							tName=tName+","+teacherMap.get(tn).getTeacherName();
						}
					}
					if(StringUtils.isNotBlank(tName)){
						tName=tName.substring(1);
					}
				}
				String cName=courseMap.get(ttt.getSubjectId())==null?"":courseMap.get(ttt.getSubjectId()).getSubjectName();
				//时间
				if(CollectionUtils.isNotEmpty(ttt.getTimeList())){
					for (NewGkTimetableOther time:ttt.getTimeList()) {
						String pName="";
						if(StringUtils.isNotBlank(time.getPlaceId())){
							if(placeMap.containsKey(time.getPlaceId())){
								pName=placeMap.get(time.getPlaceId()).getPlaceName();
							}
						}
						String key=time.getDayOfWeek()+"_"+time.getPeriodInterval()+"_"+time.getPeriod();
						TimetableScheduleDto dto = dtoMap.get(key);
						if(dto==null){
							dto=new TimetableScheduleDto();
							dtoMap.put(key, dto);
						}
						List<String> showScheduleList = dto.getShowSchedule();
						if(CollectionUtils.isEmpty(showScheduleList)){
							showScheduleList=new ArrayList<String>();
							dto.setShowSchedule(showScheduleList);
						}
						Integer fWeek = time.getFirstsdWeek();
						//组装数据 科目 (班级,教师，场地)
						if(isXZB){
							showScheduleList.add(makeStr(cName,"",tName,pName,fWeek,isExport));
						}else{
							showScheduleList.add(makeStr(cName,newGkDivideClass.getClassName(),tName,pName,fWeek,isExport));
						}
					}
				}	
			}
		}
	}
	
	
	
	
	//组装数据 科目 (班级,教师，场地)
	private String makeStr(String subjectName,String className,String teacherName,String placeName, Integer fWeek, boolean isExport){
		StringBuilder mm= new StringBuilder();
		if(StringUtils.isNotBlank(className)){
			if(!isExport)
				mm.append("<span class='addition_class'>&nbsp");
			mm.append(className);
			if(!isExport)
				mm.append("</span>&nbsp");
		}
		if(StringUtils.isNotBlank(teacherName)){
			if(!isExport)
				mm.append("<span class='addition_teacher'>&nbsp");
			mm.append(teacherName);
			if(!isExport)
				mm.append("</span>&nbsp");
		}
		if(StringUtils.isNotBlank(placeName)){
			if(!isExport)
				mm.append("<span class='addition_place'>&nbsp");
			mm.append(placeName);
			if(!isExport)
				mm.append("</span>&nbsp");
		}
		if(StringUtils.isNotBlank(mm)){
			// <sapn class='addition'>（
//			mm.setCharAt(0, '(');
			if(!isExport) {
				mm.insert(0, "<sapn class='addition'>(");
				mm.insert(0, subjectName);
				mm.append(")</span>");
			}else {
				mm.insert(0, "(");
				mm.insert(0, subjectName);
				mm.append(")");
			}
		}else {
			mm.append(subjectName);
		}
		
		if(fWeek!=null){
			if(NewGkElectiveConstant.FIRSTSD_WEEK_1==fWeek){
				mm.append("(单)");
			}else if(NewGkElectiveConstant.FIRSTSD_WEEK_2==fWeek){
				mm.append("(双)");
			}
		}
		return mm.toString();
	}
	
	private void showTeacherAll(String unitId, String arrayId,String showClass, String showPlace, ModelMap map, boolean isExport){
		//行政班与教学班
		Map<String,NewGkDivideClass> classMap=new HashMap<>();
		if("1".equals(showClass)) {
			List<NewGkDivideClass> clazzList = newGkDivideClassService.findByDivideIdAndClassType(unitId,
					arrayId, new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2}, true, 
					NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
			classMap=EntityUtils.getMap(clazzList, e->e.getId());
		}

		//具体上课时间
		List<NewGkTimetable> timeTableList = newGkTimetableService.findByArrayId(unitId, arrayId);
		newGkTimetableService.makeTeacher(timeTableList);
		newGkTimetableService.makeTime(unitId, arrayId, timeTableList);
		//key teacherId
		Map<String,Set<String>> timeTableIdByTeacherId=new HashMap<String,Set<String>>();
		Map<String,NewGkTimetable> timeTableMap=new HashMap<String,NewGkTimetable>();
		Set<String> teacherIds=new HashSet<String>();
		Set<String> placeIds=new HashSet<String>();
		Set<String> subjectIds=new HashSet<String>();

		for(NewGkTimetable tt:timeTableList){
			timeTableMap.put(tt.getId(), tt);
			if(CollectionUtils.isNotEmpty(tt.getTeacherIdList())){
				for(String tId:tt.getTeacherIdList()){
					if(!timeTableIdByTeacherId.containsKey(tId)){
						timeTableIdByTeacherId.put(tId, new HashSet<String>());
					}
					timeTableIdByTeacherId.get(tId).add(tt.getId());
					teacherIds.add(tId);
				}
			}
			if("1".equals(showPlace)&& CollectionUtils.isNotEmpty(tt.getTimeList())){
				for (NewGkTimetableOther time:tt.getTimeList()) {
					if(StringUtils.isNotBlank(time.getPlaceId())){
						placeIds.add(time.getPlaceId());
					}
				}
			}
			subjectIds.add(tt.getSubjectId());
			
		}
		//组装参数用的
		List<Teacher> tList=new ArrayList<Teacher>();
		if(CollectionUtils.isNotEmpty(teacherIds)){
			tList = SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[]{})), new TR<List<Teacher>>(){});
		}
		List<String[]> entityList = tList.stream().map(e->new String[] {e.getId(),e.getTeacherName()}).collect(Collectors.toList());
		map.put("entityList", entityList);
		map.put("teacherList", tList);
		Map<String, String> courseMap = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
		Map<String, TeachPlace> placeMap = new HashMap<String,TeachPlace>();
		if(CollectionUtils.isNotEmpty(placeIds)){
			List<TeachPlace> pList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceList(placeIds.toArray(new String[]{})), new TR<List<TeachPlace>>(){});
			placeMap=EntityUtils.getMap(pList, e->e.getId());
		}
		
		//组装key1:teacherId key2:time(1_1_1)
		Map<String,Map<String, TimetableScheduleDto>> scheduleDtoMap=new HashMap<String,Map<String, TimetableScheduleDto>>();
		Map<String, TimetableScheduleDto> dtoMap;
		for (Teacher teacher : tList) {
			dtoMap=new HashMap<String, TimetableScheduleDto>();
			//组装课程
			if(timeTableIdByTeacherId.containsKey(teacher.getId())){
				Set<String> ii = timeTableIdByTeacherId.get(teacher.getId());
				for(String i:ii){
					NewGkTimetable ttt = timeTableMap.get(i);
					//课程
					String cName=courseMap.get(ttt.getSubjectId());
					//班级
					String className=classMap.get(ttt.getClassId())==null?"":classMap.get(ttt.getClassId()).getClassName();
					//时间
					if(CollectionUtils.isNotEmpty(ttt.getTimeList())){
						for (NewGkTimetableOther time:ttt.getTimeList()) {
							String pName="";
							if(StringUtils.isNotBlank(time.getPlaceId())){
								if(placeMap.containsKey(time.getPlaceId())){
									pName=placeMap.get(time.getPlaceId()).getPlaceName();
								}
							}
							String key=time.getDayOfWeek()+"_"+time.getPeriodInterval()+"_"+time.getPeriod();
							TimetableScheduleDto dto = dtoMap.get(key);
							if(dto==null){
								dto=new TimetableScheduleDto();
								dtoMap.put(key, dto);
							}
							List<String> showScheduleList = dto.getShowSchedule();
							if(CollectionUtils.isEmpty(showScheduleList)){
								showScheduleList=new ArrayList<String>();
								dto.setShowSchedule(showScheduleList);
							}
							Integer fWeek = time.getFirstsdWeek();
							//组装数据 科目 (班级,场地)
							showScheduleList.add(makeStr(cName,className,"",pName,fWeek,isExport));
						}
					}	
				}
			}
			scheduleDtoMap.put(teacher.getId(), dtoMap);
		}
		
		map.put("scheduleDtoMap", scheduleDtoMap);
	}
	

	private void showPlaceAll(String unitId, String arrayId,String showClass, String showTeacher, ModelMap map, boolean isExport){
		//行政班与教学班
		Map<String,NewGkDivideClass> classMap=new HashMap<>();
		if("1".equals(showTeacher)) {
			List<NewGkDivideClass> clazzList = newGkDivideClassService.findByDivideIdAndClassType(unitId, 
					arrayId, new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2}, true, 
					NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
			classMap=EntityUtils.getMap(clazzList, e->e.getId());
		}

		//具体上课时间
		List<NewGkTimetable> timeTableList = newGkTimetableService.findByArrayId(unitId, arrayId);
		newGkTimetableService.makeTeacher(timeTableList);
		newGkTimetableService.makeTime(unitId, arrayId, timeTableList);
		//key teacherId
		Map<String,Set<String>> timeTableIdByPlaceId=new HashMap<String,Set<String>>();
		Map<String,NewGkTimetable> timeTableMap=new HashMap<String,NewGkTimetable>();
		Set<String> teacherIds=new HashSet<String>();
		Set<String> placeIds=new HashSet<String>();
		Set<String> subjectIds=new HashSet<String>();

		for(NewGkTimetable tt:timeTableList){
			timeTableMap.put(tt.getId(), tt);
			if("1".equals(showTeacher)&& CollectionUtils.isNotEmpty(tt.getTeacherIdList())){
				teacherIds.addAll(tt.getTeacherIdList());
			}
			if(CollectionUtils.isNotEmpty(tt.getTimeList())){
				for (NewGkTimetableOther time:tt.getTimeList()) {
					if(StringUtils.isNotBlank(time.getPlaceId())){
						if(!timeTableIdByPlaceId.containsKey(time.getPlaceId())){
							timeTableIdByPlaceId.put(time.getPlaceId(), new HashSet<String>());
						}
						timeTableIdByPlaceId.get(time.getPlaceId()).add(tt.getId());
						placeIds.add(time.getPlaceId());
					}
				}
			}
			subjectIds.add(tt.getSubjectId());
			
		}
		//组装参数用的
		Map<String, String> teacherMap=new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(teacherIds)){
			teacherMap  = SUtils.dt(teacherRemoteService.findPartByTeacher(teacherIds.toArray(new String[teacherIds.size()])),new TypeReference<Map<String, String>>(){});
		}
		Map<String, String> courseMap = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
		List<TeachPlace> placeList=new ArrayList<TeachPlace>();
		if(CollectionUtils.isNotEmpty(placeIds)){
			placeList = teachPlaceRemoteService.findListObjectBy(TeachPlace.class, null, null, "id",
					placeIds.toArray(new String[]{}),new String[] {"id","placeName"});
		}
		List<String[]> entityList = placeList.stream().map(e->new String[] {e.getId(),e.getPlaceName()}).collect(Collectors.toList());
		map.put("entityList", entityList);
		map.put("placeList", placeList);
		//组装key1:teacherId key2:time(1_1_1)
		Map<String,Map<String, TimetableScheduleDto>> scheduleDtoMap=new HashMap<String,Map<String, TimetableScheduleDto>>();
		Map<String, TimetableScheduleDto> dtoMap;
		for (TeachPlace place : placeList) {
			dtoMap=new HashMap<String, TimetableScheduleDto>();
			//组装课程
			if(timeTableIdByPlaceId.containsKey(place.getId())){
				Set<String> ii = timeTableIdByPlaceId.get(place.getId());
				for(String i:ii){
					NewGkTimetable ttt = timeTableMap.get(i);
					//课程
					String cName=courseMap.get(ttt.getSubjectId());
					//班级
					String className=classMap.get(ttt.getClassId())==null?"":classMap.get(ttt.getClassId()).getClassName();
					//教师
					String tName="";
					if(CollectionUtils.isNotEmpty(ttt.getTeacherIdList())){
						for(String tn:ttt.getTeacherIdList()){
							if(teacherMap.containsKey(tn)){
								tName=tName+","+teacherMap.get(tn);
							}
						}
						if(StringUtils.isNotBlank(tName)){
							tName=tName.substring(1);
						}
					}
					//时间
					if(CollectionUtils.isNotEmpty(ttt.getTimeList())){
						for (NewGkTimetableOther time:ttt.getTimeList()) {
							String key=time.getDayOfWeek()+"_"+time.getPeriodInterval()+"_"+time.getPeriod();
							TimetableScheduleDto dto = dtoMap.get(key);
							if(dto==null){
								dto=new TimetableScheduleDto();
								dtoMap.put(key, dto);
							}
							List<String> showScheduleList = dto.getShowSchedule();
							if(CollectionUtils.isEmpty(showScheduleList)){
								showScheduleList=new ArrayList<String>();
								dto.setShowSchedule(showScheduleList);
							}
							Integer fWeek = time.getFirstsdWeek();
							if(place.getId().equals(time.getPlaceId())){
								//组装数据 科目 (班级,场地)
								showScheduleList.add(makeStr(cName,className,tName,"",fWeek,isExport));
							}
							
						}
					}	
				}
			}
			scheduleDtoMap.put(place.getId(), dtoMap);
		}
		
		map.put("scheduleDtoMap", scheduleDtoMap);
	}
	
	private String makeDate(String arrayId,ModelMap map){
		//显示时间
		NewGkArray array = newGkArrayService.findById(arrayId);
		if(array==null){
			return "该排课方案已经不存在";
		}
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(array.getGradeId()), Grade.class);
		if(grade==null){
			return "该排课方案下的年级不存在";
		}
		Map<String,Integer> timesMap=new HashMap<String,Integer>();
		int oneDayAll=0;
		Integer ms = grade.getMornPeriods();
		if(ms!=null && ms>0){
			timesMap.put(BaseConstants.PERIOD_INTERVAL_1, ms);
			oneDayAll=oneDayAll+ms;
		}
		Integer am = grade.getAmLessonCount();
		if(am!=null && am>0){
			timesMap.put(BaseConstants.PERIOD_INTERVAL_2, am);
			oneDayAll=oneDayAll+am;
		}
		Integer pm = grade.getPmLessonCount();
		if(pm!=null && pm>0){
			timesMap.put(BaseConstants.PERIOD_INTERVAL_3, pm);
			oneDayAll=oneDayAll+pm;
		}
		Integer nm = grade.getNightLessonCount();
		if(nm!=null && nm>0){
			timesMap.put(BaseConstants.PERIOD_INTERVAL_4, nm);
			oneDayAll=oneDayAll+nm;
		}
		if(timesMap.size()<=0){
			return  "先去设置年级的上课时间";
		}
		map.put("timesMap", timesMap);
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/arrayResult/classroomAll/export")
    @ControllerInfo(value = "导出班级总课表" )
	public String exportAllResult(@PathVariable String arrayId,String type, String showTeacher, String showClass, String showPlace,
			HttpServletResponse response,ModelMap map){
		
		//时间
		makeDate(arrayId, map);
		Map<String,Integer> timesMap=(Map<String, Integer>) map.get("timesMap");
		Map<String,Map<String, TimetableScheduleDto>> scheduleDtoMap=new HashMap<String,Map<String, TimetableScheduleDto>>();
		Map<String,String> nameMap=new LinkedHashMap<String,String>();
		
		//type 1:行政班 2教师 3教室
		String  title="";
		String headtile="";
		String unitId = getLoginInfo().getUnitId();
		if("1".equals(type)){
			title="班级总课表";
			showClassAll(unitId, arrayId, showTeacher, showPlace, map,true);
			scheduleDtoMap=(Map<String, Map<String, TimetableScheduleDto>>) map.get("scheduleDtoMap");
			headtile="班级";
			List<NewGkDivideClass> xzbList=(List<NewGkDivideClass>) map.get("xzbList");
			for(NewGkDivideClass xzb:xzbList){
				nameMap.put(xzb.getId(), xzb.getClassName());
			}
		}else if("2".equals(type)){
			title="教师总课表";
			showTeacherAll(unitId, arrayId, showClass, showPlace, map ,true);
			scheduleDtoMap=(Map<String, Map<String, TimetableScheduleDto>>) map.get("scheduleDtoMap");
			headtile="教师";
			List<Teacher> teacherList=(List<Teacher>)map.get("teacherList");
			for(Teacher t:teacherList){
				nameMap.put(t.getId(), t.getTeacherName());
			}
		}else{
			title="教室总课表";
			showPlaceAll(unitId, arrayId, showClass,showTeacher, map ,true);
			scheduleDtoMap=(Map<String, Map<String, TimetableScheduleDto>>) map.get("scheduleDtoMap");
			headtile="教室";
			List<TeachPlace> placeList=(List<TeachPlace>) map.get("placeList");
			for(TeachPlace p:placeList){
				nameMap.put(p.getId(), p.getPlaceName());
			}
		}
		NewGkArray array = newGkArrayService.findById(arrayId);
		if(array!=null){
			Grade gd = gradeRemoteService.findOneObjectById(array.getGradeId());
			writeExcel(timesMap, title, headtile, nameMap, scheduleDtoMap, response,gd.getWeekDays());
		}else{ 
			writeExcel(timesMap, title, headtile, nameMap, scheduleDtoMap, response,7);
		}

    	return "";
	}
	
	private String nameDay(int i){
		switch (i) {
		case 0:
			return "星期一";
		case 1:
			return "星期二";
		case 2:
			return "星期三";
		case 3:
			return "星期四";
		case 4:
			return "星期五";
		case 5:
			return "星期六";
		case 6:
			return "星期天";
		default:
			return "未找到";
		}
	}
	
	private void writeExcel(Map<String,Integer> timesMap,String title,String cellName,Map<String,String> itemMap,Map<String,Map<String, TimetableScheduleDto>> scheduleDtoMap,
			HttpServletResponse response,Integer xingqi){
	      if(xingqi == null){
	      	xingqi = 7;
		  }
	      int allDay=0;
	      Integer ms = timesMap.get(BaseConstants.PERIOD_INTERVAL_1);
	      Integer am = timesMap.get(BaseConstants.PERIOD_INTERVAL_2);
	      Integer pm = timesMap.get(BaseConstants.PERIOD_INTERVAL_3);
	      Integer nm = timesMap.get(BaseConstants.PERIOD_INTERVAL_4);
	      if(ms==null){
	    	  ms=0;
	      }else{
	    	  allDay=allDay+ms; 
	      }
	      
	      if(am==null){
	    	  am=0;
	      }else{
	    	  allDay=allDay+am; 
	      }
	      if(pm==null){
	    	  pm=0;
	      }else{
	    	  allDay=allDay+pm; 
	      }
	      if(nm==null){
	    	  nm=0;
	      }else{
	    	  allDay=allDay+nm; 
	      }
	      int allLength=allDay*xingqi+1;//1:班级或者教师或者教师
	      HSSFWorkbook workbook = new HSSFWorkbook();
	      HSSFSheet sheet = workbook.createSheet("总课表");
	      //标题 居中
          HSSFRow row = sheet.createRow(0);
          row.setHeight((short)(2 * 256)); 
          HSSFCell cell = row.createCell(0);
          HSSFCellStyle centerStype=workbook.createCellStyle();
          centerStype.setAlignment(HorizontalAlignment.CENTER);//水平居中
          centerStype.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
          centerStype.setWrapText(true);//自动换行
          
          cell.setCellStyle(centerStype);
          cell.setCellType(HSSFCell.CELL_TYPE_STRING);//文本
          cell.setCellValue(new HSSFRichTextString(title));
          sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, allLength-1));//列从0开始 包括最后一列  //合并
          //星期
          row = sheet.createRow(1);
          row.setHeight((short)(2 * 256)); 
          cell = row.createCell(0);
          cell.setCellType(HSSFCell.CELL_TYPE_STRING);//文本
          cell.setCellValue(new HSSFRichTextString(cellName));//班级或者教师或者教师
          cell.setCellStyle(centerStype);
          int j=1;
          for(int ii=0;ii<xingqi;ii++){
        	  cell = row.createCell(j);
        	  cell.setCellStyle(centerStype);
              cell.setCellType(HSSFCell.CELL_TYPE_STRING);//文本
              sheet.addMergedRegion(new CellRangeAddress(1, 1,j, j+allDay-1));//合并allDay列
              cell.setCellValue(new HSSFRichTextString(nameDay(ii)));//班级或者教师或者教师
              j=j+allDay;
          }   
          sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));//合并两行
          row = sheet.createRow(2);
          row.setHeight((short)(2 * 256)); 
          sheet.setDefaultColumnWidth(30);
          sheet.setColumnWidth(0, 15 * 256);
          j=1;
          //上课天数
          for(int ii=0;ii<xingqi;ii++){
        	  for(int iii=1;iii<=allDay;iii++){
        		  cell = row.createCell(j);
                  cell.setCellType(HSSFCell.CELL_TYPE_STRING);//文本
                  cell.setCellStyle(centerStype);
                  cell.setCellValue(new HSSFRichTextString(String.valueOf(iii)));
                  j++;
        	  }
        	 
          }
          int rowIndex=3;
          String key1;
          Map<String, TimetableScheduleDto> dtoMap;
          TimetableScheduleDto dto;
          List<String> showList;
          int y=0;
          String mess;
          int enterCnt=0;//用于设定行高
          for(Entry<String, String> item:itemMap.entrySet()){
        	  enterCnt=2;
        	  y=0;
        	  row = sheet.createRow(rowIndex);
        	  String key=item.getKey();
        	  String value=item.getValue();
        	  
        	  cell = row.createCell(y);
              cell.setCellType(HSSFCell.CELL_TYPE_STRING);//文本
              cell.setCellStyle(centerStype);
              cell.setCellValue(new HSSFRichTextString(String.valueOf(value)));
              y++;
        	
        	  dtoMap = scheduleDtoMap.get(key);
        	  if(dtoMap==null){
        		  dtoMap=new HashMap<String, TimetableScheduleDto>();
        	  }
        	  for(int k=0;k<xingqi;k++){
        		  if(ms>0){
            		  for(int p=1;p<=ms;p++){
            			  key1=k+"_"+BaseConstants.PERIOD_INTERVAL_1+"_"+p;
            			  cell = row.createCell(y);
            			  cell.setCellStyle(centerStype);
                          cell.setCellType(HSSFCell.CELL_TYPE_STRING);//文本
                          mess="";
                          if(dtoMap.containsKey(key1)){
                        	  dto = dtoMap.get(key1);
                        	  showList = dto.getShowSchedule();
                        	  if(CollectionUtils.isNotEmpty(showList)){
                        		  if(enterCnt<showList.size()){
                        			  enterCnt=showList.size();
                        		  }
                        		  mess=nameList(showList);
                        	  }
                          } 
                          cell.setCellValue(new HSSFRichTextString(mess));
                          y++;
            		  }
            	  }
        		  
        		  if(am>0){
            		  for(int p=1;p<=am;p++){
            			  key1=k+"_"+BaseConstants.PERIOD_INTERVAL_2+"_"+p;
            			  cell = row.createCell(y);
            			  cell.setCellStyle(centerStype);
                          cell.setCellType(HSSFCell.CELL_TYPE_STRING);//文本
                          mess="";
                          if(dtoMap.containsKey(key1)){
                        	  dto = dtoMap.get(key1);
                        	  showList = dto.getShowSchedule();
                        	  if(CollectionUtils.isNotEmpty(showList)){
                        		  if(enterCnt<showList.size()){
                        			  enterCnt=showList.size();
                        		  }
                        		  mess=nameList(showList);
                        	  }
                          }
                          cell.setCellValue(new HSSFRichTextString(mess));
                          y++;
            		  }
            	  }
        		  if(pm>0){
            		  for(int p=1;p<=pm;p++){
            			  key1=k+"_"+BaseConstants.PERIOD_INTERVAL_3+"_"+p;
            			  cell = row.createCell(y);
            			  cell.setCellStyle(centerStype);
                          cell.setCellType(HSSFCell.CELL_TYPE_STRING);//文本
                          mess="";
                          if(dtoMap.containsKey(key1)){
                        	  dto = dtoMap.get(key1);
                        	  showList = dto.getShowSchedule();
                        	  if(CollectionUtils.isNotEmpty(showList)){
                        		  if(enterCnt<showList.size()){
                        			  enterCnt=showList.size();
                        		  }
                        		  mess=nameList(showList);
                        	  }
                          }
                          cell.setCellValue(new HSSFRichTextString(mess));
                          y++;
            		  }
            	  }
        		  if(nm>0){
            		  for(int p=1;p<=nm;p++){
            			  key1=k+"_"+BaseConstants.PERIOD_INTERVAL_4+"_"+p;
            			  cell = row.createCell(y);
            			  cell.setCellStyle(centerStype);
                          cell.setCellType(HSSFCell.CELL_TYPE_STRING);//文本
                          mess="";
                          if(dtoMap.containsKey(key1)){
                        	  dto = dtoMap.get(key1);
                        	  showList = dto.getShowSchedule();
                        	  if(CollectionUtils.isNotEmpty(showList)){
                        		  if(enterCnt<showList.size()){
                        			  enterCnt=showList.size();
                        		  }
                        		  mess=nameList(showList);
                        	  }
                          }
                          cell.setCellValue(new HSSFRichTextString(mess));
                          y++;
            		  }
            	  }
        	  }
        	  
        	  row.setHeight((short)(enterCnt * 256)); 
        	  rowIndex++;
          }
          
          
          try{
        	  ExportUtils.outputData(workbook, title, response); // 导出文件
          }catch(Exception e){
        	  
          }

	}

	private String nameList(List<String> showList) {
		String str="";
		for(String s:showList){
			if(StringUtils.isNotBlank(str)){
				str=str+"\n"+s;
			}else{
				str=s;
			}
		}
		return str;		
	}
	
	@RequestMapping("/arrayResult/toChangeStuChooseIndex")
	public String changeStuChooseIndex(@PathVariable String arrayId, ModelMap map){
		NewGkArray array = newGkArrayService.findById(arrayId);
		if(array==null){
			return errorFtl(map, "该排课数据已不存在");
		}
		NewGkDivide divide = newGkDivideService.findById(array.getDivideId());
		if(divide==null){
			return errorFtl(map, "该排课对应分班数据已不存在");
		}
		List<String> subjectIdlist = newGkChoRelationService.findByChoiceIdAndObjectType(divide.getUnitId(), divide.getChoiceId(), NewGkElectiveConstant.CHOICE_TYPE_01);
		List<Course> courseList=new ArrayList<Course>();
		if(CollectionUtils.isNotEmpty(subjectIdlist)){
			courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdlist.toArray(new String[0])), new TR<List<Course>>(){});
		}else{
			courseList= SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()), new TR<List<Course>>(){});
		}
		//某个年级下的所有正常学生
		List<Student> studentList = SUtils.dt(studentRemoteService.findPartStudByGradeId(array.getUnitId(), array.getGradeId(),null, null), new TR<List<Student>>(){});
		map.put("studentList", studentList);
		map.put("courseList", courseList);
		map.put("arrayId", arrayId);
		return "/newgkelective/arrayResult/changeStuChooseIndex.ftl";
	}
	
	@RequestMapping("/arrayResult/toChangeStuChooseList")
	public String changeStuChooseList(@PathVariable String arrayId,String subjectIds,String studentId, ModelMap map){
		
		NewGkArray array = newGkArrayService.findById(arrayId);
		if(array==null){
			return errorFtl(map, "该排课数据已不存在");
		}
		NewGkDivide divide = newGkDivideService.findById(array.getDivideId());
		if(divide==null){
			return errorFtl(map, "该排课对应分班数据已不存在");
		}
		//开设科目AB
		List<NewGkOpenSubject> list = newGkOpenSubjectService.findByDivideIdAndSubjectTypeIn(divide.getId(), new String[]{NewGkElectiveConstant.SUBJECT_TYPE_A,NewGkElectiveConstant.SUBJECT_TYPE_B});
		if(CollectionUtils.isEmpty(list)){
			return errorFtl(map, "需要安排的学考选考科目不存在");
		}
		Set<String> needA=new HashSet<String>();
		Set<String> needB=new HashSet<String>();
		String[] chooseSubjectId = subjectIds.split(",");
		List<String> chooseList = Arrays.asList(chooseSubjectId);
		for(NewGkOpenSubject open:list){
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(open.getSubjectType())){
				if(chooseList.contains(open.getSubjectId())){
					needA.add(open.getSubjectId());
				}
			}else{
				if(!chooseList.contains(open.getSubjectId())){
					needB.add(open.getSubjectId());
				}
			}
		}
		List<NewGkDivideClass> NewGkDivideClassList = newGkDivideClassService.findByDivideIdAndClassType(array.getUnitId(), arrayId, new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_2}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
		//学生调整 前提批次
		Map<String,List<NewGkDivideClass>> classByStuId=new HashMap<String,List<NewGkDivideClass>>();
		Set<String> notStuId=new HashSet<String>();
		Map<String,String> groupIdToxzbId=new HashMap<String,String>();
		//全固定 与半固定 行政班id与组合id是一一固定的 
		for(NewGkDivideClass clazz:NewGkDivideClassList){
			if(CollectionUtils.isEmpty(clazz.getStudentList())){
				continue;
			}
			if(NewGkElectiveConstant.CLASS_TYPE_1.equals(clazz.getClassType())){
				//行政班
				if(StringUtils.isNotBlank(clazz.getRelateId())){
					groupIdToxzbId.put(clazz.getRelateId(), clazz.getId());
				}
				continue;
			}
			clazz.setStudentCount(clazz.getStudentList().size());
			if(NewGkElectiveConstant.CLASS_TYPE_0.equals(clazz.getClassType())){
				//组合
				String clazzSubIds = clazz.getSubjectIds();
				
				boolean f=true;
				//A
				if(!NewGkElectiveConstant.SUBJTCT_TYPE_0.equals(clazz.getSubjectType())) {
					String[] arr = clazzSubIds.split(",");
					for(String ss:arr){
						if(StringUtils.isNotBlank(ss)) {
							if(!chooseList.contains(ss)){
								//不符合
								f=false;
								break;
							}
						}
					}
				}
				
				//B
				if(f && StringUtils.isNotBlank(clazz.getSubjectIdsB())){
					String[] arr = clazz.getSubjectIdsB().split(",");
					//取反
					for(String ss:arr){
						if(StringUtils.isNotBlank(ss)) {
							if(chooseList.contains(ss)){
								//不符合
								f=false;
								break;
							}
						}
					}
				}
				if(!f){
					for(String stuId:clazz.getStudentList()){
						notStuId.add(stuId);
						classByStuId.remove(stuId);
					}
					continue;
				}
				for(String stuId:clazz.getStudentList()){
					if(notStuId.contains(stuId)){
						classByStuId.remove(stuId);
						continue;
					}
					if(!classByStuId.containsKey(stuId)){
						classByStuId.put(stuId,new ArrayList<NewGkDivideClass>());
					}
					classByStuId.get(stuId).add(clazz);
				}
			}else{
				if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(clazz.getSubjectType())){
					//A
					if(!needA.contains(clazz.getSubjectIds())){
						for(String stuId:clazz.getStudentList()){
							notStuId.add(stuId);
							classByStuId.remove(stuId);
						}
						continue;
					}
					for(String stuId:clazz.getStudentList()){
						if(notStuId.contains(stuId)){
							classByStuId.remove(stuId);
							continue;
						}
						if(!classByStuId.containsKey(stuId)){
							classByStuId.put(stuId,new ArrayList<NewGkDivideClass>());
						}
						classByStuId.get(stuId).add(clazz);
					}
				}else{
					//B
					if(!needB.contains(clazz.getSubjectIds())){
						for(String stuId:clazz.getStudentList()){
							notStuId.add(stuId);
							classByStuId.remove(stuId);
						}
						continue;
					}
					for(String stuId:clazz.getStudentList()){
						if(notStuId.contains(stuId)){
							classByStuId.remove(stuId);
							continue;
						}
						if(!classByStuId.containsKey(stuId)){
							classByStuId.put(stuId,new ArrayList<NewGkDivideClass>());
						}
						classByStuId.get(stuId).add(clazz);
					}
				}
			}
		}
		//剩余的学生就是符合条件的学生
		Map<String,List<NewGkDivideClass>> chooseClazzList=new HashMap<String,List<NewGkDivideClass>>();
		if(classByStuId.size()>0){
			for(Entry<String, List<NewGkDivideClass>> chooseItem:classByStuId.entrySet()){
				List<NewGkDivideClass> list1 = chooseItem.getValue();
				if(CollectionUtils.isNotEmpty(list1)){
					Collections.sort(list1, new Comparator<NewGkDivideClass>() {
						@Override
						public int compare(NewGkDivideClass o1, NewGkDivideClass o2) {
							if(o1 == null || o2 == null) {
								return 0;
							}
							if(o1.getClassType().equals(o2.getClassType())) {
								if(StringUtils.isBlank(o1.getSubjectType())) {
									return 1;
								}else if(StringUtils.isBlank(o2.getSubjectType())){
									return -1;
								}else{
									if(o1.getSubjectType().contains(o2.getSubjectType())) {
										return o1.getSubjectIds().compareTo(o2.getSubjectIds());
									}else {
										return o1.getSubjectType().compareTo(o2.getSubjectType());
									}
									
								}
							}else {
								return o1.getClassType().compareTo(o2.getClassType());
							}
							
						}
					});
				}
				String key="";
				for(NewGkDivideClass c:list1){
					if(NewGkElectiveConstant.CLASS_TYPE_0.equals(c.getClassType())){
						key=key+","+groupIdToxzbId.get(c.getId());//行政班id 之前分班模式没有改的时候 可能会有问题 暂时不处理
					}
					key=key+","+c.getId();
				}
				key=key.substring(1);
				if(!chooseClazzList.containsKey(key)){
					chooseClazzList.put(key, new ArrayList<NewGkDivideClass>());
					chooseClazzList.get(key).addAll(list1);
				}
			}
		}
		//暂时只考虑AB
		List<NewGkTimetable> timeTableList = newGkTimetableService.findByArrayId(array.getUnitId(), arrayId);
		newGkTimetableService.makeTeacher(timeTableList);
		Map<String,Set<String>> teacherIdByClassId=new HashMap<String,Set<String>>();
		Set<String> teacherIds=new HashSet<String>();
		for(NewGkTimetable n:timeTableList){
			if(NewGkElectiveConstant.CLASS_TYPE_2.equals(n.getClassType())){
				if(CollectionUtils.isNotEmpty(n.getTeacherIdList())){
					teacherIdByClassId.put(n.getClassId(), new HashSet<String>());
					teacherIdByClassId.get(n.getClassId()).addAll(n.getTeacherIdList());
					teacherIds.addAll(n.getTeacherIdList());
				}
			}
		}
		
		Map<String,String> teacherNamesByClassId=new HashMap<String,String>();
		if(CollectionUtils.isNotEmpty(teacherIds)){
			List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[0])), new TR<List<Teacher>>(){});
    	    Map<String, String> teacherNameMap=new HashMap<String, String>();
			for(Teacher teacher : teacherList){
    	    	teacherNameMap.put(teacher.getId(), teacher.getTeacherName());
    	    }
			for(Entry<String, Set<String>> set:teacherIdByClassId.entrySet()){
				String key = set.getKey();
				Set<String> values = set.getValue();
				String tName="";
				for(String t:values){
					if(teacherNameMap.containsKey(t)){
						tName=tName+"、"+teacherNameMap.get(t);
					}
				}
				if(StringUtils.isNotBlank(tName)){
					tName=tName.substring(1);
				}
				teacherNamesByClassId.put(key, tName);
			}
		}
		map.put("teacherNamesByClassId", teacherNamesByClassId);
		map.put("chooseClazzList", chooseClazzList);
		map.put("arrayId", arrayId);
		return "/newgkelective/arrayResult/changeStuChooseList.ftl";
	}
	@RequestMapping("/arrayResult/saveChangeStuChoose")
	@ResponseBody
	public String saveChangeStuChoose(@PathVariable String arrayId,String stuId,String chosenClassIds) {
		String[] chooseClass = chosenClassIds.split(",");
		try {
			
			newGkDivideClassService.saveChangeByStuId(arrayId,stuId,chooseClass,getLoginInfo().getUnitId());
			
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败");
		}
		
		return success("");
	}
	
	
	/**
	 * 将时间点 字符串 转换为 文字说明
	 * Eg:
	 * 		2-2-4 : 周3上午 第4节 
	 * @param arrayId
	 * @return
	 */

	@RequestMapping("/arrayResult/copy")
	@ResponseBody
	public String copyArrayResult(@PathVariable String arrayId) {
		NewGkArray oldArray = newGkArrayService.findOne(arrayId);
		int maxTimes = newGkArrayService.findMaxByGradeId(oldArray.getUnitId(), oldArray.getGradeId(),oldArray.getArrangeType());
		NewGkArray newArray = new NewGkArray();
		
		newArray.setId(UuidUtils.generateUuid());
		newArray.setModifyTime(new Date());
		newArray.setCreationTime(new Date());
		
		newArray.setUnitId(oldArray.getUnitId());
		newArray.setGradeId(oldArray.getGradeId());
		newArray.setTimes(maxTimes + 1);
		newArray.setIsDeleted(oldArray.getIsDeleted());
		newArray.setDivideId(oldArray.getDivideId());
		newArray.setPlaceArrangeId(oldArray.getPlaceArrangeId());
		newArray.setLessonArrangeId(oldArray.getLessonArrangeId());
		newArray.setArrayName("排课方案 "+newArray.getTimes()+" --复制于排课方案 "+oldArray.getTimes());
		newArray.setStat(oldArray.getStat());
		newArray.setArrangeType(oldArray.getArrangeType());
		newArray.setIsDefault(0);
		
		// 班级 学生
		List<NewGkDivideClass> allClassList = newGkDivideClassService.findByDivideIdAndClassType(oldArray.getUnitId(), oldArray.getId(),
				null, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, true);
		allClassList = allClassList.stream().filter(e->!(NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())&&
				StringUtils.isBlank(e.getOldDivideClassId()))).collect(Collectors.toList());
		boolean oldVersion = allClassList.stream().allMatch(e->StringUtils.isBlank(e.getOldDivideClassId()));
		Map<String, String> toOldIdMap = null;
		if(!oldVersion) {
			Map<String, String>  toOldIdMapT = EntityUtils.getMap(allClassList, NewGkDivideClass::getId,NewGkDivideClass::getOldDivideClassId);
			allClassList.forEach(e->{
				e.setRelateId(toOldIdMapT.get(e.getRelateId()));
				e.setParentId(toOldIdMapT.get(e.getParentId()));
				e.setId(e.getOldDivideClassId());
			});
			toOldIdMap = toOldIdMapT;
		}
		
		
		List<NewGkClassStudent> addStuList = new ArrayList<>();
		List<NewGkDivideClass> newClassList = new ArrayList<>();
		Map<String, NewGkDivideClass> oldNewClassMap = arrayComputeService.copyDivideClassToArray(allClassList, 
				newArray, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, addStuList, newClassList);
		if(oldVersion) {
			newClassList.forEach(e->e.setOldDivideClassId(null));
		}
		
		// 课表 老师
		List<NewGkTimetable> timeTableList = newGkTimetableService.findByArrayId(oldArray.getUnitId(), arrayId);
		newGkTimetableService.makeTime(oldArray.getUnitId(), arrayId, timeTableList);
		newGkTimetableService.makeTeacher(timeTableList);
		
		NewGkTimetable ntt;
		NewGkTimetableOther ntto;
		NewGkTimetableTeacher newTeacher;
		List<NewGkTimetable> newTimetableList = new ArrayList<>();
		List<NewGkTimetableOther> newTimetableOtherList = new ArrayList<>();
		List<NewGkTimetableTeacher> newTimetableTeacherList = new ArrayList<>();
		for (NewGkTimetable ott : timeTableList) {
			String oldCid = ott.getClassId();
			if(toOldIdMap != null) {
				oldCid = toOldIdMap.get(ott.getClassId());
			}
			NewGkDivideClass nowClass = oldNewClassMap.get(oldCid);
			
//			NewGkDivideClass newClass = oldNewClassMap.get(ott.getClassId());
			if(nowClass == null) {
				throw new RuntimeException("找不到原始班级");
			}
			ntt = new NewGkTimetable();
			ntt.setId(UuidUtils.generateUuid());
			ntt.setModifyTime(new Date());
			ntt.setCreationTime(new Date());
			
			ntt.setArrayId(newArray.getId());
			ntt.setClassId(nowClass.getId());
			ntt.setClassType(ott.getClassType());
			ntt.setSubjectId(ott.getSubjectId());
			ntt.setSubjectType(ott.getSubjectType());
			ntt.setUnitId(oldArray.getUnitId());
			
			newTimetableList.add(ntt);
			// 教师
			List<String> teacherIdList = ott.getTeacherIdList();
			if(CollectionUtils.isNotEmpty(teacherIdList)) {
				for (String teacherId : teacherIdList) {
					if(StringUtils.isBlank(teacherId)) {
						continue;
					}
					newTeacher = new NewGkTimetableTeacher();
					newTeacher.setId(UuidUtils.generateUuid());
					newTeacher.setTeacherId(teacherId);
					newTeacher.setTimetableId(ntt.getId());
					newTimetableTeacherList.add(newTeacher);
				}
			}
			
			// timetableOther
			List<NewGkTimetableOther> timeList = ott.getTimeList();
			if(CollectionUtils.isNotEmpty(timeList)) {
				for (NewGkTimetableOther tto : timeList) {
					ntto = new NewGkTimetableOther();
					ntto.setId(UuidUtils.generateUuid());
					ntto.setTimetableId(ntt.getId());
					ntto.setPlaceId(tto.getPlaceId());
					ntto.setDayOfWeek(tto.getDayOfWeek());
					ntto.setPeriodInterval(tto.getPeriodInterval());
					ntto.setPeriod(tto.getPeriod());
					ntto.setFirstsdWeek(tto.getFirstsdWeek()==null?NewGkElectiveConstant.FIRSTSD_WEEK_3:tto.getFirstsdWeek());
					ntto.setUnitId(oldArray.getUnitId());
					newTimetableOtherList.add(ntto);
				}
			}
		}
		
		NewGkArrayResultSaveDto dto=new NewGkArrayResultSaveDto();
		dto.setInsertOtherList(newTimetableOtherList);
		dto.setInsertTimeTableList(newTimetableList);
		dto.setInsertTeacherList(newTimetableTeacherList);
		dto.setArrayId(newArray.getId());
		dto.setNewGkArray(newArray);
		dto.setInsertClassList(newClassList);
		dto.setInsertStudentList(addStuList);
		
		try {
			newGkTimetableService.saveCopyArray(dto);
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		
		return returnSuccess();
	}
	
}
