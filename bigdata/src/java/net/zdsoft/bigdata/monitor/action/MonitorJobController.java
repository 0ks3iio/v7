package net.zdsoft.bigdata.monitor.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.zdsoft.api.base.entity.eis.OpenApiApp;
import net.zdsoft.api.base.service.OpenApiAppService;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.bigdata.base.entity.PropertyTopic;
import net.zdsoft.bigdata.base.service.BgPropertyTopicService;
import net.zdsoft.bigdata.taskScheduler.EtlType;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJobLog;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobLogService;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datax.entity.DataxJob;
import net.zdsoft.bigdata.datax.entity.DataxJobInsLog;
import net.zdsoft.bigdata.datax.enums.DataxJobResultEnum;
import net.zdsoft.bigdata.datax.service.DataxJobInsLogService;
import net.zdsoft.bigdata.datax.service.DataxJobService;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.MetadataRelation;
import net.zdsoft.bigdata.metadata.enums.MetadataRelationTypeEnum;
import net.zdsoft.bigdata.metadata.service.MetadataRelationService;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 调度监控
 * @author duhuachao
 * @date 2019/06/03
 */
@Controller
@RequestMapping(value = "/bigdata/monitor/job")
public class MonitorJobController extends BigdataBaseAction {

    private static final Logger logger = LoggerFactory.getLogger(MonitorJobController.class);

    @Autowired
    private BgPropertyTopicService bgPropertyTopicService;
    @Autowired
    private DataxJobService dataxJobService;
    @Autowired
    private DataxJobInsLogService dataxJobInsLogService;
    @Autowired
    private EtlJobService etlJobService;
    @Autowired
    private EtlJobLogService etlJobLogService;
    @Autowired
    private MetadataRelationService metadataRelationService;
    @Autowired
    private MetadataService metadataService;
    @Autowired
    private OpenApiAppService openApiAppService;

    @ControllerInfo("主界面")
    @RequestMapping("/index")
    public String monitorJobIndex(ModelMap map) {
        Integer allTaskNum = 0;
        Integer runningNum = 0;
        Integer successNum = 0;
        Integer errorNum = 0;
        List<DataxJob> dataxJobList = dataxJobService.findAllDataxJob();
        List<EtlJob> etlJobList = etlJobService.findByUnitId(getLoginInfo().getUnitId());
        List<EtlJob> systemJobList = etlJobService.findEtlJobsByUnitId(BaseConstants.ZERO_GUID, 0);
        allTaskNum += dataxJobList.size() + etlJobList.size() + systemJobList.size();
        runningNum += (int)dataxJobList.stream().filter(dataxJob -> Objects.equals(dataxJob.getIsSchedule(),1)).count();
        runningNum += (int)etlJobList.stream().filter(etlJob -> (!Objects.equals(etlJob.getEtlType(), EtlType.FLINK_STREAM.getValue())) && Objects.equals(etlJob.getIsSchedule(), 1)).count();
        runningNum += (int)etlJobList.stream().filter(etlJob -> Objects.equals(etlJob.getEtlType(), EtlType.FLINK_STREAM.getValue())).count();
        runningNum += systemJobList.size();
        successNum += (int)dataxJobInsLogService.count((Specification<DataxJobInsLog>) (root, cq, cb) -> {
            Predicate time = cb.equal(root.get("result").as(Integer.class), DataxJobResultEnum.SUCCESS.getCode());
            return cq.where(time).getRestriction();
        });
        List<DataxJobInsLog> errorDataxJobs = dataxJobInsLogService.findAll((Specification<DataxJobInsLog>) (root, cq, cb) -> {
            Predicate time = cb.equal(root.get("result").as(Integer.class), DataxJobResultEnum.FAILED.getCode());
            return cq.where(time).getRestriction();
        });
        successNum += (int)etlJobLogService.count((Specification<EtlJobLog>) (root, cq, cb) -> {
            Predicate time = cb.equal(root.get("state").as(Integer.class), EtlJobLog.state_success);
            return cq.where(time).getRestriction();
        });
        List<EtlJobLog> errorEtlJobs = etlJobLogService.findAll((Specification<EtlJobLog>) (root, cq, cb) -> {
            Predicate time = cb.equal(root.get("state").as(Integer.class), EtlJobLog.state_fail);
            return cq.where(time).getRestriction();
        });
        errorNum = errorDataxJobs.size() + errorEtlJobs.size();
        map.put("allTaskNum",allTaskNum);
        map.put("runningNum",runningNum);
        map.put("successNum",successNum);
        map.put("errorNum",errorNum);
        List<PropertyTopic> topicList = bgPropertyTopicService.findAll();
        map.put("topicList",topicList);
        map.put("errorDataxJobs",errorDataxJobs);
        map.put("errorEtlJobs",errorEtlJobs);
        return "/bigdata/monitor/job/jobMonitor.ftl";
    }

