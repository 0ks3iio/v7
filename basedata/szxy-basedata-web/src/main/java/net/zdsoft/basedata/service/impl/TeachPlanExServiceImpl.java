package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.dao.TeachPlanExDao;
import net.zdsoft.basedata.entity.TeachPlanEx;
import net.zdsoft.basedata.service.TeachPlanExService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service("teachPlanExService")
public class TeachPlanExServiceImpl extends BaseServiceImpl<TeachPlanEx, String> implements TeachPlanExService {

	@Autowired
	private TeachPlanExDao teachPlanExDao;
	
	@Override
	protected BaseJpaRepositoryDao<TeachPlanEx, String> getJpaDao() {
		return teachPlanExDao;
	}

	@Override
	protected Class<TeachPlanEx> getEntityClass() {
		return TeachPlanEx.class;
	}

	@Override
	public void deleteByTeacherIdAndPrimaryTableIdIn(String teacherId, String[] primaryTableIds) {
		if(primaryTableIds==null || primaryTableIds.length<1) {
			return;
		}
		if (primaryTableIds.length <= 1000) {
			if(StringUtils.isBlank(teacherId)) {
				teachPlanExDao.deleteByPrimaryTableIdIn(primaryTableIds);
			}else {
				teachPlanExDao.deleteByTeacherIdAndPrimaryTableIdIn(teacherId,primaryTableIds);
			}
		} else {
			int cyc = primaryTableIds.length / 1000 + (primaryTableIds.length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > primaryTableIds.length)
					max = primaryTableIds.length;
				
				if(StringUtils.isBlank(teacherId)) {
					teachPlanExDao.deleteByPrimaryTableIdIn(ArrayUtils.subarray(primaryTableIds, i * 1000, max));
				}else {
					teachPlanExDao.deleteByTeacherIdAndPrimaryTableIdIn(teacherId,ArrayUtils.subarray(primaryTableIds, i * 1000, max));
				}
			}
	
		}
	}

	@Override
	public List<TeachPlanEx> findByPrimaryTableIdIn(String[] primaryTableIds) {
		List<TeachPlanEx> teacherPlanExList = new ArrayList<TeachPlanEx>();
		if(primaryTableIds!=null && primaryTableIds.length>0){
			Specification<TeachPlanEx> s = new Specification<TeachPlanEx>() {
				@Override
				public Predicate toPredicate(Root<TeachPlanEx> root,
						CriteriaQuery<?> cq, CriteriaBuilder cb) {
					 List<Predicate> ps = new ArrayList<Predicate>();
					 queryIn("primaryTableId", primaryTableIds, root, ps, cb);
					 cq.where(ps.toArray(new Predicate[0]));
					 return cq.getRestriction();
				}
	        };
	        teacherPlanExList = teachPlanExDao.findAll(s);
    	}
        return teacherPlanExList;
	}

	@Override
	public void deleteByTeacherIdInAndPrimaryTableIdIn(String[] teacherIds, String[] primaryTableIds) {
		if(ArrayUtils.isEmpty(teacherIds) || ArrayUtils.isEmpty(primaryTableIds)) {
			return;
		}
		int cyc = primaryTableIds.length / 1000 + (primaryTableIds.length % 1000 == 0 ? 0 : 1);
		for (int i = 0; i < cyc; i++) {
			int max = (i + 1) * 1000;
			if (max > primaryTableIds.length){
				max = primaryTableIds.length;
				teachPlanExDao.deleteByTeacherIdInAndPrimaryTableIdIn(teacherIds,ArrayUtils.subarray(primaryTableIds, i * 1000, max));
			}
		}
	
	}

}
