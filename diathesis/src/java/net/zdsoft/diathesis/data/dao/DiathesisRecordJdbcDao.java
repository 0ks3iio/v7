package net.zdsoft.diathesis.data.dao;

import net.zdsoft.diathesis.data.entity.DiathesisRecord;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;

public interface DiathesisRecordJdbcDao {
	
	List<DiathesisRecord> findListByCondition(String unitId, String projectId, String acadyear, String semester, String classId,
			String[] classIds, String studentId, String[] status, Pagination page);

}
