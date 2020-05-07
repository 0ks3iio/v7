package net.zdsoft.bigdata.datav.action;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.entity.AbstractDatabase;
import net.zdsoft.bigdata.data.service.ApiService;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.service.DatabaseService;
import net.zdsoft.bigdata.data.service.NosqlDatabaseService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datav.DataVStringUtil;
import net.zdsoft.bigdata.datav.dto.DiagramLevel;
import net.zdsoft.bigdata.datav.entity.*;
import net.zdsoft.bigdata.datav.enu.ArrayEnum;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.Root;
import net.zdsoft.bigdata.datav.render.crete.custom.ImageOption;
import net.zdsoft.bigdata.datav.service.*;
import net.zdsoft.bigdata.datav.vo.*;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author shenke
 * @since 2018/10/11 15:00
 */
@Controller
@RequestMapping("/bigdata/datav/diagram/config")
public class DiagramParameterController extends BigdataBaseAction {

    @Resource
    private DiagramParameterService diagramParameterService;
    @Resource
    private DiagramParameterGroupService diagramParameterGroupService;
    @Resource
    private DiagramParameterSelectService diagramParameterSelectService;
    @Resource
    private DiagramParameterArrayService diagramParameterArrayService;
    @Resource
    private DiagramContrastService diagramContrastService;
    @Resource
    private ApiService apiService;
    @Resource
    private DatabaseService databaseService;
    @Resource
    private DiagramService diagramService;
    @Resource
    private DiagramElementService diagramElementService;
    @Resource
    private DiagramElementContrastService diagramElementContrastService;
    @Resource
    private InteractionActiveService interactionActiveService;
    @Resource
    private InteractionBindingService interactionBindingService;
    @Resource
    private InteractionItemService interactionItemService;
    @Resource
    private ScreenInteractionElementService screenInteractionElementService;
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Resource
    private NosqlDatabaseService nosqlDatabaseService;
    @Resource
    private BigLogService bigLogService;

    /**
     * 获取某个screen的某个diagram的Config
     */
    @GetMapping(
            value = "/{id}"
    )
    public String doGetDiagramConfig(@PathVariable("id") String diagramId,
                                     ModelMap model) {
        Diagram diagram = diagramService.findOne(diagramId);

        //组件
        List<DiagramElementContrast> elementContrasts = diagramElementContrastService.getElementContrastsByRootDiagramType(diagram.getDiagramType());
        model.addAttribute("elementContrasts", elementContrasts);
        Map<Integer, String> elementTypeNameMap =
                elementContrasts.stream().collect(Collectors.toMap(DiagramElementContrast::getElementDiagramType, DiagramElementContrast::getName));

        //parameter
        List<DiagramParameter> diagramParameters = diagramParameterService.getOrDefault(diagramId, diagram.getDiagramType());
        List<DiagramParameterGroupVo> diagramParameterGroupVos = groupVos(diagram, diagramParameters);
        List<DiagramElement> elements = diagramElementService.getElementsByDiagramId(diagramId);
        List<ElementVo> elementVos = new ArrayList<>(elements.size());
        Map<String, AbstractDiagram> diagramMap = new HashMap<>(elements.size() + 1);
        diagramMap.put(diagram.getId(), diagram);
        Map<String, List<DiagramParameterGroupVo>> parameterVoMap = new HashMap<>(elements.size() + 1);
        parameterVoMap.put(diagramId, diagramParameterGroupVos);
        for (DiagramElement element : elements) {
            List<DiagramParameter> elementParameter = diagramParameterService.getOrDefault(element.getId(), element.getDiagramType());
            parameterVoMap.put(element.getId(), groupVos(element, elementParameter));
            diagramMap.put(element.getId(), element);
            ElementVo elementVo = new ElementVo();
            elementVo.setElementId(element.getId());
            elementVo.setName(elementTypeNameMap.get(element.getDiagramType()));
            elementVos.add(elementVo);
        }
        model.addAttribute("diagramGroupMap", parameterVoMap);
        model.addAttribute("diagramMap", diagramMap);
        model.addAttribute("elementVos", elementVos);
        model.addAttribute("rootDiagramId", diagramId);

        if (DiagramEnum.SINGLE_IMAGE.getType().equals(diagram.getDiagramType())
                || DiagramEnum.SHUFFLING_IMAGE.getType().equals(diagram.getDiagramType())) {
            List<ImageOption> imageOptions = JSONObject.parseArray(diagram.getDatasourceValueJson(), ImageOption.class);
            model.addAttribute("imageOptions", imageOptions);
            model.addAttribute("fileUrl", sysOptionRemoteService.getFileUrl(getRequest().getServerName()));
        }

        interaction(diagram, model);

        //数据源
        model.addAttribute("datasourceTypes", Arrays.asList(DataSourceType.values()));
        model.addAttribute("apis", apiService.findApisByUnitId(getLoginInfo().getUnitId()));
        String currentUnitId = getLoginInfo().getUnitId();
        List<AbstractDatabase> databases = new ArrayList<>();
        databases.addAll(nosqlDatabaseService.findNosqlDatabasesByUnitIdAndTypes(currentUnitId));
        databases.addAll(databaseService.findDatabasesByUnitId(currentUnitId));
        model.addAttribute("databases", databases);
        return "/bigdata/datav/datav-create-diagram-config.ftl";
    }

