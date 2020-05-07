package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmSpaceItem;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmSpaceItemDao extends BaseJpaRepositoryDao<EmSpaceItem, String> {

    @Query("From EmSpaceItem where statParmId=?1 order by parmType,lowScore,upScore")
    List<EmSpaceItem> findByStatParmId(String statParmId);

    void deleteByStatParmId(String statParmId);

    List<EmSpaceItem> findByStatParmIdIn(String[] statParmIds);

    List<EmSpaceItem> findByNameAndIdIn(String name, String[] ids);

    List<EmSpaceItem> findByParmTypeAndIdIn(String parmType, String[] ids);

    @Query("From EmSpaceItem where id in(?1)")
    List<EmSpaceItem> findByIds(String[] ids);
}
