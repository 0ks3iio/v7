package net.zdsoft.eclasscard.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.dto.StuClockResultDto;
import net.zdsoft.eclasscard.data.entity.EccClockIn;
import net.zdsoft.framework.entity.Pagination;

import java.util.Date;
import java.util.List;


public interface EccClockInService extends BaseService<EccClockIn, String>{
	/**
	 * 
	 * @param ownerId
	 * @param infoName
	 * @param cardNumber
	 * @param objectId
	 * @param clockType
	 * @param type 区分行政班班牌 上学放学（1）与上课考勤（0）
	 * @return
	 */
	public StuClockResultDto dealClockIn(String ownerId, String infoName, String cardNumber, String objectId, Integer clockType,Integer type);

	public List<EccClockIn> findListGateRecently(String eccInfoId);

	/**
	 * 打卡日志
	 * @param ownerType 类型：教师或学生
	 * @param startTime
	 * @param endTime
	 * @param type 班牌类型
	 * @param ownerId
	 * @return
	 */
	public List<EccClockIn> findListByAll(String unitId, String ownerType, Date startTime, Date endTime, String type, String ownerId, Pagination page);
}
