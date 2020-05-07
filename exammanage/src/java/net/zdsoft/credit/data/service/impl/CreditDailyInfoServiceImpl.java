package net.zdsoft.credit.data.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import net.zdsoft.basedata.entity.GradeTeaching;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeTeachingRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.credit.data.constant.CreditConstants;
import net.zdsoft.credit.data.dao.CreditDailyInfoDao;
import net.zdsoft.credit.data.dto.CredutDailyInfoDto;
import net.zdsoft.credit.data.dto.DailyEditDto;
import net.zdsoft.credit.data.entity.CreditDailyInfo;
import net.zdsoft.credit.data.entity.CreditDailySet;
import net.zdsoft.credit.data.entity.CreditSet;
import net.zdsoft.credit.data.service.CreditDailyInfoService;
import net.zdsoft.credit.data.service.CreditSetService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;

@Service("creditDailyInfoService")
public class CreditDailyInfoServiceImpl extends BaseServiceImpl<CreditDailyInfo, String> implements CreditDailyInfoService {
    @Autowired
    private CreditDailyInfoDao creditDailyInfoDao;
	@Autowired
	private TeachClassStuRemoteService teachClassStuRemoteService;
	@Autowired
	private CreditSetService creditSetService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private GradeTeachingRemoteService gradeTeachingRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Override
	public int statNum(String acadyear, String semester, String unitId, String gradeId) {
		// TODO Auto-generated method stub
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
			int subNum = creditDailyInfoDao.countStuNum(acadyear,semester,gradeId,subId);
			total = total + subNum;
		}
		int avg = total * 100/subIds.size();
		float c = (float)avg/sumStuNum;
		BigDecimal d = new BigDecimal(c+"").setScale(0, BigDecimal.ROUND_HALF_UP);
		return d.intValue();
	}
	
	@Override
	public void importInfo(List<String> titleList, List<String[]> datas, CreditSet set, String gradeId, String userId,
			String subjectId, String clsTypeId, Map<String,Student> stuMap, int successCount, int errorCount, List<String[]> errorDataList) {
		int sequence =1;
		String classId = clsTypeId.split("_")[0];
		String classType = clsTypeId.split("_")[1];
		List<CreditDailyInfo> addList = new ArrayList<>();
		Set<String> stuIds = new HashSet<>();
		CreditDailyInfo info;
		List<CreditDailySet> setlist = set.getDailySetList();
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
			float allScore = 0f;
			for (CreditDailySet setF : setlist) {
				List<CreditDailySet> subSets = setF.getSubSetList();
				float sumScore = 0f;
				for (CreditDailySet subSet : subSets) {
					String name = setF.getName()+"("+subSet.getName()+") "+subSet.getScore();
					int j = 0;
					for (int i = 0; i < titleList.size(); i++) {
						if(StringUtils.equals(name, titleList.get(i))) {
							j = i;
						}
					}
					float score = 0f;
					if(j>1 && rowData.length > j) {
						score = NumberUtils.toFloat(rowData[j]);
						if(score > subSet.getScore()) {
							errorData=new String[4];
							errorData[0]=String.valueOf(sequence);
							sequence++;
			                errorData[1]=name;
			                errorData[2]=stuName;
			                errorData[3]="得分不能超过满分"+subSet.getScore();
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
					}
					sumScore = sumScore + score;
					info = new CreditDailyInfo();
					info.setAcadyear(set.getAcadyear());
					info.setSemester(set.getSemester());
					info.setGradeId(gradeId);
					info.setSubjectId(subjectId);
					info.setSetId(set.getId());
					info.setDailyId(setF.getId());
					info.setSubDailyId(subSet.getId());
					info.setStudentCode(stuCode);
					info.setStudentId(stu.getId());
					info.setStudentName(stu.getStudentName());
					info.setScore(score);
					info.setTotalScore(subSet.getScore());
					info.setClassId(classId);
					info.setClassType(classType);
					info.setOperator(userId);
					info.setUnitId(set.getUnitId());;
					addList.add(info);
				}
				info = new CreditDailyInfo();
				info.setAcadyear(set.getAcadyear());
				info.setSemester(set.getSemester());
				info.setGradeId(gradeId);
				info.setSubjectId(subjectId);
				info.setSetId(set.getId());
				info.setDailyId(setF.getId());
				info.setSubDailyId("");
				info.setStudentCode(stuCode);
				info.setStudentId(stu.getId());
				info.setStudentName(stu.getStudentName());
				info.setScore(sumScore);
				info.setTotalScore(setF.getScore());
				info.setClassId(classId);
				info.setClassType(classType);
				info.setOperator(userId);
				info.setUnitId(set.getUnitId());;
				addList.add(info);
				allScore = allScore + sumScore;
			}
			
			info = new CreditDailyInfo();
			info.setDailyId("");
			info.setSubDailyId("");
			info.setAcadyear(set.getAcadyear());
			info.setSemester(set.getSemester());
			info.setGradeId(gradeId);
			info.setSubjectId(subjectId);
			info.setSetId(set.getId());
			info.setStudentCode(stuCode);
			info.setStudentId(stu.getId());
			info.setStudentName(stu.getStudentName());
			info.setScore(allScore);
			info.setTotalScore(set.getDailyScore());
			info.setClassId(classId);
			info.setClassType(classType);
			info.setOperator(userId);
			info.setUnitId(set.getUnitId());;
			addList.add(info);
			stuIds.add(stu.getId());
			successCount++;
		}
		if(CollectionUtils.isNotEmpty(stuIds)) {
			creditDailyInfoDao.deleteByStuIds(set.getAcadyear(), set.getSemester(), set.getId(), subjectId, stuIds.toArray(new String[0]));
		}
		System.out.println(successCount);
		if(CollectionUtils.isNotEmpty(addList)) {
			checkSave(addList.toArray(new CreditDailyInfo[] {}));
			saveAll(addList.toArray(new CreditDailyInfo[] {}));
		}
		
	}

	@Override
	public List<CreditDailyInfo> findListByNullDailyId(String acadyear, String unitId, String semester, String gradeId) {
		return creditDailyInfoDao.findListByNullDailyId(acadyear, unitId, semester, gradeId);
	}

	@Override
	public List<CreditDailyInfo> findListByNullDailyIdByStuId(String acadyear, String unitId, String semester, String studentId) {
		return creditDailyInfoDao.findListByNullDailyIdByStuId(acadyear, unitId, semester, studentId);
	}

	@Override
	public List<CreditDailyInfo> findListByNotNullDailyId(String acadyear, String semester, String studentId) {
		return creditDailyInfoDao.findListByNotNullDailyId(acadyear, semester, studentId);
	}

	@Override
	public List<CreditDailyInfo> findListByClassIdAndNotNullDailyId(String acadyear, String semester, String classId,String subjectId) {
		return creditDailyInfoDao.findListByClassIdAndNotNullDailyId(acadyear, semester, classId,subjectId);
	}


	@Override
	public void saveDto(String unitId, CredutDailyInfoDto dto) {
		//先删除数据
		List<DailyEditDto> list = dto.getDtolist();
		Set<String> stuIds = EntityUtils.getSet(list, DailyEditDto::getStudentId);
		creditDailyInfoDao.deleteByStuIds(dto.getAcadyear(),dto.getSemester(),dto.getSetId(),dto.getSubjectId(),stuIds.toArray(new String[0]));
		//保存数据
		Map<String, DailyEditDto> dtoMap = new HashMap<>();
		for (DailyEditDto e : list) {
			dtoMap.put(e.getStudentId()+","+e.getDailyId()+","+e.getSubDailyId(), e);
		}
		List<CreditDailyInfo> insertList = new ArrayList<>();
		CreditDailyInfo info;
		CreditSet set = creditSetService.findAndInit(unitId, dto.getAcadyear(),dto.getSemester());
		List<CreditDailySet> dailySets = set.getDailySetList();
		for (String stuId : stuIds) {
			for (CreditDailySet dailySet : dailySets) {
				List<CreditDailySet> subDailySets = dailySet.getSubSetList();
				for (CreditDailySet subSet : subDailySets) {
					if(!dtoMap.containsKey(stuId+","+dailySet.getId()+","+subSet.getId())) {
						continue;
					}
					DailyEditDto dd = dtoMap.get(stuId+","+dailySet.getId()+","+subSet.getId());
					info = new CreditDailyInfo();
					info.setAcadyear(dto.getAcadyear());
					info.setSemester(dto.getSemester());
					info.setGradeId(dto.getGradeId());
					info.setSubjectId(dto.getSubjectId());
					info.setSetId(dto.getSetId());
					info.setDailyId(dd.getDailyId());
					info.setSubDailyId(dd.getSubDailyId());
					info.setStudentCode(dd.getStudentCode());
					info.setStudentId(dd.getStudentId());
					info.setStudentName(dd.getStudentName());
					info.setScore(dd.getScore());
					info.setTotalScore(dd.getTotalScore());
					info.setClassId(dto.getClassId());
					info.setClassType(dto.getClassType());
					info.setOperator(dto.getUserId());
					info.setUnitId(unitId);;
					insertList.add(info);
				}
				
				DailyEditDto dd = dtoMap.get(stuId+","+dailySet.getId()+",");
				info = new CreditDailyInfo();
				info.setAcadyear(dto.getAcadyear());
				info.setSemester(dto.getSemester());
				info.setGradeId(dto.getGradeId());
				info.setSubjectId(dto.getSubjectId());
				info.setSetId(dto.getSetId());
				info.setDailyId(dd.getDailyId());
				info.setSubDailyId("");
				info.setStudentCode(dd.getStudentCode());
				info.setStudentId(dd.getStudentId());
				info.setStudentName(dd.getStudentName());
				info.setScore(dd.getScore());
				info.setTotalScore(dd.getTotalScore());
				info.setClassId(dto.getClassId());
				info.setClassType(dto.getClassType());
				info.setOperator(dto.getUserId());
				info.setUnitId(unitId);;
				insertList.add(info);
			}

			DailyEditDto dd = dtoMap.get(stuId+",,");
			info = new CreditDailyInfo();
			info.setAcadyear(dto.getAcadyear());
			info.setSemester(dto.getSemester());
			info.setGradeId(dto.getGradeId());
			info.setSubjectId(dto.getSubjectId());
			info.setSetId(dto.getSetId());
			info.setDailyId("");
			info.setSubDailyId("");
			info.setStudentCode(dd.getStudentCode());
			info.setStudentId(dd.getStudentId());
			info.setStudentName(dd.getStudentName());
			info.setScore(dd.getScore());
			info.setTotalScore(dd.getTotalScore());
			info.setClassId(dto.getClassId());
			info.setClassType(dto.getClassType());
			info.setOperator(dto.getUserId());
			info.setUnitId(unitId);;
			insertList.add(info);
		}
		
		
		checkSave(insertList.toArray(new CreditDailyInfo[0]));
		saveAll(insertList.toArray(new CreditDailyInfo[0]));
	}
	
	@Override
	public Map<String, CreditDailyInfo> findMapByStuIds(String acadyear, String semester, String subjectId,
			CreditSet set, Set<String> stuIds) {
		Map<String, CreditDailyInfo> infoMap = new HashMap<>();
		if(CollectionUtils.isEmpty(stuIds)) {
			return infoMap;
		}
		List<CreditDailyInfo> list = creditDailyInfoDao.findListByStudentIds(acadyear,semester,subjectId, stuIds.toArray(new String[0]));
		Map<String, CreditDailyInfo> map = new HashMap<>();
		for (CreditDailyInfo e : list) {
			map.put(e.getDailyId() + "," + e.getSubDailyId()+","+e.getStudentId(), e);
		}
		
		List<CreditDailySet> dailySetList = set.getDailySetList();
		for (String stuId : stuIds) {
			float maxScore = 0f;
			for (CreditDailySet dailySet : dailySetList) {
				List<CreditDailySet> subSetList = dailySet.getSubSetList();
				float score = 0f;
				for (CreditDailySet subSet : subSetList) {
					//子项列
					if(map.containsKey(dailySet.getId()+","+subSet.getId()+","+stuId)) {
						CreditDailyInfo info = map.get(dailySet.getId()+","+subSet.getId()+","+stuId);
						score = score + info.getScore();
						infoMap.put(dailySet.getId()+","+subSet.getId()+","+stuId, info);
					}else {
						CreditDailyInfo info = new CreditDailyInfo();
						info.setScore(0f);
						if(StringUtils.equals(CreditConstants.SCORE_TYPE_1, subSet.getScoreType())) {
							info.setScore(subSet.getScore());
						}
						score = score + info.getScore();
						infoMap.put(dailySet.getId()+","+subSet.getId()+","+stuId, info);
					}
				}
				//分值列
				if(map.containsKey(dailySet.getId()+",,"+stuId)) {
					CreditDailyInfo info = map.get(dailySet.getId()+",,"+stuId);
					infoMap.put(dailySet.getId()+",,"+stuId, info);
				}else {
					CreditDailyInfo info = new CreditDailyInfo();
					info.setScore(score);
					infoMap.put(dailySet.getId()+",,"+stuId, info);
				}
				maxScore = maxScore + score;
			}
			if(map.containsKey(",,"+stuId)) {
				CreditDailyInfo info = map.get(",,"+stuId);
				infoMap.put(",,"+stuId, info);
			}else {
				CreditDailyInfo info = new CreditDailyInfo();
				info.setScore(maxScore);
				infoMap.put(stuId, info);
			}
		}
		return infoMap;
	}
	
	@Override
	protected BaseJpaRepositoryDao<CreditDailyInfo, String> getJpaDao() {
		return creditDailyInfoDao;
	}


    @Override
    protected Class<CreditDailyInfo> getEntityClass() {
        return CreditDailyInfo.class;
    }

    @Override
    public List<CreditDailyInfo> findListBySubDailySetIds(String[] setIds) {
        return creditDailyInfoDao.findListBySubDailySetIds(setIds);
    }
}
