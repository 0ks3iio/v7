package net.zdsoft.szxy.base.service;

import net.zdsoft.szxy.base.api.GradeRemoteService;
import net.zdsoft.szxy.base.dao.GradeDao;
import net.zdsoft.szxy.base.entity.Grade;
import net.zdsoft.szxy.utils.AssertUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shenke
 * @since 2019/3/21 下午4:54
 */
@Service("gradeRemoteService")
public class GradeServiceImpl implements GradeRemoteService {

    @Resource
    private GradeDao gradeDao;

    @Override
    public Grade getGradeById(String gradeId) {
        AssertUtils.notNull(gradeId, "gradeId can't null");
        return gradeDao.getGradeById(gradeId);
    }

    @Override
    public List<Grade> getGradesBySchoolId(String schoolId) {
        AssertUtils.notNull(schoolId, "schoolId can't null");
        Sort sort = Sort.by(Sort.Order.desc("section"), Sort.Order.desc("openAcadyear"));
        return gradeDao.getGradesBySchoolId(schoolId, sort);
    }
}
