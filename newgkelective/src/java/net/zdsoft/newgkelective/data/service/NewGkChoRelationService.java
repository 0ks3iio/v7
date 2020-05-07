package net.zdsoft.newgkelective.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;

public interface NewGkChoRelationService extends BaseService<NewGkChoRelation, String>{
	
	Map<String, String> findByChoiceIdsAndObjectTypeAndObjVal(String unitId, String[] choiceIds, String choiceType, String ObjectValues);

	List<NewGkChoRelation> findByChoiceIdAndObjectTypeAndObjectValueIn(
			String unitId, String choiceId, String choiceType, String[] ObjectValues);

	List<String> findByChoiceIdAndObjectType(String unitId, String choiceId, String objectType);
	
	List<String> findByChoiceIdAndObjectTypeWithMaster(String unitId, String choiceId, String objectType);

	void deleteByChoiceIdType(String unitId, String choiceId,
			String objectType, String... studentIds);
	
	void deleteByTypeChoiceIds(String unitId, String objectType, String... choiceIds);

//	void saveList(List<NewGkChoRelation> newGkChoRelations);
	/**
	 * 
	 * @param newGkChoRelations
	 * @param choiceId 传null 只增不删
	 * @param unitId TODO
	 */
	void saveAndDeleteList(List<NewGkChoRelation> newGkChoRelations,
			String choiceId, String unitId);
//in方法不走索引而且多个类型需要多次查下	
//	List<NewGkChoRelation> getNewGkChoRelationsByChoiceIdAndType(
//			String[] choiceId, String objectType);
	/**
	 * 取得选课科目
	 * @param choiceId
	 * @param unitId TODO
	 * @return
	 */
	List<Course> findChooseSubject(final String choiceId, String unitId);
	Map<String, List<NewGkChoRelation>> findByChoiceIdAndObjectTypeIn(String unitId,String choiceId,String[] objectTypes);
	/**
	 * key=ObjectType
	 * 一次性查出所有类型 缓存半分钟
	 * @param gradeId
	 * @return
	 */
	Map<String, List<NewGkChoRelation>> findByGradeId(String gradeId);

	List<NewGkChoRelation> findByChoiceIdsAndObjectType(String unitId, String[] choiceIds, String objectType);

    List<NewGkChoRelation> findByChoiceIdsAndObjectTypeWithMaster(String unitId, String[] choiceIds, String objectType);

	/**
	 * 
	 * @param saveList
	 * @param choiceIds
	 * @param unitId 必填
	 * @param choiceType 必填
	 */
	void saveAndDeleteByList(List<NewGkChoRelation> saveList, String[] choiceIds, String unitId, String choiceType);

	void deleteByObjectTypeVal(String divideId, String subjectId);
	
	List<NewGkChoRelation> findByChoiceIdAndObjectTypeAndObjectTypeValIn(
			String unitId, String choiceId, String choiceType, String[] objectTypeVals);
}
