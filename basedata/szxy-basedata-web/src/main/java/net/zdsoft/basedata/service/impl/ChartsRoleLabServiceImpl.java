package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.ChartsRoleLabDao;
import net.zdsoft.basedata.entity.ChartsRoleLab;
import net.zdsoft.basedata.service.ChartsRoleLabService;

@Service("chartsRoleLabService")
public class ChartsRoleLabServiceImpl implements ChartsRoleLabService {

	@Autowired
	private ChartsRoleLabDao chartsRoleLabDao;
	
	@Override
	public List<ChartsRoleLab> findByChartsRoleIdIn(Integer... chartsRoleId) {
		if(chartsRoleId!=null && chartsRoleId.length>0){
			return chartsRoleLabDao.findByChartsRoleIdIn(chartsRoleId);
		}else{
			return new ArrayList<ChartsRoleLab>();
		}
	}

}
