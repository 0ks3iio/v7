package net.zdsoft.nbwebsite.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.CustomRoleUserRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.nbwebsite.constant.WebsiteConstants;
import net.zdsoft.nbwebsite.constant.WebsiteConstants.Type;
import net.zdsoft.nbwebsite.dto.WebArticleDto;
import net.zdsoft.nbwebsite.entity.WebArticle;
import net.zdsoft.nbwebsite.service.CommonService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

/**
 * 工作台
 * @author shenke
 * @since 2017/1/3 17:33
 */
@Controller
@RequestMapping("/sitedata")
public class NbWorkbenchAction extends BaseAction{

    @Autowired
    private CommonService commonService;
    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private CustomRoleUserRemoteService customRoleUserRemoteService;
    
    @Autowired
    private McodeRemoteService mcodeDetailService;

    @RequestMapping("/workbench/index/page")
    public String index(ModelMap map,@RequestParam(required = false) String container){

        map.put("type","1");
        map.put("manager",isManager());
        map.put("container",container);
        return "/nbsitedata/webbackground/workbench/index.ftl";
    }
    
    
    @RequestMapping("/workbench/edit/page")
    public String doEdit(@RequestParam(required = true) String articleId,@RequestParam String container, ModelMap map){       
        WebArticle webArticle = commonService.findOne(articleId);
        //获得文章类型
    	List<McodeDetail> models=getMcodeDetails();
        map.put("models", models);
        map.put("webArticle",webArticle);
        map.put("container",container);
        map.put("manager",isManager());
        map.put("type",webArticle.getType());
        return "/nbsitedata/webbackground/workbench/edit.ftl";
    }

    /**
	 * @return
	 */
	private List<McodeDetail> getMcodeDetails() {
		// TODO Auto-generated method stub
		 List<McodeDetail>	models = SUtils.dt(mcodeDetailService.findByMcodeIds(WebsiteConstants.TYPE_MCODE_ID), new TR<List<McodeDetail>>(){});
		    List<McodeDetail> rm = Lists.newArrayList();
		    for (McodeDetail model : models) {
		        if(ArrayUtils.contains(ArrayUtils.toArray(Type.FRENDLY_LINKS.getThisId(),Type.PICTURE_FLIP.getThisId(),Type.REGIONAL_PICTURE.getThisId()),model.getThisId())){
		            rm.add(model);
		        }
		    }
		     models.removeAll(rm);
		     return models;
		    }



	@RequestMapping("/workbench/list/page")
    public String doLoadList(@RequestParam(required = false) String state, @RequestParam String container ,
                             @RequestParam(required = false) String title, @RequestParam(required = false) Date startDate,
                             @RequestParam(required = false) Date endDate, ModelMap map){
        List<WebArticleDto> dtos = null;
        List<McodeDetail> models=null;
        Pagination page = createPagination();
        //草稿
        if(StringUtils.equals("uncommit",state)){
            dtos = getArticles(WebsiteConstants.STATE_UNCOMMIT,title,startDate,endDate,page);
        }
        //最近
        if(StringUtils.equals("recent",state)){
            dtos = getArticles(null,title,startDate,endDate,page);
        }
        //待审
        if(StringUtils.equals("pending",state)){
            dtos = getArticles(WebsiteConstants.STATE_COMMITED,title,startDate,endDate,page);
          //开始 2017-1-19
        	
                models = SUtils.dt(mcodeDetailService.findByMcodeIds(WebsiteConstants.TYPE_MCODE_ID), new TR<List<McodeDetail>>(){});
                List<McodeDetail> rm = Lists.newArrayList();
                for (McodeDetail model : models) {
                    if(ArrayUtils.contains(ArrayUtils.toArray(Type.FRENDLY_LINKS.getThisId(),Type.PICTURE_FLIP.getThisId(),Type.REGIONAL_PICTURE.getThisId()),model.getThisId())){
                        rm.add(model);
                    }
                }
                models.removeAll(rm);
//                map.put("models", models);
           
        	//结束
        }
        map.put("models", models);
        map.put("dtos",dtos);
        map.put("commitState",WebsiteConstants.STATE_UNCOMMIT);
        map.put("manager",isManager());
        map.put("pagination",page);
        map.put("type","1");
        map.put("container",container);
        return "/nbsitedata/webbackground/workbench/list.ftl";
    }

    private List<WebArticleDto> getArticles(String state, String title,Date startDate,Date endDate, Pagination page){
        String createUserId = WebsiteConstants.STATE_UNCOMMIT.equals(state)?getLoginInfo().getUserId():null;
        String unitId = WebsiteConstants.STATE_COMMITED.equals(state)?null:getLoginInfo().getUnitId();
        unitId = state==null&&getLoginInfo().getUnitClass()==Unit.UNIT_CLASS_EDU?null:unitId;
        List<WebArticle> webArticles = commonService.getCommonList(unitId,null,title,startDate,endDate, state,false,createUserId,page,true,true);

        List<WebArticleDto> dtos = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(webArticles)){
            List<String> commitUserIds = EntityUtils.getList(webArticles, "commitUserId");
            List<User> commitUsers = SUtils.dt(userRemoteService.findByIds(commitUserIds.toArray(new String[0])), new TR<List<User>>(){});
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
        return dtos;
    }
    /**
     * 是否是审核员
     * @return
     */
    protected boolean isManager(){
        boolean isManager = customRoleUserRemoteService.containRole(getLoginInfo().getUserId(),WebsiteConstants.MANAGER_ROLE_CODE);
        return isManager;
    }
    
    @RequestMapping("/workbench/addoredit/page")
	public String addOrEdit( String unitId,
			String articleId, ModelMap map,String container) {
    	WebArticle webArticle = new WebArticle();
    	 //获得文章类型
    	List<McodeDetail> models=getMcodeDetails();
		map.put("addWeb", true);
		map.put("models", models);
		map.put("webArticle", webArticle);
		map.put("manager", isManager());
		map.put("type","2");
		map.put("container",container);
		map.put("userId",getLoginInfo().getUserId());
		return "/nbsitedata/webbackground/workbench/edit.ftl";
	}
    
 
    
}
