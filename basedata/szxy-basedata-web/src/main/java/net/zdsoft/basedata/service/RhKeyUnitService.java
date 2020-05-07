package net.zdsoft.basedata.service;


import java.util.List;
import net.zdsoft.basedata.entity.RhKeyUnit;


public interface RhKeyUnitService extends BaseService<RhKeyUnit, String>{

	public List<RhKeyUnit> findByUkey(String ukey);

	public List<RhKeyUnit> findByUnitId(String unitId);

	public List<RhKeyUnit> findByUnitIdAnduKeyId(String unitId, String uKeyId);
}
