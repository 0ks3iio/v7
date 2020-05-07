package net.zdsoft.bigdata.metadata.action;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import net.zdsoft.api.base.entity.eis.ApiDataSet;
import net.zdsoft.api.base.service.ApiDataSetService;
import net.zdsoft.api.base.service.ApiEntityService;
import net.zdsoft.api.base.service.ApiInterfaceTypeService;
import net.zdsoft.bigdata.base.entity.DwRank;
import net.zdsoft.bigdata.base.entity.PropertyTag;
import net.zdsoft.bigdata.base.entity.PropertyTopic;
import net.zdsoft.bigdata.base.service.BgDwRankService;
import net.zdsoft.bigdata.base.service.BgPropertyTopicService;
import net.zdsoft.bigdata.base.service.PropertyTagService;
import net.zdsoft.bigdata.daq.data.component.BizOperationLogCollector;
import net.zdsoft.bigdata.daq.data.entity.BizOperationLog;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.entity.DataModel;
import net.zdsoft.bigdata.data.entity.Database;
import net.zdsoft.bigdata.extend.data.entity.FormSet;
import net.zdsoft.bigdata.extend.data.service.FormSetService;
import net.zdsoft.bigdata.metadata.vo.RelatedTableVo;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.manager.datasource.IDataSourceUtils;
import net.zdsoft.bigdata.data.service.DataModelParamService;
import net.zdsoft.bigdata.data.service.DataModelService;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobService;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.extend.data.entity.Event;
import net.zdsoft.bigdata.extend.data.service.EventService;
import net.zdsoft.bigdata.extend.data.service.TagRuleRelationService;
import net.zdsoft.bigdata.frame.data.elastic.EsClientService;
import net.zdsoft.bigdata.frame.data.hbase.HbaseClientService;
import net.zdsoft.bigdata.frame.data.phoenix.PhoenixClientService;
import net.zdsoft.bigdata.metadata.entity.*;
import net.zdsoft.bigdata.metadata.service.*;
import net.zdsoft.bigdata.metadata.vo.MetadataTreeVO;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.validation.Valid;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static net.zdsoft.bigdata.data.DatabaseType.MYSQL;

/**
 * Created by wangdongdong on 2019/1/7 10:24.
 */
@Controller
@RequestMapping(value = "/bigdata/metadata")
public class MetadataController extends BigdataBaseAction {

    @Resource
    private MetadataService metadataService;
    @Resource
    private MetadataTableColumnService metadataTableColumnService;
    @Resource
    private MetadataRelationService metadataRelationService;
    @Resource
    private EtlJobService etlJobService;
    @Resource
    private DataModelService dataModelService;
    @Resource
    private DataModelParamService dataModelParamService;
    @Resource
    private EventService eventService;
    @Resource
    private QualityRuleRelationService qualityRuleRelationService;
    @Resource
    private QualityDimService qualityDimService;
    @Resource
    private QualityRelatedTableService qualityRelatedTableService;
    @Resource
    private QualityRuleService qualityRuleService;
    @Resource
    private HbaseClientService hbaseClientService;
    @Resource
    private PhoenixClientService phoenixClientService;
    @Resource
    private BgPropertyTopicService bgPropertyTopicService;
    @Resource
    private BgDwRankService bgDwRankService;
    @Resource
    private PropertyTagService propertyTagService;
    @Resource
    private OptionService optionService;
    @Resource
    private MetadataIndexService metadataIndexService;
    @Resource
    private ApiInterfaceTypeService apiInterfaceTypeService;
    @Resource
    private ApiEntityService apiEntityService;
    @Resource
    private TagRuleRelationService tagRuleRelationService;
    @Resource
    private MetadataTagService metadataTagService;
    @Resource
    private EsClientService esClientService;
    @Resource
    private ApiDataSetService apiDataSetService;
    @Resource
    private FormSetService formSetService;

    @RequestMapping("/index")
    public String index() {
        return "/bigdata/metadata/index.ftl";
    }

    @RequestMapping("/system/index")
    public String systemIndex() {
        return "/bigdata/base/metadata/index.ftl";
    }

    @RequestMapping("/{mdType}/list")
    public String list(@PathVariable String mdType, ModelMap map, String id) {
        List<Metadata> metadataList = metadataService.findAllTables();

        Map<String, List<Metadata>> metadataMap = metadataList.stream().collect(Collectors.groupingBy(Metadata::getDwRankId));

        List<MetadataTreeVO> metadataTreeVOList = Lists.newArrayList();
        metadataMap.forEach((k, v) -> {
            MetadataTreeVO dwRankVO = new MetadataTreeVO();
            dwRankVO.setId(k);
            DwRank dwRank = bgDwRankService.findOne(k);
            dwRankVO.setType("dwRank");
            dwRankVO.setName(dwRank == null ? StringUtils.EMPTY : dwRank.getName());

            Map<String, List<Metadata>> propertyTopicMap = v.stream().collect(Collectors.groupingBy(Metadata::getPropertyTopicId));
            List<MetadataTreeVO> propertyTopicVOList = Lists.newArrayList();
            propertyTopicMap.forEach((ptId, ptMetadataList) -> {
                MetadataTreeVO ptVO = new MetadataTreeVO();
                ptVO.setId(ptId);
                PropertyTopic propertyTopic = bgPropertyTopicService.findOne(ptId);
                ptVO.setType("propertyTopic");
                ptVO.setName(propertyTopic == null ? StringUtils.EMPTY : propertyTopic.getName());

                List<MetadataTreeVO> mtVOList = Lists.newArrayList();
                for (Metadata metadata : ptMetadataList) {
                    MetadataTreeVO mdVO = new MetadataTreeVO();
                    mdVO.setId(metadata.getId());
                    mdVO.setType("metadata");
                    mdVO.setName(metadata.getName());
                    mdVO.setDbType(metadata.getDbType());
                    mdVO.setStatus(metadata.getStatus());
                    mdVO.setIsPhoenix(metadata.getIsPhoenix());
                    mtVOList.add(mdVO);
                }
                ptVO.setChild(mtVOList);
                propertyTopicVOList.add(ptVO);
            });
            dwRankVO.setChild(propertyTopicVOList);
            metadataTreeVOList.add(dwRankVO);
        });

        map.put("metadataTreeVOList", metadataTreeVOList);
        map.put("mdType", mdType);
        map.put("id", id);
        return "/bigdata/metadata/list.ftl";
    }

