package net.zdsoft.api.base.service;

import java.util.List;

import net.zdsoft.api.base.entity.eis.ApiDataSet;
import net.zdsoft.api.dataset.vo.DateSetDto;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;

public interface ApiDataSetService  extends BaseService<ApiDataSet, String> {

	List<ApiDataSet> findAllByPage(Pagination page, String dataSetName);

	
	void deleteDataSet(String id);


	void save(DateSetDto dataDateSetDto);


	ApiDataSet findByMdId(String metadataId);


	List<ApiDataSet> findByMdIdIn(String[] metadataIds);
	
	
	void deleteDataSetByMbid (String mbid);
	
	

}
