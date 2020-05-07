package net.zdsoft.newgkelective.data.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.ChosenSearchDto;
import net.zdsoft.newgkelective.data.dto.NewGkChoResourceDto;
import net.zdsoft.newgkelective.data.dto.NewGkChoResultDto;
import net.zdsoft.newgkelective.data.dto.NewGkConditionDto;
import net.zdsoft.newgkelective.data.dto.NewGkResultSaveDto;
import net.zdsoft.newgkelective.data.dto.StudentResultDto;
import net.zdsoft.newgkelective.data.entity.NewGkChoCategory;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkTeachUpStuLog;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;
import net.zdsoft.newgkelective.data.service.NewGkChoCategoryService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkChoiceService;
import net.zdsoft.newgkelective.data.service.NewGkReferScoreService;
import net.zdsoft.newgkelective.data.service.NewGkScoreResultService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkTeachUpStuLogService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanService;
import net.zdsoft.newgkelective.data.utils.CombineAlgorithmInt;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
@Controller
@RequestMapping("/newgkelective/{choiceId}")
public class NewGKElectiveChosenAction extends NewGkRoleCommonAction{
	
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private NewGkChoiceService newGkChoiceService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private NewGkScoreResultService newGkScoreResultService;
	@Autowired
	private NewGkChoResultService newGkChoResultService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private NewGkReferScoreService newGkReferScoreService;
	@Autowired
    private NewGkChoCategoryService newGkChoCategoryService;
	@Autowired
	private NewGkTeachUpStuLogService newGkTeachUpStuLogService;
	@Autowired
	private NewGkSubjectTimeService newGkSubjectTimeService;
	@Autowired
	private NewGkTeacherPlanService newGkTeacherPlanService;
	
	@RequestMapping("/chosen/tabHead/page")
	@ControllerInfo(value="头部")
	public String chosenTab(@PathVariable String choiceId,String scourceType,ChosenSearchDto dto,ModelMap map){
		//头部查询
		NewGkChoice gkChoice=newGkChoiceService.findOne(choiceId);
		
		//根据年级id拿到所有班级id
		List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(gkChoice.getUnitId(),gkChoice.getGradeId()), new TR<List<Clazz>>(){});
		
		//权限控制
		LoginInfo loginInfo = getLoginInfo();
		Map<String, List<String>> roleMap = findRoleByUserId(loginInfo.getUnitId(), loginInfo.getUserId());
		if(isClassRole(roleMap, gkChoice.getGradeId())){
			map.put("isClassRole", true);
			List<String> classIdList = roleMap.get(ROLE_CLASS);
			clazzList = clazzList.stream().filter(e->classIdList.contains(e.getId())).collect(Collectors.toList());
		}
		if (roleMap.get(ROLE_ADMIN) != null || (roleMap.get(ROLE_ADMIN) != null && roleMap.get(ROLE_ADMIN).contains(gkChoice.getGradeId()))) {
			map.put("isGradeRole", true);
		}

