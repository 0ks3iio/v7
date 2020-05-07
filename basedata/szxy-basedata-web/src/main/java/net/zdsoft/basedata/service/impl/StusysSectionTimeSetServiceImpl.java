package net.zdsoft.basedata.service.impl;

import java.util.List;

import net.zdsoft.basedata.dao.StusysSectionTimeSetDao;
import net.zdsoft.basedata.entity.StusysSectionTimeSet;
import net.zdsoft.basedata.service.StusysSectionTimeSetService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;


@Service("stusysSectionTimeSetService")
public class StusysSectionTimeSetServiceImpl extends BaseServiceImpl<StusysSectionTimeSet, String> implements StusysSectionTimeSetService {
    @Autowired
    private StusysSectionTimeSetDao stusysSectionTimeSetDao;
    
	@Override
	public List<StusysSectionTimeSet> findByAcadyearAndSemesterAndUnitId(
			String acadyear, Integer semester, String unitId,String[] section,boolean upToFind) {
		List<StusysSectionTimeSet> returnTimeSets = Lists.newLinkedList();
		List<StusysSectionTimeSet> unitTimeSets = Lists.newLinkedList();
		List<StusysSectionTimeSet> timeSets = stusysSectionTimeSetDao.findByAcadyearAndSemesterAndUnitId(acadyear, semester, unitId);
		for(StusysSectionTimeSet set:timeSets){
			if(StringUtils.isEmpty(set.getSection())){
				unitTimeSets.add(set);
			}
		}
		if(section!=null&&section.length>0){
			for(String sec:section){
				List<StusysSectionTimeSet> sectionSets = Lists.newLinkedList();
				for(StusysSectionTimeSet set:timeSets){
					if(sec.equals(set.getSection())){
						sectionSets.add(set);
					}
				}
				if(CollectionUtils.isNotEmpty(sectionSets)){
					returnTimeSets.addAll(sectionSets);
				}else{
					for(StusysSectionTimeSet set:unitTimeSets){
						StusysSectionTimeSet timeSet = new StusysSectionTimeSet();
						timeSet.setId(set.getId());
						timeSet.setAcadyear(set.getAcadyear());
						timeSet.setBeginTime(set.getBeginTime());
						timeSet.setEndTime(set.getEndTime());
						timeSet.setSection(sec);
						timeSet.setSectionNumber(set.getSectionNumber());
						timeSet.setSemester(set.getSemester());
						timeSet.setUnitId(set.getUnitId());
						timeSet.setUserId(set.getUserId());
						timeSet.setPeriod(set.getPeriod());
						timeSet.setPeriodInterval(set.getPeriodInterval());
						timeSet.setIsDeleted(0);
						sectionSets.add(timeSet);
					}
					if(upToFind)returnTimeSets.addAll(sectionSets);
				}
			}
		}else{
			returnTimeSets = unitTimeSets;
		}
		return returnTimeSets;
	}

	@Override
	protected BaseJpaRepositoryDao<StusysSectionTimeSet, String> getJpaDao() {
		return stusysSectionTimeSetDao;
	}

	@Override
	protected Class<StusysSectionTimeSet> getEntityClass() {
		return StusysSectionTimeSet.class;
	}

	@Override
	public List<StusysSectionTimeSet> findByUnitIdIn(String[] unitIds) {
		return stusysSectionTimeSetDao.findByUnitIdIn(unitIds);
	}
}
