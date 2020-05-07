package net.zdsoft.remote.openapi.action.business;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.exammanage.remote.service.ExamManageRemoteService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.action.OpenApiBaseAction;
import net.zdsoft.remote.openapi.constant.OpenApiConstants;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = { "/remote/openapi", "/openapi" })
public class RemoteExamAction extends OpenApiBaseAction{
	
	@Autowired
	private ExamManageRemoteService examManageRemoteService;
	
	/**
	 * 获取考试列表
	 * @param  acadyear
	 * @param  semester
	 * @param  teacherId
	 */
	@RequestMapping("/exam/list")
	@ResponseBody
	public String getExamList(String acadyear,String  semester, String unitId,String gradeCode, HttpServletRequest request) {
		if (isOverMaxNumDay("examInfoList", request, "/openapi/exam/list")){
        	return returnOpenJsonError(OpenApiBaseAction.MAX_NUM_DAY_ERROR_MSG);
        } 
		doSaveInterfaceCount("examInfoList","/openapi/exam/list",request);
		JSONObject json = new JSONObject();
		json.put("result", OpenApiConstants.REMOTE_JSON_RESULT_CODE_ERROR);
		if(StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)){
			Semester semester1 = getCurrentSemester();
			if (semester1 == null) {
				json.put("resultMsg", "获取不到学年学期数据！");
				return json.toJSONString();
			}
			acadyear = semester1.getAcadyear();
			semester = String.valueOf(semester1.getSemester());
		}
		Set<String> unitSet = getPowerUnitSet(request);
		if(CollectionUtils.isEmpty(unitSet)){
			json.put("resultMsg", "没有授权单位！");
			return json.toJSONString();
		}
		//判断该教师是否在授权范围内
		boolean isTrue = Boolean.FALSE;
		for (String c : unitSet) {
			if(c.equals(unitId))
				isTrue = Boolean.TRUE;
		}
		String data = null;
		if(isTrue){
			try{
				data = examManageRemoteService.getExamList(unitId, gradeCode, acadyear, semester);
			}
			catch(Exception e) {
				json.put("resultMsg" , e.getMessage());
				return json.toJSONString();
			}
		}else{
			json.put("resultMsg", "该教师不在授权单位內！");
			return json.toJSONString();
		}
		json.put("data", data);
		json.put("resultMsg", "调用成功！");
		json.put("result", OpenApiConstants.REMOTE_JSON_RESULT_CODE_SUCCESS);
		return json.toJSONString();
	}
	
	
	/**
	 * 获取考试列表
	 * @param  acadyear
	 * @param  semester
	 * @param  teacherId
	 */
	@RequestMapping("/exam/info")
	@ResponseBody
	public String getExamInfo(String examId,String unitId, HttpServletRequest request) {
		if (isOverMaxNumDay("examInfo", request, "/openapi/exam/info")){
        	return returnOpenJsonError(OpenApiBaseAction.MAX_NUM_DAY_ERROR_MSG);
        } 
		doSaveInterfaceCount("examInfo","/openapi/exam/info",request);
		JSONObject json = new JSONObject();
		json.put("result", OpenApiConstants.REMOTE_JSON_RESULT_CODE_ERROR);
		if(StringUtils.isBlank(examId) || StringUtils.isBlank(unitId)){
			json.put("resultMsg", "必填的参数为空！");
			return json.toJSONString();
		}
		Set<String> unitSet = getPowerUnitSet(request);
		if(CollectionUtils.isEmpty(unitSet)){
			json.put("resultMsg", "没有授权单位！");
			return json.toJSONString();
		}
		//判断该教师是否在授权范围内
		boolean isTrue = Boolean.FALSE;
		for (String c : unitSet) {
			if(c.equals(unitId))
				isTrue = Boolean.TRUE;
		}
		String data = null;
		if(isTrue){
			try{
				data = examManageRemoteService.getExamArrange(examId, unitId);
			}
			catch(Exception e) {
				json.put("resultMsg" , e.getMessage());
				return json.toJSONString();
			}
		}else{
			json.put("resultMsg", "该教师不在授权单位內！");
			return json.toJSONString();
		}
		json.put("data", data);
		json.put("resultMsg", "调用成功！");
		json.put("result", OpenApiConstants.REMOTE_JSON_RESULT_CODE_SUCCESS);
		return json.toJSONString();
	}
	
	/**
	 * @param uri
	 * @param request
	 */
	private void doSaveInterfaceCount(String interfaceType,String uri, HttpServletRequest request) {
		doSaveInterfaceCount(interfaceType, request, uri);
	}
}
