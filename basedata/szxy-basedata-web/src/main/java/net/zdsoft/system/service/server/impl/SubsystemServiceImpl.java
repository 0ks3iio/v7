package net.zdsoft.system.service.server.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Specifications;
import net.zdsoft.system.dao.server.SubSystemDao;
import net.zdsoft.system.entity.server.SubSystem;
import net.zdsoft.system.service.server.SubsystemService;

@Service("subsystemService")
public class SubsystemServiceImpl extends BaseServiceImpl<SubSystem, Integer> implements SubsystemService {

    @Autowired
    private SubSystemDao subSystemDao;

    @Override
    public int findMaxDisplayOrder() {
        return subSystemDao.findMaxDisplayOrder();
    }

    @Override
    public SubSystem findById(Integer id) {
        Specifications<SubSystem> sp = new Specifications<>();
        sp.addEq("id", id);
        return subSystemDao.findOne(sp.getSpecification()).orElse(null);
    }

    @Override
    protected BaseJpaRepositoryDao<SubSystem, Integer> getJpaDao() {
        return subSystemDao;
    }

    @Override
    protected Class<SubSystem> getEntityClass() {
        return SubSystem.class;
    }

    @Override
    public void deleteById(Integer intId) {
        subSystemDao.deleteByIntId(intId);
    }

    @Override
    public void update(SubSystem system) {
        List<SubSystem> systems = findListByIn("name", new String[] { system.getName() });
        for (SubSystem ss : systems) {
            if (ss.getId() != system.getId()) {
                throw new RuntimeException("应用名称与其他应用重复，保存失败！");
            }
        }
        systems = findListByIn("code", new String[] { system.getCode() });
        for (SubSystem ss : systems) {
            if (ss.getId() != system.getId()) {
                throw new RuntimeException("应用编号与其他应用重复，保存失败！");
            }
        }
        subSystemDao.update(system);

    }

    @Override
    public void insert(SubSystem system) {
        int maxIntId = subSystemDao.findMaxIntId() + 1;
        system.setId(maxIntId);
        SubSystem ss = findById(system.getId());
        if (ss == null) {
            List<SubSystem> sss = findListByIn("code", new String[] { system.getCode() });
            if (CollectionUtils.isNotEmpty(sss)) {
                throw new RuntimeException("编号" + system.getId() + "已经被【" + sss.get(0).getName() + "】使用，保存失败！");
            }
            sss = findListByIn("name", new String[] { system.getName() });
            if (CollectionUtils.isNotEmpty(sss)) {
                throw new RuntimeException("名称已经被使用，保存失败！");
            }

            subSystemDao.insert(system);
        }
        else {
            insert(system);
        }
    }
}
