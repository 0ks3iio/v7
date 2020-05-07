package net.zdsoft.gkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.gkelective.data.entity.GkBatch;
import net.zdsoft.gkelective.data.entity.GkGroupClass;
import net.zdsoft.gkelective.data.entity.GkGroupClassStu;
import net.zdsoft.gkelective.data.entity.GkRounds;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.entity.GkTeachClassEx;
import net.zdsoft.gkelective.data.entity.GkTeachClassStore;
import net.zdsoft.gkelective.data.entity.GkTeachClassStuStore;

public interface GkBatchService extends BaseService<GkBatch, String> {

	/**
     * 删除班级批次及对应的groupclass/班级、班级学生数据
     * @param RoundsId 
     */
    public void deleteByRoundsId(String roundsId);

    public List<GkBatch> findByClassIds(String roundId, String[] teachClassIds);
	/**
	 * 
	 * @param roundId
	 * @param batch
	 * @param teachClassIds 可为空
	 * @return
	 */
    public List<GkBatch> findByBatchAndInClassIds(String roundId, int batch, String[] teachClassIds);

    public List<GkBatch> saveAllEntitys(GkBatch... gkBatch);

    public void deleteAll(String[] ids);

    /**
     * 清除之前的数据(组合排班 清空所有排班记录，单科排班 只清空单科排班)
     * 
     * @param type
     */
    public void deleteByRoundsIdAndType(String roundsId, String type);

    public List<GkBatch> findByRoundsIdAndPlaceIn(String roundsId, String[] placeIds);

    /**
     *
     * @param arrangeId
     * @param roundIds
     * @param groupClassStu
     * @param groupClass  对于单科保存 这个主要用于保存2+x中行政班2个批次未启用
     * @param bath
     * @param teachClass
     * @param teachClassStu
     * @param exList
     * @param isCom 是否组合排班
     */
    public void saveBatchs(GkRounds round,GkSubjectArrange gkArrange,List<GkGroupClassStu> groupClassStu,
			List<GkGroupClass> groupClass, List<GkBatch> bath,List<GkTeachClassStore> teachClass,
			List<GkTeachClassStuStore> teachClassStu,List<GkTeachClassEx> exList,boolean isCom);
	/**
	 * 
	 * @param roundsId
	 * @param type 组合 或者 单科 可为空
	 * @return
	 */
	public List<GkBatch> findByRoundsId(String roundsId,String type);

    /**
     * 根据条件获取数据
     * @param roundsId
     * @param gkBatchType 组合 或者 单科 可为空
     * @return
     */
	public List<GkBatch> findBatchList(String roundsId, String gkBatchType);

	/**
	 * 获取组合班下批次
	 * @param roundsId TODO
	 * @param groupClassId
	 * @return
	 */
	public List<GkBatch> findBatchListByGroupClassId(String roundsId, String... groupClassId);


	/**
	 * 自动分配教师，以及保存到课程表
	 * @param round
	 * @param subjectteacher 教师
	 */
//	public String saveAllotTeacher(GkRounds round,GkSubjectArrange gkArrange,
//			Map<String, Set<String>> subjectteacher);

	/**
	 * 
	 * @param roundsId
	 * @param batch 可为null
	 * @param classType 可为null
	 * @return
	 */
	public List<GkBatch> findGkBatchList(String roundsId, Integer batch, String classType);

	/**
	 * 清除某年级下该轮次的时间段内的课程表数据
	 * @param round
	 * @param gkArrange
	 */
//	public void clearCourseSchedule(GkRounds round,GkSubjectArrange gkArrange);

	/**
	 * 解散组合班
	 * @param roundsId
	 * @param groupClassId
	 * @param schoolId TODO
	 */
	public void deleteByGroupClassId(String roundsId, String groupClassId, String schoolId);
	
//	public void saveAllAndCourseSchedule(List<GkBatch> gkBatchs, String roundsId, String unitId);
//
//	public void saveTeaAndCourseSch(List<TeachClass> clsList, String planId, String unitId);

	/**
	 * 删除某个轮次下教学班（删除批次，教学班，或者课程表）
	 * 
	 * @param roundId
	 * @param teaClsId
	 * @param flag 是否同时删除课程表
	 */
	public void deleteByTeaClsId(String schoolId,GkRounds gkRounds, String teaClsId,boolean flag);

	public List<GkBatch> findByRoundsId(String[] roundsIds);

	/**
	 * 仅删除批次表
	 * @param id
	 */
	public void deleteOnlyByRoundsId(String id);

	/**
	 * 删除批次、教学班以及学生
	 * @param batchId
	 */
	public void deleteById(String batchId);

}
