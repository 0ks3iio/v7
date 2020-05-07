package net.zdsoft.scoremanage.data.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dto.ExamInfoSearchDto;
import net.zdsoft.scoremanage.data.dto.ResitScoreListDto;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.ResitInfo;
import net.zdsoft.scoremanage.data.entity.ResitScore;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.ResitInfoService;
import net.zdsoft.scoremanage.data.service.ResitScoreService;
import net.zdsoft.scoremanage.data.service.ScoreInfoService;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/scoremanage")
public class ResitScoreAction extends BaseAction{
	@Autowired
	private SemesterRemoteService semesterService;
	@Autowired
	private UnitRemoteService unitService;
	@Autowired
	private SchoolRemoteService  schoolService;
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private ResitInfoService resitInfoService;
	@Autowired
	private GradeRemoteService gradeService;
	@Autowired
	private ClassRemoteService classService;
	@Autowired
	private ScoreInfoService scoreInfoService;
	@Autowired
	private CourseRemoteService courseService;
	@Autowired
	private SubjectInfoService subjectInfoService;
	@Autowired
	private StudentRemoteService studentService;
	@Autowired
	private ResitScoreService resitScoreService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;

	@RequestMapping("/resitScore/index/page")
    @ControllerInfo(value = "补考管理")
    public String showIndex(ModelMap map, HttpSession httpSession) {
        List<String> acadyearList = SUtils.dt(semesterService.findAcadeyearList(), new TR<List<String>>(){}); 
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
		Semester semester = SUtils.dc(semesterService.getCurrentSemester(2), Semester.class);
		String acadyearSearch = semester.getAcadyear();
		String semesterSearch=semester.getSemester()+"";
		
		map.put("acadyearList", acadyearList);
		map.put("acadyearSearch", acadyearSearch);
		map.put("semesterSearch", semesterSearch);
		List<ExamInfo> examInfoList = new ArrayList<>();
		ExamInfoSearchDto searchDto = new ExamInfoSearchDto();
		searchDto.setSearchAcadyear(acadyearSearch);
		searchDto.setSearchSemester(semesterSearch);
		List<ExamInfo> examInfoListTemp = examInfoService.findExamInfoList(getLoginInfo().getUnitId(),searchDto,null);
		for(ExamInfo examInfo : examInfoListTemp){
			if("12".equals(examInfo.getExamType())){
				examInfoList.add(examInfo);
			}
		}
		map.put("examInfoList", examInfoList);
		List<Grade> gradeList = new ArrayList<>();
		map.put("gradeList", gradeList);
        return "/scoremanage/resitScore/resitScoreIndex.ftl";
    }
	
