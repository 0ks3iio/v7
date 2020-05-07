package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.ExammanageEnrollmentDao;
import net.zdsoft.exammanage.data.entity.ExammanageEnrollment;
import net.zdsoft.exammanage.data.service.ExammanageEnrollmentService;
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

@Service("exammanageEnrollmentService")
public class ExammanageEnrollmentServiceImpl extends BaseServiceImpl<ExammanageEnrollment, String> implements ExammanageEnrollmentService {
    @Autowired
    private ExammanageEnrollmentDao exammanageEnrollmentDao;

    @Override
    protected BaseJpaRepositoryDao<ExammanageEnrollment, String> getJpaDao() {
        return exammanageEnrollmentDao;
    }

    @Override
    protected Class<ExammanageEnrollment> getEntityClass() {
        return ExammanageEnrollment.class;
    }

    @Override
    public List<ExammanageEnrollment> findListByParams(String acadyear, String semester, String examId, String unitId) {
        Specification<ExammanageEnrollment> s = new Specification<ExammanageEnrollment>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<ExammanageEnrollment> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<>();
                if (StringUtils.isNotBlank(acadyear)) {
                    ps.add(cb.equal(root.get("acadyear").as(String.class), acadyear));
                }
                if (StringUtils.isNotBlank(semester)) {
                    ps.add(cb.equal(root.get("semester").as(String.class), semester));
                }
                if (StringUtils.isNotBlank(examId)) {
                    ps.add(cb.equal(root.get("examId").as(String.class), examId));
                }
                if (StringUtils.isNotBlank(unitId)) {
                    ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
                }
                cq.where(ps.toArray(new Predicate[0]));
                return cq.getRestriction();
            }
        };
        return exammanageEnrollmentDao.findAll(s);
    }
}
