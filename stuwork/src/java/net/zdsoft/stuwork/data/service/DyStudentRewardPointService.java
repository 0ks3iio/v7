package net.zdsoft.stuwork.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.stuwork.data.dto.DyStudentRewardPointDto;
import net.zdsoft.stuwork.data.dto.DyStuworkDataCountDto;
import net.zdsoft.stuwork.data.entity.DyStudentRewardPoint;

import java.util.List;
import java.util.Map;

public interface DyStudentRewardPointService extends BaseService<DyStudentRewardPoint, String>{
	/**
	 * 
	 * @param schoolId
	 * @param studentIds 可为空
	 * @param acadyear
	 * @param semester
	 * @param page
	 * @param flag
	 * @return
	 */
	public List<DyStudentRewardPoint> getStudentRewardPointByStudentId(String schoolId,String[] studentIds,String acadyear,String semester,Pagination page,boolean flag);
	
	public List<DyStudentRewardPoint> getStudentRewardPointByStucode(String schoolId,String studentCode, String acadyear,String semester,Pagination page);

	public List<DyStudentRewardPoint> findBySettingIdStuSearch(String settingId, int semester, String acadyear, String fieldType, String search);

	public List<DyStudentRewardPoint> findBySettingId(String settingId, int semester, String acadyear);

	public void saveDto(DyStudentRewardPointDto pointDto, String acadyear,
			String semester);

	public DyStuworkDataCountDto findStuworkCountByStudentId(Map<String, Integer> maxValueMap, String studentId,
			Integer maxValuePer, boolean isShow);

	public Map<String, Float> findStuworkCountByUnitId(Map<String, Integer> maxValueMap, String unitId, Integer maxValuePer, Map<String, Boolean> showMap);

	public void setRecoverStuScore(String[] studentIds,Map<String,Integer> stuYearMap,String acadyear);

	public void deleteBySettingId(String settingId);
	
	public void deleteByProjectId(String projectId);
	/**
	 * 软删
	 * @param projectIds
	 */
	public void updateIsDeteletd(String[] projectIds);
	
	public String saveImport(List<String[]> datas, String unitId, String classesType, String acadyear, String semester, String coverType);

	public void addOtherPoint(String remark, String stuId, String acadyear, String semester, String unitId);

	public Map<String, DyStuworkDataCountDto> findStuworkCountByStudentIds(Map<String, Integer> maxValueMap, String classId, String[] studentIds, Integer maxValuePer, boolean isShow);

	public Map<String, String> findXkjsByStudentIds(String unitId, String acadyear, String semester, String[] studentIds);
}
