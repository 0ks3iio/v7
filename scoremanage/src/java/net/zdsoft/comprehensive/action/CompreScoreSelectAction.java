package net.zdsoft.comprehensive.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.comprehensive.constant.CompreStatisticsConstants;
import net.zdsoft.comprehensive.dto.CompreInfoDto;
import net.zdsoft.comprehensive.dto.CompreSetupDto;
import net.zdsoft.comprehensive.entity.CompreInfo;
import net.zdsoft.comprehensive.entity.CompreParameter;
import net.zdsoft.comprehensive.entity.CompreScore;
import net.zdsoft.comprehensive.entity.CompreSetup;
import net.zdsoft.comprehensive.service.CompreInfoService;
import net.zdsoft.comprehensive.service.CompreParameterService;
import net.zdsoft.comprehensive.service.CompreScoreService;
import net.zdsoft.comprehensive.service.CompreSetupService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 总评成绩组成设置
 * @author niuchao
 * @time 2018-12-10
 *
 */

@Controller
@RequestMapping("/comprehensive/select")
public class CompreScoreSelectAction extends BaseAction{
	
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private CompreParameterService compreParameterService;
	@Autowired
	private CompreInfoService compreInfoService;
	@Autowired
	private CompreSetupService compreSetupService;
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private CourseRemoteService courseRemoteService; 
	@Autowired
	private SubjectInfoService subjectInfoService;
	@Autowired
	private CompreScoreService compreScoreService;
	@Autowired
	private StudentRemoteService studentRemoteService;

