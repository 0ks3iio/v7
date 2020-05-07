package net.zdsoft.szxy.operation;

import net.zdsoft.szxy.operation.unitmanage.dao.OpUnitDao;
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

/**
 * @author ZhuJinY
 * @since 2019年1月16日上午11:38:14
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class OpUnitControllerTest {
	@Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Resource
    private OpUnitDao opUnitDao;
    
    @Before
    public void init() {
    	mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

	/**
	 * 问题 ：查询顶级单位时，效率很低很低需要很久
	 * 测试通过 查询顶层和子集 分页动态sql  可以的
	 * @throws Exception
	 */
	@Test
	public void findUnitPageByParentId() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/operation/unit/manage/page/findUnitPageByParentId")
				.param("_pageIndex","1").param("_pageSize","3")
				.param("parentId","00000000000000000000000000610722")
		).andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}

    /**
     * 测试通过  新增单位可以的
     * @throws Exception
     */
    @Test
    public void saveAllUnit() throws Exception {
    	 mockMvc.perform(
     			MockMvcRequestBuilders.get("/operation/unit/manage/page/saveAllUnit")
     			.param("unitName","测试新增单位5").param("parentId", "00000000000000000000000000610722")
     			.param("schoolName", "测试新增单位5").param("expireTimeType","0")
     			.param("usingNature","4").param("unitClass","2")
     			).andExpect(MockMvcResultMatchers.status().isOk())
     	.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 测试通过  根据unitId 查询3张表数据可以的
     * @throws Exception
     */
    @Test
    public void findUnitByUnitId() throws Exception {
    	mockMvc.perform(
    			MockMvcRequestBuilders.get("/operation/unit/manage/page/findUnitByUnitId")
    			.param("unitId","15930439401822061873952788602548")
    			).andExpect(MockMvcResultMatchers.status().isOk())
    	.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 测试通过  更新可以的
     * @throws Exception
     */
    @Test
    public void updateUnitById() throws Exception {
    	 mockMvc.perform(
      			MockMvcRequestBuilders.get("/operation/unit/manage/page/updateUnitById")
      			.param("id", "67286798092029070430530244786584")
      			.param("unitName","测试更新单位").param("parentId", "00000000000000000000000000610722")
      			.param("schoolName", "测试更新学校").param("expireTimeType", "1")
      			.param("usingNature","1")
      			).andExpect(MockMvcResultMatchers.status().isOk())
      	.andDo(MockMvcResultHandlers.print());
    }


    
    
    
    
    
}
