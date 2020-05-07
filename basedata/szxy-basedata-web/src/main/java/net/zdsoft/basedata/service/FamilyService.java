package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.Family;

public interface FamilyService extends BaseService<Family, String> {

    List<Family> saveAllEntitys(Family... family);

    List<Family> findByRealNameAndIdentityCard(String realName, String identityCard);

    List<Family> findByRealNameAndIdentityCardWithNoUser(String realName,
                                                         String identityCard);

    List<Family> findByRealNameAndMobilePhone(String realName, String mobilePhone);

    List<Family> findListByCondition(Family searchfamily);

    void deleteFamiliesBySchoolId(String schoolId);

	List<Family> findByPhoneNum(String mobile);
	
	List<Family> findByStudentIds(String[] stuIds);
}