		map.put("scourceType", scourceType);//来源用于返回
		//已选未选
		//已选科目s
		if(StringUtils.isBlank(dto.getChosenType())){
			map.put("chosenType", NewGkElectiveConstant.IF_1);
		}else{
			map.put("chosenType", dto.getChosenType());
		}
		Map<String,String> subjectIdsMap=new HashMap<String,String>();
		if(StringUtils.isNotBlank(dto.getSubjectIds())){
			String[] subjectIds = dto.getSubjectIds().split(",");
			//key,key
			for(String s:subjectIds){
				subjectIdsMap.put(s, s);
			}
		}
		map.put("subjectIdsMap", subjectIdsMap);
		//科目列表
		String unitId = dto.getUnitId();
		if(StringUtils.isEmpty(unitId)) {
			unitId = getLoginInfo().getUnitId();
		}
		List<Course> courseList = newGkChoRelationService.findChooseSubject(choiceId, unitId);
		map.put("clazzList", clazzList);
		map.put("courseList", courseList);
		map.put("gradeId", gkChoice.getGradeId());
		map.put("choiceId",choiceId);
		map.put("refscoreId",newGkReferScoreService.findDefaultIdByGradeId(gkChoice.getUnitId(), gkChoice.getGradeId()));
		map.put("classIds", dto.getClassIds());
		return "/newgkelective/chosen/chosenSearchHead.ftl";
	}
	
	@RequestMapping("/chosen/list/page")
	@ControllerInfo(value="列表")
	public String chosenList(@PathVariable String choiceId,ChosenSearchDto dto, HttpServletRequest request, String isMaster, ModelMap map){
		NewGkChoice gkChoice=newGkChoiceService.findOne(choiceId);
		
		//权限控制
		LoginInfo loginInfo = getLoginInfo();
		Map<String, List<String>> roleMap = findRoleByUserId(loginInfo.getUnitId(), loginInfo.getUserId());
		List<String> classIdList = null;
		if(isClassRole(roleMap, gkChoice.getGradeId())){
			map.put("isClassRole", true);
			classIdList = roleMap.get(ROLE_CLASS);
			List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(loginInfo.getUnitId(), gkChoice.getGradeId()),Clazz.class);
			List<String> thisGradeClass = EntityUtils.getList(classList, Clazz::getId);
			classIdList = classIdList.stream().filter(e->thisGradeClass.contains(e)).collect(Collectors.toList());
			if(StringUtils.isBlank(dto.getClassIds())){
				dto.setClassIds(StringUtils.join(classIdList,","));
			}
		}
		
		dto.setGradeId(gkChoice.getGradeId());
		dto.setUnitId(gkChoice.getUnitId());
		dto.setIsstuCache(false);
		map.put("gkChoice", gkChoice);
		map.put("scourceType", StringUtils.trimToEmpty(request.getParameter("scourceType")));
		List<Course> courseList = newGkChoRelationService.findChooseSubject(choiceId, gkChoice.getUnitId());
		map.put("courseList", courseList);
		if(NewGkElectiveConstant.IF_0.equals(dto.getChosenType())){
			map.put("chooseNum", gkChoice.getChooseNum());
			//未选
			//未参与选课学生
			List<StudentResultDto> studentList = findNotChosen(gkChoice.getUnitId(),choiceId, dto, isMaster);
			Collections.sort(studentList, (x,y)->{
				if(x.getStudentCode() == null && y.getStudentCode() == null) {
					return 0;
				}else if(x.getStudentCode() == null) {
					return 1;
				}else if(y.getStudentCode() == null) {
					return -1;
				}
				return x.getStudentCode().compareTo(y.getStudentCode());
			});
			map.put("studentList", studentList);
			return "/newgkelective/chosen/nochosenList.ftl";
		}else{
			//已选
			List<StudentResultDto> studentList = findChosen(gkChoice.getUnitId(),choiceId, dto, isMaster);
			Collections.sort(studentList, (x,y)->{
				if(x.getStudentCode() == null && y.getStudentCode() == null) {
					return 0;
				}else if(x.getStudentCode() == null) {
					return 1;
				}else if(y.getStudentCode() == null) {
					return -1;
				}
				return x.getStudentCode().compareTo(y.getStudentCode());
			});
			map.put("studentList", studentList);
			int chooseNum=gkChoice.getChooseNum();
			if(StringUtils.isNotBlank(dto.getSubjectIds())){
				String[] arr = dto.getSubjectIds().split(",");
				if(arr.length<chooseNum && arr.length>0){
					chooseNum=arr.length;
				}
			}
			map.put("chooseNum", chooseNum);
			
			if (CollectionUtils.isNotEmpty(studentList)) {
				//参考成绩
				Map<String,Map<String,Float>> stuSubjectScore = new HashMap<String, Map<String,Float>>();
				String defaultReferScoreId = gkChoice.getReferScoreId();
				if (StringUtils.isNotBlank(defaultReferScoreId)) {
					stuSubjectScore = newGkScoreResultService.findMapByReferScoreId(gkChoice.getUnitId(), defaultReferScoreId);
				}
				//锁定
				List<String> lockStudentList;
				if(Constant.IS_TRUE_Str.equals(isMaster)){
					lockStudentList = newGkChoRelationService.findByChoiceIdAndObjectTypeWithMaster(gkChoice.getUnitId(), choiceId, NewGkElectiveConstant.CHOICE_TYPE_05);
				}else{
					lockStudentList = newGkChoRelationService.findByChoiceIdAndObjectType(gkChoice.getUnitId(), choiceId, NewGkElectiveConstant.CHOICE_TYPE_05);
				}
				for(StudentResultDto sd:studentList){
					if(stuSubjectScore.containsKey(sd.getStudentId())){
						sd.setSubjectScore(stuSubjectScore.get(sd.getStudentId()));
					}
					if(lockStudentList.contains(sd.getStudentId())){
						sd.setLock(NewGkElectiveConstant.IF_1);
					}
				}
			}
			return "/newgkelective/chosen/chosenList.ftl";
		}
		
	}

	@RequestMapping("/chosen/list/export")
	public void chosenTableExport(@PathVariable String choiceId, String classIds, HttpServletRequest request, HttpServletResponse response){

		NewGkChoice newChoice = newGkChoiceService.findOne(choiceId);

		//权限控制
		LoginInfo loginInfo = getLoginInfo();
		Map<String, List<String>> roleMap = findRoleByUserId(loginInfo.getUnitId(), loginInfo.getUserId());
		List<Clazz> classList = null;
		if(isClassRole(roleMap, newChoice.getGradeId())){
			List<String> classIdList = roleMap.get(ROLE_CLASS);
			classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(loginInfo.getUnitId(), newChoice.getGradeId()), Clazz.class);
			List<String> thisGradeClass = EntityUtils.getList(classList, Clazz::getId);
			classList = classList.stream().filter(e -> classIdList.contains(e.getId())).collect(Collectors.toList());
		} else if (roleMap.get(ROLE_ADMIN) != null) {
			classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(loginInfo.getUnitId(), newChoice.getGradeId()), Clazz.class);
		}

		List<Student> studentList = SUtils.dt(studentRemoteService.findPartStudByGradeId(newChoice.getUnitId(), newChoice.getGradeId(), null, null), new TR<List<Student>>() {});
		Map<String, List<String>> classIdToStudentsMap = EntityUtils.getListMap(studentList, Student::getClassId, e -> e.getId());

		HSSFWorkbook workbook = new HSSFWorkbook();
		CellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
		titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		titleStyle.setAlignment(HorizontalAlignment.CENTER);//水平
		titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
		titleStyle.setBorderBottom(BorderStyle.THIN);
		titleStyle.setBorderLeft(BorderStyle.THIN);
		titleStyle.setBorderRight(BorderStyle.THIN);
		titleStyle.setBorderTop(BorderStyle.THIN);

		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);

		// 获取所有的选课结果
		List<NewGkChoResult> resultList = newGkChoResultService.findByKindTypeAndChoiceIdAndStudentIds(newChoice.getUnitId(), NewGkElectiveConstant.KIND_TYPE_01, choiceId, EntityUtils.getList(studentList, Student::getId).toArray(new String[0]));
		// 获取选考科目
		Map<String, List<NewGkChoRelation>> NewGkChoRelationAllMap= newGkChoRelationService.findByChoiceIdAndObjectTypeIn(newChoice.getUnitId(), choiceId,
                new String[]{NewGkElectiveConstant.CHOICE_TYPE_01, NewGkElectiveConstant.CHOICE_TYPE_02, NewGkElectiveConstant.CHOICE_TYPE_04});

		List<NewGkChoRelation> newGkChoRelationList = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01);
		List<String> courseIds = EntityUtils.getList(newGkChoRelationList, NewGkChoRelation::getObjectValue);
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])), new TR<List<Course>>() {});

		// 不参与选课排课人员
		List<NewGkChoRelation> notVals = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_04);
		if (CollectionUtils.isNotEmpty(notVals)) {
			List<String> studentIds = EntityUtils.getList(studentList, Student::getId);
			Iterator<NewGkChoRelation> iterator = notVals.iterator();
			while (iterator.hasNext()) {
				NewGkChoRelation cho = iterator.next();
				if (!studentIds.contains(cho.getObjectValue())) {
					iterator.remove();
				}
			}
		}
		// 获取禁选组合
		List<NewGkChoRelation> forbidVals = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_02);
		List<String> objectValues = null;
		if (CollectionUtils.isNotEmpty(forbidVals)) {
			objectValues = EntityUtils.getList(forbidVals, NewGkChoRelation::getObjectValue);
		}
		// 已选学生ids
		Set<String> chosenStuIds = new HashSet<>();
		NewGkChoResultDto dto;
		// 获取学生对应的选课ids
		Map<String, NewGkChoResultDto> dtoMap = new HashMap<>();
		Map<String, Integer> courseIdCountMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(resultList)) {
			for (NewGkChoResult result : resultList) {
				chosenStuIds.add(result.getStudentId());
				if (!dtoMap.containsKey(result.getStudentId())) {
					dto = new NewGkChoResultDto();
					dto.setChooseSubjectIds(new HashSet<>());
					dto.setStudentId(result.getStudentId());
					dtoMap.put(result.getStudentId(), dto);
				}
				dtoMap.get(result.getStudentId()).getChooseSubjectIds().add(result.getSubjectId());

				Integer integer = courseIdCountMap.get(result.getSubjectId());
				if (integer == null) {
					integer = 0;
				}
				courseIdCountMap.put(result.getSubjectId(), ++integer);
			}
		}
		Map<String, Integer> courseNameCountMap = new HashMap<>();
		for (Course course : courseList) {
			Integer integer = courseIdCountMap.get(course.getId());
			if (integer == null) {
				integer = 0;
			}
			courseNameCountMap.put(course.getSubjectName(), integer);
		}

		// 各科目选择结果
		courseNameCountMap = sortByValue(courseNameCountMap);
		// 计算组合
		Integer[] cSize = new Integer[courseList.size()];
		for (int i = 0; i < courseList.size(); i++) {
			cSize[i] = i;
		}

		// 三科目选择结果
		CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize, 3);
		Integer[][] result = combineAlgorithm.getResutl();
		List<NewGkConditionDto> newConditionList3 = newGkChoiceService.findSubRes(courseList, dtoMap, result, 3);
		// 新增过滤筛选组合不推荐课程
		if (CollectionUtils.isNotEmpty(objectValues)) {
			for (String subjectIdstr : objectValues) {
				String[] subjectIds = subjectIdstr.split(",");
				if (subjectIds.length == 3) {
					for (NewGkConditionDto newCondition : newConditionList3) {
						Set<String> subs = newCondition.getSubjectIds();
						if (subs.contains(subjectIds[0]) && subs.contains(subjectIds[1]) && subs.contains(subjectIds[2])) {
							newCondition.setLimitSubject(true);
						}
					}
				}
			}
		}

		// 二科目选择结果
		combineAlgorithm = new CombineAlgorithmInt(cSize, 2);
		result = combineAlgorithm.getResutl();
		List<NewGkConditionDto> newConditionList2 = newGkChoiceService.findSubRes(courseList, dtoMap, result, 2);

		HSSFSheet sheet = workbook.createSheet("年级选课结果统计");

		int rowIndex = 0;
		Map<String, Integer> rowLocationMap = new HashMap<>();

		// 单科统计结果
		HSSFRow titleRow = sheet.createRow(rowIndex++);
		titleRow.setHeightInPoints((2f) * sheet.getDefaultRowHeightInPoints());
		CellRangeAddress car = new CellRangeAddress(0, 0, 0, 1);
		sheet.addMergedRegion(car);
		HSSFCell titleCell = titleRow.createCell(0);
		titleCell.setCellStyle(titleStyle);
		titleRow.createCell(1).setCellStyle(titleStyle);
		titleCell.setCellValue(new HSSFRichTextString("单科统计结果"));
		titleCell = titleRow.createCell(2);
		titleCell.setCellStyle(titleStyle);
		titleCell.setCellValue(new HSSFRichTextString("总计"));
		rowLocationMap.put("单科统计结果", rowIndex - 1);
		int index = 1;
		for (Map.Entry<String, Integer> entry : courseNameCountMap.entrySet()) {
			if (Integer.valueOf(0).equals(entry.getValue())) {
				continue;
			}
			HSSFRow rowTmp = sheet.createRow(rowIndex++);
			rowTmp.setHeightInPoints((1.5f) * sheet.getDefaultRowHeightInPoints());
			HSSFCell cellTmp = rowTmp.createCell(0);
			cellTmp.setCellStyle(cellStyle);
			cellTmp.setCellValue(new HSSFRichTextString(String.valueOf(index++)));
			cellTmp = rowTmp.createCell(1);
			cellTmp.setCellStyle(cellStyle);
			cellTmp.setCellValue(new HSSFRichTextString(entry.getKey()));
			rowLocationMap.put(entry.getKey(), rowIndex - 1);
			cellTmp = rowTmp.createCell(2);
			cellTmp.setCellStyle(cellStyle);
			cellTmp.setCellValue(new HSSFRichTextString(String.valueOf(entry.getValue())));
		}

		// 两科统计结果
		titleRow = sheet.createRow(rowIndex);
		titleRow.setHeightInPoints((2f) * sheet.getDefaultRowHeightInPoints());
		car = new CellRangeAddress(rowIndex, rowIndex, 0, 1);
		rowIndex++;
		sheet.addMergedRegion(car);
		titleCell = titleRow.createCell(0);
		titleCell.setCellStyle(titleStyle);
		titleRow.createCell(1).setCellStyle(titleStyle);
		titleCell.setCellValue(new HSSFRichTextString("两科统计结果"));
		titleCell = titleRow.createCell(2);
		titleCell.setCellStyle(titleStyle);
		titleCell.setCellValue(new HSSFRichTextString("总计"));
		rowLocationMap.put("两科统计结果", rowIndex - 1);
		index = 1;
		for (NewGkConditionDto one : newConditionList2) {
			if (Integer.valueOf(0).equals(one.getSumNum())) {
				continue;
			}
			HSSFRow rowTmp = sheet.createRow(rowIndex++);
			rowTmp.setHeightInPoints((1.5f) * sheet.getDefaultRowHeightInPoints());
			HSSFCell cellTmp = rowTmp.createCell(0);
			cellTmp.setCellStyle(cellStyle);
			cellTmp.setCellValue(new HSSFRichTextString(String.valueOf(index++)));
			cellTmp = rowTmp.createCell(1);
			cellTmp.setCellStyle(cellStyle);
			cellTmp.setCellValue(new HSSFRichTextString(one.getSubShortNames()));
			rowLocationMap.put(one.getSubShortNames(), rowIndex - 1);
			cellTmp = rowTmp.createCell(2);
			cellTmp.setCellStyle(cellStyle);
			cellTmp.setCellValue(new HSSFRichTextString(String.valueOf(one.getSumNum())));
		}

		// 三科统计结果
		titleRow = sheet.createRow(rowIndex);
		titleRow.setHeightInPoints((2f) * sheet.getDefaultRowHeightInPoints());
		car = new CellRangeAddress(rowIndex, rowIndex, 0, 1);
		rowIndex++;
		sheet.addMergedRegion(car);
		titleCell = titleRow.createCell(0);
		titleCell.setCellStyle(titleStyle);
		titleRow.createCell(1).setCellStyle(titleStyle);
		titleCell.setCellValue(new HSSFRichTextString("三科统计结果"));
		titleCell = titleRow.createCell(2);
		titleCell.setCellStyle(titleStyle);
		titleCell.setCellValue(new HSSFRichTextString("总计"));
		rowLocationMap.put("三科统计结果", rowIndex - 1);
		index = 1;
		for (NewGkConditionDto one : newConditionList3) {
			if (Integer.valueOf(0).equals(one.getSumNum())) {
				continue;
			}
			HSSFRow rowTmp = sheet.createRow(rowIndex++);
			rowTmp.setHeightInPoints((1.5f) * sheet.getDefaultRowHeightInPoints());
			HSSFCell cellTmp = rowTmp.createCell(0);
			cellTmp.setCellStyle(cellStyle);
			cellTmp.setCellValue(new HSSFRichTextString(String.valueOf(index++)));
			cellTmp = rowTmp.createCell(1);
			cellTmp.setCellStyle(cellStyle);
			cellTmp.setCellValue(new HSSFRichTextString(one.getSubShortNames()));
			rowLocationMap.put(one.getSubShortNames(), rowIndex - 1);
			cellTmp = rowTmp.createCell(2);
			cellTmp.setCellStyle(cellStyle);
			cellTmp.setCellValue(new HSSFRichTextString(String.valueOf(one.getSumNum())));
		}

		// 已选未选
		titleRow = sheet.createRow(rowIndex);
		titleRow.setHeightInPoints((2f) * sheet.getDefaultRowHeightInPoints());
		car = new CellRangeAddress(rowIndex, rowIndex, 0, 1);
		rowIndex++;
		sheet.addMergedRegion(car);
		titleCell = titleRow.createCell(0);
		titleCell.setCellStyle(titleStyle);
		titleRow.createCell(1).setCellStyle(titleStyle);
		titleCell.setCellValue(new HSSFRichTextString("整体统计结果"));
		titleCell = titleRow.createCell(2);
		titleCell.setCellStyle(titleStyle);
		titleCell.setCellValue(new HSSFRichTextString("总计"));
		rowLocationMap.put("整体统计结果", rowIndex - 1);

		index = 1;
		HSSFRow totalRowTmp = sheet.createRow(rowIndex++);
		totalRowTmp.setHeightInPoints((1.5f) * sheet.getDefaultRowHeightInPoints());
		HSSFCell totalCellTmp = totalRowTmp.createCell(0);
		totalCellTmp.setCellStyle(cellStyle);
		totalCellTmp.setCellValue(new HSSFRichTextString(String.valueOf(index++)));
		totalCellTmp = totalRowTmp.createCell(1);
		totalCellTmp.setCellStyle(cellStyle);
		totalCellTmp.setCellValue(new HSSFRichTextString("已选"));
		rowLocationMap.put("已选", rowIndex - 1);
		totalCellTmp = totalRowTmp.createCell(2);
		totalCellTmp.setCellStyle(cellStyle);
		totalCellTmp.setCellValue(new HSSFRichTextString(String.valueOf(chosenStuIds.size())));

		totalRowTmp = sheet.createRow(rowIndex++);
		totalRowTmp.setHeightInPoints((1.5f) * sheet.getDefaultRowHeightInPoints());
		totalCellTmp = totalRowTmp.createCell(0);
		totalCellTmp.setCellStyle(cellStyle);
		totalCellTmp.setCellValue(new HSSFRichTextString(String.valueOf(index++)));
		totalCellTmp = totalRowTmp.createCell(1);
		totalCellTmp.setCellStyle(cellStyle);
		totalCellTmp.setCellValue(new HSSFRichTextString("未选"));
		rowLocationMap.put("未选", rowIndex - 1);
		totalCellTmp = totalRowTmp.createCell(2);
		totalCellTmp.setCellStyle(cellStyle);
		totalCellTmp.setCellValue(new HSSFRichTextString(String.valueOf(studentList.size() - chosenStuIds.size())));

		int col = 3;
		HSSFSheet mainSheet = sheet;
		if (CollectionUtils.isNotEmpty(classList)) {
			for (Clazz classTmp : classList) {
				dtoMap = new HashMap<>();
				chosenStuIds = new HashSet<>();
				courseIdCountMap = new HashMap<>();
				titleRow = mainSheet.getRow(0);
				titleCell = titleRow.createCell(col);
				titleCell.setCellStyle(titleStyle);
				titleCell.setCellValue(new HSSFRichTextString(classTmp.getClassName()));
				List<String> thisClassStudent = classIdToStudentsMap.get(classTmp.getId()) == null ? new ArrayList<>() : classIdToStudentsMap.get(classTmp.getId());
				List<NewGkChoResult> subResultList = resultList.stream().filter(e -> thisClassStudent.contains(e.getStudentId())).collect(Collectors.toList());
				if (CollectionUtils.isNotEmpty(resultList)) {
					for (NewGkChoResult resultTmp : subResultList) {
						chosenStuIds.add(resultTmp.getStudentId());
						if (!dtoMap.containsKey(resultTmp.getStudentId())) {
							dto = new NewGkChoResultDto();
							dto.setChooseSubjectIds(new HashSet<>());
							dto.setStudentId(resultTmp.getStudentId());
							dtoMap.put(resultTmp.getStudentId(), dto);
						}
						dtoMap.get(resultTmp.getStudentId()).getChooseSubjectIds().add(resultTmp.getSubjectId());

						Integer integer = courseIdCountMap.get(resultTmp.getSubjectId());
						if (integer == null) {
							integer = 0;
						}
						courseIdCountMap.put(resultTmp.getSubjectId(), ++integer);
					}
				}
				courseNameCountMap = new HashMap<>();
				for (Course course : courseList) {
					Integer integer = courseIdCountMap.get(course.getId());
					if (integer == null) {
						integer = 0;
					}
					courseNameCountMap.put(course.getSubjectName(), integer);
				}

				// 各科目选择结果
				courseNameCountMap = sortByValue(courseNameCountMap);
				// 计算组合
				cSize = new Integer[courseList.size()];
				for (int i = 0; i < courseList.size(); i++) {
					cSize[i] = i;
				}

				// 三科目选择结果
				combineAlgorithm = new CombineAlgorithmInt(cSize, 3);
				result = combineAlgorithm.getResutl();
				newConditionList3 = newGkChoiceService.findSubRes(courseList, dtoMap, result, 3);
				// 新增过滤筛选组合不推荐课程
				if (CollectionUtils.isNotEmpty(objectValues)) {
					for (String subjectIdstr : objectValues) {
						String[] subjectIds = subjectIdstr.split(",");
						if (subjectIds.length == 3) {
							for (NewGkConditionDto newCondition : newConditionList3) {
								Set<String> subs = newCondition.getSubjectIds();
								if (subs.contains(subjectIds[0]) && subs.contains(subjectIds[1]) && subs.contains(subjectIds[2])) {
									newCondition.setLimitSubject(true);
								}
							}
						}
					}
				}

				// 二科目选择结果
				combineAlgorithm = new CombineAlgorithmInt(cSize, 2);
				result = combineAlgorithm.getResutl();
				newConditionList2 = newGkChoiceService.findSubRes(courseList, dtoMap, result, 2);

				// HSSFSheet sheetTmp = workbook.createSheet(classTmp.getClassNameDynamic() + "选课结果统计");

				rowIndex = 0;

				// 单科统计结果
				/*titleRow = sheetTmp.createRow(rowIndex);
				titleRow.setHeightInPoints((1.5f) * sheetTmp.getDefaultRowHeightInPoints());
				car = new CellRangeAddress(rowIndex, rowIndex, 0, 2);
				rowIndex++;
				sheetTmp.addMergedRegion(car);
				titleCell = titleRow.createCell(0);
				titleCell.setCellStyle(titleStyle);
				titleCell.setCellValue(new HSSFRichTextString("单科统计结果"));*/

				titleRow = mainSheet.getRow(rowLocationMap.get("单科统计结果"));
				titleCell = titleRow.createCell(col);
				titleCell.setCellStyle(titleStyle);
				titleCell.setCellValue(new HSSFRichTextString(classTmp.getClassName()));

				index = 1;
				for (Entry<String, Integer> entry : courseNameCountMap.entrySet()) {
					/*if (Integer.valueOf(0).equals(entry.getValue())) {
						continue;
					}*/
					/*HSSFRow rowTmp = sheetTmp.createRow(rowIndex++);
					HSSFCell cellTmp = rowTmp.createCell(0);
					cellTmp.setCellValue(new HSSFRichTextString(String.valueOf(index++)));
					cellTmp = rowTmp.createCell(1);
					cellTmp.setCellValue(new HSSFRichTextString(entry.getKey()));
					cellTmp = rowTmp.createCell(2);
					cellTmp.setCellValue(new HSSFRichTextString(String.valueOf(entry.getValue())));*/

					if (rowLocationMap.get(entry.getKey()) != null) {
						HSSFRow rowTmp = mainSheet.getRow(rowLocationMap.get(entry.getKey()));
						HSSFCell cellTmp = rowTmp.createCell(col);
						cellTmp.setCellStyle(cellStyle);
						cellTmp.setCellValue(new HSSFRichTextString(String.valueOf(entry.getValue())));
					}
				}

				// 两科统计结果
				/*titleRow = sheetTmp.createRow(rowIndex);
				titleRow.setHeightInPoints((1.5f) * sheetTmp.getDefaultRowHeightInPoints());
				car = new CellRangeAddress(rowIndex, rowIndex, 0, 2);
				rowIndex++;
				sheetTmp.addMergedRegion(car);
				titleCell = titleRow.createCell(0);
				titleCell.setCellStyle(titleStyle);
				titleCell.setCellValue(new HSSFRichTextString("两科统计结果"));*/

				titleRow = mainSheet.getRow(rowLocationMap.get("两科统计结果"));
				titleCell = titleRow.createCell(col);
				titleCell.setCellStyle(titleStyle);
				titleCell.setCellValue(new HSSFRichTextString(classTmp.getClassName()));

				index = 1;
				for (NewGkConditionDto one : newConditionList2) {
					/*if (Integer.valueOf(0).equals(one.getSumNum())) {
						continue;
					}*/
					/*HSSFRow rowTmp = sheetTmp.createRow(rowIndex++);
					HSSFCell cellTmp = rowTmp.createCell(0);
					cellTmp.setCellValue(new HSSFRichTextString(String.valueOf(index++)));
					cellTmp = rowTmp.createCell(1);
					cellTmp.setCellValue(new HSSFRichTextString(one.getSubShortNames()));
					cellTmp = rowTmp.createCell(2);
					cellTmp.setCellValue(new HSSFRichTextString(String.valueOf(one.getSumNum())));*/

					if (rowLocationMap.get(one.getSubShortNames()) != null) {
						HSSFRow rowTmp = mainSheet.getRow(rowLocationMap.get(one.getSubShortNames()));
						HSSFCell cellTmp = rowTmp.createCell(col);
						cellTmp.setCellStyle(cellStyle);
						cellTmp.setCellValue(new HSSFRichTextString(String.valueOf(one.getSumNum())));
					}
				}

				// 三科统计结果
				/*titleRow = sheetTmp.createRow(rowIndex);
				titleRow.setHeightInPoints((1.5f) * sheetTmp.getDefaultRowHeightInPoints());
				car = new CellRangeAddress(rowIndex, rowIndex, 0, 2);
				rowIndex++;
				sheetTmp.addMergedRegion(car);
				titleCell = titleRow.createCell(0);
				titleCell.setCellStyle(titleStyle);
				titleCell.setCellValue(new HSSFRichTextString("三科统计结果"));*/

				titleRow = mainSheet.getRow(rowLocationMap.get("三科统计结果"));
				titleCell = titleRow.createCell(col);
				titleCell.setCellStyle(titleStyle);
				titleCell.setCellValue(new HSSFRichTextString(classTmp.getClassName()));

				index = 1;
				for (NewGkConditionDto one : newConditionList3) {
					/*if (Integer.valueOf(0).equals(one.getSumNum())) {
						continue;
					}*/
					/*HSSFRow rowTmp = sheetTmp.createRow(rowIndex++);
					HSSFCell cellTmp = rowTmp.createCell(0);
					cellTmp.setCellValue(new HSSFRichTextString(String.valueOf(index++)));
					cellTmp = rowTmp.createCell(1);
					cellTmp.setCellValue(new HSSFRichTextString(one.getSubShortNames()));
					cellTmp = rowTmp.createCell(2);
					cellTmp.setCellValue(new HSSFRichTextString(String.valueOf(one.getSumNum())));*/

					if (rowLocationMap.get(one.getSubShortNames()) != null) {
						HSSFRow rowTmp = mainSheet.getRow(rowLocationMap.get(one.getSubShortNames()));
						HSSFCell cellTmp = rowTmp.createCell(col);
						cellTmp.setCellStyle(cellStyle);
						cellTmp.setCellValue(new HSSFRichTextString(String.valueOf(one.getSumNum())));
					}
				}

				// 已选未选
				/*titleRow = sheetTmp.createRow(rowIndex);
				titleRow.setHeightInPoints((1.5f) * sheetTmp.getDefaultRowHeightInPoints());
				car = new CellRangeAddress(rowIndex, rowIndex, 0, 2);
				rowIndex++;
				sheetTmp.addMergedRegion(car);
				titleCell = titleRow.createCell(0);
				titleCell.setCellStyle(titleStyle);
				titleCell.setCellValue(new HSSFRichTextString("整体统计结果"));*/

				titleRow = mainSheet.getRow(rowLocationMap.get("整体统计结果"));
				titleCell = titleRow.createCell(col);
				titleCell.setCellStyle(titleStyle);
				titleCell.setCellValue(new HSSFRichTextString(classTmp.getClassName()));

				index = 1;
				/*totalRowTmp = sheetTmp.createRow(rowIndex++);
				totalCellTmp = totalRowTmp.createCell(0);
				totalCellTmp.setCellValue(new HSSFRichTextString(String.valueOf(index++)));
				totalCellTmp = totalRowTmp.createCell(1);
				totalCellTmp.setCellValue(new HSSFRichTextString("已选"));
				totalCellTmp = totalRowTmp.createCell(2);
				totalCellTmp.setCellValue(new HSSFRichTextString(String.valueOf(chosenStuIds.size())));*/
				if (rowLocationMap.get("已选") != null) {
					totalRowTmp = mainSheet.getRow(rowLocationMap.get("已选"));
					totalCellTmp = totalRowTmp.createCell(col);
					totalCellTmp.setCellStyle(cellStyle);
					totalCellTmp.setCellValue(new HSSFRichTextString(String.valueOf(chosenStuIds.size())));
				}

				/*totalRowTmp = sheetTmp.createRow(rowIndex++);
				totalCellTmp = totalRowTmp.createCell(0);
				totalCellTmp.setCellValue(new HSSFRichTextString(String.valueOf(index++)));
				totalCellTmp = totalRowTmp.createCell(1);
				totalCellTmp.setCellValue(new HSSFRichTextString("未选"));
				totalCellTmp = totalRowTmp.createCell(2);
				totalCellTmp.setCellValue(new HSSFRichTextString(String.valueOf(thisClassStudent.size() - chosenStuIds.size())));*/
				if (rowLocationMap.get("未选") != null) {
					totalRowTmp = mainSheet.getRow(rowLocationMap.get("未选"));
					totalCellTmp = totalRowTmp.createCell(col);
					totalCellTmp.setCellStyle(cellStyle);
					totalCellTmp.setCellValue(new HSSFRichTextString(String.valueOf(thisClassStudent.size() - chosenStuIds.size())));
				}

				col++;
			}
		}

		String fileName = "选课结果统计";
		ExportUtils.outputData(workbook, fileName, response);
	}

	@RequestMapping("/change/log/list/page")
	public String getChangeLog(@PathVariable String choiceId, String studentCode, String studentName, String operatorName, HttpServletRequest request, ModelMap map) {
		List<NewGkTeachUpStuLog> logList = newGkTeachUpStuLogService.findByChoiceId(choiceId, null);
		Pagination page = createPagination(request);
		if (StringUtils.isNotBlank(studentCode)) {
			logList = logList.stream().filter(e -> e.getStudentCode().indexOf(studentCode) > -1).collect(Collectors.toList());
			map.put("searchText", studentCode);
			map.put("searchType", "studentCode");
		} else if (StringUtils.isNotBlank(studentName)) {
			logList = logList.stream().filter(e -> e.getStudentName().indexOf(studentName) > -1).collect(Collectors.toList());
			map.put("searchText", studentName);
			map.put("searchType", "studentName");
		} else if (StringUtils.isNotBlank(operatorName)) {
			logList = logList.stream().filter(e -> e.getOperatorName().indexOf(operatorName) > -1).collect(Collectors.toList());
			map.put("searchText", operatorName);
			map.put("searchType", "operatorName");
		}
		page.setMaxRowCount(logList.size());
		int end = page.getPageIndex() * page.getPageSize();
		logList = logList.subList((page.getPageIndex() - 1) * page.getPageSize(), end > logList.size() ? logList.size() : end);
		page.initialize();
		sendPagination(request, map, page);
		map.put("logList", logList);
		map.put("choiceId", choiceId);
		return "/newgkelective/chosen/changeLog.ftl";
	}

	@RequestMapping("/resource/evaluation/list/page")
	public String getResourceEvaluation(@PathVariable String choiceId, ModelMap map) {
		NewGkChoice choice = newGkChoiceService.findById(choiceId);

		// 获取选课科目
		String unitId = getLoginInfo().getUnitId();
		List<String> courseIdList = newGkChoRelationService.findByChoiceIdAndObjectType(unitId, choiceId, NewGkElectiveConstant.CHOICE_TYPE_01);
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIdList.toArray(new String[0])), Course.class);

		// 理想开班人数
		int studentNum = (int) studentRemoteService.CountStudByGradeId(choice.getGradeId());
		int classNum = SUtils.dt(classRemoteService.findByGradeId(choice.getUnitId(), choice.getGradeId(), null), Clazz.class).size();
		int average = studentNum / classNum;

		// 各科选考学考人数
		Map<String, Integer> examCourseCount = new HashMap<>();
		Map<String, Integer> studyCourseCount = new HashMap<>();
		List<NewGkChoResult> chosenList = newGkChoResultService.findByChoiceIdAndKindType(unitId, NewGkElectiveConstant.KIND_TYPE_01, choiceId);
		Map<String, Set<String>> chosenMap = chosenList.stream().collect(Collectors.groupingBy(NewGkChoResult::getStudentId,
				Collectors.mapping(NewGkChoResult::getSubjectId, Collectors.toSet())));
		Set<String> examTmp;
		Set<String> studyTmp;
		for (Map.Entry<String, Set<String>> one : chosenMap.entrySet()) {
			examTmp = one.getValue();
			studyTmp = new HashSet<>(CollectionUtils.subtract(courseIdList, examTmp));
			for (String courseId : courseIdList) {
				if (examCourseCount.get(courseId) == null) {
					examCourseCount.put(courseId, Integer.valueOf(0));
				}
				if (examTmp.contains(courseId)) {
					Integer tmp = examCourseCount.get(courseId);
					examCourseCount.put(courseId, ++tmp);
				}
				if (studyCourseCount.get(courseId) == null) {
					studyCourseCount.put(courseId, Integer.valueOf(0));
				}
				if (studyTmp.contains(courseId)) {
					Integer tmp = studyCourseCount.get(courseId);
					studyCourseCount.put(courseId, ++tmp);
				}
			}
		}

		// 各科目课时
		List<NewGkSubjectTime> subjectTimes = newGkSubjectTimeService.findListBy("arrayItemId", choice.getGradeId());
		Map<String, Integer> subjectTimeMap = subjectTimes.stream().collect(Collectors.toMap(e -> e.getSubjectId() + e.getSubjectType(), e -> e.getPeriod()));

		// 教师数
		List<NewGkTeacherPlan> teacherPlans = newGkTeacherPlanService.findByArrayItemIds(new String[]{choice.getGradeId()}, true);
		Map<String, String> teacherPlanMap = teacherPlans.stream().collect(Collectors.toMap(e -> e.getSubjectId(), e -> e.getTeacherCounts()));

		List<NewGkChoResourceDto> resourceDtoList = new ArrayList<>();
		for (Course course : courseList) {
			NewGkChoResourceDto tmp = new NewGkChoResourceDto();
			tmp.setCourseName(course.getSubjectName());

			tmp.setExamChosenNum(examCourseCount.getOrDefault(course.getId(), 0));
			tmp.setExamClassNum(new BigDecimal(tmp.getExamChosenNum()).divide(new BigDecimal(average), 0, BigDecimal.ROUND_HALF_UP).intValue());
			tmp.setExamClassStudentAverage(tmp.getExamClassNum() == 0 ? 0 : tmp.getExamChosenNum() / tmp.getExamClassNum());
			tmp.setExamTimeCount(subjectTimeMap.getOrDefault(course.getId() + "A", 0));
			tmp.setExamTotalTimeCount(tmp.getExamClassNum() * tmp.getExamTimeCount());

			tmp.setStudyChosenNum(studyCourseCount.getOrDefault(course.getId(), 0));
			tmp.setStudyClassNum(new BigDecimal(tmp.getStudyChosenNum()).divide(new BigDecimal(average), 0, BigDecimal.ROUND_HALF_UP).intValue());
			tmp.setStudyClassStudentAverage(tmp.getStudyClassNum() == 0 ? 0 : tmp.getStudyChosenNum() / tmp.getStudyClassNum());
			tmp.setStudyTimeCount(subjectTimeMap.getOrDefault(course.getId() + "B", 0));
			tmp.setStudyTotalTimeCount(tmp.getStudyClassNum() * tmp.getStudyTimeCount());

			tmp.setTotalTimeCount(tmp.getExamTotalTimeCount() + tmp.getStudyTotalTimeCount());
			tmp.setTeacherCount(Integer.valueOf(teacherPlanMap.getOrDefault(course.getId(), "0")));
			tmp.setTotalTimeAverage(tmp.getTeacherCount() > 0 ? new BigDecimal(tmp.getTotalTimeCount()).divide(new BigDecimal(tmp.getTeacherCount()), 1, BigDecimal.ROUND_HALF_UP).toString() : "0");
			resourceDtoList.add(tmp);
		}
		map.put("resultList", resourceDtoList);

		return "/newgkelective/chosen/chosenResource.ftl";
	}
	
	private List<StudentResultDto> findNotChosen(String unitId,String choiceId,ChosenSearchDto dto, String isMaster){
		//暂时不分页形式
		if(Constant.IS_TRUE_Str.equals(isMaster)){
			return newGkChoResultService.findNotChosenListWithMaster(unitId,choiceId,dto);
		}else{
			return newGkChoResultService.findNotChosenList(unitId,choiceId,dto);
		}
	}
	
	private List<StudentResultDto> findChosen(String unitId,String choiceId,ChosenSearchDto dto, String isMaster){
		//暂时不分页形式
		if(Constant.IS_TRUE_Str.equals(isMaster)){
			return newGkChoResultService.findChosenListWithMaster(unitId,choiceId,dto);
		}else{
			return newGkChoResultService.findChosenList(unitId,choiceId,dto);
			
		}
	}
	
	private Set<String[]> notChooseSubjects(String unitId,String choiceId){
		List<String> limitChoList = newGkChoRelationService
				.findByChoiceIdAndObjectType(unitId,
						choiceId, NewGkElectiveConstant.CHOICE_TYPE_02);
    	if(CollectionUtils.isNotEmpty(limitChoList)){
    		Set<String[]> returnSet = new HashSet<String[]>();
    		for(String s:limitChoList){
    			returnSet.add(s.split(","));
    		}
    		return returnSet;
    	}else{
    		return new HashSet<String[]>();
    	}
	}
	
	private boolean checkSubjects(String[] checkSubjects,Set<String[]> subjectIds){
		for (String[] limitSubject : subjectIds) {
    		List<String> limitSubjectList = Arrays.asList(checkSubjects);
    		if (limitSubjectList.containsAll(Arrays.asList(limitSubject))) {
    			 return true;
    		}
    	}
		return false;
	}
	
	
	
	@ResponseBody
    @RequestMapping("/chosenSubject/saveAll")
    @ControllerInfo(value = "批量保存未选课学生的选课信息")
    public String doSaveAllGkResult(@PathVariable  String choiceId,NewGkResultSaveDto dto, ModelMap map) {
		NewGkChoice gkChoice=newGkChoiceService.findOne(choiceId);
		int chooseNum = gkChoice.getChooseNum();
		//不推荐组合
		List<NewGkChoResult> resultList=new ArrayList<NewGkChoResult>();
		//先删除后新增 删除学生id
		Set<String> stuId=new HashSet<String>();
		//不参与学生id 同时也是要清空已选数据
		Set<String> noJoinStuId=new HashSet<String>();
		
		Set<String> moveNoJoin=new HashSet<String>();
		
		NewGkChoResult result=null;
		Set<String[]> set = notChooseSubjects(gkChoice.getUnitId(),choiceId);
		boolean flag=false;
		boolean flag3=false;

    	// 判断各类目选课数量是否在限定范围内
    	List<NewGkChoCategory> categoryList = newGkChoCategoryService.findByChoiceId(gkChoice.getUnitId(), choiceId);
    	List<List<String>> combinationList = new ArrayList<List<String>>();
    	if(CollectionUtils.isNotEmpty(categoryList)){
    		categoryList.stream().filter(a -> NewGkElectiveConstant.CATEGORY_TYPE_2.equals(a.getCategoryType())).collect(Collectors.toList())
    		.forEach(e->combinationList.add(e.getCourseList()));
    		categoryList= categoryList.stream().filter(a -> NewGkElectiveConstant.CATEGORY_TYPE_1.equals(a.getCategoryType())).collect(Collectors.toList());
    	}
		for(StudentResultDto stuDto:dto.getResultDtoList()){

			if (NewGkElectiveConstant.IF_1.equals(stuDto.getNojoinChoose())) {
                //不参与
				noJoinStuId.add(stuDto.getStudentId());
				stuId.add(stuDto.getStudentId());
            }else{
            	moveNoJoin.add(stuDto.getStudentId());
            	if(stuDto.getCourseIds()==null || stuDto.getCourseIds().length<=0){
            		continue;
            	}
            	//判断数量
            	if (stuDto.getCourseIds().length != chooseNum) {
                    return error("请确认已选课的学生都选择了 " + chooseNum + "门课程");
                }

            	//判断选课类别限制
            	boolean flag2=false;
            	for (NewGkChoCategory category : categoryList) {
            		List<String> selectList = Arrays.stream(stuDto.getCourseIds()).collect(Collectors.toList());
            		int i=0;
            		//组合
            		List<List<String>> cList = category.getCourseLists();
            		if(CollectionUtils.isNotEmpty(cList)){
            			for(List<String> cl:cList){
            				if(CollectionUtils.isNotEmpty(cl)){
                        		if(selectList.containsAll(cl)){
                        			selectList.removeAll(cl);
                        			i+=1;
                        		}
                    		}
            			}
            		}else{
            			if(CollectionUtils.isNotEmpty(combinationList)){
            				for(List<String> cl:combinationList){
                				if(CollectionUtils.isNotEmpty(cl)){
                            		if(selectList.containsAll(cl)){
                            			selectList.removeAll(cl);
                            		}
                        		}
                			}
            			}
            		}
            		//单科
            		List<String> courseList = category.getCourseList();
            		if(CollectionUtils.isNotEmpty(courseList)){
        				int size = CollectionUtils.intersection(courseList, selectList).size();
            			i+=size;
            		}
            		
            		if(category.getMinNum()>i || i>category.getMaxNum()){
            			flag2=true;
            			break;
            		}
            	}
            	if(flag2){
            		if(!flag3){
        				flag3=true;
        			}
            		continue;
            	}
            	
            	//判断禁选组合
            	if(set!=null && set.size()>0){
            		//判断学生选择结果属于禁选组合
            		if(checkSubjects(stuDto.getCourseIds(), set)){
            			if(!flag){
            				flag=true;
            			}
            			continue;
            		}
            	}
            	stuId.add(stuDto.getStudentId());
            	for(String course:stuDto.getCourseIds()){
            		result=new NewGkChoResult();
            		result.setId(UuidUtils.generateUuid());
            		result.setUnitId(gkChoice.getUnitId());
            		result.setCreationTime(new Date());
            		result.setModifyTime(new Date());
            		result.setChoiceId(choiceId);
            		result.setStudentId(stuDto.getStudentId());
            		result.setSubjectId(course);
            		result.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_1);
            		result.setKindType(NewGkElectiveConstant.KIND_TYPE_01);
            		resultList.add(result);
            	}
            }
		}
		
        try {
            if(stuId.size()>0){
            	String[] stuIds = stuId.toArray(new String[]{});
            	String[] noJoinStuIds=null;
            	if(noJoinStuId.size()>0){
            		noJoinStuIds=noJoinStuId.toArray(new String[]{});
            	}
            	newGkChoResultService.saveAllResult(gkChoice.getUnitId(),choiceId,resultList,stuIds,noJoinStuIds,moveNoJoin.toArray(new String[]{}));
            	if(CollectionUtils.isEmpty(resultList)){
            		if(flag&&flag3){
        				return success("所选结果为不推荐组合或不符合类别门数限制，其余数据保存成功！");
            		}else if(flag){
                		return success("所选结果为不推荐组合，其余数据保存成功！");
                	}else if(flag3){
                		return success("所选结果不符合类别门数限制，其余数据保存成功！");
                	}
            	}else{
            		if(flag&&flag3){
            			return success("部分选择了不推荐组合或不符合类别门数限制，其余保存成功！");
            		}else if(flag){
            			return success("部分选择了不推荐组合，其余保存成功！");
            		}else if(flag3){
            			return success("部分选择不符合类别门数限制，其余保存成功！");
            		}
            	}
            }else{
            	String[] moveNoJoinIds=null;
            	if(moveNoJoin.size()>0){
            		moveNoJoinIds=moveNoJoin.toArray(new String[]{});
            		newGkChoResultService.saveAllResult(gkChoice.getUnitId(),choiceId,resultList,null,null,moveNoJoinIds);
            		if(CollectionUtils.isEmpty(resultList)){
	            		if(flag&&flag3){
            				return success("所选结果为不推荐组合或不符合类别门数限制，其余数据保存成功！");
	            		}else if(flag){
	                		return success("所选结果为不推荐组合，其余数据保存成功！");
	                	}else if(flag3){
	                		return success("所选结果不符合类别门数限制，其余数据保存成功！");
	                	}
                	}else{
                		if(flag&&flag3){
                			return success("部分选择了不推荐组合或不符合类别门数限制，其余保存成功！");
                		}else if(flag){
                			return success("部分选择了不推荐组合，其余保存成功！");
                		}else if(flag3){
                			return success("部分选择不符合类别门数限制，其余保存成功！");
                		}
                	}
            	}else{
	            	if(flag){
	            		return error("没有数据需要保存！都选择了不推荐组合！");
	            	}
	            	return error("没有数据需要保存！");
            	}
            }
        }catch (Exception e) {
            e.printStackTrace();
            return returnError("保存失败！", e.getMessage());
        }
        return success("保存成功！");
    }
	@ResponseBody
    @RequestMapping("/chosenSubject/lockAll")
    @ControllerInfo(value = "批量锁定或者解除")
    public String doSaveAllGkResult(@PathVariable  String choiceId,String lock) {
		NewGkChoice newChoice = newGkChoiceService.findOne(choiceId);
		
		String optionName="";
		try{
			boolean isLock=true;
			if(NewGkElectiveConstant.IF_1.equals(lock)){
				optionName="锁定";
			}else{
				optionName="解锁";
				isLock=false;
			}
			
			//权限控制
			LoginInfo loginInfo = getLoginInfo();
			Map<String, List<String>> roleMap = findRoleByUserId(loginInfo.getUnitId(), loginInfo.getUserId());
			if(isClassRole(roleMap, newChoice.getGradeId())){
				List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(newChoice.getUnitId(),newChoice.getGradeId()), new TR<List<Clazz>>(){});
				List<String> classIdList = roleMap.get(ROLE_CLASS);
				clazzList = clazzList.stream().filter(e->classIdList.contains(e.getId())).collect(Collectors.toList());
				List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(EntityUtils.getList(clazzList, Clazz::getId).toArray(new String[0])),Student.class);
				if(CollectionUtils.isEmpty(studentList)){
					return success("没有需要"+optionName+"的学生！");
				}
				String[] studentIds = EntityUtils.getList(studentList, Student::getId).toArray(new String[0]);
				newGkChoResultService.saveLock(loginInfo.getUnitId(),choiceId,isLock,studentIds);
			}else{
				newGkChoResultService.saveLock(loginInfo.getUnitId(),choiceId,isLock,null);
			}
			
	
		}catch (Exception e) {
			e.printStackTrace();
			return returnError("操作失败！", e.getMessage());
		}
		return success(optionName+"操作成功！");
	}
	
	@ResponseBody
    @RequestMapping("/chosenSubject/lockOne")
    @ControllerInfo(value = "批量锁定或者解除-{lock}")
    public String doSaveAllGkResult(@PathVariable  String choiceId,String lock,String studentId) {
		 String optionName="";
		 try{
			 boolean isLock=true;
			 if(NewGkElectiveConstant.IF_1.equals(lock)){
				 optionName="锁定";
			 }else{
				 optionName="解锁";
				 isLock=false;
			 }
			 if(StringUtils.isNotBlank(studentId)){
				 newGkChoResultService.saveLock(getLoginInfo().getUnitId(),choiceId,isLock,new String[]{studentId});
			 }
		 }catch (Exception e) {
	         e.printStackTrace();
	         return returnError("操作失败！", e.getMessage());
	     }
		 return success(optionName+"操作成功！");
	}
	
	@ResponseBody
    @RequestMapping("/chosenSubject/delete")
    @ControllerInfo(value = "删除学生选课结果，studentId{studentId}")
    public String doDelete(@PathVariable String choiceId,String studentId) {
		 String optionName="";
		 try{
			 if(StringUtils.isNotBlank(studentId)){
				 List<NewGkChoResult> oldChoiceResult = newGkChoResultService.findByChoiceIdAndStudentIdAndKindType(getLoginInfo().getUnitId(), NewGkElectiveConstant.KIND_TYPE_01, choiceId, studentId);
				 newGkChoResultService.deleteByStudentIds(getLoginInfo().getUnitId(),choiceId,new String[]{studentId});
				 // 操作记录
				 NewGkChoice gkChoice = newGkChoiceService.findById(choiceId);
				 Student student = studentRemoteService.findOneObjectById(studentId);
				 List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()), Course.class);
				 Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId, Course::getSubjectName);
				 NewGkTeachUpStuLog newGkTeachUpStuLog = new NewGkTeachUpStuLog();
				 newGkTeachUpStuLog.setId(UuidUtils.generateUuid());
				 newGkTeachUpStuLog.setChoiceId(choiceId);
				 newGkTeachUpStuLog.setOperatorName(getLoginInfo().getRealName());
				 newGkTeachUpStuLog.setUnitId(getLoginInfo().getUnitId());
				 newGkTeachUpStuLog.setGradeId(gkChoice.getGradeId());
				 newGkTeachUpStuLog.setClassId(student.getClassId());
				 newGkTeachUpStuLog.setStudentId(studentId);
				 newGkTeachUpStuLog.setStudentCode(student.getStudentCode());
				 newGkTeachUpStuLog.setStudentName(student.getStudentName());
				 newGkTeachUpStuLog.setModifyTime(new Date());
				 StringBuilder stringBuilder = new StringBuilder("删除选课信息");
				 for (NewGkChoResult one : oldChoiceResult) {
					 stringBuilder.append(courseNameMap.get(one.getSubjectId()));
				 }
				 newGkTeachUpStuLog.setRemark(stringBuilder.toString());
				 newGkTeachUpStuLogService.save(newGkTeachUpStuLog);
			 }else{
				 return success("没有数据可删除！");
			 }
		 }catch (Exception e) {
	         e.printStackTrace();
	         return returnError("操作失败！", e.getMessage());
	     }
		 return success(optionName+"操作成功！");
	}
	@RequestMapping("/chosenSubject/edit/page")
	@ControllerInfo(value = "编辑学生选课结果")
	public String editResult(@PathVariable String choiceId,String studentId,ModelMap map){
		NewGkChoice gkChoice=newGkChoiceService.findOne(choiceId);
		int chooseNum = gkChoice.getChooseNum();
		map.put("chooseNum", chooseNum);
		map.put("choiceId", choiceId);
		// 所有供选择的学科
        List<Course> coursesList = newGkChoRelationService.findChooseSubject(choiceId, getLoginInfo().getUnitId());
        map.put("coursesList", coursesList);
        Student student =null;
        if(StringUtils.isNotBlank(studentId)){
        	student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        }
        if(student!=null){
        	//学生存在
        	//学生选课结果
        	List<NewGkChoResult> list=newGkChoResultService.findByChoiceIdAndStudentIdAndKindType(gkChoice.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,choiceId,studentId);
        	Map<String, NewGkChoResult> chosenSubject = new HashMap<String, NewGkChoResult>();
        	for (NewGkChoResult g : list) {
                chosenSubject.put(g.getSubjectId(), g);
            }
        	Map<String, String> courseNameMap = EntityUtils.getMap(coursesList, Course::getId, Course::getSubjectName);
        	List<NewGkChoResult> wantToList=newGkChoResultService.findByChoiceIdAndStudentIdAndKindType(gkChoice.getUnitId(),NewGkElectiveConstant.KIND_TYPE_02,choiceId,studentId);
        	List<NewGkChoResult> noWantToList=newGkChoResultService.findByChoiceIdAndStudentIdAndKindType(gkChoice.getUnitId(),NewGkElectiveConstant.KIND_TYPE_03,choiceId,studentId);
        	String mark = "";
        	if(CollectionUtils.isNotEmpty(wantToList)){
        		mark = "优先调剂到（";
        		for (NewGkChoResult result : wantToList) {
					mark+=courseNameMap.get(result.getSubjectId())+"、";
				}
        		mark=mark.substring(0,mark.length()-1)+"）";
        	}
        	if(CollectionUtils.isNotEmpty(noWantToList)){
        		if(StringUtils.isNotBlank(mark))mark+="；";
        		mark+="明确不选（";
        		for (NewGkChoResult result : noWantToList) {
					mark+=courseNameMap.get(result.getSubjectId())+"、";
				}
        		mark=mark.substring(0,mark.length()-1)+"）";
        	}
        	map.put("mark", StringUtils.isBlank(mark)?"无":mark);
        	map.put("student", student);
            map.put("chosenSubject", chosenSubject);
            return "/newgkelective/chosen/chosenEdit.ftl";
        }
        /**
         * DOTO
         */
        return "/newgkelective/chosen/chosenAdd.ftl"; 
	}
	
	@ResponseBody
    @RequestMapping("/chosenSubject/save")
    @ControllerInfo(value = "保存学生选课信息，student[{studentId}]，subjectId[{subjectId}]")
    public String doSaveGkResult(@PathVariable String choiceId,String subjectId, String studentId, ModelMap map) {
		NewGkChoice gkChoice=newGkChoiceService.findOne(choiceId);
		List<NewGkChoResult> resultList = new ArrayList<NewGkChoResult>();
        // 获取设置的选课数
        String[] courseIds = subjectId.split(",");
        if (courseIds.length != gkChoice.getChooseNum()) {
            return error("请选择" + gkChoice.getChooseNum() + "门课程");
        }
        Set<String[]> set = notChooseSubjects(gkChoice.getUnitId(),choiceId);
        //判断禁选组合
    	if(set!=null && set.size()>0){
    		//判断学生选择结果属于禁选组合
    		if(checkSubjects(courseIds, set)){
    			return error("不推荐组合请调整后，再保存！");
    		}
    	}

    	// 判断各类目选课数量是否在限定范围内
    	List<NewGkChoCategory> categoryList = newGkChoCategoryService.findByChoiceId(gkChoice.getUnitId(), choiceId);
    	List<List<String>> combinationList = new ArrayList<List<String>>();
    	if(CollectionUtils.isNotEmpty(categoryList)){
    		categoryList.stream().filter(a -> NewGkElectiveConstant.CATEGORY_TYPE_2.equals(a.getCategoryType())).collect(Collectors.toList())
    		.forEach(e->combinationList.add(e.getCourseList()));
    		categoryList= categoryList.stream().filter(a -> NewGkElectiveConstant.CATEGORY_TYPE_1.equals(a.getCategoryType())).collect(Collectors.toList());
    	}
    	
    	for (NewGkChoCategory category : categoryList) {
    		//判断选课类别限制
    		List<String> selectList = Arrays.stream(courseIds).collect(Collectors.toList());
    		int i=0;
    		//组合
    		List<List<String>> cList = category.getCourseLists();
    		if(CollectionUtils.isNotEmpty(cList)){
    			for(List<String> cl:cList){
    				if(CollectionUtils.isNotEmpty(cl)){
                		if(selectList.containsAll(cl)){
                			selectList.removeAll(cl);
                			i+=1;
                		}
            		}
    			}
    		}else{
    			if(CollectionUtils.isNotEmpty(combinationList)){
    				for(List<String> cl:combinationList){
        				if(CollectionUtils.isNotEmpty(cl)){
                    		if(selectList.containsAll(cl)){
                    			selectList.removeAll(cl);
                    		}
                		}
        			}
    			}
    		}
    		//单科
    		List<String> courseList = category.getCourseList();
    		if(CollectionUtils.isNotEmpty(courseList)){
				int size = CollectionUtils.intersection(courseList, selectList).size();
    			i+=size;
    		}
    		if(i>category.getMaxNum()){
    			return error(category.getCategoryName()+"类别最多选"+category.getMaxNum()+"门");
    		}else if(i<category.getMinNum()){
    			return error(category.getCategoryName()+"类别最少选"+category.getMinNum()+"门");
    		}
    		
    	}

    	// 操作记录
		boolean different = false;
		List<NewGkChoResult> oldChoiceResult = newGkChoResultService.findByChoiceIdAndStudentIdAndKindType(getLoginInfo().getUnitId(), NewGkElectiveConstant.KIND_TYPE_01, choiceId, studentId);
		Student student = studentRemoteService.findOneObjectById(studentId);
    	List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()), Course.class);
    	Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId, Course::getSubjectName);
    	NewGkTeachUpStuLog newGkTeachUpStuLog = new NewGkTeachUpStuLog();
    	newGkTeachUpStuLog.setId(UuidUtils.generateUuid());
    	newGkTeachUpStuLog.setChoiceId(choiceId);
    	newGkTeachUpStuLog.setOperatorName(getLoginInfo().getRealName());
    	newGkTeachUpStuLog.setUnitId(getLoginInfo().getUnitId());
    	newGkTeachUpStuLog.setGradeId(gkChoice.getGradeId());
    	newGkTeachUpStuLog.setClassId(student.getClassId());
    	newGkTeachUpStuLog.setStudentId(studentId);
    	newGkTeachUpStuLog.setStudentCode(student.getStudentCode());
    	newGkTeachUpStuLog.setStudentName(student.getStudentName());
    	newGkTeachUpStuLog.setModifyTime(new Date());
    	StringBuilder stringBuilder = new StringBuilder("由");
    	if (CollectionUtils.isNotEmpty(oldChoiceResult)) {
			for (NewGkChoResult one : oldChoiceResult) {
				stringBuilder.append(courseNameMap.get(one.getSubjectId()));
			}
		} else {
    		stringBuilder.append("未选");
		}
    	stringBuilder.append("修改至");

    	NewGkChoResult result=null;
        for (int i = 0; i < courseIds.length; i++) {
        	result = new NewGkChoResult();
        	result.setId(UuidUtils.generateUuid());
        	result.setUnitId(gkChoice.getUnitId());
    		result.setCreationTime(new Date());
    		result.setModifyTime(new Date());
    		result.setChoiceId(choiceId);
    		result.setStudentId(studentId);
    		result.setSubjectId(courseIds[i]);
    		if (stringBuilder.indexOf(courseNameMap.get(courseIds[i])) < 0) {
    			different = true;
			}
    		stringBuilder.append(courseNameMap.get(courseIds[i]));
    		result.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_1);
    		result.setKindType(NewGkElectiveConstant.KIND_TYPE_01);
    		resultList.add(result);
        }
        newGkTeachUpStuLog.setRemark(stringBuilder.toString());
        try {
        	newGkChoResultService.saveAllResult(gkChoice.getUnitId(),choiceId,resultList,new String[]{studentId},null,new String[]{studentId});
			if (different) {
				newGkTeachUpStuLogService.save(newGkTeachUpStuLog);
			}
		}
        catch (Exception e) {
            e.printStackTrace();
            return returnError("保存失败！", e.getMessage());
        }
        return success("保存成功！");
    }
	
	
	@RequestMapping("/choiceResult/list/head")
	public String resultHead(@PathVariable String choiceId,ModelMap map){
		NewGkChoice newChoice=newGkChoiceService.findOne(choiceId);//选课
		if(newChoice==null) return errorFtl(map, "找不到选课");
		
		String gradeId=newChoice.getGradeId();
		map.put("gradeId", gradeId);
		List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(newChoice.getUnitId(),gradeId), new TR<List<Clazz>>(){});
		//权限控制
		LoginInfo loginInfo = getLoginInfo();
		Map<String, List<String>> roleMap = findRoleByUserId(loginInfo.getUnitId(), loginInfo.getUserId());
		if(isClassRole(roleMap, gradeId)){
			map.put("isClassRole", true);
			List<String> classIdList = roleMap.get(ROLE_CLASS);
			clazzList = clazzList.stream().filter(e->classIdList.contains(e.getId())).collect(Collectors.toList());
		}
		if (roleMap.get(ROLE_ADMIN) != null || (roleMap.get(ROLE_ADMIN) != null && roleMap.get(ROLE_ADMIN).contains(newChoice.getGradeId()))) {
			map.put("isGradeRole", true);
		}
		
		map.put("choiceName",newChoice.getChoiceName());
		map.put("choiceId", choiceId);
		map.put("clazzList", clazzList);
		return "/newgkelective/result/newResultHead.ftl";
	}

	/**
	 * 选课统计界面
	 * 从组合统计和分数统计两个维度展现
	 * "1"为组合统计
	 * "2"为分数统计
	 * @param choiceId
	 * @param classIds
	 * @param showType
	 * @param statisticType
	 * @param map
	 * @return
	 */
	@RequestMapping("/choiceResult/list/page")
	public String getResult(@PathVariable String choiceId, String classIds, String showType, String statisticType, String edge, ModelMap map) {
		map.put("showType", showType);
		NewGkChoice newChoice=newGkChoiceService.findOne(choiceId);
		if (Objects.equals("2", statisticType)) {
			if (StringUtils.isBlank(newChoice.getReferScoreId())) {
				return "/newgkelective/result/newResultScoreLevelCount.ftl";
			} else {
				map.put("isReferScoreImported", true);
			}
		}
		// 权限控制
		LoginInfo loginInfo = getLoginInfo();
		Map<String, List<String>> roleMap = findRoleByUserId(loginInfo.getUnitId(), loginInfo.getUserId());
		List<String> classIdList;
		if (isClassRole(roleMap, newChoice.getGradeId())) {
			classIdList = roleMap.get(ROLE_CLASS);
			List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(loginInfo.getUnitId(), newChoice.getGradeId()), Clazz.class);
			List<String> thisGradeClass = EntityUtils.getList(classList, Clazz::getId);
			classIdList = classIdList.stream().filter(e -> thisGradeClass.contains(e)).collect(Collectors.toList());
			if (StringUtils.isBlank(classIds)) {
				classIds = StringUtils.join(classIdList, ",");
			}
		}
		List<Student> studentList;
		if (StringUtils.isEmpty(classIds)) {
			studentList = SUtils.dt(studentRemoteService.findPartStudByGradeId(newChoice.getUnitId(), newChoice.getGradeId(), null, null), new TR<List<Student>>() {});
		} else {
			String[] classId = classIds.split(",");
			studentList = SUtils.dt(studentRemoteService.findPartStudByGradeId(newChoice.getUnitId(), null, classId, null), new TR<List<Student>>() {});
		}
		// 获取所有的选课结果
		List<NewGkChoResult> resultList = newGkChoResultService.findByKindTypeAndChoiceIdAndStudentIds(newChoice.getUnitId(), NewGkElectiveConstant.KIND_TYPE_01, choiceId, EntityUtils.getList(studentList, Student::getId).toArray(new String[0]));
		// 获取选考科目
		Map<String, List<NewGkChoRelation>> NewGkChoRelationAllMap = newGkChoRelationService.findByChoiceIdAndObjectTypeIn(newChoice.getUnitId(), choiceId,
				new String[] {NewGkElectiveConstant.CHOICE_TYPE_01, NewGkElectiveConstant.CHOICE_TYPE_04, NewGkElectiveConstant.CHOICE_TYPE_02});

		List<NewGkChoRelation> newGkChoRelationList = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01);
		List<String> courseIds = EntityUtils.getList(newGkChoRelationList, NewGkChoRelation::getObjectValue);
		if (CollectionUtils.isEmpty(courseIds)) {
			return errorFtl(map, "选课没有维护选课科目");
		}
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])), new TR<List<Course>>() {});
		Map<String, String> couserNameMap = EntityUtils.getMap(courseList, Course::getSubjectName, Course::getId);
		
		// 不参与选课排课人员
		List<NewGkChoRelation> notvals= NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_04);
		if(CollectionUtils.isNotEmpty(notvals)){
			List<String> studentIds = EntityUtils.getList(studentList, Student::getId);
			Iterator<NewGkChoRelation> iterator = notvals.iterator();
			while(iterator.hasNext()){
				NewGkChoRelation cho = iterator.next();
				if(!studentIds.contains(cho.getObjectValue())){
					iterator.remove();
				}
			}
		}
		// 获取禁选组合
		List<NewGkChoRelation> notvals2 = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_02);
		List<String> objectValues = null;
		if (CollectionUtils.isNotEmpty(notvals2)) {
			objectValues = EntityUtils.getList(notvals2, NewGkChoRelation::getObjectValue);
		}
		// 已选学生ids
		Set<String> chosenStuIds = new HashSet<>();
		NewGkChoResultDto dto;
		// 获取学生对应的选课ids
		Map<String, NewGkChoResultDto> dtoMap = new HashMap<>();
		Map<String, Integer> courseIdCountMap = new HashMap<>();
		Map<String, List<String>> courseSelectMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(resultList)) {
			for (NewGkChoResult result : resultList) {
				chosenStuIds.add(result.getStudentId());
				if (!dtoMap.containsKey(result.getStudentId())) {
					dto = new NewGkChoResultDto();
					dto.setChooseSubjectIds(new TreeSet<>());
					dto.setStudentId(result.getStudentId());
					dtoMap.put(result.getStudentId(), dto);
				}
				dtoMap.get(result.getStudentId()).getChooseSubjectIds().add(result.getSubjectId());

				Integer integer = courseIdCountMap.get(result.getSubjectId());
				if (integer == null) {
					integer = 0;
				}
				courseIdCountMap.put(result.getSubjectId(), ++integer);

				if (!courseSelectMap.containsKey(result.getSubjectId())) {
					courseSelectMap.put(result.getSubjectId(), new ArrayList<>());
				}
				courseSelectMap.get(result.getSubjectId()).add(result.getStudentId());
			}
		}
		Map<String, Integer> courseNameCountMap = new HashMap<>();
		for (Course course : courseList) {
			Integer integer = courseIdCountMap.get(course.getId());
			if (integer == null) {
				integer = 0;
			}
			courseNameCountMap.put(course.getSubjectName(), integer);
		}
      
        // 各科目选择结果
		courseNameCountMap = sortByValue(courseNameCountMap);
		// 计算组合
		Integer[] cSize = new Integer[courseList.size()];
		for (int i = 0; i < courseList.size(); i++) {
			cSize[i] = i;
		}

		// 三科目选择结果
		CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize, 3);
		Integer[][] result = combineAlgorithm.getResutl();
		List<NewGkConditionDto> newConditionList3 = newGkChoiceService.findSubRes(courseList, dtoMap, result, 3);
		// 新增过滤筛选组合不推荐课程
		if (CollectionUtils.isNotEmpty(objectValues)) {
			for (String subjectIdstr : objectValues) {
				String[] subjectIds = subjectIdstr.split(",");
				if (subjectIds.length == 3) {
					for (NewGkConditionDto newCondition : newConditionList3) {
						Set<String> subs = newCondition.getSubjectIds();
						if (subs.contains(subjectIds[0]) && subs.contains(subjectIds[1]) && subs.contains(subjectIds[2])) {
							newCondition.setLimitSubject(true);
						}
					}
				}
			}
		}
		map.put("newConditionList3", newConditionList3);
        // 二科目选择结果
		combineAlgorithm = new CombineAlgorithmInt(cSize, 2);
		result = combineAlgorithm.getResutl();
		List<NewGkConditionDto> newConditionList2 = newGkChoiceService.findSubRes(courseList, dtoMap, result, 2);
		map.put("newConditionList2", newConditionList2);

		map.put("couserNameMap", couserNameMap);
		map.put("courseNameCountMap", courseNameCountMap);
		int val = 0;
		if (CollectionUtils.isNotEmpty(notvals)) {
			val = notvals.size();
		}
		map.put("chosenStuIdsNum", chosenStuIds.size());
		map.put("noChosenStuIdsNum", studentList.size() - chosenStuIds.size() - val);

		if (Objects.equals("2", statisticType)) {
			// 科目单科及组合总排名
			Map<String, List<String>> rankMap = getRankList(newChoice, new ArrayList<>(chosenStuIds), courseIds, newConditionList3);
			/*Map<String, List<String>> rankMap = RedisUtils.getObject("RANKLIST_" + choiceId + "_" + newChoice.getReferScoreId(), RedisUtils.TIME_TEN_MINUTES, new TR<Map<String, List<String>>>() {
			}, new RedisInterface<Map<String, List<String>>>() {
				@Override
				public Map<String, List<String>> queryData() {
					return getRankList(newChoice, new ArrayList<>(chosenStuIds), courseIds, newConditionList3);
				}
			});*/

			if (StringUtils.isBlank(edge)) {
				edge = "200,200,200";
			}
			Integer firstParam;
			Integer secondParam;
			Integer thirdParam;
			try {
				String[] tmp = edge.split(",");
				firstParam = Integer.valueOf(tmp[0]) < chosenStuIds.size() ? Integer.valueOf(tmp[0]) : chosenStuIds.size();
				secondParam = Integer.valueOf(tmp[1]) < chosenStuIds.size() ? Integer.valueOf(tmp[1]) : chosenStuIds.size();
				thirdParam = Integer.valueOf(tmp[2]) < chosenStuIds.size() ? Integer.valueOf(tmp[2]) : chosenStuIds.size();
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return errorFtl(map, "参数丢失");
			}
			map.put("firstParam", firstParam);
			map.put("secondParam", secondParam);
			map.put("thirdParam", thirdParam);

			Map<String, Integer> topSingleCourseTotalScoreRankMap = new HashMap<>();
			Map<String, Integer> topSingleCourseSingleScoreRankMap = new HashMap<>();
			for (Course course : courseList) {
				topSingleCourseTotalScoreRankMap.put(course.getSubjectName(), CollectionUtils.intersection(courseSelectMap.getOrDefault(course.getId(), new ArrayList<>()), rankMap.get("TOTAL").subList(0, firstParam)).size());
				topSingleCourseSingleScoreRankMap.put(course.getSubjectName(), CollectionUtils.intersection(courseSelectMap.getOrDefault(course.getId(), new ArrayList<>()), rankMap.get(course.getId()).subList(0, secondParam)).size());
			}
			map.put("topSingleCourseTotalScoreRankMap", topSingleCourseTotalScoreRankMap);
			map.put("topSingleCourseSingleScoreRankMap", topSingleCourseSingleScoreRankMap);

			Map<String, Integer> topThreeCourseTotalScoreRankMap = new HashMap<>();
			StringBuilder courseCombineName = new StringBuilder();
			StringBuilder courseCombineCounts = new StringBuilder();
			StringBuilder courseTotalCounts = new StringBuilder();
			StringBuilder coursePartCounts = new StringBuilder();
			List<NewGkConditionDto> courseCombine = new ArrayList<>();
			List<NewGkConditionDto> courseCombineResort = new ArrayList<>();
			int size=0;
			if (CollectionUtils.isNotEmpty(newConditionList3)) {
				for (int i = newConditionList3.size() - 1; i >= 0; i--) {
					NewGkConditionDto newDto = newConditionList3.get(i);
					if (newDto.getSumNum() > 0) {
						topThreeCourseTotalScoreRankMap.put(newDto.getSubjectIdstr(), CollectionUtils.intersection(newDto.getStuIds(), rankMap.get("TOTAL").subList(0, firstParam)).size());
						courseCombineName.append(",\"" + newDto.getSubShortNames() + "\"");
						courseCombineCounts.append("," + newDto.getSumNum());
						courseTotalCounts.append("," + CollectionUtils.intersection(newDto.getStuIds(), rankMap.get("TOTAL").subList(0, firstParam)).size());
						coursePartCounts.append("," + CollectionUtils.intersection(newDto.getStuIds(), rankMap.get(newDto.getSubjectIdstr()).subList(0, thirdParam)).size());
						courseCombine.add(newDto);
						NewGkConditionDto tmp = new NewGkConditionDto();
						tmp.setStuIds(newDto.getStuIds());
						tmp.setSubjectIdstr(newDto.getSubjectIdstr());
						tmp.setSubjectNames(newDto.getSubjectNames());
						courseCombineResort.add(tmp);
						size++;
					}
				}
			}
			map.put("rowNums", size);
			courseCombine.sort((prev, next) -> next.getSumNum().compareTo(prev.getSumNum()));
			map.put("topThreeCourseTotalScoreRankMap", topThreeCourseTotalScoreRankMap);
			map.put("courseCombineNames", courseCombineName.length() > 0 ? courseCombineName.substring(1) : "");
			map.put("courseCombineCounts", courseCombineCounts.length() > 0 ? courseCombineCounts.substring(1) : "");
			map.put("courseTotalCounts", courseTotalCounts.length() > 0 ? courseTotalCounts.substring(1) : "");
			map.put("coursePartCounts", coursePartCounts.length() > 0 ? coursePartCounts.substring(1) : "");
			map.put("courseCombine", courseCombine);

			courseCombineResort.forEach(e -> {
				e.setSumNum(CollectionUtils.intersection(e.getStuIds(), rankMap.get(e.getSubjectIdstr()).subList(0, thirdParam)).size());
			});
			courseCombineResort.sort((prev, next) -> next.getSumNum().compareTo(prev.getSumNum()));
			map.put("courseCombineResort", courseCombineResort);
			return "/newgkelective/result/newResultScoreLevelCount.ftl";
		} else {
			int oneCombine = 0;
			int twoCombine = 0;
			int threeCombine = 0;

			List<String> subjectNames = new ArrayList<>();
			subjectNames.addAll(courseNameCountMap.entrySet().stream().filter(e -> !Integer.valueOf(0).equals(e.getValue())).map(e -> e.getKey()).collect(Collectors.toList()));
			Collections.reverse(subjectNames);
			JSONObject json = new JSONObject();
			json.put("legendData", subjectNames.toArray(new String[0]));
			JSONArray jsonArr = new JSONArray();
			JSONObject json1;
			List<Integer> realDataList = new ArrayList<>();
			for (String name : subjectNames) {
				Integer value = courseNameCountMap.get(name);
				if (Integer.valueOf(0).equals(value)) {
					continue;
				}
				oneCombine++;
				json1 = new JSONObject();
				json1.put("value", value == null ? 0 : value);
				json1.put("name", name);
				json1.put("max", chosenStuIds.size());
				json1.put("subjectId", couserNameMap.get(name));
				jsonArr.add(json1);
				realDataList.add(value == null ? 0 : value);
			}
			json.put("realData", realDataList.toArray(new Integer[0]));
			json.put("loadingData", jsonArr);
			String jsonStringData1 = json.toString();
			map.put("jsonStringData1", jsonStringData1);
			map.put("oneCombineCount", oneCombine);

			json = new JSONObject();
			jsonArr = new JSONArray();
			JSONObject json2;
			subjectNames.clear();
			List<Integer> allSizeList = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(newConditionList2)) {
				for (int i = newConditionList2.size() - 1; i >= 0; i--) {
					NewGkConditionDto newDto = newConditionList2.get(i);
					if (Integer.valueOf(0).equals(newDto.getSumNum())) {
						continue;
					}
					twoCombine++;
					json2 = new JSONObject();
					subjectNames.add(newDto.getSubShortNames());
					json2.put("value", newDto.getSumNum());
					json2.put("name", newDto.getSubShortNames());
					json2.put("subjectId", newDto.getSubjectIdstr());
					jsonArr.add(json2);
					allSizeList.add(chosenStuIds.size());
				}
			}
			json.put("allSizeData", allSizeList.toArray(new Integer[0]));
			json.put("legendData", subjectNames.toArray(new String[0]));
			json.put("loadingData", jsonArr);
			String jsonStringData2 = json.toString();
			map.put("jsonStringData2", jsonStringData2);
			map.put("twoCombineCount", twoCombine);

			json = new JSONObject();
			jsonArr = new JSONArray();
			subjectNames.clear();
			allSizeList.clear();
			if (CollectionUtils.isNotEmpty(newConditionList3)) {
				for (int i = newConditionList3.size() - 1; i >= 0; i--) {
					NewGkConditionDto newDto = newConditionList3.get(i);
					if (Integer.valueOf(0).equals(newDto.getSumNum())) {
						continue;
					}
					threeCombine++;
					json2 = new JSONObject();
					subjectNames.add(newDto.getSubShortNames());
					json2.put("value", newDto.getSumNum());
					json2.put("name", newDto.getSubShortNames());
					json2.put("subjectId", newDto.getSubjectIdstr());
					jsonArr.add(json2);
					allSizeList.add(chosenStuIds.size());
				}
			}
			json.put("allSizeData", allSizeList.toArray(new Integer[0]));
			json.put("legendData", subjectNames.toArray(new String[0]));
			json.put("loadingData", jsonArr);
			String jsonStringData3 = json.toString();
			map.put("jsonStringData3", jsonStringData3);
			map.put("threeCombineCount", threeCombine);

			//return "/newgkelective/result/newResultCount.ftl";
			return "/newgkelective/result/newResultCount1.ftl";
		}
	}

	/**
	 * 科目单科及组合总排名
	 */
	private Map<String, List<String>> getRankList(NewGkChoice choice, List<String> chosenStudentList, List<String> courseList, List<NewGkConditionDto> courseCombine) {
		Map<String, List<String>> returnMap = new HashMap<>();
		// 语数英
		List<String> tmp = EntityUtils.getList(SUtils.dt(courseRemoteService.findByCodesYSY(choice.getUnitId()), Course.class), Course::getId);
		Map<String, Map<String, Float>> referScoreMap = newGkScoreResultService.findMapByReferScoreId(choice.getUnitId(), choice.getReferScoreId());
		Set<String> allCourseIds = new HashSet<>(CollectionUtils.union(courseList, tmp));
		chosenStudentList.sort(new Comparator<String>() {
			@Override
			public int compare(String prevStudent, String nextStudent) {
				if (referScoreMap.get(prevStudent) == null && referScoreMap.get(nextStudent) == null) {
					return 0;
				}
				if (referScoreMap.get(prevStudent) == null) {
					return 1;
				}
				if (referScoreMap.get(nextStudent) == null) {
					return -1;
				}
				float difference = 0.0f;
				for (String courseId : allCourseIds) {
					difference += referScoreMap.get(prevStudent).getOrDefault(courseId, 0.0f) - referScoreMap.get(nextStudent).getOrDefault(courseId, 0.0f);
				}
				if (difference == 0f) {
					return 0;
				} else if (difference < 0f) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		returnMap.put("TOTAL", new ArrayList<>(chosenStudentList));

		/*chosenStudentList.forEach(e -> {
			System.out.println(referScoreMap.get(e) != null ? referScoreMap.get(e).values().stream().reduce(Float::sum) : 11111111);
		});*/

		// 单科排名
		for (String courseId : courseList) {
			chosenStudentList.sort((prevStudent, nextStudent) -> {
				if (referScoreMap.get(prevStudent) == null && referScoreMap.get(nextStudent) == null) {
					return 0;
				}
				if (referScoreMap.get(prevStudent) == null) {
					return 1;
				}
				if (referScoreMap.get(nextStudent) == null) {
					return -1;
				}
				float difference = referScoreMap.get(prevStudent).getOrDefault(courseId, 0.0f) - referScoreMap.get(nextStudent).getOrDefault(courseId, 0.0f);
				if (difference == 0f) {
					return 0;
				} else if (difference < 0f) {
					return 1;
				} else {
					return -1;
				}
			});
			returnMap.put(courseId, new ArrayList<>(chosenStudentList));
		}

		for (NewGkConditionDto one : courseCombine) {
			if (one.getSumNum() != null && one.getSumNum() > 0) {
				chosenStudentList.sort((prevStudent, nextStudent) -> {
					if (referScoreMap.get(prevStudent) == null && referScoreMap.get(nextStudent) == null) {
						return 0;
					}
					if (referScoreMap.get(prevStudent) == null) {
						return 1;
					}
					if (referScoreMap.get(nextStudent) == null) {
						return -1;
					}
					float difference = 0.0f;
					for (String courseId : tmp) {
						difference += referScoreMap.get(prevStudent).getOrDefault(courseId, 0.0f) - referScoreMap.get(nextStudent).getOrDefault(courseId, 0.0f);
					}
					for (String courseId : one.getSubjectIdstr().split(",")) {
						difference += referScoreMap.get(prevStudent).getOrDefault(courseId, 0.0f) - referScoreMap.get(nextStudent).getOrDefault(courseId, 0.0f);
					}
					if (difference == 0f) {
						return 0;
					} else if (difference < 0f) {
						return 1;
					} else {
						return -1;
					}
				});
				returnMap.put(one.getSubjectIdstr(), new ArrayList<>(chosenStudentList));
			}
		}

		return returnMap;
	}
	
	@RequestMapping("/choiceAnalysis/list/page")
	public String resultAnalysis(@PathVariable String choiceId, ModelMap map) {
		NewGkChoice newChoice=newGkChoiceService.findOne(choiceId);//选课
		if(newChoice==null) return errorFtl(map, "找不到选课");
		map.put("choiceName", newChoice.getChoiceName());
		map.put("choiceId", choiceId);
		map.put("gradeId", newChoice.getGradeId());
		
		Map<String, List<NewGkChoRelation>> NewGkChoRelationAllMap= newGkChoRelationService.findByChoiceIdAndObjectTypeIn(newChoice.getUnitId(),choiceId,new String[]{NewGkElectiveConstant.CHOICE_TYPE_01});
		// 获取选考科目
		List<NewGkChoRelation> newGkChoRelationList = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01);
		List<String> courseIds = EntityUtils.getList(newGkChoRelationList, NewGkChoRelation::getObjectValue);
		if(CollectionUtils.isEmpty(courseIds)) {
			return errorFtl(map, "选课没有维护选课科目");
		}
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])), new TR<List<Course>>(){});
		if(CollectionUtils.isEmpty(courseList)) {
			return errorFtl(map, "选课没有维护选课科目");
		}
		
		//获取学生对应的选课ids
		Map<String,NewGkChoResultDto> dtoMap = getStuResultMap(choiceId);
      
        //计算组合
        Integer[] cSize = new Integer[courseList.size()];
        for(int i = 0;i < courseList.size();i++){
        	cSize[i] = i;
        }
        
        //三科目选择结果
        CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize,3);
        Integer[][] result = combineAlgorithm.getResutl();
        List<NewGkConditionDto> newConditionList3=newGkChoiceService.findSubRes(courseList, dtoMap, result, 3);
        map.put("newConditionList3", newConditionList3);
        //二科目选择结果
        combineAlgorithm = new CombineAlgorithmInt(cSize,2);
        result = combineAlgorithm.getResutl();
        List<NewGkConditionDto> newConditionList2=newGkChoiceService.findSubRes(courseList, dtoMap, result, 2);
        map.put("newConditionList2", newConditionList2);
		return "/newgkelective/result/newResultAnalysis.ftl";
	}
	
	private Map<String, NewGkChoResultDto> getStuResultMap(String choiceId){
		//获取所有的选课结果
		NewGkChoice choice = newGkChoiceService.findOne(choiceId);
		List<NewGkChoResult> resultList = getResultList(choice);
		NewGkChoResultDto dto;	
		//获取学生对应的选课ids
		Map<String,NewGkChoResultDto> dtoMap=new HashMap<String,NewGkChoResultDto>();
		if(CollectionUtils.isNotEmpty(resultList)){
			for(NewGkChoResult result:resultList){
				if(!dtoMap.containsKey(result.getStudentId())){
					dto = new NewGkChoResultDto();
	                dto.setChooseSubjectIds(new HashSet<String>());
	                dto.setStudentId(result.getStudentId());
	                dtoMap.put(result.getStudentId(), dto);
	            }
	            dtoMap.get(result.getStudentId()).getChooseSubjectIds().add(result.getSubjectId());
			}
		}
		return dtoMap;
	}
	
	@ResponseBody
	@RequestMapping("/choiceAnalysis/sync")
	@ControllerInfo("根据勾选的科目组合，获得同化数")
	public String syncToOther(@PathVariable String choiceId, int arrayType, String subjectIdstr, ModelMap map) {
		// 勾选的科目组合
		// 勾选的科目id
		Set<String> allSids = new HashSet<String>();
		String[] subjectIdstrAr = StringUtils.split(subjectIdstr, ";");
		List<List<String>> subIdstr = new ArrayList<List<String>>();
		for(String subIds : subjectIdstrAr) {
			String[] sidsArr = subIds.split(",");
			Arrays.sort(sidsArr);
			List<String> sids = new ArrayList<String>();
			for(String sid : sidsArr) {
				if(!allSids.contains(sid)) {
					allSids.add(sid);
				}
				sids.add(sid);
			}
			subIdstr.add(sids);
		}
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(allSids.toArray(new String[0])), new TR<List<Course>>() {});
		//获取学生对应的选课ids
		Map<String,NewGkChoResultDto> dtoMap = getStuResultMap(choiceId);
		
		//计算组合
        Integer[] cSize = new Integer[courseList.size()];
        for(int i = 0;i < courseList.size();i++){
        	cSize[i] = i;
        }
        // 重新获取组合科目及其选课学生
        int num = arrayType;
        CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize, num);
        Integer[][] result = combineAlgorithm.getResutl();
        List<NewGkConditionDto> newConditionList1 = newGkChoiceService.findSubRes(courseList, dtoMap, result, num);
		Set<String> stuIds = new HashSet<String>();
        for(NewGkConditionDto condto : newConditionList1) {
        	Iterator<List<String>> it = subIdstr.iterator();
        	while(it.hasNext()) {
        		List<String> subIds = it.next();
        		if(num == CollectionUtils.intersection(subIds,condto.getSubjectIds()).size()){
        			stuIds.addAll(condto.getStuIds());
        			break;
        		}
        	}
        }
        // 根据选课学生过滤学生结果数据
        Iterator<Entry<String, NewGkChoResultDto>> dtoIt = dtoMap.entrySet().iterator();
        allSids.clear();
        while(dtoIt.hasNext()) {
			Entry<String, NewGkChoResultDto> en = dtoIt.next();
			NewGkChoResultDto sd = en.getValue();
			if(!stuIds.contains(en.getKey())) {
				dtoIt.remove();
			}
			allSids.addAll(sd.getChooseSubjectIds());
		}
        courseList = SUtils.dt(courseRemoteService.findListByIds(allSids.toArray(new String[0])), new TR<List<Course>>() {});
		int toNum = 2;
		if(arrayType == 2) {
			toNum = 3;
		}
		cSize = new Integer[courseList.size()];
        for(int i = 0;i < courseList.size();i++){
        	cSize[i] = i;
        }
		combineAlgorithm = new CombineAlgorithmInt(cSize, toNum);
        result = combineAlgorithm.getResutl();
        newConditionList1 = newGkChoiceService.findSubRes(courseList, dtoMap, result, toNum);
		JSONArray ars = new JSONArray();
		for(NewGkConditionDto condto : newConditionList1) {
			if(condto.getSumNum() == 0) {
				continue;
			}
			JSONObject ob = new JSONObject();
			ob.put("subIds", condto.getSubjectIdstr());
			ob.put("stuNum", condto.getSumNum());
			ars.add(ob);
		}
		return ars.toJSONString();
	}
	
	@RequestMapping("/choiceAnalysis/sync/list/page")
	@ControllerInfo("根据勾选的科目组合，获得对应同化数的学生列表")
	public String syncList(@PathVariable String choiceId, String subjectIdstr, int arrayType, String subArrayIds, ModelMap map) {
		// 先获取组合的学生列表，再根据勾选原学科组合进行过滤
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdstr.split(",")), new TR<List<Course>>() {});
		//获取学生对应的选课ids
		Map<String, NewGkChoResultDto> dtoMap = getStuResultMap(choiceId);
		// 原科目组合
		String[] subArr = subArrayIds.split(";");
		List<List<String>> subIdstrList = new ArrayList<List<String>>();
		for(String subIds : subArr) {
			String[] sidsArr = subIds.split(",");
			Arrays.sort(sidsArr);
			List<String> sids = new ArrayList<String>();
			for(String sid : sidsArr) {
				sids.add(sid);
			}
			subIdstrList.add(sids);
		}
		int sourceNum = 2;
		if(arrayType == 2) {
			sourceNum = 3;
		}
		// 过滤学生数据
		Iterator<Entry<String, NewGkChoResultDto>> dtoIt = dtoMap.entrySet().iterator();
		while(dtoIt.hasNext()) {
			Entry<String, NewGkChoResultDto> en = dtoIt.next();
			Set<String> sids = en.getValue().getChooseSubjectIds();
			boolean hasSub = false;
			for(int i=0;i<subIdstrList.size();i++) {
				List<String> sarr = subIdstrList.get(i);
				if(sourceNum == CollectionUtils.intersection(sids, sarr).size()){
					hasSub = true;
					break;
	    		}
			}
			if(!hasSub) {
				dtoIt.remove();
				continue;
			}
		}
		//计算组合
        Integer[] cSize = new Integer[courseList.size()];
        for(int i = 0;i < courseList.size();i++){
        	cSize[i] = i;
        }
        CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize, arrayType);
        Integer[][] result = combineAlgorithm.getResutl();
        List<NewGkConditionDto> newConditionList1 = newGkChoiceService.findSubRes(courseList, dtoMap, result, arrayType);
        Set<String> stuIds = new HashSet<String>();
        for(NewGkConditionDto condto : newConditionList1) {
			if(condto.getSumNum() == 0) {
				continue;
			}
			stuIds.addAll(condto.getStuIds());
		}
        List<Student> stus = getStuInfo(stuIds.toArray(new String[0]));
        map.put("stus", stus);
        map.put("choiceId", choiceId);
		return "/newgkelective/result/newResultSyncList.ftl";
	}
	
	/**
	 * 获取学生数据
	 * @param sids
	 * @return
	 */
	private List<Student> getStuInfo(String[] sids) {
		if(ArrayUtils.isEmpty(sids)) {
			return new ArrayList<Student>();
		}
		List<Student> stus = Student.dt(studentRemoteService.findListByIds(sids));
    	Set<String> clsIds = EntityUtils.getSet(stus, Student::getClassId);
    	Map<String, String> clsMap = EntityUtils.getMap(Clazz.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0]))),
    									Clazz::getId, Clazz::getClassNameDynamic);
		for(Student stu : stus) {
			stu.setClassName(clsMap.get(stu.getClassId()));
		}
		Collections.sort(stus, new Comparator<Student>() {

			public int compare(Student o1, Student o2) {
				if(StringUtils.trimToEmpty(o1.getClassName()).equals(StringUtils.trimToEmpty(o2.getClassName()))) {
					return StringUtils.trimToEmpty(o1.getStudentCode()).compareTo(StringUtils.trimToEmpty(o2.getStudentCode()));
				}
				return StringUtils.trimToEmpty(o1.getClassName()).compareTo(StringUtils.trimToEmpty(o2.getClassName()));
			}
			
		});
		return stus;
	}
	
	@RequestMapping("/choiceResult/export")
	public void resultExport(@PathVariable String choiceId, int arrayType, int arraySize, 
			HttpServletResponse response) {
		// 人数统计取本次全部的选课数据来统计
		// 名单，取后面组的学生名册
		// 3科》2+x   newConditionList3 
		// 2科》3 newConditionList2
		// 
		Map<String, List<NewGkChoRelation>> NewGkChoRelationAllMap= newGkChoRelationService.findByChoiceIdAndObjectTypeIn(getLoginInfo().getUnitId(),choiceId,new String[]{NewGkElectiveConstant.CHOICE_TYPE_01});
		// 获取选考科目
		List<NewGkChoRelation> newGkChoRelationList = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01);
		List<String> courseIds = EntityUtils.getList(newGkChoRelationList, NewGkChoRelation::getObjectValue);
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])), new TR<List<Course>>(){});
		
		//获取学生对应的选课ids
		Map<String,NewGkChoResultDto> dtoMap = getStuResultMap(choiceId);
		//计算组合
        Integer[] cSize = new Integer[courseList.size()];
        for(int i = 0;i < courseList.size();i++){
        	cSize[i] = i;
        }
		// 先获取前size组的数据及科目
		CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize, arrayType);
        Integer[][] result = combineAlgorithm.getResutl();
        List<NewGkConditionDto> newConditionList = newGkChoiceService.findSubRes(courseList, dtoMap, result, arrayType);
		List<NewGkConditionDto> lefts;// 剩余组
        if(newConditionList.size() > arraySize) {
        	lefts = newConditionList.subList(arraySize, newConditionList.size());
        	newConditionList = newConditionList.subList(0, arraySize);
		} else {
			lefts = new ArrayList<NewGkConditionDto>();
		}
        // 剩余组学生id
        Set<String> sids = new HashSet<String>();
        if(lefts.size() > 0) {
        	for(NewGkConditionDto dto : lefts) {
        		sids.addAll(dto.getStuIds());
        	}
        }
		Set<String> cids = new HashSet<String>();
		for(NewGkConditionDto dto : newConditionList) {
			cids.addAll(dto.getSubjectIds());
		}
		if(cids.size() < courseList.size()) {
			Iterator<Course> cit = courseList.iterator();
			while(cit.hasNext()) {
				Course cr = cit.next();
				if(!cids.contains(cr.getId())) {
					cit.remove();
				}
			}
		}
		// 根据过滤之后的科目获取同化后的组合统计结果
		int num = 2;
		if(arrayType == 2) {
			num = 3;
		}
		cSize = new Integer[courseList.size()];
        for(int i = 0;i < courseList.size();i++){
        	cSize[i] = i;
        }
        combineAlgorithm = new CombineAlgorithmInt(cSize, num);
        result = combineAlgorithm.getResutl();
        newConditionList = newGkChoiceService.findSubRes(courseList, dtoMap, result, num);
		
        HSSFWorkbook workbook = new HSSFWorkbook();
	    HSSFCellStyle style = workbook.createCellStyle();
	    HSSFFont headfont = workbook.createFont();
        headfont.setFontHeightInPoints((short) 10);// 字体大小
        headfont.setBold(true);// 加粗
        style.setFont(headfont);
		HSSFSheet sheet = workbook.createSheet("同化导出统计");
		sheet.setColumnWidth(1, 30 * 256);// 内容列宽度
		//标题行
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints((1.5f)*sheet.getDefaultRowHeightInPoints());
        String[] titleNum = {"序号", "学科", "选课人数"};
        for(int j=0;j<titleNum.length;j++) {
        	HSSFCell cell = titleRow.createCell(j);
        	cell.setCellValue(new HSSFRichTextString(titleNum[j]));
        	cell.setCellStyle(style);
        }
        // 内容行
        int rowInt = 1;
        for(NewGkConditionDto dto : newConditionList) {
        	HSSFRow conRow = sheet.createRow(rowInt);
        	HSSFCell cell = conRow.createCell(0);
        	cell.setCellValue(new HSSFRichTextString(rowInt+""));
        	HSSFCell cell1 = conRow.createCell(1);
			cell1.setCellValue(new HSSFRichTextString(StringUtils.trimToEmpty(dto.getSubNames()[0]) 
					+ "、" + StringUtils.trimToEmpty(dto.getSubNames()[1])
					+ (num==3?"、"+StringUtils.trimToEmpty(dto.getSubNames()[2]):"")));
			HSSFCell cell2 = conRow.createCell(2);
        	cell2.setCellValue(new HSSFRichTextString(dto.getSumNum().toString()));
        	rowInt++;
        }
		
        // 表格2
		String[] titleList = {"序号", "班级", "学生"};
		HSSFSheet sheet1 = workbook.createSheet("统计中未被选取组合的学生名单");
		sheet1.setColumnWidth(1, 15 * 256);// 内容列宽度
		sheet1.setColumnWidth(2, 15 * 256);// 内容列宽度
		//标题行
        HSSFRow titleRow1 = sheet1.createRow(0);
        titleRow.setHeightInPoints((1.5f)*sheet1.getDefaultRowHeightInPoints());
        for(int j=0;j<titleList.length;j++) {
        	HSSFCell cell = titleRow1.createCell(j);
        	cell.setCellValue(new HSSFRichTextString(titleNum[j]));
        	cell.setCellStyle(style);
        }
        if (sids.size() > 0) {//TODO
        	List<Student> stus = getStuInfo(sids.toArray(new String[0]));
        	// 内容行
			rowInt = 1;
			for (Student stu : stus) {
				HSSFRow conRow = sheet1.createRow(rowInt);
				HSSFCell cell = conRow.createCell(0);
				cell.setCellValue(new HSSFRichTextString(rowInt + ""));
				HSSFCell cell1 = conRow.createCell(1);
				cell1.setCellValue(new HSSFRichTextString(stu.getClassName()));
				HSSFCell cell2 = conRow.createCell(2);
				cell2.setCellValue(new HSSFRichTextString(stu.getStudentName()));
				rowInt++;
			} 
		}
        
        String fileName = (arrayType==2?"两":"三")+"科同化导出数据";
		ExportUtils.outputData(workbook, fileName, response);
	}
	
	@RequestMapping("/selected/export")
	@ResponseBody
	@ControllerInfo("已选课详情导出Excel")
	private String exportSelectedReport(@PathVariable String choiceId, String filterType, HttpServletResponse resp){
		//判断选课
		NewGkChoice choice = newGkChoiceService.findOne(choiceId);
		List<NewGkChoResult> resultList = getResultList(choice);
		List<Course> course73List = SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()),new TR<List<Course>>() {});
		Map<String, String> courseNameMap = EntityUtils.getMap(course73List, Course::getId, Course::getSubjectName);
		// List<String> courseChoList = newGkChoRelationService.findByChoiceIdAndObjectType(choiceId,NewGkElectiveConstant.CHOICE_TYPE_01);

		List<List<String>> datas = new ArrayList<List<String>>();
		//判断是否有选课结果
		if(CollectionUtils.isNotEmpty(resultList)){
			
			//基本信息数据
			Set<String> studentIds = EntityUtils.getSet(resultList, NewGkChoResult::getStudentId);
			List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])), new TR<List<Student>>() {});

			List<NewGkChoResult> wantResult = newGkChoResultService.findByKindTypeAndChoiceIdAndStudentIds(choice.getUnitId(),NewGkElectiveConstant.KIND_TYPE_02, choice.getId(), studentIds.toArray(new String[0]));
			List<NewGkChoResult> noWantResult = newGkChoResultService.findByKindTypeAndChoiceIdAndStudentIds(choice.getUnitId(),NewGkElectiveConstant.KIND_TYPE_03, choice.getId(), studentIds.toArray(new String[0]));
			Map<String, StringBuilder> wantResultMap = new HashMap<>();
			Map<String, StringBuilder> noWantResultMap = new HashMap<>();
			for (NewGkChoResult one : wantResult) {
				if (wantResultMap.get(one.getStudentId()) == null) {
					wantResultMap.put(one.getStudentId(), new StringBuilder());
				}
				wantResultMap.get(one.getStudentId()).append("," + courseNameMap.get(one.getSubjectId()));
			}
			for (NewGkChoResult one : noWantResult) {
				if (noWantResultMap.get(one.getStudentId()) == null) {
					noWantResultMap.put(one.getStudentId(), new StringBuilder());
				}
				noWantResultMap.get(one.getStudentId()).append("," + courseNameMap.get(one.getSubjectId()));
			}

			Set<String> classIds = EntityUtils.getSet(studentList, Student::getClassId);
			List<Clazz> classList = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[0])), new TR<List<Clazz>>() {});
			Map<String, String> classNameMap = EntityUtils.getMap(classList, Clazz::getId, Clazz::getClassNameDynamic);
			
			Map<String, String> sexNameMap = getSexNameMap();
			
			
			//按学生封装结果集,key-studentId,value-subjectIds
			Map<String,List<String>> resultMap = new HashMap<String,List<String>>(resultList.size());
			for (NewGkChoResult result : resultList) {
				List<String> inList = resultMap.get(result.getStudentId());
				if(CollectionUtils.isEmpty(inList)){
					inList = new ArrayList<String>();
					inList.add("");
					resultMap.put(result.getStudentId(), inList);
				}
				if (StringUtils.isBlank(result.getSubjectType())) {
					result.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_1);
				}
				if (NewGkElectiveConstant.SUBJTCT_TYPE_1.equals(result.getSubjectType())) {
					inList.add(result.getSubjectId());
				} else if (NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(result.getSubjectType())) {
					inList.add(result.getSubjectId() + "_可调剂");
				} else {
					inList.add(result.getSubjectId() + "_优先调剂");
				}
				// 可调剂、优先调剂筛选
				if (!inList.get(0).contains(result.getSubjectType())) {
					inList.set(0, inList.get(0) + result.getSubjectType());
				}
			}
			
			//组装数据
			for (Student student : studentList) {
				List<String> inList = new ArrayList<String>();
				inList.add(classNameMap.get(student.getClassId()));
				inList.add(student.getStudentName());
				inList.add(student.getStudentCode());
				inList.add(sexNameMap.get(student.getSex()+""));
				List<String> subjectIds = resultMap.get(student.getId());
				if (StringUtils.isNotBlank(filterType)) {
					if (!subjectIds.get(0).contains(filterType)) {
						continue;
					}
				}
				for (Course one : course73List) {
			        /*if (subjectIds.contains(one.getId())) {
			        	inList.add(one.getSubjectName());
                    }else{
                    	inList.add("");
                    }*/
			        boolean flag = true;
			        boolean isFirst = true;
			        for (String subjectId : subjectIds) {
			        	if (isFirst) {
			        		isFirst = false;
			        		continue;
						}
			        	String[] tmp = subjectId.split("_");
			        	if (one.getId().equals(tmp[0])) {
							if (tmp.length > 1) {
								inList.add(one.getSubjectName() + "(" + tmp[1] + ")");
							} else {
								inList.add(one.getSubjectName());
							}
							flag = false;
						}
					}
			        if (flag) {
			        	inList.add("");
					}
                }
				if (wantResultMap.get(student.getId()) == null) {
					inList.add("");
				} else {
					inList.add(wantResultMap.get(student.getId()).substring(1));
				}
				if (noWantResultMap.get(student.getId()) == null) {
					inList.add("");
				} else {
					inList.add(noWantResultMap.get(student.getId()).substring(1));
				}
				datas.add(inList);
			}
			//结果按学生班级和学号排序
			sortByClassNameAndStudentCode(datas);
			
		}
		
        //表头
        List<String> titleList = new ArrayList<String>();
        titleList.add("班级");
        titleList.add("姓名");
        titleList.add("学号");
        titleList.add("性别");
        titleList.addAll(EntityUtils.getList(course73List, Course::getSubjectName));
		titleList.add("优先调剂");
		titleList.add("明确不选");

    	//导出
		String fileName =choice.getChoiceName();
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("学生已选结果");
		CellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
		titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		titleStyle.setAlignment(HorizontalAlignment.CENTER);//水平
		titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
		titleStyle.setBorderBottom(BorderStyle.THIN);
		titleStyle.setBorderLeft(BorderStyle.THIN);
		titleStyle.setBorderRight(BorderStyle.THIN);
		titleStyle.setBorderTop(BorderStyle.THIN);
		HSSFCellStyle centerStyle=workbook.createCellStyle();
		centerStyle.setAlignment(HorizontalAlignment.CENTER);//水平
		centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
		centerStyle.setBorderBottom(BorderStyle.THIN);
		centerStyle.setBorderLeft(BorderStyle.THIN);
		centerStyle.setBorderRight(BorderStyle.THIN);
		centerStyle.setBorderTop(BorderStyle.THIN);
		//标题行固定
		sheet.createFreezePane(0, 1);
		int rownum=0;
		HSSFRow rowTitle = sheet.createRow(rownum++);
		rowTitle.setHeightInPoints((2f) * sheet.getDefaultRowHeightInPoints());

		for(int i=0;i<titleList.size();i++){
			sheet.setColumnWidth(i, 10 * 256);//列宽
			HSSFCell cell = rowTitle.createCell(i);
			cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
			cell.setCellStyle(titleStyle);
		}
		if(CollectionUtils.isNotEmpty(datas)){
			for (List<String> data : datas) {
				HSSFRow inRow = sheet.createRow(rownum++);
				inRow.setHeightInPoints((1.5f) * sheet.getDefaultRowHeightInPoints());
				for(int i=0;i<titleList.size();i++){
					HSSFCell inCell = inRow.createCell(i);
					inCell.setCellValue(new HSSFRichTextString(data.get(i)));
					inCell.setCellStyle(centerStyle);
				}
			}
		}
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		ExportUtils.outputData(workbook, fileName, resp);
		return returnSuccess();
	}

	@RequestMapping("/unselect/export")
	@ResponseBody
	@ControllerInfo("未选课详情导出Excel")
	private String exportUnselectReport(@PathVariable String choiceId,HttpServletResponse resp){
		NewGkChoice choice = newGkChoiceService.findOne(choiceId);
		List<Student> studentList = new ArrayList<Student>();
		LoginInfo loginInfo = getLoginInfo();
		Map<String, List<String>> roleMap = findRoleByUserId(loginInfo.getUnitId(), loginInfo.getUserId());
		if(isClassRole(roleMap, choice.getGradeId())){
			List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(choice.getUnitId(),choice.getGradeId()), new TR<List<Clazz>>(){});
			List<String> classIdList = roleMap.get(ROLE_CLASS);
			clazzList = clazzList.stream().filter(e->classIdList.contains(e.getId())).collect(Collectors.toList());
			studentList = SUtils.dt(studentRemoteService.findByClassIds(EntityUtils.getList(clazzList, Clazz::getId).toArray(new String[0])),Student.class);
		}else{
			List<Clazz> clazzs = Clazz.dt(classRemoteService.findListBy(new String[] {"gradeId", "isDeleted"}, new Object[] {choice.getGradeId(), 0}));
			if(CollectionUtils.isNotEmpty(clazzs)){
				Set<String> claids=EntityUtils.getSet(clazzs, Clazz::getId);
				studentList=SUtils.dt(studentRemoteService.findByClassIds(claids.toArray(new String[]{})),new TR<List<Student>>(){});
			}
		}
		List<NewGkChoResult>  resultList=newGkChoResultService.findByKindTypeAndChoiceIdAndStudentIds(choice.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,choice.getId(), EntityUtils.getList(studentList, Student::getId).toArray(new String[0]));
		
		Set<String> studentIds = EntityUtils.getSet(resultList, NewGkChoResult::getStudentId);
		Set<String> studentAllIds = EntityUtils.getSet(studentList, Student::getId);
		
		List<List<String>> datas = new ArrayList<List<String>>();
		//判断是否全部未选课
		if(studentIds.size()!=studentAllIds.size()){
			studentAllIds.removeAll(studentIds);
			studentList = studentList.stream().filter(e->studentAllIds.contains(e.getId())).collect(Collectors.toList());
			
			//基本信息数据
			Set<String> classIds = EntityUtils.getSet(studentList, "classId");
			List<Clazz> classList = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[0])), new TR<List<Clazz>>() {});
			Map<String, String> classNameMap = EntityUtils.getMap(classList, "id", "classNameDynamic");
			
			Map<String, String> sexNameMap = getSexNameMap();
			
			//组装数据
			for (Student student : studentList) {
				List<String> inList = new ArrayList<String>() ;
				inList.add(classNameMap.get(student.getClassId()));
				inList.add(student.getStudentName());
				inList.add(student.getStudentCode());
				inList.add(sexNameMap.get(student.getSex()+""));
				datas.add(inList);
			}
			//结果按学生班级和学号排序
			sortByClassNameAndStudentCode(datas);
			
		}
        
        //表头
        List<String> titleList = new ArrayList<String>();
        titleList.add("班级");
        titleList.add("姓名");
        titleList.add("学号");
        titleList.add("性别");

    	//导出
		String fileName =choice.getChoiceName();
		HSSFWorkbook workbook = new HSSFWorkbook();
		CellStyle splitStyle = workbook.createCellStyle();
		splitStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
		splitStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		splitStyle.setAlignment(HorizontalAlignment.CENTER);//水平
		splitStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
		splitStyle.setBorderBottom(BorderStyle.THIN);
		splitStyle.setBorderLeft(BorderStyle.THIN);
		splitStyle.setBorderRight(BorderStyle.THIN);
		splitStyle.setBorderTop(BorderStyle.THIN);
		HSSFCellStyle centerStyle=workbook.createCellStyle();
		centerStyle.setAlignment(HorizontalAlignment.CENTER);//水平
		centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
		centerStyle.setBorderBottom(BorderStyle.THIN);
		centerStyle.setBorderLeft(BorderStyle.THIN);
		centerStyle.setBorderRight(BorderStyle.THIN);
		centerStyle.setBorderTop(BorderStyle.THIN);
		HSSFSheet sheet = workbook.createSheet("未完成选课学生");
		//标题行固定
		sheet.createFreezePane(0, 1);
		int rownum=0;
		HSSFRow rowTitle = sheet.createRow(rownum++);
		rowTitle.setHeightInPoints((2f) * sheet.getDefaultRowHeightInPoints());
		for(int i=0;i<titleList.size();i++){
			sheet.setColumnWidth(i, 10 * 256);//列宽
			HSSFCell cell = rowTitle.createCell(i);
			cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
			cell.setCellStyle(splitStyle);
		}
		if(CollectionUtils.isNotEmpty(datas)){
			for (List<String> data : datas) {
				HSSFRow inRow = sheet.createRow(rownum++);
				inRow.setHeightInPoints((1.5f) * sheet.getDefaultRowHeightInPoints());
				for(int i=0;i<titleList.size();i++){
					HSSFCell inCell = inRow.createCell(i);
					inCell.setCellValue(new HSSFRichTextString(data.get(i)));
					inCell.setCellStyle(centerStyle);
				}
			}
		}
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		ExportUtils.outputData(workbook, fileName, resp);
		return returnSuccess();
	}


	private Map<String, String> getSexNameMap() {
		List<McodeDetail> mcodeList = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-XB"),new TR<List<McodeDetail>>() {});
		Map<String,String> sexNameMap = EntityUtils.getMap(mcodeList,"thisId","mcodeContent");
		return sexNameMap;
	}

	private void sortByClassNameAndStudentCode(List<List<String>> datas) {
		Collections.sort(datas,new Comparator<List<String>>(){
			@Override
			public int compare(List<String> o1, List<String> o2) {
				
				if(o1.get(0)==null || o2.get(0)==null){
					return 0;
				}
				if(o1.get(0).equals(o2.get(0))){
					return o1.get(2).compareTo(o2.get(2));
				}
				return o1.get(0).compareTo(o2.get(0));
		     }
        });
	}
	
	 public static Map<String, Integer> sortByValue(Map<String, Integer> map) {
	        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
	        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
	            @Override
	            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
	                return o2.getValue() - o1.getValue();
	            }
	        });
	        Map<String, Integer> result = new LinkedHashMap<String, Integer>();
	        for (Map.Entry<String, Integer> entry : list) {
	            result.put(entry.getKey(), entry.getValue());
	        }
	        return result;
	    }
	 
	@InitBinder
	public void initBinder(WebDataBinder wb) throws Exception {
		wb.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
	}
	
	@RequestMapping("/choiceCountDetail/list/page")
	@ControllerInfo("查看统计明细")
	public String showChoiceCountDetail(@PathVariable String choiceId, ModelMap map) {
		map.put("choiceId", choiceId);
		NewGkChoice newChoice=newGkChoiceService.findOne(choiceId);
		List<NewGkChoResult> resultList = getResultList(newChoice);
		//获取选考科目
		Map<String, List<NewGkChoRelation>> NewGkChoRelationAllMap= newGkChoRelationService.findByChoiceIdAndObjectTypeIn(newChoice.getUnitId(),choiceId,new String[]{NewGkElectiveConstant.CHOICE_TYPE_01
				,NewGkElectiveConstant.CHOICE_TYPE_04,NewGkElectiveConstant.CHOICE_TYPE_02});
		
		List<NewGkChoRelation> newGkChoRelationList = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01);
		List<String> courseIds = EntityUtils.getList(newGkChoRelationList, "objectValue");
		if(CollectionUtils.isEmpty(courseIds)) {
			return errorFtl(map, "选课没有维护选课科目");
		}
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])), new TR<List<Course>>(){});
		List<String> courseNameList=EntityUtils.getList(courseList, "subjectName");
		map.put("courseNameList", courseNameList);
		//获取禁选组合
		List<NewGkChoRelation> notvals2= NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_02);
		List<String> objectValues= EntityUtils.getList(notvals2, "objectValue");
		//已选学生ids
		Set<String> chosenStuIds=new HashSet<String>();
		NewGkChoResultDto dto=new NewGkChoResultDto();	
		//获取学生对应的选课ids
		Map<String,NewGkChoResultDto> dtoMap=new HashMap<String,NewGkChoResultDto>();
		Map<String, Integer> courseIdCountMap= new HashMap<String, Integer>();
		if(CollectionUtils.isNotEmpty(resultList)){
			for(NewGkChoResult result:resultList){
				chosenStuIds.add(result.getStudentId());
				if(!dtoMap.containsKey(result.getStudentId())){
					dto = new NewGkChoResultDto();
	                dto.setChooseSubjectIds(new HashSet<String>());
	                dto.setStudentId(result.getStudentId());
	                dtoMap.put(result.getStudentId(), dto);
	            }
	            dtoMap.get(result.getStudentId()).getChooseSubjectIds().add(result.getSubjectId());
	            
	            Integer integer = courseIdCountMap.get(result.getSubjectId());
		    	if(integer == null){
		    		integer = 0;
		    	}
		    	courseIdCountMap.put(result.getSubjectId(), ++integer);
			}
		}
		map.put("sumNum",chosenStuIds.size());
		Map<String, Integer> courseNameCountMap = new HashMap<String, Integer>();
		for(Course course:courseList){
			Integer integer = courseIdCountMap.get(course.getId());
			if(integer==null) integer=0;
			courseNameCountMap.put(course.getSubjectName(), integer);
		}
      
        //各科目选择结果
        courseNameCountMap = sortByValue(courseNameCountMap);
        //计算组合
        Integer[] cSize = new Integer[courseList.size()];
        for(int i = 0;i < courseList.size();i++){
        	cSize[i] = i;
        }
        
        //三科目选择结果
        CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize,3);
        Integer[][] result = combineAlgorithm.getResutl();
        List<NewGkConditionDto> newConditionList3=newGkChoiceService.findSubRes(courseList, dtoMap, result, 3);
		//新增过滤筛选组合不推荐课程
		if(CollectionUtils.isNotEmpty(objectValues)){
			for(String subjectIdstr:objectValues){
				String[] subjectIds = subjectIdstr.split(",");
				if (subjectIds.length==3) {
					for(NewGkConditionDto newCondition: newConditionList3){
						Set<String> subs =newCondition.getSubjectIds();
						if(subs.contains(subjectIds[0])&& subs.contains(subjectIds[1]) &&subs.contains(subjectIds[2])){
							newCondition.setLimitSubject(true);
						}
					}
				}
			}
		}
		Collections.sort(newConditionList3, new Comparator<NewGkConditionDto>() {
	          @Override
	          public int compare(NewGkConditionDto o1, NewGkConditionDto o2) {
	        	  if(o1.isLimitSubject()!=o2.isLimitSubject()){
	        		  return String.valueOf(o1.isLimitSubject()).compareTo(String.valueOf(o2.isLimitSubject()));
	        	  }
	        	  return o2.getSumNum().compareTo(o1.getSumNum());
	          }
	        }); 
        map.put("newConditionList3", newConditionList3);
        map.put("courseNameCountMap", courseNameCountMap);
        
		return "newgkelective/result/newResultCountDetail.ftl";
	}
	
	/**
	 * 判断是否为班主任权限
	 * @param roleMap
	 * @param gradeId
	 * @return
	 */
	private boolean isClassRole(Map<String, List<String>> roleMap, String gradeId){
		if(MapUtils.isNotEmpty(roleMap) 
				&& !( roleMap.containsKey(ROLE_GRADE) && roleMap.get(ROLE_GRADE).contains(gradeId))  
				&& roleMap.containsKey(ROLE_CLASS)){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据权限获取所有的选课结果
	 * @param newGkChoice
	 * @return
	 */
	private List<NewGkChoResult> getResultList(NewGkChoice newGkChoice) {
		List<Student> studentList = new ArrayList<Student>();
		LoginInfo loginInfo = getLoginInfo();
		Map<String, List<String>> roleMap = findRoleByUserId(loginInfo.getUnitId(), loginInfo.getUserId());
		if(isClassRole(roleMap, newGkChoice.getGradeId())){
			List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(newGkChoice.getUnitId(),newGkChoice.getGradeId()), new TR<List<Clazz>>(){});
			List<String> classIdList = roleMap.get(ROLE_CLASS);
			clazzList = clazzList.stream().filter(e->classIdList.contains(e.getId())).collect(Collectors.toList());
			studentList = SUtils.dt(studentRemoteService.findByClassIds(EntityUtils.getList(clazzList, Clazz::getId).toArray(new String[0])),Student.class);
		}else{
			List<Clazz> clazzs = Clazz.dt(classRemoteService.findListBy(new String[] {"gradeId", "isDeleted"}, new Object[] {newGkChoice.getGradeId(), 0}));
			if(CollectionUtils.isNotEmpty(clazzs)){
				Set<String> claids=EntityUtils.getSet(clazzs, Clazz::getId);
				studentList=SUtils.dt(studentRemoteService.findByClassIds(claids.toArray(new String[]{})),new TR<List<Student>>(){});
			}
		}
		List<NewGkChoResult>  resultList=newGkChoResultService.findByKindTypeAndChoiceIdAndStudentIds(newGkChoice.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,newGkChoice.getId(), EntityUtils.getList(studentList, Student::getId).toArray(new String[0]));
		return resultList;
	}
}
    
