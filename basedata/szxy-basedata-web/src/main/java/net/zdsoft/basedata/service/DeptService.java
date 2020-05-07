package net.zdsoft.basedata.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.framework.entity.Pagination;

/**
 * @author admin
 */
public interface DeptService extends BaseService<Dept, String> {

    /**
     * 得到一个单位的所有部门
     *
     * @param unitId
     * @return
     */
    List<Dept> findByUnitId(String unitId);

    /**
     * 根据父部门ID得到下属部门列表，根据排序号排序
     *
     * @param parentId
     * @return
     */
    List<Dept> findByParentId(String parentId);

    Integer countByUnitId(String unitId);

    List<Dept> findByUnitId(String unitId, Pagination page);

    /**
     * 根据父部门ID得到下属部门列表，根据排序号排序
     *
     * @param parentId
     * @param page
     * @return
     */
    List<Dept> findByParentId(String parentId, Pagination page);

    Map<String, List<Dept>> findByUnitIdMap(String[] unitIds);

    /**
     * 得到该单位该父部门的下级部门列表，根据排序号排列。
     *
     * @param unitId
     * @param parentId
     * @return
     */
    List<Dept> findByUnitIdAndParentId(String unitId, String parentId);

    List<Dept> findByParentIdAndDeptName(String parentId, String deptName);

    /**
     * 分管校长
     *
     * @param unitId
     * @param deputyHeadId
     * @return
     */
    List<Dept> findByUnitIdAndDeputyHeadId(String unitId,
                                           String deputyHeadId);

    /**
     * 取校区下面的所有部门
     *
     * @param areaId
     * @return
     */
    List<Dept> findByAreaId(String areaId);

    /**
     * 根据教师id判读是否这个部门下成员，如果是返回教研组id
     *
     * @param userId
     * @return
     */
    List<Dept> findByTeacherId(String userId);

    /**
     * 根据分管领导id获取部门负责人信息
     *
     * @param unitId
     * @param leaderId
     * @return
     */
    List<Dept> findByUnitIdAndLeaderId(String unitId, String leaderId);

    Dept findByUnitAndCode(String unitId, String code);

    /**
     * 取院系的直属部门
     *
     * @param instituteId
     * @return
     */
    List<Dept> findByInstituteId(String instituteId);

    List<Dept> findByUnitIdAndDeptNameLike(String unitId, String deptName);

    List<Dept> saveAllEntitys(Dept... dept);

    /**
     * 硬删
     *
     * @param id
     */
    void deleteAllByIds(String... id);

    /**
     * 根据单位和更新时间获取部门
     *
     * @param
     * @return
     */

    List<Dept> findByUnitIdAndModifyTime(String unitId, Date modifyTime);

    /**
     * 根据单位获取部门
     *
     * @param
     * @return
     */
    List<Dept> findAllByUnitId(String unitId);

    /**
     * 更新用户钉钉号
     */
    void updateDingDingIdById(String dingdingId, String id);

    /**
     * 获取单位部门可用代码
     * max + 1
     * @param unitId 单位ID
     * @return code
     */
    String getAvailableDeptCodeByUnitId(String unitId);

    void deleteDeptsByUnitId(String unitId);
}
