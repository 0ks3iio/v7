package net.zdsoft.szxy.base.api;

import net.zdsoft.szxy.base.RecordName;
import net.zdsoft.szxy.base.entity.MicrocodeDetail;
import net.zdsoft.szxy.monitor.Rpc;

import java.util.List;

/**
 * 基础数据：微代码明细接口
 * @author shenke
 * @since 2019/3/22 上午9:15
 */
@Rpc(domain = RecordName.name)
public interface MicrocodeDetailRemoteService {

    /**
     * 根据微代码ID获取微代码明细详细数据的接口
     * 该接口会自动过滤掉is_using为0的数据
     * @param microcodeId 微代码ID
     * @return {@link List}
     */
    List<MicrocodeDetail> getMicrocodesByMicrocode(String microcodeId);
}
