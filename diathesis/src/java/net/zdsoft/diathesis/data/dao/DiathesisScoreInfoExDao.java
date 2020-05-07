package net.zdsoft.diathesis.data.dao;

import net.zdsoft.diathesis.data.entity.DiathesisScoreInfoEx;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/8/14 19:21
 */
public interface DiathesisScoreInfoExDao extends BaseJpaRepositoryDao<DiathesisScoreInfoEx, String> {

    @Query("from DiathesisScoreInfoEx where scoreInfoId in ?1")
    List<DiathesisScoreInfoEx> findListByInfoIdIn(String[] infoExList);

    @Query("from DiathesisScoreInfoEx where scoreTypeId = ?1")
    List<DiathesisScoreInfoEx> findByScoreTypeId(String scoreTypeId);

    @Query("from DiathesisScoreInfoEx where scoreTypeId in ?1")
    List<DiathesisScoreInfoEx> findListByInfoTypeIdIn(List<String> scoreTypeIds);
}
