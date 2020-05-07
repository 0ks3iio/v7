package net.zdsoft.studevelop.data.action;

import java.util.List;

import net.zdsoft.basedata.entity.Semester;
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
public class EvaluateResultAction extends BaseAction{
	
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private StuEvaluateRecordService stuEvaluateRecordService;
	
	@RequestMapping("/evaluateRecord/stuReview/page")
    @ControllerInfo(value = "记录回顾与展望")
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
		return "/studevelop/stuplat/recordReviewIndex.ftl";
	}
	
	@RequestMapping("/recordReview/detail")
	@ControllerInfo(value = "记录回顾与展望详情")
	public String recordReviewDetail(@RequestParam String acadyear,@RequestParam String semester,ModelMap map){
		StuEvaluateRecord evaluateRecord = stuEvaluateRecordService.findById(getLoginInfo().getOwnerId(),acadyear,semester);
		if(evaluateRecord == null){
			evaluateRecord = new StuEvaluateRecord();
			evaluateRecord.setId(UuidUtils.generateUuid());
			evaluateRecord.setAcadyear(acadyear);
			evaluateRecord.setSemester(semester);
			evaluateRecord.setStudentId(getLoginInfo().getOwnerId());
		}
		map.put("evaluateRecord", evaluateRecord);
		return "/studevelop/stuplat/recordReviewEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/recordReview/save")
    @ControllerInfo(value = "保存")
	public String recordReviewSave(StuEvaluateRecord stuEvaluateRecord){
		try {
			stuEvaluateRecordService.save(stuEvaluateRecord);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
}