    @ResponseBody
    @ControllerInfo("关系图")
    @RequestMapping("/relation")
    public Response monitorJobRelation(String topicId) {
        JSONArray array = new JSONArray();
        List<Metadata> dataList = metadataService.findByPropertyTopicId(topicId);
        if (CollectionUtils.isEmpty(dataList)) {
            return Response.ok().data(array.toJSONString()).build();
        }
        Set<String> dataIdsSet = EntityUtils.getSet(dataList,Metadata::getId);
        List<MetadataRelation> metadataRelations = metadataRelationService.getJobMetadataRelation();
        Set<String> dataxJobIds = Sets.newHashSet();
        Set<String> jobIds = Sets.newHashSet();
        Set<String> tableIds = Sets.newHashSet();
        Map<String,JSONObject> relationMap = Maps.newHashMap();
        getRelationMap(metadataRelations, dataxJobIds, jobIds, tableIds,relationMap);
        List<DataxJob> dataxJobList = dataxJobService.findListByIdIn(dataxJobIds.toArray(new String[dataxJobIds.size()]));
        dataxJobList.forEach(e->{
            List<DataxJobInsLog> logs = dataxJobInsLogService.findByJobId(e.getId());
            if (logs.size() > 0) {
                e.setDataxJobInsLog(logs.get(0));
            }
        });
        Map<String,DataxJob> dataxJobName = EntityUtils.getMap(dataxJobList,DataxJob::getId);
        List<EtlJob> etlJobList = etlJobService.findListByIdIn(jobIds.toArray(new String[jobIds.size()]));
        Map<String,EtlJob> etlJobName = EntityUtils.getMap(etlJobList,EtlJob::getId);
        List<Metadata> metadataList = metadataService.findListByIdIn(tableIds.toArray(new String[tableIds.size()]));
        Map<String,String> metadataName = EntityUtils.getMap(metadataList,Metadata::getId,Metadata::getName);
        List<OpenApiApp> appList = openApiAppService.getApps();
        Map<String,String> appName = EntityUtils.getMap(appList,OpenApiApp::getId,OpenApiApp::getName);
        JSONObject obj = null;
        boolean haveTable = false;
        for (Map.Entry<String, JSONObject> entry : relationMap.entrySet()) {
            obj = entry.getValue();
            haveTable = false;
            if ((StringUtils.isNotEmpty(obj.getString("sourceId")) && dataIdsSet.contains(obj.getString("sourceId"))) ||
                    (StringUtils.isNotEmpty(obj.getString("targetId")) && dataIdsSet.contains(obj.getString("targetId")))) {
                haveTable = true;
            }
            if (!haveTable) {
                continue;
            }
            obj.put("jobId",entry.getKey());
            if (dataxJobName.containsKey(entry.getKey())) {
                obj.put("jobName",dataxJobName.get(entry.getKey()).getName());
                if (dataxJobName.get(entry.getKey()).getDataxJobInsLog() != null) {
                    obj.put("jobState",dataxJobName.get(entry.getKey()).getDataxJobInsLog().getResult());
                } else {
                    obj.put("jobState",0);
                }
                obj.put("jobType","dataxJob");
            } else if (etlJobName.containsKey(entry.getKey())) {
                obj.put("jobName",etlJobName.get(entry.getKey()).getName());
                if (etlJobName.get(entry.getKey()).getLastCommitState() != null) {
                    obj.put("jobState",etlJobName.get(entry.getKey()).getLastCommitState());
                } else {
                    obj.put("jobState",0);
                }
                obj.put("jobType","job");
            } else {
                continue;
            }
            if (StringUtils.isNotEmpty(obj.getString("sourceId"))) {
                if (Objects.equals(obj.getString("sourceType"), MetadataRelationTypeEnum.TABLE.getCode())) {
                    obj.put("sourceName",metadataName.get(obj.getString("sourceId")));
                }
                if (Objects.equals(obj.getString("sourceType"), MetadataRelationTypeEnum.APP.getCode())) {
                    obj.put("sourceName",appName.get(obj.getString("sourceId")));
                }
            }
            if (StringUtils.isNotEmpty(obj.getString("targetId"))) {
                if (Objects.equals(obj.getString("targetType"), MetadataRelationTypeEnum.TABLE.getCode())) {
                    obj.put("targetName",metadataName.get(obj.getString("targetId")));
                }
                if (Objects.equals(obj.getString("targetType"), MetadataRelationTypeEnum.APP.getCode())) {
                    obj.put("targetName",appName.get(obj.getString("targetId")));
                }
            }
            array.add(obj);
        }
        return Response.ok().data(array.toJSONString()).build();
    }

