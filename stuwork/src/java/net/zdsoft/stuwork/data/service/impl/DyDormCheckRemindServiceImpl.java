package net.zdsoft.stuwork.data.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.stuwork.data.dao.DyDormCheckRemindDao;
import net.zdsoft.stuwork.data.dto.DormSearchDto;
import net.zdsoft.stuwork.data.entity.DyDormCheckRemind;
import net.zdsoft.stuwork.data.service.DyDormCheckRemindService;

@Service("dyDormCheckRemindService")
public class DyDormCheckRemindServiceImpl extends BaseServiceImpl<DyDormCheckRemind, String> implements DyDormCheckRemindService{
	@Autowired
	private  DyDormCheckRemindDao dyDormCheckRemindDao;
	
	
	@Override
	public List<DyDormCheckRemind> getRemindByCon(String schoolId,String checkDate,String[] roomIds){
		return dyDormCheckRemindDao.getRemindByCon(schoolId, DateUtils.string2Date(checkDate,"yyyy-MM-dd"), roomIds);
	}
	
	@Override
	public Map<String, DyDormCheckRemind> getRemindMap(String schoolId,
			String buildingId, String checkDate) {
		//得到某天 某寝室楼下的提醒信息  为得到key为roomId的map
		/*List<DyDormCheckRemind> remindList=findListBy(new String[]{"schoolId","buildingId","week","day"}
											,new Object[]{schoolId,buildingId,week,day});*/
		List<DyDormCheckRemind> remindList=dyDormCheckRemindDao.findByCon(schoolId, buildingId,
												DateUtils.string2Date(checkDate,"yyyy-MM-dd"));
		
		Map<String,DyDormCheckRemind> remindMap=new HashMap<String,DyDormCheckRemind>();
		if(CollectionUtils.isNotEmpty(remindList)){
			for(DyDormCheckRemind remind:remindList){
				remindMap.put(remind.getRoomId(), remind);
			}
		}
		return remindMap;
	}
	@Override
	public Map<String,DyDormCheckRemind> getRemindMap(DormSearchDto dormDto){
		List<DyDormCheckRemind> remindList=findListBy(new String[]{"schoolId","acadyear","semester","week","day"}, 
				new Object[]{dormDto.getUnitId(),dormDto.getAcadyear(),dormDto.getSemesterStr(),dormDto.getWeek(),dormDto.getWeekDay()});
		//key-roomId  value提醒 空则代表 无扣分
		Map<String,DyDormCheckRemind> remindMap=EntityUtils.getMap(remindList, "roomId");
		/*if(CollectionUtils.isNotEmpty(remindList)){
			for(DyDormCheckRemind remind:remindList){
				remindStrMap.put(remind.getRoomId(), remind.getRemark());
			}
		}*/
		return remindMap;
	}
	@Override
	public void deleteByIds(String[] ids){
		dyDormCheckRemindDao.deletedByIds(ids);
	}
	@Override
	protected BaseJpaRepositoryDao<DyDormCheckRemind, String> getJpaDao() {
		return dyDormCheckRemindDao;
	}
	
	@Override
	protected Class<DyDormCheckRemind> getEntityClass() {
		return DyDormCheckRemind.class;
	}

	@Override
	public Map<String, DyDormCheckRemind> getRemindWeekMap(DormSearchDto dormDto, String[] roomIds) {
		List<DyDormCheckRemind> remindList=dyDormCheckRemindDao.findByCon(dormDto.getUnitId(), dormDto.getAcadyear(), dormDto.getSemesterStr(), dormDto.getWeek(), roomIds);
		Map<String,DyDormCheckRemind> remindMap = new HashMap<String, DyDormCheckRemind>();
		for(DyDormCheckRemind remind : remindList){
			remindMap.put(remind.getRoomId()+remind.getDay(), remind);
		}
		//Map<String,DyDormCheckRemind> remindMap=EntityUtils.getMap(remindList, "roomId");
		return remindMap;
	}
}
