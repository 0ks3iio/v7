package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
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
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
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
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.entity.DyBusinessOption;
import net.zdsoft.stuwork.data.entity.DyStuPunishment;
import net.zdsoft.stuwork.data.service.DyBusinessOptionService;
import net.zdsoft.stuwork.data.service.DyPermissionService;
import net.zdsoft.stuwork.data.service.DyStuPunishmentService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
@RequestMapping("/stuwork") 
public class DyStuPunishmentAction extends BaseAction{
    @Autowired
	private DyStuPunishmentService dyStuPunishmentService;
    @Autowired
    private DyBusinessOptionService dyBusinessOptionService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private DyPermissionService dyPermissionService;
    
	@RequestMapping("/studentManage/punishScoreInfo")
	public String punishScoreInfoHead(String acadyear, String semester, ModelMap map){
		if (StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)) {
			Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1, getLoginInfo().getUnitId()), Semester.class);
			map.put("nowAcadyear", semesterObj.getAcadyear());
			map.put("nowSemester", String.valueOf(semesterObj.getSemester()));
		} else {
			map.put("nowAcadyear", acadyear);
			map.put("nowSemester", semester);
		}
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), String.class);
		map.put("acadyearList", acadyearList);
		return "/stuwork/studentManage/punishScoreInfoHead.ftl";
	}
	
	@RequestMapping("/studentManage/punishScoreInfoList")
	public String punishScoreInfoList(String acadyear, String semester, String studentName, String punishTypeId, Date startTime, Date endTime, ModelMap map, HttpServletRequest request){
		Pagination page = createPagination();
		String[] studentIds = new String[]{};
		if(StringUtils.isNotBlank(studentName)){
			List<Student> stuList = SUtils.dt(studentRemoteService.findBySchoolIdIn(studentName, new String[]{getLoginInfo().getUnitId()}), new TR<List<Student>>() {});
			Set<String> stuIdSet = new HashSet<String>();
			for(Student stu : stuList){
				stuIdSet.add(stu.getId());
			}
			if(stuIdSet.size()>0){
				studentIds = stuIdSet.toArray(new String[0]);
			}else{
				return "/stuwork/studentManage/punishScoreInfoList.ftl";
			}
		}
		if (StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)) {
			return "/stuwork/studentManage/punishScoreInfoList.ftl";
		}
		List<DyStuPunishment> dyStuPunishmentList = dyStuPunishmentService.findByAll(getLoginInfo().getUnitId(), acadyear, semester, punishTypeId, startTime, endTime, studentIds, page);
		Set<String> studentIdSet = new HashSet<String>();
		for(DyStuPunishment item : dyStuPunishmentList){
			studentIdSet.add(item.getStudentId());
		}
		List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(studentIdSet.toArray(new String[0])), new TR<List<Student>>() {});
		Map<String, String> stuNameMap = new HashMap<String, String>();
		Map<String, String> stuCodeMap = new HashMap<String, String>();
		for(Student stu : studentList){
			stuNameMap.put(stu.getId(), stu.getStudentName());
			stuCodeMap.put(stu.getId(), stu.getStudentCode());
		}
		for(DyStuPunishment item : dyStuPunishmentList){
			item.setStudentName(stuNameMap.get(item.getStudentId()));
			item.setStudentCode(stuCodeMap.get(item.getStudentId()));
		}
		map.put("dyStuPunishmentList", dyStuPunishmentList);
		sendPagination(request, map, page);
		return "/stuwork/studentManage/punishScoreInfoList.ftl";
	}
	
	@RequestMapping("/studentManage/punishScoreInfoEdit")
	public String punishScoreInfoEdit(String id, ModelMap map){
		DyStuPunishment dyStuPunishment;
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1, getLoginInfo().getUnitId()), Semester.class);
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), String.class);
		map.put("acadyearList", acadyearList);
		if(StringUtils.isNotBlank(id)){
			dyStuPunishment = dyStuPunishmentService.findOne(id);
			Student stu = SUtils.dc(studentRemoteService.findOneById(dyStuPunishment.getStudentId()), Student.class);
			dyStuPunishment.setStudentName(stu.getStudentName());
			dyStuPunishment.setStudentCode(stu.getStudentCode());
			map.put("nowAcadyear", dyStuPunishment.getAcadyear());
			map.put("nowSemester", Integer.valueOf(dyStuPunishment.getSemester()));
		}else{
			map.put("nowAcadyear", semester.getAcadyear());
			map.put("nowSemester", semester.getSemester());
			dyStuPunishment = new DyStuPunishment();
		}
		List<DyBusinessOption> dyBusinessOptionList = dyBusinessOptionService.findListByUnitIdAndType(getLoginInfo().getUnitId(), StuworkConstants.BUSINESS_TYPE_1);
		map.put("dyBusinessOptionList", dyBusinessOptionList);
		map.put("dyStuPunishment", dyStuPunishment);
		return "/stuwork/studentManage/punishScoreInfoEdit.ftl";
	}

	@RequestMapping("/studentManage/punishScoreInfoAllDelete")
	@ResponseBody
	public String punishScoreInfoAllDelete(String acadyear, String semester){
		try {
			dyStuPunishmentService.deleteByUnitIdAndAcadyearAndSemester(getLoginInfo().getUnitId(), acadyear, semester);
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		return success("已删除");
	}
	
	@ResponseBody
	@RequestMapping("/studentManage/getStuCode")
	public String getStuCode(String studentId){
		String studentCode = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class).getStudentCode();
		if(StringUtils.isBlank(studentCode)){
			return success("当前学生没有学号");
		}else{
			return success(studentCode);
		}
	}
	
	@ResponseBody
	@RequestMapping("/studentManage/getScore")
	public String getScore(String punishTypeId){
		List<DyBusinessOption> dyBusinessOptionList = dyBusinessOptionService.findListByIds(new String[]{punishTypeId});
		DyBusinessOption dyBusinessOption = new DyBusinessOption();
		if(CollectionUtils.isNotEmpty(dyBusinessOptionList)){
			dyBusinessOption = dyBusinessOptionList.get(0);
		}
		if(1 == dyBusinessOption.getHasScore() && 0 == dyBusinessOption.getIsCustom()){
			return success(String.valueOf(dyBusinessOption.getScore()));
		}else{
			return success("");
		}
	}
	
	@ResponseBody
	@RequestMapping("/studentManage/punishScoreInfoDelete")
	public String punishScoreInfoDelete(String id){
		try{
			dyStuPunishmentService.delete(id);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
	
	@ResponseBody
	@RequestMapping("/studentManage/punishScoreInfoSave")
	public String punishScoreInfoSave(DyStuPunishment dyStuPunishment){
		try{
			if(StringUtils.isBlank(dyStuPunishment.getId())){
				dyStuPunishment.setId(UuidUtils.generateUuid());
			}
			dyStuPunishment.setUnitId(getLoginInfo().getUnitId());
			dyStuPunishment.setPunishTypeId(dyStuPunishment.getPunishTypeId().split("-")[0]);
			DyBusinessOption option = dyBusinessOptionService.findOne(dyStuPunishment.getPunishTypeId().split("-")[0]);
			dyStuPunishment.setPunishName(option.getOptionName());			
			if(1 == option.getHasScore() && 0 == option.getIsCustom()){
				dyStuPunishment.setScore(option.getScore());
			}
			dyStuPunishmentService.save(dyStuPunishment);		
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
	
	@RequestMapping("/studentManage/punishScoreQuery")
	public String punishScoreQuery(ModelMap map){
		List<DyBusinessOption> dyBusinessOptionList = dyBusinessOptionService.findListByUnitIdAndType(getLoginInfo().getUnitId(), StuworkConstants.BUSINESS_TYPE_1);
		
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
		map.put("dyBusinessOptionList", dyBusinessOptionList);
		return "/stuwork/studentManage/punishScoreQueryHead.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/studentManage/clsList")
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
	
	@RequestMapping("/studentManage/punishScoreQueryList")
	public String punishScoreQueryList(String acadyear, String semester, String gradeId, String classId, String studentName, String punishTypeId, Date startTime, Date endTime, ModelMap map, HttpServletRequest request){
		Pagination page = createPagination();
		String[] studentIds = new String[]{};
		if (StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)) {
			return "/stuwork/studentManage/punishScoreQueryList.ftl";
		}
		if(StringUtils.isNotBlank(classId)){
			List<Student> stuList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {});
			Set<String> stuIdSet = new HashSet<String>();
			for(Student stu : stuList){
				stuIdSet.add(stu.getId());
			}
			if(stuIdSet.size()>0){
				studentIds = stuIdSet.toArray(new String[0]);
			}else{
				return "/stuwork/studentManage/punishScoreQueryList.ftl";
			}
		}else{
			if(StringUtils.isNotBlank(gradeId)){
				List<Clazz> clazzs = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), new TR<List<Clazz>>() {});
				Set<String> clsIdSet = new HashSet<String>();
				//班级权限
				Set<String> classPermission = dyPermissionService.findClassSetByUserId(getLoginInfo().getUserId());
				Iterator<Clazz> it = clazzs.iterator();	
				while(it.hasNext()) {
					Clazz clazz = it.next();
					if (classPermission.contains(clazz.getId())) {
						clsIdSet.add(clazz.getId());
					}
				}
				List<Student> stuList = new ArrayList<>();
				if(CollectionUtils.isNotEmpty(clsIdSet)){					
					stuList = SUtils.dt(studentRemoteService.findByClassIds(clsIdSet.toArray(new String[0])), new TR<List<Student>>() {});
				}
				
				
				Set<String> stuIdSet = new HashSet<String>();
				for(Student stu : stuList){
					stuIdSet.add(stu.getId());
				}
				if(stuIdSet.size()>0){
					studentIds = stuIdSet.toArray(new String[0]);
				}else{
					return "/stuwork/studentManage/punishScoreQueryList.ftl";
				}
			}else{
				Set<String> classPermission = dyPermissionService.findClassSetByUserId(getLoginInfo().getUserId());
				List<Student> stuList = new ArrayList<>();
				if(CollectionUtils.isNotEmpty(classPermission)){					
					stuList = SUtils.dt(studentRemoteService.findByClassIds(classPermission.toArray(new String[0])), new TR<List<Student>>() {});
				}
				Set<String> stuIdSet = new HashSet<String>();
				for(Student stu : stuList){
					stuIdSet.add(stu.getId());
				}
				if(stuIdSet.size()>0){
					studentIds = stuIdSet.toArray(new String[0]);
				}else{
					return "/stuwork/studentManage/punishScoreQueryList.ftl";
				}
			}
		}		
		if(StringUtils.isNotBlank(studentName)){
			List<Student> stuList = SUtils.dt(studentRemoteService.findBySchoolIdIn(studentName, new String[]{getLoginInfo().getUnitId()}), new TR<List<Student>>() {});
			Set<String> stuIdSet = new HashSet<String>();
			for(Student stu : stuList){
				stuIdSet.add(stu.getId());
			}
			if(stuIdSet.size()>0){
				studentIds = stuIdSet.toArray(new String[0]);
			}else{
				return "/stuwork/studentManage/punishScoreQueryList.ftl";
			}
		}
		List<DyStuPunishment> dyStuPunishmentList = dyStuPunishmentService.findByAll(getLoginInfo().getUnitId(), acadyear, semester, punishTypeId, startTime, endTime, studentIds, page);
		Set<String> studentIdSet = new HashSet<String>();
		for(DyStuPunishment item : dyStuPunishmentList){
			studentIdSet.add(item.getStudentId());
		}
		List<Student> studentListTemp = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(studentIdSet)){
			studentListTemp = SUtils.dt(studentRemoteService.findListByIds(studentIdSet.toArray(new String[0])), new TR<List<Student>>() {});
		}
		List<Student> studentList = new ArrayList<>();
		for(Student student : studentListTemp){
			if(1!=student.getIsLeaveSchool()){
				studentList.add(student);
			}
		}
		Map<String, String> stuNameMap = new HashMap<String, String>();
		Map<String, String> stuCodeMap = new HashMap<String, String>();
		Map<String, String> clsIdMap = new HashMap<>();
		Set<String> clsIds = new HashSet<>();
		for(Student stu : studentList){
			stuNameMap.put(stu.getId(), stu.getStudentName());
			stuCodeMap.put(stu.getId(), stu.getStudentCode());
			clsIdMap.put(stu.getId(), stu.getClassId());
			clsIds.add(stu.getClassId());
		}
		Map<String, Clazz> clsMap = EntityUtils.getMap(SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<Clazz>>() {}), Clazz::getId);
		for(DyStuPunishment item : dyStuPunishmentList){
			item.setStudentName(stuNameMap.get(item.getStudentId()));
			item.setStudentCode(stuCodeMap.get(item.getStudentId()));
			if(clsMap.containsKey(clsIdMap.get(item.getStudentId()))) {
				//TODO by nizq
				item.setClassName(clsMap.get(clsIdMap.get(item.getStudentId())).getClassNameDynamic());
			}
		}
		map.put("dyStuPunishmentList", dyStuPunishmentList);
		sendPagination(request, map, page);		
		return "/stuwork/studentManage/punishScoreQueryList.ftl";
	}
	
	@RequestMapping("/studentManage/punishScoreQueryExport")
	public void punishScoreQueryExport(String gradeId, String classId, String studentName, String punishTypeId, Date startTime, Date endTime, ModelMap map, HttpServletRequest request,HttpServletResponse response){
		String[] studentIds = new String[]{};
		if(StringUtils.isNotBlank(classId)){
			List<Student> stuList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {});
			Set<String> stuIdSet = new HashSet<String>();
			for(Student stu : stuList){
				stuIdSet.add(stu.getId());
			}
			if(stuIdSet.size()>0){
				studentIds = stuIdSet.toArray(new String[0]);
			}else{
				List<DyStuPunishment> dyStuPunishmentList = new ArrayList<DyStuPunishment>();
				doExport(dyStuPunishmentList, response);
			}
		}		
		if(StringUtils.isNotBlank(classId)){
			List<Student> stuList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {});
			Set<String> stuIdSet = new HashSet<String>();
			for(Student stu : stuList){
				stuIdSet.add(stu.getId());
			}
			if(stuIdSet.size()>0){
				studentIds = stuIdSet.toArray(new String[0]);
			}else{
				List<DyStuPunishment> dyStuPunishmentList = new ArrayList<DyStuPunishment>();
				doExport(dyStuPunishmentList, response);
			}
		}else{
			if(StringUtils.isNotBlank(gradeId)){
				List<Clazz> clazzs = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), new TR<List<Clazz>>() {});
				Set<String> clsIdSet = new HashSet<String>();
				//班级权限
				Set<String> classPermission = dyPermissionService.findClassSetByUserId(getLoginInfo().getUserId());
				Iterator<Clazz> it = clazzs.iterator();	
				while(it.hasNext()) {
					Clazz clazz = it.next();
					if (classPermission.contains(clazz.getId())) {
						clsIdSet.add(clazz.getId());
					}
				}
				List<Student> stuList = new ArrayList<>();
				if(CollectionUtils.isNotEmpty(clsIdSet)){					
					stuList = SUtils.dt(studentRemoteService.findByClassIds(clsIdSet.toArray(new String[0])), new TR<List<Student>>() {});
				}
				
				
				Set<String> stuIdSet = new HashSet<String>();
				for(Student stu : stuList){
					stuIdSet.add(stu.getId());
				}
				if(stuIdSet.size()>0){
					studentIds = stuIdSet.toArray(new String[0]);
				}else{
					List<DyStuPunishment> dyStuPunishmentList = new ArrayList<DyStuPunishment>();
					doExport(dyStuPunishmentList, response);
				}
			}else{
				Set<String> classPermission = dyPermissionService.findClassSetByUserId(getLoginInfo().getUserId());
				List<Student> stuList = new ArrayList<>();
				if(CollectionUtils.isNotEmpty(classPermission)){					
					stuList = SUtils.dt(studentRemoteService.findByClassIds(classPermission.toArray(new String[0])), new TR<List<Student>>() {});
				}
				Set<String> stuIdSet = new HashSet<String>();
				for(Student stu : stuList){
					stuIdSet.add(stu.getId());
				}
				if(stuIdSet.size()>0){
					studentIds = stuIdSet.toArray(new String[0]);
				}else{
					List<DyStuPunishment> dyStuPunishmentList = new ArrayList<DyStuPunishment>();
					doExport(dyStuPunishmentList, response);
				}
			}
		}		
		if(StringUtils.isNotBlank(studentName)){
			List<Student> stuList = SUtils.dt(studentRemoteService.findBySchoolIdIn(studentName, new String[]{getLoginInfo().getUnitId()}), new TR<List<Student>>() {});
			Set<String> stuIdSet = new HashSet<String>();
			for(Student stu : stuList){
				stuIdSet.add(stu.getId());
			}
			if(stuIdSet.size()>0){
				studentIds = stuIdSet.toArray(new String[0]);
			}else{
				List<DyStuPunishment> dyStuPunishmentList = new ArrayList<DyStuPunishment>();
				doExport(dyStuPunishmentList, response);
			}
		}
		List<DyStuPunishment> dyStuPunishmentList = dyStuPunishmentService.findByAll(getLoginInfo().getUnitId(), null, null, punishTypeId, startTime, endTime, studentIds, null);
		Set<String> studentIdSet = new HashSet<String>();
		for(DyStuPunishment item : dyStuPunishmentList){
			studentIdSet.add(item.getStudentId());
		}
		List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(studentIdSet.toArray(new String[0])), new TR<List<Student>>() {});
		Map<String, String> stuNameMap = new HashMap<String, String>();
		Map<String, String> stuCodeMap = new HashMap<String, String>();
		Map<String, String> clsIdMap = new HashMap<>();
		Set<String> clsIds = new HashSet<>();
		for(Student stu : studentList){
			stuNameMap.put(stu.getId(), stu.getStudentName());
			stuCodeMap.put(stu.getId(), stu.getStudentCode());
			clsIdMap.put(stu.getId(), stu.getClassId());
			clsIds.add(stu.getClassId());
		}
		Map<String, Clazz> clsMap = EntityUtils.getMap(SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<Clazz>>() {}), Clazz::getId);
		for(DyStuPunishment item : dyStuPunishmentList){
			item.setStudentName(stuNameMap.get(item.getStudentId()));
			item.setStudentCode(stuCodeMap.get(item.getStudentId()));
			if(clsMap.containsKey(clsIdMap.get(item.getStudentId()))) {
				//TODO by nizq
				item.setClassName(clsMap.get(clsIdMap.get(item.getStudentId())).getClassNameDynamic());
			}
		}
		doExport(dyStuPunishmentList,response);
	}
	
	public void doExport(List<DyStuPunishment> dyStuPunishmentList,HttpServletResponse response){
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		for(DyStuPunishment item : dyStuPunishmentList){
			Map<String,String> sMap = new HashMap<String,String>();
			sMap.put("班級", item.getClassName());
			sMap.put("姓名", item.getStudentName());
			sMap.put("学号", item.getStudentCode());
			sMap.put("学年", item.getAcadyear());
			sMap.put("学期", StringUtils.equals(item.getSemester(), "1") ? "第一学期" : "第二学期");
			sMap.put("违纪类型", item.getPunishName());
			sMap.put("违纪原因", item.getPunishContent());		
			sMap.put("违纪扣分", String.valueOf(item.getScore()));
			sMap.put("违纪时间", String.valueOf(item.getPunishDate()).split(" ")[0]);
			recordList.add(sMap);
		}
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList();
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile("学生违纪信息", titleMap, sheetName2RecordListMap, response);	
	}
	
	public String getObjectName() {
		return "学生违纪";
	}
	
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("班級");
		tis.add("姓名");
		tis.add("学号");
		tis.add("学年");
		tis.add("学期");
		tis.add("违纪类型");
		tis.add("违纪原因");
		tis.add("违纪扣分");
		tis.add("违纪时间");
		return tis;
	}
}