    @RequestMapping("/jobdata")
    @ControllerInfo("job详情")
    public String dataContent(String jobId, String jobType, ModelMap map){
        Metadata metadata = null;
        if (Objects.equals(MetadataRelationTypeEnum.ETL_JOB.getCode(),jobType)) {
            EtlJob etlJob = etlJobService.findOne(jobId);
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
            return "/bigdata/monitor/job/job.ftl";
        } else {
            DataxJob dataxJob = dataxJobService.findOne(jobId);
            map.put("remark",dataxJob.getRemark());
            return "/bigdata/monitor/job/dataxjob.ftl";
        }
    }

    private void getRelationMap(List<MetadataRelation> metadataRelations, Set<String> dataxJobIds, Set<String> jobIds,
                           Set<String> tableIds, Map<String,JSONObject> relationMap) {
        metadataRelations.forEach(data->{
            JSONObject obj = null;
            if (Objects.equals(data.getSourceType(), MetadataRelationTypeEnum.DATAX_JOB.getCode()) ||
                    Objects.equals(data.getSourceType(), MetadataRelationTypeEnum.ETL_JOB.getCode())) {
                if (Objects.equals(data.getSourceType(), MetadataRelationTypeEnum.DATAX_JOB.getCode())) {
                    dataxJobIds.add(data.getSourceId());
                }
                if (Objects.equals(data.getSourceType(), MetadataRelationTypeEnum.ETL_JOB.getCode())) {
                    jobIds.add(data.getSourceId());
                }
                if (Objects.equals(data.getTargetType(), MetadataRelationTypeEnum.TABLE.getCode())) {
                    tableIds.add(data.getTargetId());
                }
                obj = relationMap.get(data.getSourceId());
                if (obj == null) {
                    obj = new JSONObject();
                }
                obj.put("targetId",data.getTargetId());
                obj.put("targetType",data.getTargetType());
                relationMap.put(data.getSourceId(),obj);
                return;
            }
            if (Objects.equals(data.getTargetType(), MetadataRelationTypeEnum.DATAX_JOB.getCode()) ||
                    Objects.equals(data.getTargetType(), MetadataRelationTypeEnum.ETL_JOB.getCode())) {
                if (Objects.equals(data.getTargetType(), MetadataRelationTypeEnum.DATAX_JOB.getCode())) {
                    dataxJobIds.add(data.getTargetId());
                }
                if (Objects.equals(data.getTargetType(), MetadataRelationTypeEnum.ETL_JOB.getCode())) {
                    jobIds.add(data.getTargetId());
                }
                if (Objects.equals(data.getSourceType(), MetadataRelationTypeEnum.TABLE.getCode())) {
                    tableIds.add(data.getSourceId());
                }
                obj = relationMap.get(data.getTargetId());
                if (obj == null) {
                    obj = new JSONObject();
                }
                obj.put("sourceId",data.getSourceId());
                obj.put("sourceType",data.getSourceType());
                relationMap.put(data.getTargetId(),obj);
                return;
            }
        });
    }
}
