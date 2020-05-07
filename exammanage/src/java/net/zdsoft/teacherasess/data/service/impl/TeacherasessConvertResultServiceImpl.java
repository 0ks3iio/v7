package net.zdsoft.teacherasess.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.StudentSelectSubjectRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.entity.EmConversion;
import net.zdsoft.exammanage.data.entity.EmScoreInfo;
import net.zdsoft.exammanage.data.service.EmConversionService;
import net.zdsoft.exammanage.data.service.EmScoreInfoService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.teacherasess.data.dao.TeacherasessConvertResultDao;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvert;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvertExam;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvertGroup;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvertResult;
import net.zdsoft.teacherasess.data.service.TeacherasessConvertExamService;
import net.zdsoft.teacherasess.data.service.TeacherasessConvertGroupService;
import net.zdsoft.teacherasess.data.service.TeacherasessConvertResultService;
import net.zdsoft.teacherasess.data.service.TeacherasessConvertService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service("teacherasessConvertResultService")
public class TeacherasessConvertResultServiceImpl extends BaseServiceImpl<TeacherasessConvertResult, String> implements TeacherasessConvertResultService{
	@Autowired
	private TeacherasessConvertResultDao teacherasessConvertResultDao;
	@Autowired
	private TeacherasessConvertService teacherasessConvertService;
	@Autowired
	private TeacherasessConvertExamService teacherasessConvertExamService;
	@Autowired
	private TeacherasessConvertGroupService teacherasessConvertGroupService;
	@Autowired
	private EmScoreInfoService emScoreInfoService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private StudentSelectSubjectRemoteService studentSelectSubjectRemoteService;
	@Autowired
	private EmConversionService emConversionService;
	
