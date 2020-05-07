package net.zdsoft.framework.utils;

import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpSyncClient {

	private static int maxConnNum = 4000;// 连接池最大连接数

	private static int maxPerRoute = 1500;// 每个主机的并发最多只有1500

	private CloseableHttpClient httpClient;

	// 应用启动的时候就应该执行的方法
	public HttpSyncClient() {
		this.httpClient = createClient(false);
	}

	public CloseableHttpClient createClient(boolean proxy) {
		LayeredConnectionSocketFactory sslsf = null;
		try {
			sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();
		// 初始化连接池

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		cm.setMaxTotal(maxConnNum);
		cm.setDefaultMaxPerRoute(maxPerRoute);

		return HttpClients.custom().setConnectionManager(cm).build();
	}

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}
}
