package net.zdsoft.tutor.data.action.teacher;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.popup.BaseDivAction;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PinyinUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import net.zdsoft.tutor.data.constant.TutorConstants;
import net.zdsoft.tutor.data.dto.TutorShowRecordDto;
import net.zdsoft.tutor.data.entity.TutorRecord;
import net.zdsoft.tutor.data.entity.TutorRecordDetailed;
import net.zdsoft.tutor.data.entity.TutorResult;
import net.zdsoft.tutor.data.service.TutorRecordDetailedService;
import net.zdsoft.tutor.data.service.TutorRecordService;
import net.zdsoft.tutor.data.service.TutorResultService;

/**
 * @author yangsj 2017年11月20日上午10:20:11
 */
@Controller
@RequestMapping("/tutor/record/manage")
public class TutorRecordManageAction extends BaseDivAction {

	private static String student_recent_key = "tutorstudent-popup-recent-";
	@Autowired
	private TutorResultService tutorResultService;
	@Autowired
	private TutorRecordService tutorRecordService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private TutorRecordDetailedService tutorRecordDetailedService;

	@RequestMapping("/popupData")
	@ResponseBody
	public String showPopUpData() {
		String dataJson = null;
		if (dataJson == null) {
			List<String[]> dataList = new LinkedList<String[]>();
			List<Student> students = getTutorAllStudent();

			int level = 1;
			int startCode = 10000;
			for (Student stu : students) {
				startCode++;
				String[] data = new String[9];
				data[0] = stu.getId();
				data[1] = stu.getStudentName();
				data[2] = stu.getStudentName();
				data[3] = PinyinUtils.toHanyuPinyin(stu.getStudentName(), false);
				data[4] = PinyinUtils.toHanyuPinyin(stu.getStudentName(), true);
				data[5] = TYPE_DATA;
				data[6] = Constant.GUID_ZERO;
				data[7] = String.valueOf(level);
				data[8] = String.valueOf(startCode);
				dataList.add(data);
			}
			dataJson = JsonArray.toJSON(dataList).toString();
		}
		List<String> recentDataList = RedisUtils.queryDataFromList(student_recent_key + getLoginInfo().getUserId(),
				true);
		if (CollectionUtils.isEmpty(recentDataList))
			recentDataList = new ArrayList<String>();
		String recentDataJson = JsonArray.toJSON(recentDataList).toString();
		List<String> resultList = new LinkedList<String>();
		resultList.add(dataJson);
		resultList.add(recentDataJson);
		return JsonArray.toJSON(resultList).toString();
	}

	@RequestMapping("/recentData")
	@ResponseBody
	@ControllerInfo(value = "获取最近的数据", parameter = "{ids}")
	public String putRecentPopUpData(String ids) {
		String[] studentIds = ids.split(",");
		for (int i = 0; i < studentIds.length; i++) {
			RedisUtils.addDataToList(student_recent_key + getLoginInfo().getUserId(), studentIds[i], MAX_COUNT);
		}
		return "";
	}

