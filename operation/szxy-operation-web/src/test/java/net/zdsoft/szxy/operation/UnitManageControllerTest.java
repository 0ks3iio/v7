package net.zdsoft.szxy.operation;

import net.zdsoft.szxy.base.entity.UnitExtension;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class UnitManageControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UnitExtensionDao opUnitDao;

    private UnitExtension opUnit;
    @Before
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        opUnit = new UnitExtension();
        opUnit.setId(UuidUtils.generateUuid());
        opUnit.setUnitId(UuidUtils.generateUuid());
        opUnit.setExpireTimeType(0);
        opUnit.setUsingNature(0);
        opUnit.setUsingState(1);
        opUnit.setExpireTime(new Date());
        opUnit.setServiceExpireTime(new Date());
        opUnitDao.save(opUnit);
    }

    @Test
    public void testUsingState() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/operation/unit/manage/{unitId}/state/{usingState}/",
                        opUnit.getUnitId(),0)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        entityManager.clear();
        Assert.assertEquals(Integer.valueOf(0),opUnitDao.findByUnitId(opUnit.getUnitId()).getUsingNature());
    }

    @Test
    public void testUsingNature() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/operation/unit/manage/{unitId}/nature/official",opUnit.getUnitId())
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        entityManager.clear();
        Assert.assertEquals(Integer.valueOf(1),opUnitDao.findByUnitId(opUnit.getUnitId()).getUsingNature());
    }

    @Test
    public void testExpireTime() throws Exception{
        String date = "2019-10-15";
        mockMvc.perform(
                MockMvcRequestBuilders.get("/operation/unit/manage/expire")
                        .param("expireTime",date)
                        .param("expireTimeType","0")
                        .param("unitId",opUnit.getUnitId())
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testServiceExpireTime() throws Exception{
        String date = "2019-1-18";
        mockMvc.perform(
                MockMvcRequestBuilders.get("/operation/unit/manage/service").param("serviceExpireTime",date)
                        .param("unitId",opUnit.getUnitId())
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
