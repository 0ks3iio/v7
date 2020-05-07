package net.zdsoft.newgkelective.data.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import net.sf.json.JSONArray;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ArrayUtil;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.SortUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.DivideClassEditSaveDto;
import net.zdsoft.newgkelective.data.dto.DivideResultDto;
import net.zdsoft.newgkelective.data.dto.NewGKStudentRangeDto;
import net.zdsoft.newgkelective.data.dto.NewGkGroupDto;
import net.zdsoft.newgkelective.data.dto.NewGkQuickGroupDto;
import net.zdsoft.newgkelective.data.dto.NewGkQuickSaveDto;
import net.zdsoft.newgkelective.data.dto.SaveMoveStudentDto;
import net.zdsoft.newgkelective.data.dto.StudentResultDto;
import net.zdsoft.newgkelective.data.entity.NewGKStudentRange;
import net.zdsoft.newgkelective.data.entity.NewGKStudentRangeEx;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;
import net.zdsoft.newgkelective.data.entity.NewGkClassBatch;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkDivideStusub;
import net.zdsoft.newgkelective.data.entity.NewGkOpenSubject;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectGroupColor;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.shuffling.ShufflingApp;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.shuffling.ShufflingAppDto;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning2.api.Sectioning2A1XInput;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning2.domain1.SectioningApp2;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning2.domain1.SectioningApp3;
import net.zdsoft.newgkelective.data.optaplanner.common.CalculateSections;
import net.zdsoft.newgkelective.data.optaplanner.shuff.api.ShuffleInput;
import net.zdsoft.newgkelective.data.optaplanner.shuff.api.ShuffleResult;
import net.zdsoft.newgkelective.data.optaplanner.shuff.api.ShuffleWork;
import net.zdsoft.newgkelective.data.optaplanner2.domain.ArrangeCapacityRange;
import net.zdsoft.newgkelective.data.service.NewGKStudentRangeExService;
import net.zdsoft.newgkelective.data.service.NewGkClassBatchService;
import net.zdsoft.newgkelective.data.service.NewGkDivideStusubService;

/**
 *  进入某个分班方案
 */
@Controller
@RequestMapping("/newgkelective/{divideId}/divideClass")
public class NewGkDivideItemAction extends NewGkElectiveDivideCommonAction{
	private static Logger logger = LoggerFactory.getLogger(NewGkDivideItemAction.class);
	@Autowired
	private NewGkDivideStusubService  newGkDivideStusubService;
	@Autowired
	private NewGkClassBatchService newGkClassBatchService;
	@Autowired
	private NewGKStudentRangeExService newGKStudentRangeExService;
	
	//设置redis缓存时间 一般不会超过1个小时
	private static int TIME_ONE_HOUR=RedisUtils.TIME_ONE_HOUR;
	
	
	@ResponseBody
	@RequestMapping("/checkByDivideIdCanEdit")
	@ControllerInfo(value = "验证是否可操作功能")
	public String checkByDivideIdCanEdit(@PathVariable String divideId) {
		JSONObject obj=new JSONObject();
		NewGkDivide divide = newGkDivideService.findOneWithMaster(divideId);
		if (divide == null) {
			obj.put("type", "3");
			obj.put("msg", "分班方案不存在,返回列表页进行操作");
			return obj.toJSONString();
		}
		if (NewGkElectiveConstant.IF_1.equals(divide.getStat())) {
			obj.put("type", "2");
			obj.put("msg", "分班已完成");
			return obj.toJSONString();
		}
		if(divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_10)) {
			long countNum = newGkDivideClassService.countByDivideId(divideId,NewGkElectiveConstant.CLASS_TYPE_4,NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
			if(countNum>0) {
				obj.put("type", "1");
				obj.put("msg", "已经安排剩余两科组合数据，不能操作");
				return obj.toJSONString();
			}
			obj.put("type", "0");
			obj.put("msg", "");
			return obj.toJSONString();
		}
		if(checkAutoTwo(divideId) || isNowDivide(divideId)){
			//正在智能分班中
			obj.put("type", "1");
			obj.put("msg", "正在分班中，不能操作");
			return obj.toJSONString();
		}
		//是否有后一步操作----目前根据是否开设教学班考虑---classbath
		long countNum = newGkDivideClassService.countByDivideId(divideId,NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
		if(countNum>0) {
			obj.put("type", "1");
			obj.put("msg", "已经安排教学班数据，不能操作");
			return obj.toJSONString();
		}else {
			//根据科目批次点
			long num = newGkClassBatchService.countByDivideId(divideId);
			if(num>0) {
				obj.put("type", "1");
				obj.put("msg", "已经进行下一步，不能操作");
				return obj.toJSONString();
			}
		}
		obj.put("type", "0");
		obj.put("msg", "");
		return obj.toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/checkAllInGroup")
	@ControllerInfo(value = "进入下一步前，验证学生是否正确")
	public String checkAllInGroup(@PathVariable String divideId) {
		NewGkDivide gkDivide = newGkDivideService.findById(divideId);
		
		List<NewGkDivideClass> gkDivideClassList = newGkDivideClassService
				.findByDivideIdAndClassTypeWithMaster(gkDivide.getUnitId(),
						divideId,
						new String[] { NewGkElectiveConstant.CLASS_TYPE_0 }, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		if (CollectionUtils.isEmpty(gkDivideClassList)) {
			return error("没有安排班级");
		}
		if(gkDivide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_10)) {
			//只能进入物理，历史以及3科组合班级
			Set<String> types = EntityUtils.getSet(gkDivideClassList, e->e.getSubjectType());
			types.remove(NewGkElectiveConstant.SUBJTCT_TYPE_3);
			types.remove(NewGkElectiveConstant.SUBJTCT_TYPE_1);
			if(CollectionUtils.isNotEmpty(types)) {
				return error("数据有误，存在学生不在三科组合以及单科组合内，请重新分配");
			}
			Set<String> subIds = gkDivideClassList.stream().filter(e->e.getSubjectType().equals(NewGkElectiveConstant.SUBJTCT_TYPE_1)).map(e->e.getSubjectIds()).filter(Objects::nonNull).collect(Collectors.toSet());
			if(CollectionUtils.isNotEmpty(subIds)) {
				List<Course> courselist = SUtils.dt(courseRemoteService.findBySubjectIdIn(subIds.toArray(new String[] {})),Course.class);
				if(CollectionUtils.isEmpty(courselist)) {
					return error("科目数据有误，请检查科目模块");
				}
				for(Course c:courselist) {
					if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(c.getSubjectCode()) || NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(c.getSubjectCode())) {
						
					}else {
						return error("存在学生不属于物理或者历史的单科组合内，请重新分配");
					}
				}
			}
		}
		List<NewGkDivideStusub> stuchooseList = newGkDivideStusubService.findByDivideIdWithMaster(divideId, NewGkElectiveConstant.SUBJECT_TYPE_A, null);
		// 学生数据是不是正确，有没有在2个班级内
		String errorMess = checkStudentIfRight(gkDivideClassList, stuchooseList);
		if (StringUtils.isNotBlank(errorMess)) {
			return error(errorMess);
		}
		//是不是都是3+0 无需走到下一步
		int notThree=0;
		for(NewGkDivideClass item:gkDivideClassList){
			if(!NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(item.getSubjectType())){
				notThree++;
			}
		}
		if(notThree==0){
			try{
				saveXzbClass(gkDivide, gkDivideClassList);
				newGkDivideService.updateStat(divideId, NewGkElectiveConstant.IF_1);
				return success("allArrange");
			}catch(Exception e){
				return error("全部为3+0组合，直接保存失败");
			}
		}else {
			try{
				saveXzbClass(gkDivide, gkDivideClassList);
			}catch(Exception e){
				return error("数据验证成功，生成行政班报错");
			}
		}
		return success("");
	}
	
	/**
	 * 组合班转行政班
	 */
	private void saveXzbClass(NewGkDivide divide,List<NewGkDivideClass> gkDivideClassList) {
		List<NewGkDivideClass> divideClassList = new ArrayList<NewGkDivideClass>();
		List<NewGkClassStudent> classStuList = new ArrayList<NewGkClassStudent>();
		List<NewGkDivideClass> oldXzbDivideClassList = newGkDivideClassService
				.findByDivideIdAndClassType(divide.getUnitId(),
						divide.getId(),
						new String[] { NewGkElectiveConstant.CLASS_TYPE_1 }, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		String[] deleClassIds=null;
		if(CollectionUtils.isNotEmpty(oldXzbDivideClassList)){
			Set<String> deleIds=EntityUtils.getSet(oldXzbDivideClassList, e->e.getId());
			deleClassIds=deleIds.toArray(new String[]{});
		}
		
		int index=1;
		NewGkClassStudent teachClassStu;
		NewGkDivideClass xzbClass;
		for(NewGkDivideClass newClass:gkDivideClassList){
			xzbClass = initXzbClass(divide.getId(),index);
			index++;
			xzbClass.setRelateId(newClass.getId());
			divideClassList.add(xzbClass);
			//学生
			for (String stuid : newClass.getStudentList()) {
				teachClassStu = initClassStudent(divide.getUnitId(), divide.getId(), xzbClass.getId(), stuid);
				classStuList.add(teachClassStu);
			}
		}
		newGkDivideClassService.saveAllList(divide.getUnitId(), divide.getId(), deleClassIds, divideClassList, classStuList, false);
	}
	
	private NewGkDivideClass initXzbClass(String divideId,int index) {
		NewGkDivideClass xzbClass=initNewGkDivideClass(divideId,null,NewGkElectiveConstant.CLASS_TYPE_1);
		xzbClass.setClassType(NewGkElectiveConstant.CLASS_TYPE_1);
		if(index/10 == 0) {
			xzbClass.setClassName("0"+index+"班");
		}else {
			xzbClass.setClassName(index+"班");
		}
		xzbClass.setOrderId(index);
		return xzbClass;
	}

	/**
	 * 验证所有学生安排在正确的组合班
	 * @param gkDivideClassList
	 * @param stuchooseList
	 * @return
	 */
	private String checkStudentIfRight(List<NewGkDivideClass> gkDivideClassList,
			List<NewGkDivideStusub> stuchooseList) {
		Set<String> gIds = new HashSet<String>();// 班级下人数为0
		String errorStr = "";// 返回报错信息
		Set<String> arrangeStuId = new HashSet<String>();
		Map<String, NewGkDivideStusub> stuMap = EntityUtils.getMap(stuchooseList, e->e.getStudentId());
		Map<String,Set<String>> stuBysubMap=new HashMap<>();
		if (CollectionUtils.isNotEmpty(gkDivideClassList)) {
			for (NewGkDivideClass dd : gkDivideClassList) {
				if (CollectionUtils.isEmpty(dd.getStudentList())) {
					gIds.add(dd.getId());
					continue;
				} 
				if (NewGkElectiveConstant.SUBJTCT_TYPE_0.equals(dd.getSubjectType())) {
					//混合
					if (CollectionUtils.isNotEmpty(dd.getStudentList())) {
						arrangeStuId.addAll(dd.getStudentList());
					}
				}else {
					Set<String> lst1 = stuBysubMap.get(dd.getSubjectIds());
					if(CollectionUtils.isEmpty(lst1)) {
						List<NewGkDivideStusub> list1= EntityUtils.filter2(stuchooseList,e->{
							String ss = dd.getSubjectIds();
							String[] arr = ss.split(",");
							for(String r:arr) {
								if(e.getSubjectIds().indexOf(r)>-1) {
									
								}else {
									return false;
								}
							}
							return true;
						});
						lst1=EntityUtils.getSet(list1, e->e.getStudentId());
						stuBysubMap.put(dd.getSubjectIds(), lst1);
					}
					//考虑同个班级下学生id重复
					if (!(CollectionUtils.intersection(dd.getStudentList(),
							lst1).size() == dd.getStudentList().size())) {
						errorStr = errorStr + "," + dd.getClassName();
					}else{
						arrangeStuId.addAll(dd.getStudentList());
					}
				}
			}
		}
		if (StringUtils.isNotBlank(errorStr)) {
			errorStr = errorStr.substring(1);
			errorStr = "数据验证失败！在" + errorStr + "中存在学生选课数据与班级的组合科目信息不对应";
			return errorStr;
		} else {
			// 判断学生是否有在超过两个班级---arrangStuList是否有重复
			Map<String, String> stumap = checkStuClassNum(gkDivideClassList);
			if (stumap != null && stumap.size() > 0) {
				for (String s : stumap.keySet()) {
					if (stuMap.containsKey(s)
							&& StringUtils.isNotBlank(stumap.get(s))) {
						errorStr = errorStr + "，" + stuMap.get(s).getStudentName() + "("
								+ stumap.get(s) + ")";
					}
				}
				errorStr = "数据验证失败！存在部分学生安排在超过一个的组合班级，有" + errorStr.substring(1) + "。";
				return errorStr;
			}

		}
		if (arrangeStuId.size() != stuchooseList.size()) {
			errorStr = "存在学生数据有误，原因学生选课数据有所调整，或者未安排！";
			return errorStr;
		}
		if (gIds.size() > 0) {
			errorStr = "存在" + gIds.size() + "个组合班级下学生数量为0，请先删除！";
			return errorStr;
		}
		return errorStr;
	}
	
	@ResponseBody
	@RequestMapping("/moveAllGroup")
	@ControllerInfo(value = "移除所有开设班级")
	public String moveAllGroup(@PathVariable String divideId) {
		try {
			String unitId=this.getLoginInfo().getUnitId();
			//删除所有
			List<NewGkDivideClass> groupClassList = newGkDivideClassService
					.findClassBySubjectIdsWithMaster(unitId,
							divideId,
							NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_0,
							null, false);
			if (CollectionUtils.isNotEmpty(groupClassList)) {
				newGkDivideClassService.deleteByClassIdIn(unitId,divideId, EntityUtils
						.getSet(groupClassList, NewGkDivideClass::getId).toArray(
								new String[] {}));
			}

		} catch (Exception e) {
			e.printStackTrace();
			return error("解散数据失败！");
		}
		return success("解散数据成功！");
	}
	
	@ResponseBody
	@RequestMapping("/sysStuChoose")
	@ControllerInfo(value = "更新学生选课数据")
	public String sysStuChoose(@PathVariable String divideId) {
		try {
			NewGkDivide gkDivide = newGkDivideService.findOneWithMaster(divideId);
			String error = newGkDivideStusubService.saveChoiceResult(gkDivide,false,null);
			if(StringUtils.isNotBlank(error)) {
				return error(error);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error("操作失败！");
		}
		return success("操作成功！");
	}
	
	@RequestMapping("/item")
	@ControllerInfo(value = "进入分班方案")
	public String itemIndex(@PathVariable String divideId,ModelMap map) {
		NewGkDivide divide = newGkDivideService.findOneWithMaster(divideId);
		if (divide == null) {
			return errorFtl(map, "分班方案不存在");
		}
		map.put("divide", divide);
		//分班完成
		if (NewGkElectiveConstant.IF_1.equals(divide.getStat())) {
			//进入结果页
			return "redirect:/newgkelective/"+divideId+"/divideClass/resultClassList";
		}
		
		if(NewGkElectiveConstant.DIVIDE_TYPE_09.equals(divide.getOpenType())) {
			//3+1+2单科分层+重组
			return singleRecomb(divide,map);
			
		}else if(NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divide.getOpenType())) {
			//3+1+2单科分层+不重组
			
			return nosingleRecomb(divide,map);
			
		}else if(NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())) {
			//3+1+2走班固定+重组
			NewGkChoice choice = newGkChoiceService.findOne(divide.getChoiceId());
			if (choice == null) {
				return errorFtl(map, "分班方案对应的选课不存在");
			}
			return singlefixed(divide,choice,map);
			
		}else if(NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType())) {
			//3+1+2走班固定+不重组
			
			return nosinglefixed(divide,map);
			
		}
		else if(!NewGkElectiveConstant.DIVIDE_TYPE_08.equals(divide.getOpenType())) {
			//除去08
			return "redirect:/newgkelective/"+divideId+"/divideClass/resultClassList";
		}
		NewGkChoice choice = newGkChoiceService.findOne(divide.getChoiceId());
		if (choice == null) {
			return errorFtl(map, "分班方案对应的选课不存在");
		}
		return showOpenXzbGroup(divide, choice,map);
	}
	
