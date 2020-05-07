package net.zdsoft.diathesis.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.diathesis.data.dto.DiathesisGlobalSettingDto;
import net.zdsoft.diathesis.data.dto.DiathesisRecordSettingDto;
import net.zdsoft.diathesis.data.entity.DiathesisSet;

/**
 * @Author: panlf
 * @Date: 2019/3/29 9:41
 */
public interface DiathesisSetService extends BaseService<DiathesisSet, String> {
    /**
     * 返回单位的全局设置信息
     * @param unitId
     * @return
     */
    DiathesisGlobalSettingDto findGlobalByUnitId(String unitId);

//    /**
//     * 新增全局设置,如果有id,则为更新
//     * @param diathesisGlobalSettingDto
//     */
//    void saveDiathesisSet(DiathesisGlobalSettingDto diathesisGlobalSettingDto,String realName);

    /**
     * 返回写实记录的设置信息
     * @param projectId
     * @param realName
     * @return
     */
    DiathesisRecordSettingDto findRecordSettingByProjectId(String projectId, String unitId, String realName);

    /**
     * 保存或更新 单个 写实记录项目的设置
     * @param diathesisRecordSettingDto
     */
    void saveRecordSetting(DiathesisRecordSettingDto diathesisRecordSettingDto,String realName);

    DiathesisSet findByUnitId(String unitId);

    /**
     * 删除 newdiathesis_set 表 auditorTypes 和 inputTypes 字段中 存在的角色 code
     * @param unitId
     */
    void deleteRoleByUnitIdAndRoleCode(String unitId,String roleCode);

    //boolean exitByAndUnitIdRoleCode(String unitId, String roleCode);

    void setChildUnit(DiathesisGlobalSettingDto dto, String realName,String regionCode);

    //保存有权限的学校设置
    void saveSchoolSet(DiathesisGlobalSettingDto dto);
    //保存没有设置权限的学校设置
    void saveSchoolNoAuthorSet(DiathesisGlobalSettingDto dto);

    //查找单位的 设置信息 如果不存在 就初始化
    DiathesisSet findSetIfNullCreate(String unitId,String operator);

    void saveEduSet(DiathesisGlobalSettingDto dto);

    /**
     *  查找单位实际使用的 设置信息
     *  规则: 本单位如果没有设置 查找上级单位,如果查找到顶级单位都没有, 那么就返回 unitId =32个0  的设置信息
     * @param unitId
     * @return
     */
    DiathesisGlobalSettingDto findUsingSetByUnitId(String unitId);

    /**
     * 没有权限的单位保存写实记录设置
     *  只能保存  1. 审核人   2. 录入人    3.是否显示在行
     * @param dto
     * @param realName
     */
    void saveUnAuthorRecordSet(DiathesisRecordSettingDto dto, String realName);

    void deleteRole(String unitId, String roleId, String roleCode);
}
