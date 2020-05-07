package net.zdsoft.bigdata.data.action;

import com.google.common.collect.Maps;
import net.zdsoft.bigdata.data.ChartBusinessType;
import net.zdsoft.bigdata.data.OrderType;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.entity.*;
import net.zdsoft.bigdata.data.enu.BatchOperateType;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.metadata.entity.Folder;
import net.zdsoft.bigdata.metadata.entity.FolderDetail;
import net.zdsoft.bigdata.metadata.entity.FolderEx;
import net.zdsoft.bigdata.metadata.service.FolderDetailService;
import net.zdsoft.bigdata.metadata.service.FolderService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/bigdata/multireport")
public class MultiReportController extends BigdataBaseAction {

    @Resource
    private MultiReportService multiReportService;
    @Resource
    private MultiReportDetailService multiReportDetailService;
    @Resource
    private ChartService chartService;
    @Resource
    private FolderService folderService;
    @Resource
    private FolderDetailService folderDetailService;
    @Resource
    private TagRelationService tagRelationService;
    @Resource
    private TagService tagService;
    @Resource
    private BigLogService bigLogService;

    @Resource
    DataModelFavoriteService dataModelFavoriteService;

    @RequestMapping("/board")
    public String board(ModelMap map) {
        map.put("type", MultiReport.BOARD);
        Set<GraphConfigurationController.GraphIndexTag> graphIndexTags;
        // (当前单位新增的)
        List<Tag> tags = tagService.findTagsByUnitIdAndTagType(getLoginInfo()
                .getUnitId(), ChartBusinessType.DATA_BOARD.getBusinessType()
                .shortValue());

        graphIndexTags = tags
                .parallelStream()
                .map(tag -> GraphConfigurationController.GraphIndexTag.create(
                        tag.getId(), tag.getTagName()))
                .collect(Collectors.toSet());

        // 订阅列表自带的
        /*
         * List<MultiReport> resultList = multiReportService
         * .findMultiReportsByUnitIdAndType(getLoginInfo().getUnitId(),
         * MultiReport.BOARD, null, null); String[] chartIdArray =
         * resultList.stream().map(MultiReport::getId).toArray(String[]::new);
         * if (ArrayUtils.isNotEmpty(chartIdArray)) { List<TagRelation>
         * tagRelations = tagRelationService.getByBusinessId(chartIdArray);
         * graphIndexTags.addAll(tagRelations.parallelStream().map(tagRelation
         * -> { return
         * GraphConfigurationController.GraphIndexTag.create(tagRelation
         * .getTagId(), tagRelation.getTagName());
         * }).collect(Collectors.toSet())); }
         */
        map.put("tags", graphIndexTags);// .addObject("charts", chartVOList);

        return "/bigdata/multireport/multiReportIndex.ftl";
    }

    @RequestMapping("/report")
    public String report(ModelMap map) {
        map.put("type", MultiReport.REPORT);
        Set<GraphConfigurationController.GraphIndexTag> graphIndexTags;
        // (当前单位新增的)
        List<Tag> tags = tagService.findTagsByUnitIdAndTagType(getLoginInfo()
                .getUnitId(), ChartBusinessType.DATA_REPORT.getBusinessType()
                .shortValue());

        graphIndexTags = tags
                .parallelStream()
                .map(tag -> GraphConfigurationController.GraphIndexTag.create(
                        tag.getId(), tag.getTagName()))
                .collect(Collectors.toSet());
        map.put("tags", graphIndexTags);// .addObject("charts", chartVOList);
        return "/bigdata/multireport/multiReportIndex.ftl";
    }

    @RequestMapping("/list")
    public String list(int type, String reportName,
                       @RequestParam(value = "tags", required = false) String ts,
                       ModelMap map) {

        String[] tagIdArray = null;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(ts)) {
            tagIdArray = StringUtils.trim(ts).split(",");
        }
        List<MultiReport> resultList = multiReportService
                .findMultiReportsByUnitIdAndType(getLoginInfo().getUnitId(),
                        type, tagIdArray, reportName);

