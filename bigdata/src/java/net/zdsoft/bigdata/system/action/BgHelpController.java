package net.zdsoft.bigdata.system.action;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.system.entity.BgHelp;
import net.zdsoft.bigdata.system.entity.BgModule;
import net.zdsoft.bigdata.system.service.BgAuthorityService;
import net.zdsoft.bigdata.system.service.BgHelpService;
import net.zdsoft.bigdata.system.service.BgModuleService;
import net.zdsoft.bigdata.system.service.BgUserAuthService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.bigdata.v3.index.action.BigdataBiBaseAction;
import net.zdsoft.bigdata.v3.index.entity.HeadInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping(value = "/bigdata/help")
public class BgHelpController extends BigdataBiBaseAction {

    @Autowired
    private BgHelpService bgHelpService;

    @Autowired
    private BgModuleService bgModuleService;

    @Autowired
    private BgAuthorityService bgAuthorityService;

    @Autowired
    private BgUserAuthService bgUserAuthService;

    @Resource
    private BigLogService bigLogService;

    @RequestMapping("/index")
    public String index(String moduleId, ModelMap map) {
        List<BgHelp> helpList = new ArrayList<BgHelp>();
        if (StringUtils.isNotBlank(moduleId))
            helpList = bgHelpService.findHelpListByModuleId(moduleId);
        else
            helpList = bgHelpService.findAllHelpList();
        map.put("helpList", helpList);
        List<BgModule> moduleList = bgModuleService.findAllModuleList();
        map.put("moduleList", moduleList);
        map.put("moduleId", moduleId);
        return "/bigdata/system/help/helpList.ftl";
    }

    @RequestMapping("/bi/index")
    public String index4Bi(String moduleId, ModelMap map) {
        map.put("currentModuleUrl", "/bigdata/help/tree");
        HeadInfo headInfo = getHeadInfo();
        headInfo.setTitle("帮助中心");
        map.put("headInfo", headInfo);
        return "/bigdata/v3/templates/bi/bi-transfer.ftl";
    }

    @RequestMapping("/tree")
    public String tree(String helpId, String showAll, ModelMap map) {
        boolean isShowAll = false;
        if (StringUtils.isNotBlank(showAll) && "yes".equals(showAll)) {
            isShowAll = true;
        }
        List<BgHelp> helpList = bgHelpService.findAllHelpList();
        Map<String, List<BgHelp>> helpMap = new HashMap<String, List<BgHelp>>();

        for (BgHelp help : helpList) {
            List<BgHelp> moduleHelpList = helpMap.get(help.getModuleId());
            if (CollectionUtils.isEmpty(moduleHelpList))
                moduleHelpList = new ArrayList<BgHelp>();
            moduleHelpList.add(help);
            helpMap.put(help.getModuleId(), moduleHelpList);
        }
        List<BgModule> moduleList = new ArrayList<BgModule>();
        if (isShowAll) {
            moduleList = bgModuleService.findAllModuleList();
        } else {
            String userType = getLoginInfo().getUserType().toString();
            boolean isTopAdmin = false;
            if (getLoginInfo().getUserType() == User.USER_TYPE_TOP_ADMIN) {
                isTopAdmin = true;
            }
            boolean isBackgroundUser = bgUserAuthService.isBackgroundUser(
                    getLoginInfo().getUserId(), getLoginInfo().getUserType());
            if (isBackgroundUser) {
                userType = BgModule.USER_TYPE_BACKGROUND;
            }
            moduleList = bgAuthorityService.getAuthorityModuleList(
                    getLoginInfo().getUserId(), userType, isTopAdmin); // bgModuleService.findAllModuleList();
        }
        List<BgModule> topModuleList = new ArrayList<BgModule>();
        Map<String, List<BgModule>> secondModuleMap = new HashMap<String, List<BgModule>>();
        for (BgModule module : moduleList) {
            List<BgHelp> moduleHelpList = helpMap.get(module.getId());

            if (CollectionUtils.isEmpty(moduleHelpList)
                    && !"dir".equals(module.getType())) {
                module.setHelpId(Constant.GUID_ZERO);
            } else if (CollectionUtils.isNotEmpty(moduleHelpList)
                    && moduleHelpList.size() == 1) {
                module.setHelpId(moduleHelpList.get(0).getId());
                helpMap.remove(module.getId());
            }
            if (module.getParentId().equals(Constant.GUID_ZERO)) {
                topModuleList.add(module);
            } else {
                List<BgModule> secondModuleList = secondModuleMap.get(module
                        .getParentId());
                if (CollectionUtils.isEmpty(secondModuleList))
                    secondModuleList = new ArrayList<BgModule>();
                secondModuleList.add(module);
                secondModuleMap.put(module.getParentId(), secondModuleList);
            }
        }
        map.put("topModuleList", topModuleList);
        map.put("secondModuleMap", secondModuleMap);
        map.put("helpId", helpId);
        map.put("helpMap", helpMap);
        return "/bigdata/system/help/helpTreeIndex.ftl";
    }

    @RequestMapping("/component")
    public String component(ModelMap map) {
        List<BgHelp> helpList = bgHelpService.findHelpListByCore(1);
        map.put("helpList", helpList);
        return "/bigdata/user/component/help.ftl";
    }

