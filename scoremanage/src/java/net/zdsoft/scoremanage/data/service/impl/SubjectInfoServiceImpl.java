package net.zdsoft.scoremanage.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
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

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dao.SubjectInfoDao;
import net.zdsoft.scoremanage.data.entity.Borderline;
import net.zdsoft.scoremanage.data.entity.ClassInfo;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.data.service.BorderlineService;
import net.zdsoft.scoremanage.data.service.ClassInfoService;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;

@Service("courseInfoService")
public class SubjectInfoServiceImpl extends BaseServiceImpl<SubjectInfo, String> implements SubjectInfoService{

	@Autowired
	private SubjectInfoDao subjectInfoDao;
	@Autowired
	private ClassInfoService classInfoService;
	@Autowired
	private GradeRemoteService gradeService;
	@Autowired
	private UnitRemoteService unitService;
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private CourseRemoteService courseService;
	@Autowired
	private ClassTeachingRemoteService classTeachingService;
	@Autowired
	private BorderlineService borderlineService;
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<SubjectInfo> findByUnitIdExamId(String schoolId,
			String examId, String... gradeCode) {
		List<SubjectInfo> returnList=subjectInfoDao.findByExamIdAndRangeType(examId, gradeCode);
		if(StringUtils.isNotBlank(schoolId) && CollectionUtils.isNotEmpty(returnList)){
			Unit u = SUtils.dc(unitService.findOneById(schoolId), Unit.class);
			if(u!=null && u.getUnitClass()==2){
				//学校 
				getOtherMsg(schoolId, returnList);
			}
		}
		return returnList;
	}

	private void getOtherMsg(String schoolId, List<SubjectInfo> returnList) {
		List<String> examInfoexIds=new ArrayList<String>();
		Map<String,LinkedHashSet<String>> courseInfoClassIdMap=new HashMap<String,LinkedHashSet<String>>();
		Map<String,LinkedHashSet<String>> courseInfoTeacheClassIdMap=new HashMap<String,LinkedHashSet<String>>();
		for(SubjectInfo info:returnList){
			examInfoexIds.add(info.getId());
		}
		List<ClassInfo> classInfoList = classInfoService.findBySchoolIdAndSubjectInfoIdIn(schoolId,examInfoexIds.toArray(new String[]{}));
		if(CollectionUtils.isNotEmpty(classInfoList)){
			
			for(ClassInfo item:classInfoList){
				if(ScoreDataConstants.CLASS_TYPE1.equals(item.getClassType())){
					if(!courseInfoClassIdMap.containsKey(item.getSubjectInfoId())){
						courseInfoClassIdMap.put(item.getSubjectInfoId(),new LinkedHashSet<String>());
					}
					courseInfoClassIdMap.get(item.getSubjectInfoId()).add(item.getClassId());
				}else{
					if(!courseInfoTeacheClassIdMap.containsKey(item.getSubjectInfoId())){
						courseInfoTeacheClassIdMap.put(item.getSubjectInfoId(),new LinkedHashSet<String>());
					}
					courseInfoTeacheClassIdMap.get(item.getSubjectInfoId()).add(item.getClassId());
				}
			}
			
			for(SubjectInfo info:returnList){
				if(courseInfoClassIdMap.containsKey(info.getId())){
					Set<String> classSet = courseInfoClassIdMap.get(info.getId());
					Map<String,String> map = new HashMap<String,String>();
					if(CollectionUtils.isNotEmpty(classSet)){
						info.setClassIds(classSet.toArray(new String[]{}));
						for(String id:classSet){
							map.put(id, id);
						}
					}
					info.setClassIdsMap(map);
				}
				if(courseInfoTeacheClassIdMap.containsKey(info.getId())){
					Set<String> classSet = courseInfoTeacheClassIdMap.get(info.getId());
					Map<String,String> map = new HashMap<String,String>();
					if(CollectionUtils.isNotEmpty(classSet)){
						info.setTeachClassIds(classSet.toArray(new String[]{}));
						for(String id:classSet){
							map.put(id, id);
						}
					}
					info.setTeachClassIdsMap(map);
				}
			}
		}
	}

	@Override
	protected BaseJpaRepositoryDao<SubjectInfo, String> getJpaDao() {
		return subjectInfoDao;
	}

	@Override
	protected Class<SubjectInfo> getEntityClass() {
		return SubjectInfo.class;
	}


