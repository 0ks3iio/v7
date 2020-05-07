package net.zdsoft.stutotality.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.entity.StutotalityCheckStu;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StutotalityCheckStuDao extends BaseJpaRepositoryDao<StutotalityCheckStu, String> {

    @Query("from StutotalityCheckStu where unitId = ?1 and acadyear = ?2 and semester = ?3 ")
    List<StutotalityCheckStu> findByUParms(String unitId, String acadyear, String semester);

    @Query("from StutotalityCheckStu where gradeId = ?1 and acadyear = ?2 and semester = ?3 ")
    List<StutotalityCheckStu> findByGParms(String gradeId, String acadyear, String semester);

    @Query("from StutotalityCheckStu where studentId in (?1) and acadyear = ?2 and semester = ?3 ")
    List<StutotalityCheckStu>  findBySParms(String[] studentIds, String acadyear, String semester);
}
