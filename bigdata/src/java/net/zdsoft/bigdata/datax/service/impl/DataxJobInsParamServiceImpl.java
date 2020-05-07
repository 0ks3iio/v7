package net.zdsoft.bigdata.datax.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import net.zdsoft.api.base.entity.eis.OpenApiApp;
import net.zdsoft.api.base.service.OpenApiAppService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.datax.dao.DataxJobInsParamDao;
import net.zdsoft.bigdata.datax.entity.DataxJobIns;
import net.zdsoft.bigdata.datax.entity.DataxJobInsParam;
import net.zdsoft.bigdata.datax.entity.DataxJobInsRule;
import net.zdsoft.bigdata.datax.enums.DataxJobParamEnum;
import net.zdsoft.bigdata.datax.service.DataxJobInsParamService;
import net.zdsoft.bigdata.datax.service.DataxJobInsRuleService;
import net.zdsoft.bigdata.datax.service.DataxJobInsService;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.MetadataRelation;
import net.zdsoft.bigdata.metadata.enums.MetadataRelationTypeEnum;
import net.zdsoft.bigdata.metadata.service.MetadataRelationService;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class DataxJobInsParamServiceImpl extends BaseServiceImpl<DataxJobInsParam, String> implements DataxJobInsParamService {

    @Resource
    private DataxJobInsParamDao dataxJobInsParamDao;
    @Resource
    private DataxJobInsService dataxJobInsService;
    @Resource
    private DataxJobInsRuleService dataxJobInsRuleService;
    @Resource
    private MetadataRelationService metadataRelationService;
    @Resource
    private OpenApiAppService openApiAppService;
    @Resource
    private MetadataService metadataService;

    @Override
    protected BaseJpaRepositoryDao<DataxJobInsParam, String> getJpaDao() {
        return dataxJobInsParamDao;
    }

    @Override
    protected Class<DataxJobInsParam> getEntityClass() {
        return DataxJobInsParam.class;
    }

    @Override
    public List<DataxJobInsParam> findDataxJobParamByJobInstanceId(String jobInstanceId) {
        return dataxJobInsParamDao.findAllByJobInsId(jobInstanceId);
    }

    @Override
    public void deleteByJobInsId(String jobInsId) {
        dataxJobInsParamDao.deleteByJobInsId(jobInsId);
    }

    @Override
    public void saveDataxJobInsParamList(List<DataxJobInsParam> dataxJobInsParamList) {
        if (dataxJobInsParamList.size() > 0) {
            // 删除之前的参数
            dataxJobInsParamDao.deleteByJobInsId(dataxJobInsParamList.get(0).getJobInsId());
        }

        dataxJobInsParamList.forEach(e->{
            e.setId(UuidUtils.generateUuid());
            dataxJobInsParamDao.save(e);
        });

    }

    @Override
    public void deleteByJobId(String jobId) {
        dataxJobInsParamDao.deleteByJobId(jobId);
    }

    @Override
    public void saveDataxJobInsParam(Map<String, String> pMap, DataxJobIns dataxJobIns, List<DataxJobInsParam> dataxJobInsParamList) throws BigDataBusinessException {

        String transformer = pMap.get("transformer");
        if (StringUtils.isNotBlank(transformer)) {
            List<DataxJobInsRule> dataxJobInsRules = Lists.newArrayList();
            List<List<String>> rules = JSON.parseObject(
                    transformer, new TypeReference<List<List<String>>>() {
                    });
            int i = 1;
            for (List<String> rule : rules) {
                if (rule.size() < 2) {
                    throw new BigDataBusinessException("规则配置错误");
                }

                DataxJobInsRule insRule = new DataxJobInsRule();
                insRule.setJobId(dataxJobIns.getJobId());
                insRule.setJobInsId(dataxJobIns.getId());
                insRule.setColumnId(rule.get(0));
                insRule.setOrderId(i++);
                insRule.setRule(JSON.toJSONString(rule));
                dataxJobInsRules.add(insRule);
            }
            dataxJobInsRuleService.deleteByJobInsId(dataxJobIns.getId());
            dataxJobInsRuleService.saveDataxJobInsRuleList(dataxJobInsRules);
            this.saveDataxJobInsParamList(dataxJobInsParamList);
        }

        dataxJobInsService.update(dataxJobIns, dataxJobIns.getId(), new String[]{"isIncrement"});
        metadataRelationService.deleteBySourceIdAndTargetId(pMap.get(DataxJobParamEnum.OLD_SOURCE_ID.getCode()), pMap.get(DataxJobParamEnum.OLD_TARGET_ID.getCode()));
        metadataRelationService.deleteBySourceIdAndTargetId(pMap.get(DataxJobParamEnum.OLD_SOURCE_ID.getCode()), dataxJobIns.getJobId());
        metadataRelationService.deleteBySourceIdAndTargetId(dataxJobIns.getJobId(), pMap.get(DataxJobParamEnum.OLD_TARGET_ID.getCode()));
        // 应用和元数据
        // 保存元数据血缘关系
        String targetId = pMap.get(DataxJobParamEnum.TARGET_ID.getCode());
        String appId = pMap.get(DataxJobParamEnum.APP_ID.getCode());

        if (StringUtils.isNotBlank(targetId)) {
            MetadataRelation metadataRelation = new MetadataRelation();
            OpenApiApp app = openApiAppService.getApp(appId);
            metadataRelation.setSourceId(appId);
            metadataRelation.setSourceName(app.getName());
            metadataRelation.setSourceType(MetadataRelationTypeEnum.APP.getCode());
            Metadata metadata = metadataService.findOne(targetId);
            metadataRelation.setTargetId(metadata.getId());
            metadataRelation.setTargetName(metadata.getName());
            metadataRelation.setTargetType(MetadataRelationTypeEnum.TABLE.getCode());
            metadataRelationService.saveMetadataRelation(metadataRelation);

            // 任务和元数据
            MetadataRelation jobToMetadata = new MetadataRelation();
            jobToMetadata.setSourceId(dataxJobIns.getJobId());
            jobToMetadata.setSourceType(MetadataRelationTypeEnum.DATAX_JOB.getCode());
            jobToMetadata.setTargetId(metadataRelation.getTargetId());
            jobToMetadata.setTargetType(MetadataRelationTypeEnum.TABLE.getCode());
            metadataRelationService.saveMetadataRelation(jobToMetadata);
        }

        // 应用和任务
        MetadataRelation appToJob = new MetadataRelation();
        appToJob.setSourceId(appId);
        appToJob.setSourceType(MetadataRelationTypeEnum.APP.getCode());
        appToJob.setTargetId(dataxJobIns.getJobId());
        appToJob.setTargetType(MetadataRelationTypeEnum.DATAX_JOB.getCode());
        metadataRelationService.saveMetadataRelation(appToJob);

    }
}