	@Override
	public void deleteByConvertId(String convertId, String subjectId) {
		teacherasessConvertResultDao.deleteByConvertId(convertId, subjectId);
	}
	@Override
	public List<TeacherasessConvertResult> findListByConvertId(String convertId, String subjectId, Pagination page) {
		Specification<TeacherasessConvertResult> specification=new Specification<TeacherasessConvertResult>() {
			@Override
			public Predicate toPredicate(Root<TeacherasessConvertResult> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb.equal(root.get("convertId").as(String.class), convertId));
				if(StringUtils.isNotBlank(subjectId)){
					ps.add(cb.equal(root.get("subjectId").as(String.class), subjectId));
				}
				List<Order> orderList = new ArrayList<>();
              orderList.add(cb.desc(root.get("score").as(Float.class)));
              orderList.add(cb.desc(root.get("id").as(String.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
	                return cq.getRestriction();
			}
		};
		List<TeacherasessConvertResult> list=new ArrayList<>();
		if(page!=null){
			list=findAll(specification, page);
		}else{
			list=findAll(specification);
		}
		return list;
	}
	
	@Override
	public void statResult(String convertId, String acadyear, String semester) {
		TeacherasessConvert convert = teacherasessConvertService.findOneWithMaster(convertId);
		List<TeacherasessConvertExam> exams = teacherasessConvertExamService.findListByConvertIdInWithMaster(new String[] {convertId});
		List<TeacherasessConvertGroup> groups = teacherasessConvertGroupService.findListByConvertIdInWithMaster(new String[] {convertId});
		Map<String, Float> examMap = EntityUtils.getMap(exams, TeacherasessConvertExam::getExamId, TeacherasessConvertExam::getScale);
//		Map<String, Student> stuMap = EntityUtils.getMap(SUtils.dt(studentRemoteService.findByGradeId(convert.getGradeId()), new TR<List<Student>>() {}), Student::getId);
//		Map<String, Student> stuMap = EntityUtils.getMap(SUtils.dt(studentRemoteService.findBySchoolId(convert.getUnitId()), new TR<List<Student>>() {}), Student::getId);
		Map<String, Clazz> clsMap = EntityUtils.getMap(SUtils.dt(classRemoteService.findBySchoolId(convert.getUnitId()), new TR<List<Clazz>>() {}), Clazz::getId);
//		Map<String, Grade> gradeMap = EntityUtils.getMap(SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(convert.getUnitId(), convert.getAcadyear()),new TR<List<Grade>>() {}),Grade::getId);
//		Grade grade = gradeMap.get(convert.getGradeId());
		
		teacherasessConvertResultDao.deleteByConvertId(convertId);
		
		Map<String, String> stuClsMap = new HashMap<>();
		//获取7选3科目
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73YSY(convert.getUnitId()), new TR<List<Course>>() {});
		Course allCourse = new Course();
		allCourse.setId(BaseConstants.ZERO_GUID);
		courseList.add(allCourse);
		//获得学生7选3科目
		Map<String, Set<String>> stuSubIdsMap = SUtils.dt(studentSelectSubjectRemoteService.findStuSelectMapByGradeId(acadyear, semester, convert.getGradeId()), new TR<Map<String, Set<String>>>(){});
		//赋分规则
		List<EmConversion> cons = emConversionService.findByUnitId(convert.getUnitId());
		
		Set<String> subSet=EntityUtils.getSet(courseList, e->e.getId());
		Map<String, Set<String>> xuankaoStuIdMap=SUtils.dt(studentSelectSubjectRemoteService.getStuSelectByGradeId(acadyear,semester,convert.getGradeId(),true, subSet.toArray(new String[0])),new TR<Map<String,Set<String>>>(){});
		//总分
		Map<String, Float> stuAllScoreMap = new HashMap<>();
		//十个科目折算分
		Map<String, Float> stuAllScoreMapNew = new HashMap<>();
		Map<String,String> zeroScoreMap=new HashMap<>(); 
		for(Course sub:courseList) {//00000000000000000000000000000006
			if(!StringUtils.equals(sub.getId(), BaseConstants.ZERO_GUID)) {
				//没个学生每次考试成绩
				Map<String, Map<String,Float>> stuExamScoreMap = new HashMap<>();
				//折算分
				Map<String, Float> scoreMap = new HashMap<>();
				for (TeacherasessConvertExam exam : exams) {
					List<EmScoreInfo> scoreList = emScoreInfoService.findByUnitIdAndExamIdAndSubjectId(convert.getUnitId(),exam.getExamId(),sub.getId());
					for (EmScoreInfo info : scoreList) {
						if(Float.valueOf(info.getScore())==0.0f || StringUtils.equals(info.getScoreStatus(), "1")){
							//0分 缺考不算成绩，且不算总分
							zeroScoreMap.put(info.getStudentId(), "true");
							continue;
						}
						/*if(StringUtils.equals(info.getScoreStatus(), "1")) {
							//缺考不算成绩
							continue;
						}*/
						if(!stuExamScoreMap.containsKey(info.getStudentId())) {
							stuExamScoreMap.put(info.getStudentId(), new HashMap<String,Float>());
						}
						stuClsMap.put(info.getStudentId(),info.getClassId());
						stuExamScoreMap.get(info.getStudentId()).put(exam.getExamId(), NumberUtils.toFloat(info.getScore()));
					}
				}
				
				if(!stuExamScoreMap.isEmpty()) {
					for (String stuId : stuExamScoreMap.keySet()) {
						float convertScore = 0f;
						Map<String, Float> scoreMap1 = stuExamScoreMap.get(stuId);
						if(scoreMap1.keySet().size() == exams.size()) {
							for (String examId : scoreMap1.keySet()) {
								float s1 = scoreMap1.get(examId);
								float scale = examMap.get(examId);
								convertScore = convertScore + s1*scale/100;
							}
						}else if(scoreMap1.keySet().size() == 2) {
							for(TeacherasessConvertGroup group:groups){
								boolean flag=false;
								for (String examId : scoreMap1.keySet()) {
									if(!group.getExamIds().contains(examId)){
										flag=true;//此时不是该组合
									}
								}
								if(!flag){
									String[] examIds=group.getExamIds().split(",");
									String[] scales=group.getScales().split(",");
									float s1 = scoreMap1.get(examIds[0]);
									float s2 = scoreMap1.get(examIds[1]);
									float scale1 = NumberUtils.toFloat(scales[0]);
									float scale2 = NumberUtils.toFloat(scales[1]);
									convertScore=s1*scale1/100+s2*scale2/100;
									break;
								}
							}
						}else if(scoreMap1.keySet().size() == 1) {
							for (String examId : scoreMap1.keySet()) {
								float s1 = scoreMap1.get(examId);
								convertScore = s1;
							}
						}
						scoreMap.put(stuId,convertScore);
					}
				}
				List<TeacherasessConvertResult> resultList = new ArrayList<>();
				TeacherasessConvertResult re;
				Map<String, Student> stuMap = EntityUtils.getMap(SUtils.dt(studentRemoteService.findListByIds(scoreMap.keySet().toArray(new String[0])), new TR<List<Student>>() {}), Student::getId);
				Set<String> stuIds=null;
				for (String stuId : scoreMap.keySet()) {
					re = new TeacherasessConvertResult();
					re.setId(UuidUtils.generateUuid());
					re.setUnitId(convert.getUnitId());
					re.setConvertId(convertId);
					re.setStudentId(stuId);
					re.setSubjectId(sub.getId());
					if(stuMap.containsKey(stuId)) {
						Student stu = stuMap.get(stuId);
						re.setStudentCode(stu.getStudentCode());
						re.setStudentName(stu.getStudentName());
						re.setClassId(stu.getClassId());
						re.setClassName(clsMap.containsKey(stu.getClassId())?clsMap.get(stu.getClassId()).getClassNameDynamic():"");
					}
					re.setScore(scoreMap.get(stuId));
					resultList.add(re);
				}
				sortRank(resultList);
				for (TeacherasessConvertResult e : resultList) {//10个科目的折算分
					if(stuAllScoreMapNew.containsKey(e.getStudentId())) {
						stuAllScoreMapNew.put(e.getStudentId(), stuAllScoreMapNew.get(e.getStudentId()) + e.getScore());
					}else {
						stuAllScoreMapNew.put(e.getStudentId(), e.getScore());
					}
				}
				if(Arrays.binarySearch(BaseConstants.SUBJECT_TYPES_YSY,sub.getSubjectCode()) >= 0) {
					for (TeacherasessConvertResult e : resultList) {
						if(stuAllScoreMap.containsKey(e.getStudentId())) {
							stuAllScoreMap.put(e.getStudentId(), stuAllScoreMap.get(e.getStudentId()) + e.getScore());
						}else {
							stuAllScoreMap.put(e.getStudentId(), e.getScore());
						}
					}
					saveAll(resultList.toArray(new TeacherasessConvertResult[0]));
					continue;
				}
				List<TeacherasessConvertResult> selectReList = new ArrayList<>();
				for (int i = 0;i<resultList.size();i++) {
					TeacherasessConvertResult e  = resultList.get(i);
					Set<String> subIds = stuSubIdsMap.get(e.getStudentId());
					if(CollectionUtils.isNotEmpty(subIds) && subIds.contains(sub.getId())) {
						selectReList.add(e);
						resultList.remove(i);
						i--;
					}
				}
				stuIds=xuankaoStuIdMap.get(sub.getId());
				if(StringUtils.isNotBlank(convert.getXuankaoType())&&StringUtils.equals(convert.getXuankaoType(), "1")){
					List<TeacherasessConvertResult> desList = new ArrayList<>();
					if(CollectionUtils.isNotEmpty(stuIds)){
						for (TeacherasessConvertResult teacherasessConvertResult : resultList) {
							if(stuIds.contains(teacherasessConvertResult.getStudentId())){
								desList.add(teacherasessConvertResult);
							}
						}
					}
					sortRank(desList);
					saveAll(desList.toArray(new TeacherasessConvertResult[0]));
					
				}else{
					saveAll(resultList.toArray(new TeacherasessConvertResult[0]));
				}
				if(selectReList.size() == 0) {
					continue;
				}
				List<TeacherasessConvertResult> insertlist = new ArrayList<>();
				countConScore(cons,selectReList,insertlist);
				if(CollectionUtils.isEmpty(insertlist)) {
					continue;
				}
				for (TeacherasessConvertResult e : insertlist) {
					if(stuAllScoreMap.containsKey(e.getStudentId())) {
						stuAllScoreMap.put(e.getStudentId(), stuAllScoreMap.get(e.getStudentId()) + e.getConScore());
					}else {
						stuAllScoreMap.put(e.getStudentId(), e.getConScore());
					}
				}
				if(StringUtils.isNotBlank(convert.getXuankaoType())&&StringUtils.equals(convert.getXuankaoType(), "1")){
					List<TeacherasessConvertResult> desList = new ArrayList<>();
					if(CollectionUtils.isNotEmpty(stuIds)){
						for (TeacherasessConvertResult teacherasessConvertResult : insertlist) {
							if(stuIds.contains(teacherasessConvertResult.getStudentId())){
								desList.add(teacherasessConvertResult);
							}
						}
					}
					sortRank(desList);
					saveAll(desList.toArray(new TeacherasessConvertResult[0]));
				}else{
					saveAll(insertlist.toArray(new TeacherasessConvertResult[0]));
				}
			}else {
				if(stuAllScoreMap.isEmpty()) {
					continue;
				}
				List<TeacherasessConvertResult> resultList = new ArrayList<>();
				TeacherasessConvertResult re;
				Map<String, Student> stuMap = EntityUtils.getMap(SUtils.dt(studentRemoteService.findListByIds(stuAllScoreMap.keySet().toArray(new String[0])), new TR<List<Student>>() {}), Student::getId);
				for (String stuId : stuAllScoreMap.keySet()) {
					if(zeroScoreMap.containsKey(stuId) && "true".equals(zeroScoreMap.get(stuId))){
						continue;//0分不算总分
					}
					re = new TeacherasessConvertResult();
					re.setId(UuidUtils.generateUuid());
					re.setUnitId(convert.getUnitId());
					re.setConvertId(convertId);
					re.setStudentId(stuId);
					re.setSubjectId(sub.getId());
					if(stuMap.containsKey(stuId)) {
						Student stu = stuMap.get(stuId);
						re.setStudentCode(stu.getStudentCode());
						re.setStudentName(stu.getStudentName());
						re.setClassId(stu.getClassId());
						re.setClassName(clsMap.containsKey(stu.getClassId())?clsMap.get(stu.getClassId()).getClassNameDynamic():"");
					}
					re.setScore((StringUtils.isNotBlank(convert.getXuankaoType())&&StringUtils.equals(convert.getXuankaoType(), "1"))?stuAllScoreMap.get(stuId):stuAllScoreMapNew.get(stuId));
					resultList.add(re);
				}
				sortRank(resultList);
				saveAll(resultList.toArray(new TeacherasessConvertResult[0]));
			}
		}
		convert.setStatus("2");
		teacherasessConvertService.save(convert);
		
	}
	
	private void countConScore(List<EmConversion> cons, List<TeacherasessConvertResult> list,List<TeacherasessConvertResult> insertlist) {
		Collections.sort(list, new Comparator<TeacherasessConvertResult>() {
			@Override
			public int compare(TeacherasessConvertResult arg0, TeacherasessConvertResult arg1) {
				if(arg0.getScore() > arg1.getScore()) {
					return -1;
				}else if(arg0.getScore() < arg1.getScore()) {
					return 1;
				}else {
					return 0;
				}
			}
		});
		if(CollectionUtils.isEmpty(cons)) {
			EmConversion con = new EmConversion();
			con.setBalance(100);
			con.setEndScore(0);
			con.setStartScore(0);
			cons.add(con);
		}
		Map<String,Set<String>> rankMap = new HashMap<>();
		Map<String,TeacherasessConvertResult> map = new HashMap<>(); 
		int maxRank = 1;
		float oldScore = 0f;
		for (int i = 0;i<list.size();i++) {
			map.put(list.get(i).getId(), list.get(i));
			if(i==0) {
				list.get(i).setXkRank(maxRank+"");
				oldScore = list.get(i).getScore();
				rankMap.put(maxRank+"", new HashSet<>());
			}else {
				if(oldScore > list.get(i).getScore()) {
					oldScore = list.get(i).getScore();
					maxRank = i+1;
					rankMap.put(maxRank+"", new HashSet<>());
				}
				list.get(i).setXkRank(maxRank+"");
			}
			rankMap.get(maxRank+"").add(list.get(i).getId());
		}
		int countMax = list.size();//12
		List<TeacherasessConvertResult> conInfos;
		int arrangeRank = 0;
		int blance = 0;
		int oldStuNumTo = 0;
		for (int i = 0;i<cons.size();i++) {
			conInfos = new ArrayList<>();
			EmConversion con = cons.get(i);
			int conBlace = con.getBalance()+blance;
			int stuNumTo = Math.round((float)(countMax*conBlace)/100);
			blance = con.getBalance() + blance;
			if(stuNumTo <= oldStuNumTo) {
				continue;
			}
			String nowRank;
			for (;arrangeRank<maxRank;) {
				nowRank = (arrangeRank +1)+"";
				if(rankMap.containsKey(nowRank)) {
					for (String ee : rankMap.get(nowRank)) {
						TeacherasessConvertResult e = map.get(ee);
						conInfos.add(e);
					}
					oldStuNumTo += rankMap.get(nowRank).size();
				}
				arrangeRank++;
				if(oldStuNumTo >= stuNumTo) {
					break;
				}
			}
			if(CollectionUtils.isNotEmpty(conInfos)) {
				float a = conInfos.get(0).getScore();
				float b = conInfos.get(conInfos.size()-1).getScore();
				float d = (float)con.getStartScore();
				float c = (float)con.getEndScore();
				for (TeacherasessConvertResult em : conInfos) {
					if(a == b) {
						em.setConScore(con.getEndScore());
					}else {
						float z = em.getScore();
						int x = Math.round((z*c-b*c+a*d-d*z)/(a-b));
						em.setConScore(x);
					}
					insertlist.add(em);
				}
			}
		}
		
	}

	private void sortRank(List<TeacherasessConvertResult> list) {
		Collections.sort(list, new Comparator<TeacherasessConvertResult>() {
			@Override
			public int compare(TeacherasessConvertResult arg0, TeacherasessConvertResult arg1) {
				if(arg0.getScore() > arg1.getScore()) {
					return -1;
				}else if(arg0.getScore() < arg1.getScore()) {
					return 1;
				}else {
					return 0;
				}
			}
		});
		int maxRank = 1;
		float oldScore = 0f;
		for (int i = 0;i<list.size();i++) {
			if(i==0) {
				list.get(i).setRank(maxRank);
				oldScore = list.get(i).getScore();
			}else {
				if(oldScore > list.get(i).getScore()) {
					oldScore = list.get(i).getScore();
					maxRank = i+1;
				}
			}
			list.get(i).setRank(maxRank);
		}
	}



	@Override
	protected BaseJpaRepositoryDao<TeacherasessConvertResult, String> getJpaDao() {
		return teacherasessConvertResultDao;
	}
	
	@Override
	public List<TeacherasessConvertResult> findByUnitIdAndConvertIdAndSubjectIdAndStudentIdIn(
			String unitId, String convertId, String subjectId,
			String... studentIds) {
		return teacherasessConvertResultDao.findByUnitIdAndConvertIdAndSubjectIdAndStudentIdIn(unitId, convertId, subjectId, studentIds);
	}


	@Override
	protected Class<TeacherasessConvertResult> getEntityClass() {
		return TeacherasessConvertResult.class;
	}
	
}
