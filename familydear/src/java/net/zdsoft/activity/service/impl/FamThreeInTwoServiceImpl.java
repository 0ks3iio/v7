package net.zdsoft.activity.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.activity.dao.FamThreeInTwoDao;
import net.zdsoft.activity.entity.FamDearThreeInTwo;
import net.zdsoft.activity.service.FamThreeInTwoService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service("FamThreeInTwoService")
public class FamThreeInTwoServiceImpl extends BaseServiceImpl<FamDearThreeInTwo,String> implements FamThreeInTwoService {
	@Autowired
	private FamThreeInTwoDao famThreeInTwoDao;

	@SuppressWarnings("serial")
	@Override
	public List<FamDearThreeInTwo> getListByUnitIdAndYearAndTitleAndState(
			String unitId, String year, String title, String state,
			Pagination page) {
		List<FamDearThreeInTwo> list=new ArrayList<FamDearThreeInTwo>();
        Specification<FamDearThreeInTwo> specification = new Specification<FamDearThreeInTwo>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<FamDearThreeInTwo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> ps = Lists.newArrayList();
                ps.add(criteriaBuilder.equal(root.get("unitId").as(String.class),unitId));
                ps.add(criteriaBuilder.equal(root.get("year").as(String.class),year));
                ps.add(criteriaBuilder.equal(root.get("isDelete").as(String.class),"0"));
                if(StringUtils.isNotBlank(title)){
                    ps.add(criteriaBuilder.like(root.get("title").as(String.class),"%"+title+"%"));
                }
                if(StringUtils.isNotBlank(state)){
                    ps.add(criteriaBuilder.equal(root.get("state").as(String.class),state));
                }
                List<Order> orderList = new ArrayList<>();
                orderList.add(criteriaBuilder.asc(root.get("createTime").as(String.class)));
                criteriaQuery.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return criteriaQuery.getRestriction();
            }
        };
        if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<FamDearThreeInTwo> findAll = famThreeInTwoDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            list=findAll.getContent();
        }
        else {
            list=famThreeInTwoDao.findAll(specification);
        }
        return list;
	}

	@Override
	protected BaseJpaRepositoryDao<FamDearThreeInTwo, String> getJpaDao() {
		return famThreeInTwoDao;
	}

	@Override
	protected Class<FamDearThreeInTwo> getEntityClass() {
		return FamDearThreeInTwo.class;
	}

}
