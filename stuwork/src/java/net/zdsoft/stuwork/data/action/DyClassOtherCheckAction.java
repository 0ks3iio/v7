package net.zdsoft.stuwork.data.action;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.entity.DyClassOtherCheck;
import net.zdsoft.stuwork.data.service.DyClassOtherCheckService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/stuwork")
public class DyClassOtherCheckAction extends BaseAction{

	@Autowired
	private DyClassOtherCheckService dyClassOtherCheckService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	
	@RequestMapping("/otherStart/index/page")
	@ControllerInfo(value = "班级其它考核Index")
	public String otherStartIndex(HttpServletRequest request,ModelMap map) {
		String unitId = getLoginInfo().getUnitId();
		
		//学年List
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        String acadyear  =request.getParameter("acadyear");
		Integer semester  =NumberUtils.toInt(request.getParameter("semester"));
		if(StringUtils.isBlank(acadyear)){
        	Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(0,unitId), Semester.class);
        	if(se == null){
            	se = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId), Semester.class);
            	semester = se.getSemester();
            	acadyear = se.getAcadyear();
            }else{
            	semester = se.getSemester();
            	acadyear = se.getAcadyear();
            }
        }
		
		//行政班级
		List<Clazz> clazzs =  SUtils.dt(classRemoteService.findBySchoolId(unitId),new TR<List<Clazz>>() {});
		
		Iterator<Clazz> it = clazzs.iterator();
		while(it.hasNext()) {
			Clazz clazz = it.next();
			if (clazz.getIsGraduate()==1) {
				it.remove();
			}
		}
		map.put("clazzList", clazzs);
		map.put("acadyearList", acadyearList);
		map.put("semester", semester);
		map.put("acadyear", acadyear);
		return "/stuwork/otherStat/otherStatIndex.ftl";
	}
	
	@RequestMapping("/otherStart/list/page")
	@ControllerInfo(value = "班级其它考核List")
	public String otherStartList(String acadyear, String semester, String classId, ModelMap map) {
		String unitId = getLoginInfo().getUnitId();
		List<DyClassOtherCheck> checksList = null;
		if (StuworkConstants.STUDENT_REWARD_ID.equals(classId)) {
			checksList = dyClassOtherCheckService.findByUnitId(unitId,acadyear,Integer.parseInt(semester));
		} else {
			checksList = dyClassOtherCheckService.findByClassId(classId,acadyear,Integer.parseInt(semester));
		}
		map.put("checksList", checksList);
		return "/stuwork/otherStat/otherStatList.ftl";
	}
	
	@RequestMapping("/otherStart/edit/page")
	@ControllerInfo(value = "班级其它考核edit")
	public String otherStartEdit(String id,String acadyear, String semester, ModelMap map) {
		String unitId = getLoginInfo().getUnitId();
		//行政班级
		List<Clazz> clazzs =  SUtils.dt(classRemoteService.findBySchoolId(unitId),new TR<List<Clazz>>() {});
						
		Iterator<Clazz> it = clazzs.iterator();
		while(it.hasNext()) {
			Clazz clazz = it.next();
			if (clazz.getIsGraduate()==1) {
				it.remove();
			}
		}
		
		DyClassOtherCheck dyClassOtherCheck = new DyClassOtherCheck(); 
		if (StringUtils.isNotBlank(id)) {
			List<DyClassOtherCheck> checks = dyClassOtherCheckService.findListByIds(new String[]{id});
			dyClassOtherCheck = checks.get(0);
		}
		map.put("clazzList", clazzs);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("dyClassOtherCheck", dyClassOtherCheck);
		return "/stuwork/otherStat/otherStatEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/otherStart/editSave")
	@ControllerInfo(value = "班级其它考核Save")
	public String editSave(DyClassOtherCheck dyClassOtherCheck,ModelMap map) {
		String unitId = getLoginInfo().getUnitId();
		try{
			DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(unitId, dyClassOtherCheck.getAcadyear(), dyClassOtherCheck.getSemester(), dyClassOtherCheck.getCheckTime()), DateInfo.class);
			dyClassOtherCheck.setWeek(dateInfo!=null?dateInfo.getWeek():1);
			if (StringUtils.isBlank(dyClassOtherCheck.getId())) {
				dyClassOtherCheck.setId(UuidUtils.generateUuid());
			}
			dyClassOtherCheck.setUnitId(unitId);
			dyClassOtherCheckService.save(dyClassOtherCheck);
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/otherStart/delete")
	@ControllerInfo(value = "班级其它考核delete")
	public String editDelete(String id,ModelMap map) {
		try{
			dyClassOtherCheckService.delete(id);
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
}
