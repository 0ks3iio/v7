package net.zdsoft.eclasscard.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccHonor;
import net.zdsoft.framework.entity.Pagination;

public interface EccHonorService extends BaseService<EccHonor,String>{

	/**
	 * 查找某单位所有的班级或个人荣誉,分页page
	 * @param unitId
	 * @param page 
	 * @return
	 */
	public List<EccHonor> findByUnitIdAndType(String unitId,Integer type, Pagination page);

	public void saveHonor(EccHonor eccHonor) throws Exception;

	public EccHonor findById(String honorId);

	public void deleteHonor(String honorId,String attachmentId) throws Exception;

	public List<EccHonor> findByIdsAndTime(String[] honorIds, String time);

	public void honorTaskRun(String honorId, boolean isEnd);

	public List<EccHonor> findShowList(String classId, Integer type);

	public List<EccHonor> findListPeriod(String[] ids);
	
	public void addHonorQueue();
	/**
	 * 根据荣誉获得者ids查询
	 * @param honorToObjIds
	 * @return
	 */
	public List<EccHonor> findByHonorToObjIds(String[] honorToObjIds, Pagination page);
	public List<EccHonor> findByIdsDesc(String[] honorIds);

}
