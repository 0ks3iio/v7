package net.zdsoft.familydear.dao;


import net.zdsoft.familydear.entity.FamilyDearObject;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * family_dear_object 
 * @author 
 * 
 */
public interface FamilyDearObjectDao extends BaseJpaRepositoryDao<FamilyDearObject,String> {

    @Query("From FamilyDearObject where identityCard=?1 and id !=?2")
    public List<FamilyDearObject> getDearObjByIdentityCard(String identitycard, String id);
    @Query("From FamilyDearObject where id in ?1 and state =?2")
    public List<FamilyDearObject> getAllByidsAndState(String[] ids, String state);
    @Query("From FamilyDearObject where village like '%'||?1||'%'")
    public List<FamilyDearObject> findByVillageName(String villageName);
}