	public List<SubjectInfo> findByExamIdIn(String schoolId, String... examIds) {
		List<SubjectInfo> returnList = subjectInfoDao.findByExamIdIn(examIds);
		if(StringUtils.isNotBlank(schoolId)){
			getOtherMsg(schoolId, returnList);
		}
		return returnList;
	}
	
	public void saveCourseInfoAll(String unitId, List<SubjectInfo> courseInfoList, String searchType, boolean isDeleteClass) throws Exception {
		if(CollectionUtils.isEmpty(courseInfoList)){
			return;
		}
		List<ClassInfo> addClassInfo = new ArrayList<ClassInfo>();
		ClassInfo classInfo = null;
		String id = null;
		Set<String> courseInfoIds = new HashSet<String>();
		for (SubjectInfo item : courseInfoList) {
			if(StringUtils.isBlank(item.getId())){
				id = UuidUtils.generateUuid();
				item.setId(id);
			}else{
				id=item.getId();
				courseInfoIds.add(id);
			}
			if(item.getClassIds()!=null)
			for(String claId:item.getClassIds()){
				classInfo = new ClassInfo();
				classInfo.setClassType(ScoreDataConstants.CLASS_TYPE1);
				classInfo.setClassId(claId);
				classInfo.setSchoolId(unitId);
				classInfo.setSubjectInfoId(id);
				addClassInfo.add(classInfo);
			}
			if(item.getTeachClassIds()!=null)
			for(String claId:item.getTeachClassIds()){
				classInfo = new ClassInfo();
				classInfo.setClassType(ScoreDataConstants.CLASS_TYPE2);
				classInfo.setClassId(claId);
				classInfo.setSchoolId(unitId);
				classInfo.setSubjectInfoId(id);
				addClassInfo.add(classInfo);
			}
		}
		if(CollectionUtils.isNotEmpty(courseInfoIds) && isDeleteClass){
			Set<String> classInfoIds = new HashSet<String>();
			List<ClassInfo> classInfoList = classInfoService.findBySchoolIdAndSubjectInfoIdIn(unitId,courseInfoIds.toArray(new String[]{}));
			if(CollectionUtils.isNotEmpty(classInfoList)){
				for (ClassInfo item : classInfoList) {
					classInfoIds.add(item.getId());
				}
				classInfoService.deleteAllByIds(classInfoIds.toArray(new String[0]));
			}
		}
		if(CollectionUtils.isNotEmpty(addClassInfo)){
			classInfoService.saveAllEntitys(addClassInfo.toArray(new ClassInfo[0]));
		}
		if("1".equals(searchType) && CollectionUtils.isNotEmpty(courseInfoList)){
			//保存前判读：考试是否存在、是否存在考试科目
			List<ExamInfo> examInfo = examInfoService.findNotDeletedByIdIn(courseInfoList.get(0).getExamId());
			if(CollectionUtils.isEmpty(examInfo)){
				throw new Exception("考试已不存在");
			}
			Set<String> courseId = new HashSet<String>();
			for (SubjectInfo item : courseInfoList) {
				courseId.add(item.getSubjectId());
			}
			List<SubjectInfo> findByExamIdAndRangeType = subjectInfoDao.findByExamIdAndRangeType(courseInfoList.get(0).getExamId(),courseInfoList.get(0).getRangeType());
			Map<String,SubjectInfo> map = new HashMap<String, SubjectInfo>();
			for (SubjectInfo item : findByExamIdAndRangeType) {
				map.put(item.getSubjectId(), item);
			}
			for (SubjectInfo item : courseInfoList) {
				SubjectInfo courseInfo = map.get(item.getSubjectId());
				if(courseInfo!=null && !item.getId().equals(courseInfo.getId())){
					throw new Exception("科目已存在");
				}
			}
			this.saveAllEntitys(courseInfoList.toArray(new SubjectInfo[0]));
		}
	}

