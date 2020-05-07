package net.zdsoft.familydear.action;

import com.alibaba.fastjson.JSONArray;
import net.zdsoft.activity.entity.FamilyDearPermission;
import net.zdsoft.activity.entity.FamilyDearRegister;
import net.zdsoft.activity.service.FamilyDearPermissionService;
import net.zdsoft.activity.service.FamilyDearRegisterService;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.familydear.common.FamDearConstant;
import net.zdsoft.familydear.dto.FamActivityDto;
import net.zdsoft.familydear.entity.FamDearActivity;
import net.zdsoft.familydear.entity.FamDearArrange;
import net.zdsoft.familydear.entity.FamDearPlan;
import net.zdsoft.familydear.service.FamDearActivityService;
import net.zdsoft.familydear.service.FamDearArrangeService;
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
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.zdsoft.familydear.common.FamDearConstant.PLAN_PUBLISH;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.familydear.action
 * @ClassName: FamPlanActivityAction
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/8 9:33
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/8 9:33
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Controller
@RequestMapping("/familydear/activity")
public class FamPlanActivityAction extends BaseAction {
    @Autowired
    SemesterRemoteService semesterRemoteService;
    @Autowired
    FamDearPlanService famDearPlanService;
    @Autowired
    FamDearActivityService famDearActivityService;
    @Autowired
    FamDearArrangeService famDearArrangeService;
    @Autowired
    UserRemoteService userRemoteService;
    @Autowired
    TeacherRemoteService teacherRemoteService;
    @Autowired
    FamilyDearRegisterService familyDearRegisterService;
    @Autowired
    FamilyDearPermissionService familyDearPermissionService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    private List<String> acadyearList;
    private int currentPageIndex,currentPageSize;
    @RequestMapping("/eduFamDearActivityIndex")
    public String eduFamDearActivityIndex(ModelMap map){
        getAcadyearList(map);
        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(map,"年份不存在");
        }
        Calendar cale = Calendar.getInstance();
        int nowYear = cale.get(Calendar.YEAR);
        List<FamDearPlan> list = famDearPlanService.findListBy(new String[]{"year","state"},new String[] {nowYear+"","1"});
        map.put("planList",list);
        Pagination page=createPagination();
        currentPageIndex = page.getPageIndex();
        currentPageSize = page.getPageSize();
        List<FamilyDearPermission> familyDearPermissions = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(),getLoginInfo().getUserId(),"3");
        map.put("hasPermission",false);
        if(CollectionUtils.isNotEmpty(familyDearPermissions)){
            map.put("hasPermission",true);
        }
        map.put("currentPageIndex",currentPageIndex);
        map.put("currentPageSize",currentPageSize);
        return "familydear/famDearActivity/FamDearActivityIndex.ftl";
    }

    @RequestMapping("/edufamDearActivityList")
    public String eduFamDearActivityIndex(String year,String planId,String startTime,String endTime,String village,ModelMap map,HttpServletRequest request){
        Pagination page=createPagination();
        currentPageIndex = page.getPageIndex();
        currentPageSize = page.getPageSize();
        Set<String> activityIds = new HashSet<>();
        Set<String> activityIds1 = new HashSet<>();
        Set<String> activityIds2 = new HashSet<>();

//        if(StringUtils.isNotBlank(startTime)){
//
//        }
        Date startDate = DateUtils.string2Date(startTime,"yyyy-MM-dd");
        Date endDate = DateUtils.string2Date(endTime,"yyyy-MM-dd");
        if(StringUtils.isNotBlank(village)||StringUtils.isNotBlank(endTime)||StringUtils.isNotBlank(startTime)) {
            List<FamDearArrange> famDearArrangelist = famDearArrangeService.getFamilyDearArrangeListByParams(startDate,endDate,village);
            if (CollectionUtils.isNotEmpty(famDearArrangelist)) {
                activityIds1 = famDearArrangelist.stream().map(FamDearArrange::getActivityId).collect(Collectors.toSet());
            }
            List<FamDearActivity > famDearActivityList = famDearActivityService.findListBy(new String[]{"year","planId"}, new String[]{year,planId});
            if(CollectionUtils.isNotEmpty(famDearActivityList)){
                activityIds2 = famDearActivityList.stream().map(FamDearActivity::getId).collect(Collectors.toSet());
            }
            if(CollectionUtils.isNotEmpty(activityIds1)) {
                activityIds.addAll(activityIds1);
                activityIds.retainAll(activityIds2);
            }else {
                activityIds.addAll(activityIds1);
            }
        }else {
            List<FamDearActivity > famDearActivityList = famDearActivityService.findListBy(new String[]{"year","planId"}, new String[]{year,planId});
            if(CollectionUtils.isNotEmpty(famDearActivityList)){
                activityIds = famDearActivityList.stream().map(FamDearActivity::getId).collect(Collectors.toSet());
            }
        }

        List<FamActivityDto> famActivityDtos = new ArrayList<>();
        List<FamilyDearPermission> familyDearPermissions = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(),getLoginInfo().getUserId(),"3");
        map.put("hasPermission",false);
        List<FamDearActivity> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(activityIds)) {
            if(CollectionUtils.isNotEmpty(familyDearPermissions)){
                map.put("hasPermission",true);
                list = famDearActivityService.findListByYearAndTitleByPage(activityIds.toArray(new String[0]),null,null, page);
            }else {
                list = famDearActivityService.findListByYearAndTitleByPage(activityIds.toArray(new String[0]),FamDearConstant.PLAN_PUBLISH,null, page);
            }
            Set<String> ids = new HashSet<>();
            for(FamDearActivity famDearActivity:list){
                ids.add(famDearActivity.getId());
            }
//            if(CollectionUtils.isEmpty(familyDearPermissions)){
//                for(FamDearActivity famDearActivity:list){
//                    if(famDearActivity.equals(FamDearConstant.PLAN_PUBLISH)){
//                        ids.add(famDearActivity.getId());
//                    }
//                }
//            }else {
//                for(FamDearActivity famDearActivity:list){
//                    ids.add(famDearActivity.getId());
//                }
//            }

            if (CollectionUtils.isNotEmpty(ids)) {
                List<FamDearArrange> famDearArranges = famDearArrangeService.getFamilyDearArrangeListByUnitId(ids.toArray(new String[0]),getLoginInfo().getUnitId());
                Map<String, List<FamDearArrange>> stringListMap = new HashMap<>();
                for (String id : ids) {
                    List<FamDearArrange> list1 = new ArrayList();
                    for (FamDearArrange famDearArrange : famDearArranges) {
                        if(famDearArrange.getRural().length()>20){
                            famDearArrange.setRuralSub(famDearArrange.getRural().substring(0,20)+"......");
                        }else {
                            famDearArrange.setRuralSub(famDearArrange.getRural());
                        }
                        if (famDearArrange.getActivityId().equals(id)) {
                            list1.add(famDearArrange);
                        }
                    }
                    stringListMap.put(id, list1);
                }
                for (FamDearActivity famDearActivity : list) {
                    FamActivityDto famActivityDto = new FamActivityDto();
                    famActivityDto.setTitle(famDearActivity.getTitle());
                    famActivityDto.setFileContent(famDearActivity.getFileContent());
                    famActivityDto.setActivityType(famDearActivity.getActivityType());
                    famActivityDto.setPlanId(famDearActivity.getPlanId());
                    famActivityDto.setId(famDearActivity.getId());
                    famActivityDto.setState(famDearActivity.getState());
                    if (MapUtils.isNotEmpty(stringListMap)) {
                        if (CollectionUtils.isNotEmpty(stringListMap.get(famDearActivity.getId()))) {
                            famActivityDto.setFamDearArrangeList(stringListMap.get(famDearActivity.getId()));
                        }
                    }
                    famActivityDtos.add(famActivityDto);
                }
            }
        }
        map.put("activityList",famActivityDtos);
        map.put("currentPageIndex",currentPageIndex);
        map.put("currentPageSize",currentPageSize);
        sendPagination(request, map, page);
        return "familydear/famDearActivity/FamDearActivityList.ftl";
    }

    @RequestMapping("/edu/add")
    public String eduFamDearActivityAdd(ModelMap map,String year,String planId){
        getAcadyearList(map);
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        List<FamDearPlan> list = famDearPlanService.findListBy(new String[]{"year","state"},new String[] {year,PLAN_PUBLISH});
        map.put("planId",planId);
        map.put("unitId",getLoginInfo().getUnitId());
        map.put("createUserId",getLoginInfo().getUserId());
        map.put("year",year);
        map.put("planList",list);
        return "familydear/famDearActivity/FamDearActivityAdd.ftl";
    }

    @RequestMapping("/edu/updateActivityState")
    @ResponseBody
    public String updateActivityState(String id,String state){
        FamDearActivity famDearActivity = famDearActivityService.findOne(id);
        List<FamDearArrange> arranges = famDearArrangeService.getFamilyDearArrangeList(new String[]{famDearActivity.getId()});
        if(CollectionUtils.isEmpty(arranges)&&famDearActivity.getState().equals(FamDearConstant.PLAN_UNPUBLISH)){
            return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("未维护批次的活动不能发布！"));
        }
        if(state.equals(FamDearConstant.PLAN_UNPUBLISH)){
            famDearActivity.setState(PLAN_PUBLISH);
        }else {
            famDearActivity.setState(FamDearConstant.PLAN_UNPUBLISH);
        }
        try {
            famDearActivityService.update(famDearActivity,id,new String[]{"state"});
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @RequestMapping("/edu/activityEdit")
    @ControllerInfo(value = "编辑活动")
    public String eduFamDearActivityEdit(String id,String type,ModelMap map){
        getAcadyearList(map);
        Pagination page=createPagination();
        currentPageIndex = page.getPageIndex();
        currentPageSize = page.getPageSize();

        FamDearActivity famDearActivity = famDearActivityService.findOne(id);
        List<FamDearPlan> list = famDearPlanService.findListBy(new String[]{"year","state"},new String[] {famDearActivity.getYear(),PLAN_PUBLISH});
        map.put("planList",list);
        List<FamDearArrange> list1 = famDearArrangeService.getFamilyDearArrangeList(new String[]{id});
        FamActivityDto famActivityDto = new FamActivityDto();
        famActivityDto.setCreateUserId(famDearActivity.getCreateUserId());
        famActivityDto.setUnitId(famDearActivity.getUnitId());
        famActivityDto.setState(famDearActivity.getState());
        famActivityDto.setId(famDearActivity.getId());
        famActivityDto.setTitle(famDearActivity.getTitle());
        famActivityDto.setPlanId(famDearActivity.getPlanId());
        famActivityDto.setFileContent(famDearActivity.getFileContent());
        famActivityDto.setRequire(famDearActivity.getRequire());
        famActivityDto.setYear(famDearActivity.getYear());


        for(FamDearArrange famDearArrange:list1){
            String ruralValues = famDearArrange.getRuralValue();
            if(famDearArrange.getBatchType().contains("一")) {
                famDearArrange.setSortBatch(famDearArrange.getBatchType().replace("一", "1"));
            }else if(famDearArrange.getBatchType().contains("二")) {
                famDearArrange.setSortBatch(famDearArrange.getBatchType().replace("二", "2"));
            }else if(famDearArrange.getBatchType().contains("三")) {
                famDearArrange.setSortBatch(famDearArrange.getBatchType().replace("三", "3"));
            }else if(famDearArrange.getBatchType().contains("四")) {
                famDearArrange.setSortBatch(famDearArrange.getBatchType().replace("四", "4"));
            }else if(famDearArrange.getBatchType().contains("五")) {
                famDearArrange.setSortBatch(famDearArrange.getBatchType().replace("五", "5"));
            }else if(famDearArrange.getBatchType().contains("六")) {
                famDearArrange.setSortBatch(famDearArrange.getBatchType().replace("六", "6"));
            }else if(famDearArrange.getBatchType().contains("七")) {
                famDearArrange.setSortBatch(famDearArrange.getBatchType().replace("七", "7"));
            }else if(famDearArrange.getBatchType().contains("八")) {
                famDearArrange.setSortBatch(famDearArrange.getBatchType().replace("八", "8"));
            }else if(famDearArrange.getBatchType().contains("九")) {
                famDearArrange.setSortBatch(famDearArrange.getBatchType().replace("九", "9"));
            }
            if(StringUtils.isNotBlank(ruralValues)) {
                List<String> list2 = Arrays.asList(ruralValues.split(","));
                if(CollectionUtils.isNotEmpty(list2)){
                    famDearArrange.setSelectedRural(list2);
                }
            }
        }
        list1.sort(new Comparator<FamDearArrange>() {
            @Override
            public int compare(FamDearArrange o1, FamDearArrange o2) {
                return o1.getSortBatch().compareTo(o2.getSortBatch());
            }
        });
        famActivityDto.setFamDearArrangeList(list1);
        String[] mcodes = {"DM-XJJQC"};
        List<McodeDetail> mds = SUtils.dt(mcodeRemoteService.findByMcodeIds(mcodes), new TR<List<McodeDetail>>() {} );
        map.put("mcodeDetails",mds);
        map.put("currentPageIndex",currentPageIndex);
        map.put("currentPageSize",currentPageSize);
        map.put("famActivityDto",famActivityDto);
        //是否为编辑页面
        map.put("type",type);
        return "familydear/famDearActivity/FamDearActivityEdit.ftl";
    }

    @RequestMapping("/edu/arrangeEdit")
    @ControllerInfo(value = "编辑批次")
    public String eduFamDearArrangeEdit(String id,ModelMap map){
        List<FamDearArrange> list = famDearArrangeService.getFamilyDearArrangeList(new String[] {id});
        FamDearActivity famDearActivity = famDearActivityService.findOne(id);
        List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByUnitId(getLoginInfo().getUnitId()), Teacher.class);
        Map<String,String> teacherNameMap = teachers.stream().collect(Collectors.toMap(Teacher::getId,Teacher::getTeacherName));
        List<FamDearArrange> needEditList = new ArrayList<>();
        if(famDearActivity.getState().equals(FamDearConstant.PLAN_PUBLISH)) {
            for (FamDearArrange famDearArrange : list) {
                int ret = famDearArrange.getApplyEndTime().compareTo(new Date());
                if (ret < 0) {
                    if(StringUtils.isNotBlank(famDearArrange.getLeaderUserId())){
                        String[] ids = famDearArrange.getLeaderUserId().split(",");
                        List<User>  userList = userRemoteService.findListObjectByIds(ids);
                        Map<String,String> userTeaIdMap = new HashMap<>();
                        for (User user : userList) {
                            userTeaIdMap.put(user.getId() , user.getOwnerId());

                        }
                        StringBuffer buffer = new StringBuffer();
                        for (String id1 : ids) {
                            String teacherName = teacherNameMap.get(userTeaIdMap.get(id1));
                            buffer.append(teacherName + ",");
                        }
                        famDearArrange.setLeaderUserIds(ids);
                        famDearArrange.setLeaderUserName(buffer.toString().split(","));
                        famDearArrange.setLeaderUserNames(buffer.substring(0,buffer.length()-1));
                    }
                    needEditList.add(famDearArrange);
                }
            }
        }
        map.put("activityId",id);
        map.put("needEditList",needEditList);
        return "familydear/famDearActivity/FamDearArrangeEdit.ftl";
    }

    @RequestMapping("/edu/saveArrangeEdit")
    @ControllerInfo(value = "保存编辑批次")
    @ResponseBody
    public String eduFamDearArrangeEditSave(FamActivityDto famActivityDto){
        List<FamDearArrange> list = famActivityDto.getFamDearArrangeList();
        Map<String,String> famDearArrangeMap = new HashMap<>();
        Map<String,List<FamilyDearRegister>> familyDearRegisterMap = new HashMap<>();
        List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByUnitId(getLoginInfo().getUnitId()), Teacher.class);
        Map<String,String> teacherNameMap = teachers.stream().collect(Collectors.toMap(Teacher::getId,Teacher::getTeacherName));
        if(CollectionUtils.isNotEmpty(list)){
            famDearArrangeMap = list.stream().collect(Collectors.toMap(FamDearArrange::getId, FamDearArrange::getLeaderUserId));
            List<FamDearArrange> famDearArranges = famDearArrangeService.getFamilyDearArrangeList(new String[] {famActivityDto.getId()});
            List<FamilyDearRegister> familyDearRegisters = familyDearRegisterService.findListBy(new String[]{"activityId","status"},new String[]{famActivityDto.getId(),"2"});
            if(CollectionUtils.isNotEmpty(famDearArranges)) {
                for(FamDearArrange famDearArrange:famDearArranges) {
                    List<FamilyDearRegister> list1 = new ArrayList<>();
                    for (FamilyDearRegister familyDearRegister : familyDearRegisters) {
                        if (familyDearRegister.getArrangeId().equals(famDearArrange.getId())){
                            list1.add(familyDearRegister);
                        }
                    }
                    familyDearRegisterMap.put(famDearArrange.getId(),list1);
                }
            }
            if(CollectionUtils.isNotEmpty(famDearArranges)){
                for(FamDearArrange famDearArrange:famDearArranges){
                    List<FamilyDearRegister> familyDearRegisters1 = familyDearRegisterMap.get(famDearArrange.getId());
                    Set<String> teacherUserIds=new HashSet<>();
                    for(FamilyDearRegister familyDearRegister:familyDearRegisters1){
                        teacherUserIds.add(familyDearRegister.getTeaUserId());
                    }
                    if(StringUtils.isNotBlank(famDearArrangeMap.get(famDearArrange.getId()))) {
                        String[] ids = famDearArrangeMap.get(famDearArrange.getId()).split(",");
                        if (ids != null || (ids == null && ids.length != 0)) {
                            Set<String> arrangeIds = new HashSet<>(Arrays.asList(ids));
                            if (CollectionUtils.isNotEmpty(arrangeIds)) {
                                for (String id : arrangeIds) {
                                    if (!teacherUserIds.contains(id)) {
                                        User user = SUtils.dc(userRemoteService.findOneById(id), User.class);
                                        if (user != null) {
                                            String teacherName = teacherNameMap.get(user.getOwnerId());
                                            return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(teacherName + "没有报名" + famDearArrange.getBatchType() + "活动批次"));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    famDearArrange.setLeaderUserId(famDearArrangeMap.get(famDearArrange.getId()));
                }
                try {
                    famDearArrangeService.saveAll(famDearArranges.toArray(new FamDearArrange[]{}));
                } catch (Exception e) {

                    e.printStackTrace();
                    return returnError();
                }
            }
        }
        return returnSuccess();
    }

    @RequestMapping("/edu/activityDel")
    @ControllerInfo(value = "删除")
    @ResponseBody
    public String eduFamDearPlanDel(String id,String year,String title,ModelMap map){
        try {
            List<FamDearArrange> famDearArranges = famDearArrangeService.findListBy("activityId",id);
            if(CollectionUtils.isNotEmpty(famDearArranges)){
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("请先删除该轮次下所有的批次活动！"));
            }
            famDearArrangeService.deleteAll(famDearArranges.toArray(new FamDearArrange[0]));
            famDearActivityService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @RequestMapping("/activityCopy")
    @ControllerInfo(value = "复制")
    public String activityCopy(String year,String planId,ModelMap map){
        getAcadyearList(map);
        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(map,"年份不存在");
        }
        map.put("year",year);
        map.put("planId",planId);
        return "familydear/famDearActivity/famDearActivityCopy.ftl";
    }


    @RequestMapping("/saveActivityCopy")
    @ControllerInfo(value = "复制")
    @ResponseBody
    public String saveActivityCopy(String planId,String activityId,String year){
        try {
            FamDearActivity famDearActivity = famDearActivityService.findOne(activityId);
            List<FamDearActivity> list = new ArrayList<>();
//            String title = famDearActivity.getTitle();
//            title = title+"副本";
            int i = 1;
            do{
                String title = famDearActivity.getTitle();
                title = title+"副本";
                title = title+i;
                list = famDearActivityService.findListBy(new String[]{"title"},new String[]{title});
                i++;
            }while (CollectionUtils.isNotEmpty(list));
            List<FamDearArrange> arranges = famDearArrangeService.getFamilyDearArrangeList(new String[]{famDearActivity.getId()});
            int index = i-1;
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            famDearActivity.setId(uuid);
            famDearActivity.setYear(year);
            famDearActivity.setPlanId(planId);
            famDearActivity.setState(FamDearConstant.PLAN_UNPUBLISH);
            famDearActivity.setCreateTime(new Date());
            famDearActivity.setTitle(famDearActivity.getTitle()+"副本"+index);
            for(FamDearArrange famDearArrange:arranges){
                String id = UUID.randomUUID().toString().replaceAll("-", "");
                famDearArrange.setId(id);
                famDearArrange.setLeaderUserId("");
                famDearArrange.setActivityId(famDearActivity.getId());
            }
            famDearArrangeService.saveAll(arranges.toArray(new FamDearArrange[0]));
            famDearActivityService.save(famDearActivity);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @RequestMapping("/edu/yearChange")
    @ResponseBody
    public String famDearYearChange(String year){
        List<FamDearPlan> list = famDearPlanService.findListBy(new String[]{"year","state"},new String[] {year,PLAN_PUBLISH});
        JSONArray jsonArr=new JSONArray();
        com.alibaba.fastjson.JSONObject job=null;
        if(CollectionUtils.isNotEmpty(list)){
            for (FamDearPlan item : list) {
                job=new com.alibaba.fastjson.JSONObject();
                job.put("id", item.getId());
                job.put("name", item.getTitle());
                jsonArr.add(job);
            }
        }
        return jsonArr.toJSONString();
    }


    @RequestMapping("/edu/planChange")
    @ResponseBody
    public String planChange(String year,String planId){

        List<FamilyDearPermission> familyDearPermissions = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(),getLoginInfo().getUserId(),"3");
        List<FamDearActivity> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(familyDearPermissions)){
            list = famDearActivityService.findListBy(new String[]{"year","planId","state"},new String[]{year,planId,FamDearConstant.PLAN_PUBLISH});
        }else {
            list = famDearActivityService.findListBy(new String[]{"year","planId","createUserId","state"},new String[]{year,planId,getLoginInfo().getUserId(),FamDearConstant.PLAN_PUBLISH});
        }
        JSONArray jsonArr=new JSONArray();
        com.alibaba.fastjson.JSONObject job=null;
        if(CollectionUtils.isNotEmpty(list)){
            for (FamDearActivity item : list) {
                job=new com.alibaba.fastjson.JSONObject();
                job.put("id", item.getId());
                job.put("name", item.getTitle());
                jsonArr.add(job);
            }
        }
        return jsonArr.toJSONString();
    }

    @RequestMapping("/edu/saveActivity")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public String famDearSaveActivity(FamActivityDto famActivityDto,String arrangeIds){
        String activityName = famActivityDto.getTitle();
        if(StringUtils.isNotBlank(famActivityDto.getId())){
            if(famActivityDto.getState().equals(FamDearConstant.PLAN_PUBLISH)){
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("不能修改已发布的计划！"));
            }
        }else {
            List<FamDearActivity> famDearActivityList = famDearActivityService.findListBy(new String []{"planId","title"},new String []{famActivityDto.getPlanId(),activityName});
            if(CollectionUtils.isNotEmpty(famDearActivityList)){
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("该计划下已存在相同标题的活动！"));
            }
        }
        try {
            famDearActivityService.saveFamDearActivityDto(famActivityDto,arrangeIds);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
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
