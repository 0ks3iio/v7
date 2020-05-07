package net.zdsoft.basedata.action.div;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.service.SchoolService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.popup.BaseDivAction;
import net.zdsoft.framework.utils.PinyinUtils;
import net.zdsoft.framework.utils.RedisUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yangsj  2018年7月13日上午10:48:40
 */
@Controller
@RequestMapping("/common/div/unit")
public class UnitDivAction extends BaseDivAction {

	Logger logger = Logger.getLogger(UnitDivAction.class);
	
	private static String business_key = "unit-popup-";
	private static String unit_recent_key = "unt-popup-recent-";
//	private static String unit_by_edu_key = "unitByEdu-popup-";
	@Autowired
	SchoolService schoolService;
	@Autowired
	UnitService unitService;
	
	
//	/**
//	 * 按照班级显示学生 名称 搜索显示tip　在搜索的时候提示的一些信息　比如搜索学生的时候　可以看到班级 全拼 模糊搜索用 首字母拼音　模糊搜索用
//	 * 上一级id 第一级目录的上一级id为32个0 类型　 类型　第一级是top 下级的sub 数据data 历史搜索data-history
//	 * code　100001000010000 从上往下有关联关系 level 第几层　第一级为1 第二级为2...
//	 * String[]{"id","名称","搜索显示tip","全拼","首字母拼音","类型","上一级id","code","level"}
//	 */
//	@RequestMapping("/popupData/edu")
//	@ResponseBody
//	public String showPopUpDataByEduId() {
//		String dataJson = RedisUtils.get(unit_by_edu_key
//				+ getLoginInfo().getUnitId());
//		if (dataJson == null) {
//			List<String[]> dataList = new LinkedList<>();
//			// －－－－－－－－－－－－－－－－－－－学生－－－－－－－－－－－－－－－－－－－－－－－－－
//			int startCode = 10000;
//			int level = 1;
//			Unit unit = unitService.findOne(getLoginInfo().getUnitId());
//			List<Unit> units = new ArrayList<>();
//			if(unit.getUnitClass().equals(Unit.UNIT_CLASS_EDU)) {
//				units.addAll(unitService.findByParentIdAndUnitClass(unit.getId(), Unit.UNIT_MARK_NORAML, Unit.UNIT_CLASS_SCHOOL));
//			}else {
//				units.addAll(unitService.findByParentIdAndUnitClass(unit.getParentId(), Unit.UNIT_MARK_NORAML, Unit.UNIT_CLASS_SCHOOL));
//			}
//			for (Unit school : units) {
//				startCode++;
//				String[] data = new String[9];
//				data[0] = school.getId();
//				data[1] = school.getUnitName();
//				data[2] = "";
//				data[3] = PinyinUtils
//						.toHanyuPinyin(school.getUnitName(), false);
//				data[4] = PinyinUtils.toHanyuPinyin(school.getUnitName(), true);
//				data[5] = TYPE_DATA;
//				data[6] = school.getParentId();
//				data[7] = String.valueOf(level);
//				data[8] = String.valueOf(startCode);
//				dataList.add(data);
//			}
//			RedisUtils.set(unit_by_edu_key
//					+ getLoginInfo().getUnitId(), JsonArray.toJSON(dataList)
//					.toString(), 30 * 60);
//			dataJson = JsonArray.toJSON(dataList).toString();
//		}
//		List<String> resultList = new LinkedList<>();
//		resultList.add(dataJson);
//
//		return JsonArray.toJSON(resultList).toString();
//	}
	
	/**
	 * 单位选择的结构如下 教育局－－>学校id 名称 搜索显示tip　在搜索的时候提示的一些信息　比如搜索学生的时候　可以看到班级
	 * 全拼 模糊搜索用 首字母拼音　模糊搜索用 上一级id 第一级目录的上一级id为32个0 类型　 类型　第一级是top 下级的sub 数据data
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
			//先得到当前的教育局的region_leave
			Unit u1 = unitService.findOne(getLoginInfo().getUnitId());
			List<Unit> edUnits = unitService.findByUnionId(u1.getUnionCode(), Unit.UNIT_MARK_NORAML, Unit.UNIT_CLASS_EDU);
			int startCode = 10000;
			int level = 1;
			// -------------------------教育局做为第一级---------------------------------
			for (Unit unit : edUnits) {
				startCode++;
				String[] data = new String[9];
				data[0] = unit.getId();
				data[1] = unit.getUnitName();
				data[2] = "";
				data[3] = PinyinUtils.toHanyuPinyin(unit.getUnitName(),
						false);
				data[4] = PinyinUtils.toHanyuPinyin(unit.getUnitName(),
						true);
				data[5] = TYPE_TOP;
				data[6] = Constant.GUID_ZERO;
				data[7] = String.valueOf(level);
				data[8] = String.valueOf(startCode);
				codeMap.put(business_key + unit.getId(),
						String.valueOf(startCode));
				dataList.add(data);
			}
			level++;
			// －－－－－－－－－－－－－－－－－－－学校－－－－－－－－－－－－－－－－－－－－－－－－－
			startCode = 10000;
			List<Unit> schoolUnits = unitService.findByUnionId(u1.getUnionCode(), Unit.UNIT_MARK_NORAML, Unit.UNIT_CLASS_SCHOOL);
			startCode = 10000;
			for (Unit school : schoolUnits) {
				startCode++;
				String[] data = new String[9];
				data[0] = school.getId();
				data[1] = school.getUnitName();
				data[2] = "";
				data[3] = PinyinUtils.toHanyuPinyin(
						school.getUnitName(), false);
				data[4] = PinyinUtils.toHanyuPinyin(
						school.getUnitName(), true);
				data[5] = TYPE_DATA;
				data[6] = school.getParentId();
				data[7] = String.valueOf(level);
				String businessCode = codeMap.get(business_key
						+ school.getParentId())
						+ String.valueOf(startCode);
				codeMap.put(business_key + school.getId(), businessCode);
				data[8] = businessCode;
				dataList.add(data);
			}
			for (Unit unit : edUnits) {
				startCode++;
				String[] data = new String[9];
				data[0] = unit.getId();
				data[1] = unit.getUnitName();
				data[2] = "";
				data[3] = PinyinUtils.toHanyuPinyin(unit.getUnitName(),
						false);
				data[4] = PinyinUtils.toHanyuPinyin(unit.getUnitName(),
						true);
				data[5] = TYPE_DATA;
				data[6] = unit.getId();
				data[7] = String.valueOf(level);
				String businessCode = codeMap.get(business_key
						+ unit.getId())
						+ String.valueOf(startCode);
				codeMap.put(business_key + unit.getId(),
						String.valueOf(businessCode));
				data[8] = businessCode;
				dataList.add(data);
			}
			RedisUtils.set(business_key + getLoginInfo().getUnitId(), JsonArray
					.toJSON(dataList).toString(), 30 * 60);
			dataJson = JsonArray.toJSON(dataList).toString();
		}
		List<String> recentDataList = RedisUtils
				.queryDataFromList(unit_recent_key
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
		String[] unitIds = ids.split(",");
		for (int i = 0; i < unitIds.length; i++) {
			RedisUtils.addDataToList(unit_recent_key
					+ getLoginInfo().getUserId(), unitIds[i], MAX_COUNT);
		}
		return "";
	}

}
