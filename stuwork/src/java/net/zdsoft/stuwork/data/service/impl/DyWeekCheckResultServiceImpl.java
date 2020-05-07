package net.zdsoft.stuwork.data.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder.In;

import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.dao.DyWeekCheckResultDao;
import net.zdsoft.stuwork.data.dto.DyWeekCheckResultDto;
import net.zdsoft.stuwork.data.entity.DyCourseRecord;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItem;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItemRole;
import net.zdsoft.stuwork.data.entity.DyWeekCheckResult;
import net.zdsoft.stuwork.data.service.DyWeekCheckItemService;
import net.zdsoft.stuwork.data.service.DyWeekCheckResultService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service("dyWeekCheckResultService")
public class DyWeekCheckResultServiceImpl extends BaseServiceImpl<DyWeekCheckResult, String> implements DyWeekCheckResultService{
	@Autowired
	private DyWeekCheckResultDao dyWeekCheckResultDao;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private DyWeekCheckItemService dyWeekCheckItemService;
	
	@Override
	public void saveDto(DyWeekCheckResultDto dto) {
		List<DyWeekCheckResult> results = dto.getResult();
		if(CollectionUtils.isEmpty(results)){
			return;
		}
		Set<String> ids = EntityUtils.getSet(results, "id");
		if(ids != null && ids.size() > 0){
			dyWeekCheckResultDao.deleteByIds(ids.toArray(new String[0]));
		}
		DyWeekCheckResult[] reArr = results.toArray(new DyWeekCheckResult[0]);
		checkSave(reArr);
		saveAll(reArr);
	}
	
	@Override
	public boolean checkUseItem(String unitId, String itemId, String acadyear, String semester) {
		List<DyWeekCheckResult> results = dyWeekCheckResultDao.findByItemId(unitId,itemId,acadyear,semester);
		if(CollectionUtils.isEmpty(results)){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public List<DyWeekCheckResult> findByClassIdAndCheckDate(String unitId,
			String acadyear, String semester, String classId, Date checkDate) {
		List<DyWeekCheckResult> resultList = dyWeekCheckResultDao.findByClassIdAndCheckDate(unitId,acadyear,semester,classId, checkDate);
		return resultList;
	}
	@Override
	public List<DyWeekCheckResult> findByWeekAndInClassId(String unitId,
			String acadyear, String semester, int week, String[] classIds) {
		return dyWeekCheckResultDao.findByWeekAndInClassId(unitId,acadyear,semester,week,classIds);
	}
	
	public List<DyWeekCheckResult> findByDateAndInClassId(String unitId,
			String acadyear, String semester, Date checkDate, String[] classIds){
		Specification<DyWeekCheckResult> specification = new Specification<DyWeekCheckResult>(){
			@Override
			public Predicate toPredicate(Root<DyWeekCheckResult> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = new ArrayList<Predicate>();
				ps.add(cb.equal(root.get("schoolId").as(String.class), unitId));
				ps.add(cb.equal(root.get("acadyear").as(String.class), acadyear));
				ps.add(cb.equal(root.get("semester").as(String.class), semester));
				Date startDate = checkDate;
				Date endDate = checkDate;
				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.add(Calendar.DATE, 1);
				endDate = cal.getTime();
				ps.add(cb.between(root.get("checkDate").as(Date.class), startDate, endDate));
				In<String> in = cb.in(root.get("classId").as(String.class));
                for (int i = 0; i < classIds.length; i++) {
                    in.value(classIds[i]);
                }
                ps.add(in);
				cq.where(ps.toArray(new Predicate[ps.size()]));
				return cq.getRestriction();
			}			
		};
		return findAll(specification);
	}
	
	@Override
	public List<DyWeekCheckResultDto> findByItemList(String unitId,
			String acadyear, String semester, String classId, String dutyDate) {
		List<DyWeekCheckResultDto> resultDto = new ArrayList<DyWeekCheckResultDto>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = sdf.parse(dutyDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(unitId, acadyear, NumberUtils.toInt(semester), date), DateInfo.class);
		if(dateInfo == null){
			return resultDto;
		}
		List<DyWeekCheckItem> itemList = dyWeekCheckItemService.findBySchoolAndDay(unitId,dateInfo.getWeekday());
		if(CollectionUtils.isEmpty(itemList)){
			return resultDto;
		}
		Set<String> itemIds = EntityUtils.getSet(itemList, "id");
		List<DyWeekCheckResult> resultList = dyWeekCheckResultDao.findByCheckDateAndInItemIds(unitId,acadyear,semester,date,classId, itemIds.toArray(new String[0]));
		Map<String,DyWeekCheckResult> reMap = new HashMap<String, DyWeekCheckResult>();
		for(DyWeekCheckResult result : resultList){
			reMap.put(result.getItemId() + result.getRoleType(), result);
		}
		DyWeekCheckResultDto dto = null;
		DyWeekCheckResult result = null;
		for(DyWeekCheckItem item : itemList){
			dto = new DyWeekCheckResultDto();
			dto.setItem(item);
			List<DyWeekCheckItemRole> roles = item.getItemRoles();
			for(DyWeekCheckItemRole role : roles){
				if(reMap.containsKey(item.getId()+role.getRoleType())){
					reMap.get(item.getId()+role.getRoleType()).setRoleName(role.getRoleName());
					dto.getResult().add(reMap.get(item.getId()+role.getRoleType()));
				}else{
					result  =new DyWeekCheckResult();
					result.setAcadyear(acadyear);
					result.setSemester(semester);
					result.setWeek(dateInfo.getWeek());
					result.setDay(dateInfo.getWeekday());
					result.setRoleType(role.getRoleType());
					result.setRoleName(role.getRoleName());
					dto.getResult().add(result);
				}
			}
			Collections.sort(dto.getResult(), new Comparator<DyWeekCheckResult>(){
				@Override
				public int compare(DyWeekCheckResult o1, DyWeekCheckResult o2) {
					return o1.getRoleType().compareTo(o2.getRoleType());
				}});
			resultDto.add(dto);
		}
		return resultDto;
	}

	@Override
	protected BaseJpaRepositoryDao<DyWeekCheckResult, String> getJpaDao() {
		return dyWeekCheckResultDao;
	}

	@Override
	protected Class<DyWeekCheckResult> getEntityClass() {
		return DyWeekCheckResult.class;
	}
	
}
