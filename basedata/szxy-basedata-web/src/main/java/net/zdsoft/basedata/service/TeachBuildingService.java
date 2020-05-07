package net.zdsoft.basedata.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.TeachBuilding;

public interface TeachBuildingService extends BaseService<TeachBuilding, String>{

	public List<TeachBuilding> findByUnitId(String unitId);

	public Map<String,String> findTeachBuildMap(String[] ids);

	public List<TeachBuilding> findByUnitIdIn(String[] uidList);
}
