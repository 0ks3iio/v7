package net.zdsoft.eclasscard.data.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.dao.EccAttenceDormPeriodDao;
import net.zdsoft.eclasscard.data.entity.EccAttenceDormGrade;
import net.zdsoft.eclasscard.data.entity.EccAttenceDormPeriod;
import net.zdsoft.eclasscard.data.service.EccAttenceDormGradeService;
import net.zdsoft.eclasscard.data.service.EccAttenceDormPeriodService;
import net.zdsoft.eclasscard.data.service.EccTaskService;
import net.zdsoft.eclasscard.data.task.DormAttanceTask;
import net.zdsoft.eclasscard.data.task.EccTask;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
@Service("eccAttenceDormPeriodService")
public class EccAttenceDormPeriodServiceImpl extends BaseServiceImpl<EccAttenceDormPeriod, String> implements EccAttenceDormPeriodService{

	@Autowired
	private EccAttenceDormPeriodDao eccAttenceDormPeriodDao;
	@Autowired
	private EccAttenceDormGradeService eccAttenceDormGradeService;
	@Autowired
    private GradeRemoteService gradeRemoteService;
	@Autowired
	private EccTaskService eccTaskService;
	@Override
	protected BaseJpaRepositoryDao<EccAttenceDormPeriod, String> getJpaDao() {
		return eccAttenceDormPeriodDao;
	}

	@Override
	protected Class<EccAttenceDormPeriod> getEntityClass() {
		return EccAttenceDormPeriod.class;
	}

	@Override
	public List<EccAttenceDormPeriod> findList(final String unitId,final String gradeCode,Pagination page) {
		List<EccAttenceDormPeriod> dormPeriods = Lists.newArrayList();
		Set<String> periodIds = Sets.newHashSet();
		if (StringUtils.isNotEmpty(gradeCode)) {
			 List<EccAttenceDormGrade> attenceDormGrades = eccAttenceDormGradeService.findListBy(new String[]{"grade","unitId"}, new String[]{gradeCode,unitId});
			 for(EccAttenceDormGrade adgs:attenceDormGrades){
				 periodIds.add(adgs.getPeriodId());
			 }
			 if(periodIds.size()<1){
				 return dormPeriods;
			 }
		 }
		final String[] periodIdArr = periodIds.toArray(new String[periodIds.size()]);
		Specification<EccAttenceDormPeriod> specification = new Specification<EccAttenceDormPeriod>() {

            @Override
            public Predicate toPredicate(Root<EccAttenceDormPeriod> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                if (null != periodIdArr&&periodIdArr.length > 0) {
                    In<String> in = cb.in(root.get("id").as(String.class));
                    for (int i = 0; i < periodIdArr.length; i++) {
                        in.value(periodIdArr[i]);
                    }
                    ps.add(in);
                }
                ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.asc(root.get("type").as(String.class)));
                orderList.add(cb.asc(root.get("beginTime").as(String.class)));

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
        if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<EccAttenceDormPeriod> findAll = eccAttenceDormPeriodDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            dormPeriods = findAll.getContent();
        }
        else {
        	dormPeriods = eccAttenceDormPeriodDao.findAll(specification);
        }
        if(CollectionUtils.isEmpty(periodIds)){//全部时
        	for(EccAttenceDormPeriod eadp:dormPeriods){
				 periodIds.add(eadp.getId());
			 }
        }
		Map<String,List<EccAttenceDormGrade>> map = eccAttenceDormGradeService.findByPeriodIdMap(periodIds.toArray(new String[0]));
		//获取单位下的年级,组装年级名
		List<Grade> grades = SUtils.dt(gradeRemoteService.findBySchoolId(unitId),new TR<List<Grade>>() {});
		Map<String,Grade> grateMap = Maps.newHashMap();
		for(Grade grade:grades){
			grateMap.put(grade.getGradeCode(), grade);
		}
		for(EccAttenceDormPeriod period:dormPeriods){
			if(map.containsKey(period.getId())){
				List<EccAttenceDormGrade> dormGrades = map.get(period.getId());
				if(CollectionUtils.isNotEmpty(dormGrades)){
					for(EccAttenceDormGrade grade:dormGrades){
						if(grateMap.containsKey(grade.getGrade())){
							Grade g = grateMap.get(grade.getGrade());
							if(g!=null){
								if(StringUtils.isBlank(period.getGradeNames())){
									period.setGradeNames(g.getGradeName());
								}else{
									period.setGradeNames(period.getGradeNames()+"、"+g.getGradeName());
								}
							}
						}
					}
				}
			}
		}
		return dormPeriods;
	}

