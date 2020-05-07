package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideStusub;

public interface NewGkDivideStusubService extends BaseService<NewGkDivideStusub, String>{
	
	/**
	 * 
	 * @param newGkDivide
	 * @param isSix 是不是3+1+2
	 * @param subjectIdArr isSix:true 验证是不是subjectIdArr2个科目中选中一个 如果subjectIdArr==null 默认取物理，历史
	 * @return 返回错误信息
	 */
	public String saveChoiceResult(NewGkDivide newGkDivide,boolean isSix,String[] subjectIdArr);
	
	public void deleteByChoiceIdAndDivideId(String choiceId,String divideId);

	public List<NewGkDivideStusub> findByDivideIdWithMaster(String divideId,String subjectType, String[] subjectIdArr);

	public List<NewGkDivideStusub> findListByStudentIdsWithMaster(String divideId,String subjectType,String[] studentIds);
	/**
	 * 查询未安排（指的是未安排到组合班）的学生
	 * @param divideId
	 * @param subjectIdArr
	 * @return
	 */
	public List<NewGkDivideStusub> findNoArrangeXzbStudentWithMaster(String divideId, String[] subjectIdArr);

	public void deleteByChoiceIdAndDivideIdAndStudentId(String choiceId, String divideId, String studentId);

}
