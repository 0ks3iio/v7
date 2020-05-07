package net.zdsoft.scoremanage.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.scoremanage.data.entity.JoinexamschInfo;

public interface JoinexamschInfoService extends BaseService<JoinexamschInfo, String>{

	void deleteByExamInfoId(String examInfoId);

	List<JoinexamschInfo> findByExamInfoId(String examInfoId);

	List<JoinexamschInfo> saveAllEntitys(JoinexamschInfo... joinexamschInfo);

	public void deleteAllByIds(String... id);
	
}