	@RequestMapping("/index/page")
	@ControllerInfo("首页")
	public String index(ModelMap map){
		//初始化参数
		String unitId = getLoginInfo().getUnitId();
		List<CompreParameter> paramList = compreParameterService.findCompreParameterByUnitId(unitId);
		if(CollectionUtils.isEmpty(paramList)){
			List<CompreParameter> allParamList = compreParameterService.findCompreParameterByUnitId(BaseConstants.ZERO_GUID);
			for (CompreParameter param : allParamList) {
				param.setId(UuidUtils.generateUuid());
				param.setUnitId(unitId);
			}
			compreParameterService.saveAllCompreParameter(allParamList);
		}
		
		//初始化年级列表
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId, new Integer[]{BaseConstants.SECTION_HIGH_SCHOOL,BaseConstants.SECTION_COLLEGE}),Grade.class);
		List<CompreParameter> oneParamList = compreParameterService.findCompreParameterByTypeAndUnitId(CompreStatisticsConstants.TYPE_OVERALL, unitId);
		List<CompreInfo> infoList = compreInfoService.findByUnitIdAndGradeIds(unitId,EntityUtils.getList(gradeList, Grade::getId).toArray(new String[0]));
		Set<String> hasGradeSet = EntityUtils.getSet(infoList, CompreInfo::getGradeId);
		Set<String> gradeSet = EntityUtils.getSet(gradeList, Grade::getId);
		Map<String, Grade> gradeMap = EntityUtils.getMap(gradeList, Grade::getId);
		gradeSet.removeAll(hasGradeSet);
		if(CollectionUtils.isNotEmpty(gradeSet)){
			List<CompreInfo> saveInfoList = new ArrayList<CompreInfo>();
			CompreInfo info;
			for (String gradeId : gradeSet) {
				Grade grade = gradeMap.get(gradeId);
				for (CompreParameter param : oneParamList) {
					info = new CompreInfo();
					info.setId(UuidUtils.generateUuid());
					info.setUnitId(unitId);
					info.setGradeId(gradeId);
					info.setGradeCode(param.getParkey());
					String openAcadyear = grade.getOpenAcadyear();
					int temp = -1;
					if(!param.getParkey().equals(CompreStatisticsConstants.THIRD_LOWER)){
						temp+=Integer.parseInt(param.getParkey().substring(1,2));
					}
					String acadyear = (Integer.parseInt(openAcadyear.substring(0,4))+temp)+"-"+(Integer.parseInt(openAcadyear.substring(5,9))+temp);
					String semester = param.getParkey().substring(2,3);
					info.setAcadyear(acadyear);
					info.setSemester(semester);
					info.setCreationTime(new Date());
					info.setModifyTime(new Date());
					saveInfoList.add(info);
				}
			}
			compreInfoService.saveAll(saveInfoList.toArray(new CompreInfo[0]));
		}
		return "/comprehensive/select/selectIndex.ftl";	
	}
	
	@RequestMapping("/gradeIndex/page")
	@ControllerInfo("年级列表")
	public String gradeIndex(ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(2, unitId), Semester.class);
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId, new Integer[]{BaseConstants.SECTION_HIGH_SCHOOL,BaseConstants.SECTION_COLLEGE}),Grade.class);
		List<CompreInfo> infoList = compreInfoService.findByUnitIdAndGradeIds(unitId,EntityUtils.getList(gradeList, Grade::getId).toArray(new String[0]));
		List<CompreParameter> paramList = compreParameterService.findCompreParameterByTypeAndUnitId(CompreStatisticsConstants.TYPE_OVERALL, unitId);
		Map<String, String> paramMap = EntityUtils.getMap(paramList, CompreParameter::getParkey, CompreParameter::getName);
		Map<String, List<CompreInfo>> gradeInfoMap = infoList.stream().collect(Collectors.groupingBy(CompreInfo::getGradeId));
		List<CompreInfoDto> dtoList =new ArrayList<CompreInfoDto>();
		CompreInfoDto dto;
		for (Grade grade : gradeList) {
			dto = new CompreInfoDto();
			dto.setGradeId(grade.getId());
			dto.setGradeName(grade.getGradeName());
			dto.setOpenAcadyear(grade.getOpenAcadyear().substring(0, 4));
			if(BaseConstants.SECTION_COLLEGE.equals(grade.getSection())){//剑桥高中
				dto.setCurkey(BaseConstants.SECTION_HIGH_SCHOOL+grade.getGradeCode().substring(1)+se.getSemester());
			}else{
				dto.setCurkey(grade.getGradeCode()+""+se.getSemester());
			}
			dto.setInfoList(gradeInfoMap.get(grade.getId()));
			dtoList.add(dto);
		}
		map.put("paramMap", paramMap);
		map.put("dtoList", dtoList);
		return "/comprehensive/select/gradeIndex.ftl";	
	}
	
	@RequestMapping("/detailList/page")
	@ControllerInfo("考试及科目首页")
	public String detailHead(String infoId, ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		List<CompreSetup> setupList = compreSetupService.findByUnitIdAndInfoId(unitId,infoId);
		Map<String, List<CompreSetupDto>> dtoMap = new HashMap<String, List<CompreSetupDto>>();
		for (Entry<String, String> entry : CompreStatisticsConstants.TYPE_NAME_MAP.entrySet()) {
			dtoMap.put(entry.getKey(), new ArrayList<CompreSetupDto>());
		}
		if(CollectionUtils.isNotEmpty(setupList)){
			Set<String> examIdSet = EntityUtils.getSet(setupList, CompreSetup::getExamId);
			List<ExamInfo> examInfoList = examInfoService.findListByIds(examIdSet.toArray(new String[examIdSet.size()]));
			Map<String, String> examNameMap = EntityUtils.getMap(examInfoList, ExamInfo::getId, ExamInfo::getExamName);
			Set<String> subjectIdSet = EntityUtils.getSet(setupList, CompreSetup::getSubjectId);
			List<Course> courseList = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIdSet.toArray(new String[subjectIdSet.size()])),Course.class);
			Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId, Course::getSubjectName);
			Map<String, List<CompreSetup>> setupMap = setupList.stream().collect(Collectors.groupingBy(CompreSetup::getType));
			CompreSetupDto dto;
			for (Entry<String, List<CompreSetup>> entry : setupMap.entrySet()) {//key=type
				Map<String, List<CompreSetup>> inMap = entry.getValue().stream().collect(Collectors.groupingBy(CompreSetup::getExamId));
				for (Entry<String, List<CompreSetup>> inEntry : inMap.entrySet()) {//key=examId
					dto = new CompreSetupDto();
					dto.setExamId(inEntry.getKey());
					dto.setExamName(examNameMap.containsKey(inEntry.getKey())?examNameMap.get(inEntry.getKey()):"");
					String subjectNames = "";
					for (CompreSetup setup : inEntry.getValue()) {
						if(courseNameMap.containsKey(setup.getSubjectId())){
							subjectNames += "、"+courseNameMap.get(setup.getSubjectId());
						}
					}
					if(StringUtils.isNotBlank(subjectNames)){
						dto.setSubjectNames(subjectNames.substring(1));
					}
					dtoMap.get(entry.getKey()).add(dto);
				}
			}
		}
		map.put("infoId", infoId);
		map.put("dtoMap", dtoMap);
		map.put("typeNameMap", CompreStatisticsConstants.TYPE_NAME_MAP);
		
		
		return "/comprehensive/select/detailList.ftl";	
	}
	
	@RequestMapping("/xkcjEdit/page")
	@ControllerInfo("学考成绩编辑")
	public String xkcjEdit(String infoId, ModelMap map){
		CompreInfo info = compreInfoService.findOne(infoId);
		
		List<ExamInfo> examList;
		if(!CompreStatisticsConstants.THIRD_LOWER.equals(info.getGradeCode())){//非初三下
			examList = examInfoService.findExamInfoListAll(info.getUnitId(), info.getAcadyear(), info.getSemester(), info.getGradeId());
		}else{//初三下
			examList = examInfoService.findExamInfoList(info.getUnitId(), info.getAcadyear(), info.getSemester());
			if(CollectionUtils.isNotEmpty(examList)){
				examList = examList.stream().filter(e->StringUtils.isNotBlank(e.getRanges())&&e.getRanges().indexOf("23")>=0).collect(Collectors.toList());
			}
		}
		
		
		if(CollectionUtils.isNotEmpty(examList)){
			Grade grade = SUtils.dc(gradeRemoteService.findOneById(info.getGradeId()),Grade.class);
			List<String> examIdList = EntityUtils.getList(examList, ExamInfo::getId);
			List<SubjectInfo> subjectInfoList = subjectInfoService.findByExamIdIn(null,examIdList.toArray(new String[examIdList.size()]));
			if(BaseConstants.SECTION_COLLEGE.equals(grade.getSection())){//剑桥高中
				final String gradeCode = BaseConstants.SECTION_COLLEGE+info.getGradeCode().substring(1);
				subjectInfoList = subjectInfoList.stream().filter(e->e.getInputType().equals(ScoreDataConstants.ACHI_SCORE)&& gradeCode.startsWith(e.getRangeType())).collect(Collectors.toList());
			}else{
				subjectInfoList = subjectInfoList.stream().filter(e->e.getInputType().equals(ScoreDataConstants.ACHI_SCORE)&&info.getGradeCode().startsWith(e.getRangeType())).collect(Collectors.toList());
			}
			Set<String> subjectIdSet = EntityUtils.getSet(subjectInfoList, SubjectInfo::getSubjectId);
			List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdSet.toArray(new String[subjectIdSet.size()])),Course.class);
			Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId, Course::getSubjectName);
			List<CompreSetup> setupList = compreSetupService.findByUnitIdAndInfoIdAndType(info.getUnitId(), infoId, CompreStatisticsConstants.TYPE_OVERALL);
			Map<String, String> selectedMap = EntityUtils.getMap(setupList, CompreSetup::getSubjectId, CompreSetup::getExamId);
			for (SubjectInfo subjectInfo : subjectInfoList) {
				subjectInfo.setCourseName(courseNameMap.containsKey(subjectInfo.getSubjectId())?courseNameMap.get(subjectInfo.getSubjectId()):"");
				if(selectedMap.containsKey(subjectInfo.getSubjectId())&&selectedMap.get(subjectInfo.getSubjectId()).equals(subjectInfo.getExamId())){
					subjectInfo.setIsUsed(Constant.IS_TRUE_Str);
				}else{
					subjectInfo.setIsUsed(Constant.IS_FALSE_Str);
				}
			}
			
			Map<String, List<SubjectInfo>> examSubjectMap = subjectInfoList.stream().collect(Collectors.groupingBy(SubjectInfo::getExamId));
			Map<String, String> examNameMap = EntityUtils.getMap(examList, ExamInfo::getId, ExamInfo::getExamName);
			
			map.put("infoId", infoId);
			map.put("examSubjectMap", examSubjectMap);
			map.put("examNameMap", examNameMap);
		}
		
		return "/comprehensive/select/xkcjEdit.ftl";
	}

	@RequestMapping("/xkcjEdit/save")
	@ResponseBody
	@ControllerInfo("学考成绩保存")
	public String xkcjSave(String infoId, String examSubjectStr, ModelMap map){
		try {
			CompreInfo info = compreInfoService.findOne(infoId);
			if(StringUtils.isBlank(examSubjectStr)){
				compreScoreService.saveAndDelete(infoId,CompreStatisticsConstants.TYPE_OVERALL,null);
			}else{
				String[] examAndSubject = examSubjectStr.split(",");
				Map<String, String> subjectToExam = new HashMap<String, String>();
				for (String str : examAndSubject) {
					String[] sp = str.split("_");
					subjectToExam.put(sp[1], sp[0]);
				}
				List<CompreSetup> saveSetupList = new ArrayList<CompreSetup>();
				for (Entry<String, String> entry : subjectToExam.entrySet()) {
					CompreSetup setup;
					setup = new CompreSetup();
					setup.setId(UuidUtils.generateUuid());
					setup.setUnitId(info.getUnitId());
					setup.setCompreInfoId(infoId);
					setup.setExamId(entry.getValue());
					setup.setSubjectId(entry.getKey());
					setup.setType(CompreStatisticsConstants.TYPE_OVERALL);
					setup.setCreationTime(new Date());
					setup.setModifyTime(new Date());
					saveSetupList.add(setup);
				}
				compreScoreService.saveAndDelete(infoId, CompreStatisticsConstants.TYPE_OVERALL, saveSetupList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@RequestMapping("/otherEdit/check")
	@ControllerInfo("英语、口试、体育科目校验")
	@ResponseBody
	public String otherEditCheck(String infoId, String type, ModelMap map){
		CompreInfo info = compreInfoService.findOne(infoId);
		String subjectId = getSubjectId(info, type);
		if(StringUtils.isBlank(subjectId)){
			return error("科目编号有误！");
		}
		return returnSuccess();
	}

	@RequestMapping("/otherEdit/page")
	@ControllerInfo("英语、口试、体育编辑")
	public String otherEdit(String infoId, String type, ModelMap map){
		CompreInfo info = compreInfoService.findOne(infoId);
		String subjectId = getSubjectId(info, type);
		List<ExamInfo> examList;
		if(!CompreStatisticsConstants.THIRD_LOWER.equals(info.getGradeCode())){
			examList = examInfoService.findExamInfoListAll(info.getUnitId(), info.getAcadyear(), info.getSemester(), info.getGradeId());
		}else{
			examList = examInfoService.findExamInfoList(info.getUnitId(), info.getAcadyear(), info.getSemester());
			if(CollectionUtils.isNotEmpty(examList)){
				examList = examList.stream().filter(e->StringUtils.isNotBlank(e.getRanges())&&e.getRanges().indexOf("23")>=0).collect(Collectors.toList());
			}
		}
		
		if(CollectionUtils.isNotEmpty(examList)){
			Grade grade = SUtils.dc(gradeRemoteService.findOneById(info.getGradeId()),Grade.class);
			List<String> examIdList = EntityUtils.getList(examList, ExamInfo::getId);
			List<SubjectInfo> subjectInfoList = subjectInfoService.findByExamIdIn(null,examIdList.toArray(new String[examIdList.size()]));
			if(BaseConstants.SECTION_COLLEGE.equals(grade.getSection())){//剑桥高中
				final String gradeCode = BaseConstants.SECTION_COLLEGE+info.getGradeCode().substring(1);
				subjectInfoList = subjectInfoList.stream().filter(e->e.getSubjectId().equals(subjectId)
						&&e.getInputType().equals(ScoreDataConstants.ACHI_SCORE)
						&&gradeCode.startsWith(e.getRangeType())).collect(Collectors.toList());
			}else{
				subjectInfoList = subjectInfoList.stream().filter(e->e.getSubjectId().equals(subjectId)
						&&e.getInputType().equals(ScoreDataConstants.ACHI_SCORE)
						&&info.getGradeCode().startsWith(e.getRangeType())).collect(Collectors.toList());
			}
			if(CollectionUtils.isNotEmpty(subjectInfoList)){
				List<CompreSetup> setupList = compreSetupService.findByUnitIdAndInfoIdAndType(info.getUnitId(), infoId, type);
				List<String> selectedList = EntityUtils.getList(setupList,CompreSetup::getExamId);
				List<String> hasExamIdList = EntityUtils.getList(subjectInfoList, SubjectInfo::getExamId);
				Iterator<ExamInfo> iterator = examList.iterator();
				while(iterator.hasNext()){
					ExamInfo examInfo = iterator.next();
					if(hasExamIdList.contains(examInfo.getId())){
						if(selectedList.contains(examInfo.getId())){
							examInfo.setIsStat(Constant.IS_TRUE_Str);
						}else{
							examInfo.setIsStat(Constant.IS_FALSE_Str);
						}
					}else{
						iterator.remove();
					}
				}
				
				map.put("infoId", infoId);
				map.put("type", type);
				map.put("examList", examList);
			}
		}
		
		return "/comprehensive/select/otherEdit.ftl";
	}

	@RequestMapping("/otherEdit/save")
	@ResponseBody
	@ControllerInfo("英语、口试、体育保存")
	public String otherSave(String infoId, String type, String examId, ModelMap map){
		try {
			CompreInfo info = compreInfoService.findOne(infoId);
			if(StringUtils.isBlank(examId)){
				compreScoreService.saveAndDelete(infoId,type,null);
			}else{
				List<CompreSetup> saveSetupList = new ArrayList<CompreSetup>();
				CompreSetup setup = new CompreSetup();
				setup.setId(UuidUtils.generateUuid());
				setup.setUnitId(info.getUnitId());
				setup.setCompreInfoId(infoId);
				setup.setExamId(examId);
				setup.setSubjectId(getSubjectId(info, type));
				setup.setType(type);
				setup.setCreationTime(new Date());
				setup.setModifyTime(new Date());
				saveSetupList.add(setup);
				compreScoreService.saveAndDelete(infoId, type, saveSetupList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}

	private String getSubjectId(CompreInfo info, String type) {
		String subjectCode;
		if(info.getGradeCode().equals(CompreStatisticsConstants.THIRD_LOWER)){
			if(CompreStatisticsConstants.TYPE_ENGLISH.equals(type)){
				subjectCode = CompreStatisticsConstants.SUBJECT_CODE_YY_2;
			}else if(CompreStatisticsConstants.TYPE_ENG_SPEAK.equals(type)){
				subjectCode = CompreStatisticsConstants.SUBJECT_CODE_KS;
			}else{
				subjectCode = CompreStatisticsConstants.SUBJECT_CODE_TY_2;
			}
		}else{
			if(CompreStatisticsConstants.TYPE_ENGLISH.equals(type)){
				subjectCode = CompreStatisticsConstants.SUBJECT_CODE_YY_3;
			}else if(CompreStatisticsConstants.TYPE_ENG_SPEAK.equals(type)){
				subjectCode = CompreStatisticsConstants.SUBJECT_CODE_KS;
			}else{
				subjectCode = CompreStatisticsConstants.SUBJECT_CODE_TY_3;
			}
		}
		List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitCourseCodes(info.getUnitId(), subjectCode),Course.class);
		if(CollectionUtils.isEmpty(courseList)){
			return null;
		}
		return courseList.get(0).getId();
	}
	
	@RequestMapping("/xkcjEdit/count")
	@ResponseBody
	@ControllerInfo("学科成绩统计")
	public String xkcjCount(String gradeId, String gradeCode, ModelMap map){
		try {
			String unitId = getLoginInfo().getUnitId();
			CompreInfo info = compreInfoService.findOneByGradeIdAndGradeCode(unitId, gradeId, gradeCode);
			if(info==null){
				return error("请先设置总评成绩组成");
			}
			List<CompreSetup> setupList = compreSetupService.findByUnitIdAndInfoIdAndType(unitId, info.getId(), CompreStatisticsConstants.TYPE_OVERALL);
			if(CollectionUtils.isEmpty(setupList)){
				return error("请先设置总评成绩组成");
			}
			List<CompreScore> scoreList = compreScoreService.findByInfoIdAndType(unitId, info.getId(), CompreStatisticsConstants.TYPE_OVERALL);
			if(CollectionUtils.isNotEmpty(scoreList)){
				List<CompreScore> saveScoreList = new ArrayList<CompreScore>();
				List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitCourseCodes(unitId, CompreStatisticsConstants.CODE_73.toArray(new String[0])), Course.class);
				List<String> subjectIdList = EntityUtils.getList(courseList, Course::getId);
				Map<String, List<CompreScore>> subjectScoreMap = scoreList.stream().filter(e->!BaseConstants.ZERO_GUID.equals(e.getSubjectId())).collect(Collectors.groupingBy(CompreScore::getSubjectId));
				for (Entry<String, List<CompreScore>> entry : subjectScoreMap.entrySet()) {
					List<CompreScore> subjectScoreList = entry.getValue();
					if(CollectionUtils.isNotEmpty(subjectIdList) && subjectIdList.contains(entry.getKey())){
						Double ave = subjectScoreList.stream().filter(e-> Objects.equals(Constant.IS_TRUE, e.getVirtual()))
						.collect(Collectors.averagingDouble(new ToDoubleFunction<CompreScore>() {

							@Override
							public double applyAsDouble(CompreScore score) {
								if(StringUtils.isBlank(score.getScore())){
									return 0d;
								}
								return Double.parseDouble(score.getScore());
							}
						}));
						
						for (CompreScore score : subjectScoreList) {
							if(Objects.equals(Constant.IS_TRUE,score.getVirtual())){
								float s = StringUtils.isNotBlank(score.getScore())?Float.parseFloat(score.getScore()):0f;
								score.setToScore((float)((s-ave)/(100-ave)*25+75));
							}else{
								score.setToScore(0f);
							}
						}
						
					}else{
						for (CompreScore score : subjectScoreList) {
							score.setToScore(StringUtils.isNotBlank(score.getScore())?Float.valueOf(score.getScore()):0f);
						}
					}
					
					sort(subjectScoreList);
					saveScoreList.addAll(subjectScoreList);
				}
				if(StringUtils.isBlank(info.getStateArea())){
					info.setStateArea(CompreStatisticsConstants.TYPE_OVERALL);
				}else{
					if(!info.getStateArea().contains(CompreStatisticsConstants.TYPE_OVERALL)){
						info.setStateArea(info.getStateArea()+","+CompreStatisticsConstants.TYPE_OVERALL);
					}
				}
				
				//总分
				List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(EntityUtils.getSet(saveScoreList, CompreScore::getStudentId).toArray(new String[0])),Student.class);
				Map<String, String> studentClassMap = EntityUtils.getMap(studentList, Student::getId, Student::getClassId);
				Map<String, List<CompreScore>> studentScoreMap = saveScoreList.stream().collect(Collectors.groupingBy(CompreScore::getStudentId));
				CompreScore compreScore;
				List<CompreScore> sumScoreList = new ArrayList<CompreScore>();
				for (Entry<String, List<CompreScore>> entry : studentScoreMap.entrySet()) {
					compreScore = new CompreScore();
					compreScore.setId(UuidUtils.generateUuid());
					compreScore.setUnitId(info.getUnitId());
					compreScore.setCompreInfoId(info.getId());
					compreScore.setAcadyear(info.getAcadyear());
					compreScore.setSemester(info.getSemester());
					compreScore.setStudentId(entry.getKey());
					compreScore.setSubjectId(BaseConstants.ZERO_GUID);
					compreScore.setExamId(BaseConstants.ZERO_GUID);
					compreScore.setClassId(studentClassMap.get(entry.getKey()));
					float f = entry.getValue().stream().map(e->e.getToScore()).reduce(Float::sum).get();
					compreScore.setScore(f+"");
					compreScore.setToScore(f);
					compreScore.setVirtual(Constant.IS_TRUE);
					
					compreScore.setScoremanageType(CompreStatisticsConstants.TYPE_OVERALL);
					compreScore.setCreationTime(new Date());
					compreScore.setModifyTime(new Date());

					
					sumScoreList.add(compreScore);
				}
				sort(sumScoreList);
				
				saveScoreList.addAll(sumScoreList);
				compreScoreService.saveAll(info, saveScoreList);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@RequestMapping("/otherEdit/count")
	@ResponseBody
	@ControllerInfo("英语、口试、体育统计")
	public String otherCount(String type, String gradeId, String gradeCode, ModelMap map){
		try {
			String unitId = getLoginInfo().getUnitId();
			CompreInfo info = compreInfoService.findOneByGradeIdAndGradeCode(unitId, gradeId, gradeCode);
			if(info==null){
				return error("请先设置总评成绩组成");
			}
			List<CompreSetup> setupList = compreSetupService.findByUnitIdAndInfoIdAndType(unitId, info.getId(), type);
			if(CollectionUtils.isEmpty(setupList)){
				return error("请先设置总评成绩组成");
			}
			List<CompreScore> scoreList = compreScoreService.findByInfoIdAndType(unitId, info.getId(), type);
			if(CollectionUtils.isNotEmpty(scoreList)){
				if(!CompreStatisticsConstants.TYPE_ENG_SPEAK.equals(type)){
					for (CompreScore score : scoreList) {
						score.setToScore(StringUtils.isNotBlank(score.getScore())?Float.valueOf(score.getScore()):0f);
					}
				}
				sort(scoreList);
				if(StringUtils.isBlank(info.getStateArea())){
					info.setStateArea(type);
				}else{
					if(!info.getStateArea().contains(type)){
						info.setStateArea(info.getStateArea()+","+type);
					}
				}
				compreScoreService.saveAll(info, scoreList);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	private static void sort(List<CompreScore> scoreList) {
		List<Entry<Float, List<CompreScore>>> newList = scoreList.stream().collect(Collectors.groupingBy(CompreScore::getToScore)).entrySet()
		        .stream().sorted((s1, s2) -> -Float.compare(s1.getKey(), s2.getKey())).collect(Collectors.toList());
		scoreList.clear();
		int index = 1;
		for (Entry<Float, List<CompreScore>> entry : newList) {
			final int i = index;
			entry.getValue().forEach(e->e.setRanking(i));
			scoreList.addAll(entry.getValue());
		    index = index+entry.getValue().size();
		}
		
		for (CompreScore score : scoreList) {//特殊处理，如果是0分则为最后一名
			if(score.getToScore()==0f){
				score.setRanking(scoreList.size());
			}
		}
	 }
	
}
