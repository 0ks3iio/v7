package net.zdsoft.bigdata.extend.data.service.impl;

import com.google.common.collect.Lists;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.TeacherTagResultDao;
import net.zdsoft.bigdata.extend.data.entity.TeacherTagResult;
import net.zdsoft.bigdata.extend.data.service.TeacherTagResultService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class TeacherTagResultServiceImpl extends BaseServiceImpl<TeacherTagResult, String> implements TeacherTagResultService {

    @Resource
    private TeacherTagResultDao teacherTagResultDao;

    @Override
    protected BaseJpaRepositoryDao<TeacherTagResult, String> getJpaDao() {
        return teacherTagResultDao;
    }

    @Override
    protected Class<TeacherTagResult> getEntityClass() {
        return TeacherTagResult.class;
    }

    @Override
    public List<TeacherTagResult> getByTagIds(String[] tagIds, Pagination page) {
        if (tagIds.length < 1) {
            return Lists.newArrayList();
        }
        if (page == null) {
            return teacherTagResultDao.getTeacherTagResultByTagIdIn(tagIds);
        }
        Long count = teacherTagResultDao.countTeacherTagResultByTagIdIn(tagIds);
        page.setMaxRowCount(count == null ? 0 : count.intValue());
        return teacherTagResultDao.getTeacherTagResultByTagIdIn(tagIds, Pagination.toPageable(page));
    }
}
