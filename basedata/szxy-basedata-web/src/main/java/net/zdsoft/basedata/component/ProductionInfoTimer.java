package net.zdsoft.basedata.component;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.dao.SysVersionDao;
import net.zdsoft.basedata.entity.SysVersion;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.system.service.config.SysOptionService;

@Component
@Lazy(false)
public class ProductionInfoTimer {

	@Autowired
	private UnitService unitService;

	@Autowired
	private SysOptionService sysOptionService;

	@Autowired
	private SysVersionDao sysVersionDao;

	private RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Scheduled(fixedDelayString = "604800000")
	public void pushProductionServerInfo() {
			try {
				Unit topUnit = unitService.findTopUnit(null);
				if (topUnit == null) {
					return;
				}
				List<SysVersion> versions = sysVersionDao.findAll();
				if (CollectionUtils.isEmpty(versions)) {
					return;
				}
				String indexUrl = sysOptionService.findValueByOptionCode("INDEX.URL");
				String token;
				RestTemplate restTemplate = restTemplate();
				String url = "http://api.msyk.cn/dcAuth/login/dcApplication/byToken?token=%s";
				String resultStr = restTemplate.getForObject(String.format(url, "18dade42902d459089fb58048bf3e913"),
						String.class);
				JSONObject result = JSONObject.parseObject(resultStr);
				String eis7ProductionId = "EIS-E7";
				String eis7Version = null;
				Date versionDate = null;
				if ("200".equals(result.getString("code"))) {
					token = result.getString("data");
					for (SysVersion version : versions) {
						if (StringUtils.equals(version.getProductId(), eis7ProductionId)) {
							eis7Version = Optional.ofNullable(eis7Version).orElse(version.getCurVersion());
							versionDate = Optional.ofNullable(versionDate).orElse(version.getCreateDate());
							if (versionDate != null && version.getCreateDate() != null
									&& versionDate.compareTo(version.getCreateDate()) <= 0) {
								versionDate = version.getCreateDate();
								eis7Version = version.getCurVersion();
							}
						}
					}
					
					String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
					String ipPort = getIpPort();
					JSONObject server = new JSONObject();
					server.put("ipIntranet", ipPort);
					server.put("ip", indexUrl);
					server.put("path", path);
					server.put("platformName", topUnit.getUnitName());
					server.put("productionCode", eis7ProductionId);
					server.put("productionVersion", eis7Version);
					HttpHeaders serverHeaders = new HttpHeaders();
					serverHeaders.setContentType(MediaType.valueOf("application/json;UTF-8"));
					serverHeaders.add("authorization", token);
					HttpEntity<JSONObject> serverHttpEntity = new HttpEntity<>(server, serverHeaders);
					String serverUrl = "http://api.msyk.cn/dcProductionInfo/view/v1/productionInfo/pushProductionServerInfo";
					restTemplate.exchange(serverUrl, HttpMethod.POST, serverHttpEntity, String.class);
				}
			} catch (Exception e) {
			}
	}

