package net.zdsoft.diathesis.data.dao;

import net.zdsoft.diathesis.data.entity.DiathesisEvaluate;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/5/15 10:32
 */
public interface DiathesisEvaluateDao extends BaseJpaRepositoryDao<DiathesisEvaluate,String> {

	@Query("from DiathesisEvaluate where unitId=?1 and studentId=?2 order by modifyTime desc")
	List<DiathesisEvaluate> findByUnitIdAndStudentId(String unitId, String studentId);

}