	@Override
	public void deleteCourseInfoAll(SubjectInfo... cifs) {
		Set<String> courseInfoIds = new HashSet<String>();
		Map<String,Map<String,Set<String>>> examIdCourseIds = new HashMap<String,Map<String,Set<String>>>();//key examId gradeCode
		Map<String,Set<String>> setMap = null;
		Set<String> set = null;
		for (SubjectInfo item : cifs) {
			courseInfoIds.add(item.getId());
			setMap = examIdCourseIds.get(item.getExamId());
			if(setMap == null){
				setMap = new HashMap<String,Set<String>>();
				set = new HashSet<String>();
				setMap.put(item.getRangeType(), set);
			}
			set.add(item.getSubjectId());
		}
		//删除班级设置
		List<ClassInfo> classInfoList = classInfoService.findByCourseInfoIdIn(courseInfoIds.toArray(new String[0]));
		if(CollectionUtils.isNotEmpty(classInfoList)){
			Set<String> classInfoIds = new HashSet<String>();
			for (ClassInfo item : classInfoList) {
				classInfoIds.add(item.getId());
			}
			classInfoService.deleteAllByIds(classInfoIds.toArray(new String[0]));
		}
		//同时删除分数线设置
		Set<String> deleteBlIds = new HashSet<String>();
		List<Borderline> findBorderlineList = null;
		for(Map.Entry<String,Map<String,Set<String>>> entry : examIdCourseIds.entrySet()){
			for(Map.Entry<String,Set<String>> entryC : entry.getValue().entrySet()){
				findBorderlineList = borderlineService.findBorderlineList(entry.getKey(),entryC.getKey(), entryC.getValue().toArray(new String[0]));
				for (Borderline borderline : findBorderlineList) {
					deleteBlIds.add(borderline.getId());
				}
			}
		}
		if(CollectionUtils.isNotEmpty(deleteBlIds)){
			borderlineService.deleteAllByIds(deleteBlIds.toArray(new String[0]));
		}
			if(CollectionUtils.isNotEmpty(courseInfoIds))
			subjectInfoDao.deleteAllByIds(courseInfoIds.toArray(new String[0]));
	}

