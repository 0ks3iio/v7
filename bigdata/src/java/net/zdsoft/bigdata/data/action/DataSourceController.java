package net.zdsoft.bigdata.data.action;

import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.entity.*;
import net.zdsoft.bigdata.data.manager.api.IDataSourceService;
import net.zdsoft.bigdata.data.manager.datasource.IDataSourceUtils;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/bigdata/datasource")
public class DataSourceController extends BigdataBaseAction {

	@Autowired
	private DatabaseService databaseService;

	@Autowired
	private ApiService apiService;

	@Autowired
	IDataSourceService iDataSourceService;

	@Autowired
	ReportTermService reportTermService;

	@Autowired
	private NosqlDatabaseService nosqlDatabaseService;

	@Autowired
	private DsFileService dsFileService;
	
	@Autowired
	private MetadataService metadataService;

	@Resource
	private BigLogService bigLogService;

	@RequestMapping("/index")
	public String index(ModelMap map) {
		// 查询所有数据库
		map.addAttribute("typeList", DatabaseType.values());
		return "/bigdata/datasource/dsIndex.ftl";
	}

	@ControllerInfo("进入列表页面")
	@RequestMapping("/list")
	public String databaseList(Integer dsType, HttpServletRequest request,
			ModelMap map) {
		String unitId = getLoginInfo().getUnitId();
		if (DataSourceType.DB.getValue() == dsType) {
			List<Database> databaseList = databaseService.findDatabasesByUnitId(unitId);
			List<NosqlDatabase> nosqlDatabaseList = nosqlDatabaseService.findNosqlDatabasesByUnitId(unitId);
			List<DsFile> dsFileList = dsFileService.findDsFileByUnitId(unitId);
			List<AbstractDatabase> dbList = new ArrayList();
			dbList.addAll(databaseList);
			dbList.addAll(nosqlDatabaseList);
			dbList.addAll(dsFileList);
			Collections.sort(dbList, new Comparator<AbstractDatabase>() {
				@Override
				public int compare(AbstractDatabase o1, AbstractDatabase o2) {
					return o2.getModifyTime().compareTo(o1.getModifyTime());
				}
			});
			map.put("databaseList", dbList);
			return "/bigdata/datasource/dbList.ftl";
		} else if (DataSourceType.API.getValue() == dsType) {
			List<Api> apiList = apiService.findApisByUnitId(unitId);
			map.put("apiList", apiList);
			return "/bigdata/datasource/apiList.ftl";
		} else {
			return "/bigdata/v3/common/error.ftl";
		}
	}

	@RequestMapping("/getDatabaseList")
	@ResponseBody
	public Response getDatabaseList(String type) {
		String unitId = getLoginInfo().getUnitId();
		if ("mysql".equals(type)) {
			List<Database> databaseList = databaseService.findListBy(new String[]{"type", "unitId"}, new String[]{DatabaseType.MYSQL.getType(), unitId});
			return Response.ok().data(databaseList).build();
		} else if ("impala".equals(type)) {
			List<NosqlDatabase> databaseList = nosqlDatabaseService.findNosqlDatabasesByUnitIdAndType(unitId, DatabaseType.IMPALA.getType());
			return Response.ok().data(databaseList).build();
		} else if ("kylin".equals(type)) {
			List<NosqlDatabase> databaseList = nosqlDatabaseService.findNosqlDatabasesByUnitIdAndType(unitId, DatabaseType.KYLIN.getType());
			return Response.ok().data(databaseList).build();
		}
		return Response.ok().build();
	}

	/**
	 * 加载数据源列表和{@link #databaseList(Integer, HttpServletRequest, ModelMap)} 一致
	 * 这里需要的是JSON数据而不是html
	 */
	@ResponseBody
	@RequestMapping(value = "{type}", method = RequestMethod.GET)
	public Object datasourceList(@PathVariable("type") int type) {

		if (DataSourceType.DB.getValue() == type) {
			List<Database> databases = databaseService
					.findDatabasesByUnitId(getLoginInfo().getUnitId());
			return Response.ok().data(databases).build();
		} else if (DataSourceType.API.getValue() == type) {
			List<Api> apis = apiService.findApisByUnitId(getLoginInfo()
					.getUnitId());
			return Response.ok().data(apis).build();
		}
		return Response.error().message("不支持的数据源类型");
	}

