package net.zdsoft.stutotality.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.dao.StutotalityCheckStuDao;
import net.zdsoft.stutotality.data.entity.StutotalityCheckStu;
import net.zdsoft.stutotality.data.service.StutotalityCheckStuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("stutotalityCheckStuService")
public class StutotalityCheckStuServiceImpl extends BaseServiceImpl<StutotalityCheckStu,String> implements StutotalityCheckStuService {

    @Autowired
    private StutotalityCheckStuDao stutotalityCheckStuDao;

    @Override
    public List<StutotalityCheckStu> findByUParms(String unitId, String acadyear, String semester) {
        return stutotalityCheckStuDao.findByUParms(unitId,acadyear,semester);
    }


    @Override
    public List<StutotalityCheckStu> findByGParms(String gradeId, String acadyear, String semester) {
        return stutotalityCheckStuDao.findByGParms(gradeId,acadyear,semester);
    }

    @Override
    public List<StutotalityCheckStu> findBySParms(String[] studentIds, String acadyear, String semester) {
        return stutotalityCheckStuDao.findBySParms(studentIds,acadyear,semester);
    }




    @Override
    protected BaseJpaRepositoryDao<StutotalityCheckStu, String> getJpaDao() {
        return stutotalityCheckStuDao;
    }

    @Override
    protected Class<StutotalityCheckStu> getEntityClass() {
        return StutotalityCheckStu.class;
    }

}