	@ResponseBody
	@RequestMapping(value="/resitScore/saveResitInfo")
	public String saveResitInfo(String acadyear, String semester, String examId, String gradeId, HttpServletResponse response,HttpSession httpSession) {
    	try{
    		Set<String> courseIdSet = new HashSet<String>();
    		List<Clazz> clsList = SUtils.dt(classService.findByInGradeIds(new String[]{gradeId}), new TR<List<Clazz>>(){}); 
    		Set<String> clsIdSet = new HashSet<String>();
    		for(Clazz cls : clsList){
    			clsIdSet.add(cls.getId());
    		}
    		if(CollectionUtils.isNotEmpty(clsIdSet)){
    			List<ScoreInfo> scoreInfoList = scoreInfoService.findByClsIds(examId,ScoreDataConstants.ACHI_SCORE,clsIdSet.toArray(new String[0]));
    			Set<String> subjectIdSet = new HashSet<String>();
    			List<SubjectInfo> subInfoList = subjectInfoService.findBySubjectInfoList(examId,getLoginInfo().getUnitId(),null);
    			for(SubjectInfo subInfo : subInfoList){
    				subjectIdSet.add(subInfo.getSubjectId());
    			}
    			List<Course> courseList = new ArrayList<>();
    			if(CollectionUtils.isNotEmpty(subjectIdSet)){
    				List<Course> courseListTemp = SUtils.dt(courseService.findListByIds(subjectIdSet.toArray(new String[0])), new TR<List<Course>>(){}); 
    				for(Course course : courseListTemp){
    					if(BaseConstants.SUBJECT_TYPE_BX.equals(course.getType())){
    						courseList.add(course);
    					}
    				}
    				for(Course course : courseList){
    					for(ScoreInfo scoreInfo : scoreInfoList){
    						if(course.getId().equals(scoreInfo.getSubjectId()) && null!=course.getInitPassMark()){
    							if(StringUtils.isNotBlank(scoreInfo.getToScore())){
    								if(null!=scoreInfo.getToScore() && Float.parseFloat(scoreInfo.getToScore())<course.getInitPassMark()){
    									courseIdSet.add(course.getId());
    								}
    							}else{
    								if(null!=scoreInfo.getScore() && Float.parseFloat(scoreInfo.getScore())<course.getInitPassMark()){
    									courseIdSet.add(course.getId());
    								}
    							}
    						}
    					}
    				}
    			}    			
    		}
    		List<ResitInfo> resitInfoList = new ArrayList<>();
			for(String courseId : courseIdSet){
				ResitInfo resitInfo = new ResitInfo();
				resitInfo.setId(UuidUtils.generateUuid());
				resitInfo.setUnitId(getLoginInfo().getUnitId());
				resitInfo.setAcadyear(acadyear);
				resitInfo.setSemester(semester);
				resitInfo.setExamId(examId);
				resitInfo.setGradeId(gradeId);
				resitInfo.setSubjectId(courseId);
				resitInfoList.add(resitInfo);
			}
			resitInfoService.deleteResitInfoBy(getLoginInfo().getUnitId(), acadyear, semester, examId, gradeId);
			if(CollectionUtils.isNotEmpty(resitInfoList)){				
				resitInfoService.saveAll(resitInfoList.toArray(new ResitInfo[0]));
			}
    	}catch (Exception e) {
			e.printStackTrace();
			return returnError("获取补考名单失败！", e.getMessage());
		}
		return success("获取补考名单成功！");
	}
	
	@ResponseBody
	@RequestMapping("/resitScore/searchExam")
	public List<ExamInfo> searchExam(String acadyear,String semester){
		List<ExamInfo> examInfoList = new ArrayList<>();
		ExamInfoSearchDto searchDto = new ExamInfoSearchDto();
		searchDto.setSearchAcadyear(acadyear);
		searchDto.setSearchSemester(semester);
		List<ExamInfo> examInfoListTemp = examInfoService.findExamInfoList(getLoginInfo().getUnitId(),searchDto,null);
		for(ExamInfo examInfo : examInfoListTemp){
			if("12".equals(examInfo.getExamType())){
				examInfoList.add(examInfo);
			}
		}
		return examInfoList;
	}
	
	@ResponseBody
	@RequestMapping("/resitScore/searchGrade")
	public List<Grade> searchGrade(String examId){
		List<Grade> gradeList = new ArrayList<>();
		ExamInfo examInfo = examInfoService.findOne(examId);
		String ranges = examInfo.getRanges();
		String  acadyear = examInfo.getAcadyear();
		
		Map<String,String> allCode=new HashMap<String, String>();
    	List<McodeDetail> mlist = SUtils.dt(mcodeRemoteService.findAllByMcodeIds(new String[]{"DM-RKXD-0","DM-RKXD-1","DM-RKXD-2","DM-RKXD-3","DM-RKXD-9"}),new TR<List<McodeDetail>>(){});
    	if(CollectionUtils.isNotEmpty(mlist)){
    		for(McodeDetail m:mlist){
    			if("DM-RKXD-0".equals(m.getMcodeId())){
    				allCode.put("0"+m.getThisId(), m.getMcodeContent());
    			}else
    			if("DM-RKXD-1".equals(m.getMcodeId())){
    				allCode.put("1"+m.getThisId(), m.getMcodeContent());
    			}else
    			if("DM-RKXD-2".equals(m.getMcodeId())){
    				allCode.put("2"+m.getThisId(), m.getMcodeContent());
    			}else
    			if("DM-RKXD-3".equals(m.getMcodeId())){
    				allCode.put("3"+m.getThisId(), m.getMcodeContent());
    			}
    			else
        			if("DM-RKXD-9".equals(m.getMcodeId())){
        				allCode.put("9"+m.getThisId(), m.getMcodeContent());
        			}
    		}
    	}
		
		if(StringUtils.isNotBlank(ranges)){
			String[] gradeCodeArr = ranges.split(",");
			for(String gradeCode : gradeCodeArr){
				String oyear = String.valueOf(Integer.parseInt(acadyear.substring(5,9))-Integer.parseInt(gradeCode.substring(1, 2)));
				String eyear = String.valueOf(Integer.parseInt(oyear)+1);
				String openAcadyear = oyear+"-"+eyear;
				String section = gradeCode.substring(0, 1);
				Grade grade = SUtils.dc(gradeService.findOneBy(new String[] {"schoolId","openAcadyear","section","isDeleted"}, new String[]{getLoginInfo().getUnitId(),openAcadyear,section,"0"}), Grade.class);;
				grade.setGradeName(allCode.get(gradeCode));
				gradeList.add(grade);
			}
		}
		return gradeList;
	}
	
