package net.zdsoft.eclasscard.data.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccTeaclzAttence;


public interface EccTeaclzAttenceService extends BaseService<EccTeaclzAttence, String>{

	public EccTeaclzAttence findByAttId(String id);

	public List<EccTeaclzAttence> findListByAttIds(String[] attIds);
	/**
	 * 部门下老师考勤统计
	 * @param deptId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<EccTeaclzAttence> findByDeptIdSum(String deptId,
			Date beginDate, Date endDate);

	public List<EccTeaclzAttence> findByTeacherIdSum(String teacherId,
			String bDate, String eDate);
	
}
