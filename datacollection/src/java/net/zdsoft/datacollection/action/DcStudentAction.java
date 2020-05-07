package net.zdsoft.datacollection.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;

@Controller
@RequestMapping("/dc/student")
public class DcStudentAction extends BaseAction {

	@Autowired
	private ClassRemoteService classRemoteService;

	@Autowired
	private StudentRemoteService studentRemoteService;

	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/object/{id}")
	/**
	 * 学生信息
	 * 
	 * @param id
	 * @return
	 */
	public String student(@PathVariable String id) {
		return studentRemoteService.findOneById(id);
	}
	
	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/objects/{ids}")
	public String teacherByIds(@PathVariable String ids) {
		return studentRemoteService.findListByIds(ids.split(","));
	}
	
	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/objectsWithIds")
	public String objectsWithIds(@RequestParam String ids) {
		return studentRemoteService.findListByIds(ids.split(","));
	}

	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/list/clazz/{classIds}")
	/**
	 * 多个班级学生
	 * 
	 * @param classIds
	 * @return
	 */
	public String listByClassIds(@PathVariable String classIds) {
		return studentRemoteService.findByClassIds(classIds.split(","));
	}
	
	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/list/teachClassStuCount/{teacherId}")
	public String teachClassStuCount(@PathVariable String teacherId){
		List<Clazz> clazzs = Clazz.dt(classRemoteService.findByTeacherId(teacherId));
		List<String> classIdList = EntityUtils.getList(clazzs, "id");
		String s = studentRemoteService.countMapByClassIds(classIdList.toArray(new String[0]));
		Map<String, Integer> map = SUtils.dt(s, new TypeReference<Map<String, Integer>>(){});
		JSONArray array = new JSONArray();
		for(String key : map.keySet()){
			JSONObject json = new JSONObject();
			json.put("classId", key);
			json.put("count", map.get(key));
			array.add(json);
		}
		return array.toJSONString();
	}
	
	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/list/schoolClassStuCount/{schoolId}")
	public String schoolClassStuCount(@PathVariable String schoolId){
		List<Clazz> clazzs = Clazz.dt(classRemoteService.findBySchoolId(schoolId));
		List<String> classIdList = EntityUtils.getList(clazzs, "id");
		String s = studentRemoteService.countMapByClassIds(classIdList.toArray(new String[0]));
		Map<String, Integer> map = SUtils.dt(s, new TypeReference<Map<String, Integer>>(){});
		JSONArray array = new JSONArray();
		for(String key : map.keySet()){
			JSONObject json = new JSONObject();
			json.put("classId", key);
			json.put("count", map.get(key));
			array.add(json);
		}
		return array.toJSONString();
	}
	

}
