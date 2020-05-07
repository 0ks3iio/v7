package net.zdsoft.remote.openapi.action.business;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.power.remote.service.SysUserPowerRemoteService;
import net.zdsoft.remote.openapi.action.OpenApiBaseAction;
import net.zdsoft.remote.openapi.service.OpenApiInterfaceCountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * 用户权限
 * @author yangsj  2018年6月20日上午10:43:34
 */
@Controller
@RequestMapping(value = { "/remote/openapi", "/openapi" })
public class UserPowerAction extends OpenApiBaseAction{

	protected static final Logger logger = LoggerFactory.getLogger(UserPowerAction.class);
	private final String interfaceType = "power"; 
	@Autowired
	private SysUserPowerRemoteService sysUserPowerRemoteService;
	@Autowired
	private OpenApiInterfaceCountService openApiInterfaceCountService;
	  
    @ResponseBody
    @RequestMapping(value = "/findUserPower")
    public String findUserPowerList(HttpServletRequest request,String userId,String userName) {
        try {
	          if (isOverMaxNumDay(interfaceType, request, "/openapi/findUserPower")){
	            	return returnOpenJsonError(OpenApiBaseAction.MAX_NUM_DAY_ERROR_MSG);
	          } 
    		  doSaveInterfaceCount(interfaceType,request,"/openapi/sync/findUser");
        	  JSONObject json = new JSONObject();
        	  String result = sysUserPowerRemoteService.findPowerByUserName(userName);
        	  json = JSONObject.parseObject(result);
        	  return returnOpenJsonObjSuccess(json, "ok");
        }
        catch (Exception ex) {
        	return returnOpenJsonError("调用远程接口失败");
        }
    }
    
	  
	  
}
