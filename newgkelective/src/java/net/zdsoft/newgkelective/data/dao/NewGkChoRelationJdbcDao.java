package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;

public interface NewGkChoRelationJdbcDao {
	public void insertBatch(List<NewGkChoRelation> list);

	void deleteByChoiceIdAndObjectTypeIn(String unitId, String choiceId, String[] objectTypes);
	
	void deleteByChoiceIdTypeStu(String unitId, String choiceId,String objectType, String[] studentId);
	
	/**
	 * 
	 * @param unitId 必填
	 * @param objectType 必填
	 * @param choiceIds 可选
	 */
	void deleteByTypeChoiceIds(String unitId, String objectType, String[] choiceIds);
	
}
