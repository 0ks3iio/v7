package net.zdsoft.studevelop.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StudevelopHealthProject;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Administrator on 2018/4/13.
 */
public interface StuHealthyProjectService extends BaseService<StudevelopHealthProject,String> {


    public List<StudevelopHealthProject> getProjectByAcadyearSemesterSection(StudevelopHealthProject project);
    public List<StudevelopHealthProject> getProjectList(StudevelopHealthProject project , Pageable page);

    public void copyProject(StudevelopHealthProject project , String itemIds);

    public void doCopy(String oldAcadyear, String oldSemester,
                       String acadyear, String semester, String unitId);

}
