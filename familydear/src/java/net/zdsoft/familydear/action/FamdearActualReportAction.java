package net.zdsoft.familydear.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.activity.entity.FamilyDearPermission;
import net.zdsoft.activity.entity.FamilyDearRegister;
import net.zdsoft.activity.service.FamilyDearPermissionService;
import net.zdsoft.activity.service.FamilyDearRegisterService;
import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StorageDirRemoteService;
import net.zdsoft.familydear.common.FamDearConstant;
import net.zdsoft.familydear.entity.AttachmentDto;
import net.zdsoft.familydear.entity.FamDearActivity;
import net.zdsoft.familydear.entity.FamDearArrange;
import net.zdsoft.familydear.entity.FamDearAttachment;
import net.zdsoft.familydear.entity.FamdearActualReport;
import net.zdsoft.familydear.service.FamDearActivityService;
import net.zdsoft.familydear.service.FamDearArrangeService;
import net.zdsoft.familydear.service.FamDearAttachmentService;
import net.zdsoft.familydear.service.FamdearActualReportService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.zdsoft.familydear.common.FamDearConstant.PLAN_PUBLISH;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.familydear.action
 * @ClassName: FamdearActualReportAction
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/24 13:55
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/24 13:55
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Controller
@RequestMapping("/familydear/famdearActualReport")
public class FamdearActualReportAction extends BaseAction {

    @Autowired
    SemesterRemoteService semesterRemoteService;
    @Autowired
    FamdearActualReportService famdearActualReportService;
    @Autowired
    FamDearArrangeService famDearArrangeService;
    @Autowired
    FamilyDearPermissionService familyDearPermissionService;
    @Autowired
    FamDearAttachmentService famDearAttachmentService;
    @Autowired
    FamDearActivityService famDearActivityService;
    @Autowired
    StorageDirRemoteService storageDirRemoteService;
    @Autowired
    FamilyDearRegisterService familyDearRegisterService;
    @Autowired
    McodeRemoteService McodeRemoteService;
    private int currentPageIndex,currentPageSize;
    private List<String> acadyearList;

