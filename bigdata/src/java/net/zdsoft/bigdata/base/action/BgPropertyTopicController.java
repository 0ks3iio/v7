package net.zdsoft.bigdata.base.action;

import net.zdsoft.bigdata.base.entity.PropertyTopic;
import net.zdsoft.bigdata.base.enu.PropertyTopicCustomCode;
import net.zdsoft.bigdata.base.enu.PropertyTopicOrderCode;
import net.zdsoft.bigdata.base.service.BgPropertyTopicService;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author yangkj
 * @since 2019/5/20 10:52
 */
@Controller
@RequestMapping(value = "bigdata/property/topic")
public class BgPropertyTopicController {

    @Autowired
    private BgPropertyTopicService bgPropertyTopicService;

    @Autowired
    private MetadataService metadataService;

    @Resource
    private BigLogService bigLogService;

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(ModelMap map){
        List<PropertyTopic> propertyTopics = bgPropertyTopicService.findAllByOrderId();
        map.put("propertyTopics",propertyTopics);
        return "/bigdata/base/propertyTopic/propertyTopicList.ftl";
    }

    @RequestMapping(value = "/addPropertyTopic",method = RequestMethod.POST)
    @ResponseBody
    public Response addPropertyTopic(PropertyTopic propertyTopic){
        PropertyTopic propertyTopicByName = bgPropertyTopicService.findByName(propertyTopic.getName());
        if (propertyTopicByName!=null) {
            return Response.error().message("该资产主题名称已被使用").build();
        }
        //检查排序号是否符合规则
        String orderMessage = getOrderMessage(propertyTopic);
        if (!"".equals(orderMessage)){
            return Response.error().message(orderMessage).build();
        }
        propertyTopic.setId(UuidUtils.generateUuid());
        propertyTopic.setCreationTime(new Date());
        propertyTopic.setModifyTime(new Date());
        propertyTopic.setIsCustom(PropertyTopicCustomCode.CUSTOM);
        bgPropertyTopicService.savePropertyTopicByOrderId(propertyTopic);
        if (bgPropertyTopicService.findOne(propertyTopic.getId())!=null){
            //业务日志埋点  新增
            LogDto logDto=new LogDto();
            logDto.setBizCode("insert-propertyTopic");
            logDto.setDescription("资产主题 "+propertyTopic.getName());
            logDto.setNewData(propertyTopic);
            logDto.setBizName("资产主题设置");
            bigLogService.insertLog(logDto);
            return Response.ok().message("新增成功").build();
        }else {
            return Response.error().message("新增失败").build();
        }
    }

    private String getOrderMessage(PropertyTopic propertyTopic) {
        String message = "";
        if (propertyTopic.getOrderId() > PropertyTopicOrderCode.LARGEST
                || propertyTopic.getOrderId() < PropertyTopicOrderCode.LEAST) {
            message = "自定义资产主题排序号只支持100~999";
        }
        return message;
    }

    @RequestMapping(value = "/editPropertyTopic",method = RequestMethod.POST)
    @ResponseBody
    public Response editPropertyTopic(PropertyTopic propertyTopic){
        PropertyTopic propertyTopicExists = bgPropertyTopicService.findOne(propertyTopic.getId());
        if (propertyTopicExists==null){
            return Response.error().message("没有改资产主题信息").build();
        }
        PropertyTopic propertyTopicByName = bgPropertyTopicService.findByName(propertyTopic.getName());
        if (propertyTopicByName!=null && !propertyTopicByName.getId().equals(propertyTopic.getId())){
            return Response.error().message("该资产主题名称已被使用").build();
        }
        if (PropertyTopicCustomCode.INLAY.equals(propertyTopic.getIsCustom())){
            return Response.error().message("系统内置资产主题，无法修改").build();
        }
        //检查排序号是否符合规则
        String orderMessage = getOrderMessage(propertyTopic);
        if (!"".equals(orderMessage)){
            return Response.error().message(orderMessage).build();
        }
        propertyTopic.setCreationTime(propertyTopicExists.getCreationTime());
        propertyTopic.setModifyTime(new Date());
        propertyTopic.setIsCustom(PropertyTopicCustomCode.CUSTOM);
        bgPropertyTopicService.editPropertyTopicByOrderId(propertyTopic);
        //业务日志埋点  修改
        LogDto logDto=new LogDto();
        logDto.setBizCode("update-propertyTopic");
        logDto.setDescription("资产主题 "+propertyTopicExists.getName());
        logDto.setOldData(propertyTopicExists);
        logDto.setNewData(propertyTopic);
        logDto.setBizName("资产主题设置");
        bigLogService.updateLog(logDto);
        return Response.ok().message("编辑成功").build();
    }

    @RequestMapping(value = "/deletePropertyTopic",method = RequestMethod.POST)
    @ResponseBody
    public Response deletePropertyTopic(@RequestParam("id") String id){
        List<Metadata> metadataList = metadataService.findListBy("propertyTopicId",id);
        if (!metadataList.isEmpty()){
            return Response.error().message("该资产被元数据引用，无法删除").build();
        }
        PropertyTopic propertyTopic = bgPropertyTopicService.findOne(id);
        if (PropertyTopicCustomCode.INLAY.equals(propertyTopic.getIsCustom())){
            return Response.error().message("系统内置资产主题，无法删除").build();
        }
        bgPropertyTopicService.deletePropertyTopicByOrderId(id);
        //业务日志埋点  删除
        LogDto logDto=new LogDto();
        logDto.setBizCode("delete-propertyTopic");
        logDto.setDescription("资产主题 "+propertyTopic.getName());
        logDto.setBizName("资产主题设置");
        logDto.setOldData(propertyTopic);
        bigLogService.deleteLog(logDto);
        return Response.ok().message("删除成功").build();
    }

    @RequestMapping(value = "/getLargestOrderId",method = RequestMethod.GET)
    @ResponseBody
    public Response getLargestOrderId(){
        Integer largestOrderId = bgPropertyTopicService.findLargestOrderId();
        if ((!PropertyTopicOrderCode.LEAST.equals(largestOrderId))&&
        !PropertyTopicOrderCode.LARGEST.equals(largestOrderId)){
            largestOrderId +=1;
        }
        return Response.ok().data(largestOrderId).build();
    }
}
