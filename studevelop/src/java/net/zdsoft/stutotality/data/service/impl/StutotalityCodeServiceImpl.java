package net.zdsoft.stutotality.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.dao.StutotalityCodeDao;
import net.zdsoft.stutotality.data.entity.StutotalityCode;
import net.zdsoft.stutotality.data.service.StutotalityCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("stutotalityCodeService")
public class StutotalityCodeServiceImpl extends BaseServiceImpl<StutotalityCode,String> implements StutotalityCodeService {

    @Autowired
    private StutotalityCodeDao stutotalityCodeDao;

    @Override
    public List<StutotalityCode> findListByUnitIdAndTypeAndNameIn(String unitId,Integer type, String[] names){
        return stutotalityCodeDao.findListByUnitIdAndTypeAndNameIn(unitId,type,names);
    }

    @Override
    protected BaseJpaRepositoryDao<StutotalityCode, String> getJpaDao() {
        return stutotalityCodeDao;
    }

    @Override
    protected Class<StutotalityCode> getEntityClass() {
        return StutotalityCode.class;
    }
}
