package net.zdsoft.officework.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class OfficeUtils {
	private static final Logger log = LoggerFactory.getLogger(OfficeUtils.class);
	/**
	 * 四舍五入
	 * @param d
	 * @param scale 保留几位小数
	 * @return
	 */
	public static double formatDouble2(double d, int scale) {
        // 新方法，如果不需要四舍五入，可以使用RoundingMode.DOWN
        BigDecimal bg = new BigDecimal(d).setScale(scale, BigDecimal.ROUND_HALF_UP);
        return bg.doubleValue();
    }
	
	/**
	 * http请求(post方法)
	 * @param url
	 * @param username
	 * @param password
	 * @param parm
	 * @return
	 */
	public static String sendHttpPost(String url, Map<String, String> parmMap) throws Exception{
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		BufferedReader in = null;
		try {
			//post
			List<NameValuePair> parms = new ArrayList<NameValuePair>();
			for(String key : parmMap.keySet()){
				parms.add(new BasicNameValuePair(key, parmMap.get(key)));
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parms);
			//表单提交
			HttpPost post = new HttpPost(url);
            post.setEntity(formEntity);
            
			response = client.execute(post);
            StringBuilder result = new StringBuilder();
            if(response.getStatusLine().getStatusCode() ==  HttpStatus.SC_OK){
            	 HttpEntity entity = response.getEntity();
                 if(entity!=null){
                 	in = new BufferedReader(new InputStreamReader(entity.getContent()));
                 	String line = null;
                 	while((line = in.readLine())!=null){
                 		result.append(line);
                 	}
                 }
                 log.info("推送httpPost请求URL"+url+"推送数据="+parmMap.toString());
            }else{
            	log.error("推送httpPost请求失败：URL"+url+"推送数据="+parmMap.toString()+",response:"+response.getStatusLine());
            }
            
           
            return result.toString();
		}catch(Exception e){
			throw e;
		}finally{
			try {
				if(response!=null){
					response.close();
				}
				if (in != null)
					in.close();
				if(client!=null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * http请求(post方法 json格式)
	 * @param url
	 * @param username
	 * @param password
	 * @param parm
	 * @return
	 */
	public static String sendHttpPostJson(String url, JSONObject parm){
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		BufferedReader in = null;
		try {
			
			StringEntity stringEntity = new StringEntity(parm.toString(),"UTF-8");//解决中文乱码问题  
            stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));  
            stringEntity.setContentType("application/json"); 
            //post json提交
            HttpPost post = new HttpPost(url);
            post.setEntity(stringEntity);
            
			response = client.execute(post);
			
            StringBuilder result = new StringBuilder();
            if(response.getStatusLine().getStatusCode() ==  HttpStatus.SC_OK){
            	 HttpEntity entity = response.getEntity();
                 if(entity!=null){
                 	in = new BufferedReader(new InputStreamReader(entity.getContent()));
                 	String line = null;
                 	while((line = in.readLine())!=null){
                 		result.append(line);
                 	}
                 }
            }
           
            return result.toString();
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}finally{
			try {
				if(response!=null){
					response.close();
				}
				if (in != null)
					in.close();
				if(client!=null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * http摘要认证(post方法 json格式)
	 * @param url
	 * @param username
	 * @param password
	 * @param parm
	 * @return
	 */
	public static String sendHttpPostJsonAuth(String url, String username, String password, JSONObject parm){
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		BufferedReader in = null;
		try {
			URI serverUri = new URI(url);
			//host
			HttpHost httpHost = new HttpHost(serverUri.getHost(), serverUri.getPort());
			//post
			HttpPost post = new HttpPost(url);
			
			StringEntity stringEntity = new StringEntity(parm.toString(),"UTF-8");//解决中文乱码问题  
            stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));  
            stringEntity.setContentType("application/json"); 
            post.setEntity(stringEntity);
			
			// 创建认证缓存  
			AuthCache authCache = new BasicAuthCache();
			// 创建基础认证机制 添加到缓存  
			DigestScheme digestAuth = new DigestScheme();
			authCache.put(httpHost, digestAuth);  
			
			//context
			//基础凭证提供器,明文传输数据  
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(httpHost.getHostName(), httpHost.getPort()), new UsernamePasswordCredentials(username,password));
			
			// 将认证缓存添加到执行环境中  即预填充  
			HttpClientContext context = HttpClientContext.create();
			context.setCredentialsProvider(credsProvider);
			context.setAuthCache(authCache);  
			
			response = client.execute(post, context);
			
            StringBuilder result = new StringBuilder();
            if(response.getStatusLine().getStatusCode() ==  HttpStatus.SC_OK){
            	 HttpEntity entity = response.getEntity();
                 if(entity!=null){
                 	in = new BufferedReader(new InputStreamReader(entity.getContent()));
                 	String line = null;
                 	while((line = in.readLine())!=null){
                 		result.append(line);
                 	}
                 }
            }
           
            return result.toString();
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}finally{
			try {
				if(response!=null){
					response.close();
				}
				if (in != null)
					in.close();
				if(client!=null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String httpGetAuth(String url, String apikey, List<NameValuePair> parm){
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			//host
			url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(parm, "utf-8"));
			HttpGet get = new HttpGet(url);  
			get.addHeader("Content-Type", "application/json;charset=UTF-8");
//			get.addHeader("Apikey", "daca6d584fa64a1e8bdecd9594131df9");scenarioId=801034&schoolId=1  
//			get.addHeader("Apikey", "2101caa11b834ec09826ffa0516b3504");scenarioId=652945&schoolId=1  
			get.addHeader("Apikey", apikey);
			RequestConfig requestConfig = RequestConfig.custom()  
			        .setConnectTimeout(5000).setConnectionRequestTimeout(5000)  
			        .setSocketTimeout(5000).build();  
			get.setConfig(requestConfig); 
			response = client.execute(get);
			String result = "";
            if(response.getStatusLine().getStatusCode() ==  HttpStatus.SC_OK){
            	 HttpEntity entity = response.getEntity();
            	 result = EntityUtils.toString(entity,"UTF-8");//此处要用UTF-8编码，否则tomcat请求返回会中文乱码
                 log.info("调用h3c数据URL:"+url+",返回结果:"+result);
            }else{
            	log.error("调用远程api失败，url:"+url+",返回结果:"+result);
            }
            System.out.println(result);
            return result;
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}finally{
			try {
				if(response!=null){
					response.close();
				}
				if(client!=null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 获取高字节16进制
	 * @param value
	 * @return
	 */
	public static String getHightByte(int value) {
		String hightString = "00";
		String hexString = Integer.toHexString(value);
		if(hexString.length()>2 && hexString.length()<=4){
			hightString = hexString.substring(0, hexString.length()-2);
		}
		return hightString;
    }
	/**
	 * 获取低字节10进制值
	 * @param value
	 * @return
	 */
	public static int getLowByteInt(int value) {
		String lowString = "";
		String hexString = Integer.toHexString(value);
		if(hexString.length()>2){
			lowString = hexString.substring(hexString.length()-2, hexString.length());
		}else{
			lowString = hexString;
		}
		return Integer.parseInt(lowString, 16);
	}
	
	public static void main(String[] args) {
		int value = 16129;
		System.out.println(getHightByte(value));
		System.out.println(getLowByteInt(value));
	}
	
}
