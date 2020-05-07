package net.zdsoft.studevelop.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StudevelopHealthProject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2018/4/13.
 */
public interface StuHealthProjectDao extends BaseJpaRepositoryDao<StudevelopHealthProject , String> {
    @Query(nativeQuery = true,value = "select * from studoc_health_project where acadyear= ?1 and semester=?2 and sch_section=?3 and project_type=?4 and school_id =?5 and project_name=?6 and id != ?7")
    public List<StudevelopHealthProject> getProjectByAcadyearSemesterSection(String acadyear , String semester , String schSection , String projectType , String schoolId , String projectName , String id );

    @Query("From  StudevelopHealthProject  where acadyear= ?1 and semester=?2 and schSection=?3 and projectType=?4 and schoolId =?5 ")
    public List<StudevelopHealthProject> getProjectList(String acadyear , String semester , String schSection , String projectType , String schoolId , Pageable page );

}
