package net.zdsoft.syncdata.custom.yingshuo.service;

public interface YingShuoService {
	
	void saveEdu();

	void saveSchool();
	
	void saveDept();
	
	void saveClass();
	
	void saveCourse();
	
	void saveSchoolSemester(); //保存学校学年学期

	void saveTeacher(String unitId);

	void saveStudent(String unitId);

	void saveBuilding(); //保存楼层
	
	void savePlace();  //保存场地
}
