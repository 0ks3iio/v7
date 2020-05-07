package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2019/3/22 下午1:41
 */
@Repository
public interface ServerDao extends JpaRepository<Server, Integer> {

    /**
     * 获取整个平台可用的子系统
     * @return List
     */
    @Query(value = "from Server where isDeleted=0 and status=1")
    List<Server> getServersForPlatform();

    /**
     * 根据subId查询系统数据
     * @param subIds subId列表
     * @return List
     */
    @Query(value = "from Server where subId in (?1) and isDeleted=0 and status=1")
    List<Server> getServersBySubId(Integer[] subIds);
}
