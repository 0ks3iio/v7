package net.zdsoft.credit.data.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.GradeTeaching;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeTeachingRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.credit.data.constant.CreditConstants;
import net.zdsoft.credit.data.dao.CreditModuleInfoDao;
import net.zdsoft.credit.data.dto.CredutDailyInfoDto;
import net.zdsoft.credit.data.dto.DailyEditDto;
import net.zdsoft.credit.data.entity.CreditExamSet;
import net.zdsoft.credit.data.entity.CreditModuleInfo;
import net.zdsoft.credit.data.entity.CreditSet;
import net.zdsoft.credit.data.service.CreditExamSetService;
import net.zdsoft.credit.data.service.CreditModuleInfoService;
import net.zdsoft.exammanage.data.entity.EmScoreInfo;
import net.zdsoft.exammanage.data.service.EmScoreInfoService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

@Service("creditModuleInfoService")
public class CreditModuleInfoServiceImpl extends BaseServiceImpl<CreditModuleInfo, String> implements CreditModuleInfoService {
    @Autowired
    private CreditModuleInfoDao creditModuleInfoDao;
	@Autowired
	private CreditExamSetService creditExamSetService;
	@Autowired
	private EmScoreInfoService emScoreInfoService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private GradeTeachingRemoteService gradeTeachingRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Override
	public void deleteBySetExamIds(String[] examSetIds) {
		creditModuleInfoDao.deleteByExamSetIds(examSetIds);
	}
	
