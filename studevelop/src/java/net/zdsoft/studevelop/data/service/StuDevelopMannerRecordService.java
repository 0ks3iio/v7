package net.zdsoft.studevelop.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StuDevelopMannerRecord;

public interface StuDevelopMannerRecordService extends BaseService<StuDevelopMannerRecord, String>{
	
	public List<StuDevelopMannerRecord> findListByCls(String acadyear, String semester, String subjId, String[] stuIds);

	public void bacthSave(List<StuDevelopMannerRecord> mannerRecordList);
	
	public List<StuDevelopMannerRecord> findListByStu(String acadyear, String semester, String stuId, String subjectId);
}
