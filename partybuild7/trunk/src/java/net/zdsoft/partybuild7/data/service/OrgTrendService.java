package net.zdsoft.partybuild7.data.service;

import java.util.List;

import net.zdsoft.partybuild7.data.entity.OrgTrend;

public interface OrgTrendService {
    /**
     * 通过组织id 获取 组织动态
     * @param id
     * @return
     */
    public List<OrgTrend> getAllByOrgId(String id);

    /**
     * 通过id 获取 一条组织信息
     * @param id
     * @return
     */
    public OrgTrend getOrgTrendById(String id);

    /**
     * 获取最新一条组织动态
     * @param orgId
     * @return
     */
    public OrgTrend getOrgTrendByOrgId(String orgId);
    /**
     * 获取组织动态 通过teacherId

     */
    public OrgTrend getOrgTrendByTeacherId(String teacherId);
}
