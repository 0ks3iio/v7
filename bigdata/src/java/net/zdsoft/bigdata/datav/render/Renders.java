package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.interaction.InteractionRender;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author shenke
 * @since 2018/9/27 17:16
 */
@Component
public class Renders {

    private List<Render> renders;
    @Resource
    private InteractionRender interactionRender;

    @PostConstruct
    public void init() {
        renders = new ArrayList<>();
        ServiceLoader<Render> loader = ServiceLoader.load(Render.class);
        loader.iterator().forEachRemaining(renders::add);
        renders.add(interactionRender);
    }

    public void render(Object renderOption, List<DiagramParameter> parameters, Diagram diagram) {
        boolean echartsRenderOption = renderOption instanceof EChartsRenderOption;
        for (Render render : renders) {
            if (echartsRenderOption && render.supportECharts()) {
                render.doRender(renderOption, parameters, diagram);
            }
            else if (!echartsRenderOption && render.supportOthers()) {
                render.doRender(renderOption, parameters, diagram);
            }
        }
    }
}
