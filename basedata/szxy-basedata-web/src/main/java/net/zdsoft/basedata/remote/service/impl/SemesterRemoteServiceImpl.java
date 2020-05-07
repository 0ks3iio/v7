package net.zdsoft.basedata.remote.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.SemesterDao;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.framework.utils.SUtils;

@Service("semesterRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class SemesterRemoteServiceImpl extends BaseRemoteServiceImpl<Semester,String> implements SemesterRemoteService {

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private SemesterDao semesterDao;

    @Override
    protected BaseService<Semester, String> getBaseService() {
        return semesterService;
    }

    @Override
    public String getCurrentSemester(int type) {
        return SUtils.s(semesterService.getCurrentSemester(type));
    }

    @Override
    public String findAcadeyearList() {
        return SUtils.s(semesterService.findAcadeyearList());
    }

    @Override
    public String findByCurrentDay(String currentDay) {
        // TODO Auto-generated method stub
        return SUtils.s(semesterDao.findByCurrentDay(currentDay));
    }

    @Override
    public String findByCurrentDay(String currentDay, int i) {
        // TODO Auto-generated method stub
        return SUtils.s(semesterDao.findByCurrentDay(currentDay, i));
    }

    @Override
    public String findByAcadYearAndSemester(String acadyear, Integer semester) {
        // TODO Auto-generated method stub
        return SUtils.s(semesterDao.findByAcadYearAndSemester(acadyear, semester));
    }

    @Override
    public String findSemesters() {
        // TODO Auto-generated method stub
        return SUtils.s(semesterDao.findSemesters());
    }

    @Override
    public String findDistinctSemesters() {
        // TODO Auto-generated method stub
        return SUtils.s(semesterDao.findDistinctSemesters());
    }

    @Override
    public String findSemestersByDate(Date date) {
        // TODO Auto-generated method stub
        return SUtils.s(semesterDao.findSemestersByDate(date));
    }

	@Override
	public String findListByDate(Date date) {
		return SUtils.s(semesterDao.findListByDate(date));
	}

    @Override
    public String getCurrentSemester(int type, String schoolId) {
        return SUtils.s(semesterService.findCurrentSemester(type, schoolId));
    }

	@Override
	public String findByAcadyearAndSemester(String acadyear, Integer semester, String unitId) {
		return SUtils.s(semesterService.findByAcadyearAndSemester(acadyear, semester, unitId));
	}
}
