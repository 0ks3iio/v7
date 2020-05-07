package net.zdsoft.eclasscard.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccOtherSetDao;
import net.zdsoft.eclasscard.data.dto.EccOtherSetDto;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccOtherSet;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccOtherSetService;
import net.zdsoft.eclasscard.data.utils.PushPotoUtils;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service("eccOtherSetService")
public class EccOtherSetServiceImpl extends BaseServiceImpl<EccOtherSet,String> implements EccOtherSetService{

	@Autowired
	private EccOtherSetDao eccOtherSetDao;
	@Autowired
	private EccInfoService eccInfoService;
	
	@Override
	protected BaseJpaRepositoryDao<EccOtherSet, String> getJpaDao() {
		return eccOtherSetDao;
	}

	@Override
	protected Class<EccOtherSet> getEntityClass() {
		return EccOtherSet.class;
	}

	@Override
	public void updateOtherSet(Integer nowvalue,String unitId,Integer type) {
		eccOtherSetDao.updateOtherSet(nowvalue,unitId,type);
		RedisUtils.del(EccConstants.ECC_OTHER_SET+"_"+unitId+"_"+type);
	}

	@Override
	public EccOtherSet findByUnitIdAndType(String unitId, Integer type) {
		EccOtherSet eccOtherSet = RedisUtils.getObject(EccConstants.ECC_OTHER_SET+"_"+unitId+"_"+type, EccOtherSet.class);
		if (eccOtherSet==null) {
			eccOtherSet = eccOtherSetDao.findByUnitIdAndType(unitId,type);
			if (eccOtherSet != null) {
				RedisUtils.setObject(EccConstants.ECC_OTHER_SET+"_"+unitId+"_"+type, eccOtherSet);
			}
		} 
		return eccOtherSet;
	}
	
	@Override
	public void saveOtherSet(String unitId, Integer nowValue, Integer type) {
		EccOtherSet eccOtherSet = findByUnitIdAndType(unitId, type);
		if (eccOtherSet != null) {
			updateOtherSet(nowValue,unitId,type);
		} else {
			EccOtherSet eccSet = new EccOtherSet();
			eccSet.setId(UuidUtils.generateUuid());
			eccSet.setNowvalue(nowValue);
			eccSet.setUnitId(unitId);
			eccSet.setType(type);
			save(eccSet);
		}
		if (EccConstants.ECC_OTHER_SET_2 == type) {
			List<EccInfo> eccInfos = eccInfoService.findByUnitId(unitId);
			Set<String> cardNumbers = eccInfos.stream().map(c -> c.getId()).collect(Collectors.toSet());
			PushPotoUtils.addPushCards(cardNumbers, unitId);
		}
	}

	@Override
	public boolean openProperty(String unitId, Integer type) {
		EccOtherSet set = findByUnitIdAndType(unitId, type);
		if(set != null && EccConstants.ECC_START_USING_1==set.getNowvalue()){
			return true;
		}
		return false;
	}

	@Override
	public List<EccOtherSet> findByOpenAndType(Integer nowvalue, Integer type) {
		return eccOtherSetDao.findByOpenAndType(nowvalue, type);
	}

	@Override
	public List<EccOtherSet> findListByUnitId(String unitId) {
		return eccOtherSetDao.findListByUnitId(unitId);
	}

