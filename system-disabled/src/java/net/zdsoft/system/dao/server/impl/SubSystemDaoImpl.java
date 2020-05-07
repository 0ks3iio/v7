package net.zdsoft.system.dao.server.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.dao.server.SubSystemJdbcDao;
import net.zdsoft.system.entity.server.SubSystem;

public class SubSystemDaoImpl extends BaseDao<SubSystem> implements SubSystemJdbcDao {

    @Override
    public void update(SubSystem system) {
        String sql = "update sys_subsystem set name = ?, code = ?, index_url = ?, parentid = ?, orderid = ? where id = ?";
        update(sql,
                new Object[] { system.getName(), system.getCode(), system.getUrl(), system.getParentId(),
                        system.getDisplayOrder(), system.getId() });
    }

    @Override
    public void insert(SubSystem system) {
        String sql = "insert into sys_subsystem (id, system_id, name, code, index_url, parentid, orderid, markbit, rolegroup, ISCONTROLBYPERM) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        update(sql,
                new Object[] { system.getId(), UuidUtils.generateUuid(), system.getName(), system.getCode(),
                        system.getUrl(), system.getParentId(), system.getDisplayOrder(), 0, "1,2,3,4", 1 });
    }

    @Override
    public void deleteByIntId(int intId) {
        String sql = "delete from sys_subsystem where id = ?";
        update(sql, new Object[] { intId });
    }

    @Override
    public SubSystem setField(ResultSet rs) throws SQLException {
        return null;
    }

}
