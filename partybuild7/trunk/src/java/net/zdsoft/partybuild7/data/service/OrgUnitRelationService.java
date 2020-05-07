package net.zdsoft.partybuild7.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.partybuild7.data.entity.OrgUnitRelation;

import java.util.List;

public interface OrgUnitRelationService extends BaseService<OrgUnitRelation , String> {


    public List<OrgUnitRelation> getAllByUnitId(String unitId);
}
