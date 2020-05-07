package net.zdsoft.bigdata.data.action;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.bigdata.base.entity.PropertyTag;
import net.zdsoft.bigdata.base.service.PropertyTagService;
import net.zdsoft.bigdata.daq.data.component.BizOperationLogCollector;
import net.zdsoft.bigdata.daq.data.entity.BizOperationLog;
import net.zdsoft.bigdata.data.OrderType;
import net.zdsoft.bigdata.data.biz.DataModelBiz;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.entity.*;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.metadata.entity.*;
import net.zdsoft.bigdata.metadata.service.*;
import net.zdsoft.bigdata.system.service.BgUserAuthService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.LoginInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static net.zdsoft.bigdata.data.ChartBusinessType.MODEL_REPORT;

/**
 * Created by wangdongdong on 2018/8/28 10:46.
 */
@Controller
@RequestMapping("/bigdata/model")
public class DataModelController extends BigdataBaseAction {

    public static final String TOP_DIMENSION = "00000000000000000000000000000000";

    @Resource
    private DataModelService dataModelService;
    @Resource
    private DataModelParamService dataModelParamService;
    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Resource
    private SubscribeService subscribeService;
    @Resource
    private DataModelBiz dataModelBiz;
    @Resource
    private TagService tagService;
    @Resource
    private OptionService optionService;
    @Resource
    private FolderService folderService;
    @Resource
    private BgUserAuthService bgUserAuthService;
    @Resource
    private DataModelFavoriteService dataModelFavoriteService;
    @Resource
    private TagRelationService tagRelationService;
    @Resource
    private DataModelFavoriteParamService dataModelFavoriteParamService;
    @Resource
    private FolderDetailService folderDetailService;
    @Resource
    private MetadataService metadataService;
    @Resource
    private PropertyTagService propertyTagService;
    @Resource
    private MetadataTagService metadataTagService;
    @Resource
    private MetadataRelationService metadataRelationService;
    @Resource
    private MetadataTableColumnService metadataTableColumnService;
    /**
     * 数据模型管理页面
     *
     * @param map
     * @return
     */
    @RequestMapping("/design")
    public String design(ModelMap map, String selectId) {
        String unitId = getLoginInfo().getUnitId();
        List<DataModel> dataModels = dataModelService.findAll(unitId);

        OptionDto impalaDto = optionService.getAllOptionParam("impala");
        OptionDto mysqlDto = optionService.getAllOptionParam("mysql");
        OptionDto kylinDto = optionService.getAllOptionParam("kylin");
        Iterator<DataModel> it = dataModels.iterator();
        while (it.hasNext()) {
            DataModel dataModel = it.next();

            if ("impala".equals(dataModel.getType())) {
                if (impalaDto == null || impalaDto.getStatus() == 0) {
                    it.remove();
                    continue;
                }
            }

            if ("mysql".equals(dataModel.getType())) {
                if (mysqlDto == null || mysqlDto.getStatus() == 0) {
                    it.remove();
                    continue;
                }
            }

            if ("kylin".equals(dataModel.getType())) {
                if (kylinDto == null || kylinDto.getStatus() == 0) {
                    it.remove();
                    continue;
                }
            }

            if (!dataModel.getUnitId().equals(unitId)
                    && dataModel.getOrderType() == OrderType.UNIT_ORDER_USER_AUTHORIZATION
                    .getOrderType()) {
                List<Subscribe> subscribeUnits = subscribeService
                        .getSubscribeUnits(dataModel.getId());
                if (subscribeUnits.contains(dataModel.getUnitId())) {
                    dataModel.setCanEdit(true);
                }
            }
        }
        map.put("orderTypes", OrderType.useValues());
        map.put("models", dataModels);
        map.put("currentUnitId", unitId);
        map.put("selectId", selectId);
        return "/bigdata/dataModel/modelList.ftl";
    }

