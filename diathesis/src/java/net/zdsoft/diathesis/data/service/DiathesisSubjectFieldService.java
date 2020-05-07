package net.zdsoft.diathesis.data.service;

import net.zdsoft.diathesis.data.dto.DiathesisFieldDto;
import net.zdsoft.diathesis.data.entity.DiathesisSubjectField;

import java.util.List;
import java.util.Map;

/**
 * @Author: panlf
 * @Date: 2019/8/13 10:08
 */
public interface DiathesisSubjectFieldService {

    void saveAllEntity(List<DiathesisSubjectField> fieldList);

    List<DiathesisSubjectField> findByUnitAndSubjectType(String unitId,String subjectType);

//    void deleteByUnitId(String unitId);

    List<DiathesisSubjectField> findByUnitId(String unitId);

    Map<String, List<DiathesisFieldDto>> findMapByUnitId(String unitId);

    void deleteByIds(List<String> delFieldList);


    List<DiathesisSubjectField> findUsingField(String unitId);

    DiathesisSubjectField findByUnitIdAndFieldCode(String unitId, String fieldCode);

    /**
     * 获得改单位实际正在使用的字段
     * @param unitId
     * @param type
     * @return
     */
    List<DiathesisSubjectField> findUsingFieldByUnitIdAndSubjectType(String unitId, String type);
}
