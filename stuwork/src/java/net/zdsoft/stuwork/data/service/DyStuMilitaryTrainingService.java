package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyStuMilitaryTraining;

public interface DyStuMilitaryTrainingService extends BaseService<DyStuMilitaryTraining, String>{
	
	public void saveList(List<DyStuMilitaryTraining> dyStuMilitaryTrainingList, String classId, String unitId);
	
	public List<DyStuMilitaryTraining> findByStudentIds(String[] studentIds);

	public String saveImport(String unitId, List<String[]> datas);
	
	public List<DyStuMilitaryTraining> findByStudentIdIn(String[] studentIds);

	public List<DyStuMilitaryTraining> findByUnitIdAndGradeIds(String unitId, String[] gradeIds);
	/**
	 * 
	 * @param unitId
	 * @param studentIds 可为空
	 * @return
	 */
	public List<DyStuMilitaryTraining> findByUnitIdAndStudentIds(String unitId,String[] studentIds,String acadyear,String semester);
}
