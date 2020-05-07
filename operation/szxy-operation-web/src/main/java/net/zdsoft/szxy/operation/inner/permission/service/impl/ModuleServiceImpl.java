package net.zdsoft.szxy.operation.inner.permission.service.impl;

import net.zdsoft.szxy.operation.inner.permission.dao.ModuleDao;
import net.zdsoft.szxy.operation.inner.permission.dao.UserModuleRelationDao;
import net.zdsoft.szxy.operation.inner.permission.entity.Module;
import net.zdsoft.szxy.operation.inner.permission.service.ModuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shenke
 * @since 2019/4/9 下午1:20
 */
@Service("moduleService")
public class ModuleServiceImpl implements ModuleService {

    @Resource
    private ModuleDao moduleDao;
    @Resource
    private UserModuleRelationDao userModuleRelationDao;

    @Override
    public List<Module> getAllModules() {
        return moduleDao.findAll();
    }

    @Override
    public List<Module> getModulesByIds(String[] ids) {
        return moduleDao.getModulesByIds(ids);
    }
}