	@Override
	public int saveCopy(Unit unit,ExamInfo sourceExamInfo, ExamInfo oldExamInfo) {
		List<SubjectInfo> oldCourseInfoList = subjectInfoDao.findByExamIdIn(oldExamInfo.getId());
		if(CollectionUtils.isEmpty(oldCourseInfoList)){
			return 0;
		}
		Set<String> subjectIds = EntityUtils.getSet(oldCourseInfoList, e->e.getSubjectId());
		if(CollectionUtils.isEmpty(subjectIds)){
			return 0;
		}
		//根据subjectIds 取得对应course
		List<Course> courseList = SUtils.dt(courseService.findListByIds(subjectIds.toArray(new String[]{})), new TR<List<Course>>(){});
		
		
		Set<String> unitIds=new HashSet<String>();
		Unit djunit = SUtils.dc(unitService.findTopUnit(unit == null ? null : unit.getId()), Unit.class);
		if(unit.getUnitClass()==2 && !ScoreDataConstants.TKLX_3.equals(sourceExamInfo.getExamUeType())){
			//学校
			unitIds.add(unit.getId());
    		if(djunit!=null){
    			unitIds.add(djunit.getId());
    		}
		}else{
			if(djunit!=null){
    			unitIds.add(djunit.getId());
    		}
		}
		//学段对应科目  科目中学段是多选的
		Map<Integer,Map<String,Course>> courseMap = new HashMap<Integer, Map<String,Course>>();//key section courseId
		Map<String,Course> courseIdMap = new HashMap<String,Course>();//key courseId
//		List<Course> courseList = SUtils.dt(courseService.findByUnitIdIn(unitIds.toArray(new String[]{}),new String[]{}), new TR<List<Course>>(){});
		for (Course item : courseList) {
			//有权限的课程
			if(!unitIds.contains(item.getUnitId())){
				continue;
			}
			if(StringUtils.isBlank(item.getSection())){
				continue;
			}
			String[] sections=null;
			if(item.getSection().indexOf(",")>=0){
				sections=item.getSection().split(",");
			}else{
				sections=new String[]{item.getSection()};
			}
			for(String s:sections){
				if(StringUtils.isNotBlank(s)){
					Map<String, Course> map = courseMap.get(Integer.valueOf(s));
					if(map == null){
						map = new HashMap<String, Course>();
						courseMap.put(Integer.valueOf(s), map);
						
					}
					map.put(item.getId(), item);
					courseIdMap.put(item.getId(),item);
				}
			}
			
			
		}
		//年级code对应考试科目
		Map<Integer,Map<String,SubjectInfo>> scourceCourseInfoMap = new HashMap<Integer, Map<String,SubjectInfo>>();//key rangeType(gradeCode) courseId
		List<SubjectInfo> sourceCourseInfoList = subjectInfoDao.findByExamIdIn(sourceExamInfo.getId());
		for (SubjectInfo item : sourceCourseInfoList) {
			Map<String,SubjectInfo> map = scourceCourseInfoMap.get(Integer.valueOf(item.getRangeType()));
			if(map == null){
				map = new HashMap<String, SubjectInfo>();
				scourceCourseInfoMap.put(Integer.valueOf(item.getRangeType()), map);
			}
			map.put(item.getSubjectId(), item);
		}
		List<SubjectInfo> addCourseInfoList = new ArrayList<SubjectInfo>();
		int scourceAcadyear = NumberUtils.toInt(StringUtils.substringBefore(sourceExamInfo.getAcadyear(), "-"));
		int oldAcadyear = NumberUtils.toInt(StringUtils.substringBefore(oldExamInfo.getAcadyear(), "-"));
		/**
		 * 相差为0说明两个考试对应的学年是一样的，不存在升级问题
		 * 例：如果老数据与原数据相差1，说明老数据对原数据而言存在升级概念，老数据的小一相当于原数据的小二，所以这种情况下原数据的小一是不会产生新数据的
		 */
		int difVal = scourceAcadyear-oldAcadyear;
		
		
		String sourceRanges = sourceExamInfo.getRanges();
		
		
		
		//处理老数据中需要被拷贝的考试科目
		Map<String, SubjectInfo> linCifMap=null;
		for (SubjectInfo item : oldCourseInfoList) {
			//先判断所属学段是否有这门科目
			if(ScoreDataConstants.RANGE_TYPE99.equals(item.getRangeType())){
				Course course = courseIdMap.get(item.getSubjectId());
				if(course == null){
					continue;
				}
				//有这门科目的情况下，判断原数据中是否存在这门科目
				linCifMap = scourceCourseInfoMap.get(Integer.valueOf(item.getRangeType()));
				if(linCifMap != null){
					SubjectInfo courseInfo = linCifMap.get(item.getSubjectId());
					if(courseInfo == null){
						addCourseInfoList.add(item);
					}else{
						continue;
					}
				}else{
					addCourseInfoList.add(item);
				}
				item.setRangeType(String.valueOf(Integer.valueOf(item.getRangeType())));
			}else{
				
				Map<String, Course> csMap = courseMap.get(Integer.valueOf(item.getRangeType().substring(0, 1)));
				if(csMap != null){
					Course course = csMap.get(item.getSubjectId());
					if(course == null){
						continue;
					}
				}else{
					continue;
				}
				//有这门科目的情况下，判断原数据中是否存在这门科目
				/**
				 * 先判断考试跟复用的考试 gradeCode是否有相同 复用的 有的年级
				 */
				int rangeInt = Integer.valueOf(item.getRangeType())+difVal;
				if(sourceRanges.indexOf(String.valueOf(rangeInt)) ==-1) {
					continue;
				}
				
				linCifMap = scourceCourseInfoMap.get(Integer.valueOf(item.getRangeType())+difVal);
				if(linCifMap != null){
					SubjectInfo courseInfo = linCifMap.get(item.getSubjectId());
					if(courseInfo == null){
						addCourseInfoList.add(item);
					}else{
						continue;
					}
				}else{
					addCourseInfoList.add(item);
				}
				item.setRangeType(String.valueOf(Integer.valueOf(item.getRangeType())+difVal));
			}
			item.setExamId(sourceExamInfo.getId());//设置为原数据的考试id
		}
		Set<String> courseInfoIds = new HashSet<String>();
		Set<String> courseIds = new HashSet<String>();
		for (SubjectInfo item : addCourseInfoList) {
			courseInfoIds.add(item.getId());
			courseIds.add(item.getSubjectId());
		}
		if(unit.getUnitClass()!=2){
			//教育局
			for (SubjectInfo item : addCourseInfoList) {
				item.setId(UuidUtils.generateUuid());
			}
			entityManager.clear();//清除持久化，否则这里当作修改id主键，而不是新增
			this.saveAllEntitys(addCourseInfoList.toArray(new SubjectInfo[0]));
			return addCourseInfoList.size();
		}
		//处理需要拷贝的班级数据
		List<ClassInfo> addClassInfoList = new ArrayList<ClassInfo>();
		if(!ScoreDataConstants.TKLX_3.equals(sourceExamInfo.getExamUeType()) || (ScoreDataConstants.TKLX_3.equals(sourceExamInfo.getExamUeType()) && sourceExamInfo.getUnitId().equals(unit.getId()))){
			//校校不处理班级数据  增加学校设置的校校联考 可以复用
			Map<String, Map<String, ClassTeaching>> classTeachingMap = SUtils.dt(classTeachingService.findByCourseIdsMap(unit.getId(), 
					sourceExamInfo.getAcadyear(), sourceExamInfo.getSemester(), courseIds.toArray(new String[0])), new TR<Map<String, Map<String, ClassTeaching>>>(){});//key courseId classId
			Map<String,List<ClassInfo>> classInfoMap = new HashMap<String, List<ClassInfo>>();//key courseInfoId
			List<ClassInfo> findList = new ArrayList<ClassInfo>();
			if(CollectionUtils.isNotEmpty(courseInfoIds))
				findList = classInfoService.findList(unit.getId(), courseInfoIds.toArray(new String[0]));
			for (ClassInfo item : findList) {
				List<ClassInfo> list = classInfoMap.get(item.getSubjectInfoId());
				if(list == null){
					list = new ArrayList<ClassInfo>();
					classInfoMap.put(item.getSubjectInfoId(), list);
				}
				list.add(item);
			}
			for (SubjectInfo item : addCourseInfoList) {
				List<ClassInfo> list = classInfoMap.get(item.getId());
				String newId = UuidUtils.generateUuid();
				if(list != null){
					for (ClassInfo classInfo : list) {
						classInfo.setId(null);
						classInfo.setSubjectInfoId(newId);
						if(difVal == 0){
							//如果同一学年的考试拷贝，教学班也拷贝，否则不拷贝教学班数据
							if(ScoreDataConstants.CLASS_TYPE1.equals(classInfo.getClassType())){
								//行政班 判断是否学了这个科目
								Map<String, ClassTeaching> map = classTeachingMap.get(item.getSubjectId());
								if(map != null){
									ClassTeaching classTeaching = map.get(classInfo.getClassId());
									//除去走教学班的
									if(classTeaching == null || classTeaching.getIsTeaCls()==1){
										continue;
									}
								}else{
									continue;
								}
							}else{
								//教学班
							}
							addClassInfoList.add(classInfo);
						}else if(ScoreDataConstants.CLASS_TYPE1.equals(classInfo.getClassType())){
							//行政班 判断是否学了这个科目
							Map<String, ClassTeaching> map = classTeachingMap.get(item.getSubjectId());
							if(map != null){
								ClassTeaching classTeaching = map.get(classInfo.getClassId());
								if(classTeaching == null){
									continue;
								}
							}else{
								continue;
							}
							addClassInfoList.add(classInfo);
						}
					}
				}
				item.setId(newId);
			}
		}else{
			for (SubjectInfo item : addCourseInfoList) {
				item.setId(UuidUtils.generateUuid());
			}
		}
		entityManager.clear();//清除持久化，否则这里当作修改id主键，而不是新增
		if(CollectionUtils.isNotEmpty(addCourseInfoList)){
			this.saveAllEntitys(addCourseInfoList.toArray(new SubjectInfo[0]));
		}
		if(CollectionUtils.isNotEmpty(addClassInfoList)){
			classInfoService.saveAllEntitys(addClassInfoList.toArray(new ClassInfo[0]));
		}
		return addCourseInfoList.size();
	}
	
