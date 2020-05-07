package net.zdsoft.bigdata.data.action;

import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.entity.DsFile;
import net.zdsoft.bigdata.data.entity.NosqlDatabase;
import net.zdsoft.bigdata.data.service.DsFileService;
import net.zdsoft.bigdata.data.service.NosqlDatabaseService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/bigdata/nosqlDatasource")
public class NosqlDataSourceController extends BigdataBaseAction {

	private static final Logger logger = LoggerFactory
			.getLogger(NosqlDataSourceController.class);
	@Autowired
	private NosqlDatabaseService nosqlDatabaseService;
	@Autowired
	private SysOptionRemoteService sysOptionRemoteService;
	@Autowired
	private DsFileService dsFileService;

	@RequestMapping("/edit")
	public String edit(String id, ModelMap model, String type) {
		NosqlDatabase database = StringUtils.isNotBlank(id) ? nosqlDatabaseService
				.findOne(id) : new NosqlDatabase();
		if (StringUtils.isBlank(id)) {
			database.setType(type);
		}
		model.addAttribute("database", database);
		model.addAttribute("databaseName",
				DatabaseType.parse(database.getType()).getThumbnail());
		if ("13".equals(database.getType())) {
			return "/bigdata/datasource/hbaseEdit.ftl";
		}
		return "/bigdata/datasource/nosqlDbEdit.ftl";
	}

