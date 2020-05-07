package net.zdsoft.diathesis.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.diathesis.data.entity.DiathesisEvaluate;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/5/15 10:33
 */
public interface DiathesisEvaluateService extends BaseService<DiathesisEvaluate,String> {

    /**
     * 如果id为空  则新增  否则更新
     * @param evaluate
     */
    void saveEvaluate(DiathesisEvaluate evaluate);

    List<DiathesisEvaluate> findByUnitIdAndStudentId(String unitId,String studentId);
}
