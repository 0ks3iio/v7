/*
* Project: v7
* Author : shenke
* @(#) StudentInImportJop.java Created on 2016-10-10
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.component;

import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zdsoft.basedata.entity.ImportEntity;
import net.zdsoft.basedata.service.ImportService;
import net.zdsoft.basedata.service.StudentFlowService;
import net.zdsoft.framework.config.Evn;

/**
 * @description: TODO
 * @author: shenke
 * @version: 1.0
 * @date: 2016-10-10下午1:37:05
 */
@DisallowConcurrentExecution
public class StudentInImportJop implements Job{

	private static final Logger log = LoggerFactory.getLogger(StudentInImportJop.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		//InImporting inImporting = InImporting.getInstance();
		log.info("检查导入服务……");
		ImportEntity importEntity = null;
		try {
        	if(!InImporting.isInImporting()){
        		log.info("调入导入服务开始导入……");
        		InImporting.setInImporting(true);
        		importEntity = ((StudentFlowService) Evn.getBean("studentFlowService")).checkStudentFlow(ImportEntity.IMPORT_TYPE_IN);
        		if(importEntity!=null)
        			((StudentFlowService) Evn.getBean("studentFlowService")).importStudentFlow(importEntity,ImportEntity.IMPORT_TYPE_IN);
        		log.info("调入导入服务导入完成");
        		InImporting.setInImporting(false);
        	}else{
        		log.info("调入导入服务导入ing……");
        	}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.info("调入导入服务出错"+e.getMessage());
			if(InImporting.isInImporting()){
				InImporting.setInImporting(false);
				if(importEntity !=null){
					importEntity.setStatus(ImportEntity.IMPORT_STATUS_WRONG);
					importEntity.setModifyTime(new Date());
					((ImportService) Evn.getBean("importService")).saveAllEntitys(importEntity);
				}
			}
		} finally{
		}
    }

}
class InImporting{
    
	private static InImporting inImporting;
	
    private static boolean isInImporting =false;
    
    private InImporting() { }
    
    public static InImporting getInstance(){
          if(inImporting==null){
        	  inImporting =  new InImporting();
          }
          return inImporting;
    }

	public static boolean isInImporting() {
		return isInImporting;
	}

	public static void setInImporting(boolean isInImporting) {
		InImporting.isInImporting = isInImporting;
	}
    
}
