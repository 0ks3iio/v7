package net.zdsoft.bigdata.datav.dao;

import net.zdsoft.bigdata.datav.entity.ScreenGroup;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2018/11/14 上午9:27
 */
@Repository
public interface ScreenGroupRepository extends BaseJpaRepositoryDao<ScreenGroup, String> {

    List<ScreenGroup> getAllByCreateUserId(String userId, Sort sort);
}
