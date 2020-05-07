package net.zdsoft.szxy.base.service;

import net.zdsoft.szxy.base.api.SchoolRemoteService;
import net.zdsoft.szxy.base.dao.SchoolDao;
import net.zdsoft.szxy.base.dao.UnitDao;
import net.zdsoft.szxy.base.dto.UpdateUnit;
import net.zdsoft.szxy.base.entity.School;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author zhujy
 * 日期:2019/3/19 0019
 */
@Service("schoolRemoteService")
public class SchoolServiceImpl implements SchoolRemoteService {

    @Resource
    private SchoolDao schoolDao;
    @Resource
    private UnitDao unitDao;

    @Override
    public School getSchoolById(String id) {
        return schoolDao.getSchoolById(id);
    }

    @Record(type = RecordType.Call)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateSchool(School school, String[] properties, String parentUnitId) {
        schoolDao.updateSchool(school,properties);

        UpdateUnit updateUnit = new UpdateUnit();
        updateUnit.setParentUnitId(parentUnitId);
        updateUnit.setRegionCode(school.getRegionCode());
        updateUnit.setUnitId(school.getId());
        updateUnit.setUnitName(school.getSchoolName());
        unitDao.updateUnit(updateUnit);
    }
}
