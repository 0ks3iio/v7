package net.zdsoft.remote.openapi.service;

import java.util.List;

import net.zdsoft.remote.openapi.entity.OpenApiApplyDetail;

public interface OpenApiApplyDetailService {
     List<OpenApiApplyDetail> findByApplyId(String applyId);
}
