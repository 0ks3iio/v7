package net.zdsoft.bigdata.base.action;

import net.zdsoft.bigdata.base.dto.NodeServerTypeDto;
import net.zdsoft.bigdata.base.entity.NodeServer;
import net.zdsoft.bigdata.base.service.BgNodeServerService;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.entity.Option;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobService;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datax.service.DataxJobService;
import net.zdsoft.framework.utils.StringUtils;
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
import java.util.stream.Collectors;

/**
 * @author yangkj
 * @since 2019/6/4 16:21
 */
@Controller
@RequestMapping(value = "bigdata/node")
public class BgNodeServerController {

    @Autowired
    private BgNodeServerService bgNodeServerService;

    @Autowired
    private OptionService optionService;

    @Autowired
    private DataxJobService dataxJobService;

    @Autowired
    private EtlJobService etlJobService;

    @Resource
    private BigLogService bigLogService;

    @RequestMapping(value = "/server/index",method = RequestMethod.GET)
    public String index(ModelMap map,@RequestParam(value = "id") String id){
        List<NodeServer> nodeServerList = bgNodeServerService.findListBy("nodeId", id);
        map.put("nodeId",id);
        map.put("nodeServerList",nodeServerList);
        return "/bigdata/base/node/nodeServerList.ftl";
    }

    @RequestMapping(value = "/server/edit",method = RequestMethod.GET)
    public String edit(ModelMap map,@RequestParam(value = "id",required = false) String id){
        if (id!=null){
            NodeServer nodeServer = bgNodeServerService.findOne(id);
            map.put("nodeServer",nodeServer);
        }else {
            NodeServer nodeServer = new NodeServer();
            map.put("nodeServer",nodeServer);
        }
        String type = "frame";
        List<Option> allOptionByType = optionService.findAllOptionByType(type);
        List<NodeServerTypeDto> collect = allOptionByType.stream().map(x -> {
            NodeServerTypeDto nodeServerTypeDto = new NodeServerTypeDto();
            nodeServerTypeDto.setName(x.getName());
            nodeServerTypeDto.setCode(x.getCode());
            return nodeServerTypeDto;
        }).collect(Collectors.toList());
        map.put("nodeServerTypes",collect);
        return "/bigdata/base/node/nodeServerEdit.ftl";
    }

    @RequestMapping(value = "/server/saveNodeServer",method = RequestMethod.POST)
    @ResponseBody
    public Response saveNodeServer(NodeServer nodeServer){
        if (StringUtils.isBlank(nodeServer.getId())){
            nodeServer.setId(UuidUtils.generateUuid());
            nodeServer.setCreationTime(new Date());
            nodeServer.setModifyTime(new Date());
            //业务日志埋点  新增
            LogDto logDto=new LogDto();
            logDto.setBizCode("insert-nodeServer");
            logDto.setDescription("节点服务 "+nodeServer.getName());
            logDto.setNewData(nodeServer);
            logDto.setBizName("节点设置");
            bigLogService.insertLog(logDto);
        }else {
            NodeServer oldNodeServer = bgNodeServerService.findOne(nodeServer.getId());
            Date creationTime =oldNodeServer.getCreationTime();
            nodeServer.setCreationTime(creationTime);
            nodeServer.setModifyTime(new Date());
            //业务日志埋点  修改
            LogDto logDto=new LogDto();
            logDto.setBizCode("update-nodeServer");
            logDto.setDescription("节点服务 "+oldNodeServer.getName());
            logDto.setOldData(oldNodeServer);
            logDto.setNewData(nodeServer);
            logDto.setBizName("节点设置");
            bigLogService.updateLog(logDto);
        }
        bgNodeServerService.save(nodeServer);

        return Response.ok().message("保存成功").build();
    }

    @RequestMapping(value = "/server/delete",method = RequestMethod.POST)
    @ResponseBody
    public Response deleteNodeServer(@RequestParam("id") String id){
        NodeServer nodeServer = bgNodeServerService.findOne(id);
        long datax = dataxJobService.countBy("nodeId", nodeServer.getNodeId());
        boolean useNode = etlJobService.isExistsNodeAndType(nodeServer.getNodeId(),nodeServer.getType());
        if (datax>0 && "datax".equals(nodeServer.getType())){
            return Response.error().message("该服务被数据同步引用无法删除").build();
        }
        if (useNode){
            return Response.error().message("该服务被批处理引用无法删除").build();
        }
        bgNodeServerService.delete(id);
        if (bgNodeServerService.findOne(id)==null){
            //业务日志埋点  删除
            LogDto logDto=new LogDto();
            logDto.setBizCode("delete-nodeServer");
            logDto.setDescription("节点服务 "+nodeServer.getName());
            logDto.setBizName("节点设置");
            logDto.setOldData(nodeServer);
            bigLogService.deleteLog(logDto);
            return Response.ok().message("删除成功").build();
        }
        return Response.error().message("删除失败").build();
    }
}