    /**
     * 加载交互组件
     */
    private void interaction(Diagram diagram, ModelMap model) {
        //是否激活
        Active active = interactionActiveService.isActice(diagram.getId());
        model.addAttribute("active", Active.enable.equals(active));
        //
        if (Objects.isNull(active) || Active.disable.equals(active)) {
            List<InteractionItem> interactionItems = interactionItemService.getInteractionItemsByDiagramType(diagram.getDiagramType());
            model.addAttribute("interactionItems", interactionItems);
        } else {
            List<InteractionBinding> bindings = interactionBindingService.getBindingsByElementId(diagram.getId());
            model.addAttribute("interactionItems", bindings);
        }
    }

    private List<DiagramParameterGroupVo> groupVos(AbstractDiagram diagram, List<DiagramParameter> diagramParameters) {
        Map<String, DiagramParameter> configParameterMap = new HashMap<>();
        int maxArrayCounter = 1;
        for (DiagramParameter diagramParameter : diagramParameters) {
            configParameterMap.put(diagramParameter.getKey() + diagramParameter.getArrayName(), diagramParameter);
            if (StringUtils.isNotBlank(diagramParameter.getArrayName())) {
                String maxCounterStr = diagramParameter.getArrayName().replaceAll("[^0-9]", "");
                maxArrayCounter = Math.max(maxArrayCounter, NumberUtils.toInt(maxCounterStr));
            }
        }
        List<DiagramParameterGroup> groups = diagramParameterGroupService.getGroupsByDiagramType(diagram.getDiagramType());
        List<DiagramParameterGroupVo> groupVos = new ArrayList<>();
        Map<String, LinkedHashSet<DiagramParameterCategoryVo>> categoryVoMap = new HashMap<>();
        Map<String, LinkedHashSet<DiagramParameterVo>> categoryParameterMap = new HashMap<>();
        Map<String, List<DiagramParameterVo>> groupParameterMap = new HashMap<>();
        for (DiagramParameterGroup group : groups) {
            if (Root.ROOT.getRoot().equals(group.getRoot())) {
                DiagramParameterGroupVo groupVo = new DiagramParameterGroupVo();
                groupVo.setGroupName(group.getGroupName());
                groupVo.setGroupKey(group.getGroupKey());
                //
                if (ArrayEnum.TRUE.getArray().equals(group.getArray())) {
                    groupVo.setArray(Boolean.TRUE);

                    List<DiagramParameterArray> diagramParameterArrays =
                            diagramParameterArrayService.getDiagramParameterArraysByGroupKeyAndDiagramType(
                                    group.getGroupKey(), diagram.getDiagramType());
                    List<DiagramParameterCategoryVo> arrayCategoryVos = new ArrayList<>();
                    if (!diagramParameterArrays.isEmpty()) {
                        for (int i = 0, length = maxArrayCounter; i < length; i++) {
                            DiagramParameterCategoryVo arrayCategoryVo = new DiagramParameterCategoryVo();
                            arrayCategoryVo.setCategoryName(group.getArrayNamePrefix() + (i + 1));
                            arrayCategoryVo.setArray(Boolean.TRUE);
                            List<DiagramParameterVo> arrayParameterVos = new ArrayList<>(length);
                            for (DiagramParameterArray diagramParameterArray : diagramParameterArrays) {
                                DiagramParameterVo arrayParameterVo = new DiagramParameterVo();
                                arrayParameterVo.setKey(diagramParameterArray.getKey());
                                arrayParameterVo.setValue(diagramParameterArray.getDefaultValue());
                                arrayParameterVo.setName(diagramParameterArray.getName());
                                arrayParameterVo.setValueType(diagramParameterArray.getValueType());
                                arrayParameterVo.setSelects(diagramParameterSelectService.getSelectsByValueType(diagramParameterArray.getValueType()));
                                DiagramParameter configParameter;
                                if ((configParameter = configParameterMap.get(diagramParameterArray.getKey() + arrayCategoryVo.getCategoryName())) != null) {
                                    arrayParameterVo.setValue(configParameter.getValue());
                                }
                                arrayParameterVos.add(arrayParameterVo);
                            }
                            arrayCategoryVo.setDiagramParameterVos(arrayParameterVos);
                            arrayCategoryVos.add(arrayCategoryVo);
                        }
                    }
                    groupVo.setDiagramParameterCategoryVos(arrayCategoryVos);
                }
                groupVos.add(groupVo);
            } else {
                DiagramParameterVo parameterVo = new DiagramParameterVo();
                parameterVo.setName(group.getName());
                DiagramParameter config;
                //key + null
                if ((config = configParameterMap.get(group.getKey() + group.getArrayNamePrefix())) != null) {
                    parameterVo.setValue(config.getValue());
                } else {
                    parameterVo.setValue(group.getDefaultValue());
                }
                parameterVo.setValueType(group.getValueType());
                parameterVo.setSelects(diagramParameterSelectService.getSelectsByValueType(group.getValueType()));
                parameterVo.setKey(group.getKey());
                if (group.getCategory() != null) {
                    DiagramParameterCategoryVo categoryVo = new DiagramParameterCategoryVo();
                    categoryVo.setCategoryName(group.getCategory());
                    categoryVoMap.computeIfAbsent(group.getGroupKey(), k -> new LinkedHashSet<>()).add(categoryVo);
                    categoryParameterMap.computeIfAbsent(group.getGroupKey() + group.getCategory(), k -> new LinkedHashSet<>()).add(parameterVo);
                } else {
                    groupParameterMap.computeIfAbsent(group.getGroupKey(), k -> new ArrayList<>()).add(parameterVo);
                }
            }

        }
        for (Map.Entry<String, LinkedHashSet<DiagramParameterCategoryVo>> categoryEntry : categoryVoMap.entrySet()) {
            for (DiagramParameterCategoryVo categoryParameterEntry : categoryEntry.getValue()) {
                Set<DiagramParameterVo> tem;
                if ((tem = categoryParameterMap.get(categoryEntry.getKey() + categoryParameterEntry.getCategoryName())) != null) {
                    categoryParameterEntry.setDiagramParameterVos(new ArrayList<>(tem));
                }
            }
        }

        for (DiagramParameterGroupVo groupVo : groupVos) {
            if (Objects.isNull(groupVo.getArray()) || Boolean.FALSE.equals(groupVo.getArray())) {
                Set<DiagramParameterCategoryVo> tem;
                if ((tem = categoryVoMap.get(groupVo.getGroupKey())) != null) {
                    groupVo.setDiagramParameterCategoryVos(new ArrayList<>(tem));
                }
                groupVo.setDiagramParameterVos(groupParameterMap.get(groupVo.getGroupKey()));
            }
        }
        return groupVos;
    }

