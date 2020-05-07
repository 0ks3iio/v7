package net.zdsoft.system.dao;

import net.zdsoft.system.entity.server.ServerClassify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2019/3/13 上午11:12
 */
@Repository
public interface ServerClassifyDao extends JpaRepository<ServerClassify, String> {

    /**
     * 获取指定单位的系统分类标准
     * @param unitId 单位ID
     * @return
     */
    List<ServerClassify> getClassifyByUnitId(String unitId);

}
