package net.zdsoft.teacherasess.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvertExam;

public interface TeacherasessConvertExamService extends BaseService<TeacherasessConvertExam, String>{

	public List<TeacherasessConvertExam> findListByConvertIdInWithMaster(String[] convertIds);
	
}