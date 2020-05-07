package net.zdsoft.diathesis.data.intercept;

import net.zdsoft.diathesis.data.service.DiathesisProjectService;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.RedisUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 单位基本数据初始化
 * @Author: panlf
 * @Date: 2019/12/11 16:43
 */
@Component
public class AutoSettingInterceptor implements HandlerInterceptor {

    protected static final Logger log = Logger.getLogger(AutoSettingInterceptor.class);

    @Autowired
    private DiathesisProjectService diathesisProjectService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LoginInfo loginInfo=(LoginInfo)request.getSession().getAttribute("loginInfo");
        String unitId = loginInfo.getUnitId();
        String realName = loginInfo.getRealName();
        RedisUtils.hasLocked("diathesisInitLock_" + unitId + "_78,");
        try{
            if("true".equals(RedisUtils.get("diathesisInitStatus_" + unitId + "78,"))){
                return true;
            }
            Integer count=diathesisProjectService.countTopProjectByUnitId(unitId);
            if(count==null || count==0){
                diathesisProjectService.addAutoSetting(unitId,realName);
            }
            RedisUtils.set("diathesisInitStatus_"+unitId+"78,","true",7200);
        }catch (Exception e){
            RedisUtils.del("diathesisInitStatus_" + unitId + "78,");
            log.info(e.getMessage());
        }finally {
            RedisUtils.unLock("diathesisInitLock_" + unitId + "_78,");
        }
        return true;
    }
}