	public String showOpenXzbGroup(NewGkDivide divide,NewGkChoice choice,ModelMap map) {
		String divideId=divide.getId();
		map.put("divideId", divideId);
		map.put("gradeId", divide.getGradeId());
		//是否可以修改
		boolean isAuto2=checkAutoTwo(divideId);//自动2+x分班中或者混合
		boolean isAuto=false;
		if(isAuto2 || isNowDivide(divideId)){
			//正在智能分班中
			isAuto=true;
		}
		boolean isCanEdit=true;//是否可以修改
		if(!isAuto) {
			long countNum = newGkDivideClassService.countByDivideId(divideId,NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
			if(countNum>0) {
				isCanEdit=false;
			}else {
				long num = newGkClassBatchService.countByDivideId(divideId);
				if(num>0) {
					isCanEdit=false;
				}
			}
		}else {
			isCanEdit=false;
		}
		map.put("isAuto2", isAuto2);
		map.put("isCanEdit", isCanEdit);
		
		return makeGroupsItem(divide, choice, map);
				
	}
	
	private List<NewGkGroupDto> makeOldGroupDto(List<NewGkDivideClass> oldGroupList) {
		List<NewGkGroupDto> oldGDtoList = new ArrayList<NewGkGroupDto>();
		if (CollectionUtils.isNotEmpty(oldGroupList)) {
			Set<String> groupIds = new HashSet<String>();
			NewGkGroupDto g = null;
			// key:subjectIds  插入到数据库的subjectIds 本身就是有序的
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
						g.setSubjectType(group.getSubjectType());
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
	
	private Set<String> findStudentChoose(String divideId,Map<String,List<String>> subjectIdsByStuId){
//		List<NewGkChoResult> list = newGkChoResultService.findByChoiceIdAndKindType(choice.getUnitId(), NewGkElectiveConstant.KIND_TYPE_01,choice.getId());
//		Map<String,List<String>> subjectIdsByStuId=new HashMap<String, List<String>>();
		Set<String> chooseIds=new HashSet<>();
//		if(CollectionUtils.isNotEmpty(list)) {
//			chooseIds=EntityUtils.getSet(list, e->e.getSubjectId());
//			subjectIdsByStuId=EntityUtils.getListMap(list, "studentId", "subjectId");
//		}
		
		List<NewGkDivideStusub> list = newGkDivideStusubService.findByDivideIdWithMaster(divideId, NewGkElectiveConstant.SUBJECT_TYPE_A,null);
		if(CollectionUtils.isNotEmpty(list)) {
			for(NewGkDivideStusub s:list) {
				subjectIdsByStuId.put(s.getStudentId(), Arrays.asList(s.getSubjectIds().split(",")));
				chooseIds.addAll(subjectIdsByStuId.get(s.getStudentId()));
			}
		}
		return chooseIds;
	}
	
	public String makeGroupsItem(NewGkDivide divide,NewGkChoice choice,ModelMap map) {
		List<String> subjectIdList = newGkChoRelationService.findByChoiceIdAndObjectType(choice.getUnitId(), choice.getId(), NewGkElectiveConstant.CHOICE_TYPE_01);
		if(CollectionUtils.isEmpty(subjectIdList)) {
			return errorFtl(map, "选课方案："+choice.getChoiceName()+"，选课科目有问题。");
		}
		List<Course> courselist = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIdList.toArray(new String[] {})),Course.class);
		if(CollectionUtils.isEmpty(courselist)) {
			return errorFtl(map, "选课方案："+choice.getChoiceName()+"，选课科目有问题。");
		}
		map.put("courseList",courselist);
		Map<String,Course> courseMap = EntityUtils.getMap(courselist, e->e.getId());
		
		
		Set<String> subIds = courseMap.keySet();
		int size=subIds.size();
		// 所有学生选课情况
		Map<String,List<String>> subjectIdsByStuId=new HashMap<String, List<String>>();
		Set<String> chooseIds=findStudentChoose(divide.getId(), subjectIdsByStuId);
		chooseIds.addAll(chooseIds);
		if(size!=subIds.size()) {
			return errorFtl(map, "选课方案："+choice.getChoiceName()+"，学生选课数据有问题。");
		}
		
		// 已有组合班数据
		List<NewGkDivideClass> oldGroupList = newGkDivideClassService
				.findByDivideIdAndClassType(divide.getUnitId(),divide.getId(),new String[] { NewGkElectiveConstant.CLASS_TYPE_0 }, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		boolean flag = false;// 是否有异常
		List<NewGkGroupDto> oldGroupDtoList = makeOldGroupDto(oldGroupList);
		
		// 3门组合
		List<NewGkGroupDto> gDtoList = new ArrayList<NewGkGroupDto>();
		Map<String, NewGkGroupDto> gDtomap = new HashMap<String, NewGkGroupDto>();
		Map<String, Set<String>> subjectIdsMap = new HashMap<String, Set<String>>();// 同组合下学生

		// 2门组合
		List<NewGkGroupDto> gDtoList2 = new ArrayList<NewGkGroupDto>();
		Map<String, NewGkGroupDto> gDtomap2 = new HashMap<String, NewGkGroupDto>();
		Map<String, Set<String>> subjectIdsMap2 = new HashMap<String, Set<String>>();// 同组合下学生
		//1门组合
		List<NewGkGroupDto> gDtoList3 = new ArrayList<NewGkGroupDto>();
		Map<String, NewGkGroupDto> gDtomap3 = new HashMap<String, NewGkGroupDto>();
		Map<String, Set<String>> subjectIdsMap3 = new HashMap<String, Set<String>>();// 同组合下学生

		// 混合组合
		List<NewGkGroupDto> gDtoList4 = new ArrayList<NewGkGroupDto>();
		Map<String, NewGkGroupDto> gDtomap4 = new HashMap<String, NewGkGroupDto>();
		Map<String, Set<String>> subjectIdsMap4 = new HashMap<String, Set<String>>();// 同组合下学生

		String guidZero = BaseConstants.ZERO_GUID;// 混合subjectIds 
		
		NewGkGroupDto g = new NewGkGroupDto();
		int chooseNum = choice.getChooseNum();
		
		
		Map<String,NewGkGroupDto> stuDtoColorMap3 = new HashMap<>();//用于color
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
				g=makeNewGkGroupDto(ids,NewGkElectiveConstant.SUBJTCT_TYPE_3,nameSet(courseMap, ids));
				
				gDtoList.add(g);
				gDtomap.put(ids, g);
				subjectIdsMap.put(ids, new HashSet<String>());
				subjectIdsMap.get(ids).add(stuId);
			} else {
				subjectIdsMap.get(ids).add(stuId);
				g = gDtomap.get(ids);
				g.setAllNumber(g.getAllNumber() + 1);
			}
			stuDtoColorMap3.put(stuId, g);

			// 两门组合--3中组合
			List<String> twoGroup = keySort2(chooseSubjectId);
			if (CollectionUtils.isNotEmpty(twoGroup)) {
				for (String key : twoGroup) {
					if (!gDtomap2.containsKey(key)) {
						
						g=makeNewGkGroupDto(key,NewGkElectiveConstant.SUBJTCT_TYPE_2,nameSet(courseMap, key));

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
			//1科
			for(String s:chooseSubjectId) {
				if (!gDtomap3.containsKey(s)) {
					g=makeNewGkGroupDto(s,NewGkElectiveConstant.SUBJTCT_TYPE_1,courseMap.get(s).getSubjectName());
					gDtoList3.add(g);
					gDtomap3.put(s, g);
					subjectIdsMap3.put(s, new HashSet<String>());
					subjectIdsMap3.get(s).add(stuId);
				} else {
					subjectIdsMap3.get(s).add(stuId);
					g = gDtomap3.get(s);
					g.setAllNumber(g.getAllNumber() + 1);
				}
				
			}
			
			// 混合
			if (!gDtomap4.containsKey(guidZero)) {
				g=makeNewGkGroupDto(guidZero,NewGkElectiveConstant.SUBJTCT_TYPE_0,"混合");
				gDtoList4.add(g);
				gDtomap4.put(guidZero, g);
				subjectIdsMap4.put(guidZero, new HashSet<String>());
				subjectIdsMap4.get(guidZero).add(stuId);
			} else {
				subjectIdsMap4.get(guidZero).add(stuId);
				g = gDtomap4.get(guidZero);
				g.setAllNumber(g.getAllNumber() + 1);
			}
		}

		Set<String> arrangeStuId = new HashSet<String>();// 已经排的学生
		if (CollectionUtils.isNotEmpty(oldGroupList)) {
			List<NewGkDivideClass> gc = null;
			for (NewGkGroupDto dd : oldGroupDtoList) {
				 if(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(dd.getSubjectType())) {
					 if (gDtomap.containsKey(dd.getSubjectIds())) {
						boolean f= makeArrangeStu(gDtomap.get(dd.getSubjectIds()), dd, subjectIdsMap.get(dd.getSubjectIds()), arrangeStuId,
								subjectIdsByStuId,courseMap);
						if(f){
							dd.setNotexists(1);
						}
						if (!flag && f) {
							flag = true;
						}
					 }else {
						// 这种组合已经不存在啦
						dd.setConditionName(nameSet(courseMap, dd.getSubjectIds()));
						if (!flag) {
							flag = true;
						}
						dd.setNotexists(1);
						gDtoList.add(dd);
						gDtomap.put(dd.getSubjectIds(), dd);
						gc = dd.getGkGroupClassList();
						if (CollectionUtils.isNotEmpty(gc)) {
							for (NewGkDivideClass gg : gc) {
								if (CollectionUtils.isNotEmpty(gg.getStudentList())) {
									arrangeStuId.addAll(gg.getStudentList());
								}
							}
						}
					 }
				 }else if(NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(dd.getSubjectType())) {
					 if (gDtomap2.containsKey(dd.getSubjectIds())) {
						boolean f= makeArrangeStu(gDtomap2.get(dd.getSubjectIds()), dd, subjectIdsMap2.get(dd.getSubjectIds()), arrangeStuId,
								subjectIdsByStuId,courseMap);
						if(f ){
							dd.setNotexists(1);
						}
						if (!flag && f) {
							flag = true;
						}
					 }else {
						dd.setConditionName(nameSet(courseMap, dd.getSubjectIds()));
						if (!flag) {
							flag = true;
						}
						dd.setNotexists(1);
						gDtoList2.add(dd);
						gDtomap2.put(dd.getSubjectIds(), dd);
						gc = dd.getGkGroupClassList();
						if (CollectionUtils.isNotEmpty(gc)) {
							for (NewGkDivideClass gg : gc) {
								if (CollectionUtils.isNotEmpty(gg.getStudentList())) {
									arrangeStuId.addAll(gg.getStudentList());
								}
							}
						}
					 }
				 }else if(NewGkElectiveConstant.SUBJTCT_TYPE_1.equals(dd.getSubjectType())) {
					 if (gDtomap3.containsKey(dd.getSubjectIds())) {
						boolean f= makeArrangeStu(gDtomap3.get(dd.getSubjectIds()), dd, subjectIdsMap3.get(dd.getSubjectIds()), arrangeStuId,
								subjectIdsByStuId,courseMap);
						if(f ){
							dd.setNotexists(1);
						}
						if (!flag && f) {
							flag = true;
						}
					 }else {
						dd.setConditionName(nameSet(courseMap, dd.getSubjectIds()));
						if (!flag) {
							flag = true;
						}
						dd.setNotexists(1);
						gDtoList3.add(dd);
						gDtomap3.put(dd.getSubjectIds(), dd);
						gc = dd.getGkGroupClassList();
						if (CollectionUtils.isNotEmpty(gc)) {
							for (NewGkDivideClass gg : gc) {
								if (CollectionUtils.isNotEmpty(gg.getStudentList())) {
									arrangeStuId.addAll(gg.getStudentList());
								}
							}
						}
					 }
				 }else{
					 //全混合
					 if (gDtomap4.containsKey(dd.getSubjectIds())) {
						 gDtomap4.get(dd.getSubjectIds()).getGkGroupClassList().addAll(dd.getGkGroupClassList());
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
		if (CollectionUtils.isNotEmpty(gDtoList4)) {
			for (NewGkGroupDto dd : gDtoList4) {
				Set<String> stuIds = subjectIdsMap4.get(dd.getSubjectIds());
				// 取得除去 arrangeStuId中剩下的学生
				stuIds.removeAll(arrangeStuId);
				dd.setLeftNumber(stuIds.size());
			}
		}
		
		// 将两科组合中 包含的三科组合 上色
		if(gDtomap2.size()>0) {
			List<NewGkSubjectGroupColor> sgcList = newGkSubjectGroupColorService.findByUnitIdGroupType(choice.getUnitId(),
					new String[] {NewGkElectiveConstant.COLOR_GROUP_TYPE_2});
			Map<String,NewGkSubjectGroupColor> subColorMap = EntityUtils.getMap(sgcList, NewGkSubjectGroupColor::getSubjectGroup);
			//如果有固定有几个色 可以在这里进行添加--页面上默认颜色多一点 但是暂不保存到数据库
			List<String> colorList=new ArrayList<>();
			colorList.addAll(NewGkElectiveConstant.DEFAULT_COLOR);
			Set<String> oldList = EntityUtils.getSet(sgcList, e->e.getColor());
			if(CollectionUtils.isNotEmpty(oldList)) {
				colorList.removeAll(oldList);
			}
			final String DEFAULT_COLR = "#ff6600";
			boolean isHasClass=false;
			//final NewGkSubjectGroupColor defaultGroupColor = new NewGkSubjectGroupColor();
			for (String subKey : gDtomap2.keySet()) {
				NewGkGroupDto newGkGroupDto = gDtomap2.get(subKey);
				isHasClass=false;
				if(CollectionUtils.isNotEmpty(newGkGroupDto.getGkGroupClassList())) {
					isHasClass=true;
				}
				// 确保 subKey 是按照 科目id 排序过后的 字符串  
				NewGkSubjectGroupColor gc ;
				if(subColorMap.containsKey(subKey)) {
					gc=subColorMap.get(subKey);
				}else {
					gc = new NewGkSubjectGroupColor();
					
					if(isHasClass && CollectionUtils.isNotEmpty(colorList)) {
						gc.setColor(colorList.get(0));
						colorList.remove(0);
					}else {
						gc.setColor(DEFAULT_COLR);
					}
				}
				newGkGroupDto.getColorList().add(gc);
				newGkGroupDto.getGkGroupClassList().stream().flatMap(e -> e.getStudentList().stream())
						.filter(e->stuDtoColorMap3.containsKey(e))
						.map(e -> stuDtoColorMap3.get(e)).distinct().forEach(e -> {
							e.getColorList().add(gc);
						});
			}
		}
		
		// 根据总人数排序
		SortUtils.DESC(gDtoList, "allNumber");
		map.put("gDtoList", gDtoList);
		SortUtils.DESC(gDtoList2, "allNumber");
		map.put("gDtoList2", gDtoList2);
		SortUtils.DESC(gDtoList3, "allNumber");
		map.put("gDtoList3", gDtoList3);
		NewGkGroupDto gDto = null;
		if (CollectionUtils.isNotEmpty(gDtoList4)) {
			gDto = gDtoList4.get(0);
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
		
		//默认显示班级数量
		List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(divide.getUnitId(),divide.getGradeId()), new TR<List<Clazz>>(){});
		int xzbNum = clazzList.size();
		map.put("xzbNum", xzbNum);
		map.put("gDto", gDto);
		map.put("haserror", flag);
		return "/newgkelective/divideAuto/openXzb.ftl";

	}
	
	private NewGkGroupDto makeNewGkGroupDto(String subjectIds, String subjectType, String conditionName) {
		NewGkGroupDto g = new NewGkGroupDto();
		g.setSubjectIds(subjectIds);
		g.setSubjectType(subjectType);
		g.setConditionName(conditionName);
		g.setGkGroupClassList(new ArrayList<NewGkDivideClass>());
		g.setAllNumber(1);
		return g;
	}

	/**
	 * 
	 * @param targerDto 目标dto
	 * @param gDto 旧dto
	 * @param stusIds 该组合的学生ids 
	 * @param arrangeStuId
	 * @return
	 */
	private boolean makeArrangeStu(NewGkGroupDto targerDto,NewGkGroupDto gDto,
			Set<String> stusIds,Set<String> arrangeStuId,Map<String,List<String>> stuChooseMap,
			Map<String,Course> courseMap) {
		boolean isShowCom=false;
		if(NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(targerDto.getSubjectType())) {
			isShowCom=true;
		}
		boolean flag=false;
		List<NewGkDivideClass> list = targerDto.getGkGroupClassList();
		list.addAll(gDto.getGkGroupClassList());
		List<NewGkDivideClass> gc = gDto.getGkGroupClassList();
		if (CollectionUtils.isNotEmpty(gc)) {
			for (NewGkDivideClass gg : gc) {
				if (CollectionUtils.isNotEmpty(gg.getStudentList())) {
					gg.setStudentCount(gg.getStudentList().size());
					arrangeStuId.addAll(gg.getStudentList());
					List<String> stuIdnow = (List<String>) CollectionUtils
							.intersection(gg.getStudentList(),
									stusIds);
					gg.setStuNumBySubList(new ArrayList<>());
					if(isShowCom && CollectionUtils.isNotEmpty(stuIdnow)) {
						Map<String,Integer> map1=makeStuNumBySubjectIds(stuIdnow,stuChooseMap);
						for(Entry<String, Integer> kk:map1.entrySet()) {
							gg.getStuNumBySubList().add(nameSet(courseMap,kk.getKey())+"-"+kk.getValue());
						}
					}
					//学生选课数据与班级不匹配
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
		return flag;
	}

	private Map<String, Integer> makeStuNumBySubjectIds(List<String> stuIdnow, Map<String, List<String>> stuChooseMap) {
		Map<String, Integer> returnMap=new HashMap<>();
		for(String s:stuIdnow) {
			List<String> aa = stuChooseMap.get(s);
			if(CollectionUtils.isNotEmpty(aa)) {
				Set<String> set=new HashSet<>();
				set.addAll(aa);
				String ids=keySort(set);
				if(returnMap.containsKey(ids)) {
					returnMap.put(ids, returnMap.get(ids)+1);
				}else {
					returnMap.put(ids, 1);
				}
			}
		}
		return returnMap;
	}
	
	@RequestMapping("/quickOpenClass/page")
	@ControllerInfo(value = "进入快捷设置")
	public String quickOpenClass(@PathVariable String divideId,String subjectIds,String type,ModelMap map) {
		map.put("divideId", divideId);
		String[] subjectIdArr = subjectIds.split(",");
		List<NewGkDivideStusub> list =new ArrayList<>();
		//已经安排的学生
		List<String> arrangeStuList = newGkClassStudentService.findArrangeStudentIdWithMaster(divideId, NewGkElectiveConstant.CLASS_TYPE_0);
		if(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(type) || NewGkElectiveConstant.SUBJTCT_TYPE_1.equals(type)) {
			//1:总体年级平均人数
			NewGkDivide divide = newGkDivideService.findOneWithMaster(divideId);
			int avg = findAvgByGradeId(divide.getGradeId());
			map.put("avg", avg);
			//查询未安排的3科组合 单科组合 
			list = newGkDivideStusubService.findByDivideIdWithMaster(divideId, NewGkElectiveConstant.SUBJECT_TYPE_A, subjectIdArr);
			map.put("subjectType", type);
			map.put("subjectIds", subjectIds);
			List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdArr), Course.class);
			Map<String, Course> courseMap = EntityUtils.getMap(courseList, e->e.getId());
			String groupName="";
			if(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(type)) {
				groupName=nameSet(courseMap, subjectIds);
			}else {
				groupName=courseMap.get(subjectIds).getSubjectName();
			}
			map.put("groupName", groupName);
			map.put("allSize", list.size());
			int arrangeSize=0;
			if(CollectionUtils.isNotEmpty(arrangeStuList)) {
				Set<String> ids = EntityUtils.getSet(list,e->e.getStudentId());
				arrangeSize=list.size()-CollectionUtils.intersection(ids, arrangeStuList).size();
			}else {
				arrangeSize=list.size();
				
			}
			int defaultClassNum=(arrangeSize-1)/avg+1;
			map.put("defaultClassNum", defaultClassNum);
			map.put("arrangeSize", arrangeSize);
			map.put("subjectType", type);
			
			return "/newgkelective/divideAuto/quickXzb3.ftl";
			
		}else{
			//混合与2+x一样
			if(!NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(type)) {
				subjectIdArr=null;
				type=NewGkElectiveConstant.SUBJTCT_TYPE_0;
				subjectIds=BaseConstants.ZERO_GUID;
			}
			list = newGkDivideStusubService.findByDivideIdWithMaster(divideId, NewGkElectiveConstant.SUBJECT_TYPE_A, subjectIdArr);
			//value:未安排 总人数
			Map<String,Integer[]> stuChooseMap=new HashMap<>();
			Set<String> subIds=new HashSet<>();
			int left=0;
			for(NewGkDivideStusub l:list) {
				Integer[] arr = stuChooseMap.get(l.getSubjectIds());
				if(arr==null) {
					arr=new Integer[] {0,0};
					stuChooseMap.put(l.getSubjectIds(), arr);
					subIds.addAll(Arrays.asList(l.getSubjectIds().split(",")));
				}
				arr[1]=arr[1]+1;
				if(!arrangeStuList.contains(l.getStudentId())) {
					arr[0]=arr[0]+1;
					left++;
				}
			}
			map.put("allSize", list.size());
			map.put("arrangeSize", left);
			List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[0])), Course.class);
			Map<String, Course> courseMap = EntityUtils.getMap(courseList, e->e.getId());
			List<NewGkGroupDto> groupDtoList=new ArrayList<>();
			NewGkGroupDto groupDto=null;
			for(Entry<String, Integer[]> item:stuChooseMap.entrySet()) {
				if(item.getValue()[0]==0) {
					continue;
				}
				groupDto=new NewGkGroupDto();
				groupDto.setAllNumber(item.getValue()[1]);
				groupDto.setLeftNumber(item.getValue()[0]);
				groupDto.setSubjectIds(item.getKey());
				groupDto.setConditionName(nameSet(courseMap, item.getKey()));
				groupDtoList.add(groupDto);
			}
			//根据剩余人数排序
			if(CollectionUtils.isNotEmpty(groupDtoList)) {
				SortUtils.DESC(groupDtoList, "leftNumber");
			}
			map.put("groupDtoList", groupDtoList);
			String groupName;
			if(NewGkElectiveConstant.SUBJTCT_TYPE_0.equals(type)) {
				groupName = "混合";
			}else {
				groupName = nameSet(courseMap, subjectIds);
			}
			map.put("group", groupName);
			groupName=groupName+findNewClassName(getLoginInfo().getUnitId(), divideId, groupName)+"班";
			map.put("subjectIds", subjectIds);
			map.put("groupName", groupName);
			map.put("subjectType", type);
			return "/newgkelective/divideAuto/quickXzb2.ftl";
		}
		
	}
	
	private String makeGroupName(String subIds) {
		String groupName = "";
		if (BaseConstants.ZERO_GUID.equals(subIds)) {
			groupName = "混合";
		} else {
			List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subIds.split(",")),
					new TR<List<Course>>() {});
			Map<String, Course> courseMap = EntityUtils.getMap(courseList, e->e.getId());
			groupName = nameSet(courseMap, subIds);
		}
		return groupName;
	}
	@ResponseBody
	@RequestMapping("/saveQuickOpen")
	@ControllerInfo(value = "快捷分班保存")
	public String saveQuickOpen(@PathVariable String divideId,NewGkQuickSaveDto saveDto) {
		List<NewGkDivideClass> insertClassList=new ArrayList<>();
		List<NewGkClassStudent> insertStuList=new ArrayList<>();
		String unitId=getLoginInfo().getUnitId();
		String subIds = saveDto.getSubjectIds();
		
		NewGkDivide divide = newGkDivideService.findOneWithMaster(divideId);
		if(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(saveDto.getSubjectType()) || NewGkElectiveConstant.SUBJTCT_TYPE_1.equals(saveDto.getSubjectType())) {
			String groupName = makeGroupName(subIds);
			int index=findNewClassName(unitId, divideId, groupName);
			String[] arr = subIds.split(",");
			//剩余未安排的学生
			List<StudentResultDto> leftList = leftStudent(divideId, arr);
			if(CollectionUtils.isNotEmpty(leftList)) {
//				 0:无依据
//				 1:按该选科成绩排名  ---3+0 1+x----2+x
//				 2:按该选科与语数英成绩之和排名---3+0 1+x----2+x
				if("1".equals(saveDto.getOpenBasis()) || "2".equals(saveDto.getOpenBasis())) {
					makeStudentSubjectScore(divide.getReferScoreId(), leftList);
					if("1".equals(saveDto.getOpenBasis())) {
						makeScoreByOpenBasic(leftList, subIds, false);
					}else {
						makeScoreByOpenBasic(leftList, subIds, true);
					}
					onlysortStuScore(leftList);
					//根据moveList 排序
					if("1".equals(saveDto.getBasisType())) {
						//按顺序
						//根据成绩排序
						makeClassesBySort(leftList, divideId, unitId, index, groupName, saveDto, insertClassList, insertStuList);
					}else {
						int classNum=saveDto.getClassNum();
						int stuNum=saveDto.getArrangeStudentNum();
						List<StudentResultDto> moveList=new ArrayList<>();
						if(stuNum <=leftList.size()) {
							moveList=leftList.subList(0, stuNum);
						}else {
							moveList=leftList;
						}
						// 学生(每班学生)
						List<String>[] array = new List[classNum];
						for (int i = 0; i < classNum; i++) {
							array[i] = new ArrayList<String>();
						}
						//交叉 加上性别
						openClassBySex(classNum, moveList, array);
						
						for(List<String> ss:array) {
							NewGkDivideClass clazz = initNewGkDivideClass(divideId, saveDto.getSubjectIds(), NewGkElectiveConstant.CLASS_TYPE_0);
							clazz.setSubjectType(saveDto.getSubjectType());
							clazz.setClassName(groupName+index+"班");
							clazz.setOrderId(index);//同种类型排序
							insertClassList.add(clazz);
							index++;
							for(String s:ss) {
								NewGkClassStudent su = initClassStudent(unitId, divideId, clazz.getId(), s);
								insertStuList.add(su);
							}
						}
					}
				}else {
					makeClassesBySort(leftList, divideId, unitId, index, groupName, saveDto, insertClassList, insertStuList);
				}
				
				
			}else {
				return error("没有未安排的学生，请刷新后操作");
			}
			
		}else {
			if(StringUtils.isBlank(saveDto.getClassName())) {
				return error("班级名称不能为空！");
			}
			//判断名称是不是重复
			List<NewGkDivideClass> groupClassList = newGkDivideClassService
			.findClassBySubjectIds(unitId,
					divideId,
					NewGkElectiveConstant.CLASS_SOURCE_TYPE1,NewGkElectiveConstant.CLASS_TYPE_0,null, false);
			if (CollectionUtils.isNotEmpty(groupClassList)) {
				List<String> groupNameList = EntityUtils.getList(
						groupClassList, NewGkDivideClass::getClassName);
				if (groupNameList.contains(saveDto.getClassName())) {
					return error("班级名称重复！");
				}
			}
			//混合 2+x
			String[] arr =null;
			if(NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(saveDto.getSubjectType())){
				arr = subIds.split(",");
				//剩余未安排的学生
			}
			List<StudentResultDto> leftList = leftStudent(divideId, arr);
			if(CollectionUtils.isEmpty(leftList)) {
				return error("没有未安排的学生，请刷新后操作");
			}
			if("1".equals(saveDto.getOpenBasis()) || "2".equals(saveDto.getOpenBasis())) {
				makeStudentSubjectScore(divide.getReferScoreId(), leftList);
				if("1".equals(saveDto.getOpenBasis())) {
					makeScoreByOpenBasic(leftList, subIds, false);
				}else {
					makeScoreByOpenBasic(leftList, subIds, true);
				}
				//根据成绩排序
				onlysortStuScore(leftList);
			}
			Map<String, List<StudentResultDto>> mapList = EntityUtils.getListMap(leftList,StudentResultDto::getChooseSubjects, e->e);
			List<NewGkQuickGroupDto> dtoList = saveDto.getDtoList();
			List<StudentResultDto> moveList=new ArrayList<>();
			if(CollectionUtils.isNotEmpty(dtoList)) {
				for(NewGkQuickGroupDto dto:dtoList) {
					if(dto.getChooseNum()>0) {
						List<StudentResultDto> l1 = mapList.get(dto.getSubjectIds());
						if(CollectionUtils.isNotEmpty(l1)) {
							if(l1.size()>dto.getChooseNum()) {
								moveList.addAll(l1.subList(0, dto.getChooseNum()));
							}else {
								moveList.addAll(l1);
							}
						}
					}
				}
			}
			if(CollectionUtils.isNotEmpty(moveList)) {
				NewGkDivideClass clazz = initNewGkDivideClass(divideId, saveDto.getSubjectIds(), NewGkElectiveConstant.CLASS_TYPE_0);
				clazz.setSubjectType(saveDto.getSubjectType());
				clazz.setClassName(saveDto.getClassName());
				//从名称中获取数字
				Integer order = subNum(clazz.getClassName());
				if(order!=null) {
					clazz.setOrderId(order);
				}
				insertClassList.add(clazz);
				for(StudentResultDto s:moveList) {
					NewGkClassStudent su = initClassStudent(unitId, divideId, clazz.getId(), s.getStudentId());
					insertStuList.add(su);
				}
			}else {
				return error("没有找到未安排的对应学生，请刷新后操作");
			}
		}
		try {
			if(CollectionUtils.isNotEmpty(insertClassList)) {
				newGkDivideClassService.saveAllList(null, insertClassList, insertStuList);
			}
		}catch (Exception e) {
			return error("保存失败！");
		}
		
		return success("保存成功！");
	}
	
	private void makeClassesBySort(List<StudentResultDto> leftList,
			String divideId,String unitId,int index,String groupName,
			NewGkQuickSaveDto saveDto,
			List<NewGkDivideClass> insertClassList,List<NewGkClassStudent> insertStuList) {
		int classNum=saveDto.getClassNum();
		int stuNum=saveDto.getArrangeStudentNum();
		List<StudentResultDto> moveList=new ArrayList<>();
		if(stuNum <=leftList.size()) {
			moveList=leftList.subList(0, stuNum);
		}else {
			moveList=leftList;
		}
		
		int n1=moveList.size()/classNum;
		int n2=moveList.size()%classNum;
		if(n1==0) {
			//直接安排一个班 moveList
			NewGkDivideClass clazz = initNewGkDivideClass(divideId, saveDto.getSubjectIds(), NewGkElectiveConstant.CLASS_TYPE_0);
			clazz.setSubjectType(saveDto.getSubjectType());
			clazz.setClassName(groupName+index+"班");
			clazz.setOrderId(index);
			insertClassList.add(clazz);
			index++;
			for(StudentResultDto dd:moveList) {
				NewGkClassStudent ss = initClassStudent(unitId, divideId, clazz.getId(), dd.getStudentId());
				insertStuList.add(ss);
			}
		}else {
			int start=0;
			for(int i=0;i<classNum;i++) {
				List<StudentResultDto> chooseList=new ArrayList<>();
				if(n2>0) {
					chooseList = moveList.subList(start, start+n1+1);
					start=start+n1+1;
					n2--;
					
				}else {
					chooseList = moveList.subList(start, start+n1);
					start=start+n1;
					n2--;
				}
				//新增一个班
				NewGkDivideClass clazz = initNewGkDivideClass(divideId, saveDto.getSubjectIds(), NewGkElectiveConstant.CLASS_TYPE_0);
				clazz.setSubjectType(saveDto.getSubjectType());
				clazz.setClassName(groupName+index+"班");
				clazz.setOrderId(index);
				insertClassList.add(clazz);
				index++;
				for(StudentResultDto dd:chooseList) {
					NewGkClassStudent ss = initClassStudent(unitId, divideId, clazz.getId(), dd.getStudentId());
					insertStuList.add(ss);
				}
			}
		}
	}
	
	private void makeScoreByOpenBasic(List<StudentResultDto> list,String subjectIds,boolean isContatinYsy) {
		for(StudentResultDto dd:list) {
			//score切换成按分班要求的参数
			float s=0.0f;
			Map<String, Float> scoreMap = dd.getSubjectScore();
			for(Entry<String, Float> item:scoreMap.entrySet()) {
				if(subjectIds.indexOf(item.getKey())>-1) {
					if(item.getValue()!=null) {
						s=s+item.getValue();
					}
				}else if(isContatinYsy && NewGkElectiveConstant.YSY_SUBID.equals(item.getKey())){
					if(item.getValue()!=null) {
						s=s+item.getValue();
					}
				}
			}
			dd.setScore(s);
		}
	}
	
	
	
	private List<StudentResultDto> leftStudent(String divideId,String[] subjectIds){
		List<NewGkDivideStusub> noArrangeList = newGkDivideStusubService.findNoArrangeXzbStudentWithMaster(divideId, subjectIds);
		List<StudentResultDto> alllist=new ArrayList<>();
		if(CollectionUtils.isNotEmpty(noArrangeList)) {
			StudentResultDto dto=null;
			for(NewGkDivideStusub s:noArrangeList) {
				dto=new StudentResultDto();
				dto.setChooseSubjects(s.getSubjectIds());
				dto.setClassName(s.getClassName());
				dto.setClassId(s.getClassId());
				dto.setStudentName(s.getStudentName());
				dto.setStudentId(s.getStudentId());
				dto.setSex(s.getStudentSex());
				alllist.add(dto);
			}
		}
		return alllist;
	}
	
	
	@RequestMapping("/scheduleIndex")
	@ControllerInfo(value = "进入学生分班列表")
	public String scheduleIndex(@PathVariable String divideId,String subjectIds,String groupClassId,ModelMap map) {
		NewGkDivide divide = newGkDivideService.findOneWithMaster(divideId);
		List<Course> courseList=findByChoice(divide.getUnitId(), divide.getChoiceId());
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, e->e.getId());
		String unitId=getLoginInfo().getUnitId();
		List<NewGkDivideClass> oldList = newGkDivideClassService.findByDivideIdAndClassTypeWithMaster(unitId, divideId, 
				new String[] {NewGkElectiveConstant.CLASS_TYPE_0}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		Map<String,NewGkDivideClass> groupClassMap=new HashMap<>();
		List<String[]> classList=new ArrayList<>();
		NewGkDivideClass chooseClass=null;
		Set<String> arrangeStuIds=new HashSet<>();
		if(CollectionUtils.isNotEmpty(oldList)) {
			for(NewGkDivideClass n:oldList) {
				if(n.getSubjectIds().equals(subjectIds)) {
					classList.add(new String[] {n.getId(),n.getClassName()});
				}
				if(CollectionUtils.isNotEmpty(n.getStudentList())) {
					arrangeStuIds.addAll(n.getStudentList());
				}
				groupClassMap.put(n.getId(), n);
			}
			if(StringUtils.isNotBlank(groupClassId)) {
				chooseClass=groupClassMap.get(groupClassId);
			}
		}
		map.put("classList", classList);
		String[] subjectArr=null;
		String subjectNames="";
		List<String[]> showSubjectName=new ArrayList<>();
		List<String> showIds = new ArrayList<>();
		if(BaseConstants.ZERO_GUID.equals(subjectIds)) {
			//混合
			subjectNames="混合";
		}else {
			subjectArr=subjectIds.split(",");
			subjectNames=nameSet(courseMap, subjectIds);
			for(String s:subjectArr) {
				showSubjectName.add(new String[] {s,courseMap.get(s).getSubjectName()});
				showIds.add(s);
			}
		}
		showSubjectName.add(new String[] {NewGkElectiveConstant.YSY_SUBID,NewGkElectiveConstant.YSY_SUBNAME});
		showIds.add(NewGkElectiveConstant.YSY_SUBID);
		map.put("subjectIds", subjectIds);
		map.put("subjectNames", subjectNames);
		map.put("showSubjectName", showSubjectName);
		
		List<StudentResultDto> allList = findStudentList(divideId, subjectArr,null,courseMap);
				
		List<StudentResultDto> leftStuDtoList=new ArrayList<>();
		Map<String,String> classMap=new LinkedHashMap<>(); 
		Map<String,Integer> classNumMap=new HashMap<>();
		Map<String,String> chooseSubjectMap=new LinkedHashMap<>();
		Map<String,Integer> subNumMap=new HashMap<>();
		int noArrangeStuNums=0;
		if(CollectionUtils.isNotEmpty(allList)) {
			for(StudentResultDto s:allList) {
				if(!arrangeStuIds.contains(s.getStudentId())) {
					s.setChoResultStr(nameSet(courseMap, s.getChooseSubjects()));
					if(!classMap.containsKey(s.getClassId())) {
						classMap.put(s.getClassId(), s.getClassName());
						classNumMap.put(s.getClassId(), 1);
					}else {
						classNumMap.put(s.getClassId(), classNumMap.get(s.getClassId())+1);
					}
					if(!chooseSubjectMap.containsKey(s.getChooseSubjects())) {
						chooseSubjectMap.put(s.getChooseSubjects(), s.getChoResultStr());
						subNumMap.put(s.getChooseSubjects(), 1);
					}else {
						subNumMap.put(s.getChooseSubjects(), subNumMap.get(s.getChooseSubjects())+1);
					}
					noArrangeStuNums++;
					leftStuDtoList.add(s);
				}
			}
		}
		
		List<String[]> classFilterList=new ArrayList<>();
		classFilterList.add(new String[] {"","全部",String.valueOf(noArrangeStuNums)});
		if(classMap.size()>0) {
			for(Entry<String, String> ii:classMap.entrySet()) {
				classFilterList.add(new String[] {ii.getKey(),ii.getValue(),String.valueOf(classNumMap.get(ii.getKey()))});
			}
		}
		List<String[]> subjectFilterList=new ArrayList<>();
		subjectFilterList.add(new String[] {"","全部",String.valueOf(noArrangeStuNums)});
		if(chooseSubjectMap.size()>0) {
			for(Entry<String, String> ii:chooseSubjectMap.entrySet()) {
				subjectFilterList.add(new String[] {ii.getKey(),ii.getValue(),String.valueOf(subNumMap.get(ii.getKey()))});
			}
		}
		if(CollectionUtils.isNotEmpty(leftStuDtoList)){
			String refScore=divide.getReferScoreId();
			if(StringUtils.isBlank(refScore)) {
				//默认参数
				refScore=newGkReferScoreService.findDefaultIdByGradeId(unitId, divide.getGradeId());
			}
			makeStudentSubjectScore(refScore, leftStuDtoList);
		}
		map.put("classFilterList", classFilterList);
		map.put("subjectFilterList", subjectFilterList);
		
		map.put("leftStuDtoList", leftStuDtoList);
		
		if(chooseClass!=null) {
			map.put("groupClassId", chooseClass.getId());
		}
		
		countTableHead(leftStuDtoList, showIds,map);
		
		
		
		return "/newgkelective/divideAuto/openXzbHead.ftl";
	}
	
	private void countTableHead(List<StudentResultDto> leftStuDtoList,List<String> showIds,ModelMap map) {
		int manNum=0;
		int womanNum=0;
		//计算平均分 showIds
		Map<String,Double> allScore=new HashMap<>();
		
		for(StudentResultDto l:leftStuDtoList) {
			if("男".equals(l.getSex())) {
				manNum++;
			}else {
				womanNum++;
			}
			Map<String, Float> scoreMap = l.getSubjectScore();
			if(MapUtils.isEmpty(scoreMap)) {
				continue;
			}
			for(Entry<String, Float> item:scoreMap.entrySet()) {
				if(showIds.contains(item.getKey())) {
					float score = item.getValue()==null?0.0f:item.getValue();
					if(!allScore.containsKey(item.getKey())) {
						allScore.put(item.getKey(), (double) score);
					}else {
						allScore.put(item.getKey(), allScore.get(item.getKey())+score);
					}
				}
			}
		}
		int size=leftStuDtoList.size();
		for(String s:showIds) {
			if(!allScore.containsKey(s) || size==0) {
				allScore.put(s, 0.0);
			}else {
				allScore.put(s, allScore.get(s)/size);
			}
		}
		map.put("allScore", allScore);
		map.put("manNum", manNum);
		map.put("womanNum", womanNum);
	}

	private List<StudentResultDto> findStudentList(String divideId, String[] subjectArr,String[] stuIds,Map<String, Course> courseMap){
		//查询选中--subjectIds
		List<StudentResultDto> alllist=new ArrayList<>();
		List<NewGkDivideStusub> list=new ArrayList<>();
		if(stuIds==null) {
			list=newGkDivideStusubService.findByDivideIdWithMaster(divideId,NewGkElectiveConstant.SUBJECT_TYPE_A,subjectArr);
		}else {
			list=newGkDivideStusubService.findListByStudentIdsWithMaster(divideId,NewGkElectiveConstant.SUBJECT_TYPE_A,stuIds);
		}
		if(CollectionUtils.isNotEmpty(list)) {
			StudentResultDto dto=null;
			for(NewGkDivideStusub s:list) {
				dto=new StudentResultDto();
				dto.setChooseSubjects(s.getSubjectIds());
				dto.setClassName(s.getClassName());
				dto.setClassId(s.getClassId());
				dto.setStudentName(s.getStudentName());
				dto.setStudentId(s.getStudentId());
				dto.setSex(s.getStudentSex());
				dto.setChoResultStr(nameSet(courseMap, s.getSubjectIds()));
				alllist.add(dto);
			}
		}
		return alllist;
	}
	
	@RequestMapping("/loadRightList")
	@ControllerInfo(value = "加载右边学生")
	public String loadRightList(@PathVariable String divideId,String groupClassId,String type,ModelMap map) {
		String unitId=getLoginInfo().getUnitId();
		NewGkDivideClass groupClazz = newGkDivideClassService.findOne(groupClassId);
		NewGkDivide divide = newGkDivideService.findOneWithMaster(divideId);
		List<Course> courseList=findByChoice(divide.getUnitId(), divide.getChoiceId());
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, e->e.getId());
		
		//学生
		List<NewGkDivideClass> gList=new ArrayList<>();
		gList.add(groupClazz);
		newGkDivideClassService.toMakeStudentList(unitId, divideId, gList);
		List<StudentResultDto> rightStuDtoList=new ArrayList<>();
		if(CollectionUtils.isNotEmpty(groupClazz.getStudentList())) {
			rightStuDtoList=findStudentList(groupClazz.getDivideId(), null, groupClazz.getStudentList().toArray(new String[0]),courseMap);
			String refScore=divide.getReferScoreId();
			if(StringUtils.isBlank(refScore)) {
				//默认参数
				refScore=newGkReferScoreService.findDefaultIdByGradeId(unitId, divide.getGradeId());
			}
			makeStudentSubjectScore(refScore, rightStuDtoList);
			
		}
		
		List<String[]> showSubjectName=new ArrayList<>();
		List<String> showIds=new ArrayList<>();
		if(BaseConstants.ZERO_GUID.equals(groupClazz.getSubjectIds())) {
			//混合
		}else {
			String[] subjectArr = groupClazz.getSubjectIds().split(",");
			for(String s:subjectArr) {
				showSubjectName.add(new String[] {s,courseMap.get(s).getSubjectName()});
				showIds.add(s);
			}
		}
		showSubjectName.add(new String[] {NewGkElectiveConstant.YSY_SUBID,NewGkElectiveConstant.YSY_SUBNAME});
		showIds.add(NewGkElectiveConstant.YSY_SUBID);
		map.put("showSubjectName", showSubjectName);
		
		map.put("rightStuDtoList", rightStuDtoList);
		countTableHead(rightStuDtoList, showIds,map);
		if("1".equals(type)) {
			//查看
			return "/newgkelective/divideAuto/openXzbDetailList.ftl";
		}
		return "/newgkelective/divideAuto/openXzbRight.ftl";
	}
	
	
	private List<Course> findByChoice(String unitId,String choiceId){
		List<String> chooseList = newGkChoRelationService.findByChoiceIdAndObjectType(unitId, 
				choiceId, NewGkElectiveConstant.CHOICE_TYPE_01);
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(chooseList.toArray(new String[] {})), Course.class);
		
		return courseList;
	}
	@RequestMapping("/newClass/page")
	@ControllerInfo(value = "新增班级")
	public String newClassName(@PathVariable String divideId,String subjectIds,String stuIdStr,ModelMap map) {
		NewGkDivide newDivide = newGkDivideService.findOneWithMaster(divideId);
		if (newDivide == null) {
			return errorFtl(map, "分班方案不存在");
		}
		map.put("divideId", divideId);

		
		String groupName = "";
		if (BaseConstants.ZERO_GUID.equals(subjectIds)) {
			groupName = "混合";
		} else {
			List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.split(",")),
					new TR<List<Course>>() {});
			Map<String, Course> courseMap = EntityUtils.getMap(courseList, e->e.getId());
			groupName = nameSet(courseMap, subjectIds);
		}
		NewGkDivideClass newGkDivideClass = new NewGkDivideClass();
		newGkDivideClass.setSubjectIds(subjectIds);
		groupName=groupName+findNewClassName(getLoginInfo().getUnitId(), divideId, groupName)+"班";
		newGkDivideClass.setClassName(groupName );
		map.put("newGkDivideClass", newGkDivideClass);
		map.put("subjectIds", subjectIds);
		map.put("divideId", divideId);
		map.put("stuIdStr", stuIdStr);
		return "/newgkelective/divideAuto/openXzbEdit.ftl";
	}
	
	private int findNewClassName(String unitId,String divideId,String namePrex) {
		String groupName = namePrex;
		int k = 1;
		List<NewGkDivideClass> groupClassList = newGkDivideClassService
				.findClassBySubjectIds(unitId,divideId,
						NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_0, null, false);
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
		return  k ;
	}
	@ResponseBody
	@RequestMapping("/saveOneClass")
	@ControllerInfo(value = "保存新增班级")
	public String saveOneClass(@PathVariable String divideId,NewGkDivideClass newGkDivideClass) {
		NewGkDivide newDivide = newGkDivideService.findOneWithMaster(divideId);
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
						newGkDivideClass.getClassType(), null, false);
		if (CollectionUtils.isNotEmpty(groupClassList)) {
			List<String> groupNameList = EntityUtils.getList(
					groupClassList, NewGkDivideClass::getClassName);
			if (groupNameList.contains(newGkDivideClass.getClassName())) {
				return error("班级名称重复！");
			}
		}
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
				newGkDivideClass
				.setSubjectType(NewGkElectiveConstant.SUBJTCT_TYPE_1);
			}
		}
		newGkDivideClass.setDivideId(divideId);
		newGkDivideClass.setCreationTime(new Date());
		newGkDivideClass.setModifyTime(new Date());
		newGkDivideClass.setIsHand(NewGkElectiveConstant.IS_HAND_1);
		newGkDivideClass
				.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
		Integer orderId = subNum(newGkDivideClass.getClassName());
		if(orderId!=null) {
			newGkDivideClass.setOrderId(orderId);
		}
		
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
		return success(newGkDivideClass.getId());
		
	}
	
	@ResponseBody
	@RequestMapping("/groupClassSaveStu")
	@ControllerInfo(value = "保存组合班级学生")
	public String groupClassSaveStu(@PathVariable String divideId,
			String groupClassId, String stuId, ModelMap map) {
		try {
			NewGkDivide newDivide = newGkDivideService.findOneWithMaster(divideId);
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
				List<NewGkClassStudent> insertStudentList = new ArrayList<NewGkClassStudent>();
				if(StringUtils.isNotBlank(stuId)) {
					String[] arrs = stuId.split(",");
					NewGkClassStudent gkGroupClassStu;
					for (String s : arrs) {
						gkGroupClassStu = initClassStudent(newDivide.getUnitId(), newDivide.getId(), groupClassId, s);
						insertStudentList.add(gkGroupClassStu);
					}
				}
				
				newGkDivideClassService.saveStuList(groupClassId,insertStudentList);
			} else {
				return error("该班级不存在！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！");
		}
		return success(groupClassId);
	}
	
	
	@RequestMapping("/editClass/page")
	@ControllerInfo(value = "编辑")
	public String editClass(@PathVariable String divideId,String subjectIds,ModelMap map) {
		String groupName="";
		String[] subjectIdArr=null;
		List<Course> courseList =SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()),
				new TR<List<Course>>() {});
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, e->e.getId());
		if(BaseConstants.ZERO_GUID.equals(subjectIds)) {
			groupName="混合";
		}else if(subjectIds.indexOf(",")>0 || BaseConstants.ZERO_GUID.equals(subjectIds)) {
			groupName = nameSet(courseMap, subjectIds);
			subjectIdArr=subjectIds.split(",");
		}else {
			groupName=courseMap.get(subjectIds)==null?"":courseMap.get(subjectIds).getSubjectName();
			subjectIdArr=new String[] {subjectIds};
		}
		
		map.put("groupName", groupName);
		List<NewGkDivideClass> clazzList = newGkDivideClassService.findClassBySubjectIdsWithMaster(getLoginInfo().getUnitId(), divideId,NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_0, subjectIds, true);
		//组装数据
		List<NewGkDivideStusub> chooselist = newGkDivideStusubService.findByDivideIdWithMaster(divideId, NewGkElectiveConstant.SUBJECT_TYPE_A, subjectIdArr);
		Map<String,String> stuMap=EntityUtils.getMap(chooselist, NewGkDivideStusub::getStudentId, NewGkDivideStusub::getSubjectIds);
		List<DivideClassEditSaveDto> dtoList=new ArrayList<>();
		DivideClassEditSaveDto dto;
		for(NewGkDivideClass cc:clazzList) {
			dto=new DivideClassEditSaveDto();
			dto.setClassId(cc.getId());
			dto.setClassName(cc.getClassName());
			if(CollectionUtils.isEmpty(cc.getStudentList())) {
				//学生人数为0
				dto.setStuNum(0);
				dtoList.add(dto);
				continue;
			}
			//每种组合人数
			Map<String,Integer> numMap=new HashMap<>();
			for(String s:cc.getStudentList()) {
				if(!stuMap.containsKey(s)) {
					//一般不会进来
					continue;
				}
				if(!numMap.containsKey(stuMap.get(s))) {
					numMap.put(stuMap.get(s), 1);
				}else {
					numMap.put(stuMap.get(s), numMap.get(stuMap.get(s))+1);
				}
			}
			if(numMap.size()==0) {
				//一般不会进来--如果进来 建议删除
				dto.setStuNum(0);
				dtoList.add(dto);
				continue;
			}
			String[] subjectIdsq=new String[numMap.size()];//保留的组合数据
			String[] subGroupName=new String[numMap.size()];
			Integer[] subStuNum=new Integer[numMap.size()];
			int i=0;
			int size=0;
			for(Entry<String, Integer> ii:numMap.entrySet()) {
				subjectIdsq[i]=ii.getKey();
				subStuNum[i]=ii.getValue();
				subGroupName[i]=nameSet(courseMap, ii.getKey());
				i++;
				size=size+ii.getValue();
			}
			dto.setStuNum(size);
			dto.setSubGroupName(subGroupName);
			dto.setSubjectIds(subjectIdsq);
			dto.setSubStuNum(subStuNum);
			dtoList.add(dto);
		}
		
		map.put("dtoList", dtoList);
		return "/newgkelective/divideAuto/openXzbClassEdit.ftl";
	}
	@ResponseBody
	@RequestMapping("/saveClassOrDel")
	@ControllerInfo(value = "保存")
	public String saveClassOrDel(@PathVariable String divideId,String delClassId,NewGkGroupDto dto) {
		//delClassId 防止对其他刚新增的班级进行删除
		List<DivideClassEditSaveDto> dcList = dto.getSaveDto();
		List<NewGkDivideClass> groupClassList = newGkDivideClassService
				.findClassBySubjectIds(getLoginInfo().getUnitId(),divideId,NewGkElectiveConstant.CLASS_SOURCE_TYPE1,
						NewGkElectiveConstant.CLASS_TYPE_0, null, false);
		Set<String> delSet=new HashSet<>();//需要完全删除的班级id
		//学生数据修改
		Set<String> delCStuIds=new HashSet<>();//需要删除的学生数据
		boolean isNoFind=false;
		if(CollectionUtils.isEmpty(groupClassList)) {
			return error("班级数据有调整，请刷新后操作");
		}
		Map<String, NewGkDivideClass> clazzMap = EntityUtils.getMap(groupClassList, e->e.getId());
		if(StringUtils.isNotBlank(delClassId)) {
			String[] delIds = delClassId.split(",");
			for(String s:delIds) {
				if(clazzMap.containsKey(s)) {
					delSet.add(s);
				}else {
					if(!isNoFind) {
						isNoFind=true;
					}
				}
			}
		}
		
		List<NewGkDivideClass> insertUpdateList=new ArrayList<>();//修改名称
		List<DivideClassEditSaveDto> updateNameList=new ArrayList<>();
		List<DivideClassEditSaveDto> updateStuList=new ArrayList<>();
		if(CollectionUtils.isNotEmpty(dcList)) {
			//修改名称--是否重名
			Set<String> updateIds=new HashSet<>();
			for(DivideClassEditSaveDto c:dcList) {
				if(c==null || StringUtils.isBlank(c.getClassId())) {
					continue;
				}
				if(!clazzMap.containsKey(c.getClassId())) {
					if(!isNoFind) {
						isNoFind=true;
					}
					continue;
				}
				if(StringUtils.isBlank(c.getClassName())) {
					return error("班级名称不能为空");
				}
				updateStuList.add(c);
				if(clazzMap.get(c.getClassId()).getClassName().equals(c.getClassName())) {
					//不需要改变
					continue;
				}
				//需要修改名称的班级
				updateNameList.add(c);
				updateIds.add(c.getClassId());
			}
			if(CollectionUtils.isNotEmpty(updateIds)) {
				groupClassList=groupClassList.stream().filter(x -> {
					return !updateIds.contains(x.getId());
				}).collect(Collectors.toList());
				Set<String> sameList=EntityUtils.getSet(groupClassList, e->e.getClassName());
				for(DivideClassEditSaveDto v:updateNameList) {
					if(sameList.contains(v.getClassName().trim())) {
						return  error(v.getClassName()+"名称重复");
					}
					NewGkDivideClass item = clazzMap.get(v.getClassId());
					item.setClassName(v.getClassName().trim());
					item.setModifyTime(new Date());
					Integer ii = subNum(item.getClassName());
					if(ii!=null) {
						item.setOrderId(ii);
					}
					insertUpdateList.add(item);
					sameList.add(item.getClassName());
				}
			}
			
			if(CollectionUtils.isNotEmpty(updateStuList)) {
				List<NewGkDivideStusub> chooselist = newGkDivideStusubService.findByDivideIdWithMaster(divideId, NewGkElectiveConstant.SUBJECT_TYPE_A, null);
				Map<String,String> stuMap=EntityUtils.getMap(chooselist, NewGkDivideStusub::getStudentId, NewGkDivideStusub::getSubjectIds);
				Set<String> classIds = EntityUtils.getSet(updateStuList, e->e.getClassId());
				List<NewGkClassStudent> slist = newGkClassStudentService.findListByClassIds(getLoginInfo().getUnitId(), divideId, classIds.toArray(new String[0]));
				if(CollectionUtils.isNotEmpty(slist)) {
					Map<String, List<NewGkClassStudent>> classStuList = EntityUtils.getListMap(slist, NewGkClassStudent::getClassId, e->e);
					for(DivideClassEditSaveDto c:updateStuList) {
						NewGkDivideClass clazz = clazzMap.get(c.getClassId());
						List<NewGkClassStudent> ll = classStuList.get(clazz.getId());
						//传递的subjectIds都是同样的排序
						//sub 包含null 影响不大
						List<String> sub = Arrays.asList(c.getSubjectIds());
						if(CollectionUtils.isNotEmpty(ll)) {
							for(NewGkClassStudent s:ll) {
								if(!stuMap.containsKey(s.getStudentId())) {
									delCStuIds.add(s.getId());
								}
								if(!sub.contains(stuMap.get(s.getStudentId()))) {
									delCStuIds.add(s.getId());
								}
							}
						}
					}
				}
				
			}
			
			
		}
		String[] ids=null;
		if(CollectionUtils.isNotEmpty(delSet)) {
			ids=delSet.toArray(new String[0]);
		}
		String[] csids=null;
		if(CollectionUtils.isNotEmpty(delCStuIds)) {
			csids=delCStuIds.toArray(new String[0]);
		}
		try {
			newGkDivideClassService.saveClassOrDel(getLoginInfo().getUnitId(),divideId,insertUpdateList,ids,csids);
		}catch (Exception e) {
			e.printStackTrace();
			return error("操作失败");
		}
		return success("操作成功");
	}
	
	
	@ResponseBody
	@RequestMapping("/auto2x")
	@ControllerInfo(value = "保存")
	public String auto2x(@PathVariable String divideId,String type,String noArrIds,int openClassnum) {
		final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_" + divideId;
		final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_"
				+ divideId + "_mess";
		JSONObject on = new JSONObject();
		final NewGkDivide divide = newGkDivideService.findOneWithMaster(divideId);
		
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
			RedisUtils.set(key, "start",TIME_ONE_HOUR);
			RedisUtils.set(keyMess, "进行中",TIME_ONE_HOUR);
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
			if("2".equals(type)) {
				autoSixType2(divide, noArrIds, openClassnum);
			}else {
				autoSixType1(divide, noArrIds, openClassnum);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			on.put("stat", "error");
			on.put("message", "失败");
			RedisUtils.del(new String[] { key,  keyMess });
			return error("失败！" + e.getMessage());
		}

		return JSON.toJSONString(on);
	}
	//完全分班--1.5
	private void  autoSixType1(NewGkDivide divide,String noArrIds,int openClassnum) {
		final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_" + divide.getId();
		final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_"
				+ divide.getId() + "_mess";
		//未安排的学生数据
		List<NewGkDivideStusub> list = newGkDivideStusubService.findNoArrangeXzbStudentWithMaster(divide.getId(), null);
		if(CollectionUtils.isEmpty(list)) {
			RedisUtils.set(key, "error",TIME_ONE_HOUR);
			RedisUtils.set(keyMess, "没有学生需要安排",TIME_ONE_HOUR);
			return;
		}
		if(openClassnum<=0) {
			List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(divide.getUnitId(),divide.getGradeId()), new TR<List<Clazz>>(){});
			openClassnum = clazzList.size();
		}
		//找到已经的组合
		List<NewGkDivideClass> oldlist = newGkDivideClassService
				.findClassBySubjectIds(getLoginInfo().getUnitId(),divide.getId(),NewGkElectiveConstant.CLASS_SOURCE_TYPE1,
						NewGkElectiveConstant.CLASS_TYPE_0, null, false);
		openClassnum=openClassnum-oldlist.size();
		if(openClassnum<=0){
			RedisUtils.set(key, "error",TIME_ONE_HOUR);
			RedisUtils.set(keyMess, "开班数量设置不合适",TIME_ONE_HOUR);
			return;
		}
		
		
		//一般不会存在问题
		List<String> subjectIdsList = newGkChoRelationService.findByChoiceIdAndObjectType(divide.getUnitId(), 
				divide.getChoiceId(), NewGkElectiveConstant.CHOICE_TYPE_01);
		if(CollectionUtils.isEmpty(subjectIdsList )){
			RedisUtils.set(key, "error",TIME_ONE_HOUR);
			RedisUtils.set(keyMess, "选课科目为空，请联系管理员",TIME_ONE_HOUR);
			return;
		}
		
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdsList.toArray(new String[] {})), Course.class);
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
		Set<String> oldClassName=EntityUtils.getSet(oldlist, e->e.getClassName());
		
		//默认选课数量为3
		Map<String, List<String>> listMap = EntityUtils.getListMap(list, e->e.getSubjectIds(), e->e.getStudentId());
		List<List<String>> group3AList=new ArrayList<List<String>>();
		Set<String> chooseSubjectIds=new HashSet<>();
		for (Entry<String, List<String>> item:listMap.entrySet()){
			String[] keys = item.getKey().split(",");
			List<String> keylist1=new ArrayList<String>();
			List<String> keylist = Arrays.asList(keys);
			keylist1.addAll(keylist);
			keylist1.add(item.getValue().size()+"");
			chooseSubjectIds.addAll(keylist);
			group3AList.add(keylist1);
		}
		
		//排除不能组成的2+x 例如有些学校不喜欢物理政治组合--暂时不使用
		List<List<String>> excludedGroup2AList =new ArrayList<List<String>>();
		if(StringUtils.isNotBlank(noArrIds)) {
			String[] arr = noArrIds.split(";");
			for(String s:arr) {
				String[] mm = s.split(",");
				excludedGroup2AList.add(Arrays.asList(mm));
			}
		}
		final int opennum=openClassnum;
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					int bestStuNnum=list.size()/opennum;;//平均人数					
					List<List<String>> resultBest=null;
					double[] s2 = null;//极差 ，方差
					int bestMagin=0;
					//20%浮动人数
					for(int ii=3;ii<=bestStuNnum*0.2;ii++) {
						//多次循环
						//误差范围
						Integer sectionSizeMargin = ii;
						
						Sectioning2A1XInput s2a1xInput=new Sectioning2A1XInput();
						s2a1xInput.setGroup3AList(group3AList);//各选课组合数量
						s2a1xInput.setSectionSizeMean(bestStuNnum);
						s2a1xInput.setSectionSizeMargin(sectionSizeMargin);
						List<List<String>> maxTeacherCountList=new ArrayList<>();
						s2a1xInput.setMaxTeacherCountList(maxTeacherCountList);//由于已经开设班级不考虑，那么老师数量也不考虑
						s2a1xInput.setMaxRoomCount(opennum);//开设班级数量
						List<List<String>> pre1xList =new ArrayList<List<String>>();//2+x中x不考虑
						
					
						s2a1xInput.setPre1XList(pre1xList);
						
						s2a1xInput.setExcludedGroup2AList(excludedGroup2AList);
						
					
						SectioningApp2 app2=new SectioningApp2();
						//<选课1-2A><选课2-2A> <开班数> <选课1-3A><选课2-3A><选课3-3A><人数>
						List<List<String>> result=null;
						try {
							result =app2.makeResult(s2a1xInput,3);
						}catch (Exception e) {
							//这种错误
							if("ERROR: Something error in the result!".equals(e.getMessage())) {
								System.out.println(e.getMessage());
								continue;
							}
							RedisUtils.set(key, "error",TIME_ONE_HOUR);
							RedisUtils.set(keyMess, "自动分配失败",TIME_ONE_HOUR);
							return ;
						}
						
//						if(Evn.isDevModel()) {
//							System.out.println("1.5版本:最佳班级人数："+bestStuNnum+":班级数量："+opennum+":浮动人数:"+ii);
//							System.out.println("最终开设人数："+countOpenClassNum(result));
//							double[] mmArr = findBestResult(result,null);
//							System.out.println("极差："+mmArr[0]);
//							System.out.println("方差："+mmArr[1]);
//						}
						//对于结果比较
						/***************************************/
						//必须按行政班级数量开设
						if(countOpenClassNum(result)!=opennum) {
							continue;
						}
						if(resultBest==null) {
							resultBest=result;
							s2=findBestResult(result,null);
							bestMagin=ii;
							if(s2[0]==0) {
								break;
							}
						}else {
							//判断resultBest 与 result比较
							//1:班级数量==leftXzbNum 3:极差2：每个班级人数浮动比例 方差 
							double[] mmArr = findBestResult(result,null);
							if(mmArr[0]==0) {
								break;
							}
							if(s2[0]>10 ||  mmArr[0]>10) {
								//比较极差
								if( mmArr[0]<s2[0]) {
									resultBest=result;
									s2=mmArr;
									bestMagin=ii;
								}
							}else {
								//比较方差
								if(mmArr[1]<s2[1]) {
									resultBest=result;
									s2=mmArr;
									bestMagin=ii;
								}
							}
							
						}
						/*********************************************/
					}
					if(resultBest==null) {
						RedisUtils.set(key, "error",TIME_ONE_HOUR);
						RedisUtils.set(keyMess, "数据不适合2+x形式",TIME_ONE_HOUR);
						return ;
					}
					if(Evn.isDevModel()) {
						System.out.println("3.0版本最后数据:最佳班级人数："+bestStuNnum+":浮动人数:"+bestMagin);
						System.out.println("极差："+s2[0]);
						System.out.println("方差："+s2[1]);
					}
					saveResult(divide, resultBest, courseMap, listMap, oldClassName,null);
					RedisUtils.set(key, "success",TIME_ONE_HOUR);
					RedisUtils.set(keyMess, "自动分配成功",TIME_ONE_HOUR);
					return ;
				} catch (Exception e) {
					e.printStackTrace();
					RedisUtils.set(key, "error",TIME_ONE_HOUR);
					RedisUtils.set(keyMess, "自动分配失败",TIME_ONE_HOUR);
					return ;
				}
				

			}


		}).start();

	}
	//1.5版本结果 保存
	/**
	 * 
	 * @param gkDivide
	 * @param resultGroup2AList 算法结果
	 * @param courseMap
	 * @param stuIdBySubjectidsMap
	 * @param oldClassName
	 * @param group3Map 3.0版本
	 */
	public void saveResult(NewGkDivide gkDivide,List<List<String>>  resultGroup2AList,Map<String,Course> courseMap,
			Map<String,List<String>> stuIdBySubjectidsMap,Set<String> oldClassName,Map<String, Integer[]> group3Map){
		//key:组合2科id key:组合3科id value:人数
		Map<String,Map<String,Integer>> group=new HashMap<>();
		//key:组合2科id value:开班数量
		Map<String,Integer> groupClassNum=new HashMap<>();
		//key:组合2科id value:总人数
		Map<String,Integer> countGroupClassNum=new HashMap<>();
		
		List<NewGkDivideClass> insertClassList=new ArrayList<NewGkDivideClass>();
		List<NewGkClassStudent> insertStudentList=new ArrayList<NewGkClassStudent>();
		
		Map<String,Integer> nameNumMap=new HashMap<String,Integer>();
		if(group3Map!=null && group3Map.size()>0) {
			//stuIdBySubjectidsMap这个里面获取数据
			for(Entry<String, Integer[]> item:group3Map.entrySet()) {
				String name3=nameSet(courseMap, item.getKey());
				Integer[] vv = item.getValue();
				List<String> list = stuIdBySubjectidsMap.get(item.getKey());
				int toindex=vv[1]>list.size()?list.size():vv[1];
				List<String> list1 = list.subList(0, toindex);
				//开班
				List<Integer> sizeList = CalculateSections.calculateSectionsByKnownCount(list1.size(), vv[0]);
				int from=0;
				for(Integer ii:sizeList) {
					List<String> list2 = list1.subList(from, from+ii);
					//一个班级
					String newName=findNewName(nameNumMap, item.getKey(), name3, oldClassName);
					addClassAndStuToList(list2, gkDivide, item.getKey(), newName, NewGkElectiveConstant.SUBJTCT_TYPE_3, insertClassList, insertStudentList);
					from=from+ii;
				}
				
				
				list.removeAll(list1);
			}
		}
		
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
				List<String> stulist2 = ll.subList(fromIndex, toIndex);
				studentId.put(ii1.getKey(), stulist2);
				stulist.addAll(stulist2);
				indexMap.put(ii1.getKey(), toIndex);
			}
			
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
		}
		
		newGkDivideClassService.saveAllList(gkDivide.getUnitId(), gkDivide.getId(),
				null, insertClassList, insertStudentList, false);
	}
	
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
		divideClass.setIsHand(NewGkElectiveConstant.IS_HAND_0);//自动
		divideClass.setClassName(className);
		divideClass.setSubjectType(subjectType);
		insertClassList.add(divideClass);
		//学生
		for(String stuId:stuList1){
			gkGroupClassStu = initClassStudent(gkDivide.getUnitId(), gkDivide.getId(), divideClass.getId(), stuId);
			
			insertStudentList.add(gkGroupClassStu);
		}
	}
	
	
	//1.5版本结果 获取总开设班级数量
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
	
	//1.5版本结果 每个班级人数方差
	//group3Map:3.0版本
	private double[] findBestResult(List<List<String>> result,Map<String, Integer[]> group3Map) {
		//<选课1-2A><选课2-2A> <开班数> <选课1-3A><选课2-3A><选课3-3A><人数>
		Map<String,Integer> groupClassNum=new HashMap<>();
		Map<String,Integer> countGroupClassNum=new HashMap<>();
		List<Integer> classStuNum=new ArrayList<>();
		if(group3Map!=null && group3Map.size()>0) {
			for(Entry<String, Integer[]> item:group3Map.entrySet()) {
				Integer[] vv = item.getValue();
				classStuNum.addAll(CalculateSections.calculateSectionsByKnownCount(vv[1], vv[0]));
			}
		}
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
		if(Evn.isDevModel()) {
			System.out.println(ArrayUtil.print(classStuNum.toArray(new Integer[0])));
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
	
	//不完全分班--3.0
	private void  autoSixType2(NewGkDivide divide,String noArrIds,int openClassnum) {
		final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_" + divide.getId();
		final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_"
				+ divide.getId() + "_mess";
		//未安排的学生数据
		List<NewGkDivideStusub> list = newGkDivideStusubService.findNoArrangeXzbStudentWithMaster(divide.getId(), null);
		if(CollectionUtils.isEmpty(list)) {
			RedisUtils.set(key, "error",TIME_ONE_HOUR);
			RedisUtils.set(keyMess, "没有剩下学生需要安排",TIME_ONE_HOUR);
			return;
		}
		if(openClassnum<=0) {
			List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(divide.getUnitId(),divide.getGradeId()), new TR<List<Clazz>>(){});
			openClassnum = clazzList.size();
		}
		//找到已经的组合
		List<NewGkDivideClass> oldlist = newGkDivideClassService
				.findClassBySubjectIds(getLoginInfo().getUnitId(),divide.getId(),NewGkElectiveConstant.CLASS_SOURCE_TYPE1,
						NewGkElectiveConstant.CLASS_TYPE_0, null, false);
		openClassnum=openClassnum-oldlist.size();
		if(openClassnum<=0){
			RedisUtils.set(key, "error",TIME_ONE_HOUR);
			RedisUtils.set(keyMess, "开班数量设置不合适",TIME_ONE_HOUR);
			return;
		}
		
		
		//一般不会存在问题
		List<String> subjectIdsList = newGkChoRelationService.findByChoiceIdAndObjectType(divide.getUnitId(), 
				divide.getChoiceId(), NewGkElectiveConstant.CHOICE_TYPE_01);
		if(CollectionUtils.isEmpty(subjectIdsList )){
			RedisUtils.set(key, "error",TIME_ONE_HOUR);
			RedisUtils.set(keyMess, "选课科目为空，请联系管理员",TIME_ONE_HOUR);
			return;
		}
		
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdsList.toArray(new String[] {})), Course.class);
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
		Set<String> oldClassName=EntityUtils.getSet(oldlist, e->e.getClassName());
		
		//默认选课数量为3
		Map<String, List<String>> listMap = EntityUtils.getListMap(list, e->e.getSubjectIds(), e->e.getStudentId());
		List<List<String>> group3AList=new ArrayList<List<String>>();
		Set<String> chooseSubjectIds=new HashSet<>();
		for (Entry<String, List<String>> item:listMap.entrySet()){
			String[] keys = item.getKey().split(",");
			List<String> keylist1=new ArrayList<String>();
			List<String> keylist = Arrays.asList(keys);
			keylist1.addAll(keylist);
			keylist1.add(item.getValue().size()+"");
			chooseSubjectIds.addAll(keylist);
			group3AList.add(keylist1);
		}
		
		//排除不能组成的2+x 例如有些学校不喜欢物理政治组合--暂时不使用
		List<List<String>> excludedGroup2AList =new ArrayList<List<String>>();
		if(StringUtils.isNotBlank(noArrIds)) {
			String[] arr = noArrIds.split(";");
			for(String s:arr) {
				String[] mm = s.split(",");
				excludedGroup2AList.add(Arrays.asList(mm));
			}
		}
		final int opennum=openClassnum;
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					int bestStuNnum=list.size()/opennum;//平均人数					
					List<List<String>> resultBest=null;
					Map<String, Integer[]> bestGroup3Map=new HashMap<>();
					double[] s2 = null;//极差 ，方差
					int leftStuNum=0;
