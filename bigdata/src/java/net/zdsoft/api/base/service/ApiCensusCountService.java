package net.zdsoft.api.base.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.api.base.entity.eis.ApiCensusCount;
import net.zdsoft.basedata.service.BaseService;

public interface ApiCensusCountService extends BaseService<ApiCensusCount, String> {

	List<ApiCensusCount> findByKeyAndDate(String key, Date startDate, Date endDate);

	List<ApiCensusCount> findByDate(Date dayFirst, Date dayEnd);

	List<ApiCensusCount> getInterfaceCounts(String name, String resultType,Date dayFirst, Date dayEnd);

}
