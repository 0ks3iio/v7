package net.zdsoft.system.dao.server;

import net.zdsoft.system.entity.server.SubSystem;

public interface SubSystemJdbcDao {

    public void deleteByIntId(int intId);

    public void update(SubSystem system);

    public void insert(SubSystem system);

}
