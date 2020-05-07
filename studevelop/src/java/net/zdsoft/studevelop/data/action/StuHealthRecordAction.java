package net.zdsoft.studevelop.data.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.dto.StuHealthRecordDto;
import net.zdsoft.studevelop.data.entity.StuHealthRecord;
import net.zdsoft.studevelop.data.service.StuHealthRecordService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/studevelop") 
public class StuHealthRecordAction extends BaseAction{
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private StuHealthRecordService stuHealthRecordService;
	
	@RequestMapping("/healthRecord/index/page")
    @ControllerInfo(value = "身心健康登记index")
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
		return "/studevelop/record/stuHealthRecordIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/healthRecord/classIds")
    @ControllerInfo(value = "获得班级列表")
	public List<Clazz> acadyearUpdate(String acadyear,String semester,ModelMap map) {
		List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(getLoginInfo().getUnitId(),acadyear), new TR<List<Clazz>>() {});
		return classList;
	}
	
	@ResponseBody
	@RequestMapping("/healthRecord/stuIds")
    @ControllerInfo(value = "获得学生列表")
	public List<Student> classUpdate(String classId,ModelMap map) {
		Map<String, List<Student>> stuMap = SUtils.dt(studentRemoteService.findMapByClassIdIn(new String[]{classId}),new TR<Map<String, List<Student>>>(){});
		List<Student> stuList = new ArrayList<Student>(); 
		if(!stuMap.isEmpty()){
			stuList = stuMap.get(classId);
		}
		return stuList;
	}
	
	@RequestMapping("/healthRecord/Edit")
	@ControllerInfo(value = "获得身心健康情况详情")
	public String healthRecordList(@RequestParam String acadyear,@RequestParam String semester,@RequestParam String stuId,ModelMap map){
		StuHealthRecord healthRecord = stuHealthRecordService.getHealthRecordByStuIdSemes(stuId,acadyear,semester);
		StuHealthRecordDto healthRecordDto = new StuHealthRecordDto();
		if(healthRecord==null){
     		healthRecordDto.setId(UuidUtils.generateUuid());
			healthRecordDto.setAcadyear(acadyear);
			healthRecordDto.setSemester(semester);
			healthRecordDto.setStudentId(stuId);
		}else{
			healthRecordDto.toDto(healthRecord, healthRecordDto);
		}
		map.put("healthRecord", healthRecordDto);
		return "/studevelop/record/stuHealthRecordEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/healthRecord/save")
    @ControllerInfo(value = "保存")
	public String healthRecordSave(StuHealthRecordDto healthRecordDto){
		try {
			StuHealthRecord healthRecord = new StuHealthRecord();
			healthRecordDto.toEntity(healthRecord, healthRecordDto);
			stuHealthRecordService.save(healthRecord);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	
}
