package net.zdsoft.officework.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.officework.constant.OfficeConstants;
import net.zdsoft.officework.dto.OfficeHealthInfoDto;
import net.zdsoft.officework.entity.OfficeHealthDevice;
import net.zdsoft.officework.service.OfficeHealthCountService;
import net.zdsoft.officework.service.OfficeHealthDeviceService;
import net.zdsoft.officework.utils.OfficeHealthUtils;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TaskJobHealthInfoRun implements Job{
	private static final Logger log = Logger.getLogger(TaskJobHealthInfoRun.class);
	
	private static final String REDIS_HEALTH_COUNT_LOCK = "redis.health.count.lock.everyhour";
	private OfficeHealthCountService officeHealthCountService;
	private OfficeHealthDeviceService officeHealthDeviceService;
	
	public OfficeHealthCountService getOfficeHealthCountService(){
    	if(officeHealthCountService == null){
    		officeHealthCountService = Evn.getBean("officeHealthCountService");
    	}
    	return officeHealthCountService;
    }
	public OfficeHealthDeviceService getOfficeHealthDeviceService(){
		if(officeHealthDeviceService == null){
			officeHealthDeviceService = Evn.getBean("officeHealthDeviceService");
		}
		return officeHealthDeviceService;
	}
	
	@Override
	public void execute(JobExecutionContext jobec) throws JobExecutionException {
		//test
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date nowtime = new Date();
		String hourNow = DateUtils.date2String(nowtime, "yyyy-MM-dd HH");
		if(StringUtils.isNotBlank(hourNow)){
			hourNow = hourNow.replaceAll(" ", ".");
		}
		if(RedisUtils.hasLocked(REDIS_HEALTH_COUNT_LOCK+hourNow)){
			try{
				if(RedisUtils.get(REDIS_HEALTH_COUNT_LOCK+hourNow+".data") == null) {//防止冗余,只跑一次
					RedisUtils.set(REDIS_HEALTH_COUNT_LOCK+hourNow+".data", REDIS_HEALTH_COUNT_LOCK, 10*60);
					String msginfo = "------进入华三健康数据获取定时任务："+df.format(nowtime);
					System.out.println(msginfo);
					
					//如果nowtime为凌晨  则应该查询并更新前一天的数据 此时nowtime修改为前一天的凌晨
					Calendar cal = Calendar.getInstance();
					cal.setTime(nowtime);
					if(cal.get(Calendar.HOUR_OF_DAY) == 0){
						cal.add(Calendar.DATE, -1);
						nowtime = cal.getTime();
					}
					List<OfficeHealthDevice> devices = getOfficeHealthDeviceService().findByType(OfficeConstants.HEALTH_DEVICE_TYPE_04);
					Set<String> devsns = EntityUtils.getSet(devices, OfficeHealthDevice::getSerialNumber);
					if(devsns.size()==0){
						log.info("华三健康数据定时任务：没有配置对接学校,任务暂时停止");
						return;
					}
					List<OfficeHealthInfoDto> list = new ArrayList<OfficeHealthInfoDto>();
					for(String devSn : devsns){
						List<OfficeHealthInfoDto> dtolist = OfficeHealthUtils.getHealthInfos(devSn);
						list.addAll(dtolist);
					}
					getOfficeHealthCountService().saveOrUpdate(list, nowtime);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				RedisUtils.unLock(REDIS_HEALTH_COUNT_LOCK+hourNow);
			}
		}
		
	}
}
