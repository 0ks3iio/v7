package net.zdsoft.bigdata.datav.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.datav.dao.InteractionActiveRepository;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.InteractionActive;
import net.zdsoft.bigdata.datav.entity.InteractionBinding;
import net.zdsoft.bigdata.datav.entity.InteractionItem;
import net.zdsoft.bigdata.datav.entity.ScreenInteractionElement;
import net.zdsoft.bigdata.datav.entity.Active;
import net.zdsoft.bigdata.datav.service.DiagramService;
import net.zdsoft.bigdata.datav.service.InteractionActiveService;
import net.zdsoft.bigdata.datav.service.InteractionBindingService;
import net.zdsoft.bigdata.datav.service.InteractionItemService;
import net.zdsoft.bigdata.datav.service.ScreenInteractionElementService;
import net.zdsoft.bigdata.datav.service.ScreenService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2018/10/26 上午11:16
 */
@Service("interactionActiveService")
public class InteractionActiveServiceImpl extends BaseServiceImpl<InteractionActive, String> implements InteractionActiveService {
    @Resource
    private InteractionActiveRepository interactionActiveRepository;
    @Resource
    private DiagramService diagramService;
    @Resource
    private InteractionBindingService interactionBindingService;
    @Resource
    private InteractionItemService interactionItemService;
    @Resource
    private ScreenInteractionElementService screenInteractionElementService;

    @Override
    protected BaseJpaRepositoryDao<InteractionActive, String> getJpaDao() {
        return interactionActiveRepository;
    }

    @Override
    protected Class<InteractionActive> getEntityClass() {
        return InteractionActive.class;
    }

    @Override
    public Active isActice(String elementId) {
        return Optional.ofNullable(interactionActiveRepository.isActive(elementId)).orElse(Active.disable);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void activeForElement(String elementId) {
        Diagram diagram = diagramService.findOne(elementId);
        //设置状态
        InteractionActive interactionActive = new InteractionActive();
        interactionActive.setActive(Active.enable);
        interactionActive.setElementId(elementId);
        interactionActive.setId(UuidUtils.generateUuid());
        save(interactionActive);

        //设置binding
        List<InteractionItem> items = interactionItemService.getInteractionItemsByDiagramType(diagram.getDiagramType());
        List<InteractionBinding> bindings = new ArrayList<>(items.size());
        List<ScreenInteractionElement> elements = new ArrayList<>(items.size());
        for (InteractionItem item : items) {
            ScreenInteractionElement element =
                    screenInteractionElementService.getByBindKeyAndScreenId(item.getBindKey(), diagram.getScreenId());
            InteractionBinding binding = new InteractionBinding();
            binding.setBindKey(item.getBindKey());
            binding.setKey(item.getKey());
            binding.setElementId(elementId);
            binding.setId(UuidUtils.generateUuid());
            bindings.add(binding);

            if (element == null) {
                element = new ScreenInteractionElement();
                element.setId(UuidUtils.generateUuid());
                element.setScreenId(diagram.getScreenId());
                element.setBindKey(item.getBindKey());
            }
            elements.add(element);
        }

        interactionBindingService.saveAll(bindings.toArray(new InteractionBinding[0]));

        //设置大屏默认参数的值
        screenInteractionElementService.saveAll(elements.toArray(new ScreenInteractionElement[0]));
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void cancelActive(String screenId, String elementId) {

        List<InteractionBinding> interactionBindings = interactionBindingService.getBindingsByScreenId(screenId);
        Set<String> keepBindKeys = interactionBindings.stream().filter(e->!elementId.equals(e.getElementId()))
                .map(InteractionBinding::getBindKey).collect(Collectors.toSet());

        for (InteractionBinding binding : interactionBindingService.getBindingsByElementId(elementId)) {
            if (!keepBindKeys.contains(binding.getBindKey())) {
                screenInteractionElementService.deleteByScreenIdAndBindKey(screenId, binding.getBindKey());
            }
        }
        //取消激活状态
        interactionActiveRepository.deleteByElementId(elementId);
        //删除bind
        interactionBindingService.deleteByElementId(elementId);
    }

    @Override
    public void deleteByElementIds(String[] elementIds) {
        interactionActiveRepository.deleteByElementIdIn(elementIds);
    }

    @Override
    public List<InteractionActive> getActivitiesByScreenId(String screenId) {
        return findListBy("screenId", screenId);
    }
}
