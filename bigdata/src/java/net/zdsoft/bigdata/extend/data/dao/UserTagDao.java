package net.zdsoft.bigdata.extend.data.dao;

import java.util.List;

import net.zdsoft.bigdata.extend.data.entity.UserTag;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by wangdongdong on 2018/7/9 13:44.
 */
public interface UserTagDao extends BaseJpaRepositoryDao<UserTag, String> {

    List<UserTag> getUserTagByProfileCodeOrderByCreationTimeAsc(String profileCode);

    @Transactional
    void deleteUserTagByParentId(String parentId);

    List<UserTag> getUserTagByParentId(String parentId);

    @Query("FROM UserTag where profileCode=?1 and parentId <> '00000000000000000000000000000000'")
    List<UserTag> getSecondaryUserTagByProfileCode(String profileCode);
    
    @Query("FROM UserTag where profileCode=?1 and parentId = '00000000000000000000000000000000'")
    List<UserTag> getfirstUserTagByProfileCode(String profileCode);

    @Query("FROM UserTag where parentId = ?1 order by orderId")
	List<UserTag> getSecondaryUserTagByParenrId(String parenrId);
}
