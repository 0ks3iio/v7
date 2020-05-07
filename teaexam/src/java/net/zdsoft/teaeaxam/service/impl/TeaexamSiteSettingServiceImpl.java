package net.zdsoft.teaeaxam.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import net.zdsoft.teaeaxam.constant.TeaexamConstant;
import net.zdsoft.teaeaxam.dao.TeaexamRegisterInfoDao;
import net.zdsoft.teaeaxam.dao.TeaexamSiteSettingDao;
import net.zdsoft.teaeaxam.dao.TeaexamSubjectDao;
import net.zdsoft.teaeaxam.entity.TeaexamRegisterInfo;
import net.zdsoft.teaeaxam.entity.TeaexamSite;
import net.zdsoft.teaeaxam.entity.TeaexamSiteSetting;
import net.zdsoft.teaeaxam.entity.TeaexamSubject;
import net.zdsoft.teaeaxam.service.TeaexamSiteService;
import net.zdsoft.teaeaxam.service.TeaexamSiteSettingService;

/**
 * 
 * @author weixh
 * 2018年10月26日	
 */
@Service("teaexamSiteSettingService")
public class TeaexamSiteSettingServiceImpl extends BaseServiceImpl<TeaexamSiteSetting, String>
		implements TeaexamSiteSettingService {
	@Autowired
	private TeaexamSiteSettingDao teaexamSiteSettingDao;
	@Autowired
	private TeaexamRegisterInfoDao teaexamRegisterInfoDao;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private TeaexamSubjectDao teaexamSubjectDao;
	@Autowired
	private TeaexamSiteService teaexamSiteService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;

	@Override
	protected BaseJpaRepositoryDao<TeaexamSiteSetting, String> getJpaDao() {
		return teaexamSiteSettingDao;
	}

	@Override
	protected Class<TeaexamSiteSetting> getEntityClass() {
		return TeaexamSiteSetting.class;
	}
	
	public List<TeaexamSiteSetting> findSettingByExamId(String examId){
		return teaexamSiteSettingDao.findSettingByExamId(examId);
	}
	
	public List<TeaexamSiteSetting> findSettingBySubInfoId(String subInfoId){
		return teaexamSiteSettingDao.findSettingBySubInfoId(subInfoId);
	}
	
	public List<TeaexamSiteSetting> findChangeSettingBySubInfoId(String examId, String subInfoId, int teaSize, String nowRmNo){
		List<TeaexamSiteSetting> sets = new ArrayList<>();
		// 同一时段考试的科目
		List<TeaexamSubject> subs = teaexamSubjectDao.findSameTimeBySubId(subInfoId);
		if(CollectionUtils.isEmpty(subs)) {
			return sets;
		}
		Map<String, TeaexamSubject> subNames = EntityUtils.getMap(subs, TeaexamSubject::getId); 
		List<String> rms = teaexamRegisterInfoDao.findRoomNoBySubInfoId(examId, subNames.keySet().toArray(new String[0]));
		if(CollectionUtils.isEmpty(rms)) {
			return sets;
		}
		if(StringUtils.isNotEmpty(nowRmNo)) {
			rms.remove(nowRmNo);
		}
		rms.remove(null);
		if(CollectionUtils.isEmpty(rms)) {
			return sets;
		}
		List<TeaexamRegisterInfo> rmNum = teaexamRegisterInfoDao.findByExamIdRoomNos(examId, rms.toArray(new String[0]));
		// 考场容量
		List<TeaexamSite> sites = teaexamSiteService.findBySchoolIds(EntityUtils.getSet(rmNum, TeaexamRegisterInfo::getLocationId).toArray(new String[0]));
		if(CollectionUtils.isEmpty(sites)) {
			return sets;
		}
		Map<String, Integer> siteNumMap = EntityUtils.getMap(sites, TeaexamSite::getSchoolId, TeaexamSite::getCapacity);
		// 考场数据及已有考生数量统计
		Map<String, TeaexamSiteSetting> rmSet = new HashMap<>();
		Set<String> fullRms = new HashSet<>();// 已满的考场
		for(TeaexamRegisterInfo info : rmNum) {
			if(StringUtils.isEmpty(info.getRoomNo()) 
					|| StringUtils.isEmpty(info.getSeatNo())) {
				continue;
			}
			if(fullRms.contains(info.getRoomNo())) {
				if (rmSet.containsKey(info.getRoomNo())) {
					rmSet.remove(info.getRoomNo());
				}
				continue;
			}
			TeaexamSiteSetting set = rmSet.get(info.getRoomNo()); 
			if (set == null) {
				Integer cap = siteNumMap.get(info.getLocationId());
				if(cap == null || cap.intValue() < 1+teaSize) {// 考场容量不足，不能转入
					fullRms.add(info.getRoomNo());
					continue;
				}
				set = new TeaexamSiteSetting();
				set.setSchoolId(info.getLocationId());
				set.setPerNum(cap);
				set.setRoomNo(info.getRoomNo());
			}
			if(set.getPerNum()-set.getUsedNum() == teaSize) {// 考场还少一个位子，不能转入
				fullRms.add(info.getRoomNo());
				rmSet.remove(info.getRoomNo());
				continue;
			}
			String sid = StringUtils.trimToEmpty(set.getSubjectInfoId());// 考场的考试科目
			if(sid.indexOf(info.getSubjectInfoId()) == -1) {
				sid = sid + info.getSubjectInfoId() + ",";
				set.setSubjectInfoId(sid);
			}
			set.setUsedNum(set.getUsedNum() + 1);
			rmSet.put(info.getRoomNo(), set);
		}
		if (rmSet.size() == 0) {// 全满
			return sets;
		}
		CollectionUtils.addAll(sets, rmSet.values().iterator());
		sets = sets.stream().sorted((a,b)->{
			return NumberUtils.toInt(a.getRoomNo())-NumberUtils.toInt(b.getRoomNo());
		}).collect(Collectors.toList());
		// 考试科目名称组装
		Map<String, McodeDetail> mm = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-XD"), new TR<Map<String, McodeDetail>>(){});
		for(TeaexamSiteSetting set : sets) {
			String[] sids = StringUtils.split(set.getSubjectInfoId(), ",");
			ArrayUtils.removeElement(sids, "");
			StringBuilder sn = new StringBuilder();
			for(String sid : sids) {
				if(StringUtils.isEmpty(sid)){
					continue;
				}
				TeaexamSubject sub = subNames.get(sid);
				if(sub != null) {
					sn.append(sub.getSubjectName()+"("+mm.get(sub.getSection()+"").getMcodeContent()+"),");
				}
			}
			if(sn.length() > 0) {
				sn.setLength(sn.length() - 1);
			}
			set.setSubName(sn.toString());
		}
		return sets;
	}
	
	public ResultDto saveSet(List<TeaexamSiteSetting> sets, String subInfoId) {
		ResultDto dto = new ResultDto();
		if(CollectionUtils.isEmpty(sets)) {
			dto.setMsg("没有需要保存的数据！");
			return dto;
		}
		List<TeaexamSiteSetting> setList = findSettingBySubInfoId(subInfoId);
		Map<String, TeaexamSiteSetting> setMap = EntityUtils.getMap(setList, TeaexamSiteSetting::getSchoolId);
		List<TeaexamSiteSetting> dels = new ArrayList<>();
		List<TeaexamSiteSetting> saves = new ArrayList<>();
		List<TeaexamSiteSetting> ups = new ArrayList<>();
		for(TeaexamSiteSetting set : sets) {
			if(set == null || StringUtils.isEmpty(set.getSchoolId())) {
				continue;
			}
			TeaexamSiteSetting ex = setMap.get(set.getSchoolId());
			if (ex != null) {
				if(set.getRoomNum() == ex.getRoomNum()) {// 和原有安排的数量一致，无需保存
					setMap.remove(set.getSchoolId());
					continue;
				}
				else if(set.getRoomNum() < ex.getRoomNum()) {// 判断是否少于已安排的教师教室数
					ups.add(set);
					continue;
				} 
				ex.setRoomNum(set.getRoomNum());
				ex.setModifyTime(new Date());
				saves.add(ex);
				setMap.remove(set.getSchoolId());
			} else if(set.getRoomNum() == 0) {// 没有考场，不保存
				continue;
			} else {
				set.setId(UuidUtils.generateUuid());
				set.setCreationTime(new Date());
				set.setModifyTime(new Date());
				saves.add(set);
			}
		}
		if(ups.size() > 0) {
			List<TeaexamRegisterInfo> infos = teaexamRegisterInfoDao.findByStatusAndSubInfoId(TeaexamConstant.STATUS_PASS, subInfoId);
			Map<String, Integer> schNum = new HashMap<>();
			if(CollectionUtils.isNotEmpty(infos)) {
				List<String> schRms = new ArrayList<>();
				for(TeaexamRegisterInfo info : infos) {
					if(schRms.contains(info.getLocationId()+StringUtils.trimToEmpty(info.getRoomNo()))) {
						continue;
					}
					schRms.add(info.getLocationId()+StringUtils.trimToEmpty(info.getRoomNo()));
					Integer count = schNum.get(info.getLocationId());
					if(count == null) {
						count = 0;
					}
					count++;
					schNum.put(info.getLocationId(), count);
				}
			}
			List<String> schIds = new ArrayList<>();
			for(TeaexamSiteSetting set : ups) {
				Integer count = schNum.get(set.getSchoolId());
				TeaexamSiteSetting ex = setMap.get(set.getSchoolId());
				if((count == null || count == 0) && set.getRoomNum() == 0) {
					continue;
				}
				if(count == null || count < set.getRoomNum()) {
					ex.setRoomNum(set.getRoomNum());
					ex.setModifyTime(new Date());
					setMap.remove(set.getSchoolId());
					saves.add(ex);
				} else {
					schIds.add(set.getSchoolId());
				}
			}
			if(schIds.size() > 0) {
				List<String> schNames = EntityUtils.getList(Unit.dt(unitRemoteService.findListByIds(schIds.toArray(new String[0]))), Unit::getUnitName);
				if(CollectionUtils.isNotEmpty(schNames)) {
					throw new RuntimeException("当前科目在考点["+StringUtils.join(schNames, "，")+"]已安排老师，分配数不能少于已安排掉的考场数");
				}
			}
		}
		if(setMap.size() > 0) {// 没有勾选保存的，删除
			CollectionUtils.addAll(dels, setMap.values().iterator());
		}
		StringBuilder sb = new StringBuilder();
		if(dels.size() > 0) {
			sb.append("删除数据"+dels.size()+"条");
			this.deleteAll(dels.toArray(new TeaexamSiteSetting[0]));
		}
		if(saves.size() > 0) {
			if(sb.length() > 0) {
				sb.append("，");
			}
			sb.append("保存数据"+saves.size()+"条");
			this.saveAll(saves.toArray(new TeaexamSiteSetting[0]));
		}
		if(sb.length() == 0) {
			sb.append("没有需要保存的数据");
		} else {
			sb.setLength(0);
		}
		dto.setMsg(sb.toString());
		return dto;
	}

}
