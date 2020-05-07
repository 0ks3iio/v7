
package net.zdsoft.newgkelective.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.dto.ArrayFeaturesDto;
import net.zdsoft.newgkelective.data.entity.NewGkArrayItem;

public interface NewGkArrayItemService extends BaseService<NewGkArrayItem, String>{
	/**
	 * 判断是否已有记录
	 * @param divideId
	 * @param divideType
	 * @param arrayName
	 * @return
	 */
	List<String> findIdsByDivideIdName(String divideId, String divideType, String arrayName);

	/**
	 * 
	 * @param divideId
	 * @param divideType 可为null
	 * @return
	 */
	List<NewGkArrayItem> findByDivideId(String divideId, String[] divideType);
	List<NewGkArrayItem> findByDivideIdWithMaster(String divideId, String[] divideType);
	/**
	 * 软删
	 * @param unitId TODO
	 * @param id
	 */
	void deleteById(String unitId, String id);

	/**
	 * 组装数据newGkItemDto
	 * @param type 类型
	 * @param itemList
	 */
	void makeDtoData(String type,String gradeId,List<NewGkArrayItem> itemList);
	
	/**
	 * 根据给定的排课方案查出一周每天有几节课是可排的
	 * @param newGkArrayItem 排课方案对象
	 * @return
	 */
	Map<String,Integer> findFreeLessonByArrayItem(NewGkArrayItem newGkArrayItem);
	
	/**
	 * 保存复制的 排课特征
	 * @param copyRelationList 
	 * @param copySubjectTimes 
	 * @param copyLessonTimeExList 
	 * @param copyLessonTimeList 
	 * @param copyItem 不能为空
	 * 
	 */
	void saveCopyArrayFeature(ArrayFeaturesDto featureDto);

	
}
