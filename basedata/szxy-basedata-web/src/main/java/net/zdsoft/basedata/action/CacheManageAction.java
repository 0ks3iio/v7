package net.zdsoft.basedata.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import net.zdsoft.framework.action.BaseAction;

@Controller
@RequestMapping("/basedata")
public class CacheManageAction extends BaseAction{
	
//	@Autowired
//	private SystemCacheService systemCacheService;
//	
//	@ResponseBody
//	@RequestMapping("/cache/clearBefore")
//	@ControllerInfo("清除以key开头的缓存")
//	public String clearBefore(String key) {
//		try{
//			JSONObject returnMsg = systemCacheService.clearBefore(key);
//			return doReturnMsg(returnMsg);
//		}catch(Exception e){
//			e.printStackTrace();
//			return returnError();
//		}
//		
//	}
//
//	private String doReturnMsg(JSONObject returnMsg) {
//		if(returnMsg.getBooleanValue("success")){
//			return success(returnMsg.getString("msg"));
//		}else{
//			return error(returnMsg.getString("msg"));
//		}
//	}
//	
//	@ResponseBody
//	@RequestMapping("/cache/clearKey")
//	@ControllerInfo("清除key的缓存")
//	public String clearKey(String key) {
//		try{
//			JSONObject returnMsg = systemCacheService.clearKey(key);
//			return doReturnMsg(returnMsg);
//		}catch(Exception e){
//			e.printStackTrace();
//			return returnError();
//		}
//	}
//	
//	@ResponseBody
//	@RequestMapping("/cache/refreshMcode")
//	@ControllerInfo("刷新微代码缓存")
//	public String refreshMcode(String mcodeId) {
//		try{
//			JSONObject returnMsg = systemCacheService.refreshMcode(mcodeId);
//			return doReturnMsg(returnMsg);
//		}catch(Exception e){
//			e.printStackTrace();
//			return returnError();
//		}
//	}
//	
//	@ResponseBody
//	@RequestMapping("/cache/refreshBSysOpt")
//	@ControllerInfo("刷新BSysOpt开关缓存")
//	public String refreshBaseSysOption() {
//		try{
//			JSONObject returnMsg = systemCacheService.refreshBaseSysOption();
//			return doReturnMsg(returnMsg);
//		}catch(Exception e){
//			e.printStackTrace();
//			return returnError();
//		}
//	}
//	
//	@ResponseBody
//	@RequestMapping("/cache/refreshSysOpt")
//	@ControllerInfo("刷新SysOpt开关缓存")
//	public String refreshSysOption() {
//		try{
//			JSONObject returnMsg = systemCacheService.refreshSysOption();
//			return doReturnMsg(returnMsg);
//		}catch(Exception e){
//			e.printStackTrace();
//			return returnError();
//		}
//	}
//	
//	@ResponseBody
//	@RequestMapping("/cache/refreshCharts")
//	@ControllerInfo("刷新图表权限有关缓存")
//	public String refreshCharts() {
//		try{
//			JSONObject returnMsg = systemCacheService.refreshCharts();
//			return doReturnMsg(returnMsg);
//		}catch(Exception e){
//			e.printStackTrace();
//			return returnError();
//		}
//	}
//	
//	@ResponseBody
//	@RequestMapping("/cache/refreshAction")
//	@ControllerInfo("刷新cacheAction开关缓存")
//	public String refreshAction() {
//		try{
//			JSONObject returnMsg = systemCacheService.refreshAction();
//			return doReturnMsg(returnMsg);
//		}catch(Exception e){
//			e.printStackTrace();
//			return returnError();
//		}
//	}
//	
//	@ResponseBody
//	@RequestMapping("/cache/closeAction")
//	@ControllerInfo("关闭cacheAction开关")
//	public String closeAction() {
//		try{
//			JSONObject returnMsg = systemCacheService.closeAction();
//			return doReturnMsg(returnMsg);
//		}catch(Exception e){
//			e.printStackTrace();
//			return returnError();
//		}
//	}
	
}
