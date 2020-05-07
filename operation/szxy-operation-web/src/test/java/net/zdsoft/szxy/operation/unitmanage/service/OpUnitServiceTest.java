package net.zdsoft.szxy.operation.unitmanage.service;

import net.zdsoft.szxy.base.entity.ServerExtension;
import net.zdsoft.szxy.base.entity.UnitExtension;
import net.zdsoft.szxy.base.enu.UnitExtensionState;
import net.zdsoft.szxy.operation.unitmanage.dao.UnitExtensionDao;
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

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class OpUnitServiceTest {

    @Resource
    private OpUnitService opUnitService;
    @Autowired
    private UnitExtensionDao opUnitDao;
    @Autowired
    private EntityManager entityManager;

    private UnitExtension opUnit;

    private ServerExtension opServer;

    @Before
    public void init() {
        opUnit = new UnitExtension();
        opUnit.setUsingState(UnitExtensionState.NORMAL);
        opUnit.setUsingNature(0);
        opUnit.setExpireTime(new Date());
        opUnit.setUnitId(UuidUtils.generateUuid());
        opUnit.setExpireTimeType(1);
        opUnit.setServiceExpireTime(new Date());
        opUnit.setId(UuidUtils.generateUuid());
        opUnitDao.save(opUnit);

        opServer = new ServerExtension();
        opServer.setExpireTime(new Date());
        opServer.setId(UuidUtils.generateUuid());
        opServer.setServerCode("55");
        opServer.setUnitId(opUnit.getUnitId());
        opServer.setUsingNature(1);
        opServer.setUsingState(1);
    }

    @Test
    public void test_stopUnit() throws NoUnitExtensionException{
        //opUnitService.stopUnit(opUnit.getUnitId());
        entityManager.clear();
        Assert.assertEquals(Integer.valueOf(1),opUnitDao.findByUnitId(opUnit.getUnitId()).getUsingState());
    }

    @Test
    public void test_recoverUnit() throws NoUnitExtensionException{
        //opUnitService.recoverUnit(opUnit.getUnitId());
        entityManager.clear();
        Assert.assertEquals(Integer.valueOf(0),opUnitDao.findByUnitId(opUnit.getUnitId()).getUsingState());
    }

    @Test
    public void test_turnToOfficial() throws NoUnitExtensionException{
        //opUnitService.turnToOfficial(opUnit.getUnitId());
        entityManager.clear();
        Assert.assertEquals(Integer.valueOf(1),opUnitDao.findByUnitId(opUnit.getUnitId()).getUsingNature());
    }

    @Test
    public void test_updateExpireTimeToPermanent() throws NoUnitExtensionException{
        //opUnitService.updateExpireTimeToPermanent(opUnit.getUnitId());
        entityManager.clear();
        Assert.assertEquals(Integer.valueOf(0),opUnitDao.findByUnitId(opUnit.getUnitId()).getExpireTimeType());
    }

    @Test
    public void test_renewal()throws NoUnitExtensionException,IllegalRenewalTimeException{
        Date date = new Date();
        //opUnitService.renewal(date,"15930439401822061873952788602548");
       // Assert.assertEquals(date,opUnitDao.findByUnitId(opUnit.getUnitId()).getExpireTime());
    }

    @Test
    public void test_renewalServiceTime()throws NoUnitExtensionException,IllegalRenewalTimeException{
        Date date = new Date();
        opUnitService.renewalServiceTime(date,opUnit.getUnitId());
        Assert.assertEquals(date,opUnitDao.findByUnitId(opUnit.getUnitId()).getServiceExpireTime());
    }
}
