package net.zdsoft.tutor.data.action.teacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ServletUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.tutor.data.constant.TutorConstants;
import net.zdsoft.tutor.data.dto.TutorStuSearchDto;
import net.zdsoft.tutor.data.dto.TutorStuTeaDto;
import net.zdsoft.tutor.data.dto.TutorTeaStuDto;
import net.zdsoft.tutor.data.entity.TutorParam;
import net.zdsoft.tutor.data.entity.TutorResult;
import net.zdsoft.tutor.data.entity.TutorRound;
import net.zdsoft.tutor.data.entity.TutorRoundGrade;
import net.zdsoft.tutor.data.entity.TutorRoundTeacher;
import net.zdsoft.tutor.data.service.TutorParamService;
import net.zdsoft.tutor.data.service.TutorResultService;
import net.zdsoft.tutor.data.service.TutorRoundGradeService;
import net.zdsoft.tutor.data.service.TutorRoundService;
import net.zdsoft.tutor.data.service.TutorRoundTeacherService;

@Controller
@RequestMapping("/tutor/result")
public class TutorResultAction extends BaseAction {
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private TutorParamService tutorParamService;
	@Autowired
	private TutorResultService tsutorResultService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private TutorRoundService tutorRoundService;
	@Autowired
	private TutorRoundTeacherService tutorRoundTeacherService;
	@Autowired
	private TutorRoundGradeService tutorRoundGradeService;
	
	@RequestMapping("/tab/page")
	public String resultTabPage(ModelMap map){
		return "/tutor/teacher/defend/tutorResultManageIndex.ftl";
	}
	
	@RequestMapping("/teacher/head")
	public String resultTeaHead(ModelMap map){
		List<TutorRound> tutorRounds = tutorRoundService.findByUnitId(getLoginInfo().getUnitId());
		map.put("tutorList", tutorRounds);
		return "/tutor/teacher/defend/tutorResultTeacherTab.ftl";
	}
	
