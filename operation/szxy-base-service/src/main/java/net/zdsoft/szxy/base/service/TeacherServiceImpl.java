package net.zdsoft.szxy.base.service;

import net.zdsoft.szxy.base.api.TeacherRemoteService;
import net.zdsoft.szxy.base.dao.TeacherDao;
import net.zdsoft.szxy.base.dao.UserDao;
import net.zdsoft.szxy.base.entity.Teacher;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.utils.AssertUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author shenke
 * @since 2019/3/19 下午2:54
 */
@Service("teacherRemoteService")
public class TeacherServiceImpl implements TeacherRemoteService {

    @Resource
    private TeacherDao teacherDao;
    @Resource
    private UserDao userDao;

    @Record(type = RecordType.Call)
    @Override
    public Teacher getTeacherById(String id) {
        return teacherDao.findById(id).orElse(null);
    }

    @Record(type = RecordType.Call)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateDisplayOrder(Integer displayOrder, String id) {
        AssertUtils.notNull(id, "教师ID不能为空");
        AssertUtils.notNull(displayOrder, "新的排序号不能为空");
        teacherDao.updateDisplayOrder(displayOrder, id);
        userDao.getUserByOwnerId(id).ifPresent(user->{
            userDao.updateDisplayOrderById(user.getId(), displayOrder);
        });
    }
}
