package net.zdsoft.eclasscard.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.dto.AttanceSearchDto;
import net.zdsoft.eclasscard.data.entity.EccStudormAttence;
import net.zdsoft.framework.entity.Pagination;


public interface EccStudormAttenceService extends BaseService<EccStudormAttence, String>{

	public List<EccStudormAttence> findByInAttIdInit(String[] dormAttIds);

	/**unitId  attDto参数 查找签到记录
	 * 
	 * List<EccStudormAttence>
	 */
	public List<EccStudormAttence> findListByDto(String unitId,AttanceSearchDto attDto,String acadyear,String semesterStr,Pagination page);
	/**unitId  attDto参数 汇总记录
	 * 
	 * List<EccStudormAttence>
	 */
	public List<EccStudormAttence> findStatByDto(String unitId,AttanceSearchDto attDto,String acadyear,String semesterStr);
	/**  studentId startTime endTime参数 查看记录
	 * 
	 * List<EccStudormAttence>
	 */
	public List<EccStudormAttence> findCheckByCon(String unitId,String studentId,String startTime,String endTime,Pagination page);
	/**
	 * 修改 状态 通过id
	 * @param id
	 * @param status
	 */
	public void updateStatus(String id,int status);

	public List<EccStudormAttence> findByDormAttIdsNeedPush(String[] attIds);
	/**
	 * 当前考勤请假未打卡学生
	 * @param dormAttenceIds
	 * @param studentIds
	 * @return
	 */
	public List<EccStudormAttence> findListLeaveStudent(String[] dormAttenceIds, String[] studentIds);
}
