package net.zdsoft.familydear.service.impl;

import com.google.common.collect.Lists;
import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;

import net.zdsoft.familydear.dao.FamDearPlanDao;
import net.zdsoft.familydear.entity.FamDearPlan;
import net.zdsoft.familydear.service.FamDearPlanService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.familydear.service.impl
 * @ClassName: FamDearPlanServiceImpl
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/6 11:17
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/6 11:17
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service("famDearPlanService")
public class FamDearPlanServiceImpl extends BaseServiceImpl<FamDearPlan, String> implements FamDearPlanService {
    @Autowired
    private FamDearPlanDao famDearPlanDao;
    @Override
    protected BaseJpaRepositoryDao<FamDearPlan, String> getJpaDao() {
        return famDearPlanDao;
    }

    @Override
	public List<FamDearPlan> getFamilyDearPlanList(String unitId, String year) {
		return famDearPlanDao.getFamilyDearPlanList(unitId, year);
	}

	@Override
    protected Class<FamDearPlan> getEntityClass() {
        return FamDearPlan.class;
    }

    @Override
    public List<FamDearPlan> findListByYearAndTitleByPage(String year,String title,String state,Pagination page) {
        List<FamDearPlan> list=new ArrayList<FamDearPlan>();
        Specification<FamDearPlan> specification = new Specification<FamDearPlan>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<FamDearPlan> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> ps = Lists.newArrayList();
                ps.add(criteriaBuilder.equal(root.get("year").as(String.class),year));
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
            Page<FamDearPlan> findAll = famDearPlanDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            list=findAll.getContent();
        }
        else {
            list=famDearPlanDao.findAll(specification);
        }
        return list;
    }
}
