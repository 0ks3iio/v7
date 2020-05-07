package net.zdsoft.newstusys.service;

import java.util.Map;

public interface StudentReportService {

	/**
     * 根据班级取班级中(学生总数)
     */
    public Map<String, Integer> stuCountByClass(String... classIds);
    
    /**
     * 根据班级取班级中(市内)
     */
    public Map<String, Integer> inCityStuCountByClass(String... classIds);
	
	/**
     * 根据班级取班级中(市外)
     */
    public Map<String, Integer> notInCityStuCountByClass(String... classIds);
    
    /**
     * 根据班级取班级中(随迁子女数总)
     */
    public Map<String, Integer> migrationStuCountByClass(String... classIds);
    
    /**
     * 根据年级取班级中(随迁子(省外)女数)
     */
    public Map<String, Integer> sYmigrationStuCountByClass(String... classIds);
    
    /**
     * 根据班级取班级中(留守儿童数)
     */
    public Map<String, Integer> stayinStuCountByClass(String... classIds);
    
    /**
     * 根据班级取班级中(随班就读数)
     */
    public Map<String, Integer> regularClassStuCountByClass(String... classIds);
    
    /**
     * 根据班级取班级中(住宿生数)
     */
    public Map<String, Integer> boardingStuCountByClass(String... classIds);
    
    /**
     * 根据班级取班级中(无户口)
     */
    public Map<String, Integer> notHkStuCountByClass(String... classIds);
    
    /**
     * 根据班级取班级中(外籍学生数)
     */
    public Map<String, Integer> normalStuCountByClass(String... classIds);
    
    /**
     * 根据班级取班级中(港澳台生数)
     */
    public Map<String, Integer> compatriotsCountByClass(String... classIds);
    
    /**
     * 根据班级取班级中(待转入学生数)
     */
    public Map<String, Integer> nowStateStuCountByClass(String... classIds);
    
    /**
     * 异动报表根据（班级）取人数
     * @return <classId+flowType, 人数>
     */
    public Map<String, Integer> ydByClassId(String acadyear, String semester, String flowtype, String... classIds);
    public Map<String, Integer> ydByClassIdsTypes(String acadyear, String semester, String[] flowTypes, String[] classIds);
    
    //==========================================================年级接口=======================================================================================
    /**
     * 根据年级取班级中(班级总数)
     */
    public Map<String, Integer> classCountByGrade(String... gradeIds);
    
    /**
     * 根据年级取班级中(学生总数)
     */
    public Map<String, Integer> stuCountByGrade(String... gradeIds);
    
    /**
     * 根据年级取班级中(诸暨户籍学生)
     */
    public Map<String, Integer> inCityCountByGrade(String... gradeIds);
    
    /**
     * 根据年级取班级中(市外学生)
     */
    public Map<String, Integer> notCityCountByGrade(String... gradeIds);
    
    /**
     * 根据年级取班级中(无户口)
     */
    public Map<String, Integer> notHkCountByGrade(String... gradeIds);
    
    /**
     * 根据年级取班级中(随迁子(总)女数)
     */
    public Map<String, Integer> migrationStuCountByGrade(String... gradeIds);
    
    /**
     * 根据年级取班级中(随迁子(省外)女数)
     */
    public Map<String, Integer> sYmigrationStuCountByGrade(String... gradeIds);
    
    /**
     * 根据年级取班级中(留守儿童数)
     */
    public Map<String, Integer> stayinStuCountByGrade(String... gradeIds);
    
    /**
     * 根据年级取班级中(随班就读数)
     */
    public Map<String, Integer> regularClassStuCountByGrade(String... gradeIds);
    
    /**
     * 根据年级取班级中(外籍学生数)
     */
    public Map<String, Integer> normalStuCountByGrade(String... gradeIds);
    
    /**
     * 根据年级取班级中(港澳台生数)
     */
    public Map<String, Integer> compatriotsCountByGrade(String... gradeIds);
    
    /**
     * 根据年级取班级中(待转入学生数)
     */
    public Map<String, Integer> nowStateStuCountByGrade(String... gradeIds);
    
    /**
     * 根据年级取班级中(住宿学生数)
     */
    public Map<String, Integer> boardingStuCountByGrade(String... gradeIds);
    
    /**
     * 异动报表根据（年级）取（复学）人数
     */
    public Map<String, Integer> ydByGradeId(String acadyear, String semester, String flowtype,String... gradeIds);
    
    // =======================================================学校接口===============================================================================================
    /**
     * 根据学校取(班级总数)
     */
    public Map<String, Integer> classCountBySchool(String... schoolIds);
    
    /**
     * 根据学校取(学生总数)
     */
    public Map<String, Integer> stuCountBySchool(String... schoolIds);
    
    /**
     * 根据学校取(诸暨户籍学生)
     */
    public Map<String, Integer> inCityCountBySchool(String... schoolIds);
    
    /**
     * 根据学校取(市外户籍)
     */
    public Map<String, Integer> notCityCountBySchool(String... schoolIds);
    
    /**
     * 根据学校取(无户口)
     */
    public Map<String, Integer> notHkCountBySchool(String... schoolIds);
       
    /**
     * 根据年级取班级中(随迁子(总)女数)
     */
    public Map<String, Integer> migrationStuCountBySchool(String... schoolIds);
    
    /**
     * 根据年级取班级中(随迁子(省外)女数)
     */
    public Map<String, Integer> sYmigrationStuCountBySchool(String... schoolIds);
    
    /**
     * 根据年级取班级中(留守儿童数)
     */
    public Map<String, Integer> stayinStuCountBySchool(String... schoolIds);
    
    /**
     * 根据年级取班级中(随班就读数)
     */
    public Map<String, Integer> regularClassStuCountBySchool(String... schoolIds);
    
    /**
     * 根据年级取班级中(外籍学生数)
     */
    public Map<String, Integer> normalStuCountBySchool(String... schoolIds);
    
    /**
     * 根据年级取班级中(港澳台生数)
     */
    public Map<String, Integer> compatriotsCountBySchool(String... schoolIds);
    
    /**
     * 根据年级取班级中(待转入学生数)
     */
    public Map<String, Integer> nowStateStuCountBySchool(String... schoolIds);
    
    /**
     * 根据年级取班级中(住宿学生数)
     */
    public Map<String, Integer> boardingStuCountBySchool(String... schoolIds);
    
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
     * 异动报表根据（小学）取（复学）人数
     */
    public Map<String, Integer> ydBySchIdPrimary(String acadyear, String semester, String flowtype, String... schoolIds);
       
    /**
     * 异动报表根据（初高）取（复学）人数
     */
    public Map<String, Integer> ydBySchIdHign(String acadyear, String semester,String flowtype,String... schoolIds);
    
}
