package net.zdsoft.newgkelective.data.service.impl;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.newgkelective.data.dao.NewGKStudentRangeDao;
import net.zdsoft.newgkelective.data.dao.NewGKStudentRangeJdbcDao;
import net.zdsoft.newgkelective.data.dto.NewGKStudentRangeDto;
import net.zdsoft.newgkelective.data.entity.NewGKStudentRange;
import net.zdsoft.newgkelective.data.entity.NewGKStudentRangeEx;
import net.zdsoft.newgkelective.data.service.NewGKStudentRangeExService;
import net.zdsoft.newgkelective.data.service.NewGKStudentRangeService;

@Service("newGKStudentRangeService")
public class NewGKStudentRangeServiceImpl extends BaseServiceImpl<NewGKStudentRange, String> implements NewGKStudentRangeService {
	@Autowired
	private NewGKStudentRangeDao newGKStudentRangeDao;
	@Autowired
	private NewGKStudentRangeJdbcDao newGKStudentRangeJdbcDao;
	@Autowired
	private NewGKStudentRangeExService newGKStudentRangeExService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGKStudentRangeJdbcDao stuRangejdbcDao;
	
	@Override
	protected BaseJpaRepositoryDao<NewGKStudentRange, String> getJpaDao() {
		return newGKStudentRangeDao;
	}

	@Override
	protected Class<NewGKStudentRange> getEntityClass() {
		return NewGKStudentRange.class;
	}

	@Override
	public List<NewGKStudentRange> findByDivideId(String unitId, String divideId) {
		return newGKStudentRangeDao.findByUnitIdAndDivideId(unitId, divideId);
	}

	@Override
	public List<NewGKStudentRangeDto> findDtoListByDivideIdAndSubjectType(String unitId,String divideId, String subjectType) {
		List<NewGKStudentRangeEx> exList = newGKStudentRangeExService.findByDivideIdAndSubjectType(divideId, subjectType);
		Map<String, Integer> countMap = newGKStudentRangeJdbcDao.findCountMapByDivideIdAndSubjectType(unitId,divideId, subjectType);
		Map<String, String> courseNameMap = getCourseNameMap();
		List<NewGKStudentRangeDto> dtoList = new ArrayList<NewGKStudentRangeDto>();
		if(MapUtils.isNotEmpty(countMap)){
			NewGKStudentRangeDto dto = null;
			Map<String,NewGKStudentRangeEx> exMap = new HashMap<String, NewGKStudentRangeEx>();
			if(CollectionUtils.isNotEmpty(exList)){
				for (NewGKStudentRangeEx ex : exList) {
					String key = ex.getSubjectId()+"_"+ex.getRange();
					ex.setStuNum(countMap.get(key));
					exMap.put(key, ex);
				}
			}
			Map<String,NewGKStudentRangeDto> dtoMap = new HashMap<String, NewGKStudentRangeDto>();
			NewGKStudentRangeEx ex = null;
			for (Entry<String, Integer> entry : countMap.entrySet()) {
				String[] keys = entry.getKey().split("_");
				dto = dtoMap.get(keys[0]);
				if(dto==null){
					dto = new NewGKStudentRangeDto();
					dto.setSubjectId(keys[0]);
					dto.setSubjectName(courseNameMap.get(keys[0]));
					dto.setSubjectType(subjectType);
					dtoMap.put(keys[0], dto);
				}
				if(MapUtils.isEmpty(exMap)){
					ex = new NewGKStudentRangeEx();
					ex.setSubjectId(keys[0]);
					ex.setRange(keys[1]);
					ex.setStuNum(entry.getValue());
					dto.getExList().add(ex);
				}else{
					dto.getExList().add(exMap.get(entry.getKey()));
				}
				dto.setStuNum(dto.getStuNum()+entry.getValue());
			}
			for (Entry<String, NewGKStudentRangeDto> entry : dtoMap.entrySet()) {
				sortList(entry.getValue().getExList());
				dtoList.add(entry.getValue());
			}
		}
		return dtoList;
	}

	private void sortList(List<NewGKStudentRangeEx> exList) {
		Collections.sort(exList, new Comparator<NewGKStudentRangeEx>() {
			@Override
			public int compare(NewGKStudentRangeEx o1, NewGKStudentRangeEx o2) {
				if(o1 == null || o2 == null) {
					
					return 0;
				}
				if(o1.getSubjectId().equals(o2.getSubjectId())){
					return o1.getRange().compareTo(o2.getRange());
				}
				return o1.getSubjectId().compareTo(o2.getSubjectId());
			}
		});
	}

