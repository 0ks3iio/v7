package net.zdsoft.system.dao;

import net.zdsoft.system.entity.server.ServerClassifyRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author shenke
 * @since 2019/3/13 上午11:15
 */
@Repository
public interface ServerClassifyRelationDao extends JpaRepository<ServerClassifyRelation, String> {

    /**
     * 获取指定分类的子系统code的集合
     * @param classifyId
     * @return
     */
    @Query(
            value = "select serverCode from ServerClassifyRelation where classifyId=?1"
    )
    Set<String> getServerCodesByClassifyId(String classifyId);

    List<ServerClassifyRelation> getServerClassifyRelationsByClassifyIdIn(String[] classifyIds);
}
