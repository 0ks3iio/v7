package net.zdsoft.eclasscard.data.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccAttenceGatePeriodDao;
import net.zdsoft.eclasscard.data.entity.EccAttenceGateGrade;
import net.zdsoft.eclasscard.data.entity.EccAttenceGatePeriod;
import net.zdsoft.eclasscard.data.service.EccAttenceGateGradeService;
import net.zdsoft.eclasscard.data.service.EccAttenceGatePeriodService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
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
@Service("eccAttenceGatePeriodService")
public class EccAttenceGatePeriodServiceImpl extends BaseServiceImpl<EccAttenceGatePeriod, String> implements EccAttenceGatePeriodService{

	@Autowired
	private EccAttenceGatePeriodDao eccAttenceGatePeriodDao;
	@Autowired
	private EccAttenceGateGradeService eccAttenceGateGradeService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Override
	protected BaseJpaRepositoryDao<EccAttenceGatePeriod, String> getJpaDao() {
		return eccAttenceGatePeriodDao;
	}

	@Override
	protected Class<EccAttenceGatePeriod> getEntityClass() {
		return EccAttenceGatePeriod.class;
	}

//	@Override
//	public List<EccAttenceGatePeriod> findListByWeek(String acadyear,
//			String semester, int week,int weekNums) {
//		List<EccAttenceGatePeriod> gatePeriods = eccAttenceGatePeriodDao.findListByWeek(acadyear,semester,week);
//		//TODO 查找年级数
//		Set<String> gradeCodes = Sets.newHashSet();
//		if(CollectionUtils.isEmpty(gatePeriods)){
//			gatePeriods = Lists.newArrayList();
//			for(String gradeCode:gradeCodes){
//				for(int i=0;i<weekNums;i++){
//					EccAttenceGatePeriod gatePeriod = new EccAttenceGatePeriod();
//					gatePeriod.setId(UuidUtils.generateUuid());
//					gatePeriods.add(gatePeriod);
//				}
//			}
//			saveAll(gatePeriods.toArray(new EccAttenceGatePeriod[gatePeriods.size()]));
//		}
//		return gatePeriods;
//	}

