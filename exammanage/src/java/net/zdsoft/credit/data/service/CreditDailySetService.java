package net.zdsoft.credit.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.credit.data.entity.CreditDailySet;

import java.util.List;

public interface CreditDailySetService extends BaseService<CreditDailySet, String> {

    List<CreditDailySet> findBySetId(String setId);

    void deleteByIds(String[] ids);

}
