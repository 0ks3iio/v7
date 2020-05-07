package net.zdsoft.basedata.dao;
import java.util.List;
import net.zdsoft.basedata.entity.RhKeyUnit;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface RhKeyUnitDao extends BaseJpaRepositoryDao<RhKeyUnit, String> {
	
	public List<RhKeyUnit> findByUkey(String ukey);

	public List<RhKeyUnit> findByUnitId(String unitId);

	public List<RhKeyUnit> findByUnitIdAndUkey(String unitId, String uKeyId);
}
