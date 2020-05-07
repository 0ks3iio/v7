package net.zdsoft.newgkelective.data.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.CourseCategoryDto;
import net.zdsoft.newgkelective.data.dto.NewGkChoiceDto;
import net.zdsoft.newgkelective.data.dto.NewGkScoreDto;
import net.zdsoft.newgkelective.data.entity.NewGkChoCategory;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;
import net.zdsoft.newgkelective.data.entity.NewGkReferScore;
import net.zdsoft.newgkelective.data.entity.NewGkScoreResult;
import net.zdsoft.newgkelective.data.service.NewGkChoCategoryService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkChoiceService;
import net.zdsoft.newgkelective.data.service.NewGkReferScoreService;
import net.zdsoft.newgkelective.data.service.NewGkScoreResultService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import avro.shaded.com.google.common.base.Objects;

import com.alibaba.fastjson.TypeReference;

@Controller
@RequestMapping("/newgkelective/stuChooseSubject")
public class NewGkElectiveStudentChoiceAction extends BaseAction {
	
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkChoiceService newGkChoiceService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private NewGkChoResultService newGkChoResultService;
	@Autowired
	private NewGkChoCategoryService newGkChoCategoryService;
	@Autowired
	private NewGkReferScoreService newGkReferScoreService;
	@Autowired
	private NewGkScoreResultService newGkScoreResultService;
	@Autowired
	private StudentRemoteService studentRemoteService;

