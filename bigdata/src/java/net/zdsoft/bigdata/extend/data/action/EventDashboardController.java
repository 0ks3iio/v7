package net.zdsoft.bigdata.extend.data.action;

import com.alibaba.fastjson.JSON;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.extend.data.entity.EventFavorite;
import net.zdsoft.bigdata.extend.data.entity.EventGroup;
import net.zdsoft.bigdata.extend.data.entity.EventGroupFavorite;
import net.zdsoft.bigdata.extend.data.service.EventFavoriteParamService;
import net.zdsoft.bigdata.extend.data.service.EventFavoriteService;
import net.zdsoft.bigdata.extend.data.service.EventGroupFavoriteService;
import net.zdsoft.bigdata.extend.data.service.EventGroupService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/bigdata/event")
public class EventDashboardController extends BigdataBaseAction {

    @Autowired
    private EventGroupService eventGroupService;

    @Autowired
    private EventFavoriteService eventFavoriteService;

    @Autowired
    private EventGroupFavoriteService eventGroupFavoriteService;

    @Autowired
    private EventFavoriteParamService eventFavoriteParamService;

    @Autowired
    private OptionService optionService;

    @ControllerInfo("进入dashborad页面")
    @RequestMapping("/dashboard")
    public String index(ModelMap map) {
        OptionDto druidDto = optionService.getAllOptionParam("druid");
        if (druidDto == null || druidDto.getStatus() == 0) {
            map.put("serverName", "druid");
            map.put("serverCode", "druid");
            return "/bigdata/noServer.ftl";
        }
        return "/bigdata/extend/event/eventDashboard.ftl";
    }

    @ControllerInfo("加载groupList页面")
    @RequestMapping("/dashboard/group/list")
    public String groupList(ModelMap map) {
        List<EventGroup> groupList = eventGroupService
                .findGroupListByUserId(getLoginInfo().getUserId());
        if (CollectionUtils.isEmpty(groupList))
            groupList = new ArrayList<EventGroup>();
        map.put("groupList", groupList);
        return "/bigdata/extend/event/eventGroupList.ftl";
    }

    @ControllerInfo("加载没有group的页面")
    @RequestMapping("/dashboard/group/list/nodata")
    public String noGroupData(ModelMap map) {
        return "/bigdata/extend/event/noGroupData.ftl";
    }

    @ControllerInfo("加载面板下收藏的页面")
    @RequestMapping("/dashboard/favorite/list")
    public String favoriteList(String groupId, ModelMap map) {
        map.put("groupId", groupId);
        List<EventGroupFavorite> groupFavoriteList = eventGroupFavoriteService
                .findFavoriteListByGroupId(groupId);
        List<EventFavorite> favoriteList = eventFavoriteService
                .findByUserId(getLoginInfo().getUserId());
        List<EventFavorite> resultList = new ArrayList<EventFavorite>();
        Map<String, EventFavorite> favoriteMap = new HashMap<String, EventFavorite>();

        for (EventFavorite favorite : favoriteList) {
            favoriteMap.put(favorite.getId(), favorite);
        }
        for (EventGroupFavorite groupFavorite : groupFavoriteList) {
            if (favoriteMap.containsKey(groupFavorite.getFavoriteId())) {
                resultList.add(favoriteMap.get(groupFavorite.getFavoriteId()));
            }
        }

        resultList.sort(new Comparator<EventFavorite>() {
            @Override
            public int compare(EventFavorite o1, EventFavorite o2) {
                if (o1.getOrderId() != null && o2.getOrderId() != null) {
                    return o1.getOrderId().compareTo(o2.getOrderId());
                } else {
                    return 1;
                }
            }
        });
        map.put("favoriteList", resultList);
        return "/bigdata/extend/event/eventFavoriteList.ftl";
    }

    @ControllerInfo("加载面板下收藏的页面,过滤掉已经显示的收藏")
    @RequestMapping("/dashboard/favorite/menuList")
    public String favoriteMenuList(String groupId, ModelMap map) {
        map.put("groupId", groupId);
        List<EventGroupFavorite> groupFavoriteList = eventGroupFavoriteService
                .findFavoriteListByGroupId(groupId);
        List<EventFavorite> favoriteList = eventFavoriteService
                .findByUserId(getLoginInfo().getUserId());
        List<String> favoriteIds = new LinkedList<String>();
        for (EventGroupFavorite groupFavorite : groupFavoriteList) {
            favoriteIds.add(groupFavorite.getFavoriteId());
        }
        List<EventFavorite> resultList = new ArrayList<EventFavorite>();
        for (EventFavorite favorite : favoriteList) {
            if (!favoriteIds.contains(favorite.getId())) {
                Map<String, String> param = eventFavoriteParamService
                        .getMapByFavoriteId(favorite.getId());
                if (param.get("timeInfo") == null) {
                    favorite.setTimeInfo(DateUtils.date2StringByDay(favorite.getBeginDate()) + "~" + DateUtils.date2StringByDay(favorite.getEndDate()));
                } else {
                    favorite.setTimeInfo(param.get("timeInfo"));
                }
                resultList.add(favorite);
            }
        }
        map.put("favoriteList", resultList);
        return "/bigdata/extend/event/eventFavoriteMenuList.ftl";
    }

