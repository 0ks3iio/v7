package net.zdsoft.eclasscard.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccInfoDao;
import net.zdsoft.eclasscard.data.dao.EccSignInDao;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccSignIn;
import net.zdsoft.eclasscard.data.service.EccSignInService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("eccSignInService")
public class EccSignInServiceImpl extends BaseServiceImpl<EccSignIn, String> implements EccSignInService{

	@Autowired
	private EccSignInDao eccSignInDao; 
	@Autowired
	private EccInfoDao eccInfoDao;
	
	@Override
	protected BaseJpaRepositoryDao<EccSignIn, String> getJpaDao() {
		return eccSignInDao;
	}

	@Override
	protected Class<EccSignIn> getEntityClass() {
		return EccSignIn.class;
	}

	@Override
	public List<EccSignIn> findByIdAndTime(String classId, String placeId, String startTime, String endTime) {
		List<EccSignIn> eccSignIns = null;
		if (EccConstants.ZERO32.equals(placeId)) {
			eccSignIns = eccSignInDao.findByClassIdAndTime(classId,startTime,endTime);
		} else {
			List<EccInfo> eccInfos = eccInfoDao.findByPlaceId(placeId);
			List<String> placeIdLsit = EntityUtils.getList(eccInfos, "id");
			eccSignIns = eccSignInDao.findByClassIdAndTimeAndPlaceIds(classId,startTime,endTime,placeIdLsit.toArray(new String[0]));
		}
		return eccSignIns;
	}

}
