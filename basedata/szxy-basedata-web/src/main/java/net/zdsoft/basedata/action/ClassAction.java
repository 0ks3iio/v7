package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.zdsoft.basedata.dto.ClazzDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachArea;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.SchoolService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.basedata.service.TeachAreaService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.EntityUtils;

@Controller
@RequestMapping("/basedata")
public class ClassAction extends BaseAction {

	@Autowired
	private GradeService gradeService;

	@Autowired
	private ClassService classService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private SchoolService schoolService;

	@Autowired
	private TeachAreaService teachAreaService;
	
	private static Map<String, LinkedHashMap<String, String>> sectionMap = new HashMap<String, LinkedHashMap<String,String>>();

    static{
    	LinkedHashMap<String, String> linMap = new LinkedHashMap<String, String>();
    	linMap.put("0", "幼儿园");
    	sectionMap.put("0", linMap);
    	linMap = new LinkedHashMap<String, String>();
    	linMap.put("1", "小学");
    	sectionMap.put("1", linMap);
    	linMap = new LinkedHashMap<String, String>();
    	linMap.put("2", "初中");
    	sectionMap.put("2", linMap);
    	linMap = new LinkedHashMap<String, String>();
    	linMap.put("3", "高中");
    	sectionMap.put("3", linMap);
    	linMap = new LinkedHashMap<String, String>();
    	linMap.put("1", "小学");
    	linMap.put("2", "初中");
    	sectionMap.put("1,2", linMap);
    	linMap = new LinkedHashMap<String, String>();
    	linMap.put("2", "初中");
    	linMap.put("3", "高中");
    	sectionMap.put("2,3", linMap);
    	linMap = new LinkedHashMap<String, String>();
    	linMap.put("1", "小学");
    	linMap.put("2", "初中");
    	linMap.put("3", "高中");
    	sectionMap.put("1,2,3", linMap);
    	linMap = new LinkedHashMap<String, String>();
    	linMap.put("0", "幼儿园");
    	linMap.put("1", "小学");
    	linMap.put("2", "初中");
    	linMap.put("3", "高中");
    	sectionMap.put("0,1,2,3", linMap);
    	
    }
	
	@ControllerInfo("进入年级{gradeId}下的班级页面")
	@RequestMapping("/class/grade/{gradeId}/list/page")
	public String showClassByGradeIdPage(@PathVariable String gradeId, ModelMap map, HttpSession httpSession) {
		Grade grade = gradeService.findOne(gradeId);
		if (grade != null) {
			map.put("gradeId", gradeId);
			map.put("gradeName", grade.getGradeName());
			map.put("unitId", grade.getSchoolId());
		}
		return "/basedata/class/classList.ftl";
	}

	@ResponseBody
	@ControllerInfo("读取年级{gradeId}下的班级数据")
	@RequestMapping("/class/grade/{gradeId}/list")
	public String showClassByGradeId(@PathVariable String gradeId, HttpServletRequest req, HttpSession httpSession) {
		List<Clazz> classes = classService.findByGradeIdIn(gradeId);
		List<String> classIds = new ArrayList<String>(classes.size());
		for (Clazz clazz : classes) {
			classIds.add(clazz.getId());
		}
		List<String> teacherIds = EntityUtils.getList(classes, "teacherId");
		List<Teacher> ts = teacherService.findListByIdIn(teacherIds.toArray(new String[0]));
		Map<String, Teacher> tns = new HashMap<String, Teacher>(ts.size());
		for (Teacher t : ts) {
			tns.put(t.getId(), t);
		}
		Map<String, Integer> studentCount = studentService.countMapByClassIds(classIds.toArray(new String[0]));
		List<ClazzDto> dtos = new ArrayList<ClazzDto>();
		for (Clazz clazz : classes) {
			String classId = clazz.getId();
			ClazzDto dto = new ClazzDto();
			dto.setClazz(clazz);
			Integer count = studentCount.get(classId);
			dto.setStudentCount(count == null ? 0 : count);
			dto.setTeacher(tns.get(clazz.getTeacherId()));
			dtos.add(dto);
		}
		return JSON.toJSONString(dtos);
	}
	
