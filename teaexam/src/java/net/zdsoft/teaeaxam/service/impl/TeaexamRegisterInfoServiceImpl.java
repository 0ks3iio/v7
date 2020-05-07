package net.zdsoft.teaeaxam.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.teaeaxam.constant.TeaexamConstant;
import net.zdsoft.teaeaxam.dao.TeaexamRegisterInfoDao;
import net.zdsoft.teaeaxam.entity.TeaexamInfo;
import net.zdsoft.teaeaxam.entity.TeaexamRegisterInfo;
import net.zdsoft.teaeaxam.entity.TeaexamSite;
import net.zdsoft.teaeaxam.entity.TeaexamSiteSetting;
import net.zdsoft.teaeaxam.entity.TeaexamSubject;
import net.zdsoft.teaeaxam.service.TeaexamInfoService;
import net.zdsoft.teaeaxam.service.TeaexamRegisterInfoService;
import net.zdsoft.teaeaxam.service.TeaexamSiteService;
import net.zdsoft.teaeaxam.service.TeaexamSiteSettingService;
import net.zdsoft.teaeaxam.service.TeaexamSubjectService;

@Service("teaexamRegisterInfoService")
public class TeaexamRegisterInfoServiceImpl extends BaseServiceImpl<TeaexamRegisterInfo,String> implements TeaexamRegisterInfoService{
    @Autowired
	private TeaexamRegisterInfoDao teaexamRegisterInfoDao;
    @Autowired
	private TeaexamSiteSettingService teaexamSiteSettingService;
	@Autowired
	private TeaexamSiteService teaexamSiteService;
	@Autowired
	private TeaexamSubjectService teaexamSubjectService;
	@Autowired
	private TeaexamInfoService teaexamInfoService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	private Map<String, Integer> roomNoMap;
	private boolean examClear = false;
    
	@Override
	protected BaseJpaRepositoryDao<TeaexamRegisterInfo, String> getJpaDao() {
		return teaexamRegisterInfoDao;
	}

	@Override
	protected Class<TeaexamRegisterInfo> getEntityClass() {
		return TeaexamRegisterInfo.class;
	}
	
	public void saveAutoArrange(String examId, String subInfoId) {
		TeaexamInfo exam = teaexamInfoService.findOne(examId);
		if(exam == null) {
			return;
		}
		List<TeaexamRegisterInfo> infos = this.findByStatusAndRoomNo(TeaexamConstant.STATUS_PASS + "", null, subInfoId, examId);
		if(CollectionUtils.isEmpty(infos)) {
			return;
		}
		roomNoMap = new HashMap<>();
		System.out.println("考试编排开始["+exam.getId()+"]===========");
		if(StringUtils.isEmpty(subInfoId)) {
			teaexamRegisterInfoDao.clearSetByExamId(examId);
			examClear = true;
			// 获取状态
			String status = RedisUtils.lpop(TeaexamConstant.AUTO_ARRANGE_PREFIX 
					+ TeaexamConstant.AUTO_ARRANGE_TYPE_EXAM + examId + TeaexamConstant.AUTO_ARRANGE_POSTFIX_STATUS);
			if(TeaexamConstant.AUTO_ARRANGE_STATUS_ING.equals(status)) {
				return;
			}
			// 删除提示信息缓存
			RedisUtils.del(TeaexamConstant.AUTO_ARRANGE_PREFIX
					+ TeaexamConstant.AUTO_ARRANGE_TYPE_EXAM + examId 
					+ TeaexamConstant.AUTO_ARRANGE_POSTFIX_MSG);
			// 缓存状态进行时
			RedisUtils.lpush(TeaexamConstant.AUTO_ARRANGE_PREFIX 
					+ TeaexamConstant.AUTO_ARRANGE_TYPE_EXAM + examId 
					+ TeaexamConstant.AUTO_ARRANGE_POSTFIX_STATUS, TeaexamConstant.AUTO_ARRANGE_STATUS_ING);
			List<TeaexamSubject> subs = teaexamSubjectService.findByExamIds(new String[] {examId});
			StringBuilder sb = new StringBuilder();
			for(TeaexamSubject sub : subs) {
				boolean re = subArrange(exam, sub);
				if(!re) {
					// 失败时，获取msg，后删除科目msg缓存
					String msg = RedisUtils.lpop(TeaexamConstant.AUTO_ARRANGE_PREFIX 
							+ TeaexamConstant.AUTO_ARRANGE_TYPE_SUB + sub.getId() + TeaexamConstant.AUTO_ARRANGE_POSTFIX_MSG);
					if (StringUtils.isNotEmpty(msg)) {
						sb.append(msg+"；");
						RedisUtils.del(TeaexamConstant.AUTO_ARRANGE_PREFIX + TeaexamConstant.AUTO_ARRANGE_TYPE_SUB + sub.getId() 
								+ TeaexamConstant.AUTO_ARRANGE_POSTFIX_MSG);
					}
				}
			}
			if(sb.length() > 0) {// 缓存msg
				sb.setLength(sb.length() - 1);
				RedisUtils.lpush(TeaexamConstant.AUTO_ARRANGE_PREFIX
						+ TeaexamConstant.AUTO_ARRANGE_TYPE_EXAM + examId 
						+ TeaexamConstant.AUTO_ARRANGE_POSTFIX_MSG, sb.toString());
			}
			// 删除状态缓存
			RedisUtils.del(TeaexamConstant.AUTO_ARRANGE_PREFIX 
					+ TeaexamConstant.AUTO_ARRANGE_TYPE_EXAM + examId 
					+ TeaexamConstant.AUTO_ARRANGE_POSTFIX_STATUS);
		} else {
			subArrange(exam, teaexamSubjectService.findOne(subInfoId));
		}
		roomNoMap.clear();
		System.out.println("考试编排结束["+exam.getId()+"]===========");
	}
	
