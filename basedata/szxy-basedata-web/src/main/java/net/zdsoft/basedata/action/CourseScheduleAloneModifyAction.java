package net.zdsoft.basedata.action;

import net.zdsoft.basedata.constant.TipsayConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 课表调整。管理员老师的 单独调整模块
 * @author wangyy
 *
 */
@RequestMapping("/basedata/scheduleModify/")
@Controller
public class CourseScheduleAloneModifyAction extends BaseAction{
	@Autowired
	private GradeService gradeService;
	@Autowired
	private ClassService classService;
	@Autowired
	private CourseScheduleService courseScheduleService;
	@Autowired
	private TeachClassService teachClassService;
	@Autowired
	private SemesterService semesterService;
	@Autowired
	private DateInfoService dateInfoService;
	@Autowired
	private CustomRoleService customRoleService;
	@Autowired
	private CourseScheduleRevertService courseScheduleRevertService;
	@Autowired
	private SchoolCalendarService schoolCalendarService;
	
	
	public static final int WEEK_TYPE_ODD = 1; //单周
	public static final int WEEK_TYPE_EVEN = 2; //双周
	public static final int WEEK_TYPE_NORMAL = 3; //正常
	
	@RequestMapping("/alone/index/page")
	public String aloneModifyIndex(ModelMap map) {
		String unitId = getLoginInfo().getUnitId();

		if (!isAdmin(unitId, getLoginInfo().getUserId())) {
			map.put("msg", "对不起！暂时无权限访问<br>请联系管理员");
			return "/basedata/courseSchedule/limit.ftl";
		}
		List<String> acadyearList = semesterService.findAcadeyearList();
		if(CollectionUtils.isEmpty(acadyearList)){
			map.put("msg", "学年学期不存在");
			return "/basedata/courseSchedule/limit.ftl";
		}
		Semester semester = semesterService.findCurrentSemester(2, unitId);
		map.put("acadyearList", acadyearList);
		map.put("semester", semester);
		
		Map<String, Integer> cur2Max =schoolCalendarService.findCurrentWeekAndMaxWeek(semester.getAcadyear(), String.valueOf(semester.getSemester()), unitId);
        Integer nowWeek = cur2Max.get("current");
        Integer max = cur2Max.get("max");
        if(nowWeek==null){
        	nowWeek=max;
        }
        map.put("nowWeek", nowWeek);
        map.put("max", max);

		List<Grade> gradeList = gradeService.findByUnitId(unitId);
		map.put("gradeList", gradeList);
		map.put("schoolId", unitId);

		return "/basedata/courseSchedule/modify/mutil/aloneModifyIndex.ftl";
	}
	
	 /**
     * 判断是否有调课管理权限
     */
    private boolean isAdmin(String unitId,String userId) {
        return customRoleService.checkUserRole(unitId, TipsayConstants.SUBSYSTEM_86, TipsayConstants.EDUCATION_CODE, userId);
    }
    
    /**
     * 备份年级课表
     * @param gradeId
     * @return
     */
    @RequestMapping("/{gradeId}/backupSchedule")
    @ResponseBody
    public String backupSchedule(@PathVariable String gradeId, String acadyear, String semester) {
    	if(StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)) {
    		return error("请先设置学年学期");
    	}
    	gradeId = null;
    	String unitId = getLoginInfo().getUnitId();
		// 获取年级 课表
    	List<String> classIdList = new ArrayList<>();
//    	List<Clazz> clazzList = classService.findByGradeId(gradeId);
		List<Clazz> clazzList = classService.findBySchoolId(unitId);
		List<TeachClass> teachClaList = teachClassService.findBySearch(unitId, acadyear, semester, null);
		Map<String,String> cIdGradeIdMap = new HashMap<>();
		clazzList.forEach(e->{
			cIdGradeIdMap.put(e.getId(),e.getGradeId());
			classIdList.add(e.getId());
		});
		teachClaList.forEach(e->{
			cIdGradeIdMap.put(e.getId(),e.getGradeId());
			classIdList.add(e.getId());
		});
//    	classIdList.addAll(EntityUtils.getSet(clazzList, Clazz::getId));
//    	classIdList.addAll(EntityUtils.getSet(teachClaList, TeachClass::getId));
    	
