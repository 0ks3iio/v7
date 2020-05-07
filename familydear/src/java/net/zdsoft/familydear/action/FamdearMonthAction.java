package net.zdsoft.familydear.action;

import com.alibaba.fastjson.JSONArray;
import net.zdsoft.activity.entity.FamilyDearPermission;
import net.zdsoft.activity.service.FamilyDearPermissionService;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.familydear.common.FamDearConstant;
import net.zdsoft.familydear.entity.*;
import net.zdsoft.familydear.service.FamDearActivityService;
import net.zdsoft.familydear.service.FamDearArrangeService;
import net.zdsoft.familydear.service.FamDearAttachmentService;
import net.zdsoft.familydear.service.FamdearMonthService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

import static net.zdsoft.familydear.common.FamDearConstant.PLAN_PUBLISH;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.familydear.action
 * @ClassName: FamdearMonthAction
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/24 13:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/24 13:58
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Controller
@RequestMapping("/familydear/famdearMonth")
public class FamdearMonthAction extends BaseAction {
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private FamDearActivityService famDearActivityService;
    @Autowired
    private FamDearArrangeService famDearArrangeService;
    @Autowired
    private FamdearMonthService famdearMonthService;
    @Autowired
    private DeptRemoteService deptRemoteService;
    @Autowired
    FamilyDearPermissionService familyDearPermissionService;
    @Autowired
    UserRemoteService userRemoteService;
    @Autowired
    TeacherRemoteService teacherRemoteService;
    @Autowired
    FamDearAttachmentService famDearAttachmentService;
    private int currentPageIndex,currentPageSize;
    @RequestMapping("/famdearMonthTab")
    public String famdearMonthTab(ModelMap modelMap){
        return "/familydear/famDearMonth/famDearMonthTab.ftl";
    }

    @RequestMapping("/famdearMonthIndex")
    public String famdearMonthIndex(ModelMap map){
        List<FamilyDearPermission> familyDearPermissions = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(),getLoginInfo().getUserId(),"6");
        List<FamDearArrange> list = famDearArrangeService.findListBy(new String[]{"leaderUserId"},new String[]{getLoginInfo().getUserId()});
        map.put("hasPermission",false);
        if(CollectionUtils.isNotEmpty(familyDearPermissions)){
            map.put("hasPermission",true);
        }
        map.put("hasLeadPermission",false);
        if(CollectionUtils.isNotEmpty(list)) {
            map.put("hasLeadPermission",true);
        }