	@RequestMapping("/resitScore/resitInfoList")
    @ControllerInfo(value = "补考管理")
    public String resitInfoList(String acadyear, String semester, String examId, String gradeId,ModelMap map) {
		List<ResitInfo> resitInfoList = resitInfoService.listResitInfoBy(getLoginInfo().getUnitId(), acadyear, semester, examId, gradeId);
		Set<String> courseIdSet = new HashSet<String>();
		for(ResitInfo info : resitInfoList){
			courseIdSet.add(info.getSubjectId());
		}
		if(CollectionUtils.isNotEmpty(courseIdSet)){
			List<Course> courseList = SUtils.dt(courseService.findListByIds(courseIdSet.toArray(new String[0])), new TR<List<Course>>(){}); 
			Map<String, Course> courseMap = new HashMap<String, Course>();
			for(Course course : courseList){
				courseMap.put(course.getId(), course);
			}
			for(ResitInfo info : resitInfoList){
				info.setSubjectName(courseMap.get(info.getSubjectId()).getSubjectName());
				info.setSubjectCode(courseMap.get(info.getSubjectId()).getSubjectCode());
			}
		}
		Collections.sort(resitInfoList, new Comparator<ResitInfo>() {
    		@Override
    		public int compare(ResitInfo o1, ResitInfo o2) {
    			if(StringUtils.isNotBlank(o1.getSubjectCode()) && StringUtils.isNotBlank(o2.getSubjectCode())){
    				if(!o1.getSubjectCode().equals(o2.getSubjectCode())){
    					return o1.getSubjectCode().compareTo(o2.getSubjectCode());
    				}
    			}
    			return o1.getSubjectCode().compareTo(o2.getSubjectCode());
    		}
    	});
		map.put("resitInfoList", resitInfoList);
		if(CollectionUtils.isNotEmpty(resitInfoList)){
			map.put("subjectId", resitInfoList.get(0).getSubjectId());
		}
        return "/scoremanage/resitScore/resitInfoHead.ftl";
    }
	
