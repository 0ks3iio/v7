package net.zdsoft.bigdata.datav.action;

import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datav.entity.ScreenStyle;
import net.zdsoft.bigdata.datav.service.ScreenStyleService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author shenke
 * @since 2018/10/16 18:39
 */
@Controller
@RequestMapping("/bigdata/datav/screen_style/")
public class ScreenStyleController extends BigdataBaseAction {

    @Resource
    private ScreenStyleService screenStyleService;

    @ResponseBody
    @PostMapping("/update/{screenId}")
    public Response doUpdate(@PathVariable("screenId") String screenId,
                             ScreenStyle screenStyle) {
        ScreenStyle exists = screenStyleService.getByScreenId(screenId);
        if (exists != null) {
            exists.setBackgroundColor(screenStyle.getBackgroundColor());
            exists.setHeight(screenStyle.getHeight());
            exists.setWidth(screenStyle.getWidth());
            exists.setDateTimeStyle(screenStyle.getDateTimeStyle());
            screenStyleService.save(exists);
        } else {
            screenStyle.setId(screenId);
            screenStyleService.save(screenStyle);
        }
        return Response.ok().message("更新成功").build();
    }
}
