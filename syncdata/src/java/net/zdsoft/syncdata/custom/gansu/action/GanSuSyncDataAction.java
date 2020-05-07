package net.zdsoft.syncdata.custom.gansu.action;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.syncdata.custom.gansu.service.GanSuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Designed By luf
 *
 * @author luf
 * @date 2019/8/21 9:55
 */
@Controller
@RequestMapping("/syncdata/gansu")
@Lazy
public class GanSuSyncDataAction  extends BaseAction {
    @Autowired
    private GanSuService ganSuService;

    @RequestMapping("/syncEduAndSch")
    public String syncEdu(HttpServletRequest request, ModelMap map) {
//        http://localhost:8083/V7/syncdata/gansu/syncEduAndSch
        try {
            ganSuService.saveEduAndSch();
        } catch (Exception e) {
            e.printStackTrace();
            return promptFlt(map, "同步教育局 、学校失败："+e.getMessage());
        }
        return promptFlt(map, "同步教育局 、学校，请查看结果。");
    }
}
