package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmJoinexamschInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmJoinexamschInfoDao extends BaseJpaRepositoryDao<EmJoinexamschInfo, String> {

    public void deleteByExamId(String examId);

    public List<EmJoinexamschInfo> findByExamId(String examId);

    @Query("select count(id) From EmJoinexamschInfo where examId=?1 ")
    public int countByExamId(String examId);

    @Query("From EmJoinexamschInfo where examId=?1 order by schoolId")
    public List<EmJoinexamschInfo> findByExamId(String examId, Pageable pageable);

}
