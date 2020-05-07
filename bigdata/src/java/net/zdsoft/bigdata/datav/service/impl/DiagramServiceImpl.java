package net.zdsoft.bigdata.datav.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.datav.DataVStringUtil;
import net.zdsoft.bigdata.datav.dao.DiagramRepository;
import net.zdsoft.bigdata.datav.dto.SimpleDiagram;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramContrast;
import net.zdsoft.bigdata.datav.service.*;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shenke
 * @since 2018/9/26 16:09
 */
@Service
public class DiagramServiceImpl extends BaseServiceImpl<Diagram, String> implements DiagramService {

    @Resource
    private DiagramRepository diagramRepository;
    @Resource
    private DiagramParameterService diagramParameterService;
    @Resource
    private DiagramContrastService diagramContrastService;
    @Resource
    private DiagramElementService diagramElementService;
    @Resource
    private InteractionActiveService interactionActiveService;
    @Resource
    private InteractionBindingService interactionBindingService;
    @Resource
    private BigLogService bigLogService;

    @Override
    protected BaseJpaRepositoryDao<Diagram, String> getJpaDao() {
        return diagramRepository;
    }

    @Override
    protected Class<Diagram> getEntityClass() {
        return Diagram.class;
    }

    @Override
    public void deleteByScreenIdAnId(String screenId, String id) {
        Diagram diagram = diagramRepository.findById(id).get();
        DiagramContrast diagramContrast = diagramContrastService.getDiagramContrastByDiagramType(diagram.getDiagramType());
        //业务日志埋点  删除
        LogDto logDto=new LogDto();
        logDto.setBizCode("delete-diagram");
        logDto.setDescription("大屏图表类型为"+diagramContrast.getName()+"的图表");
        logDto.setBizName("数据大屏");
        logDto.setOldData(diagram);

        bigLogService.deleteLog(logDto);
        diagramRepository.deleteByScreenIdAndId(screenId, id);
        diagramParameterService.deleteByDiagramId(id);
        diagramElementService.deleteByDiagramId(id);
        //for delete screen interaction element
        interactionActiveService.cancelActive(screenId, id);
        interactionBindingService.deleteByElementId(id);
        interactionActiveService.deleteByElementIds(new String[]{id});

    }

    @Override
    public void deleteByScreenId(String screenId) {
        diagramRepository.deleteAllByScreenId(screenId);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public Diagram createEmpty(String screenId, Integer diagramType) {
        Diagram diagram = new Diagram();
        diagram.setScreenId(screenId);
        diagram.setDatasourceType(DataSourceType.STATIC.getValue());
        diagram.setDiagramType(diagramType);
        diagram.setDatasourceValueJson(DataVStringUtil.compressOfBlank(diagramContrastService.getDiagramContrastByDiagramType(diagramType).getDemoData()));
        diagram.setId(UuidUtils.generateUuid());
        save(diagram);
        //业务日志埋点  新增
        LogDto logDto=new LogDto();
        logDto.setBizCode("insert-diagram");
        DiagramContrast diagramContrast = diagramContrastService.getDiagramContrastByDiagramType(diagramType);
        logDto.setDescription("大屏图表类型为"+diagramContrast.getName()+"的图表");
        logDto.setNewData(diagram);
        logDto.setBizName("数据大屏");
        bigLogService.insertLog(logDto);

        return diagram;
    }

    @Override
    public List<Diagram> getDiagramsByScreenId(String screenId) {
        return diagramRepository.getDiagramsByScreenId(screenId);
    }

    @Override
    public List<String> getIdsByScreenId(String screenId) {
        return diagramRepository.getIdsByScreenId(screenId);
    }

    @Override
    public List<SimpleDiagram> getSimpleDiagramByScreenId(String screenId) {
        return diagramRepository.getSimpleDiagramByScreenId(screenId);
    }

    @Override
    public void lock(String[] diagramId, boolean lock) {
        diagramRepository.lock(diagramId, lock ? 1 : 0);
    }
}
