package net.zdsoft.basedata.remote.service.impl;

import net.zdsoft.basedata.entity.TipsayEx;
import net.zdsoft.basedata.remote.service.TipsayExRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.TipsayExService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author niuchao
 * @since  2019年3月22日
 */
@Service("tipsayExRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class TipsayExRemoteServiceImpl extends BaseRemoteServiceImpl<TipsayEx,String> implements TipsayExRemoteService {
	
    @Autowired
    private TipsayExService tipsayExService;

    @Override
    protected BaseService<TipsayEx, String> getBaseService() {
    	return tipsayExService;
    }

	@Override
	public String findByAcadyearAndSemesterAndTeacherId(String acadyear, Integer semester, String teacherId) {
		return tipsayExService.findByAcadyearAndSemesterAndTeacherId(acadyear, semester, teacherId);
	}

}
