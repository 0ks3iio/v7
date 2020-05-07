package net.zdsoft.tutor.data.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder.In;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.tutor.data.dao.TutorRoundTeacherDao;
import net.zdsoft.tutor.data.entity.TutorRoundTeacher;
import net.zdsoft.tutor.data.service.TutorRoundTeacherService;

/**
 * @author yangsj  2017年9月11日下午8:32:32
 */
@Service
public class TutorRoundTeacherServiceImpl extends BaseServiceImpl<TutorRoundTeacher, String> implements TutorRoundTeacherService {
    
	@Autowired
	private TutorRoundTeacherDao tutorRoundTeacherDao;
	
	@Override
	protected BaseJpaRepositoryDao<TutorRoundTeacher, String> getJpaDao() {
		// TODO Auto-generated method stub
		return tutorRoundTeacherDao;
	}

	@Override
	protected Class<TutorRoundTeacher> getEntityClass() {
		// TODO Auto-generated method stub
		return TutorRoundTeacher.class;
	}

	@Override
	public List<TutorRoundTeacher> findByUnitId(String unitId) {
		// TODO Auto-generated method stub
		return tutorRoundTeacherDao.findByUnitId(unitId);
	}

	@Override
	public void deleteByRoundId(String tutorRoundId) {
		// TODO Auto-generated method stub
		tutorRoundTeacherDao.deleteByRoundId(tutorRoundId);
	}

	@Override
	public List<TutorRoundTeacher> findByRoundId(String tutorRoundId) {
		// TODO Auto-generated method stub
		return tutorRoundTeacherDao.findByRoundId(tutorRoundId);
	}

	@Override
	public TutorRoundTeacher findByRoundAndTeaId(String tutorRoundId, String teacherId) {
		// TODO Auto-generated method stub
		return tutorRoundTeacherDao.findByRoundAndTeaId(tutorRoundId,teacherId);
	}

	@Override
	public void deleteByRoundAndTeaId(String tutorRoundId, String teacherId) {
		// TODO Auto-generated method stub
		tutorRoundTeacherDao.deleteByRoundAndTeaId(tutorRoundId,teacherId);
	}

	@Override
	public List<TutorRoundTeacher> findByRidAndTeaIdIn(final String tutorId,final String unitId,final Pagination pagination,
			final	String... teaids) {
        return findAll(new Specification<TutorRoundTeacher>() {
        	@Override
            public Predicate toPredicate(Root<TutorRoundTeacher> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
            	 List<Predicate> ps = new ArrayList<Predicate>();
            	 ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
            	 if(StringUtils.isNotBlank(tutorId)){
            		 ps.add(cb.equal(root.get("roundId").as(String.class), tutorId));
            	 }
            	 if(teaids!=null&&teaids.length>0){
            		In<String> in = cb.in(root.get("teacherId").as(String.class));
 					for (int i = 0; i < teaids.length; i++) {
 						in.value(teaids[i]);
 					}
 					ps.add(in);
            	 }
            	 cq.where(ps.toArray(new Predicate[0]));
            	return cq.getRestriction();
            }
		}, pagination);
	}

}
