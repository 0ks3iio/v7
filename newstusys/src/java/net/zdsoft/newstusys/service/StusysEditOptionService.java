package net.zdsoft.newstusys.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newstusys.entity.StusysEditOption;

/**
 * 
 * @author weixh
 * 2019年9月4日	
 */
public interface StusysEditOptionService extends BaseService<StusysEditOption, String> {
	public StusysEditOption findByUnitId(String unitId);
	
	public boolean isEditOpen(String unitId);
	
	public void saveOption(StusysEditOption option);

}
