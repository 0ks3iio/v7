package net.zdsoft.comprehensive.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.zdsoft.comprehensive.dto.CompreScoreMapDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.comprehensive.constant.CompreStatisticsConstants;
import net.zdsoft.comprehensive.entity.CompreInfo;
import net.zdsoft.comprehensive.entity.CompreScore;
import net.zdsoft.comprehensive.service.CompreScoreService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
/**
 * 
 * 暂时用不到
 *
 */
@Controller
@RequestMapping("/comprehensive")
public class CompreScoreManagerAction extends BaseAction {

	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private CompreScoreService compreScoreService;
	@Autowired
	private CourseRemoteService courseRemoteService;

	@RequestMapping("/exam/first/index/page")
	@ControllerInfo(value = "学考原始成绩管理")
	public String showFirstIndex(HttpSession httpSession, ModelMap map) {
		return "/comprehensive/exam/examFirstIndex.ftl";
	}

	@RequestMapping("/exam/first/list")
	@ControllerInfo(value = "学考原始成绩查询")
	public String searchFirstList(String gradeId, String classId,
			HttpServletRequest request, ModelMap map) {
		//查询当前学期
		Semester se = SUtils.dc(
				semesterRemoteService.getCurrentSemester(2), Semester.class);
		//根据登录用户信息查询所有年级
		List<Grade> gradeList = SUtils.dt(gradeRemoteService
				.findBySchoolId(getLoginInfo().getUnitId()),
				new TR<List<Grade>>() {
		});
		Pagination page=createPagination();
		if(CollectionUtils.isEmpty(gradeList)){
			sendPagination(request, map, page);
			return "/comprehensive/exam/examFirstList.ftl";
		}
		

		if(StringUtils.isBlank(gradeId)) 
					gradeId=gradeList.get(0).getId();
		//根据年级id查询所有班级
		List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(getLoginInfo().getUnitId(),
						gradeId), new TR<List<Clazz>>(){});
		//查科目
		List<Course> courseList=SUtils.dt(courseRemoteService.findByCodes73(this.getLoginInfo().getUnitId()),new TR<List<Course>>(){});
		if(courseList != null)
			courseList.addAll(SUtils.dt(courseRemoteService.findByCodesYSY(this.getLoginInfo().getUnitId()),new TR<List<Course>>(){}));
		
		String[] classIds =null;
		if(StringUtils.isNotBlank(classId)){
			classIds=new String[]{classId};
		}else if(CollectionUtils.isNotEmpty(classList)){
			classIds = EntityUtils.getList(classList, "id").toArray(new String[0]);
		}
		
		List<CompreScoreMapDto> scoreList=compreScoreService.getFirstOrFinallyList(getLoginInfo().getUnitId(),
								"", "", classIds,true,page);
		
		map.put("scoreList", scoreList);
		map.put("gradeId", gradeId);
		map.put("classId", classId);
		map.put("gradeList", gradeList);
		map.put("classList", classList);
		map.put("courseList", courseList);
		sendPagination(request, map, page);
		return "/comprehensive/exam/examFirstList.ftl";
	}
	@RequestMapping("/exam/first/synFirst")
	@ResponseBody
	public String synFirst(ModelMap map,String gradeId){
		try{
			if(StringUtils.isBlank(gradeId)){
				return error("年级不能为空");
			}
			Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);

			compreScoreService.saveSynFirst(getLoginInfo().getUnitId(), se.getAcadyear(), se.getSemester()+"", 
					gradeId, CompreStatisticsConstants.TYPE_XK);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	@RequestMapping("/exam/first/saveAll")
	@ResponseBody
	public String saveAll(ModelMap map,CompreInfo compreInfo,String gradeId,String classId){
		try{
			
			Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
//			compreScoreService.saveAll(getLoginInfo().getUnitId(), se.getAcadyear(), se.getSemester()+"", 
//								gradeId,classId, getLoginInfo().getUserId(), compreInfo.getScoreList());
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@RequestMapping("/exam/finally/saveFinally")
	@ResponseBody
	public String convertFirst(ModelMap map,String gradeId){
		try{
			Semester se = SUtils.dc(
					semesterRemoteService.getCurrentSemester(2), Semester.class);
			
			compreScoreService.saveConvertFirst(getLoginInfo().getUnitId(), se.getAcadyear(), se.getSemester()+"", 
					gradeId, getLoginInfo().getUserId());
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}

	@RequestMapping("/exam/finally/index/page")
	@ControllerInfo(value = "学考换算成绩管理")
	public String showFinallyIndex(HttpSession httpSession, ModelMap map) {
		compreScoreService.updateParamInfoList(getLoginInfo().getUnitId(),CompreStatisticsConstants.INFO_KEY_5);;
		return "/comprehensive/exam/examFinallyIndex.ftl";
	}
	@RequestMapping("/exam/finally/list")
	@ControllerInfo(value = "学考换算成绩查询")
	public String searchFinallyList(String gradeId, String classId,
			HttpServletRequest request, ModelMap map) {
		//查询当前学期
		Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
		//根据登录用户信息查询所有年级
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(getLoginInfo().getUnitId()),
				new TR<List<Grade>>(){});
		Pagination page=createPagination();
		if(CollectionUtils.isEmpty(gradeList)){
			sendPagination(request, map, page);
			return "/comprehensive/exam/examFinallyList.ftl";
		}
		if(StringUtils.isBlank(gradeId)) gradeId=gradeList.get(0).getId();
		//根据年级id查询所有班级
		List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(getLoginInfo().getUnitId(),
									gradeId), new TR<List<Clazz>>(){});
		//查科目
		List<Course> courseList=SUtils.dt(courseRemoteService.findByCodes73(this.getLoginInfo().getUnitId()),new TR<List<Course>>(){});
		if(courseList != null)
			courseList.addAll(SUtils.dt(courseRemoteService.findByCodesYSY(this.getLoginInfo().getUnitId()),new TR<List<Course>>(){}));
		
		String[] classIds={classId};
		if(StringUtils.isBlank(classId)){
			classIds = EntityUtils.getList(classList, "id").toArray(new String[0]);
		}
		
		List<CompreScoreMapDto> scoreList=compreScoreService.getFirstOrFinallyList(getLoginInfo().getUnitId(),
									 "", "", classIds,false,page);
		
		map.put("scoreList", scoreList);
		map.put("gradeId", gradeId);
		map.put("classId", classId);
		map.put("gradeList", gradeList);
		map.put("classList", classList);
		map.put("courseList", courseList);
		sendPagination(request, map, page);
		return "/comprehensive/exam/examFinallyList.ftl";
	}
}