        return "/familydear/famDearMonth/famDearMonthIndex.ftl";
    }
    @RequestMapping("/famdearMonthList")
    public String famdearMonthList(String startTime, String endTime, String type, ModelMap map, HttpServletRequest request){
        Pagination page=createPagination();
        currentPageIndex = page.getPageIndex();
        currentPageSize = page.getPageSize();
        Date startDate = DateUtils.string2Date(startTime,"yyyy-MM-dd");
        Date endDate = DateUtils.string2Date(endTime,"yyyy-MM-dd");
        List<FamdearMonth> famdearMonthList = new ArrayList<>();
        map.put("hasPermission",false);
        List<FamilyDearPermission> familyDearPermissions = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(),getLoginInfo().getUserId(),"6");
        if(CollectionUtils.isNotEmpty(familyDearPermissions)){
            famdearMonthList = famdearMonthService.findListByTimePage(startDate,endDate,null,null,type,page);
//            List<FamdearMonth> list = famdearMonthService.findListByTimePage(startDate,endDate,null,FamDearConstant.PLAN_PUBLISH,type,null);
//            for(int i = 0;i<list.size();i++){
//                if(list.get(i).getCreateUserId().equals(getLoginInfo().getUserId())){
//                    list.remove(list.get(i));
//                }
//            }
//            List<FamdearMonth> list1 = famdearMonthService.findListByTimePage(startDate,endDate,getLoginInfo().getUserId(),null,type,null);
//            if(CollectionUtils.isNotEmpty(list1)){
//                list.addAll(list1);
//            }
//            if(CollectionUtils.isNotEmpty(list)){
//                famdearMonthList = famdearMonthService.findListByIdsPage(list.stream().map(FamdearMonth::getId).collect(Collectors.toSet()).toArray(new String[0]), page);
//            }
            map.put("hasPermission",true);
        }else {
            List<FamdearMonth> list = famdearMonthService.findListByTimePage(startDate,endDate,null,FamDearConstant.PLAN_PUBLISH,type,null);
            for(int i = 0;i<list.size();i++){
                if(list.get(i).getCreateUserId().equals(getLoginInfo().getUserId())){
                    list.remove(list.get(i));
                }
            }
            List<FamdearMonth> list1 = famdearMonthService.findListByTimePage(startDate,endDate,getLoginInfo().getUserId(),null,type,null);
            if(CollectionUtils.isNotEmpty(list1)){
                list.addAll(list1);
            }
            if(CollectionUtils.isNotEmpty(list)){
                famdearMonthList = famdearMonthService.findListByIdsPage(list.stream().map(FamdearMonth::getId).collect(Collectors.toSet()).toArray(new String[0]), page);
            }
//            famdearMonthList = famdearMonthService.findListByTimePage(startDate,endDate,getLoginInfo().getUserId(),null,type,page);
        }
        if(CollectionUtils.isNotEmpty(famdearMonthList)){
            for(FamdearMonth famdearMonth:famdearMonthList){
                String startTimeStr = DateUtils.date2String(famdearMonth.getActivityTime(),"yyyy.MM.dd");
                String endTimeStr = DateUtils.date2String(famdearMonth.getActivityEndTime(),"yyyy.MM.dd");
                famdearMonth.setActivityTimeStr(startTimeStr+"-"+endTimeStr);
                if(famdearMonth.getType().equals("1")){
                    famdearMonth.setTypeStr("访亲轮次活动");
                }else {
                    famdearMonth.setTypeStr("部门每月活动");
                }
            }
        }
        map.put("currentPageIndex",currentPageIndex);
        map.put("currentPageSize",currentPageSize);
        sendPagination(request, map, page);
        map.put("famdearMonthList",famdearMonthList);
        map.put("createUserId",getLoginInfo().getUserId());
        return "/familydear/famDearMonth/famDearMonthList.ftl";
    }

    @RequestMapping("/export")
    public void doExport(String startTime, String endTime, String type, ModelMap map, HttpServletRequest request){
        Pagination page=createPagination();
        currentPageIndex = page.getPageIndex();
        currentPageSize = page.getPageSize();
        Date startDate = DateUtils.string2Date(startTime,"yyyy-MM-dd");
        Date endDate = DateUtils.string2Date(endTime,"yyyy-MM-dd");
        List<FamdearMonth> famdearMonthList = new ArrayList<>();
        List<FamilyDearPermission> familyDearPermissions = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(),getLoginInfo().getUserId(),"6");
        if(CollectionUtils.isNotEmpty(familyDearPermissions)){
            famdearMonthList = famdearMonthService.findListByTimePage(startDate,endDate,null,null,type,null);
        }else {
            List<FamdearMonth> list = famdearMonthService.findListByTimePage(startDate,endDate,null,FamDearConstant.PLAN_PUBLISH,type,null);
            for(int i = 0;i<list.size();i++){
                if(list.get(i).getCreateUserId().equals(getLoginInfo().getUserId())){
                    list.remove(list.get(i));
                }
            }
            List<FamdearMonth> list1 = famdearMonthService.findListByTimePage(startDate,endDate,getLoginInfo().getUserId(),null,type,null);
            if(CollectionUtils.isNotEmpty(list1)){
                list.addAll(list1);
            }
            if(CollectionUtils.isNotEmpty(list)){
                famdearMonthList = famdearMonthService.findListByIdsPage(list.stream().map(FamdearMonth::getId).collect(Collectors.toSet()).toArray(new String[0]), null);
            }
//            famdearMonthList = famdearMonthService.findListByTimePage(startDate,endDate,getLoginInfo().getUserId(),null,type,page);
        }
        String titleName = "";
        if(CollectionUtils.isNotEmpty(famdearMonthList)){
            for(FamdearMonth famdearMonth:famdearMonthList){
                String startTimeStr = DateUtils.date2String(famdearMonth.getActivityTime(),"yyyy.MM.dd");
                String endTimeStr = DateUtils.date2String(famdearMonth.getActivityEndTime(),"yyyy.MM.dd");
                famdearMonth.setActivityTimeStr(startTimeStr+"-"+endTimeStr);
                if(famdearMonth.getType().equals("1")){
                    famdearMonth.setTypeStr("访亲轮次活动");
                    titleName = "访亲轮次活动";
                }else {
                    famdearMonth.setTypeStr("部门每月活动");
                    titleName = "部门每月活动";
                }
            }
        }
        doExportList(famdearMonthList,getResponse(),titleName);
    }

    private void doExportList(List<FamdearMonth> famdearMonths, HttpServletResponse response, String titleName){
        Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
        List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
        if(CollectionUtils.isNotEmpty(famdearMonths)){
            for(FamdearMonth famdearMonth:famdearMonths) {
                Map<String, String> sMap = new HashMap<String, String>();
                sMap.put("活动类型", famdearMonth.getTypeStr());
                sMap.put("时间", famdearMonth.getActivityTimeStr());
                sMap.put("地点", famdearMonth.getPlace());
                sMap.put("参加人数", famdearMonth.getPartnum()+"");
                sMap.put("活动说明", famdearMonth.getActivityContent());
                sMap.put("状态",famdearMonth.getState().equals(FamDearConstant.PLAN_PUBLISH)?"已提交":"未提交");
                recordList.add(sMap);
            }
        }
        sheetName2RecordListMap.put("报名审核",recordList);
        Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
        List<String> tis = getRowTitleList();
        titleMap.put("报名审核", tis);
        ExportUtils exportUtils = ExportUtils.newInstance();
        exportUtils.exportXLSFile( titleName+"每月活动填报信息导出", titleMap, sheetName2RecordListMap, response);
    }

    private List<String> getRowTitleList() {
        List<String> tis = new ArrayList<String>();
        tis.add("活动类型");
        tis.add("时间");
        tis.add("地点");
        tis.add("参加人数");
        tis.add("活动说明");
        tis.add("状态");
        return tis;
    }

    @RequestMapping("/famdearMonthAdd")
    public String famdearMonthAdd(ModelMap map,String type){
        List<FamilyDearPermission> familyDearPermissions = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(),getLoginInfo().getUserId(),"6");
        List<FamDearActivity> activities = new ArrayList<>();
        List<FamDearArrange> list = famDearArrangeService.findListBy(new String[]{"leaderUserId"},new String[]{getLoginInfo().getUserId()});
        map.put("hasLeadPermission",false);
        if(CollectionUtils.isNotEmpty(list)) {
            Set<String> ids = new HashSet<>();
            for (FamDearArrange famDearArrange : list) {
                ids.add(famDearArrange.getActivityId());
            }
            if (CollectionUtils.isNotEmpty(ids)) {
//                Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
//                String nowYear = semester.getAcadyear().substring(5,9);
                activities = famDearActivityService.findListByYearAndTitleByPage(ids.toArray(new String[0]), FamDearConstant.PLAN_PUBLISH, null,null);
            }
            map.put("hasLeadPermission",true);
            map.put("activityList", activities);
        }
        if(CollectionUtils.isNotEmpty(familyDearPermissions)){
            Dept dept = SUtils.dc(deptRemoteService.findOneById(getLoginInfo().getDeptId()),Dept.class);
            if(dept!=null){
                map.put("deptId", dept.getId());
                map.put("deptName", dept.getDeptName());
            }else {
                map.put("deptId", "");
                map.put("deptName", "");
            }

        }
        map.put("id",UuidUtils.generateUuid());
        map.put("type",type);
        map.put("hasPermission",false);
        if(CollectionUtils.isNotEmpty(familyDearPermissions)){
            map.put("hasPermission",true);
        }

