package net.zdsoft.studevelop.data.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.entity.StuDevelopHonorRecord;
import net.zdsoft.studevelop.data.service.StuDevelopHonorRecordService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/studevelop") 
public class StuDevelopHonorRecordAction extends BaseAction{
	
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private StuDevelopHonorRecordService honorRecordService;
	
	@RequestMapping("/honorRecord/index/page")
    @ControllerInfo(value = "荣誉评选index")
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
		return "/studevelop/record/stuHonorRecordIndex.ftl";
	}
	
	@RequestMapping("/honorRecordQuery/index/page")
    @ControllerInfo(value = "荣誉评选查询index")
    public String QueryshowIndex(ModelMap map) {
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
		return "/studevelop/query/honorRecordQuery.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/honorRecord/classIds")
    @ControllerInfo(value = "获得班级列表")
	public List<Clazz> acadyearUpdate(String acadyear,String semester) {
		List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(getLoginInfo().getUnitId(),acadyear), new TR<List<Clazz>>() {});
		return classList;
	}
	
	@ResponseBody
	@RequestMapping("/honorRecord/stuIds")
    @ControllerInfo(value = "获得学生列表")
	public List<Student> classUpdate(String classId,ModelMap map) {
		Map<String, List<Student>> stuMap = SUtils.dt(studentRemoteService.findMapByClassIdIn(new String[]{classId}),new TR<Map<String, List<Student>>>(){});
		List<Student> stuList = new ArrayList<Student>(); 
		if(!stuMap.isEmpty()){
			stuList = stuMap.get(classId);
		}
		return stuList;
	}
	
	@RequestMapping("/honorRecordQuery/list")
    @ControllerInfo(value = "班级荣誉查询列表")
	public String honorRecordQueryListAll(String acadyear,String semester,String honortype,String classIdSearch,ModelMap map) {
		List<StuDevelopHonorRecord> honorRecords = new ArrayList<StuDevelopHonorRecord>();
		if((!"0".equals(honortype)) && classIdSearch != ""){
			Map<String, List<Student>> stuMap = SUtils.dt(studentRemoteService.findMapByClassIdIn(new String[]{classIdSearch}),new TR<Map<String, List<Student>>>(){});
			if(!stuMap.isEmpty()){
			List<Student> stuList = stuMap.get(classIdSearch);
			Map<String, Student> clsStuMap = EntityUtils.getMap(stuList, Student::getId);
			honorRecords = honorRecordService.findByfindBytypeAndclass(getLoginInfo().getUnitId(),honortype,acadyear,semester,clsStuMap.keySet().toArray(new String[0]));
			}
		}
		if("0".equals(honortype) && classIdSearch == ""){
			honorRecords = honorRecordService.findAllhonor(acadyear,semester,getLoginInfo().getUnitId());
		}
		if((!"0".equals(honortype)) && classIdSearch == ""){
			honorRecords = honorRecordService.findByHonortype(honortype,acadyear,semester,getLoginInfo().getUnitId());
		}
		if("0".equals(honortype) && classIdSearch != ""){
			Map<String, List<Student>> stuMap = SUtils.dt(studentRemoteService.findMapByClassIdIn(new String[]{classIdSearch}),new TR<Map<String, List<Student>>>(){});
			if(!stuMap.isEmpty()){
			List<Student> stuList = stuMap.get(classIdSearch);
			Map<String, Student> clsStuMap = EntityUtils.getMap(stuList, Student::getId);
			honorRecords = honorRecordService.findListByCls(getLoginInfo().getUnitId(),acadyear,semester,clsStuMap.keySet().toArray(new String[0]));
			}
		}
		for(StuDevelopHonorRecord developHonorRecord : honorRecords){
			if((StuDevelopConstant.HONOR_TYPE_XJRW).equals(developHonorRecord.getHonorType())){
				developHonorRecord.setHonorTypeStr("星级人物");
			}else{
				developHonorRecord.setHonorTypeStr("七彩阳光卡");
			}
			Student student = SUtils.dc(studentRemoteService.findOneById(developHonorRecord.getStudentId()),Student.class);
			Clazz myclass = SUtils.dc(classRemoteService.findOneById(student.getClassId()),Clazz.class);
			Grade grade = SUtils.dc(gradeRemoteService.findOneById(myclass.getGradeId()),Grade.class);
			developHonorRecord.setStudentName(student.getStudentName());
			developHonorRecord.setClassName(grade.getGradeName()+myclass.getClassName());
		}
		map.put("honorRecordsList", honorRecords);
		return "/studevelop/query/honorRecordQueryList.ftl";
	}
	
	@RequestMapping("/honorRecord/listAll")
    @ControllerInfo(value = "班级荣誉列表")
	public String honorRecordListAll(@RequestParam String acadyear,@RequestParam String semester,@RequestParam String classId,ModelMap map) {
		Map<String, List<Student>> stuMap = SUtils.dt(studentRemoteService.findMapByClassIdIn(new String[]{classId}),new TR<Map<String, List<Student>>>(){});
		List<StuDevelopHonorRecord> honorRecords = new ArrayList<StuDevelopHonorRecord>();
		if(!stuMap.isEmpty()){
		List<Student> stuList = stuMap.get(classId);
		Map<String, Student> clsStuMap = EntityUtils.getMap(stuList, Student::getId);
		honorRecords = honorRecordService.findListByCls(getLoginInfo().getUnitId(),acadyear,semester,clsStuMap.keySet().toArray(new String[0]));
		for(StuDevelopHonorRecord developHonorRecord : honorRecords){
			if((StuDevelopConstant.HONOR_TYPE_XJRW).equals(developHonorRecord.getHonorType())){
				developHonorRecord.setHonorTypeStr("星级人物");
			}else{
				developHonorRecord.setHonorTypeStr("七彩阳光卡");
			}
			if (clsStuMap.containsKey(developHonorRecord.getStudentId())){
				developHonorRecord.setStudentName(clsStuMap.get(developHonorRecord.getStudentId()).getStudentName());
			}
		}
		}
		map.put("honorRecordsList", honorRecords);
		return "/studevelop/record/stuHonorRecordList.ftl";
	}
	
	@RequestMapping("/honorRecord/list")
    @ControllerInfo(value = "学生荣誉列表")
	public String honorRecordList(@RequestParam String acadyear,@RequestParam String semester,@RequestParam String stuId,ModelMap map) {
		List<StuDevelopHonorRecord> honorRecords = honorRecordService.getHonorList(getLoginInfo().getUnitId(),acadyear,semester,stuId);
		String studentName = SUtils.dc(studentRemoteService.findOneById(stuId),Student.class).getStudentName();
		for(StuDevelopHonorRecord developHonorRecord : honorRecords){
			if((StuDevelopConstant.HONOR_TYPE_XJRW).equals(developHonorRecord.getHonorType())){
				developHonorRecord.setHonorTypeStr("星级人物");
			}else{
				developHonorRecord.setHonorTypeStr("七彩阳光卡");
			}
			developHonorRecord.setStudentName(studentName);
		}
		map.put("honorRecordsList", honorRecords);
		return "/studevelop/record/stuHonorRecordList.ftl";
	}
	
	@RequestMapping("/honorRecord/add")
    @ControllerInfo(value = "添加荣誉")
	public String honorRecordAdd(@RequestParam String acadyear,@RequestParam String semester,@RequestParam String stuId,ModelMap map) {
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("stuId", stuId);
		return "/studevelop/record/stuHonorRecordAdd.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value="/honorRecord/save",method = {RequestMethod.POST,RequestMethod.GET})
    @ControllerInfo(value = "新增荣誉保存")
	public String honorRecordSave( String acadyear, String semester, String stuId,
			 Date giveDateXjrw, Date giveDateQcygk,
			 String XJRWremark, String QCYGKremark,
			 String[] XJRWhonorLevel, String[] QCYGKhonorLevel) {
		try {
			StuDevelopHonorRecord honorRecordXJRW = new StuDevelopHonorRecord();
			if(XJRWhonorLevel !=null && XJRWhonorLevel.length > 0){
				honorRecordXJRW.setUnitId(getLoginInfo().getUnitId());
				honorRecordXJRW.setAcadyear(acadyear);
				honorRecordXJRW.setSemester(semester);
				honorRecordXJRW.setStudentId(stuId);
				honorRecordXJRW.setGiveDate(giveDateXjrw);
				honorRecordXJRW.setRemark(XJRWremark);
				honorRecordXJRW.setHonorType(StuDevelopConstant.HONOR_TYPE_XJRW);
				honorRecordXJRW.setHonorLevelArray(XJRWhonorLevel);
			}
			StuDevelopHonorRecord honorRecordQCYGK = new StuDevelopHonorRecord();
			if(QCYGKhonorLevel !=null && QCYGKhonorLevel.length > 0){
				honorRecordQCYGK.setUnitId(getLoginInfo().getUnitId());
				honorRecordQCYGK.setAcadyear(acadyear);
				honorRecordQCYGK.setSemester(semester);
				honorRecordQCYGK.setStudentId(stuId);
				honorRecordQCYGK.setGiveDate(giveDateQcygk);
				honorRecordQCYGK.setRemark(QCYGKremark);
				honorRecordQCYGK.setHonorType(StuDevelopConstant.HONOR_TYPE_QCYGK);
				honorRecordQCYGK.setHonorLevelArray(QCYGKhonorLevel);
			}
			honorRecordService.save(honorRecordXJRW,honorRecordQCYGK);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	
	@RequestMapping("/honorRecord/edit")
    @ControllerInfo(value = "修改荣誉")
	public String honorRecordUpdate(@RequestParam String id,ModelMap map) {
		StuDevelopHonorRecord stuDevelopHonorRecord = honorRecordService.findById(id);
		map.put("stuDevelopHonorRecord",stuDevelopHonorRecord);
		return "/studevelop/record/stuHonorRecordEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/honorRecord/updateSave")
    @ControllerInfo(value = "修改荣誉保存")
	public String honorRecordUpdateSave(StuDevelopHonorRecord stuDevelopHonorRecord) {
		try {
			honorRecordService.save(stuDevelopHonorRecord);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/honorRecord/del")
    @ControllerInfo(value = "删除")
	public String honorRecordDel(String id){
		try {
			honorRecordService.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
}
