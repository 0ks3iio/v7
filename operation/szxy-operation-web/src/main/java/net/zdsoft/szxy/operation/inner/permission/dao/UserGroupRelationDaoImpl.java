package net.zdsoft.szxy.operation.inner.permission.dao;

import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.inner.permission.dto.GroupUserCounter;
import net.zdsoft.szxy.operation.inner.permission.entity.UserGroupRelation;
import org.apache.commons.lang3.ArrayUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;

/**
 * @author shenke
 * @since 2019/4/10 下午3:00
 */
public class UserGroupRelationDaoImpl {

    @PersistenceContext
    private EntityManager entityManager;

    @Record(type = RecordType.SQL)
    public List<GroupUserCounter> getGroupUserCounters(String[] groupIds) {
        if (ArrayUtils.isEmpty(groupIds)) {
            return Collections.emptyList();
        }
        CriteriaQuery<GroupUserCounter> c = entityManager.getCriteriaBuilder().createQuery(GroupUserCounter.class);
        Root root = c.from(UserGroupRelation.class);

        c.multiselect(root.get("groupId"), entityManager.getCriteriaBuilder().count(root.get("groupId")).alias("count"))
                .where(root.get("groupId").in(groupIds)).groupBy(root.get("groupId"));
        return entityManager.createQuery(c).getResultList();
    }
}
