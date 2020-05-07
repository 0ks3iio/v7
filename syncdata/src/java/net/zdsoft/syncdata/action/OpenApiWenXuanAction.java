package net.zdsoft.syncdata.action;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.syncdata.constant.WenXunConstant;
import net.zdsoft.syncdata.service.WXSourceService;
import net.zdsoft.syncdata.util.ThreadPool;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * 文轩智慧调用同步启动接口
 * @author yangsj  2018年5月16日上午10:43:28
 */
@Controller
@RequestMapping(value = { "/homepage/remote/openapi"})
public class OpenApiWenXuanAction extends BaseAction{

	private Logger log = Logger.getLogger(OpenApiWenXuanAction.class);
	private List<String> codeList = new ArrayList<>(); //文轩推送过来的 orgcode
	
	@Autowired
	private WXSourceService wXSourceService;
	
	/**
     * 启动
     * @param remoteParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/wxsyn/start", method = RequestMethod.POST, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String doWxStart(@RequestParam("syncode") String syncode ,@RequestParam("orgcodes") String[] orgcodes) {
        try {                       
        	if (StringUtils.isBlank(Evn.getString("wxzh.scheduler.start"))
        			|| "false".equalsIgnoreCase(Evn.getString("wxzh.scheduler.start"))) {
        		return error("同步接口没有开启");
        	}
        	//进行数据的解析
        	if(StringUtils.isBlank(syncode) || orgcodes == null ||  orgcodes.length == 0) {
        		return error("数据为空");
        	}
        	ExecutorService singleThreadPool = ThreadPool.getThreadPool();
        	log.info("singleThreadPool----------"+singleThreadPool.hashCode());
        		try {
        			singleThreadPool.execute(new Runnable() {
    					@Override
    					public void run() {
    						log.info("开始同步数据------------");
    						codeList = setCodeList(orgcodes);
    						List<String> saveCodeList = wXSourceService.saveSchool(codeList);
    						if(CollectionUtils.isNotEmpty(saveCodeList)){
    							saveCodeList.forEach(c->{
    								wXSourceService.saveClass(c);
    								wXSourceService.saveTeacher(c);
    								wXSourceService.saveAdmin(c);
    								wXSourceService.saveStudent(c);
    								wXSourceService.saveFamily(c);
    							});
    							wXSourceService.saveTeachArea(saveCodeList);
    							wXSourceService.saveTeachPlace(saveCodeList);
    						}
    						wXSourceService.noticeResult(codeList,syncode);
    						log.info("结束同步数据------------");
    					}
    				});
    			} catch (Exception e) {
    				log.error("服务繁忙，请稍后请求");
    				return error("服务繁忙，请稍后请求");
    			}
		    return success("启动成功");
        }
        catch (Exception ex) {
        	return error("启动失败");
        }
    }
    
	public String success(String msg) {
		return Json.toJSONString(new ResultDto().setSuccess(true)
				.setCode(WenXunConstant.WX_SYN_SUCCESS_VALUE).setMsg(msg));
	}
    
    public String error(String msg) {
		return Json.toJSONString(new ResultDto().setSuccess(false)
				.setCode(WenXunConstant.WX_SYN_FAIL_VALUE).setMsg(msg));
	}
    
    private List<String> setCodeList(String[] orgcodes) {
		List<String> newCodeList = new ArrayList<>();
		for (String code : orgcodes) {
			if(code.contains("[")) {
				code = code.replaceAll("\\[", "");
			}
			if(code.contains("]")) {
				code = code.replaceAll("\\]", "");
			}
			if(code.contains("\"")) {
				code = code.replaceAll("\"", "");
			}
			newCodeList.add(code);
		}
		return newCodeList;
	}
}
