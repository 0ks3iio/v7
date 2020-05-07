package net.zdsoft.bigdata.datav.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datav.ShotPath;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.Base64ConvertUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author shenke
 * @since 2018/10/18 9:44
 */
@Controller
@RequestMapping("/bigdata/datav/screen/shot")
public class ScreenshotController extends BigdataBaseAction{

    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;

    @ResponseBody
    @PostMapping("/{screenId}")
    public Response doUpload(@PathVariable("screenId") String screenId,
                             @RequestParam("data") String data,
                             HttpServletRequest request) {
        String filePath = sysOptionRemoteService.findValue(Constant.FILE_PATH);
        LoginInfo loginInfo = (LoginInfo) request.getSession().getAttribute("loginInfo");

        try {
            Base64ConvertUtils.convert2Path(data, ShotPath.of(filePath, loginInfo.getUnitId(), screenId), "jpg");
        } catch (IOException e) {
            return Response.error().message("截图失败").build();
        }
        return Response.ok().message("截图成功").build();
    }
}