	@Scheduled(fixedDelayString = "604800000")
	public void pushProductionInfo() {
		RedisUtils.get("production-info-timer-v7", 604000, () -> {
			try {
				Unit topUnit = unitService.findTopUnit(null);
				if (topUnit == null) {
					return null;
				}
				List<SysVersion> versions = sysVersionDao.findAll();
				if (CollectionUtils.isEmpty(versions)) {
					return null;
				}
				String indexUrl = sysOptionService.findValueByOptionCode("INDEX.URL");
				String token;
				RestTemplate restTemplate = restTemplate();
				String url = "http://api.msyk.cn/dcAuth/login/dcApplication/byToken?token=%s";
				String resultStr = restTemplate.getForObject(String.format(url, "18dade42902d459089fb58048bf3e913"),
						String.class);
				JSONObject result = JSONObject.parseObject(resultStr);
				String eis7ProductionId = "EIS-E7";
				String eis7Version = null;
				Date versionDate = null;
				if ("200".equals(result.getString("code"))) {
					token = result.getString("data");
					for (SysVersion version : versions) {
						if (StringUtils.equals(version.getProductId(), eis7ProductionId)) {
							eis7Version = Optional.ofNullable(eis7Version).orElse(version.getCurVersion());
							versionDate = Optional.ofNullable(versionDate).orElse(version.getCreateDate());
							if (versionDate != null && version.getCreateDate() != null
									&& versionDate.compareTo(version.getCreateDate()) <= 0) {
								versionDate = version.getCreateDate();
								eis7Version = version.getCurVersion();
							}
						}
						
						HttpHeaders headers = new HttpHeaders();
						headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
						headers.add("authorization", token);
						JSONObject productionDeploy = new JSONObject();
						productionDeploy.put("productionCode", version.getProductId());
						productionDeploy.put("productionVersion", version.getCurVersion());
						productionDeploy.put("regionCode", topUnit.getRegionCode());
						productionDeploy.put("platformName", topUnit.getUnitName());
						productionDeploy.put("registrationTime", topUnit.getCreationTime());
						productionDeploy.put("modifyTime", version.getCreateDate());
						productionDeploy.put("webUrl", indexUrl);
						HttpEntity<JSONObject> entity = new HttpEntity<>(productionDeploy, headers);
						String deployUrl = "http://api.msyk.cn/dcProductionInfo/view/v1/productionInfo/pushProductionDeployment";
						restTemplate.postForObject(deployUrl, entity, String.class);
					}

					if (StringUtils.isNotBlank(eis7Version)) {
						String contextRoot = getClass().getResource("/").getPath() + "../lib/";
						File libDir = new File(contextRoot);
						File[] files = libDir.listFiles();
						JSONArray jarInfos = new JSONArray();
						for (File file : files) {
							String baseName = FilenameUtils.getBaseName(file.getName());
							JSONObject jarInfo = new JSONObject();
							jarInfo.put("productionCode", eis7ProductionId);
							jarInfo.put("productionVersion", eis7Version);
							jarInfo.put("jarName", baseName);
							jarInfo.put("jarVersion", "-");
							jarInfos.add(jarInfo);
						}
						HttpHeaders headers = new HttpHeaders();
						headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
						headers.add("authorization", token);
						HttpEntity<JSONArray> entity = new HttpEntity<>(jarInfos, headers);
						String deployUrl = "http://api.msyk.cn/dcProductionInfo/view/v1/productionInfo/pushProductionJarsInfo?productionCode=%s&productionVersion=%s";
						restTemplate.postForObject(String.format(deployUrl, eis7ProductionId, eis7Version), entity,
								String.class);
					}
				}
			} catch (Exception e) {
				return null;
			}
			return "true";
		});
	}

	private String getIpPort() {
		InetAddress address;
		try {
			address = InetAddress.getLocalHost();
			return String.format("%s:%s", address.getHostAddress(), getLocalPort());            
		} catch (UnknownHostException e) {
			return "";
		}
	}
	
	public String getLocalPort(){
		try {
			MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
			Set<ObjectName> objectNames = mBeanServer.queryNames(new ObjectName("*:type=Connector,*"), null);
			if (objectNames == null || objectNames.size() <= 0) {
				return "";
			}
			for (ObjectName objectName : objectNames) {
				String protocol = String.valueOf(mBeanServer.getAttribute(objectName, "protocol"));
				String port = String.valueOf(mBeanServer.getAttribute(objectName, "port"));
				// windows下属性名称为HTTP/1.1, linux下为org.apache.coyote.http11.Http11NioProtocol
				if (protocol.equals("HTTP/1.1") || protocol.equals("org.apache.coyote.http11.Http11NioProtocol")) {
					return port;
				}
			}
			return "";
		}
		catch(Exception e) {
			return "";
		}
	}

}