    @ResponseBody
    @PostMapping(
            value = "/update/{diagramId}"
    )
    public Response doUpdateConfig(@PathVariable("diagramId") String diagramId,
                                   ModifyDiagramParameterVo diagramParameterVo) {

        if (StringUtils.isNotBlank(diagramParameterVo.getElementId())) {
            diagramId = diagramParameterVo.getElementId();
        }
        //宽高问题
        if ("width".equalsIgnoreCase(diagramParameterVo.getKey())
                || "height".equalsIgnoreCase(diagramParameterVo.getKey())
                || "x".equalsIgnoreCase(diagramParameterVo.getKey())
                || "y".equalsIgnoreCase(diagramParameterVo.getKey())) {
            diagramParameterVo.setValue(Double.valueOf(diagramParameterVo.getValue()).intValue() + "");
        }
        if (StringUtils.isBlank(diagramParameterVo.getArrayName())) {
            DiagramParameter oldDiagramParameter=diagramParameterService.findByDiagramIdAndKey(diagramId,diagramParameterVo.getKey());
            diagramParameterService.updateDiagramParameter(diagramId, diagramParameterVo.getKey(), diagramParameterVo.getValue());
            //业务日志埋点  修改
            LogDto logDto=new LogDto();
            logDto.setBizCode("update-diagramParameter");
            logDto.setDescription("大屏图表无系列参数");
            logDto.setOldData(oldDiagramParameter);
            DiagramParameter newDiagramParameter=diagramParameterService.findByDiagramIdAndKey(diagramId,diagramParameterVo.getKey());
            logDto.setNewData(newDiagramParameter);
            logDto.setBizName("数据大屏");
            bigLogService.updateLog(logDto);

        } else {
            DiagramParameter oldDiagramParameter=diagramParameterService.findByDiagramIdAndKeyAndArrayName(diagramId,diagramParameterVo.getKey(),diagramParameterVo.getArrayName());
            diagramParameterService.updateDiagramParameter(diagramId, diagramParameterVo.getKey(), diagramParameterVo.getArrayName(), diagramParameterVo.getValue());
            //业务日志埋点  修改
            LogDto logDto=new LogDto();
            logDto.setBizCode("update-diagramParameter");
            logDto.setDescription("大屏图表有系列参数");
            logDto.setOldData(oldDiagramParameter);
            DiagramParameter newDiagramParameter=diagramParameterService.findByDiagramIdAndKeyAndArrayName(diagramId,diagramParameterVo.getKey(),diagramParameterVo.getArrayName());
            logDto.setNewData(newDiagramParameter);
            logDto.setBizName("数据大屏");
            bigLogService.updateLog(logDto);
        }


        return Response.ok().message("更新成功").build();
    }

