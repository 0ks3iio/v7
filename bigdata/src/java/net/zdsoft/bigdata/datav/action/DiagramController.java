package net.zdsoft.bigdata.datav.action;

import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datav.DataVStringUtil;
import net.zdsoft.bigdata.datav.entity.AbstractDiagram;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramElement;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.render.crete.custom.ImageOption;
import net.zdsoft.bigdata.datav.service.DiagramElementService;
import net.zdsoft.bigdata.datav.service.DiagramService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author shenke
 * @since 2018/9/26 16:00
 */
@Controller
@RequestMapping("/bigdata/datav/diagram")
public class DiagramController extends BigdataBaseAction {

    @Resource
    private DiagramService diagramService;
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Resource
    private BigLogService bigLogService;

    /**
     * 创建一个空的图表
     */
    @ResponseBody
    @PostMapping(
            value = "/{screenId}/{diagramType}"
    )
    public Response doCreateEmpty(@PathVariable("screenId") String screenId,
                             @PathVariable("diagramType") Integer diagramType) {
        return Response.ok().data(diagramService.createEmpty(screenId, diagramType).getId()).build();
    }

    @Resource
    private DiagramElementService diagramElementService;

    @ResponseBody
    @RequestMapping(
            value = "/update/{id}",
            method = {RequestMethod.PUT, RequestMethod.POST}
    )
    public Response doUpdate(@PathVariable("id") String diagramId,
                             @RequestParam(value = "elementId", required = false) String elementId,
                             Diagram modifyDiagram) {

        if (StringUtils.isNotBlank(elementId)) {
            DiagramElement diagram = diagramElementService.findOne(elementId);
            setValue(diagram, modifyDiagram);
            diagramElementService.save(diagram);
        } else {
            Diagram diagram = diagramService.findOne(diagramId);
            setValue(diagram, modifyDiagram);
            diagramService.save(diagram);
        }
        return Response.ok().build();
    }

    private void setValue(AbstractDiagram diagram, Diagram modifyDiagram) {
        diagram.setDatasourceType(modifyDiagram.getDatasourceType());
        diagram.setDatasourceId(modifyDiagram.getDatasourceId());
        diagram.setDatasourceValueJson(DataVStringUtil.compressOfBlank(modifyDiagram.getDatasourceValueJson()));
        diagram.setDatasourceValueSql(DataVStringUtil.compressOfEnterLine(modifyDiagram.getDatasourceValueSql()));
        diagram.setUpdateInterval(modifyDiagram.getUpdateInterval());
    }


    @ResponseBody
    @GetMapping("/delete/{screenId}/{id}")
    public Response doDelete(@PathVariable("screenId") String screenId,
                             @PathVariable("id") String id) {
        Diagram diagram = diagramService.findOne(id);
        tryDeleteStoreImage(diagram);
        diagramService.deleteByScreenIdAnId(screenId, id);
        return Response.ok().message("删除成功").build();
    }

    private void tryDeleteStoreImage(Diagram diagram) {
        if (diagram == null) {
            return;
        }
        if (DiagramEnum.SINGLE_IMAGE.getType().equals(diagram.getDiagramType())
                || DiagramEnum.SHUFFLING_IMAGE.getType().equals(diagram.getDiagramType())) {
            String filePath = sysOptionRemoteService.findValue(Constant.FILE_PATH);
            try {
                FileUtils.deleteDirectory(Paths.get(filePath + File.separator + ImageOption.PATH + File.separator + getLoginInfo().getUnitId() + File.separator + diagram.getId()).toFile());
            } catch (IOException e) {
                // ignore
            }
        }
    }

}