    @RequestMapping("/add")
    public String add(String moduleId, ModelMap map) {
        BgHelp help = new BgHelp();
        Integer maxOrderId = bgHelpService.getMaxOrderId();
        if (maxOrderId == null)
            maxOrderId = 0;
        if (maxOrderId >= 999)
            maxOrderId = 0;
        help.setOrderId(++maxOrderId);
        if (StringUtils.isNotBlank(moduleId)) {
            help.setModuleId(moduleId);
            help.setName(bgModuleService.findOne(moduleId).getName());
            help.setModuleName(bgModuleService.findOne(moduleId).getName());
        }
        map.put("help", help);
        List<BgModule> moduleList = bgModuleService.findAllModuleList();
        map.put("moduleList", moduleList);
        return "/bigdata/system/help/helpEdit.ftl";
    }

    @RequestMapping("/edit")
    public String edit(String id, ModelMap map) {
        BgHelp help = bgHelpService.findOne(id);
        map.put("help", help);
        List<BgModule> moduleList = bgModuleService.findAllModuleList();
        map.put("moduleList", moduleList);
        return "/bigdata/system/help/helpEdit.ftl";
    }

    @RequestMapping("/preview")
    public String preview(String id, ModelMap map) {
        if (Constant.GUID_ZERO.equals(id)) {
            map.put("errorMsg", "不存在相关的帮助文件");
            return "/bigdata/v3/common/error.ftl";
        }
        BgHelp help = bgHelpService.findOne(id);
        map.put("help", help);
        return "/bigdata/system/help/helpPreview.ftl";
    }

    @RequestMapping("/preview2np")
    public String preview2np(String id, ModelMap map) {
        BgHelp help = bgHelpService.findOne(id);
        map.put("help", help);
        return "/bigdata/system/help/helpPreview2np.ftl";
    }

    @RequestMapping("/save")
    @ResponseBody
    public Response save(BgHelp help) {
        try {
            BgHelp queryResult = bgHelpService
                    .findOneBy("name", help.getName());

            if (StringUtils.isBlank(help.getId())) {
                if (queryResult != null) {
                    return Response.error().message("帮助名称已经存在,请重新维护").build();
                }
                help.setId(UuidUtils.generateUuid());
                help.setCreationTime(new Date());
                help.setModifyTime(new Date());
                bgHelpService.save(help);
                //业务日志埋点  新增
                LogDto logDto = new LogDto();
                logDto.setBizCode("insert-help");
                logDto.setDescription("帮助 " + help.getName());
                logDto.setNewData(help);
                logDto.setBizName("帮助设置");
                bigLogService.insertLog(logDto);

                return Response.ok().message("保存成功").build();
            } else {
                if (queryResult != null) {
                    if (!queryResult.getId().equals(help.getId())) {
                        return Response.error().message("帮助名称已经存在,请重新维护")
                                .build();
                    }
                }
                help.setModifyTime(new Date());
                BgHelp oldHelp = bgHelpService.findOne(help.getId());
                bgHelpService.update(help, help.getId(), new String[]{"name",
                        "core", "moduleId", "moduleName", "content",
                        "description", "orderId", "modifyTime"});
                //业务日志埋点  修改
                LogDto logDto = new LogDto();
                logDto.setBizCode("update-help");
                logDto.setDescription("帮助 " + help.getName());
                logDto.setOldData(oldHelp);
                logDto.setNewData(help);
                logDto.setBizName("帮助设置");
                bigLogService.updateLog(logDto);

                return Response.ok().message("保存成功").build();
            }

        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/saveWithNoFresh")
    @ResponseBody
    public Response saveWithNoFresh(BgHelp help) {
        try {
            BgHelp queryResult = bgHelpService
                    .findOneBy("name", help.getName());

            if (StringUtils.isBlank(help.getId())) {
                if (queryResult != null) {
                    return Response.error().message("帮助名称已经存在,请重新维护").build();
                }
                help.setId(UuidUtils.generateUuid());
                help.setCreationTime(new Date());
                help.setModifyTime(new Date());
                bgHelpService.save(help);
                //业务日志埋点  新增
                LogDto logDto = new LogDto();
                logDto.setBizCode("insert-help");
                logDto.setDescription("帮助 " + help.getName());
                logDto.setNewData(help);
                logDto.setBizName("帮助设置");
                bigLogService.insertLog(logDto);
                return Response.ok().message(help.getId()).build();
            } else {
                if (queryResult != null) {
                    if (!queryResult.getId().equals(help.getId())) {
                        return Response.error().message("帮助名称已经存在,请重新维护")
                                .build();
                    }
                }
                help.setModifyTime(new Date());
                BgHelp oldHelp = bgHelpService.findOne(help.getId());
                bgHelpService.update(help, help.getId(), new String[]{"name",
                        "core", "moduleId", "moduleName", "content",
                        "description", "orderId", "modifyTime"});
                //业务日志埋点  修改
                LogDto logDto = new LogDto();
                logDto.setBizCode("update-help");
                logDto.setDescription("帮助 " + help.getName());
                logDto.setOldData(oldHelp);
                logDto.setNewData(help);
                logDto.setBizName("帮助设置");
                bigLogService.updateLog(logDto);

                return Response.ok().message(help.getId()).build();
            }

        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Response deleteNotice(String id) {
        try {
            BgHelp oldHelp = bgHelpService.findOne(id);
            bgHelpService.delete(id);
            //业务日志埋点  删除
            LogDto logDto = new LogDto();
            logDto.setBizCode("delete-help");
            logDto.setDescription("帮助 " + oldHelp.getName());
            logDto.setBizName("帮助设置");
            logDto.setOldData(oldHelp);
            bigLogService.deleteLog(logDto);

            return Response.ok().message("删除成功").build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

}
