package net.zdsoft.newgkelective.data.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ColumnInfoUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.SortUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.BatchClassDto;
import net.zdsoft.newgkelective.data.dto.DivideResultDto;
import net.zdsoft.newgkelective.data.dto.NewGkDivideExDto;
import net.zdsoft.newgkelective.data.dto.NewGkGroupDto;
import net.zdsoft.newgkelective.data.dto.StudentResultDto;
import net.zdsoft.newgkelective.data.entity.NewGKStudentRange;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;
import net.zdsoft.newgkelective.data.entity.NewGkClassBatch;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkDivideEx;
import net.zdsoft.newgkelective.data.entity.NewGkOpenSubject;
import net.zdsoft.newgkelective.data.entity.NewGkScoreResult;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectGroupColor;
import net.zdsoft.newgkelective.data.optaplanner.bestsectioncount.api.BestSectionCount;
import net.zdsoft.newgkelective.data.optaplanner.bestsectioncount.api.BestSectionCountInput;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectioning.dto.ChooseStudent;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectioning.dto.GroupResult;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectioning.dto.RequiredParm;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectioning.dto.ThreeGroup;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectioning.dto.TwoGroup;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectioning.solve.SolveTwoSubject;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning2.api.Sectioning2A1XInput;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning2.domain1.SectioningApp2;
import net.zdsoft.newgkelective.data.optaplanner.common.CalculateSections;
import net.zdsoft.newgkelective.data.utils.CombineAlgorithmInt;
import net.zdsoft.system.entity.mcode.McodeDetail;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

/**
 * 分班结果,文理，手动分班等
 */
@Controller
@RequestMapping("/newgkelective/{divideId}")
public class NewGkElectiveDivideItemAction extends
		NewGkElectiveDivideCommonAction {

	private static Logger logger = LoggerFactory.getLogger(NewGkElectiveDivideItemAction.class);
	/** 以下分班结果 **/


	@RequestMapping("/divideClass/resultClassList")
	@ControllerInfo(value = "进入分班方案")
	public String resultClassList(@PathVariable String divideId, String type,
			String groupType, HttpServletRequest request, ModelMap map) {
		
		NewGkDivide divide = newGkDivideService.findOneWithMaster(divideId);
		if (divide == null) {
			return errorFtl(map, "分班方案不存在");
		}
		// 返回到排课编辑页面用的参数
		String fromArray = request.getParameter("fromArray");
		String fromSolve = request.getParameter("fromSolve");
		String arrayId = request.getParameter("arrayId");
        map.put("lessArrayId", StringUtils.trimToEmpty(request.getParameter("lessArrayId")));
		map.put("plArrayId", StringUtils.trimToEmpty(request.getParameter("plArrayId")));
		map.put("fromArray", fromArray);
		map.put("fromSolve", fromSolve);
		map.put("arrayId", arrayId);
		NewGkChoice choice = newGkChoiceService.findOne(divide.getChoiceId());
		if (choice == null) {
			return errorFtl(map, "分班方案对应的选课不存在");
		}
		map.put("divide", divide);
		//分班完成
		if (NewGkElectiveConstant.IF_1.equals(divide.getStat())) {
			//进入结果页
			if(NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType()) ||
					NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType())){
				//3+1+2固定
				return showDivideComResultIndex(divide,type,map);
			}else {
				//单科分层 //全固定模式   半固定模式  全手动模式
				return showDivideRangeResultIndex(divide,type,map);
			}
		}else {
			boolean isCanEdit = true;//是否显示可以编辑
			if (isNowDivide(divideId)) {
				//正在分班中
				isCanEdit = false;
			}
//			if (NewGkElectiveConstant.DIVIDE_TYPE_03.equals(divide.getOpenType())
//					|| NewGkElectiveConstant.DIVIDE_TYPE_04.equals(divide
//							.getOpenType())) {
//				//文理
//				map.put("isCanEdit", isCanEdit);
//				return showWenliPartList(choice, divide, map);
//			}else 
			if(NewGkElectiveConstant.DIVIDE_TYPE_05.equals(divide.getOpenType())) {
				//单科分层
				map.put("isCanEdit", isCanEdit);
				return showDivideRangeIndex(choice, divide, map);
			}else {
				//全固定模式   半固定模式  全手动模式
				boolean isAutoTwo=false;//自动组2+x 是否在分班中 
				if(NewGkElectiveConstant.DIVIDE_TYPE_01.equals(divide.getOpenType())) {
					//全固定
					// 开班中，未开班 进入自动调整页面
					if(checkAutoTwo(divideId)){
						//正在智能分班中
						isAutoTwo=true;
						isCanEdit=false;
					}
				}
//				if(NewGkElectiveConstant.DIVIDE_TYPE_02.equals(divide.getOpenType())
//						|| NewGkElectiveConstant.DIVIDE_TYPE_06.equals(divide.getOpenType())) {
//					
//				}
				if(isCanEdit) {
					//手动安排
					//存在教学班就不能修改
					List<NewGkDivideClass> jxbList = newGkDivideClassService
							.findByDivideIdAndClassType(divide.getUnitId(),
									divideId,
									new String[] { NewGkElectiveConstant.CLASS_TYPE_2 }, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
					if(CollectionUtils.isNotEmpty(jxbList)) {
						isCanEdit=false;
					}
				}
				// 半固定模式  全手动模式--暂不开放自动生成2+x
				map.put("isAutoTwo", isAutoTwo);
				map.put("isCanEdit", isCanEdit);
				return showArrangeList(choice, divide, map);
			}
		}
	}
	
	@ResponseBody
	@RequestMapping("/divideGroup/checkByDivideIdCanEdit")
	@ControllerInfo(value = "验证是否可操作功能")
	public String checkByDivideIdCanEdit(@PathVariable String divideId) {
		NewGkDivide divide = newGkDivideService.findById(divideId);
		if (NewGkElectiveConstant.IF_1.equals(divide.getStat())) {
			return success("分班已完成");
		}
		if (isNowDivide(divideId)) {
			//正在分班中
			return error("正在分班中，不能操作");
		}
		if(NewGkElectiveConstant.DIVIDE_TYPE_01.equals(divide.getOpenType())) {
			if(checkAutoTwo(divideId)){
				//正在智能分班中
				return error("正在智能分班中，不能操作");
			}
		}
		//如果有教学班
		//这个地方可能存在老数据，原因是之前一个分班方案多个线程
		if(NewGkElectiveConstant.DIVIDE_TYPE_01.equals(divide.getOpenType()) || 
				NewGkElectiveConstant.DIVIDE_TYPE_06.equals(divide.getOpenType()) ) {
			List<NewGkDivideClass> oldjxbList = newGkDivideClassService
					.findByDivideIdAndClassType(divide.getUnitId(),
							divideId,
							new String[] { NewGkElectiveConstant.CLASS_TYPE_2 }, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			if(CollectionUtils.isNotEmpty(oldjxbList)) {
				return error("已经安排教学班数据，不能操作");
			}
		}
		
		return success("");
	}
	
	
	private String showDivideRangeResultIndex(NewGkDivide divide, String type, ModelMap map) {
		map.put("onlyJxb", false);
		map.put("showOther", false);
		map.put("showXZB", false);
		if(!(NewGkElectiveConstant.DIVIDE_TYPE_05.equals(divide.getOpenType()) || NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divide.getOpenType()))) {
			map.put("showXZB", true);
		}
		map.put("type", type);
		map.put("dividiId", divide.getId());
		map.put("gradeId", divide.getGradeId());
		return "/newgkelective/divide/divideResultIndex.ftl";
	}

    /**
     * 删除无学生的教学班
     */
    @RequestMapping("/showDivideResult/deleteEmptyTeachClass")
    @ResponseBody
    public String deleteEmptyTeachClass(@PathVariable String divideId, String classId) {
        try {
            if (classId == null) {
                return error("未选择班级");
            }
            newGkDivideClassService.deleteByClassIdIn(getLoginInfo().getUnitId(), divideId, new String[]{classId});
        } catch (Exception e) {
            e.printStackTrace();
            return error("删除失败");
        }
        return success("删除成功");
    }

	/**
	 * 展示 除了文理分班以外的分班结果 
	 * @return
	 */
	@RequestMapping("/showDivideResult/page")
	public String showDivideResultNavigation(@PathVariable String divideId, String arrayId, String fromSolve, String type, ModelMap map) {
		NewGkDivide divide = newGkDivideService.findById(divideId);

		map.put("fromSolve", fromSolve);
		map.put("divideId", divideId);
		map.put("arrayId", arrayId);
		map.put("type", type);
		if(NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType()) || NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType())){
			return showDivideResult2(divide, fromSolve, arrayId, type, map);
		}
		if("count".equals(type)) {
			// 显示 班级 结果统计信息
			newGkDivideService.divideResultCount(divide, fromSolve, arrayId, map, null);
			return "/newgkelective/divide/divideClassCountList.ftl";
		}else if ("X".equals(type) || "Y".equals(type)) {
			// 行政班结果
			newGkDivideService.showDivideXzbList(divide, fromSolve, arrayId, map);
			return "/newgkelective/divide/divideXzbResultList.ftl";
		}
		
		if(NewGkElectiveConstant.DIVIDE_TYPE_05.equals(divide.getOpenType())
			|| NewGkElectiveConstant.DIVIDE_TYPE_09.equals(divide.getOpenType()) || NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divide.getOpenType())
				) {
			// 全走班单科分层
			return showDivideRangeResult(divide, fromSolve, arrayId, type, map);
		}else if(NewGkElectiveConstant.DIVIDE_TYPE_01.equals(divide.getOpenType())
					|| NewGkElectiveConstant.DIVIDE_TYPE_02.equals(divide.getOpenType())
					|| NewGkElectiveConstant.DIVIDE_TYPE_06.equals(divide.getOpenType())
					|| NewGkElectiveConstant.DIVIDE_TYPE_08.equals(divide.getOpenType())) {
			// 普通7选三
			return showDivideResult(divide, fromSolve, arrayId, type, map);
		}else {
			//不可能
			return errorFtl(map, "分班类型错误");
//			return null;
		}
	}
	
	@RequestMapping("/showDivideCountResult/page")
	public String showDivideCountResultHead(@PathVariable String divideId, String arrayId, String fromSolve, ModelMap map) {
		// 显示教学班学生组成统计信息
		NewGkDivide divide = newGkDivideService.findById(divideId);
		newGkDivideService.divideResultCount(divide, fromSolve, arrayId, map, null);
		List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService.findByDivideId(divideId);
		Set<String> subjectIds = EntityUtils.getSet(openSubjectList, NewGkOpenSubject::getSubjectId);
		LinkedHashMap<String, String> coureNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<LinkedHashMap<String, String>>(){});
		map.put("divideId", divideId);
		map.put("arrayId", arrayId);
		map.put("fromSolve", fromSolve);
		map.put("courseNameMap", coureNameMap);
		return "/newgkelective/divide/divideClassCountHead.ftl";
	}
	
	@RequestMapping("/showDivideCountResult/list/page")
	public String showDivideCountResultList(@PathVariable String divideId, String arrayId, String fromSolve, String subjectId, ModelMap map) {
		// 显示教学班学生组成统计信息
		NewGkDivide divide = newGkDivideService.findById(divideId);
		newGkDivideService.divideJxbResultCount(divide, fromSolve, arrayId, subjectId, map);
		return "/newgkelective/divide/divideClassCountList.ftl";
	}
	
	/**
	 * 普通7选三 情况的 分班结果
	 * @param divide
	 * @param type
	 * @param map
	 * @return
	 */
	private String showDivideResult(NewGkDivide divide, String fromSolve, String arrayId, String type, ModelMap map) {
		map.put("type", type);
		if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(type)
				|| NewGkElectiveConstant.SUBJECT_TYPE_B.equals(type)) {
			int bathNum=3;
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(type)) {
				bathNum=divide.getBatchCountTypea()==null?3:divide.getBatchCountTypea();
			}else {
				bathNum=divide.getBatchCountTypea()==null?3:divide.getBatchCountTypeb();
			}
			
			map.put("bathNum", bathNum);
			newGkDivideService.divideResultCount(divide, fromSolve, arrayId, map, type);
			return "/newgkelective/divide/divideJxbResultList2.ftl";
		}
		return null;
	}


	/**
	 * 全走班单科分层 分班结果
	 * @param divide
	 * @param type
	 * @param map
	 * @return
	 */
	private String showDivideRangeResult(NewGkDivide divide, String fromSolve, String arrayId, String type, ModelMap map) {
		if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(type)
				|| NewGkElectiveConstant.SUBJECT_TYPE_B.equals(type)
				|| "C".equals(type)
				|| "O".equals(type)) {
            List<NewGkDivideClass> jxbClassList;
            List<String> bathList=new ArrayList<>();
            if ("1".equals(fromSolve)) {
                jxbClassList = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), arrayId,
                        new String[] {NewGkElectiveConstant.CLASS_TYPE_2}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
            } else {
                jxbClassList = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divide.getId(),
                        new String[] {NewGkElectiveConstant.CLASS_TYPE_2}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
            }
            jxbClassList = jxbClassList.stream().filter(e->StringUtils.isNotBlank(e.getBatch())).filter(e->type.equals(e.getSubjectType())).collect(Collectors.toList());
			
			Set<String> subjectIds = EntityUtils.getSet(jxbClassList, NewGkDivideClass::getSubjectIds);
			Map<String, String> coureNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
			
			Map<String, Map<String, List<NewGkDivideClass>>> subjectLevelMap = new HashMap<>();
			String bestType;
			for (NewGkDivideClass divideClass : jxbClassList) {
				if(StringUtils.isBlank(divideClass.getBatch())) {
					continue;//暂时页面不展现
				}
				bestType = Optional.ofNullable(divideClass.getBestType()).orElse("V");
				
				if(!subjectLevelMap.containsKey(divideClass.getSubjectIds())) {
					subjectLevelMap.put(divideClass.getSubjectIds(), new TreeMap<>());
				}
				if(!subjectLevelMap.get(divideClass.getSubjectIds()).containsKey(bestType)) {
					subjectLevelMap.get(divideClass.getSubjectIds()).put(bestType, new ArrayList<>());
				}
				subjectLevelMap.get(divideClass.getSubjectIds()).get(bestType).add(divideClass);
				if(!bathList.contains(divideClass.getBatch())) {
					bathList.add(divideClass.getBatch());
				}
				
			}
			
			List<DivideResultDto> dtoList = new ArrayList<>();
			DivideResultDto dto;
			for (String subjectId : subjectLevelMap.keySet()) {
				Map<String, List<NewGkDivideClass>> levelMap = subjectLevelMap.get(subjectId);
				Integer stuNum = levelMap.values().stream().flatMap(e->e.stream())
						.map(e->e.getStudentList().size())
						.reduce((e1,e2)->e1+e2)
						.orElse(0);
				
				dto = new DivideResultDto();
				dto.setCourseName(coureNameMap.get(subjectId)+type);
				dto.setLevelNum(levelMap.size());
				dto.setTotalNum(stuNum);
				dto.setLevelMap(levelMap);
				
				for (List<NewGkDivideClass> l : levelMap.values()) {
					Collections.sort(l, (x,y)->compareClassName(x.getClassName(),y.getClassName()));
				}
				for (Entry<String, List<NewGkDivideClass>> entry : levelMap.entrySet()) {
					Map<String, List<NewGkDivideClass>> batchMap = entry.getValue().stream().collect(Collectors.groupingBy(NewGkDivideClass::getBatch));
					dto.getLevelBatchMap().put(entry.getKey(), batchMap);
				}
				dtoList.add(dto);
			}
			
			map.put("dtoList", dtoList);
			if(CollectionUtils.isEmpty(bathList)) {
				bathList.add("1");
				bathList.add("2");
				bathList.add("3");
			}else {
				Collections.sort(bathList);
			}
			map.put("bathList", bathList);
			return "/newgkelective/divide/divideJxbResultList.ftl";
			
		}else {
			return errorFtl(map, "类型错误，请联系管理员");
		}
		
//		return null;
	}
	
	private int compareClassName(String className, String className2) {
		if((className.contains("A") || className.contains("B")) 
				&& (!className2.contains("A") && !className2.contains("B"))) {
			return -1;
		}
		if((className2.contains("A") || className2.contains("B")) 
				&& (!className.contains("A") && !className.contains("B"))) {
			return 1;
		}
//		Pattern p = Pattern.compile("[^\\d]+(\\d+)班");
		Pattern p = Pattern.compile("(\\d+)班");
		try {
			Matcher matcher = p.matcher(className);
			matcher.find();
			Integer num1 = Integer.parseInt(matcher.group(1));
			
			matcher = p.matcher(className2);
			matcher.find();
			Integer num2 = Integer.parseInt(matcher.group(1));
			
			return num1.compareTo(num2);
		} catch (Exception e) {
			e.printStackTrace();
			return className.compareTo(className2);
		}
		
	}

//	// TODO 手动随便组班
//	public void shoudongDivide(String divideId) {
//		NewGkDivide divide = newGkDivideService.findById(divideId);
//		List<NewGKStudentRange> studentRangeList = newGKStudentRangeService.findByDivideId(this.getLoginInfo().getUnitId(), divideId);
//		
//		studentRangeList = studentRangeList.stream().filter(e->e.getSubjectType().equals("A")).collect(Collectors.toList());
//		
//		List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeIn(divide.getId(),
//				new String[] {NewGkElectiveConstant.SUBJECT_TYPE_A, NewGkElectiveConstant.SUBJECT_TYPE_B});
//		Set<String> subjectIds = openSubjectList.stream().map(e->e.getSubjectId()).collect(Collectors.toSet());
//		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[] {})), Course.class);
//		Map<String, String> courseNameMap = EntityUtils.getMap(courseList, e->e.getId(),e->e.getSubjectName());
//		
//		Map<String,Map<String,List<String>>> map = new HashMap<>();
//		Map<String, List<String>> map2;
//		for (NewGKStudentRange range : studentRangeList) {
//			map2 = map.get(range.getSubjectId());
//			if(map2 == null) {
//				map2 = new HashMap<>();
//				map.put(range.getSubjectId(), map2);
//			}
//			if(!map2.containsKey(range.getRange())) {
//				map2.put(range.getRange(), new ArrayList<>());
//			}
//			map2.get(range.getRange()).add(range.getStudentId());
//		}
//		
//		List<String> stuIds;
//		int start = 0;
//		int endNum = 0;
//		List<NewGkDivideClass> classList = new ArrayList<>();
//		List<NewGkClassStudent> studentList = new ArrayList<>();
//		NewGkClassStudent student;
//		NewGkDivideClass clazz;
//		int classIndex = 1;
//		for (String subjectId : map.keySet()) {
//			map2 = map.get(subjectId);
//			
//			classIndex = 1;
//			for (String rangeLevel : map2.keySet()) {
//				stuIds = map2.get(rangeLevel);
//				
//				start = 0;
//				endNum = 0;
//				while(endNum < stuIds.size() ) {
//					endNum = start + 50;
//					if(endNum >= stuIds.size()) {
//						endNum = stuIds.size();
//					}
//					
//					List<String> subList = stuIds.subList(start, endNum);
//					
//					// 分班
//					clazz = new NewGkDivideClass();
//					clazz.setId(UuidUtils.generateUuid());
//					clazz.setClassName(courseNameMap.get(subjectId)+"A"+(classIndex++)+"("+rangeLevel+")班");
//					clazz.setSubjectIds(subjectId);
//					clazz.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
//					clazz.setClassType(NewGkElectiveConstant.CLASS_TYPE_2);
//					clazz.setBestType(rangeLevel);
//					
//					clazz.setGroupType(NewGkElectiveConstant.GROUP_TYPE_1);
//					clazz.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
//					clazz.setDivideId(divideId);
//					clazz.setIsHand(NewGkElectiveConstant.IS_HAND_1);
//					clazz.setModifyTime(new Date());
//					clazz.setCreationTime(new Date());
//					
//					classList.add(clazz);
//					
//					for (String stuId : subList) {
//						student = initClassStudent(divide.getUnitId(), divide.getId(), clazz.getId(), stuId);
//						studentList.add(student);
//					}
//					start = endNum;
//				}
//				
//			}
//		}
//		
//		List<NewGkChoResult> choiceList = newGkChoResultService.findByChoiceIdAndKindType(divide.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,divide.getChoiceId());
//		Set<String> studentIds = choiceList.stream().map(e->e.getStudentId()).collect(Collectors.toSet());
//		
//		map2 = new HashMap<>();  // 每个学生的 学考科目
//		for (String studentId : studentIds) {
//			map2.put(studentId, new ArrayList<>(subjectIds));
//		}
//		
//		for (NewGkChoResult choose : choiceList) {
//			if(map2.containsKey(choose.getStudentId())) {
//				map2.get(choose.getStudentId()).remove(choose.getSubjectId());
//			}
//		}
//		
//		Map<String, List<String>> subjectStuMap  = new HashMap<>();
//		for (String stuId : map2.keySet()) {
//			List<String> subjectIds2 = map2.get(stuId);
//			for (String saubjectId : subjectIds2) {
//				if(!subjectStuMap.containsKey(saubjectId)) {
//					subjectStuMap.put(saubjectId, new ArrayList<>());
//				}
//				subjectStuMap.get(saubjectId).add(stuId);
//			}
//		}
//		
//		for (String subjectId : subjectStuMap.keySet()) {
//			stuIds = subjectStuMap.get(subjectId);
//			
//			classIndex =1;
//			start = 0;
//			endNum = 0;
//			while(endNum < stuIds.size() ) {
//				endNum = start + 50;
//				if(endNum >= stuIds.size()) {
//					endNum = stuIds.size();
//				}
//				
//				List<String> subList = stuIds.subList(start, endNum);
//				
//				// 分班
//				clazz = new NewGkDivideClass();
//				clazz.setId(UuidUtils.generateUuid());
//				clazz.setClassName(courseNameMap.get(subjectId)+"B"+(classIndex++)+"班");
//				clazz.setSubjectIds(subjectId);
//				clazz.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_B);
//				clazz.setClassType(NewGkElectiveConstant.CLASS_TYPE_2);
////				clazz.setBestType(rangeLevel);
//				
//				clazz.setGroupType(NewGkElectiveConstant.GROUP_TYPE_1);
//				clazz.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
//				clazz.setDivideId(divideId);
//				clazz.setIsHand(NewGkElectiveConstant.IS_HAND_1);
//				
//				classList.add(clazz);
//				
//				for (String stuId : subList) {
//					student = new NewGkClassStudent();
//					student.setClassId(clazz.getId());
//					student.setStudentId(stuId);
//					student.setModifyTime(new Date());
//					student.setCreationTime(new Date());
//					student.setDivideId(divideId);
//					studentList.add(student);
//				}
//				
//				start = endNum;
//			}
//		}
////		int i = 1/0;
//		List<NewGkDivideClass> delClassList = newGkDivideClassService.findByDivideIdIn(new String[] {divideId});
//		Set<String> delClassIds = EntityUtils.getSet(delClassList,e->e.getId());
//		newGkDivideClassService.saveAllList(divide.getUnitId(), divideId, delClassIds.toArray(new String[] {}), classList, studentList, true);
//		
//		
//	}
	
	@RequestMapping("/showClassDetail/page")
	public String showDivideClassDetail(@PathVariable String divideId, @RequestParam("classId") String classId, 
			@RequestParam(name="type",required=false) String type, ModelMap map) {
		String unitId =this.getLoginInfo().getUnitId();
		NewGkDivideClass divideClass = newGkDivideClassService.findById(unitId, classId, true);
		List<String> stuIdList = divideClass.getStudentList();
		
//		List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(stuIdList.toArray(new String[] {})), Student.class);
		List<Student> studentList=new ArrayList<>();
		if(CollectionUtils.isNotEmpty(stuIdList)) {
			studentList = SUtils.dt(studentRemoteService.findPartStudentById(stuIdList.toArray(new String[] {})), Student.class);
		}
		
		String mcodeId = ColumnInfoUtils.getColumnInfo(Student.class, "sex").getMcodeId();
//		Map<String, JSONObject> codeMap = SUtils.deserialize(mcodeRemoteService.findMapByMcodeId(mcodeId),
//				new TypeReference<Map<String, JSONObject>>() {});
		Map<String, McodeDetail> codeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId(mcodeId), 
				new TypeReference<Map<String, McodeDetail>>() {});
		
//		Map<String, String> toClassIdMap = EntityUtils.getMap(studentList, "id", "classId");
		Set<String> classIds = EntityUtils.getSet(studentList,Student::getClassId);
		List<Clazz> classList = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[] {})), Clazz.class);
		Map<String, Clazz> classMap = EntityUtils.getMap(classList, Clazz::getId);
		
		// 获取学生选课 情况
		NewGkDivide divide = newGkDivideService.findById(divideId);
		List<NewGkChoResult> choResultList = newGkChoResultService.findByKindTypeAndChoiceIdAndStudentIds(divide.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,divide.getChoiceId(), stuIdList.toArray(new String[] {}));
		Map<String, List<String>> stuSubjectMap = new HashMap<>();
		for (NewGkChoResult choose : choResultList) {
			if(!stuSubjectMap.containsKey(choose.getStudentId())) {
				stuSubjectMap.put(choose.getStudentId(), new ArrayList<>());
			}
			stuSubjectMap.get(choose.getStudentId()).add(choose.getSubjectId());
		}
		
		// 选课组合
		List<String> subjectIds = newGkChoRelationService.findByChoiceIdAndObjectType(divide.getUnitId(), 
				divide.getChoiceId(), NewGkElectiveConstant.CHOICE_TYPE_01);
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[] {})), Course.class);
		Map<String, String> courseMap = EntityUtils.getMap(courseList, "id","subjectName");
		
		List<NewGkDivideClass> divideClassList = newGkDivideClassService.findByDivideIdAndClassType(unitId, 
				divideId,
				new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_4}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		Map<String, List<String>> stuAJxbMap = new HashMap<>();
		Map<String, List<String>> stuBJxbMap = new HashMap<>();
		
		Map<String, List<String>> otherClassMap = new HashMap<>();
		
		Map<String, String> stuXzbMap = new HashMap<>();
		for (NewGkDivideClass divideClass2 : divideClassList) {
			if(NewGkElectiveConstant.CLASS_TYPE_1.equals(divideClass2.getClassType())) {
				// 行政班
				for (String stuId : divideClass2.getStudentList()) {
					stuXzbMap.put(stuId, divideClass2.getClassName());
				}
			}else if(NewGkElectiveConstant.CLASS_TYPE_2.equals(divideClass2.getClassType())){
				for (String stuId : divideClass2.getStudentList()) {
					if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(divideClass2.getSubjectType())) {
						if(!stuAJxbMap.containsKey(stuId)) {
							stuAJxbMap.put(stuId, new ArrayList<>());
						}
						stuAJxbMap.get(stuId).add(divideClass2.getClassName());
					}else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(divideClass2.getSubjectType())) {
						if(!stuBJxbMap.containsKey(stuId)) {
							stuBJxbMap.put(stuId, new ArrayList<>());
						}
						stuBJxbMap.get(stuId).add(divideClass2.getClassName());
					}else {

						//其他
						//throw new RuntimeException("error occur in showDivideClassDetail,班级科目类型除了A/B 不应该有其他类型。");
//						
//						if(!otherJxbMap.containsKey(stuId)) {
//							otherJxbMap.put(stuId, new ArrayList<>());
//						}
//						otherJxbMap.get(stuId).add(divideClass2.getClassName());
					}
				}
			}else if(NewGkElectiveConstant.CLASS_TYPE_4.equals(divideClass2.getClassType())) {
				for (String stuId : divideClass2.getStudentList()) {
					if(!otherClassMap.containsKey(stuId)) {
						otherClassMap.put(stuId, new ArrayList<>());
					}
					otherClassMap.get(stuId).add(divideClass2.getClassName());
				}
			}
			
		}
		
		// 结果
		List<StudentResultDto> dtoList = new ArrayList<>();
		StudentResultDto dto;
		for (Student student : studentList) {
//			String chooseSubjects = stuSubjectMap.get(student.getId()).stream().map(e->courseMap.get(e)).reduce((e1,e2)->e1+"、"+e2).orElse("");
			//单科分层可能存在学生没有选课
			String chooseSubjects="";
			if(stuSubjectMap.containsKey(student.getId())) {
				for(String s:stuSubjectMap.get(student.getId())) {
					if(courseMap.containsKey(s)) {
						chooseSubjects=chooseSubjects+"、"+courseMap.get(s);
					}
				}
			}
			if(StringUtils.isNotBlank(chooseSubjects)) {
				chooseSubjects=chooseSubjects.substring(1);
			}
			
			dto = new StudentResultDto();
			
			dto.setStudentName(student.getStudentName());
			dto.setStudentCode(student.getStudentCode());
			dto.setSex(codeMap.get(student.getSex()+"").getMcodeContent());
//			dto.setSex((String)codeMap.get(student.getSex()+"").get("mcodeContent"));
			if(classMap.get(student.getClassId()) == null) {
				dto.setOldClassName("未找到");
			}else {
				dto.setOldClassName(classMap.get(student.getClassId()).getClassName());
			}
			
			dto.setChooseSubjects(chooseSubjects);
			dto.setClassName(stuXzbMap.get(student.getId()));
			dto.setJxbAClasss(stuAJxbMap.get(student.getId()));
			dto.setJxbBClasss(stuBJxbMap.get(student.getId()));
//			dto.getJxbBClasss().addAll(otherJxbMap.get(student.getId()));
			dto.setOtherClasss(otherClassMap.get(student.getId()));
			dtoList.add(dto);
		}
		
		if("Y".equals(type) || "X".equals(type)) {
			// 获取所有行政班
			List<NewGkDivideClass> xzbList = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_1},
					false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			xzbList.sort((x,y)->{
				if(x.getOrderId()==null){
					return 1;
				}else if(y.getOrderId()==null){
					return -1;
				}else if(x.getOrderId().compareTo(y.getOrderId()) != 0){
					return x.getOrderId().compareTo(y.getOrderId());
				}
				return x.getClassName().compareTo(y.getClassName());
			});
			map.put("xzbList", xzbList);
		}else if("J".equals(type)) {
			List<NewGkDivideClass> xzbList = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_4},
					false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			map.put("xzbList", xzbList);
		}
		boolean isShowAB=true;
		if(divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_10) || divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_12) ) {
			isShowAB=false;
		}
		map.put("isShowAB", isShowAB);
		//按学号排序
		if(CollectionUtils.isNotEmpty(dtoList)) {
			Collections.sort(dtoList,(x,y)->{
				if(x.getStudentCode() == null && y.getStudentCode() == null) {
					return 0;
				}else if(x.getStudentCode() == null) {
					return 1;
				}else if(y.getStudentCode() == null) {
					return -1;
				}
				return x.getStudentCode().compareTo(y.getStudentCode());
			});
		}
		map.put("dtoList", dtoList);
		map.put("type", type);
		map.put("divideId", divideId);
		map.put("divideClass", divideClass);
		
		if(NewGkElectiveConstant.DIVIDE_TYPE_05.equals(divide.getOpenType())) {
			// 不显示新行政班一列
			map.put("showNewXZB", false);
		}
		
		return "/newgkelective/divide/divideStudentResult.ftl";
	}
	/**
	 * 2+x 3+0 手动维护
	 */
	@RequestMapping("/divideGroup/groupIndex/page")
	@ControllerInfo(value = "手动维护组合班")
	public String showGroupList(@PathVariable String divideId, ModelMap map) {
		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			return errorFtl(map, "分班方案不存在");
		}
		NewGkChoice newGkChoice = newGkChoiceService.findById(newDivide
				.getChoiceId());
		boolean isCanEdit=false;
		boolean isAutoTwo=false;
		if ("0".equals(newDivide.getStat()) && !isNowDivide(divideId)) {
			isCanEdit=true;
		} 
		map.put("divide", newDivide);
		if(checkAutoTwo(divideId)){
			isAutoTwo=true;
		}
		
		if(isCanEdit && !isAutoTwo){
			isCanEdit=true;
		}else{
			isCanEdit=false;
		}
		
		if(isCanEdit) {
			//手动安排
			//存在教学班就不能修改
			List<NewGkDivideClass> jxbList = newGkDivideClassService
					.findByDivideIdAndClassType(newDivide.getUnitId(),
							divideId,
							new String[] { NewGkElectiveConstant.CLASS_TYPE_2 }, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			if(CollectionUtils.isNotEmpty(jxbList)) {
				isCanEdit=false;
			}
		}
		map.put("isCanEdit", isCanEdit);
		map.put("isAutoTwo", isAutoTwo);
		return showArrangeList(newGkChoice, newDivide, map);
	}

	/**
	 * 获取单科数据
	 * 
	 * @param divideOneSubList
	 * @return
	 */
	public Map<String, List<NewGkDivideClass>> getOneSubMap(
			List<NewGkDivideClass> divideOneSubList) {
		Map<String, List<NewGkDivideClass>> oneSubMap = new HashMap<String, List<NewGkDivideClass>>();

		Map<String, String> courseNameMap = EntityUtils.getMap(SUtils.dt(
				courseRemoteService.findByCodes73(getLoginInfo().getUnitId()), new TR<List<Course>>() {
				}), "id", "subjectName");// TODO
		if (CollectionUtils.isNotEmpty(divideOneSubList)) {
			for (NewGkDivideClass divideClass : divideOneSubList) {
				String courseName = courseNameMap.get(divideClass
						.getSubjectIds());
				List<NewGkDivideClass> inList = oneSubMap.get(courseName);
				if (CollectionUtils.isEmpty(inList)) {
					inList = new ArrayList<NewGkDivideClass>();
				}
				inList.add(divideClass);
				oneSubMap.put(courseName, inList);
			}
		}
		return oneSubMap;
	}

	/**
	 * 7选3分班结果
	 */
//	@SuppressWarnings("unused")
//	private String showArrangeResult(String type, NewGkChoice newGkChoice,
//			NewGkDivide newDivide, ModelMap map) {
//		// 选课学生
//		Set<String> studentIds = newGkChoResultService
//				.findSetByChoiceIdAndKindType(newGkChoice.getId(),NewGkElectiveConstant.KIND_TYPE_01);
//		// 查分班結果
//		List<NewGkDivideClass> allDivideClassList = new ArrayList<NewGkDivideClass>();
//		if ("1".equals(type)) {
//			allDivideClassList = newGkDivideClassService
//					.findByDivideIdAndSourceType(newDivide.getId(),
//							NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
//		} else {
//			allDivideClassList = newGkDivideClassService
//					.findByDivideIdAndSourceType(newDivide.getId(),
//							NewGkElectiveConstant.CLASS_SOURCE_TYPE1, true);
//		}
//
//		if (CollectionUtils.isNotEmpty(allDivideClassList)) {
//			Set<String> ids = EntityUtils.getSet(allDivideClassList, "id");
//			Map<String, List<String>> map1 = newGkClassStudentService
//					.findMapByClassIds(ids.toArray(new String[] {}));
//			for (NewGkDivideClass divideClass : allDivideClassList) {
//				if (map1.containsKey(divideClass.getId())) {
//					List<String> stusList = map1.get(divideClass.getId());
//					if (CollectionUtils.isNotEmpty(stusList)) {
//						divideClass.setStudentList(stusList);
//					}
//
//				}
//			}
//		}
//
//		List<NewGkDivideClass> divideClassList = new ArrayList<NewGkDivideClass>();
//		List<NewGkDivideClass> divideThreeSubList = new ArrayList<NewGkDivideClass>();
//		List<NewGkDivideClass> divideOneSubList = new ArrayList<NewGkDivideClass>();
//		Set<String> divideClassIds = new HashSet<String>();
//		Set<String> stuidSet = new HashSet<>();
//		// int fixStudentNum=0;
//		if (CollectionUtils.isNotEmpty(allDivideClassList)) {
//			for (NewGkDivideClass allDivideClass : allDivideClassList) {
//				if (NewGkElectiveConstant.CLASS_TYPE_0.equals(allDivideClass
//						.getClassType())) {
//					divideThreeSubList.add(allDivideClass);
//				} else if (NewGkElectiveConstant.CLASS_TYPE_1
//						.equals(allDivideClass.getClassType())) {
//					divideClassList.add(allDivideClass);
//				} else if (NewGkElectiveConstant.CLASS_TYPE_2
//						.equals(allDivideClass.getClassType())) {
//					// 教學班
//					divideOneSubList.add(allDivideClass);
//				}
//				divideClassIds.add(allDivideClass.getId());
//				List<String> stulist = allDivideClass.getStudentList();
//				if (CollectionUtils.isNotEmpty(stulist)) {
//					stuidSet.addAll(new HashSet<String>(stulist));
//				}
//			}
//		}
//		if (newDivide.getStat().equals("1")) {// 已完成分班 显示单科
//			map.put("oneSubMap", getOneSubMap(divideOneSubList));
//		}
//		// divide.setStat(stat);
//		// int
//		// fixStudentNum=newGkClassStudentService.findSetByClassIds(divideClassIds.toArray(new
//		// String[0])).size();
//		map.put("divide", newDivide);
//		int allNum1 = stuidSet.size();
//		map.put("noFixStudentNum",
//				allNum1 - stuidSet.size() >= 0 ? (allNum1 - stuidSet.size())
//						: 0);
//		map.put("fixStudentNum", allNum1);
//		map.put("allStudentNum", allNum1);
//		map.put("chosenStudentNum", studentIds.size());
//		map.put("divideClassList", divideClassList);
//		map.put("divideThreeSubList", divideThreeSubList);
//
//		return "newgkelective/divide/divideResultList.ftl";
//	}

	@SuppressWarnings("unused")
	private String showArrangeResult(String type, NewGkChoice newGkChoice,
			NewGkDivide newDivide, ModelMap map) {

		// Map<String, List<NewGkChoRelation>> NewGkChoRelationAllMap=
		// newGkChoRelationService.findByChoiceId(divide.getChoiceId(),true);
		// List<NewGkChoRelation> relationList=
		// NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_04);
		// List<String> values=EntityUtils.getList(relationList, "objectValue");

		// int allNum=0;
		// List<Student>stulistsum =
		// SUtils.dt(studentRemoteService.findByGradeId(divide.getGradeId()),new
		// TR<List<Student>>(){});
		// if(CollectionUtils.isNotEmpty(stulistsum)){
		// allNum= stulistsum.size()-values.size();
		// }

		// 选课学生
		Set<String> studentIds = newGkChoResultService
				.findSetByChoiceIdAndKindType(newGkChoice.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,newGkChoice.getId());
		// 查分班結果
		List<NewGkDivideClass> allDivideClassList = new ArrayList<NewGkDivideClass>();
		if ("1".equals(type)) {
			allDivideClassList = newGkDivideClassService
					.findByDivideIdAndSourceType(newDivide.getId(),
							NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		} else {
			allDivideClassList = newGkDivideClassService
					.findByDivideIdAndSourceType(newDivide.getId(),
							NewGkElectiveConstant.CLASS_SOURCE_TYPE1, true);
		}

		if (CollectionUtils.isNotEmpty(allDivideClassList)) {
			Set<String> ids = EntityUtils.getSet(allDivideClassList, NewGkDivideClass::getId);
			Map<String, List<String>> map1 = newGkClassStudentService
					.findMapByClassIds(newDivide.getUnitId(),newDivide.getId(), ids.toArray(new String[] {}));
			for (NewGkDivideClass divideClass : allDivideClassList) {
				if (map1.containsKey(divideClass.getId())) {
					List<String> stusList = map1.get(divideClass.getId());
					if (CollectionUtils.isNotEmpty(stusList)) {
						divideClass.setStudentList(stusList);
					}

				}
			}
		}

		List<NewGkDivideClass> divideClassList = new ArrayList<NewGkDivideClass>();
		List<NewGkDivideClass> divideThreeSubList = new ArrayList<NewGkDivideClass>();
		List<NewGkDivideClass> divideOneSubList = new ArrayList<NewGkDivideClass>();
		Set<String> divideClassIds = new HashSet<String>();
		Set<String> stuidSet = new HashSet<>();
		// int fixStudentNum=0;
		if (CollectionUtils.isNotEmpty(allDivideClassList)) {
			for (NewGkDivideClass allDivideClass : allDivideClassList) {
				if (NewGkElectiveConstant.CLASS_TYPE_0.equals(allDivideClass
						.getClassType())) {
					divideThreeSubList.add(allDivideClass);
				} else if (NewGkElectiveConstant.CLASS_TYPE_1
						.equals(allDivideClass.getClassType())) {
					divideClassList.add(allDivideClass);
				} else if (NewGkElectiveConstant.CLASS_TYPE_2
						.equals(allDivideClass.getClassType())) {
					// 教學班
					divideOneSubList.add(allDivideClass);
				}
				divideClassIds.add(allDivideClass.getId());
				List<String> stulist = allDivideClass.getStudentList();
				if (CollectionUtils.isNotEmpty(stulist)) {
					stuidSet.addAll(new HashSet<String>(stulist));
				}
			}
		}
		if (newDivide.getStat().equals("1")) {// 已完成分班 显示单科
			map.put("oneSubMap", getOneSubMap(divideOneSubList));
		}
		// divide.setStat(stat);
		// int
		// fixStudentNum=newGkClassStudentService.findSetByClassIds(divideClassIds.toArray(new
		// String[0])).size();
		map.put("divide", newDivide);
		int allNum1 = stuidSet.size();
		map.put("noFixStudentNum",
				allNum1 - stuidSet.size() >= 0 ? (allNum1 - stuidSet.size())
						: 0);
		map.put("fixStudentNum", allNum1);
		map.put("allStudentNum", allNum1);
		map.put("chosenStudentNum", studentIds.size());
		map.put("divideClassList", divideClassList);
		map.put("divideThreeSubList", divideThreeSubList);

		return "newgkelective/divide/divideResultList.ftl";
	}

	/**
	 * 7选3手动排班首页
	 * 
	 * @param newGkChoice
	 * @param newDivide
	 * @param map
	 * @return
	 */
	private String showArrangeList(NewGkChoice newGkChoice,
			NewGkDivide newDivide, ModelMap map) {
		List<Course> list = SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()),
				new TR<List<Course>>() {
				});
		// 此处不再排序
		Map<String, Course> courseMap = new HashMap<String, Course>();
		for (Course course : list) {
			courseMap.put(course.getId(), course);
		}

		boolean flag = findGroupClass(newDivide, newGkChoice, map, courseMap);
		map.put("divideId", newDivide.getId());
		map.put("openType", newDivide.getOpenType());
		map.put("haserror", flag);
		map.put("gradeId", newDivide.getGradeId());
		map.put("divideName", newDivide.getDivideName());
		List<String> subjectIdList = newGkChoRelationService.findByChoiceIdAndObjectType(newDivide.getUnitId(), newGkChoice.getId(), NewGkElectiveConstant.CHOICE_TYPE_01);
		if(CollectionUtils.isNotEmpty(subjectIdList) ){
//			if(subjectIdList.size()==6){
//				map.put("isSix", true);
//			}
			
			List<Course> courselist = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIdList.toArray(new String[] {})),Course.class);
			map.put("courseList",courselist);
		}else{
//			map.put("isSix", false);
		}
		//原行政班数量
		List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(newDivide.getUnitId(),newDivide.getGradeId()), new TR<List<Clazz>>(){});
		int xzbNum = clazzList.size();
		map.put("xzbNum", xzbNum);
		
		//每个科目的人数
		
		
		
		return "/newgkelective/divideGroup/groupMainList.ftl";
	}
	/**
	 * 选考科目手动分层 页面
	 * 
	 * @param newGkChoice
	 * @param newDivide
	 * @param map
	 * @return
	 */
	private String showDivideRangeIndex(NewGkChoice newGkChoice,
			NewGkDivide newDivide, ModelMap map) {
		List<NewGkOpenSubject> aSubjectList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeInWithMaster(newDivide.getId(), new String[] {NewGkElectiveConstant.SUBJECT_TYPE_A});
		// 此处不再排序
		Map<String, Course> courseMap = new HashMap<String, Course>();
		
		if(CollectionUtils.isNotEmpty(aSubjectList)){
			Set<String> subjectSet = EntityUtils.getSet(aSubjectList, NewGkOpenSubject::getSubjectId);
			
			List<Course> list = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectSet.toArray(new String[] {})),Course.class);
			if(false) {
				List<Course> ysyCourses = SUtils.dt(courseRemoteService.findByCodesYSY(getLoginInfo().getUnitId()),Course.class);
				list.addAll(ysyCourses);
			}
			if(CollectionUtils.isNotEmpty(list)){
				for (Course course : list) {
					courseMap.put(course.getSubjectName(), course);
				}
			}
		}
		
		

		map.put("courseMap", courseMap);
		map.put("divideId", newDivide.getId());
		map.put("gradeId", newDivide.getGradeId());
		
		return "/newgkelective/divideGroup/rankStuRangeIndex.ftl";
	}
	/**
	 * 
	 * @param newGkChoice
	 * @param newDivide
	 * @param map
	 * @return
	 */
	@RequestMapping("/subjectChoiceInfo")
	private String changeSubjectTab(@PathVariable("divideId") String divideId, String subjectId, ModelMap map) {
		NewGkDivide divide = newGkDivideService.findById(divideId);
		List<Course> ysyCourses = SUtils.dt(courseRemoteService.findByCodesYSY(getLoginInfo().getUnitId()),Course.class);
		Set<String> ysyIds = EntityUtils.getSet(ysyCourses, Course::getId);
		
		
		Set<String> studentIds;
		String subjectType = NewGkElectiveConstant.SUBJECT_TYPE_A;
		if(ysyIds.contains(subjectId)) {
			subjectType = NewGkElectiveConstant.SUBJECT_TYPE_O;
			studentIds = newGkChoResultService.findSetByChoiceIdAndKindType(divide.getUnitId(), NewGkElectiveConstant.KIND_TYPE_01,divide.getChoiceId());
		}else {
			List<String> choiceList = newGkChoResultService.findByChoiceIdAndSubjectIdAndKindType(divide.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,divide.getChoiceId(), subjectId);
			studentIds = new HashSet<>(choiceList);
		}
		
		
		// 取出已安排人数
		List<NewGKStudentRange> stuRangeList = newGKStudentRangeService.findByDivideIdSubjectIdAndSubjectType(divideId, subjectId, 
				subjectType, null);
		Set<String> rangeStuIds = EntityUtils.getSet(stuRangeList, NewGKStudentRange::getStudentId);
		
		studentIds.removeAll(rangeStuIds);
		
		return makeStuResultDto(subjectId, map, divide, studentIds.toArray(new String[] {}));
	}

	private String makeStuResultDto(String subjectId, ModelMap map, NewGkDivide divide, String[] studentIds) {
		List<Student> gradeStuList = SUtils.dt(studentRemoteService.findPartStudByGradeId(divide.getUnitId(), divide.getGradeId(),null, null),Student.class);
		Map<String, Student> stuMap = EntityUtils.getMap(gradeStuList, Student::getId);
		List<Student> studentList = Arrays.stream(studentIds)
				.distinct()
				.filter(e->stuMap.containsKey(e))
				.map(e->stuMap.get(e)).collect(Collectors.toList());
		
		int manCount = (int)studentList.stream().filter(e->e.getSex().equals(1)).count();
		int woManCount = (int)studentList.stream().filter(e->e.getSex().equals(2)).count();
		
		// 成绩
		String mcodeId = ColumnInfoUtils.getColumnInfo(Student.class, "sex").getMcodeId();
		Map<String, McodeDetail> codeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId(mcodeId), 
				new TypeReference<Map<String, McodeDetail>>() {});
		Map<String, Map<String, Float>> stuScoreMap = getScoreMap(divide, studentIds);
		
		
		List<Course> ysyCourses = SUtils.dt(courseRemoteService.findByCodesYSY(getLoginInfo().getUnitId()),Course.class);
		Course course = SUtils.dc(courseRemoteService.findOneById(subjectId),Course.class);
		List<StudentResultDto> dtoList = new ArrayList<>();
		StudentResultDto dto;
		float courseAvg = (float)0.0;
		float ysyAvg = (float)0.0;
		float totalAvg = (float)0.0;
		for (Student stu : studentList) {
			dto = new StudentResultDto();
			dto.setStudentId(stu.getId());
			dto.setStudentName(stu.getStudentName());
			dto.setSex(codeMap.get(stu.getSex()+"").getMcodeContent());
			
			Map<String, Float> scoreMap = stuScoreMap.get(stu.getId());
			Float score1;
			float ysyScore = (float)0.0;
			float allScore = (float)0.0;
			if(scoreMap==null){
				score1 = (float)0.0;
			}else{
				score1 = scoreMap.get(subjectId);
				if(score1 == null) {
					score1 = (float)0.0;
				}
				for(Course cc:ysyCourses){
					if(scoreMap.containsKey(cc.getId())){
						Float ss = scoreMap.get(cc.getId());
						if(ss==null){
							ss = (float)0.0;
						}
						ysyScore=ysyScore+ss;
					}
				}
				
				for(Entry<String, Float> item:scoreMap.entrySet()){
					Float ss = item.getValue();
					if(ss==null){
						ss = (float)0.0;
					}
					allScore=allScore+ss;
				}
			}
			
			courseAvg += score1;
			ysyAvg += ysyScore;
			totalAvg += allScore;
			
			dto.getSubjectScore().put(subjectId, score1);
			dto.getSubjectScore().put("YSY", ysyScore);
			dto.getSubjectScore().put("TOTAL", allScore);
			
			dtoList.add(dto);
		}
		
		int size = dtoList.size();
		if(size==0) {
			size = 1;
		}
		
		map.put("courseName", course.getSubjectName());
		map.put("subjectId", subjectId);
		map.put("dtoList", dtoList);
		map.put("manCount", manCount);
		map.put("woManCount", woManCount);
		map.put("courseAvg", courseAvg/size);
		map.put("ysyAvg", ysyAvg/size);
		map.put("totalAvg", totalAvg/size);
		
		return "/newgkelective/divideGroup/rangeStuInfoList.ftl";
	}
	/**
	 * 保存学生分层信息
	 * @param divideId
	 * @param subjectId
	 * @param stuIdstr
	 * @return
	 */
	@RequestMapping("/saveRangeInfo")
	@ResponseBody
	public String saveRangeInfo(@PathVariable("divideId")String divideId, String subjectId, 
			@RequestParam(name="stuIds[]",required=false) String[] stuIds, String range) {
		LoginInfo linf = getLoginInfo();
		List<Course> ysyCourses = SUtils.dt(courseRemoteService.findByCodesYSY(linf.getUnitId()),Course.class);
		Set<String> ysyIds = EntityUtils.getSet(ysyCourses, Course::getId);
		
		String subjectType = NewGkElectiveConstant.SUBJECT_TYPE_A;
		if(ysyIds.contains(subjectId)) {
			subjectType = NewGkElectiveConstant.SUBJECT_TYPE_O;
		}
		List<NewGKStudentRange> stuRangeList = new ArrayList<>();
		NewGKStudentRange stuRange;
		for (String stuId : Optional.ofNullable(stuIds).orElse(new String[] {}) ) {
			stuRange = new NewGKStudentRange();
			stuRange.setId(UuidUtils.generateUuid());
			stuRange.setDivideId(divideId);
			stuRange.setSubjectId(subjectId);
			stuRange.setSubjectType(subjectType);
			stuRange.setRange(range);
			stuRange.setStudentId(stuId);
			stuRange.setModifyTime(new Date());
			stuRange.setUnitId(linf.getUnitId());
			stuRangeList.add(stuRange);
		}
		
		
		try {
			newGKStudentRangeService.updateStudentRange(linf.getUnitId(),divideId,subjectId,subjectType, new String[] {range}, stuRangeList);
			
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		
		return returnSuccess();
	}
	/**
	 * 展示 学生分层信息
	 * @return
	 */
	@RequestMapping("/showRangeStus")
	public String showRangeInfo(@PathVariable("divideId")String divideId, String subjectId, String range, ModelMap map) {
		LoginInfo linfo= getLoginInfo();
		List<Course> ysyCourses = SUtils.dt(courseRemoteService.findByCodesYSY(linfo.getUnitId()),Course.class);
		Set<String> ysyIds = EntityUtils.getSet(ysyCourses, Course::getId);
		
		String subjectType = NewGkElectiveConstant.SUBJECT_TYPE_A;
		if(ysyIds.contains(subjectId)) {
			subjectType = NewGkElectiveConstant.SUBJECT_TYPE_O;
		}
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		
		List<NewGKStudentRange> allStuRangeList = newGKStudentRangeService.findByDivideIdSubjectIdAndSubjectType(divideId,
				subjectId, subjectType, range);
		List<String> stuIdList = allStuRangeList.stream()
				.map(e->e.getStudentId()).collect(Collectors.toList());
		
		Map<String, Integer> rangeMap = newGKStudentRangeService.findStuRangeCount(linfo.getUnitId(), divideId, subjectId, subjectType);
		
		map.put("rangeMap", rangeMap);
		map.put("right", 1);
		map.put("range", range);
		
		return makeStuResultDto(subjectId, map, divide, stuIdList.toArray(new String[] {}));
	}
	
	@RequestMapping("/resetRange")
	@ResponseBody
	public String resetRange(@PathVariable("divideId")String divideId, String subjectId) {
//		List<Course> ysyCourses = SUtils.dt(courseRemoteService.findByCodesYSY(getLoginInfo().getUnitId()),Course.class);
//		Set<String> ysyIds = EntityUtils.getSet(ysyCourses, Course::getId);
		String subjectType = NewGkElectiveConstant.SUBJECT_TYPE_A;
		
//		if(ysyIds.contains(subjectId)) {
//			subjectType = NewGkElectiveConstant.SUBJECT_TYPE_O;
//		}
		try {
			newGKStudentRangeService.deleteByDivideIdSubjectIdAndRange(this.getLoginInfo().getUnitId(), divideId, subjectId, subjectType, null);
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		
		return returnSuccess();
	}
	/**
	 * 分层完成 进行下一步
	 * @param divideId
	 * @param map
	 * @return
	 */
	@RequestMapping("/checkRangeFinish")
	@ResponseBody
	public String setdivideClassNum(@PathVariable("divideId")String divideId, ModelMap map) {
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService
				.findByDivideIdAndSubjectTypeIn(divideId, new String[] {
						NewGkElectiveConstant.SUBJECT_TYPE_A});
		Set<String> needArraySubIds=new HashSet<String>();
		if(CollectionUtils.isNotEmpty(openSubjectList)){
			needArraySubIds = EntityUtils.getSet(openSubjectList, NewGkOpenSubject::getSubjectId);
		}
		// 取出科目 人数
		List<Student> gradeStuList = SUtils.dt(studentRemoteService.findPartStudByGradeId(divide.getUnitId(), divide.getGradeId(),null, null),Student.class);
		Set<String> stuIds = EntityUtils.getSet(gradeStuList, Student::getId);
		List<NewGkChoResult> choiceList = newGkChoResultService.findByChoiceIdAndKindType(divide.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,divide.getChoiceId());
		// 确保这些选课的学生现在还在 还属于这个年级
		choiceList = choiceList.stream().filter(e->stuIds.contains(e.getStudentId())).collect(Collectors.toList());
		Map<String, Integer> subjectStuNumMap = choiceList.stream()
				.collect(()->new HashMap<>(), 
						(r,e)->{
							if(!r.containsKey(e.getSubjectId())) {
								r.put(e.getSubjectId(), 0);
							}
							r.put(e.getSubjectId(), r.get(e.getSubjectId())+1);
						}, 
						(r,r2)->{
							r.keySet().forEach(e->{
								if(!r2.containsKey(e)) {
									r2.put(e, 0);
								}
//								r2.put(e, r2.get(e)+1);
							});
						});
//		Map<String, List<NewGkChoResult>> subjectStuMap = choiceList.stream().collect(Collectors.groupingBy((e1->e1.getSubjectId())));
		
//		if(false) {
//			List<Course> ysyCourses = SUtils.dt(courseRemoteService.findByCodesYSY(getLoginInfo().getUnitId()),Course.class);
//			needArraySubIds.addAll(EntityUtils.getSet(ysyCourses, "id"));
//			Set<String> stuIds = EntityUtils.getSet(choiceList, "studentId");
//			for (Course course : ysyCourses) {
//				subjectStuNumMap.put(course.getId(), stuIds.size());
//			}
//		}
		
		// 取出已安排人数
		List<NewGKStudentRange> stuRangeList = newGKStudentRangeService.findByDivideIdSubjectIdAndSubjectType(divideId, null,
				NewGkElectiveConstant.SUBJECT_TYPE_A, null);
		List<NewGKStudentRange> stuRangeList2 = newGKStudentRangeService.findByDivideIdSubjectIdAndSubjectType(divideId, null,
				NewGkElectiveConstant.SUBJECT_TYPE_O, null);
		stuRangeList.addAll(stuRangeList2);
		Map<String, List<NewGKStudentRange>> subjectStuRangeMap = stuRangeList.stream()
				.collect(Collectors.groupingBy(NewGKStudentRange::getSubjectId));
		
		List<Course> courses = SUtils.dt(courseRemoteService.findListByIds(needArraySubIds.toArray(new String[] {})),Course.class);
		Map<String, Course> courseMap = EntityUtils.getMap(courses, e->e.getId());
		StringBuilder msg = new StringBuilder();
		for (String subjectId : subjectStuNumMap.keySet()) {
			Integer list = subjectStuNumMap.get(subjectId);
			if(list < 1) {
				continue;
			}
			if(!needArraySubIds.contains(subjectId)){
				//无需分层
				continue;
			}
			if(subjectStuRangeMap.containsKey(subjectId)) {
				// 此科目 已经分层
				List<NewGKStudentRange> list2 = subjectStuRangeMap.get(subjectId);
				if(list2.size() >= list) {
					continue;
				}
			}
			msg.append(courseMap.get(subjectId).getSubjectName())
					.append(",");
		}
		
		if(msg.length() > 0) {
			int index = msg.lastIndexOf(",");
			msg.replace(index, index+1, "");
			msg.append("等课程还没有分层,请完善");
			return error(msg.toString());
		}
		
		return returnSuccess();
	}
	
	private Map<String, Map<String, Float>> getScoreMap(NewGkDivide divide, String[] studentIds) {
		List<NewGkScoreResult> scoreList = newGkScoreResultService.findListByReferScoreId(divide.getUnitId(), divide.getReferScoreId(), true);
//		Set<String> students = new HashSet<>(Arrays.asList(studentIds));
//		scoreList = scoreList.stream().filter(e->students.contains(e.getStudentId())).collect(Collectors.toList());
		Map<String,Map<String, Float>> stuScoreMap = new HashMap<>();
		for (NewGkScoreResult sr : scoreList) {
			String studentId = sr.getStudentId();
			if(!stuScoreMap.containsKey(sr.getStudentId())) {
				stuScoreMap.put(sr.getStudentId(), new HashMap<>());
			}
			stuScoreMap.get(studentId).put(sr.getSubjectId(), sr.getScore());
		}
		return stuScoreMap;
	}

	@SuppressWarnings("unchecked")
	public boolean findGroupClass(NewGkDivide newDivide,
			NewGkChoice newGkChoice, ModelMap map, Map<String, Course> courseMap) {
		// 是否显示混合
		boolean isShowAll = false;
		if (NewGkElectiveConstant.DIVIDE_TYPE_02
				.equals(newDivide.getOpenType()) 
				|| NewGkElectiveConstant.DIVIDE_TYPE_06.equals(newDivide.getOpenType())) {
			// 重组
			isShowAll = true;
		}
		map.put("isShowAll", isShowAll);
		String divideId = newDivide.getId();

		boolean flag = false;// 是否有异常
		// 所有学生选课情况
		List<NewGkChoResult> list = newGkChoResultService.findByChoiceIdAndKindType(newGkChoice.getUnitId(), NewGkElectiveConstant.KIND_TYPE_01,newGkChoice.getId());
		Map<String,List<String>> subjectIdsByStuId=new HashMap<String, List<String>>();
		if(CollectionUtils.isNotEmpty(list)) {
			subjectIdsByStuId=EntityUtils.getListMap(list, "studentId", "subjectId");
		}
		
		
//		ChosenSearchDto dto = new ChosenSearchDto();
//		dto.setGradeId(newGkChoice.getGradeId());
//		dto.setUnitId(newGkChoice.getUnitId());
//		List<StudentResultDto> stuChooselist = newGkChoResultService
//				.findChosenList(newGkChoice.getId(), dto);

		// 已有组合班数据
		List<NewGkDivideClass> oldGroupList = newGkDivideClassService
				.findByDivideIdAndClassType(newDivide.getUnitId(),
						divideId,
						new String[] { NewGkElectiveConstant.CLASS_TYPE_0 }, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<NewGkGroupDto> oldGroupDtoList = makeOldGroupDto(oldGroupList);
		// 3门组合
		List<NewGkGroupDto> gDtoList = new ArrayList<NewGkGroupDto>();
		Map<String, NewGkGroupDto> gDtomap = new HashMap<String, NewGkGroupDto>();
		Map<String, Set<String>> subjectIdsMap = new HashMap<String, Set<String>>();// 同组合下学生

		// 2门组合
		List<NewGkGroupDto> gDtoList2 = new ArrayList<NewGkGroupDto>();
		Map<String, NewGkGroupDto> gDtomap2 = new HashMap<String, NewGkGroupDto>();
		Map<String, Set<String>> subjectIdsMap2 = new HashMap<String, Set<String>>();// 同组合下学生

		// 混合组合
		List<NewGkGroupDto> gDtoList3 = new ArrayList<NewGkGroupDto>();
		Map<String, NewGkGroupDto> gDtomap3 = new HashMap<String, NewGkGroupDto>();
		Map<String, Set<String>> subjectIdsMap3 = new HashMap<String, Set<String>>();// 同组合下学生

		String guidZero = BaseConstants.ZERO_GUID;// 混合subjectIds subjectType=1
		NewGkGroupDto g = new NewGkGroupDto();
		int chooseNum = newGkChoice.getChooseNum();
		Map<String,NewGkGroupDto> stuDtoMap3 = new HashMap<>();
		for (Entry<String, List<String>> stuSubjectIds : subjectIdsByStuId.entrySet()) {
			if(CollectionUtils.isEmpty(stuSubjectIds.getValue())) {
				continue;
			}
			// 选择满3门才算组合
			if (stuSubjectIds.getValue().size() != chooseNum) {
				continue;
			}
			String stuId=stuSubjectIds.getKey();
			Set<String> chooseSubjectId = new HashSet<>();
			chooseSubjectId.addAll(stuSubjectIds.getValue());
			String ids = keySort(chooseSubjectId);
			// 3门组合
			if (!gDtomap.containsKey(ids)) {
				g = new NewGkGroupDto();
				g.setSubjectIds(ids);
				g.setConditionName(nameSet(courseMap, ids));
				g.setGkGroupClassList(new ArrayList<NewGkDivideClass>());
				g.setAllNumber(1);
				gDtoList.add(g);
				gDtomap.put(ids, g);
				subjectIdsMap.put(ids, new HashSet<String>());
				subjectIdsMap.get(ids).add(stuId);
			} else {
				subjectIdsMap.get(ids).add(stuId);
				g = gDtomap.get(ids);
				g.setAllNumber(g.getAllNumber() + 1);
			}
			stuDtoMap3.put(stuId, g);

			// 两门组合--3中组合
			List<String> twoGroup = keySort2(chooseSubjectId);
			if (CollectionUtils.isNotEmpty(twoGroup)) {
				for (String key : twoGroup) {
					if (!gDtomap2.containsKey(key)) {
						g = new NewGkGroupDto();
						g.setSubjectIds(key);
						g.setConditionName(nameSet(courseMap, key));
						g.setGkGroupClassList(new ArrayList<NewGkDivideClass>());
						g.setAllNumber(1);
						gDtoList2.add(g);
						gDtomap2.put(key, g);
						subjectIdsMap2.put(key, new HashSet<String>());
						subjectIdsMap2.get(key).add(stuId);
					} else {
						subjectIdsMap2.get(key).add(stuId);
						g = gDtomap2.get(key);
						g.setAllNumber(g.getAllNumber() + 1);
					}
				}
			}
			// 混合
			if (!gDtomap3.containsKey(guidZero)) {
				g = new NewGkGroupDto();
				g.setSubjectIds(guidZero);
				g.setConditionName("混合");
				g.setGkGroupClassList(new ArrayList<NewGkDivideClass>());
				g.setAllNumber(1);
				gDtoList3.add(g);
				gDtomap3.put(guidZero, g);
				subjectIdsMap3.put(guidZero, new HashSet<String>());
				subjectIdsMap3.get(guidZero).add(stuId);
			} else {
				subjectIdsMap3.get(guidZero).add(stuId);
				g = gDtomap3.get(guidZero);
				g.setAllNumber(g.getAllNumber() + 1);
			}
		}

		Set<String> arrangeStuId = new HashSet<String>();// 已经排的学生
		if (CollectionUtils.isNotEmpty(oldGroupList)) {
			List<NewGkDivideClass> gc = null;
			Set<String> stusIds = null;
			for (NewGkGroupDto dd : oldGroupDtoList) {
				if (gDtomap.containsKey(dd.getSubjectIds())) {
					gDtomap.get(dd.getSubjectIds()).getGkGroupClassList()
							.addAll(dd.getGkGroupClassList());
					gc = dd.getGkGroupClassList();
					stusIds = subjectIdsMap.get(dd.getSubjectIds());
					if (CollectionUtils.isNotEmpty(gc)) {
						for (NewGkDivideClass gg : gc) {
							if (CollectionUtils.isNotEmpty(gg.getStudentList())) {
								gg.setStudentCount(gg.getStudentList().size());
								arrangeStuId.addAll(gg.getStudentList());
								List<String> stuIdnow = (List<String>) CollectionUtils
										.intersection(gg.getStudentList(),
												stusIds);
								if (stuIdnow.size() != gg.getStudentList()
										.size()) {
									gg.setNotexists(1);
									if (!flag) {
										flag = true;
									}
									break;
								}

							}
						}
					}

				} else if (gDtomap2.containsKey(dd.getSubjectIds())) {
					gDtomap2.get(dd.getSubjectIds()).getGkGroupClassList()
							.addAll(dd.getGkGroupClassList());
					gc = dd.getGkGroupClassList();
					stusIds = subjectIdsMap2.get(dd.getSubjectIds());
					if (CollectionUtils.isNotEmpty(gc)) {
						for (NewGkDivideClass gg : gc) {
							if (CollectionUtils.isNotEmpty(gg.getStudentList())) {
								gg.setStudentCount(gg.getStudentList().size());
								arrangeStuId.addAll(gg.getStudentList());
								List<String> stuIdnow = (List<String>) CollectionUtils
										.intersection(gg.getStudentList(),
												stusIds);
								if (stuIdnow.size() != gg.getStudentList()
										.size()) {
									gg.setNotexists(1);
									if (!flag) {
										flag = true;
									}
									break;
								}
							}
						}
					}

				} else if (gDtomap3.containsKey(dd.getSubjectIds())) {
					gDtomap3.get(dd.getSubjectIds()).getGkGroupClassList()
							.addAll(dd.getGkGroupClassList());
					gc = dd.getGkGroupClassList();
					// stusIds = subjectIdsMap3.get(dd.getSubjectIds());
					if (CollectionUtils.isNotEmpty(gc)) {
						for (NewGkDivideClass gg : gc) {
							gg.setStudentCount(gg.getStudentList().size());
							if (CollectionUtils.isNotEmpty(gg.getStudentList())) {
								arrangeStuId.addAll(gg.getStudentList());
							}
						}
					}

				} else {
					// 这种组合已经不存在啦
					dd.setConditionName(nameSet(courseMap, dd.getSubjectIds()));
					if (!flag) {
						flag = true;
					}
					dd.setNotexists(1);
					String[] zz = dd.getSubjectIds().split(",");
					if (zz == null || zz.length <= 1) {
						continue;// 只包括一个科目 那就是混合 32个0 这个问题不存在
					}
					if (zz.length == 2) {
						// 两门的
						gDtoList2.add(dd);
						gDtomap2.put(dd.getSubjectIds(), dd);
					} else {
						// 3门的
						gDtoList.add(dd);
						gDtomap.put(dd.getSubjectIds(), dd);
					}
					gc = dd.getGkGroupClassList();
					if (CollectionUtils.isNotEmpty(gc)) {
						for (NewGkDivideClass gg : gc) {
							if (CollectionUtils.isNotEmpty(gg.getStudentList())) {
								arrangeStuId.addAll(gg.getStudentList());
							}
						}
					}
				}

			}
		}
		if (CollectionUtils.isNotEmpty(gDtoList)) {
			for (NewGkGroupDto dd : gDtoList) {
				Set<String> stuIds = subjectIdsMap.get(dd.getSubjectIds());
				if (stuIds != null) {
					// 取得除去 arrangeStuId中剩下的学生
					stuIds.removeAll(arrangeStuId);
					dd.setLeftNumber(stuIds.size());
				}

			}
		}
		if (CollectionUtils.isNotEmpty(gDtoList2)) {
			for (NewGkGroupDto dd : gDtoList2) {
				Set<String> stuIds = subjectIdsMap2.get(dd.getSubjectIds());
				if (stuIds != null) {
					// 取得除去 arrangeStuId中剩下的学生
					stuIds.removeAll(arrangeStuId);
					dd.setLeftNumber(stuIds.size());
				}

			}
		}
		if (CollectionUtils.isNotEmpty(gDtoList3)) {
			for (NewGkGroupDto dd : gDtoList3) {
				Set<String> stuIds = subjectIdsMap3.get(dd.getSubjectIds());
				// 取得除去 arrangeStuId中剩下的学生
				stuIds.removeAll(arrangeStuId);
				dd.setLeftNumber(stuIds.size());
			}
		}
		
		// 将两科组合中 包含的三科组合 上色
		if(gDtomap2.size()>0) {
			List<NewGkSubjectGroupColor> sgcList = newGkSubjectGroupColorService.findByUnitIdGroupType(newGkChoice.getUnitId(),
					new String[] {NewGkElectiveConstant.COLOR_GROUP_TYPE_2});
			Map<String,NewGkSubjectGroupColor> subColorMap = EntityUtils.getMap(sgcList, NewGkSubjectGroupColor::getSubjectGroup);
			final String DEFAULT_COLR = "#ff6600";
			final NewGkSubjectGroupColor defaultGroupColor = new NewGkSubjectGroupColor();
			defaultGroupColor.setColor(DEFAULT_COLR);
			for (String subKey : gDtomap2.keySet()) {
				NewGkGroupDto newGkGroupDto = gDtomap2.get(subKey);
				// 确保 subKey 是按照 科目id 排序过后的 字符串  
				NewGkSubjectGroupColor gc = Optional.ofNullable(subColorMap.get(subKey)).orElse(defaultGroupColor);  //TODO 默认颜色
				newGkGroupDto.getColorList().add(gc);
				
				newGkGroupDto.getGkGroupClassList().stream().flatMap(e -> e.getStudentList().stream())
						.filter(e->stuDtoMap3.containsKey(e))
						.map(e -> stuDtoMap3.get(e)).distinct().forEach(e -> {
							e.getColorList().add(gc);
						});
			}
		}
		
		// 根据总人数排序
		SortUtils.DESC(gDtoList, "allNumber");
		map.put("gDtoList", gDtoList);
		SortUtils.DESC(gDtoList2, "allNumber");
		map.put("gDtoList2", gDtoList2);
		NewGkGroupDto gDto = null;
		if (CollectionUtils.isNotEmpty(gDtoList3)) {
			gDto = gDtoList3.get(0);
			// 总共学生人数
			map.put("chosenStudentNum", gDto.getAllNumber());
			map.put("noFixStudentNum", gDto.getLeftNumber());
			map.put("fixStudentNum", gDto.getAllNumber() - gDto.getLeftNumber());
		} else {
			gDto = new NewGkGroupDto();
			gDto.setConditionName("混合");
			gDto.setSubjectIds(BaseConstants.ZERO_GUID);
			// 总共学生人数
			map.put("chosenStudentNum", 0);
			map.put("noFixStudentNum", 0);
			map.put("fixStudentNum", 0);

		}

		map.put("gDto", gDto);

		return flag;
	}

	/**
	 * 手动调整页面 subjectIds 多个科目id以，隔开 groupClassId 某一个组合id
	 */
	@RequestMapping("/divideGroup/groupDetail/page")
	@ControllerInfo(value = "手动维护组合班")
	public String showGroupDetailIndex(@PathVariable String divideId,
			String subjectIds, String groupClassId, String groupsubjectIds,ModelMap map) {
		// 头部详情
		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			return errorFtl(map, "分班方案不存在");
		}
//		List<Course> list = SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()),
//				new TR<List<Course>>() {
//				});
//		// 此处不再排序
//		Map<String, Course> courseMap = new HashMap<String, Course>();
//		for (Course course : list) {
//			courseMap.put(course.getId(), course);
//		}
		
		if (StringUtils.isBlank(subjectIds)) {
			addErrorFtlOperation(map, "返回", "/newgkelective/" + divideId
					+ "/divideClass/resultClassList", "#showList");
			return errorFtl(map, "没有组合可以调整！");
		}

		Map<String, String> groupMap = new LinkedHashMap<String, String>();
//		Map<String, String> groupMap1 = new LinkedHashMap<String, String>();
//
//		
//		ChosenSearchDto dto = new ChosenSearchDto();
//		dto.setGradeId(newGkChoice.getGradeId());
//		dto.setUnitId(newGkChoice.getUnitId());
//		List<StudentResultDto> stuChooselist = newGkChoResultService
//				.findChosenList(newGkChoice.getId(), dto);
//		int chooseNum = newGkChoice.getChooseNum();
//
//		if (CollectionUtils.isNotEmpty(stuChooselist)) {
//			for (StudentResultDto d : stuChooselist) {
//				// 选择满3门才算组合
//				if (d.getResultList().size() != chooseNum) {
//					continue;
//				}
//				Set<String> chooseSubjectId = EntityUtils.getSet(
//						d.getResultList(), "subjectId");
//				String ids = keySort(chooseSubjectId);
//				if (!groupMap.containsKey(ids)) {
//					groupMap.put(ids, nameSet(courseMap, ids));
//				}
//				List<String> idsList = keySort2(chooseSubjectId);
//				if (CollectionUtils.isNotEmpty(idsList)) {
//					for (String s : idsList) {
//						if (!groupMap1.containsKey(s)) {
//							groupMap1.put(s, nameSet(courseMap, s));
//						}
//					}
//				}
//			}
//			for (String key : groupMap1.keySet()) {
//				groupMap.put(key, groupMap1.get(key));
//			}
//		}

		
		
		// 增加混合
		StringBuilder zeroStrs = new StringBuilder("");//组合中学生人数已经为0的组合
		if( NewGkElectiveConstant.DIVIDE_TYPE_06.equals(newDivide.getOpenType())) {
			//全手动 只体现组合数据
			if (newDivide != null) {
				findGroupMap(newDivide, groupMap,false, zeroStrs);
			}
		}else {
			if (newDivide != null) {
				findGroupMap(newDivide, groupMap,true, zeroStrs);
			}
//			if (NewGkElectiveConstant.DIVIDE_TYPE_02
//					.equals(newDivide.getOpenType()) ) {
//				// 重组
//				groupMap.put(BaseConstants.ZERO_GUID, "混合");
//			}
		}
		
		map.put("zeroStrs", zeroStrs);
		map.put("groupMap", groupMap);// 左边下拉框	
		map.put("subjectIds", subjectIds);
		// 左边
		List<NewGkDivideClass> groupClassList = new ArrayList<NewGkDivideClass>();
		if (StringUtils.isNotBlank(subjectIds)) {
			groupClassList = newGkDivideClassService.findClassBySubjectIdsWithMaster(
					newDivide.getUnitId(), divideId,
					NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_0, subjectIds, false);
		}
		map.put("groupClassList", groupClassList);
		map.put("groupClassId", groupClassId);
		map.put("divideId", divideId);
		//原行政
		List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(newDivide.getUnitId(),newDivide.getGradeId()), new TR<List<Clazz>>(){});
		map.put("clazzList", clazzList);
		
		if( NewGkElectiveConstant.DIVIDE_TYPE_06.equals(newDivide.getOpenType()) && BaseConstants.ZERO_GUID.equals(subjectIds)) {
			//只要显示三科就好
			if(StringUtils.isNotBlank(groupsubjectIds)) {
				map.put("groupsubjectIds", groupsubjectIds);
			}else {
				map.put("groupsubjectIds", "");
			}
			return "/newgkelective/divideGroup/schedulingIndex1.ftl";
		}else {
			return "/newgkelective/divideGroup/schedulingIndex.ftl";
		}
		
	}
	
	
	/**
	 * 查询选课组合
	 * @param newDivide
	 * @param groupMap(统计组合剩余人数)
	 * @param zeroSubStrs 没有人的组合科目ids
	 */
	public void findGroupMap(NewGkDivide newDivide, Map<String,String> groupMap, boolean isHasTwo, StringBuilder zeroSubStrs){
		//暂时不考虑数据错误的情况
		NewGkChoice newGkChoice = newGkChoiceService.findById(newDivide.getChoiceId());
		
		boolean isShowHun=false;
		if (NewGkElectiveConstant.DIVIDE_TYPE_02
				.equals(newDivide.getOpenType())) {
			// 重组
			isShowHun = true;
		}
		List<Course> list = SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()),
				new TR<List<Course>>() {});
		// 此处不再排序
		Map<String, Course> courseMap = EntityUtils.getMap(list, Course::getId);

		// 所有学生选课情况
		List<StudentResultDto> stuChooselist = newGkChoResultService
				.findGradeIdList(newGkChoice.getUnitId(),newGkChoice.getGradeId(),newGkChoice.getId(), null);

		// 已有组合班数据
		List<NewGkDivideClass> oldGroupList = newGkDivideClassService
				.findByDivideIdAndClassTypeWithMaster(newDivide.getUnitId(),
						newDivide.getId(),
						new String[] { NewGkElectiveConstant.CLASS_TYPE_0 }, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		//所有安排过的学生ids
		Set<String> oldStuId=new HashSet<>();
		if(CollectionUtils.isNotEmpty(oldGroupList)) {
			for (NewGkDivideClass cc : oldGroupList) {
				if(CollectionUtils.isNotEmpty(cc.getStudentList())) {
					oldStuId.addAll(cc.getStudentList());
				}
			}
		}
		Map<String, Set<String>> leftStuIdsMap = new HashMap<String, Set<String>>();// 剩下学生
		Map<String, List<String>> twoSubjectIdMap=new HashMap<>();
		int chooseNum = newGkChoice.getChooseNum();
		List<String> twoSubjectId;
		String guidZero = BaseConstants.ZERO_GUID;// 混合
		List<String> allChooseSubjectIds=new ArrayList<>();//所有组合科目数据
		if(isShowHun) {
			leftStuIdsMap.put(guidZero, new HashSet<>());
		}
		for(StudentResultDto studto:stuChooselist) {
			// 选择满3门才算组合
			if (studto.getResultList().size() != chooseNum) {
				continue;
			}
			Set<String> chooseSubjectId = EntityUtils.getSet(studto.getResultList(),NewGkChoResult::getSubjectId);
			String ids = keySort(chooseSubjectId);
			if(!allChooseSubjectIds.contains(ids)) {
				allChooseSubjectIds.add(ids);
			}
			twoSubjectId=null;
			if(isHasTwo) {
				twoSubjectId = twoSubjectIdMap.get(ids);
				if(CollectionUtils.isEmpty(twoSubjectId)) {
					twoSubjectId = keySort2(chooseSubjectId);
					twoSubjectIdMap.put(ids, twoSubjectId);
				}
			}
			
			//剩余各个组合学生id
			if(oldStuId.contains(studto.getStudentId())) {
				continue;
			}

			// 3门组合
			if (!leftStuIdsMap.containsKey(ids)) {
				leftStuIdsMap.put(ids, new HashSet<>());
			}
			leftStuIdsMap.get(ids).add(studto.getStudentId());
			// 2门组合
			if(isHasTwo && CollectionUtils.isNotEmpty(twoSubjectId)) {
				for (String twoId : twoSubjectId) {
					if (!leftStuIdsMap.containsKey(twoId)) {
						leftStuIdsMap.put(twoId, new HashSet<>());
					}
					leftStuIdsMap.get(twoId).add(studto.getStudentId());
				}
			}
			//混合
			if(isShowHun) {
				leftStuIdsMap.get(guidZero).add(studto.getStudentId());
			}
		}
		
		
		
//		// 3门组合
//		List<NewGkGroupDto> gDtoList = new ArrayList<NewGkGroupDto>();
//		Map<String, NewGkGroupDto> gDtomap = new HashMap<String, NewGkGroupDto>();
//		Map<String, Set<String>> subjectIdsMap = new HashMap<String, Set<String>>();// 同组合下学生
//
//		// 2门组合
//		List<NewGkGroupDto> gDtoList2 = new ArrayList<NewGkGroupDto>();
//		Map<String, NewGkGroupDto> gDtomap2 = new HashMap<String, NewGkGroupDto>();
//		Map<String, Set<String>> subjectIdsMap2 = new HashMap<String, Set<String>>();// 同组合下学生
//
//		// 混合组合
//		List<NewGkGroupDto> gDtoList3 = new ArrayList<NewGkGroupDto>();
//		Map<String, NewGkGroupDto> gDtomap3 = new HashMap<String, NewGkGroupDto>();
//		Map<String, Set<String>> subjectIdsMap3 = new HashMap<String, Set<String>>();// 同组合下学生
//
//		String guidZero = BaseConstants.ZERO_GUID;// 混合subjectIds subjectType=1
//		NewGkGroupDto g = new NewGkGroupDto();
//		int chooseNum = newGkChoice.getChooseNum();
//		for (StudentResultDto d : stuChooselist) {
//			// 选择满3门才算组合
//			if (d.getResultList().size() != chooseNum) {
//				continue;
//			}
//			Set<String> chooseSubjectId = EntityUtils.getSet(d.getResultList(),
//					"subjectId");
//			String ids = keySort(chooseSubjectId);
//			// 3门组合
//			if (!gDtomap.containsKey(ids)) {
//				g = new NewGkGroupDto();
//				g.setSubjectIds(ids);
//				g.setConditionName(nameSet(courseMap, ids));
//				g.setAllNumber(1);
//				gDtoList.add(g);
//				gDtomap.put(ids, g);
//				subjectIdsMap.put(ids, new HashSet<String>());
//				subjectIdsMap.get(ids).add(d.getStudentId());
//			} else {
//				subjectIdsMap.get(ids).add(d.getStudentId());
//				g = gDtomap.get(ids);
//				g.setAllNumber(g.getAllNumber() + 1);
//			}
//			if(isHasTwo) {
//				// 两门组合--3中组合
//				List<String> twoGroup = keySort2(chooseSubjectId);
//				if (CollectionUtils.isNotEmpty(twoGroup)) {
//					for (String key : twoGroup) {
//						if (!gDtomap2.containsKey(key)) {
//							g = new NewGkGroupDto();
//							g.setSubjectIds(key);
//							g.setConditionName(nameSet(courseMap, key));
//							g.setAllNumber(1);
//							gDtoList2.add(g);
//							gDtomap2.put(key, g);
//							subjectIdsMap2.put(key, new HashSet<String>());
//							subjectIdsMap2.get(key).add(d.getStudentId());
//						} else {
//							subjectIdsMap2.get(key).add(d.getStudentId());
//							g = gDtomap2.get(key);
//							g.setAllNumber(g.getAllNumber() + 1);
//						}
//					}
//				}
//			}
//			
//			// 混合
//			if (!gDtomap3.containsKey(guidZero)) {
//				g = new NewGkGroupDto();
//				g.setSubjectIds(guidZero);
//				g.setConditionName("混合");
//				g.setAllNumber(1);
//				gDtoList3.add(g);
//				gDtomap3.put(guidZero, g);
//				subjectIdsMap3.put(guidZero, new HashSet<String>());
//				subjectIdsMap3.get(guidZero).add(d.getStudentId());
//			} else {
//				subjectIdsMap3.get(guidZero).add(d.getStudentId());
//				g = gDtomap3.get(guidZero);
//				g.setAllNumber(g.getAllNumber() + 1);
//			}
//		}
//		//暂时不考虑学生错误的情况 只统计剩余人数
//		
//		Set<String> arrangeStuId = new HashSet<String>();// 已经排的学生
//		
//		if (CollectionUtils.isNotEmpty(oldGroupList)) {
//			for (NewGkDivideClass dd : oldGroupList) {
//				if(CollectionUtils.isNotEmpty(dd.getStudentList())){
//					arrangeStuId.addAll(dd.getStudentList());
//				}
//			}
//		}
//		if (CollectionUtils.isNotEmpty(gDtoList)) {
//			for (NewGkGroupDto dd : gDtoList) {
//				Set<String> stuIds = subjectIdsMap.get(dd.getSubjectIds());
//				if (stuIds != null) {
//					// 取得除去 arrangeStuId中剩下的学生
//					stuIds.removeAll(arrangeStuId);
//					dd.setLeftNumber(stuIds.size());
//				}
//
//			}
//		}
//		if (CollectionUtils.isNotEmpty(gDtoList2)) {
//			for (NewGkGroupDto dd : gDtoList2) {
//				Set<String> stuIds = subjectIdsMap2.get(dd.getSubjectIds());
//				if (stuIds != null) {
//					// 取得除去 arrangeStuId中剩下的学生
//					stuIds.removeAll(arrangeStuId);
//					dd.setLeftNumber(stuIds.size());
//				}
//
//			}
//		}
//		NewGkGroupDto gDto=null;
//		if (CollectionUtils.isNotEmpty(gDtoList3)) {
//			for (NewGkGroupDto dd : gDtoList3) {
//				Set<String> stuIds = subjectIdsMap3.get(dd.getSubjectIds());
//				// 取得除去 arrangeStuId中剩下的学生
//				stuIds.removeAll(arrangeStuId);
//				dd.setLeftNumber(stuIds.size());
//			}
//			gDto = gDtoList3.get(0);
//		}
//		// 根据总人数排序
//		SortUtils.DESC(gDtoList, "allNumber");
//		//根据总人数排序
//		SortUtils.DESC(gDtoList2, "allNumber");
		
		
		
//		if (CollectionUtils.isNotEmpty(gDtoList)) {
//			for (NewGkGroupDto item : gDtoList) {
//				groupMap.put(item.getSubjectIds(), item.getConditionName()+"-"+item.getLeftNumber());
//				if(item.getLeftNumber() == null || item.getLeftNumber() == 0) {
//					zeroSubStrs.append(item.getSubjectIds()+";");
//				}
//			}
//		} 
//		if (CollectionUtils.isNotEmpty(gDtoList2)) {
//			for (NewGkGroupDto item : gDtoList2) {
//				groupMap.put(item.getSubjectIds(), item.getConditionName()+"-"+item.getLeftNumber());
//				if(item.getLeftNumber() == null || item.getLeftNumber() == 0) {
//					zeroSubStrs.append(item.getSubjectIds()+";");
//				}
//			}
//		} 		
//		if(isShowHun){
//			if(gDto==null){
//				groupMap.put(guidZero, "混合-0");
//				zeroSubStrs.append(guidZero+";");
//			}else{
//				groupMap.put(guidZero, "混合-"+gDto.getLeftNumber());
//				if(gDto.getLeftNumber() == null || gDto.getLeftNumber() == 0) {
//					zeroSubStrs.append(guidZero+";");
//				}
//			}
//		}
		List<String> twoSubjectList;
		//allChooseSubjectIds排序
		Arrays.sort(allChooseSubjectIds.toArray(new String[0]));
		Map<String,String> groupMap2=new LinkedHashMap<String, String>();
		for(String threeIds: allChooseSubjectIds) {
			if(leftStuIdsMap.containsKey(threeIds) && CollectionUtils.isNotEmpty(leftStuIdsMap.get(threeIds))) {
				groupMap.put(threeIds, nameSet(courseMap, threeIds)+"-"+leftStuIdsMap.get(threeIds).size());
			}else {
				groupMap.put(threeIds, nameSet(courseMap, threeIds)+"-0");
				zeroSubStrs.append(threeIds+";");
			}
			//2科
			if(isHasTwo) {
				twoSubjectList = twoSubjectIdMap.get(threeIds);
				if(CollectionUtils.isNotEmpty(twoSubjectList)) {
					for (String twoIds : twoSubjectList) {
						if(leftStuIdsMap.containsKey(twoIds) && CollectionUtils.isNotEmpty(leftStuIdsMap.get(twoIds))) {
							groupMap2.put(twoIds, nameSet(courseMap, twoIds)+"-"+leftStuIdsMap.get(twoIds).size());
						}else {
							groupMap2.put(twoIds, nameSet(courseMap, twoIds)+"-0");
							zeroSubStrs.append(twoIds+";");
						}
					}
					
				}
			}
		}
		if(groupMap2.size()>0) {
			groupMap.putAll(groupMap2);
		}
		if(isShowHun){
			if(leftStuIdsMap.containsKey(guidZero) && CollectionUtils.isNotEmpty(leftStuIdsMap.get(guidZero))) {
				groupMap.put(guidZero, "混合-"+leftStuIdsMap.get(guidZero).size());
			}else {
				groupMap.put(guidZero,"混合-0");
				zeroSubStrs.append(guidZero+";");
			}
		}
	}
	
	
//	@ResponseBody
//	@RequestMapping("/divideGroup/findGroupMap")
//	@ControllerInfo(value = "组合下拉框")
//	public String findGroupList(@PathVariable String divideId){
//		NewGkDivide newDivide = newGkDivideService.findById(divideId);
//		Map<String, String> groupMap = new LinkedHashMap<String, String>();
//		JSONArray jsonArr = new JSONArray();
//		if (newDivide == null) {
//			return jsonArr.toString();
//		}
//		findGroupMap(newDivide, groupMap);
//		for(Entry<String, String> item:groupMap.entrySet()){
//			String key=item.getKey();
//			String value=item.getValue();
//			JSONObject obj=new JSONObject();
//			obj.put("id", key);
//			obj.put("name", value);
//		}
//		return jsonArr.toJSONString();
//	}

	@RequestMapping("/divideGroup/headSelectList/page")
	@ControllerInfo(value = "手动调整头部详情")
	public String showHeadSelectList(@PathVariable String divideId, ModelMap map) {
		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			return errorFtl(map, "分班方案不存在");
		}
		NewGkChoice newGkChoice = newGkChoiceService.findById(newDivide
				.getChoiceId());
		List<Course> list = SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()),
				new TR<List<Course>>() {
				});
		// 此处不再排序
		Map<String, Course> courseMap = new HashMap<String, Course>();
		for (Course course : list) {
			courseMap.put(course.getId(), course);
		}

		// 课程
		boolean flag = findGroupClass(newDivide, newGkChoice, map, courseMap);
		map.put("divideId", divideId);
		map.put("haserror", flag);
		return "/newgkelective/divideGroup/schedulingHeadSelectList.ftl";
	}

	@RequestMapping("/divideGroup/schedulingLeft/page")
	@ControllerInfo(value = "手动排班调整-左边")
	public String loadSchedulingLeft(@PathVariable String divideId,
			String subjectIds,String oldClassIds, ModelMap map) {
		Set<String> subjectIdSet = new HashSet<String>();
		String[] subjectIdArr = subjectIds.split(",");
		for (String s1 : subjectIdArr) {
			if (StringUtils.isNotBlank(s1)) {
				subjectIdSet.add(s1);
			}
		}
		Set<String> chooseClazzId=new HashSet<String>();
		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			return errorFtl(map, "分班方案不存在");
		}

		if (StringUtils.isBlank(subjectIds)) {
			addErrorFtlOperation(map, "返回", "/newgkelective/" + divideId
					+ "/divideClass/groupList", "#showList");
			return errorFtl(map, "没有选择组合可以调整！");
		}
		
		List<StudentResultDto> allStuDtoList1 = new ArrayList<StudentResultDto>();
		NewGkChoice newGkChoice = newGkChoiceService.findById(newDivide
				.getChoiceId());
		if (BaseConstants.ZERO_GUID.equals(subjectIds)) {
			allStuDtoList1 = findByChoiceSubjectIds(newGkChoice, null);
		} else {
			allStuDtoList1 = findByChoiceSubjectIds(newGkChoice, subjectIds);
		}
		 
		//过滤chooseClazzId
		List<StudentResultDto> allStuDtoList = new ArrayList<StudentResultDto>();
		if(CollectionUtils.isNotEmpty(chooseClazzId)){
			for(StudentResultDto oo:allStuDtoList1){
				if(chooseClazzId.contains(oo.getClassId())){
					allStuDtoList.add(oo);
				}
			}
		}else{
			allStuDtoList=allStuDtoList1;
		}

		List<String> stuList;
		Set<String> noStuId = new HashSet<String>();
		List<StudentResultDto> stuDtoList = new ArrayList<StudentResultDto>();
		if (CollectionUtils.isNotEmpty(allStuDtoList)) {
			// 已安排学生数据
			List<NewGkDivideClass> groupClassList = newGkDivideClassService
					.findByDivideIdAndClassTypeWithMaster(
							newDivide.getUnitId(),
							divideId,
							new String[] { NewGkElectiveConstant.CLASS_TYPE_0 }, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			if (CollectionUtils.isNotEmpty(groupClassList)) {
				for (NewGkDivideClass g : groupClassList) {
					stuList = g.getStudentList();
					if (CollectionUtils.isNotEmpty(stuList)) {
						noStuId.addAll(stuList);
					}
				}
				for (StudentResultDto s : allStuDtoList) {
					if (!noStuId.contains(s.getStudentId())) {
						stuDtoList.add(s);
					}
				}
			} else {
				stuDtoList = allStuDtoList;
			}
		}
		
		map.put("divideId", divideId);
		map.put("subjectIds", subjectIds);
		return showData("left", newDivide.getReferScoreId(), subjectIdSet,
				stuDtoList, map);

	}
	
	
	
	
	@RequestMapping("/divideGroup/schedulingLeft2/page")
	@ControllerInfo(value = "全手动组装混合-手动排班调整-左边")
	public String loadSchedulingLeft2(@PathVariable String divideId,
			String groupSubjectIds, ModelMap map) {
		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			return errorFtl(map, "分班方案不存在");
		}
		NewGkChoice newGkChoice = newGkChoiceService.findById(newDivide
				.getChoiceId());
		List<StudentResultDto> allStuDtoList = new ArrayList<StudentResultDto>();
		if(StringUtils.isBlank(groupSubjectIds)) {
			//没有选择组合--返回空数据
		}else {
			String[] groupSubjectIdArr=null;
			if(groupSubjectIds.indexOf("-")>-1) {
				groupSubjectIdArr = groupSubjectIds.split("-");
			}else {
				groupSubjectIdArr=new String[] {groupSubjectIds};
			}
			
			for(String s:groupSubjectIdArr) {
				List<StudentResultDto> allStuDtoList2 = findByChoiceSubjectIds(newGkChoice, s);
				if(CollectionUtils.isNotEmpty(allStuDtoList2)) {
					allStuDtoList.addAll(allStuDtoList2);
				}
			}
		}
		
		List<String> stuList;
		Set<String> noStuId = new HashSet<String>();
		List<StudentResultDto> stuDtoList = new ArrayList<StudentResultDto>();
		if (CollectionUtils.isNotEmpty(allStuDtoList)) {
			// 已安排学生数据
			List<NewGkDivideClass> groupClassList = newGkDivideClassService
					.findByDivideIdAndClassType(
							newDivide.getUnitId(),
							divideId,
							new String[] { NewGkElectiveConstant.CLASS_TYPE_0 }, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			if (CollectionUtils.isNotEmpty(groupClassList)) {
				for (NewGkDivideClass g : groupClassList) {
					stuList = g.getStudentList();
					if (CollectionUtils.isNotEmpty(stuList)) {
						noStuId.addAll(stuList);
					}
				}
				for (StudentResultDto s : allStuDtoList) {
					if (!noStuId.contains(s.getStudentId())) {
						stuDtoList.add(s);
					}
				}
			} else {
				stuDtoList = allStuDtoList;
			}
		}

		map.put("divideId", divideId);
		map.put("subjectIds", BaseConstants.ZERO_GUID);
		Set<String> subSet = new HashSet<String>();
		subSet.add(BaseConstants.ZERO_GUID);
		return showData("left", newDivide.getReferScoreId(), subSet,
				stuDtoList, map);
	}
	
	
	@RequestMapping("/divideGroup/schedulingRight/page")
	@ControllerInfo(value = "开班安排-手动排班调整-右边")
	public String loadSchedulingRight(@PathVariable String divideId,
			String subjectIds, String groupClassId, ModelMap map) {
		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			return errorFtl(map, "分班方案不存在");
		}
		if (StringUtils.isBlank(subjectIds)) {
			addErrorFtlOperation(map, "返回", "/newgkelective/" + divideId
					+ "/divideClass/groupList", "#showList");
			return errorFtl(map, "没有选择组合可以调整！");
		}
		Set<String> subjectIdSet = new HashSet<String>();
		if (!BaseConstants.ZERO_GUID.equals(subjectIds)) {
			String[] subjectIdArr = subjectIds.split(",");
			for (String s1 : subjectIdArr) {
				if (StringUtils.isNotBlank(s1)) {
					subjectIdSet.add(s1);
				}
			}
		}

		List<StudentResultDto> stuDtoList = new ArrayList<StudentResultDto>();
		if (StringUtils.isNotBlank(groupClassId)) {
			NewGkDivideClass group = newGkDivideClassService.findByIdWithMaster(
					newDivide.getUnitId(), groupClassId, true);
			if (group == null) {
				addErrorFtlOperation(map, "返回", "/newgkelective/" + divideId
						+ "/divideClass/groupList", "#showList");
				return errorFtl(map, "未找到选择的班级！");
			}
			NewGkChoice newGkChoice = newGkChoiceService.findById(newDivide
					.getChoiceId());
			List<StudentResultDto> allStuDtoList = findByChoiceSubjectIds(
					newGkChoice, subjectIds);
			//不考虑错误数据的话
			List<String> stuList = group.getStudentList();
			List<String> errorStuId = new ArrayList<String>();
			if (CollectionUtils.isNotEmpty(stuList)) {
				if (CollectionUtils.isNotEmpty(allStuDtoList)) {
					Map<String, StudentResultDto> stuMap = EntityUtils.getMap(
							allStuDtoList, StudentResultDto::getStudentId);
					for (String s : stuList) {
						if (stuMap.containsKey(s)) {
							stuDtoList.add(stuMap.get(s));
						} else {
							errorStuId.add(s);
						}
					}

				} else {
					// 该组合下没有学生 那么该班级下数据都不对
				}
			}
		}
		map.put("divideId", divideId);
		map.put("subjectIds", subjectIds);
		return showData("right", newDivide.getReferScoreId(), subjectIdSet,
				stuDtoList, map);
	}

	/**
	 * 
	 * @param scource
	 *            左右
	 * @param referScoreId
	 * @param subjectIdSet
	 * @param stuDtoList
	 * @param map
	 * @return
	 */
	private String showData(String scource, String referScoreId,
			Set<String> subjectIdSet, List<StudentResultDto> stuDtoList,
			ModelMap map) {
		// stuDtoList 各科成绩
		if (CollectionUtils.isNotEmpty(stuDtoList)) {
			if (StringUtils.isNotBlank(referScoreId)) {
				makeStudentSubjectScore(referScoreId, stuDtoList);
			}
			stuDtoList.forEach(e->{
				e.setChoResultStr(e.getResultList().stream().map(r -> String.valueOf(r.getSubjectName().charAt(0)))
						.reduce((x, y) -> x + y).orElse(""));
			});
		}
		List<Course> courseList = findShowCourseList(subjectIdSet);
		map.put("courseList", courseList);// 显示科目成绩
		int maxCount = stuDtoList.size();
		Map<String, Float> avgMap = new HashMap<String, Float>();// 平均分(包括语数英)
		Map<String, Float> allScoreMap = new HashMap<String, Float>();// 各科总分
																		// 用于计算平均分
		int manCount = countStu(stuDtoList, courseList, avgMap, allScoreMap);
		int womanCount = maxCount - manCount;
		map.put("maxCount", maxCount);
		map.put("avgMap", avgMap);
		map.put("manCount", manCount);
		map.put("womanCount", womanCount);
		map.put("allScoreMap", allScoreMap);
		map.put("stuDtoList", stuDtoList);
		map.put("rightOrLeft", scource);
		return "/newgkelective/divideGroup/schedulingList.ftl";
	}

	


	@RequestMapping("/divideGroup/schedulingEdit/page")
	@ControllerInfo(value = "手动排班调整-新增班级")
	public String schedulingEdit(@PathVariable String divideId,
			String subjectIds, String stuIdStr, ModelMap map) {

		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			return errorFtl(map, "分班方案不存在");
		}
		map.put("divideId", divideId);

		Set<String> subjectIdSet = new HashSet<String>();
		String[] subjectIdArr = subjectIds.split(",");
		for (String s1 : subjectIdArr) {
			if (StringUtils.isNotBlank(s1)) {
				subjectIdSet.add(s1);
			}
		}
		List<Course> courseList = SUtils.dt(courseRemoteService
				.findListByIds(subjectIdSet.toArray(new String[0])),
				new TR<List<Course>>() {
				});
		

		List<NewGkDivideClass> groupClassList = newGkDivideClassService
				.findClassBySubjectIds(newDivide.getUnitId(),
						divideId,
						NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_0, subjectIds, false);
		String groupName = "";
		if (BaseConstants.ZERO_GUID.equals(subjectIds)) {
			groupName = "混合";
		} else {
			Map<String, Course> courseMap = EntityUtils.getMap(courseList, "id");
			groupName = nameSet(courseMap, subjectIds);
		}

		NewGkDivideClass newGkDivideClass = new NewGkDivideClass();
		newGkDivideClass.setSubjectIds(subjectIds);
		int k = 1;
		if (CollectionUtils.isNotEmpty(groupClassList)) {
			List<String> groupNameList = EntityUtils.getList(groupClassList,
					NewGkDivideClass::getClassName);
			while (true) {
				if (!groupNameList.contains(groupName + k + "班")) {
					break;
				}
				k++;
			}
		}
		newGkDivideClass.setClassName(groupName + k + "班");
		map.put("newGkDivideClass", newGkDivideClass);
		map.put("subjectIds", subjectIds);
		map.put("divideId", divideId);
		map.put("stuIdStr", stuIdStr);
		return "/newgkelective/divideGroup/schedulingEdit.ftl";
	}

	@ResponseBody
	@RequestMapping("/divideGroup/saveClass")
	@ControllerInfo(value = "保存新增组合班级名称")
	public String saveGroupClass(@PathVariable String divideId,
			NewGkDivideClass newGkDivideClass, ModelMap map) {
		
		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			return error("分班方案");
		}
		if (StringUtils.isBlank(newGkDivideClass.getId())) {
			newGkDivideClass.setId(UuidUtils.generateUuid());
		}
		if (StringUtils.isBlank(newGkDivideClass.getSubjectIds())) {
			return error("数据错误！");
		}
		// 验证名字是否重复
		List<NewGkDivideClass> groupClassList = newGkDivideClassService
				.findClassBySubjectIds(newDivide.getUnitId(),
						divideId,
						NewGkElectiveConstant.CLASS_SOURCE_TYPE1,
						newGkDivideClass.getClassType(), newGkDivideClass.getSubjectIds(), false);
		if (CollectionUtils.isNotEmpty(groupClassList)) {
			List<String> groupNameList = EntityUtils.getList(
					groupClassList, NewGkDivideClass::getClassName);
			if (groupNameList.contains(newGkDivideClass.getClassName())) {
				return error("班级名称重复！");
			}
		}
		if(NewGkElectiveConstant.CLASS_TYPE_0.equals(newGkDivideClass.getClassType())) {
			if (BaseConstants.ZERO_GUID
					.equals(newGkDivideClass.getSubjectIds())) {
				newGkDivideClass
						.setSubjectType(NewGkElectiveConstant.SUBJTCT_TYPE_0);
			} else {
				String[] split = newGkDivideClass.getSubjectIds().split(",");
				if (split.length == 3) {
					newGkDivideClass
							.setSubjectType(NewGkElectiveConstant.SUBJTCT_TYPE_3);
				} else if (split.length == 2) {
					newGkDivideClass
							.setSubjectType(NewGkElectiveConstant.SUBJTCT_TYPE_2);
				} else {
					return error("数据错误！");
				}
			}
		}else {
			newGkDivideClass
			.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
			newGkDivideClass.setBatch("1");
		}
		newGkDivideClass.setDivideId(divideId);
		newGkDivideClass.setCreationTime(new Date());
		newGkDivideClass.setModifyTime(new Date());
		newGkDivideClass.setIsHand(NewGkElectiveConstant.IS_HAND_1);
		newGkDivideClass
				.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
		List<NewGkClassStudent> insertStudentList = new ArrayList<NewGkClassStudent>();
		List<NewGkDivideClass> insertClassList = new ArrayList<NewGkDivideClass>();
		insertClassList.add(newGkDivideClass);
		NewGkClassStudent gkGroupClassStu = null;
		if (StringUtils.isNotBlank(newGkDivideClass.getStuIdStr())) {
			String[] strIdArr = newGkDivideClass.getStuIdStr().split(",");
			for (String s : strIdArr) {
				gkGroupClassStu = initClassStudent(newDivide.getUnitId(), divideId, newGkDivideClass.getId(), s);
				
				insertStudentList.add(gkGroupClassStu);
			}
		}
		try {


			// 考虑班级学生重复：理论上新增班级 不会出现重复
			newGkDivideClassService.saveAllList(null, null,
					null, insertClassList, insertStudentList, false);
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！");
		}
		return success("保存成功！");
	}

	@ResponseBody
	@RequestMapping("/divideGroup/findClassByGroup")
	@ControllerInfo("查询组合班级")
	public String findGroupClass(@PathVariable String divideId,
			String subjectIds) {
		JSONArray jsonArr = new JSONArray();
		JSONObject jsonObj = null;
		if (StringUtils.isBlank(subjectIds)) {
			return jsonArr.toJSONString();
		}
		List<NewGkDivideClass> groupClassList = newGkDivideClassService
				.findClassBySubjectIds(this.getLoginInfo().getUnitId(),
						divideId,
						NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_0, subjectIds, false);
		if (CollectionUtils.isNotEmpty(groupClassList)) {
			for (NewGkDivideClass g : groupClassList) {
				jsonObj = new JSONObject();
				jsonObj.put("id", g.getId());
				jsonObj.put("name", g.getClassName());
				jsonArr.add(jsonObj);
			}
		} else {
			return jsonArr.toJSONString();
		}
		return jsonArr.toJSONString();
	}

	@ResponseBody
	@RequestMapping("/divideGroup/groupClassSaveStu")
	@ControllerInfo(value = "保存组合班级学生")
	public String groupClassSave(@PathVariable String divideId,
			String groupClassId, String stuId, ModelMap map) {
		try {
			NewGkDivide newDivide = newGkDivideService.findById(divideId);
			if (newDivide == null) {
				return errorFtl(map, "分班方案不存在");
			}
			if (StringUtils.isNotBlank(groupClassId)) {
				NewGkDivideClass divideClass = newGkDivideClassService
						.findById(newDivide.getUnitId(), groupClassId, false);
				if (divideClass == null
						|| (!divideClass.getDivideId().equals(divideId))) {
					return error("该班级不存在！");
				}
				String subjectIds = divideClass.getSubjectIds();
				Set<String> subjectIdSet = new HashSet<String>();
				String[] subjectIdArr = subjectIds.split(",");
				for (String s1 : subjectIdArr) {
					if (StringUtils.isNotBlank(s1)) {
						subjectIdSet.add(s1);
					}
				}
				Set<String> stuIds = new HashSet<String>();
				if (StringUtils.isNotBlank(stuId)) {
					String[] arr = stuId.split(",");
					if (arr.length > 0) {
						// 判断当前
						String error = checkGroupStu(newDivide.getChoiceId(),
								arr, subjectIdSet);
						if (StringUtils.isNotBlank(error)) {
							return error(error);
						}
					}
					for (int i = 0; i < arr.length; i++) {
						if (StringUtils.isNotBlank(arr[i])) {
							stuIds.add(arr[i]);
						}
					}

				}
				List<NewGkClassStudent> insertStudentList = new ArrayList<NewGkClassStudent>();
				NewGkClassStudent gkGroupClassStu;
				for (String s : stuIds) {
					gkGroupClassStu = initClassStudent(newDivide.getUnitId(), newDivide.getId(), groupClassId, s);
					
					insertStudentList.add(gkGroupClassStu);
				}
				List<NewGkDivideClass> updateClassList = new ArrayList<NewGkDivideClass>();
				updateClassList.add(divideClass);

				newGkDivideClassService.saveAllList(newDivide.getUnitId(), newDivide.getId(),
						new String[] { groupClassId }, updateClassList, insertStudentList, false);

			} else {
				return error("该班级不存在！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！");
		}
		return success("保存成功！");
	}

	@ResponseBody
	@RequestMapping("/divideGroup/autoOpenClass")
	@ControllerInfo(value = "根据开班数自动分组")
	public String autoOpenClassByClassNum(@PathVariable String divideId,
			String subjectIds, String groupSubjectIds,int openNum, String[] stuids, ModelMap map) {
		if (StringUtils.isBlank(subjectIds)) {
			return error("没有选中组合");
		}
		if (openNum <= 0) {
			return error("开设班级数应为正整数");
		}
		Set<String> subjectIdSet = new HashSet<String>();

		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			return errorFtl(map, "分班方案不存在");
		}

		// 组合名称
		String shortName = "";
		boolean isMix = false;
		if (BaseConstants.ZERO_GUID.equals(subjectIds)) {
			isMix = true;
			shortName = "混合";
		} else {
			String[] subjectIdArr = subjectIds.split(",");
			for (String s1 : subjectIdArr) {
				if (StringUtils.isNotBlank(s1)) {
					subjectIdSet.add(s1);
				}
			}
			List<Course> courseList = SUtils.dt(courseRemoteService
					.findListByIds(subjectIdSet.toArray(new String[0])),
					new TR<List<Course>>() {
					});
			Map<String, Course> subjectNameMap = EntityUtils.getMap(courseList,
					"id");
			shortName = nameSet(subjectNameMap, subjectIds);
		}
		NewGkChoice gkChoice = newGkChoiceService.findOne(newDivide
				.getChoiceId());
		List<NewGkDivideClass> groupClassList = newGkDivideClassService
				.findClassBySubjectIds(newDivide.getUnitId(),
						divideId,
						NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_0, null, true);

		Set<String> groupNames = EntityUtils
				.getSet(groupClassList, NewGkDivideClass::getClassName);

		Set<String> noStuId = new HashSet<String>();
		for (NewGkDivideClass s : groupClassList) {
			if (CollectionUtils.isNotEmpty(s.getStudentList())) {
				noStuId.addAll(s.getStudentList());
			}
		}
		List<StudentResultDto> allStuDtoList=new ArrayList<>();
		if(StringUtils.isNotBlank(groupSubjectIds)) {
			// 查询多种某种组合的学生结果--用于混合的
			String[] arr = groupSubjectIds.split("-");
			
			for(String aa:arr) {
				List<StudentResultDto> allStuDtoList1 = findByChoiceSubjectIds(gkChoice,
						aa);
				if(CollectionUtils.isNotEmpty(allStuDtoList1)) {
					allStuDtoList.addAll(allStuDtoList1);
				}
			}
		}else {
			// 查询某种组合的学生结果
			allStuDtoList = findByChoiceSubjectIds(gkChoice,
					subjectIds);
		}
		

		List<StudentResultDto> stuDtoList = new ArrayList<StudentResultDto>();
		boolean hasStuId = false;
		List<String> needStuIds = new ArrayList<String>();

		if (stuids != null && stuids.length > 0) {
			// 选中的学生组合中自动分班
			hasStuId = true;
			needStuIds = Arrays.asList(stuids);
		} else {
			// 所有组合
		}
		for (StudentResultDto dto : allStuDtoList) {
			if (noStuId.contains(dto.getStudentId())) {
				continue;
			}
			if (hasStuId) {
				if (needStuIds.contains(dto.getStudentId())) {
					stuDtoList.add(dto);
				}
			} else {
				stuDtoList.add(dto);
			}
		}

		if (CollectionUtils.isEmpty(stuDtoList)) {
			return success("没有学生需要分班");
		}
		
		try {
			// 根据先性别平均 后总成绩分数平均
			// 学生(每班学生)
			String refScoreId=newDivide.getReferScoreId();
			if(StringUtils.isBlank(refScoreId)){
				refScoreId=newGkReferScoreService.findDefaultIdByGradeId(gkChoice.getUnitId(), gkChoice.getGradeId());
			}
			List<String>[] array =autoStuIdToXzbId(refScoreId,stuDtoList, openNum);
			

			// 新增班级
			List<NewGkClassStudent> insertStudentList = new ArrayList<NewGkClassStudent>();
			List<NewGkDivideClass> insertClassList = new ArrayList<NewGkDivideClass>();
			NewGkDivideClass newGkDivideClass;
			NewGkClassStudent newGkClassStudent;
			int k = 1;
			String subjectType = subjectIdSet.size() + "";
			if (isMix) {
				subjectType = NewGkElectiveConstant.SUBJTCT_TYPE_0;
			}
			for (int i = 0; i < array.length; i++) {
				if (CollectionUtils.isNotEmpty(array[i])) {
					// 新增预排班
					newGkDivideClass = initNewGkDivideClass(divideId,
							subjectIds,NewGkElectiveConstant.CLASS_TYPE_0);
					while (true) {
						if (!groupNames.contains(shortName + k + "班")) {
							newGkDivideClass.setClassName(shortName + k + "班");
							newGkDivideClass.setOrderId(k);
							break;
						}
						k++;
					}
					newGkDivideClass.setSubjectType(subjectType);
					insertClassList.add(newGkDivideClass);
					for (String stuId : array[i]) {
						newGkClassStudent = initClassStudent(newDivide.getUnitId(), newDivide.getId(), newGkDivideClass.getId(), stuId);
						insertStudentList.add(newGkClassStudent);
					}
					groupNames.add(newGkDivideClass.getClassName());
				}
			}
			// 考虑班级学生重复：理论上新增班级 不会出现重复
			newGkDivideClassService.saveAllList(null, null,
					null, insertClassList, insertStudentList, false);

		} catch (Exception e) {
			e.printStackTrace();
			return error("分班失败！");
		}
		return success("分班成功！");
	}
	

	private String checkGroupStu(String choiceId, String[] stuId,
			Set<String> subjectIdSet) {
		List<NewGkChoResult> resultList = newGkChoResultService
				.findByKindTypeAndChoiceIdAndStudentIds(getLoginInfo().getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,choiceId, stuId);
		// 组装学生选课数据
		List<StudentResultDto> stuSubjectList = resultToDto(stuId, resultList);
		if (CollectionUtils.isEmpty(resultList)) {
			return "保存失败，所选的学生的选课记录已经不存在！";
		}
		Set<String> errorStuId = new HashSet<String>();
		for (StudentResultDto dto : stuSubjectList) {
			if (CollectionUtils.isEmpty(dto.getResultList())) {
				errorStuId.add(dto.getStudentId());
				continue;
			}
			Set<String> chooseSubjectIdSet = EntityUtils.getSet(
					dto.getResultList(), "subjectId");
			if (CollectionUtils.isEmpty(chooseSubjectIdSet)) {
				errorStuId.add(dto.getStudentId());
				continue;
			}
			if (CollectionUtils.union(chooseSubjectIdSet, subjectIdSet).size() == chooseSubjectIdSet
					.size()) {

			} else {
				errorStuId.add(dto.getStudentId());
				continue;
			}
		}
		String errorStr = "";
		if (errorStuId.size() > 0) {
			List<Student> stuList = SUtils.dt(studentRemoteService
					.findListByIds(errorStuId.toArray(new String[] {})),
					new TR<List<Student>>() {
					});
			for (Student s : stuList) {
				errorStr = errorStr + "、" + s.getStudentName();
			}
			if (StringUtils.isNotBlank(errorStr)) {
				errorStr = errorStr.substring(1);
			}
			errorStr = "保存失败，其中有" + errorStr.substring(1)
					+ "学生的选课组合与该班级科目组合不一致！";
		}

		return errorStr;
	}

	private List<StudentResultDto> resultToDto(String[] stuId,
			List<NewGkChoResult> resultList) {
		// key:studentId
		Map<String, List<String>> subjectByStudentId = new HashMap<String, List<String>>();
		if (CollectionUtils.isNotEmpty(resultList)) {
			for (NewGkChoResult result : resultList) {
				if (!subjectByStudentId.containsKey(result.getStudentId())) {
					subjectByStudentId.put(result.getStudentId(),
							new ArrayList<String>());
				}
				subjectByStudentId.get(result.getStudentId()).add(
						result.getSubjectId());
			}
		}
		List<StudentResultDto> returnList = new ArrayList<StudentResultDto>();
		StudentResultDto dto;
		NewGkChoResult rr;
		for (String s : stuId) {
			dto = new StudentResultDto();
			dto.setStudentId(s);
			dto.setResultList(new ArrayList<NewGkChoResult>());
			if (subjectByStudentId.containsKey(s)) {
				for (String ss : subjectByStudentId.get(s)) {
					rr = new NewGkChoResult();
					rr.setSubjectId(ss);
					dto.getResultList().add(rr);
				}
			}
		}
		return returnList;
	}

	@ResponseBody
	@RequestMapping("/divideGroup/moveGroup")
	@ControllerInfo(value = "解散")
	public String moveGroup(@PathVariable String divideId, String subjectIds,
			String dividClassId, ModelMap map) {
		try {
			String unitId=this.getLoginInfo().getUnitId();
			if (StringUtils.isNotBlank(dividClassId)) {
				
				List<NewGkDivideClass>ndclist = newGkDivideClassService.findListBy("relateId", dividClassId);
				Set<String> claids=new HashSet<String>();
				if(CollectionUtils.isNotEmpty(ndclist)) {
					claids = EntityUtils.getSet(ndclist, NewGkDivideClass::getId);
					
				}
				claids.add(dividClassId);
				newGkDivideClassService
						.deleteByClassIdIn(unitId,divideId, claids.toArray(new String[0]));
				
			} else if (StringUtils.isNotBlank(subjectIds)) {
				List<NewGkDivideClass> groupClassList = newGkDivideClassService
						.findClassBySubjectIds(this.getLoginInfo().getUnitId(),
								divideId,
								NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_0,
								subjectIds, false);
				if (CollectionUtils.isNotEmpty(groupClassList)) {
					Set<String> claids=new HashSet<String>();
					claids = EntityUtils.getSet(groupClassList, NewGkDivideClass::getId);
					List<NewGkDivideClass> ndclist = newGkDivideClassService.findListByIn("relateId", claids.toArray(new String[0]));
					
					if(CollectionUtils.isNotEmpty(ndclist)) {
						groupClassList.addAll(ndclist);
					}
					newGkDivideClassService.deleteByClassIdIn(unitId,divideId, EntityUtils
							.getSet(groupClassList, NewGkDivideClass::getId).toArray(
									new String[] {}));
				}
			}else{
				//删除所有
				List<NewGkDivideClass> groupClassList = newGkDivideClassService
						.findClassBySubjectIds(this.getLoginInfo().getUnitId(),
								divideId,
								NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_0,
								null, false);
				if (CollectionUtils.isNotEmpty(groupClassList)) {
					newGkDivideClassService.deleteByClassIdIn(unitId,divideId, EntityUtils
							.getSet(groupClassList, NewGkDivideClass::getId).toArray(
									new String[] {}));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return error("解散数据失败！");
		}
		return success("解散数据成功！");
	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/divideGroup/clearNotPerArrange")
	@ControllerInfo(value = "清除不符合数据")
	public String clearNotGroup(@PathVariable String divideId, ModelMap map) {
		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			return error("分班方案不存在");
		}
		NewGkChoice newGkChoice = newGkChoiceService.findOne(newDivide
				.getChoiceId());
		if (newGkChoice == null) {
			return error("该方案的选课数据不存在");
		}
		List<StudentResultDto> stuChooselist = newGkChoResultService
				.findGradeIdList(newGkChoice.getUnitId(),newGkChoice.getGradeId(),newGkChoice.getId(), null);
		// 已有数据
		List<NewGkDivideClass> groupClassList = newGkDivideClassService
				.findByDivideIdAndClassType(newDivide.getUnitId(),
						divideId,
						new String[] { NewGkElectiveConstant.CLASS_TYPE_0 }, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		if (CollectionUtils.isEmpty(groupClassList)) {
			return success("清除数据成功！");
		}
		Set<String> delClassIds = new HashSet<String>();
		List<NewGkDivideClass> updateGroup = new ArrayList<NewGkDivideClass>();
		if (CollectionUtils.isEmpty(stuChooselist)) {
			// 学生选课数据不存在 删除所有组合信息
			delClassIds = EntityUtils.getSet(groupClassList, NewGkDivideClass::getId);
			newGkDivideClassService.deleteByClassIdIn(newDivide.getUnitId(),divideId, delClassIds
					.toArray(new String[] {}));
		} else {

			Map<String, Set<String>> subjectIdsMap = new HashMap<String, Set<String>>();// 同组合下学生(包括2门)
			for (StudentResultDto d : stuChooselist) {
				// 选择满3门才算组合
				if (CollectionUtils.isEmpty(d.getResultList())) {
					continue;
				}
				Set<String> chooseSubjectIds = EntityUtils.getSet(
						d.getResultList(), "subjectId");
				if (chooseSubjectIds.size() == newGkChoice.getChooseNum()) {
					String ids = keySort(chooseSubjectIds);
					if (!subjectIdsMap.containsKey(ids)) {
						subjectIdsMap.put(ids, new HashSet<String>());
					}
					subjectIdsMap.get(ids).add(d.getStudentId());
					List<String> idsList = keySort2(chooseSubjectIds);
					if (CollectionUtils.isNotEmpty(idsList)) {
						for (String key : idsList) {
							if (!subjectIdsMap.containsKey(key)) {
								subjectIdsMap.put(key, new HashSet<String>());
							}
							subjectIdsMap.get(key).add(d.getStudentId());
						}
					}
				}
			}

			Set<String> stusIds;
			List<String> groupStudent;
			for (NewGkDivideClass groupClass : groupClassList) {
				if (subjectIdsMap.containsKey(groupClass.getSubjectIds())) {
					stusIds = subjectIdsMap.get(groupClass.getSubjectIds());
					groupStudent = groupClass.getStudentList();
					if (CollectionUtils.isNotEmpty(groupStudent)) {
						// 交集
						List<String> stuIdnow = (List<String>) CollectionUtils
								.intersection(groupStudent, stusIds);
						if (stuIdnow.size() != groupStudent.size()) {
							// 修改、
							if (CollectionUtils.isNotEmpty(stuIdnow)) {
								// 修改班级下学生
								groupClass.setStudentList(stuIdnow);
								updateGroup.add(groupClass);
							} else {
								// 当前为空
								delClassIds.add(groupClass.getId());
							}
						} else {
							// 正常
						}
					} else {
						// 当前为空
						delClassIds.add(groupClass.getId());
					}
				} else {
					delClassIds.add(groupClass.getId());
				}
			}
		}
		try {
			if (CollectionUtils.isNotEmpty(delClassIds)
					|| CollectionUtils.isNotEmpty(updateGroup)) {
				if (CollectionUtils.isNotEmpty(delClassIds)) {
					newGkDivideClassService.updateStu(
							newDivide.getUnitId(), divideId,delClassIds.toArray(new String[] {}), updateGroup);
				} else {
					newGkDivideClassService.updateStu(newDivide.getUnitId(), divideId,null, updateGroup);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return error("清除数据失败！");
		}
		return success("清除数据成功！");
	}

	@RequestMapping("/divideGroup/showStu/page")
	@ControllerInfo(value = "组合班级详情")
	public String showStu(@PathVariable String divideId, String subjectIds,
			String divideClassId,String type, ModelMap map) {
		// 去开班前
		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			addErrorFtlOperation(map, "返回", "/newgkelective/index/page",
					"#showList");
			return errorFtl(map, "分班方案不存在！");
		}
		List<NewGkDivideClass> divideClassList=new ArrayList<>();
		if("2".equals(type)) {
			//教学班
			divideClassList = newGkDivideClassService
					.findClassBySubjectIds(newDivide.getUnitId(),
							divideId,
							NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_2, subjectIds, false);
		}else {
			divideClassList = newGkDivideClassService
					.findClassBySubjectIds(newDivide.getUnitId(),
							divideId,
							NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_0, subjectIds, false);
			type=NewGkElectiveConstant.CLASS_TYPE_0;
		}
		map.put("divideClassList", divideClassList);
		map.put("divideClassId", divideClassId);
		map.put("divideId", divideId);
		map.put("subjectIds", subjectIds);
		map.put("type", type);
		return "/newgkelective/divideGroup/schedulingShowIndex.ftl";
	}

	@RequestMapping("/divideGroup/schedulingOne/page")
	@ControllerInfo(value = "单个班级详情")
	public String loadOneTable(@PathVariable String divideId,
			String divideClassId, ModelMap map) {
		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			addErrorFtlOperation(map, "返回", "/newgkelective/index/page",
					"#showList");
			return errorFtl(map, "分班方案不存在！");
		}
		NewGkDivideClass divideClass = newGkDivideClassService.findById(
				newDivide.getUnitId(), divideClassId, true);
		List<Course> courseList = new ArrayList<Course>();
		int maxCount = 0;
		double manRatio = 0;
		double womanRatio = 0;

		List<StudentResultDto> stuResultDtoList = new ArrayList<StudentResultDto>();
		Map<String, Float> avgMap = new HashMap<String, Float>();// 平均分
		map.put("dividClassId", divideClassId);
		if (divideClass == null
				|| StringUtils.isBlank(divideClass.getSubjectIds())) {

		} else {
			NewGkChoice newGkChoice = newGkChoiceService.findOne(newDivide
					.getChoiceId());
			String subjectIds = divideClass.getSubjectIds();
			Set<String> subjectIdSet = new HashSet<String>();
			String[] subjectIdArr = subjectIds.split(",");
			for (String s1 : subjectIdArr) {
				if (StringUtils.isNotBlank(s1)) {
					subjectIdSet.add(s1);
				}
			}
			courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdSet
					.toArray(new String[0])), new TR<List<Course>>() {
			});
			Course course = new Course();
			course.setId(NewGkElectiveConstant.ZCJ_SUBID);
			course.setSubjectName(NewGkElectiveConstant.ZCJ_SUBNAME);
			courseList.add(course);
			List<String> stuIds = divideClass.getStudentList();
			if (CollectionUtils.isNotEmpty(stuIds)) {
				stuResultDtoList = findByChoiceStudentId(newGkChoice,
						subjectIds, stuIds);
			}
			// 计算成绩

			if (CollectionUtils.isNotEmpty(stuResultDtoList)) {
				if (StringUtils.isNotBlank(newDivide.getReferScoreId())) {
					makeStudentSubjectScore(newDivide.getReferScoreId(),
							stuResultDtoList);
				}
				maxCount = stuResultDtoList.size();
				Map<String, Float> allScoreMap = new HashMap<String, Float>();// 各科总分
																				// 用于计算平均分
				int manCount = countStu(stuResultDtoList, courseList, avgMap,
						allScoreMap);
				manRatio = manCount * 100.0 / maxCount;
				BigDecimal b = new BigDecimal(manRatio);
				manRatio = b.setScale(1, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
				womanRatio = 100 - manRatio;
			}

		}

		map.put("courseList", courseList);
		map.put("maxCount", maxCount);
		map.put("manRatio", manRatio);
		map.put("womanRatio", womanRatio);
		map.put("stuResultDtoList", stuResultDtoList);
		map.put("avgMap", avgMap);
		return "/newgkelective/divideGroup/schedulingShowDetail.ftl";
	}

	private List<StudentResultDto> findByChoiceStudentId(
			NewGkChoice newGkChoice, String subjectIds, List<String> studentIds) {
		List<StudentResultDto> allStuDtoList = findByChoiceSubjectIds(
				newGkChoice, subjectIds);
		List<StudentResultDto> dtoList = new ArrayList<StudentResultDto>();
		for (StudentResultDto dto : allStuDtoList) {
			if (studentIds.contains(dto.getStudentId())) {
				dtoList.add(dto);
			}
		}
		return dtoList;
	}

	
	/************************************以下文理模式**************************************/
	
	
	/**
	 * 文理设置界面
	 * 
	 * @return
	 */
//	private String showWenliPartList(NewGkChoice newGkChoice,
//			NewGkDivide newDivide, ModelMap map) {
//		// 查询当前分班方案的已经设置
//		List<NewGkOpenSubject> openClassList = newGkOpenSubjectService
//				.findByDivideId(newDivide.getId());
//		// openClassList
//		if (CollectionUtils.isEmpty(openClassList)) {
//			return errorFtl(map, "分班方案中开设科目不存在");
//		}
//
//		Map<String, Course> courseMap = new HashMap<String, Course>();
//		List<NewGkDivideEx> exlist = newGkDivideExService
//				.findByDivideId(newDivide.getId());
//		Map<String, Map<String, NewGkDivideEx>> newGkDivideExMap = new HashMap<String, Map<String, NewGkDivideEx>>();
//		if (CollectionUtils.isNotEmpty(exlist)) {
//			for (NewGkDivideEx newGkDivideEx : exlist) {
//				if (!newGkDivideExMap.containsKey(newGkDivideEx.getGroupType())) {
//					newGkDivideExMap.put(newGkDivideEx.getGroupType(),
//							new HashMap<String, NewGkDivideEx>());
//				}
//				newGkDivideExMap.get(newGkDivideEx.getGroupType()).put(
//						newGkDivideEx.getSubjectType(), newGkDivideEx);
//			}
//		}
//		Set<String> subjectIds = new HashSet<String>();
//		Map<String, Map<String, Set<String>>> subjectGroupMap = new HashMap<String, Map<String, Set<String>>>();
//		for (NewGkOpenSubject open : openClassList) {
//			if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(open
//					.getSubjectType())
//					|| NewGkElectiveConstant.SUBJECT_TYPE_J.equals(open
//							.getSubjectType())) {
//				Map<String, Set<String>> m1 = subjectGroupMap.get(open
//						.getGroupType());
//				if (m1 == null) {
//					m1 = new HashMap<String, Set<String>>();
//				}
//				subjectGroupMap.put(open.getGroupType(), m1);
//				Set<String> m2 = m1.get(open.getSubjectType());
//				if (CollectionUtils.isEmpty(m2)) {
//					m2 = new HashSet<String>();
//				}
//				m1.put(open.getSubjectType(), m2);
//				m2.add(open.getSubjectId());
//				subjectIds.add(open.getSubjectId());
//			}
//
//		}
//		if (CollectionUtils.isNotEmpty(subjectIds)) {
//			List<Course> courseList = SUtils.dt(courseRemoteService
//					.findListByIds(subjectIds.toArray(new String[0])),
//					new TR<List<Course>>() {
//					});
//			courseMap = EntityUtils.getMap(courseList, Course::getId);
//		}
//		Map<String, NewGkDivideExDto> dtoMap = new HashMap<String, NewGkDivideExDto>();
//		NewGkDivideExDto exdto = null;
//		// 按理科，文科，语数英顺序
//		for (Entry<String, Map<String, Set<String>>> item : subjectGroupMap
//				.entrySet()) {
//			String key = item.getKey();
//			Map<String, Set<String>> value = item.getValue();
//			if (!dtoMap.containsKey(key)) {
//				exdto = new NewGkDivideExDto();
//				exdto.setGroupType(key);
//				exdto.setExMap(new HashMap<String, NewGkDivideEx>());
//				dtoMap.put(key, exdto);
//			}
//			Map<String, NewGkDivideEx> old = newGkDivideExMap.get(key);
//			for (Entry<String, Set<String>> item1 : value.entrySet()) {
//				String key1 = item1.getKey();
//				Set<String> value1 = item1.getValue();
//				String groupName = nameSet(courseMap, keySort(value1));
//				if ((NewGkElectiveConstant.DIVIDE_GROUP_2.equals(key) || NewGkElectiveConstant.DIVIDE_GROUP_3
//						.equals(key))
//						&& NewGkElectiveConstant.SUBJECT_TYPE_A.equals(key1)) {
//					dtoMap.get(key).setAllgroupName(groupName);
//				}
//				if (NewGkElectiveConstant.DIVIDE_GROUP_4.equals(key)
//						&& NewGkElectiveConstant.SUBJECT_TYPE_J.equals(key1)) {
//					dtoMap.get(key).setAllgroupName(groupName);
//				}
//				if (old != null && old.containsKey(key1)) {
//					old.get(key1).setGroupName(groupName);
//					old.get(key1).setSubjectType(key1);
//					old.get(key1).makeClassSumNumMap();
//					exdto.getExMap().put(key1, old.get(key1));
//				} else {
//					exdto.getExMap().put(key1, new NewGkDivideEx());
//					exdto.getExMap().get(key1).setGroupName(groupName);
//					exdto.getExMap().get(key1).setSubjectType(key1);
//				}
//			}
//
//		}
//		// 文科人数
//		int wNum = makeStuNum(newGkChoice,
//				NewGkElectiveConstant.DIVIDE_GROUP_3, subjectGroupMap);
//		// 理科人数
//		int lNum = makeStuNum(newGkChoice,
//				NewGkElectiveConstant.DIVIDE_GROUP_2, subjectGroupMap);
//		;
//
//		NewGkDivideExDto dto = null;
//		List<NewGkDivideExDto> dtoList = new ArrayList<NewGkDivideExDto>();
//		if (dtoMap.containsKey(NewGkElectiveConstant.DIVIDE_GROUP_2)) {
//			dto = dtoMap.get(NewGkElectiveConstant.DIVIDE_GROUP_2);
//			dto.setExList(new ArrayList<NewGkDivideEx>());
//			if (dto.getExMap()
//					.containsKey(NewGkElectiveConstant.SUBJECT_TYPE_A)) {
//				dto.getExMap().get(NewGkElectiveConstant.SUBJECT_TYPE_A)
//						.setStudentNum(lNum);
//				dto.getExList().add(
//						dto.getExMap()
//								.get(NewGkElectiveConstant.SUBJECT_TYPE_A));
//			}
//			if (dto.getExMap()
//					.containsKey(NewGkElectiveConstant.SUBJECT_TYPE_J)) {
//				dto.getExMap().get(NewGkElectiveConstant.SUBJECT_TYPE_J)
//						.setStudentNum(lNum);
//				dto.getExList().add(
//						dto.getExMap()
//								.get(NewGkElectiveConstant.SUBJECT_TYPE_J));
//			}
//			dtoList.add(dto);
//		}
//		if (dtoMap.containsKey(NewGkElectiveConstant.DIVIDE_GROUP_3)) {
//			dto = dtoMap.get(NewGkElectiveConstant.DIVIDE_GROUP_3);
//			dto.setExList(new ArrayList<NewGkDivideEx>());
//			if (dto.getExMap()
//					.containsKey(NewGkElectiveConstant.SUBJECT_TYPE_A)) {
//				dto.getExMap().get(NewGkElectiveConstant.SUBJECT_TYPE_A)
//						.setStudentNum(wNum);
//				dto.getExList().add(
//						dto.getExMap()
//								.get(NewGkElectiveConstant.SUBJECT_TYPE_A));
//			}
//			if (dto.getExMap()
//					.containsKey(NewGkElectiveConstant.SUBJECT_TYPE_J)) {
//				dto.getExMap().get(NewGkElectiveConstant.SUBJECT_TYPE_J)
//						.setStudentNum(wNum);
//				dto.getExList().add(
//						dto.getExMap()
//								.get(NewGkElectiveConstant.SUBJECT_TYPE_J));
//			}
//			dtoList.add(dto);
//		}
//		if (dtoMap.containsKey(NewGkElectiveConstant.DIVIDE_GROUP_4)) {
//			dto = dtoMap.get(NewGkElectiveConstant.DIVIDE_GROUP_4);
//			dto.setExList(new ArrayList<NewGkDivideEx>());
//			if (dto.getExMap()
//					.containsKey(NewGkElectiveConstant.SUBJECT_TYPE_J)) {
//				dto.getExMap().get(NewGkElectiveConstant.SUBJECT_TYPE_J)
//						.setStudentNum(wNum + lNum);
//				dto.getExList().add(
//						dto.getExMap()
//								.get(NewGkElectiveConstant.SUBJECT_TYPE_J));
//			}
//			dtoList.add(dto);
//		}
//		map.put("dtoList", dtoList);
//		map.put("newDivide", newDivide);
//
//		return "/newgkelective/divideTwo/groupIndex.ftl";
//	}

	/**
	 * 某种选课人数(主要用于文理科人数)
	 * 
	 * @param newGkChoice
	 * @return
	 */
//	private int makeStuNum(NewGkChoice newGkChoice, String groupType,
//			Map<String, Map<String, Set<String>>> subjectGroupMap) {
//		int wNum = 0;
//		if (subjectGroupMap.containsKey(groupType)) {
//			Map<String, Set<String>> subjectMap = subjectGroupMap
//					.get(groupType);
//			if (subjectMap.containsKey(NewGkElectiveConstant.SUBJECT_TYPE_A)) {
//				Set<String> set = subjectMap
//						.get(NewGkElectiveConstant.SUBJECT_TYPE_A);
//				List<StudentResultDto> stulist = findByChoiceSubjectIds(
//						newGkChoice, keySort(set));
//				if (CollectionUtils.isNotEmpty(stulist)) {
//					wNum = stulist.size();
//				}
//			}
//		}
//		return wNum;
//	}

	/**
	 * 文理参数保存
	 * 
	 * @param divideId
	 * @param dto
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/divideTwo/saveTwo")
	public String saveTwo(@PathVariable String divideId, NewGkDivideExDto dto) {
		List<NewGkDivideEx> exList = dto.getExList();
		if (CollectionUtils.isEmpty(exList)) {
			return error("没有需要保存数据");
		}
		if(isNowDivide(divideId)){
			//分班中
			return success("分班中");
		}
		// 先删后增
		for (NewGkDivideEx ex : exList) {
			if (StringUtils.isBlank(ex.getId())) {
				ex.setId(UuidUtils.generateUuid());
			}
			ex.setCreationTime(new Date());
			ex.setDivideId(divideId);
			ex.setModifyTime(new Date());
			ex.setHierarchyType("3");
		}
		try {
			newGkDivideExService.saveAndDel(divideId,
					exList.toArray(new NewGkDivideEx[] {}));
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败!");
		}
		return success("");
	}
	/************************************以上文理模式**************************************/

	private List<NewGkGroupDto> makeOldGroupDto(
			List<NewGkDivideClass> oldGroupList) {
		List<NewGkGroupDto> oldGDtoList = new ArrayList<NewGkGroupDto>();
		if (CollectionUtils.isNotEmpty(oldGroupList)) {
			Set<String> groupIds = new HashSet<String>();
			NewGkGroupDto g = null;
			// key:subjectIds
			Map<String, NewGkGroupDto> dto = new HashMap<String, NewGkGroupDto>();
			for (NewGkDivideClass group : oldGroupList) {
				groupIds.add(group.getId());
				if (StringUtils.isNotBlank(group.getSubjectIds())) {
					String ids = group.getSubjectIds();
					if (dto.containsKey(ids)) {
						g = dto.get(ids);
						g.getGkGroupClassList().add(group);
					} else {
						g = new NewGkGroupDto();
						g.setSubjectIds(ids);
						g.setGkGroupClassList(new ArrayList<NewGkDivideClass>());
						g.getGkGroupClassList().add(group);
						oldGDtoList.add(g);
						dto.put(ids, g);
					}
				}
			}
		}
		return oldGDtoList;
	}

	/**
	 * 页面需要显示的成绩 包括语数英成绩
	 * 
	 * @param subjectIdSet
	 * @return
	 */
	private List<Course> findShowCourseList(Set<String> subjectIdSet) {
		List<Course> courseList = SUtils.dt(courseRemoteService
				.findListByIds(subjectIdSet.toArray(new String[0])),
				new TR<List<Course>>() {
				});
		// 增加总成绩
		Course cource = new Course();
		// cource.setId(NewGkElectiveConstant.ZCJ_SUBID);
		// cource.setSubjectName(NewGkElectiveConstant.ZCJ_SUBNAME);
		cource.setId(NewGkElectiveConstant.YSY_SUBID);
		cource.setSubjectName(NewGkElectiveConstant.YSY_SUBNAME);
		courseList.add(cource);

		return courseList;
	}

	/**
	 * 返回男生人数
	 * 
	 * @param stuSubjectList
	 * @param courseList
	 * @param avgMap
	 *            各科平均分
	 * @return
	 */
	private int countStu(List<StudentResultDto> stuDtoList,
			List<Course> courseList, Map<String, Float> avgMap,
			Map<String, Float> allScoreMap) {
		int length = stuDtoList.size();
		for (Course c : courseList) {
			allScoreMap.put(c.getId(), 0.0f);
			avgMap.put(c.getId(), 0.0f);
		}
		if (length == 0) {
			return 0;
		}
		int manCount = 0;
		for (StudentResultDto stu : stuDtoList) {
			if ("1".equals(stu.getSex())) {
				manCount = manCount + 1;
			}
			Map<String, Float> scoreMap = stu.getSubjectScore();
			if (scoreMap.size() > 0) {
				for (String subjectId : scoreMap.keySet()) {
					if (avgMap.containsKey(subjectId)) {
						allScoreMap.put(subjectId, allScoreMap.get(subjectId)
								+ scoreMap.get(subjectId));
					}
				}
			}
		}
		for (String subjectId : avgMap.keySet()) {
			float avg = allScoreMap.get(subjectId) / length;
			avgMap.put(subjectId, avg);
		}
		return manCount;

	}

	

	
	//6选3 没有技术  6选3
	@ResponseBody
	@RequestMapping("/divideGroup/saveAutoSixOld")
	public String saveAutoSixOld(@PathVariable String divideId,String courseTeachNum,int xzbNum) {
		NewGkDivide divide = newGkDivideService.findById(divideId);
		NewGkChoice choice = newGkChoiceService.findById(divide.getChoiceId());
		List<StudentResultDto> allStulist = findByChoiceSubjectIds(choice, null);
		//最佳班级数量 必须按原来的行政班数量
		if(xzbNum<=0){
			List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(divide.getUnitId(),divide.getGradeId()), new TR<List<Clazz>>(){});
			xzbNum = clazzList.size();
		}
		if(xzbNum<=0){
			return error("设置安排行政班数量为正整数");
		}
		//暂时不考虑xzbNum <= 已经安排的2+x或者3+0
		Set<String> threeStuId=new HashSet<String>();
		Set<String> twoStuId=new HashSet<String>();
		Set<String> delClassId=new HashSet<String>();
		Map<String,String> twoSubIdBystuId=new HashMap<String,String>();
		int oldNum=0;//已经安排的行政班数量
		List<NewGkDivideClass> list = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divideId, null, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		
		Set<String> oldClassName=new HashSet<String>();
		
		int threeClassNum=0;
		
		if(CollectionUtils.isNotEmpty(list)){
			for(NewGkDivideClass clazz:list){
				if(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(clazz.getSubjectType())){
					//不考虑班级人数为0
					if(CollectionUtils.isNotEmpty(clazz.getStudentList())){
						threeStuId.addAll(clazz.getStudentList());
						oldNum++;
						threeClassNum++;
					}
					oldClassName.add(clazz.getClassName());
				}else if(NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(clazz.getSubjectType())){
					if(CollectionUtils.isNotEmpty(clazz.getStudentList())){
						for(String c:clazz.getStudentList()){
							twoSubIdBystuId.put(c, clazz.getSubjectIds());
							twoStuId.add(c);
						}
						oldNum++;
					}
					oldClassName.add(clazz.getClassName());
				}else{
					delClassId.add(clazz.getId());
				}
			}
		}
		
		//过滤3+0
		List<StudentResultDto> stulist=new ArrayList<StudentResultDto>();
		
		if(CollectionUtils.isNotEmpty(threeStuId) || CollectionUtils.isNotEmpty(twoStuId)){
			List<NewGkChoResult> oldChooseSubject;
			List<NewGkChoResult> newChooseSubject;
			String subIds;
			for(StudentResultDto dto:allStulist){
				if(threeStuId.contains(dto.getStudentId())){
					continue;
				}
				if(twoSubIdBystuId.containsKey(dto.getStudentId())){
					subIds = twoSubIdBystuId.get(dto.getStudentId());
					dto.setTwo(true);
					oldChooseSubject = dto.getResultList();
					newChooseSubject = new ArrayList<NewGkChoResult>();
					for(NewGkChoResult b:oldChooseSubject){
						if(subIds.indexOf(b.getSubjectId())>-1){
							continue;
						}
						newChooseSubject.add(b);
					}
					dto.setResultList(newChooseSubject);
				}
				stulist.add(dto);
			}
		}else{
			stulist=allStulist;
		}

		//根据剩余总人数以及班级数量取得最佳学生人数
		int leftXzbStu=allStulist.size()-twoStuId.size()-threeStuId.size();
		if(leftXzbStu<=0){
			return error("没有剩余学生需要安排的");
		}
		int leftXzbNum=xzbNum-oldNum;
		if(leftXzbNum<=0){
			return error("手动安排的组合班级数过多，没有多余可形成行政班");
		}
//		int min=0;
//		int max=0;
		int bestStuNnum=0;
		if(leftXzbNum!=0){
			bestStuNnum=leftXzbStu/leftXzbNum;
			//上下限浮动 考虑在3
//			min=bestStuNnum-3;
//			max=bestStuNnum+3;
		}else{
			bestStuNnum=divide.getGalleryful();
//			min=bestStuNnum-divide.getMaxGalleryful();
//			max=bestStuNnum+divide.getMaxGalleryful();
		}
		List<String> subjectIdsList = newGkChoRelationService.findByChoiceIdAndObjectType(divide.getUnitId(), 
				divide.getChoiceId(), NewGkElectiveConstant.CHOICE_TYPE_01);
		if(CollectionUtils.isEmpty(subjectIdsList ) || subjectIdsList.size()!=6){
			return error("目前使用条件6选3");
		}
		List<Course> courselist = SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()),new TR<List<Course>>(){});
		Set<String> allCourseIds=new HashSet<String>();
		allCourseIds.addAll(subjectIdsList);
		Map<String,Course> courseMap=new HashMap<String,Course>();
		courseMap=EntityUtils.getMap(courselist, Course::getId);
		//教师数量限制
		Map<String,Integer> teacherNumMap=new HashMap<String,Integer>();
		
		if(StringUtils.isNotBlank(courseTeachNum)){
			String[] arrayStr = courseTeachNum.split(",");
			for(String arr:arrayStr){
				String[] tt = arr.split("_");
				try{
					int num=Integer.parseInt(tt[1]);
					if(num>1){
						teacherNumMap.put(tt[0], num);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
				
		}
//		Set<String> ww=new HashSet<String>();
//		ww.add("3003");
//		ww.add("3006");
//		ww.add("3011");
//		
//		Set<String> ss=new HashSet<String>();
//		ss.add("3001");
//		ss.add("3002");
//		ss.add("3020");
//		Set<String> wenSet=new HashSet<String>();
//		Set<String> liSet=new HashSet<String>();
//		for(Course c:courselist){
//			courseMap.put(c.getId(), c);
//			if(ww.contains(c.getSubjectCode())){
//				wenSet.add(c.getId());
//			}
//			if(ss.contains(c.getSubjectCode())){
//				liSet.add(c.getId());
//			}
//			
//		}
		
		//不固定文理 穷举 6分2
		Map<Integer,List<Set<String>>> functionMap=new HashMap<Integer,List<Set<String>>>();
//		functionMap.put(1, new ArrayList<Set<String>>());
//		functionMap.get(1).add(wenSet);
//		functionMap.get(1).add(liSet);
		functionMap=divideSixToTwo(allCourseIds);
		if(functionMap==null || functionMap.size()<=0){
			return error("数据不对");
		}
		
		
		
		Map<Integer,List<GroupResult>> resultMap=new HashMap<Integer,List<GroupResult>>();
		//循环所有次数
		for(Entry<Integer, List<Set<String>>> item:functionMap.entrySet()){
			Integer key = item.getKey();
			List<Set<String>> value = item.getValue();
			Set<String> liSubjectIds = value.get(0);
			Set<String> wenSubjectIds = value.get(1);
			
			//理科subjectIds
			//文科subjectIds
			List<ThreeGroup> groupList=new ArrayList<ThreeGroup>();
			Map<String,TwoGroup> tMap=new HashMap<String,TwoGroup>();
			initThreeGroup(groupList, tMap, liSubjectIds, "第一种");
			initThreeGroup(groupList, tMap, wenSubjectIds, "第二种");
			
			RequiredParm requiredParm = new RequiredParm();
//			requiredParm.setMinReguired(min);
//			requiredParm.setCanAddReguired( max-min);
			requiredParm.setBestClassStuNum(bestStuNnum);
			requiredParm.setSingleNum(xzbNum-threeClassNum);//去除手动三科组合
			requiredParm.setXzbNum(leftXzbNum);
			Map<String,Set<ChooseStudent>> otherChooseStudentMap=new HashMap<String,Set<ChooseStudent>>();
			requiredParm.setOtherChooseStudentMap(otherChooseStudentMap);
			//学生数据放到stulist tMap
			initstudent(stulist, tMap,otherChooseStudentMap);
			SolveTwoSubject solve = new SolveTwoSubject();
			List<GroupResult> returnList;
			try {
				returnList = solve.solve(groupList, requiredParm);
				resultMap.put(key, returnList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(resultMap==null || resultMap.size()<=0){
			return error("没有找到合适的3+0、2+x数据");
		}
		int index=-1;
		double score=-1;
		for(Entry<Integer, List<GroupResult>> result:resultMap.entrySet()){
			Integer kk = result.getKey();
			double score1=doCount(leftXzbNum,xzbNum,bestStuNnum,result.getValue(),teacherNumMap);//负数
			if(index<0 || score1>score){
				index=kk;
				score=score1;
			}
		}
		List<GroupResult> goodReturnList=resultMap.get(index);
		Map<String,Integer> classNameMap=new HashMap<String,Integer>();
		List<NewGkDivideClass> insertClassList=new ArrayList<NewGkDivideClass>();
		List<NewGkClassStudent> insertStudentList=new ArrayList<NewGkClassStudent>();
		
		//组装到数据库--开设班级中
		NewGkClassStudent gkGroupClassStu;
		NewGkDivideClass divideClass;
		for(GroupResult g:goodReturnList){
			if(CollectionUtils.isEmpty(g.getStuIdList())){
				continue;
			}
			String subjectIds=keySort(g.getSubjectIds());
			divideClass = initNewGkDivideClass(divideId, subjectIds,NewGkElectiveConstant.CLASS_TYPE_0);
			if(!classNameMap.containsKey(subjectIds)){
				classNameMap.put(subjectIds, 1);
			}else{
				classNameMap.put(subjectIds, classNameMap.get(subjectIds)+1);
			}
			if(StringUtils.isNotBlank(g.getSubjectType())){
				String name=nameSet(courseMap, subjectIds);
				int j=classNameMap.get(subjectIds);
				String newName="";
				while(true){
					newName=name+j+"班";
					if(oldClassName.contains(newName)){
						j++;
					}else{
						break;
					}
				}
				classNameMap.put(subjectIds, j);
				j++;
				divideClass.setClassName(newName);
				divideClass.setSubjectType(g.getSubjectType());
			}else{
				continue;
			}
			insertClassList.add(divideClass);
			//学生
			for(ChooseStudent stu:g.getStuIdList()){
				gkGroupClassStu = initClassStudent(divide.getUnitId(), divide.getId(), divideClass.getId(), stu.getStudentId());
				insertStudentList.add(gkGroupClassStu);
			}
			
		}
		try{
			String[] divideClassIds=null;
			if(CollectionUtils.isNotEmpty(delClassId)){
				divideClassIds=delClassId.toArray(new String[]{});
			}
			newGkDivideClassService.saveAllList(divide.getUnitId(), divide.getId(),
					divideClassIds, insertClassList, insertStudentList, true);
//			//为了可以看到结果 直接看结果
//			newGkDivideClassService.saveAllList(divideId, insertClassList,
//					insertStudentList, divideClassIds);
		}catch(Exception e){
			return error("自动分配失败");
		}
		return success("自动分配成功");
	}
	
	@ResponseBody
	@RequestMapping("/updateXzbClassOrder")
	public String updateDivideClassOrder(String inf) {
		// inf 格式 A-B,C-D,
		String[] split = inf.split(",");
		
		Map<String, Integer> classIdOrderMap = new HashMap<>();
		
		for (String str : split) {
			if(str.contains("-")) {
				String[] orderInfoArr = str.split("-");
				
				classIdOrderMap.put(orderInfoArr[0], Integer.parseInt(orderInfoArr[1]));
			}
		}
		
		try {
			newGkDivideClassService.update(classIdOrderMap, "id", "orderId");
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		
		return returnSuccess();
	}
	
	
	@ResponseBody
	@RequestMapping("/restartXzbname")
	//根据序号重命名
	public String restartXzbname(@PathVariable String divideId,String fromSolve, String arrayId, String inf) {
		//取出来的数据已经排序啦
		String[] split = inf.split(",");
		
		Map<String, Integer> classIdOrderMap = new HashMap<>();
		int maxCount=0;
		for (String str : split) {
			if(str.contains("-")) {
				String[] orderInfoArr = str.split("-");
				int order=Integer.parseInt(orderInfoArr[1]);
				classIdOrderMap.put(orderInfoArr[0], order);
				if(maxCount<order) {
					maxCount=order;
				}
			}
		}
		List<NewGkDivideClass> list;
		if ("1".equals(fromSolve)) {
            list = newGkDivideClassService.findByDivideIdAndClassType(this.getLoginInfo().getUnitId(), arrayId, new String[] {NewGkElectiveConstant.CLASS_TYPE_1}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
        } else {
            list = newGkDivideClassService.findByDivideIdAndClassType(this.getLoginInfo().getUnitId(), divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_1}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
        }
		if(CollectionUtils.isEmpty(list)) {
			return returnSuccess();
		}
		for(NewGkDivideClass clazz:list) {
			if(classIdOrderMap.containsKey(clazz.getId())) {
				clazz.setClassName(classIdOrderMap.get(clazz.getId())+"班");
				clazz.setOrderId(classIdOrderMap.get(clazz.getId()));
			}else {
				maxCount++;
				clazz.setClassName(maxCount+"班");
				clazz.setOrderId(maxCount);
			}
			
		}
		
		try {
			newGkDivideClassService.saveAll(list.toArray(new NewGkDivideClass[] {}));
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		
		return returnSuccess();
	}
	
	/**
	 * 暂时计算方式
	 * 选择最佳的
	 * 1:如果必须保证行政班数量 xzbNum 
	 * 		超过xzbNum -1000*(实际开设班级数量-xzbNum)
	 * 		不超过xzbNum  -1*(xzbNum-实际开设班级数量)
	 * 2:场地数限制--当做原行政班数量xzbNum
	 * 		剩余单科班总数量
	 * 		超过xzbNum -1000*(实际开设班级数量-xzbNum)
	 * 		不超过xzbNum --暂时不给分
	 * 3:老师数量--暂时不考虑
	 * 		如果存在各科老师数量限制
	 * 		超出老师数量的科目   -10000*(实际开设班级数量-已有老师数量)
	 * 		不超过老师数量的科目  -暂时不给分
	 * 4:班级人数
	 * 	 行政班班级人数与单科班班级人数  只考虑容纳数 不考虑+或者-的值
	 * 	 -10*(（人数-标准人数）*（人数-标准人数）)
	 * 5:单科班各科目需要的教师数量的方差 -10*方差
	 */
	private double doCount(int leftXzbNum,int xzbNum,int bestStuName,List<GroupResult> value,Map<String,Integer> teacherNumMap) {
		double allSore=0d;
		int xzbFact=0;
		int jxbFact=0;
		Map<String,Integer> subjectClassNum=new HashMap<String,Integer>();//单科班各科目需要的教师数量
//		int two=0;
//		int one=0;
//		int allstu=0;
		for(GroupResult g:value){
			if(StringUtils.isNotBlank(g.getSubjectType())){
				xzbFact=xzbFact+1;
//				if(NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(g.getSubjectType())){
//					two=two+g.getStuIdList().size();
//				}
//				allstu=allstu+g.getStuIdList().size();
			}else{
				jxbFact=jxbFact+1;
				String[] subjectId = g.getSubjectIds().toArray(new String[]{});
				if(!subjectClassNum.containsKey(subjectId[0])){
					subjectClassNum.put(subjectId[0], 1);
				}else{
					subjectClassNum.put(subjectId[0], subjectClassNum.get(subjectId[0])+1);
				}
//				one=one+g.getStuIdList().size();
			}
			//4:班级人数计算的方式
			int stuNum = g.getStuIdList().size();
			allSore=allSore+10*(bestStuName-stuNum)*(bestStuName-stuNum);
		}
		//1与2
		if(xzbFact>leftXzbNum){
			allSore=allSore+(-1000)*(xzbFact-leftXzbNum);
		}else{
			allSore=allSore+(-1)*(leftXzbNum-xzbFact);
		}
		if(jxbFact>xzbNum){
			allSore=allSore+(-1000)*(jxbFact-xzbNum);
		}
//		System.out.println("行政班数量："+xzbFact+"教学班数量:"+jxbFact);
//		System.out.println("总人数"+allstu+"----2+x学生数量："+two+"教学班学生数量:"+one);
		//3
		for(Entry<String, Integer> item:subjectClassNum.entrySet()){
			if(teacherNumMap.containsKey(item.getKey())){
				if(teacherNumMap.get(item.getKey())<item.getValue()){
					allSore=allSore+(-10000)*(item.getValue()-teacherNumMap.get(item.getKey()));
				}
			}
		}
		
		//5
		double five_s = makeTwo_S(subjectClassNum);
		allSore=allSore+(-10)*five_s;
		return allSore;
	}
	//求方差
	private double makeTwo_S(Map<String,Integer> subjectClassNum){
		List<Integer> list=new ArrayList<Integer>();
		double all=0d;
		for(Entry<String, Integer> item:subjectClassNum.entrySet()){
			all=all+item.getValue();
			list.add(item.getValue());
		}
		double avg=all*1.0/(list.size());
		double sum=0d;
		for(Integer ii:list){
			sum=sum+(ii-avg)+(ii-avg);
		}
		sum=sum/(list.size());
		//保留2位小数
		sum=Math.round(sum*100)/100;
		return sum;
	}

	private Map<Integer, List<Set<String>>> divideSixToTwo(
			Set<String> allCourseIds) {
		if(allCourseIds.size()!=6){
			return null;
		}
		//切割6选3
		String[] courseIds = allCourseIds.toArray(new String[]{});
		
		Integer[] cSize = new Integer[courseIds.length];
        for(int i = 0;i < courseIds.length;i++){
        	cSize[i] = i;
        }
        CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize,3);
        Integer[][] result = combineAlgorithm.getResutl();
        Map<Integer,List<Set<String>>> functionMap=new HashMap<Integer,List<Set<String>>>();
        Set<String> set=new HashSet<String>();
        int index=0;
        for(int i = 0; i < result.length; i++) {
			Set<String> subjectIds1=new HashSet<String>();
			Set<String> subjectIds2=new HashSet<String>();
			for(int j = 0; j <result[i].length; j++) {
				subjectIds1.add(courseIds[result[i][j]]);
			}
			for(String s:allCourseIds){
				if(!subjectIds1.contains(s)){
					subjectIds2.add(s);
				}
			}
			String str1=keySort(subjectIds1);
			String str2=keySort(subjectIds2);
			if(set.contains(str1+"_"+str2) || set.contains(str2+"_"+str1)){
				continue;
			}
			set.add(str1+"_"+str2);
			set.add(str2+"_"+str1);
			functionMap.put(index, new ArrayList<Set<String>>());
			functionMap.get(index).add(subjectIds1);
			functionMap.get(index).add(subjectIds2);
			index++;
		}
		return functionMap;
	}

	private void initstudent(List<StudentResultDto> stulist,Map<String,TwoGroup> tMap,Map<String,Set<ChooseStudent>> otherChooseStudentMap){
		for(StudentResultDto dto:stulist){
			if(CollectionUtils.isNotEmpty(dto.getResultList())){
				Set<String> subjectIds = EntityUtils.getSet(dto.getResultList(), "subjectId");
				ChooseStudent stu=new ChooseStudent();
				stu.setChooseSubjectIds(subjectIds);
				stu.setStudentId(dto.getStudentId());
				if(dto.isTwo()){
					//subjectIds 这个数量为1个
					for(String s:subjectIds){
						if(!otherChooseStudentMap.containsKey(s)){
							otherChooseStudentMap.put(s, new HashSet<ChooseStudent>());
						}
						otherChooseStudentMap.get(s).add(stu);
					}
				}else{
					String s=keySort(subjectIds);
					if(tMap.containsKey(s)){
						tMap.get(s).getStudentList().add(stu);
						tMap.get(s).setStudentNum(tMap.get(s).getStudentNum()+1);
					}else{
						List<String> list = keySort2(subjectIds);
						for(String ss:list){
							if(tMap.containsKey(ss)){
								tMap.get(ss).getStudentList().add(stu);
								tMap.get(ss).setStudentNum(tMap.get(ss).getStudentNum()+1);
								break;
							}
						}
						
						//如果都不在tMap 将会丢失
					}
				}
			}
			
		}
	}

	private void initThreeGroup(List<ThreeGroup> groupList,Map<String,TwoGroup> tMap,Set<String> subjectIds,String name){
		//理科
		ThreeGroup group=new ThreeGroup();
		group.setName(name);
		group.setSubjectIds(subjectIds);
		groupList.add(group);
		TwoGroup twoGroup = initTwoGroup(group, subjectIds);
		group.setTwoGroup(twoGroup);
		String key=keySort(subjectIds);
		tMap.put(key, twoGroup);
		group.setTwoGroupList(new ArrayList<TwoGroup>());
		//liSubjectIds 两两
		List<String> list = keySort2(subjectIds);
		for(String s:list){
			List<String> setList = Arrays.asList(s.split(","));
			Set<String> set = new HashSet<String>(setList);
			twoGroup=initTwoGroup(group, set);
			group.getTwoGroupList().add(twoGroup);
			tMap.put(s, twoGroup);
		}
		
		
	}
	
	private TwoGroup initTwoGroup(ThreeGroup group,Set<String> subjectIds){
		TwoGroup g=new TwoGroup();
		g.setThreeGroup(group);
		g.setSubjectIds(subjectIds);
		g.setStudentList(new ArrayList<ChooseStudent>());
		return g;
	}
	
	/**
	 * 全固定 智能分行政班
	 * @param divideId
	 * @param xzbNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/divideGroup/saveAutoSixNew")
	public String saveAutoSix(@PathVariable String divideId,int xzbNum) {
		
		final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_" + divideId;
		final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_"
				+ divideId + "_mess";
		JSONObject on = new JSONObject();
		final NewGkDivide divide = newGkDivideService.findById(divideId);
		
		if (divide == null) {
			on.put("stat", "error");
			on.put("message", "该分班方案不存在");
			RedisUtils.del(new String[] { key, keyMess });
			return JSON.toJSONString(on);
		}
		
		if (NewGkElectiveConstant.IF_1.equals(divide.getStat())) {
			on.put("stat", "success");
			on.put("message", "已经分班成功,不能操作");
			RedisUtils.del(new String[] { key,  keyMess });
			return JSON.toJSONString(on);
		}
		
		// 判断分班 状态
		if (RedisUtils.get(key) == null) {
			// 开始智能分班
			RedisUtils.set(key, "start");
			RedisUtils.set(keyMess, "进行中");
		} else {
			on.put("stat", RedisUtils.get(key));
			on.put("message", RedisUtils.get(keyMess));
			if ("success".equals(RedisUtils.get(key))
					|| "error".equals(RedisUtils.get(key))) {
				RedisUtils.del(new String[] { key, keyMess });
			} 
			return JSON.toJSONString(on);
		}
		
		try {
			autoSix2New(divide, xzbNum, null);
//			autoSix(divide, courseTeachNum, xzbNum,marginNum);
			
		} catch (Exception e) {
			e.printStackTrace();
			on.put("stat", "error");
			on.put("message", "失败");
			RedisUtils.del(new String[] { key,  keyMess });
			return error("失败！" + e.getMessage());
		}

		return JSON.toJSONString(on);
		
	
		
	}

	@ResponseBody
	@RequestMapping("/divideGroup/saveAutoSix")
	public String saveAutoSix(@PathVariable String divideId,String courseTeachNum,int xzbNum,int marginNum) {
		
		final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_" + divideId;
		final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_"
				+ divideId + "_mess";
		JSONObject on = new JSONObject();
		final NewGkDivide divide = newGkDivideService.findById(divideId);
		
		if (divide == null) {
			on.put("stat", "error");
			on.put("message", "该分班方案不存在");
			RedisUtils.del(new String[] { key, keyMess });
			return JSON.toJSONString(on);
		}
		
		if (NewGkElectiveConstant.IF_1.equals(divide.getStat())) {
			on.put("stat", "success");
			on.put("message", "已经分班成功,不能操作");
			RedisUtils.del(new String[] { key,  keyMess });
			return JSON.toJSONString(on);
		}
		
		// 判断分班 状态
		if (RedisUtils.get(key) == null) {
			// 开始智能分班
			RedisUtils.set(key, "start");
			RedisUtils.set(keyMess, "进行中");
		} else {
			on.put("stat", RedisUtils.get(key));
			on.put("message", RedisUtils.get(keyMess));
			if ("success".equals(RedisUtils.get(key))
					|| "error".equals(RedisUtils.get(key))) {
				RedisUtils.del(new String[] { key, keyMess });
			} 
			return JSON.toJSONString(on);
		}
		
		try {
			autoSix2(divide, courseTeachNum, xzbNum, marginNum, null);
//			autoSix(divide, courseTeachNum, xzbNum,marginNum);
			
		} catch (Exception e) {
			e.printStackTrace();
			on.put("stat", "error");
			on.put("message", "失败");
			RedisUtils.del(new String[] { key,  keyMess });
			return error("失败！" + e.getMessage());
		}

		return JSON.toJSONString(on);
		
	
		
	}
	
//	private void autoSix(NewGkDivide divide,String courseTeachNum,int xzbNum,int marginNum){
//		final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_" + divide.getId();
//		final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_"
//				+ divide.getId() + "_mess";
//		
//		
//		NewGkChoice choice = newGkChoiceService.findById(divide.getChoiceId());
//		//所有学生
//		List<StudentResultDto> allStulist = findByChoiceSubjectIds(choice, null);
//		//最佳班级数量 必须按原来的行政班数量
//		if(xzbNum<=0){
//			List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(divide.getUnitId(),divide.getGradeId()), new TR<List<Clazz>>(){});
//			xzbNum = clazzList.size();
//		}
//		if(xzbNum<=0){
//			RedisUtils.set(key, "error");
//			RedisUtils.set(keyMess, "设置安排行政班数量为正整数");
//			return;
//		}
//		//暂时不考虑xzbNum <= 已经安排的2+x或者3+0
//		Set<String> threeStuId=new HashSet<String>();
//		Set<String> twoStuId=new HashSet<String>();
//		Set<String> delClassId=new HashSet<String>();
//		Map<String,String> twoSubIdBystuId=new HashMap<String,String>();
//		int oldNum=0;//已经安排的行政班数量
//		List<NewGkDivideClass> list = newGkDivideClassService.findByDivideIdAndClassType(choice.getUnitId(), divide.getId(), null, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
//		
//		Set<String> oldClassName=new HashSet<String>();
//		
////		int threeClassNum=0;
//		if(CollectionUtils.isNotEmpty(list)){
//			for(NewGkDivideClass clazz:list){
//				if(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(clazz.getSubjectType())){
//					//不考虑班级人数为0
//					if(CollectionUtils.isNotEmpty(clazz.getStudentList())){
//						threeStuId.addAll(clazz.getStudentList());
//						oldNum++;
////						threeClassNum++;
//					}
//					oldClassName.add(clazz.getClassName());
//				}else if(NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(clazz.getSubjectType())){
//					if(CollectionUtils.isNotEmpty(clazz.getStudentList())){
//						for(String c:clazz.getStudentList()){
//							twoSubIdBystuId.put(c, clazz.getSubjectIds());
//							twoStuId.add(c);
//						}
//						oldNum++;
//					}
//					oldClassName.add(clazz.getClassName());
//				}else{
////					delClassId.add(clazz.getId());
//					//2+x全固定 不会进入这里 如果进入这边 一般就是数据有误 （暂时不考虑） 防止之后可能也需要在半固定 剩下学生组2+x
//					//混合也当做3+0去考虑 直接过滤
//					if(CollectionUtils.isNotEmpty(clazz.getStudentList())){
//						threeStuId.addAll(clazz.getStudentList());
//						oldNum++;
////						threeClassNum++;
//					}
//					oldClassName.add(clazz.getClassName());
//				}
//			}
//		}
//		
//		//过滤3+0
//		List<StudentResultDto> stulist=new ArrayList<StudentResultDto>();
//		
//		List<StudentResultDto> noArrangeStulist=new ArrayList<StudentResultDto>();
//		//key:选1，选2，选3，选X
//		Map<String,Integer> leftXNum=new HashMap<String,Integer>();
//		if(CollectionUtils.isNotEmpty(threeStuId) || CollectionUtils.isNotEmpty(twoStuId)){
//			List<NewGkChoResult> oldChooseSubject;
//			List<NewGkChoResult> newChooseSubject;
//			String subIds;
//			for(StudentResultDto dto:allStulist){
//				if(threeStuId.contains(dto.getStudentId())){
//					continue;
//				}
//				if(twoSubIdBystuId.containsKey(dto.getStudentId())){
//					subIds = twoSubIdBystuId.get(dto.getStudentId());
//					dto.setTwo(true);
//					oldChooseSubject = dto.getResultList();
//					newChooseSubject = new ArrayList<NewGkChoResult>();
//					Set<String> allChoose=new HashSet<String>();
//					String leftChoose=null;
//					for(NewGkChoResult b:oldChooseSubject){
//						allChoose.add(b.getSubjectId());
//						if(subIds.indexOf(b.getSubjectId())>-1){
//							continue;
//						}
//						newChooseSubject.add(b);
//						leftChoose=b.getSubjectId();
//					}
//					dto.setResultList(newChooseSubject);
//					if(allChoose.size()!=3 || StringUtils.isBlank(leftChoose)){
//						RedisUtils.set(key, "error");
//						RedisUtils.set(keyMess, dto.getStudentName()+"的选课数据有误");
//						return;
//					}
//					String stukey=keySort(allChoose)+","+leftChoose;
//					if(!leftXNum.containsKey(stukey)){
//						leftXNum.put(stukey, 1);
//					}else{
//						leftXNum.put(stukey, leftXNum.get(stukey)+1);
//					}
//
//					
//				}else{
//					noArrangeStulist.add(dto);
//				}
//				stulist.add(dto);
//			}
//		}else{
//			stulist=allStulist;
//			noArrangeStulist=allStulist;
//		}
//
//		//根据剩余总人数以及班级数量取得最佳学生人数
//		int leftXzbStu=allStulist.size()-twoStuId.size()-threeStuId.size();
//		if(leftXzbStu<=0){
//			RedisUtils.set(key, "success");
//			RedisUtils.set(keyMess, "没有剩余学生需要安排的");
//			return;
//		}
//		int leftXzbNum=xzbNum-oldNum;
//		if(leftXzbNum<=0){
//			RedisUtils.set(key, "error");
//			RedisUtils.set(keyMess, "手动安排的组合班级数过多，没有多余可形成行政班");
//			return;
//		}
//		
//		
//		List<String> subjectIdsList = newGkChoRelationService.findByChoiceIdAndObjectType(choice.getUnitId(), 
//				divide.getChoiceId(), NewGkElectiveConstant.CHOICE_TYPE_01);
//		if(CollectionUtils.isEmpty(subjectIdsList )){
//			
//			RedisUtils.set(key, "error");
//			RedisUtils.set(keyMess, "选课科目为空，请联系管理员");
//			return;
//		}
//		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdsList.toArray(new String[] {})), Course.class);
//		Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
//		
//		int maxGroup3ACountPerGroup2A=subjectIdsList.size()-2;//默认选课数量为3
//			
//		
//		Map<String,Integer> stuNumBySubjectidsMap=new HashMap<String,Integer>();
//		Map<String,List<String>> stuIdBySubjectidsMap=new HashMap<String,List<String>>();
//		for(StudentResultDto stu:noArrangeStulist){
//			Set<String> subSet = EntityUtils.getSet(stu.getResultList(),NewGkChoResult::getSubjectId);
//			String subIds=keySort(subSet);
//			if(stuNumBySubjectidsMap.containsKey(subIds)){
//				stuNumBySubjectidsMap.put(subIds, stuNumBySubjectidsMap.get(subIds)+1);
//			}else{
//				stuNumBySubjectidsMap.put(subIds, 1);
//				stuIdBySubjectidsMap.put(subIds, new ArrayList<String>());
//			}
//			stuIdBySubjectidsMap.get(subIds).add(stu.getStudentId());
//		}
//		List<List<String>> group3AList=new ArrayList<List<String>>();
//		for (Entry<String, Integer> item:stuNumBySubjectidsMap.entrySet()){
//			String[] keys = item.getKey().split(",");
//			List<String> keylist1=new ArrayList<String>();
//			List<String> keylist = Arrays.asList(keys);
//			keylist1.addAll(keylist);
//			keylist1.add(item.getValue()+"");
//			
//			group3AList.add(keylist1);
//		}
//		Set<String> tSubjectIds=new HashSet<String>();
//		List<List<String>> maxTeacherCountList=new ArrayList<List<String>>();
//		if(StringUtils.isNotBlank(courseTeachNum)){
//			String[] arrayStr = courseTeachNum.split(",");
//			for(String arr:arrayStr){
//				String[] tt = arr.split("_");
//				try{
//					int num=Integer.parseInt(tt[1]);
//					if(num>1){
//						tSubjectIds.add(tt[0]);
//						List<String> ss=new ArrayList<String>();
//						ss.add(tt[0]);
//						ss.add(num+"");
//						maxTeacherCountList.add(ss);
//					}
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//				
//			}
//				
//		}
//		//没有设置的科目班级限制 默认少于行政班数量
//		for(Course s:courseList){
//			if(!tSubjectIds.contains(s.getId())){
//				tSubjectIds.add(s.getId());
//				List<String> ss=new ArrayList<String>();
//				ss.add(s.getId());
//				ss.add(xzbNum+"");
//				maxTeacherCountList.add(ss);
//			}
//		}
//
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//
//				try {
//					int bestStuNnum=0;//平均人数
//					if(leftXzbNum!=0){
//						bestStuNnum=leftXzbStu/leftXzbNum;
//					}else{
//						bestStuNnum=divide.getGalleryful();
//					}
//					//误差范围
//					Integer sectionSizeMargin = marginNum;
////					sectionSizeMargin=4;
//					SectioningApp app=new SectioningApp();
//					SectioningAppDto sectioningAppDto=new SectioningAppDto();
//					//group3AList,maxGroup3ACountPerGroup2A,bestStuNnum,sectionSizeMargin,maxTeacherCountList,leftXzbNum
//					sectioningAppDto.setGroup3AList(group3AList);
//					sectioningAppDto.setMaxGroup3ACountPerGroup2A(maxGroup3ACountPerGroup2A);
//					sectioningAppDto.setSectionSizeMean(bestStuNnum);
//					sectioningAppDto.setSectionSizeMargin(sectionSizeMargin);
//					sectioningAppDto.setMaxTeacherCountList(maxTeacherCountList);
//					sectioningAppDto.setMaxRoomCount(leftXzbNum);
//					List<List<String>> pre1xList =new ArrayList<List<String>>();
//					
//					if(leftXNum.size()>0){
//						for(Entry<String, Integer> xx:leftXNum.entrySet()){
//							String[] aaa=xx.getKey().split(",");
//							List<String> nn=new ArrayList<String>();
//							nn.add(aaa[0]);
//							nn.add(aaa[1]);
//							nn.add(aaa[2]);
//							nn.add(xx.getValue()+"");
//							nn.add(aaa[3]);
//							pre1xList.add(nn);
//						}
//					}
//
//					sectioningAppDto.setPre1XList(pre1xList);
//					//排除不能组成的2+x 例如有些学校不喜欢物理政治组合--暂时不使用
//					List<List<String>> excludedGroup2AList =new ArrayList<List<String>>();
//					//少于30个人排除掉
//					//00000000000000000000000000000001,00000000000000000000000000000037
//					//00000000000000000000000000000003,00000000000000000000000000000037
//					//00000000000000000000000000000002,00000000000000000000000000000037
//					//00000000000000000000000000000006,00000000000000000000000000000020
//					//00000000000000000000000000000020,00000000000000000000000000000037
//					//00000000000000000000000000000003,00000000000000000000000000000020
////					List<String> si=new ArrayList<String>();
////					si.add("00000000000000000000000000000001");
////					si.add("00000000000000000000000000000037");
////					excludedGroup2AList.add(si);
////					si=new ArrayList<String>();
////					si.add("00000000000000000000000000000003");
////					si.add("00000000000000000000000000000037");
////					excludedGroup2AList.add(si);
////					si=new ArrayList<String>();
////					si.add("00000000000000000000000000000002");
////					si.add("00000000000000000000000000000037");
////					excludedGroup2AList.add(si);
////					si=new ArrayList<String>();
////					si.add("00000000000000000000000000000006");
////					si.add("00000000000000000000000000000020");
////					excludedGroup2AList.add(si);
////					si=new ArrayList<String>();
////					si.add("00000000000000000000000000000020");
////					si.add("00000000000000000000000000000037");
////					excludedGroup2AList.add(si);
////					si=new ArrayList<String>();
////					si.add("00000000000000000000000000000003");
////					si.add("00000000000000000000000000000020");
////					excludedGroup2AList.add(si);
//					
//					sectioningAppDto.setExcludedGroup2AList(excludedGroup2AList);
//
//					List<List<String>> result = app.solve(sectioningAppDto);
//					saveResult(divide,result, bestStuNnum, sectionSizeMargin,courseMap,stuIdBySubjectidsMap,oldClassName,delClassId);
//					
//					RedisUtils.set(key, "success");
//					RedisUtils.set(keyMess, "自动分配成功");
//					return ;
//				} catch (IOException e) {
//					e.printStackTrace();
//					RedisUtils.set(key, "error");
//					RedisUtils.set(keyMess, "自动分配失败");
//					return ;
//				}catch (Exception e) {
//					e.printStackTrace();
//					RedisUtils.set(key, "error");
//					RedisUtils.set(keyMess, "自动分配失败");
//					return ;
//				}
//				
//
//			}
//
//
//		}).start();
//
//	}
	
	private void autoSix2New(NewGkDivide divide,int xzbNum,String[] noTwoSubjectIds){
		final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_" + divide.getId();
		final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_"
				+ divide.getId() + "_mess";
		
		
		NewGkChoice choice = newGkChoiceService.findById(divide.getChoiceId());
		//所有学生
		List<StudentResultDto> allStulist = findByChoiceSubjectIds(choice, null);
		//最佳班级数量 必须按原来的行政班数量
		if(xzbNum<=0){
			List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(divide.getUnitId(),divide.getGradeId()), new TR<List<Clazz>>(){});
			xzbNum = clazzList.size();
		}
		if(xzbNum<=0){
			RedisUtils.set(key, "error");
			RedisUtils.set(keyMess, "设置安排行政班数量为正整数");
			return;
		}
		//暂时不考虑xzbNum <= 已经安排的2+x或者3+0
		Set<String> threeStuId=new HashSet<String>();
		Set<String> twoStuId=new HashSet<String>();
		Set<String> delClassId=new HashSet<String>();
		Map<String,String> twoSubIdBystuId=new HashMap<String,String>();
		int oldNum=0;//已经安排的行政班数量
		List<NewGkDivideClass> list = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(),divide.getId(), null, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		
		Set<String> oldClassName=new HashSet<String>();
		
//		int threeClassNum=0;
		if(CollectionUtils.isNotEmpty(list)){
			for(NewGkDivideClass clazz:list){
				if(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(clazz.getSubjectType())){
					//不考虑班级人数为0
					if(CollectionUtils.isNotEmpty(clazz.getStudentList())){
						threeStuId.addAll(clazz.getStudentList());
						oldNum++;
//						threeClassNum++;
					}
					oldClassName.add(clazz.getClassName());
				}else if(NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(clazz.getSubjectType())){
					if(CollectionUtils.isNotEmpty(clazz.getStudentList())){
						for(String c:clazz.getStudentList()){
							twoSubIdBystuId.put(c, clazz.getSubjectIds());
							twoStuId.add(c);
						}
						oldNum++;
					}
					oldClassName.add(clazz.getClassName());
				}else{
//					delClassId.add(clazz.getId());
					//2+x全固定 不会进入这里 如果进入这边 一般就是数据有误 （暂时不考虑） 防止之后可能也需要在半固定 剩下学生组2+x
					//混合也当做3+0去考虑 直接过滤
					if(CollectionUtils.isNotEmpty(clazz.getStudentList())){
						threeStuId.addAll(clazz.getStudentList());
						oldNum++;
//						threeClassNum++;
					}
					oldClassName.add(clazz.getClassName());
				}
			}
		}
		
		//过滤3+0
		List<StudentResultDto> stulist=new ArrayList<StudentResultDto>();
		
		List<StudentResultDto> noArrangeStulist=new ArrayList<StudentResultDto>();
		//key:选1，选2，选3，选X
		Map<String,Integer> leftXNum=new HashMap<String,Integer>();
		if(CollectionUtils.isNotEmpty(threeStuId) || CollectionUtils.isNotEmpty(twoStuId)){
			List<NewGkChoResult> oldChooseSubject;
			List<NewGkChoResult> newChooseSubject;
			String subIds;
			for(StudentResultDto dto:allStulist){
				if(threeStuId.contains(dto.getStudentId())){
					continue;
				}
				if(twoSubIdBystuId.containsKey(dto.getStudentId())){
					subIds = twoSubIdBystuId.get(dto.getStudentId());
					dto.setTwo(true);
					oldChooseSubject = dto.getResultList();
					newChooseSubject = new ArrayList<NewGkChoResult>();
					Set<String> allChoose=new HashSet<String>();
					String leftChoose=null;
					for(NewGkChoResult b:oldChooseSubject){
						allChoose.add(b.getSubjectId());
						if(subIds.indexOf(b.getSubjectId())>-1){
							continue;
						}
						newChooseSubject.add(b);
						leftChoose=b.getSubjectId();
					}
					dto.setResultList(newChooseSubject);
					if(allChoose.size()!=3 || StringUtils.isBlank(leftChoose)){
						RedisUtils.set(key, "error");
						RedisUtils.set(keyMess, dto.getStudentName()+"的选课数据有误");
						return;
					}
					String stukey=keySort(allChoose)+","+leftChoose;
					if(!leftXNum.containsKey(stukey)){
						leftXNum.put(stukey, 1);
					}else{
						leftXNum.put(stukey, leftXNum.get(stukey)+1);
					}

					
				}else{
					noArrangeStulist.add(dto);
				}
				stulist.add(dto);
			}
		}else{
			stulist=allStulist;
			noArrangeStulist=allStulist;
		}

		//根据剩余总人数以及班级数量取得最佳学生人数
		int leftXzbStu=allStulist.size()-twoStuId.size()-threeStuId.size();
		if(leftXzbStu<=0){
			RedisUtils.set(key, "success");
			RedisUtils.set(keyMess, "没有剩余学生需要安排的");
			return;
		}
		int leftXzbNum=xzbNum-oldNum;
		if(leftXzbNum<=0){
			RedisUtils.set(key, "error");
			RedisUtils.set(keyMess, "手动安排的组合班级数过多，没有多余可形成行政班");
			return;
		}
		
		
		List<String> subjectIdsList = newGkChoRelationService.findByChoiceIdAndObjectType(choice.getUnitId(), 
				divide.getChoiceId(), NewGkElectiveConstant.CHOICE_TYPE_01);
		if(CollectionUtils.isEmpty(subjectIdsList )){
			
			RedisUtils.set(key, "error");
			RedisUtils.set(keyMess, "选课科目为空，请联系管理员");
			return;
		}
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdsList.toArray(new String[] {})), Course.class);
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
		
		int maxGroup3ACountPerGroup2A=subjectIdsList.size()-2;//默认选课数量为3
			
		
		Map<String,Integer> stuNumBySubjectidsMap=new HashMap<String,Integer>();
		Map<String,List<String>> stuIdBySubjectidsMap=new HashMap<String,List<String>>();
		for(StudentResultDto stu:noArrangeStulist){
			Set<String> subSet = EntityUtils.getSet(stu.getResultList(),NewGkChoResult::getSubjectId);
			String subIds=keySort(subSet);
			if(stuNumBySubjectidsMap.containsKey(subIds)){
				stuNumBySubjectidsMap.put(subIds, stuNumBySubjectidsMap.get(subIds)+1);
			}else{
				stuNumBySubjectidsMap.put(subIds, 1);
				stuIdBySubjectidsMap.put(subIds, new ArrayList<String>());
			}
			stuIdBySubjectidsMap.get(subIds).add(stu.getStudentId());
		}
		List<List<String>> group3AList=new ArrayList<List<String>>();
		Set<String> chooseSubjectIds=new HashSet<>();
		for (Entry<String, Integer> item:stuNumBySubjectidsMap.entrySet()){
			String[] keys = item.getKey().split(",");
			List<String> keylist1=new ArrayList<String>();
			List<String> keylist = Arrays.asList(keys);
			keylist1.addAll(keylist);
			keylist1.add(item.getValue()+"");
			chooseSubjectIds.addAll(keylist);
			group3AList.add(keylist1);
		}
		List<List<String>> maxTeacherCountList=new ArrayList<List<String>>();
		

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					int bestStuNnum=0;//平均人数
					if(leftXzbNum!=0){
						bestStuNnum=leftXzbStu/leftXzbNum;
					}else{
						bestStuNnum=divide.getGalleryful();
					}
					List<List<String>> resultBest=null;
					double[] s2 = null;//极差 ，方差
					//20%浮动人数
					for(int ii=3;ii<=bestStuNnum*0.2;ii++) {
						//误差范围
						Integer sectionSizeMargin = ii;
						
						Sectioning2A1XInput s2a1xInput=new Sectioning2A1XInput();
//						s2a1xInput.setMaxGroup3ACountPerGroup2A(maxGroup3ACountPerGroup2A);
						s2a1xInput.setGroup3AList(group3AList);
						s2a1xInput.setSectionSizeMean(bestStuNnum);
						s2a1xInput.setSectionSizeMargin(sectionSizeMargin);
						s2a1xInput.setMaxTeacherCountList(maxTeacherCountList);
						s2a1xInput.setMaxRoomCount(leftXzbNum);
						List<List<String>> pre1xList =new ArrayList<List<String>>();
						
						if(leftXNum.size()>0){
							for(Entry<String, Integer> xx:leftXNum.entrySet()){
								String[] aaa=xx.getKey().split(",");
								List<String> nn=new ArrayList<String>();
								nn.add(aaa[0]);
								nn.add(aaa[1]);
								nn.add(aaa[2]);
								nn.add(xx.getValue()+"");
								nn.add(aaa[3]);
								pre1xList.add(nn);
							}
						}
						s2a1xInput.setPre1XList(pre1xList);
						//排除不能组成的2+x 例如有些学校不喜欢物理政治组合--暂时不使用
						List<List<String>> excludedGroup2AList =new ArrayList<List<String>>();
						if(noTwoSubjectIds!=null) {
							for(String s:noTwoSubjectIds) {
								String[] mm = s.split(",");
								excludedGroup2AList.add(Arrays.asList(mm));
							}
						}
						s2a1xInput.setExcludedGroup2AList(excludedGroup2AList);
						//防止某个科目没有人选
//						List<List<String>> sectionSizeList=calculateSectionSize(group3AList, bestStuNnum, sectionSizeMargin,EntityUtils.getSet(courseList, e->e.getId()));
//						List<List<String>> sectionSizeList=calculateSectionSize2(s2a1xInput, 3);
//						s2a1xInput.setSectionSizeList(sectionSizeList);
					
						SectioningApp2 app2=new SectioningApp2();
						//<选课1-2A><选课2-2A> <开班数> <选课1-3A><选课2-3A><选课3-3A><人数>
						List<List<String>> result =app2.makeResult(s2a1xInput,3);
						if(countOpenClassNum(result)!=leftXzbNum) {
							//结果行政班数量！=leftXzbNum 则舍弃
							continue;
						}
						if(resultBest==null) {
							resultBest=result;
							s2=findBestResult(result);
							if(s2[0]==0) {
								break;
							}
						}else {
							//判断resultBest 与 result比较
							//1:班级数量==leftXzbNum 3:极差2：每个班级人数浮动比例 方差 
							double[] mmArr = findBestResult(result);
							if(mmArr[0]==0) {
								break;
							}
							if(s2[0]>10 && mmArr[0]>10) {
								//比较极差
								if( mmArr[0]<s2[0]) {
									resultBest=result;
									s2=mmArr;
								}
							}else if(s2[0]>10 && mmArr[0]<=10) {
								//选择极差小的
								resultBest=result;
								s2=mmArr;
							}else if(s2[0]<=10 && mmArr[0]>10) {
								//取原来的
							}else {
								//比较方差
								if(mmArr[1]<s2[1]) {
									resultBest=result;
									s2=mmArr;
								}
							}
							
						}
					}
					if(resultBest==null) {
						RedisUtils.set(key, "error");
						RedisUtils.set(keyMess, "数据不适合2+x形式");
						return ;
					}
					
					//saveResult(divide,result, bestStuNnum, sectionSizeMargin,courseMap,stuIdBySubjectidsMap,oldClassName,delClassId);
					saveResult2(divide, resultBest, courseMap, stuIdBySubjectidsMap, oldClassName, delClassId);
					RedisUtils.set(key, "success");
					RedisUtils.set(keyMess, "自动分配成功");
					return ;
				} catch (IOException e) {
					e.printStackTrace();
					RedisUtils.set(key, "error");
					RedisUtils.set(keyMess, "自动分配失败");
					return ;
				}catch (Exception e) {
					e.printStackTrace();
					RedisUtils.set(key, "error");
					RedisUtils.set(keyMess, "自动分配失败");
					return ;
				}
				

			}


		}).start();

	}
	//每个班级人数方差
	private double[] findBestResult(List<List<String>> result) {
		//<选课1-2A><选课2-2A> <开班数> <选课1-3A><选课2-3A><选课3-3A><人数>
		Map<String,Integer> groupClassNum=new HashMap<>();
		Map<String,Integer> countGroupClassNum=new HashMap<>();
		List<Integer> classStuNum=new ArrayList<>();
		for(List<String> r:result) {
			String key=r.get(0)+","+r.get(1);
			int cNum = Integer.parseInt(r.get(2));
			if(!groupClassNum.containsKey(key)) {
				groupClassNum.put(key, cNum);
				countGroupClassNum.put(key, Integer.parseInt(r.get(6)));
			}else {
				countGroupClassNum.put(key, countGroupClassNum.get(key)+Integer.parseInt(r.get(6)));
			}
		}
		
		for(Entry<String, Integer> item:countGroupClassNum.entrySet()) {
			String key=item.getKey();
			int stuNum=item.getValue();
			int cNum=groupClassNum.get(key);
			classStuNum.addAll(CalculateSections.calculateSectionsByKnownCount(stuNum, cNum));
		}
		//计算极差
		int min=-1;
		int max=-1;
		for(Integer r:classStuNum) {
			if(min==-1) {
				min=r;
				max=r;
			}else {
				max=Math.max(max, r);
				min=Math.min(min, r);
			}
		}
		//计算方差
		double s2 = varianceImperative(classStuNum);
		double[] returnData=new double[] {max-min ,s2};
		return returnData;
	}
	public double varianceImperative(List<Integer> population) {		
		double average = 0.0;		
		for (int p : population) {			
			average += p;		
		}		
		average /= population.size(); 		
		double variance = 0.0;		
		for (double p : population) {			
			variance += (p - average) * (p - average);		
		}		
		return variance / population.size();	
	}

	
	public int countOpenClassNum(List<List<String>> result) {
		Map<String, Integer> groupClassNum=new HashMap<>();
		int classNum=0;
		for(List<String> r:result) {
			String key=r.get(0)+","+r.get(1);
			int cNum = Integer.parseInt(r.get(2));
			
			if(!groupClassNum.containsKey(key)) {
				groupClassNum.put(key, cNum);
				classNum=classNum+cNum;
			}
		}
		return classNum;
	}
	
	private void autoSix2(NewGkDivide divide,String courseTeachNum,int xzbNum,int marginNum,String[] noTwoSubjectIds){
		final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_" + divide.getId();
		final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_"
				+ divide.getId() + "_mess";
		
		
		NewGkChoice choice = newGkChoiceService.findById(divide.getChoiceId());
		//所有学生
		List<StudentResultDto> allStulist = findByChoiceSubjectIds(choice, null);
		//最佳班级数量 必须按原来的行政班数量
		if(xzbNum<=0){
			List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(divide.getUnitId(),divide.getGradeId()), new TR<List<Clazz>>(){});
			xzbNum = clazzList.size();
		}
		if(xzbNum<=0){
			RedisUtils.set(key, "error");
			RedisUtils.set(keyMess, "设置安排行政班数量为正整数");
			return;
		}
		//暂时不考虑xzbNum <= 已经安排的2+x或者3+0
		Set<String> threeStuId=new HashSet<String>();
		Set<String> twoStuId=new HashSet<String>();
		Set<String> delClassId=new HashSet<String>();
		Map<String,String> twoSubIdBystuId=new HashMap<String,String>();
		int oldNum=0;//已经安排的行政班数量
		List<NewGkDivideClass> list = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(),divide.getId(), null, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		
		Set<String> oldClassName=new HashSet<String>();
		
//		int threeClassNum=0;
		if(CollectionUtils.isNotEmpty(list)){
			for(NewGkDivideClass clazz:list){
				if(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(clazz.getSubjectType())){
					//不考虑班级人数为0
					if(CollectionUtils.isNotEmpty(clazz.getStudentList())){
						threeStuId.addAll(clazz.getStudentList());
						oldNum++;
//						threeClassNum++;
					}
					oldClassName.add(clazz.getClassName());
				}else if(NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(clazz.getSubjectType())){
					if(CollectionUtils.isNotEmpty(clazz.getStudentList())){
						for(String c:clazz.getStudentList()){
							twoSubIdBystuId.put(c, clazz.getSubjectIds());
							twoStuId.add(c);
						}
						oldNum++;
					}
					oldClassName.add(clazz.getClassName());
				}else{
//					delClassId.add(clazz.getId());
					//2+x全固定 不会进入这里 如果进入这边 一般就是数据有误 （暂时不考虑） 防止之后可能也需要在半固定 剩下学生组2+x
					//混合也当做3+0去考虑 直接过滤
					if(CollectionUtils.isNotEmpty(clazz.getStudentList())){
						threeStuId.addAll(clazz.getStudentList());
						oldNum++;
//						threeClassNum++;
					}
					oldClassName.add(clazz.getClassName());
				}
			}
		}
		
		//过滤3+0
		List<StudentResultDto> stulist=new ArrayList<StudentResultDto>();
		
		List<StudentResultDto> noArrangeStulist=new ArrayList<StudentResultDto>();
		//key:选1，选2，选3，选X
		Map<String,Integer> leftXNum=new HashMap<String,Integer>();
		if(CollectionUtils.isNotEmpty(threeStuId) || CollectionUtils.isNotEmpty(twoStuId)){
			List<NewGkChoResult> oldChooseSubject;
			List<NewGkChoResult> newChooseSubject;
			String subIds;
			for(StudentResultDto dto:allStulist){
				if(threeStuId.contains(dto.getStudentId())){
					continue;
				}
				if(twoSubIdBystuId.containsKey(dto.getStudentId())){
					subIds = twoSubIdBystuId.get(dto.getStudentId());
					dto.setTwo(true);
					oldChooseSubject = dto.getResultList();
					newChooseSubject = new ArrayList<NewGkChoResult>();
					Set<String> allChoose=new HashSet<String>();
					String leftChoose=null;
					for(NewGkChoResult b:oldChooseSubject){
						allChoose.add(b.getSubjectId());
						if(subIds.indexOf(b.getSubjectId())>-1){
							continue;
						}
						newChooseSubject.add(b);
						leftChoose=b.getSubjectId();
					}
					dto.setResultList(newChooseSubject);
					if(allChoose.size()!=3 || StringUtils.isBlank(leftChoose)){
						RedisUtils.set(key, "error");
						RedisUtils.set(keyMess, dto.getStudentName()+"的选课数据有误");
						return;
					}
					String stukey=keySort(allChoose)+","+leftChoose;
					if(!leftXNum.containsKey(stukey)){
						leftXNum.put(stukey, 1);
					}else{
						leftXNum.put(stukey, leftXNum.get(stukey)+1);
					}

					
				}else{
					noArrangeStulist.add(dto);
				}
				stulist.add(dto);
			}
		}else{
			stulist=allStulist;
			noArrangeStulist=allStulist;
		}

		//根据剩余总人数以及班级数量取得最佳学生人数
		int leftXzbStu=allStulist.size()-twoStuId.size()-threeStuId.size();
		if(leftXzbStu<=0){
			RedisUtils.set(key, "success");
			RedisUtils.set(keyMess, "没有剩余学生需要安排的");
			return;
		}
		int leftXzbNum=xzbNum-oldNum;
		if(leftXzbNum<=0){
			RedisUtils.set(key, "error");
			RedisUtils.set(keyMess, "手动安排的组合班级数过多，没有多余可形成行政班");
			return;
		}
		
		
		List<String> subjectIdsList = newGkChoRelationService.findByChoiceIdAndObjectType(choice.getUnitId(), 
				divide.getChoiceId(), NewGkElectiveConstant.CHOICE_TYPE_01);
		if(CollectionUtils.isEmpty(subjectIdsList )){
			
			RedisUtils.set(key, "error");
			RedisUtils.set(keyMess, "选课科目为空，请联系管理员");
			return;
		}
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdsList.toArray(new String[] {})), Course.class);
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
		
		int maxGroup3ACountPerGroup2A=subjectIdsList.size()-2;//默认选课数量为3
			
		
		Map<String,Integer> stuNumBySubjectidsMap=new HashMap<String,Integer>();
		Map<String,List<String>> stuIdBySubjectidsMap=new HashMap<String,List<String>>();
		for(StudentResultDto stu:noArrangeStulist){
			Set<String> subSet = EntityUtils.getSet(stu.getResultList(),NewGkChoResult::getSubjectId);
			String subIds=keySort(subSet);
			if(stuNumBySubjectidsMap.containsKey(subIds)){
				stuNumBySubjectidsMap.put(subIds, stuNumBySubjectidsMap.get(subIds)+1);
			}else{
				stuNumBySubjectidsMap.put(subIds, 1);
				stuIdBySubjectidsMap.put(subIds, new ArrayList<String>());
			}
			stuIdBySubjectidsMap.get(subIds).add(stu.getStudentId());
		}
		List<List<String>> group3AList=new ArrayList<List<String>>();
		Set<String> chooseSubjectIds=new HashSet<>();
		for (Entry<String, Integer> item:stuNumBySubjectidsMap.entrySet()){
			String[] keys = item.getKey().split(",");
			List<String> keylist1=new ArrayList<String>();
			List<String> keylist = Arrays.asList(keys);
			keylist1.addAll(keylist);
			keylist1.add(item.getValue()+"");
			chooseSubjectIds.addAll(keylist);
			group3AList.add(keylist1);
		}
//		Set<String> tSubjectIds=new HashSet<String>();
		Map<String,Integer> numT=new HashMap<>();
				
		if(StringUtils.isNotBlank(courseTeachNum)){
			String[] arrayStr = courseTeachNum.split(",");
			for(String arr:arrayStr){
				String[] tt = arr.split("_");
				try{
					int num=Integer.parseInt(tt[1]);
					if(num>1){
						numT.put(tt[0],num);
//						tSubjectIds.add(tt[0]);
//						List<String> ss=new ArrayList<String>();
//						ss.add(tt[0]);
//						ss.add(num+"");
//						maxTeacherCountList.add(ss);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
				
		}
		List<List<String>> maxTeacherCountList=new ArrayList<List<String>>();
		//没有设置的科目班级限制 默认少于行政班数量
		//没有学生选择的科目 maxTeacherCountList 不能存放
		for(Course s:courseList){
			if(chooseSubjectIds.contains(s.getId())) {
				//如学生没有选择的科目 不设置maxTeacherCountList
				if(numT.containsKey(s.getId())) {
					List<String> ss=new ArrayList<String>();
					ss.add(s.getId());
					ss.add(numT.get(s.getId())+"");
					maxTeacherCountList.add(ss);
				}else {
					List<String> ss=new ArrayList<String>();
					ss.add(s.getId());
					ss.add(xzbNum+"");
					maxTeacherCountList.add(ss);
				}
			}
			
		}

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					int bestStuNnum=0;//平均人数
					if(leftXzbNum!=0){
						bestStuNnum=leftXzbStu/leftXzbNum;
					}else{
						bestStuNnum=divide.getGalleryful();
					}
					//误差范围
					Integer sectionSizeMargin = marginNum;
					
					Sectioning2A1XInput s2a1xInput=new Sectioning2A1XInput();
//					s2a1xInput.setMaxGroup3ACountPerGroup2A(maxGroup3ACountPerGroup2A);
					s2a1xInput.setGroup3AList(group3AList);
					s2a1xInput.setSectionSizeMean(bestStuNnum);
					s2a1xInput.setSectionSizeMargin(sectionSizeMargin);
					s2a1xInput.setMaxTeacherCountList(maxTeacherCountList);
					s2a1xInput.setMaxRoomCount(leftXzbNum);
					List<List<String>> pre1xList =new ArrayList<List<String>>();
					
					if(leftXNum.size()>0){
						for(Entry<String, Integer> xx:leftXNum.entrySet()){
							String[] aaa=xx.getKey().split(",");
							List<String> nn=new ArrayList<String>();
							nn.add(aaa[0]);
							nn.add(aaa[1]);
							nn.add(aaa[2]);
							nn.add(xx.getValue()+"");
							nn.add(aaa[3]);
							pre1xList.add(nn);
						}
					}
					s2a1xInput.setPre1XList(pre1xList);
					//排除不能组成的2+x 例如有些学校不喜欢物理政治组合--暂时不使用
					List<List<String>> excludedGroup2AList =new ArrayList<List<String>>();
					if(noTwoSubjectIds!=null) {
						for(String s:noTwoSubjectIds) {
							String[] mm = s.split(",");
							excludedGroup2AList.add(Arrays.asList(mm));
						}
					}
					s2a1xInput.setExcludedGroup2AList(excludedGroup2AList);
					//防止某个科目没有人选
//					List<List<String>> sectionSizeList=calculateSectionSize(group3AList, bestStuNnum, sectionSizeMargin,EntityUtils.getSet(courseList, e->e.getId()));
//					List<List<String>> sectionSizeList=calculateSectionSize2(s2a1xInput, 3);
//					s2a1xInput.setSectionSizeList(sectionSizeList);
				
					SectioningApp2 app2=new SectioningApp2();
					//<选课1-2A><选课2-2A> <开班数> <选课1-3A><选课2-3A><选课3-3A><人数>
					List<List<String>> result =app2.makeResult(s2a1xInput,3);
					
					//saveResult(divide,result, bestStuNnum, sectionSizeMargin,courseMap,stuIdBySubjectidsMap,oldClassName,delClassId);
					saveResult2(divide, result, courseMap, stuIdBySubjectidsMap, oldClassName, delClassId);
					RedisUtils.set(key, "success");
					RedisUtils.set(keyMess, "自动分配成功");
					return ;
				} catch (IOException e) {
					e.printStackTrace();
					RedisUtils.set(key, "error");
					RedisUtils.set(keyMess, "自动分配失败");
					return ;
				}catch (Exception e) {
					e.printStackTrace();
					RedisUtils.set(key, "error");
					RedisUtils.set(keyMess, "自动分配失败");
					return ;
				}
				

			}


		}).start();

	}
	
	/**
	 * 
	 * @param 学生选课列表，group3AList，每个3A组合的人数: { <选课1-3A> <选课2-3A> <选课3-3A> <人数> }
	 * @return 每门课的教学班大小，sectionSizeList: {<选课><每个教学班平均人数><误差大小><教学班数量>}
	 * @throws IOException 
	 */
	private static List<List<String>> calculateSectionSize2(Sectioning2A1XInput s2a1xInput,int batch) throws IOException {
		
		//studentCountByCourse：统计一下每门课总共有多少人上
		Map<String, Integer> studentCountByCourse = new HashMap<>();
		
		//先取3A部分的学生人数
		for (List<String> line : s2a1xInput.getGroup3AList()) {
			for (int i = 0; i < 3; i ++) {
				studentCountByCourse.merge(line.get(i).trim(), Integer.parseInt(line.get(3)), Integer::sum);
			}
		}
		
		//再取Pre1X部分的学生人数
		for(List<String> line : s2a1xInput.getPre1XList()) {
			String courseName =  line.get(0);
			int studentCount = Integer.parseInt(line.get(1));
			studentCountByCourse.merge(courseName.trim(), studentCount, Integer::sum);
		}
		
		//现在，studentCountByCourse里是全体的学生数了
		
		//把studentCountByCourse整理成 BestSectionCount 算法的输入格式
		List<List<String>> courseStudentCountList = new ArrayList<>();
		for (Map.Entry<String, Integer> me : studentCountByCourse.entrySet()) {
			List<String> line = new ArrayList<>();
			line.add(me.getKey());
			line.add("" + me.getValue().intValue());
			courseStudentCountList.add(line);
		}
		
		//1. 准备输入数据
		BestSectionCountInput solutionInput = new BestSectionCountInput();
		solutionInput.setMaxRoomCount(s2a1xInput.getMaxRoomCount());		//算法参数1：总的教室数量
		solutionInput.setTimeSlotCount(batch); 									//算法参数2：3个时间点是常见的，如果是7选4的B课情况，这里应该是4
		solutionInput.setCourseStudentCountList(courseStudentCountList); 	//算法参数3：每门课的学生人数
		
		//2. 创建算法对象
		BestSectionCount launcher = new BestSectionCount(solutionInput);
		
		//3. Go Fly! 开拔，调用算法，获得结果
		//resultList: <课程名><开班数><学生数> 
		List<List<String>> resultList = launcher.calculateSectionCount();

		//DEBUG 把结果打印出来看看
//		printSectionSizeList(solutionInput, resultList);
		
		//整理成sectionSizeList要求的格式
		List<List<String>> sectionSizeList = new ArrayList<>();
		int allMarginSize=s2a1xInput.getSectionSizeMargin();
		int allMeanSize=s2a1xInput.getSectionSizeMean();
		int max=allMeanSize+allMarginSize;
		int min=allMeanSize+allMarginSize;
		for (List<String> line : resultList) {
			String courseName = line.get(0);
			int sectionCount = Integer.parseInt(line.get(1));
			int studentCount = Integer.parseInt(line.get(2));
			List<Integer> sizeList = CalculateSections.calculateSectionsByKnownCount(studentCount, sectionCount);
			
			List<String> oneCourseSizeInfo = new ArrayList<>();
			oneCourseSizeInfo.add(courseName);
			int sizeMean = sizeList.get(sizeList.size()/2);		//取中间的一个
			oneCourseSizeInfo.add("" + sizeMean); 				//sectionSizeMean;
			//oneCourseSizeInfo.add("" + (int)(sizeMean * 0.15)); //sectionSizeMargin;
			if(sizeMean>=min && sizeMean<=max) {
				if(sizeMean>allMeanSize) {
					oneCourseSizeInfo.add("" + (max-sizeMean));
				}else {
					oneCourseSizeInfo.add("" + (sizeMean-min));
				}
			}else {
				oneCourseSizeInfo.add("" + allMarginSize);
			}
			
			oneCourseSizeInfo.add("" + sectionCount);
			sectionSizeList.add(oneCourseSizeInfo);
		}

		return sectionSizeList;
	}
	//<选课1-2A><选课2-2A> <开班数> <选课1-3A><选课2-3A><选课3-3A><人数>
	private void saveResult2(NewGkDivide gkDivide,List<List<String>>  resultGroup2AList,Map<String,Course> courseMap,
			Map<String,List<String>> stuIdBySubjectidsMap,Set<String> oldClassName,Set<String> delClassId){
		Map<String,Map<String,Integer>> group=new HashMap<>();
		Map<String,Integer> groupClassNum=new HashMap<>();
		Map<String,Integer> countGroupClassNum=new HashMap<>();
		List<NewGkDivideClass> insertClassList=new ArrayList<NewGkDivideClass>();
		List<NewGkClassStudent> insertStudentList=new ArrayList<NewGkClassStudent>();
		
		Map<String,Integer> nameNumMap=new HashMap<String,Integer>();
		
		for(List<String> r:resultGroup2AList) {
			String key=r.get(0)+","+r.get(1);
			int cNum = Integer.parseInt(r.get(2));
			if(!groupClassNum.containsKey(key)) {
				groupClassNum.put(key, cNum);
				group.put(key, new HashMap<>());
				countGroupClassNum.put(key, Integer.parseInt(r.get(6)));
			}else {
				countGroupClassNum.put(key, countGroupClassNum.get(key)+Integer.parseInt(r.get(6)));
			}
			String key2=r.get(3)+","+r.get(4)+","+r.get(5);
			if(group.get(key).containsKey(key2)) {
				group.get(key).put(key2,group.get(key).get(key2)+ Integer.parseInt(r.get(6)));
			}else {
				group.get(key).put(key2, Integer.parseInt(r.get(6)));
			}
		}
		Map<String,Integer> indexMap=new HashMap<>();
		for(Entry<String, Integer> ii:groupClassNum.entrySet()) {
			String twoSubjectIds=ii.getKey();
			String nameKey2=nameSet(courseMap, twoSubjectIds);
			int classNum=ii.getValue();
//			int count=countGroupClassNum.get(twoSubjectIds);
			
			//每个组合的人数
			Map<String, Integer> groupStudent = group.get(twoSubjectIds);
			
			Map<String,List<String>> studentId=new HashMap<>();
			List<String> stulist=new ArrayList<>();
			for(Entry<String, Integer> ii1:groupStudent.entrySet()) {
				List<String> ll = stuIdBySubjectidsMap.get(ii1.getKey());
				int fromIndex=0;
				if(indexMap.containsKey(ii1.getKey())) {
					fromIndex=indexMap.get(ii1.getKey());
				}
				if(fromIndex==ll.size()) {
					continue;
				}
				int toIndex=fromIndex+ii1.getValue();
				if(toIndex>ll.size()) {
					toIndex=ll.size();
				}
//				studentId.put(ii1.getKey(), ll.subList(fromIndex, toIndex));
//				int oneNum=studentId.get(ii1.getKey()).size();
//				int ss=oneNum/classStuNum;
//				if(ss>0) {
//					
//				}else {
//					//不足开班
//				}
				List<String> stulist2 = ll.subList(fromIndex, toIndex);
				studentId.put(ii1.getKey(), stulist2);
				stulist.addAll(stulist2);
				indexMap.put(ii1.getKey(), toIndex);
			}
			
			//System.out.println(nameKey2+"-------------:"+stulist.size());
			int classStuNum=stulist.size()/classNum;
			int left=stulist.size()%classNum;
			int other=classNum-left;
			//有left个班级需要classStuNum+1 剩余班级classNum-left
			if(left>0) {
				for(Entry<String, List<String>> ss:studentId.entrySet()) {
					if(left==0) {
						break;
					}
					String key=ss.getKey();
					String name3=nameSet(courseMap, key);
					List<String> s1=ss.getValue();
					while(true) {
						if(left==0) {
							break;
						}
						if(s1.size()>=(classStuNum+1)) {
							//开设班级
							List<String> rr = s1.subList(0, classStuNum+1);
							String newName=findNewName(nameNumMap, key, name3, oldClassName);
							addClassAndStuToList(rr, gkDivide, key, newName, NewGkElectiveConstant.SUBJTCT_TYPE_3, insertClassList, insertStudentList);
							left--;
							s1=s1.subList(classStuNum+1, s1.size());
							
						}else {
							break;
						}
					}
					studentId.put(key, s1);
				}
			}
			//按classStuNum
			for(Entry<String, List<String>> ss:studentId.entrySet()) {
				if(other==0) {
					break;
				}
				String key=ss.getKey();
				String name3=nameSet(courseMap, key);
				List<String> s1=ss.getValue();
				if(CollectionUtils.isEmpty(s1)) {
					continue;
				}
				while(true) {
					if(other==0) {
						break;
					}
					if(s1.size()>=classStuNum) {
						//开设班级
						List<String> rr = s1.subList(0, classStuNum);
						String newName=findNewName(nameNumMap, key, name3, oldClassName);
						addClassAndStuToList(rr, gkDivide, key, newName, NewGkElectiveConstant.SUBJTCT_TYPE_3, insertClassList, insertStudentList);
						other--;
						s1=s1.subList(classStuNum, s1.size());
						
					}else {
						break;
					}
				}
				studentId.put(key, s1);
			}
			//还有剩余
			List<String> leftList=new ArrayList<>();
			for(Entry<String, List<String>> ss:studentId.entrySet()) {
				if(CollectionUtils.isNotEmpty(ss.getValue())) {
					leftList.addAll(ss.getValue());
				}
			}
			int m=0;
			List<String> stuList11=leftList;
			List<String> stuList111=new ArrayList<>();
			while(true) {
				if(CollectionUtils.isNotEmpty(stuList11)) {
					if(left==0) {
						stuList111=stuList11.subList(0, classStuNum);
						String newName=findNewName(nameNumMap, twoSubjectIds, nameKey2, oldClassName);
						addClassAndStuToList(stuList111, gkDivide, twoSubjectIds, newName, NewGkElectiveConstant.SUBJTCT_TYPE_2, insertClassList, insertStudentList);
						m=classStuNum;
						stuList11=stuList11.subList(m, stuList11.size());
						other--;
					}else {
						stuList111=stuList11.subList(0, classStuNum+1);
						String newName=findNewName(nameNumMap, twoSubjectIds, nameKey2, oldClassName);
						addClassAndStuToList(stuList111, gkDivide, twoSubjectIds, newName, NewGkElectiveConstant.SUBJTCT_TYPE_2, insertClassList, insertStudentList);
						m=classStuNum+1;
						stuList11=stuList11.subList(m, stuList11.size());
						left--;
					}
					
				}else {
					break;
				}
			}
			
			
			
//			int ss=0;
//			List<String> stuList1;
//			for(int j=0;j<classNum;j++) {
//				if(left==0) {
//					stuList1=stulist.subList(ss, ss+classStuNum);
//					ss=ss+classStuNum;
//				}else {
//					stuList1=stulist.subList(ss, ss+classStuNum+1);
//					ss=ss+classStuNum+1;
//					left--;
//				}
//				String newName=findNewName(nameNumMap, twoSubjectIds, nameKey2, oldClassName);
//				addClassAndStuToList(stuList1, gkDivide, twoSubjectIds, newName, NewGkElectiveConstant.SUBJTCT_TYPE_2, insertClassList, insertStudentList);
//			}
			
			
		}
		
		String[] divideClassIds=null;
		if(CollectionUtils.isNotEmpty(delClassId)){
			divideClassIds=delClassId.toArray(new String[]{});
		}
		newGkDivideClassService.saveAllList(gkDivide.getUnitId(), gkDivide.getId(),
				divideClassIds, insertClassList, insertStudentList, false);
	}
	
	
	//暂时用不到
//	private void saveResult(NewGkDivide gkDivide,List<List<String>> resultGroup2AList, int sizeMean, int sizeMargin,Map<String,Course> courseMap,Map<String,List<String>> stuIdBySubjectidsMap,
//			Set<String> oldClassName,Set<String> delClassId) {
//		Map<String,Integer> indexBySubjectidsMap=new HashMap<String,Integer>();////key:选择的3门组合 value 用于截取stuIdBySubjectidsMap学生数据
//		
//		Map<String,Integer> nameNumMap=new HashMap<String,Integer>();
//		Map<String, List<List<String>>> group2AList = resultGroup2AList.stream().collect(Collectors.groupingBy(e -> e.get(0) +","+ e.get(1)));
//		
//		List<NewGkDivideClass> insertClassList=new ArrayList<NewGkDivideClass>();
//		List<NewGkClassStudent> insertStudentList=new ArrayList<NewGkClassStudent>();
//		
//		for (Map.Entry<String, List<List<String>>> me : group2AList.entrySet()) {
//			//循环某一种2+x
//			String subjectIds=me.getKey();//2+x中2
//			String[] s1 = subjectIds.split(",");
//			Arrays.sort(s1);
//			String sortsubjectIds=ArrayUtil.print(s1);
//			
//			
//			//总共学生id
////			List<String> iids=new ArrayList<>();
////			for (int i = 0; i < me.getValue().size(); i++) {
////				int num=Integer.parseInt(me.getValue().get(i).get(5));
////				Set<String> subId=new HashSet<String>();
////				subId.add(me.getValue().get(i).get(2));
////				subId.add(me.getValue().get(i).get(3));
////				subId.add(me.getValue().get(i).get(4));
////				String subject3=keySort(subId);
////				if(!indexBySubjectidsMap.containsKey(subject3)){
////					indexBySubjectidsMap.put(subject3, 0);
////				}
////				iids.addAll(subList(stuIdBySubjectidsMap.get(subject3), indexBySubjectidsMap.get(subject3), indexBySubjectidsMap.get(subject3)+num));
////			}
////			List<Integer> sectionSizeList = CalculateSections.calculateSectioning(iids.size(), sizeMean, sizeMargin);
////			int ii=0;
////			for(Integer jj:sectionSizeList) {
////				List<String> stuList1=iids.subList(ii, ii+jj);
////				String nameKey2=nameSet(courseMap, sortsubjectIds);
////				addClassAndStuToList(stuList1, gkDivide, sortsubjectIds, nameKey2, NewGkElectiveConstant.SUBJTCT_TYPE_2, insertClassList, insertStudentList);
////				ii=ii+jj;
////			}
//			
//			Map<String,List<String>> stuIdBySubjectidsMap1=new HashMap<String,List<String>>();//key:选择的3门组合 value 学生id
//			int studentCount = 0;//总人数
//			for (int i = 0; i < me.getValue().size(); i++) {
//				int num=Integer.parseInt(me.getValue().get(i).get(5));
//				studentCount += num;
//				Set<String> subId=new HashSet<String>();
//				subId.add(me.getValue().get(i).get(2));
//				subId.add(me.getValue().get(i).get(3));
//				subId.add(me.getValue().get(i).get(4));
//				String subject3=keySort(subId);
//				if(!indexBySubjectidsMap.containsKey(subject3)){
//					indexBySubjectidsMap.put(subject3, 0);
//				}
//				stuIdBySubjectidsMap1.put(subject3, subList(stuIdBySubjectidsMap.get(subject3), indexBySubjectidsMap.get(subject3), indexBySubjectidsMap.get(subject3)+num));
//				indexBySubjectidsMap.put(subject3, indexBySubjectidsMap.get(subject3)+num);
//			}
//			//从大到小
//			List<Integer> sectionSizeList = CalculateSections.calculateSectioning(studentCount, sizeMean, sizeMargin);
//			
//			//总班级数
//			int classNum=sectionSizeList.size();
//			int minMean=sectionSizeList.get(sectionSizeList.size()-1);
//			int maxMean=sectionSizeList.get(0);
//			int maxMeanNum=studentCount%minMean;
//			List<String> leftStuId=new ArrayList<String>();
//			int classNum1=0;
//			for(Entry<String, List<String>> item1:stuIdBySubjectidsMap1.entrySet()){
//				
//				List<String> stuList = item1.getValue();
//				int sizes = stuList.size();
//				if(sizes<minMean){
//					//不能成一个班 只能2+x
//					leftStuId.addAll(stuList);
//					continue;
//				}else{
//					String key1= item1.getKey();
//					//排序
//					String[] s = key1.split(",");
//					Arrays.sort(s);
//					String sortkey1=ArrayUtil.print(s);
//					String nameKey1=nameSet(courseMap, sortkey1);
//					String subjectType3=NewGkElectiveConstant.SUBJTCT_TYPE_3;
//					
//					int jj=0;//用于截取stuList
//					while(true){
//						if(maxMeanNum>0){
//							int lastIndex=jj+maxMean;
//							if(sizes>lastIndex){
//								//以最大值
//								List<String> stuList1 = subList(stuList, jj, lastIndex);
//								//一个三科组合班
//								String newName=findNewName(nameNumMap, sortkey1, nameKey1, oldClassName);
//								
//								addClassAndStuToList(stuList1, gkDivide, sortkey1, newName, subjectType3, insertClassList, insertStudentList);
//	
//								jj=lastIndex;
//								maxMeanNum--;
//								classNum1++;
//								continue;
//							}else{
//								//以最小值
//								lastIndex=jj+minMean;
//								if(sizes>lastIndex){
//									List<String> stuList1 = subList(stuList, jj, lastIndex);
//									//一个三科组合班
//									String newName=findNewName(nameNumMap, sortkey1, nameKey1, oldClassName);
//									addClassAndStuToList(stuList1, gkDivide, sortkey1, newName, subjectType3, insertClassList, insertStudentList);
//									jj=lastIndex;
//									classNum1++;
//									continue;
//								}else{
//									break;
//								}
//							}
//						}else{
//							//以最小值
//							int lastIndex=jj+minMean;
//							if(sizes>lastIndex){
//								List<String> stuList1 = subList(stuList, jj, lastIndex);
//								//一个三科组合班
//								String newName=findNewName(nameNumMap, sortkey1, nameKey1, oldClassName);
//								
//								addClassAndStuToList(stuList1, gkDivide, sortkey1, newName, subjectType3, insertClassList, insertStudentList);
//								jj=lastIndex;
//								classNum1++;
//								continue;
//							}else{
//								break;
//							}
//						}
//						
//					}
//					
//					if(jj<stuList.size()){
//						leftStuId.addAll(subList(stuList, jj, stuList.size()));
//					}
//				}
//			}
//			
//			if(CollectionUtils.isNotEmpty(leftStuId)){
//				String nameKey2=nameSet(courseMap, sortsubjectIds);
//				//leftStuId 分成classNum-classNum1 2+x
//				int classnum2 = classNum-classNum1;
//				int lestStuNum=leftStuId.size();
//				int mean1=lestStuNum/classnum2;
//				int dd=lestStuNum%classnum2;
//				List<Integer> arrList=new ArrayList<Integer>();
//				for(int k=0;k<classnum2;k++){
//					if(k<dd){
//						arrList.add(mean1+1);
//					}else{
//						arrList.add(mean1);
//					}
//				}
//				int index1=0;
//				for(Integer nn:arrList){
//					List<String> stuList1 = subList(leftStuId, index1, index1+nn);
//					//一个三科组合班
//					index1=index1+nn;
//					//2科
//					String newName=findNewName(nameNumMap, sortsubjectIds, nameKey2, oldClassName);
//					
//					addClassAndStuToList(stuList1, gkDivide, sortsubjectIds, newName, NewGkElectiveConstant.SUBJTCT_TYPE_2, insertClassList, insertStudentList);
//
//				}
//			}
//
//		}
//		
//		
//		String[] divideClassIds=null;
//		if(CollectionUtils.isNotEmpty(delClassId)){
//			divideClassIds=delClassId.toArray(new String[]{});
//		}
//		newGkDivideClassService.saveAllList(gkDivide.getUnitId(), gkDivide.getId(),
//				divideClassIds, insertClassList, insertStudentList, false);
//		
//	}	
	
	private String findNewName(Map<String,Integer> nameNumMap,String subjectIds,String nameKey2,Set<String> oldClassName){
		if(!nameNumMap.containsKey(subjectIds)){
			nameNumMap.put(subjectIds, 1);
		}else{
			nameNumMap.put(subjectIds, nameNumMap.get(subjectIds)+1);
		}
		int j=nameNumMap.get(subjectIds);
		String newName="";
		while(true){
			newName=nameKey2+j+"班";
			if(oldClassName.contains(newName)){
				j++;
			}else{
				break;
			}
		}
		nameNumMap.put(subjectIds, j);
		oldClassName.add(newName);
		return newName;
	}
	
	private void addClassAndStuToList(List<String> stuList1,NewGkDivide gkDivide,String subjectIds,String className,String subjectType,
			List<NewGkDivideClass> insertClassList,List<NewGkClassStudent> insertStudentList){
		NewGkClassStudent gkGroupClassStu;
		//2科
		NewGkDivideClass divideClass = initNewGkDivideClass(gkDivide.getId(), subjectIds,NewGkElectiveConstant.CLASS_TYPE_0);
		divideClass.setClassName(className);
		divideClass.setSubjectType(subjectType);
		insertClassList.add(divideClass);
		//学生
		for(String stuId:stuList1){
			gkGroupClassStu = initClassStudent(gkDivide.getUnitId(), gkDivide.getId(), divideClass.getId(), stuId);
			
			insertStudentList.add(gkGroupClassStu);
		}
	}
	
	/**
	 * 全手动分班 手动组教学班
	 * @param divideId
	 * @return
	 */
	@RequestMapping("/manualDivide/index")
	public String manualDivideClassIndex(@PathVariable String divideId, ModelMap modelMap) {
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		boolean canEdit = !NewGkElectiveConstant.IF_1.equals(divide.getStat());
		if(canEdit) {
			canEdit = !isNowDivide(divide.getId());
		}
		modelMap.put("canEdit", canEdit);
		
		//所有教学班以及组合班数据
		List<NewGkDivideClass> classList = newGkDivideClassService.findByDivideIdAndClassTypeWithMaster(divide.getUnitId(), 
				divideId,
				new String[] {NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_2}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		//混合班数据
		List<NewGkDivideClass> hhbList = classList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType())
						&& BaseConstants.ZERO_GUID.equals(e.getSubjectIds())).collect(Collectors.toList());
		//2科组合数据+3科组合数据
		List<NewGkDivideClass> pureList = classList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType())
						&& !BaseConstants.ZERO_GUID.equals(e.getSubjectIds())).collect(Collectors.toList());
		//2科组合数据 
		List<NewGkDivideClass> twoClassList=classList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType())
						&& NewGkElectiveConstant.SUBJECT_TYPE_2.equals(e.getSubjectType())).collect(Collectors.toList());
		
		Map<String,NewGkDivideClass> hhbMap = EntityUtils.getMap(hhbList, e->e.getId());
		
		//教学班数据
		List<NewGkDivideClass> jxbList = classList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())).collect(Collectors.toList());
		
		List<NewGkClassBatch> clasBatchList = classBatchService.findListByIn("divideId", new String[] {divideId});
		
		// 获取选课数据
		Map<String,Set<String>> subjectChoMap = new HashMap<>();
		Map<String,Set<String>> chooseByStuId=new HashMap<>();
		makeStudentChooseResult(divide, subjectChoMap, chooseByStuId);
		Set<String> subjectIds =subjectChoMap.keySet();

		List<Course> courseList = courseRemoteService.findListObjectByIds(subjectIds.toArray(new String[0]));
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, e->e.getId());
		
		List<NewGkOpenSubject> openASubjectList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeIn(divideId, new String[] {NewGkElectiveConstant.SUBJECT_TYPE_A});
		Set<String> openSubjectIds = EntityUtils.getSet(openASubjectList, e->e.getSubjectId());
		// <batch,<subjectId, <studentIds>>>
		Map<String,Map<String, Set<String>>> batchSubjectMap = new HashMap<>();
		//混合班的统计数据
		for (NewGkClassBatch cb : clasBatchList) {
			if(!openSubjectIds.contains(cb.getSubjectId())) {
				// 这门科目不走班
				continue;
			}
			String[] subjectIds2 = cb.getSubjectIds().split(",");
			if(!batchSubjectMap.containsKey(cb.getBatch())) {
				batchSubjectMap.put(cb.getBatch(), new HashMap<>());
			}
			Map<String, Set<String>> map = batchSubjectMap.get(cb.getBatch());
			if(!map.containsKey(cb.getSubjectId())) {
				map.put(cb.getSubjectId(), new HashSet<>());
			}
			NewGkDivideClass claz = hhbMap.get(cb.getDivideClassId());
			
//			Set<String> students = subjectChoMap.get(cb.getSubjectId());
//			List<String> collect = claz.getStudentList().stream().filter(e->students.contains(e)).collect(Collectors.toList());
			Set<String> students1 = subjectChoMap.get(subjectIds2[0]);
			Set<String> students2 = subjectChoMap.get(subjectIds2[1]);
			Set<String> students3 = subjectChoMap.get(subjectIds2[2]);
			if(claz!=null && CollectionUtils.isNotEmpty(claz.getStudentList())) {
				List<String> collect = claz.getStudentList().stream()
						.filter(e->students1.contains(e)
								&&students2.contains(e)
								&&students3.contains(e))
						.collect(Collectors.toList());
				map.get(cb.getSubjectId()).addAll(collect);
			}
			
		}
		//增加2+x key:classId key:subjectId
		Map<String, Map<String, Set<String>>> studentXBySubIdClassId = makeStudentX(twoClassList, chooseByStuId);
		if(studentXBySubIdClassId.size()>0) {
			String endBath = divide.getBatchCountTypea().toString();
			if(!batchSubjectMap.containsKey(endBath)) {
				batchSubjectMap.put(endBath, new HashMap<>());
			}
			for(Entry<String, Map<String, Set<String>>> ttt:studentXBySubIdClassId.entrySet()) {
				Map<String, Set<String>> value = ttt.getValue();
				if(MapUtils.isNotEmpty(value)) {
					for(Entry<String, Set<String>> tttt:value.entrySet()) {
						Map<String, Set<String>> map = batchSubjectMap.get(endBath);
						if(!map.containsKey(tttt.getKey())) {
							map.put(tttt.getKey(), new HashSet<>());
						}
						map.get(tttt.getKey()).addAll(tttt.getValue());
					}
				}
			}
		}
		
//		
		//batchSubjectMap代表着需要安排的所有人
		
		
		// <batch,<subjectId, <NewGkDivideClass>>>
		Map<String,Map<String, Set<NewGkDivideClass>>> batchSubjectClassMap = new HashMap<>();
		for (NewGkDivideClass jxb : jxbList) {
			if(!batchSubjectClassMap.containsKey(jxb.getBatch())) {
				batchSubjectClassMap.put(jxb.getBatch(), new HashMap<>());
			}
			Map<String, Set<NewGkDivideClass>> map = batchSubjectClassMap.get(jxb.getBatch());
			if(!map.containsKey(jxb.getSubjectIds())) {
				map.put(jxb.getSubjectIds(), new HashSet<>());
			}
			map.get(jxb.getSubjectIds()).add(jxb);
		}
		
		BatchClassDto dto;
		List<BatchClassDto> dtoList = new ArrayList<>();
		for (String batch : batchSubjectMap.keySet()) {
			Map<String, Set<String>> subjectStuMap = batchSubjectMap.get(batch);
			Map<String, Set<NewGkDivideClass>> subjectClassMap = batchSubjectClassMap.get(batch);
			if(subjectClassMap == null) {
				subjectClassMap = new HashMap<>();
			}
			
			for (String subjectId : subjectStuMap.keySet()) {
				Set<String> studentIds = subjectStuMap.get(subjectId);
				Set<NewGkDivideClass> classSet = subjectClassMap.get(subjectId);
				int classStuNum = 0;
				if(classSet != null) {
					//去除不属于这个studentIds这个范围的学生剩下就是已安排的人
					Set<String> arrangeStuId=new HashSet<>();
					for(NewGkDivideClass ss:classSet) {
						arrangeStuId.addAll(ss.getStudentList());
					}
					if(CollectionUtils.isNotEmpty(arrangeStuId)) {
						//studentIds与arrangeStuId的交集
						classStuNum=CollectionUtils.intersection(studentIds, arrangeStuId).size();
					}else {
						classStuNum=0;
					}
					
//					classStuNum = (int)classSet.stream().flatMap(e->e.getStudentList().stream()).count();
//					int pureCount = (int)classSet.stream().filter(e->pureClassMap.containsKey(e.getRelateId()))
//							.map(e->pureClassMap.get(e.getRelateId()))
//							.flatMap(e->e.getStudentList().stream()).count();
//					classStuNum = classStuNum - pureCount;
				}else {
					classSet = new HashSet<>();
				}
				
				dto = new BatchClassDto();
				dto.setBatch(batch);
				dto.setDevideClassList(new ArrayList<>(classSet));
				dto.setStuNum(studentIds.size());
				dto.setFreeStuNum(studentIds.size() - classStuNum);
				dto.setCourse(courseMap.get(subjectId));
				
				dtoList.add(dto);
			}
		}
		
		Map<String, List<BatchClassDto>> dtoMap = EntityUtils.getListMap(dtoList, "batch",null);
		modelMap.put("dtoList", dtoList);
		modelMap.put("dtoMap", dtoMap);
//		boolean isCanEdit=true;
//		if(NewGkElectiveConstant.IF_1.equals(divide.getStat()) || isNowDivide(divideId)) {
//			isCanEdit=false;
//		}
//		modelMap.put("isCanEdit", isCanEdit);
		return "/newgkelective/clsBatch/batchStuCount.ftl";
	}
	
	/**
	 * 手动开班
	 * @param divideId
	 * @param batchDto
	 * @param modelMap 
	 * @return
	 */
	@RequestMapping("/showUndivideStudents")
	public String showUndivideStudents(@PathVariable String divideId, BatchClassDto batchDto, ModelMap modelMap) {
		String batch = batchDto.getBatch();
		if(batchDto.getCourse() == null || StringUtils.isBlank(batchDto.getCourse().getId()) 
				|| StringUtils.isBlank(batch)) {
			return error("科目或批次为空");
		}
		String subjectId = batchDto.getCourse().getId();
		String classId = batchDto.getClassId();
		modelMap.put("classId", classId);
		String unitId=this.getLoginInfo().getUnitId();
		List<NewGkDivideClass> classList = newGkDivideClassService.findByDivideIdAndClassTypeWithMaster(unitId, 
				divideId,
				new String[] {NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_2}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<NewGkDivideClass> hhbList = classList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType())
						&& BaseConstants.ZERO_GUID.equals(e.getSubjectIds())).collect(Collectors.toList());
		Map<String,NewGkDivideClass> hhbMap = EntityUtils.getMap(hhbList, e->e.getId());
		
		List<NewGkDivideClass> pureList = classList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType())
						&& !BaseConstants.ZERO_GUID.equals(e.getSubjectIds())).collect(Collectors.toList());

		
		
		List<NewGkDivideClass> jxbList = classList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())
						&& subjectId.equals(e.getSubjectIds())
						&& batch.equals(e.getBatch())).collect(Collectors.toList());
		
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		// 获取选课数据
		// K:科目 V：学生
		Map<String,Set<String>> subjectChoMap = new HashMap<>();
		Map<String,Set<String>> chooseByStuId = new HashMap<>();
		makeStudentChooseResult(divide, subjectChoMap, chooseByStuId);

		
		List<NewGkClassBatch> batchClassList = classBatchService.findbyBatchAndSubjectId(divideId,batch,subjectId);
		
		Set<String> studentIds = new HashSet<>();
		
		for (NewGkClassBatch batchClaz : batchClassList) {
			String[] subjectIds = batchClaz.getSubjectIds().split(",");
			NewGkDivideClass clazz = hhbMap.get(batchClaz.getDivideClassId());
			if(clazz == null) {
				continue;
			}
//			Set<String> stus = subjectChoMap.get(batchClaz.getSubjectId());
//			List<String> studentList = clazz.getStudentList().stream()
//					.filter(e->stus.contains(e)).collect(Collectors.toList());
			Set<String> stus1 = subjectChoMap.get(subjectIds[0]);
			Set<String> stus2 = subjectChoMap.get(subjectIds[1]);
			Set<String> stus3 = subjectChoMap.get(subjectIds[2]);
			List<String> studentList = clazz.getStudentList().stream()
					.filter(e->stus1.contains(e) 
							&& stus2.contains(e) 
							&& stus3.contains(e))
					.collect(Collectors.toList());
			studentIds.addAll(studentList);
		}
		if(batch.equals(divide.getBatchCountTypea().toString())) {
			//增加2+x 最后一个批次
			Set<String> set ;
			for(NewGkDivideClass ps:pureList) {
				if(NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(ps.getSubjectType())) {
					if(ps.getSubjectIds().indexOf(subjectId)>-1) {
						continue;
					}
					if(CollectionUtils.isNotEmpty(ps.getStudentList())) {
						for(String s:ps.getStudentList()) {
							set= chooseByStuId.get(s);
							if(set.contains(subjectId)) {
								studentIds.add(s);
							}
						}
					}
				}
			}
		}
		
		
		Set<String> jxbStuIds = jxbList.stream().flatMap(e->e.getStudentList().stream()).collect(Collectors.toSet());
		studentIds.removeAll(jxbStuIds);
		List<Student> students= SUtils.dt(studentRemoteService.findPartStudByGradeId(unitId,null,null,studentIds.toArray(new String[0])), new TR<List<Student>>(){});
		makeStudentDto(divide,batchDto.getCourse(), students, classList,modelMap);
		List<NewGkOpenSubject> aOpenSubjectList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeIn(divideId, new String[] {});
		
		Set<String> subjectIds = EntityUtils.getSet(aOpenSubjectList, e->e.getSubjectId());
		List<Course> courseList = courseRemoteService.findListObjectByIds(subjectIds.toArray(new String[0]));
		modelMap.put("courseList", courseList);
		modelMap.put("divideId", divideId);
		modelMap.put("batch", batch);
		modelMap.put("subjectId", subjectId);
		
		return "/newgkelective/clsBatch/manualDivide.ftl";
	}

	/**
	 * 走班学生统计，显示已分班班级
	 * @param divideId
	 * @param batchDto
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showDivideStudents")
	public String showDivideStudent(@PathVariable String divideId, BatchClassDto batchDto, ModelMap modelMap) {
		String batch = batchDto.getBatch();
		if(batchDto.getCourse() == null || StringUtils.isBlank(batchDto.getCourse().getId()) 
				|| StringUtils.isBlank(batch)) {
			return error("科目或批次为空");
		}
		String subjectId = batchDto.getCourse().getId();
		String classId = batchDto.getClassId();
		
		List<NewGkDivideClass> classList = newGkDivideClassService.findByDivideIdAndClassTypeWithMaster(this.getLoginInfo().getUnitId(),
				divideId, 
				new String[] {NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_2}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		Map<String, NewGkDivideClass> classMap = EntityUtils.getMap(classList, e->e.getId());
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		
		List<NewGkDivideClass> subjectJxbList = classList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType()) 
				&& subjectId.equals(e.getSubjectIds())
				&& batch.equals(e.getBatch())).collect(Collectors.toList());
		modelMap.put("jxbList", subjectJxbList);
		
		List<Student> gradeStuList = SUtils.dt(studentRemoteService.findPartStudByGradeId(divide.getUnitId(), divide.getGradeId(),null, null),Student.class);
		Map<String, Student> stuMap = EntityUtils.getMap(gradeStuList, Student::getId);
		
		if(CollectionUtils.isNotEmpty(subjectJxbList)) {
			if(StringUtils.isBlank(classId)) {
				classId = subjectJxbList.get(0).getId();
			}
			for (NewGkDivideClass clz : subjectJxbList) {
				boolean isZhb = false;
				NewGkDivideClass zhbClz = null;
				if(StringUtils.isNotBlank(clz.getRelateId()) && classMap.containsKey(clz.getRelateId())) {
					isZhb = true;
					zhbClz = classMap.get(clz.getRelateId());
					if(zhbClz == null) {
						continue;
					}
					clz.setClassName(clz.getClassName()+"("+zhbClz.getClassName()+")");
				}
				// 要显示学生列表的班级
				if(classId.equals(clz.getId())) {
					List<String> studentList = clz.getStudentList();
					//Set<String> studentIds = new HashSet<>(studentList);
					if(isZhb) {
						List<String> studentList2 = zhbClz.getStudentList();
						if(!BaseConstants.ZERO_GUID.equals(zhbClz.getSubjectIds())) {
							modelMap.put("hcb", 1);
						}
						modelMap.put("xzbStus", new HashSet<>(studentList2));
					}
					List<Student> stutList = studentList.stream()
							.filter(e->stuMap.containsKey(e))
							.map(e->stuMap.get(e)).collect(Collectors.toList());
					makeStudentDto(divide, batchDto.getCourse(), stutList, classList, modelMap);
				}
			}
			
		}else {
			Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
			if(course!=null) {
				modelMap.put("courseName", course.getSubjectName());
			}
		}
		
		modelMap.put("right", 1);
		modelMap.put("classId", classId);
		modelMap.put("subjectId", subjectId);
		modelMap.put("batch", batch);
		return "/newgkelective/clsBatch/batchClassDetail.ftl";
	}
	
	private String makeStudentDto(NewGkDivide divide, Course course, List<Student> studentList, 
			List<NewGkDivideClass> classList, ModelMap map) {
//		List<Student> gradeStuList = SUtils.dt(studentRemoteService.findPartStudByGradeId(divide.getGradeId(), null,null),Student.class);
//		Map<String, Student> stuMap = EntityUtils.getMap(gradeStuList, Student::getId);
//		List<Student> studentList = studentIds.stream()
//				.filter(e->stuMap.containsKey(e))
//				.map(e->stuMap.get(e)).collect(Collectors.toList());
		
		int manCount =  (int)studentList.stream().filter(e->Integer.valueOf(1).equals(e.getSex())).count();
		int woManCount = (int)studentList.stream().filter(e->Integer.valueOf(2).equals(e.getSex())).count();
		
		//学生行政班班级
		Map<String,NewGkDivideClass> stuClassMap = new HashMap<>();
		for (NewGkDivideClass clz : classList) {
			if(NewGkElectiveConstant.CLASS_TYPE_0.equals(clz.getClassType())) {
				clz.getStudentList().forEach(e->stuClassMap.put(e, clz));
			}
		}
		
		// 成绩
		String mcodeId = ColumnInfoUtils.getColumnInfo(Student.class, "sex").getMcodeId();
		Map<String, McodeDetail> codeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId(mcodeId), 
				new TypeReference<Map<String, McodeDetail>>() {});
		Map<String, Map<String, Float>> stuScoreMap = getScoreMap(divide, EntityUtils.getList(studentList, Student::getId).toArray(new String[0]));
		
//		List<Course> ysyCourses = SUtils.dt(courseRemoteService.findByCodesYSY(getLoginInfo().getUnitId()),Course.class);
//		Course course = SUtils.dc(courseRemoteService.findOneById(subjectId),Course.class);
		List<StudentResultDto> dtoList = new ArrayList<>();
		StudentResultDto dto;
		float courseAvg = (float)0.0;
//		float ysyAvg = (float)0.0;
//		float totalAvg = (float)0.0;
		for (Student stu : studentList) {
			dto = new StudentResultDto();
			dto.setStudentId(stu.getId());
			dto.setStudentName(stu.getStudentName());
			if(stu.getSex()!=null){
				dto.setSex(codeMap.get(stu.getSex()+"").getMcodeContent());
			}else {
				dto.setSex("未知性别");
			}
			
			
			Map<String, Float> scoreMap = stuScoreMap.get(stu.getId());
			Float score1;
			float ysyScore = (float)0.0;
			float allScore = (float)0.0;
			if(scoreMap==null){
				score1 = (float)0.0;
			}else{
				score1 = scoreMap.get(course.getId());
				if(score1 == null) {
					score1 = (float)0.0;
				}
				
				for(Entry<String, Float> item:scoreMap.entrySet()){
					Float ss = item.getValue();
					if(ss==null){
						ss = (float)0.0;
					}
					allScore=allScore+ss;
				}
			}
			
			courseAvg += score1;
			
			dto.getSubjectScore().put(course.getId(), score1);
			dto.getSubjectScore().put("YSY", ysyScore);
			dto.getSubjectScore().put("TOTAL", allScore);
			
			NewGkDivideClass divideClass = stuClassMap.get(stu.getId());
			if(divideClass == null || StringUtils.isBlank(divideClass.getClassName())) {
				dto.setClassName("未知");
			}else {
				dto.setClassName(stuClassMap.get(stu.getId()).getClassName());
			}
			
			dtoList.add(dto);
		}
		
		int size = dtoList.size();
		if(size==0) {
			size = 1;
		}
		
		map.put("courseName", course.getSubjectName());
		map.put("subjectId", course.getId());
		map.put("dtoList", dtoList);
		map.put("manCount", manCount);
		map.put("woManCount", woManCount);
		map.put("courseAvg", courseAvg/size);
		
		return "/";
	}
	
	@RequestMapping("/modifyStudents")
	@ResponseBody
	public String modifyStudents(@PathVariable String divideId, BatchClassDto batchDto,
			@RequestParam(name="studentIds[]",required=false)String[] studentIds) {
		String classId = batchDto.getClassId();
		String batch = batchDto.getBatch();
		String subjectId = batchDto.getCourse().getId();
		Integer classNum = batchDto.getClassNum();
		NewGkDivide divide = newGkDivideService.findById(divideId);
		if(divide==null) {
			return error("分班方案不存在");
		}
		if(StringUtils.isBlank(batch)) {
			return error("时间点为空");
		}
		
		Date date = new Date();
		if(StringUtils.isBlank(classId) || classNum != null) {
			int stuSize = studentIds.length;
			Course course = courseRemoteService.findOneObjectById(subjectId);
			
			// 新建班级
			List<NewGkDivideClass> classList = newGkDivideClassService.findClassBySubjectIds(divide.getUnitId(), divideId,
					NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_2, subjectId, false);
			Set<String> classNames = EntityUtils.getSet(classList, e->e.getClassName());
			int classIndex = classList.size()+1;
			if(classNum == null || classNum < 1) {
				classNum = 1;
			}
			
			int avgNum = stuSize/classNum;
			int remainNum = stuSize%classNum;
			int start =0;
			int end =0;
			String className ;
			List<NewGkDivideClass> newClassList = new ArrayList<>();
			List<NewGkClassStudent> classStudentList = new ArrayList<>();
			for (int i=0;i<classNum&&start<stuSize;i++) {
				end = start + avgNum + (remainNum-->0?1:0);
				if(end > stuSize) {
					end = stuSize;
				}
				String[] subStuIds = Arrays.copyOfRange(studentIds, start,end);
				NewGkDivideClass clazz = new NewGkDivideClass();
				classId = UuidUtils.generateUuid();
				clazz.setId(classId);
				clazz.setDivideId(divideId);
				clazz.setCreationTime(date);
				clazz.setModifyTime(date);
				clazz.setBatch(batch);
				clazz.setSubjectIds(subjectId);
				clazz.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
				clazz.setClassType(NewGkElectiveConstant.CLASS_TYPE_2);
				clazz.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
				className = course.getSubjectName()+"A"+classIndex++ +"班";
				while(classNames.contains(className)) {
					className = course.getSubjectName()+"A"+classIndex++ +"班";
				}
				clazz.setClassName(className);
				classNames.add(className);
				clazz.setIsHand(NewGkElectiveConstant.IS_HAND_1);
				newClassList.add(clazz);
				
				List<NewGkClassStudent> stuList = makeStudents(divide,subStuIds, classId, date);
				classStudentList.addAll(stuList);
				
				start = end;
			}
			
			try {
				newGkDivideClassService.saveAllList(null, null, null, newClassList, classStudentList, false);
			} catch (Exception e) {
				e.printStackTrace();
				return error(e.getMessage());
			}
		}else {
			if(studentIds == null) {
				studentIds = new String[] {};
			}
			
			NewGkDivideClass divideClass = newGkDivideClassService.findById(divide.getUnitId(),classId, true);
			if(StringUtils.isNotBlank(divideClass.getRelateId())) {
				NewGkDivideClass zhbClass = newGkDivideClassService.findById(divide.getUnitId(), divideClass.getRelateId(), true);
				if(!BaseConstants.ZERO_GUID.intern().equals(zhbClass.getSubjectIds())) {
					// 是合班数据
					if(studentIds.length == 0) {
						// 删除合班
						try {
							newGkDivideClassService.deleteById(divide.getUnitId(), divideId, classId);
						} catch (Exception e) {
							e.printStackTrace();
							return error(e.getMessage());
						}
						return returnSuccess();
					}
					
					Set<String> studentIdSet = new HashSet<>(zhbClass.getStudentList());
					studentIdSet.addAll(Arrays.asList(studentIds));
					studentIds = studentIdSet.toArray(new String[0]);
				}else {
					// 混合班 对应 教学班
					Set<String> zhbStuIds = new HashSet<>(zhbClass.getStudentList());
					Set<String> studentIdSet = divideClass.getStudentList().stream()
							.filter(e->zhbStuIds.contains(e)).collect(Collectors.toSet());
					studentIdSet.addAll(Arrays.asList(studentIds));
					studentIds = studentIdSet.toArray(new String[0]);
				}
			}
			// 调整学生
			List<NewGkClassStudent> classStudentList = makeStudents(divide,studentIds, classId, date);
			
			try {
				String[] divideClassIds=null;
				if(StringUtils.isNotBlank(classId)) {
					divideClassIds=new String[] {classId};
				}
				newGkClassStudentService.saveOrSaveList(divide.getUnitId(), divideId, divideClassIds, classStudentList);
			} catch (Exception e) {
				e.printStackTrace();
				return error(e.getMessage());
			}
		}
		
		return returnSuccess();
	}

	@ResponseBody
	@RequestMapping("/deleteManualClass")
	public String deleteManualClass(String classId) {
		if(StringUtils.isBlank(classId)) {
			return error("classId为空");
		}
		try {
			NewGkDivideClass divideClass = newGkDivideClassService.findOne(classId);
			if(StringUtils.isNotBlank(divideClass.getRelateId())) {
				// 混合班所关联的教学班不能删除
//				return error("组合班所关联的教学班不能删除");
			}
			
			newGkDivideClassService.deleteById(this.getLoginInfo().getUnitId(), divideClass.getDivideId(), classId);
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		return returnSuccess();
	}
	private List<NewGkClassStudent> makeStudents(NewGkDivide divide,String[] studentIds, String classId, Date date) {
		List<NewGkClassStudent> classStudentList = new ArrayList<>();
		NewGkClassStudent stu;
		for (String stuId : studentIds) {
			stu = new NewGkClassStudent();
			stu.setId(UuidUtils.generateUuid());
			stu.setCreationTime(date);
			stu.setModifyTime(date);
			stu.setClassId(classId);
			stu.setStudentId(stuId);
			stu.setDivideId(divide.getId());
			stu.setUnitId(divide.getUnitId());
			classStudentList.add(stu);
		}
		return classStudentList;
	}
	
	/**
	 * 走班学生分布 首页  所有需要安排的走班数目 包括混合所有走班以及2+x中第3批次x
	 * @param divideId
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showStudentDistribution/page")
	public String showStudentDistribution(@PathVariable String divideId, ModelMap modelMap) {
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		boolean canEdit = !NewGkElectiveConstant.IF_1.equals(divide.getStat());
		if(canEdit) {
			canEdit = !isNowDivide(divide.getId()); 
		}
		modelMap.put("canEdit", canEdit);
		
		//所有组合班
		List<NewGkDivideClass> hhbList = newGkDivideClassService.findByDivideIdAndClassTypeSubjectType(divide.getUnitId(), 
				divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_0},
				true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, null);
		Map<String,NewGkDivideClass> hhbMap = EntityUtils.getMap(hhbList, e->e.getId());
		
		List<NewGkDivideClass> twoClassList = hhbList.stream().filter(e->NewGkElectiveConstant.SUBJECT_TYPE_2.equals(e.getSubjectType()) ).collect(Collectors.toList());
		
		//混合班批次时间点数据
		List<NewGkClassBatch> batchClassList = classBatchService.findByDivideClsIds(hhbMap.keySet().toArray(new String[0]));
		
		Map<String,Set<String>> subjectChoMap = new HashMap<>();
		Map<String,Set<String>> stuChoose=new HashMap<>();
		makeStudentChooseResult(divide, subjectChoMap, stuChoose);
		Set<String> subjectIds =subjectChoMap.keySet();

		List<Course> courseList = courseRemoteService.findListObjectByIds(subjectIds.toArray(new String[0]));
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, e->e.getId());
		
		BatchClassDto dto;
		Map<String,BatchClassDto> dtoMap = new TreeMap<>();
		
		List<NewGkOpenSubject> openASubjectList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeIn(divideId, new String[] {NewGkElectiveConstant.SUBJECT_TYPE_A});
		Set<String> openASubject=EntityUtils.getSet(openASubjectList, e->e.getSubjectId());
		
		if(CollectionUtils.isNotEmpty(twoClassList)) {
			for(NewGkDivideClass ts:twoClassList) {
				if(CollectionUtils.isNotEmpty(ts.getStudentList())) {
					for(String s:ts.getStudentList()) {
						Set<String> set = stuChoose.get(s);
						if(CollectionUtils.isEmpty(set)) {
							continue;
						}
						for(String sw:set) {
							if(!openASubject.contains(sw)) {
								continue;
							}
							if(ts.getSubjectIds().indexOf(sw)<=-1) {
								dto = dtoMap.get(ts.getId());
								if(dto == null) {
									dto = new BatchClassDto();
									dtoMap.put(ts.getId(), dto);
									dto.setDevideClass(hhbMap.get(ts.getId()));
									dto.setMap(new HashMap<>());
								}
								Map<String, Map<String, Integer>> map = dto.getMap();
								if(!map.containsKey(divide.getBatchCountTypea().toString())) {
									map.put(divide.getBatchCountTypea().toString(), new HashMap<>());
								}
								if(!map.get(divide.getBatchCountTypea().toString()).containsKey(sw)) {
									map.get(divide.getBatchCountTypea().toString()).put(sw, 0);
								}
								map.get(divide.getBatchCountTypea().toString()).put(sw, map.get(divide.getBatchCountTypea().toString()).get(sw)+1);
							}
						}
					}
				}
				
			}
		}
		
		
		
		
		
		for (NewGkClassBatch cb : batchClassList) {
			String subjectId = cb.getSubjectId();
			if(!openASubject.contains(subjectId)) {
				continue;
			}
			String[] subjectIds2 = cb.getSubjectIds().split(",");
			String classId = cb.getDivideClassId();
			String batch = cb.getBatch();
			dto = dtoMap.get(classId);
			if(dto == null) {
				dto = new BatchClassDto();
				dtoMap.put(classId, dto);
				dto.setDevideClass(hhbMap.get(classId));
				dto.setMap(new HashMap<>());
			}
			Map<String, Map<String, Integer>> map = dto.getMap();
			if(!map.containsKey(batch)) {
				map.put(batch, new HashMap<>());
			}
			if(!map.get(batch).containsKey(subjectId)) {
				map.get(batch).put(subjectId, 0);
			}
			Set<String> subjStus1 = subjectChoMap.get(subjectIds2[0]);
			Set<String> subjStus2 = subjectChoMap.get(subjectIds2[1]);
			Set<String> subjStus3 = subjectChoMap.get(subjectIds2[2]);
			List<String> studentList = dto.getDevideClass().getStudentList().stream()
					.filter(e->subjStus1.contains(e)&&subjStus2.contains(e)&&subjStus3.contains(e))
					.collect(Collectors.toList());
			if(map==null || map.get(batch).get(subjectId)==null || studentList == null || map.get(batch) == null) {
				System.out.println();
			}
			int num = map.get(batch).get(subjectId)+studentList.size();
			map.get(batch).put(subjectId, num);
		}
		
		
		Map<String,BatchClassDto> dtoMap2 = new TreeMap<>();
		modelMap.put("courseMap", courseMap);
		
		for (BatchClassDto dto2 : dtoMap.values()) {
			if(dto2.getMap()==null || dto2.getMap().size()==0) {
				continue;
			}
			Integer num = dto2.getMap().values().stream().map(e->e.size()).max(Integer::compare).orElse(0);
			dto2.setMaxCourseNum(num);
			dtoMap2.put(dto2.getDevideClass().getId(), dto2);
		}
		modelMap.put("dtoMap", dtoMap2);
		
		return "/newgkelective/clsBatch/batchStulct.ftl";
	}
	@ResponseBody
	@RequestMapping("/divideClass/updateGroupClassName")
	@ControllerInfo(value="修改组合班名称")
	public String saveDivideName(@PathVariable String divideId, String classId, String className) {
		List<NewGkDivideClass> clazzList = newGkDivideClassService.findByDivideIdAndClassTypeWithMaster(this.getLoginInfo().getUnitId(), divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_0}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		if(CollectionUtils.isNotEmpty(clazzList)) {
			NewGkDivideClass toCh = null;
			for(NewGkDivideClass ch : clazzList) {
				if(ch.getId().equals(classId)) {
					toCh = ch;
				} else if(ch.getClassName().equals(className)) {
					return error(className+"这个名称已被其他班级占用！");
				}
			}
			if(toCh == null) {
				return error("班级不存在或已被删除！");
			}
			toCh.setClassName(className);
			toCh.setModifyTime(new Date());
			newGkDivideClassService.save(toCh);;
		}else {
			return error("班级不存在或已被删除！");
		}
		
		return returnSuccess();
	}

	/**
	 * 公用代码
	 * @param divide
	 * @param subjectChoMap // K:科目 V：学生
	 * @param studentChooseMap // K:学生 V：科目
	 */
	private void makeStudentChooseResult(NewGkDivide divide,Map<String,Set<String>> subjectChoMap,Map<String,Set<String>> studentChooseMap) {
		List<NewGkChoResult> choList = newGkChoResultService.findByChoiceIdAndKindType(divide.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,divide.getChoiceId());
		
		for (NewGkChoResult chr : choList) {
			if(!subjectChoMap.containsKey(chr.getSubjectId())) {
				subjectChoMap.put(chr.getSubjectId(), new HashSet<>());
			}
			subjectChoMap.get(chr.getSubjectId()).add(chr.getStudentId());
			if(!studentChooseMap.containsKey(chr.getStudentId())) {
				studentChooseMap.put(chr.getStudentId(), new HashSet<>());
			}
			studentChooseMap.get(chr.getStudentId()).add(chr.getSubjectId());
		}
	}
	
	//最后一个批次2+x各科目数据
	/**
	 * 2+x数据的X
	 * @param twoClassList 2科组合的班级，包括学生数据
	 * @param chooseByStuId 学生选课结果
	 * @Return studentXBySubIdClassId 返回 key:classId,key:subjectId value studentId
	 */
	private Map<String,Map<String,Set<String>>>  makeStudentX(List<NewGkDivideClass> twoClassList,Map<String,Set<String>> chooseByStuId ) {
		Map<String,Map<String,Set<String>>> studentXBySubIdClassId=new HashMap<>();
		Set<String> set ;
		if(CollectionUtils.isEmpty(twoClassList)) {
			return studentXBySubIdClassId;
		}
		for(NewGkDivideClass ps:twoClassList) {
			if(CollectionUtils.isNotEmpty(ps.getStudentList())) {
				for(String s:ps.getStudentList()) {
					set= chooseByStuId.get(s);
					if(CollectionUtils.isEmpty(set)) {
						continue;
					}
					for(String s1:set) {
						if(ps.getSubjectIds().indexOf(s1)>-1) {
							continue;
						}
						if(!studentXBySubIdClassId.containsKey(ps.getId())) {
							studentXBySubIdClassId.put(ps.getId(), new HashMap<>());
						}
						Map<String, Set<String>> map = studentXBySubIdClassId.get(ps.getId());
						if(!map.containsKey(s1)) {
							map.put(s1, new HashSet<>());
						}
						map.get(s1).add(s);
					}
				}
			}
		}
		return studentXBySubIdClassId;
	}
	
	
	
	@RequestMapping("/showBatchJxbClass/index")
	public String showBatchJxbClass(@PathVariable String divideId, BatchClassDto batchDto, String useMaster, ModelMap modelMap) {
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		//批次不是最后一个批次可以合班2科组合
		//批次为最后一个批次 只能合班3科组合
		//最后一个批次包括2+x中x
		String batch = batchDto.getBatch();
		Course course = batchDto.getCourse();
//		String subjectId = null;
//		if(course != null) {
//			subjectId = course.getId();
//		}
		//暂时默认不会超过3个批次
		List<String> batchs = Arrays.asList("1","2","3");
		if(StringUtils.isBlank(batch)) {
			batch = "1";
		}
		boolean isEndBath=false;
		String endBath=divide.getBatchCountTypea().toString();
		if(endBath.equals(batch)) {
			isEndBath=true;
		}
		//具体开设科目
		List<NewGkOpenSubject> openASubjectList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeIn(divideId, new String[] {NewGkElectiveConstant.SUBJECT_TYPE_A});
		Set<String> openSubjectIds = EntityUtils.getSet(openASubjectList,e->e.getSubjectId());
		
		//暂时用不到classId
		String classId = batchDto.getClassId();
		
		//某个批次下的科目信息
		//1：批次表
		List<NewGkDivideClass> allClassList = null; 
		if(Objects.equals(useMaster, "1"))
			allClassList = newGkDivideClassService.findByDivideIdAndClassTypeWithMaster(divide.getUnitId(), 
				divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_0}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		else
			allClassList = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), 
					divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_0}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		
		
		List<NewGkDivideClass> classList = allClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType()))
				.collect(Collectors.toList());
		
		List<NewGkDivideClass> hhbList = allClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType()) 
						&& BaseConstants.ZERO_GUID.equals(e.getSubjectIds()))
				.collect(Collectors.toList());
		
		List<NewGkDivideClass> twoList = allClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType()) 
						&& NewGkElectiveConstant.SUBJECT_TYPE_2.equals(e.getSubjectType()))
				.collect(Collectors.toList());
		
		Map<String, NewGkDivideClass> hhbMap = EntityUtils.getMap(hhbList, e->e.getId());
		modelMap.put("batch", batch);
		
		final String batch2 = batch;
//		Set<String> subjectIds = classList.stream().filter(e->batch2.equals(e.getBatch()))
//				.map(e->e.getSubjectIds()).collect(Collectors.toSet());
		
		List<NewGkClassBatch> classBatchList = classBatchService.findbyBatchAndSubjectId(divideId, batch, null);
		Set<String> subjectIds=new HashSet<>();//显示的科目下拉框
		Set<String> subjectIds1 = classBatchList.stream()
				.filter(e->batch2.equals(e.getBatch()) && openSubjectIds.contains(e.getSubjectId()))
				.map(e->e.getSubjectId()).collect(Collectors.toSet());
		if(CollectionUtils.isNotEmpty(subjectIds1)) {
			subjectIds.addAll(subjectIds1);
		}
		
		
		Map<String, Set<String>> subjectChoMap=new HashMap<>();
		Map<String, Set<String>> studentChooseMap=new HashMap<>();
		makeStudentChooseResult(divide, subjectChoMap, studentChooseMap);
		//key:bath subjectId
		Map<String,Map<String,Set<String>>> twoXstuId=new HashMap<>();
		if(isEndBath) {
			if(subjectIds.size()==openSubjectIds.size()) {
				//科目类表下已经达到最大值，无需到2+x中再去查询
			}else {
				if(CollectionUtils.isNotEmpty(twoList)) {
					Map<String, Map<String, Set<String>>> studentXBySubIdClassId = makeStudentX(twoList, studentChooseMap);
					if(MapUtils.isNotEmpty(studentXBySubIdClassId)) {
						twoXstuId.put(endBath, new HashMap<>());
						for(Entry<String, Map<String, Set<String>>> ttt:studentXBySubIdClassId.entrySet()) {
							Map<String, Set<String>> value = ttt.getValue();
							
							if(MapUtils.isNotEmpty(value)) {
								for(Entry<String, Set<String>> tttt:value.entrySet()) {
									subjectIds.add(tttt.getKey());
									if(!twoXstuId.get(endBath).containsKey(tttt.getKey())) {
										twoXstuId.get(endBath).put(tttt.getKey(), new HashSet<>());
									}
									twoXstuId.get(endBath).get(tttt.getKey()).addAll(tttt.getValue());
								}
							}
							
						}
					}
				}
			}
		}
		
		
		
		if(CollectionUtils.isEmpty(subjectIds)) {
//			return errorFtl(modelMap, "没有需要合班的学生");
			return "/newgkelective/clsBatch/showJxbStuPage.ftl";
		}
		List<Course> courseList = courseRemoteService.findListObjectByIds(subjectIds.toArray(new String[0]));
		Map<String,Course>couMap = EntityUtils.getMap(courseList, Course::getId);
		if(course==null) {
			course = courseList.get(0);
		}else if(!couMap.keySet().contains(course.getId())){
			course = courseList.get(0);
		}else if(couMap.keySet().contains(course.getId())){
			course = couMap.get(course.getId());
		}
		final String subjectId2 = course.getId();
		
		
		
		
		List<String> allStuIds = new ArrayList<>();
		for (NewGkClassBatch cb : classBatchList) {
			if(!course.getId().equals(cb.getSubjectId())) {
				continue;
			}
			
			String[] subjectIds2 = cb.getSubjectIds().split(",");
			Set<String> subjectStus1 = subjectChoMap.get(subjectIds2[0]);
			Set<String> subjectStus2 = subjectChoMap.get(subjectIds2[1]);
			Set<String> subjectStus3 = subjectChoMap.get(subjectIds2[2]);
			
			List<String> studentIds = hhbMap.get(cb.getDivideClassId()).getStudentList().stream()
					.filter(e->subjectStus1.contains(e)
							&& subjectStus2.contains(e)
							&& subjectStus3.contains(e))
					.collect(Collectors.toList());
			allStuIds.addAll(studentIds);
		}
		if(isEndBath) {
			Map<String, Set<String>> twoSubjectStuId = twoXstuId.get(batch);
			if(MapUtils.isNotEmpty(twoSubjectStuId) && CollectionUtils.isNotEmpty(twoSubjectStuId.get(course.getId()))) {
				allStuIds.addAll(twoSubjectStuId.get(course.getId()));
			}
		}
		
		// 获取已经分配的学生
		Set<String> rangedStuIds = classList.stream().filter(e->batch2.equals(e.getBatch()) && subjectId2.equals(e.getSubjectIds()))
				.flatMap(e->e.getStudentList().stream()).collect(Collectors.toSet());
		allStuIds.removeAll(rangedStuIds);
		
		List<NewGkDivideClass> zhbList = new ArrayList<>(allClassList);
		zhbList.removeAll(classList);
		List<Student> stulist =studentRemoteService.findListObjectByIds(allStuIds.toArray(new String[0]));
		makeStudentDto(divide, course, stulist, zhbList, modelMap);
		
		
		
		modelMap.put("batchs", batchs);
		modelMap.put("courseList", courseList);
//		modelMap.put("divideClassList", divideClassList);
		modelMap.put("subjectId", course.getId());
		modelMap.put("classId", classId);
		
		
		// 显示可合班的班级  .纯三科组合班
		List<NewGkDivideClass> pureClassList =new ArrayList<>();
		if(isEndBath) {
			 pureClassList = zhbList.stream()
					.filter(e->NewGkElectiveConstant.SUBJECT_TYPE_3.equals(e.getSubjectType())).collect(Collectors.toList());
		}else {
			pureClassList = zhbList.stream()
					.filter(e->!BaseConstants.ZERO_GUID.equals(e.getSubjectIds())).collect(Collectors.toList());
		}
		
		
		Map<String, List<NewGkDivideClass>> relatedClassMap = classList.stream().filter(e->StringUtils.isNotBlank(e.getRelateId()))
				.collect(Collectors.groupingBy(e->e.getRelateId()));
		List<NewGkDivideClass> showPureList = new ArrayList<>();
		for (NewGkDivideClass clz : pureClassList) {
			if(clz.getSubjectIds().contains(course.getId())) {
//				clz.setStudentCount(clz.getStudentList().size());
//				showPureList.add(clz);
				
				if(relatedClassMap.containsKey(clz.getId())) {
					boolean flag = false;
					List<NewGkDivideClass> list = relatedClassMap.get(clz.getId());
					if(list.size() >= 3) {
						continue;
					}
					List<String> usedBatch = new ArrayList<>();
					for (NewGkDivideClass clz2 : list) {
						usedBatch.add(clz2.getBatch());
						if(course.getId().equals(clz2.getSubjectIds()) && batch.equals(clz2.getBatch())) {
							// 找到已经开的班级
							clz.setStudentCount(clz2.getStudentList().size());
							showPureList.add(clz);
							flag = true;
							break;
						}else if(course.getId().equals(clz2.getSubjectIds())) {
							flag = true;
							break;
						}
					}
					
					if(!usedBatch.contains(batch) && !flag) {
						clz.setStudentCount(clz.getStudentList().size());
						showPureList.add(clz);
					}
				}else {
					clz.setStudentCount(clz.getStudentList().size());
					showPureList.add(clz);
				}
			}
		}
		modelMap.put("showPureList", showPureList);
		return "/newgkelective/clsBatch/showJxbStuPage.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/doCombine")
	public String couldCombineClass(@PathVariable String divideId,
			@RequestParam("stuIds[]") String[] stuIds,
			@RequestParam(name="oldClassId",required=false)String oldClassId, BatchClassDto batchDto) {
		String batch = batchDto.getBatch();
		String classId = batchDto.getClassId();
		String subjectId = batchDto.getCourse().getId();
		NewGkDivide divide = newGkDivideService.findById(divideId);
		if(divide==null) {
			return error("分班方案不存在");
		}
		if(StringUtils.isBlank(batch)) {
			return error("时间点为空");
		}
		
		NewGkDivideClass zhb = newGkDivideClassService.findById(divide.getUnitId(), classId, true);
//		String[] subjectStr = zhb.getSubjectIds().split(",");
		List<NewGkDivideClass> jxbList = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), 
				divideId,
				new String[] {NewGkElectiveConstant.CLASS_SOURCE_TYPE2}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		NewGkDivideClass theClass = jxbList.stream()
				.filter(e->subjectId.equals(e.getSubjectIds()) && classId.equals(e.getRelateId()))
				.findFirst().orElse(null);
		
		Date date = new Date();
		List<String> studentIdList = new ArrayList<>(Arrays.asList(stuIds));
		boolean isClassAdd=false;
		if(theClass == null) {
			Course course = courseRemoteService.findOneObjectById(subjectId);
			List<NewGkDivideClass> subjectClassList = newGkDivideClassService.findClassBySubjectIds(divide.getUnitId(), divideId,
					NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_2, subjectId, false);
			Set<String> classNames = EntityUtils.getSet(subjectClassList, NewGkDivideClass::getClassName);
			int classIndex = subjectClassList.size()+1;
			theClass = new NewGkDivideClass();
			theClass.setId(UuidUtils.generateUuid());
			theClass.setDivideId(divideId);
			theClass.setCreationTime(date);
			theClass.setModifyTime(date);
			theClass.setBatch(batch);
			theClass.setSubjectIds(subjectId);
			theClass.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
			theClass.setClassType(NewGkElectiveConstant.CLASS_TYPE_2);
			theClass.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
			
			String className = course.getSubjectName()+"A"+classIndex++ +"班";
			while(classNames.contains(className)) {
				className = course.getSubjectName()+"A"+classIndex++ +"班";
			}
			theClass.setClassName(className);
			theClass.setIsHand(NewGkElectiveConstant.IS_HAND_1);
			theClass.setRelateId(zhb.getId());
			studentIdList.addAll(zhb.getStudentList());
			
			isClassAdd=true;
		}
//		List<NewGkClassStudent> oldStuList = newGkClassStudentService.findListByClassIds(new String[] {theClass.getId()});
		List<NewGkClassStudent> makeStudents = makeStudents(divide,studentIdList.toArray(new String[0]),
				theClass.getId(), date);
		
		try {
			if(isClassAdd) {
				newGkDivideClassService.updateMoveStudents(oldClassId,stuIds,Arrays.asList(theClass),makeStudents);
			}else {//班级不需要修改
				newGkDivideClassService.updateMoveStudents(oldClassId,stuIds,null,makeStudents);
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
			return error(e1.getMessage());
		}
		
		return returnSuccess();
	}
	
	@RequestMapping("/showCombineInfo")
	public String showCombineInfo(@PathVariable String divideId,ModelMap map) {
		List<NewGkDivideClass> allClassList = newGkDivideClassService.findByDivideIdAndClassType(this.getLoginInfo().getUnitId(), 
				divideId,
				new String[] {NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_2}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<NewGkDivideClass> pureClassList = allClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType())
						&& !BaseConstants.ZERO_GUID.equals(e.getSubjectIds()))
				.collect(Collectors.toList());
		Map<String,NewGkDivideClass> pureClassMap = EntityUtils.getMap(pureClassList, e->e.getId());
		Set<String> pureClassIds = pureClassMap.keySet();
		
		// 合成班
		List<NewGkDivideClass> hcbList = allClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType()) 
						&& pureClassIds.contains(e.getRelateId())).collect(Collectors.toList());
		Set<String> hcbListIds = EntityUtils.getSet(hcbList, e->e.getSubjectIds());
		List<Course> courseList = courseRemoteService.findListObjectByIds(hcbListIds.toArray(new String[0]));
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, e->e.getId());
		
		Map<String,Map<Course,List<NewGkDivideClass>>> dtoMap = new HashMap<>();
		for (NewGkDivideClass clz : hcbList) {
			NewGkDivideClass clazz = pureClassMap.get(clz.getRelateId());
			if(clazz == null)
				continue;
			clz.setRelateName(clazz.getClassName());
			clz.setStudentCount(clazz.getStudentList().size());
			
			Course course = courseMap.get(clz.getSubjectIds());
			if(clz.getBatch() == null)
				clz.setBatch("1");
			if(!dtoMap.containsKey(clz.getBatch())) {
				dtoMap.put(clz.getBatch(), new HashMap<>());
			}
			if(!dtoMap.get(clz.getBatch()).containsKey(course)) {
				dtoMap.get(clz.getBatch()).put(course, new ArrayList<>());
			}
			dtoMap.get(clz.getBatch()).get(course).add(clz);
		}
		
		
		Map<String, List<BatchClassDto>> dtoMap2 = new HashMap<>();
		Map<String,Integer> batchCountMap = new HashMap<>();
		BatchClassDto dto;
		for (String batch : dtoMap.keySet()) {
			Map<Course, List<NewGkDivideClass>> map2 = dtoMap.get(batch);
			int count = (int)map2.values().stream().flatMap(e->e.stream()).count();
			batchCountMap.put(batch, count);
			
			dtoMap2.put(batch, new ArrayList<>());
			List<BatchClassDto> list = dtoMap2.get(batch);
			for (Course course : map2.keySet()) {
				List<NewGkDivideClass> list2 = map2.get(course);
				dto = new BatchClassDto();
				dto.setCourse(course);
				dto.setDevideClassList(list2);
				list.add(dto);
			}
		}
		
		map.put("divideId", divideId);
		map.put("dtoMap", dtoMap2);
		map.put("batchCountMap", batchCountMap);
		return "/newgkelective/clsBatch/combineInfo.ftl";
	}
	
	/**
	 * 解散 合班
	 * @param classId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/dismissClass")
	public String dismissClass(@PathVariable String divideId,String classId) {
		try {
			newGkDivideClassService.deleteById(this.getLoginInfo().getUnitId(), divideId, classId);
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		return returnSuccess();
	}
	
	/**
	 * 解散指定批次 指定科目下的教学班
	 * @param divideId
	 * @param batchDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/dismissAllClass")
	public String dismissAllClass(@PathVariable String divideId, BatchClassDto batchDto) {
		String batch = batchDto.getBatch();
		if(StringUtils.isBlank(batch)) {
			return error("时间点为空");
		}
		Course course = batchDto.getCourse();
		if(course == null || StringUtils.isBlank(course.getId())) {
			return error("科目为空");
		}
		String subjectId = course.getId();
		String unitId=this.getLoginInfo().getUnitId();
		List<NewGkDivideClass> subjectClassList = newGkDivideClassService.findClassBySubjectIds(unitId, divideId,
				NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_2, subjectId, true);
		
		List<NewGkDivideClass> zhbList = newGkDivideClassService.findClassBySubjectIds(unitId, divideId,
				NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_0, null, true);
		Map<String, NewGkDivideClass> zhbMap = EntityUtils.getMap(zhbList, NewGkDivideClass::getId);
		Set<String> delClassIds = new HashSet<>();
//		Map<String,List<String>> classIdStuIdMap = new HashMap<>();
		for (NewGkDivideClass clz : subjectClassList) {
			NewGkDivideClass relatedClass = zhbMap.get(clz.getRelateId());
			
			if(batch.equals(clz.getBatch()) && relatedClass == null) {
				// 和组合班没有关联直接删除 
				delClassIds.add(clz.getId());
			}else if(batch.equals(clz.getBatch()) && !BaseConstants.ZERO_GUID.equals(relatedClass.getSubjectIds())){
				// 是 合班 对应的 教学班
				delClassIds.add(clz.getId());
			}else if(batch.equals(clz.getBatch()) 
					&& BaseConstants.ZERO_GUID.equals(relatedClass.getSubjectIds())) {
				delClassIds.add(clz.getId());
				// 是 混合班 对应的 教学班
//				Set<String> zhbStudents = new HashSet<>(relatedClass.getStudentList());
//				List<String> delStudentIds = clz.getStudentList().stream().filter(e->!zhbStudents.contains(e)).collect(Collectors.toList());
//				classIdStuIdMap.put(clz.getId(), delStudentIds);
			}
		}
		if(CollectionUtils.isEmpty(delClassIds)) {
			return success("没有需要解散");
		}
		try {
			newGkDivideClassService.deleteByClassIdIn(unitId,divideId, delClassIds.toArray(new String[0]));
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		return success("解散成功");
	}
	
	
	/**
	 * 验证是不是所有学生都安排啦
	 * @param divideId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/manualDivide/checkStudent")
	public String checkStudent(@PathVariable String divideId, ModelMap modelMap) {
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		List<NewGkDivideClass> classList = newGkDivideClassService.findByDivideIdAndClassTypeWithMaster(divide.getUnitId(), 
				divideId,
				new String[] {NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_2}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		//所有选课结果
		List<StudentResultDto> stuChooselist = newGkChoResultService
				.findGradeIdList(divide.getUnitId(),divide.getGradeId(),divide.getChoiceId(), null);
		List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService
				.findByDivideIdAndSubjectTypeIn(divideId, new String[] {
						NewGkElectiveConstant.SUBJECT_TYPE_A});
		Set<String> subIds=EntityUtils.getSet(openSubjectList, e->e.getSubjectId());
		
		//验证所有混合班学生是否都安排啦
		Map<String,List<NewGkDivideClass>> jxbByStudent=new HashMap<>();
		Set<String> needStuIds=new HashSet<>();//混合班学生
		Map<String,String> twoSubIdByStudent=new HashMap<>();
		Set<String> noNeedStuIds=new HashSet<>();//3+0
		List<NewGkDivideClass> twoList=new ArrayList<>();
		for(NewGkDivideClass n:classList) {
			if(CollectionUtils.isEmpty(n.getStudentList())) {
				continue;
			}
			if(NewGkElectiveConstant.CLASS_TYPE_0.equals(n.getClassType())) {
				if(NewGkElectiveConstant.SUBJTCT_TYPE_0.equals(n.getSubjectType())) {
					//混合
					needStuIds.addAll(n.getStudentList());
				}else if(NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(n.getSubjectType())) {
					twoList.add(n);
					//2+x
					for(String s:n.getStudentList()) {
						twoSubIdByStudent.put(s, n.getSubjectIds());
					}
					
				}else {
					noNeedStuIds.addAll(n.getStudentList());
				}
			}else {
				if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(n.getSubjectType())) {
					for(String s:n.getStudentList()) {
						if(!jxbByStudent.containsKey(s)) {
							jxbByStudent.put(s, new ArrayList<>());
						}
						jxbByStudent.get(s).add(n);
					}
				}
			}
		}
		
		for(StudentResultDto sd:stuChooselist) {
			if(CollectionUtils.isEmpty(sd.getResultList())) {
				continue;
			}
			if(noNeedStuIds.contains(sd.getStudentId())){
				continue;
			}
			List<NewGkChoResult> rList = sd.getResultList();
			Set<String> chooseSet = EntityUtils.getSet(rList, NewGkChoResult::getSubjectId);
			Set<String> arrId=new HashSet<>();
			String twosubIds="";
			if(twoSubIdByStudent.containsKey(sd.getStudentId())) {
				twosubIds=twoSubIdByStudent.get(sd.getStudentId());
			}
			for(String ss:chooseSet){
				if(twosubIds.indexOf(ss)>-1) {
					continue;
				}
				if(subIds.contains(ss)) {
					arrId.add(ss);
				}
			}
			
			
			
			if(CollectionUtils.isNotEmpty(arrId) || StringUtils.isNotBlank(twosubIds)) {
				if(jxbByStudent.containsKey(sd.getStudentId())) {
					List<NewGkDivideClass> jxblist = jxbByStudent.get(sd.getStudentId());
					if(StringUtils.isNotBlank(twosubIds)) {
						Map<String,String> bathMap=new HashMap<>();
						Set<String> baths=new HashSet<>();
						//至少要安排的科目
						int count=0;
						for(NewGkDivideClass c:jxblist) {
							if(!arrId.contains(c.getSubjectIds()) && twosubIds.indexOf(c.getSubjectIds())==-1) {
								return error("学生"+sd.getStudentName()+"安排选考班级有误");
							}
							if(bathMap.containsKey(c.getSubjectIds())) {
								//重复安排科目
								return error("学生"+sd.getStudentName()+"安排选考班级有误");
							}
							if(baths.contains(c.getBatch())) {
								//安排时间重复
								return error("学生"+sd.getStudentName()+"安排选考班级有误");
							}
							baths.add(c.getBatch());
							bathMap.put(c.getSubjectIds(), c.getBatch());
							if(arrId.contains(c.getSubjectIds()) ){
								count=count+1;
							}
						}
						if(count<arrId.size()) {
							return error("存在学生没有安排选考");
						}
					}else {
						if(jxblist.size()<arrId.size()) {
							return error("存在学生没有安排选考");
						}else if(jxblist.size()>arrId.size()) {
							return error("学生"+sd.getStudentName()+"安排选考班级有误");
						}else {
							Map<String,String> bathMap=new HashMap<>();
							Set<String> baths=new HashSet<>();
							for(NewGkDivideClass c:jxblist) {
								if(!arrId.contains(c.getSubjectIds())) {
									return error("学生"+sd.getStudentName()+"安排选考班级有误");
								}
								if(bathMap.containsKey(c.getSubjectIds())) {
									//重复安排科目
									return error("学生"+sd.getStudentName()+"安排选考班级有误");
								}
								if(baths.contains(c.getBatch())) {
									return error("学生"+sd.getStudentName()+"安排选考班级有误");
								}
								baths.add(c.getBatch());
								bathMap.put(c.getSubjectIds(), c.getBatch());
							}
						}
					}
					
				}else {
					return error("存在学生没有安排选考");
				}
			}
		}
		//修改2+xBath
		if(CollectionUtils.isNotEmpty(twoList)) {
			for(NewGkDivideClass two:twoList) {
				int batha=divide.getBatchCountTypea();
				String s="";
				for(int i=1;i<batha;i++) {
					s=s+","+i;
				}
				if(StringUtils.isNotBlank(s)) {
					two.setBatch(s.substring(1));
				}
				
			}
			try {
				newGkDivideClassService.saveAll(twoList.toArray(new NewGkDivideClass[] {}));
			}catch (Exception e) {
				return error("操作失败");
			}
		}
		


//		List<NewGkDivideClass> hhbList = classList.stream()
//				.filter(e->NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType())
//						&& BaseConstants.ZERO_GUID.equals(e.getSubjectIds())).collect(Collectors.toList());
//		List<NewGkDivideClass> pureList = classList.stream()
//				.filter(e->NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType())
//						&& !BaseConstants.ZERO_GUID.equals(e.getSubjectIds())).collect(Collectors.toList());
//		Map<String, NewGkDivideClass> pureClassMap = EntityUtils.getMap(pureList, "id");
//		
//		Map<String,NewGkDivideClass> hhbMap = EntityUtils.getMap(hhbList, "id");
//		List<NewGkDivideClass> jxbList = classList.stream()
//				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())).collect(Collectors.toList());
//		
//		List<NewGkClassBatch> clasBatchList = classBatchService.findListByIn("divideClassId", 
//				hhbMap.keySet().toArray(new String[] {}));
//		
//		// 获取选课数据
//		List<NewGkChoResult> choList = newGkChoResultService.findByChoiceIdAndKindType(divide.getChoiceId(), NewGkElectiveConstant.KIND_TYPE_01);
//		Map<String,Set<String>> subjectChoMap = new HashMap<>();
//		for (NewGkChoResult chr : choList) {
//			if(!subjectChoMap.containsKey(chr.getSubjectId())) {
//				subjectChoMap.put(chr.getSubjectId(), new HashSet<>());
//			}
//			subjectChoMap.get(chr.getSubjectId()).add(chr.getStudentId());
//		}
//		
//		
//		Map<String,Map<String, Set<String>>> batchSubjectMap = new HashMap<>();
//		for (NewGkClassBatch cb : clasBatchList) {
//			String[] subjectIds2 = cb.getSubjectIds().split(",");
//			if(!batchSubjectMap.containsKey(cb.getBatch())) {
//				batchSubjectMap.put(cb.getBatch(), new HashMap<>());
//			}
//			Map<String, Set<String>> map = batchSubjectMap.get(cb.getBatch());
//			if(!map.containsKey(cb.getSubjectId())) {
//				map.put(cb.getSubjectId(), new HashSet<>());
//			}
//			NewGkDivideClass claz = hhbMap.get(cb.getDivideClassId());
////			Set<String> students = subjectChoMap.get(cb.getSubjectId());
////			List<String> collect = claz.getStudentList().stream().filter(e->students.contains(e)).collect(Collectors.toList());
//			Set<String> students1 = subjectChoMap.get(subjectIds2[0]);
//			Set<String> students2 = subjectChoMap.get(subjectIds2[1]);
//			Set<String> students3 = subjectChoMap.get(subjectIds2[2]);
//			List<String> collect = claz.getStudentList().stream()
//					.filter(e->students1.contains(e)
//							&&students2.contains(e)
//							&&students3.contains(e))
//					.collect(Collectors.toList());
//			map.get(cb.getSubjectId()).addAll(collect);
//		}
//		
//		Map<String,Map<String, Set<NewGkDivideClass>>> batchSubjectClassMap = new HashMap<>();
//		for (NewGkDivideClass jxb : jxbList) {
//			if(!batchSubjectClassMap.containsKey(jxb.getBatch())) {
//				batchSubjectClassMap.put(jxb.getBatch(), new HashMap<>());
//			}
//			Map<String, Set<NewGkDivideClass>> map = batchSubjectClassMap.get(jxb.getBatch());
//			if(!map.containsKey(jxb.getSubjectIds())) {
//				map.put(jxb.getSubjectIds(), new HashSet<>());
//			}
//			map.get(jxb.getSubjectIds()).add(jxb);
//		}
//		
//
//		boolean flag=false;
//		for (String batch : batchSubjectMap.keySet()) {
//			Map<String, Set<String>> subjectStuMap = batchSubjectMap.get(batch);
//			Map<String, Set<NewGkDivideClass>> subjectClassMap = batchSubjectClassMap.get(batch);
//			if(subjectClassMap == null) {
//				subjectClassMap = new HashMap<>();
//			}
//			
//			for (String subjectId : subjectStuMap.keySet()) {
//				Set<String> studentIds = subjectStuMap.get(subjectId);
//				Set<NewGkDivideClass> classSet = subjectClassMap.get(subjectId);
//				int classStuNum = 0;
//				if(classSet != null) {
//					classStuNum = (int)classSet.stream().flatMap(e->e.getStudentList().stream()).count();
//					int pureCount = (int)classSet.stream().filter(e->pureClassMap.containsKey(e.getRelateId()))
//							.map(e->pureClassMap.get(e.getRelateId()))
//							.flatMap(e->e.getStudentList().stream()).count();
//					classStuNum = classStuNum - pureCount;
//				}
//				if(studentIds.size() - classStuNum !=0) {
//					if(!flag) {
//						flag=true;
//						break;
//					}
//				}
//			}
//			if(flag) {
//				break;
//			}
//		}
//		if(flag) {
//			return error("存在学生未安排，详情请查看走班学生统计");
//		}else {
//			return returnSuccess();
//		}
		return returnSuccess();
	}
	
	
	
	/**
	 * 手动调整页面 subjectIds 多个科目id以，隔开 groupClassId 某一个组合id
	 */
	@RequestMapping("/divideGroup/jzbGroupDetail/page")
	@ControllerInfo(value = "手动维护选考班")
	public String showJzbGroupDetailIndex(@PathVariable String divideId,
			String subjectId, String groupClassId,ModelMap map) {
		// 头部详情
		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			return errorFtl(map, "分班方案不存在");
		}
		//科目列表
		List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService
				.findByDivideIdAndSubjectTypeIn(divideId, new String[] {
						NewGkElectiveConstant.SUBJECT_TYPE_A});
		Set<String> subIds=EntityUtils.getSet(openSubjectList, NewGkOpenSubject::getSubjectId);
		
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[] {})),
				new TR<List<Course>>() {
				});
		map.put("courseList", courseList);
		//科目对应的班级
//		if(StringUtils.isBlank(subjectId)) {
//			subjectId=courseList.get(0).getId();
//			return errorFtl(map, "科目不存在");
//		}
//		Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
//		if(course==null) {
//			return errorFtl(map, "科目不存在");
//		}
//		map.put("course", course);
		
//		map.put("clazzList", clazzList);
		map.put("subjectId", subjectId);
		map.put("groupClassId", groupClassId);
		return "/newgkelective/divideGroup/schedulingAIndex.ftl";

	}
	
	@ResponseBody
	@RequestMapping("/divideGroup/findClassBysubject")
	@ControllerInfo("查询科目下选考班级")
	public String findClassBysubject(@PathVariable String divideId,
			String subjectId) {
		JSONArray jsonArr = new JSONArray();
		JSONObject jsonObj = null;
		if (StringUtils.isBlank(subjectId)) {
			return jsonArr.toJSONString();
		}
		List<NewGkDivideClass> clazzList = newGkDivideClassService.findListByDivideId(divideId,NewGkElectiveConstant.CLASS_SOURCE_TYPE1, 
				NewGkElectiveConstant.CLASS_TYPE_2, subjectId,
				NewGkElectiveConstant.SUBJECT_TYPE_A);
		if (CollectionUtils.isNotEmpty(clazzList)) {
			for (NewGkDivideClass g : clazzList) {
				jsonObj = new JSONObject();
				jsonObj.put("id", g.getId());
				jsonObj.put("name", g.getClassName());
				jsonArr.add(jsonObj);
			}
		} else {
			return jsonArr.toJSONString();
		}
		return jsonArr.toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/divideGroup/groupClassSaveStuA")
	@ControllerInfo(value = "保存教学班班级学生")
	public String groupClassSaveStuA(@PathVariable String divideId,
			String groupClassId, String stuId, int type,ModelMap map) {
		try {
			NewGkDivide newDivide = newGkDivideService.findById(divideId);
			if (newDivide == null) {
				return errorFtl(map, "分班方案不存在");
			}
			if (StringUtils.isNotBlank(groupClassId)) {
				NewGkDivideClass divideClass = newGkDivideClassService
						.findById(newDivide.getUnitId(), groupClassId, true);
				if (divideClass == null
						|| (!divideClass.getDivideId().equals(divideId))) {
					return error("该班级不存在！");
				}
				//不进行判断学生是否可以进入 之后统一验证
				Set<String> stuIds = new HashSet<String>();
				if (StringUtils.isNotBlank(stuId)) {
					String[] arr = stuId.split(",");
					
					for (int i = 0; i < arr.length; i++) {
						if (StringUtils.isNotBlank(arr[i])) {
							stuIds.add(arr[i]);
						}
					}
				}
				//拿到原有的
				List<String> stuList = divideClass.getStudentList();
				if(CollectionUtils.isEmpty(stuList)) {
					stuList=new ArrayList<>();
				}
				if(type>0) {
					//新增
					stuList.addAll(stuIds);
				}else {
					//删除
					stuList.removeAll(stuIds);
				}
				List<NewGkClassStudent> insertStudentList = new ArrayList<NewGkClassStudent>();
				NewGkClassStudent gkGroupClassStu;
				for (String s : stuList) {
					gkGroupClassStu = initClassStudent(newDivide.getUnitId(), newDivide.getId(), groupClassId, s);
					insertStudentList.add(gkGroupClassStu);
				}
				List<NewGkDivideClass> updateClassList = new ArrayList<NewGkDivideClass>();
				updateClassList.add(divideClass);

				newGkDivideClassService.saveAllList(newDivide.getUnitId(), newDivide.getId(),
						new String[] { groupClassId }, updateClassList, insertStudentList, false);

			} else {
				return error("该班级不存在！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！");
		}
		return success("保存成功！");
	}
	
	
	@RequestMapping("/divideGroup/schedulingALeft/page")
	@ControllerInfo(value = "教学班手动排班调整-左边")
	public String loadSchedulingALeft(@PathVariable String divideId,
			String subjectId, ModelMap map) {
		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			return errorFtl(map, "分班方案不存在");
		}
		NewGkChoice choice = newGkChoiceService.findById(newDivide.getChoiceId());
		if (choice == null) {
			return errorFtl(map, "选课方案不存在");
		}
		String referScoreId = newDivide.getReferScoreId();
		if(StringUtils.isBlank(referScoreId)) {
			referScoreId=newGkReferScoreService.findDefaultIdByGradeId(choice.getUnitId(), choice.getGradeId());
		}

		List<StudentResultDto> stuDtoList=new ArrayList<>();
		if(StringUtils.isNotBlank(subjectId)) {
			List<String> stulist = newGkChoResultService.findByChoiceIdAndSubjectIdAndKindType(newDivide.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,newDivide.getChoiceId(),subjectId);
			Map<String,String> zhbStuClassMap = new HashMap<>();
			if(CollectionUtils.isNotEmpty(stulist)) {
				List<NewGkDivideClass> clazzList = newGkDivideClassService.findByDivideIdAndClassTypeWithMaster(newDivide.getUnitId(), divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_2}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1,false);

				for(NewGkDivideClass n:clazzList) {
					if(CollectionUtils.isNotEmpty(n.getStudentList())) {
						if(NewGkElectiveConstant.SUBJECT_TYPE_3.equals(n.getSubjectType())) {
							stulist.removeAll(n.getStudentList());
						}else if(NewGkElectiveConstant.SUBJECT_TYPE_2.equals(n.getSubjectType())
								&& n.getSubjectIds().indexOf(subjectId) >-1) {
							//2+x中2
							stulist.removeAll(n.getStudentList());
						}else if(NewGkElectiveConstant.CLASS_TYPE_2.equals(n.getClassType())
								&& NewGkElectiveConstant.SUBJECT_TYPE_A.equals(n.getSubjectType())
								&& n.getSubjectIds().equals(subjectId)) {
							//教学班
							stulist.removeAll(n.getStudentList());
						}
						
						if(NewGkElectiveConstant.CLASS_TYPE_0.equals(n.getClassType())) {
							n.getStudentList().forEach(e->zhbStuClassMap.put(e, n.getClassName()));
						}
					}
					
				}
			
			}
			//剩余
			if(CollectionUtils.isNotEmpty(stulist)) {
				List<Student> studentList = SUtils.dt(studentRemoteService.findPartStudByGradeId(choice.getUnitId(), null,null, stulist.toArray(new String[] {})), new TR<List<Student>>(){});
				Map<String, Student> stuMap = EntityUtils.getMap(studentList, Student::getId);
				Set<String> classSet = EntityUtils.getSet(studentList, Student::getClassId);
				List<Clazz> classList = SUtils.dt(classRemoteService.findListByIds(classSet.toArray(new String[] {})),
						new TR<List<Clazz>>() {});
				Map<String, Clazz> clazzMap = EntityUtils.getMap(classList, Clazz::getId);
				//初始化stulist
				StudentResultDto dto;
				for(String ss:stulist) {
					if(stuMap.containsKey(ss)) {
						dto=new StudentResultDto();
						dto.setStudentId(ss);
						dto.setStudentName(stuMap.get(ss).getStudentName());
						dto.setSex(stuMap.get(ss).getSex()==null?"":String.valueOf(stuMap.get(ss).getSex()));
						if(clazzMap.containsKey(stuMap.get(ss).getClassId())) {
							dto.setOldClassName(clazzMap.get(stuMap.get(ss).getClassId()).getClassName());
						}
						if(zhbStuClassMap.containsKey(ss)) {
							dto.setClassName(zhbStuClassMap.get(ss));
						}
						stuDtoList.add(dto);
					}
					
				}
				if (CollectionUtils.isNotEmpty(stuDtoList)) {
					if (StringUtils.isNotBlank(referScoreId)) {
						makeStudentSubjectScore(referScoreId, stuDtoList);
					}
				}
			}
		}
		
		map.put("divideId", divideId);
		map.put("subjectIds", subjectId);
		// stuDtoList 各科成绩
		if (CollectionUtils.isNotEmpty(stuDtoList)) {
			if (StringUtils.isNotBlank(referScoreId)) {
				makeStudentSubjectScore(referScoreId, stuDtoList);
			}
		}
	
		Set<String> subjectIdSet=new HashSet<>();
		subjectIdSet.add(subjectId);
		List<Course> courseList = findShowCourseList(subjectIdSet);
		map.put("courseList", courseList);// 显示科目成绩
		int maxCount = stuDtoList.size();
		Map<String, Float> avgMap = new HashMap<String, Float>();// 平均分(包括语数英)
		Map<String, Float> allScoreMap = new HashMap<String, Float>();// 各科总分
																		// 用于计算平均分
		int manCount = countStu(stuDtoList, courseList, avgMap, allScoreMap);
		int womanCount = maxCount - manCount;
		map.put("maxCount", maxCount);
		map.put("avgMap", avgMap);
		map.put("manCount", manCount);
		map.put("womanCount", womanCount);
		map.put("allScoreMap", allScoreMap);
		map.put("stuDtoList", stuDtoList);
		map.put("rightOrLeft", "left");

		return "/newgkelective/divideGroup/schedulingListA.ftl";
	}
	@RequestMapping("/divideGroup/schedulingARight/page")
	@ControllerInfo(value = "教学班手动排班调整-右边")
	public String schedulingARight(@PathVariable String divideId,
			String subjectId,String groupClassId, ModelMap map) {
		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			return errorFtl(map, "分班方案不存在");
		}
		NewGkChoice choice = newGkChoiceService.findById(newDivide.getChoiceId());
		if (choice == null) {
			return errorFtl(map, "选课方案不存在");
		}
		String referScoreId = newDivide.getReferScoreId();
		if(StringUtils.isBlank(referScoreId)) {
			referScoreId=newGkReferScoreService.findDefaultIdByGradeId(choice.getUnitId(), choice.getGradeId());
		}
		NewGkDivideClass clazz = newGkDivideClassService.findByIdWithMaster(newDivide.getUnitId(), groupClassId, true);
		if(clazz==null || !clazz.getSubjectIds().equals(subjectId)) {
			return errorFtl(map, "班级不存在");
		}
		List<String> stulist=clazz.getStudentList();
		List<StudentResultDto> stuDtoList=new ArrayList<>();
		
		//剩余
		if(CollectionUtils.isNotEmpty(stulist)) {
			List<NewGkDivideClass> zhbClazzList = newGkDivideClassService.findByDivideIdAndClassTypeWithMaster(newDivide.getUnitId(), divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_0}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1,false);

			Map<String, String> zhbStuClassMap = new HashMap<>();
			for (NewGkDivideClass n : zhbClazzList) {
				if(NewGkElectiveConstant.CLASS_TYPE_0.equals(n.getClassType())) {
					n.getStudentList().forEach(e->zhbStuClassMap.put(e, n.getClassName()));
				}
				
			}
			
			List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(stulist.toArray(new String[] {})),
					new TR<List<Student>>() {});
			
			Map<String, Student> stuMap = EntityUtils.getMap(studentList, Student::getId);
			Set<String> classSet = EntityUtils.getSet(studentList, Student::getClassId);
			List<Clazz> classList = SUtils.dt(classRemoteService.findListByIds(classSet.toArray(new String[] {})),
					new TR<List<Clazz>>() {});
			Map<String, Clazz> clazzMap = EntityUtils.getMap(classList, Clazz::getId);
			//初始化stulist
			StudentResultDto dto;
			for(String ss:stulist) {
				if(stuMap.containsKey(ss)) {
					dto=new StudentResultDto();
					dto.setStudentId(ss);
					dto.setSex(stuMap.get(ss).getSex()==null?"":String.valueOf(stuMap.get(ss).getSex()));
					dto.setStudentName(stuMap.get(ss).getStudentName());
					if(clazzMap.containsKey(stuMap.get(ss).getClassId())) {
						dto.setOldClassName(clazzMap.get(stuMap.get(ss).getClassId()).getClassName());
					}
					if(zhbStuClassMap.containsKey(ss)) {
						dto.setClassName(zhbStuClassMap.get(ss));
					}
					stuDtoList.add(dto);
				}
				
			}
			if (CollectionUtils.isNotEmpty(stuDtoList)) {
				if (StringUtils.isNotBlank(referScoreId)) {
					makeStudentSubjectScore(referScoreId, stuDtoList);
				}
			}
		}
		map.put("divideId", divideId);
		map.put("subjectIds", clazz.getSubjectIds());
		// stuDtoList 各科成绩
		if (CollectionUtils.isNotEmpty(stuDtoList)) {
			if (StringUtils.isNotBlank(referScoreId)) {
				makeStudentSubjectScore(referScoreId, stuDtoList);
			}
		}
	
		Set<String> subjectIdSet=new HashSet<>();
		subjectIdSet.add(subjectId);
		List<Course> courseList = findShowCourseList(subjectIdSet);
		map.put("courseList", courseList);// 显示科目成绩
		int maxCount = stuDtoList.size();
		Map<String, Float> avgMap = new HashMap<String, Float>();// 平均分(包括语数英)
		Map<String, Float> allScoreMap = new HashMap<String, Float>();// 各科总分
																		// 用于计算平均分
		int manCount = countStu(stuDtoList, courseList, avgMap, allScoreMap);
		int womanCount = maxCount - manCount;
		map.put("maxCount", maxCount);
		map.put("avgMap", avgMap);
		map.put("manCount", manCount);
		map.put("womanCount", womanCount);
		map.put("allScoreMap", allScoreMap);
		map.put("stuDtoList", stuDtoList);
		map.put("rightOrLeft", "right");

		return "/newgkelective/divideGroup/schedulingListA.ftl";
	}
	
	@RequestMapping("/divideGroup/schedulingAEdit/page")
	@ControllerInfo(value = "手动排班调整-新增教学班班级")
	public String schedulingAEdit(@PathVariable String divideId,
			String subjectId,String stuIdStr, ModelMap map) {

		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			return errorFtl(map, "分班方案不存在");
		}
		map.put("divideId", divideId);
		Course course = SUtils.dc(courseRemoteService.findOneById(subjectId),Course.class);
		if(course==null) {
			return errorFtl(map, "科目不存在");
		}
	
		List<NewGkDivideClass> groupClassList = newGkDivideClassService
				.findClassBySubjectIdsWithMaster(newDivide.getUnitId(),
						divideId,
						NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_2, subjectId, false);
		
		List<NewGkDivideClass> classInsubjectList = groupClassList.stream().filter(e->e.getSubjectType().equals(NewGkElectiveConstant.SUBJECT_TYPE_A)).collect(Collectors.toList());
		String groupName = course.getSubjectName();

		NewGkDivideClass newGkDivideClass = new NewGkDivideClass();
		newGkDivideClass.setSubjectIds(subjectId);
		int k = 1;
		if (CollectionUtils.isNotEmpty(classInsubjectList)) {
			List<String> groupNameList = EntityUtils.getList(classInsubjectList,
					NewGkDivideClass::getClassName);
			while (true) {
				if (!groupNameList.contains(groupName + NewGkElectiveConstant.SUBJECT_TYPE_A+k + "班")) {
					break;
				}
				k++;
			}
		}
		newGkDivideClass.setClassName(groupName +NewGkElectiveConstant.SUBJECT_TYPE_A+ k + "班");
		map.put("newGkDivideClass", newGkDivideClass);
		map.put("subjectIds", subjectId);
		map.put("divideId", divideId);
		map.put("stuIdStr", stuIdStr);
		return "/newgkelective/divideGroup/schedulingAEdit.ftl";
	}
	
	
	@ResponseBody
	@RequestMapping("/divideGroup/autoOpenClassA")
	@ControllerInfo(value = "根据开班数自动分A")
	public String autoOpenClassA(@PathVariable String divideId,
			String subjectId,int openNum, String[] stuids, ModelMap map) {
		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			return error("分班方案不存在");
		}
		if (StringUtils.isBlank(subjectId)) {
			return error("没有选中科目");
		}
		if (openNum <= 0) {
			return error("开设班级数应为正整数");
		}
		Course course = SUtils.dc(courseRemoteService.findOneById(subjectId),Course.class);
		if(course==null) {
			return errorFtl(map, "科目不存在");
		}
		//查询需要安排的A学生
		List<String> stulist = newGkChoResultService.findByChoiceIdAndSubjectIdAndKindType(newDivide.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,newDivide.getChoiceId(),subjectId);
		if(CollectionUtils.isNotEmpty(stulist)) {
			List<NewGkDivideClass> clazzList = newGkDivideClassService.findByDivideIdAndClassType(newDivide.getUnitId(), divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_2}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			for(NewGkDivideClass n:clazzList) {
				if(CollectionUtils.isNotEmpty(n.getStudentList())) {
					if(NewGkElectiveConstant.SUBJECT_TYPE_3.equals(n.getSubjectType())) {
						stulist.removeAll(n.getStudentList());
					}else if(NewGkElectiveConstant.SUBJECT_TYPE_2.equals(n.getSubjectType())
							&& n.getSubjectIds().indexOf(subjectId) >-1) {
						//2+x中2
						stulist.removeAll(n.getStudentList());
					}else if(NewGkElectiveConstant.CLASS_TYPE_2.equals(n.getClassType())
							&& NewGkElectiveConstant.SUBJECT_TYPE_A.equals(n.getSubjectType())
							&& n.getSubjectIds().equals(subjectId)) {
						//教学班
						stulist.removeAll(n.getStudentList());
					}
				}
				
			}
		
		}
		//剩余
		if(CollectionUtils.isEmpty(stulist)) {
			return success("没有学生需要分班");
		}
		List<String> stulist1=new ArrayList<>();
		//stuids在stulist
		if(ArrayUtils.isNotEmpty(stuids)) {
			for(String s:stuids) {
				if(stulist.contains(s)) {
					stulist1.add(s);
				}
			}
			if(CollectionUtils.isEmpty(stulist1)) {
				return success("没有学生需要分班");
			}
		}else {
			stulist1=stulist;
		}
		List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(stulist1.toArray(new String[] {})), new TR<List<Student>>() {});
		if(CollectionUtils.isEmpty(studentList)) {
			return success("学生数据有误，找不到对应学生信息");
		}
		Map<String,Student> studentMap=EntityUtils.getMap(studentList, e->e.getId());
		//最后 平均分成stulist--openNum班
		List<StudentResultDto> stuDtoList=new ArrayList<>();
		StudentResultDto studentResultDto;
		String refScoreId=newDivide.getReferScoreId();
		NewGkChoice gkChoice = newGkChoiceService.findById(newDivide.getChoiceId());
		if(StringUtils.isBlank(refScoreId) && gkChoice!=null){
			refScoreId=newGkReferScoreService.findDefaultIdByGradeId(gkChoice.getUnitId(), gkChoice.getGradeId());
		}
		Map<String,Float> stuScoreMap=new HashMap<>();
		if(StringUtils.isNotBlank(refScoreId)) {
			List<NewGkScoreResult> subjectScoreList = newGkScoreResultService.findByReferScoreIdAndSubjectId(gkChoice.getUnitId(),refScoreId, subjectId);
			if(CollectionUtils.isNotEmpty(subjectScoreList)) {
				for(NewGkScoreResult r:subjectScoreList) {
					if(stulist1.contains(r.getStudentId())) {
						stuScoreMap.put(r.getStudentId(), r.getScore()==null?0.0f:r.getScore());
					}
				}
			}
		}
		for(String sss:stulist1) {
			if(studentMap.containsKey(sss)) {
				studentResultDto=new StudentResultDto();
				studentResultDto.setStudentId(studentMap.get(sss).getId());
				studentResultDto.setStudentName(studentMap.get(sss).getStudentName());
				studentResultDto.setSex(studentMap.get(sss).getSex()==null?"":String.valueOf(studentMap.get(sss).getSex()));
				//成绩
				if(stuScoreMap.containsKey(studentMap.get(sss).getId())) {
					studentResultDto.setScore(stuScoreMap.get(studentMap.get(sss).getId()));
				}else {
					studentResultDto.setScore(0.0f);
				}
				stuDtoList.add(studentResultDto);
			}
		}
		List<String>[] array =autoStuIdToXzbId(null,stuDtoList, openNum);

		//名称
		List<NewGkDivideClass> groupClassList = newGkDivideClassService
				.findClassBySubjectIds(newDivide.getUnitId(),
						divideId,
						NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_2, subjectId, false);
		List<NewGkDivideClass> classInsubjectList = groupClassList.stream().filter(e->e.getSubjectType().equals(NewGkElectiveConstant.SUBJECT_TYPE_A)).collect(Collectors.toList());
		String groupName = course.getSubjectName();
		List<String> groupNameList = EntityUtils.getList(classInsubjectList,
				NewGkDivideClass::getClassName);
		
		int k=1;
		List<NewGkDivideClass> insertClassList=new ArrayList<>();
		List<NewGkClassStudent> insertStudentList=new ArrayList<>();
		NewGkDivideClass newGkDivideClass;
		NewGkClassStudent newGkClassStudent;
		
		for(List<String> item:array) {
			//开设班级
			newGkDivideClass = initNewGkDivideClass(divideId,
					subjectId,NewGkElectiveConstant.CLASS_TYPE_2);
			newGkDivideClass.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
			newGkDivideClass.setBatch("1");
			//
			while (true) {
				if (!groupNameList.contains(groupName +NewGkElectiveConstant.SUBJECT_TYPE_A+ k + "班")) {
					newGkDivideClass.setClassName(groupName +NewGkElectiveConstant.SUBJECT_TYPE_A+ k + "班");
					break;
				}
				k++;
			}
			insertClassList.add(newGkDivideClass);
			for (String stuId : item) {
				newGkClassStudent = initClassStudent(newDivide.getUnitId(), newDivide.getId(), newGkDivideClass.getId(), stuId);
				insertStudentList.add(newGkClassStudent);
			}
			groupNameList.add(newGkDivideClass.getClassName());
		}
		try {
			newGkDivideClassService.saveAllList(null, null,
					null, insertClassList, insertStudentList, false);

		} catch (Exception e) {
			e.printStackTrace();
			return error("操作失败！");
		}
		return success("操作成功！");
	}
	
	private Map<String,Map<String,Float>> makeStuSubScoreMap(Set<String> yswIds,Set<String> chooseIds,List<NewGkScoreResult> scoreList,Map<String, List<String>> stuSubjectMap){
		String yswSubIds=NewGkElectiveConstant.YSY_SUBID;//语数外
		String zcjSubIds=NewGkElectiveConstant.ZCJ_SUBID;//语数外+选考组合
		Map<String,Map<String,Float>> stuSubScoreMap=new HashMap<>();
		//学生各科成绩  
		//同组合排名
		for(NewGkScoreResult rr:scoreList) {
			if(!stuSubjectMap.containsKey(rr.getStudentId())) {
				continue;
			}
			if(!yswIds.contains(rr.getSubjectId()) && !chooseIds.contains(rr.getSubjectId())) {
				continue;
			}
			if(!stuSubScoreMap.containsKey(rr.getStudentId())) {
				stuSubScoreMap.put(rr.getStudentId(),new HashMap<>());
				stuSubScoreMap.get(rr.getStudentId()).put(yswSubIds, 0.0f);
				stuSubScoreMap.get(rr.getStudentId()).put(zcjSubIds, 0.0f);
			}
			Map<String, Float> map1 = stuSubScoreMap.get(rr.getStudentId());
			map1.put(rr.getSubjectId(), rr.getScore()==null?0.0f:rr.getScore());
			if(stuSubjectMap.get(rr.getStudentId()).contains(rr.getSubjectId())) {
				map1.put(zcjSubIds, map1.get(zcjSubIds)+map1.get(rr.getSubjectId()));
			}else if(yswIds.contains(rr.getSubjectId())) {
				map1.put(yswSubIds, map1.get(yswSubIds)+map1.get(rr.getSubjectId()));
				map1.put(zcjSubIds, map1.get(zcjSubIds)+map1.get(rr.getSubjectId()));
			}
		}
		//保留两位小数
		for(Entry<String, Map<String, Float>> item:stuSubScoreMap.entrySet()) {
			Map<String, Float> map1 = item.getValue();
			for(Entry<String, Float> item2:map1.entrySet()) {
				map1.put(item2.getKey(), makeTwo(item2.getValue()));
			}
		}
		return stuSubScoreMap;
	}
	
	@RequestMapping("/exportAllClaStu")
	@ResponseBody
	@ControllerInfo("所有新行政班学生名单Excel")
    public String exportNewClass(@PathVariable String divideId,String type,HttpServletResponse resp){
		NewGkDivide divide = newGkDivideService.findById(divideId);
		
		List<Course> chooseCourseList=newGkChoRelationService.findChooseSubject(divide.getChoiceId(), divide.getUnitId());
		
		boolean isShowScore=false;
		List<NewGkDivideClass> divideClassList = newGkDivideClassService.findByDivideIdAndClassType(this.getLoginInfo().getUnitId(), 
				divideId,
				new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_4}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		if(StringUtils.isBlank(type)) {
			type="X";
		}
		List<NewGkDivideClass> xzbClazzList=new ArrayList<>();
		Map<String,NewGkDivideClass> relClassMap=new HashMap<>();
		if("J".equals(type)) {
			xzbClazzList = divideClassList.parallelStream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_4)).collect(Collectors.toList());
		}else {
			xzbClazzList = divideClassList.parallelStream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_1)).collect(Collectors.toList());
			//关联的组合班级id
			List<NewGkDivideClass> class0List = newGkDivideClassService.findByDivideIdAndClassType(this.getLoginInfo().getUnitId(), 
					divideId,new String[] {NewGkElectiveConstant.CLASS_TYPE_0}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			relClassMap=EntityUtils.getMap(class0List, e->e.getId());
		}
		
		Set<String> studentIds = new HashSet<String>();
		for (NewGkDivideClass d : xzbClazzList) {
			if(CollectionUtils.isNotEmpty(d.getStudentList())){
				studentIds.addAll(d.getStudentList());
			}
		}
		List<Student> studentList = SUtils.dt(studentRemoteService.findPartStudentById(studentIds.toArray(new String[0])), Student.class);
		String mcodeId = ColumnInfoUtils.getColumnInfo(Student.class, "sex").getMcodeId();
		Map<String, McodeDetail> codeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId(mcodeId), 
				new TypeReference<Map<String, McodeDetail>>() {});
		
		Set<String> classIds = EntityUtils.getSet(studentList,Student::getClassId);
		List<Clazz> classList = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[] {})), Clazz.class);
		Map<String, Clazz> classMap = EntityUtils.getMap(classList, Clazz::getId);
		
		
		List<NewGkChoResult> choResultList = newGkChoResultService.findByKindTypeAndChoiceIdAndStudentIds(divide.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,divide.getChoiceId(), studentIds.toArray(new String[] {}));
		Map<String, List<String>> stuSubjectMap = new HashMap<>();
		for (NewGkChoResult choose : choResultList) {
			if(!stuSubjectMap.containsKey(choose.getStudentId())) {
				stuSubjectMap.put(choose.getStudentId(), new ArrayList<>());
			}
			stuSubjectMap.get(choose.getStudentId()).add(choose.getSubjectId());
		}
		List<Course> ysyList=new ArrayList<>();
		List<NewGkScoreResult> scoreList=new ArrayList<>();
		//07行政班 不显示成绩 以及3+1+2组合模式的教学班
		Map<String,Map<String,Float>> stuSubScoreMap=new HashMap<>();
		if(!NewGkElectiveConstant.DIVIDE_TYPE_07.equals(divide.getOpenType())) {
			isShowScore=true;
			//语数英
			ysyList = SUtils.dt(courseRemoteService.findByCodesYSY(divide.getUnitId()), new TR<List<Course>>() {});
			//成绩
			String refScoreId=divide.getReferScoreId();
			if(StringUtils.isBlank(refScoreId)) {
				refScoreId=newGkReferScoreService.findDefaultIdByGradeId(divide.getGradeId());
			}
			if(StringUtils.isNotBlank(refScoreId)) {
				scoreList = newGkScoreResultService.findListByReferScoreId(divide.getUnitId(), refScoreId);
			}
			if(CollectionUtils.isNotEmpty(scoreList)) {
				Set<String> yswIds=EntityUtils.getSet(ysyList, e->e.getId());
				Set<String> chooseIds=EntityUtils.getSet(chooseCourseList, e->e.getId());
				stuSubScoreMap=makeStuSubScoreMap(yswIds, chooseIds, scoreList, stuSubjectMap);
			}
		}
		
		Map<String, Course> courseMap = EntityUtils.getMap(chooseCourseList, e->e.getId());
		
		Map<String, List<String>> stuAJxbMap = new HashMap<>();
		Map<String, List<String>> stuBJxbMap = new HashMap<>();
		
		Map<String, List<String>> stuOtherMap = new HashMap<>();
		
		Map<String, String> stuXzbMap = new HashMap<>();
		for (NewGkDivideClass divideClass2 : divideClassList) {
			if(NewGkElectiveConstant.CLASS_TYPE_1.equals(divideClass2.getClassType())) {
				// 行政班
				for (String stuId : divideClass2.getStudentList()) {
					stuXzbMap.put(stuId, divideClass2.getClassName());
				}
			}else if(NewGkElectiveConstant.CLASS_TYPE_2.equals(divideClass2.getClassType())){
				for (String stuId : divideClass2.getStudentList()) {
					if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(divideClass2.getSubjectType())) {
						if(!stuAJxbMap.containsKey(stuId)) {
							stuAJxbMap.put(stuId, new ArrayList<>());
						}
						stuAJxbMap.get(stuId).add(divideClass2.getClassName());
					}else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(divideClass2.getSubjectType())) {
						if(!stuBJxbMap.containsKey(stuId)) {
							stuBJxbMap.put(stuId, new ArrayList<>());
						}
						stuBJxbMap.get(stuId).add(divideClass2.getClassName());
					}
				}
			}else if(NewGkElectiveConstant.CLASS_TYPE_4.equals(divideClass2.getClassType())) {
				for (String stuId : divideClass2.getStudentList()) {
					if(!stuOtherMap.containsKey(stuId)) {
						stuOtherMap.put(stuId, new ArrayList<>());
					}
					stuOtherMap.get(stuId).add(divideClass2.getClassName());
				}
			}
			
		}
		
		// 结果key1:studentId,key2:标题
		Map<String,Map<String,String>> studentMap = new HashMap<String, Map<String,String>>();
		Map<String,String> resultMap;
		String aJxbName;
		String bJxbName;
		String otherName;
		Map<String,Student> stuMap=new HashMap<>();
		//key:subjectId,key:stuId,value:语数外+选考组合成绩
		Map<String,Map<String,Float>> subjectScoreMap=new HashMap<>();
		
		//key:studentId,key:subjectId,value:value
		//subjectId:语数英+三科成绩
		Map<String,Map<String,Float>> studentSubScoreMap=new HashMap<>();
		//排序
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
		for (Student student : studentList) {
			stuMap.put(student.getId(), student);
			//单科分层可能存在学生没有选课
			String chooseSubjects="";
			List<String> subIds = stuSubjectMap.get(student.getId());
			
			if(CollectionUtils.isNotEmpty(subIds)) {
				for(String s:subIds) {
					if(courseMap.containsKey(s)) {
						chooseSubjects=chooseSubjects+"、"+courseMap.get(s).getSubjectName();
					}
				}
			}
			if(StringUtils.isNotBlank(chooseSubjects)) {
				chooseSubjects=chooseSubjects.substring(1);
			}
		
			resultMap = new HashMap<String, String>();
			
			resultMap.put("姓名", student.getStudentName());
			resultMap.put("学号", student.getStudentCode());
			resultMap.put("性别", codeMap.get(student.getSex()+"").getMcodeContent());
			resultMap.put("原行政班", classMap.get(student.getClassId())==null?"":classMap.get(student.getClassId()).getClassName());
			resultMap.put("新行政班", stuXzbMap.get(student.getId()));
			resultMap.put("已选学科", chooseSubjects);
			aJxbName = "";
			if(CollectionUtils.isNotEmpty(stuAJxbMap.get(student.getId()))){
				for (String className : stuAJxbMap.get(student.getId())) {
					aJxbName+="、"+className;
				}
				aJxbName = aJxbName.substring(1);
			}
			resultMap.put("选考班", aJxbName);
			bJxbName = "";
			if(CollectionUtils.isNotEmpty(stuBJxbMap.get(student.getId()))){
				for (String className : stuBJxbMap.get(student.getId())) {
					bJxbName+="、"+className;
				}
				bJxbName = bJxbName.substring(1);
			}
			resultMap.put("学考班", bJxbName);
			otherName="";
			if(CollectionUtils.isNotEmpty(stuOtherMap.get(student.getId()))){
				for (String className : stuOtherMap.get(student.getId())) {
					otherName+="、"+className;
				}
				otherName = otherName.substring(1);
			}
			resultMap.put("二科组合", otherName);
			if(isShowScore) {
				studentSubScoreMap.put(student.getId(), new HashMap<>());
				Map<String, Float> scoreMap = stuSubScoreMap.get(student.getId())==null?new HashMap<>():stuSubScoreMap.get(student.getId());
				for(Course c:ysyList) {
					if(scoreMap.containsKey(c.getId())) {
						resultMap.put(c.getSubjectName(), String.valueOf(scoreMap.get(c.getId())));
						studentSubScoreMap.get(student.getId()).put(c.getId(), scoreMap.get(c.getId()));
					}else {
						resultMap.put(c.getSubjectName(), "/");
						studentSubScoreMap.get(student.getId()).put(c.getId(), 0.0f);
					}
				}
				if(scoreMap.containsKey(NewGkElectiveConstant.YSY_SUBID)) {
					resultMap.put("语数外", String.valueOf(scoreMap.get(NewGkElectiveConstant.YSY_SUBID)));
				}else {
					resultMap.put("语数外", "/");
				}
				Float f=0.0f;
				for(Course c:chooseCourseList) {
					if(subIds.contains(c.getId())) {
						studentSubScoreMap.get(student.getId()).put(c.getId(), 0.0f);
						if(scoreMap.containsKey(c.getId())) {
							f=f+scoreMap.get(c.getId());
							resultMap.put(c.getSubjectName(), String.valueOf(scoreMap.get(c.getId())));
							studentSubScoreMap.get(student.getId()).put(c.getId(),scoreMap.get(c.getId()));
						}else {
							resultMap.put(c.getSubjectName(), "/");
						}
					}else {
						resultMap.put(c.getSubjectName(), "/");
					}
				}
				//没有选课结果--语数英成绩还是有的其他则无
				float yswxkzhScore=0.0f;
				if(CollectionUtils.isNotEmpty(subIds)) {
					resultMap.put("选考组合", String.valueOf(f));
					if(scoreMap.containsKey(NewGkElectiveConstant.ZCJ_SUBID)) {
						yswxkzhScore=scoreMap.get(NewGkElectiveConstant.ZCJ_SUBID);
						resultMap.put("语数外+选考组合", String.valueOf(scoreMap.get(NewGkElectiveConstant.ZCJ_SUBID)));
					}else {
						resultMap.put("语数外+选考组合", "/");
					}
					resultMap.put("同组合排名", "/");
				}else {
					resultMap.put("选考组合", "/");
					resultMap.put("语数外+选考组合", "/");
					resultMap.put("同组合排名", "/");
				}
				
				if(CollectionUtils.isNotEmpty(subIds)) {
					String chooseKey=keyListSort(subIds);
					if(!subjectScoreMap.containsKey(chooseKey)) {
						subjectScoreMap.put(chooseKey, new HashMap<>());
					}
					subjectScoreMap.get(chooseKey).put(student.getId(), yswxkzhScore);
				}
				
			}
			
			
			studentMap.put(student.getId(), resultMap);
		}
		if(subjectScoreMap.size()>0) {
			//计算同组合排名 学生排名
			Map<String,Integer> stuRank=makeGroupRank(subjectScoreMap);
			//放入结果中
			for(Entry<String, Map<String, String>> item:studentMap.entrySet()) {
				if(stuRank.containsKey(item.getKey())) {
					item.getValue().put("同组合排名", String.valueOf(stuRank.get(item.getKey())));
				}
			}
		}
		
		
		List<String> titleArrList=new ArrayList<>();
		//共通点
		titleArrList.add("姓名");
		titleArrList.add("学号");
		titleArrList.add("性别");
		List<String> titleArr2List=new ArrayList<>();
		if(NewGkElectiveConstant.DIVIDE_TYPE_05.equals(divide.getOpenType())) {
			titleArrList.add("原行政班");
			titleArrList.add("已选学科");
			titleArrList.add("选考班");
			titleArrList.add("学考班");
			//titleArr = new String[]{"姓名","学号","性别","原行政班","已选学科","选考班","学考班"};
		}else if(NewGkElectiveConstant.DIVIDE_TYPE_07.equals(divide.getOpenType())) {
			titleArrList.add("原行政班");
			titleArrList.add("选考班");
			//titleArr = new String[]{"姓名","学号","性别","原行政班","选考班"};
			titleArr2List.add("姓名");
			titleArr2List.add("学号");
			titleArr2List.add("性别");
			titleArr2List.add("行政班");
			titleArr2List.add("走班班级");
			//titleArr2 = new String[]{"姓名","学号","性别","行政班","走班班级"};
		}else if(NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType()) || NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divide.getOpenType())){
			if("J".equals(type)) {
				titleArrList.add("原行政班");
				titleArrList.add("新行政班");
				titleArrList.add("已选学科");
				//titleArr = new String[]{"姓名","学号","性别","原行政班","新行政班","已选学科"};
			}else {
				titleArrList.add("原行政班");
				titleArrList.add("已选学科");
				titleArrList.add("二科组合");
				//titleArr = new String[]{"姓名","学号","性别","原行政班","已选学科","二科组合"};
			}
			
		}else {
			titleArrList.add("原行政班");
			titleArrList.add("新行政班");
			titleArrList.add("已选学科");
			titleArrList.add("选考班");
			titleArrList.add("学考班");
			//titleArr = new String[]{"姓名","学号","性别","原行政班","新行政班","已选学科","选考班","学考班"};
		}
		if(isShowScore) {
			for(Course c:ysyList) {
				titleArrList.add(c.getSubjectName());
			}
			titleArrList.add("语数外");
			for(Course c:chooseCourseList) {
				titleArrList.add(c.getSubjectName());
			}
			titleArrList.add("选考组合");
			titleArrList.add("语数外+选考组合");
			titleArrList.add("同组合排名");
		}
		String[] titleArr;
		String[] titleArr2 = null;
		titleArr=titleArrList.toArray(new String[0]);
		if(CollectionUtils.isNotEmpty(titleArr2List)) {
			titleArr2=titleArr2List.toArray(new String[0]);
		}
        HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = null;
		HSSFRow titleRow = null;
		HSSFCell titleCell = null;
		HSSFCellStyle centerStyle=workbook.createCellStyle();
		centerStyle.setAlignment(HorizontalAlignment.CENTER);//水平
		centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
		if(isShowScore) {
			CellRangeAddress inCar1=null;
			//先创建sheet
			int j=0;
			sheet = workbook.createSheet("统计情况");
			String[] title1=null;
			if("J".equals(type)) {
				title1=new String[] {"组合班","总人数","男","女","组合","组合人数"};
			}else {
				title1=new String[] {"行政班","所属组合班","总人数","男","女","组合","组合人数"};
			}
			titleRow = sheet.createRow(j);
			for(int i=0; i<title1.length; i++) {
				titleCell = titleRow.createCell(i);
				titleCell.setCellValue(new HSSFRichTextString(title1[i]));
				inCar1 = new CellRangeAddress(j,j+1,i,i);
				sheet.addMergedRegion(inCar1);
			}
			List<String> scoreSubList=new ArrayList<>();
			for(Course c:ysyList) {
				scoreSubList.add(c.getSubjectName());
			}
			scoreSubList.add("语数外");
			for(Course c:chooseCourseList) {
				scoreSubList.add(c.getSubjectName());
			}
			scoreSubList.add("选考组合");
			scoreSubList.add("语数外+选考组合");
			//平均分
			titleCell = titleRow.createCell(title1.length);
			titleCell.setCellValue(new HSSFRichTextString("平均分"));
			//居中
			titleCell.setCellStyle(centerStyle);
			inCar1 = new CellRangeAddress(j,j,title1.length,title1.length+scoreSubList.size()-1);
			sheet.addMergedRegion(inCar1);
			j++;
			titleRow = sheet.createRow(j);
			for(int k=0;k<scoreSubList.size();k++) {
				titleCell = titleRow.createCell(k+title1.length);
				titleCell.setCellValue(new HSSFRichTextString(scoreSubList.get(k)));
			}
			j++;
			//行政班	所属组合班	总人数	男	女	组合	组合人数	平均分
			//		语文	数学	外语	语数外	物理	化学	生物	政治	历史	地理	选考组合	语数外+选考组合;
			for(NewGkDivideClass divideClass : xzbClazzList){
				Map<String,String> rrMap=new HashMap<>();
				int allStuNum=0;
				int boyNum=0;
				int girlNum=0;
				Map<String,List<String>> groupStuMap=new HashMap<>();
				Set<String> allSubIds=new HashSet<>();
				List<String> stuIds=new ArrayList<>();
				if(CollectionUtils.isNotEmpty(divideClass.getStudentList())) {
					for(String s:divideClass.getStudentList()) {
						if(stuMap.containsKey(s)) {
							stuIds.add(s);
							allStuNum++;
							if(Objects.equals(1, stuMap.get(s).getSex())) {
								boyNum++;
							}
							if(Objects.equals(2, stuMap.get(s).getSex())) {
								girlNum++;
							}
							//选课结果
							List<String> subIds = stuSubjectMap.get(s);
							if(CollectionUtils.isNotEmpty(subIds)) {
								Collections.sort(subIds);
								String s2 = "";
								for (String s1 : subIds) {
									s2 = s2 + "," + s1;
								}
								s2 = s2.substring(1);
								if(!groupStuMap.containsKey(s2)) {
									groupStuMap.put(s2, new ArrayList<>());
								}
								groupStuMap.get(s2).add(s);
								allSubIds.addAll(subIds);
							}
						}
					}
				}
				int rows=1;
				if(groupStuMap.size()>1) {
					rows=groupStuMap.size();
				}
				
				rrMap.put("行政班", divideClass.getClassName());
				rrMap.put("组合班", divideClass.getClassName());
				if(StringUtils.isNotBlank(divideClass.getRelateId()) && relClassMap.containsKey(divideClass.getRelateId())) {
					rrMap.put("所属组合班", relClassMap.get(divideClass.getRelateId()).getClassName());
				}else {
					rrMap.put("所属组合班", "/");
				}
				rrMap.put("总人数", allStuNum+"");
				rrMap.put("男", boyNum+"");
				rrMap.put("女", girlNum+"");
				titleRow = sheet.createRow(j);
				for(int ii=0; ii<title1.length-2; ii++) {
					titleCell = titleRow.createCell(ii);
					titleCell.setCellValue(new HSSFRichTextString(rrMap.get(title1[ii])));
					if(rows>1) {
						inCar1 = new CellRangeAddress(j,j+rows-1,ii,ii);
						sheet.addMergedRegion(inCar1);
					}
				}
				
				Set<String> ysyIds = EntityUtils.getSet(ysyList, e->e.getId());
				//统计成绩
				Map<String,Float> avgScoreMap=makeAvg(stuIds,studentSubScoreMap,ysyIds);
				int kk=title1.length;
				//先进入成绩
				float ysyAvg=0.0f;
				for(Course c:ysyList) {
					float avgscore=avgScoreMap.get(c.getId())==null?0.0f:avgScoreMap.get(c.getId());
					titleCell = titleRow.createCell(kk);
					titleCell.setCellValue(new HSSFRichTextString(String.valueOf(avgscore)));
					if(rows>1) {
						inCar1 = new CellRangeAddress(j,j+rows-1,kk,kk);
						sheet.addMergedRegion(inCar1);
					}
					ysyAvg=ysyAvg+avgscore;
					kk++;
				}
				titleCell = titleRow.createCell(kk);
				titleCell.setCellValue(new HSSFRichTextString(String.valueOf(makeTwo(ysyAvg))));
				if(rows>1) {
					inCar1 = new CellRangeAddress(j,j+rows-1,kk,kk);
					sheet.addMergedRegion(inCar1);
				}
				kk++;
				for(Course c:chooseCourseList) {
					titleCell = titleRow.createCell(kk);
					if(allSubIds.contains(c.getId())) {
						float avgscore=avgScoreMap.get(c.getId())==null?0.0f:avgScoreMap.get(c.getId());
						titleCell.setCellValue(new HSSFRichTextString(String.valueOf(avgscore)));
					}else {
						titleCell.setCellValue(new HSSFRichTextString("/"));
					}
					if(rows>1) {
						inCar1 = new CellRangeAddress(j,j+rows-1,kk,kk);
						sheet.addMergedRegion(inCar1);
					}
					kk++;
				}
				float threeAvgscore=avgScoreMap.get("3")==null?0.0f:avgScoreMap.get("3");
				titleCell = titleRow.createCell(kk);
				titleCell.setCellValue(new HSSFRichTextString(String.valueOf(threeAvgscore)));
				if(rows>1) {
					inCar1 = new CellRangeAddress(j,j+rows-1,kk,kk);
					sheet.addMergedRegion(inCar1);
				}
				kk++;
				titleCell = titleRow.createCell(kk);
				titleCell.setCellValue(new HSSFRichTextString(String.valueOf(makeTwo(threeAvgscore+ysyAvg))));
				if(rows>1) {
					inCar1 = new CellRangeAddress(j,j+rows-1,kk,kk);
					sheet.addMergedRegion(inCar1);
				}
				int rr=0;
				for(Entry<String, List<String>> items:groupStuMap.entrySet()) {
					if(rr==0) {
						titleCell = titleRow.createCell(title1.length-2);
						titleCell.setCellValue(new HSSFRichTextString(nameSet(courseMap, items.getKey())));
						titleCell = titleRow.createCell(title1.length-1);
						titleCell.setCellValue(new HSSFRichTextString(items.getValue().size()+""));
					}else {
						j++;
						titleRow = sheet.createRow(j);
						titleCell = titleRow.createCell(title1.length-2);
						titleCell.setCellValue(new HSSFRichTextString(nameSet(courseMap, items.getKey())));
						titleCell = titleRow.createCell(title1.length-1);
						titleCell.setCellValue(new HSSFRichTextString(items.getValue().size()+""));
					}
					rr++;
				}
				j++;
			}
		}
		
		//每班为一个sheet
		int index;
		for(NewGkDivideClass divideClass : xzbClazzList){
			index=0;
			sheet = workbook.createSheet(divideClass.getClassName());
			//首行首列固定
			sheet.createFreezePane(1, 1);
			//sheet.setDefaultColumnWidth(15);
			//首行
			titleRow = sheet.createRow(index++);
			for (int i=0; i<titleArr.length; i++) {
				if(i<6){
					sheet.setColumnWidth(i, 18 * 256);//列宽
				}else{
					sheet.setColumnWidth(i, 30 * 256);//列宽
				}
				 titleCell = titleRow.createCell(i);
				 if(titleArr2 != null) {
					 titleCell.setCellValue(new HSSFRichTextString(titleArr2[i]));
				 }else{
					 titleCell.setCellValue(new HSSFRichTextString(titleArr[i]));
				 }
			}
			List<String> sList = divideClass.getStudentList();
			if(CollectionUtils.isNotEmpty(sList)){
				//根据学号排序
				List<Student> stuList = studentList.stream().filter(e->sList.contains(e.getId())).collect(Collectors.toList());
				for (Student s : stuList) {
					Map<String, String> inMap = studentMap.get(s.getId());
					HSSFRow contentRow = sheet.createRow(index++);
					for (int i=0; i<titleArr.length; i++) {
						 HSSFCell cell = contentRow.createCell(i);
						 cell.setCellValue(new HSSFRichTextString(inMap.get(titleArr[i])));
					}
				}
			}
		}
		
		ExportUtils.outputData(workbook, divide.getDivideName().replaceAll(" ", "")+"各班学生名单", resp);
		return returnSuccess();
    }
	
	/**
	 * 各科平均分+3科组合平均分（key:3）
	 * @param stuIds
	 * @param studentSubScoreMap
	 * @param ysyIds
	 * @return
	 */
	private Map<String, Float> makeAvg(List<String> stuIds, Map<String, Map<String, Float>> studentSubScoreMap,
			Set<String> ysyIds) {
		Map<String, Float> returnMap=new HashMap<>();
		String threeIds="3";
		if(CollectionUtils.isNotEmpty(stuIds)) {
			Map<String,Float> sumScoreMap=new HashMap<>();//3科组合key:3
			Map<String,Integer> subStuNumMap=new HashMap<>();
			for(String s:stuIds) {
				Map<String, Float> scoreMap = studentSubScoreMap.get(s);
				boolean f=false;
				if(scoreMap!=null) {
					for(Entry<String, Float> item:scoreMap.entrySet()) {
						if(!sumScoreMap.containsKey(item.getKey())) {
							sumScoreMap.put(item.getKey(), 0.0f);
							subStuNumMap.put(item.getKey(), 0);
						}
						sumScoreMap.put(item.getKey(), sumScoreMap.get(item.getKey())+item.getValue());
						subStuNumMap.put(item.getKey(), subStuNumMap.get(item.getKey())+1);
						if(!ysyIds.contains(item.getKey())) {
							if(!f) {
								f=true;
							}
							//3科成绩
							if(!sumScoreMap.containsKey(threeIds)) {
								sumScoreMap.put(threeIds,0.0f);
								subStuNumMap.put(threeIds, 0);
							}
							sumScoreMap.put(threeIds, sumScoreMap.get(threeIds)+item.getValue());
						}
					}
				}
				subStuNumMap.put(threeIds, subStuNumMap.get(threeIds)+1);
			}
			//统计平均分
			for(Entry<String, Float> item:sumScoreMap.entrySet()) {
				//保留两位小数
				returnMap.put(item.getKey(), makeTwo(item.getValue()/subStuNumMap.get(item.getKey())));
			}
		}
		return returnMap;
	}

	private Map<String, Integer> makeGroupRank(Map<String, Map<String, Float>> subjectScoreMap) {
		Map<String, Integer> returnMap=new HashMap<>();
		for(Entry<String, Map<String, Float>> item:subjectScoreMap.entrySet()) {
			List<Float> scoreList = item.getValue().values().stream().collect(Collectors.toList());
			Collections.sort(scoreList);
			Collections.reverse(scoreList);
			Map<Float,Integer> scoreRankMap=new HashMap<>();
			for(int i=0;i<scoreList.size();i++) {
				if(scoreRankMap.containsKey(scoreList.get(i))) {
					continue;
				}
				scoreRankMap.put(scoreList.get(i), i+1);
			}
			for(Entry<String, Float> item2:item.getValue().entrySet()) {
				returnMap.put(item2.getKey(), scoreRankMap.get(item2.getValue()));
			}
		}
		return returnMap;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/exportCount")
	@ResponseBody
	@ControllerInfo("教学班组成统计导出")
    public String exportCount(@PathVariable String divideId, String fromSolve ,String arrayId, ModelMap map, HttpServletResponse resp){
		//导出教学班统计信息
		NewGkDivide divide = newGkDivideService.findById(divideId);
		//获取成绩
		String refScoreId=divide.getReferScoreId();
		if(StringUtils.isBlank(refScoreId)) {
			refScoreId=newGkReferScoreService.findDefaultIdByGradeId(divide.getGradeId());
		}
		//key:subjectId,key:studentId
		Map<String, Map<String, Float>> stuScoreMap=new HashMap<>();
		//key:studentId,key:subjectId,
		Map<String, Map<String, Float>> stuSubjectScoreMap=new HashMap<>();
		if(StringUtils.isNotBlank(refScoreId)) {
			List<NewGkScoreResult> scoreList = newGkScoreResultService.findListByReferScoreId(divide.getUnitId(), refScoreId);
			if(CollectionUtils.isNotEmpty(scoreList)) {
				for(NewGkScoreResult r:scoreList) {
					if(!stuScoreMap.containsKey(r.getSubjectId())) {
						stuScoreMap.put(r.getSubjectId(), new HashMap<>());
					}
					stuScoreMap.get(r.getSubjectId()).put(r.getStudentId(), r.getScore()==null?0.0f:r.getScore());
					
					if(!stuSubjectScoreMap.containsKey(r.getStudentId())) {
						stuSubjectScoreMap.put(r.getStudentId(), new HashMap<>());
					}
					stuSubjectScoreMap.get(r.getStudentId()).put(r.getSubjectId(), r.getScore()==null?0.0f:r.getScore());
				}
			}
		}
		
		
		newGkDivideService.divideJxbResultCount(divide, fromSolve, arrayId, null, map);
		Map<String, Map<String, List<DivideResultDto>>> exportMap  = (Map<String, Map<String, List<DivideResultDto>>>) map.get("exportMap");
		LinkedHashMap<String,String> coumap = SUtils.dt(courseRemoteService.findPartCouByIds(exportMap.keySet().toArray(new String[0])),new TypeReference<LinkedHashMap<String, String>>(){});
        HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("教学班学生组成统计");
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
		//前两行固定
		sheet.createFreezePane(0, 2);
		//第一行
		HSSFRow titleRow1 = sheet.createRow(0);
		titleRow1.setHeightInPoints((2f) * sheet.getDefaultRowHeightInPoints());
		CellRangeAddress car1 = new CellRangeAddress(0,0,0,5);
        sheet.addMergedRegion(car1);
        HSSFCell row1Cell1 = titleRow1.createCell(0);
        row1Cell1.setCellValue("选考");
        CellRangeAddress car2 = new CellRangeAddress(0,0,7,11);
        sheet.addMergedRegion(car2);
		HSSFCell row1Cell2 = titleRow1.createCell(7);
		row1Cell2.setCellValue("学考");
		//第二行
		HSSFRow titleRow2 = sheet.createRow(1);
		titleRow2.setHeightInPoints((2f) * sheet.getDefaultRowHeightInPoints());
		String[] titleArr = new String[]{"科目","教学班","总人数","平均分","行政班","人数","","科目","教学班","总人数","行政班","人数"};
		for (int i = 0; i < titleArr.length; i++) {
			sheet.setColumnWidth(i, 12 * 256);//列宽
			HSSFCell row2Cell = titleRow2.createCell(i);
			row2Cell.setCellValue(titleArr[i]);
		}
		//填充内容
		int indexa=2;//选考科目起始行
		int indexb=2;//学考科目起始行
		HSSFRow row;
		Map<String,List<String>> stuIdBysubjectA=new HashMap<>();//某个科目下的学生信息--用于统计排名
		
		
		for(Entry<String, String> entry : coumap.entrySet()){
			List<DivideResultDto> aDtoList = exportMap.get(entry.getKey()).get("aDtoList");
			List<DivideResultDto> bDtoList = exportMap.get(entry.getKey()).get("bDtoList");
			int num = 0;//班级总数
			int ind = indexa;//教学班起始行
			for (int i = 0; i < aDtoList.size(); i++) {
				row= sheet.getRow(ind);
				if(row==null){
					row = sheet.createRow(ind);
				}
				row.setHeightInPoints((1.5f) * sheet.getDefaultRowHeightInPoints());
				if(i==0){
					HSSFCell cell0 = row.createCell(0);
					cell0.setCellValue(coumap.get(entry.getKey()));
				}
				DivideResultDto aDto = aDtoList.get(i);
				HSSFCell cell1= row.createCell(1);
				cell1.setCellValue(aDto.getClassName());
				HSSFCell cell2= row.createCell(2);
				cell2.setCellValue(aDto.getTotalNum());
				
				//拿到所有学生id
				List<String> tId = aDto.getStudentList();
				float avg=makeScore(tId,stuScoreMap.get(entry.getKey()));
				if(!stuIdBysubjectA.containsKey(entry.getKey())) {
					stuIdBysubjectA.put(entry.getKey(), new ArrayList<>());
				}
				stuIdBysubjectA.get(entry.getKey()).addAll(tId);
				
				HSSFCell cell33= row.createCell(3);
				cell33.setCellValue(String.valueOf(avg));
				List<NewGkDivideClass> alist = aDto.getNormalClassList();
				if(alist.size()>1){
					CellRangeAddress inCar1 = new CellRangeAddress(ind,ind+alist.size()-1,1,1);
					sheet.addMergedRegion(inCar1);
					CellRangeAddress inCar2 = new CellRangeAddress(ind,ind+alist.size()-1,2,2);
					sheet.addMergedRegion(inCar2);
					CellRangeAddress inCar33 = new CellRangeAddress(ind,ind+alist.size()-1,3,3);
					sheet.addMergedRegion(inCar33);
					for (int j = 0; j < alist.size(); j++) {
						NewGkDivideClass dc = alist.get(j);
						row = sheet.getRow(ind+j);
						if(row==null){
							row = sheet.createRow(ind+j);
						}
						row.setHeightInPoints((1.5f) * sheet.getDefaultRowHeightInPoints());
						HSSFCell cell3 = row.createCell(4);
						cell3.setCellValue(dc.getClassName());
						HSSFCell cell4 = row.createCell(5);
						cell4.setCellValue(dc.getClassNum());
					}
					num+=alist.size();
					ind+=alist.size();
				}else{
					NewGkDivideClass dc = alist.get(0);
					HSSFCell cell3= row.createCell(4);
					cell3.setCellValue(dc.getClassName());
					HSSFCell cell4= row.createCell(5);
					cell4.setCellValue(dc.getClassNum());
					num++;
					ind++;
				}
			}
			if (CollectionUtils.isNotEmpty(aDtoList)) {
				if(num>1){
					CellRangeAddress car = new CellRangeAddress(indexa,indexa+num-1,0,0);
					sheet.addMergedRegion(car);
					indexa+=num;
				}else{
					indexa++;
				}
			}
			num=0;
			ind=indexb;
			for (int i = 0; i < bDtoList.size(); i++) {
				row= sheet.getRow(ind);
				if(row==null){
					row = sheet.createRow(ind);
				}
				row.setHeightInPoints((1.5f) * sheet.getDefaultRowHeightInPoints());
				if(i==0){
					HSSFCell cell0 = row.createCell(7);
					cell0.setCellValue(coumap.get(entry.getKey()));
				}
				DivideResultDto bDto = bDtoList.get(i);
				HSSFCell cell1= row.createCell(8);
				cell1.setCellValue(bDto.getClassName());
				HSSFCell cell2= row.createCell(9);
				cell2.setCellValue(bDto.getTotalNum());
				List<NewGkDivideClass> blist = bDto.getNormalClassList();
				if(CollectionUtils.isNotEmpty(blist) && blist.size()>1){
					CellRangeAddress inCar1 = new CellRangeAddress(ind,ind+blist.size()-1,8,8);
					sheet.addMergedRegion(inCar1);
					CellRangeAddress inCar2 = new CellRangeAddress(ind,ind+blist.size()-1,9,9);
					sheet.addMergedRegion(inCar2);
					for (int j = 0; j < blist.size(); j++) {
						NewGkDivideClass dc = blist.get(j);
						row = sheet.getRow(ind+j);
						if(row==null){
							row = sheet.createRow(ind+j);
						}
						row.setHeightInPoints((1.5f) * sheet.getDefaultRowHeightInPoints());
						HSSFCell cell3 = row.createCell(10);
						cell3.setCellValue(dc.getClassName());
						HSSFCell cell4 = row.createCell(11);
						cell4.setCellValue(dc.getClassNum());
					}
					num+=blist.size();
					ind+=blist.size();
				}else{
					if(CollectionUtils.isNotEmpty(blist)) {
						NewGkDivideClass dc = blist.get(0);
						HSSFCell cell3= row.createCell(10);
						cell3.setCellValue(dc.getClassName());
						HSSFCell cell4= row.createCell(11);
						cell4.setCellValue(dc.getClassNum());
					}else {
						HSSFCell cell3= row.createCell(10);
						cell3.setCellValue("");
						HSSFCell cell4= row.createCell(11);
						cell4.setCellValue("");
					}
					num++;
					ind++;
				}
			}
			if (CollectionUtils.isNotEmpty(bDtoList)) {
				if(num>1){
					CellRangeAddress car = new CellRangeAddress(indexb,indexb+num-1,7,7);
					sheet.addMergedRegion(car);
					indexb+=num;
				}else{
					indexb++;
				}
			}
		}

		logger.error(indexa + " " + indexb);
		CellRangeAddress car = new CellRangeAddress(0, (indexa < indexb ? indexb : indexa) - 1, 6, 6);
		sheet.addMergedRegion(car);

		for (int i = 0; i < (indexa < indexb ? indexb : indexa); i++) {
			HSSFRow rowTmp = sheet.getRow(i);
			if (rowTmp == null) {
				rowTmp = sheet.createRow(i);
			}
			for (int j = 0; j < 12; j++) {
				HSSFCell cellTmp = rowTmp.getCell(j);
				if (cellTmp == null) {
					cellTmp = rowTmp.createCell(j);
				}
				if (j == 6) {
					cellTmp.setCellStyle(splitStyle);
				} else {
					cellTmp.setCellStyle(centerStyle);
				}
			}
		}

		List<NewGkDivideClass> allClassList = newGkDivideClassService.
				findByDivideIdAndClassTypeSubjectType(divide.getUnitId(), divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_1, NewGkElectiveConstant.CLASS_TYPE_2, NewGkElectiveConstant.CLASS_TYPE_3, NewGkElectiveConstant.CLASS_TYPE_4},
						true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, null);

		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(divide.getUnitId()), Course.class);
		Map<String, Course> courseMap = courseList.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));

		// 获取选课数据
		Map<String,Set<String>> subjectChosenMap = new HashMap<>();
		Map<String,Set<String>> studentChosenMap = new HashMap<>();
		makeStudentChooseResult(divide, subjectChosenMap, studentChosenMap);

		List<NewGkDivideClass> showTeachClass = allClassList.stream().filter(e -> NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())).collect(Collectors.toList());
		Set<String> allStudentIdSet = showTeachClass.stream().flatMap(e -> e.getStudentList().stream()).collect(Collectors.toSet());
		List<Student> allStudentList = SUtils.dt(studentRemoteService.findListByIds(allStudentIdSet.toArray(new String[0])), Student.class);
		Collections.sort(allStudentList, (x,y)->{
			if(x.getStudentCode() == null && y.getStudentCode() == null) {
				return 0;
			}else if(x.getStudentCode() == null) {
				return 1;
			}else if(y.getStudentCode() == null) {
				return -1;
			}
			return x.getStudentCode().compareTo(y.getStudentCode());
		});
		
		List<StudentResultDto> studentResultDtoList = makeStudentDto(divide, courseList, allStudentList, allClassList, courseMap, studentChosenMap,stuSubjectScoreMap);
		Map<String, StudentResultDto> stringStudentResultDtoMap = EntityUtils.getMap(studentResultDtoList, StudentResultDto::getStudentId);

		HSSFSheet sheetTmp;
		if (CollectionUtils.isNotEmpty(showTeachClass)) {
			List<String> titleList1 = new ArrayList<>();
			titleList1.add("学号");
			titleList1.add("姓名");
			titleList1.add("性别");
			titleList1.add("行政班");
			titleList1.add("选课科目");
			titleList1.add("总成绩");
			titleList1.add("科目成绩");
			titleList1.add("科目排名");
			List<String> titleList2 = new ArrayList<>();
			titleList2.add("学号");
			titleList2.add("姓名");
			titleList2.add("性别");
			titleList2.add("行政班");
			titleList2.add("选课科目");
			titleList2.add("总成绩");
			
			//计算学生排名成绩
			//key:studentId_subjectId
			Map<String,Integer> rankMap=makeAllRank(stuIdBysubjectA,stuScoreMap);
			List<String> titleList =null;
			for (NewGkDivideClass classTmp : showTeachClass) {
				boolean isA=false;
				if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(classTmp.getSubjectType())) {
					titleList=titleList1;
					isA=true;
				}else {
					titleList=titleList2;
				}
				sheetTmp = workbook.createSheet(classTmp.getClassName());
				sheetTmp.createFreezePane(0, 1);
				HSSFRow titleRow = sheetTmp.createRow(0);
				titleRow.setHeightInPoints((2f) * sheetTmp.getDefaultRowHeightInPoints());
				for (int index = 0; index < titleList.size(); index++) {
					HSSFCell cellTmp = titleRow.createCell(index);
					cellTmp.setCellStyle(splitStyle);
					cellTmp.setCellValue(new HSSFRichTextString(titleList.get(index)));
				}
				List<String> studentIdListTmp = classTmp.getStudentList();
				List<Student> stuList = allStudentList.stream().filter(e->studentIdListTmp.contains(e.getId())).collect(Collectors.toList());
				if(CollectionUtils.isNotEmpty(stuList)) {
					for (int index = 1; index <= stuList.size(); index++) {
						StudentResultDto tmp = stringStudentResultDtoMap.get(stuList.get(index - 1).getId());
						HSSFRow rowTmp = sheetTmp.createRow(index);
						rowTmp.setHeightInPoints((1.5f) * sheetTmp.getDefaultRowHeightInPoints());
						rowTmp.createCell(0).setCellValue(tmp.getStudentCode());
						rowTmp.createCell(1).setCellValue(tmp.getStudentName());
						rowTmp.createCell(2).setCellValue(tmp.getSex());
						rowTmp.createCell(3).setCellValue(tmp.getClassName());
						rowTmp.createCell(4).setCellValue(tmp.getChooseSubjects());
						rowTmp.createCell(5).setCellValue(tmp.getSubjectScore().get("TOTAL"));
						if(isA) {
							rowTmp.createCell(6).setCellValue(tmp.getSubjectScore().get(classTmp.getSubjectIds())==null?0.0f:tmp.getSubjectScore().get(classTmp.getSubjectIds()));
							if(rankMap.containsKey(tmp.getStudentId()+"_"+classTmp.getSubjectIds())) {
								rowTmp.createCell(7).setCellValue(String.valueOf(rankMap.get(tmp.getStudentId()+"_"+classTmp.getSubjectIds())));
							}else {
								rowTmp.createCell(7).setCellValue("/");
							}
						}
						for (int i = 0; i < 8; i++) {
							if (!isA && (i == 6 || i == 7)) {
								continue;
							}
							rowTmp.getCell(i).setCellStyle(centerStyle);
						}
					}
				}
				sheetTmp.autoSizeColumn(0);
			}
		}
		
		ExportUtils.outputData(workbook, divide.getDivideName().replaceAll(" ", "")+"教学班组成统计", resp);
		return returnSuccess();
    }
	private Map<String, Integer> makeAllRank(Map<String, List<String>> stuIdBysubjectA,
			Map<String, Map<String, Float>> stuScoreMap) {
		Map<String, Integer> returnMap=new HashMap<>();
		for(Entry<String, List<String>> item:stuIdBysubjectA.entrySet()) {
			String subId=item.getKey();
			Map<String, Float> scoreMap = stuScoreMap.get(subId)==null?new HashMap<>():stuScoreMap.get(subId);
			List<String> stuIdList=item.getValue();
			Map<String,Float> relScoreMap=new HashMap<>();
			for(String s:stuIdList) {
				if(scoreMap.containsKey(s)) {
					relScoreMap.put(s, scoreMap.get(s)==null?0.0f:scoreMap.get(s));
				}else {
					relScoreMap.put(s, 0.0f);
				}
			}
			List<Float> scoreList = relScoreMap.values().stream().collect(Collectors.toList());
			Collections.sort(scoreList);
			Collections.reverse(scoreList);
			Map<Float,Integer> scoreRankMap=new HashMap<>();
			for(int i=0;i<scoreList.size();i++) {
				if(scoreRankMap.containsKey(scoreList.get(i))) {
					continue;
				}
				scoreRankMap.put(scoreList.get(i), i+1);
			}
			for(Entry<String, Float> item2:relScoreMap.entrySet()) {
				returnMap.put(item2.getKey()+"_"+subId, scoreRankMap.get(item2.getValue()));
			}
		}
		return returnMap;
	}

	/**
	 * @param tId
	 * @param map1 key:stuId
	 * @return
	 */
	private float makeScore(List<String> tId, Map<String, Float> map1) {
		if(CollectionUtils.isEmpty(tId) || map1==null) {
			return 0.0f;
		}
		float sum=0.0f;
		for(String t:tId) {
			if(map1.containsKey(t)) {
				sum=sum+map1.get(t);
			}
		}
		
		return makeTwo(sum/tId.size());
	}

	/*************************3+1+2固定结果展现***********************************/

	private List<StudentResultDto> makeStudentDto(NewGkDivide divide, List<Course> courseList, List<Student> studentList,
												  List<NewGkDivideClass> classList, Map<String, Course> courseMap, Map<String, Set<String>> studentChosenMap,Map<String, Map<String, Float>> stuScoreMap) {

		// 学生 -> 班级
		Map<String, NewGkDivideClass> studentClassMap = new HashMap<>();
		if (classList != null) {
			for (NewGkDivideClass clazz : classList) {
				if (NewGkElectiveConstant.CLASS_TYPE_1.equals(clazz.getClassType())) {
					clazz.getStudentList().forEach(e -> studentClassMap.put(e, clazz));
				}
			}
		}

		List<Course> ysyCourses = SUtils.dt(courseRemoteService.findByCodesYSY(getLoginInfo().getUnitId()),Course.class);

		// 成绩
		String mcodeId = ColumnInfoUtils.getColumnInfo(Student.class, "sex").getMcodeId();
		Map<String, McodeDetail> codeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId(mcodeId), new TypeReference<Map<String, McodeDetail>>() {});
		List<StudentResultDto> dtoList = new ArrayList<>();
		StudentResultDto dto;
		for (Student stu : studentList) {
			dto = new StudentResultDto();
			dto.setStudentId(stu.getId());
			dto.setStudentCode(stu.getStudentCode());
			dto.setStudentName(stu.getStudentName());
			if (stu.getSex() != null) {
				dto.setSex(codeMap.get(stu.getSex() + "").getMcodeContent());
			}else {
				dto.setSex("未知性别");
			}

			Map<String, Float> scoreMap = stuScoreMap.get(stu.getId());
			Float scoreTmp;
			Float ysyScore = Float.valueOf(0.0f);
			Float totalScore = Float.valueOf(0.0f);
			if (scoreMap == null) {
				scoreMap = new HashMap<>();
			}
			for (Course one : ysyCourses) {
				scoreTmp = scoreMap.get(one.getId());
				ysyScore += (scoreTmp == null ? Float.valueOf(0.0f) : scoreTmp);
			}
			totalScore += ysyScore;
			dto.getSubjectScore().put("YSY", ysyScore);
			if (courseList != null) {
				for (Course one : courseList) {
					dto.getSubjectScore().put(one.getId(), scoreMap.get(one.getId()));
					totalScore += scoreMap.get(one.getId()) == null ? 0.0f : scoreMap.get(one.getId());
				}
			}

			dto.getSubjectScore().put("TOTAL", totalScore);

			NewGkDivideClass divideClass = studentClassMap.get(stu.getId());
			if (divideClass == null || org.apache.commons.lang3.StringUtils.isBlank(divideClass.getClassName())) {
				dto.setClassName("未知");
			} else {
				dto.setClassName(studentClassMap.get(stu.getId()).getClassName());
			}
			if (studentChosenMap != null && studentChosenMap.size() > 0 && studentChosenMap.get(stu.getId()) != null) {
				String chosenName = studentChosenMap.get(stu.getId()).stream().map(e -> courseMap.get(e).getShortName()).collect(Collectors.joining(""));
				dto.setChooseSubjects(chosenName);
			}
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	private String showDivideComResultIndex(NewGkDivide divide, String type, ModelMap map) {
		map.put("showOther", false);
		map.put("showXZB", false);
		if(NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())) {
			map.put("showXZB", true);
		}
		map.put("type", type);
		map.put("dividiId", divide.getId());
		map.put("gradeId", divide.getGradeId());
		return "/newgkelective/divide/divideResultIndex2.ftl";
	}
	
	
	private String showDivideResult2(NewGkDivide divide, String fromSolve, String arrayId, String type, ModelMap map) {
		map.put("type", type);
		if ("X".equals(type) || "Y".equals(type)) {
			// 行政班结果
			showDivideXzbList2(divide,map);
			return "/newgkelective/divide/divideXzbResultList2.ftl";
		}
		if(!"J".equals(type)) {
			return errorFtl(map, "参数错误");
		}
		List<NewGkDivideClass> list = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divide.getId(), new String[] {NewGkElectiveConstant.CLASS_TYPE_4}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<DivideClassDto> dtoList = makeStudentItem(divide, list);
		map.put("dtoList", dtoList);
		return "/newgkelective/divide/divideComJxbResultList.ftl";
	}
	
	public void showDivideXzbList2(NewGkDivide divide, ModelMap map) {
		List<Course> list = newGkChoRelationService.findChooseSubject(divide.getChoiceId(), divide.getUnitId());
		Map<String,Course> courseMap=new HashMap<>();
		String lsCouserId="";
		String wlCouserId="";
		for(Course c:list) {
			courseMap.put(c.getId(), c);
			if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(c.getSubjectCode())) {
				lsCouserId=c.getId();
			}else if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(c.getSubjectCode())) {
				wlCouserId=c.getId();
			}
		}
		boolean isNoOpenXzb=NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType());//不重组
		
		Set<String> wlStudents=new HashSet<>();
		Set<String> lsStudents=new HashSet<>();
		final String ls=lsCouserId;
		final String wl=wlCouserId;
		if(isNoOpenXzb) {
			List<NewGkChoResult> resultList = newGkChoResultService.findByChoiceIdAndKindType(divide.getUnitId(), NewGkElectiveConstant.KIND_TYPE_01, divide.getChoiceId());
			lsStudents=resultList.stream().filter(e->e.getSubjectId().equals(ls)).map(e->e.getStudentId()).collect(Collectors.toSet());
			wlStudents=resultList.stream().filter(e->e.getSubjectId().equals(wl)).map(e->e.getStudentId()).collect(Collectors.toSet());
		}
        List<NewGkDivideClass>  classList = newGkDivideClassService.findByDivideIdAndClassTypeWithMaster(divide.getUnitId(),
                divide.getId(),
                new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_0}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
        
		//不选用根据id去查学生信息
		List<Student> studentList =SUtils.dt(studentRemoteService.findPartStudByGradeId(divide.getUnitId(),divide.getGradeId(),null, null), Student.class);
		Map<String, Student> studentMap = EntityUtils.getMap(studentList,Student::getId);
		
		List<NewGkDivideClass> xzbClassList = classList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType()))
				.sorted((x,y)->{
					if(x.getOrderId()==null){
						return 1;
					}else if(y.getOrderId()==null){
						return -1;
					}else if(x.getOrderId().compareTo(y.getOrderId()) != 0){
						return x.getOrderId().compareTo(y.getOrderId());
					}
					return x.getClassName().compareTo(y.getClassName());
				})
				.collect(Collectors.toList());
		List<NewGkDivideClass> zhbClassList = classList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType()))
				.collect(Collectors.toList());
		
		Map<String, NewGkDivideClass> zhbMap = EntityUtils.getMap(zhbClassList, NewGkDivideClass::getId);
		
		String mcodeId = ColumnInfoUtils.getColumnInfo(Student.class, "sex").getMcodeId();
		Map<String, McodeDetail> codeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId(mcodeId), 
				new TypeReference<Map<String, McodeDetail>>() {});
		String manCodet = "";
		String woManCodet ="";
		for (String thisId : codeMap.keySet()) {
			if("男".equals(codeMap.get(thisId).getMcodeContent())) {
				manCodet = thisId;
			}
			if("女".equals(codeMap.get(thisId).getMcodeContent())) {
				woManCodet = thisId;
			}
		}
		final String manCode = manCodet;
		final String woManCode = woManCodet;
		
		NewGkDivideClass zhbClass;
		List<Student> studentsT;
		int manCount;
		int woManCount;
		
		List<DivideClassDto> dtoList = new ArrayList<>();
		DivideClassDto dto;
		for (NewGkDivideClass xzbClass : xzbClassList) {
			dto=new DivideClassDto();
			dto.setClassId(xzbClass.getId());
			dto.setClassName(xzbClass.getClassName());
			zhbClass = null;
			if(xzbClass.getRelateId() != null && zhbMap.get(xzbClass.getRelateId()) != null) {
				zhbClass = zhbMap.get(xzbClass.getRelateId());
			}
			if(zhbClass != null) {
				dto.setRelateName(zhbClass.getClassName());
				//学考上课科目
				if(!isNoOpenXzb) {
					String courseB="";
					if(NewGkElectiveConstant.SUBJECT_TYPE_3.equals(zhbClass.getSubjectType())) {
						for(Course c:list) {
							if(zhbClass.getSubjectIds().indexOf(c.getId())>-1) {
								continue;
							}
							courseB=courseB+"、"+c.getSubjectName();
						}
						if(StringUtils.isNotBlank(courseB)) {
							courseB=courseB.substring(1);
						}
					}else {
						if(zhbClass.getSubjectIds().equals(lsCouserId)) {
							courseB=courseMap.get(wlCouserId).getSubjectName();
						}else if(zhbClass.getSubjectIds().equals(wlCouserId)) {
							courseB=courseMap.get(lsCouserId).getSubjectName();
						} 
					}
					dto.setCourseB(courseB);
				}
			}
			studentsT = xzbClass.getStudentList().stream().map(e->studentMap.get(e)).collect(Collectors.toList());
			
			manCount = (int)studentsT.stream().filter(e->e!=null && (""+e.getSex()).equals(manCode)).count();
			woManCount = (int)studentsT.stream().filter(e->e!=null && (""+e.getSex()).equals(woManCode)).count();
			dto.setAllNum(xzbClass.getStudentList().size());
			dto.setBoyNum(manCount);
			dto.setGirlNum(woManCount);
			
			if(isNoOpenXzb) {
				int wlStuNums=CollectionUtils.intersection(wlStudents, xzbClass.getStudentList()).size();
				int liStuNums=CollectionUtils.intersection(lsStudents, xzbClass.getStudentList()).size();
				dto.setWlStuNums(wlStuNums);
				dto.setLiStuNums(liStuNums);
			}
			
			
			dtoList.add(dto);
		}
		
		map.put("dtoList", dtoList);
		map.put("divideId", divide.getId());
		map.put("isNoOpenXzb", isNoOpenXzb);
    }
	
	private List<DivideClassDto> makeStudentItem(NewGkDivide divide,List<NewGkDivideClass> list) {
		List<DivideClassDto> dtoList=new ArrayList<>();
		if(CollectionUtils.isEmpty(list)) {
			return dtoList;
		}
		List<Course> courseList = newGkChoRelationService.findChooseSubject(divide.getChoiceId(), divide.getUnitId());
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, e->e.getId());
		List<Student> studentList =SUtils.dt(studentRemoteService.findPartStudByGradeId(divide.getUnitId(),divide.getGradeId(),null, null), Student.class);
		Map<String, Student> studentMap = EntityUtils.getMap(studentList,Student::getId);

		
		String mcodeId = ColumnInfoUtils.getColumnInfo(Student.class, "sex").getMcodeId();
		Map<String, McodeDetail> codeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId(mcodeId), 
				new TypeReference<Map<String, McodeDetail>>() {});
		String manCodet = "";
		String woManCodet ="";
		for (String thisId : codeMap.keySet()) {
			if("男".equals(codeMap.get(thisId).getMcodeContent())) {
				manCodet = thisId;
			}
			if("女".equals(codeMap.get(thisId).getMcodeContent())) {
				woManCodet = thisId;
			}
		}
		final String manCode = manCodet;
		final String woManCode = woManCodet;
		
		DivideClassDto dto;
		List<Student> studentsT;
		int manCount;
		int woManCount;
		for (NewGkDivideClass zhbClass4 : list) {
			dto=new DivideClassDto();
			dto.setClassId(zhbClass4.getId());
			dto.setClassName(zhbClass4.getClassName());	
			studentsT = zhbClass4.getStudentList().stream().map(e->studentMap.get(e)).collect(Collectors.toList());
			manCount = (int)studentsT.stream().filter(e->e!=null && (""+e.getSex()).equals(manCode)).count();
			woManCount = (int)studentsT.stream().filter(e->e!=null && (""+e.getSex()).equals(woManCode)).count();
			dto.setAllNum(zhbClass4.getStudentList().size());
			dto.setBoyNum(manCount);
			dto.setGirlNum(woManCount);
			
			String[] arr1 = zhbClass4.getSubjectIds().split(",");
			String courseA="";
			for(String s:arr1) {
				courseA=courseA+"、"+courseMap.get(s).getSubjectName();
			}
			dto.setCourseA(courseA.substring(1));
			String[] arr2 = zhbClass4.getSubjectIdsB().split(",");
			String courseB="";
			for(String s:arr2) {
				courseB=courseB+"、"+courseMap.get(s).getSubjectName();
			}
			dto.setCourseB(courseB.substring(1));
			dtoList.add(dto);
		}
		
		return dtoList;
	}
	
	public class DivideClassDto{
		private String classId;
		private String className;
		private String relateName;
		private int allNum;
		private int girlNum;
		private int boyNum;
		private String courseA;
		private String courseB;
		private int wlStuNums;
		private int liStuNums;

		public String getClassId() {
			return classId;
		}
		public void setClassId(String classId) {
			this.classId = classId;
		}
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}
		public int getAllNum() {
			return allNum;
		}
		public void setAllNum(int allNum) {
			this.allNum = allNum;
		}
		public int getGirlNum() {
			return girlNum;
		}
		public void setGirlNum(int girlNum) {
			this.girlNum = girlNum;
		}
		
		public int getBoyNum() {
			return boyNum;
		}
		public void setBoyNum(int boyNum) {
			this.boyNum = boyNum;
		}
		public String getCourseA() {
			return courseA;
		}
		public void setCourseA(String courseA) {
			this.courseA = courseA;
		}
		public String getCourseB() {
			return courseB;
		}
		public void setCourseB(String courseB) {
			this.courseB = courseB;
		}
		public String getRelateName() {
			return relateName;
		}
		public void setRelateName(String relateName) {
			this.relateName = relateName;
		}
		public int getWlStuNums() {
			return wlStuNums;
		}
		public void setWlStuNums(int wlStuNums) {
			this.wlStuNums = wlStuNums;
		}
		public int getLiStuNums() {
			return liStuNums;
		}
		public void setLiStuNums(int liStuNums) {
			this.liStuNums = liStuNums;
		}
		
		
	}
	
	
	
	
}
