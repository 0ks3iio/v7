package net.zdsoft.evaluation.data.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.evaluation.data.dao.TeachEvaluateItemOptionDao;
import net.zdsoft.evaluation.data.entity.TeachEvaluateItemOption;
import net.zdsoft.evaluation.data.entity.TeachEvaluateResult;
import net.zdsoft.evaluation.data.service.TeachEvaluateItemOptionService;
import net.zdsoft.evaluation.data.service.TeachEvaluateResultService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teachEvaluateItemOptionService")
public class TeachEvaluateItemOptionServiceImpl extends BaseServiceImpl<TeachEvaluateItemOption, String> implements TeachEvaluateItemOptionService{
	@Autowired
	private TeachEvaluateItemOptionDao teachevaluateItemOptionDao;
	@Autowired
	private TeachEvaluateResultService teachEvaluateResultService;
	
	
	@Override
	public List<TeachEvaluateItemOption> findByItemIds(String unitId,String[] itemIds){
		return teachevaluateItemOptionDao.findByItemIds(unitId, itemIds);
	}
	@Override
	public List<TeachEvaluateItemOption> findByUnitId(String unitId){
		return teachevaluateItemOptionDao.findByUnitId(unitId);
	}
	@Override
	public String  updateOption(String unitId,String itemId,List<TeachEvaluateItemOption> optionList){
		List<TeachEvaluateItemOption> insertList=new ArrayList<TeachEvaluateItemOption>();
		Set<String> optionIds=new HashSet<String>();
		if(CollectionUtils.isNotEmpty(optionList)){
			int i=1;
			for(TeachEvaluateItemOption option:optionList){
				if(StringUtils.isBlank(option.getOptionName())){//跳过空的部分
					continue;
				}
				if(StringUtils.isBlank(option.getId())){
					option.setId(UuidUtils.generateUuid());
				}else{
					optionIds.add(option.getId());
				}
				option.setItemId(itemId);
				option.setUnitId(unitId);
				option.setOptionNo(i);
				i++;
				insertList.add(option);
			}
			
			//List<TeachEvaluateResult> resultList=teachEvaluateResultService.findResultByItemId(unitId, itemId);
			List<TeachEvaluateItemOption> everOptionList=findByItemIds(unitId, new String[]{itemId});
			if(CollectionUtils.isNotEmpty(everOptionList)){
				/*if(CollectionUtils.isNotEmpty(resultList)){
					if(everOptionList.size()!=optionIds.size()){
						return "该指标已存在结果，不能删除原有选项";
					}
				}*/
				Set<String> deleteIds=new HashSet<String>();
				for(TeachEvaluateItemOption option:everOptionList){
					if(!optionIds.contains(option.getId())){
						deleteIds.add(option.getId());
					}
				}
				if(CollectionUtils.isNotEmpty(deleteIds)){
					teachevaluateItemOptionDao.delteByIds(deleteIds.toArray(new String[0]));
				}
			}
			//teachevaluateItemOptionDao.delteByItemId(unitId, itemId);
			saveAll(insertList.toArray(new TeachEvaluateItemOption[0]));
		}
		return "success";
	}
	@Override
	public void  deleteByItemId(String unitId,String itemId){
		teachevaluateItemOptionDao.delteByItemId(unitId, itemId);
	}
	@Override
	protected BaseJpaRepositoryDao<TeachEvaluateItemOption, String> getJpaDao() {
		return teachevaluateItemOptionDao;
	}

	@Override
	protected Class<TeachEvaluateItemOption> getEntityClass() {
		return TeachEvaluateItemOption.class;
	}

}
