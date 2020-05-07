package net.zdsoft.remote.openapi.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.remote.openapi.entity.OpenApiApplyDetail;

public interface OpenApiApplyDetailDao extends BaseJpaRepositoryDao<OpenApiApplyDetail,String>{
	List<OpenApiApplyDetail> findByApplyId(String applyId);
}