	@Override
	public void saveAndCheck(String unitId,EccAttenceDormPeriod eccAttenceDormPeriod,String[] gradeCodes) throws ParseException {
		Date oldUpdateTime = null;
		Map<String, List<EccAttenceDormGrade>> gradeListMap = eccAttenceDormGradeService.findByGradeMap(unitId,eccAttenceDormPeriod.getType(),gradeCodes);
		for(String gradeCode:gradeCodes){
			if(gradeListMap.containsKey(gradeCode)){
				List<EccAttenceDormGrade> dormGrades = gradeListMap.get(gradeCode);
				if(CollectionUtils.isNotEmpty(dormGrades)){
					for(EccAttenceDormGrade dormGrade:dormGrades){
						if(!dormGrade.getPeriodId().equals(eccAttenceDormPeriod.getId())&&!checkTimeStr(eccAttenceDormPeriod.getBeginTime(),eccAttenceDormPeriod.getEndTime(),dormGrade.getBeginTime(),dormGrade.getEndTime())){
							throw new RuntimeException("时间段有交叉，请重新选择！");
						}
					}
				}
			}
		}
		boolean isEdit = false;
		if(StringUtils.isBlank(eccAttenceDormPeriod.getId())){
			eccAttenceDormPeriod.setId(UuidUtils.generateUuid());
			eccAttenceDormPeriod.setCreateTime(new Date());
		}else{
			isEdit = true;
			EccAttenceDormPeriod oldEccAttenceDormPeriod = findOne(eccAttenceDormPeriod.getId());
			String nowTime = DateUtils.date2String(new Date(), "HH:mm");
		    if(addTimeStr(nowTime).compareTo(addTimeStr(oldEccAttenceDormPeriod.getBeginTime()))>=0 &&
		    		addTimeStr(nowTime).compareTo(addTimeStr(oldEccAttenceDormPeriod.getEndTime()))<=0){
		    	throw new RuntimeException("该时间段，考勤任务进行中请勿修改！");
		    }
		    oldUpdateTime = oldEccAttenceDormPeriod.getUpdateTime();
		}
		eccAttenceDormPeriod.setUnitId(unitId);
		eccAttenceDormPeriod.setUpdateTime(new Date());
		save(eccAttenceDormPeriod);
		List<EccAttenceDormGrade> dormGrads = Lists.newArrayList();
		for(String gradeCode:gradeCodes){
			EccAttenceDormGrade dormGrade = new EccAttenceDormGrade(); 
			dormGrade.setId(UuidUtils.generateUuid());
			dormGrade.setUnitId(unitId);
			dormGrade.setPeriodId(eccAttenceDormPeriod.getId());
			dormGrade.setGrade(gradeCode);
			dormGrade.setType(eccAttenceDormPeriod.getType());
			dormGrade.setBeginTime(eccAttenceDormPeriod.getBeginTime());
			dormGrade.setEndTime(eccAttenceDormPeriod.getEndTime());
			dormGrads.add(dormGrade);
		}
		eccAttenceDormGradeService.deleteByUnitIdPeriodId(unitId,eccAttenceDormPeriod.getId());
		eccAttenceDormGradeService.saveAll(dormGrads.toArray(new EccAttenceDormGrade[dormGrads.size()]));
		//加入定时队列
        Calendar calendarEnd = Calendar.getInstance();
        String beginTime = eccAttenceDormPeriod.getBeginTime();
        String endTime = eccAttenceDormPeriod.getEndTime();
        String nowTime = DateUtils.date2String(new Date(), "HH:mm");
        String nowDate = DateUtils.date2String(new Date(), "yyyy-MM-dd");
    	if(addTimeStr(nowTime).compareTo(addTimeStr(beginTime))<0){
    		// 初始化日期
    		beginTime = nowDate+" "+addTimeStr(beginTime)+":00";
    		endTime = nowDate+" "+addTimeStr(endTime)+":00";
    		calendarEnd.setTime(DateUtils.string2DateTime(endTime));
    		calendarEnd.add(Calendar.MINUTE, 1);
    		endTime = DateUtils.date2StringBySecond(calendarEnd.getTime());
    		EccTask dormAttanceTaskStart = new DormAttanceTask(eccAttenceDormPeriod.getId(),false);
    		EccTask dormAttanceTaskEnd = new DormAttanceTask(eccAttenceDormPeriod.getId(),true);
    		
    		eccTaskService.addEccTaskBandE(dormAttanceTaskStart, dormAttanceTaskEnd, beginTime, endTime, eccAttenceDormPeriod,oldUpdateTime, isEdit);
    	}else if(!isEdit && addTimeStr(nowTime).compareTo(addTimeStr(beginTime))>0 && addTimeStr(nowTime).compareTo(addTimeStr(endTime))<0){
    		EccTask dormAttanceTaskStart = new DormAttanceTask(eccAttenceDormPeriod.getId(),false);
    		EccTask dormAttanceTaskEnd = new DormAttanceTask(eccAttenceDormPeriod.getId(),true);
    		beginTime = DateUtils.date2StringBySecond(DateUtils.addMinute(new Date(), 1));
    		endTime = nowDate+" "+addTimeStr(endTime)+":00";
    		calendarEnd.setTime(DateUtils.string2DateTime(endTime));
    		calendarEnd.add(Calendar.MINUTE, 1);
    		endTime = DateUtils.date2StringBySecond(calendarEnd.getTime());
    		eccTaskService.addEccTaskBandE(dormAttanceTaskStart, dormAttanceTaskEnd, beginTime, endTime, eccAttenceDormPeriod,oldUpdateTime, isEdit);
    	}
	}

