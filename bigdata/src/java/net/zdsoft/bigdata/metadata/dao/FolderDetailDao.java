package net.zdsoft.bigdata.metadata.dao;

import net.zdsoft.bigdata.metadata.entity.FolderDetail;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.stream.Stream;

public interface FolderDetailDao extends BaseJpaRepositoryDao<FolderDetail, String> {

    @Query(value = "select distinct * from (" +
                    "(select * from BG_FOLDER_DETAIL where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id)) "+
                    "union all " +
                    "(select * from BG_FOLDER_DETAIL where ORDER_TYPE = 3 and UNIT_ID = :unitId) " +
                    "union all " +
                    "(select bf.* from BG_FOLDER_DETAIL bf , BG_SUBSCRIBE s where bf.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = bf.BUSINESS_ID" +
                    ") " +
                    "union all " +
                    "(select bf.* from BG_FOLDER_DETAIL bf , BG_SUBSCRIBE s where bf.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = bf.BUSINESS_ID" +
                    ") " +
                    "union all " +
                    "(select bf.* from BG_FOLDER_DETAIL bf , BG_SUBSCRIBE s where bf.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = bf.BUSINESS_ID" +
                    ") " +
                    ") order by order_id",
            nativeQuery = true
    )
    List<FolderDetail> findAllAuthorityFolderDetail(@Param("unitId")String unitId, @Param("userId")String userId);

    @Query(value = "select distinct * from (" +
            "(select * from BG_FOLDER_DETAIL where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id)) "+
            "union all " +
            "(select * from BG_FOLDER_DETAIL where ORDER_TYPE = 3 and UNIT_ID = :unitId) " +
            "union all " +
            "(select bf.* from BG_FOLDER_DETAIL bf , BG_SUBSCRIBE s where bf.ORDER_TYPE = 4 " +
            " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = bf.BUSINESS_ID" +
            ") " +
            "union all " +
            "(select bf.* from BG_FOLDER_DETAIL bf , BG_SUBSCRIBE s where bf.ORDER_TYPE = 5" +
            " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = bf.BUSINESS_ID" +
            ") " +
            "union all " +
            "(select bf.* from BG_FOLDER_DETAIL bf , BG_SUBSCRIBE s where bf.ORDER_TYPE = 6" +
            " and s.USER_ID=:userId and s.BUSINESS_ID = bf.BUSINESS_ID" +
            ") " +
            ") where BUSINESS_NAME like :businessName order by CREATION_TIME desc ",
            nativeQuery = true
    )
    List<FolderDetail> findAllAuthorityFolderDetailByBusinessName(@Param("unitId")String unitId, @Param("userId")String userId, @Param("businessName")String businessName);

    @Query(
            value = "select * from BG_FOLDER_DETAIL where BUSINESS_NAME like :businessName order by CREATION_TIME desc ",
            nativeQuery = true
    )
    List<FolderDetail> findAllFolderDetailByBusinessName(@Param("businessName")String businessName);

    @Query(value = "select distinct * from (" +
            "(select * from BG_FOLDER_DETAIL where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id)) "+
            "union all " +
            "(select * from BG_FOLDER_DETAIL where ORDER_TYPE = 3 and UNIT_ID = :unitId) " +
            "union all " +
            "(select bf.* from BG_FOLDER_DETAIL bf , BG_SUBSCRIBE s where bf.ORDER_TYPE = 4 " +
            " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = bf.BUSINESS_ID) " +
            "union all " +
            "(select bf.* from BG_FOLDER_DETAIL bf , BG_SUBSCRIBE s where bf.ORDER_TYPE = 5" +
            " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = bf.BUSINESS_ID)" +
            "union all (select bf.* from BG_FOLDER_DETAIL bf , BG_SUBSCRIBE s where bf.ORDER_TYPE = 6" +
            " and s.USER_ID=:userId and s.BUSINESS_ID = bf.BUSINESS_ID)" +
            ") where FOLDER_ID in :folderIds order by order_id",
            nativeQuery = true
    )
    List<FolderDetail> findAllAuthorityFolderDetailByFolderId(@Param("unitId")String unitId, @Param("userId")String userId, @Param("folderIds")String[] folderIds);

    @Query(
            value = "select * from BG_FOLDER_DETAIL where FOLDER_ID in :folderIds",
            nativeQuery = true
    )
    List<FolderDetail> findAllFolderDetailByFolderId(@Param("folderIds")String[] folderIds);

