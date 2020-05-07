package net.zdsoft.tutor.data.action.teacher;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.tutor.data.constant.TutorConstants;
import net.zdsoft.tutor.data.dto.TutorAddTeacherDto;
import net.zdsoft.tutor.data.dto.TutorSetupDto;
import net.zdsoft.tutor.data.entity.TutorParam;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * @author yangsj  2017年9月11日下午5:03:09
 * 导师设置
 */
@Controller
@RequestMapping("/tutor/teacher/setUp")
public class TutorSetUpAction extends BaseAction {
	@Autowired
	private TutorRoundService tutorRoundService;
	@Autowired
	private TutorRoundTeacherService tutorRoundTeacherService;
	@Autowired
	private TutorRoundGradeService tutorRoundGradeService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private TutorResultService tutorResultService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private TutorParamService tutorParamService;
	@Autowired
	private TutorRecordService tutorRecordService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@RequestMapping("/showIndex")
	@ControllerInfo("页面的第一次跳转")
	public String showIndex(){
		return "/tutor/teacher/setup/setUpShowIndex.ftl";
	}
	
	@RequestMapping("/index")
	public String indexProject(ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		List<TutorRound> tutorRounds = tutorRoundService.findByUnitId(unitId);
		//查找当前轮次的导师
		List<TutorRoundTeacher> tutorRoundTeachers = tutorRoundTeacherService.findByUnitId(unitId);
		Map<String, List<TutorRoundTeacher>> trtMap = EntityUtils.getListMap(tutorRoundTeachers, TutorRoundTeacher::getRoundId,
				Function.identity());
		//当前满员的参数
		String param  = getMaxParam();
		//查找当前轮次的学生
		List<TutorRoundGrade> tutorRoundGrades = tutorRoundGradeService.findByUnitId(unitId);
		Map<String, List<TutorRoundGrade>> trgMap = EntityUtils.getListMap(tutorRoundGrades, TutorRoundGrade::getRoundId,
				Function.identity());
		//当前轮次已选的学生数
		List<TutorResult> tutorResults = tutorResultService.findByUnitId(unitId);
		Map<String, List<TutorResult>> trTidMap = EntityUtils.getListMap(tutorResults, TutorResult::getTeacherId, Function.identity());
		List<String> isHaveTutor = EntityUtils.getList(tutorResults, TutorResult::getStudentId);
		//过滤掉轮次id为空 的
		tutorResults = EntityUtils.filter2(tutorResults, t->{
    		return t.getRoundId() != null;
    	});
		Map<String, List<TutorResult>> trMap = EntityUtils.getListMap(tutorResults, TutorResult::getRoundId, Function.identity());
		List<TutorSetupDto>  listSetupDto = new ArrayList<TutorSetupDto>();
        for (TutorRound tutorRound : tutorRounds) {
        	String roundId = tutorRound.getId();
        	Integer selectedStuN = trMap.isEmpty() || CollectionUtils.isEmpty(trMap.get(roundId)) ? 0 : trMap.get(roundId).size();
        	TutorSetupDto tutorSetupDto = new TutorSetupDto();
        	//截取过长的roundName
        	tutorRound.setRoundTitleName(StringUtils.length(tutorRound.getRoundName()) > 10 ? tutorRound.getRoundName().substring(0, 10)+"..." : tutorRound.getRoundName());
        	tutorSetupDto.setTutorRound(tutorRound);
        	tutorSetupDto.setSelectedStuN(selectedStuN);
        	Integer canChooseTeaN;
        	if(!trtMap.isEmpty() && !CollectionUtils.isEmpty(trtMap.get(roundId))) {
        		List<String> teacherIds = EntityUtils.getList(trtMap.get(roundId), TutorRoundTeacher::getTeacherId);
        		List<String> canChoosTeacher = new ArrayList<String>() ;
        		for (String tid : teacherIds) {
        			if(CollectionUtils.isEmpty(trTidMap.get(tid)) || trTidMap.get(tid).size() < Integer.valueOf(param)) {
        				canChoosTeacher.add(tid);
        			}
        		}
        		canChooseTeaN = canChoosTeacher.size();
        	}else {
        		canChooseTeaN = 0;
        	}
        	tutorSetupDto.setCanChooseTeaN(canChooseTeaN);
        	//当前轮次年纪的学生总人数
        	List<TutorRoundGrade> trgList = trgMap.get(roundId);
        	List<String> gradeIds = EntityUtils.getList(trgList, TutorRoundGrade::getGradeId);
    		List<String> newList = new ArrayList<String>(new HashSet<String>(gradeIds)); 
    		List<Student> students = SUtils.dt(studentRemoteService.findByGradeIds(newList.toArray(new String[newList.size()])), Student.class);
        	List<String> studentIds = EntityUtils.getList(students, Student::getId);
        	studentIds.removeAll(isHaveTutor);
    		tutorSetupDto.setNoSelectedStuN(studentIds.size());
        	Date date = new Date();
        	int beginT = tutorRound.getBeginTime().compareTo(date) ;
        	int endT = tutorRound.getEndTime().compareTo(date);
        	int state = beginT == 1 ?1: (endT == -1?-1:0);
        	tutorSetupDto.setState(state);
        	listSetupDto.add(tutorSetupDto);
		}
        map.put("listSetupDto", listSetupDto);
		return "/tutor/teacher/setup/setUpIndex.ftl";
	}
	
