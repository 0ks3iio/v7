package net.zdsoft.cache.admin.controller;

import net.zdsoft.cache.admin.service.ListService;
import net.zdsoft.framework.action.BaseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author shenke
 * @since 2017.07.13
 */
@Controller
@RequestMapping(value = {"redis/list","redis/LIST"})
public class ListController extends BaseAction {

    @Autowired private ListService listService;

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(@RequestParam("key") String key,
                         @RequestParam("value") String value,
                         @RequestParam("index") long index) {
        try {
            listService.update(key,value, index);
        } catch (Exception e){
            return error("update error");
        }
        return success("update success");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Object del(@RequestParam("key") String key,
                      @RequestParam("index") long index) {
        try {
            listService.delete(key, index);
        } catch (Exception e){
            return error("delete error");
        }
        return success("update success");
    }
}
