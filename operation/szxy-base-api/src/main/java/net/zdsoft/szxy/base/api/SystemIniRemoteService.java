package net.zdsoft.szxy.base.api;

import net.zdsoft.szxy.base.RecordName;
import net.zdsoft.szxy.base.entity.SystemIni;
import net.zdsoft.szxy.base.wrapper.OptionValue;
import net.zdsoft.szxy.monitor.Rpc;

/**
 * 基础数据
 * @author shenke
 * @since 2019/3/22 上午9:36
 */
@Rpc(domain = RecordName.name)
public interface SystemIniRemoteService {

    /**
     * 根据iniId查询sys_option的数据，封装成编程友好对象
     * 对于空对象、空值将不会做内置的处理
     * @param iniId key
     * @return {@link OptionValue}
     */
    OptionValue getOptionValueByIniId(String iniId);

    /**
     * 获取完整SysOption对象
     * @param iniId key
     * @return 不存在则返回null值
     */
    SystemIni getSystemIniByIniId(String iniId);

    /**
     * 获取sys_option的nowValue值
     * @param iniId key
     * @return String
     */
    String getRawValueByIniId(String iniId);
}