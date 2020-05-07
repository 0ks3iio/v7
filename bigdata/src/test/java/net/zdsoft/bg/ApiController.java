package net.zdsoft.bg;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ke_shen@126.com
 * @since 2018/4/9 下午5:23
 */
@Controller
@RequestMapping("/api")
public class ApiController {

    @ResponseBody
    @RequestMapping("/test")
    public Object testApi() {
        JSONObject object = new JSONObject();
        object.put("username", "zhangsan");
        return object;
    }
}
