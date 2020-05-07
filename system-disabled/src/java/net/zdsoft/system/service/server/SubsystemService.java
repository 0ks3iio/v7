package net.zdsoft.system.service.server;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.server.SubSystem;

public interface SubsystemService extends BaseService<SubSystem, Integer> {

    public SubSystem findById(Integer id);

    public void deleteById(Integer intId);

    public void update(SubSystem system);

    public void insert(SubSystem system);

    public int findMaxDisplayOrder();
}
