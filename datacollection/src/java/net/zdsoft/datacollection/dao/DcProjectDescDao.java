package net.zdsoft.datacollection.dao;

import java.util.List;

import net.zdsoft.datacollection.entity.DcProjectDesc;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface DcProjectDescDao extends BaseJpaRepositoryDao<DcProjectDesc, String> {

	List<DcProjectDesc> findByProjectId(String projectId);
}
