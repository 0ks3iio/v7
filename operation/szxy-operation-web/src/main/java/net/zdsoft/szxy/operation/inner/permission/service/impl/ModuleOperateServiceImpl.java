package net.zdsoft.szxy.operation.inner.permission.service.impl;

import net.zdsoft.szxy.operation.inner.permission.dao.ModuleOperateDao;
import net.zdsoft.szxy.operation.inner.permission.entity.ModuleOperate;
import net.zdsoft.szxy.operation.inner.permission.service.ModuleOperateService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author shenke
 * @since 2019/4/10 上午9:58
 */
@Service("moduleOperateService")
public class ModuleOperateServiceImpl implements ModuleOperateService {

    @Resource
    private ModuleOperateDao moduleOperateDao;


    @Override
    public List<ModuleOperate> getAllOperates() {
        return moduleOperateDao.findAll();
    }

    @Override
    public List<ModuleOperate> getModuleOperatesByModuleIds(String[] moduleIds) {
        if (ArrayUtils.isEmpty(moduleIds)) {
            return Collections.emptyList();
        }
        return moduleOperateDao.getModuleOperatesByModuleIds(moduleIds);
    }
}
