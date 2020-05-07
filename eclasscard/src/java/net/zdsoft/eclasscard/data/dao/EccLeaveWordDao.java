package net.zdsoft.eclasscard.data.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.eclasscard.data.entity.EccLeaveWord;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EccLeaveWordDao extends BaseJpaRepositoryDao<EccLeaveWord, String>{

	@Modifying
    @Query("update from EccLeaveWord set state = 1 Where state != 1 and (senderId = ?1 and receiverId = ?2) or (senderId = ?2 and receiverId = ?1) and creationTime < ?3")
	public void updateStatus(String receiverId, String senderId, Date time);
	
	@Query("From EccLeaveWord Where receiverId in (?1) and state = 0 order by creationTime desc")
	public List<EccLeaveWord> findByReceiverIdsNotRead(String[] stuIds);

}
