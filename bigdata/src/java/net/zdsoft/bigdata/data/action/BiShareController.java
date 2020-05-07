package net.zdsoft.bigdata.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.TreeRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.bigdata.daq.data.component.BizOperationLogCollector;
import net.zdsoft.bigdata.daq.data.entity.BizOperationLog;
import net.zdsoft.bigdata.data.entity.BiShare;
import net.zdsoft.bigdata.data.service.BiShareService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.RedisUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/bigdata/share")
public class BiShareController extends BigdataBaseAction {

    @Resource
    private BiShareService biShareService;
    @Resource
    private UserRemoteService userRemoteService;

    @RequestMapping("/index")
    public String index(Integer type, ModelMap map) {
        if (type == null)
            type = 1;
        map.put("type", type);
        return "/bigdata/user/share/shareIndex.ftl";
    }

    @RequestMapping("/list")
    public String list(Integer type, ModelMap map) {
        List<BiShare> shareList = new ArrayList<BiShare>();
        if (type == 1)
            shareList = biShareService.findBiFavoriteListByShareUserId(
                    getLoginInfo().getUserId(), null);
        else
            shareList = biShareService.findBiFavoriteListByBeShareUserId(
                    getLoginInfo().getUserId(), null);
        map.put("shareList", shareList);
        map.put("type", type);
        return "/bigdata/user/share/shareList.ftl";
    }

    @RequestMapping("/common/bi/index")
    public String listBI(Integer type, ModelMap map,HttpServletRequest request,@RequestParam(required = false) Integer tabType) {
        List<BiShare> shareList = new ArrayList<BiShare>();
        Pagination page = createPagination(request);
        page.setPageSize(10);
        if (type == 1)
            shareList = biShareService.findBiFavoriteListByShareUserId(
                    getLoginInfo().getUserId(), page);
        else
            shareList = biShareService.findBiFavoriteListByBeShareUserId(
                    getLoginInfo().getUserId(), page);
        sendPagination(request, map,"2", page);
        map.put("shareList", shareList);
        map.put("type", type);
        map.put("tabType",tabType);
        return "/bigdata/user/share/shareListBI.ftl";
    }

    @RequestMapping("/component")
    public String component(Integer type, HttpServletRequest request,
                            ModelMap map) {
        List<BiShare> shareList = new ArrayList<BiShare>();
        if (type == 1) {
            shareList = biShareService.findBiFavoriteListByShareUserId(
                    getLoginInfo().getUserId(), null);
        } else {
            Pagination page = createPagination(request);
            page.setPageSize(5);
            shareList = biShareService.findBiFavoriteListByBeShareUserId(
                    getLoginInfo().getUserId(), page);
        }
        map.put("shareList", shareList);
        map.put("type", type);
        if (type == 1)
            return "/bigdata/user/component/shared.ftl";
        else
            return "/bigdata/user/component/beShared.ftl";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Response deleteShare(String id) {
        try {
            biShareService.deleteBiShareByShardUserIdAndBusinessId(
                    getLoginInfo().getUserId(), id);
            return Response.ok().message("取消分享成功").build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/addShare")
    @ResponseBody
    public Response addShare(
            @RequestParam("businessId") String businessId,
            @RequestParam("businessType") String businessType,
            @RequestParam("businessName") String businessName,
            @RequestParam(value = "userArray[]", required = false) String[] userArray) {
        try {
            String key = getLoginInfo().getUserId() + "-user-diy-stat";
            RedisUtils.del(key);
            if (ArrayUtils.isEmpty(userArray)) {
                biShareService.deleteBiShareByShardUserIdAndBusinessId(
                        getLoginInfo().getUserId(), businessId);

                return Response.ok().message("取消分享成功").build();
            }

            String[] orderUserNames = null;
            List<User> userList = userRemoteService.findListObjectByIds(userArray);

            if (userArray.length > 0) {
                orderUserNames = userList.stream().map(User::getUsername).toArray(String[]::new);
            }

            biShareService.addBiShares(businessId, businessType, businessName,
                    getLoginInfo().getUserId(), userArray);

            // 业务日志埋点
            BizOperationLog bizLog = new BizOperationLog();
            bizLog.setLogType(BizOperationLog.LOG_TYPE_OTHER);
            bizLog.setBizCode("model-dataset-auth");
            bizLog.setDescription(businessName + "分享");
            bizLog.setBizName("数据模型管理");
            bizLog.setSubSystem("大数据管理");
            bizLog.setOperator(getLoginInfo().getRealName() + "("
                    + getLoginInfo().getUserName() + ")");
            bizLog.setOperationTime(new Date());
            bizLog.setNewData("名称："
                    + businessName
                    + ";分享用户:"
                    + JSON.toJSONString(orderUserNames != null ? orderUserNames
                    : ""));
            BizOperationLogCollector.submitBizOperationLog(bizLog);

            return Response.ok().message("分享成功").build();
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getAllUser", method = RequestMethod.GET)
    public Response getShareUsers(
            @RequestParam("businessId") String businessId) {
        JSONArray userArray = new JSONArray();
        List<BiShare> biShares = biShareService.findListBy(new String[]{
                "shareUserId", "businessId"}, new String[]{
                getLoginInfo().getUserId(), businessId});
        List<User> orderUsers = userRemoteService.findListObjectByIds(biShares
                .stream().map(BiShare::getBeSharedUserId)
                .toArray(String[]::new));

        for (User u : orderUsers) {
            JSONObject j = new JSONObject();
            j.put("userId", u.getId());
            j.put("userName", u.getRealName());
            userArray.add(j);
        }
        return Response.ok().data(userArray.toJSONString()).build();
    }

}