	@RequestMapping("/teacher/param/edit")
	public String resultTeaPaeamSet(ModelMap map){
		TutorParam tutorParam = tutorParamService.findByUnitIdAndPtype(getLoginInfo().getUnitId(), TutorConstants.TUTOR_STUENT_MAX_NUMBER);
		map.put("maxStuNum", tutorParam==null?0:Integer.valueOf(tutorParam.getParam()));
		return "/tutor/teacher/defend/tutorResultMaxNumSet.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/teacher/param/save")
	public String resultTeaParamSave(String param,ModelMap map){
		try{
			TutorParam tutorParam = tutorParamService.findByUnitIdAndPtype(getLoginInfo().getUnitId(), TutorConstants.TUTOR_STUENT_MAX_NUMBER);
			if(tutorParam==null){
				tutorParam = new TutorParam();
				tutorParam.setId(UuidUtils.generateUuid());
				tutorParam.setUnitId(getLoginInfo().getUnitId());
				tutorParam.setParam(param);
				tutorParam.setParamType(TutorConstants.TUTOR_STUENT_MAX_NUMBER);
			}else{
				tutorParam.setParam(param);
			}
			tutorParamService.save(tutorParam);
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	@RequestMapping("/teacher/import")
	public void resultTeaImport(String type,ModelMap map){
		
	}
	@RequestMapping("/teacher/export")
	public void resultTeaExport(String teacherName,String tutorId,ModelMap map){
		List<TutorTeaStuDto> teaStuDtos = getTutorTeaStuDto(teacherName,tutorId,null);
		doExportTea(teaStuDtos, getResponse());
	}
	
	@RequestMapping("/teacher/list")
	public String resultTeaList(HttpServletRequest request,String teacherName,String tutorId,ModelMap map){
		Pagination page=createPagination();
		Map<String, String> paramMap = syncParameters(request);
		int row = NumberUtils.toInt(paramMap.get("_pageSize"));
		if(row<=0){
			page.setPageSize(20);
		}
		List<TutorTeaStuDto> teaStuDtos = getTutorTeaStuDto(teacherName,tutorId,page);
		TutorParam tutorParam = tutorParamService.findByUnitIdAndPtype(getLoginInfo().getUnitId(), TutorConstants.TUTOR_STUENT_MAX_NUMBER);
		map.put("maxStuNum", tutorParam==null?0:tutorParam.getParam());
		map.put("teaStuDtos", teaStuDtos);
		sendPagination(request, map, page);
		return "/tutor/teacher/defend/tutorResultTeacherList.ftl";
	}
	
	
	private List<TutorTeaStuDto> getTutorTeaStuDto(String teacherName,String tutorId,Pagination page){
		return getTutorTeaStuDto(teacherName,tutorId,page,null);
	}
	
	private List<TutorTeaStuDto> getTutorTeaStuDto(String teacherName,String tutorId,Pagination page, Integer section){
		List<TutorResult> tutorResults = Lists.newArrayList();
		List<Student> students = Lists.newArrayList();
		List<Teacher> teachers = Lists.newArrayList();
		if(StringUtils.isNotBlank(tutorId)) {
			List<TutorRoundTeacher> tutorRoundTeachers = Lists.newArrayList();
			teachers = SUtils.dt(teacherRemoteService.findByNameLikeIdNotIn(teacherName, getLoginInfo().getUnitId(), null, new String[0]),Teacher.class);
			Set<String> teaIds = EntityUtils.getSet(teachers, Teacher::getId);
			if(page!=null){
				tutorRoundTeachers =tutorRoundTeacherService.findByRidAndTeaIdIn(tutorId,getLoginInfo().getUnitId(),page,teaIds.toArray(new String[teaIds.size()]));
			}else{
				tutorRoundTeachers =tutorRoundTeacherService.findByRidAndTeaIdIn(tutorId,getLoginInfo().getUnitId(),null,teaIds.toArray(new String[teaIds.size()]));
			}
			Set<String> teaidList = EntityUtils.getSet(tutorRoundTeachers, TutorRoundTeacher::getTeacherId);
			teachers =SUtils.dt (teacherRemoteService.findListByIds(teaidList.toArray(new String[teaidList.size()])),Teacher.class);
		}else {
			if(page!=null){
				teachers = Teacher.dt(teacherRemoteService.findByNameLikeIdNotIn(teacherName, getLoginInfo().getUnitId(), SUtils.s(page), new String[0]),page);
			}else{
				teachers = SUtils.dt(teacherRemoteService.findByNameLikeIdNotIn(teacherName, getLoginInfo().getUnitId(), null, new String[0]),Teacher.class);
			}
		}
		Set<String> teaIds = EntityUtils.getSet(teachers, Teacher::getId);
		if(teaIds.size()>0){
			//增加一个学段条件
			tutorResults = tsutorResultService.findByTeacherIds(teaIds.toArray(new String[teaIds.size()]));
//			if(section != null){
//				tutorResults = tutorResults.stream().filter(s -> s.getSection() == section).collect(Collectors.toList());
//			}
			
		}
		Set<String> studentIds = EntityUtils.getSet(tutorResults, TutorResult::getStudentId);
		if(studentIds.size()>0){
			students = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])),new TR<List<Student>>() {});
		}
		Map<String,String> stuNameMap = EntityUtils.getMap(students, Student::getId, Student::getStudentName);
		Map<String,Set<String>> teaStuMap = Maps.newHashMap();
		for(TutorResult result:tutorResults){
			if(teaStuMap.containsKey(result.getTeacherId())){
				Set<String> stuIds = teaStuMap.get(result.getTeacherId());
				stuIds.add(result.getStudentId());
				teaStuMap.put(result.getTeacherId(), stuIds);
			}else{
				Set<String> stuIds = Sets.newHashSet();
				stuIds.add(result.getStudentId());
				teaStuMap.put(result.getTeacherId(), stuIds);
			}
		}
		List<TutorTeaStuDto> teaStuDtos = Lists.newArrayList();
		for(Teacher teacher:teachers){
			StringBuilder studentIdsStr = new StringBuilder("");
			StringBuilder studentNamesStr = new StringBuilder("");
			int studentNum = 0;
			Set<String> stuIds = Sets.newHashSet();
			if(teaStuMap.containsKey(teacher.getId())){
				stuIds = teaStuMap.get(teacher.getId());
				studentNum = stuIds.size();
				for(String stuId:stuIds){
					studentIdsStr.append(stuId+",");
					if(stuNameMap.containsKey(stuId)){
						studentNamesStr.append(stuNameMap.get(stuId)+",");
					}
				}
			}
			TutorTeaStuDto teaStuDto = new TutorTeaStuDto();
			teaStuDto.setTeacherId(teacher.getId());
			teaStuDto.setTeacherName(teacher.getTeacherName());
			teaStuDto.setStudentIds(StringUtils.removeEnd(studentIdsStr.toString(), ","));
			teaStuDto.setStudentNames(StringUtils.removeEnd(studentNamesStr.toString(), ","));
			teaStuDto.setStudentNum(studentNum);
			teaStuDtos.add(teaStuDto);
		}
		return teaStuDtos;
	}
	@ResponseBody
	@RequestMapping("/teacher/savestu")
	public String resultTeaSaveStu(String teacherId,String[] stuIds,String tutorId,ModelMap map){
		try{
			if(StringUtils.isBlank(teacherId)){
				return error("保存失败,教师Id为空！");
			}
			List<TutorResult> teaResults = tsutorResultService.findByTeacherId(teacherId);
			Set<String> thisTstuIds = EntityUtils.getSet(teaResults, TutorResult::getStudentId);
			if(thisTstuIds.size()>0&&(stuIds==null||stuIds.length==0)){
				return error("不可删除已选学生！");
			}
			if(stuIds==null||stuIds.length==0){
				return success("保存成功");
			}
			if(thisTstuIds.size()>0){
				thisTstuIds.remove(null);
				for(String stuId:stuIds){
					thisTstuIds.remove(stuId);
				}
				if(thisTstuIds.size()>0){
					return error("不可删除已选学生！");
				}
			}
			Set<String> updateStuIds = Sets.newHashSet();
			List<TutorResult> insertResults = Lists.newArrayList();
			List<TutorResult> stuResults = tsutorResultService.findByStudentIds(stuIds);
			List<String> stuIdLists = Arrays.asList(stuIds);
			Set<String> stuIdSets = Sets.newHashSet(stuIdLists);
			for(TutorResult result:stuResults){
				if(!teacherId.equals(result.getTeacherId())){
					updateStuIds.add(result.getStudentId());
				}
				stuIdSets.remove(result.getStudentId());
			}
			Map<String, Student> stuIdMap = null;
			//判断是否是本年级的学生添加
			if(StringUtils.isNotBlank(tutorId)) {
				List<TutorRoundGrade> tutorRoundGrade = tutorRoundGradeService.findByRoundId(tutorId);
				List<String> gradeIds = EntityUtils.getList(tutorRoundGrade, TutorRoundGrade::getGradeId);
	    		List<String> newList = new ArrayList<String>(new HashSet<String>(gradeIds)); 
	    		List<Student> students = SUtils.dt(studentRemoteService.findByGradeIds(newList.toArray(new String[newList.size()])), Student.class);
				stuIdMap = EntityUtils.getMap(students, Student::getId);
			}
			for(String stuId:stuIdSets){
				TutorResult result = new TutorResult();
				if(StringUtils.isNotBlank(tutorId) && !stuIdMap.isEmpty()) {
					if(stuIdMap.get(stuId) != null) {
						result.setRoundId(StringUtils.isNotBlank(tutorId)?tutorId:"");
					}
				}else {
					result.setRoundId("");
				}
				result.setId(UuidUtils.generateUuid());
				result.setState(TutorResult.STATE_NORMAL);
				result.setStudentId(stuId);
				result.setTeacherId(teacherId);
				result.setUnitId(getLoginInfo().getUnitId());
				result.setCreationTime(new Date());
				result.setModifyTime(new Date());
				insertResults.add(result);
			}
			if(insertResults.size()>0){
				tsutorResultService.saveAll(insertResults.toArray(new TutorResult[insertResults.size()]));
			}
			if(updateStuIds.size()>0){
				if(StringUtils.isNotBlank(tutorId) && !stuIdMap.isEmpty()) {
					Set<String> updateStuIds1 = Sets.newHashSet();
					Set<String> updateStuIds2 = Sets.newHashSet();
					for (String stuId : updateStuIds) {
						if(stuIdMap.get(stuId) != null) {
							updateStuIds1.add(stuId);
						}else {
							updateStuIds2.add(stuId);
						}
					}
					if(updateStuIds1.size()>0)
						tsutorResultService.updateTutor(updateStuIds1.toArray(new String[updateStuIds1.size()]),teacherId,tutorId);
					if(updateStuIds2.size()>0)
						tsutorResultService.updateTutor(updateStuIds2.toArray(new String[updateStuIds2.size()]),teacherId,"");
				}else {
					tsutorResultService.updateTutor(updateStuIds.toArray(new String[updateStuIds.size()]),teacherId,tutorId);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@RequestMapping("/student/head")
	public String resultStuHead(String type,ModelMap map){
		List<Grade> grades = SUtils.dt(gradeRemoteService.findBySchoolId(getLoginInfo().getUnitId()),new TR<List<Grade>>() {});
		map.put("grades", grades);
		return "/tutor/teacher/defend/tutorResultStudentTab.ftl";
	}
	
	@RequestMapping("/get/class/page")
	public void getClass(String gradeId,ModelMap map){
		List<Clazz> clazzs = SUtils.dt(classRemoteService.findBySchoolIdGradeId(getLoginInfo().getUnitId(), gradeId),new TR<List<Clazz>>() {});
		JSONArray jsonArray = new JSONArray();
		for(Clazz clazz:clazzs){
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id",clazz.getId());
			jsonObject.put("name",clazz.getClassNameDynamic());
			jsonArray.add(jsonObject);
		}
		try {
			ServletUtils.print(getResponse(), jsonArray.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/student/list")
	public String resultStuList(TutorStuSearchDto stuSearchDto,HttpServletRequest request,ModelMap map){
		List<TutorStuTeaDto> tutorStuTeaDtos = getTutorStuTeaDto(stuSearchDto);
		List<TutorStuTeaDto> returnTutorStuTeaDtos = Lists.newArrayList();
		Pagination page=createPagination();
		Map<String, String> paramMap = syncParameters(request);
		int row = NumberUtils.toInt(paramMap.get("_pageSize"));
		if(row<=0){
			page.setPageSize(20);
		}
		//页面选人渲染较慢，做个分页
		page.setMaxRowCount(tutorStuTeaDtos.size());
		int index = 0;
		for(TutorStuTeaDto dto:tutorStuTeaDtos){
			if((page.getPageIndex()-1)*(page.getPageSize())<=index&&index<(page.getPageIndex())*(page.getPageSize())){
				returnTutorStuTeaDtos.add(dto);
			}
			index++;
		}
		map.put("tutorStuTeaDtos", returnTutorStuTeaDtos);
		sendPagination(request, map, page);
		return "/tutor/teacher/defend/tutorResultStudentList.ftl";
	}
	
	private List<TutorStuTeaDto> getTutorStuTeaDto(TutorStuSearchDto stuSearchDto){
		List<TutorStuTeaDto> tutorStuTeaDtos = Lists.newArrayList();
		if(StringUtils.isNotBlank(stuSearchDto.getClassId())){
			//班级id,学号，姓名查询
			Clazz clazz = SUtils.dc(classRemoteService.findOneById(stuSearchDto.getClassId()),Clazz.class);
//			List<Student> students =  SUtils.dt(studentRemoteService.findByClassIds(stuSearchDto.getClassId()),new TR<List<Student>>() {});
			Student searchStudent = new Student();
			if (("2").equals(stuSearchDto.getQueryType())) {
	        	// 学号模糊查询
	        	if(StringUtils.isNotBlank(stuSearchDto.getStudentCode())){
	        		//防止注入
	        		String linStr = stuSearchDto.getStudentCode().replaceAll("%", "");
	        		//全匹配
	        		searchStudent.setStudentCode("%" + linStr+ "%");
	        	}
	        }
	        else {
	        	// 姓名
	        	if(StringUtils.isNotBlank(stuSearchDto.getStudentName())){
	        		//防止注入
	        		String linStr = stuSearchDto.getStudentName().replaceAll("%", "");
	        		//全匹配
	        		searchStudent.setStudentName("%" + linStr+ "%");
	        	}
	        }
			List<Student> students =  SUtils.dt(studentRemoteService.findByIdsClaIdLikeStuCodeNames(getLoginInfo().getUnitId(), null, new String[]{stuSearchDto.getClassId()}, Json.toJSONString(searchStudent), null),new TR<List<Student>>() {});
			Set<String> studentIds = EntityUtils.getSet(students, Student::getId);
			Map<String,String> stuTeaMap = Maps.newHashMap();
			Map<String,String> teaNameMap = Maps.newHashMap();
			if(studentIds.size()>0){
				List<TutorResult> tutorResults = tsutorResultService.findByStudentIds(studentIds.toArray(new String[studentIds.size()]));
				Set<String> teaIds = EntityUtils.getSet(tutorResults, TutorResult::getTeacherId);
				if(teaIds.size()>0){
					List<Teacher> teachers = SUtils.dt(teacherRemoteService.findListByIds(teaIds.toArray(new String[teaIds.size()])),new TR<List<Teacher>>(){});
					teaNameMap = EntityUtils.getMap(teachers, Teacher::getId,Teacher::getTeacherName);
				}
				stuTeaMap = EntityUtils.getMap(tutorResults, TutorResult::getStudentId,TutorResult::getTeacherId);
			}
			for(Student student:students){
				if(clazz!=null){
					student.setClassName(clazz.getClassNameDynamic());
				}
				TutorStuTeaDto stuTeaDto = new TutorStuTeaDto();
				if(stuTeaMap.containsKey(student.getId())){
					stuTeaDto.setTutorTeaId(stuTeaMap.get(student.getId()));
					if(teaNameMap.containsKey(stuTeaDto.getTutorTeaId())){
						stuTeaDto.setTutorTeaName(teaNameMap.get(stuTeaDto.getTutorTeaId()));
					}
					stuTeaDto.setHaveTutor(true);
					if("1".equals(stuSearchDto.getType())){
						tutorStuTeaDtos.add(stuTeaDto);
					}
				}else{
					if("0".equals(stuSearchDto.getType())){
						tutorStuTeaDtos.add(stuTeaDto);
					}
				}
				stuTeaDto.setStudent(student);
				if(StringUtils.isBlank(stuSearchDto.getType())){
					tutorStuTeaDtos.add(stuTeaDto);
				}
			}
		}
		return tutorStuTeaDtos;
	}
	@ResponseBody
	@RequestMapping("/student/saveTea")
	public String resultStuSaveTea(String studentId,String teacherId,ModelMap map){
		try{
			TutorResult result = tsutorResultService.findByStudentId(studentId);
			if(result!=null){
				if(StringUtils.isBlank(teacherId)){
					return error("学生导师不可删除");
				}else{
					tsutorResultService.updateTutor(new String[]{studentId}, teacherId,"");
				}
			}else{
				if(StringUtils.isNotBlank(teacherId)&&StringUtils.isNotBlank(studentId)){
					result = new TutorResult();
					result.setId(UuidUtils.generateUuid());
					result.setState(TutorResult.STATE_NORMAL);
					result.setStudentId(studentId);
					result.setTeacherId(teacherId);
					result.setUnitId(getLoginInfo().getUnitId());
					result.setCreationTime(new Date());
					result.setModifyTime(new Date());
					tsutorResultService.save(result);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}

	@RequestMapping("/student/export")
	public void resultStuExport(TutorStuSearchDto stuSearchDto,ModelMap map){
		List<TutorStuTeaDto> tutorStuTeaDtos = getTutorStuTeaDto(stuSearchDto);
		doExportStu(tutorStuTeaDtos, getResponse());
	}
	
	private void doExportStu(List<TutorStuTeaDto> tutorStuTeaDtos,HttpServletResponse response){
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		for(TutorStuTeaDto item : tutorStuTeaDtos){
			Map<String,String> sMap = new HashMap<String,String>();
			sMap.put("学生姓名", item.getStudent().getStudentName());
			sMap.put("学号", item.getStudent().getStudentCode());
			sMap.put("行政班", item.getStudent().getClassName());
			sMap.put("是否有导师", item. isHaveTutor()?"是":"否");
			sMap.put("所选导师", item.getTutorTeaName());
			recordList.add(sMap);
		}
		sheetName2RecordListMap.put(getObjectName(2),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList(2);
		titleMap.put(getObjectName(2), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile("学生导师信息", titleMap, sheetName2RecordListMap, response);	
	}
	private void doExportTea(List<TutorTeaStuDto> tutorTeaStuDtos,HttpServletResponse response){
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		TutorParam tutorParam = tutorParamService.findByUnitIdAndPtype(getLoginInfo().getUnitId(), TutorConstants.TUTOR_STUENT_MAX_NUMBER);
		for(TutorTeaStuDto item : tutorTeaStuDtos){
			Map<String,String> sMap = new HashMap<String,String>();
			sMap.put("教师姓名", item.getTeacherName());
			sMap.put("人员情况", item.getStudentNum()+"/"+(tutorParam==null?0:tutorParam.getParam()));
			sMap.put("所导学生", item.getStudentNames());
			recordList.add(sMap);
		}
		sheetName2RecordListMap.put(getObjectName(1),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList(1);
		titleMap.put(getObjectName(1), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile("导师学生信息", titleMap, sheetName2RecordListMap, response);	
	}
	
	private String getObjectName(int type) {
		if(type==1){
			return "导师学生";
		}else{
			return "学生导师";
		}
	}
	
	private List<String> getRowTitleList(int type) {
		List<String> tis = new ArrayList<String>();
		if(type==1){
			tis.add("教师姓名");
			tis.add("人员情况");
			tis.add("所导学生");
		}else{
			tis.add("学生姓名");
			tis.add("学号");
			tis.add("行政班");
			tis.add("是否有导师");
			tis.add("所选导师");
		}
		return tis;
	}
	
	@ResponseBody
	@RequestMapping("/student/relieve")
	public String resultStuRelieve(ModelMap map){
		try{
			List<Grade> grades = SUtils.dt(gradeRemoteService.findBySchoolIdAndIsGraduate(getLoginInfo().getUnitId(),Grade.GRADUATED),
					new TR<List<Grade>>() {});
			if(CollectionUtils.isNotEmpty(grades)){
				Set<String> gradeIdList = EntityUtils.getSet(grades, Grade::getId);
				List<TutorRoundGrade> tutorRoundGrades = tutorRoundGradeService.findbyGradeIds(gradeIdList.toArray(new String[gradeIdList.size()]));
				if(CollectionUtils.isNotEmpty(tutorRoundGrades)){
					Set<String> roundIds = EntityUtils.getSet(tutorRoundGrades, TutorRoundGrade::getRoundId);
					tsutorResultService.updateStateByRoundId(TutorResult.STATE_LEAVE,roundIds.toArray(new String[roundIds.size()]));
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			return error("更新失败！"+e.getMessage());
		}
		return success("更新成功");
	}
}
