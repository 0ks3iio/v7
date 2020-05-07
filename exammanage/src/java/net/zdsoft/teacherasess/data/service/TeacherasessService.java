package net.zdsoft.teacherasess.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.teacherasess.data.entity.TeacherAsess;

public interface TeacherasessService extends BaseService<TeacherAsess, String>{
	
	public List<TeacherAsess> findByUnitIdAndAcayearWithMaster(String unitId,String acayear);
	
	public List<TeacherAsess> saveAllEntitys(TeacherAsess... asesses);
	
	public void deleteByIdIn(String unitId,String...id);
	
	public void dealWithTeacherAsessResult(String unitId,String acadyear,String semester,String teacherAsessId);
	
	public void updateforStatus(String hasPass, String id);
}
