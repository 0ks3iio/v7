package net.zdsoft.bigdata.frame.data.kylin;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.taskScheduler.EtlType;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.echarts.EntryUtils.DataException;
import net.zdsoft.bigdata.data.entity.*;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.taskScheduler.listener.EtlChannelConstant;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.datasource.*;
import net.zdsoft.bigdata.datasource.jdbc.JdbcDatabaseAdapter;
import net.zdsoft.bigdata.extend.data.utils.MultiConvertUtils;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJobLog;
import net.zdsoft.bigdata.taskScheduler.entity.KylinCube;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobLogService;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

@Service("kylinClientService")
public class KylinClientServiceImpl implements KylinClientService,
		ApplicationContextAware {

	private Logger logger = Logger.getLogger(KylinClientServiceImpl.class);

	private static DatasourceQueryChecker datasourceQueryChecker;

	private static Database database;

	private static String encoding;

	private static String kylinApiUrl;

	@Autowired
	private EtlJobService etlJobService;

	@Autowired
	private EtlJobLogService etlJobLogService;

	@Autowired
	private OptionService optionService;

	@Resource
	private BigLogService bigLogService;

	@Resource
	private NosqlDatabaseService nosqlDatabaseService;

	@PostConstruct
	public void init() {
		try {
			OptionDto kylinDto = optionService.getAllOptionParam("kylin");
			if (kylinDto == null || kylinDto.getStatus() == 0) {
				logger.error("kylin服务未初始化");
				return;
			}
			initParam(kylinDto.getFrameParamMap().get("kylin_api_url"),
					kylinDto.getFrameParamMap().get("user"), kylinDto
							.getFrameParamMap().get("password"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reInit() {
		try {
			OptionDto kylinDto = optionService.getAllOptionParam("kylin");
			if (kylinDto == null || kylinDto.getStatus() == 0) {
				return;
			}
			logger.error("kylin服务重新初始化");
			initParam(kylinDto.getFrameParamMap().get("kylin_api_url"),
					kylinDto.getFrameParamMap().get("user"), kylinDto
							.getFrameParamMap().get("password"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initParam(String apiUrl, String user, String password)
			throws Exception {
		String method = "POST";
		String para = "user/authentication";

		byte[] key = (user + ":" + password).getBytes();
		encoding = Base64.encodeBase64String(key);
		kylinApiUrl = apiUrl;
		dealKylin(para, method, null);
	}

	public Adapter getAdapter(String dataSourceId, String projectName) {
		if (StringUtils.isBlank(dataSourceId)) {
			OptionDto kylinDto = optionService.getAllOptionParam("kylin");
			if (kylinDto == null || kylinDto.getStatus() == 0) {
				return JdbcDatabaseAdapter.JdbcDatabaseAdapterBuilder.builder()
						.build();
			}
			if (database == null) {
				initDatabase(kylinDto, projectName);
			} else {
				if (kylinDto.getMobility() == 1) {
					initDatabase(kylinDto, projectName);
				} else {
					database.setDbName(projectName);
				}
			}
			if (kylinDto.getMobility() == 1) {
				optionService.updateOptionMobility(kylinDto.getCode(), 0);
			}
			return JdbcDatabaseAdapter.JdbcDatabaseAdapterBuilder.builder()
					.dbName(database.getDbName()).username(database.getUsername())
					.domain(database.getDomain()).password(database.getPassword())
					.port(database.getPort())
					.type(DatabaseType.parse(database.getType())).build();
		} else {
			NosqlDatabase nosqlDatabase = nosqlDatabaseService.findOne(dataSourceId);
			if (nosqlDatabase == null) {
				return JdbcDatabaseAdapter.JdbcDatabaseAdapterBuilder.builder()
						.build();
			}
			return JdbcDatabaseAdapter.JdbcDatabaseAdapterBuilder.builder()
					.dbName(nosqlDatabase.getDbName()).username(nosqlDatabase.getUserName())
					.domain(nosqlDatabase.getDomain()).password(nosqlDatabase.getPassword())
					.port(nosqlDatabase.getPort())
					.type(DatabaseType.parse(nosqlDatabase.getType())).build();
		}
	}

	private void initDatabase(OptionDto kylinDto, String projectName) {
		database = new Database();
		database.setPort(Integer.valueOf(kylinDto.getFrameParamMap()
				.get("port")));
		database.setDomain(kylinDto.getFrameParamMap().get("domain"));
		database.setDbName(projectName);
		database.setUsername(kylinDto.getFrameParamMap().get("user"));
		database.setPassword(kylinDto.getFrameParamMap().get("password"));
		database.setType(DatabaseType.KYLIN.getType());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Json> getDataListFromKylin(String projectName, String sql,
			List<Json> paramList, List<Json> resultFieldList) {
		return getDataListFromKylin(null, projectName, sql, paramList, resultFieldList);
	}

	@Override
	public List<Json> getDataListFromKylin(String dataSourceId, String projectName, String sql,
										   List<Json> paramList, List<Json> resultFieldList) {
		List<Json> resultList = new ArrayList<Json>();
		try {
			Adapter adapter = getAdapter(dataSourceId, projectName);
			QueryExtractor extractor = QueryExtractor
					.extractorResultSetForJSONList();
			QueryStatement<Adapter> queryStatement = new QueryStatement<>(
					adapter, sql);
			@SuppressWarnings("unchecked")
			QueryResponse queryResponse = datasourceQueryChecker.query(
					queryStatement, extractor);
			if (queryResponse.getError() != null) {
				throw new BigDataBusinessException(queryResponse.getError());
			} else {
				return resultList = parse(queryResponse.getQueryValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return resultList;
		}
	}

	public String getDataListFromKylin(String projectName, String sql,
			List<Json> paramList, List<Json> resultFieldList,
			List<Json> rowDimensionList, List<Json> columnDimensionList,
			List<Json> indexList) {
		List<Json> dataList = getDataListFromKylin(projectName, sql, paramList,
				resultFieldList);
		return MultiConvertUtils.getMultiConvertData(dataList, paramList,
				resultFieldList, rowDimensionList, columnDimensionList,
				indexList);
	}

	@Override
	public String getDataListFromKylin(String dataSourceId, String projectName, String sql, List<Json> paramList, List<Json> resultFieldList, List<Json> rowDimensionList, List<Json> columnDimensionList, List<Json> indexList) {
		List<Json> dataList = getDataListFromKylin(dataSourceId, projectName, sql, paramList,
				resultFieldList);
		return MultiConvertUtils.getMultiConvertData(dataList, paramList,
				resultFieldList, rowDimensionList, columnDimensionList,
				indexList);
	}

	private List<Json> parse(Object value) throws DataException {
		try {
			if (value == null) {
				return Collections.emptyList();
			}
			String text = null;
			if (value instanceof String) {
				text = (String) value;
			} else {
				text = JSONObject.toJSONString(value);
			}
			List<Json> list;
			DefaultJSONParser parser = new DefaultJSONParser(text,
					ParserConfig.getGlobalInstance());

			JSONLexer lexer = parser.lexer;
			int token = lexer.token();
			if (token == JSONToken.NULL) {
				lexer.nextToken();
				list = null;
			} else if (token == JSONToken.EOF && lexer.isBlankInput()) {
				list = null;
			} else {
				list = new ArrayList<>();
				parser.parseArray(Json.class, list);
				parser.handleResovleTask(list);
			}
			parser.close();
			return list;
		} catch (Exception e) {
			throw new DataException("数据解析异常");
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		datasourceQueryChecker = applicationContext.getBean(
				DatasourceQueryChecker.NAME, DatasourceQueryChecker.class);
	}

	@Override
	public EtlJob getKylinJob(String unitId, String cubeName) {
		List<EtlJob> jobList = etlJobService.findEtlJobsByJobCode(unitId,
				cubeName);
		if (CollectionUtils.isNotEmpty(jobList)) {
			return jobList.get(0);
		}
		return null;
	}

	@Override
	public void saveKylinJob(EtlJob kylinJob) {
		boolean isAdd = false;
		// 处理业务
		if (StringUtils.isNotBlank(kylinJob.getId())) {
			kylinJob.setModifyTime(new Date());
			EtlJob oldKylin = etlJobService.findOne(kylinJob.getId());
			etlJobService.update(kylinJob, kylinJob.getId(), new String[] {
					"name", "etlType", "unitId", "jobType", "isSchedule",
					"scheduleParam", "remark", "modifyTime", "flowChartJson"});
			//业务日志埋点  修改
			LogDto logDto=new LogDto();
			logDto.setBizCode("update-kylin");
			logDto.setDescription("kylin "+oldKylin.getName());
			logDto.setOldData(oldKylin);
			logDto.setNewData(kylinJob);
			logDto.setBizName("批处理");
			bigLogService.updateLog(logDto);
		} else {
			isAdd = true;
			kylinJob.setId(UuidUtils.generateUuid());
			kylinJob.setCreationTime(new Date());
			kylinJob.setModifyTime(new Date());
			etlJobService.save(kylinJob);
			//业务日志埋点  新增
			LogDto logDto=new LogDto();
			logDto.setBizCode("insert-kylin");
			logDto.setDescription("kylin "+kylinJob.getName());
			logDto.setNewData(kylinJob);
			logDto.setBizName("批处理");
			bigLogService.insertLog(logDto);

		}
		// 如果是定时任务 需要增加到定时任务列表中去 定时任务指定某一台机器执行 通过参数配置
		if (kylinJob.getIsSchedule() == 1) {
			Json json = new Json();
			json.put("jobId", kylinJob.getId());
			json.put("cron", kylinJob.getScheduleParam());
			if (isAdd) {
				json.put("operation", "add");
			} else {
				json.put("operation", "modify");
			}
			RedisUtils.publish(EtlChannelConstant.ETL_KYLIN_REDIS_CHANNEL,
					json.toJSONString());
		} else {
			Json json = new Json();
			json.put("jobId", kylinJob.getId());
			json.put("cron", "");
			json.put("operation", "delete");
			RedisUtils.publish(EtlChannelConstant.ETL_KYLIN_REDIS_CHANNEL,
					json.toJSONString());
		}
	}

	@Override
	public List<KylinCube> getKylinCubesList(String unitId) throws Exception {
		String method = "cubes";
		String result = null;
		try {
			result = dealKylin(method, "GET", null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new ArrayList<KylinCube>();
		}

		List<KylinCube> cubeList = JSONObject.parseArray(result,
				KylinCube.class);

		List<EtlJob> jobList = etlJobService.findEtlJobsByUnitId(unitId,
				EtlType.KYLIN.getValue());
		Map<String, EtlJob> jobMap = new HashMap<String, EtlJob>();
		for (EtlJob job : jobList) {
			jobMap.put(job.getJobCode(), job);
		}
		List<KylinCube> resultList = new ArrayList<KylinCube>();
		for (KylinCube cube : cubeList) {
			if ("DISABLED".equals(cube.getStatus())) {
				continue;
			}
			if (jobMap.containsKey(cube.getName())) {
				cube.setEtlJob(jobMap.get(cube.getName()));
			} else {
				EtlJob newJob = new EtlJob();
				newJob.setName(cube.getName());
				cube.setEtlJob(newJob);
			}
			resultList.add(cube);
		}
		return resultList;
	}

	@Override
	public KylinCube getKylinCube(String cubeName) throws Exception {
		String method = "cubes/" + cubeName;
		String result = dealKylin(method, "GET", null);
		return JSONObject.parseObject(result, KylinCube.class);
	}

	@Override
	public boolean dealJob(EtlJob etlJob) {
		boolean result = true;
		long startTime = System.currentTimeMillis(); // 获取开始时间
		try {
			String method = "cubes/" + etlJob.getName() + "/rebuild";

			Json params = new Json();
			params.put("startTime", "");
			params.put("endTime", "");
			params.put("buildType", "BUILD");
			dealKylin(method, "PUT", params.toJSONString());
			EtlJobLog log = assembledKettleLog(etlJob, startTime,
					EtlJobLog.state_success, "执行成功");
			etlJobLogService.save(log);
			etlJobService.updateEtlJobStateById(etlJob.getId(),
					EtlJob.STATE_TRUE, log.getId());
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			if (errorMsg.length() > 2000) {
				errorMsg = errorMsg.substring(0, 2000);
			}
			EtlJobLog log = assembledKettleLog(etlJob, startTime,
					EtlJobLog.state_fail, errorMsg);
			etlJobLogService.save(log);
			etlJobService.updateEtlJobStateById(etlJob.getId(),
					EtlJob.STATE_ERROR, log.getId());
			result = false;
		}
		return result;
	}

	private String dealKylin(String method, String type, String params)
			throws Exception {
		OptionDto kylinDto = optionService.getAllOptionParam("kylin");
		if (kylinDto == null || kylinDto.getStatus() == 0) {
			throw new BigDataBusinessException("错误码：kylin服务未启动或者未启用");
		}
		StringBuffer out = new StringBuffer();
		int responseCode = 200;
		URL url = new URL(kylinApiUrl + method);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(type);
		connection.setDoOutput(true);
		connection.setRequestProperty("Authorization",
				"Basic " + encoding.replaceAll("\n", ""));
		connection.setRequestProperty("Content-Type",
				"application/json;charset=UTF-8");
		if (StringUtils.isNotBlank(params)) {
			byte[] outputInBytes = params.getBytes("UTF-8");
			OutputStream os = connection.getOutputStream();
			os.write(outputInBytes);
			os.close();
		}
		InputStream content = null;
		responseCode = connection.getResponseCode();
		if (responseCode >= 400) {
			content = connection.getErrorStream();
			throw new BigDataBusinessException("错误码：" + responseCode);
		} else {
			content = connection.getInputStream();
		}
		// 解决乱码问题
		BufferedReader in = new BufferedReader(new InputStreamReader(content,
				Charset.forName("UTF-8")));
		String line;
		while ((line = in.readLine()) != null) {
			out.append(line);
		}
		in.close();
		connection.disconnect();

		return out.toString();
	}

	private EtlJobLog assembledKettleLog(EtlJob etlJob, long startTime,
			int state, String logDescription) {
		EtlJobLog log = new EtlJobLog();
		log.setId(UuidUtils.generateUuid());
		log.setLogTime(new Date());
		log.setJobId(etlJob.getId());
		log.setName(etlJob.getName());
		log.setType("kylin");
		log.setUnitId(etlJob.getUnitId());
		log.setState(state);
		long endTime = System.currentTimeMillis();
		log.setDurationTime((endTime - startTime));
		log.setLogDescription(logDescription);
		return log;
	}

}
