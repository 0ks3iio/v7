/*
 * Project: v7
 * Author : shenke
 * @(#) CommonAction.java Created on 2016-10-9
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.nbwebsite.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;

import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.CustomRoleRemoteService;
import net.zdsoft.basedata.remote.service.CustomRoleUserRemoteService;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.nbwebsite.constant.WebsiteConstants;
import net.zdsoft.nbwebsite.constant.WebsiteConstants.Type;
import net.zdsoft.nbwebsite.dto.WebArticleDto;
import net.zdsoft.nbwebsite.entity.WebArticle;
import net.zdsoft.nbwebsite.service.CommonService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

/**
 * @description: 宁波教研室后台
 * @author: shenke
 * @version: 1.0
 * @date: 2016-10-9下午6:56:31
 */
@Controller
@RequestMapping("/sitedata")
public class NbWebBackgroundAction extends BaseAction {

	protected static final Logger LOG = LoggerFactory.getLogger(NbWebBackgroundAction.class);
	
	//BIZ(业务) redis
	@Autowired
	private CustomRoleUserRemoteService customRoleUserService;
	@Autowired
	private CustomRoleRemoteService customRoleService;
	@Autowired
	private McodeRemoteService mcodeDetailService;
	@Autowired
	private CommonService commonService;
    @Autowired
	private UserRemoteService userService;
    @Autowired
    private RegionRemoteService regionService;
    @Autowired
    private UnitRemoteService unitService;
    //表单中的日期 字符串和Javabean中的日期类型的属性转换,进行配置
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@RequestMapping("/{type}/index/page")
	public String execute(@PathVariable("type") String type, String unitId,
			ModelMap map,String container) {
		String pageName = "commonIndex.ftl";
		checkType(type);
		map.put("manager", isManager());
		map.put("type", type);
		map.put("container",container);
		if(StringUtils.equals(type,Type.FRENDLY_LINKS.getType())){
			pageName = "friendlink/friendLinkIndex.ftl";
		}
		if(StringUtils.equals(type,Type.REGIONAL_PICTURE.getType())){
			List<WebArticle> webArticles = commonService.getCommonList(unitId, type, null, null, null, null,isManager(),null,null,false,true);
			pageName = "regionpicture/pictureIndex.ftl";
			map.put("isInsert",CollectionUtils.size(webArticles)<5);
		}
		if(StringUtils.equals(type,Type.PICTURE_FLIP.getType())){
			List<WebArticle> articles = commonService.getCommonList(unitId,type,null,null,null,null,isManager(),null,null,false,true);
			map.put("isInsert",CollectionUtils.size(articles)<6);
		}
		return "/nbsitedata/webbackground/"+pageName;
	}

	@RequestMapping("/{type}/list/page")
	public String articleList(final @PathVariable("type") String type, String unitId,
			final String title,final String startDate,final String endDate,ModelMap map,
			String commitState,String page_index,String container) {
		String pageName = "commonList.ftl";
		checkType(type);
		unitId = checkUnitId(unitId);
		Pagination page = createPagination();

		String createUserId = getLoginInfo().getUserId();
		if(isManager()){
			unitId = StringUtils.EMPTY;
		}


		if(StringUtils.equals(type,Type.FRENDLY_LINKS.getType())){
			unitId = null;
			createUserId = null;
		}
		if(StringUtils.equals(type,Type.REGIONAL_PICTURE.getType())){
			//pageName = "regionpicture/pictureIndex.ftl";
			unitId = null;
			createUserId = null;
		}
		if(StringUtils.equals(type,Type.PICTURE_FLIP.getType())){
			unitId = null;
			createUserId = null;
		}
		List<WebArticle> webArticles = commonService.getCommonList(unitId, type, title, getDate(startDate), getDate(endDate), commitState,isManager(),createUserId,page,false,true);
		
		//wrapper  Lists.newArrayList()与new ArrayList()等价
		List<WebArticleDto> dtos = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(webArticles)){
			List<String> commitUserIds = EntityUtils.getList(webArticles, "commitUserId");
			List<User> commitUsers = SUtils.dt(userService.findByIds(commitUserIds.toArray(new String[0])), new TR<List<User>>(){});
			Map<String,User> commitUserMap = EntityUtils.getMap(commitUsers, "id", StringUtils.EMPTY);
			
			User tempUser = null;
			for(WebArticle webArticle : webArticles){
				WebArticleDto dto = new WebArticleDto();
				dto.setWebArticle(webArticle);
				tempUser = commitUserMap.get(webArticle.getCommitUserId());
				dto.setCommitUserName(tempUser!=null?tempUser.getRealName():StringUtils.EMPTY);
				dtos.add(dto);
			}
		}
		
