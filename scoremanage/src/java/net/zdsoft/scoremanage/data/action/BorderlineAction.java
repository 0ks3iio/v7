package net.zdsoft.scoremanage.data.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.TaskJobDto;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.TaskRecordService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dto.BorderlineDto;
import net.zdsoft.scoremanage.data.entity.Borderline;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.data.service.BorderlineService;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

@Controller
@RequestMapping("/scoremanage")
public class BorderlineAction extends BaseAction{
	
	@Autowired
	private BorderlineService borderlineService;
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private SubjectInfoService courseInfoService;
	@Autowired
	private CourseRemoteService courseService;
	@Autowired
	private TaskRecordService taskRecordService;
	@Autowired
	private SemesterRemoteService semesterService;
	@Autowired
	private UnitRemoteService unitService;
	@Autowired
	private SchoolRemoteService  schoolService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	
	@RequestMapping("/borderline/index/page")
    @ControllerInfo(value = "分数线设置")
    public String showIndex(ModelMap map, HttpSession httpSession) {
        List<String> acadyearList = SUtils.dt(semesterService.findAcadeyearList(), new TR<List<String>>(){}); 
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId=loginInfo.getUnitId();
		map.put("unitId", unitId);
		List<Grade> gradeList = new ArrayList<Grade>();
		Unit u = SUtils.dc(unitService.findOneById(unitId), Unit.class);
		map.put("unitClass", u.getUnitClass());
		Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-RKXD-0","DM-RKXD-1","DM-RKXD-2","DM-RKXD-3"}), new TR<Map<String, Map<String,McodeDetail>>>(){});
		if(u.getUnitClass()==2){
			//学校
			School school = SUtils.dc(schoolService.findOneById(unitId), School.class);
			gradeList = getSchGradeList(findMapMapByMcodeIds,school);
			map.put("gradeList", gradeList);
			gradeList = getSchGkGradeList(findMapMapByMcodeIds, school);
			map.put("gkGradeList", gradeList);
		}
		List<McodeDetail> mcodelist = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-JYJXZ"), new TR<List<McodeDetail>>(){});
		gradeList=getEduGradeList(mcodelist,findMapMapByMcodeIds);
		map.put("gradeEduList", gradeList);
		gradeList = getEduGkGradeList(mcodelist,findMapMapByMcodeIds);
		map.put("gkGradeEduList", gradeList);
		Semester semester = SUtils.dc(semesterService.getCurrentSemester(2), Semester.class);
		String acadyearSearch = semester.getAcadyear();
		String semesterSearch=semester.getSemester()+"";
		
		map.put("acadyearList", acadyearList);
		map.put("acadyearSearch", acadyearSearch);
		map.put("semesterSearch", semesterSearch);
		
        return "/scoremanage/borderline/borderlineIndex.ftl";
    }
	private List<Grade> getSchGkGradeList(Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds,
			School school) {
		List<Grade> gradeList;
		Integer yearLength = school.getSeniorYear();
		Map<String, McodeDetail> mdMap = findMapMapByMcodeIds.get("DM-RKXD-3");
		gradeList = new ArrayList<Grade>();
		for (int j = 0; j < yearLength; j++) {
			int grade = j + 1;
			Grade dto = new Grade();
			dto.setGradeCode(BaseConstants.SECTION_HIGH_SCHOOL+""+grade);
			if(mdMap.containsKey(grade+"")){
				dto.setGradeName(mdMap.get(grade+"").getMcodeContent());
			}
			gradeList.add(dto);
		}
		Collections.sort(gradeList, new Comparator<Grade>() {
			public int compare(Grade o1, Grade o2) {
				return (o1.getGradeCode().compareToIgnoreCase(o2.getGradeCode()));
			}
		});
		return gradeList;
	}
	
