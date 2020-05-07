package net.zdsoft.eclasscard.data.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.dto.ClassAttNumSumDto;
import net.zdsoft.eclasscard.data.entity.EccStuclzAttence;


public interface EccStuclzAttenceService extends BaseService<EccStuclzAttence, String>{

	public EccStuclzAttence findByStuIdClzAttId(String studentId,String classAttId);

	public List<EccStuclzAttence> findListByClassAttId(String classAttId,
			int classType, String classId,String type);

	public List<EccStuclzAttence> findListByClassAttId(String classAttId,
			int classType, String classId);
	
	public void updateStatus(String[] ids,int status);

	public List<ClassAttNumSumDto>  findByClassIdSum(String classId,Date beginDate,Date endDate);

	public List<EccStuclzAttence> findByStudentIdSum(String studentId,
			String bDate, String eDate);
	/**
	 * 每节课未打卡推送钉钉
	 * @param attIds
	 * @return
	 */
	public List<EccStuclzAttence> findByClassAttIdsNeedPush(String[] attIds);
	/**
	 * 这次考勤未打卡的请假学生
	 * @param classAttenceIds
	 * @param studentIds
	 */
	public List<EccStuclzAttence> findListLeaveStudent(String[] classAttenceIds, String[] studentIds);
	
	/**
	 * 从主库查询
	 * @param classAttId
	 * @return
	 */
	public List<EccStuclzAttence> findListByAttIdWithMaster(String classAttId);
	/**
	 * 这节课考勤的学生数据是否已经生成
	 * @param classAttId
	 * @return
	 */
	public boolean findIsInitWithMaster(String classAttId);

	public Set<String>  findListStuByAttIds(String[] attIds);
	
}
