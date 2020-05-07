package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dto.DyStuWeekCheckPerformanceDto;
import net.zdsoft.stuwork.data.dto.DyStudentDto;
import net.zdsoft.stuwork.data.entity.DyBusinessOption;
import net.zdsoft.stuwork.data.entity.DyStuMilitaryTraining;
import net.zdsoft.stuwork.data.entity.DyStuWeekCheckPerformance;
import net.zdsoft.stuwork.data.service.DyBusinessOptionService;
import net.zdsoft.stuwork.data.service.DyPermissionService;
import net.zdsoft.stuwork.data.service.DyStuWeekCheckPerformanceService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
@Controller
@RequestMapping("/stuwork")
public class DyStuWeekCheckPerformanceAction extends BaseAction{
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private DyBusinessOptionService dyBusinessOptionService;
	@Autowired
	private DyStuWeekCheckPerformanceService dyStuWeekCheckPerformanceService;
	@Autowired
	private DyPermissionService dyPermissionService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	
    @RequestMapping("/weekCheckPerformance/pageIndex")
	public String pageIndex(ModelMap map){
    	Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
		if(null!=sem){
			String acadyear = sem.getAcadyear();
			int semester = sem.getSemester();
			DateInfo dateInfo =  SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), acadyear, semester, new Date()), DateInfo.class);
			if(dateInfo == null){
				return errorFtl(map, "所选日期不在当前学期的节假日设置范围内！");
			}
			List<DateInfo> dateInfoList = SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester(getLoginInfo().getUnitId(), acadyear, semester), new TR<List<DateInfo>>() {});
			Set<String> weekSet = new HashSet<String>();
			for(DateInfo item : dateInfoList){
				weekSet.add(String.valueOf(item.getWeek()));
			}
			int[] weekArray = new int[weekSet.size()];
			int i = 0;
			for(String week : weekSet){
				weekArray[i] = Integer.parseInt(week);
				i++;
			}
			Arrays.sort(weekArray);			
			map.put("acadyear", acadyear);
			map.put("semester", semester);
			map.put("weekArray", weekArray);
			
		}else{
			return errorFtl(map,"当前时间不在学年学期内，无法维护！");
		}
		//List<Clazz> clsList = SUtils.dt(classRemoteService.findByIdCurAcadyear(getLoginInfo().getUnitId(), sem.getAcadyear()), new TR<List<Clazz>>() {});
		//map.put("clsList", clsList);
		//班级权限
		Set<String> classPermission = dyPermissionService.findClassSetByUserId(getLoginInfo().getUserId());
		List<Clazz> clazzs = SUtils.dt(classRemoteService.findListByIds(classPermission.toArray(new String[0])), new TR<List<Clazz>>() {});
		Set<String> gradeIds = EntityUtils.getSet(clazzs, "gradeId");
		List<Grade> gradeList = Lists.newArrayList();
		if(gradeIds.size()>0){
			gradeList = SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[0])), new TR<List<Grade>>() {});
			Collections.sort(gradeList, new Comparator<Grade>() {
				@Override
				public int compare(Grade o1, Grade o2) {
					return o1.getGradeCode().compareTo(o2.getGradeCode());
				}
			});
		}
		map.put("gradeList", gradeList);
		map.put("acadyearList", acadyearList);
    	return "/stuwork/studentManage/weekCheckPerformanceHead.ftl";
    }
    
    @ResponseBody
	@RequestMapping("/weekCheckPerformance/clsList")
	public List<Clazz> clsList(String gradeId){
		List<Clazz> clazzs = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), new TR<List<Clazz>>() {});
		//班级权限
		Set<String> classPermission = dyPermissionService.findClassSetByUserId(getLoginInfo().getUserId());
		Iterator<Clazz> it = clazzs.iterator();	
		while(it.hasNext()) {
			Clazz clazz = it.next();
			if (!classPermission.contains(clazz.getId())) {
				it.remove();
			}
		}
		Collections.sort(clazzs, new Comparator<Clazz>() {
			@Override
			public int compare(Clazz o1, Clazz o2) {
				return o1.getClassCode().compareTo(o2.getClassCode());
			}
		});
		return clazzs;
	}
    
    @RequestMapping("/weekCheckPerformance/weekCheckPerformanceList")
    public String pageList(String acadyear, String semester, String gradeId, String enter, String classId, String week, ModelMap map, HttpServletRequest request){
    	Set<String> stuIdSet = new HashSet<String>();
		List<Student> stuList = new ArrayList<>();
		Pagination page = createPagination();
		Map<String, String> stuInnerCodeMap = new HashMap<String, String>();
		Map<String, String> stuClsCodeMap = new HashMap<String, String>();
		if(StringUtils.isNotBlank(classId)){
			Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
			stuList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {});
			for(Student stu : stuList){
				stuIdSet.add(stu.getId());
				stu.setClassName(cls.getClassNameDynamic());
				stuInnerCodeMap.put(stu.getId(), stu.getClassInnerCode());
				stuClsCodeMap.put(stu.getId(), cls.getClassCode());
			}		
		}else{
			if(StringUtils.isNotBlank(gradeId)){
				List<Clazz> clazzs = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), new TR<List<Clazz>>() {});
				//班级权限
				Set<String> classPermission = dyPermissionService.findClassSetByUserId(getLoginInfo().getUserId());
				Set<String> clsIdSet = new HashSet<String>();
				Map<String, String> claNameMap = new HashMap<String, String>();
				Map<String, String> claCodeMap = new HashMap<String, String>();
				Iterator<Clazz> it = clazzs.iterator();	
				while(it.hasNext()) {
					Clazz clazz = it.next();
					if (classPermission.contains(clazz.getId())) {
						clsIdSet.add(clazz.getId());
						claNameMap.put(clazz.getId(), clazz.getClassNameDynamic());
						claCodeMap.put(clazz.getId(), clazz.getClassCode());
					}
				}
				stuList = SUtils.dt(studentRemoteService.findByClassIds(clsIdSet.toArray(new String[0])), new TR<List<Student>>() {});
				for(Student stu : stuList){
					stuIdSet.add(stu.getId());
					stu.setClassName(claNameMap.get(stu.getClassId()));
					stu.setBankCardNo(claCodeMap.get(stu.getClassId()));
					stuInnerCodeMap.put(stu.getId(), stu.getClassInnerCode());
					stuClsCodeMap.put(stu.getId(), claCodeMap.get(stu.getClassId()));
				}
			}
		}
		if(stuIdSet.size()==0){
			return "/stuwork/studentManage/weekCheckPerformanceList.ftl";
		}
    	
		List<DyStudentDto> dtoList = new ArrayList<DyStudentDto>();
		List<DyBusinessOption> dyBusinessOptionList = dyBusinessOptionService.findListByUnitIdAndType(getLoginInfo().getUnitId(), StuworkConstants.BUSINESS_TYPE_4);
    	if(StringUtils.isNotBlank(week)){
    		List<DyStuWeekCheckPerformance> dyStuWeekCheckPerformanceList = dyStuWeekCheckPerformanceService.findByStudentIds(getLoginInfo().getUnitId(), acadyear, semester, week, stuIdSet.toArray(new String[0]));
    		Map<String, DyStuWeekCheckPerformance> weekCheckPerformanceMap = EntityUtils.getMap(dyStuWeekCheckPerformanceList, DyStuWeekCheckPerformance::getStudentId);
    		DyStudentDto dto = null;
    		for(Student stu : stuList){
    			dto = new DyStudentDto();
    			dto.setId(stu.getId());
    			dto.setSex(stu.getSex());
    			dto.setStudentCode(stu.getStudentCode());
    			dto.setStudentName(stu.getStudentName());
    			dto.setClassName(stu.getClassName());
    			DyStuWeekCheckPerformance dyStuWeekCheckPerformance = weekCheckPerformanceMap.get(stu.getId());
    			if(dyStuWeekCheckPerformance!=null){
    				dto.setRemark(dyStuWeekCheckPerformance.getRemark());
    				dto.setGradeId(dyStuWeekCheckPerformance.getGradeId());
    				dto.setAcadyear(dyStuWeekCheckPerformance.getAcadyear());
    				dto.setSemester(dyStuWeekCheckPerformance.getSemester());
    			}
    			if(null!=stuInnerCodeMap.get(stu.getId())){    						
					dto.setClassInnerCode(stuInnerCodeMap.get(stu.getId()));
				}
				if(null!=stuClsCodeMap.get(stu.getId())){    						
					dto.setClassCode(stuClsCodeMap.get(stu.getId()));
				}
    			dto.setWeek(Integer.parseInt(week));
    			if(StringUtils.isNotBlank(enter)){
    				if("1".equals(enter)){
    					if(dyStuWeekCheckPerformance!=null && StringUtils.isNotBlank(dyStuWeekCheckPerformance.getGradeId())){
    						dtoList.add(dto);
    					}
    				}else{
    					if(dyStuWeekCheckPerformance==null || (dyStuWeekCheckPerformance!=null && StringUtils.isBlank(dyStuWeekCheckPerformance.getGradeId()))){
    						dtoList.add(dto);
    					}
    				}
    			}else{			
    				dtoList.add(dto);
    			}
    		}
    	}else{
    		List<DyStuWeekCheckPerformance> dyStuWeekCheckPerformanceList = dyStuWeekCheckPerformanceService.findByStudentIds(getLoginInfo().getUnitId(), acadyear, semester, week, stuIdSet.toArray(new String[0]));
    		//Map<String, DyStuWeekCheckPerformance> weekCheckPerformanceMap = EntityUtils.getMap(dyStuWeekCheckPerformanceList, DyStuWeekCheckPerformance::getStudentId);
    		Map<String, DyStuWeekCheckPerformance> weekCheckPerformanceMap = new HashMap<String, DyStuWeekCheckPerformance>();
    		for(DyStuWeekCheckPerformance checkPerformance : dyStuWeekCheckPerformanceList){
    			weekCheckPerformanceMap.put(checkPerformance.getStudentId()+checkPerformance.getWeek(), checkPerformance);
    		}
    		
    		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
    		if(null!=sem){
    			String acadyear1 = sem.getAcadyear();
    			int semester1 = sem.getSemester();
    			DateInfo dateInfo =  SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), acadyear1, semester1, new Date()), DateInfo.class);
    			if(dateInfo == null){
    				return errorFtl(map, "所选日期不在当前学期的节假日设置范围内！");
    			}
    			List<DateInfo> dateInfoList = SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester(getLoginInfo().getUnitId(), acadyear1, semester1), new TR<List<DateInfo>>() {});
    			Set<String> weekSet = new HashSet<String>();
    			for(DateInfo item : dateInfoList){
    				weekSet.add(String.valueOf(item.getWeek()));
    			}
    			int[] weekArray = new int[weekSet.size()];
    			int i = 0;
    			for(String week1 : weekSet){
    				weekArray[i] = Integer.parseInt(week1);
    				i++;
    			}
    			Arrays.sort(weekArray);			 			
    			DyStudentDto dto = null;
    			for(int w=0;w<weekArray.length;w++){			
    				for(Student stu : stuList){
    					dto = new DyStudentDto();
    					dto.setId(stu.getId());
    					dto.setSex(stu.getSex());
    					dto.setStudentCode(stu.getStudentCode());
    					dto.setStudentName(stu.getStudentName());
    					dto.setClassName(stu.getClassName());
    					if(null!=stuInnerCodeMap.get(stu.getId())){    						
    						dto.setClassInnerCode(stuInnerCodeMap.get(stu.getId()));
    					}
    					if(null!=stuClsCodeMap.get(stu.getId())){    						
    						dto.setClassCode(stuClsCodeMap.get(stu.getId()));
    					}
    					DyStuWeekCheckPerformance dyStuWeekCheckPerformance = weekCheckPerformanceMap.get(stu.getId()+weekArray[w]);
    					if(dyStuWeekCheckPerformance!=null){
    						dto.setRemark(dyStuWeekCheckPerformance.getRemark());
    						dto.setGradeId(dyStuWeekCheckPerformance.getGradeId());
    						dto.setAcadyear(dyStuWeekCheckPerformance.getAcadyear());
    						dto.setSemester(dyStuWeekCheckPerformance.getSemester());
    					}
    					dto.setWeek(weekArray[w]);
    					if(StringUtils.isNotBlank(enter)){
    						if("1".equals(enter)){
    							if(dyStuWeekCheckPerformance!=null && StringUtils.isNotBlank(dyStuWeekCheckPerformance.getGradeId())){
    								dtoList.add(dto);
    							}
    						}else{
    							if(dyStuWeekCheckPerformance==null || StringUtils.isBlank(dyStuWeekCheckPerformance.getGradeId())){
    								dtoList.add(dto);
    							}
    						}
    					}else{			
    						dtoList.add(dto);
    					}
    				}
    			}
    		}	
    	}
    	Collections.sort(dtoList, new Comparator<DyStudentDto>() {
    		@Override
    		public int compare(DyStudentDto o1, DyStudentDto o2) {
    			if(o1.getWeek() < o2.getWeek()){
    				return -1;
    			}
    			if(o1.getWeek() > o2.getWeek()){
    				return 1;
    			}
    			if(!o1.getClassCode().equals(o2.getClassCode())){
    				return o1.getClassCode().compareTo(o2.getClassCode());
    			}
    			if(StringUtils.isEmpty(o1.getClassInnerCode()) && StringUtils.isEmpty(o2.getClassInnerCode())){
    				return 0;
    			}
    			if(StringUtils.isEmpty(o1.getClassInnerCode())){
    				return 1;
    			}
    			if(StringUtils.isEmpty(o2.getClassInnerCode())){
    				return -1;
    			}
   				return o1.getClassInnerCode().compareTo(o2.getClassInnerCode());
    		}
    	});
    	List<DyStudentDto> dtoNewList = Lists.newArrayList();
    	page.setMaxRowCount(dtoList.size());
    	Integer pageSize = page.getPageSize();
    	Integer pageIndex = page.getPageIndex();
    	for(int p=pageSize*(pageIndex-1);p<dtoList.size();p++){
    		if(p<pageSize*pageIndex&&p>=pageSize*(pageIndex-1)){
    			dtoNewList.add(dtoList.get(p));
    		} else {
    			break;
    		}
    	}
    	map.put("dyBusinessOptionList", dyBusinessOptionList);
    	map.put("studentList", dtoNewList);
    	map.put("week", week);
    	map.put("Pagination", page);
    	sendPagination(request, map, page);   		
    	return "/stuwork/studentManage/weekCheckPerformanceList.ftl";
    }
    
    @ResponseBody
	@RequestMapping("/weekCheckPerformance/searchWeek")
	public int[] clsList(String acadyear,int semester){
		List<DateInfo> dateInfoList = SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester(getLoginInfo().getUnitId(), acadyear, semester), new TR<List<DateInfo>>() {});
		Set<String> weekSet = new HashSet<String>();
		for(DateInfo item : dateInfoList){
			weekSet.add(String.valueOf(item.getWeek()));
		}
		int[] weekArray = new int[weekSet.size()];
		int i = 0;
		for(String week : weekSet){
			weekArray[i] = Integer.parseInt(week);
			i++;
		}
		Arrays.sort(weekArray);	
		return weekArray;
	}
    
    @ResponseBody
	@RequestMapping("/weekCheckPerformance/save")
	public String save(String acadyear, String semester, String week, DyStuWeekCheckPerformanceDto dyStuWeekCheckPerformanceDto, String classId, ModelMap map){
		try{
			List<DyStuWeekCheckPerformance> weekCheckPerformanceList = dyStuWeekCheckPerformanceDto.getDyStuWeekCheckPerformanceList();		
			dyStuWeekCheckPerformanceService.saveList(getLoginInfo().getUnitId(), acadyear, semester, week, weekCheckPerformanceList);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();		
	}
    
    @RequestMapping("/weekCheckPerformance/export")
	public void weekCheckPerformanceExport(String gradeId, String classId,String enter, String acadyear, String semester, String week, HttpServletResponse response){
    	List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
    	Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList();
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
    	Set<String> stuIdSet = new HashSet<String>();
		List<Student> stuList = new ArrayList<>();
		Pagination page = createPagination();
		Map<String, String> stuInnerCodeMap = new HashMap<String, String>();
		Map<String, String> stuClsCodeMap = new HashMap<String, String>();
		if(StringUtils.isNotBlank(classId)){
			Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
			stuList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {});
			for(Student stu : stuList){
				stuIdSet.add(stu.getId());
				stu.setClassName(cls.getClassNameDynamic());
				stuInnerCodeMap.put(stu.getId(), stu.getClassInnerCode());
				stuClsCodeMap.put(stu.getId(), cls.getClassCode());
			}		
		}else{
			if(StringUtils.isNotBlank(gradeId)){
				List<Clazz> clazzs = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), new TR<List<Clazz>>() {});
				//班级权限
				Set<String> classPermission = dyPermissionService.findClassSetByUserId(getLoginInfo().getUserId());
				Set<String> clsIdSet = new HashSet<String>();
				Map<String, String> claNameMap = new HashMap<String, String>();
				Map<String, String> claCodeMap = new HashMap<String, String>();
				Iterator<Clazz> it = clazzs.iterator();	
				while(it.hasNext()) {
					Clazz clazz = it.next();
					if (classPermission.contains(clazz.getId())) {
						clsIdSet.add(clazz.getId());
						claNameMap.put(clazz.getId(), clazz.getClassNameDynamic());
						claCodeMap.put(clazz.getId(), clazz.getClassCode());
					}
				}
				stuList = SUtils.dt(studentRemoteService.findByClassIds(clsIdSet.toArray(new String[0])), new TR<List<Student>>() {});
				for(Student stu : stuList){
					stuIdSet.add(stu.getId());
					stu.setClassName(claNameMap.get(stu.getClassId()));
					stu.setBankCardNo(claCodeMap.get(stu.getClassId()));
					stuInnerCodeMap.put(stu.getId(), stu.getClassInnerCode());
					stuClsCodeMap.put(stu.getId(), claCodeMap.get(stu.getClassId()));
				}
			}
		}
		if(stuIdSet.size()==0){
			ex.exportXLSFile("值周表现信息", titleMap, sheetName2RecordListMap, response);	
		}
    	
		List<DyStudentDto> dtoList = new ArrayList<DyStudentDto>();
		List<DyBusinessOption> dyBusinessOptionList = dyBusinessOptionService.findListByUnitIdAndType(getLoginInfo().getUnitId(), StuworkConstants.BUSINESS_TYPE_4);
		Map<String, String> optionMap = new HashMap<String, String>();
		for(DyBusinessOption item : dyBusinessOptionList){
			optionMap.put(item.getId(), item.getOptionName());
		}
		if(StringUtils.isNotBlank(week)){
    		List<DyStuWeekCheckPerformance> dyStuWeekCheckPerformanceList = dyStuWeekCheckPerformanceService.findByStudentIds(getLoginInfo().getUnitId(), acadyear, semester, week, stuIdSet.toArray(new String[0]));
    		Map<String, DyStuWeekCheckPerformance> weekCheckPerformanceMap = EntityUtils.getMap(dyStuWeekCheckPerformanceList, DyStuWeekCheckPerformance::getStudentId);
    		DyStudentDto dto = null;
    		for(Student stu : stuList){
    			dto = new DyStudentDto();
    			dto.setId(stu.getId());
    			dto.setSex(stu.getSex());
    			dto.setStudentCode(stu.getStudentCode());
    			dto.setStudentName(stu.getStudentName());
    			dto.setClassName(stu.getClassName());
    			DyStuWeekCheckPerformance dyStuWeekCheckPerformance = weekCheckPerformanceMap.get(stu.getId());
    			if(dyStuWeekCheckPerformance!=null){
    				dto.setRemark(dyStuWeekCheckPerformance.getRemark());
    				dto.setGradeId(dyStuWeekCheckPerformance.getGradeId());
    				dto.setAcadyear(dyStuWeekCheckPerformance.getAcadyear());
    				dto.setSemester(dyStuWeekCheckPerformance.getSemester());
    			}
    			if(null!=stuInnerCodeMap.get(stu.getId())){    						
					dto.setClassInnerCode(stuInnerCodeMap.get(stu.getId()));
				}
				if(null!=stuClsCodeMap.get(stu.getId())){    						
					dto.setClassCode(stuClsCodeMap.get(stu.getId()));
				}
    			dto.setWeek(Integer.parseInt(week));
    			if(StringUtils.isNotBlank(enter)){
    				if("1".equals(enter)){
    					if(dyStuWeekCheckPerformance!=null && StringUtils.isNotBlank(dyStuWeekCheckPerformance.getGradeId())){
    						dtoList.add(dto);
    					}
    				}else{
    					if(dyStuWeekCheckPerformance==null || (dyStuWeekCheckPerformance!=null && StringUtils.isBlank(dyStuWeekCheckPerformance.getGradeId()))){
    						dtoList.add(dto);
    					}
    				}
    			}else{			
    				dtoList.add(dto);
    			}
    		}
    	}else{
    		List<DyStuWeekCheckPerformance> dyStuWeekCheckPerformanceList = dyStuWeekCheckPerformanceService.findByStudentIds(getLoginInfo().getUnitId(), acadyear, semester, week, stuIdSet.toArray(new String[0]));
    		//Map<String, DyStuWeekCheckPerformance> weekCheckPerformanceMap = EntityUtils.getMap(dyStuWeekCheckPerformanceList, DyStuWeekCheckPerformance::getStudentId);
    		Map<String, DyStuWeekCheckPerformance> weekCheckPerformanceMap = new HashMap<String, DyStuWeekCheckPerformance>();
    		for(DyStuWeekCheckPerformance checkPerformance : dyStuWeekCheckPerformanceList){
    			weekCheckPerformanceMap.put(checkPerformance.getStudentId()+checkPerformance.getWeek(), checkPerformance);
    		}
    		
    		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
    		if(null!=sem){
    			String acadyear1 = sem.getAcadyear();
    			int semester1 = sem.getSemester();
    			DateInfo dateInfo =  SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), acadyear1, semester1, new Date()), DateInfo.class);
    			if(dateInfo == null){
    				ex.exportXLSFile("值周表现信息", titleMap, sheetName2RecordListMap, response);	
    			}
    			List<DateInfo> dateInfoList = SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester(getLoginInfo().getUnitId(), acadyear1, semester1), new TR<List<DateInfo>>() {});
    			Set<String> weekSet = new HashSet<String>();
    			for(DateInfo item : dateInfoList){
    				weekSet.add(String.valueOf(item.getWeek()));
    			}
    			int[] weekArray = new int[weekSet.size()];
    			int i = 0;
    			for(String week1 : weekSet){
    				weekArray[i] = Integer.parseInt(week1);
    				i++;
    			}
    			Arrays.sort(weekArray);			 			
    			DyStudentDto dto = null;
    			for(int w=0;w<weekArray.length;w++){			
    				for(Student stu : stuList){
    					dto = new DyStudentDto();
    					dto.setId(stu.getId());
    					dto.setSex(stu.getSex());
    					dto.setStudentCode(stu.getStudentCode());
    					dto.setStudentName(stu.getStudentName());
    					dto.setClassName(stu.getClassName());
    					if(null!=stuInnerCodeMap.get(stu.getId())){    						
    						dto.setClassInnerCode(stuInnerCodeMap.get(stu.getId()));
    					}
    					if(null!=stuClsCodeMap.get(stu.getId())){    						
    						dto.setClassCode(stuClsCodeMap.get(stu.getId()));
    					}
    					DyStuWeekCheckPerformance dyStuWeekCheckPerformance = weekCheckPerformanceMap.get(stu.getId()+weekArray[w]);
    					if(dyStuWeekCheckPerformance!=null){
    						dto.setRemark(dyStuWeekCheckPerformance.getRemark());
    						dto.setGradeId(dyStuWeekCheckPerformance.getGradeId());
    						dto.setAcadyear(dyStuWeekCheckPerformance.getAcadyear());
    						dto.setSemester(dyStuWeekCheckPerformance.getSemester());
    					}
    					dto.setWeek(weekArray[w]);
    					if(StringUtils.isNotBlank(enter)){
    						if("1".equals(enter)){
    							if(dyStuWeekCheckPerformance!=null && StringUtils.isNotBlank(dyStuWeekCheckPerformance.getGradeId())){
    								dtoList.add(dto);
    							}
    						}else{
    							if(dyStuWeekCheckPerformance==null || StringUtils.isBlank(dyStuWeekCheckPerformance.getGradeId())){
    								dtoList.add(dto);
    							}
    						}
    					}else{			
    						dtoList.add(dto);
    					}
    				}
    			}
    		}	
    	}
    	Collections.sort(dtoList, new Comparator<DyStudentDto>() {
    		@Override
    		public int compare(DyStudentDto o1, DyStudentDto o2) {
    			if(o1.getWeek() < o2.getWeek()){
    				return -1;
    			}
    			if(o1.getWeek() > o2.getWeek()){
    				return 1;
    			}
    			if(!o1.getClassCode().equals(o2.getClassCode())){
    				return o1.getClassCode().compareTo(o2.getClassCode());
    			}
    			if(StringUtils.isEmpty(o1.getClassInnerCode()) && StringUtils.isEmpty(o2.getClassInnerCode())){
    				return 0;
    			}
    			if(StringUtils.isEmpty(o1.getClassInnerCode())){
    				return 1;
    			}
    			if(StringUtils.isEmpty(o2.getClassInnerCode())){
    				return -1;
    			}
   				return o1.getClassInnerCode().compareTo(o2.getClassInnerCode());
    		}
    	});
    	
    	for(DyStudentDto dto : dtoList){
    		Map<String,String> sMap = new HashMap<String,String>();
			sMap.put("姓名", dto.getStudentName());
			if(dto.getSex() == null) {
				sMap.put("性别", "");
			}else {
				sMap.put("性别", dto.getSex()==1?"男":"女");
			}
			sMap.put("学号", dto.getStudentCode());
			sMap.put("周次", String.valueOf(dto.getWeek()));
			sMap.put("班级", dto.getClassName());
			String grade = optionMap.get(dto.getGradeId());
			sMap.put("等第", grade);
			sMap.put("备注", dto.getRemark());		
			recordList.add(sMap);

    	}

    	 
		ex.exportXLSFile("值周表现信息", titleMap, sheetName2RecordListMap, response);	
    }
    
   /* @RequestMapping("/weekCheckPerformance/export")
	public void weekCheckPerformanceExport(String gradeId, String classId,String enter, String acadyear, String semester, String week, HttpServletResponse response){
    	Set<String> stuIdSet = new HashSet<String>();
		List<Student> stuList = new ArrayList<>();
		if(StringUtils.isNotBlank(classId)){
			Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
			stuList=SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}),new TR<List<Student>>(){});
			for(Student stu : stuList){
				stuIdSet.add(stu.getId());
				stu.setClassName(cls.getClassNameDynamic());
			}		
			Collections.sort(stuList, new Comparator<Student>() {
				@Override
				public int compare(Student o1, Student o2) {
					if(StringUtils.isBlank(o1.getClassInnerCode()) && StringUtils.isBlank(o2.getClassInnerCode())){
						return 0;
					}
					if(StringUtils.isBlank(o1.getClassInnerCode()) || StringUtils.isBlank(o2.getClassInnerCode())){
						return -1;
					}
					return o1.getClassInnerCode().compareTo(o2.getClassInnerCode());
				}
			});
		}else{
			if(StringUtils.isNotBlank(gradeId)){
				List<Clazz> clazzs = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), new TR<List<Clazz>>() {});
				//班级权限
				Set<String> classPermission = dyPermissionService.findClassSetByUserId(getLoginInfo().getUserId());
				Set<String> clsIdSet = new HashSet<String>();
				Map<String, String> claNameMap = new HashMap<String, String>();
				Map<String, String> claCodeMap = new HashMap<String, String>();
				Iterator<Clazz> it = clazzs.iterator();	
				while(it.hasNext()) {
					Clazz clazz = it.next();
					if (classPermission.contains(clazz.getId())) {
						clsIdSet.add(clazz.getId());
						claNameMap.put(clazz.getId(), clazz.getClassNameDynamic());
						claCodeMap.put(clazz.getId(), clazz.getClassCode());
					}
				}
				stuList=SUtils.dt(studentRemoteService.findByClassIds(clsIdSet.toArray(new String[0])),new TR<List<Student>>(){});
				for(Student stu : stuList){
					stuIdSet.add(stu.getId());
					stu.setClassName(claNameMap.get(stu.getClassId()));
					stu.setBankCardNo(claCodeMap.get(stu.getClassId()));
				}
				Collections.sort(stuList, new Comparator<Student>() {
					@Override
					public int compare(Student o1, Student o2) {
						if(!o1.getBankCardNo().equals(o2.getBankCardNo())){
							return o1.getBankCardNo().compareTo(o2.getBankCardNo());
						}
						if(StringUtils.isBlank(o1.getClassInnerCode()) && StringUtils.isBlank(o2.getClassInnerCode())){
							return 0;
						}
						if(StringUtils.isBlank(o1.getClassInnerCode()) || StringUtils.isBlank(o2.getClassInnerCode())){
							return -1;
						}
						if(!o1.getClassInnerCode().equals(o2.getClassInnerCode())){
							return o1.getClassInnerCode().compareTo(o2.getClassInnerCode());
						}
						return o1.getClassInnerCode().compareTo(o2.getClassInnerCode());
					}
				});
			}
		}
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
    	if(StringUtils.isNotBlank(week) && CollectionUtils.isNotEmpty(stuIdSet)){
    		List<DyBusinessOption> dyBusinessOptionList = dyBusinessOptionService.findListByUnitIdAndType(getLoginInfo().getUnitId(), StuworkConstants.BUSINESS_TYPE_4);
    		Map<String, String> optionMap = new HashMap<String, String>();
    		for(DyBusinessOption item : dyBusinessOptionList){
    			optionMap.put(item.getId(), item.getOptionName());
    		}
    		List<DyStuWeekCheckPerformance> dyStuWeekCheckPerformanceList = dyStuWeekCheckPerformanceService.findByStudentIds(getLoginInfo().getUnitId(), acadyear, semester, week, stuIdSet.toArray(new String[0]));
    		Map<String, DyStuWeekCheckPerformance> weekCheckPerformanceMap = EntityUtils.getMap(dyStuWeekCheckPerformanceList, DyStuWeekCheckPerformance::getStudentId);
			for(Student stu : stuList){
				DyStuWeekCheckPerformance dyStuWeekCheckPerformance = weekCheckPerformanceMap.get(stu.getId());
				Map<String,String> sMap = new HashMap<String,String>();
				sMap.put("姓名", stu.getStudentName());
				if(stu.getSex() == null) {
					sMap.put("性别", "");
				}else {
					sMap.put("性别", stu.getSex()==1?"男":"女");
				}
				sMap.put("学号", stu.getStudentCode());
				sMap.put("班级", stu.getClassName());
				if(dyStuWeekCheckPerformance!=null){
					String grade = optionMap.get(dyStuWeekCheckPerformance.getGradeId());
					sMap.put("等第", grade);
					sMap.put("备注", dyStuWeekCheckPerformance.getRemark());
				}else{
					sMap.put("等第", "");
					sMap.put("备注", "");
				}
				if(StringUtils.isNotBlank(enter)){
					if("1".equals(enter)){
						if(dyStuWeekCheckPerformance!=null && StringUtils.isNotBlank(dyStuWeekCheckPerformance.getGradeId())){
							recordList.add(sMap);
						}
					}else{
						if(dyStuWeekCheckPerformance==null || (dyStuWeekCheckPerformance!=null && StringUtils.isBlank(dyStuWeekCheckPerformance.getGradeId()))){
							recordList.add(sMap);
						}
					}
				}else{			
					recordList.add(sMap);
				}
			}
		}
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList();
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance(); 
		ex.exportXLSFile("值周表现信息", titleMap, sheetName2RecordListMap, response);	
	}*/
	
	public String getObjectName() {
		return "值周表现信息";
	}
	
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("姓名");
		tis.add("性别");
		tis.add("学号");
		tis.add("班级");
		tis.add("周次");
		tis.add("等第");
		tis.add("备注");
		return tis;
	}
}
