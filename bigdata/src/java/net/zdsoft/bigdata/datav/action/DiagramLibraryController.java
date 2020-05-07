package net.zdsoft.bigdata.datav.action;

import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datav.service.DiagramLibraryDeletedException;
import net.zdsoft.bigdata.datav.service.DiagramLibraryService;
import net.zdsoft.bigdata.datav.service.OverDiagramLibraryCollectMaxException;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author shenke
 * @since 2018/12/6 下午1:16
 */
@Controller
@RequestMapping("/bigdata/diagramLibrary")
public class DiagramLibraryController extends BigdataBaseAction {

    @Resource
    private DiagramLibraryService diagramLibraryService;
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;

    private Logger logger = LoggerFactory.getLogger(DiagramLibraryController.class);

    /**
     * 收藏一个图表
     */
    @ResponseBody
    @PostMapping(value = "collect")
    public Response doCollectDiagram(String diagramId, String libraryName) {
        try {
            diagramLibraryService.collectDiagram(diagramId, getLoginInfo().getUserId(), libraryName);
        } catch (OverDiagramLibraryCollectMaxException e) {
            logger.error("收藏出错", e);
            return Response.error().message(e.getMessage()).build();
        }
        return Response.ok().message("收藏成功").build();
    }

    /**
     * 删除收藏
     */
    @ResponseBody
    @GetMapping(
            value = "/delete/collect/{libraryId}"
    )
    public Response doUnCollectDiagram(@PathVariable("libraryId") String libraryId) {
        diagramLibraryService.deleteCollect(libraryId);
        return Response.ok().message("删除成功").build();
    }

    /**
     * 更新收藏名称
     */
    @PostMapping(
            value = "/update/collect/{libraryId}"
    )
    @ResponseBody
    public Response doUpdateCollectName(String libraryName,
                                        @PathVariable("libraryId") String libraryId) {
        diagramLibraryService.updateCollectLibraryName(libraryId, libraryName);
        return Response.ok().build();
    }

    /**
     * 收藏列表
     */
    @GetMapping(
            value = "collect"
    )
    public String collectIndex(ModelMap model, HttpServletRequest request) {
        String userId = getLoginInfo().getUserId();
        model.addAttribute("libries", diagramLibraryService.getCollectLibraryByUserId(userId));
        return "/bigdata/datav/model/diagramLibrary.ftl";
    }

    @ResponseBody
    @PostMapping(
            value = "collect/screen"
    )
    public Response addToScreen(String libraryId, String screenId) {
        String diagramId = null;
        try {
            diagramId = diagramLibraryService.add2Screen(libraryId, screenId);
        } catch (DiagramLibraryDeletedException e) {
            return Response.error().message(e.getMessage()).data(null).build();
        }
        return Response.ok().message("").data(diagramId).build();
    }
}
