package net.zdsoft.officework.remote.service;


import java.util.Set;
public interface OfficeAttanceRemoteService{
	
	/**
	 * 获取要考勤的单位ids
	 */
	Set<String> getAttUnitIds();
	
	/**
	 * 获取在当前教室的学生
	 * @param unitId
	 * @param placeId
	 * @param beginTime
	 * @param endTime
	 * @param stuIds
	 * @return
	 */
	Set<String> findInClassStudentIds(String unitId, String placeId,
			Set<String> stuIds);

}