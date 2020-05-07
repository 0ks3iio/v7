package net.zdsoft.bigdata.datav.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.OrderType;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.datav.ShotPath;
import net.zdsoft.bigdata.datav.dao.ScreenRepository;
import net.zdsoft.bigdata.datav.entity.*;
import net.zdsoft.bigdata.datav.service.*;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author shenke
 * @since 2018/9/26 15:52
 */
@Service
public class ScreenServiceImpl extends BaseServiceImpl<Screen, String> implements ScreenService {

    @Resource
    private ScreenRepository screenRepository;
    @Resource
    private DiagramElementService diagramElementService;
    @Resource
    private DiagramService diagramService;
    @Resource
    private DiagramParameterService diagramParameterService;
    @Resource
    private ScreenStyleService screenStyleService;
    @Resource
    private ScreenLibraryService screenLibraryService;
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Resource
    private ScreenInteractionElementService screenInteractionElementService;
    @Resource
    private InteractionActiveService interactionActiveService;
    @Resource
    private InteractionBindingService interactionBindingService;
    @Resource
    private BigLogService bigLogService;

    @Override
    protected BaseJpaRepositoryDao<Screen, String> getJpaDao() {
        return screenRepository;
    }

    @Override
    protected Class<Screen> getEntityClass() {
        return Screen.class;
    }

    @Override
    public long countAll() {
        return screenRepository.countAll();
    }