	@Override
	public int saveCopyClass(Unit unit,ExamInfo sourceExamInfo, ExamInfo oldExamInfo) {
		List<SubjectInfo> oldCourseInfoList = subjectInfoDao.findByExamIdIn(oldExamInfo.getId());
		if(CollectionUtils.isEmpty(oldCourseInfoList)){
			return 0;
		}
		List<SubjectInfo> sourceCourseInfoList = subjectInfoDao.findByExamIdIn(sourceExamInfo.getId());
		Set<String> oldCourseInfoIds=new HashSet<String>();
		Set<String> sourceCourseInfoIds=new HashSet<String>();
		for (SubjectInfo item : oldCourseInfoList) {
			oldCourseInfoIds.add(item.getId());
		}
		for (SubjectInfo item : sourceCourseInfoList) {
			sourceCourseInfoIds.add(item.getId());
		}
		List<ClassInfo> oldClassInfoList = classInfoService.findBySchoolIdAndSubjectInfoIdIn(unit.getId(), oldCourseInfoIds.toArray(new String[0]));
		Map<String,List<ClassInfo>> linCIFMap = new HashMap<String, List<ClassInfo>>();//key courseInfoId
		for (ClassInfo item : oldClassInfoList) {
			List<ClassInfo> list = linCIFMap.get(item.getSubjectInfoId());
			if(list == null){
				list = new ArrayList<ClassInfo>();
				linCIFMap.put(item.getSubjectInfoId(), list);
			}
			list.add(item);
		}
		Map<Integer,Map<String,List<ClassInfo>>> oldClassInfoMap = new HashMap<Integer, Map<String,List<ClassInfo>>>();//key rangeType(gradeCode) courseId
		for (SubjectInfo item : oldCourseInfoList) {
			Map<String, List<ClassInfo>> map = oldClassInfoMap.get(Integer.valueOf(item.getRangeType()));
			if(map == null){
				map = new HashMap<String, List<ClassInfo>>();
				oldClassInfoMap.put(Integer.valueOf(item.getRangeType()), map);
			}
			map.put(item.getSubjectId(), linCIFMap.get(item.getId()));
		}
		int scourceAcadyear = NumberUtils.toInt(StringUtils.substringBefore(sourceExamInfo.getAcadyear(), "-"));
		int oldAcadyear = NumberUtils.toInt(StringUtils.substringBefore(oldExamInfo.getAcadyear(), "-"));
		/**
		 * 相差为0说明两个考试对应的学年是一样的，不存在升级问题
		 * 例：如果老数据与原数据相差1，说明老数据对原数据而言存在升级概念，老数据的小一相当于原数据的小二，所以这种情况下原数据的小一是不会产生新数据的
		 */
		int difVal = scourceAcadyear-oldAcadyear;
		//处理需要拷贝的班级数据
		List<ClassInfo> addClassInfoList = new ArrayList<ClassInfo>();
		Map<String, List<ClassInfo>> linCifMap = null;
		for (SubjectInfo item : sourceCourseInfoList) {
			if(ScoreDataConstants.RANGE_TYPE99.equals(item.getRangeType())){
				linCifMap = oldClassInfoMap.get(Integer.valueOf(item.getRangeType()));
			}else{
				linCifMap = oldClassInfoMap.get(Integer.valueOf(item.getRangeType())-difVal);
			}
			if(linCifMap!=null){
				List<ClassInfo> list = linCifMap.get(item.getSubjectId());
				if(list!=null){
					for (ClassInfo classInfo : list) {
						classInfo.setId(UuidUtils.generateUuid());
						classInfo.setSubjectInfoId(item.getId());
						if(difVal == 0){
							//如果同一学年的考试拷贝，教学班也拷贝，否则不拷贝教学班数据
							addClassInfoList.add(classInfo);
						}else if(ScoreDataConstants.CLASS_TYPE1.equals(classInfo.getClassType())){
							//行政班
							addClassInfoList.add(classInfo);
						}
					}
				}
			}
		}
		entityManager.clear();//清除持久化，否则这里当作修改id主键，而不是新增
		if(CollectionUtils.isNotEmpty(addClassInfoList)){
			classInfoService.deleteBySchoolId(unit.getId(),sourceCourseInfoIds.toArray(new String[0]));
			classInfoService.saveAllEntitys(addClassInfoList.toArray(new ClassInfo[0]));
		}
		return addClassInfoList.size();
	}