	@ControllerInfo("进入database明细页面")
	@RequestMapping("/db/edit")
	public String editDatabase(String id, HttpServletRequest request,
			ModelMap map, String type) {
		Database database = new Database();
		database.setType(type);
		if (StringUtils.isNotBlank(id)) {
			database = databaseService.findOne(id);
		}
		map.put("database", database);
		map.put("databaseName", DatabaseType.parse(database.getType()).getThumbnail());
		return "/bigdata/datasource/dbEdit.ftl";
	}

	@ControllerInfo("进入api明细页面")
	@RequestMapping("/api/edit")
	public String editApi(String id, HttpServletRequest request, ModelMap map) {
		Api api = new Api();
		if (StringUtils.isNotBlank(id)) {
			api = apiService.findOne(id);
		}
		map.put("api", api);
		return "/bigdata/datasource/apiEdit.ftl";
	}

	@ResponseBody
	@ControllerInfo("测试database")
	@RequestMapping("/db/test")
	public String testDatabase(Database database) {
		try {
			boolean checked = iDataSourceService
					.checkDataSource(IDataSourceUtils
							.createLazyIDataSource(database));
			if (checked) {
				return success("数据库连接成功!");
			} else {
				return error("数据库连接失败,请检查配置!");
			}
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}

	@ResponseBody
	@ControllerInfo("保存database")
	@RequestMapping("/db/save")
	public String saveDatabase(Database database) {
		try {
			database.setUnitId(getLoginInfo().getUnitId());
			databaseService.saveDatabase(database);
			// 在更新database的时候要更新IDatasource
			iDataSourceService.updateIDataSource(database.getId(),
					DataSourceType.DB);
			return success("保存数据库成功!");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}

	@ResponseBody
	@ControllerInfo("保存api")
	@RequestMapping("/api/save")
	public String saveApi(Api api) {
		try {
			api.setUnitId(getLoginInfo().getUnitId());
			apiService.saveApi(api);
			return success("保存API成功");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}

	@ResponseBody
	@ControllerInfo("删除database")
	@RequestMapping("/db/delete")
	public String deleteDatabase(String id) {
		try {
			boolean isused = iDataSourceService.isUsed(id, DataSourceType.DB);
			if (isused) {
				Database database = databaseService.findOne(id);
				return error(database.getName() + "被引用，不能删除。");
			}
			int count = reportTermService.countBySourceId(
					DataSourceType.DB.getValue(), id);
			if (count > 0) {
				Database database = databaseService.findOne(id);
				return error(database.getName() + "被引用，不能删除。");
			}
			
			count=metadataService.getCountByDbId(id);
			if (count > 0) {
				Database database = databaseService.findOne(id);
				return error(database.getName() + "被元数据引用，不能删除。");
			}
			Database oldDatabase = databaseService.findOne(id);
			databaseService.deleteDatabase(id);
			//业务日志埋点  删除
			LogDto logDto=new LogDto();
			logDto.setBizCode("delete-database");
			logDto.setDescription("数据源 "+oldDatabase.getName());
			logDto.setBizName("数据源管理");
			logDto.setOldData(oldDatabase);
			bigLogService.deleteLog(logDto);

			return success("删除数据库成功!");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}

	@ResponseBody
	@ControllerInfo("删除api")
	@RequestMapping("/api/delete")
	public String deleteApi(String id) {

		try {
			boolean isused = iDataSourceService.isUsed(id, DataSourceType.API);
			if (isused) {
				Api api = apiService.findOne(id);
				return error(api.getName() + "被引用，不能删除。");
			}

			int count = reportTermService.countBySourceId(
					DataSourceType.API.getValue(), id);
			if (count > 0) {
				Api api = apiService.findOne(id);
				return error(api.getName() + "被引用，不能删除。");
			}
			Api oldApi = apiService.findOne(id);
			apiService.deleteApi(id);
			//业务日志埋点  删除
			LogDto logDto=new LogDto();
			logDto.setBizCode("delete-api");
			logDto.setDescription("API "+oldApi.getName());
			logDto.setBizName("数据源管理");
			logDto.setOldData(oldApi);
			bigLogService.deleteLog(logDto);

			return success("删除API成功");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}

	@ControllerInfo("进入列表页面")
	@RequestMapping("/apiSearch")
	public String apiSearch(String name, String description, ModelMap map) {
		List<Api> apiList = apiService.findApis(getLoginInfo().getUnitId(), name, description);
		map.put("apiList", apiList);
		map.put("name", name);
		map.put("description", description);
		return "/bigdata/datasource/apiList.ftl";
	}

}
