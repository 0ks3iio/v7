package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.service.DiagramParameterGroupService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shenke
 * @since 2018/9/27 17:09
 */
@Component
@Lazy(false)
public class NeedRenderChecker implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static DiagramParameterGroupService diagramParameterGroupService;
    private static Map<String, Boolean> cache;

    @PostConstruct
    public void initDiagramGroupService() {
        diagramParameterGroupService = applicationContext.getBean("diagramParameterGroupService", DiagramParameterGroupService.class);
        cache = new HashMap<>(16);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        NeedRenderChecker.applicationContext = applicationContext;
    }

    public static boolean contain(GroupKey key, Integer diagramType) {
        //return diagramParameterGroupService.getGroupsByDiagramType(diagramType)
        //        .stream().anyMatch(dg->key.name().equals(dg.getGroupKey()));
        return cache.computeIfAbsent(key.name() + diagramType, k-> {
            return diagramParameterGroupService.getGroupsByDiagramType(diagramType)
                    .stream().anyMatch(dg->key.name().equals(dg.getGroupKey()));
        });
    }
}
