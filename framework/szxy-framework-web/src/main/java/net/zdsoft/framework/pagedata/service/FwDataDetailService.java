package net.zdsoft.framework.pagedata.service;

import java.util.List;

import net.zdsoft.framework.pagedata.entity.FwDataDetail;

public interface FwDataDetailService {
    List<FwDataDetail> findByGroupCodeOrderBy(String groupCode);

    List<FwDataDetail> findByGroupCodeUnHidenOrderBy(String groupCode);

    FwDataDetail findOne(String id);

    List<FwDataDetail> save(List<FwDataDetail> details);

    FwDataDetail save(FwDataDetail detail);

}
