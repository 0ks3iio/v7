package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.SystemIni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author shenke
 * @since 2019/3/20 下午4:56
 */
@Repository
public interface SystemIniDao extends JpaRepository<SystemIni, String> {

    @Query(value = "select nowValue from SystemIni where iniId=?1")
    String getNowValueByIniId(String iniId);

    SystemIni getSystemIniByIniId(String iniId);
}
