package net.zdsoft.bigdata.datax.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.datax.dao.DataxJobInsDao;
import net.zdsoft.bigdata.datax.entity.DataxJobIns;
import net.zdsoft.bigdata.datax.entity.DataxJobInsParam;
import net.zdsoft.bigdata.datax.enums.DataxJobParamEnum;
import net.zdsoft.bigdata.datax.service.DataxJobInsParamService;
import net.zdsoft.bigdata.datax.service.DataxJobInsRuleService;
import net.zdsoft.bigdata.datax.service.DataxJobInsService;
import net.zdsoft.bigdata.datax.service.DataxJobService;
import net.zdsoft.bigdata.metadata.service.MetadataRelationService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataxJobInsServiceImpl extends BaseServiceImpl<DataxJobIns, String> implements DataxJobInsService {

    @Resource
    private DataxJobInsDao dataxJobInsDao;
    @Resource
    private DataxJobService dataxJobService;
    @Resource
    private DataxJobInsParamService dataxJobInsParamService;
    @Resource
    private DataxJobInsRuleService dataxJobInsRuleService;
    @Resource
    private MetadataRelationService metadataRelationService;
    @Resource
    private BigLogService bigLogService;

    @Override
    protected BaseJpaRepositoryDao<DataxJobIns, String> getJpaDao() {
        return dataxJobInsDao;
    }

    @Override
    protected Class<DataxJobIns> getEntityClass() {
        return DataxJobIns.class;
    }

    @Override
    public List<DataxJobIns> findByJobId(String jobId) {
        return dataxJobInsDao.findAllByJobId(jobId);
    }

    @Override
    public void saveDataxJobIns(DataxJobIns dataxJobIns) {

        if (StringUtils.isBlank(dataxJobIns.getParentId())) {
            dataxJobIns.setParentId("00000000000000000000000000000000");
        }

        dataxJobIns.setModifyTime(new Date());
        if (StringUtils.isBlank(dataxJobIns.getId())) {
            dataxJobIns.setCreationTime(new Date());
            dataxJobIns.setId(UuidUtils.generateUuid());
            dataxJobInsDao.save(dataxJobIns);
            //业务日志埋点  新增
            LogDto logDto=new LogDto();
            logDto.setBizCode("insert-dataxJobIns");
            logDto.setDescription("任务配置 "+dataxJobIns.getName());
            logDto.setNewData(dataxJobIns);
            logDto.setBizName("数据同步任务配置");
            bigLogService.insertLog(logDto);
            return;
        }
        DataxJobIns oldDataxJobIns = dataxJobInsDao.findById(dataxJobIns.getId()).get();
        dataxJobInsDao.update(dataxJobIns, new String[]{"parentId", "name", "modifyTime", "remark"});
        //业务日志埋点  修改
        LogDto logDto=new LogDto();
        logDto.setBizCode("update-dataxJobIns");
        logDto.setDescription("任务配置 "+oldDataxJobIns.getName());
        logDto.setOldData(oldDataxJobIns);
        logDto.setNewData(dataxJobIns);
        logDto.setBizName("数据同步任务配置");
        bigLogService.updateLog(logDto);
    }

    @Override
    public void deleteDataxJobIns(String id) {
        // 删除关系
        List<DataxJobInsParam> dataxJobInsParamList = dataxJobInsParamService.findDataxJobParamByJobInstanceId(id);
        Map<String, String> dataxJobInsParamMap = dataxJobInsParamList.stream().collect(Collectors.toMap(DataxJobInsParam::getParamKey, DataxJobInsParam::getParamValue));
        String appId = dataxJobInsParamMap.get(DataxJobParamEnum.APP_ID.getCode());
        String targetId = dataxJobInsParamMap.get(DataxJobParamEnum.TARGET_ID.getCode());
        DataxJobIns oldDataxJobIns = dataxJobInsDao.findById(id).get();
        metadataRelationService.deleteBySourceIdAndTargetId(appId, targetId);
        metadataRelationService.deleteBySourceId(id);
        // 删除参数
        dataxJobInsParamService.deleteByJobInsId(id);
        // 删除规则
        dataxJobInsRuleService.deleteByJobInsId(id);
        // 删除配置
        dataxJobInsDao.deleteById(id);
        //业务日志埋点  删除
        LogDto logDto=new LogDto();
        logDto.setBizCode("delete-dataxJobIns");
        logDto.setDescription("任务配置 "+oldDataxJobIns.getName());
        logDto.setBizName("数据同步任务配置");
        logDto.setOldData(oldDataxJobIns);
        bigLogService.deleteLog(logDto);
        System.out.println("删除数据同步任务配置");
    }

    @Override
    public DataxJobIns findTopJobIns(String jobId) {
        return findJobInsByParentId(jobId, "00000000000000000000000000000000");
    }

    @Override
    public void deleteByJobId(String jobId) {
        dataxJobInsDao.deleteByJobId(jobId);
    }

    private DataxJobIns findJobInsByParentId(String jobId, String parentId) {
        List<DataxJobIns> dataxJobInsList = this.findListBy(new String[]{"jobId", "parentId"}, new String[]{jobId, parentId});
        if (dataxJobInsList.size() < 1) {
            return null;
        }
        DataxJobIns dataxJobIns = dataxJobInsList.get(0);
        dataxJobIns.setChild(findJobInsByParentId(jobId, dataxJobIns.getId()));
        return dataxJobIns;
    }
}
