package net.zdsoft.bigdata.extend.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.bigdata.extend.data.entity.FormSet;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface FormSetDao extends BaseJpaRepositoryDao<FormSet, String> {

	@Query("FROM FormSet order by orderId ")
	List<FormSet> findListByOrder();

	FormSet findByMdId(String mdId);


}