    @RequestMapping("/system/{mdType}/list")
    public String systemList(@PathVariable String mdType, ModelMap map) {
        List<Metadata> metadataList = metadataService.findAllSystemTables();

        map.put("mdType", mdType);
        map.put("metadataList", metadataList);
        return "/bigdata/base/metadata/list.ftl";
    }

    @RequestMapping("/{mdType}/listData")
    @ResponseBody
    public Response list(@PathVariable String mdType) {
        List<Metadata> metadataList = Lists.newArrayList();
        if ("job".equals(mdType)) {
            etlJobService.findAll().forEach(e -> {
                Metadata metadata = new Metadata();
                metadata.setName(e.getName());
                metadata.setMdType("job");
                metadata.setId(e.getId());
                metadataList.add(metadata);
            });
        } else if ("model".equals(mdType)) {
            dataModelService.findAll().forEach(e -> {
                Metadata metadata = new Metadata();
                metadata.setName(e.getName());
                metadata.setMdType("model");
                metadata.setId(e.getId());
                metadataList.add(metadata);
            });
        } else if ("event".equals(mdType)) {
            eventService.findAll().forEach(e -> {
                Metadata metadata = new Metadata();
                metadata.setName(e.getEventName());
                metadata.setMdType("event");
                metadata.setId(e.getId());
                metadataList.add(metadata);
            });
        } else {
            metadataList.addAll(metadataService.findByMdType(mdType));
        }
        return Response.ok().data(metadataList).build();
    }

    @RequestMapping("/{metadataId}/detailInformation")
    public String detailInformation(@PathVariable String metadataId, String mdType, ModelMap map) {
        if ("job".equals(mdType)) {
            EtlJob metadata = etlJobService.findOne(metadataId);
            map.put("metadata", metadata);
        } else if ("model".equals(mdType)) {
            DataModel metadata = dataModelService.findOne(metadataId);
            map.put("metadata", metadata);
        } else if ("event".equals(mdType)) {
            Event metadata = eventService.findOne(metadataId);
            map.put("metadata", metadata);
        } else {
            Metadata metadata = metadataService.findOne(metadataId);
            PropertyTopic propertyTopic = bgPropertyTopicService.findOne(metadata.getPropertyTopicId());
            DwRank dwRank = bgDwRankService.findOne(metadata.getDwRankId());
            map.put("propertyTopic", propertyTopic != null ? propertyTopic.getName() : StringUtils.EMPTY);
            map.put("dwRank", dwRank != null ? dwRank.getName() : StringUtils.EMPTY);
            List<MetadataTag> tagList = metadataTagService.findByMetadataId(metadata.getId());
            Set<String> tagIds = new HashSet<>();
            for (MetadataTag tag : tagList) {
                tagIds.add(tag.getTagId());
            }
            List<PropertyTag> pTagList = propertyTagService.findListByIdIn(tagIds.toArray(new String[0]));
            StringBuffer tagsBuffer = new StringBuffer();
            for (PropertyTag tag : pTagList) {
                tagsBuffer.append(tag.getName()).append(",");
            }
            map.put("tags", StringUtils.isNotBlank(tagsBuffer.toString()) ? tagsBuffer.toString().substring(0, tagsBuffer.toString().length() - 1) : StringUtils.EMPTY);
            map.put("metadata", metadata);
        }
        map.put("mdType", mdType);
        return "/bigdata/metadata/detail/info/detailInformation.ftl";
    }

    @RequestMapping("/{metadataId}/fieldInformation")
    public String fieldInformation(@PathVariable String metadataId, ModelMap map, boolean isAdmin) {
        List<MetadataTableColumn> columnList = metadataTableColumnService.findByMetadataId(metadataId);
        Metadata metadata = metadataService.findOne(metadataId);
        map.put("columnList", columnList);
        map.put("isAdmin", isAdmin);
        if ("es".equals(metadata.getDbType())) {
            return "/bigdata/metadata/detail/field/es.ftl";
        } else if ("kylin".equals(metadata.getDbType())) {
            return "/bigdata/metadata/detail/field/kylin.ftl";
        } else if ("impala".equals(metadata.getDbType())) {
            return "/bigdata/metadata/detail/field/impala.ftl";
        } else if ("hbase".equals(metadata.getDbType())) {
            return "/bigdata/metadata/detail/field/hbase.ftl";
        }
        return "/bigdata/metadata/detail/field/fieldInformation.ftl";
    }

