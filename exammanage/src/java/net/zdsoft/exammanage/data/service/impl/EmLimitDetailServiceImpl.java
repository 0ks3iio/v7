package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmLimitDetailDao;
import net.zdsoft.exammanage.data.entity.EmLimit;
import net.zdsoft.exammanage.data.entity.EmLimitDetail;
import net.zdsoft.exammanage.data.service.EmLimitDetailService;
import net.zdsoft.exammanage.data.service.EmLimitService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("emLimitDetailService")
public class EmLimitDetailServiceImpl extends BaseServiceImpl<EmLimitDetail, String> implements EmLimitDetailService {

    @Autowired
    private EmLimitDetailDao emLimitDetailDao;
    @Autowired
    private EmLimitService emLimitService;

    @Override
    protected BaseJpaRepositoryDao<EmLimitDetail, String> getJpaDao() {
        return emLimitDetailDao;
    }

    @Override
    protected Class<EmLimitDetail> getEntityClass() {
        return EmLimitDetail.class;
    }

    @Override
    public List<EmLimitDetail> findByLimitIdIn(String[] limitIds) {
        if (limitIds == null || limitIds.length <= 0) {
            return new ArrayList<EmLimitDetail>();
        }
        return emLimitDetailDao.findByLimitIdIn(limitIds);
    }

    @Override
    public void deleteAllByIds(String... id) {
        if (id != null && id.length > 0)
            emLimitDetailDao.deleteAllByIds(id);
    }

    @Override
    public List<EmLimitDetail> saveAllEntitys(EmLimitDetail... emLimitDetailList) {
        if (emLimitDetailList != null && emLimitDetailList.length > 0) {
            return emLimitDetailDao.saveAll(checkSave(emLimitDetailList));
        } else {
            return new ArrayList<EmLimitDetail>();
        }

    }

    @Override
    public void deleteByLimitId(String[] limitId) {
        if (limitId == null || limitId.length <= 0) {
            return;
        }
        emLimitDetailDao.deleteByLimitIdIn(limitId);
    }

    @Override
    public List<EmLimitDetail> findBySubjectTeacher(String examId,
                                                    String teacherId, String subjectId) {
        List<EmLimit> list = emLimitService.findByExamIdST(examId, teacherId, subjectId, null);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<EmLimitDetail>();
        }
        Set<String> limitIds = EntityUtils.getSet(list, "id");

        return findByLimitIdIn(limitIds.toArray(new String[]{}));
    }
}
