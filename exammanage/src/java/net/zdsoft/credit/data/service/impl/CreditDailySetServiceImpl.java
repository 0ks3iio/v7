package net.zdsoft.credit.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.credit.data.dao.CreditDailySetDao;
import net.zdsoft.credit.data.entity.CreditDailySet;
import net.zdsoft.credit.data.service.CreditDailySetService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("creditDailySetService")
public class CreditDailySetServiceImpl extends BaseServiceImpl<CreditDailySet, String> implements CreditDailySetService {
    @Autowired
    private CreditDailySetDao creditDailySetDao;

    @Override
    public List<CreditDailySet> findBySetId(String setId) {
        List<CreditDailySet> list = creditDailySetDao.findBySetId(setId);
        for (CreditDailySet creditDailySet : list) {
            if (StringUtils.isBlank(creditDailySet.getParentId())) {
                List<CreditDailySet> arrayList = new ArrayList<>();
                for (CreditDailySet creditDailySet1 : list) {
                    if (StringUtils.isNotBlank(creditDailySet1.getParentId())) {
                        if (creditDailySet1.getParentId().equals(creditDailySet.getId())) {
                            arrayList.add(creditDailySet1);
                        }
                    }
                }
                creditDailySet.setSubSetList(arrayList);
            }
        }
        return list;
    }

    @Override
    public void deleteByIds(String... ids) {
        creditDailySetDao.deleteByIds(ids);
    }

    @Override
    protected BaseJpaRepositoryDao<CreditDailySet, String> getJpaDao() {
        return creditDailySetDao;
    }

    @Override
    protected Class<CreditDailySet> getEntityClass() {
        return CreditDailySet.class;
    }

}
