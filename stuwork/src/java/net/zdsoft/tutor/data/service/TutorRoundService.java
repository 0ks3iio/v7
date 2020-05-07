package net.zdsoft.tutor.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.tutor.data.entity.TutorRound;

/**
 * @author yangsj  2017年9月11日下午8:18:55
 */
public interface TutorRoundService extends BaseService<TutorRound, String> {

	/**
	 * @param unitId
	 * @return
	 */
	List<TutorRound> findByUnitId(String unitId);

}