	@Override
	public void importInfo(List<String> colls, List<String[]> datas, CreditSet set, String gradeId, String userId,
			String subjectId, String clsTypeId, Map<String, Student> stuMap, int successCount, int errorCount,
			List<String[]> errorDataList) {
		// TODO Auto-generated method stub
		int sequence =1;
		String classId = clsTypeId.split("_")[0];
		String classType = clsTypeId.split("_")[1];
		
		List<CreditExamSet> usualSetList = creditExamSetService.findByUsualSet(set.getId(), set.getAcadyear(), set.getSemester(), subjectId, classId, classType);
		Map<String,CreditExamSet> usualNameMap = EntityUtils.getMap(usualSetList, CreditExamSet::getName);
		Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
		int fullMark = course.getFullMark() == null?100:course.getFullMark();
		float usualBance = (float)set.getUsualScore()/fullMark;
		float moduleBance = (float)set.getModuleScore()/fullMark;
		List<CreditExamSet> moudleSets = creditExamSetService.findBySetIdAndAcadyearAndSemesterAndGradeIdAndType(set.getId(), set.getAcadyear(), set.getSemester(), gradeId, CreditConstants.CREDIT_EXAM_TYPE_2);
		CreditExamSet moudleSet = null;
		if(CollectionUtils.isNotEmpty(moudleSets)) {
			moudleSet = moudleSets.get(0);
		}
		
		List<CreditExamSet> insertSetList = new ArrayList<>();
		CreditExamSet usualSet = null;
		List<CreditModuleInfo> insertList = new ArrayList<>();
		Set<String> stuIds = new HashSet<>();
		CreditModuleInfo info;
		
		for (String[] rowData : datas) {
			String[] errorData=new String[4];
			String stuCode = rowData[0];
			String stuName = rowData[1];
			if(StringUtils.isBlank(stuCode)) {
				errorData[0]=String.valueOf(sequence);
				sequence++;
                errorData[1]="学号";
                errorData[2]="";
                errorData[3]="学号不能为空";
                errorDataList.add(errorData);
                errorCount++;
				continue;
			}
			if(!stuMap.containsKey(stuCode)) {
				errorData[0]=String.valueOf(sequence);
				sequence++;
                errorData[1]="学号";
                errorData[2]=stuCode;
                errorData[3]="该学号不在导入名单内";
                errorDataList.add(errorData);
                errorCount++;
				continue;
			}
			Student stu = stuMap.get(stuCode);
			if(!StringUtils.equals(stu.getStudentName(), stuName)){
				errorData[0]=String.valueOf(sequence);
				sequence++;
                errorData[1]="姓名";
                errorData[2]=StringUtils.isBlank(stuName)?"":stuName;
                errorData[3]="学号和姓名不匹配";
                errorDataList.add(errorData);
                errorCount++;
				continue;
			}
			int moudleIndex = -1;
			float sumUsual = 0f;
			int usualSize = 0;
			
			for (int i = 2; i < colls.size(); i++) {
				String name = colls.get(i).trim();
				float score = 0f;
				score = NumberUtils.toFloat(rowData[i]);
				if(score > fullMark) {
					errorData=new String[4];
					errorData[0]=String.valueOf(sequence);
					sequence++;
	                errorData[1]=name;
	                errorData[2]=stuName;
	                errorData[3]="得分不能超过满分"+fullMark;
	                errorDataList.add(errorData);
	                errorCount++;
					continue;
				}
				if(score < 0) {
					errorData=new String[4];
					errorData[0]=String.valueOf(sequence);
					sequence++;
	                errorData[1]=name;
	                errorData[2]=stuName;
	                errorData[3]="得分不能小于0";
	                errorDataList.add(errorData);
	                errorCount++;
					continue;
				}
				
				if(StringUtils.equals(name, "模块成绩")) {
					moudleIndex = i;
					continue;
				}
				
				if(usualNameMap.containsKey(name)) {
					usualSet = usualNameMap.get(name);
				}else {
					continue;
//					usualSet = new CreditExamSet();
//					usualSet.setAcadyear(set.getAcadyear());
//					usualSet.setSemester(set.getSemester());
//					usualSet.setUnitId(set.getUnitId());
//					usualSet.setOperator(userId);
//					usualSet.setClassId(classId);
//					usualSet.setClassType(classType);
//					usualSet.setName(name);
//					usualSet.setGradeId(gradeId);
//					usualSet.setSetId(set.getId());
//					usualSet.setSubjectId(subjectId);
//					usualSet.setType(CreditConstants.CREDIT_EXAM_TYPE_1);
//					usualSet.setId(UuidUtils.generateUuid());
//					usualSet.setCreationTime(new Date());
//					insertSetList.add(usualSet);
//					usualNameMap.put(name, usualSet);
				}

				info = new CreditModuleInfo();
				info.setAcadyear(set.getAcadyear());
				info.setSemester(set.getSemester());
				info.setGradeId(gradeId);
				info.setSubjectId(subjectId);
				info.setSetId(set.getId());
				info.setExamSetId(usualSet.getId());
				info.setStudentCode(stu.getStudentCode());
				info.setStudentId(stu.getId());
				info.setStudentName(stu.getStudentName());
				info.setScore(score);
				info.setClassId(classId);
				info.setClassType(classType);
				info.setOperator(userId);
				info.setUnitId(set.getUnitId());
				info.setExamType(CreditConstants.CREDIT_EXAM_TYPE_1);
				info.setScoreType(CreditConstants.CREDIT_SCORE_TYPE_1);
				insertList.add(info);
				sumUsual = sumUsual + score;
				usualSize++;
			}
			//平均分列
			if(usualSize == 0) {
				usualSize++;
			}
			float avgScore = sumUsual/usualSize;
			info = new CreditModuleInfo();
			info.setAcadyear(set.getAcadyear());
			info.setSemester(set.getSemester());
			info.setGradeId(gradeId);
			info.setSubjectId(subjectId);
			info.setSetId(set.getId());
			info.setExamSetId("");
			info.setStudentCode(stu.getStudentCode());
			info.setStudentId(stu.getId());
			info.setStudentName(stu.getStudentName());
			info.setScore(avgScore);
			info.setClassId(classId);
			info.setClassType(classType);
			info.setOperator(userId);
			info.setUnitId(set.getUnitId());
			info.setExamType(CreditConstants.CREDIT_EXAM_TYPE_1);
			info.setScoreType(CreditConstants.CREDIT_SCORE_TYPE_2);
			insertList.add(info);
			//折分
			float usualScore = avgScore*usualBance;
			info = new CreditModuleInfo();
			info.setAcadyear(set.getAcadyear());
			info.setSemester(set.getSemester());
			info.setGradeId(gradeId);
			info.setSubjectId(subjectId);
			info.setSetId(set.getId());
			info.setExamSetId("");
			info.setStudentCode(stu.getStudentCode());
			info.setStudentId(stu.getId());
			info.setStudentName(stu.getStudentName());
			info.setScore(usualScore);
			info.setClassId(classId);
			info.setClassType(classType);
			info.setOperator(userId);
			info.setUnitId(set.getUnitId());
			info.setExamType(CreditConstants.CREDIT_EXAM_TYPE_1);
			info.setScoreType(CreditConstants.CREDIT_SCORE_TYPE_3);
			insertList.add(info);
			if(moudleIndex > -1
					&& moudleSet != null) {
				//折分
				float mScore = NumberUtils.toFloat(rowData[moudleIndex]);
				if(mScore > fullMark) {
					errorData=new String[4];
					errorData[0]=String.valueOf(sequence);
					sequence++;
	                errorData[1]="模块成绩";
	                errorData[2]=stuName;
	                errorData[3]="得分不能超过满分"+fullMark;
	                errorDataList.add(errorData);
	                errorCount++;
					continue;
				}
				info = new CreditModuleInfo();
				info.setAcadyear(set.getAcadyear());
				info.setSemester(set.getSemester());
				info.setGradeId(gradeId);
				info.setSubjectId(subjectId);
				info.setSetId(set.getId());
				info.setExamSetId(moudleSet.getId());
				info.setStudentCode(stu.getStudentCode());
				info.setStudentId(stu.getId());
				info.setStudentName(stu.getStudentName());
				info.setScore(mScore);
				info.setClassId(classId);
				info.setClassType(classType);
				info.setOperator(userId);
				info.setUnitId(set.getUnitId());
				info.setExamType(CreditConstants.CREDIT_EXAM_TYPE_2);
				info.setScoreType(CreditConstants.CREDIT_SCORE_TYPE_1);
				insertList.add(info);
				float moudleScore = mScore*moduleBance;
				info = new CreditModuleInfo();
				info.setAcadyear(set.getAcadyear());
				info.setSemester(set.getSemester());
				info.setGradeId(gradeId);
				info.setSubjectId(subjectId);
				info.setSetId(set.getId());
				info.setExamSetId("");
				info.setStudentCode(stu.getStudentCode());
				info.setStudentId(stu.getId());
				info.setStudentName(stu.getStudentName());
				info.setScore(moudleScore);
				info.setClassId(classId);
				info.setClassType(classType);
				info.setOperator(userId);
				info.setUnitId(set.getUnitId());
				info.setExamType(CreditConstants.CREDIT_EXAM_TYPE_2);
				info.setScoreType(CreditConstants.CREDIT_SCORE_TYPE_3);
				insertList.add(info);
			}
			stuIds.add(stu.getId());
		}
		if(CollectionUtils.isNotEmpty(stuIds)) {
			creditModuleInfoDao.deleteByStuIds(set.getAcadyear(),set.getSemester(),subjectId,stuIds.toArray(new String[0]));
		}
		
		if(CollectionUtils.isNotEmpty(insertList)) {
			checkSave(insertList.toArray(new CreditModuleInfo[] {}));
			for (CreditModuleInfo string : insertList) {
				float nan=Float.NaN;
				if(string.getScore() == Float.NaN) {
					System.out.println(string.getExamType()+"---"+string.getScoreType()+"---"+string.getStudentName());
				}
//				System.out.println(string.getScore());
			}
			saveAll(insertList.toArray(new CreditModuleInfo[] {}));
		}
		if(CollectionUtils.isNotEmpty(insertSetList)) {
			creditExamSetService.saveAll(insertSetList.toArray(new CreditExamSet[] {}));
		}
	}
	
