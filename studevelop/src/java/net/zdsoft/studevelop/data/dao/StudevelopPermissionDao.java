package net.zdsoft.studevelop.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StudevelopPermission;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2018/10/15.
 */
public interface StudevelopPermissionDao extends BaseJpaRepositoryDao<StudevelopPermission,String> {

    @Query("From StudevelopPermission where unitId=?1 and permissionType=?2 ")
    public List<StudevelopPermission> getStuDevelopPermissionList(String unitId , String permissionType);
    
    @Query("From StudevelopPermission where unitId=?1  ")
    public List<StudevelopPermission> getStuDevelopList(String unitId);
}
