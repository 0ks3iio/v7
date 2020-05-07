package net.zdsoft.scoremanage.data.service.impl;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dao.ResitScoreDao;
import net.zdsoft.scoremanage.data.entity.ResitInfo;
import net.zdsoft.scoremanage.data.entity.ResitScore;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;
import net.zdsoft.scoremanage.data.service.ResitInfoService;
import net.zdsoft.scoremanage.data.service.ResitScoreService;
import net.zdsoft.scoremanage.data.service.ScoreInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
@Service("resitScoreService")
public class ResitScoreServiceImpl extends BaseServiceImpl<ResitScore, String> implements ResitScoreService{
    @Autowired
	private ResitScoreDao resitScoreDao;
    @Autowired
	private StudentRemoteService studentService;
    @Autowired
    private ResitInfoService resitInfoService;
    @Autowired
    private CourseRemoteService courseService;
    @Autowired
    private ClassRemoteService classService;
    @Autowired
    private ScoreInfoService scoreInfoService;
	@Override
	protected BaseJpaRepositoryDao<ResitScore, String> getJpaDao() {
		return resitScoreDao;
	}

	@Override
	protected Class<ResitScore> getEntityClass() {
		return ResitScore.class;
	}

	@Override
	public void deleteResitScoreBy(String unitId, String examId,String gradeId,
			String subjectId) {
		resitScoreDao.deleteResitScoreBy(unitId,examId,gradeId,subjectId);
	}

	@Override
	public List<ResitScore> listResitScoreBy(String unitId, String examId,
			String subjectId) {
		return resitScoreDao.listResitScoreBy(unitId,examId,subjectId);
	}
	
	@Override
	public List<ResitScore> listResitScoreBy(String unitId, String examId) {
		return resitScoreDao.listResitScoreBy(unitId,examId);
	}
	
