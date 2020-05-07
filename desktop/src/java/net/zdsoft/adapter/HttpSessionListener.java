package net.zdsoft.adapter;

import javax.servlet.http.HttpSessionEvent;

/**
 * 清除net.zdsoft.adapter.AdapterSessionHolder session防止内存泄漏
 * 有可能是被动通知
 * @author shenke
 * @date 2019/10/23 下午2:48
 */
public class HttpSessionListener implements javax.servlet.http.HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        //do nothing
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        AdapterSessionHolder.clearHttpSession(httpSessionEvent.getSession());
    }
}
