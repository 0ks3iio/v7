package net.zdsoft.officework.action;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.FileUtils;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
/**
 * 测试用，模拟华三获取学生在班级数据
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/eccShow")
public class AttTestAction extends BaseAction{
	@RequestMapping("/getStudent/inClass")
	@ResponseBody
	public String inClass(){
		Map<String,String> h3cPropertiesMap = Maps.newHashMap();
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource h3cResource = null;
		try {
			h3cResource = new FileSystemResource("/opt/server_data/v7/test.properties");
		} catch (Exception e) {
		}
		
		if(!h3cResource.isReadable()){
			h3cResource = resourceLoader.getResource("conf/test.properties");
		}else{
		}
		
		if(!h3cResource.isReadable())
			return null;
		
		try {
			Properties p =FileUtils.readProperties(h3cResource.getInputStream());
			
			for (Object key : p.keySet()) {
	            String value = p.getProperty(key.toString());
	            h3cPropertiesMap.put(key.toString(), value);
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject json = new JSONObject();
		json.put("code", 1);
		json.put("message", "ok");
		JSONArray data = new JSONArray();
		String testStuInclass = h3cPropertiesMap.get("stu.inclass.test");
		String[] aa = testStuInclass.split(",");
		for(String a:aa){
			String[] bb = a.split("-");
			JSONObject js = new JSONObject();
			js.put("studentId", bb[0]);
			js.put("inClassroom", bb[1]);
			data.add(js);
		}
		json.put("data", data);
		
		return json.toJSONString();
	}

}
