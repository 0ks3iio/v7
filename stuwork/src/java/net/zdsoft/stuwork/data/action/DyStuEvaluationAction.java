package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.ZipUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dto.DyBusinessOptionDto;
import net.zdsoft.stuwork.data.dto.DyStuEvaluationDto;
import net.zdsoft.stuwork.data.entity.DyBusinessOption;
import net.zdsoft.stuwork.data.entity.DyStuEvaluation;
import net.zdsoft.stuwork.data.service.DyBusinessOptionService;
import net.zdsoft.stuwork.data.service.DyPermissionService;
import net.zdsoft.stuwork.data.service.DyStuEvaluationService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import net.zdsoft.tutor.data.constant.TutorConstants;
import net.zdsoft.tutor.data.dto.TutorTeaStuDto;
import net.zdsoft.tutor.data.entity.TutorParam;

@Controller
@RequestMapping("/stuwork")
public class DyStuEvaluationAction extends BaseAction{
	@Autowired
	private DyBusinessOptionService dyBusinessOptionService;
	@Autowired
	private DyStuEvaluationService dyStuEvaluationService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
    private DyPermissionService dyPermissionService;
	@InitBinder
	public void initBinder(WebDataBinder wb) throws Exception {
		wb.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
	}
	//***********************以下评语录入***************分割线***********
	
	@RequestMapping("/evaluation/stu/index/page")
	@ControllerInfo(value = "录入index")
	public String showStuIndex(ModelMap map){
		return "/stuwork/stuEvaluation/evaluationStuIndex.ftl";
	}
	
	@RequestMapping("/evaluation/stu/list/page")
	@ControllerInfo(value = "录入列表")
	public String showStuList(ModelMap map,DyBusinessOptionDto dto){
		String unitId=getLoginInfo().getUnitId();
		
		List<Clazz> classList=SUtils.dt(classRemoteService.findBySchoolIdTeacherId(unitId, getLoginInfo().getOwnerId()),new TR<List<Clazz>>(){});
		if(CollectionUtils.isEmpty(classList)){
			return errorFtl(map, "当前用户不是班主任");
		}
		if(StringUtils.isBlank(dto.getAcadyear())){//此时说明 重新或第一次进入此列表
			Semester se=SUtils.dc(semesterRemoteService.getCurrentSemester(0,unitId), Semester.class);
			if(se==null){
				return errorFtl(map, "当前时间不在学年学期范围内");
			}
			dto.setAcadyear(se.getAcadyear());
			dto.setSemester(se.getSemester()+"");
			dto.setClassId(classList.get(0).getId());
		}
		List<Student> studentList=SUtils.dt(studentRemoteService.findByClassIds(new String[]{dto.getClassId()}),new TR<List<Student>>(){});
		if(CollectionUtils.isEmpty(studentList)){
			return errorFtl(map, "该班级没有学生数据");
		}
		if(StringUtils.isBlank(dto.getStudentId()) ){
			dto.setStudentId(studentList.get(0).getId());
		}
		
		classList=setClassName(classList);
		
		List<String> acadyearList=SUtils.dt(semesterRemoteService.findAcadeyearList(),new TR<List<String>>(){});
		List<DyBusinessOption> boptionList=dyBusinessOptionService.findListByUnitIdAndType(getLoginInfo().getUnitId(), StuworkConstants.BUSINESS_TYPE_2);
		Student stu=SUtils.dc(studentRemoteService.findOneById(dto.getStudentId()),Student.class);
		
		List<DyStuEvaluation> evaList=dyStuEvaluationService.findListByUidAndDto(unitId, dto);
		DyStuEvaluation eva=null;
		if(CollectionUtils.isNotEmpty(evaList)){
			for(DyStuEvaluation evaluation:evaList){
				if(evaluation.getStudentId().equals(dto.getStudentId())){
					eva=evaluation;
					break;
				}
			}
			int rowCount = (evaList.size()+1)/2;
			map.put("rowCount", rowCount);
			map.put("evaCount", evaList.size());
			//获取学校名
			Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
			String semesterName = SUtils.dc(mcodeRemoteService.findByMcodeAndThisId("DM-XQ", dto.getSemester()),McodeDetail.class).getMcodeContent();
			Clazz clazz=SUtils.dc(classRemoteService.findOneById(dto.getClassId()),Clazz.class);
			String className=clazz==null?"":clazz.getClassNameDynamic();
			map.put("unitName", unit.getUnitName());
			map.put("semesterName",semesterName);
			map.put("className", className);
		}
		if(eva==null) eva=new DyStuEvaluation();
		
		map.put("eva", eva);
		map.put("evaList",evaList);
		map.put("acadyearList", acadyearList);
		if(CollectionUtils.isNotEmpty(classList)) {
			Collections.sort(classList, new Comparator<Clazz>() {
				@Override
				public int compare(Clazz o1, Clazz o2) {
					return o1.getClassCode().compareTo(o2.getClassCode());
				}
			});
		}
		map.put("classList", classList);
		//map.put("studentList", studentList);
		map.put("boptionList", boptionList);
		map.put("studentName", stu==null?"":stu.getStudentName());
		map.put("dto",dto);
		
		return "/stuwork/stuEvaluation/evaluationStuList.ftl";
	}
	