    @ResponseBody
    @GetMapping(
            value = "/delete/{diagramId}/{arrayName}/array"
    )
    public Response doDeleteConfigArray(@PathVariable("diagramId") String diagramId,
                                        @PathVariable("arrayName") String arrayName,
                                        @RequestParam(value = "elementId", required = false) String elementId) {
        if (StringUtils.isNotBlank(elementId)) {
            diagramId = elementId;
        }
        diagramParameterService.deleteByDiagramIdAndArrayName(diagramId, arrayName);
        return Response.ok().build();
    }

    @ResponseBody
    @PostMapping(
            value = "/{diagramId}"
    )
    public Response doAddConfigArray(@PathVariable("diagramId") String diagramId,
                                     @RequestBody ConfigArrayVo configArrayVo) {
        if (StringUtils.isNotBlank(configArrayVo.getElementId())) {
            diagramId = configArrayVo.getElementId();
        }
        List<DiagramParameter> diagramParameterList = new ArrayList<>();
        for (ConfigArrayItem item : configArrayVo.getConfigArrayItems()) {
            DiagramParameter parameter = new DiagramParameter();
            parameter.setDiagramId(diagramId);
            parameter.setGroupKey(configArrayVo.getGroupKey());
            parameter.setId(UuidUtils.generateUuid());
            parameter.setValue(item.getValue());
            parameter.setKey(item.getKey());
            parameter.setArrayName(configArrayVo.getArrayName());
            diagramParameterList.add(parameter);
        }
        diagramParameterService.saveAll(diagramParameterList.toArray(new DiagramParameter[0]));
        return Response.ok().message("新增成功").build();
    }

    //----------------------------------组件部分----------------------------------//

    @ResponseBody
    @PostMapping("/{diagramId}/{elementDiagramType}")
    public Response doAddElement(@PathVariable("diagramId") String diagramId,
                                 @PathVariable("elementDiagramType") Integer elementDiagramType) {
        DiagramElementContrast elementContrast = diagramElementContrastService.getElementByElementDiagramType(elementDiagramType);
        DiagramElement element = new DiagramElement();
        element.setDiagramId(diagramId);
        element.setDatasourceType(DataSourceType.STATIC.getValue());
        element.setDatasourceValueJson(DataVStringUtil.compressOfBlank(elementContrast.getDemoData()));
        element.setId(UuidUtils.generateUuid());
        element.setDiagramType(elementDiagramType);
        diagramElementService.save(element);
        return Response.ok().data(element.getId()).message("新增成功").build();
    }