	private boolean checkTimeStr(String newBegin,String newEnd,String oldBegin,String oldEnd){
		newBegin = addTimeStr(newBegin);
		newEnd = addTimeStr(newEnd);
		oldBegin = addTimeStr(oldBegin);
		oldEnd = addTimeStr(oldEnd);
		if(newBegin.compareTo(oldBegin)<=0&&oldBegin.compareTo(newEnd)<=0){
			return false;
		}
		if(newBegin.compareTo(oldEnd)<=0&&oldEnd.compareTo(newEnd)<=0){
			return false;
		}
		if(newBegin.compareTo(oldBegin)>=0&&oldEnd.compareTo(newEnd)>=0){
			return false;
		}
		return true;
	}
	private String addTimeStr(String timeStr){
		if(StringUtils.isNotBlank(timeStr)){
			if(timeStr.length()==4){
				timeStr="0"+timeStr;
			}
		}
		return timeStr;
	}
	@Override
	public List<EccAttenceDormPeriod> findByIdsOrderBy(String[] ids){
		List<EccAttenceDormPeriod> periodList=findListByIds(ids);
		if(CollectionUtils.isNotEmpty(periodList)){
			for(EccAttenceDormPeriod period:periodList){
				period.setBeginTime(addTimeStr(period.getBeginTime()));
				period.setEndTime(addTimeStr(period.getEndTime()));
			}
			Collections.sort(periodList,new Comparator<EccAttenceDormPeriod>(){
				@Override
				public int compare(EccAttenceDormPeriod o1,
						EccAttenceDormPeriod o2) {
					return o1.getBeginTime().compareTo(o2.getBeginTime());
				}
			});
		}
		return periodList;
	}
	@Override
	public void deleteDormPeriod(String id) {
		EccAttenceDormPeriod oldEccAttenceDormPeriod = findOne(id);
		String nowTime = DateUtils.date2String(new Date(), "HH:mm");
	    if(addTimeStr(nowTime).compareTo(addTimeStr(oldEccAttenceDormPeriod.getBeginTime()))>=0 &&
	    		addTimeStr(nowTime).compareTo(addTimeStr(oldEccAttenceDormPeriod.getEndTime()))<=0){
	    	throw new RuntimeException("该时间段，考勤任务进行中请勿删除！");
	    }
		List<EccAttenceDormGrade> dormGrades = eccAttenceDormGradeService.findListBy("periodId", id);
		delete(id);
		eccAttenceDormGradeService.deleteAll(dormGrades.toArray(new EccAttenceDormGrade[0]));
		eccTaskService.deleteEccTaskBandE(oldEccAttenceDormPeriod, oldEccAttenceDormPeriod.getUpdateTime());
	}

	@Override
	public List<EccAttenceDormPeriod> findInNowTime(String unitId) {
		List<EccAttenceDormPeriod> returnDormPeriods = Lists.newArrayList();
		List<EccAttenceDormPeriod> dormPeriods = findListBy("unitId", unitId);
		String nowMinuteStr = DateUtils.date2String(new Date(), "HH:mm");
		for(EccAttenceDormPeriod period:dormPeriods){
			if(nowMinuteStr.compareTo(addTimeStr(period.getBeginTime()))>=0&&nowMinuteStr.compareTo(addTimeStr(period.getEndTime()))<=0){
				returnDormPeriods.add(period);
			}
		}
		return returnDormPeriods;
	}
}
