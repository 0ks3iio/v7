package net.zdsoft.bigdata.datav.action;

import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datav.entity.InteractionBinding;
import net.zdsoft.bigdata.datav.entity.ScreenInteractionElement;
import net.zdsoft.bigdata.datav.service.InteractionActiveService;
import net.zdsoft.bigdata.datav.service.InteractionBindingService;
import net.zdsoft.bigdata.datav.service.ScreenInteractionElementService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

/**
 * @author shenke
 * @since 2019/1/15 上午9:19
 */
@Controller
@RequestMapping("/bigdata/diagram/bind-key")
public class DiagramBindKeyController extends BigdataBaseAction {

    @Autowired
    private InteractionBindingService interactionBindingService;
    @Autowired
    private InteractionActiveService interactionActiveService;
    @Autowired
    private ScreenInteractionElementService screenInteractionElementService;


    @ResponseBody
    @GetMapping("/{screenId}/{elementId}/{key}/{bindKey}")
    public Response updateBindKey(@PathVariable("screenId") String screenId,
                                  @PathVariable("elementId") String elementId,
                                  @PathVariable("key") String key,
                                  @PathVariable("bindKey") String bindKey) {
        List<InteractionBinding> bindingList = interactionBindingService.getBindingsByElementId(elementId);
        if (bindingList.isEmpty()) {
            return Response.error().message("请先启用交互").build();
        }
        Optional<InteractionBinding> binding = bindingList.stream().filter(e -> e.getKey().equals(key)).findFirst();
        InteractionBinding updateBinding = binding.orElseGet(()->{
            InteractionBinding e = new InteractionBinding();
            e.setKey(key);
            e.setElementId(elementId);
            e.setId(UuidUtils.generateUuid());
            return e;
        });
        final String oldBindKey = updateBinding.getBindKey();
        updateBinding.setBindKey(bindKey);
        interactionBindingService.save(updateBinding);

        //查找当前screen的所有交互
        boolean newSave = interactionBindingService.getBindingsByScreenId(screenId)
                .stream().filter(e->!e.getElementId().equals(elementId)).anyMatch(e->e.getBindKey().equals(oldBindKey));
        ScreenInteractionElement screenInteractionElement = null;
        if (newSave) {
            screenInteractionElement = new ScreenInteractionElement();
            screenInteractionElement.setScreenId(screenId);
            screenInteractionElement.setBindKey(bindKey);
            screenInteractionElement.setDefaultValue(null);
            screenInteractionElement.setId(UuidUtils.generateUuid());
        } else {
            //update
            screenInteractionElement = screenInteractionElementService.getByBindKeyAndScreenId(oldBindKey, screenId);
            if (screenInteractionElement == null) {
                screenInteractionElement = new ScreenInteractionElement();
                screenInteractionElement.setId(UuidUtils.generateUuid());
                screenInteractionElement.setScreenId(screenId);
            }
            screenInteractionElement.setBindKey(bindKey);
        }
        screenInteractionElementService.save(screenInteractionElement);

        return Response.ok().build();
    }
}
