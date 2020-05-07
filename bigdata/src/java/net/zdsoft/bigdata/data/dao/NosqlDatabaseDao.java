package net.zdsoft.bigdata.data.dao;

import net.zdsoft.bigdata.data.entity.NosqlDatabase;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NosqlDatabaseDao extends BaseJpaRepositoryDao<NosqlDatabase, String> {

	List<NosqlDatabase> findNosqlDatabaseByUnitIdOrderByModifyTimeDesc(String unitId);

	@Query("From NosqlDatabase where unitId = ?1 and type in('07','09','12') order by creationTime desc")
	List<NosqlDatabase> findNosqlDatabasesByUnitIdAndTypes(String unitId);

	List<NosqlDatabase> findNosqlDatabasesByUnitIdAndType(String unitId, String type);
}
