package net.zdsoft.bigdata.data.action;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.TreeRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.bigdata.daq.data.component.BizOperationLogCollector;
import net.zdsoft.bigdata.daq.data.entity.BizOperationLog;
import net.zdsoft.bigdata.data.entity.DataModel;
import net.zdsoft.bigdata.data.entity.ModelDataset;
import net.zdsoft.bigdata.data.entity.ModelDatasetUser;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.DataModelService;
import net.zdsoft.bigdata.data.service.ModelDatasetService;
import net.zdsoft.bigdata.data.service.ModelDatasetUserService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by wangdongdong on 2018/12/6 10:09.
 */
@Controller
@RequestMapping("/bigdata/model/user")
public class DataModelUserController extends BigdataBaseAction {

	@Resource
	private DataModelService dataModelService;
	@Resource
	private ModelDatasetService modelDatasetService;
	@Resource
	private ModelDatasetUserService modelDatasetUserService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private TreeRemoteService treeRemoteService;

	/**
	 * 数据模型用户集管理页面
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/list")
	public String list(String modelId, ModelMap map) {

		DataModel dataModel = dataModelService.findOne(modelId);
		List<ModelDataset> modelDatasets = modelDatasetService.getByModelId(
				modelId, getLoginInfo().getUnitId());

		modelDatasets.forEach(e -> {
			StringBuilder orderUserBuilder = new StringBuilder();
			String[] userIds = modelDatasetUserService
					.findListBy("dsId", e.getId()).stream()
					.map(ModelDatasetUser::getUserId).toArray(String[]::new);
			userRemoteService
					.findListObjectByIds(userIds)
					.parallelStream()
					.map(User::getRealName)
					.iterator()
					.forEachRemaining(
							name -> orderUserBuilder.append(name).append("、"));
			if (orderUserBuilder.length() == 0) {
				e.setOrderUsers(StringUtils.EMPTY);
			} else {
				e.setOrderUsers(orderUserBuilder.substring(0,
						orderUserBuilder.length() - 1));
			}
		});

		map.put("dataModel", dataModel);
		map.put("modelDatasets", modelDatasets);

		return "/bigdata/dataModel/userDataset.ftl";
	}

	/**
	 * 保存数据模型
	 *
	 * @return
	 */
	@RequestMapping("/editUserDataset")
	public String editUserDataset(String id, String modelId, ModelMap map) {
		ModelDataset dataset = StringUtils.isNotBlank(id) ? modelDatasetService
				.findOne(id) : new ModelDataset();
		dataset.setModelId(modelId);
		map.put("dataset", dataset);
		return "/bigdata/dataModel/userDatasetAdd.ftl";
	}

	/**
	 * 保存数据模型
	 *
	 * @return
	 */
	@RequestMapping("/saveUserDataset")
	@ResponseBody
	public Response saveUserDataset(ModelDataset modelDataset) {
		modelDataset.setUnitId(getLoginInfo().getUnitId());
		try {
			String id = modelDataset.getId();
			modelDatasetService.saveModelDataset(modelDataset);
			// 业务日志埋点
			BizOperationLog bizLog = new BizOperationLog();
			if (net.zdsoft.framework.utils.StringUtils.isBlank(id)) {
				bizLog.setLogType(BizOperationLog.LOG_TYPE_INSERT);
				bizLog.setBizCode("insert-model-dataset");
				bizLog.setDescription("新增数据集" + modelDataset.getDsName());
				bizLog.setNewData(JSON.toJSONString(modelDataset));
			} else {
				bizLog.setLogType(BizOperationLog.LOG_TYPE_UPDATE);
				bizLog.setBizCode("update-model-dataset");
				bizLog.setDescription("修改数据集" + modelDataset.getDsName());
				ModelDataset oldDataSet = modelDatasetService.findOne(id);
				bizLog.setOldData(JSON.toJSONString(oldDataSet));
				bizLog.setNewData(JSON.toJSONString(modelDataset));
			}
			bizLog.setBizName("数据模型管理");
			bizLog.setSubSystem("大数据管理");
			bizLog.setOperator(getLoginInfo().getRealName() + "("
					+ getLoginInfo().getUserName() + ")");
			bizLog.setOperationTime(new Date());
			BizOperationLogCollector.submitBizOperationLog(bizLog);
			return Response.ok().message("保存成功").data(modelDataset).build();
		} catch (BigDataBusinessException e) {
			return Response.error().message(e.getMessage()).data(modelDataset)
					.build();
		}
	}

