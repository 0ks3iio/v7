package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.DeptService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;

@Service("deptRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class DeptRemoteServiceImpl extends BaseRemoteServiceImpl<Dept,String> implements DeptRemoteService {

    @Autowired
    private DeptService deptService;

    @Override
    protected BaseService<Dept, String> getBaseService() {
        return deptService;
    }

    @Override
    public String findByUnitId(String unitId) {
        return SUtils.s(deptService.findByUnitId(unitId));
    }

    @Override
    public String findByParentId(String parentId) {
        return SUtils.s(deptService.findByParentId(parentId));
    }

    @Override
    public String findByUnitIdAndParentId(String unitId, String parentId) {
        return SUtils.s(deptService.findByUnitIdAndParentId(unitId, parentId));
    }

    @Override
    public String findByParentIdAndDeptName(String parentId, String deptName) {
        return SUtils.s(deptService.findByParentIdAndDeptName(parentId, deptName));
    }

    @Override
    public String findByUnitIdAndDeputyHeadId(String unitId, String deputyHeadId) {
        return SUtils.s(deptService.findByUnitIdAndDeputyHeadId(unitId, deputyHeadId));
    }

    @Override
    public String findByAreaId(String areaId) {
        return SUtils.s(deptService.findByAreaId(areaId));
    }

    @Override
    public String findByTeacherId(String userId) {
        return SUtils.s(deptService.findByTeacherId(userId));
    }

    @Override
    public String findByUnitIdAndLeaderId(String unitId, String leaderId) {
        return SUtils.s(deptService.findByUnitIdAndLeaderId(unitId, leaderId));
    }
    
    public String findByUnitAndCode(String unitId, String code) {
    	return SUtils.s(deptService.findByUnitAndCode(unitId, code));
    }

    @Override
    public String findByInstituteId(String instituteId) {
        return SUtils.s(deptService.findByInstituteId(instituteId));
    }

    @Override
    public String findByUnitIdAndDeptNameLike(String unitId, String deptName) {
        return SUtils.s(deptService.findByUnitIdAndDeptNameLike(unitId, deptName));
    }

    @Override
    public String saveAllEntitys(String entitys) {
        Dept[] dt = SUtils.dt(entitys, new TR<Dept[]>() {
        });
        return SUtils.s(deptService.saveAllEntitys(dt));
    }

	@Override
	public String findByUnitIdMap(String[] unitIds) {
		return SUtils.s(deptService.findByUnitIdMap(unitIds));
	}

}
