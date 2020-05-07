package net.zdsoft.tutor.data.action.student;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.tutor.data.constant.TutorConstants;
import net.zdsoft.tutor.data.dto.TutorAddTeacherDto;
import net.zdsoft.tutor.data.entity.TutorParam;
import net.zdsoft.tutor.data.entity.TutorRecord;
import net.zdsoft.tutor.data.entity.TutorResult;
import net.zdsoft.tutor.data.entity.TutorRound;
import net.zdsoft.tutor.data.entity.TutorRoundGrade;
import net.zdsoft.tutor.data.entity.TutorRoundTeacher;
import net.zdsoft.tutor.data.service.TutorParamService;
import net.zdsoft.tutor.data.service.TutorRecordService;
import net.zdsoft.tutor.data.service.TutorResultService;
import net.zdsoft.tutor.data.service.TutorRoundGradeService;
import net.zdsoft.tutor.data.service.TutorRoundService;
import net.zdsoft.tutor.data.service.TutorRoundTeacherService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yangsj  2017年9月19日上午9:30:07
 */
@Controller
@RequestMapping("/tutor/student")
public class TutorStudentAction extends BaseAction{
	@Autowired
	private TutorResultService tutorResultService;
	@Autowired
	private TutorRecordService tutorRecordService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired 
	private UserRemoteService userRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private TutorParamService tutorParamService;
	@Autowired
	private TutorRoundService tutorRoundService;
	@Autowired
	private TutorRoundTeacherService tutorRoundTeacherService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private TutorRoundGradeService tutorRoundGradeService;
	@RequestMapping("/index")
	@ControllerInfo("展示学生的导师管理")
	public String manageIndex(ModelMap map){
		User user = SUtils.dc(userRemoteService.findOneById(getLoginInfo().getUserId(), true),User.class);
		String studentId = user.getOwnerId();
		//当前的学年学期
    	Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1, getLoginInfo().getUnitId()), Semester.class);
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        //查看我的导师
//        Clazz clazz = SUtils.dc(classRemoteService.findOneById(user.getClassId()), Clazz.class);
//        TutorResult tutorResult = tutorResultService.findByStudentIdAndSection(studentId,clazz.getSection());
        TutorResult tutorResult = tutorResultService.findByStudentId(studentId);
        TutorAddTeacherDto tatd =new TutorAddTeacherDto();
        boolean isDel = false;
        if(tutorResult != null) {
        	String param  = getMaxParam();
        	List<TutorResult> listTR = tutorResultService.findByTeacherId(tutorResult.getTeacherId());
        	Integer isChooseNum = listTR.size();
			Teacher teacher = SUtils.dc(teacherRemoteService.findOneById(tutorResult.getTeacherId()), Teacher.class);
			tatd.setParam(param);
			tatd.setIsChooseNum(isChooseNum);
			tatd.setIsFull("");
			tatd.setTeacher(teacher);
			map.put("tatd", tatd);
        }else {
        	List<TutorResult> tutorResultDels = tutorResultService.findByStudentIdDel(studentId);
//        	List<TutorResult> tutorResultDels = tutorResultService.findByStudentIdDelAndSection(studentId,clazz.getSection());
        	if(CollectionUtils.isNotEmpty(tutorResultDels)){
        		isDel = true;
        	}
        }
        map.put("isDel", isDel); 
        map.put("semester", semester); 
		map.put("acadyearList", acadyearList);
        map.put("studentId", studentId);
		return "/tutor/student/manageIndex.ftl";
	}
	
	@RequestMapping("/showRecordList")
    @ControllerInfo("查看学生的导师记录")
	public String showRecordList(ModelMap map,String acadyear,String semester,String studentId){
    	//当前的学年学期
    	Semester semester1 = SUtils.dc(semesterRemoteService.getCurrentSemester(1, getLoginInfo().getUnitId()), Semester.class);
    	if(StringUtils.isBlank(semester) && StringUtils.isBlank(semester)) {
    		acadyear = semester1.getAcadyear();
    		semester = String.valueOf(semester1.getSemester());
    	}
		List<TutorRecord> listTR = tutorRecordService.findBySIdAndSemester(studentId,acadyear,semester);
		for (TutorRecord tutorRecord : listTR) {
			if(!tutorRecord.getUnitId().equals(getLoginInfo().getUnitId())) {
				listTR.remove(tutorRecord);
			}
		}
		if(CollectionUtils.isNotEmpty(listTR)) {
			String teacherId = listTR.get(0).getTeacherId();
			Teacher teacher = SUtils.dc(teacherRemoteService.findOneById(teacherId), Teacher.class);
			//得到老师的姓名
			for (TutorRecord tutorRecord : listTR) {
				tutorRecord.setTeacherName(teacher.getTeacherName());
			}
		}
		Map<String, List<TutorRecord>> trMap = EntityUtils.getListMap(listTR, TutorRecord::getRecordType, Function.identity());
		map.put("trMap", trMap);
    	return "/tutor/tutorrecord/showRecordList.ftl";
	}
	
	@RequestMapping("/showTutorList")
    @ControllerInfo("查看学生的导师记录")
	public String showTutorList(String studentId, ModelMap map){
		List<TutorAddTeacherDto> listTATD = new ArrayList<TutorAddTeacherDto>();
		String tutorRoundId = null;
		//得到当前学生的年级
		Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
		String classId = student.getClassId();
		Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
		String gradeId = clazz.getGradeId();
		boolean noRound = false;
		TutorRound tutorRound1 = null;
		try {
			Date date = new Date();
			String unitId = getLoginInfo().getUnitId();
			List<TutorRoundGrade> listTRG = tutorRoundGradeService.findbyGradeIdsAndUnitId(ArrayUtils.toArray(gradeId),getLoginInfo().getUnitId());
			Set<String> tutorRoundIds = EntityUtils.getSet(listTRG, TutorRoundGrade::getRoundId);
			List<TutorRound> tutorRounds = tutorRoundService.findListByIds(tutorRoundIds.toArray(new String[tutorRoundIds.size()]));
//			List<TutorRound> tutorRounds = tutorRoundService.findByUnitId(unitId);
			for (TutorRound tutorRound : tutorRounds) {
				int beginT = tutorRound.getBeginTime().compareTo(date) ;
				int endT = tutorRound.getEndTime().compareTo(date);
				int state = beginT == 1 ?1: (endT == -1?-1:0);
				if(state == 0) {
					tutorRound1 = tutorRound;
					break;
				}
			}		
			if(tutorRound1 != null) {
				 tutorRoundId = tutorRound1.getId();
				//当前轮次所有的老师
				 List<TutorRoundTeacher> tutorRoundTeachers = tutorRoundTeacherService.findByRoundId(tutorRoundId);
				 List<String> teacherIds = EntityUtils.getList(tutorRoundTeachers, TutorRoundTeacher::getTeacherId);
				//所有的老师结果
				List<TutorResult> tutorResults1 = tutorResultService.findByUnitId(unitId);
				Map<String, List<TutorResult>> trMap = EntityUtils.getListMap(tutorResults1, TutorResult::getTeacherId, Function.identity());
				//得到所有的老师
				List<Teacher> teachers = SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[teacherIds.size()])), Teacher.class);
				Map<String,Teacher> tMap = EntityUtils.getMap(teachers, Teacher::getId);
				//当前满员的参数
				String param  = getMaxParam();
				//查找当前轮次的学生
			    for (String teacherId : teacherIds) {
			    	TutorAddTeacherDto tatd = new TutorAddTeacherDto();
			    	tatd.setParam(param);
			    	Integer selectedStuN = trMap.isEmpty() || CollectionUtils.isEmpty(trMap.get(teacherId)) ? 0 : trMap.get(teacherId).size();
			    	tatd.setIsChooseNum(selectedStuN);
			    	tatd.setIsFull("");
			    	tatd.setTeacher(tMap.get(teacherId));
			    	if(StringUtils.isNotBlank(teacherId)&&!(teacherId.equals(clazz.getTeacherId()))){
			    		listTATD.add(tatd);
			    	}
				}
			}else {
				noRound = true;
				map.put("messageEmpty", "暂无轮次进行中");
			}
			TutorResult tutorResult = tutorResultService.findByStudentId(studentId);
			map.put("tutorResult", tutorResult);
			map.put("tutorRoundId", tutorRoundId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return error("查看学生的导师记录出错");
		}
		map.put("noRound", noRound);
		map.put("tutorRound", tutorRound1);
		map.put("listTATD", listTATD);
    	return "/tutor/student/showTutorList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/chooseTutor")
	@ControllerInfo("选择导师")
	public String chooseTutor(String studentId,String teacherId,String tutorRoundId){
		if(RedisUtils.hasLocked(studentId)){
			try {
				String param  = getMaxParam(); 
//				Clazz clazz = SUtils.dc(classRemoteService.findOneById(getLoginInfo().getClassId()), Clazz.class);
//		        TutorResult tutorResult = tutorResultService.findByStudentIdAndSection(studentId,clazz.getSection());
				TutorResult tutorResult = tutorResultService.findByStudentId(studentId);
				if(tutorResult == null ) {
					TutorResult tutorResult1 = new TutorResult();
					tutorResult1.setId(UuidUtils.generateUuid());
					tutorResult1.setRoundId(tutorRoundId);
					tutorResult1.setUnitId(getLoginInfo().getUnitId());
					tutorResult1.setState(TutorResult.STATE_NORMAL);
					tutorResult1.setTeacherId(teacherId);
					tutorResult1.setStudentId(studentId);
//					tutorResult1.setSection(clazz.getSection());  //添加学段
					tutorResult1.setCreationTime(new Date());
					tutorResult1.setModifyTime(new Date());
					String returnStr = "";
					returnStr = tutorResultService.save(tutorResult1,param,teacherId);
					if(returnStr.equals("error")) {
						return error("导师人员已满，请选择其他导师");
					}else if(returnStr.equals("retry")) {
						return error("请重新选择");
					}
				}else {
					tutorResult.setTeacherId(teacherId);
					tutorResult.setModifyTime(new Date());
					String returnStr = "";
					returnStr = tutorResultService.save(tutorResult,param,teacherId);
					if(returnStr.equals("error")) {
						return error("导师人员已满，请选择其他导师");
					}else if(returnStr.equals("retry")) {
						return error("请重新选择");
					}
					List<TutorRecord> listTR = tutorRecordService.findByStudentIds(studentId);
					if(CollectionUtils.isNotEmpty(listTR)) {
						for (TutorRecord tutorRecord : listTR) {
							tutorRecord.setTeacherId(teacherId);
						}
						tutorRecordService.saveAll(EntityUtils.toArray(listTR,TutorRecord.class));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return error("选择导师失败");
			}finally{
				RedisUtils.unLock(studentId);
			}
		}
		return success("选择导师成功");
	}

	/**
	 * @param teacherId
	 * @param param
	 */
	protected String tutorIsFull(String teacherId) {
		//当前满员的参数
		String param = getMaxParam(); 
		List<TutorResult> tutorResults1 = tutorResultService.findByTeacherId(teacherId);
		if(tutorResults1.size()>= Integer.valueOf(param)) {
			return TutorConstants.TUTOR_TEACHER_IS_FULL;
		}else {
			return TutorConstants.TUTOR_TEACHER_ISNOT_FULL;
		}
	}

	/**
	 * @return
	 */
	private String getMaxParam() {
		TutorParam tutorParam =tutorParamService.findByUnitIdAndPtype(getLoginInfo().getUnitId(),TutorConstants.TUTOR_STUENT_MAX_NUMBER);
		String param  = (tutorParam ==null? "0" : tutorParam.getParam());
		return param;
	}
}
