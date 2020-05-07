package net.zdsoft.syncdata.service;

import net.zdsoft.syncdata.dto.ResultDto;
import net.zdsoft.syncdata.entity.EdqUser;

/**
 * 
 * @author weixh
 * @since 2017年11月30日 上午11:10:28
 */
public interface EdqUserService {
	public ResultDto saveEdqUser(EdqUser eu);

}
