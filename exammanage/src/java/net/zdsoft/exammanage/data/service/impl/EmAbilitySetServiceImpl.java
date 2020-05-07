package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmAbilitySetDao;
import net.zdsoft.exammanage.data.entity.EmAbilitySet;
import net.zdsoft.exammanage.data.service.EmAbilitySetService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("emAbilitySetService")
public class EmAbilitySetServiceImpl extends BaseServiceImpl<EmAbilitySet, String> implements EmAbilitySetService {

    @Autowired
    private EmAbilitySetDao emAbilitySetDao;

    @Override
    public List<EmAbilitySet> findListByObjAndExamId(String statObjectId, String examId) {
        return emAbilitySetDao.findListByObjAndExamId(statObjectId, examId);
    }

    @Override
    public void deleteByObjAndExamId(String statObjectId, String examId) {
        emAbilitySetDao.deleteByObjAndExamId(statObjectId, examId);
    }

    @Override
    protected BaseJpaRepositoryDao<EmAbilitySet, String> getJpaDao() {
        return emAbilitySetDao;
    }

    @Override
    protected Class<EmAbilitySet> getEntityClass() {
        return EmAbilitySet.class;
    }

}
