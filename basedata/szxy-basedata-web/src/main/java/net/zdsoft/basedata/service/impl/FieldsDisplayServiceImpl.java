package net.zdsoft.basedata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.FieldsDisplayDao;
import net.zdsoft.basedata.entity.FieldsDisplay;
import net.zdsoft.basedata.service.FieldsDisplayService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("fieldsDisplayService")
public class FieldsDisplayServiceImpl extends BaseServiceImpl<FieldsDisplay, String> implements FieldsDisplayService {

    @Autowired
    private FieldsDisplayDao fieldsDisplayDao;

    @Override
    protected BaseJpaRepositoryDao<FieldsDisplay, String> getJpaDao() {
        return fieldsDisplayDao;
    }

    @Override
    protected Class<FieldsDisplay> getEntityClass() {
        return FieldsDisplay.class;
    }

    @Override
    public List<FieldsDisplay> findByColsDisplays(String unitId, String type, Integer colsUse) {
        return fieldsDisplayDao.findByColsDisplays(unitId, type, colsUse);
    }

    @Override
    public List<FieldsDisplay> findByColsDisplays(String unitId, String type) {
        return fieldsDisplayDao.findByColsDisplays(unitId, type);
    }

    @Override
    public List<FieldsDisplay> findByColsDisplays(String parentId, Integer colsUse) {
        return fieldsDisplayDao.findByColsDisplays(parentId, colsUse);
    }
}
