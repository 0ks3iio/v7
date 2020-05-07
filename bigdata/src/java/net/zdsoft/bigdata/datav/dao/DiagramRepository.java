package net.zdsoft.bigdata.datav.dao;

import net.zdsoft.bigdata.datav.dto.SimpleDiagram;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/26 16:10
 */
@Repository
public interface DiagramRepository extends BaseJpaRepositoryDao<Diagram, String> {

    void deleteByScreenIdAndId(String screenId, String id);

    void deleteAllByScreenId(String screenId);

    List<Diagram> getDiagramsByScreenId(String screenId);

    @Query(
            value = "select id from Diagram where screenId=?1"
    )
    List<String> getIdsByScreenId(String screenId);

    List<SimpleDiagram> getSimpleDiagramByScreenId(String screenId);

    @Modifying
    @Query(value = "update Diagram set lock=?2 where id in (?1)")
    void lock(String[] ids, Integer lock);
}
