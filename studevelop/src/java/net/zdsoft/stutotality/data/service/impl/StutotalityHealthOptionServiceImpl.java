package net.zdsoft.stutotality.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.dao.StutotalityHealthOptionDao;
import net.zdsoft.stutotality.data.entity.StutotalityHealth;
import net.zdsoft.stutotality.data.entity.StutotalityHealthOption;
import net.zdsoft.stutotality.data.service.StutotalityHealthOptionService;
import net.zdsoft.stutotality.data.service.StutotalityHealthService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("stutotalityHealthOptionService")
public class StutotalityHealthOptionServiceImpl extends BaseServiceImpl<StutotalityHealthOption,String> implements StutotalityHealthOptionService {
	@Autowired
	private StutotalityHealthOptionDao stutotalityHealthOptionDao;


	@Override
	protected BaseJpaRepositoryDao<StutotalityHealthOption, String> getJpaDao() {
		return stutotalityHealthOptionDao;
	}

    @Autowired
    private StutotalityHealthService stutotalityHealthService;

    @Override
    protected Class<StutotalityHealthOption> getEntityClass() {
        return StutotalityHealthOption.class;
    }

    @Override
    public List<StutotalityHealthOption> findByUnitIdAndGradeCode(String unitId, String gradeCode) {
		List<StutotalityHealthOption> stutotalityHealthOptions = null;
    	if(StringUtils.isNotBlank(gradeCode)) {
			stutotalityHealthOptions = stutotalityHealthOptionDao.findByUnitIdAndGradeCode(unitId, gradeCode);
		}else {
			stutotalityHealthOptions = stutotalityHealthOptionDao.findByUnitId(unitId);
		}
        if(CollectionUtils.isNotEmpty(stutotalityHealthOptions)) {
            Set<String> healthIds = stutotalityHealthOptions.stream().map(StutotalityHealthOption::getHealthId).collect(Collectors.toSet());
            List<StutotalityHealth> healthList = stutotalityHealthService.findListByIds(healthIds.toArray(new String[0]));
            Map<String, StutotalityHealth> healthMap = healthList.stream().collect(Collectors.toMap(StutotalityHealth::getId, Function.identity()));
			List<StutotalityHealthOption> lastList =new ArrayList<>();
            for (StutotalityHealthOption stutotalityHealthOption : stutotalityHealthOptions) {
                StutotalityHealth stutotalityHealth = healthMap.get(stutotalityHealthOption.getHealthId());
                if (stutotalityHealth != null) {
                    stutotalityHealthOption.setHealthName(stutotalityHealth.getHealthName());
                    stutotalityHealthOption.setOrderNumber(stutotalityHealth.getOrderNumber());
					lastList.add(stutotalityHealthOption);
                }
            }
            if(CollectionUtils.isNotEmpty(lastList)){
				lastList.sort(new Comparator<StutotalityHealthOption>() {
					@Override
					public int compare(StutotalityHealthOption o1, StutotalityHealthOption o2) {
						return o1.getOrderNumber()-o2.getOrderNumber();
					}
				});
			}
            return lastList;
        }
        return new ArrayList<>();
    }



	@Override
	public List<StutotalityHealthOption> findByHealthId(String healthId) {
		return stutotalityHealthOptionDao.findByHealthId(healthId);
	}

	@Override
	public List<StutotalityHealthOption> findByHealthIdAndUnitId(String unitId, String healthId) {
		return stutotalityHealthOptionDao.findByUnitIdAndHealthId(unitId, healthId);
	}

	@Override
	public List<StutotalityHealthOption> findByUnitId(String unitId) {
		return stutotalityHealthOptionDao.findByUnitId(unitId);
	}
	@Override
	public List<StutotalityHealthOption> findByUnitIdWithMaster(String unitId) {
		return stutotalityHealthOptionDao.findByUnitId(unitId);
	}


	@Override
	public List<StutotalityHealthOption> findHealthItemsByIds(String[] healthIds) {
		return stutotalityHealthOptionDao.findHealthItemsByIds(healthIds);
	}

	@Override
	public void deleteByhealthId(String healthId) {
		stutotalityHealthOptionDao.deleteByhealthId(healthId);
	}

	@Override
	public void saveHealthStandard(StutotalityHealthOption stutotalityHealthOption,List<StutotalityHealthOption> healthOption) {

		Iterator<StutotalityHealthOption> optionIterator = healthOption.iterator();
		while (optionIterator.hasNext()){
			StutotalityHealthOption option = optionIterator.next();
			option.setHealthId(stutotalityHealthOption.getHealthId());
			option.setGradeCode(stutotalityHealthOption.getGradeCode());
			option.setHealthStandard(stutotalityHealthOption.getHealthStandard());
			healthOption.add(option);
		}

	}
}
