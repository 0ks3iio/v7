package net.zdsoft.basedata.dingding.action;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.basedata.dingding.service.DingDingSyncDataService;
import net.zdsoft.basedata.remote.service.DingdingMsgRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.DdMsgUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/syncdata/dingding/")
public class DingDingSyncdataAction {

	@Autowired
	DingDingSyncDataService dingDingSyncDataService;

	@Autowired
	DingdingMsgRemoteService dingdingMsgRemoteService;

	@ControllerInfo("进入钉钉同步数据首页")
	@RequestMapping("/main")
	public String execute(ModelMap map) {

		return "/basedata/dingding/ddSyncData.ftl";
	}

	@RequestMapping("/handlebyHand")
	@ResponseBody
	public String sync2dingdingByHand() {
		try {
			dingDingSyncDataService.dealData2DingDing();
			// dingDingSyncDataService.getDepts();
			// dingDingSyncDataService.getUsers();
			return "同步数据成功";
		} catch (Exception e) {
			e.printStackTrace();
			return "同步数据失败";
		}

	}

	@RequestMapping("/sendMsg")
	@ResponseBody
	public String sendMsg() {
		try {
			String userIds = "6625162926109163|274526391212971|070509503632586843|054966590437910233|0542106530751279";
			String partyIds = "";
			String content = "下午我请大家喝咖啡　阿杜呈上";
			JSONObject textJson = DdMsgUtils.toDingDingTextJson(userIds, partyIds,
					content);
			List<JSONObject> contextList = new ArrayList<JSONObject>();
			contextList.add(textJson);
			dingdingMsgRemoteService.addDingDingMsgs("00EA65CEE57ADE8DE050A8C09B006DCE",
					contextList);
			return "消息发送成功";
		} catch (Exception e) {
			e.printStackTrace();
			return "消息发送失败";
		}

	}

}
