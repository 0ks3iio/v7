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
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.entity.StudevelopDutySituation;
import net.zdsoft.studevelop.data.service.StudevelopDutySituationService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping("/studevelop") 
public class StudevelopDutySituationAction extends BaseAction{
	
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private StudevelopDutySituationService dutySituationService;
	
	@RequestMapping("/dutySituation/index/page")
    @ControllerInfo(value = "任职情况登记index")
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
		}
		return "/studevelop/record/dutySituationIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/dutySituation/classIds")
    @ControllerInfo(value = "获得班级列表")
	public List<Clazz> acadyearUpdate(String acadyear,String semester) {
		List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(getLoginInfo().getUnitId(),acadyear), new TR<List<Clazz>>() {});
		return classList;
	}
	
	@ResponseBody
	@RequestMapping("/dutySituation/stuIds")
    @ControllerInfo(value = "获得学生列表")
	public List<Student> classUpdate(String classId,ModelMap map) {
		Map<String, List<Student>> stuMap = SUtils.dt(studentRemoteService.findMapByClassIdIn(new String[]{classId}),new TR<Map<String, List<Student>>>(){});
		List<Student> stuList = new ArrayList<Student>(); 
		if(!stuMap.isEmpty()){
			stuList = stuMap.get(classId);
		}
		return stuList;
	}
	
	@RequestMapping("/dutySituation/listAll")
	@ControllerInfo(value = "获得班级任职情况列表")
	public String dutySituationListAll(@RequestParam String acadyear,@RequestParam String semester,@RequestParam String classId,ModelMap map){
			Map<String, List<Student>> stuMap = SUtils.dt(studentRemoteService.findMapByClassIdIn(new String[]{classId}),new TR<Map<String, List<Student>>>(){});
			List<StudevelopDutySituation> situations = new ArrayList<StudevelopDutySituation>();
			if(!stuMap.isEmpty()){
			List<Student> stuList = stuMap.get(classId);
			Map<String, Student> clsStuMap = EntityUtils.getMap(stuList, "id");
			situations = dutySituationService.findListByCls(acadyear,semester,clsStuMap.keySet().toArray(new String[0]));
			for(StudevelopDutySituation dutySituation : situations){
				if (clsStuMap.containsKey(dutySituation.getStudentId())) {
					dutySituation.setStudentName(clsStuMap.get(dutySituation.getStudentId()).getStudentName());
				}
			}
			}
			map.put("dutySituationList", situations);
			return "/studevelop/record/dutySituationList.ftl";
	}
	
	@RequestMapping("/dutySituation/list")
	@ControllerInfo(value = "获得任职情况列表")
	public String dutySituationList(@RequestParam String acadyear,@RequestParam String semester,@RequestParam String stuId,ModelMap map){
		List<StudevelopDutySituation> dutySituationList = dutySituationService.getDutySituationList(stuId, acadyear,semester);
		String studentName = SUtils.dc(studentRemoteService.findOneById(stuId),Student.class).getStudentName();
		for(StudevelopDutySituation dutySituation : dutySituationList){
			dutySituation.setStudentName(studentName);
		}
		map.put("dutySituationList", dutySituationList);
		return "/studevelop/record/dutySituationList.ftl";
	}
	
	@RequestMapping("/dutySituation/edit")
    @ControllerInfo(value = "添加或修改信息")
	public String dutySituationEdit(@RequestParam String acadyear,@RequestParam String semester,@RequestParam String stuId,@RequestParam String id,ModelMap map){
		StudevelopDutySituation Situation;
			if(id.isEmpty()){
					Situation = new StudevelopDutySituation();
					Situation.setAcadyear(acadyear);
					Situation.setSemester(semester);
					Situation.setStudentId(stuId);
					Situation.setId(UuidUtils.generateUuid());
				map.put("Situation",Situation);
			}else{
				Situation = dutySituationService.findById(id);
				map.put("Situation",Situation);
			}
			return "/studevelop/record/dutySituationEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/dutySituation/save")
    @ControllerInfo(value = "保存")
	public String dutySituationSave(StudevelopDutySituation dutySituation){
		try {
			dutySituationService.save(dutySituation);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/dutySituation/del")
    @ControllerInfo(value = "删除")
	public String dutySituationDel(String id){
		try {
			dutySituationService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
}