        // 获取每个图表标记的标签
        Map<String, List<String>> tagNameMap = new HashMap<>(resultList.size());
        tagRelationService.getByBusinessId(
                resultList.stream().map(MultiReport::getId)
                        .toArray(String[]::new)).forEach(
                tagRelation -> tagNameMap.computeIfAbsent(
                        tagRelation.getBusinessId(), k -> new ArrayList<>())
                        .add(tagRelation.getTagName()));
        resultList.forEach(e -> e.setTagNameList(tagNameMap.get(e.getId())));
        map.put("type", type);
        map.put("resultList", resultList);
        return "/bigdata/multireport/multiReportList.ftl";
    }

    @RequestMapping("/bi/list")
    public String list4Bi(int type, String reportName, ModelMap map, HttpServletRequest request) {
        Pagination page = createPagination(request);
        page.setPageSize(10);
        List<MultiReport> resultList = multiReportService.findMultiReports(getLoginInfo().getUnitId(), getLoginInfo().getUserId(), reportName, type, page);
        sendPagination(request, map,"2", page);
        // 获取每个图表标记的标签
        Map<String, List<String>> tagNameMap = new HashMap<>(resultList.size());
        tagRelationService.getByBusinessId(
                resultList.stream().map(MultiReport::getId)
                        .toArray(String[]::new)).forEach(
                tagRelation -> tagNameMap.computeIfAbsent(
                        tagRelation.getBusinessId(), k -> new ArrayList<>())
                        .add(tagRelation.getTagName()));
        resultList.forEach(e -> e.setTagNameList(tagNameMap.get(e.getId())));
        map.put("type", type);
        map.put("resultList", resultList);
        return "/bigdata/multireport/bi/multiReportList4Bi.ftl";
    }

    @RequestMapping("/add")
    public String add(int type, ModelMap map) {
        MultiReport multiReport = null;
        multiReport = new MultiReport();
        multiReport.setType(type);
        multiReport.setUnitId(getLoginInfo().getUnitId());
        // Integer maxOrderId = multiReportService.getMaxOrderIdByUnitIdAndType(
        // getLoginInfo().getUnitId(), type);
        // if (maxOrderId == null)
        // maxOrderId = 0;
        // if (maxOrderId >= 999)
        // maxOrderId = 0;
        // multiReport.setOrderId(++maxOrderId);

        map.put("multiReport", multiReport);
        return "/bigdata/multireport/multiReportAdd.ftl";
    }

    @RequestMapping("/edit")
    public String edit(String reportId, int type, ModelMap map) {
        MultiReport multiReport = multiReportService.findOne(reportId);
        // 标签
        map.put("tags", this.getTags(multiReport));
        map.put("orderTypes", OrderType.useValues());
        map.put("orderType", multiReport.getOrderType());
        map.put("multiReport", multiReport);
        Map<String, List<Folder>> folderMap = folderService
                .findAllFolderForDirectory();
        List<FolderEx> folderTree = folderService.findFolderTree();
        List<FolderDetail> folderDetails = folderDetailService.findListBy(
                "businessId", multiReport.getId());
        map.put("folderDetail", folderDetails.size() > 0 ? folderDetails.get(0)
                : new FolderDetail());
        map.put("folderMap", folderMap);
        map.put("folderTree", folderTree);
        map.put("tagType",
                multiReport.getType() == MultiReport.BOARD ? ChartBusinessType.DATA_BOARD
                        .getBusinessType() : ChartBusinessType.DATA_REPORT
                        .getBusinessType());
        map.put("type", multiReport.getType() == MultiReport.BOARD ? BatchOperateType.DATA_BOARD.getValue() : BatchOperateType.DATA_REPORT.getValue());

        return "/bigdata/multireport/multiReportEdit.ftl";
    }

    private List<GraphConfigurationController.GraphEditTag> getTags(
            MultiReport multiReport) {
        ChartBusinessType businessType = ChartBusinessType.DATA_BOARD;
        if (multiReport.getType() == MultiReport.REPORT) {
            businessType = ChartBusinessType.DATA_REPORT;
        }

        Map<String, TagRelation> tagRelationMap = org.apache.commons.lang3.StringUtils
                .isNotBlank(multiReport.getId()) ? tagRelationService
                .getByBusinessId(new String[]{multiReport.getId()}).stream()
                .collect(Collectors.toMap(TagRelation::getTagId, t -> t))
                : Maps.newHashMap();

        return tagService
                .findTagsByUnitIdAndTagType(getLoginInfo().getUnitId(),
                        businessType.getBusinessType().shortValue())
                .parallelStream()
                .map(tag -> {
                    GraphConfigurationController.GraphEditTag editTag = new GraphConfigurationController.GraphEditTag();
                    editTag.setId(tag.getId());
                    editTag.setName(tag.getTagName());
                    editTag.setSelected(tagRelationMap.containsKey(tag.getId()));
                    return editTag;
                }).collect(Collectors.toList());
    }

    @RequestMapping("/design")
    public String design(String reportId, Integer type, ModelMap map) {
        List<MultiReportDetail> reportDetailList = multiReportDetailService
                .findMultiReportDetailsByReportId(reportId);
        map.put("reportId", reportId);
        map.put("type", type);
        MultiReport multiReport = multiReportService.findOne(reportId);
        map.put("multiReport", multiReport);
        map.put("reportDetailList", reportDetailList);
        if (MultiReport.BOARD == type) {
            return "/bigdata/multireport/boardDesign.ftl";
        } else {
            return "/bigdata/multireport/reportDesign.ftl";
        }
    }

    @RequestMapping("/preview")
    public String preview(String reportId, Integer type, String containHeader,
                          ModelMap map) {
        MultiReport report = multiReportService.findOne(reportId);
        if (report == null) {
            if (type == 6)
                map.put("errorMsg", "该看板已经被删除");
            else
                map.put("errorMsg", "该报告已经被删除");
            return "/bigdata/v3/common/error.ftl";
        }
        List<MultiReportDetail> reportDetailList = multiReportDetailService
                .findMultiReportDetailsByReportId(reportId);
        map.put("reportId", reportId);
        map.put("type", type);
        map.put("containHeader", containHeader);
        MultiReport multiReport = multiReportService.findOne(reportId);
        map.put("multiReport", multiReport);
        map.put("reportDetailList", reportDetailList);
        if (MultiReport.BOARD == type) {
            return "/bigdata/multireport/boardPreview.ftl";
        } else {
            return "/bigdata/multireport/reportPreview.ftl";
        }
    }

    @RequestMapping("/component/set")
    public String componentSetting(Integer type, String reportId,
                                   String componentId, String businessType, ModelMap map) {
        MultiReportDetail component = null;
        if (StringUtils.isBlank(componentId)) {
            component = new MultiReportDetail();
            component.setReportId(reportId);
            component.setBusinessType(businessType);
            Integer maxOrderId = multiReportDetailService
                    .getMaxOrderIdByReportId(reportId);
            if (maxOrderId == null)
                maxOrderId = 0;
            if (maxOrderId >= 999)
                maxOrderId = 0;
            component.setOrderId(++maxOrderId);
        } else {
            component = multiReportDetailService.findOne(componentId);
        }
        map.put("component", component);
        map.put("componentId", componentId);
        map.put("reportId", reportId);
        map.put("businessType", businessType);
        map.put("type", type);
        return "/bigdata/multireport/componentSet.ftl";

    }

    @RequestMapping("/component/detail")
    public String componentDetail(Integer type, String componentId,
                                  String preview, Integer zIndex, ModelMap map) {
        MultiReportDetail component = multiReportDetailService
                .findOne(componentId);
        map.put("reportId", component.getReportId());
        map.put("component", component);
        map.put("type", type);
        map.put("zIndex", zIndex);
        map.put("preview", preview);
        if ("chart".equals(component.getBusinessType())) {
            return "/bigdata/multireport/chartDetail4c.ftl";
        } else if ("report".equals(component.getBusinessType())) {
            return "/bigdata/multireport/reportDetail4c.ftl";
        } else if ("multimodel".equals(component.getBusinessType())) {
            return "/bigdata/multireport/multimodelDetail4c.ftl";
        } else if ("richText".equals(component.getBusinessType())) {
            return "/bigdata/multireport/richTextDetail4c.ftl";
        }
        return "/bigdata/v3/common/404.ftl";
    }

    @RequestMapping("/component/delete")
    @ResponseBody
    public Response deleteComponent(String componentId) {
        try {
            String reportId = multiReportDetailService.findReportIdById(componentId);
            MultiReport oldMultiReport = multiReportService.findOne(reportId);
            String reportName = oldMultiReport.getType() == MultiReport.BOARD ? "看板" : "报告";

            MultiReportDetail oldMultiReportDetail = multiReportDetailService.findOne(componentId);
            String detailName = (oldMultiReportDetail.getBusinessName() == ""
                    || oldMultiReportDetail.getBusinessName() == null)
                    ? "富文本组件" : oldMultiReportDetail.getBusinessName();

            multiReportDetailService.delete(componentId);

            //业务日志埋点  删除
            LogDto logDto = new LogDto();
            logDto.setBizCode("delete-component");
            logDto.setDescription("数据" + reportName + "设计 " + detailName);
            logDto.setBizName("数据" + reportName + "设置");
            logDto.setOldData(oldMultiReportDetail);
            bigLogService.deleteLog(logDto);

            return Response.ok().message("删除组件成功").build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/component/richText/new")
    @ResponseBody
    public Response addRichText(String reportId) {
        try {
            MultiReportDetail component = new MultiReportDetail();
            component.setBusinessType("richText");
            component.setReportId(reportId);
            Integer maxOrderId = multiReportDetailService
                    .getMaxOrderIdByReportId(reportId);
            if (maxOrderId == null)
                maxOrderId = 0;
            if (maxOrderId >= 999)
                maxOrderId = 0;
            component.setOrderId(++maxOrderId);
            component.setId(UuidUtils.generateUuid());
            multiReportDetailService.save(component);
            //业务日志埋点  新增
            LogDto logDto = new LogDto();
            logDto.setBizCode("insert-richText");
            logDto.setDescription("数据报告设计 富文本组件");
            logDto.setNewData(component);
            logDto.setBizName("数据报告");
            bigLogService.insertLog(logDto);
            return Response.ok().message("添加富文本组件成功").build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/component/richText/update")
    @ResponseBody
    public Response updateRichText(String componentId, String content) {
        try {
            MultiReportDetail component = multiReportDetailService
                    .findOne(componentId);
            String oldContent = component.getContent();
            component.setContent(content);
            multiReportDetailService.update(component, component.getId(),
                    new String[]{"content"});
            //业务日志埋点  修改
            LogDto logDto = new LogDto();
            logDto.setBizCode("update-richText");
            logDto.setDescription("数据报告设计 富文本内容");
            logDto.setOldData(oldContent);
            logDto.setNewData(content);
            logDto.setBizName("数据报告设置");
            bigLogService.updateLog(logDto);

            return Response.ok().message("保存富文本成功").build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/component/save")
    @ResponseBody
    public Response addComponent(MultiReportDetail component) {
        try {
            if (StringUtils.isNotBlank(component.getId())) {
                MultiReport oldMultiReport = multiReportService.findOne(component.getReportId());
                String reportName = oldMultiReport.getType() == MultiReport.BOARD ? "看板" : "报告";
                MultiReportDetail oldMultiReportDetail = multiReportDetailService.findOne(component.getId());
                multiReportDetailService.delete(component.getId());
                multiReportDetailService.save(component);
                //业务日志埋点  修改
                LogDto logDto = new LogDto();
                logDto.setBizCode("update-optionParam");
                logDto.setDescription("数据" + reportName + "设计 " + oldMultiReportDetail.getBusinessName());
                logDto.setOldData(oldMultiReportDetail);
                logDto.setNewData(component);
                logDto.setBizName("数据" + reportName + "设置");
                bigLogService.updateLog(logDto);

                return Response.ok().message("修改组件成功").build();
            } else {
                component.setId(UuidUtils.generateUuid());
                multiReportDetailService.save(component);
                MultiReport oldMultiReport = multiReportService.findOne(component.getReportId());
                String reportName = oldMultiReport.getType() == MultiReport.BOARD ? "看板" : "报告";
                //业务日志埋点  新增
                LogDto logDto = new LogDto();
                logDto.setBizCode("insert-multiReportDetail");
                logDto.setDescription("数据" + reportName + "设计 " + component.getBusinessName());
                logDto.setNewData(component);
                logDto.setBizName("数据" + reportName + "设置");
                bigLogService.insertLog(logDto);

                return Response.ok().message("添加组件成功").build();
            }
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/component/update")
    @ResponseBody
    public Response updateComponent(MultiReportDetail component) {
        try {
            multiReportDetailService.updateMultiReportDetail(component);
            return Response.ok().message("组件设置成功").build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Response deleteMultiReport(String reportId) {
        try {
            MultiReport oldMultiReport = multiReportService.findOne(reportId);
            String reportName = oldMultiReport.getType() == MultiReport.BOARD ? "看板" : "报告";
            multiReportService.deleteMultiReport(reportId);
            //业务日志埋点  删除
            LogDto logDto = new LogDto();
            logDto.setBizCode("delete-multiReport");
            logDto.setDescription("数据" + reportName + " " + oldMultiReport.getName());
            logDto.setBizName("数据" + reportName + "设置");
            logDto.setOldData(oldMultiReport);
            bigLogService.deleteLog(logDto);

            return Response.ok().message("").build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/component/library")
    public String componentLibrary(String reportId, String businessType,
                                   String businessId, ModelMap map) {
        List<MultiReportDetail> detailList = multiReportDetailService
                .findMultiReportDetailsByReportId(reportId);
        Set<String> selectedBusinessIds = new java.util.HashSet<String>();
        for (MultiReportDetail detail : detailList) {
            selectedBusinessIds.add(detail.getBusinessId());
            if (StringUtils.isNotBlank(businessId)) {
                selectedBusinessIds.remove(businessId);
            }
        }
        if ("chart".equals(businessType)) {
            List<Chart> chartList = chartService
                    .getChartsByUnitIdAndBusinessTypeAndSort(getLoginInfo()
                            .getUnitId(), 1);
            List<Chart> resultList = new ArrayList<Chart>();

            for (Chart chart : chartList) {
                if (!selectedBusinessIds.contains(chart.getId())) {
                    resultList.add(chart);
                }
            }
            map.put("libraryList", resultList);
        } else if ("report".equals(businessType)) {
            List<Chart> chartList = chartService
                    .getChartsByUnitIdAndBusinessTypeAndSort(getLoginInfo()
                            .getUnitId(), 3);
            List<Chart> resultList = new ArrayList<Chart>();
            for (Chart chart : chartList) {
                if (!selectedBusinessIds.contains(chart.getId())) {
                    resultList.add(chart);
                }
            }
            map.put("libraryList", resultList);
        } else if ("multimodel".equals(businessType)) {
            List<DataModelFavorite> dataModelList = dataModelFavoriteService
                    .findAll(getLoginInfo().getUnitId());
            List<DataModelFavorite> resultList = new ArrayList<DataModelFavorite>();
            for (DataModelFavorite dataModel : dataModelList) {
                if (!selectedBusinessIds.contains(dataModel.getId())) {
                    resultList.add(dataModel);
                }
            }
            map.put("libraryList", resultList);
        }
        map.put("businessType", businessType);
        map.put("businessId", businessId);
        return "/bigdata/multireport/componentLibrary.ftl";
    }

    @RequestMapping("/save")
    @ResponseBody
    public Response saveReport(MultiReport multiReport) {
        try {
            multiReport.setCreatorUserId(getLoginInfo().getUserId());
            multiReport.setOrderType(OrderType.SELF.getOrderType());
            multiReportService.saveMultiReport(multiReport);

            return Response.ok().message("保存成功").build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/saveMultiReport")
    @ResponseBody
    public Response saveMultiReport(
            MultiReport multiReport,
            @RequestParam(value = "tags[]", required = false) String[] tags,
            @RequestParam(value = "orderUnit[]", required = false) String[] orderUnits,
            @RequestParam(value = "orderTeacher[]", required = false) String[] orderTeachers) {
        try {
            multiReportService.saveMultiReport(multiReport, tags, orderUnits, orderTeachers);
            return Response.ok().message("保存成功").build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

}
