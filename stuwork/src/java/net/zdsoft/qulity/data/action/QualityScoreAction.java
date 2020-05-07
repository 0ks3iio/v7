package net.zdsoft.qulity.data.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.FilePathRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.HtmlToPdf;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ServletUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newstusys.remote.service.StudentAbnormalFlowRemoteService;
import net.zdsoft.qulity.data.constant.QualityConstants;
import net.zdsoft.qulity.data.dto.StudentScoreDto;
import net.zdsoft.qulity.data.dto.StuworkDataCountDto;
import net.zdsoft.qulity.data.entity.QualitStuRecover;
import net.zdsoft.qulity.data.entity.QualityParam;
import net.zdsoft.qulity.data.entity.QualityScore;
import net.zdsoft.qulity.data.service.QualitStuRecoverService;
import net.zdsoft.qulity.data.service.QualityScoreService;
import net.zdsoft.qulity.data.utils.QualityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/quality/sum")
public class QualityScoreAction extends QualityCommonAction {

	@Autowired
	private QualityScoreService qualityScoreService;
	@Autowired
	private FilePathRemoteService filePathRemoteService;
	@Autowired
	private StudentAbnormalFlowRemoteService studentAbnormalFlowRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private QualitStuRecoverService qualitStuRecoverService;

	@RequestMapping("/index")
	public String scoreIndex(ModelMap map) {
		List<QualityParam> params = qualityParamService.findByUnitId(getLoginInfo().getUnitId(), false);
		if(CollectionUtils.isNotEmpty(params)){
			Set<String> hasCode = EntityUtils.getSet(params, QualityParam::getParamType);
			paramCodes = Arrays.stream(paramCodes).filter(e->!hasCode.contains(e)).toArray(String[]::new);
		}else{
			params = Lists.newArrayList();
		}
		if(ArrayUtils.isNotEmpty(paramCodes)){
			List<QualityParam> newParams = Lists.newArrayList();
			for(String paramCode:paramCodes){
				QualityParam qulityParam = new QualityParam();
				qulityParam.setId(UuidUtils.generateUuid());
				qulityParam.setUnitId(getLoginInfo().getUnitId());
				qulityParam.setParamType(paramCode);
				newParams.add(qulityParam);
				params.add(qulityParam);
			}
			qualityParamService.saveAll(newParams.toArray(new QualityParam[newParams.size()]));
		}
		return "/quality/score/qualityScoreIndex.ftl";
	}

	@RequestMapping("/tab")
	public String scoreTab(ModelMap map) {
		// 班级权限
		Set<String> classPermission = stuworkRemoteService
				.findClassSetByUserId(getLoginInfo().getUserId());
		List<Clazz> clazzs = SUtils.dt(classRemoteService
				.findClassListByIds(classPermission.toArray(new String[0])),
				new TR<List<Clazz>>() {
				});
		Set<String> gradeIds = EntityUtils.getSet(clazzs, Clazz::getGradeId);
		List<Grade> returngrade = Lists.newArrayList();
		if (gradeIds.size() > 0) {
			List<Grade> grades = SUtils.dt(gradeRemoteService
					.findListByIds(gradeIds.toArray(new String[0])),
					new TR<List<Grade>>() {
					});
			for (Grade grade : grades) {
				if (grade.getSection() > 2) {
					returngrade.add(grade);
				}
			}
		}
		if (CollectionUtils.isNotEmpty(returngrade)) {
			Collections.sort(returngrade, new Comparator<Grade>() {
				@Override
				public int compare(Grade o1, Grade o2) {
					return o1.getGradeCode().compareTo(o2.getGradeCode());
				}
			});
		}
		map.put("grades", returngrade);
		QualityScore qualityScore = qualityScoreService
				.findByUnitIdOne(getLoginInfo().getUnitId(), QualityConstants.SCORE_TYPE_1);
		map.put("qualityScore", qualityScore);
		return "/quality/score/qualityScoreTab.ftl";
	}

	@ResponseBody
	@RequestMapping("/get/class/page")
	public void getClass(String gradeId, ModelMap map) {
		List<Clazz> clazzs = SUtils.dt(classRemoteService
				.findBySchoolIdGradeId(getLoginInfo().getUnitId(), gradeId),
				new TR<List<Clazz>>() {
				});
		JSONArray jsonArray = new JSONArray();
		Set<String> classIds = stuworkRemoteService
				.findClassSetByUserId(getLoginInfo().getUserId());
		for (Clazz clazz : clazzs) {
			if (classIds.contains(clazz.getId())) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", clazz.getId());
				jsonObject.put("name", clazz.getClassNameDynamic());
				jsonArray.add(jsonObject);
			}
		}
		try {
			ServletUtils.print(getResponse(), jsonArray.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/list")
	public String scoreList(HttpServletRequest request, String gradeId,
			String classId, ModelMap map) {
		Pagination page = createPagination();
		Set<String> classIds = Sets.newHashSet();
		Map<String, String> classNameMap = Maps.newHashMap();
		if (StringUtils.isNotBlank(classId)) {
			classIds.add(classId);
			Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId),
					Clazz.class);
			if (clazz != null) {
				classNameMap.put(clazz.getId(), clazz.getClassNameDynamic());
			}
		} else if (StringUtils.isNotBlank(gradeId)) {
			List<Clazz> clazzs = SUtils.dt(
					classRemoteService.findBySchoolIdGradeId(getLoginInfo()
							.getUnitId(), gradeId), new TR<List<Clazz>>() {
					});
			for (Clazz clazz : clazzs) {
				classIds.add(clazz.getId());
				classNameMap.put(clazz.getId(), clazz.getClassNameDynamic());
			}
		}
		Set<String> classIds2 = stuworkRemoteService
				.findClassSetByUserId(getLoginInfo().getUserId());
		classIds.retainAll(classIds2);
		List<StudentScoreDto> dtos = Lists.newArrayList();
		List<Student> students = Lists.newArrayList();
		if (classIds.size() > 0) {
			Map<String, QualityScore> qualityScoreMap = Maps.newHashMap();
			QualityScore qScore = qualityScoreService
					.findByUnitIdOne(getLoginInfo().getUnitId(), QualityConstants.SCORE_TYPE_1);
			if (qScore == null) {
				students = Student.dt(studentRemoteService.findByClassIds(
						classIds.toArray(new String[0]), SUtils.s(page)), page);
			} else {
				List<QualityScore> qualityScores = qualityScoreService
						.findByClassIdsAndType(classIds.toArray(new String[0]), QualityConstants.SCORE_TYPE_1, page);
				qualityScoreMap = EntityUtils
						.getMap(qualityScores, "studentId");
				Set<String> stuIds = EntityUtils.getSet(qualityScores,
						"studentId");
				if (stuIds.size() > 0) {
					students = SUtils.dt(studentRemoteService
							.findListByIds(stuIds.toArray(new String[0])),
							new TR<List<Student>>() {
							});
				}
			}
			for (Student student : students) {
				StudentScoreDto scoreDto = new StudentScoreDto();
				scoreDto.setStudentId(student.getId());
				scoreDto.setSex(student.getSex());
				scoreDto.setStudentCode(student.getStudentCode());
				scoreDto.setStudentName(student.getStudentName());
				if (classNameMap.containsKey(student.getClassId())) {
					scoreDto.setClassName(classNameMap.get(student.getClassId()));
				}
				if (qualityScoreMap.containsKey(student.getId())) {
					QualityScore qualityScore = qualityScoreMap.get(student
							.getId());
					if (qualityScore != null) {
						scoreDto.setTotalScore(qualityScore.getTotalScore());
						scoreDto.setGradeRank(qualityScore.getGradeRank());
						scoreDto.setClassRank(qualityScore.getClassRank());
					}
				}
				dtos.add(scoreDto);
			}
		}
		if (dtos.size() > 0) {
			compare(dtos);
		}
		map.put("studentDtos", dtos);
		sendPagination(request, map, page);
		return "/quality/score/qualityScoreList.ftl";
	}