	@Override
	public void saveOtherSet(EccOtherSetDto otherSetDto,String unitId) {
		List<EccOtherSet> eccOtherSets = findListByUnitId(unitId);
		Map<Integer,EccOtherSet> otherMaps = EntityUtils.getMap(eccOtherSets, EccOtherSet::getType);
		List<EccOtherSet> insertOtherSets = Lists.newArrayList();
		if(otherMaps.containsKey(EccConstants.ECC_OTHER_SET_1)){
			EccOtherSet eccSet =otherMaps.get(EccConstants.ECC_OTHER_SET_1);
			if(otherSetDto.isScreenRadio()){
				eccSet.setNowvalue(1);
			}else{
				eccSet.setNowvalue(0);
			}
			insertOtherSets.add(eccSet);
		}else{
			EccOtherSet eccSet = new EccOtherSet();
			eccSet.setId(UuidUtils.generateUuid());
			eccSet.setUnitId(unitId);
			eccSet.setType(EccConstants.ECC_OTHER_SET_1);
			if(otherSetDto.isScreenRadio()){
				eccSet.setNowvalue(1);
			}else{
				eccSet.setNowvalue(0);
			}
			insertOtherSets.add(eccSet);
		}
		if(otherMaps.containsKey(EccConstants.ECC_OTHER_SET_2)){
			EccOtherSet eccSet =otherMaps.get(EccConstants.ECC_OTHER_SET_2);
			if(otherSetDto.isVoiceRadio()){
				eccSet.setNowvalue(1);
			}else{
				eccSet.setNowvalue(0);
			}
			insertOtherSets.add(eccSet);
		}else{
			EccOtherSet eccSet = new EccOtherSet();
			eccSet.setId(UuidUtils.generateUuid());
			eccSet.setUnitId(unitId);
			eccSet.setType(EccConstants.ECC_OTHER_SET_2);
			if(otherSetDto.isVoiceRadio()){
				eccSet.setNowvalue(1);
			}else{
				eccSet.setNowvalue(0);
			}
			insertOtherSets.add(eccSet);
		}
		if(otherMaps.containsKey(EccConstants.ECC_OTHER_SET_3)){
			EccOtherSet eccSet =otherMaps.get(EccConstants.ECC_OTHER_SET_3);
			if(otherSetDto.isDoorRadio()){
				eccSet.setNowvalue(1);
				eccSet.setParam(otherSetDto.getDoorDelayTime());
			}else{
				eccSet.setParam(otherSetDto.getDoorDelayTime());
				eccSet.setNowvalue(0);
			}
			insertOtherSets.add(eccSet);
		}else{
			EccOtherSet eccSet = new EccOtherSet();
			eccSet.setId(UuidUtils.generateUuid());
			eccSet.setUnitId(unitId);
			eccSet.setType(EccConstants.ECC_OTHER_SET_3);
			if(otherSetDto.isDoorRadio()){
				eccSet.setNowvalue(1);
				eccSet.setParam(otherSetDto.getDoorDelayTime());
			}else{
				eccSet.setParam(otherSetDto.getDoorDelayTime());
				eccSet.setNowvalue(0);
			}
			insertOtherSets.add(eccSet);
		}
		if (otherMaps.containsKey(EccConstants.ECC_OTHER_SET_4)) {
			EccOtherSet eccSet =otherMaps.get(EccConstants.ECC_OTHER_SET_4);
			if(Objects.equals(0, otherSetDto.getLoginRadio())){
				eccSet.setNowvalue(0);
			}else{
				eccSet.setNowvalue(1);
			}
			insertOtherSets.add(eccSet);
		} else {
			EccOtherSet eccSet = new EccOtherSet();
			eccSet.setId(UuidUtils.generateUuid());
			eccSet.setUnitId(unitId);
			eccSet.setType(EccConstants.ECC_OTHER_SET_4);
			if(Objects.equals(0, otherSetDto.getLoginRadio())){
				eccSet.setNowvalue(0);
			}else{
				eccSet.setNowvalue(1);
			}
			insertOtherSets.add(eccSet);
		}
		if (otherMaps.containsKey(EccConstants.ECC_OTHER_SET_5)) {
			EccOtherSet eccSet =otherMaps.get(EccConstants.ECC_OTHER_SET_5);
			eccSet.setParam(otherSetDto.getSpeedValue());
			insertOtherSets.add(eccSet);
		} else {
			EccOtherSet eccSet = new EccOtherSet();
			eccSet.setId(UuidUtils.generateUuid());
			eccSet.setUnitId(unitId);
			eccSet.setType(EccConstants.ECC_OTHER_SET_5);
			eccSet.setParam(otherSetDto.getSpeedValue());
			insertOtherSets.add(eccSet);
		}
		if (otherMaps.containsKey(EccConstants.ECC_OTHER_SET_6)) {
			EccOtherSet eccSet =otherMaps.get(EccConstants.ECC_OTHER_SET_6);
			eccSet.setNowvalue(otherSetDto.getFaceService());
			if(Objects.equals(otherSetDto.getFaceService(), 1)) {
				eccSet.setParam(otherSetDto.getPushThreshold()+","+otherSetDto.getCheckThreshold());
			}else {
				eccSet.setParam("");
			}
			insertOtherSets.add(eccSet);
		} else {
			EccOtherSet eccSet = new EccOtherSet();
			eccSet.setId(UuidUtils.generateUuid());
			eccSet.setUnitId(unitId);
			eccSet.setType(EccConstants.ECC_OTHER_SET_6);
			eccSet.setNowvalue(otherSetDto.getFaceService());
			insertOtherSets.add(eccSet);
		}
		if (otherMaps.containsKey(EccConstants.ECC_OTHER_SET_7)) {
			EccOtherSet eccSet =otherMaps.get(EccConstants.ECC_OTHER_SET_7);
			eccSet.setNowvalue(otherSetDto.getPrompt());
			insertOtherSets.add(eccSet);
		} else {
			EccOtherSet eccSet = new EccOtherSet();
			eccSet.setId(UuidUtils.generateUuid());
			eccSet.setUnitId(unitId);
			eccSet.setType(EccConstants.ECC_OTHER_SET_7);
			eccSet.setNowvalue(otherSetDto.getPrompt());
			insertOtherSets.add(eccSet);
		}
		RedisUtils.del(EccConstants.ECC_OTHER_SET+"_"+unitId+"_"+EccConstants.ECC_OTHER_SET_1);
		RedisUtils.del(EccConstants.ECC_OTHER_SET+"_"+unitId+"_"+EccConstants.ECC_OTHER_SET_2);
		RedisUtils.del(EccConstants.ECC_OTHER_SET+"_"+unitId+"_"+EccConstants.ECC_OTHER_SET_3);
		RedisUtils.del(EccConstants.ECC_OTHER_SET+"_"+unitId+"_"+EccConstants.ECC_OTHER_SET_4);
		RedisUtils.del(EccConstants.ECC_OTHER_SET+"_"+unitId+"_"+EccConstants.ECC_OTHER_SET_5);
		RedisUtils.del(EccConstants.ECC_OTHER_SET+"_"+unitId+"_"+EccConstants.ECC_OTHER_SET_6);
		RedisUtils.del(EccConstants.ECC_OTHER_SET+"_"+unitId+"_"+EccConstants.ECC_OTHER_SET_7);
		saveAll(insertOtherSets.toArray(new EccOtherSet[insertOtherSets.size()]));
	}

}
