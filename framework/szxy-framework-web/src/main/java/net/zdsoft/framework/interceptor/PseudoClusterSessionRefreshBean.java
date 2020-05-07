package net.zdsoft.framework.interceptor;

import net.zdsoft.framework.entity.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

;

/**
 * @author shenke
 */
@Component
public class PseudoClusterSessionRefreshBean {

    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public PseudoClusterSessionRefreshBean(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void refresh(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            return ;
        }
        //
        //net.zdsoft.desktop.login.LoginConstant.SESSION_ATTRIBUTE_NAME
        String passportSessionId = (String) httpSession.getAttribute("passportSessionId");
        if (StringUtils.isBlank(passportSessionId)) {
            return;
        }
        String redisPassportSessionId = redisTemplate.opsForValue().get(Constant.PASSPORT_TICKET_KEY + passportSessionId);

        //refresh passport sessionID
        if (StringUtils.isNotBlank(passportSessionId) && StringUtils.isNotBlank(redisPassportSessionId)) {
            redisTemplate.opsForValue().set(Constant.PASSPORT_TICKET_KEY + passportSessionId,
                    passportSessionId, httpSession.getMaxInactiveInterval(), TimeUnit.SECONDS);
        }
    }
}
