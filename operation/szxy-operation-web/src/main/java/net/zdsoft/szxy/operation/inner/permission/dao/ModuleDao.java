package net.zdsoft.szxy.operation.inner.permission.dao;

import net.zdsoft.szxy.operation.inner.permission.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2019/4/2 下午6:32
 */
@Repository
public interface ModuleDao extends JpaRepository<Module, String> {

    @Query("from Module where id in (?1)")
    List<Module> getModulesByIds(String[] ids);
}
