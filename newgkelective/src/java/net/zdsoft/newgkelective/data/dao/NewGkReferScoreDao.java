package net.zdsoft.newgkelective.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkReferScore;

public interface NewGkReferScoreDao extends BaseJpaRepositoryDao<NewGkReferScore, String>{
    @Query("from NewGkReferScore where isDeleted = 0 and unitId = ?1 and gradeId = ?2 and name in (?3)")
	public List<NewGkReferScore> findByNames(String unitId, String gradeId, String[] names);
    @Query("from NewGkReferScore where isDeleted = 0 and unitId = ?1 and gradeId = ?2 order by isDefault desc, times desc")
	public List<NewGkReferScore> findListByGradeId(String unitId, String gradeId);
    @Query("from NewGkReferScore where isDeleted = 0 and gradeId = ?1 order by isDefault desc, times desc")
    public List<NewGkReferScore> findListByGradeId(String gradeId);
    @Query("from NewGkReferScore where isDeleted = 0 and unitId = ?1")
    public List<NewGkReferScore> findByUnitId(String unitId);
    @Modifying
    @Query("update NewGkReferScore set isDeleted = 1,modifyTime =?1 where id = ?2")
    public void deleteById(Date currentDate,String id);

    // Basedata Sync Method
    @Modifying
    @Query("update NewGkReferScore set isDeleted=1, modifyTime=?1 where gradeId in(?2)")
    void deleteByGradeIds(Date date, String... gradeIds);
}
