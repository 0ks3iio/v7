package net.zdsoft.syncdata.action;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.syncdata.constant.JledqConstant;
import net.zdsoft.syncdata.dto.ResultDto;
import net.zdsoft.syncdata.entity.EdqOrg;
import net.zdsoft.syncdata.entity.EdqUser;
import net.zdsoft.syncdata.service.EdqOrgService;
import net.zdsoft.syncdata.service.EdqUserService;
import net.zdsoft.syncdata.util.JledqSyncDataUtil;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author weixh
 * @since 2017-11-28 下午2:43:37
 */
@Controller
@RequestMapping("/syncdata/jledq")
@Lazy
public class JledqSyncDataAction extends BaseAction {
	@Autowired
	private EdqOrgService edqOrgService;
	@Autowired
	private EdqUserService edqUserService;
	
	@RequestMapping("/org")
	@ResponseBody
	@ControllerInfo("组织机构数据同步")
	public String syncOrg(ModelMap map, HttpServletRequest request, HttpSession httpSession){
		try {
			if(!JledqSyncDataUtil.isOpenSync()) {
				return jsonReturn(JledqConstant.STATUS_FAIL, "同步没有开启，无需同步数据！");
			}
			String jsonStr = getDecodeBody(request);
			System.out.println(jsonStr);
			if(StringUtils.isEmpty(jsonStr)){
				return jsonReturn(JledqConstant.STATUS_FAIL, "参数解密失败！");
			}
			EdqOrg eorg = JSONObject.parseObject(jsonStr, EdqOrg.class);
			if(eorg == null) {
				return jsonReturn(JledqConstant.STATUS_FAIL, "参数获取失败！");
			}
			return jsonReturn(edqOrgService.saveEdqOrg(eorg));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return jsonReturn(JledqConstant.STATUS_FAIL, "同步报错："+e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		String ss = "{\"zzdm\":\"10001000100\",\"operationType\":1,\"sjhm\":\"133233243\",\"user_id\":\"z1LVxQAF8E8=\",\"yhlx\":2,\"yhm\":\"领导\",\"yhzh\":\"test1\",\"zt\":1}";
		EdqUser eu = JSONObject.parseObject(ss, EdqUser.class);
		System.out.println(eu);
	}
	
	@RequestMapping("/user")
	@ResponseBody
	@ControllerInfo("用户数据同步")
	public String syncUser(ModelMap map, HttpServletRequest request, HttpSession httpSession){
		try {
			if(!JledqSyncDataUtil.isOpenSync()) {
				return jsonReturn(JledqConstant.STATUS_FAIL, "同步没有开启，无需同步数据！");
			}
			String jsonStr = getDecodeBody(request);
			System.out.println("jsonStr="+jsonStr);
			if(StringUtils.isEmpty(jsonStr)){
				return jsonReturn(JledqConstant.STATUS_FAIL, "参数解密失败！");
			}
			EdqUser eu = JSONObject.parseObject(jsonStr, EdqUser.class);
			if(eu == null) {
				return jsonReturn(JledqConstant.STATUS_FAIL, "参数获取失败！");
			}
			return jsonReturn(edqUserService.saveEdqUser(eu));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return jsonReturn(JledqConstant.STATUS_FAIL, "同步报错："+e.getMessage());
		}
	}
	
	// 获取解密后的参数
	private String getDecodeBody(HttpServletRequest request) throws Exception{
		String body = null;
		InputStream inputStream = request.getInputStream();
		if (inputStream != null)
			body = IOUtils.toString(inputStream, "utf-8");
//		body = "{\"zzdm\":\"10001000100\",\"operationType\":1,\"sjhm\":\"133233243\",\"user_id\":\"z1LVxQAF8E8=\",\"yhlx\":3,\"yhm\":\"学生1\",\"yhzh\":\"testxs1\",\"zt\":1}";
		System.out.println("body="+body);
		return JledqSyncDataUtil.decode(body);
	}
	
	// 返回信息
	private String jsonReturn(String status, String msg){
		ResultDto dto = new ResultDto(status, msg);
		return JSONObject.toJSONString(dto);
	}
	
	// 返回信息
	private String jsonReturn(ResultDto dto) {
		return JSONObject.toJSONString(dto);
	}
}
