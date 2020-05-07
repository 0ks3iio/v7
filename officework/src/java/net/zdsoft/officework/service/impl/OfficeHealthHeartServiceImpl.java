package net.zdsoft.officework.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.officework.dao.OfficeHealthHeartDao;
import net.zdsoft.officework.entity.OfficeHealthData;
import net.zdsoft.officework.entity.OfficeHealthHeart;
import net.zdsoft.officework.service.OfficeHealthHeartService;
import net.zdsoft.officework.utils.OfficeUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service("officeHealthHeartService")
public class OfficeHealthHeartServiceImpl extends BaseServiceImpl<OfficeHealthHeart, String> implements OfficeHealthHeartService {
	
	@Autowired
	private OfficeHealthHeartDao officeHealthHeartDao;
	
	@Override
	protected BaseJpaRepositoryDao<OfficeHealthHeart, String> getJpaDao() {
		return officeHealthHeartDao;
	}

	@Override
	protected Class<OfficeHealthHeart> getEntityClass() {
		return OfficeHealthHeart.class;
	}

	@Override
	public void dealTeacherHeartData(String schoolId, String serialNumber,
			Map<String, String> teaCardMap, List<OfficeHealthData> heartDatas) {
		List<OfficeHealthHeart> healthHearts = Lists.newArrayList();
		Map<String, String> heartTimeMap = Maps.newHashMap();
		for(OfficeHealthData data :heartDatas){
			if(teaCardMap.containsKey(data.getWristbandId())){
				OfficeHealthHeart healthHeart = new OfficeHealthHeart();
				String uploadTime = DateUtils.date2StringByMinute(data.getUploadTime());
				healthHeart.setHeartValue(OfficeUtils.getLowByteInt(data.getDataValue()));
				if(healthHeart.getHeartValue()<1 || heartTimeMap.containsKey(data.getWristbandId()+uploadTime)){
					continue;
				}
				healthHeart.setId(UuidUtils.generateUuid());
				healthHeart.setCreateTime(new Date());
				healthHeart.setKinematic(OfficeUtils.getHightByte(data.getDataValue()));
				healthHeart.setOwnerId(teaCardMap.get(data.getWristbandId()));
				healthHeart.setOwnerType(User.OWNER_TYPE_TEACHER+"");
				healthHeart.setSerialNumber(serialNumber);
				healthHeart.setUploadTime(data.getUploadTime());
				healthHeart.setWristbandId(data.getWristbandId());
				healthHeart.setUnitId(schoolId);
				heartTimeMap.put(data.getWristbandId()+uploadTime, data.getWristbandId());
				healthHearts.add(healthHeart);
			}
		}
		if(CollectionUtils.isNotEmpty(healthHearts)){
			saveAll(healthHearts.toArray(new OfficeHealthHeart[healthHearts.size()]));
		}
	}


	
	
}
