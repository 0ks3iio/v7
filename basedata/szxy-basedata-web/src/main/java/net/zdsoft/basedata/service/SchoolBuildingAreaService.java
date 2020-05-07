package net.zdsoft.basedata.service;

import net.zdsoft.basedata.entity.SchoolBuildingArea;

/**
 * @author yangsj 2017-1-24下午5:19:29
 */
public interface SchoolBuildingAreaService extends BaseService<SchoolBuildingArea, String> {

    void deleteAllByIds(String... ids);

}
