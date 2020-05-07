package net.zdsoft.studevelop.studevelop.remote.service.impl;

import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.studevelop.data.entity.StudevelopPermission;
import net.zdsoft.studevelop.data.service.StudevelopPermissionService;
import net.zdsoft.studevelop.studevelop.remote.service.StudevelopRemoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by luf on 2018/11/16.
 */
@Service("studevelopRemoteService")
public class StudevelopRemoteServiceImpl implements StudevelopRemoteService {

    @Autowired
    private StudevelopPermissionService studevelopPermissionService;
    @Override
    public String getPermissiontListByUnitId(String permissionType, String unitId) {

        List<StudevelopPermission> list = studevelopPermissionService.getStuDevelopPermissionList(unitId,permissionType);

        return SUtils.s(list);

    }
}
