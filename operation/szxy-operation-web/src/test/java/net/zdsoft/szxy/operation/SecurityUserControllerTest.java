package net.zdsoft.szxy.operation;

import net.zdsoft.szxy.operation.inner.dao.OpUserDao;
import net.zdsoft.szxy.operation.inner.entity.OpUser;
import net.zdsoft.szxy.utils.UuidUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
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

/**
 * @author shenke
 * @since 2019/1/11 下午4:33
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class SecurityUserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Resource
    private OpUserDao opUserDao;

    private OpUser opUser;
    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        //
        opUser = new OpUser();
        opUser.setId(UuidUtils.generateUuid());
        opUser.setPhone("152");
        opUser.setEmail("121@qq.com");
        opUser.setUsername("test");
        opUser.setPassword("password");
        opUser.setRealName("real-d");
        opUserDao.save(opUser);
    }

    @WithMockUser(username = "test", password = "password")
    @Test
    public void testSecurityUser() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/operation/security-user/test").param("dataKey", "dataKey")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("@.dataKey").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("@.dataKey.username").value("test"));
    }
}
