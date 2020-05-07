package net.zdsoft.bigdata.base.dao;

import net.zdsoft.bigdata.base.entity.PropertyTopic;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yangkj
 * @since 2019/5/20 10:54
 */
public interface BgPropertyTopicDao extends BaseJpaRepositoryDao<PropertyTopic,String> {

    PropertyTopic findByName(String name);
}