//        List<FamDearActivity> famDearActivityList = famDearActivityService.
        return "/familydear/famDearMonth/famDearMonthAdd.ftl";
    }
    @RequestMapping("/famdearChangeActivity")
    @ResponseBody
    public String famdearChangeActivity(String activityId){
        List<FamDearArrange> famDearArranges = famDearArrangeService.findListBy(new String[]{"activityId","leaderUserId"},new String[] {activityId,getLoginInfo().getUserId()});
        JSONArray jsonArr = new JSONArray();
        if(CollectionUtils.isNotEmpty(famDearArranges)) {
//            Set<String> arrangeIds = famDearArranges.stream().map(FamDearArrange::getId).collect(Collectors.toSet());
//            List<FamdearMonth> famdearMonths = famdearMonthService.findListByArrangeIds(arrangeIds.toArray(new String[0]));
//            Set<String> arrangeIds1 = famdearMonths.stream().map(FamdearMonth::getArrangeId).collect(Collectors.toSet());
//            List<FamDearArrange> arranges = famDearArrangeService.findListByIdIn(arrangeIds1.toArray(new String[0]));
//            famDearArranges.removeAll(arranges);
            com.alibaba.fastjson.JSONObject job = null;
            if (CollectionUtils.isNotEmpty(famDearArranges)) {
                for (FamDearArrange item : famDearArranges) {
                    job = new com.alibaba.fastjson.JSONObject();
                    job.put("id", item.getId());
                    job.put("name", item.getBatchType());
                    jsonArr.add(job);
                }
            }
        }
        return jsonArr.toJSONString();
    }
    @RequestMapping("/famdearMonthSave")
    @ResponseBody
    public String famdearMonthSave(FamdearMonth famdearMonth,String type){
        String picIds = getRequest().getParameter("picIds");
        //删除软删数据
        if(StringUtils.isNotBlank(picIds)) {
            famDearAttachmentService.delete(org.apache.commons.lang3.StringUtils.split(picIds, ","));
        }
        FamdearMonth famdearMonth1 = famdearMonthService.findOne(famdearMonth.getId());
        if(famdearMonth1==null) {
            Calendar cale = Calendar.getInstance();
            int nowYear = cale.get(Calendar.YEAR);
            famdearMonth.setYear(nowYear+"");
            if (famdearMonth.getType().equals("1")) {
                famdearMonth.setDeptId("");
            } else {
                famdearMonth.setActivityId("");
                famdearMonth.setArrangeId("");
            }
            famdearMonth.setCreateUserId(getLoginInfo().getUserId());
            famdearMonth.setCreateTime(new Date());
            famdearMonth.setUnitId(getLoginInfo().getUnitId());
            famdearMonth.setState(FamDearConstant.PLAN_UNPUBLISH);
        }else {


        }
        try {
            famdearMonthService.save(famdearMonth);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }
    @RequestMapping("/updateMonthState")
    @ResponseBody
    public String updateMonthState(String id,String state){
        FamdearMonth famdearMonth = famdearMonthService.findOne(id);
        if(state.equals(FamDearConstant.PLAN_UNPUBLISH)){
            famdearMonth.setState(PLAN_PUBLISH);
        }else {
            famdearMonth.setState(FamDearConstant.PLAN_UNPUBLISH);
        }
        try {
            famdearMonthService.update(famdearMonth,id,new String[]{"state"});
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @RequestMapping("/delMonth")
    @ResponseBody
    public String delMonth(String id){
        try {
            FamdearMonth famdearMonth = famdearMonthService.findOne(id);
            if(famdearMonth.getState().equals(FamDearConstant.PLAN_PUBLISH)){
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("请先取消提交再进行删除！"));
            }
            famdearMonthService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }
    @RequestMapping("/famdearMonthEdit")
    public String famdearMonthEdit(String id,String type,ModelMap map){
        List<FamilyDearPermission> familyDearPermissions = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(),getLoginInfo().getUserId(),"6");
        FamdearMonth famdearMonth = famdearMonthService.findOne(id);
        List<FamDearAttachment> famDearAttachmentList = famDearAttachmentService.findListByObjIds(new String[]{famdearMonth.getId()});
        if(CollectionUtils.isNotEmpty(famDearAttachmentList)){
            famdearMonth.setAttachmentList(famDearAttachmentList);
        }
        Pagination page=createPagination();
        currentPageIndex = page.getPageIndex();
        currentPageSize = page.getPageSize();
        map.put("hasPermission",false);
        List<FamDearActivity> activities = new ArrayList<>();
        List<FamDearArrange> list = famDearArrangeService.findListBy(new String[]{"leaderUserId"},new String[]{famdearMonth.getCreateUserId()});
        List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByUnitId(getLoginInfo().getUnitId()), Teacher.class);
        Map<String,String> teacherNameMap = teachers.stream().collect(Collectors.toMap(Teacher::getId,Teacher::getTeacherName));
        map.put("hasLeadPermission",false);
        if(StringUtils.isNotBlank(famdearMonth.getPartUserIds())) {
            String[] ids = famdearMonth.getPartUserIds().split(",");
            List<User> userList = userRemoteService.findListObjectByIds(ids);
            Map<String, String> userTeaIdMap = new HashMap<>();
            for (User user : userList) {
                userTeaIdMap.put(user.getId(), user.getOwnerId());
            }
            StringBuffer buffer = new StringBuffer();
            for (String id1 : ids) {
                String teacherName = teacherNameMap.get(userTeaIdMap.get(id1));
                buffer.append(teacherName + ",");
            }
            famdearMonth.setPartUserNames(buffer.substring(0,buffer.length()-1));
        }
        if(CollectionUtils.isNotEmpty(list)) {
            Set<String> ids = new HashSet<>();
            for (FamDearArrange famDearArrange : list) {
                ids.add(famDearArrange.getActivityId());
            }
            if (CollectionUtils.isNotEmpty(ids)) {
//                Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
//                String nowYear = semester.getAcadyear().substring(5,9);
                activities = famDearActivityService.findListByYearAndTitleByPage(ids.toArray(new String[0]), null,null, null);
            }
            map.put("hasLeadPermission",true);
            map.put("activityList", activities);
        }
        if(CollectionUtils.isNotEmpty(familyDearPermissions)){
            map.put("hasPermission",true);
        }
        map.put("deptId", "");
        map.put("deptName", "");
        if(StringUtils.isNotBlank(famdearMonth.getDeptId())) {
            Dept dept = SUtils.dc(deptRemoteService.findOneById(famdearMonth.getDeptId()), Dept.class);
            if (dept != null) {
                map.put("deptId", dept.getId());
                map.put("deptName", dept.getDeptName());
            }
        }
        if(StringUtils.isNotBlank(famdearMonth.getActivityId())){
            FamDearActivity famDearActivity = famDearActivityService.findOne(famdearMonth.getActivityId());
            if(famDearActivity!=null) {
                famdearMonth.setActivityName(famDearActivity.getTitle());
            }
        }
        if(StringUtils.isNotBlank(famdearMonth.getArrangeId())){
            FamDearArrange famDearArrange = famDearArrangeService.findOne(famdearMonth.getArrangeId());
            if(famDearArrange!=null) {
                famdearMonth.setArrangeMame(famDearArrange.getBatchType());
            }
        }
        map.put("currentPageIndex",currentPageIndex);
        map.put("currentPageSize",currentPageSize);
        map.put("type",type);
        map.put("famdearMonth",famdearMonth);
        return "/familydear/famDearMonth/famDearMonthEdit.ftl";
    }
}
