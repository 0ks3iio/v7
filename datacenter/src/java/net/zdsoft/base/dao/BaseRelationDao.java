package net.zdsoft.base.dao;

import java.util.List;

import net.zdsoft.base.entity.base.BaseRelation;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BaseRelationDao extends BaseJpaRepositoryDao<BaseRelation, String> {
	public static final String SQL_NOT_DELETED = " and isDeleted = 0";
	@Query("Select dcId From BaseRelation where businessId = ?1 and ticketKey = ?2 and sourceAp = ?3 and model = ?4"+SQL_NOT_DELETED)
    public String findByRelationParm(String businessId,String ticketKey,String sourceAp,String model);
	
	@Query("From BaseRelation where businessId in (?1) and ticketKey = ?2 and sourceAp = ?3 and model = ?4 and unitClass = ?5"+SQL_NOT_DELETED)
	public List<BaseRelation> findListByRelationParm(String[] businessIds,String ticketKey,String sourceAp,String model,int unitClass);
	@Modifying
	@Query("update BaseRelation set isDeleted = 1 where dcId in (?1)")
	public void deleteByDcIds(String[] dcIds);
}