	@Override
	public void saveDto(String unitId, CredutDailyInfoDto dto) {
		//先删除数据
		List<DailyEditDto> list = dto.getDtolist();
		Set<String> stuIds = EntityUtils.getSet(list, DailyEditDto::getStudentId);
//		creditDailyInfoDao.deleteByStuIds(dto.getAcadyear(),dto.getSemester(),dto.getSetId(),dto.getSubjectId(),stuIds.toArray(new String[0]));
		creditModuleInfoDao.deleteByStuIds(dto.getAcadyear(),dto.getSemester(),dto.getSubjectId(),stuIds.toArray(new String[0]));
		//保存数据
		Map<String, DailyEditDto> dtoMap = new HashMap<>();
		for (DailyEditDto e : list) {
			dtoMap.put(e.getExamType()+","+e.getStudentId()+","+e.getExamSetId()+","+e.getScoreType(), e);
		}
		List<CreditModuleInfo> insertList = new ArrayList<>();
		CreditModuleInfo info;
		List<CreditExamSet> usualSetList = creditExamSetService.findByUsualSet(dto.getSetId(), dto.getAcadyear(), dto.getSemester(), dto.getSubjectId(), dto.getClassId(), dto.getClassType());
		List<CreditExamSet> moudleSets = creditExamSetService.findBySetIdAndAcadyearAndSemesterAndGradeIdAndType(dto.getSetId(), dto.getAcadyear(), dto.getSemester(), dto.getGradeId(), CreditConstants.CREDIT_EXAM_TYPE_2);
		CreditExamSet moudleSet = null;
		if(CollectionUtils.isNotEmpty(moudleSets)) {
			moudleSet = moudleSets.get(0);
		}
		for (String stuId : stuIds) {
			if(CollectionUtils.isNotEmpty(usualSetList)) {
				for (CreditExamSet usualSet : usualSetList) {
					if(!dtoMap.containsKey(CreditConstants.CREDIT_EXAM_TYPE_1+","+stuId+","+usualSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_1)) {
						continue;
					}
					DailyEditDto dd = dtoMap.get(CreditConstants.CREDIT_EXAM_TYPE_1+","+stuId+","+usualSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_1);
					info = new CreditModuleInfo();
					info.setAcadyear(dto.getAcadyear());
					info.setSemester(dto.getSemester());
					info.setGradeId(dto.getGradeId());
					info.setSubjectId(dto.getSubjectId());
					info.setSetId(dto.getSetId());
					info.setExamSetId(usualSet.getId());
					info.setStudentCode(dd.getStudentCode());
					info.setStudentId(dd.getStudentId());
					info.setStudentName(dd.getStudentName());
					info.setScore(dd.getScore());
					info.setClassId(dto.getClassId());
					info.setClassType(dto.getClassType());
					info.setOperator(dto.getUserId());
					info.setUnitId(unitId);
					info.setExamType(CreditConstants.CREDIT_EXAM_TYPE_1);
					info.setScoreType(CreditConstants.CREDIT_SCORE_TYPE_1);
					insertList.add(info);
				}
				
				DailyEditDto dd2 = dtoMap.get(CreditConstants.CREDIT_EXAM_TYPE_1+","+stuId+",,"+CreditConstants.CREDIT_SCORE_TYPE_2);
				info = new CreditModuleInfo();
				info.setAcadyear(dto.getAcadyear());
				info.setSemester(dto.getSemester());
				info.setGradeId(dto.getGradeId());
				info.setSubjectId(dto.getSubjectId());
				info.setSetId(dto.getSetId());
				info.setExamSetId("");
				info.setStudentCode(dd2.getStudentCode());
				info.setStudentId(dd2.getStudentId());
				info.setStudentName(dd2.getStudentName());
				info.setScore(dd2.getScore());
				info.setClassId(dto.getClassId());
				info.setClassType(dto.getClassType());
				info.setOperator(dto.getUserId());
				info.setUnitId(unitId);
				info.setExamType(CreditConstants.CREDIT_EXAM_TYPE_1);
				info.setScoreType(CreditConstants.CREDIT_SCORE_TYPE_2);
				insertList.add(info);
				
				DailyEditDto dd3 = dtoMap.get(CreditConstants.CREDIT_EXAM_TYPE_1+","+stuId+",,"+CreditConstants.CREDIT_SCORE_TYPE_3);
				info = new CreditModuleInfo();
				info.setAcadyear(dto.getAcadyear());
				info.setSemester(dto.getSemester());
				info.setGradeId(dto.getGradeId());
				info.setSubjectId(dto.getSubjectId());
				info.setSetId(dto.getSetId());
				info.setExamSetId("");
				info.setStudentCode(dd3.getStudentCode());
				info.setStudentId(dd3.getStudentId());
				info.setStudentName(dd3.getStudentName());
				info.setScore(dd3.getScore());
				info.setClassId(dto.getClassId());
				info.setClassType(dto.getClassType());
				info.setOperator(dto.getUserId());
				info.setUnitId(unitId);
				info.setExamType(CreditConstants.CREDIT_EXAM_TYPE_1);
				info.setScoreType(CreditConstants.CREDIT_SCORE_TYPE_3);
				insertList.add(info);
				
			}
			if(moudleSet != null) {
				if(!dtoMap.containsKey(CreditConstants.CREDIT_EXAM_TYPE_2+","+stuId+","+moudleSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_1)) {
					continue;
				}
				DailyEditDto dd = dtoMap.get(CreditConstants.CREDIT_EXAM_TYPE_2+","+stuId+","+moudleSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_1);
				info = new CreditModuleInfo();
				info.setAcadyear(dto.getAcadyear());
				info.setSemester(dto.getSemester());
				info.setGradeId(dto.getGradeId());
				info.setSubjectId(dto.getSubjectId());
				info.setSetId(dto.getSetId());
				info.setExamSetId(moudleSet.getId());
				info.setStudentCode(dd.getStudentCode());
				info.setStudentId(dd.getStudentId());
				info.setStudentName(dd.getStudentName());
				info.setScore(dd.getScore());
				info.setClassId(dto.getClassId());
				info.setClassType(dto.getClassType());
				info.setOperator(dto.getUserId());
				info.setUnitId(unitId);
				info.setExamType(CreditConstants.CREDIT_EXAM_TYPE_2);
				info.setScoreType(CreditConstants.CREDIT_SCORE_TYPE_1);
				insertList.add(info);
				
				DailyEditDto dd3 = dtoMap.get(CreditConstants.CREDIT_EXAM_TYPE_2+","+stuId+",,"+CreditConstants.CREDIT_SCORE_TYPE_3);
				info = new CreditModuleInfo();
				info.setAcadyear(dto.getAcadyear());
				info.setSemester(dto.getSemester());
				info.setGradeId(dto.getGradeId());
				info.setSubjectId(dto.getSubjectId());
				info.setSetId(dto.getSetId());
				info.setExamSetId(moudleSet.getId());
				info.setStudentCode(dd3.getStudentCode());
				info.setStudentId(dd3.getStudentId());
				info.setStudentName(dd3.getStudentName());
				info.setScore(dd3.getScore());
				info.setClassId(dto.getClassId());
				info.setClassType(dto.getClassType());
				info.setOperator(dto.getUserId());
				info.setUnitId(unitId);
				info.setExamType(CreditConstants.CREDIT_EXAM_TYPE_2);
				info.setScoreType(CreditConstants.CREDIT_SCORE_TYPE_3);
				insertList.add(info);
				
				

				if(!dtoMap.containsKey(CreditConstants.CREDIT_EXAM_TYPE_3+","+stuId+","+moudleSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_1)) {
					continue;
				}
				DailyEditDto ddd = dtoMap.get(CreditConstants.CREDIT_EXAM_TYPE_3+","+stuId+","+moudleSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_1);
				info = new CreditModuleInfo();
				info.setAcadyear(dto.getAcadyear());
				info.setSemester(dto.getSemester());
				info.setGradeId(dto.getGradeId());
				info.setSubjectId(dto.getSubjectId());
				info.setSetId(dto.getSetId());
				info.setExamSetId(moudleSet.getId());
				info.setStudentCode(ddd.getStudentCode());
				info.setStudentId(ddd.getStudentId());
				info.setStudentName(ddd.getStudentName());
				info.setScore(ddd.getScore());
				info.setClassId(dto.getClassId());
				info.setClassType(dto.getClassType());
				info.setOperator(dto.getUserId());
				info.setUnitId(unitId);
				info.setExamType(CreditConstants.CREDIT_EXAM_TYPE_3);
				info.setScoreType(CreditConstants.CREDIT_SCORE_TYPE_1);
				insertList.add(info);
				
				DailyEditDto ddd3 = dtoMap.get(CreditConstants.CREDIT_EXAM_TYPE_3+","+stuId+","+moudleSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_3);
				info = new CreditModuleInfo();
				info.setAcadyear(dto.getAcadyear());
				info.setSemester(dto.getSemester());
				info.setGradeId(dto.getGradeId());
				info.setSubjectId(dto.getSubjectId());
				info.setSetId(dto.getSetId());
				info.setExamSetId(moudleSet.getId());
				info.setStudentCode(ddd3.getStudentCode());
				info.setStudentId(ddd3.getStudentId());
				info.setStudentName(ddd3.getStudentName());
				info.setScore(ddd3.getScore());
				info.setClassId(dto.getClassId());
				info.setClassType(dto.getClassType());
				info.setOperator(dto.getUserId());
				info.setUnitId(unitId);
				info.setExamType(CreditConstants.CREDIT_EXAM_TYPE_3);
				info.setScoreType(CreditConstants.CREDIT_SCORE_TYPE_3);
				insertList.add(info);
			}
			
			
			
		}
		checkSave(insertList.toArray(new CreditModuleInfo[0]));
		saveAll(insertList.toArray(new CreditModuleInfo[0]));
	}
	@Autowired
	private CourseRemoteService courseRemoteService;
	
