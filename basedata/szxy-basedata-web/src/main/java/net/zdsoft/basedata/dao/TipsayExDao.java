package net.zdsoft.basedata.dao;

import net.zdsoft.basedata.entity.TipsayEx;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TipsayExDao extends BaseJpaRepositoryDao<TipsayEx, String>{

    @Modifying
    @Query("delete from TipsayEx where tipsayId in (?1)")
    void deleteByTipsayIds(String[] adjustedIds);

    @Modifying
    @Query("update TipsayEx set state = ?2, auditorId = ?3 where tipsayId = ?1")
    void updateAuditorIdAndStateByAdjustId(String adjustedId, String state, String teacherId);

    @Modifying
    @Query("delete from TipsayEx where (tipsayId in (select id from Tipsay where classId=?1) and sourceType='01' )"
    		+ " or (tipsayId in (select adjustedId from AdjustedDetail where classId=?1) and sourceType='02')")
	void deleteByClassId(String classId);

	void deleteByTipsayIdIn(String[] tipsayIds);

}
