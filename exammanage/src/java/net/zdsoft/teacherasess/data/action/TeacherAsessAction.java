package net.zdsoft.teacherasess.data.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.StudentSelectSubjectRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.teacherasess.data.dto.ClassDto;
import net.zdsoft.teacherasess.data.dto.RankDto;
import net.zdsoft.teacherasess.data.dto.TeaSetDto;
import net.zdsoft.teacherasess.data.entity.TeacherAsess;
import net.zdsoft.teacherasess.data.entity.TeacherAsessCheck;
import net.zdsoft.teacherasess.data.entity.TeacherAsessLine;
import net.zdsoft.teacherasess.data.entity.TeacherAsessRank;
import net.zdsoft.teacherasess.data.entity.TeacherAsessResult;
import net.zdsoft.teacherasess.data.entity.TeacherAsessSet;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvert;
import net.zdsoft.teacherasess.data.service.TeacherasessCheckService;
import net.zdsoft.teacherasess.data.service.TeacherasessConvertService;
import net.zdsoft.teacherasess.data.service.TeacherasessLineService;
import net.zdsoft.teacherasess.data.service.TeacherasessRankService;
import net.zdsoft.teacherasess.data.service.TeacherasessResultService;
import net.zdsoft.teacherasess.data.service.TeacherasessService;
import net.zdsoft.teacherasess.data.service.TeacherasessSetService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

@Controller
@RequestMapping("/teacherasess")
public class TeacherAsessAction extends BaseAction {
	
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private TeacherasessService teacherasessService;
	@Autowired
	private GradeRemoteService GradeRemoteService;
	@Autowired
	private TeacherasessConvertService teacherasessConvertService;
	@Autowired
	private TeacherasessResultService teacherasessResultService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private ClassTeachingRemoteService classTeachingRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private TeacherasessSetService teacherasessSetService;
	@Autowired
	private TeacherasessRankService teacherasessRankService;
	@Autowired
	private StudentSelectSubjectRemoteService studentSelectSubjectRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuRemoteService;
	@Autowired
	private TeacherasessLineService teacherasessLineService;
	@Autowired
	private TeacherasessCheckService teacherasessCheckService;
	
	private static final String[] ABS={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

	@RequestMapping("/asess/index/page")
	@ControllerInfo(value = "考核方案")
	public String showConList(ModelMap map,HttpSession httpSession,String acadyear){
		
		LoginInfo info = getLoginInfo(httpSession);
		String unitId = info.getUnitId();
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年不存在");
		}
        if(StringUtils.isBlank(acadyear)) {
        	Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
        	acadyear = semester.getAcadyear();
        }
		List<TeacherAsess> teacherAsessList = teacherasessService.findByUnitIdAndAcayearWithMaster(unitId, acadyear);
		List<String> ids = new ArrayList<>();
		for (TeacherAsess dto : teacherAsessList) {
			if(StringUtils.equals(dto.getStatus(), "1")) {
				ids.add(dto.getId());
			}
		}
		map.put("ids", ids);
		map.put("teacherAsessList", teacherAsessList);
		map.put("acadyearList", acadyearList);
		map.put("acadyear", acadyear);
		
		return "/teacherasess/asessResult/asessIndexList.ftl";
	}
	
	@RequestMapping("/asess/edit/page")
    @ControllerInfo(value = "新增考核方案")
    public String showExamInfo(String id,ModelMap map, HttpSession httpSession) {
		LoginInfo info = getLoginInfo(httpSession);
		String unitId = info.getUnitId();
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
	 	List<Grade> gradeList = SUtils.dt(GradeRemoteService.findByUnitIdAndCurrentAcadyear(unitId,semester.getAcadyear()), new TR<List<Grade>>(){});
		Collections.sort(gradeList, new Comparator<Grade>(){
			@Override
			public int compare(Grade o1, Grade o2) {
				return (StringUtils.isNotEmpty(o1.getGradeCode())?Integer.valueOf(o1.getGradeCode()):0)-(StringUtils.isNotEmpty(o2.getGradeCode())?Integer.valueOf(o2.getGradeCode()):0);
			}
		});
		map.put("gradeList", gradeList);
		map.put("teacherAsess", new TeacherAsess());
        
        return "/teacherasess/asessResult/asessAdd.ftl";
    }
	
	@ResponseBody
	@RequestMapping("/asess/convertList")
    @ControllerInfo(value = "折算分方案列表")
	public List<TeacherasessConvert> convertList(String gradeId,ModelMap map, HttpSession httpSession) {
		//根据年级id查询折分方案
		LoginInfo info = getLoginInfo(httpSession);
		String unitId = info.getUnitId();
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
		List<TeacherasessConvert> convertList = teacherasessConvertService.findListByAcadyearAndGradeId(unitId, semester.getAcadyear(), gradeId);
		return convertList;
    }
	
