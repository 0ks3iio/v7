package net.zdsoft.szxy.operation.dao;

import net.zdsoft.szxy.base.entity.ServerExtension;
import net.zdsoft.szxy.operation.servermanage.dao.OpServerExtensionDao;
import net.zdsoft.szxy.operation.servermanage.dao.ServerManageDao;
import net.zdsoft.szxy.operation.servermanage.service.OpServerExService;
import net.zdsoft.szxy.operation.unitmanage.dto.EnableServerDto;
import net.zdsoft.szxy.utils.UuidUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author panlf   2019年1月16日
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class OpServerExDaoTest {

    @Autowired
    private OpServerExtensionDao opServerExtensionDao;

    @Autowired
    private OpServerExService opServerExService;

    @Autowired
    private ServerManageDao serverManageDao;

    private ServerExtension opServerExtension;

    @Before
    public void init() {
        opServerExtension = getEntity();
    }

    public ServerExtension getEntity() {
        ServerExtension opServerEx = new ServerExtension();
        opServerEx.setId(UuidUtils.generateUuid());
        opServerEx.setExpireTime(new Date());
        opServerEx.setServerCode("13");
        opServerEx.setUnitId(UuidUtils.generateUuid());
        opServerEx.setUsingNature(0);
        opServerEx.setUsingState(1);
        return opServerEx;
    }

    /**
     * 查找所有系统
     *
     * @return
     * @author panlf
     */
    @Test
    public void findAllSystem() {
        List<EnableServerDto> allSystem = serverManageDao.getAllEnableServers();
        Assert.assertTrue("数据查询错误", allSystem.size() > 0);
    }

    /**
     * 查找某个单位没有授权的系统
     *
     * @param unitId
     * @return
     * @author panlf
     */
    @Test
    public void unAuthorizeSystem() {
        String unitId = "53060530121141359760521054241966";
        List<EnableServerDto> unAuthorizeSystem = opServerExService.unAuthorizeSystem(unitId);
        Assert.assertTrue("数据查询错误", unAuthorizeSystem.size() > 0);
    }


}
