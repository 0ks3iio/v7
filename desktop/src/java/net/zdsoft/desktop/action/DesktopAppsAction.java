package net.zdsoft.desktop.action;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.desktop.constant.DeskTopConstant;
import net.zdsoft.desktop.entity.FunctionArea;
import net.zdsoft.desktop.entity.FunctionAreaDataUrlEnum;
import net.zdsoft.desktop.entity.FunctionAreaDynamicParam;
import net.zdsoft.desktop.entity.FunctionAreaTemplate;
import net.zdsoft.desktop.entity.FunctionAreaUser;
import net.zdsoft.desktop.entity.UserSet;
import net.zdsoft.desktop.service.FunctionAreaService;
import net.zdsoft.desktop.service.FunctionAreaTemplateService;
import net.zdsoft.desktop.service.FunctionAreaUserService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ServletUtils;
import net.zdsoft.framework.utils.SortUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;

/**
 * @author shenke
 * @since 2017/2/16 8:52
 */
@RequestMapping("/desktop/app")
@Controller
public class DesktopAppsAction extends DeskTopBaseAction {

	@Autowired
	private FunctionAreaUserService functionAreaUserService;
	@Autowired
	private FunctionAreaService functionAreaService;
	@Autowired
	private FunctionAreaTemplateService functionAreaTemplateService;

	@ControllerInfo(ignoreLog=1, value="显示功能区内容：{functionAreaUserId}")
	@RequestMapping("/showFunctionArea")
	public String showFunctionArea(String functionAreaUserId, ModelMap map) {
		FunctionAreaUser fau = functionAreaUserService.findOne(functionAreaUserId);
		FunctionArea fa = functionAreaService.findOne(fau.getFunctionAreaId());
		FunctionAreaTemplate ct = functionAreaTemplateService.findOne(fa.getTemplateId());
		String data = null;
		if (StringUtils.isNotBlank(data)) {
			map.put("data", JSONObject.parse(data));
		} else {
			try {
				String dataUrl = fa.getDataUrl();
				if (!StringUtils.startsWith(dataUrl, "http")) {
					dataUrl = ServletUtils.getWebsiteRoot(getRequest()) + dataUrl;
				}
				LoginInfo loginInfo = getLoginInfo();
				if (loginInfo != null) {
					String userId = loginInfo.getUserId();
					String unitId = loginInfo.getUnitId();
					String ownerId = loginInfo.getOwnerId();
					int ownerType = loginInfo.getOwnerType().intValue();
					int unitClass = loginInfo.getUnitClass().intValue();
					// 处理占位符
					if (StringUtils.indexOf(dataUrl, FunctionAreaDataUrlEnum.USERID.getMatchText()) >= 0) {
						dataUrl = StringUtils.replace(dataUrl, FunctionAreaDataUrlEnum.USERID.getMatchText(),
								userId);
					}
					if (StringUtils.indexOf(dataUrl, FunctionAreaDataUrlEnum.UNITID.getMatchText()) >= 0) {
						dataUrl = StringUtils.replace(dataUrl, FunctionAreaDataUrlEnum.UNITID.getMatchText(),
								unitId);
					}
					if (StringUtils.indexOf(dataUrl, FunctionAreaDataUrlEnum.OWNERID.getMatchText()) >= 0) {
						dataUrl = StringUtils.replace(dataUrl, FunctionAreaDataUrlEnum.OWNERID.getMatchText(),
								ownerId);
					}
					if (StringUtils.indexOf(dataUrl, FunctionAreaDataUrlEnum.STUDENTID.getMatchText()) >= 0) {
						User user = SUtils.dc(userRemoteService.findOneById(getLoginInfo().getUserId(), true), User.class);
						if (Objects.equals(User.OWNER_TYPE_FAMILY, user.getOwnerType())) {
							// 家庭作业
							Family family = SUtils.dc(familyRemoteService.findOneById(user.getOwnerId(), true), Family.class);
							dataUrl = StringUtils.replace(dataUrl, FunctionAreaDataUrlEnum.STUDENTID.getMatchText(),
									family.getStudentId());
						} else {
							// 其他业务需要
						}
					}

					if (StringUtils.indexOf(dataUrl, "{messageType}") >= 0) {
						dataUrl = StringUtils.replace(dataUrl, "{messageType}", "1");
					}
	
					// 处理动态参数
					if (StringUtils.isNotBlank(fau.getDynamicParam())) {
						if (fau.getDynamicParam().indexOf('?') > -1) {
							dataUrl = UrlUtils.addQueryString(dataUrl,
									fau.getDynamicParam().replace("?", StringUtils.EMPTY));
						} else {
							dataUrl = UrlUtils.addQueryString(dataUrl, fau.getDynamicParam());
						}
					}

					if (!dataUrl.contains("userId")) {
						dataUrl = UrlUtils.addQueryString(dataUrl, "userId", userId);
					}
					if (!dataUrl.contains("ownerType")) {
						dataUrl = UrlUtils.addQueryString(dataUrl, "ownerType", ownerType);
					}
					if (!dataUrl.contains("unitClass")) {
						dataUrl = UrlUtils.addQueryString(dataUrl, "unitClass", unitClass);
					}
					if (!dataUrl.contains("unitId")) {
						dataUrl = UrlUtils.addQueryString(dataUrl, "unitId", unitId);
					}
				}
				
				data = UrlUtils.postWithSession(dataUrl, getRequest().getSession().getId());
				if (StringUtils.startsWith(data, "{")) {
					JSONObject json = JSONObject.parseObject(data);
					// 获取自定义的宽度（12等分）
					Integer col = fau.getColumns();
					if (Objects.equals(UserSet.LAYOUT_TWO2ONE, getUserSetLayout())
							&& FunctionAreaUser.LAYOUT_TYPE_RIGHT.equals(fau.getLayoutType())) {
						json.put("col", 12);
					} else {
						if (col != null && col > 0) {
							json.put("col", col);
						} else {
							json.put("col", 6);
						}
					}
					// 获取自定义的高度
					Integer height = fau.getHeight();
					if (height != null && height > 0) {
						json.put("height", height);
					}
					// 将json格式转为string，以适应页面的类型设置
					if (json.containsKey("jsonData")) {
						String o = JSONUtils.toJSONString(json.get("jsonData"));
						json.put("jsonData", o);
					}
					// 如果数据没有设置功能区标题，则取默认的标题
					if (!json.containsKey("title") || StringUtils.isBlank(json.getString("title"))) {
						json.put("title", fa.getName());
					}
					map.put("data", json);
				}
			} catch (Exception e) {
				LOG.error("show functionare error",e);
			}
		}
		
		if(!map.containsKey("data")) {
			map.put("data", new JSONObject());
		}

		map.put("echartsDivId", UuidUtils.generateUuid());
		
		return ct.getTemplatePath();
	}



