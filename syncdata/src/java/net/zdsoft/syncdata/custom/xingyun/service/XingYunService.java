package net.zdsoft.syncdata.custom.xingyun.service;

import java.util.Map;
import java.util.Set;

public interface XingYunService {

	void saveEdu(String value);

	void saveSchool(String value);
	
	void saveClassAndStudent(String value);
	
	void saveTeacher(String value);

	void saveStudent(String value);

	void saveFamily(String value);

	void saveRoleModel(Set<String> unitIds);

	void saveRole(Map<String, Map<String,String>> teacherSerCodeMap  );
}
