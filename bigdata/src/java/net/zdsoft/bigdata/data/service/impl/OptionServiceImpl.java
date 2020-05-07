package net.zdsoft.bigdata.data.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.OptionDao;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.entity.Option;
import net.zdsoft.bigdata.data.entity.OptionParam;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.OptionParamService;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;

/**
 * Created by wangdongdong on 2018/10/25 16:33.
 */
@Service
public class OptionServiceImpl extends BaseServiceImpl<Option, String> implements
		OptionService {

	@Resource
	private OptionDao optionDao;
	@Resource
	private OptionParamService optionParamService;

	@Override
	protected BaseJpaRepositoryDao<Option, String> getJpaDao() {
		return optionDao;
	}

	@Override
	protected Class<Option> getEntityClass() {
		return Option.class;
	}

	@Override
	public List<Option> findAllOption() {
		return optionDao.findAllOption();
	}
	
	@Override
	public List<Option> findAllOptionByType(String type){
		return optionDao.findAllOptionByType(type);
	}

	@Override
	public Option findByCode(String frameCode)
			throws BigDataBusinessException {
		List<Option> frameList = optionDao.findAllByCode(frameCode);
		if (frameList.size() == 0) {
			throw new BigDataBusinessException(String.format("不存在code为%s的框架!",
					frameCode));
		}
		return frameList.get(0);
	}

	@Override
	public void updateOptionStatus(String id, Integer status) {
		Option frame = findOne(id);
		frame.setStatus(status);
		optionDao.update(frame, new String[] { "status" });
		RedisUtils.del("BIGDATA_OPTION_" + frame.getCode());
	}

	@Override
	public void updateOptionMobility(String code, Integer mobility) {
		optionDao.updateMobilityByCode(code, mobility);
	}

	@Override
	public OptionDto getAllOptionParam(String code) {
		return RedisUtils.getObject("BIGDATA_OPTION_" + code,
				RedisUtils.TIME_FOREEVER, new TypeReference<OptionDto>() {
				}, new RedisInterface<OptionDto>() {
					@Override
					public OptionDto queryData() {
						List<Option> frameList = optionDao
								.findAllByCode(code);
						List<OptionParam> paramList = optionParamService
								.findByOptionCode(code);
						Map<String, String> paramMap = Maps.newHashMap();
						paramList.forEach(e -> paramMap.putIfAbsent(e
								.getParamKey(),
								e.getParamValue() == null ? StringUtils.EMPTY
										: e.getParamValue()));
						OptionDto frameDto = new OptionDto();
						frameDto.setCode(code);
						frameDto.setFrameParamMap(paramMap);
						frameDto.setStatus(frameList.size() > 0 ? frameList
								.get(0).getStatus() : 0);
						frameDto.setMobility(frameList.size() > 0 ? frameList
								.get(0).getMobility() : 0);
						return frameDto;
					}
				});
	}
}
