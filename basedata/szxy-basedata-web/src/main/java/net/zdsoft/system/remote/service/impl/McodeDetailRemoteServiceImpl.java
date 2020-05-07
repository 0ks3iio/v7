package net.zdsoft.system.remote.service.impl;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeDetailRemoteService;
import net.zdsoft.system.service.mcode.McodeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zengzt on 2019/12/16
 */
@Service("mcodeDetailRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class McodeDetailRemoteServiceImpl extends BaseRemoteServiceImpl<McodeDetail,String> implements McodeDetailRemoteService {
    @Autowired
    McodeDetailService mcodeDetailService;

    @Override
    protected BaseService<McodeDetail, String> getBaseService() {
        return mcodeDetailService;
    }

    @Override
    public String findByMcodeIds(String[] mcodeIds) {
        return SUtils.s(mcodeDetailService.findByMcodeIds(mcodeIds));
    }

    @Override
    public String findMapListByMcodeIds(String[] mcodeIds) {
        return SUtils.s(mcodeDetailService.findMapListByMcodeIds(mcodeIds));
    }
}
