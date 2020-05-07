package net.zdsoft.remote.openapi.action;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.RemoteCallUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.service.OpenApiTacherAttendanceService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 教师考勤打卡
 * @author Administrator
 */
@Controller
@RequestMapping(value = { "/remote/openapi/office/tacherAttendance" })
public class OpenApiTeacherAttendanceAction {
	
	private static OpenApiTacherAttendanceService openApiTacherAttendanceService;

    public OpenApiTacherAttendanceService getOpenApiTacherAttendanceService() {
        if (openApiTacherAttendanceService == null) {
        	openApiTacherAttendanceService = Evn.getBean("openApiTacherAttendanceService");
        }
        return openApiTacherAttendanceService;
    }
    
    /**
	 * 获取考勤信息
	 * @param remoteParam
	 * @return
	 */
    @ResponseBody
    @RequestMapping("/attendanceDetail")
	public String attendanceDetail(String remoteParam){
		try {
			return getOpenApiTacherAttendanceService().attendanceDetail(remoteParam);
		} catch (Exception e) {
			e.printStackTrace();
			return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用dubbo接口出错")));
		}
	}
	
	/**
	 * 打卡提交
	 * @param remoteParam
	 * @return
	 */
    @ResponseBody
    @RequestMapping("/attendanceSubmit")
	public String attendanceSubmit(String remoteParam){
    	try {
    		return getOpenApiTacherAttendanceService().attendanceSubmit(remoteParam);
		} catch (Exception e) {
			e.printStackTrace();
			return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用dubbo接口出错")));
		}
	}
	
	/**
	 * 补卡申请
	 * @param remoteParam
	 * @return
	 */
    @ResponseBody
    @RequestMapping("/attendanceApply")
	public String attendanceApply(String remoteParam){
		try {
			return getOpenApiTacherAttendanceService().attendanceApply(remoteParam);
		} catch (Exception e) {
			e.printStackTrace();
			return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用dubbo接口出错")));
		}
	}
	
	/**
	 * 考勤统计
	 * @param remoteParam
	 * @return
	 */
    @ResponseBody
    @RequestMapping("/attendanceCount")
	public String attendanceCount(String remoteParam){
		try {
			return getOpenApiTacherAttendanceService().attendanceCount(remoteParam);
		} catch (Exception e) {
			e.printStackTrace();
			return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用dubbo接口出错")));
		}
	}
	
	/**
	 * 考勤月历
	 * @param remoteParam
	 * @return
	 */
    @ResponseBody
    @RequestMapping("/attendanceMonth")
	public String attendanceMonth(String remoteParam){
		try {
			return getOpenApiTacherAttendanceService().attendanceMonth(remoteParam);
		} catch (Exception e) {
			e.printStackTrace();
			return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用dubbo接口出错")));
		}
	}
}
