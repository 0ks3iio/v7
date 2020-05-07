package net.zdsoft.familydear.dao;

import net.zdsoft.familydear.entity.FamilyDearServant;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * family_dear_servant 
 * @author 
 * 
 */
public interface FamilyDearServantDao extends BaseJpaRepositoryDao<FamilyDearServant,String> {

    @Query("From FamilyDearServant where objectId in ?1 and isDeleted =?2")
    public List<FamilyDearServant> getListByObjIds(String[] objIds ,String isDeleted);
    
    @Query("From FamilyDearServant where teacherId in ?1 and isDeleted =0")
    public List<FamilyDearServant> getListByTeacherIds(String[] teacherIds);
}