	/**
	 * 单科目编排
	 * @param examId
	 * @param subInfoId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean subArrange(TeaexamInfo exam, TeaexamSubject sub) {
		if(sub == null) {
			return false;
		}
		String subInfoId = sub.getId();
		try {
			System.out.println("科目编排开始["+exam.getId()+"=="+subInfoId+"]===========");
//			clearSubCache(subInfoId, null);
			
			String status = RedisUtils.lpop(TeaexamConstant.AUTO_ARRANGE_PREFIX 
					+ TeaexamConstant.AUTO_ARRANGE_TYPE_SUB + subInfoId + TeaexamConstant.AUTO_ARRANGE_POSTFIX_STATUS);
			if(TeaexamConstant.AUTO_ARRANGE_STATUS_ING.equals(status)) {
				return true;
			}
			
			// 重置状态和msg缓存
			RedisUtils.lpush(TeaexamConstant.AUTO_ARRANGE_PREFIX 
					+ TeaexamConstant.AUTO_ARRANGE_TYPE_SUB + subInfoId 
					+ TeaexamConstant.AUTO_ARRANGE_POSTFIX_STATUS, TeaexamConstant.AUTO_ARRANGE_STATUS_ING);
			RedisUtils.del(TeaexamConstant.AUTO_ARRANGE_PREFIX + TeaexamConstant.AUTO_ARRANGE_TYPE_SUB + subInfoId 
					+ TeaexamConstant.AUTO_ARRANGE_POSTFIX_MSG);
			
			List<TeaexamRegisterInfo> infos = this.findByStatusAndRoomNo(TeaexamConstant.STATUS_PASS + "", null, subInfoId, exam.getId());
			if(CollectionUtils.isEmpty(infos)) {
				clearSubCache(subInfoId, "科目["+sub.getSubjectName()+"]下没有可安排的考生");
				return false;
			}
			List<TeaexamSiteSetting> sets = teaexamSiteSettingService.findSettingBySubInfoId(subInfoId);
			if(CollectionUtils.isEmpty(sets)) {
				clearSubCache(subInfoId, "科目["+sub.getSubjectName()+"]下没有可安排的考场");
				return false;
			}
			
			// 获取考场容纳数
			Set<String> schIds = EntityUtils.getSet(sets, TeaexamSiteSetting::getSchoolId);
			Map<String, TeaexamSite> siteMap = EntityUtils.getMap(teaexamSiteService.findBySchoolIds(schIds.toArray(new String[0])), TeaexamSite::getSchoolId);
			Iterator<TeaexamSiteSetting> sit = sets.iterator();
			while(sit.hasNext()) {
				TeaexamSiteSetting set = sit.next();
				TeaexamSite site = siteMap.get(set.getSchoolId());
				if(site == null || site.getCapacity() <= 0) {
					sit.remove();
					continue;
				}
				set.setPerNum(site.getCapacity());
			}
			if(CollectionUtils.isEmpty(sets)) {
				clearSubCache(subInfoId, "科目["+sub.getSubjectName()+"]下没有可安排的考场");
				return false;
			}
			
			if (!examClear) {
				// 清空原来的安排
				teaexamRegisterInfoDao.clearSetByExamIdSubInfoId(exam.getId(), subInfoId);
			}
			if (roomNoMap.isEmpty()) {
				String rms = teaexamRegisterInfoDao.findRoomNoByExamId(exam.getId());
				if(StringUtils.isNotEmpty(rms)) {
					roomNoMap.put(exam.getId(), NumberUtils.toInt(rms));
				}
			}
			
			Collections.shuffle(infos);
			sit = sets.iterator();
			List<TeaexamRegisterInfo> saves = new ArrayList<>();
			while(sit.hasNext()) {// 考点循环
				TeaexamSiteSetting set = sit.next();
				int roomNo = 1;// 考点考场号
				boolean hasEnd = false;
				Integer rmi = roomNoMap.get(exam.getId());// 单次考试最大考场号
				if(rmi == null) {
					rmi = 0;
				}
				while(roomNo <= set.getRoomNum()) {
					if(CollectionUtils.isEmpty(infos)) {
						hasEnd = true;
						break;
					}
					int roomNoStr = rmi + roomNo;// 本考场的考场号
					int count = set.getPerNum();
					if(count > infos.size()) {
						count = infos.size();
					}
					List<TeaexamRegisterInfo> temp = infos.subList(0, count);
					String roomStr = StringUtils.leftPad(roomNoStr + "", 3, "0");
					int i = 1;
					for(TeaexamRegisterInfo info : temp) {
						info.setLocationId(set.getSchoolId());
						info.setRoomNo(roomStr);
						info.setSeatNo(StringUtils.leftPad(i+"", 2, "0"));
						info.setModifyTime(new Date());
						saves.add(info);
						i++;
					}
					infos = ListUtils.removeAll(infos, temp);
					roomNo++;
					roomNoMap.put(exam.getId(), roomNoStr);
				}
				if(hasEnd) {
					break;
				}
			}
			if(saves.size() > 0) {
				this.saveAll(saves.toArray(new TeaexamRegisterInfo[0]));
			}
			clearSubCache(subInfoId, null);
		} catch (Exception e) {
			e.printStackTrace();
			RedisUtils.lpush(TeaexamConstant.AUTO_ARRANGE_PREFIX 
					+ TeaexamConstant.AUTO_ARRANGE_TYPE_SUB + subInfoId
					+ TeaexamConstant.AUTO_ARRANGE_POSTFIX_STATUS, TeaexamConstant.AUTO_ARRANGE_STATUS_FAIL);
			RedisUtils.lpush(TeaexamConstant.AUTO_ARRANGE_PREFIX 
					+ TeaexamConstant.AUTO_ARRANGE_TYPE_SUB + subInfoId
					+ TeaexamConstant.AUTO_ARRANGE_POSTFIX_MSG, "科目["+sub.getSubjectName()+"]安排失败");
			return false;
		}
		return true;
	} 
	
	/**
	 * 去除班级同步状态缓存，添加同步结果说明
	 * @param clsId
	 * @param msg 
	 */
	private void clearSubCache(String subId, String msg) {
		RedisUtils.del(TeaexamConstant.AUTO_ARRANGE_PREFIX + TeaexamConstant.AUTO_ARRANGE_TYPE_SUB + subId 
				+ TeaexamConstant.AUTO_ARRANGE_POSTFIX_STATUS);
		if (StringUtils.isNotEmpty(msg)) {
			RedisUtils.lpush(TeaexamConstant.AUTO_ARRANGE_PREFIX 
					+ TeaexamConstant.AUTO_ARRANGE_TYPE_SUB + subId
					+ TeaexamConstant.AUTO_ARRANGE_POSTFIX_MSG, msg);
		} else {
			RedisUtils.del(TeaexamConstant.AUTO_ARRANGE_PREFIX + TeaexamConstant.AUTO_ARRANGE_TYPE_SUB + subId 
					+ TeaexamConstant.AUTO_ARRANGE_POSTFIX_MSG);
		}
		System.out.println("科目编排结束["+subId+"]===========");
	}
	
