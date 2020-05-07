package net.zdsoft.scoremanage.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.scoremanage.data.entity.ScoreConversion;

public interface ScoreConversionService extends BaseService<ScoreConversion, String>{
	
	/**
	 * 按分数降序
	 * @param examId
	 * @param isOther 如果找不到数据是否获取examId为32个0的数据
	 * @return
	 */
	public List<ScoreConversion> findListByExamId(String examId,boolean isOther);

	public List<ScoreConversion> saveAllEntitys(ScoreConversion... scoreConversion);

	public void deleteAllByIds(String... id);
	/**
	 * 保存某个考试下的新高考赋分设置(先清空对应数据然后插入)
	 * @param scoreConversion
	 * @param examIdArray 
	 */
	public void saveAll(ScoreConversion[] scoreConversion,String[] examIdArray);
	
}
