package net.zdsoft.stuwork.data.action;

import com.google.common.collect.Lists;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dto.DyStuMilitaryTrainingDto;
import net.zdsoft.stuwork.data.dto.DyStudentDto;
import net.zdsoft.stuwork.data.entity.DyBusinessOption;
import net.zdsoft.stuwork.data.entity.DyStuMilitaryTraining;
import net.zdsoft.stuwork.data.service.DyBusinessOptionService;
import net.zdsoft.stuwork.data.service.DyPermissionService;
import net.zdsoft.stuwork.data.service.DyStuMilitaryTrainingService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/stuwork") 
public class DyStuMilitaryTrainingAction extends BaseAction{
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private DyBusinessOptionService dyBusinessOptionService;
	@Autowired
	private DyStuMilitaryTrainingService dyStuMilitaryTrainingService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DyPermissionService dyPermissionService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;

	@RequestMapping("/militaryTraining/pageIndex")
	public String pageIndex(ModelMap map){
		//map.put("unitClass", getLoginInfo().getUnitClass());
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
		return "/stuwork/studentManage/stuMilitaryTrainingHead2.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/militaryTraining/clsList")
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
	
	@RequestMapping("/militaryTraining/pageList")
	public String pageList(String gradeId, String classId,String enter, ModelMap map, HttpServletRequest request){
		Set<String> stuIdSet = new HashSet<String>();
		List<Student> stuList = new ArrayList<>();
		Pagination page = createPagination();
		if(StringUtils.isNotBlank(classId)){
			Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
			stuList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {});
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
				stuList = SUtils.dt(studentRemoteService.findByClassIds(clsIdSet.toArray(new String[0])), new TR<List<Student>>() {});
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
		if(stuIdSet.size()==0){
			return "/stuwork/studentManage/stuMilitaryTrainingList.ftl";
		}
		List<DyBusinessOption> dyBusinessOptionList = dyBusinessOptionService.findListByUnitIdAndType(getLoginInfo().getUnitId(), StuworkConstants.BUSINESS_TYPE_3);
		List<DyStuMilitaryTraining> dyStuMilitaryTrainingList = dyStuMilitaryTrainingService.findByStudentIds(stuIdSet.toArray(new String[0]));		
		Map<String, DyStuMilitaryTraining> militaryTrainingMap = EntityUtils.getMap(dyStuMilitaryTrainingList, DyStuMilitaryTraining::getStudentId);
		List<DyStudentDto> dtoList = new ArrayList<DyStudentDto>();
		DyStudentDto dto = null;
		for(Student stu : stuList){
			dto = new DyStudentDto();
			dto.setId(stu.getId());
			dto.setSex(stu.getSex());
			dto.setStudentCode(stu.getStudentCode());
			dto.setStudentName(stu.getStudentName());
			dto.setClassName(stu.getClassName());
			dto.setClassInnerCode(stu.getClassInnerCode());
			DyStuMilitaryTraining dyStuMilitaryTraining = militaryTrainingMap.get(stu.getId());
			if(dyStuMilitaryTraining!=null){
				dto.setRemark(dyStuMilitaryTraining.getRemark());
				dto.setGradeId(dyStuMilitaryTraining.getGradeId());
				dto.setAcadyear(dyStuMilitaryTraining.getAcadyear());
				dto.setSemester(dyStuMilitaryTraining.getSemester());
			}
			if(StringUtils.isNotBlank(enter)){
				if("1".equals(enter)){
					if(dyStuMilitaryTraining!=null && StringUtils.isNotBlank(dyStuMilitaryTraining.getGradeId())){
						dtoList.add(dto);
					}
				}else{
					if(dyStuMilitaryTraining==null || (dyStuMilitaryTraining!=null && StringUtils.isBlank(dyStuMilitaryTraining.getGradeId()))){
						dtoList.add(dto);
					}
				}
			}else{			
				dtoList.add(dto);
			}
		}
		List<DyStudentDto> dtoNewList = Lists.newArrayList();
		page.setMaxRowCount(dtoList.size());
		Integer pageSize = page.getPageSize();
		Integer pageIndex = page.getPageIndex();
		for(int i=pageSize*(pageIndex-1);i<dtoList.size();i++){
			if(i<pageSize*pageIndex&&i>=pageSize*(pageIndex-1)){
				dtoNewList.add(dtoList.get(i));
			} else {
				break;
			}
		}
		map.put("dyBusinessOptionList", dyBusinessOptionList);
		map.put("studentList", dtoNewList);
		map.put("classId", classId);
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), String.class);
		map.put("acadyearList", acadyearList);
		map.put("Pagination", page);
        sendPagination(request, map, page);
		return "/stuwork/studentManage/stuMilitaryTrainingList.ftl";
	}
	
	/*@RequestMapping("/militaryTraining/pageList")
	public String pageList(String classId, ModelMap map){
		List<Student> studentList =  SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {});
		List<DyBusinessOption> dyBusinessOptionList = dyBusinessOptionService.findListByUnitIdAndType(getLoginInfo().getUnitId(), StuworkConstants.BUSINESS_TYPE_3);
		Set<String> stuIdSet = new HashSet<String>();
		for(Student stu : studentList){
			stuIdSet.add(stu.getId());
		}
		if(stuIdSet.size()==0){
			return "/stuwork/studentManage/stuMilitaryTrainingList.ftl";
		}
		List<DyStuMilitaryTraining> dyStuMilitaryTrainingList = dyStuMilitaryTrainingService.findByStudentIds(stuIdSet.toArray(new String[0]));
		Map<String, DyStuMilitaryTraining> militaryTrainingMap = EntityUtils.getMap(dyStuMilitaryTrainingList, DyStuMilitaryTraining::getStudentId);
		List<DyStudentDto> dtoList = new ArrayList<DyStudentDto>();
		DyStudentDto dto = null;
		for(Student stu : studentList){
			dto = new DyStudentDto();
			dto.setId(stu.getId());
			dto.setSex(stu.getSex());
			dto.setStudentCode(stu.getStudentCode());
			dto.setStudentName(stu.getStudentName());
			DyStuMilitaryTraining dyStuMilitaryTraining = militaryTrainingMap.get(stu.getId());
			if(dyStuMilitaryTraining!=null){
				dto.setRemark(dyStuMilitaryTraining.getRemark());
				dto.setGradeId(dyStuMilitaryTraining.getGradeId());
				dto.setAcadyear(dyStuMilitaryTraining.getAcadyear());
				dto.setSemester(dyStuMilitaryTraining.getSemester());
			}
			dtoList.add(dto);
		}
		map.put("dyBusinessOptionList", dyBusinessOptionList);
		map.put("studentList", dtoList);
		map.put("classId", classId);
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), String.class);
		map.put("acadyearList", acadyearList);
		return "/stuwork/studentManage/stuMilitaryTrainingList.ftl";
	}*/
	
	@ResponseBody
	@RequestMapping("/militaryTraining/save")
	public String save(DyStuMilitaryTrainingDto dyStuMilitaryTrainingDto, String classId, ModelMap map){
		try{
			List<DyStuMilitaryTraining> dyStuMilitaryTrainingList = dyStuMilitaryTrainingDto.getDyStuMilitaryTrainingList();			
			dyStuMilitaryTrainingService.saveList(dyStuMilitaryTrainingList, classId, getLoginInfo().getUnitId());
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();		
	}
	
	@RequestMapping("/militaryTraining/militaryTrainingExport")
	public void stuMilitaryTrainingExport(String gradeId, String classId,String enter, HttpServletResponse response){
		//List<Student> studentList =  SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {});
		List<Student> studentList = new ArrayList<>();	
		if(StringUtils.isNotBlank(classId)){
			Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
			studentList=SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}),new TR<List<Student>>(){});
			for(Student stu : studentList){
				//stuIdSet.add(stu.getId());
				stu.setClassName(cls.getClassNameDynamic());
			}		
			Collections.sort(studentList, new Comparator<Student>() {
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
				studentList=SUtils.dt(studentRemoteService.findByClassIds(clsIdSet.toArray(new String[0])),new TR<List<Student>>(){});
				for(Student stu : studentList){
					//stuIdSet.add(stu.getId());
					stu.setClassName(claNameMap.get(stu.getClassId()));
					stu.setBankCardNo(claCodeMap.get(stu.getClassId()));
				}
				Collections.sort(studentList, new Comparator<Student>() {
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
		List<DyBusinessOption> dyBusinessOptionList = dyBusinessOptionService.findListByUnitIdAndType(getLoginInfo().getUnitId(), StuworkConstants.BUSINESS_TYPE_3);
		Map<String, String> optionMap = new HashMap<String, String>();
		for(DyBusinessOption item : dyBusinessOptionList){
			optionMap.put(item.getId(), item.getOptionName());
		}
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		if(CollectionUtils.isNotEmpty(studentList)){
			Set<String> stuIdSet = new HashSet<String>();
			for(Student stu : studentList){
				stuIdSet.add(stu.getId());
			}
			List<DyStuMilitaryTraining> dyStuMilitaryTrainingList = dyStuMilitaryTrainingService.findByStudentIds(stuIdSet.toArray(new String[0]));
			Map<String, DyStuMilitaryTraining> militaryTrainingMap = EntityUtils.getMap(dyStuMilitaryTrainingList, DyStuMilitaryTraining::getStudentId);
			for(Student stu : studentList){
				DyStuMilitaryTraining dyStuStudyingFarming = militaryTrainingMap.get(stu.getId());
				Map<String,String> sMap = new HashMap<String,String>();
				sMap.put("姓名", stu.getStudentName());
				if(stu.getSex() == null) {
					sMap.put("性别", "");
				}else {
					sMap.put("性别", stu.getSex()==1?"男":"女");
				}
				sMap.put("学号", stu.getStudentCode());
				sMap.put("班级", stu.getClassName());
				if(dyStuStudyingFarming!=null){
					String grade = optionMap.get(dyStuStudyingFarming.getGradeId());
					sMap.put("等第", grade);
					sMap.put("学年", dyStuStudyingFarming.getAcadyear());
					if("1".equals(dyStuStudyingFarming.getSemester())){
						sMap.put("学期", "第一学期");
					}else if("2".equals(dyStuStudyingFarming.getSemester())){
						sMap.put("学期", "第二学期");
					}else{
						sMap.put("学期", "");
					}
					sMap.put("备注", dyStuStudyingFarming.getRemark());
				}else{
					sMap.put("等第", "");
					sMap.put("备注", "");
				}
				if(StringUtils.isNotBlank(enter)){
					if("1".equals(enter)){
						if(dyStuStudyingFarming!=null && StringUtils.isNotBlank(dyStuStudyingFarming.getGradeId())){
							recordList.add(sMap);
						}
					}else{
						if(dyStuStudyingFarming==null || (dyStuStudyingFarming!=null && StringUtils.isBlank(dyStuStudyingFarming.getGradeId()))){
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
		ex.exportXLSFile("军训信息", titleMap, sheetName2RecordListMap, response);	
	}
	
	public String getObjectName() {
		return "军训信息";
	}
	
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("姓名");
		tis.add("性别");
		tis.add("学号");
		tis.add("班级");
		tis.add("等第");
		tis.add("学年");
		tis.add("学期");
		tis.add("备注");
		return tis;
	}
}