	public void saveAutoCardNo(String examId, String type) {
		List<TeaexamRegisterInfo> infos;
		if(BaseConstants.ONE_STR.equals(type)) {
			infos = this.findByStatusAndExamIdIn(TeaexamConstant.STATUS_PASS, new String[] {examId});
		} else {
			infos = teaexamRegisterInfoDao.findByExamIdWithNoCard(examId);
			Set<String> tids = EntityUtils.getSet(infos, TeaexamRegisterInfo::getTeacherId);
			infos = findBy(examId, null, null, TeaexamConstant.STATUS_PASS+"", null, tids.toArray(new String[0]), null);
		}
		if(CollectionUtils.isEmpty(infos)) {
			return;
		}
		String status = RedisUtils.lpop(TeaexamConstant.AUTO_ARRANGE_PREFIX + examId);
		if(TeaexamConstant.AUTO_ARRANGE_STATUS_ING.equals(status)) {
			return ;
		}
		RedisUtils.lpush(TeaexamConstant.AUTO_ARRANGE_PREFIX + examId, TeaexamConstant.AUTO_ARRANGE_STATUS_ING);
		Map<String, List<TeaexamRegisterInfo>> tm = EntityUtils.getListMap(infos, TeaexamRegisterInfo::getTeacherId, Function.identity());
		Set<String> schIds = EntityUtils.getSet(infos, TeaexamRegisterInfo::getSchoolId);
		List<School> schs = SUtils.dt(schoolRemoteService.findListByIds(schIds.toArray(new String[0])), new TR<List<School>>() {});
		// 学校代码
		Map<String, School> schMap = EntityUtils.getMap(schs, School::getId);
		// 科目信息
		schIds = EntityUtils.getSet(infos, TeaexamRegisterInfo::getSubjectInfoId);
		Map<String, TeaexamSubject> subMap = teaexamSubjectService.findMapByIdIn(schIds.toArray(new String[0]));
		TeaexamInfo exam = teaexamInfoService.findOne(examId);
		String examYear = DateUtils.date2String(exam.getCreationTime(), "yyMM");
		Map<String, Integer> codeMax = new HashMap<>();
		Iterator<Entry<String, List<TeaexamRegisterInfo>>> tit = tm.entrySet().iterator();
		List<TeaexamRegisterInfo> saves = new ArrayList<>();
		Map<String, String> schCodes = new HashMap<>();
		while(tit.hasNext()) {
			Entry<String, List<TeaexamRegisterInfo>> en = tit.next();
			List<TeaexamRegisterInfo> tins = en.getValue();
			int i = 0;
			String card = "";
			for(TeaexamRegisterInfo info : tins) {
				if(i == 0) {
					if(!subMap.containsKey(info.getSubjectInfoId())) {
						continue;
					}
					String schCode = null;
					if (!schCodes.containsKey(info.getSchoolId())) {
						School sch = schMap.get(info.getSchoolId());
						if (sch != null) {
							schCode = sch.getSchoolCode();
						} 
						if(StringUtils.isEmpty(schCode) || StringUtils.length(schCode) < 2) {
							schCode = StringUtils.leftPad(StringUtils.trimToEmpty(schCode), 2, "0");
						}
						schCodes.put(info.getSchoolId(), schCode);
					} else {
						schCode = schCodes.get(info.getSchoolId());
					}
					TeaexamSubject sub = subMap.get(info.getSubjectInfoId());
					// 年份2+月份2+学校代码（2位）+学段（1位）+流水号（4位）
					String prefix = examYear+StringUtils.substring(schCode, schCode.length()-2, schCode.length()) + sub.getSection();
					Integer max = codeMax.get(prefix);
					if(max == null) {
						max = 0;
					}
					max++;
					codeMax.put(prefix, max);
					card = prefix + StringUtils.leftPad(max+"", 4, "0");
					info.setCardNo(card);
					info.setModifyTime(new Date());
				} else {
					info.setCardNo(card);
					info.setModifyTime(new Date());
				}
				saves.add(info);
				i++;
			}
		}
		this.saveAll(saves.toArray(new TeaexamRegisterInfo[0]));
		RedisUtils.del(TeaexamConstant.AUTO_ARRANGE_PREFIX + examId);
	}
	
