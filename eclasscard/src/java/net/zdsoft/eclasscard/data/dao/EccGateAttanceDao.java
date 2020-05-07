package net.zdsoft.eclasscard.data.dao;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;

import net.zdsoft.eclasscard.data.entity.EccGateAttance;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccGateAttanceDao extends BaseJpaRepositoryDao<EccGateAttance, String> {
	
	@Query("FROM EccGateAttance WHERE status = ?1 and studentId in (?2)")
	public List<EccGateAttance> getListByCon(int status,String[] studentIds);
	@Query("FROM EccGateAttance WHERE  studentId in (?1)")
	public List<EccGateAttance> getListByCon(String[] studentIds);
	@Modifying
	@Query("update EccGateAttance set status=?1 where id in (?2)")
	public void updateStatus(int status,String[] ids);
	@Query("From EccGateAttance Where studentId = ?1")
	public EccGateAttance findByStuId(String studentId);

}
