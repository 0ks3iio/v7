package net.zdsoft.cache.admin.controller;

import net.zdsoft.cache.admin.service.StringService;
import net.zdsoft.framework.action.BaseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * @author shenke
 * @since 2017.07.13
 */
@Controller
@RequestMapping(value = {"redis/string","redis/STRING"})
public class StringController extends BaseAction {
    @Autowired private StringService stringService;

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(@RequestParam("key") String key,
                         @RequestParam("value") String value){
        stringService.updateValue(key, value);
        return success("更新成功");
    }
}