    @Override
    public long countByTime(Date start, Date end) {
        return screenRepository.count(new Specification<Screen>() {
            @Override
            public Predicate toPredicate(Root<Screen> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.between(root.get("creationTime").as(Timestamp.class), start, end)).getRestriction();
            }
        });
    }

    @Override
    public void updateName(String id, String name) {
        screenRepository.updateName(name, id);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void batchDelete(String[] ids) {
        Assert.notNull(ids);
        //删除diagram
        for (String id : ids) {
            List<String> diagramIds = diagramService.getIdsByScreenId(id);
            for (String diagramId : diagramIds) {
                diagramElementService.deleteByDiagramId(diagramId);
                diagramParameterService.deleteByDiagramId(diagramId);
            }
            diagramService.deleteByScreenId(id);
            if (!diagramIds.isEmpty()) {
                interactionActiveService.deleteByElementIds(diagramIds.toArray(new String[0]));
                interactionBindingService.deleteByElementIds(diagramIds.toArray(new String[0]));
            }
            screenInteractionElementService.deleteByScreenId(id);

        }
        Screen screen = screenRepository.findById(ids[0]).get();
        screenRepository.batchDelete(ids);
        screenStyleService.bacthDelete(ids);
        //业务日志埋点  删除
        LogDto logDto=new LogDto();
        logDto.setBizCode("delete-screen");
        logDto.setDescription("大屏 "+screen.getName());
        logDto.setBizName("数据大屏");
        logDto.setOldData(screen);
        bigLogService.deleteLog(logDto);

    }

    @Override
    public void updateOrderType(String id, Integer orderType) {
        screenRepository.updateOrderType(id, orderType);
    }

    @Override
    public List<Screen> getScreensByUnitId(String unitId, String userId) {
        return screenRepository.getScreensForEdit(userId);
    }

    @Override
    public List<Screen> getScreensByUnitIdAndUserId(String unitId, String userId) {
        return screenRepository.getScreensForQuery(unitId, userId);
    }

    @Override
    public List<Screen> getScreensForSuper(String userId) {
        return screenRepository.getScreensForSuper(userId);
    }

    @Override
    public List<Screen> getScreensByUnitIdAndUserIdForGroup(String unitId, String userId, String[] screenIds) {
        return screenRepository.getScreensForQuery(unitId, userId, screenIds);
    }

    @Transactional(rollbackFor = Throwable.class, transactionManager = "txManagerJap")
    @Override
    public void cloneScreen(String id, String name, String userId) {
        screenRepository.findById(id).ifPresent(screen -> {
            String newScreenId = UuidUtils.generateUuid();
            if (StringUtils.isBlank(screen.getUrl())) {
                cloneDiagramAndElementAndParametersAndStyle(id, newScreenId);
            }
            screen.setName(name);
            screen.setId(newScreenId);
            screen.setCreateUserId(userId);
            screenRepository.save(screen);
            //copy
            String filePath = sysOptionRemoteService.findValue(Constant.FILE_PATH);
            try {
                ShotPath.copy(filePath, screen.getUnitId(), id, newScreenId);
            } catch (IOException e) {
                //ignore
            }
        });
    }

    private void cloneDiagramAndElementAndParametersAndStyle(String id, String newScreenId) {
        List<Diagram> diagrams = diagramService.getDiagramsByScreenId(id);

        Map<String, String> diagramIdMap = new HashMap<>(diagrams.size());
        List<DiagramElement> diagramElements = new ArrayList<>();
        List<DiagramParameter> diagramParameters = new ArrayList<>();
        for (Diagram diagram : diagrams) {
            diagram.setScreenId(newScreenId);
            String newId = UuidUtils.generateUuid();
            diagramIdMap.put(diagram.getId(), newId);
            List<DiagramElement> elements = diagramElementService.getElementsByDiagramId(diagram.getId());
            if (!elements.isEmpty()) {
                elements.forEach(element -> element.setDiagramId(newId));
                diagramElements.addAll(elements);
            }
            List<DiagramParameter> parameters = diagramParameterService.getDiagramParametersByDiagramId(diagram.getId());
            if (!parameters.isEmpty()) {
                for (DiagramParameter p : parameters) {
                    p.setDiagramId(newId);
                    p.setId(UuidUtils.generateUuid());
                }
                diagramParameters.addAll(parameters);
            }
            diagram.setId(newId);
        }
        doCloneInteractive(id, newScreenId, diagramIdMap);

        // clone style
        ScreenStyle screenStyle = screenStyleService.getByScreenId(id);
        for (DiagramElement diagramElement : diagramElements) {
            List<DiagramParameter> parameters = diagramParameterService.getDiagramParametersByDiagramId(diagramElement.getId());
            diagramElement.setId(UuidUtils.generateUuid());
            if (!parameters.isEmpty()) {
                for (DiagramParameter p : parameters) {
                    p.setDiagramId(diagramElement.getId());
                    p.setId(UuidUtils.generateUuid());
                }
                diagramParameters.addAll(parameters);
            }
        }

        if (screenStyle != null) {
            screenStyle.setId(UuidUtils.generateUuid());
            screenStyle.setScreenId(newScreenId);

            screenStyleService.save(screenStyle);
        }
        diagramService.saveAll(diagrams.toArray(new Diagram[0]));
        diagramElementService.saveAll(diagramElements.toArray(new DiagramElement[0]));
        diagramParameterService.saveAll(diagramParameters.toArray(new DiagramParameter[0]));
    }

    /**
     * 复制交互参数
     */
    private void doCloneInteractive(String screenId, String newScreenId, Map<String, String> oldNewMap) {
        List<InteractionBinding> interactionBindings
                = interactionBindingService.getBindingsByScreenId(screenId);
        List<String> activeBinds = new ArrayList<>(interactionBindings.size());
        for (InteractionBinding interactionBinding : interactionBindings) {
            interactionBinding.setElementId(oldNewMap.get(interactionBinding.getElementId()));
            interactionBinding.setId(UuidUtils.generateUuid());
            activeBinds.add(interactionBinding.getElementId());
        }
        List<ScreenInteractionElement> screenInteractionElements
                = screenInteractionElementService.getScreenDefaultInteractionItems(screenId);
        for (ScreenInteractionElement screenInteractionElement : screenInteractionElements) {
            screenInteractionElement.setScreenId(newScreenId);
            screenInteractionElement.setId(UuidUtils.generateUuid());
        }
        interactionActiveService.saveAll(
                activeBinds.stream().map(id -> {
                    InteractionActive active = new InteractionActive();
                    active.setId(UuidUtils.generateUuid());
                    active.setActive(Active.enable);
                    active.setElementId(id);
                    return active;
                }).toArray(InteractionActive[]::new));
        interactionBindingService.saveAll(interactionBindings.toArray(new InteractionBinding[0]));
        screenInteractionElementService.saveAll(screenInteractionElements.toArray(new ScreenInteractionElement[0]));
    }

    @Override
    public void cloneNoTransactional(String id, String name, String userId) {
        cloneScreen(id, name, userId);
    }


    @Override
    public String createFromLibrary(String templateId, String name, String unitId, String userId) {
        ScreenLibrary screenLibrary = screenLibraryService.findOne(templateId);
        if (screenLibrary == null) {
            throw new IllegalArgumentException("该模版已被删除:" + templateId);
        }
        Screen screen = new Screen();
        screen.setName(name);
        screen.setCreateUserId(userId);
        screen.setId(UuidUtils.generateUuid());
        screen.setCreationTime(new Date());
        screen.setOrderType(OrderType.SELF.getOrderType());
        screen.setUnitId(unitId);
        save(screen);
        cloneDiagramAndElementAndParametersAndStyle(templateId, screen.getId());
        return screen.getId();
    }

    @Override
    public long countScreensForQuery(String unitId, String userId) {
        return screenRepository.countScreensForQuery(unitId, userId);
    }

}