	public void setCourseRemoteService(CourseRemoteService courseRemoteService) {
		this.courseRemoteService = courseRemoteService;
	}

	@Override
	public Map<String, CreditModuleInfo> findMapByStuIds(String acadyear, String semester, String subjectId,
			List<CreditExamSet> usualSetList, CreditExamSet moudleSet, CreditSet set, Set<String> stuIds) {
		Map<String, CreditModuleInfo> infoMap = new HashMap<>();
		if(CollectionUtils.isEmpty(stuIds)) {
			return infoMap;
		}
		List<CreditModuleInfo> list = creditModuleInfoDao.findListByStudentIds(acadyear,semester,subjectId, stuIds.toArray(new String[0]));
		Map<String, CreditModuleInfo> map = new HashMap<>();
		for (CreditModuleInfo e : list) {
			map.put(e.getExamType()+","+e.getExamSetId()+","+e.getScoreType()+","+e.getStudentId(), e);
		}
		Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
		int fullMark = course.getFullMark() == null?100:course.getFullMark();
		float usualBance = (float)set.getUsualScore()/fullMark;
		float moduleBance = (float)set.getModuleScore()/fullMark;
		
		
		
		for (String stuId : stuIds) {
			if(CollectionUtils.isNotEmpty(usualSetList)) {
				float sumScore = 0f;
				for (CreditExamSet usualSet : usualSetList) {
					//得分
					if(map.containsKey(CreditConstants.CREDIT_EXAM_TYPE_1+","+usualSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_1+","+stuId)) {
						CreditModuleInfo info = map.get(CreditConstants.CREDIT_EXAM_TYPE_1+","+usualSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_1+","+stuId);
						sumScore = sumScore + info.getScore();
						infoMap.put(CreditConstants.CREDIT_EXAM_TYPE_1+","+usualSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_1+","+stuId, info);
					}else {
						CreditModuleInfo info = new CreditModuleInfo();
						info.setScore(0f);
						infoMap.put(CreditConstants.CREDIT_EXAM_TYPE_1+","+usualSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_1+","+stuId, info);
					}
				}
				//平均分列
				float avgScore = sumScore/usualSetList.size();
				if(map.containsKey(CreditConstants.CREDIT_EXAM_TYPE_1+",,"+CreditConstants.CREDIT_SCORE_TYPE_2+","+stuId)) {
					CreditModuleInfo info = map.get(CreditConstants.CREDIT_EXAM_TYPE_1+",,"+CreditConstants.CREDIT_SCORE_TYPE_2+","+stuId);
					infoMap.put(CreditConstants.CREDIT_EXAM_TYPE_1+",,"+CreditConstants.CREDIT_SCORE_TYPE_2+","+stuId, info);
				}else {
					CreditModuleInfo info = new CreditModuleInfo();
					info.setScore(avgScore);
					infoMap.put(CreditConstants.CREDIT_EXAM_TYPE_1+",,"+CreditConstants.CREDIT_SCORE_TYPE_2+","+stuId, info);
				}
				//折分
				float usualScore = avgScore*usualBance;
				if(map.containsKey(CreditConstants.CREDIT_EXAM_TYPE_1+",,"+CreditConstants.CREDIT_SCORE_TYPE_3+","+stuId)) {
					CreditModuleInfo info = map.get(CreditConstants.CREDIT_EXAM_TYPE_1+",,"+CreditConstants.CREDIT_SCORE_TYPE_3+","+stuId);
					infoMap.put(CreditConstants.CREDIT_EXAM_TYPE_1+",,"+CreditConstants.CREDIT_SCORE_TYPE_3+","+stuId, info);
				}else {
					CreditModuleInfo info = new CreditModuleInfo();
					info.setScore(usualScore);
					infoMap.put(CreditConstants.CREDIT_EXAM_TYPE_1+",,"+CreditConstants.CREDIT_SCORE_TYPE_3+","+stuId, info);
				}
				
			}
			if(moudleSet != null) {
				//得分
				float mScore = 0f;
				if(map.containsKey(CreditConstants.CREDIT_EXAM_TYPE_2+","+moudleSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_1+","+stuId)) {
					CreditModuleInfo info = map.get(CreditConstants.CREDIT_EXAM_TYPE_2+","+moudleSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_1+","+stuId);
					mScore = info.getScore();
					infoMap.put(CreditConstants.CREDIT_EXAM_TYPE_2+","+moudleSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_1+","+stuId, info);
				}else {
					CreditModuleInfo info = new CreditModuleInfo();
					info.setScore(0f);
					infoMap.put(CreditConstants.CREDIT_EXAM_TYPE_2+","+moudleSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_1+","+stuId, info);
				}
				//折分
				float moudleScore = mScore*moduleBance;
				if(map.containsKey(CreditConstants.CREDIT_EXAM_TYPE_2+",,"+CreditConstants.CREDIT_SCORE_TYPE_3+","+stuId)) {
					CreditModuleInfo info = map.get(CreditConstants.CREDIT_EXAM_TYPE_2+",,"+CreditConstants.CREDIT_SCORE_TYPE_3+","+stuId);
					infoMap.put(CreditConstants.CREDIT_EXAM_TYPE_2+",,"+CreditConstants.CREDIT_SCORE_TYPE_3+","+stuId, info);
				}else {
					CreditModuleInfo info = new CreditModuleInfo();
					info.setScore(moudleScore);
					infoMap.put(CreditConstants.CREDIT_EXAM_TYPE_2+",,"+CreditConstants.CREDIT_SCORE_TYPE_3+","+stuId, info);
				}
				//补考得分
				if(map.containsKey(CreditConstants.CREDIT_EXAM_TYPE_3+","+moudleSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_1+","+stuId)) {
					CreditModuleInfo info = map.get(CreditConstants.CREDIT_EXAM_TYPE_3+","+moudleSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_1+","+stuId);
					infoMap.put(CreditConstants.CREDIT_EXAM_TYPE_3+","+moudleSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_1+","+stuId, info);
				}
				//补考折分
				if(map.containsKey(CreditConstants.CREDIT_EXAM_TYPE_3+","+moudleSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_3+","+stuId)) {
					CreditModuleInfo info = map.get(CreditConstants.CREDIT_EXAM_TYPE_3+","+moudleSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_3+","+stuId);
					infoMap.put(CreditConstants.CREDIT_EXAM_TYPE_3+","+moudleSet.getId()+","+CreditConstants.CREDIT_SCORE_TYPE_3+","+stuId, info);
				}
			}
			
			
			
		}
		return infoMap;
	}
	
