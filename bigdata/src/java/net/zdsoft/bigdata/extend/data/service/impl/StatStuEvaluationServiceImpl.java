package net.zdsoft.bigdata.extend.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.StatStuEvaluationDao;
import net.zdsoft.bigdata.extend.data.entity.StatStuEvaluation;
import net.zdsoft.bigdata.extend.data.service.StatStuEvaluationService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class StatStuEvaluationServiceImpl extends BaseServiceImpl<StatStuEvaluation, String> implements StatStuEvaluationService {

    @Resource
    private StatStuEvaluationDao statStuEvaluationDao;

    @Override
    protected BaseJpaRepositoryDao<StatStuEvaluation, String> getJpaDao() {
        return statStuEvaluationDao;
    }

    @Override
    protected Class<StatStuEvaluation> getEntityClass() {
        return StatStuEvaluation.class;
    }

    @Override
    public List<StatStuEvaluation> findByStudentId(String studentId) {
        return statStuEvaluationDao.findAllByStudentIdOrderByOrderIdAsc(studentId);
    }
}
