package net.zdsoft.syncdata.service;

import java.util.List;

/**
 * @author yangsj  2018年4月26日下午5:32:43
 * 
 * 获取源基础数据 ，开始拉取
 */
public interface WXSourceService {

	List<String> saveSchool(List<String> codeList);
	
	void saveTeacher(String code);
	
	void saveClass(String code);

    void saveStudent(String code);
    
    void saveFamily(String code);
    
    void saveAdmin(String code);
    
    void saveTeachArea(List<String> codeList);
    
    void saveTeachPlace(List<String> codeList);
    
    void saveSchoolSemester();
    
//    void saveData(Map<String, String[]> paramMap);

	/**
	 * @param codeList
	 * @param code
	 */
	void noticeResult(List<String> codeList, String syncode);
	
	
	
	
	
	
	
	
}
