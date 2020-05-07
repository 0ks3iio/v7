package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.Family;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface FamilyDao extends BaseJpaRepositoryDao<Family, String>,FamilyJdbcDao{

	/**
	 * @param studentId
	 * @return
	 */
	@Query("From Family where isDeleted = 0 and  studentId= ?1")
	List<Family> findByStudentId(String studentId);

	@Query("From Family where isDeleted = 0 and  studentId IN (?1)")
	List<Family> findByStudentIds(String[] stuIds);
	
	/**
	 * @param unitId 
	 * @return
	 */
	@Query("From Family where isDeleted = 0 and  schoolId= ?1")
	List<Family> findByUnitId(String unitId);

	/**
	 * @param studentId
	 * @param phoneNum
	 * @return
	 */
	@Query("From Family where isDeleted = 0 and  studentId= ?1 and mobilePhone=?2")
	List<Family> findByStudentIdPhoneNum(String studentId, String phoneNum);
	
	List<Family> findByRealNameAndIdentityCard(String realName, String identityCard);
	
	@Query("From  Family Where  realName = ?1 and identityCard=?2 and isDeleted = 0 and id not in (select ownerId from User u1 where u1.isDeleted = 0 and u1.ownerType= 3)")
	List<Family> findByRealNameAndIdentityCardWithNoUser(String realName,
			String identityCard);
	@Query("From  Family Where  realName = ?1 and mobilePhone=?2 and isDeleted = 0 ")
	List<Family> findByRealNameAndMobilePhone(String realName,
			String mobilePhone);

	/**
	 * @param realNames
	 * @param mobilePhones
	 * @return
	 */
	@Query("From  Family Where  realName in ?1 and mobilePhone in ?2 and isDeleted = 0 ")
	List<Family> findByRealNameInAndMobilePhoneIn(String[] realNames, String[] mobilePhones);

	@Modifying
	@Query(
			value = "update Family set isDeleted=1 where schoolId=?1"
	)
	void deleteFamiliesBySchoolId(String schoolId);
	/**
	 * @param phoneNum
	 * @return
	 */
	@Query("From Family where isDeleted = 0 and mobilePhone=?1")
	List<Family> findByPhoneNum(String phoneNum);

	@Query("From Family where isDeleted = 0 and identityCard in (?1)")
	List<Family> findByIdentityCardIn(String[] identityCards);

	@Query("From  Family Where  realName in ?1 or mobilePhone in ?2 ")
	List<Family> findByRealNameInOrMobilePhoneIn(String[] realNames,String[] mobilePhones);
}
