package net.zdsoft.newgkelective.data.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.newgkelective.data.dto.CourseCategoryDto;
import net.zdsoft.newgkelective.data.entity.*;
import net.zdsoft.newgkelective.data.service.*;
import org.apache.commons.collections.CollectionUtils;
import net.zdsoft.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.ChoiceSubjectDto;
import net.zdsoft.newgkelective.data.dto.NewGkChoiceDto;
import net.zdsoft.newgkelective.data.utils.CombineAlgorithmInt;

@Controller
@RequestMapping("/newgkelective")
public class NewGkElectiveChoiceIndexAction extends BaseAction {

	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private NewGkChoiceService newGkChoiceService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private NewGkDivideService newGkDivideService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
    private NewGkChoCategoryService newGkChoCategoryService;
	@Autowired
	private NewGkReferScoreService newGkReferScoreService;
	@Autowired
	private UnitRemoteService unitRemoteService;

	@RequestMapping("/{gradeId}/choice/publish/page")
	@ControllerInfo(value = "发布选课首页")
	public String showPublichChoiceIndex(
			@PathVariable("gradeId") String gradeId, ModelMap map) {

		NewGkChoiceDto newGkChoiceDto = new NewGkChoiceDto();

		newGkChoiceDto.setGradeId(gradeId);
		List<Course> list = SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()),new TR<List<Course>>() {});
		if(CollectionUtils.isEmpty(list)) {
			return errorFtl(map, "请联系管理员，初始化科目数据");
		}

		List<NewGkReferScore> referScoreList = newGkReferScoreService.findListByGradeId(getLoginInfo().getUnitId(), gradeId, false, false);
		map.put("referScoreList", referScoreList);

		Map<String, String> studentCodeToIdMap = EntityUtils.getMap(list, Course::getSubjectCode, Course::getId);
		map.put("allCourse", list);
        map.put("codeToIdMap", studentCodeToIdMap);

		List<CourseCategoryDto> courseCategoryDtoList = getChoiceCourseDtoList(list);
		newGkChoiceDto.setCourseCategoryDtoList(courseCategoryDtoList);

		map.put("newGkChoiceDto", newGkChoiceDto);
		
		LoginInfo loginInfo = getLoginInfo();
		Grade grade = gradeRemoteService.findOneObjectById(gradeId);
		Integer times = newGkChoiceService.getChoiceMaxTime(loginInfo.getUnitId(),
				gradeId);
		if (times == null) {
			times = 1;
		} else {
			times = times + 1;
		}
		
		String semesterJson = semesterRemoteService.getCurrentSemester(2, loginInfo.getUnitId());
		Semester semester = SUtils.dc(semesterJson, Semester.class);
		//NewGkDivide divide = newGkDivideService.findById(divide_id);
		//Grade grade = SUtils.dc(gradeRemoteService.findById(divide.getGradeId()), Grade.class);	
		String choiceName = semester.getAcadyear()+"学年"+grade.getGradeName()+"第"+
				semester.getSemester()+"学期第"+times+"次选课";
		map.put("choiceName", choiceName);
		map.put("gradeId", gradeId);

		// 只有浙江有7选3
		Unit unit = unitRemoteService.findOneObjectById(getLoginInfo().getUnitId());
		if(unit !=null && StringUtils.isNotBlank(unit.getRegionCode()) && unit.getRegionCode().indexOf("33")==0) {
			map.put("isZheJiang", true);
		}
		
		return "/newgkelective/choice/publishChoiceIndex.ftl";
	}

	@RequestMapping("/choice/getSubjectList/page")
	@ControllerInfo(value = "推荐禁选科目首页")
	public String showGetRecommendList(HttpServletRequest request, ModelMap map) {
		String str = null;
		try {
			str = URLDecoder.decode(request.getParameter("str"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String state = request.getParameter("state");
		String chooseNum = request.getParameter("chooseNum");
		String choiceId = request.getParameter("choiceId");
		String type = request.getParameter("type");
		// 准备获取推荐组合
		List<Course> courseList = splitStr(str);
		List<String> ids = courseList.stream().map(e -> e.getId()).collect(Collectors.toList());
		courseList = SUtils.dt(courseRemoteService.findListByIds(ids.toArray(new String[0])), Course.class);
		Integer[] cSize = new Integer[courseList.size()];
        for(int i = 0;i < courseList.size();i++){
        	cSize[i] = i;
        }
        CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize,Integer.parseInt(chooseNum));
        Integer[][] result = combineAlgorithm.getResutl();
		List<ChoiceSubjectDto> choiceSubjectDtoList = getChoiceSubjectDtoList(courseList,result,state,choiceId);
		if ("3_1_2".equals(type)) {
			choiceSubjectDtoList = choiceSubjectDtoList.stream().filter(e ->
				!((e.getShortNames().contains("物") && (e.getShortNames().contains("历") || e.getShortNames().contains("史")))
						|| (!e.getShortNames().contains("物") && !(e.getShortNames().contains("历") || e.getShortNames().contains("史")))))
						.collect(Collectors.toList());
		} else if ("3_2_1".equals(type)) {
			choiceSubjectDtoList = choiceSubjectDtoList.stream().filter(e ->
			((e.getShortNames().contains("物") && e.getShortNames().contains("化"))
					|| (e.getShortNames().contains("政") && (e.getShortNames().contains("历") || e.getShortNames().contains("史")))))
					.collect(Collectors.toList());
		}
		map.put("choiceSubjectDtoList", choiceSubjectDtoList);
		map.put("state", state);
		map.put("courseList", courseList);
		return "/newgkelective/choice/getSubjectList.ftl";
	}
	
	//临时演示
	@RequestMapping("/choice/setStudentNum/page")
	@ControllerInfo(value = "设置组合限定人数")
	public String showSetStudentNum(HttpServletRequest request, ModelMap map) {
		String str = null;
		try {
			str = URLDecoder.decode(request.getParameter("str"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String state = request.getParameter("state");
		String chooseNum = request.getParameter("chooseNum");
		String choiceId = request.getParameter("choiceId");
		// 准备获取推荐组合
		List<Course> courseList = splitStr(str);
		Integer[] cSize = new Integer[courseList.size()];
        for(int i = 0;i < courseList.size();i++){
        	cSize[i] = i;
        }
        CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize,Integer.parseInt(chooseNum));
        Integer[][] result = combineAlgorithm.getResutl();
		List<ChoiceSubjectDto> choiceSubjectDtoList = getChoiceSubjectDtoList(courseList,result,state,choiceId);

		map.put("choiceSubjectDtoList", choiceSubjectDtoList);
		map.put("state", state);
		return "/newgkelective/choice/setStudentNum.ftl";
	}

	@ResponseBody
	@RequestMapping("/choice/saveChoice")
	@ControllerInfo(value = "选课公告设置发布")
	public String doChoiceSave(NewGkChoiceDto newGkChoiceDto) {
		String choiceId = newGkChoiceDto.getChoiceId();
		String unitId = getLoginInfo().getUnitId();
		Set<String> oldSubids=newGkChoRelationService.findByChoiceIdAndObjectType(unitId,choiceId, NewGkElectiveConstant.CHOICE_TYPE_01).stream().collect(Collectors.toSet());
		if(CollectionUtils.isNotEmpty(oldSubids)) {
			List<String> newSubids =new ArrayList<>();
			for(CourseCategoryDto ent: newGkChoiceDto.getCourseCategoryDtoList()){
				if (ent.getCourseList() != null) {
					for(Course one : ent.getCourseList()) {
						newSubids.add(one.getId());
					}
				}
				if (ent.getCourseCombination() != null) {
					for (CourseCategoryDto sub : ent.getCourseCombination()) {
						for(Course one : sub.getCourseList()) {
							newSubids.add(one.getId());
						}
					}
				}
			}
			Set<String> newSubIdSet = newSubids.stream().collect(Collectors.toSet());
			if(!(oldSubids.containsAll(newSubIdSet) && newSubIdSet.containsAll(oldSubids))) {
				return error("选课科目不能调整！");
			}
		}
		/*List<NewGkChoice> oldChoiceList = newGkChoiceService.findListByGradeId(newGkChoiceDto.getGradeId());
		if(StringUtils.isNotBlank(choiceId)){
			oldChoiceList = oldChoiceList.stream().filter(e->!e.getId().equals(choiceId)).collect(Collectors.toList());
		}
		for (NewGkChoice newGkChoice : oldChoiceList) {
			if(!(DateUtils.compareIgnoreSecond(newGkChoiceDto.getStartTime(),newGkChoice.getEndTime())>0
					||DateUtils.compareIgnoreSecond(newGkChoiceDto.getEndTime(),newGkChoice.getStartTime())<0)){
				return error("选课时间与"+newGkChoice.getChoiceName()+"有交叉！");
			}
		}*/
        // 封装需要保存的NewGkChoice
        String id = UuidUtils.generateUuid();

		// 封装选课类别
        // 无论发布选课还是修改选课设置，均重新建立类别
        List<NewGkChoCategory> newGkChoCategoryList = new ArrayList<>();
        Set<String> oldCourseCategoryIds = new HashSet<>();
        int index = 0;
        for (CourseCategoryDto courseCategoryDto : newGkChoiceDto.getCourseCategoryDtoList()) {
            NewGkChoCategory newGkChoCategory = new NewGkChoCategory();
            newGkChoCategory.setId(UuidUtils.generateUuid());
            newGkChoCategory.setChoiceId(StringUtils.isBlank(choiceId) ? id : choiceId);
            newGkChoCategory.setCategoryType(NewGkElectiveConstant.CATEGORY_TYPE_1);
            newGkChoCategory.setCategoryName(courseCategoryDto.getCategoryName());
            newGkChoCategory.setIsDeleted(0);
            newGkChoCategory.setMaxNum(courseCategoryDto.getMaxNum());
            newGkChoCategory.setMinNum(courseCategoryDto.getMinNum());
            newGkChoCategory.setOrderId(index++);
            newGkChoCategory.setUnitId(unitId);
            newGkChoCategory.setCreationTime(new Date());
            newGkChoCategory.setModifyTime(new Date());
            newGkChoCategoryList.add(newGkChoCategory);
            // 替换类别Id
            oldCourseCategoryIds.add(courseCategoryDto.getId());
            courseCategoryDto.setId(newGkChoCategory.getId());
            if (courseCategoryDto.getCourseCombination() != null) {
                for (CourseCategoryDto sub : courseCategoryDto.getCourseCombination()) {
                    // 科目组合
                    NewGkChoCategory subItem = new NewGkChoCategory();
                    subItem.setId(UuidUtils.generateUuid());
                    subItem.setChoiceId(StringUtils.isBlank(choiceId) ? id : choiceId);
                    subItem.setCategoryType(NewGkElectiveConstant.CATEGORY_TYPE_2);
                    subItem.setCategoryName(sub.getCategoryName());
                    subItem.setIsDeleted(0);
                    subItem.setUnitId(unitId);
                    subItem.setCreationTime(new Date());
                    subItem.setModifyTime(new Date());
                    newGkChoCategoryList.add(subItem);
                    oldCourseCategoryIds.add(sub.getId());
                    sub.setId(subItem.getId());
                }
            }
        }

		NewGkChoice newGkChoice = getSaveNewGkChoice(newGkChoiceDto, id,
				choiceId, unitId);
		// 封装需要保存的List<NewGkChoRelation>
		List<NewGkChoRelation> newGkChoRelations = null;
		try {
			newGkChoRelations = getSaveGkChoRelations(
					newGkChoiceDto, id, choiceId, unitId);
		} catch (Exception e) {
			e.printStackTrace();
			return error("选课组合信息有误");
		}

		try {
			if (StringUtils.isNotEmpty(choiceId)) {
				// 有考试id 需要先删除 再保存
				newGkChoiceService.saveAndDeleteNewGkChoice(newGkChoice, newGkChoRelations, newGkChoCategoryList, oldCourseCategoryIds.toArray(new String[0]), choiceId, unitId);
			} else {
				// 没有考试id 则直接保存就成
				newGkChoiceService.saveNewGkChoice(newGkChoice, newGkChoRelations, newGkChoCategoryList, oldCourseCategoryIds.toArray(new String[0]), unitId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}

	@RequestMapping("/{gradeId}/{choiceId}/choice/publish/page")
	@ControllerInfo(value = "查看选课设置首页")
	public String showChoiceIndex(@PathVariable("gradeId") String gradeId,
			@PathVariable("choiceId") String choiceId, ModelMap map) {

		NewGkChoiceDto newGkChoiceDto = new NewGkChoiceDto();

		newGkChoiceDto.setChoiceId(choiceId);
		newGkChoiceDto.setGradeId(gradeId);
		NewGkChoice newGkChoice = newGkChoiceService.findById(choiceId);
		newGkChoiceDto.setChooseNum(newGkChoice.getChooseNum());
		newGkChoiceDto.setStartTime(newGkChoice.getStartTime());
		newGkChoiceDto.setEndTime(newGkChoice.getEndTime());
		newGkChoiceDto.setChoiceName(newGkChoice.getChoiceName());
		newGkChoiceDto.setNotice(newGkChoice.getNotice());
		newGkChoiceDto.setStatShow(newGkChoice.getStatShow());
		newGkChoiceDto.setShowNum(newGkChoice.getShowNum());
		newGkChoiceDto.setShowTime(newGkChoice.getShowTime());
		newGkChoiceDto.setHintContent(newGkChoice.getHintContent());
		newGkChoiceDto.setReferScoreId(newGkChoice.getReferScoreId());
		newGkChoiceDto.setShowSamesele(newGkChoice.getShowSamesele());
		map.put("choiceName", newGkChoice.getChoiceName());
		
		List<Course> list = SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()),new TR<List<Course>>() {});
		map.put("allCourse", list);

		List<NewGkReferScore> referScoreList = newGkReferScoreService.findListByGradeId(getLoginInfo().getUnitId(), gradeId, false, false);
		map.put("referScoreList", referScoreList);

		//此处不再排序
		Map<String, Course> courseMap = new HashMap<String, Course>();
		for (Course course : list) {
			courseMap.put(course.getId(), course);
		}
		Map<String, List<NewGkChoRelation>> NewGkChoRelationAllMap= newGkChoRelationService.findByChoiceIdAndObjectTypeIn(newGkChoice.getUnitId(),choiceId,new String[]{NewGkElectiveConstant.CHOICE_TYPE_01, NewGkElectiveConstant.CHOICE_TYPE_06
				,NewGkElectiveConstant.CHOICE_TYPE_03,NewGkElectiveConstant.CHOICE_TYPE_02});
		//还有三个 list封装
		List<NewGkChoRelation> newGkChoRelationList = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01);
		List<NewGkChoRelation> combinationRelationList = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_06);
		Set<String> subIds = new HashSet<>();
		if (combinationRelationList != null) {
			newGkChoRelationList.addAll(combinationRelationList);
			subIds = combinationRelationList.stream().map(e -> e.getObjectValue()).collect(Collectors.toSet());
		}
		Map<String, List<NewGkChoRelation>> categoryIdToRelationMap = EntityUtils.getListMap(newGkChoRelationList, NewGkChoRelation::getObjectTypeVal, e -> e);
		List<NewGkChoCategory> newGkChoCategoryList = newGkChoCategoryService.findListByIdIn(categoryIdToRelationMap.keySet().toArray(new String[0]));
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
				List<Course> courseList = new ArrayList<>();
				List<CourseCategoryDto> combinationList = new ArrayList<>();
				for (NewGkChoRelation tmp : entry.getValue()) {
					if (NewGkElectiveConstant.CHOICE_TYPE_01.equals(tmp.getObjectType())) {
						courseList.add(courseMap.get(tmp.getObjectValue()));
					} else {
						CourseCategoryDto sub = new CourseCategoryDto();
						sub.setId(tmp.getObjectValue());
						combinationList.add(sub);
					}
				}
				courseCategoryDto.setCourseList(courseList);
				courseCategoryDto.setCourseCombination(combinationList);
				courseCategoryDtoList.add(courseCategoryDto);
			}
		}
		// 封装科目组合
		for (CourseCategoryDto one : courseCategoryDtoList) {
			for (CourseCategoryDto sub : one.getCourseCombination()) {
				NewGkChoCategory tmp = newGkChoCategoryMap.get(sub.getId());
				sub.setCategoryName(tmp.getCategoryName());
				List<Course> courses = new ArrayList<>();
				for (NewGkChoRelation course : categoryIdToRelationMap.get(sub.getId())) {
					courses.add(courseMap.get(course.getObjectValue()));
				}
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
        newGkChoiceDto.setCourseCategoryDtoList(courseCategoryDtoList);
		
		newGkChoRelationList = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_02);
		List<ChoiceSubjectDto> banCourseList = new ArrayList<ChoiceSubjectDto>();
		ChoiceSubjectDto choiceSubjectDto;
		if(CollectionUtils.isNotEmpty(newGkChoRelationList)){
			for (int i = 0; i < newGkChoRelationList.size(); i++) {
				choiceSubjectDto = new ChoiceSubjectDto();
				String ids = newGkChoRelationList.get(i).getObjectValue();
				choiceSubjectDto.setIds(ids);
				String[] id = ids.split(",");
				String shortNames = "";
				for (String string : id) {
					if(courseMap.containsKey(string)){
						shortNames += courseMap.get(string).getShortName();
					}
				}
				choiceSubjectDto.setShortNames(shortNames);
				banCourseList.add(choiceSubjectDto);
			}	
		}
		
		newGkChoiceDto.setBanCourseList(banCourseList);
		
		newGkChoRelationList = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_03);
		List<ChoiceSubjectDto> recommendList = new ArrayList<ChoiceSubjectDto>();
		if(CollectionUtils.isNotEmpty(newGkChoRelationList)){
			for (int i = 0; i < newGkChoRelationList.size(); i++) {
				choiceSubjectDto = new ChoiceSubjectDto();
				String ids = newGkChoRelationList.get(i).getObjectValue();
				choiceSubjectDto.setIds(ids);
				String[] id = ids.split(",");
				String shortNames = "";
				for (String string : id) {
					if(courseMap.containsKey(string)){
						shortNames += courseMap.get(string).getShortName();
					}
				}
				choiceSubjectDto.setShortNames(shortNames);
				recommendList.add(choiceSubjectDto);
			}	
		}
		newGkChoiceDto.setRecommendList(recommendList);
		
		map.put("newGkChoiceDto", newGkChoiceDto);
		map.put("canChange", false);
		return "/newgkelective/choice/publishChoiceIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/choice/{choiceId}/saveName")
	@ControllerInfo(value="修改名称")
	public String saveChoiceName(@PathVariable String choiceId, String gradeId, String choiceName) {
		List<NewGkChoice> chs = newGkChoiceService.findListByGradeId(gradeId);

		if("create".equals(choiceId)) {
			for(NewGkChoice ch : chs) {
				if(ch.getChoiceName().equals(choiceName)) {
					return error("该名称已被其他记录使用！");
				}
			}
		}else {
			if(CollectionUtils.isNotEmpty(chs)) {
				NewGkChoice toCh = null;
				for(NewGkChoice ch : chs) {
					if(ch.getId().equals(choiceId)) {
						toCh = ch;
					} else if(ch.getChoiceName().equals(choiceName)) {
						return error("该名称已被其他记录使用！");
					}
				}
				if(toCh == null) {
					return error("选课记录不存在或已被删除！");
				}
				toCh.setChoiceName(choiceName);
				toCh.setModifyTime(new Date());
				newGkChoiceService.save(toCh);
			} else {
				return error("选课记录不存在或已被删除！");
			}
			
		}
		
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/choice/{choiceId}/setDefault")
	@ControllerInfo(value="置为默认")
	public String setDefault(@PathVariable String choiceId, String gradeId,String stat) {
		if("0".equals(stat)){
			//置为默认
			List<NewGkChoice> chs = newGkChoiceService.findListByGradeId(gradeId);
			if(CollectionUtils.isNotEmpty(chs)) {
				boolean hasDefault = false;
				List<NewGkChoice> toSave = new ArrayList<NewGkChoice>();
				for(NewGkChoice ch : chs) {
					if(ch.getIsDeleted() == 1
							|| (!StringUtils.equals(ch.getId(), choiceId) && ch.getIsDefault() == 0)) {
						continue;
					}
					if(StringUtils.equals(ch.getId(), choiceId)) {
						ch.setIsDefault(1);
						hasDefault = true;
					} else {
						ch.setIsDefault(0);
					}
					ch.setModifyTime(new Date());
					toSave.add(ch);
				}
				if (hasDefault && toSave.size() > 0) {
					newGkChoiceService.saveDefault(chs.toArray(new NewGkChoice[0]));
					return success("操作成功！");
				}
			}
			return error("没有可置为默认的选课记录！");
		}else if("1".equals(stat)){
			//直接取消默认
			NewGkChoice cc=newGkChoiceService.findById(choiceId);
			if(cc!=null){
				cc.setIsDefault(0);
				cc.setModifyTime(new Date());
				newGkChoiceService.saveDefault(new NewGkChoice[]{cc});
				return success("操作成功！");
			}
			return error("没有可取消默认的选课记录！");
		}else{
			return error("操作失败！");
		}
		
	}
	
	@RequestMapping("/choice/delete/page")
    @ControllerInfo(value = "删除界面")
	public String showDelete(HttpServletRequest request,ModelMap map){
		String choiceId = request.getParameter("choiceId");
		map.put("choiceId", choiceId);
		return "/newgkelective/choice/choiceDeleteIndex.ftl";
	}

	@ResponseBody
	@RequestMapping("/choice/delete")
	@ControllerInfo("删除")
	public String doDelete(String id,String verifyCode, HttpSession httpSession) {
		try{
//            String verifyCodeKey = DeskTopConstant.VERIFY_CODE_CACHE_KEY + httpSession.getId();
//            String sessionVerifyCode = StringUtils.trim(RedisUtils.get(verifyCodeKey));
//            if (StringUtils.isBlank(sessionVerifyCode)) {
//                return error("验证码已失效");
//            }
//            if (StringUtils.equalsIgnoreCase(sessionVerifyCode, verifyCode)) {
//                RedisUtils.del(verifyCodeKey);
//            } else {
//                return error("验证码错误");
//            }
            if(checkChoose(id)){
            	return error("数据已被使用，不能删除");	
            }
            //验证有没有被使用
            newGkChoiceService.deleteById(this.getLoginInfo().getUnitId(), id);
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	private boolean checkChoose(String id){
		List<NewGkDivide> newGkDivideList=newGkDivideService.findByChoiceId(id);
		if(CollectionUtils.isNotEmpty(newGkDivideList)){
			return true;
		}
		return false;
	}
	
	private List<NewGkChoRelation> getSaveGkChoRelations(
			NewGkChoiceDto newGkChoiceDto, String id, String choiceId,String unitId) {
		
		// 封装选课科目
		List<CourseCategoryDto> courseCategoryDtoList = newGkChoiceDto.getCourseCategoryDtoList();

		List<NewGkChoRelation> newGkChoRelations = new ArrayList<NewGkChoRelation>();
		for (CourseCategoryDto courseCategoryDto : courseCategoryDtoList) {
			if (courseCategoryDto.getCourseList() != null) {
				for (Course course : courseCategoryDto.getCourseList()) {
					NewGkChoRelation newGkChoRelation = new NewGkChoRelation();
					newGkChoRelation.setObjectType(NewGkElectiveConstant.CHOICE_TYPE_01);
					newGkChoRelation.setObjectTypeVal(courseCategoryDto.getId());
					newGkChoRelation.setObjectValue(course.getId());
					newGkChoRelation.setChoiceId(StringUtils.isBlank(choiceId) ? id : choiceId);
					newGkChoRelation.setId(UuidUtils.generateUuid());
					newGkChoRelation.setCreationTime(new Date());
					newGkChoRelation.setModifyTime(new Date());
					newGkChoRelation.setUnitId(unitId);
					newGkChoRelations.add(newGkChoRelation);
				}
			}
			if (courseCategoryDto.getCourseCombination() != null) {
                for (CourseCategoryDto combination : courseCategoryDto.getCourseCombination()) {
                    NewGkChoRelation combinationRelation = new NewGkChoRelation();
                    combinationRelation.setId(UuidUtils.generateUuid());
                    combinationRelation.setObjectType(NewGkElectiveConstant.CHOICE_TYPE_06);
                    combinationRelation.setObjectTypeVal(courseCategoryDto.getId());
                    combinationRelation.setObjectValue(combination.getId());
                    combinationRelation.setChoiceId(StringUtils.isBlank(choiceId) ? id : choiceId);
                    combinationRelation.setCreationTime(new Date());
                    combinationRelation.setModifyTime(new Date());
                    combinationRelation.setUnitId(unitId);
                    newGkChoRelations.add(combinationRelation);

                    // first subject
                    combinationRelation = new NewGkChoRelation();
                    combinationRelation.setId(UuidUtils.generateUuid());
                    combinationRelation.setObjectType(NewGkElectiveConstant.CHOICE_TYPE_01);
                    combinationRelation.setObjectTypeVal(combination.getId());
                    combinationRelation.setObjectValue(combination.getCourseList().get(0).getId());
                    combinationRelation.setChoiceId(StringUtils.isBlank(choiceId) ? id : choiceId);
                    combinationRelation.setCreationTime(new Date());
                    combinationRelation.setModifyTime(new Date());
                    combinationRelation.setUnitId(unitId);
                    newGkChoRelations.add(combinationRelation);

                    // second subject
                    combinationRelation = new NewGkChoRelation();
                    combinationRelation.setId(UuidUtils.generateUuid());
                    combinationRelation.setObjectType(NewGkElectiveConstant.CHOICE_TYPE_01);
                    combinationRelation.setObjectTypeVal(combination.getId());
                    combinationRelation.setObjectValue(combination.getCourseList().get(1).getId());
                    combinationRelation.setChoiceId(StringUtils.isBlank(choiceId) ? id : choiceId);
                    combinationRelation.setCreationTime(new Date());
                    combinationRelation.setModifyTime(new Date());
                    combinationRelation.setUnitId(unitId);
                    newGkChoRelations.add(combinationRelation);
                }
            }
        }
		// 封装禁选组合
		List<ChoiceSubjectDto> banCourseList = newGkChoiceDto
				.getBanCourseList();
		for (ChoiceSubjectDto choiceSubjectDto : banCourseList) {
			NewGkChoRelation newGkChoRelation = new NewGkChoRelation();
			newGkChoRelation
					.setObjectType(NewGkElectiveConstant.CHOICE_TYPE_02);
			newGkChoRelation.setObjectValue(choiceSubjectDto.getIds());
			if(StringUtils.isBlank(choiceId)) {
				newGkChoRelation.setChoiceId(id);
			}else {
				newGkChoRelation.setChoiceId(choiceId);
			}
			newGkChoRelation.setId(UuidUtils.generateUuid());
			newGkChoRelation.setCreationTime(new Date());
			newGkChoRelation.setModifyTime(new Date());
			newGkChoRelation.setUnitId(unitId);
			newGkChoRelations.add(newGkChoRelation);
		}
		// 封装推荐组合
		List<ChoiceSubjectDto> recommendList = newGkChoiceDto
				.getRecommendList();
		for (ChoiceSubjectDto choiceSubjectDto : recommendList) {
			NewGkChoRelation newGkChoRelation = new NewGkChoRelation();
			newGkChoRelation
					.setObjectType(NewGkElectiveConstant.CHOICE_TYPE_03);
			newGkChoRelation.setObjectValue(choiceSubjectDto.getIds());
			if(StringUtils.isBlank(choiceId)) {
				newGkChoRelation.setChoiceId(id);
			}else {
				newGkChoRelation.setChoiceId(choiceId);
			}
			newGkChoRelation.setId(UuidUtils.generateUuid());
			newGkChoRelation.setCreationTime(new Date());
			newGkChoRelation.setModifyTime(new Date());
			newGkChoRelation.setUnitId(unitId);
			newGkChoRelations.add(newGkChoRelation);
		}
		return newGkChoRelations;
	}

	private NewGkChoice getSaveNewGkChoice(NewGkChoiceDto newGkChoiceDto,
			String id, String choiceId, String unitId) {
		NewGkChoice newGkChoice = new NewGkChoice();
		
		if(StringUtils.isNotBlank(choiceId)) {
			//id不为空 创建时间不修改
			newGkChoice = newGkChoiceService.findById(choiceId);
			newGkChoice.setModifyTime(new Date());
			newGkChoice.setChoiceName(newGkChoiceDto.getChoiceName());
		}else {
			newGkChoice.setId(id);
			newGkChoice.setUnitId(unitId);
			newGkChoice.setGradeId(newGkChoiceDto.getGradeId());
			
			String choiceName = "";
			Grade grade = SUtils.dc(
					gradeRemoteService.findOneById(newGkChoiceDto.getGradeId()),
					Grade.class);
			Integer times = newGkChoiceService.getChoiceMaxTime(unitId,
					newGkChoiceDto.getGradeId());
			if (times == null) {
				times = 1;
			} else {
				if (choiceId.equals("")) {
					times = times + 1;
				}
			}
//			Semester currentSemester = semesterService.getCurrentSemester(0);
//			String acadyear = currentSemester.getAcadyear();
//			String[] strings = acadyear.split("-");
//			String name = strings[0] + "年" + grade.getGradeName() + "年级" + "第"
//					+ times + "次选课";
			
			if(StringUtils.isBlank(newGkChoiceDto.getChoiceName())) {
				
				String semesterJson = semesterRemoteService.getCurrentSemester(2, unitId);
				Semester semester = SUtils.dc(semesterJson, Semester.class);
				//NewGkDivide divide = newGkDivideService.findById(divide_id);
				//Grade grade = SUtils.dc(gradeRemoteService.findById(divide.getGradeId()), Grade.class);	
				choiceName = semester.getAcadyear()+"学年"+grade.getGradeName()+"第"+
						semester.getSemester()+"学期第"+times+"次选课";
			}else {
				choiceName = newGkChoiceDto.getChoiceName();
			}
			
			newGkChoice.setChoiceName(choiceName);
			newGkChoice.setTimes(times);
			newGkChoice.setCreationTime(new Date());
		}

		newGkChoice.setReferScoreId(newGkChoiceDto.getReferScoreId());
		newGkChoice.setModifyTime(new Date());
		newGkChoice.setStartTime(newGkChoiceDto.getStartTime());
		newGkChoice.setEndTime(newGkChoiceDto.getEndTime());

		newGkChoice.setNotice(newGkChoiceDto.getNotice());
		newGkChoice.setChooseNum(newGkChoiceDto.getChooseNum());
		newGkChoice.setStatShow(newGkChoiceDto.getStatShow());
		newGkChoice.setShowSamesele(newGkChoiceDto.getShowSamesele());
        if (Integer.valueOf(1).equals(newGkChoiceDto.getNoticeShow())) {
            newGkChoice.setShowNum(newGkChoiceDto.getShowNum());
            newGkChoice.setShowTime(newGkChoiceDto.getShowTime());
            newGkChoice.setHintContent(newGkChoiceDto.getHintContent());
        }
        newGkChoice.setIsDeleted(0);// 0未删除 1删除
		
		return newGkChoice;
	}

	public List<ChoiceSubjectDto> getChoiceSubjectDtoList(
			List<Course> courseList, Integer[][] result,String state,String choiceId) {
		List<ChoiceSubjectDto> choiceSubjectDtoList = new ArrayList<ChoiceSubjectDto>();
		List<NewGkChoRelation> newGkChoRelationList = new ArrayList<NewGkChoRelation>();
		
		if(choiceId != "") {
			Map<String, List<NewGkChoRelation>> NewGkChoRelationAllMap= newGkChoRelationService.findByChoiceIdAndObjectTypeIn(getLoginInfo().getUnitId(),choiceId,new String[]{NewGkElectiveConstant.CHOICE_TYPE_02,NewGkElectiveConstant.CHOICE_TYPE_03});
			if(state.equals("1")) {
				//推荐组合
				newGkChoRelationList = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_03);
			}else {
				//禁选组合
				newGkChoRelationList = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_02);
			}
		}
		for(int i = 0; i < result.length; i++) {
			ChoiceSubjectDto choiceSubjectDto = new ChoiceSubjectDto();
			String ids = "";
			String shortNames = "";
			for(int j = 0; j <result[i].length; j++) {
				if(j == 0) {
					ids += courseList.get(result[i][j]).getId();
				}else {
					ids = ids + ',' + courseList.get(result[i][j]).getId();
				}
				shortNames += courseList.get(result[i][j]).getShortName();
			}
			choiceSubjectDto.setState("0");
			choiceSubjectDto.setIds(ids);
			choiceSubjectDto.setShortNames(shortNames);
			if(CollectionUtils.isNotEmpty(newGkChoRelationList)){
				for (NewGkChoRelation newGkChoRelation : newGkChoRelationList) {
					if(newGkChoRelation.getObjectValue().equals(choiceSubjectDto.getIds())) {
						choiceSubjectDto.setState("1");
					}
				}
			}
			
			choiceSubjectDtoList.add(choiceSubjectDto);
		}
		return choiceSubjectDtoList;
	}

	public List<Course> splitStr(String str) {
		List<Course> courseList = new ArrayList<Course>();
		String[] strings = str.split("-");
		for (String string : strings) {
			String[] temp = string.split(",");
			Course course = new Course();
			course.setId(temp[0]);
			course.setShortName(temp[1]);
			courseList.add(course);
		}
		return courseList;
	}

	private List<CourseCategoryDto> getChoiceCourseDtoList(
			List<Course> newCourselist) {
		List<CourseCategoryDto> choiceCourseDtoList = new ArrayList<>();
		CourseCategoryDto courseCategoryDto = new CourseCategoryDto();
		courseCategoryDto.setCategoryName("选课科目");
		courseCategoryDto.setCourseList(newCourselist);
		choiceCourseDtoList.add(courseCategoryDto);
		return choiceCourseDtoList;
	}

}
