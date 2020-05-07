package net.zdsoft.remote.openapi.action.business;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.action.OpenApiBaseAction;
import net.zdsoft.remote.openapi.constant.OpenApiConstants;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * 获取请假信息接口
 * @author yangsj
 *
 */
@Controller
@RequestMapping(value = { "/remote/openapi", "/openapi" })
public class RemoteLeaveAction extends OpenApiBaseAction{
	
	/**
	 * 获取请假信息列表 (可以传学生id + cardNumber 或 stuCode )
	 * @param  cardNumber
	 * @param  studentId
	 * @param  stuCode
	 */
	@RequestMapping("/stuLeave/info")
	@ResponseBody
	public String getExamList(String cardNumber,String studentId,String stuCode,  HttpServletRequest request) {
		JSONObject json = new JSONObject();
		json.put("result", OpenApiConstants.REMOTE_JSON_RESULT_CODE_ERROR);
		Student student = null;
		if(StringUtils.isNotBlank(stuCode)){
			student = SUtils.dc(studentRemoteService.findByStudentCode(stuCode), Student.class);
		}else if(StringUtils.isNotBlank(studentId)){
			student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
		}
		String data = null;
		if(student != null){
			String unitId = student.getSchoolId();
			try{
				OpenApiOfficeService openApiOfficeService = Evn.getBean("openApiOfficeService");
				if (openApiOfficeService != null) {
					data = openApiOfficeService.getOfficeHwstuByCardNum(student.getCardNumber(), unitId );
				}
			}
			catch(Exception e) {
				json.put("resultMsg" , e.getMessage());
				return json.toJSONString();
			}
			json.put("data", data);
			json.put("resultMsg", "调用成功！");
		}else{
			json.put("resultMsg" , "该学生不存在，请核对数据");
		}
		json.put("result", OpenApiConstants.REMOTE_JSON_RESULT_CODE_SUCCESS);
		return json.toJSONString();
	}
}
