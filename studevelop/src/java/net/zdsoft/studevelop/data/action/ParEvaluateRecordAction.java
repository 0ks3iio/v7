package net.zdsoft.studevelop.data.action;

import java.util.List;

import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.FamilyRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.entity.StuEvaluateRecord;
import net.zdsoft.studevelop.data.service.StuEvaluateRecordService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/studevelop") 
public class ParEvaluateRecordAction extends BaseAction{
	
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private StuEvaluateRecordService stuEvaluateRecordService;
	@Autowired
	private FamilyRemoteService familyRemoteService;
	
	@RequestMapping("/evaluateRecord/famEvaluate/page")
    @ControllerInfo(value = "孩子期末评价index")
    public String showIndex(ModelMap map) {
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
		    map.put("canEdit","false");
		}
		return "/studevelop/famplat/stuEvaluateRecordIndex.ftl";
	}
	
	@RequestMapping("/semesterEndEvaluate/detail")
	@ControllerInfo(value = "家长评价情况")
	public String semesterEndEvaluateDetail(@RequestParam String acadyear,@RequestParam String semester,ModelMap map){
		Family family = SUtils.dc(familyRemoteService.findOneById(getLoginInfo().getOwnerId()), Family.class);
		StuEvaluateRecord evaluateRecord = stuEvaluateRecordService.findById(family.getStudentId(),acadyear,semester);
		if(evaluateRecord == null){
			evaluateRecord = new StuEvaluateRecord();
			evaluateRecord.setId(UuidUtils.generateUuid());
			evaluateRecord.setAcadyear(acadyear);
			evaluateRecord.setSemester(semester);
			evaluateRecord.setStudentId(family.getStudentId());
		}
		map.put("evaluateRecord", evaluateRecord);
		return "/studevelop/famplat/stuEvaluateRecordEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/semesterEndEvaluate/save")
    @ControllerInfo(value = "保存")
	public String semesterEndEvaluateSave(StuEvaluateRecord stuEvaluateRecord){
		try {
			stuEvaluateRecordService.save(stuEvaluateRecord);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
}