	@RequestMapping("/personal")
	public String scorePersonal(String studentId, ModelMap map) {
		if(StringUtils.isBlank(studentId)){
			LoginInfo loginInfo = getLoginInfo();
			if(User.OWNER_TYPE_STUDENT==loginInfo.getOwnerType()){
				List<QualityParam> param = qualityParamService.findByUnitIdAndParamType(loginInfo.getUnitId(), QualityConstants.QULITY_STUFAM_QUERY_SWITCH);
				if(CollectionUtils.isEmpty(param) || param.get(0).getParam() != 1) {
					return promptFlt(map, "暂不允许查询个人的综合素质表！");
				}
				studentId=loginInfo.getOwnerId();
			}
		}
		Student student = SUtils.dc(studentRemoteService.findOneById(studentId),
				Student.class);
		Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()),
				Clazz.class);
		if(!(clazz.getSection()==3 || clazz.getSection()==9)){
			return promptFlt(map, "只有高中学段可以查看综合素质表！");
		}
		getPersonalInfo(studentId, map, false);
		return "/quality/score/qualityScorePersonal.ftl";
	}

	private void getPersonalInfo(String studentId, ModelMap map, boolean export) {
		Student student = SUtils.dc(
				studentRemoteService.findOneById(studentId), Student.class);
		String classId = student.getClassId();
		boolean isShow = false;
		int graduateYear = 0;
		if (StringUtils.isNotBlank(classId)) {
			Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId),
					Clazz.class);
			isShow = qualityParamService.findIsShowByGradeId(student.getSchoolId(), clazz.getGradeId());
			if (clazz != null) {
				student.setClassName(clazz.getClassNameDynamic());
				if (StringUtils.isNotBlank(clazz.getAcadyear())) {
					int beginYear = Integer.parseInt(clazz.getAcadyear()
							.substring(2, 4));
					graduateYear = beginYear + clazz.getSchoolingLength();
				}
			}
		}
		List<QualityParam> params = qualityParamService
				.findByUnitId(getLoginInfo().getUnitId(), false);
		if (CollectionUtils.isEmpty(params)) {
			params = Lists.newArrayList();
			for (String paramCode : paramCodes) {
				QualityParam qulityParam = new QualityParam();
				qulityParam.setParamType(paramCode);
				params.add(qulityParam);
			}
		}
		Integer maxValuePer = 0;
		for (QualityParam param : params) {
			if (QualityConstants.QULITY_5FESTIVAL_MAX_NUMBER.equals(param
					.getParamType())) {
				maxValuePer = param.getParamPer();
			}
		}
		Map<String, Integer> maxValueMap = EntityUtils.getMap(params,
				"paramType", "param");
		StuworkDataCountDto countDto = SUtils.dc(stuworkRemoteService
				.findStuworkCountByStudentId(maxValueMap, studentId,
						maxValuePer, isShow), StuworkDataCountDto.class);
		Map<String, String[]> culturalMap = comStatisticsRemoteService
				.findStatisticsByStudentId(studentId, maxValueMap);
		/***
		 * 页面显示学考成绩 key:subjectId
		 */
		Map<String, String> xkMap = comStatisticsRemoteService
				.findXKStatisticsByStudentId(studentId);
		List<Course> courseList = SUtils.dt(courseRemoteService
				.orderCourse(courseRemoteService.findListByIds(xkMap.keySet()
						.toArray(new String[0]))), new TR<List<Course>>() {
		});
		StringBuffer xkResult = new StringBuffer();
		for (Course one : courseList) {
			if (xkMap.get(one.getId()) != null) {
				xkResult.append(";" + one.getSubjectName() + ":"
						+ xkMap.get(one.getId()));
			}
		}

		Float totalScore = new Float(0);
		Float dyScore = new Float(0);
		Float myScore = new Float(0);
		Float xkjsScore = new Float(0);
		List<String[]> dyList = Lists.newArrayList();
		List<String[]> myList = Lists.newArrayList();
		List<String[]> xkjsList = Lists.newArrayList();

		String[] acadyears = new String[4];
		if (countDto != null) {
			Float[] countNumbers = countDto.getCountNumbers();
			int index = 0;
			for (Float num : countNumbers) {
				if (num == null) {
					index++;
					continue;
				}
				if (index == 0) {
					dyScore = num;
				}
				if (index == 1) {
					myScore = num;
				}
				if (index == 2) {
					xkjsScore = num;
				}
				totalScore = totalScore + num;
				index++;
			}

			acadyears = countDto.getAcadyears();
			Map<String, List<String[]>> infoMap = countDto.getInfoMap();
			if (infoMap.containsKey(StuworkDataCountDto.STUWORK_LIST)) {
				dyList = infoMap.get(StuworkDataCountDto.STUWORK_LIST);
			}
			if (infoMap.containsKey(StuworkDataCountDto.FESTIVAL_LIST)) {
				myList = infoMap.get(StuworkDataCountDto.FESTIVAL_LIST);
			}
			if (infoMap.containsKey(StuworkDataCountDto.GAME_LIST)) {
				xkjsList = infoMap.get(StuworkDataCountDto.GAME_LIST);
			}
		}
		if (culturalMap == null) {
			culturalMap = Maps.newHashMap();
			culturalMap.put(key_1, new String[] { "", "", "", "", "", "" });
			culturalMap.put(key_2, new String[] { "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "" });
			culturalMap.put(key_3, new String[] { "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "" });
			culturalMap.put(key_4, new String[] { "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "" });
			culturalMap.put(key_5, new String[] { "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "" });
		}
		String[] xnxq = culturalMap.get(key_1);
		String[] xkcj = culturalMap.get(key_2);
		String[] yybs = culturalMap.get(key_3);
		String[] yyks = culturalMap.get(key_4);
		String[] tycj = culturalMap.get(key_5);
		if (StringUtils.isNotBlank(xkcj[0])) {
			if(!isShow && StringUtils.isNotBlank(xkcj[3])){
				xkcj[0] = (Float.valueOf(xkcj[0]) - Float.valueOf(xkcj[3]))+"";
			}
			totalScore = totalScore + Float.valueOf(xkcj[0]);
		}
		if (StringUtils.isNotBlank(yybs[0])) {
			if(!isShow && StringUtils.isNotBlank(yybs[3])){
				yybs[0] = (Float.valueOf(yybs[0]) - Float.valueOf(yybs[3]))+"";
			}
			totalScore = totalScore + Float.valueOf(yybs[0]);
		}
		if (StringUtils.isNotBlank(yyks[0])) {
			if(!isShow && StringUtils.isNotBlank(yyks[3])){
				yyks[0] = (Float.valueOf(yyks[0]) - Float.valueOf(yyks[3]))+"";
			}
			totalScore = totalScore + Float.valueOf(yyks[0]);
		}
		if (StringUtils.isNotBlank(tycj[0])) {
			if(!isShow && StringUtils.isNotBlank(tycj[3])){
				tycj[0] = (Float.valueOf(tycj[0]) - Float.valueOf(tycj[3]))+"";
			}
			totalScore = totalScore + Float.valueOf(tycj[0]);
		}
		if (StringUtils.isNotBlank(xkMap.get(BaseConstants.ZERO_GUID))) {
			totalScore = totalScore
					+ Float.valueOf(xkMap.get(BaseConstants.ZERO_GUID));
		}
		if (map != null) {
			map.put("isShow", isShow);
			map.put("student", student);
			map.put("totalScore", totalScore);
			// 学考
			map.put("courseList", courseList);
			map.put("xkMap", xkMap);
			map.put("xkTotalScore", xkMap.get(BaseConstants.ZERO_GUID));
			if (xkResult.length() != 0) {
				map.put("xkResult", xkResult.substring(1));
			}
			// 德育、美育
			map.put("dyScore", dyScore);
			map.put("myScore", myScore);
			map.put("xkjsScore", xkjsScore);
			map.put("acadyears", acadyears);
			map.put("dyList", dyList);
			map.put("myList", myList);
			map.put("xkjsList", xkjsList);
			map.put("xnxq", xnxq);
			map.put("xkcj", xkcj);
			map.put("yybs", yybs);
			map.put("yyks", yyks);
			map.put("tycj", tycj);
			// 文化成绩
		} else if (export) {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFCellStyle style = workbook.createCellStyle();
			setHSSFCellStyle(workbook, style, (short)8);
			HSSFCellStyle style1 = workbook.createCellStyle();
			setHSSFCellStyle(workbook, style1, (short)11);
			HSSFCellStyle style2 = workbook.createCellStyle();
			setHSSFCellStyle(workbook, style2, (short)10);
			HSSFSheet sheet = workbook.createSheet(getSheetName());
			mergeCells(sheet);
			setColumnWidth(sheet);
			for (int i = 0; i < 58; i++) {
				HSSFRow row = sheet.createRow(i);
				row.setHeight((short) 500);
				if (i == 0) {
					row1Str(row, style1, graduateYear + "届高中学生综合素质评定成绩");
					continue;
				}
				if (i == 1) {
					row2(row, style2, student, totalScore);
					continue;
				}
				if (i == 2) {
					row1Str(row, style2, "文化成绩");
					continue;
				}
				if (i == 3) {
					row3(row, style, xnxq);
					continue;
				}
				if (i >= 4 && i <= 9) {
					row4(row, style, i, xkcj, yybs, yyks);
					continue;
				}
				if (i == 10 || i == 11) {
					row10(row, style, i, xkjsScore, acadyears, xkjsList);
					continue;
				}
				if (i == 12) {
					for (int m = 0; m < 20; m++) {
						HSSFCell cell = row.createCell(m);
						if (m == 0) {
							cell.setCellValue(new HSSFRichTextString("学考折分"));
						}
						if (m == 5) {
							cell.setCellValue(new HSSFRichTextString(xkMap
									.get(BaseConstants.ZERO_GUID)));
						}
						if (m == 7 && xkResult.length() != 0) {
							cell.setCellValue(new HSSFRichTextString(xkResult
									.substring(1)));
						}
						cell.setCellStyle(style);
					}
				}
				if (i == 13) {
					for (int m = 0; m < 20; m++) {
						HSSFCell cell = row.createCell(m);
						cell.setCellStyle(style);
					}
				}
				if (i == 14) {
					row1Str(row, style2, "德育成绩");
					continue;
				}
				if (i >= 15 && i <= 30) {
					row13(row, style, i, dyScore, acadyears, dyList);
					continue;
				}
				if (i == 31) {
					row1Str(row, style2, "体育、美育成绩");
					continue;
				}
				if (i == 32 || i == 33) {
					row30(row, style, i, tycj);
					continue;
				}
				if (i >= 34 && i <= 57) {
					row32(row, style, i, myScore, acadyears, myList);
					continue;
				}
			}

			setPrint(sheet);

			ExportUtils.outputData(workbook,
					student.getClassName() + student.getStudentName()
							+ "综合素质评定成绩", getResponse());
		}
	}

	//@ResponseBody
	//@RequestMapping("/personal/export")
	//public String scorePersonalExport(String studentId) {
	//	try {
	//		if(StringUtils.isBlank(studentId)){
	//			LoginInfo loginInfo = getLoginInfo();
	//			if(User.OWNER_TYPE_STUDENT==loginInfo.getOwnerType()){
	//				studentId=loginInfo.getOwnerId();
	//			}
	//		}
	//		getPersonalInfo(studentId, null, true);
	//	} catch (Exception e) {
	//		e.printStackTrace();
	//		return error("导出失败");
	//	}
	//	return success("导出完成");
	//}
	
	//@ResponseBody
	//@RequestMapping("/class/export")
	//public String scoreClassExport(String classId) {
	//	try {
	//		int graduateYear = 0;
	//		Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId),
	//				Clazz.class);
	//		if (clazz != null) {
	//			if (StringUtils.isNotBlank(clazz.getAcadyear())) {
	//				int beginYear = Integer.parseInt(clazz.getAcadyear()
	//						.substring(2, 4));
	//				graduateYear = beginYear + clazz.getSchoolingLength();
	//			}
	//		}
	//		List<QualityParam> params = qualityParamService
	//				.findByUnitId(getLoginInfo().getUnitId(), false);
	//		if (CollectionUtils.isEmpty(params)) {
	//			params = Lists.newArrayList();
	//			for (String paramCode : paramCodes) {
	//				QualityParam qulityParam = new QualityParam();
	//				qulityParam.setParamType(paramCode);
	//				params.add(qulityParam);
	//			}
	//		}
	//		Integer maxValuePer = 0;
	//		for (QualityParam param : params) {
	//			if (QualityConstants.QULITY_5FESTIVAL_MAX_NUMBER.equals(param
	//					.getParamType())) {
	//				maxValuePer = param.getParamPer();
	//			}
	//		}
	//		List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classId), Student.class);
	//		studentList.forEach(e->e.setClassName(clazz.getClassNameDynamic()));
	//		String[] studentIds = EntityUtils.getList(studentList, e->e.getId()).toArray(new String[0]);
	//		Map<String, Integer> maxValueMap = EntityUtils.getMap(params,
	//				"paramType", "param");
	//		Map<String, StuworkDataCountDto> countDtoMap = SUtils.dt(stuworkRemoteService
	//				.findStuworkCountByStudentIds(maxValueMap, classId, studentIds,
	//						maxValuePer), new TR<Map<String, StuworkDataCountDto>>(){});
	//
	//		Map<String, Map<String, String[]>> stuCulturalMap = comStatisticsRemoteService
	//				.findStatisticsByStudentIds(classId, studentIds, maxValueMap);
	//		/***
	//		 * 页面显示学考成绩 key:subjectId
	//		 */
	//		Map<String, Map<String, String>> stuXkMap = comStatisticsRemoteService
	//				.findXKStatisticsByStudentIds(clazz.getSchoolId(), studentIds);
	//		Set<String> courseIdSet = new HashSet<String>();
	//		stuXkMap.entrySet().forEach(e->{
	//			courseIdSet.addAll(e.getValue().keySet());
	//		});
	//		List<Course> courseList = SUtils.dt(courseRemoteService
	//				.orderCourse(courseRemoteService.findListByIds(courseIdSet
	//						.toArray(new String[0]))), new TR<List<Course>>() {
	//		});
	//
	//		HSSFWorkbook workbook = new HSSFWorkbook();
	//		HSSFCellStyle style = workbook.createCellStyle();
	//		setHSSFCellStyle(workbook, style, (short)8);
	//		HSSFCellStyle style1 = workbook.createCellStyle();
	//		setHSSFCellStyle(workbook, style1, (short)11);
	//		HSSFCellStyle style2 = workbook.createCellStyle();
	//		setHSSFCellStyle(workbook, style2, (short)10);
	//
	//		for (Student student : studentList) {
	//			String studentId= student.getId();
	//			Map<String, String> xkMap = stuXkMap.get(studentId);
	//			StringBuffer xkResult = new StringBuffer();
	//			for (Course one : courseList) {
	//				if (xkMap.get(one.getId()) != null) {
	//					xkResult.append(";" + one.getSubjectName() + ":"
	//							+ xkMap.get(one.getId()));
	//				}
	//			}
	//			Float totalScore = new Float(0);
	//			Float dyScore = new Float(0);
	//			Float myScore = new Float(0);
	//			Float xkjsScore = new Float(0);
	//			List<String[]> dyList = Lists.newArrayList();
	//			List<String[]> myList = Lists.newArrayList();
	//			List<String[]> xkjsList = Lists.newArrayList();
	//			String[] acadyears = new String[4];
	//			StuworkDataCountDto countDto = countDtoMap.get(studentId);
	//			if (countDto != null) {
	//				Float[] countNumbers = countDto.getCountNumbers();
	//				int index = 0;
	//				for (Float num : countNumbers) {
	//					if (num == null) {
	//						index++;
	//						continue;
	//					}
	//					if (index == 0) {
	//						dyScore = num;
	//					}
	//					if (index == 1) {
	//						myScore = num;
	//					}
	//					if (index == 2) {
	//						xkjsScore = num;
	//					}
	//					totalScore = totalScore + num;
	//					index++;
	//				}
	//
	//				acadyears = countDto.getAcadyears();
	//				Map<String, List<String[]>> infoMap = countDto.getInfoMap();
	//				if (infoMap.containsKey(StuworkDataCountDto.STUWORK_LIST)) {
	//					dyList = infoMap.get(StuworkDataCountDto.STUWORK_LIST);
	//				}
	//				if (infoMap.containsKey(StuworkDataCountDto.FESTIVAL_LIST)) {
	//					myList = infoMap.get(StuworkDataCountDto.FESTIVAL_LIST);
	//				}
	//				if (infoMap.containsKey(StuworkDataCountDto.GAME_LIST)) {
	//					xkjsList = infoMap.get(StuworkDataCountDto.GAME_LIST);
	//				}
	//			}
	//			Map<String, String[]> culturalMap = stuCulturalMap.get(studentId);
	//			if (culturalMap == null) {
	//				culturalMap = Maps.newHashMap();
	//				culturalMap.put(key_1, new String[] { "", "", "", "", "", "" });
	//				culturalMap.put(key_2, new String[] { "", "", "", "", "", "", "",
	//						"", "", "", "", "", "", "", "", "", "", "", "" });
	//				culturalMap.put(key_3, new String[] { "", "", "", "", "", "", "",
	//						"", "", "", "", "", "", "", "", "", "", "", "" });
	//				culturalMap.put(key_4, new String[] { "", "", "", "", "", "", "",
	//						"", "", "", "", "", "", "", "", "", "", "", "" });
	//				culturalMap.put(key_5, new String[] { "", "", "", "", "", "", "",
	//						"", "", "", "", "", "", "", "" });
	//			}
	//			String[] xnxq = culturalMap.get(key_1);
	//			String[] xkcj = culturalMap.get(key_2);
	//			String[] yybs = culturalMap.get(key_3);
	//			String[] yyks = culturalMap.get(key_4);
	//			String[] tycj = culturalMap.get(key_5);
	//			if (StringUtils.isNotBlank(xkcj[0])) {
	//				totalScore = totalScore + Float.valueOf(xkcj[0]);
	//			}
	//			if (StringUtils.isNotBlank(yybs[0])) {
	//				totalScore = totalScore + Float.valueOf(yybs[0]);
	//			}
	//			if (StringUtils.isNotBlank(yyks[0])) {
	//				totalScore = totalScore + Float.valueOf(yyks[0]);
	//			}
	//			if (StringUtils.isNotBlank(tycj[0])) {
	//				totalScore = totalScore + Float.valueOf(tycj[0]);
	//			}
	//			if (StringUtils.isNotBlank(xkMap.get(BaseConstants.ZERO_GUID))) {
	//				totalScore = totalScore
	//						+ Float.valueOf(xkMap.get(BaseConstants.ZERO_GUID));
	//			}
	//
	//			HSSFSheet sheet = workbook.createSheet(student.getStudentName());
	//			mergeCells(sheet);
	//			setColumnWidth(sheet);
	//			for (int i = 0; i < 58; i++) {
	//				HSSFRow row = sheet.createRow(i);
	//				row.setHeight((short) 500);
	//				if (i == 0) {
	//					row1Str(row, style1, graduateYear + "届高中学生综合素质评定成绩");
	//					continue;
	//				}
	//				if (i == 1) {
	//					row2(row, style2, student, totalScore);
	//					continue;
	//				}
	//				if (i == 2) {
	//					row1Str(row, style2, "文化成绩");
	//					continue;
	//				}
	//				if (i == 3) {
	//					row3(row, style, xnxq);
	//					continue;
	//				}
	//				if (i >= 4 && i <= 9) {
	//					row4(row, style, i, xkcj, yybs, yyks);
	//					continue;
	//				}
	//				if (i == 10 || i == 11) {
	//					row10(row, style, i, xkjsScore, acadyears, xkjsList);
	//					continue;
	//				}
	//				if (i == 12) {
	//					for (int m = 0; m < 20; m++) {
	//						HSSFCell cell = row.createCell(m);
	//						if (m == 0) {
	//							cell.setCellValue(new HSSFRichTextString("学考折分"));
	//						}
	//						if (m == 5) {
	//							cell.setCellValue(new HSSFRichTextString(xkMap
	//									.get(BaseConstants.ZERO_GUID)));
	//						}
	//						if (m == 7 && xkResult.length() != 0) {
	//							cell.setCellValue(new HSSFRichTextString(xkResult
	//									.substring(1)));
	//						}
	//						cell.setCellStyle(style);
	//					}
	//				}
	//				if (i == 13) {
	//					for (int m = 0; m < 20; m++) {
	//						HSSFCell cell = row.createCell(m);
	//						cell.setCellStyle(style);
	//					}
	//				}
	//				if (i == 14) {
	//					row1Str(row, style2, "德育成绩");
	//					continue;
	//				}
	//				if (i >= 15 && i <= 30) {
	//					row13(row, style, i, dyScore, acadyears, dyList);
	//					continue;
	//				}
	//				if (i == 31) {
	//					row1Str(row, style2, "体育、美育成绩");
	//					continue;
	//				}
	//				if (i == 32 || i == 33) {
	//					row30(row, style, i, tycj);
	//					continue;
	//				}
	//				if (i >= 34 && i <= 57) {
	//					row32(row, style, i, myScore, acadyears, myList);
	//					continue;
	//				}
	//			}
	//
	//			setPrint(sheet);
	//		}
	//
	//		ExportUtils.outputData(workbook,clazz.getClassNameDynamic()+ "综合素质评定成绩", getResponse());
	//	} catch (Exception e) {
	//		e.printStackTrace();
	//		return error("导出失败");
	//	}
	//	return success("导出完成");
	//}

	@ResponseBody
	@RequestMapping("/statistics")
	public String scoreStatistics() {
		try {
			String unitId = getLoginInfo().getUnitId();
			setRecoverStu(unitId);
			List<QualityParam> params = qualityParamService
					.findByUnitId(unitId,false);
			if (CollectionUtils.isEmpty(params)) {
				params = Lists.newArrayList();
				for (String paramCode : paramCodes) {
					QualityParam qulityParam = new QualityParam();
					qulityParam.setParamType(paramCode);
					params.add(qulityParam);
				}
			}
			Integer maxValuePer = 0;
			for (QualityParam param : params) {
				if (QualityConstants.QULITY_5FESTIVAL_MAX_NUMBER.equals(param
						.getParamType())) {
					maxValuePer = param.getParamPer();
				}
			}
			Map<String, Integer> maxValueMap = EntityUtils.getMap(params,
					"paramType", "param");
			Map<String, Boolean> showMap = qualityParamService.findIsShowMapByUnitId(unitId);
			Map<String, Float> stuDmyScoreMap = SUtils.dt(stuworkRemoteService
					.findStuworkCountByUnitId(maxValueMap, unitId, maxValuePer,showMap),
					new TR<Map<String, Float>>() {
					});
			Map<String, String> culturalMap = comStatisticsRemoteService
					.findStatisticsByUnitId(unitId, maxValueMap,showMap);
			if (culturalMap == null) {
				culturalMap = new HashMap<>();
			}
			/**
			 * 增加学考
			 */
			Map<String, String> xkMap = comStatisticsRemoteService
					.findXKStatisticsByUnitId(unitId);
			if (xkMap == null) {
				xkMap = new HashMap<String, String>();
			}
			if (culturalMap != null) {
				for (String key : stuDmyScoreMap.keySet()) {
					if (culturalMap.containsKey(key)
							&& StringUtils.isNotBlank(culturalMap.get(key))) {
						Float stuScore = stuDmyScoreMap.get(key)
								+ Float.valueOf(culturalMap.get(key));
						stuDmyScoreMap.put(key,
								QualityUtils.roundFloat(stuScore, 2));
					}
					// 总分加上学考
					if (xkMap.containsKey(key)
							&& StringUtils.isNotBlank(xkMap.get(key))) {
						Float stuScore = stuDmyScoreMap.get(key)
								+ Float.valueOf(xkMap.get(key));
						stuDmyScoreMap.put(key,
								QualityUtils.roundFloat(stuScore, 2));
					}
				}
			}

			List<QualityScore> scores = Lists.newArrayList();
			List<StudentScoreDto> scoreDtos = Lists.newArrayList();
			List<Grade> grades = SUtils.dt(gradeRemoteService
					.findBySchoolId(unitId),
					new TR<List<Grade>>() {
					});
			List<Grade> returngrade = Lists.newArrayList();
			for (Grade grade : grades) {
				if (grade.getSection() > 2) {
					returngrade.add(grade);
				}
			}
			Set<String> gradeIds = EntityUtils.getSet(returngrade, "id");
			if (gradeIds.size() > 0) {
				List<Student> students = SUtils.dt(studentRemoteService
						.findByGradeIds(gradeIds.toArray(new String[0])),
						new TR<List<Student>>() {
						});
				List<Clazz> classList = SUtils.dt(classRemoteService
						.findBySchoolIdInGradeIds(unitId,
								gradeIds.toArray(new String[0])),
						new TR<List<Clazz>>() {
						});
				Map<String, String> classGradeMap = EntityUtils.getMap(
						classList, "id", "gradeId");

				for (Student student : students) {
					StudentScoreDto dto = new StudentScoreDto();
					dto.setStudentId(student.getId());
					dto.setClassId(student.getClassId());
					if (classGradeMap.containsKey(student.getClassId())) {
						dto.setGradeId(classGradeMap.get(student.getClassId()));
					} else {
						dto.setGradeId("");
					}
					if (stuDmyScoreMap.containsKey(student.getId())) {
						dto.setTotalScore(stuDmyScoreMap.get(student.getId()));
					}
					scoreDtos.add(dto);
				}
				Map<String, List<StudentScoreDto>> gradeStuScoreMap = EntityUtils
						.getListMap(scoreDtos, "gradeId", null);
				Map<String, List<StudentScoreDto>> classStuScoreMap = EntityUtils
						.getListMap(scoreDtos, "classId", null);
				for (String key : gradeStuScoreMap.keySet()) {
					List<StudentScoreDto> dtos = gradeStuScoreMap.get(key);
					compare(dtos);
					int i = 1;
					int rank = 1;
					float lastScore = 0;
					for (StudentScoreDto dto : dtos) {
						if (!(lastScore == dto.getTotalScore())) {
							rank = i;
						}
						lastScore = dto.getTotalScore();
						dto.setGradeRank(rank);
						i++;
					}
				}
				for (String key : classStuScoreMap.keySet()) {
					List<StudentScoreDto> dtos = classStuScoreMap.get(key);
					compare(dtos);
					int i = 1;
					int rank = 1;
					float lastScore = 0;
					for (StudentScoreDto dto : dtos) {
						if (!(lastScore == dto.getTotalScore())) {
							rank = i;
						}
						lastScore = dto.getTotalScore();
						dto.setClassRank(rank);
						i++;
					}
				}
			}

			for (StudentScoreDto dto : scoreDtos) {
				QualityScore score = new QualityScore();
				score.setId(UuidUtils.generateUuid());
				score.setUnitId(unitId);
				score.setClassId(dto.getClassId());
				score.setClassRank(dto.getClassRank());
				score.setGradeId(dto.getGradeId());
				score.setGradeRank(dto.getGradeRank());
				score.setStatisticalTime(new Date());
				score.setStudentId(dto.getStudentId());
				score.setTotalScore(dto.getTotalScore());
				score.setType(QualityConstants.SCORE_TYPE_1);
				if (score.getTotalScore() < 0) {
					score.setTotalScore(0f);
				}
				if (StringUtils.isBlank(score.getGradeId())
						|| StringUtils.isBlank(score.getClassId())) {
					continue;
				}
				scores.add(score);
			}
			qualityScoreService.saveAndDelete(unitId, null ,scores.toArray(new QualityScore[scores.size()]));
		} catch (Exception e) {
			e.printStackTrace();
			return error("统计失败");
		}
		return success("统计完成");
	}

	private void compare(List<StudentScoreDto> dtos) {
		Collections.sort(dtos, new Comparator<StudentScoreDto>() {
			@Override
			public int compare(StudentScoreDto o1, StudentScoreDto o2) {
				if (o1.getTotalScore() == o2.getTotalScore()) {
					return 0;
				} else if (o1.getTotalScore() > o2.getTotalScore()) {
					return -1;
				} else {
					return 1;
				}
			}
		});
	}

	/**
	 * 表格名称
	 * 
	 * @return
	 */
	public String getSheetName() {
		String sheetName = "高中学生综合素质评定成绩"; // key 工作表名称
		sheetName = StringUtils.replaceEach(sheetName, new String[] { "\\",
				"/", "?", "*", "[", "]" }, new String[] { "", "", "", "", "",
				"" });
		if (StringUtils.EMPTY.equals(sheetName)) {
			sheetName = " ";
		}
		return sheetName;
	}

	/**
	 * 设置样式
	 * 
	 * @param workbook
	 * @param style
	 */
	public void setHSSFCellStyle(HSSFWorkbook workbook, HSSFCellStyle style, short fontSize) {
		HSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints(fontSize);
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);// 左右居中
		style.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
		style.setLocked(true);
		style.setWrapText(true);// 自动换行
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setBorderLeft(BorderStyle.THIN);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBorderRight(BorderStyle.THIN);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		style.setBorderTop(BorderStyle.THIN);
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setBorderBottom(BorderStyle.THIN);
		// style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
		// //小数点后保留两位，可以写contentStyle.setDataFormat(df.getFormat("#,#0.00"));
	}
	
	/**
	 * 设置列宽
	 * @param sheet
	 */
	public void setColumnWidth(HSSFSheet sheet){
		sheet.setColumnWidth(0, 1100);
		sheet.setColumnWidth(1, 1200);
		for (int j = 0; j <= 5; j++) {
			sheet.setColumnWidth(2+j*3, 1200);
			sheet.setColumnWidth(3+j*3, 1200);
			sheet.setColumnWidth(4+j*3, 1200);
		}
	}
	
	/**
	 * 设置打印
	 * @param sheet
	 */
	public void setPrint(HSSFSheet sheet){
		HSSFPrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); //A4纸
		printSetup.setLandscape(false);//横向打印
		printSetup.setScale((short)100);//缩放比例
		sheet.setHorizontallyCenter(true);//设置打印页面为水平居中  
		printSetup.setHeaderMargin(( double ) 0.12 );//页眉
		printSetup.setFooterMargin(( double ) 0.12 );//页脚
		sheet.setMargin(HSSFSheet.TopMargin,( double ) 0.12 );// 页边距（上）0.3cm
		sheet.setMargin(HSSFSheet.BottomMargin,( double ) 0.12 );// 页边距（下）
		sheet.setMargin(HSSFSheet.LeftMargin,( double ) 0.12 );// 页边距（左）
		sheet.setMargin(HSSFSheet.RightMargin,( double ) 0.12 );// 页边距（右）
	}

	/**
	 * 合并单元格
	 */
	public void mergeCells(HSSFSheet sheet) {
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 19));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 2));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 4, 5));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 7));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 10, 19));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 19));
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 4));
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 5, 7));
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 8, 10));
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 11, 13));
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 14, 16));
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 17, 19));
		for (int index = 4; index < 9;) {
			sheet.addMergedRegion(new CellRangeAddress(index, index + 1, 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(index, index + 1, 1, 1));
			index = index + 2;
		}
		sheet.addMergedRegion(new CellRangeAddress(10, 11, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(10, 11, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(10, 10, 2, 3));
		sheet.addMergedRegion(new CellRangeAddress(10, 10, 5, 9));
		sheet.addMergedRegion(new CellRangeAddress(10, 10, 11, 15));
		sheet.addMergedRegion(new CellRangeAddress(10, 10, 17, 18));
		sheet.addMergedRegion(new CellRangeAddress(11, 11, 2, 3));
		sheet.addMergedRegion(new CellRangeAddress(11, 11, 5, 9));
		sheet.addMergedRegion(new CellRangeAddress(11, 11, 11, 15));
		sheet.addMergedRegion(new CellRangeAddress(11, 11, 17, 18));
		sheet.addMergedRegion(new CellRangeAddress(12, 13, 0, 4));
		sheet.addMergedRegion(new CellRangeAddress(12, 13, 5, 6));
		sheet.addMergedRegion(new CellRangeAddress(12, 13, 7, 19));
		sheet.addMergedRegion(new CellRangeAddress(14, 14, 0, 19));
		sheet.addMergedRegion(new CellRangeAddress(15, 30, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(15, 30, 1, 2));
		sheet.addMergedRegion(new CellRangeAddress(15, 18, 3, 4));
		sheet.addMergedRegion(new CellRangeAddress(19, 22, 3, 4));
		sheet.addMergedRegion(new CellRangeAddress(23, 26, 3, 4));
		sheet.addMergedRegion(new CellRangeAddress(27, 30, 3, 4));
		for (int i = 15; i < 31; i++) {
			for (int j = 5; j < 19; j++) {
				sheet.addMergedRegion(new CellRangeAddress(i, i, j, ++j));
				j++;
			}
		}
		sheet.addMergedRegion(new CellRangeAddress(31, 31, 0, 19));
		sheet.addMergedRegion(new CellRangeAddress(32, 33, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(32, 33, 1, 2));
		sheet.addMergedRegion(new CellRangeAddress(32, 33, 3, 4));
		sheet.addMergedRegion(new CellRangeAddress(32, 32, 5, 6));
		sheet.addMergedRegion(new CellRangeAddress(33, 33, 5, 6));
		sheet.addMergedRegion(new CellRangeAddress(32, 32, 17, 18));
		sheet.addMergedRegion(new CellRangeAddress(33, 33, 17, 18));
		sheet.addMergedRegion(new CellRangeAddress(32, 33, 8, 8));
		sheet.addMergedRegion(new CellRangeAddress(32, 33, 12, 12));
		sheet.addMergedRegion(new CellRangeAddress(32, 33, 16, 16));
		sheet.addMergedRegion(new CellRangeAddress(34, 57, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(34, 57, 1, 2));
		sheet.addMergedRegion(new CellRangeAddress(34, 39, 3, 4));
		sheet.addMergedRegion(new CellRangeAddress(40, 45, 3, 4));
		sheet.addMergedRegion(new CellRangeAddress(46, 51, 3, 4));
		sheet.addMergedRegion(new CellRangeAddress(52, 57, 3, 4));
		for (int index = 34; index < 58; index++) {
			sheet.addMergedRegion(new CellRangeAddress(index, index, 5, 6));
			sheet.addMergedRegion(new CellRangeAddress(index, index, 8, 10));
			sheet.addMergedRegion(new CellRangeAddress(index, index, 12, 14));
			if (index == 38 || index == 44 || index == 50 || index == 56) {
				sheet.addMergedRegion(new CellRangeAddress(index, index,
						16, 18));
				sheet.addMergedRegion(new CellRangeAddress(index+1, index+1,
						16, 18));
			} else if (index != 39 && index != 45 && index != 51 && index != 57) {
				sheet.addMergedRegion(new CellRangeAddress(index, index, 16, 18));
			}
		}
	}

	public void row1Str(HSSFRow row, HSSFCellStyle style, String text) {
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(new HSSFRichTextString(text));
		cell.setCellStyle(style);
		for (int i = 1; i <= 19 ; i++) {
			HSSFCell cell19 = row.createCell(i);
			cell19.setCellStyle(style);
		}
	}

	public void row2(HSSFRow row, HSSFCellStyle style, Student student,
			Float totalScore) {
		for (int i = 0; i < 20; i++) {
			HSSFCell cell = row.createCell(i);
			if (i == 0) {
				cell.setCellValue(new HSSFRichTextString("姓名"));
			}
			if (i == 1) {
				cell.setCellValue(new HSSFRichTextString(student
						.getStudentName()));
			}
			if (i == 3) {
				cell.setCellValue(new HSSFRichTextString("班级"));
			}
			if (i == 4) {
				cell.setCellValue(new HSSFRichTextString(student.getClassName()));
			}
			if (i == 6) {
				cell.setCellValue(new HSSFRichTextString("最终总分"));
			}
			if (i == 8) {
				cell.setCellValue(QualityUtils.roundDouble(totalScore, 2));
			}
			cell.setCellStyle(style);
		}
	}

	public void row3(HSSFRow row, HSSFCellStyle style, String[] xnxq) {
		int pos = 0;
		for (int i = 0; i < 20; i++) {
			HSSFCell cell = row.createCell(i);
			if (i == 2 || i == 5 || i == 8 || i == 11 || i == 14 || i == 17) {
				cell.setCellValue(new HSSFRichTextString(xnxq[pos]));
				pos++;
			}
			cell.setCellStyle(style);
		}
	}

	public void row4(HSSFRow row, HSSFCellStyle style, int index,
			String[] xkcj, String[] yybs, String[] yyks) {
		String colName = "";
		if (index == 4) {
			colName = "学科成绩";
		} else if (index == 6) {
			colName = "英语笔试";
		} else if (index == 8) {
			colName = "英语口试";

		}
		for (int i = 0; i < 20; i++) {
			HSSFCell cell = row.createCell(i);
			if (index % 2 == 0 && i == 0) {
				cell.setCellValue(new HSSFRichTextString(colName + "折分"));
			}
			if (index % 2 == 0 && i > 1) {
				if ((i - 2) % 3 == 0) {
					cell.setCellValue(new HSSFRichTextString("成绩"));
				}
				if ((i - 2) % 3 == 1) {
					cell.setCellValue(new HSSFRichTextString("年级排名"));
				}
				if ((i - 2) % 3 == 2) {
					cell.setCellValue(new HSSFRichTextString("折分"));
				}
			}
			if (i == 1) {
				if (index == 4)
					cell.setCellValue(new HSSFRichTextString(xkcj[0]));
				if (index == 6)
					cell.setCellValue(new HSSFRichTextString(yybs[0]));
				if (index == 8)
					cell.setCellValue(new HSSFRichTextString(yyks[0]));
			}
			if (i > 1) {
				if (index == 5) {
					cell.setCellValue(new HSSFRichTextString(xkcj[i - 1]));
				}
				if (index == 7) {
					cell.setCellValue(new HSSFRichTextString(yybs[i - 1]));
				}
				if (index == 9) {
					cell.setCellValue(new HSSFRichTextString(yyks[i - 1]));
				}
			}
			cell.setCellStyle(style);
		}
	}

	public void row10(HSSFRow row, HSSFCellStyle style, int index,
			Float xkjsScore, String[] acadyears, List<String[]> xkjsList) {
		int count=2;
		for (int i = 0; i < 20; i++) {
			float normal=29f;
			HSSFCell cell = row.createCell(i);
			if (index == 10) {
				if (i == 0) {
					cell.setCellValue(new HSSFRichTextString("学科竞赛"));
				}
				if (i == 1) {
					cell.setCellValue(QualityUtils.roundDouble(
							((Float) xkjsScore).floatValue(), 2));
				}
				if (i == 2) {
					cell.setCellValue(new HSSFRichTextString(acadyears[0]
							+ "学年"));
				}
				if (i == 11) {
					cell.setCellValue(new HSSFRichTextString(acadyears[1]
							+ "学年"));
				}
				if (i == 5) {
					cell.setCellValue(new HSSFRichTextString(acadyears[2]
							+ "学年"));
				}
				if (i == 17) {
					cell.setCellValue(new HSSFRichTextString(acadyears[3]
							+ "学年"));
				}
				if (i == 4 || i == 10 || i == 16 || i == 19) {
					cell.setCellValue(new HSSFRichTextString("折分"));
				}
			}
			if (index == 11) {
				if (i == 2) {
					cell.setCellValue(new HSSFRichTextString(xkjsList.get(0)[0]));
					normal=11;
				}
				if (i == 4) {
					cell.setCellValue(QualityUtils.roundDouble(
							(Float.valueOf(xkjsList.get(0)[1])).floatValue(), 2));
				}
				if (i == 5) {
					cell.setCellValue(new HSSFRichTextString(xkjsList.get(1)[0]));
				}
				if (i == 10) {
					cell.setCellValue(QualityUtils.roundDouble(
							(Float.valueOf(xkjsList.get(1)[1])).floatValue(), 2));
				}
				if (i == 11) {
					cell.setCellValue(new HSSFRichTextString(xkjsList.get(2)[0]));
				}
				if (i == 16) {
					cell.setCellValue(QualityUtils.roundDouble(
							(Float.valueOf(xkjsList.get(2)[1])).floatValue(), 2));
				}
				if (i == 17) {
					cell.setCellValue(new HSSFRichTextString(xkjsList.get(3)[0]));
					normal=11;
				}
				if (i == 19) {
					cell.setCellValue(QualityUtils.roundDouble(
							(Float.valueOf(xkjsList.get(3)[1])).floatValue(), 2));
				}
			}
			cell.setCellStyle(style);
			int num = Math.round(StringUtils.getRealLength(cell.toString())/normal);
			if(num>count){
				count=num;
			}
		}
		row.setHeight((short)(count*250));
	}

	public void row13(HSSFRow row, HSSFCellStyle style, int i, Float dyScore,
			String[] acadyears, List<String[]> dyList) {
		int count=2;
		for (int index = 0; index < 20; index++) {
			HSSFCell cell = row.createCell(index);
			if (index == 0 && i == 15) {
				cell.setCellValue(new HSSFRichTextString("德育成绩"));
			}
			if (index == 1 && i == 15) {
				cell.setCellValue(QualityUtils.roundDouble(dyScore, 2));
			}
			if (i % 2 == 1
					&& (index == 7 || index == 10 || index == 13 || index == 16 || index == 19)) {
				cell.setCellValue(new HSSFRichTextString("折分"));
			}
			if (i % 2 == 0
					&& (index == 7 || index == 10 || index == 13 || index == 16 || index == 19)) {
				int set = ((index - 7) / 3 + 1) * 2 - 1;
				if ((i - 2) % 4 == 0) {
					set = ((index - 7) / 3 + 6) * 2 - 1;
				}
				cell.setCellValue(QualityUtils.roundDouble((Float
						.valueOf(dyList.get((i - 3) / 4 - 3)[set]))
						.floatValue(), 2));
			}
			if (index == 3) {
				cell.setCellValue(new HSSFRichTextString(
						acadyears[((i - 3) / 4 - 3)] + "学年"));
			}
			if (index == 5) {
				if ((i - 2) % 4 == 1) {
					cell.setCellValue(new HSSFRichTextString("操行等第"));
				}
				if ((i - 2) % 4 == 2) {
					cell.setCellValue(new HSSFRichTextString(dyList
							.get((i - 3) / 4 - 3)[0]));
				}
				if ((i - 2) % 4 == 3) {
					cell.setCellValue(new HSSFRichTextString("值周表现"));
				}
				if ((i - 2) % 4 == 0) {
					cell.setCellValue(new HSSFRichTextString(dyList
							.get((i - 3) / 4 - 3)[10]));
				}
			}
			if (index == 8) {
				if ((i - 2) % 4 == 1) {
					cell.setCellValue(new HSSFRichTextString("学生干部"));
				}
				if ((i - 2) % 4 == 2) {
					cell.setCellValue(new HSSFRichTextString(dyList
							.get((i - 3) / 4 - 3)[2]));
				}
				if ((i - 2) % 4 == 3) {
					cell.setCellValue(new HSSFRichTextString("军训"));
				}
				if ((i - 2) % 4 == 0) {
					cell.setCellValue(new HSSFRichTextString(dyList
							.get((i - 3) / 4 - 3)[12]));
				}
			}
			if (index == 11) {
				if ((i - 2) % 4 == 1) {
					cell.setCellValue(new HSSFRichTextString("社团骨干"));
				}
				if ((i - 2) % 4 == 2) {
					cell.setCellValue(new HSSFRichTextString(dyList
							.get((i - 3) / 4 - 3)[4]));
				}
				if ((i - 2) % 4 == 3) {
					cell.setCellValue(new HSSFRichTextString("学农"));
				}
				if ((i - 2) % 4 == 0) {
					cell.setCellValue(new HSSFRichTextString(dyList
							.get((i - 3) / 4 - 3)[14]));
				}
			}
			if (index == 14) {
				if ((i - 2) % 4 == 1) {
					cell.setCellValue(new HSSFRichTextString("突出贡献（特殊奖励）"));
				}
				if ((i - 2) % 4 == 2) {
					cell.setCellValue(new HSSFRichTextString(dyList
							.get((i - 3) / 4 - 3)[6]));
				}
				if ((i - 2) % 4 == 3) {
					cell.setCellValue(new HSSFRichTextString("评优"));
				}
				if ((i - 2) % 4 == 0) {
					cell.setCellValue(new HSSFRichTextString(dyList
							.get((i - 3) / 4 - 3)[16]));
				}
			}
			if (index == 17) {
				if ((i - 2) % 4 == 1) {
					cell.setCellValue(new HSSFRichTextString("违纪处罚"));
				}
				if ((i - 2) % 4 == 2) {
					cell.setCellValue(new HSSFRichTextString(dyList
							.get((i - 3) / 4 - 3)[8]));
				}
				if ((i - 2) % 4 == 3) {
					cell.setCellValue(new HSSFRichTextString("先进"));
				}
				if ((i - 2) % 4 == 0) {
					cell.setCellValue(new HSSFRichTextString(dyList
							.get((i - 3) / 4 - 3)[18]));
				}
			}
			cell.setCellStyle(style);
			int num = Math.round(StringUtils.getRealLength(cell.toString())/11f);
			if(num>count){
				count=num;
			}
		}
		row.setHeight((short)(count*250));
	}

	public void row30(HSSFRow row, HSSFCellStyle style, int index, String[] tycj) {
		int pos = 1;
		for (int i = 0; i < 20; i++) {
			HSSFCell cell = row.createCell(i);
			if (index == 32) {
				if (i == 0) {
					cell.setCellValue(new HSSFRichTextString("体育"));
				}
				if (i == 1) {
					cell.setCellValue(new HSSFRichTextString(tycj[0]));
				}

				if (i == 3 || i == 8 || i == 12 || i == 16) {
					cell.setCellValue(new HSSFRichTextString(tycj[pos]));
					if (i == 3) {
						pos += 3;
					} else {
						pos += 4;
					}
				}
				if (i == 5 || i == 9 || i == 13 || i == 17) {
					cell.setCellValue(new HSSFRichTextString("实分1"));
				}
				if (i == 10 || i == 14) {
					cell.setCellValue(new HSSFRichTextString("实分2"));
				}
				if (i == 7 || i == 11 || i == 15 || i == 19) {
					cell.setCellValue(new HSSFRichTextString("折分"));
				}
			} else if (index == 33) {
				if (i == 5)
					cell.setCellValue(new HSSFRichTextString(tycj[2]));
				if (i == 7)
					cell.setCellValue(new HSSFRichTextString(tycj[3]));
				if (i == 9)
					cell.setCellValue(new HSSFRichTextString(tycj[5]));
				if (i == 10)
					cell.setCellValue(new HSSFRichTextString(tycj[6]));
				if (i == 11)
					cell.setCellValue(new HSSFRichTextString(tycj[7]));
				if (i == 13)
					cell.setCellValue(new HSSFRichTextString(tycj[9]));
				if (i == 14)
					cell.setCellValue(new HSSFRichTextString(tycj[10]));
				if (i == 15)
					cell.setCellValue(new HSSFRichTextString(tycj[11]));
				if (i == 17)
					cell.setCellValue(new HSSFRichTextString(tycj[13]));
				if (i == 19)
					cell.setCellValue(new HSSFRichTextString(tycj[14]));
			}
			cell.setCellStyle(style);
		}
	}

	public void row32(HSSFRow row, HSSFCellStyle style, int index,
			Float myScore, String[] acadyears, List<String[]> myList) {
		int count = 2;
		for (int i = 0; i < 20; i++) {
			float normal = 17f;
			HSSFCell cell = row.createCell(i);
			if (index == 34) {
				if (i == 0) {
					cell.setCellValue(new HSSFRichTextString("各大节日及全校性活动"));
				}
				if (i == 1) {
					cell.setCellValue(QualityUtils.roundDouble(
							((Float) myScore).floatValue(), 2));
				}
			}
			int set = (index - 4) / 6 - 5;
			if (i == 3) {
				cell.setCellValue(new HSSFRichTextString(acadyears[set] + "学年"));
			}
			if (i == 5
					&& (index == 34 || index == 40 || index == 46 || index == 52)) {
				cell.setCellValue(new HSSFRichTextString("体育节-个人"));
			}
			if (i == 8
					&& (index == 34 || index == 40 || index == 46 || index == 52)) {
				cell.setCellValue(new HSSFRichTextString("体育节-团体"));
			}
			if (i == 12
					&& (index == 34 || index == 40 || index == 46 || index == 52)) {
				cell.setCellValue(new HSSFRichTextString("外语节-个人"));
			}
			if (i == 16
					&& (index == 34 || index == 40 || index == 46 || index == 52)) {
				cell.setCellValue(new HSSFRichTextString("外语节-团体"));
			}
			if (i == 5
					&& (index == 36 || index == 42 || index == 48 || index == 54)) {
				cell.setCellValue(new HSSFRichTextString("艺术节-个人"));
			}
			if (i == 8
					&& (index == 36 || index == 42 || index == 48 || index == 54)) {
				cell.setCellValue(new HSSFRichTextString("艺术节-团体"));
			}
			if (i == 12
					&& (index == 36 || index == 42 || index == 48 || index == 54)) {
				cell.setCellValue(new HSSFRichTextString("科技节-个人"));
			}
			if (i == 16
					&& (index == 36 || index == 42 || index == 48 || index == 54)) {
				cell.setCellValue(new HSSFRichTextString("科技节-团体"));
			}
			if (i == 5
					&& (index == 38 || index == 44 || index == 50 || index == 56)) {
				cell.setCellValue(new HSSFRichTextString("文化节-个人"));
			}
			if (i == 8
					&& (index == 38 || index == 44 || index == 50 || index == 56)) {
				cell.setCellValue(new HSSFRichTextString("文化节-团体"));
			}
			if (i == 12
					&& (index == 38 || index == 44 || index == 50 || index == 56)) {
				cell.setCellValue(new HSSFRichTextString("全校性活动-个人"));
			}
			if (i == 16
					&& (index == 38 || index == 44 || index == 50 || index == 56)) {
				cell.setCellValue(new HSSFRichTextString("全校性活动-团体"));
			}
			if (index % 2 == 0) {
				if (i == 7 || i == 11 || i == 15
						|| i == 19 ) {//&& ((index - 2) % 6 != 0)
					cell.setCellValue(new HSSFRichTextString("折分"));
				}
			}
			int j = ((index - 2 - (33 + set * 6)) / 2) * 8;
			if (i == 5 && index % 2 == 1) {
				cell.setCellValue(new HSSFRichTextString(myList.get(set)[j]));
				normal=11f;
			}
			if (i == 8 && index % 2 == 1) {
				cell.setCellValue(new HSSFRichTextString(myList.get(set)[j + 2]));
			}
			if (i == 12 && index % 2 == 1) {
				cell.setCellValue(new HSSFRichTextString(myList.get(set)[j + 4]));
			}
			if (i == 16 && index % 2 == 1 ) {//&& (j + 6) < 22
				cell.setCellValue(new HSSFRichTextString(myList.get(set)[j + 6]));
			}
			if (i == 7 && index % 2 == 1) {
				cell.setCellValue(QualityUtils.roundDouble(
						(Float.valueOf(myList.get(set)[j + 1])).floatValue(), 2));
			}
			if (i == 11 && index % 2 == 1) {
				cell.setCellValue(QualityUtils.roundDouble(
						(Float.valueOf(myList.get(set)[j + 3])).floatValue(), 2));
			}
			if (i == 15 && index % 2 == 1) {
				cell.setCellValue(QualityUtils.roundDouble(
						(Float.valueOf(myList.get(set)[j + 5])).floatValue(), 2));
			}
			if (i == 19 && index % 2 == 1 ) {//&& (j + 6) < 22
				cell.setCellValue(QualityUtils.roundDouble(
						(Float.valueOf(myList.get(set)[j + 7])).floatValue(), 2));
			}
			cell.setCellStyle(style);
			int num = Math.round(StringUtils.getRealLength(cell.toString())/normal);
			if(num>count){
				count=num;
			}
		}
		row.setHeight((short)(count*250));
	}

	@RequestMapping("/class/pdfExport")
	public void classPdfExport(HttpServletRequest request, HttpServletResponse response) {
		String classId = request.getParameter("classId");
		try {
			Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
			String fileSystemPath = filePathRemoteService.getFilePath();// 文件系统地址
			String filePath = "qualityPdf" + File.separator + getLoginInfo().getUnitId() + File.separator + classId + ".pdf";
			String dp = fileSystemPath + File.separator + filePath;
			File file = new File(dp);
			if(file.exists()) {
				file.delete();
			}
			List<Student> stuList = Student.dt(studentRemoteService.findByClassIds(new String[] {classId}));
			if(CollectionUtils.isEmpty(stuList)) {
				return;
			}
			List<String> stuIds = EntityUtils.getList(stuList, Student::getId);
			List<String> urls = new ArrayList<>();
			String domain = UrlUtils.getPrefix(request);
			for(String sid : stuIds) {
				urls.add(domain + "/quality/common/student/pdfHtml?studentId="+sid);
			}
			HtmlToPdf.convert(urls.toArray(new String[0]), dp, null, null, 10000);
			//HtmlToPdf.convert(new String[] {UrlUtils.getPrefix(request)+"/quality/common/class/pdfHtml?classId="+classId}, dp, null, null, 2000);
			//如果文件不存在
			file = null;
			file = new File(dp);
			if (!file.exists()) {
				return;
			}
			String downTime=request.getParameter("downId");//获取下载的时间戳
			ServletUtils.addCookie(response,"D"+downTime, downTime, 20000);
			ServletUtils.download(new FileInputStream(file), request, response, clazz.getClassNameDynamic()+ "综合素质评定成绩.pdf");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/student/pdfExport")
	public void studentPdfExport(HttpServletRequest request, HttpServletResponse response) {
		String studentId = request.getParameter("studentId");
		try {
			Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);

			String fileSystemPath = filePathRemoteService.getFilePath();// 文件系统地址
			String filePath = "qualityPdf" + File.separator + getLoginInfo().getUnitId() + File.separator + studentId + ".pdf";
			String dp = fileSystemPath + File.separator + filePath;
			File file = new File(dp);
			if(file.exists()) {
				file.delete();
			}
			HtmlToPdf.convert(new String[] {UrlUtils.getPrefix(request)+"/quality/common/student/pdfHtml?studentId="+studentId}, dp, null, null, 2000);
			//如果文件不存在
			file = null;
			file = new File(dp);
			if (!file.exists()) {
				return;
			}
			String downTime=request.getParameter("downId");//获取下载的时间戳
			ServletUtils.addCookie(response,"D"+downTime, downTime, 20000);
			ServletUtils.download(new FileInputStream(file), request, response, student.getStudentName()+ "综合素质评定成绩.pdf");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setRecoverStu(String unitId){
		try {
			Semester se=SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId),Semester.class);
			JSONArray array=studentAbnormalFlowRemoteService.findRecoverStuBySemester(unitId,se.getAcadyear(),se.getSemester().toString(),null);
			if(!array.isEmpty() && array.size()>0){
				JSONObject json=null;
				Set<String> stuIds=new HashSet<>();
				Map<String,Integer> stuYearMap=new HashMap<>();
				List<QualitStuRecover> qualitStuRecoverList=qualitStuRecoverService.findListByUnitIDAndAcadyear(unitId,se.getAcadyear());
				Map<String,QualitStuRecover> qualityScoreMap=EntityUtils.getMap(qualitStuRecoverList,QualitStuRecover::getStudentId);
				List<QualitStuRecover> list=new ArrayList<>();
				QualitStuRecover stuRecover=null;
				for(int i=0;i<array.size();i++){
					json=array.getJSONObject(i);
					String stuId=json.getString("stuId");
					if(qualityScoreMap.containsKey(stuId)){//不存在该学生当前学年的复学升级记录 或者未升级状态
						stuRecover=qualityScoreMap.get(stuId);
						if(stuRecover.getHasChange()!=1){
							stuRecover.setHasChange(1);
							list.add(stuRecover);
							stuIds.add(stuId);
							stuYearMap.put(stuId,json.getIntValue("years"));
						}
					}else{
						stuRecover=new QualitStuRecover();
						stuRecover.setId(UuidUtils.generateUuid());
						stuRecover.setAcadyear(se.getAcadyear());
						stuRecover.setUnitId(unitId);
						stuRecover.setStudentId(stuId);
						stuRecover.setHasChange(1);
						stuRecover.setCreationTime(new Date());
						list.add(stuRecover);
						stuIds.add(stuId);
						stuYearMap.put(stuId,json.getIntValue("years"));
					}
				}
				if(CollectionUtils.isNotEmpty(list)){
					qualitStuRecoverService.saveAll(list.toArray(new QualitStuRecover[0]));
				}
				if(CollectionUtils.isNotEmpty(stuIds)){
					stuworkRemoteService.setRecoverStuScore(stuIds.toArray(new String[0]),stuYearMap,se.getAcadyear());
					comStatisticsRemoteService.setRecoverStuScore(stuIds.toArray(new String[0]),stuYearMap,se.getAcadyear());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
