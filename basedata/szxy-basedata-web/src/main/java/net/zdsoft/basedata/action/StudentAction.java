package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

@Controller
@RequestMapping("/basedata")
public class StudentAction extends BaseAction{
	
	@Autowired
	private StudentService studentService;
	@Autowired
	private ClassService classService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private SemesterService semesterService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	
	@RequestMapping("/student/index/page")
    @ControllerInfo(value = "学生管理")
    public String showIndex(ModelMap map, HttpSession httpSession) {
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
        Integer unitClass = loginInfo.getUnitClass();
        map.put("unitId", unitId);
        if(unitClass==Constant.CLASS_SCH){
        	//学校
        	map.put("schoolId", unitId);
        	return "/basedata/student/studentSchIndex.ftl";
    	}else{
    		map.put("eduList", getEduList(unitId));
    		map.put("schoolList", getSchoolList(unitId));
    		//教育局
    		return "/basedata/student/studentEduIndex.ftl";
    	}
        
    }
	
	private List<Unit> getEduList(String unitId){
		List<Unit> unitList=new ArrayList<Unit>();
		Unit unit = unitService.findOne(unitId);
		unitList.add(unit);
		List<Unit> list = unitService.findDirectUnitsByParentId(unitId, Constant.CLASS_EDU);
		if(CollectionUtils.isNotEmpty(list)){
			unitList.addAll(list);
		}
		return unitList;
	}
	private List<Unit> getSchoolList(String unitId){
		List<Unit> list = unitService.findDirectUnitsByParentId(unitId, Constant.CLASS_SCH);
		if(CollectionUtils.isNotEmpty(list)){
			return list;
		}else{
			return new ArrayList<Unit>();
		}
	}
	
	@RequestMapping("/student/stulist/page")
	@ControllerInfo("查看学校学生列表")
	public String showStulist(ModelMap map,HttpServletRequest request,HttpSession httpSession) {
		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId=  request.getParameter("unitId");
		String schoolId=  request.getParameter("schoolId");
        String gradeId= request.getParameter("gradeId");
        String classId= request.getParameter("classId");
        String stuName= request.getParameter("stuName");
        map.put("unitId", unitId);
        map.put("schoolId", schoolId);
		map.put("gradeId", gradeId);
		map.put("classId", classId);
		map.put("stuName", stuName);
		map.put("unitClass", loginInfo.getUnitClass());
		return "/basedata/student/studentList.ftl";
	}
	
    @ResponseBody
    @RequestMapping("/student/stulist")
    @ControllerInfo("学生列表")
    public String stulist( ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession sesion) {
    	Pagination page = createPaginationJqGrid(request);
    	String unitId=  request.getParameter("unitId");
    	String schoolId=  request.getParameter("schoolId");
        String gradeId= request.getParameter("gradeId");
        String classId= request.getParameter("classId");
        String stuName= request.getParameter("stuName");
        if(StringUtils.isNotBlank(stuName)){
        	stuName=stuName.trim();
        }
    	List<Student> studentList=studentService.findBy(stuName,unitId,schoolId,gradeId,classId,page);
        return returnJqGridData(page, studentList);
    }
    
    @ResponseBody
	@RequestMapping("/student/deletes")
	@ControllerInfo("批量删除学生")
	public String doDeletes(String[] ids, ModelMap map, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	
    	try {
    		if(ids!=null && ids.length>0){
    			studentService.updateIsDeleteds(ids);
        	}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("删除失败！", e.getMessage());
		}
		return success("删除成功");
	}
    
	@RequestMapping("/student/add/page")
	@ControllerInfo("新增")
	public String add(ModelMap map,HttpServletRequest request) {
        String classId= request.getParameter("classId");
        Student student=new Student();
        Clazz clazz = classService.findOne(classId);
        student.setClassId(classId);
        student.setSchoolId(clazz.getSchoolId());
		map.put("student", student);
		map.put("acadyearList", semesterService.findAcadeyearList());
		return "/basedata/student/studentEdit.ftl";
	}
	
	@RequestMapping("/student/{stuId}/edit/page")
	@ControllerInfo("修改")
	public String edit(@PathVariable final String stuId,ModelMap map,HttpServletRequest request) {
		Student student=studentService.findOne(stuId);
		map.put("student", student);
		map.put("acadyearList", semesterService.findAcadeyearList());
		return "/basedata/student/studentEdit.ftl";
	}
	
