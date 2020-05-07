package net.zdsoft.basedata.action;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.ImportService;
import net.zdsoft.basedata.service.SchoolService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.StudentFlowService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.StorageFileUtils;

@Controller
@RequestMapping("/basedata/studentFlowIn")
public class StudentFlowInAction extends BaseAction {

	private static final Logger logger = LoggerFactory
			.getLogger(StudentFlowInAction.class);

	@Autowired
	private StudentFlowService studentFlowService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private ClassService classService;
	@Autowired
	private SemesterService semesterService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private UserService userService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private ImportService importService;

	@RequestMapping("/index/page")
	public String index(ModelMap map) {
		map.put("isUseVerifyCode", isUseVerifyCode());
		map.put("isUseIdentityCard",isUseIdentityCard());
		return "/basedata/student/studentFlowInIndex.ftl";
	}

	/**
	 * 搜索
	 * 
	 * @param type
	 *            1->转入管理搜索，2->操作记录搜索
	 * @param map
	 * @param httpSession
	 * @return
	 */
	@RequestMapping("/searResult/{type}/page")
	public String search(@PathVariable("type") String type, ModelMap map,
			HttpSession httpSession, HttpServletRequest req, String studentname) {
		String respResult = StringUtils.EMPTY;
		List<StudentFlowDto> flows = new ArrayList<StudentFlowDto>();
		Pagination page = createPagination(req);

		try {
			if ("1".equals(type)) {

				respResult = "/basedata/student/studentFlowInResult.ftl";

				String identityCard = req.getParameter("identityCard");
				String pin = req.getParameter("pin");
				List<Student> sts = getStudentsByIdAndNameAndLeaveSchool(
						studentname.trim(), identityCard.trim());
				if (CollectionUtils.isEmpty(sts)) {
					map.put("errorMsg", "该学生不存在");
					map.put("studentDtos", flows);
					return respResult;
				}

				if(!isUseIdentityCard()) {
					List<Student> newStudents = Lists.newArrayList();
					for (Student sttemp : sts) {
						if (sttemp.getIsLeaveSchool() == Student.STUDENT_NORMAL) {
							continue;
						}
						newStudents.add(sttemp);
					}

					sts.clear();
					if(CollectionUtils.isEmpty(newStudents)){
						map.put("errorMsg", "该学生已被转入或转出");
						map.put("studentDtos", flows);
						return respResult;
					}
					sts.addAll(newStudents);
				}
				if (CollectionUtils.isEmpty(sts)) {
					map.put("errorMsg", "该学生不存在");
					map.put("studentDtos", flows);
					return respResult;
				}
				Student sttemp2 = sts.get(0);

				// 是否启用转入验证码
				if (isUseVerifyCode()) {
					if ((sttemp2 = isExists(sts, pin)) == null) {
						map.put("errorMsg", "验证码错误！");
						map.put("studentDtos", flows);
						return respResult;
					}
				}
				if(isUseIdentityCard()) {
					if (sts.get(0).getIsLeaveSchool() == Student.STUDENT_NORMAL) {
						map.put("errorMsg", "该学生已被转入或并未转出！");
						map.put("studentDtos", flows);
						return respResult;
					}
				}



				for(Student sttemp : sts){
					StudentFlowDto dto = new StudentFlowDto();
					dto.setStudent(sttemp);
					Clazz clazz = classService.findOne(sttemp.getClassId());
					Grade grade = null;
					if (clazz != null) {
						grade = gradeService.findOne(clazz.getGradeId());
					}
					Unit unit = unitService.findOne(sttemp.getSchoolId());

					StudentFlow flow = new StudentFlow();
					flow.setFlowType(StudentFlow.STUDENT_FLOW_IN);
					flow.setPin(sttemp.getVerifyCode());
					flow.setSchoolName(unit != null ? unit.getUnitName()
							: StringUtils.EMPTY);
					flow.setClassName(grade != null ? (grade.getGradeName() + clazz
							.getClassName()) : StringUtils.EMPTY);
					dto.setStudentFlow(flow);
					flows.add(dto);
				}
				if(!isUseIdentityCard()) {
					page.setPageSize(4);
					page.setMaxRowCount(flows.size());
					int end = page.getPageIndex() * page.getPageSize();

					flows = flows.subList((page.getPageIndex() - 1) * page.getPageSize(), end > flows.size() ? flows.size() : end);
					page.initialize();
					map.put("pagination", page);
				}
				map.put("isUseIdentityCard",isUseIdentityCard());
				map.put("studentDtos", flows);
			} else if ("2".equals(type)) {
				respResult = "/basedata/student/studentFlowHistory.ftl";

				String startDateStr = req.getParameter("startDate");
				String endDateStr = req.getParameter("endDate");
				Date startDate = new Date(100, 1, 1);
				startDate = StringUtils.isNotBlank(startDateStr) ? DateUtils
						.parseDate(startDateStr, new String[] { "yyyy-MM-dd" })
						: null;
				Date endDate = new Date();
				endDate = StringUtils.isNotBlank(endDateStr) ? DateUtils
						.parseDate(endDateStr + " 23:59:59",
								new String[] { "yyyy-MM-dd HH:mm:ss" })
						: null;
				flows = studentFlowService.searchFlowLogs(
						StringUtils.trim(studentname), startDate, endDate,
						StudentFlow.STUDENT_FLOW_IN, page,
						getLoginInfo(httpSession).getUnitId(),
						isEdu(httpSession));
				map.put("studentDtos", flows);
				map.put("pagination", page);

				map.put("studentFlowType", StudentFlow.STUDENT_FLOW_IN);
			}

		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}
		return respResult;
	}

