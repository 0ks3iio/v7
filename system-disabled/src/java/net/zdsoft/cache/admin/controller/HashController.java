package net.zdsoft.cache.admin.controller;

import net.zdsoft.cache.admin.core.CValue;
import net.zdsoft.cache.admin.service.HashService;
import net.zdsoft.framework.action.BaseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

/**
 * @author shenke
 * @since 2017.07.13
 */
@Controller
@RequestMapping(value = {"redis/hash","redis/HASH"})
public class HashController extends BaseAction {

    @Autowired private HashService hashService;

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(@RequestParam("key") String key,
                         @RequestParam("value") final String value,
                         @RequestParam("field")final String field){

        try {
            hashService.update(key,new ArrayList<CValue>(){
                {
                    add(new CValue(field, value));
                }
            });
        } catch (Exception e) {
            log.error("update Hash error",e);
            return error("update error");
        }
        return success("update success");
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Object del(@RequestParam("key") String key,
                      @RequestParam("field") String field) {
        try {
            hashService.del(key,field);
        } catch (Exception e) {
            return error("delete error");
        }
        return success("delete success");
    }
}
