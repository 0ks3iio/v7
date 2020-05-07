package net.zdsoft.gkelective.data.service.impl;

import java.util.ArrayList;
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

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dao.GkStuRemarkDao;
import net.zdsoft.gkelective.data.dto.ChosenSubjectSearchDto;
import net.zdsoft.gkelective.data.dto.GkStuScoreDto;
import net.zdsoft.gkelective.data.entity.GkStuRemark;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.service.GkStuRemarkService;
import net.zdsoft.gkelective.data.service.GkSubjectArrangeService;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;
import net.zdsoft.scoremanage.remote.service.ScoreInfoRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

@Service("gkStuRemarkService")
public class GkStuRemarkServiceImpl  extends BaseServiceImpl<GkStuRemark, String> implements GkStuRemarkService{
	@Autowired
	private GkStuRemarkDao gkStuRemarkDao;
	@Autowired
	private StudentRemoteService studentService;
	@Autowired
	private GkSubjectArrangeService gkSubjectArrangeService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private ScoreInfoRemoteService scoreInfoRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	
	@Override
	protected BaseJpaRepositoryDao<GkStuRemark, String> getJpaDao() {
		return gkStuRemarkDao;
	}

	@Override
	protected Class<GkStuRemark> getEntityClass() {
		return GkStuRemark.class;
	}

