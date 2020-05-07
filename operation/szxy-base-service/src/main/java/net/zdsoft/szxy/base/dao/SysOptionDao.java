package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.SysOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author shenke
 * @since 2019/3/19 下午6:25
 */
@Repository
public interface SysOptionDao extends JpaRepository<SysOption, String> {

    @Query(value = "select nowValue from SysOption where optionCode=?1 and isDeleted=0")
    String getValueByOptionCode(String optionCode);

    SysOption getSysOptionByOptionCode(String optionCode);
}
