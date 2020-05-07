package net.zdsoft.basedata.service.impl;

import net.zdsoft.basedata.dao.CourseTypeDao;
import net.zdsoft.basedata.entity.CourseType;
import net.zdsoft.basedata.service.CourseTypeService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Pagination;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("SubjectService")
public class CourseTypeServiceImpl extends BaseServiceImpl<CourseType, String> implements CourseTypeService{
	@Autowired
	private CourseTypeDao courseTypeDao;
	
	@Override
	protected BaseJpaRepositoryDao<CourseType, String> getJpaDao() {
		return courseTypeDao;
	}

	@Override
	protected Class<CourseType> getEntityClass() {
		return CourseType.class;
	}

	@Override
	public List<CourseType> findByNameAndTypeWithPage(String seachName, String type, Pagination page) {
		seachName = "%"+seachName+"%";
		List<CourseType> subjectList = courseTypeDao.findByIsDeletedAndNameLikeAndType(Constant.IS_DELETED_FALSE,seachName,type);
		return subjectList;
	}

	@Override
	public List<CourseType> findByName(String name) {
		List<CourseType> subjectList = courseTypeDao.findByIsDeletedAndName(Constant.IS_DELETED_FALSE, name);
		return subjectList;
	}

	@Override
	public List<CourseType> findByCode(String code) {
		List<CourseType> subjectList = courseTypeDao.findByIsDeletedAndCode(Constant.IS_DELETED_FALSE, code);
		return subjectList;
	}

	@Override
	public void updateIsDeleteds(String[] ids) {
		courseTypeDao.updateIsDeleteds(ids);
		
	}

	@Override
	public List<CourseType> findByType(String type) {
		List<CourseType> subjectList = courseTypeDao.findByIsDeletedAndType(Constant.IS_DELETED_FALSE, type);
		return subjectList;
	}
	
	@Override
	public String checkUnique(CourseType subject){
    	String mess="";
    	List<CourseType> s1 = findByName(subject.getName());
    	for(CourseType su:s1){
    		if(subject.getName().equals(su.getName()) && !su.getId().equals(subject.getId())){
    			mess=mess+ "学科名称已重复,";
    			break;
    		}
    	}
    	List<CourseType> s2 = findByCode(subject.getCode());
    	for(CourseType su:s2){
    		if(subject.getCode().equals(su.getCode()) && !su.getId().equals(subject.getId())){
    			mess=mess+ "学科编号已重复,";
    			break;
    		}
    	}
    	//验证编号的 格式
    	if(StringUtils.isBlank(subject.getId()) && !subject.getCode().matches("^[0-9a-zA-Z]+$")){
    		mess=mess+ "学科编号只支持数字、字母";
    	}
    	
    	if(StringUtils.isNotBlank(mess)){
			mess=mess+ "保存失败";
		}
    	return mess;
    }

	@Override
	public List<CourseType> findByTypes(String[] types) {
		return courseTypeDao.findByIsDeletedAndTypeIn(Constant.IS_DELETED_FALSE, types);
	}
}
