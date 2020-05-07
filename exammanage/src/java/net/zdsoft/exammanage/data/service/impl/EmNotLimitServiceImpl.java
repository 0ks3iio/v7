package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmNotLimitDao;
import net.zdsoft.exammanage.data.entity.EmNotLimit;
import net.zdsoft.exammanage.data.service.EmNotLimitService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("emNotLimitService")
public class EmNotLimitServiceImpl extends BaseServiceImpl<EmNotLimit, String> implements
        EmNotLimitService {
    @Autowired
    private EmNotLimitDao emNotLimitDao;

    @Override
    public List<String> findTeacherIdByUnitId(String unitId) {
        return emNotLimitDao.findByUnitId(unitId);
    }

    @Override
    public void saveTeacherIds(String[] teacherIds, String unitId) {
        deleteByUnitId(unitId);
        if (ArrayUtils.isNotEmpty(teacherIds)) {
            List<EmNotLimit> limitList = new ArrayList<EmNotLimit>();
            EmNotLimit limit = null;
            for (String s : teacherIds) {
                limit = new EmNotLimit();
                limit.setUnitId(unitId);
                limit.setTeacherId(s);
                limit.setId(UuidUtils.generateUuid());
                limitList.add(limit);
            }
            emNotLimitDao.saveAll(limitList);
        }

    }

    @Override
    public void deleteByUnitId(String unitId) {
        emNotLimitDao.deleteByUnitId(unitId);
    }

    @Override
    protected BaseJpaRepositoryDao<EmNotLimit, String> getJpaDao() {
        return emNotLimitDao;
    }

    @Override
    protected Class<EmNotLimit> getEntityClass() {
        return EmNotLimit.class;
    }

}
