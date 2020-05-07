package net.zdsoft.studevelop.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StudevelopDutySituation;

public interface StudevelopDutySituationService extends BaseService<StudevelopDutySituation, String>{

	/**
	 * 查找学生任职详情
	 * @param stuId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	public List<StudevelopDutySituation> getDutySituationList(String stuId,String acadyear, String semester);

	/**
	 * 通过id找任职详情
	 * @param id
	 * @return
	 */
	public StudevelopDutySituation findById(String id);

	/**
	 * 查找班级里的任职情况
	 * @param acadyear
	 * @param semester
	 * @param array
	 * @return
	 */
	public List<StudevelopDutySituation> findListByCls(String acadyear,
			String semester, String[] array);

	public String saveImportDutySituationDatas(String unitId , List<String[]> datas ,StudevelopDutySituation dutySituation);
	
}
