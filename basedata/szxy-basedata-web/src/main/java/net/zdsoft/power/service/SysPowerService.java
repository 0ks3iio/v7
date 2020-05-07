package net.zdsoft.power.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.power.entity.SysPower;

/**
 * @author yangsj  2018年6月7日下午2:20:48
 */
public interface SysPowerService extends BaseService<SysPower, String> {

	/**
	 * @param source
	 * @return
	 */
	List<SysPower> findBySource(int source);

	/**
	 * @param value
	 * @param ids
	 */
	SysPower findByValueAndIdIn(String value, String[] ids);

	/**
	 * @param value
	 * @param defaultSourceValue
	 */
	SysPower findByValueAndSource(String value, int defaultSourceValue);

	/**
	 * @param source
	 * @param unitId
	 * @return
	 */
	List<SysPower> findBySourceAndUnitId(int source, String unitId);

}
