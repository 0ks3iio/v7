package net.zdsoft.bigdata.datav.dao;

import net.zdsoft.bigdata.datav.entity.Screen;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/26 15:42
 */
@Repository
public interface ScreenRepository extends BaseJpaRepositoryDao<Screen, String> {

    @Query(
            value = "select count(1) from Screen "
    )
    long countAll();

    @Modifying
    @Query(
            value = "update Screen set name=?1 where id=?2"
    )
    void updateName(String name, String screenId);

    @Modifying
    @Query(
            value = "delete from Screen where id in (?1)"
    )
    void batchDelete(String[] ids);

    @Modifying
    @Query(
            value = "update Screen set orderType=?2 where id=?1"
    )
    void updateOrderType(String id, Integer orderType);

    /**
     * 和权限相关的
     * @param unitId 单位ID
     * @param userId 用户ID
     * @return
     */
    @Query(
            value = "from Screen where createUserId=?1 order by creationTime desc"
            //value = "select * from (" +
            //        "(select * from BG_SCREEN where UNIT_ID = :unitId and (ORDER_TYPE<>1 and create_user_id is null))" +
            //        " union  " +
            //        "(select * from BG_SCREEN where create_user_id=:userId and ORDER_TYPE=1) " +
            //        " union " +
            //        "(select c.* from BG_SCREEN c, BG_SUBSCRIBE s " +
            //        " where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId )" +
            //        " union " +
            //        "(select c.* from BG_SCREEN c, BG_SUBSCRIBE s " +
            //        " where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=6 and s.USER_ID = :userId )" +
            //        ") order by creation_time desc ",
            //comments by shenke at 19.06.10

            //countQuery = "select count(*) from (" +
            //        "(select * from BG_SCREEN where UNIT_ID = :unitId)" +
            //        " union  " +
            //        "(select c.* from BG_SCREEN c, BG_SUBSCRIBE s" +
            //        " where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId)" +
            //        ")",
            //nativeQuery = true
    )
    List<Screen> getScreensForEdit(String userId);


    @Query(
            //value = "from Screen where createUserId=?1 or (orderType<>1 and createUserId <>?1) or (orderType<>1 and createUserId is null ) order by creationTime desc "
            value = "from Screen where (createUserId=?1 and orderType=1) or orderType <>1"
    )
    List<Screen> getScreensForSuper(String userId);

    @Query(
            value = "select * from (" +
                    "(select * from BG_SCREEN where ORDER_TYPE = 2 and UNIT_ID " +
                    "in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id)) " +
                    "union " +
                    "(select * from BG_SCREEN where ORDER_TYPE = 3 and UNIT_ID = :unitId ) " +
                    "union " +
                    "(select c.* from BG_SCREEN c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID " +
                    ") " +
                    "union " +
                    "(select c.* from BG_SCREEN c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID " +
                    ") " +
                    "union " +
                    "(select c.* from BG_SCREEN c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID " +
                    ") " +
                    ") order by creation_time desc ",
            countQuery = "select count(*) from (" +
                    "(select * from BG_SCREEN where ORDER_TYPE = 2 and UNIT_ID " +
                    "in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id)) " +
                    "union " +
                    "(select * from BG_SCREEN where ORDER_TYPE = 3 and UNIT_ID = :unitId ) " +
                    "union " +
                    "(select c.* from BG_SCREEN c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID " +
                    ") " +
                    "union " +
                    "(select c.* from BG_SCREEN c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID " +
                    ") " +
                    "union " +
                    "(select c.* from BG_SCREEN c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID " +
                    ") " +
                    ")",
            nativeQuery = true
    )
    List<Screen> getScreensForQuery(@Param("unitId") String unitId, @Param("userId") String userId);


    @Query(
            value = "select * from (" +
                    "(select * from BG_SCREEN where ORDER_TYPE = 2 and UNIT_ID " +
                    "in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id)) " +
                    "union " +
                    "(select * from BG_SCREEN where ORDER_TYPE = 3 and UNIT_ID = :unitId ) " +
                    "union " +
                    "(select c.* from BG_SCREEN c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID " +
                    ") " +
                    "union " +
                    "(select c.* from BG_SCREEN c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID " +
                    ") " +
                    "union " +
                    "(select c.* from BG_SCREEN c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID " +
                    ") " +
                    ") where id in (:screenIds) order by creation_time desc ",
            countQuery = "select count(*) from (" +
                    "(select * from BG_SCREEN where ORDER_TYPE = 2 and UNIT_ID " +
                    "in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id)) " +
                    "union " +
                    "(select * from BG_SCREEN where ORDER_TYPE = 3 and UNIT_ID = :unitId ) " +
                    "union " +
                    "(select c.* from BG_SCREEN c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID " +
                    ") " +
                    "union " +
                    "(select c.* from BG_SCREEN c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID " +
                    ") " +
                    "union " +
                    "(select c.* from BG_SCREEN c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID " +
                    ") " +
                    ")",
            nativeQuery = true
    )
    List<Screen> getScreensForQuery(@Param("unitId") String unitId, @Param("userId") String userId, @Param("screenIds") String[] screenIds);

    @Query(
            value = "select count(*) from (" +
                    "(select * from BG_SCREEN where ORDER_TYPE = 2 and UNIT_ID " +
                    "in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id)) " +
                    "union " +
                    "(select * from BG_SCREEN where ORDER_TYPE = 3 and UNIT_ID = :unitId ) " +
                    "union " +
                    "(select c.* from BG_SCREEN c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID " +
                    ") " +
                    "union " +
                    "(select c.* from BG_SCREEN c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID " +
                    ") " +
                    "union " +
                    "(select c.* from BG_SCREEN c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID " +
                    ") " +
                    ")",
            nativeQuery = true
    )
    long countScreensForQuery(@Param("unitId") String unitId, @Param("userId") String userId);
}
