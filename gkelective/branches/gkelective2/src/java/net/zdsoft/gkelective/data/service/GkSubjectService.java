package net.zdsoft.gkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.gkelective.data.entity.GkSubject;

public interface GkSubjectService extends BaseService<GkSubject, String>{

	/**
	 * 查询某一轮次的开班课程设置
	 * @param roundsId
	 * @param teachModel 可为null
	 * @return
	 */
	public List<GkSubject> findByRoundsId(String roundsId, Integer teachModel);
	
	public void saveOrUpdate(GkSubject... gkSubjects);

	/**
	 * 查询某一轮次走班数
	 * @param roundsId
	 * @param teachModel
	 * @return
	 */
	public int findSubNumByRoundsIdTeachModel(String roundsId, int teachModel);
	/**
	 * 同步删除预排老师信息,及科目信息
	 * @param roundsId
	 */
	public void deleteByRoundsId(String roundsId);
}