    	List<CourseSchedule> courseScheduleList = null;
    	if(CollectionUtils.isNotEmpty(classIdList))
    		courseScheduleList = courseScheduleService.findCourseScheduleListByClassIdes(acadyear, Integer.parseInt(semester), 
    				classIdList.toArray(new String[0]), null, "1");
    	List<CourseScheduleRevert> savedList = EntityUtils.copyProperties(courseScheduleList, CourseSchedule.class, CourseScheduleRevert.class);
    	Date now = new Date();
    	savedList.forEach(e->{
    		e.setId(UuidUtils.generateUuid());
    		e.setGradeId(cIdGradeIdMap.get(e.getClassId()));
//    		e.setCreationTime(now);
    		e.setModifyTime(now);
    	});
    	
    	if(CollectionUtils.isEmpty(savedList)) {
    		return error("没有数据需要备份");
    	}
    	
    	// 写入之前 先删除上一次的保存结果
    	try {
    		// 隐患,如果某班级被删了 那么临时表的课表数据就永远不会被删除了；加了gradeId 已解决
			courseScheduleRevertService.saveBackup(unitId,acadyear, Integer.parseInt(semester),null,savedList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(""+e.getMessage());
		}
    	
    	return returnSuccess();
    }
    
    /**
     * 检查备份课表是否存在
     * @param gradeId
     * @param acadyear
     * @param semester
     * @return
     */
    @RequestMapping("/{gradeId}/checkBackupExists")
    @ResponseBody
    public String checkBackupExists(@PathVariable String gradeId, String acadyear, String semester) {
    	if(StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)) {
    		return error("请先设置学年学期");
    	}
    	gradeId = null;
    	String unitId = getLoginInfo().getUnitId();
		List<Grade> gradeList = gradeService.findBySchoolIdAndIsGraduate(unitId, 0);
		Set<String> gradeIds = EntityUtils.getSet(gradeList, e -> e.getId());

		try {
			boolean exists = courseScheduleRevertService.checkBackupExists(unitId,acadyear,Integer.parseInt(semester),gradeIds.toArray(new String[0]));
			if(exists) {
				return returnSuccess();
			}
			else {
				return error("没有备份数据");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(""+e.getMessage());
		}
    }
    
    /**
     * 还原年级课表
     * @param gradeId
     * @return
     */
    @RequestMapping("/{gradeId}/revertSchedule")
    @ResponseBody
    public String revertSchedule(@PathVariable String gradeId, String acadyear, String semester) {
    	if(StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)) {
    		return error("请先设置学年学期");
    	}
		gradeId = null;

    	String unitId = getLoginInfo().getUnitId();
    	// 从备份中还原课表
		// 获取年级 课表
    	List<String> classIdList = new ArrayList<>();
		List<Clazz> clazzList = classService.findBySchoolId(unitId);
		List<TeachClass> teachClaList = teachClassService.findBySearch(unitId, acadyear, semester, null);
    	classIdList.addAll(EntityUtils.getSet(clazzList, Clazz::getId));
    	classIdList.addAll(EntityUtils.getSet(teachClaList, TeachClass::getId));
    	
    	List<CourseScheduleRevert> revertList = courseScheduleRevertService.findByClassIds(unitId,acadyear, Integer.parseInt(semester), classIdList.toArray(new String[0]));
    	List<CourseSchedule> savedList = EntityUtils.copyProperties(revertList, CourseScheduleRevert.class, CourseSchedule.class);
    	Date now = new Date();
    	savedList.forEach(e->{
    		e.setId(UuidUtils.generateUuid());
//    		e.setCreationTime(now);
    		e.setModifyTime(now);
    	});
    	
    	// 删除 现年级课程 并且保存备份的课程；检查班级是否存在
    	try {
			courseScheduleService.saveRevertSchedule(unitId,acadyear, semester,classIdList,savedList);
		} catch (Exception e1) {
			e1.printStackTrace();
			return error(""+e1.getMessage());
		}
    	
    	return returnSuccess();
    }
}
