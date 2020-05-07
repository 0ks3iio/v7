package net.zdsoft.webservice.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.webservice.entity.SuCdStaffBind;

public interface SuCdStaffBindService extends BaseService<SuCdStaffBind, String>{
	/**
	 * 处理移动绑定关系，并且按格式返回
	 * @param msg
	 * @return
	 */
	public String dealBindMsg(String msg);
}
