package net.zdsoft.eclasscard.data.service;

import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccExamTime;

public interface EccExamTimeService extends BaseService<EccExamTime,String>{
	/**
	 * 考试那边推过来每场考试的时间，重复保存，根据examId，删除之前的
	 * @param examTimeArray
	 * @return
	 */
	public String saveOrUpdate(JSONArray examTimeArray);
	
	public void deleteByExamIds(String[] examIds);
	
	public List<EccExamTime> findByExamIds(String[] examIds);
	/**
	 * 从队列中取出task执行
	 * @param id
	 * @param isEnd
	 */
	public void examTimeTaskRun(String id, boolean isEnd);
	/**
	 * 加入考试时间到队列
	 */
	public void addExamTimeQueue();
	/**
	 * 获取结束时间大于当前时间的列表
	 * @return
	 */
	public List<EccExamTime> findByltEndTime();

	public String deleteBySubjectInfoIds(String[] subjectInfoId);
	
	public void examTimeUnitTaskRun(String id,Set<String> infoIds);

}
