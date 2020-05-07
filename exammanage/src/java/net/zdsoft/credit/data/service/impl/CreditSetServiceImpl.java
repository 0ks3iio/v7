package net.zdsoft.credit.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.credit.data.dao.CreditSetDao;
import net.zdsoft.credit.data.entity.CreditDailySet;
import net.zdsoft.credit.data.entity.CreditSet;
import net.zdsoft.credit.data.service.CreditDailySetService;
import net.zdsoft.credit.data.service.CreditSetService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("creditSetService")
public class CreditSetServiceImpl extends BaseServiceImpl<CreditSet, String> implements CreditSetService {
    @Autowired
    private CreditSetDao creditSetDao;
    @Autowired
    private CreditDailySetService creditDailySetService;
	
	
	@Override
	public CreditSet findByUnitIdAndAcadyearAndSemester(String unitId, String acadyear, String semester) {
		CreditSet set = creditSetDao.findByUnitIdAndAcadyearAndSemester(unitId,acadyear,semester);
		if(set == null) {
			return set;
		}
		List<CreditDailySet> dailySets = creditDailySetService.findBySetId(set.getId());
		if(CollectionUtils.isNotEmpty(dailySets)) {
			for (CreditDailySet creditDailySet : dailySets) {
				if(StringUtils.isNotBlank(creditDailySet.getParentId())) {
					continue;
				}
				creditDailySet.setSubSetList(new ArrayList<>());
				for (CreditDailySet e : dailySets) {
					if(StringUtils.isBlank(e.getParentId())) {
						continue;
					}
					if(StringUtils.equals(creditDailySet.getId(), e.getParentId())) {
						creditDailySet.getSubSetList().add(e);
					}
				}
				set.getDailySetList().add(creditDailySet);
			}
		}
		return set;
	}
	
	
	@Override
	public CreditSet findAndInit(String unitId, String acadyear, String semester) {
		CreditSet set = findByUnitIdAndAcadyearAndSemester(unitId,acadyear,semester);
		if(set != null) {
			return set;
		}
		String ac = "";
		String se = "";
		if(StringUtils.equals(semester, "1")) {
			se = "2";
			ac = (NumberUtils.toInt(acadyear.split("-")[0]) - 1) + "-" + acadyear.split("-")[0];
		}else {
			se = "1";
			ac = acadyear;
		}
		set = findByUnitIdAndAcadyearAndSemester(unitId,ac,se);
		if(set == null) {
			return set;
		}
		//复制上学期设置数据
		set.setId(UuidUtils.generateUuid());
		set.setAcadyear(acadyear);
		set.setSemester(semester);
		creditSetDao.save(set); 
		List<CreditDailySet> list = new ArrayList<>();
		for (CreditDailySet dSet : set.getDailySetList()) {
			dSet.setId(UuidUtils.generateUuid());
			dSet.setSetId(set.getId());
			for (CreditDailySet subSet : dSet.getSubSetList()) {
				subSet.setParentId(dSet.getId());
				subSet.setId(UuidUtils.generateUuid());
				subSet.setSetId(set.getId());
				list.add(subSet);
			}
			list.add(dSet);
		}
		creditDailySetService.saveAll(list.toArray(new CreditDailySet[] {}));
		return set;
	}


    @Override
    protected BaseJpaRepositoryDao<CreditSet, String> getJpaDao() {
        return creditSetDao;
    }

    @Override
    protected Class<CreditSet> getEntityClass() {
        return CreditSet.class;
    }

}
