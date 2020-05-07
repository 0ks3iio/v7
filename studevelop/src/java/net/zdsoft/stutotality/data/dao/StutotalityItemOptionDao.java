package net.zdsoft.stutotality.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.entity.StutotalityItemOption;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * stutotality_item_option
 * @author
 *
 */
public interface StutotalityItemOptionDao extends BaseJpaRepositoryDao<StutotalityItemOption, String>{
    @Query("From StutotalityItemOption where itemId = ?1 ORDER BY orderNumber asc")
    public List<StutotalityItemOption> findListByItemId(String itemId);

    public void deleteStutotalityItemOptionById(String id);

    @Query("From StutotalityItemOption where itemId in (?1) order by orderNumber")
    public List<StutotalityItemOption> findByItemIds(String [] itemIds);

    @Modifying
    @Query("delete From StutotalityItemOption where unitId = ?1 and itemId in (?2)")
    void deleteByUnitIdAndItemId(String udit,String [] itemIds);
    @Query("From StutotalityItemOption where unitId in (?1) order by orderNumber")
    public List<StutotalityItemOption> findListByUnitId(String unitId);

}
