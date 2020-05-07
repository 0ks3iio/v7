package net.zdsoft.newstusys.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.newstusys.dao.StudentReportJdbcDao;
import net.zdsoft.newstusys.service.StudentReportService;
@Service("studentReportService")
public class StudentReportServiceImpl implements StudentReportService{
	@Autowired
	private StudentReportJdbcDao studentReportJdbcDao;
	
	@Override
	public Map<String, Integer> stuCountByClass(String... classIds) {
		return studentReportJdbcDao.stuCountByClass(classIds);
	}
	
	@Override
	public Map<String, Integer> inCityStuCountByClass(String... classIds){
		return studentReportJdbcDao.inCityStuCountByClass(classIds);
	}

	@Override
	public Map<String, Integer> notInCityStuCountByClass(String... classIds) {
		return studentReportJdbcDao.notInCityStuCountByClass(classIds);
	}

	@Override
	public Map<String, Integer> migrationStuCountByClass(String... classIds) {
		return studentReportJdbcDao.migrationStuCountByClass(classIds);
	}
	
	public Map<String, Integer> sYmigrationStuCountByClass(String... classIds){
		return studentReportJdbcDao.sYmigrationStuCountByClass(classIds);
	}

	@Override
	public Map<String, Integer> stayinStuCountByClass(String... classIds) {
		return studentReportJdbcDao.stayinStuCountByClass(classIds);
	}

	@Override
	public Map<String, Integer> regularClassStuCountByClass(String... classIds) {
		return studentReportJdbcDao.regularClassStuCountByClass(classIds);
	}

	@Override
	public Map<String, Integer> boardingStuCountByClass(String... classIds) {
		return studentReportJdbcDao.boardingStuCountByClass(classIds);
	}
	
	@Override
	public Map<String, Integer> notHkStuCountByClass(String... classIds){
		return studentReportJdbcDao.notHkStuCountByClass(classIds);
	}

	@Override
	public Map<String, Integer> normalStuCountByClass(String... classIds) {
		return studentReportJdbcDao.normalStuCountByClass(classIds);
	}

	@Override
	public Map<String, Integer> classCountByGrade(String... gradeIds) {
		return studentReportJdbcDao.classCountByGrade(gradeIds);
	}
	
	@Override
	public Map<String, Integer> stuCountByGrade(String... gradeIds) {
		return studentReportJdbcDao.stuCountByGrade(gradeIds);
	}

	@Override
	public Map<String, Integer> inCityCountByGrade(String... gradeIds) {
		return studentReportJdbcDao.inCityCountByGrade(gradeIds);
	}

	@Override
	public Map<String, Integer> notCityCountByGrade(String... gradeIds) {
		return studentReportJdbcDao.notCityCountByGrade(gradeIds);
	}

	@Override
	public Map<String, Integer> notHkCountByGrade(String... gradeIds) {
		return studentReportJdbcDao.notHkCountByGrade(gradeIds);
	}

	@Override
	public Map<String, Integer> migrationStuCountByGrade(String... gradeIds) {
		return studentReportJdbcDao.migrationStuCountByGrade(gradeIds);
	}

	@Override
	public Map<String, Integer> sYmigrationStuCountByGrade(String... gradeIds) {
		return studentReportJdbcDao.sYmigrationStuCountByGrade(gradeIds);
	}

	@Override
	public Map<String, Integer> stayinStuCountByGrade(String... gradeIds) {
		return studentReportJdbcDao.stayinStuCountByGrade(gradeIds);
	}

	@Override
	public Map<String, Integer> regularClassStuCountByGrade(String... gradeIds) {
		return studentReportJdbcDao.regularClassStuCountByGrade(gradeIds);
	}

	@Override
	public Map<String, Integer> normalStuCountByGrade(String... gradeIds) {
		return studentReportJdbcDao.normalStuCountByGrade(gradeIds);
	}

	@Override
	public Map<String, Integer> compatriotsCountByGrade(String... gradeIds) {
		return studentReportJdbcDao.compatriotsCountByGrade(gradeIds);
	}

	@Override
	public Map<String, Integer> nowStateStuCountByGrade(String... gradeIds) {
		return studentReportJdbcDao.nowStateStuCountByGrade(gradeIds);
	}

	@Override
	public Map<String, Integer> boardingStuCountByGrade(String... gradeIds) {
		return studentReportJdbcDao.boardingStuCountByGrade(gradeIds);
	}

	@Override
	public Map<String, Integer> classCountBySchool(String... schoolIds) {
		return studentReportJdbcDao.classCountBySchool(schoolIds);
	}
	
