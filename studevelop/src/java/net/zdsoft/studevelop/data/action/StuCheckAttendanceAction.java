package net.zdsoft.studevelop.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.dto.StuCheckAttDto;
import net.zdsoft.studevelop.data.entity.StuCheckAttendance;
import net.zdsoft.studevelop.data.service.StuCheckAttendanceService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/studevelop") 
public class StuCheckAttendanceAction extends CommonAuthAction{
	
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private StuCheckAttendanceService stuCheckAttendanceService;
	
	@RequestMapping("/checkAttendance/index/page")
    @ControllerInfo(value = "考勤总计index")
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
		boolean isAdmin=isAdmin(StuDevelopConstant.PERMISSION_TYPE_REPORT);
		if(!isAdmin){//不是管理员 只能班主任看自己的班级
			List<Clazz> clazzList=SUtils.dt(classRemoteService.findByTeacherId(getLoginInfo().getOwnerId()),new TR<List<Clazz>>(){});
			 map.put("clazzList",clazzList);
		}
	    map.put("isAdmin",isAdmin);
		return "/studevelop/record/stuCheckAttendanceIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/checkAttendance/classIds")
    @ControllerInfo(value = "获得班级列表")
	public List<Clazz> acadyearUpdate(String acadyear,String semester,ModelMap map) {
		List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(getLoginInfo().getUnitId(),acadyear), new TR<List<Clazz>>() {});
		return classList;
	}
	
	@ResponseBody
	@RequestMapping("/checkAttendance/stuIds")
    @ControllerInfo(value = "获得学生列表")
	public List<Student> classUpdate(String classId,ModelMap map) {
		Map<String, List<Student>> stuMap = SUtils.dt(studentRemoteService.findMapByClassIdIn(new String[]{classId}),new TR<Map<String, List<Student>>>(){});
		List<Student> stuList = new ArrayList<Student>(); 
		if(!stuMap.isEmpty()){
			stuList = stuMap.get(classId);
		}
		return stuList;
	}
	
	@RequestMapping("/checkAttendance/listAll")
	@ControllerInfo(value = "班级考勤总计list")
	public String checkAttendanceListAll(@RequestParam String acadyear,@RequestParam String semester,@RequestParam String classId,ModelMap map){
		//获取班级下的所有学生 
		List<Student> stuList=SUtils.dt(studentRemoteService.findByClassIds(classId),new TR<List<Student>>(){});
		//最终返回的参数列
		List<StuCheckAttendance> lastList=new ArrayList<>();
		if(CollectionUtils.isNotEmpty(stuList)){
			Set<String> stuIdSet = stuList.stream().map(Student::getId).collect(Collectors.toSet());
			Map<String,StuCheckAttendance> checkAttMap=new HashMap<>();
			//获取学生考勤数据
			List<StuCheckAttendance> checkAttendanceslist = stuCheckAttendanceService.findListByCls(acadyear,semester,stuIdSet.toArray(new String[0]));
			if(CollectionUtils.isNotEmpty(checkAttendanceslist)){
				checkAttMap=checkAttendanceslist.stream().collect(Collectors.toMap(StuCheckAttendance::getStudentId, Function.identity(),(k2,k1)->k1));
			}
			//匹配对应的考勤数据 
			StuCheckAttendance checkAtt=null;
			for(Student stu:stuList){
				checkAtt=checkAttMap.get(stu.getId());
				if(checkAtt==null){
					checkAtt=new StuCheckAttendance();
					checkAtt.setId(UuidUtils.generateUuid());
					checkAtt.setStudentId(stu.getId());
					checkAtt.setAcadyear(acadyear);
					checkAtt.setSemester(semester);
				}
				checkAtt.setStudentName(stu.getStudentName());
				lastList.add(checkAtt);
			}
			map.put("lastList", lastList);
		}
		return "/studevelop/record/stuCheckAttendanceList.ftl";
	}
	
	@RequestMapping("/checkAttendance/list")
	@ControllerInfo(value = "考勤总计详情")
	public String checkAttendanceList(@RequestParam String acadyear,@RequestParam String semester,@RequestParam String stuId,ModelMap map){
		StuCheckAttendance stuCheckAttendance =  stuCheckAttendanceService.findBystudentId(acadyear,semester,stuId);
		StuCheckAttendance stuCheckAttendances = new StuCheckAttendance(); 
//		String register = null;
//		String study = null;
		if(stuCheckAttendance == null){
			stuCheckAttendances.setAcadyear(acadyear);
			stuCheckAttendances.setSemester(semester);
			stuCheckAttendances.setStudentId(stuId);
//			Semester nextSeme = getNextSemeData(acadyear,semester);
//			if(nextSeme != null){
//				register = new SimpleDateFormat("MM-dd").format(nextSeme.getRegisterDate().getTime());
//				study = new SimpleDateFormat("MM-dd").format(nextSeme.getSemesterBegin().getTime());
//				stuCheckAttendances.setRegisterBegin(register);
//				stuCheckAttendances.setStudyBegin(study);
//			}else{
//				return errorFtl(map,"还没有维护过下个学年学期，请先维护！");
//			}
			map.put("stuCheckAttendance", stuCheckAttendances);
		}else{
			map.put("stuCheckAttendance", stuCheckAttendance);
		}
		return "/studevelop/record/stuCheckAttendanceEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/checkAttendance/saveAllAtt")
	@ControllerInfo(value = "保存一批学生")
	public String dutySituationSave(StuCheckAttDto checkAttDto){
		try {
			List<StuCheckAttendance> checkAttList=checkAttDto.getCheckAttList();
			if(CollectionUtils.isNotEmpty(checkAttList)){
				String acadyear=checkAttList.get(0).getAcadyear();
				String semester=checkAttList.get(0).getSemester();
				Set<String> stuIds=checkAttList.stream().map(StuCheckAttendance::getStudentId).collect(Collectors.toSet());
				//删除之前的数据  不做更新操作
				stuCheckAttendanceService.deleteByStuIds(acadyear, semester, stuIds.toArray(new String[]{}));
				//保存
				stuCheckAttendanceService.saveAll(checkAttList.toArray(new StuCheckAttendance[]{}));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	@ResponseBody
	@RequestMapping("/checkAttendance/save")
    @ControllerInfo(value = "保存")
	public String dutySituationSave(StuCheckAttendance stuCheckAttendance){
		try {
			if(stuCheckAttendance.getId().isEmpty()){
				stuCheckAttendance.setId(UuidUtils.generateUuid());
			}
			stuCheckAttendanceService.save(stuCheckAttendance);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	/**  获取(当前学年学期的)下学期数据*/
	public Semester getNextSemeData(String acadyear, String semester){
		/*Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
		String acadYear = "";
		Integer semester = 1;
		// 当前为第一学期
		if("1".equals(semesterObj.getSemester())){
			acadYear = semesterObj.getAcadyear();
			semester = 2;
		}else{
			// 当前为第二学期
			String[] years = semesterObj.getAcadyear().split("-");
			if (!ArrayUtils.isEmpty(years) && years.length == 2) {
				int firYear = Integer.parseInt(years[0]);
				int secYear = Integer.parseInt(years[1]);
				acadYear = (firYear + 1) + "-" + (secYear + 1);
			} else {
				return null;
			}
		}
		Semester semester2 = SUtils.dc(semesterRemoteService.findByAcadYearAndSemester(acadYear, semester),Semester.class);
		if(semester2 == null || StringUtils.isBlank(semester2.getId())){
			return null;
		}
		return semester2;*/
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
		    Semester sem = SUtils.dc(semesterRemoteService.findByAcadYearAndSemester(acaStr, Integer.parseInt(semStr)), Semester.class);
		    return sem;
	}
}
