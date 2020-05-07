package net.zdsoft.bigdata.datav.dao;

import net.zdsoft.bigdata.datav.entity.ScreenDemo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yangkj
 * @since 2019/6/19 17:35
 */
@Repository
public interface ScreenDemoDao extends BaseJpaRepositoryDao<ScreenDemo,String> {

    @Query(value = "select * from BG_SCREEN_DEMO order by ORDER_ID",nativeQuery = true)
    List<ScreenDemo> findAllByOrderId();
}
