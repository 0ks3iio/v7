package net.zdsoft.stuwork.data.service;

import net.zdsoft.stuwork.data.dto.DyQualityComprehensiveDto;

import java.util.Map;


public interface DyQualityComprehensiveService{

	public DyQualityComprehensiveDto getQualityScoreMap(Map<String,Integer> maxValueMap, String  studentId, boolean isShow);
	
	public Map<String, Float> getAllStudentQualityScoreMap(Map<String,Integer> maxValueMap, String unitId, Map<String, Boolean> showMap);

	public Map<String, DyQualityComprehensiveDto> getQualityScoreMapByStudentIds(Map<String, Integer> maxValueMap, String classId, String[] studentIds, boolean isShow);

	public void setRecoverStuScore(String[] studentIds,Map<String,Integer> stuYearMap,String acadyear);
}
