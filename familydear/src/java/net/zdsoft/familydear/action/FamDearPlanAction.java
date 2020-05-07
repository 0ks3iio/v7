package net.zdsoft.familydear.action;

import net.zdsoft.activity.entity.FamilyDearPermission;
import net.zdsoft.activity.service.FamilyDearPermissionService;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.familydear.common.FamDearConstant;
import net.zdsoft.familydear.entity.FamDearActivity;
import net.zdsoft.familydear.entity.FamDearPlan;
import net.zdsoft.familydear.service.FamDearActivityService;
import net.zdsoft.familydear.service.FamDearPlanService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.familydear.action
 * @ClassName: FamDearPlanAction
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/6 11:06
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/6 11:06
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Controller
@RequestMapping("/familydear/plan")
public class FamDearPlanAction extends BaseAction {
    @Autowired
    SemesterRemoteService semesterRemoteService;
    @Autowired
    FamDearPlanService famDearPlanService;
    @Autowired
    FamDearActivityService famDearActivityService;
    @Autowired
    UserRemoteService userRemoteService;
    @Autowired
    FamilyDearPermissionService familyDearPermissionService;
    private List<String> acadyearList;
    private int currentPageIndex,currentPageSize;
    @RequestMapping("/eduFamDearPlanIndex")
    public String eduFamDearPlanIndex(ModelMap map){
        getAcadyearList(map);
        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(map,"年份不存在");
        }
        List<FamilyDearPermission> familyDearPermissions = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(),getLoginInfo().getUserId(),"2");
        map.put("hasPermission",false);
        if(CollectionUtils.isNotEmpty(familyDearPermissions)){
            map.put("hasPermission",true);
        }
        Pagination page=createPagination();
        currentPageIndex = page.getPageIndex();
        currentPageSize = page.getPageSize();
        map.put("currentPageIndex",currentPageIndex);
        map.put("currentPageSize",currentPageSize);
        return "familydear/famDearPlan/FamDearPlanIndex.ftl";
    }

    @RequestMapping("/famDearPlanList")
    public String famDearPlanList(String year, String title, ModelMap map, HttpServletRequest request){
        Pagination page=createPagination();
        currentPageIndex = page.getPageIndex();
        currentPageSize = page.getPageSize();
        List<FamDearPlan> list = new ArrayList<>();
        List<FamilyDearPermission> familyDearPermissions = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(),getLoginInfo().getUserId(),"2");
        map.put("hasPermission",false);
        if(CollectionUtils.isNotEmpty(familyDearPermissions)){
            map.put("hasPermission",true);
            list = famDearPlanService.findListByYearAndTitleByPage(year,title,null,page);
        }else {
            list = famDearPlanService.findListByYearAndTitleByPage(year,title,FamDearConstant.PLAN_PUBLISH,page);
        }

