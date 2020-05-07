package net.zdsoft.basedata.action.div;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.service.DeptService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.popup.BaseDivAction;
import net.zdsoft.framework.utils.PinyinUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/common/div/user")
public class UserDivAction extends BaseDivAction {
	Logger logger = Logger.getLogger(UserDivAction.class);

	private static String business_key = "user-popup-";
	private static String user_recent_key = "user-popup-recent-";

	@Autowired
	UserService userService;

	@Autowired
	DeptService deptService;

	@ControllerInfo("弹出层测试页面")
	@RequestMapping("/test")
	public String test() {
		return "/fw/commonftl/commonDiv.ftl";
	}

	/**
	 * 教师选择的结构如下 部门－－>用户 id 名称 搜索显示tip　在搜索的时候提示的一些信息　比如搜索教师的时候　可以看到部门 全拼 模糊搜索用
	 * 首字母拼音　模糊搜索用 上一级id 第一级目录的上一级id为32个0 类型　 类型　第一级是top 下级的sub 数据data
	 * 历史搜索data-history code　100001000010000 从上往下有关联关系 level 第几层　第一级为1 第二级为2...
	 * String[]{"id","名称","搜索显示tip","全拼","首字母拼音","类型","上一级id","code","level"}
	 */
	@RequestMapping("/popupData")
	@ResponseBody
	public String showPopUpData() {
		String ownerType = getRequest().getParameter("ownerType");
		if (StringUtils.isBlank(ownerType)) {
			ownerType = "";
		}
		String businessKey = business_key + ownerType;
		String userRecentKey = user_recent_key + ownerType;
		String dataJson = RedisUtils.get(businessKey
				+ getLoginInfo().getUnitId());
		if (dataJson == null) {
			List<String[]> dataList = new LinkedList<String[]>();
			List<Dept> deptList = deptService.findByUnitId(getLoginInfo()
					.getUnitId());
			Map<String, String> deptNameMap = new HashMap<String, String>();
			int startCode = 10000;
			for (Dept dept : deptList) {
				startCode++;
				String[] data = new String[9];
				data[0] = dept.getId();
				data[1] = dept.getDeptName();
				data[2] = "";
				data[3] = PinyinUtils.toHanyuPinyin(dept.getDeptName(), false);
				data[4] = PinyinUtils.toHanyuPinyin(dept.getDeptName(), true);
				data[5] = TYPE_TOP;
				data[6] = Constant.GUID_ZERO;
				data[7] = "1";
				data[8] = String.valueOf(startCode);
				codeMap.put(businessKey + dept.getId(),
						String.valueOf(startCode));
				deptNameMap.put(dept.getId(), dept.getDeptName());
				dataList.add(data);
			}

			List<User> userList = new ArrayList<User>();
			if (StringUtils.isBlank(ownerType)) {
				userList = userService.findByUnitId(getLoginInfo().getUnitId());
			} else {
				userList = userService.findByOwnerType(
						new String[] { getLoginInfo().getUnitId() },
						User.OWNER_TYPE_TEACHER);
			}
			startCode = 10000;
			for (User user : userList) {
				if(User.USER_MARK_NORMAL!=user.getUserState()){
					continue;
				}
				startCode++;
				String[] data = new String[9];
				data[0] = user.getId();
				data[1] = user.getRealName();
				data[2] = deptNameMap.get(user.getDeptId()) == null ? ""
						: deptNameMap.get(user.getDeptId());
				data[3] = PinyinUtils.toHanyuPinyin(user.getRealName(), false);
				data[4] = PinyinUtils.toHanyuPinyin(user.getRealName(), true);
				data[5] = TYPE_DATA;
				data[6] = user.getDeptId();
				data[7] = "2";
				String businessCode = codeMap.get(businessKey
						+ user.getDeptId())
						+ String.valueOf(startCode);
				codeMap.put(businessKey + user.getId(), businessCode);
				data[8] = businessCode;
				dataList.add(data);
			}
			RedisUtils.set(businessKey + getLoginInfo().getUnitId(), JsonArray
					.toJSON(dataList).toString(), 30 * 60);
			dataJson = JsonArray.toJSON(dataList).toString();
		}
		List<String> recentDataList = RedisUtils.queryDataFromList(
				userRecentKey + getLoginInfo().getUserId(), true);
		if (CollectionUtils.isEmpty(recentDataList))
			recentDataList = new ArrayList<String>();
		String recentDataJson = JsonArray.toJSON(recentDataList).toString();
		List<String> resultList = new LinkedList<String>();
		resultList.add(dataJson);
		resultList.add(recentDataJson);
		return JsonArray.toJSON(resultList).toString();
	}

	@RequestMapping("/recentData")
	@ResponseBody
	@ControllerInfo(value = "获取最近的数据", parameter = "{ids}")
	public String putRecentPopUpData(String ids) {
		String ownerType = getRequest().getParameter("ownerType");
		if (StringUtils.isBlank(ownerType)) {
			ownerType = "";
		}
		String userRecentKey = user_recent_key + ownerType;
		String[] userIds = ids.split(",");
		for (int i = 0; i < userIds.length; i++) {
			RedisUtils.addDataToList(userRecentKey
					+ getLoginInfo().getUserId(), userIds[i], MAX_COUNT);
		}
		return "";
	}
}
