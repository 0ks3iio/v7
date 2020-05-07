/*
 * Project: v7
 * Author : shenke
 * @(#) ClassFlow.java Created on 2016-9-27
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.basedata.action;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.TypeReference;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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

import net.zdsoft.basedata.dto.ClassFlowDto;
import net.zdsoft.basedata.dto.ImportEntityDto;
import net.zdsoft.basedata.dto.StudentDto;
import net.zdsoft.basedata.entity.ClassFlow;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.ImportEntity;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.service.ClassFlowService;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.ImportService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;

/**
 * @description: 校内转班
 * @author: shenke
 * @version: 1.0
 * @date: 2016-9-27下午1:42:25
 */
@Controller
@RequestMapping("/basedata")
public class ClassFlowAction extends BaseAction {

	private static final String DATE_PATTERN = "yyyy-MM-dd";
	
	@Autowired
	private StudentService studentService;
	@Autowired
	private ClassService classService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private ClassFlowService classFlowService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private ImportService importService;
	@Autowired
	private UserService userService;
	
	// 首页
	@RequestMapping("/classflow/index/page")
	public String execute(ModelMap map) {
		map.put("unitId", getLoginInfo().getUnitId());
		map.put("isEdu",getLoginInfo().getUnitClass()!=Unit.UNIT_CLASS_EDU);
		return "/basedata/class/classflow/classflowIndex.ftl";
	}

