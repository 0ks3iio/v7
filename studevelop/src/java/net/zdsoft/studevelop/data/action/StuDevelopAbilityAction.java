package net.zdsoft.studevelop.data.action;

import java.util.ArrayList;
import java.util.List;

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
import net.zdsoft.studevelop.data.entity.StuDevelopPunishment;
import net.zdsoft.studevelop.data.entity.StuDevelopRewards;
import net.zdsoft.studevelop.data.entity.StuEvaluateRecord;
import net.zdsoft.studevelop.data.entity.StuHealthRecord;
import net.zdsoft.studevelop.data.entity.StuLeagueRecord;
import net.zdsoft.studevelop.data.entity.StudevelopDutySituation;
import net.zdsoft.studevelop.data.service.StuDevelopPunishmentService;
import net.zdsoft.studevelop.data.service.StuDevelopRewardsService;
import net.zdsoft.studevelop.data.service.StuEvaluateRecordService;
import net.zdsoft.studevelop.data.service.StuHealthRecordService;
import net.zdsoft.studevelop.data.service.StuLeagueRecordService;
import net.zdsoft.studevelop.data.service.StudevelopDutySituationService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping("/studevelop") 
public class StuDevelopAbilityAction extends BaseAction{
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;	
	@Autowired
	private StudentRemoteService studenteRemoteService;
	@Autowired
	private StuDevelopRewardsService stuDevelopRewardsService;
	@Autowired
	private StuDevelopPunishmentService stuDevelopPunishmentService;
	@Autowired
	private StudevelopDutySituationService studevelopDutySituationService;
	@Autowired
	private StuLeagueRecordService stuLeagueRecordService;
	@Autowired
	private StuHealthRecordService stuHealthRecordService;
	@Autowired
	private StuEvaluateRecordService stuEvaluateRecordService;
	
	@ResponseBody
	@RequestMapping("/abilityQuery/clsList")
	public List<Clazz> clsList(String acayear){
		return SUtils.dt(classRemoteService.findByIdCurAcadyear(getLoginInfo().getUnitId(),acayear), new TR<List<Clazz>>() {});
	}
	
	@ResponseBody
	@RequestMapping("/abilityQuery/studentList")
	public List<Student> studentList(String classId){
		return SUtils.dt(studenteRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>(){});
	}
	
	@RequestMapping("/abilityQuery/index/page")
    @ControllerInfo(value = "学习能力tab")
	public String abilityHead(ModelMap map){
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()), Semester.class);
        List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(getLoginInfo().getUnitId(),semester.getAcadyear()), new TR<List<Clazz>>() {});
        String classId = "";
        String studentId = "";
        List<Student> studentList = new ArrayList<Student>();
        if(CollectionUtils.isNotEmpty(classList)){
        	classId = classList.get(0).getId();
        	studentList = SUtils.dt(studenteRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>(){});
        	if(CollectionUtils.isNotEmpty(studentList)){
        		studentId = studentList.get(0).getId();
        	}
        }
        map.put("studentList", studentList);
        map.put("classId", classId);
        map.put("studentId", studentId);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        map.put("classList", classList);
		return "/studevelop/query/ablityQueryHead.ftl";
	}
	
	
	@RequestMapping("/abilityQuery/showAblity")
    @ControllerInfo(value = "学习能力List")
	public String showAblity(String acadyear, String semester, String studentId, ModelMap map){
		StuHealthRecord stuHealthRecord;
		//奖励信息
		List<StuDevelopRewards> stuDevelopRewardsList = stuDevelopRewardsService.findByAcaAndSemAndStuId(acadyear, semester, studentId);
		//惩处信息
		List<StuDevelopPunishment> stuDevelopPunishmentList = stuDevelopPunishmentService.findByAcaAndSemAndStuId(acadyear, semester, studentId);
		//任职情况
		List<StudevelopDutySituation> studevelopDutySituationList = studevelopDutySituationService.getDutySituationList(studentId, acadyear, semester);
		//社团活动情况
		List<StuLeagueRecord> stuLeagueRecordList = stuLeagueRecordService.getStuLeagueRecordList(studentId, acadyear, semester);
		//身体健康 
		stuHealthRecord = stuHealthRecordService.getHealthRecordByStuIdSemes(studentId, acadyear, semester);
		if(null==stuHealthRecord){
			stuHealthRecord = new StuHealthRecord();
		}
		//期末评语
		StuEvaluateRecord stuEvaluateRecord;
		stuEvaluateRecord = stuEvaluateRecordService.findById(studentId, acadyear, semester);
		if(null == stuEvaluateRecord){
			stuEvaluateRecord = new StuEvaluateRecord();
		}
		map.put("stuDevelopRewardsList", stuDevelopRewardsList);
		map.put("stuDevelopPunishmentList", stuDevelopPunishmentList);
		map.put("studevelopDutySituationList", studevelopDutySituationList);
		map.put("stuLeagueRecordList", stuLeagueRecordList);
		map.put("stuHealthRecord", stuHealthRecord);
		map.put("stuEvaluateRecord", stuEvaluateRecord);
		return "/studevelop/query/ablityQueryList.ftl";
	}
	
	
}
