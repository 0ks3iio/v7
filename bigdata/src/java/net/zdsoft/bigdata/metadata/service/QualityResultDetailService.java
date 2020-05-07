package net.zdsoft.bigdata.metadata.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.metadata.entity.QualityResultDetail;

public interface QualityResultDetailService extends
		BaseService<QualityResultDetail, String> {

	/**
	 * 删除所有数据
	 */
	public void deleteAll();

}
