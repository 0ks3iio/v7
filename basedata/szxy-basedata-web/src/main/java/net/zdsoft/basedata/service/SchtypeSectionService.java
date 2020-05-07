package net.zdsoft.basedata.service;

import java.util.Map;

import net.zdsoft.basedata.entity.SchtypeSection;

public interface SchtypeSectionService extends BaseService<SchtypeSection, String> {

    public SchtypeSection findBySchoolType(String schoolType);

    /**
     * map<schoolType,section>
     */
    public Map<String, String> findAllMap();
}
