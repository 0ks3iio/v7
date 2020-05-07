package net.zdsoft.szxy.base.api;

import net.zdsoft.szxy.base.RecordName;
import net.zdsoft.szxy.base.entity.School;
import net.zdsoft.szxy.monitor.Rpc;

/**
 * 基础数据： 学校
 * @author shenke
 * @since 2019/3/19 上午11:28
 */
@Rpc(domain = RecordName.name)
public interface SchoolRemoteService {

    /**
     * 根据学校ID获取学校信息,不包含软删的数据
     * @param id 学校ID
     * @return School or null
     *
     */
    School getSchoolById(String id);

    /**
     * 根据字段名更新学校数据，school必须包含主键数据
     * 当schoolName或parentId或RegionCode发生变化时这个接口会同步更新Unit的数据
     * @param school 学校数据
     * @param properties 要更新的字段名列表
     * @param parentUnitId 可选参数，当上级单位发生变化时必须传该参数
     */
    void updateSchool(School school, String[] properties, String parentUnitId);
}