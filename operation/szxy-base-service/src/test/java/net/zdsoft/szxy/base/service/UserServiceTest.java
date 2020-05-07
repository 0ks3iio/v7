package net.zdsoft.szxy.base.service;

import net.zdsoft.szxy.base.api.UserRemoteService;
import net.zdsoft.szxy.base.dao.UserDao;
import net.zdsoft.szxy.base.entity.User;
import net.zdsoft.szxy.base.enu.UserOwnerTypeCode;
import net.zdsoft.szxy.utils.UuidUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author shenke
 * @since 2019/3/19 下午4:08
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class UserServiceTest {

    @Resource
    private UserDao userDao;
    @Resource
    private UserRemoteService userRemoteService;

    private User user;

    @Before
    public void init() {
        user = new User();
        user.setId(UuidUtils.generateUuid());
        user.setEventSource(1);
        user.setModifyTime(new Date());
        user.setCreationTime(user.getModifyTime());
        user.setAccountId(UuidUtils.generateUuid());
        user.setOwnerType(UserOwnerTypeCode.TEACHER);
        user.setUnitId(UuidUtils.generateUuid());
        user.setRegionCode("330000");
        user.setIsDeleted(0);
        user.setRealName("单位管理员");
        user.setUserState(1);
        user.setOwnerId(UuidUtils.generateUuid());
        user.setUsername("ceshillllll");
        user.setIconIndex(0);
        user.setUserRole(2);
        user.setUserType(1);
        user.setPassword("123456");
        user.setEnrollYear(0);
        user.setDisplayOrder(1);
        user.setSequence(1231);
        userDao.save(user);
    }

    @Test
    public void test_existsUsername() {
        Assert.assertTrue(userRemoteService.existsUsername(user.getUsername()));
    }
}