	@Override
	public void saveAndCheck(String unitId,EccAttenceGatePeriod eccAttenceGatePeriod,
			String[] gradeCodes) {
		List<Grade> grades = SUtils.dt(gradeRemoteService.findBySchoolId(unitId),new TR<List<Grade>>() {});
		Map<String, String> gradeNameMap = EntityUtils.getMap(grades, Grade::getGradeCode, Grade::getGradeName);
		String gradeNameStr = "";
		if(Objects.equals(EccConstants.ECC_CLASSIFY_1, eccAttenceGatePeriod.getClassify())){
			if(EccConstants.GATE_ATTENCE_PERIOD_TYPE1==eccAttenceGatePeriod.getType()||
					EccConstants.GATE_ATTENCE_PERIOD_TYPE2==eccAttenceGatePeriod.getType()||
					EccConstants.GATE_ATTENCE_PERIOD_TYPE4==eccAttenceGatePeriod.getType()){
				List<EccAttenceGateGrade> attenceGateGrades = eccAttenceGateGradeService.findByUnitIdAndTypeInCodes(unitId,EccConstants.ECC_CLASSIFY_1,eccAttenceGatePeriod.getType(), gradeCodes);
				boolean isHave = false;
				for(EccAttenceGateGrade grade:attenceGateGrades){
					if(!grade.getPeriodId().equals(eccAttenceGatePeriod.getId())){
						isHave = true;
						gradeNameStr = gradeNameMap.get(grade.getGrade());
						break;
					}
				}
				String returnStr = "休假日离校";
				if(EccConstants.GATE_ATTENCE_PERIOD_TYPE1==eccAttenceGatePeriod.getType()){
					returnStr = "放假日离校";
				}
				if(EccConstants.GATE_ATTENCE_PERIOD_TYPE4==eccAttenceGatePeriod.getType()){
					returnStr = "通校生离校";
				}
				if(isHave){
					throw new RuntimeException(gradeNameStr+"年级已设置"+returnStr+"时间，请重新选择！");
				}
			}
		}else{
			//时间不能交叉
			List<EccAttenceGateGrade> attenceGateGrades = eccAttenceGateGradeService.findByUnitIdAndClassifyAndGradeCodes(unitId, EccConstants.ECC_CLASSIFY_2, gradeCodes);
			if(StringUtils.isNotBlank(eccAttenceGatePeriod.getId())){
				//其他时间
				attenceGateGrades = attenceGateGrades.stream().filter(e->!e.getPeriodId().equals(eccAttenceGatePeriod.getId())).collect(Collectors.toList());
			}
			String returnStr = "上学";
			if(EccConstants.GATE_ATTENCE_PERIOD_TYPE2==eccAttenceGatePeriod.getType()){
				returnStr = "放学";
			}
			String beginTime=eccAttenceGatePeriod.getBeginTime();
			String endTime=eccAttenceGatePeriod.getEndTime();
			if(CollectionUtils.isNotEmpty(attenceGateGrades)) {
				for(EccAttenceGateGrade ee:attenceGateGrades) {
					//时间不能交叉
					String beginTime1=ee.getBeginTime();
					String endTime1=ee.getEndTime();
					if(StringUtils.leftPad(beginTime1, 5, "0").compareTo(StringUtils.leftPad(beginTime, 5, "0"))==0
							|| StringUtils.leftPad(endTime1, 5, "0").compareTo(StringUtils.leftPad(endTime, 5, "0"))==0) {
						//开始相等 或者结尾相等--有交叉时间
						gradeNameStr = gradeNameMap.get(ee.getGrade());
						throw new RuntimeException(gradeNameStr+"年级设置上学或者放学时间出现时间交叉！");
					}
					if(StringUtils.leftPad(beginTime1, 5, "0").compareTo(StringUtils.leftPad(beginTime, 5, "0"))>0
							&& StringUtils.leftPad(endTime1, 5, "0").compareTo(StringUtils.leftPad(beginTime, 5, "0"))<0) {
						//beginTime 在beginTime1 与endTime1 之间
						gradeNameStr = gradeNameMap.get(ee.getGrade());
						
						throw new RuntimeException(gradeNameStr+"年级设置上学或者放学时间出现时间交叉！");
					}
					if(StringUtils.leftPad(beginTime1, 5, "0").compareTo(StringUtils.leftPad(endTime, 5, "0"))>0
							&& StringUtils.leftPad(endTime1, 5, "0").compareTo(StringUtils.leftPad(endTime, 5, "0"))<0) {
						//beginTime 在beginTime1 与endTime1 之间
						gradeNameStr = gradeNameMap.get(ee.getGrade());
						
						throw new RuntimeException(gradeNameStr+"年级设置上学或者放学时间出现时间交叉！");
					}
					if(StringUtils.leftPad(beginTime1, 5, "0").compareTo(StringUtils.leftPad(beginTime, 5, "0"))<0
							&& StringUtils.leftPad(endTime1, 5, "0").compareTo(StringUtils.leftPad(endTime, 5, "0"))>0) {
						//包围
						gradeNameStr = gradeNameMap.get(ee.getGrade());
						throw new RuntimeException(gradeNameStr+"年级设置上学或者放学时间出现时间交叉！");
					}
					
					if(StringUtils.leftPad(beginTime1, 5, "0").compareTo(StringUtils.leftPad(beginTime, 5, "0"))>0
							&& StringUtils.leftPad(endTime1, 5, "0").compareTo(StringUtils.leftPad(endTime, 5, "0"))<0) {
						//包围
						gradeNameStr = gradeNameMap.get(ee.getGrade());
						throw new RuntimeException(gradeNameStr+"年级设置上学或者放学时间出现时间交叉！");
					}
				}
			}
//			List<EccAttenceGateGrade> gateGrades = attenceGateGrades.stream().filter(e->e.getType().equals(eccAttenceGatePeriod.getType())).collect(Collectors.toList());
//			for (EccAttenceGateGrade grade : gateGrades) {
//				gradeNameStr = gradeNameMap.get(grade.getGrade());
//				if(grade.getType()==eccAttenceGatePeriod.getType()){
//					throw new RuntimeException(gradeNameStr+"年级已设置"+returnStr+"时间，请重新选择！");
//				}
//			}
			
//			gateGrades = attenceGateGrades.stream().filter(e->!e.getType().equals(eccAttenceGatePeriod.getType())).collect(Collectors.toList());
//			for (EccAttenceGateGrade grade : gateGrades) {
//				gradeNameStr = gradeNameMap.get(grade.getGrade());
//				if(grade.getType()!=eccAttenceGatePeriod.getType()){
//					if(EccConstants.GATE_ATTENCE_PERIOD_TYPE1.equals(eccAttenceGatePeriod.getType())
//							&& StringUtils.leftPad(eccAttenceGatePeriod.getEndTime(), 5, "0").compareTo(StringUtils.leftPad(grade.getBeginTime(), 5, "0"))>0){
//						throw new RuntimeException(gradeNameStr+"年级的上学考勤结束时间不能晚于放学考勤开始时间，请重新选择！");
//						
//					}
//					if(EccConstants.GATE_ATTENCE_PERIOD_TYPE2.equals(eccAttenceGatePeriod.getType())
//							&& StringUtils.leftPad(eccAttenceGatePeriod.getBeginTime(), 5, "0").compareTo(StringUtils.leftPad(grade.getEndTime(), 5, "0"))<0){
//						throw new RuntimeException(gradeNameStr+"年级的放学考勤开始时间不能早于上学考勤结束时间，请重新选择！");
//					}
//				}
//			}
		}
		eccAttenceGatePeriod.setUnitId(unitId);
		if(StringUtils.isBlank(eccAttenceGatePeriod.getId())){
			eccAttenceGatePeriod.setId(UuidUtils.generateUuid());
			save(eccAttenceGatePeriod);
		}else{
			EccAttenceGatePeriod oldeccAttenceGatePeriod = findOne(eccAttenceGatePeriod.getId());
			if(oldeccAttenceGatePeriod!=null){
				oldeccAttenceGatePeriod.setBeginTime(eccAttenceGatePeriod.getBeginTime());
				oldeccAttenceGatePeriod.setEndTime(eccAttenceGatePeriod.getEndTime());
				oldeccAttenceGatePeriod.setTempDate(eccAttenceGatePeriod.getTempDate());
				save(oldeccAttenceGatePeriod);
			}
			List<EccAttenceGateGrade> ts= eccAttenceGateGradeService.findListBy("periodId", eccAttenceGatePeriod.getId());
			eccAttenceGateGradeService.deleteAll(ts.toArray(new EccAttenceGateGrade[0]));
		}
		List<EccAttenceGateGrade> gateGrads = Lists.newArrayList();
		for(String gradeCode:gradeCodes){
			EccAttenceGateGrade gateGrade = new EccAttenceGateGrade(); 
			gateGrade.setId(UuidUtils.generateUuid());
			gateGrade.setUnitId(unitId);
			gateGrade.setPeriodId(eccAttenceGatePeriod.getId());
			gateGrade.setGrade(gradeCode);
			gateGrade.setType(eccAttenceGatePeriod.getType());
			gateGrade.setBeginTime(eccAttenceGatePeriod.getBeginTime());
			gateGrade.setEndTime(eccAttenceGatePeriod.getEndTime());
			gateGrade.setTempDate(eccAttenceGatePeriod.getTempDate());
			gateGrade.setClassify(eccAttenceGatePeriod.getClassify());
			gateGrads.add(gateGrade);
		}
		eccAttenceGateGradeService.saveAll(gateGrads.toArray(new EccAttenceGateGrade[gateGrads.size()]));
		
	}

