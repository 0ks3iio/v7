package net.zdsoft.eclasscard.data.action.show;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.FileUtils;
import net.zdsoft.framework.utils.RemoteCallUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
@Controller
@RequestMapping("/eccShow/eclasscard")
public class EccAppVersionAction {
	public static final String ECC_APP_VERSION = "ecc.app.version";
	public static final String ECC_APP_VERSION_SHOW = "ecc.app.version.show";
	public static final String ECC_APP_VERSION_CONTENT = "ecc.app.version.content";
	public static final String ECC_APP_DOWNLOAD_URL = "ecc.app.download.url";
	public static final String ECC_APP_DOWNLOAD_URL_HK_01="ecc.app.download.hk.url";
	
	@ResponseBody
	@RequestMapping("/app/version")
	public String getIndexUrl(HttpServletRequest request){
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + request.getContextPath();
		JSONObject returnObject = new JSONObject();
		String version = "";
		String versionShow = "";
		String content = "";
		String downloadUrl = "";
		Map<String, String>  eccAppVersionMap = getEccAppVersionMap();
		if(eccAppVersionMap.containsKey(ECC_APP_VERSION)){
			version = eccAppVersionMap.get(ECC_APP_VERSION);
		}
		if(eccAppVersionMap.containsKey(ECC_APP_VERSION_SHOW)){
			versionShow = eccAppVersionMap.get(ECC_APP_VERSION_SHOW);
		}
		if(eccAppVersionMap.containsKey(ECC_APP_VERSION_CONTENT)){
			content = eccAppVersionMap.get(ECC_APP_VERSION_CONTENT);
		}
		if(eccAppVersionMap.containsKey(ECC_APP_DOWNLOAD_URL)){
			downloadUrl = eccAppVersionMap.get(ECC_APP_DOWNLOAD_URL);
		}
		if(StringUtils.isBlank(version)){
			version = "0";
		}
		returnObject.put("version", Integer.valueOf(version));
		returnObject.put("versionShow", versionShow);
		returnObject.put("content", content);
		returnObject.put("downloadUrl", basePath+downloadUrl);
		return RemoteCallUtils.returnResultJson(returnObject);
	}
	
	//下载apk
	@RequestMapping("/download")
	public String download(HttpServletResponse response){
		String filePath = "bp/classBrand.apk";
		String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
				"sysOptionRemoteService").findValue(Constant.FILE_URL);// 文件系统地址
//		fileSystemPath = "http://192.168.22.35/file";
		if (!fileSystemPath.endsWith(File.separator)) {
			fileSystemPath += File.separator;
		}
		return "redirect:"+fileSystemPath+filePath;
	}
	
	@ResponseBody
	@RequestMapping("/app/versionhk")
	public String getIndexUrlHk(HttpServletRequest request){
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + request.getContextPath();
		JSONObject returnObject = new JSONObject();
		String version = "";
		String versionShow = "";
		String content = "";
		String downloadUrl = "";
		Map<String, String>  eccAppVersionMap = getEccAppVersionMap();
		if(eccAppVersionMap.containsKey(ECC_APP_VERSION)){
			version = eccAppVersionMap.get(ECC_APP_VERSION);
		}
		if(eccAppVersionMap.containsKey(ECC_APP_VERSION_SHOW)){
			versionShow = eccAppVersionMap.get(ECC_APP_VERSION_SHOW);
		}
		if(eccAppVersionMap.containsKey(ECC_APP_VERSION_CONTENT)){
			content = eccAppVersionMap.get(ECC_APP_VERSION_CONTENT);
		}
		if(eccAppVersionMap.containsKey(ECC_APP_DOWNLOAD_URL_HK_01)){
			downloadUrl = eccAppVersionMap.get(ECC_APP_DOWNLOAD_URL_HK_01);
		}
		if(StringUtils.isBlank(version)){
			version = "0";
		}
		returnObject.put("version", Integer.valueOf(version));
		returnObject.put("versionShow", versionShow);
		returnObject.put("content", content);
		returnObject.put("downloadUrl", basePath+downloadUrl);
		return RemoteCallUtils.returnResultJson(returnObject);
	}
	
	//下载apk
	@RequestMapping("/downloadhk01")
	public String downloadHk01(HttpServletResponse response){
		String filePath = "bp/hkClassBrand.apk";
		String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
				"sysOptionRemoteService").findValue(Constant.FILE_URL);// 文件系统地址
//			fileSystemPath = "http://192.168.22.35/file";
		if (!fileSystemPath.endsWith(File.separator)) {
			fileSystemPath += File.separator;
		}
		return "redirect:"+fileSystemPath+filePath;
	}
	
	public Map<String, String> getEccAppVersionMap(){
		Map<String, String>  eccAppVersionMap = new HashMap<String, String>();
		if(eccAppVersionMap.size()>0)
			return eccAppVersionMap;
		
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource eccResource = null;
		try {
			eccResource = new FileSystemResource("/opt/server_data/v7/eccAppVersion.properties");
		} catch (Exception e) {
		}
		
		if(!eccResource.isReadable()){
			eccResource = resourceLoader.getResource("conf/eccAppVersion.properties");
		}else{
		}
		
		if(!eccResource.isReadable())
			return eccAppVersionMap;
		
		try {
			Properties p =FileUtils.readProperties(eccResource.getInputStream());
			
			for (Object key : p.keySet()) {
	            String value = p.getProperty(key.toString());
	            eccAppVersionMap.put(key.toString(), value);
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return eccAppVersionMap;
	}
	
	public static void main(String[] args) {
//		EccAppVersionAction action = new EccAppVersionAction();
//		String version = "";
//		String content = "";
//		String downloadUrl = "";
//		Map<String, String>  eccAppVersionMap = action.getEccAppVersionMap();
//		if(eccAppVersionMap.containsKey(ECC_APP_VERSION)){
//			version = eccAppVersionMap.get(ECC_APP_VERSION);
//		}
//		if(eccAppVersionMap.containsKey(ECC_APP_VERSION_CONTENT)){
//			content = eccAppVersionMap.get(ECC_APP_VERSION_CONTENT);
//		}
//		if(eccAppVersionMap.containsKey(ECC_APP_DOWNLOAD_URL)){
//			downloadUrl = eccAppVersionMap.get(ECC_APP_DOWNLOAD_URL);
//		}
//		System.out.println(Integer.valueOf(version));
//		try {
//			System.out.println(URLEncoder.encode(content, "UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(downloadUrl);
	}
}
