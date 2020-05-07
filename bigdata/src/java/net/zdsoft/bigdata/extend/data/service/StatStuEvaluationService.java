package net.zdsoft.bigdata.extend.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.extend.data.entity.StatStuEvaluation;

import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:41.
 */
public interface StatStuEvaluationService extends BaseService<StatStuEvaluation, String> {

    List<StatStuEvaluation> findByStudentId(String studentId);
}