	@Override
	public void saveModuleId(String unitId, String acadyear, String semester, String gradeId, String setId,
			String setExamId, String userId) {
		List<CreditExamSet> examSets = creditExamSetService.findBySetIdAndAcadyearAndSemesterAndGradeIdAndType(setId,acadyear,semester,gradeId,CreditConstants.CREDIT_EXAM_TYPE_2);
		if(CollectionUtils.isNotEmpty(examSets)) {
			creditExamSetService.delete(examSets.get(0));
		}
		creditModuleInfoDao.deleteByType(setId,gradeId,acadyear,semester,CreditConstants.CREDIT_EXAM_TYPE_2);
		CreditExamSet set = new CreditExamSet();
		set.setAcadyear(acadyear);
		set.setSemester(semester);
		set.setGradeId(gradeId);
		set.setExamId(setExamId);
		set.setType(CreditConstants.CREDIT_EXAM_TYPE_2);
		set.setSetId(setId);
		set.setOperator(userId);
		set.setUnitId(unitId);
		set.setCreationTime(new Date());
		set.setId(UuidUtils.generateUuid());
		creditExamSetService.save(set);
		List<EmScoreInfo> scoreList= emScoreInfoService.findByExamIdAndUnitId(setExamId,unitId);
		if(CollectionUtils.isEmpty(scoreList)) {
			return;
		}
		List<CreditModuleInfo> infolist = new ArrayList<>();
		CreditModuleInfo info = null;
		for (EmScoreInfo e : scoreList) {
			info = new CreditModuleInfo();
			info.setAcadyear(acadyear);
			info.setSemester(semester);
			info.setGradeId(gradeId);
			info.setExamType(CreditConstants.CREDIT_EXAM_TYPE_2);
			info.setSetId(setId);
			info.setOperator(userId);
			info.setUnitId(unitId);
			info.setSubjectId(e.getSubjectId());
			if(StringUtils.isNotBlank(e.getTeachClassId())) {
				info.setClassType(CreditConstants.CLASS_TYPE2);
				info.setClassId(e.getTeachClassId());
			}else {
				info.setClassType(CreditConstants.CLASS_TYPE1);
				info.setClassId(e.getClassId());
			}
			info.setStudentId(e.getStudentId());
			info.setExamSetId(set.getId());
			info.setScore(NumberUtils.toFloat(e.getScore()));
			info.setScoreType(CreditConstants.CREDIT_SCORE_TYPE_1);
			infolist.add(info);
		}
		if(CollectionUtils.isNotEmpty(infolist)) {
			checkSave(infolist.toArray(new CreditModuleInfo[0]));
			saveAll(infolist.toArray(new CreditModuleInfo[0]));
		}
	}
	