	@Override
	public Map<String, Integer> stuCountBySchool(String... schoolIds) {
		return studentReportJdbcDao.stuCountBySchool(schoolIds);
	}

	@Override
	public Map<String, Integer> inCityCountBySchool(String... schoolIds) {
		return studentReportJdbcDao.inCityCountBySchool(schoolIds);
	}

	@Override
	public Map<String, Integer> notCityCountBySchool(String... schoolIds) {
		return studentReportJdbcDao.notCityCountBySchool(schoolIds);
	}

	@Override
	public Map<String, Integer> notHkCountBySchool(String... schoolIds) {
		return studentReportJdbcDao.notHkCountBySchool(schoolIds);
	}

	@Override
	public Map<String, Integer> migrationStuCountBySchool(String... schoolIds) {
		return studentReportJdbcDao.migrationStuCountBySchool(schoolIds);
	}

	@Override
	public Map<String, Integer> sYmigrationStuCountBySchool(String... schoolIds) {
		return studentReportJdbcDao.sYmigrationStuCountBySchool(schoolIds);
	}

	@Override
	public Map<String, Integer> stayinStuCountBySchool(String... schoolIds) {
		return studentReportJdbcDao.stayinStuCountBySchool(schoolIds);
	}

	@Override
	public Map<String, Integer> regularClassStuCountBySchool(
			String... schoolIds) {
		return studentReportJdbcDao.regularClassStuCountBySchool(schoolIds);
	}

	@Override
	public Map<String, Integer> normalStuCountBySchool(String... schoolIds) {
		return studentReportJdbcDao.normalStuCountBySchool(schoolIds);
	}

	@Override
	public Map<String, Integer> compatriotsCountBySchool(String... schoolIds) {
		return studentReportJdbcDao.compatriotsCountBySchool(schoolIds);
	}

	@Override
	public Map<String, Integer> nowStateStuCountBySchool(String... schoolIds) {
		return studentReportJdbcDao.nowStateStuCountBySchool(schoolIds);
	}

	@Override
	public Map<String, Integer> boardingStuCountBySchool(String... schoolIds) {
		return studentReportJdbcDao.boardingStuCountBySchool(schoolIds);
	}

	@Override
	public Map<String, Integer> compatriotsCountByClass(String... classIds) {
		return studentReportJdbcDao.compatriotsCountByClass(classIds);
	}

	@Override
	public Map<String, Integer> nowStateStuCountByClass(String... classIds) {
		return studentReportJdbcDao.nowStateStuCountByClass(classIds);
	}

	@Override
	public Map<String, Integer> classCountBySchoolPrimary(String... schoolIds) {
		return studentReportJdbcDao.classCountBySchoolPrimary(schoolIds);
	}

	@Override
	public Map<String, Integer> stuCountBySchoolPrimary(String... schoolIds) {
		return studentReportJdbcDao.stuCountBySchoolPrimary(schoolIds);
	}

	@Override
	public Map<String, Integer> inCityCountBySchoolPrimary(String... schoolIds) {
		return studentReportJdbcDao.inCityCountBySchoolPrimary(schoolIds);
	}

	@Override
	public Map<String, Integer> notCityCountBySchoolPrimary(String... schoolIds) {
		return studentReportJdbcDao.notCityCountBySchoolPrimary(schoolIds);
	}

	@Override
	public Map<String, Integer> notHkCountBySchoolPrimary(String... schoolIds) {
		return studentReportJdbcDao.notHkCountBySchoolPrimary(schoolIds);
	}

	@Override
	public Map<String, Integer> migrationStuCountBySchoolPrimary(
			String... schoolIds) {
		return studentReportJdbcDao.migrationStuCountBySchoolPrimary(schoolIds);
	}

	@Override
	public Map<String, Integer> sYmigrationStuCountBySchoolPrimary(
			String... schoolIds) {
		return studentReportJdbcDao.sYmigrationStuCountBySchoolPrimary(schoolIds);
	}

	@Override
	public Map<String, Integer> stayinStuCountBySchoolPrimary(
			String... schoolIds) {
		return studentReportJdbcDao.stayinStuCountBySchoolPrimary(schoolIds);
	}

	@Override
	public Map<String, Integer> regularClassStuCountBySchoolPrimary(
			String... schoolIds) {
		return studentReportJdbcDao.regularClassStuCountBySchoolPrimary(schoolIds);
	}

