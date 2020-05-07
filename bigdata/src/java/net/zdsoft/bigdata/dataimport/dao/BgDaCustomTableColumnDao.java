package net.zdsoft.bigdata.dataimport.dao;

import net.zdsoft.bigdata.dataimport.entity.BgDaCustomTableColumn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author:zhujy
 * @since:2019/6/17 19:28
 */
public interface BgDaCustomTableColumnDao extends BaseJpaRepositoryDao<BgDaCustomTableColumn, String> {

    @Modifying
    @Query(value = "delete from bg_da_custom_table_column where table_id=?1",nativeQuery = true)
    void deleteByTableId(String id);

    @Query(value = "select column_name from bg_da_custom_table_column where table_id=?1",nativeQuery = true)
    List<String> findColumnNameByTableId(String id);
}
