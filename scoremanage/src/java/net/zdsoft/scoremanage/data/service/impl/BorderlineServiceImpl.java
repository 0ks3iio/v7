package net.zdsoft.scoremanage.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dao.BorderlineDao;
import net.zdsoft.scoremanage.data.entity.Borderline;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.data.service.BorderlineService;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;

@Service("borderlineService")
public class BorderlineServiceImpl extends BaseServiceImpl<Borderline, String> implements BorderlineService{

	@Autowired
	private BorderlineDao borderlineDao;
	@Autowired
	private CourseRemoteService courseService;
	@Autowired
	private SubjectInfoService subjectInfoService;
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	protected BaseJpaRepositoryDao<Borderline, String> getJpaDao() {
		return borderlineDao;
	}

	@Override
	protected Class<Borderline> getEntityClass() {
		return Borderline.class;
	}

	@Override
	public int saveCopy(ExamInfo sourceExamInfo, ExamInfo oldExamInfo) {
		List<Borderline> oldBorderlineList = this.findBorderlineListByExamId(oldExamInfo.getId(),null);
		if(CollectionUtils.isEmpty(oldBorderlineList)){
			return 0;
		}
		List<Borderline> addList = new ArrayList<Borderline>();
		//考试对应科目
		List<SubjectInfo> subjectInfoList = subjectInfoService.findByExamIdIn(null, new String[]{sourceExamInfo.getId()});
		Map<Integer,Set<String>> scourceCourseMap = new HashMap<Integer,Set<String>>(); //key greadCode value subjectId
		Set<String> linStrSet = null;
		for (SubjectInfo item : subjectInfoList) {
			linStrSet = scourceCourseMap.get(Integer.parseInt(item.getRangeType()));
			if(linStrSet == null){
				linStrSet = new HashSet<String>();
				scourceCourseMap.put(Integer.valueOf(item.getRangeType()), linStrSet);
				linStrSet.add(ScoreDataConstants.ZERO32);
			}
			linStrSet.add(item.getSubjectId());
		}
		List<Borderline> borderlineList = this.findBorderlineListByExamId(sourceExamInfo.getId(),null);
		//分数段map
		Map<Integer,Map<String,List<Borderline>>> garMap = new HashMap<Integer,Map<String,List<Borderline>>>();//key gradeCode subjectId
		Map<String, List<Borderline>> linMap = null;
		List<Borderline> blList = null;
		for (Borderline item : borderlineList) {
			if(ScoreDataConstants.statType1.containsKey(item.getStatType())){
				//分数线
			}else if(ScoreDataConstants.statType2.containsKey(item.getStatType())){
				//分数段
				linMap = garMap.get(Integer.valueOf(item.getGradeCode()));
				if(linMap == null){
					linMap = new HashMap<String, List<Borderline>>();
					garMap.put(Integer.valueOf(item.getGradeCode()), linMap);
					blList = new ArrayList<Borderline>();
					linMap.put(item.getSubjectId(), blList);
				}
				blList.add(item);
			}
		}
		int scourceAcadyear = NumberUtils.toInt(StringUtils.substringBefore(sourceExamInfo.getAcadyear(), "-"));
		int oldAcadyear = NumberUtils.toInt(StringUtils.substringBefore(oldExamInfo.getAcadyear(), "-"));
		/**
		 * 相差为0说明两个考试对应的学年是一样的，不存在升级问题
		 * 例：如果老数据与原数据相差1，说明老数据对原数据而言存在升级概念，老数据的小一相当于原数据的小二，所以这种情况下原数据的小一是不会产生新数据的
		 */
		int difVal = scourceAcadyear-oldAcadyear;
		//处理要复制的数据
		for (Borderline item : oldBorderlineList) {
			//先判断是否有这个学段的数据
			if(scourceCourseMap.get(Integer.valueOf(item.getGradeCode())) == null){
				continue;
			}
			//判断该学段是否有这门科目
			if(!scourceCourseMap.get(Integer.valueOf(item.getGradeCode())+difVal).contains(item.getSubjectId())){
				continue;
			}
			if(ScoreDataConstants.statType1.containsKey(item.getStatType())){
				//分数线直接新增
			}else if(ScoreDataConstants.statType2.containsKey(item.getStatType())){
				//分数段判断是否已经有设置，有的话跳过
				if(garMap.get(Integer.valueOf(item.getGradeCode())+difVal) != null){
					continue;
				}
			}
			//重新赋值
			item.setId(UuidUtils.generateUuid());
			item.setGradeCode(String.valueOf(Integer.valueOf(item.getGradeCode())+difVal));
			item.setExamId(sourceExamInfo.getId());
			addList.add(item);
		}
		entityManager.clear();//清除持久化，否则这里当作修改id主键，而不是新增
		if(CollectionUtils.isNotEmpty(addList))
			this.saveAllEntitys(addList.toArray(new Borderline[0]));
		return addList.size();
	}

	@Override
	public List<Borderline> findBorderlineListByExamId(String examId,String gradeCode) {
		List<Borderline> list = new ArrayList<Borderline>();
		if(StringUtils.isNotBlank(gradeCode)){
			list = borderlineDao.findBorderlineList(examId,gradeCode);
		}else{
			list = borderlineDao.findBorderlineList(examId);
		}
		Set<String>subjectIds=new HashSet<String>();
		for (Borderline item : list) {
			if(!ScoreDataConstants.ZERO32.equals(item.getSubjectId())){
				subjectIds.add(item.getSubjectId());
			}else{
				item.setCourseName("总");
			}
		}
		List<Course> courseList = SUtils.dt(courseService.findListByIds(subjectIds.toArray(new String[0])), new TR<List<Course>>(){});
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, "id");
		for (Borderline item : list) {
			if(ScoreDataConstants.ZERO32.equals(item.getSubjectId()))
				continue;
			Course course = courseMap.get(item.getSubjectId());
			if(course!=null){
				item.setCourseName(course.getShortName());
			}else{
				item.setCourseName("未找到");
			}
		}
		return list;
	}

	@Override
	public List<Borderline> findBorderlineList(String examId, String gradeCode, String... subjectIds) {
		return borderlineDao.findBorderlineList(examId,gradeCode, subjectIds);
	}
	@Override
	public List<Borderline> findBorderlineList(String examId, String gradeCode,String statType, String subjectId) {
		return borderlineDao.findBorderlineList(examId,gradeCode,statType, subjectId);
	}

	@Override
	public List<Borderline> findSubjectId32(String examId, String gradeCode, String... statType) {
		return borderlineDao.findSubjectId32(examId,gradeCode, statType);
	}

	@Override
	public List<Borderline> saveAllEntitys(Borderline... borderline) {
		return borderlineDao.saveAll(checkSave(borderline));
	}

	@Override
	public void deleteAllByIds(String... id) {
		if(id!=null && id.length>0)
			borderlineDao.deleteAllByIds(id);
	}

}
