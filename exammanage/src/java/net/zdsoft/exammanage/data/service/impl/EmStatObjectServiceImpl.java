package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmStatObjectDao;
import net.zdsoft.exammanage.data.entity.EmStatObject;
import net.zdsoft.exammanage.data.service.EmStatObjectService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("emStatObjectService")
public class EmStatObjectServiceImpl extends BaseServiceImpl<EmStatObject, String> implements
        EmStatObjectService {
    @Autowired
    private EmStatObjectDao emStatObjectDao;

    @Override
    protected BaseJpaRepositoryDao<EmStatObject, String> getJpaDao() {
        return emStatObjectDao;
    }

    @Override
    protected Class<EmStatObject> getEntityClass() {
        return EmStatObject.class;
    }

    @Override
    public EmStatObject findByUnitIdExamId(String unitId, String examId) {

        return emStatObjectDao.findByUnitIdAndExamId(unitId, examId);
    }

    @Override
    public void updateIsStat(String isStat, String... id) {
        if (id == null || id.length <= 0) {
            return;
        }
        emStatObjectDao.updateIsStat(isStat, id);
    }

    @Override
    public List<EmStatObject> findByUnitId(String unitId) {
        return emStatObjectDao.findByUnitId(unitId);
    }

    @Override
    public List<EmStatObject> findByUnitIdAndExamIdIn(String unitId,
                                                      String[] examIds) {
        if (examIds == null || examIds.length <= 0) {
            return new ArrayList<EmStatObject>();
        }
        return emStatObjectDao.findByUnitIdAndExamIdIn(unitId, examIds);
    }
}
