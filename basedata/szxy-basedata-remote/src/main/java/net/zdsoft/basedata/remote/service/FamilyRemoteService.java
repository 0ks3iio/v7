package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.Family;

/**
 * @author yangsj  2017-1-23下午2:02:48
 */
public interface FamilyRemoteService extends BaseRemoteService<Family, String> {
	
	public String findByStudentIds(String[] stuIds);

	/**
	 * @param studentId
	 * @return
	 */
    public String findByStudentId(String studentId);

	/**
	 * @param studentId
	 * @param phoneNum
	 * @return
	 */
	public String findByStudentIdPhoneNum(String studentId, String phoneNum);

	/**
	 * @param unitId (schoolId)
	 * @return
	 */
	public String findByUnitId(String unitId);

	String findByRealNameAndIdentityCard(String realName, String identityCard);

	public String findByRealNameAndIdentityCardWithNoUser(String realName,
			String identityCard);

	public String findByRealNameAndMobilePhone(String realName,
			String mobilePhone);

	/**
	 * @param RealName
	 * @param mobilePhone
	 * @return
	 */
	public String findByRealNameInAndMobilePhoneIn(String[] array, String[] array2);

	public String  findListByCondition(Family searchfamily);

	public String findByIdentityCardIn(String[] identityCards);

	public String findByRealNameInOrMobilePhoneIn(String[] realNames,String[] mobilePhones);
	
}
