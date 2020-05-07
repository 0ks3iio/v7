package net.zdsoft.bigdata.property.action;

import com.alibaba.fastjson.JSON;
import net.zdsoft.api.base.entity.eis.ApiInterface;
import net.zdsoft.api.base.entity.eis.OpenApiApp;
import net.zdsoft.api.base.service.ApiInterfaceService;
import net.zdsoft.api.base.service.OpenApiAppService;
import net.zdsoft.bigdata.base.entity.DwRank;
import net.zdsoft.bigdata.base.entity.PropertyTopic;
import net.zdsoft.bigdata.base.service.BgDwRankService;
import net.zdsoft.bigdata.base.service.BgPropertyTopicService;
import net.zdsoft.bigdata.data.entity.DataModel;
import net.zdsoft.bigdata.data.entity.DataModelParam;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.bigdata.data.service.DataModelParamService;
import net.zdsoft.bigdata.data.service.DataModelService;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datax.entity.DataxJob;
import net.zdsoft.bigdata.datax.service.DataxJobService;
import net.zdsoft.bigdata.extend.data.entity.Event;
import net.zdsoft.bigdata.extend.data.service.EventService;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.MetadataRelation;
import net.zdsoft.bigdata.metadata.enums.MetadataRelationTypeEnum;
import net.zdsoft.bigdata.metadata.service.MetadataRelationService;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.property.constant.BgPropertyConstant;
import net.zdsoft.bigdata.property.service.PropertyGeneralService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 数据地图
 * @author duhuachao
 * @date 2019/5/21
 */
@Controller
@RequestMapping(value = "/bigdata/property/map")
public class MetadataMapController extends BigdataBaseAction {

    @Autowired
    private BgDwRankService bgDwRankService;
    @Autowired
    private MetadataService metadataService;
    @Autowired
    private PropertyGeneralService propertyGeneralService;
    @Autowired
    private MetadataRelationService metadataRelationService;
    @Autowired
    private EtlJobService etlJobService;
    @Autowired
    private DataModelService dataModelService;
    @Autowired
    private OpenApiAppService openApiAppService;
    @Autowired
    private EventService eventService;
    @Autowired
    private DataModelParamService dataModelParamService;
    @Autowired
    private ApiInterfaceService apiInterfaceService;
    @Autowired
    private DataxJobService dataxJobService;
    @Autowired
    private BgPropertyTopicService bgPropertyTopicService;

    @RequestMapping("/index")
    @ControllerInfo("数据地图首页")
    public String mapIndex(ModelMap map){
        List<DwRank> dwRankList = bgDwRankService.findAll();
        List<PropertyTopic> topicList = bgPropertyTopicService.findAll();
        map.put("dwRankList",dwRankList);
        map.put("topicList",topicList);
        return "/bigdata/property/dataMap/index.ftl";
    }

    @RequestMapping("/metadata")
    @ControllerInfo("元数据详情")
    public String dataContent(String id, String mdType, ModelMap map){
        Metadata metadata = null;
        if (Objects.equals(MetadataRelationTypeEnum.TABLE.getCode(),mdType)) {
            metadata = metadataService.findOne(id);
            map.put("remark",metadata.getRemark());
            Json json = propertyGeneralService.findStatInfo(id, BgPropertyConstant.QUERY_STAT_TABLE_TIMES);
            if (json.size() != 0) {
                map.put("totalAmount",json.getString("total_amount"));
                map.put("apiAmount",json.getString("api_amount"));
                if (StringUtils.isNotBlank(json.getString("data_quality"))) {
                    map.put("dataQuality",Integer.valueOf(json.getString("data_quality")));
                }
                map.put("statTime",json.getString("stat_time"));
                map.put("targetAmount",json.getString("target_amount"));
                map.put("sourceAmount",json.getString("source_amount"));
            }
            return "/bigdata/property/dataMap/table.ftl";
        } else if (Objects.equals(MetadataRelationTypeEnum.ETL_JOB.getCode(),mdType)) {
            EtlJob etlJob = etlJobService.findOne(id);
            if (Objects.equals(MetadataRelationTypeEnum.TABLE.getCode(),etlJob.getSourceType()) && StringUtils.isNotEmpty(etlJob.getSourceId())) {
                metadata = metadataService.findOne(etlJob.getSourceId());
                if (metadata != null) {
                    map.put("sourceName",metadata.getName());
                }
            }
            if (Objects.equals(MetadataRelationTypeEnum.APP.getCode(),etlJob.getSourceType()) && StringUtils.isNotEmpty(etlJob.getSourceId())) {
                OpenApiApp app =openApiAppService.getApp(etlJob.getSourceId());
                if (app != null) {
                    map.put("sourceName",app.getName());
                }
            }
            if (StringUtils.isNotEmpty(etlJob.getTargetId())) {
                metadata = metadataService.findOne(etlJob.getTargetId());
                if (metadata != null) {
                    map.put("targetName",metadata.getName());
                }
            }
            map.put("remark",etlJob.getRemark());
            map.put("json",etlJob.getFlowChartJson());
            return "/bigdata/property/dataMap/job.ftl";
        } else if (Objects.equals(MetadataRelationTypeEnum.MODEL.getCode(),mdType)) {
            DataModel dataModel = dataModelService.findOne(id);
            List<DataModelParam> modelIndex = dataModelParamService.findByCodeAndType(dataModel.getCode(), "index");
            List<DataModelParam> modelDimension = dataModelParamService.findByCodeAndType(dataModel.getCode(), "dimension");
            map.put("remark",dataModel.getRemark());
            map.put("modelIndex",modelIndex);
            map.put("modelDimension",modelDimension);
            return "/bigdata/property/dataMap/model.ftl";
        } else if (Objects.equals(MetadataRelationTypeEnum.APP.getCode(),mdType)) {
            OpenApiApp app = openApiAppService.getApp(id);
            map.put("remark",app.getDescription());
            return "/bigdata/property/dataMap/app.ftl";
        } else if (Objects.equals(MetadataRelationTypeEnum.EVENT.getCode(),mdType)) {
            Event event = eventService.findOne(id);
            map.put("remark",event.getRemark());
            return "/bigdata/property/dataMap/event.ftl";
        } else if (Objects.equals(MetadataRelationTypeEnum.API.getCode(),mdType)) {
            ApiInterface api = apiInterfaceService.findOne(id);
            map.put("remark",api.getDescription());
            return "/bigdata/property/dataMap/api.ftl";
        } else {
            DataxJob dataxJob = dataxJobService.findOne(id);
            map.put("remark",dataxJob.getRemark());
            return "/bigdata/property/dataMap/dataxJob.ftl";
        }
    }

    @ResponseBody
    @RequestMapping("/relation")
    @ControllerInfo("获取节点和节点关系的数据")
    public Response relation(String dwRankId, String topicId){
        try {
            List<MetadataRelation> dataRelations = metadataRelationService.getMetadataRelation(dwRankId, topicId);
            return Response.ok().data(JSON.toJSONString(dataRelations)).build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }
}