	/**
	 * 详细页面
	 * 
	 * @param httpSession
	 * @param map
	 * @param studentId
	 * @return
	 */
	@RequestMapping("/detail/page/{studentId}")
	public String flowinPage(HttpSession httpSession, ModelMap map,
			@PathVariable("studentId") String studentId, String pinCode) {
		try {
			Student student = studentService.findOne(studentId);
			if (student != null
					&& Student.STUDENT_NORMAL == student.getIsLeaveSchool()) {
				map.put("errorMsg", "该学生已转入，请到操作记录页面查看！");
				return "/basedata/student/studentFlowInDetail.ftl";
			}
			StudentFlowDto dto = new StudentFlowDto();
			dto.setStudent(student);

			String unitId = getLoginInfo(httpSession).getUnitId();
			School school = schoolService.findOne(unitId);
			Unit unit = unitService.findOne(unitId);
			String schoolName = school != null ? school.getSchoolName()
					: StringUtils.EMPTY;

			// 学校
			if (getLoginInfo(httpSession).getUnitClass() == Unit.UNIT_CLASS_SCHOOL) {
				map.put("schoolName", schoolName);
				map.put("schoolId", unitId);

				// 当前学校可以选择班级
				map.put("classList", getClassList(unitId, false));
			}
			//
			else {
				List<Unit> underEdus = unitService.findDirectUnitsByParentId(
						unitId, Unit.UNIT_CLASS_EDU);
				if (CollectionUtils.isEmpty(underEdus)) {
					map.put("unitName", schoolName);
					map.put("eduList", Lists.newArrayList());
					map.put("schoolList", unitService
							.findDirectUnitsByParentId(unitId,
									Unit.UNIT_CLASS_SCHOOL));
				} else {
					underEdus.add(unitService.findOne(unitId));
					map.put("eduList", underEdus);
					List<Unit> schoolList = unitService
							.findDirectUnitsByParentId(
									underEdus.get(0).getId(),
									Unit.UNIT_CLASS_SCHOOL);
					map.put("schoolList",
							CollectionUtils.isNotEmpty(schoolList) ? schoolList
									: Lists.newArrayList());
					// map.put("classList",CollectionUtils.isNotEmpty(schoolList)?getClassList(schoolList.get(0).getId(),false):Lists.newArrayList());
				}
			}
			map.put("studentDto", dto);
			map.put("pin", pinCode);
			map.put("isSch",
					getLoginInfo(httpSession).getUnitClass() == Unit.UNIT_CLASS_SCHOOL);
			// map.put("isSch", false);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[" + this.getClass().getName() + ".flowinPage(...)]",
					e);
		}
		return "/basedata/student/studentFlowInDetail.ftl";
	}

	/**
	 * 转入确认
	 * 
	 * @param httpSession
	 * @param studentFlow
	 * @return
	 */
	@RequestMapping("/detail/confirm")
	@ResponseBody
	public String flowIn(HttpSession httpSession,
			@RequestBody StudentFlow studentFlow, HttpServletRequest req) {
		try {
			Student st1 = studentService.findOne(studentFlow.getStudentId());
			if (st1.getIsLeaveSchool() == 0) {
				return error(" 该学生并未离校，不能转入! ");
			}
			studentFlow.setHandleUserId(getLoginInfo(httpSession).getUserId());
			studentFlow.setFlowType(StudentFlow.STUDENT_FLOW_IN);
			Semester semester = semesterService.getCurrentSemester(1);
			studentFlow.setSemester(semester.getSemester());
			studentFlow.setAcadyear(semester.getAcadyear());
			studentFlowService.save(st1, studentFlow);
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
			return error("转入失败！");
		}
		// studentFlowService.saveOne(t);
		// return error("转入失败！");
		return success("转入成功！");
	}

	/**
	 * 导出导入记录
	 * 
	 * @param studentname
	 * @param startDate
	 * @param endDate
	 */
	@RequestMapping("/export/logs")
	@ResponseBody
	public void exportLogs(String studentname, HttpServletRequest req,
			HttpSession httpSession, HttpServletResponse resp) {
		try {
			ExportUtils exportUtils = ExportUtils.newInstance();
			String startDateStr = req.getParameter("startDate");
			String endDateStr = req.getParameter("endDate");
			Date startDate = new Date(0, 1, 1);
			startDate = StringUtils.isNotEmpty(startDateStr) ? DateUtils
					.parseDate(startDateStr, new String[] { "yyyy-MM-dd" })
					: startDate;
			Date endDate = new Date();
			endDate = StringUtils.isNotEmpty(endDateStr) ? DateUtils.parseDate(
					endDateStr + " 23:59:59",
					new String[] { "yyyy-MM-dd HH:mm:ss" }) : endDate;
			List<StudentFlowDto> dtos = studentFlowService.searchFlowLogs(
					StringUtils.trim(studentname), startDate, endDate,
					StudentFlow.STUDENT_FLOW_IN, null,
					getLoginInfo(httpSession).getUnitId(), isEdu(httpSession));
			// 姓名","身份证号","现学校","现班级","转入时间","转入原因","验证码","操作人
			String[] fieldTitles = new String[] { "姓名", "身份证件号", "现学校", "现班级",
					"转入时间", "转入原因", "验证码", "操作人" };
			String[] propertyNames = new String[] { "student.studentName",
					"student.identityCard", "studentFlow.schoolName",
					"studentFlow.className", "studentFlow.creationTime",
					"studentFlow.reason", "studentFlow.pin",
					"studentFlow.handleUserName" };
			Map<String, List<StudentFlowDto>> records = Maps.newHashMap();
			records.put("学生转入记录表", dtos);
			exportUtils.exportXLSFile(fieldTitles, propertyNames, records,
					"学生转入记录表", resp);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出Excel失败", e);
			// return error("导出失败！");
		}
		// return success("导出成功！");
	}

	@RequestMapping("/import/list")
	public String importList(HttpSession httpSession,
			HttpServletRequest request, ModelMap map) {

		final String unitId = getLoginInfo(httpSession).getUnitId();
		// int unitClass = getLoginInfo(httpSession).getUnitClass();
		Pagination page = createPagination(request);
		page.setPageSize(8);
		List<ImportEntity> importLists = importService.findAll(
				new Specification<ImportEntity>() {
					@Override
					public Predicate toPredicate(Root<ImportEntity> root,
							CriteriaQuery<?> cq, CriteriaBuilder cb) {
						List<Predicate> ps = Lists.newArrayList();
						ps.add(cb.equal(root.get("unitId").as(String.class),
								unitId));
						ps.add(cb.equal(
								root.get("importType").as(String.class),
								StudentFlow.STUDENT_FLOW_IN));
						cq.where(ps.toArray(new Predicate[0])).orderBy(
								cb.desc(root.get("creationTime").as(
										Timestamp.class)));
						return cq.getRestriction();
					}
				}, page);

		if (CollectionUtils.isEmpty(importLists)) {
			importLists = Lists.newArrayList();
		}

		List<String> userId = EntityUtils.getList(importLists, "importUserId");
		Map<String, User> userMap = userService.findMapByIdIn(Sets.newHashSet(
				userId).toArray(new String[0]));

		List<ImportEntityDto> entityDtos = Lists.newArrayList();
		for (ImportEntity entity : importLists) {
			ImportEntityDto dto = new ImportEntityDto();
			User user = userMap.get(entity.getImportUserId());
			dto.setHandlerUserName(user != null ? user.getRealName() : "未找到操作人");
			dto.setImportEntity(entity);
			if (ImportEntity.IMPORT_STATUS_ERROR.equals(entity.getStatus())) {
				String error = entity.getFilePath() + entity.getId()
						+ "_error." + entity.getFileType();
				dto.setErrorUrl(error);

			} else if (ImportEntity.IMPORT_STATUS_SUCCESS.equals(entity
					.getStatus())) {
				String error = entity.getFilePath() + entity.getId()
						+ "_error." + entity.getFileType();
				File errorF = new File(error);
				if (errorF.exists()) {
					dto.setErrorUrl(error);
				}
				String success = entity.getFilePath() + entity.getId()
						+ "_success." + entity.getFileType();
				File successF = new File(success);
				if (successF.exists()) {
					dto.setSuccessUrl(success);
				}
			}
			dto.setOriginUrl(entity.getFilePath() + entity.getId() + "."
					+ entity.getFileType());
			entityDtos.add(dto);
		}

		map.put("importList", entityDtos);
		map.put("pagination", page);
		return "/basedata/student/studentFlowImportList.ftl";

	}

	@RequestMapping("/import/page")
	public String batchImportPage(ModelMap map) {
		map.put("flowType", StudentFlow.STUDENT_FLOW_IN);
		return "/basedata/student/studentFlowImport.ftl";
	}

	@RequestMapping("/import")
	@ResponseBody
	public String batchImport(HttpSession httpSession, HttpServletRequest req,
			HttpServletResponse resp) {
		MultipartFile uploadFile = StorageFileUtils.getFile(req);
		try {
			studentFlowService.saveImport(uploadFile, getLoginInfo(httpSession)
					.getUserId(), getLoginInfo(httpSession).getUnitId(),
					StudentFlow.STUDENT_FLOW_IN);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return error("导入文件失败:" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			return error("导入文件失败:" + e.getMessage());
		}
		return success("导入文件上传成功,请稍后查看");
	}

	@RequestMapping("/template")
	public void downloadTemplate(HttpServletResponse resp,
			HttpServletRequest req) {
		String tName = "studentFlowIn.xls";
		if (isUseVerifyCode()) {
			tName = "studentFlowIn.xls";
		} else {
			tName = "studentFlowInNoVerifyCode.xls";
		}
		String path = req.getSession().getServletContext()
				.getRealPath("/basedata/student/" + tName);
		try {
			ExportUtils.outputFile(new File(path), "批量转入模板.xls", resp);
			// ExportUtils.outputFile(path, resp);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * select
	 * 
	 * @param type
	 * @param unitId
	 * @return
	 */
	@RequestMapping("/{type}/options/{otherparam}")
	@ResponseBody
	public String getSelectOptions(@PathVariable("type") String type,
			String unitId, @PathVariable("otherparam") Integer otherparam) {
		StringBuilder sb = new StringBuilder();
		List<Unit> units = null;
		List<Clazz> clazzs = null;
		if ("unit".equals(type)) {
			Assert.notNull(otherparam);
			units = unitService.findDirectUnitsByParentId(unitId, otherparam);
			units = CollectionUtils.isEmpty(units) ? new ArrayList<Unit>()
					: units;
			sb.append("<option value=\"\">请选择学校</option>");
			for (Unit u : units) {
				sb.append("<option value=\"").append(u.getId() + "\">")
						.append(u.getUnitName()).append("</option>");
			}
		} else if ("clazz".equals(type)) {
			clazzs = getClassList(unitId, false);
			sb.append("<option value=\"\">请选择班级</option>");
			for (Clazz c : clazzs) {
				sb.append("<option value=\"").append(c.getId() + "\">")
						.append(c.getClassName()).append("</option>");
			}
		}
		return sb.toString();
	}

	/**
	 * 转入搜索
	 * 
	 * @param studentname
	 * @param identityCard
	 * @param pin
	 * @return
	 */
	private List<Student> searchFlowIn(final String studentname,
			final String identityCard, final String pin, Pagination page) {

		return studentService.findAll(new Specification<Student>() {

			@Override
			public Predicate toPredicate(Root<Student> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb.equal(root.get("studentName").as(String.class),
						studentname));
				ps.add(cb.equal(root.get("identityCard").as(String.class),
						identityCard));
				ps.add(cb.equal(root.get("verifyCode").as(String.class), pin));
				cq.where(ps.toArray(new Predicate[0]));
				return cq.getRestriction();
			}
		});

		// List<StudentFlow> studentFlows =
		// studentFlowService.searchFlows(studentname, identityCard, pin,
		// StudentFlow.STUDENT_FLOW_LEAVE,page);
		// Set<String> studentIds = new HashSet<String>();
		// for (StudentFlow sf : studentFlows) {
		// studentIds.add(sf.getStudentId());
		// }
		//
		// List<StudentFlowDto> studentFlowDtos = Lists.newArrayList();
		// for (StudentFlow studentFlow : studentFlows) {
		// StudentFlowDto dto = new StudentFlowDto();
		// dto.setStudent(studentService.findOne(studentFlow.getStudentId()));
		// dto.setStudentFlow(studentFlow);
		// studentFlowDtos.add(dto);
		// }
		// return studentFlowDtos;
	}

	/**
	 * 查询指定条件的学生是否处存在
	 * 
	 * @param studentname
	 * @param identityCard
	 * @param pin
	 * @return
	 */
	private Student isExists(List<Student> sts, final String pin) {
		for (final Student st : sts) {
			if (pin.equals(st.getVerifyCode())) {
				return st;
			}
		}
		return null;
	}

	private List<Student> getStudentsByIdAndNameAndLeaveSchool(
			final String studentname, final String identityCard) {
		return studentService.findAll(new Specification<Student>() {
			@Override
			public Predicate toPredicate(Root<Student> root,
					CriteriaQuery<?> criteria, CriteriaBuilder builder) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(builder.equal(root.get("studentName").as(String.class),
						studentname));
				if(StringUtils.isNotBlank(identityCard)) {
					ps.add(builder.equal(root.get("identityCard").as(String.class),
							identityCard));
				}
				// ps.add(builder.equal(root.get("isLeaveSchool").as(Integer.class),
				// leaveSchool));
				criteria.where(ps.toArray(new Predicate[0]));
				return criteria.getRestriction();
			}
		});

	}

	/**
	 * TODO 获取某个学校下的所有班级
	 * 
	 * @param schoolId
	 * @return
	 */
	@SuppressWarnings("此处获取班级名称通过GradeName和ClassName拼装,需要判定是否中职学校")
	private List<Clazz> getClassList(final String schoolId, boolean isEisu) {
		// final Semester semester = semesterService.getCurrentSemester(1);
 		List<Clazz> clazzs = classService.findAll(new Specification<Clazz>() {
			@Override
			public Predicate toPredicate(Root<Clazz> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb
						.equal(root.get("schoolId").as(String.class), schoolId));
				ps.add(cb.equal(root.get("isGraduate").as(Integer.class),
						Clazz.NOT_GRADUATED));
				ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
				cq.where(ps.toArray(new Predicate[0]));
				return cq.getRestriction();
			}
		});
		if (isEisu) {
			return clazzs;
		}
		List<Grade> grades = gradeService.findAll(new Specification<Grade>() {

			@Override
			public Predicate toPredicate(Root<Grade> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb
						.equal(root.get("schoolId").as(String.class), schoolId));
				ps.add(cb.equal(root.get("isGraduate").as(Integer.class),
						Grade.NOT_GRADUATED));
				ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
				cq.where(ps.toArray(new Predicate[0]));
				return cq.getRestriction();
			}
		});
		Map<String, Grade> gradeMap = EntityUtils.getMap(grades, "id",
				StringUtils.EMPTY);
		for (Clazz clazz : clazzs) {
			if(gradeMap.get(clazz.getGradeId()) != null){
			clazz.setClassName(gradeMap.get(clazz.getGradeId()).getGradeName()
					+ clazz.getClassName());
			}
		}
		return clazzs;
	}

	protected boolean isEdu(HttpSession httpSession) {
		return getLoginInfo(httpSession).getUnitClass() == Unit.UNIT_CLASS_EDU;
	}

	private boolean isUseVerifyCode() {
		// String isUse = RedisUtils.get("student.flow.is.use.verifycode",new
		// RedisInterface<String>() {
		//
		// @Override
		// public String queryData() {
		// SysOption option =
		// sysOptionService.getByOptionCode("IS.USE.VERIFY.CODE");
		// System.out.println(option!=null?option.getNowValue():-1);
		// return option!=null?option.getNowValue():"1";
		// }
		// });
		// return BooleanUtils.toBoolean(NumberUtils.toInt(isUse));
		return Evn.getBoolean("is_use_verify_code");
	}

	private boolean isUseIdentityCard(){
		return Evn.getBoolean("is_use_identity_card");
	}
}
