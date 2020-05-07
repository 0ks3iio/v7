package net.zdsoft.newgkelective.mobile.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.MobileAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ArrayUtil;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.WeiKeyUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.NewGkChoCategoryDto;
import net.zdsoft.newgkelective.data.dto.NewGkChoiceDto;
import net.zdsoft.newgkelective.data.entity.NewGkChoCategory;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;
import net.zdsoft.newgkelective.data.service.NewGkChoCategoryService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkChoiceService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import avro.shaded.com.google.common.base.Objects;

import com.alibaba.fastjson.TypeReference;

/**
 * 
 * @author weixh
 * @since 2018年4月8日 上午9:51:20
 */
@RequestMapping("/mobile/open/newgkelective")
@Controller
public class StuIndexAction extends MobileAction {
	@Autowired
	private NewGkChoiceService newGkChoiceService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private NewGkChoResultService newGkChoResultService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkChoCategoryService newGkChoCategoryService;
	
	@RequestMapping("/index")
	@ControllerInfo("跳转")
	public String index(String token, ModelMap map) {
		try {
			long s = System.currentTimeMillis();
			map.put("indexPage", true);
			if(StringUtils.isBlank(token)){
				return errorFtl(map, "token信息为空，请联系管理员");
			}
			String ownerId = WeiKeyUtils.decodeByDes(token);
			//String ownerId="dfff948011174ac784ea57163d77b276";
			return showHomepage(ownerId, map);
		} catch (Exception e) {
			e.printStackTrace();
			return errorFtl(map, "发送未知错误");
		}
	}
	
