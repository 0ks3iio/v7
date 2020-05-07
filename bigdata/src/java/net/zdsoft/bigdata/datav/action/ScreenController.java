package net.zdsoft.bigdata.datav.action;

import net.zdsoft.bigdata.data.OrderType;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datav.ShotPath;
import net.zdsoft.bigdata.datav.action.vo.Layer;
import net.zdsoft.bigdata.datav.dto.SimpleDiagram;
import net.zdsoft.bigdata.datav.entity.*;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.render.NeedRenderGroupBuilder;
import net.zdsoft.bigdata.datav.service.*;
import net.zdsoft.bigdata.datav.vo.DiagramEnumCategory;
import net.zdsoft.bigdata.datav.vo.DiagramEnumGroup;
import net.zdsoft.bigdata.datav.vo.DiagramEnumView;
import net.zdsoft.bigdata.datav.vo.DiagramVo;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2018/9/26 15:44
 */
@Controller
@RequestMapping("/bigdata/datav/screen")
public class ScreenController extends BigdataBaseAction {

    @Resource
    private ScreenService screenService;
    @Resource
    private DiagramContrastService diagramContrastService;
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Resource
    private DiagramService diagramService;
    @Resource
    private DiagramParameterService diagramParameterService;
    @Resource
    private ScreenStyleService screenStyleService;
    @Resource
    private ServletContext servletContext;
    @Resource
    private ScreenLibraryService screenLibraryService;
    @Resource
    private ScreenInteractionElementService screenInteractionElementService;
    @Resource
    private ScreenGroupService screenGroupService;
    @Resource
    private DiagramLibraryService diagramLibraryService;
    //@Resource
    //private DiagramLayerGroupService diagramLayerGroupService;
    @Resource
    private BigLogService bigLogService;

    @RequestMapping(
            value = "{id}",
            method = RequestMethod.GET
    )
    public String edit(@PathVariable("id") String id,
                       ModelMap model) {
        Screen screen = screenService.findOne(id);
        List<Diagram> diagrams = diagramService.getDiagramsByScreenId(id);
        model.addAttribute("diagramVos", earlyConvertByParameters(diagrams));
        model.addAttribute("diagramTypeGroups", diagramGroup());
        model.addAttribute("screen", screen);
        model.addAttribute("screenId", id);
        ScreenStyle style = screenStyleService.getByScreenId(screen.getId());
        if (style == null) {
            style = new ScreenStyle();
        }
        model.addAttribute("screenStyle", style);
        model.addAttribute("fileUrl", sysOptionRemoteService.getFileUrl(getRequest().getServerName()));
        //默认的交互参数
        List<ScreenInteractionElement> interactionElements = screenInteractionElementService.getScreenDefaultInteractionItems(id);
        model.addAttribute("interactionElements", interactionElements);
        model.addAttribute("libries", diagramLibraryService.getCollectLibraryByUserId(getLoginInfo().getUserId()));

        //图层，分组情况



        return "/bigdata/datav/datav-create-index2.ftl";
    }
    @RequestMapping(
            value = "{id}/layer",
            method = RequestMethod.GET
    )
    public String doLoadLayer(@PathVariable("id") String id,
                              ModelMap model) {
        Screen screen = screenService.findOne(id);
        model.addAttribute("layers", loadLayer(screen));
        return "/bigdata/datav/datav-layer.ftl";
    }

    private List<Layer> loadLayer(Screen screen) {
        List<SimpleDiagram> simpleDiagrams = diagramService.getSimpleDiagramByScreenId(screen.getId());
        //List<DiagramLayerGroup> layerGroups = screen.getDiagramLayerGroups();
        //整合分组和图层
        List<Layer> layers = simpleDiagrams.stream().map(e-> new Layer().convert(e))
                .sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        //if (!CollectionUtils.isEmpty(layerGroups)) {
        //    Map<String, List<Layer>> groupLayers = new HashMap<>(layerGroups.size());
        //    simpleDiagrams.stream().filter(e->StringUtils.isNotBlank(e.getLayerGroupId()))
        //            .forEach(e->{
        //                groupLayers.computeIfAbsent(e.getLayerGroupId(), k->new ArrayList<>())
        //                        .add(new Layer().convert(e));
        //            });
        //    List<Layer> remove = new ArrayList<>();
        //    List<Layer> groups = layerGroups.stream().map(e->{
        //        Layer layer = new Layer();
        //        layer.setChildren(groupLayers.get(e.getId()));
        //        if (layer.getChildren() != null) {
        //            remove.addAll(layer.getChildren());
        //        }
        //        layer.setName(e.getName());
        //        layer.setId(e.getId());
        //        layer.setLevel(e.getLevel());
        //        return layer;
        //    }).collect(Collectors.toList());
        //    layers.removeAll(remove);
        //    layers.addAll(groups);
        //    layers.sort(Comparator.reverseOrder());
        //}
        return layers;
    }

