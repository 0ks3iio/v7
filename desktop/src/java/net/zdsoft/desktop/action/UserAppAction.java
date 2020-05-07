package net.zdsoft.desktop.action;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.zdsoft.desktop.constant.DeskTopConstant;
import net.zdsoft.desktop.dto.DServerDto;
import net.zdsoft.desktop.entity.UserApp;
import net.zdsoft.desktop.service.UserAppService;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.SortUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.dto.server.ModelDto;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.remote.service.ModelRemoteService;
import net.zdsoft.system.remote.service.ServerRemoteService;

/**
 * 常用操作
 * @author shenke
 * @since 2017.05.09
 */
@Controller
@RequestMapping("/desktop/userApp")
public class UserAppAction extends DeskTopBaseAction {

    private Logger logger = LoggerFactory.getLogger(UserAppAction.class);

    @Autowired private UserAppService userAppService;
    @Autowired private ModelRemoteService modelRemoteService;
    @Autowired private ServerRemoteService serverRemoteService;

    private static final String MODEL_ICON_DEFAULT = "/default/images/default_b.png";

    @RequestMapping("/set") public String execute(ModelMap map, String templatePath) {
        List<UserApp> userAppList = userAppService.findListBy("userId", getUserId());
        SortUtils.ASC(userAppList,"displayOrder");
        Object sessionAppId = getRequest().getSession().getAttribute("appId");
        int appId = -1;
        if (sessionAppId != null) {
            try {
                appId = NumberUtils.toInt(sessionAppId.toString());
            } catch (Exception e) {
                logger.error("Get session appId is {} transfer error", appId);
            }
        }
        List<Model> modelList = (List<Model>)getSession().getAttribute(DeskTopConstant.DESKTOP_SESSION_MODEL + getUserId() + appId);
        Map<Integer,Model> modelMap = EntityUtils.getMap(modelList, "id");
        List<UserApp> authorityUserAppList = Lists.newArrayList();
        List<Model> authorityModelList = Lists.newArrayList();
        for (UserApp userApp : userAppList) {
            Model authorityModel = modelMap.get(userApp.getModelId());
            if (authorityModel != null ) {
                authorityModelList.add(authorityModel);
                authorityUserAppList.add(userApp);
            }
        }
        List<ModelDto> authorityModelDtoList = Lists.newArrayList();
        String filePath = sysOptionRemoteService.findValue(net.zdsoft.system.constant.Constant.FILE_PATH);
        for (Model authorityModel : authorityModelList) {
            ModelDto modelDto = new ModelDto();
            modelDto.setModel(authorityModel);
            String picture = authorityModel.getPicture();
            if (StringUtils.indexOf(picture,".")<0) {
                picture = picture + "_b.png";
            } else {
                picture = picture.substring(0, picture.indexOf(".")) + "_b." + picture.substring(picture.indexOf(".")+1);
            }
            picture = iconExists(filePath,picture)?picture:MODEL_ICON_DEFAULT;
            modelDto.setImageUrl(UrlUtils.ignoreLastRightSlash(getFileURL()) + "/store/modelpic/" + UrlUtils.ignoreFirstLeftSlash(picture));
            if ( new Integer(-1).equals(authorityModel.getParentId()) ) {
                continue;
            }
            authorityModelDtoList.add(modelDto);
        }
        map.put("authorityModelDtoList",authorityModelDtoList);

        //子系统
        final Set<Integer> subSystemSet = EntityUtils.getSet(modelList,"subSystem");
        List<Server> serverList = SUtils.dt(RedisUtils.get("session.server." + getUserId(), new RedisInterface<String>() {
            @Override
            public String queryData() {
                return serverRemoteService.findListByIn("subId",EntityUtils.toArray(subSystemSet, Integer.class));
            }
        }),Server.class);
        final List<DServerDto> dServerDtoList = Lists.newArrayList();
        Map<Integer, DServerDto> dServerDtoMap = Maps.newHashMap();
        for (Server e : serverList) {
            DServerDto dServerDto = new DServerDto();
            dServerDto.setServer(e);
            String iconURLString = UrlUtils.ignoreLastRightSlash(getFileURL()) + "/" + UrlUtils.ignoreFirstLeftSlash(e.getIconUrl());
            dServerDto.setImageUrl(iconURLString);
            dServerDto.setDir(Boolean.FALSE);
            dServerDtoMap.put(e.getSubId(),dServerDto);
            dServerDtoList.add(dServerDto);
        }

        final List<JSONObject> modelDtoList = Lists.newArrayList();
        modelList = EntityUtils.filter(modelList, new EntityUtils.Filter<Model>() {
            @Override
            public boolean doFilter(Model model) {
                return new Integer(Model.PARENT_ID_DIRECT_SUBSYSTEM).equals(model.getParentId());
            }
        });
        for (Model e : modelList) {
            DServerDto dServerDto = dServerDtoMap.get(e.getSubSystem());
            if (dServerDto == null) {
                continue;
            }
            ModelDto modelDto = new ModelDto();
            modelDto.setModel(e);
            String picture = e.getPicture();
            if (StringUtils.indexOf(picture,".")<0) {
                picture = picture + "_b.png";
            } else {
                picture = picture.substring(0, picture.indexOf(".")) + "_b." + picture.substring(picture.indexOf(".")+1);
            }
            picture = iconExists(filePath,picture)?picture:MODEL_ICON_DEFAULT;
            modelDto.setImageUrl(UrlUtils.ignoreLastRightSlash(getFileURL()) + "/store/modelpic/" + UrlUtils.ignoreFirstLeftSlash(picture));
            dServerDto.addSubModeldto(modelDto);
        }
        EntityUtils.filter(dServerDtoList, new EntityUtils.Filter<DServerDto>() {
            @Override
            public boolean doFilter(DServerDto dServerDto) {
                return CollectionUtils.isEmpty(dServerDto.getModelDtos());
            }
        });
        map.put("serverDtos",dServerDtoList);
        return "/desktop/homepage/ap/set/operation-scroll-set.ftl";
    }