	/**
	 * 删除用户数据集
	 *
	 * @return
	 */
	@RequestMapping("/deleteUserDataset")
	@ResponseBody
	public Response deleteDataModel(String id) {
		ModelDataset dataSet = modelDatasetService.findOne(id);
		modelDatasetService.deleteDataModel(id);
		// 业务日志埋点
		BizOperationLog bizLog = new BizOperationLog();
		bizLog.setLogType(BizOperationLog.LOG_TYPE_DELETE);
		bizLog.setBizCode("delete-model-dataset");
		bizLog.setDescription("删除数据集" + dataSet.getDsName());
		bizLog.setBizName("数据模型管理");
		bizLog.setSubSystem("大数据管理");
		bizLog.setOperator(getLoginInfo().getRealName() + "("
				+ getLoginInfo().getUserName() + ")");
		bizLog.setOperationTime(new Date());
		bizLog.setOldData(JSON.toJSONString(dataSet));
		BizOperationLogCollector.submitBizOperationLog(bizLog);
		return Response.ok().data("删除成功!").build();
	}

	@ResponseBody
	@RequestMapping(value = "/getAllUser", method = RequestMethod.GET)
	public Response doGetCurrentTeacherTree(String modelDatasetId) {
		String currentUnitId = getLoginInfo().getUnitId();
		List<ModelDatasetUser> users = modelDatasetUserService.findListBy(
				"dsId", modelDatasetId);
		// 只显示本单位的授权
		List<User> orderUsers = userRemoteService.findListObjectByIds(users
				.stream().map(ModelDatasetUser::getUserId)
				.toArray(String[]::new));

		JSONArray array = treeRemoteService
				.deptTeacherForUnitInsetTree(getLoginInfo().getUnitId());
		for (Object t : array) {
			if (t instanceof JSONObject) {
				JSONObject treeNode = (JSONObject) t;
				if ("teacher".equals(treeNode.getString("type"))) {
					Optional<User> u = orderUsers
							.stream()
							.filter(user -> user.getOwnerId().equals(
									treeNode.getString("id"))).findFirst();
					treeNode.put("checked", u.isPresent());
					u.ifPresent(user -> treeNode.put("chkDisabled", !user
							.getUnitId().equals(currentUnitId)));
				}
			}
		}
		return Response.ok().data(JSONObject.toJSONString(array)).build();
	}

	@ResponseBody
	@RequestMapping(value = "authorization", method = RequestMethod.POST)
	public Response doBatchAuthorization(
			@RequestParam("modelId") String modelId,
			@RequestParam("modelDatasetId") String modelDatasetId,
			@RequestParam(value = "orderUsers[]", required = false) String[] orderUsers) {
		String[] orderUserIdArray = null;
		String[] orderUserNames = null;
		if (ArrayUtils.isNotEmpty(orderUsers)) {
			List<User> userList = userRemoteService.findListObjectByIn(
					"ownerId", orderUsers);
			orderUserIdArray = userList.stream().map(User::getId)
					.toArray(String[]::new);
			if (orderUserIdArray != null && orderUsers.length > 0) {
				orderUserNames = userList.stream().map(User::getUsername)
						.toArray(String[]::new);
			}
		}
		ModelDataset dataSet = modelDatasetService.findOne(modelDatasetId);
		modelDatasetService.authorization(modelId, modelDatasetId,
				orderUserIdArray, getLoginInfo().getUnitId());

		// 业务日志埋点
		BizOperationLog bizLog = new BizOperationLog();
		bizLog.setLogType(BizOperationLog.LOG_TYPE_AUTH);
		bizLog.setBizCode("model-dataset-auth");
		bizLog.setDescription(dataSet.getDsName() + "授权");
		bizLog.setBizName("数据模型管理");
		bizLog.setSubSystem("大数据管理");
		bizLog.setOperator(getLoginInfo().getRealName() + "("
				+ getLoginInfo().getUserName() + ")");
		bizLog.setOperationTime(new Date());
		bizLog.setNewData("数据集名称："
				+ dataSet.getDsName()
				+ ";授权用户:"
				+ JSON.toJSONString(orderUserNames != null ? orderUserNames
						: ""));
		BizOperationLogCollector.submitBizOperationLog(bizLog);

		return Response.ok().message("操作成功").build();
	}

	/**
	 * 获取数据集
	 * 
	 * @param modelId
	 * @return
	 */
	@RequestMapping("/getUserDataset")
	@ResponseBody
	public Response getUserDataset(String modelId) {
		String userId = getLoginInfo().getUserId();
		List<ModelDatasetUser> orderUserList = modelDatasetUserService
				.findListBy(new String[] { "modelId", "userId" }, new String[] {
						modelId, userId });
		List<ModelDataset> modelDatasets = modelDatasetService
				.findAllByIdIn(orderUserList.stream()
						.map(ModelDatasetUser::getDsId).toArray(String[]::new));
		return Response.ok().data(modelDatasets).build();
	}
}