	@ControllerInfo(value="显示功能区设置")
	@RequestMapping("/showFunctionAreaUserSet")
	public String doLoadFunctionAreaUserSerPage(String functionAreaUserId, int functionAreaIndex, ModelMap map, RedirectAttributes redirectAttributes){

		FunctionAreaUser functionAreaUser = functionAreaUserService.findOne(functionAreaUserId);
		String functionAreaId = functionAreaUser.getFunctionAreaId();
		FunctionArea functionArea = functionAreaService.findOne(functionAreaId);
		List<FunctionAreaDynamicParam> functionAreaDynamicParams = FunctionAreaDynamicParam.parse(functionArea.getDynamicParam());
		map.addAttribute("dynamicParams",functionAreaDynamicParams);
		map.addAttribute("layout", getUserSetLayout());
		map.addAttribute("height",functionAreaUser.getHeight());
		map.addAttribute("displayOrder",functionAreaUser.getDisplayOrder().toString());
		map.addAttribute("functionAreaUserId",functionAreaUser.getId());
		map.addAttribute("columns", functionAreaUser.getColumns()==null? 12 : functionAreaUser.getColumns());
		map.addAttribute("layoutType",getIntValue(functionAreaUser.getLayoutType()));

		List<FunctionAreaUser> all = functionAreaUserService.findByUserInfo(getUserId(),getLoginInfo().getUnitClass());
		map.addAttribute("maxDisplayOrder",CollectionUtils.size(all));
		
		List<FunctionAreaUser> left = EntityUtils.filter2(all, t->{
				return t.getLayoutType().equals(FunctionAreaUser.LAYOUT_TYPE_LEFT);
		});
				
		List<FunctionAreaUser> right = EntityUtils.filter2(all, t -> {
				return t.getLayoutType().equals(FunctionAreaUser.LAYOUT_TYPE_RIGHT);
		});
		
		map.addAttribute("leftMaxIndex",CollectionUtils.size(left));
		map.addAttribute("rightMaxIndex",CollectionUtils.size(right));
		map.addAttribute("functionAreaIndex",functionAreaIndex);

		Map<String,String> functionAreaUserDynamicParams = null;
		if ( !StringUtils.contains(functionAreaUser.getDynamicParam(),"?") ){
			functionAreaUserDynamicParams = UrlUtils.getParameters("?" + functionAreaUser.getDynamicParam());
		} else {
			functionAreaUserDynamicParams = UrlUtils.getParameters(functionAreaUser.getDynamicParam());
		}

		for (FunctionAreaDynamicParam param : functionAreaDynamicParams) {
			String value = functionAreaUserDynamicParams.get(param.getName());
			param.setValue(value);
		}
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			redirectAttributes.addFlashAttribute(entry.getKey(),entry.getValue());
		}

