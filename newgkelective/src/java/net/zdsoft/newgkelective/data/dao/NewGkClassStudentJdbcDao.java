package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;

public interface NewGkClassStudentJdbcDao {
	
	public void insertBatch(List<NewGkClassStudent> newGkClassStudentList);
	/**
	 * 这两个参数三选一 否则不会生效
	 * @param unitId ids可以不带其他必须带
	 * @param divideId
	 * @param classIds
	 * @param ids
	 */
	public void deleteBydivideIdOrClassIds(String unitId,String divideId,String[] classIds, String[] ids);

	public void deleteByClassIdAndStuids(String classId,String[] stuids);
	
	public List<String> findArrangeStudentIdWithMaster(String divideId, String classType);
}
