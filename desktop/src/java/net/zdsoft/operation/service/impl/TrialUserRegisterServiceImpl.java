package net.zdsoft.operation.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.operation.dao.TrialUserRegisterDao;
import net.zdsoft.operation.entity.TrialUser;
import net.zdsoft.operation.service.TrialUserRegisterService;
@Service
public class TrialUserRegisterServiceImpl implements TrialUserRegisterService {
	
	@Autowired
	private TrialUserRegisterDao trialUserRegisterDao;
	
	@Override
	public void insertTrialUser(TrialUser trialUser) {
		String id=UuidUtils.generateUuid();
		Date createTime = Calendar.getInstance().getTime();
		trialUser.setCreationTime(createTime);
		trialUser.setId(id);
		trialUserRegisterDao.save(trialUser);
		
	}
	

}