		map.put("pagination", page);
		map.put("webArticles", dtos);
		map.put("manager",isManager());
		map.put("commitState", commitState);
		//分页辅助字段
		map.put("container", container);
		map.put("page_index",page_index);

		if(StringUtils.equals(type,Type.FRENDLY_LINKS.getType())){
			pageName = "friendlink/friendLinkList.ftl";
		}

		if(StringUtils.equals(type,Type.REGIONAL_PICTURE.getType())){
			pageName = "regionpicture/pictureList.ftl";
		}

		return "/nbsitedata/webbackground/"+pageName;
	}

	@RequestMapping("/{type}/addoredit/page")
	public String addOrEdit(@PathVariable("type") String type, String unitId,
			String articleId, ModelMap map,String container) {
		String pageName = "commonEdit.ftl";

		checkType(type);
		unitId = checkUnitId(unitId);
		WebArticle webArticle = null;
		List<McodeDetail> models = SUtils.dt(mcodeDetailService.findByMcodeIds(WebsiteConstants.TYPE_MCODE_ID), new TR<List<McodeDetail>>(){});
        List<McodeDetail> rm = Lists.newArrayList();
        for (McodeDetail model : models) {
            if(ArrayUtils.contains(ArrayUtils.toArray(Type.FRENDLY_LINKS.getThisId(),Type.PICTURE_FLIP.getThisId(),Type.REGIONAL_PICTURE.getThisId()),model.getThisId())){
                rm.add(model);
            }
        }
        models.removeAll(rm);
		try {
			if (StringUtils.isEmpty( articleId)) {
				webArticle = new WebArticle();
			} else {
				webArticle = commonService.findOne(articleId);				
			}
			map.put("models", models);
			map.put("webArticle", webArticle);
			map.put("type", type);
			map.put("manager", isManager());
			map.put("container",container);
			map.put("userId",getLoginInfo().getUserId());

			if(StringUtils.equals(type,Type.FRENDLY_LINKS.getType())){
				pageName = "friendlink/friendLinkEdit.ftl";
			}
			if(StringUtils.equals(type,Type.REGIONAL_PICTURE.getType())){
				pageName = "regionpicture/pictureEdit.ftl";
                String regionCode = WebsiteConstants.REGION_CODE;

                Unit topUnit = SUtils.dc(unitService.findTopUnit(), Unit.class);
                if(topUnit !=null && StringUtils.isNotBlank(topUnit.getRegionCode())){
                    Region region = SUtils.dc(regionService.findByFullCode(topUnit.getRegionCode()), Region.class);
                    if(region!=null)
                        regionCode = region.getRegionCode();
                }

                List<Region> regions = SUtils.dt(regionService.findSubRegionByFullCode(regionCode), new TR<List<Region>>());
				map.put("regions",regions);
				String link = webArticle!=null?webArticle.getTitleLink():StringUtils.EMPTY;
				map.put("regionCode",StringUtils.substring(link,link.length()-6));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/nbsitedata/webbackground/"+pageName;
	}

	@ResponseBody
	@RequestMapping("/{type}/saveorupdate")
	public String saveOrUpdate(WebArticle webArticle ,String lnoOpinion,String ltype,String lpushType, @PathVariable("type") String type, HttpServletRequest request) {
		//断定某一个实际的运行值和预期想一样，否则就抛出异常
//		if(lnoOpinion == null){
//			lnoOpinion="";
//		}
		if(lnoOpinion == null||lnoOpinion.equalsIgnoreCase("undefined")){
			lnoOpinion="";
		}
		if(ltype == null||ltype.equalsIgnoreCase("undefined")){
			ltype="";
		}
		if(StringUtils.isNotBlank(ltype)){
			type=ltype;
		}
		if(lpushType == null||lpushType.equalsIgnoreCase("undefined")){
			lpushType="";
		}
		
		Assert.notNull(webArticle);
		checkType(type);
		WebArticle article = null;
		StringBuilder msg = new StringBuilder();
		try {
			MultipartFile mFile = StorageFileUtils.getFile(request);
			String fileName = StringUtils.EMPTY;
			if(mFile!=null){
				//返回原来的文件名在客户端的文件系统
				fileName = mFile.getOriginalFilename();
			}

			WebArticle oldWebArticle = null;
			//save
			if(StringUtils.isEmpty(webArticle.getId())){
				//保存為草稿
				if(WebsiteConstants.STATE_UNCOMMIT.equals(webArticle.getCommitState())){
					webArticle.setId(UuidUtils.generateUuid());
					
					webArticle.setCreationTime(getNow());
					webArticle.setCommitUnitId(getUnitId());
					webArticle.setCreateUserId(getLoginInfo().getUserId());
					webArticle.setType(type);
					
					msg.append("草稿保存");
				}
				//提交
				else if(WebsiteConstants.STATE_COMMITED.equals(webArticle.getCommitState())){
					webArticle.setId(UuidUtils.generateUuid());
					
					webArticle.setCreationTime(getNow());
					webArticle.setCommitTime(getNow());
					webArticle.setCommitUnitId(getUnitId());
					webArticle.setCreateUserId(getLoginInfo().getUserId());
					webArticle.setCommitUserId(getLoginInfo().getUserId());
					webArticle.setType(type);

					webArticle.setAuditOpinion(StringUtils.EMPTY);
					msg.append("提交");
				}
				else if(WebsiteConstants.STATE_PASSED.equals(webArticle.getCommitState())){
                    webArticle.setAuditUnitId(getLoginInfo().getUnitId());
                    webArticle.setAuditUserId(getLoginInfo().getUserId());
                    webArticle.setReleaseTime(getNow());
                  
					if(StringUtils.equals(type,Type.FRENDLY_LINKS.getType())|| StringUtils.equals(type,Type.REGIONAL_PICTURE.getType())||StringUtils.equals(type,Type.PICTURE_FLIP.getType()) || isManager()){
						webArticle.setId(UuidUtils.generateUuid());
                        
						webArticle.setCreationTime(getNow());
						webArticle.setCommitTime(getNow());
						webArticle.setCommitUnitId(getUnitId());
						webArticle.setCreateUserId(getLoginInfo().getUserId());
						webArticle.setCommitUserId(getLoginInfo().getUserId());
						webArticle.setType(type);
						msg.append("保存");
					}else {
						throw new IllegalArgumentException("您不能直接通过该文章");
					}
				}
				
			}
			//update
			else{

				oldWebArticle = commonService.findOne(webArticle.getId());
				
				webArticle.setCommitTime(oldWebArticle.getCommitTime());
				webArticle.setCreationTime(oldWebArticle.getCreationTime());
				
				//提交
				if(StringUtils.equals(webArticle.getCommitState(), WebsiteConstants.STATE_COMMITED)){
					webArticle.setCommitTime(getNow());
					webArticle.setCommitUnitId(getUnitId());
					webArticle.setCommitUserId(getLoginInfo().getUserId());
					
					if(StringUtils.equals(oldWebArticle.getCommitState(),WebsiteConstants.STATE_RETURN)){
						webArticle.setCommitState(WebsiteConstants.STATE_COMMITED);
					}
					//提交后清空上一次的意见
					webArticle.setAuditOpinion(StringUtils.EMPTY);
					msg.append("提交");
				}
				else if(StringUtils.equals(webArticle.getCommitState(), WebsiteConstants.STATE_UNCOMMIT)){
					msg.append("更新");
				}
				//2017-1-20  判断不通过的提交
//				else if( difference.equalsIgnoreCase("true") &&StringUtils.equals(webArticle.getCommitState(), WebsiteConstants.STATE_UNPASS)){
//					webArticle.setCommitTime(getNow());
//					webArticle.setCommitUnitId(getUnitId());
//					webArticle.setCommitUserId(getLoginInfo().getUserId());
//					webArticle.setAuditOpinion(list);
//					
//					msg.append("不通过的提交");
//					/*if(){
//						
//					}*/
//					
//					
//				}
				
				//审核相关
				else{
					if(StringUtils.equals(webArticle.getCommitState(), WebsiteConstants.STATE_RETURN)){
						msg.append("退回");
					}
					else if(StringUtils.equals(webArticle.getCommitState(), WebsiteConstants.STATE_PASSED)){
						if(StringUtils.equals(type,Type.PICTURE_FLIP.getType())){
							msg.append("保存");
						}else {
							msg.append("通过操作");
						}
						webArticle.setType(type);
						webArticle.setPushType(lpushType);
						webArticle.setCommitTime(getNow());
						if(StringUtils.isBlank(webArticle.getCommitUserId())||StringUtils.isBlank(webArticle.getCommitUnitId())){
							webArticle.setCommitUnitId(getUnitId());
							webArticle.setCommitUserId(getLoginInfo().getUserId());
						}
						webArticle.setAuditUserId(getLoginInfo().getUserId());
						webArticle.setAuditUnitId(getLoginInfo().getUnitId());
						webArticle.setReleaseTime(getNow());
					}
					else if(StringUtils.equals(webArticle.getCommitState(), WebsiteConstants.STATE_UNPASS)){
						webArticle.setNoOpinion(lnoOpinion);						
						msg.append("不通过操作");
					}
					webArticle.setAuditUnitId(getUnitId());
					webArticle.setAuditUserId(getLoginInfo().getUserId());
				}
			}
			if(mFile != null && StringUtils.isNotBlank(fileName)){
				if(StringUtils.isEmpty(webArticle.getTitleImageName())){
					//删除
					webArticle.setTitleImageName(StringUtils.EMPTY);
					webArticle.setTitleImageUrl(StringUtils.EMPTY);
				}
				else if(oldWebArticle == null || !StringUtils.equals(oldWebArticle.getTitleImageName(),webArticle.getTitleImageName())){
					//保存新文件
					String path = saveFile(mFile);
					//删除之前上传的文件
					if(oldWebArticle != null && StringUtils.isNotEmpty(oldWebArticle.getTitleImageUrl())) {
						FileUtils.forceDeleteOnExit(new File(oldWebArticle.getTitleImageUrl()));
					}
					//设置新的文件相关属性
					webArticle.setTitleImageUrl(path);
					webArticle.setTitleImageName(fileName);

				}
			}
			if(StringUtils.equals(type,Type.REGIONAL_PICTURE.getType())){
				String regionCode = request.getParameter("regionCode");
				Region region = SUtils.dc(regionService.findByFullCode(regionCode), Region.class);
				webArticle.setTitle(region!=null?region.getRegionName():StringUtils.EMPTY);
				webArticle.setTitleLink("?regionCode="+regionCode);

				webArticle.setCommitUserId(getLoginInfo().getUserId());

				List<WebArticle> os = commonService.getArticleByType(Type.REGIONAL_PICTURE.getType(),null,true);
				if(CollectionUtils.size(os)>=5 && StringUtils.isEmpty(webArticle.getId())){
					return error("您最多保存五条数据！");
				}
			}
			if(StringUtils.equals(type,Type.PICTURE_FLIP.getType())){
				webArticle.setCommitState(WebsiteConstants.STATE_PASSED);
			}
			article = commonService.saveAllEntitys(webArticle).get(0);
			//save File
			msg.append("成功!"); 
		} catch (Exception e) {
			e.printStackTrace();
			msg.append("失败!"+e.getMessage());
			return error(msg.toString());
		}
		return new ResultDto()
				.setSuccess(true)
				.setCode("00")
				.setMsg(msg.toString())
				.setBusinessValue(article!=null?article.getId():StringUtils.EMPTY)
				.toJSONString();
	}

	/**
	 * 通过不通过操作
	 */
	@ResponseBody
	@RequestMapping("/{type}/commitoruncommit")
	public String commit(@PathVariable("type") String type,String ids,String commitState) {
		checkType(type);
		Assert.notNull(ids);
		try {
			JSONArray array = JSON.parseArray(ids);
			String[] idss = array.toArray(new String[0]);
			if(ArrayUtils.isEmpty(idss)){
				
			}else{
				int successNum = 0;
				int errorNum = 0;
				int passed = 0;
				int unpassed =  0;
				int returned = 0;
				
				List<WebArticle> waitUpdates = Lists.newArrayList();
				List<WebArticle> webArticles = commonService.findByIdIn(idss);
				Map<String,WebArticle> webArMap = EntityUtils.getMap(webArticles, "id", StringUtils.EMPTY);
				for (String id : idss) {
					WebArticle webArticle = webArMap.get(id);
					if(webArticle == null){
						errorNum ++;
					}
					else{
                        webArticle.setAuditUnitId(getLoginInfo().getUnitId());
                        webArticle.setAuditUserId(getLoginInfo().getUserId());
                        webArticle.setReleaseTime(getNow());
                        if(WebsiteConstants.STATE_PASSED.equals(webArticle.getCommitState())){
                            passed ++;

						}
						else if(WebsiteConstants.STATE_UNPASS.equals(webArticle.getCommitState())){
							unpassed ++;
						}else if(WebsiteConstants.STATE_RETURN.equals(webArticle.getCommitState())){
							returned ++;
						}
						else{
							if(WebsiteConstants.STATE_COMMITED.equals(commitState)){
								webArticle.setCommitTime(getNow());
								webArticle.setCommitUnitId(getUnitId());
								webArticle.setCommitUserId(getLoginInfo().getUserId());
							}
							webArticle.setCommitState(commitState);
							successNum++;
							LOG.info("["+webArticle.getId()+"设置状态为"+commitState+"]");
						}
						waitUpdates.add(webArticle);
					}
					
				}
				
				commonService.saveAllEntitys(waitUpdates.toArray(new WebArticle[0]));
				return success(new StringBuilder()
				.append("成功操作"+successNum+"条数据\n")
				.append(passed>0?passed+"条已被其他审核人员通过\n":StringUtils.EMPTY)
				.append(unpassed>0?unpassed+"条被其他审核人员设置未通过\n":StringUtils.EMPTY)
				.append(returned>0?returned+"条被其他审核人员退回\n":StringUtils.EMPTY)
				.append(errorNum>0?errorNum+"条已不存在（请联系管理员检查日志）":StringUtils.EMPTY).toString()
				);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("审核失败",e);
			return error("审核失败!"+e.getMessage());
		}
		return error("审核失败!");
	}

	@ResponseBody
	@RequestMapping("/{type}/delete")
	public String delete(String ids, @PathVariable("type") String type) {
		checkType(type);
		StringBuilder msg = new StringBuilder();
		try {
			if(StringUtils.isNotEmpty(ids)){
				JSONArray array = JSON.parseArray(ids);
				String[] idss = array.toArray(new String[0]);
				if(ArrayUtils.isNotEmpty(idss)) {
					List<WebArticle> articles = commonService.findByIdIn(idss);
					for (WebArticle article : articles) {
						article.setIsDeleted("1");
						article.setAuditUserId(getLoginInfo().getUserId());
					}
					commonService.saveAllEntitys(articles.toArray(new WebArticle[0]));

					//commonService.deleteAll(idss);
				}
				else
					return error("id为空无法删除，删除失败！");
			}else{
				msg.append("id为空无法删除，删除失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("",e);
		}
		return success("删除成功！");
	}

	@RequestMapping("/{type}/cancel")
	@ResponseBody
	public String cancel(@PathVariable("type") String type ,String id, String state){
		try{
			if(StringUtils.isBlank(id)){
				return error("id为空删除失败");
			}
				WebArticle webArticle = commonService.findOne(id);
				if(webArticle==null){
					return error("该文章已被其他人删除");
				}
				if("1".equals(state)) {
					webArticle.setCommitState(WebsiteConstants.STATE_CANCEL);
				}else{
					webArticle.setCommitState(WebsiteConstants.STATE_PASSED);
				}
				webArticle.setAuditUserId(getLoginInfo().getUserId());
				commonService.saveAllEntitys(webArticle);
		}catch(Exception e){
		    e.printStackTrace();
		}
		return success(("1".equals(state)?"":"取消")+"撤回成功");
	}


	// ***********************管理员特有**********************/
	/**
	 * 置顶
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/{type}/top")
	public String top(@PathVariable("type") String type, String id,String isTop) {
		checkType(type);
		String msg = (WebsiteConstants.TOP_TRUE.equals(isTop)?"置顶":"取消置顶");
		try {
			if(StringUtils.isNotEmpty(id)){
				WebArticle webArticle = commonService.findOne(id);
				if(webArticle==null){
					return error("该文章已被其他人删除");
				}
				if(WebsiteConstants.TOP_TRUE.equals(isTop)){
					webArticle.setTopTime(getNow());
				}else{
					webArticle.setTopTime(null);
				}
				webArticle.setIsTop(isTop);
				commonService.saveAllEntitys(webArticle);
				return success(msg+"成功！");
			}
			else{
				return error(msg+"失败（id为空）！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(msg+"失败！"+e.getMessage());
		}
	}

	/**
	 * 审核 三种操作，审核通过 审核不通过退回，修改审核通过并发布到前台
	 * 
	 * @return
	 */
	public String verify() {
		return "/nbsitedata";
	}

	/**
	 * 发布
	 * 
	 * @return
	 */
	public String issue() {
		return "/nbsitedata";
	}

	protected String getUnitId() {
		return getLoginInfo().getUnitId();
	}

	protected String checkUnitId(String unitId) {
		return StringUtils.isEmpty(unitId) ? getUnitId() : unitId;
	}

	//public abstract String getType();
	
	
	//防止注入
	protected boolean checkType(String thisId){
		boolean isOk = true;
		//缓存
		List<McodeDetail> mcodeDetails = Lists.newArrayList();
		try {
			mcodeDetails = RedisUtils.getObject("eis7_web_article_type_mcode_id", 10, new TypeReference<List<McodeDetail>>() {}, new RedisInterface<List<McodeDetail>>() {
				@Override
				public List<McodeDetail> queryData() {
					return  SUtils.dt(mcodeDetailService.findByMcodeIds(WebsiteConstants.TYPE_MCODE_ID), new TR<List<McodeDetail>>(){});
				}
			});
			isOk = containThisId(mcodeDetails,thisId);
		} catch (Exception e) {
			if(e instanceof IllegalArgumentException){
				isOk = containThisId(mcodeDetails,thisId);
				if(!isOk){
					throw new IllegalArgumentException(e.getMessage());
				}
			}
		}
		return isOk;
	}
	
	protected Date getNow(){
		return new Date();
	}

	private Date getDate(String dateStr){
		Date date = null;
		try {
			date = DateUtils.parseDate(dateStr, "yyyy-MM-dd");
		} catch (Exception e) {
			LOG.error("",e);
		}
		return date;
	}
	
	private boolean containThisId(List<McodeDetail> mcodeDetails, String thisId){
		boolean isOk = Boolean.FALSE;
		for (McodeDetail mcodeDetail : mcodeDetails) {
			isOk = StringUtils.trimToEmpty(thisId).equals(StringUtils.trimToEmpty(mcodeDetail.getThisId()));
			if(isOk){
				break;
			}
		}
		return isOk;
	}
	
	/**
	 * 是否是审核员
	 * @return
	 */
	protected boolean isManager(){
		boolean isManager = customRoleUserService.containRole(getLoginInfo().getUserId(),WebsiteConstants.MANAGER_ROLE_CODE);
		return isManager;
	}
	private String saveFile(MultipartFile multipartFile){
		String[] ext = new String[]{"png","jpg","jpeg"};
		File tempFile = null;
		try {
			String fileName = multipartFile.getOriginalFilename();
			String savePath = getLoginInfo().getUnitId();
			SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");
			String filePath = "photo" + File.separator + "attached" +File.separator + savePath + File.separator + df1.format(new Date());
			//检查扩展名
			String fileExt1 = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			if(!ArrayUtils.contains(ext,fileExt1)){
				throw new IllegalArgumentException("文件格式错误，请上传正确的图片！");
			}
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt1;
			//String detailPath = dir.getDir() + File.separator + filePath + File.separator;

			//暫時定死，
			String detailPath = Evn.getString("store_path") + File.separator + filePath + File.separator;
			File saveDirFile = new File(detailPath);
			//如果不存在，那么创建一个
			if (!saveDirFile.exists()) {
                saveDirFile.mkdirs();
            }
			tempFile = new File(detailPath+newFileName);
			FileOutputStream outStream = new FileOutputStream(tempFile);
			//写入数据
			InputStream in = multipartFile.getInputStream();
			byte[] buffer = new byte[1024];
			int t = -1;
			while ((t = in.read(buffer))!=-1){
				outStream.write(buffer);
			}
			outStream.close();
		} catch (IOException e) {
			try {
				throw new Exception(e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return tempFile.getPath();
	}
}
