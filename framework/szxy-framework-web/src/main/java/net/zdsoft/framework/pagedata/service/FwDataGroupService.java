package net.zdsoft.framework.pagedata.service;

import java.util.List;

import net.zdsoft.framework.pagedata.entity.FwDataGroup;

public interface FwDataGroupService {
    FwDataGroup findByCode(String code);
    
    List<FwDataGroup> findAll();
    
    FwDataGroup findOne(String id);
    
    List<FwDataGroup> save(List<FwDataGroup> groups);
    
    FwDataGroup save(FwDataGroup group);
}