    @ResponseBody
    @GetMapping("/delete/{diagramId}/{elementId}")
    public Response doDeleteElement(@PathVariable("diagramId") String diagramId,
                                    @PathVariable("elementId") String elementId) {
        diagramElementService.delete(elementId);
        return Response.ok().message("删除成功").build();
    }

    @GetMapping("/{diagramId}/{elementId}")
    public String getElementConfig(@PathVariable("diagramId") String diagramId,
                                   @PathVariable("elementId") String elementId,
                                   ModelMap model) {
        DiagramElement element = diagramElementService.findOne(elementId);
        List<DiagramParameter> elementParameters = diagramParameterService.getOrDefault(elementId, element.getDiagramType());
        List<DiagramParameterGroupVo> groupVos = groupVos(element, elementParameters);
        model.addAttribute("elementGroupVos", groupVos);
        model.addAttribute("element", element);
        model.addAttribute("datasourceTypes", Arrays.asList(DataSourceType.values()));
        model.addAttribute("apis", apiService.findApisByUnitId(getLoginInfo().getUnitId()));
        model.addAttribute("databases", databaseService.findDatabasesByUnitId(getLoginInfo().getUnitId()));
        model.addAttribute("rootDiagramId", diagramId);
        return "/bigdata/datav/datav-create-element.ftl";
    }

    @ResponseBody
    @GetMapping("/{screenId}/max-level")
    public Response doGetMaxLevel(@PathVariable("screenId") String screenId) {
        List<Diagram> diagrams = diagramService.getDiagramsByScreenId(screenId);
        if (diagrams.isEmpty()) {
            return Response.ok().data(3).build();
        } else {
            Integer maxLevel = diagramParameterService.getMaxLevel(diagrams.stream()
                    .map(Diagram::getId).toArray(String[]::new));
            if (maxLevel == null || new Integer(0).equals(maxLevel)) {
                maxLevel = 3;
            }
            return Response.ok().data(maxLevel).build();
        }
    }

    @ResponseBody
    @GetMapping("/{screenId}/modify-level")
    public Response modifyLevel(@PathVariable("screenId") String screenId,
                                @RequestParam("diagramId") String diagramId,
                                @RequestParam("top") boolean isTop) {
        List<DiagramParameter> levelParameters = diagramParameterService.getLevelParameters(screenId);
        Optional<DiagramParameter> current = levelParameters.stream().filter(e->e.getDiagramId().equals(diagramId)).findFirst();
        if (!current.isPresent()) {
            return Response.error().message("图表已被删除,请刷新页面重试").build();
        }
        int currentLevel = StringUtils.isBlank(current.get().getValue()) ? 0 : Integer.valueOf(current.get().getValue());

        //TODO 这里以后还要获取分组的数据，结合分组的层级动态设置

        List<DiagramParameter> parameters = levelParameters.stream().filter(e->{
            if (!e.getDiagramId().equals(diagramId)) {
                if (StringUtils.isNotBlank(e.getValue())) {
                    if (isTop) {
                        return Integer.valueOf(e.getValue()) > currentLevel;
                    } else {
                        return Integer.valueOf(e.getValue()) < currentLevel;
                    }
                }
            }
            return false;
        }).sorted(Comparator.comparing(o -> Integer.valueOf(o.getValue()))).collect(Collectors.toList());
        if (!parameters.isEmpty()) {
            int step = isTop ? 1 : -1;
            if (isTop) {
                int max = Integer.valueOf(parameters.get(parameters.size() - 1).getValue());
                current.get().setValue(String.valueOf(max));
            } else {
                int min = Integer.valueOf(parameters.get(0).getValue());
                current.get().setValue(String.valueOf(min));
            }
            for (DiagramParameter e : parameters) {
                e.setValue(String.valueOf(Integer.valueOf(e.getValue()) + step * -1));
            }
            parameters.add(current.get());
            diagramParameterService.saveAll(parameters.toArray(new DiagramParameter[0]));

            List<DiagramLevel> levels = parameters.stream().map(DiagramLevel::convert).collect(Collectors.toList());
            return Response.ok().data(levels).build();
        } else {
            //do nothing
        }

        return Response.ok().message("").build();
    }

