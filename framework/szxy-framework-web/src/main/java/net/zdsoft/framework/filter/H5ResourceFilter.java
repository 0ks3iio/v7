package net.zdsoft.framework.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.zdsoft.framework.utils.EncryptAES;
import net.zdsoft.framework.utils.FilterParamUtils;

import org.apache.commons.lang3.StringUtils;

/**
 * 微课对接手机端html、js、css文件版本号控制
 * 
 * @author like 2017-07-17
 */
public class H5ResourceFilter implements Filter {
	/*
	 * 手机端相关的html、js、css文件版本号控制
	 * 1、启动服务时 初始化需要控制版本号的html、js、css文件 并存放到对应的map（key：文件路径，value：文件md5摘要信息）
	 * 2、请求html、js、css资源时，判断版本号 若一致则放行，否则替换为新的版本号
	 * 
	 * fliter控制版本号可行性分析： 1、客户端第一次访问html文件以及页面上引用的js、css文件 会走进过滤器，过滤器会添加一个版本号并重定向
	 * 2、浏览器中对于重定向之前的url链接不会缓存 且没有response信息，而会对重定向之后的url链接做缓存
	 * 3、客户端第二次访问html页面以及页面上引用的js、css时 会用页面上引用的js、css地址请求服务器资源
	 * 因为这些地址没有做过缓存，所以依旧会进入过滤器 4、若第二次服务器重定向的地址（主要是版本号）跟浏览器中缓存的地址相同
	 * 则浏览器取缓存中的文件信息， 否则取服务器上的资源
	 * 
	 * 备注：理论上新增的页面都没有问题，若是之前已开发的手机端页面 第一次使用Filter进行版本号控制，则需要修改html页面上引用的js、css地址
	 * 以保证浏览器中没有html中所引用的js、css地址的缓存（这样才能进入过滤器进行版本号控制）
	 */
	
	//默认版本号
	static final String DEFAULT_VERSION = "1.0";
	
	static final String VERSION_KEY = "rfv";//resource-filter-version

	// 是否只关联手机端相关页面 默认为false--只关联手机端页面
	private boolean isOpenAll;
	// 需要过滤的路径
	private String[] mobileFilterPaths;

	// 资源文件map 服务启动时初始化
	private Map<String, String> resourceMap;// key统一转换为‘/’网络路径 value=版本号

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		isOpenAll = "true".equals(filterConfig.getInitParameter("isOpenAll")) ? true
				: false;
		mobileFilterPaths = FilterParamUtils.getParamValues(filterConfig,
				"mobileFilterPaths");

		resourceMap = new HashMap<String, String>();

		String rootPath = filterConfig.getServletContext().getRealPath("/");
		if (isOpenAll) {// 扫描根目录下所有文件
			File file = new File(rootPath);
			initResourceMap(rootPath, file);
		} else {
			// 扫描手机端文件
			if (mobileFilterPaths == null || mobileFilterPaths.length == 0)
				return;
			File file = null;
			for (String filterPath : mobileFilterPaths) {
				file = new File(rootPath + filterPath);
				if (file.exists()) {
					initResourceMap(rootPath, file);
				}
			}
		}

//		for (String key : resourceMap.keySet()) {
//			System.out.println("移动端js:" + key + "===" + resourceMap.get(key));
//		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String servletPath = httpRequest.getServletPath();

		if (FilterParamUtils.containsPath(servletPath, mobileFilterPaths)) {
			/*
			 * 手机端相关的html、js、css文件 版本号为该文件最后修改时间long，若返回的版本号跟浏览器中缓存的js版本号一致
			 * 则浏览器取缓存中的文件
			 */
			String requestUrl = httpRequest.getRequestURL().toString();
			if (!servletPath.startsWith("/")) {
				servletPath = "/" + servletPath;
			}

			String requestVersion = httpRequest.getParameter(VERSION_KEY);
			String version = DEFAULT_VERSION;// 默认版本号
			if (servletPath.endsWith(".html") || servletPath.contains(".js")
					|| servletPath.contains(".css")) {
				if (resourceMap.containsKey(servletPath))
					version = resourceMap.get(servletPath);
			}

			/*
			 * 版本号一致则放行
			 */
			if (version.equals(requestVersion)) {
				filterChain.doFilter(request, response);
				return;
			}

			/*
			 * 更新版本号
			 */
			String queryStr = httpRequest.getQueryString();
			if (StringUtils.isBlank(queryStr)) {
				requestUrl += "?" + VERSION_KEY + "=" + version;
			} else {
				requestUrl += "?" + queryStr;
				if (requestUrl.indexOf(VERSION_KEY) == -1) {
					requestUrl += "&" + VERSION_KEY + "=" + version;
				} else {
					requestUrl = replaceAccessTokenReg(requestUrl, VERSION_KEY,
							version);// 替换参数
				}
			}

			httpResponse.sendRedirect(requestUrl);
			return;
		}
		filterChain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		if (resourceMap != null)
			resourceMap.clear();
	}

	/**
	 * 初始化map，取配置目录下的html、js、css文件
	 * 
	 * @param rootPath
	 * @param file
	 */
	private void initResourceMap(String rootPath, File file) {
		if (file == null || !file.exists())
			return;
		if (file.isDirectory()) {
			// 遍历目录下所有js文件
			File[] files = file.listFiles();
			if (files == null)
				return;
			for (File f : files) {
				if (f.isDirectory()) {
					initResourceMap(rootPath, f);
				} else {
					String path = f.getAbsolutePath().replace(rootPath, "");
					if (StringUtils.isNotBlank(path)) {// 统一转换为网络路径
						path = path.replaceAll(
								Matcher.quoteReplacement(File.separator), "/");
						if (!path.startsWith("/")) {
							path = "/" + path;
						}
					}
					
					if (path.endsWith(".html") || path.contains(".js")
							|| path.contains(".css")) {
						//文件摘要信息
						String md5digest = getMD5Digest(f);
						resourceMap.put(path, md5digest);// 文件的最后修改时间作为版本号
					}
				}
			}
		}
	}

	/**
	 * 替换参数的值
	 * 
	 * @param url
	 * @param name
	 * @param accessToken
	 * @return
	 */
	private static String replaceAccessTokenReg(String url, String name,
			String accessToken) {
		if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(accessToken)) {
			url = url.replaceAll("(" + name + "=[^&]*)", name + "="
					+ accessToken);
		}
		return url;
	}

	/**
	 * 对文件全文生成MD5摘要
	 * @param file 要加密的文件
	 * @return MD5摘要码
	 */
	public static String getMD5Digest(File file) {
		FileInputStream fis = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			fis = new FileInputStream(file);
			
			byte[] buffer = new byte[2048];
			int length = -1;
			while ((length = fis.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}
			//========摘要生成成功============
			
			byte[] b = md.digest();
			return EncryptAES.parseByte2HexStr(b);
			// 16位加密
			// return buf.toString().substring(8, 24);
		} catch (Exception ex) {
			ex.printStackTrace();
			return DEFAULT_VERSION;
		} finally {
			try {
				if(fis!=null)
					fis.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
