package net.zdsoft.stuwork.data.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.stuwork.data.dao.DyWeekCheckItemDao;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItem;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItemDay;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItemRole;
import net.zdsoft.stuwork.data.entity.DyWeekCheckRoleUser;
import net.zdsoft.stuwork.data.service.DyWeekCheckItemDayService;
import net.zdsoft.stuwork.data.service.DyWeekCheckItemRoleService;
import net.zdsoft.stuwork.data.service.DyWeekCheckItemService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("dyWeekCheckItemService")
public class DyWeekCheckItemServiceImpl extends BaseServiceImpl<DyWeekCheckItem, String> implements DyWeekCheckItemService{
	@Autowired
	private DyWeekCheckItemDao dyWeekCheckItemDao;
	@Autowired
	private DyWeekCheckItemRoleService dyWeekCheckItemRoleService;
	@Autowired
	private DyWeekCheckItemDayService dyWeekCheckItemDayService;
	
	@Override
	public List<DyWeekCheckItem> findBySchoolAndDay(String unitId,
			Integer weekday) {
		List<DyWeekCheckItem> items = dyWeekCheckItemDao.findBySchoolId(unitId);
		if(CollectionUtils.isEmpty(items)){
			return items;
		}
		Set<String> itemIds = EntityUtils.getSet(items, "id");
		List<DyWeekCheckItemDay> dayList = dyWeekCheckItemDayService.findByItemIds(itemIds.toArray(new String[0]));
		List<DyWeekCheckItem> returnList = new ArrayList<DyWeekCheckItem>();
		boolean excit = false;
		for(DyWeekCheckItem item : items){
			for(DyWeekCheckItemDay day : dayList){
				if(StringUtils.equals(day.getItemId(), item.getId()) && weekday == day.getDay()){
					excit = true;
					break;
				}
			}
			if(excit){
				excit = false;
				returnList.add(item);
			}
		}
		List<DyWeekCheckItemRole> roleList = dyWeekCheckItemRoleService.findByItemIds(itemIds.toArray(new String[0]));
		for(DyWeekCheckItem item : returnList){
			for(DyWeekCheckItemRole role : roleList){
				if(StringUtils.equals(role.getItemId(), item.getId())){
					if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_CLASS, role.getRoleType())){
						role.setRoleName("值周班");
					}else if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_GRADE, role.getRoleType())){
						role.setRoleName("年级组");
					}else if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_TEACHER, role.getRoleType())){
						role.setRoleName("值周干部");
					}else if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_STUDENT, role.getRoleType())){
						role.setRoleName("学生处");
					}else if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_DEFEND, role.getRoleType())){
						role.setRoleName("保卫处");
					}else if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_HEALTH, role.getRoleType())){
						role.setRoleName("体育老师");
					}else if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_HYGIENE, role.getRoleType())){
						role.setRoleName("卫生检查");
					}
					item.getItemRoles().add(role);
				}
			}
		}
		return returnList;
	}
	@Override
	public void deleteItem(String itemId) {
		dyWeekCheckItemRoleService.deleteByItemId(itemId);
		dyWeekCheckItemDayService.deleteByItemId(itemId);
		delete(itemId);
	}
	
	@Override
	public void saveItem(DyWeekCheckItem item) {
		if(StringUtils.isNotBlank(item.getId())){
			dyWeekCheckItemRoleService.deleteByItemId(item.getId());
			dyWeekCheckItemDayService.deleteByItemId(item.getId());
			delete(item.getId());
		}else{
			checkSave(item);
		}
		for(DyWeekCheckItemDay day : item.getItemDays()){
			day.setItemId(item.getId());
		}
		for(DyWeekCheckItemRole role : item.getItemRoles()){
			role.setItemId(item.getId());
		}
		save(item);
		dyWeekCheckItemDayService.saveList(item.getItemDays());
		dyWeekCheckItemRoleService.saveList(item.getItemRoles());
	}
	
	@Override
	public List<DyWeekCheckItem> findBySchoolId(String unitId) {
		List<DyWeekCheckItem> items = dyWeekCheckItemDao.findBySchoolId(unitId);
		if(CollectionUtils.isEmpty(items)){
			return items;
		}
		Set<String> itemIds = EntityUtils.getSet(items, DyWeekCheckItem::getId);
		List<DyWeekCheckItemDay> dayList = dyWeekCheckItemDayService.findByItemIds(itemIds.toArray(new String[0]));
		List<DyWeekCheckItemRole> roleList = dyWeekCheckItemRoleService.findByItemIds(itemIds.toArray(new String[0]));
		String[] dayNames = new String[]{"","周一","周二","周三","周四","周五","周六","周日"};
		for(DyWeekCheckItem item : items){
			for(DyWeekCheckItemDay day : dayList){
				if(StringUtils.equals(day.getItemId(), item.getId())){
					day.setDayName(dayNames[day.getDay()]);
					item.getItemDays().add(day);
					item.getDays().add(day.getDay()+"");
				}
			}
			for(DyWeekCheckItemRole role : roleList){
				if(StringUtils.equals(role.getItemId(), item.getId())){
					if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_CLASS, role.getRoleType())){
						role.setRoleName("值周班");
					}else if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_GRADE, role.getRoleType())){
						role.setRoleName("年级组");
					}else if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_TEACHER, role.getRoleType())){
						role.setRoleName("值周干部");
					}else if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_STUDENT, role.getRoleType())){
						role.setRoleName("学生处");
					}else if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_DEFEND, role.getRoleType())){
						role.setRoleName("保卫处");
					}else if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_HEALTH, role.getRoleType())){
						role.setRoleName("体育老师");
					}else if(StringUtils.equals(DyWeekCheckRoleUser.CHECK_HYGIENE, role.getRoleType())){
						role.setRoleName("卫生检查");
					}
					item.getItemRoles().add(role);
					item.getRoles().add(role.getRoleType());
				}
			}
		}
		return items;
	}
	
	@Override
	public List<DyWeekCheckItem> findByRoleTypes(String schoolId,
			Set<String> roleTypes) {
		List<DyWeekCheckItem> itemAlls = dyWeekCheckItemDao.findByRoleTypes(schoolId);
		Set<String> itemAllIds = EntityUtils.getSet(itemAlls, "id");
		if(CollectionUtils.isEmpty(itemAllIds)){
			return new ArrayList<DyWeekCheckItem>();
		}
		List<DyWeekCheckItemRole> itemRoles = dyWeekCheckItemRoleService.findByItemIds(itemAllIds.toArray(new String[0]));
		Set<String> itemIds = new HashSet<String>();
		for(int i = 0; i<itemRoles.size();i++){
			DyWeekCheckItemRole itemRole = itemRoles.get(i);
			if(roleTypes.contains(itemRole.getRoleType())){
				itemIds.add(itemRole.getItemId());
			}
		}
		if(CollectionUtils.isEmpty(itemIds)){
			return new ArrayList<DyWeekCheckItem>();
		}
		List<DyWeekCheckItem> items = findListByIdIn(itemIds.toArray(new String[0]));
		List<DyWeekCheckItemDay> itemDays = dyWeekCheckItemDayService.findByItemIds(itemIds.toArray(new String[0]));
		for(DyWeekCheckItem item : items){
			for(DyWeekCheckItemRole itemRole : itemRoles){
				if(StringUtils.equals(itemRole.getItemId(), item.getId())){
					item.getRoles().add(itemRole.getRoleType());
				}
			}
			for(DyWeekCheckItemDay itemDay : itemDays){
				if(StringUtils.equals(itemDay.getItemId(), item.getId())){
					item.getDays().add(itemDay.getDay()+"");
				}
			}
		}
		return items;
	}
	
	@Override
	public List<DyWeekCheckItem> findByRoleTypeAndCheckWeek(String unitId,
			String roleType, Integer week) {
		List<DyWeekCheckItem> itemAlls = dyWeekCheckItemDao.findByRoleTypes(unitId);
		Set<String> itemAllIds = EntityUtils.getSet(itemAlls, "id");
		List<DyWeekCheckItemRole> itemRoles = dyWeekCheckItemRoleService.findByItemIds(itemAllIds.toArray(new String[0]));
		Set<String> itemIds = new HashSet<String>();
		for(int i = 0; i<itemRoles.size();i++){
			DyWeekCheckItemRole itemRole = itemRoles.get(i);
			if(StringUtils.equals(roleType, itemRole.getRoleType())){
				itemIds.add(itemRole.getItemId());
			}
		}
		List<DyWeekCheckItemDay> itemDays = dyWeekCheckItemDayService.findByItemIds(itemIds.toArray(new String[0]));
		itemIds = new HashSet<String>();
		for(int i = 0; i<itemDays.size();i++){
			DyWeekCheckItemDay itemDay = itemDays.get(i);
			if(week == itemDay.getDay()){
				itemIds.add(itemDay.getItemId());
			}
		}
		List<DyWeekCheckItem> items = findListByIdIn(itemIds.toArray(new String[0]));
		return items;
	}
	
	@Override
	protected BaseJpaRepositoryDao<DyWeekCheckItem, String> getJpaDao() {
		return dyWeekCheckItemDao;
	}

	@Override
	protected Class<DyWeekCheckItem> getEntityClass() {
		return DyWeekCheckItem.class;
	}
	
}