	@Override
	public List<SubjectInfo> findByExamIdAndCourseId(String examId,
			String courseId) {
		if(StringUtils.isNotBlank(courseId)){
			return subjectInfoDao.findByExamIdAndSubjectId(examId,courseId);
		}else{
			return subjectInfoDao.findByExamIdIn(examId);
			
		}
	}

	@Override
	public List<SubjectInfo> findByExamIdClassId(String examId,
			String classIdSearch) {
		List<SubjectInfo> infoList=subjectInfoDao.findByExamIdClassId(examId,classIdSearch);
		if(CollectionUtils.isNotEmpty(infoList)){
			Set<String> courseIds=new HashSet<String>();
			for(SubjectInfo info:infoList){
				courseIds.add(info.getSubjectId());
			}
			List<Course> courses = SUtils.dt(courseService.findListByIds(courseIds.toArray(new String[0])), new TR<List<Course>>(){});
			Map<String, Course> courseMap = EntityUtils.getMap(courses, "id");
			for(SubjectInfo info:infoList){
				if(courseMap.containsKey(info.getSubjectId())){
					info.setCourseName(courseMap.get(info.getSubjectId()).getSubjectName());
				}
			}
		}
		return infoList;
	}

	@Override
	public List<SubjectInfo> saveAllEntitys(SubjectInfo... courseInfo) {
		return subjectInfoDao.saveAll(checkSave(courseInfo));
	}

