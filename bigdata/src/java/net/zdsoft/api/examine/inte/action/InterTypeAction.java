package net.zdsoft.api.examine.inte.action;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.api.base.entity.eis.ApiInterface;
import net.zdsoft.api.base.entity.eis.ApiInterfaceType;
import net.zdsoft.api.base.service.ApiEntityService;
import net.zdsoft.api.base.service.ApiInterfaceService;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.service.DatabaseService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

@Controller
@RequestMapping(value = "/bigdata/api/interType")
public class InterTypeAction extends InterBaseAction {

    @Autowired
    private ApiInterfaceService apiInterfaceService;
    @Autowired
    private ApiEntityService apiEntityService;
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private MetadataService metadataService;
    @Resource
    private BigLogService bigLogService;

    @RequestMapping("/showIndex")
    public String showIndexManageIndex(ModelMap map) {
        return "/api/examine/interfaceManage/type/typeIndex.ftl";
    }

    /**
     * 类型管理
     */
    @RequestMapping("/showIntefaceTypes")
    public String showInterfaces(HttpServletRequest request, String typeName, Integer classify, ModelMap map) {
        List<ApiInterfaceType> interfaceTypes = openApiInterfaceTypeService.getInterfaceTypes(typeName, classify);
        map.put("openInterfaceTypes", interfaceTypes);
        return "/api/examine/interfaceManage/type/typeList.ftl";
    }

    /**
     * 修改参数
     *
     * @param typeId
     * @return
     */
    @RequestMapping("/editType")
    @ControllerInfo("查找接口类型")
    public String editEntity(String typeId, ModelMap map) {
        ApiInterfaceType openApiInterfaceType = StringUtils.isNotBlank(typeId) ? openApiInterfaceTypeService.findOne(typeId)
                : new ApiInterfaceType();
        map.put("interfaceType", openApiInterfaceType);
        List<Metadata> metadataList = metadataService.findSupportApiMetadata();
        map.put("metadataList", metadataList);
        return "/api/examine/interfaceManage/type/typeEdit.ftl";
    }

    /**
     * 删除类型 1：清除接口 2：清除属性
     *
     * @param typeId
     * @return
     */
    @ResponseBody
    @RequestMapping("/delType")
    @ControllerInfo("删除类型")
    public Response delType(String typeId) {
        try {
            ApiInterfaceType openApiInterfaceType = openApiInterfaceTypeService.findOne(typeId);
            if (isNotExistApplyByType(openApiInterfaceType.getType())) {
                if (isInterfaceType(openApiInterfaceType.getClassify())){
                	apiInterfaceService.deleteByType(openApiInterfaceType.getType());
                }
                if (isResultType(openApiInterfaceType.getClassify())){
                	apiInterfaceService.deleteByResultType(openApiInterfaceType.getType());
                }
                apiEntityService.deleteByType(openApiInterfaceType.getType());
                openApiInterfaceTypeService.delete(typeId);
                //业务日志埋点  删除
                LogDto logDto = new LogDto();
                logDto.setBizCode("delete-apiInterfaceType");
                logDto.setDescription("接口类型 " + openApiInterfaceType.getTypeName());
                logDto.setBizName("数据接口管理");
                logDto.setOldData(openApiInterfaceType);
                bigLogService.deleteLog(logDto);
            } else {
                return Response.error().message("类型正在使用，不能删除！").build();
            }
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    /**
     * 保存类型
     *
     * @param body
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveType")
    @ControllerInfo("保存接口类型")
    public Response saveParam(@RequestBody String body) {
        try {
            JSONObject jsonObject = SUtils.dc(body, JSONObject.class);
            ApiInterfaceType openApiInterfaceType = getApiType(jsonObject);
            String oldType = jsonObject.getString("oldType");
            String newType = openApiInterfaceType.getType();
            boolean isHvingType = false;
            if (StringUtils.isBlank(oldType) || !oldType.equals(newType)) {
                isHvingType = isExistInterfaceType(openApiInterfaceType);
            }
            if (!isHvingType) {
                openApiInterfaceTypeService.save(openApiInterfaceType);
                if (StringUtils.isNotBlank(oldType)) {
                    if (isResultType(openApiInterfaceType.getClassify()))
                        apiEntityService.updateType(newType, oldType);
                    apiInterfaceService.updateResultType(newType, oldType);
                    if (isInterfaceType(openApiInterfaceType.getClassify()))
                        apiInterfaceService.updatetTypeNameAndType(openApiInterfaceType.getTypeName(), newType, oldType);
                }
            } else {
                return Response.error().message("类型已经存在，不能重复添加！").build();
            }
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    //-----------------------------------------------------------------私有方法区 -----------------------------------------------
    private ApiInterfaceType getApiType(JSONObject jsonObject) {
        boolean flag=true;
        ApiInterfaceType openApiInterfaceType = new ApiInterfaceType();
        String typeId = jsonObject.getString("typeId");
        if (StringUtils.isBlank(typeId)) {
            typeId = UuidUtils.generateUuid();
            flag=false;

        }
        openApiInterfaceType.setId(typeId);
        openApiInterfaceType.setClassify(Integer.valueOf(jsonObject.getString("classify")));
        openApiInterfaceType.setType(jsonObject.getString("type"));
        openApiInterfaceType.setTypeName(jsonObject.getString("typeName"));
        openApiInterfaceType.setMetadataId(jsonObject.getString("metadataId"));
        if (flag){
            ApiInterfaceType oldApiInterfaceType = openApiInterfaceTypeService.findOne(typeId);
            //业务日志埋点  修改
            LogDto logDto=new LogDto();
            logDto.setBizCode("update-apiInterface");
            logDto.setDescription("接口类型名称 "+oldApiInterfaceType.getTypeName());
            logDto.setOldData(oldApiInterfaceType);
            logDto.setNewData(jsonObject);
            logDto.setBizName("数据接口管理");
            bigLogService.updateLog(logDto);
        }else {
            //业务日志埋点  新增
            LogDto logDto=new LogDto();
            logDto.setBizCode("insert-apiType");
            logDto.setDescription("接口类型名称 "+jsonObject.getString("typeName"));
            logDto.setNewData(jsonObject);
            logDto.setBizName("数据接口管理");
            bigLogService.insertLog(logDto);
        }
        return openApiInterfaceType;
    }

    /**
     * 是否已经存在类型
     *
     * @param ApiInterfaceType
     * @return
     */
    private boolean isExistInterfaceType(ApiInterfaceType openApiInterfaceType) {
        List<ApiInterfaceType> typeList = openApiInterfaceTypeService.findByTypeAndByClassify(openApiInterfaceType.getType(),
                openApiInterfaceType.getClassify());
        return CollectionUtils.isNotEmpty(typeList);
    }
}