    private boolean iconExists(String filePath, String picture) {
        try {
            return new File(filePath + File.separator + "store" + File.separator + "modelpic" + File.separator + picture ).exists();
        } catch (Exception e) {
            return false;
        }
    }

    @ResponseBody @RequestMapping("/model/add") public String doSaveSettings(String jsonString) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            JSONArray modelIdArray = null;
            if (jsonObject.get("modelId") == null) {
                userAppService.deleteByUserId(getUserId());
                RedisUtils.del("my.common.use.app." + getUserId());
                return success("保存成功");
            }
            List<UserApp> originUserApps = userAppService.findListBy("userId",getUserId());
            Map<Integer, UserApp> oroginUserAppMap = EntityUtils.getMap(originUserApps,"modelId");
            boolean notArray = Boolean.FALSE;
            try {
                modelIdArray = jsonObject.getJSONArray("modelId");
            } catch (Exception e) {
                notArray = Boolean.TRUE;
                modelIdArray = new JSONArray();
                modelIdArray.add(jsonObject.getString("modelId"));
            }
            JSONArray subsystemArray = null;
            JSONArray displayOrderArray;
            if (notArray) {
                subsystemArray = new JSONArray();
                subsystemArray.add(jsonObject.getString("subsystem"));
                displayOrderArray = new JSONArray();
                displayOrderArray.add(jsonObject.getString("displayOrder"));
            }else {
                subsystemArray = jsonObject.getJSONArray("subsystem");
                displayOrderArray = jsonObject.getJSONArray("displayOrder");
            }
            List<UserApp> userAppList = Lists.newArrayList();
            Object[] modelIds = modelIdArray.toArray(new Object[0]);
            String userId = getUserId();
            final List<UserApp> saveUserAppContainers = Lists.newArrayList();
            for (int i=0; i<modelIds.length; i++) {
                UserApp userApp = packageUserApp(modelIds[i],subsystemArray.get(i),displayOrderArray.get(i),i);
                UserApp originUserApp = oroginUserAppMap.get(userApp.getModelId());
                saveUserAppContainers.add(originUserApp);
                userApp.setId(originUserApp == null?UuidUtils.generateUuid():originUserApp.getId());
                userApp.setUserId(userId);
                userAppList.add(userApp);
            }
            EntityUtils.filter(originUserApps, new EntityUtils.Filter<UserApp>() {
                @Override
                public boolean doFilter(UserApp userApp) {
                    return saveUserAppContainers.contains(userApp);
                }
            });
            //delete and update
            userAppService.updateUserApps(userAppList,originUserApps);
        } catch (Exception e){
            e.printStackTrace();
            return error("保存失败");
        }
        RedisUtils.del("my.common.use.app." + getUserId());
        return success("保存成功!");
    }

    private UserApp packageUserApp (Object modelId, Object subsystem, Object displayOrder, Integer defaultOrder) {
        UserApp userApp = new UserApp();
        try {
            Assert.notNull(modelId);
            userApp.setModelId(Integer.valueOf(modelId.toString()));
            if (subsystem == null) {
                Model model = SUtils.dc(modelRemoteService.findOneById(userApp.getModelId()),Model.class);
                subsystem = model.getSubSystem();
            }
            userApp.setSubsystem(Integer.valueOf(subsystem.toString()));
            userApp.setDisplayOrder(NumberUtils.toInt(displayOrder.toString(),defaultOrder));
        } catch (Exception e){
            return userApp;
        }
        return userApp;
    }

}
