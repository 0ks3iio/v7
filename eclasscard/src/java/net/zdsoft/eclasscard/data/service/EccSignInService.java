package net.zdsoft.eclasscard.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccSignIn;

public interface EccSignInService extends BaseService<EccSignIn, String>{

	/**
	 * 根据班级ID，场地ID，刷卡状态和时间来查找刷卡信息
	 * @param classId
	 * @param placeId
	 * @param state
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<EccSignIn> findByIdAndTime(String classId, String placeId, String startTime, String endTime);

}
