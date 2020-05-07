package net.zdsoft.diathesis.data.dao;

import net.zdsoft.diathesis.data.entity.DiathesisCustomAuthor;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/6/11 9:51
 */
public interface DiathesisCustomAuthorDao extends BaseJpaRepositoryDao<DiathesisCustomAuthor,String> {
    @Query("from DiathesisCustomAuthor where unitId=?1 and isDeleted=0")
    List<DiathesisCustomAuthor> findByUnitId(String unitId);

    @Modifying
    @Query("update DiathesisCustomAuthor set isDeleted=1 where unitId=?1 and authorType in ?2")
    void deleteByUnitIdAndAuthorTypeIn(String unitId, List<Integer> oldList);

    @Query("from DiathesisCustomAuthor where regionCode like ?1 and isDeleted=0 and authorType in ?2")
    List<DiathesisCustomAuthor> findByRegionCodeAndAuthorTypeIn(String region, Integer[] authorTypes);

    @Query("from DiathesisCustomAuthor where unitId in ?1 and isDeleted=0 and authorType in ?2 ")
    List<DiathesisCustomAuthor> findByUnitIdInAndAuthorTypeIn(List<String> unitIds, Integer[] authorTypes);

    /**
     * 获得多级所有子单位
     * @param unitId
     * @return
     */
    @Query(value = "select id,parent_id,unit_name,unit_class from base_unit where is_deleted=0 start " +
            " with id=?1 connect by prior id = parent_id order by unit_class",nativeQuery = true)
    List<Object[]> findAllChildUnit(String unitId);
}
