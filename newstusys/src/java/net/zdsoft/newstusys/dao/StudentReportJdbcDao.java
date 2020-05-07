package net.zdsoft.newstusys.dao;

import java.util.Map;

public interface StudentReportJdbcDao {

	public Map<String, Integer> stuCountByClass(String... classIds);
	
	public Map<String, Integer> inCityStuCountByClass(String... classIds);
	
	public Map<String, Integer> notInCityStuCountByClass(String... classIds);
	
	public Map<String, Integer> migrationStuCountByClass(String... classIds);
	
	public Map<String, Integer> sYmigrationStuCountByClass(String... classIds);
	
	public Map<String, Integer> stayinStuCountByClass(String... classIds);
	
	public Map<String, Integer> regularClassStuCountByClass(String... classIds);
	
	public Map<String, Integer> boardingStuCountByClass(String... classIds);
	
	public Map<String, Integer> notHkStuCountByClass(String... classIds);

	public Map<String, Integer> normalStuCountByClass(String... classIds);
	
	public Map<String, Integer> classCountByGrade(String... gradeIds);
	
	public Map<String, Integer> stuCountByGrade(String... gradeIds);
	
	public Map<String, Integer> inCityCountByGrade(String... gradeIds);
	
	public Map<String, Integer> notCityCountByGrade(String... gradeIds);
	
	public Map<String, Integer> notHkCountByGrade(String... gradeIds);
	
	public Map<String, Integer> migrationStuCountByGrade(String... gradeIds);
	
	public Map<String, Integer> sYmigrationStuCountByGrade(String... gradeIds);
	
	public Map<String, Integer> stayinStuCountByGrade(String... gradeIds);
	
	public Map<String, Integer> regularClassStuCountByGrade(String... gradeIds);
	
	public Map<String, Integer> normalStuCountByGrade(String... gradeIds);
	
	public Map<String, Integer> compatriotsCountByGrade(String... gradeIds);
	
	public Map<String, Integer> nowStateStuCountByGrade(String... gradeIds);
	
	public Map<String, Integer> boardingStuCountByGrade(String... gradeIds);
	
	public Map<String, Integer> classCountBySchool(String... schoolIds);
	
	public Map<String, Integer> stuCountBySchool(String... schoolIds);
	
	public Map<String, Integer> inCityCountBySchool(String... schoolIds);
	
	public Map<String, Integer> notCityCountBySchool(String... schoolIds);
	
	public Map<String, Integer> notHkCountBySchool(String... schoolIds);
		
	public Map<String, Integer> migrationStuCountBySchool(String... schoolIds);

	public Map<String, Integer> sYmigrationStuCountBySchool(String... schoolIds);
	
	public Map<String, Integer> stayinStuCountBySchool(String... schoolIds);

	public Map<String, Integer> regularClassStuCountBySchool(String... schoolIds);
	
	public Map<String, Integer> normalStuCountBySchool(String... schoolIds);
	
	public Map<String, Integer> compatriotsCountBySchool(String... schoolIds);
	
	public Map<String, Integer> nowStateStuCountBySchool(String... schoolIds);
	
	public Map<String, Integer> boardingStuCountBySchool(String... schoolIds);
	
	public Map<String, Integer> compatriotsCountByClass(String... classIds);

	public Map<String, Integer> nowStateStuCountByClass(String... classIds);
	
	 /**
     * 根据(小学)学校取(班级总数)
     */
    public Map<String, Integer> classCountBySchoolPrimary(String... schoolIds);
    
    /**
     * 根据(小学)学校取(学生总数)
     */
    public Map<String, Integer> stuCountBySchoolPrimary(String... schoolIds);
    
    /**
     * 根据(小学)学校取(诸暨户籍学生)
     */
    public Map<String, Integer> inCityCountBySchoolPrimary(String... schoolIds);
    
    /**
     * 根据(小学)学校取(市外户籍)
     */
    public Map<String, Integer> notCityCountBySchoolPrimary(String... schoolIds);
    
    /**
     * 根据(小学)学校取(无户口)
     */
    public Map<String, Integer> notHkCountBySchoolPrimary(String... schoolIds);
       
    /**
     * 根据(小学)年级取班级中(随迁子(总)女数)
     */
    public Map<String, Integer> migrationStuCountBySchoolPrimary(String... schoolIds);
    
    /**
     * 根据(小学)年级取班级中(随迁子(省外)女数)
     */
    public Map<String, Integer> sYmigrationStuCountBySchoolPrimary(String... schoolIds);
    
