package net.zdsoft.remote.openapi.interceptor;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.ServletUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.remote.openapi.action.OpenApiBaseAction;
import net.zdsoft.remote.openapi.constant.OpenApiConstants;
import net.zdsoft.remote.openapi.entity.Developer;
import net.zdsoft.remote.openapi.entity.OpenApiApply;
import net.zdsoft.remote.openapi.entity.OpenApiInterfaceCount;
import net.zdsoft.remote.openapi.enums.OpenapiParamEnum;
import net.zdsoft.remote.openapi.service.DeveloperService;
import net.zdsoft.remote.openapi.service.OpenApiApplyService;
import net.zdsoft.remote.openapi.service.OpenApiInterfaceCountService;
import net.zdsoft.remote.openapi.utils.IdChangeUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 验证接口是否申请， 成功申请的话， 调用接口保存到 记录表中
 * @author yangsj
 *
 */
public class InterfaceJudgeInterceptor implements HandlerInterceptor{
	
	OpenApiApplyService openApiApplyService = Evn.getBean("openApiApplyService");
	OpenApiInterfaceCountService openApiInterfaceCountService = Evn.getBean("openApiInterfaceCountService");
	 @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
	    Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
	    String endType;
        String type = (String) pathVariables.get("type");
        String subType = (String) pathVariables.get("subType");
        String id   =  (String) pathVariables.get("id");
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/remote")) {
        	requestURI = requestURI.replaceFirst("/remote", "");
        }
        if(StringUtils.isNotBlank(id)){
        	id = IdChangeUtils.get32Id(id);
        	requestURI = requestURI.replaceFirst(id, "{id}");
        }
        if(StringUtils.isNotBlank(type)){
        	endType = StringUtils.isNotBlank(subType) ? subType : type;
        }else{
        	ServletUtils.print(response, "请传接口类型参数！！");
            return false;
        }
        if(isApply(endType, request)){        
        	if (isOverMaxNumDay(endType, request, requestURI)){
        		ServletUtils.print(response, OpenApiBaseAction.MAX_NUM_DAY_ERROR_MSG);
                return false;
        	} else {
        		doSaveInterfaceCount(endType, request, requestURI);
        		return true;
        	}
        }else{
        	ServletUtils.print(response, OpenApiBaseAction.APPLY_ERROR_MSG);
            return false;
        }
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
    private boolean isOverMaxNumDay(String type, HttpServletRequest request, String uri) {
    	Developer developer = getDeveloperByRequest(request);
    	List<OpenApiApply> openApiApplies = openApiApplyService.findByDeveloperIdAndTypeIn(developer.getId(), type);
    	if(CollectionUtils.isNotEmpty(openApiApplies)){
    		int maxNum = openApiApplies.get(0).getMaxNumDay();
    		Integer limitEveryTime = openApiApplies.get(0).getLimitEveryTime();
    		if(limitEveryTime == null) 
    			limitEveryTime = 1000;
    		request.setAttribute(OpenapiParamEnum.MAX_LIMIT.getName(), limitEveryTime);
    		return isOverMaxNumDay(type, developer.getTicketKey(), maxNum);
    	}
    	return Boolean.FALSE;
    }
    
    private boolean isOverMaxNumDay(String type, String ticketKey , int maxNum){
    	Date startTime = getDateBySimple(new Date(),"yyyy-MM-dd");// 定义起始日期
        Date endTime = DateUtils.addDay(new Date(), 1);// 定义结束日期
        endTime = getDateBySimple (endTime,"yyyy-MM-dd");
	    List<OpenApiInterfaceCount> operationLogs = openApiInterfaceCountService.findDoInterfaceNum(ticketKey,type,startTime,endTime);
	    int hasCount = 0;
	    if(CollectionUtils.isNotEmpty(operationLogs)){
	    	hasCount = operationLogs.size();
	    }
	    return hasCount >= maxNum;
    }
    
    /**
     * 该类型 是否已经申请了
     * 
     */
    private boolean isApply(String type, HttpServletRequest request) {
    	Developer developer = getDeveloperByRequest(request);
    	List<OpenApiApply> openApiApplies = openApiApplyService.findByDeveloperIdAndTypeIn(developer.getId(), type);
    	return CollectionUtils.isNotEmpty(openApiApplies);
    }
    
    private Developer getDeveloperByRequest(HttpServletRequest request) {
    	DeveloperService bean = Evn.getBean("developerService");
    	String ticketKey = request.getParameter("ticketKey");
    	if (StringUtils.isEmpty(ticketKey)) {
    		ticketKey = request.getHeader("ticketKey");
    	}
		return bean.findByTicketKey(ticketKey);
	}
    
    private Date getDateBySimple(Date date,String pattern) {
		return DateUtils.string2Date(DateUtils.date2String(date,pattern),pattern);
	}
    
    private void doSaveInterfaceCount(String interfaceType, HttpServletRequest request, String uri) {
    	openApiInterfaceCountService.save(OpenApiInterfaceCount.saveDoInterface(interfaceType, uri, request,OpenApiConstants.OPENAPI_V20_VERSION));
    }
}
