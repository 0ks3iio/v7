package net.zdsoft.officework.action.div;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.popup.BaseDivAction;
import net.zdsoft.framework.utils.PinyinUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.officework.dto.ClassRoomDto;
import net.zdsoft.officework.service.OfficeClassroomRelevanceService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/officework/classroom")
public class OfficeClassRoomDivAction  extends BaseDivAction{
	Logger logger = Logger.getLogger(OfficeClassRoomDivAction.class);
	
	private static String business_key = "classroom-popup-";
	private static String user_recent_key = "classroom-popup-recent-";
	

	@Autowired
	private OfficeClassroomRelevanceService officeClassroomRelevanceService;

	
	@RequestMapping("/popupData")
	@ResponseBody
	public String showPopUpData() {
		String unitId = getLoginInfo().getUnitId();
		List<String[]> dataList = new LinkedList<String[]>();
		List<ClassRoomDto> roomDtos = officeClassroomRelevanceService.getClassRoomList(unitId);
		int startCode = 10000;
		for (ClassRoomDto dto : roomDtos) {
			startCode++;
			String[] data = new String[9];
			data[0] = dto.getClassRoomId();
			data[1] = dto.getClassRoomName();
			data[2] = "";
			data[3] = PinyinUtils.toHanyuPinyin(dto.getClassRoomName(),
					false);
			data[4] = PinyinUtils.toHanyuPinyin(dto.getClassRoomName(),
					true);
			data[5] = TYPE_DATA;
			data[6] = Constant.GUID_ZERO;
			data[7] = "1";
			data[8] = String.valueOf(startCode);
			dataList.add(data);
		}
		String dataJson = JsonArray.toJSON(dataList).toString();
		List<String> recentDataList = RedisUtils
				.queryDataFromList(user_recent_key
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
		String[] classRoomIds = ids.split(",");
		for (int i = 0; i < classRoomIds.length; i++) {
			RedisUtils.addDataToList(user_recent_key
					+ getLoginInfo().getUserId(), classRoomIds[i], MAX_COUNT);
		}
		return "";
	}

}
