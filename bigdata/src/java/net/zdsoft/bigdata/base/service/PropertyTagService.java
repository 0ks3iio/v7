package net.zdsoft.bigdata.base.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.base.entity.PropertyTag;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;

public interface PropertyTagService extends BaseService<PropertyTag,String> {

    void savePropertyTag(PropertyTag propertyTag) throws BigDataBusinessException;
}
