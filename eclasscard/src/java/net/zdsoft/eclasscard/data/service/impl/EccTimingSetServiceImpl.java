package net.zdsoft.eclasscard.data.service.impl;

import com.google.common.collect.Lists;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.dao.EccTimingSetDao;
import net.zdsoft.eclasscard.data.dto.TimingSetDto;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccTimingSet;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccTimingSetService;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("eccTimingSetService")
public class EccTimingSetServiceImpl extends BaseServiceImpl<EccTimingSet,String> implements EccTimingSetService{

	@Autowired
	private EccTimingSetDao eccTimingSetDao;
	@Autowired
	private EccInfoService eccInfoService;
	
	@Override
	public List<EccTimingSet> findByUnitId(String unitId) {
		return eccTimingSetDao.findByUnitId(unitId);
	}
	
	@Override
	public void saveTimingSet(TimingSetDto timingSetDto) {
		eccTimingSetDao.deleteByUnitId(timingSetDto.getUnitId());
		List<EccTimingSet> eccTimingSets = Lists.newArrayList();
		EccTimingSet eccTimingSet = null;
		Date date = new Date();
		for (String code : timingSetDto.getWeekChoose()) {
			eccTimingSet = new EccTimingSet();
			eccTimingSet.setId(UuidUtils.generateUuid());
			eccTimingSet.setUnitId(timingSetDto.getUnitId());
			eccTimingSet.setCode(code);
			eccTimingSet.setOpenTime(timingSetDto.getOpenTime());
			eccTimingSet.setCloseTime(timingSetDto.getCloseTime());
			eccTimingSet.setCreationTime(date);
			eccTimingSet.setUpdateTime(date);
			if (Objects.equals("MON",code)) {
				eccTimingSet.setOrderIndex(1);
			} else if (Objects.equals("TUES",code)) {
				eccTimingSet.setOrderIndex(2);
			} else if (Objects.equals("WED",code)) {
				eccTimingSet.setOrderIndex(3);
			} else if (Objects.equals("THUR",code)) {
				eccTimingSet.setOrderIndex(4);
			} else if (Objects.equals("FRI",code)) {
				eccTimingSet.setOrderIndex(5);
			} else if (Objects.equals("SAT",code)) {
				eccTimingSet.setOrderIndex(6);
			} else if (Objects.equals("SUN",code)) {
				eccTimingSet.setOrderIndex(0);
			}
			eccTimingSets.add(eccTimingSet);
		}
		eccTimingSetDao.saveAll(eccTimingSets);
		List<EccInfo> eccInfos = eccInfoService.findByUnitId(timingSetDto.getUnitId());
		List<String> sids = EntityUtils.getList(eccInfos, EccInfo::getId);
		if (CollectionUtils.isNotEmpty(sids)) {
			EccNeedServiceUtils.postBeMsg(sids, EccNeedServiceUtils.POST_MSG_TYPE_BE_0010, null);
		}
	}
	
	@Override
	protected BaseJpaRepositoryDao<EccTimingSet, String> getJpaDao() {
		return eccTimingSetDao;
	}

	@Override
	protected Class<EccTimingSet> getEntityClass() {
		return EccTimingSet.class;
	}

}
