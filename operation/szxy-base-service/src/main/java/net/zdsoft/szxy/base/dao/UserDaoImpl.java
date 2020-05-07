package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.dto.UserUpdater;
import net.zdsoft.szxy.base.entity.Family;
import net.zdsoft.szxy.base.entity.User;
import net.zdsoft.szxy.base.model.QUser;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.utils.AssertUtils;
import org.hibernate.query.criteria.internal.CriteriaUpdateImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import java.util.Date;
import java.util.Objects;

/**
 * @author shenke
 * @since 2019/4/15 下午2:32
 */
public class UserDaoImpl {

    @PersistenceContext
    private EntityManager entityManager;

    @Record(type = RecordType.SQL)
    public void updateUser(UserUpdater updateUser) {
        AssertUtils.notNull(updateUser.getId(), "用户ID不能为空");
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<User> update = builder.createCriteriaUpdate(User.class);
        update.from(User.class);
        if (Objects.nonNull(updateUser.getDisplayOrder())) {
            update.set(QUser.displayOrder, updateUser.getDisplayOrder());
        }
        if (Objects.nonNull(updateUser.getUserState())) {
            update.set(QUser.userState, updateUser.getUserState());
        }
        if (Objects.nonNull(updateUser.getMobilePhone())) {
            update.set(QUser.mobilePhone, updateUser.getMobilePhone());
        }

        if (!updateUser.isIgnoreExpireTime()) {
            update.set(QUser.expireDate, updateUser.getExpireTime());
        }

        CriteriaQuery<User> userCriteriaQuery = builder.createQuery(User.class);
        
        //先校验
        ((CriteriaUpdateImpl) update).validate();

        update.set(QUser.modifyTime, new Date());
        update.where(builder.equal(update.getRoot().get(QUser.id), updateUser.getId()));
        entityManager.createQuery(update).executeUpdate();
    }
}
