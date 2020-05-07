package net.zdsoft.bigdata.extend.data.service.impl;

import com.google.common.collect.Lists;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.StudentTagResultDao;
import net.zdsoft.bigdata.extend.data.entity.StudentTagResult;
import net.zdsoft.bigdata.extend.data.service.StudentTagResultService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class StudentTagResultServiceImpl extends BaseServiceImpl<StudentTagResult, String> implements StudentTagResultService {

    @Resource
    private StudentTagResultDao studentTagResultDao;

    @Override
    protected BaseJpaRepositoryDao<StudentTagResult, String> getJpaDao() {
        return studentTagResultDao;
    }

    @Override
    protected Class<StudentTagResult> getEntityClass() {
        return StudentTagResult.class;
    }

    @Override
    public List<StudentTagResult> getByTagIds(String[] tagIds, Pagination page) {
        if (tagIds.length < 1) {
            return Lists.newArrayList();
        }
        if (page == null) {
            return studentTagResultDao.getStudentTagResultByTagIdIn(tagIds);
        }
        Long count = studentTagResultDao.countStudentTagResultByTagIdIn(tagIds);
        page.setMaxRowCount(count == null ? 0 : count.intValue());
        return studentTagResultDao.getStudentTagResultByTagIdIn(tagIds, Pagination.toPageable(page));
    }
}
