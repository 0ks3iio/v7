package net.zdsoft.szxy.base.service;

import net.zdsoft.szxy.base.api.ClassRemoteService;
import net.zdsoft.szxy.base.dao.ClassDao;
import net.zdsoft.szxy.base.entity.Clazz;
import net.zdsoft.szxy.utils.AssertUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shenke
 * @since 2019/3/21 下午4:41
 */
@Service("classRemoteService")
public class ClassServiceImpl implements ClassRemoteService {

    @Resource
    private ClassDao classDao;

    @Override
    public Clazz getClazzById(String id) {
        AssertUtils.notNull(id, "id 不能为空");
        return classDao.getClazzById(id);
    }

    @Override
    public List<Clazz> getClazzesByGradeId(String gradeId) {
        AssertUtils.notNull(gradeId, "gradeId 不能为空");
        Sort sort = Sort.by(Sort.Order.asc("section"), Sort.Order.asc("classCode"), Sort.Order.desc("acadyear"));
        return classDao.getClazzesByGradeId(gradeId, sort);
    }
}
