package net.zdsoft.bigdata.datax.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.datax.dao.DataxJobInsLogDao;
import net.zdsoft.bigdata.datax.entity.DataxJobInsLog;
import net.zdsoft.bigdata.datax.service.DataxJobInsLogService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class DataxJobInsLogServiceImpl extends BaseServiceImpl<DataxJobInsLog, String> implements DataxJobInsLogService {

    @Resource
    private DataxJobInsLogDao dataxJobInsLogDao;

    @Override
    protected BaseJpaRepositoryDao<DataxJobInsLog, String> getJpaDao() {
        return dataxJobInsLogDao;
    }

    @Override
    protected Class<DataxJobInsLog> getEntityClass() {
        return DataxJobInsLog.class;
    }

    @Override
    public void saveDataxJobInstanceLog(DataxJobInsLog dataxJobInsLog) {
        if (StringUtils.isBlank(dataxJobInsLog.getId())) {
            dataxJobInsLog.setId(UuidUtils.generateUuid());
            dataxJobInsLogDao.save(dataxJobInsLog);
            return;
        }

        dataxJobInsLogDao.update(dataxJobInsLog, new String[]{"status", "result", "startTime", "endTime"
            ,"duration", "totalCount", "successCount", "errorCount", "averageFlow", "writeSpeed", "jobInsRemark"});
    }

    @Override
    public List<DataxJobInsLog> findByJobInstanceId(String jobInstanceId) {
        List<DataxJobInsLog> dataxJobInsLogs = dataxJobInsLogDao.findAllByJobInstanceId(jobInstanceId);
        this.updateStatus(dataxJobInsLogs);
        return dataxJobInsLogs;
    }

    @Override
    public List<DataxJobInsLog> findByJobId(String jobId) {
        List<DataxJobInsLog> dataxJobInsLogs = dataxJobInsLogDao.findAllByJobIdOrderByStartTimeDesc(jobId);
        return dataxJobInsLogs;
    }

    private void updateStatus(List<DataxJobInsLog> dataxJobInsLogs) {
        Date now = new Date();
        for (DataxJobInsLog log : dataxJobInsLogs) {
            if (log.getStatus() != null && log.getStatus() == 0) {
                long differ = now.getTime() - log.getStartTime().getTime();
                if (differ/60000 > 120) {
                    log.setStatus(1);
                    log.setResult(2);
                    log.setEndTime(now);
                    dataxJobInsLogDao.update(log, new String[]{"status", "result", "endTime"});
                }
            }
        }
    }

    @Override
    public void deleteByJobId(String jobId) {
        dataxJobInsLogDao.deleteByJobId(jobId);
    }

    @Override
    public void updateJobStatus() {
        List<DataxJobInsLog> logs = dataxJobInsLogDao.findAllByResultIsNull();
        this.updateStatus(logs);
    }
}
