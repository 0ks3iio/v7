package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.TeachPlanDao;
import net.zdsoft.basedata.entity.TeachPlan;
import net.zdsoft.basedata.service.TeachPlanService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("teachPlanService")
public class TeachPlanServiceImpl extends BaseServiceImpl<TeachPlan, String> implements TeachPlanService {

	@Autowired
	private TeachPlanDao teachPlanDao;
	
	@Override
	protected BaseJpaRepositoryDao<TeachPlan, String> getJpaDao() {
		return teachPlanDao;
	}

	@Override
	protected Class<TeachPlan> getEntityClass() {
		return TeachPlan.class;
	}

	@Override
	public void deleteByAcadyearAndSemesterAndClassIds(String unitId, String acadyear, int semester,String[] classIds) {
		teachPlanDao.deleteByUnitIdAndAcadyearAndSemesterAndClassIdIn(unitId,acadyear,semester,classIds);
		
	}

	@Override
	public List<TeachPlan> findTeachPlanListByClassIds(String acadyear, int semester, String[] classIds) {
		List<TeachPlan> teacherPlanList = new ArrayList<TeachPlan>();
		if(classIds!=null && classIds.length>0){
			Specification<TeachPlan> s = new Specification<TeachPlan>() {
				@Override
				public Predicate toPredicate(Root<TeachPlan> root,
						CriteriaQuery<?> cq, CriteriaBuilder cb) {
					 List<Predicate> ps = new ArrayList<Predicate>();
					 queryIn("classId", classIds, root, ps, cb);
					 ps.add(cb.equal(root.get("acadyear").as(String.class), acadyear));
					 ps.add(cb.equal(root.get("semester").as(String.class), semester));
					 cq.where(ps.toArray(new Predicate[0]));
					 return cq.getRestriction();
				}
	        };
	        teacherPlanList = teachPlanDao.findAll(s);
    	}
        return teacherPlanList;
	}

	@Override
	public List<TeachPlan> findTeachPlanListByClassIdsAndSubjectIds(String acadyear, int semester, String[] classIds, String[] subjectIds) {
		return teachPlanDao.findByAcadyearAndSemesterAndClassIdInAndSubjectIdIn(acadyear,semester,classIds,subjectIds);
	}
	
}
