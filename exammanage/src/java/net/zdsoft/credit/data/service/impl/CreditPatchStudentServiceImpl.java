package net.zdsoft.credit.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.credit.data.dao.CreditPatchStudentDao;
import net.zdsoft.credit.data.entity.CreditPatchStudent;
import net.zdsoft.credit.data.service.CreditPatchStudentService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service("creditPatchStudentService")
public class CreditPatchStudentServiceImpl extends BaseServiceImpl<CreditPatchStudent, String> implements CreditPatchStudentService {
    @Autowired
    private CreditPatchStudentDao creditPatchStudentDao;
    
	@Override
	protected BaseJpaRepositoryDao<CreditPatchStudent, String> getJpaDao() {
		return creditPatchStudentDao;
	}

	@Override
	protected Class<CreditPatchStudent> getEntityClass() {
		return CreditPatchStudent.class;
	}

	@Override
	public List<CreditPatchStudent> findListByParam(String year,String semester,String studentId,String gradeId,String classId,String subjectId) {
		List<CreditPatchStudent> list=new ArrayList<CreditPatchStudent>();
		Specification<CreditPatchStudent> specification = new Specification<CreditPatchStudent>() {
			@Nullable
			@Override
			public Predicate toPredicate(Root<CreditPatchStudent> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {


				List<Predicate> ps = new ArrayList<Predicate>();

				if(StringUtils.isNotBlank(year)){
					ps.add(criteriaBuilder.equal(root.get("acadyear").as(String.class),year));
				}
				if(StringUtils.isNotBlank(semester)){
					ps.add(criteriaBuilder.equal(root.get("semester").as(String.class),semester));
				}
				if(StringUtils.isNotBlank(studentId)){
					ps.add(criteriaBuilder.equal(root.get("studentId").as(String.class),studentId));
				}
				if(StringUtils.isNotBlank(gradeId)){
					ps.add(criteriaBuilder.equal(root.get("gradeId").as(String.class),gradeId));
				}
				if(StringUtils.isNotBlank(classId)){
					ps.add(criteriaBuilder.equal(root.get("classId").as(String.class),classId));
				}
				if(StringUtils.isNotBlank(subjectId)){
					ps.add(criteriaBuilder.equal(root.get("subjectId").as(String.class),subjectId));
				}
				criteriaQuery.where(ps.toArray(new Predicate[0]));
				return criteriaQuery.getRestriction();
			}
		};
		list=creditPatchStudentDao.findAll(specification);
		return list;
	}
}
