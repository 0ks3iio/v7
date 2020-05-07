package net.zdsoft.eclasscard.remote.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

public interface EclasscardRemoteService {

	public void attancePerMinute(Map<String, String> unitIdMap);
	/**
	 * 获取某段时间（包含开始结束时间）补课日List<Date>
	 * @param gradeId  传空 返回空list
	 * @param beginDate 传空默认当天
	 * @param endDate 传空默认当天
	 * @return
	 */
	public List<Date> getInfoDateList(String unitId,String gradeId,Date beginDate,Date endDate);
	
	/**
	 * 同步考勤时间到班牌，用于班牌到时间取考场门贴展示
	 * @param examTimeArray   
	 * 该array必须包含四个String类型字段examId，subjectId，beginTime，endTime,subType,unitIds(,分割的字符串)   时间格式   yyyy-MM-dd HH:mm
	 * @return 1:成功   0 ：失败
	 */
	public String syncExamTimeToEClassCard(JSONArray examTimeArray);
	/**
	 * 删除科目考试信息时同步删除
	 * @param subjectId
	 * @return
	 */
	public String syncDeleteExamTimeTo(String[] subjectInfoId);
	
	/**
	 * 班牌当天学生考勤对外接口
	 * @param unitId
	 * @param studentId
	 * @param placeId
	 * @param sectionNumber  节次
	 * @param clockTime     考勤时间
	 * @return  返回1，考勤成功，不必重复        返回0，失败，间隔时间重复     返回-1，失败，不必重复
	 */
	public int stuClassAttance(String unitId,String studentId,String placeId,Integer sectionNumber,Date clockTime);

}
