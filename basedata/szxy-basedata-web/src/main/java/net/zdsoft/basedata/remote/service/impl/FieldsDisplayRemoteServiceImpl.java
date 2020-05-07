package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.FieldsDisplay;
import net.zdsoft.basedata.remote.service.FieldsDisplayRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.FieldsDisplayService;
import net.zdsoft.framework.utils.SUtils;

@Service("fieldsDisplayRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class FieldsDisplayRemoteServiceImpl extends BaseRemoteServiceImpl<FieldsDisplay,String> implements
        FieldsDisplayRemoteService {

    @Autowired
    private FieldsDisplayService displayService;

    @Override
    protected BaseService<FieldsDisplay, String> getBaseService() {
        return displayService;
    }

    @Override
    public String findByColsDisplays(String unitId, String type, Integer colsUse) {
        return SUtils.s(displayService.findByColsDisplays(unitId, type, colsUse));
    }

    @Override
    public String findByColsDisplays(String unitId, String type) {
        return SUtils.s(displayService.findByColsDisplays(unitId, type));
    }

    @Override
    public String findByColsDisplays(String parentId, Integer colsUse) {
        return SUtils.s(displayService.findByColsDisplays(parentId, colsUse));
    }

}
