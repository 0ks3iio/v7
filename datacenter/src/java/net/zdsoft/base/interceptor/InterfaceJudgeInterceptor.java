package net.zdsoft.base.interceptor;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.base.constant.BaseDataCenterConstant;
import net.zdsoft.base.entity.eis.OpenApiApply;
import net.zdsoft.base.entity.eis.OpenApiInterface;
import net.zdsoft.base.entity.eis.OpenApiInterfaceCount;
import net.zdsoft.base.enums.OpenapiParamEnum;
import net.zdsoft.base.service.OpenApiApplyService;
import net.zdsoft.base.service.OpenApiInterfaceCountService;
import net.zdsoft.base.service.OpenApiInterfaceService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.ServletUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.openapi.remote.openapi.constant.OpenApiConstants;

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
	OpenApiInterfaceService openApiInterfaceService = Evn.getBean("openApiInterfaceService");
	 @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		//验证之前的接口 走类型审核
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
    	OpenApiInterface oi = openApiInterfaceService.findByUri(requestURI);
    	if(oi != null){
    		if(isApply(endType, ticketKey, oi.getId())){        
    			if (isOverMaxNumDay(endType, request, oi.getId())){
    				ServletUtils.print(response, BaseDataCenterConstant.MAX_NUM_DAY_ERROR_MSG);
    				return false;
    			} else {
//    				doSaveInterfaceCount(endType, request, requestURI);
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
    private boolean isOverMaxNumDay(String type, HttpServletRequest request, String interfaceId) {
    	String ticketKey = request.getParameter("ticketKey");
    	if (StringUtils.isEmpty(ticketKey)) {
    		ticketKey = request.getHeader("ticketKey");
    	}
    	OpenApiApply openApiApply = getApiApply(type,ticketKey,interfaceId);
    	if(openApiApply != null){
    		if(openApiApply.getIsLimit() == OpenApiApply.IS_LIMIT_TRUE){
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
    private boolean isApply(String type,String ticketKey, String interfaceId) {
    	return getApiApply(type,ticketKey,interfaceId) != null;
    }
    
    private OpenApiApply getApiApply(String type,String ticketKey, String interfaceId){
		List<OpenApiApply> openApiApplies = openApiApplyService.findByTicketKeyAndTypeOrInterfaceId(ticketKey, type, interfaceId);
		if(CollectionUtils.isNotEmpty(openApiApplies))
			return openApiApplies.get(0);
		return null;
    }
    
    private Date getDateBySimple(Date date,String pattern) {
		return DateUtils.string2Date(DateUtils.date2String(date,pattern),pattern);
	}
    
//    private void doSaveInterfaceCount(String interfaceType, HttpServletRequest request, String uri) {
//    	openApiInterfaceCountService.save(OpenApiInterfaceCount.saveDoInterface(interfaceType, uri, request,OpenApiConstants.OPENAPI_V20_VERSION));
//    }
}
