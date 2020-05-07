package net.zdsoft.basedata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.SchoolDao;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.service.SchoolService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("schoolService")
public class SchoolServiceImpl extends BaseServiceImpl<School, String> implements SchoolService {

    @Autowired
    private SchoolDao schoolDao;
    @Autowired
    private UnitService unitService;

    @Override
    protected BaseJpaRepositoryDao<School, String> getJpaDao() {
        return schoolDao;
    }

    @Override
    protected Class<School> getEntityClass() {
        return School.class;
    }

    @Override
    public void saveSchool(School school) {
        this.saveAllEntitys(school);
        Unit unit = unitService.findOne(school.getId());
        unit.setSchoolType(school.getSchoolType());
        unit.setRunSchoolType(school.getRunSchoolType());
        unitService.saveAllEntitys(unit);
    }

    @Override
    public List<School> saveAllEntitys(School... school) {
        return schoolDao.saveAll(checkSave(school));
    }

    @Override
    public String findSectionsById(String id) {
        //因为已经做了本地缓存，可以直接获取对象内容
        School school = findOne(id);
        if (school != null)
            return school.getSections();
        return null;
    }

    @Override
    public void delete(String id) {
        super.delete(id);
    }
}