	@Override
	public void deleteByStudentIdsAndArrangeId(String subjectArrangeId, String type, String[] studentIds) {
		if(ArrayUtils.isEmpty(studentIds)){
    		return;
    	}
    	if(studentIds.length>1000){
			int length = studentIds.length;
			int cyc = length / 1000 + (length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > length)
					max = length;
				String[] stuId = ArrayUtils.subarray(studentIds, i * 1000, max);
				gkStuRemarkDao.deleteByStudentIdsAndArrangeId(subjectArrangeId, type, stuId);
			}
    	}else{
    		gkStuRemarkDao.deleteByStudentIdsAndArrangeId(subjectArrangeId, type, studentIds);
    	}
		
		
	}

	@Override
	public void saveAll(List<GkStuRemark> stuRemarkList) {
		if(CollectionUtils.isEmpty(stuRemarkList)){
			return;
		}
		checkSave(stuRemarkList.toArray(new GkStuRemark[0]));
		gkStuRemarkDao.saveAll(stuRemarkList);
	}
	
	@Override
	public List<GkStuRemark> findByStudentIds(String subjectArrangeId, String type, String[] studentIds) {
		if(ArrayUtils.isEmpty(studentIds)){
    		return new ArrayList<GkStuRemark>();
    	}
		List<GkStuRemark> returnList = new ArrayList<GkStuRemark>();
    	if(studentIds.length>1000){
			int length = studentIds.length;
			int cyc = length / 1000 + (length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > length)
					max = length;
				String[] stuId = ArrayUtils.subarray(studentIds, i * 1000, max);
				List<GkStuRemark> list = gkStuRemarkDao.findByStudentIds(subjectArrangeId, type, stuId);
				if(CollectionUtils.isNotEmpty(list)){
					returnList.addAll(list);
				}
			}
    	}else{
    		returnList=gkStuRemarkDao.findByStudentIds(subjectArrangeId, type, studentIds);
    	}
		
		
		return returnList;
	}

	@Override
	public List<GkStuScoreDto> findStuScoreDtoList(final String arrangeId, final ChosenSubjectSearchDto searchDto, Pagination page) {
		Student searchStudent = new Student();
		if (("1").equals(searchDto.getSearchSelectType())) {
        	// 学号模糊查询
        	if(StringUtils.isNotBlank(searchDto.getSearchCondition())){
        		//防止注入
        		String linStr = searchDto.getSearchCondition().replaceAll("%", "");
        		//右匹配
        		searchStudent.setStudentCode(linStr+ "%");
        	}
        }
        else {
        	// 姓名
        	if(StringUtils.isNotBlank(searchDto.getSearchCondition())){
        		//防止注入
        		String linStr = searchDto.getSearchCondition().replaceAll("%", "");
        		//右匹配
        		searchStudent.setStudentName(linStr+ "%");
        	}
        }
		final GkSubjectArrange gkArrange = RedisUtils.getObject(GkElectveConstants.GK_ARRANGE_KEY+arrangeId, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<GkSubjectArrange>(){}, new RedisInterface<GkSubjectArrange>(){
			@Override
			public GkSubjectArrange queryData() {
				return gkSubjectArrangeService.findArrangeById(arrangeId);
			}
		});
		List<Student> studentList = new ArrayList<Student>();
		Map<String,String> classNameMap = new HashMap<String, String>();
		if (StringUtils.isBlank(searchDto.getSearchClassId())) {
            // 班级id参数为空 ---- 按年级查询则分页
	        List<Clazz> clazzList = RedisUtils.getObject(GkElectveConstants.GRADE_CLASS_LIST_KEY+gkArrange.getGradeId(), RedisUtils.TIME_FIVE_MINUTES, new TypeReference<List<Clazz>>(){}, new RedisInterface<List<Clazz>>(){
				@Override
				public List<Clazz> queryData() {
					return SUtils.dt(classRemoteService.findBySchoolIdGradeId(gkArrange.getUnitId(),gkArrange.getGradeId()),new TR<List<Clazz>>() {});
				}
	        });
	        for (Clazz item : clazzList) {
	        	classNameMap.put(item.getId(), item.getClassNameDynamic());
			}
	        Set<String> classIds = EntityUtils.getSet(clazzList, "id");
	        if(page!=null){
	        	studentList = Student.dt(studentService.findByIdsClaIdLikeStuCodeNames(gkArrange.getUnitId(),
	        			null, classIds.toArray(new String[0]), Json.toJSONString(searchStudent), SUtils.s(page)), page);
	        }else{
	        	studentList = Student.dt(studentService.findByIdsClaIdLikeStuCodeNames(gkArrange.getUnitId(),
	        			null, classIds.toArray(new String[0]), Json.toJSONString(searchStudent), null));
	        }
        }
        else {
        	Clazz clazz = RedisUtils.getObject(GkElectveConstants.GRADE_CLASS_ID_KEY+searchDto.getSearchClassId(), RedisUtils.TIME_FIVE_MINUTES, new TypeReference<Clazz>(){}, new RedisInterface<Clazz>(){
				@Override
				public Clazz queryData() {
					List<Clazz> dt = SUtils.dt(classRemoteService.findClassListByIds(new String[]{searchDto.getSearchClassId()}),new TR<List<Clazz>>() {});
					return CollectionUtils.isNotEmpty(dt)?dt.get(0):null;
				}
	        });
        	classNameMap.put(clazz.getId(), clazz.getClassNameDynamic());
            // 班级id参数不为空 ---- 按班级查询则不分页
            studentList = Student.dt(studentService.findByIdsClaIdLikeStuCodeNames(gkArrange.getUnitId(),
            		null, new String[]{searchDto.getSearchClassId()}, Json.toJSONString(searchStudent), null));
        }
		Set<String> studentIds = EntityUtils.getSet(studentList, "id");
		List<GkStuRemark> stuRemarkList = new ArrayList<GkStuRemark>();
		if(CollectionUtils.isNotEmpty(studentIds)){
//			stuRemarkList = gkStuRemarkDao.findByStudentIds(arrangeId, searchDto.getSearchScoreType(), studentIds.toArray(new String[0]));
//			stuRemarkList = gkStuRemarkDao.findByStudentIds(arrangeId, new String[]{GkStuRemark.TYPE_SCORE,GkStuRemark.TYPE_SCORE_YSY}, studentIds.toArray(new String[0]));
			stuRemarkList =findByStudentIds(arrangeId, new String[]{GkStuRemark.TYPE_SCORE,GkStuRemark.TYPE_SCORE_YSY}, studentIds.toArray(new String[0]));
		}
		Map<String,List<GkStuRemark>> stuRemarkMap = new HashMap<String, List<GkStuRemark>>();
		List<GkStuRemark> linList = null;
		for (GkStuRemark item : stuRemarkList) {
			linList = stuRemarkMap.get(item.getStudentId());
			if(linList == null){
				linList = new ArrayList<GkStuRemark>();
				stuRemarkMap.put(item.getStudentId(), linList);
			}
			linList.add(item);
		}
		List<GkStuScoreDto> resultList = new ArrayList<GkStuScoreDto>();
		GkStuScoreDto linDto = null;
		List<GkStuRemark> stuRemarkLinList = null;
		for (Student item : studentList) {
			String string = classNameMap.get(item.getClassId());
			if(StringUtils.isNotBlank(string)){
				item.setClassName(string);
			}else{
				item.setClassName("未找到");
			}
			linDto = new GkStuScoreDto();
			linDto.setStudent(item);
			stuRemarkLinList = stuRemarkMap.get(item.getId());
			if(stuRemarkLinList!=null){
				for (GkStuRemark gkStuRemark : stuRemarkLinList) {
					linDto.getSubjectScore().put(gkStuRemark.getSubjectId(), gkStuRemark.getScore());
				}
			}
			resultList.add(linDto);
		}
		return resultList;
	}

	@Override
	public void saveStuScore(String unitId,String arrangeId, String searchExamId, String[] subjectIds) {
		if(ArrayUtils.isEmpty(subjectIds)){
			throw new ControllerException("未找到科目！");
		}
		Set<String> finSubjectIds = new HashSet<String>();
		boolean isYsy = false;//是否有语数英
		for (String string : subjectIds) {
			if(!GkStuRemark.YSY_SUBID.equals(string)){
				finSubjectIds.add(string);
			}else{
				isYsy = true;
			}
		}
		GkStuRemark linStuRem = null;
		List<GkStuRemark> stuScoreList = new ArrayList<GkStuRemark>();
		if(finSubjectIds.size() > 0){
			List<ScoreInfo> scoreInfoList = SUtils.dt(scoreInfoRemoteService.findScoreInfoList(searchExamId, finSubjectIds.toArray(new String[0])), new TR<List<ScoreInfo>>(){});
			if(CollectionUtils.isEmpty(scoreInfoList)){
				throw new ControllerException("未找到成绩数据！");
			}
			for (ScoreInfo item : scoreInfoList) {
				if(!ScoreInfo.ACHI_SCORE.equals(item.getInputType())){
					continue;
				}
				linStuRem = new GkStuRemark();
				linStuRem.setSubjectArrangeId(arrangeId);
				linStuRem.setType(GkStuRemark.TYPE_SCORE);
				linStuRem.setStudentId(item.getStudentId());
				linStuRem.setSubjectId(item.getSubjectId());
				linStuRem.setScore(Double.valueOf(item.getScore()));
				stuScoreList.add(linStuRem);
			}
		}
		if(isYsy){
			List<Course> ysyCourseList = SUtils.dt(courseRemoteService.findByCodesYSY(unitId),new TR<List<Course>>() {});
			Set<String> subIds = EntityUtils.getSet(ysyCourseList, "id");
			List<ScoreInfo> scoInfoList = SUtils.dt(scoreInfoRemoteService.findScoreInfoList(searchExamId, subIds.toArray(new String[0])), new TR<List<ScoreInfo>>(){});
			Map<String,Double> stuScore = new HashMap<String, Double>();
			Double sco = null;
			for (ScoreInfo item : scoInfoList) {
				if(!ScoreInfo.ACHI_SCORE.equals(item.getInputType())){
					continue;
				}
				sco = stuScore.get(item.getStudentId());
				if(sco == null){
					stuScore.put(item.getStudentId(), Double.valueOf(item.getScore()));
				}else{
					stuScore.put(item.getStudentId(), sco + Double.valueOf(item.getScore()));
				}
			}
			for(Map.Entry<String,Double> entry : stuScore.entrySet()){
				linStuRem = new GkStuRemark();
				linStuRem.setSubjectArrangeId(arrangeId);
				linStuRem.setType(GkStuRemark.TYPE_SCORE_YSY);
				linStuRem.setStudentId(entry.getKey());
				linStuRem.setSubjectId(GkStuRemark.YSY_SUBID);
				linStuRem.setScore(entry.getValue());
				stuScoreList.add(linStuRem);
			}
			gkStuRemarkDao.deleteBySubjectIds(arrangeId,GkStuRemark.TYPE_SCORE_YSY,new String[]{GkStuRemark.YSY_SUBID});
		}
		
		//先删除全部再增加
		if(finSubjectIds.size() > 0)
			gkStuRemarkDao.deleteBySubjectIds(arrangeId,GkStuRemark.TYPE_SCORE,finSubjectIds.toArray(new String[0]));
		gkStuRemarkDao.saveAll(checkSave(stuScoreList.toArray(new GkStuRemark[0])));
	}

	@Override
	public List<GkStuRemark> findStuScoreList(final String arrangeId, final String[] subjectId, final String[] studentId) {
		if(ArrayUtils.isEmpty(subjectId) || ArrayUtils.isEmpty(studentId)){
			return new ArrayList<GkStuRemark>();
		}
		Specification<GkStuRemark> s = new Specification<GkStuRemark>() {
            @Override
            public Predicate toPredicate(Root<GkStuRemark> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("subjectArrangeId").as(String.class), arrangeId));
                queryIn("subjectId", subjectId, root, ps,cb);
                queryIn("studentId", studentId, root, ps,cb);
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.asc(root.get("subjectId").as(String.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
		return gkStuRemarkDao.findAll(s);
	}

	@Override
	public Map<String, Double> findByArrangeIdType(String subjectArrangeId,String type) {
		Map<String, Double> map =new HashMap<String, Double>();
		 List<GkStuRemark>  entlist = gkStuRemarkDao.findByArrangeIdType(subjectArrangeId, type);
		 if(CollectionUtils.isNotEmpty(entlist)){
			 for(GkStuRemark ent:entlist){
				 map.put(ent.getStudentId()+ent.getSubjectId(), ent.getScore());
			 }
		 }
		 
		return map;
	}

	@Override
	public List<GkStuRemark> findByStudentIds(String subjectArrangeId, String[] types, String[] studentIds) {
		List<GkStuRemark> returnList=new ArrayList<GkStuRemark>();
		if(studentIds.length>1000){
			int length = studentIds.length;
			int cyc = length / 1000 + (length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > length)
					max = length;
				String[] stuId = ArrayUtils.subarray(studentIds, i * 1000, max);
				List<GkStuRemark> list = gkStuRemarkDao.findByStudentIds(subjectArrangeId, types, stuId);
				if(CollectionUtils.isNotEmpty(list)){
					returnList.addAll(list);
				}
			}
    	}else{
    		returnList=gkStuRemarkDao.findByStudentIds(subjectArrangeId, types, studentIds);
    	}
		return returnList;
	}

	
}
