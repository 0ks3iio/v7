package net.zdsoft.partybuild7.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.partybuild7.data.entity.OrgTrend;

import org.springframework.data.jpa.repository.Query;

public interface OrgTrendDao extends BaseJpaRepositoryDao<OrgTrend, String> {

    /**
     * 根据组织id查询所有动态
     * @param id
     * @return
     */
    @Query(nativeQuery = true,value="select * from pb_organization_trends where org_id = ?1 order by creation_time desc ")
    public List<OrgTrend> getAllByOrgId(String id);

    /**
     * 根据id 查询一条动态
     * @param id
     * @return
     */
    @Query(nativeQuery = true , value="select * from pb_organization_trends where id = ?1")
    public OrgTrend   getOrgTrendById(String id);

    /**
     * 取该组织下最新一条动态
     * @param orgId
     * @return
     */
    @Query(nativeQuery = true,value="select * from pb_organization_trends where org_id = ?1 and rownum<=1  order by creation_time desc ")
    public OrgTrend getOrgTrendByOrgId(String orgId);
}
