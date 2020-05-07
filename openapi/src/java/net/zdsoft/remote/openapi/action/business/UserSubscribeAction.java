package net.zdsoft.remote.openapi.action.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.desktop.remote.service.UserSubscribeRemoteService;
import net.zdsoft.remote.openapi.action.OpenApiBaseAction;

/**
 * 
 * 排行榜中调用的接口
 * @author yangsj  2017-6-22上午11:20:45
 */
@Controller
@RequestMapping(value = { "/remote/openapi/userSubscribe" })
public class UserSubscribeAction extends OpenApiBaseAction{
  
	protected static final Logger logger = LoggerFactory.getLogger(UserSubscribeAction.class);
	
	@Autowired
	private UserSubscribeRemoteService userSubscribeRemoteService;
	
	
	/**
     * 排行榜
     *
     * @param remoteParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/rankingList")
    public String findRankingList(String pageNum) {
    	
        try {
        	getResponse().setContentType("text/html");
            String result = userSubscribeRemoteService.findRankingList(Integer.valueOf(pageNum));
            return getRequest().getParameter("callback")+"("+result+")";
    //        return result;
        }
        catch (Exception ex) {
            return "调用远程接口失败";
        }
    }
   
    
    /**
     * 人气
     *
     * @param remoteParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/appPopularity")
    public String findAppPopularity() {
    	
        try {
            String result = userSubscribeRemoteService.findAppPopularity();
            return getRequest().getParameter("callback")+"("+result+")";
//            return result;
        }
        catch (Exception ex) {
            return "调用远程接口失败";
        }
    }

	
}
