package net.zdsoft.tutor.data.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.tutor.data.dao.TutorRecordDetailedDao;
import net.zdsoft.tutor.data.entity.TutorRecordDetailed;
import net.zdsoft.tutor.data.service.TutorRecordDetailedService;

/**
 * @author yangsj  2017年11月20日上午10:15:17
 */
@Service
public class TutorRecordDetailedServiceImpl extends BaseServiceImpl<TutorRecordDetailed, String> implements TutorRecordDetailedService{
    
	@Autowired
	private TutorRecordDetailedDao tutorRecordDetailedDao;
	
	@Override
	protected BaseJpaRepositoryDao<TutorRecordDetailed, String> getJpaDao() {
		return tutorRecordDetailedDao;
	}

	@Override
	protected Class<TutorRecordDetailed> getEntityClass() {
		return TutorRecordDetailed.class;
	}
	
	@Override
	public List<TutorRecordDetailed> findBySIdAndSemester(final String unitId, final String acadyear, final String semester,
			final String recordType,final Pagination pagination,final String... teacherId) {
		
		return findAll(new Specification<TutorRecordDetailed>() {
			@Override
            public Predicate toPredicate(Root<TutorRecordDetailed> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
            	 List<Predicate> ps = new ArrayList<Predicate>();
            	 if(teacherId != null){
						ps.add(root.<String>get("teacherId").in(teacherId));
				 }
            	 ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
            	 ps.add(cb.equal(root.get("acadyear").as(String.class), acadyear));
            	 ps.add(cb.equal(root.get("semester").as(String.class), semester));
            	 if(StringUtils.isNotBlank(recordType)){
            		 ps.add(cb.equal(root.get("recordType").as(String.class), recordType));
            	 }
            	 cq.where(ps.toArray(new Predicate[0]));
            	return cq.getRestriction();
            }
		}, pagination);
	}

	@Override
	public List<TutorRecordDetailed> findByTIdsAndSemester(String[] teacherIds, String acadyear, String semester) {
		// TODO Auto-generated method stub
		return tutorRecordDetailedDao.findByTIdsAndSemester(teacherIds,acadyear,semester);
	}


}
