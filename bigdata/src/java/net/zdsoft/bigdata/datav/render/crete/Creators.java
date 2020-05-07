package net.zdsoft.bigdata.datav.render.crete;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.framework.utils.Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author shenke
 * @since 2018/9/27 16:44
 */
public class Creators {

    private static List<RenderOptionCreator> creators;
    private static Map<Integer, RenderOptionCreator> cache;

    static {
        creators = new ArrayList<>();
        ServiceLoader<RenderOptionCreator> loader = ServiceLoader.load(RenderOptionCreator.class);
        loader.iterator().forEachRemaining(creators::add);
        cache = new HashMap<>(creators.size());
    }

    public static Object create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException, CreateException {
        RenderOptionCreator cacheCreator = tryGetCache(diagram.getDiagramType());
        if (Objects.nonNull(cacheCreator)) {
            return create(result, diagram, earlyParameters, cacheCreator);
        }

        Object ct;
        for (RenderOptionCreator creator : creators) {
            if ((ct = create(result, diagram, earlyParameters, creator)) != null) {
                cache.put(diagram.getDiagramType(), creator);
                return ct;
            }
        }
        throw new CreateException("No Creators for " + diagram.getDiagramType());
    }

    private static Object create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters,
                                 RenderOptionCreator creator) throws EntryUtils.DataException {
        Object ct = creator.create(result, diagram, earlyParameters);
        if (ct == null) {
            return null;
        }
        creator.beforeRenderDefaultGloableTextStyleForECharts(ct, diagram.getDiagramType());
        ((RenderOption) ct).diagramType(diagram.getDiagramType());
        return ct;
    }

    private static RenderOptionCreator tryGetCache(Integer diagramType) {
        return cache.get(diagramType);
    }
}
