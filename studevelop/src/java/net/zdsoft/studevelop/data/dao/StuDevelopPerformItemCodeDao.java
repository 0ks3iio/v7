package net.zdsoft.studevelop.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StuDevelopPerformItemCode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StuDevelopPerformItemCodeDao extends BaseJpaRepositoryDao<StuDevelopPerformItemCode ,String> {

    @Modifying
    @Query(value = " Delete from StuDevelopPerformItemCode where item_Id = ?1 ")
    public void deleteByItemId(String itemId);

    @Query(nativeQuery = true , value = "select * from studevelop_perform_itemcode  where item_id = ?1")
    public List<StuDevelopPerformItemCode> getAllByItemId(String itemId);

    @Query(nativeQuery = true , value = "select * from studevelop_perform_itemcode  where item_id  IN ?1")
    public List<StuDevelopPerformItemCode> getPerformItemsByItemIds(String[] itemIds);

}
