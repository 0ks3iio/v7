package net.zdsoft.system.remote.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.dao.mcode.McodeDetailDao;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import net.zdsoft.system.service.mcode.McodeDetailService;

@Service("mcodeRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class McodeRemoteServiceImpl implements McodeRemoteService {

    @Autowired
    private McodeDetailService mcodeDetailService;

    @Autowired
    private McodeDetailDao mcodeDetailDao;

    @Override
    public String findByMcodeIds(String... mcodeIds) {
        return SUtils.s(mcodeDetailService.findByMcodeIds(mcodeIds));
    }

    @Override
    public String findByMcodeAndThisId(String mcodeId, String thisId) {
        return SUtils.s(mcodeDetailService.findByMcodeAndThisId(mcodeId, thisId));
    }

    @Override
    public String findByMcodeContentLike(String mcodeId, String content) {
        return SUtils.s(mcodeDetailDao.findByMcodeContentLike(mcodeId, content));
    }

    @Override
    public String findAllByMcodeIds(String... mcodeIds) {
        return SUtils.s(mcodeDetailDao.findAllByMcodeIds(mcodeIds));
    }

    @Override
    public String findMapByMcodeIds(String... mcodeIds) {
        List<McodeDetail> mds = mcodeDetailService.findByMcodeIds(mcodeIds);
        Map<String, Map<String, McodeDetail>> map = new HashMap<String, Map<String, McodeDetail>>();
        for (McodeDetail md : mds) {
            String mcodeId = md.getMcodeId();
            Map<String, McodeDetail> m = map.get(mcodeId);
            if (m == null) {
                m = new HashMap<String, McodeDetail>();
                map.put(mcodeId, m);
            }
            m.put(md.getThisId(), md);
        }
        return SUtils.s(map);
    }

    @Override
    public void doRefreshCache(String... mcodeId) {
        mcodeDetailService.doRefreshCache(mcodeId);
    }

    @Override
    public void doRefreshCacheAll() {
        mcodeDetailService.doRefreshCacheAll();
    }

    @Override
    public String findMapMapByMcodeIds(String... mcodeIds) {
        return SUtils.s(mcodeDetailService.findMapMapByMcodeIds(mcodeIds));
    }

    @Override
    public String findMapByMcodeId(String mcodeId) {
        return SUtils.s(mcodeDetailService.findMapByMcodeId(mcodeId));
    }
}
