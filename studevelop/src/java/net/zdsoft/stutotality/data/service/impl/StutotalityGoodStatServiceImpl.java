package net.zdsoft.stutotality.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.dao.StutotalityGoodStatDao;
import net.zdsoft.stutotality.data.entity.StutotalityGoodStat;
import net.zdsoft.stutotality.data.service.StutotalityGoodStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("stutotalityGoodStatService")
public class StutotalityGoodStatServiceImpl extends BaseServiceImpl<StutotalityGoodStat,String> implements StutotalityGoodStatService {
    @Autowired
    StutotalityGoodStatDao stutotalityGoodStatDao;
    @Override
    protected BaseJpaRepositoryDao<StutotalityGoodStat, String> getJpaDao() {
        return stutotalityGoodStatDao;
    }

    @Override
    protected Class<StutotalityGoodStat> getEntityClass() {
        return StutotalityGoodStat.class;
    }

    @Override
    public List<StutotalityGoodStat> findListByStudentIds(String year, String semete, String[] studentIds) {
        return stutotalityGoodStatDao.findListByStudentIds(year, semete, studentIds);
    }

    @Override
    public List<StutotalityGoodStat> findListByStudentIdsWithMaster(String year, String semete, String[] studentIds) {
        return stutotalityGoodStatDao.findListByStudentIds(year, semete, studentIds);
    }

    @Override
    public void delByStudentIds(String year, String semeter, String[] studentIds) {
        stutotalityGoodStatDao.delByStudentIds(year, semeter, studentIds);
    }

    @Override
    public void saveAll(String year, String semeter, String[] studentIds,List<StutotalityGoodStat> list) {
        stutotalityGoodStatDao.delByStudentIds(year, semeter, studentIds);
        stutotalityGoodStatDao.saveAll(list);
    }
}
