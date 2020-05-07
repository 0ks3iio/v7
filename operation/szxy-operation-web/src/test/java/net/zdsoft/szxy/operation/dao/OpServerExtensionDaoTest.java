package net.zdsoft.szxy.operation.dao;

import net.zdsoft.szxy.base.entity.ServerExtension;
import net.zdsoft.szxy.base.enu.UnitExtensionState;
import net.zdsoft.szxy.operation.servermanage.dao.OpServerExtensionDao;
import net.zdsoft.szxy.operation.servermanage.service.NoServerExtensionException;
import net.zdsoft.szxy.operation.servermanage.service.OpServerExService;
import net.zdsoft.szxy.operation.unitmanage.service.IllegalRenewalTimeException;
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

/**
 * @author 张帆远
 * @since 2019/1/17  下午15:21
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class OpServerExtensionDaoTest {
    @Resource
    private OpServerExtensionDao opServerExtensionDao;
    @Resource
    private OpServerExService opServerExService;

    @Autowired
    private EntityManager entityManager;

    private ServerExtension opServerExtension;
    @Before
    public void init(){
         opServerExtension = new ServerExtension();
        opServerExtension.setId(UuidUtils.generateUuid());
        opServerExtension.setUnitId(UuidUtils.generateUuid());
        opServerExtension.setServerCode("22");
        opServerExtension.setExpireTime(new Date());
        opServerExtension.setUsingNature(0);
        opServerExtension.setUsingState(UnitExtensionState.NORMAL);
        opServerExtensionDao.save(opServerExtension);

        ServerExtension opServerExtension1 = new ServerExtension();
        opServerExtension1.setId(UuidUtils.generateUuid());
        opServerExtension1.setUnitId(opServerExtension.getUnitId());
        opServerExtension1.setServerCode("23");
        opServerExtension1.setExpireTime(new Date());
        opServerExtension1.setUsingNature(0);
        opServerExtension1.setUsingState(UnitExtensionState.NORMAL);
        opServerExtensionDao.save(opServerExtension1);
    }

    @Test
    public void test_stopServer() throws NoServerExtensionException {
        //opServerExService.stopServer(opServerExtension.getId());
        entityManager.clear();
        Assert.assertEquals(Integer.valueOf(1), opServerExtensionDao.findById(opServerExtension.getId()).get().getUsingState());
    }

    @Test
    public void test_recoverServer() throws NoServerExtensionException{
        //opServerExService.recoverServer(opServerExtension.getId());
        entityManager.clear();
        Assert.assertEquals(Integer.valueOf(0), opServerExtensionDao.findById(opServerExtension.getId()).get().getUsingState());
    }

    @Test
    public void  test_turnToOfficial() throws NoServerExtensionException {
       //opServerExService.turnToOfficial(opServerExtension.getId());
        entityManager.clear();
        Assert.assertEquals(Integer.valueOf(1), opServerExtensionDao.findById(opServerExtension.getId()).get().getUsingNature());
    }

    @Test
    public void test_renewal()throws NoServerExtensionException, IllegalRenewalTimeException {
        Date date = new Date();
        //opServerExService.renewal(date,opServerExtension.getId());
        Assert.assertEquals(date, opServerExtensionDao.findById(opServerExtension.getId()).get().getExpireTime());
    }

//    @Test
//    public void testGetByUnitId(){
//        List<Object[]> obj = opServerExService.getByUnitId(opServerExtension.getUnitId());
//        entityManager.clear();
//        Assert.assertEquals(2,obj.size());
//    }

    

}
