package net.zdsoft.bigdata.stat.service;

import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;

public interface BgDataQualityStatService {
	/**
	 * 数据质量统计
	 */
	public void dataQualityStat(boolean saveDetail) throws BigDataBusinessException;

}
