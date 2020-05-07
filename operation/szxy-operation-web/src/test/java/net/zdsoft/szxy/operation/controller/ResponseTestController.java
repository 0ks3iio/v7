package net.zdsoft.szxy.operation.controller;

import net.zdsoft.szxy.plugin.mvc.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author shenke
 * @since 2019/1/11 下午4:18
 */
@Controller
@RequestMapping("/operation/response/test")
public class ResponseTestController {

    @GetMapping("")
    @ResponseBody
    public Response execute() {
        return Response.ok()
                .message("i18n-key")
                .data("dataKey", "dataValue")
                .build();
    }
}
