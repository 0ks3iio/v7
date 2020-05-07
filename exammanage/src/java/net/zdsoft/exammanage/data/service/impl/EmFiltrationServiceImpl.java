package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmFiltrationDao;
import net.zdsoft.exammanage.data.entity.EmFiltration;
import net.zdsoft.exammanage.data.service.EmFiltrationService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("emFiltrationService")
public class EmFiltrationServiceImpl extends BaseServiceImpl<EmFiltration, String> implements EmFiltrationService {

    @Autowired
    private EmFiltrationDao emFiltrationDao;

    @Override
    protected BaseJpaRepositoryDao<EmFiltration, String> getJpaDao() {
        return emFiltrationDao;
    }

    @Override
    protected Class<EmFiltration> getEntityClass() {
        return EmFiltration.class;
    }

    @Override
    public Map<String, String> findByExamIdAndSchoolIdAndType(String examId,
                                                              String unitId, String type) {
        List<EmFiltration> list = emFiltrationDao.findByExamIdAndSchoolIdAndType(examId, unitId, type);
        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, String> returnMap = new HashMap<String, String>();
            for (EmFiltration f : list) {
                returnMap.put(f.getStudentId(), f.getStudentId());
            }
            return returnMap;
        } else {
            return new HashMap<String, String>();
        }

    }

    @Override
    public List<EmFiltration> saveAllEntitys(EmFiltration... filtration) {
        return emFiltrationDao.saveAll(checkSave(filtration));
    }

    @Override
    public void deleteByExamIdAndStudentIdIn(String examId, String type, String[] studentIds) {
        emFiltrationDao.deleteByExamIdStu(examId, type, studentIds);
    }

    @Override
    public void saveOrDel(List<EmFiltration> fList, String examId,
                          Set<String> studentIds, String type) {
        if (studentIds != null && studentIds.size() > 0) {
            emFiltrationDao.deleteByExamIdStu(examId, type, studentIds.toArray(new String[]{}));
        }
        if (CollectionUtils.isNotEmpty(fList)) {
            saveAllEntitys(fList.toArray(new EmFiltration[]{}));
        }
    }


}

