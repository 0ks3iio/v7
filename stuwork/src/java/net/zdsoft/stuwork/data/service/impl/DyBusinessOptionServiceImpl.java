package net.zdsoft.stuwork.data.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.dao.DyBusinessOptionDao;
import net.zdsoft.stuwork.data.entity.DyBusinessOption;
import net.zdsoft.stuwork.data.service.DyBusinessOptionService;
@Service("dyBusinessOptionService")
public class DyBusinessOptionServiceImpl extends BaseServiceImpl<DyBusinessOption, String> implements DyBusinessOptionService{
    @Autowired
	private DyBusinessOptionDao dyBusinessOptionDao;
	
	@Override
	protected BaseJpaRepositoryDao<DyBusinessOption, String> getJpaDao() {
		return dyBusinessOptionDao;
	}

	@Override
	protected Class<DyBusinessOption> getEntityClass() {
		return DyBusinessOption.class;
	}

	@Override
	public List<DyBusinessOption> findListByUnitId(String unitId) {
		return dyBusinessOptionDao.findListByUnitId(unitId);
	}
	@Override
	public List<DyBusinessOption> findListByUnitIdAndType(String unitId,
			String businessType) {
		return dyBusinessOptionDao.findListByUnitIdAndType(unitId,businessType);
	}
	@Override
	public List<DyBusinessOption> findListByUidBtypeAndOname(String unitId, String businessType,String optionName){
		return dyBusinessOptionDao.findListByUidBtypeAndOname(unitId,businessType,optionName);
	}
	@Override
	public void deleteByUIdBType(String unitId,String businessType){
		deleteByUIdBType(unitId, businessType);
	}
	@Override
	public void save(List<DyBusinessOption> dyBusinessOptionList,
			String unitId, String businessType) {
		dyBusinessOptionDao.deleteByUnitIdAndType(unitId, businessType);
		dyBusinessOptionDao.saveAll(dyBusinessOptionList);		
	}

	@Override
	public void deleteByUnitIdAndType(String unitId, String businessType) {
		dyBusinessOptionDao.deleteByUnitIdAndType(unitId, businessType);
	}

	@Override
	public List<DyBusinessOption> findListByUidBtypeAndOrderId(String unitId,
			String businessType, int orderId) {
		return dyBusinessOptionDao.findListByUidBtypeAndOrderId(unitId, businessType, orderId);
	}

	@Override
	public void deleteAndOrder(String id, String unitId, String businessType) {		
		//dyBusinessOptionDao.delete(id);
		List<DyBusinessOption> dyBusinessOptionList = dyBusinessOptionDao.findListByUnitIdAndType(unitId,businessType);
		List<DyBusinessOption> dyBusinessOptionList2 = new ArrayList<DyBusinessOption>();
		for(DyBusinessOption item : dyBusinessOptionList){
			if(id.equals(item.getId())){
				dyBusinessOptionList2.add(item);
			}
		}
		dyBusinessOptionList.removeAll(dyBusinessOptionList2);
		List<DyBusinessOption> dyBusinessOptionList3 = new ArrayList<DyBusinessOption>();
		int i = 1;
		for(DyBusinessOption item : dyBusinessOptionList){
			DyBusinessOption newItem = new DyBusinessOption();
			newItem.setOrderId(i);
			newItem.setId(UuidUtils.generateUuid());
			newItem.setHasScore(item.getHasScore());
			newItem.setBusinessType(businessType);
			newItem.setIsCustom(item.getIsCustom());
			newItem.setUnitId(unitId);
			newItem.setOptionName(item.getOptionName());
			newItem.setScore(item.getScore());
			dyBusinessOptionList3.add(newItem);
			i++;
		}
		dyBusinessOptionDao.deleteByUnitIdAndType(unitId, businessType);
		dyBusinessOptionDao.saveAll(dyBusinessOptionList3);
	}

	@Override
	public void saveAndOrder(String unitId, String businessType,
			List<DyBusinessOption> dyBusinessOptionList) {
		dyBusinessOptionDao.deleteByUnitIdAndType(unitId, businessType);
		dyBusinessOptionDao.saveAll(dyBusinessOptionList);
	}

}