    /**
     * 授权页面
     *
     * @param map
     * @param code
     * @param type
     * @return
     */
    @RequestMapping("/modelAuth")
    public String modelAuth(ModelMap map, String code, String type) {
        map.put("orderTypes", OrderType.useValues());
        String unitId = getLoginInfo().getUnitId();
        List<DataModel> dataModels = dataModelService.findListBy("code", code);
        DataModel dataModel = dataModels.size() > 0 ? dataModels.get(0)
                : new DataModel();
        if (dataModels.size() > 0
                && !dataModel.getUnitId().equals(unitId)
                && dataModel.getOrderType() == OrderType.UNIT_ORDER_USER_AUTHORIZATION
                .getOrderType()) {
            List<Subscribe> subscribeUnits = subscribeService
                    .getSubscribeUnits(dataModel.getId());
            if (subscribeUnits.contains(dataModel.getUnitId())) {
                dataModel.setCanEdit(true);
            }
        }
        map.put("dataModel", dataModels.size() > 0 ? dataModel
                : new DataModel());

        return "/bigdata/dataModel/modelAuth.ftl";
    }

    /**
     * 保存授权
     *
     * @param modelId
     * @param orderType
     * @param orderUnits
     * @param orderTeachers
     * @return
     */
    @RequestMapping("/saveAuth")
    @ResponseBody
    public Response saveAuth(
            String modelId,
            Integer orderType,
            @RequestParam(value = "orderUnit[]", required = false) String[] orderUnits,
            @RequestParam(value = "orderTeacher[]", required = false) String[] orderTeachers) {
        DataModel dataModel = dataModelService.findOne(modelId);
        if (dataModel == null) {
            return Response.error().message("该模型不存在").build();
        }
        dataModel.setOrderType(orderType);
        String[] orderUsers = orderTeachers;
        String[] orderUserNames = null;
        String[] orderUnitNames = null;
        if (!ArrayUtils.isEmpty(orderTeachers)) {
            List<User> userList = userRemoteService
                    .findListObjectByIds(orderTeachers);
            if (orderUsers != null && orderUsers.length > 0) {
                orderUserNames = userList.stream()
                        .map(User::getUsername).toArray(String[]::new);
            }
        }
        if (StringUtils.isNotBlank(modelId) && ArrayUtils.isEmpty(orderUnits)
                && ArrayUtils.isEmpty(orderTeachers)) {
            orderUnits = subscribeService.getSubscribeUnits(modelId).stream()
                    .map(e -> e.getUnitId()).toArray(String[]::new);
            orderUsers = subscribeService.getSubscribeUsers(modelId).stream()
                    .map(e -> e.getUserId()).toArray(String[]::new);
            orderUnitNames = unitRemoteService.findListObjectByIds(orderUnits).stream()
                    .map(Unit::getUnitName).toArray(String[]::new);
            orderUserNames = userRemoteService.findListObjectByIds(orderUsers).stream()
                    .map(User::getUsername).toArray(String[]::new);
        }
        dataModelService.saveDataModel(dataModel, orderUnits, orderUsers);

        // 业务日志埋点
        BizOperationLog bizLog = new BizOperationLog();
        bizLog.setLogType(BizOperationLog.LOG_TYPE_AUTH);
        bizLog.setBizCode("model-auth");
        bizLog.setDescription(dataModel.getName() + "授权");
        bizLog.setBizName("数据模型管理");
        bizLog.setSubSystem("大数据管理");
        bizLog.setOperator(getLoginInfo().getRealName() + "("
                + getLoginInfo().getUserName() + ")");
        bizLog.setOperationTime(new Date());
        bizLog.setNewData("订阅类型：" + orderType + ";订阅单位:" + JSON.toJSONString(orderUnitNames != null ? orderUnitNames : "") + ";订阅用户:" + JSON.toJSONString(orderUserNames != null ? orderUserNames : ""));
        BizOperationLogCollector.submitBizOperationLog(bizLog);
        return Response.ok().build();
    }

