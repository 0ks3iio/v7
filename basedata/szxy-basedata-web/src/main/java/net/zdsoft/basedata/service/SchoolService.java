package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.School;

public interface SchoolService extends BaseService<School, String> {

    void saveSchool(School school);

    List<School> saveAllEntitys(School... school);

    /**
     * 查询学校学段信息
     * 
     * @author cuimq
     * @param id
     * @return
     */
    String findSectionsById(String id);

    /**
     * 删除学校
     * @param id
     */
    @Override
    void delete(String id);
}
