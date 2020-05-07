package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.TeachGroupEx;

public interface TeachGroupExRemoteService extends BaseRemoteService<TeachGroupEx, String>{

	String findByTeachGroupId(String[] teachGroupIds);

}
