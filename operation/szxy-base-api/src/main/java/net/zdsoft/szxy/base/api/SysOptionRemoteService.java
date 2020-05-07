package net.zdsoft.szxy.base.api;

import net.zdsoft.szxy.base.RecordName;
import net.zdsoft.szxy.base.entity.SysOption;
import net.zdsoft.szxy.base.wrapper.OptionValue;
import net.zdsoft.szxy.monitor.Rpc;

/**
 * 基础数据：系统参数
 * @author shenke
 * @since 2019/3/19 下午6:12
 */
@Rpc(domain = RecordName.name)
public interface SysOptionRemoteService {

    SysOption getSysOptionByOptionCode(String optionCode);

    OptionValue getOptionByOptionCode(String optionCode);

    String getValueByOptionCode(String optionCode);
}
