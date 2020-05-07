package net.zdsoft.syncdata.custom.tianchang.service;

public interface TcSyncService {

	void saveSchool();

	void saveTeacher();
	
	void saveClass();
	
	void saveCourse();
	
//	void saveStudent();
	
	void saveUser();

	void saveEdu();

	void saveEduTeacher();

	void saveStudent(String id);
}
