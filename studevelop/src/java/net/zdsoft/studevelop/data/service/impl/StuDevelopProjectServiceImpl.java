package net.zdsoft.studevelop.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.dao.StuDevelopProjectDao;
import net.zdsoft.studevelop.data.entity.StuDevelopProject;
import net.zdsoft.studevelop.data.service.StuDevelopProjectService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("stuDevelopProjectService")
public class StuDevelopProjectServiceImpl extends BaseServiceImpl<StuDevelopProject,String> implements StuDevelopProjectService{
    @Autowired
	private StuDevelopProjectDao stuDevelopProjectDao;
	@Override
	protected BaseJpaRepositoryDao<StuDevelopProject, String> getJpaDao() {
		return stuDevelopProjectDao;
	}

	@Override
	protected Class<StuDevelopProject> getEntityClass() {
		return StuDevelopProject.class;
	}

	@Override
	public void saveStuDevelopProject(StuDevelopProject stuDevelopProject) {
		if(StringUtils.isBlank(stuDevelopProject.getId())){
			String id = UuidUtils.generateUuid();
			stuDevelopProject.setId(id);
			stuDevelopProject.setCreationTime(new Date());
		}
		stuDevelopProjectDao.save(stuDevelopProject);	
	}

	@Override
	public void deleteStuDevelopProject(String[] projectIds) {
		stuDevelopProjectDao.deleteByIds(projectIds);
	}

	@Override
	public List<StuDevelopProject> stuDevelopProjectList(String unitId,
			String acadyear, String semester, String... gradeId) {
		List<StuDevelopProject> stuDevelopProjectList = stuDevelopProjectDao.stuDevelopProjectListByGradeIds(unitId, acadyear, semester, gradeId);
		return stuDevelopProjectList;
	}

	@Override
	public void copyProject(String oldAcadyear, String oldSemester, String[] gradeIds,
			String acadyear, String semester, String unitId) {
		List<StuDevelopProject> stuDevelopProjectList = stuDevelopProjectDao.stuDevelopProjectListByGradeIds(unitId, oldAcadyear, oldSemester, gradeIds);
		List<StuDevelopProject> stuDevelopProjectListTemp = new ArrayList<StuDevelopProject>();		
		for(StuDevelopProject item : stuDevelopProjectList){
			StuDevelopProject pro = new StuDevelopProject();
			pro.setAcadyear(acadyear);
			pro.setSemester(semester);
			pro.setId(UuidUtils.generateUuid());
			pro.setCreationTime(item.getCreationTime());
			pro.setGradeId(item.getGradeId());
			pro.setUnitId(item.getUnitId());
			pro.setProjectName(item.getProjectName());
			pro.setState(item.getState());
			stuDevelopProjectListTemp.add(pro);
		}
		List<StuDevelopProject> yStuDevelopProjectList = stuDevelopProjectDao.stuDevelopProjectListByGradeIds(unitId, acadyear, semester, gradeIds);
	    if(CollectionUtils.isNotEmpty(yStuDevelopProjectList)){
	    	stuDevelopProjectDao.deleteAll(yStuDevelopProjectList);
	    }
		if(CollectionUtils.isNotEmpty(stuDevelopProjectListTemp)){
			stuDevelopProjectDao.saveAll(stuDevelopProjectListTemp);
	    }
	}

}