	public void saveChangeRm(TeaexamRegisterInfo info) {
		List<TeaexamRegisterInfo> saves = this.findListByIds(StringUtils.split(info.getId(), ","));
					//teaexamRegisterInfoDao.findBySubInfoIdTeaIds(info.getExamId(), info.getSubjectInfoId(), StringUtils.split(info.getTeacherId(), ","));
		if(CollectionUtils.isEmpty(saves)) {
			return;
		}
		int max = teaexamRegisterInfoDao.findCountByExamIdRoomNo(info.getExamId(), info.getRoomNo()) + 1;
		for(TeaexamRegisterInfo rs : saves) {
			rs.setLocationId(info.getLocationId());
			rs.setRoomNo(info.getRoomNo());
			rs.setSeatNo(StringUtils.leftPad(max + "", 2, "0"));
			rs.setModifyTime(new Date());
			max++;
		}
		this.saveAll(saves.toArray(new TeaexamRegisterInfo[0]));
	}
	
	public List<TeaexamSiteSetting> findRoomByExamIdSubId(String examId, String subInfoId){
		List<TeaexamSiteSetting> sets = new ArrayList<>();
		List<Object[]> obs = teaexamRegisterInfoDao.findRoomCountByExamIdSubInfoId(examId, subInfoId);
		if(CollectionUtils.isEmpty(obs)) {
			return sets;
		}
		List<String> schIds = new ArrayList<>();
		TeaexamSiteSetting set;
		for(Object[] ob : obs) {
			set = new TeaexamSiteSetting();
			set.setExamId(examId);
			set.setSubjectInfoId(subInfoId);
			set.setRoomNo((String) ob[0]);
			set.setSchoolId((String) ob[1]);
			set.setPerNum(NumberUtils.toInt(ob[2] + ""));
			if(!schIds.contains(set.getSchoolId())) {
				schIds.add(set.getSchoolId());
			}
			sets.add(set);
			set = null;
		}
		Map<String, String> nameMap = EntityUtils.getMap(Unit.dt(unitRemoteService.findListByIds(schIds.toArray(new String[0]))), 
				Unit::getId, Unit::getUnitName);
		if(MapUtils.isNotEmpty(nameMap)) {
			for(TeaexamSiteSetting st : sets) {
				st.setSchoolName(nameMap.get(st.getSchoolId()));
			}
		}
		sets = sets.stream().sorted((a,b)->{
			return NumberUtils.toInt(a.getRoomNo())-NumberUtils.toInt(b.getRoomNo());
		}).collect(Collectors.toList());
		return sets;
	}
	
