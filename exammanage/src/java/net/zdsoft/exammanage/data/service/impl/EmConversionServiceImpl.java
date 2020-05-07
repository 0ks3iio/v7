package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmConversionDao;
import net.zdsoft.exammanage.data.entity.EmConversion;
import net.zdsoft.exammanage.data.service.EmConversionService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("emConversionService")
public class EmConversionServiceImpl extends BaseServiceImpl<EmConversion, String> implements EmConversionService {
    @Autowired
    private EmConversionDao emConversionDao;

    @Override
    public List<EmConversion> findByUnitId(String unitId) {
        return emConversionDao.findByUnitId(unitId);
    }

    @Override
    public void saveAllEntity(String unitId, List<EmConversion> insertlist) {
        List<EmConversion> list = emConversionDao.findByUnitId(unitId);
        deleteAll(list.toArray(new EmConversion[]{}));
        saveAll(insertlist.toArray(new EmConversion[]{}));
    }

    @Override
    protected BaseJpaRepositoryDao<EmConversion, String> getJpaDao() {
        return emConversionDao;
    }

    @Override
    protected Class<EmConversion> getEntityClass() {
        return EmConversion.class;
    }

}