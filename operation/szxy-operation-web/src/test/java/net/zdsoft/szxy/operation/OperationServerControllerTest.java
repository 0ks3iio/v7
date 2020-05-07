package net.zdsoft.szxy.operation;

import com.alibaba.fastjson.JSON;
import net.zdsoft.szxy.base.entity.ServerExtension;
import net.zdsoft.szxy.operation.servermanage.dao.OpServerExtensionDao;
import net.zdsoft.szxy.utils.UuidUtils;
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

import java.util.Arrays;
import java.util.List;

/**
 * @author panlf 2019年1月16日
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class OperationServerControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Autowired
    private OpServerExtensionDao opServerExDao;
    private ServerExtension op;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        op = getEntity();
    }

    public ServerExtension getEntity() {
        ServerExtension opServerEx = new ServerExtension();
        opServerEx.setId(UuidUtils.generateUuid());
        //opServerEx.setExpireTime(new Date());
        opServerEx.setServerCode("13");
        opServerEx.setUnitId(UuidUtils.generateUuid());
        opServerEx.setUsingNature(0);
        opServerEx.setUsingState(1);
        return opServerEx;
    }

    @Test
    public void findAllSystem() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/operation/server/findAllSystem")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void unAuthorizeSystem() throws Exception {
        opServerExDao.save(op);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/operation/server/unAuthorizeSystem").param("unitId", op.getUnitId())
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void AuthoringSystem() throws Exception {
        ServerExtension o1 = getEntity();
        ServerExtension o2 = getEntity();
        ServerExtension o3 = getEntity();
        //o1.setServerCode(0);  		//模拟 ServerCode参数不正确
        //o1.setUnitId("363636");      // 模拟 unitId 不存在
        o1.setUsingState(9);            //模拟 UsingState 参数不正确
        List<ServerExtension> asList = Arrays.asList(new ServerExtension[]{o1, o2, o3});
        String toAuthorizeSystems = JSON.toJSONString(asList);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/operation/server/AuthoringSystem").param("systems", toAuthorizeSystems)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

}
