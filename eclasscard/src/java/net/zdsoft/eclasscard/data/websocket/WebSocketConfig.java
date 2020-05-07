package net.zdsoft.eclasscard.data.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 *
 * @author jiang feng
 *
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements
		WebSocketConfigurer {
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// 电子班牌注册
		registry.addHandler(eClassCardWebSocketHandler,
				"/eClassCard/webSocketServer").addInterceptors(
				webSocketHandshakeInterceptor).setAllowedOrigins("*");
		// .setAllowedOrigins("http://localhost:8088");
		registry.addHandler(eClassCardWebSocketHandler,
				"/sockjs/eClassCard/webSocketServer")
				.addInterceptors(webSocketHandshakeInterceptor).setAllowedOrigins("*").withSockJS();
	}
	
	@Autowired
	private EClassCardWebSocketHandler eClassCardWebSocketHandler;

	@Autowired
	private WebSocketHandshakeInterceptor webSocketHandshakeInterceptor;

}
