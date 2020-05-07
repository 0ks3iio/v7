package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyStuStudyingFarming;

public interface DyStuStudyingFarmingService extends BaseService<DyStuStudyingFarming, String>{
	
	public List<DyStuStudyingFarming> findByUnitIdAndGradeIds(String unitId, String[] gradeIds);

	public List<DyStuStudyingFarming> findByUnitIdAndStudentIds(String unitId, String[] studentIds);

	public List<DyStuStudyingFarming> findByStudentIds(String[] studentIds);

	public void saveList(String unitId, String classId, List<DyStuStudyingFarming> dyStuStudyingFarmingList);

	public String saveImport(String unitId, List<String[]> datas);
	/**
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param studentIds 可为空
	 * @return
	 */
	public List<DyStuStudyingFarming>  findByUnitIdAndStuIds(String unitId, String acadyear, String semester,String[] studentIds);
}
 