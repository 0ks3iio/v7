package net.zdsoft.szxy.operation;

import net.zdsoft.szxy.operation.record.dao.OperationRecordDao;
import net.zdsoft.szxy.operation.record.entity.OperationRecord;
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

/**
 * 
 * 
 * @author panlf 2019年1月16日
 *
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class UnitTreeControllerTest {
	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	@Autowired
	private OperationRecordDao operationRecordDao;

	private OperationRecord opRecord;

	@Before
	public void init() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}


	/**
	 * 根据parentId查找子单位树
	 * 若parentId为空,则返回所有顶级单位
	 * @throws Exception
	 */
	@Test 
	public void findByparentId() throws Exception {
		String pId="00000000000000000000000000610722";
		mockMvc.perform(
					MockMvcRequestBuilders.get("/operation/unit/findUnitByParentId").param("pid", pId)
				).andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}
	
	
	
	/**
	 * 根据unitName查找单位树(模糊查找)
	 * @throws Exception
	 */
	@Test 
	public void findUnitByUnitName() throws Exception {
		String unitName="幼儿园";

		mockMvc.perform(
				MockMvcRequestBuilders.get("/operation/unit/findUnitByUnitName").param("unitName", unitName)
				).andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print());
	}
}
