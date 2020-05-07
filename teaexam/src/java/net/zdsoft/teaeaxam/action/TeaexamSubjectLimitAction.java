package net.zdsoft.teaeaxam.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.teaeaxam.constant.TeaexamConstant;
import net.zdsoft.teaeaxam.dto.TeaexamTeacherDto;
import net.zdsoft.teaeaxam.entity.TeaexamInfo;
import net.zdsoft.teaeaxam.entity.TeaexamSubject;
import net.zdsoft.teaeaxam.entity.TeaexamSubjectLimit;
import net.zdsoft.teaeaxam.service.TeaexamInfoService;
import net.zdsoft.teaeaxam.service.TeaexamSubjectLimitService;
import net.zdsoft.teaeaxam.service.TeaexamSubjectService;
@Controller
@RequestMapping("/teaexam")
public class TeaexamSubjectLimitAction extends BaseAction{
	@Autowired
	private TeaexamSubjectLimitService teaexamSubjectLimitService;
	@Autowired
	private TeaexamInfoService teaexamInfoService;
	@Autowired
	private TeaexamSubjectService teaexamSubjectService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;

	@RequestMapping("/subjectLimit/index/page")
	public String pageIndex(String examId, ModelMap map, HttpServletRequest req){
		LoginInfo info = getLoginInfo();
		String unitId = info.getUnitId();
		int year = NumberUtils.toInt(req.getParameter("year")); 
		int type = NumberUtils.toInt(req.getParameter("type"));;
		Calendar now = Calendar.getInstance();
		int nowy = now.get(Calendar.YEAR); 
		if(year == 0) {
			year = nowy;
		}
		map.put("maxYear", nowy+1);
		map.put("minYear", nowy-5);
		map.put("year", year);
		map.put("type", type);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(new Date());
		List<TeaexamInfo> examList = teaexamInfoService.findByEndTime(year, type, dateString, unitId);
		map.put("examList", examList);
		if(StringUtils.isBlank(examId) && CollectionUtils.isNotEmpty(examList)){
			examId = examList.get(0).getId();
		}
		if(CollectionUtils.isEmpty(examList)){
			examId = "";
		}
		if(StringUtils.isNotBlank(examId)){
			List<TeaexamSubject> subList = teaexamSubjectService.findByExamIds(new String[]{examId});
			map.put("subList", subList);
		}
		List<TeaexamSubjectLimit> limitList = teaexamSubjectLimitService.limitList(new String[]{examId,TeaexamConstant.ALL_LIMIT});
		List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>(){});
		Map<String, String> teacherMap = new HashMap<String, String>();
		for(Teacher teacher : teacherList){
			teacherMap.put(teacher.getId(), teacher.getTeacherName());
		}
		for(TeaexamSubjectLimit limit : limitList){
			if(StringUtils.isNotBlank(limit.getTeacherIds())){
				String[] arr = limit.getTeacherIds().split(",");
				List<TeaexamTeacherDto> teaDto = new ArrayList<TeaexamTeacherDto>();
				for(String teacherId : arr){
					TeaexamTeacherDto dto = new TeaexamTeacherDto();
					dto.setTeacherName(teacherMap.get(teacherId));
					dto.setTeacherId(teacherId);
					if(null!=teacherMap.get(teacherId)){
						teaDto.add(dto);
					}
				}
				limit.setTeacherDtoList(teaDto);
			}
		}
		map.put("limitList", limitList);
		map.put("examId", examId);
		return "/teaexam/subjectLimit/subjectLimit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/subjectLimit/saveLimit")
    @ControllerInfo(value = "")
	public String saveTeachers(String examId, String subjectId, String teacherIds){
		try{
			teaexamSubjectLimitService.saveLimit(examId, subjectId, teacherIds);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
	
	@ResponseBody
	@RequestMapping("/subjectLimit/getTeacherIds")
	public String getTeacherIds(String examId, String subjectId){
		if(StringUtils.isBlank(subjectId)){
			subjectId = TeaexamConstant.ALL_LIMIT;
			examId = TeaexamConstant.ALL_LIMIT;
		}
		TeaexamSubjectLimit limit = teaexamSubjectLimitService.findByExamIdAndSubId(examId, subjectId);
		String teacherIds = "";
		if(null!=limit){
			teacherIds = limit.getTeacherIds();
		}
		return teacherIds;
	}
	
	@ResponseBody
	@RequestMapping("/subjectLimit/deleteTeacher")
	public String deleteTeacher(String examId, String subjectId, String teacherId){
		try{
			TeaexamSubjectLimit limit = teaexamSubjectLimitService.findByExamIdAndSubId(examId, subjectId);
			if(null!=limit){
				String[] arr = limit.getTeacherIds().split(",");
				String newTeacherId = "";
				for(String teaId : arr){
					if(!teaId.equals(teacherId)){
						newTeacherId = newTeacherId + teaId + ",";
					}
				}
				limit.setTeacherIds(newTeacherId);
				teaexamSubjectLimitService.save(limit);
			}
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
}
