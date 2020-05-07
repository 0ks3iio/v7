package net.zdsoft.bigdata.dataimport.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.ImportResult;
import net.zdsoft.basedata.remote.service.ImportResultRemoteService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.dataimport.listener.EventImportListener;
import net.zdsoft.bigdata.dataimport.utils.ExcelReaderFactory;
import net.zdsoft.bigdata.extend.data.entity.Event;
import net.zdsoft.bigdata.extend.data.service.EventService;
import net.zdsoft.bigdata.frame.data.druid.DruidClientService;
import net.zdsoft.bigdata.frame.data.kafka.KafkaUtils;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/bigdata/event/import")
public class EventDataImportController extends BigdataBaseAction {
	private static Logger logger = LoggerFactory
			.getLogger(EventDataImportController.class);

	@Autowired
	ImportResultRemoteService importResultRemoteService;
	
	@Autowired
	EventService eventService;
	
	@Autowired
	DruidClientService druidClientService;

	@RequestMapping("/index")
	public String index(String eventId, ModelMap map) {
		Event event=eventService.findOne(eventId);
		//确保kafka服务正常
		KafkaUtils kafkaUtils =new KafkaUtils();
		boolean isExistsTopic =false;
		try {
			 isExistsTopic =kafkaUtils.isExistsTopic(event.getTopicName());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		if(!isExistsTopic){
			map.put("errorMsg", "kafka消息Topic不可用，暂时不能导入(可能的原因：1，kafka服务不正常；2，Topic不存在)");
			return "/bigdata/v3/common/error.ftl";
		}
		//确保druid服务正常
		String status =druidClientService.getDruidTaskStatus(event.getTableName());
		if("error".equals(status)){
			map.put("errorMsg", "druid任务不可用，暂时不能导入(可能的原因：1，druid服务不正常；2，druid任务没有启动)");
			return "/bigdata/v3/common/error.ftl";
		}
		map.put("eventId", eventId);
		map.put("businessId", UuidUtils.generateUuid());
		return "/bigdata/extend/event/dataImport/eventDataImport.ftl";
	}

	@RequestMapping("/progress")
	public String progress(String businessId, ModelMap map) {
		map.put("businessId", businessId);
		return "/bigdata/extend/event/dataImport/eventImportProgress.ftl";
	}

	@RequestMapping("/history")
	public String history(String eventId, HttpServletRequest request,
			ModelMap map) {
		Pagination page = createPagination(request);
		page.setPageSize(15);
		List<ImportResult> importResultList = importResultRemoteService
				.findListByUserIdAndBusinessId(getLoginInfo().getUserId(),
						eventId, page);
		map.put("importResultList", importResultList);
		return "/bigdata/extend/event/dataImport/eventImportHistory.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/submit")
	@ControllerInfo(value = "数据导入")
	public Response dataImport(String eventId, String businessId,
			String filePath, String fileName, HttpServletRequest request)
			throws IOException {
		long startTime = System.currentTimeMillis();
		RedisUtils.set("event-import-status-" + businessId, "start");
		ExcelTypeEnum excelType = ExcelTypeEnum.XLSX;
		String fullFilePath = filePath + File.separator + fileName;
		if (fullFilePath.endsWith(".xls")) {
			excelType = ExcelTypeEnum.XLS;
		}
		try (InputStream inputStream = new FileInputStream(fullFilePath)) {
			EventImportListener eventImportListener = new EventImportListener(
					eventId, businessId, getLoginInfo().getUnitId(),
					getLoginInfo().getUserId(), fullFilePath, startTime);
			ExcelReader eventImportReader = ExcelReaderFactory.getExcelReader(
					inputStream, excelType, null, eventImportListener, true);
			eventImportReader.read();
			logger.info("上传文件成功,开始导入数据!");
			return Response.ok().message("上传数据成功,开始导入数据!").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.error().message("上传数据失败!" + e.getMessage()).build();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/common/removeRedisData")
	@ControllerInfo(value = "删除所有状态数据")
	public Response commonRemoveRedisData(HttpServletRequest request)
			throws IOException {
		// bigdata/event/import/common/removeRedisData
		try {
			RedisUtils.delBeginWith("event-import-");
			return Response.ok().message("success").build();
		} catch (Exception e) {
			return Response.error().message(e.getMessage()).build();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getProgress")
	@ControllerInfo(value = "获取导入进度")
	public Response getProgress(String businessId, HttpServletRequest request)
			throws IOException {
		Jedis jedis = RedisUtils.getJedis();
		Json result = new Json();
		List<String> detailLogs = new ArrayList<>();
		try {
			
			String status = RedisUtils.get("event-import-status-"
					+ businessId);
			String processData = RedisUtils.get("event-import-progress-"
					+ businessId);
			if ("error".equals(status)) {
				return Response.error().message(processData).build();
			}
			
			if (StringUtils.isEmpty(processData)) {
				return Response.ok().data("").build();
			}
			result.put("processData", processData);
			int max = 300;
			for (int i = 0; i < max; i++) {
				try {
					String s = RedisUtils.lpop("event-import-detail-"
							+ businessId);
					if (StringUtils.isEmpty(s)) {
						break;
					}
					detailLogs.add(s);
				} catch (Exception e) {
					break;
				}
			}
			if (CollectionUtils.isNotEmpty(detailLogs)) {
				result.put("detailData", JSON.toJSONString(detailLogs));
			} else {
				result.put("detailData", "");
			}
		} finally {
			RedisUtils.returnResource(jedis);
		}
		return Response.ok().data(JSON.toJSONString(result)).build();

	}

	@ResponseBody
	@RequestMapping(value = "/common/test")
	@ControllerInfo(value = "测试数据")
	public String dataImportTest(String businessId, String filePath,
			HttpServletRequest request) throws IOException {
		try {
			long startTime = System.currentTimeMillis();
			filePath = "d:\\test\\test.xls";
			// filePath = "d:\\test\\test2.xlsx";
			businessId = BaseConstants.ZERO_GUID;
			RedisUtils.set("event-import-status-" + businessId, "start");
			RedisUtils.del("event-import-progress-" + businessId);
			ExcelTypeEnum excelType = ExcelTypeEnum.XLSX;
			if (filePath.endsWith(".xls")) {
				excelType = ExcelTypeEnum.XLS;
			}

			try (InputStream inputStream = new FileInputStream(filePath)) {
				EventImportListener eventImportListener = new EventImportListener(
						"76543CD7A115285DE050A8C09B002318", businessId,
						getLoginInfo().getUnitId(), getLoginInfo().getUserId(),
						filePath, startTime);
				ExcelReader eventImportReader = ExcelReaderFactory
						.getExcelReader(inputStream, excelType, null,
								eventImportListener, false);
				eventImportReader.read(new Sheet(1));
			} catch (Exception e) {
				e.printStackTrace();
			}
			long endTime = System.currentTimeMillis();
			System.out.println("耗时间=======:" + (endTime - startTime) + "毫秒");
			return "测试数据成功!";
		} catch (Exception e) {
			e.printStackTrace();
			return "测试数据失败!" + e.getMessage();
		}
	}
}
