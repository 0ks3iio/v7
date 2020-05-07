package net.zdsoft.comprehensive.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.comprehensive.entity.CompreInfo;

public interface CompreInfoService extends BaseService<CompreInfo, String>{

	/**
	 * 获取某学年学期下各个年级的总评成绩
	 * @param unitId
	 * @param searchAcadyear
	 * @param searchSemester
	 * @return
	 */
	public List<CompreInfo> findByUnitIdAndAcadyearAndSemester(String unitId,
			String searchAcadyear, String searchSemester);

	public CompreInfo findByGradeId(String gradeId, String acadyear, String semester);

	public void deleteAll(String id);

//	public void deleteAndsaveByInfoAndRel(String compreInfoId, CompreInfo compreInfo,List<CompreRelationship> compreRelList,List<String> delSubList);
	
//分割线，以上为原有方法，方便后续删除
	
	public List<CompreInfo> findByUnitIdAndGradeIds(String unitId, String[] gradeIds);

    public CompreInfo findOneByGradeIdAndGradeCode(String unitId, String gradeId, String gradeCode);

}
