package net.zdsoft.comprehensive.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.comprehensive.constant.CompreStatisticsConstants;
import net.zdsoft.comprehensive.dao.CompreScoreDao;
import net.zdsoft.comprehensive.dto.CompreScoreMapDto;
import net.zdsoft.comprehensive.entity.CompreInfo;
import net.zdsoft.comprehensive.entity.CompreParameterInfo;
import net.zdsoft.comprehensive.entity.CompreScore;
import net.zdsoft.comprehensive.entity.CompreSetup;
import net.zdsoft.comprehensive.service.CompreInfoService;
import net.zdsoft.comprehensive.service.CompreParamInfoService;
import net.zdsoft.comprehensive.service.CompreScoreService;
import net.zdsoft.comprehensive.service.CompreSetupService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.ScoreInfoService;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

@Service("compreScoreService")
public class CompreScoreServiceImpl extends BaseServiceImpl<CompreScore, String> implements CompreScoreService{
	@Autowired
	private CompreScoreDao compreScoreDao;
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private ScoreInfoService scoreInfoService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private CompreParamInfoService compreParamInfoService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private CompreSetupService compreSetupService;
	@Autowired
	private CompreInfoService compreInfoService;
	@Autowired
	private SubjectInfoService subjectInfoService;
	
	@Override
	public void saveSynFirst(String unitId,String acadyear,String semester,String gradeId,String scoreManageType){
		List<ExamInfo> examInfoList=examInfoService.findListBy(new String[]{"unitId", "examType"}, new String[]{unitId, scoreManageType});
		//获取该年级对应的所有考试id 包括之前学年的 （只有高中）
		Set<String> examIds=new HashSet<String>();
		if(CollectionUtils.isNotEmpty(examInfoList)){
			for(ExamInfo examInfo : examInfoList){
				examIds.add(examInfo.getId());
			}
		}
		// 获取该年级下行政班的学生
		// Set<String> studentIds =getStudentIdsByGradeId(gradeId);
		Set<String> studentIds = new HashSet<>();
		List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId,gradeId),new TR<List<Clazz>>(){});
		Map<String,String> map = new HashMap<>();
		Set<String> classIds = new HashSet<>();
		if (CollectionUtils.isNotEmpty(classList)) {
			for (Clazz clazz : classList) {
				classIds.add(clazz.getId());
			}
			List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[0])), new TR<List<Student>>(){});
			if (CollectionUtils.isNotEmpty(studentList)){
				for(Student one : studentList){
					studentIds.add(one.getId());
					map.put(one.getId(), one.getClassId());
				}
			}
		} else {
		    return;
		}
		//获取考试ids对应的所有学生成绩
		List<ScoreInfo> scoreInfoList = scoreInfoService.findScoreByExamIds(unitId, examIds.toArray(new String[0]), ScoreDataConstants.ACHI_GRADE);
		compreScoreDao.deleteFirstList(unitId, scoreManageType, classIds.toArray(new String[0]));
        List<CompreScore> compreScoreList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(scoreInfoList)) {
			// key-(studentId+"-"+subjectId) value-该科目的最高成绩
			Map<String,ScoreInfo> studentIdToScoreMap = new HashMap<String,ScoreInfo>();
			for (ScoreInfo scoreInfo : scoreInfoList) {
				String studentId = scoreInfo.getStudentId();
				if (studentIds.contains(studentId)) {
					ScoreInfo in = studentIdToScoreMap.get(studentId+"-"+scoreInfo.getSubjectId());
					if (in == null) {
						studentIdToScoreMap.put(studentId+"-"+scoreInfo.getSubjectId(), scoreInfo);
					} else {
					    if (in.getScore().charAt(0) > scoreInfo.getScore().charAt(0)) {
                            studentIdToScoreMap.put(studentId+"-"+scoreInfo.getSubjectId(), scoreInfo);
                        }
                    }
				}
			}
			for (Entry<String, ScoreInfo> one : studentIdToScoreMap.entrySet()) {
			    CompreScore compreScore = new CompreScore();
			    compreScore.setId(UuidUtils.generateUuid());
			    compreScore.setUnitId(unitId);
			    compreScore.setAcadyear(acadyear);
			    compreScore.setSemester(semester);
			    compreScore.setExamId(BaseConstants.ZERO_GUID);
			    compreScore.setCompreInfoId(BaseConstants.ZERO_GUID);
			    compreScore.setSubjectId(one.getValue().getSubjectId());
			    compreScore.setClassId(one.getValue().getClassId());
			    compreScore.setStudentId(one.getValue().getStudentId());
			    compreScore.setScore(one.getValue().getScore());
			    compreScore.setScoremanageType(scoreManageType);
			    compreScore.setCreationTime(new Date());
			    compreScore.setModifyTime(new Date());
			    compreScoreList.add(compreScore);
			}
		}
        if(CollectionUtils.isNotEmpty(compreScoreList)){
            saveAll(compreScoreList.toArray(new CompreScore[0]));
        }
	}
	@Override
	public void saveAll(String unitId,String acadyear,String semester,String gradeId,String classId,String userId,
				List<CompreScore> scoreList){
		Set<String> classIds=new HashSet<String>();
		if(StringUtils.isBlank(classId)){
			List<Clazz> classList=SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId,gradeId),new TR<List<Clazz>>(){});
			if(CollectionUtils.isNotEmpty(classList)){
				for(Clazz clazz:classList){
					classIds.add(clazz.getId());
				}
			}
		}else{
			classIds.add(classId);
		}
		if(CollectionUtils.isNotEmpty(scoreList)){
			//先删除
			compreScoreDao.deleteFirstList(unitId, "",classIds.toArray(new String[0]));
			/*Map<String,String> subjectIdMap=EntityUtils.getMap(SUtils.dt(courseRemoteService.findBySubjectUnitId(unitId),
					new TR<List<Course>>(){}),"subjectName","id");*/
			List<CompreScore> inserList=new ArrayList<CompreScore>();
//			for(CompreScore compreScore:scoreList){
//				Map<String,String> scoreMap=compreScore.getScoreMap();
//				Set<Entry<String,String>> entries=scoreMap.entrySet();
//				for(Entry<String,String> entry:entries){
//					if(StringUtils.isBlank(entry.getValue())){
//						continue;
//					}
//					CompreScore insertScore=new CompreScore();
//					insertScore.setId(UuidUtils.generateUuid());
//					insertScore.setUnitId(unitId);
//					insertScore.setAcadyear(acadyear);
//					insertScore.setSemester(semester);
//					insertScore.setExamId(BaseConstants.ZERO_GUID);
//					insertScore.setSubjectId(entry.getKey());
//					insertScore.setClassId(compreScore.getClassId());
//					insertScore.setClassType(CompreStatisticsConstants.CLASS_TYPE_1);
//					insertScore.setStudentId(compreScore.getStudentId());
//					insertScore.setScore(entry.getValue());
//					insertScore.setOperatorId(userId);
//					insertScore.setInputType(ScoreDataConstants.ACHI_GRADE);
//					insertScore.setScoremanageType(CompreStatisticsConstants.SCORE_XKFS);
//					insertScore.setCreationTime(new Date());
//					inserList.add(insertScore);
//				}
//			}
			if(CollectionUtils.isNotEmpty(inserList)){
				saveAll(inserList.toArray(new CompreScore[0]));
			}
		}
	}
	@Override
	public List<CompreScore> getFirstList(String unitId,String acadyear,String semester,String scoreType,String[] classIds){
		if(classIds==null || classIds.length==0){
			return null;
		}
		return compreScoreDao.getFirstList(unitId, acadyear, semester, "",classIds);
	}
	@Override
	public String doImport(String unitId,String acadyear,String semester,String params,List<String[]> datas){
		//原始成绩导入数据处理
		Json importResultJson=new Json();
		List<String[]> errorDataList=new ArrayList<String[]>();
		//int successCount  =0;
		JSONObject obj = JSON.parseObject(params,JSONObject.class);
		String gradeId=obj.getString("gradeId");
		String classId=obj.getString("classId");
		
		String[] classIds=new String[]{classId};
		if(StringUtils.isBlank(classId)){
			classIds=EntityUtils.getList(SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId,gradeId),new TR<List<Clazz>>(){})
					,"id").toArray(new String[0]);
		}
		Map<String,String> classNameMap=EntityUtils.getMap(SUtils.dt(classRemoteService.findListByIds(classIds),new TR<List<Clazz>>(){}),"classNameDynamic","id");
		
		List<Student> studentList=SUtils.dt(studentRemoteService.findByClassIds(classIds),new TR<List<Student>>(){});
		//Map<String,Course> courseMap=EntityUtils.getMap(SUtils.dt(courseRemoteService.findByIds(CompreStatisticsConstants.SUBJECT_CODES),new TR<List<Course>>(){}), "id");
		List<Course> courseList=SUtils.dt(courseRemoteService.findByCodes73(unitId),new TR<List<Course>>(){});
		if(courseList != null)
			courseList.addAll(SUtils.dt(courseRemoteService.findByCodesYSY(unitId),new TR<List<Course>>(){}));
		
		//key - studentCode
		Map<String,Student> studentMap = EntityUtils.getMap(studentList,"studentCode"); 
		
		//既有的成绩
		List<CompreScore> scoreList=getFirstList(unitId, acadyear, semester, "",classIds);
		Map<String,CompreScore> scoreMap=new HashMap<String, CompreScore>();
		if(CollectionUtils.isNotEmpty(scoreList)){
			for(CompreScore score:scoreList){
				scoreMap.put(score.getStudentId()+"_"+score.getSubjectId(),score);
			}
		}
		List<CompreScore> inserList=new ArrayList<CompreScore>();
		
		Map<String,String> testStuMap=new HashMap<String, String>();//判断学生是否重复
		CompreScore score = null;
		String[] errorData=null;//new String[4]
		for(int i =0;i< datas.size();i++){
			String[] dataArr = datas.get(i);
			int length=dataArr.length;
			String studentCode=length>0?dataArr[0]:"";
			if(StringUtils.isNotBlank(studentCode)){
				studentCode=studentCode.trim();
			}else{
				studentCode="";
			}
			Student stu=studentMap.get(studentCode);
			if(stu==null){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="学号";
				errorData[2]=studentCode;
				errorData[3]="学号有误";
				errorDataList.add(errorData);
				continue;
			}
			String studentName=length>1?dataArr[1]:"";
			if(StringUtils.isNotBlank(studentName)){
				studentName=studentName.trim();
			}else{
				studentName="";
			}
			if(!stu.getStudentName().equals(studentName)){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="姓名";
				errorData[2]=studentName;
				errorData[3]="学生姓名有误";
				errorDataList.add(errorData);
				continue;
			}
			if(StringUtils.isNotBlank(testStuMap.get(stu.getId()))){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="姓名";
				errorData[2]=studentName;
				errorData[3]="学生重复";
				errorDataList.add(errorData);
				continue;
			}
			testStuMap.put(stu.getId(), "one");
			
			String className=length>2?dataArr[2]:"";
			if(StringUtils.isNotBlank(className)){
				className=className.trim();
			}else{
				className="";
			}
			String stuClassId=classNameMap.get(className);
			if(StringUtils.isBlank(stuClassId) || !stuClassId.equals(stu.getClassId())){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="班级";
				errorData[2]=studentName;
				errorData[3]="班级名称有误";
				errorDataList.add(errorData);
				continue;
			}
			int j=0;
			for(int l=3;l<length;l++){
				String scoreStr=dataArr[l];
				Course course=courseList.get(j);
				j++;
				if(StringUtils.isNotBlank(scoreStr)){
					scoreStr=scoreStr.trim();
				}else{
					continue;
				}
				if(scoreStr.length()>1){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]=course.getSubjectName();
					errorData[2]=scoreStr;
					errorData[3]="该科目对应的等第有误";
					errorDataList.add(errorData);
					break;
				}
				score=scoreMap.get(stu.getId()+"_"+course.getId());
				if(score!=null){
					score.setClassId(stuClassId);
					score.setScore(scoreStr);
					score.setModifyTime(new Date());
				}else{
					score=new CompreScore();
					score.setId(UuidUtils.generateUuid());
					score.setUnitId(unitId);
					score.setAcadyear(acadyear);
					score.setSemester(semester);
					score.setExamId(BaseConstants.ZERO_GUID);
					score.setSubjectId(course.getId());
					score.setClassId(stuClassId);
					score.setStudentId(stu.getId());
					score.setScore(scoreStr);
					score.setScoremanageType("");
					score.setCreationTime(new Date());
				}
				inserList.add(score);
			}
		}
		//compreScoreDao.deleteFirstList(unitId, acadyear, semester, CompreStatisticsConstants.SCORE_XKFS,classIds);
		saveAll(inserList.toArray(new CompreScore[0]));
		importResultJson.put("totalCount", datas.size());
		importResultJson.put("successCount", datas.size()-errorDataList.size());
		importResultJson.put("errorCount",errorDataList.size());
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
	}

	@Override
	public List<CompreScoreMapDto> getFirstOrFinallyList(String unitId, String scoreManageType, String compreInfoId, String[] classIds, boolean isFirst, Pagination page){
		List<CompreScoreMapDto> lastScoreList=new ArrayList<>();
		if(classIds==null || classIds.length==0){
			return lastScoreList;
		}
        Map<String,String> classNameMap =EntityUtils.getMap(SUtils.dt(classRemoteService.findListByIds(classIds),new TR<List<Clazz>>(){}),"id","classNameDynamic");
		List<Student> studentList= Student.dt(studentRemoteService.findByClassIds(classIds,SUtils.s(page)), page);
		if(CollectionUtils.isNotEmpty(studentList)) {
		    List<String> studentIds = EntityUtils.getList(studentList, Student::getId);
            List<CompreScore> scoreList = null;
            if (CompreStatisticsConstants.TYPE_XK.equals(scoreManageType)) {
                scoreList = compreScoreDao.getByStuIds(unitId, scoreManageType, studentIds.toArray(new String[0]));
            } else {
                scoreList = compreScoreDao.findByCompreInfoIdAndScoremanageType(compreInfoId, scoreManageType);
            }
            //key-studentId   value-成绩list
            Map<String, List<CompreScore>> scoreMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(scoreList)) {
                for (CompreScore score : scoreList) {
                    List<CompreScore> inList = scoreMap.get(score.getStudentId());
                    if (CollectionUtils.isEmpty(inList)) {
                        inList = new ArrayList<>();
                        inList.add(score);
                        scoreMap.put(score.getStudentId(), inList);
                    } else {
                        inList.add(score);
                    }
                }
            }
            //获取 key-subjectId  value-subjectName
			/*Map<String,String> subjectNameMap=EntityUtils.getMap(SUtils.dt(courseRemoteService.findByIds(subjectIds.toArray(new String[0])),
					new TR<List<Course>>(){}),"id","subjectName");*/

            //Map<String,Student> studentMap=EntityUtils.getMap(studentList, "id");
            //最后封装
            for (Student one : studentList) {
                String studentId = one.getId();
                CompreScoreMapDto lastScore = new CompreScoreMapDto();
                lastScore.setStudentId(studentId);
                lastScore.setStudentCode(one.getStudentCode());
                lastScore.setStudentName(one.getStudentName());
                lastScore.setClassName(classNameMap.get(one.getClassId()));

                List<CompreScore> inList = scoreMap.get(studentId);
                if (CollectionUtils.isNotEmpty(inList)) {
                    Map<String, String> map = new HashMap<String, String>();
                    if (isFirst) {
                        for (CompreScore score : inList) {
                            if (CompreStatisticsConstants.TYPE_OVERALL.equals(scoreManageType)) {
                                map.put(score.getSubjectId(), String.valueOf(score.getToScore()));
                                if (BaseConstants.ZERO_GUID.equals(score.getSubjectId())) {
                                    lastScore.setRanking(score.getRanking());
                                }
                            } else if (CompreStatisticsConstants.TYPE_ENGLISH.equals(scoreManageType)) {
                                map.put(CompreStatisticsConstants.SUBJECT_CODE_YY_3, String.valueOf(score.getToScore()));
                                lastScore.setRanking(score.getRanking());
                            } else if (CompreStatisticsConstants.TYPE_ENG_SPEAK.equals(scoreManageType)) {
                                map.put(CompreStatisticsConstants.SUBJECT_CODE_KS, String.valueOf(score.getScore()));
                                lastScore.setRanking(score.getRanking());
                            } else if (CompreStatisticsConstants.TYPE_GYM.equals(scoreManageType)) {
                                map.put(CompreStatisticsConstants.SUBJECT_CODE_TY_3, String.valueOf(score.getToScore()));
                                lastScore.setRanking(score.getRanking());
                            } else {
                                map.put(score.getSubjectId(), score.getScore());
                            }
                        }
                    }
                    lastScore.setScoreMap(map);
                }
                lastScoreList.add(lastScore);
            }
        }
		return lastScoreList;
	}
	//得到班级名称map
	public Map<String,String> getClassNameMap(String[] classIds){
		Map<String,String> classNameMap =EntityUtils.getMap(SUtils.dt(classRemoteService.findListByIds(classIds),new TR<List<Clazz>>(){}),"id","classNameDynamic");
		return  classNameMap;
	}
	//得到学生map
	public Map<String,Student> getStudentMap(String[] classIds){
		Map<String,Student> studentMap=EntityUtils.getMap(SUtils.dt(studentRemoteService.findByClassIds(classIds),new TR<List<Student>>(){}),"id");
		return  studentMap;
	}
	@Override
	protected BaseJpaRepositoryDao<CompreScore, String> getJpaDao() {
		return compreScoreDao;
	}

	@Override
	protected Class<CompreScore> getEntityClass() {
		return CompreScore.class;
	}

	@Override
	public List<CompreScore> findByExamIdSubjectIdIn(String unitId,
			String examId,String type, String[] subjectIds,Pagination page) {
		if(subjectIds==null || subjectIds.length<=0){
			return new ArrayList<CompreScore>();
		}
		if (page == null) {
			return compreScoreDao.findByExamIdSubjectIdIn(unitId,examId,type,subjectIds);
		} else {
			page.setMaxRowCount(compreScoreDao.getSizeByExamIdSubjectIdIn(unitId,examId,type,subjectIds));
			return compreScoreDao.findByExamIdSubjectIdIn(unitId,examId,type,subjectIds,Pagination.toPageable(page));
		}
	}

	@Override
	public List<CompreScore> findByStudentId(String unitId, String acadyear,String semester,String type,
			String[] studentIds) {
		if(studentIds==null || studentIds.length<=0){
			return new ArrayList<CompreScore>();
		}
		if(studentIds.length<=1000){
			return compreScoreDao.findByStudentIds(unitId,acadyear,semester,type,studentIds);
		}else{
			List<CompreScore> returnList = new ArrayList<CompreScore>();
			int length = studentIds.length;
			int cyc = length / 1000 + (length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > length)
					max = length;
				String[] stuId = ArrayUtils.subarray(studentIds, i * 1000, max);
				compreScoreDao.findByStudentIds(unitId,acadyear,semester,type,stuId);
			}
			return returnList;
		}
		
	}
	
	@Override
	public void updateParamInfoList(String unitId, String infoKey) {
		List<CompreParameterInfo> infoList = compreParamInfoService.findByInfoKey(unitId, infoKey);
		if(CollectionUtils.isEmpty(infoList)){
			List<CompreParameterInfo> baseInfoList = compreParamInfoService.findByInfoKey(BaseConstants.ZERO_GUID,infoKey);
			if(CollectionUtils.isNotEmpty(baseInfoList)){
				List<CompreParameterInfo> insertList = new ArrayList<CompreParameterInfo>();
				for (CompreParameterInfo compreParameterInfo : baseInfoList) {
					CompreParameterInfo insertInfo = new CompreParameterInfo();
					insertInfo.setId(UuidUtils.generateUuid());
					insertInfo.setUnitId(unitId);
					insertInfo.setInfoKey(infoKey);
					insertInfo.setGradeScore(compreParameterInfo.getGradeScore());
					insertInfo.setScore(compreParameterInfo.getScore());
					insertList.add(insertInfo);
				}
				if(CollectionUtils.isNotEmpty(insertList)){
					compreParamInfoService.saveAll(insertList.toArray(new CompreParameterInfo[0]));
				}
			}
		}
		
	}

	@Override
	public void saveConvertFirst(String unitId, String acadyear, String semester,
			String gradeId, String userId) {
		List<Clazz> classList=SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId,gradeId),new TR<List<Clazz>>(){});
		if(CollectionUtils.isEmpty(classList)){
			return;
		}
		List<String> classIds = EntityUtils.getList(classList, "id");
		//删除该年级原来所有总分
		compreScoreDao.deleteBySubjectIdAndClassIdIn(unitId, acadyear, semester, BaseConstants.ZERO_GUID,"",classIds.toArray(new String[0]));
		
		List<CompreScore> scoreList=compreScoreDao.getFirstList(unitId, acadyear, semester, "",classIds.toArray(new String[0]));
		if(CollectionUtils.isNotEmpty(scoreList)){
			List<CompreParameterInfo> compreParamInfoList = compreParamInfoService.findByInfoKey(unitId,CompreStatisticsConstants.INFO_KEY_5);
			Map<String, Float> gradeScoreMap = EntityUtils.getMap(compreParamInfoList, "gradeScore","score");
			//分数转换
			for (CompreScore compreScore : scoreList) {
				if(gradeScoreMap.containsKey(compreScore.getScore())){
					compreScore.setToScore(gradeScoreMap.get(compreScore.getScore()));
				}else{
					compreScore.setToScore(0f);
				}
			}
			//按科目排名
			//key-subjectId   value-成绩list
			Map<String,List<CompreScore>> subScoreMap = new HashMap<String,List<CompreScore>>();
			for(CompreScore score:scoreList){
				List<CompreScore> subList=subScoreMap.get(score.getSubjectId());
				if(CollectionUtils.isEmpty(subList)){
					subList=new ArrayList<CompreScore>();
				}
				subList.add(score);
				subScoreMap.put(score.getSubjectId(), subList);
			}
			Set<Entry<String, List<CompreScore>>> entrySet = subScoreMap.entrySet();
			for (Entry<String, List<CompreScore>> entry : entrySet) {
				List<CompreScore> inList = subScoreMap.get(entry.getKey());
				if(CollectionUtils.isNotEmpty(inList)){
					makeRanking(inList);
				}
			}
			
			//key-studentId   value-成绩list
			Map<String,List<CompreScore>> stuScoreMap=new HashMap<String, List<CompreScore>>();
			for(CompreScore score:scoreList){
				List<CompreScore> inList=stuScoreMap.get(score.getStudentId());
				if(CollectionUtils.isEmpty(inList)){
					inList=new ArrayList<CompreScore>();
				}
				inList.add(score);
				stuScoreMap.put(score.getStudentId(), inList);
			}
			
			//计算总分
			Set<Entry<String, List<CompreScore>>> entries=stuScoreMap.entrySet();
			List<CompreScore> totalScoreList = new ArrayList<CompreScore>();
			for(Entry<String, List<CompreScore>> entry:entries){
				List<CompreScore> inList=stuScoreMap.get(entry.getKey());
				if(CollectionUtils.isNotEmpty(inList)){
					CompreScore totalScore = new CompreScore();
					totalScore.setId(UuidUtils.generateUuid());
					totalScore.setUnitId(unitId);
					totalScore.setAcadyear(acadyear);
					totalScore.setSemester(semester);
					totalScore.setExamId(inList.get(0).getExamId());
					totalScore.setSubjectId(BaseConstants.ZERO_GUID);
					totalScore.setClassId(inList.get(0).getClassId());
					totalScore.setStudentId(entry.getKey());
					totalScore.setScore("");
					totalScore.setScoremanageType("");
					totalScore.setCreationTime(new Date());
					Float totalScoreNum = 0f;
					for (CompreScore compreScore : inList) {
						totalScoreNum+=compreScore.getToScore();
					}
					totalScore.setToScore(totalScoreNum);
					totalScoreList.add(totalScore);
				}
			}
			if(CollectionUtils.isNotEmpty(totalScoreList)){
				//年级总分排名
				makeRanking(totalScoreList);
		
				saveAll(totalScoreList.toArray(new CompreScore[0]));
			}
			
			
			
		}
	}

	//按分数排名
	private void makeRanking(List<CompreScore> scoreList) {
		Collections.sort(scoreList,new Comparator<CompreScore>(){

			@Override
			public int compare(CompreScore o1, CompreScore o2) {
				if(o1.getToScore()==null || o2.getToScore()==null)return 0;
				float mm = o2.getToScore()-o1.getToScore();
				if(mm>0){
					return 1;
				}else if(mm<0){
					return -1;
				}else{
					return 0;
				}
			}
		});
		float before=0;
		int classNum=0;
		int ranking=0;
		for (CompreScore c: scoreList){
			if(c.getToScore()==null){
				continue;
			}
			if(ranking==0){
				before=c.getToScore();
				ranking=1;
				classNum=1;
			}else{
				if(before>c.getToScore()){
					ranking=classNum+1;
					before=c.getToScore();
				}
				classNum++;
			}
			c.setRanking(ranking);
		}
	}
	@Override
	public List<CompreScore> findByExamIdIn(String unitId, String scoremanageType,
			String[] examIds) {
		return compreScoreDao.findByExamIdIn(unitId, scoremanageType, examIds);
	}
	@Override
	public void saveAllScores(String unitId, String searchAcadyear,
			String searchSemester, String examId, 
			String userId, List<CompreScore> compreScores) {
		for (CompreScore compreScore : compreScores) {
			compreScore.setId(UuidUtils.generateUuid());
			compreScore.setAcadyear(searchAcadyear);
			compreScore.setSemester(searchSemester);
			compreScore.setExamId(examId);
			compreScore.setUnitId(unitId);
			compreScore.setCreationTime(new Date());
			compreScore.setModifyTime(new Date());
			compreScore.setScoremanageType("");
		}
		if (CollectionUtils.isNotEmpty(compreScores)) {
			saveAll(compreScores.toArray(new CompreScore[0]));
		}
	}
	@Override
	public List<CompreScore> findExamAllScores(String searchAcadyear,
			String searchSemester, String unitId, 
			String inputType, String scoresType,String[] examIds) {
		List<CompreScore> CompreScoreList = new ArrayList<CompreScore>();
		if (examIds.length != 0) {
			CompreScoreList = compreScoreDao.findExamAllScores(searchAcadyear,searchSemester,unitId,inputType,scoresType,examIds);
		}
		return CompreScoreList;
	}

	@Override
	public void deleteAndSaveScoreSave(String unitId, String comInfoId,
			String searchAcadyear, String searchSemester, String userId,
			List<CompreScore> compreScores, String[] studentIds) {
		compreScoreDao.deleteByIdAndSubIdAndType(unitId, comInfoId, ScoreDataConstants.ZERO32, "");
		if (studentIds.length != 0) {
			compreScoreDao.deleteByIdsAndType(unitId,comInfoId,"",studentIds);
		}
		saveAllScores(unitId, searchAcadyear, searchSemester, comInfoId, userId, compreScores);
	}
	@Override
	public void deleteAndSaveScoreRank(String englishId, String englishOralTestId,String unitId, String comInfoId,
			String searchAcadyear, String searchSemester, String userId,
			List<CompreScore> compreScores, List<CompreScore> englishScoreList, List<CompreScore> englishOralTestScoreList) {
		compreScoreDao.deleteByIdAndSubIdAndType(unitId, comInfoId, ScoreDataConstants.ZERO32, "");
		saveAllScores(unitId, searchAcadyear, searchSemester, comInfoId, userId, compreScores);
		if (StringUtils.isNotBlank(englishId)) {
			compreScoreDao.deleteByIdAndSubIdAndType(unitId, comInfoId, englishId, "");
			saveAllScores(unitId, searchAcadyear, searchSemester, comInfoId, userId, englishScoreList);
		}
		if (StringUtils.isNotBlank(englishOralTestId)) {
			compreScoreDao.deleteByIdAndSubIdAndType(unitId, comInfoId, englishOralTestId, "");
			saveAllScores(unitId, searchAcadyear, searchSemester, comInfoId, userId, englishOralTestScoreList);
		}
	}
	@Override
	public String zHSZImport(String unitId, String examId, String gradeId, String operatorId,
			List<String[]> datas) {
		Pattern pattern = Pattern.compile("^(0|[1-9]\\d{0,2})(\\.\\d{1,2})?$");
		
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
		String gradeName = grade.getGradeName();
		ExamInfo examInfo = examInfoService.findListByIds(new String[]{examId}).get(0);
		String acadyear = examInfo.getAcadyear();
		String semester = examInfo.getSemester();
		Json importResultJson=new Json();
		List<String[]> errorDataList=new ArrayList<String[]>();
		int successCount  =0;
		String[] errorData=null;
		Set<String> stuCodeSet = new HashSet<String>();
		Set<String> clsCodeSet = new HashSet<String>();
		Set<String> courseCodeSet = new HashSet<String>();
		Set<String> teachClsNameSet = new HashSet<String>();
		List<CompreScore> insertList=new ArrayList<CompreScore>();
		for(String[] arr : datas){
			if(null != arr[1]){
				stuCodeSet.add(arr[1]);
			}
			if(null != arr[3]){
				clsCodeSet.add(arr[3]);
			}
			if(null != arr[5]){
				courseCodeSet.add(arr[5]);
			}
			if(null != arr[2]){
				teachClsNameSet.add(arr[2]);
			}
        }
		List<Student> stuList = new ArrayList<Student>();
		if(CollectionUtils.isNotEmpty(stuCodeSet)){
			stuList = SUtils.dt(studentRemoteService.findBySchIdStudentCodes(unitId, stuCodeSet.toArray(new String[0])), new TR<List<Student>>() {});
		}
		List<Clazz> clsList = new ArrayList<Clazz>();
		if(CollectionUtils.isNotEmpty(clsCodeSet)){
			clsList = SUtils.dt(classRemoteService.findByClassCode(unitId, clsCodeSet.toArray(new String[0])), new TR<List<Clazz>>() {});
		}
        Map<String, String> clsCodeNameMap = new HashMap<String, String>();
        Map<String, String> clsCodeIdMap = new HashMap<String, String>();
        Map<String, String> clsGradeIdMap = new HashMap<String, String>();
        for(Clazz cls : clsList){
        	clsCodeNameMap.put(cls.getClassCode(), cls.getClassNameDynamic());
        	clsCodeIdMap.put(cls.getClassCode(), cls.getId());
        	clsGradeIdMap.put(cls.getClassCode(), cls.getGradeId());
		}
        Map<String, String> stuCodeNameMap = new HashMap<String, String>();
        Map<String, String> stuCodeIdMap = new HashMap<String, String>();
		for(Student stu : stuList){
			stuCodeNameMap.put(stu.getStudentCode(), stu.getStudentName());
			stuCodeIdMap.put(stu.getStudentCode(), stu.getId());
        }
		List<Course> courseList = new ArrayList<Course>();
		if(CollectionUtils.isNotEmpty(courseCodeSet)){
			courseList = SUtils.dt(courseRemoteService.findByUnitCourseCodes(unitId, courseCodeSet.toArray(new String[0])), new TR<List<Course>>(){});
		}
		Map<String, String> courseCodeNameMap = new HashMap<String, String>();
        Map<String, String> courseCodeIdMap = new HashMap<String, String>();
		for(Course course : courseList){
			courseCodeNameMap.put(course.getSubjectCode(), course.getSubjectName());
			courseCodeIdMap.put(course.getSubjectCode(), course.getId());
		}
		//获取以勾选科目
		Set<String> courseIdSet = new HashSet<String>();
		List<Course> courseList2 = new ArrayList<Course>();
		if(CollectionUtils.isNotEmpty(courseIdSet)){
			courseList2 = SUtils.dt(courseRemoteService.findBySubjectIdIn(courseIdSet.toArray(new String[]{})),new TR<List<Course>>() {});
		}
		Set<String> checkCourseSet = new HashSet<String>();
		for(Course course : courseList2){
			checkCourseSet.add(course.getSubjectCode());
		}
		
		//获取教学班
		List<TeachClass> teachClassList = new ArrayList<TeachClass>();
		if(CollectionUtils.isNotEmpty(teachClsNameSet)){
			teachClassList = SUtils.dt(teachClassRemoteService.findByNames(unitId, teachClsNameSet.toArray(new String[0])), new TR<List<TeachClass>>(){});
		}
		Map<String, String> teachClassNameIdMap = new HashMap<String, String>();
        Map<String, String> teachClassNameGradeIdMap = new HashMap<String, String>();
        Map<String, String> teachClassNameSubjectIdMap = new HashMap<String, String>();
        Map<String, String> teachClassNameClassTypeMap = new HashMap<String, String>();
		for(TeachClass teachClass : teachClassList){
			teachClassNameIdMap.put(teachClass.getName(), teachClass.getId());
			teachClassNameGradeIdMap.put(teachClass.getName(), teachClass.getGradeId());
			teachClassNameSubjectIdMap.put(teachClass.getName(), teachClass.getCourseId());
			teachClassNameClassTypeMap.put(teachClass.getName(), teachClass.getClassType());
        }
		
		//获取表里已有数据
		List<CompreScore> compreScoreList = compreScoreDao.findByExamIdIn(unitId, "", new String[]{examId});
		
		for(String[] arr : datas){
			CompreScore compreScore = new CompreScore();
			//姓名
			if(StringUtils.isBlank(arr[0])){
        		errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="学生姓名";
				errorData[2]="";
				errorData[3]="学生姓名不能为空";
				errorDataList.add(errorData);
				continue;
        	}
			if(StringUtils.isBlank(arr[1])){
        		errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="学号";
				errorData[2]="";
				errorData[3]="学号不能为空";
				errorDataList.add(errorData);
				continue;
        	}else{
        		if(StringUtils.isBlank(stuCodeNameMap.get(arr[1]))){
        			errorData = new String[4];
    				errorData[0]=errorDataList.size()+1+"";
    				errorData[1]="学号";
    				errorData[2]=arr[1];
    				errorData[3]="不存在该学号所属的学生";
    				errorDataList.add(errorData);
    				continue;
        		}else{
        			if(StringUtils.isNotBlank(arr[0]) && !arr[0].equals(stuCodeNameMap.get(arr[1]))){
        				errorData = new String[4];
        				errorData[0]=errorDataList.size()+1+"";
        				errorData[1]="学号";
        				errorData[2]="姓名："+arr[0]+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;学号："+arr[1];
        				errorData[3]="学生姓名与该学号不匹配";
        				errorDataList.add(errorData);
        				continue;
        			}
        		}
        	}
			
			///学科
			if(StringUtils.isBlank(arr[4])){
        		errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="科目名称";
				errorData[2]="";
				errorData[3]="科目名称不能为空";
				errorDataList.add(errorData);
				continue;
        	}
			if(StringUtils.isBlank(arr[5])){
        		errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="科目编号";
				errorData[2]="";
				errorData[3]="科目编号不能为空";
				errorDataList.add(errorData);
				continue;
        	}else{
        		if(!checkCourseSet.contains(arr[5])){
        			errorData = new String[4];
    				errorData[0]=errorDataList.size()+1+"";
    				errorData[1]="科目编号";
    				errorData[2]=arr[1];
    				errorData[3]="该科目编号所属科目在科目设置没有被选择！";
    				errorDataList.add(errorData);
    				continue;
        		}
        		if(StringUtils.isBlank(courseCodeNameMap.get(arr[5]))){
        			errorData = new String[4];
    				errorData[0]=errorDataList.size()+1+"";
    				errorData[1]="科目编号";
    				errorData[2]=arr[1];
    				errorData[3]="不存在该科目编号所属的科目名称";
    				errorDataList.add(errorData);
    				continue;
        		}else{
        			if(StringUtils.isNotBlank(arr[4]) && !arr[4].equals(courseCodeNameMap.get(arr[5]))){
        				errorData = new String[4];
        				errorData[0]=errorDataList.size()+1+"";
        				errorData[1]="科目编号";
        				errorData[2]="科目名称："+arr[4]+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;科目编号："+arr[5];
        				errorData[3]="科目名称与该科目编号不匹配";
        				errorDataList.add(errorData);
        				continue;
        			}
        			compreScore.setSubjectId(courseCodeIdMap.get(arr[5]));
        		}
        	}
			
			//班级
			if(StringUtils.isBlank(arr[2])){
        		errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="班级名称";
				errorData[2]="";
				errorData[3]="班级名称不能为空";
				errorDataList.add(errorData);
				continue;
        	}
			if(StringUtils.isBlank(arr[3])){
				if(StringUtils.isBlank(teachClassNameIdMap.get(arr[2]))){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="教学班名称";
					errorData[2]="";
					errorData[3]="根据该教学班名称找不到对应的教学班";
					errorDataList.add(errorData);
					continue;
				}else{
					if(!gradeId.equals(teachClassNameGradeIdMap.get(arr[3]))){
						errorData = new String[4];
	    				errorData[0]=errorDataList.size()+1+"";
	    				errorData[1]="教学班名称";
	    				errorData[2]=arr[1];
	    				errorData[3]="该教学班名称所属教学班不在"+gradeName+"年级范围";
	    				errorDataList.add(errorData);
	    				continue;
					}
					if(!compreScore.getSubjectId().equals(teachClassNameSubjectIdMap.get(arr[3]))){
						errorData = new String[4];
	    				errorData[0]=errorDataList.size()+1+"";
	    				errorData[1]="教学班名称";
	    				errorData[2]=arr[1];
	    				errorData[3]="该教学班名称所属教学班对应的科目不在与导入科目不一致";
	    				errorDataList.add(errorData);
	    				continue;
					}
					compreScore.setClassId(clsCodeIdMap.get(teachClassNameIdMap.get(arr[2])));
				}
        	}else{
        		if(!gradeId.equals(clsGradeIdMap.get(arr[3]))){
        			errorData = new String[4];
    				errorData[0]=errorDataList.size()+1+"";
    				errorData[1]="班级编号";
    				errorData[2]=arr[1];
    				errorData[3]="该班级编号所属班级不在"+gradeName+"年级范围";
    				errorDataList.add(errorData);
    				continue;
        		}
        		if(StringUtils.isBlank(clsCodeIdMap.get(arr[3]))){
        			errorData = new String[4];
    				errorData[0]=errorDataList.size()+1+"";
    				errorData[1]="班级编号";
    				errorData[2]=arr[1];
    				errorData[3]="不存在该班级编号所属的班级";
    				errorDataList.add(errorData);
    				continue;
        		}else{
        			if(StringUtils.isNotBlank(arr[2]) && !arr[2].equals(clsCodeNameMap.get(arr[3]))){
        				errorData = new String[4];
        				errorData[0]=errorDataList.size()+1+"";
        				errorData[1]="班级编号";
        				errorData[2]="班级名称："+arr[2]+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;班级编号："+arr[3];
        				errorData[3]="班级名称与该班级编号不匹配";
        				errorDataList.add(errorData);
        				continue;
        			}
        			compreScore.setClassId(clsCodeIdMap.get(arr[3]));
        		}
        	}
			
			if(StringUtils.isBlank(arr[6])){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="卷面分";
				errorData[2]=arr[1];
				errorData[3]="卷面分不能为空";
				errorDataList.add(errorData);
				continue;
			}else{
				if (!pattern.matcher(arr[6]).matches()){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="卷面分";
					errorData[2]=arr[1];
					errorData[3]="卷面分："+arr[6]+"格式不正确(最多3位整数，2位小数)!";
					errorDataList.add(errorData);
					continue;
				}
			}
			
			if(StringUtils.isNotBlank(arr[7]) && !pattern.matcher(arr[7]).matches()){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="实分";
				errorData[2]=arr[1];
				errorData[3]="实分："+arr[7]+"格式不正确(最多3位整数，2位小数)!";
				errorDataList.add(errorData);
				continue;
			}
			
			compreScore.setAcadyear(acadyear);
			compreScore.setSemester(semester);
			compreScore.setExamId(examId);
			compreScore.setUnitId(unitId);
			compreScore.setScore(arr[6]);
			compreScore.setStudentId(stuCodeIdMap.get(arr[1]));
			compreScore.setId(UuidUtils.generateUuid());
			if(StringUtils.isNotBlank(arr[7])){
				compreScore.setToScore(Float.parseFloat(arr[7]));
			}
			compreScore.setCreationTime(new Date());
			compreScore.setModifyTime(new Date());
			compreScore.setScoremanageType("");
			insertList.add(compreScore);
        	successCount++;
		}	
		List<CompreScore> ins = new ArrayList<CompreScore>();
		if(CollectionUtils.isNotEmpty(insertList)){
            for(CompreScore item1 : insertList){
            	CompreScore c = new CompreScore();
            	for(CompreScore item2 : compreScoreList){
            		if(item1.getAcadyear().equals(item2.getAcadyear()) && item1.getSemester().equals(item2.getSemester()) &&
            				item1.getExamId().equals(item2.getExamId()) && item1.getSubjectId().equals(item2.getSubjectId()) &&
            				item1.getClassId().equals(item2.getClassId()) && item1.getStudentId().equals(item2.getStudentId())){
            			item2.setScore(item1.getScore());
            			item2.setToScore(item1.getToScore());
            			c = item2;
            		}
            	}
            	if(StringUtils.isBlank(c.getId())){
        			c = item1;
        		}
            	ins.add(c);
            }
            compreScoreDao.saveAll(ins);
		}
		importResultJson.put("totalCount", datas.size());
		importResultJson.put("successCount", successCount);
		importResultJson.put("errorCount", errorDataList.size());
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
	}
	@Override
	public List<CompreScore> queryByIdIn(final String[] ids) {
		Specification<CompreScore> specification = new Specification<CompreScore>() {
			@Override
			public Predicate toPredicate(Root<CompreScore> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
                if(null!=ids && ids.length>0){
                	queryIn("id", ids, root, ps, null);  				
                }
                return cb.or(ps.toArray(new Predicate[0]));
            }			
		};
        return compreScoreDao.findAll(specification);
	}
	@Override
	public void saveAndDelete(String infoId, String type, List<CompreSetup> saveSetupList) {
		CompreInfo info = compreInfoService.findOne(infoId);
		compreScoreDao.deleteByInfoIdAndType(info.getUnitId(), infoId, type);
		compreSetupService.deleteByInfoIdAndType(info.getUnitId(),infoId,type);
		if(StringUtils.isNotBlank(info.getStateArea())){
			List<String> stateList = Stream.of(info.getStateArea().split(",")).collect(Collectors.toList());
			stateList.remove(type);
			if(CollectionUtils.isNotEmpty(stateList)){
				info.setStateArea(stateList.stream().collect(Collectors.joining(",")));
			}else{
				info.setStateArea(null);
			}
			compreInfoService.save(info);
		}
		
		if(CollectionUtils.isNotEmpty(saveSetupList)){
			List<Student> studentList = SUtils.dt(studentRemoteService.findByGradeId(info.getGradeId()), Student.class);
			if(CollectionUtils.isNotEmpty(studentList)){
				List<String> studentIdList = EntityUtils.getList(studentList, Student::getId);
				Map<String, String> subjectToExam = EntityUtils.getMap(saveSetupList, CompreSetup::getSubjectId, CompreSetup::getExamId);
				Map<String, ScoreInfo> studentScoreMap;
				if(!CompreStatisticsConstants.THIRD_LOWER.equals(info.getGradeCode())){
					List<ScoreInfo> scoreInfoList = scoreInfoService.findByExamIdAndSubIdsAndStudentIds(info.getUnitId(), 
							subjectToExam.values().toArray(new String[0]), subjectToExam.keySet().toArray(new String[0]), 
							studentIdList.toArray(new String[studentList.size()]));
					studentScoreMap = scoreInfoList.stream().filter(e->e.getExamId().equals(subjectToExam.get(e.getSubjectId())))
							.collect(Collectors.toMap(e->e.getStudentId()+e.getSubjectId(), e->e));
					
				}else{
					List<ScoreInfo> scoreInfoList = scoreInfoService.findByExamIdAndSubIdsAndStudentIds(info.getUnitId(), 
							subjectToExam.values().toArray(new String[0]), subjectToExam.keySet().toArray(new String[0]), null);
					scoreInfoList = scoreInfoList.stream().filter(e->e.getExamId().equals(subjectToExam.get(e.getSubjectId()))).collect(Collectors.toList());
					Set<String> oldStuIdSet = EntityUtils.getSet(scoreInfoList, ScoreInfo::getStudentId);
					List<Student> oldStuList = SUtils.dt(studentRemoteService.findListByIds(oldStuIdSet.toArray(new String[oldStuIdSet.size()])),Student.class);
					Map<String, String> oldStuMap = EntityUtils.getMap(oldStuList, e->e.getId(), e->e.getIdentitycardType()+e.getIdentityCard());
					Map<String, String> nowStuMap = EntityUtils.getMap(studentList, e->e.getIdentitycardType()+e.getIdentityCard(), e->e.getId());
					Map<String, String> studentMap = new HashMap<String, String>();
					for (Entry<String, String> entry : oldStuMap.entrySet()) {
						if(nowStuMap.containsKey(entry.getValue())){
							studentMap.put(entry.getKey(), nowStuMap.get(entry.getValue()));
						}
					}
							
					studentScoreMap = scoreInfoList.stream().filter(e->studentMap.containsKey(e.getStudentId()))
							.collect(Collectors.toMap(e->studentMap.get(e.getStudentId())+e.getSubjectId(), e->e));
				}
				//如果是口试，同时写入换算成绩toScore
				float fullScore = 100f;
				if(CompreStatisticsConstants.TYPE_ENG_SPEAK.equals(type)){
					Grade grade = SUtils.dc(gradeRemoteService.findOneById(info.getGradeId()), Grade.class);
					String examId = saveSetupList.get(0).getExamId();
					String subjectId = saveSetupList.get(0).getSubjectId();
					SubjectInfo subjectInfo = null;
					String gradeCode = info.getGradeCode().substring(0, 2);
					if(BaseConstants.SECTION_COLLEGE.equals(grade.getSection())){//剑桥高中
						gradeCode = BaseConstants.SECTION_COLLEGE + gradeCode.substring(1);
					}
					subjectInfo = subjectInfoService.findByExamIdAndCourseIdAndGradeCode(examId, subjectId, gradeCode);
					if(subjectInfo !=null && subjectInfo.getFullScore()!=null){
						fullScore = subjectInfo.getFullScore();
					}
				}
				CompreScore compreScore;
				List<CompreScore> saveScoreList = new ArrayList<CompreScore>();
				for (Student student : studentList) {
					for (CompreSetup setup : saveSetupList) {
						compreScore = new CompreScore();
						compreScore.setId(UuidUtils.generateUuid());
						compreScore.setUnitId(info.getUnitId());
						compreScore.setCompreInfoId(infoId);
						compreScore.setAcadyear(info.getAcadyear());
						compreScore.setSemester(info.getSemester());
						compreScore.setStudentId(student.getId());
						compreScore.setSubjectId(setup.getSubjectId());
						if(studentScoreMap.containsKey(student.getId()+setup.getSubjectId())){
							ScoreInfo scoreInfo = studentScoreMap.get(student.getId()+setup.getSubjectId());
							compreScore.setExamId(scoreInfo.getExamId());
							compreScore.setClassId(student.getClassId());
							if(CompreStatisticsConstants.TYPE_ENG_SPEAK.equals(type)){//口试取原始成绩，其他取总评成绩
								if(StringUtils.isNotBlank(scoreInfo.getScore())){
									compreScore.setScore(scoreInfo.getScore());
									compreScore.setVirtual(Constant.IS_TRUE);
								}else{
									compreScore.setScore("0");
									compreScore.setVirtual(Constant.IS_FALSE);
								}
								compreScore.setToScore(Float.valueOf(compreScore.getScore())/fullScore*10);
							}else{
								if(StringUtils.isNotBlank(scoreInfo.getToScore())){
									compreScore.setScore(scoreInfo.getToScore());
									compreScore.setVirtual(Constant.IS_TRUE);
								}else{
									compreScore.setScore("0");
									compreScore.setVirtual(Constant.IS_FALSE);
								}
							}
						}else {
							compreScore.setScore("0");
							compreScore.setVirtual(Constant.IS_FALSE);
							if(CompreStatisticsConstants.TYPE_ENG_SPEAK.equals(type)){
								compreScore.setToScore(0f);
							}
						}
						
						compreScore.setScoremanageType(type);
						compreScore.setCreationTime(new Date());
						compreScore.setModifyTime(new Date());
						saveScoreList.add(compreScore);
					}
				}
				this.saveAll(saveScoreList.toArray(new CompreScore[saveScoreList.size()]));
			}
			compreSetupService.saveAll(saveSetupList.toArray(new CompreSetup[saveSetupList.size()]));
		}
		
	}

    @Override
    public List<CompreScore> findByStudentIdAndType(String studentId, String type) {
        return compreScoreDao.findByStudentIdAndType(studentId, type);
    }

    @Override
    public List<CompreScore> findByUnitIdAndType(String unitId, String type) {
        return compreScoreDao.findByUnitIdAndType(unitId, type);
    }

	@Override
	public List<CompreScore> findByInfoIdAndType(String unitId, String infoId, String type) {
		return compreScoreDao.findByUnitIdAndCompreInfoIdAndScoremanageType(unitId, infoId, type);
	}

	@Override
	public List<CompreScore> findByCompreInfoId(String compreInfoId, boolean isAll) {
		if(isAll) {
			return compreScoreDao.findByCompreInfoIdOnlyAll(compreInfoId);
		}else {
			return compreScoreDao.findByCompreInfoId(compreInfoId);
		}
		
	}

	@Override
	public void saveAll(CompreInfo info, List<CompreScore> scoreList) {
		if(info!=null){
			compreInfoService.save(info);
		}
		if(CollectionUtils.isNotEmpty(scoreList)){
			this.saveAll(scoreList.toArray(new CompreScore[scoreList.size()]));
		}
		
	}
	@Override
	public List<CompreScore> findByStudentIdsAndType(String[] studentIds, String type) {
		return compreScoreDao.findByTypeAndStudentIds(type, studentIds);
	}
	
}
