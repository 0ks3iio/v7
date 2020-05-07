package net.zdsoft.szxy.operation;

import com.alibaba.fastjson.JSON;
import net.zdsoft.szxy.operation.record.dao.OperationRecordDao;
import net.zdsoft.szxy.operation.record.entity.OperationRecord;
import net.zdsoft.szxy.utils.UuidUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Calendar;

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
public class OperationRecordControllerTest {
	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Autowired
	private OperationRecordDao operationRecordDao;
	
	private OperationRecord opRecord;
	
    
	@Before
	public void init() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		opRecord = new OperationRecord();
		opRecord.setId(getUUID());
		opRecord.setOperateTime(Calendar.getInstance().getTime());
		opRecord.setOperateType(10);
		String unitId = getUUID();
		opRecord.setOperateUnitId(unitId);
		opRecord.setOperatorId(getUUID());
	}

	public String getUUID() {
		return UuidUtils.generateUuid();
	}
	
	
	/**
	 * 保存服务操作记录
	 * @throws Exception
	 */
	@Test 
	public void saveOperationRecord() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/operation/operationRecord/saveOperationRecord")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JSON.toJSONString(opRecord))
				).andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print());
	}
}