    /**
     * 获取数据模型指标和维度
     *
     * @param code
     * @param type
     * @return
     */
    @RequestMapping("/getModelParam")
    @ResponseBody
    public Response getModelParam(String code, String type) {

        List<DataModelParam> modelParams = dataModelParamService
                .findByCodeAndType(code, type);
        return Response.ok().data(modelParams).build();
    }

    /**
     * 获取时间维度
     *
     * @return
     */
    @RequestMapping("/timeDimensionList")
    public String timeDimensionList(ModelMap map) {
        List<DataModelParam> modelParams = dataModelParamService
                .findByCodeAndType("date", "dimension");
        map.put("modelParams", modelParams);
        return "/bigdata/dataModel/timeDimension.ftl";
    }

    /**
     * 多维报表查询页面
     *
     * @param map
     * @return
     */
    @RequestMapping("/query")
    public String query(ModelMap map, String favoriteId) {
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();
        String userId = loginInfo.getUserId();
        if (StringUtils.isNotBlank(favoriteId)) {
            DataModelFavorite dataModelFavorite = dataModelFavoriteService.findOne(favoriteId);
            // 判断模型有没有权限
            if (!dataModelBiz.hasModelPermission(unitId, userId, dataModelFavorite)) {
                map.put("errorMsg", "没有该数据模型权限!");
                return "/bigdata/v3/common/error.ftl";
            }

            List<DataModelFavoriteParam> paramList = dataModelFavoriteParamService.findListBy(new String[]{"favoriteId", "paramType"}, new String[]{favoriteId, "filterDataMap"});
            if (paramList.size() > 0) {
                map.put("filterDataMap", paramList.get(0).getParamValue());
            }
            map.put("dataModelFavorite", dataModelFavorite);
        } else {
            map.put("dataModelFavorite", new DataModelFavorite());
        }

        List<DataModel> dataModels = dataModelService.getCurrentUserDataModel(
                userId, unitId);

        OptionDto impalaDto = optionService.getAllOptionParam("impala");
        OptionDto mysqlDto = optionService.getAllOptionParam("mysql");
        OptionDto kylinDto = optionService.getAllOptionParam("kylin");
        Iterator<DataModel> it = dataModels.iterator();
        while (it.hasNext()) {
            DataModel dataModel = it.next();
            if ("impala".equals(dataModel.getType())) {
                if (impalaDto == null || impalaDto.getStatus() == 0) {
                    it.remove();
                    continue;
                }
            }

            if ("mysql".equals(dataModel.getType())) {
                if (mysqlDto == null || mysqlDto.getStatus() == 0) {
                    it.remove();
                    continue;
                }
            }

            if ("kylin".equals(dataModel.getType())) {
                if (kylinDto == null || kylinDto.getStatus() == 0) {
                    it.remove();
                    continue;
                }
            }
        }

        map.put("models", dataModels);

        map.put("isBackgroundUser", bgUserAuthService.isBackgroundUser(userId, loginInfo.getUserType()));

        return "/bigdata/dataModel/modelView.ftl";
    }

