package net.zdsoft.diathesis.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.diathesis.data.dto.DiathesisProjectDto;
import net.zdsoft.diathesis.data.entity.DiathesisRecordSet;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/8/8 17:50
 */
public interface DiathesisRecordSetService  extends BaseService<DiathesisRecordSet, String>{



    void saveRecordSet(List<DiathesisProjectDto> projectlist, String unitId, String realName);


    void saveOne(DiathesisRecordSet recordSet);

    List<DiathesisRecordSet> findByUnitId(String unitId);

    void saveAllEntity(List<DiathesisRecordSet> recordSetList);

    void deleteByIds(String[] projectIds);
}
