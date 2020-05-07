package net.zdsoft.newstusys.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newstusys.entity.StusysColsDisplay;

/**
 * 
 * @author weixh
 * 2019年9月3日	
 */
public interface StusysColsDisplayService extends BaseService<StusysColsDisplay, String> {
	public List<StusysColsDisplay> findByUnitIdType(String unitId, String type);
	
	public void deleteByUnitIdType(String unitId, String type);
}
