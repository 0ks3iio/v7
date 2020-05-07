package net.zdsoft.system.dao.config;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.config.UnitIni;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UnitIniDao extends BaseJpaRepositoryDao<UnitIni, String> {
    
	@Query("From UnitIni where unitId = ?1 and iniid=?2")
	public UnitIni getUnitIni(String unitId, String iniId) ;

    @Modifying
    @Query("update UnitIni set nowvalue = ?1 where iniid=?2 and unitId = ?3")
    public void updateNowvalue(String nowValue, String iniid,String unitId);
    
    @Query("select max(id) From UnitIni")
    public long findMaxId();

    void deleteUnitInisByUnitid(String unitId);
    
    @Query("From UnitIni where iniid=?1")
	public List<UnitIni> getIniList(String iniId);
}
