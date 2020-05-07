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

import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;


/**
 * @author yangsj 2017-2-14下午5:52:58
 */
@Controller
@RequestMapping("/sitedate")
public class NbWebTestModifyAction extends BaseAction {


	    @Autowired
	    private CustomRoleUserRemoteService customRoleUserRemoteService;
		
		@Autowired
		private McodeRemoteService mcodeRemoteService;
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
		
		@RequestMapping("/modify/index/page")
	    public String index(ModelMap map,@RequestParam(required = false) String container){

	        map.put("type","12");
	        map.put("manager",isManager());
	        map.put("container",container);
	        return "/nbsitedata/webbackground/modify/index.ftl";
	    }
		@RequestMapping("/modify/{type}/list/page")
		public String articleList(final @PathVariable("type") String type, String unitId,
				final String title,final String startDate,final String endDate,ModelMap map,
				String commitState,String page_index,String container) {
//			String pageName = "commonList.ftl";
			checkType(type);
			unitId = checkUnitId(unitId);
			Pagination page = createPagination();

			String createUserId = getLoginInfo().getUserId();
			if(!isManager()){
				createUserId = StringUtils.EMPTY;
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

			
			return "/nbsitedata/webbackground/modify/commonList.ftl";
		}

		@RequestMapping("/modify/{type}/addoredit/page")
		public String addOrEdit(@PathVariable("type") String type, String unitId,
				String articleId, ModelMap map,String container) {
			String pageName = "commonEdit.ftl";
			checkType(type);
			unitId = checkUnitId(unitId);
			WebArticle webArticle = null;
			List<McodeDetail> models = SUtils.dt(mcodeRemoteService.findByMcodeIds(WebsiteConstants.TYPE_MCODE_ID), new TR<List<McodeDetail>>(){});
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
			return "/nbsitedata/webbackground/modify/commonEdit.ftl";
		}

		@ResponseBody
		@RequestMapping("/modify/{type}/saveorupdate")
		public String saveOrUpdate(WebArticle webArticle, @PathVariable("type") String type,String lnoOpinion,String ltype,String lpushType, HttpServletRequest request) {
			//断定某一个实际的运行值和预期想一样，否则就抛出异常
			if(lnoOpinion.equalsIgnoreCase("undefined")||lnoOpinion==null){
				lnoOpinion="";
			}
			if(ltype.equalsIgnoreCase("undefined")||ltype==null){
				ltype="";
			}
			if(lpushType.equalsIgnoreCase("undefined")||lpushType==null){
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
	                    webArticle.setAuditUnitId(getLoginInfo().getUnitId());
	                    webArticle.setAuditUserId(getLoginInfo().getUserId());
	                    webArticle.setReleaseTime(getNow());
						if(StringUtils.equals(type,Type.FRENDLY_LINKS.getType())|| StringUtils.equals(type,Type.REGIONAL_PICTURE.getType())||StringUtils.equals(type,Type.PICTURE_FLIP.getType()) || isManager()){
							webArticle.setId(UuidUtils.generateUuid());
							webArticle.setType("12");
							webArticle.setPushType(lpushType);
							webArticle.setNoOpinion(lnoOpinion);
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
				//update
				else{
					oldWebArticle = commonService.findOne(webArticle.getId());					
					webArticle.setCommitTime(oldWebArticle.getCommitTime());
					webArticle.setCreationTime(oldWebArticle.getCreationTime());
					
					//保存
					if(StringUtils.equals(webArticle.getCommitState(), WebsiteConstants.STATE_PASSED)){
						webArticle.setCommitTime(getNow());
						webArticle.setCommitUnitId(getUnitId());
						webArticle.setCommitUserId(getLoginInfo().getUserId());
						webArticle.setType("12");
						webArticle.setPushType(lpushType);
						webArticle.setNoOpinion(lnoOpinion);
//						webArticle.setAuditOpinion(StringUtils.EMPTY);
						msg.append("保存");
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
				article = (WebArticle) commonService.saveAllEntitys(webArticle).get(0);

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
						return  SUtils.dt(mcodeRemoteService.findByMcodeIds(WebsiteConstants.TYPE_MCODE_ID), new TR<List<McodeDetail>>(){});
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
		 * 
		 * @return
		 */
		
	    protected boolean isManager(){
	        boolean isManager = customRoleUserRemoteService.containRole(getLoginInfo().getUserId(),WebsiteConstants.MANAGER_ROLE_CODE);
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

	