    /**
     * 根据(小学)年级取班级中(留守儿童数)
     */
    public Map<String, Integer> stayinStuCountBySchoolPrimary(String... schoolIds);
    
    /**
     * 根据(小学)取班级中(随班就读数)
     */
    public Map<String, Integer> regularClassStuCountBySchoolPrimary(String... schoolIds);
    
    /**
     * 根据(小学)取班级中(外籍学生数)
     */
    public Map<String, Integer> normalStuCountBySchoolPrimary(String... schoolIds);
    
    /**
     * 根据(小学)取班级中(港澳台生数)
     */
    public Map<String, Integer> compatriotsCountBySchoolPrimary(String... schoolIds);
    
    /**
     * 根据(小学)取班级中(待转入学生数)
     */
    public Map<String, Integer> nowStateStuCountBySchoolPrimary(String... schoolIds);
    
    /**
     * 根据(小学)取班级中(住宿学生数)
     */
    public Map<String, Integer> boardingStuCountBySchoolPrimary(String... schoolIds);
    
    /**
     * 根据(高中)年级取班级中(班级总数)
     */
    public Map<String, Integer> classCountByGradeHigh(String... gradeIds);
    
    /**
     * 根据(高中)年级取班级中(学生总数)
     */
    public Map<String, Integer> stuCountByGradeHigh(String... gradeIds);
    
    /**
     * 根据(高中)年级取班级中(诸暨户籍学生)
     */
    public Map<String, Integer> inCityCountByGradeHigh(String... gradeIds);
    
    /**
     * 根据(高中)年级取班级中(市外学生)
     */
    public Map<String, Integer> notCityCountByGradeHigh(String... gradeIds);
    
    /**
     * 根据(高中)年级取班级中(无户口)
     */
    public Map<String, Integer> notHkCountByGradeHigh(String... gradeIds);
    
    /**
     * 根据(高中)年级取班级中(随迁子(总)女数)
     */
    public Map<String, Integer> migrationStuCountByGradeHigh(String... gradeIds);
    
    /**
     * 根据(高中)年级取班级中(随迁子(省外)女数)
     */
    public Map<String, Integer> sYmigrationStuCountByGradeHigh(String... gradeIds);
    
    /**
     * 根据(高中)年级取班级中(留守儿童数)
     */
    public Map<String, Integer> stayinStuCountByGradeHigh(String... gradeIds);
    
    /**
     * 根据(高中)年级取班级中(随班就读数)
     */
    public Map<String, Integer> regularClassStuCountByGradeHigh(String... gradeIds);
    
    /**
     * 根据(高中)年级取班级中(外籍学生数)
     */
    public Map<String, Integer> normalStuCountByGradeHigh(String... gradeIds);
    
    /**
     * 根据(高中)年级取班级中(港澳台生数)
     */
    public Map<String, Integer> compatriotsCountByGradeHigh(String... gradeIds);
    
    /**
     * 根据(高中)年级取班级中(待转入学生数)
     */
    public Map<String, Integer> nowStateStuCountByGradeHigh(String... gradeIds);
    
    /**
     * 根据(高中)年级取班级中(住宿学生数)
     */
    public Map<String, Integer> boardingStuCountByGradeHigh(String... gradeIds);
    
    /**
     * 异动报表根据（班级）取人数
     */
    public Map<String, Integer> ydByClassId(String acadyear, String semester, String flowtype, String... classIds);
    
    /**
     * 异动报表根据（班级）取人数
     */
    public Map<String, Integer> ydByClassIdsTypes(String acadyear, String semester, String[] flowTypes, String[] classIds);
       
    
    //---------------------------------------------------------------------------------------
    /**
     * 异动报表根据（年级）取（复学）人数
     */
    public Map<String, Integer> ydByGradeId(String acadyear, String semester, String flowtype,String... gradeIds);
   
    //---------------------------------------------------------------------------------------------
    
    /**
     * 异动报表根据（小学）取（复学）人数
     */
    public Map<String, Integer> ydBySchIdPrimary(String acadyear, String semester, String flowtype, String... schoolIds);
       
    //--------------------------------------------------------------------------------------------------
    
    /**
     * 异动报表根据（初高）取（复学）人数
     */
    public Map<String, Integer> ydBySchIdHign(String acadyear, String semester,String flowtype,String... schoolIds);
}
