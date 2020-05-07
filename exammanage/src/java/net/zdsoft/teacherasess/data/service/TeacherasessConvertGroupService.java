package net.zdsoft.teacherasess.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvertGroup;

public interface TeacherasessConvertGroupService extends BaseService<TeacherasessConvertGroup, String>{
	
	public List<TeacherasessConvertGroup> findListByConvertIdInWithMaster(String[] convertIds);
}
