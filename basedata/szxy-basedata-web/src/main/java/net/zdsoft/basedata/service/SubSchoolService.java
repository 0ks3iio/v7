package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.SubSchool;

public interface SubSchoolService extends BaseService<SubSchool, String> {

    List<SubSchool> saveAllEntitys(SubSchool... subSchool);

	List<SubSchool> findbySchoolIdIn(String... unitId);

}
