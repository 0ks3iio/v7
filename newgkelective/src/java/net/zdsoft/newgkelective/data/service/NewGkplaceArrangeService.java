package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkplaceArrange;

public interface NewGkplaceArrangeService extends BaseService<NewGkplaceArrange, String>{

	public void savePlaceArrang(String arrayItemId, Boolean isAddArrayItem, NewGkDivide divide, List<NewGkplaceArrange> newGkplaceArrangeList);
	
	public List<NewGkplaceArrange> findByArrayItemId(String arrayItemId);
	public List<NewGkplaceArrange> findByArrayItemIdWithMaster(String arrayItemId);
	
	public void deleteByItemIdAndPlaceId(String arrayItemId, String placeId);
	/**
	 * @param arrayItemId
	 */
	public void deleteByItemId(String arrayItemId);
	
	public List<NewGkplaceArrange> findByArrayItemIds(String[] arrayItemIds);

	void updateBasicPlaceSet(String gradeId, NewGkplaceArrange[] all);
	
	void savePlaceArrangeModify(String arrayItemId, NewGkplaceArrange[] all, String[] placeIdArr);
}