	@Override
	public int saveGradeCourseCopy(String examId, String gradeCode, String[] toGradeCode) {
		List<String> finGradeCode = new ArrayList<String>();
		finGradeCode.add(gradeCode);
		finGradeCode.addAll(Arrays.asList(toGradeCode));
		List<SubjectInfo> subInfo = subjectInfoDao.findByExamIdAndRangeType(examId, finGradeCode.toArray(new String[0]));
		Map<String,Map<String,SubjectInfo>> subMap = new HashMap<String,Map<String,SubjectInfo>>();//key gradeCode subjectId
		Map<String,SubjectInfo> linSubMap = null;
		for (SubjectInfo item : subInfo) {
			linSubMap = subMap.get(item.getRangeType());
			if(linSubMap == null){
				linSubMap = new HashMap<String,SubjectInfo>();
				subMap.put(item.getRangeType(), linSubMap);
			}
			linSubMap.put(item.getSubjectId(), item);
		}
		Map<String, SubjectInfo> gradeCodeMap = subMap.get(gradeCode);//subjectId
		if(gradeCodeMap == null){
			return 0;
		}
		List<SubjectInfo> addSubList = new ArrayList<SubjectInfo>();
		SubjectInfo linSubInfo = null;
		for (String item : toGradeCode) {
			linSubMap = subMap.get(item);
			if(linSubMap == null){
				for(Map.Entry<String,SubjectInfo> entry : gradeCodeMap.entrySet()){
					linSubInfo = entry.getValue();
					linSubInfo = EntityUtils.copyProperties(linSubInfo, SubjectInfo.class);
					linSubInfo.setId(UuidUtils.generateUuid());
					linSubInfo.setRangeType(item);
					addSubList.add(linSubInfo);
				}
			}else{
				for(Map.Entry<String,SubjectInfo> entry : gradeCodeMap.entrySet()){
					linSubInfo = linSubMap.get(entry.getKey());
					//如果存在这科目则跳过，不覆盖
					if(linSubInfo != null){
						continue;
					}
					linSubInfo = entry.getValue();
					linSubInfo = EntityUtils.copyProperties(linSubInfo, SubjectInfo.class);
					linSubInfo.setId(UuidUtils.generateUuid());
					linSubInfo.setRangeType(item);
					addSubList.add(linSubInfo);
				}
			}
		}
		entityManager.clear();//清除持久化，否则这里当作修改id主键，而不是新增
		if(CollectionUtils.isNotEmpty(addSubList)){
			this.saveAllEntitys(addSubList.toArray(new SubjectInfo[0]));
		}
		return 1;
	}

	@Override
	public List<SubjectInfo> findByExamIdGradeId(final String examId, final String gradeId) {
		List<SubjectInfo> infoList = new ArrayList<SubjectInfo>();
		ExamInfo findOne = RedisUtils.getObject("scoremanage_examInfo_id_"+examId, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<ExamInfo>(){}, new RedisInterface<ExamInfo>(){
			@Override
			public ExamInfo queryData() {
				return examInfoService.findOne(examId);
			}
		});
		Grade grade = RedisUtils.getObject("scoremanage_grade_id_"+gradeId, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<Grade>(){}, new RedisInterface<Grade>(){
			@Override
			public Grade queryData() {
				return SUtils.dt(gradeService.findOneById(gradeId), new TypeReference<Grade>(){});
			}
		});
		int examAcadyear = NumberUtils.toInt(StringUtils.substringBefore(findOne.getAcadyear(), "-"));
		int openAcadyear = NumberUtils.toInt(StringUtils.substringBefore(grade.getOpenAcadyear(), "-"));
		int difCode = examAcadyear-openAcadyear+1;
		if( difCode < 1 || difCode > grade.getSchoolingLength()){
			return infoList;
		}
		String gradeCode = grade.getSection()+""+difCode;
		infoList = subjectInfoDao.findByExamIdAndRangeType(examId, gradeCode);
		if(CollectionUtils.isNotEmpty(infoList)){
			Set<String> courseIds=new HashSet<String>();
			for(SubjectInfo info:infoList){
				courseIds.add(info.getSubjectId());
			}
			List<Course> courses = SUtils.dt(courseService.findListByIds(courseIds.toArray(new String[0])), new TR<List<Course>>(){});
			Map<String, Course> courseMap = EntityUtils.getMap(courses, "id");
			for(SubjectInfo info:infoList){
				if(courseMap.containsKey(info.getSubjectId())){
					info.setCourseName(courseMap.get(info.getSubjectId()).getSubjectName());
				}
			}
		}
		return infoList;
	}

	@Override
	public List<SubjectInfo> findByExamId(String examId) {
		return subjectInfoDao.findByExamId(examId);
	}

