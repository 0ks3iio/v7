package net.zdsoft.bigdata.extend.data.service.impl;

import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.WarningProjectUserDao;
import net.zdsoft.bigdata.extend.data.entity.WarningProjectUser;
import net.zdsoft.bigdata.extend.data.service.WarningProjectUserService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author duhuachao
 * @date 2019/6/12
 */
@Service
public class WarningProjectUserServiceImpl extends BaseServiceImpl<WarningProjectUser,String> implements WarningProjectUserService {

    @Autowired
    private WarningProjectUserDao warningProjectUserDao;
    @Autowired
    private TeacherRemoteService teacherRemoteService;

    @Override
    public List<WarningProjectUser> findByProjectId(String id) {
        List<WarningProjectUser> userList = warningProjectUserDao.findByProjectId(id);
        String[] teacherIds = EntityUtils.getArray(userList,WarningProjectUser::getUsersId,String[]::new);
        List<Teacher> users = SUtils.dt(teacherRemoteService.findListByIds(teacherIds),new TR<List<Teacher>>() {});
        Map<String,String> teacherNameMap = EntityUtils.getMap(users,Teacher::getId,Teacher::getTeacherName);
        userList.forEach(user->{
            user.setUserName(teacherNameMap.get(user.getUsersId()));
        });
        return userList;
    }

    @Override
    public void deleteByProjectId(String projectId) {
        warningProjectUserDao.deleteByProjectId(projectId);
    }

    @Override
    public List<WarningProjectUser> findByUsersId(String ownerId) {
        return warningProjectUserDao.findByUsersId(ownerId);
    }

    @Override
    protected BaseJpaRepositoryDao<WarningProjectUser, String> getJpaDao() {
        return warningProjectUserDao;
    }

    @Override
    protected Class<WarningProjectUser> getEntityClass() {
        return WarningProjectUser.class;
    }
}
