package net.zdsoft.bigdata.dataimport.dao;

import net.zdsoft.bigdata.dataimport.entity.BgDaCustomTable;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author:zhujy
 * @since:2019/6/17 19:04
 */
public interface BgDaCustomTableDao extends BaseJpaRepositoryDao<BgDaCustomTable, String> {

    @Query(value = "select table_name from bg_da_custom_table where id=?1",nativeQuery = true)
    String findTableNameById(String id);

    @Query(value = "select id from bg_da_custom_table where table_name=?1",nativeQuery = true)
    String findIdByTableName(String tableName);

    @Modifying
    @Query(value="update bg_da_custom_table set data_num=data_num+?1 where table_name=?2",nativeQuery = true)
    void updateDataNumByTableName(String dataNum,String tableName);

    @Modifying
    @Query(value="update bg_da_custom_table set data_num=?1 where table_name=?2",nativeQuery = true)
    void updateDataNumToZeroByTableName(String dataNum, String tableName);
}