    @GetMapping(
            value = "/view/{id}"
    )
    public String view(@PathVariable("id") String id, ModelMap model) {
        Screen screen = screenService.findOne(id);
        if (StringUtils.isNotBlank(screen.getUrl())) {
            return "redirect:" + screen.getUrl();
        }
        List<Diagram> diagrams = diagramService.getDiagramsByScreenId(id);
        model.addAttribute("diagramVos", earlyConvertByParameters(diagrams));
        model.addAttribute("screen", screen);
        model.addAttribute("screenId", id);
        ScreenStyle style = screenStyleService.getByScreenId(screen.getId());
        if (style == null) {
            style = new ScreenStyle();
        }
        model.addAttribute("screenStyle", style);
        model.addAttribute("fileUrl", sysOptionRemoteService.getFileUrl(getRequest().getServerName()));
        return "/bigdata/datav/datav-view.ftl";
    }



    @GetMapping("groupView/{id}")
    public String viewGroup(@PathVariable("id") String groupId, ModelMap model) {
        ScreenGroup screenGroup = screenGroupService.findOne(groupId);
        if (screenGroup == null) {
            //error
        } else {
            String[] screenIds = StringUtils.split(screenGroup.getScreenIds(), ",");
            if (!ArrayUtils.isEmpty(screenIds)) {
                List<List<DiagramVo>> diagramVosList = new ArrayList<>();
                List<ScreenStyle> screenStyles = new ArrayList<>();
                for (String screenId : screenIds) {
                    Screen screen = screenService.findOne(screenId);
                    if (screen != null) {
                        diagramVosList.add(earlyConvertByParameters(diagramService.getDiagramsByScreenId(screenId)));
                        ScreenStyle style = screenStyleService.getByScreenId(screen.getId());
                        if (style == null) {
                            style = new ScreenStyle();
                        }
                        style.setScreenName(screen.getName());
                        screenStyles.add(style);
                    }
                }
                model.addAttribute("diagramVosList", diagramVosList);
                model.addAttribute("screenStyles", screenStyles);
            }
        }
        model.addAttribute("fileUrl", sysOptionRemoteService.getFileUrl(getRequest().getServerName()));
        model.addAttribute("screenGroup", screenGroup);
        return "/bigdata/datav/datav-group-view.ftl";
    }

    private List<DiagramVo> earlyConvertByParameters(List<Diagram> diagrams) {
        List<DiagramVo> diagramVos = new ArrayList<>(diagrams.size());
        for (Diagram diagram : diagrams) {
            DiagramVo diagramVo = NeedRenderGroupBuilder.buildGroup(
                    diagramParameterService.getDiagramBasicById(diagram.getId()),
                    GroupKey.basic, DiagramVo.class);
            if (diagramVo != null) {
                diagramVo.setDiagramId(diagram.getId());
                diagramVo.setDiagramType(diagram.getDiagramType());
                diagramVos.add(diagramVo);
                diagramVo.setUpdateInterval(diagram.getUpdateInterval());
                diagramVo.setToScreenId(diagram.getScreenId());
            }
        }
        return diagramVos;
    }

