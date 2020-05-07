package net.zdsoft.remote.openapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.remote.openapi.dao.OpenApiApplyDetailDao;
import net.zdsoft.remote.openapi.entity.OpenApiApplyDetail;
import net.zdsoft.remote.openapi.service.OpenApiApplyDetailService;
@Service("openApiApplyDetailService")
public class OpenApiApplyDetailServiceImpl implements OpenApiApplyDetailService{  
	 @Autowired
	 private OpenApiApplyDetailDao openApiApplyDetailDao;

	@Override
	public List<OpenApiApplyDetail> findByApplyId(String applyId) {
		return openApiApplyDetailDao.findByApplyId(applyId);
	}
}
