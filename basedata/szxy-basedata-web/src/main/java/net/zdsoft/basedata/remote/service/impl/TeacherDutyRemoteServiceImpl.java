package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.UnitDao;
import net.zdsoft.basedata.entity.TeacherDuty;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.enums.UnitClassEnum;
import net.zdsoft.basedata.remote.service.TeacherDutyRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.TeacherDutyService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.remote.service.McodeRemoteService;

@Service("teacherDutyRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class TeacherDutyRemoteServiceImpl extends BaseRemoteServiceImpl<TeacherDuty,String> implements
        TeacherDutyRemoteService {

    @Autowired
    private UnitDao unitDao;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private UserService userService;
    @Autowired
    private TeacherDutyService teacherDutyService;

    @Override
    protected BaseService<TeacherDuty, String> getBaseService() {
        return teacherDutyService;
    }

    @Override
    public String findByTeacherIds(String dutyCode, String[] teacherIds) {
        return SUtils.s(teacherDutyService.findByTeacherIds(dutyCode, teacherIds));
    }

    @Override
    public String findByTeacherIds(String[] teacherIds) {
        return SUtils.s(teacherDutyService.findByTeacherIds(teacherIds));
    }

    @Override
    public String findDutysByUnitId(String unitId) {
        Unit unit = unitDao.findById(unitId).orElse(null);
        if (unit != null) {
            if (UnitClassEnum.SCHOOL.getValue() == unit.getUnitClass()) {
                return mcodeRemoteService.findAllByMcodeIds("DM-XXZW");
            }
            else {
                return mcodeRemoteService.findAllByMcodeIds("DM-JYJZW");
            }
        }
        return null;
    }

    @Override
    public String findDutysByUnitIdAndDuty(String unitId, String dutyCode) {
        return SUtils.s(userService.findByUnitDutyCode(unitId, dutyCode));
    }

}
