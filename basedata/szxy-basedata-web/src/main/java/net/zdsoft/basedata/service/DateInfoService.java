package net.zdsoft.basedata.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.entity.DateInfo;

public interface DateInfoService extends BaseService<DateInfo, String> {
    /**
     * 获得当前日期信息
     */
    DateInfo getDate(String schId, String acadyear, Integer semester, Date date);

    /**
     * 获得本学期日期信息
     */
    List<DateInfo> findByAcadyearAndSemester(String schId, String acadyear, Integer semester);

    List<DateInfo> findByWeek(String unitId, String acadyear, Integer semester,
                              Integer week);

    List<DateInfo> findByDates(String schId, Date startDate, Date endDate);

    /**
     * 获得某个学年学期下的日期信息
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @param startDate 可为空
     * @param endDate   可为空
     * @return
     */
    List<DateInfo> findByAcadyearAndSemesterAndDates(String unitId, String acadyear, Integer semester, Date startDate, Date endDate);

    void deleteDateInfosBySchoolId(String unitId);
}
