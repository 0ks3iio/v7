package net.zdsoft.basedata.action;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.zdsoft.basedata.dto.ImportEntityDto;
import net.zdsoft.basedata.dto.StudentFlowDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.ImportEntity;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.StudentFlow;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.FamilyService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.ImportService;
import net.zdsoft.basedata.service.SchoolService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.StudentFlowService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.StorageFileUtils;

@Controller
@RequestMapping("/basedata/studentFlowOut")
public class StudentFlowOutAction extends BaseAction {

    @Autowired
    private StudentService studentService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private ClassService classService;
    @Autowired
    private StudentFlowService studentFlowService;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private FamilyService familyService;
    @Autowired
    private UserService userService;
    @Autowired
    private UnitService	unitService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private ImportService importService;
    
    @RequestMapping("/welcome")
    public String welcome() {
        return "/basedata/student/welcome.ftl";
    }
    
    @RequestMapping("/index/page")
    public String index() {
        return "/basedata/student/studentFlowOutIndex.ftl";
    }
    
    /**
     * 搜索
     * @param type 1->调入管理搜索，2->操作记录搜索
     * @param map
     * @param httpSession
     * @return
     */
    @SuppressWarnings("type==1 设置班级名称时需考虑中职(不需要增加年级名)")
    @RequestMapping("/searchResult/{type}/page")
    public String search(@PathVariable("type") String type,HttpServletRequest request, HttpSession httpSession, ModelMap map, final String studentName,
            final String identityCard) {
    	try {
	    	if("1".equals(type)){
		    	final String unitId = getLoginInfo(httpSession).getUnitId();
		    	final Integer unitClass = getLoginInfo(httpSession).getUnitClass();
		    	if(unitClass.equals(new Integer(Unit.UNIT_CLASS_EDU))){
		    		if (StringUtils.isBlank(studentName) && StringUtils.isBlank(identityCard)) {
		    			map.put("errorMessage", "请输入姓名和身份证件号！");
		    			return "/basedata/student/studentFlowResult.ftl";
		    		}
		    	}else{
		    		if (StringUtils.isBlank(studentName) && StringUtils.isBlank(identityCard)) {
		    			map.put("errorMessage", "请输入姓名或者身份证件号！");
		    			return "/basedata/student/studentFlowResult.ftl";
		    		}
		    	}
		
		        Pagination page = createPagination(request);
		        List<Student> students = null;
		        if(getLoginInfo(httpSession).getUnitClass() == Unit.UNIT_CLASS_SCHOOL){
		        	students = studentService.getStudentIsLeaveSchool(studentName, identityCard,unitId,page);
		        }
		        else{
		        	students = studentService.getStudentIsLeaveSchool(studentName, identityCard,null,page);
		        }
		        		//studentService.findAll(s, page);
		        						
		        // if (page.getMaxRowCount() > page.getPageSize()) {
		        // map.put("infoMessage", "只显示符合条件前" + page.getPageSize() +
		        // "个学生数据，请输入更加详细的查询条件(测试用)！");
		        // }
		        Set<String> schoolIds = new HashSet<String>();
		        Set<String> classIds = new HashSet<String>();
		        
		        for (Student student : students) {
		        	schoolIds.add(student.getSchoolId());
		            classIds.add(student.getClassId());
		        }
		        Map<String, School> schoolMap = schoolService.findMapByIdIn(schoolIds.toArray(new String[0]));
		        Map<String, Clazz> classMap = classService.findMapByIdIn(classIds.toArray(new String[0]));
		        // 设置班级名称是待修正(中职)
		        List<String> gradeIds = EntityUtils.getList(Lists.newArrayList(classMap.values()), "gradeId");
		        Map<String,Grade> gradeMap = gradeService.findMapByIdIn(Sets.newHashSet(gradeIds).toArray(new String[0]));
		        
		        List<StudentFlowDto> studentFlowDtos = new ArrayList<StudentFlowDto>();
		        for (Student student : students) {
		        	if("99".equals(student.getNowState())){
		        		continue;
		        	}
		            StudentFlowDto flowDto = new StudentFlowDto();
		            flowDto.setStudent(student);
		            School school = schoolMap.get(student.getSchoolId());
		            Clazz clazz = classMap.get(student.getClassId());
		            Grade grade = gradeMap.get(clazz.getGradeId());
		            flowDto.setSchoolName(school == null ? "" : school.getSchoolName());
		            String gradeName = grade!=null?grade.getGradeName():"";
		            flowDto.setClassName(gradeName+ (clazz == null?"":clazz.getClassName()));
		            studentFlowDtos.add(flowDto);
		        }
		        if (students.size() <= 0) {
		            map.put("errorMessage", "没找到符合条件的学生！");
		            return "/basedata/student/studentFlowResult.ftl";
		        }
		        map.put("students", studentFlowDtos);
		        map.put("pagination", page);
		        return "/basedata/student/studentFlowResult.ftl";
	    	}else if("2".equals(type)){
	    		Pagination page = createPagination(request);
	    		String startDateStr = request.getParameter("startDate");
        		String endDateStr = request.getParameter("endDate");
        		Date startDate = new Date(0,1,1);
        		startDate = StringUtils.isNotBlank(startDateStr)?DateUtils.parseDate(startDateStr, new String[]{"yyyy-MM-dd"}):null;
        		Date endDate = new Date();
        		endDate = StringUtils.isNotBlank(endDateStr)?DateUtils.parseDate(endDateStr+" 23:59:59", new String[]{"yyyy-MM-dd HH:mm:ss"}):null;
				List<StudentFlowDto> flows = studentFlowService.searchFlowLogs(StringUtils.trim(studentName), startDate, endDate,StudentFlow.STUDENT_FLOW_LEAVE,page,getLoginInfo(httpSession).getUnitId(),isEdu(httpSession));
				map.put("studentDtos", flows);
				map.put("pagination", page);
				map.put("studentFlowType", StudentFlow.STUDENT_FLOW_LEAVE);
				return  "/basedata/student/studentFlowHistory.ftl";
			}else{
				throw new RuntimeException("没有找到对应的页面");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return StringUtils.EMPTY;
    }
    
    @ResponseBody    
    @RequestMapping("/searchResult/leaveSchool")
    public String leaveSchool(HttpServletRequest request, HttpSession httpSession, ModelMap map, final String studentId,
            final String schoolName,final String className,final String reason) {
    	if(StringUtils.isBlank(studentId)||StringUtils.isBlank(className)||StringUtils.isBlank(schoolName)){
    		return error("转出出错,请刷新页面重试");
    	}
    	Student student = studentService.findOne(studentId);
    	if(new Integer(0).equals(student.getIsLeaveSchool())||(StringUtils.isEmpty(student.getVerifyCode())&& new Integer(1).equals(student.getIsLeaveSchool()))){
    		StudentFlow studentFlow = new StudentFlow();
    		studentFlow.setId(student.getId());
    		studentFlow.setStudentId(studentId);
    		studentFlow.setSchoolId(student.getSchoolId());
    		studentFlow.setSchoolName(schoolName);
    		studentFlow.setClassId(student.getClassId());
    		studentFlow.setClassName(className);
    		Semester semester = semesterService.getCurrentSemester(1);
			if(semester!=null){
				studentFlow.setAcadyear(semester.getAcadyear());
				studentFlow.setSemester(semester.getSemester());
			}
    		studentFlow.setFlowType(StudentFlow.STUDENT_FLOW_LEAVE);
    		studentFlow.setHandleUserId(getLoginInfo(httpSession).getUserId());
    		studentFlow.setReason(reason);
    		studentFlowService.save(student,studentFlow);
    	}else{
			return error("转出出错,该学生已处于转出状态");
    	}
    	String pin ="";
    	return success("转出操作成功!学生:"+student.getStudentName()+"已成功转出,该学生转出验证码为:"+student.getVerifyCode());
    }
    
    @RequestMapping("/export/logs")
    @ResponseBody
    public void exportLogs(String studentName, HttpServletRequest req, HttpSession httpSession,HttpServletResponse resp){
    	try {
    		ExportUtils exportUtils = ExportUtils.newInstance();
    		String startDateStr = req.getParameter("startDate");
    		String endDateStr = req.getParameter("endDate");
    		Date startDate = new Date(0,1,1);
    		startDate = StringUtils.isNotEmpty(startDateStr)?DateUtils.parseDate(startDateStr, new String[]{"yyyy-MM-dd"}):startDate;
    		Date endDate = new Date();
    		endDate = StringUtils.isNotEmpty(endDateStr)?DateUtils.parseDate(endDateStr+" 23:59:59", new String[]{"yyyy-MM-dd HH:mm:ss"}):endDate;
    		List<StudentFlowDto> dtos = studentFlowService.searchFlowLogs(StringUtils.trim(studentName), startDate, endDate, StudentFlow.STUDENT_FLOW_LEAVE,null, getLoginInfo(httpSession).getUnitId(), isEdu(httpSession));
    		//姓名","身份证号","现学校","现班级","调入时间","调入原因","验证码","操作人
    		String[] fieldTitles = new String[]{"姓名","身份证件号","原学校","原班级","转出时间","转出原因","验证码","操作人"};
    		String[] propertyNames = new String[]{"student.studentName","student.identityCard",
    				"studentFlow.schoolName","studentFlow.className","studentFlow.creationTime",
    				"studentFlow.reason","studentFlow.pin","studentFlow.handleUserName"};
    		Map<String,List<StudentFlowDto>> records = Maps.newHashMap();
    		records.put("学生转出记录表", dtos);
    		exportUtils.exportXLSFile(fieldTitles, propertyNames, records, "学生转出记录表", resp);
		} catch (Exception e) {
			e.printStackTrace();
			//logger.error("导出Excel失败",e);
			//return error("导出失败！");
		}
    	//return success("导出成功！");
    }
    
    @RequestMapping("/import/list")
    public String importList(HttpSession httpSession,HttpServletRequest request, ModelMap map){
    	
    	final String unitId = getLoginInfo(httpSession).getUnitId();
    	//int unitClass = getLoginInfo(httpSession).getUnitClass();
    	Pagination page = createPagination(request);
    	page.setPageSize(8);
    	List<ImportEntity> importLists = importService.findAll(new Specification<ImportEntity>() {
				@Override
				public Predicate toPredicate(Root<ImportEntity> root,
						CriteriaQuery<?> cq, CriteriaBuilder cb) {
					List<Predicate> ps = Lists.newArrayList();
					ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
					ps.add(cb.equal(root.get("importType").as(String.class), StudentFlow.STUDENT_FLOW_LEAVE));
					cq.where(ps.toArray(new Predicate[0])).orderBy(cb.desc(root.get("creationTime").as(Timestamp.class)));
					return cq.getRestriction();
				}
			}, page);
    	
    	if(CollectionUtils.isEmpty(importLists)){
    		importLists = Lists.newArrayList();
    	}
    	
    	List<String> userId = EntityUtils.getList(importLists, "importUserId");
    	Map<String,User> userMap = userService.findMapByIdIn(Sets.newHashSet(userId).toArray(new String[0]));
    	
    	List<ImportEntityDto> entityDtos = Lists.newArrayList();
    	for(ImportEntity entity: importLists){
    		ImportEntityDto dto = new ImportEntityDto();
    		User user = userMap.get(entity.getImportUserId());
    		dto.setHandlerUserName(user!=null?user.getRealName():"未找到操作人");
    		dto.setImportEntity(entity);
    		if(ImportEntity.IMPORT_STATUS_ERROR.equals(entity.getStatus())){
    			String error = entity.getFilePath()+entity.getId()+"_error."+entity.getFileType();
    			dto.setErrorUrl(error);
    			
    		}
    		else if(ImportEntity.IMPORT_STATUS_SUCCESS.equals(entity.getStatus())){
	    		String error = entity.getFilePath()+entity.getId()+"_error."+entity.getFileType();
	    		File errorF = new File(error);
	    		if(errorF.exists()){
	    			dto.setErrorUrl(error);
	    		}
	    		String success = entity.getFilePath()+entity.getId()+"_success."+entity.getFileType();
	    		File successF = new File(success);
	    		if(successF.exists()){
	    			dto.setSuccessUrl(success);
	    		}
    		}
    		dto.setOriginUrl(entity.getFilePath()+entity.getId()+"."+entity.getFileType());
    		entityDtos.add(dto);
    	}
    	
    	map.put("importList", entityDtos);
    	map.put("pagination", page);
    	return "/basedata/student/studentFlowImportList.ftl";
    	
    }
    
    @RequestMapping("/template")
    public void downloadTemplate(HttpServletResponse resp, HttpServletRequest req){
    	String path = req.getSession().getServletContext().getRealPath("/basedata/student/studentFlowOut.xls");
    	try {
    		ExportUtils.outputFile(new File(path), "批量转出模板.xls", resp);
			//ExportUtils.outputFile(path, resp);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @RequestMapping("/import/download/result")
    public void downloadResult(HttpServletResponse resp, HttpServletRequest req, String path, String fileName,String type){
    	try {
    		fileName = fileName+(ImportEntity.IMPORT_STATUS_ERROR.equals(type.trim())?"_失败数据.xls":"_正确数据.xls");
    		ExportUtils.outputFile(new File(path), fileName, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @RequestMapping("/import/page")
    public String batchImportPage(ModelMap map){
    	map.put("flowType", StudentFlow.STUDENT_FLOW_LEAVE);
    	return "/basedata/student/studentFlowImport.ftl";
    }
    
    @ResponseBody
    @RequestMapping("/import")
    public String batchImport(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response){
    	
    	MultipartFile uploadFile = StorageFileUtils.getFile(request);
//    	return studentFlowService.batchImport(uploadFile, getLoginInfo(httpSession).getUserId(), getLoginInfo(httpSession).getUnitId(), StudentFlow.STUDENT_FLOW_LEAVE);
    	try {
			studentFlowService.saveImport(uploadFile, getLoginInfo(httpSession).getUserId(), getLoginInfo(httpSession).getUnitId(), StudentFlow.STUDENT_FLOW_LEAVE);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return error("导入文件失败:"+e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			return error("导入文件失败:"+e.getMessage());
		}
    	return success("导入文件上传成功,请稍后查看");
    }
    
	private List<Student> getStudentsByNameAndIdCard(final List<String> studentNames, final List<String> identityCards){
    	return studentService.findAll(new Specification<Student>() {

			@Override
			public Predicate toPredicate(Root<Student> root,
					CriteriaQuery<?> criteria, CriteriaBuilder builder) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(builder.in(root.get("studentName").as(String.class).in(studentNames.toArray())));
				ps.add(builder.in(root.get("identityCard").as(String.class).in(identityCards.toArray())));
				criteria.where(ps.toArray(new Predicate[0]));
				return criteria.getRestriction();
			}
		});
    }
    
    private List<School> getSchools(final List<String> schoolNames){
    	return schoolService.findAll(new Specification<School>() {
			@Override
			public Predicate toPredicate(Root<School> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				cq.where(cb.in(root.get("schoolName").as(String.class).in(schoolNames)));
				return cq.getRestriction();
			}
		});
    }
    
    private List<Clazz> getClazz(final List<String> schIds, final String acadyear) {
		return classService.findAll(new Specification<Clazz>() {
			@Override
			public Predicate toPredicate(Root<Clazz> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb.in(root.get("schoolId").as(String.class).in(schIds)));
				ps.add(cb.equal(root.get("acadyear").as(String.class), acadyear));
				cq.where(ps.toArray(new Predicate[0]));
				return cq.getRestriction();
			}
		});
	}
    protected boolean isEdu(HttpSession httpSession){
		return getLoginInfo(httpSession).getUnitClass() == Unit.UNIT_CLASS_EDU;
	}
    
    @RequestMapping("/remote")
    public void remoteOutSchool(HttpSession httpSession,HttpServletResponse resp, HttpServletRequest req,String unitId,String[] studentIds){
    	studentFlowService.remoteOutSchool(unitId, studentIds);
    }
}