	@ResponseBody
	@RequestMapping("/class/maxName/find")
	@ControllerInfo("查找最大班级名称")
	public String showMaxClassName(String schoolId,String section,String acadyear,int schoolingLength, HttpServletRequest req, HttpSession httpSession) {
		String classCode = classService.findNextClassCode(schoolId, section,
				acadyear, schoolingLength);
		int code = NumberUtils.toInt(StringUtils.right(classCode, 2));
		return "(" + StringUtils.leftPad("" + code, 2, "0") + ")班";
	}
	
	@RequestMapping("/class/edit/page")
    @ControllerInfo(value = "新增修改班级")
    public String showClassAdd(String id,String gradeId,String section,ModelMap map, HttpSession httpSession) {
		Clazz clazz = new Clazz();
        if(StringUtils.isNotBlank(id)){
        	clazz = classService.findOne(id);
        	List<Student> findByClassId = studentService.findByClassIds(id);
        	map.put("studentList", findByClassId);
        }else{
        	clazz.setGradeId(gradeId);
        	clazz.setSection(Integer.valueOf(section));
        	clazz.setBuildDate(new Date());
        }
        ClazzDto dto = new ClazzDto();
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        dto.setClazz(clazz);
        clazz.setSchoolId(unitId);
        map.put("dto", dto);
        map.put("unitId", unitId);
        List<Teacher> findByUnitId = teacherService.findByUnitId(unitId);
        map.put("teacherList", findByUnitId);
        List<TeachArea> findByUnitId2 = teachAreaService.findByUnitId(unitId);
        map.put("teachAreaList", findByUnitId2);
        School school = schoolService.findOne(unitId);
        LinkedHashMap<String, String> linMap = sectionMap.get(school.getSections());
        if(linMap!=null){
        	map.put("xdMap", linMap);
        }
        return "/basedata/class/classAdd.ftl";
    }
	
	@RequestMapping("/class/batchAdd/page")
	@ControllerInfo(value = "批量新增班级")
	public String showClassBatchAdd(String gradeId,String section,ModelMap map, HttpSession httpSession) {
		Clazz clazz = new Clazz();
    	clazz.setGradeId(gradeId);
    	if(StringUtils.isNotBlank(section)){
    		clazz.setSection(Integer.valueOf(section));
    	}
    	clazz.setBuildDate(new Date());
        ClazzDto dto = new ClazzDto();
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        dto.setClazz(clazz);
        clazz.setSchoolId(unitId);
        map.put("dto", dto);
        map.put("unitId", unitId);
        School school = schoolService.findOne(unitId);
        LinkedHashMap<String, String> linMap = sectionMap.get(school.getSections());
        if(linMap!=null){
        	map.put("xdMap", linMap);
        }
		return "/basedata/class/classBatchAdd.ftl";
	}
	
