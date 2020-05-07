package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.ClassHourEx;

public interface ClassHourExRemoteService extends BaseRemoteService<ClassHourEx,String>{

	
	String findListByHourIdIn(String[] classHourIds);
}