//					int finalOpenNum=0;
					int bestMagin=0;
					//20%浮动人数
					for(int ii=3;ii<=bestStuNnum*0.2;ii++) {
						//多次循环
						//误差范围
						Integer sectionSizeMargin = ii;
						
						net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning3.api.Sectioning2A1XInput s2a1xInput=
									new net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning3.api.Sectioning2A1XInput();
						s2a1xInput.setGroup3AList(group3AList);//各选课组合数量
						s2a1xInput.setSectionSizeMean(bestStuNnum);
						s2a1xInput.setSectionSizeMargin(sectionSizeMargin);
						List<List<String>> maxTeacherCountList=new ArrayList<>();
						s2a1xInput.setMaxTeacherCountList(maxTeacherCountList);//由于已经开设班级不考虑，那么老师数量也不考虑
						s2a1xInput.setMaxRoomCount(opennum);//开设班级数量
						List<List<String>> pre1xList =new ArrayList<List<String>>();//2+x中x不考虑
					
						s2a1xInput.setPre1XList(pre1xList);
						
						s2a1xInput.setExcludedGroup2AList(excludedGroup2AList);
						
						SectioningApp3 app3=new SectioningApp3();
						List<List<String>> sizeList = app3.calculateSectionSize(s2a1xInput, 3);
						Map<String,Integer> sizeBysubject=new HashMap<>();
						for(List<String> s:sizeList) {
							sizeBysubject.put(s.get(0), Integer.parseInt(s.get(1)));
						}
						List<List<String>> newGroupList=new ArrayList<>();
						//varlue 开班数,总人数
						Map<String, Integer[]> group3Map=new HashMap<>();//预开班级
						int preOpen=aloneOpenXzb(group3AList, sizeBysubject, group3Map, newGroupList);
						
						s2a1xInput.setGroup3AList(newGroupList);
						s2a1xInput.setMaxRoomCount(opennum-preOpen);
						
						List<List<String>> sizeList2 = app3.calculateSectionSize(s2a1xInput, 3);
						s2a1xInput.setSectionSizeList(sizeList2);
						
						//预先安排数据 剩余人数超过3倍三科组合平均人数
						
						//<选课1-2A><选课2-2A> <开班数> <选课1-3A><选课2-3A><选课3-3A><人数>
						List<List<String>> result3 =app3.makeResult(s2a1xInput);
						
						//结果中这个数据算法应该不会有 group3Map
						Map<String, Integer> leftMap=new HashMap<>();
						List<List<String>> group2AList=new ArrayList<>();
						//对于结果比较
						/***************************************/
						
						
						//解析
						//剩余人数，开设班级数
						int[] res = analysisResult(result3, group3Map, leftMap, group2AList);
//						if(Evn.isDevModel()) {
//							System.out.println("3.0版本:最佳班级人数："+bestStuNnum+":班级总数量："+opennum+"：班级数量："+s2a1xInput.getMaxRoomCount()+":浮动人数:"+ii);
//							System.out.println("最终开设班级数量："+res[1]+"：预开设数量："+opennum);
//							System.out.println("剩余人数"+res[0]);
//							double[] s3 = findBestResult(group2AList,group3Map);
//							System.out.println("极差："+s3[0]);
//							System.out.println("方差："+s3[1]);
//						}
						//必须按行政班级数量开设
						if(res[1]+preOpen!=opennum) {
							continue;
						}
						
						if(resultBest==null) {
							resultBest=group2AList;
							bestGroup3Map=group3Map;
							s2=findBestResult(group2AList,group3Map);
							leftStuNum=res[0];
							bestMagin=ii;
//							finalOpenNum=res[1]+preOpen;
							if(leftStuNum==0 && s2[0]==0) {
								break;
							}
						}else {
							//0:剩余人数比较--在半数以下
							int half=bestStuNnum/2;
							if(leftStuNum>half && res[0]>half) {
								if(res[0]<leftStuNum) {
									resultBest=group2AList;
									bestGroup3Map=group3Map;
									leftStuNum=res[0];
									s2=findBestResult(group2AList,group3Map);
									break;
								}
							}
							
							//判断resultBest 与 result比较
							//1:班级数量==leftXzbNum 3:极差2：每个班级人数浮动比例 方差 
							double[] mmArr = findBestResult(group2AList,group3Map);
							if(mmArr[0]==0) {
								break;
							}
							if(s2[0]>10 || mmArr[0]>10) {
								//比较极差
								if( mmArr[0]<s2[0]) {
									resultBest=group2AList;
									bestGroup3Map=group3Map;
									leftStuNum=res[0];
									s2=mmArr;
									bestMagin=ii;
//									finalOpenNum=res[1]+preOpen;
								}
							}else {
								//比较方差
								if(mmArr[1]<s2[1]) {
									resultBest=group2AList;
									bestGroup3Map=group3Map;
									leftStuNum=res[0];
									s2=mmArr;
									bestMagin=ii;
//									finalOpenNum=res[1]+preOpen;
								}
							}
							
						}
						/*********************************************/
					}
					if(resultBest==null) {
						RedisUtils.set(key, "error",TIME_ONE_HOUR);
						RedisUtils.set(keyMess, "数据不适合2+x形式",TIME_ONE_HOUR);
						return ;
					}
					if(Evn.isDevModel()) {
						System.out.println("3.0版本最后数据:最佳班级人数："+bestStuNnum+":浮动人数:"+bestMagin);
//						System.out.println("剩余人数"+leftStuNum+":进入算法开班结果："+finalOpenNum);
//						System.out.println("极差："+s2[0]);
//						System.out.println("方差："+s2[1]);
					}
					saveResult(divide, resultBest, courseMap, listMap, oldClassName,bestGroup3Map);
					RedisUtils.set(key, "success",TIME_ONE_HOUR);
					RedisUtils.set(keyMess, "自动分配成功",TIME_ONE_HOUR);
					return ;
				} catch (IOException e) {
					e.printStackTrace();
					RedisUtils.set(key, "error",TIME_ONE_HOUR);
					RedisUtils.set(keyMess, "自动分配失败",TIME_ONE_HOUR);
					return ;
				}catch (Exception e) {
					e.printStackTrace();
					RedisUtils.set(key, "error",TIME_ONE_HOUR);
					RedisUtils.set(keyMess, "自动分配失败",TIME_ONE_HOUR);
					return ;
				}
				

			}


		}).start();
	}
	
	private int aloneOpenXzb(List<List<String>> group3AList,Map<String,Integer> sizeBysubject,
			Map<String, Integer[]> group3Map,List<List<String>> newGroupList) {
		int openNum=0;
		for(List<String> item:group3AList) {
			List<String> chList = item.subList(0, item.size()-1);
			int num = Integer.parseInt(item.get(item.size()-1));
			int k=0;
			for(String j:chList) {
				k=k+sizeBysubject.get(j);//三科平均数之和
			}
			int avg = k/(item.size()-1);
			int allleft=avg*3;
			int alonenum=0;
			if(num>allleft) {
				while(true) {
					if(num-avg>allleft) {
						//开设一个班级
						alonenum++;
						num=num-avg;
					}else {
						break;
					}
				}
			}
			if(alonenum>0) {
				openNum=openNum+alonenum;
				group3Map.put(ArrayUtil.print(chList.toArray(new String[0])), new Integer[] {alonenum,alonenum*avg});
			}
			List<String> l2=new  ArrayList<>();
			l2.addAll(chList);
			l2.add(num+"");
			newGroupList.add(l2);
		}
		return openNum;
	}
	
	/**
	 * @param resultGroup2AList
		 * 1. 2A 开班情况： 		<选课1-2A> <选课2-2A> <开班数>   <总人数> [<选课1-3A><选课2-3A><选课3-3A><人数>]+
		 * 2. 3A 插班生情况：	<选课1-3A> <选课2-3A> <选课3-3A> <人数>---剩余人数
		 * 3. 3A独立开班的情况：	<选课1-3A> <选课2-3A> <选课3-3A> <开班数> <总人数>
	 */
	private int[] analysisResult(List<List<String>> resultGroup2AList,Map<String,Integer[]> group3Map,Map<String,Integer> leftMap,List<List<String>> group2AList) {
		int[] leftNumOrClassNum=new int[] {0,0};
		for (List<String> oneLine : resultGroup2AList) {
			if (oneLine.size() == 5) {//3A独立开班部分
				List<String> choose = oneLine.subList(0, 3);
				String[] arr = choose.toArray(new String[0]);
				Arrays.sort(arr);
				leftNumOrClassNum[1]=leftNumOrClassNum[1]+Integer.parseInt(oneLine.get(3));
				String key=ArrayUtil.print(arr);
				if(group3Map.containsKey(key)) {
					Integer[] vv = group3Map.get(key);
					vv[0]=vv[0]+Integer.parseInt(oneLine.get(3));
					vv[1]=vv[1]+Integer.parseInt(oneLine.get(4));
				}else {
					group3Map.put(key, new Integer[] {Integer.parseInt(oneLine.get(3)),Integer.parseInt(oneLine.get(4))});
				}
			}
			else if (oneLine.size() > 5) { //2A开班部分
				int nums = Integer.parseInt(oneLine.get(2));
				leftNumOrClassNum[1]=leftNumOrClassNum[1]+nums;
				List<String> choose = oneLine.subList(0, 2);
				String[] arr = choose.toArray(new String[0]);
				Arrays.sort(arr);
				List<String> ss=new ArrayList<>();
				ss.add(arr[0]);
				ss.add(arr[1]);
				ss.add(nums+"");
				for (int i = 4; i < oneLine.size(); i += 4) {
					List<String> l1=new ArrayList<>();
					Integer localStudentCount = Integer.parseInt(oneLine.get(i + 3));
					String[] arr1 = new String[] {oneLine.get(i),oneLine.get(i + 1),oneLine.get(i + 2)};
					Arrays.sort(arr1);
					l1.addAll(ss);
					l1.addAll(Arrays.asList(arr1));
					l1.add(localStudentCount+"");
					group2AList.add(l1);
				}
			}
			else {//插班生部分
				Integer localStudentCount = Integer.parseInt(oneLine.get(3));
				if(localStudentCount==0) {
					continue;
				}
				List<String> choose = oneLine.subList(0, 3);
				String[] arr = choose.toArray(new String[0]);
				Arrays.sort(arr);
				leftMap.put(ArrayUtil.print(arr),localStudentCount);
				leftNumOrClassNum[0]=leftNumOrClassNum[0]+localStudentCount;
			}
		}
		return leftNumOrClassNum;
	}
	
	@ResponseBody
	@RequestMapping("/clearOpenNext")
	@ControllerInfo(value = "清除后续步骤数据")
	public String clearOpenNext(@PathVariable String divideId) {
		NewGkDivide divide = newGkDivideService.findOneWithMaster(divideId);
		if (divide == null) {
			return error("分班方案不存在,返回列表页进行操作");
		}
		if(divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_10)) {
			//清除class_type=4
			try {
				newGkDivideService.deleteOpenLeftZhbNext(divide.getUnitId(),divideId);
			}catch (Exception e) {
				return error("操作失败");
			}
			return  success("操作成功");
		}
		if(checkAutoTwo(divideId) || isNowDivide(divideId)){
			//正在智能分班中
			return error("正在分班中，不适合操作");
		}
		try {
			newGkDivideService.deleteOpenXzbNext(divide.getUnitId(),divideId);
		}catch (Exception e) {
			return error("操作失败");
		}
		return  success("操作成功");
	}
	@ResponseBody
	@RequestMapping("/clearNotPerArrange")
	@ControllerInfo(value = "异常处理")
	public String clearNotPerArrange(@PathVariable String divideId) {
		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		// 已有数据
		List<NewGkDivideClass> groupClassList = newGkDivideClassService
				.findByDivideIdAndClassType(newDivide.getUnitId(),
						divideId,
						new String[] { NewGkElectiveConstant.CLASS_TYPE_0 }, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		if (CollectionUtils.isEmpty(groupClassList)) {
			return success("操作成功！");
		}
		List<NewGkDivideStusub> list = newGkDivideStusubService.findByDivideIdWithMaster(divideId, NewGkElectiveConstant.SUBJECT_TYPE_A, null);
		Set<String> delClassIds = new HashSet<String>();
		List<NewGkDivideClass> updateGroup = new ArrayList<NewGkDivideClass>();
		if (CollectionUtils.isEmpty(list)) {
			// 学生选课数据不存在 删除所有组合信息
			delClassIds = EntityUtils.getSet(groupClassList, NewGkDivideClass::getId);
			newGkDivideClassService.deleteByClassIdIn(newDivide.getUnitId(),divideId, delClassIds
					.toArray(new String[] {}));
		} else {
			Map<String,Set<String>> stuBysubIds=new HashMap<>();
			//一个人员只能去向一个班级，多个班级默认随机放一个--这个暂时不过滤 人工去移除
			List<String> groupStudent;
			Set<String> emplySubjectIds=new HashSet<>();
			for (NewGkDivideClass groupClass : groupClassList) {
				groupStudent = groupClass.getStudentList();
				if(CollectionUtils.isNotEmpty(groupStudent)) {
					if(emplySubjectIds.contains(groupClass.getSubjectIds())) {
						//不存在的组合
						delClassIds.add(groupClass.getId());
					}
					if(!stuBysubIds.containsKey(groupClass.getSubjectIds())) {
						List<NewGkDivideStusub> list1= EntityUtils.filter2(list,e->{
							String ss = groupClass.getSubjectIds();
							if(BaseConstants.ZERO_GUID.equals(ss)) {
								return true;
							}
							String[] arr = ss.split(",");
							for(String r:arr) {
								if(e.getSubjectIds().indexOf(r)>-1) {
									
								}else {
									return false;
								}
							}
							return true;
						});
						Set<String> set1=EntityUtils.getSet(list1, e->e.getStudentId());
						if(CollectionUtils.isEmpty(set1)) {
							//不存在组合
							emplySubjectIds.add(groupClass.getSubjectIds());
							delClassIds.add(groupClass.getId());
							continue;
						}
						stuBysubIds.put(groupClass.getSubjectIds(), set1);
						//剩下判断有没有学生不属于这个组合的
						List<String> stuIdnow = (List<String>) CollectionUtils
								.intersection(groupStudent, set1);
						if (stuIdnow.size() != groupStudent.size()) {
							groupClass.setStudentList(stuIdnow);
							updateGroup.add(groupClass);
						}
					}else {
						//学生数据为0
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
				return error("操作失败！");
			}
		
		}
		return  success("操作成功");
	}
	@RequestMapping("/showStu/page")
	@ControllerInfo(value = "组合班级详情")
	public String showStu(@PathVariable String divideId, String subjectIds,
			String divideClassId, ModelMap map) {
		// 去开班前
		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			addErrorFtlOperation(map, "返回", "/newgkelective/index/page",
					"#showList");
			return errorFtl(map, "分班方案不存在！");
		}
		List<NewGkDivideClass> divideClassList=new ArrayList<>();
	
		divideClassList = newGkDivideClassService
				.findClassBySubjectIds(newDivide.getUnitId(),
						divideId,
						NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_0, subjectIds, false);
		
		map.put("divideClassList", divideClassList);
		map.put("groupClassId", divideClassId);
		map.put("divideId", divideId);
		return "/newgkelective/divideAuto/openXzbDetail.ftl";
	}
	
	
	
	
	/***************************自动安排批次***********************************************/
	@ResponseBody
	@RequestMapping("/autoArrangeBath")
	@ControllerInfo(value = "保存")
	public String autoArrangeBath(@PathVariable String divideId,String subjectType) {
		final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_Bath_" +subjectType+"_"+ divideId;
		final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_Bath_"+subjectType+"_"
				+ divideId + "_mess";
		JSONObject on = new JSONObject();
		final NewGkDivide divide = newGkDivideService.findOneWithMaster(divideId);
		
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
		if(StringUtils.isBlank(subjectType)) {
			on.put("stat", "error");
			on.put("message", "参数丢失");
			RedisUtils.del(new String[] { key, keyMess });
			return JSON.toJSONString(on);
		}
		if(!(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType) || 
				NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subjectType))) {
			on.put("stat", "error");
			on.put("message", "参数丢失");
			RedisUtils.del(new String[] { key, keyMess });
			return JSON.toJSONString(on);
		}
		
		// 判断分班 状态
		if (RedisUtils.get(key) == null) {
			// 开始智能分班
			RedisUtils.set(key, "start",TIME_ONE_HOUR);
			RedisUtils.set(keyMess, "进行中",TIME_ONE_HOUR);
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
			int bath=3;
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
				bath = divide.getBatchCountTypea();
			}else {
				bath = divide.getBatchCountTypeb();
			}
			autoArrange(divide, subjectType,bath);

			
		} catch (Exception e) {
			e.printStackTrace();
			on.put("stat", "error");
			on.put("message", "失败");
			RedisUtils.del(new String[] { key,  keyMess });
			return error("失败！" + e.getMessage());
		}
		return JSON.toJSONString(on);
	}
	
	private void autoArrange(NewGkDivide divide, String subjectType,int times) {
		final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_Bath_" +subjectType+"_" +divide.getId();
		final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_Bath_"+subjectType+"_"
				+ divide.getId() + "_mess";
		
		//classId,组合subIds(以，隔开)，相同subIds,学生数量
		ShuffleInput shuffleInput=new ShuffleInput();
		List<ShuffleResult> resultList=new ArrayList<>();
		//找到已经的组合
		List<NewGkDivideClass> oldlist = newGkDivideClassService
				.findClassBySubjectIds(getLoginInfo().getUnitId(),divide.getId(),NewGkElectiveConstant.CLASS_SOURCE_TYPE1,
								NewGkElectiveConstant.CLASS_TYPE_0, null, true);
		Map<String, NewGkDivideStusub> stuChooseMap1=new HashMap<>();//真正组合
		Map<String, NewGkDivideStusub> stuChooseMap2=new HashMap<>();//需要安排的科目
		
		if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
			List<NewGkDivideStusub> list = newGkDivideStusubService.findByDivideIdWithMaster(divide.getId(), NewGkElectiveConstant.SUBJECT_TYPE_A, null);
			stuChooseMap1 = EntityUtils.getMap(list, e->e.getStudentId());
			stuChooseMap2 = EntityUtils.getMap(list, e->e.getStudentId());
		}else {
			List<NewGkDivideStusub> list = newGkDivideStusubService.findByDivideIdWithMaster(divide.getId(), null, null);
			Map<String, List<NewGkDivideStusub>> listMap1 = EntityUtils.getListMap(list, e->e.getSubjectType(),e->e);
			stuChooseMap1 = EntityUtils.getMap(listMap1.get(NewGkElectiveConstant.SUBJECT_TYPE_A), e->e.getStudentId());
			stuChooseMap2 = EntityUtils.getMap(listMap1.get(NewGkElectiveConstant.SUBJECT_TYPE_B), e->e.getStudentId());
			
		}
		
		int roomNum=oldlist.size();
		ShuffleResult re;
		Map<String,NewGkDivideClass> clazzMap=new HashMap<>();
		for(NewGkDivideClass n:oldlist) {
			List<String> stuIds = n.getStudentList();
			clazzMap.put(n.getId(), n);
			String sameSubjectIds=null;
			if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subjectType)) {
				sameSubjectIds=n.getSubjectIdsB();
			}else {
				if(!NewGkElectiveConstant.SUBJTCT_TYPE_0.equals(n.getSubjectType()) ) {
					sameSubjectIds=n.getSubjectIds();
				}
			}
			if(CollectionUtils.isEmpty(stuIds)) {
				continue;
			}
			//对于学考 取值需要开设的科目 对于选考默认所有开设
			
			Map<String,Integer> chooseMap=new HashMap<>();
			Map<String,String> groupSubIds=new HashMap<>();
			for(String s:stuIds) {
				NewGkDivideStusub item1 = stuChooseMap1.get(s);
				NewGkDivideStusub item2 = stuChooseMap2.get(s);
				if(item2==null) {
					continue;
				}
				if(!chooseMap.containsKey(item2.getSubjectIds())) {
					chooseMap.put(item2.getSubjectIds(), 1);
				}else {
					chooseMap.put(item2.getSubjectIds(), chooseMap.get(item2.getSubjectIds())+1);
				}
				groupSubIds.put(item2.getSubjectIds(), item1.getSubjectIds());
			}
			for(Entry<String, Integer> var:chooseMap.entrySet()) {
				re=new ShuffleResult();
				re.setClassId(n.getId());
				re.setNameSubjectIds(groupSubIds.get(var.getKey()));
	            re.setSameSubjectIds(sameSubjectIds);
	            re.setStudentNum(var.getValue());
	            re.setSubjectIds(var.getKey());
	            if(var.getKey().split(",").length>times) {
	            	//验证批次不够直接往上加
	            	times=var.getKey().split(",").length;
//	            	RedisUtils.set(key, "error",TIME_ONE_HOUR);
//	    			RedisUtils.set(keyMess, "批次数量不够",TIME_ONE_HOUR);
//	    			return;
	            }
	            resultList.add(re);
			}
			
		}
		final int time2=times;
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					shuffleInput.setResultList(resultList);
					ShuffleWork work=new ShuffleWork(shuffleInput,roomNum,time2,Evn.isDevModel());
					List<ShuffleResult> result = work.shuffleSections();
					saveResult(divide.getId(), divide.getUnitId(),subjectType, result,clazzMap);
					RedisUtils.set(key, "success",TIME_ONE_HOUR);
					RedisUtils.set(keyMess, "自动分配成功",TIME_ONE_HOUR);
					return ;
				} catch (Exception e) {
					e.printStackTrace();
					RedisUtils.set(key, "error",TIME_ONE_HOUR);
					RedisUtils.set(keyMess, "自动分配失败",TIME_ONE_HOUR);
					return ;
				}
				

			}


		}).start();
		
	}


	public void saveResult(String divideId,String unitId,String subjectType,List<ShuffleResult> result,Map<String,NewGkDivideClass> clazzMap) {
		NewGkClassBatch batch;
		List<NewGkClassBatch> insertList=new ArrayList<>();
		Map<String, List<ShuffleResult>> listMap = EntityUtils.getListMap(result, ShuffleResult::getClassId,e->e);
		List<NewGkDivideClass> saveClsList=new ArrayList<>();
		for(Entry<String, List<ShuffleResult>> list:listMap.entrySet()) {
			String clazzId=list.getKey();
			NewGkDivideClass clazz = clazzMap.get(clazzId);
			List<ShuffleResult> value = list.getValue();
			//防止定的科目不在同一个批次点
			Set<Integer> setIds=new HashSet<>();
			for(ShuffleResult rr:value) {
				String subjectIds="";
				if(!NewGkElectiveConstant.SUBJTCT_TYPE_0.equals(clazz.getSubjectType()) ) {
					if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
						subjectIds=clazz.getSubjectIds();
					}else {
						subjectIds=clazz.getSubjectIdsB();
					}
				}
				if(StringUtils.isNotBlank(subjectIds) && subjectIds.contains(rr.getSubjectId())) {
					continue;
				}
				setIds.add(rr.getBath());
				batch=new NewGkClassBatch();
				batch.setId(UuidUtils.generateUuid());
				batch.setDivideClassId(rr.getClassId());
				batch.setDivideId(divideId);
				batch.setUnitId(unitId);
				batch.setSubjectIds(rr.getNameSubjectIds());
				batch.setSubjectId(rr.getSubjectId());
				batch.setBatch(rr.getBath()+"");
				batch.setSubjectType(subjectType);
				insertList.add(batch);
			}
			if(CollectionUtils.isNotEmpty(setIds)) {
				Integer[] arr = setIds.toArray(new Integer[0]);
				Arrays.sort(arr);
				String ss = ArrayUtil.print(arr);
				if(StringUtils.isNotBlank(clazz.getBatch())) {
					String[] arr1 = clazz.getBatch().split(";");
					if(arr1.length==2){
						if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
							clazz.setBatch(ss+";"+(arr1[1]==null?"":arr1[1]));
						}else {
							clazz.setBatch((arr1[0]==null?"":arr1[0])+";"+ss);
						}
					}else {
						clazz.setBatch(ss+";");
					}
						
					
				}else {
					clazz.setBatch(ss+";");
				}
				
				saveClsList.add(clazz);
			}
		}
		newGkClassBatchService.saveBatchs3(unitId, divideId, saveClsList, insertList, subjectType);
	}

	/***************************自动安排教学班***********************************************/
	@ResponseBody
	@RequestMapping("/autoJxb")
	@ControllerInfo(value = "保存")
	public String autoJxb(@PathVariable String divideId,String subjectType,Integer studentNum) {
		String unitId=getLoginInfo().getUnitId();
		if(!(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType) || 
				NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subjectType))) {
			return error("参数错误");
		}
		if(studentNum==null || studentNum<=0) {
			return error("参数错误");
		}
		String subjectTypeName="选";
		if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subjectType)) {
			subjectTypeName="学";
		}
		//获取需要开设的科目
		List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeIn(divideId, new String[] {subjectType});
		Set<String> openSubjectIds = EntityUtils.getSet(openSubjectList, NewGkOpenSubject::getSubjectId);
		if(CollectionUtils.isEmpty(openSubjectIds)) {
			return error("没有需要开设的科目");
		}
		List<Course> courselist = SUtils.dt(courseRemoteService.findBySubjectIdIn(openSubjectIds.toArray(new String[] {})),Course.class);
		Map<String,Course> courseMap = EntityUtils.getMap(courselist, e->e.getId());
		//获取组合班数据
		List<NewGkDivideClass> oldlist = newGkDivideClassService
				.findClassBySubjectIds(getLoginInfo().getUnitId(),divideId,NewGkElectiveConstant.CLASS_SOURCE_TYPE1,
								NewGkElectiveConstant.CLASS_TYPE_0, null, true);
		if(CollectionUtils.isEmpty(oldlist)) {
			return error("组合班数据丢失，请返回后再操作");
		}
		
		List<NewGkDivideStusub> list = newGkDivideStusubService.findByDivideIdWithMaster(divideId, NewGkElectiveConstant.SUBJECT_TYPE_A, null);
		if(CollectionUtils.isEmpty(list)) {
			return error("学生选课数据不存在");
		}
		Map<String, NewGkDivideStusub> stuChooseMap = EntityUtils.getMap(list, e->e.getStudentId());
		
		//批次数据
		List<NewGkClassBatch> bathList = classBatchService.findBySubjectTypeWithMaster(divideId,subjectType);
		if(CollectionUtils.isEmpty(bathList)) {
			return error("走班批次点未维护");
		}
		//key classId_sujectIds,key subjectId 对应科目批次点
		Map<String,Map<String,String>> map=new HashMap<>();
		for(NewGkClassBatch n:bathList) {
			String kk=n.getDivideClassId()+"_"+n.getSubjectIds();
			Map<String, String> map1 = map.get(kk);
			if(map1==null) {
				map1=new HashMap<>();
				map.put(kk, map1);
			}
			map1.put(n.getSubjectId(), n.getBatch());
		}
		//统计结果 subjectId_bath 班级id 学生ids
		Map<String,Map<String,List<String>>> bathStuIdsMap=new HashMap<>();
		for(NewGkDivideClass cc:oldlist) {
			if(CollectionUtils.isEmpty(cc.getStudentList())) {
				continue;
			}
			for(String s:cc.getStudentList()) {
				if(!stuChooseMap.containsKey(s)) {
					continue;
				}
				String subIds = stuChooseMap.get(s).getSubjectIds();
				Map<String, String> map1 = map.get(cc.getId()+"_"+subIds);
				if(map1==null) {
					continue;
				}
				for(Entry<String, String> item:map1.entrySet()) {
					if(openSubjectIds.contains(item.getKey())) {
						Map<String, List<String>> map2 = bathStuIdsMap.get(item.getKey()+"_"+item.getValue());
						if(map2==null) {
							map2=new HashMap<>();
							bathStuIdsMap.put(item.getKey()+"_"+item.getValue(), map2);
						}
						List<String> l1 = map2.get(cc.getId());
						if(l1==null) {
							l1=new ArrayList<>();
							map2.put(cc.getId(), l1);
						}
						l1.add(s);
					}
				}
			}
		}
		List<NewGkDivideClass> insertJxbList=new ArrayList<>();
		List<NewGkClassStudent> csList=new ArrayList<>();
		NewGkClassStudent classStu;
		NewGkDivideClass cc;
		Map<String,Integer> subOpenClassNum=new HashMap<>();
		List<String> keylist = new ArrayList<>();
		keylist.addAll(bathStuIdsMap.keySet());
		Collections.sort(keylist);
		for(String skey:keylist) {
			String[] sub_bath=skey.split("_");
			String subjectId=sub_bath[0];
			String batch=sub_bath[1];
			//安排人员
			Map<String, List<String>> classStuMap = bathStuIdsMap.get(skey);
			if(classStuMap==null || classStuMap.size()==0) {
				continue;
			}
			//统计总人数
			int all=0;
			List<ClassStudent> itemList=new ArrayList<>();
			ClassStudent cs;
			for(Entry<String, List<String>> kk:classStuMap.entrySet()) {
				if(CollectionUtils.isEmpty(kk.getValue())) {
					continue;
				}
				cs=new ClassStudent();
				cs.setClassId(kk.getKey());
				cs.setStuList(new ArrayList<>());
				cs.getStuList().addAll(kk.getValue());
				cs.setSize(cs.getStuList().size());
				all=all+cs.getSize();
				itemList.add(cs);
			}
			if(all==0) {
				continue;
			}
			//根据all以及studentNum 开班
			int openNum=(all-1)/studentNum+1;//开设班级数量
			List<String>[] result=openJxb(openNum,itemList,studentNum);
			if(!subOpenClassNum.containsKey(subjectId)) {
				subOpenClassNum.put(subjectId, 1);
			}
			int k=subOpenClassNum.get(subjectId);
			for(List<String> rr:result) {
				cc = initNewGkDivideClass(divideId, subjectId, NewGkElectiveConstant.CLASS_TYPE_2);
				cc.setClassName(courseMap.get(subjectId).getSubjectName()+subjectTypeName+k+"班");
				cc.setSubjectType(subjectType);
				cc.setBatch(batch);
				cc.setOrderId(k);
				k++;
				for(String r:rr) {
					classStu = initClassStudent(unitId, divideId, cc.getId(), r);
					csList.add(classStu);
				}
				insertJxbList.add(cc);
			}
			subOpenClassNum.put(subjectId, k);
		}
		
		try {
			
			newGkDivideClassService.savejxbBySubjectType(unitId,divideId, subjectType, insertJxbList, csList);
			
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败");
		}
		
		return success("");
		
	}
	//同班级在不超过最大人数时 不拆
	private List<String>[] openJxb(int openNum, List<ClassStudent> itemList,int maxStu) {
		List<String>[] array = new List[openNum];
		sort(itemList);
		ClassStudent cs;
		int allSize=0;
		for(ClassStudent c:itemList) {
			allSize=allSize+c.getSize();
		}
		int avg=(allSize-1)/openNum+1;
		int j=0;
		//初始化 
		//itemList排序
		//1、如果出现刚好<=maxStu--直接开班
		//2、直接出现超过maxStu 根据数量算出本来的平均班级人数 通过平均班级人数进行avg满员开班--可能出现剩余人数 重新排序itemList
		while(true) {
			if(j>=openNum) {
				break;
			}
			array[j] = new ArrayList<String>();
			cs=itemList.get(0);
			if(cs.getSize()<=maxStu) {
				//小于最大值 默认直接开班
				array[j].addAll(cs.getStuList());
				itemList.remove(cs);
				j++;
			}else {
				//大于最大值 --用平均值开班
				int kk=cs.getSize()/avg;//可以开班数量
				
				if(maxStu*kk>=cs.getSize()) {
					int avg1=(cs.getSize()-1)/kk+1;
					//完全开班
					for(int k=0;k<kk;k++) {
						if(j>=openNum) {
							//一般不会出现
							break;
						}
						if(cs.getSize()==0) {
							break;
						}
						List<String> ll=new ArrayList<>() ;
						if(cs.getSize()>avg1) {
							 ll=cs.getStuList().subList(0, avg);
						}else {
							ll=cs.getStuList().subList(0, cs.getSize());
						}
						if(array[j]==null) {
							array[j]=new ArrayList<>();
						}
						array[j].addAll(ll);
						j++;
						cs.getStuList().removeAll(ll);
						cs.setSize(cs.getStuList().size());
					}
				}else {
					for(int k=0;k<kk;k++) {
						if(j>=openNum) {
							//一般不会出现
							break;
						}
						List<String> ll = cs.getStuList().subList(0, avg);
						if(array[j]==null) {
							array[j]=new ArrayList<>();
						}
						array[j].addAll(ll);
						j++;
						cs.getStuList().removeAll(ll);
						cs.setSize(cs.getStuList().size());
					}
					sort(itemList);
				}
				if(cs.getSize()==0) {
					itemList.remove(cs);
				}
			}
		}
		//第二步 从最大值人数开始操作，且完全分完人员
		while(true) {
			if(CollectionUtils.isEmpty(itemList)) {
				break;
			}
			ClassStudent tt = itemList.get(0);
			//取得最多班级的数据放到最小的里面
			int minIndex=findMin(array);//min
			//1、如果人数完全可以放在最小班级里面，只要人数少于maxStu
			if(array[minIndex].size()+tt.getSize()<=maxStu) {
				//所有
				array[minIndex].addAll(tt.getStuList());
				itemList.remove(tt);
			}else {
				//2、如果不可以，从最小班级人数开始，放足人数在最小班级 使其到达avg
				if(array[minIndex].size()>avg) {
					//不可能出现 跳出循环
					break;
				}else {
					while(true) {
						if(tt.getSize()==0) {
							break;
						}
						int gg=avg-array[minIndex].size();
						if(gg>=tt.getSize()) {
							//完全分完
							array[minIndex].addAll(tt.getStuList());
							itemList.remove(tt);
							tt.setStuList(new ArrayList<>());
							tt.setSize(0);
							break;
						}else {
							List<String> ll = tt.getStuList().subList(0, gg);
							array[minIndex].addAll(ll);
							tt.getStuList().removeAll(ll);
							tt.setSize(tt.getStuList().size());
							//找到最小
							minIndex=findMin(array);
						}
					}
				}
			}
		}
		
		
//		for (int i = 0; i < openNum; i++) {
//			array[i] = new ArrayList<String>();
//			//取得第一多
//			cs=itemList.get(0);
//			if(cs.getSize()<=maxStu) {
//				//小于最大值 默认直接开班
//				array[i].addAll(cs.getStuList());
//				itemList.remove(cs);
//			}else {
//				//大于最大值 
//				List<String> ll = cs.getStuList().subList(0, maxStu);
//				array[i].addAll(ll);
//				cs.getStuList().removeAll(ll);
//				cs.setSize(cs.getStuList().size());
//				sort(itemList);
//			}
//		}
		//itemList排序
//		while(true) {
//			if(CollectionUtils.isEmpty(itemList)) {
//				break;
//			}
//			//取得最多班级的数据放到最小的里面
//			int i=findMin(array);
//			ClassStudent tt = itemList.get(0);
//			if(array[i].size()+tt.getSize()<=maxStu) {
//				//所有
//				array[i].addAll(tt.getStuList());
//				itemList.remove(tt);
//			}else {
//				int left = maxStu-array[i].size();
//				List<String> ll = tt.getStuList().subList(0, left);
//				array[i].addAll(ll);
//				tt.getStuList().removeAll(ll);
//				tt.setSize(tt.getStuList().size());
//				sort(itemList);
//			}
//		}
		return array;
		
	}
	public int findMin(List<String>[] array) {
		int i=0;
		int min=array[0].size();
		for(int j=1;j<array.length;j++) {
			if(min>array[j].size()) {
				i=j;
				min=array[j].size();
			}
		}
		return i;
	}
	
	//根据人数降序
	public void sort(List<ClassStudent> temp) {
		if (CollectionUtils.isNotEmpty(temp)) {
			Collections.sort(temp, new Comparator<ClassStudent>() {
				@Override
				public int compare(ClassStudent o1, ClassStudent o2) {
					int cc = o2.getSize() - o1.getSize();
					if (cc > 0) {
						return 1;
					} else if (cc < 0) {
						return -1;
					} else {
						return 0;
					}
				}

			});
		}
	}

	class ClassStudent{
		private String classId;
		private List<String> stuList=new ArrayList<>();
		private int size;
		public String getClassId() {
			return classId;
		}
		public void setClassId(String classId) {
			this.classId = classId;
		}
		public List<String> getStuList() {
			return stuList;
		}
		public void setStuList(List<String> stuList) {
			this.stuList = stuList;
		}
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
		
	}
	
	/**********************3+1+2单科分层模式***************************************/
	/**
	 * 返回物理历史科目信息 5分钟缓存
	 * @param choiceId
	 * @param unitId
	 * @return
	 */
	public String[][] findWuliAndLishi(String choiceId,String unitId) {
		return RedisUtils.getObject("choice_wuli_lishi"+choiceId, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<String[][]>(){}, new RedisInterface<String[][]>(){
			@Override
			public String[][] queryData() {
				//默认增加历史物理
				List<Course> courseList=newGkChoRelationService.findChooseSubject(choiceId, unitId);
				//判断有没有物理，历史
				String[][] ss1=new String[2][];
				for(Course c:courseList) {
					if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(c.getSubjectCode())) {
						ss1[0]=new String[] {c.getSubjectCode(),c.getId(), c.getSubjectName()};
					}else if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(c.getSubjectCode())) {
						ss1[1]=new String[] {c.getSubjectCode(),c.getId(), c.getSubjectName()};
					}
				}
				return ss1;
			}
        });
	}
	
	/**
	 * 单科分层重组 09,单科分层不重组 11分选考
	 * @param divided
	 * @param map
	 * @return
	 */
	public String singleRecomb(NewGkDivide divided,ModelMap map) {
		String unitId=getLoginInfo().getUnitId();
		//行政班科目--物理--历史
		List<Course> courseList=newGkChoRelationService.findChooseSubject(divided.getChoiceId(), unitId);
		
		//已经安排学生--暂时不考虑学生状态变动（删除等）
		List<NewGkDivideStusub> stuchooseList = newGkDivideStusubService.findByDivideIdWithMaster(divided.getId(), NewGkElectiveConstant.SUBJECT_TYPE_A, null);
		//统计各种科目人数
		Map<String,Integer> countBySubjectId=new HashMap<>();
		for(NewGkDivideStusub sub:stuchooseList) {
			String[] sids = sub.getSubjectIds().split(",");
			for(String s:sids) {
				if(!countBySubjectId.containsKey(s)) {
					countBySubjectId.put(s, 1);
				}else {
					countBySubjectId.put(s, countBySubjectId.get(s)+1);
				}
			}
		}
		//各种层次安排的人员
		List<NewGKStudentRange> stuRangeList = newGKStudentRangeService.findByDivideIdSubjectIdAndSubjectType(divided.getId(), null,
				NewGkElectiveConstant.SUBJECT_TYPE_A, null);
		//key:subjectId,key:range
		Map<String,Map<String,Integer>> arrangeCountBySubjectId=new HashMap<>();
		Map<String,Integer> arrangeBySubjectId=new HashMap<>();
		if(CollectionUtils.isNotEmpty(stuRangeList)) {
			for(NewGKStudentRange r:stuRangeList) {
				if(!arrangeBySubjectId.containsKey(r.getSubjectId())) {
					arrangeBySubjectId.put(r.getSubjectId(), 1);
					arrangeCountBySubjectId.put(r.getSubjectId(), new HashMap<>());
				}else {
					arrangeBySubjectId.put(r.getSubjectId(), arrangeBySubjectId.get(r.getSubjectId())+1);
				}
				Map<String, Integer> map1 = arrangeCountBySubjectId.get(r.getSubjectId());
				if(!map1.containsKey(r.getRange())) {
					map1.put(r.getRange(), 1);
				}else {
					map1.put(r.getRange(), map1.get(r.getRange())+1);
				}
			}
		}
		List<SingleRange> xzbList=new ArrayList<>();
		List<SingleRange> jxbList=new ArrayList<>();
		SingleRange singleRange;
		SingleRange singleRange1;
		
		//开设科目
		List<NewGkOpenSubject> openSubList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeInWithMaster(divided.getId(), new String[] {NewGkElectiveConstant.SUBJECT_TYPE_A});
		Set<String> subIds = EntityUtils.getSet(openSubList, e->e.getSubjectId());
		
		for(Course c:courseList) {
			singleRange=new SingleRange();
			singleRange.setSubjectId(c.getId());
			singleRange.setSubjectName(c.getSubjectName());
			singleRange.setPngName(NewGkElectiveConstant.DEFAULT_PNG.get(c.getSubjectCode()));
			
			singleRange.setStuNum(countBySubjectId.get(c.getId())==null?0:countBySubjectId.get(c.getId()));
			singleRange.setArrangeNum(arrangeBySubjectId.get(c.getId())==null?0:arrangeBySubjectId.get(c.getId()));
			List<SingleRange> rangeList=new ArrayList<>();
			Map<String, Integer> map2 = arrangeCountBySubjectId.get(c.getId());
			if(map2!=null && map2.size()>0) {
				for(Entry<String, Integer> item:map2.entrySet()) {
					singleRange1=new SingleRange();
					singleRange1.setSubjectId(c.getId());
					singleRange1.setSubjectName(c.getSubjectName());
					singleRange1.setArrangeNum(item.getValue()==null?0:item.getValue());
					singleRange1.setType(item.getKey());
					rangeList.add(singleRange1);
				}
				//排序
				if (CollectionUtils.isNotEmpty(rangeList)) {
					Collections.sort(rangeList, new Comparator<SingleRange>() {
						@Override
						public int compare(SingleRange o1, SingleRange o2) {
							return o1.getType().compareTo(o2.getType());
						}

					});
				}
			}
			singleRange.setRangeList(rangeList);
			if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(c.getSubjectCode())) {
				xzbList.add(singleRange);
			
			}else if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(c.getSubjectCode())) {
				xzbList.add(singleRange);
			}else {
				if(subIds.contains(c.getId())) {
					jxbList.add(singleRange);
				}
			}
		}
		
		map.put("jxbList", jxbList);
		map.put("gradeId", divided.getGradeId());
		
		boolean canEdit=true;
		if(isNowDivide(divided.getId())) {
			canEdit=false;
		}else {
			List<NewGkDivideClass> jxbClassList = newGkDivideClassService.findByDivideIdAndClassTypeSubjectTypeWithMaster(divided.getUnitId(),
					divided.getId(), new String[] {NewGkElectiveConstant.CLASS_TYPE_2}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, null);
			if(CollectionUtils.isNotEmpty(jxbClassList)) {
				canEdit=false;
			}
		}
		map.put("canEdit", canEdit);
		map.put("openType", divided.getOpenType());
		if(!NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divided.getOpenType())) {
			map.put("xzbList", xzbList);
		}
		return "/newgkelective/singleRecomb/singleRecombIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/checkSingleA")
	@ControllerInfo(value = "验证学生分层")
	public String checkSingleA(@PathVariable String divideId,ModelMap map) {
		NewGkDivide divide = newGkDivideService.findOneWithMaster(divideId);
		if (divide == null) {
			return error("分班方案不存在");
		}
		List<NewGkDivideStusub> stuchooseList = newGkDivideStusubService.findByDivideIdWithMaster(divide.getId(), NewGkElectiveConstant.SUBJECT_TYPE_A, null);
		//开设科目
		List<NewGkOpenSubject> openSubList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeInWithMaster(divide.getId(), new String[] {NewGkElectiveConstant.SUBJECT_TYPE_A});
		Set<String> subIds = EntityUtils.getSet(openSubList, e->e.getSubjectId());
		List<Course> courseList=newGkChoRelationService.findChooseSubject(divide.getChoiceId(), divide.getUnitId());
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, e->e.getId());
		
		List<NewGKStudentRange> stuRangeList = newGKStudentRangeService.findByDivideIdSubjectIdAndSubjectType(divide.getId(), null,
				NewGkElectiveConstant.SUBJECT_TYPE_A, null);
		if(CollectionUtils.isEmpty(stuRangeList)) {
			return error("未完成分层");
		}
		Map<String,List<String>> rangeByStuId=new HashMap<>();
		if(CollectionUtils.isNotEmpty(stuRangeList)) {
			for(NewGKStudentRange r:stuRangeList) {
				if(!rangeByStuId.containsKey(r.getStudentId())) {
					rangeByStuId.put(r.getStudentId(), new ArrayList<>());
				}
				rangeByStuId.get(r.getStudentId()).add(r.getSubjectId());
			}
		}
		
		boolean isShowWL=false;
		if(NewGkElectiveConstant.DIVIDE_TYPE_09.equals(divide.getOpenType())) {
			isShowWL=true;
		}
		for(NewGkDivideStusub s:stuchooseList) {
			String[] arr = s.getSubjectIds().split(",");
			List<String> canList=new ArrayList<>();
			for(String a:arr) {
				if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(courseMap.get(a).getSubjectCode())
						|| NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(courseMap.get(a).getSubjectCode())) {
					if(isShowWL) {
						canList.add(a);
					}
				}else {
					if(subIds.contains(a)) {
						canList.add(a);
					}
				}
			}
			if(CollectionUtils.isEmpty(canList)) {
				continue;
			}
			if(!rangeByStuId.containsKey(s.getStudentId())) {
				logger.info(s.getStudentName()+"所有都科目未分层");
				return error("未完成分层");
			}
			if(CollectionUtils.intersection(canList, rangeByStuId.get(s.getStudentId())).size()==canList.size()) {
				
			}else {
				logger.info(s.getStudentName()+"未完成分层");
				return error("未完成分层");
			}
			
		}
		
		return success("");
	}
	
	
	
	/**
	 * 选考分层页面
	 * @return
	 */
	@RequestMapping("/xkSingle/page")
	@ControllerInfo(value = "选考分层页面")
	public String xkSingle(@PathVariable String divideId,ModelMap map) {
		NewGkDivide divide = newGkDivideService.findOneWithMaster(divideId);
		if (divide == null) {
			return errorFtl(map, "分班方案不存在");
		}
		map.put("divideType", divide.getOpenType());
		return singleRecomb(divide, map);
	}
	
	
	
	
	public class SingleRange{
		private String subjectId;
		private String subjectName;
		private String pngName;
		private int stuNum;
		private int arrangeNum;
		private String type;//A,B,C,D
		private List<SingleRange> rangeList=new ArrayList<>();
		public String getSubjectId() {
			return subjectId;
		}
		public void setSubjectId(String subjectId) {
			this.subjectId = subjectId;
		}
		public String getSubjectName() {
			return subjectName;
		}
		public void setSubjectName(String subjectName) {
			this.subjectName = subjectName;
		}
		public int getStuNum() {
			return stuNum;
		}
		public void setStuNum(int stuNum) {
			this.stuNum = stuNum;
		}
		public int getArrangeNum() {
			return arrangeNum;
		}
		public void setArrangeNum(int arrangeNum) {
			this.arrangeNum = arrangeNum;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public List<SingleRange> getRangeList() {
			return rangeList;
		}
		public void setRangeList(List<SingleRange> rangeList) {
			this.rangeList = rangeList;
		}
		public String getPngName() {
			return pngName;
		}
		public void setPngName(String pngName) {
			this.pngName = pngName;
		}
		
	}
	
	/**
	 * 单科分层不重组 11
	 * @param divided
	 * @param map
	 * @return
	 */
	
	public String nosingleRecomb(NewGkDivide divided,ModelMap map) {
		String unitId=getLoginInfo().getUnitId();
		boolean canEdit=true;
		if(isNowDivide(divided.getId())) {
			canEdit=false;
		}
		if(canEdit) {
			if(divided.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_12)) {
				//不限制
			}else {
				List<NewGkDivideClass> jxbList = newGkDivideClassService.findByDivideIdAndClassTypeWithMaster(unitId, divided.getId(), 
						new String[] {NewGkElectiveConstant.CLASS_TYPE_2}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
				if(CollectionUtils.isNotEmpty(jxbList)) {
					canEdit=false;
				}
			}
			
		}
		//默认选中物理历史的人
		String[][] list_W_L = findWuliAndLishi(divided.getChoiceId(), unitId);
		if(list_W_L.length!=2 || list_W_L[0]==null || list_W_L[1]==null) {
			return errorFtl(map, "选课科目有误");
		}
		String wuliCourse=list_W_L[0][1];//物理id
		String lishiCourse=list_W_L[1][1];//历史id
		List<NewGkDivideClass> oldClassList = newGkDivideClassService.findByDivideIdAndClassTypeWithMaster(unitId, divided.getId(), 
				new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_3}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		
		List<SingleClassDto> dtoList=new ArrayList<>();
		Map<String,SingleClassDto> dtoMap=new HashMap<>();
		SingleClassDto dto;
		List<NewGkDivideClass> list_0 = oldClassList.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_1)).collect(Collectors.toList());
		List<NewGkDivideClass> list_3 = oldClassList.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_3)).collect(Collectors.toList());
		List<NewGkDivideStusub> stuchooseList = newGkDivideStusubService.findByDivideIdWithMaster(divided.getId(), NewGkElectiveConstant.SUBJECT_TYPE_A, null);
		Map<String, NewGkDivideStusub> stuMap = EntityUtils.getMap(stuchooseList, e->e.getStudentId());
		
		//relateId
		Map<String,NewGkDivideClass> oldMap=new HashMap<>();
		Set<String> oldId3=new HashSet<>();
		if(CollectionUtils.isNotEmpty(list_3)) {
			for(NewGkDivideClass n:list_3) {
				String[] arr = n.getRelateId().split(",");
				for(String s:arr) {
					//如果某个班级同一个科目进去两个班级
					oldMap.put(s+"_"+n.getSubjectIds(), n);
				}
				oldId3.add(n.getId());
			}
		}
		
		List<NewGkDivideClass> updateOrInsertList=new ArrayList<>();
		Map<String,NewGkDivideClass> updateClassMap=new HashMap<>();
		//key:classType=3的班级id  value=xzbIds
		Map<String,List<String>> updateMap=new HashMap<>();
		boolean isNeedSave=false;//整体是否有修改
		for(NewGkDivideClass n:list_0) {
			if(CollectionUtils.isEmpty(n.getStudentList())) {
				//学生行政班
				continue;
			}
			int boynum=0;
			int girlnum=0;
			int wulinums=0;
			int lishinums=0;
			for(String s:n.getStudentList()) {
				if(!stuMap.containsKey(s)) {
					//学生没有选课结果--不计入
					continue;
				}
				NewGkDivideStusub stuchoose = stuMap.get(s);
				if("男".equals(stuchoose.getStudentSex())) {
					boynum++;
				}else {
					girlnum++;
				}
				if(stuchoose.getSubjectIds().indexOf(wuliCourse)>-1) {
					wulinums++;
				}else {
					lishinums++;
				}
			}
			if(boynum==0 && girlnum==0) {
				continue;
			}
			dto=new SingleClassDto();
			dto.setClassId(n.getId());
			dto.setClassName(n.getClassName());
			dto.setAllStuNum(boynum+girlnum);
			dto.setBoyNum(boynum);
			dto.setGirlNum(girlnum);
			dto.setCourseNum1(wulinums);
			dto.setCourseNum2(lishinums);
			if(wulinums>0 ) {
				NewGkDivideClass cc = oldMap.get(n.getId()+"_"+wuliCourse);
				if(cc==null) {
					
					cc=new NewGkDivideClass();
					cc.setClassType(NewGkElectiveConstant.CLASS_TYPE_3);
					cc.setId(UuidUtils.generateUuid());
					cc.setSubjectIds(wuliCourse);
					cc.setDivideId(divided.getId());
					cc.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
					//对应的学考id
					cc.setSubjectIdsB(lishiCourse);
					cc.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
					/**
					 * 其他字段DOTO
					 */
					updateOrInsertList.add(cc);
					updateClassMap.put(cc.getId(), cc);
					if(!isNeedSave) {
						isNeedSave=true;
					}
				}else {
					if(!updateClassMap.containsKey(cc.getId())) {
						updateOrInsertList.add(cc);
						updateClassMap.put(cc.getId(), cc);
					}
				}
				if(!updateMap.containsKey(cc.getId())) {
					updateMap.put(cc.getId(), new ArrayList<>());
				}
				updateMap.get(cc.getId()).add(dto.getClassId());
				
			}
			if(lishinums>0) {
				NewGkDivideClass cc = oldMap.get(n.getId()+"_"+lishiCourse);
				if(cc==null) {
					cc=new NewGkDivideClass();
					cc.setClassType(NewGkElectiveConstant.CLASS_TYPE_3);
					cc.setId(UuidUtils.generateUuid());
					cc.setSubjectIds(lishiCourse);
					cc.setCreationTime(new Date());
					cc.setModifyTime(new Date());
					cc.setDivideId(divided.getId());
					cc.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
					//对应的学考id
					cc.setSubjectIdsB(wuliCourse);
					cc.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
					/**
					 * 其他字段DOTO
					 */
					updateOrInsertList.add(cc);
					updateClassMap.put(cc.getId(), cc);
				}else {
					if(!updateClassMap.containsKey(cc.getId())) {
						updateOrInsertList.add(cc);
						updateClassMap.put(cc.getId(), cc);
					}
				}
				if(!updateMap.containsKey(cc.getId())) {
					updateMap.put(cc.getId(), new ArrayList<>());
				}
				updateMap.get(cc.getId()).add(dto.getClassId());
			}
			
			dtoList.add(dto);
			dtoMap.put(dto.getClassId(), dto);
		}

		
		for(NewGkDivideClass update:updateOrInsertList) {
			if(oldId3.contains(update.getId())) {
				oldId3.remove(update.getId());
			}
			List<String> xzbIdList = updateMap.get(update.getId());
			if(!isNeedSave) {
				if(StringUtils.isNotBlank(update.getRelateId())) {
					String[] oldRelateIds = update.getRelateId().split(",");
					if(xzbIdList.size()!=oldRelateIds.length) {
						if(!isNeedSave) {
							isNeedSave=true;
						}
					}else {
						for(String t:oldRelateIds) {
							if(!xzbIdList.contains(t)) {
								if(!isNeedSave) {
									isNeedSave=true;
								}
								break;
							}
						}
					}
				}else {
					isNeedSave=true;
				}
			}
			int studentCount=0;
			String relateIds="";
			String relateName="";
			String classNameSub="";
			if(wuliCourse.equals(update.getSubjectIds())) {
				classNameSub="物理";
				for(String s:xzbIdList) {
					studentCount+=dtoMap.get(s).getCourseNum1();
					relateName=relateName+"+"+dtoMap.get(s).getClassName();
					relateIds=relateIds+","+s;
				}
			}else {
				classNameSub="历史";
				for(String s:xzbIdList) {
					studentCount+=dtoMap.get(s).getCourseNum2();
					relateName=relateName+"+"+dtoMap.get(s).getClassName();
					relateIds=relateIds+","+s;
				}
			}
			relateName=relateName.substring(1);
			relateIds=relateIds.substring(1);
			if(xzbIdList.size()==1) {
				relateName="";
				if(StringUtils.isNotBlank(update.getParentId()) && xzbIdList.contains(update.getParentId())) {
					
				}else {
					if(!isNeedSave) {
						isNeedSave=true;
					}
					update.setParentId(xzbIdList.get(0));
				}
				String cname=dtoMap.get(update.getParentId()).getClassName()+"-"+classNameSub+"选";
				if(canEdit) {
					if(!cname.equals(update.getClassName())) {
						if(!isNeedSave) {
							isNeedSave=true;
						}
					}
					update.setClassName(cname);
				}else {
					if(StringUtils.isBlank(update.getClassName())) {
						update.setClassName(cname);
					}
				}
			}else {
				if(StringUtils.isNotBlank(update.getParentId()) && xzbIdList.contains(update.getParentId())) {
					
				}else {
					if(!isNeedSave) {
						isNeedSave=true;
					}
					update.setParentId(xzbIdList.get(0));
				}
				String cname=classNameSub+"-"+"选"+dtoMap.get(update.getParentId()).getClassName();
				if(canEdit) {
					if(!cname.equals(update.getClassName())) {
						if(!isNeedSave) {
							isNeedSave=true;
						}
					}
					update.setClassName(cname);
				}else {
					if(StringUtils.isBlank(update.getClassName())) {
						update.setClassName(cname);
					}
				}
				
			}
			update.setRelateId(relateIds);
			update.setModifyTime(new Date());
			update.setRelateName(relateName);
			update.setStudentCount(studentCount);
			
			//dtoList
			SingleClassDto dd = dtoMap.get(update.getParentId());
			if(wuliCourse.equals(update.getSubjectIds())) {
				dd.setRelaJxb1(new String[] {update.getId(),update.getClassName(),studentCount+"",update.getRelateName()});
			}else {
				dd.setRelaJxb2(new String[] {update.getId(),update.getClassName(),studentCount+"",update.getRelateName()});
			}
				
		}
		String[] delIds=null;
		if(CollectionUtils.isNotEmpty(oldId3)) {
			delIds=oldId3.toArray(new String[0] );
		}
		if( isNeedSave || delIds!=null) {
			if(canEdit) {
				try {
					newGkDivideClassService.saveClassOrDel(divided.getUnitId(),divided.getId(),updateOrInsertList, delIds, null);
				}catch (Exception e) {
					e.printStackTrace();
					return errorFtl(map, "初始化数据错误");
				}
			}else {
				//不能修改，但是数据有误--一般不存在
				map.put("isError", true);
			}
		}
		
		map.put("gradeId", divided.getGradeId());
		map.put("dtoList", dtoList);
		if(divided.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_12)) {
			return "/newgkelective/singleRecomb/nosinglefixedIndex.ftl";
		}
		//3+1+2不重组
		map.put("canEdit", canEdit);
		return "/newgkelective/singleRecomb/nosingleRecombIndex.ftl";
	}
	
	public class SingleClassDto{
		private String classId;
		private String className;
		private int allStuNum;
		private int boyNum;
		private int girlNum;
		private int courseNum1;//物理
		private int courseNum2;//历史
		//1个行政班只会关联一个
		//班级id,班级name,班级人数[详情(可有可无)]
		private String[] relaJxb1;//物理关联
		private String[] relaJxb2;//历史关联
		
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
		public int getAllStuNum() {
			return allStuNum;
		}
		public void setAllStuNum(int allStuNum) {
			this.allStuNum = allStuNum;
		}
		public int getBoyNum() {
			return boyNum;
		}
		public void setBoyNum(int boyNum) {
			this.boyNum = boyNum;
		}
		public int getGirlNum() {
			return girlNum;
		}
		public void setGirlNum(int girlNum) {
			this.girlNum = girlNum;
		}
		public int getCourseNum1() {
			return courseNum1;
		}
		public void setCourseNum1(int courseNum1) {
			this.courseNum1 = courseNum1;
		}
		public int getCourseNum2() {
			return courseNum2;
		}
		public void setCourseNum2(int courseNum2) {
			this.courseNum2 = courseNum2;
		}
		public String[] getRelaJxb1() {
			return relaJxb1;
		}
		public void setRelaJxb1(String[] relaJxb1) {
			this.relaJxb1 = relaJxb1;
		}
		public String[] getRelaJxb2() {
			return relaJxb2;
		}
		public void setRelaJxb2(String[] relaJxb2) {
			this.relaJxb2 = relaJxb2;
		}
		
		
	}
	
	/**
	 * 3+1+2组合固定重组
	 */
	public String singlefixed(NewGkDivide divide,NewGkChoice choice,ModelMap map) {
		//默认都是可编辑的
		boolean isCanEdit = true;
		//是否开设其余2科组合班
		List<NewGkDivideClass> zhClass4 = newGkDivideClassService
				.findByDivideIdAndClassType(divide.getUnitId(),divide.getId(),new String[] { NewGkElectiveConstant.CLASS_TYPE_4 }, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		if(CollectionUtils.isNotEmpty(zhClass4)) {
			isCanEdit=false;
		}
		
		map.put("isCanEdit", isCanEdit);
		
		map.put("gradeId", divide.getGradeId());
		List<String> subjectIdList = newGkChoRelationService.findByChoiceIdAndObjectType(choice.getUnitId(), choice.getId(), NewGkElectiveConstant.CHOICE_TYPE_01);
		if(CollectionUtils.isEmpty(subjectIdList)) {
			return errorFtl(map, "选课方案："+choice.getChoiceName()+"，选课科目有问题。");
		}
		List<Course> courselist = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIdList.toArray(new String[] {})),Course.class);
		if(CollectionUtils.isEmpty(courselist)) {
			return errorFtl(map, "选课方案："+choice.getChoiceName()+"，选课科目有问题。");
		}
		map.put("courseList",courselist);
		Map<String,Course> courseMap = EntityUtils.getMap(courselist, e->e.getId());
		String[][] ss1=new String[2][];
		for(Course c:courselist) {
			if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(c.getSubjectCode())) {
				ss1[0]=new String[] {c.getSubjectCode(),c.getId(), c.getSubjectName()};
			}else if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(c.getSubjectCode())) {
				ss1[1]=new String[] {c.getSubjectCode(),c.getId(), c.getSubjectName()};
			}
		}
		if(ss1[0]==null || ss1[1]==null) {
			return errorFtl(map, "选课方案："+choice.getChoiceName()+"，选课科目有问题。");
		}
		
		
		Set<String> subIds = courseMap.keySet();
		int size=subIds.size();
		// 所有学生选课情况
		Map<String,List<String>> subjectIdsByStuId=new HashMap<String, List<String>>();
		Set<String> chooseIds=findStudentChoose(divide.getId(), subjectIdsByStuId);
		chooseIds.addAll(chooseIds);
		if(size!=subIds.size()) {
			return errorFtl(map, "选课方案："+choice.getChoiceName()+"，学生选课数据有问题。");
		}
		
		// 已有组合班数据
		List<NewGkDivideClass> oldGroupList = newGkDivideClassService
				.findByDivideIdAndClassType(divide.getUnitId(),divide.getId(),new String[] { NewGkElectiveConstant.CLASS_TYPE_0 }, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		boolean flag = false;// 是否有异常
		List<NewGkGroupDto> oldGroupDtoList = makeOldGroupDto(oldGroupList);
		
		// 3门组合
		List<NewGkGroupDto> gDtoList = new ArrayList<NewGkGroupDto>();
		Map<String, NewGkGroupDto> gDtomap = new HashMap<String, NewGkGroupDto>();
		Map<String, Set<String>> subjectIdsMap = new HashMap<String, Set<String>>();// 同组合下学生

		//1门组合
		List<NewGkGroupDto> gDtoList3 = new ArrayList<NewGkGroupDto>();
		Map<String, NewGkGroupDto> gDtomap3 = new HashMap<String, NewGkGroupDto>();
		Map<String, Set<String>> subjectIdsMap3 = new HashMap<String, Set<String>>();// 同组合下学生

		
		NewGkGroupDto g = new NewGkGroupDto();
		int chooseNum = choice.getChooseNum();
		
		int allStuNums=0;
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
				g=makeNewGkGroupDto(ids,NewGkElectiveConstant.SUBJTCT_TYPE_3,nameSet(courseMap, ids));
				
				gDtoList.add(g);
				gDtomap.put(ids, g);
				subjectIdsMap.put(ids, new HashSet<String>());
				subjectIdsMap.get(ids).add(stuId);
			} else {
				subjectIdsMap.get(ids).add(stuId);
				g = gDtomap.get(ids);
				g.setAllNumber(g.getAllNumber() + 1);
			}

			//1科
			for(String s:chooseSubjectId) {
				if(ss1[0][1].equals(s) || ss1[1][1].equals(s)){
					if (!gDtomap3.containsKey(s)) {
						g=makeNewGkGroupDto(s,NewGkElectiveConstant.SUBJTCT_TYPE_1,courseMap.get(s).getSubjectName());
						gDtoList3.add(g);
						gDtomap3.put(s, g);
						subjectIdsMap3.put(s, new HashSet<String>());
						subjectIdsMap3.get(s).add(stuId);
					} else {
						subjectIdsMap3.get(s).add(stuId);
						g = gDtomap3.get(s);
						g.setAllNumber(g.getAllNumber() + 1);
					}
				}
				
			}
			allStuNums++;
		}

		Set<String> arrangeStuId = new HashSet<String>();// 已经排的学生
		if (CollectionUtils.isNotEmpty(oldGroupList)) {
			List<NewGkDivideClass> gc = null;
			for (NewGkGroupDto dd : oldGroupDtoList) {
				 if(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(dd.getSubjectType())) {
					 if (gDtomap.containsKey(dd.getSubjectIds())) {
						boolean f= makeArrangeStu(gDtomap.get(dd.getSubjectIds()), dd, subjectIdsMap.get(dd.getSubjectIds()), arrangeStuId,
								subjectIdsByStuId,courseMap);
						if(f){
							dd.setNotexists(1);
						}
						if (!flag && f) {
							flag = true;
						}
					 }else {
						// 这种组合已经不存在啦
						dd.setConditionName(nameSet(courseMap, dd.getSubjectIds()));
						if (!flag) {
							flag = true;
						}
						dd.setNotexists(1);
						gDtoList.add(dd);
						gDtomap.put(dd.getSubjectIds(), dd);
						gc = dd.getGkGroupClassList();
						if (CollectionUtils.isNotEmpty(gc)) {
							for (NewGkDivideClass gg : gc) {
								if (CollectionUtils.isNotEmpty(gg.getStudentList())) {
									arrangeStuId.addAll(gg.getStudentList());
								}
							}
						}
					 }
				 }else if(NewGkElectiveConstant.SUBJTCT_TYPE_1.equals(dd.getSubjectType())) {
					 if (gDtomap3.containsKey(dd.getSubjectIds())) {
						boolean f= makeArrangeStu(gDtomap3.get(dd.getSubjectIds()), dd, subjectIdsMap3.get(dd.getSubjectIds()), arrangeStuId,
								subjectIdsByStuId,courseMap);
						if(f ){
							dd.setNotexists(1);
						}
						if (!flag && f) {
							flag = true;
						}
					 }else {
						dd.setConditionName(nameSet(courseMap, dd.getSubjectIds()));
						if (!flag) {
							flag = true;
						}
						dd.setNotexists(1);
						gDtoList3.add(dd);
						gDtomap3.put(dd.getSubjectIds(), dd);
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
		if (CollectionUtils.isNotEmpty(gDtoList3)) {
			for (NewGkGroupDto dd : gDtoList3) {
				Set<String> stuIds = subjectIdsMap3.get(dd.getSubjectIds());
				// 取得除去 arrangeStuId中剩下的学生
				stuIds.removeAll(arrangeStuId);
				dd.setLeftNumber(stuIds.size());
			}
		}
		
		int chosenStudentNum=allStuNums;
		int fixStudentNum=arrangeStuId.size();
		int nofixStudentNum=chosenStudentNum-fixStudentNum;
	
		map.put("chosenStudentNum", chosenStudentNum);
		map.put("noFixStudentNum", nofixStudentNum);
		map.put("fixStudentNum", fixStudentNum);
		
		// 根据总人数排序
		SortUtils.DESC(gDtoList, "allNumber");
		map.put("gDtoList", gDtoList);
		SortUtils.DESC(gDtoList3, "allNumber");
		map.put("gDtoList3", gDtoList3);

		map.put("haserror", flag);
		
		return "/newgkelective/singleRecomb/singlefixedIndex.ftl";
		
	}
	
	@RequestMapping("/singlefixedJxb/page")
	@ControllerInfo(value = "进入下一步分教学班(剩下两科组合班)")
	public String singlefixedJxb(@PathVariable String divideId) {
		
		return "/newgkelective/singleRecomb/singlefixedJxb.ftl";
	}
	
	
	/**
	 * 3+1+2组合不重组
	 */
	public String nosinglefixed(NewGkDivide divided,ModelMap map) {
		
		return nosingleRecomb(divided, map);
	}
	
	/**
	 * 选考参数设置
	 */
	@RequestMapping("/parametersetA")
	@ControllerInfo(value = "参数设置")
	public String showparameterA(@PathVariable String divideId,ModelMap map) {
		NewGkDivide gkDivide = newGkDivideService.findById(divideId);
		if(gkDivide == null || (gkDivide.getIsDeleted() != null && gkDivide.getIsDeleted() == 1)) {
    		return errorFtl(map, "分班记录不存在或已被删除！");
    	}
		map.put("gradeId",gkDivide.getGradeId());
		map.put("divideId", gkDivide.getId());
		NewGkChoice choice = newGkChoiceService.findById(gkDivide.getChoiceId());
		if(choice == null || (choice.getIsDeleted() != null && choice.getIsDeleted() == 1)) {
    		return errorFtl(map, "选课记录不存在或已被删除！");
    	}
		List<String> subjectIdList = newGkChoRelationService.findByChoiceIdAndObjectType(gkDivide.getUnitId(), choice.getId(), NewGkElectiveConstant.CHOICE_TYPE_01);
		if(CollectionUtils.isEmpty(subjectIdList)){
			return errorFtl(map, "选课的科目记录不存在或已被删除！");
		}
		//已经分班结束，直接到结果
		if (NewGkElectiveConstant.IF_1.equals(gkDivide.getStat())) {
			return "redirect:/newgkelective/"+divideId+"/divideClass/resultClassList";
		}
		List<NewGkDivideClass> oldlist = newGkDivideClassService
				.findByDivideIdAndClassType(gkDivide.getUnitId(),divideId,
						new String[] { NewGkElectiveConstant.CLASS_TYPE_2 }, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		boolean canEdit=true;
		if(CollectionUtils.isNotEmpty(oldlist)) {
			//不能修改，出现重新安排按钮
			canEdit=false;
		}
		boolean isDivideNow=false;
		if(isNowDivide(divideId)) {
			canEdit=false;
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(RedisUtils.get(NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId+"_type"))) {
				isDivideNow=true;
			}
		}else {
			String subtype=RedisUtils.get(NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId+"_type");
			
			if(StringUtils.isNotBlank(subtype) && NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subtype)) {
				if("error".equals(RedisUtils.get(NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId))){
					String submess=RedisUtils.get(NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId+"_mess");
					map.put("errorOpenDivide", submess);
				}
				RedisUtils.del(new String[] { NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId, NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId+"_type",NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId+"_mess" });
			}
		}
		map.put("canEdit", canEdit);
		map.put("isDivideNow", isDivideNow);
		
		int avg=findAvgByGradeId(gkDivide.getGradeId());
		
		List<Course> courselist = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIdList.toArray(new String[] {})),Course.class);
		//开设科目
		List<NewGkOpenSubject> openSubList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeInWithMaster(divideId, new String[] {NewGkElectiveConstant.SUBJECT_TYPE_A});
		Set<String> subIds = EntityUtils.getSet(openSubList, e->e.getSubjectId());
		//选考 key:subjectId
		Map<String,Map<String,Integer>> stuNumMap=new HashMap<String,Map<String,Integer>>();
		List<NewGKStudentRange> allStuRangeList = newGKStudentRangeService.findByDivideId(gkDivide.getUnitId(), divideId);
		if(CollectionUtils.isNotEmpty(allStuRangeList)){
			for(NewGKStudentRange range:allStuRangeList){
				if(!stuNumMap.containsKey(range.getSubjectId())){
					stuNumMap.put(range.getSubjectId(), new HashMap<String,Integer>());
				}
				Map<String, Integer> map1 = stuNumMap.get(range.getSubjectId());
				String key=range.getRange();
				if(!map1.containsKey(key)){
					map1.put(key, 1);
				}else{
					map1.put(key, map1.get(key)+1);
				}
			}
		}
		List<NewGKStudentRangeEx> allExList = newGKStudentRangeExService.findByDivideIdAndSubjectType(divideId, NewGkElectiveConstant.SUBJECT_TYPE_A);
		Map<String,NewGKStudentRangeEx> oldExMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(allExList)){
			for(NewGKStudentRangeEx ex:allExList){
				oldExMap.put(ex.getSubjectId()+"_"+ex.getRange(), ex);
			}
		}
		NewGKStudentRangeDto dto;
		NewGKStudentRangeEx ex;
		
		//行政科目
		List<NewGKStudentRangeDto> xzbList=new ArrayList<>();
		//走班科目--过滤开设科目id
		List<NewGKStudentRangeDto> jxbList=new ArrayList<>();
		
		for(Course c:courselist) {
			Map<String, Integer> map1 = stuNumMap.get(c.getId());
			if(map1==null || map1.size()==0) {
				continue;
			}
			dto=new NewGKStudentRangeDto();
			dto.setSubjectId(c.getId());
			dto.setSubjectName(c.getSubjectName());
			dto.setExList(new ArrayList<>());
			int stuNum=0;
			for(Entry<String, Integer> item:map1.entrySet()) {
				ex=new NewGKStudentRangeEx();
				ex.setDivideId(divideId);
				ex.setSubjectId(c.getId());
				ex.setStuNum(item.getValue());
				ex.setRange(item.getKey());
				int sn=item.getValue();
				stuNum=stuNum+sn;
				NewGKStudentRangeEx oldRange = oldExMap.get(c.getId()+"_"+item.getKey());
				//默认上下限5
				if(oldRange!=null) {
					ex.setLeastNum(oldRange.getLeastNum());
					ex.setMaximum(oldRange.getMaximum());
					ex.setClassNum(oldRange.getClassNum());
				}else {
					if(canEdit) {
						//只有可以修改时候才有初始化数据
						//四舍五入
						long cNum = Math.round(sn*1.0/avg);
						if(cNum<=1) {
							cNum=1;
							ex.setLeastNum(sn);
							ex.setMaximum(sn);
						}else {
							int avg1 = (sn-1)/(int)cNum+1;
							ex.setLeastNum(avg1-5);
							ex.setMaximum(avg1+5);
						}
						ex.setClassNum((int)cNum);
					}
					
				}
				dto.getExList().add(ex);
			}
			dto.setStuNum(stuNum);
			if(CollectionUtils.isNotEmpty(dto.getExList())){
				Collections.sort(dto.getExList(), new Comparator<NewGKStudentRangeEx>() {
					@Override
					public int compare(NewGKStudentRangeEx o1, NewGKStudentRangeEx o2) {
						if(o1 == null || o2 == null) {
							
							return 0;
						}
						if(o1.getSubjectId().equals(o2.getSubjectId())){
							return o1.getRange().compareTo(o2.getRange());
						}
						return o1.getSubjectId().compareTo(o2.getSubjectId());
					}
				});
			}
			
			if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(c.getSubjectCode())) {
				xzbList.add(dto);
			}else if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(c.getSubjectCode())) {
				xzbList.add(dto);
			}else {
				if(subIds.contains(c.getId())) {
					jxbList.add(dto);
				}
			}
		}
		map.put("jxbList", jxbList);
		if(NewGkElectiveConstant.DIVIDE_TYPE_09.equals(gkDivide.getOpenType())) {
			map.put("xzbList", xzbList);
			return "/newgkelective/singleRecomb/parametersetA.ftl";
		}else {
			return "/newgkelective/singleRecomb/parametersetA2.ftl";
		}
	}
	
	/**
	 * redis 缓存参数
	 * NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId
	 * start error success
	 * NewGkElectiveConstant.DIVIDE_CLASS+divideId+"_type"
	 * A:选考 B学考
	 * NewGkElectiveConstant.DIVIDE_CLASS+divideId+"_mess"
	 * 提示信息
	 */
	@ResponseBody
	@RequestMapping("/autoShuff")
	@ControllerInfo(value = "进入分班算法")
	public String autoShuff(@PathVariable String divideId,String subjectType,String weight) {
		
		final String redisKey= NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId;
		final String redisKeyMess= NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId+"_mess";
		final String redisKeyType= NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId+"_type";
		JSONObject on = new JSONObject();
		final NewGkDivide gkDivide = newGkDivideService.findById(divideId);
		
		if (gkDivide == null) {
			on.put("stat", "error");
			on.put("message", "该分班方案不存在");
			RedisUtils.del(new String[] { redisKey, redisKeyMess,redisKeyType });
			return JSON.toJSONString(on);
		}
		if (NewGkElectiveConstant.IF_1.equals(gkDivide.getStat())) {
			on.put("stat", "success");
			on.put("message", "已经分班成功");
			RedisUtils.del(new String[] { redisKey, redisKeyMess,redisKeyType });
			return JSON.toJSONString(on);
		}

		// 判断分班 状态
		if (RedisUtils.get(redisKey) == null) {
			//开始排班--不会超过一个小时
			RedisUtils.set(redisKey, "start",TIME_ONE_HOUR);
			RedisUtils.set(redisKeyMess, "进行中",TIME_ONE_HOUR);
			RedisUtils.set(redisKeyType, subjectType,TIME_ONE_HOUR);
		} else {
			on.put("stat", RedisUtils.get(redisKey));
			on.put("message", RedisUtils.get(redisKeyMess));
			if ("success".equals(RedisUtils.get(redisKey))
					|| "error".equals(RedisUtils.get(redisKey))) {
				RedisUtils.del(new String[] { redisKey, redisKeyMess,redisKeyType });
			} 
			return JSON.toJSONString(on);
		}
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Integer weightInt=null;
				if(StringUtils.isNotBlank(weight)) {
					try {
						weightInt=Integer.parseInt(weight);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
					autoA(gkDivide,weightInt);
				}else {
					autoB(gkDivide,weightInt);
				}
				
			}
			
		}).start();
		
		on.put("stat", RedisUtils.get(redisKey));
		on.put("message", RedisUtils.get(redisKeyMess));
		return JSON.toJSONString(on);
	}
	
	public void autoB(NewGkDivide gkDivide,Integer weight) {
		final String redisKey= NewGkElectiveConstant.DIVIDE_CLASS+"_"+gkDivide.getId();
		final String redisKeyMess= NewGkElectiveConstant.DIVIDE_CLASS+"_"+gkDivide.getId()+"_mess";
		
		
		//先判断A是不是已经安排--防止页面停留的位置上其他人已经重新安排A
		List<NewGkDivideClass> oldJxbAlist = newGkDivideClassService
				.findByDivideIdAndClassTypeSubjectTypeWithMaster(gkDivide.getUnitId(), 
						gkDivide.getId(), new String[] { NewGkElectiveConstant.CLASS_TYPE_2 }, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.SUBJECT_TYPE_A);
		if(CollectionUtils.isEmpty(oldJxbAlist)) {
			RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
			RedisUtils.set(redisKeyMess, "选考数据已经被重置，请先去安排选考",TIME_ONE_HOUR);
			return;
		}
		oldJxbAlist=oldJxbAlist.stream().filter(e->StringUtils.isNotBlank(e.getBatch())).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(oldJxbAlist)) {
			RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
			RedisUtils.set(redisKeyMess, "选考数据已经被重置，请先去安排选考",TIME_ONE_HOUR);
			return;
		}
		//选课科目
		List<Course> courseList=newGkChoRelationService.findChooseSubject(gkDivide.getChoiceId(), gkDivide.getUnitId());
		if (CollectionUtils.isEmpty(courseList)) {
			RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
			RedisUtils.set(redisKeyMess, "选课方案科目有问题",TIME_ONE_HOUR);
			return;
		}
		String wuliId=null;
		String lishiId=null;
		Map<String,Course> courseMap=new HashMap<>();
		for(Course c:courseList) {
			if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(c.getSubjectCode())) {
				wuliId=c.getId();
			}else if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(c.getSubjectCode())){
				lishiId=c.getId();
			}
			courseMap.put(c.getId(), c);
		}
		//开设科目
		List<NewGkOpenSubject> openSubList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeInWithMaster(gkDivide.getId(), new String[] {NewGkElectiveConstant.SUBJECT_TYPE_B});
		Set<String> subIds = EntityUtils.getSet(openSubList, e->e.getSubjectId());
		
		//参数设置
		List<NewGKStudentRangeEx> rangeExList = newGKStudentRangeExService
				.findByDivideIdAndSubjectType(gkDivide.getId(), NewGkElectiveConstant.SUBJECT_TYPE_B);
		if (CollectionUtils.isEmpty(rangeExList)) {
			RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
			RedisUtils.set(redisKeyMess, "没有参数设置",TIME_ONE_HOUR);
			return;
		}
		int bathB = gkDivide.getBatchCountTypeb()==null?3:gkDivide.getBatchCountTypeb();
		
		Map<String,ArrangeCapacityRange> rangeMap=new HashMap<>();
		for (NewGKStudentRangeEx rangeEx : rangeExList) {
			ArrangeCapacityRange range = new ArrangeCapacityRange();
			range.setClassNum(rangeEx.getClassNum());	
			range.setAvgNum((rangeEx.getMaximum()+rangeEx.getLeastNum())/2);
			range.setFolNum((rangeEx.getMaximum()-rangeEx.getLeastNum())/2);
			rangeMap.put(rangeEx.getSubjectId(), range);
		}
		
		Map<String,String> subByNewSub=new HashMap<>();
		int maxRoomCount=0;
		if(NewGkElectiveConstant.DIVIDE_TYPE_09.equals(gkDivide.getOpenType())) {
			boolean isFollowA=false;
			//key:stuId,value:subId_clazzId
			Map<String,String> stuSubClassMap=new HashMap<>();
			if(StringUtils.isNotBlank(gkDivide.getFollowType()) && gkDivide.getFollowType().indexOf(NewGkElectiveConstant.FOLLER_TYPE_B1)>-1) {
				isFollowA=true;
				//获取选考教学班
				List<NewGkDivideClass> oldlist = newGkDivideClassService
						.findByDivideIdAndClassTypeSubjectTypeWithMaster(gkDivide.getUnitId(), 
								gkDivide.getId(), new String[] { NewGkElectiveConstant.CLASS_TYPE_2 }, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.SUBJECT_TYPE_A);
				if(CollectionUtils.isEmpty(oldlist)) {
					RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
					RedisUtils.set(redisKeyMess, "选考结果未找到",TIME_ONE_HOUR);
					return;
				}
				for(NewGkDivideClass clazz:oldlist) {
					if(clazz.getSubjectIds().equals(wuliId) || clazz.getSubjectIds().equals(lishiId)) {
						if(CollectionUtils.isEmpty(clazz.getStudentList())) {
							continue;
						}
						//选考班数量
						maxRoomCount=maxRoomCount+1;
						for(String s:clazz.getStudentList()) {
							if(stuSubClassMap.containsKey(s)) {
								if(stuSubClassMap.get(s).indexOf(clazz.getSubjectIds())>-1) {
									RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
									RedisUtils.set(redisKeyMess, "存在学生"+(clazz.getSubjectIds().equals(wuliId)?"物理":"历史")+"科目在两个班级",TIME_ONE_HOUR);
									logger.info("存在学生id:"+s+(clazz.getSubjectIds().equals(wuliId)?"物理":"历史")+"科目在两个班级");
									return;
								}else {
									RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
									RedisUtils.set(redisKeyMess, "存在学生既在物理班，又在历史班",TIME_ONE_HOUR);
									logger.info("存在学生id:"+s+"既在物理班，又在历史");
									return;
								}
							}else {
								stuSubClassMap.put(s, clazz.getSubjectIds()+"_"+clazz.getId());
							}
						}
						
					}
				}
				
				
			}
			//学生选课数据
			List<NewGkDivideStusub> chooseBList = newGkDivideStusubService.findByDivideIdWithMaster(gkDivide.getId(), NewGkElectiveConstant.SUBJECT_TYPE_B, null);
			if(CollectionUtils.isEmpty(chooseBList)) {
				RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
				RedisUtils.set(redisKeyMess, "未找到学生选课数据",TIME_ONE_HOUR);
				return;
			}
			
			//物理历史每个班级数量
			Map<String,Integer> clazzANum=new HashMap<>();
			
			Map<String,List<String>> stuChooseMap=new HashMap<>();
			for(NewGkDivideStusub sub:chooseBList) {
				stuChooseMap.put(sub.getStudentId(), new ArrayList<>());
				if(sub.getSubjectIds().indexOf(wuliId)>-1 || sub.getSubjectIds().indexOf(lishiId)>-1) {
					
				}else {
					RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
					RedisUtils.set(redisKeyMess, sub.getStudentName()+"选课数据不存在物理与历史",TIME_ONE_HOUR);
					return;
				}
				String[] arr = sub.getSubjectIds().split(",");
				for(String s:arr) {
					//取反 历史AclassId---物理id
					if(isFollowA && (s.equals(wuliId) || s.equals(lishiId))) {
						String relSub="";//对应选考班取反
						if(s.equals(wuliId)) {
							relSub=lishiId;
						}else {
							relSub=wuliId;
						}
						if(!stuSubClassMap.containsKey(sub.getStudentId())) {
							RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
							RedisUtils.set(redisKeyMess, sub.getStudentName()+"物理或历史未安排选考班",TIME_ONE_HOUR);
							return;
						}
						//以行政班作为科目id
						String xId=stuSubClassMap.get(sub.getStudentId()).split("_")[0];
						String cId=stuSubClassMap.get(sub.getStudentId()).split("_")[1];
						if(!xId.equals(relSub)) {
							RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
							RedisUtils.set(redisKeyMess, sub.getStudentName()+"物理或历史未安排选考班",TIME_ONE_HOUR);
							return;
						}
						stuChooseMap.get(sub.getStudentId()).add(cId);
						if(!subByNewSub.containsKey(cId)) {
							subByNewSub.put(cId, s);//最终学考科目
						}
						if(clazzANum.containsKey(cId)) {
							clazzANum.put(cId, clazzANum.get(cId)+1);
						}else {
							clazzANum.put(cId, 1);
						}
						
					}else {
						if(subIds.contains(s)) {
							stuChooseMap.get(sub.getStudentId()).add(s);
							if(!subByNewSub.containsKey(s)) {
								subByNewSub.put(s, s);
							}
						}
						
					}
				}
			}
			
			if(clazzANum.size()>0) {
				for(Entry<String, Integer> item:clazzANum.entrySet()) {
					ArrangeCapacityRange range = new ArrangeCapacityRange();
					range.setClassNum(1);	
					range.setAvgNum(item.getValue());
					range.setFolNum(0);
					rangeMap.put(item.getKey(), range);
				}
			}
			
			Set<String> wlIdsSet=new HashSet<>();
			autoArrange(gkDivide, wlIdsSet, stuChooseMap, rangeMap, bathB, maxRoomCount, NewGkElectiveConstant.SUBJECT_TYPE_B, subByNewSub, courseMap,weight);
			
		}else {
			//学生选课数据
			List<NewGkDivideStusub> chooseBList = newGkDivideStusubService.findByDivideIdWithMaster(gkDivide.getId(), NewGkElectiveConstant.SUBJECT_TYPE_B, null);
			if(CollectionUtils.isEmpty(chooseBList)) {
				RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
				RedisUtils.set(redisKeyMess, "未找到学生选课数据",TIME_ONE_HOUR);
				return;
			}
			List<NewGkDivideClass> oldlist = newGkDivideClassService.findByDivideIdAndClassType(gkDivide.getUnitId(), gkDivide.getId(), new String[] {NewGkElectiveConstant.CLASS_TYPE_1}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			maxRoomCount=oldlist.size();
			if(StringUtils.isNotBlank(gkDivide.getFollowType()) && gkDivide.getFollowType().indexOf(NewGkElectiveConstant.FOLLER_TYPE_B2)>-1) {
				//跟行政班上课
				bathB=bathB-1;
				Map<String,List<String>> stuChooseMap=new HashMap<>();
				for(NewGkDivideStusub sub:chooseBList) {
					stuChooseMap.put(sub.getStudentId(), new ArrayList<>());
					String[] arr = sub.getSubjectIds().split(",");
					for(String s:arr) {
						if(s.equals(wuliId) || s.equals(lishiId)) {
							continue;
						}
						if(subIds.contains(s)) {
							stuChooseMap.get(sub.getStudentId()).add(s);
							if(!subByNewSub.containsKey(s)) {
								subByNewSub.put(s, s);
							}
						}
					}
				}
				Set<String> wlIdsSet=new HashSet<>();
				autoArrange(gkDivide, wlIdsSet, stuChooseMap, rangeMap, bathB, maxRoomCount, NewGkElectiveConstant.SUBJECT_TYPE_B, subByNewSub, courseMap,weight);
			}else if(StringUtils.isNotBlank(gkDivide.getFollowType()) && gkDivide.getFollowType().indexOf(NewGkElectiveConstant.FOLLER_TYPE_B1)>-1) {
				//分层 批次点
				List<NewGkDivideClass> oldlist3 = newGkDivideClassService
						.findByDivideIdAndClassTypeSubjectTypeWithMaster(gkDivide.getUnitId(), 
								gkDivide.getId(), new String[] { NewGkElectiveConstant.CLASS_TYPE_3 }, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, null);
				if(CollectionUtils.isEmpty(oldlist3)) {
					RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
					RedisUtils.set(redisKeyMess, "选考班结果",TIME_ONE_HOUR);
					return;
				}
				//key:stuId,value:subId_clazzId
				Map<String,String> stuSubClassMap=new HashMap<>();
				for(NewGkDivideClass clazz:oldlist3) {
					if(CollectionUtils.isEmpty(clazz.getStudentList())) {
						continue;
					}
					for(String s:clazz.getStudentList()) {
						if(stuSubClassMap.containsKey(s)) {
							if(stuSubClassMap.get(s).indexOf(clazz.getSubjectIds())>-1) {
								RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
								RedisUtils.set(redisKeyMess, "存在学生"+(clazz.getSubjectIds().equals(wuliId)?"物理":"历史")+"科目在两个班级",TIME_ONE_HOUR);
								logger.info("存在学生id:"+s+(clazz.getSubjectIds().equals(wuliId)?"物理":"历史")+"科目在两个班级");
								return;
							}else {
								RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
								RedisUtils.set(redisKeyMess, "存在学生既在物理班，又在历史班",TIME_ONE_HOUR);
								logger.info("存在学生id:"+s+"既在物理班，又在历史");
								return;
							}
						}else {
							stuSubClassMap.put(s, clazz.getSubjectIds()+"_"+clazz.getId());
						}
					}
				}
				//物理历史每个班级数量
				Map<String,Integer> clazzANum=new HashMap<>();
				
				Map<String,List<String>> stuChooseMap=new HashMap<>();
				for(NewGkDivideStusub sub:chooseBList) {
					stuChooseMap.put(sub.getStudentId(), new ArrayList<>());
					if(sub.getSubjectIds().indexOf(wuliId)>-1 || sub.getSubjectIds().indexOf(lishiId)>-1) {
						
					}else {
						RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
						RedisUtils.set(redisKeyMess, sub.getStudentName()+"选课数据不存在物理与历史",TIME_ONE_HOUR);
						return;
					}
					String[] arr = sub.getSubjectIds().split(",");
					for(String s:arr) {
						//取反 历史AclassId---物理id
						if(s.equals(wuliId) || s.equals(lishiId)) {
							String relSub="";//对应选考班取反
							if(s.equals(wuliId)) {
								relSub=lishiId;
							}else {
								relSub=wuliId;
							}
							if(!stuSubClassMap.containsKey(sub.getStudentId())) {
								RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
								RedisUtils.set(redisKeyMess, sub.getStudentName()+"物理或历史未安排选考班",TIME_ONE_HOUR);
								return;
							}
							//以行政班作为科目id
							String xId=stuSubClassMap.get(sub.getStudentId()).split("_")[0];
							String cId=stuSubClassMap.get(sub.getStudentId()).split("_")[1];
							if(!xId.equals(relSub)) {
								RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
								RedisUtils.set(redisKeyMess, sub.getStudentName()+"物理或历史未安排选考班",TIME_ONE_HOUR);
								return;
							}
							stuChooseMap.get(sub.getStudentId()).add(cId);
							if(!subByNewSub.containsKey(cId)) {
								subByNewSub.put(cId, s);//最终学考科目
							}
							if(clazzANum.containsKey(cId)) {
								clazzANum.put(cId, clazzANum.get(cId)+1);
							}else {
								clazzANum.put(cId, 1);
							}
							
						}else {
							if(subIds.contains(s)) {
								stuChooseMap.get(sub.getStudentId()).add(s);
								if(!subByNewSub.containsKey(s)) {
									subByNewSub.put(s, s);
								}
							}
							
						}
					}
				}
				
				if(clazzANum.size()>0) {
					for(Entry<String, Integer> item:clazzANum.entrySet()) {
						ArrangeCapacityRange range = new ArrangeCapacityRange();
						range.setClassNum(1);	
						range.setAvgNum(item.getValue());
						range.setFolNum(0);
						rangeMap.put(item.getKey(), range);
					}
				}
				
				Set<String> wlIdsSet=new HashSet<>();
				autoArrange(gkDivide, wlIdsSet, stuChooseMap, rangeMap, bathB, maxRoomCount, NewGkElectiveConstant.SUBJECT_TYPE_B, subByNewSub, courseMap,weight);
			}else {
				Map<String,List<String>> stuChooseMap=new HashMap<>();
				for(NewGkDivideStusub sub:chooseBList) {
					stuChooseMap.put(sub.getStudentId(), new ArrayList<>());
					String[] arr = sub.getSubjectIds().split(",");
					for(String s:arr) {
						if(subIds.contains(s)) {
							stuChooseMap.get(sub.getStudentId()).add(s);
							if(!subByNewSub.containsKey(s)) {
								subByNewSub.put(s, s);
							}
						}
					}
				}
				Set<String> wlIdsSet=new HashSet<>();
				autoArrange(gkDivide, wlIdsSet, stuChooseMap, rangeMap, bathB, maxRoomCount, NewGkElectiveConstant.SUBJECT_TYPE_B, subByNewSub, courseMap,weight);
			}
			
			
		}
		
	}
	/**
	 * 3+1+2选考分班
	 * @param gkDivide
	 */
	public void autoA(NewGkDivide gkDivide,Integer weight) {
		
		
		final String redisKey= NewGkElectiveConstant.DIVIDE_CLASS+"_"+gkDivide.getId();
		final String redisKeyMess= NewGkElectiveConstant.DIVIDE_CLASS+"_"+gkDivide.getId()+"_mess";
		//1、获取单科分层学生数据
		List<NewGKStudentRange> stuRangelist = newGKStudentRangeService
				.findByDivideId(gkDivide.getUnitId(), gkDivide.getId());
		
		//参数设置
		List<NewGKStudentRangeEx> rangeExList = newGKStudentRangeExService
				.findByDivideIdAndSubjectType(gkDivide.getId(), NewGkElectiveConstant.SUBJECT_TYPE_A);
		if (CollectionUtils.isEmpty(rangeExList)) {
			RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
			RedisUtils.set(redisKeyMess, "没有参数设置",TIME_ONE_HOUR);
			return;
		}
		int bathA = gkDivide.getBatchCountTypea()==null?3:gkDivide.getBatchCountTypea();
		
		//选课科目
		List<Course> courseList=newGkChoRelationService.findChooseSubject(gkDivide.getChoiceId(), gkDivide.getUnitId());
		if (CollectionUtils.isEmpty(courseList)) {
			RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
			RedisUtils.set(redisKeyMess, "选课方案科目有问题",TIME_ONE_HOUR);
			return;
		}
		Set<String> wlIdsSet=new HashSet<>();
		Map<String,Course> subjectNameMap=new HashMap<>();
		for(Course c:courseList) {
			if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(c.getSubjectCode())) {
				wlIdsSet.add(c.getId());
			}else if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(c.getSubjectCode())){
				wlIdsSet.add(c.getId());
			}
			subjectNameMap.put(c.getId(), c);
		}
		//开设科目
		List<NewGkOpenSubject> openSubList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeInWithMaster(gkDivide.getId(), new String[] {NewGkElectiveConstant.SUBJECT_TYPE_A});
		Set<String> subIds = EntityUtils.getSet(openSubList, e->e.getSubjectId());
		
		List<NewGkDivideStusub> list = newGkDivideStusubService.findByDivideIdWithMaster(gkDivide.getId(), NewGkElectiveConstant.SUBJECT_TYPE_A, null);
		if(CollectionUtils.isEmpty(list)) {
			RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
			RedisUtils.set(redisKeyMess, "没有找到学生选课数据",TIME_ONE_HOUR);
			return;
		}
		
		Map<String,ArrangeCapacityRange> rangeMap=new HashMap<>();
		int maxRoomCount=0;
		if(NewGkElectiveConstant.DIVIDE_TYPE_09.equals(gkDivide.getOpenType())) {
			if(CollectionUtils.isEmpty(stuRangelist)) {
				//没有单科分层数据
				RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
				RedisUtils.set(redisKeyMess, "没有找到学生单科分层数据",TIME_ONE_HOUR);
				return;
			}
			//key:stuId,value:subIds_A
			Map<String,List<String>> stuChooseMap=new HashMap<>();
			//key:subId_A,value:subId
			Map<String,String> subIdsByNowIds=new HashMap<>();
			for(NewGKStudentRange range:stuRangelist) {
				if((!wlIdsSet.contains(range.getSubjectId())) && (!subIds.contains(range.getSubjectId()))) {
					continue;
				}
				if(!subIdsByNowIds.containsKey(range.getSubjectId()+range.getRange())) {
					subIdsByNowIds.put(range.getSubjectId()+range.getRange(), range.getSubjectId());
				}
				if(stuChooseMap.containsKey(range.getStudentId())) {
					stuChooseMap.get(range.getStudentId()).add(range.getSubjectId()+range.getRange());
				}else {
					stuChooseMap.put(range.getStudentId(), new ArrayList<>());
					stuChooseMap.get(range.getStudentId()).add(range.getSubjectId()+range.getRange());
				}
			}
			//maxRoomCount 默认取开设行政班数量--09模式行政班数量是物理+历史开设班级数量;后续如果数量超过范围 可以增加
			
			for (NewGKStudentRangeEx rangeEx : rangeExList) {
				if(wlIdsSet.contains(rangeEx.getSubjectId())) {
					maxRoomCount=maxRoomCount+rangeEx.getClassNum();
				}
				ArrangeCapacityRange range = new ArrangeCapacityRange();
				range.setClassNum(rangeEx.getClassNum());	
				range.setAvgNum((rangeEx.getMaximum()+rangeEx.getLeastNum())/2);
				range.setFolNum((rangeEx.getMaximum()-rangeEx.getLeastNum())/2);
				rangeMap.put(rangeEx.getSubjectId()+rangeEx.getRange(), range);
			}
			
			autoArrange(gkDivide, wlIdsSet, stuChooseMap, rangeMap, bathA, maxRoomCount, NewGkElectiveConstant.SUBJECT_TYPE_A, subIdsByNowIds, subjectNameMap,weight);
			
		}else if(NewGkElectiveConstant.DIVIDE_TYPE_11.equals(gkDivide.getOpenType())) {
		
			//key:stuId,value:subIds_A
			Map<String,List<String>> stuChooseMap=new HashMap<>();
			//key:subId_A,value:subId
			Map<String,String> subIdsByNowIds=new HashMap<>();
			if(CollectionUtils.isNotEmpty(stuRangelist)) {
				for(NewGKStudentRange range:stuRangelist) {
					//如果不小心物理历史科目也保存进去
					if(wlIdsSet.contains(range.getSubjectId())) {
						continue;
					}
					if(!subIds.contains(range.getSubjectId())) {
						continue;
					}
					if(!subIdsByNowIds.containsKey(range.getSubjectId()+range.getRange())) {
						subIdsByNowIds.put(range.getSubjectId()+range.getRange(), range.getSubjectId());
					}
					if(stuChooseMap.containsKey(range.getStudentId())) {
						stuChooseMap.get(range.getStudentId()).add(range.getSubjectId()+range.getRange());
					}else {
						stuChooseMap.put(range.getStudentId(), new ArrayList<>());
						stuChooseMap.get(range.getStudentId()).add(range.getSubjectId()+range.getRange());
					}
				}
			}
			
			for (NewGKStudentRangeEx rangeEx : rangeExList) {
				ArrangeCapacityRange range = new ArrangeCapacityRange();
				range.setClassNum(rangeEx.getClassNum());	
				range.setAvgNum((rangeEx.getMaximum()+rangeEx.getLeastNum())/2);
				range.setFolNum((rangeEx.getMaximum()-rangeEx.getLeastNum())/2);
				rangeMap.put(rangeEx.getSubjectId()+rangeEx.getRange(), range);
			}
			
			Map<String, NewGkDivideStusub> subsByStuId = EntityUtils.getMap(list, e->e.getStudentId());
			List<NewGkDivideClass> oldlist = newGkDivideClassService.findByDivideIdAndClassType(gkDivide.getUnitId(), gkDivide.getId(), new String[] {NewGkElectiveConstant.CLASS_TYPE_1}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			maxRoomCount=oldlist.size();
			if(StringUtils.isNotBlank(gkDivide.getFollowType()) && gkDivide.getFollowType().indexOf(NewGkElectiveConstant.FOLLER_TYPE_A2)>-1) {
				//只需要安排剩下4门
				bathA=bathA-1;
				
			}else {
				//根据组合数据取得学生
				List<NewGkDivideClass> list3 = newGkDivideClassService.findByDivideIdAndClassType(gkDivide.getUnitId(), gkDivide.getId(), new String[] {NewGkElectiveConstant.CLASS_TYPE_3}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
				if(CollectionUtils.isEmpty(list3)) {
					RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
					RedisUtils.set(redisKeyMess, "没有找到历史物理的班级",TIME_ONE_HOUR);
					return;
				}
				//list3 下学生id
				for(NewGkDivideClass s:list3) {
					if(CollectionUtils.isEmpty(s.getStudentList())) {
						continue;
					}
					int size=0;
					for(String ss:s.getStudentList()) {
						if(subsByStuId.containsKey(ss) && subsByStuId.get(ss).getSubjectIds().indexOf(s.getSubjectIds())>-1) {
							size++;
							if(stuChooseMap.containsKey(ss)) {
								stuChooseMap.get(ss).add(s.getId());
							}else {
								stuChooseMap.put(ss, new ArrayList<>());
								stuChooseMap.get(ss).add(s.getId());
							}
						}
					}
					if(size>0) {
						subIdsByNowIds.put(s.getId(), s.getSubjectIds());
						ArrangeCapacityRange range = new ArrangeCapacityRange();
						range.setClassNum(1);	
						range.setAvgNum(s.getStudentList().size());
						range.setFolNum(0);
						rangeMap.put(s.getId(), range);
					}
				}
			}
			
			autoArrange(gkDivide, wlIdsSet, stuChooseMap, rangeMap, bathA, maxRoomCount, NewGkElectiveConstant.SUBJECT_TYPE_A, subIdsByNowIds, subjectNameMap,weight);
		}
	}
	
	
	private void autoArrange(NewGkDivide divide,Set<String> wlSet, 
			Map<String,List<String>> stuChooseMap,
			Map<String, ArrangeCapacityRange> rangeMap, int bath,int maxRoomCount,
			String subjectType,Map<String, String> relateSubjectId,
			Map<String, Course> subjectNameMap,Integer weight) {
		
			String redisKey= NewGkElectiveConstant.DIVIDE_CLASS+"_"+divide.getId();
			String redisKeyMess= NewGkElectiveConstant.DIVIDE_CLASS+"_"+divide.getId()+"_mess";
		
			String subjectTypeName;
			boolean isA=false;
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)){
				isA=true;
				subjectTypeName="选考";
			}else{
				subjectTypeName="学考";
			}
			
			ShufflingApp shufflingApp=new ShufflingApp();
			
			ShufflingAppDto shufflingAppDto = new ShufflingAppDto();
			
			List<List<String>> pre1xList=new ArrayList<List<String>>();
			shufflingAppDto.setPre1XList(pre1xList);
			
			List<List<String>> sectionSizeList = new ArrayList<List<String>>();
			List<List<String>> studentCourseSelectionList=new ArrayList<List<String>>();
			
			
			//用于验证参数准确性
			Map<String, Integer> courseSectionSizeClassMap=new HashMap<String, Integer>();//页面维护开设班级数量
			//根据人数范围给定 获得最大可容纳学生人数
			Map<String,Integer> maxStuIdBySubId=new HashMap<>();
			
			int allOpenClassNum=0;
			for(Entry<String, ArrangeCapacityRange> range:rangeMap.entrySet()){
				String subId=range.getKey();
				ArrangeCapacityRange arrangeRange=range.getValue();
				List<String> arr=new ArrayList<String>();
				arr.add(subId);
				arr.add(""+arrangeRange.getAvgNum());
				arr.add(""+arrangeRange.getFolNum());
				arr.add(""+arrangeRange.getClassNum());
				if(arrangeRange.getClassNum()==1) {
					if(weight==null) {
						arr.add("8");
					}else {
						arr.add(weight+"");
					}
				}else {
					//暂时权重不维护-----物理与历史开像行政班
					if(wlSet.contains(subId.split("_")[0])) {
						arr.add("2");//此处权重稍微加一点
					}
				}
				sectionSizeList.add(arr);
				
				allOpenClassNum=allOpenClassNum+arrangeRange.getClassNum();
				courseSectionSizeClassMap.put(subId, arrangeRange.getClassNum());
				maxStuIdBySubId.put(subId, (arrangeRange.getAvgNum()+arrangeRange.getFolNum())*arrangeRange.getClassNum());
			}
			shufflingAppDto.setSectionSizeList(sectionSizeList);
			//每个科目的人数
			Map<String,Integer> stuNumBySubId=new HashMap<>();
			for(Entry<String, List<String>> stu:stuChooseMap.entrySet()) {
				List<String> chooseList=stu.getValue();
				if(CollectionUtils.isEmpty(chooseList)) {
					continue;
				}
				List<String> arr=new ArrayList<String>();
				String key=stu.getKey();
				
				arr.add(key);
				arr.addAll(chooseList);
				for(String subId:chooseList){
					if(!stuNumBySubId.containsKey(subId)) {
						stuNumBySubId.put(subId, 1);
					}else {
						stuNumBySubId.put(subId, stuNumBySubId.get(subId)+1);
					}
					if(!courseSectionSizeClassMap.containsKey(subId)){
						String realSubjectId = relateSubjectId.get(subId);
						String bestType="";
						if(isA) {
							if(!realSubjectId.equals(subId)){
								bestType=subId.substring(subId.length()-1);
							}
						}
						String name = subjectNameMap.get(realSubjectId).getSubjectName()+bestType;
						RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
						RedisUtils.set(redisKeyMess, name+"没有设置平均班级人数",TIME_ONE_HOUR);
						return;
					}
				}
				//如果批次点不够
				if(chooseList.size()>bath) {
					bath=chooseList.size();
				}
				studentCourseSelectionList.add(arr);
			}
			//验证参数合理性
			for(Entry<String, Integer> uu:stuNumBySubId.entrySet()) {
				String realSubjectId=relateSubjectId.get(uu.getKey());//正式id
				String bestType="";
				if(isA) {
					if(!realSubjectId.equals(uu.getKey())){
						bestType=uu.getKey().substring(uu.getKey().length()-1);
					}
				}
				String name = subjectNameMap.get(realSubjectId).getSubjectName()+bestType;
				if(!maxStuIdBySubId.containsKey(uu.getKey())) {
					RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
					RedisUtils.set(redisKeyMess, subjectTypeName+":"+name+"未设置范围",TIME_ONE_HOUR);
					return ;
				}
				if(uu.getValue()>maxStuIdBySubId.get(uu.getKey())) {
					RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
					RedisUtils.set(redisKeyMess, subjectTypeName+":"+name+"设置范围不合理，实际需要安排的学生人数："+uu.getValue()+",参数范围控制最大人数："+maxStuIdBySubId.get(uu.getKey()),TIME_ONE_HOUR);
					return ;
				}
			}
			shufflingAppDto.setTimeSlotCount(bath);
			//设置班级数量超过行政班数量
			if(allOpenClassNum>maxRoomCount*bath) {
				shufflingAppDto.setMaxRoomCount((allOpenClassNum-1)/bath+1);
			}else {
				shufflingAppDto.setMaxRoomCount(maxRoomCount);
			}
			shufflingAppDto.setStudentCourseSelectionList(studentCourseSelectionList);
			shufflingAppDto.setPre1XList(pre1xList);

			try {
				List<List<String>> result = shufflingApp.solve(shufflingAppDto);
				
				saveAOrB(divide, result, rangeMap,subjectType, relateSubjectId, subjectNameMap);
				RedisUtils.set(redisKey, "success");
				RedisUtils.set(redisKeyMess, subjectTypeName+"分班成功");
			} catch (IOException e) {
				e.printStackTrace();
				RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
				RedisUtils.set(redisKeyMess, subjectTypeName+"分班失败",TIME_ONE_HOUR);
				return ;
			}catch (Exception e) {
				e.printStackTrace();
				logger.info(e.getMessage());
				RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
				RedisUtils.set(redisKeyMess, subjectTypeName+"分班失败",TIME_ONE_HOUR);
				return ;
			}

	}
	
	
	private void saveAOrB(NewGkDivide divide, List<List<String>> result, Map<String, ArrangeCapacityRange> rangeMap,
			String subjectType, Map<String, String> relateSubjectId, Map<String, Course> subjectNameMap) {
		String subjectTypeName;
		if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)){
			subjectTypeName="选";
		}else{
			subjectTypeName="学";
		}
		
		//分班，按时间点+教学班ID
		//resultStudentList: {<studentID> <选课> <教学班ID> <时间点>}
		Map<String, List<List<String>>> jxbList=new HashMap<String, List<List<String>>>();
		for(List<String> ee:result){
			//教学班ID:subjectId+数字
			String key=ee.get(3)+"_"+ee.get(2)+"_"+ee.get(1);//T1+"_"+教学班ID+"_"+subjectId
			if(!jxbList.containsKey(key)){
				jxbList.put(key, new ArrayList<List<String>>());
			}
			jxbList.get(key).add(ee);
		}
		List<NewGkDivideClass> insertClassList = new ArrayList<NewGkDivideClass>();
		List<NewGkClassStudent> insertClassStuList = new ArrayList<NewGkClassStudent>();
		//用于科目班级数量
		Map<String, Integer> subjectNumMap = new HashMap<String, Integer>();
		Map<String,List<NewGkDivideClass>> classBySubj=new HashMap<>();
		//组装数据
		for(Entry<String, List<List<String>>> item:jxbList.entrySet()){
			//T1+"_"+教学班ID+"_"+subjectId
			String key=item.getKey();
			key.substring(0, key.length()-1);
			String[] tt = key.split("_");
			int tBath=Integer.parseInt(tt[0].substring(1));
			String subId=tt[2];
			
			String realSubjectId = relateSubjectId.get(subId);
			String bestType="";
			if(!realSubjectId.equals(subId) && subId.substring(0,subId.length()-1).equals(realSubjectId)) {
				bestType=subId.substring(subId.length()-1);
			}
			Set<String> stuids = new HashSet<String>();
			for(List<String> stu:item.getValue()){
				stuids.add(stu.get(0));
			}
			if(!subjectNumMap.containsKey(realSubjectId)) {
				subjectNumMap.put(realSubjectId, 1);
			}
			String className=subjectNameMap.get(realSubjectId).getSubjectName()+"-"+subjectTypeName+bestType;
			NewGkDivideClass classDto = toDivideClass(divide, realSubjectId, bestType, subjectType, stuids, tBath, className,subjectNumMap.get(realSubjectId), insertClassList, insertClassStuList);
			subjectNumMap.put(realSubjectId, subjectNumMap.get(realSubjectId)+1);
			if(!classBySubj.containsKey(classDto.getSubjectIds())) {
				classBySubj.put(classDto.getSubjectIds(), new ArrayList<>());
			}
			classBySubj.get(classDto.getSubjectIds()).add(classDto);
		}
		if(CollectionUtils.isNotEmpty(insertClassList)) {
			for(Entry<String, List<NewGkDivideClass>> item:classBySubj.entrySet()) {
				Collections.sort(item.getValue(),  new Comparator<NewGkDivideClass>() {
					@Override
					public int compare(NewGkDivideClass arg0, NewGkDivideClass arg1) {
						if(StringUtils.isNotBlank(arg0.getBestType())
								&& StringUtils.isNotBlank(arg1.getBestType())) {
							if(arg0.getBestType().equals(arg1.getBestType())) {
								return arg0.getBatch().compareTo(arg1.getBatch());
							}else {
								return arg0.getBestType().compareTo(arg1.getBestType());
							}
						}else if(StringUtils.isNotBlank(arg0.getBestType())) {
							return 1;
						}else if(StringUtils.isNotBlank(arg1.getBestType())) {
							return -1;
						}else {
							return arg0.getBatch().compareTo(arg1.getBatch());
						}
					}
					
				});
				int j=1;
				for(NewGkDivideClass c:item.getValue()) {
					String ss=c.getOrderId()+"班";
					if(c.getOrderId()<=9) {
						ss="0"+c.getOrderId()+"班";
					}
					c.setClassName(c.getClassName().replace(ss, j+"班"));
					c.setOrderId(j);
					j++;
				}
			}
			
		}
		
		
		List<NewGkDivideClass> oldlist;
		if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
			oldlist = newGkDivideClassService
					.findByDivideIdAndClassTypeSubjectType(divide.getUnitId(),
							divide.getId(), new String[] { NewGkElectiveConstant.CLASS_TYPE_2 }, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, null);
		}else {
			oldlist = newGkDivideClassService
					.findByDivideIdAndClassTypeSubjectType(divide.getUnitId(),
							divide.getId(), new String[] { NewGkElectiveConstant.CLASS_TYPE_2 }, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.SUBJECT_TYPE_B);
		}
		String[] delClassId=null;
		if(CollectionUtils.isNotEmpty(oldlist)) {
			delClassId=EntityUtils.getSet(oldlist, e->e.getId()).toArray(new String[0]);
		}
				
		//需要清空多有教学班 
		newGkDivideClassService.saveAllList(divide.getUnitId(), divide.getId(), delClassId, insertClassList, insertClassStuList, false);
		
	}
	
	
	public NewGkDivideClass toDivideClass(NewGkDivide divide, String subjectId,
			String bestType, String subjectType, Set<String> stuids, int batch,
			String className,int order,
			List<NewGkDivideClass> divideClassList,
			List<NewGkClassStudent> classStuList) {// 批次

		NewGkDivideClass classDto = initNewGkDivideClass(divide.getId());
		NewGkClassStudent teachClassStu;
		classDto.setSubjectIds(subjectId);
		classDto.setSubjectType(subjectType);
		classDto.setBestType(bestType);
		classDto.setBatch(batch+"");
		if(order>9) {
			classDto.setClassName(className+order+"班");
		}else {
			classDto.setClassName(className+"0"+order+"班");
		}
		
		classDto.setOrderId(order);
		divideClassList.add(classDto);
		// 教学班下学生
		for (String stuid : stuids) {
			teachClassStu=initClassStudent(divide.getUnitId(), divide.getId(), classDto.getId(), stuid);
			classStuList.add(teachClassStu);
		}
		return classDto;
	}
	private NewGkDivideClass initNewGkDivideClass(String divideId) {
		NewGkDivideClass classDto = new NewGkDivideClass();
		classDto.setId(UuidUtils.generateUuid());
		classDto.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
		classDto.setIsHand(NewGkElectiveConstant.IF_0);
		classDto.setDivideId(divideId);
		classDto.setCreationTime(new Date());
		classDto.setClassType(NewGkElectiveConstant.CLASS_TYPE_2);
		return classDto;
	}
	
	
	@RequestMapping("/resultA/page")
	@ControllerInfo(value = "选考结果")
	public String showResultA(@PathVariable String divideId,ModelMap map) {
		NewGkDivide gkDivide = newGkDivideService.findById(divideId);
		showResultAOrB(gkDivide,NewGkElectiveConstant.SUBJECT_TYPE_A, map);
		map.put("openType", gkDivide.getOpenType());
		
		return "/newgkelective/singleRecomb/resultJxbA.ftl";
	}
	
	
	@RequestMapping("/resultB/page")
	@ControllerInfo(value = "学考结果")
	public String showResultB(@PathVariable String divideId,ModelMap map) {
		NewGkDivide gkDivide = newGkDivideService.findById(divideId);
		showResultAOrB(gkDivide,NewGkElectiveConstant.SUBJECT_TYPE_B, map);
		map.put("openType", gkDivide.getOpenType());
		return "/newgkelective/singleRecomb/resultJxbB.ftl";
	}
	
	public void showResultAOrB(NewGkDivide gkDivide,String subjectType,ModelMap map) {
		map.put("gradeId", gkDivide.getGradeId());
		List<NewGkDivideClass> gkDivideClassList = newGkDivideClassService.
				findByDivideIdAndClassTypeSubjectType(gkDivide.getUnitId(), gkDivide.getId(), new String[] { NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_3 }, 
						true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, null);
		List<DivideResultDto> onlyXzbDtoList = new ArrayList<>();
		List<DivideResultDto> xzbDtoList = new ArrayList<>();
		List<DivideResultDto> jxbDtoList = new ArrayList<>();
		DivideResultDto dto;
		List<String> bathList=new ArrayList<>();
		
		if(CollectionUtils.isNotEmpty(gkDivideClassList)) {
			Set<String> subIds = EntityUtils.getSet(gkDivideClassList, e->e.getSubjectIds());
			List<Course> courselist = SUtils.dt(courseRemoteService.findBySubjectIdIn(subIds.toArray(new String[] {})),Course.class);
			Map<String, Course> subjectMap=EntityUtils.getMap(courselist, e->e.getId());
			
			Map<String, Map<String, List<NewGkDivideClass>>> subjectLevelMap = new HashMap<>();
			Map<String, List<NewGkDivideClass>> classBysubjectAMap = new HashMap<>();
			Map<String, List<NewGkDivideClass>> classBysubjectBMap = new HashMap<>();
			for(NewGkDivideClass divideClass:gkDivideClassList) {
				if(NewGkElectiveConstant.CLASS_TYPE_2.equals(divideClass.getClassType())
						&& (!subjectType.equals(divideClass.getSubjectType()))) {
					continue;
				}
				if(NewGkElectiveConstant.CLASS_TYPE_3.equals(divideClass.getClassType())){
					if(!classBysubjectAMap.containsKey(divideClass.getSubjectIds())) {
						classBysubjectAMap.put(divideClass.getSubjectIds(), new ArrayList<>());
					}
					classBysubjectAMap.get(divideClass.getSubjectIds()).add(divideClass);
					if(!classBysubjectBMap.containsKey(divideClass.getSubjectIdsB())) {
						classBysubjectBMap.put(divideClass.getSubjectIdsB(), new ArrayList<>());
					}
					classBysubjectBMap.get(divideClass.getSubjectIdsB()).add(divideClass);
					continue;
				}
				if(!bathList.contains(divideClass.getBatch())) {
					bathList.add(divideClass.getBatch());
				}
				String bestType = Optional.ofNullable(divideClass.getBestType()).orElse("V");
				
				if(!subjectLevelMap.containsKey(divideClass.getSubjectIds())) {
					subjectLevelMap.put(divideClass.getSubjectIds(), new TreeMap<>());
				}
				if(!subjectLevelMap.get(divideClass.getSubjectIds()).containsKey(bestType)) {
					subjectLevelMap.get(divideClass.getSubjectIds()).put(bestType, new ArrayList<>());
				}
				subjectLevelMap.get(divideClass.getSubjectIds()).get(bestType).add(divideClass);
			}
			if(gkDivide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_11)) {
				if(StringUtils.isNotBlank(gkDivide.getFollowType())){
					if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType) && gkDivide.getFollowType().indexOf(NewGkElectiveConstant.FOLLER_TYPE_A2)>-1 ) {
						for (Entry<String, List<NewGkDivideClass>> item: classBysubjectAMap.entrySet()) {
							String subId=item.getKey();
							List<NewGkDivideClass> values = item.getValue();
							Integer stuNum = values.stream().map(e->e.getStudentList().size())
									.reduce((e1,e2)->e1+e2)
									.orElse(0);
							dto = new DivideResultDto();
							dto.setCourseName(subjectMap.get(subId).getSubjectName());
							dto.setTotalNum(stuNum);
							dto.setaClassNum(values.size());//班级数量
							dto.setAsXzbClassList(new ArrayList<>());
							dto.getAsXzbClassList().addAll(values);
							onlyXzbDtoList.add(dto);
						}
					}else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subjectType) &&  gkDivide.getFollowType().indexOf(NewGkElectiveConstant.FOLLER_TYPE_B2)>-1){
						for (Entry<String, List<NewGkDivideClass>> item: classBysubjectBMap.entrySet()) {
							String subId=item.getKey();
							List<NewGkDivideClass> values = item.getValue();
							Integer stuNum = values.stream().map(e->e.getStudentList().size())
									.reduce((e1,e2)->e1+e2)
									.orElse(0);
							dto = new DivideResultDto();
							dto.setCourseName(subjectMap.get(subId).getSubjectName());
							dto.setTotalNum(stuNum);
							dto.setaClassNum(values.size());//班级数量
							dto.setAsXzbClassList(new ArrayList<>());
							NewGkDivideClass newClass;
							for(NewGkDivideClass n:values) {
								newClass=new NewGkDivideClass();
								newClass.setId(n.getId());
								newClass.setStudentList(n.getStudentList());
								String className="";
								if(n.getRelateId().split(",").length>1) {
									if(n.getClassName().indexOf("物理-选")>-1) {
										className=n.getClassName().replace("物理-选", "历史-学");
									}else if(n.getClassName().indexOf("历史-选")>-1){
										className=n.getClassName().replace("历史-选", "物理-学");
									}else {
										className=n.getClassName();
									}
								}else {
									if(n.getClassName().indexOf("物理选")>-1) {
										className=n.getClassName().replace("物理选", "历史学");
									}else if(n.getClassName().indexOf("历史选")>-1){
										className=n.getClassName().replace("历史选", "物理学");
									}else {
										className=n.getClassName();
									}
								}
								newClass.setClassName(className);
								dto.getAsXzbClassList().add(newClass);
							}
							//修改行政班名称
							onlyXzbDtoList.add(dto);
						}
					}
					
				}
				
			}
			
			for (String subjectId : subjectLevelMap.keySet()) {
				Map<String, List<NewGkDivideClass>> levelMap = subjectLevelMap.get(subjectId);
				Integer stuNum = levelMap.values().stream().flatMap(e->e.stream())
						.map(e->e.getStudentList().size())
						.reduce((e1,e2)->e1+e2)
						.orElse(0);
				dto = new DivideResultDto();
				dto.setCourseName(subjectMap.get(subjectId).getSubjectName());
				dto.setTotalNum(stuNum);
				dto.setLevelNum(levelMap.size());
				int classNum=0;
				dto.setLevelMap(levelMap);
				for (Entry<String, List<NewGkDivideClass>> entry : levelMap.entrySet()) {
					Map<String, List<NewGkDivideClass>> batchMap = entry.getValue().stream().collect(Collectors.groupingBy(NewGkDivideClass::getBatch));
					dto.getLevelBatchMap().put(entry.getKey(), batchMap);
					classNum=classNum+entry.getValue().size();
				}
				dto.setaClassNum(classNum);//班级数量
				if(gkDivide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_09)) {
					if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType) || (
							StringUtils.isNotBlank(gkDivide.getFollowType()) && gkDivide.getFollowType().indexOf(NewGkElectiveConstant.FOLLER_TYPE_B1)>-1)) {
						if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(subjectMap.get(subjectId).getSubjectCode()) || NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(subjectMap.get(subjectId).getSubjectCode())) {
							xzbDtoList.add(dto);
							continue;
						}
					}
					jxbDtoList.add(dto);
				}else {
					jxbDtoList.add(dto);
				}
			}
		}
		if(CollectionUtils.isNotEmpty(bathList)) {
			Collections.sort(bathList, (x,y)->x.compareTo(y));
		}
		map.put("onlyXzbDtoList", onlyXzbDtoList);
		map.put("bathList", bathList);
		map.put("xzbDtoList", xzbDtoList);
		map.put("jxbDtoList", jxbDtoList);
	}
	
	
	@RequestMapping("/parametersetB")
	@ControllerInfo(value = "参数设置")
	public String showparameterB(@PathVariable String divideId,ModelMap map) {
		NewGkDivide gkDivide = newGkDivideService.findById(divideId);
		map.put("divide", gkDivide);
		if(gkDivide == null || (gkDivide.getIsDeleted() != null && gkDivide.getIsDeleted() == 1)) {
    		return errorFtl(map, "分班记录不存在或已被删除！");
    	}
		map.put("gradeId",gkDivide.getGradeId());
		map.put("divideId", gkDivide.getId());
		NewGkChoice choice = newGkChoiceService.findById(gkDivide.getChoiceId());
		if(choice == null || (choice.getIsDeleted() != null && choice.getIsDeleted() == 1)) {
    		return errorFtl(map, "选课记录不存在或已被删除！");
    	}
		List<String> subjectIdList = newGkChoRelationService.findByChoiceIdAndObjectType(gkDivide.getUnitId(), choice.getId(), NewGkElectiveConstant.CHOICE_TYPE_01);
		if(CollectionUtils.isEmpty(subjectIdList)){
			return errorFtl(map, "选课的科目记录不存在或已被删除！");
		}
		//已经分班结束，直接到结果
		if (NewGkElectiveConstant.IF_1.equals(gkDivide.getStat())) {
			return "redirect:/newgkelective/"+divideId+"/divideClass/resultClassList";
		}
		
		List<NewGkDivideClass> oldlist = newGkDivideClassService.
				findByDivideIdAndClassTypeSubjectType(gkDivide.getUnitId(), divideId, new String[] { NewGkElectiveConstant.CLASS_TYPE_2 }, 
						false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.SUBJECT_TYPE_B);
		boolean canEdit=true;
		if(CollectionUtils.isNotEmpty(oldlist)) {
			//不能修改，出现重新安排按钮
			canEdit=false;
		}
		boolean isDivideNow=false;
		if(isNowDivide(divideId)) {
			canEdit=false;
			if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(RedisUtils.get(NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId+"_type"))) {
				isDivideNow=true;
			}
		}else {
			String subtype=RedisUtils.get(NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId+"_type");
			if(StringUtils.isNotBlank(subtype) && NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subtype)) {
				if("error".equals(RedisUtils.get(NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId))){
					String submess=RedisUtils.get(NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId+"_mess");
					map.put("errorOpenDivide", submess);
				}
				RedisUtils.del(new String[] { NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId, NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId+"_type",NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId+"_mess" });
			}
		}
		map.put("canEdit", canEdit);
		map.put("isDivideNow", isDivideNow);
		
		int avg=findAvgByGradeId(gkDivide.getGradeId());
		
		List<Course> courselist = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIdList.toArray(new String[] {})),Course.class);
		//开设科目
		List<NewGkOpenSubject> openSubList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeInWithMaster(divideId, new String[] {NewGkElectiveConstant.SUBJECT_TYPE_B});
		Set<String> subIds = EntityUtils.getSet(openSubList, e->e.getSubjectId());
		
		//学考 key:subjectId
		Map<String,Integer> stuNumMap=new HashMap<String,Integer>();
		List<NewGkDivideStusub> list = newGkDivideStusubService.findByDivideIdWithMaster(divideId, NewGkElectiveConstant.SUBJECT_TYPE_B, null);
		for(NewGkDivideStusub s:list) {
			String[] arr = s.getSubjectIds().split(",");
			for(String a:arr) {
				
				if(stuNumMap.containsKey(a)) {
					stuNumMap.put(a, stuNumMap.get(a)+1);
				}else {
					stuNumMap.put(a,1);
				}
			}
		}
		List<NewGKStudentRangeEx> allExList = newGKStudentRangeExService.findByDivideIdAndSubjectType(divideId, NewGkElectiveConstant.SUBJECT_TYPE_B);
		Map<String,NewGKStudentRangeEx> oldExMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(allExList)){
			for(NewGKStudentRangeEx ex:allExList){
				oldExMap.put(ex.getSubjectId(), ex);
			}
		}
		NewGKStudentRangeDto dto;
		NewGKStudentRangeEx ex;
		
		//行政科目
		List<NewGKStudentRangeDto> xzbList=new ArrayList<>();
		//走班科目--过滤开设科目id
		List<NewGKStudentRangeDto> jxbList=new ArrayList<>();
		
		for(Course c:courselist) {
			Integer num = stuNumMap.get(c.getId());
			if(num==null) {
				continue;
			}
			dto=new NewGKStudentRangeDto();
			dto.setSubjectId(c.getId());
			dto.setSubjectName(c.getSubjectName());
			dto.setExList(new ArrayList<>());
			dto.setStuNum(num);
			dto.setPngName(NewGkElectiveConstant.DEFAULT_PNG.get(c.getSubjectCode()));
			ex=new NewGKStudentRangeEx();
			ex.setDivideId(divideId);
			ex.setSubjectId(c.getId());
			ex.setStuNum(num);
			NewGKStudentRangeEx oldRange = oldExMap.get(c.getId());
			//默认上下限5
			if(oldRange!=null) {
				ex.setLeastNum(oldRange.getLeastNum());
				ex.setMaximum(oldRange.getMaximum());
				ex.setClassNum(oldRange.getClassNum());
				ex.setId(oldRange.getId());
			}else {
				if(canEdit) {
					//只有可以修改时候才有初始化数据
					//四舍五入
					long cNum = Math.round(num*1.0/avg);
					if(cNum<=1) {
						cNum=1;
						ex.setLeastNum(num);
						ex.setMaximum(num);
					}else {
						int avg1 = (num-1)/(int)cNum+1;
						ex.setLeastNum(avg1-5);
						ex.setMaximum(avg1+5);
					}
					ex.setClassNum((int)cNum);
				}
			}
			dto.getExList().add(ex);
			
			if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(c.getSubjectCode())) {
				xzbList.add(dto);
			}else if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(c.getSubjectCode())) {
				xzbList.add(dto);
			}else {
				if(subIds.contains(c.getId())) {
					jxbList.add(dto);
				}
			}
		}
		map.put("xzbList", xzbList);
		map.put("jxbList", jxbList);
		//第一次进来条件设置
		map.put("followType", gkDivide.getFollowType());
		if(NewGkElectiveConstant.DIVIDE_TYPE_09.equals(gkDivide.getOpenType())) {
			return "/newgkelective/singleRecomb/parametersetB.ftl";
		}else {
			return "/newgkelective/singleRecomb/parametersetB2.ftl";
		}
		
	}
	
	
	@ResponseBody
	@RequestMapping("/clearJxbOnly")
	@ControllerInfo(value = "智能分班清除教学班")
	public String clearJxbOnly(@PathVariable String divideId,String subjectType) {
		//清除所有教学班以及学考参数
		NewGkDivide divide = newGkDivideService.findOneWithMaster(divideId);
		if (divide == null) {
			return error("分班方案不存在,返回列表页进行操作");
		}
		if(isNowDivide(divideId)){
			//正在智能分班中
			return error("正在分班中，不适合操作");
		}
		try {
			newGkDivideService.deleteOpenJxb(divide.getUnitId(),divideId,subjectType);
		}catch (Exception e) {
			return error("操作失败");
		}
		return  success("操作成功");
		
	}
	
	@ResponseBody
	@RequestMapping("/clearJxb")
	@ControllerInfo(value = "重新进入算法")
	public String clearJxb(@PathVariable String divideId,String subjectType) {
		//清除所有教学班以及学考参数
		NewGkDivide divide = newGkDivideService.findOneWithMaster(divideId);
		if (divide == null) {
			return error("分班方案不存在,返回列表页进行操作");
		}
		if(isNowDivide(divideId)){
			//正在智能分班中
			return error("正在分班中，不适合操作");
		}
		try {
			newGkDivideService.deleteOpenJxbNext(divide.getUnitId(),divideId,subjectType);
		}catch (Exception e) {
			return error("操作失败");
		}
		return  success("操作成功");
		
	}
	
	
	@ResponseBody
	@RequestMapping("/finshDivide")
	@ControllerInfo(value = "完成分班")
	public String finshDivide(@PathVariable String divideId) {
		NewGkDivide gkDivide = newGkDivideService.findById(divideId);
		Integer batchCountTypea=gkDivide.getBatchCountTypea();
		Integer batchCountTypeb=gkDivide.getBatchCountTypeb();
		List<NewGkDivideClass> gkDivideClassList = newGkDivideClassService
				.findByDivideIdAndClassTypeWithMaster(gkDivide.getUnitId(),
						divideId,
						new String[] {NewGkElectiveConstant.CLASS_TYPE_2 }, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		if (CollectionUtils.isEmpty(gkDivideClassList)) {
			return error("没有安排班级");
		}
		Set<String> bathASet = gkDivideClassList.stream().filter(e->StringUtils.isNotBlank(e.getBatch())&&NewGkElectiveConstant.SUBJECT_TYPE_A.equals(e.getSubjectType())).map(e->e.getBatch()).collect(Collectors.toSet());
		Set<String> bathBSet = gkDivideClassList.stream().filter(e->StringUtils.isNotBlank(e.getBatch())&&NewGkElectiveConstant.SUBJECT_TYPE_B.equals(e.getSubjectType())).map(e->e.getBatch()).collect(Collectors.toSet());
		if(CollectionUtils.isNotEmpty(bathASet)) {
			 String[] arrA = bathASet.toArray(new String[0]);
			 Arrays.sort(arrA);
			 batchCountTypea=Integer.parseInt(arrA[arrA.length-1]);
		}
		if(CollectionUtils.isNotEmpty(bathBSet)) {
			 String[] arrB = bathBSet.toArray(new String[0]);
			 Arrays.sort(arrB);
			 batchCountTypeb=Integer.parseInt(arrB[arrB.length-1]);
		}
		
		boolean isXzbA=false;
		boolean isXzbB=false;
		if(StringUtils.isNotBlank(gkDivide.getFollowType())) {
			if(gkDivide.getFollowType().indexOf(NewGkElectiveConstant.FOLLER_TYPE_A2)>-1) {
				isXzbA=true;
			}
			if(gkDivide.getFollowType().indexOf(NewGkElectiveConstant.FOLLER_TYPE_B2)>-1) {
				isXzbB=true;
			}
		}
		
		List<NewGkDivideStusub> stuchooseList = newGkDivideStusubService.findByDivideIdWithMaster(divideId, NewGkElectiveConstant.SUBJECT_TYPE_A, null);
		List<String> subjectIdList = newGkChoRelationService.findByChoiceIdAndObjectType(gkDivide.getUnitId(), gkDivide.getChoiceId(), NewGkElectiveConstant.CHOICE_TYPE_01);
		List<Course> courselist = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIdList.toArray(new String[] {})),Course.class);
		Set<String> wlIds=new HashSet<>();
		for(Course c:courselist) {
			if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(c.getSubjectCode()) ||
					NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(c.getSubjectCode())	) {
				wlIds.add(c.getId());
			}
		}
		List<NewGkDivideClass> wlClassList=new ArrayList<>();
		Map<String,Map<String,String>> stuSubjectAMap=new HashMap<>();
		Map<String,Map<String,String>> stuSubjectBMap=new HashMap<>();
		for(NewGkDivideClass n:gkDivideClassList) {
			List<String> sList = n.getStudentList();
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(n.getSubjectType())) {
				if(wlIds.contains(n.getSubjectIds())) {
					wlClassList.add(n);
				}
				for(String s:sList) {
					if(!stuSubjectAMap.containsKey(s)) {
						stuSubjectAMap.put(s, new HashMap<>());
					}
					if(stuSubjectAMap.get(s).containsKey(n.getBatch())) {
						logger.info("存在学生"+s+"在选考"+n.getBatch()+"有重复上课");
						return error("存在学生在选考"+n.getBatch()+"有重复上课");
					}
					stuSubjectAMap.get(s).put(n.getBatch(), n.getSubjectIds());
				}
			}else {
				for(String s:sList) {
					if(!stuSubjectBMap.containsKey(s)) {
						stuSubjectBMap.put(s, new HashMap<>());
					}
					if(stuSubjectBMap.get(s).containsKey(n.getBatch())) {
						logger.info("存在学生"+s+"在学考"+n.getBatch()+"有重复上课");
						return error("存在学生在学考"+n.getBatch()+"有重复上课");
					}
					stuSubjectBMap.get(s).put(n.getBatch(), n.getSubjectIds());
				}
			}
		}
		
		
		List<NewGkOpenSubject> openSubList = newGkOpenSubjectService.findByDivideId(divideId);
		Set<String> openA=new HashSet<>();
		Set<String> openB=new HashSet<>();
		for(NewGkOpenSubject n:openSubList) {
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.contains(n.getSubjectType())) {
				openA.add(n.getSubjectId());
			}else if(NewGkElectiveConstant.SUBJECT_TYPE_B.contains(n.getSubjectType())) {
				openB.add(n.getSubjectId());
			}
		}
		for(NewGkDivideStusub s:stuchooseList) {
			String[] chooseIds = s.getSubjectIds().split(",");
			List<String> noChooseList=new ArrayList<>();
			noChooseList.addAll(subjectIdList);
			Map<String, String> saMap = stuSubjectAMap.get(s.getStudentId());
			Set<String> sA=new HashSet<>();
			if(saMap!=null) {
				sA = saMap.values().stream().collect(Collectors.toSet());
			}
			for(String c:chooseIds ) {
				noChooseList.remove(c);
				if(openA.contains(c)) {
					if(!sA.contains(c)) {
						if(isXzbA && wlIds.contains(c)) {
							
						}else {
							logger.info("存在学生"+s.getStudentName()+"存在选考"+c+"未安排");
							return error("存在学生"+s.getStudentName()+"存在选考未安排");
						}
					}
				}
			}
			Map<String, String> sbMap = stuSubjectBMap.get(s.getStudentId());
			Set<String> sB=new HashSet<>();
			if(sbMap!=null) {
				sB = sbMap.values().stream().collect(Collectors.toSet());
			}
			for(String c:noChooseList ) {
				if(openB.contains(c)) {
					if(!sB.contains(c)) {
						if(isXzbB && wlIds.contains(c)) {
							
						}else {
							logger.info("存在学生"+s.getStudentName()+"存在学考"+c+"未安排");
							return error("存在学生"+s.getStudentName()+"存在学考未安排");
						}
					}
				}
			}
		}
		if(gkDivide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_09)) {
			//保存行政班
			int index=1;
			List<NewGkDivideClass> insertClazzList=new ArrayList<>();
			List<NewGkClassStudent> insertStuList=new ArrayList<>();
			NewGkClassStudent teachClassStu;
			NewGkDivideClass xzbClass;
			for(NewGkDivideClass clazz:wlClassList) {
				xzbClass = initXzbClass(gkDivide.getId(),index);
				index++;
				insertClazzList.add(xzbClass);
				//学生
				for (String stuid : clazz.getStudentList()) {
					teachClassStu = initClassStudent(gkDivide.getUnitId(), gkDivide.getId(), xzbClass.getId(), stuid);
					insertStuList.add(teachClassStu);
				}
			}
			List<NewGkDivideClass> oldXZBList = newGkDivideClassService.findByDivideIdAndClassType(gkDivide.getUnitId(), divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_0}, false,  NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			String[] delClassId=null;
			if(CollectionUtils.isNotEmpty(oldXZBList)) {
				delClassId=EntityUtils.getList(oldXZBList, e->e.getId()).toArray(new String[0]);
			}
			try{
				
				newGkDivideClassService.saveAllList(gkDivide.getUnitId(), divideId, delClassId, insertClazzList, insertStuList, true);
				
			}catch(Exception e){
				return error("保存行政班失败");
			}
		}else if(NewGkElectiveConstant.DIVIDE_TYPE_11.equals(gkDivide.getOpenType())) {
			List<NewGkDivideClass> oldlist = newGkDivideClassService
					.findByDivideIdAndClassTypeWithMaster(gkDivide.getUnitId(),
							divideId,
							new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_3}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			Map<String, Course> courseMap = EntityUtils.getMap(courselist, e->e.getId());
			List<NewGkDivideClass> insertClassList=new ArrayList<>();
			List<NewGkClassStudent> insertStudentList=new ArrayList<>();
			String ss = initTwoClass(gkDivide, courseMap, oldlist, insertClassList, insertStudentList);
    		if(StringUtils.isNotBlank(ss)) {
    			return error(ss);
    		}
    		
    		List<NewGkDivideClass> teachClassList = oldlist.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())&&StringUtils.isBlank(e.getBatch())).collect(Collectors.toList());
            String[] oldTeachClassIds=null;
    		if (CollectionUtils.isNotEmpty(teachClassList)) {
                oldTeachClassIds = EntityUtils.getArray(teachClassList, NewGkDivideClass::getId, String[]::new);
            }
    		try {
	            newGkDivideClassService.saveAllList(gkDivide.getUnitId(), divideId,
	                    oldTeachClassIds, insertClassList, insertStudentList, false);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return error(e.getMessage());
	        }
		}
		try {
			
			
			gkDivide.setBatchCountTypea(batchCountTypea);
			gkDivide.setBatchCountTypeb(batchCountTypeb);
			gkDivide.setStat(NewGkElectiveConstant.IF_1);
			gkDivide.setModifyTime(new Date());
			newGkDivideService.save(gkDivide);
		}catch (Exception e) {
			e.printStackTrace();
			return error("修改状态失败");
		}
		
		return success("");
	}
	
	@ResponseBody
	@RequestMapping("/delhbJxb")
	@ControllerInfo(value = "删除合并班级")
	public String delhbJxb(@PathVariable String divideId,String jxbId) {
		try{
			if(StringUtils.isBlank(jxbId)) {
				return error("参数丢失");
			}
			newGkDivideClassService.deleteById(getLoginInfo().getUnitId(), divideId, jxbId);
		}catch(Exception e){
			return error("操作失败");
		}
		return success("");
	}
	
	@ResponseBody
	@RequestMapping("/clearSingleRecomd")
	@ControllerInfo(value = "清空数据")
	public String clearSingleRecomd(@PathVariable String divideId) {
		if(isNowDivide(divideId)) {
			return error("正在分班中，不能操作");
		}
		try{
			newGkDivideService.deleteOpenJxbNext(getLoginInfo().getUnitId(), divideId, null);
		}catch(Exception e){
			return error("操作失败");
		}
		return success("");
	}
	
	@ResponseBody
	@RequestMapping("/checkSingleRecomd")
	@ControllerInfo(value = "进入下一步前，验证学生是不是物理历史都有归属")
	public String checkSingleRecomd(@PathVariable String divideId) {
		NewGkDivide gkDivide = newGkDivideService.findById(divideId);
		List<NewGkDivideClass> oldlist = newGkDivideClassService.findByDivideIdAndClassType(gkDivide.getUnitId(), gkDivide.getId(), new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_3}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		if(CollectionUtils.isEmpty(oldlist)) {
			return error("未找到物理历史班级");
		}
		Map<String, NewGkDivideClass> xzbMap = oldlist.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_1)).collect(Collectors.toMap(NewGkDivideClass::getId, e->e));
		List<NewGkDivideClass> list3 = oldlist.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_3)).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(list3)) {
			return error("未找到物理历史班级");
		}
		
		List<NewGkDivideStusub> stuchooseList = newGkDivideStusubService.findByDivideIdWithMaster(divideId, NewGkElectiveConstant.SUBJECT_TYPE_A, null);
		Map<String, NewGkDivideStusub> subsByStuId = EntityUtils.getMap(stuchooseList, e->e.getStudentId());
		List<NewGkClassStudent> insertStuList=new ArrayList<>();
		Set<String> dels=new HashSet<>();
		int size=0;
		boolean isUserMorenClass=false;
		Set<String> userParents=new HashSet<String>();
		for(NewGkDivideClass s:list3) {
			dels.add(s.getId());
			if(!isUserMorenClass && userParents.contains(s.getParentId())) {
				isUserMorenClass=true;
			}else {
				userParents.add(s.getParentId());
			}
			List<String> stuIds=new ArrayList<>();
			String[] relaIds = s.getRelateId().split(",");
			for(String sr:relaIds) {
				NewGkDivideClass oldXzb = xzbMap.get(sr);
				if(oldXzb!=null && CollectionUtils.isNotEmpty(oldXzb.getStudentList())) {
					for(String ss:oldXzb.getStudentList()) {
						if(!subsByStuId.containsKey(ss)) {
							//学生未选
							continue;
						}
						if(subsByStuId.get(ss).getSubjectIds().indexOf(s.getSubjectIds())>-1) {
							stuIds.add(ss);
							NewGkClassStudent su = initClassStudent(gkDivide.getUnitId(), divideId, s.getId(), ss);
							insertStuList.add(su);
						}
					}
				}
			}
			if(CollectionUtils.isEmpty(stuIds)) {
				return error(s.getClassName()+"下没有学生");
			}
			size=size+stuIds.size();
			
		}
		if(stuchooseList.size()!=size) {
			return error("存在学生选课数据不是3+1+2模式，或者未安排物理或者历史");
		}
		try{
			newGkClassStudentService.saveOrSaveList(gkDivide.getUnitId(), divideId, dels.toArray(new String[0]), insertStuList);
		}catch(Exception e){
			return error("数据验证成功，保存学生数据失败");
		}
		//提醒数据问题
		if(isUserMorenClass) {
			return success("尽量通过合班方式，不要让同一个行政班同时开设物理，历史，否则容易造成排课时候场地不够");
		}
		return success("");
	}
	
	
	@ResponseBody
	@RequestMapping("/findClassList")
	@ControllerInfo(value = "合并")
	public String findClassList(@PathVariable String divideId,String xzbId) {
		JSONObject json = new JSONObject();
		JSONArray oneList = new JSONArray();
		JSONArray twoList = new JSONArray();
		
		List<NewGkDivideClass> oldClassList = newGkDivideClassService.findByDivideIdAndClassTypeWithMaster(getLoginInfo().getUnitId(), divideId, 
				new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_3}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<NewGkDivideClass> list_1 = oldClassList.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_1)).collect(Collectors.toList());
		List<NewGkDivideClass> list_3 = oldClassList.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_3)).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(list_1) || CollectionUtils.isEmpty(list_3)) {
			return json.toString();
		}
		Map<String, NewGkDivideClass> xzbMap = EntityUtils.getMap(list_1, e->e.getId());
		if(!xzbMap.containsKey(xzbId)) {
			return json.toString();
		}
		
		List<NewGkDivideStusub> stuchooseList = newGkDivideStusubService.findByDivideIdWithMaster(divideId, NewGkElectiveConstant.SUBJECT_TYPE_A, null);
		Map<String, NewGkDivideStusub> stuMap = EntityUtils.getMap(stuchooseList, e->e.getStudentId());
		JSONObject json1 = null;
		for(NewGkDivideClass item3:list_3) {
			if(StringUtils.isBlank(item3.getRelateId())) {
				continue;
			}
			json1 = new JSONObject();
			json1.put("classId", item3.getId());
			json1.put("className", item3.getClassName());
			json1.put("subjectId", item3.getSubjectIds());
			int stuNum=0;
			String[] oldRelateIds = item3.getRelateId().split(",");
			for(String id:oldRelateIds) {
				if(!xzbMap.containsKey(id)) {
					continue;
				}
				for(String s:xzbMap.get(id).getStudentList()) {
					if(stuMap.containsKey(s) && stuMap.get(s).getSubjectIds().indexOf(item3.getSubjectIds())>-1) {
						stuNum++;
					}
				}
			}
			json1.put("stunum", stuNum+"");
			if(xzbId.equals(item3.getParentId())) {
				oneList.add(json1);
			}else {
				twoList.add(json1);
			}
		}
		json.put("oneList", oneList);
		json.put("twoList", twoList);
		return json.toJSONString();
	}
	@ResponseBody
	@RequestMapping("/saveComjxb")
	@ControllerInfo(value = "合并保存")
	public String saveComjxb(@PathVariable String divideId,String fromId,String toId) {
		if(StringUtils.isBlank(fromId) || StringUtils.isBlank(toId)) {
			return error("参数丢失");
		}
		List<NewGkDivideClass> list = newGkDivideClassService.findListByIdsWithMaster(new String[] {fromId,toId});
		if(CollectionUtils.isEmpty(list) || list.size()!=2) {
			return error("班级不存在，请重新操作");
		}
		NewGkDivideClass fromClass = null;
		NewGkDivideClass toClass=null;
		for(NewGkDivideClass s:list) {
			if(s.getId().equals(fromId)) {
				fromClass=s;
			}else {
				toClass=s;
			}
		}
		toClass.setRelateId(toClass.getRelateId()+","+fromClass.getRelateId());
		toClass.setModifyTime(new Date());
		List<NewGkDivideClass> insertlist=new ArrayList<>();
		insertlist.add(toClass);
		try {
			newGkDivideClassService.saveAllList(getLoginInfo().getUnitId(), 
				divideId, new String[] {fromClass.getId()}, insertlist, null, false);
		}catch (Exception e) {
			e.printStackTrace();
			return error("合并失败");
		}
		return success("");
	}
	
	@RequestMapping("/oldXzbStudent/page")
	@ControllerInfo(value = "行政班学生")
	public String showXzbStudentList(@PathVariable String divideId,String clazzId,ModelMap map) {
		List<StudentResultDto> dtoList = new ArrayList<>();
		if(StringUtils.isNotBlank(clazzId)) {
			NewGkDivide gkDivide = newGkDivideService.findById(divideId);
			String unitId = getLoginInfo().getUnitId();
			NewGkDivideClass clazz = newGkDivideClassService.findByIdWithMaster(unitId, clazzId, true);
			if(clazz!=null) {
				StudentResultDto dto;
				if(CollectionUtils.isNotEmpty(clazz.getStudentList())) {
					List<Course> courseList = newGkChoRelationService.findChooseSubject(gkDivide.getChoiceId(), unitId);
					Map<String, Course> courseMap = EntityUtils.getMap(courseList, e->e.getId());
					List<NewGkDivideStusub> chooselist = newGkDivideStusubService.findListByStudentIdsWithMaster(divideId, NewGkElectiveConstant.SUBJECT_TYPE_A, clazz.getStudentList().toArray(new String[0]));
					for(NewGkDivideStusub stu:chooselist) {
						dto=new StudentResultDto();
						dto.setStudentName(stu.getStudentName());
						dto.setStudentCode(stu.getStudentCode());
						dto.setSex(stu.getStudentSex());
						String chooseSubjects="";
						for(String s:stu.getSubjectIds().split(",")) {
							chooseSubjects=chooseSubjects+"、"+courseMap.get(s).getSubjectName();
						}
						chooseSubjects=chooseSubjects.substring(1);
						dto.setChooseSubjects(chooseSubjects);
						dtoList.add(dto);
					}
				}
			}
			map.put("clazz", clazz);
		}
		map.put("dtoList", dtoList);
		map.put("divideId", divideId);
		return "/newgkelective/singleRecomb/studentList.ftl";
	}
	@ResponseBody
	@RequestMapping("/clearRange")
	@ControllerInfo(value = "清除分层数据")
	public String clearRange(@PathVariable String divideId) {
		NewGkDivide divide = newGkDivideService.findOneWithMaster(divideId);
		if (divide == null) {
			return error("分班方案不存在");
		}
		if(isNowDivide(divide.getId())) {
			return error("正在分班中");
		}else {
			List<NewGkDivideClass> jxbClassList = newGkDivideClassService.findByDivideIdAndClassTypeSubjectTypeWithMaster(divide.getUnitId(),
					divide.getId(), new String[] {NewGkElectiveConstant.CLASS_TYPE_2}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, null);
			if(CollectionUtils.isNotEmpty(jxbClassList)) {
				return error("已经进入下一步，不能操作");
			}
		}
		try {
			newGKStudentRangeService.deleteByDivideId(divide.getUnitId(), divide.getId());
		}catch(Exception e) {
			e.printStackTrace();
			return error("操作失败");
		}
		return success("");
	}
	@RequestMapping("/showJxbStuResult")
	@ControllerInfo(value = "显示学生数据")
	public String showJxbStuResult(@PathVariable String divideId,String teachClassId,String subjectType,ModelMap map) {
		if(StringUtils.isBlank(subjectType)) {
			return errorFtl(map,"参数不对");
		}
		map.put("divideId", divideId);
		//默认可以修改的是教学班班级
		NewGkDivide divide = newGkDivideService.findById(divideId);
		String unitId = getLoginInfo().getUnitId();
		NewGkDivideClass newGkDivideClass = newGkDivideClassService.findById(unitId, teachClassId, true);
		if(newGkDivideClass==null) {
			return errorFtl(map,"对应班级不存在");
		}
		boolean isCanEdit=true;
		if(newGkDivideClass.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_2)) {
			if(NewGkElectiveConstant.DIVIDE_TYPE_09.equals(divide.getOpenType()) && NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
				//3+1+2单科分层重组，学考跟随选考分组，有学考教学班  选考不能调整
				if(StringUtils.isNotBlank(divide.getFollowType()) && divide.getFollowType().indexOf(NewGkElectiveConstant.FOLLER_TYPE_B1)>-1) {
					List<NewGkDivideClass> oldlist = newGkDivideClassService.
							findByDivideIdAndClassTypeSubjectType(divide.getUnitId(), divideId, new String[] { NewGkElectiveConstant.CLASS_TYPE_2 }, 
									false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.SUBJECT_TYPE_B);
					if(CollectionUtils.isNotEmpty(oldlist)) {
						isCanEdit=false;
					}
				}
				
			}
		}else {
			if(newGkDivideClass.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_3) &&
					subjectType.equals(NewGkElectiveConstant.SUBJECT_TYPE_B)) {
				String className="";
				if(newGkDivideClass.getRelateId().split(",").length>1) {
					if(newGkDivideClass.getClassName().indexOf("物理-选")>-1) {
						className=newGkDivideClass.getClassName().replace("物理-选", "历史-学");
					}else if(newGkDivideClass.getClassName().indexOf("历史-选")>-1){
						className=newGkDivideClass.getClassName().replace("历史-选", "物理-学");
					}else {
						className=newGkDivideClass.getClassName();
					}
				}else {
					if(newGkDivideClass.getClassName().indexOf("物理选")>-1) {
						className=newGkDivideClass.getClassName().replace("物理选", "历史学");
					}else if(newGkDivideClass.getClassName().indexOf("历史选")>-1){
						className=newGkDivideClass.getClassName().replace("历史选", "物理学");
					}else {
						className=newGkDivideClass.getClassName();
					}
				}
				newGkDivideClass.setClassName(className);
				
			}
			isCanEdit=false;
		}
		List<StudentResultDto> studentResultDtoList=new ArrayList<>();
		List<String> stuIdsList = newGkDivideClass.getStudentList();
		if(CollectionUtils.isNotEmpty(stuIdsList)) {
			List<NewGkDivideStusub> list1 = newGkDivideStusubService.findListByStudentIdsWithMaster(divideId, subjectType, stuIdsList.toArray(new String[0]));
			List<Course> courseList =newGkChoRelationService.findChooseSubject(divide.getChoiceId(), unitId);
			Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
			StudentResultDto dto;
			List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeIn(divideId, new String[] {subjectType});
			Set<String> openSubIds=EntityUtils.getSet(openSubjectList, e->e.getSubjectId());
			if(CollectionUtils.isNotEmpty(list1)) {
				for(NewGkDivideStusub n:list1) {
					dto=new StudentResultDto();
					dto.setStudentId(n.getStudentId());
					dto.setStudentCode(n.getStudentCode());
					dto.setStudentName(n.getStudentName());
					dto.setSex(n.getStudentSex());
					dto.setClassName(n.getClassName());
					String[] arr = n.getSubjectIds().split(",");
					String chooseSubjects="";
					for(String s:arr) {
						if(openSubIds.contains(s)) {
							chooseSubjects=chooseSubjects+courseMap.get(s).getShortName();
						}
					}
					dto.setChooseSubjects(chooseSubjects);
					studentResultDtoList.add(dto);
				}
			}
		}
		
		map.put("jxbClass", newGkDivideClass);
		map.put("subjectType", subjectType);
		map.put("studentList", studentResultDtoList);
		map.put("isCanEdit", isCanEdit);
		return "/newgkelective/singleRecomb/studentJxbList.ftl";
	}
	@RequestMapping("/moveStudent")
	@ControllerInfo(value = "调整学生数据")
	public String moveStudent(@PathVariable String divideId,String studentId,String subjectType,String containType,ModelMap map) {
		if(StringUtils.isBlank(subjectType)) {
			return errorFtl(map,"参数不对");
		}
		List<NewGkDivideStusub> list1 = newGkDivideStusubService.findListByStudentIdsWithMaster(divideId, subjectType, new String[] {studentId});
		if(CollectionUtils.isEmpty(list1) || list1.size()!=1) {
			return errorFtl(map,"选课数据有误");
		}
		boolean isFilterBestType=false;
		if("1".equals(containType)) {
			isFilterBestType=true;
		}
		NewGkDivide divide = newGkDivideService.findById(divideId);
		String unitId = getLoginInfo().getUnitId();
		
		NewGkDivideStusub stuChoose=list1.get(0);
		StudentResultDto dto=new StudentResultDto();
		dto.setStudentId(stuChoose.getStudentId());
		dto.setStudentCode(stuChoose.getStudentCode());
		dto.setStudentName(stuChoose.getStudentName());
		dto.setSex(stuChoose.getStudentSex());
		dto.setClassName(stuChoose.getClassName());
		
		//学生所在对应subjectType的教学班数据
		List<NewGkClassStudent> list = newGkClassStudentService.findListByStudentId(unitId, divideId, studentId);
		Set<String> oldClassIds = EntityUtils.getSet(list,e->e.getClassId());
		if(CollectionUtils.isEmpty(oldClassIds)) {
			return errorFtl(map,"该学生教学班不存在");
		}
		List<NewGkDivideClass> stuOldClassList=newGkDivideClassService.findListByIdIn(oldClassIds.toArray(new String[0]));
		if(CollectionUtils.isEmpty(stuOldClassList)) {
			return errorFtl(map,"该学生教学班不存在");
		}
		//学生科目层次
		Map<String,String> subjectRangeMap=new HashMap<>();
		if(subjectType.equals(NewGkElectiveConstant.SUBJECT_TYPE_A)) {
			//科目显示对应的层次
			List<NewGKStudentRange> rangeList = newGKStudentRangeService.findByDivideIdAndStudentId(unitId, divideId,studentId);
			if(CollectionUtils.isNotEmpty(rangeList)) {
				subjectRangeMap=EntityUtils.getMap(rangeList, e->e.getSubjectId(), e->e.getRange());
			}
		}
		
		
		//获取所有教学班
		List<NewGkDivideClass> oldJxblist = newGkDivideClassService.
				findByDivideIdAndClassTypeSubjectType(divide.getUnitId(), divideId, new String[] { NewGkElectiveConstant.CLASS_TYPE_2 }, 
						true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, subjectType);
		
		List<Course> allCourseList=newGkChoRelationService.findChooseSubject(divide.getChoiceId(), unitId);
		Map<String,Course> courseMap=EntityUtils.getMap(allCourseList, e->e.getId());
		//原来选中科目
		List<String> bathList=new ArrayList<>();
		Map<String,String> chooseMap=new HashMap<>();
		List<String> subIds=new ArrayList<>();//可调整的科目
		
		List<String> bestSubIds=new ArrayList<>();
		//由于是3+1+2
		String subjectNames="";
		Set<String> wlIds=new HashSet<>();//就一个科目值
		Set<String> classType3Ids=new HashSet<>();//就一个历史班或者物理班
		Set<String> classType2AIds=new HashSet<>();//就一个班，用于下面B如果跟随选考
		for(NewGkDivideClass n:stuOldClassList) {
			if(subjectType.equals(n.getSubjectType()) && NewGkElectiveConstant.CLASS_TYPE_2.contains(n.getClassType())) {
				chooseMap.put(n.getBatch(), n.getId());
				bathList.add(n.getBatch());
				Course course = courseMap.get(n.getSubjectIds());
				if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(course.getSubjectCode()) ||
						NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(course.getSubjectCode())){
					wlIds.add(course.getId());
				}
				subjectNames=subjectNames+"、"+course.getSubjectName()+(subjectRangeMap.containsKey(n.getSubjectIds())?"("+subjectRangeMap.get(n.getSubjectIds())+"层)":"");
				subIds.add(n.getSubjectIds());
				
				bestSubIds.add(n.getSubjectIds()+"_"+(n.getBestType()==null?"V":n.getBestType()));
				
			}else if(NewGkElectiveConstant.CLASS_TYPE_3.contains(n.getClassType())) {
				classType3Ids.add(n.getId());
			}
			if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subjectType) && NewGkElectiveConstant.SUBJECT_TYPE_A.equals(n.getSubjectType())
					&& NewGkElectiveConstant.CLASS_TYPE_2.contains(n.getClassType())) {
				Course course = courseMap.get(n.getSubjectIds());
				if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(course.getSubjectCode()) ||
						NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(course.getSubjectCode())){
					classType2AIds.add(n.getId());
				}
			}
		}
		Set<String> sameStuIds=new HashSet<>();
		if(NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divide.getOpenType())
				&& StringUtils.isNotBlank(divide.getFollowType()) && CollectionUtils.isNotEmpty(classType3Ids)) {
			if(divide.getFollowType().indexOf(NewGkElectiveConstant.FOLLER_TYPE_A1)>-1
					|| divide.getFollowType().indexOf(NewGkElectiveConstant.FOLLER_TYPE_B1)>-1) {
				List<NewGkClassStudent> stuList = newGkClassStudentService.findListByClassIds(unitId, divideId, classType3Ids.toArray(new String[0]));
				sameStuIds=EntityUtils.getSet(stuList, e->e.getStudentId());
			}
		}else if(NewGkElectiveConstant.DIVIDE_TYPE_09.equals(divide.getOpenType())
				&& StringUtils.isNotBlank(divide.getFollowType()) && CollectionUtils.isNotEmpty(classType2AIds)){
			//09
			if(subjectType.equals(NewGkElectiveConstant.SUBJECT_TYPE_B) && divide.getFollowType().indexOf(NewGkElectiveConstant.FOLLER_TYPE_B1)>-1) {
				List<NewGkClassStudent> stuList = newGkClassStudentService.findListByClassIds(unitId, divideId, classType2AIds.toArray(new String[0]));
				sameStuIds=EntityUtils.getSet(stuList, e->e.getStudentId());
			}
		}
		
		dto.setChooseSubjects(subIds.stream().collect(Collectors.joining(",")));
		map.put("subjectNames", subjectNames.substring(1));
		
		Map<String,List<NewGkDivideClass>> classByBath=new HashMap<>();
		for(NewGkDivideClass n:oldJxblist) {
			//进行过滤
			if(!subIds.contains(n.getSubjectIds())) {
				continue;
			}
			//过滤跟随
			if(wlIds.contains(n.getSubjectIds()) && CollectionUtils.isNotEmpty(sameStuIds)) {
				if(CollectionUtils.isNotEmpty(n.getStudentList()) &&
						CollectionUtils.intersection(n.getStudentList(), sameStuIds).size()==0) {
					continue;
				}
			}
			//同等第
			if(isFilterBestType && !bestSubIds.contains(n.getSubjectIds()+"_"+(n.getBestType()==null?"V":n.getBestType()))) {
				continue;
			}
			if(!classByBath.containsKey(n.getBatch())) {
				classByBath.put(n.getBatch(), new ArrayList<>());
			}
			classByBath.get(n.getBatch()).add(n);
		}
		Collections.sort(bathList);
		map.put("bathList", bathList);
		map.put("chooseMap", chooseMap);
		map.put("classByBath", classByBath);
		map.put("subjectType", subjectType);
		map.put("studentResultDto", dto);
		return "/newgkelective/singleRecomb/studentMoveEdit.ftl";
	}
	@ResponseBody
	@RequestMapping("/saveMoveStudent")
	@ControllerInfo(value = "保存学生数据")
	public String saveMoveStudent(SaveMoveStudentDto moveDto) {
		String unitId=getLoginInfo().getUnitId();
		List<NewGkClassStudent> list = newGkClassStudentService.findListByStudentId(unitId, moveDto.getDivideId(), moveDto.getStudentId());
		Set<String> oldClassIds = EntityUtils.getSet(list,e->e.getClassId());
		if(CollectionUtils.isEmpty(oldClassIds)) {
			return error("该学生教学班不存在,请刷新后操作");
		}
		List<NewGkDivideClass> stuOldClassList=newGkDivideClassService.findListByIdIn(oldClassIds.toArray(new String[0]));
		if(CollectionUtils.isEmpty(stuOldClassList)) {
			return error("该学生教学班不存在,请刷新后操作");
		}
		Map<String, NewGkDivideClass> classMap = stuOldClassList.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_2) && e.getSubjectType().equals(moveDto.getSubjectType()))
				.collect(Collectors.toMap(NewGkDivideClass::getId, Function.identity(), (k1, k2) -> k1));
		Map<String,NewGkClassStudent> updateOldMap=new HashMap<>();
		for(NewGkClassStudent s:list) {
			if(classMap.containsKey(s.getClassId())) {
				if(!classMap.get(s.getClassId()).getSubjectType().equals(moveDto.getSubjectType())) {
					return error("数据错误");
				}
				updateOldMap.put(classMap.get(s.getClassId()).getSubjectIds(), s);
			}
		}
		String[] courseClassIds = moveDto.getCourseClassId();
		Map<String,String> newClassIdBysubId=new HashMap<>();
		Set<String> newClassIds=new HashSet<>();
		for(String s:courseClassIds) {
			String[] arr = s.split("_");
			newClassIdBysubId.put(arr[0], arr[1]);
			newClassIds.add(arr[1]);
		}
		List<NewGkDivideClass> newClassList=newGkDivideClassService.findListByIdIn(newClassIds.toArray(new String[0]));
		if(CollectionUtils.isEmpty(newClassList) || newClassList.size()!=newClassIds.size()) {
			return error("移动到的部分班级找不到");
		}
		if(EntityUtils.getSet(newClassList, e->e.getBatch()).size()!=newClassIds.size()) {
			return error("班级对应批次点重复");
		}
		List<NewGkClassStudent> updateSaveList=new ArrayList<>();
		for(Entry<String, NewGkClassStudent> item:updateOldMap.entrySet()) {
			if(!newClassIdBysubId.containsKey(item.getKey())) {
				return error("班级对应科目不一致");
			}
			item.getValue().setClassId(newClassIdBysubId.get(item.getKey()));
			item.getValue().setModifyTime(new Date());
			updateSaveList.add(item.getValue());
		}
		try {
			newGkClassStudentService.saveAll(updateSaveList.toArray(new NewGkClassStudent[0]));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return success("操作成功");
	}
	@ResponseBody
	@RequestMapping("/deleteTeachClass")
	@ControllerInfo(value = "保存学生数据")
	public String deleteTeachClass(@PathVariable String divideId,String classId) {
		if(StringUtils.isBlank(classId)) {
			return error("参数丢失");
		}
		NewGkDivideClass divideClass = newGkDivideClassService.findById(getLoginInfo().getUnitId(), classId, true);
		if (divideClass == null) {
			return error("所选班级不存在");
		}
		if(CollectionUtils.isNotEmpty(divideClass.getStudentList())) {
			return error("所选班级下有学生，不能删除");
		}
		try {
			newGkDivideClassService.delete(classId);
		} catch (Exception e) {
			e.printStackTrace();
			return error("删除失败");
		}
		return success("");
		
	}
	
	public static void main(String[] args) {
		int stum=20;
		int avg=50;
		long cNum = Math.round(stum*1.0/avg);
		System.out.println(cNum);
	}
	
}
