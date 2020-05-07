package net.zdsoft.basedata.extension.remote.service;

import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.extension.entity.ServerExtension;

/**
 * @author shenke
 * @since 2019/3/26 上午11:09
 */
public interface ServerExtensionRemoteService {

    /**
     * 获取指定单位可用的子系统集合
     * @param unitId 单位ID
     * @return 返回null则意味着该单位没有经过运营平台管理
     */
    Set<String> getEnableServerCodesByUnitId(String unitId);
    
    
    void saveAll(List<ServerExtension> serverExtension);


	List<ServerExtension> findByUnitIdIn(String... unitIds);


	void deleteByUnitIdIn(String... unitIds);
}
