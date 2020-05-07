package net.zdsoft.diathesis.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.diathesis.data.entity.DiathesisProjectEx;
import net.zdsoft.diathesis.data.vo.DiathesisChildProjectVo;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/6/13 17:52
 */
public interface DiathesisProjectExService extends BaseService<DiathesisProjectEx,String> {

    /**
     * 如果没有设置过, 会返回该单位的标准数据
     * @param unitId
     * @param projectId
     * @return
     */
    DiathesisProjectEx findByUnitIdAndProjectId(String unitId, String projectId);

    /**
     * 如果没有设置过, 会返回该单位的标准数据
     * @param unitId
     * @return
     */
    List<DiathesisProjectEx> findByUnitIdAndProjectIdIn(String unitId, List<String> projectIds);

    boolean existInputTypesByUnitIdAndRoleCode(String unitId, String roleCode);

    void deleteByProjectIdsAndUnitId(String[] projectIds,String unitId);

    //保存评价人
    void saveChildProjectEx(DiathesisChildProjectVo childProjectList);


    void deleteByProjectIds(String[] projectIds);

    void deleteByIds(List<String> delExIds);
}