    private List<DiagramEnumGroup> diagramGroup() {
        String fileUrl = "";
        List<DiagramContrast> diagramContrasts = diagramContrastService.getAll();
        diagramContrasts = diagramContrasts.stream().filter(diagramContrast -> {
            return !(new Integer(22).equals(diagramContrast.getType()) || new Integer(25).equals(diagramContrast.getType())
                    || new Integer(34).equals(diagramContrast.getType()));
        }).collect(Collectors.toList());
        Map<String, Map<String, List<DiagramEnumView>>> groups = new HashMap<>();
        for (DiagramContrast diagramContrast : diagramContrasts) {
            Map<String, List<DiagramEnumView>> categories =
                    groups.computeIfAbsent(diagramContrast.getGroupName(), groupName-> new HashMap<>());
            List<DiagramEnumView> views = categories.computeIfAbsent(diagramContrast.getCategory(), category-> new ArrayList<>());
            DiagramEnumView view = new DiagramEnumView();
            view.setDiagramType(diagramContrast.getType());
            view.setIconUrl(fileUrl + "/bigdata/v3/static/datav/images/chart-" + diagramContrast.getType() + ".png");
            views.add(view);
        }
        List<DiagramEnumGroup> groupList = new ArrayList<>(groups.size());
        for (Map.Entry<String, Map<String, List<DiagramEnumView>>> entry : groups.entrySet()) {
            List<DiagramEnumCategory> categories = new ArrayList<>(entry.getValue().size());
            for (Map.Entry<String, List<DiagramEnumView>> categoryEntry : entry.getValue().entrySet()) {
                DiagramEnumCategory category = new DiagramEnumCategory();
                category.setCategoryName(categoryEntry.getKey());
                category.setViews(categoryEntry.getValue());
                category.setIconUrl(parseCategoryIconUrl(fileUrl, category.getCategoryName()));
                categories.add(category);
            }
            DiagramEnumGroup group = new DiagramEnumGroup();
            group.setGroup(entry.getKey());
            group.setCategories(categories);
            if ("常规图表".equals(group.getGroup())) {
                Collections.sort(group.getCategories(), new Comparator<DiagramEnumCategory>() {
                    @Override
                    public int compare(DiagramEnumCategory o1, DiagramEnumCategory o2) {
                        if (isFirst(o1)) {
                            return -1;
                        }
                        if (isFirst(o2)) {
                            return 1;
                        }
                        return 0;
                    }
                    private boolean isFirst(DiagramEnumCategory g) {
                        return "柱状图".equals(g.getCategoryName()) || "饼状图".equals(g.getCategoryName()) || "折线图".equals(g.getCategoryName());
                    }
                });
            }
            group.setIconUrl(parseGroupIconUrl(fileUrl, entry.getKey()));
            boolean add = !"装饰".equals(group.getGroup()) && !"媒体".equals(group.getGroup()) && !"交互".equals(group.getGroup());
            if (add) {
                groupList.add(group);
            }
        }
        return groupList;
    }



    private String parseCategoryIconUrl(String fileUrl, String category) {
        if (category == null) {
            return null;
        }
        switch (category) {
            case "柱状图" : return fileUrl + "/bigdata/v3/static/images/big-data/bar-logo.png";
            case "饼状图" : return fileUrl + "/bigdata/v3/static/images/big-data/pie-logo.png";
            case "散点图" : return fileUrl + "/bigdata/v3/static/images/big-data/scatter-logo.png";
            case "桑基图" : return fileUrl + "/bigdata/v3/static/images/big-data/sankey-logo.png";
            case "关系图" : return fileUrl + "/bigdata/v3/static/images/big-data/graph-logo.png";
            case "漏斗图" : return fileUrl + "/bigdata/v3/static/images/big-data/funnel-logo.png";
            case "折线图" : return fileUrl + "/bigdata/v3/static/images/big-data/line-logo.png";
            case "雷达图" : return fileUrl + "/bigdata/v3/static/images/big-data/radar-logo.png";
            case "地图" : return fileUrl + "/bigdata/v3/static/images/big-data/map-logo.png";
            case "字符云" : return fileUrl + "/bigdata/v3/static/images/big-data/wordCloud-logo.png";
            case "关系树图" : return fileUrl + "/bigdata/v3/static/images/big-data/treeMap-logo.png";
            case "表格" : return fileUrl + "/bigdata/v3/static/images/big-data/table-logo.png";
            case "标题" : return fileUrl + "/bigdata/v3/static/images/big-data/title-logo.png";
            case "其他" : return fileUrl + "/bigdata/v3/static/images/big-data/other-logo.png";
            case "数字" : return fileUrl + "/bigdata/v3/static/images/big-data/number-logo.png";
            default:
                return null;
        }
    }

    private String parseGroupIconUrl(String fileUrl, String group) {
        switch (group) {
            case "常规图表" : return  fileUrl + "/static/bigdata/images/chart.png";
            case "文字" : return  fileUrl + "/static/bigdata/images/text.png";
            default:
                return null;
        }
    }