	/**
	 * @return
	 */
	private List<Student> getTutorAllStudent() {
		User user = SUtils.dc(userRemoteService.findOneById(getLoginInfo().getUserId(), true), User.class);
		// 得出当前导师下的所有学生
		String teacherId = user.getOwnerId();
		List<TutorResult> listTR = tutorResultService.findByTeacherId(teacherId);
		List<String> studentIds = EntityUtils.getList(listTR, TutorResult::getStudentId);
		List<Student> students = SUtils
				.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[studentIds.size()])), Student.class);
		return students;
	}

	@RequestMapping("/doRecordtab")
	@ControllerInfo("操作记录")
	public String doRecordtab(ModelMap map) {
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()),
				Semester.class);
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
		});
		// 记录类型
		List<McodeDetail> recordTypes = SUtils
				.dt(mcodeRemoteService.findByMcodeIds(TutorConstants.TUTOR_RECORD_TYPE_MCODE_ID), McodeDetail.class);
		map.put("recordTypes", recordTypes);
		map.put("acadyearList", acadyearList);
		map.put("semester", semester);
		return "/tutor/teacher/manage/tutorRecordtab.ftl";
	}

	@RequestMapping("/doRecordList")
	@ControllerInfo("查看记录")
	public String doRecordList(HttpServletRequest request, ModelMap map, String acadyear, String semester,
			String recordType, String isAll,String teacherName) {
		Pagination page = createPagination();
		Map<String, String> paramMap = syncParameters(request);
		int row = NumberUtils.toInt(paramMap.get("_pageSize"));
		if (row <= 0) {
			page.setPageSize(20);
		}
		List<TutorShowRecordDto> tutorShowRecordDtos = new ArrayList<TutorShowRecordDto>();
		List<TutorRecordDetailed> tutorRecordDetaileds = new ArrayList<>();
		if (StringUtils.isEmpty(isAll)) {
			User user = SUtils.dc(userRemoteService.findOneById(getLoginInfo().getUserId(), true), User.class);
			String teacherId = user.getOwnerId();
			tutorRecordDetaileds = tutorRecordDetailedService.findBySIdAndSemester(
					getLoginInfo().getUnitId(), acadyear, semester, recordType, page,teacherId);
		} else {
			if(StringUtils.isNotBlank(teacherName)) {
				List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByTeacherNameLike(teacherName),Teacher.class);
				Set<String> teacherIds = EntityUtils.getSet(teachers, Teacher::getId);
				if(CollectionUtils.isNotEmpty(teacherIds)) {
					tutorRecordDetaileds = tutorRecordDetailedService.findBySIdAndSemester(
							getLoginInfo().getUnitId(), acadyear, semester, recordType, page,
							teacherIds.toArray(new String[teacherIds.size()]));
				}
			}else {
				tutorRecordDetaileds = tutorRecordDetailedService.findBySIdAndSemester(
						getLoginInfo().getUnitId(), acadyear, semester, recordType, page,null);
			}
		}
		List<TutorRecord> listTR = tutorRecordService.findByUidAndSemester(getLoginInfo().getUnitId(),acadyear, semester);

		//
		Map<String, Student> stuMap = new HashMap<String, Student>();
		if (CollectionUtils.isNotEmpty(listTR)) {
			Set<String> stuIdList = EntityUtils.getSet(listTR, TutorRecord::getStudentId);
			List<Student> stuList = SUtils.dt(
					studentRemoteService.findListByIds(stuIdList.toArray(new String[stuIdList.size()])),
					Student.class);
			stuMap = EntityUtils.getMap(stuList, Student::getId);
		}
		List<Clazz> clazzs = RedisUtils.getObject("CLAZZ" + getLoginInfo().getUserId(), RedisUtils.TIME_ONE_HOUR,
				new TypeReference<List<Clazz>>() {
				}, new RedisInterface<List<Clazz>>() {
					@Override
					public List<Clazz> queryData() {
						return getListClazz();
					}
				});
		Map<String, Clazz> cMap = EntityUtils.getMap(clazzs, Clazz::getId);

		if (CollectionUtils.isNotEmpty(tutorRecordDetaileds)) {
			for (final TutorRecordDetailed tutorRecordDetailed : tutorRecordDetaileds) {
				String recordResult = tutorRecordDetailed.getRecordResult();
				//对内容进行截取
				if(recordResult!=null && recordResult.length()>20){
					recordResult = recordResult.substring(0,20)+"...";
					tutorRecordDetailed.setRecordResult(recordResult);
				}
				TutorShowRecordDto tsrDto = new TutorShowRecordDto();
				tsrDto.setTutorRecordDetailed(tutorRecordDetailed);
				if (tutorRecordDetailed.getClassId() == null) {
					tsrDto.setGcName("");
				} else {
					Clazz clazz = cMap.get(tutorRecordDetailed.getClassId());
					tsrDto.setGcName(clazz.getClassNameDynamic());
				}
				if (CollectionUtils.isNotEmpty(listTR)) {
					Map<String, List<TutorRecord>> detailedMap = EntityUtils.getListMap(listTR, TutorRecord::getDetailedId, Function.identity());
					List<TutorRecord> TutorRecords = detailedMap.get(tutorRecordDetailed.getId());
					Set<String> studentIds = EntityUtils.getSet(TutorRecords, TutorRecord::getStudentId);
					List<Student> stuList = new ArrayList<Student>();
					for (String stuId : studentIds) {
					   stuList.add(stuMap.get(stuId));
					}
					tsrDto.setStuNames(stuList);
				}

				Teacher teacher = RedisUtils.getObject("TEACHER" + tutorRecordDetailed.getTeacherId(), RedisUtils.TIME_ONE_HOUR,
						new TypeReference<Teacher>() {
						}, new RedisInterface<Teacher>() {
							@Override
							public Teacher queryData() {
								return SUtils.dc(teacherRemoteService.findOneById(tutorRecordDetailed.getTeacherId()),
										Teacher.class);
							}
						});
				tsrDto.setTeacherName(teacher.getTeacherName());
				tutorShowRecordDtos.add(tsrDto);
			}

		}
		map.put("tutorShowRecordDtos", tutorShowRecordDtos);
		map.put("isAll", isAll == null ? true : false);
		sendPagination(request, map, page);
		return "/tutor/teacher/manage/tutorRecordList.ftl";
	}

	@ResponseBody
	@RequestMapping("/deleteRecord")
	@ControllerInfo("删除记录")
	public String deleteRecord(String tutorRecordDetailedId) {
		try {
			TutorRecordDetailed tutorRecordDetailed = tutorRecordDetailedService.findOne(tutorRecordDetailedId);
			String recordType = tutorRecordDetailed.getRecordType();
			if (!(recordType.equals(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID02)
					|| recordType.equals(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID08)
					|| recordType.equals(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID06))) {
				tutorRecordService.deleteByDetailed(tutorRecordDetailedId);
			}
			tutorRecordDetailedService.delete(tutorRecordDetailedId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return error("删除失败");
		}
		return success("删除成功");
	}

	@RequestMapping("/register")
	@ControllerInfo("登记学生记录")
	public String registerRecord(String tutorRecordDetailedId, String isSee, String isEdit, ModelMap map) {
		Semester semester = new Semester();
		TutorRecordDetailed tutorRecordDetailed = null;
		if (StringUtils.isNotBlank(tutorRecordDetailedId)) {
			tutorRecordDetailed = tutorRecordDetailedService.findOne(tutorRecordDetailedId);
			semester.setAcadyear(tutorRecordDetailed.getAcadyear());
			semester.setSemester(Integer.valueOf(tutorRecordDetailed.getSemester()));
		} else {
			// 当前的学年学期
			semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()),
					Semester.class);
		}
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {});
		// 记录类型
		List<McodeDetail> recordTypes = SUtils
				.dt(mcodeRemoteService.findByMcodeIds(TutorConstants.TUTOR_RECORD_TYPE_MCODE_ID), McodeDetail.class);

		// 找到所有的行政班
		// List<Clazz> clazzs = getListClazz();
		List<Clazz> clazzs = RedisUtils.getObject("CLAZZ" + getLoginInfo().getUserId(), RedisUtils.TIME_ONE_HOUR,
				new TypeReference<List<Clazz>>() {
				}, new RedisInterface<List<Clazz>>() {
					@Override
					public List<Clazz> queryData() {
						return getListClazz();
					}
				});
		map.put("classList", clazzs);
		map.put("isFamShow", TutorRecord.TUTOR_RECORD_IS_FAM_SHOW);
		map.put("isNFamShow", TutorRecord.TUTOR_RECORD_ISNOT_FAM_SHOW);
		map.put("isStuShow", TutorRecord.TUTOR_RECORD_IS_STU_SHOW);
		map.put("isNStuShow", TutorRecord.TUTOR_RECORD_ISNOT_STU_SHOW);
		map.put("recordTypes", recordTypes);
		map.put("acadyearList", acadyearList);
		map.put("semester", semester);
		map.put("isSee", isSee == null ? false : isSee.equals("true"));
		map.put("isEdit", isEdit == null ? false : isEdit.equals("true"));
		map.put("tutorRecordDetailed", tutorRecordDetailed);

		// 得到选中的学生
		String stuNames = getShowStuName(tutorRecordDetailed, "name");
		String stuIds = getShowStuName(tutorRecordDetailed, "id");
		map.put("stuNames", stuNames);
		map.put("stuIds", stuIds);

		return "/tutor/teacher/manage/manageRegisterRecord.ftl";
	}

	/**
	 * @param tutorRecordDetailed
	 */
	private String getShowStuName(TutorRecordDetailed tutorRecordDetailed, String type) {
		if (tutorRecordDetailed == null) {
			return null;
		}
		List<TutorRecord> listTR = tutorRecordService.findAll();
		// 过滤掉detailedId为空 的
		listTR = EntityUtils.filter2(listTR, t->{
    		return t.getDetailedId() != null;
    	});
		Map<String, List<TutorRecord>> detailedMap = EntityUtils.getListMap(listTR, TutorRecord::getDetailedId, Function.identity());
		List<TutorRecord> TutorRecords = detailedMap.get(tutorRecordDetailed.getId());
		Set<String> studentIds = EntityUtils.getSet(TutorRecords, TutorRecord::getStudentId);
		List<Student> students = SUtils
				.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[studentIds.size()])), Student.class);
		StringBuilder studentNames = new StringBuilder();
		String stuNames = null;
		if (type.equals("name")) {
			if (!CollectionUtils.isEmpty(students)) {
				for (Student student : students) {
					studentNames.append(student.getStudentName() + ",");
				}
				stuNames = StringUtils.removeEnd(studentNames.toString(), ",");
			}
		} else {
			if (!CollectionUtils.isEmpty(students)) {
				for (Student student : students) {
					studentNames.append(student.getId() + ",");
				}
				stuNames = StringUtils.removeEnd(studentNames.toString(), ",");
			}
		}
		return stuNames;
	}

	@ResponseBody
	@RequestMapping("/saveRecord")
	@ControllerInfo("保存记录")
	public String saveRecord(String tutorRecordDetailedId, @RequestBody String record) {
		try {
			JSONObject jsonObject = SUtils.dc(record, JSONObject.class);
			TutorRecordDetailed tutorRecordDetailed = new TutorRecordDetailed();
			String recordType;
			if (StringUtils.isNotBlank(tutorRecordDetailedId)) {
				recordType = jsonObject.getString("layerRecordType");
				if (!(recordType.equals(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID02)
						|| recordType.equals(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID08)
						|| recordType.equals(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID06))) {
					tutorRecordService.deleteByDetailed(tutorRecordDetailedId);
				}
				tutorRecordDetailed = saveTutorRecordDetailed(jsonObject, tutorRecordDetailedId);
			} else {
				tutorRecordDetailed = saveTutorRecordDetailed(jsonObject, null);
			}
			recordType = tutorRecordDetailed.getRecordType();
			if (!(recordType.equals(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID02)
					|| recordType.equals(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID08)
					|| recordType.equals(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID06))) {
				String studentIds = jsonObject.getString("studentIds");
				tutorRecordService.saveAll(
						EntityUtils.toArray(saveTutorRecord(studentIds, tutorRecordDetailed), TutorRecord.class));
			}
			tutorRecordDetailedService.save(tutorRecordDetailed);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return error("登记失败");
		}
		return success("登记成功");
	}

	/**
	 * @param jsonObject
	 * @param strArray
	 * @param tutorRecordDetailed
	 * @return 批量保存学生记录
	 */
	private List<TutorRecord> saveTutorRecord(String studentIds, TutorRecordDetailed tutorRecordDetailed) {
		// TODO Auto-generated method stub
		List<TutorRecord> listTR = new ArrayList<TutorRecord>();
		if (StringUtils.isNotBlank(studentIds)) {
			String[] strArray = studentIds.split(",");
			// 批量保存tutorRecord
			for (String sid : strArray) {
				TutorRecord tutorRecord = tutorRecordService.findByDetailedIdAndStuId(tutorRecordDetailed.getId(), sid);
				if (tutorRecord == null) {
					tutorRecord = new TutorRecord();
					tutorRecord.setId(UuidUtils.generateUuid());
					tutorRecord.setCreationTime(new Date());
					tutorRecord.setStudentId(sid);
					tutorRecord.setUnitId(getLoginInfo().getUnitId());
					tutorRecord.setTeacherId(tutorRecordDetailed.getTeacherId());
					tutorRecord.setDetailedId(tutorRecordDetailed.getId());
				} else {
					tutorRecord.setModofyTime(new Date());
				}
				tutorRecord.setAcadyear(tutorRecordDetailed.getAcadyear());
				tutorRecord.setSemester(tutorRecordDetailed.getSemester());
				tutorRecord.setRecordType(tutorRecordDetailed.getRecordType());
				tutorRecord.setRecordResult(tutorRecordDetailed.getRecordResult());
				tutorRecord.setIsStudentShow(tutorRecordDetailed.getIsStudentShow());
				tutorRecord.setIsFamilyShow(tutorRecordDetailed.getIsFamilyShow());
				tutorRecord.setModofyTime(new Date());
				listTR.add(tutorRecord);
			}
		}
		return listTR;
	}

	/**
	 * @return 找到所有的行政班
	 */
	private List<Clazz> getListClazz() {
		Unit unit = SUtils.dc(unitRemoteService.findOneById(getLoginInfo().getUnitId()), Unit.class);
		return Clazz.dt(classRemoteService.findBySchoolId(unit.getId()));
//		List<Grade> lGrades = null;
//		List<Clazz> clazzs = new ArrayList<Clazz>();
//		if (unit.getUnitClass() == Unit.UNIT_CLASS_SCHOOL) {
//			lGrades = RedisUtils.getObject("SGRADE" + getLoginInfo().getUserId(), RedisUtils.TIME_ONE_HOUR,
//					new TypeReference<List<Grade>>() {
//					}, new RedisInterface<List<Grade>>() {
//						@Override
//						public List<Grade> queryData() {
//							return SUtils.dt(gradeRemoteService.findBySchoolId(getLoginInfo().getUnitId()),
//									new TR<List<Grade>>() {
//									});
//						}
//					});
//		}
//		if(CollectionUtils.isNotEmpty(lGrades)){
//			final Set<String> gradeIds = EntityUtils.getSet(lGrades, Grade::getId);
//			clazzs = RedisUtils.getObject("SCLAZZ" + getLoginInfo().getUserId(), RedisUtils.TIME_ONE_HOUR,
//					new TypeReference<List<Clazz>>() {
//			}, new RedisInterface<List<Clazz>>() {
//				@Override
//				public List<Clazz> queryData() {
//					return SUtils.dt(
//							classRemoteService.findByInGradeIds(gradeIds.toArray(new String[gradeIds.size()])),
//							new TR<List<Clazz>>() {
//							});
//				}
//			});
//			Map<String, String> gnMap = new HashMap<String, String>();
//			if (CollectionUtils.isNotEmpty(gradeIds) && gradeIds.size() > 0) {
//				gnMap = EntityUtils.getMap(lGrades, Grade::getId, Grade::getGradeName);
//			}
//			for (Clazz clazz : clazzs) {
//				clazz.setClassNameDynamic(gnMap.get(clazz.getGradeId()) + clazz.getClassName());
//			}
//		}
//		return clazzs;
	}

	private TutorRecordDetailed saveTutorRecordDetailed(JSONObject jsonObject, String tutorRecordDetailedId) {
		User user = SUtils.dc(userRemoteService.findOneById(getLoginInfo().getUserId(), true), User.class);
		String teacherId = user.getOwnerId();
		String acadyear = jsonObject.getString("searchAcadyear");
		String semester = jsonObject.getString("searchSemester");
		String recordType = jsonObject.getString("layerRecordType");
		String recordResult = jsonObject.getString("recordResult");
		String isStudentShow = jsonObject.getString("stuShow");
		String isFamilyShow = jsonObject.getString("famShow");
		String clazzId = jsonObject.getString("clazz");
		TutorRecordDetailed tutorRecordDetailed = null;
		if (StringUtils.isNotBlank(tutorRecordDetailedId)) {
			tutorRecordDetailed = tutorRecordDetailedService.findOne(tutorRecordDetailedId);
			tutorRecordDetailed.setModifyTime(new Date());
		} else {
			tutorRecordDetailed = new TutorRecordDetailed();
			tutorRecordDetailed.setId(UuidUtils.generateUuid());
			tutorRecordDetailed.setCreationTime(new Date());
			tutorRecordDetailed.setUnitId(getLoginInfo().getUnitId());
			tutorRecordDetailed.setTeacherId(teacherId);
		}
		if (recordType.equals(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID06)) {
			if (StringUtils.isNotBlank(clazzId)) {
				tutorRecordDetailed.setClassId(clazzId);
			}
		}
		tutorRecordDetailed.setAcadyear(acadyear);
		tutorRecordDetailed.setSemester(semester);
		tutorRecordDetailed.setRecordType(recordType);
		tutorRecordDetailed.setRecordResult(recordResult);
		tutorRecordDetailed.setIsStudentShow(Integer.valueOf(isStudentShow == null ? "0" : isStudentShow));
		tutorRecordDetailed.setIsFamilyShow(Integer.valueOf(isFamilyShow == null ? "0" : isFamilyShow));
		tutorRecordDetailed.setModifyTime(new Date());
		return tutorRecordDetailed;
	}
}