	@ResponseBody
    @RequestMapping("/create")
    @ControllerInfo("创建导师项目")
	public String createProject(@RequestBody String project){
		try {
			TutorDetails tutorDetails = new TutorDetails(project);
			if(isAcrossTime(tutorDetails,null)) {
				TutorRound tutorRound = new TutorRound();
				tutorRound.setId(UuidUtils.generateUuid());
				tutorRound.setRoundName(tutorDetails.getRoundName());
				tutorRound.setBeginTime(tutorDetails.getBeginTime());
				tutorRound.setEndTime(tutorDetails.getEndTime());
				tutorRound.setUnitId(getLoginInfo().getUnitId());
				tutorRound.setCreatTime(new Date());
				tutorRoundService.save(tutorRound);
				//出现多选的情况
				saveTutorRoundGrade(tutorDetails, tutorRound);
			}else {
				return error("创建的时间有交叉，请重新创建");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error("创建失败");
		}
		return success("创建成功");
	}

	@ResponseBody
    @RequestMapping("/update")
    @ControllerInfo("修改导师项目")
	public String updateProject(String tutorRoundId,String roundName,@RequestBody String project){
		try {
			TutorRound tutorRound = tutorRoundService.findOne(tutorRoundId);
			if(StringUtils.isNotBlank(roundName)) {
				tutorRound.setRoundName(roundName);
			}else {
				TutorDetails tutorDetails = new TutorDetails(project);
				if(isAcrossTime(tutorDetails,tutorRoundId)) {
				tutorRound.setRoundName(tutorDetails.getRoundName());
				tutorRound.setBeginTime(tutorDetails.getBeginTime());
				tutorRound.setEndTime(tutorDetails.getEndTime());
				//根据tutorRoundId删除
				tutorRoundGradeService.deleteByRoundId(tutorRoundId);
				saveTutorRoundGrade(tutorDetails, tutorRound);
				}else {
					return error("更新的时间有交叉，请重新选择时间");
				}
			}
			tutorRoundService.save(tutorRound);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return error("更新失败");
		}
		return success("更新成功");
	}
	
	@ResponseBody
	@RequestMapping("/delete")
	@ControllerInfo("删除导师项目")
	public String deleteProject(String tutorRoundId){
		try {
			//关于这个批次的数据全部删除  tutor_result  tutor_round tutor_round_grade tutor_round_teacher tutor_record
			tutorRoundService.delete(tutorRoundId);
			tutorRoundGradeService.deleteByRoundId(tutorRoundId);
			tutorRoundTeacherService.deleteByRoundId(tutorRoundId);
			//得到所有的teacherId
			List<TutorResult> listTRT = tutorResultService.findByRoundId(tutorRoundId);
			List<String> studentIds = EntityUtils.getList(listTRT, TutorResult::getStudentId);
		    if(CollectionUtils.isNotEmpty(studentIds)) {
		    	tutorRecordService.deleteByStudentIds(studentIds);
		    }
			tutorResultService.deleteByRoundId(tutorRoundId);
		} catch (Exception e) {
			e.printStackTrace();
			return error("删除失败");
		}
		return success("删除成功");
	}
	
	@ResponseBody
	@RequestMapping("/addTutor")
	@ControllerInfo("添加导师")
	public String addTutor(String tutorRoundId,String teacherId){
		try {
			//查找是否存在，有就删除
			TutorRoundTeacher tutorRoundTeacher = tutorRoundTeacherService.findByRoundAndTeaId(tutorRoundId,teacherId);
			if(tutorRoundTeacher != null) {
				tutorRoundTeacherService.deleteByRoundAndTeaId(tutorRoundId,teacherId);
			}else {
				tutorRoundTeacher = new TutorRoundTeacher();
				tutorRoundTeacher.setId(UuidUtils.generateUuid());
				tutorRoundTeacher.setRoundId(tutorRoundId);
				tutorRoundTeacher.setTeacherId(teacherId);
				tutorRoundTeacher.setUnitId(getLoginInfo().getUnitId());
				tutorRoundTeacherService.save(tutorRoundTeacher);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return error("添加失败");
		}
		return success("添加成功");
	}
	
	@RequestMapping("/showTutorIndex")
	@ControllerInfo("展示导师列表")
	public String showTutorIndex(String tutorRoundId,ModelMap map){
		map.put("tutorRoundId", tutorRoundId);
		return "/tutor/teacher/setup/suAddTeacherIndex.ftl";
	}
	
	@RequestMapping("/showTutorList")
	@ControllerInfo("展示导师列表")
	public String showTutorList(HttpServletRequest request,String tutorRoundId,String teacherName,String isFull,ModelMap map){
			Pagination page = createPagination();
			Map<String, String> paramMap = syncParameters(request);
			int row = NumberUtils.toInt(paramMap.get("_pageSize"));
			if(row<=0){
				page.setPageSize(20);
			}
			String unitId = getLoginInfo().getUnitId();
		    //得出已经添加的导师
			List<TutorRoundTeacher> listTRT =tutorRoundTeacherService.findByRoundId(tutorRoundId);
			Map<String,TutorRoundTeacher> trtMap = EntityUtils.getMap(listTRT, TutorRoundTeacher::getTeacherId);
			String param  = getMaxParam();
			
			//得到轮次的结果 加上学段
//			List<TutorRoundGrade> tutorRoundGrade = tutorRoundGradeService.findByRoundId(tutorRoundId);
//			Grade grade = SUtils.dc(gradeRemoteService.findOneById(tutorRoundGrade.get(0).getGradeId()), Grade.class);
//			List<TutorResult> listTR = tutorResultService.findByUnitIdAndSection(unitId,grade.getSection());
			List<TutorResult> listTR = tutorResultService.findByUnitId(unitId);
			Set<String> teacherIds = EntityUtils.getSet(listTR, TutorResult::getTeacherId);
			Map<String, List<String>> trMap = EntityUtils.getListMap(listTR, TutorResult::getTeacherId, TutorResult::getStudentId);
			
			String[] notInTeacherIds = getNotInTeacherId(trMap,isFull,param);
			//得出本校所有的老师
			List<Teacher> teachers = Teacher.dt(teacherRemoteService.findByNameLikeIdNotIn(teacherName, getLoginInfo().getUnitId(), SUtils.s(page), notInTeacherIds),page);
			//得到所有的学生
			List<String> studentIds = EntityUtils.getList(listTR, TutorResult::getStudentId);
			List<Student> students =  SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[studentIds.size()])), Student.class);
			Map<String, Student> sMap = EntityUtils.getMap(students, Student::getId);
			//得到一个teacherId 和List<Student> 的map
			Map<String, List<Student>> stuMap = new HashMap<String, List<Student>>();
			for (String tid : teacherIds) {
				List<String> studentIds1 = trMap.get(tid);
				List<Student> studets = new ArrayList<Student>();
				for (String sid : studentIds1) {
					if(sMap.get(sid) != null) {
						studets.add(sMap.get(sid));
					}
				}
				stuMap.put(tid, studets);
			}
			List<TutorAddTeacherDto> listTATD = new ArrayList<TutorAddTeacherDto>();
			//遍历数据
			for (Teacher teacher : teachers) {
				Integer isChooseNum = trMap.isEmpty() ||  CollectionUtils.isEmpty(trMap.get(teacher.getId())) ? 0 :trMap.get(teacher.getId()).size();
				TutorAddTeacherDto tatd = new TutorAddTeacherDto();
				tatd.setIsChooseNum(isChooseNum);
				tatd.setParam(param);
				tatd.setTeacher(teacher);
				tatd.setIsFull("");
				List<Student> listStudent =  stuMap.isEmpty() || CollectionUtils.isEmpty(stuMap.get(teacher.getId())) ? null :stuMap.get(teacher.getId());
				tatd.setStudents(listStudent);
				if(!trtMap.isEmpty() && trtMap.get(teacher.getId()) != null) {
					tatd.setIsAdd("1");
				}else {
					tatd.setIsAdd("0");
				}
				listTATD.add(tatd);
			}
		map.put("listTATD", listTATD);
	    map.put("pagination", page);
	    map.put("tutorRoundId", tutorRoundId);
	    sendPagination(request, map, page);
		return "/tutor/teacher/setup/suAddTeacherList.ftl";
	}
	
	@RequestMapping("/save")
	public String saveProject(String tutorRoundId,ModelMap map,String isSee){
		TutorRound tutorRound = new TutorRound();
		if(StringUtils.isNotBlank(tutorRoundId)) {
			tutorRound = tutorRoundService.findOne(tutorRoundId);
			List<TutorRoundGrade> tutorRoundGrade = tutorRoundGradeService.findByRoundId(tutorRound.getId());
			Map<String, List<TutorRoundGrade>> trgMap = EntityUtils.getListMap(tutorRoundGrade, TutorRoundGrade::getRoundId, Function.identity());
			List<TutorRoundGrade> listTRG = trgMap.get(tutorRoundId);
			Map<String, TutorRoundGrade> trgMap1 = EntityUtils.getMap(listTRG, TutorRoundGrade::getGradeId);
			map.put("listTRG", listTRG);
			map.put("trgMap1", trgMap1);
		}
		Unit unit = SUtils.dc(unitRemoteService.findOneById(getLoginInfo().getUnitId()), Unit.class);
		List<Grade> lGrades = null;
		if(unit.getUnitClass() == Unit.UNIT_CLASS_SCHOOL) {
			lGrades = SUtils.dt(gradeRemoteService.findBySchoolId(getLoginInfo().getUnitId()),new TR<List<Grade>>() {});
		}
		map.put("isSee", isSee== null ?false : isSee.equals("true") );
		map.put("lGrades", lGrades);
		map.put("tutorRound", tutorRound);
		return "/tutor/teacher/setup/setUpSave.ftl";
	}
	
	public class TutorDetails { 
		private String radio;
		private String roundName;
		private Date beginTime;
		private Date endTime;
		public TutorDetails(String project){
			String roundName = null;
			String radio = null;
			Date beginTime = null;
			Date endTime = null;
			try {
				JSONObject jsonObject = SUtils.dc(project, JSONObject.class);    	
				roundName = jsonObject.getString("roundName");
				radio = jsonObject.getString("radio");
				beginTime = DateUtils.parseDate(jsonObject.getString("beginTime").trim(), "yyyy-MM-dd HH:mm");
				endTime = DateUtils.parseDate(jsonObject.getString("endTime").trim(), "yyyy-MM-dd HH:mm");
			} catch (ParseException e) {
				e.printStackTrace();
			}
	    	this.roundName = roundName;
			this.radio = radio;
			this.beginTime = beginTime;
			this.endTime = endTime;
		}
		public String getRadio() {
			return radio;
		}
		public void setRadio(String radio) {
			this.radio = radio;
		}
		public String getRoundName() {
			return roundName;
		}
		public void setRoundName(String roundName) {
			this.roundName = roundName;
		}
		public Date getBeginTime() {
			return beginTime;
		}
		public void setBeginTime(Date beginTime) {
			this.beginTime = beginTime;
		}
		public Date getEndTime() {
			return endTime;
		}
		public void setEndTime(Date endTime) {
			this.endTime = endTime;
		}
	}
	
	//判断是否有交叉时间
	private Boolean isAcrossTime(TutorDetails tutorDetails,String tutorRoundId) {
		Date beginTime = tutorDetails.getBeginTime();
		Date endTime = tutorDetails.getEndTime();
		String radios = tutorDetails.getRadio();
		List<String> gradeIds = new ArrayList<String>();
		if(radios.contains("[")) {
			gradeIds = SUtils.dt(radios,String.class);
		}else {
			gradeIds.add(radios); 
		}
		List<TutorRoundGrade> listTRG = tutorRoundGradeService.findbyGradeIdsAndUnitId(gradeIds.toArray(new String[gradeIds.size()]),getLoginInfo().getUnitId());
		List<String> roundIds = EntityUtils.getList(listTRG, TutorRoundGrade::getRoundId);
		if(StringUtils.isNotBlank(tutorRoundId)) 
			roundIds.remove(tutorRoundId);
		if(CollectionUtils.isNotEmpty(roundIds)) {
			List<TutorRound> listTR = tutorRoundService.findListByIds(roundIds.toArray(new String[roundIds.size()]));
			for (TutorRound tutorRound2 : listTR) {
				if(tutorRound2.getBeginTime().compareTo(beginTime) == 1) {
					if(tutorRound2.getBeginTime().compareTo(endTime) != 1) {
						return false;
					}
				}else {
					if(tutorRound2.getEndTime().compareTo(beginTime) != -1) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * @param tutorDetails
	 * @param tutorRound
	 */
	private void saveTutorRoundGrade(TutorDetails tutorDetails, TutorRound tutorRound) {
		String radios = tutorDetails.getRadio();
		List<String> jsonObjectList = new ArrayList<String>();
		List<TutorRoundGrade> trgList = new ArrayList<TutorRoundGrade>();
		if(radios.contains("[")) {
			jsonObjectList = SUtils.dt(radios,String.class);
		}else {
			jsonObjectList.add(radios); 
		}
		for (String gradeId : jsonObjectList) {
			if(StringUtils.isNotBlank(gradeId)) {
				TutorRoundGrade tutorRoundGrade = returnTRG(tutorRound, gradeId);
				trgList.add(tutorRoundGrade);
			}
		}
		tutorRoundGradeService.saveAll(EntityUtils.toArray(trgList,TutorRoundGrade.class));
	}

	/**
	 * @param tutorRound
	 * @param grade
	 * @return
	 */
	private TutorRoundGrade returnTRG(TutorRound tutorRound, String gradeId) {
		TutorRoundGrade tutorRoundGrade = new TutorRoundGrade();
		tutorRoundGrade.setId(UuidUtils.generateUuid());
		tutorRoundGrade.setRoundId(tutorRound.getId());
		tutorRoundGrade.setUnitId(getLoginInfo().getUnitId());
		tutorRoundGrade.setGradeId(gradeId);
		return tutorRoundGrade;
	}
	
	//得到不包含的teacherId
	private String[] getNotInTeacherId (Map<String, List<String>> trMap,String isFull,String param) {
		List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByUnitId(getLoginInfo().getUnitId()), Teacher.class);
		Set<String> tidList = EntityUtils.getSet(teachers, Teacher::getId);
		List<String> teacherIds = new ArrayList<String>();
		List<User> tUserList = SUtils.dt(userRemoteService.findByOwnerIds(tidList.toArray(new String[tidList.size()])), User.class);
		Map<String, User> ownerIdMap = EntityUtils.getMap(tUserList, User::getOwnerId);
		for (Teacher teacher : teachers) {
			Integer isChooseNum = trMap.isEmpty() ||  CollectionUtils.isEmpty(trMap.get(teacher.getId())) ? 0 :trMap.get(teacher.getId()).size();
			Boolean isFull1 = isChooseNum >= Integer.valueOf(param);
			if(StringUtils.isNotBlank(isFull)) {
				if(TutorConstants.TUTOR_TEACHER_IS_FULL.equals(isFull) && !isFull1) {
					teacherIds.add(teacher.getId());
				}else if(TutorConstants.TUTOR_TEACHER_ISNOT_FULL.equals(isFull) && isFull1) {
					teacherIds.add(teacher.getId());
				}
			}
			if(ownerIdMap.get(teacher.getId()).getUserState() != User.USER_MARK_NORMAL){
				teacherIds.add(teacher.getId());
			}
		}
		return teacherIds.toArray(new String[teacherIds.size()]);
	}
	
	private String getMaxParam() {
		TutorParam tutorParam =tutorParamService.findByUnitIdAndPtype(getLoginInfo().getUnitId(),TutorConstants.TUTOR_STUENT_MAX_NUMBER);
		String param  = (tutorParam ==null? "0" : tutorParam.getParam());
		return param;
	}
}