	@RequestMapping("/evaluation/stu/myStat")
	@ControllerInfo(value = "我的班级汇总页")
	public String myClassStat(ModelMap map,String classId){
		
		List<Clazz> classList=SUtils.dt(classRemoteService.findBySchoolIdTeacherId(getLoginInfo().getUnitId(), getLoginInfo().getOwnerId()),new TR<List<Clazz>>(){});
		if(CollectionUtils.isEmpty(classList)){
			return errorFtl(map, "当前用户不是班主任");
		}
		classList=setClassName(classList);
		if(StringUtils.isBlank(classId)){
			classId=classList.get(0).getId();
		}
		Clazz clazz=SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
		if(clazz!=null){
			List<DyStuEvaluationDto> evaList=dyStuEvaluationService.findListByUidAndCid(getLoginInfo().getUnitId(), clazz);
			List<String> titleList=new ArrayList<String>();
			if(CollectionUtils.isNotEmpty(evaList)){
				List<DyBusinessOptionDto> dtoList=evaList.get(0).getDtoList();
				if(CollectionUtils.isNotEmpty(dtoList)){
					for(DyBusinessOptionDto dto:dtoList){
						String semester="";
						if(dto.getSemester().equals("1")){
							semester="第一学期";
						}else{
							semester="第二学期";
						}
						titleList.add(dto.getAcadyear()+"<br>"+semester);
					}
				}
			}
			
			map.put("titleList", titleList);
			map.put("evaList", evaList);
			map.put("className", clazz.getClassNameDynamic());
		}
		if(CollectionUtils.isNotEmpty(classList)) {
			Collections.sort(classList, new Comparator<Clazz>() {
				@Override
				public int compare(Clazz o1, Clazz o2) {
					return o1.getClassCode().compareTo(o2.getClassCode());
				}
			});
		}
		map.put("classList", classList);
		map.put("classId", classId);
		return "/stuwork/stuEvaluation/evaluationStuMyStat.ftl";
	}
	@ResponseBody
	@RequestMapping("/evaluation/stu/doSaveEva")
	@ControllerInfo(value = "录入评语")
	public String doSaveEva(ModelMap map,DyStuEvaluation eva){
		try {
			DyBusinessOption boption=dyBusinessOptionService.findOne(eva.getGradeId());
			eva.setGrade(boption.getOptionName());
			eva.setScore(boption.getScore());
			if(StringUtils.isNotBlank(eva.getId())){
				dyStuEvaluationService.update(eva, eva.getId(), new String[]{"gradeId","grade","remark","association","score"});
			}else{
				eva.setUnitId(getLoginInfo().getUnitId());
				eva.setId(UuidUtils.generateUuid());
				dyStuEvaluationService.save(eva);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	//**********************以下评语汇总************分割线****************//
	@RequestMapping("/evaluation/stat/index")
	@ControllerInfo(value = "评语汇总")
	public String statIndex(ModelMap map,DyStuEvaluation eva){
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

		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1, getLoginInfo().getUnitId()), Semester.class);
		map.put("nowAcadyear", semester.getAcadyear());
		map.put("nowSemester", semester.getSemester());
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), String.class);
		map.put("acadyearList", acadyearList);

		map.put("gradeList", gradeList);
		return "/stuwork/stuEvaluation/evaluationStuStatHead.ftl";
		//return "/stuwork/stuEvaluation/evaluationStuStatIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/evaluation/clsList")
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
	