	public List<ScoreInfo> scoreInfoList(String acadyear, String semester, String examId, String gradeId,String subjectId) {
		Course course = SUtils.dc(courseService.findOneById(subjectId),Course.class);		
		List<Clazz> clsList = SUtils.dt(classService.findByInGradeIds(new String[]{gradeId}), new TR<List<Clazz>>(){}); 
		Set<String> clsIdSet = new HashSet<String>();
		for(Clazz cls : clsList){
			clsIdSet.add(cls.getId());
		}
		List<ScoreInfo> scoreInfoListTemp = scoreInfoService.findListByClsIds(getLoginInfo().getUnitId(), acadyear, semester, examId, subjectId, ScoreDataConstants.ACHI_SCORE,clsIdSet.toArray(new String[0]));
        int initPassMark = course.getInitPassMark();
        List<ScoreInfo> scoreInfoList = new ArrayList<ScoreInfo>();
        Set<String> stuIdSet = new HashSet<String>();
        Set<String> clsIdSet2 = new HashSet<String>();
        for(ScoreInfo scoreInfo : scoreInfoListTemp){
        	if(StringUtils.isNotBlank(scoreInfo.getToScore())){
        		if(Float.parseFloat(scoreInfo.getToScore())<initPassMark){
        			scoreInfoList.add(scoreInfo);
        			stuIdSet.add(scoreInfo.getStudentId());
        			clsIdSet2.add(scoreInfo.getClassId());
        		}
        	}else{
        		if(StringUtils.isBlank(scoreInfo.getScore()) || Float.parseFloat(scoreInfo.getScore())<initPassMark){
        			scoreInfoList.add(scoreInfo);
        			stuIdSet.add(scoreInfo.getStudentId());
        			clsIdSet2.add(scoreInfo.getClassId());
        		}
        	}
        }
        Map<String, String> stuNameMap = new HashMap<String, String>();
        Map<String, String> clsNameMap = new HashMap<String, String>();
        Map<String, String> stuCodeMap = new HashMap<String, String>();
        Map<String, String> clsCodeMap = new HashMap<String, String>();
        if(CollectionUtils.isNotEmpty(stuIdSet)){      	
        	List<Student> stuList = SUtils.dt(studentService.findListByIds(stuIdSet.toArray(new String[0])), new TR<List<Student>>(){}); 
        	for(Student student : stuList){
        		stuNameMap.put(student.getId(), student.getStudentName());
        		stuCodeMap.put(student.getId(), student.getStudentCode());
        	}
        }
        if(CollectionUtils.isNotEmpty(clsIdSet2)){  
        	List<Clazz> clsList2 = SUtils.dt(classService.findClassListByIds(clsIdSet2.toArray(new String[0])), new TR<List<Clazz>>(){}); 
        	for(Clazz cls : clsList2){
        		clsNameMap.put(cls.getId(), cls.getClassNameDynamic());
        		clsCodeMap.put(cls.getId(), cls.getClassCode());
        	}
        }
        List<ResitScore> resitScoreList = resitScoreService.listResitScoreBy(getLoginInfo().getUnitId(), examId, subjectId);
        Map<String, String> resitScoreMap = new HashMap<String, String>();
        for(ResitScore score : resitScoreList){
        	resitScoreMap.put(score.getStudentId(), score.getScore());
        }
        for(ScoreInfo scoreInfo : scoreInfoList){
        	scoreInfo.setStudentName(stuNameMap.get(scoreInfo.getStudentId()));
        	scoreInfo.setClsName(clsNameMap.get(scoreInfo.getClassId()));
        	scoreInfo.setStudentCode(stuCodeMap.get(scoreInfo.getStudentId()));
        	scoreInfo.setClsCode(clsCodeMap.get(scoreInfo.getClassId()));
        	scoreInfo.setSubjectName(course.getSubjectName());
        	scoreInfo.setResitScore(resitScoreMap.get(scoreInfo.getStudentId()));
        }
        Collections.sort(scoreInfoList, new Comparator<ScoreInfo>() {
			@Override
			public int compare(ScoreInfo o1, ScoreInfo o2) {
				if(StringUtils.isNotBlank(o1.getClsCode()) && StringUtils.isNotBlank(o2.getClsCode())){
					if(!o1.getClsCode().equals(o2.getClsCode())){
						return o1.getClsCode().compareTo(o2.getClsCode());
					}
				}
				if(StringUtils.isNotBlank(o1.getStudentId()) && StringUtils.isNotBlank(o2.getStudentId())){
					if(!o1.getStudentId().equals(o2.getStudentId())){
						return o1.getStudentId().compareTo(o2.getStudentId());
					}
				}
				return o1.getStudentId().compareTo(o2.getStudentId());
			}
		});
        return scoreInfoList;
	}
	
