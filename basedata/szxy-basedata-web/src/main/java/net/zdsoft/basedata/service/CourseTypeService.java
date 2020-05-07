package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.CourseType;
import net.zdsoft.framework.entity.Pagination;

public interface CourseTypeService extends BaseService<CourseType, String>{

	List<CourseType> findByNameAndTypeWithPage(String seachName, String type, Pagination page);

	List<CourseType> findByName(String name);

	List<CourseType> findByCode(String code);

	void updateIsDeleteds(String[] array);

	List<CourseType> findByType(String type);

	String checkUnique(CourseType subject);

	List<CourseType> findByTypes(String[] types);

}
