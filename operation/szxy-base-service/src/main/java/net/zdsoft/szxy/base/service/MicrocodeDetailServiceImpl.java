package net.zdsoft.szxy.base.service;

import net.zdsoft.szxy.base.api.MicrocodeDetailRemoteService;
import net.zdsoft.szxy.base.dao.MicrocodeDetailDao;
import net.zdsoft.szxy.base.entity.MicrocodeDetail;
import net.zdsoft.szxy.utils.AssertUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shenke
 * @since 2019/3/22 上午9:22
 */
@Service("microcodeDetailRemoteService")
public class MicrocodeDetailServiceImpl implements MicrocodeDetailRemoteService {

    @Resource
    private MicrocodeDetailDao microcodeDetailDao;

    @Override
    public List<MicrocodeDetail> getMicrocodesByMicrocode(String microcodeId) {
        AssertUtils.notNull(microcodeId, "微代码ID不能为空");
        return microcodeDetailDao.getMicrocodeByMicrocodeId(microcodeId);
    }
}
