package net.zdsoft.studevelop.data.dao;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StudevelopMasterWords;

import java.util.List;

/**
 * 
 * @author weixh
 * @since 2017-7-20 下午5:16:40
 */
public interface StudevelopMasterWordsDao extends BaseJpaRepositoryDao<StudevelopMasterWords, String> {

	@Query("from StudevelopMasterWords where unitId=?1")
	public List<StudevelopMasterWords> findByUnitId(String unitId);
}
