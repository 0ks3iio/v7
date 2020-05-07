package net.zdsoft.gkelective.data.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.entity.GkRounds;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GkRoundsDao extends BaseJpaRepositoryDao<GkRounds, String>{

	@Query("From GkRounds where id =?1 and isDeleted =0")
	public GkRounds findRoundById(String roundsId);
	@Query("From GkRounds where subjectArrangeId =?1 and isDeleted =0 order by orderId desc ")
	public List<GkRounds> findBySubjectArrangeId(String arrangeId);
	@Query("From GkRounds where isDeleted=0 and subjectArrangeId=?1 and orderId=(select max(orderId) from GkRounds where isDeleted=0 and subjectArrangeId=?1)")
	public GkRounds getCurrentGkRounds(String arrangeId);
	@Modifying
	@Query("update GkRounds set isDeleted=1 where id = ?1")
	public void deleteById(String id);
	@Modifying
	@Query("update GkRounds set orderId=orderId-1 where subjectArrangeId=?1 and orderId > ?2")
	public void updateOrderIdByArrangeId(String arrangeId, int oldOrderId);
	@Modifying
	@Query("update GkRounds set step=?1,modifyTime=?2 where id=?3 ")
	public void updateStep(int step, Date time,String id);
	@Query("From GkRounds where subjectArrangeId =?1 ")
	public List<GkRounds> findBySubjectArrangeIdHasDelete(String arrangeId);
	@Query("From GkRounds where subjectArrangeId =?1 and isDeleted =0  and openClassType = ?2 order by orderId desc ")
	public List<GkRounds> findByArrangeIdClassType(String arrangeId,
			String openClassType);
}
