package net.zdsoft.evaluation.data.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.evaluation.data.dao.TeachEvaluateItemDao;
import net.zdsoft.evaluation.data.dto.OptionDto;
import net.zdsoft.evaluation.data.entity.TeachEvaluateItem;
import net.zdsoft.evaluation.data.entity.TeachEvaluateItemOption;
import net.zdsoft.evaluation.data.service.TeachEvaluateItemOptionService;
import net.zdsoft.evaluation.data.service.TeachEvaluateItemService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teachEvaluateItemService")
public class TeachEvaluateItemServiceImpl extends BaseServiceImpl<TeachEvaluateItem, String> implements TeachEvaluateItemService{
	@Autowired
	private TeachEvaluateItemDao teachEvaluateItemDao;
	@Autowired
	private TeachEvaluateItemOptionService teachEvaluateItemOptionService;
	
	@Override
	public void saveItem(String unitId,TeachEvaluateItem evaItem){
		evaItem.setUnitId(unitId);
		evaItem.setProjectId(BaseConstants.ZERO_GUID);
		checkSave(evaItem);
		save(evaItem);
		
		List<TeachEvaluateItemOption>  optionList=evaItem.getOptionList();
		List<TeachEvaluateItemOption> insertList=new ArrayList<TeachEvaluateItemOption>();
		if(CollectionUtils.isNotEmpty(optionList)){
			int i=1;
			for(TeachEvaluateItemOption option:optionList){
				if(StringUtils.isBlank(option.getOptionName())){//跳过空的部分
					continue;
				}
				option.setId(UuidUtils.generateUuid());
				option.setUnitId(unitId);
				option.setItemId(evaItem.getId());
				option.setOptionNo(i);
				i++;
				insertList.add(option);
			}
			teachEvaluateItemOptionService.saveAll(insertList.toArray(new TeachEvaluateItemOption[0]));
		}
	}
	@Override
	public List<TeachEvaluateItem> findByCon(String unitId,String evaluateType,String itemType){
		return teachEvaluateItemDao.findByDto(unitId,itemType,evaluateType,BaseConstants.ZERO_GUID);
	}
	@Override
	public List<TeachEvaluateItem> findForCheck(String unitId,String evaluateType,String itemName){
		return teachEvaluateItemDao.findForCheck(unitId,evaluateType,itemName,BaseConstants.ZERO_GUID);
	}
	@Override
	public List<TeachEvaluateItem> findByDto(String unitId,OptionDto dto){
		List<TeachEvaluateItem> itemList=teachEvaluateItemDao.findByDto(unitId, dto.getItemType(), dto.getEvaluateType(),BaseConstants.ZERO_GUID);
		return setOptionList(unitId,itemList);
	}
	@Override
	public List<TeachEvaluateItem> findByUidAndEvaType(String unitId,String evaluateType,String projectId){
		List<TeachEvaluateItem> itemList=teachEvaluateItemDao.findByUnitIdAndEvaluateType(unitId,evaluateType,projectId);
		return setOptionList(unitId,itemList);
	}
	public List<TeachEvaluateItem> setOptionList(String unitId,List<TeachEvaluateItem> itemList){
		if(CollectionUtils.isNotEmpty(itemList)){
			Set<String> itemIds=new HashSet<String>();
			for(TeachEvaluateItem item:itemList){
				itemIds.add(item.getId());
			}
			//获取选项名称
			List<TeachEvaluateItemOption> optionList=teachEvaluateItemOptionService.findByItemIds(unitId, itemIds.toArray(new String[0]));
			Map<String,List<TeachEvaluateItemOption>> optionMap=new HashMap<String, List<TeachEvaluateItemOption>>();
			if(CollectionUtils.isNotEmpty(optionList)){
				for(TeachEvaluateItemOption option:optionList){
					List<TeachEvaluateItemOption> inList=optionMap.get(option.getItemId());
					if(CollectionUtils.isEmpty(inList)){
						inList=new ArrayList<TeachEvaluateItemOption>();
					}
					inList.add(option);
					optionMap.put(option.getItemId(),inList);
				}
			}
			for(TeachEvaluateItem item:itemList){
				item.setOptionList(optionMap.get(item.getId()));
			}
		}
		return itemList;
	}
	@Override
	public List<TeachEvaluateItem> findByEvaluateType(String unitId,
			String evaluateType,String projectId) {
		List<TeachEvaluateItem> itemList=teachEvaluateItemDao.findByUnitIdAndEvaluateType(unitId, evaluateType,projectId);
		if(CollectionUtils.isEmpty(itemList)){
			itemList = teachEvaluateItemDao.findByUnitIdAndEvaluateType(unitId, evaluateType, BaseConstants.ZERO_GUID);
		}
		if(CollectionUtils.isEmpty(itemList)){
			return new ArrayList<TeachEvaluateItem>();
		}
		Set<String> itemIds = EntityUtils.getSet(itemList, "id");
		List<TeachEvaluateItemOption> optionList=teachEvaluateItemOptionService.findByItemIds(unitId, itemIds.toArray(new String[0]));
		for (TeachEvaluateItem item : itemList) {
			for (TeachEvaluateItemOption option : optionList) {
				if(StringUtils.equals(option.getItemId(), item.getId())){
					item.getOptionList().add(option);
				}
			}
			if(item.getOptionList().size()>0){
				Collections.sort(item.getOptionList(), new Comparator<TeachEvaluateItemOption>(){
					@Override
					public int compare(TeachEvaluateItemOption o1,
							TeachEvaluateItemOption o2) {
						return o1.getOptionNo()-o2.getOptionNo();
					}});
			}
		}
		return itemList;
	}
	@Override
	protected BaseJpaRepositoryDao<TeachEvaluateItem, String> getJpaDao() {
		return teachEvaluateItemDao;
	}

	@Override
	protected Class<TeachEvaluateItem> getEntityClass() {
		return TeachEvaluateItem.class;
	}
	
	@Override
	public void deleteByProjectId(String projectId) {
		List<TeachEvaluateItem> items = teachEvaluateItemDao.findByProjectId(projectId);
		if(CollectionUtils.isNotEmpty(items)){
			Set<String> itemIds = EntityUtils.getSet(items, "id");
			deleteAll(items.toArray(new TeachEvaluateItem[0]));
			List<TeachEvaluateItemOption> options = teachEvaluateItemOptionService.findByItemIds(items.get(0).getUnitId(), itemIds.toArray(new String[0]));
			teachEvaluateItemOptionService.deleteAll(options.toArray(new TeachEvaluateItemOption[0]));
		}
	}
}
