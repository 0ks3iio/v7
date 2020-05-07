package net.zdsoft.studevelop.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StudevelopPermission;

import java.util.List;

/**
 * Created by Administrator on 2018/10/15.
 */
public interface StudevelopPermissionService extends BaseService<StudevelopPermission,String> {

    public List<StudevelopPermission> getStuDevelopPermissionList(String unitId , String permissionType);
}
