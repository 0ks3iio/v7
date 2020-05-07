package net.zdsoft.remote.openapi.service;

import java.util.List;

import net.zdsoft.remote.openapi.entity.OpenApiBusinessTk;

public interface OpenApiBusinessTkService {

	/**
	 * 获取业务
	 * @param ticketkeyId	
	 * @param type	
	 * @param isAll	是否获取全部,false下只获取启用的
	 * @return
	 */
	List<OpenApiBusinessTk> findOpenApiBusinessTkList(String ticketkeyId,int type, boolean isAll);
	
}