	@ResponseBody
    @RequestMapping("/class/save")
    @ControllerInfo(value = "保存班级")
    public String doSaveClass(@RequestBody ClazzDto clazzDto) {
		try{
			Clazz clazz = clazzDto.getClazz();
			if(StringUtils.isBlank(clazz.getId())){
				List<Clazz> clazzs = classService.findClassList(clazz.getGradeId(),clazz.getClassName());
				if (CollectionUtils.isNotEmpty(clazzs)) {
					return error("操作失败，该年级下已存在相同名称的班级！");
				}
				String findNextClassCode = classService.findNextClassCode(clazz.getSchoolId(), String.valueOf(clazz.getSection()), clazz.getAcadyear(), clazz.getSchoolingLength());
				clazz.setClassCode(findNextClassCode);
				clazz.setIsGraduate(0);
			}else{
				List<Clazz> clazzs = classService.findClassList(clazz.getGradeId(),clazz.getClassName());
				boolean ok = false;
				if (CollectionUtils.isEmpty(clazzs))
					ok = true;
				for (Clazz g : clazzs) {
					if (StringUtils.equals(g.getId(), clazz.getId())) {
						ok = true;
						break;
					}
				}
				if (!ok) {
					return error("操作失败，该年级下已存在相同名称的班级！");
				}
				Clazz clazzOld = classService.findOne(clazz.getId());
				EntityUtils.copyProperties(clazz, clazzOld, true);
				clazz = clazzOld;
			}
			classService.saveAllEntitys(clazz);
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
    }
	
	@ResponseBody
	@RequestMapping("/class/batchSave")
	@ControllerInfo(value = "批量新增班级保存")
	public String doBatchSaveClass(@RequestBody ClazzDto clazzDto) {
		try{
			Clazz clazz = clazzDto.getClazz();
			String classCode = classService.findNextClassCode(clazz.getSchoolId(), "" + clazz.getSection(),
					clazz.getAcadyear(), clazz.getSchoolingLength());
			int code = NumberUtils.toInt(StringUtils.right(classCode, 2));
			List<Clazz> batchClazz = new ArrayList<Clazz>();
			String className;
			Clazz linCla;
			int classCodeInt = NumberUtils.toInt(classCode);
			for (int i = 0; i < clazzDto.getAddClassCount(); i++) {
				linCla = new Clazz();
				EntityUtils.copyProperties(clazzDto.getClazz(), linCla);
				className = "(" + StringUtils.leftPad("" + code, 2, "0") + ")班";
				linCla.setClassName(className);
				linCla.setClassCode(String.valueOf(classCodeInt));
				linCla.setIsGraduate(0);
				batchClazz.add(linCla);
				code++;
				classCodeInt++;
			}
			classService.saveAllEntitys(batchClazz.toArray(new Clazz[0]));
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
    @RequestMapping("/class/{id}/delete")
    @ControllerInfo(value = "删除班级:{id}")
    public String doDeleteClass(@PathVariable String id, HttpSession httpSession) {
		try{
			List<Student> findByClassId = studentService.findByClassIds(id);
	        if (CollectionUtils.isNotEmpty(findByClassId)) {
	            return error("该班级下存在学生，不能删除！");
	        }
	        classService.deleteAllIsDeleted(id);
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
    }
	
	@ResponseBody
	@RequestMapping("/school/{schoolId}/class/tree")
	@ControllerInfo("显示某个学校下面的班级树")
	public String showClassBySchoolId(@PathVariable("schoolId") final String schoolId){
		if(StringUtils.isEmpty(schoolId)){
			return error("");
		}
		JSONArray array = null;
		try {
			List<Grade> grades =gradeService.findByUnitId(schoolId);
			//List<Grade> grades = gradeService.findByIn("schoolId", new String[]{schoolId});
			List<String> gradeIds = EntityUtils.getList(grades, "id");
			List<Clazz> clazzs = classService.findBySchoolIdAndGradeIds(schoolId, gradeIds.toArray(new String[0]));
			Map<String,List<Clazz>> g2CMap = Maps.newHashMap();// EntityUtils.getMap(clazzs, "gradeId", StringUtils.EMPTY);
			for(Grade grade :  grades){
				List<Clazz> clazzss = Lists.newArrayList();
				for(Clazz clazz : clazzs){
					if(StringUtils.equals(clazz.getGradeId(), grade.getId())){
						clazzss.add(clazz);
					}
				}
				if(CollectionUtils.isNotEmpty(clazzss))
					g2CMap.put(grade.getId(),clazzss);
			}
			
			School school = schoolService.findOne(schoolId);
			//构造树
			array = new JSONArray();
			Json json = new Json();
			json.put("pId", StringUtils.EMPTY);
			json.put("type","school");
			json.put("id",schoolId);
			json.put("name",school.getSchoolName());
			json.put("title",school.getSchoolName());
			json.put("open",true);
			array.add(json);
			
			for(Grade grade : grades){
				json = new Json();
				json.put("pId", schoolId);
				json.put("type","grade");
				json.put("id",grade.getId());
				json.put("name",grade.getGradeName());
				json.put("title",grade.getGradeName());
				array.add(json);
				List<Clazz> cls = g2CMap.get(grade.getId());
				if(CollectionUtils.isEmpty(cls)){
					continue;
				}
				for(Clazz clazz : cls){
					json = new Json();
					json.put("pId", grade.getId());
					json.put("type","class");
					json.put("id",clazz.getId());
					json.put("name",clazz.getClassName());
					json.put("title",clazz.getClassName());
					array.add(json);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success(JSONUtils.toJSONString(array));
	}
}