	@Override
	public Map<String, Integer> normalStuCountBySchoolPrimary(
			String... schoolIds) {
		return studentReportJdbcDao.normalStuCountBySchoolPrimary(schoolIds);
	}

	@Override
	public Map<String, Integer> compatriotsCountBySchoolPrimary(
			String... schoolIds) {
		return studentReportJdbcDao.compatriotsCountBySchoolPrimary(schoolIds);
	}

	@Override
	public Map<String, Integer> nowStateStuCountBySchoolPrimary(
			String... schoolIds) {
		return studentReportJdbcDao.nowStateStuCountBySchoolPrimary(schoolIds);
	}

	@Override
	public Map<String, Integer> boardingStuCountBySchoolPrimary(
			String... schoolIds) {
		return studentReportJdbcDao.boardingStuCountBySchoolPrimary(schoolIds);
	}

	@Override
	public Map<String, Integer> classCountByGradeHigh(String... gradeIds) {
		return studentReportJdbcDao.classCountByGradeHigh(gradeIds);
	}

	@Override
	public Map<String, Integer> stuCountByGradeHigh(String... gradeIds) {
		return studentReportJdbcDao.stuCountByGradeHigh(gradeIds);
	}

	@Override
	public Map<String, Integer> inCityCountByGradeHigh(String... gradeIds) {
		return studentReportJdbcDao.inCityCountByGradeHigh(gradeIds);
	}

	@Override
	public Map<String, Integer> notCityCountByGradeHigh(String... gradeIds) {
		return studentReportJdbcDao.notCityCountByGradeHigh(gradeIds);
	}

	@Override
	public Map<String, Integer> notHkCountByGradeHigh(String... gradeIds) {
		return studentReportJdbcDao.notHkCountByGradeHigh(gradeIds);
	}

	@Override
	public Map<String, Integer> migrationStuCountByGradeHigh(String... gradeIds) {
		return studentReportJdbcDao.migrationStuCountByGradeHigh(gradeIds);
	}

	@Override
	public Map<String, Integer> sYmigrationStuCountByGradeHigh(
			String... gradeIds) {
		return studentReportJdbcDao.sYmigrationStuCountByGradeHigh(gradeIds);
	}

	@Override
	public Map<String, Integer> stayinStuCountByGradeHigh(String... gradeIds) {
		return studentReportJdbcDao.stayinStuCountByGradeHigh(gradeIds);
	}

	@Override
	public Map<String, Integer> regularClassStuCountByGradeHigh(
			String... gradeIds) {
		return studentReportJdbcDao.regularClassStuCountByGradeHigh(gradeIds);
	}

	@Override
	public Map<String, Integer> normalStuCountByGradeHigh(String... gradeIds) {
		return studentReportJdbcDao.normalStuCountByGradeHigh(gradeIds);
	}

	@Override
	public Map<String, Integer> compatriotsCountByGradeHigh(String... gradeIds) {
		return studentReportJdbcDao.compatriotsCountByGradeHigh(gradeIds);
	}

	@Override
	public Map<String, Integer> nowStateStuCountByGradeHigh(String... gradeIds) {
		return studentReportJdbcDao.nowStateStuCountByGradeHigh(gradeIds);
	}

	@Override
	public Map<String, Integer> boardingStuCountByGradeHigh(String... gradeIds) {
		return studentReportJdbcDao.boardingStuCountByGradeHigh(gradeIds);
	}

	public Map<String, Integer> ydByClassId(String acadyear, String semester,
			String flowtype, String... classIds) {
		return studentReportJdbcDao.ydByClassId(acadyear, semester, flowtype, classIds);
	}
	
	public Map<String, Integer> ydByClassIdsTypes(String acadyear, String semester, String[] flowTypes, String[] classIds){
		return studentReportJdbcDao.ydByClassIdsTypes(acadyear, semester, flowTypes, classIds);
	}

	public Map<String, Integer> ydByGradeId(String acadyear, String semester,
			String flowtype, String... gradeIds) {
		return studentReportJdbcDao.ydByGradeId(acadyear, semester, flowtype, gradeIds);
	}

	@Override
	public Map<String, Integer> ydBySchIdPrimary(String acadyear,
			String semester, String flowtype, String... schoolIds) {
		return studentReportJdbcDao.ydBySchIdPrimary(acadyear, semester, flowtype, schoolIds);
	}

	public Map<String, Integer> ydBySchIdHign(String acadyear, String semester,
			String flowtype, String... schoolIds) {
		return studentReportJdbcDao.ydBySchIdHign(acadyear, semester, flowtype, schoolIds);
	}

	
}
