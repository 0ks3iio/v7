package net.zdsoft.bigdata.taskScheduler.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.datax.service.DataxJobService;
import net.zdsoft.bigdata.frame.data.kylin.KylinClientService;
import net.zdsoft.bigdata.metadata.entity.MetadataRelation;
import net.zdsoft.bigdata.metadata.service.MetadataRelationService;
import net.zdsoft.bigdata.taskScheduler.EtlType;
import net.zdsoft.bigdata.taskScheduler.dao.EtlJobDao;
import net.zdsoft.bigdata.taskScheduler.dao.EtlJobStepDao;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJobStep;
import net.zdsoft.bigdata.taskScheduler.service.*;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.lang3.StringUtils;
import org.pentaho.di.core.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author feekang
 */

@Service("etlJobStepService")
public class EtlJobStepServiceImpl extends BaseServiceImpl<EtlJobStep, String>
        implements EtlJobStepService {

    private Logger logger = LoggerFactory.getLogger(EtlJobStepServiceImpl.class);

    @Autowired
    private EtlJobStepDao etlJobStepDao;

    @Autowired
    private EtlJobService etlJobService;

    @Override
    public List<EtlJobStep> findEtlJobStepsByGroupId(String groupId) {

        List<EtlJobStep> steps = etlJobStepDao.findEtlJobStepsByGroupId(groupId);
        Set<String> jobIds = new HashSet<>();
        for (EtlJobStep step : steps) {
            jobIds.add(step.getJobId());
        }
        List<EtlJob> jobList = etlJobService.findListByIdIn(jobIds.toArray(new String[0]));
        Map<String, EtlJob> jobMap = new HashMap<>();
        for (EtlJob job : jobList) {
            jobMap.put(job.getId(), job);
        }
        for (EtlJobStep step : steps) {
            if (jobMap.containsKey(step.getJobId())) {
                step.setJobName(jobMap.get(step.getJobId()).getName());
            }
        }
        return steps;
    }

    @Override
    public void saveJobSteps(String groupId, String jobIds) {
        deleteJobStepByGroupId(groupId);
        List<EtlJobStep> jobSteps = new ArrayList<>();
        int num = 1;
        for (String jobId : jobIds.split(",")) {
            if (StringUtils.isEmpty(jobId))
                continue;
            EtlJobStep step = new EtlJobStep();
            step.setId(UuidUtils.generateUuid());
            step.setStep(num);
            num++;
            step.setGroupId(groupId);
            step.setJobId(jobId);
            jobSteps.add(step);
        }
        saveAll(jobSteps.toArray(new EtlJobStep[0]));
    }

    @Override
    public Long countByJobId(String jobId) {
        return etlJobStepDao.countByJobId(jobId);
    }

    @Override
    public void deleteJobStepByGroupId(String groupId) {
        etlJobStepDao.deleteByGroupId(groupId);
    }

    @Override
    protected BaseJpaRepositoryDao<EtlJobStep, String> getJpaDao() {
        return etlJobStepDao;
    }

    @Override
    protected Class<EtlJobStep> getEntityClass() {
        return EtlJobStep.class;
    }
}
