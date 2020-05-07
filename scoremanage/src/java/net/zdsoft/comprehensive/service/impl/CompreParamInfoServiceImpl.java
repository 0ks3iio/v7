package net.zdsoft.comprehensive.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.comprehensive.constant.CompreStatisticsConstants;
import net.zdsoft.comprehensive.dao.CompreParamInfoDao;
import net.zdsoft.comprehensive.entity.CompreParameterInfo;
import net.zdsoft.comprehensive.service.CompreParamInfoService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("compreParamInfoService")
public class CompreParamInfoServiceImpl extends
		BaseServiceImpl<CompreParameterInfo, String> implements
		CompreParamInfoService {

	@Autowired
	private CompreParamInfoDao compreParamInfoDao;

	@Override
	protected BaseJpaRepositoryDao<CompreParameterInfo, String> getJpaDao() {
		return compreParamInfoDao;
	}

	@Override
	protected Class<CompreParameterInfo> getEntityClass() {
		return CompreParameterInfo.class;
	}

	@Override
	public List<CompreParameterInfo> findByInfoKey(String unitId,
			String... infoKey) {
		if (infoKey == null || infoKey.length <= 0) {
			return new ArrayList<CompreParameterInfo>();
		}
		return compreParamInfoDao.getCompreParamInfoList(unitId, infoKey);
	}

//	@Override
//	public List<CompreParameterInfo> getCompreParamInfoListByUnitIdAndInfoKey(
//			String unitId, String infoKey) {
//		return compreParamInfoDao.getCompreParamInfoList(unitId, new String[] {infoKey});
//	}

	
	@Override
	public void deleteAllByInfoKeys(String unitId, String... infoKey) {
		compreParamInfoDao.deleteAll(unitId, infoKey);
	}

	@Override
	public void delete(Integer mcPrefix, Integer mcSuffix, String unidId,
			String... infoKeys) {
		compreParamInfoDao.delete(mcPrefix, mcSuffix, unidId, infoKeys);
	}

	
	@Override
	public void deleteAndSaveOther(String unitId, String[] parkeys,
			List<CompreParameterInfo> compreParamInfoList) {
		// 先删除
		deleteAllByInfoKeys(unitId, parkeys);
		// 后保存
		List<CompreParameterInfo> compreParameterInfos = new ArrayList<CompreParameterInfo>();
		// 封装数据
		for (CompreParameterInfo compreParameterInfo : compreParamInfoList) {
			String[] grades = { CompreStatisticsConstants.THIRD_LOWER,
					CompreStatisticsConstants.SENIOR_ONE_LOWER,
					CompreStatisticsConstants.SENIOR_ONE_UPPER,
					CompreStatisticsConstants.SENIOR_TWO_LOWER,
					CompreStatisticsConstants.SENIOR_TWO_UPPER,
					CompreStatisticsConstants.THIRD_UPPER };
			int i = 0;
			for (Float f : compreParameterInfo.getScoreList()) {
				CompreParameterInfo compreParameterInfoSave = new CompreParameterInfo();
				compreParameterInfoSave.setId(UuidUtils.generateUuid());
				compreParameterInfoSave.setMcPrefix(compreParameterInfo
						.getMcPrefix());
				compreParameterInfoSave.setMcSuffix(compreParameterInfo
						.getMcSuffix());
				compreParameterInfoSave.setScore(f);
				compreParameterInfoSave.setInfoKey(grades[i]);
				compreParameterInfoSave.setUnitId(unitId);
				compreParameterInfos.add(compreParameterInfoSave);
				i++;
			}
		}
		compreParamInfoDao.saveAll(compreParameterInfos);
	}

	@Override
	public void deleteAndSaveEnglish(String unitId, String[] parkeys,
			List<CompreParameterInfo> compreParamInfoListSave) {

		deleteAllByInfoKeys(unitId, parkeys);
		saveXkOrEnglish(unitId,CompreStatisticsConstants.INFO_KEY_2,compreParamInfoListSave);

	}

	@Override
	public void deleteAndSaveXk(String unitId, String[] parkeys, List<CompreParameterInfo> compreParamInfoListSave) {
		deleteAllByInfoKeys(unitId, parkeys);
		saveXkOrEnglish(unitId,CompreStatisticsConstants.INFO_KEY_5,compreParamInfoListSave);
	}
	
	private void saveXkOrEnglish(String unitId, String infoKey, List<CompreParameterInfo> compreParamInfoListSave) {
		if(CollectionUtils.isEmpty(compreParamInfoListSave)) {
			return;
		}
		for (CompreParameterInfo compreParameterInfo : compreParamInfoListSave) {
			compreParameterInfo.setInfoKey(infoKey);
			compreParameterInfo.setUnitId(unitId);
			compreParameterInfo.setId(UuidUtils.generateUuid());
		}
		compreParamInfoDao.saveAll(compreParamInfoListSave);
	}
}
