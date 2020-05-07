package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkClassBatch;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;

/**
 * 
 * @author weixh
 * @since 2018年6月12日 上午10:01:12
 */
public interface NewGkClassBatchService extends BaseService<NewGkClassBatch, String> {
	public List<NewGkClassBatch> findByDivideClsIds(String... clsIds);
	public List<NewGkClassBatch> findByDivideClsIdsWithMaster(String... clsIds);
	/**
	 * 根据批次和科目获取对象
	 * @param batch
	 * @param subjectId 科目Id，可为null,此时获取批次下的所有科目的对象
	 * @param subjectId2 
	 * @return
	 */
	List<NewGkClassBatch> findbyBatchAndSubjectId(String divideId, String batch, String subjectId);

	/**
	 * 保存时间点数据
	 * @param batchs
	 * @param divideId
	 * @param deleteByCls 是否清除班级数据
	 */
	void saveBatchs(List<NewGkClassBatch> batchs, String divideId, boolean deleteByCls,List<NewGkDivideClass> updateStuClass,String unitId);
	
	void saveClsBatch(NewGkDivide divide, String divideClsId, NewGkDivideClass divideCls);
	
	void deleteByDivideId(String unitId,String divideId, String[] classIds);

    // Basedata Sync Method
    void deleteBySubjectIds(String... subIds);
    /**
     * 全手动 保存
     * @param unitId
     * @param divideId
     * @param updateStuClass
     * @param clsBatchs
     */
	void saveBatchs2(String unitId, String divideId, List<NewGkDivideClass> updateStuClass,
			List<NewGkClassBatch> clsBatchs);
	/**
	 * 保存 走班 时间点 操作
	 * @param unitId
	 * @param divideId
	 * @param clsId
	 * @param planType A选考 B学考
	 * @param delClsIds
	 * @param updateStuClass
	 * @param clsBatchs
	 */
	void updateMoveCourse(String unitId, String divideId, String clsId, String planType,
			List<String> delClsIds, List<NewGkDivideClass> updateStuClass, List<NewGkClassBatch> clsBatchs);
	
	void deleteByDivideClassIds(String divideId, String[] divideClsIds, String subjectType);
	
	public long countByDivideId(String divideId);
	
	/**
	 * 重置走班 删除 classBatch  和 对应的 教学班
	 * @param unitId
	 * @param divideId
	 * @param planType 必填
	 */
	void dealResetFloatingPlan(String unitId, String divideId, String planType);
	
	
	void saveBatchs3(String unitId, String divideId, List<NewGkDivideClass> updateStuClass,
			List<NewGkClassBatch> clsBatchs,String planType);
	
	List<NewGkClassBatch> findBySubjectTypeWithMaster(String divideId,String subjectType);
}
