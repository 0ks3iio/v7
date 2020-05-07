package net.zdsoft.studevelop.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StudevelopSchoolNotice;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Administrator on 2018/4/8.
 */
public interface StuDevelopSchoolNoticeDao extends BaseJpaRepositoryDao<StudevelopSchoolNotice,String> {

    @Query("From StudevelopSchoolNotice where acadyear=?1 and semester=?2 and  schoolSection=?3 and unitId=?4")
    public StudevelopSchoolNotice getSchoolNoticeByAcadyearSemesterUnitId(String acadyear,String semester , String schoolSection ,String unitId);
}
