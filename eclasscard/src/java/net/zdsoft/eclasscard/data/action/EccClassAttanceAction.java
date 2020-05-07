package net.zdsoft.eclasscard.data.action;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.StusysSectionTimeSetRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dto.ClassAttNumSumDto;
import net.zdsoft.eclasscard.data.entity.EccClassAttence;
import net.zdsoft.eclasscard.data.entity.EccStuclzAttence;
import net.zdsoft.eclasscard.data.entity.EccTeaclzAttence;
import net.zdsoft.eclasscard.data.service.EccClassAttenceService;
import net.zdsoft.eclasscard.data.service.EccStuclzAttenceService;
import net.zdsoft.eclasscard.data.service.EccTeaclzAttenceService;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ServletUtils;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
@Controller
@RequestMapping("/eclasscard")
public class EccClassAttanceAction extends BaseAction{

	@Autowired
	private EccClassAttenceService eccClassAttenceService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private EccStuclzAttenceService eccStuclzAttenceService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private StusysSectionTimeSetRemoteService stusysSectionTimeSetRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	@Autowired
	private EccTeaclzAttenceService eccTeaclzAttenceService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;
	
	@RequestMapping("/calss/signin/index")
	public String classSignInIndex(ModelMap map){
		return "/eclasscard/class/studentClassAttIndex.ftl";
	}
	@RequestMapping("/class/signin/tabPage")
	public String classSignInTab(ModelMap map){
		Map<String,String> periodMap = eccClassAttenceService.findSectionNumMap(getLoginInfo().getUnitId());
		//班级权限
		Set<String> classPermission = stuworkRemoteService.findClassSetByUserId(getLoginInfo().getUserId());
		List<Grade> grades = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(classPermission)) {
			List<Clazz> clazzs = SUtils.dt(classRemoteService.findClassListByIds(classPermission.toArray(new String[0])),new TR<List<Clazz>>() {});
			Set<String> gradeIds = EntityUtils.getSet(clazzs, Clazz::getGradeId);
			grades = SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[0])),new TR<List<Grade>>() {});
		}
		map.put("periodMap", periodMap);
		if (CollectionUtils.isNotEmpty(grades)) {
			Collections.sort(grades, new Comparator<Grade>() {
				@Override
				public int compare(Grade arg0, Grade arg1) {
					return arg0.getGradeCode().compareTo(arg1.getGradeCode());
				}
			});
		}
		map.put("grades", grades);
		map.put("nowDate", new Date());
		return "/eclasscard/class/studentClassAttTab.ftl";
	}
	
	@RequestMapping("/mycalss/signin/list")
	public String myClassSignIn(String type,int section,String classId,Date date,HttpServletRequest request,ModelMap map){
		Pagination page=createPagination();
		List<EccClassAttence> classAttences = eccClassAttenceService.findListByStuAtt(getLoginInfo().getOwnerId(), date, getLoginInfo().getUnitId(), section, classId,type,page);
		map.put("classAttences", classAttences);
		if("2".equals(type)){
			sendPagination(request, map, page);
			return "/eclasscard/class/allClaasStuAttList.ftl";
		}else{
			return "/eclasscard/class/myClaasStuAttList.ftl";
		}
	}
	
	@RequestMapping("/mycalss/signin/detail/list")
	public String myClassSignInDetail(String id,String type,ModelMap map){
		EccClassAttence classAttence = eccClassAttenceService.findByIdFillName(id);
		if(classAttence!=null&&classAttence.getClassType()>0){
			List<EccStuclzAttence> eccStuclzAttences = eccStuclzAttenceService.findListByClassAttId(classAttence.getId(),classAttence.getClassType(),classAttence.getClassId(),type);
			map.put("eccStuclzAttences", eccStuclzAttences);
		}else{
			classAttence = new EccClassAttence();
		}
		map.put("type", type);
		map.put("classAttence", classAttence);
		return "/eclasscard/class/studentClassAttDetail.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/mycalss/signin/detail/save")
	public String myClassSignInDetailSave(String classAttId,String[] ids,int status,ModelMap map){
		try{
//			EccClassAttence classAttence = eccClassAttenceService.findOne(classAttId);
			List<EccStuclzAttence> eccStuclzAttences = eccStuclzAttenceService.findListByIds(ids);
			for(EccStuclzAttence stuclzAttence:eccStuclzAttences){
				stuclzAttence.setStatus(status);
//				if(classAttence!=null){
//					stuclzAttence.setClockDate(classAttence.getClockDate());
//				}else{
//					if(stuclzAttence.getClockDate()==null){
//						stuclzAttence.setClockDate(new Date());
//					}
//				}
			}
			if(eccStuclzAttences.size()>0){
				eccStuclzAttenceService.saveAll(eccStuclzAttences.toArray(new EccStuclzAttence[eccStuclzAttences.size()]));
			}
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@RequestMapping("/student/sum/index")
	public String studentSumIndex(ModelMap map){
		return "/eclasscard/class/studentClassAttSumIndex.ftl";
	}
	@RequestMapping("/student/sum/tabPage")
	public String studentSumTabPage(ModelMap map){
		map.put("beginDate", EccUtils.getSundayOfLastWeek());
		map.put("nowDate", new Date());
		return "/eclasscard/class/studentClassAttSumTab.ftl";
	}
	
	@RequestMapping("/class/stuAttance/list")
	public String classStuAttanceList(String classId,Date beginDate,Date endDate,ModelMap map){
		Clazz clz = SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
		
		List<ClassAttNumSumDto> attNumSumDtos = eccStuclzAttenceService.findByClassIdSum(classId, beginDate, endDate);
		if(clz!=null){
			map.put("className", clz.getClassNameDynamic());
		}
		map.put("attNumSumDtos", attNumSumDtos);
		return "/eclasscard/class/studentClassAttSumList.ftl";
	}
	@RequestMapping("/class/stuAttance/detail")
	public String classStuAttanceDetail(String studentId,Date beginDate,Date endDate,HttpServletRequest request,ModelMap map){
		String bDate = DateUtils.date2StringByDay(beginDate);
		String eDate = DateUtils.date2StringByDay(endDate);
		Pagination page=createPagination();
		Student student= SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
		List<EccClassAttence> classAttences = eccClassAttenceService.findByStudentIdSum(studentId, bDate, eDate,page);
		map.put("beginDate", bDate);
		map.put("endDate", eDate);
		if(student!=null){
			map.put("studentName", student.getStudentName());
		}
		map.put("classAttences", classAttences);
		sendPagination(request, map, page);
		return "/eclasscard/class/studentClassAttSumDetail.ftl";
	}
	
	/**
	 * 老师打卡
	 */
	@RequestMapping("/teacher/signin/index")
	public String teacherSignInIndex(ModelMap map){
		return "/eclasscard/class/teacherClassAttIndex.ftl";
	}
	@RequestMapping("/teacher/signin/tabPage")
	public String teacherSignInTab(ModelMap map){
		map.put("nowDate", new Date());
		return "/eclasscard/class/teacherClassAttTab.ftl";
	}
	@RequestMapping("/teacher/signin/list")
	public String teacherSignIn(Date date,ModelMap map){
		List<EccClassAttence> classAttences = eccClassAttenceService.findListByTeaAtt(date, getLoginInfo().getUnitId());
		map.put("classAttences", classAttences);
		return "/eclasscard/class/teacherSectionAttList.ftl";
	}
	@RequestMapping("/teacher/signin/detail")
	public String teacherSignInDetail(Date date,Integer sectionNumber,HttpServletRequest request,ModelMap map){
		EccClassAttence classAttence = new EccClassAttence();
		classAttence.setSectionNumber(sectionNumber);
		classAttence.setSectionName(EccUtils.getSectionName(sectionNumber));
		int lateNum = 0;
		int quekeNum = 0;
		Pagination page=createPagination();
		List<EccTeaclzAttence> teaclzAttences = eccClassAttenceService.findListByTeaAttDetail(date, getLoginInfo().getUnitId(),sectionNumber);
		for(EccTeaclzAttence attence:teaclzAttences){
			if(EccConstants.CLASS_ATTENCE_STATUS1==attence.getStatus()){
				quekeNum++;
			}else if(EccConstants.CLASS_ATTENCE_STATUS2==attence.getStatus()){
				lateNum ++;
			}
		}
		classAttence.setQkStuNum(quekeNum);
		classAttence.setCdStuNum(lateNum);
		map.put("classAttence", classAttence);
		map.put("teaclzAttences", teaclzAttences);
		sendPagination(request, map, page);
		return "/eclasscard/class/teacherSectionAttDetail.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/teacher/signin/detail/save")
	public String teacherSignInDetailSave(String[] ids,int status,ModelMap map){
		try{
			List<EccTeaclzAttence> teaclzAttences = eccTeaclzAttenceService.findListByIds(ids);
			for(EccTeaclzAttence teaclzAttence:teaclzAttences){
				teaclzAttence.setStatus(status);
			}
			if(teaclzAttences.size()>0){
				eccTeaclzAttenceService.saveAll(teaclzAttences.toArray(new EccTeaclzAttence[teaclzAttences.size()]));
			}
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@RequestMapping("/teacher/sum/index")
	public String teacherSumIndex(ModelMap map){
		return "/eclasscard/class/teacherClassAttSumIndex.ftl";
	}
	
	@RequestMapping("/teacher/sum/tabPage")
	public String teacherSumTabPage(ModelMap map){
		map.put("beginDate", EccUtils.getSundayOfLastWeek());
		map.put("nowDate", new Date());
		return "/eclasscard/class/teacherClassAttSumTab.ftl";
	}
	
	@RequestMapping("/teacher/teaAttance/list")
	public String teacherAttanceList(String deptId,Date beginDate,Date endDate,ModelMap map){
		Dept dept = SUtils.dc(deptRemoteService.findOneById(deptId),Dept.class);
		
		List<EccTeaclzAttence> teaclzAttences = eccTeaclzAttenceService.findByDeptIdSum(deptId, beginDate, endDate);
		if(dept!=null){
			map.put("deptName", dept.getDeptName());
		}
		map.put("teaclzAttences", teaclzAttences);
		return "/eclasscard/class/teacherDeptAttSumList.ftl";
	}
	
	@RequestMapping("/teacher/teaAttance/detail")
	public String teacherAttanceDetail(String teacherId,Date beginDate,Date endDate,HttpServletRequest request,ModelMap map){
		String bDate = DateUtils.date2StringByDay(beginDate);
		String eDate = DateUtils.date2StringByDay(endDate);
		Pagination page=createPagination();
		Teacher teacher= SUtils.dc(teacherRemoteService.findOneById(teacherId),Teacher.class);
		List<EccClassAttence> classAttences = eccClassAttenceService.findByTeacherIdSum(teacherId, bDate, eDate,page);
		map.put("beginDate", bDate);
		map.put("endDate", eDate);
		if(teacher!=null){
			map.put("teacherName", teacher.getTeacherName());
		}
		map.put("classAttences", classAttences);
		sendPagination(request, map, page);
		return "/eclasscard/class/teacherAttSumDetail.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/get/class/page")
	public void getClass(String gradeId,ModelMap map){
		JSONArray jsonArray = new JSONArray();
		Set<String> classIds = stuworkRemoteService.findClassSetByUserId(getLoginInfo().getUserId());
		if (CollectionUtils.isNotEmpty(classIds)) {
			List<Clazz> clazzs = SUtils.dt(classRemoteService.findBySchoolIdGradeId(getLoginInfo().getUnitId(), gradeId),new TR<List<Clazz>>() {});
			for(Clazz clazz:clazzs){
				if(classIds.contains(clazz.getId())){
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id",clazz.getId());
					jsonObject.put("name",clazz.getClassNameDynamic());
					jsonArray.add(jsonObject);
				}
			}
		}
		try {
			ServletUtils.print(getResponse(), jsonArray.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@ResponseBody
	@RequestMapping("/get/classall/page")
	public void getClassAll(String gradeId,ModelMap map){
		JSONArray jsonArray = new JSONArray();
		List<Clazz> clazzs = SUtils.dt(classRemoteService.findBySchoolIdGradeId(getLoginInfo().getUnitId(), gradeId),new TR<List<Clazz>>() {});
		for(Clazz clazz:clazzs){
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id",clazz.getId());
			jsonObject.put("name",clazz.getClassNameDynamic());
			jsonArray.add(jsonObject);
		}
		try {
			ServletUtils.print(getResponse(), jsonArray.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