    @ResponseBody
    @PostMapping("")
    public Response create(Screen create) {
        Screen screen = new Screen();
        screen.setName(create.getName());
        screen.setUnitId(getLoginInfo().getUnitId());
        screen.setCreationTime(new Date());
        screen.setModifyTime(new Date());
        screen.setId(UuidUtils.generateUuid());
        screen.setOrderType(OrderType.SELF.getOrderType());
        screen.setCreateUserId(getLoginInfo().getUserId());
        screenService.save(screen);
        ScreenStyle screenStyle = new ScreenStyle();
        screenStyle.setWidth(1920);
        screenStyle.setHeight(1080);
        screenStyle.setScreenId(screen.getId());
        screenStyle.setBackgroundColor("#072956");
        screenStyle.setId(UuidUtils.generateUuid());
        screenStyleService.save(screenStyle);
        //业务日志埋点  新增
        LogDto logDto=new LogDto();
        logDto.setBizCode("insert-screen");
        logDto.setDescription("大屏 "+screen.getName());
        logDto.setNewData(screen);
        logDto.setBizName("数据大屏");
        bigLogService.insertLog(logDto);

        String empty = servletContext.getRealPath("/static/bigdata/datav/images/empty.jpg");
        if (Files.exists(Paths.get(empty))) {
            String filePath = sysOptionRemoteService.findValue(Constant.FILE_PATH);
            try {
                FileUtils.copyFile(new File(empty), new File(ShotPath.of(filePath, screen.getUnitId(), screen.getId())));
            } catch (IOException e) {
                log.error("copy empty image error");
            }
        }
        return  Response.ok().data(screen.getId()).build();
    }

    @ResponseBody
    @RequestMapping(
            value = "/update/{screenId}",
            method = RequestMethod.POST
    )
    public Response doModify(@PathVariable("screenId") String screenId,
                             Screen screen) {
        Screen oldScreem = screenService.findOne(screenId);
        screenService.updateName(screenId, screen.getName());
        //业务日志埋点  修改
        LogDto logDto=new LogDto();
        logDto.setBizCode("update-screenName");
        logDto.setDescription("大屏名称为 "+screen.getName());
        logDto.setOldData(oldScreem.getName());
        logDto.setNewData(screen.getName());
        logDto.setBizName("数据大屏");
        bigLogService.updateLog(logDto);
        return Response.ok().build();
    }

    @ResponseBody
    @GetMapping("/order_type/{screenId}/{orderType}")
    public Response doUpdateOrderType(@PathVariable("screenId") String screenId,
                                      @PathVariable("orderType") Integer orderType) {
        Screen oldScreen = screenService.findOne(screenId);
        Screen screen=new Screen();
        EntityUtils.copyProperties(oldScreen,screen);
        screen.setOrderType(orderType);
        screenService.updateOrderType(screenId, orderType);
        //业务日志埋点  修改
        LogDto logDto=new LogDto();
        logDto.setBizCode("update-screenOrderType");
        logDto.setDescription("大屏"+oldScreen.getName()+"的权限");
        logDto.setOldData(oldScreen);
        logDto.setNewData(screen);
        logDto.setBizName("数据大屏");
        bigLogService.updateLog(logDto);

        return Response.ok().message("更新成功").build();
    }

    @ResponseBody
    @PostMapping("/delete")
    public Response doDelete(@RequestParam("ids[]") String[] ids) {
        try {
            screenService.batchDelete(ids);

        } catch (Exception e) {
            return Response.error().message("删除失败").build();
        }
        //
        String filePath = sysOptionRemoteService.findValue(Constant.FILE_PATH);
        Arrays.stream(ids).forEach(id->{
            try {
                Files.deleteIfExists(Paths.get(ShotPath.of(filePath, getLoginInfo().getUnitId(), id)));
            } catch (IOException e) {
                //ignore
            }
        });
        return Response.ok().message("删除成功").build();
    }

    @ResponseBody
    @PostMapping("/create-from-template")
    public Response doCreateFromTemplate(@RequestParam("templateId") String templateId,
                                         @RequestParam("name") String name) {
        LoginInfo loginInfo = getLoginInfo();
        String screenId = screenService.createFromLibrary(templateId, name, loginInfo.getUnitId(), loginInfo.getUserId());
        String empty = servletContext.getRealPath("/static/bigdata/datav/images/full-template.png");
        if (Files.exists(Paths.get(empty))) {
            String filePath = sysOptionRemoteService.findValue(Constant.FILE_PATH);
            try {
                FileUtils.copyFile(new File(empty), new File(ShotPath.of(filePath, getLoginInfo().getUnitId(), screenId)));
            } catch (IOException e) {
                log.error("copy empty image error");
            }
        }
        return Response.ok().data(screenId).build();
    }
}
