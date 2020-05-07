package net.zdsoft.bigdata.data.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.OptionParamDao;
import net.zdsoft.bigdata.data.entity.OptionParam;
import net.zdsoft.bigdata.data.service.OptionParamService;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Created by wangdongdong on 2018/10/25 16:33.
 */
@Service
public class OptionParamServiceImpl extends BaseServiceImpl<OptionParam, String>
		implements OptionParamService {

	@Resource
	private OptionParamDao optionParamDao;

	@Resource
	private OptionService optionService;

	@Override
	protected BaseJpaRepositoryDao<OptionParam, String> getJpaDao() {
		return optionParamDao;
	}

	@Override
	protected Class<OptionParam> getEntityClass() {
		return OptionParam.class;
	}

	@Override
	public List<OptionParam> findByOptionCode(String optionCode) {
		return optionParamDao.findAllByOptionCodeOrderByOrderId(optionCode);
	}

	@Override
	public void saveOptionParam(OptionParam optionParam) {
		if (StringUtils.isBlank(optionParam.getId())) {
			optionParam.setId(UuidUtils.generateUuid());
			optionParamDao.save(optionParam);
		}
		OptionParam oldParam = findOne(optionParam.getId());
		optionParamDao.update(optionParam, new String[] { "paramName",
				"paramKey", "paramValue", "orderId", "remark" });
		RedisUtils.del("BIGDATA_OPTION_" + oldParam.getOptionCode());
	}
}
