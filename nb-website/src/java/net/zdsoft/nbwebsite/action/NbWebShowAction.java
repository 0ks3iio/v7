/*
 * @Project: v7
 * @Author shenke
 * @(#)NbSiteDateBaseAction.java  Created on 2016/10/27
 * @Copyright (c) 2016 LOVESoft Inc. All rights reserved
 * @Date 2016/10/27 create by intellij idea
 */
package net.zdsoft.nbwebsite.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.nbwebsite.constant.WebsiteConstants;
import net.zdsoft.nbwebsite.constant.WebsiteConstants.Type;
import net.zdsoft.nbwebsite.entity.WebArticle;
import net.zdsoft.nbwebsite.service.CommonService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;


/**
 * @author shenke
 * @version Revision: 1.0 ,
 * @description: 宁波教研室网站前台action
 * @date: 2016/10/27 16:02
 */
@Controller
@RequestMapping(value = {"/sitedata",""})
public class NbWebShowAction {

    @Autowired
    private CommonService commonService;
    @Autowired
    private McodeRemoteService mcodeDetailService;
    @Autowired
    private UnitRemoteService unitService;
    @Autowired
    private RegionRemoteService regionService;
    @Autowired
    private SchoolRemoteService schoolService;

    @RequestMapping("/index.html")
    public String index(ModelMap map){
        try {
            List<McodeDetail> models = SUtils.dt(mcodeDetailService.findByMcodeIds(WebsiteConstants.TYPE_MCODE_ID), new TR<List<McodeDetail>>(){});
            List<McodeDetail> rm = Lists.newArrayList();
            for (McodeDetail model : models) {
                if(ArrayUtils.contains(ArrayUtils.toArray(Type.FRENDLY_LINKS.getThisId(),Type.PICTURE_FLIP.getThisId(),Type.REGIONAL_PICTURE.getThisId()),model.getThisId())){
                    rm.add(model);
                }
            }
            models.removeAll(rm);
            map.put("models", models);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "/nbsitedata/index.ftl";
    }

    @RequestMapping("/friendLink.html")
    public String doFriendLink(ModelMap map){
        try {
            List<WebArticle> friendLinks =  commonService.getArticleByType(Type.FRENDLY_LINKS.getType(),null,false);
            map.put("friendLinks",friendLinks);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "/nbsitedata/friendLink.ftl";
    }

    @RequestMapping("/model.html")
    public String doSwitchModel(String thisId,ModelMap map,String parentId,String navigator,HttpServletRequest request){
        String pageName = StringUtils.EMPTY;
        //首页 -1
        if (StringUtils.equals(thisId,"-1")){
            pageName = "indexDetail.ftl";
            map.put("modelName","首页");

             //图文翻动数据
            List<WebArticle> articles = commonService.getArticleByType(Type.PICTURE_FLIP.getType(),null,false);
            map.put("articles",articles);
            //区域直通图片数据
            List<WebArticle> pics = commonService.getArticleByType(Type.REGIONAL_PICTURE.getType(),null,false);
            map.put("pics",pics);

            //设置首页数据
            setIndexData(map);
        }
        else{
            pageName = "detail.ftl";
            if(StringUtils.isEmpty(parentId)) {
                //此处应获取部署的单位Id（应该是市级，或者省级）
                parentId = "";
            }
            //设置正常页数据
            setCommonDetail(map);
            McodeDetail mcodeDetail = SUtils.dc(mcodeDetailService.findByMcodeAndThisId(WebsiteConstants.TYPE_MCODE_ID,thisId), McodeDetail.class);
            map.put("modelName", mcodeDetail.getMcodeContent());
            map.put("type",thisId);
            String regionCode = request.getParameter("regionCode");
            if(StringUtils.isNotEmpty(regionCode)){
                map.put("regionCode",regionCode);
                Region region = SUtils.dc(regionService.findByFullCode(regionCode), Region.class);
                map.put("regionName",region!=null?region.getRegionName():StringUtils.EMPTY);
                //navigator = mcodeDetail.getMcodeContent()+region!=null?" > "+region.getRegionName():StringUtils.EMPTY;
            }

            //unitService.findDirectUnitsByParentId(parentId, Unit.UNIT_CLASS_EDU);
        }
        map.put("navigator",navigator);
        return "/nbsitedata/"+pageName;
    }

    //填充首页数据
    private void setIndexData(ModelMap map){
        Pagination page = new Pagination(6,false);
        McodeDetail mcodeDetail = SUtils.dc(mcodeDetailService.findByMcodeAndThisId(WebsiteConstants.TYPE_MCODE_ID,Type.MEDIA_FOCUS.getThisId()), McodeDetail.class);

        List<WebArticle> medias = commonService.getArticleByType(Type.MEDIA_FOCUS.getThisId(),page,false);
        page.setPageSize(7);
        List<WebArticle> bulletins = commonService.getArticleByType(Type.BULLETIN.getThisId(),page,false);
        page.setPageSize(8);
        List<WebArticle> experiences = commonService.getArticleByType(Type.SCHOOL_EXPERIENCE.getThisId(),page,false);
        page.setPageSize(6);
        List<WebArticle> shows = commonService.getArticleByType(Type.RESULT_SHOW.getThisId(),page,false);
        page.setPageSize(3);
        List<WebArticle> projects = commonService.getArticleByType(Type.EAREA_PROJECT.getThisId(),page,false);
        page.setPageSize(7);
        List<WebArticle> dynamics = commonService.getArticleByType(Type.DYNAMIC_EAREA.getThisId(),page,false);
        
        List<WebArticle> testIntroductions = commonService.getArticleByType(Type.TEST_MAINTAIN.getThisId(),page,false);
        if(CollectionUtils.isNotEmpty(testIntroductions)){
        	WebArticle tindu=testIntroductions.get(0);
        	map.put("tindu", tindu);
        }else{
        	map.put("tindu", new WebArticle());
        }
        
        map.put("medias",medias);
        map.put("bulletins",bulletins);
        map.put("experiences",experiences);
        map.put("shows",shows);
        map.put("projects",projects);
        map.put("dynamics",dynamics);
        
        
    }
    //填充普通页面数据
    private void setCommonDetail(ModelMap map){

        String regionCode = "3300";//WebsiteConstants.REGION_CODE;

        Unit topUnit = SUtils.dc(unitService.findTopUnit(), Unit.class);
        if(topUnit !=null && StringUtils.isNotBlank(topUnit.getRegionCode())){
            Region region = SUtils.dc(regionService.findByFullCode(topUnit.getRegionCode()), Region.class);
            if(region!=null)
            regionCode = region.getRegionCode();
        }

        List<Region> regions = SUtils.dt(regionService.findSubRegionByFullCode(regionCode), new TR<List<Region>>(){});
        Map<Region,List<School>> regionListMap = Maps.newHashMap();
        Map<String,List<School>> unitMap = Maps.newHashMap();
        Map<String,Region> codes = EntityUtils.getMap(regions,"fullCode",StringUtils.EMPTY);
        for (String code : codes.keySet()) {
            List<School> schs = SUtils.dt(schoolService.findByRegionCodes(ArrayUtils.toArray(code)), new TR<List<School>>(){});
            regionListMap.put(codes.get(code),schs);
            unitMap.put(code,schs);
        }
        map.put("regionListMap",regionListMap);
        map.put("unitMap",unitMap);
        map.put("regions",regions);
    }


    @RequestMapping("/unit")
    public String loadUnit(){

        return null;
    }
    //从数据库中查找出文章列表
    @RequestMapping("/webarticle/list.html")
    public String articleList(String type,ModelMap map,String schId,String regionCode){
    	//开始 2017-1-19
//    	try {
//            List<McodeDetail> models = SUtils.dt(mcodeDetailService.findByMcodeIds(WebsiteConstants.TYPE_MCODE_ID), new TR<List<McodeDetail>>(){});
//            List<McodeDetail> rm = Lists.newArrayList();
//            for (McodeDetail model : models) {
//                if(ArrayUtils.contains(ArrayUtils.toArray(Type.FRENDLY_LINKS.getThisId(),Type.PICTURE_FLIP.getThisId(),Type.REGIONAL_PICTURE.getThisId()),model.getThisId())){
//                    rm.add(model);
//                }
//            }
//            models.removeAll(rm);
//            map.put("models", models);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
    	//结束
        List<WebArticle> articles = Lists.newArrayList();
        if(StringUtils.isEmpty(schId) && StringUtils.isEmpty(regionCode)) {
            articles = commonService.getArticleByType(type,null,false);
        }
        else if(StringUtils.isNotEmpty(regionCode) && StringUtils.isEmpty(schId)){
            List<School> schs = SUtils.dt(schoolService.findByRegionCodes(ArrayUtils.toArray(regionCode)), new TR<List<School>>(){});
            articles = commonService.getArticleByUnitIdsAndType(EntityUtils.getList(schs,"id").toArray(new String[0]),type,null,false);
        }
        else if(StringUtils.isNotEmpty(schId)){
            articles = commonService.getArticleByUnitIdAndType(schId, type, null,false);
        }
        map.put("articles",articles);
        return "/nbsitedata/detailArticleList.ftl";
    }
    //点击文章，进入文章页面
    @RequestMapping("/webarticle/detail.html")
    public String articleDetail(String id, String modelName, ModelMap map, String thisId, HttpServletRequest req){
        try {
            WebArticle article = commonService.findOne(id);
            List<McodeDetail> models = SUtils.dt(mcodeDetailService.findByMcodeIds(WebsiteConstants.TYPE_MCODE_ID), new TR<List<McodeDetail>>(){});
            School sch = SUtils.dc(schoolService.findById(article.getCommitUnitId()), School.class);
            Region region = null;
            if(sch != null) {
                region = SUtils.dc(regionService.findByFullCode(sch.getRegionCode()), Region.class);
            }
            map.put("models", models);
            map.put("article",article==null?new WebArticle():article);

            map.put("regionCode",req.getParameter("regionCode"));
            StringBuilder navigator = new StringBuilder();
            if(StringUtils.isNotEmpty(modelName)){
                navigator.append(modelName).append(" > ");
            }else{
                navigator.append(Type.valueOfThisid(thisId).getContent()).append(" > ");
            }
            navigator.append(region!=null?region.getRegionName()+" > ":StringUtils.EMPTY)
                    .append(sch!=null?sch.getSchoolName()+" > ":StringUtils.EMPTY)
                    .append("新闻详情");
            map.put("navigator",navigator.toString());
            commonService.addClickNumber(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "/nbsitedata/articleDetail.ftl";
    }

    @RequestMapping("/titleimage.html")
    @ResponseBody
    public String getImage(String titleImageUrl, HttpServletResponse response){
        try {
            ExportUtils.outputFile(titleImageUrl,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void checkThisId(String thisId){

    }
    //开始 更新文章类型 2017-1-19
    @RequestMapping("/updateArticleType.html")
    @ResponseBody
    public String updateArticle(String id, String type, ModelMap map, HttpServletRequest req){
    	WebArticle webArticle = commonService.findOne(id);
		if(webArticle==null){
			return "该文章已被其他人删除";
		}
		webArticle.setType(type);
		
		
		commonService.saveAllEntitys(webArticle);
    	
    	
    	
    	
    	
        return "修改成功";
    }
    
    
}
