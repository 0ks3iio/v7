package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;

public interface NewGkChoRelationDao extends BaseJpaRepositoryDao<NewGkChoRelation, String>{

	@Query("select distinct(objectValue) from NewGkChoRelation where unitId=?1 and choiceId=?2 and objectType=?3 ")
	List<String> findByChoiceIdAndObjectType(String unitId, String choiceId, String objectType);
	
	List<NewGkChoRelation> findByUnitIdAndChoiceIdAndObjectTypeIn(String unitId,String choiceId, String[] objectTypes);
	
	@Query("From NewGkChoRelation where unitId=?1 and choiceId in (?2) and objectType=?3")
	List<NewGkChoRelation> findByChoiceIdInAndObjectType(String unitId, String[] choiceIds, String objectType);
	
	@Query("select t1 from NewGkChoRelation as t1,NewGkChoice as t2  where t2.gradeId=?1 and t2.isDeleted=0 and t1.unitId=t2.unitId and t2.id=t1.choiceId")
	List<NewGkChoRelation> findByGradeId(String gradeId);
	
	List<NewGkChoRelation> findByUnitIdAndChoiceIdAndObjectTypeAndObjectValueIn(String unitId,String choiceId, String choiceType, String[] objectValues);

	void deleteByChoiceIdAndObjectTypeVal(String divideId, String subjectId);
	
	List<NewGkChoRelation> findByUnitIdAndChoiceIdAndObjectTypeAndObjectTypeValIn(String unitId,String choiceId, String choiceType, String[] objectTypeVals);
}
