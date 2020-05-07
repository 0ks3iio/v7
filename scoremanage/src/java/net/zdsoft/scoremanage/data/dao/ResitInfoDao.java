package net.zdsoft.scoremanage.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.entity.ResitInfo;

public interface ResitInfoDao extends BaseJpaRepositoryDao<ResitInfo, String>{
    @Modifying
	@Query("delete From ResitInfo where unitId=?1 and acadyear=?2 and semester=?3 and examId=?4 and gradeId=?5")
	public void deleteResitInfoBy(String unitId, String acadyear,
			String semester, String examId, String gradeId);
    @Query("From ResitInfo where unitId=?1 and acadyear=?2 and semester=?3 and examId=?4 and gradeId=?5")
    public List<ResitInfo> listResitInfoBy(String unitId, String acadyear,
			String semester, String examId, String gradeId);
}
