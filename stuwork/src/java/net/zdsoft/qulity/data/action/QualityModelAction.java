package net.zdsoft.qulity.data.action;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/quality/model")
public class QualityModelAction extends BaseAction {
    @RequestMapping("/culture/page")
    @ControllerInfo("文化成绩")
    public String cultureIndex(ModelMap map) {

        return "/quality/model/qualityModel1.ftl";
    }

    @RequestMapping("/stuwork/page")
    @ControllerInfo("德育")
    public String stuworkIndex(ModelMap map) {

        return "/quality/model/qualityModel2.ftl";
    }
    @RequestMapping("/sports/page")
    @ControllerInfo("体育")
    public String sportsIndex(ModelMap map) {

        return "/quality/model/qualityModel3.ftl";
    }
    @RequestMapping("/special/page")
    @ControllerInfo("美育")
    public String specialIndex(ModelMap map) {

        return "/quality/model/qualityModel4.ftl";
    }

}
