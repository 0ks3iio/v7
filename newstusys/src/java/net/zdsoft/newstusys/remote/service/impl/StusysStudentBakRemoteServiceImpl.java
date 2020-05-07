package net.zdsoft.newstusys.remote.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newstusys.dao.StusysStudentBakJdbcDao;
import net.zdsoft.newstusys.entity.StusysStudentBak;
import net.zdsoft.newstusys.remote.service.StusysStudentBakRemoteService;
import net.zdsoft.newstusys.service.StudentGraduateService;
import net.zdsoft.newstusys.service.StusysStudentBakService;

/**
 * 
 * @author weixh
 * 2018年9月18日	
 */
@Service("stusysStudentBakRemoteService")
public class StusysStudentBakRemoteServiceImpl implements StusysStudentBakRemoteService {
	@Autowired
	private StusysStudentBakJdbcDao stusysStudentBakJdbcDao;
	@Autowired
	private StudentGraduateService studentGraduateService;
	@Autowired
	private StusysStudentBakService stusysStudentBakService;

	@Override
	public String findStuBySchId(String schId, String stuName, String stuCode, String idcard) {
		List<StusysStudentBak> baks;
		if(StringUtils.isEmpty(stuName) && StringUtils.isEmpty(stuCode) && StringUtils.isEmpty(idcard)) {
			baks = new ArrayList<>();
			return SUtils.s(baks);
		}
		baks = stusysStudentBakJdbcDao.findBySchIdParams(schId, stuName, stuCode, idcard);
		return SUtils.s(baks);
	}

	@Override
	public String findStuByStuIdSemester(String acadyear, String semester, String stuId) {
		StusysStudentBak bak = null;
		List<StusysStudentBak> baks = stusysStudentBakJdbcDao.findStuByStuIdSemester(acadyear, semester, stuId);
		if(CollectionUtils.isNotEmpty(baks)) {
			bak = baks.get(0);
		}
		return SUtils.s(bak);
	}

	@Override
	public String findByClassIds(String[] classIds) {
		return SUtils.s(studentGraduateService.findByClassIds(classIds));
	}
	@Override
	public void deleteByStuIds(String acadyear,String semester,String[] studentIds){
		stusysStudentBakJdbcDao.deleteByStuIds(acadyear, semester, studentIds);
	}
	@Override
	public void saveAll(StusysStudentBak[] stuBaks){
		stusysStudentBakService.saveAll(stuBaks);
	}
}
