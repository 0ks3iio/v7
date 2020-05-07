package net.zdsoft.bigdata.base.action;

import net.zdsoft.bigdata.base.dto.NodeDto;
import net.zdsoft.bigdata.base.entity.Node;
import net.zdsoft.bigdata.base.entity.NodeServer;
import net.zdsoft.bigdata.base.service.BgNodeServerService;
import net.zdsoft.bigdata.base.service.BgNodeService;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datax.service.DataxJobService;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yangkj
 * @since 2019/5/20 16:09
 */
@Controller
@RequestMapping(value = "bigdata/node")
public class BgNodeController {

    @Autowired
    private BgNodeService bgNodeService;

    @Autowired
    private BgNodeServerService bgNodeServerService;

    @Autowired
    private DataxJobService dataxJobService;

    @Autowired
    private EtlJobService etlJobService;

    @Autowired
    private BigLogService bigLogService;

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(ModelMap map){
        List<Node> nodes = bgNodeService.findAll();
        //获取所有节点的id
        String[] ids = EntityUtils.getArray(nodes, x -> x.getId(), String[]::new);
        //根据节点id查找所有的节点服务信息
        Map<String, List<NodeServer>> nodeMap = bgNodeServerService.findListByIn("nodeId", ids)
                .stream().collect(Collectors.groupingBy(x -> x.getNodeId()));
        //将节点信息和节点服务信息重新封装
        List<NodeDto> result = nodes.stream().map(x -> {
            NodeDto nodeDto = new NodeDto();
            nodeDto.setId(x.getId());
            nodeDto.setName(x.getName());
            nodeDto.setStatus(x.getStatus());
            nodeDto.setNodeServers(CollectionUtils.isEmpty(
                    nodeMap.get(x.getId()))?new ArrayList<>():nodeMap.get(x.getId()));
            nodeDto.setCount(nodeDto.getNodeServers().size());
            return nodeDto;
        }).collect(Collectors.toList());
        map.put("nodes",result);
        return "/bigdata/base/node/nodeList.ftl";
    }

    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public String add(ModelMap map,@RequestParam(value = "id",required = false) String id){
        if (id!=null){
            Node node = bgNodeService.findOne(id);
            map.put("node",node);
        }else {
            Node node = new Node();
            map.put("node",node);
        }
        return "/bigdata/base/node/nodeEdit.ftl";
    }

    @RequestMapping(value = "/testConnect",method = RequestMethod.POST)
    @ResponseBody
    public Response testConnect(Node node){
        boolean flag = bgNodeService.nodeConnection(node);
        if (flag){
            return Response.ok().message("连接成功").build();
        }else{
            return Response.error().message("连接失败").build();
        }
    }

    @RequestMapping(value = "/addNode",method = RequestMethod.POST)
    @ResponseBody
    public Response addNode(Node node){
        Node nameExists = bgNodeService.findOneBy("name", node.getName());
        if (nameExists!=null){
            return Response.error().message("该节点名称已被使用").build();
        }
        node.setId(UuidUtils.generateUuid());
        node.setCreationTime(new Date());
        node.setModifyTime(new Date());
        bgNodeService.save(node);
        //业务日志埋点  新增
        LogDto logDto=new LogDto();
        logDto.setBizCode("insert-node");
        logDto.setDescription("节点 "+node.getName());
        logDto.setNewData(node);
        logDto.setBizName("节点设置");
        bigLogService.insertLog(logDto);
        return Response.ok().message("新增成功").build();
    }

    @RequestMapping(value = "/editNode",method = RequestMethod.POST)
    @ResponseBody
    public Response editNode(Node node){
        Node nameExists = bgNodeService.findOneBy("name", node.getName());
        if (nameExists!=null && !nameExists.getId().equals(node.getId())){
            return Response.error().message("该节点名称已被使用").build();
        }
        Node nodeForTime = bgNodeService.findOne(node.getId());
        node.setCreationTime(nodeForTime.getCreationTime());
        node.setModifyTime(new Date());
        bgNodeService.save(node);
        //业务日志埋点  修改
        LogDto logDto=new LogDto();
        logDto.setBizCode("update-node");
        logDto.setDescription("节点 "+nodeForTime.getName());
        logDto.setOldData(nodeForTime);
        logDto.setNewData(node);
        logDto.setBizName("节点设置");
        bigLogService.updateLog(logDto);

        return Response.ok().message("修改成功").build();
    }

    @RequestMapping(value = "/deleteNode",method = RequestMethod.POST)
    @ResponseBody
    public Response deleteNode(@RequestParam("id") String id){
        Node oldNode = bgNodeService.findOne(id);
        long datax = dataxJobService.countBy("nodeId", oldNode.getId());
        boolean useNode = etlJobService.isExistsNode(oldNode.getId());
        if (datax>0){
            return Response.error().message("该节点被数据同步引用无法删除").build();
        }
        if (useNode){
            return Response.error().message("该节点被批处理引用无法删除").build();
        }
        bgNodeService.delete(id);
        if (bgNodeService.findOne(id)==null){
            //业务日志埋点  删除
            LogDto logDto=new LogDto();
            logDto.setBizCode("delete-qualityRule");
            logDto.setDescription("节点 "+oldNode.getName());
            logDto.setBizName("节点设置");
            logDto.setOldData(oldNode);
            bigLogService.deleteLog(logDto);
            return Response.ok().message("删除成功").build();
        }
        return Response.error().message("删除失败").build();
    }
}
