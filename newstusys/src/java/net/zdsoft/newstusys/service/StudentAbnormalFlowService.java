package net.zdsoft.newstusys.service;

import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.newstusys.entity.StudentAbnormalFlow;

import java.util.List;

/**
 * 学生异动
 * @author weixh
 * 2019年2月28日	
 */
public interface StudentAbnormalFlowService {
	/**
	 * 离校保存
	 * @param flow
	 */
	public void saveLeaveStu(StudentAbnormalFlow flow);

	/**
	 * 离校生转入保存
	 * @param flow
	 */
	public void saveInStu(StudentAbnormalFlow flow);
	
    public StudentAbnormalFlow findAbnormalFlowByFlowByStuid(String stuid, String[] flowtypes);

    public List<StudentAbnormalFlow> findFlowByStuidsType(String schoolId, String[] stuids, String[] flowtypes);
	
	public List<StudentAbnormalFlow> findAbnormalFlowByFlowTypes(String[] unitIds, String[] flowTypes, Pagination page);
	
	public List<StudentAbnormalFlow> findAbnormalFlowStudent(String schoolId, String acadyear, String semester, String[] flowTypes);

	public List<StudentAbnormalFlow> findAbnormalFlowStudentSec(String schoolId, String acadyear, String semester, String section, String[] flowTypes);
}
