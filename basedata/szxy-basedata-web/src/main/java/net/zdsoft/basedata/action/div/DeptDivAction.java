package net.zdsoft.basedata.action.div;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.service.DeptService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.popup.BaseDivAction;
import net.zdsoft.framework.utils.PinyinUtils;
import net.zdsoft.framework.utils.RedisUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;

@Controller
@RequestMapping("/common/div/dept")
public class DeptDivAction extends BaseDivAction {

	private static String business_key = "dept-popup-";
	private static String teacher_recent_key = "dept-popup-recent-";
	@Autowired
	TeacherService teacherService;

	@Autowired
	DeptService deptService;

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
//			List<Dept> deptList = deptService.findByUnitId(getLoginInfo()
//					.getUnitId());
			Map<String, String> deptNameMap = new HashMap<String, String>();
			int startCode = 10000;
			startCode++;
			String[] dataTop = new String[9];
			dataTop[0] = "00000000000000000000000000000000";
			dataTop[1] = "选择部门……";
			dataTop[2] = "";
			dataTop[3] = "";
			dataTop[4] = "";
			dataTop[5] = TYPE_TOP;
			dataTop[6] = Constant.GUID_ZERO;
			dataTop[7] = "1";
			dataTop[8] = String.valueOf(startCode);
			codeMap.put(business_key + dataTop[0],
					String.valueOf(startCode));
			deptNameMap.put(dataTop[0], dataTop[1]);
			dataList.add(dataTop);

			List<Dept> deptList = deptService.findByUnitId(getLoginInfo()
					.getUnitId());
			startCode = 10000;
			for (Dept d : deptList) {
				startCode++;
				String[] data = new String[9];
				data[0] = d.getId();
				data[1] = d.getDeptName();
				data[2] = d.getDeptCode();
				data[3] = PinyinUtils.toHanyuPinyin(d.getDeptName(),
						false);
				data[4] = PinyinUtils.toHanyuPinyin(d.getDeptName(),
						true);
				data[5] = TYPE_DATA;
				data[6] = "00000000000000000000000000000000";
				data[7] = "2";
				String businessCode = codeMap.get(business_key
						+ "00000000000000000000000000000000")
						+ String.valueOf(startCode);
				codeMap.put(business_key + d.getId(), businessCode);
				data[8] = businessCode;
				dataList.add(data);
			}
			RedisUtils.set(business_key + getLoginInfo().getUnitId(), JsonArray
					.toJSON(dataList).toString(), 30 * 60);
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