	@RequestMapping("/evaluation/stat/list")
	@ControllerInfo(value = "评语汇总")
	public String statList(ModelMap map,String gradeId, String classId, String acadyear, String semester, String allcheck, HttpServletRequest request){
		Set<String> stuIdSet = new HashSet<String>();
		List<Student> stuList = new ArrayList<>();
		Pagination page = createPagination();
		if(StringUtils.isNotBlank(classId)){
			Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
			stuList = Student.dt(studentRemoteService.findByClassIds(new String[]{classId}, SUtils.s(page)), page);
			for(Student stu : stuList){
				stuIdSet.add(stu.getId());
				stu.setClassName(cls.getClassNameDynamic());
			}		
			Collections.sort(stuList, new Comparator<Student>() {
				@Override
				public int compare(Student o1, Student o2) {
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
				stuList = Student.dt(studentRemoteService.findByClassIds(clsIdSet.toArray(new String[0]), SUtils.s(page)), page);
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
			}
		}
		List<String[]> adaSemList = new ArrayList<String[]>();
		Map<String, String> resultMap = new HashMap<String, String>();
		//List<Student> stuList = SUtils.dt(studentRemoteService.findListByIds(stuIdSet.toArray(new String[0])), new TR<List<Student>>() {});
		dealdata(allcheck,resultMap,adaSemList,acadyear,semester,stuList,stuIdSet);
		map.put("resultMap", resultMap);
		map.put("adaSemList", adaSemList);
		map.put("stuList", stuList);
		map.put("Pagination", page);
        sendPagination(request, map, page);
		return "/stuwork/stuEvaluation/evaluationStuStatList2.ftl";
	}
	
	public void dealdata(String allcheck,Map<String, String> resultMap,List<String[]> adaSemList, String acadyear, String semester,List<Student> stuList,Set<String> stuIdSet){
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), String.class);
		List<DyBusinessOption> boptionList=dyBusinessOptionService.findListByUnitIdAndType(getLoginInfo().getUnitId(), StuworkConstants.BUSINESS_TYPE_2);
		Map<String, String> evaMap = new HashMap<String, String>();
		Map<String, String> optionMap = new HashMap<String, String>();
		for(DyBusinessOption option : boptionList){
			optionMap.put(option.getId(), option.getOptionName());
		}
		if("1".equals(allcheck)){
			List<DyStuEvaluation> stuEvaluationList=dyStuEvaluationService.findByStudentIdIn(stuIdSet.toArray(new String[0]));
			for(DyStuEvaluation eva : stuEvaluationList){
				evaMap.put(eva.getStudentId()+eva.getAcadyear()+eva.getSemester(), optionMap.get(eva.getGradeId()));
			}
			for(Student student : stuList){
				for(String aca: acadyearList){
					if(null!=evaMap.get(student.getId()+aca+"1")){
						resultMap.put(student.getId()+aca+"1", evaMap.get(student.getId()+aca+"1"));
					}else{
						resultMap.put(student.getId()+aca+"1", "");
					}
                    if(null!=evaMap.get(student.getId()+aca+"2")){
                    	resultMap.put(student.getId()+aca+"2", evaMap.get(student.getId()+aca+"2"));
					}else{
						resultMap.put(student.getId()+aca+"2", "");
					}
				}
			}
			for(String aca: acadyearList){
				String[] acdsemArr1 = new String[2];
				acdsemArr1[0] = aca;
				acdsemArr1[1] = "1";
				adaSemList.add(acdsemArr1);
				String[] acdsemArr2 = new String[2];
				acdsemArr2[0] = aca;
				acdsemArr2[1] = "2";
				adaSemList.add(acdsemArr2);
			}
		}else{
			if("3".equals(semester)){
				List<DyStuEvaluation> stuEvaluationList1=dyStuEvaluationService.findByStudentIdIn(stuIdSet.toArray(new String[0]), getLoginInfo().getUnitId(), acadyear, "1");
				List<DyStuEvaluation> stuEvaluationList2=dyStuEvaluationService.findByStudentIdIn(stuIdSet.toArray(new String[0]), getLoginInfo().getUnitId(), acadyear, "2");
				for(DyStuEvaluation eva : stuEvaluationList1){
					evaMap.put(eva.getStudentId()+eva.getAcadyear()+eva.getSemester(), optionMap.get(eva.getGradeId()));
				}
				for(DyStuEvaluation eva : stuEvaluationList2){
					evaMap.put(eva.getStudentId()+eva.getAcadyear()+eva.getSemester(), optionMap.get(eva.getGradeId()));
				}
				
				for(Student student : stuList){
					if(null!=evaMap.get(student.getId()+acadyear+"1")){
						resultMap.put(student.getId()+acadyear+"1", evaMap.get(student.getId()+acadyear+"1"));
					}else{
						resultMap.put(student.getId()+acadyear+"1", "");
					}
                    if(null!=evaMap.get(student.getId()+acadyear+"2")){
                    	resultMap.put(student.getId()+acadyear+"2", evaMap.get(student.getId()+acadyear+"2"));
					}else{
						resultMap.put(student.getId()+acadyear+"2", "");
					}
				}
				String[] acdsemArr1 = new String[2];
				acdsemArr1[0] = acadyear;
				acdsemArr1[1] = "1";
				adaSemList.add(acdsemArr1);
				String[] acdsemArr2 = new String[2];
				acdsemArr2[0] = acadyear;
				acdsemArr2[1] = "2";
				adaSemList.add(acdsemArr2);
			}else{
				List<DyStuEvaluation> stuEvaluationList=dyStuEvaluationService.findByStudentIdIn(stuIdSet.toArray(new String[0]), getLoginInfo().getUnitId(), acadyear, semester);
				for(DyStuEvaluation eva : stuEvaluationList){
					evaMap.put(eva.getStudentId()+eva.getAcadyear()+eva.getSemester(), optionMap.get(eva.getGradeId()));
				}
				for(Student student : stuList){
					if(null!=evaMap.get(student.getId()+acadyear+semester)){
						resultMap.put(student.getId()+acadyear+semester, evaMap.get(student.getId()+acadyear+semester));
					}else{
						resultMap.put(student.getId()+acadyear+semester, "");
					}
				}
				String[] acdsemArr = new String[2];
				acdsemArr[0] = acadyear;
				acdsemArr[1] = semester;
				adaSemList.add(acdsemArr);
			}
		}
	}
	
