package net.zdsoft.eclasscard.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.dao.EccStuLeaveInfoDao;
import net.zdsoft.eclasscard.data.entity.EccStuLeaveInfo;
import net.zdsoft.eclasscard.data.service.EccStuLeaveInfoService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service("eccStuLeaveInfoService")
public class EccStuLeaveInfoServiceImpl extends BaseServiceImpl<EccStuLeaveInfo,String> implements EccStuLeaveInfoService{

	@Autowired
	private EccStuLeaveInfoDao eccStuLeaveInfoDao;
	
	@Override
	protected BaseJpaRepositoryDao<EccStuLeaveInfo, String> getJpaDao() {
		return eccStuLeaveInfoDao;
	}

	@Override
	protected Class<EccStuLeaveInfo> getEntityClass() {
		return EccStuLeaveInfo.class;
	}

	@Override
	public List<EccStuLeaveInfo> findByStuDormAttIdIn(String[] stuDormAttIds) {
		return eccStuLeaveInfoDao.findByStuDormAttIdIn(stuDormAttIds);
	}

	@Override
	public List<EccStuLeaveInfo> findByLeaveIdIn(String[] leaveIds) {
		List<EccStuLeaveInfo> eccStuLeaveInfos = Lists.newArrayList();
		if (leaveIds.length < 1000) {
			eccStuLeaveInfos = eccStuLeaveInfoDao.findByLeaveIdIn(leaveIds);
		} else {
			List<String> leaveIdsList = Arrays.asList(leaveIds);
			int count = 500;
			int len = leaveIds.length;
			int size = len % count;
			if (size == 0) {
				size = len / count;
			} else {
				size = (len / count) + 1;
			}
			for (int i = 0; i < size; i++) {
				int fromIndex = i * count;
				int toIndex = fromIndex + count;
				if (toIndex > len) {
					toIndex = len;
				}
				List<String> leaveIdList = leaveIdsList.subList(fromIndex, toIndex);
				eccStuLeaveInfos.addAll(eccStuLeaveInfoDao.findByLeaveIdIn(leaveIdList.toArray(new String[leaveIdList.size()])));
			}
		}
		return eccStuLeaveInfos;
	}

}
