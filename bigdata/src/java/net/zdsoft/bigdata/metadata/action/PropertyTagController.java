package net.zdsoft.bigdata.metadata.action;

import net.zdsoft.bigdata.base.entity.PropertyTag;
import net.zdsoft.bigdata.base.service.PropertyTagService;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.vo.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by wangdongdong on 2019/5/21 21:04.
 */
@Controller
@RequestMapping(value = "/bigdata/propertyTag")
public class PropertyTagController {

    @Resource
    private PropertyTagService propertyTagService;

    @RequestMapping("/addTag")
    @ResponseBody
    public Response addTag(String tagName) {
        try {
            PropertyTag propertyTag = new PropertyTag();
            propertyTag.setName(tagName);
            propertyTag.setIsCustom(0);
            propertyTagService.savePropertyTag(propertyTag);
            return Response.ok().data(propertyTag.getId()).build();
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/deleteTag")
    @ResponseBody
    public Response deleteTag(String tagId) {
        propertyTagService.delete(tagId);
        return Response.ok().build();
    }
}