	@ResponseBody
    @RequestMapping("/student/save")
    @ControllerInfo(value = "保存学生" )
    public String doSaveExam(@RequestBody Student student) {
		try{
			if(StringUtils.isBlank(student.getId())){
				student.setId(UuidUtils.generateUuid());
				student.setNowState(Student.NOWSTATE_40);
				student.setIsFreshman(Student.FRESHMAN_9);
				student.setIsDeleted(Constant.IS_DELETED_FALSE);
				student.setIsLeaveSchool(Constant.IS_FALSE);
			}else{
				Student studentOld = studentService.findOne(student.getId());
				if(Constant.IS_DELETED_TRUE==studentOld.getIsDeleted()){
					//已删除
					return error("该学生已删除");
				}
				EntityUtils.copyProperties(student, studentOld, true);
				student=studentOld;
				student.setModifyTime(new Date());
			}
			
			studentService.saveAllEntitys(student);
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return success("保存成功");
    }
	
	
	@ResponseBody
	@RequestMapping("/student/schoolList")
	public List<Unit> schoolList(String eduId){
		List<Unit> schoolList = getSchoolList(eduId);
		if(CollectionUtils.isNotEmpty(schoolList)){
			return schoolList;
		}
		return new ArrayList<Unit>();
	}
	@ResponseBody
	@RequestMapping("/student/gradeList")
	public List<Grade> gradeList(String schoolId){
		List<Grade> gradeList = gradeService.findByUnitId(schoolId);
		if(CollectionUtils.isNotEmpty(gradeList)){
			return gradeList;
		}
		return new ArrayList<Grade>();
	}
	@ResponseBody
	@RequestMapping("/student/classList")
	public List<Clazz> classList(String gradeId){
		List<Clazz> classList = classService.findByGradeIdIn(gradeId);
		if(CollectionUtils.isNotEmpty(classList)){
			return classList;
		}
		return new ArrayList<Clazz>();
	}
   //------------填写身份证号--------------
	@RequestMapping("/student/cardIndex/page")
    @ControllerInfo(value = "学生管理")
    public String showCardIndex(ModelMap map, HttpSession httpSession) {
		List<McodeDetail> sexList = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-XB"),new TypeReference<List<McodeDetail>>(){});
		map.put("sexList", sexList);
		return "/basedata/student/studentCardIndex.ftl";
	}
	@RequestMapping("/student/cardList/page")
	public String cardStuList(ModelMap map, HttpServletRequest request,HttpSession httpSession){
	    String stuName= request.getParameter("stuName");
        String sex= request.getParameter("sex");
        map.put("stuName", stuName);
        map.put("sex", sex);
	    return "/basedata/student/studentCardList.ftl";
	}
	@ResponseBody
    @RequestMapping("/student/cardList")
    @ControllerInfo("学生列表")
    public String cardList( ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession) {
    	LoginInfo loginInfo = getLoginInfo(httpSession);
	    String unitId = loginInfo.getUnitId();
	    String stuName= request.getParameter("stuName");
        String sex= request.getParameter("sex");
        List<Student> stuList=studentService.findByNameSexNoCard(unitId,stuName,Integer.parseInt(sex));
        return Json.toJSONString(stuList);
    }
	@ResponseBody
	@RequestMapping("/student/stuCardEditing")
	public String stuCardEditing(Student student){
		try{
			if(StringUtils.isNotBlank(student.getId())){
				if(StringUtils.isBlank(student.getIdentityCard())){
					return error("身份证类型为空");
				}
				if(StringUtils.isNotBlank(student.getIdentityCard())){
					if("1".equals(student.getIdentitycardType())){
						//居民身份证
						String result = Validators.validateIdentityCard(student.getIdentityCard(), true);
	        			if(StringUtils.isNotBlank(result)){
	        				return error("不是有效的身份证号");
	        			}
					}
				}else{
					return error("身份证号为空");
				}
				List<Student> list = new ArrayList<Student>();
				list.add(student);
				
				studentService.updateIdCard(list);
			}else{
				return error("该学生不存在");
			}
		}catch (Exception e) {
			e.printStackTrace();
			return returnError("保存失败！", e.getMessage());
		}
		return success("");
	}
	
	@ResponseBody
	@RequestMapping(value="/student/stuCardSaveAll",method = {RequestMethod.POST })
	public String stuCardEditingAll(@RequestBody List<Student> data,HttpServletResponse response,HttpSession httpSession) {
		try{
			List<Student> list = new ArrayList<Student>();
			boolean mess1=false;//身份证类型为空
			boolean mess2=false;//身份证号为空
			boolean mess3=false;//不是有效的身份证号
			boolean mess4=false;//该学生不存在
			if(CollectionUtils.isNotEmpty(data)){
				for(Student stu:data){
					if(StringUtils.isNotBlank(stu.getId())){
						if(StringUtils.isBlank(stu.getIdentityCard())){
							if(!mess1){
								mess1=true;
							}
							continue;
						}
						if(StringUtils.isNotBlank(stu.getIdentityCard())){
							if("1".equals(stu.getIdentitycardType())){
								//居民身份证
								String result = Validators.validateIdentityCard(stu.getIdentityCard(), true);
			        			if(StringUtils.isNotBlank(result)){
			        				if(!mess3){
										mess3=true;
									}
									continue;
			        			}
							}
						}else{
							if(!mess2){
								mess2=true;
							}
							continue;
						}
					}else{
						if(!mess4){
							mess4=true;
						}
						continue;
					}
					list.add(stu);
				}
			}
			if(CollectionUtils.isEmpty(list)){
				return error("没有要保存的数据");
			}
			
			studentService.updateIdCard(list);
			if(mess1 || mess2 || mess3 || mess4){
				success("部分操作成功");
			}
		}catch (Exception e) {
			e.printStackTrace();
			return returnError("保存失败！", e.getMessage());
		}
		return success("");
	}
	
}
