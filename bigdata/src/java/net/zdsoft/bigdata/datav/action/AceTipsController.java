package net.zdsoft.bigdata.datav.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datav.entity.ScreenInteractionElement;
import net.zdsoft.bigdata.datav.service.ScreenInteractionElementService;
import net.zdsoft.bigdata.datav.vo.AceTipVo;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author shenke
 * @since 2018/10/29 下午1:52
 */
@Controller
@RequestMapping("/bigdata/datav/ace/tips")
public class AceTipsController extends BigdataBaseAction{

    @Resource
    private ScreenInteractionElementService screenInteractionElementService;

    @ResponseBody
    @GetMapping("/{screenId}")
    public Response doGetTips(@PathVariable("screenId") String screenId) {
        List<ScreenInteractionElement> elementList = screenInteractionElementService.getScreenDefaultInteractionItems(screenId);
        List<AceTipVo> aceTipVos = new ArrayList<>(elementList.size() + 11);
        int count = 12;
        for (ScreenInteractionElement element : elementList) {
            AceTipVo tipVo = new AceTipVo();
            tipVo.setCaption("${" + element.getBindKey() + "}");
            tipVo.setValue(tipVo.getCaption());
            tipVo.setScore(count);
            count ++;
            aceTipVos.add(tipVo);
        }
        return Response.ok().data(aceTipVos).build();
    }

}
