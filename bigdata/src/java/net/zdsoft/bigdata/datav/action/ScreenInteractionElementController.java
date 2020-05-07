package net.zdsoft.bigdata.datav.action;

import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datav.entity.Screen;
import net.zdsoft.bigdata.datav.entity.ScreenInteractionElement;
import net.zdsoft.bigdata.datav.service.ScreenInteractionElementService;
import net.zdsoft.bigdata.datav.service.ScreenService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shenke
 * @since 2018/10/29 上午9:57
 */
@Controller
@RequestMapping("/bigdata/datav/interaction/screen-element")
public class ScreenInteractionElementController extends BigdataBaseAction {

    @Resource
    private ScreenInteractionElementService screenInteractionElementService;
    @Resource
    private ScreenService screenService;

    @ResponseBody
    @PostMapping("/update/{screenId}")
    public Response doUpdateValue(@PathVariable("screenId") String screenId,
                                  @RequestParam("key") String key,
                                  @RequestParam("value") String value) {
        Screen screen = screenService.findOne(screenId);
        if (screen == null) {
            return Response.error().message("大屏已经不存在了").build();
        }
        ScreenInteractionElement element = screenInteractionElementService.getByBindKeyAndScreenId(key, screenId);
        if (element == null) {
            element = new ScreenInteractionElement();
            element.setId(UuidUtils.generateUuid());
            element.setScreenId(screenId);
            element.setBindKey(key);
        }
        element.setDefaultValue(value);
        screenInteractionElementService.save(element);
        return Response.ok().message("更新成功").build();
    }

    @ResponseBody
    @GetMapping("/delete/{screenId}")
    public Response doDelete(@PathVariable("screenId") String screenId,
                             @RequestParam("key") String key) {
        screenInteractionElementService.deleteByScreenIdAndBindKey(screenId, key);
        return Response.ok().message("OK").build();
    }

    @ResponseBody
    @GetMapping("/{screenId}")
    public Response doGetList(@PathVariable("screenId") String screenId) {
        List<ScreenInteractionElement> els =  screenInteractionElementService.getScreenDefaultInteractionItems(screenId);
        return Response.ok().data(els).build();
    }
}
