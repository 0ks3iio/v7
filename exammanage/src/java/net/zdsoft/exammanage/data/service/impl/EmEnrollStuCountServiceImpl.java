package net.zdsoft.exammanage.data.service.impl;

import com.google.common.collect.Lists;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmEnrollStuCountDao;
import net.zdsoft.exammanage.data.entity.EmEnrollStuCount;
import net.zdsoft.exammanage.data.service.EmEnrollStuCountService;
import net.zdsoft.exammanage.data.service.EmEnrollStudentService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service("emEnrollStuCountService")
public class EmEnrollStuCountServiceImpl extends BaseServiceImpl<EmEnrollStuCount, String> implements EmEnrollStuCountService {

    @Autowired
    private EmEnrollStuCountDao emEnrollStuCountDao;
    @Autowired
    private EmEnrollStudentService emEnrollStudentService;

    @Override
    protected BaseJpaRepositoryDao<EmEnrollStuCount, String> getJpaDao() {
        return emEnrollStuCountDao;
    }

    @Override
    protected Class<EmEnrollStuCount> getEntityClass() {
        return EmEnrollStuCount.class;
    }

    @Override
    public List<EmEnrollStuCount> findByExamId(String examId, Pagination page) {
        Specification<EmEnrollStuCount> specification = new Specification<EmEnrollStuCount>() {
            @Override
            public Predicate toPredicate(Root<EmEnrollStuCount> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("examId").as(String.class), examId));
                List<Order> orderList = new ArrayList<Order>();
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
        List<EmEnrollStuCount> emEnrollStuCounts = Lists.newArrayList();
        if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<EmEnrollStuCount> findAll = emEnrollStuCountDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            emEnrollStuCounts = findAll.getContent();
        } else {
            emEnrollStuCounts = emEnrollStuCountDao.findAll(specification);
        }
        return emEnrollStuCounts;
    }

    @Override
    public List<EmEnrollStuCount> findByExamIdAndSchoolIdIn(String examId, String[] schoolIds) {
        return emEnrollStuCountDao.findByExamIdAndSchoolIdIn(examId, schoolIds);
    }

}
