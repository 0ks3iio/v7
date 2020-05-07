package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyStuHealthResult;

public interface DyStuHealthResultService extends BaseService<DyStuHealthResult, String>{

	/**
	 * 通过学年学期获取结果list
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	public List<DyStuHealthResult> findByUidAndSe(String unitId,String acadyear,String semester);

	/**
	 * @param unitId
	 * @param studentId
	 * @return
	 */
	List<DyStuHealthResult> findByUnitIdAndStuId(String unitId, String studentId);

	/**
	 * @param unitId
	 * @param datas
	 * @return
	 */
	public String doImport(String unitId,String acadyear,String semester, List<String[]> datas);

	public List<DyStuHealthResult> findOneByStudnetId(String unitId, String acadyear, String semester, String studentId);
	public List<DyStuHealthResult> findListByStudentIds(String unitId, String acadyear, String semester, String[] studentIds);
}