	@Override
	public List<SubjectInfo> findByExamIdAndCourseIdAndUnitId(String unitId,
			String examId, String courseId) {
		return subjectInfoDao.findByExamIdAndCourseIdAndUnitId(unitId, examId, courseId);
	}

	@Override
	public List<SubjectInfo> findBySubjectInfoList(String examId,
			String unitId, String classId) {
		List<SubjectInfo> infoList=new ArrayList<SubjectInfo>();
		if(StringUtils.isNotBlank(classId)){
			infoList=subjectInfoDao.findByExamIdClassId(examId,classId);
		}else{
			infoList=subjectInfoDao.findByExamIdUnitId(examId,unitId);
		}
		return infoList;
	}
	
	public void saveCourseInfoAll(ExamInfo examInfo,String unitId, List<SubjectInfo> courseInfoList,boolean isCanEditSubject,boolean isCanEditClass) throws Exception {
		if(CollectionUtils.isEmpty(courseInfoList)){
			return;
		}
		List<ClassInfo> addClassInfo = new ArrayList<ClassInfo>();
		ClassInfo classInfo = null;
		String id = null;
		Set<String> courseInfoIds = new HashSet<String>();
		for (SubjectInfo item : courseInfoList) {
			if(StringUtils.isBlank(item.getId())){
				id = UuidUtils.generateUuid();
				item.setId(id);
			}else{
				id=item.getId();
				courseInfoIds.add(id);
			}
			if(isCanEditClass){
				if(item.getClassIds()!=null){
					for(String claId:item.getClassIds()){
						classInfo = new ClassInfo();
						classInfo.setClassType(ScoreDataConstants.CLASS_TYPE1);
						classInfo.setClassId(claId);
						classInfo.setSchoolId(unitId);
						classInfo.setSubjectInfoId(id);
						//默认值"0"
						classInfo.setIsLock("0");
						addClassInfo.add(classInfo);
					}
				}
				if(item.getTeachClassIds()!=null){
					for(String claId:item.getTeachClassIds()){
						classInfo = new ClassInfo();
						classInfo.setClassType(ScoreDataConstants.CLASS_TYPE2);
						classInfo.setClassId(claId);
						classInfo.setSchoolId(unitId);
						classInfo.setSubjectInfoId(id);
						//默认值"0"
						classInfo.setIsLock("0");
						addClassInfo.add(classInfo);
					}
				}
			}
			
		}
		if(CollectionUtils.isNotEmpty(courseInfoIds) && isCanEditClass){
			Set<String> classInfoIds = new HashSet<String>();
			List<ClassInfo> classInfoList = classInfoService.findBySchoolIdAndSubjectInfoIdIn(unitId,courseInfoIds.toArray(new String[]{}));
			if(CollectionUtils.isNotEmpty(classInfoList)){
				for (ClassInfo item : classInfoList) {
					classInfoIds.add(item.getId());
				}
				classInfoService.deleteAllByIds(classInfoIds.toArray(new String[0]));
			}
		}
		if(CollectionUtils.isNotEmpty(addClassInfo)){
			classInfoService.saveAllEntitys(addClassInfo.toArray(new ClassInfo[0]));
		}
		if(isCanEditSubject && CollectionUtils.isNotEmpty(courseInfoList)){
			//保存前判读：是否存在考试科目
			Set<String> courseId = new HashSet<String>();
			for (SubjectInfo item : courseInfoList) {
				courseId.add(item.getSubjectId());
			}
			List<SubjectInfo> findByExamIdAndRangeType = subjectInfoDao.findByExamIdAndRangeType(courseInfoList.get(0).getExamId(),courseInfoList.get(0).getRangeType());
			Map<String,SubjectInfo> map = new HashMap<String, SubjectInfo>();
			for (SubjectInfo item : findByExamIdAndRangeType) {
				map.put(item.getSubjectId(), item);
			}
			for (SubjectInfo item : courseInfoList) {
				SubjectInfo courseInfo = map.get(item.getSubjectId());
				if(courseInfo!=null && !item.getId().equals(courseInfo.getId())){
					throw new Exception("科目已存在");
				}
			}
			this.saveAllEntitys(courseInfoList.toArray(new SubjectInfo[0]));
		}
	}

	@Override
	public SubjectInfo findByExamIdAndCourseIdAndGradeCode(String examId, String courseId, String gradeCode) {
		return subjectInfoDao.findByExamIdAndSubjectIdAndRangeType(examId, courseId, gradeCode);
	}

}