	@ResponseBody
    @RequestMapping("/asess/save")
    @ControllerInfo(value = "保存考核方案")
    public String doSaveExam(TeacherAsess teacherAsess,ModelMap map,String type, HttpSession httpSession) {
		try{
			if(StringUtils.equals(teacherAsess.getConvertId(), teacherAsess.getReferConvertId())){
				return error("本次考核方案和原始考核方案一样，请修正！");
			}
			Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
			teacherAsess.setAcadyear(semester.getAcadyear());
			teacherAsess.setCreationTime(new Date());
			teacherAsess.setIsDeleted(0);
			String unitId = getLoginInfo().getUnitId();
			if(StringUtils.equals(type, "2")) {
				teacherAsess.setStatus("1");//对比中
			}else{
				teacherAsess.setStatus("0");//未对比
			}
			teacherAsess.setUnitId(unitId);
			teacherAsess.setId(UuidUtils.generateUuid());
			teacherasessService.saveAllEntitys(teacherAsess);
			if(StringUtils.equals(type, "2")) {
				verificationData(teacherAsess.getId(),map,httpSession);
			}
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
    }
	
	@ResponseBody
	@RequestMapping("/asess/delete")
	@ControllerInfo("删除考核方案")
	public String doDeleteInfo(String id,HttpSession httpSession) {
		try{
			String unitId = getLoginInfo().getUnitId();
			teacherasessService.deleteByIdIn(unitId,id);
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/asess/dealData")
    @ControllerInfo("对比")
    public String verificationData(final String teacherAsessId, ModelMap map, HttpSession httpSession){
		final String unitId = getLoginInfo().getUnitId();
		final String key = unitId+"_"+teacherAsessId;
		if(RedisUtils.get(key)==null){
			RedisUtils.set(key,"start");
		}else{
			JSONObject jsonObject=new JSONObject();;
			jsonObject.put("type", RedisUtils.get(key));
			jsonObject.put("teacherAsessId",teacherAsessId);
			if("success".equals(RedisUtils.get(key)) || "error".equals(RedisUtils.get(key))){
				RedisUtils.del(key);
			}
			return JSON.toJSONString(jsonObject);
		}
		
		new Thread(new Runnable(){  
			public void run(){
				TeacherAsess teacherAsess = teacherasessService.findOneWithMaster(teacherAsessId);
				if(teacherAsess!=null){
					teacherasessService.updateforStatus("1", teacherAsessId);
				}
				Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
				try {
					teacherasessService.dealWithTeacherAsessResult(unitId, semester.getAcadyear(), semester.getSemester()+"", teacherAsessId);
					RedisUtils.set(key,"success");
				} catch (Exception e) {
					e.printStackTrace();
					RedisUtils.set(key,"error");
				}
				return;
			}
		}).start();
		
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("type", RedisUtils.get(key));
		jsonObject.put("teacherAsessId", teacherAsessId);
		if("success".equals(RedisUtils.get(key)) || "error".equals(RedisUtils.get(key))){
			RedisUtils.del(key);
		}
		return JSON.toJSONString(jsonObject);
	}
	@RequestMapping("/asessResult/asessResultIndex/page")
	@ControllerInfo("考核方案结果列表")
	public String showAsessResultIndex(ModelMap map,  HttpSession httpSession,HttpServletRequest request){
		String teacherAsessId=request.getParameter("teacherAsessId");
		String url="/teacherasess/asessResult/asessResultIndex.ftl";
		String unitId = getLoginInfo(httpSession).getUnitId();
		TeacherAsess teacherAsess=teacherasessService.findOne(teacherAsessId);
		if(teacherAsess==null){
			return errorFtl(map, "考核方案不存在");
		}
    	Set<String> asessIds=new HashSet<String>();
		asessIds.add(teacherAsess.getConvertId());
		asessIds.add(teacherAsess.getReferConvertId());
		List<TeacherasessConvert> teacherasessConverts=teacherasessConvertService.findByIdIn(asessIds.toArray(new String[0]));
		Map<String,String> teaMap=EntityUtils.getMap(teacherasessConverts, TeacherasessConvert::getId, TeacherasessConvert::getName);
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
		Map<String, Grade> gradeMap = EntityUtils.getMap(SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(unitId, semester.getAcadyear()), new TR<List<Grade>>() {}), Grade::getId);
		if(gradeMap.containsKey(teacherAsess.getGradeId())){
			teacherAsess.setGradeName(gradeMap.get(teacherAsess.getGradeId()).getGradeName());
		}
		if(teaMap.containsKey(teacherAsess.getConvertId())){
			teacherAsess.setConvertName(teaMap.get(teacherAsess.getConvertId()));
		}
		if(teaMap.containsKey(teacherAsess.getReferConvertId())){
			teacherAsess.setReferConvertName(teaMap.get(teacherAsess.getReferConvertId()));
		}
		List<Course> courses=teacherasessResultService.findByUnitIdAndAsessId(unitId, teacherAsessId);
		
		map.put("classType", "1");//默认1  行政班考核结果
		map.put("teacherAsessId", teacherAsessId);
		map.put("teacherAsess", teacherAsess);
		map.put("courses", courses);
		map.put("courseId", CollectionUtils.isNotEmpty(courses)?courses.get(0).getId():null);
		return url;
	}
	
	@RequestMapping("/asessResult/asessResultList/page")
	@ControllerInfo("考核方案结果列表")
	public String showAsessListIn(ModelMap map,  HttpSession httpSession,String teacherAsessId,HttpServletRequest request){
		String url="/teacherasess/asessResult/asessResultList.ftl";
		
		String subjectId=request.getParameter("subjectId");
		String resultType=request.getParameter("resultType");
		String classType=request.getParameter("classType");
		String unitId = getLoginInfo(httpSession).getUnitId();
		map.put("teacherAsessId", teacherAsessId);
		map.put("resultType", resultType);
		map.put("classType", classType);
		
		List<TeacherAsessResult> teacherAsessResults=null;
        if("1".equals(classType)){//行政班考核
        	teacherAsessResults=teacherasessResultService.findByUnitIdAndAsessIdAndClassTypeAndSubjectId(unitId, teacherAsessId, "1", "00000000000000000000000000000000");
        }else{//教学班考核
        	teacherAsessResults=teacherasessResultService.findByUnitIdAndAsessIdAndSubjectId(unitId, teacherAsessId, subjectId);
        }
        paixu(teacherAsessResults);
        map.put("teacherAsessResults", teacherAsessResults);
        return url;
	}
	private void paixu(List<TeacherAsessResult> teacherAsessResults){
		if(CollectionUtils.isNotEmpty(teacherAsessResults)){
			Collections.sort(teacherAsessResults, new Comparator<TeacherAsessResult>(){
				@Override
				public int compare(TeacherAsessResult o1, TeacherAsessResult o2) {
					return o1.getClassName().compareTo(o2.getClassName());
				}
				
			});
		}
	}
	
	@RequestMapping("/asessResult/asessResultChange/page")
	@ControllerInfo("AB设置分层")
	public String showAsessResultChange(ModelMap map,  HttpSession httpSession,HttpServletRequest request){
		String teacherAsessId=request.getParameter("teacherAsessId");
		String url="/teacherasess/asessResult/asessResultChange.ftl";
		String unitId = getLoginInfo(httpSession).getUnitId();
		TeacherAsess teacherAsess=teacherasessService.findOne(teacherAsessId);
		if(teacherAsess==null){
			return errorFtl(map, "考核方案不存在");
		}
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73YSY(unitId), new TR<List<Course>>() {});
		//List<Course> courses=teacherasessResultService.findByUnitIdAndAsessId(unitId, teacherAsessId);
		Course allCourse = new Course();
		allCourse.setId(BaseConstants.ZERO_GUID);
		allCourse.setSubjectName("总分");
		courseList.add(allCourse);
		
		map.put("teacherAsessId", teacherAsessId);
		map.put("teacherAsess", teacherAsess);
		map.put("courses", courseList);
		map.put("courseId", CollectionUtils.isNotEmpty(courseList)?courseList.get(0).getId():null);
		return url;
	}
	
	@RequestMapping("/asessResult/asessResultChange/edit")
	@ControllerInfo("AB设置分层")
	public String showAsessResultChangeEdit(ModelMap map,  HttpSession httpSession,HttpServletRequest request){
		String teacherAsessId=request.getParameter("teacherAsessId");
		String subjectId=request.getParameter("subjectId");
		String url="/teacherasess/asessResult/asessResultChangeEdit.ftl";
		String unitId = getLoginInfo(httpSession).getUnitId();
		Course subject=courseRemoteService.findOneObjectById(subjectId);
		TeacherAsess teacherAsess=teacherasessService.findOne(teacherAsessId);
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
		if(teacherAsess==null){
			return errorFtl(map, "考核方案不存在");
		}
		List<TeacherAsessSet> teacherAsessSets=new ArrayList<>();
		List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, teacherAsess.getGradeId()),new TR<List<Clazz>>() {});
		Set<String> classIds = EntityUtils.getSet(classList, Clazz::getId);
		List<TeacherAsessSet> setOld=teacherasessSetService.findByUnitIdAndAsessIdAndSubjectId(unitId, teacherAsessId, subjectId);
		List<TeacherAsessRank> teacherAsessRanks=teacherasessRankService.findByUnitIdAndAsessIdAndSubjectId(unitId, teacherAsessId, subjectId);
		Map<String,TeacherAsessSet> teacherAsessSetMap=EntityUtils.getMap(setOld, e->e.getClassId()+"_"+e.getClassType());
		if(StringUtils.isNotBlank(teacherAsess.getXuankaoType())&&StringUtils.equals(teacherAsess.getXuankaoType(), "1")){
			if(!StringUtils.equals(subjectId, BaseConstants.ZERO_GUID)){
				//选考学生
				Map<String, Set<String>> xuankaoStuIdMap=SUtils.dt(studentSelectSubjectRemoteService.getStuSelectByGradeId(semester.getAcadyear(),semester.getSemester()+"",teacherAsess.getGradeId(),true, new String[]{subjectId}),new TR<Map<String,Set<String>>>(){});
				
				List<ClassTeaching> classTeachingList=SUtils.dt(classTeachingRemoteService.findByClassIdsSubjectIds(unitId,semester.getAcadyear(),semester.getSemester()+"",
						classIds.toArray(new String[0]),new String[]{subjectId},false), new TR<List<ClassTeaching>>(){});//取单科行政班
				if(CollectionUtils.isNotEmpty(classTeachingList)){
					Map<String,List<Student>> studentListMap=new HashMap<>();
					List<Clazz> xzbList=new ArrayList<>();
					Set<String> classIdSets = EntityUtils.getSet(classTeachingList, ClassTeaching::getClassId);
					List<Clazz> clsList3 = SUtils.dt(classRemoteService.findClassListByIds(classIdSets.toArray(new String[0])), new TR<List<Clazz>>() {});
					List<Student> studentList=SUtils.dt(studentRemoteService.findByClassIds(clsList3.stream().map(Clazz::getId).collect(Collectors.toSet())
							.toArray(new String[]{})),new TR<List<Student>>(){});
					//行政班对应的学生map
					if(CollectionUtils.isNotEmpty(studentList)){
						for(Student stu:studentList){
							if(!studentListMap.containsKey(stu.getClassId())){
								studentListMap.put(stu.getClassId(), new ArrayList<>());
							}
							studentListMap.get(stu.getClassId()).add(stu);
						}
					}
					
					for(Clazz clazz:clsList3){
						if(studentListMap.containsKey(clazz.getId())){
							Set<String> stuIds=studentListMap.get(clazz.getId()).stream().map(Student::getId).collect(Collectors.toSet());
							Set<String> xuankaoStuIds=xuankaoStuIdMap.get(subjectId);
							if(xuankaoStuIds==null) xuankaoStuIds=new HashSet<>();
							stuIds.retainAll(xuankaoStuIds);//此时是两交集 
							if(stuIds.size()==0&&!(Arrays.binarySearch(BaseConstants.SUBJECT_TYPES_YSY,subject.getSubjectCode()) >= 0)){
								continue;
							}
							xzbList.add(clazz);
						}
					}
					
					if(CollectionUtils.isNotEmpty(xzbList)){
						for (Clazz clazz : xzbList) {
							if(MapUtils.isNotEmpty(teacherAsessSetMap)&&teacherAsessSetMap.containsKey(clazz.getId()+"_"+"1")){
								TeacherAsessSet teacherAsessOld=teacherAsessSetMap.get(clazz.getId()+"_"+"1");
								TeacherAsessSet teacherAsnew=new TeacherAsessSet();
								teacherAsnew.setAssessId(teacherAsessId);
								teacherAsnew.setId(teacherAsessOld.getId());
								teacherAsnew.setUnitId(unitId);
								teacherAsnew.setSlice(teacherAsessOld.getSlice());
								teacherAsnew.setClassType("1");//行政班
								teacherAsnew.setClassId(clazz.getId());
								teacherAsnew.setClassName(clazz.getClassNameDynamic());
								teacherAsessSets.add(teacherAsnew);
							}else{
								TeacherAsessSet teacherAsessSet=new TeacherAsessSet();
								teacherAsessSet.setAssessId(teacherAsessId);
								teacherAsessSet.setClassType("1");//行政班
								teacherAsessSet.setClassId(clazz.getId());
								teacherAsessSet.setClassName(clazz.getClassNameDynamic());
								teacherAsessSets.add(teacherAsessSet);
							}
						}
					}
				}
				
				
				List<TeachClass> teaClaList = SUtils.dt(teachClassRemoteService.findTeachClassList(unitId,semester.getAcadyear(),semester.getSemester()+"",
						subjectId,new String[]{teacherAsess.getGradeId()},true), TeachClass.class);//取单科教学班
				if(CollectionUtils.isNotEmpty(teaClaList)){
					Map<String,List<TeachClassStu>> teachStuListMap=new HashMap<>();
					List<TeachClass> jxbList=new ArrayList<>();
					List<TeachClassStu> teachStuList=SUtils.dt(teachClassStuRemoteService.findByClassIds(teaClaList.stream().map(TeachClass::getId).collect(Collectors.toSet())
							.toArray(new String[]{})),new TR<List<TeachClassStu>>(){});
					//教学班对应的学生map
					if(CollectionUtils.isNotEmpty(teachStuList)){
						for(TeachClassStu teachStu:teachStuList){
							if(!teachStuListMap.containsKey(teachStu.getClassId())){
								teachStuListMap.put(teachStu.getClassId(), new ArrayList<>());
							}
							teachStuListMap.get(teachStu.getClassId()).add(teachStu);
						}
					}
					for (TeachClass teachClass : teaClaList) {
						if(teachStuListMap.containsKey(teachClass.getId())){
							Set<String> stuIds=teachStuListMap.get(teachClass.getId()).stream().map(TeachClassStu::getStudentId).collect(Collectors.toSet());
							Set<String> xuankaoStuIds=xuankaoStuIdMap.get(subjectId);
							if(xuankaoStuIds==null) xuankaoStuIds=new HashSet<>();
							stuIds.retainAll(xuankaoStuIds);
							if(stuIds.size()==0&&!(Arrays.binarySearch(BaseConstants.SUBJECT_TYPES_YSY,subject.getSubjectCode()) >= 0)){
								continue;
							}
							jxbList.add(teachClass);
						}
					}
					
					if(CollectionUtils.isNotEmpty(jxbList)){
						for (TeachClass teachClass : jxbList) {
							if(MapUtils.isNotEmpty(teacherAsessSetMap)&&teacherAsessSetMap.containsKey(teachClass.getId()+"_"+"2")){
								TeacherAsessSet teacherAsessOld=teacherAsessSetMap.get(teachClass.getId()+"_"+"2");
								TeacherAsessSet teacherAsnew=new TeacherAsessSet();
								teacherAsnew.setAssessId(teacherAsessId);
								teacherAsnew.setId(teacherAsessOld.getId());
								teacherAsnew.setUnitId(unitId);
								teacherAsnew.setSlice(teacherAsessOld.getSlice());
								teacherAsnew.setClassType("2");//行政班
								teacherAsnew.setClassId(teachClass.getId());
								teacherAsnew.setClassName(teachClass.getName());
								teacherAsessSets.add(teacherAsnew);
							}else{
								TeacherAsessSet teacherAsessSet=new TeacherAsessSet();
								teacherAsessSet.setAssessId(teacherAsessId);
								teacherAsessSet.setClassId(teachClass.getId());
								teacherAsessSet.setClassName(teachClass.getName());
								teacherAsessSet.setClassType("2");//教学班
								teacherAsessSets.add(teacherAsessSet);
							}
						}
					}
				}
				
			}else{//总分
				for (Clazz clazz : classList) {
					if(MapUtils.isNotEmpty(teacherAsessSetMap)&&teacherAsessSetMap.containsKey(clazz.getId()+"_"+"1")){
						TeacherAsessSet teacherAsessOld=teacherAsessSetMap.get(clazz.getId()+"_"+"1");
						TeacherAsessSet teacherAsnew=new TeacherAsessSet();
						teacherAsnew.setAssessId(teacherAsessId);
						teacherAsnew.setId(teacherAsessOld.getId());
						teacherAsnew.setUnitId(unitId);
						teacherAsnew.setSlice(teacherAsessOld.getSlice());
						teacherAsnew.setClassType("1");//行政班
						teacherAsnew.setClassId(clazz.getId());
						teacherAsnew.setClassName(clazz.getClassNameDynamic());
						teacherAsessSets.add(teacherAsnew);
					}else{
						TeacherAsessSet teacherAsessSet=new TeacherAsessSet();
						teacherAsessSet.setAssessId(teacherAsessId);
						teacherAsessSet.setClassType("1");//行政班
						teacherAsessSet.setClassId(clazz.getId());
						teacherAsessSet.setClassName(clazz.getClassNameDynamic());
						teacherAsessSets.add(teacherAsessSet);
					}
				}
			}
//			if(CollectionUtils.isNotEmpty(setOld)){
//				teacherAsessSets=setOld;
//			}else{
//			}
		}else{
			if(!StringUtils.equals(subjectId, BaseConstants.ZERO_GUID)){
				List<ClassTeaching> classTeachingList=SUtils.dt(classTeachingRemoteService.findByClassIdsSubjectIds(unitId,semester.getAcadyear(),semester.getSemester()+"",
						classIds.toArray(new String[0]),new String[]{subjectId},false), new TR<List<ClassTeaching>>(){});//取单科行政班
				if(CollectionUtils.isNotEmpty(classTeachingList)){
					Set<String> classIdSets = EntityUtils.getSet(classTeachingList, ClassTeaching::getClassId);
					List<Clazz> clsList3 = SUtils.dt(classRemoteService.findClassListByIds(classIdSets.toArray(new String[0])), new TR<List<Clazz>>() {});
					for (Clazz clazz : clsList3) {
						if(MapUtils.isNotEmpty(teacherAsessSetMap)&&teacherAsessSetMap.containsKey(clazz.getId()+"_"+"1")){
							TeacherAsessSet teacherAsessOld=teacherAsessSetMap.get(clazz.getId()+"_"+"1");
							TeacherAsessSet teacherAsnew=new TeacherAsessSet();
							teacherAsnew.setAssessId(teacherAsessId);
							teacherAsnew.setId(teacherAsessOld.getId());
							teacherAsnew.setUnitId(unitId);
							teacherAsnew.setSlice(teacherAsessOld.getSlice());
							teacherAsnew.setClassType("1");//行政班
							teacherAsnew.setClassId(clazz.getId());
							teacherAsnew.setClassName(clazz.getClassNameDynamic());
							teacherAsessSets.add(teacherAsnew);
						}else{
							TeacherAsessSet teacherAsessSet=new TeacherAsessSet();
							teacherAsessSet.setAssessId(teacherAsessId);
							teacherAsessSet.setClassType("1");//行政班
							teacherAsessSet.setClassId(clazz.getId());
							teacherAsessSet.setClassName(clazz.getClassNameDynamic());
							teacherAsessSets.add(teacherAsessSet);
						}
					}
				}
				List<TeachClass> teaClaList = SUtils.dt(teachClassRemoteService.findTeachClassList(unitId,semester.getAcadyear(),semester.getSemester()+"",
						subjectId,new String[]{teacherAsess.getGradeId()},true), TeachClass.class);//取单科教学班
				if(CollectionUtils.isNotEmpty(teaClaList)){
					for (TeachClass teachClass : teaClaList) {
						if(MapUtils.isNotEmpty(teacherAsessSetMap)&&teacherAsessSetMap.containsKey(teachClass.getId()+"_"+"2")){
							TeacherAsessSet teacherAsessOld=teacherAsessSetMap.get(teachClass.getId()+"_"+"2");
							TeacherAsessSet teacherAsnew=new TeacherAsessSet();
							teacherAsnew.setAssessId(teacherAsessId);
							teacherAsnew.setId(teacherAsessOld.getId());
							teacherAsnew.setUnitId(unitId);
							teacherAsnew.setSlice(teacherAsessOld.getSlice());
							teacherAsnew.setClassType("2");//教学班
							teacherAsnew.setClassId(teachClass.getId());
							teacherAsnew.setClassName(teachClass.getName());
							teacherAsessSets.add(teacherAsnew);
						}else{
							TeacherAsessSet teacherAsessSet=new TeacherAsessSet();
							teacherAsessSet.setAssessId(teacherAsessId);
							teacherAsessSet.setClassId(teachClass.getId());
							teacherAsessSet.setClassName(teachClass.getName());
							teacherAsessSet.setClassType("2");//教学班
							teacherAsessSets.add(teacherAsessSet);
						}
					}
				}
			}else{//总分
				for (Clazz clazz : classList) {
					if(MapUtils.isNotEmpty(teacherAsessSetMap)&&teacherAsessSetMap.containsKey(clazz.getId()+"_"+"1")){
						TeacherAsessSet teacherAsessOld=teacherAsessSetMap.get(clazz.getId()+"_"+"1");
						TeacherAsessSet teacherAsnew=new TeacherAsessSet();
						teacherAsnew.setAssessId(teacherAsessId);
						teacherAsnew.setId(teacherAsessOld.getId());
						teacherAsnew.setUnitId(unitId);
						teacherAsnew.setSlice(teacherAsessOld.getSlice());
						teacherAsnew.setClassType("1");//行政班
						teacherAsnew.setClassId(clazz.getId());
						teacherAsnew.setClassName(clazz.getClassNameDynamic());
						teacherAsessSets.add(teacherAsnew);
					}else{
						TeacherAsessSet teacherAsessSet=new TeacherAsessSet();
						teacherAsessSet.setAssessId(teacherAsessId);
						teacherAsessSet.setClassType("1");//行政班
						teacherAsessSet.setClassId(clazz.getId());
						teacherAsessSet.setClassName(clazz.getClassNameDynamic());
						teacherAsessSets.add(teacherAsessSet);
					}
				}
			} 
//			if(CollectionUtils.isNotEmpty(setOld)){
//				teacherAsessSets=setOld;
//			}else{
//			}
			
		}
		