    @RequestMapping("/{metadataId}/getField")
    @ResponseBody
    public Response getField(@PathVariable String metadataId) {
        List<MetadataTableColumn> columnList = metadataTableColumnService.findByMetadataId(metadataId);
        return Response.ok().data(columnList).build();
    }

    @RequestMapping("/{metadataId}/bloodRelationshipChart")
    @ResponseBody
    public Response bloodRelationshipChart(@PathVariable String metadataId, String mdType) {
        return Response.ok().data(metadataRelationService.getMetadataRelation(metadataId)).build();
    }

    @RequestMapping("{metadataId}/relativeTable")
    public String relatedTable(@PathVariable String metadataId, String mdType, ModelMap map) {
        List<QualityRelatedTable> relatedTables = qualityRelatedTableService.findAllByMetadataId(metadataId);

        List<RelatedTableVo> relatedTableVos = new ArrayList<>(relatedTables.size());
        RelatedTableVo relatedTableVo = null;
        for (QualityRelatedTable relatedTable : relatedTables) {
            relatedTableVo = new RelatedTableVo();
            Metadata masterTable = metadataService.findOne(metadataId);
            Metadata followerTable = metadataService.findOne(relatedTable.getRelatedMdId());
            MetadataTableColumn masterTableColumn = metadataTableColumnService.findById(relatedTable.getMdColumnId()).get();
            MetadataTableColumn followerTableColumn = metadataTableColumnService.findById(relatedTable.getRelatedColumnId()).get();

            relatedTableVo.setId(relatedTable.getId());
            relatedTableVo.setMasterTableName(masterTable.getTableName());
            relatedTableVo.setFollowerTableName(followerTable.getTableName());
            relatedTableVo.setMasterTableColumnName(masterTableColumn.getColumnName());
            relatedTableVo.setFollowerTableColumnName(followerTableColumn.getColumnName());
            relatedTableVos.add(relatedTableVo);
        }
        map.put("relatedTableVos", relatedTableVos);
        return "/bigdata/metadata/detail/relatedTable/relatedTable.ftl";
    }

    @RequestMapping("/relatedTableEdit")
    public String doEditRelatedTable(String id, String metadataId, String mdType, ModelMap map) {
        Optional<Metadata> masterTable = metadataService.findByMetadataId(metadataId);
        List<MetadataTableColumn> masterTableColumns = metadataTableColumnService.findByMetadataId(metadataId);
        // 获取所有从表
        List<Metadata> allTables = metadataService.findTablesByDbType(masterTable.get().getDbType());
        List<Metadata> followerTableList = allTables.stream().filter(x -> !x.getId().equals(metadataId)).collect(Collectors.toList());

        QualityRelatedTable relatedTable = null;
        //编辑操作id不为空
        if (StringUtils.isNotBlank(id)) {
            relatedTable = qualityRelatedTableService.findOne(id);
        }

        map.put("relatedTable", relatedTable);
        map.put("masterTable", masterTable.get());
        map.put("masterTableColumns", masterTableColumns);
        map.put("followerTableList", followerTableList);
        return "/bigdata/metadata/edit/relatedTable/relatedTableEdit.ftl";
    }

    @RequestMapping("/saveRelatedTable")
    @ResponseBody
    public Response saveRelatedTable(@Valid QualityRelatedTable qualityRelatedTable) {
        qualityRelatedTableService.saveQualityRelatedTable(qualityRelatedTable);
        return Response.ok().message("保存成功!").build();
    }

    /**
     * 根据选择的从表加载从表列
     *
     * @param id         关联从表的metadataId
     * @param metadataId
     * @param mdType
     * @return
     */
    @RequestMapping("/followerTableColumnList")
    @ResponseBody
    public Response followerTableColumnList(String id, String metadataId, String mdType) {
        List<MetadataTableColumn> followerTableColumns = metadataTableColumnService.findByMetadataId(id);
        return Response.ok().data(followerTableColumns).build();
    }

    @RequestMapping("/{metadataId}/dataRule")
    public String dataRule(@PathVariable String metadataId, String mdType, ModelMap map) {
        List<QualityRuleRelation> ruleRelations = qualityRuleRelationService.findAllByMetadataId(metadataId);
        Map<String, QualityDim> dimMap = qualityDimService
                .findQualityDimMapByType(2);
        for (QualityRuleRelation rule : ruleRelations) {
            if (dimMap.containsKey(rule.getDimCode().trim())) {
                rule.setDimName(dimMap.get(rule.getDimCode().trim()).getName());
            }
        }
        map.put("ruleRelations", ruleRelations);
        return "/bigdata/metadata/detail/dataRule/dataRule.ftl";
    }

    @RequestMapping("/deleteRelatedTable")
    @ResponseBody
    public Response deleteRelatedTable(String id) {
        qualityRelatedTableService.delete(id);
        return Response.ok().build();
    }

