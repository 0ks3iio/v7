package net.zdsoft.basedata.action;

import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.constant.SystemIniConstants;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.SpagoBIRemote;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/basedata")
public class ChartsAction extends BaseAction{
	
//	@Autowired
//	private ChartsService chartsService;
//	@Autowired
//	private ChartsRoleService chartsRoleService;
	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
	
	@ControllerInfo("进入BI图表加载页面")
	@RequestMapping("/charts/page")
	public String showClassByGradeIdPage(String label,String parameters,String width,String height,String iframeId, ModelMap map, HttpSession httpSession) {
//		LoginInfo info = getLoginInfo(httpSession);
//		String userId = info.getUserId();
//		List<Charts> cList = chartsService.findChartsByUserId(userId);
//		if(CollectionUtils.isNotEmpty(cList)){
//			boolean isCan = false;
//			for (Charts charts : cList) {
//				if(label.equals(charts.getDocumentLabel())){
//					isCan = true;
//					break;
//				}
//			}
//			if(!isCan){
//				return errorFtl(map, "没有权限！");
//			}
//		}
//		List<ChartsRole> findByUserId = chartsRoleService.findByUserId(userId);
//		if(CollectionUtils.isEmpty(findByUserId)){
//			return errorFtl(map, "未找到角色！");
//		}
//		map.put("appid", findByUserId.get(0).getAppid());
//		map.put("appkey", findByUserId.get(0).getAppkey());
		//暂时这样处理
//		map.put("appid", "554AB061CE67A252784A413B741F1773");
//		map.put("appkey", "gnkNr8B32a12aaphM2rWQA==");
//		String biUrlCol = systemIniRemoteService.findValue(SystemIniConstants.BI_URL);
//		if(StringUtils.isNotBlank(biUrlCol)){
//			String[] split = biUrlCol.split(",");
//			if(split.length == 3){
//				map.put("appid", split[1]);
//				map.put("appkey", split[2]);
//			}else{
//				map.put("appid", "554AB061CE67A252784A413B741F1773");
//				map.put("appkey", "gnkNr8B32a12aaphM2rWQA==");
//			}
//		}else{
//			map.put("appid", "");
//			map.put("appkey", "");
//		}
//		map.put("label", label);
//		map.put("parameters", parameters);
//		map.put("width", width);
//		map.put("height", height);
//		map.put("iframeId", iframeId);
		
		String biUrl = systemIniRemoteService.findValue(SystemIniConstants.BI_URL);
		try {
			if(StringUtils.isNotBlank(biUrl)){
				String documentUrl = SpagoBIRemote.findDocumentUrl(biUrl, label, parameters);
				map.put("documentUrl", documentUrl);
			}else{
				System.out.println("biUrl参数为空！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("width", width);
		map.put("height", height);
		map.put("iframeId", iframeId);
		return "/basedata/charts/chartsIndex.ftl";
	}
}
