package net.zdsoft.bigdata.data.dao;

import net.zdsoft.bigdata.data.entity.BiShare;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BiShareDao extends BaseJpaRepositoryDao<BiShare, String> {

	@Query("From BiShare where shareUserId = ?1 order by creationTime desc")
	public List<BiShare> findBiFavoriteListByShareUserId(String shareUserId);

	@Query("From BiShare where beSharedUserId = ?1 order by creationTime desc")
	public List<BiShare> findBiFavoriteListByBeShareUserId(String beSharedUserId);

	@Query("From BiShare where shareUserId = ?1 order by creationTime desc")
	public List<BiShare> findBiFavoriteListByShareUserId(String shareUserId,
			Pageable page);

	@Query("From BiShare where beSharedUserId = ?1 order by creationTime desc")
	public List<BiShare> findBiFavoriteListByBeShareUserId(
			String beSharedUserId, Pageable page);

	@Query(value = "select count(distinct business_id) from bg_bi_share where share_User_Id =?1",nativeQuery = true)
	Integer countMyShareByUserId(String shareUserId);

	@Query(value = "delete from bg_bi_share where share_user_id= ?1 and business_id =?2 ", nativeQuery = true)
	@Modifying
	public void deleteBiShareByShardUserIdAndBusinessId(String userId,
			String businessId);

	@Query(value = "select count(*) from  bg_bi_share where  be_Shared_User_Id = ?1",nativeQuery = true)
	Integer countBeSharedByUserId(String userId);
}
