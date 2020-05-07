package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.dto.UpdateUnit;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.utils.AssertUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author shenke
 * @since 2019/3/21 下午12:39
 */
public class UnitDaoImpl {

    @PersistenceContext
    private EntityManager entityManager;

    @Record(type = RecordType.SQL)
    public void updateUnit(UpdateUnit updateUnit) {
        AssertUtils.notNull(updateUnit, "UpdateUnit can't null");
        AssertUtils.notNull(updateUnit.getUnitId(), "UpdateUnit#unitId can't null");

        StringBuilder sql = new StringBuilder();
        Map<Integer, Object> parameters = new HashMap<>(8);
        int index = 0;
        sql.append("update Unit set");
        if (Objects.nonNull(updateUnit.getUnitName())) {
            sql.append(" unitName=?").append(++index).append(",");
            parameters.put(index, updateUnit.getUnitName());
        }
        if (Objects.nonNull(updateUnit.getParentUnitId())) {
            sql.append(" parentId=?").append(++index).append(",");
            parameters.put(index, updateUnit.getParentUnitId());
        }
        if (Objects.nonNull(updateUnit.getRegionCode())) {
            sql.append(" regionCode=?").append(++index).append(",");
            parameters.put(index, updateUnit.getRegionCode());
        }

        if (index > 0) {
            sql.append(" modifyTime=?").append(++index);
            parameters.put(index, new Date());
            sql.append(" where id=?").append(++index);
            parameters.put(index, updateUnit.getUnitId());
            Query query = entityManager.createQuery(sql.toString());
            for (Map.Entry<Integer, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            query.executeUpdate();
        }
    }
}
