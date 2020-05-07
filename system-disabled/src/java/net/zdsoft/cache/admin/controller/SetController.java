package net.zdsoft.cache.admin.controller;

import net.zdsoft.cache.admin.service.SetService;
import net.zdsoft.framework.action.BaseAction;
import org.apache.commons.lang3.ArrayUtils;
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
@RequestMapping(value = {"redis/set", "redis/SET"})
public class SetController extends BaseAction {

    @Autowired private SetService setService;

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(@RequestParam("key") String key,
                         @RequestParam("value") String value,
                         @RequestParam("oldValue") String oldValue) {
        setService.update(key, ArrayUtils.toArray(value), ArrayUtils.toArray(oldValue));
        return success("update success");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Object delete(@RequestParam("key") String key,
                         @RequestParam("value") String value) {
        setService.delete(key, ArrayUtils.toArray(value));
        return success("delete success");
    }
}
