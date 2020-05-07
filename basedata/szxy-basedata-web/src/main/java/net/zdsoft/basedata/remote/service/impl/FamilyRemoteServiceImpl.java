package net.zdsoft.basedata.remote.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.FamilyDao;
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.remote.service.FamilyRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.FamilyService;
import net.zdsoft.framework.utils.SUtils;

/**
 * @author yangsj 2017-1-23下午2:04:19
 */
@Service("familyRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class FamilyRemoteServiceImpl extends BaseRemoteServiceImpl<Family,String> implements FamilyRemoteService {

    @Autowired
    private FamilyService familyService;

    @Autowired
    private FamilyDao familyDao;

    @Override
    protected BaseService<Family, String> getBaseService() {
        return familyService;
    }

    @Override
    public String findByStudentId(String studentId) {
        return SUtils.s(familyDao.findByStudentId(studentId));
    }
    
    public String findByStudentIds(String[] stuIds) {
    	List<Family> fams;
    	if(ArrayUtils.isEmpty(stuIds)) {
    		fams = new ArrayList<>();
    	} else {
    		fams = familyDao.findByStudentIds(stuIds);
    	}
    	return SUtils.s(fams);
    }

    @Override
    public String findByStudentIdPhoneNum(String studentId, String phoneNum) {
        return SUtils.s(familyDao.findByStudentIdPhoneNum(studentId, phoneNum));
    }

    @Override
    public String findByUnitId(String unitId) {
        return SUtils.s(familyDao.findByUnitId(unitId));
    }

    @Override
    public String findByRealNameAndIdentityCard(String realName, String identityCard) {
        return SUtils.s(familyService.findByRealNameAndIdentityCard(realName, identityCard));
    }
    
    @Override
    public String findByRealNameAndIdentityCardWithNoUser(String realName, String identityCard) {
        return SUtils.s(familyService.findByRealNameAndIdentityCardWithNoUser(realName, identityCard));
    }

	@Override
	public String findByRealNameAndMobilePhone(String realName,
			String mobilePhone) {
		return SUtils.s(familyService.findByRealNameAndMobilePhone(realName, mobilePhone));
	}

	@Override
	public String findByRealNameInAndMobilePhoneIn(String[] realNames, String[] mobilePhones) {
		return SUtils.s(familyDao.findByRealNameInAndMobilePhoneIn(realNames,mobilePhones));
	}

    @Override
    public String findListByCondition(Family searchfamily) {
        return SUtils.s(familyService.findListByCondition(searchfamily));
    }

	@Override
	public String findByIdentityCardIn(String[] identityCards) {
		return SUtils.s(familyDao.findByIdentityCardIn(identityCards));
	}

	@Override
	public String findByRealNameInOrMobilePhoneIn(String[] realNames,
			String[] mobilePhones) {
		return SUtils.s(familyDao.findByRealNameInOrMobilePhoneIn(realNames,mobilePhones));
	}
}