	// 搜索列表
	@RequestMapping("/{unitId}/classflow/{type}/list/page")
	public String search(@PathVariable("type") String type,
			@PathVariable("unitId") final String unitId, String startDateStr, 
			String endDateStr,final String studentName,ModelMap map) {
		String pageName = "";
		try {
			Pagination page = createPagination();
			if(StringUtils.equals(type, "1")){
				pageName = "classflowList.ftl";
				page.setPageSize(8);
				List<StudentDto> dtos = Lists.newArrayList();
				
				
				List<Student> students = studentService.findAll(new Specification<Student>() {
					@Override
					public Predicate toPredicate(Root<Student> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {
						List<Predicate> ps = Lists.newArrayList();
						if(StringUtils.isNotEmpty(studentName)){
							ps.add(cb.like(root.<String>get("studentName"), studentName));
						}
						ps.add(cb.equal(root.<Integer>get("isDeleted"), 0));
						ps.add(cb.equal(root.<String>get("schoolId"), unitId));
						ps.add(cb.notEqual(root.<String>get("nowState"), "99"));
						ps.add(cb.notEqual(root.<Integer>get("isFreshman"), "-1"));
						ps.add(cb.notEqual(root.<Integer>get("isLeaveSchool"),Student.STUDENT_LEAVE));
						return query.where(ps.toArray(new Predicate[0])).getRestriction();
					}
				}, page);
				
				List<String> studentIds = EntityUtils.getList(students, "classId");
				List<Clazz> clazzs = classService.findListByIn("id", studentIds.toArray(new String[0]));
				Map<String,Clazz> clazzMap = EntityUtils.getMap(clazzs, "id", StringUtils.EMPTY);
				
				List<String> gradeIds = EntityUtils.getList(clazzs, "gradeId");
				List<Grade> grades = gradeService.findListByIdIn(gradeIds.toArray(new String[0]));
				Map<String,Grade> gradeMap = EntityUtils.getMap(grades, "id", StringUtils.EMPTY);
				
				Clazz clazz = null;
				for(Student student : students){
					StudentDto dto = new StudentDto();
					dto.setStudent(student);
					
					clazz = clazzMap.get(student.getClassId());
					if(clazz!=null){
						dto.setClassName(clazz.getClassName());
						if(gradeMap.get(clazz.getGradeId())!=null){
							dto.setClassName(gradeMap.get(clazz.getGradeId()).getGradeName()+dto.getClassName());
						}
					}
					dtos.add(dto);
				}
				
				map.put("datas", dtos);
			}else if(StringUtils.equals(type, "2")){
				pageName = "classflowHistoryList.ftl";
				List<ClassFlow> datas = Lists.newArrayList();
				List<ClassFlow> flows = logsSearch(studentName, unitId, startDateStr, endDateStr, page);
				if(CollectionUtils.isNotEmpty(flows)){
					datas.addAll(packageDatas(unitId, flows));
				}
				map.put("datas", datas);
			}
			map.put("pagination", page);
			map.put("unitId", unitId);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return "/basedata/class/classflow/"+pageName;
	}

	private List<ClassFlow> logsSearch(final String studentName,final String unitId,String startDateStr, String endDateStr,Pagination page) throws ParseException{
		List<Student> students = Lists.newArrayList();
		if(StringUtils.isNotEmpty(studentName)){
			
			students = studentService.findAll(new Specification<Student>() {
				@Override
				public Predicate toPredicate(Root<Student> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> ps = Lists.newArrayList();
					ps.add(cb.like(root.<String>get("studentName"), "%"+studentName+"%"));
					ps.add(cb.equal(root.<Integer>get("isDeleted"), 0));
					ps.add(cb.equal(root.<String>get("schoolId"), unitId));
					return query.where(ps.toArray(new Predicate[0])).getRestriction();
				}
			});
		}
		
		List<String> ids = EntityUtils.getList(students, "id");
		final List<String> studentIds = StringUtils.isNotEmpty(studentName)&&CollectionUtils.isEmpty(ids)?null:ids;
		
		Date startDate = null;
		if(StringUtils.isNotEmpty(startDateStr))
			startDate = DateUtils.parseDate(startDateStr, DATE_PATTERN);
		Date endDate = null;
		if(StringUtils.isNotEmpty(endDateStr)){
			endDate = DateUtils.parseDate(endDateStr, DATE_PATTERN);
			endDate = DateUtils.addDays(endDate, 1);
		}
		
		return getLogs(startDate,endDate,studentIds,unitId,page);
	}
	
	private List<ClassFlow> packageDatas(String unitId, List<ClassFlow> flows){
		
		List<ClassFlow> dtos = Lists.newArrayList();
		//数据封装
		List<Clazz> clazzs = classService.findAll(this.<Clazz>getListSpecification(unitId, null,null));
		Map<String,Clazz> clazzMap = EntityUtils.getMap(clazzs, "id", StringUtils.EMPTY);
		//List<Grade> grades = gradeService.findAll(this.<Grade>getListSpecification(unitId, null));
		//Map<String,Grade> gradeMap = EntityUtils.getMap(grades, "id", StringUtils.EMPTY);
		
		List<String> studentId2s = EntityUtils.getList(flows, "studentId");
		List<Student> student2s = studentService.findListByIdIn(studentId2s.toArray(new String[0]));
		Map<String,Student> studentMap = EntityUtils.getMap(student2s, "id", StringUtils.EMPTY);
		
		Clazz clazz = null;
		//Grade grade = null;
		Student studentTemp = null;
		for(ClassFlow classFlow : flows){
			ClassFlowDto dto = new ClassFlowDto();
			dto = EntityUtils.copyProperties(classFlow, dto);
			clazz = clazzMap.get(classFlow.getNewClassId());
			if(clazz != null){
				//grade = gradeMap.get(clazz.getGradeId());
				//if(grade != null){
				//	dto.setNewClassName(grade.getGradeName()+clazz.getClassName());
				//}
				dto.setNewClassName(getGradeName(classFlow.getAcadyear(),classFlow.getSemester(),clazz)+clazz.getClassName());
				
			}
			clazz = clazzMap.get(classFlow.getOldClassId());
			if(clazz != null){
				//grade = gradeMap.get(clazz.getGradeId());
				//if(grade != null){
				//	dto.setOldClassName(grade.getGradeName()+clazz.getClassName());
				//}
				dto.setOldClassName(getGradeName(classFlow.getAcadyear(),classFlow.getSemester(),clazz)+clazz.getClassName());
				
			}
			studentTemp = studentMap.get(classFlow.getStudentId());
			if(studentTemp != null)
				dto.setStudentName(studentTemp.getStudentName());
			
			dtos.add(dto);
		}
		return dtos;
	}
	
	private List<ClassFlow> getLogs(final Date startDate, final Date endDate, final List<String> studentIds,final String unitId,Pagination page){
		if(studentIds == null){
			return Lists.newArrayList();
		}
		return  classFlowService.findAll(new Specification<ClassFlow>() {
				@Override
				public Predicate toPredicate(Root<ClassFlow> root, CriteriaQuery<?> query,
						CriteriaBuilder cb) {
					List<Predicate> ps = Lists.newArrayList();
					if(CollectionUtils.isNotEmpty(studentIds)){
						ps.add(root.<String>get("studentId").in(studentIds));
					}
					if(startDate != null){
						ps.add(cb.greaterThanOrEqualTo(root.<Timestamp>get("creationTime"), startDate));
					}
					if(endDate != null)
					ps.add(cb.lessThanOrEqualTo(root.<Timestamp>get("creationTime"), endDate));
					ps.add(cb.equal(root.<String>get("schoolId"), unitId));
					return query.where(ps.toArray(new Predicate[0])).orderBy(cb.desc(root.<Timestamp>get("creationTime"))).getRestriction();
				}
			}, page);
	}
	
	// 弹出页
	@RequestMapping("/classflow/student/detail/page")
	public String detail(ModelMap map,final String unitId,String studentId) {
		try {
			
			List<Grade> grades = gradeService.findAll(this.<Grade>getListSpecification(unitId,null,"gradeCode"));
			
			map.put("grades", grades);
			map.put("unitId", unitId);
			map.put("studentId", studentId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "/basedata/class/classflow/classflowDetail.ftl";
	}

	@ResponseBody
	@RequestMapping("/classflow/{gradeId}/clazz/optionhtml")
	public String getClazzOptionHtml(@PathVariable("gradeId") final String gradeId,String unitId){
		
		StringBuilder sb = new StringBuilder("<option value=\""+"\">请选择班级</option>");
		try {
			List<Clazz> clazzs = classService.findAll(this.<Clazz>getListSpecification(unitId,gradeId,"className"));
			for(Clazz c : clazzs){
				sb.append("<option value=\"").append(c.getId() + "\">")
				  .append(c.getClassName()).append("</option>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
	@ResponseBody
	@RequestMapping("/classflow/student/flow")
	public String confirm(String studentId, String newClassId, String operateUserId) {

		try {
			if(StringUtils.isEmpty(newClassId)){
				throw new IllegalArgumentException("转入班级Id不能为空");
			}
			if(StringUtils.isEmpty(studentId)){
				throw new IllegalArgumentException("学生Id不能为空");
			}
			
			Student student = studentService.findOne(studentId);
			if(student == null){
				throw new IllegalArgumentException("该学生不存在!");
			}
			if(student.getClassId().equals(newClassId)){
				throw new IllegalArgumentException("该生已在目标班级!");
			}
			if("99".equals(student.getNowState())){
				throw new IllegalArgumentException("该生处于离校状态!");
			};
			if(StringUtils.isEmpty(operateUserId)){
				operateUserId = getLoginInfo().getUserId();
			}
			
			classFlowService.flowClass(student,newClassId,operateUserId);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return error("转班失败!"+e.getMessage());
		}
		
		return success("转入成功!");
	}

	// 历史记录搜索
	// 导出
	@ResponseBody
	@RequestMapping("/classflow/history/export")
	public void export(final String studentName, final String startDateStr, final String endDateStr,final String unitId, HttpServletResponse resp) {
		try {
			List<ClassFlow> datas = Lists.newArrayList();
			List<ClassFlow> flows = logsSearch(studentName, getUnitId(), startDateStr, endDateStr, null);
			if(CollectionUtils.isNotEmpty(flows)){
				datas.addAll(packageDatas(unitId, flows));
			}
			 
			String[] fieldTitles = new String[] { "姓名", "原班级", "转入班级",
					"转班时间", "操作人" };
			String[] propertyNames = new String[] { "studentName", "oldClassName",
					"newClassName", "creationTimeStr","operateUserName" };
			Map<String,List<ClassFlow>> records = Maps.newHashMap();
			records.put("转班记录表", datas);
			StringBuilder title = new StringBuilder("转班操作记录"); 
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(startDateStr)&&StringUtils.isNotEmpty(endDateStr)){
				title.append("("+format.format(DateUtils.parseDate(startDateStr, DATE_PATTERN))).append("-"+format.format(DateUtils.parseDate(endDateStr, DATE_PATTERN))+")");
			}
			if(StringUtils.isEmpty(startDateStr)&&StringUtils.isNotEmpty(endDateStr)){
				title.append("("+format.format(DateUtils.parseDate(endDateStr, DATE_PATTERN))+"以前)");
			}
			if(StringUtils.isNotEmpty(startDateStr)&&StringUtils.isEmpty(endDateStr)){
				title.append("("+format.format(DateUtils.parseDate(startDateStr, DATE_PATTERN))).append("-导出该表格时)");
			}
			if(StringUtils.isEmpty(startDateStr)&&StringUtils.isEmpty(endDateStr)){
				title.append("(所有)");
			}
			ExportUtils.newInstance().exportXLSFile(fieldTitles, propertyNames, records, "转班操作记录", title.toString(), resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//模板下载
	@RequestMapping("/classflow/template")
	public String downloadTemplate(){
		try{
			String path = getRequest().getSession().getServletContext().getRealPath("/basedata/class/classflow/classflow.xls");
			ExportUtils.outputFile(path,"批量转班模板.xls",getResponse());
		}catch(Exception e){
		    e.printStackTrace();
		}
		return null;
	}

	//批量导入页面
	@RequestMapping("/classflow/import/page")
	public String batchImportPage(){

		return "/basedata/class/classflow/classFlowImport.ftl";
	}

	//批量导入列表显示页面
	@RequestMapping("/classflow/import/list/page")
	public String importList(ModelMap map){
		try{
			Pagination page = createPagination();
			page.setPageSize(8);
			String unitId = getLoginInfo().getUnitId();
			//TODO
			List<ImportEntity> importEntityList = importService.findByTypeAndUnitId(ImportEntity.IMPORT_TYPE_CLASSFLOW,unitId,page);
			if(importEntityList == null){
				importEntityList = Lists.newArrayList();
			}
			List<ImportEntityDto> dtos = Lists.newArrayList();
			List<String> userIds = EntityUtils.getList(importEntityList,"importUserId");
			List<User> users = userService.findListByIdIn(userIds.toArray(new String[0]));
			Map<String,User> userMap = EntityUtils.getMap(users,"id");

			for (ImportEntity importEntity : importEntityList) {
				ImportEntityDto dto = new ImportEntityDto();
				User user = userMap.get(importEntity.getImportUserId());
				dto.setHandlerUserName(user!=null?user.getRealName():StringUtils.EMPTY);
				dto.setImportEntity(importEntity);
				if(ImportEntity.IMPORT_STATUS_ERROR.equals(importEntity.getStatus())){
					String error = importEntity.getFilePath()+importEntity.getId()+"_error."+importEntity.getFileType();
					dto.setErrorUrl(error);

				}
				else if(ImportEntity.IMPORT_STATUS_SUCCESS.equals(importEntity.getStatus())){
					String error = importEntity.getFilePath()+importEntity.getId()+"_error."+importEntity.getFileType();
					File errorF = new File(error);
					if(errorF.exists()){
						dto.setErrorUrl(error);
					}
					String success = importEntity.getFilePath()+importEntity.getId()+"_success."+importEntity.getFileType();
					File successF = new File(success);
					if(successF.exists()){
						dto.setSuccessUrl(success);
					}
				}
				dtos.add(dto);
			}
			map.put("importList", dtos);
			map.put("pagination", page);
		}catch(Exception e){
		    e.printStackTrace();
		}
		return "/basedata/class/classflow/classFlowImportList.ftl";
	}

	//导入
	@ResponseBody
	@RequestMapping("/classflow/import")
	public String batchImport(HttpServletRequest request){
		MultipartFile uploadFile = StorageFileUtils.getFile(request);
		try{
			LoginInfo loginInfo = getLoginInfo();
			classFlowService.saveImport(uploadFile,loginInfo.getUnitId(), loginInfo.getUserId());

		}catch(Exception e){
		    e.printStackTrace();
		    return error("文件上传失败"+e.getMessage());
		}
		return success("文件上传成功，稍后查看结果！");
	}

	@RequestMapping("/classflow/import/download/result")
	public void downloadResult(HttpServletResponse response, String id){
		try {
			ImportEntity importEntity = importService.findOne(id);
			if(importEntity != null) {
				String fileName = importEntity.getFileName();
				fileName = fileName + "_失败数据.xls";
				File file = new File(importEntity.getFilePath()+importEntity.getId()+"_error."+importEntity.getFileType());
				if(file.exists()) {
					ExportUtils.outputFile(file, fileName, response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getUnitId(){
		return getLoginInfo().getUnitId();
	}
	
	private <T> Specification<T> getListSpecification(final String unitId,final String gradeId, final String orderBy){
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb.equal(root.<String>get("schoolId"), StringUtils.isEmpty(unitId)?getUnitId():unitId));
				if(StringUtils.isNotEmpty(gradeId)){
					ps.add(cb.equal(root.<String>get("gradeId"), gradeId));
				}
				ps.add(cb.equal(root.<Integer>get("isDeleted"), 0));
				ps.add(cb.equal(root.<Integer>get("isGraduate"), 0));
				if(StringUtils.isNotEmpty(orderBy)){
					return query.where(ps.toArray(new Predicate[0])).orderBy(cb.asc(root.<String>get(orderBy))).getRestriction();
				}
				return query.where(ps.toArray(new Predicate[0])).getRestriction();
			}
		};
	}
	
	/**
	 * 获取给定学年学期的年级名称
	 * @param acadyear
	 * @param semester
	 * @param
	 * @return
	 */
	private String getGradeName(String acadyear,Integer semester,Clazz clazz){
		String openAcadyear = clazz.getAcadyear();
		String gradeName = StringUtils.EMPTY;
		try {
			int grade = 1 + NumberUtils.toInt(StringUtils.substringBefore(acadyear, "-"))
					- NumberUtils.toInt(StringUtils.substringBefore(openAcadyear, "-"));
			
			List<McodeDetail> mcodeDetails = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-RKXD-"+clazz.getSection()),new TypeReference<List<McodeDetail>>(){});
			Map<String,McodeDetail> mcMap = EntityUtils.getMap(mcodeDetails, "thisId", StringUtils.EMPTY);
			gradeName = mcMap.get(grade+"").getMcodeContent();
		} catch (Exception e) {
			e.printStackTrace();
			return StringUtils.EMPTY;
		}
		return gradeName;
		
	}


}
