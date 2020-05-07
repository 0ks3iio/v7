package net.zdsoft.desktop.action;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.desktop.constant.DeskTopConstant;
import net.zdsoft.desktop.dto.FlowchartDto;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.system.dto.server.ModelDto;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.remote.service.ModelRemoteService;
import net.zdsoft.system.remote.service.ServerRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/flowchart")
public class FlowchartAction extends DeskTopBaseAction{
	
	@Autowired
	private ModelRemoteService modelRemoteService;
	@Autowired
	private ServerRemoteService serverRemoteService;

	@ResponseBody
	@RequestMapping("/index")
	public String index(ModelMap map, HttpServletRequest request){
		List<FlowchartDto> flowcharDtoList = new ArrayList<FlowchartDto>();
		Integer[] modelIds = new Integer[]{3000,3004,509,99029,99011,73101,34020,34027,34031,34032,34033,73103,73102};
		List<Model> modelList = SUtils.dt(modelRemoteService.findListByIds(modelIds), new TypeReference<List<Model>>(){});
		Model oneModel = SUtils.dc(modelRemoteService.findOneById(99012), Model.class);
		if (oneModel == null) {
			oneModel = SUtils.dc(modelRemoteService.findOneById(99058), Model.class);
		}
		modelList.add(oneModel);
		Map<Integer, Model> modelMap = EntityUtils.getMap(modelList, Model::getId);
		//有权限的所有模块
		List<Model> sessionModelList = SUtils.dt(modelRemoteService.findByUserId(getLoginInfo().getUserId()),new TypeReference<List<Model>>(){});
		Map<Integer, Model> modelIdMap = EntityUtils.getMap(sessionModelList, Model::getId);
		
		Set<Integer> subSystemIds=EntityUtils.getSet(modelList, Model::getSubSystem);
				
				
		Integer[][] models = new Integer[][]{{3000,3004,509,99029,99011},{73101,73101,73101,73101},{73101,73101,73101,73101,73101},{34020,99012,34027,34031,34032,34033},{73103,73103,73102}};
		Map<String,String> modelNamesMap = modelNamesMap();
		FlowchartDto dto = null;
		List<FlowchartDto> dtoList = null;
		List<Server> servers = SUtils.dt(
				serverRemoteService.findListByIn("subId",
						subSystemIds.toArray(new Integer[0])),
				new TypeReference<List<Server>>() {
				});
		
		Map<Integer, Server> serverSubIdMap = EntityUtils.getMap(servers,
				Server::getSubId);
		User user = User.dc(userRemoteService.findOneById(getLoginInfo()
				.getUserId(), true));
		Map<Integer, ModelDto> modelDtoMap = new HashMap<Integer,ModelDto>();
		boolean isSecondUrl = sysOptionRemoteService.isSecondUrl(getRequest().getServerName());
		for (Model model : modelList) {
			Server server = serverSubIdMap.get(model.getSubSystem());
			ModelDto modelDto = new ModelDto();
			modelDto.setModel(model);

			if (server == null) {
				LOG.error("该模块所在的子系统没有在Server中配置,modelId:" + model.getId());
				continue;
			}
			String url = isSecondUrl ? server.getSecondUrl() : server.getUrl();
			if (Evn.isDevModel() && StringUtils.equals("7", model.getVersion())) {
				url = UrlUtils.getPrefix(request);
			}
			if (StringUtils.isBlank(url)) {
				LOG.error("该模块所在的子系统没有在Server中配置URL,modelId:" + model.getId());
				continue;
			}
			if (model.getOpenType() == null) {
				model.setOpenType(DeskTopConstant.MODEL_OPEN_TYPE_IFRAME);
			}
			String modelFullURL = UrlUtils.ignoreLastRightSlash(url) + "/"
					+ UrlUtils.ignoreFirstLeftSlash(model.getUrl());
			String fullUrl = StringUtils.EMPTY;
			if (DeskTopConstant.MODEL_OPEN_TYPE_DIV.equals(model.getOpenType())) {
				fullUrl = modelFullURL;
			}
			else if (DeskTopConstant.MODEL_OPEN_TYPE_IFRAME.equals(model.getOpenType())
					|| DeskTopConstant.MODEL_OPEN_TYPE_NEW.equals(model.getOpenType())) {
				if ( StringUtils.startsWithIgnoreCase(model.getUrl(), "http") ) {
					fullUrl = model.getUrl();
				}
				else if ("7".equals(StringUtils.trim(model.getVersion()))) {
					fullUrl = modelFullURL;
				} else {
					// 6.0 5.0 跳转 参数超过两个 passport那边会丢弃
					Map<String, String> modelParams = UrlUtils.getParameters(modelFullURL);
					if (modelFullURL.indexOf("?")>0) {
						String newModelFullUrl = modelFullURL.substring(0, modelFullURL.indexOf("?"));
						if (CollectionUtils.size(modelParams) > 1) {
							int first = 0;
							for (Map.Entry<String, String> entry : modelParams.entrySet()) {
								if (first == 0) {
									newModelFullUrl = UrlUtils.addQueryString(newModelFullUrl,  entry.getKey(), entry.getValue());
								}else {
									newModelFullUrl = UrlUtils.addQueryString(newModelFullUrl,  "{"+entry.getKey()+"}", entry.getValue());
								}
								first ++;
							}
							modelFullURL = newModelFullUrl;
						}
						modelFullURL = UrlUtils.addQueryString(modelFullURL,"{appId}="+Objects.getVaule(model.getSubSystem(),"-1"));
					} else {
						modelFullURL = modelFullURL + "?appId=" + Objects.getVaule(model.getSubSystem(),"-1");
					}
					fullUrl = UrlUtils.ignoreLastRightSlash(url) + "/" + DeskTopConstant.UNIFY_LOGIN_URL + "?" + "url="
								+ modelFullURL + "&uid=" + user.getUsername()+"&appId="+Objects.getVaule(model.getSubSystem(),"-1");
					if ( DeskTopConstant.MODEL_OPEN_TYPE_DIV.equals(model.getOpenType()) ) {
						model.setOpenType(DeskTopConstant.MODEL_OPEN_TYPE_IFRAME);
					}

				}
			} else {
				LOG.error("未知的打开方式，modelId:" + model.getId());
				continue;
			}
			modelDto.setFullUrl(fullUrl);
			modelDtoMap.put(model.getId(), modelDto);

		}
		
		
		for (int i = 0; i < models.length; i++) {
			dto = new FlowchartDto();
			dtoList = new ArrayList<FlowchartDto>();
			for (int j = 0; j < models[i].length; j++) {
				if (models[i][j] != null) {
					Model model = modelIdMap.get(models[i][j]);
					if (model == null && models[i][j] == 99012) {
						model = modelIdMap.get(99058);
					}
					if (j == 0) {
						if (model == null) {
							model = modelMap.get(models[i][j]);
						} 
						if (modelNamesMap.containsKey(""+i+j)) {
							dto.setTitle(modelNamesMap.get(""+i+j));
						} else {
							dto.setTitle(model.getName());
						}
					} else {
						FlowchartDto dtos = new FlowchartDto();
						if (model == null) {
							model = modelMap.get(models[i][j]);
							if (model == null && models[i][j] == 99012) {
								model = modelMap.get(99058);
							}
							dtos.setAuthority(false);
						} else {
							dtos.setAuthority(true);
						}
						if (modelNamesMap.containsKey(""+i+j)) {
							dtos.setTitle(modelNamesMap.get(""+i+j));
						} else {
							dtos.setTitle(model.getName());
						}
						if ("13".equals(""+i+j)) {
							dtos.setAuthority(false);
						}
						
						
						if(modelDtoMap.containsKey(model.getId())){
							dtos.setUrl(modelDtoMap.get(model.getId()).getFullUrl());
						}else{
							dtos.setUrl(model.getUrl());
						}
						dtos.setSerialNumber(String.valueOf(models[i][j]));
						dtos.setStyle(String.valueOf(model.getOpenType()));
						dtoList.add(dtos);
					}
				}
			}
			dto.setDtos(dtoList);
			flowcharDtoList.add(dto);
		}
		JSONObject json = new JSONObject();
		json.put("dtoList", flowcharDtoList);
		return json.toJSONString();
	}
	
	
	public Map<String,String> modelNamesMap() {
		Map<String,String> map = new HashMap<String,String>();
		map.put("11","新建选课");
		map.put("12","发布选课");
		map.put("13","学生选课");
		map.put("20","走班开班");
		map.put("21","设置参考成绩");
		map.put("22","选课结果查询");
		map.put("23","手动自动开班");
		map.put("24","最终确定方案");
		map.put("30","排课管理");
		map.put("40","课程查询");
		return map;
	}
}
