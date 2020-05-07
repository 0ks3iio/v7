/**
 * FileName: TagController.java
 * Author:   shenke
 * Date:     2018/6/1 下午1:36
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action;

import java.util.Date;

import javax.annotation.Resource;

import net.zdsoft.bigdata.data.entity.Tag;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.TagRelationService;
import net.zdsoft.bigdata.data.service.TagService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author shenke
 * @since 2018/6/1 下午1:36
 */
@Controller
@RequestMapping(
        value = "bigdata/tag"
)
public class TagController extends BigdataBaseAction {

    @Resource
    private TagService tagService;
    @Resource
    private TagRelationService tagRelationService;

    @ResponseBody
    @RequestMapping(
            value = "{tagId}",
            method = {RequestMethod.DELETE, RequestMethod.GET}
    )
    public Response deleteTag(@PathVariable("tagId") String tagId,
                              @RequestParam(value = "chartId", required = false) String chartId) {
        boolean used = tagService.isUsed(tagId, chartId);
        if (used) {
            return Response.error().message("该标签已被使用，无法删除").build();
        }
        try {
            tagService.deleteByTagId(tagId);
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }
        return Response.ok().message("标签删除成功").build();
    }

    @ResponseBody
    @RequestMapping(
            value = "",
            method = RequestMethod.POST,
            consumes = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            }
    )
    public Response addTag(Tag tag) {
        if (StringUtils.isBlank(tag.getId())) {
            tag.setId(UuidUtils.generateUuid());
            tag.setUnitId(getLoginInfo().getUnitId());
            tag.setCreationTime(new Date());
        }
        try {
            if (tagService.findListBy(new String[]{"tagName", "unitId", "tagType"}, new String[]{tag.getTagName(), getLoginInfo().getUnitId(), tag.getTagType().toString()}).isEmpty()) {
                tagService.save(tag);
            } else {
                return Response.error().message("已存在相同名称的标签").build();
            }
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
        return Response.ok().data(tag.getId()).build();
    }
}