	public List<ScoreInfo> scoreInfoList2(String acadyear, String semester, String examId, String gradeId) {
		List<Clazz> clsList = SUtils.dt(classService.findByInGradeIds(new String[]{gradeId}), new TR<List<Clazz>>(){}); 
		Set<String> clsIdSet = new HashSet<String>();
		for(Clazz cls : clsList){
			clsIdSet.add(cls.getId());
		}
		List<ScoreInfo> scoreInfoListTemp = scoreInfoService.findListByClsIds(getLoginInfo().getUnitId(), acadyear, semester, examId, ScoreDataConstants.ACHI_SCORE,clsIdSet.toArray(new String[0]));
        
		List<ResitInfo> resitInfoList = resitInfoService.listResitInfoBy(getLoginInfo().getUnitId(), acadyear, semester, examId, gradeId);
		Set<String> courseIdSet = new HashSet<String>();
		for(ResitInfo info : resitInfoList){
			courseIdSet.add(info.getSubjectId());
		}
		List<ScoreInfo> scoreInfoList = new ArrayList<ScoreInfo>();
		Set<String> stuIdSet = new HashSet<String>();
		Set<String> clsIdSet2 = new HashSet<String>();
		Map<String, Course> courseMap = new HashMap<String, Course>();
        if(CollectionUtils.isNotEmpty(courseIdSet)){       	
        	List<Course> coureList = SUtils.dt(courseService.findListByIds(courseIdSet.toArray(new String[0])), new TR<List<Course>>(){}); 
        	for(Course course : coureList){    	
        		courseMap.put(course.getId(), course);
        		int initPassMark = course.getInitPassMark();
        		for(ScoreInfo scoreInfo : scoreInfoListTemp){
        			if(StringUtils.isNotBlank(scoreInfo.getToScore())){
        				if(course.getId().equals(scoreInfo.getSubjectId()) && Float.parseFloat(scoreInfo.getToScore())<initPassMark){
        					scoreInfoList.add(scoreInfo);
        					stuIdSet.add(scoreInfo.getStudentId());
        					clsIdSet2.add(scoreInfo.getClassId());
        				}
        			}else{
        				if(course.getId().equals(scoreInfo.getSubjectId()) && (StringUtils.isBlank(scoreInfo.getScore()) || Float.parseFloat(scoreInfo.getScore())<initPassMark)){
        					scoreInfoList.add(scoreInfo);
        					stuIdSet.add(scoreInfo.getStudentId());
        					clsIdSet2.add(scoreInfo.getClassId());
        				}
        			}
        		}
        	}
        	Map<String, String> stuNameMap = new HashMap<String, String>();
        	Map<String, String> clsNameMap = new HashMap<String, String>();
        	Map<String, String> stuCodeMap = new HashMap<String, String>();
        	Map<String, String> clsCodeMap = new HashMap<String, String>();
        	if(CollectionUtils.isNotEmpty(stuIdSet)){      	
        		List<Student> stuList = SUtils.dt(studentService.findListByIds(stuIdSet.toArray(new String[0])), new TR<List<Student>>(){}); 
        		for(Student student : stuList){
        			stuNameMap.put(student.getId(), student.getStudentName());
        			stuCodeMap.put(student.getId(), student.getStudentCode());
        		}
        	}
        	if(CollectionUtils.isNotEmpty(clsIdSet2)){  
        		List<Clazz> clsList2 = SUtils.dt(classService.findClassListByIds(clsIdSet2.toArray(new String[0])), new TR<List<Clazz>>(){}); 
        		for(Clazz cls : clsList2){
        			clsNameMap.put(cls.getId(), cls.getClassNameDynamic());
        			clsCodeMap.put(cls.getId(), cls.getClassCode());
        		}
        	}
        	List<ResitScore> resitScoreList = resitScoreService.listResitScoreBy(getLoginInfo().getUnitId(), examId);
        	Map<String, String> resitScoreMap = new HashMap<String, String>();
        	for(ResitScore score : resitScoreList){
        		resitScoreMap.put(score.getStudentId()+score.getSubjectId(), score.getScore());
        	}
        	for(ScoreInfo scoreInfo : scoreInfoList){
        		scoreInfo.setStudentName(stuNameMap.get(scoreInfo.getStudentId()));
        		scoreInfo.setClsName(clsNameMap.get(scoreInfo.getClassId()));
        		scoreInfo.setStudentCode(stuCodeMap.get(scoreInfo.getStudentId()));
        		scoreInfo.setClsCode(clsCodeMap.get(scoreInfo.getClassId()));
        		scoreInfo.setSubjectName(courseMap.get(scoreInfo.getSubjectId()).getSubjectName());
        		scoreInfo.setResitScore(resitScoreMap.get(scoreInfo.getStudentId()+scoreInfo.getSubjectId()));
        		scoreInfo.setSubjectCode(courseMap.get(scoreInfo.getSubjectId()).getSubjectCode());
        	}
        	Collections.sort(scoreInfoList, new Comparator<ScoreInfo>() {
        		@Override
        		public int compare(ScoreInfo o1, ScoreInfo o2) {
        			if(StringUtils.isNotBlank(o1.getSubjectCode()) && StringUtils.isNotBlank(o2.getSubjectCode())){
        				if(!o1.getSubjectCode().equals(o2.getSubjectCode())){
        					return o1.getSubjectCode().compareTo(o2.getSubjectCode());
        				}
        			}
        			if(StringUtils.isNotBlank(o1.getClsCode()) && StringUtils.isNotBlank(o2.getClsCode())){
        				if(!o1.getClsCode().equals(o2.getClsCode())){
        					return o1.getClsCode().compareTo(o2.getClsCode());
        				}
        			}
        			if(StringUtils.isNotBlank(o1.getStudentId()) && StringUtils.isNotBlank(o2.getStudentId())){
        				if(!o1.getStudentId().equals(o2.getStudentId())){
        					return o1.getStudentId().compareTo(o2.getStudentId());
        				}
        			}
        			return o1.getStudentId().compareTo(o2.getStudentId());
        		}
        	});
        }		
        return scoreInfoList;
	}
	