	private Map<String, String> getCourseNameMap() {
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73YSY(null),Course.class);
		Map<String, String> courseNameMap = EntityUtils.getMap(courseList, "id","subjectName");
		return courseNameMap;
	}
	
	@Override
	public List<NewGKStudentRange> findByDivideIdSubjectIdAndSubjectType(String divideId, String subjectId,
			String subjectType, String range) {
		List<NewGKStudentRange> list = null;
		list = newGKStudentRangeDao.findAll(new Specification<NewGKStudentRange>() {
			@Override
			public Predicate toPredicate(Root<NewGKStudentRange> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				list.add(cb.equal(root.get("divideId"), divideId));
				if(StringUtils.isNotBlank(subjectId)) {
					list.add(cb.equal(root.get("subjectId"), subjectId));
				}
				list.add(cb.equal(root.get("subjectType"), subjectType));
				
				if(StringUtils.isNotBlank(range)) {
					list.add(cb.equal(root.get("range"), range));
				}
				
				return cq.where(list.toArray(new Predicate[] {})).getRestriction();
			}
		});
		
		return list.stream().filter(e->e.getSubjectType().equals(subjectType)).collect(Collectors.toList());
	}

	@Override
	public void deleteByDivideIdSubjectIdAndRange(String unitId, String divideId, String subjectId, String subjectType, String[] range) {
		if (subjectId == null && range == null) {
			newGKStudentRangeJdbcDao.deleteByDivideIdAnd(unitId, divideId, subjectType);
		}
		if (range == null) {
			newGKStudentRangeJdbcDao.deleteByDivideIdAnd(unitId, divideId, subjectId, subjectType);
		} else {
			newGKStudentRangeJdbcDao.deleteByDivideIdAnd(unitId, divideId, subjectId, subjectType, range);
		}
	}

	@Override
	public void updateStudentRange(String unitId, String divideId, String subjectId, String subjectType,
			String[] range, List<NewGKStudentRange> stuRangeList) {
		
		deleteByDivideIdSubjectIdAndRange(unitId,divideId, subjectId, subjectType, range);
		stuRangejdbcDao.insertBatch(stuRangeList);
	}

	@Override
	public List<NewGKStudentRangeEx> findExListByDivideIdAndSubjectType(String divideId, String subjectType) {
		List<NewGKStudentRangeEx> exList = new ArrayList<NewGKStudentRangeEx>();
			Map<String, String> courseNameMap = getCourseNameMap();
			exList = newGKStudentRangeExService.findByDivideIdAndSubjectType(divideId, subjectType);
			if(CollectionUtils.isNotEmpty(exList)){
				for (NewGKStudentRangeEx ex : exList) {
					ex.setSubjectName(courseNameMap.get(ex.getSubjectId()));
				}
			}
		return exList;
	}

	@Override
	public Map<String, Integer> findStuRangeCount(String unitId, String divideId, String subjectId, String subjectType) {
		Map<String, Integer> map = new HashMap<>();
		List<Object[]> rangeInfo = newGKStudentRangeDao.findStuRangeCount(unitId, divideId, subjectId, subjectType);
		if(CollectionUtils.isEmpty(rangeInfo)) {
			return map;
		}
		for (Object[] objects : rangeInfo) {
			String range = (String)objects[0];
			Long stuCount = (Long)objects[1];
			
			map.put(range, stuCount.intValue());
		}
		
		return map;
	}

	@Override
	public Map<String, Set<String>> findSubjectRangeSet(String unitId, String divideId, String subjectType) {
		Map<String, Set<String>> map = new HashMap<>();
		List<Object[]> rangeInfo = newGKStudentRangeDao.findSubjectRanges(unitId, divideId, subjectType);
		if (CollectionUtils.isEmpty(rangeInfo)) {
			return map;
		}
		for (Object[] objects : rangeInfo) {
			String range = (String)objects[0];
			String subjectId = (String)objects[1];

			if (map.get(subjectId) == null) {
				map.put(subjectId, new TreeSet<>());
			}
			map.get(subjectId).add(range);
		}
		return map;
	}

	@Override
	public void deleteByDivideId(String unitId, String divideId) {
		stuRangejdbcDao.deleteByDivideId(unitId, divideId);
		//同时附带的ex也要全部清除
		newGKStudentRangeExService.deleteByDivideIdAndSubjectType(divideId, null);
	}

    @Override
    public void deleteByStudentIds(String... stuids) {
        newGKStudentRangeDao.deleteByStudentIdIn(stuids);
    }

    @Override
    public void deleteBySubjectIds(String... subids) {
        newGKStudentRangeDao.deleteBySubjectIdIn(subids);
    }

	@Override
	public List<NewGKStudentRange> findByDivideIdAndStudentId(String unitId, String divideId, String studentId) {
		return newGKStudentRangeDao.findByDivideIdAndStudentId(unitId,divideId,studentId);
	}

}
