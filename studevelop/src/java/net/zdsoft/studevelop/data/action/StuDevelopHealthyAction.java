package net.zdsoft.studevelop.data.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateItem;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateOptions;
import net.zdsoft.studevelop.data.service.StudevelopTemplateItemService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateOptionsService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.entity.StudevelopHealthProject;
import net.zdsoft.studevelop.data.service.StuHealthyProjectService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

/**
 * Created by Administrator on 2018/4/13.
 */
@Controller
@RequestMapping("/stuDevelop/healthyHeart")
public class StuDevelopHealthyAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(StuDevelopHealthyAction.class);
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private StuHealthyProjectService stuHealthyProjectService;
    @Autowired
    private StudevelopTemplateItemService studevelopTemplateItemService;
    @Autowired
    private StudevelopTemplateOptionsService studevelopTemplateOptionsService;
    @RequestMapping("/head/index/page")
    public String healthyHead(String code ,ModelMap map){
        //stuDevelop/healtyHeart/head/index/page
        map.put("code",code);
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {});
        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(map,"学年学期不存在");
        }
        map.put("acadyearList", acadyearList);
        Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()), Semester.class);
        if(semesterObj!=null){
            String acadyear=semesterObj.getAcadyear();
            String semester=semesterObj.getSemester()+"";
            map.put("acadyear", acadyear);
            map.put("semester", semester);
        }else{
            map.put("acadyear", "");
            map.put("semester", "");
        }
        //获取学段
        String unitId = getLoginInfo().getUnitId();
        String sections = schoolRemoteService.findSectionsById(unitId);
        String[] sectionArray = sections.split(",");
        Map<String, String> sectionMap = new HashMap<String, String>();
        if(sectionArray.length==0){
            map.put("section", sections);
        }else{
            for(String str : sectionArray){
                if("1".equals(str)){
                    sectionMap.put("1", "小学");
                }else if("2".equals(str)){
                    sectionMap.put("2", "初中");
                }else if("3".equals(str)){
                    sectionMap.put("3", "高中");
                }
            }
            map.put("sectionMap", sectionMap);
        }
        map.put("schoolId",getLoginInfo().getUnitId());
        List<McodeDetail> projectTyps = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-SXXMLX") ,McodeDetail.class);

        map.put("projectTyps", projectTyps);

        logger.error("spring");
        return "/studevelop/template/healthyHeadSetHead.ftl";
    }

    @RequestMapping("/healthyItemLink")
    public String healthItemLink(StudevelopHealthProject healthProject  , ModelMap map){

        if(StringUtils.isNotEmpty(healthProject.getId())){
            healthProject = stuHealthyProjectService.findOne(healthProject.getId());
        }else{
            healthProject.setSchoolId(getLoginInfo().getUnitId());
            healthProject.setIsClosed(StuDevelopConstant.HEALTH_IS_NOT_CLOSED);
        }

        StudevelopTemplateItem studevelopTemplateItem;
        if(StringUtils.isNotEmpty(healthProject.getId())){
            studevelopTemplateItem = studevelopTemplateItemService.findOne(healthProject.getId());
            List<StudevelopTemplateOptions> list =  studevelopTemplateOptionsService.findListBy("templateItemId" ,studevelopTemplateItem.getId());
            map.put("optionsList" ,list);
        }else{
            studevelopTemplateItem =  new StudevelopTemplateItem();
        }

        map.put("studevelopTemplateItem" ,studevelopTemplateItem);
        map.put("healthProject" ,healthProject);
        return "/studevelop/template/healthProItemEdit.ftl";
    }
    @RequestMapping("/healthyItem/save")
    @ResponseBody
    public String healthItemSave(StudevelopHealthProject project , ModelMap map){
        try{
        	List<StudevelopHealthProject>  list = stuHealthyProjectService.getProjectByAcadyearSemesterSection(project);
            if(CollectionUtils.isNotEmpty(list)){
                return error("该名称已存在！");
            }
        	if(StringUtils.isEmpty(project.getId())){
                project.setId(UuidUtils.generateUuid());
                project.setCreationTime(new Date());
            }else{
                project.setModifyTime(new Date());
            }
            stuHealthyProjectService.save(project);
        }catch (Exception e){
            e.printStackTrace();
            log.info("保存失败 ！");
            return error("保存失败！");
        }
        return success("保存成功！");
    }

    @RequestMapping("/checkItemName")
    @ResponseBody
    public String checkItemName(StudevelopHealthProject project  ){

        List<StudevelopHealthProject>  list = stuHealthyProjectService.getProjectByAcadyearSemesterSection(project);
        if(CollectionUtils.isNotEmpty(list)){
            return Json.toJSONString(new ResultDto().setSuccess(false).setCode("00").setMsg("该名称已存在！"));
        }
        return Json.toJSONString(new ResultDto().setSuccess(true).setCode("00"));
    }

    @RequestMapping("/list/page")
    public String healthItemList(StudevelopHealthProject project , ModelMap map , HttpServletRequest request){
//        Pagination page = createPagination();
//        Pageable pageable =Pagination.toPageable(page);
        List<StudevelopHealthProject>  list = stuHealthyProjectService.getProjectByAcadyearSemesterSection(project );

        if(list == null){
            list = new ArrayList<>();
        }

//        page.setMaxRowCount(list.size());
//        sendPagination(request, map, page);
        map.put("itemList",list);
        map.put("project",project);
        return "/studevelop/template/healthyItemList.ftl";
    }
    @RequestMapping("/projectItem/delete")
    @ResponseBody
    public String projectItemDelete(String id ){
        try{
            if(StringUtils.isNotEmpty(id)){

                stuHealthyProjectService.delete(id);
            }
        }catch (Exception e){
            log.error("删除错误");
            return error("删除失败!");
        }

        return success("删除成功！");
    }

    @RequestMapping("/projectItemCopyLink")
    public String projectItemCopyLink(String itemIds  ,ModelMap map){
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {});
        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(map,"学年学期不存在");
        }
        map.put("acadyearList", acadyearList);
        Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()), Semester.class);
        if(semesterObj!=null){
            String acadyear=semesterObj.getAcadyear();
            String semester=semesterObj.getSemester()+"";
            map.put("acadyear", acadyear);
            map.put("semester", semester);
        }else{
            map.put("acadyear", "");
            map.put("semester", "");
        }
        //获取学段
        String unitId = getLoginInfo().getUnitId();
        String sections = schoolRemoteService.findSectionsById(unitId);
        String[] sectionArray = sections.split(",");
        Map<String, String> sectionMap = new HashMap<String, String>();
        if(sectionArray.length==0){
            map.put("section", sections);
        }else{
            for(String str : sectionArray){
                if("1".equals(str)){
                    sectionMap.put("1", "小学");
                }else if("2".equals(str)){
                    sectionMap.put("2", "初中");
                }else if("3".equals(str)){
                    sectionMap.put("3", "高中");
                }
            }
            map.put("sectionMap", sectionMap);
        }
        map.put("schoolId",getLoginInfo().getUnitId());
        map.put("itemIds",itemIds);
        return "/studevelop/template/healthItemCopy.ftl";
    }
    @RequestMapping("/projectItemDoCopy")
    @ResponseBody
    public String doCopy(StudevelopHealthProject project  ,String itemIds){

        try{
            stuHealthyProjectService.copyProject(project,itemIds);
        }catch (Exception e){
            log.error("复制错误");
            return error("复制失败!");
        }

        return success("复制成功！");

    }
    @RequestMapping("/projectItemDoClosed")
    @ResponseBody
    public String doClosed(String id ,String state){
        try{
            StudevelopHealthProject project = stuHealthyProjectService.findOne(id);
            if(project != null){
                project.setIsClosed(state);
            }
            stuHealthyProjectService.save(project);
        }catch (Exception e){
            log.error("操作错误");
            return error("操作失败!");
        }

        return success("操作成功！");

    }


}
