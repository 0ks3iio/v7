package net.zdsoft.basedata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.constant.ChartsConstants;
import net.zdsoft.basedata.dao.ChartsModelDao;
import net.zdsoft.basedata.entity.ChartsModel;
import net.zdsoft.basedata.service.ChartsModelService;

@Service("chartsModelService")
public class ChartsModelServiceImpl implements ChartsModelService {

	@Autowired
	private ChartsModelDao chartsModelDao;
	
	@Override
	public List<ChartsModel> findByModelId(Integer modelId, String modelType, String tagType) {
		if(StringUtils.isNotBlank(tagType)){
			return chartsModelDao.findByIsUsingAndModelIdAndTypeAndTagType(ChartsConstants.IS_USING_1,modelId,modelType,tagType);
		}else{
			return chartsModelDao.findByIsUsingAndModelIdAndType(ChartsConstants.IS_USING_1,modelId,modelType);
		}
	}

	@Override
	public List<ChartsModel> findAll() {
		return chartsModelDao.findByIsUsing(ChartsConstants.IS_USING_1);
	}

}
