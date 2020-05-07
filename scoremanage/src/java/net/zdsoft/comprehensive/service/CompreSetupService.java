package net.zdsoft.comprehensive.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.comprehensive.entity.CompreSetup;

public interface CompreSetupService extends BaseService<CompreSetup, String>{

	public void deleteByInfoIdAndSubId(String compreInfoId, String subjectId);

	public CompreSetup findByAll(String unitId, String examId, String subjectId, String infoId);

	public List<CompreSetup> findByUnitIdAndSubIdAndInfoId(String unitId,String subjectId, String comInfoId);

	public void saveAllSetup(String unitId, String subjectId, String comInfoId,List<CompreSetup> compreSetups);

	public void deleteByInfo(String compreInfoId);

	public void deleteAndSave(String comInfoId, String unitId,String subjectId, List<CompreSetup> compreSetups);

//分割线，以上为原有方法，方便后续删除

	public List<CompreSetup> findByUnitIdAndInfoId(String unitId, String infoId);
	
	public List<CompreSetup> findByUnitIdAndInfoIdAndType(String unitId, String infoId, String type);

	public void deleteByInfoIdAndType(String unitId, String infoId, String type);
}