	@RequestMapping("/save")
	@ResponseBody
	public String save(NosqlDatabase database, String projectName) {
		try {
			database.setUnitId(getLoginInfo().getUnitId());
			DatabaseType db = DatabaseType.parse(database.getType());
			if (db == DatabaseType.HADOOP_HIVE || db == DatabaseType.SPARK) {
				// 判断认证方式
				String authWay = database.getAuthWay();
				if (StringUtils.isBlank(authWay)) {
					database.setServerPrincipal(null);
					database.setClientPrincipal(null);
					database.setKeytabFile(null);
					database.setUserName(null);
					database.setPassword(null);
				} else if ("kerberos".equals(authWay)) {
					database.setUserName(null);
					database.setPassword(null);
				} else if ("username".equals(authWay)) {
					database.setServerPrincipal(null);
					database.setClientPrincipal(null);
					database.setKeytabFile(null);
					database.setPassword(null);
				} else {
					database.setServerPrincipal(null);
					database.setClientPrincipal(null);
					database.setKeytabFile(null);
				}
			} else if (db == DatabaseType.KYLIN) {
				database.setDbName(projectName);
			} else if (db == DatabaseType.REDIS) {
				// database.setDbName(projectName);
			} else {
				// database.setDbName(projectName);
			}
			database.setThumbnail(db.getThumbnail());
			nosqlDatabaseService.saveNosqlDatabase(database);
			return success("保存数据库成功!");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}

	@ResponseBody
	@ControllerInfo("删除database")
	@RequestMapping("/delete")
	public String deleteDatabase(String id) {
		try {
			nosqlDatabaseService.delete(id);
			return success("删除数据库成功!");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}

	@ResponseBody
	@ControllerInfo("测试database")
	@RequestMapping("/test")
	public String testDatabase(NosqlDatabase database, String projectName,
			HttpServletRequest request) {
		try {
			if (StringUtils.isBlank(database.getDbName())
					&& StringUtils.isNotBlank(projectName)) {
				database.setDbName(projectName);
			}
			if (StringUtils.isNotBlank(database.getKeytabFile())) {
				database.setKeytabFile(sysOptionRemoteService
						.getFileUrl(request.getServerName())
						+ "/"
						+ database.getKeytabFile());
			}
			boolean checked = checkNosqlDatabase(database);
			if (checked) {
				return success("数据库连接成功!");
			} else {
				return error("数据库连接失败,请检查配置!");
			}
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}

	private boolean checkNosqlDatabase(NosqlDatabase database) {
		DatabaseType db = DatabaseType.parse(database.getType());
		if (db == DatabaseType.HADOOP_HIVE) {
			return checkHiveDatabase(database);
		} else if (db == DatabaseType.KYLIN) {
			return checkKylinDatabase(database);
		} else if (db == DatabaseType.IMPALA) {
			return checkImpalaDatabase(database);
		} else if (db == DatabaseType.SPARK) {
			return checkHiveDatabase(database);
		} else if (db == DatabaseType.REDIS) {
			return checkRedisDatabase(database);
		} else if (db == DatabaseType.HBASE) {
			return checkHbaseDatabase(database);
		}
		return false;
	}

	private boolean checkHiveDatabase(NosqlDatabase database) {
		// 判断认证方式
		String authWay = database.getAuthWay();
		String url = new StringBuilder("jdbc:hive2://")
				.append(database.getDomain()).append(":")
				.append(database.getPort()).append("/")
				.append(database.getDbName()).toString();

		if ("zookeeper".equals(database.getConnectMode())) {
			url = url + ";serviceDiscoveryMode=zooKeeper;zooKeeperNamespace="
					+ database.getNameSpace();
		}
		Connection connection = null;
		try {
			Class.forName(DatabaseType.HADOOP_HIVE.getDriverName());
			if (StringUtils.isBlank(authWay)) {
				// jdbc:hive2://192.168.0.202:10000/bigdata
				connection = getConnection(url, null, null);
			} else if ("kerberos".equals(authWay)) {
				Configuration conf = new Configuration();
				conf.set("hadoop.security.authentication", "Kerberos");
				url = url + ";principal=" + database.getServerPrincipal();
				UserGroupInformation.setConfiguration(conf);
				UserGroupInformation
						.loginUserFromKeytab(database.getClientPrincipal(),
								database.getKeytabFile());
				connection = getConnection(url, database.getUserName(),
						database.getPassword());
			} else if ("username".equals(authWay)) {
				connection = getConnection(url, database.getUserName(), null);
			} else {
				connection = getConnection(url, database.getUserName(),
						database.getPassword());
			}
			return true;
		} catch (ClassNotFoundException e) {
			logger.error("driver class not found", e);
			return false;
		} catch (SQLException e) {
			logger.error("get connection fail", e);
			return false;
		} catch (Exception e) {
			logger.error("keylab文件未找到", e);
			return false;
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("connection close fail", e);
					return false;
				}
			}
		}
	}

	private boolean checkImpalaDatabase(NosqlDatabase database) {
		// 判断认证方式
		String authWay = database.getAuthWay();
		String url = new StringBuilder("jdbc:impala://")
				.append(database.getDomain()).append(":")
				.append(database.getPort()).append("/")
				.append(database.getDbName()).toString();

		url = url + "/;auth=noSasl";
		Connection connection = null;
		try {
			Class.forName(DatabaseType.IMPALA.getDriverName());
			if (StringUtils.isBlank(authWay)) {
				connection = getConnection(url, null, null);
			} else if ("kerberos".equals(authWay)) {
				Configuration conf = new Configuration();
				conf.set("hadoop.security.authentication", "Kerberos");
				url = url + ";principal=" + database.getServerPrincipal();
				UserGroupInformation.setConfiguration(conf);
				UserGroupInformation
						.loginUserFromKeytab(database.getClientPrincipal(),
								database.getKeytabFile());
				connection = getConnection(url, database.getUserName(),
						database.getPassword());
			} else if ("username".equals(authWay)) {
				connection = getConnection(url, database.getUserName(), null);
			} else {
				connection = getConnection(url, database.getUserName(),
						database.getPassword());
			}
			return true;
		} catch (ClassNotFoundException e) {
			logger.error("driver class not found", e);
			return false;
		} catch (SQLException e) {
			logger.error("get connection fail", e);
			return false;
		} catch (Exception e) {
			logger.error("keylab文件未找到", e);
			return false;
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("connection close fail", e);
					return false;
				}
			}
		}
	}

	private boolean checkKylinDatabase(NosqlDatabase database) {
		String url = new StringBuilder("jdbc:kylin://")
				.append(database.getDomain()).append(":")
				.append(database.getPort()).append("/")
				.append(database.getDbName()).toString();
		try {
			Driver driver = (Driver) Class.forName(
					DatabaseType.KYLIN.getDriverName()).newInstance();
			Properties properties = new Properties();
			properties.put("user", database.getUserName());
			properties.put("password", database.getPassword());
			Connection connection = null;
			try {
				connection = driver.connect(url, properties);
			} catch (SQLException e) {
				logger.error("get connection fail", e);
				return false;
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
			return true;
		} catch (Exception e) {
			logger.error("driver class not found", e);
			return false;
		}
	}

	public boolean checkRedisDatabase(NosqlDatabase database) {
		JedisPool jedisPool = null;
		Jedis jedis = null;
		try {
			// 创建jedis池配置实例
			JedisPoolConfig config = new JedisPoolConfig();
			// 设置池配置项值
			config.setMaxTotal(1000);
			config.setMaxIdle(300);
			config.setMaxWaitMillis(1000);
			config.setTestOnBorrow(true);
			// 根据配置实例化jedis池
			jedisPool = new JedisPool(config, database.getDomain(),
					database.getPort());
			jedis = jedisPool.getResource();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (jedisPool != null)
				jedisPool.close();
			if (jedis != null)
				jedis.close();
		}
	}

	public boolean checkHbaseDatabase(NosqlDatabase database) {
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", database.getDomain());
		conf.set("hbase.zookeeper.property.clientPort", String.valueOf(database.getPort()));
		conf.set("hbase.master", database.getNameSpace());
		try {
			org.apache.hadoop.hbase.client.Connection connection = ConnectionFactory.createConnection(conf);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private Connection getConnection(String url, String username,
			String password) throws SQLException {
		if (StringUtils.isBlank(username)) {
			return DriverManager.getConnection(url);
		}
		if (StringUtils.isBlank(password)) {
			return DriverManager.getConnection(url, username, null);
		}
		return DriverManager.getConnection(url, username, password);
	}

	@RequestMapping("/editFileDB")
	public String editFileDB(String id, ModelMap model, String type) {
		DsFile dsFile = StringUtils.isNotBlank(id) ? dsFileService.findOne(id)
				: new DsFile();
		if (StringUtils.isBlank(id)) {
			dsFile.setType(type);
		}
		model.addAttribute("database", dsFile);
		model.addAttribute("databaseName", DatabaseType.parse(dsFile.getType())
				.getThumbnail());
		return DatabaseType.CSV.getType().equals(dsFile.getType()) ? "/bigdata/datasource/csvEdit.ftl"
				: "/bigdata/datasource/excelEdit.ftl";
	}

	@RequestMapping("/previewCsvFile")
	public String previewCsvFile(HttpServletRequest request, ModelMap map,
			String id) {
		InputStream in = null;
		MultipartFile file = StorageFileUtils.getFile(request);
		try {
			if (file == null && StringUtils.isNotBlank(id)) {
				DsFile dsFile = dsFileService.findOne(id);
				String filePath = sysOptionRemoteService.getFileUrl(request
						.getServerName()) + "/" + dsFile.getFilePath();
				URL url = new URL(encode(filePath, "utf-8"));
				in = url.openStream();
			} else {
				in = file.getInputStream();
			}
			List<String[]> list = ExcelUtils.readCvsFromStream(in);
			map.put("title", getTitle(request, list));
			map.put("list", list);
			map.put("type", "csv");
			return "/bigdata/datasource/previewData.ftl";
		} catch (IOException e) {
			map.put("errorMsg", e.getMessage());
			return "/bigdata/v3/common/error.ftl";
		}
	}

	@RequestMapping("/previewExcelFile")
	public String previewExcelFile(HttpServletRequest request, ModelMap map,
			String id, String tabKey) {
		InputStream in = null;
		MultipartFile file = StorageFileUtils.getFile(request);
		String suffix = "";
		try {
			if ((file == null || StringUtils
					.isBlank(file.getOriginalFilename()))
					&& StringUtils.isNotBlank(id)) {
				DsFile dsFile = dsFileService.findOne(id);
				String filePath = sysOptionRemoteService.getFileUrl(request
						.getServerName()) + "/" + dsFile.getFilePath();
				URL url = new URL(encode(filePath, "utf-8"));
				suffix = dsFile.getFileName().substring(
						dsFile.getFileName().lastIndexOf(".") + 1);
				in = url.openStream();
			} else {
				in = file.getInputStream();
				suffix = file.getOriginalFilename().substring(
						file.getOriginalFilename().lastIndexOf(".") + 1);
			}
			Map<String, List<String[]>> excelMap = ExcelUtils
					.readExcelFromStream(in, suffix, 0);
			Set<String> keySet = excelMap.keySet();
			if (keySet.size() > 0) {
				if (StringUtils.isBlank(tabKey)) {
					tabKey = keySet.iterator().next();
				}
				List<String[]> list = excelMap.get(tabKey);
				map.put("title", getTitle(request, list));
				map.put("list", list);
			}
			map.put("tabKey", tabKey);
			map.put("type", "excel");
			map.put("excelTab", keySet);
			return "/bigdata/datasource/previewData.ftl";
		} catch (IOException e) {
			map.put("errorMsg", e.getMessage());
			return "/bigdata/v3/common/error.ftl";
		}
	}

	private String[] getTitle(HttpServletRequest request, List<String[]> list) {
		if (list.size() < 1) {
			return new String[] {};
		}
		String headType = request.getParameter("headType");
		if (headType != null && "2".equals(headType)) {
			int columnLength = list.get(0).length;
			String[] title = new String[columnLength];
			for (int i = 0; i < columnLength; i++) {
				title[i] = "列名" + (i + 1);
			}
			return title;
		} else {
			String[] title = list.get(0);
			list.remove(0);
			return title;
		}
	}

	@RequestMapping("/saveFile")
	@ResponseBody
	public String saveFile(HttpServletRequest request, String type) {
		DatabaseType databaseType = "csv".equals(type) ? DatabaseType.CSV
				: DatabaseType.EXCEL;
		MultipartFile file = StorageFileUtils.getFile(request);
		String name = request.getParameter("name");
		String remark = request.getParameter("remark");
		String headType = request.getParameter("headType");
		String id = request.getParameter("id");
		DsFile dsFile = new DsFile();
		dsFile.setType(databaseType.getType());
		dsFile.setHeadType((short) ("1".equals(headType) ? 1 : 2));
		dsFile.setName(name);
		dsFile.setId(id);
		dsFile.setRemark(remark);
		dsFile.setUnitId(getLoginInfo().getUnitId());
		dsFile.setThumbnail(databaseType.getThumbnail());
		try {
			dsFileService.saveDsFile(dsFile, file);
		} catch (IOException e) {
			return error("保存数据源失败【" + e.getMessage() + "】");
		}
		return success("保存成功");
	}

	@ResponseBody
	@RequestMapping("/deleteFileDB")
	public String deleteFileDB(String id) {
		try {
			dsFileService.delete(id);
			return success("删除数据库成功!");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}

	private static String zhPattern = "[\\u4e00-\\u9fa5]";

	/**
	 * 替换字符串卷
	 *
	 * @param str
	 *            被替换的字符串
	 * @param charset
	 *            字符集
	 * @return 替换好的
	 */
	private String encode(String str, String charset)
			throws UnsupportedEncodingException {
		Pattern p = Pattern.compile(zhPattern);
		Matcher m = p.matcher(str);
		StringBuffer b = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(b, URLEncoder.encode(m.group(0), charset));
		}
		m.appendTail(b);
		return b.toString();
	}

}
