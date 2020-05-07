package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.SubSchool;
import net.zdsoft.basedata.remote.service.SubSchoolRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.SubSchoolService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;

@Service("subSchoolRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class SubSchoolRemoteServiceImpl extends BaseRemoteServiceImpl<SubSchool,String> implements SubSchoolRemoteService {

    @Autowired
    private SubSchoolService subSchoolService;

    @Override
    protected BaseService<SubSchool, String> getBaseService() {
        return subSchoolService;
    }

    @Override
    public String saveAllEntitys(String entitys) {
        SubSchool[] dt = SUtils.dt(entitys, new TR<SubSchool[]>() {
        });
        return SUtils.s(subSchoolService.saveAllEntitys(dt));
    }

	@Override
	public String findbySchoolIdIn(String... unitId) {
		return SUtils.s(subSchoolService.findbySchoolIdIn(unitId));
	}

}
