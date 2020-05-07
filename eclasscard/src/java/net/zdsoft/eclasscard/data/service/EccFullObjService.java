package net.zdsoft.eclasscard.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccFullObj;
import net.zdsoft.framework.entity.Pagination;

public interface EccFullObjService extends BaseService<EccFullObj,String>{
	/**
	 * 查找当前班牌，当前时间正在全屏展示的对象
	 * @param eccInfoId
	 * @return
	 */
	public List<EccFullObj> findByBetweenTime(String eccInfoId);
	
	public List<EccFullObj> findByObjectId(String... objectId);
	
	public List<EccFullObj> findBySourceIds(String... sourceIds);
	
	public void deleteByObjectIds(String... objectId);

	public void deleteBySourceIds(String... sourceId);
	
	public List<EccFullObj> findByEccInfoId(String eccInfoId,Pagination page);

	public void addFullObjQueue();

	public void saveWithTask(EccFullObj eccFullObj, boolean isEdit);

	public void fullObjTaskRun(String id, boolean isEnd);

	public void deleteFullObj(String id);

	public void updateFullObjAllLock(String id, boolean isLock);
	
}
