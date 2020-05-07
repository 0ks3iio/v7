package net.zdsoft.studevelop.data.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.entity.StudevelopSchoolNotice;
import net.zdsoft.studevelop.data.service.StuDevelopSchoolNoticeService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2018/4/9.
 */
@Controller
@RequestMapping("/studevelop")
public class StuDevelopSchoolNoticeAction extends BaseAction {

    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private StuDevelopSchoolNoticeService stuDevelopSchoolNoticeService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private SystemIniRemoteService systemIniRemoteService;
    @RequestMapping("templateSet/index/page")
    public String schoolNoticeIndex(ModelMap map){
        String deployRegion = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
        map.put("deployRegion" ,deployRegion);
        return "/studevelop/template/templateSetAdmin.ftl";
    }
    @RequestMapping("templateSet/schoolNotice/head")
    public String schoolNoticeHead(ModelMap modelMap){
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(),new TR<List<String>>(){});
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
        //获取学段
        String unitId = getLoginInfo().getUnitId();
        String sections = schoolRemoteService.findSectionsById(unitId);
        String[] sectionArray = sections.split(",");
        Map<String, String> sectionMap = new HashMap<String, String>();
        if(sectionArray.length==0){
            modelMap.put("section", sections);
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
            modelMap.put("sectionMap", sectionMap);
        }
        return "/studevelop/template/templateSetHead.ftl";
    }
    @RequestMapping("templateSet/schoolNotice/edit")
    public String schoolNoticeEdit(String acadyear , String semester , String section , ModelMap map){
        String unitId = getLoginInfo().getUnitId();
        StudevelopSchoolNotice schoolNotice = stuDevelopSchoolNoticeService.getSchoolNoticeByAcadyearSemesterUnitId(acadyear,semester,section,unitId);
        if(schoolNotice == null){
            schoolNotice = new StudevelopSchoolNotice();
            schoolNotice.setUnitId(unitId);
            schoolNotice.setAcadyear(acadyear);
            schoolNotice.setSemester(semester);
            schoolNotice.setSchoolSection(section);
            Semester nextSeme = getNextSemeData(schoolNotice,acadyear,semester);
			if(nextSeme == null){
				return errorFtl(map,"还没有维护过下个学年学期，请先维护！");
			}
        }else{
        	if(StringUtils.isBlank(schoolNotice.getRegisterBegin()) || StringUtils.isBlank(schoolNotice.getStudyBegin())){
        		Semester nextSeme =getNextSemeData(schoolNotice,acadyear,semester);
        		if(nextSeme == null){
    				return errorFtl(map,"还没有维护过下个学年学期，请先维护！");
    			}
        	}
        }
        map.put("schoolNotice" ,schoolNotice);

        return "/studevelop/template/schoolNoticeEdit.ftl";
    }
    /**  获取(当前学年学期的)下学期数据
     * 	 传入schoolNotice的情况下 将注册正式 时间赋值	
     * */
	public Semester getNextSemeData(StudevelopSchoolNotice schoolNotice,String acadyear, String semester){
	    String semStr;
	    String acaStr;
	    if("1".equals(semester)){
	    	semStr = "2";
	    	acaStr = acadyear;
	    }else{
	    	String[] acadyearArr = acadyear.split("-");
	    	acaStr =  String.valueOf(Integer.parseInt(acadyearArr[0])+1)+"-"+String.valueOf(Integer.parseInt(acadyearArr[1])+1);
	    	semStr = "1";
	    }
	    Semester nextSeme = SUtils.dc(semesterRemoteService.findByAcadYearAndSemester(acaStr, Integer.parseInt(semStr)), Semester.class);
		if(schoolNotice!=null){
			if(nextSeme != null){
				String register = new SimpleDateFormat("MM-dd").format(nextSeme.getRegisterDate().getTime());
				String study = new SimpleDateFormat("MM-dd").format(nextSeme.getSemesterBegin().getTime());
				schoolNotice.setRegisterBegin(register);
				schoolNotice.setStudyBegin(study);
			}else{
				return null;
			}
		}
	    return nextSeme;
	}
    @ResponseBody
    @RequestMapping("templateSet/schoolNotice/save")
    public String schoolNoticeSave(StudevelopSchoolNotice schoolNotice  , ModelMap map){
        try{
            if(StringUtils.isEmpty(schoolNotice.getId())){
                schoolNotice.setId(UuidUtils.generateUuid());
                schoolNotice.setCreationTime(new Date());
            }else{
                schoolNotice.setModifyTime(new Date());
            }
            stuDevelopSchoolNoticeService.save(schoolNotice);
        }catch(Exception e){
            e.printStackTrace();
            return returnError();
        }
    return returnSuccess();
    }
}
