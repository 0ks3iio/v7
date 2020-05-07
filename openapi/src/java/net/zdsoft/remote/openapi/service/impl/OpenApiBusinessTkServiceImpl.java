package net.zdsoft.remote.openapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.remote.openapi.dao.OpenApiBusinessTkDao;
import net.zdsoft.remote.openapi.entity.OpenApiBusinessTk;
import net.zdsoft.remote.openapi.service.OpenApiBusinessTkService;

@Service("openApiBusinessTkService")
public class OpenApiBusinessTkServiceImpl implements OpenApiBusinessTkService{
	
	@Autowired
	private OpenApiBusinessTkDao openApiBusinessTkDao;

	@Override
	public List<OpenApiBusinessTk> findOpenApiBusinessTkList(String ticketkeyId, int type, boolean isAll) {
		if(isAll){
			return openApiBusinessTkDao.findOpenApiBusinessTkList(ticketkeyId,type);
		}else{
			return openApiBusinessTkDao.findOpenApiBusinessTkList(ticketkeyId,type,1);
		}
	}
	
}