		map.put("assessId", teacherAsessId);
		map.put("teacherAsess", teacherAsess);
		map.put("teacherAsessSets", teacherAsessSets);
		map.put("teacherAsessRanks", teacherAsessRanks);
		map.put("subjectId", subjectId);
		return url;
	}
	
	@ResponseBody
	@RequestMapping("/asessResult/asessResultChange/save")
    @ControllerInfo(value = "保存赋分等级设置")
	public String saveABRank(TeaSetDto dto,String assessId,String subjectId, HttpSession httpSession){
		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId=loginInfo.getUnitId();
		List<TeacherAsessSet> teaSetlist=new ArrayList<>();
		List<TeacherAsessRank> teaAsessRanks=new ArrayList<>();
		int countBalance = 0;
		int i=0;
		for(TeacherAsessRank e : dto.getTeaRankList()) {
			if(e!=null && e.getScale()>0) {
				countBalance += e.getScale();
				e.setUnitId(unitId);
				e.setSubjectId(subjectId);
				e.setAssessId(assessId);
				e.setName(ABS[i]);
				e.setId(StringUtils.isNotBlank(e.getId())?e.getId():UuidUtils.generateUuid());
				e.setRankOrder(i+1);
				teaAsessRanks.add(e);
				i++;
			}
		}
		for (TeacherAsessSet teacherAsessSet : dto.getTeaSetlist()) {
			teacherAsessSet.setAssessId(assessId);
			teacherAsessSet.setCreationTime(new Date());
			teacherAsessSet.setId(StringUtils.isNotBlank(teacherAsessSet.getId())?teacherAsessSet.getId():UuidUtils.generateUuid());
			teacherAsessSet.setSubjectId(subjectId);
			teacherAsessSet.setUnitId(unitId);
			teaSetlist.add(teacherAsessSet);
		}
		if(countBalance != 100) {
			return error("得分比例之和必须为100%,当前为"+countBalance+"%");
		}
		try{
			if(CollectionUtils.isNotEmpty(teaAsessRanks)&&CollectionUtils.isNotEmpty(teaSetlist)) {
				teacherasessSetService.saveAllEntity(unitId, assessId, subjectId, teaSetlist, teaAsessRanks);
			}else{
				return error("数据没有维护完全!");
			}
		}catch(Exception e){
			e.printStackTrace();
			returnError();
		}
		return success("保存成功！");		
	}
	
	@RequestMapping("/asessResult/tab/page")
    @ControllerInfo(value = "结果Tab页")
    public String asessTab(ModelMap map,String assessId) {
		map.put("assessId", assessId);
        return "/teacherasess/asessResult/asessResultTab.ftl";
    }
	
	@RequestMapping("/asessLine/index")
	@ControllerInfo(value = "结果Tab页")
	public String asessLineIndex(ModelMap map,String assessId, HttpSession httpSession) {
		String unitId = getLoginInfo(httpSession).getUnitId();
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73YSY(unitId), new TR<List<Course>>() {});
		//List<Course> courses=teacherasessResultService.findByUnitIdAndAsessId(unitId, teacherAsessId);
		Course allCourse = new Course();
		allCourse.setId(BaseConstants.ZERO_GUID);
		allCourse.setSubjectName("总分");
		courseList.add(allCourse);
		
		map.put("teacherAsessId", assessId);
		map.put("courses", courseList);
		map.put("subjectId", CollectionUtils.isNotEmpty(courseList)?courseList.get(0).getId():null);
		return "/teacherasess/asessResult/asessLineIndex.ftl";
	}
	
	@RequestMapping("/asessLine/list")
	@ControllerInfo(value = "结果Tab页")
	public String asessLineList(ModelMap map,String assessId,String subjectId, HttpSession httpSession) {
		String unitId = getLoginInfo(httpSession).getUnitId();
		List<ClassDto> classDtos=teacherasessLineService.getClassDtoByUnitIdAndAsessIdAndSubjectId(unitId, assessId, subjectId);
		List<RankDto> rankDtos=teacherasessRankService.findRankDtoByUnitIdAndAsessIdAndSubjectId(unitId, assessId, subjectId);
		Map<String, TeacherAsessLine> teaLineMap=teacherasessLineService.getTeacherAsessLineByUnitIdAndAssessIdAndSubjectId(unitId, assessId, subjectId);
		
		List<String> convertTypes=new ArrayList<>();
		convertTypes.add("1");//本次
		convertTypes.add("2");//参照
		
		map.put("classDtos", classDtos);
		map.put("rankDtos", rankDtos);
		map.put("convertTypes", convertTypes);
		map.put("teaLineMap", teaLineMap);
		
		return "/teacherasess/asessResult/asessLineList.ftl";
	}
	
	@RequestMapping("/asessCheck/index")
	@ControllerInfo(value = "结果Tab页")
	public String asessCheckIndex(ModelMap map,String assessId, HttpSession httpSession) {
		String unitId = getLoginInfo(httpSession).getUnitId();
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73YSY(unitId), new TR<List<Course>>() {});
		//List<Course> courses=teacherasessResultService.findByUnitIdAndAsessId(unitId, teacherAsessId);
		Course allCourse = new Course();
		allCourse.setId(BaseConstants.ZERO_GUID);
		allCourse.setSubjectName("总分");
		courseList.add(allCourse);
		
		map.put("teacherAsessId", assessId);
		map.put("courses", courseList);
		map.put("subjectId", CollectionUtils.isNotEmpty(courseList)?courseList.get(0).getId():null);
		return "/teacherasess/asessResult/asessCheckIndex.ftl";
	}
	
	@RequestMapping("/asessCheck/list")
	@ControllerInfo(value = "结果Tab页")
	public String asessCheckList(ModelMap map,String assessId,String subjectId, HttpSession httpSession) {
		String unitId = getLoginInfo(httpSession).getUnitId();
		List<ClassDto> classDtos=teacherasessLineService.getClassDtoByUnitIdAndAsessIdAndSubjectId(unitId, assessId, subjectId);
		List<RankDto> rankDtos=teacherasessRankService.findCheckRankDtoByUnitIdAndAsessIdAndSubjectId(unitId, assessId, subjectId);
		Map<String, TeacherAsessCheck> teaCheckMap=teacherasessCheckService.getTeacherAsessLineByUnitIdAndAssessIdAndSubjectId(unitId, assessId, subjectId);
		if(MapUtils.isNotEmpty(teaCheckMap)){
			for (Entry<String, TeacherAsessCheck> entry : teaCheckMap.entrySet()) {
				TeacherAsessCheck teacherAsessCheck=entry.getValue();
				if(teacherAsessCheck!=null){
					DecimalFormat decimalFormat=new DecimalFormat("##0.0");
					String pri=decimalFormat.format(teacherAsessCheck.getLineScore()); 
					teacherAsessCheck.setLineScoreStr(pri);
				}
			}
		}
		List<String> convertTypes=new ArrayList<>();
		convertTypes.add("1");//得分
		convertTypes.add("2");//排名
		
		map.put("classDtos", classDtos);
		map.put("rankDtos", rankDtos);
		//map.put("convertTypes", convertTypes);
		map.put("teaCheckMap", teaCheckMap);
		
		return "/teacherasess/asessResult/asessCheckList.ftl";
	}
	
	@RequestMapping("/asessLine/export")
	public void doLineExport(String assessId, HttpSession httpSession,HttpServletResponse response){
		String unitId = getLoginInfo(httpSession).getUnitId();
		List<Course> courseAll=new ArrayList<>();
		Course allCourse = new Course();
		allCourse.setId(BaseConstants.ZERO_GUID);
		allCourse.setSubjectName("总分");
		courseAll.add(allCourse);
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73YSY(unitId), new TR<List<Course>>() {});
		courseAll.addAll(courseList);
		
		HSSFWorkbook workbook = new HSSFWorkbook();	
		for (Course course : courseAll) {
			HSSFSheet sheet = workbook.createSheet(course.getSubjectName());
	        sheet.setColumnWidth(0, 2000);
	        sheet.setColumnWidth(1, 2800);
	        sheet.setColumnWidth(2, 2800);
	        sheet.setColumnWidth(3, 2800);
	        sheet.setColumnWidth(4, 2800);
	        sheet.setColumnWidth(5, 2800);
	        sheet.setColumnWidth(6, 2800);
	        sheet.setColumnWidth(7, 2800);
	        
	        HSSFCellStyle style = workbook.createCellStyle();
			HSSFFont headfont = workbook.createFont();
			headfont.setFontHeightInPoints((short) 10);// 字体大小
			style.setFont(headfont);
			style.setBorderBottom(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			style.setWrapText(true);
			
			//第一行
			HSSFRow row1 = sheet.createRow(0);
			CellRangeAddress car11 = new CellRangeAddress(0,1,0,0);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car11, sheet); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car11, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car11, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car11, sheet); 
			sheet.addMergedRegion(car11);
			HSSFCell cell11 = row1.createCell(0);
			cell11.setCellStyle(style);
			cell11.setCellValue(new HSSFRichTextString("班级"));
			
			//第2行
			HSSFRow row2 = sheet.createRow(1);
			
			List<ClassDto> classDtos=teacherasessLineService.getClassDtoByUnitIdAndAsessIdAndSubjectId(unitId, assessId, course.getId());
			List<RankDto> rankDtos=teacherasessRankService.findRankDtoByUnitIdAndAsessIdAndSubjectId(unitId, assessId, course.getId());
			Map<String, TeacherAsessLine> teaLineMap=teacherasessLineService.getTeacherAsessLineByUnitIdAndAssessIdAndSubjectId(unitId, assessId, course.getId());
			
			List<String> convertTypes=new ArrayList<>();
			convertTypes.add("1");//本次
			convertTypes.add("2");//参照
			
			int i=1;
			int j=0;
			int k=1;
			for (RankDto rankDto : rankDtos) {
				CellRangeAddress car12 = new CellRangeAddress(0,0,k,k+1);
			    RegionUtil.setBorderBottom(BorderStyle.THIN, car12, sheet); 
			    RegionUtil.setBorderLeft(BorderStyle.THIN, car12, sheet); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car12, sheet); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car12, sheet); 
			    sheet.addMergedRegion(car12);
				HSSFCell cell12 = row1.createCell(k);
				cell12.setCellStyle(style);
				cell12.setCellValue(new HSSFRichTextString(rankDto.getName()));
				
				for (String string : convertTypes) {
					HSSFCell cell21 = row2.createCell(j+1);
					cell21.setCellStyle(style);
					if(StringUtils.equals(string, "1")){
						cell21.setCellValue(new HSSFRichTextString("本次"));
					}else{
						cell21.setCellValue(new HSSFRichTextString("参照"));
					}
					j++;
				}
				k=k+2;
				i++;
			}
			
			int n=2;
			for (ClassDto classDto : classDtos) {
				int m=1;
				HSSFRow row3 = sheet.createRow(n);
				HSSFCell cell31 = row3.createCell(0);
				cell31.setCellStyle(style);
				cell31.setCellValue(new HSSFRichTextString(classDto.getClassName()));
				for (RankDto rankDto : rankDtos) {
					for (String string : convertTypes) {
						HSSFCell cell32 = row3.createCell(m);
						cell32.setCellStyle(style);
						if(teaLineMap.containsKey(classDto.getClassId()+"_"+rankDto.getAsessRankId()+"_"+rankDto.getLineType()+"_"+string+"_"+rankDto.getSlice())){
							cell32.setCellValue(new HSSFRichTextString(teaLineMap.get(classDto.getClassId()+"_"+rankDto.getAsessRankId()+"_"+rankDto.getLineType()+"_"+string+"_"+rankDto.getSlice()).getLineNum()+""));
						}else{
							cell32.setCellValue(new HSSFRichTextString(""));
						}
						m++;
					}
				}
				n++;
			}
			
		}
		
		ExportUtils.outputData(workbook, "单双上线导出", response);
		
	}
	
	@RequestMapping("/asessCheck/export")
	public void doCheckExport(String assessId, HttpSession httpSession,HttpServletResponse response){
		String unitId = getLoginInfo(httpSession).getUnitId();
		List<Course> courseAll=new ArrayList<>();
		Course allCourse = new Course();
		allCourse.setId(BaseConstants.ZERO_GUID);
		allCourse.setSubjectName("总分");
		courseAll.add(allCourse);
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73YSY(unitId), new TR<List<Course>>() {});
		courseAll.addAll(courseList);
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("名次线导出");
        sheet.setColumnWidth(0, 2000);
        sheet.setColumnWidth(1, 2800);
        sheet.setColumnWidth(2, 2800);
        sheet.setColumnWidth(3, 2800);
        sheet.setColumnWidth(4, 2800);
        sheet.setColumnWidth(5, 2800);
        sheet.setColumnWidth(6, 2800);
        sheet.setColumnWidth(7, 2800);
        
        HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont headfont = workbook.createFont();
		headfont.setFontHeightInPoints((short) 10);// 字体大小
		style.setFont(headfont);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setWrapText(true);
		
		//第一行
		HSSFRow row1 = sheet.createRow(0);
		int indexRow=0;
		int maxClass=0;
		Map<String,List<RankDto>> rankMap=new HashMap<>();
		Map<String,List<ClassDto>> classMap=new HashMap<>();
		Map<String,Map<String, TeacherAsessCheck>> checkMap=new HashMap<>();
		for (Course course : courseAll) {
			List<RankDto> rankDtos=teacherasessRankService.findCheckRankDtoByUnitIdAndAsessIdAndSubjectId(unitId, assessId, course.getId());
			List<ClassDto> classDtos=teacherasessLineService.getClassDtoByUnitIdAndAsessIdAndSubjectId(unitId, assessId, course.getId());
			if(maxClass<classDtos.size()){
				maxClass=classDtos.size();
			}
			if(CollectionUtils.isEmpty(rankDtos)){
				continue;
			}
			Map<String, TeacherAsessCheck> teaCheckMap=teacherasessCheckService.getTeacherAsessLineByUnitIdAndAssessIdAndSubjectId(unitId, assessId, course.getId());
			rankMap.put(course.getId(), rankDtos);
			classMap.put(course.getId(), classDtos);
			checkMap.put(course.getId(), teaCheckMap);
			
			CellRangeAddress car11 = new CellRangeAddress(0,0,indexRow,indexRow+rankDtos.size());
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car11, sheet); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car11, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car11, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car11, sheet); 
			sheet.addMergedRegion(car11);
			HSSFCell cell11 = row1.createCell(indexRow);
			cell11.setCellStyle(style);
			cell11.setCellValue(new HSSFRichTextString(course.getSubjectName()));
			indexRow = indexRow+rankDtos.size()+1;
		}
		
		//第二行
		HSSFRow row2 = sheet.createRow(1);
		int indexRow2=0;
		int i=0;
		for (Course course : courseAll) {
			List<RankDto> rankDtos=teacherasessRankService.findCheckRankDtoByUnitIdAndAsessIdAndSubjectId(unitId, assessId, course.getId());
			if(CollectionUtils.isEmpty(rankDtos)){
				continue;
			}
			
			for (RankDto rankDto : rankDtos) {
				if(i==indexRow2){
					HSSFCell cell21 = row2.createCell(i);
					cell21.setCellStyle(style);
					cell21.setCellValue(new HSSFRichTextString("班级"));
					i++;
				}
				HSSFCell cell21 = row2.createCell(i);
				cell21.setCellStyle(style);
				cell21.setCellValue(new HSSFRichTextString(rankDto.getName()));
				i++;
			}
			
			indexRow2 = indexRow2+rankDtos.size()+1;
		}
		
		//第三行之后//rankMap,classMap,checkMap
		List<Course> desCourse=new ArrayList<>();
		for (Course course : courseAll) {
			List<RankDto> rankDtos=rankMap.get(course.getId());
			rankDtos=rankMap.get(course.getId());
			if(CollectionUtils.isNotEmpty(rankDtos)){
				desCourse.add(course);
			}
		}
		
		String subjectId=desCourse.get(0).getId();
		List<ClassDto> classDtos=classMap.get(subjectId);
		int k=2;
		for (ClassDto classDto : classDtos) {
			int j=1;
			HSSFRow rowk = sheet.createRow(k);
			HSSFCell cellk1 = rowk.createCell(0);
			cellk1.setCellStyle(style);
			cellk1.setCellValue(new HSSFRichTextString(classDto.getClassName()));
			
			for (Course rowCour : desCourse){
				Map<String,TeacherAsessCheck> teaCheckMap=new HashMap<>();
				teaCheckMap=checkMap.get(rowCour.getId());
				List<RankDto> rankDtos=rankMap.get(rowCour.getId());
				List<ClassDto> classdes=classMap.get(rowCour.getId());
				if(!StringUtils.equals(subjectId, rowCour.getId())){
					ClassDto classDto2=null;
					if(CollectionUtils.isNotEmpty(classdes)){
						classDto2=classdes.get(0);
						HSSFCell cellk3 = rowk.createCell(j);
						cellk3.setCellStyle(style);
						cellk3.setCellValue(new HSSFRichTextString(classDto2.getClassName()));
						j++;
					}else{
						HSSFCell cellk3 = rowk.createCell(j);
						cellk3.setCellStyle(style);
						cellk3.setCellValue(new HSSFRichTextString(""));
						j++;
					}
					for (RankDto rankDto : rankDtos) {
						if(CollectionUtils.isNotEmpty(classdes)){
							if(teaCheckMap.containsKey(classDto2.getClassId()+"_"+rankDto.getAsessRankId())){
								TeacherAsessCheck teacherAsessCheck=teaCheckMap.get(classDto2.getClassId()+"_"+rankDto.getAsessRankId());
								HSSFCell cellk2 = rowk.createCell(j);
								cellk2.setCellStyle(style);
								if(StringUtils.equals(rankDto.getLineType(),"1")){
									DecimalFormat decimalFormat=new DecimalFormat("##0.0");
									String pri=decimalFormat.format(teacherAsessCheck.getLineScore()); 
									cellk2.setCellValue(new HSSFRichTextString(pri));
								}else{
									cellk2.setCellValue(new HSSFRichTextString(teacherAsessCheck.getLineRank()+""));
								}
							}else{
								HSSFCell cellk2 = rowk.createCell(j);
								cellk2.setCellStyle(style);
								cellk2.setCellValue(new HSSFRichTextString(""));
							}
						}else{
							HSSFCell cellk2 = rowk.createCell(j);
							cellk2.setCellStyle(style);
							cellk2.setCellValue(new HSSFRichTextString(""));
						}
						j++;
					}
					if(CollectionUtils.isNotEmpty(classdes)){
						classdes.remove(classDto2);
					}
				}else{
					for (RankDto rankDto : rankDtos) {
						if(teaCheckMap.containsKey(classDto.getClassId()+"_"+rankDto.getAsessRankId())){
							TeacherAsessCheck teacherAsessCheck=teaCheckMap.get(classDto.getClassId()+"_"+rankDto.getAsessRankId());
							HSSFCell cellk2 = rowk.createCell(j);
							cellk2.setCellStyle(style);
							if(StringUtils.equals(rankDto.getLineType(),"1")){
								DecimalFormat decimalFormat=new DecimalFormat("##0.0");
								String pri=decimalFormat.format(teacherAsessCheck.getLineScore()); 
								cellk2.setCellValue(new HSSFRichTextString(pri));
							}else{
								cellk2.setCellValue(new HSSFRichTextString(teacherAsessCheck.getLineRank()+""));
							}
						}else{
							HSSFCell cellk2 = rowk.createCell(j);
							cellk2.setCellStyle(style);
							cellk2.setCellValue(new HSSFRichTextString(""));
						}
						j++;
					}
				}
			} 
			k++;
			
		}
		
		if(maxClass>classDtos.size()){
			for (int o = 0; o < maxClass-classDtos.size(); o++) {
				int j=1;
				HSSFRow rowk = sheet.createRow(k);
				HSSFCell cellk1 = rowk.createCell(0);
				cellk1.setCellStyle(style);
				cellk1.setCellValue(new HSSFRichTextString(""));
				for (Course rowCour : desCourse){
					Map<String,TeacherAsessCheck> teaCheckMap=new HashMap<>();
					teaCheckMap=checkMap.get(rowCour.getId());
					List<RankDto> rankDtos=rankMap.get(rowCour.getId());
					List<ClassDto> classdes=classMap.get(rowCour.getId());
					if(!StringUtils.equals(subjectId, rowCour.getId())){
						ClassDto classDto2=null;
						if(CollectionUtils.isNotEmpty(classdes)){
							classDto2=classdes.get(0);
							HSSFCell cellk3 = rowk.createCell(j);
							cellk3.setCellStyle(style);
							cellk3.setCellValue(new HSSFRichTextString(classDto2.getClassName()));
							j++;
						}else{
							HSSFCell cellk3 = rowk.createCell(j);
							cellk3.setCellStyle(style);
							cellk3.setCellValue(new HSSFRichTextString(""));
							j++;
						}
						for (RankDto rankDto : rankDtos) {
							if(CollectionUtils.isNotEmpty(classdes)){
								if(teaCheckMap.containsKey(classDto2.getClassId()+"_"+rankDto.getAsessRankId())){
									TeacherAsessCheck teacherAsessCheck=teaCheckMap.get(classDto2.getClassId()+"_"+rankDto.getAsessRankId());
									HSSFCell cellk2 = rowk.createCell(j);
									cellk2.setCellStyle(style);
									if(StringUtils.equals(rankDto.getLineType(),"1")){
										DecimalFormat decimalFormat=new DecimalFormat("##0.0");
										String pri=decimalFormat.format(teacherAsessCheck.getLineScore()); 
										cellk2.setCellValue(new HSSFRichTextString(pri));
									}else{
										cellk2.setCellValue(new HSSFRichTextString(teacherAsessCheck.getLineRank()+""));
									}
								}else{
									HSSFCell cellk2 = rowk.createCell(j);
									cellk2.setCellStyle(style);
									cellk2.setCellValue(new HSSFRichTextString(""));
								}
							}else{
								HSSFCell cellk2 = rowk.createCell(j);
								cellk2.setCellStyle(style);
								cellk2.setCellValue(new HSSFRichTextString(""));
							}
							j++;
						}
						if(CollectionUtils.isNotEmpty(classdes)){
							classdes.remove(classDto2);
						}
					}else{
						for (RankDto rankDto : rankDtos) {
							HSSFCell cellk2 = rowk.createCell(j);
							cellk2.setCellStyle(style);
							cellk2.setCellValue(new HSSFRichTextString(""));
							j++;
						}
					}
				} 
				k++;
			}
		}
		
		ExportUtils.outputData(workbook, "名次线导出", response);
	}
	
	// 导出
	@ResponseBody
	@RequestMapping("/asessResult/asessResult/export")
	public void export(final String teacherAsessId, HttpServletRequest request,HttpSession httpSession,HttpServletResponse resp) {
		try {
			String subjectId=request.getParameter("subjectId");
			String resultType=request.getParameter("resultType");
			String classType=request.getParameter("classType");
			String unitId = getLoginInfo(httpSession).getUnitId();
			
			List<TeacherAsessResult> teacherAsessResults=null;
	        if("1".equals(classType)){//行政班考核
	        	teacherAsessResults=teacherasessResultService.findByUnitIdAndAsessIdAndClassTypeAndSubjectId(unitId, teacherAsessId, "1", "00000000000000000000000000000000");
	        }else{//教学班考核
	        	teacherAsessResults=teacherasessResultService.findByUnitIdAndAsessIdAndSubjectId(unitId, teacherAsessId, subjectId);
	        }
			paixu(teacherAsessResults);
			Map<String,List<TeacherAsessResult>> records = Maps.newHashMap();
			
			StringBuilder title = new StringBuilder(""); 
			List<String> propertyNames=new ArrayList<>();
			List<String> fieldTitles=new ArrayList<>();
			if("1".equals(classType)){
				fieldTitles.add("班级");
				fieldTitles.add("班主任");
				title.append("行政班考核结果-");
			}else{
				fieldTitles.add("教学班");
				fieldTitles.add("任课老师");
				title.append("教学班考核结果-");
				Course course=SUtils.dc(courseRemoteService.findOneById(subjectId),Course.class);
				title.append(course.getSubjectName()+"-");
			}
			propertyNames.add("className");
			propertyNames.add("teacherName");
			if(("1").equals(resultType)){
				fieldTitles.add("本次考核方案考核分系数");
				fieldTitles.add("原始参照方案考核分系数");
				fieldTitles.add("考核分");
				fieldTitles.add("名次");
				propertyNames.add("convertParam");
				propertyNames.add("referConvertParam");
				propertyNames.add("asessScore");
				propertyNames.add("rank");
				title.append("总名次排名表");
			}else if("2".equals(resultType)){
				fieldTitles.add("班级本次对比原始参照的进步人数");
				fieldTitles.add("进步率");
				fieldTitles.add("进步率排名");
				propertyNames.add("upStuNum");
				propertyNames.add("upScale");
				propertyNames.add("upScaleRank");
				title.append("进步人数排名表");
			}else{
				fieldTitles.add("总名次排名");
				fieldTitles.add("进步人次排名");
				fieldTitles.add("最终考核分");
				fieldTitles.add("最终考核名次");
				propertyNames.add("rank");
				propertyNames.add("upScaleRank");
				propertyNames.add("score");
				propertyNames.add("scoreRank");
				title.append("最终考核排名表");
			}
			
			records.put(title.toString(), teacherAsessResults);
			ExportUtils.newInstance().exportXLSFile(fieldTitles.toArray(new String[]{}), propertyNames.toArray(new String[]{}),
					records, title.toString(), title.toString(), resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
