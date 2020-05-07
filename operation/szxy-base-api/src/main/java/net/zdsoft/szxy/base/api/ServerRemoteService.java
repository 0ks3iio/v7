package net.zdsoft.szxy.base.api;

import net.zdsoft.szxy.base.RecordName;
import net.zdsoft.szxy.base.entity.Server;
import net.zdsoft.szxy.monitor.Rpc;

import java.util.List;

/**
 * 基础数据：子系统
 * @author shenke
 * @since 2019/3/22 下午1:32
 */
@Rpc(domain = RecordName.name)
public interface ServerRemoteService {

    /**
     * 获取当前平台可用的子系统列表
     * <br>不区分是否为内部或第三方Ap，只包含不是软删、状态为已上线的子系统</br>
     * @return List
     */
    List<Server> getEnableServersForPlatform();

    /**
     * 获取指定单位可用的子系统列表(将会和base_unit_extension表进行关联查询) </br>
     * 当base_server_extension没有任何记录时我们将认为这个单位暂时不受运营平台的管理
     * 这个单位所有可用的子系统的逻辑判断将按照原有的逻辑查询
     * <p>
     *     <li>是否软删</li>
     *     <li>是否上线ap</li>
     *     <li>根据给定单位的单位类型</li>
     *     <li>根据给定单位的订购策略（由运营平台这边维护的授权逻辑）</li>
     * </p>
     * 这里获取的子系统只是说明这个单位可以有这些系统，但不代表这个单位真的有这些系统 </br>
     * 每个单位具体的系统要根据系统管理的授权逻辑来判断
     * @param unitId 单位ID
     * @return List
     */
    List<Server> getEnableServersByUnitId(String unitId);

    /**
     * 根据subId（原sys_subsystem的ID，即model表中的subsystem）获取
     * 不包含软删的数据,以及status!=1的数据
     * @param subIds subId列表
     * @return List
     */
    List<Server> getServersBySubId(Integer[] subIds);
}