	public int findCountByArrange(String examId) {
		return teaexamRegisterInfoDao.findCountByArrange(examId);
	}
	
	public int findCountByStatus(String examId, int status) {
		return teaexamRegisterInfoDao.findCountByStatus(examId, status);
	}

	@Override
	public List<TeaexamRegisterInfo> findByTeacherIdAndExamIdIn(
			String teacherId, String[] examIds) {
		return teaexamRegisterInfoDao.findByTeacherIdAndExamIdIn(teacherId, examIds);
	}

	@Override
	public List<TeaexamRegisterInfo> findByExamIdIn(String[] examIds) {
		return teaexamRegisterInfoDao.findByExamIdIn(examIds);
	}

	@Override
	public List<TeaexamRegisterInfo> findByStatusAndExamIdIn(int status,
			String[] examIds) {
		return teaexamRegisterInfoDao.findByStatusAndExamIdIn(status,examIds);
	}

	@Override
	public List<TeaexamRegisterInfo> findBy(String examId, String subId,
			String schId, String status, String state, String[] teacherIds, Pagination page) {
		Specification<TeaexamRegisterInfo> specification=new Specification<TeaexamRegisterInfo>() {
			@Override
			public Predicate toPredicate(Root<TeaexamRegisterInfo> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb.equal(root.get("examId").as(String.class), examId));
				if(StringUtils.isNotBlank(subId)){
               	   ps.add(cb.equal(root.get("subjectInfoId").as(String.class), subId));
				}
				if(StringUtils.isNotBlank(schId)){
	               ps.add(cb.equal(root.get("schoolId").as(String.class), schId));
				}
				if(StringUtils.isNotBlank(status)){
					ps.add(cb.equal(root.get("status").as(Integer.class), Integer.parseInt(status)));
				}else{
					ps.add(cb.notEqual(root.get("status").as(Integer.class), 1));
				}
				if(StringUtils.isNotBlank(state)){
					if(ArrayUtils.isNotEmpty(teacherIds)){
						ps.add(root.<String>get("teacherId").in(teacherIds));
					}else{
						ps.add(root.<String>get("teacherId").in(new String[]{"1"}));
					}
				}
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.desc(root.get("schoolId").as(String.class)));
				orderList.add(cb.desc(root.get("subjectInfoId").as(String.class)));
				orderList.add(cb.desc(root.get("modifyTime").as(Date.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
	            return cq.getRestriction();
			}
		};
		List<TeaexamRegisterInfo> list=new ArrayList<>();
		
		if(page!=null){
			list=findAll(specification, page);
		}else{
			list=findAll(specification);
		}
		return list;
	}

	@Override
	public List<TeaexamRegisterInfo> findByStatusAndRoomNo(String status,
			String roomNo, String subjectId, String examId) {
		Specification<TeaexamRegisterInfo> specification=new Specification<TeaexamRegisterInfo>() {
			@Override
			public Predicate toPredicate(Root<TeaexamRegisterInfo> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb.equal(root.get("examId").as(String.class), examId));
				if(StringUtils.isNotBlank(subjectId)){
               	   ps.add(cb.equal(root.get("subjectInfoId").as(String.class), subjectId));
				}
				if(StringUtils.isNotBlank(roomNo)){
	               ps.add(cb.equal(root.get("roomNo").as(String.class), roomNo));
				}
				if(StringUtils.isNotBlank(status)){
					ps.add(cb.equal(root.get("status").as(Integer.class), Integer.parseInt(status)));
				}
				List<Order> orderList = new ArrayList<Order>();
		        orderList.add(cb.asc(root.get("seatNo").as(String.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
	            return cq.getRestriction();
			}
		};
		List<TeaexamRegisterInfo> list=new ArrayList<>();
		list=findAll(specification);
		return list;
	}

	@Override
	public String doImport(List<String[]> datas, String subjectId, String examId) {
		Json importResultJson=new Json();
		List<String[]> errorDataList=new ArrayList<String[]>();
		int successCount  =0;
		String[] errorData=null;
		TeaexamSubject subject = teaexamSubjectService.findOne(subjectId);
		String subName = "";
		if(subject.getSection()==1){
			subName = subject.getSubjectName()+"（小学）";
		}else if(0==subject.getSection()){
			subName = subject.getSubjectName()+"（学前）";
		}else if(subject.getSection()==2){
			subName = subject.getSubjectName()+"（初中）";
		}else if(subject.getSection()==3){
			subName = subject.getSubjectName()+"（高中）";
		}
		Float fullScore = subject.getFullScore();
		List<TeaexamRegisterInfo> regList = findByStatusAndRoomNo("2", null, subjectId, examId);
		Map<String, TeaexamRegisterInfo> regMap = new HashMap<String, TeaexamRegisterInfo>();
		Set<String> teacherIdSet = new HashSet<String>();
		for(TeaexamRegisterInfo reg : regList){
			if(StringUtils.isNotBlank(reg.getRoomNo())){
				regMap.put(reg.getCardNo(), reg);
				teacherIdSet.add(reg.getTeacherId());
			}
		}
		Map<String, String> teacherMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(teacherIdSet)){
			List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(teacherIdSet.toArray(new String[0])), new TR<List<Teacher>>(){});
			for(Teacher t : teacherList){
				teacherMap.put(t.getId(), t.getTeacherName());
			}
		}
		
		for (String[] data : datas) {
			if(StringUtils.isBlank(data[0])) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="考生姓名";
				errorData[2]="";
				errorData[3]="考生姓名不能为空";
				errorDataList.add(errorData);
				continue;
			}
			if(StringUtils.isBlank(data[1])) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="准考证号";
				errorData[2]="";
				errorData[3]="准考证号不能为空";
				errorDataList.add(errorData);
				continue;
			}
			if(StringUtils.isBlank(data[2])) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="所在单位";
				errorData[2]="";
				errorData[3]="所在单位不能为空";
				errorDataList.add(errorData);
				continue;
			}
			if(StringUtils.isBlank(data[3])) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="考场编号";
				errorData[2]="";
				errorData[3]="考场编号不能为空";
				errorDataList.add(errorData);
				continue;
			}
			if(StringUtils.isBlank(data[4])) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="座位号";
				errorData[2]="";
				errorData[3]="座位号不能为空";
				errorDataList.add(errorData);
				continue;
			}
			if(StringUtils.isBlank(data[5])) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="考试科目";
				errorData[2]="";
				errorData[3]="考试科目不能为空";
				errorDataList.add(errorData);
				continue;
			}
			if(StringUtils.isBlank(data[6])) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="考试成绩";
				errorData[2]="";
				errorData[3]="考试成绩不能为空";
				errorDataList.add(errorData);
				continue;
			}
			if(null==regMap.get(data[1])){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="准考证号";
				errorData[2]=data[1];
				errorData[3]="该考试科目下不存在该准考证号";
				errorDataList.add(errorData);
				continue;
			}else{
				TeaexamRegisterInfo reg = regMap.get(data[1]);
				if(!data[3].equals(reg.getRoomNo())){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="考场编号";
					errorData[2]=data[3];
					errorData[3]="考场编号不对应";
					errorDataList.add(errorData);
					continue;
				}
				if(!data[4].equals(reg.getSeatNo())){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="座位号";
					errorData[2]=data[4];
					errorData[3]="座位号不对应";
					errorDataList.add(errorData);
					continue;
				}
				if(!data[0].equals(teacherMap.get(reg.getTeacherId()))){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="考生姓名";
					errorData[2]=data[0];
					errorData[3]="考生姓名和准考证号不对应";
					errorDataList.add(errorData);
					continue;
				}
			}
			if(!data[5].equals(subName)) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="考试科目";
				errorData[2]=data[5];
				errorData[3]="考试科目错误";
				errorDataList.add(errorData);
				continue;
			}
			String regExp = "^\\d{1,3}(\\.\\d{1,1})?$"; //n为小数位数
			Pattern p = Pattern.compile(regExp);  
			Matcher m = p.matcher(data[6]);  
			if(!m.matches()){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="考试成绩";
				errorData[2]=data[6];
				errorData[3]="考试成绩输入最多3位整数1小数";
				errorDataList.add(errorData);
				continue;
			}
			if(null!=fullScore && Float.parseFloat(data[6])>fullScore){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="考试成绩";
				errorData[2]=data[6];
				errorData[3]=data[0]+"分数超过满分值"+String.valueOf(fullScore);
				errorDataList.add(errorData);
				continue;
			}
			for(TeaexamRegisterInfo reg : regList){
				if(String.valueOf(data[3]).equals(reg.getRoomNo()) && String.valueOf(data[4]).equals(reg.getSeatNo()) && data[1].equals(reg.getCardNo())){
					reg.setScore(Float.parseFloat(data[6]));
					successCount++;
				}
			}			
		}
			
	
        if(CollectionUtils.isNotEmpty(regList)){
        	saveAll(regList.toArray(new TeaexamRegisterInfo[0]));
        }
		
		importResultJson.put("totalCount", datas.size());
		importResultJson.put("successCount", successCount);
		importResultJson.put("errorCount", errorDataList.size());
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
	}

	@Override
	public List<TeaexamRegisterInfo> findByTeacherId(String teacherId) {
		return teaexamRegisterInfoDao.findByTeacherId(teacherId);
	}

}
