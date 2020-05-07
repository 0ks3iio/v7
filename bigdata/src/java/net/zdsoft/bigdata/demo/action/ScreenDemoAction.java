package net.zdsoft.bigdata.demo.action;

import net.zdsoft.bigdata.datav.entity.ScreenDemo;
import net.zdsoft.bigdata.datav.service.ScreenDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yangkj
 * @since 2019/6/17 18:51
 */
@Controller
@RequestMapping(value = "/bigdata/demo")
public class ScreenDemoAction {

    @Autowired
    private ScreenDemoService screenDemoService;

    @GetMapping("/screen")
    public String indexPage(ModelMap model) {
        List<ScreenDemo> allByOrderId = screenDemoService.findAllByOrderId();
        //获取启用状态的经典案例
        List<ScreenDemo> collect = allByOrderId.stream()
                .filter(x -> x.getStatus() == 1).collect(Collectors.toList());
        model.addAttribute("ScreenDemos",collect);
        return "/bigdata/demo/screenDemo.ftl";
    }

    @GetMapping("/bi/screen")
    public String biScreenPage(ModelMap model) {
        List<ScreenDemo> allByOrderId = screenDemoService.findAllByOrderId();
        //获取启用状态的经典案例
        List<ScreenDemo> collect = allByOrderId.stream()
                .filter(x -> x.getStatus() == 1).collect(Collectors.toList());
        model.addAttribute("ScreenDemos",collect);
        return "/bigdata/demo/biScreenDemo.ftl";
    }

    @GetMapping("/gw")
    public String gwPage() {
        return "/bigdata/demo/screenDemo/gwDemo.ftl";
    }

    @GetMapping("/gwxx")
    public String gwxxPage() {
        return "/bigdata/demo/screenDemo/gwxxDemo.ftl";
    }

    @GetMapping("/msyk")
    public String msykPage() {
        return "/bigdata/demo/screenDemo/msykDemo.ftl";
    }

    @GetMapping("/rcbg")
    public String rcbgPage() {
        return "/bigdata/demo/screenDemo/rcbgDemo.ftl";
    }

    @GetMapping("/xsqk")
    public String xsqkPage() {
        return "/bigdata/demo/screenDemo/xsqkDemo.ftl";
    }

    @GetMapping("/jsqk")
    public String jsqkPage() {
        return "/bigdata/demo/screenDemo/jsqkDemo.ftl";
    }

    @GetMapping("/yjq")
    public String yjqPage() {
        return "/bigdata/demo/screenDemo/yjqDemo.ftl";
    }

    @GetMapping("/kelzhdp")
    public String kelzhdpPage() {
        return "/bigdata/demo/screenDemo/kelzhdpDemo.ftl";
    }
}
