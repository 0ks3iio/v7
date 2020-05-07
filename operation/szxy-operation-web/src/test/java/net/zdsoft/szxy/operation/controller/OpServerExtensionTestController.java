package net.zdsoft.szxy.operation.controller;

import net.zdsoft.szxy.base.entity.ServerExtension;
import net.zdsoft.szxy.base.enu.UnitExtensionState;
import net.zdsoft.szxy.operation.servermanage.dao.OpServerExtensionDao;
import net.zdsoft.szxy.operation.servermanage.service.OpServerExService;
import net.zdsoft.szxy.utils.UuidUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

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
public class OpServerExtensionTestController {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Resource
    private OpServerExService opServerExService;

    @Resource
    private OpServerExtensionDao opServerExtensionDao;
    @Resource
    private EntityManager entityManager;

    private ServerExtension opServerExtension;

    @Before
    public void  init(){
        mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        opServerExtension = new ServerExtension();
        opServerExtension.setId(UuidUtils.generateUuid());
        opServerExtension.setUnitId(UuidUtils.generateUuid());
        opServerExtension.setServerCode("22");
        opServerExtension.setExpireTime(new Date());
        opServerExtension.setUsingNature(1);
        opServerExtension.setUsingState(UnitExtensionState.DISABLE);
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
    public void testOpServerExUsingState() throws Exception{
      mockMvc.perform(
                MockMvcRequestBuilders.put("/operation/serverEx/manage/{id}/state/{usingState}/",opServerExtension.getId(),0)
        ).andExpect(MockMvcResultMatchers.status().isOk())
              .andDo(MockMvcResultHandlers.print());
        entityManager.clear();
        Assert.assertEquals(Integer.valueOf(0), opServerExtensionDao.findById(opServerExtension.getId()).get().getUsingState());
    }

    @Test
    public void testOpServerExUsingNature() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.put("/operation/serverEx/manage/{id}/nature/official", opServerExtension.getId(),0)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        entityManager.clear();
        Assert.assertEquals(Integer.valueOf(1), opServerExtensionDao.findById(opServerExtension.getId()).get().getUsingNature());
    }
    @Test
    public void testExpireTime() throws Exception{
        String date = "2019-1-16";
        mockMvc.perform(
                MockMvcRequestBuilders.get("/operation/serverEx/manage/expire").param("expireTime",date)
                        .param("id", opServerExtension.getId())
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testFindAuthorizeSystem() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.get("/operation/serverEx/manage/authorize").param("unitId", opServerExtension.getUnitId())
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        //List<Object[]> obj = opServerExService.getByUnitId(opServerExtension.getUnitId());
        entityManager.clear();
        //Assert.assertEquals(2,obj.size());
    }

}
