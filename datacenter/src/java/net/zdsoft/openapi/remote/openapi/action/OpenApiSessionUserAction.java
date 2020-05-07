package net.zdsoft.openapi.remote.openapi.action;

import java.util.Collections;
import java.util.Set;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.RedisUtils;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/remote/openapi")
public class OpenApiSessionUserAction extends BaseAction {
	
	@RequestMapping("/sessionUserCount")
	@ResponseBody
	public String querySessionUserCount(@RequestParam(defaultValue="0", required=false) int type) {
		JSONObject json = new JSONObject();
		Set<String> set = RedisUtils.getSet(Constant.SESSION_COUNT_PREFIX_SET, () -> Collections.EMPTY_SET);
		int count = 0;
		for(String x : set) {
			String size = RedisUtils.get(Constant.SESSION_COUNT_PREFIX + x);
			count += NumberUtils.toInt(size, 0);
		}
		if(type == 2) {
			JSONArray array = new JSONArray();
			json.put("y", count);
			json.put("x", "在线数");
			json.put("s", "统计");
			array.add(json);
			return array.toJSONString();
		}
		else {
			json.put("value", count);
			return json.toJSONString();
		}
	}
}