    @ResponseBody
    @RequestMapping("/{screenId}/up-down-level")
    public Response upDownLevel(@PathVariable("screenId") String screenId,
                                @RequestParam("diagramId") String diagramId,
                                @RequestParam("up") boolean up) {
        List<DiagramParameter> levelParameters = diagramParameterService.getLevelParameters(screenId);
        //当前元素
        Optional<DiagramParameter> current = levelParameters.stream().filter(e->e.getDiagramId().equals(diagramId)).findFirst();
        DiagramParameter oldCurrent = new DiagramParameter();
        EntityUtils.copyProperties(current.get(),oldCurrent);
        if (!current.isPresent()) {
            return Response.error().message("图表已被删除,请刷新页面重试").build();
        }
        int currentLevel = StringUtils.isBlank(current.get().getValue()) ? 0 : Integer.valueOf(current.get().getValue());
        Stream<DiagramParameter> stream = levelParameters.stream()
                .filter(e-> {
                    if (up) {
                        return Integer.valueOf(e.getValue()) > currentLevel;
                    }
                    return Integer.valueOf(e.getValue()) < currentLevel;
                })
                .sorted(Comparator.comparing(o -> Integer.valueOf(o.getValue())));
        //要交换的元素
        Optional<DiagramParameter> target = up ? stream.findFirst()
                : stream.max(Comparator.comparing(o -> Integer.valueOf(o.getValue())));
        DiagramParameter oldTarget = new DiagramParameter();
        EntityUtils.copyProperties(target.get(),oldTarget);
        if (target.isPresent()) {
            current.get().setValue(target.get().getValue());
            target.get().setValue(currentLevel + "");
            DiagramParameter[] parameters = new DiagramParameter[]{current.get(), target.get()};
            diagramParameterService.saveAll(parameters);
            Diagram diagram = diagramService.findOne(diagramId);
            DiagramContrast diagramContrast = diagramContrastService.getDiagramContrastByDiagramType(diagram.getDiagramType());
            Diagram targetDiagram = diagramService.findOne(target.get().getDiagramId());
            DiagramContrast targetDiagramContrast = diagramContrastService.getDiagramContrastByDiagramType(targetDiagram.getDiagramType());

            //业务日志埋点  修改
            LogDto logDto=new LogDto();
            logDto.setBizCode("update-upDownLevel");
            logDto.setDescription("大屏图表 "+diagramContrast.getName()+" 的层级");
            HashMap<String, DiagramParameter> map1 = new HashMap<>();
            map1.put(diagramContrast.getName(),oldCurrent);
            map1.put(targetDiagramContrast.getName(),oldTarget);
            logDto.setOldData(map1);

            HashMap<String, DiagramParameter> map2 = new HashMap<>();
            map2.put(diagramContrast.getName(),current.get());
            map2.put(targetDiagramContrast.getName(),target.get());
            logDto.setNewData(map2);
            logDto.setBizName("数据大屏");
            bigLogService.updateLog(logDto);
            return Response.ok().data(Arrays.stream(parameters).map(DiagramLevel::convert).collect(Collectors.toList())).build();
        }
        return Response.ok().build();
        //
    }

    @ResponseBody
    @RequestMapping("/{screenId}/lock")
    public Response upDownLevel(@PathVariable("screenId") String screenId,
                                @RequestParam("diagramId[]") String[] diagramId,
                                @RequestParam("lock") boolean lock) {

        diagramService.lock(diagramId, lock);
        //业务日志埋点  修改
        LogDto logDto=new LogDto();
        logDto.setBizCode("update-diagramLock");
        logDto.setDescription("图表锁定状态");
        logDto.setOldData(diagramId);
        logDto.setNewData(diagramId);
        logDto.setBizName("数据大屏");
        bigLogService.updateLog(logDto);

        return Response.ok().build();
    }

    @ResponseBody
    @PostMapping("/interaction/{elementId}/active")
    public Response doActiveInteraction(@PathVariable("elementId") String elementId) {
        if (Active.enable.equals(interactionActiveService.isActice(elementId))) {
            return Response.ok().message("ok").build();
        }
        interactionActiveService.activeForElement(elementId);
        return Response.ok().message("ok").build();
    }

    @ResponseBody
    @GetMapping("/interaction/{elementId}/active")
    public Response doCancelActiveInteraction(@PathVariable("elementId") String elementId,
                                              @RequestParam("screenId") String screenId) {
        interactionActiveService.cancelActive(screenId, elementId);
        return Response.ok().build();
    }

    //TODO add interaction key
}
