package net.zdsoft.eclasscard.data.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccTimingLog;
import net.zdsoft.eclasscard.data.entity.EccTimingSet;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccTimingLogService;
import net.zdsoft.eclasscard.data.service.EccTimingSetService;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.eclasscard.data.websocket.constant.EClassCardConstant;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.ArrayUtil;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.system.entity.config.UnitIni;
import net.zdsoft.system.remote.service.UnitIniRemoteService;

public class EccOpenCloseTimeJob implements Job{
	private static Logger logger = Logger.getLogger(EccOpenCloseTimeJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("通知定时开关机");
		EccTimingSetService eccTimingSetService=Evn.getBean("eccTimingSetService");
		List<EccTimingSet> list = eccTimingSetService.findAll();
		if(CollectionUtils.isNotEmpty(list)) {
			Map<String, List<EccTimingSet>> setList = EntityUtils.getListMap(list,e->e.getUnitId(),e->e);
			//获取时间
			UnitIniRemoteService unitIniRemoteService=Evn.getBean("unitIniRemoteService");
			List<UnitIni> unitIniList = SUtils.dt(unitIniRemoteService.getIniList(UnitIni.ECC_USE_OPEN_CLOSE),UnitIni.class);
			Set<String> useTimeUnitIds=new HashSet<>();
			for(UnitIni u:unitIniList) {
				if(StringUtils.isNotBlank(u.getNowvalue()) && "1".equals(u.getNowvalue())){
					useTimeUnitIds.add(u.getUnitid());
				}
			}
			List<String> sids=new ArrayList<String>();
			if(CollectionUtils.isNotEmpty(useTimeUnitIds)) {
				EccInfoService eccInfoService=Evn.getBean("eccInfoService");
				List<EccInfo> infoList = eccInfoService.findAll();
				if(CollectionUtils.isNotEmpty(infoList)) {
					List<EccInfo> needList = infoList.stream().filter(e->useTimeUnitIds.contains(e.getUnitId())).collect(Collectors.toList());
					
					if(CollectionUtils.isNotEmpty(needList)) {
						Map<String, List<EccInfo>> infoByUnitMap = EntityUtils.getListMap(needList, e->e.getUnitId(), e->e);
						EccTimingLogService eccTimingLogService=Evn.getBean("eccTimingLogService");
						List<EccTimingLog> logList = eccTimingLogService.findNewTimeList(useTimeUnitIds.toArray(new String[0]));
						Map<String, EccTimingLog> map1 = EntityUtils.getMap(logList, e->e.getCardId());
						
						for(Entry<String, List<EccInfo>> infoSet:infoByUnitMap.entrySet()) {
							String key=infoSet.getKey();
							List<EccTimingSet> timeSet1 = setList.get(key);
							if(CollectionUtils.isEmpty(timeSet1)) {
								//没有时间不下发
								continue;
							}
							//找到最晚时间
							Set<String> set1 = new HashSet<>();
							Date lastTime=listToSet(timeSet1,set1);
							
							List<EccInfo> valueList=infoSet.getValue();
							for(EccInfo ii:valueList) {
								if(!map1.containsKey(ii.getId())) {
									sids.add(ii.getId());
									continue;
								}
								//暂时不考虑同一分钟内多次保存
								if(map1.get(ii.getId()).getCreationTime()!=null || lastTime!=null) {
									if(DateUtils.compareIgnoreSecond(map1.get(ii.getId()).getCreationTime(), lastTime)<0) {
										sids.add(ii.getId());
										continue;
									}
								}
								
								String[] arr = map1.get(ii.getId()).getTimeRemark().split(";");
								//判断是不是一样 不一样需要通知
								for(String s:arr) {
									if(StringUtils.isNotBlank(s) && !set1.contains(s)) {
										sids.add(ii.getId());
										break;
									}
								}
							}
						}
						
					}
				}
			}
			if (CollectionUtils.isNotEmpty(sids)) {
				logger.info("通知定时开关机"+ArrayUtil.print(sids.toArray(new String[0])));
				EccNeedServiceUtils.postBeMsg(sids, EccNeedServiceUtils.POST_MSG_TYPE_BE_0010, null);
			}
			
		}
		//清除在线缓存
		logger.info("清除在线缓存");
		RedisUtils.del(EClassCardConstant.ECLASSCARD_REDIS_ONLINE_MEMBERS);
	}
	
	public Date listToSet(List<EccTimingSet> timeSetList,Set<String> timeStrSet){
		Date lastTime=null;
		for(EccTimingSet set:timeSetList) {
			String[] times = set.getOpenTime().split(":");
			String[] times1 = set.getCloseTime().split(":");
			timeStrSet.add(set.getCode()+"-"+Integer.parseInt(times[0])+":"+Integer.parseInt(times[1])+"-"+Integer.parseInt(times1[0])+":"+Integer.parseInt(times1[1]));
			if(set.getCreationTime()==null) {
				continue;
			}
			if(lastTime==null) {
				lastTime=set.getCreationTime();
			}else {
				if(DateUtils.compareIgnoreSecond(set.getCreationTime(), lastTime)>0) {
					lastTime=set.getCreationTime();
				}
			}
			
		}
		return lastTime;
	}
	

}
