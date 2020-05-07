package net.zdsoft.basedata.service.impl;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.dao.DateInfoDao;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.service.DateInfoService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dateInfoService")
public class DateInfoServiceImpl extends BaseServiceImpl<DateInfo, String> implements DateInfoService {

    @Autowired
    private DateInfoDao dateInfoDao;

    @Override
    public DateInfo getDate(String schId, String acadyear, Integer semester,
                            Date date) {
        return dateInfoDao.find(schId, acadyear, semester, date);
    }

    @Override
    public List<DateInfo> findByDates(String schId, Date startDate,
                                      Date endDate) {
        return dateInfoDao.findByDates(schId, startDate, endDate);
    }

    @Override
    public List<DateInfo> findByAcadyearAndSemester(String schId,
                                                    String acadyear, Integer semester) {
        return dateInfoDao.findByAcadyearAndSemester(schId, acadyear, semester);
    }

    @Override
    public List<DateInfo> findByWeek(String unitId, String acadyear,
                                     Integer semester, Integer week) {
        if (week == null) {
            return findByAcadyearAndSemester(unitId, acadyear, semester);
        }
        return dateInfoDao.findByWeek(unitId, acadyear, semester, week);
    }

    @Override
    protected BaseJpaRepositoryDao<DateInfo, String> getJpaDao() {
        return dateInfoDao;
    }

    @Override
    protected Class<DateInfo> getEntityClass() {
        return DateInfo.class;
    }

    @Override
    public List<DateInfo> findByAcadyearAndSemesterAndDates(String unitId, String acadyear, Integer semester,
                                                            Date startDate, Date endDate) {
        if (startDate == null && endDate == null) {
            return findByAcadyearAndSemester(unitId, acadyear, semester);
        } else if (startDate == null && endDate != null) {
            return dateInfoDao.findByAcadyearAndSemesterAndEndDate(unitId, acadyear, semester, endDate);
        } else if (startDate != null && endDate == null) {
            return dateInfoDao.findByAcadyearAndSemesterAndStartDate(unitId, acadyear, semester, startDate);
        } else {
            return dateInfoDao.findByAcadyearAndSemesterAndDates(unitId, acadyear, semester, startDate, endDate);
        }
    }

    @Override
    public void deleteDateInfosBySchoolId(String unitId) {
        dateInfoDao.deleteDateInfosBySchoolId(unitId);
    }
}