    @ControllerInfo("加载面板下收藏的detail页面")
    @RequestMapping("/dashboard/favorite/detail")
    public String favoriteDetail(String favoriteId, String groupId, ModelMap map) {
        map.put("groupId", groupId);
        EventFavorite favorite = eventFavoriteService.findOne(favoriteId);
        Map<String, String> param = eventFavoriteParamService
                .getMapByFavoriteId(favorite.getId());
        favorite.setTimeInfo(param.get("timeInfo"));
        if (StringUtils.isNotBlank(param.get("contrastTimeInterval"))) {
            map.put("compareTimeIntervalList", JSON.parseObject(param.get("contrastTimeInterval"), String[].class));
        }
        map.put("favorite", favorite);
        return "/bigdata/extend/event/eventFavoriteDetail.ftl";
    }

    @ControllerInfo("加载收藏的设置页面")
    @RequestMapping("/dashboard/favorite/set")
    public String favoriteSet(String farvirateId, ModelMap map) {
        return "/bigdata/extend/event/favoriteSet.ftl";
    }

    @ControllerInfo("加载组的设置页面")
    @RequestMapping("/dashboard/group/edit")
    public String groupEdit(String groupId, ModelMap map) {
        EventGroup group = new EventGroup();

        if (StringUtils.isNotBlank(groupId)) {
            group = eventGroupService.findOne(groupId);
        } else {
            Integer maxOrderId = eventGroupService
                    .getMaxOrderIdByUserId(getLoginInfo().getUserId());
            if (maxOrderId == null)
                maxOrderId = 0;
            if (maxOrderId >= 999)
                maxOrderId = 0;
            group.setOrderId(maxOrderId + 1);
        }
        map.put("group", group);
        return "/bigdata/extend/event/eventGroupEdit.ftl";
    }

    @ControllerInfo("保存或者修改组的设置")
    @RequestMapping("/dashboard/group/save")
    @ResponseBody
    public String saveGroup(EventGroup group) {
        try {
            group.setUnitId(getLoginInfo().getUnitId());
            group.setUserId(getLoginInfo().getUserId());
            eventGroupService.saveOrUpdateGroup(group);
            return success("保存组成功!");
        } catch (Exception e) {
            return error("保存组失败:" + e.getMessage());
        }
    }

    @ControllerInfo("组增加收藏")
    @RequestMapping("/dashboard/group/favorite/add")
    @ResponseBody
    public String addGroupFavorite(String groupId, String favoriteId) {
        try {
            List<EventGroupFavorite> favoriteList = new ArrayList<EventGroupFavorite>();
            EventGroupFavorite groupFavorite = new EventGroupFavorite();
            groupFavorite.setId(UuidUtils.generateUuid());
            groupFavorite.setGroupId(groupId);
            groupFavorite.setFavoriteId(favoriteId);
            favoriteList.add(groupFavorite);
            eventGroupFavoriteService.saveFavorite2Group(favoriteList);
            return success("添加成功");
        } catch (Exception e) {
            return error("添加失败:" + e.getMessage());
        }
    }

    @ControllerInfo("删除组")
    @RequestMapping("/dashboard/group/delete")
    @ResponseBody
    public String deleteGroup(String groupId) {
        try {
            eventGroupService.delete(groupId);
            // 删除相关的信息
            eventGroupFavoriteService.deleteByGroupId(groupId);
            return success("删除组成功");
        } catch (Exception e) {
            return error("删除组失败:" + e.getMessage());
        }
    }

    @ControllerInfo("删除组下的组件")
    @RequestMapping("/dashboard/group/favorite/delete")
    @ResponseBody
    public String deleteGroupFavorite(String groupId, String favoriteId) {
        try {
            eventGroupFavoriteService.deleteByGroupAndFarvoriteId(groupId,
                    favoriteId);
            return success("删除成功");
        } catch (Exception e) {
            return error("删除失败:" + e.getMessage());
        }
    }

    @ControllerInfo("修改窗口大小")
    @RequestMapping("/dashboard/favoreite/changeWindowSize")
    @ResponseBody
    public String changeWindowSize(String id, int windowSize) {
        try {
            eventFavoriteService.updateFavoriteWindowSize(id, windowSize);
            return success("修改成功");
        } catch (Exception e) {
            return error("修改失败:" + e.getMessage());
        }
    }
}
