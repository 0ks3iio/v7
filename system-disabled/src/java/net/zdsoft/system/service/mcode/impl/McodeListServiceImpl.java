package net.zdsoft.system.service.mcode.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.system.dao.mcode.McodeListDao;
import net.zdsoft.system.entity.mcode.McodeList;
import net.zdsoft.system.enums.mcode.MaintainEnum;
import net.zdsoft.system.service.mcode.McodeListService;

@Service("mcodeListService")
public class McodeListServiceImpl extends BaseServiceImpl<McodeList, String> implements McodeListService {

    @Autowired
    private McodeListDao mcodeListDao;

    @Override
    protected BaseJpaRepositoryDao<McodeList, String> getJpaDao() {
        return mcodeListDao;
    }

    @Override
    protected Class<McodeList> getEntityClass() {
        return McodeList.class;
    }

    @Override
    public List<McodeList> findByMcodeIdOrMaintain(int subsystem, int maintain) {
        if (Validators.isEmpty(MaintainEnum.getName(maintain))) {
            // 不根据maintain查询
            return mcodeListDao.findBySubsystem(subsystem);
        }
        else {
            return mcodeListDao.findBySubsystemAndMaintain(subsystem, maintain);
        }
    }

    @Override
    public List<McodeList> findByName(final String mcodeName, Pagination page) {
        Pageable pageable = Pagination.toPageable(page);
        Specification<McodeList> specification = new Specification<McodeList>() {
            @Override
            public Predicate toPredicate(Root<McodeList> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.like(root.get("mcodeName").as(String.class), "%" + mcodeName + "%"));
                ps.add(cb.equal(root.get("isUsing"), 1));
                cq.where(ps.toArray(new Predicate[0]));
                return cq.getRestriction();
            }
        };
        Page<McodeList> findAll = mcodeListDao.findAll(specification, pageable);
        page.setMaxRowCount((int) findAll.getTotalElements());
        return findAll.getContent();
    }
}
