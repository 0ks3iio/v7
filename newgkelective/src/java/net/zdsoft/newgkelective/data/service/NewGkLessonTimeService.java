package net.zdsoft.newgkelective.data.service;

import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTime;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTimeEx;

public interface NewGkLessonTimeService extends BaseService<NewGkLessonTime, String>{

	/**
	 * 
	 * @param itemId 不能为空
	 * @param objectIds
	 * @param objectType
	 * @param isMake 是否组装具体时间数据
	 * @return
	 */
	List<NewGkLessonTime> findByItemIdObjectId(String itemId,
			String[] objectIds, String[] objectType,boolean isMake);



    List<NewGkLessonTime> findByItemIdObjectIdWithMaster(String itemId, String[] objectIds, String[] objectType,boolean isMake);

	/**
	 * 
	 * @param itemId 不能为空
	 * @param objectIds
	 * @param objectType
	 * @param groupType
	 * @return
	 */
	List<NewGkLessonTime> findByItemIdObjectIdAndGroupType(String itemId,
			String[] objectIds, String[] objectTypes, String groupType);
	
	List<NewGkLessonTime> findByObjectTypeAndItem(String type,
			String[] itemIds,boolean isMake);

	/**
	 * 复制课表时间
	 * @param arrayItemId
	 * @param objIds
	 * @param insertNewGkLessonTimeList 新增的time记录
	 * @param insertNewGkLessonTimeExList
	 * @param objType
	 * @param exSourceIds 已有time记录的sourceTypeId集合
	 */
	void saveCopyLessonTime(String arrayItemId, String[] objIds,
			List<NewGkLessonTime> insertNewGkLessonTimeList,
			List<NewGkLessonTimeEx> insertNewGkLessonTimeExList, String objType, Set<String> exSourceIds);
	/**
	 * 删除相关扩展表
	 * @param unitId TODO
	 * @param arrayItemId
	 */
	void deleteByArrayItemId(String unitId, String arrayItemId);

	void deleteByArrayItemIdAndObjectType(String itemId, String objType,String[] objectIds);
	/**
	 * 只删除本表
	 * @param ids
	 */
	public void deleteByIds(String[] ids);

	void updateTeacherExisted(String unitId, String gradeId, String arrayItemId);

	/**
	 * 自动安排批次点 结果保存
	 * @param delSourceTypeIds
	 * @param lessonTimeList
	 * @param lessonTimeExList
	 * @param arrayId 判断是否是通过排课设置 进入的 课表设置
	 */
	void saveAutoBatchResult(List<String> delSourceTypeIds, List<NewGkLessonTime> lessonTimeList,
			List<NewGkLessonTimeEx> lessonTimeExList, String arrayId);


	/**
	 * 删除本表 和  ex关联表
	 * @param leeeonTimeIds
	 */
	void deleteWithExByIds(String[] leeeonTimeIds);

	/**
	 * 更新教师组 禁排时间
	 * @param unitId
	 * @param gradeId
	 * @param arrayItemId
	 */
	void updateTeacherGroupTime(String unitId, String gradeId, String arrayItemId);
}
