package net.zdsoft.credit.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.credit.data.entity.CreditStat;
import net.zdsoft.framework.entity.LoginInfo;

import java.util.List;

public interface CreditStatService extends BaseService<CreditStat, String> {

    void sumCreditStat(String gradeId, String year, String semester, String unitId);

    List<CreditStat> findBySubDailySetIds(String[] subDailySetIds);

    List<CreditStat> findBydailySetIds(String[] dailySetIds);

    void deleteByGradeId(String gradeId, String year, String semester);

    String saveImportData(List<String[]> arrDatas, LoginInfo loginInfo);
}
