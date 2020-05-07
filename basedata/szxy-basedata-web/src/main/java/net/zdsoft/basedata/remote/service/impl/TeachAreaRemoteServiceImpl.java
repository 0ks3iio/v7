package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.TeachArea;
import net.zdsoft.basedata.remote.service.TeachAreaRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.TeachAreaService;
import net.zdsoft.framework.utils.SUtils;

/**
 * @author yangsj  2018年5月22日下午2:17:58
 */
@Service("teachAreaRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class TeachAreaRemoteServiceImpl extends BaseRemoteServiceImpl<TeachArea,String> implements TeachAreaRemoteService {
	@Autowired
	private TeachAreaService teachAreaService;

    @Override
	protected BaseService<TeachArea, String> getBaseService() {
		return teachAreaService;
	}

	@Override
	public String findByUnitIdIn(String[] uidList) {
		return SUtils.s(teachAreaService.findByUnitIdIn(uidList));
	}


}