    @RequestMapping("/{metadataId}/tableView")
    public String tableView(@PathVariable String metadataId, ModelMap map) {
        List<Metadata> tableViews = metadataService.findTableViewById(metadataId);
        Metadata metadata = metadataService.findOne(metadataId);
        List<MetadataTableColumn> metadataTableColumns = metadataTableColumnService.findByMetadataId(metadata.getId());
        Map<String, String> nameMap = metadataTableColumns.stream().collect(Collectors.toMap(MetadataTableColumn::getColumnName, MetadataTableColumn::getName));
        tableViews.forEach(e -> {
            StringBuilder s = new StringBuilder();
            if (StringUtils.isNotBlank(e.getTableName())) {
                List<String> columnNames = JSON.parseArray(e.getTableName(), String.class);
                for (String columnName : columnNames) {
                    s.append(nameMap.getOrDefault(columnName, "")).append(",");
                }
                s.deleteCharAt(s.length() - 1);
                e.setTableName(s.toString());
            }
        });
        map.put("tableViews", tableViews);
        map.put("dbType", metadata.getDbType());
        return "/bigdata/metadata/detail/tableView/viewInfo.ftl";
    }

    @RequestMapping("/{metadataId}/dataSourceTarget")
    public String dataSourceTarget(@PathVariable String metadataId, String mdType, ModelMap map) {
        // 查询应用去向
        List<MetadataRelation> target = metadataRelationService.findBySourceId(metadataId);
        Iterator<MetadataRelation> targetIt = target.iterator();
        while (targetIt.hasNext()) {
            MetadataRelation metadataRelation = targetIt.next();
            if ("该数据源已被删除".equals(metadataRelation.getTargetName())) {
                metadataRelationService.delete(metadataRelation.getId());
                targetIt.remove();
            }
        }
        // 查询应用来源
        List<MetadataRelation> source = metadataRelationService.findByTargetId(metadataId);
        Iterator<MetadataRelation> sourceIt = source.iterator();
        while (sourceIt.hasNext()) {
            MetadataRelation metadataRelation = sourceIt.next();
            if ("该数据源已被删除".equals(metadataRelation.getSourceName())) {
                metadataRelationService.delete(metadataRelation.getId());
                sourceIt.remove();
            }
        }

        map.put("target", target);
        map.put("source", source);
        return "/bigdata/metadata/detail/relationship/dataSourceTarget.ftl";
    }

    @RequestMapping("/metadataEdit")
    public String metadataEdit(String id, String mdType, ModelMap model) {
        Metadata metadata = StringUtils.isNotBlank(id) ? metadataService.findOne(id) : new Metadata();
        List<DwRank> dwRanks = bgDwRankService.findAll();
        List<PropertyTopic> propertyTopics = bgPropertyTopicService.findAll();
        List<PropertyTag> propertyTags = propertyTagService.findAll();
        Set<String> tagIds = EntityUtils.getSet(metadataTagService.findByMetadataId(id), MetadataTag::getTagId);

        model.put("dwRanks", dwRanks);
        model.put("propertyTopics", propertyTopics);
        model.put("metadata", metadata);
        model.put("mdType", mdType);
        model.put("propertyTags", propertyTags);
        model.put("tagIds", StringUtils.join(tagIds, ','));
        return "/bigdata/metadata/edit/metadata/metadataEdit.ftl";
    }

