package net.zdsoft.gkelective.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.gkelective.data.entity.GkTeachClassEx;

public interface GkTeachClassExService extends BaseService<GkTeachClassEx, String>{

	List<GkTeachClassEx> findGkTeachClassExList(String roundId, String[] teachClassIds);

	void saveAllEntitys(GkTeachClassEx... array);
	/**
	 * 查询某个批次下教学班平均分
	 * @param roundId
	 * @return key:教学班Id
	 */
	Map<String,GkTeachClassEx> findByGkRoundId(String roundId);
	
	public void deleteByIds(String[] ids);

}
