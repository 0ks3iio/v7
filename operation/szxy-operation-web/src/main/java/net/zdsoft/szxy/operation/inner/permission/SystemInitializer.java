package net.zdsoft.szxy.operation.inner.permission;

import net.zdsoft.szxy.base.enu.DeleteCode;
import net.zdsoft.szxy.base.enu.ID;
import net.zdsoft.szxy.operation.inner.dao.OpUserDao;
import net.zdsoft.szxy.operation.inner.entity.OpUser;
import net.zdsoft.szxy.operation.inner.enums.UserState;
import net.zdsoft.szxy.operation.inner.permission.dao.ModuleOperateDao;
import net.zdsoft.szxy.operation.inner.permission.dao.UserModuleRelationDao;
import net.zdsoft.szxy.operation.inner.permission.entity.Group;
import net.zdsoft.szxy.operation.inner.permission.entity.ModuleOperate;
import net.zdsoft.szxy.operation.inner.permission.entity.UserModuleRelation;
import net.zdsoft.szxy.utils.OSUtils;
import net.zdsoft.szxy.utils.UuidUtils;
import net.zdsoft.szxy.utils.crypto.PasswordUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 初始化系统管理员
 *
 * @author shenke
 * @since 2019/4/11 下午7:57
 */
@Component
class SystemInitializer implements ApplicationListener<ApplicationEvent> {

    @Resource
    private OpUserDao opUserDao;
    @Resource
    private ModuleOperateDao moduleOperateDao;
    @Resource
    private UserModuleRelationDao userModuleRelationDao;


    private OpUser initAdmin() {
        OpUser user = new OpUser();
        user.setId(ID.ZERO_32);
        user.setRealName("系统管理员");
        user.setPhone("15258828767");
        user.setPassword(PasswordUtils.encode(UuidUtils.generateUuid().substring(0, 9)));
        user.setEmail("shenke@winupon.com");
        user.setCreationTime(new Date());
        user.setState(UserState.NORMAL.getState());
        user.setUsername("admin");
        user.setIsDeleted(DeleteCode.NOT_DELETED);
        return user;
    }

    private List<UserModuleRelation> initAuth(String userId) {
        List<ModuleOperate> operates = moduleOperateDao.findAll();
        if (operates.isEmpty()) {
            throw new RuntimeException("数据库中没有权限数据，系统管理员初始化失败");
        }
        List<UserModuleRelation> hasAuths = userModuleRelationDao.getUserModuleRelationsByUserId(userId);
        Set<String> hasAuthIds = hasAuths.stream().map(UserModuleRelation::getOperateId).collect(Collectors.toSet());
        operates = operates.stream().filter(e -> !hasAuthIds.contains(e.getId())).collect(Collectors.toList());

        return operates.stream().map(e -> {
            UserModuleRelation relation = new UserModuleRelation();
            relation.setId(UuidUtils.generateUuid());
            relation.setUserId(userId);
            relation.setOperateId(e.getId());
            relation.setModuleId(e.getModuleId());
            return relation;
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Throwable.class)
    public void init() {
        Optional<OpUser> optional = opUserDao.getOpUserByUsername("admin");
        if (!optional.isPresent()) {
            OpUser user = initAdmin();
            opUserDao.save(user);
            String message = "系统管理员密码:" + PasswordUtils.decode(user.getPassword());
            System.out.println(OSUtils.getOsInfo().isWindows() ? message : "\033[31m" + message + "\033[0m");
        }
        List<UserModuleRelation> modules = initAuth(optional.get().getId());
        if (!modules.isEmpty()) {
            userModuleRelationDao.saveAll(modules);
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        this.init();
    }
}