	public static void main(String[] args) {
		System.out.println(PWD.decode("LDRRUMNRFDFDQKCNJ4NNMR8LDAB4C7BCNRFLF8G8V9NV36CQZRX6GMXD9XNRUMYP"));
	}
	/**
	 * 首页
	 */
	@RequestMapping("/homepage")
    @ControllerInfo("首页")
	public String showHomepage(String studentId, ModelMap map){
		try {
			map.put("indexPage", true);
			Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
			if(student==null){
				return errorFtl(map, "找不到用户信息，请联系管理员");
			}
			if(student.getIsDeleted() != null && student.getIsDeleted() == 1) {
				return errorFtl(map, "该学生已被删除！");
			}
			if(student.getIsLeaveSchool() != null && student.getIsLeaveSchool() == 1) {
				return errorFtl(map, "该学生已离校！");
			}
			Clazz cls = Clazz.dc(classRemoteService.findOneById(student.getClassId()));
			if(cls == null || cls.getIsDeleted() == 1) {
				return errorFtl(map, "该学生所在班级不存在或已被删除！");
			}
			
			// 缓存
			List<NewGkChoice> chs =  RedisUtils.getObject("NEW_GK_CHOICE_GRADE_LIST"+cls.getGradeId(), RedisUtils.TIME_TEN_MINUTES, new TypeReference<List<NewGkChoice>>(){}, new RedisInterface<List<NewGkChoice>>(){

				@Override
				public List<NewGkChoice> queryData() {
					return newGkChoiceService.findListByGradeId(cls.getGradeId());
				}
			});
			if(CollectionUtils.isEmpty(chs)) {
				return errorFtl(map, "学校还未发布选课！");
			}
			List<String> cids = EntityUtils.getList(chs, NewGkChoice::getId);
			List<NewGkChoiceDto> chDtos = new ArrayList<NewGkChoiceDto>();
			Map<String, String> limitMap =  RedisUtils.getObject("NEW_GK_CHORELATION_STU_UNPARTIN_"+studentId, RedisUtils.TIME_TEN_MINUTES, new TypeReference<Map<String, String>>(){}, new RedisInterface<Map<String, String>>(){

				@Override
				public Map<String, String> queryData() {
					return newGkChoRelationService.findByChoiceIdsAndObjectTypeAndObjVal(student.getSchoolId(), 
							cids.toArray(new String[0]), NewGkElectiveConstant.CHOICE_TYPE_04, studentId);
				}
			});
			boolean hasLimit = MapUtils.isNotEmpty(limitMap);
			NewGkChoiceDto dto;
			Date now = new Date();
			for(NewGkChoice ch : chs) {
				dto = new NewGkChoiceDto();
				dto.setChoiceName(ch.getChoiceName());
				dto.setChoiceId(ch.getId());
				dto.setStartTime(ch.getStartTime());
				dto.setEndTime(ch.getEndTime());
				if(Constant.IS_TRUE==ch.getIsDefault()){
					dto.setTimeState(4);// 已采用
				}else{
					int sn = DateUtils.compareIgnoreSecond(ch.getStartTime(), now); 
					if(sn > 0 || DateUtils.compareIgnoreSecond(ch.getEndTime(), now)<0) {
						if(sn>0) {
							dto.setTimeState(0);// 未开始
						} else {
							dto.setTimeState(2);// 已过期
						}
					} else {
						dto.setTimeState(1);// 进行中
					}
				}
				dto.setWarning(false);
				if(hasLimit && limitMap.containsKey(ch.getId())) {
					dto.setWarning(true);
				}
				chDtos.add(dto);
				dto = null;
			}
			
			map.put("chDtos", chDtos);
			map.put("studentId", student.getId());
			map.put("stuName", student.getStudentName());
			map.put("unitId", student.getSchoolId());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "/newgkelective/mobile/index.ftl";
	}
	
	@RequestMapping("/{studentId}/choice/page")
	@ControllerInfo("学生选课页面")
	public String stuChoosePage(@PathVariable String studentId, String choiceId, String isMaster, String subjectIds, HttpServletRequest request, ModelMap map) {
		NewGkChoice newGkChoice = newGkChoiceService.findOne(choiceId);
		if(newGkChoice == null || (newGkChoice.getIsDeleted() != null && newGkChoice.getIsDeleted() == 1)) {
			return "redirect:/mobile/open/newgkelective/homepage?studentId="+studentId;
		}
		
		List<NewGkChoRelation> limitStudentList = newGkChoRelationService
				.findByChoiceIdAndObjectTypeAndObjectValueIn(newGkChoice.getUnitId(), choiceId,
						NewGkElectiveConstant.CHOICE_TYPE_04, new String[] { studentId });
		if(CollectionUtils.isNotEmpty(limitStudentList)) {
			return "redirect:/mobile/open/newgkelective/homepage?studentId="+studentId;
		}
		
		int toEdit = NumberUtils.toInt(request.getParameter("toEdit"));
		
		Date now = new Date();
		int sn = DateUtils.compareIgnoreSecond(newGkChoice.getStartTime(), now); 
		int timeState = 0;
		if(sn > 0 || DateUtils.compareIgnoreSecond(newGkChoice.getEndTime(), now)<0) {
			if(sn>0) {
				timeState = 0;// 未开始
			} else {
				timeState = 2;// 已过期
			}
		} else {
			timeState = 1;// 进行中
		}
		
		//选课信息
		map.put("timeState", timeState);
		map.put("studentId", studentId);
		map.put("choiceId", choiceId); 
		map.put("choice", newGkChoice);
		
	
		
		
		
		//是否提示
		Map<String, List<NewGkChoRelation>> newGkChoRelationAllMap = RedisUtils.getObject("NEW_GK_CHORELATION_CHO_OBJTYPES_"+choiceId, RedisUtils.TIME_TEN_MINUTES, new TypeReference<Map<String, List<NewGkChoRelation>>>(){},new RedisInterface<Map<String, List<NewGkChoRelation>>>() {
			
			@Override
			public Map<String, List<NewGkChoRelation>> queryData() {
				return newGkChoRelationService.findByChoiceIdAndObjectTypeIn(newGkChoice.getUnitId(),choiceId, new String[]{NewGkElectiveConstant.CHOICE_TYPE_01,
					NewGkElectiveConstant.CHOICE_TYPE_02, NewGkElectiveConstant.CHOICE_TYPE_03,NewGkElectiveConstant.CHOICE_TYPE_06});
			}
		});
		
		List<NewGkChoRelation> newGkChoRelationList = newGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01);
		List<String> courseIds = EntityUtils.getList(newGkChoRelationList, NewGkChoRelation::getObjectValue);
		
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[] {})),new TR<List<Course>>() {});
		if(CollectionUtils.isEmpty(courseList)) {
			return errorFtl(map, "该选课科目数据被调整，未找到！");
		}
		Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId,Course::getSubjectName);
		Map<String, String> courseShortNameMap = EntityUtils.getMap(courseList,Course::getId, Course::getShortName);
		map.put("courseNameMap", courseNameMap);
		
		if(newGkChoice.getShowNum()==null || newGkChoice.getShowTime()==null){
			map.put("isTips",false);
		}else{
			Date nowDate = new Date();
			if(DateUtils.compareIgnoreSecond(newGkChoice.getShowTime(), nowDate)>0){
				map.put("isTips",false);
			}else{
				//三科目组合选课结果
//				List<NewGkChoRelation> newGkChoRelationList = newGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01);
//				List<String> courseIds = EntityUtils.getList(newGkChoRelationList, NewGkChoRelation::getObjectValue);
				if(CollectionUtils.isNotEmpty(courseIds)) {
					String jsonStringData = newGkChoResultService.getCount(newGkChoice.getUnitId(),choiceId, courseIds, false);
					map.put("jsonStringData", jsonStringData);
				}
				map.put("isTips",true);
				map.put("showNum", newGkChoice.getShowNum());
				map.put("hintContent", newGkChoice.getHintContent());
			}
		}
		
		//已选课程
		Map<String,List<NewGkChoResult>> resultMap;
		if(Constant.IS_TRUE_Str.equals(isMaster)){
			resultMap = newGkChoResultService.findMapByChoiceIdAndStudentIdWithMaster(newGkChoice.getUnitId(),new String[]{NewGkElectiveConstant.KIND_TYPE_01,NewGkElectiveConstant.KIND_TYPE_02,NewGkElectiveConstant.KIND_TYPE_03}, newGkChoice.getId(), studentId);
		}else{
			resultMap = newGkChoResultService.findMapByChoiceIdAndStudentId(newGkChoice.getUnitId(),new String[]{NewGkElectiveConstant.KIND_TYPE_01,NewGkElectiveConstant.KIND_TYPE_02,NewGkElectiveConstant.KIND_TYPE_03}, newGkChoice.getId(), studentId);
		}
		List<NewGkChoResult> newGkChoResultList = resultMap.get(NewGkElectiveConstant.KIND_TYPE_01);
		boolean hasChoose = CollectionUtils.isNotEmpty(newGkChoResultList);
		if(StringUtils.isBlank(subjectIds)){//重选或者第一次进入
			if(hasChoose) {
				
				List<String> subIds = EntityUtils.getList(newGkChoResultList, NewGkChoResult::getSubjectId);
				Collections.sort(subIds);
				map.put("resultIds", StringUtils.join(subIds, ","));
				
				List<NewGkChoResult> wantToSubjectList = resultMap.get(NewGkElectiveConstant.KIND_TYPE_02);
				if(CollectionUtils.isNotEmpty(wantToSubjectList)){
					map.put("wantToSubjectList", wantToSubjectList);
				}
				
				List<NewGkChoResult> noWantToSubjectList = resultMap.get(NewGkElectiveConstant.KIND_TYPE_03);
				if(CollectionUtils.isNotEmpty(noWantToSubjectList)){
					map.put("noWantToSubjectList", noWantToSubjectList);
				}
				map.put("chooseNum", newGkChoResultList.size());
			}
			map.put("resultList", newGkChoResultList);
			
			boolean toDetail = hasChoose && toEdit == 0;
			if(toDetail) {
				return "/newgkelective/mobile/stuResultDetail.ftl";
			}
		}else{//上一步
			List<NewGkChoResult> resultList = new ArrayList<NewGkChoResult>();
			NewGkChoResult result;
			
			String[] resultIds = subjectIds.split(",");
			for (String resultId : resultIds) {
				result = new NewGkChoResult();
				result.setSubjectId(resultId);
				result.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_1);
				resultList.add(result);
			}
			
			map.put("resultList", resultList);
			if(hasChoose){
				map.put("chooseNum", newGkChoResultList.size());
			}
		}
		
		//是否公开
		if(newGkChoice.getStatShow()!=null && Constant.IS_TRUE==newGkChoice.getStatShow()){
			map.put("isOpen", true);
			//三科目组合选课结果
//			List<NewGkChoRelation> newGkChoRelationList = newGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01);
//			List<String> courseIds = EntityUtils.getList(newGkChoRelationList, NewGkChoRelation::getObjectValue);
			if(CollectionUtils.isNotEmpty(courseIds)) {
				String jsonStringData = newGkChoResultService.getCount(newGkChoice.getUnitId(),choiceId, courseIds, false);
				map.put("jsonStringData", jsonStringData);
			}
		}else{
			map.put("isOpen", false);
		}
		
		List<NewGkChoCategoryDto> categoryDtoList=new ArrayList<>();
		
		List<NewGkChoRelation> newGkChoRelationList6 = newGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_06);
		Map<String, List<String>> zhMap =new HashMap<>();
		//key:ChoCategoryId
		Map<String, List<String>> categoryMap = EntityUtils.getListMap(newGkChoRelationList, NewGkChoRelation::getObjectTypeVal, NewGkChoRelation::getObjectValue);
		Set<String> zhId=new HashSet<>();
		if(CollectionUtils.isNotEmpty(newGkChoRelationList6)) {
			zhId=EntityUtils.getSet(newGkChoRelationList6, NewGkChoRelation::getObjectValue);
			zhMap = EntityUtils.getListMap(newGkChoRelationList6, NewGkChoRelation::getObjectTypeVal, NewGkChoRelation::getObjectValue);
		}
		//全部封装类别和合并名称
		List<NewGkChoCategory> categoryList = newGkChoCategoryService.findByChoiceId(newGkChoice.getUnitId(), newGkChoice.getId());
		NewGkChoCategoryDto dto=null;
		for(NewGkChoCategory item:categoryList) {
			if(zhId.contains(item.getId())) {
				continue;
			}
			dto=new NewGkChoCategoryDto();
			dto.setCategoryName(item.getCategoryName());
			dto.setMaxNum(item.getMaxNum()==null?0:item.getMaxNum());
			dto.setMinNum(item.getMinNum()==null?0:item.getMinNum());
			Map<String, String> choNameMap=new HashMap<>();
			dto.setChoNameMap(choNameMap);
			List<String> zhIdIds = zhMap.get(item.getId());
			if(CollectionUtils.isNotEmpty(zhIdIds)) {
				for(String s:zhIdIds) {
					List<String> list1 = categoryMap.get(s);
					if(CollectionUtils.isNotEmpty(list1)) {
						String[] ids = list1.toArray(new String[] {});
						Arrays.sort(ids);
						String key=ArrayUtil.print(ids);
						String groupName="";
						for (String id : ids) {
							String objectName = courseNameMap.get(id);
							groupName += (objectName+",");
						}
						choNameMap.put(key, groupName.substring(0,groupName.length()-1));
					}
				}
			}
			List<String> list2 = categoryMap.get(item.getId());
			if(CollectionUtils.isNotEmpty(list2)) {
				for(String s:list2) {
					choNameMap.put(s, courseNameMap.get(s));
				}
				
			}
			if(choNameMap.size()>0) {
				categoryDtoList.add(dto);
			}
			
		}
		
		
		//map.put("categoryList", categoryList);
		map.put("categoryDtoList", categoryDtoList);
		
		Map<String, String> codeNameMap = EntityUtils.getMap(courseList, Course::getSubjectCode, Course::getSubjectName);
		List<String[]> codeNames = new ArrayList<String[]>();
		for(String code : BaseConstants.SUBJECT_73) {
			if(codeNameMap.containsKey(code)) {
				codeNames.add(new String[] {codeNameMap.get(code), BaseConstants.SUBJECT_CODE_CLASSNAME.get(code)});
			}
		}
		map.put("codeNames", codeNames);
		
		
		//推荐选课
		List<NewGkChoRelation> recommendChoReList = newGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_03);
		if(CollectionUtils.isNotEmpty(recommendChoReList)){
			List<String> recommendValueList = EntityUtils.getList(recommendChoReList, NewGkChoRelation::getObjectValue);
			if(CollectionUtils.isNotEmpty(recommendValueList)){
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

		return "/newgkelective/mobile/stuResultChoose1.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/{studentId}/choice/check")
	@ControllerInfo("学生选课第二步校验")
	public String stuChooseNextCheck(@PathVariable String studentId, String choiceId, String subjectIds, ModelMap map) {
		if(StringUtils.isBlank(subjectIds)||StringUtil.isBlank(choiceId)){
			return error("参数为空");
		}
		String[] subjectIdsArr = subjectIds.split(",");
		List<String> subjectIdsList = Arrays.asList(subjectIdsArr);
        try {
        	Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
			//判断是否被锁定
			List<String> lockChoList = newGkChoRelationService.findByChoiceIdAndObjectType(student.getSchoolId(),choiceId, NewGkElectiveConstant.CHOICE_TYPE_05);
			if(CollectionUtils.isNotEmpty(lockChoList)&&lockChoList.contains(studentId)){
				return error("您的选课结果已被管理员锁定！");
			}
			//判断限选
			List<String> limitChoList = newGkChoRelationService.findByChoiceIdAndObjectType(student.getSchoolId(),choiceId, NewGkElectiveConstant.CHOICE_TYPE_02);
			for (String limitSubject : limitChoList) {
				List<String> limitSubjectList = Arrays.asList(limitSubject.split(","));
				if (limitSubjectList.containsAll(subjectIdsList)) {
					 return error("您选择的是不推荐组合，请调整！");
				}
			}
        }catch (Exception e) {
        	e.printStackTrace();
            return error("保存失败！");
        }
        return success();
	}
	
	@RequestMapping("/{studentId}/choice/next/page")
	@ControllerInfo("学生选课第二步页面")
	public String stuChooseNextPage(@PathVariable String studentId, String choiceId, String subjectIds, ModelMap map) {
		NewGkChoice newGkChoice = newGkChoiceService.findOne(choiceId);
		
		map.put("studentId", studentId);
		map.put("choiceId", choiceId); 
		map.put("choice", newGkChoice);
		
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(newGkChoice.getUnitId()),new TR<List<Course>>() {});
		Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId,Course::getSubjectName);
		map.put("courseNameMap", courseNameMap);
		
		List<String> resultIds = Arrays.stream(subjectIds.split(",")).collect(Collectors.toList());
		
		Map<String, List<NewGkChoRelation>> newGkChoRelationAllMap = RedisUtils.getObject("NEW_GK_CHORELATION_CHO_OBJTYPES_"+choiceId, RedisUtils.TIME_TEN_MINUTES, new TypeReference<Map<String, List<NewGkChoRelation>>>(){},new RedisInterface<Map<String, List<NewGkChoRelation>>>() {

			@Override
			public Map<String, List<NewGkChoRelation>> queryData() {
				return newGkChoRelationService.findByChoiceIdAndObjectTypeIn(newGkChoice.getUnitId(),choiceId, new String[]{NewGkElectiveConstant.CHOICE_TYPE_01,
						NewGkElectiveConstant.CHOICE_TYPE_02, NewGkElectiveConstant.CHOICE_TYPE_03});
			}
		});
		
		//调剂科目选择
		List<NewGkChoRelation> courseChoReList = newGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01);
		Set<String> courseChoList = EntityUtils.getSet(courseChoReList, NewGkChoRelation::getObjectValue);
		courseChoList.removeAll(resultIds);
		map.put("courseChoList", courseChoList);
		
		//已选课程
		Map<String,List<NewGkChoResult>> resultMap = newGkChoResultService.findMapByChoiceIdAndStudentId(newGkChoice.getUnitId(),new String[]{NewGkElectiveConstant.KIND_TYPE_01,NewGkElectiveConstant.KIND_TYPE_02,NewGkElectiveConstant.KIND_TYPE_03}, newGkChoice.getId(), studentId);
		List<NewGkChoResult> newGkChoResultList = resultMap.get(NewGkElectiveConstant.KIND_TYPE_01);
		int size = 0;
		if(CollectionUtils.isNotEmpty(newGkChoResultList)){
			List<String> subIds = EntityUtils.getList(newGkChoResultList, NewGkChoResult::getSubjectId);
			size = CollectionUtils.intersection(subIds, resultIds).size();
			map.put("chooseNum", newGkChoResultList.size());
		}
		
		if(CollectionUtils.isNotEmpty(newGkChoResultList) && Objects.equal(size, newGkChoice.getChooseNum())){
			map.put("resultList", newGkChoResultList);
			List<NewGkChoResult> wantToSubjectList = resultMap.get(NewGkElectiveConstant.KIND_TYPE_02);
			if(CollectionUtils.isNotEmpty(wantToSubjectList)){
				map.put("wantToSubjectList", wantToSubjectList);
			}
			
			List<NewGkChoResult> noWantToSubjectList = resultMap.get(NewGkElectiveConstant.KIND_TYPE_03);
			if(CollectionUtils.isNotEmpty(noWantToSubjectList)){
				map.put("noWantToSubjectList", noWantToSubjectList);
			}
		}else{
			List<NewGkChoResult> resultList = new ArrayList<NewGkChoResult>();
			NewGkChoResult result;
			for (String resultId : resultIds) {
				result = new NewGkChoResult();
				result.setSubjectId(resultId);
				result.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_1);
				resultList.add(result);
			}
			map.put("resultList", resultList);
		}
		
		//是否公开
		if(newGkChoice.getStatShow()!=null && Constant.IS_TRUE==newGkChoice.getStatShow()){
			map.put("isOpen", true);
			//三科目组合选课结果
			List<NewGkChoRelation> newGkChoRelationList = newGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01);
			List<String> courseIds = EntityUtils.getList(newGkChoRelationList, NewGkChoRelation::getObjectValue);
			if(CollectionUtils.isNotEmpty(courseIds)) {
				String jsonStringData = newGkChoResultService.getCount(newGkChoice.getUnitId(),choiceId, courseIds, false);
				map.put("jsonStringData", jsonStringData);
			}
		}else{
			map.put("isOpen", false);
		}

		return "/newgkelective/mobile/stuResultChoose2.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/{studentId}/choice/save")
	@ControllerInfo("学生选课保存")
	public String saveChoose(@PathVariable String studentId, String subjectIds,String subjectTypes,String wantToIds,String noWantToIds, String choiceId) {
		if(StringUtils.isBlank(subjectIds)||StringUtil.isBlank(choiceId)){
			return error("参数为空");
		}
		//判断时间
    	NewGkChoice newGkChoice = newGkChoiceService.findOne(choiceId);
		Date now = new Date();
		if (DateUtils.compareIgnoreSecond(newGkChoice.getEndTime(), now)<0) {
			return error("已超过选课截止时间！");
		}
		String[] subjectIdsArr = subjectIds.split(",");
		List<String> subjectIdsList = Arrays.asList(subjectIdsArr);
        try {
        	Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        	
        	String[] subjectTypesArr = subjectTypes.split(",");
        	String[] wantToIdsArr = null;
			String[] noWantToIdsArr = null;
			if(StringUtils.isNotBlank(wantToIds)){
				wantToIdsArr = wantToIds.split(",");
			}
			if(StringUtils.isNotBlank(noWantToIds)){
				noWantToIdsArr = noWantToIds.split(",");
			}
			
            newGkChoResultService.saveNewGkChoResult(student.getSchoolId(),choiceId,studentId,subjectIdsList,subjectTypesArr,wantToIdsArr,noWantToIdsArr);
        }
        catch (Exception e) {
        	e.printStackTrace();
            return error("保存失败！");
        }
        return success();
	}
	
}
