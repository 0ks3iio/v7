package net.zdsoft.eclasscard.data.scheduler;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.service.EccClientLogService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;


public class EccClearLogJob implements Job{
	private static Logger logger = Logger.getLogger(EccOpenCloseTimeJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//保存15天数据
		logger.info("定时清除日志文件");
		EccClientLogService eccClientLogService=Evn.getBean("eccClientLogService");
		Date nowDate=new Date();
		Date beforeDate = DateUtils.addDay(nowDate, -15);
		EccInfoService eccInfoService=Evn.getBean("eccInfoService");
		List<EccInfo> infoList=eccInfoService.findAll();
		if(CollectionUtils.isNotEmpty(infoList)) {
			Set<String> unitIds = EntityUtils.getSet(infoList,e->e.getUnitId());
			String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
					"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
			//fileSystemPath="G:\\11";
			File dir = new File(fileSystemPath);
			if (!dir.exists()) {
				// 不存在不处理
				return;
			}
			for(String unitId:unitIds) {
				String filePath = fileSystemPath+File.separator+"ecclogs" + File.separator+ unitId;
				File dir1 = new File(filePath);
				if(!dir1.exists()) {
					return;
				}
				//找到该文件下文件夹
				if(!dir1.isDirectory()) {
					return;
				}
				File[] files = dir1.listFiles();
				if(files!=null && files.length>0) {
					for(File f:files) {
						if(f.isDirectory()) {
							String fName=f.getName();
							try {
								Date date1=DateUtils.string2Date(fName, "yyyyMMdd");
								if(DateUtils.compareForDay(date1, beforeDate)<0) {
									delFile(f);
								}
							}catch (Exception e) {
								logger.error(fName+":EccClearLogJob:"+e.getMessage());
							}
						}
					}
				}
			}
		}
		
		try {
			Date beforeDate2 = DateUtils.addDay(nowDate, -16);
			eccClientLogService.deleteBeforeByDay(beforeDate2);
		}catch (Exception e) {
			logger.error("EccClearLogJob:删除日志失败"+e.getMessage());
		}
	}
	
	public  boolean delFile(File file) {
         if (!file.exists()) {
             return false;
         }
         if (file.isFile()) {
            return file.delete();
          } else {
            File[] files = file.listFiles();
            for (File f : files) {
                 delFile(f);
            }
            return file.delete();
        }
	}


}

