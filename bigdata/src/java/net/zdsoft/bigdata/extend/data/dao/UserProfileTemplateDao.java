package net.zdsoft.bigdata.extend.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.bigdata.extend.data.entity.UserProfileTemplate;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface UserProfileTemplateDao extends BaseJpaRepositoryDao<UserProfileTemplate, String> {

	@Query("FROM UserProfileTemplate where profileCode=?1 order by orderId")
	List<UserProfileTemplate> findByProfileCode(String profileCode);

	void deleteBytagId(String tagId);

}
