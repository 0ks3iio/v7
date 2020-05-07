package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmJoinexamschInfoDao;
import net.zdsoft.exammanage.data.entity.EmJoinexamschInfo;
import net.zdsoft.exammanage.data.service.EmJoinexamschInfoService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("emJoinexamschInfoService")
public class EmJoinexamschInfoServiceImpl extends BaseServiceImpl<EmJoinexamschInfo, String> implements EmJoinexamschInfoService {
    @Autowired
    private EmJoinexamschInfoDao emJoinexamschInfoDao;

    @Override
    protected BaseJpaRepositoryDao<EmJoinexamschInfo, String> getJpaDao() {
        return emJoinexamschInfoDao;
    }

    @Override
    protected Class<EmJoinexamschInfo> getEntityClass() {
        return EmJoinexamschInfo.class;
    }

    @Override
    public void deleteByExamId(String examId) {
        emJoinexamschInfoDao.deleteByExamId(examId);

    }

    @Override
    public List<EmJoinexamschInfo> saveAllEntitys(EmJoinexamschInfo... joinexamschInfo) {
        return emJoinexamschInfoDao.saveAll(checkSave(joinexamschInfo));
    }

    @Override
    public List<EmJoinexamschInfo> findByExamId(String examId) {
        return emJoinexamschInfoDao.findByExamId(examId);
    }

    @Override
    public List<EmJoinexamschInfo> findByExamIdAndPage(String examId, Pagination page) {
        if (page == null) {
            return emJoinexamschInfoDao.findByExamId(examId);
        } else {
            page.setMaxRowCount(emJoinexamschInfoDao.countByExamId(examId));
            return emJoinexamschInfoDao.findByExamId(examId, Pagination.toPageable(page));
        }
    }
}
