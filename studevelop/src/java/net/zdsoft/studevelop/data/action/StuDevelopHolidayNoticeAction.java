package net.zdsoft.studevelop.data.action;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.entity.StuDevelopHolidayNotice;
import net.zdsoft.studevelop.data.service.StuDevelopHolidayNoticeService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/studevelop")
public class StuDevelopHolidayNoticeAction extends CommonAuthAction {
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private StuDevelopHolidayNoticeService stuDevelopHolidayNoticeService;
    @RequestMapping("/holidayNotice/index/page")
    @ControllerInfo(value = "假期事项")
    public String  showHolidayNotice(ModelMap modelMap){
        if(!isAdmin(StuDevelopConstant.PERMISSION_TYPE_GROWTH)){
            return promptFlt(modelMap , "不是成长手册管理员不能维护和查询假期事项内容");
        }
        List<String>  acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(),new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(modelMap , "学年学期不存在");
        }
        modelMap.put("acadyearList",acadyearList);
        Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()),Semester.class);
      //  StuDevelopHolidayNotice stuDevelopHolidayNotice = null;
        if(semesterObj != null){
            String currentAcadyear = semesterObj.getAcadyear();
            String currentSemester = semesterObj.getSemester() + "";
            modelMap.put("acadyear" ,currentAcadyear);
            modelMap.put("semester" , currentSemester);
          //  stuDevelopHolidayNotice = stuDevelopHolidayNoticeService.getStuDevelopHolidayNoticeByUnitId(currentAcadyear ,currentSemester,unitId );
        }else{
            modelMap.put("acadyear",acadyearList.get(0));
            modelMap.put("semester" ,"1");

        }
        return "/studevelop/stuGrow/stuDevelopHolidayNoticeMain.ftl";
    }
    @ResponseBody
    @RequestMapping("/holidayNotice/save")
    @ControllerInfo("save")
    public String noticeSave(StuDevelopHolidayNotice stuDevelopHolidayNotice){
        try{
            String unitId = getLoginInfo().getUnitId();
            StuDevelopHolidayNotice stuDevelopNotice=null;
            List<StuDevelopHolidayNotice>  stuDevelopNotices = stuDevelopHolidayNoticeService.getStuDevelopHolidayNoticeByUnitId(stuDevelopHolidayNotice.getAcadyear() ,stuDevelopHolidayNotice.getSemester(),unitId );
            if(CollectionUtils.isNotEmpty(stuDevelopNotices)){
                stuDevelopNotice = stuDevelopNotices.get(0);
            }
            if(stuDevelopNotice != null){
                stuDevelopNotice.setNotice(stuDevelopHolidayNotice.getNotice());
                stuDevelopHolidayNotice = stuDevelopNotice;
            }
            if(StringUtils.isEmpty(stuDevelopHolidayNotice.getId())){
                stuDevelopHolidayNotice.setId(UuidUtils.generateUuid());
                stuDevelopHolidayNotice.setCreationTime(new Date());
                stuDevelopHolidayNoticeService.save(stuDevelopHolidayNotice);
            }else{
                stuDevelopHolidayNotice.setModifyTime(new Date());
                stuDevelopHolidayNoticeService.update(stuDevelopHolidayNotice,stuDevelopHolidayNotice.getId(),new String[]{"notice" , "modifyTime"});
            }
        }catch(Exception e){
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @RequestMapping("/holidayNotice/getNotice")
    @ControllerInfo("getNotice by acad semester")
    public String getNoticeByAcadSemester(@RequestParam String acadyear,@RequestParam String semester , ModelMap map){
        LoginInfo  login = getLoginInfo();
        String unitId = login.getUnitId();
        StuDevelopHolidayNotice  stuDevelopNotice = null;

        if(StringUtils.isNotEmpty(acadyear)  && StringUtils.isNotEmpty(semester)){
            List<StuDevelopHolidayNotice>  stuDevelopNotices = stuDevelopHolidayNoticeService.getStuDevelopHolidayNoticeByUnitId(acadyear ,semester,unitId );
            if(CollectionUtils.isNotEmpty(stuDevelopNotices)){
                stuDevelopNotice = stuDevelopNotices.get(0);
            }

        }
        if(stuDevelopNotice == null){
            stuDevelopNotice = new StuDevelopHolidayNotice();
            stuDevelopNotice.setUnitId(unitId);
        }
        map.put("stuDevelopHolidayNotice" ,stuDevelopNotice);
        return "/studevelop/stuGrow/stuDevelopHolidayNoticeEdit.ftl";
    }
}
