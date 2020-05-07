package net.zdsoft.gkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.entity.GkBatch;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GkBatchDao extends BaseJpaRepositoryDao<GkBatch, String> {
	@Query("From GkBatch where roundsId in (?1) order by groupClassId desc")
	public List<GkBatch> findByRoundsId(String... roundsId);
	@Query("From GkBatch where  groupClassId<> ?1 and roundsId in (?2) order by groupClassId desc ")
	public List<GkBatch> findByGroupRoundsId(String groupClassId,String... roundsId);
	@Query("From GkBatch where  groupClassId= ?1 and roundsId in (?2) order by groupClassId desc ")
	public List<GkBatch> findByRoundsIdAndGroupClassId(String groupClassId,String... roundsId);
    @Query("From GkBatch where roundsId=?1 and teachClassId in (?2) order by batch")
    public List<GkBatch> findByClassIds(String roundsId, String[] teachClassIds);
//
    @Query("From GkBatch where roundsId=?1 and placeId in (?2) order by batch")
    public List<GkBatch> findByPlaceIds(String roundsId, String[] placeIds);

    @Query("From GkBatch where roundsId=?1 and batch = ?2 and teachClassId in (?3)")
    public List<GkBatch> findByBatchAndInClassIds(String roundId, int batch, String[] teachClassIds);
//
//    @Query("From GkBatch where subjectArrangeId=?1 and batch = ?2")
//    public List<GkBatch> findByArrangeIdAndBatch(String arrangeId, int batch);
//
    @Modifying
    @Query("delete from GkBatch where id in (?1)")
    public void deleteByIds(String[] ids);
//
//    @Modifying
//    @Query("delete from GkBatch where teachClassId in (?1)")
//    public void deleteByClassIds(String[] clsIds);
//
//    @Query("From GkBatch where subjectArrangeId in (?1)")
//    public List<GkBatch> findByArrangeId(String... arrangeIds);
//
    
    @Query("From GkBatch where roundsId =?1 and groupClassId in (?2)")
	public List<GkBatch> findBatchListByGroupClassId(String roundsId, String... groupClassId);
    @Modifying
    @Query("delete from GkBatch where roundsId =?1 ")
    public void deleteByRoundsId(String id);
    @Query("From GkBatch where roundsId=?1 and batch = ?2 ")
	public List<GkBatch> findByBatch(String roundId, int batch);

}
