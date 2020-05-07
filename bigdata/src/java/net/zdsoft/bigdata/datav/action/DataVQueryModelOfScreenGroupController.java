package net.zdsoft.bigdata.datav.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datav.entity.Screen;
import net.zdsoft.bigdata.datav.entity.ScreenGroup;
import net.zdsoft.bigdata.datav.service.ScreenGroupService;
import net.zdsoft.bigdata.datav.service.ScreenService;
import net.zdsoft.bigdata.datav.vo.ScreenGroupLayerScreenVo;
import net.zdsoft.bigdata.datav.vo.ScreenGroupVo;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author shenke
 * @since 2018/11/14 下午4:10
 */
@Controller
@RequestMapping("/bigdata/cockpitQuery")
public class DataVQueryModelOfScreenGroupController  extends BigdataBaseAction{

    @Resource
    private ScreenService screenService;

    /**
     * screenGroup  screenIds 分隔符
     */
    private static final String SCREEN_GROUP_SCREEN_ID_SPLIT = ",";

    @Autowired
    private ScreenGroupService screenGroupService;
    @Autowired
    private OptionService optionService;

    private String fileUrl;

    private boolean isNewStyle() {
        OptionDto styleDto = optionService.getAllOptionParam("pageStyle");
        String style = styleDto.getFrameParamMap().get("style");
        return "2".equals(style);
    }

    @Autowired
    public DataVQueryModelOfScreenGroupController(SysOptionRemoteService sysOptionRemoteService, HttpServletRequest request) {
        this.fileUrl = sysOptionRemoteService.getFileUrl(request.getServerName());
    }

    @GetMapping("groupIndex")
    public String groupIndex(ModelMap model, HttpServletRequest request) {
        LoginInfo loginInfo = (LoginInfo) request.getSession().getAttribute("loginInfo");
        List<ScreenGroup> groups = screenGroupService.getGroupsByUserId(loginInfo.getUserId());
        model.addAttribute("groupVos", cleanScreenGroups(groups, loginInfo.getUnitId(), loginInfo.getUserId()));
        model.addAttribute("fileUrl", fileUrl);
        if (isNewStyle()) {
            return "/bigdata/datav/model/new/new-group-index.ftl";
        }
        return "/bigdata/datav/model/groupIndex.ftl";
    }

    private List<ScreenGroupVo> cleanScreenGroups(List<ScreenGroup> groups, String unitId, String userId) {
        List<ScreenGroupVo> groupVos = new ArrayList<>(groups.size());
        List<String> emptyGroupVos = new ArrayList<>();
        List<ScreenGroup> modifyGroups = new ArrayList<>();
        for (ScreenGroup group : groups) {
            ScreenGroupVo groupVo = new ScreenGroupVo();
            groupVo.setName(group.getName());
            if (StringUtils.isNotBlank(group.getScreenIds())) {
                String[] screenIds = StringUtils.split(group.getScreenIds(), SCREEN_GROUP_SCREEN_ID_SPLIT);
                List<Screen> screens = screenService.getScreensByUnitIdAndUserIdForGroup(unitId, userId, screenIds);
                if (screens.isEmpty()) {
                    emptyGroupVos.add(group.getId());
                } else {
                    Set<String> screenIdSet = new HashSet<>(Arrays.asList(screenIds));
                    Map<String, ScreenGroupVo.GroupScreenVo> groupScreenVoMap = screens.stream().collect(Collectors.toMap(Screen::getId, e -> {
                        ScreenGroupVo.GroupScreenVo groupScreenVo = new ScreenGroupVo.GroupScreenVo();
                        groupScreenVo.setId(e.getId());
                        groupScreenVo.setUnitId(e.getUnitId());
                        return groupScreenVo;
                    }));
                    groupVo.setGroupScreenVos(new ArrayList<>(groupScreenVoMap.values()));
                    screenIdSet.removeAll(groupScreenVoMap.keySet());
                    if (!screenIdSet.isEmpty()) {
                        group.setScreenIds(StringUtils.join(groupScreenVoMap.keySet().toArray(new String[0]), SCREEN_GROUP_SCREEN_ID_SPLIT));
                        modifyGroups.add(group);
                    }
                }
            } else {
                emptyGroupVos.add(group.getId());
            }
            groupVo.setId(group.getId());
            groupVos.add(groupVo);
        }
        //
        if (!modifyGroups.isEmpty()) {
            screenGroupService.saveAll(modifyGroups.toArray(new ScreenGroup[0]));
        }
        //
        return groupVos;
    }

    @GetMapping("groupLayer")
    public String groupLayer(String groupId, ModelMap model, HttpServletRequest request) {
        LoginInfo loginInfo = (LoginInfo) request.getSession().getAttribute("loginInfo");
        List<Screen> screenList = screenService.getScreensByUnitIdAndUserId(loginInfo.getUnitId(), loginInfo.getUserId());

        Set<String> beUsedScreenIdSet = new HashSet<>();
        if (StringUtils.isNotBlank(groupId)) {
            ScreenGroup screenGroup = screenGroupService.findOne(groupId);
            model.addAttribute("screenGroup", screenGroup);
            String[] screenIds = StringUtils.split(screenGroup.getScreenIds(), SCREEN_GROUP_SCREEN_ID_SPLIT);
            if (screenIds != null) {
                beUsedScreenIdSet.addAll(Arrays.asList(screenIds));
            }
        } else {
            model.addAttribute("screenGroup", new ScreenGroup());
        }

        model.addAttribute("screens",
                screenList.stream().filter(e->StringUtils.isBlank(e.getUrl())).map(e -> {
                    ScreenGroupLayerScreenVo screenGroupLayerScreenVo = new ScreenGroupLayerScreenVo();
                    screenGroupLayerScreenVo.setScreenId(e.getId());
                    screenGroupLayerScreenVo.setScreenName(e.getName());
                    screenGroupLayerScreenVo.setSelected(beUsedScreenIdSet.contains(e.getId()));
                    screenGroupLayerScreenVo.setShotUrl(fileUrl + "/bigdata/datav/" + e.getUnitId() + "/" + e.getId() + ".jpg");
                    return screenGroupLayerScreenVo;
                }).collect(Collectors.toList())
        );
        if (isNewStyle()) {
            return "/bigdata/datav/model/new/new-group-layer.ftl";
        }
        return "/bigdata/datav/model/group.ftl";
    }

    @ResponseBody
    @PostMapping("screenGroup")
    public Response doUpdateScreenGroup(ScreenGroup screenGroup, HttpServletRequest request) {
        if (StringUtils.isBlank(screenGroup.getId())) {
            LoginInfo loginInfo = (LoginInfo) request.getSession().getAttribute("loginInfo");
            screenGroup.setId(UuidUtils.generateUuid());
            screenGroup.setCreateUserId(loginInfo.getUserId());
        }
        screenGroupService.save(screenGroup);
        return Response.ok().message("保存成功").build();
    }

    @ResponseBody
    @GetMapping("/delete/screenGroup/{groupId}")
    public Response doDeleteScreenGroup(@PathVariable("groupId") String id) {
        screenGroupService.delete(id);
        return Response.ok().message("删除成功").build();
    }
}
