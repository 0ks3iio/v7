package net.zdsoft.eclasscard.data.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.dao.EccFaceLowerLogDao;
import net.zdsoft.eclasscard.data.entity.EccFaceActivate;
import net.zdsoft.eclasscard.data.entity.EccFaceLowerLog;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.service.EccFaceActivateService;
import net.zdsoft.eclasscard.data.service.EccFaceLowerLogService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
@Service("eccFaceLowerLogService")
public class EccFaceLowerLogServiceImpl  extends BaseServiceImpl<EccFaceLowerLog, String>implements EccFaceLowerLogService{

	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private EccFaceActivateService eccFaceActivateService;
	@Autowired
	private EccFaceLowerLogDao eccFaceLowerLogDao;
	@Override
	protected BaseJpaRepositoryDao<EccFaceLowerLog, String> getJpaDao() {
		return eccFaceLowerLogDao;
	}

	@Override
	protected Class<EccFaceLowerLog> getEntityClass() {
		return EccFaceLowerLog.class;
	}

	@Override
	public void faceLowerCheckAll() {
		List<EccFaceLowerLog> lowerLogs = Lists.newArrayList();
		Set<String> cardIds = Sets.newHashSet();
		//需要下发的，检测是否在线，在线则下发，保存日志
		List<EccFaceActivate> activates = eccFaceActivateService.findAllNeedLower();
		List<EccFaceActivate> saveActivates = Lists.newArrayList();
		Set<String> needCardIds = EntityUtils.getSet(activates, EccFaceActivate::getInfoId);
		if(CollectionUtils.isNotEmpty(needCardIds)){
			List<EccInfo> eccInfos = eccInfoService.findListOnLineByIds(needCardIds.toArray(new String[needCardIds.size()]));
			Set<String> onlineids = EntityUtils.getSet(eccInfos, EccInfo::getId);
			for(EccFaceActivate activate:activates){
				if(onlineids.contains(activate.getInfoId())){
					EccFaceLowerLog lowerLog = new EccFaceLowerLog();
					lowerLog.setId(UuidUtils.generateUuid());
					lowerLog.setType(1);
					lowerLog.setInfoId(activate.getInfoId());
					lowerLog.setUnitId(activate.getUnitId());
					boolean flag = lowerFaceByCard(activate,lowerLog);
					activate.setLastLowerTime(new Date());
					activate.setIsLower(1);
					activate.setNeedLower(0);
					lowerLog.setState(1);
					cardIds.add(activate.getInfoId());
					lowerLog.setCreationTime(new Date());
					lowerLog.setModifyTime(new Date());
					lowerLogs.add(lowerLog);
					saveActivates.add(activate);
				}
			}
		}
		if(CollectionUtils.isNotEmpty(saveActivates)){
			eccFaceActivateService.saveAll(saveActivates.toArray(new EccFaceActivate[saveActivates.size()]));
		}
		if(CollectionUtils.isNotEmpty(lowerLogs)){
			saveAll(lowerLogs.toArray(new EccFaceLowerLog[lowerLogs.size()]));
		}
	}
	
	@Override
	public boolean lowerFaceByCard(EccFaceActivate face,EccFaceLowerLog lowerLog){
		List<String> sids = Lists.newArrayList();
		sids.add(face.getInfoId());
		if (CollectionUtils.isNotEmpty(sids)) {
			EccNeedServiceUtils.postBeMsg(sids, EccNeedServiceUtils.POST_MSG_TYPE_BE_0011, null);
		}
		lowerLog.setDataUrl("");
		return true;
	}

}
