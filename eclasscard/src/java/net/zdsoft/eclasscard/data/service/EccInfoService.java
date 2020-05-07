package net.zdsoft.eclasscard.data.service;

import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.framework.entity.Pagination;

public interface EccInfoService extends BaseService<EccInfo, String>{
	public List<EccInfo> findByNameAndType(final String name, final String type,final String gradeId,final int status,final String unitId,Pagination page);
	
	public List<EccInfo> findListByNotClass(String unitId);

	public EccInfo findOneFillName(String eccInfoId);
	public List<EccInfo> findListFillName(String[] eccInfoIds);

	public List<EccInfo> findClassAttence(Set<String> placeIds,
			Set<String> classIds);

	public List<EccInfo> findListByUnitAndType(String unitId,String... type);
	
	public List<EccInfo> findListByTypes(String... type);

	public List<EccInfo> findByClassIdIn(String[] classIds);

	public void deleteAllById(String id) throws Exception;

	public List<EccInfo> findByUnitId(String unitId);
	
	public EccInfo findByUnitIdAndName(String unitId,String name);
	
	public List<EccInfo> findListOnLineByIds(String[] ids);
	
	/**
	 * 更新app在线状态
	 * @param id
	 * @param status 1.离线  2.在线
	 * @return
	 */
	public void updateOnLineStatus(String id,int status);
	
}
