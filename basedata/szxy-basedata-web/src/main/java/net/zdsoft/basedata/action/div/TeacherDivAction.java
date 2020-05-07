package net.zdsoft.basedata.action.div;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.service.DeptService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.popup.BaseDivAction;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PinyinUtils;
import net.zdsoft.framework.utils.RedisUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/common/div/teacher")
public class TeacherDivAction extends BaseDivAction {

	private static String business_key = "teacher-popup-";
	private static String teacher_recent_key = "teacher-popup-recent-";
	@Autowired
	TeacherService teacherService;

	@Autowired
	DeptService deptService;
	
	@Autowired
	UserService userService;

	/**
	 * 教师选择的结构如下 部门－－>教师 id 名称 搜索显示tip　在搜索的时候提示的一些信息　比如搜索教师的时候　可以看到部门 全拼 模糊搜索用
	 * 首字母拼音　模糊搜索用 上一级id 第一级目录的上一级id为32个0 类型　 类型　第一级是top 下级的sub 数据data
	 * 历史搜索data-history code　100001000010000 从上往下有关联关系 level 第几层　第一级为1 第二级为2...
	 * String[]{"id","名称","搜索显示tip","全拼","首字母拼音","类型","上一级id","code","level"}
	 */
	@RequestMapping("/popupData")
	@ResponseBody
	public String showPopUpData() {
		String dataJson = RedisUtils.get(business_key
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
				codeMap.put(business_key + dept.getId(),
						String.valueOf(startCode));
				deptNameMap.put(dept.getId(), dept.getDeptName());
				dataList.add(data);
			}

			List<Teacher> teacherList = teacherService
					.findByUnitId(getLoginInfo().getUnitId());
			List<User> userList = userService.findByOwnerType(new String[]{getLoginInfo().getUnitId()}, User.OWNER_TYPE_TEACHER);
			Map<String, Integer> userStateMap = EntityUtils.getMap(userList, User::getOwnerId,User::getUserState);
			startCode = 10000;
			for (Teacher teacher : teacherList) {
				if(userStateMap.get(teacher.getId())!=null&&userStateMap.get(teacher.getId())!=User.USER_MARK_NORMAL){
					continue;
				}
				startCode++;
				String[] data = new String[9];
				data[0] = teacher.getId();
				data[1] = teacher.getTeacherName();
				data[2] = deptNameMap.get(teacher.getDeptId()) == null ? ""
						: deptNameMap.get(teacher.getDeptId());
				data[3] = PinyinUtils.toHanyuPinyin(teacher.getTeacherName(),
						false);
				data[4] = PinyinUtils.toHanyuPinyin(teacher.getTeacherName(),
						true);
				data[5] = TYPE_DATA;
				data[6] = teacher.getDeptId();
				data[7] = "2";
				String businessCode = codeMap.get(business_key
						+ teacher.getDeptId())
						+ String.valueOf(startCode);
				codeMap.put(business_key + teacher.getId(), businessCode);
				data[8] = businessCode;
				dataList.add(data);
			}
			RedisUtils.set(business_key + getLoginInfo().getUnitId(), JsonArray
					.toJSON(dataList).toString(), RedisUtils.TIME_FIVE_SECONDS);
			dataJson = JsonArray.toJSON(dataList).toString();
		}
		List<String> recentDataList = RedisUtils
				.queryDataFromList(teacher_recent_key
						+ getLoginInfo().getUserId(),true);
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
		String[] teacherIds = ids.split(",");
		for (int i = 0; i < teacherIds.length; i++) {
			RedisUtils.addDataToList(teacher_recent_key
					+ getLoginInfo().getUserId(), teacherIds[i], MAX_COUNT);
		}
		return "";
	}
}
