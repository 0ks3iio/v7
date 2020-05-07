package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkChoResult;

public interface NewGkChoResultJdbcDao {
	public void insertBatch(List<NewGkChoResult> list);
	/**
	 * 
	 * @param unitId 必填
	 * @param choiceId必填
	 * @param stuIds 可选
	 */
	void deleteByChoiceIdAndStudentIdIn(String unitId,String choiceId, String[] stuIds);
}