	@Override
	protected BaseJpaRepositoryDao<CreditModuleInfo, String> getJpaDao() {
		return creditModuleInfoDao;
	}

    @Override
    protected Class<CreditModuleInfo> getEntityClass() {
        return CreditModuleInfo.class;
    }

    @Override
    public List<CreditModuleInfo> findListByExamSetIds(String[] setIds,String scoreType) {
        return creditModuleInfoDao.findListByExamSetIds(setIds,scoreType);
    }

	@Override
	public int statNum(String unitId, String acadyear, String semester, String gradeId) {
		List<GradeTeaching> gradeTeachings = SUtils.dt(gradeTeachingRemoteService.findBySearchList(unitId, acadyear, semester, gradeId, 1),new TR<List<GradeTeaching>>() {});
		Set<String> subIds = EntityUtils.getSet(gradeTeachings, GradeTeaching::getSubjectId);
		if(CollectionUtils.isEmpty(subIds)) {
			return 0;
		}
		List<Clazz> clslist = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), new TR<List<Clazz>>() {});
		Set<String> clsIds = EntityUtils.getSet(clslist, Clazz::getId);
		List<Student> stulist = SUtils.dt(studentRemoteService.findByClassIds(clsIds.toArray(new String[0])), new TR<List<Student>>() {});
		if(CollectionUtils.isEmpty(stulist)) {
			return 0;
		}
		int sumStuNum = stulist.size();
		int total = 0;
		for (String subId : subIds) {
			int subNum = creditModuleInfoDao.countStuNum(acadyear,semester,gradeId,subId);
			total = total + subNum;
		}
		int avg = total * 100/subIds.size();
		float c = (float)avg/sumStuNum;
		BigDecimal d = new BigDecimal(c+"").setScale(0, BigDecimal.ROUND_HALF_UP);
		return d.intValue();
	}
	
}
