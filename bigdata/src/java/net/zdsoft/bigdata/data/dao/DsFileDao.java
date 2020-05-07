package net.zdsoft.bigdata.data.dao;

import net.zdsoft.bigdata.data.entity.DsFile;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DsFileDao extends BaseJpaRepositoryDao<DsFile, String> {

	@Query("From DsFile where unitId = ?1 order by creationTime desc")
	List<DsFile> findDsFileByUnitId(String unitId);

}
