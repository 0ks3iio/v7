package net.zdsoft.scoremanage.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dto.ExamInfoSearchDto;
import net.zdsoft.scoremanage.data.dto.SubjectInfoExport;
import net.zdsoft.scoremanage.data.dto.SubjectInfoSaveDto;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.JoinexamschInfoService;
import net.zdsoft.scoremanage.data.service.ScoreInfoService;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

@Controller
@RequestMapping("/scoremanage")
public class SubjectInfoAction extends BaseAction{
	private static final Logger logger = LoggerFactory
			.getLogger(SubjectInfoAction.class);
	
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private UnitRemoteService unitService;
	@Autowired
	private SchoolRemoteService  schoolService;
	@Autowired
	private ClassRemoteService classService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private SubjectInfoService subjectInfoService;
	@Autowired
	private CourseRemoteService courseService;
	@Autowired
	private TeachClassRemoteService teachClassService;
	@Autowired
	private ScoreInfoService scoreInfoService;
	@Autowired
	private SemesterRemoteService semesterService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private JoinexamschInfoService joinexamschInfoService;
	
	@RequestMapping("/courseInfo/index/page")
    @ControllerInfo(value = "考试科目设置头部查询", operationName="进入【考试科目】模块")
    public String showIndex(ModelMap map, HttpSession httpSession) {
		List<String> acadyearList = SUtils.dt(semesterService.findAcadeyearList(), new TR<List<String>>(){}); 
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		} 
        LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId=loginInfo.getUnitId();
		map.put("unitId", unitId);
		Semester semester = SUtils.dc(semesterService.getCurrentSemester(1,unitId), Semester.class);
		String acadyearSearch = semester.getAcadyear();
		String semesterSearch=semester.getSemester()+"";
		Unit u = SUtils.dc(unitService.findOneById(unitId), Unit.class);
		map.put("unitClass", u.getUnitClass());
		map.put("acadyearList", acadyearList);
		map.put("acadyearSearch", acadyearSearch);
		map.put("semesterSearch", semesterSearch);
        return "/scoremanage/courseInfo/courseInfoIndex.ftl";
	}
	
	@RequestMapping("/courseInfo/list/page")
	@ControllerInfo(ignoreLog=1, value = "考试科目设置showList")
	public String showList(String examId,String gradeCode,ModelMap map){
		if(StringUtils.isBlank(examId) || StringUtils.isBlank(gradeCode)){
			return errorFtl(map, "参数丢失，请重新刷新");
		}
		boolean isCanEditSubject=false;
		boolean isCanEditClass=false;
		int section = NumberUtils.toInt(gradeCode.substring(0,1));
		String unitId=getLoginInfo().getUnitId();
		Unit unit = SUtils.dc(unitService.findOneById(unitId), Unit.class);
		//根据考试 年级code 拿到已有的科目信息
		ExamInfo examInfo = examInfoService.findOne(examId);
		if(examInfo!=null){
			if(examInfo.getUnitId().equals(unitId)){
				//当前单位设置
				isCanEditSubject=true;
			}
			//是否参与
			if(unit.getUnitClass()==2){
				//学校
				isCanEditClass=true;
			}

			// 如果类型为会考，放开班级限制
			if (ScoreDataConstants.EXAM_TYPE_GRADUATE.equals(examInfo.getExamType())) {
				map.put("showAllClass", BaseConstants.ONE_STR);
			} else {
				map.put("showAllClass", BaseConstants.ZERO_STR);
			}
			
			//已有科目
			List<SubjectInfo> subjectInfoList = subjectInfoService.findByUnitIdExamId(unitId, examId,gradeCode);
			//科目列表
			List<Course> courseList=new ArrayList<Course>();
			Set<String> unitIds=new HashSet<String>();
			Unit djunit = SUtils.dc(unitService.findTopUnit(unitId), Unit.class);
			if(unit.getUnitClass()==Unit.UNIT_CLASS_SCHOOL && !ScoreDataConstants.TKLX_3.equals(examInfo.getExamUeType())){
				//不是校校联考  学校维护 取学校+顶级教育局
				unitIds.add(unitId);
	    		if(djunit!=null){
	    			unitIds.add(djunit.getId());
	    		}
			}else{
				//教育局开设 或者是校校联考 取顶级教育局
				if(djunit!=null){
	    			unitIds.add(djunit.getId());
	    		}
			}
			if(unitIds.size()>0){
				//直接针对性取学段科目，页面不做缓存
				courseList=SUtils.dt(courseService.findByUnitIdIn(unitIds.toArray(new String[]{}), new String[]{section+""}), new TR<List<Course>>(){});
			}
			map.put("courseList", courseList);
			map.put("subjectInfoList", subjectInfoList);
			map.put("isCanEditSubject", isCanEditSubject);
			map.put("isCanEditClass", isCanEditClass);
			map.put("examId", examId);
			map.put("unitId", unitId);
			map.put("gradeCode", gradeCode);
			return "/scoremanage/courseInfo/courseInfoList.ftl";
		}else{
			return errorFtl(map, "考试已经不存在，请重新选择");
		}
	}
	
	@ResponseBody
    @RequestMapping("/courseInfo/findClass")
    @ControllerInfo("考试科目设置-找对应班级")
    public String showFindClassBySubject(String acadyear,String semester,String subjectId,String gradeCode,String showAllClass) {
		String unitId=getLoginInfo().getUnitId();
		JSONArray jsonArr=new JSONArray();
		int section = NumberUtils.toInt(gradeCode.substring(0,1));
		int afterGradeCode = NumberUtils.toInt(gradeCode.substring(1,2));
		int beforeSelectAcadyear = NumberUtils.toInt(StringUtils.substringBefore(acadyear, "-"));
		String openAcadyear = (beforeSelectAcadyear-afterGradeCode+1)+"-"+(beforeSelectAcadyear-afterGradeCode+2);
		//班级开设课程 不走班的
		List<Clazz> classList;
		if (BaseConstants.ONE_STR.equals(showAllClass)) {
			Grade grade = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId, gradeCode), Grade.class).get(0);
			classList = SUtils.dt(classService.findBySchoolIdGradeId(unitId, grade.getId()), Clazz.class);
		} else {
			classList = SUtils.dt(classService.findClassList(unitId,subjectId,openAcadyear,acadyear, semester, section,0), new TR<List<Clazz>>(){});
		}
		JSONArray jsonArrLin=new JSONArray();
		JSONObject jsonObj=null;
		for (Clazz item : classList) {
			jsonObj=new JSONObject();
			jsonObj.put("id", item.getId());
			jsonObj.put("name", item.getClassNameDynamic());
			jsonArrLin.add(jsonObj);
		}
		jsonArr.add(jsonArrLin);
		jsonArrLin=new JSONArray();
		List<Grade> gradeList = makeGradeIdList(gradeCode, acadyear, unitId);
		Set<String> gradeIds = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(gradeList)){
			gradeIds=EntityUtils.getSet(gradeList, "id");
		}
		//TeachClass 年级id不可能为空
		List<TeachClass> teachClassList = new ArrayList<TeachClass>();
		if(gradeIds.size()>0){
			teachClassList = SUtils.dt(teachClassService.findTeachClassList(unitId,acadyear,semester,subjectId,gradeIds.toArray(new String[]{}),false), new TR<List<TeachClass>>(){});
		}
		for (TeachClass item : teachClassList) {
			jsonObj=new JSONObject();
			jsonObj.put("id", item.getId());
			jsonObj.put("name", item.getName());
			jsonArrLin.add(jsonObj);
		}
		jsonArr.add(jsonArrLin);
        return jsonArr.toJSONString();
    }
	
	private List<Grade>  makeGradeIdList(String gradeCode,String acadyear,String schoolId){
		int section = NumberUtils.toInt(gradeCode.substring(0,1));
		int afterGradeCode = NumberUtils.toInt(gradeCode.substring(1,2));
		int beforeSelectAcadyear = NumberUtils.toInt(StringUtils.substringBefore(acadyear, "-"));
		String openAcadyear = (beforeSelectAcadyear-afterGradeCode+1)+"-"+(beforeSelectAcadyear-afterGradeCode+2);//开设学年
		//理论上一个学校一个年级默认只有一个  这个gradeList只有一个
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchidSectionAcadyear(schoolId, openAcadyear,new Integer[]{section}), new TR<List<Grade>>(){}); 
		return gradeList;
	}
	@RequestMapping("/courseInfo/copyClassAdd/page")
	@ControllerInfo(value = "进入复用班级设置选择考试页面")
	public String showCopyClassAdd(String examId,String searchType,ModelMap map, HttpSession httpSession){
		List<ExamInfo> examInfoList = examInfoService.findNotDeletedByIdIn(examId);
		if(CollectionUtils.isEmpty(examInfoList)){
			return errorFtl(map,"考试已不存在！");
		}
		ExamInfo examInfo = examInfoList.get(0);
		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId=loginInfo.getUnitId();
		//必须同类型的
//		List<ExamInfo> examInfoLi = examInfoService.findExamInfoListRange(unitId,examInfo.getAcadyear(),examInfo.getSemester(),searchType);
		// 同学年学期的所有考试
		ExamInfoSearchDto searchDto=new ExamInfoSearchDto();
		searchDto.setSearchAcadyear(examInfo.getAcadyear());
		searchDto.setSearchSemester(examInfo.getSemester());
		List<ExamInfo> examInfoLi = examInfoService.findExamInfoList(unitId,searchDto,null);
		List<ExamInfo> finList = new ArrayList<ExamInfo>();
		for (ExamInfo item : examInfoLi) {
			if(!examId.equals(item.getId()))
				finList.add(item);
		}
		map.put("examInfoList", finList);
		map.put("examId", examId);
		return "/scoremanage/courseInfo/courseInfoCopyClassAdd.ftl"; 
	}
	
	@RequestMapping("/courseInfo/copyAdd/page")
	@ControllerInfo(operationName="复用考试设置", value = "进入复用选择考试页面")
	public String showCopyAdd(String examId,ModelMap map, HttpSession httpSession){
		List<ExamInfo> examInfoList = examInfoService.findNotDeletedByIdIn(examId);
		if(CollectionUtils.isEmpty(examInfoList)){
			return errorFtl(map,"考试已不存在！");
		}
		ExamInfo examInfo = examInfoList.get(0);
		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId=loginInfo.getUnitId();
		if(!examInfo.getUnitId().equals(unitId)){
			return errorFtl(map,"无权限操作！");
		}
		List<ExamInfo> examInfoLi = examInfoService.findExamInfoListRange(unitId,examInfo.getAcadyear(),examInfo.getSemester());
		List<ExamInfo> finList = new ArrayList<ExamInfo>();
		for (ExamInfo item : examInfoLi) {
			if(!examId.equals(item.getId()))
				finList.add(item);
		}
		map.put("examInfoList", finList);
		map.put("examId", examId);
		return "/scoremanage/courseInfo/courseInfoCopyAdd.ftl"; 
	}
	

	@ResponseBody
    @RequestMapping("/courseInfo/list/save")
    @ControllerInfo(operationName="保存考试科目", value = "保存考试科目")
    public String doSaveCourseInfoList(SubjectInfoSaveDto subjectInfoSaveDto, HttpSession httpSession) {
		try{
			LoginInfo loginInfo = getLoginInfo(httpSession);
			String unitId=loginInfo.getUnitId();
			List<ExamInfo> examInfoList = examInfoService.findNotDeletedByIdIn(subjectInfoSaveDto.getExamInfoId());
			if(CollectionUtils.isEmpty(examInfoList)){
				return error("考试已不存在！");
			}
			ExamInfo examInfo=examInfoList.get(0);
			List<SubjectInfo> subjectInfoList = subjectInfoSaveDto.getSubjectInfoList();
			if(CollectionUtils.isEmpty(subjectInfoList)){
				return error("没有需要保存的数据！");
			}
			//判断权限
			boolean isCanEditSubject=false;
			boolean isCanEditClass=false;
			Unit unit = SUtils.dc(unitService.findOneById(unitId), Unit.class);
			if(examInfo.getUnitId().equals(unitId)){
				//当前单位设置
				isCanEditSubject=true;
			}
			//是否参与
			if(unit.getUnitClass()==2){
				//学校
				isCanEditClass=true;
			}
			if((!isCanEditSubject) && (!isCanEditClass)){
				//都没有权限
				return error("没有权限保存数据！");
			}
			List<SubjectInfo> addSubjectInfo = new ArrayList<SubjectInfo>();
			//不判断科目维护是否有权限问题 (页面先存在  后来开设单位取消该科目 这个暂时先不处理)
			if(isCanEditSubject){
				for (SubjectInfo item : subjectInfoList) {
					if(StringUtils.isNotBlank(item.getSubjectId())){
						if(ScoreDataConstants.ACHI_SCORE.equals(item.getInputType())){
							item.setGradeType(null);
						}
						addSubjectInfo.add(item);
					}
				}
			}else{
				//没有权限 则只能从数据库取回原来设置   只用到原来的考试科目设置的id 
				//如果该考试科目对应的subjectId私下调整 但是由于页面没有改动导致班级与科目对不上号--暂时不考虑
				Set<String> subjectInfoId=new HashSet<String>();
				Map<String,SubjectInfo> newSubjectInfo=new HashMap<String,SubjectInfo>();
				for (SubjectInfo item : subjectInfoList) {
					if(StringUtils.isNotBlank(item.getId())){
						subjectInfoId.add(item.getId());
						newSubjectInfo.put(item.getId(), item);
					}else{
						//不存在  这个正常页面维护可能 原因是没有权限新增
					}
				}
				if(subjectInfoId.size()>0){
					List<SubjectInfo> oldsubjectInfoList = subjectInfoService.findListByIds(subjectInfoId.toArray(new String[]{}));
					if(CollectionUtils.isEmpty(oldsubjectInfoList)){
						return error("考试科目数据有调整，请重新刷新后操作！");
					}
					for(SubjectInfo info:oldsubjectInfoList){
						info.setClassIds(newSubjectInfo.get(info.getId()).getClassIds());
						info.setTeachClassIds(newSubjectInfo.get(info.getId()).getTeachClassIds());
						addSubjectInfo.add(info);
					}
				}else{
					return error("没有需要保存的数据！");
				}
			}
			
			if(CollectionUtils.isNotEmpty(addSubjectInfo)){
				subjectInfoService.saveCourseInfoAll(examInfo,unitId, addSubjectInfo,isCanEditSubject,isCanEditClass);
			}else{
				return error("没有需要保存的数据！");
			}
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();
    }
	
	@ResponseBody
	@RequestMapping("/courseInfo/delete")
	@ControllerInfo("删除考试科目")
	public String doDeleteCourseInfo(String[] id,String examId, ModelMap map, HttpSession httpSession) {
		try{
			List<ExamInfo> examInfoList = examInfoService.findNotDeletedByIdIn(new String[]{examId});
			if(CollectionUtils.isEmpty(examInfoList)){
				return error("考试已不存在！");
			}
			LoginInfo loginInfo = getLoginInfo(httpSession);
			String unitId=loginInfo.getUnitId();
			if(!examInfoList.get(0).getUnitId().equals(unitId)){
				return error("无权限操作！");
			}
			List<ScoreInfo> findByExamId = scoreInfoService.findByExamId(examId);
			if(findByExamId.size()>0){
				return error("考试下已存在成绩，不能删除科目！");
			}
			List<SubjectInfo> findByIdIn = subjectInfoService.findListByIdIn(id);
			subjectInfoService.deleteCourseInfoAll(findByIdIn.toArray(new SubjectInfo[0]));
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/courseInfo/findCourseInfoCopy")
	@ControllerInfo("复用考试信息")
	public String doCourseInfoCopy(String sourceExamId,String copyExamId, ModelMap map, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
		try{
			List<ExamInfo> examInfoList = examInfoService.findNotDeletedByIdIn(new String[]{sourceExamId,copyExamId});
			if(CollectionUtils.isEmpty(examInfoList) || examInfoList.size()!=2){
				return error("考试已不存在！");
			}
			ExamInfo sourceExamInfo = null;
			ExamInfo oldExamInfo = null;
			for (ExamInfo examInfo : examInfoList) {
				if(examInfo.getId().equals(sourceExamId)){
					sourceExamInfo=examInfo;
				}
				if(examInfo.getId().equals(copyExamId)){
					oldExamInfo=examInfo;
				}
			}
			LoginInfo loginInfo = getLoginInfo(httpSession);
			String unitId=loginInfo.getUnitId();
			if(!sourceExamInfo.getUnitId().equals(unitId)){
				return error("无权限操作！");
			}
			Unit unit = SUtils.dc(unitService.findOneById(unitId), Unit.class);
			int saveCopySize = subjectInfoService.saveCopy(unit,sourceExamInfo,oldExamInfo);
			if(saveCopySize == 0){
				return error("没有可复用的数据！");
			}
			return success("成功复用"+saveCopySize+"条信息！");
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
	}
	@ResponseBody
	@RequestMapping("/courseInfo/findCourseInfoCopyClass")
	@ControllerInfo("复用考试班级信息")
	public String doCourseInfoCopyClass(String sourceExamId,String copyExamId, ModelMap map, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
		try{
			List<ExamInfo> examInfoList = examInfoService.findNotDeletedByIdIn(new String[]{sourceExamId,copyExamId});
			if(CollectionUtils.isEmpty(examInfoList) || examInfoList.size()!=2){
				return error("考试已不存在！");
			}
			LoginInfo loginInfo = getLoginInfo(httpSession);
			String unitId=loginInfo.getUnitId();
			ExamInfo sourceExamInfo = null;
			ExamInfo oldExamInfo = null;
			for (ExamInfo examInfo : examInfoList) {
				if(examInfo.getId().equals(sourceExamId)){
					sourceExamInfo=examInfo;
				}
				if(examInfo.getId().equals(copyExamId)){
					oldExamInfo=examInfo;
				}
			}
			Unit unit = SUtils.dc(unitService.findOneById(unitId), Unit.class);
			int saveCopySize = subjectInfoService.saveCopyClass(unit,sourceExamInfo,oldExamInfo);
			if(saveCopySize == 0){
				return error("没有可复用的数据！");
			}
			return success("成功复用！");
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
	}
	@ResponseBody
	@RequestMapping("/courseInfo/lockScore")
	@ControllerInfo("一键锁定/解锁成绩")
	public String doLockScore(String examId,String type, ModelMap map, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
		try{
			List<ExamInfo> examInfoList = examInfoService.findNotDeletedByIdIn(examId);
			if(CollectionUtils.isEmpty(examInfoList)){
				return error("考试已不存在！");
			}
			LoginInfo loginInfo = getLoginInfo(httpSession);
			String unitId=loginInfo.getUnitId();
			if(!examInfoList.get(0).getUnitId().equals(unitId)){
				return error("无权限操作！");
			}
			List<SubjectInfo> findByExamId = subjectInfoService.findByExamIdIn(null, examId);
			subjectInfoService.saveAllEntitys(findByExamId.toArray(new SubjectInfo[0]));
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/courseInfo/clearCurr")
	@ControllerInfo("清空当前考试的考试科目")
	public String doClear(String examId, ModelMap map, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
		try{
			List<ExamInfo> examInfoList = examInfoService.findNotDeletedByIdIn(examId);
			if(CollectionUtils.isEmpty(examInfoList)){
				return error("考试已不存在！");
			}
			LoginInfo loginInfo = getLoginInfo(httpSession);
			String unitId=loginInfo.getUnitId();
			if(!examInfoList.get(0).getUnitId().equals(unitId)){
				return error("无权限操作！");
			}
			List<ScoreInfo> findByExamId = scoreInfoService.findByExamId(examId);
			if(findByExamId.size()>0){
				return error("考试下已存在成绩，不能删除科目！");
			}
			List<SubjectInfo> list = subjectInfoService.findByExamIdIn(null, examId);
			subjectInfoService.deleteCourseInfoAll(list.toArray(new SubjectInfo[0]));
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/courseInfo/copyGradeCourse")
	@ControllerInfo("考试科目复制到同学段")
	public String doCopyGradeCourse(String examId,String gradeCode,String[] toGradeCode, ModelMap map, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
		try{
			List<ExamInfo> examInfoList = examInfoService.findNotDeletedByIdIn(examId);
			if(CollectionUtils.isEmpty(examInfoList)){
				return error("考试已不存在！");
			}
			LoginInfo loginInfo = getLoginInfo(httpSession);
			String unitId=loginInfo.getUnitId();
			if(!examInfoList.get(0).getUnitId().equals(unitId)){
				return error("无权限操作！");
			}
			int saveGradeCourseCopy = subjectInfoService.saveGradeCourseCopy(examId,gradeCode,toGradeCode);
			if(saveGradeCourseCopy == 0){
				return error("没有可复制的数据！");
			}
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@RequestMapping("/courseInfo/exportReport")
	@ResponseBody
	public void exportLogs(String examId,String searchType, 
			HttpSession httpSession, HttpServletResponse resp) {
		try {
			List<ExamInfo> examInfoList = examInfoService.findNotDeletedByIdIn(examId);
			if(CollectionUtils.isEmpty(examInfoList)){
				throw new Exception("考试已不存在！");
			}
			LoginInfo loginInfo = getLoginInfo(httpSession);
			String unitId=loginInfo.getUnitId();
			Unit u = SUtils.dc(unitService.findOneById(unitId), Unit.class);
			ExamInfo examInfo = examInfoList.get(0);
			Semester semester = SUtils.dc(semesterService.getCurrentSemester(2), Semester.class);
			int beforeAcadyear = NumberUtils.toInt(StringUtils.substringBefore(examInfo.getAcadyear(), "-"));
			int beforeCurrAcadyear = NumberUtils.toInt(StringUtils.substringBefore(semester.getAcadyear(), "-"));
			int linInt = beforeCurrAcadyear-beforeAcadyear;
			if(linInt<0){
				linInt=0;
			}
			ExportUtils exportUtils = ExportUtils.newInstance();
			List<SubjectInfo> cifList = subjectInfoService.findByExamIdIn(unitId, examInfo.getId());
			Set<String> subjectIds = new HashSet<String>();
			Set<String> classIds = new HashSet<String>();
			Set<String> teachClassIds = new HashSet<String>();
			for (SubjectInfo item : cifList) {
				subjectIds.add(item.getSubjectId());
				if(item.getClassIds()!=null)
				for(String classId:item.getClassIds()){
					classIds.add(classId);
				}
				if(item.getTeachClassIds()!=null)
				for(String teachClassId:item.getTeachClassIds()){
					teachClassIds.add(teachClassId);
				}
			}
			Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-RKXD-0","DM-RKXD-1","DM-RKXD-2","DM-RKXD-3"}), new TR<Map<String, Map<String, McodeDetail>>>(){});
			List<Course> courses = SUtils.dt(courseService.findListByIds(subjectIds.toArray(new String[0])), new TR<List<Course>>(){});
			Map<String, Course> courseMap = EntityUtils.getMap(courses, "id");
			Map<String, Clazz> classMap = null;
			if(u.getUnitClass()==2){
				//学校
				classMap = new HashMap<String, Clazz>();
				List<Clazz> dt = SUtils.dt(classService.findListByIds(classIds.toArray(new String[0])), new TR<List<Clazz>>(){});
				for (Clazz clazz : dt) {
					classMap.put(clazz.getId(), clazz);
				}
			}else{
				//教育局
				classMap = new HashMap<String, Clazz>();
			}
			Map<String, TeachClass> teachClassMap = EntityUtils.getMap(SUtils
					.dt(teachClassService.findListByIds(teachClassIds.toArray(new String[0])), new TR<List<TeachClass>>() {
					}), "id");
			List<SubjectInfoExport> cifExList = new ArrayList<SubjectInfoExport>();
			SubjectInfoExport linCife = null;
			StringBuffer sbf = null;
			for (SubjectInfo item : cifList) {
				linCife = new SubjectInfoExport();
				cifExList.add(linCife);
				if(ScoreDataConstants.RANGE_TYPE99.equals(item.getRangeType())){
					linCife.setGradeName("教学班");
				}else{
					Map<String, McodeDetail> map = findMapMapByMcodeIds.get("DM-RKXD-"+item.getRangeType().substring(0, 1));
					if(map!=null){
//					McodeDetail mcodeDetail = map.get(String.valueOf(Integer.valueOf(item.getRangeType().substring(1, 2))+linInt));
						McodeDetail mcodeDetail = map.get(String.valueOf(Integer.valueOf(item.getRangeType().substring(1, 2))));
						if(mcodeDetail!=null){
							linCife.setGradeName(mcodeDetail.getMcodeContent());
						}else{
							linCife.setGradeName("【未找到年级名称】");
						}
					}else{
						linCife.setGradeName("【未找到年级】");
					}
				}
				Course course = courseMap.get(item.getSubjectId());
				if(course!=null){
					linCife.setCourseName(course.getShortName());
				}else{
					linCife.setCourseName("【未找到科目】");
				}
				sbf = new StringBuffer();
				if(item.getClassIds()!=null)
				for (String classId : item.getClassIds()) {
					Clazz clazz = classMap.get(classId);
					if(clazz!=null){
						sbf.append(clazz.getClassNameDynamic());
					}else{
						sbf.append("【未找到行政班】");
					}
					sbf.append(",");
				}
				if(item.getTeachClassIds()!=null)
				for (String teachClassId : item.getTeachClassIds()) {
					TeachClass teachClass = teachClassMap.get(teachClassId);
					if(teachClass!=null){
						sbf.append(teachClass.getName());
					}else{
						sbf.append("【未找到教学班】");
					}
					sbf.append(",");
				}
				if(sbf.length()>0){
					linCife.setClassName(sbf.substring(0, sbf.length()-1));
				}else{
					linCife.setClassName("【未设置班级】");
				}
			}
			String[] fieldTitles = null;
			String[] propertyNames = null;
			if(u.getUnitClass()==2){
				//学校
				fieldTitles = new String[] { "年级", "科目", "班级"};
				propertyNames = new String[] { "gradeName",
						"courseName", "className"};
			}else{
				//教育局
				fieldTitles = new String[] { "年级", "科目"};
				propertyNames = new String[] { "gradeName",
						"courseName"};
			}
			Map<String, List<SubjectInfoExport>> records = Maps.newHashMap();
			records.put("考试科目总览", cifExList);
			exportUtils.exportXLSFile(fieldTitles, propertyNames, records,
					examInfo.getExamName()+"考试科目总览", resp);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出Excel失败", e);
			// return error("导出失败！");
		}
		// return success("导出成功！");
	}
}
