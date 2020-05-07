package net.zdsoft.bigdata.datav.action;

import net.zdsoft.bigdata.data.OrderType;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datav.entity.Screen;
import net.zdsoft.bigdata.datav.entity.ScreenDemo;
import net.zdsoft.bigdata.datav.entity.ScreenLibrary;
import net.zdsoft.bigdata.datav.service.ScreenDemoService;
import net.zdsoft.bigdata.datav.service.ScreenLibraryService;
import net.zdsoft.bigdata.datav.service.ScreenService;
import net.zdsoft.bigdata.system.service.BgUserAuthService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据大屏设计模块Controller
 *
 * @author shenke
 * @since 2018/10/17 9:55
 */
@Controller
@RequestMapping({"/bigdata/cockpit"})
public class DataVModelController extends BigdataBaseAction {

    @Resource
    private ScreenService screenService;
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Resource
    private ScreenLibraryService screenLibraryService;
    @Resource
    private BigLogService bigLogService;
    @Resource
    private BgUserAuthService bgUserAuthService;
    @Autowired
    private ScreenDemoService screenDemoService;

    @GetMapping("index")
    public String indexPage(ModelMap model) {
        LoginInfo loginInfo = getLoginInfo();


        boolean bigdataSuperUser = bgUserAuthService.isSuperUser(loginInfo.getUserId());
        List<Screen> screenList = bigdataSuperUser ?
                screenService.getScreensForSuper(loginInfo.getUserId())
                : screenService.getScreensByUnitId(loginInfo.getUnitId(), loginInfo.getUserId());
        model.addAttribute("orderTypes", OrderType.useValues());
        model.addAttribute("cockpits", screenList);
        model.addAttribute("currentUnitId", loginInfo.getUnitId());
        model.addAttribute("fileUrl", sysOptionRemoteService.getFileUrl(getRequest().getServerName()));
        model.addAttribute("userId", loginInfo.getUserId());
        model.addAttribute("bigdataSuperUser",bigdataSuperUser);
        return "/bigdata/datav/model/editIndex.ftl";
    }

    @ResponseBody
    @PostMapping("/clone")
    public Response doClone(@RequestParam("id") String id, @RequestParam("name") String name) {

        screenService.cloneNoTransactional(id, name, getLoginInfo().getUserId());
        //业务日志埋点  新增
        LogDto logDto = new LogDto();
        logDto.setBizCode("insert-cloneScreen");
        logDto.setDescription("复制的大屏 " + name);
        logDto.setNewData(name);
        logDto.setBizName("数据大屏");
        bigLogService.insertLog(logDto);

        return Response.ok().message("复制成功").build();
    }

    @GetMapping("/template")
    public String doTemplate(ModelMap model) {
        List<ScreenLibrary> screenLibraries = screenLibraryService.findAll();
        model.addAttribute("libriaries", screenLibraries);
        return "/bigdata/datav/datav-create-template.ftl";
    }

    @GetMapping("/classic")
    public String classicCase(ModelMap model) {
        List<ScreenDemo> screenDemos = screenDemoService.findAllByOrderId();
        model.addAttribute("screenDemos", screenDemos);
        return "/bigdata/datav/datav-classic-list.ftl";
    }

    @PostMapping("/changeStatus")
    @ResponseBody
    public Response changeStatus(@RequestParam("id") String id,@RequestParam("status") Integer status){
        try {
            ScreenDemo screenDemo = screenDemoService.findOne(id);
            screenDemo.setStatus(status);
            screenDemoService.save(screenDemo);
            return Response.ok().build();
        }catch (Exception e){
            return Response.error().message("操作失败").build();
        }
    }

    @PostMapping("/editScreenDemoOrderId")
    @ResponseBody
    public Response editScreenDemoOrderId(@RequestParam("id") String id,
                                          @RequestParam("orderId") Integer orderId){
        try {
            ScreenDemo screenDemo = screenDemoService.findOne(id);
            Integer oldOrderId = screenDemo.getOrderId();
            screenDemo.setOrderId(orderId);
            ScreenDemo oldScreenDemo = screenDemoService.findOneBy("orderId", orderId);
            if (oldScreenDemo!=null){
                if (oldOrderId>orderId){
                    oldScreenDemo.setOrderId(orderId+1);
                }else {
                    oldScreenDemo.setOrderId(orderId-1);
                }
                screenDemoService.save(oldScreenDemo);
            }
            screenDemoService.save(screenDemo);
            //重新排序
            List<ScreenDemo> allByOrderId = screenDemoService.findAllByOrderId();
            for (int i=1;i<=allByOrderId.size();i++){
                allByOrderId.get(i-1).setOrderId(i);
            }
            screenDemoService.saveAll(allByOrderId.toArray(new ScreenDemo[0]));
            return Response.ok().message("修改成功").build();
        }catch (Exception e){
            return Response.error().message("修改失败").build();
        }
    }
}
