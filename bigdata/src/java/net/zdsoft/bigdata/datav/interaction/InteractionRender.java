package net.zdsoft.bigdata.datav.interaction;

import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.entity.Api;
import net.zdsoft.bigdata.data.service.ApiService;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.entity.Active;
import net.zdsoft.bigdata.datav.entity.InteractionBinding;
import net.zdsoft.bigdata.datav.render.Render;
import net.zdsoft.bigdata.datav.render.crete.RenderOption;
import net.zdsoft.bigdata.datav.service.InteractionActiveService;
import net.zdsoft.bigdata.datav.service.InteractionBindingService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author shenke
 * @since 2018/10/26 下午2:59
 */
@Component("interactionRender")
public class InteractionRender implements Render {

    @Resource
    private InteractionActiveService interactionActiveService;
    @Resource
    private InteractionBindingService interactionBindingService;
    @Resource
    private ApiService apiService;

    private Pattern jexlPattern = Pattern.compile("\\$\\{[\\w]+}");

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        RenderOption renderOption = (RenderOption) ro;
        if (renderOption.getInteractionAttribute() == null) {
            Active active = interactionActiveService.isActice(diagram.getId());
            InteractionAttribute attribute = new InteractionAttribute();
            attribute.setActive(Active.enable.equals(active));
            if (Active.enable.equals(active)) {
                attribute.setBindings(interactionBindingService.getBindingsByElementId(diagram.getId()));
            }

            //
            if (new Integer(DataSourceType.DB.getValue()).equals(diagram.getDatasourceType())) {
                attribute.setBeNotified(checkBeNotified(diagram.getDatasourceValueSql()));
            }
            else if (new Integer(DataSourceType.API.getValue()).equals(diagram.getDatasourceType())) {
                Api api = apiService.findOne(diagram.getDatasourceId());
                if (api == null) {
                    attribute.setBeNotified(false);
                } else {
                    attribute.setBeNotified(checkBeNotified(api.getUrl()));
                }
            }
            else {
                attribute.setBeNotified(false);
            }
            renderOption.setInteractionAttribute(attribute);
        }
    }

    private boolean checkBeNotified(String expression) {
        if (expression == null) {
            return false;
        }
        return jexlPattern.matcher(expression).find();
    }

    @Override
    public boolean supportOthers() {
        return true;
    }
}