//        if(org.apache.commons.lang.StringUtils.isBlank(title)){
//            list = famDearPlanService.findListBy(new String[]{"year"}, new String[]{year});
//        }else {
//            list = famDearPlanService.findListBy(new String[]{"year", "title"}, new String[]{year, title});
//        }

        List<User> userList = userRemoteService.findListObjectBy(new String[]{"unitId"}, new String[]{getLoginInfo().getUnitId()});
        Map<String,User> userMap = userList.stream().collect(Collectors.toMap(User::getId, Function.identity()));
        for(FamDearPlan famDearPlan:list){
            User user = userMap.get(famDearPlan.getCreateUserId());
            if(user!=null) {
                famDearPlan.setCreateUserName(user.getRealName());
            }
            famDearPlan.setCreateTimeStr(DateUtils.date2String(famDearPlan.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        }
        List<FamDearPlan> famDearPlans = new ArrayList<>(list);
        famDearPlans.sort((o1, o2) -> {
            return -(o1.getCreateTime().compareTo(o2.getCreateTime()));
        });
        map.put("planList",famDearPlans);
        map.put("currentPageIndex",currentPageIndex);
        map.put("currentPageSize",currentPageSize);
        sendPagination(request, map, page);
        return "familydear/famDearPlan/FamDearPlanList.ftl";
    }


    @RequestMapping("/edu/add")
    public String eduFamDearPlanAdd(ModelMap map,String year){
        getAcadyearList(map);
        Calendar cale = Calendar.getInstance();
        int nowYear = cale.get(Calendar.YEAR);
        map.put("nowYear", nowYear+"");
        String applyUserName = getLoginInfo().getRealName();
        map.put("applyUserName",applyUserName);
        map.put("year",year);
        return "familydear/famDearPlan/FamDearPlanAdd.ftl";
    }


    @RequestMapping("/edu/savePlan")
    @ControllerInfo(value = "保存计划")
    @ResponseBody
    public String eduFamDearPlanSave(FamDearPlan famDearPlan){
        if(org.apache.commons.lang.StringUtils.isNotBlank(famDearPlan.getId())){
            try {
                famDearPlan.setCreateTime(new Date());
                famDearPlan.setCreateUserId(getLoginInfo().getUserId());
                famDearPlan.setUnitId(getLoginInfo().getUnitId());
                if(StringUtils.isBlank(famDearPlan.getState())){
                        famDearPlan.setState(FamDearConstant.PLAN_UNPUBLISH);
                }
                famDearPlanService.save(famDearPlan);
                return returnSuccess();
            } catch (Exception e) {
                e.printStackTrace();
                return returnError();
            }

        }else {
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            famDearPlan.setId(uuid);
            famDearPlan.getTitle();
            List<FamDearPlan> famDearPlans = famDearPlanService.findListBy(new String[]{"year", "title"}, new String[]{famDearPlan.getYear(), famDearPlan.getTitle()});
            if(CollectionUtils.isNotEmpty(famDearPlans)){
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("该年已存在相同标题的计划！"));
            }
            famDearPlan.setCreateTime(new Date());
            famDearPlan.setCreateUserId(getLoginInfo().getUserId());
            famDearPlan.setState(FamDearConstant.PLAN_UNPUBLISH);
            famDearPlan.setUnitId(getLoginInfo().getUnitId());

            try {
                famDearPlanService.save(famDearPlan);
                return returnSuccess();
            } catch (Exception e) {
                e.printStackTrace();
                return returnError();
            }
        }
    }
    @RequestMapping("/edu/planSaveState")
    @ControllerInfo(value = "更改计划状态")
    @ResponseBody
    public String eduFamDearPlanSaveState(String id,String state,String year,String title,ModelMap map){
        FamDearPlan famDearPlan = famDearPlanService.findOne(id);
        if(state.equals(FamDearConstant.PLAN_UNPUBLISH)){
            famDearPlan.setState(FamDearConstant.PLAN_PUBLISH);
        }else {
            famDearPlan.setState(FamDearConstant.PLAN_UNPUBLISH);
        }
        try {
            famDearPlanService.update(famDearPlan,id,new String[]{"state"});
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();

    }

    @RequestMapping("/edu/planEdit")
    @ControllerInfo(value = "编辑计划")
    public String eduFamDearPlanEdit(String id,String type,ModelMap map){
        Pagination page=createPagination();
        currentPageIndex = page.getPageIndex();
        currentPageSize = page.getPageSize();
        getAcadyearList(map);
        FamDearPlan famDearPlan = famDearPlanService.findOne(id);
        List<User> userList = userRemoteService.findListObjectBy(new String[]{"unitId"}, new String[]{getLoginInfo().getUnitId()});
        Map<String,User> userMap = userList.stream().collect(Collectors.toMap(User::getId, Function.identity()));
        User user = userMap.get(famDearPlan.getCreateUserId());
        if(user!=null){
            map.put("applyUserName",user.getRealName());
        }else {
            map.put("applyUserName","");
        }
        Calendar cale = Calendar.getInstance();
        int nowYear = cale.get(Calendar.YEAR);
        map.put("nowYear", nowYear+"");
        map.put("currentPageIndex",currentPageIndex);
        map.put("currentPageSize",currentPageSize);
        map.put("type",type);
        map.put("famDearPlan",famDearPlan);
        return "familydear/famDearPlan/FamDearPlanEdit.ftl";
    }
    @RequestMapping("/edu/planDel")
    @ControllerInfo(value = "删除")
    @ResponseBody
    public String eduFamDearPlanDel(String id,String year,String title,ModelMap map){
        try {
            FamDearPlan famDearPlan = famDearPlanService.findOne(id);
            if(famDearPlan.getState().equals(FamDearConstant.PLAN_PUBLISH)){
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("请先取消发布再进行删除！"));
            }
            List<FamDearActivity> list = famDearActivityService.findListBy(new String[]{"planId"},new String[] {id});
            if(CollectionUtils.isNotEmpty(list)){
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("已经有结亲活动安排，请先去删除相关活动！"));
            }
            famDearPlanService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @RequestMapping("/schFamDearPlanIndex")
    public String schFamDearPlanIndex(ModelMap map){
        getAcadyearList(map);
        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(map,"年份不存在");
        }
        return "familydear/famDearPlan/FamDearPlanIndex.ftl";
    }

    public void getAcadyearList(ModelMap map){
        acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        List<String> yearList = new ArrayList<>();
        for(String s:acadyearList){
            s= s.substring(5,9);
            yearList.add(s);
        }
        Calendar cale = Calendar.getInstance();
        int nowYear = cale.get(Calendar.YEAR);
        map.put("nowYear", nowYear+"");
        map.put("acadyearList", yearList);
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }

    public int getCurrentPageSize() {
        return currentPageSize;
    }

    public void setCurrentPageSize(int currentPageSize) {
        this.currentPageSize = currentPageSize;
    }


}