	public List<ScoreInfo> scoreInfoList2(String unitId, String acadyear, String semester, String examId, String gradeId) {
		List<Clazz> clsList = SUtils.dt(classService.findByInGradeIds(new String[]{gradeId}), new TR<List<Clazz>>(){}); 
		Set<String> clsIdSet = new HashSet<String>();
		for(Clazz cls : clsList){
			clsIdSet.add(cls.getId());
		}
		List<ScoreInfo> scoreInfoListTemp = scoreInfoService.findListByClsIds(unitId, acadyear, semester, examId, ScoreDataConstants.ACHI_SCORE,clsIdSet.toArray(new String[0]));
        
		List<ResitInfo> resitInfoList = resitInfoService.listResitInfoBy(unitId, acadyear, semester, examId, gradeId);
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
        				if(course.getId().equals(scoreInfo.getSubjectId()) && (StringUtils.isBlank(scoreInfo.getScore()) ||Float.parseFloat(scoreInfo.getScore())<initPassMark)){
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
        	List<ResitScore> resitScoreList = listResitScoreBy(unitId, examId);
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

	@Override
	public String saveAllResitScore(String unitId, String acadyear, String semester, String examId, String gradeId,
			List<String[]> datas) {
		List<ResitInfo> resitInfoList = resitInfoService.listResitInfoBy(unitId, acadyear, semester, examId, gradeId);
		Set<String> courseIdSet = new HashSet<String>();
		Map<String, String> courseIdMap = new HashMap<String, String>();
		Map<String, Integer> courseFullMap = new HashMap<String, Integer>();
		for(ResitInfo info : resitInfoList){
			courseIdSet.add(info.getSubjectId());
		}
		if(CollectionUtils.isNotEmpty(courseIdSet)){
			List<Course> courseList = SUtils.dt(courseService.findListByIds(courseIdSet.toArray(new String[0])), new TR<List<Course>>(){}); 
			for(Course course : courseList){
				courseIdMap.put(course.getSubjectName(), course.getId());
				courseFullMap.put(course.getId(), course.getFullMark());
			}
		}
		
		List<ScoreInfo> scoreInfoList = scoreInfoList2(unitId, acadyear, semester, examId, gradeId);
		Set<String> infoSet = new HashSet<String>();
		for(ScoreInfo info : scoreInfoList){
			infoSet.add(info.getStudentId()+info.getSubjectId());
		}
		
		Map<String,Student> studentMap = new HashMap<String,Student>(); 
		List<Student> studentList=SUtils.dt(studentService.findBySchoolIdIn(null,new String[]{unitId}), new TR<List<Student>>(){});
		for(Student student : studentList){
			studentMap.put(student.getStudentCode(), student);
		}	
		Pattern pattern = Pattern.compile("^(0|[1-9]\\d{0,2})(\\.\\d{1,2})?$");
		Set<String> stuCodeIdSet = new HashSet<String>();
		for(String[] arr : datas){
			stuCodeIdSet.add(arr[1]);
		}
		Map<String, String> stuIdMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(stuCodeIdSet)){
			List<Student> stuList = SUtils.dt(studentService.findBySchIdStudentCodes(unitId,stuCodeIdSet.toArray(new String[0])), new TR<List<Student>>(){}); 
			for(Student student : stuList){
				stuIdMap.put(student.getStudentCode(), student.getId());
			}
		}
		Json importResultJson=new Json();
		List<String[]> errorDataList=new ArrayList<String[]>();
		int successCount  =0;
		String[] errorData=null;
		List<ResitScore> resitScoreList = new ArrayList<>();
		for(String[] arr : datas){
			ResitScore resitScore = new ResitScore();
			if(StringUtils.isBlank(arr[1])){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="学号";
				errorData[2]="";
				errorData[3]="学号不能为空!";
				errorDataList.add(errorData);
				continue;
        	}
			if(StringUtils.isBlank(arr[2])){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="姓名";
				errorData[2]="";
				errorData[3]="姓名不能为空!";
				errorDataList.add(errorData);
				continue;
        	}
			if(StringUtils.isBlank(arr[4])){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="科目";
				errorData[2]="";
				errorData[3]="科目不能为空!";
				errorDataList.add(errorData);
				continue;
        	}
			if(StringUtils.isNotBlank(arr[1]) && !studentMap.containsKey(arr[1])){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="学号";
				errorData[2]=arr[1];
				errorData[3]="不存在该学号!";
				errorDataList.add(errorData);
				continue;
        	}
			if(StringUtils.isNotBlank(arr[1]) && studentMap.containsKey(arr[1]) && !studentMap.get(arr[1]).getStudentName().equals(arr[2])){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="姓名";
				errorData[2]=arr[2];
				errorData[3]="姓名和学号不匹配!";
				errorDataList.add(errorData);
				continue;
        	}
			if(StringUtils.isNotBlank(arr[4]) && !courseIdMap.containsKey(arr[4])){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="科目";
				errorData[2]=arr[4];
				errorData[3]="科目不在补考范围内!";
				errorDataList.add(errorData);
				continue;
        	}
			if(StringUtils.isNotBlank(arr[7])){
				if (!pattern.matcher(arr[7]).matches()){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="录入补考成绩";
					errorData[2]=arr[7];
					errorData[3]="录入补考成绩："+arr[7]+"格式不正确(最多3位整数，2位小数)!";
					errorDataList.add(errorData);
					continue;
				}
				if (courseFullMap.get(courseIdMap.get(arr[4]))<Float.parseFloat(arr[7])){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="录入补考成绩";
					errorData[2]=arr[7];
					errorData[3]="录入补考成绩："+arr[7]+"不能大于该科目满分值"+courseFullMap.get(courseIdMap.get(arr[4]))+"!";
					errorDataList.add(errorData);
					continue;
				}
        	}
			resitScore.setId(UuidUtils.generateUuid());
			resitScore.setUnitId(unitId);
			resitScore.setExamId(examId);
			resitScore.setSubjectId(courseIdMap.get(arr[4]));
			resitScore.setStudentId(stuIdMap.get(arr[1]));
			resitScore.setGradeId(gradeId);
			if(StringUtils.isNotBlank(arr[7])){				
				resitScore.setScore(arr[7]);
			}
			if(!infoSet.contains(stuIdMap.get(arr[1])+courseIdMap.get(arr[4]))){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="科目";
				errorData[2]=arr[4];
				errorData[3]="该学生不在该科目的补考范围!";
				errorDataList.add(errorData);
				continue;
			}
			resitScoreList.add(resitScore);
			successCount++;
		}
		
		List<ResitScore> resitScores = resitScoreDao.listResitScoreByGradeId(unitId, examId, gradeId);
		Map<String, ResitScore> resitScoreMap = new HashMap<String, ResitScore>();
		for(ResitScore score : resitScores){
			resitScoreMap.put(score.getSubjectId()+score.getStudentId(), score);
		}
		List<ResitScore> resitScores2 = new ArrayList<>();
		for(ResitScore score : resitScoreList){
			if(resitScoreMap.containsKey(score.getSubjectId()+score.getStudentId())){
				ResitScore resitScore = resitScoreMap.get(score.getSubjectId()+score.getStudentId());
				resitScore.setScore(score.getScore());
				resitScores2.add(resitScore);
			}else{
				score.setId(UuidUtils.generateUuid());
				resitScores2.add(score);
			}
		}
		if(CollectionUtils.isNotEmpty(resitScores2)){
			resitScoreDao.saveAll(resitScores2);
		}
		importResultJson.put("totalCount", datas.size());
		importResultJson.put("successCount", successCount);
		importResultJson.put("errorCount", errorDataList.size());
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
	}

	@Override
	public void saveResitScoreBy(String unitId, String examId, String gradeId,
			String subjectId, List<ResitScore> resitScoreList) {
		if(CollectionUtils.isNotEmpty(resitScoreList)){
			resitScoreDao.deleteResitScoreBy(unitId,examId,gradeId,subjectId);
			for(ResitScore score : resitScoreList){
				score.setId(UuidUtils.generateUuid());
			}
			resitScoreDao.saveAll(resitScoreList);
		}		
	}

	@Override
	public List<ResitScore> findListByExamIds(String unitId, String[] examIds) {
		return resitScoreDao.findByUnitIdAndExamIdIn(unitId, examIds);
	}

}
