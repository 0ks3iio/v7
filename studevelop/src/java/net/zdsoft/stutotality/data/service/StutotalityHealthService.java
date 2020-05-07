package net.zdsoft.stutotality.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stutotality.data.entity.StutotalityHealth;

import java.util.List;

public interface StutotalityHealthService extends BaseService<StutotalityHealth,String> {

	public List<StutotalityHealth> findHealthItemByUnitId(String unitId);

	public List<StutotalityHealth> findHealthItemByUnitIdWithMaster(String unitId);

	public void deleteByUnitId(String unitId);

}
