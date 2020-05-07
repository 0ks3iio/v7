package net.zdsoft.basedata.component;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zdsoft.basedata.entity.ImportEntity;
import net.zdsoft.basedata.service.ClassFlowService;
import net.zdsoft.basedata.service.ImportService;
import net.zdsoft.framework.config.Evn;

/**
 * @author shenke
 * @since 2017/2/6 17:12
 */
public class ClassFlowImportJob implements Job{

    private static final Logger LOG = LoggerFactory.getLogger(ClassFlowImportJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info("[检查转班导入服务]");
        ImportEntity importEntity = null;
        ImportService importService = Evn.getBean("importService");
        try{
            if(!ClassFlowImport.isIsInImporting()){
                LOG.info("转班导入服务开始");
//                System.out.println("转班导入服务开始");
                ClassFlowImport.setIsInImporting(true);

                importEntity = importService.checkImportEntity(ImportEntity.IMPORT_TYPE_CLASSFLOW);
                if(importEntity != null){
                    ClassFlowService classFlowService = Evn.getBean("classFlowService");
                    classFlowService.batchImport(importEntity);
                }
                ClassFlowImport.setIsInImporting(false);
            }else{
                System.out.println("转班服务导入中");
            }
        }catch(Exception e){
            e.printStackTrace();
            LOG.error("转班导入出错"+e.getMessage());
            if(ClassFlowImport.isIsInImporting()){
                ClassFlowImport.setIsInImporting(false);
                if(importEntity != null){
                    importEntity.setStatus(ImportEntity.IMPORT_STATUS_ERROR);
                    importEntity.setModifyTime(new Date());
                    importService.saveAllEntitys(importEntity);
                }
            }
        }
        finally {

        }
    }

}
class ClassFlowImport{
    private static ClassFlowImport inImporting;
    private static boolean isImporting = false;
    private ClassFlowImport(){
    }
    public static ClassFlowImport getInstance(){
            if(inImporting == null){
                inImporting = new ClassFlowImport();
            }
        return inImporting;
    }

    public static boolean isIsInImporting() {
        return isImporting;
    }
    public static void setIsInImporting(boolean isInImporting) {
        ClassFlowImport.isImporting = isInImporting;
    }
}
