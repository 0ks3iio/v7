package net.zdsoft.basedata.component;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import net.zdsoft.basedata.entity.ImportEntity;
import net.zdsoft.basedata.service.ImportService;
import net.zdsoft.basedata.service.StudentFlowService;
import net.zdsoft.framework.config.Evn;

public class StudentOutImportJob implements Job {

    private static final Logger log = Logger.getLogger(StudentOutImportJob.class);

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        log.info("检查导入服务……");
        ImportEntity importEntity = null;
        try {
            if (!OutImporting.isOutImporting()) {
                log.info("离校导入服务开始导入……");
                OutImporting.setOutImporting(true);
                importEntity = ((StudentFlowService) Evn.getBean("studentFlowService")).checkStudentFlow(ImportEntity.IMPORT_TYPE_OUT);
                if (importEntity != null)
                    ((StudentFlowService) Evn.getBean("studentFlowService")).importStudentFlow(
                            importEntity, ImportEntity.IMPORT_TYPE_OUT);
                log.info("离校导入服务导入完成");
                OutImporting.setOutImporting(false);
            }
            else {
                log.info("离校导入服务导入ing……");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("离校导入服务出错" + e.getMessage());
            if (OutImporting.isOutImporting()) {
                OutImporting.setOutImporting(false);
                if(importEntity != null){
                    ImportService importService = Evn.getBean("studentFlowService");
                    importEntity.setStatus(ImportEntity.IMPORT_STATUS_ERROR);
                    importService.saveAllEntitys(importEntity);
                }
            }
        }
        finally {
        }

    }

}

class OutImporting {

    private static OutImporting outImporting;

    private static boolean isOutImporting = false;

    private OutImporting() {
    }

    public static OutImporting getInstance() {
        if (outImporting == null) {
            outImporting = new OutImporting();
        }
        return outImporting;
    }

    public static boolean isOutImporting() {
        return isOutImporting;
    }

    public static void setOutImporting(boolean isOutImporting) {
        OutImporting.isOutImporting = isOutImporting;
    }

}
