package net.zdsoft.bigdata.data.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import net.zdsoft.bigdata.daq.data.component.BizOperationLogCollector;
import net.zdsoft.bigdata.daq.data.entity.BizOperationLog;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.entity.DataModel;
import net.zdsoft.bigdata.data.service.DataModelService;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.frame.data.hive.HiveClientService;
import net.zdsoft.bigdata.frame.data.impala.ImpalaClientService;
import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/bigdata/datedimension")
public class DateDimensionController extends BigdataBaseAction {

	@Autowired
	private MysqlClientService mysqlClientService;

	@Autowired
	private ImpalaClientService impalaClientService;

	@Autowired
	private HiveClientService hiveClientService;

	@Autowired
	private DataModelService dataModelService;

	@Autowired
	private OptionService optionService;

	/**
	 * 时间维度detail页面
	 * 
	 * @return
	 */
	@RequestMapping("/detail")
	public String detail(String modeld, ModelMap map) {
		DataModel model = dataModelService.findOne(modeld);

		map.put("tableName", model.getDateDimTable());
		if ("mysql".equals(model.getType())) {
			OptionDto mysqlDto = optionService.getAllOptionParam("mysql");
			String mysqlDbname = "default";
			if (mysqlDto != null && mysqlDto.getStatus() == 1) {
				mysqlDbname = mysqlDto.getFrameParamMap().get("database");
			}
			String sql = "select count(1) as totalCount, max(full_date) as endDate,min(full_date) as startDate from "
					+ model.getDateDimTable();
			List<Json> resultList = mysqlClientService.getDataListFromMysql(null,
					mysqlDbname, sql, new ArrayList<Json>(),
					new ArrayList<Json>());
			if (CollectionUtils.isNotEmpty(resultList)) {
				map.put("startDate", resultList.get(0).getString("startdate"));
				map.put("endDate", resultList.get(0).getString("enddate"));
				map.put("totalCount",
						resultList.get(0).getIntValue("totalcount"));
			}
		} else if ("impala".equals(model.getType())) {
			String sql = "select count(1) as totalCount, max(full_date) as endDate,min(full_date) as startDate from "
					+ model.getDateDimTable();
			OptionDto impalaDto = optionService.getAllOptionParam("impala");
			String impalaDbname = "default";
			if (impalaDto != null && impalaDto.getStatus() == 1) {
				impalaDbname = impalaDto.getFrameParamMap().get("database");
			}
			List<Json> resultList = impalaClientService.getDataListFromImpala(
					impalaDbname, sql, new ArrayList<Json>(),
					new ArrayList<Json>());
			if (CollectionUtils.isNotEmpty(resultList)) {
				map.put("startDate", resultList.get(0).getString("startdate"));
				map.put("endDate", resultList.get(0).getString("enddate"));
				map.put("totalCount",
						resultList.get(0).getIntValue("totalcount"));
			}
		} else if ("kylin".equals(model.getType())) {
			String sql = "select count(1) as totalCount, max(full_date) as endDate,min(full_date) as startDate from "
					+ model.getDateDimTable();
			OptionDto hiveDto = optionService.getAllOptionParam("hive");
			String hiveDbname = "default";
			if (hiveDto != null && hiveDto.getStatus() == 1) {
				hiveDbname = hiveDto.getFrameParamMap().get("database");
			}
			List<Json> resultList = hiveClientService.getDataListFromHive(
					hiveDbname, sql, new ArrayList<Json>(),
					new ArrayList<Json>());
			if (CollectionUtils.isNotEmpty(resultList)) {
				map.put("startDate", resultList.get(0).getString("startdate"));
				map.put("endDate", resultList.get(0).getString("enddate"));
				map.put("totalCount",
						resultList.get(0).getIntValue("totalcount"));
			}
		} else {
			return "/bigdata/v3/common/error.ftl";
		}
		map.put("type", model.getType());
		return "/bigdata/dataModel/dateDimensionDetail.ftl";
	}