	@Override
	public List<EccAttenceGatePeriod> findList(final String unitId, String gradeCode, Integer classify,
			Pagination page) {
		List<EccAttenceGatePeriod> gatePeriods = Lists.newArrayList();
		Set<String> periodIds = Sets.newHashSet();
		if(StringUtils.isNotEmpty(gradeCode)){
			List<EccAttenceGateGrade> gateGrades = eccAttenceGateGradeService.findByUnitIdAndClassifyAndGradeCodes(unitId, classify, new String[]{gradeCode});
			for(EccAttenceGateGrade gateGrade:gateGrades){
				periodIds.add(gateGrade.getPeriodId());
			}
			if(periodIds.size()==0){
				return gatePeriods;
			}
		}
		final String[] periodIdArr = periodIds.toArray(new String[periodIds.size()]);
		Specification<EccAttenceGatePeriod> specification = new Specification<EccAttenceGatePeriod>() {

            @Override
            public Predicate toPredicate(Root<EccAttenceGatePeriod> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                if (null != periodIdArr&&periodIdArr.length > 0) {
                    In<String> in = cb.in(root.get("id").as(String.class));
                    for (int i = 0; i < periodIdArr.length; i++) {
                        in.value(periodIdArr[i]);
                    }
                    ps.add(in);
                }
                ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
                ps.add(cb.equal(root.get("classify").as(Integer.class), classify));
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.asc(root.get("type").as(String.class)));
                orderList.add(cb.asc(root.get("beginTime").as(String.class)));

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
        if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<EccAttenceGatePeriod> findAll = eccAttenceGatePeriodDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            gatePeriods = findAll.getContent();
        }
        else {
        	gatePeriods = eccAttenceGatePeriodDao.findAll(specification);
        }
        if(CollectionUtils.isEmpty(periodIds)){//全部时
        	for(EccAttenceGatePeriod gateGrade:gatePeriods){
        		periodIds.add(gateGrade.getId());
        	}
        }
		
		Map<String,List<EccAttenceGateGrade>> gateGradeMap = eccAttenceGateGradeService.findByPeriodIdMap(periodIds.toArray(new String[0]));
		List<Grade> grades = SUtils.dt(gradeRemoteService.findBySchoolId(unitId),new TR<List<Grade>>() {});
		Map<String,Grade> gateMap = Maps.newHashMap();
		for(Grade grade:grades){
			gateMap.put(grade.getGradeCode(), grade);
		}
		for(EccAttenceGatePeriod period:gatePeriods){
			if(gateGradeMap.containsKey(period.getId())){
				List<EccAttenceGateGrade> gateGrades = gateGradeMap.get(period.getId());
				if(CollectionUtils.isNotEmpty(gateGrades)){
					for(EccAttenceGateGrade grade:gateGrades){
						if(gateMap.containsKey(grade.getGrade())){
							Grade g = gateMap.get(grade.getGrade());
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
		return gatePeriods;
	}

	@Override
	public void deleteById(String id) {
		List<EccAttenceGateGrade> ts= eccAttenceGateGradeService.findListBy("periodId", id);
		eccAttenceGateGradeService.deleteAll(ts.toArray(new EccAttenceGateGrade[0]));
		this.delete(id);
	}

}
