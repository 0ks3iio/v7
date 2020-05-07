package net.zdsoft.bigdata.datav.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/26 16:59
 */
public interface DiagramParameterService extends BaseService<DiagramParameter, String> {

    List<DiagramParameter> getDiagramParametersByDiagramId(String diagramId);

    /**
     * 获取配置信息，没有设置的将返回默认的参数信息
     * @param diagramId
     * @param diagramType
     * @return
     */
    List<DiagramParameter> getOrDefault(String diagramId, Integer diagramType);

    /**
     * 获取diagram的基本配置，位置、宽、高、背景等
     * 这些数据需要在渲染之前提前获取
     * @param diagramId
     * @return
     */
    List<DiagramParameter> getDiagramBasicById(String diagramId);


    /**
     * 更新非系列型 参数
     * @param diagramId
     * @param key
     * @param value
     */
    void updateDiagramParameter(String diagramId, String key, String value);

    /**
     * 更新系列型参数
     * @param diagramId
     * @param key
     * @param arrayName arrayname
     * @param value
     */
    void updateDiagramParameter(String diagramId, String key, String arrayName, String value);

    /**
     * 删除某个图表的所有配置
     * @param diagramId
     */
    void deleteByDiagramId(String diagramId);

    void deleteByDiagramIdAndArrayName(String diagramId, String arrayName);

    /**
     * 获取level的最大值
     * @param diagramId
     * @return
     */
    Integer getMaxLevel(String[] diagramId);

    /**
     * 获取指定大屏所有图表的层级参数
     * @param screenId 大屏ID
     * @return
     */
    List<DiagramParameter> getLevelParameters(String screenId);


    DiagramParameter findByDiagramIdAndKeyAndArrayName(String diagramId, String key,String arrayName);

    DiagramParameter findByDiagramIdAndKey(String diagramId, String key);
}
