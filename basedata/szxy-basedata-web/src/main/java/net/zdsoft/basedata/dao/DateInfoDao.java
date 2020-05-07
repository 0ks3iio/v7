package net.zdsoft.basedata.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DateInfoDao extends BaseJpaRepositoryDao<DateInfo, String> {

    @Query("From DateInfo where schoolId = ?1 and acadyear = ?2 and semester = ?3 and infoDate = ?4")
    public DateInfo find(String schoolId, String acadyear, Integer semester, Date today);

    @Query("From DateInfo where schoolId = ?1 and acadyear = ?2 and semester = ?3 order by infoDate")
    public List<DateInfo> findByAcadyearAndSemester(String schoolId, String acadyear, Integer semester);

    @Query("From DateInfo where schoolId = ?1 and acadyear = ?2 and semester = ?3 and week =?4 order by infoDate")
    public List<DateInfo> findByWeek(String unitId, String acadyear,
                                     Integer semester, Integer week);

    @Query("From DateInfo where schoolId = ?1 and infoDate >= ?2 and infoDate <= ?3 order by infoDate")
    public List<DateInfo> findByDates(String schoolId, Date startDate, Date endDate);

    @Query("From DateInfo where schoolId = ?1 and acadyear = ?2 and semester = ?3 and  infoDate <= ?4 order by infoDate")
    public List<DateInfo> findByAcadyearAndSemesterAndEndDate(String unitId, String acadyear, Integer semester,
                                                              Date endDate);

    @Query("From DateInfo where schoolId = ?1 and acadyear = ?2 and semester = ?3 and  infoDate >= ?4 order by infoDate")
    public List<DateInfo> findByAcadyearAndSemesterAndStartDate(String unitId, String acadyear, Integer semester,
                                                                Date startDate);

    @Query("From DateInfo where schoolId = ?1 and acadyear = ?2 and semester = ?3 and  infoDate >= ?4 and infoDate <= ?5 order by infoDate")
    public List<DateInfo> findByAcadyearAndSemesterAndDates(String unitId, String acadyear, Integer semester,
                                                            Date startDate, Date endDate);

    void deleteDateInfosBySchoolId(String schoolId);

    @Query(nativeQuery = true, value = "select max(week) from sys_date_info where acadyear= ?1 and semester = ?2 and sch_id = ?3")
    Integer findMaxWeekByAcadyearAndSemesterAndSchId(String acadyear, String semester, String schoolId);

    @Query("select week from DateInfo where infoDate = ?1 and schoolId=?2")
    Integer findWeekByInfoDateAndSchoolId(Date date, String schoolId);
}
