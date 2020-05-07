package net.zdsoft.studevelop.data.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import net.zdsoft.studevelop.data.entity.StuLeagueRecord;
import net.zdsoft.studevelop.data.service.StuLeagueRecordService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping("/studevelop") 
public class StuLeagueRecordAction extends BaseAction{
	
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private StuLeagueRecordService stuLeagueRecordService;
	
	@RequestMapping("/leagueRecord/index/page")
    @ControllerInfo(value = "社团活动登记index")
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
		return "/studevelop/record/stuLeagueRecordIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/leagueRecord/classIds")
    @ControllerInfo(value = "获得班级列表")
	public List<Clazz> acadyearUpdate(String acadyear,String semester,ModelMap map) {
		List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(getLoginInfo().getUnitId(),acadyear), new TR<List<Clazz>>() {});
		return classList;
	}
	
	@ResponseBody
	@RequestMapping("/leagueRecord/stuIds")
    @ControllerInfo(value = "获得学生列表")
	public List<Student> classUpdate(String classId,ModelMap map) {
		Map<String, List<Student>> stuMap = SUtils.dt(studentRemoteService.findMapByClassIdIn(new String[]{classId}),new TR<Map<String, List<Student>>>(){});
		List<Student> stuList = new ArrayList<Student>(); 
		if(!stuMap.isEmpty()){
			stuList = stuMap.get(classId);
		}
		return stuList;
	}
	
	@RequestMapping("/leagueRecord/listAll")
	@ControllerInfo(value = "获得社团活动情况列表")
	public String leagueRecordListAll(@RequestParam String acadyear,@RequestParam String semester,@RequestParam String classId,ModelMap map){
		Map<String, List<Student>> stuMap = SUtils.dt(studentRemoteService.findMapByClassIdIn(new String[]{classId}),new TR<Map<String, List<Student>>>(){});
		List<StuLeagueRecord> leagueRecords = new ArrayList<StuLeagueRecord>();
		if(!stuMap.isEmpty()){
		List<Student> stuList = stuMap.get(classId);
		Set<String> stuIdSet = new HashSet<String>();
		for(Student stu : stuList){
			stuIdSet.add(stu.getId());
		}
		leagueRecords = stuLeagueRecordService.findListByCls(acadyear,semester,stuIdSet.toArray(new String[0]));
		for(StuLeagueRecord leagueRecord  : leagueRecords){
			String studentName = SUtils.dc(studentRemoteService.findOneById(leagueRecord.getStudentId()),Student.class).getStudentName();
			leagueRecord.setStudentName(studentName);
		}
		}
		map.put("LeagueRecordList", leagueRecords);
		return "/studevelop/record/stuLeagueRecordList.ftl";
	}
	
	@RequestMapping("/leagueRecord/list")
	@ControllerInfo(value = "获得社团活动情况列表")
	public String leagueRecordList(@RequestParam String acadyear,@RequestParam String semester,@RequestParam String stuId,ModelMap map){
		List<StuLeagueRecord> LeagueRecordList = stuLeagueRecordService.getStuLeagueRecordList(stuId, acadyear, semester);
		String studentName = SUtils.dc(studentRemoteService.findOneById(stuId),Student.class).getStudentName();
		for(StuLeagueRecord leagueRecord : LeagueRecordList){
			leagueRecord.setStudentName(studentName);
		}
		map.put("LeagueRecordList", LeagueRecordList);
		return "/studevelop/record/stuLeagueRecordList.ftl";
	}
	
	@RequestMapping("/leagueRecord/edit")
    @ControllerInfo(value = "添加或修改信息")
	public String leagueRecordEdit(@RequestParam String acadyear,@RequestParam String semester,@RequestParam String stuId,@RequestParam String id,ModelMap map){
		StuLeagueRecord leagueRecord;
			if(id.isEmpty()){
				leagueRecord = new StuLeagueRecord();
				leagueRecord.setAcadyear(acadyear);
				leagueRecord.setSemester(semester);
				leagueRecord.setStudentId(stuId);
				leagueRecord.setId(UuidUtils.generateUuid());
			}else{
				leagueRecord = stuLeagueRecordService.findById(id);
			}
			map.put("leagueRecord",leagueRecord);
			return "/studevelop/record/stuLeagueRecordEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/leagueRecord/save")
    @ControllerInfo(value = "保存")
	public String leagueRecordSave(StuLeagueRecord leagueRecord){
		try {
			stuLeagueRecordService.save(leagueRecord);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/leagueRecord/del")
    @ControllerInfo(value = "删除")
	public String leagueRecordDel(String id){
		try {
			stuLeagueRecordService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
}
