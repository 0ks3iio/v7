package net.zdsoft.studevelop.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StuDevelopHolidayNotice;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StuDevelopHolidayNoticeDao extends BaseJpaRepositoryDao<StuDevelopHolidayNotice,String> {

    @Query("From StuDevelopHolidayNotice where acadyear = ?1 and semester = ?2 and unitId=?3 ")
    public List<StuDevelopHolidayNotice> getStuDevelopHolidayNoticeByUnitId(String acadyear, String semester, String unitId);


}
