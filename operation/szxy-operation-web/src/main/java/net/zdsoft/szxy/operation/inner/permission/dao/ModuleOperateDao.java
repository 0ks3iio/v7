package net.zdsoft.szxy.operation.inner.permission.dao;

import net.zdsoft.szxy.operation.inner.permission.entity.ModuleOperate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2019/4/2 下午6:24
 */
@Repository
public interface ModuleOperateDao extends JpaRepository<ModuleOperate, String> {

    @Query(value = "from ModuleOperate where id in (?1)")
    List<ModuleOperate> getModuleOperatesByIds(String[] ids);

    @Query(value = "from ModuleOperate where moduleId in (?1)")
    List<ModuleOperate> getModuleOperatesByModuleIds(String[] moduleIds);
}
