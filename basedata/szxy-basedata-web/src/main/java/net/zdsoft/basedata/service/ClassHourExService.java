package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.ClassHourEx;

public interface ClassHourExService extends BaseService<ClassHourEx, String>{
	
	public List<ClassHourEx> findByClassHourIdIn(String[] classHourIds);

	public void deleteUpdateById(String id);

	public void deleteUpdateByClassHourIdIn(String[] classHourIds);
}
