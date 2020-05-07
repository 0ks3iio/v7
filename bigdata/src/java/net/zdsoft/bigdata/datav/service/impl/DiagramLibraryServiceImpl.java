package net.zdsoft.bigdata.datav.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.datav.dao.DiagramLibraryRepository;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramElement;
import net.zdsoft.bigdata.datav.entity.DiagramLibrary;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.service.*;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static net.zdsoft.bigdata.datav.service.OverDiagramLibraryCollectMaxException.MAX_COLLECT;

/**
 * @author shenke
 * @since 2018/12/6 下午1:33
 */
@Service
public class DiagramLibraryServiceImpl extends BaseServiceImpl<DiagramLibrary, String> implements DiagramLibraryService {

    @Resource
    private DiagramLibraryRepository diagramLibraryRepository;
    @Resource
    private DiagramService diagramService;
    @Resource
    private DiagramElementService diagramElementService;
    @Resource
    private DiagramParameterService diagramParameterService;
    @Resource
    private BigLogService bigLogService;

    @Override
    protected BaseJpaRepositoryDao<DiagramLibrary, String> getJpaDao() {
        return diagramLibraryRepository;
    }

    @Override
    protected Class<DiagramLibrary> getEntityClass() {
        return DiagramLibrary.class;
    }

    @Override
    public List<DiagramLibrary> getCollectLibraryByUserId(String userId) {
        return diagramLibraryRepository.getDiagramLibrariesByUserId(userId);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void collectDiagram(String diagramId, String userId, String libraryName) throws OverDiagramLibraryCollectMaxException {
        if (MAX_COLLECT < diagramLibraryRepository.countByUserId(userId) + 1 ) {
            throw new OverDiagramLibraryCollectMaxException(String.format("最多可以收藏%s个组件", MAX_COLLECT));
        }
        //组件、参数、diagram
        Diagram diagram = diagramService.findOne(diagramId);
        DiagramLibrary diagramLibrary = convertDiagramToLibrary(diagram, libraryName, userId);
        List<DiagramParameter> diagramParameters = diagramParameterService.getDiagramParametersByDiagramId(diagramId);
        diagramParameters = convertParameterAttribution(diagramParameters, diagramLibrary.getId());
        collectElement(diagramId, diagramLibrary.getId());
        //
        diagramParameterService.saveAll(diagramParameters.toArray(new DiagramParameter[0]));
        diagramLibraryRepository.save(diagramLibrary);
        //业务日志埋点  新增
        LogDto logDto=new LogDto();
        logDto.setBizCode("insert-diagramLibrary");
        logDto.setDescription("收藏图表 "+diagramLibrary.getName());
        logDto.setNewData(diagramLibrary);
        logDto.setBizName("数据大屏");
        bigLogService.insertLog(logDto);

    }

    private void collectElement(String diagramId, String newLibraryId) {
        List<DiagramElement> elements = diagramElementService.getElementsByDiagramId(diagramId);
        List<DiagramElement> ghostElements = new ArrayList<>(elements.size());
        if (CollectionUtils.isNotEmpty(elements)) {
            List<DiagramParameter> elementParameters = new ArrayList<>(32 * elements.size());
            for (DiagramElement element : elements) {
                String newElementId = UuidUtils.generateUuid();
                List<DiagramParameter> parameters = diagramParameterService.getDiagramParametersByDiagramId(element.getId());
                parameters = convertParameterAttribution(parameters, newElementId);
                elementParameters.addAll(parameters);

                DiagramElement ghostElement = element.clone();

                ghostElement.setId(newElementId);
                ghostElement.setDiagramId(newLibraryId);
                ghostElements.add(ghostElement);

            }
            diagramParameterService.saveAll(elementParameters.toArray(new DiagramParameter[0]));
            diagramElementService.saveAll(ghostElements.toArray(new DiagramElement[0]));
        }
    }

    private List<DiagramParameter> convertParameterAttribution(List<DiagramParameter> diagramParameters, String diagramId) {
        List<DiagramParameter> clones = new ArrayList<>(diagramParameters.size());
        for (DiagramParameter diagramParameter : diagramParameters) {
            DiagramParameter p = diagramParameter.clone();
            p.setId(UuidUtils.generateUuid());
            p.setDiagramId(diagramId);
            clones.add(p);
        }
        return clones;
    }


    private DiagramLibrary convertDiagramToLibrary(Diagram diagram, String name, String userId) {
        DiagramLibrary diagramLibrary = new DiagramLibrary();
        diagramLibrary.setName(name);
        diagramLibrary.setUserId(userId);
        diagramLibrary.setDatasourceId(diagram.getDatasourceId());
        diagramLibrary.setDatasourceType(diagram.getDatasourceType());
        diagramLibrary.setDatasourceValueJson(diagram.getDatasourceValueJson());
        diagramLibrary.setDatasourceValueSql(diagram.getDatasourceValueSql());
        diagramLibrary.setUpdateInterval(diagram.getUpdateInterval());
        diagramLibrary.setDiagramType(diagram.getDiagramType());
        diagramLibrary.setId(UuidUtils.generateUuid());
        return diagramLibrary;
    }

    @Override
    public void updateCollectLibraryName(String libraryId, String name) {
        DiagramLibrary oldDiagramLibrary = diagramLibraryRepository.findById(libraryId).get();
        diagramLibraryRepository.updateLibraryName(name, libraryId);
        //业务日志埋点  修改
        LogDto logDto=new LogDto();
        logDto.setBizCode("update-diagramLibraryName");
        logDto.setDescription("收藏图表名称为 "+name);
        logDto.setOldData(oldDiagramLibrary);
        DiagramLibrary newDiagramLibrary = diagramLibraryRepository.findById(libraryId).get();
        logDto.setNewData(newDiagramLibrary);
        logDto.setBizName("数据大屏");
        bigLogService.updateLog(logDto);
    }

    @Override
    public void deleteCollect(String libraryId) {
        DiagramLibrary oldDiagramLibrary = diagramLibraryRepository.findById(libraryId).get();
        delete(libraryId);
        //delete
        diagramElementService.deleteByDiagramId(libraryId);
        diagramParameterService.deleteByDiagramId(libraryId);
        //业务日志埋点  删除
        LogDto logDto=new LogDto();
        logDto.setBizCode("delete-diagramLibrary");
        logDto.setDescription("收藏图表 "+oldDiagramLibrary.getName());
        logDto.setBizName("数据大屏");
        logDto.setOldData(oldDiagramLibrary);
        bigLogService.deleteLog(logDto);
    }

    @Override
    public String add2Screen(String libraryId, String screenId) throws DiagramLibraryDeletedException {
        DiagramLibrary library = findOne(libraryId);
        if (library == null) {
            throw new DiagramLibraryDeletedException("该收藏已被删除，请刷新页面");
        }
        Diagram diagram = convertLibaryToDiagram(library, screenId);

        List<DiagramParameter> diagramParameters = diagramParameterService.getDiagramParametersByDiagramId(libraryId);
        diagramParameters = convertParameterAttribution(diagramParameters, diagram.getId());
        collectElement(libraryId, diagram.getId());
        diagramParameterService.saveAll(diagramParameters.toArray(new DiagramParameter[0]));
        diagramService.save(diagram);
        return diagram.getId();
    }

    private Diagram convertLibaryToDiagram(DiagramLibrary diagramLibrary, String screenId) {
        Diagram diagram = new Diagram();
        diagram.setDatasourceId(diagramLibrary.getDatasourceId());
        diagram.setDatasourceType(diagramLibrary.getDatasourceType());
        diagram.setDatasourceValueJson(diagramLibrary.getDatasourceValueJson());
        diagram.setDatasourceValueSql(diagramLibrary.getDatasourceValueSql());
        diagram.setUpdateInterval(diagramLibrary.getUpdateInterval());
        diagram.setDiagramType(diagramLibrary.getDiagramType());
        diagram.setId(UuidUtils.generateUuid());
        diagram.setScreenId(screenId);
        return diagram;
    }
}