    void deleteByBusinessId(String businessId);

    void deleteByBusinessIdIn(String[] businessIds);

    @Query(value = "select max(ORDER_ID) from BG_FOLDER_DETAIL where FOLDER_ID = :folderId",
            nativeQuery = true)
    Integer getMaxOrderIdByFolderId(@Param("folderId")String folderId);

    @Query(value = "select count(*) from (select distinct * from (" +
            "(select * from BG_FOLDER_DETAIL where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id)) "+
            "union all " +
            "(select * from BG_FOLDER_DETAIL where ORDER_TYPE = 3 and UNIT_ID = :unitId) " +
            "union all " +
            "(select bf.* from BG_FOLDER_DETAIL bf , BG_SUBSCRIBE s where bf.ORDER_TYPE = 4 " +
            " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = bf.BUSINESS_ID" +
            ") " +
            "union all " +
            "(select bf.* from BG_FOLDER_DETAIL bf , BG_SUBSCRIBE s where bf.ORDER_TYPE = 5" +
            " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = bf.BUSINESS_ID" +
            ") " +
            "union all " +
            "(select bf.* from BG_FOLDER_DETAIL bf , BG_SUBSCRIBE s where bf.ORDER_TYPE = 6" +
            " and s.USER_ID=:userId and s.BUSINESS_ID = bf.BUSINESS_ID" +
            ") " +
            ") where BUSINESS_TYPE= :businessType)",
            nativeQuery = true
    )
    long countAllAuthorityFolderDetail(@Param("unitId")String unitId, @Param("userId")String userId, @Param("businessType")String businessType);

    @Query(value = "select * from (" +
            "select distinct * from ((select * from BG_FOLDER_DETAIL where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id)) "+
            "union all " +
            "(select * from BG_FOLDER_DETAIL where ORDER_TYPE = 3 and UNIT_ID = :unitId) " +
            "union all " +
            "(select bf.* from BG_FOLDER_DETAIL bf , BG_SUBSCRIBE s where bf.ORDER_TYPE = 4 " +
            " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = bf.BUSINESS_ID" +
            ") " +
            "union all " +
            "(select bf.* from BG_FOLDER_DETAIL bf , BG_SUBSCRIBE s where bf.ORDER_TYPE = 5" +
            " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = bf.BUSINESS_ID" +
            ") " +
            "union all " +
            "(select bf.* from BG_FOLDER_DETAIL bf , BG_SUBSCRIBE s where bf.ORDER_TYPE = 6" +
            " and s.USER_ID=:userId and s.BUSINESS_ID = bf.BUSINESS_ID" +
            ")) order by CREATION_TIME DESC" +
            ") where ROWNUM <= 20",
            nativeQuery = true
    )
    List<FolderDetail> findRecentAuthorityFolderDetail(@Param("unitId")String unitId, @Param("userId")String userId);

    @Query(value = "select * from (" +
            "select distinct * from ((select * from BG_FOLDER_DETAIL where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id)) "+
            "union all " +
            "(select * from BG_FOLDER_DETAIL where ORDER_TYPE = 3 and UNIT_ID = :unitId) " +
            "union all " +
            "(select bf.* from BG_FOLDER_DETAIL bf , BG_SUBSCRIBE s where bf.ORDER_TYPE = 4 " +
            " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = bf.BUSINESS_ID" +
            ") " +
            "union all " +
            "(select bf.* from BG_FOLDER_DETAIL bf , BG_SUBSCRIBE s where bf.ORDER_TYPE = 5" +
            " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = bf.BUSINESS_ID" +
            ") " +
            "union all " +
            "(select bf.* from BG_FOLDER_DETAIL bf , BG_SUBSCRIBE s where bf.ORDER_TYPE = 6" +
            " and s.USER_ID=:userId and s.BUSINESS_ID = bf.BUSINESS_ID" +
            ")) order by CREATION_TIME DESC" +
            ") where business_type=1",
            nativeQuery = true
    )
    Stream<FolderDetail> getCharts(@Param("unitId")String unitId, @Param("userId")String userId);

    @Query(
            value = "select * from (select * from BG_FOLDER_DETAIL order by CREATION_TIME DESC) where rownum <=20",
            nativeQuery = true
    )
    List<FolderDetail> findRecentFolderDetail();
}