	@ResponseBody
	@ControllerInfo("初始化数据")
	@RequestMapping("/init")
	public String init(String type, String startDate, String endDate,
			String tableName) {
		try {
			if ("impala".equals(type)) {
				OptionDto impalaDto = optionService.getAllOptionParam("impala");
				if (impalaDto == null || impalaDto.getStatus() == 0) {
					return error("创建失败,原因(Impala暂未启用)");
				}
				OptionDto kuduDto = optionService.getAllOptionParam("kudu");
				if (kuduDto == null || kuduDto.getStatus() == 0) {
					return error("创建失败,原因(kudu暂未启用)");
				}
				String dbName = impalaDto.getFrameParamMap().get("database");
				StringBuffer kuduAddress = new StringBuffer();
				kuduAddress.append(kuduDto.getFrameParamMap().get("domain"));
				kuduAddress.append(":");
				kuduAddress.append(kuduDto.getFrameParamMap().get("port"));
				String createTableSql = assembledKuduTableSql(tableName,
						kuduAddress.toString());
				boolean isSucces = impalaClientService.execSql(dbName,
						createTableSql);
				if (!isSucces) {
					return error("创建Impala时间维度表失败");
				}

				StringBuffer deleteSbSql = new StringBuffer();
				deleteSbSql.append("delete from @tableName ;");
				String deleteSql = deleteSbSql.toString().replace("@tableName",
						tableName);
				isSucces = impalaClientService.execSql(dbName, deleteSql);
				if (!isSucces) {
					return error("删除Impala时间维度表中数据失败");
				}
				int totalSize = execKuduInsertSql(dbName, tableName, startDate,
						endDate);
				// 业务日志埋点
				BizOperationLog bizLog = new BizOperationLog();
				bizLog.setLogType(BizOperationLog.LOG_TYPE_INIT);
				bizLog.setBizCode("model-date-dim-init");
				bizLog.setDescription( "impala时间维度数据初始化"+totalSize+"条记录");
				bizLog.setBizName("数据模型管理");
				bizLog.setSubSystem("大数据管理");
				bizLog.setOperator(getLoginInfo().getRealName() + "("
						+ getLoginInfo().getUserName() + ")");
				bizLog.setOperationTime(new Date());
				BizOperationLogCollector.submitBizOperationLog(bizLog);
				return success("" + (totalSize));
			} else if ("kylin".equals(type)) {
				OptionDto kylinDto = optionService.getAllOptionParam("kylin");
				if (kylinDto == null || kylinDto.getStatus() == 0) {
					return error("创建失败,原因(kylin暂未启用)");
				}
				OptionDto hiveDto = optionService.getAllOptionParam("hive");
				if (hiveDto == null || hiveDto.getStatus() == 0) {
					return error("创建失败,原因(hive暂未启用)");
				}

				String dbName = hiveDto.getFrameParamMap().get("database");
				String createTableSql = assembledHiveTableSql(tableName);
				boolean isSucces = hiveClientService.execSql(dbName,
						createTableSql);
				if (!isSucces) {
					return error("创建hive时间维度表失败");
				}

				StringBuffer deleteSbSql = new StringBuffer();
				deleteSbSql.append("truncate table @tableName");
				String deleteSql = deleteSbSql.toString().replace("@tableName",
						tableName);
				isSucces = hiveClientService.execSql(dbName, deleteSql);
				if (!isSucces) {
					return error("删除hive时间维度表中数据失败");
				}

				int totalSize = execHiveInsertSql(dbName, tableName, startDate,
						endDate);
				// 业务日志埋点
				BizOperationLog bizLog = new BizOperationLog();
				bizLog.setLogType(BizOperationLog.LOG_TYPE_INIT);
				bizLog.setBizCode("model-date-dim-init");
				bizLog.setDescription( "kylin时间维度数据初始化"+totalSize+"条记录");
				bizLog.setBizName("数据模型管理");
				bizLog.setSubSystem("大数据管理");
				bizLog.setOperator(getLoginInfo().getRealName() + "("
						+ getLoginInfo().getUserName() + ")");
				bizLog.setOperationTime(new Date());
				bizLog.setNewData(type+"/"+startDate+"/"+endDate+"/"+tableName);
				BizOperationLogCollector.submitBizOperationLog(bizLog);
				return success("" + (totalSize));
			} else if ("mysql".equals(type)) {
				OptionDto mysqlDto = optionService.getAllOptionParam("mysql");
				if (mysqlDto == null || mysqlDto.getStatus() == 0) {
					return error("创建失败,原因(mysql暂未启用)");
				}
				String mysqlDbname = mysqlDto.getFrameParamMap()
						.get("database");
				// 起始日期参数
				Json startDateJson = new Json();
				startDateJson.put("dataType", "string");
				startDateJson.put("value", startDate);
				// 结束日期参数
				Json endDateJson = new Json();
				endDateJson.put("dataType", "string");
				endDateJson.put("value", endDate);
				// 参数
				Json tableNameJson = new Json();
				tableNameJson.put("dataType", "string");
				tableNameJson.put("value", tableName);

				List<Json> inParams = new LinkedList<Json>();
				inParams.add(startDateJson);
				inParams.add(endDateJson);
				inParams.add(tableNameJson);
				// 返回记录数参数
				Json totalSizeJson = new Json();
				totalSizeJson.put("dataType", "int");
				totalSizeJson.put("field", "totalSize");
				List<Json> outParams = new LinkedList<Json>();
				outParams.add(totalSizeJson);

				String sql = "{CALL pro_init_date_dim(?,?,?,?)}";
				Json result = mysqlClientService.execProc(null,mysqlDbname, sql,
						inParams, outParams);
				
				// 业务日志埋点
				BizOperationLog bizLog = new BizOperationLog();
				bizLog.setLogType(BizOperationLog.LOG_TYPE_INIT);
				bizLog.setBizCode("model-date_dim-init");
				bizLog.setDescription( "mysql时间维度数据初始化"+result.getIntValue("totalSize")+"条记录");
				bizLog.setBizName("数据模型管理");
				bizLog.setSubSystem("大数据管理");
				bizLog.setOperator(getLoginInfo().getRealName() + "("
						+ getLoginInfo().getUserName() + ")");
				bizLog.setOperationTime(new Date());
				BizOperationLogCollector.submitBizOperationLog(bizLog);
				return success("" + result.getIntValue("totalSize"));
			}
			return error("不支持的类型");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}

	private String assembledKuduTableSql(String tableName, String kuduAddress) {
		StringBuffer createSql = new StringBuffer();
		createSql.append("create table if not exists @tableName(");
		createSql.append("id	string,");
		createSql.append("full_date	string,");
		createSql.append("year_name	string,");
		createSql.append("semiyear_name	string,");
		createSql.append("quarter_name	string,");
		createSql.append("month_name	string,");
		createSql.append("tendays_name	string,");
		createSql.append("week_name		string,");
		createSql.append("week_day_name		string");
		createSql.append(")");
		createSql.append(" DISTRIBUTE BY HASH INTO 4 BUCKETS");
		createSql.append(" TBLPROPERTIES(");
		createSql
				.append(" 'storage_handler' = 'com.cloudera.kudu.hive.KuduStorageHandler',");
		createSql.append(" 'kudu.table_name' = '@tableName',");
		createSql.append(" 'kudu.master_addresses' = '@kuduAddress',");
		createSql.append(" 'kudu.key_columns' = 'id',");
		createSql.append(" 'kudu.num_tablet_replicas' = '1'");
		createSql.append(");");
		String assembledSql = createSql.toString()
				.replace("@tableName", tableName)
				.replace("@kuduAddress", kuduAddress);
		return assembledSql;
	}

	private String assembledHiveTableSql(String tableName) {
		StringBuffer createSql = new StringBuffer();
		createSql.append("create table if not exists @tableName(");
		createSql.append("id	string,");
		createSql.append("full_date	string,");
		createSql.append("year_name	string,");
		createSql.append("semiyear_name	string,");
		createSql.append("quarter_name	string,");
		createSql.append("month_name	string,");
		createSql.append("tendays_name	string,");
		createSql.append("week_name		string,");
		createSql.append("week_day_name		string");
		createSql.append(")clustered by(id) into 3 buckets");
		// createSql
		// .append(" ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n'");
		String assembledSql = createSql.toString().replace("@tableName",
				tableName);
		return assembledSql;
	}

	private int execKuduInsertSql(String dbName, String tableName,
			String beginDate, String endDate) {
		Date _beginDate = DateUtils.string2Date(beginDate, "yyyy-MM-dd");
		Date _endDate = DateUtils.string2Date(endDate, "yyyy-MM-dd");
		List<String> resultList = new ArrayList<String>();
		int totalSize = 0;
		while (DateUtils.compareForDay(_beginDate, _endDate) <= 0) {
			StringBuffer insertSBSql = new StringBuffer();
			insertSBSql
					.append("insert into table @tableName(id,full_date,year_name,semiyear_name,quarter_name,month_name,tendays_name,week_name,week_day_name) ");
			insertSBSql
					.append("select '@id','@full_date','@year_name','@semiyear_name','@quarter_name','@month_name','@tendays_name','@week_name','@week_day_name' ;");
			String insertSql = assembledInsertSql(insertSBSql.toString(),
					tableName, _beginDate);
			_beginDate = DateUtils.addDay(_beginDate, 1);
			resultList.add(insertSql);
			totalSize++;
		}
		impalaClientService.execSqls(dbName, resultList);
		return totalSize;
	}

	private int execHiveInsertSql(String dbName, String tableName,
			String beginDate, String endDate) {
		Date _beginDate = DateUtils.string2Date(beginDate, "yyyy-MM-dd");
		Date _endDate = DateUtils.string2Date(endDate, "yyyy-MM-dd");
		List<String> resultList = new ArrayList<String>();
		int totalSize = 0;
		StringBuffer insertSBSql = new StringBuffer();
		insertSBSql
				.append("insert into table @tableName(id,full_date,year_name,semiyear_name,quarter_name,month_name,tendays_name,week_name,week_day_name) values");
		while (DateUtils.compareForDay(_beginDate, _endDate) <= 0) {
			String valuesSBSql = StringUtils.EMPTY;
			if (DateUtils.compareForDay(_beginDate, _endDate) == 0)
				valuesSBSql = "('@id','@full_date','@year_name','@semiyear_name','@quarter_name','@month_name','@tendays_name','@week_name','@week_day_name' )";
			else
				valuesSBSql = "('@id','@full_date','@year_name','@semiyear_name','@quarter_name','@month_name','@tendays_name','@week_name','@week_day_name' ),";
			insertSBSql.append(assembledInsertSql(valuesSBSql, tableName,
					_beginDate));
			_beginDate = DateUtils.addDay(_beginDate, 1);
			totalSize++;
		}
		String assembledSql = insertSBSql.toString().replace("@tableName",
				tableName);
		resultList.add(assembledSql);
		System.out.println(assembledSql);
		hiveClientService.execSqls(dbName, resultList);
		return totalSize;
	}

	private String assembledInsertSql(String initSql, String tableName,
			Date currentDate) {
		String insertSql = initSql;
		Date _beginDate = currentDate;
		Calendar c = Calendar.getInstance();
		c.setTime(_beginDate);
		// tableName
		insertSql = insertSql.replace("@tableName", tableName);
		// id
		insertSql = insertSql.replace("@id", UuidUtils.generateUuid());
		// full_date
		insertSql = insertSql.replace("@full_date",
				DateUtils.date2String(_beginDate, "yyyy-MM-dd"));
		// year_name
		insertSql = insertSql.replace("@year_name",
				String.valueOf(c.get(Calendar.YEAR)));
		// semiyear_name
		String semiyearName = StringUtils.EMPTY;
		if (c.get(Calendar.MONTH) <= 6)
			semiyearName = "上半年";
		else
			semiyearName = "下半年";
		insertSql = insertSql.replace("@semiyear_name", semiyearName);
		// quarter_name
		String season = StringUtils.EMPTY;
		switch (c.get(Calendar.MONTH)) {
		case Calendar.JANUARY:
		case Calendar.FEBRUARY:
		case Calendar.MARCH:
			season = "一季度";
			break;
		case Calendar.APRIL:
		case Calendar.MAY:
		case Calendar.JUNE:
			season = "二季度";
			break;
		case Calendar.JULY:
		case Calendar.AUGUST:
		case Calendar.SEPTEMBER:
			season = "三季度";
			break;
		case Calendar.OCTOBER:
		case Calendar.NOVEMBER:
		case Calendar.DECEMBER:
			season = "四季度";
			break;
		default:
			break;
		}
		insertSql = insertSql.replace("@quarter_name", season);
		// @month_name
		String monthName = StringUtils.EMPTY;
		switch (c.get(Calendar.MONTH)) {
		case Calendar.JANUARY:
			monthName = "一月";
			break;
		case Calendar.FEBRUARY:
			monthName = "二月";
			break;
		case Calendar.MARCH:
			monthName = "三月";
			break;
		case Calendar.APRIL:
			monthName = "四月";
			break;
		case Calendar.MAY:
			monthName = "五月";
			break;
		case Calendar.JUNE:
			monthName = "六月";
			break;
		case Calendar.JULY:
			monthName = "七月";
			break;
		case Calendar.AUGUST:
			monthName = "八月";
			break;
		case Calendar.SEPTEMBER:
			monthName = "九月";
			break;
		case Calendar.OCTOBER:
			monthName = "十月";
			break;
		case Calendar.NOVEMBER:
			monthName = "十一月";
			break;
		case Calendar.DECEMBER:
			monthName = "十二月";
			break;
		default:
			break;
		}
		insertSql = insertSql.replace("@month_name", monthName);
		// @tendays_name
		String tendaysName = StringUtils.EMPTY;
		if (DateUtils.getDayOfMonth(_beginDate) <= 10)
			tendaysName = "上旬";
		else if (DateUtils.getDayOfMonth(_beginDate) > 20)
			tendaysName = "下旬";
		else
			tendaysName = "中旬";
		insertSql = insertSql.replace("@tendays_name", tendaysName);
		// @week_name
		insertSql = insertSql.replace("@week_name",
				"第" + DateUtils.getWeek(_beginDate, false) + "周");
		// @week_day_name
		insertSql = insertSql.replace("@week_day_name",
				DateUtils.getWeekOfDate(_beginDate));
		return insertSql;
	}

	public static void main(String[] args) {
		// List<String> list = assembledInsertSql("test", "2018-11-01",
		// "2018-11-05");
		// for (String sql : list) {
		// System.out.println(sql);
		// }
	}
}
