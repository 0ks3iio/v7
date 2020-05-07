package net.zdsoft.eclasscard.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccFullObjAll;
import net.zdsoft.framework.entity.Pagination;

public interface EccFullObjAllService extends BaseService<EccFullObjAll,String>{
	
	public void saveFullObjAll(EccFullObjAll fullObjAll,String[] eccInfoIds,boolean isEdit);
	
	/**
	 * 全屏展示内容状态更新
	 * @param fullObjAlls
	 */
	public void updateFullObjAllStatus(List<EccFullObjAll> fullObjAlls);

	public List<EccFullObjAll> findByObjectId(String... objectId);

	public List<EccFullObjAll> findByUnitId(String unitId,Pagination page);
	
	public void deleteByObjectIds(String... objectId);

	public void fullObjAllTaskRun(String id, boolean isEnd);

	public void addFullObjAllQueue();

	public void deleteFullObjAll(String id);
	
	public void updateFullObjAllLock(String id,boolean isLock);
}
