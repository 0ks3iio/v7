package net.zdsoft.scoremanage.data.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dao.ScoreConversionDao;
import net.zdsoft.scoremanage.data.entity.ScoreConversion;
import net.zdsoft.scoremanage.data.service.ScoreConversionService;

@Service("scoreConversionService")
public class ScoreConversionServiceImpl extends BaseServiceImpl<ScoreConversion, String> implements ScoreConversionService {

	@Autowired
	private ScoreConversionDao scoreConversionDao;
	
	@Override
	protected BaseJpaRepositoryDao<ScoreConversion, String> getJpaDao() {
		return scoreConversionDao;
	}

	@Override
	protected Class<ScoreConversion> getEntityClass() {
		return ScoreConversion.class;
	}

	@Override
	public List<ScoreConversion> findListByExamId(String examId,boolean isOther) {
		List<ScoreConversion> scoreConversionList = scoreConversionDao.findListByExamId(examId);
		if(CollectionUtils.isEmpty(scoreConversionList) && isOther){
			scoreConversionList = scoreConversionDao.findListByExamId(ScoreDataConstants.ZERO32);
			for (ScoreConversion scoreConversion : scoreConversionList) {
				scoreConversion.setId("");
			}
		}
		return scoreConversionList;
	}

	@Override
	public List<ScoreConversion> saveAllEntitys(ScoreConversion... scoreConversion) {
		return scoreConversionDao.saveAll(checkSave(scoreConversion));
	}

	@Override
	public void deleteAllByIds(String... id) {
		if(id!=null && id.length>0)
			scoreConversionDao.deleteAllByIds(id);
	}

	@Override
	public void saveAll(ScoreConversion[] scoreConversion,String[] examIdArray) {
		if(examIdArray!=null && examIdArray.length>0){
			scoreConversionDao.deleteByExamIdIn(examIdArray);
		}
		if(scoreConversion!=null && scoreConversion.length>0){
			saveAllEntitys(scoreConversion);
		}
	}

}
