package net.zdsoft.api.base.interceptor;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.api.base.constant.BaseDataCenterConstant;
import net.zdsoft.api.base.entity.eis.ApiApply;
import net.zdsoft.api.base.entity.eis.ApiInterface;
import net.zdsoft.api.base.enums.OpenapiParamEnum;
import net.zdsoft.api.base.service.ApiApplyService;
import net.zdsoft.api.base.service.ApiInterfaceCountService;
import net.zdsoft.api.base.service.ApiInterfaceService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.ServletUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 验证接口是否申请， 成功申请的话， 调用接口保存到 记录表中
 * @author yangsj
 *
 */
public class InterfaceJudgeInterceptor implements HandlerInterceptor{
	
	ApiApplyService openApiApplyService = Evn.getBean("apiApplyService");
	ApiInterfaceCountService openApiInterfaceCountService = Evn.getBean("apiInterfaceCountService");
	ApiInterfaceService openApiInterfaceService = Evn.getBean("apiInterfaceService");
	 @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		//验证之前的接口 走类型审核
	    Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
	    String endType;
        String type = (String) pathVariables.get("type");
        String subType = (String) pathVariables.get("subType");
        String id   =  (String) pathVariables.get("id");
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/api")) {
			requestURI = requestURI.replaceFirst("/api", "");
		}
        if (requestURI.contains("/remote")) {
        	requestURI = requestURI.replaceFirst("/remote", "");
        }
        if(StringUtils.isNotBlank(id)){
        	requestURI = requestURI.replaceFirst(id, "{id}");
        }
        if(StringUtils.isNotBlank(type)){
        	endType = StringUtils.isNotBlank(subType) ? subType : type;
        }else{
        	ServletUtils.print(response, "请传接口类型参数！！");
            return false;
        }
        String ticketKey =  request.getParameter("ticketKey");
    	if (StringUtils.isEmpty(ticketKey)) {
    		ticketKey = request.getHeader("ticketKey");
    	}
    	ApiInterface oi = openApiInterfaceService.findByUri(requestURI);
    	if(oi != null){
    		if(isApply(ticketKey, oi.getId())){        
    			if (isOverMaxNumDay(request, oi.getId())){
    				ServletUtils.print(response, BaseDataCenterConstant.MAX_NUM_DAY_ERROR_MSG);
    				return false;
    			} else {
    				return true;
    			}
    		}
    	}
    	ServletUtils.print(response, BaseDataCenterConstant.APPLY_ERROR_MSG);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, Object obj,
            ModelAndView modelandview) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object obj, Exception exception)
            throws Exception {

    }
    
    /**
     * 是否超过每天限制调用次数
	 * @param type
	 * @param request
	 * @param uri
	 */
    private boolean isOverMaxNumDay(HttpServletRequest request, String interfaceId) {
    	String ticketKey = request.getParameter("ticketKey");
    	if (StringUtils.isEmpty(ticketKey)) {
    		ticketKey = request.getHeader("ticketKey");
    	}
    	ApiApply openApiApply = getApiApply(ticketKey,interfaceId);
    	if(openApiApply != null){
    		if(openApiApply.getIsLimit() == ApiApply.IS_LIMIT_TRUE){
    			int maxNum = openApiApply.getMaxNumDay();
    			Integer limitEveryTime = openApiApply.getLimitEveryTime();
    			if(limitEveryTime == null) 
    				limitEveryTime = 1000;
    			request.setAttribute(OpenapiParamEnum.MAX_LIMIT.getName(), limitEveryTime);
    			return isOverMaxNumDay(interfaceId, ticketKey, maxNum);
    		}
    	}
    	return Boolean.FALSE;
    }
    
    private boolean isOverMaxNumDay(String interfaceId, String ticketKey , int maxNum){
    	Date startTime = getDateBySimple(new Date(),"yyyy-MM-dd");// 定义起始日期
        Date endTime = DateUtils.addDay(new Date(), 1);// 定义结束日期
        endTime = getDateBySimple (endTime,"yyyy-MM-dd");
	    long hasCount = openApiInterfaceCountService.findCountByInterfaceId(ticketKey,interfaceId,startTime,endTime);
	    return hasCount >= maxNum;
    }
    
    /**
     * 该类型 是否已经申请了
     * 
     */
    private boolean isApply(String ticketKey, String interfaceId) {
    	return getApiApply(ticketKey,interfaceId) != null;
    }
    
    private ApiApply getApiApply(String ticketKey, String interfaceId){
		ApiApply apply = openApiApplyService.findByTicketKeyAndInterfaceId(ticketKey, interfaceId);
		return apply;
    }
    
    private Date getDateBySimple(Date date,String pattern) {
		return DateUtils.string2Date(DateUtils.date2String(date,pattern),pattern);
	}
    
//    private void doSaveInterfaceCount(String interfaceType, HttpServletRequest request, String uri) {
//    	openApiInterfaceCountService.save(OpenApiInterfaceCount.saveDoInterface(interfaceType, uri, request,OpenApiConstants.OPENAPI_V20_VERSION));
//    }
}