	@RequestMapping("/evaluation/stat/doExport")
	@ControllerInfo(value = "评语汇总")
	public void doExport(String gradeId, String classId, String acadyear, String semester, String allcheck,HttpServletResponse response){
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
			}
		}
		List<String[]> adaSemList = new ArrayList<String[]>();
		Map<String, String> resultMap = new HashMap<String, String>();
		dealdata(allcheck,resultMap,adaSemList,acadyear,semester,stuList,stuIdSet);
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		for(Student stu : stuList){
			Map<String,String> sMap = new HashMap<String,String>();
			sMap.put("学生姓名", stu.getStudentName());
			sMap.put("班级", stu.getClassName());
			for(String[] arr : adaSemList){
				sMap.put(arr[0]+"第"+arr[1]+"学期",resultMap.get(stu.getId()+arr[0]+arr[1]));
			}
			recordList.add(sMap);
		}
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList(adaSemList);
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		//ex.exportXLSFile("评语汇总信息", titleMap, sheetName2RecordListMap, response);	
		
		
		HSSFWorkbook workbook = new HSSFWorkbook();								
		HSSFFont headfont = workbook.createFont();
		headfont.setFontHeightInPoints((short) 10);// 字体大小
		headfont.setBold(true);// 加粗
		HSSFSheet sheet = workbook.createSheet("评语汇总");
        sheet.setColumnWidth(0, 2000);
        sheet.setColumnWidth(1, 2800);
        sheet.setColumnWidth(2, 2800);
        sheet.setColumnWidth(3, 2800);
        sheet.setColumnWidth(4, 2800);
        sheet.setColumnWidth(5, 2800);
        sheet.setColumnWidth(6, 2800);
        sheet.setColumnWidth(7, 2800);
        
        HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(headfont);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		
		HSSFCellStyle style3 = workbook.createCellStyle();
		HSSFFont headfont3 = workbook.createFont();
		//headfont3.setFontHeightInPoints((short) 15);// 字体大小
		style3.setFont(headfont3);
		style3.setBorderBottom(BorderStyle.THIN);
		style3.setBorderLeft(BorderStyle.THIN);
		style3.setBorderRight(BorderStyle.THIN);
		style3.setBorderTop(BorderStyle.THIN);
		style3.setAlignment(HorizontalAlignment.CENTER);
		style3.setVerticalAlignment(VerticalAlignment.CENTER);
		
		HSSFRow row1 = sheet.createRow(0);
		CellRangeAddress car1 = new CellRangeAddress(0,0,0,1);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car1, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car1, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car1, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car1, sheet); 
	    sheet.addMergedRegion(car1);
	    HSSFCell cell1 = row1.createCell(0);
		cell1.setCellStyle(style);
		cell1.setCellValue(new HSSFRichTextString("学生姓名"));
		
		CellRangeAddress car2 = new CellRangeAddress(0,0,2,3);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car2, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car2, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car2, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car2, sheet); 
	    sheet.addMergedRegion(car2);
	    HSSFCell cell2 = row1.createCell(2);
		cell2.setCellStyle(style);
		cell2.setCellValue(new HSSFRichTextString("班级"));
		int i=4;
		for(String[] arr : adaSemList){			
			CellRangeAddress car3 = new CellRangeAddress(0,0,i,i+1);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car3, sheet); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car3, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car3, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car3, sheet); 
		    sheet.addMergedRegion(car3);
		    HSSFCell cell3 = row1.createCell(i);
			cell3.setCellStyle(style);
			cell3.setCellValue(new HSSFRichTextString(arr[0]+"第"+arr[1]+"学期"));
			i=i+2;
		}
		
		int a=1;
		for(Student stu : stuList){			
			HSSFRow row3 = sheet.createRow(a);
			CellRangeAddress car4 = new CellRangeAddress(a,a,0,1);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car4, sheet); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car4, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car4, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car4, sheet); 
		    sheet.addMergedRegion(car4);
		    HSSFCell cell4 = row3.createCell(0);
			cell4.setCellStyle(style3);
			cell4.setCellValue(stu.getStudentName());
			
			CellRangeAddress car5 = new CellRangeAddress(a,a,2,3);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car5, sheet); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car5, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car5, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car5, sheet); 
		    sheet.addMergedRegion(car5);
		    HSSFCell cell5 = row3.createCell(2);
			cell5.setCellStyle(style3);
			cell5.setCellValue(stu.getClassName());
			int t = 4;
			for(String[] arr : adaSemList){
				CellRangeAddress car6 = new CellRangeAddress(a,a,t,t+1);
			    RegionUtil.setBorderBottom(BorderStyle.THIN, car6, sheet); 
			    RegionUtil.setBorderLeft(BorderStyle.THIN, car6, sheet); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car6, sheet); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car6, sheet); 
			    sheet.addMergedRegion(car6);
			    HSSFCell cell6 = row3.createCell(t);
				cell6.setCellStyle(style3);
				cell6.setCellValue(resultMap.get(stu.getId()+arr[0]+arr[1]));
				t=t+2;
			}
			a=a+1;
		}
		
		ExportUtils.outputData(workbook, "评语汇总", response);
	}
	
	private String getObjectName() {
		return "评语汇总";
	}
	
	private List<String> getRowTitleList(List<String[]> adaSemList) {
		List<String> tis = new ArrayList<String>();
		tis.add("学生姓名");
		tis.add("班级");
		for(String[] arr : adaSemList){
			tis.add(arr[0]+"第"+arr[1]+"学期");
		}
		return tis;
	}
	
	/*@RequestMapping("/evaluation/stat/list")
	@ControllerInfo(value = "评语汇总")
	public String statList(ModelMap map,String  classId){
		Clazz clazz=SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
		if(clazz!=null){
			List<DyStuEvaluationDto> evaList=dyStuEvaluationService.findListByUidAndCid(getLoginInfo().getUnitId(), clazz);
			List<String> titleList=new ArrayList<String>();
			if(CollectionUtils.isNotEmpty(evaList)){
				List<DyBusinessOptionDto> dtoList=evaList.get(0).getDtoList();
				if(CollectionUtils.isNotEmpty(dtoList)){
					for(DyBusinessOptionDto dto:dtoList){
						String semester="";
						if(dto.getSemester().equals("1")){
							semester="第一学期";
						}else{
							semester="第二学期";
						}
						titleList.add(dto.getAcadyear()+"<br>"+semester);
					}
				}
			}
			
			map.put("titleList", titleList);
			map.put("evaList", evaList);
			map.put("className",clazz.getClassNameDynamic());
		}
		return "/stuwork/stuEvaluation/evaluationStuStatList.ftl";
	}*/
	
	public List<Clazz> setClassName(List<Clazz> classList){
		if(CollectionUtils.isNotEmpty(classList)){
			Set<String> gradeIds=new HashSet<String>();
			for(Clazz clazz:classList){
				gradeIds.add(clazz.getGradeId());
			}
			Map<String,String> gradeNameMap=EntityUtils.getMap(SUtils.dt((gradeRemoteService.findListByIds(gradeIds.toArray(new String[0])))
					,new TR<List<Grade>>(){}),"id","gradeName");
			for(Clazz clazz:classList){
				clazz.setClassNameDynamic(gradeNameMap.get(clazz.getGradeId())+clazz.getClassName());
			}
		}
		return classList;
	}
}
