package net.zdsoft.studevelop.data.action;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.entity.StuEvaluateRecord;
import net.zdsoft.studevelop.data.service.StuEvaluateRecordService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/studevelop") 
public class StuEvaluateRecordAction extends CommonAuthAction{
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private StuEvaluateRecordService stuEvaluateRecordService;
	
	@RequestMapping("/evaluateRecord/index/page")
    @ControllerInfo(value = "期末评语index")
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
		return "/studevelop/record/stuEvaluateRecordIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/evaluateRecord/classIds")
    @ControllerInfo(value = "获得班级列表")
	public List<Clazz> acadyearUpdate(String acadyear,String semester,ModelMap map) {
		List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(getLoginInfo().getUnitId(),acadyear), new TR<List<Clazz>>() {});
		return classList;
	}
	
	@ResponseBody
	@RequestMapping("/evaluateRecord/stuIds")
    @ControllerInfo(value = "获得学生列表")
	public List<Student> classUpdate(String classId,ModelMap map) {
		Map<String, List<Student>> stuMap = SUtils.dt(studentRemoteService.findMapByClassIdIn(new String[]{classId}),new TR<Map<String, List<Student>>>(){});
		List<Student> stuList = new ArrayList<Student>(); 
		if(!stuMap.isEmpty()){
			stuList = stuMap.get(classId);
		}
		return stuList;
	}
	
	@RequestMapping("/evaluateRecord/listAll")
	@ControllerInfo(value = "老师评语情况列表")
	public String evaluateRecordListAll(@RequestParam String acadyear,@RequestParam String semester,@RequestParam String classId,ModelMap map){
			Map<String, List<Student>> stuMap = SUtils.dt(studentRemoteService.findMapByClassIdIn(new String[]{classId}),new TR<Map<String, List<Student>>>(){});
			List<StuEvaluateRecord> evaluate = new ArrayList<StuEvaluateRecord>();
			if(!stuMap.isEmpty()){
			List<Student> stuList = stuMap.get(classId);
			Map<String, Student> clsStuMap = EntityUtils.getMap(stuList, "id");
			evaluate = stuEvaluateRecordService.findListByCls(acadyear,semester,clsStuMap.keySet().toArray(new String[0]));
			for(StuEvaluateRecord evaluateRecord : evaluate){
				if (clsStuMap.containsKey(evaluateRecord.getStudentId())) {
					evaluateRecord.setStudentName(clsStuMap.get(evaluateRecord.getStudentId()).getStudentName());
				}
			}
			}
			map.put("evaluateRecordList", evaluate);
			return "/studevelop/record/stuEvaluateRecordList.ftl";
	}
	
	@RequestMapping("/evaluateRecord/classList")
	@ControllerInfo(value = "班级期末老师评语情况")
	public String evaluateRecordList(@RequestParam String acadyear,@RequestParam String semester,@RequestParam String classId,ModelMap map){
		List<Student> stuList = SUtils.dt(studentRemoteService.findByClassIds(classId),new TR<List<Student>>(){});
		if(CollectionUtils.isNotEmpty(stuList)){
			List<StuEvaluateRecord> evaluateList =stuEvaluateRecordService.findListByCls(acadyear,semester,stuList.stream().map(Student::getId).collect(Collectors.toSet()).toArray(new String[0]));
			Map<String,StuEvaluateRecord> evaluateMap=EntityUtils.getMap(evaluateList, StuEvaluateRecord::getStudentId);
			List<StuEvaluateRecord> lastList=new ArrayList<>();
			for(Student stu:stuList){
				StuEvaluateRecord record=evaluateMap.get(stu.getId());
				if(record==null){
					record=new StuEvaluateRecord();
				}
				record.setStudentName(stu.getStudentName());
				lastList.add(record);
			}
			map.put("lastList", lastList);
		}
		return "/studevelop/record/stuEvaRecordClassList.ftl";
	}
	@RequestMapping("/evaluateRecord/Edit")
	@ControllerInfo(value = "期末老师评语情况")
	public String evaluateRecordEdit(@RequestParam String acadyear,@RequestParam String semester,@RequestParam String studentId,ModelMap map){
		StuEvaluateRecord stuEvaluateRecord = stuEvaluateRecordService.findById(studentId, acadyear, semester);
		if(stuEvaluateRecord == null){
			stuEvaluateRecord = new StuEvaluateRecord();
			stuEvaluateRecord.setId(UuidUtils.generateUuid());
			stuEvaluateRecord.setAcadyear(acadyear);
			stuEvaluateRecord.setSemester(semester);
			stuEvaluateRecord.setStudentId(studentId);
		}
		map.put("stuEvaluateRecord", stuEvaluateRecord);
		return "/studevelop/record/stuEvaluateRecordEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/evaluateRecord/save")
    @ControllerInfo(value = "保存")
	public String evaluateRecordSave(StuEvaluateRecord stuEvaluateRecord,String classId,String isAdmin){
		try {
			if(!isAdmin(StuDevelopConstant.PERMISSION_TYPE_REPORT)){//不是管理员的情况下
				String teaId = getLoginInfo().getOwnerId();
				Student stu = Student.dc(studentRemoteService.findOneById(stuEvaluateRecord.getStudentId()));
				Clazz cls = Clazz.dc(classRemoteService.findOneById(stu.getClassId()));
        		boolean canSave = StringUtils.equals(cls.getTeacherId(), teaId) || StringUtils.equals(cls.getViceTeacherId(), teaId);
        		if(!canSave) {
					return returnError("-1", "不是班主任没有保存权限!");
				}
        	}
			stuEvaluateRecordService.save(stuEvaluateRecord);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
}