	private List<Grade> getEduGkGradeList(List<McodeDetail> mcodelist,Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds) {
		List<Grade> gradeList = new ArrayList<Grade>();
		for (int i = 0; i < mcodelist.size(); i++) {
			McodeDetail detail =  mcodelist.get(i);
			int section = Integer.parseInt(detail.getThisId());
			if(BaseConstants.SECTION_HIGH_SCHOOL != section){
				continue;
			}
			String thisId=detail.getThisId();
			Map<String, McodeDetail> mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-"+thisId);
			if(mcodeMap==null || mcodeMap.size()<=0){
				continue;
			}
			int nz = Integer.parseInt(detail.getMcodeContent());// 年制
			for (int j = 0; j < nz; j++) {
				int grade = j + 1;
				Grade dto = new Grade();
				dto.setGradeCode(section+""+grade);
				if(mcodeMap.containsKey(grade+"")){
					dto.setGradeName(mcodeMap.get(grade+"").getMcodeContent());
				}
				gradeList.add(dto);
			}
		}
		Collections.sort(gradeList, new Comparator<Grade>() {
			public int compare(Grade o1, Grade o2) {
				return (o1.getGradeCode().compareToIgnoreCase(o2.getGradeCode()));
			}
		});
		return gradeList;
	}
	private List<Grade> getSchGradeList(Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds,School school) {
		List<Grade> gradeList = new ArrayList<Grade>();
		String sections = school.getSections();
		if(StringUtils.isNotBlank(sections)){
			String[] sectionArr = sections.split(",");
			Integer yearLength = 0;
			Map<String, McodeDetail> map = null;
			for(String ss:sectionArr){
				int section = Integer.parseInt(ss);
				switch(section){
					case 0:
						yearLength = school.getInfantYear();
						map = findMapMapByMcodeIds.get("DM-RKXD-0");
						break;
					case 1:
						yearLength = school.getGradeYear();
						map = findMapMapByMcodeIds.get("DM-RKXD-1");
						break;
					case 2:
						yearLength = school.getJuniorYear();
						map = findMapMapByMcodeIds.get("DM-RKXD-2");
						break;
					case 3:
						yearLength = school.getSeniorYear();
						map = findMapMapByMcodeIds.get("DM-RKXD-3");
						break;
					default:
						break;
				}
				if(yearLength==null || yearLength==0){
					continue;
				}
				for (int j = 0; j < yearLength; j++) {
					int grade = j + 1;
					Grade dto = new Grade();
					dto.setGradeCode(section+""+grade);
					if(map.containsKey(grade+"")){
						dto.setGradeName(map.get(grade+"").getMcodeContent());
					}
					gradeList.add(dto);
				}
			}
		}
		Collections.sort(gradeList, new Comparator<Grade>() {
			public int compare(Grade o1, Grade o2) {
				return (o1.getGradeCode().compareToIgnoreCase(o2.getGradeCode()));
			}
		});
		return gradeList;
	}
	private List<Grade> getEduGradeList(List<McodeDetail> mcodelist,Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds){
		List<Grade> gradeList = new ArrayList<Grade>();
		// 取教育局学制微代码信息
		for (int i = 0; i < mcodelist.size(); i++) {
			McodeDetail detail =  mcodelist.get(i);
			int section = Integer.parseInt(detail.getThisId());
			String thisId=detail.getThisId();
			Map<String, McodeDetail> mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-"+thisId);
			if(mcodeMap==null || mcodeMap.size()<=0){
				continue;
			}
			int nz = Integer.parseInt(detail.getMcodeContent());// 年制
			for (int j = 0; j < nz; j++) {
				int grade = j + 1;
				Grade dto = new Grade();
				dto.setGradeCode(section+""+grade);
				if(mcodeMap.containsKey(grade+"")){
					dto.setGradeName(mcodeMap.get(grade+"").getMcodeContent());
				}
				gradeList.add(dto);
			}
		}
		Collections.sort(gradeList, new Comparator<Grade>() {
			public int compare(Grade o1, Grade o2) {
				return (o1.getGradeCode().compareToIgnoreCase(o2.getGradeCode()));
			}
		});
		return gradeList;
	}
	@RequestMapping("/borderline/copyAdd/page")
	@ControllerInfo(value = "进入复用选择考试页面")
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
		map.put("examInfoList", examInfoLi);
		map.put("examId", examId);
		return "/scoremanage/borderline/borderlineCopyAdd.ftl"; 
	}
	
	@ResponseBody
	@RequestMapping("/borderline/findCopy")
	@ControllerInfo("复用考试信息")
	public String doCourseInfoCopy(String sourceExamId,String copyExamId, ModelMap map, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
		try{
			List<ExamInfo> examInfoList = examInfoService.findNotDeletedByIdIn(new String[]{sourceExamId,copyExamId});
			if(CollectionUtils.isEmpty(examInfoList) || examInfoList.size()!=2){
				return error("考试已不存在！");
			}
			LoginInfo loginInfo = getLoginInfo(httpSession);
			String unitId=loginInfo.getUnitId();
			if(!examInfoList.get(0).getUnitId().equals(unitId)){
				return error("无权限操作！");
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
			int saveCopySize = borderlineService.saveCopy(sourceExamInfo,oldExamInfo);
			if(saveCopySize == 0){
				return error("没有可复用的数据！");
			}
			return success("成功复用！");
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
	}
	
	@RequestMapping("/borderline/list/page")
	@ControllerInfo(value = "分数线设置showList")
	public String showList(String examId,String gradeCode,ModelMap map){
		List<ExamInfo> examInfoList = examInfoService.findNotDeletedByIdIn(examId);
		if(CollectionUtils.isEmpty(examInfoList)){
			return errorFtl(map,"考试已不存在！");
		}
		List<SubjectInfo> courseInfoList = courseInfoService.findByUnitIdExamId(null, examId, gradeCode);
		//页面显示用分数线map
		Map<String,List<Borderline>> borderlineMap = new LinkedHashMap<String, List<Borderline>>();//key subjectId+","+courseName
		//页面显示用分数段map
		Map<String,List<Borderline>> gardeSectionMap = new LinkedHashMap<String, List<Borderline>>();//key1 subjectId+","+courseName 
		if(CollectionUtils.isNotEmpty(courseInfoList)){
			Set<String> subjectIds = new HashSet<String>();
			for (SubjectInfo item : courseInfoList) {
				subjectIds.add(item.getSubjectId());
			}
			List<Course> courseList = SUtils.dt(courseService.findListByIds(subjectIds.toArray(new String[0])), new TR<List<Course>>(){});
			List<Borderline> borderlineList = borderlineService.findBorderlineListByExamId(examId,gradeCode);
			//分数线map
			Map<String,List<Borderline>> borMap = new HashMap<String,List<Borderline>>();//key subjectId+","+courseName
			//分数段map
			Map<String,List<Borderline>> garMap = new HashMap<String,List<Borderline>>();//key subjectId+","+courseName
			List<Borderline> lbList = null;
			for (Borderline item : borderlineList) {
				if(ScoreDataConstants.statType1.get(item.getStatType())!=null){
					lbList = borMap.get(item.getSubjectId()+","+item.getCourseName());
					if(lbList == null){
						lbList = new ArrayList<Borderline>();
						borMap.put(item.getSubjectId()+","+item.getCourseName(), lbList);
					}
				}else if(ScoreDataConstants.statType2.get(item.getStatType())!=null){
					lbList = garMap.get(item.getSubjectId()+","+item.getCourseName());
					if(lbList == null){
						lbList = new ArrayList<Borderline>();
						garMap.put(item.getSubjectId()+","+item.getCourseName(), lbList);
					}
				}
				lbList.add(item);
			}
			//处理“总”
			List<Borderline> linList = borMap.get(ScoreDataConstants.ZERO32+","+"总");
			if(linList==null){
				linList = new ArrayList<Borderline>();
			}
			borderlineMap.put(ScoreDataConstants.ZERO32+","+"总", linList);
			
			linList = garMap.get(ScoreDataConstants.ZERO32+","+"总");
			if(linList==null){
				linList = new ArrayList<Borderline>();
			}
			gardeSectionMap.put(ScoreDataConstants.ZERO32+","+"总", linList);
			//处理其他科目
			for (Course item : courseList) {
				linList = borderlineMap.get(item.getId()+","+item.getShortName());
				String shortName=item.getShortName()==null?item.getSubjectName():item.getShortName();
				if(linList == null){
					linList = new ArrayList<Borderline>();
					borderlineMap.put(item.getId()+","+shortName, linList);
				}
				List<Borderline> list = borMap.get(item.getId()+","+shortName);
				if(list!=null){
					linList.addAll(list);
				}
				
				linList = gardeSectionMap.get(item.getId()+","+shortName);
				if(linList == null){
					linList = new ArrayList<Borderline>();
					gardeSectionMap.put(item.getId()+","+shortName, linList);
				}
				list = garMap.get(item.getId()+","+shortName);
				if(list!=null){
					linList.addAll(list);
				}
			}
		}
		map.put("examId", examId);
		map.put("gradeCode", gradeCode);
		map.put("borderlineMap", borderlineMap);
		map.put("gardeSectionMap", gardeSectionMap);
		map.put("statType1", ScoreDataConstants.STAT_METHOD_DO_MAP);
		map.put("businessType", ScoreDataConstants.SCORE_STATISTIC_JOB);
		return "/scoremanage/borderline/borderlineList.ftl"; 
	}
	
	@ResponseBody
    @RequestMapping("/borderline/list/save")
    @ControllerInfo(value = "保存分数线设置")
    public String doSaveList(BorderlineDto borderlineDto, HttpSession httpSession) {
		try{
			List<Borderline> saveList = new ArrayList<Borderline>();
			if(borderlineDto.getBorderlineList()!=null){
				List<Borderline> borderlineList = borderlineDto.getBorderlineList();
				for (Borderline borderline : borderlineList) {
					if(StringUtils.isNotBlank(borderline.getExamId())){
						saveList.add(borderline);
					}
				}
			}
			if(borderlineDto.getGardeSectionList()!=null){
				List<Borderline> borderlineList = borderlineDto.getGardeSectionList();
				for (Borderline borderline : borderlineList) {
					if(StringUtils.isNotBlank(borderline.getExamId())){
						saveList.add(borderline);
					}
				}
			}
			borderlineService.saveAllEntitys(saveList.toArray(new Borderline[0]));
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();
    }
	
	@ResponseBody
	@RequestMapping("/borderline/delete")
	@ControllerInfo("删除分数线")
	public String doDeleteCourseInfo(String[] id, ModelMap map, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		try{
			borderlineService.deleteAllByIds(id);
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/borderline/clearCurr")
	@ControllerInfo("清空当前考试的分数线")
	public String doClear(String examId, ModelMap map, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
		try{
			List<ExamInfo> examInfoList = examInfoService.findNotDeletedByIdIn(examId);
			if(CollectionUtils.isEmpty(examInfoList) || examInfoList.size()<1){
				return error("考试已不存在！");
			}
			LoginInfo loginInfo = getLoginInfo(httpSession);
			String unitId=loginInfo.getUnitId();
			if(!examInfoList.get(0).getUnitId().equals(unitId)){
				return error("无权限操作！");
			}
			List<Borderline> list = borderlineService.findBorderlineListByExamId(examId,null);
			Set<String> ids = new HashSet<String>();
			for (Borderline item : list) {
				ids.add(item.getId());
			}
			borderlineService.deleteAllByIds(ids.toArray(new String[0]));
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	@ResponseBody
	@RequestMapping("/borderline/statistic")
	@ControllerInfo("统计")
	public String doStatistic(String examId, ModelMap map, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
		List<ExamInfo> examInfoList = examInfoService.findNotDeletedByIdIn(examId);
		if(CollectionUtils.isEmpty(examInfoList) || examInfoList.size()<1){
			return error("考试已不存在！");
		}
		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId=loginInfo.getUnitId();
		ExamInfo examInfo = examInfoList.get(0);
		if(!examInfo.getUnitId().equals(unitId)){
			return error("无权限操作！");
		}
		TaskJobDto taskJobDto = new TaskJobDto();
		taskJobDto.setName(examInfo.getExamName()+"成绩统计");
		taskJobDto.setServiceName("scoreStatisticJobService");
		taskJobDto.setHasTask(false);
		taskJobDto.setLoginInfo(loginInfo);
		taskJobDto.setBusinessType(ScoreDataConstants.SCORE_STATISTIC_JOB);
		
		taskJobDto.getCustomParamMap().put("unitId", unitId);
		taskJobDto.getCustomParamMap().put("examId", examId);
		
		String jobId = null;
		try {
			jobId = taskRecordService.TaskJobStart(taskJobDto);
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		return jobSuccess(jobId, "成功");
	}
}