        if (functionArea.getDataUrl().contains("showCommonUseApp2")) {
            return "forward:/desktop/recommendApp/set";
        }
//        if (functionArea.getDataUrl().contains("showCommonUseApp2")) {
//        	new RecommendAppAction().execute(map);
//        	
//			return "/desktop/homepage/ap/set/operation-scrollBig-set.ftl";
//		}
        if (functionArea.getDataUrl().contains("showCommonUseApp")) {
            return "redirect:/desktop/userApp/set";
        }
        
        //判断是否是课程表  showTimetable 是否是openapi统计图
        if (functionArea.getDataUrl().contains("showTimetable") || functionArea.getDataUrl().contains("showOpenapiCount")) {
        	map.addAttribute("isShowTimetable",false);
        }
        
        
		return "/desktop/homepage/template/functionAreaUserSet.ftl";
	}


	@ResponseBody
	@ControllerInfo("添加功能区")
	@RequestMapping("/addFunctionAreaUser")
	public String addFunctionAreaUser(@RequestBody String functions, ModelMap map){
		
		List<JSONObject> jsonObjects= SUtils.dt(functions, JSONObject.class);
		for (JSONObject jsonObject : jsonObjects) {			
			FunctionAreaUser functionAreaUser = functionAreaUserService.findOwnerByFunctionAreaId(getLoginInfo().getUserId(),jsonObject.getString("id"));       			  
	        saveFunctionAreaUser(functionAreaUser,jsonObject.getString("id"));	      
		}		
		RedisUtils.delBeginWith(FunctionAreaUserService.CACHE_KEY);
		return success("添加成功");
	}



	@RequestMapping(value = "/save/functionAreaUserSet")
	@ControllerInfo("保存功能区设置")
	public String doSaveFunctionAreaUserSet(@RequestBody String jsonString , HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String jsonPath = request.getSession().getServletContext().getRealPath("/desktop/homepage/template/functionAreaUserSet.json");
		getResponse().setContentType("application/json;charset=UTF-8");
		String msg = StringUtils.EMPTY;
		try {
			JSONObject jsonObject = JSONObject.parseObject(jsonString);

			FunctionAreaUser oldFunctionAreaUser = functionAreaUserService.findOne(jsonObject.get("functionAreaUserId").toString());
			if (oldFunctionAreaUser == null){
                return error("当前功能区已被删除，请刷新页面");
            }
			FunctionArea functionArea = functionAreaService.findOne(oldFunctionAreaUser.getFunctionAreaId());
			List<FunctionAreaDynamicParam> dynamicParams =  FunctionAreaDynamicParam.parse(functionArea.getDynamicParam());

			StringBuilder functionAreaUserDynamicParam = new StringBuilder();

			FunctionAreaUser functionAreaUser = new FunctionAreaUser();
			for (Map.Entry<String, Object> key : jsonObject.entrySet()) {
                String name = key.getKey();
                Object value = key.getValue();
                if (EntityUtils.isContainValue(dynamicParams,"name",name)){
                    UrlUtils.addQueryString(functionAreaUserDynamicParam,name+"="+value);
                }else{
                	try {
		                BeanUtils.setProperty(functionAreaUser, name, value);
	                }catch (Exception e){
                		LOG.error("设置参数失败 name {} value {}",name,value);
	                }
                }
            }

            // index 用户页面上看到的位置并且用户可以更改 list index
            Integer htmlIndex = NumberUtils.toInt(String.class.cast(jsonObject.get("index")));

			oldFunctionAreaUser.setDynamicParam(functionAreaUserDynamicParam.toString().replace("?",""));
			oldFunctionAreaUser.setHeight(functionAreaUser.getHeight()!=null?functionAreaUser.getHeight():oldFunctionAreaUser.getHeight());
			oldFunctionAreaUser.setColumns(functionAreaUser.getColumns()!=null?functionAreaUser.getColumns():oldFunctionAreaUser.getColumns());

			List<FunctionAreaUser> oneSideFunctionAreaUsers1 = Lists.newArrayList();
			
			
			//得到当前左右的应用的排序
			List<FunctionAreaUser> all = functionAreaUserService.findByUserInfo(getUserId(),
					getLoginInfo().getUnitClass());


			if (UserSet.LAYOUT_DEFAULT.equals(getUserSetLayout())){
				for (FunctionAreaUser areaUser : all) {
					if ( areaUser.getId().equals(oldFunctionAreaUser.getId()) ) {
						BeanUtils.copyProperties(areaUser, oldFunctionAreaUser);
						areaUser.setDisplayOrder(htmlIndex);
					}else {
						if ( areaUser.getDisplayOrder().equals(htmlIndex)) {
							areaUser.setDisplayOrder(oldFunctionAreaUser.getDisplayOrder());
						}
					}
				}
				oneSideFunctionAreaUsers1 = all;
			} else {
				List<FunctionAreaUser> right = EntityUtils.filter(all, new EntityUtils.Filter<FunctionAreaUser>() {
					@Override
					public boolean doFilter(FunctionAreaUser t) {
						return t.getLayoutType() != FunctionAreaUser.LAYOUT_TYPE_RIGHT;
					}
				});

				SortUtils.ASC(right, "displayOrder");
				Map<String, FunctionAreaUser> rightMap = EntityUtils.getMap(right, "id");

				List<FunctionAreaUser> left = EntityUtils.filter(all, new EntityUtils.Filter<FunctionAreaUser>() {
					@Override
					public boolean doFilter(FunctionAreaUser t) {
						return t.getLayoutType() != FunctionAreaUser.LAYOUT_TYPE_LEFT;
					}
				});
				SortUtils.ASC(left, "displayOrder");
				Map<String, FunctionAreaUser> leftMap = EntityUtils.getMap(left, "id");
            	//位置没有移动
				if (Objects.equals(oldFunctionAreaUser.getLayoutType(), functionAreaUser.getLayoutType())){
					if ( Objects.equals(functionAreaUser.getLayoutType(), FunctionAreaUser.LAYOUT_TYPE_RIGHT) ) {
						//先移除再添加
						if(right.remove(rightMap.get(oldFunctionAreaUser.getId()))) {
							if(htmlIndex>right.size()) {
								right.add(right.size(), oldFunctionAreaUser);
							}else {
								right.add(htmlIndex-1, oldFunctionAreaUser);
							}
						}
	                } else if ( Objects.equals(functionAreaUser.getLayoutType(), FunctionAreaUser.LAYOUT_TYPE_LEFT) ) {
	                	//先移除再添加
						if(left.remove(leftMap.get(oldFunctionAreaUser.getId()))) {
							if(htmlIndex>left.size()) {
								left.add(left.size(), oldFunctionAreaUser);
							}else {
								left.add(htmlIndex-1, oldFunctionAreaUser);
							}
						}
	                }
	            } else {
	            	if ( Objects.equals(functionAreaUser.getLayoutType(), FunctionAreaUser.LAYOUT_TYPE_RIGHT) ) {
						//先移除再添加
						if(left.remove(leftMap.get(oldFunctionAreaUser.getId()))) {
							if(htmlIndex>right.size()) {
								right.add(right.size(), oldFunctionAreaUser);
							}else {
								right.add(htmlIndex-1, oldFunctionAreaUser);
							}
							oldFunctionAreaUser.setLayoutType(FunctionAreaUser.LAYOUT_TYPE_RIGHT);
							
						}
	                } else if ( Objects.equals(functionAreaUser.getLayoutType(), FunctionAreaUser.LAYOUT_TYPE_LEFT) ) {
	                	//先移除再添加
						if(right.remove(rightMap.get(oldFunctionAreaUser.getId()))) {
							if(htmlIndex>left.size()) {
								left.add(left.size(), oldFunctionAreaUser);
							}else {
								left.add(htmlIndex-1, oldFunctionAreaUser);
							}
							oldFunctionAreaUser.setLayoutType(FunctionAreaUser.LAYOUT_TYPE_LEFT);
						}
	                }
	            }
           
				//重新排序
				for (int i = 0; i < right.size(); i++) {
					right.get(i).setDisplayOrder(i+1);
				}
				int rightNum = right.size(); 
				for (int i = 0; i < left.size(); i++) {
					left.get(i).setDisplayOrder(rightNum+i+1);
				}
				oneSideFunctionAreaUsers1.addAll(right);
				oneSideFunctionAreaUsers1.addAll(left);
            }
			functionAreaUserService.saveAll(EntityUtils.toArray(oneSideFunctionAreaUsers1,FunctionAreaUser.class));

			RedisUtils.delBeginWith(FunctionAreaUserService.CACHE_KEY);
			//保存常用操作
            redirectAttributes.addAttribute("jsonString",jsonString);
            if (functionArea.getDataUrl().contains("showCommonUseApp2")) {
                return "redirect:/desktop/recommendApp/order/add";
            }
            if (functionArea.getDataUrl().contains("/desktop/app/showCommonUseApp")) {
				return "redirect:/desktop/userApp/model/add";
			}
            msg = success("设置成功");
		} catch (Exception e) {
			LOG.error("",e);
			e.printStackTrace();
            msg = error("设置失败请重试！");
		} finally {
            try {
                FileUtils.writeStringToFile(new File(jsonPath),msg,"UTF-8");
            } catch (Exception e1){
            }
        }
        return "/desktop/homepage/template/functionAreaUserSet.json";
	}

	@ResponseBody
	@RequestMapping(value = "/delete/functionAreaUserSet")
	@ControllerInfo("删除功能区")
	public String doDeleteFunctionAreaUser(@RequestParam("functionAreaUserId") String functionAreaUserId){

		try {
			Assert.notNull(functionAreaUserId,"id不能为空");

			functionAreaUserService.updateState2SoftDeleteById(functionAreaUserId,getLoginInfo().getUserId(),getLoginInfo().getUnitClass());
			RedisUtils.delBeginWith(FunctionAreaUserService.CACHE_KEY);
			return success("删除成功，您可以重新添加！");
		} catch (Exception e){
			LOG.error("删除功能区失败 functionAreaUserId {} Exception Info {}" , functionAreaUserId, ExceptionUtils.getStackTrace(e));
			return error("删除失败");
		}
	}
    //保存功能区
	private void  saveFunctionAreaUser(FunctionAreaUser functionAreaUser,String functionId){
		UserSet userSet = userSetService.findByUserId(getLoginInfo().getUserId());

		FunctionArea functionArea = functionAreaService.findOne(functionId);
		
		int order = functionAreaUserService.getMaxDisplayOrder(getUserId());
		if(functionAreaUser != null ){
			if ( Objects.equals(functionAreaUser.getState(),FunctionAreaUser.STATE_ILLEGAL )) {
				if (userSet == null ||  UserSet.LAYOUT_TWO2ONE.equals(userSet.getLayout()) || functionAreaUser.getColumns() == null || functionAreaUser.getColumns() < 6) {
					functionAreaUser.setColumns(6);
				}
				functionAreaUserService.updateDisplayOrderAndState(++order,functionAreaUser.getId());
				//functionAreaUserService.updateState(functionAreaUser.getId());
			}else{				
				functionAreaUserService.updateFunctionAreaId(functionId,functionAreaUser.getId());
			}
		}else{
			FunctionAreaUser fAreaUser = new FunctionAreaUser();
			fAreaUser.setId(UuidUtils.generateUuid());
			fAreaUser.setCustomerId(getLoginInfo().getUserId());
			//fAreaUser.setCustomerType(FunctionAreaUser.LAYOUT_TYPE_RIGHT);
			if(DeskTopConstant.CHILDREN_HOME_WORK.equals(functionArea.getType()) 
					|| DeskTopConstant.XIN_GAO_KAO_FLOW.equals(functionArea.getType()) || DeskTopConstant.OPENOPI_COUNT_TYPE.equals(functionArea.getType())){
				fAreaUser.setLayoutType(FunctionAreaUser.LAYOUT_TYPE_LEFT);
			}else{
				fAreaUser.setLayoutType(FunctionAreaUser.LAYOUT_TYPE_RIGHT);
			}
			fAreaUser.setDisplayOrder(++order);
			fAreaUser.setFunctionAreaId(functionId);
			Integer col = functionArea != null ? functionArea.getColumns() : 0; 
			if (userSet == null || UserSet.LAYOUT_TWO2ONE.equals(userSet.getLayout()) ) {
				if ( col == null ) {
					fAreaUser.setColumns(6);
				} else {
					fAreaUser.setColumns(col);
				} 
			} else {
				fAreaUser.setColumns((col == null || col < 6)? 6 : col);
			}
			fAreaUser.setState(FunctionAreaUser.STATE_NORMAL);
			functionAreaUserService.save(fAreaUser);
		}
//		RedisUtils.delBeginWith(FunctionAreaUserService.CACHE_KEY);
//		return success("添加成功");
	}

    

}
