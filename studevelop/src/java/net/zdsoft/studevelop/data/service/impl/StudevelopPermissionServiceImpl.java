package net.zdsoft.studevelop.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.dao.StudevelopPermissionDao;
import net.zdsoft.studevelop.data.entity.StudevelopPermission;
import net.zdsoft.studevelop.data.service.StudevelopPermissionService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by luf on 2018/10/15.
 */
@Service("StudevelopPermissionService")
public class StudevelopPermissionServiceImpl extends BaseServiceImpl<StudevelopPermission,String> implements StudevelopPermissionService {
    @Autowired
    private StudevelopPermissionDao studevelopPermissionDao;

    @Override
    protected BaseJpaRepositoryDao<StudevelopPermission, String> getJpaDao() {
        return studevelopPermissionDao;
    }

    @Override
    protected Class<StudevelopPermission> getEntityClass() {
        return StudevelopPermission.class;
    }

    @Override
    public List<StudevelopPermission> getStuDevelopPermissionList(String unitId, String permissionType) {
    	if(StringUtils.isNotBlank(permissionType)){
    		return studevelopPermissionDao.getStuDevelopPermissionList(unitId,permissionType);
    	}else{
    		return studevelopPermissionDao.getStuDevelopList(unitId);
    	}
    }
}
