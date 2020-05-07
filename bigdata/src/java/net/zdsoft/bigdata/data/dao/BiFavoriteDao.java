package net.zdsoft.bigdata.data.dao;

import java.util.List;

import net.zdsoft.bigdata.data.entity.BiFavorite;
import net.zdsoft.bigdata.data.entity.BiShare;
import net.zdsoft.bigdata.data.entity.MultiReport;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BiFavoriteDao extends BaseJpaRepositoryDao<BiFavorite, String> {

    @Query("From BiFavorite where userId = ?1 order by creationTime desc")
    public List<BiFavorite> findBiFavoriteListByUserId(String userId);

    @Query("FROM BiFavorite where userId =?1 order by creationTime desc")
    List<BiFavorite> findBiFavoriteListByUserId(String userId, Pageable page);

    @Query("From BiFavorite where userId = ?1 and businessId = ?2")
    public List<BiFavorite> findBiFavoriteListByUserIdAndBusinessId(
            String userId, String businessId);

    @Query("select count(id) from BiFavorite where userId = ?1 ")
    Integer countAllByUserId(String userId);

    @Query(value = "delete from bg_bi_favorite where user_id= ?1 and business_id =?2 ", nativeQuery = true)
    @Modifying
    public void deleteBiFavoriteByUserIdAndBusinessId(String userId,
                                                      String businessId);

}
