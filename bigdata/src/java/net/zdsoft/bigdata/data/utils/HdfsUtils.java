package net.zdsoft.bigdata.data.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;

import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by wangdongdong on 2018/11/6 15:45.
 */
public class HdfsUtils {

	private static FileSystem hdfs = null;

	public static String businessFilePath = "/spark/job/";

	/**
	 * 拷贝到hsfs
	 *
	 * @param tempFilePath
	 * @param realPath
	 * @param fileName
	 * @throws IOException
	 */
	public static void copyFileToHdfs(String tempFilePath, String realPath,
			String fileName, String hdfsUrl) throws IOException,
			URISyntaxException {
		InputStream in = new FileInputStream(tempFilePath);
		copyFileStreamToHdfs(in, realPath, fileName, hdfsUrl);
	}

	public static void copyFileStreamToHdfs(InputStream in, String realPath,
			String fileName, String hdfsUrl) throws IOException,
			URISyntaxException {
		Path hdfsPath = new Path(realPath);
		if (!getFileSystem(hdfsUrl).exists(hdfsPath)) {
			getFileSystem(hdfsUrl).mkdirs(hdfsPath);
		}
		FSDataOutputStream fos = getFileSystem(hdfsUrl).create(
				new Path(realPath + fileName));
		// 设置一个缓存数组可以一次读取多个字节，提高了效率
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = in.read(buffer)) != -1) {
			fos.write(buffer, 0, length);
			fos.flush();
		}
		fos.close();
		in.close();
	}

	private static FileSystem getFileSystem(String hdfsUrl)
			throws URISyntaxException, IOException {
		if (hdfs != null) {
			return hdfs;
		}
		// 读取配置文件
		Configuration conf = new Configuration();
		// 返回指定的文件系统,如果在本地测试，需要使用此种方法获取文件系统
		URI uri = new URI(hdfsUrl.trim());
		return FileSystem.get(uri, conf);
	}

	public static Json getHdfsUsage(String hdfsApiUrl) {

		Json result = new Json();
		result.put("totalsize", "0MB");
		result.put("recordnum", "0");

		if (StringUtils.isBlank(hdfsApiUrl)) {
			return result;
		}
		String result4Json = httpGetWithJson(hdfsApiUrl
				+ "?user.name=root&op=GETCONTENTSUMMARY");

		if (StringUtils.isEmpty(result4Json)) {
			return null;
		}
		if ("error".equals(result4Json)) {
			return null;
		}

		JSONObject obj = JSONObject.parseObject(result4Json);
		String usageJson = obj.getString("ContentSummary");
		JSONObject usage = JSONObject.parseObject(usageJson);

		Long size = usage.getLong("length");
		DecimalFormat df = new DecimalFormat("0.00");
		float totalSize = (float) size / 1024 / 1024;

		if (totalSize < 1024) {
			result.put("totalsize", df.format(totalSize) + "MB");
		} else {
			if (totalSize / 1024 < 1024) {
				result.put("totalsize", df.format(totalSize / 1024) + "GB");
			} else {
				result.put("totalsize", df.format(totalSize / 1024 / 1024)
						+ "TB");
			}
		}
		result.put("recordnum", usage.getLong("fileCount"));
		return result;
	}

	private static String httpGetWithJson(String url) {
		HttpGet get = null;
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			get = new HttpGet(url);
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(3000).setConnectTimeout(3000).build();// 设置请求和传输超时时间
			get.setConfig(requestConfig);
			// 构造消息头
			get.setHeader("Content-type", "application/json; charset=utf-8");
			get.setHeader("Connection", "Close");

			HttpResponse response = httpClient.execute(get);
			// 检验返回码
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				System.out.println("请求出错:" + statusCode);
				return "error";
			} else {
				HttpEntity result = response.getEntity();
				return entityToString(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		} finally {
			if (get != null) {
				try {
					get.releaseConnection();
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static String entityToString(HttpEntity entity) throws IOException {
		String result = null;
		if (entity != null) {
			long lenth = entity.getContentLength();
			if (lenth != -1 && lenth < 2048) {
				result = EntityUtils.toString(entity, "UTF-8");
			} else {
				InputStreamReader reader1 = new InputStreamReader(
						entity.getContent(), "UTF-8");
				CharArrayBuffer buffer = new CharArrayBuffer(2048);
				char[] tmp = new char[1024];
				int l;
				while ((l = reader1.read(tmp)) != -1) {
					buffer.append(tmp, 0, l);
				}
				result = buffer.toString();
			}
		}
		return result;
	}

}
