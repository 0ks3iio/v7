package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkDivideEx;

public interface NewGkDivideExService extends BaseService<NewGkDivideEx, String>{

	public List<NewGkDivideEx> findByDivideId(String divideId);
	
	public List<NewGkDivideEx> findByDivideIdAndGroupType(String divideId, String groupType);
	
	/**
	 * 强删
	 * @param divideId
	 */
	public void deleteByDivideId(String divideId);

	public void saveAndDel(String divideId, NewGkDivideEx[] newGkDivideExs);
}
