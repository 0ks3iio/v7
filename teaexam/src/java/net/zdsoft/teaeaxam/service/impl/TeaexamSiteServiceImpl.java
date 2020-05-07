package net.zdsoft.teaeaxam.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.teaeaxam.dao.TeaexamSiteDao;
import net.zdsoft.teaeaxam.entity.TeaexamSite;
import net.zdsoft.teaeaxam.service.TeaexamSiteService;
@Service("teaexamSiteService")
public class TeaexamSiteServiceImpl extends BaseServiceImpl<TeaexamSite,String> implements TeaexamSiteService{
	@Autowired
	private TeaexamSiteDao teaexamSiteDao;

	@Override
	protected BaseJpaRepositoryDao<TeaexamSite, String> getJpaDao() {
		return teaexamSiteDao;
	}

	@Override
	protected Class<TeaexamSite> getEntityClass() {
		return TeaexamSite.class;
	}

	@Override
	public List<TeaexamSite> findByUnionCode(String unionCode, Pagination page) {
		Specification<TeaexamSite> specification=new Specification<TeaexamSite>() {
			@Override
			public Predicate toPredicate(Root<TeaexamSite> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb.like(root.<String>get("unionCode"), unionCode+"%"));
                cq.where(ps.toArray(new Predicate[0]));
	            return cq.getRestriction();
			}
		};
		List<TeaexamSite> list=new ArrayList<>();
		
		if(page!=null){
			list=findAll(specification, page);
		}else{
			list=findAll(specification);
		}
		return list;
	}
	
	public List<TeaexamSite> findBySchoolIds(String[] schoolIds){
		if(ArrayUtils.isEmpty(schoolIds)) {
			return new ArrayList<>();
		}
		return teaexamSiteDao.findBySchoolIds(schoolIds);
	}
}
