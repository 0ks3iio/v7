package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmFiltration;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmFiltrationDao extends BaseJpaRepositoryDao<EmFiltration, String> {

    public List<EmFiltration> findByExamIdAndSchoolIdAndType(String examId,
                                                             String schoolId, String type);

    @Modifying
    @Query("delete from EmFiltration where examId = ?1 and type= ?2 and studentId in (?3)")
    public void deleteByExamIdStu(String examId, String type, String[] studentIds);


}

