package net.zdsoft.syncdata.service;


/**
 * @author yangsj  2018年4月23日下午3:51:24
 * 
 * 进行数据的解析和转化
 */
public interface WXResolveService {

	public void saveUnit(String json);

    public void saveClass(String json);

    public void saveGrade(String json);

    public void saveTeacher(String json);

    public void saveStudent(String json);

    public void saveSubSchool(String json);
    
    public void saveDept(String json);

    public void saveData();
    
    public void saveFamily(String json);
    
	public void saveCourse(String json);

	public void saveClassTeach(String json);
	
	public void saveAdmin(String json);
	
	public void saveTeachPlace(String json, String param);  //场地信息
	
	public void saveSchoolSemester(String json, String schoolCode); //学年学期

	public void saveTeachArea(String data, String schoolCode); //校区信息
	

}
