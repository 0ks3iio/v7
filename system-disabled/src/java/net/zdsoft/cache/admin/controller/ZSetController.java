package net.zdsoft.cache.admin.controller;

import net.zdsoft.cache.admin.service.ZSetService;
import net.zdsoft.framework.action.BaseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author shenke
 * @since 2017.07.14
 */
@Controller
@RequestMapping(value = {"redis/zset","redis/ZSET"})
public class ZSetController extends BaseAction {

    @Autowired private ZSetService zSetService;

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(@RequestParam("key") String key,
                         @RequestParam("value") String value,
                         @RequestParam("score") double score) {
        zSetService.update(key, value, score);
        return success("update success");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Object delete(@RequestParam("key") String key,
                         @RequestParam("value") String value) {
        zSetService.delete(key, value);
        return success("delete success");
    }
}
