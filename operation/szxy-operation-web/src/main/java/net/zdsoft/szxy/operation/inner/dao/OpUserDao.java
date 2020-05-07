package net.zdsoft.szxy.operation.inner.dao;

import net.zdsoft.szxy.operation.inner.entity.OpUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author shenke
 * @since 2019/1/9 下午3:21
 */
@Repository
public interface OpUserDao extends JpaRepository<OpUser, String>, JpaSpecificationExecutor<OpUser> {

    @Query(value = "from OpUser where username=?1 and isDeleted=0 and state=1")
    Optional<OpUser> getOpUserByUsername(String username);

    @Modifying
    @Query(value = "update OpUser set isDeleted=1 where id=?1")
    void deleteById(String userId);

    @Modifying
    @Query(value = "update OpUser set state=?1 where id=?2")
    void updateUserState(Integer state, String id);

    @Modifying
    @Query(value = "update OpUser set password=?1 where id=?2")
    void updatePassword(String password, String id);
}