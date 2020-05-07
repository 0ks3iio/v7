package net.zdsoft.syncdata.custom.longyou.service;

public interface LYdySyncService {

    public void saveUnit(String interfaceName, String apiCode, String mark) throws Exception;

    public void saveClass(String interfaceName, String apiCode, String mark, String unitId) throws Exception;

    public void saveGrade(String interfaceName, String apiCode, String mark) throws Exception;

    public void saveTeacher(String interfaceName, String apiCode, String mark) throws Exception;

    public void saveStudent(String interfaceName, String apiCode, String mark, String unitId) throws Exception;

    public void saveSubSchool(String interfaceName, String apiCode) throws Exception;
    
    public void saveDept(String interfaceName, String apiCode, String mark) throws Exception;

    public void saveFamily(String interfaceName, String apiCode) throws Exception;
    
    public void saveSemester(String interfaceName, String apiCode,String mark) throws Exception;

	public void saveTeacherInfo(String interfaceName, String apiCode, String mark) throws Exception;

	public void saveSchoolInfo(String interfaceName, String apiCode, String mark,Integer type) throws Exception;

	public void saveStudentInfo(String string, String string2, String string3) throws Exception;

	public void saveEduUnit(String interfaceName, String apiCode, String mark) throws Exception;

	public void saveCourse(String interfaceName, String apiCode, String mark) throws Exception;

	public void doUpdateTeacher(String interfaceName, String apiCode, String mark) throws Exception;

	public void saveClassTeach(String interfaceName, String apiCode, String mark) throws Exception;

	public void updateStudentTransaction(String interfaceName, String apiCode, String mark) throws Exception;

	public void updateTeacherTransaction(String interfaceName, String apiCode, String mark) throws Exception;

	/**
	 * @param string
	 * @param string2
	 * @param string3
	 */
	public void saveXQClass(String interfaceName, String apiCode, String mark) throws Exception;
}

