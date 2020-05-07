package net.zdsoft.stuwork.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyBusinessOption;

public interface DyBusinessOptionDao extends BaseJpaRepositoryDao<DyBusinessOption, String>{
    @Query("From DyBusinessOption where unitId = ?1 and businessType = ?2 order by orderId")
	public List<DyBusinessOption> findListByUnitIdAndType(String unitId, String businessType);
    
    public List<DyBusinessOption> findListByUnitId(String unitId);
    @Query("From DyBusinessOption where unitId = ?1 and businessType = ?2 and optionName = ?3")
    public List<DyBusinessOption> findListByUidBtypeAndOname(String unitId, String businessType,String optionName);
    @Modifying
    @Query("Delete From DyBusinessOption where unitId = ?1 and businessType = ?2")
    public List<DyBusinessOption> deleteByUIdBType(String unitId, String businessType);
    @Modifying
    @Query("delete from DyBusinessOption where unitId = ?1 and businessType = ?2")
    public void deleteByUnitIdAndType(String unitId, String businessType);
    @Query("From DyBusinessOption where unitId = ?1 and businessType = ?2 and orderId = ?3")
    public List<DyBusinessOption> findListByUidBtypeAndOrderId(String unitId, String businessType,int orderId);
}
