package net.zdsoft.tutor.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.tutor.data.entity.TutorParam;

/**
 * @author yangsj  2017年9月11日下午8:16:22
 */
public interface TutorParamService extends BaseService<TutorParam,String> {


	/**
	 * @param unitId
	 * @param string
	 * @return
	 */
	TutorParam findByUnitIdAndPtype(String unitId, String string);

}