    @RequestMapping("/saveMetadata")
    @ResponseBody
    public Response saveMetadata(Metadata metadata, @RequestParam(value = "tags[]", required = false) String[] tags) {
        try {
            String id = metadata.getId();
            Metadata oldMetadata = new Metadata();
            if (StringUtils.isNotBlank(id)) {
                oldMetadata = metadataService.findOne(id);
            }

            if (StringUtils.isNotBlank(id) && metadata.getIsSupportApi() == 0) {
                long apiCount = apiInterfaceTypeService.countBy("metadataId", id);
                if (apiCount > 0) {
                    return Response.error().message("该元数据被接口引用，不能修改是否支持api!").build();
                }
            }

            if (metadata.getStatus() == null) {
                metadata.setStatus(0);
            }
            DatabaseType databaseType = DatabaseType.parse(metadata.getDbType());
            if (databaseType != null) {
                metadata.setDbType(databaseType.getDbType());
            }

            metadataService.saveMetadata(metadata, tags);

            // 业务日志埋点
            if (StringUtils.isBlank(id)) {
                BizOperationLogCollector.saveBizOperationLog(BizOperationLog.LOG_TYPE_INSERT, "insert-metadata",
                        "元数据管理", "大数据管理", "新增" + metadata.getName(), null, metadata);
            } else {
                BizOperationLogCollector.saveBizOperationLog(BizOperationLog.LOG_TYPE_UPDATE, "update-metadata",
                        "元数据管理", "大数据管理", "修改" + oldMetadata.getName(), oldMetadata, metadata);
            }

            return Response.ok().data(metadata.getId()).build();
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/deleteMetadata")
    @ResponseBody
    public Response deleteMetadata(String id) {
        try {
            long apiCount = apiInterfaceTypeService.countBy("metadataId", id);
            if (apiCount > 0) {
                return Response.error().message("该元数据被接口引用，您不能删除!").build();
            }

            long ruleCount = tagRuleRelationService.countBy("mdId", id);
            if (ruleCount > 0) {
                return Response.error().message("该元数据被预警项目引用，您不能删除!").build();
            }

            long tableCount = dataModelParamService.countBy("useTable", id);
            if (tableCount > 0) {
                return Response.error().message("该元数据被数据模型引用，您不能删除!").build();
            }

            ApiDataSet apiDateSet = apiDataSetService.findByMdId(id);
            if (apiDateSet != null) {
                return Response.error().message("该元数据被数据集引用，您不能删除!").build();
            }

            FormSet formSet = formSetService.findByMdId(id);
            if (formSet != null) {
                return Response.error().message("该元数据被数据表单引用，您不能删除!").build();
            }
            Metadata metadata = metadataService.findOne(id);
            metadataService.deleteMetadata(id);

            BizOperationLogCollector.saveBizOperationLog(BizOperationLog.LOG_TYPE_DELETE, "delete-metadata",
                    "元数据管理", "大数据管理", "删除" + metadata.getName(), metadata, null);

            return Response.ok().build();
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }

    }

    @RequestMapping("/fieldEdit")
    public String fieldEdit(String id, String metadataId, ModelMap model) {
        MetadataTableColumn field = StringUtils.isNotBlank(id) ? metadataTableColumnService.findOne(id) : new MetadataTableColumn();

        Metadata metadata = metadataService.findOne(metadataId);
        model.put("metadataId", metadataId);
        model.put("dbType", metadata.getDbType());
        model.put("isPhoenix", metadata.getIsPhoenix());
        if (StringUtils.isBlank(id)) {
            Integer maxOrderId = metadataTableColumnService.getMaxOrderIdByMetadataId(metadataId);
            if (maxOrderId == null) {
                maxOrderId = 0;
            }
            if (maxOrderId >= 999) {
                maxOrderId = 0;
            }
            field.setOrderId(++maxOrderId);
        }
        model.put("field", field);
        if ("es".equals(metadata.getDbType())) {
            return "/bigdata/metadata/edit/field/esFieldEdit.ftl";
        } else if ("kylin".equals(metadata.getDbType())) {
            return "/bigdata/metadata/edit/field/kylinFieldEdit.ftl";
        } else if ("impala".equals(metadata.getDbType())) {
            return "/bigdata/metadata/edit/field/impalaFieldEdit.ftl";
        } else if ("hbase".equals(metadata.getDbType())) {
            return "/bigdata/metadata/edit/field/hbaseFieldEdit.ftl";
        }
        return "/bigdata/metadata/edit/field/fieldEdit.ftl";
    }

    @RequestMapping("/saveField")
    @ResponseBody
    public Response saveField(MetadataTableColumn metadataTableColumn) {
        try {
            String id = metadataTableColumn.getId();
            // 业务日志埋点
            if (StringUtils.isBlank(id)) {
                BizOperationLogCollector.saveBizOperationLog(BizOperationLog.LOG_TYPE_INSERT, "insert-metadataTableColumn",
                        "元数据管理", "大数据管理", "新增元数据字段" + metadataTableColumn.getName(), null, metadataTableColumn);
            } else {
                MetadataTableColumn oldColumn = metadataTableColumnService.findOne(id);
                BizOperationLogCollector.saveBizOperationLog(BizOperationLog.LOG_TYPE_UPDATE, "update-metadataTableColumn",
                        "元数据管理", "大数据管理", "修改元数据字段" + metadataTableColumn.getName(), oldColumn, metadataTableColumn);
            }

            metadataTableColumnService.saveMetadataTableColumn(metadataTableColumn);
            return Response.ok().build();
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/deleteField")
    @ResponseBody
    public Response deleteField(String id) {
        MetadataTableColumn metadataTableColumn = metadataTableColumnService.findOne(id);
        if (apiEntityService.findByMetadataIdAndColumnName(metadataTableColumn.getMetadataId(), metadataTableColumn.getColumnName())) {
            return Response.error().message("该字段已被接口引用，您不能删除!").build();
        }

        long ruleCount = tagRuleRelationService.countBy("mdColumnId", id);
        if (ruleCount > 0) {
            return Response.error().message("该字段被预警项目引用，您不能删除!").build();
        }

        long fieldCount = dataModelParamService.countBy("useField", id);
        if (fieldCount > 0) {
            return Response.error().message("该字段被数据模型引用，您不能删除!").build();
        }

        metadataTableColumnService.delete(id);
        // 业务日志埋点
        BizOperationLogCollector.saveBizOperationLog(BizOperationLog.LOG_TYPE_DELETE, "delete-metadataTableColumn",
                "元数据管理", "大数据管理", "删除元数据字段" + metadataTableColumn.getName(), metadataTableColumn, null);

        return Response.ok().build();
    }

    @RequestMapping("/ruleRelationEdit")
    public String ruleRelationEdit(String id, String metadataId, String mdType, ModelMap model) {
        QualityRuleRelation ruleRelation = StringUtils.isNotBlank(id) ? qualityRuleRelationService.findOne(id) : new QualityRuleRelation();
        List<QualityDim> dimList = qualityDimService.findQualityDimsByType(2);
        // 查询所有字段
        List<MetadataTableColumn> tableColumns = metadataTableColumnService.findByMetadataId(metadataId);
        if ("table".equals(mdType)) {
            Metadata metadata = metadataService.findOne(metadataId);
            model.put("tableName", metadata.getTableName());
            model.put("dbType", metadata.getDbType());
        }
        model.put("dimList", dimList);
        model.put("ruleRelation", ruleRelation);
        model.put("metadataId", metadataId);
        model.put("mdType", mdType);
        model.put("tableColumns", tableColumns);
        return "/bigdata/metadata/edit/rule/ruleRelationEdit.ftl";
    }

    @RequestMapping("/saveRuleRelation")
    @ResponseBody
    public Response saveRuleRelation(QualityRuleRelation qualityRuleRelation) {
        try {
            String id = qualityRuleRelation.getId();
            if (StringUtils.isNotBlank(qualityRuleRelation.getColumnId())) {
                MetadataTableColumn column = metadataTableColumnService.findOne(qualityRuleRelation.getColumnId());
                qualityRuleRelation.setColumnName(column.getColumnName());
            }

            if (StringUtils.isNotBlank(qualityRuleRelation.getRuleTemplateId())) {
                QualityRule rule = qualityRuleService.findOne(qualityRuleRelation.getRuleTemplateId());
                qualityRuleRelation.setDimCode(rule.getDimCode());
                qualityRuleRelation.setComputerType(rule.getComputerType());
                qualityRuleRelation.setDetail(rule.getDetail());
            }

            Metadata metadata = metadataService.findOne(qualityRuleRelation.getMetadataId());
            qualityRuleRelation.setDbType(metadata.getDbType());
            // 业务日志埋点
            if (StringUtils.isBlank(id)) {
                BizOperationLogCollector.saveBizOperationLog(BizOperationLog.LOG_TYPE_INSERT, "insert-qualityRuleRelation",
                        "元数据管理", "大数据管理", "新增数据规则" + qualityRuleRelation.getName(), null, qualityRuleRelation);
            } else {
                QualityRuleRelation oldMRelation = qualityRuleRelationService.findOne(id);
                BizOperationLogCollector.saveBizOperationLog(BizOperationLog.LOG_TYPE_UPDATE, "update-qualityRuleRelation",
                        "元数据管理", "大数据管理", "修改数据规则" + qualityRuleRelation.getName(), oldMRelation, qualityRuleRelation);
            }

            qualityRuleRelationService.saveQualityRuleRelation(qualityRuleRelation);
            return Response.ok().build();
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/deleteRuleRelation")
    @ResponseBody
    public Response deleteRuleRelation(String id) {
        QualityRuleRelation ruleRelation = qualityRuleRelationService.findOne(id);
        qualityRuleRelationService.delete(id);
        // 业务日志埋点
        BizOperationLogCollector.saveBizOperationLog(BizOperationLog.LOG_TYPE_DELETE, "delete-qualityRuleRelation",
                "元数据管理", "大数据管理", "删除数据规则" + ruleRelation.getName(), ruleRelation, null);

        return Response.ok().build();
    }

    @RequestMapping("/getAllQualityRule")
    @ResponseBody
    public Response getAllQualityRule(Integer ruleType) {
        return Response.ok().data(qualityRuleService.findQualityRulesByRuleType(ruleType)).build();
    }

    @RequestMapping("/getQualityRule")
    @ResponseBody
    public Response getQualityRule(String templateId) {
        return Response.ok().data(qualityRuleService.findOne(templateId)).build();
    }

    @RequestMapping("/{metadataId}/initTable")
    public String initTable(@PathVariable String metadataId, String mdType, ModelMap map) {

        // 查询表信息
        Metadata table = metadataService.findOne(metadataId);
        // 查询字段信息
        List<MetadataTableColumn> tableColumns = metadataTableColumnService.findByMetadataId(metadataId);
        // 组装建表语句
        String tableName = table.getTableName();
        StringBuilder create_sql = new StringBuilder("create table ");
        create_sql.append(tableName).append(" ( \r\n");

        int i = 0;
        for (MetadataTableColumn column : tableColumns) {
            if (column.getIsPrimaryKey() != null && column.getIsPrimaryKey() == 1) {
                if (column.getPrimaryType() == 1) {
                    create_sql.append("\t").append("int auto_increment primary key");
                } else {
                    create_sql.append("\t")
                            .append(column.getColumnName())
                            .append(" ")
                            .append(column.getColumnType())
                            .append("(")
                            .append(column.getColumnLength())
                            .append(") ")
                            .append("primary key");
                }
            } else {
                create_sql.append("\t").append(column.getColumnName()).append(" ")
                        .append(column.getColumnType());

                if ("date".equals(column.getColumnType()) || "time".equals(column.getColumnType()) || "datetime".equals(column.getColumnType()) || "timestamp".equals(column.getColumnType())) {
                    if (++i < tableColumns.size()) {
                        create_sql.append(",\n");
                    }
                    continue;
                }
                create_sql.append("(")
                        .append(column.getColumnLength());

                if ("double".equals(column.getColumnType()) || "float".equals(column.getColumnType())) {
                    create_sql.append(",").append(column.getDecimalLength());
                }

                create_sql.append(")");
            }
            if (++i < tableColumns.size()) {
                create_sql.append(",\n");
            }
        }
        create_sql.append("\n)");
        map.put("create_sql", create_sql);
        map.put("dbType", table.getDbType());
        return "/bigdata/metadata/detail/table/initTable.ftl";
    }

    @RequestMapping("/createTable")
    @ResponseBody
    public Response createTable(String metadataId, String create_sql) {

        // 查询表信息
        Metadata table = metadataService.findOne(metadataId);
        if ("mysql".equals(table.getDbType())) {
            try {
                executeJDBCSQL(create_sql, table.getTableName());
                table.setStatus(1);
                metadataService.update(table, table.getId(), new String[]{"status"});
                return Response.ok().message("执行成功").build();
            } catch (Exception e) {
                return Response.error().message(e.getMessage()).build();
            }
        } else if ("hbase".equals(table.getDbType())) {
            Boolean result = false;
            try {

                //原生
                if (0 == table.getIsPhoenix()) {
                    List<MetadataTableColumn> columnList = metadataTableColumnService.findByMetadataId(table.getId());
                    Set<String> familySets = new HashSet<>();
                    for (MetadataTableColumn column : columnList) {
                        if (column.getColumnName().contains(":")) {
                            familySets.add(column.getColumnName().split(":")[0]);
                        } else {
                            familySets.add(column.getColumnName());
                        }

                    }
                    result = hbaseClientService.creatTable(table.getTableName(), familySets.toArray(new String[0]));
                } else {
                    result = phoenixClientService.createTableByMetadata(metadataId);
                }

                if (result) {
                    table.setStatus(1);
                    metadataService.update(table, table.getId(), new String[]{"status"});
                    return Response.ok().message("创建成功").build();
                } else {
                    return Response.error().message("创建失败，该表已经存在").build();
                }
            } catch (Exception e) {
                return Response.error().message("创建失败" + e.getMessage()).build();
            }
        } else if ("es".equals(table.getDbType())) {
            try {
                List<MetadataTableColumn> columnList = metadataTableColumnService.findByMetadataId(table.getId());
                List<String> columnSets = new ArrayList<>();
                for (MetadataTableColumn column : columnList) {
                    String columnFormat = StringUtils.isNotBlank(column.getColumnFormat()) ? column.getColumnFormat() : "";
                    columnSets.add(column.getColumnName() + "," + column.getColumnType() + "," + columnFormat);
                }
                esClientService.upsertIndex(table.getTableName(), table.getTableName(), columnSets);
                table.setStatus(1);
                metadataService.update(table, table.getId(), new String[]{"status"});
                return Response.ok().message("创建成功").build();
            } catch (Exception e) {
                return Response.error().message("创建失败" + e.getMessage()).build();
            }

        }
        return Response.error().message("数据库类型错误").build();
    }

    private void executeJDBCSQL(String sql, String tableName) throws BigDataBusinessException, SQLException {
        Map<String, String> mysqlParamMap = getMysqlParamMap();
        String url = new StringBuilder("jdbc:")
                .append(MYSQL.getName())
                .append("://")
                .append(mysqlParamMap.get("domain"))
                .append(":")
                .append(mysqlParamMap.get("port")).append("/")
                .append(mysqlParamMap.get("database"))
                .append("?useUnicode=true&characterEncoding=")
                .append(Database.DEFAULT_CHASET_ENCODING).toString();

        DataSource dataSource = IDataSourceUtils.createNativeDataSource(mysqlParamMap.get("user"),
                mysqlParamMap.get("password"), url, MYSQL);
        dataSource.setLoginTimeout(120);
        Connection connection = dataSource.getConnection();
        if (checkMysqlTableIsExist(connection, tableName)) {
            throw new BigDataBusinessException("创建失败，该表已经存在");
        }

        PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.executeUpdate();
        statement.close();
        connection.close();
    }

    private boolean checkMysqlTableIsExist(Connection connection, String tableName) throws BigDataBusinessException, SQLException {
        if (StringUtils.isBlank(tableName)) {
            return false;
        }
        ResultSet rs = null;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            rs = metaData.getTables(null, null, tableName, new String[]{"TABLE"});
            return rs.next();
        } catch (SQLException e) {
            throw new BigDataBusinessException(e.getMessage(), e);
        } finally {
            rs.close();
        }
    }

    private Map<String, String> getMysqlParamMap() throws BigDataBusinessException {
        OptionDto mysqlDto = optionService.getAllOptionParam("mysql");
        if (mysqlDto == null || mysqlDto.getStatus() == 0) {
            throw new BigDataBusinessException("mysql服务未启动");
        }
        return mysqlDto.getFrameParamMap();
    }

    @RequestMapping("/tableViewEdit")
    public String tableViewEdit(String id, String parentId, String mdType, String dbType, ModelMap model) {
        Metadata tableView = StringUtils.isNotBlank(id) ? metadataService.findOne(id) : new Metadata();
        List<MetadataTableColumn> columns = metadataTableColumnService.findByMetadataId(parentId);

        model.put("tableView", tableView);
        model.put("parentId", parentId);
        model.put("mdType", mdType);
        model.put("columns", columns);
        model.put("dbType", dbType);
        return "/bigdata/metadata/edit/tableView/tableViewEdit.ftl";
    }

    @RequestMapping("/saveTableView")
    @ResponseBody
    public Response saveTableView(Metadata metadata, @RequestParam(value = "column[]", required = false) String[] column) {
        try {
            metadata.setTableName(JSON.toJSONString(column));
            metadata.setMdType("tableView");
            metadataService.saveMetadata(metadata);
            return Response.ok().build();
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/deleteTableView")
    @ResponseBody
    public Response deleteTableView(String id) {
        StringBuilder sql = new StringBuilder();
        Metadata metadata = metadataService.findOne(id);
        sql.append("DROP VIEW IF EXISTS ").append(metadata.getName());
        try {
            executeJDBCSQL(sql.toString(), null);
            metadataService.delete(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.error().message(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    @RequestMapping("/executeTableViewUI")
    public String executeTableViewUI(String id, ModelMap map) {
        Metadata tableView = metadataService.findOne(id);
        Metadata table = metadataService.findOne(tableView.getParentId());

        List<String> columnNames = JSON.parseArray(tableView.getTableName(), String.class);

        StringBuilder sql = new StringBuilder("create view ");
        sql.append(tableView.getName())
                .append(" as select ")
                .append(StringUtils.join(columnNames, ","))
                .append(" from ")
                .append(table.getTableName());

        map.put("sql", sql);

        return "/bigdata/metadata/detail/tableView/initTableView.ftl";
    }

    @RequestMapping("/executeTableView")
    @ResponseBody
    public Response executeTableView(String sql) {
        try {
            executeJDBCSQL(sql, null);
            return Response.ok().build();
        } catch (BigDataBusinessException e) {
            log.error(e.getMessage(), e);
            return Response.error().message(e.getMessage()).build();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/{metadataId}/tableIndex")
    public String tableIndex(@PathVariable String metadataId, ModelMap map) {
        List<MetadataIndex> tableIndexs = metadataIndexService.findByMetadataId(metadataId);
        Metadata metadata = metadataService.findOne(metadataId);
        tableIndexs.forEach(e -> {
            if (StringUtils.isNotBlank(e.getColumns())) {
                List<String> columnIds = JSON.parseArray(e.getColumns(), String.class);
                List<MetadataTableColumn> columns = metadataTableColumnService.findListByIdIn(columnIds.toArray(new String[columnIds.size()]));
                List<String> nameList = columns.stream().map(MetadataTableColumn::getName).collect(Collectors.toList());
                e.setColumns(StringUtils.join(nameList, ","));
            }
        });
        map.put("tableIndexs", tableIndexs);
        map.put("dbType", metadata.getDbType());
        return "/bigdata/metadata/detail/tableIndex/tableIndex.ftl";
    }

    @RequestMapping("/tableIndexEdit")
    public String tableIndexEdit(String id, String metadataId, String dbType, ModelMap model) {
        MetadataIndex index = StringUtils.isNotBlank(id) ? metadataIndexService.findOne(id) : new MetadataIndex();
        List<MetadataTableColumn> columns = metadataTableColumnService.findByMetadataId(metadataId);

        model.put("tableIndex", index);
        model.put("columns", columns);
        model.put("dbType", dbType);
        return "/bigdata/metadata/edit/tableIndex/tableIndexEdit.ftl";
    }

    @RequestMapping("/saveTableIndex")
    @ResponseBody
    public Response saveTableIndex(MetadataIndex metadataIndex, String metadataId, @RequestParam(value = "column[]", required = false) String[] column) {
        try {
            metadataIndex.setColumns(JSON.toJSONString(column));
            metadataIndex.setMdId(metadataId);
            metadataIndexService.saveMetadataIndex(metadataIndex);
            return Response.ok().build();
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/deleteTableIndex")
    @ResponseBody
    public Response deleteTableIndex(String id) {
        StringBuilder sql = new StringBuilder();
        MetadataIndex metadataIndex = metadataIndexService.findOne(id);
        Metadata metadata = metadataService.findOne(metadataIndex.getMdId());
        sql.append("alter table ")
                .append(metadata.getTableName())
                .append(" drop index ")
                .append(metadataIndex.getName());
        try {
            metadataIndexService.delete(id);
            executeJDBCSQL(sql.toString(), null);
        } catch (Exception e) {
            return Response.ok().build();
        }
        return Response.ok().build();
    }

    @RequestMapping("/executeTableIndexUI")
    public String executeTableIndexUI(String id, ModelMap map) {
        MetadataIndex tableIndex = metadataIndexService.findOne(id);
        Metadata metadata = metadataService.findOne(tableIndex.getMdId());

        List<String> columnIds = JSON.parseArray(tableIndex.getColumns(), String.class);
        List<MetadataTableColumn> columns = metadataTableColumnService.findListByIdIn(columnIds.toArray(new String[columnIds.size()]));
        List<String> nameList = columns.stream().map(MetadataTableColumn::getColumnName).collect(Collectors.toList());

        StringBuilder sql = new StringBuilder("alter table ");
        sql.append(metadata.getTableName())
                .append(" add ");

        if ("1".equals(tableIndex.getType())) {
            sql.append("index ");
        } else if ("2".equals(tableIndex.getType())) {
            sql.append("unique ");
        } else if ("3".equals(tableIndex.getType())) {
            sql.append("primary key ");
        }

        sql.append(tableIndex.getName())
                .append(" (")
                .append(StringUtils.join(nameList, ","))
                .append(") ");

        map.put("sql", sql);

        return "/bigdata/metadata/detail/tableIndex/initTableIndex.ftl";
    }

    @RequestMapping("/executeTableIndex")
    @ResponseBody
    public Response executeTableIndex(String sql, String dbType, String id) {
        try {
            if ("hbase".equals(dbType)) {
                phoenixClientService.createTableIndexByMetadataIndex(id);
            } else {
                executeJDBCSQL(sql, null);
            }
            return Response.ok().build();
        } catch (BigDataBusinessException e) {
            log.error(e.getMessage(), e);
            return Response.error().message(e.getMessage()).build();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/getMetadataTableColumns")
    @ResponseBody
    public Response getMetadataTableColumns(String metadataId, String type) {
        List<MetadataTableColumn> columns = metadataTableColumnService.findByMetadataId(metadataId);
        Iterator<MetadataTableColumn> iterator = columns.iterator();
        while (iterator.hasNext()) {
            MetadataTableColumn column = iterator.next();
            if (column.getStatType() == null || !column.getStatType().contains(type)) {
                iterator.remove();
            }
        }
        return Response.ok().data(columns).build();
    }
}
