package net.zdsoft.stutotality.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.entity.StutotalityGoodStat;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StutotalityGoodStatDao extends BaseJpaRepositoryDao<StutotalityGoodStat, String> {
    @Modifying
    @Query("delete From StutotalityGoodStat where acadyear = ?1 and semester=?2 and studentId in (?3)")
    public void delByStudentIds(String year, String semeter, String[] studentIds);
    @Query("From StutotalityGoodStat where acadyear = ?1 and semester=?2 and studentId in (?3)")
    List<StutotalityGoodStat> findListByStudentIds(String year, String semete, String[] studentIds );
}
