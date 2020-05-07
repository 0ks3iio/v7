package net.zdsoft.basedata.extension.remote.service;

import java.util.List;

import net.zdsoft.basedata.extension.entity.UnitExtension;

/**
 * @author shenke
 */
public interface UnitExtensionRemoteService {

    /**
     * 判断当前单位是否可用
     * @param unitId 单位ID
     * @return
     */
    UnitState isEnable(String unitId);
    
    
    void saveAll(List<UnitExtension> unitExtensions);


	List<UnitExtension> findByUnitIdIn(String... unitId);
    
    
}