    @RequestMapping("/famdearActualReportTab")
    public String famdearActualReportTab(ModelMap modelMap){

        return "/familydear/famDearActualReport/famDearActualReportTab.ftl";
    }
    @RequestMapping("/famdearActualReportIndex")
    public String famdearActualReportIndex(String activityId,String year,String village,ModelMap map){
        getAcadyearList(map);
        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(map,"年份不存在");
        }
        Pagination page=createPagination();
        currentPageIndex = page.getPageIndex();
        currentPageSize = page.getPageSize();
        if(StringUtils.isNotBlank(activityId)){
            map.put("activityId",activityId);
        }
        if(StringUtils.isNotBlank(year)){
            map.put("year",year);
        }
        if(StringUtils.isNotBlank(village)){
            map.put("village",village);
            List<McodeDetail> mcodeDetails = SUtils.dt(McodeRemoteService.findByMcodeContentLike("DM-XJJQC",village),new TR<List<McodeDetail>>(){});
            map.put("villageVal",mcodeDetails.get(0).getThisId());
        }
        return "/familydear/famDearActualReport/famDearActualReportIndex.ftl";
    }
    @RequestMapping("/activityList")
    @ResponseBody
    public String activityList(String year){
        List<FamilyDearPermission> familyDearPermissions = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(),getLoginInfo().getUserId(),"4");
        List<FamDearActivity> list = new ArrayList<>();
        if(!CollectionUtils.isNotEmpty(familyDearPermissions)) {
            List<FamilyDearRegister> list1 = familyDearRegisterService.findListBy(new String[]{"teaUserId", "status",}, new String[]{getLoginInfo().getUserId(), "2"});
            Set<String> registerIds = new HashSet<>();
            for (FamilyDearRegister familyDearRegister : list1) {
                registerIds.add(familyDearRegister.getActivityId());
            }
            if (CollectionUtils.isNotEmpty(registerIds)) {
                list = famDearActivityService.findListByYearAndState(registerIds.toArray(new String[0]), FamDearConstant.PLAN_PUBLISH, year);
            }
        }else {
            list = famDearActivityService.findListByYearAndState(null,FamDearConstant.PLAN_PUBLISH,year);
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
    @RequestMapping("/famdearActualReportAdd")
    public String famdearActualReportAdd(String year,String activityId,String village,String villageVal,ModelMap map){
//        List<FamilyDearRegister> list = familyDearRegisterService.findListBy(new String[]{"teaUserId","status","activityId"},new String[] {getLoginInfo().getUserId(),"2",activityId});
        List<FamilyDearRegister> list = familyDearRegisterService.findListBy(new String[]{"teaUserId","status"},new String[] {getLoginInfo().getUserId(),"2"});

        Set<String> ids = new HashSet<>();
        Set<String> activityIds = new HashSet<>();
        for(FamilyDearRegister familyDearRegister:list){
            activityIds.add(familyDearRegister.getActivityId());
        }
        List<FamDearActivity> activities = famDearActivityService.findListByIdIn(activityIds.toArray(new String[0]));
        map.put("activityList",activities);
        map.put("id",UuidUtils.generateUuid());
        map.put("year",year);
        map.put("createUserId",getLoginInfo().getUserId());
        map.put("activityId",activityId);
        map.put("village",village);

//        map.put("arrangeList",famDearArrangeList);
        return "/familydear/famDearActualReport/famDearActualReportAdd.ftl";
    }

    @RequestMapping("/arrangeList")
    @ResponseBody
    public String arrangeList(String activityId){
        JSONArray jsonArr = new JSONArray();
        List<FamDearArrange> famDearArrangeList = new ArrayList<>();
        List<FamilyDearRegister> list = familyDearRegisterService.findListBy(new String[]{"teaUserId","status"},new String[] {getLoginInfo().getUserId(),"2"});
        Set<String> ids = new HashSet<>();
        for(FamilyDearRegister familyDearRegister:list){
            if(familyDearRegister.getActivityId().equals(activityId)) {
                ids.add(familyDearRegister.getArrangeId());
            }
        }
        List<FamDearArrange> list1 = famDearArrangeService.findListByIdIn(ids.toArray(new String[0]));
        if(CollectionUtils.isNotEmpty(list1)) {
            for (FamDearArrange famDearArrange : list1) {
                if (famDearArrange.getApplyTime().compareTo(new Date()) < 0) {
                    famDearArrangeList.add(famDearArrange);
                }
            }
            com.alibaba.fastjson.JSONObject job = null;
            if (CollectionUtils.isNotEmpty(famDearArrangeList)) {
                for (FamDearArrange item : famDearArrangeList) {
                    job = new com.alibaba.fastjson.JSONObject();
                    job.put("id", item.getId());
                    job.put("name", item.getBatchType());
                    jsonArr.add(job);
                }
            }
        }
        return jsonArr.toJSONString();
    }

    @RequestMapping("/queryArrangeTime")
    @ResponseBody
    public String queryArrangeTime(String arrangeId){
        FamDearArrange famDearArrange = famDearArrangeService.findOne(arrangeId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("startTime",DateUtils.date2String(famDearArrange.getStartTime(),"yyyy-MM-dd"));
        jsonObject.put("endTime",DateUtils.date2String(famDearArrange.getEndTime(),"yyyy-MM-dd"));
        return jsonObject.toJSONString();
    }


    @RequestMapping("/famdearActualReportSave")
    @ResponseBody
    public String famdearActualReportSave(FamdearActualReport actualReport){
        String picIds = getRequest().getParameter("picIds");
        //删除软删数据
        if(StringUtils.isNotBlank(picIds)) {
            famDearAttachmentService.delete(org.apache.commons.lang3.StringUtils.split(picIds, ","));
        }
        try {
            if(actualReport.getCreateTime()==null){
                actualReport.setCreateTime(new Date());
            }
            if(actualReport.getUnitId()==null){
                actualReport.setUnitId(getLoginInfo().getUnitId());
            }
            if(actualReport.getState()==null){
                actualReport.setState(FamDearConstant.PLAN_UNPUBLISH);
            }
            String[] titles = actualReport.getActivityTitle().split(",");
            for(int i=0;i<titles.length;i++){
                String title = titles[i];
                for(int j=0;j<titles.length;j++){
                    if(title.equals(titles[j])&&i!=j){
                        return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("活动名称不可相同！"));
                    }
                }
            }
            String[] tickets = actualReport.getTicketTitle().split(",");
            for(int i=0;i<tickets.length;i++){
                String ticket = tickets[i];
                for(int j=0;j<tickets.length;j++){
                    if(ticket.equals(tickets[j])&&i!=j){
                        return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("车票说明不可相同！"));
                    }
                }
            }
            List<FamDearAttachment> famDearAttachmentList = famDearAttachmentService.findListByObjIds(new String[]{actualReport.getId()});
            List<FamDearAttachment> list = new ArrayList<>();
            List<String> activityTitles = Arrays.asList(actualReport.getActivityTitle().split(","));
            List<String> ticketTitles = Arrays.asList(actualReport.getTicketTitle().split(","));
            int i = 1;
            int j = 1;
            List<FamDearAttachment> atts = new ArrayList<>();
            for(String title:activityTitles){
                for(FamDearAttachment famDearAttachment:famDearAttachmentList){
                    if(famDearAttachment.getObjecttype().equals("activity_"+i)){
                        atts.add(famDearAttachment);
                    }
                }
                i++;
            }
            for(String ticket:ticketTitles){
                for(FamDearAttachment famDearAttachment:famDearAttachmentList){
                    if(famDearAttachment.getObjecttype().equals("ticket_"+j)){
                        atts.add(famDearAttachment);
                    }
                }
                j++;
            }
            famDearAttachmentList.removeAll(atts);
            famDearAttachmentService.deleteAll(famDearAttachmentList.toArray(new FamDearAttachment[0]));
            famdearActualReportService.save(actualReport);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }


    @RequestMapping("/famdearAttachBack")
    @ResponseBody
    public String famdearAttachBack(){
        try {
            String picIds = getRequest().getParameter("picIds");
            //删除之前恢复软删的数据
            List<FamDearAttachment> famDearAttachments = famDearAttachmentService.findListByIds(org.apache.commons.lang3.StringUtils.split(picIds, ","));
            for(FamDearAttachment famDearAttachment:famDearAttachments){
                famDearAttachment.setIsDelete("0");
            }
            famDearAttachmentService.saveAll(famDearAttachments.toArray(new FamDearAttachment[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @RequestMapping("/famdearCheckAttach")
    @ResponseBody
    public String famdearCheckAttach(FamdearActualReport actualReport){
        String picIds = getRequest().getParameter("picIds");
        //删除之前恢复软删的数据
        List<FamDearAttachment> famDearAttachments = famDearAttachmentService.findListByIds(org.apache.commons.lang3.StringUtils.split(picIds, ","));
        for(FamDearAttachment famDearAttachment:famDearAttachments){
            famDearAttachment.setIsDelete("0");
        }
        famDearAttachmentService.saveAll(famDearAttachments.toArray(new FamDearAttachment[0]));
        try {
            List<FamDearAttachment> famDearAttachmentList = famDearAttachmentService.findListByObjIds(new String[]{actualReport.getId()});
            List<FamDearAttachment> list = new ArrayList<>();
            List<String> activityTitles = Arrays.asList(actualReport.getActivityTitle().split(","));
            List<String> ticketTitles = Arrays.asList(actualReport.getTicketTitle().split(","));
            int i = 1;
            int j = 1;
            List<FamDearAttachment> atts = new ArrayList<>();
            for(String title:activityTitles){
                for(FamDearAttachment famDearAttachment:famDearAttachmentList){
                    if(famDearAttachment.getObjecttype().equals("activity_"+i)){
                        atts.add(famDearAttachment);
                    }
                }
                i++;
            }
            for(String ticket:ticketTitles){
                for(FamDearAttachment famDearAttachment:famDearAttachmentList){
                    if(famDearAttachment.getObjecttype().equals("ticket_"+j)){
                        atts.add(famDearAttachment);
                    }
                }
                j++;
            }
            famDearAttachmentList.removeAll(atts);
            famDearAttachmentService.deleteAll(famDearAttachmentList.toArray(new FamDearAttachment[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @RequestMapping("/famdearActualReportList")
    public String famdearActualReportList(String year,String activityId,String villageName,ModelMap map,HttpServletRequest request){
        Pagination page=createPagination();
        currentPageIndex = page.getPageIndex();
        currentPageSize = page.getPageSize();
        List<FamDearArrange> list = famDearArrangeService.getFamilyDearArrangeListByContryNameAndActivityId(villageName,activityId);
        Set<String> ids = new HashSet<>();
        for(FamDearArrange famDearArrange:list){
            ids.add(famDearArrange.getId());
        }
        FamDearActivity famDearActivity = new FamDearActivity();
        if(StringUtils.isNotBlank(activityId)) {
            famDearActivity = famDearActivityService.findOne(activityId);
        }
        Map<String,FamDearArrange> famDearArrangeMap = list.stream().collect(Collectors.toMap(FamDearArrange::getId,Function.identity()));
        List<FamilyDearPermission> familyDearPermissions = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(),getLoginInfo().getUserId(),"4");
        List<FamdearActualReport> list1 = new ArrayList<>();
        map.put("hasPermission",false);
        if(CollectionUtils.isNotEmpty(familyDearPermissions)) {
            if(CollectionUtils.isNotEmpty(ids)) {
//                List<FamdearActualReport> famdearActualReports = famdearActualReportService.getListByArrangeIds(ids.toArray(new String[0]), null, FamDearConstant.PLAN_PUBLISH, null);
//                for(int i = 0;i<famdearActualReports.size();i++){
//                    if(famdearActualReports.get(i).getCreateUserId().equals(getLoginInfo().getUserId())){
//                        famdearActualReports.remove(famdearActualReports.get(i));
//                    }
//                }
//                List<FamdearActualReport> famdearActualReports1 = famdearActualReportService.getListByArrangeIds(ids.toArray(new String[0]), getLoginInfo().getUserId(), null, null);
//                if(CollectionUtils.isNotEmpty(famdearActualReports1)){
//                    famdearActualReports.addAll(famdearActualReports1);
//                }
//                if(CollectionUtils.isNotEmpty(famdearActualReports)) {
//                    list1 = famdearActualReportService.getListByIdsPage(famdearActualReports.stream().map(FamdearActualReport::getId).collect(Collectors.toSet()).toArray(new String[0]), page);
//                }
                if(CollectionUtils.isNotEmpty(ids)) {
                    list1 = famdearActualReportService.getListByArrangeIds(ids.toArray(new String[0]), null, null, page);
                }
                map.put("hasPermission",true);
            }
        }else {
            if(CollectionUtils.isNotEmpty(ids)) {
                List<FamdearActualReport> famdearActualReports = famdearActualReportService.getListByArrangeIds(ids.toArray(new String[0]), null, FamDearConstant.PLAN_PUBLISH, null);
                for(int i = 0;i<famdearActualReports.size();i++){
                    if(famdearActualReports.get(i).getCreateUserId().equals(getLoginInfo().getUserId())){
                        famdearActualReports.remove(famdearActualReports.get(i));
                    }
                }
                List<FamdearActualReport> famdearActualReports1 = famdearActualReportService.getListByArrangeIds(ids.toArray(new String[0]), getLoginInfo().getUserId(), null, null);
                if(CollectionUtils.isNotEmpty(famdearActualReports1)){
                    famdearActualReports.addAll(famdearActualReports1);
                }
                if(CollectionUtils.isNotEmpty(famdearActualReports)) {
                    list1 = famdearActualReportService.getListByIdsPage(famdearActualReports.stream().map(FamdearActualReport::getId).collect(Collectors.toSet()).toArray(new String[0]), page);
                }
                //list1 = famdearActualReportService.getListByArrangeIds(ids.toArray(new String[0]), null, FamDearConstant.PLAN_PUBLISH, page);
            }
        }
        //为了避免for循环
        List<FamDearAttachment> attachments = famDearAttachmentService.findListBy(new String[]{"unitId"},new String[]{getLoginInfo().getUnitId()});
        Set<String> objIds = attachments.stream().map(FamDearAttachment::getObjId).collect(Collectors.toSet());
        Map<String,List<FamDearAttachment>> listMap = new HashMap<>();
        for(String id:objIds){
            List<FamDearAttachment> list2 = new ArrayList<>();
            for(FamDearAttachment famDearAttachment:attachments){
                if(StringUtils.isNotBlank(famDearAttachment.getObjId())) {
                    if (famDearAttachment.getObjId().equals(id)) {
                        list2.add(famDearAttachment);
                    }
                }
            }
            listMap.put(id,list2);
        }

        if(CollectionUtils.isNotEmpty(list1)) {
            for (FamdearActualReport famdearActualReport : list1) {
                FamDearArrange famDearArrange = famDearArrangeMap.get(famdearActualReport.getArrangeId());
                if(famDearActivity!=null){
                    famdearActualReport.setActivityName(famDearActivity.getTitle());
                }
                if (famDearArrange != null) {
                    if(famDearArrange.getApplyTime()!=null&&famDearArrange.getApplyEndTime()!=null){
                        famdearActualReport.setActivityTimeStr(DateUtils.date2String(famdearActualReport.getArriveTime(), "yyyy.MM.dd")+"-"+DateUtils.date2String(famdearActualReport.getBackTime(), "yyyy.MM.dd"));
                    }
                    famdearActualReport.setVillageName(famDearArrange.getRural());
                }
                if (famdearActualReport.getCreateTime() != null) {
                    famdearActualReport.setCreateTimeStr(DateUtils.date2String(famdearActualReport.getCreateTime(), "yyyy-MM-dd"));
                }
                List<String> activityTitles = Arrays.asList(famdearActualReport.getActivityTitle().split(","));
                int picNum=0;
                List<FamDearAttachment> atts = new ArrayList<>();
                atts = listMap.get(famdearActualReport.getId());
                if(CollectionUtils.isNotEmpty(atts)) {
                    picNum = picNum + atts.size();
                }
                famdearActualReport.setImageNum(picNum);
                famdearActualReport.setStateStr(famdearActualReport.getState().equals(FamDearConstant.PLAN_PUBLISH) ? "已提交" : "未提交");
            }
        }
        map.put("currentPageIndex",currentPageIndex);
        map.put("currentPageSize",currentPageSize);
        map.put("createUserId",getLoginInfo().getUserId());
        sendPagination(request, map, page);
        map.put("FamdearActualReportList",list1);
        return "/familydear/famDearActualReport/famDearActualReportList.ftl";
    }

    @RequestMapping("/export")
    public void doExport(String year,String activityId,String villageName,ModelMap map,HttpServletRequest request){
        List<FamDearArrange> list = famDearArrangeService.getFamilyDearArrangeListByContryNameAndActivityId(villageName,activityId);
        Set<String> ids = new HashSet<>();
        for(FamDearArrange famDearArrange:list){
            ids.add(famDearArrange.getId());
        }
        FamDearActivity famDearActivity = new FamDearActivity();
        if(StringUtils.isNotBlank(activityId)) {
            famDearActivity = famDearActivityService.findOne(activityId);
        }
        Map<String,FamDearArrange> famDearArrangeMap = list.stream().collect(Collectors.toMap(FamDearArrange::getId,Function.identity()));
        List<FamilyDearPermission> familyDearPermissions = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(),getLoginInfo().getUserId(),"4");
        List<FamdearActualReport> list1 = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(familyDearPermissions)) {
            if(CollectionUtils.isNotEmpty(ids)) {
//                List<FamdearActualReport> famdearActualReports = famdearActualReportService.getListByArrangeIds(ids.toArray(new String[0]), null, FamDearConstant.PLAN_PUBLISH, null);
//                for(int i = 0;i<famdearActualReports.size();i++){
//                    if(famdearActualReports.get(i).getCreateUserId().equals(getLoginInfo().getUserId())){
//                        famdearActualReports.remove(famdearActualReports.get(i));
//                    }
//                }
//                List<FamdearActualReport> famdearActualReports1 = famdearActualReportService.getListByArrangeIds(ids.toArray(new String[0]), getLoginInfo().getUserId(), null, null);
//                if(CollectionUtils.isNotEmpty(famdearActualReports1)){
//                    famdearActualReports.addAll(famdearActualReports1);
//                }
//                if(CollectionUtils.isNotEmpty(famdearActualReports)) {
//                    list1 = famdearActualReportService.getListByIdsPage(famdearActualReports.stream().map(FamdearActualReport::getId).collect(Collectors.toSet()).toArray(new String[0]), page);
//                }
                if(CollectionUtils.isNotEmpty(ids)) {
                    list1 = famdearActualReportService.getListByArrangeIds(ids.toArray(new String[0]), null, null, null);
                }
            }
        }else {
            if(CollectionUtils.isNotEmpty(ids)) {
                List<FamdearActualReport> famdearActualReports = famdearActualReportService.getListByArrangeIds(ids.toArray(new String[0]), null, FamDearConstant.PLAN_PUBLISH, null);
                for(int i = 0;i<famdearActualReports.size();i++){
                    if(famdearActualReports.get(i).getCreateUserId().equals(getLoginInfo().getUserId())){
                        famdearActualReports.remove(famdearActualReports.get(i));
                    }
                }
                List<FamdearActualReport> famdearActualReports1 = famdearActualReportService.getListByArrangeIds(ids.toArray(new String[0]), getLoginInfo().getUserId(), null, null);
                if(CollectionUtils.isNotEmpty(famdearActualReports1)){
                    famdearActualReports.addAll(famdearActualReports1);
                }
                if(CollectionUtils.isNotEmpty(famdearActualReports)) {
                    list1 = famdearActualReportService.getListByIdsPage(famdearActualReports.stream().map(FamdearActualReport::getId).collect(Collectors.toSet()).toArray(new String[0]), null);
                }
                //list1 = famdearActualReportService.getListByArrangeIds(ids.toArray(new String[0]), null, FamDearConstant.PLAN_PUBLISH, page);
            }
        }
        //为了避免for循环
        List<FamDearAttachment> attachments = famDearAttachmentService.findListBy(new String[]{"unitId"},new String[]{getLoginInfo().getUnitId()});
        Set<String> objIds = attachments.stream().map(FamDearAttachment::getObjId).collect(Collectors.toSet());
        Map<String,List<FamDearAttachment>> listMap = new HashMap<>();
        for(String id:objIds){
            List<FamDearAttachment> list2 = new ArrayList<>();
            for(FamDearAttachment famDearAttachment:attachments){
                if(StringUtils.isNotBlank(famDearAttachment.getObjId())) {
                    if (famDearAttachment.getObjId().equals(id)) {
                        list2.add(famDearAttachment);
                    }
                }
            }
            listMap.put(id,list2);
        }

        if(CollectionUtils.isNotEmpty(list1)) {
            for (FamdearActualReport famdearActualReport : list1) {
                FamDearArrange famDearArrange = famDearArrangeMap.get(famdearActualReport.getArrangeId());
                if(famDearActivity!=null){
                    famdearActualReport.setActivityName(famDearActivity.getTitle());
                }
                if (famDearArrange != null) {
                    if(famDearArrange.getApplyTime()!=null&&famDearArrange.getApplyEndTime()!=null){
                        famdearActualReport.setActivityTimeStr(DateUtils.date2String(famdearActualReport.getArriveTime(), "yyyy.MM.dd")+"-"+DateUtils.date2String(famdearActualReport.getBackTime(), "yyyy.MM.dd"));
                    }
                    famdearActualReport.setVillageName(famDearArrange.getRural());
                }
                if (famdearActualReport.getCreateTime() != null) {
                    famdearActualReport.setCreateTimeStr(DateUtils.date2String(famdearActualReport.getCreateTime(), "yyyy-MM-dd"));
                }
                List<String> activityTitles = Arrays.asList(famdearActualReport.getActivityTitle().split(","));
                int picNum=0;
                List<FamDearAttachment> atts = new ArrayList<>();
                atts = listMap.get(famdearActualReport.getId());
                if(CollectionUtils.isNotEmpty(atts)) {
                    picNum = picNum + atts.size();
                }
                famdearActualReport.setImageNum(picNum);
                famdearActualReport.setStateStr(famdearActualReport.getState().equals(FamDearConstant.PLAN_PUBLISH) ? "已提交" : "未提交");
            }
        }
        String titleName = "";
        if(StringUtils.isNotBlank(year)){
            titleName= titleName+year;
        }
        if(StringUtils.isNotBlank(activityId)){
            FamDearActivity famDearActivity1 = famDearActivityService.findOne(activityId);
            titleName = titleName+famDearActivity1.getTitle();
        }
        if(StringUtils.isNotBlank(villageName)){
            titleName = titleName+villageName;
        }
        doExportList(list1,getResponse(),titleName);
    }

    private void doExportList(List<FamdearActualReport> famdearActualReportList, HttpServletResponse response, String titleName){
        Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
        List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
        if(CollectionUtils.isNotEmpty(famdearActualReportList)){
            for(FamdearActualReport famdearActualReport:famdearActualReportList) {
                Map<String, String> sMap = new HashMap<String, String>();
                sMap.put("访亲轮次名称", famdearActualReport.getActivityName());
                sMap.put("结亲村", famdearActualReport.getVillageName());
                sMap.put("活动时间", famdearActualReport.getActivityTimeStr());
                sMap.put("活动图片数", famdearActualReport.getImageNum()+"");
                sMap.put("状态", famdearActualReport.getStateStr());
                recordList.add(sMap);
            }
        }
        sheetName2RecordListMap.put("报名审核",recordList);
        Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
        List<String> tis = getRowTitleList();
        titleMap.put("报名审核", tis);
        ExportUtils exportUtils = ExportUtils.newInstance();
        exportUtils.exportXLSFile( titleName+"办实事填报信息导出", titleMap, sheetName2RecordListMap, response);
    }

    private List<String> getRowTitleList() {
        List<String> tis = new ArrayList<String>();
        tis.add("访亲轮次名称");
        tis.add("结亲村");
        tis.add("活动时间");
        tis.add("活动图片数");
        tis.add("状态");
        return tis;
    }

    @ResponseBody
    @ControllerInfo("删除附件")
    @RequestMapping("/delPic")
    public String deleteAtts(String id, ModelMap map){
        if(org.apache.commons.lang3.StringUtils.isEmpty(id)){
            return error("没有选择要删除的记录！");
        }
        try {
            FamDearAttachment famDearAttachment = famDearAttachmentService.findOne(id);
            famDearAttachment.setIsDelete("1");
            famDearAttachmentService.save(famDearAttachment);
//            famDearAttachmentService.delete(org.apache.commons.lang3.StringUtils.split(ids, ","));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return error("删除失败！");
        }
        return success("删除成功！");
    }

    @RequestMapping("/updateReportState")
    @ResponseBody
    public String updateReportState(String id,String state){
        FamdearActualReport famdearActualReport = famdearActualReportService.findOne(id);
        if(state.equals(FamDearConstant.PLAN_UNPUBLISH)){
            famdearActualReport.setState(PLAN_PUBLISH);
        }else {
            famdearActualReport.setState(FamDearConstant.PLAN_UNPUBLISH);
        }
        try {
            famdearActualReportService.update(famdearActualReport,id,new String[]{"state"});
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @RequestMapping("/delReport")
    @ResponseBody
    public String delReport(String id){
        try {
            FamdearActualReport famdearActualReport = famdearActualReportService.findOne(id);
            if(famdearActualReport.getState().equals(FamDearConstant.PLAN_PUBLISH)){
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("请先取消提交再进行删除！"));
            }
            famdearActualReportService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @RequestMapping("/editReport")
    public String editReport(String year,String activityId,String village,String id,String type,ModelMap map){
        FamdearActualReport famdearActualReport = famdearActualReportService.findOne(id);


        List<FamilyDearRegister> list = familyDearRegisterService.findListBy(new String[]{"teaUserId","status"},new String[] {getLoginInfo().getUserId(),"2"});
        Set<String> ids = new HashSet<>();
        Set<String> activityIds = new HashSet<>();
        for(FamilyDearRegister familyDearRegister:list){
            activityIds.add(familyDearRegister.getActivityId());
        }
        List<FamDearActivity> activities = famDearActivityService.findListByIdIn(activityIds.toArray(new String[0]));
        map.put("activityList",activities);
        Set<String> arrangeIds = new HashSet<>();
        for(FamilyDearRegister familyDearRegister:list){
            if(familyDearRegister.getActivityId().equals(famdearActualReport.getActivityId())) {
                arrangeIds.add(familyDearRegister.getArrangeId());
            }
        }
        List<FamDearArrange> list1 = famDearArrangeService.findListByIdIn(ids.toArray(new String[0]));
        map.put("arrangeList",list1);
//        Set<String> ids = new HashSet<>();
//        for(FamilyDearRegister familyDearRegister:list){
//            ids.add(familyDearRegister.getArrangeId());
//        }
//        List<FamDearArrange> famDearArrangeList = new ArrayList<>();
//        List<FamDearArrange> list1 = famDearArrangeService.findListByIdIn(ids.toArray(new String[0]));
//        for(FamDearArrange famDearArrange:list1){
//            if(famDearArrange.getApplyTime().compareTo(new Date())<0){
//                famDearArrangeList.add(famDearArrange);
//            }
//        }

        if(StringUtils.isNotBlank(famdearActualReport.getActivityFrom())){
            famdearActualReport.setActivityFroms(famdearActualReport.getActivityFrom().split(","));
        }
        List<String> activityTitles = Arrays.asList(famdearActualReport.getActivityTitle().split(","));
        List<String> ticketTitles = Arrays.asList(famdearActualReport.getTicketTitle().split(","));
        //活动列表
        List<AttachmentDto> attachmentDtos = new ArrayList<>();
        //车票列表
        List<AttachmentDto> attachmentDtos1 = new ArrayList<>();
        List<FamDearAttachment> famDearAttachmentList = famDearAttachmentService.findListByObjIds(new String[]{famdearActualReport.getId()});
        int i = 1;
        int j = 1;
        for(String title:activityTitles){
            AttachmentDto attachmentDto = new AttachmentDto();
            attachmentDto.setTitle(title);
            List<FamDearAttachment> atts = new ArrayList<>();
            for(FamDearAttachment famDearAttachment:famDearAttachmentList){
                if(famDearAttachment.getObjecttype().equals("activity_"+i)){
                    atts.add(famDearAttachment);
                }
            }
            attachmentDto.setAttachmentList(atts);
            attachmentDtos.add(attachmentDto);
            i++;
        }
        for(String ticket:ticketTitles){
            AttachmentDto attachmentDto = new AttachmentDto();
            attachmentDto.setTitle(ticket);
            List<FamDearAttachment> atts = new ArrayList<>();
            for(FamDearAttachment famDearAttachment:famDearAttachmentList){
                if(famDearAttachment.getObjecttype().equals("ticket_"+j)){
                    atts.add(famDearAttachment);
                }
            }
            attachmentDto.setAttachmentList(atts);
            attachmentDtos1.add(attachmentDto);
            j++;
        }
        if(StringUtils.isNotBlank(famdearActualReport.getActivityId())){
            FamDearActivity famDearActivity = famDearActivityService.findOne(famdearActualReport.getActivityId());
            famdearActualReport.setActivityName(famDearActivity.getTitle());
        }
        if(StringUtils.isNotBlank(famdearActualReport.getArrangeId())){
            FamDearArrange famDearArrange = famDearArrangeService.findOne(famdearActualReport.getArrangeId());
            famdearActualReport.setArrangeName(famDearArrange.getBatchType());
        }
        famdearActualReport.setActivityTitles(attachmentDtos);
        famdearActualReport.setTicketTitles(attachmentDtos1);
        map.put("activityTitlesSize",attachmentDtos.size());
        map.put("ticketTitlesSize",attachmentDtos1.size());
        map.put("type",type);
        map.put("famdearActualReport",famdearActualReport);
        map.put("year",year);
        map.put("activityId",activityId);
        map.put("village",village);
        return "/familydear/famDearActualReport/famDearActualReportEdit.ftl";
    }

    @ControllerInfo("新增附件")
    @RequestMapping("/saveAttachment")
    public String saveAtt(ModelMap map, HttpServletRequest request){
        try {
            List<MultipartFile> files = StorageFileUtils.getFiles(request);
            if(CollectionUtils.isEmpty(files)){
                return "";
            }
            String objType = request.getParameter("objType");
            String objId = request.getParameter("objId");
            for (int i=0;i<files.size();i++) {
                MultipartFile file = files.get(i);
                FamDearAttachment att = new FamDearAttachment();
                att.setIsDelete("0");
                att.setObjecttype(objType);
                att.setObjId(objId);
                att.setUnitId(getLoginInfo().getUnitId());
                try {
                    famDearAttachmentService.saveAttachment(att, file,true);
                } catch (Exception e) {
                    log.error("第"+(i+1)+"个附件保存失败："+e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }


    @ControllerInfo("显示图片附件")
    @RequestMapping("/showAllpic")
    public String Edit(String id,String objType,ModelMap modelMap){
        List<FamDearAttachment> atts = new ArrayList<>();
        atts = famDearAttachmentService.getAttachmentByObjId(id, objType);
        modelMap.put("id",id);
        modelMap.put("actDetails", atts);
        return "familydear/famDearActualReport/famDearImages.ftl";
    }


    @ControllerInfo("显示图片附件")
    @RequestMapping("/showPic")
    public String showPic(String id, String showOrigin, HttpServletResponse response){
        try {
            FamDearAttachment att = famDearAttachmentService.findOne(id);
            if(att == null){
                return null;
            }
            StorageDir sd = SUtils.dc(storageDirRemoteService.findOneById(att.getDirId()), StorageDir.class);
            if(sd == null){
                return null;
            }
            File img = new File(sd.getDir() + File.separator + att.getFilePath());
//			File img = new File("D:\\store" + File.separator + att.getFilePath());

            if(img == null || !img.exists()) {
                return null;
            }
            File pic = null;
            if(Constant.IS_TRUE_Str.equals(showOrigin)){
                String dirPath = img.getParent();
                String originFilePath = dirPath + File.separator + StuDevelopConstant.PIC_ORIGIN_NAME+"."+att.getExtName();
                pic = new File(originFilePath);
            } else if(StuDevelopConstant.IS_MOBILE_STR.equals(showOrigin)){//当2时  显示手机端的图片(更小)
                String originFilePath = img.getParent() + File.separator + StuDevelopConstant.PIC_MOBILE_NAME+"."+att.getExtName();
                pic = new File(originFilePath);
            }else{
                pic = img;
            }
            if(pic == null || !pic.exists()) {
                pic = img;
            }
            if(pic != null && pic.exists()){
                response.getOutputStream().write(FileUtils.readFileToByteArray(pic));
				/*long time1=System.currentTimeMillis();
				BasicFileAttributes bfa=Files.readAttributes(pic.toPath(),BasicFileAttributes.class);
				System.out.println(bfa.creationTime().toString());
				System.out.println(bfa.lastModifiedTime().toString());
				long time2=System.currentTimeMillis();
				System.out.println(time2-time1);*/
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
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

}
