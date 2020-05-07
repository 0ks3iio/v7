package net.zdsoft.eclasscard.data.websocket;

import java.util.Map;

import net.zdsoft.framework.utils.StringUtils;

import org.apache.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
/**
 * 
 * @author jiang feng
 *
 */
@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

	private static Logger logger = Logger
			.getLogger(WebSocketHandshakeInterceptor.class);

	@Override
	public boolean beforeHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		logger.debug("beforeHandshake.............");
		if (request instanceof ServletServerHttpRequest) {
			String sid = ((ServletServerHttpRequest) request)
					.getServletRequest().getParameter("sid");
			String isApp = ((ServletServerHttpRequest) request)
					.getServletRequest().getParameter("isApp");
			if (StringUtils.isBlank(sid)) {
				logger.debug("设备号为空!");
				return false;
			} else {
				attributes.put("sid", sid);
				if (StringUtils.isBlank(isApp)) {
					attributes.put("isApp", "");
				}else{
					attributes.put("isApp", isApp);
				}
				return true;
			}
		}
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {

	}
}
