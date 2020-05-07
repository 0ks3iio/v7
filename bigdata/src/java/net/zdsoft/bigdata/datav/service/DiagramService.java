package net.zdsoft.bigdata.datav.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datav.dto.SimpleDiagram;
import net.zdsoft.bigdata.datav.entity.Diagram;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/26 16:09
 */
public interface DiagramService extends BaseService<Diagram, String> {

    /**
     * 删除某一个大屏上的某个图表
     * 同时会删除和该图表相关的所有配置、授权等相关数据
     * @param screenId 大屏ID
     * @param id 图表ID
     */
    void deleteByScreenIdAnId(String screenId, String id);

    void deleteByScreenId(String screenId);

    /**
     * 创建一个空的diagram，随之确定默认参数
     * @param screenId
     * @param diagramType
     * @return Diagram
     */
    Diagram createEmpty(String screenId, Integer diagramType);

    /**
     * 获取某一个大屏的全部diagram
     * @param screenId
     * @return
     */
    List<Diagram> getDiagramsByScreenId(String screenId);

    List<String> getIdsByScreenId(String screenId);

    List<SimpleDiagram> getSimpleDiagramByScreenId(String screenId);

    @Transactional(rollbackFor = Throwable.class)
    void lock(String[] diagramId, boolean lock);
}
