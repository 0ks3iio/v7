package net.zdsoft.studevelop.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Specifications;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.dao.StuHealthProjectDao;
import net.zdsoft.studevelop.data.entity.StudevelopHealthProject;

import net.zdsoft.studevelop.data.service.StuHealthyProjectService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/4/13.
 */
@Service("StuHealthyProjectService")
public class StuHealthyProjectServiceImpl extends BaseServiceImpl<StudevelopHealthProject,String> implements StuHealthyProjectService {
    @Autowired
    private StuHealthProjectDao stuHealthProjectDao;
    @Override
    protected BaseJpaRepositoryDao<StudevelopHealthProject, String> getJpaDao() {
        return stuHealthProjectDao;
    }

    @Override
    protected Class<StudevelopHealthProject> getEntityClass() {
        return StudevelopHealthProject.class;
    }

    @Override
    public List<StudevelopHealthProject> getProjectByAcadyearSemesterSection(final StudevelopHealthProject project) {
        Specification<StudevelopHealthProject> specification = new Specification<StudevelopHealthProject>(){

            @Override
            public Predicate toPredicate(Root<StudevelopHealthProject> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                list.add(cb.equal(root.get("acadyear").as(String.class),project.getAcadyear()));
                list.add(cb.equal(root.get("semester").as(String.class),project.getSemester()));

                list.add(cb.equal(root.get("schoolId").as(String.class),project.getSchoolId()));
                if(StringUtils.isNotEmpty(project.getSchSection())){
                    list.add(cb.equal(root.get("schSection").as(String.class),project.getSchSection()));
                }
                if(StringUtils.isNotEmpty(project.getId())){
                    list.add(cb.notEqual(root.get("id").as(String.class),project.getId()));
                }
                if(StringUtils.isNotEmpty(project.getProjectName())){
                    list.add(cb.equal(root.get("projectName").as(String.class),project.getProjectName()));
                }
                if(StringUtils.isNotEmpty(project.getProjectType())){
                    list.add(cb.equal(root.get("projectType").as(String.class),project.getProjectType()));
                }
                Predicate[] predicates = new Predicate[list.size()];
                criteriaQuery.where(list.toArray(predicates));

                return criteriaQuery.getRestriction();
            }
        };
//        stuHealthProjectDao.getProjectByAcadyearSemesterSection(project.getAcadyear(),project.getSemester(),project.getSchSection(),project.getProjectType(),project.getSchoolId(),project.getProjectName(),project.getId());
        return stuHealthProjectDao.findAll(specification);
    }

    @Override
    public List<StudevelopHealthProject> getProjectList(StudevelopHealthProject project , Pageable page) {
        return stuHealthProjectDao.getProjectList(project.getAcadyear(),project.getSemester(),project.getSchSection(),project.getProjectType(),project.getSchoolId() ,page);

    }

    @Override
    public void copyProject(StudevelopHealthProject project, String itemIds) {
        if(StringUtils.isNotEmpty(itemIds)){
            String[] ids = itemIds.split(",");

            List<StudevelopHealthProject> projectList = findListByIdIn(ids);

            for(StudevelopHealthProject item : projectList){
                item.setAcadyear(project.getAcadyear());
                item.setSemester(project.getSemester());
                item.setIsClosed(StuDevelopConstant.HEALTH_IS_NOT_CLOSED);
                item.setSchSection(project.getSchSection());
                item.setCreationTime(new Date());
                item.setModifyTime(null);
                item.setId(UuidUtils.generateUuid());
            }
            List<StudevelopHealthProject> projects = getProjectList(project,null);
            if(CollectionUtils.isNotEmpty(projects)){
                deleteAll(projects.toArray(new StudevelopHealthProject[0] ));
            }
            saveAll(projectList.toArray(new StudevelopHealthProject[0] ));

        }
    }

    @Override
    public void doCopy(String oldAcadyear, String oldSemester, String acadyear, String semester, String unitId) {
        StudevelopHealthProject project = new StudevelopHealthProject();
        project.setAcadyear(oldAcadyear);
        project.setSemester(oldSemester);
        project.setSchoolId(unitId);
        List<StudevelopHealthProject> healthProjectList = getProjectByAcadyearSemesterSection(project);
        List<StudevelopHealthProject> newHealthProjectList =new ArrayList<>();
        if(CollectionUtils.isNotEmpty(healthProjectList)){
            for(StudevelopHealthProject project1 : healthProjectList){
                StudevelopHealthProject pro = new StudevelopHealthProject();
                pro.setIsClosed(StuDevelopConstant.HEALTH_IS_NOT_CLOSED);
                pro.setSchoolId(unitId);
                pro.setSemester(semester);
                pro.setAcadyear(acadyear);
                pro.setCreationTime(new Date());
                pro.setProjectName(project1.getProjectName());
                pro.setProjectType(project1.getProjectType());
                pro.setProjectUnit(project1.getProjectUnit());
                pro.setSchSection(project1.getSchSection());
                pro.setId(UuidUtils.generateUuid());
                newHealthProjectList.add(pro);

            }
        }
        project.setAcadyear(acadyear);
        project.setSemester(semester);
        List<StudevelopHealthProject> healthProjectList2 = getProjectByAcadyearSemesterSection(project);
        if(CollectionUtils.isNotEmpty(healthProjectList2)){
            deleteAll(healthProjectList2.toArray(new StudevelopHealthProject[0]));
        }

        saveAll(newHealthProjectList.toArray(new StudevelopHealthProject[0]));

    }
}
