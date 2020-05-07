package net.zdsoft.adapter;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 介入微服务桌面
 * @author shenke
 * @date 2019/10/23 上午10:26
 */
@Controller
@RequestMapping("/micro/adapter")
public class MicroAdapterController {

    @RequestMapping("dispatch.action")
    public String execute(@RequestParam("targetUrl") String targetUrl,
                          @RequestParam(value = "moduleName", required = false) String moduleName,
                          Model model) {
        model.addAttribute("targetUrl", targetUrl);
        model.addAttribute("moduleName", moduleName);
        return "/desktop/adapter/adapter.ftl";
    }

    @RequestMapping("error")
    public String error(Model model, HttpServletRequest request) {
        String error = (String) request.getAttribute(MicroAdapterAuthenticationInterceptor.ERROR_KEY);
        model.addAttribute("error", error);
        return "/desktop/adapter/error.ftl";
    }

    @ResponseBody
    @GetMapping("invalid")
    public Object logout(@RequestParam("authorization") String authorization) {
        AdapterSessionHolder.invalidAndClearHttpSession(authorization);
        return new Response("退出成功", HttpStatus.OK.value());
    }

    static class Response {
        private String message;
        private Integer code;

        public Response(String message, Integer code) {
            this.message = message;
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public Integer getCode() {
            return code;
        }
    }
}
