package net.zdsoft.bigdata.datav.dao;

import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/26 17:00
 */
@Repository
public interface DiagramParameterRepository extends BaseJpaRepositoryDao<DiagramParameter, String>, JpaSpecificationExecutor<DiagramParameter> {


    List<DiagramParameter> getDiagramParametersByDiagramId(String diagramId);

    @Modifying
    @Query(
            value = "update DiagramParameter set value=?2 where key=?3 and diagramId=?1"
    )
    void updateDiagramParameterValueByIdAndKey(String diagramId, String value, String key);

    @Modifying
    @Query(
            value = "update DiagramParameter  set value=?2 where key=?3 and diagramId=?1 and arrayName=?4"
    )
    void updateDiagramParameterValueByIdAndKeyAndArrayName(String diagramId, String value, String key, String arrayName);

    boolean existsByDiagramIdAndKey(String diagramId, String key);

    void deleteByDiagramId(String diagramId);

    void deleteByDiagramIdAndArrayName(String diagramId, String arrayName);

    @Query(
            value = "from DiagramParameter where diagramId=?1 and groupKey = 'basic'"
    )
    List<DiagramParameter> getBaiscParameters(String diagramId);

    @Query(
            value = "select max(to_number(value)) from bg_diagram_parameter where key='level' and " +
                    "diagram_Id in (?1)",
            nativeQuery = true
    )
    Integer getMaxLevel(String[] ids);

    @Query(value = "select * from bg_diagram_parameter where diagram_id=?1 and key=?2 and array_name=?3",nativeQuery = true)
    DiagramParameter findByDiagramIdAndKeyAndArrayName(String diagramId, String key,String arrayName);

    @Query(value = "select * from bg_diagram_parameter where diagram_id=?1 and key=?2",nativeQuery = true)
    DiagramParameter findByDiagramIdAndKey(String diagramId, String key);

    //List<DiagramParameter> getLevelParameters(String screenId);
}
