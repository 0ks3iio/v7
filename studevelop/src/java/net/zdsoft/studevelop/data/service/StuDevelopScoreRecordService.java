package net.zdsoft.studevelop.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StuDevelopScoreRecord;

public interface StuDevelopScoreRecordService extends BaseService<StuDevelopScoreRecord,String>{

	public void saveScore(List<StuDevelopScoreRecord> stuDevelopScoreRecordList);
	
	public List<StuDevelopScoreRecord> stuDevelopScoreRecordList(String acadyear, String semester, String studentId);
	
	public String doImport(String unitId, List<String[]> datas, String acadyear, String semester, String gradeId);
	
	public List<StuDevelopScoreRecord> findByProjectId(String acadyear, String semester, String projectId);
	
	public void deleteByStudentIds(String acadyear, String semester, String[] studentIds);
}
