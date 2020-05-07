package net.zdsoft.bigdata.taskScheduler.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.taskScheduler.EtlType;
import net.zdsoft.bigdata.taskScheduler.dao.EtlJobDao;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.datax.service.DataxJobService;
import net.zdsoft.bigdata.frame.data.kylin.KylinClientService;
import net.zdsoft.bigdata.metadata.entity.MetadataRelation;
import net.zdsoft.bigdata.metadata.service.MetadataRelationService;
import net.zdsoft.bigdata.taskScheduler.service.*;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author feekang
 */

@Service("etlJobService")
public class EtlJobServiceImpl extends BaseServiceImpl<EtlJob, String>
        implements EtlJobService {

    private Logger logger = LoggerFactory.getLogger(EtlJobServiceImpl.class);

    @Autowired
    private EtlJobDao etlJobDao;

    @Autowired
    private KettleService kettleService;

    @Autowired
    private KylinClientService kylinClientService;

    @Autowired
    private ShellJobService shellJobService;

    @Autowired
    private SparkService sparkService;

    @Autowired
    private DataxJobService dataxJobService;

    @Autowired
    private FlinkJobService flinkJobService;

    @Autowired
    private PythonJobService pythonJobService;

    @Autowired
    private GroupJobService groupJobService;

    @Autowired
    private MetadataRelationService metadataRelationService;

    @Override
    protected BaseJpaRepositoryDao<EtlJob, String> getJpaDao() {
        return etlJobDao;
    }

    @Override
    protected Class<EtlJob> getEntityClass() {
        return EtlJob.class;
    }

    @Override
    public List<EtlJob> findScheduledEtlJobs() {
        return etlJobDao.findScheduledEtlJobs();
    }

    @Override
    public List<EtlJob> findByUnitId(String unitId) {
        return etlJobDao.findByUnitId(unitId);
    }

    @Override
    public List<EtlJob> findEtlJobsByUnitId(String unitId, Integer etlType) {
        return etlJobDao.findEtlJobsByUnitId(unitId, etlType);
    }

    @Override
    public List<EtlJob> findEtlJobsByName(String unitId, String name) {
        return etlJobDao.findEtlJobsByName(unitId, name);
    }

    @Override
    public List<EtlJob> findEtlJobsByJobCode(String unitId, String jobCode) {
        return etlJobDao.findEtlJobsByJobCode(unitId, jobCode);
    }

    @Override
    public void updateEtlJobStateById(String id, Integer state, String logId) {
        EtlJob job = findOne(id);
        job.setLastCommitLogId(logId);
        job.setLastCommitState(state);
        job.setLastCommitTime(new Date());
        update(job, job.getId(), new String[]{"lastCommitState",
                "lastCommitTime", "lastCommitLogId"});
    }

    @Override
    public void saveEtlJobRelations(EtlJob job) {
        if (StringUtils.isNotBlank(job.getId())) {
            //删除掉元数据关系表中记录
            EtlJob oldJob = findOne(job.getId());
            if (oldJob != null) {
                if (StringUtils.isNotBlank(oldJob.getSourceId()))
                    metadataRelationService.deleteBySourceIdAndTargetId(oldJob.getSourceId(), oldJob.getId());
                if (net.zdsoft.framework.utils.StringUtils.isNotBlank(oldJob.getTargetId()))
                    metadataRelationService.deleteBySourceIdAndTargetId(oldJob.getId(), oldJob.getTargetId());
            }
        }
        try {
            //建立数据来源、去向和本JOB的关系表
            if (StringUtils.isNotBlank(job.getSourceId())) {
                MetadataRelation sourceRelation = new MetadataRelation();
                sourceRelation.setSourceId(job.getSourceId());
                sourceRelation.setSourceType(job.getSourceType());
                sourceRelation.setTargetType("job");
                sourceRelation.setTargetId(job.getId());
                metadataRelationService.saveMetadataRelation(sourceRelation);
            }
            if (StringUtils.isNotBlank(job.getTargetId())) {
                MetadataRelation targetRelation = new MetadataRelation();
                targetRelation.setSourceId(job.getId());
                targetRelation.setSourceType("job");
                targetRelation.setTargetType(job.getTargetType());
                targetRelation.setTargetId(job.getTargetId());
                metadataRelationService.saveMetadataRelation(targetRelation);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void deleteJob(EtlJob job) {
        delete(job.getId());
        metadataRelationService.deleteBySourceId(job.getId());
        metadataRelationService.deleteByTargetId(job.getId());
    }

    public boolean isExistsNode(String nodeId) {
        Long num = etlJobDao.countByNodeId(nodeId);
        if (num != null && num > 0) {
            return true;
        }
        return false;
    }

    public boolean isExistsNodeAndType(String nodeId, String type) {
        Long num = etlJobDao.countByNodeIdAndType(nodeId, type);
        if (num != null && num > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean dealEtlQuantzJob(String id) {
        EtlJob job = findOne(id);
        try {
            boolean result = true;
            if (job == null) {
                dataxJobService.executeDataxJob(id);
            } else {
                if (EtlType.KETTLE.getValue() == job.getEtlType()) {
                    if (EtlJob.KETTLE_JOB.equals(job.getJobType())) {
                        result = kettleService.dealJob(job, null);
                    } else {
                        result = kettleService.dealTrans(job, null);
                    }
                } else if (EtlType.KYLIN.getValue() == job.getEtlType()) {
                    result = kylinClientService.dealJob(job);
                } else if (EtlType.SHELL.getValue() == job.getEtlType()) {
                    result = shellJobService.dealJob(job, null);
                } else if (EtlType.SPARK.getValue() == job.getEtlType()) {
                    sparkService.dealSparkJob(job.getId());
                } else if (EtlType.PYTHON.getValue() == job.getEtlType()) {
                    result = pythonJobService.dealJob(job, null);
                } else if (EtlType.FLINK.getValue() == job.getEtlType()) {
                    result = flinkJobService.dealJob(job, null);
                } else if (EtlType.GROUP.getValue() == job.getEtlType()) {
                    result = groupJobService.dealJob(job, null);
                }
            }
            return result;
        } catch (BigDataBusinessException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public long count(Date start, Date end) {
        if (start == null && end == null) {
            return etlJobDao.count((Specification<EtlJob>) (root, criteriaQuery, criteriaBuilder)
                    -> criteriaQuery.getRestriction());
        } else {
            return etlJobDao.count((Specification<EtlJob>) (root, criteriaQuery, criteriaBuilder)
                    -> {
                Predicate time = criteriaBuilder.between(root.get("creationTime").as(Timestamp.class), start, end);
                return criteriaQuery.where(time).getRestriction();
            });
        }
    }

}
