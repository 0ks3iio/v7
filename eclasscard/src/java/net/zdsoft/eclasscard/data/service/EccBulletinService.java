package net.zdsoft.eclasscard.data.service;

import java.text.ParseException;
import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccBulletin;
import net.zdsoft.framework.entity.Pagination;

public interface EccBulletinService extends BaseService<EccBulletin, String>{

	public List<EccBulletin> findList(String eccInfoId,String userId,String unitId, String startTime, String endTime,
			Integer status, Pagination page);

	public void saveBulletin(EccBulletin eccBulletin, String[] eccInfoIds,String userId,String unitId) throws ParseException;

	public List<EccBulletin> findListOnEcc(String eccInfoId);

	public List<EccBulletin> findListPeriod(String[] ids);
	
	public void upadteStatus(String nowTime);

	public void deleteBulletin(String id);
	/**
	 * 定时器未展示的通知公告时间到delayedqueue
	 */
	public void addBulletinQueue();

	public void bulletinTaskRun(String bulletinId,boolean isEnd);

	/**
	 * 标准版-班牌公告List
	 * @param eccInfoId
	 * @param userId
	 * @param unitId
	 * @param startTimeStr
	 * @param endTimeStr
	 * @param status
	 * @param page
	 * @return
	 */
	public List<EccBulletin> findStandardList(String eccInfoId, String userId,String unitId,String bulletinLevel,Pagination page);

	/**
	 * 标准版-查询各种类型的公告
	 * @param id
	 * @param eccBulletinType1
	 * @return
	 */
	public List<EccBulletin> saveOrFindListOnEccAndType(String eccInfoId,Integer type);

	public List<EccBulletin> findBulletinList(String[] ids, Integer type);
	
}

