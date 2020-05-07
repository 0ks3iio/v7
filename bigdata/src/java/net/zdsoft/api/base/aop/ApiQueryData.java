package net.zdsoft.api.base.aop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.api.base.entity.eis.ApiApply;
import net.zdsoft.api.base.entity.eis.ApiDeveloper;
import net.zdsoft.api.base.entity.eis.ApiInterface;
import net.zdsoft.api.base.entity.eis.ApiInterfaceCount;
import net.zdsoft.api.base.entity.eis.OpenApiApp;
import net.zdsoft.api.base.service.ApiApplyService;
import net.zdsoft.api.base.service.ApiDeveloperService;
import net.zdsoft.api.base.service.ApiInterfaceCountService;
import net.zdsoft.api.base.service.ApiInterfaceService;
import net.zdsoft.api.base.service.OpenApiAppService;
import net.zdsoft.api.monitor.constant.ApiConstant;
import net.zdsoft.bigdata.frame.data.kafka.KafkaProduceService;
import net.zdsoft.bigdata.frame.data.redis.BgRedisUtils;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.servlet.HandlerMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ApiQueryData {
    ApiInterfaceCountService openApiInterfaceCountService = Evn.getBean("apiInterfaceCountService");
    ApiInterfaceService openApiInterfaceService = Evn.getBean("apiInterfaceService");
    ApiApplyService apiApplyService = Evn.getBean("apiApplyService");
    KafkaProduceService kafkaProduceService = Evn.getBean("kafkaProduceService");
    ApiDeveloperService developerService = Evn.getBean("apiDeveloperService");
    OpenApiAppService appService = Evn.getBean("openApiAppService");

    public Object saveLogger(ProceedingJoinPoint point) {
        // 用 commons-lang 提供的 StopWatch 计时
        StopWatch clock = new StopWatch();
        clock.start(); // 计时开始
        Object result = null;
        int dataCount = 0;
        try {
            result = point.proceed();
            JSONObject jsonObject = JSON.parseObject((String) result);
            dataCount = jsonObject.getInteger("dataCount");
            System.out.println(result);
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println("拉取数据的接口报错：" + e.getMessage());
            return result;
        }
        clock.stop(); // 计时结束
        doSaveInterfaceCount(point.getArgs(), clock.getTime(), dataCount);
        return result;
    }

    private void doSaveInterfaceCount(Object[] args, long time, int dataCount) {
        HttpServletRequest request = null;
        if (args != null) {
            for (Object object : args) {
                if (object instanceof HttpServletRequest) {
                    request = (HttpServletRequest) object;
                }
            }
        }
        if (request != null) {
            List<ApiInterfaceCount> saveApiInterfaceCounts = getApiInterfaceCount(request, time, dataCount);
//            openApiInterfaceCountService.saveAll(saveApiInterfaceCounts.toArray(new ApiInterfaceCount[0]));
            //把数据推送到 实时调用 实时警告 中
            saveApiInterfaceCounts.forEach(c -> {
                c.setTimes(1);
                JSONObject js = (JSONObject) JSON.toJSON(c);
                if(c.getSequenceId() == 1){
                	if (c.getIsWarn() == 1) {
                		BgRedisUtils.addDataToList(ApiConstant.API_CALL_WARNING_KEY, js.toJSONString(), 10);
                	}
                	BgRedisUtils.addDataToList(ApiConstant.API_CALL_DETAIL_KEY, js.toJSONString(), 10);
                }
                BgRedisUtils.getJedis().lpush(ApiConstant.API_CALL_REALTIME_MONITOR_KEY, js.toJSONString());
                System.out.println("----" + js.toJSONString());
            });

        }
    }

    private List<ApiInterfaceCount> getApiInterfaceCount(HttpServletRequest request, long time, int dataCount) {
        List<ApiInterfaceCount> saveApiInterfaceCounts = new ArrayList<>();
        String ticketKey = request.getHeader("ticketKey");
        if (StringUtils.isBlank(ticketKey)) {
            ticketKey = request.getParameter("ticketKey");
        }
        ApiDeveloper developer = developerService.findByTicketKey(ticketKey);
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/api")) {
            requestURI = requestURI.replaceFirst("/api", "");
        }
        if (requestURI.contains("/remote")) {
            requestURI = requestURI.replaceFirst("/remote", "");
        }
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String id = (String) pathVariables.get("id");
        if (StringUtils.isNotBlank(id)) {
            requestURI = requestURI.replaceFirst(id, "{id}");
        }
        ApiInterface oi = openApiInterfaceService.findByUri(requestURI);
        //根据id + ticketKey 来得到 应用的id
        ApiApply apply = apiApplyService.findByTicketKeyAndInterfaceId(ticketKey, oi.getId());
        String appIds = apply.getAppIds();
        ApiInterfaceCount endSaveCount = new ApiInterfaceCount();
        endSaveCount.setId(UuidUtils.generateUuid());
        endSaveCount.setCreationTime(new Date());
        endSaveCount.setTicketKey(ticketKey.trim());
        endSaveCount.setType(oi.getType());
        endSaveCount.setResultType(oi.getResultType());
        endSaveCount.setPushUrl(requestURI);
        endSaveCount.setInterfaceId(oi.getId());
        endSaveCount.setDataType(oi.getDataType());
        endSaveCount.setAppIds(appIds);
        endSaveCount.setMessage("调用接口的响应时间为：" + time + "ms");
        endSaveCount.setCount(dataCount);
        endSaveCount.setTime(time);
        openApiInterfaceCountService.save(endSaveCount);
        
        if (StringUtils.isNotBlank(appIds)) {
            String[] appStrings = appIds.split(",");
            List<OpenApiApp> appList = appService.findByIds(appStrings);
            for (int i = 0; i < appList.size(); i++) {
                int n = i;
                ApiInterfaceCount openApiInterfaceCount = new ApiInterfaceCount();
                EntityUtils.copyProperties(endSaveCount, openApiInterfaceCount);
                String dataStatus = (oi.getDataType() == ApiInterface.BASE_DATE_TYPE ||
                        oi.getDataType() == ApiInterface.BUSINESS_DATE_TYPE) ? "拉取" : "推送";
                openApiInterfaceCount.setDataStatus(dataStatus);
                openApiInterfaceCount.setDeveloperName(developer.getUnitName());
                openApiInterfaceCount.setAppName(appList.get(i).getName());
                openApiInterfaceCount.setSequenceId(++n);
                openApiInterfaceCount.setAppIds(appList.get(i).getId());
                //判断接口相应时间是否超时
                openApiInterfaceCount.setIsWarn(getIsWarning(time));

                saveApiInterfaceCounts.add(openApiInterfaceCount);
            }
        }
        return saveApiInterfaceCounts;
    }

    private int getIsWarning(long time) {
        int n = 1;
        if (1000 > time) {
            n = 0;
        }
        return n;
    }
}