    /**
     * 保存数据模型
     *
     * @param model
     * @return
     */
    @RequestMapping("/saveModel")
    @ResponseBody
    public Response saveModel(DataModel model) {
        model.setUnitId(getLoginInfo().getUnitId());
        try {
            String id = model.getId();
            // 业务日志埋点
            BizOperationLog bizLog = new BizOperationLog();
            if (net.zdsoft.framework.utils.StringUtils.isBlank(id)) {
                bizLog.setLogType(BizOperationLog.LOG_TYPE_INSERT);
                bizLog.setBizCode("insert-model");
                bizLog.setNewData(JSON.toJSONString(model));
                bizLog.setDescription("新增数据模型" + model.getName());
                dataModelService.saveDataModel(model);
            } else {
                bizLog.setLogType(BizOperationLog.LOG_TYPE_UPDATE);
                bizLog.setBizCode("update-model");
                DataModel oldModel = dataModelService.findOne(id);
                bizLog.setOldData(JSON.toJSONString(oldModel));
                bizLog.setNewData(JSON.toJSONString(model));
                bizLog.setDescription("修改数据模型 " + oldModel.getName());
                dataModelService.saveDataModel(model);
            }
            bizLog.setBizName("数据模型管理");
            bizLog.setSubSystem("大数据管理");
            bizLog.setOperator(getLoginInfo().getRealName() + "("
                    + getLoginInfo().getUserName() + ")");
            bizLog.setOperationTime(new Date());
            BizOperationLogCollector.submitBizOperationLog(bizLog);

            return Response.ok().message("保存成功").data(model).build();
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).data(model).build();
        }
    }

    /**
     * 保存数据模型
     *
     * @return
     */
    @RequestMapping("/editModel")
    public String editModel(String id, ModelMap map) {
        DataModel model = StringUtils.isNotBlank(id) ? dataModelService
                .findOne(id) : new DataModel();
        map.put("model", model);
        return "/bigdata/dataModel/modelAdd.ftl";
    }

    /**
     * 删除数据模型
     *
     * @return
     */
    @RequestMapping("/deleteDataModel")
    @ResponseBody
    public Response deleteDataModel(String id) {
        DataModel model = dataModelService.findOne(id);
        if (!getLoginInfo().getUnitId().equals(model.getUnitId())) {
            return Response.ok().data("不能删除上级单位创建的模型!").build();
        }
        List<DataModelFavorite> modelFavorites = dataModelFavoriteService.findListBy("modelId", id);
        if (modelFavorites.size() > 0) {
            return Response.ok().data("该模型被多维报表引用，您不能删除!").build();
        }
        dataModelService.deleteDataModel(id, model.getCode());
        // 业务日志埋点
        BizOperationLog bizLog = new BizOperationLog();
        bizLog.setLogType(BizOperationLog.LOG_TYPE_DELETE);
        bizLog.setBizCode("delete-model");
        bizLog.setDescription("删除" + model.getName());
        bizLog.setBizName("数据模型管理");
        bizLog.setSubSystem("大数据管理");
        bizLog.setOperator(getLoginInfo().getRealName() + "("
                + getLoginInfo().getUserName() + ")");
        bizLog.setOperationTime(new Date());
        //oldData
        bizLog.setOldData(JSON.toJSONString(model));
        BizOperationLogCollector.submitBizOperationLog(bizLog);
        return Response.ok().data("删除成功!").build();
    }

    /**
     * 保存指标和维度
     *
     * @param modelParam
     * @return
     */
    @RequestMapping("/saveModelParam")
    @ResponseBody
    public Response saveModelParam(DataModelParam modelParam) {
        try {
            String id = modelParam.getId();
            // 业务日志埋点
            BizOperationLog bizLog = new BizOperationLog();
            if (modelParam.getType().equals("dimension")) {
                if (StringUtils.isBlank(id)) {
                    bizLog.setLogType(BizOperationLog.LOG_TYPE_INSERT);
                    bizLog.setBizCode("insert-model-dimension");
                    bizLog.setDescription("新增维度" + modelParam.getName());
                    bizLog.setNewData(JSON.toJSONString(modelParam));
                } else {
                    bizLog.setLogType(BizOperationLog.LOG_TYPE_UPDATE);
                    bizLog.setBizCode("update-model-dimension");
                    bizLog.setDescription("修改维度" + modelParam.getName());
                    DataModelParam oldParam = dataModelParamService.findOne(id);
                    bizLog.setOldData(JSON.toJSONString(oldParam));
                    bizLog.setNewData(JSON.toJSONString(modelParam));
                }
            } else {
                if (StringUtils.isBlank(id)) {
                    bizLog.setLogType(BizOperationLog.LOG_TYPE_INSERT);
                    bizLog.setBizCode("insert-model-index");
                    bizLog.setDescription("新增指标" + modelParam.getName());
                    bizLog.setNewData(JSON.toJSONString(modelParam));
                } else {
                    bizLog.setLogType(BizOperationLog.LOG_TYPE_UPDATE);
                    bizLog.setBizCode("update-model-index");
                    bizLog.setDescription("修改指标" + modelParam.getName());
                    DataModelParam oldParam = dataModelParamService.findOne(id);
                    bizLog.setOldData(JSON.toJSONString(oldParam));
                    bizLog.setNewData(JSON.toJSONString(modelParam));
                }
            }
            if(modelParam.getParentId()==null||modelParam.getParentId()==""){
                modelParam.setParentId(TOP_DIMENSION);
            }
            dataModelParamService.saveDataModelParam(modelParam);
            bizLog.setBizName("数据模型管理");
            bizLog.setSubSystem("大数据管理");
            bizLog.setOperator(getLoginInfo().getRealName() + "("
                    + getLoginInfo().getUserName() + ")");
            bizLog.setOperationTime(new Date());
            BizOperationLogCollector.submitBizOperationLog(bizLog);
            return Response.ok().data("").build();
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    /**
     * 编辑指标
     *
     * @return
     */
    @RequestMapping("/editModelParam")
    public String editModelParam(String id, String type, String code, String modelId,
                                 ModelMap map) {
        DataModelParam param;
        if (StringUtils.isNotBlank(id)) {
            param = dataModelParamService.findOne(id);
            if(!(TOP_DIMENSION).equals(param.getParentId())){
                String parentName=dataModelParamService.findOneBy("id",param.getParentId()).getName();
                map.put("parentName",parentName);
            }

            List<DataModelParam> dataModelParams = dataModelParamService.findByCodeAndType(code, type);
            map.put("dataModelParams", dataModelParams);
        }else {
            param=new DataModelParam();
        }
        param.setType(type);
        param.setCode(code);
        map.put("param", param);
        DataModel model = dataModelService.findOne(modelId);
        map.put("model", model);
        if (model.getSource() != null && model.getSource() == 0) {
            List<PropertyTag> tags = propertyTagService.findListBy("code", "model");
            if (tags.size() < 1) {
                map.put("metadataList", Lists.newArrayList());

            } else {
                PropertyTag propertyTag = tags.get(0);
                List<MetadataTag> metadataTags = metadataTagService.findByTagId(propertyTag.getId());
                List<String> ids = metadataTags.stream().map(MetadataTag::getMdId).collect(Collectors.toList());
                List<Metadata> metadataList = metadataService.findByDbTypeAndIds(model.getType(), ids);
                if(StringUtils.isNotBlank(param.getUseField())) {
                    MetadataTableColumn useFieldColumn = metadataTableColumnService.findOneBy("id", param.getUseField());
                    map.put("useField",useFieldColumn);
                }
                if(StringUtils.isNotBlank(param.getOrderField())){
                    MetadataTableColumn orderFieldColumn = metadataTableColumnService.findOneBy("id",param.getOrderField());
                    map.put("orderField",orderFieldColumn);
                }
                map.put("metadataList", metadataList);
            }
            return "/bigdata/dataModel/metadataParamAdd.ftl";
        }
        return "/bigdata/dataModel/modelParamAdd.ftl";
    }

    /**
     * 展示上级维度列表
     * @param useTable
     * @return
     */
    @RequestMapping(value = "/showDimensionDataModel",method = RequestMethod.POST)
    @ResponseBody
    public Response showDimensionDataModel(String useTable,String modelName,String id){
        if(StringUtils.isNotBlank(useTable)) {
            List<DataModelParam> dimensionModelList;
            dimensionModelList= StringUtils.isNotBlank(id) ?
                    dataModelParamService.findNonDirectDimensionByIdAndUseTable(useTable, id):
                    dataModelParamService.findListBy(new String[]{"useTable","type"},new String[]{useTable,"dimension"});
            if (!StringUtil.isBlank(modelName)) {
                dimensionModelList = dimensionModelList.stream().filter(dataModelParam -> !dataModelParam.getName().equals(modelName)).collect(Collectors.toList());
            }
            return Response.ok().data(dimensionModelList).build();
        }else{
            return Response.error().message("参数不可为空").build();
        }
    }

    /**
     * 删除指标或维度
     *
     * @return
     */
    @RequestMapping("/deleteDataModelParam")
    @ResponseBody
    public Response deleteDataModelParam(String id) {
        DataModelParam param = dataModelParamService.findOne(id);
        dataModelParamService.delete(id);
        // 删除元数据关系
        List<DataModel> dataModels = dataModelService.findListBy("code", param.getCode());
        if (dataModels.size() > 0) {
            DataModel dataModel = dataModels.get(0);
            if (dataModel.getSource() != null && dataModel.getSource() == 0) {
                long count = dataModelParamService.countBy("code", dataModel.getCode());
                if (count < 1) {
                    metadataRelationService.deleteBySourceIdAndTargetId(param.getUseTable(), dataModel.getId());
                }
            }
        }

        // 业务日志埋点
        BizOperationLog bizLog = new BizOperationLog();
        bizLog.setLogType(BizOperationLog.LOG_TYPE_DELETE);
        if (param.getType().equals("dimension")) {
            bizLog.setBizCode("delete-model-dimension");
            bizLog.setDescription("删除维度" + param.getName());
        } else {
            bizLog.setBizCode("delete-model-index");
            bizLog.setDescription("删除指标" + param.getName());
        }
        bizLog.setBizName("数据模型管理");
        bizLog.setSubSystem("大数据管理");
        bizLog.setOperator(getLoginInfo().getRealName() + "("
                + getLoginInfo().getUserName() + ")");
        bizLog.setOperationTime(new Date());
        //oldData
        bizLog.setOldData(JSON.toJSONString(param));
        BizOperationLogCollector.submitBizOperationLog(bizLog);
        return Response.ok().data("删除成功!").build();
    }

    /**
     * 隐藏或显示指标或维度
     *
     * @return
     */
    @RequestMapping("/showOrHideDataModelParam")
    @ResponseBody
    public Response showOrHideDataModelParam(String id, Integer status) {
        DataModelParam param = dataModelParamService.findOne(id);
        dataModelParamService.updateDataModelParam(id, status);
        // 业务日志埋点
        BizOperationLog bizLog = new BizOperationLog();
        bizLog.setLogType(BizOperationLog.LOG_TYPE_UPDATE);
        if (param.getType().equals("dimension")) {
            bizLog.setBizCode("update-model-dimension");
            if (status == 0)
                bizLog.setDescription("显示维度" + param.getName());
            else
                bizLog.setDescription("隐藏维度" + param.getName());
        } else {
            bizLog.setBizCode("update-model-index");
            if (status == 0)
                bizLog.setDescription("显示指标" + param.getName());
            else
                bizLog.setDescription("隐藏指标" + param.getName());
        }
        bizLog.setBizName("数据模型管理");
        bizLog.setSubSystem("大数据管理");
        bizLog.setOperator(getLoginInfo().getRealName() + "("
                + getLoginInfo().getUserName() + ")");
        bizLog.setOperationTime(new Date());
        BizOperationLogCollector.submitBizOperationLog(bizLog);

        return Response.ok().data("修改成功!").build();
    }

    /**
     * 获取过滤选择项
     *
     * @param code
     * @param dimensionId
     * @return
     */
    @RequestMapping("/getFilterData")
    @ResponseBody
    public Response getFilterData(String code, String dimensionId, String modelDatasetId) {
        return Response.ok().data(dataModelBiz.getFilterData(code, dimensionId, modelDatasetId)).build();
    }


    /**
     * 保存报表
     *
     * @param code
     * @param indexParam
     * @param dimensionRowParam
     * @param dimensionColumnParam
     * @param filterDataMap
     * @return
     */
    @RequestMapping("/saveDataModelFavorite")
    @ResponseBody
    public Response saveDataModelFavorite(String code, String indexParam,
                                          @RequestParam(value = "tagArrays[]", required = false) String[] tagArrays,
                                          String dimensionRowParam, String dimensionColumnParam,
                                          @RequestParam(value = "conditionParam[]", required = false) String[] conditionParam,
                                          String filterDataMap, String modelDatasetId, DataModelFavorite dataModelFavorite) {
        LoginInfo loginInfo = getLoginInfo();
        if (dataModelBiz.checkIsExist(dataModelFavorite)) {
            return Response.error().message("该名称已存在!").build();
        }
        dataModelFavorite.setTags(tagArrays);

        DataModelFavoriteParam indexFavoriteParam = new DataModelFavoriteParam();
        indexFavoriteParam.setParamType("index");
        indexFavoriteParam.setParamValue(indexParam);

        DataModelFavoriteParam rowFavoriteParam = new DataModelFavoriteParam();
        rowFavoriteParam.setParamType("row");
        rowFavoriteParam.setParamValue(dimensionRowParam);

        DataModelFavoriteParam columnFavoriteParam = new DataModelFavoriteParam();
        columnFavoriteParam.setParamType("column");
        columnFavoriteParam.setParamValue(dimensionColumnParam);

        DataModelFavoriteParam datasetParam = new DataModelFavoriteParam();
        datasetParam.setParamType("dataset");
        datasetParam.setParamValue(modelDatasetId);

        DataModelFavoriteParam filterDataParam = new DataModelFavoriteParam();
        filterDataParam.setParamType("filterDataMap");
        filterDataParam.setParamValue(filterDataMap);

        dataModelFavorite.getDataModelFavoriteParams().add(indexFavoriteParam);
        dataModelFavorite.getDataModelFavoriteParams().add(rowFavoriteParam);
        dataModelFavorite.getDataModelFavoriteParams().add(columnFavoriteParam);
        dataModelFavorite.getDataModelFavoriteParams().add(datasetParam);
        dataModelFavorite.getDataModelFavoriteParams().add(filterDataParam);

        if (ArrayUtils.isNotEmpty(conditionParam)) {
            DataModelFavoriteParam conditionFavoriteParam = new DataModelFavoriteParam();
            conditionFavoriteParam.setParamType("condition");
            conditionFavoriteParam.setParamValue(JSON.toJSONString(conditionParam));
            dataModelFavorite.getDataModelFavoriteParams().add(conditionFavoriteParam);
        }

        return dataModelBiz.getSearchResult(code, indexParam, dimensionRowParam,
                dimensionColumnParam, filterDataMap, true, null,
                modelDatasetId, dataModelFavorite, loginInfo);
    }

    /**
     * 展示报表
     *
     * @param code
     * @param indexParam
     * @param dimensionRowParam
     * @param dimensionColumnParam
     * @param filterDataMap
     * @return
     */
    @RequestMapping("/showReport")
    @ResponseBody
    public Response showReport(String code, String indexParam,
                               String dimensionRowParam, String dimensionColumnParam,
                               String filterDataMap, String modelDatasetId) {
        try {
            return dataModelBiz.getSearchResult(code, indexParam, dimensionRowParam,
                    dimensionColumnParam, filterDataMap, true, null,
                    modelDatasetId, null, getLoginInfo());
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    /**
     * 展示图表
     *
     * @param code
     * @param indexParam
     * @param dimensionRowParam
     * @param dimensionColumnParam
     * @param filterDataMap
     * @return
     */
    @RequestMapping("/showChart")
    @ResponseBody
    public Response showChart(String code, String indexParam,
                              String dimensionRowParam, String dimensionColumnParam,
                              String filterDataMap, String type, String modelDatasetId) {
        return dataModelBiz.getSearchResult(code, indexParam, dimensionRowParam,
                dimensionColumnParam, filterDataMap, false, type,
                modelDatasetId, null, getLoginInfo());
    }

    /**
     * 保存多维报表页面
     *
     * @param map
     * @return
     */
    @RequestMapping("/saveModelFavoriteUI")
    public String saveModelFavoriteUI(ModelMap map, String code, String favoriteId) {
        if (StringUtils.isNotBlank(favoriteId)) {
            DataModelFavorite favorite = dataModelFavoriteService.findOne(favoriteId);
            DataModel model = dataModelService.findOne(favorite.getModelId());
            code = model.getCode();
            map.put("favorite", favorite);
            map.put("tags", getTags(favoriteId));
            // 已选择的条件
            List<DataModelFavoriteParam> params = dataModelFavoriteParamService.findListBy(new String[]{"favoriteId", "paramType"}, new String[]{favoriteId, "condition"});
            if (params.size() > 0) {
                String condition = params.get(0).getParamValue();
                String[] conditionIds = JSON.parseObject(condition, String[].class);
                map.put("conditionIds", conditionIds);
            }
            List<FolderDetail> folderDetails = folderDetailService.findListBy("businessId", favoriteId);
            map.put("folderDetail", folderDetails.size() > 0 ? folderDetails.get(0) : new FolderDetail());
        } else {
            map.put("favorite", new DataModelFavorite());
            List<Tag> tags = tagService.findListBy("tagType", MODEL_REPORT.getBusinessType().shortValue());
            map.put("tags", tags);
            map.put("folderDetail", new FolderDetail());
        }

        List<DataModelParam> dimensionList = dataModelParamService.findByCodeAndType(code, "dimension");
        Map<String, List<Folder>> folderMap = folderService.findAllFolderForDirectory();
        List<FolderEx> folderTree = folderService.findFolderTree();
        map.put("folderMap", folderMap);
        map.put("folderTree", folderTree);
        map.put("dimensionList", dimensionList);
        return "/bigdata/dataModel/modelSave.ftl";
    }

    private List<Tag> getTags(String favoriteId) {
        Set<String> selectTag = tagRelationService.getByBusinessId(new String[]{favoriteId}).stream().map(TagRelation::getTagId).collect(Collectors.toSet());
        List<Tag> tags = tagService.findListBy("tagType", MODEL_REPORT.getBusinessType().shortValue());

        if (selectTag.size() < 1) {
            return tags;
        }
        tags.forEach(e -> {
            if (selectTag.contains(e.getId())) {
                e.setSelected(true);
            }
        });
        return tags;
    }

    /**
     * 更新报表
     *
     * @return
     */
    @RequestMapping("/updateDataModelFavorite")
    @ResponseBody
    public Response updateDataModelFavorite(@RequestParam(value = "tagArrays[]", required = false) String[] tagArrays,
                                            @RequestParam(value = "conditionParam[]", required = false) String[] conditionParam,
                                            DataModelFavorite dataModelFavorite) {

        if (dataModelBiz.checkIsExist(dataModelFavorite)) {
            return Response.error().message("该名称已存在!").build();
        }

        dataModelFavorite.setTags(tagArrays);

        if (ArrayUtils.isNotEmpty(conditionParam)) {
            DataModelFavoriteParam conditionFavoriteParam = new DataModelFavoriteParam();
            conditionFavoriteParam.setParamType("condition");
            conditionFavoriteParam.setParamValue(JSON.toJSONString(conditionParam));
            dataModelFavorite.getDataModelFavoriteParams().add(conditionFavoriteParam);
        }

        dataModelFavoriteService.updateDataModelFavorite(dataModelFavorite);
        return Response.ok().build();
    }
}
