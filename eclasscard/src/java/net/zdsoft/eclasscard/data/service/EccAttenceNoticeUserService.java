package net.zdsoft.eclasscard.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccAttenceNoticeUser;

public interface EccAttenceNoticeUserService extends BaseService<EccAttenceNoticeUser, String> {

	public void updateNoticeUser(String type,String[] userIds,String unitId);
	public List<EccAttenceNoticeUser> findUserWhithName(String type);
	/**
	 * 按类型查本单位要发送的通知人员
	 * @param unitId
	 * @param type
	 * @return
	 */
	public List<EccAttenceNoticeUser> findByUnitIdAndType(String unitId,
			String type);

}