	@RequestMapping("/resitScore/resitScoreList")
    @ControllerInfo(value = "补考管理")
    public String resitScoreList(String acadyear, String semester, String examId, String gradeId,String subjectId,ModelMap map) {
		List<ScoreInfo> scoreInfoList = new ArrayList<ScoreInfo>();
		scoreInfoList = scoreInfoList(acadyear, semester, examId, gradeId, subjectId);
		Course course = SUtils.dc(courseService.findOneById(subjectId),Course.class);
		String subjectCode = course.getSubjectCode();
		ExamInfo examInfo = examInfoService.findOne(examId);
		if(ScoreDataConstants.EXAM_TYPE_FINAL.equals(examInfo.getExamType()) 
				&& !BaseConstants.HW_CODE_KS.equals(subjectCode) && !BaseConstants.HW_CODE_XZ.equals(subjectCode)){
			map.put("needGeneral", true);
		}
		if(null!=course.getFullMark()){
			map.put("fullMark", course.getFullMark());
		}
        map.put("scoreInfoList", scoreInfoList);
        map.put("examId", examId);
        map.put("subjectId", subjectId);
        map.put("gradeId", gradeId);
		return "/scoremanage/resitScore/resitInfoList.ftl";
    }
	
	@RequestMapping("/resitScore/doExport")
    @ControllerInfo(value = "补考管理")
    public void doExport(String acadyear, String semester, String examId, String gradeId,HttpServletResponse response) {
		List<ScoreInfo> scoreInfoList = new ArrayList<ScoreInfo>();
		scoreInfoList = scoreInfoList2(acadyear, semester, examId, gradeId);
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		ExamInfo examInfo = examInfoService.findOne(examId);
		int i =1;
		for(ScoreInfo item : scoreInfoList){
			Map<String,String> sMap = new HashMap<String,String>();
			sMap.put("序号", String.valueOf(i));
			sMap.put("学号", item.getStudentCode());
			sMap.put("姓名", item.getStudentName());
			sMap.put("行政班级", item.getClsName());
			sMap.put("科目", item.getSubjectName());
			String bkType="";
			String ysScore="";
			if(ScoreDataConstants.EXAM_TYPE_FINAL.equals(examInfo.getExamType()) 
					&& !BaseConstants.HW_CODE_KS.equals(item.getSubjectCode()) && !BaseConstants.HW_CODE_XZ.equals(item.getSubjectCode())){
				bkType="总评成绩";
				ysScore = item.getToScore();
			}else{
				bkType="考试成绩";
				ysScore = item.getScore();
			}
			
			sMap.put("补考成绩类型", bkType);
			sMap.put("原始成绩", ysScore);
			sMap.put("录入补考成绩", item.getResitScore());
			recordList.add(sMap);
			i++;
		}
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList();
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile("学生补考信息", titleMap, sheetName2RecordListMap, response);	
	}
	
	private String getObjectName() {
        return "学生补考信息";
	}
	
	private List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("序号");
		tis.add("学号");
		tis.add("姓名");
		tis.add("行政班级");
		tis.add("科目");
		tis.add("补考成绩类型");
		tis.add("原始成绩");
		tis.add("录入补考成绩");
		return tis;
	}
	
	@ResponseBody
	@RequestMapping(value="/resitScore/saveAll")
	public String saveAll(String examId, String gradeId,String subjectId, ResitScoreListDto resitScoreListDto){
		try{
			List<ResitScore> resitScoreList = resitScoreListDto.getResitScoreList();
			if(CollectionUtils.isNotEmpty(resitScoreList)){
				resitScoreService.saveResitScoreBy(getLoginInfo().getUnitId(), examId, gradeId, subjectId, resitScoreList);
			}
    	}catch (Exception e) {
			e.printStackTrace();
			return returnError("保存失败！", e.getMessage());
		}
		return success("保存成功！");
	}
}