	@RequestMapping("/index/page")
	@ControllerInfo(value = "学生页面选课主页")
	public String showIndex(ModelMap map) {
		return "/newgkelective/choice/studentChoice.ftl";		
	}
	
	
	@RequestMapping("/head/page")
	@ControllerInfo(value = "学生页面选课主页")
	public String showHead(ModelMap map){
		LoginInfo login= getLoginInfo();
		//Student student = SUtils.dt(studentRemoteService.findOneById(studentId),new TypeReference<Student>() {});
		Clazz clazz = SUtils.dt(classRemoteService.findOneById(login.getClassId()),new TypeReference<Clazz>() {});
		List<NewGkChoice> chs =newGkChoiceService.findListByGradeId(clazz.getGradeId());		
		if(CollectionUtils.isEmpty(chs)) {
			return "/newgkelective/choice/studentChoiceHead.ftl";	
		}
		
		List<String> cids = EntityUtils.getList(chs, NewGkChoice::getId);
		List<NewGkChoiceDto> chDtos = new ArrayList<NewGkChoiceDto>();
		Map<String, String> limitMap = newGkChoRelationService.findByChoiceIdsAndObjectTypeAndObjVal(login.getUnitId(), 
				cids.toArray(new String[0]), NewGkElectiveConstant.CHOICE_TYPE_04, login.getOwnerId());
		boolean hasLimit = MapUtils.isNotEmpty(limitMap);
		
		Map<String, Map<String, List<NewGkChoResult>>> resultMap = newGkChoResultService.findByStudentIdAndChoiceIds(login.getUnitId(),login.getOwnerId(),cids.toArray(new String[0]));
		
		NewGkChoiceDto dto = null;
		Date now = new Date();
		for(NewGkChoice ch : chs) {
			dto = new NewGkChoiceDto();
			dto.setChoiceName(ch.getChoiceName());
			dto.setChoiceId(ch.getId());
			dto.setStartTime(ch.getStartTime());
			dto.setEndTime(ch.getEndTime());
			int sn = DateUtils.compareIgnoreSecond(ch.getStartTime(), now); 
			if(sn > 0
					|| DateUtils.compareIgnoreSecond(ch.getEndTime(), now)<0) {
				if(sn>0) {
					dto.setTimeState(0);// 未开始
				} else {
					dto.setTimeState(2);// 已过期
				}
			} else {
				dto.setTimeState(1);// 进行中
			}
			dto.setWarning(false);
			if(hasLimit && limitMap.containsKey(ch.getId())) {
				dto.setWarning(true);
			}
			dto.setDefault(false);
			if(Constant.IS_TRUE==ch.getIsDefault()){
				dto.setDefault(true);
			}
			
			if(resultMap.containsKey(ch.getId())){
				Map<String, List<NewGkChoResult>> inMap = resultMap.get(ch.getId());
				if(inMap.containsKey(NewGkElectiveConstant.KIND_TYPE_01)){
					dto.setResultList(inMap.get(NewGkElectiveConstant.KIND_TYPE_01));
				}
				if(inMap.containsKey(NewGkElectiveConstant.KIND_TYPE_02)){
					List<NewGkChoResult> inList = inMap.get(NewGkElectiveConstant.KIND_TYPE_02);
					dto.setWantToSubjectList(EntityUtils.getList(inList, NewGkChoResult::getSubjectId));
				}
				if(inMap.containsKey(NewGkElectiveConstant.KIND_TYPE_03)){
					List<NewGkChoResult> inList = inMap.get(NewGkElectiveConstant.KIND_TYPE_03);
					dto.setNoWantToSubjectList(EntityUtils.getList(inList, NewGkChoResult::getSubjectId));
				}
			}
			chDtos.add(dto);
		}
		Collections.sort(chDtos, new Comparator<NewGkChoiceDto>() {

			@Override
			public int compare(NewGkChoiceDto o1, NewGkChoiceDto o2) {
				if(o1.getTimeState() == o2.getTimeState()) {
					return o2.getEndTime().compareTo(o1.getEndTime());
				}
				return o1.getTimeState() - o2.getTimeState();
			}
		});
					
		map.put("chDtos", chDtos);
		
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73YSY(login.getUnitId()),Course.class);
		Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId,Course::getSubjectName);
		map.put("courseNameMap", courseNameMap);
		
		Map<String, String> codeIdMap = EntityUtils.getMap(courseList, Course::getSubjectCode, Course::getId);
		List<String[]> idNames = new ArrayList<String[]>();
		for(String code : BaseConstants.SUBJECT_73) {
			if(codeIdMap.containsKey(code)) {
				idNames.add(new String[] {codeIdMap.get(code), BaseConstants.SUBJECT_CODE_CLASSNAME.get(code)});
			}
		}
		map.put("idNames", idNames);
		
		return "/newgkelective/choice/studentChoiceHead.ftl";			

	}

	@RequestMapping("/student/page")
	@ControllerInfo(value = "学生单个页面选课主页")
	public String showOneIndex(String choiceId,ModelMap map){
		NewGkChoice newGkChoice = newGkChoiceService.findOne(choiceId);
		if(newGkChoice==null) {
			return errorFtl(map, "选课项目不存在");
		}
		map.put("newGkChoice", newGkChoice);
		if(newGkChoice.getStatShow()!=null && Constant.IS_TRUE==newGkChoice.getStatShow()){
			map.put("isOpen",true);
		}
		return "/newgkelective/choice/studentChoiceIndex.ftl";
	}
	
	@ControllerInfo(value = "学生页面选课列表")
	@RequestMapping("/list/page")
	public String showIndex(String hasRead,String choiceId, String isMaster, ModelMap map) {
		
		LoginInfo login= getLoginInfo();
		
		NewGkChoice newGkChoice = newGkChoiceService.findById(choiceId);
		if(newGkChoice==null) {
			return errorFtl(map, "选课项目不存在");
		}
		map.put("newGkChoice", newGkChoice);
		
		Map<String, List<NewGkChoRelation>> newGkChoRelationAllMap= newGkChoRelationService.findByChoiceIdAndObjectTypeIn(newGkChoice.getUnitId(),choiceId,new String[]{NewGkElectiveConstant.CHOICE_TYPE_01,NewGkElectiveConstant.CHOICE_TYPE_06,
				NewGkElectiveConstant.CHOICE_TYPE_02,NewGkElectiveConstant.CHOICE_TYPE_03});
		Map<String,List<NewGkChoResult>> resultMap ;
		if(Constant.IS_TRUE_Str.equals(isMaster)){
			resultMap = newGkChoResultService.findMapByChoiceIdAndStudentIdWithMaster(login.getUnitId(),new String[]{NewGkElectiveConstant.KIND_TYPE_01,NewGkElectiveConstant.KIND_TYPE_02,NewGkElectiveConstant.KIND_TYPE_03}, newGkChoice.getId(), login.getOwnerId());
		}else{
			resultMap = newGkChoResultService.findMapByChoiceIdAndStudentId(getLoginInfo().getUnitId(),new String[]{NewGkElectiveConstant.KIND_TYPE_01,NewGkElectiveConstant.KIND_TYPE_02,NewGkElectiveConstant.KIND_TYPE_03}, newGkChoice.getId(), login.getOwnerId());
		}
		
		List<NewGkChoResult> newGkChoResultList = resultMap.get(NewGkElectiveConstant.KIND_TYPE_01);
		Date now = new Date();
		int sn = DateUtils.compareIgnoreSecond(newGkChoice.getStartTime(), now);
		if(sn > 0 || DateUtils.compareIgnoreSecond(newGkChoice.getEndTime(), now)<0) {
			if(sn>0) {
				map.put("timeState", "0");// 未开始
				if(StringUtils.isBlank(hasRead)) {
					hasRead="1";
				}
			} else {
				map.put("timeState", "2");// 已过期
				if(StringUtils.isBlank(hasRead)) {
					hasRead="1";
				}
			}
		} else {
			map.put("timeState", "1");// 进行中
		}
		if(!Constant.IS_TRUE_Str.equals(hasRead) && CollectionUtils.isEmpty(newGkChoResultList)){
			hasRead="0";//"0"表示未阅读,"1"表示已阅读
		}
		if("0".equals(hasRead)){
			return "/newgkelective/choice/studentReadIndex.ftl";
		}
		List<NewGkChoRelation> newGkChoRelationList = newGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01);
		
		List<String> realList = EntityUtils.getList(newGkChoRelationList, NewGkChoRelation::getObjectValue);
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73YSY(login.getUnitId()),Course.class);
		courseList = courseList.stream().filter(e->realList.contains(e.getId())).collect(Collectors.toList());
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId, e -> e);
		Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId,Course::getSubjectName);
		Map<String, String> courseShortNameMap = EntityUtils.getMap(courseList,Course::getId, Course::getShortName);
		Map<String, String> codeNameMap = EntityUtils.getMap(courseList, Course::getSubjectCode, Course::getSubjectName);
		List<String[]> codeNames = new ArrayList<String[]>();
		for(String code : BaseConstants.SUBJECT_73) {
			if(codeNameMap.containsKey(code)) {
				codeNames.add(new String[] {codeNameMap.get(code), BaseConstants.SUBJECT_CODE_CLASSNAME.get(code)});
			}
		}
		map.put("codeNames", codeNames);
		map.put("courseNameMap", courseNameMap);
		
		//科目选择
		List<NewGkChoCategory> newGkChoCategoryList = newGkChoCategoryService.findByChoiceId(newGkChoice.getUnitId(), newGkChoice.getId());

		List<NewGkChoRelation> combinationRelationList = newGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_06);
		Set<String> subIds = new HashSet<>();
		if (combinationRelationList != null) {
			newGkChoRelationList.addAll(combinationRelationList);
			subIds = combinationRelationList.stream().map(e -> e.getObjectValue()).collect(Collectors.toSet());
		}
		Map<String, List<NewGkChoRelation>> categoryIdToRelationMap = EntityUtils.getListMap(newGkChoRelationList, NewGkChoRelation::getObjectTypeVal, e -> e);
		Map<String, NewGkChoCategory> newGkChoCategoryMap = EntityUtils.getMap(newGkChoCategoryList, NewGkChoCategory::getId);
		List<CourseCategoryDto> courseCategoryDtoList = new ArrayList<>();
		CourseCategoryDto courseCategoryDto;
		NewGkChoCategory newGkChoCategory;
		for (Map.Entry<String, List<NewGkChoRelation>> entry : categoryIdToRelationMap.entrySet()) {
			if (!subIds.contains(entry.getKey())) {
				newGkChoCategory = newGkChoCategoryMap.get(entry.getKey());
				courseCategoryDto = new CourseCategoryDto();
				courseCategoryDto.setId(newGkChoCategory.getId());
				courseCategoryDto.setCategoryName(newGkChoCategory.getCategoryName());
				courseCategoryDto.setOrderId(newGkChoCategory.getOrderId());
				courseCategoryDto.setMaxNum(newGkChoCategory.getMaxNum());
				courseCategoryDto.setMinNum(newGkChoCategory.getMinNum());
				List<Course> coursesTmp = new ArrayList<>();
				List<CourseCategoryDto> combinationList = new ArrayList<>();
				for (NewGkChoRelation tmp : entry.getValue()) {
					if (NewGkElectiveConstant.CHOICE_TYPE_01.equals(tmp.getObjectType())) {
						coursesTmp.add(courseMap.get(tmp.getObjectValue()));
					} else {
						CourseCategoryDto sub = new CourseCategoryDto();
						sub.setId(tmp.getObjectValue());
						combinationList.add(sub);
					}
				}
				courseCategoryDto.setCourseList(coursesTmp);
				courseCategoryDto.setCourseCombination(combinationList);
				courseCategoryDtoList.add(courseCategoryDto);
			}
		}
		// 封装科目组合
		for (CourseCategoryDto one : courseCategoryDtoList) {
			for (CourseCategoryDto sub : one.getCourseCombination()) {
				List<Course> courses = new ArrayList<>();
				StringBuilder name = new StringBuilder();
				for (NewGkChoRelation course : categoryIdToRelationMap.get(sub.getId())) {
					courses.add(courseMap.get(course.getObjectValue()));
					name.append(courseNameMap.get(course.getObjectValue()) + ",");
				}
				sub.setCategoryName(name.substring(0, name.length() - 1));
				sub.setCourseList(courses);
			}
		}
		courseCategoryDtoList.sort(new Comparator<CourseCategoryDto>() {
			@Override
			public int compare(CourseCategoryDto o1, CourseCategoryDto o2) {
				int num1=o1.getOrderId()==null?0:o1.getOrderId();
				int num2=o2.getOrderId()==null?0:o2.getOrderId();
				return num1 - num2;
			}
		});
		map.put("categoryList", courseCategoryDtoList);

		// =============================================================
		//推荐选课
		List<NewGkChoRelation> recommendChoReList = newGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_03);
		if(CollectionUtils.isNotEmpty(recommendChoReList)){
			List<String> recommendValueList = EntityUtils.getList(recommendChoReList, NewGkChoRelation::getObjectValue);
			Map<String,String[]> recommendChoMap = new HashMap<String,String[]>();
			for (String objectValue : recommendValueList) {
				String groupName = "";
				String[] idArr = objectValue.split(",");
				for (String id : idArr) {
					String objectName = courseNameMap.get(id);
					groupName += (objectName+"、");
				}
				
				recommendChoMap.put(groupName.substring(0,groupName.length()-1), idArr);
			}
			map.put("recommendChoMap", recommendChoMap);
		}
		
		//限选组合
		List<NewGkChoRelation> limitChoReList2 = newGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_02);
		if(CollectionUtils.isNotEmpty(limitChoReList2)){
			List<String> limitValueList = EntityUtils.getList(limitChoReList2, NewGkChoRelation::getObjectValue);
			List<String[]> limitChoList = new ArrayList<String[]>(limitValueList.size());
			for (String limitValue : limitValueList) {
				String objectName = "";
				StringBuilder shorts = new StringBuilder();
				String idStr = limitValue;
				String[] idArr = idStr.split(",");
				for (String id : idArr) {
					objectName += courseNameMap.get(id);
					String cs = courseShortNameMap.get(id);
					if(StringUtils.isEmpty(cs)) {
						cs = courseNameMap.get(id);
					}
					shorts.append(cs);
				}
				limitChoList.add(new String[] {objectName, shorts.toString()});
			}
			map.put("limitChoList", limitChoList);
		}
		
		//该学生选课结果
		if(CollectionUtils.isNotEmpty(newGkChoResultList)){
			List<String> resultIdList = EntityUtils.getList(newGkChoResultList, NewGkChoResult::getSubjectId);
			Collections.sort(resultIdList);
			map.put("resultIds", StringUtils.join(resultIdList, ","));
			map.put("resultList", newGkChoResultList);
		}
		
		if(resultMap.containsKey(NewGkElectiveConstant.KIND_TYPE_02)){
			List<NewGkChoResult> inList = resultMap.get(NewGkElectiveConstant.KIND_TYPE_02);
			map.put("wantToSubjectList", EntityUtils.getList(inList, NewGkChoResult::getSubjectId));
		}
		if(resultMap.containsKey(NewGkElectiveConstant.KIND_TYPE_03)){
			List<NewGkChoResult> inList = resultMap.get(NewGkElectiveConstant.KIND_TYPE_03);
			map.put("noWantToSubjectList", EntityUtils.getList(inList, NewGkChoResult::getSubjectId));
		}
		
		if(newGkChoice.getShowNum()==null || newGkChoice.getShowTime()==null){
			map.put("isTips",false);
		}else{
			Date nowDate = new Date();
			if(DateUtils.compareIgnoreSecond(newGkChoice.getShowTime(), nowDate)>0){
				map.put("isTips",false);
			}else{
				//三科目组合选课结果
				List<NewGkChoRelation> newGkChoRelationListTmp = newGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01);
				List<String> courseIds = EntityUtils.getList(newGkChoRelationListTmp, NewGkChoRelation::getObjectValue);
				if(CollectionUtils.isNotEmpty(courseIds)) {
					String jsonStringData = newGkChoResultService.getCount(newGkChoice.getUnitId(),choiceId, courseIds, true);
					map.put("jsonStringData", jsonStringData);
				}
				map.put("isTips",true);
				map.put("showNum", newGkChoice.getShowNum());
				map.put("hintContent", newGkChoice.getHintContent());
			}
		}

		String referId = newGkChoice.getReferScoreId();
		if(Objects.equal(1, newGkChoice.getShowSamesele())&&StringUtils.isNotBlank(referId)){
			List<Course> allCourseList = SUtils.dt(courseRemoteService.findByCodesYSY(login.getUnitId()),Course.class);
			allCourseList.addAll(courseList);
			NewGkReferScore referScore = newGkReferScoreService.findById(newGkChoice.getReferScoreId(), false, null);
			if(referScore!=null){
				map.put("referScore", referScore);
				List<String> subjectNames = EntityUtils.getList(allCourseList, Course::getSubjectName);
				List<String> subjectIds = EntityUtils.getList(allCourseList, Course::getId);
				int totalRanking = 1;
				float totalScore = 0f;
				List<Integer> rankingList = new ArrayList<Integer>();
				int studentNum = 1;
				List<NewGkScoreResult> scoreList = newGkScoreResultService.findListByReferScoreId(login.getUnitId(), referId);
				if(CollectionUtils.isNotEmpty(scoreList)){//有导入成绩
					List<Student> studentList = SUtils.dt(studentRemoteService.findByGradeId(newGkChoice.getGradeId()),Student.class);
					List<String> studentIds = EntityUtils.getList(studentList, Student::getId);
					studentNum = studentList.size();
					scoreList = scoreList.stream().filter(e->studentIds.contains(e.getStudentId())).collect(Collectors.toList());
					Map<String, List<NewGkScoreResult>> subjectScoreMap = EntityUtils.getListMap(scoreList, NewGkScoreResult::getSubjectId, Function.identity());
					for (String sid : subjectIds) {
						if(subjectScoreMap.containsKey(sid)){
							List<NewGkScoreResult> scores = subjectScoreMap.get(sid);
							List<NewGkScoreResult> thisScores = scores.stream().filter(e->login.getOwnerId().equals(e.getStudentId())).collect(Collectors.toList());
							if(CollectionUtils.isNotEmpty(thisScores)&&thisScores.get(0).getScore()!=null){//该学生成绩不为空，则按实际排名
								int i = 1;
								for (NewGkScoreResult s : scores) {
									if(s.getScore()!=null && s.getScore()>thisScores.get(0).getScore()){
										i++;
									}
								}
								rankingList.add(i);
							}else{//该学生成绩为空，取最后一名
								rankingList.add(scores.size()+1);
							}
						}else{//该科目为空，该科目默认第一名
							rankingList.add(1);
						}
					}
					Map<String, List<NewGkScoreResult>> studentScoreMap= EntityUtils.getListMap(scoreList, NewGkScoreResult::getStudentId, Function.identity());
					if(studentScoreMap.containsKey(login.getOwnerId())){
						totalScore = studentScoreMap.get(login.getOwnerId()).stream().filter(e->e.getScore()!=null).map(NewGkScoreResult::getScore).reduce(Float::sum).get();
						for (Entry<String, List<NewGkScoreResult>> entry : studentScoreMap.entrySet()) {
							Float otherScore = entry.getValue().stream().filter(e->e.getScore()!=null).map(NewGkScoreResult::getScore).reduce(Float::sum).get();
							if(otherScore>totalScore){
								totalRanking++;
							}
						}
					}else{
						totalRanking = studentScoreMap.keySet().size()+1;
					}
				}else{//没有导入成绩，所有默认为第一名
					for (int i = 0; i < subjectIds.size(); i++) {
						rankingList.add(1);
					}
				}
				map.put("totalRanking", totalRanking);
				map.put("totalScore", totalScore);
				map.put("rankingList", rankingList);
				map.put("subjectNames", subjectNames);
				map.put("studentNum", studentNum);
			}
		}
		
		Set<String> courseIds = newGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01).stream().map(e -> e.getObjectValue()).collect(Collectors.toSet());
		courseList = courseList.stream().filter(e -> courseIds.contains(e.getId())).collect(Collectors.toList());

		map.put("courseList", courseList);
		
		return "/newgkelective/choice/studentChoiceList.ftl";

	}

	@RequestMapping("/save")
	@ResponseBody
	@ControllerInfo(value = "学生保存选课")
	public String saveChoice(String subjectIds,String choiceId,String subjectTypes,String wantToIds,String noWantToIds, ModelMap map) {
		if(StringUtils.isBlank(subjectTypes) || StringUtils.isBlank(subjectIds)||StringUtil.isBlank(choiceId)){
			return error("参数为空");
		}
		String[] subjectIdsArr = subjectIds.split(",");
		List<String> subjectIdsList = Arrays.asList(subjectIdsArr);
		String studentId = getLoginInfo().getOwnerId();
        try {
        	//判断时间
        	NewGkChoice newGkChoice = newGkChoiceService.findOne(choiceId);
			Date now = new Date();
			if (DateUtils.compareIgnoreSecond(newGkChoice.getEndTime(), now)<0) {
				return returnError("保存失败","已超过选课截止时间！");
			} 
        	//判断是否被锁定
        	List<String> lockChoList = newGkChoRelationService.findByChoiceIdAndObjectType(newGkChoice.getUnitId(),choiceId, NewGkElectiveConstant.CHOICE_TYPE_05);
        	if(CollectionUtils.isNotEmpty(lockChoList)&&lockChoList.contains(studentId)){
        		return returnError("保存失败","您的选课结果已被管理员锁定！");
        	}
        	//判断限选
        	List<String> limitChoList = newGkChoRelationService.findByChoiceIdAndObjectType(newGkChoice.getUnitId(),choiceId, NewGkElectiveConstant.CHOICE_TYPE_02);
        	for (String limitSubject : limitChoList) {
        		List<String> limitSubjectList = Arrays.asList(limitSubject.split(","));
        		if (limitSubjectList.containsAll(subjectIdsList)) {
        			 return returnError("保存失败","您选择的是不推荐组合，请调整！");
        		}
        	}
        	
			String[] subjectTypesArr = subjectTypes.split(",");
			String[] wantToIdsArr = null;
			String[] noWantToIdsArr = null;
			if(StringUtils.isNotBlank(wantToIds)){
				wantToIdsArr = wantToIds.split(",");
			}
			if(StringUtils.isNotBlank(noWantToIds)){
				noWantToIdsArr = noWantToIds.split(",");
			}
            newGkChoResultService.saveNewGkChoResult(getLoginInfo().getUnitId(),choiceId,studentId,subjectIdsList,subjectTypesArr,wantToIdsArr,noWantToIdsArr);
        }
        catch (Exception e) {
        	e.printStackTrace();
            return error(e.getMessage());
        }
        return returnSuccess();
	}
	
	@ControllerInfo(value = "学生页面选课统计")
	@RequestMapping("/count/page")
	public String showCount(String choiceId, ModelMap map) {
		NewGkChoice newChoice=newGkChoiceService.findOne(choiceId);//选课
		if(newChoice==null) return errorFtl(map, "找不到选课");
		
		//获取选考科目
		Map<String, List<NewGkChoRelation>> newGkChoRelationAllMap= newGkChoRelationService.findByChoiceIdAndObjectTypeIn(newChoice.getUnitId(),choiceId,new String[]{NewGkElectiveConstant.CHOICE_TYPE_01});
		List<NewGkChoRelation> newGkChoRelationList = newGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01);
		List<String> courseIds = EntityUtils.getList(newGkChoRelationList, NewGkChoRelation::getObjectValue);
		if(CollectionUtils.isEmpty(courseIds)) {
			return errorFtl(map, "选课没有维护选课科目");
		}
		String jsonStringData = newGkChoResultService.getCount(newChoice.getUnitId(),choiceId, courseIds, true);
		map.put("jsonStringData", jsonStringData);
		map.put("newGkChoice", newChoice);
		return "/newgkelective/choice/studentChoiceCount.ftl";
	}
	
	@ControllerInfo(value = "学生已选科目页面")
	@RequestMapping("/detail/page")
	public String showDetail(String choiceId, String isMaster, ModelMap map) {
		
		String studentId = getLoginInfo().getOwnerId();
		
		NewGkChoice newGkChoice = newGkChoiceService.findById(choiceId);
		if(newGkChoice==null) {
			return errorFtl(map, "选课项目不存在");
		}
		map.put("newGkChoice", newGkChoice);
		
		Map<String, List<NewGkChoRelation>> newGkChoRelationAllMap= newGkChoRelationService.findByChoiceIdAndObjectTypeIn(newGkChoice.getUnitId(),choiceId,new String[]{NewGkElectiveConstant.CHOICE_TYPE_01,
				NewGkElectiveConstant.CHOICE_TYPE_02,NewGkElectiveConstant.CHOICE_TYPE_03});
		Map<String,List<NewGkChoResult>> resultMap ;
		if(Constant.IS_TRUE_Str.equals(isMaster)){
			resultMap = newGkChoResultService.findMapByChoiceIdAndStudentIdWithMaster(getLoginInfo().getUnitId(),new String[]{NewGkElectiveConstant.KIND_TYPE_01,NewGkElectiveConstant.KIND_TYPE_02,NewGkElectiveConstant.KIND_TYPE_03}, newGkChoice.getId(), studentId);
		}else{
			resultMap = newGkChoResultService.findMapByChoiceIdAndStudentId(getLoginInfo().getUnitId(),new String[]{NewGkElectiveConstant.KIND_TYPE_01,NewGkElectiveConstant.KIND_TYPE_02,NewGkElectiveConstant.KIND_TYPE_03}, newGkChoice.getId(), studentId);
		}
		
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73YSY(getLoginInfo().getUnitId()),Course.class);
		Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId,Course::getSubjectName);
		map.put("courseNameMap", courseNameMap);
		
		Date now = new Date();
		boolean isReelect = false;
		if(DateUtils.compareIgnoreSecond(newGkChoice.getStartTime(), now) < 0 
				&& DateUtils.compareIgnoreSecond(newGkChoice.getEndTime(), now)>0) {
			isReelect = true;// 是否可以重新选择
		}
		map.put("isReelect", isReelect);
		
		//该学生选课结果
		if(resultMap.containsKey(NewGkElectiveConstant.KIND_TYPE_01)){
			List<NewGkChoResult> newGkChoResultList = resultMap.get(NewGkElectiveConstant.KIND_TYPE_01);
			List<String> subIds = EntityUtils.getList(newGkChoResultList, NewGkChoResult::getSubjectId);
			Collections.sort(subIds);
			map.put("resultIds", StringUtils.join(subIds, ","));
			map.put("resultList", newGkChoResultList);
		}
		if(resultMap.containsKey(NewGkElectiveConstant.KIND_TYPE_02)){
			List<NewGkChoResult> inList = resultMap.get(NewGkElectiveConstant.KIND_TYPE_02);
			map.put("wantToSubjectList", EntityUtils.getList(inList, NewGkChoResult::getSubjectId));
		}
		if(resultMap.containsKey(NewGkElectiveConstant.KIND_TYPE_03)){
			List<NewGkChoResult> inList = resultMap.get(NewGkElectiveConstant.KIND_TYPE_03);
			map.put("noWantToSubjectList", EntityUtils.getList(inList, NewGkChoResult::getSubjectId));
		}
		
		Map<String, String> codeIdMap = EntityUtils.getMap(courseList, Course::getSubjectCode, Course::getId);
		List<String[]> idNames = new ArrayList<String[]>();
		for(String code : BaseConstants.SUBJECT_73) {
			if(codeIdMap.containsKey(code)) {
				idNames.add(new String[] {codeIdMap.get(code), BaseConstants.SUBJECT_CODE_CLASSNAME.get(code)});
			}
		}
		map.put("idNames", idNames);
		
		//提示
		if(newGkChoice.getShowNum()==null || newGkChoice.getShowTime()==null){
			map.put("isTips",false);
		}else{
			Date nowDate = new Date();
			if(DateUtils.compareIgnoreSecond(newGkChoice.getShowTime(), nowDate)>0){
				map.put("isTips",false);
			}else{
				//三科目组合选课结果
				List<NewGkChoRelation> newGkChoRelationList = newGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01);
				List<String> courseIds = EntityUtils.getList(newGkChoRelationList, NewGkChoRelation::getObjectValue);
				if(CollectionUtils.isNotEmpty(courseIds)) {
					String jsonStringData = newGkChoResultService.getCount(newGkChoice.getUnitId(),choiceId, courseIds, true);
					map.put("jsonStringData", jsonStringData);
				}
				map.put("isTips",true);
				map.put("showNum", newGkChoice.getShowNum());
				map.put("hintContent", newGkChoice.getHintContent());
			}
		}
		
		return "/newgkelective/choice/studentChoiceDetail.ftl";

	}
	
	@ControllerInfo(value = "学生选课排名")
	@RequestMapping("/ranking/page")
	public String showRanking(String choiceId, String referScoreId, String subjectIds, ModelMap map) {
		if(StringUtils.isBlank(subjectIds)){
			return "/newgkelective/choice/studentChoiceRanking.ftl";
		}
		LoginInfo login = getLoginInfo();
		String unitId = login.getUnitId();
		String studentId = login.getOwnerId();
		List<String> chooseIds = Arrays.stream(subjectIds.split(",")).collect(Collectors.toList());
		NewGkChoice newGkChoice = newGkChoiceService.findOne(choiceId);
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73YSY(unitId),Course.class);
		Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId,Course::getSubjectName);
		Map<String, String> shortNameMap = EntityUtils.getMap(courseList, Course::getId,Course::getShortName);
		List<Course> ysyList = SUtils.dt(courseRemoteService.findByCodesYSY(unitId),Course.class);
		List<String> ysyIds = EntityUtils.getList(ysyList, Course::getId);
		List<Student> studentList = SUtils.dt(studentRemoteService.findByGradeId(newGkChoice.getGradeId()),Student.class);
		List<String> studentIds = EntityUtils.getList(studentList, Student::getId);
		
		Map<String, NewGkScoreDto> dtoMap = new HashMap<String, NewGkScoreDto>();
		for (String chooseId : chooseIds) {
			NewGkScoreDto dto = new NewGkScoreDto();
			dto.setSubjectId(chooseId);
			dto.setSubjectName(courseNameMap.get(chooseId));
			dtoMap.put(chooseId, dto);
		}
		
		//同选人数
		List<NewGkChoResult> resultList = newGkChoResultService.findByChoiceIdAndKindType(unitId, NewGkElectiveConstant.KIND_TYPE_01, choiceId);
		resultList = resultList.stream().filter(e->studentIds.contains(e.getStudentId())&&subjectIds.contains(e.getSubjectId())&&!studentId.equals(e.getStudentId())).collect(Collectors.toList());
		Map<String, List<NewGkChoResult>> subjectResultMap = EntityUtils.getListMap(resultList, NewGkChoResult::getSubjectId, Function.identity());
		Map<String, List<String>> subjectStudentIdMap = new HashMap<String, List<String>>();
		for (String chooseId : chooseIds) {
			if(subjectResultMap.containsKey(chooseId)){
				dtoMap.get(chooseId).setSelectNum(subjectResultMap.get(chooseId).size());
				subjectStudentIdMap.put(chooseId, EntityUtils.getList(subjectResultMap.get(chooseId), NewGkChoResult::getStudentId));
			}else{
				dtoMap.get(chooseId).setSelectNum(0);
				subjectStudentIdMap.put(chooseId, new ArrayList<String>());
			}
		}
		
		//三科同选人数
		List<String> selectStudentIds = new ArrayList<String>();
		if(chooseIds.size()==newGkChoice.getChooseNum()){
			Map<String, List<NewGkChoResult>> studentResultMap = EntityUtils.getListMap(resultList, NewGkChoResult::getStudentId, Function.identity());
			for (Entry<String, List<NewGkChoResult>> entry : studentResultMap.entrySet()) {
				if(entry.getValue().size()==chooseIds.size()){
					List<String> otherChooseIds = EntityUtils.getList(entry.getValue(), NewGkChoResult::getSubjectId);
					if(otherChooseIds.containsAll(chooseIds)){
						selectStudentIds.add(entry.getKey());
					}
				}
			}
			NewGkScoreDto dto1 = new NewGkScoreDto();
			NewGkScoreDto dto2 = new NewGkScoreDto();
			dto1.setSelectNum(selectStudentIds.size());
			dto2.setSelectNum(selectStudentIds.size());
			String shortNames = shortNameMap.entrySet().stream().filter(e->chooseIds.contains(e.getKey())).map(e->e.getValue()).reduce("",String::concat);
			String ysyNames = ysyList.stream().map(e->e.getShortName()).reduce("", String::concat);
			dto1.setSubjectName(shortNames);
			dto2.setSubjectName(shortNames+"+"+ysyNames);
			dtoMap.put("3", dto1);
			dtoMap.put("3+ysy", dto2);
		}
		
		//年级排名和同选排名
		List<NewGkScoreResult> scoreList = newGkScoreResultService.findListByReferScoreId(unitId, referScoreId);
		if(CollectionUtils.isNotEmpty(scoreList)){
			if(chooseIds.size()<newGkChoice.getChooseNum()){
				scoreList = scoreList.stream().filter(e->chooseIds.contains(e.getSubjectId())).collect(Collectors.toList());
			}else{
				scoreList = scoreList.stream().filter(e->chooseIds.contains(e.getSubjectId())||ysyIds.contains(e.getSubjectId())).collect(Collectors.toList());
			}
			scoreList = scoreList.stream().filter(e->studentIds.contains(e.getStudentId())).collect(Collectors.toList());
			Map<String, List<NewGkScoreResult>> subjectScoreMap = EntityUtils.getListMap(scoreList, NewGkScoreResult::getSubjectId, Function.identity());
			for (String sid : chooseIds) {
				NewGkScoreDto dto = dtoMap.get(sid);
				if(subjectScoreMap.containsKey(sid)){
					List<NewGkScoreResult> scores = subjectScoreMap.get(sid);
					List<NewGkScoreResult> thisScores = scores.stream().filter(e->login.getOwnerId().equals(e.getStudentId())).collect(Collectors.toList());
					if(CollectionUtils.isNotEmpty(thisScores)&&thisScores.get(0).getScore()!=null){
						float thisScore = thisScores.get(0).getScore();
						int gradeRanking = 1;
						int selectRanking = 1;
						for (NewGkScoreResult s : scores) {
							if(s.getScore()!=null && s.getScore()>thisScore){
								gradeRanking++;
								if(subjectStudentIdMap.get(sid).contains(s.getStudentId())){
									selectRanking++;
								}
							}
						}
						dto.setGradeRanking(gradeRanking);
						dto.setSelectRanking(selectRanking);
					}else{
						dto.setGradeRanking(scores.size()+1);
						dto.setSelectRanking(selectStudentIds.size()+1);
					}
				}else{
					dto.setGradeRanking(1);
					dto.setSelectRanking(1);
				}
			}
			if(chooseIds.size()==newGkChoice.getChooseNum()){
				Map<String, List<NewGkScoreResult>> studentScoreMap = EntityUtils.getListMap(scoreList, NewGkScoreResult::getStudentId, Function.identity());
				if(studentScoreMap.containsKey(studentId)){
					List<NewGkScoreResult> thisScoreList = studentScoreMap.get(studentId);
					int chooseGradeRanking = 1;
					int chooseSelectRanking = 1;
					int ysyGradeRanking = 1;
					int ysySelectRanking = 1;
					float chooseScore = thisScoreList.stream().filter(e->e.getScore()!=null&&chooseIds.contains(e.getSubjectId())).map(NewGkScoreResult::getScore).reduce(Float::sum).get();
					float ysyScore = thisScoreList.stream().filter(e->e.getScore()!=null).map(NewGkScoreResult::getScore).reduce(Float::sum).get();
					for (Entry<String, List<NewGkScoreResult>> entry : studentScoreMap.entrySet()) {
						float otherChooseScore = entry.getValue().stream().filter(e->e.getScore()!=null&&chooseIds.contains(e.getSubjectId())).map(NewGkScoreResult::getScore).reduce(Float::sum).get();
						float otherYsyScore = entry.getValue().stream().filter(e->e.getScore()!=null).map(NewGkScoreResult::getScore).reduce(Float::sum).get();
						if(otherChooseScore>chooseScore){
							chooseGradeRanking++;
							if(selectStudentIds.contains(entry.getKey())){
								chooseSelectRanking++;
							}
						}
						if(otherYsyScore>ysyScore){
							ysyGradeRanking++;
							if(selectStudentIds.contains(entry.getKey())){
								ysySelectRanking++;
							}
						}
					}
					dtoMap.get("3").setGradeRanking(chooseGradeRanking);
					dtoMap.get("3").setSelectRanking(chooseSelectRanking);
					dtoMap.get("3+ysy").setGradeRanking(ysyGradeRanking);
					dtoMap.get("3+ysy").setSelectRanking(ysySelectRanking);
				}else{
					dtoMap.get("3").setGradeRanking(studentScoreMap.keySet().size()+1);
					dtoMap.get("3").setSelectRanking(selectStudentIds.size()+1);
					dtoMap.get("3+ysy").setGradeRanking(studentScoreMap.keySet().size()+1);
					dtoMap.get("3+ysy").setSelectRanking(selectStudentIds.size()+1);
				}
			}
			
		}else{
			for (String chooseId : chooseIds) {
				dtoMap.get(chooseId).setGradeRanking(1);
				dtoMap.get(chooseId).setSelectRanking(1);
			}
			dtoMap.get("3").setGradeRanking(1);
			dtoMap.get("3").setSelectNum(1);
			dtoMap.get("3+ysy").setGradeRanking(1);
			dtoMap.get("3+ysy").setSelectNum(1);
		}
		
		List<NewGkScoreDto> dtoList = new ArrayList<NewGkScoreDto>();
		for (String chooseId : chooseIds) {
			dtoList.add(dtoMap.get(chooseId));
		}
		if(chooseIds.size()==newGkChoice.getChooseNum()){
			dtoList.add(dtoMap.get("3"));
			dtoList.add(dtoMap.get("3+ysy"));
		}
		
		map.put("dtoList", dtoList);
		return "/newgkelective/choice/studentChoiceRanking.ftl";

	}
}
