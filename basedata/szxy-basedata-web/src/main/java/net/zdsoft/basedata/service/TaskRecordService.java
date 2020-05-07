package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.dto.TaskJobDto;
import net.zdsoft.basedata.entity.TaskRecord;
import net.zdsoft.framework.entity.Pagination;

import org.springframework.data.domain.Pageable;

public interface TaskRecordService extends BaseService<TaskRecord, String>{

	List<TaskRecord> findList(String serverType, String type, int status,Pageable pageable);

	void updateJobNoHand(int resetTime);

	String TaskJobStart(TaskJobDto taskJobDto) throws Exception;

	void deleteByIds(String... jobId);

	TaskRecord findTaskJob(String jobId);

	/**
	 * 找3天以内的
	 * @param serverType
	 * @param type
	 * @param unitId
	 * @param businessType
	 * @param page
	 * @return
	 */
	List<TaskRecord> findListByUnitId(String serverType, String type, String unitId, String businessType, Pagination page);

	/**
	 * 删除一个月前的记录
	 */
	void deleteOld();
	
	public List<TaskRecord> saveAllEntitys(TaskRecord... taskRecord);
	
}
