package net.zdsoft.newstusys.dao;

import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.newstusys.entity.StudentAbnormalFlow;

import java.util.List;

/**
 * 
 * @author weixh
 * 2019年2月28日	
 */
public interface StudentAbnormalFlowJdbcDao {
	public void save(StudentAbnormalFlow flow);

	public List<StudentAbnormalFlow> findAbnormalFlowByFlowByStuid(String stuid, String[] flowtypes);

	public List<StudentAbnormalFlow> findFlowByStuidsType(String schoolId, String[] stuids, String[] flowtypes);
	
	public List<StudentAbnormalFlow> findAbnormalFlowByFlowTypes(
			String[] unitId, String[] flowTypes, Pagination page);
	
	public List<StudentAbnormalFlow> findAbnormalFlowStudent(String schoolId,
			String acadyear, String semester, String[] flowTypes);

	public List<StudentAbnormalFlow> findAbnormalFlowStudentSection(String schoolId,
															 String acadyear, String semester, String section, String[] flowTypes);
}
