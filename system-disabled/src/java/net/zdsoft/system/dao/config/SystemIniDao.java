package net.zdsoft.system.dao.config;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.config.SystemIni;

public interface SystemIniDao extends BaseJpaRepositoryDao<SystemIni, String> {

    SystemIni findByIniid(String iniid);

    List<SystemIni> findByIniidIn(String... iniid);

    @Modifying
    @Query("update SystemIni set nowvalue = ?1 where iniid=?2")
    void updateNowvalue(String nowValue, String iniid);

    /**
     * // 更新nowValue值类型
     * 
     * @param valueType
     * @param iniid
     */
    @Modifying
    @Query("update SystemIni set valueType = ?1 where iniid=?2")
    void updateValueType(int valueType, String iniid);

}
