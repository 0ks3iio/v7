package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;

public interface NewGkDivideClassJdbcDao {
	
	public void insertBatch(List<NewGkDivideClass> newGkDivideClassList);
	
	void deleteByIdInOrDivideId(String[] ids,String divideId);
	/**
	 * 
	 * @param divideId
	 * @param sourceType
	 * @param classType
	 * @param containChildren 包含被拆分的班级
	 * @return
	 */
	List<NewGkDivideClass> findByDivideIdAndClassType(String divideId,String sourceType,
			String classTypes[], boolean containChildren);
}
