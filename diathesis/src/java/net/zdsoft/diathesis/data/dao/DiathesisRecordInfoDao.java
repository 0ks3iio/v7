package net.zdsoft.diathesis.data.dao;

import net.zdsoft.diathesis.data.entity.DiathesisRecordInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 
 * @author niuchao
 * @since  2019年4月1日
 */
public interface DiathesisRecordInfoDao extends BaseJpaRepositoryDao<DiathesisRecordInfo,String> {

	List<DiathesisRecordInfo> findByUnitIdAndRecordIdIn(String unitId, String[] recordIds);

	@Modifying
	void deleteByUnitIdAndRecordIdIn(String unitId, String[] recordIds);

    /**
     * 根据recordId批量删除
     * @param recordIds
     */
	@Modifying
	@Query("delete from DiathesisRecordInfo where recordId in (?1)")
    void deleteByRecordIds(List<String> recordIds);
}
