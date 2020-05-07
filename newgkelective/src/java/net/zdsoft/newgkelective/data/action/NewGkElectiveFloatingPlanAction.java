package net.zdsoft.newgkelective.data.action;

import java.util.*;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.newgkelective.data.dto.*;
import net.zdsoft.newgkelective.data.entity.*;
import net.zdsoft.system.entity.mcode.McodeDetail;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.service.NewGkClassBatchService;
import net.zdsoft.newgkelective.data.service.NewGkDivideStusubService;
import net.zdsoft.newgkelective.data.utils.CombineAlgorithmInt;

/**
 * 分班结果,文理，手动分班等
 */
@Controller
@RequestMapping("/newgkelective/{divideId}")
public class NewGkElectiveFloatingPlanAction extends
		NewGkElectiveDivideCommonAction {

	public static final String MALE = "男";
	public static final String FEMALE = "女";
	
	final boolean useChoseBackup = true;

	@Autowired
	private NewGkClassBatchService newGkClassBatchService;
	@Autowired
	private NewGkDivideStusubService newGkDivideStusubService;
	
	private static Logger logger = LoggerFactory.getLogger(NewGkElectiveFloatingPlanAction.class);
	
	@RequestMapping("/floatingPlan/index")
	public String floatingPlanIndex(@PathVariable String divideId, String planType, ModelMap map) {
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		
		if(StringUtils.isBlank(planType)) {
			planType = NewGkElectiveConstant.SUBJECT_TYPE_A;
		}
		map.put("divide", divide);
		map.put("planType", planType);
		return "/newgkelective/floatingPlan/floatingPlanIndex.ftl";
	}
	
	/**
	 * 检查是否 所有学生 都安排完毕
	 * @param divideId
	 * @return
	 */
	@RequestMapping("/floatingPlan/finishDivide")
	@ResponseBody
	public String finishDivide(@PathVariable String divideId) {
		String keyA = NewGkElectiveConstant.DIVIDE_CLASS + "_Bath_" +NewGkElectiveConstant.SUBJECT_TYPE_A+"_"+ divideId;
		String keyB = NewGkElectiveConstant.DIVIDE_CLASS + "_Bath_" +NewGkElectiveConstant.SUBJECT_TYPE_B+"_"+ divideId;
		if (RedisUtils.get(keyA) != null && "start".equals(RedisUtils.get(keyA))) {
			return error("选考还在计算中");
		}
		if (RedisUtils.get(keyB) != null && "start".equals(RedisUtils.get(keyB))) {
			return error("学考还在计算中");
		}
		
		Map<String, List<Integer[]>> countMapA = makeStuMoveCount(divideId, NewGkElectiveConstant.SUBJECT_TYPE_A, null);
		
		boolean anyMatch = countMapA.values().stream().flatMap(e->e.stream()).map(e->e[1]).anyMatch(e->e>0);
		if(anyMatch) {
			return error("选考 还有学生未安排");
		}
		Map<String, List<Integer[]>> countMapB = makeStuMoveCount(divideId, NewGkElectiveConstant.SUBJECT_TYPE_B, null);
		anyMatch = countMapB.values().stream().flatMap(e->e.stream()).map(e->e[1]).anyMatch(e->e>0);
		if(anyMatch) {
			return error("学考 还有学生未安排");
		}
		
		// TODO 删除 不应该开设的班级
		Set<String> openA = countMapA.keySet().stream().map(e->"A"+e).collect(Collectors.toSet());
		Set<String> openB = countMapB.keySet().stream().map(e->"B"+e).collect(Collectors.toSet());
		String unitId = getLoginInfo().getUnitId();
		List<NewGkDivideClass> jxbList = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId,
				new String[] {NewGkElectiveConstant.CLASS_TYPE_2},
				true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		
		openA.addAll(openB);
		List<String> delCids = new ArrayList<>();
		Set<String> batchA = new HashSet<>();
		Set<String> batchB = new HashSet<>();
		for (NewGkDivideClass jxb : jxbList) {
			if(!openA.contains(jxb.getSubjectType()+jxb.getSubjectIds()) || CollectionUtils.isEmpty(jxb.getStudentList())) {
				delCids.add(jxb.getId());
				continue;
			}
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(jxb.getSubjectType())) {
				batchA.add(jxb.getBatch());
			}else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(jxb.getSubjectType())) {
				batchB.add(jxb.getBatch());
			}
		}
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		divide.setBatchCountTypea(batchA.size());
		divide.setBatchCountTypeb(batchB.size());
		Map<String,String> batchMap = new HashMap<>();
		checkBatch(batchA, batchMap,"A");
		checkBatch(batchB, batchMap,"B");
		List<NewGkDivideClass> savedClassList = new ArrayList<>();
		List<NewGkClassBatch> updateBatchList = new ArrayList<>();
		if(batchMap.size() > 0) {
			for (NewGkDivideClass jxb : jxbList) {
				if(batchMap.containsKey(jxb.getSubjectType()+jxb.getBatch())) {
					jxb.setBatch(batchMap.get(jxb.getSubjectType()+jxb.getBatch()));
					savedClassList.add(jxb);
				}
			}
			List<NewGkDivideClass> zhbList = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId, new String[]{NewGkElectiveConstant.CLASS_TYPE_0}, false,
					NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			Set<String> zhbIds = EntityUtils.getSet(zhbList, e -> e.getId());
			List<NewGkClassBatch> classBatchList = classBatchService.findByDivideClsIds(zhbIds.toArray(new String[0]));
			for (NewGkClassBatch clsBth : classBatchList) {
				if(batchMap.containsKey(clsBth.getSubjectType()+clsBth.getBatch())){
					clsBth.setBatch(batchMap.get(clsBth.getSubjectType()+clsBth.getBatch()));
					updateBatchList.add(clsBth);
				}
			}
			Map<String,Set<String>[]> zhbBatchMap = new HashMap<>();
			for (NewGkClassBatch classBatch : classBatchList) {
				Set<String>[] sets = zhbBatchMap.get(classBatch.getDivideClassId());
				if(sets == null){
					sets = new HashSet[2];
					sets[0] = new HashSet<>();
					sets[1] = new HashSet<>();
					zhbBatchMap.put(classBatch.getDivideClassId(),sets);
				}
				if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(classBatch.getSubjectType())){
					sets[0].add(classBatch.getBatch());
				}else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(classBatch.getSubjectType())){
					sets[1].add(classBatch.getBatch());
				}
			}
			for (NewGkDivideClass zhb : zhbList) {
				if(StringUtils.isNotBlank(zhb.getBatch())&&zhbBatchMap.containsKey(zhb.getId())){
					Set<String>[] sets = zhbBatchMap.get(zhb.getId());
					String collect = Arrays.stream(sets).map(e -> e.stream().collect(Collectors.joining(","))).collect(Collectors.joining(";"));
					System.out.println(zhb.getClassName()+" :"+ collect);
					zhb.setBatch(collect);
					savedClassList.add(zhb);
				}
			}

		}
		
		try {
			newGkDivideService.saveFinishDivide(divide, delCids.toArray(new String[0]),savedClassList, updateBatchList);
		} catch (Exception e1) {
			e1.printStackTrace();
			return error(""+e1.getMessage());
		}
		
		return returnSuccess();
	}

	private void checkBatch(Set<String> batchA, Map<String, String> batchMap, String subjectType) {
		List<String> aModel = Stream.iterate(1, e->e+1).limit(batchA.size()).map(e->e+"").collect(Collectors.toList());
		if(!batchA.containsAll(aModel)) {
			int i=0;
			for (String oldBatch : batchA) {
				if(!oldBatch.equals(aModel.get(i))) {
					batchMap.put(subjectType+oldBatch, aModel.get(i));
				}
				i++;
			}
		}
	}
	
	/**
	 * 全手动模式 - 走班安排
	 * @param divideId
	 * @param planType 区分 选考还是 学考
	 * @return
	 */
	@RequestMapping("/floatingPlan/page")
	public String showFloatingPlan(@PathVariable String divideId, String planType, ModelMap map) {
		
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		String unitId = divide.getUnitId();
		List<NewGkDivideClass> allClassList = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId, null, true, 
				NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<NewGkDivideClass> zhbList = allClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType()))
				.collect(Collectors.toList());
		List<NewGkDivideClass> jxbList = allClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())&&Objects.equals(planType, e.getSubjectType()))
				.collect(Collectors.toList());
		Map<String, List<NewGkDivideClass>> zhbJxbMap = jxbList.stream().filter(e->StringUtils.isNotBlank(e.getRelateId())).collect(Collectors.groupingBy(e->e.getRelateId()));
		
		Map<String, NewGkDivideClass> zhbMap = EntityUtils.getMap(zhbList, e->e.getId());
		boolean canEdit = true;
		for (NewGkDivideClass jxb : jxbList) {
			if(StringUtils.isBlank(jxb.getRelateId()) || !zhbMap.containsKey(jxb.getRelateId())) {
				// 组建了教学班 
				canEdit = false;
				break;
			}else {
				NewGkDivideClass zhb = zhbMap.get(jxb.getRelateId());
				String subjectIds = zhb.getSubjectIds();
				if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(planType)) {
					subjectIds = Optional.ofNullable(zhb.getSubjectIdsB()).orElse("");
				}
				if(subjectIds.contains(jxb.getSubjectIds())) {
					// 发生了 合班 
					canEdit = false;
					break;
				}else if(!zhb.getStudentList().containsAll(jxb.getStudentList())) {
					// 教学班 对应的 走班 班级 被插入了 其他班的 人
					canEdit = false;
					break;
				}
			}
		}
		
		List<NewGkOpenSubject> openSubjects = newGkOpenSubjectService.findByDivideId(divideId);
		Set<String> openSubIds = openSubjects.stream().filter(e->Objects.equals(planType, e.getSubjectType()))
				.map(e->e.getSubjectId()).collect(Collectors.toSet());
		
		 // 
		List<NewGkChoResult> choseResults = makeChoByBak(divide, null);
		
		Set<String> allSubIds = EntityUtils.getSet(choseResults, NewGkChoResult::getSubjectId);
		allSubIds.addAll(openSubIds);
		List<Course> allCourses = courseRemoteService.findListObjectBy(Course.class, null, null, "id", allSubIds.toArray(new String[0]), 
				new String[] {"id","shortName","subjectName"});
		Map<String, String> subNameMap = EntityUtils.getMap(allCourses, Course::getId, Course::getSubjectName);
		
		Set<String> zhbIds = zhbMap.keySet();
		List<NewGkClassBatch> clsBaList = newGkClassBatchService.findByDivideClsIdsWithMaster(zhbIds.toArray(new String[0]));
		clsBaList = clsBaList.stream().filter(e->Objects.equals(planType, e.getSubjectType())).collect(Collectors.toList());
		boolean initialized = false;
		// key:clsId+ 选课组合 id
		Map<String,List<NewGkClassBatch>> clsBatchMap = null;
		if(CollectionUtils.isNotEmpty(clsBaList)) {
			initialized = true;
			clsBatchMap = EntityUtils.getListMap(clsBaList, e->e.getDivideClassId()+e.getSubjectIds(),e->e);
		}else {
//			zhbList.forEach(e->{
//				e.setBatch(null);
//				e.setSubjectIdsB(null);
//			});
		}
		
		HashMap<String, NewGkChoResultDto> dtoMap = new HashMap<>();
		NewGkChoResultDto dto;
		for(NewGkChoResult result : choseResults){
			if(!dtoMap .containsKey(result.getStudentId())){
				dto = new NewGkChoResultDto();
                dto.setChooseSubjectIds(new HashSet<String>());
                dto.setStudentId(result.getStudentId());
                dtoMap.put(result.getStudentId(), dto);
            }
            dtoMap.get(result.getStudentId()).getChooseSubjectIds().add(result.getSubjectId());
		}
		
		int batchIndex = 0;
		if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(planType)) {
			batchIndex = 1;
		}
		Map<String, String> stuClassIdMap = new HashMap<>();
		List<NewGkDivideClass> fix3List = new ArrayList<>(); // 3科 组合班 
		List<NewGkDivideClass> fix2List = new ArrayList<>();  // 2科组合
		List<NewGkDivideClass> fix1List = new ArrayList<>();  // 定1走2
		List<NewGkDivideClass> mixedList = new ArrayList<>();  // 混合班
		Map<String,String> clsBatchSubMap = new HashMap<>(); // key:clsId+batch ;走班科目
		Map<String,List<String[]>> classChoseMap = new HashMap<>();  // 每个班级的学生 选课 情况 v：List{subId,stuNum}
		for(NewGkDivideClass cls:zhbList) {
			if(CollectionUtils.isNotEmpty(cls.getStudentList())) {
				cls.getStudentList().forEach(s->stuClassIdMap.put(s, cls.getId()));
			}

			
			
			List<String> studentList = cls.getStudentList();
			Map<String,Integer> numMap = new HashMap<>();
			for (String stuId : studentList) {
				NewGkChoResultDto choDto = dtoMap.get(stuId);
				if(choDto != null) {
					if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType)) {
						choDto.getChooseSubjectIds().forEach(e->{
							numMap.put(e, (numMap.get(e)==null?0:numMap.get(e))+1);
						});
					}else {
						allSubIds.stream().filter(e->!choDto.getChooseSubjectIds().contains(e)).forEach(e->{
							numMap.put(e, (numMap.get(e)==null?0:numMap.get(e))+1);
						});
					}
				}
			}
			List<String> subjectIdsB = numMap.keySet().stream().filter(e->numMap.get(e).equals(studentList.size())).sorted().collect(Collectors.toList());
			if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(planType)) {
				cls.setSubjectIdsB(String.join(",", subjectIdsB.toArray(new String[0])));
			}
			List<String> floatingSubs = numMap.keySet().stream().sorted((x, y) -> {
					if(!openSubIds.contains(x) && openSubIds.contains(y))
						return 1;
					if(!openSubIds.contains(y) && openSubIds.contains(x))
						return -1;
					return numMap.get(y) - numMap.get(x);
				}).collect(Collectors.toList());
			
			
			
			String fixedSubId = "";
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType)) {
				if(!BaseConstants.ZERO_GUID.equals(cls.getSubjectIds())) {
					fixedSubId = cls.getSubjectIds();
					String[] subIds = cls.getSubjectIds().split(",");
					if(subIds.length>1) {
						Arrays.sort(subIds);
					}
					String subNames = Arrays.stream(subIds).filter(e->subNameMap.containsKey(e)).map(e->subNameMap.get(e)).collect(Collectors.joining(","));
					cls.setSubNames(subNames);
				}
			}else {
				fixedSubId = cls.getSubjectIdsB();
				
				if(subjectIdsB.size()>1) {
					Collections.sort(subjectIdsB);
				}
				String subNames = subjectIdsB.stream().filter(e->subNameMap.containsKey(e)).map(e->subNameMap.get(e)).collect(Collectors.joining(","));
				cls.setSubNames(subNames);
			}
			if(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(cls.getSubjectType()) || ("B".equals(planType) && subjectIdsB.size()==(allSubIds.size() - 3))) {
				fix3List.add(cls);
			}else {
				String fixedSub2 = fixedSubId;
				List<String[]> collect = floatingSubs.stream().filter(e->!fixedSub2.contains(e))
						.map(e->new String[] {e,String.valueOf(numMap.get(e)),subNameMap.get(e)}).collect(Collectors.toList());
				// 每个班级的学生 选课 情况
				classChoseMap.put(cls.getId(), collect);
				// 先计算批次点；再确定 走班科目
				if(!initialized /*|| NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(cls.getSubjectType())*/) {
					int limit  = 3 - Integer.parseInt(cls.getSubjectType());
					if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(planType)) {
						limit = allSubIds.size()-3 - subjectIdsB.size();
					}
					List<String> floatingSubsT = floatingSubs.stream().filter(e->!fixedSub2.contains(e)).limit(limit).collect(Collectors.toList());
					cls.setFloatingSubIds(floatingSubsT);
					
					List<String> batchs = Stream.iterate(1, e->e+1).limit(limit).map(e->String.valueOf(e)).collect(Collectors.toList());
					cls.setBatchs(batchs);
					if(cls.getBatch() == null) cls.setBatch("");
					if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType)) {
						String[] split = cls.getBatch().split(";");
						if(split.length<2) {
							cls.setBatch(String.join(",", batchs.toArray(new String[0]))+";");
						}else {
							cls.setBatch(String.join(",", batchs.toArray(new String[0]))+";"+split[1]);
						}
					}else {
						cls.setBatch(cls.getBatch().split(";")[0]+";"+String.join(",", batchs.toArray(new String[0])));
					}
					for (int i=0;i<batchs.size();i++) {
						clsBatchSubMap.put(cls.getId()+batchs.get(i), floatingSubsT.get(i));
					}
				}else {
					// 已经初始化 
					List<NewGkDivideClass> jxbs = zhbJxbMap.get(cls.getId());
					if(cls.getBatch().split(";").length < (batchIndex+1)) {
//						continue;
					}
					String batch = cls.getBatch().split(";")[batchIndex];
					cls.setBatchs(Arrays.asList(batch.split(",")));
					Map<String,String> treeMap = new TreeMap<>();
					if(jxbs!=null) {
						Map<String, String> bthSubMap = EntityUtils.getMap(jxbs, NewGkDivideClass::getBatch,NewGkDivideClass::getSubjectIds);
						for (String bth : cls.getBatchs()) {
							if(bthSubMap.containsKey(bth)) {
								treeMap.put(bth, bthSubMap.get(bth));
							}else {
								treeMap.put(bth, "");
							}
						}
						cls.setFloatingSubIds(new ArrayList<>(treeMap.values()));
					}else {
						cls.setFloatingSubIds(new ArrayList<>());
					}
				}

				
				if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType)) {
					if(NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(cls.getSubjectType())) {
						fix2List.add(cls);
					}else if(NewGkElectiveConstant.SUBJTCT_TYPE_1.equals(cls.getSubjectType())) {
						fix1List.add(cls);
					}else if(NewGkElectiveConstant.SUBJTCT_TYPE_0.equals(cls.getSubjectType())) {
						mixedList.add(cls);
					}
				}else {
					if(cls.getBatchs().size()==1) {
						fix2List.add(cls);
					}else if(cls.getBatchs().size() >1) {
						mixedList.add(cls);
					}
				}
			}
		}
		
		Map<String, List<String>> zhbCstuMap = new HashMap<String, List<String>>();
		for(NewGkChoResult result : choseResults){
            String cid = stuClassIdMap.get(result.getStudentId());
            List<String> sids = zhbCstuMap.get(cid);
	    	if(sids == null){
	    		sids = new ArrayList<String>();
	    		zhbCstuMap.put(cid, sids);
	    	}
	    	if (!sids.contains(result.getStudentId())) {
				sids.add(result.getStudentId());
			}
		}
		
		Integer[] array = Stream.iterate(0, e->e+1).limit(allSubIds.size()).toArray(e->new Integer[e]);
		CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(array,3);
		// 排列 组合的结果 比如 7选3 所有组合 
        Integer[][] result = combineAlgorithm.getResutl();
        List<NewGkConditionDto> newConditionList3=newGkChoiceService.findSubRes(allCourses, dtoMap, result, 3);
        // key: zhbId+ 选课组合 subjectIds
        Map<String, NewGkConditionDto> clsSubGropMap = new HashMap<String, NewGkConditionDto>();
        NewGkDivideClass zhb;
        if(CollectionUtils.isNotEmpty(newConditionList3)) {
        	for(NewGkConditionDto con : newConditionList3) {
        		Set<String> sids = con.getStuIds();
        		if(CollectionUtils.isEmpty(sids)) {
        			continue;
        		}
        		for(String stuId : sids) {
        			String cid = stuClassIdMap.get(stuId);
        			NewGkConditionDto cdto = clsSubGropMap.get(cid+con.getSubjectIdstr());
        			if(cdto == null) {
        				cdto = new NewGkConditionDto();
        				EntityUtils.copyProperties(con, cdto);
        				cdto.setSumNum(0);
        				cdto.setStuIds(new HashSet<>());
        				clsSubGropMap.put(cid+con.getSubjectIdstr(), cdto);
        				zhb = zhbMap.get(cid);
        				if(zhb != null) zhb.getNewDtoList().add(cdto);
        			}
        			cdto.setSumNum(cdto.getSumNum()+1);
        			cdto.getStuIds().add(stuId);
        		}
        	}
        }
        //设置 科目 批次
        Map<String,String> floatingGroupSubIdMap = new HashMap<>();
        for (NewGkDivideClass zhbCls : zhbList) {
        	List<NewGkConditionDto> newDtoList = zhbCls.getNewDtoList();
        	List<String> batchs = zhbCls.getBatchs();
        	List<String> floatingSubIds = zhbCls.getFloatingSubIds();
        	if(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(zhbCls.getSubjectType()) 
        			|| ("B".equals(planType) && zhbCls.getSubjectIdsB()!=null && zhbCls.getSubjectIdsB().split(",").length == (allSubIds.size()-3))) {
        		continue;
        	}
        	
        	for (NewGkConditionDto newDto : newDtoList) {
        		String subjectIdstr = newDto.getSubjectIdstr();
        		if(initialized && zhbCls.getBatchs().size()>1) {
        			List<NewGkClassBatch> list = clsBatchMap.get(zhbCls.getId()+subjectIdstr);
        			if(list == null)
        				continue;
        			for (String ba : batchs) {
						for (NewGkClassBatch clsBat : list) {
							if(clsBat.getBatch().equals(ba)) {
								floatingGroupSubIdMap.put(zhbCls.getId()+subjectIdstr+ba, clsBat.getSubjectId());
							}
						}
					}
        		}else {
        			// 未初始化 的
        			String zhbSubIds = zhbCls.getSubjectIds();
        			List<String> subIds = newDto.getSubjectIds().stream().filter(e->!zhbSubIds.contains(e)).collect(Collectors.toList());
        			List<String> selSubIds = subIds.stream().collect(Collectors.toList());
        			if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(planType)) {
        				String zhbSubIdsB = zhbCls.getSubjectIdsB();
        				subIds = allSubIds.stream().filter(e->!newDto.getSubjectIds().contains(e)).filter(e->!zhbSubIdsB.contains(e)).collect(Collectors.toList());
        				selSubIds = subIds.stream().collect(Collectors.toList());
        			}
        			if(zhbCls.getBatchs().size() > 1) {
        				// 不是这个组合班 走班的 科目 
        				selSubIds = subIds.stream().filter(e->!floatingSubIds.contains(e)).collect(Collectors.toList());
        			}
        			
        			for (String ba : batchs) {
        				String subId = clsBatchSubMap.get(zhbCls.getId()+ba);
						if(StringUtils.isBlank(subId) || !subIds.contains(subId)) {
							subId = selSubIds.get(selSubIds.size()-1);
						}else {
//							clsBatchSubMap.remove(zhbCls.getId()+ba);
						}
						floatingGroupSubIdMap.put(zhbCls.getId()+subjectIdstr+ba, subId);
						selSubIds.remove(subId);
					}
        		}
        		
        		if(zhbCls.getBatchs().size() > 1) {
        			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType)) {
        				String xzbSubstr = Stream.concat(floatingSubIds.stream(), Stream.of(zhbCls.getSubjectIds().split(",")))
        						.filter(e->!BaseConstants.ZERO_GUID.equals(e)).sorted().collect(Collectors.joining(","));
        				if(Objects.equals(subjectIdstr, xzbSubstr)) {
        					newDto.setBeXzbSub(true);
        				}
        			}else {
        				String xzbSubstr = Stream.concat(floatingSubIds.stream(), Stream.of(zhbCls.getSubjectIdsB().split(",")))
        						.filter(e->!BaseConstants.ZERO_GUID.equals(e)&&StringUtils.isNotBlank(e)).sorted().collect(Collectors.joining(","));
        				String subjectIdstr2 = allSubIds.stream().filter(e->!newDto.getSubjectIds().contains(e)).sorted().collect(Collectors.joining(","));
        				if(Objects.equals(subjectIdstr2, xzbSubstr)) {
        					newDto.setBeXzbSub(true);
        				}
        			}
        		}
			}
		}
		
        if(!initialized) {
        	//初始化 保存数据
        	String planTypeStr = "选";
        	if("B".equals(planType)) {
        		planTypeStr = "学";
        	}
        	Date now = new Date();
        	List<NewGkClassBatch> savedClsBatchList = new ArrayList<>();
        	List<NewGkDivideClass> saveClsList = new ArrayList<>();
        	NewGkClassBatch clsBat;
        	NewGkDivideClass jxb;
        	Map<String,Integer> subMaxMap = new HashMap<>();
        	for (NewGkDivideClass zhbCls : zhbList) {
        		
        		// 定一走二  和 混合班 才会开教学班
        		if(!(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(zhbCls.getSubjectType()) 
        				|| ("B".equals(planType) && zhbCls.getSubjectIdsB()!=null && zhbCls.getSubjectIdsB().split(",").length == (allSubIds.size()-3)))) {
					List<String> floatingSubIds = zhbCls.getFloatingSubIds();
					List<String> batchs = zhbCls.getBatchs();
					for (int i=0;i<batchs.size();i++) {
						jxb = new NewGkDivideClass();
						jxb.setId(UuidUtils.generateUuid());
						jxb.setCreationTime(now);
						jxb.setModifyTime(now);
						
						jxb.setSubjectIds(floatingSubIds.get(i));
						jxb.setSubjectType(planType);
						jxb.setClassType(NewGkElectiveConstant.CLASS_TYPE_2);
						jxb.setDivideId(divideId);
						jxb.setRelateId(zhbCls.getId());
						jxb.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
						jxb.setBatch(batchs.get(i));
						jxb.setIsHand(NewGkElectiveConstant.IF_1);
						
						Integer max = subMaxMap.get(jxb.getSubjectIds());
						if(max == null) {
							max = 0;
						}
						if(batchs.size()>1) {
							subMaxMap.put(jxb.getSubjectIds(), ++max);
							jxb.setClassName(subNameMap.get(jxb.getSubjectIds())+planTypeStr+max+"班");
							
							saveClsList.add(jxb);
						}
						// 构造学生
						List<String> studentList = jxb.getStudentList();
						
						// 保存 classBatch 信息
						List<NewGkConditionDto> newDtoList = zhbCls.getNewDtoList();
						if(CollectionUtils.isNotEmpty(newDtoList)) {
							for (NewGkConditionDto dto2 : newDtoList) {
								String subjectIdstr = dto2.getSubjectIdstr();
								clsBat = new NewGkClassBatch();
								clsBat.setId(UuidUtils.generateUuid());
								clsBat.setUnitId(unitId);
								clsBat.setDivideId(divideId);
								clsBat.setDivideClassId(zhbCls.getId());
								clsBat.setSubjectIds(subjectIdstr);
								clsBat.setSubjectType(planType);
								String subId = floatingGroupSubIdMap.get(zhbCls.getId()+subjectIdstr+batchs.get(i));
								if(StringUtils.isBlank(subId)) {
									logger.error(zhbCls.getClassName()+" 时间点"+batchs.get(i)+"找不到科目 ");
									continue;
								}
								clsBat.setSubjectId(subId);
								clsBat.setBatch(batchs.get(i));
								savedClsBatchList.add(clsBat);
								
								if(subId.equals(jxb.getSubjectIds())) {
									studentList.addAll(dto2.getStuIds());
								}
							}
						}
					}
				}
			}
        	
        	try {
        		saveClsList.addAll(zhbList);
				newGkClassBatchService.saveBatchs2(unitId, divideId, saveClsList, savedClsBatchList);
			} catch (Exception e1) {
				e1.printStackTrace();
				return error("初始化数据失败，"+e1.getMessage());
			}
        }
        
        Map<String,String> xzbNameMap = allClassList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType()))
        		.collect(Collectors.toMap(e->e.getRelateId(), e->e.getClassName()));
        
        List<String> freeSubIds = allSubIds.stream().filter(e->!openSubIds.contains(e)).collect(Collectors.toList());
        
        map.put("canEdit", canEdit+"");
//        map.put("canEdit", false+"");
        map.put("freeSubIds", freeSubIds);
        map.put("classChoseMap", classChoseMap);
        
        map.put("floatingGroupSubIdMap", floatingGroupSubIdMap);
        
        map.put("fix3List", fix3List);
        map.put("fix2List", fix2List);
        map.put("fix1List", fix1List);
        map.put("mixedList", mixedList);
        map.put("subNameMap", subNameMap);
        map.put("xzbNameMap", xzbNameMap);
        map.put("planType", planType);
        map.put("divideId", divideId);
        map.put("allSubIds", allSubIds);
		
		return "/newgkelective/floatingPlan/floatingPlanList.ftl";
	}

	/**
	 * 使用分班时创建的 选课备份数据 而不是 实时的 选课数据 , 只有 studentId 和 subjectId
	 * @param divide
	 * @param stuIds
	 * @return 
	 */
	private List<NewGkChoResult> makeChoByBak(NewGkDivide divide, List<String> stuIds) {
		List<NewGkChoResult> choseResults = new ArrayList<>();
		if(useChoseBackup) {
			List<NewGkDivideStusub> stuSubList = null; 
			if(stuIds == null) {
				stuSubList = newGkDivideStusubService.findByDivideIdWithMaster(divide.getId(), 
						NewGkElectiveConstant.SUBJECT_TYPE_A, null);
			}else {
				stuSubList = newGkDivideStusubService.findListByStudentIdsWithMaster(divide.getId(), 
						NewGkElectiveConstant.SUBJECT_TYPE_A, stuIds.toArray(new String[0]));
			}
			if(CollectionUtils.isEmpty(stuSubList)) {
				return choseResults;
			}
			NewGkChoResult cho;
			for (NewGkDivideStusub stuSub : stuSubList) {
				String studentId = stuSub.getStudentId();
				String subjectIds = stuSub.getSubjectIds();
				if(StringUtils.isBlank(subjectIds)) {
					continue;
				}
				String[] subIdArr = subjectIds.split(",");
				if(subIdArr.length==0) {
					continue;
				}
				for (String subId : subIdArr) {
					cho = new NewGkChoResult();
					cho.setStudentId(studentId);
					cho.setSubjectId(subId);
					choseResults.add(cho);
				}
			}
			return choseResults;
		}else {
			if(stuIds == null) {
				choseResults = newGkChoResultService.findByGradeIdAndKindType(divide.getUnitId(), NewGkElectiveConstant.KIND_TYPE_01, 
						new String[] {divide.getChoiceId()});
			}else {
				choseResults = newGkChoResultService.findByKindTypeAndChoiceIdAndStudentIds(divide.getUnitId(), 
						NewGkElectiveConstant.KIND_TYPE_01,divide.getChoiceId(), 
						stuIds.toArray(new String[0]));
			}
			return choseResults;
		}
	}
	
	@RequestMapping("/floatingPlan/setMoveCourse")
	@ResponseBody
	public String setMoveCourse(@PathVariable String divideId, String zhbId, String planType, String batchSub, String batchParam) {
		
		String[] params = batchSub.split("@");
		Map<Integer,String> batchSubIdMap = new HashMap<>();
		List<Integer> batchs = new ArrayList<>();
		Set<String> floatingSubIds = new HashSet<>();
		for (String pa : params) {
			String[] entry = pa.split("=");
			// entry[0]是 batch-1/batch-2这样格式的
			Integer batch = Integer.parseInt(entry[0].split("-")[1]);
			batchs.add(batch);
			if(entry.length>1) {
				batchSubIdMap.put(batch, entry[1]);
				floatingSubIds.add(entry[1]);
			}else {
				batchSubIdMap.put(batch, "");
			}
		}
		
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		String unitId = divide.getUnitId();
		String choiceId = divide.getChoiceId();
		
		NewGkDivideClass zhbCls = newGkDivideClassService.findOne(zhbId);
		if(zhbCls == null) {
			return error("找不到对应 组合班");
		}
		newGkDivideClassService.toMakeStudentList(unitId, divideId, Arrays.asList(zhbCls));
		List<String> stuIds = zhbCls.getStudentList();
		List<NewGkDivideClass> mainJxbList = newGkDivideClassService.findByDivideIdAndClassTypeSubjectType(unitId, divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_2}, false, 
				NewGkElectiveConstant.CLASS_SOURCE_TYPE1, planType);
		List<NewGkDivideClass> jxbList = mainJxbList.stream().filter(e->zhbId.equals(e.getRelateId())).collect(Collectors.toList());
		// 找到原 走班科目 对应的 教学班
		int batchIndex = planType.equals("A")?0:1;
		String jxbBatchs = zhbCls.getBatch().split(";")[batchIndex];
		jxbList = jxbList.stream().filter(e->jxbBatchs.contains(e.getBatch())).collect(Collectors.toList());
		List<NewGkChoResult> choseResults = makeChoByBak(divide, stuIds);
		
		Set<String> allSubIds = EntityUtils.getSet(choseResults, NewGkChoResult::getSubjectId);
		
		Map<String, List<String>> stuChoMap = EntityUtils.getListMap(choseResults, NewGkChoResult::getStudentId, NewGkChoResult::getSubjectId);
		// key:科目组合  V:组合信息  学生 人数  科目名称等等
		Map<String, NewGkConditionDto> dtoMap = makeChoGroupMap(stuChoMap);
		NewGkConditionDto dto;
//		Map<String,NewGkConditionDto> dtoMap = new HashMap<>();
//		for (String stuId : stuIds) {
//			if(!stuChoMap.containsKey(stuId))
//				continue;
//			List<String> subIds = stuChoMap.get(stuId);
//			Collections.sort(subIds);
//			String subIdstr = subIds.stream().collect(Collectors.joining(","));
//			
//			dto = dtoMap.get(subIdstr);
//			if(dto == null) {
//				dto = new NewGkConditionDto();
//				dto.setSubjectIdstr(subIdstr);
//				dto.setSubjectIds(new HashSet<>(subIds));
//				dto.setSumNum(0);
//				dtoMap.put(subIdstr, dto);
//			}
//			dto.getStuIds().add(stuId);
//			dto.setSumNum(dto.getSumNum()+1);
//		}
		
		// 组教学班
		String planTypeStr = "选";
    	if("B".equals(planType)) {
    		planTypeStr = "学";
    	}
    	Map<String, Set<String>> subNamesMap = mainJxbList.stream().collect(Collectors.groupingBy(NewGkDivideClass::getSubjectIds, 
    			Collectors.mapping(NewGkDivideClass::getClassName, Collectors.toSet())));
		Map<String, String> subNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(batchSubIdMap.values().toArray(new String[0])),
				new TypeReference<Map<String,String>>() {});
		
		List<NewGkDivideClass> newClsList = new ArrayList<>();
		Map<String, NewGkDivideClass> subBatchMap = EntityUtils.getMap(jxbList, e->e.getSubjectIds()+e.getBatch());
		// key: batch+subId
		Map<String,NewGkDivideClass> newClsMap = new HashMap<>();
		NewGkDivideClass cls;
		Date now = new Date();
		for (Integer batch: batchSubIdMap.keySet()) {
			String subId = batchSubIdMap.get(batch);
			if(StringUtils.isBlank(subId)) {
				continue;
			}
			cls = subBatchMap.get(subId+batch);
			if(cls == null) {
				cls = new NewGkDivideClass();
				cls.setId(UuidUtils.generateUuid());
				cls.setCreationTime(now);
				cls.setModifyTime(now);
				
				cls.setSubjectIds(subId);
				cls.setSubjectType(planType);
				cls.setClassType(NewGkElectiveConstant.CLASS_TYPE_2);
				cls.setDivideId(divideId);
				cls.setRelateId(zhbCls.getId());
				cls.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
				cls.setBatch(batch+"");
				cls.setIsHand(NewGkElectiveConstant.IF_1);
				// key:divideId+"_"+subjectId+"_"+subjectType+"_"+bestType(当其中属性为空 则"")
				Set<String> names = subNamesMap.get(subId);
				String cn = subNameMap.get(cls.getSubjectIds())+planTypeStr;
				int max = 1;
				if(CollectionUtils.isNotEmpty(names)) {
					max = names.size();
					while(names.contains(cn+ (++max)+"班"));
				}
				cls.setClassName(cn+max+"班");
			}
			
			newClsList.add(cls);
			newClsMap.put(batch+subId, cls);
		}
		
		Set<String> fixedSubIds = new HashSet<>();
		if(StringUtils.isNotBlank(zhbCls.getSubjectIdsB())) {
			fixedSubIds.addAll(Arrays.asList(zhbCls.getSubjectIdsB().split(",")));
		}
		List<NewGkClassBatch> clsBatchList = new ArrayList<>();
		NewGkClassBatch clsBatch;
		for (String subIdstr : dtoMap.keySet()) {
			dto = dtoMap.get(subIdstr);
			Set<String> groupSubIds = null;
			List<String> tomake = null;
			
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType)) {
				groupSubIds = dto.getSubjectIds();
				tomake = groupSubIds.stream()
						.filter(e->!zhbCls.getSubjectIds().contains(e)&&!floatingSubIds.contains(e)).collect(Collectors.toList());
			}else {
				Set<String> subjectIds = dto.getSubjectIds();
				groupSubIds = allSubIds.stream().filter(e->!subjectIds.contains(e)).collect(Collectors.toSet());
				tomake = groupSubIds.stream()
						.filter(e->!fixedSubIds.contains(e)&&!floatingSubIds.contains(e)).collect(Collectors.toList());
			}
			
			for(int i=0;i<batchs.size();i++) {
				Integer batch = batchs.get(i);
				String subId = batchSubIdMap.get(batch);
				if(StringUtils.isNotBlank(subId) && groupSubIds.contains(subId)) {
					// 组教学班
					if(newClsMap.containsKey(batch+subId)) {
						newClsMap.get(batch+subId).getStudentList().addAll(dto.getStuIds());
					}
				}else {
					subId = tomake.get(tomake.size()-1);
				}
				tomake.remove(subId);
				
				clsBatch = new NewGkClassBatch();
				clsBatch.setId(UuidUtils.generateUuid());
				clsBatch.setUnitId(unitId);
				clsBatch.setDivideId(divideId);
				clsBatch.setDivideClassId(zhbId);
				clsBatch.setSubjectIds(subIdstr);
				clsBatch.setSubjectId(subId);
				clsBatch.setBatch(batch+"");
				clsBatch.setSubjectType(planType);
				clsBatchList.add(clsBatch);
			}
		}
		
		/* 保存时间点修改信息 */
		String[] newBahArr = batchParam.split(",");
		Arrays.sort(newBahArr);
		
		String batch = zhbCls.getBatch();
		String[] zhbBatch = batch.split(";");
		String[] oldBahArr = zhbBatch[batchIndex].split(",");
		
		Map<String,String> batchMap = new HashMap<>();
		for(int i=0;i<oldBahArr.length;i++) {
			batchMap.put(oldBahArr[i], newBahArr[i]);
		}
		
//		for (NewGkDivideClass dc : newClsList) {
//			if(batchMap.containsKey(dc.getBatch())) {
//				dc.setBatch(batchMap.get(dc.getBatch()));
//			}
//		}
		
//		for (NewGkClassBatch clsBth : clsBatchList) {
//			if(batchMap.containsKey(clsBth.getBatch())) {
//				clsBth.setBatch(batchMap.get(clsBth.getBatch()));
//			}
//		}
		
		if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType)) {
			if(zhbBatch.length==2) {
				zhbCls.setBatch(String.join(",", newBahArr)+";"+zhbBatch[1]);
			}else {
				zhbCls.setBatch(String.join(",", newBahArr)+";");
			}
		}else {
			zhbCls.setBatch(zhbBatch[0]+";"+String.join(",", newBahArr));
		}
		
		try {
			List<String> delClsIds = EntityUtils.getList(jxbList, e->e.getId());
			newClsList.add(zhbCls);
			// 自动删除 某些 已经开班的教学班；或者直接删除所有教学班;改为 开始分班后 锁定 不再 需要了
			classBatchService.updateMoveCourse(unitId, divideId, zhbId, planType, delClsIds, newClsList, clsBatchList);
		} catch (Exception e1) {
			e1.printStackTrace();
			return error(e1.getMessage()+"");
		}
		
		return returnSuccess();
	}
	
	/**
	 * 设置走班 时间点
	 * @param divideId
	 * @param zhbId
	 * @param planType
	 * @param batchParam
	 * @return
	 */
	@RequestMapping("/floatingPlan/setMoveTime")
	@ResponseBody
	public String setMoveTime(@PathVariable String divideId, String zhbId, String planType, String batchParam) {
		
		String[] newBahArr = batchParam.split(",");
		Arrays.sort(newBahArr);
		
		NewGkDivideClass zhbCls = newGkDivideClassService.findOne(zhbId);
		int batchIndex = NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType)?0:1;
		String batch = zhbCls.getBatch();
		String[] zhbBatch = batch.split(";");
		String[] oldBahArr = zhbBatch[batchIndex].split(",");
		
		Map<String,String> batchMap = new HashMap<>();
		for(int i=0;i<oldBahArr.length;i++) {
			batchMap.put(oldBahArr[i], newBahArr[i]);
		}
		
		List<NewGkDivideClass> jxbList = newGkDivideClassService.findListByRelateId(divideId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, 
				NewGkElectiveConstant.CLASS_TYPE_2, new String[] {zhbId});
		// 找到原 走班科目 对应的 教学班
		jxbList = jxbList.stream().filter(e->Objects.equals(planType, e.getSubjectType()) && zhbBatch[batchIndex].contains(e.getBatch())).collect(Collectors.toList());
		for (NewGkDivideClass dc : jxbList) {
			if(batchMap.containsKey(dc.getBatch())) {
				dc.setBatch(batchMap.get(dc.getBatch()));
			}
		}
		
		List<NewGkClassBatch> clsBatchList = classBatchService.findByDivideClsIds(new String[] {zhbId});
		clsBatchList = clsBatchList.stream().filter(e->Objects.equals(planType, e.getSubjectType())).collect(Collectors.toList());
		for (NewGkClassBatch clsBatch : clsBatchList) {
			if(batchMap.containsKey(clsBatch.getBatch())) {
				clsBatch.setBatch(batchMap.get(clsBatch.getBatch()));
			}
		}
		
		String unitId = getLoginInfo().getUnitId();
		if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType)) {
			if(zhbBatch.length==2) {
				zhbCls.setBatch(String.join(",", newBahArr)+";"+zhbBatch[1]);
			}else {
				zhbCls.setBatch(String.join(",", newBahArr)+";");
			}
		}else {
			zhbCls.setBatch(zhbBatch[0]+";"+String.join(",", newBahArr));
		}
		jxbList.add(zhbCls);
		
		try {
			classBatchService.updateMoveCourse(unitId, divideId, null, null, null, jxbList, clsBatchList);
		} catch (Exception e1) {
			e1.printStackTrace();
			return error(e1.getMessage()+"");
		}
		
		//TODO 已经组班级 的 学生  删掉班级
		return returnSuccess();
	}
	
	/**
	 *
	 * @param divideId
	 * @param zhbId
	 * @param planType
	 * @param subBatchParam subId-batch,subId-batch
	 * @return
	 */
	@RequestMapping("/floatingPlan/swapCourse")
	@ResponseBody
	public String swapBatchCourse(@PathVariable String divideId, String zhbId, String planType, String subGroup, String subBatchParam) {
		
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		String unitId = divide.getUnitId();
		String choiceId = divide.getChoiceId();
		
		NewGkDivideClass zhbCls = newGkDivideClassService.findOne(zhbId);
		if(zhbCls == null) {
			return error("找不到对应 组合班");
		}
		String[] split = subBatchParam.split(",");
		Map<String,String> map = new HashMap<>();
		for (String subBatch : split) {
			String[] arr = subBatch.split("-");
			map.put(arr[0], arr[1]);
		}
		
		List<NewGkClassBatch> clsBatchList = newGkClassBatchService.findByDivideClsIds(new String[] {zhbId});
		clsBatchList = clsBatchList.stream().filter(e->Objects.equals(planType, e.getSubjectType())).collect(Collectors.toList());
		List<NewGkClassBatch> collect = clsBatchList.stream().filter(e->Objects.equals(subGroup, e.getSubjectIds())&&map.containsKey(e.getSubjectId()))
				.collect(Collectors.toList());
		if(collect.size()!=2) {
			return error("数据已改变，请刷新后操作");
		}
		NewGkClassBatch clsBthA = collect.get(0);
		NewGkClassBatch clsBthB = collect.get(1);
		String oldBatchA = clsBthA.getBatch();
		clsBthA.setBatch(clsBthB.getBatch());
		clsBthB.setBatch(oldBatchA);
		
		List<NewGkDivideClass> jxbList = newGkDivideClassService.findListByRelateId(divideId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, 
				NewGkElectiveConstant.CLASS_TYPE_2, new String[] {zhbId});
		// 找到原 走班科目 对应的 教学班
		int batchIndex = NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType)?0:1;
		String batch = zhbCls.getBatch().split(";")[batchIndex];
		jxbList = jxbList.stream().filter(e->batch.contains(e.getBatch())&&Objects.equals(planType, e.getSubjectType())).collect(Collectors.toList());
		newGkDivideClassService.toMakeStudentList(unitId, divideId, Arrays.asList(zhbCls));
		List<String> stuIds = zhbCls.getStudentList();
		List<NewGkChoResult> choseResults = makeChoByBak(divide, stuIds);
		
		
		Map<String, List<String>> stuChoMap = EntityUtils.getListMap(choseResults, NewGkChoResult::getStudentId, NewGkChoResult::getSubjectId);
		Map<String, NewGkConditionDto> dtoMap = makeChoGroupMap(stuChoMap);
		Map<String,NewGkDivideClass> jxbSubBthMap = EntityUtils.getMap(jxbList, e->e.getSubjectIds()+e.getBatch());
		Map<String,Set<String>> clsNewStuMap = new HashMap<>();
		for (NewGkClassBatch clsBth : clsBatchList) {
			if(!map.values().contains(clsBth.getBatch()) || !jxbSubBthMap.containsKey(clsBth.getSubjectId()+clsBth.getBatch()) 
					||!dtoMap.containsKey(clsBth.getSubjectIds()))
				continue;
			NewGkDivideClass jxb = jxbSubBthMap.get(clsBth.getSubjectId()+clsBth.getBatch());
			
			jxb.getStudentList().addAll(dtoMap.get(clsBth.getSubjectIds()).getStuIds());
			Set<String> set = clsNewStuMap.get(jxb.getId());
			if(set == null) {
				set = new HashSet<>();
				clsNewStuMap.put(jxb.getId(), set);
			}
			set.addAll(dtoMap.get(clsBth.getSubjectIds()).getStuIds());
		}
		
		String planTypeStr = "选考";
    	if("B".equals(planType)) {
    		planTypeStr = "学考";
    	}
    	
    	Map<String, String> subNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(map.keySet().toArray(new String[0])), new TypeReference<Map<String,String>>(){});
		
		List<NewGkDivideClass> updateStuClass = new ArrayList<>();
		List<String> delClsIds = new ArrayList<>();
		boolean refresh = false;
		for (NewGkDivideClass jxb : jxbList) {
			String jxbId = jxb.getId();
			if(!map.values().contains(jxb.getBatch()))
				continue;
			
			Set<String> set = clsNewStuMap.get(jxbId);
			if(CollectionUtils.isNotEmpty(set)) {
				updateStuClass.add(jxb);
			}else {
//				return error(planTypeStr+jxb.getBatch()+" "+subNameMap.get(jxb.getSubjectIds())+" 不能没有学生！");
				refresh = true;
			}
			delClsIds.add(jxbId);
		}
		
		try {
			classBatchService.updateMoveCourse(unitId, divideId, null, null, delClsIds, updateStuClass, Arrays.asList(clsBthA,clsBthB));
		} catch (Exception e1) {
			e1.printStackTrace();
			return error(e1.getMessage()+"");
		}
		
		ResultDto resultDto = new ResultDto().setSuccess(true).setCode("00").setMsg("操作成功！");
		if(refresh) {
			resultDto.setCode("04");
		}
		return Json.toJSONString(resultDto);
	}


	/**
	 * 走班学生统计
	 * @param divideId
	 * @param planType
	 * @return
	 */
	@RequestMapping("/floatingPlan/stuMoveCount")
	public String stuMoveCount(@PathVariable String divideId, String planType, ModelMap modelMap) {
		List<String> allBatchs = new ArrayList<>();
		
		Map<String, List<Integer[]>> stuCountMap = makeStuMoveCount(divideId, planType, allBatchs);
		
		modelMap.put("planType", planType);
		modelMap.put("allBatchs", allBatchs);
		modelMap.put("stuCountMap", stuCountMap);
		return "/newgkelective/floatingPlan/stuMoveCount.ftl";
	}
	
	/**
	 * 
	 * @param divideId
	 * @param planType
	 * @param allBatchs
	 * @return K:subjectId  v:List[已开班，未开班]
	 */
	private Map<String,List<Integer[]>> makeStuMoveCount(String divideId, String planType,List<String> allBatchs){
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		String unitId = divide.getUnitId();
		
		List<NewGkOpenSubject> openSubjects = newGkOpenSubjectService.findByDivideId(divideId);
		Set<String> openSubIds = openSubjects.stream().filter(e->Objects.equals(planType, e.getSubjectType())).map(e->e.getSubjectId()).collect(Collectors.toSet());
		if(CollectionUtils.isEmpty(openSubIds)) {
			return new HashMap<>();
		}
		
		
		List<NewGkDivideClass> divideClassList = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId,
				new String[] {NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_2},
				true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<NewGkDivideClass> zhbList = divideClassList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType())).collect(Collectors.toList());
		Set<String> zhbIds = zhbList.stream().map(e->e.getId()).collect(Collectors.toSet());
		List<NewGkClassBatch> classBatchList = classBatchService.findByDivideClsIds(zhbIds.toArray(new String[0]));
		classBatchList = classBatchList.stream()
				.filter(e->Objects.equals(planType, e.getSubjectType())).collect(Collectors.toList());
		
		List<NewGkChoResult> choseResults = makeChoByBak(divide, null);
		
		Map<String, List<String>> stuChoMap = EntityUtils.getListMap(choseResults, NewGkChoResult::getStudentId, NewGkChoResult::getSubjectId);
//		Map<String, NewGkConditionDto> dtoMap = makeChoGroupMap(stuChoMap);
		Map<String,String> stuClsMap = new HashMap<>();
		zhbList.stream().filter(e->e.getStudentList()!=null).forEach(e->e.getStudentList().forEach(s->stuClsMap.put(s, e.getId())));
		NewGkConditionDto dto;
		Map<String,NewGkConditionDto> dtoMap = new HashMap<>();
		for (String stuId : stuChoMap.keySet()) {
			List<String> subIds = stuChoMap.get(stuId);
			Collections.sort(subIds);
			String subIdstr = subIds.stream().collect(Collectors.joining(","));
			String clsId = stuClsMap.get(stuId);
			if(StringUtils.isBlank(clsId))
				continue;
			dto = dtoMap.get(clsId+subIdstr);
			if(dto == null) {
				dto = new NewGkConditionDto();
				dto.setSubjectIdstr(subIdstr);
				dto.setSubjectIds(new HashSet<>(subIds));
				dto.setSumNum(0);
				dtoMap.put(clsId+subIdstr, dto);
			}
			dto.getStuIds().add(stuId);
			dto.setSumNum(dto.getSumNum()+1);
		}
		
		Map<String, List<String>> modelCountMap = EntityUtils.getListMap(classBatchList, e->e.getSubjectId()+e.getBatch(), e->e.getDivideClassId()+e.getSubjectIds());
		
		List<NewGkDivideClass> jxbList = divideClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())
						&&Objects.equals(planType, e.getSubjectType()))
				.collect(Collectors.toList());
		Map<String, NewGkDivideClass> zhbMap = EntityUtils.getMap(zhbList, e->e.getId());
		Map<String, Set<String>> savedCountMap = new HashMap<>();
		for (NewGkDivideClass dc : jxbList) {
			String jxbSubId = dc.getSubjectIds();
			List<String> studentList = dc.getStudentList();
			if(StringUtils.isNotBlank(dc.getRelateId()) && zhbMap.containsKey(dc.getRelateId())) {
				NewGkDivideClass zhb = zhbMap.get(dc.getRelateId());
				if(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(zhb.getSubjectType()) || 
						(zhb.getSubjectIds() != null && zhb.getSubjectIds().contains(jxbSubId)) || (zhb.getSubjectIdsB() != null && zhb.getSubjectIdsB().contains(jxbSubId))) {
					if(CollectionUtils.isNotEmpty(zhb.getStudentList())) {
						studentList.removeAll(zhb.getStudentList());
					}
				}
			}
			
			if(CollectionUtils.isNotEmpty(studentList)) {
				Set<String> stuIds1 = savedCountMap.get(dc.getSubjectIds()+dc.getBatch());
				if(stuIds1 == null) {
					stuIds1 = new HashSet<>();
					savedCountMap.put(dc.getSubjectIds()+dc.getBatch(), stuIds1);
				}
				stuIds1.addAll(studentList);
			}
		}
		
		Set<String> allSubIds = EntityUtils.getSet(classBatchList, e->e.getSubjectId());
		List<String> collect = classBatchList.stream().map(e->e.getBatch()).distinct().sorted().collect(Collectors.toList());
		boolean checkFinish = false;
		if(allBatchs == null) {
			checkFinish = true;
			allBatchs = new ArrayList<>();
		}
		allBatchs.addAll(collect);
		Map<String, String> subNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(allSubIds.toArray(new String[0])),
				new TypeReference<Map<String,String>>(){});
		
		Map<String,List<Integer[]>> stuCountMap = new LinkedHashMap<>();
		for (String subId : allSubIds) {
			if(!openSubIds.contains(subId))
				continue;
			String sn = subNameMap.get(subId);
			if(checkFinish) {
				sn = subId;
			}
			stuCountMap.put(sn, new ArrayList<>());
			
			for (String batch : allBatchs) {
				Set<String> set = savedCountMap.get(subId+batch);
				int saved = (set==null)?0:set.size();
				
				int modelCount = 0;
				List<String> subIdsList = modelCountMap.get(subId+batch);
				if(CollectionUtils.isNotEmpty(subIdsList)) {
					modelCount = (int)subIdsList.stream()
							.filter(e->dtoMap.containsKey(e)&&dtoMap.get(e).getStuIds()!=null)
							.flatMap(e->dtoMap.get(e).getStuIds().stream())
							.distinct().count();
				}
				stuCountMap.get(sn).add(new Integer[] {saved,modelCount-saved});
			}
		}
		return stuCountMap;
	}

	@RequestMapping("/floatingPlan/initPlan")
	@ResponseBody
	public String initPlan(@PathVariable String divideId, String planType) {
		if(!NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType)
				&&!NewGkElectiveConstant.SUBJECT_TYPE_B.equals(planType)) {
			return error("选/学考类型有误");
		}
		
		try {
			newGkClassBatchService.dealResetFloatingPlan(getLoginInfo().getUnitId(), divideId, planType);
		} catch (Exception e) {
			e.printStackTrace();
			return error(""+e.getMessage());
		}
		
		return returnSuccess();
	}
	
	@RequestMapping("/floatingPlan/teachClassSet")
	public String showTeachClassSet(@PathVariable String divideId, String planType, ModelMap map) {

		NewGkDivide divide = newGkDivideService.findOne(divideId);
        Set<String> choiceSubjectIds = EntityUtils.getSet(newGkChoRelationService.findChooseSubject(divide.getChoiceId(), divide.getUnitId()), Course::getId);

        map.put("divide", divide);
		map.put("planType", planType);
		if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType)) {
			map.put("batchCount", divide.getBatchCountTypea());
		} else {
			map.put("batchCount", divide.getBatchCountTypeb());
		}

		List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeIn(divideId, new String[] {planType});
		Set<String> openSubjectIds = EntityUtils.getSet(openSubjectList, NewGkOpenSubject::getSubjectId);

		// 获取选课数据
		Map<String,Set<String>> subjectChosenMap = new HashMap<>();
		Map<String,Set<String>> studentChosenMap = new HashMap<>();
		Map<String,Set<String>> studentChosenBMap = new HashMap<>();
		makeStudentChooseResult(divide, choiceSubjectIds, subjectChosenMap, studentChosenMap, studentChosenBMap, planType);

		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(divideId), Course.class);
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);

		String unitId = divide.getUnitId();
		List<NewGkDivideClass> allClassList = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId, null, true,
				NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		Map<String, NewGkDivideClass> combinationToBaseClassMap = allClassList.stream()
				.filter(e -> NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType()))
				.collect(Collectors.toMap(e -> e.getRelateId(), e -> e));
		Map<String, NewGkDivideClass> allClassMap = EntityUtils.getMap(allClassList, NewGkDivideClass::getId);
		List<NewGkDivideClass> mixClassList = allClassList.stream()
				.filter(e -> NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType()) && BaseConstants.ZERO_GUID.equals(e.getSubjectIds()))
				.collect(Collectors.toList());
		List<NewGkDivideClass> combinationClassList = allClassList.stream()
				.filter(e -> NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType()) && !BaseConstants.ZERO_GUID.equals(e.getSubjectIds()))
				.collect(Collectors.toList());
		// 跟随行政班上课的教学班
		List<NewGkDivideClass> showClassList = new ArrayList<>();
		for (NewGkDivideClass one : combinationClassList) {
			StringBuilder stringBuilder = new StringBuilder();
			if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType) && StringUtils.isNotBlank(one.getSubjectIds())) {
				for (String tmp : one.getSubjectIds().split(",")) {
					stringBuilder.append("," + courseMap.get(tmp).getSubjectName());
				}
				one.setSubNames(stringBuilder.substring(1));
				showClassList.add(one);
			}
			if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(planType) && StringUtils.isNotBlank(one.getSubjectIdsB())) {
				for (String tmp : one.getSubjectIdsB().split(",")) {
					stringBuilder.append("," + courseMap.get(tmp).getSubjectName());
				}
				one.setSubNames(stringBuilder.substring(1));
				showClassList.add(one);
			}
		}
		for (NewGkDivideClass one : mixClassList) {
			StringBuilder stringBuilder = new StringBuilder();
			if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(planType) && StringUtils.isNotBlank(one.getSubjectIdsB())) {
				for (String tmp : one.getSubjectIdsB().split(",")) {
					stringBuilder.append("," + courseMap.get(tmp).getSubjectName());
				}
				one.setSubNames(stringBuilder.substring(1));
				showClassList.add(one);
			}
		}
		List<NewGkDivideClass> teachClassList = allClassList.stream()
				.filter(e -> NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType()) && Objects.equals(planType, e.getSubjectType()))
				.collect(Collectors.toList());
		Map<String, List<NewGkDivideClass>> combinationToTeachClassMap = new HashMap<>();
		for (NewGkDivideClass one : teachClassList) {
			if (StringUtils.isNotBlank(one.getRelateId())) {
				if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType) && allClassMap.get(one.getRelateId()).getSubjectIds() != null && allClassMap.get(one.getRelateId()).getSubjectIds().indexOf(one.getSubjectIds()) > -1) {
					if (combinationToTeachClassMap.get(one.getRelateId()) == null) {
						combinationToTeachClassMap.put(one.getRelateId(), new ArrayList<>());
					}
					combinationToTeachClassMap.get(one.getRelateId()).add(one);
				}
				if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(planType) && allClassMap.get(one.getRelateId()).getSubjectIdsB() != null && allClassMap.get(one.getRelateId()).getSubjectIdsB().indexOf(one.getSubjectIds()) > -1) {
					if (combinationToTeachClassMap.get(one.getRelateId()) == null) {
						combinationToTeachClassMap.put(one.getRelateId(), new ArrayList<>());
					}
					combinationToTeachClassMap.get(one.getRelateId()).add(one);
				}
			}
		}

		showClassList.stream().forEach(e -> {
			e.setRelateName(combinationToBaseClassMap.get(e.getId())==null?"":combinationToBaseClassMap.get(e.getId()).getClassName());
		});

		map.put("combinationClassList", showClassList);
		map.put("combinationToTeachClassMap", combinationToTeachClassMap);

		// 处理批次数据
		List<NewGkClassBatch> classBatchList = classBatchService.findListByIn("divideId", new String[] {divideId});
		classBatchList = classBatchList.stream().filter(e -> Objects.equals(planType, e.getSubjectType())).collect(Collectors.toList());
		// Map<batch, Map<subjectId, Set<studentIds>>>
		Map<String,Map<String, Set<String>>> batchSubjectMap = new HashMap<>();
		// 各批次的统计数据
		for (NewGkClassBatch classBatch : classBatchList) {
			if(!openSubjectIds.contains(classBatch.getSubjectId())) {
				// 这门科目不走班
				continue;
			}
			List<String> subjectIdsTmp = Arrays.asList(classBatch.getSubjectIds().split(","));
			if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(planType)) {
				subjectIdsTmp = new ArrayList<>(CollectionUtils.subtract(choiceSubjectIds, subjectIdsTmp));
			}
			if (!batchSubjectMap.containsKey(classBatch.getBatch())) {
				batchSubjectMap.put(classBatch.getBatch(), new HashMap<>());
			}
			Map<String, Set<String>> subjectIdToStudentIds = batchSubjectMap.get(classBatch.getBatch());
			if (!subjectIdToStudentIds.containsKey(classBatch.getSubjectId())) {
				subjectIdToStudentIds.put(classBatch.getSubjectId(), new HashSet<>());
			}
			NewGkDivideClass classTmp = allClassMap.get(classBatch.getDivideClassId());

			if (classTmp != null && CollectionUtils.isNotEmpty(classTmp.getStudentList())) {
				for (String studentId : classTmp.getStudentList()) {
					/*if ((studentSetFirst != null && !studentSetFirst.contains(studentId))
							|| (studentSetSecond != null && !studentSetSecond.contains(studentId))
							|| (studentSetThird != null && !studentSetThird.contains(studentId))
							|| (studentSetFourth != null && !studentSetFourth.contains(studentId))) {
						continue;
					}*/
					boolean flag = false;
					for (String one : subjectIdsTmp) {
						if (!subjectChosenMap.get(one).contains(studentId)) {
							flag = true;
							break;
						}
					}
					if (flag) {
						continue;
					}
					subjectIdToStudentIds.get(classBatch.getSubjectId()).add(studentId);
				}
			}
		}

		// Map<batch, Map<subjectId, List<NewGkDivideClass>>>
		Map<String,Map<String, List<NewGkDivideClass>>> batchSubjectClassMap = new HashMap<>();
		for (NewGkDivideClass teachClass : teachClassList) {
			if (!batchSubjectClassMap.containsKey(teachClass.getBatch())) {
				batchSubjectClassMap.put(teachClass.getBatch(), new HashMap<>());
			}
			Map<String, List<NewGkDivideClass>> tmp = batchSubjectClassMap.get(teachClass.getBatch());
			if (!tmp.containsKey(teachClass.getSubjectIds())) {
				tmp.put(teachClass.getSubjectIds(), new ArrayList<>());
			}
			tmp.get(teachClass.getSubjectIds()).add(teachClass);
		}

		BatchClassDto batchClassDto;
		// 前端页面每一个批次总行数
		Map<String, String> batchRowCount = new HashMap<>();
		List<BatchClassDto> batchClassDtos = new ArrayList<>();
		for (String batch : batchSubjectMap.keySet()) {
			Map<String, Set<String>> subjectIdToStudentIdSetMap = batchSubjectMap.get(batch);
			Map<String, List<NewGkDivideClass>> subjectClassMap = batchSubjectClassMap.get(batch);
			if (subjectClassMap == null) {
				subjectClassMap = new HashMap<>();
			}

			for (String subjectId : subjectIdToStudentIdSetMap.keySet()) {
				List<NewGkDivideClass> independentTeachClassList = new ArrayList<>();
				List<NewGkDivideClass> combinationTeachClassList = new ArrayList<>();
				Set<String> studentIds = subjectIdToStudentIdSetMap.get(subjectId);
				List<NewGkDivideClass> classList = subjectClassMap.get(subjectId);
				int classStudentCount = 0;
				int independentClassStudnetTotalCount = 0;
 				if(classList != null) {
					Set<String> arrangeStudentIds = new HashSet<>();
					for (NewGkDivideClass one : classList) {
						if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType) && StringUtils.isNotBlank(one.getRelateId()) && allClassMap.get(one.getRelateId()).getSubjectIds() != null && allClassMap.get(one.getRelateId()).getSubjectIds().indexOf(one.getSubjectIds()) > -1) {
							NewGkDivideClass combinationTmp = allClassMap.get(one.getRelateId());
							one.setClassName((combinationTmp == null ? "无" : combinationTmp.getClassName()) + "-" + one.getClassName());
							one.setStudentCount(CollectionUtils.intersection(studentIds, one.getStudentList()).size());
							combinationTeachClassList.add(one);
							arrangeStudentIds.addAll(CollectionUtils.intersection(studentIds, one.getStudentList()));
						} else if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(planType) && StringUtils.isNotBlank(one.getRelateId()) && allClassMap.get(one.getRelateId()).getSubjectIdsB() != null && allClassMap.get(one.getRelateId()).getSubjectIdsB().indexOf(one.getSubjectIds()) > -1) {
							NewGkDivideClass combinationTmp = allClassMap.get(one.getRelateId());
							one.setClassName((combinationTmp == null ? "无" : combinationTmp.getClassName()) + "-" + one.getClassName());
							one.setStudentCount(CollectionUtils.intersection(studentIds, one.getStudentList()).size());
							combinationTeachClassList.add(one);
							arrangeStudentIds.addAll(CollectionUtils.intersection(studentIds, one.getStudentList()));
						} else {
							independentClassStudnetTotalCount += one.getStudentCount();
							independentTeachClassList.add(one);
							arrangeStudentIds.addAll(one.getStudentList());
						}
					}
					if (CollectionUtils.isNotEmpty(arrangeStudentIds)) {
						// studentIds与arrangeStuId的交集
						classStudentCount = CollectionUtils.intersection(studentIds, arrangeStudentIds).size();
					} else {
						classStudentCount = 0;
					}
				}

				batchClassDto = new BatchClassDto();
				batchClassDto.setBatch(batch);
				batchClassDto.setDevideClassList(independentTeachClassList);
				batchClassDto.setCombinationDivideClassList(combinationTeachClassList);
				batchClassDto.setStuNum(studentIds.size());
				batchClassDto.setFreeStuNum(studentIds.size() - classStudentCount);
				batchClassDto.setCourse(courseMap.get(subjectId));
				batchClassDto.setOtherInfo(independentClassStudnetTotalCount == 0 ? "" : String.valueOf(independentClassStudnetTotalCount));
				batchClassDtos.add(batchClassDto);
			}
		}
		map.put("batchList", batchClassDtos);

		Map<String, List<BatchClassDto>> batchDtoMap = EntityUtils.getListMap(batchClassDtos, BatchClassDto::getBatch, e -> e);
		map.put("batchMap", batchDtoMap);

		for (Map.Entry<String, List<BatchClassDto>> entry : batchDtoMap.entrySet()) {
			int rowCount = entry.getValue().size();
			for (BatchClassDto one : entry.getValue()) {
				rowCount += (one.getCombinationDivideClassList().size() > 1 ? (one.getCombinationDivideClassList().size() - 1) : 0);
			}
			batchRowCount.put(entry.getKey(), String.valueOf(rowCount));
		}
		map.put("batchRowCount", batchRowCount);

		return "/newgkelective/floatingPlan/teachClassSet.ftl";
	}

	@RequestMapping("/floatingPlan/teachClassStudentSet")
	public String teachClassStudentSet(@PathVariable String divideId, String planType, String subjectId, String batch, boolean isCombine, String teachClassId, ModelMap map) {
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		String unitId = divide.getUnitId();
        Set<String> choiceSubjectIds = EntityUtils.getSet(newGkChoRelationService.findChooseSubject(divide.getChoiceId(), unitId), Course::getId);
        Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
		map.put("course", course);

		// 定位上一次操作位置
		map.put("isCombine", isCombine);
		map.put("teachClassId", teachClassId);

		List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeIn(divideId, new String[] {planType});
		Set<String> openSubjectIdSet = EntityUtils.getSet(openSubjectList, e->e.getSubjectId());

		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(unitId), Course.class);
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);

		List<NewGkDivideClass> classList = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId, null, true,
				NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		Map<String, NewGkDivideClass> baseClassMap = classList.stream()
				.filter(e -> NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType()))
				.collect(Collectors.toMap(e -> e.getRelateId(), e -> e));
		Map<String, NewGkDivideClass> classMap = EntityUtils.getMap(classList, NewGkDivideClass::getId);
		List<NewGkDivideClass> mixClassList = classList.stream()
				.filter(e -> NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType()) && BaseConstants.ZERO_GUID.equals(e.getSubjectIds()))
				.collect(Collectors.toList());
        List<NewGkDivideClass> combinationClassList = classList.stream()
                    .filter(e -> NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType())
                            && !BaseConstants.ZERO_GUID.equals(e.getSubjectIds()))
                    .collect(Collectors.toList());
		List<NewGkDivideClass> allTeachClassList = classList.stream()
				.filter(e -> NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())
						&& Objects.equals(planType, e.getSubjectType()))
				.collect(Collectors.toList());
		List<NewGkDivideClass> teachClassList = classList.stream()
				.filter(e -> NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())
						&& Objects.equals(planType, e.getSubjectType())
						&& Objects.equals(subjectId, e.getSubjectIds())
						&& Objects.equals(batch, e.getBatch()))
				.collect(Collectors.toList());

		List<NewGkClassBatch> combinationClassBatchList = classBatchService.findByDivideClsIds(EntityUtils.getArray(combinationClassList, NewGkDivideClass::getId, String[]::new));
		Map<String, Set<String>> combinationClassIdToClassBatchSet = new HashMap<>();
		for (NewGkClassBatch one : combinationClassBatchList) {
			if (!Objects.equals(planType, one.getSubjectType())) {
				continue;
			}
			if (combinationClassIdToClassBatchSet.get(one.getDivideClassId()) == null) {
				combinationClassIdToClassBatchSet.put(one.getDivideClassId(), new HashSet<>());
			}
			combinationClassIdToClassBatchSet.get(one.getDivideClassId()).add(one.getBatch());
		}
		List<NewGkDivideClass> showClassList = new ArrayList<>();
        for (NewGkDivideClass one : combinationClassList) {
        	if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType) && StringUtils.isNotBlank(one.getSubjectIds()) && one.getSubjectIds().contains(subjectId)) {
        		if (combinationClassIdToClassBatchSet.get(one.getId()) != null && combinationClassIdToClassBatchSet.get(one.getId()).contains(batch)) {
        			continue;
				}
        		showClassList.add(one);
			}
        	if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(planType) && StringUtils.isNotBlank(one.getSubjectIdsB()) && one.getSubjectIdsB().contains(subjectId)) {
				if (combinationClassIdToClassBatchSet.get(one.getId()) != null && combinationClassIdToClassBatchSet.get(one.getId()).contains(batch)) {
					continue;
				}
        		showClassList.add(one);
			}
		}
        if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(planType)) {
        	List<NewGkClassBatch> mixClassBatchList = classBatchService.findByDivideClsIds(EntityUtils.getArray(mixClassList, NewGkDivideClass::getId, String[]::new));
        	Map<String, Set<String>> mixClassIdToClassBatchSet = new HashMap<>();
        	for (NewGkClassBatch one : mixClassBatchList) {
        		if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(one.getSubjectType())) {
        			continue;
				}
        		if (mixClassIdToClassBatchSet.get(one.getDivideClassId()) == null) {
        			mixClassIdToClassBatchSet.put(one.getDivideClassId(), new HashSet<>());
				}
				mixClassIdToClassBatchSet.get(one.getDivideClassId()).add(one.getBatch());
			}
        	for (NewGkDivideClass one : mixClassList) {
        		if (StringUtils.isNotBlank(one.getSubjectIdsB()) && one.getSubjectIdsB().contains(subjectId)) {
        			if (mixClassIdToClassBatchSet.get(one.getId()) != null && mixClassIdToClassBatchSet.get(one.getId()).contains(batch)) {
        				continue;
					}
        			showClassList.add(one);
				}
			}
		}

        Set<String> combinationStudentSet = new HashSet<>();
        showClassList.stream().forEach(e -> {
        	if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType)) {
        		StringBuilder stringBuilder = new StringBuilder();
        		for (String one : e.getSubjectIds().split(",")) {
        			stringBuilder.append("," + courseMap.get(one).getSubjectName());
				}
        		e.setSubNames(stringBuilder.substring(1));
			}
			if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(planType)) {
				StringBuilder stringBuilder = new StringBuilder();
				for (String one : e.getSubjectIdsB().split(",")) {
					stringBuilder.append("," + courseMap.get(one).getSubjectName());
				}
				e.setSubNames(stringBuilder.substring(1));
			}
			e.setClassName(baseClassMap.get(e.getId()).getClassName());
			if (CollectionUtils.isNotEmpty(e.getStudentList())) {
				combinationStudentSet.addAll(e.getStudentList());
			}
		});

		List<Student> combinationStudents = SUtils.dt(studentRemoteService.findPartStudByGradeId(unitId, null, null, combinationStudentSet.toArray(new String[0])), new TR<List<Student>>() {});
		Set<String> maleIdSet = new HashSet<>();
		Set<String> femaleIdSet = new HashSet<>();
		for (Student one : combinationStudents) {
			if (Integer.valueOf(BaseConstants.MALE).equals(one.getSex())) {
				maleIdSet.add(one.getId());
			}
			if (Integer.valueOf(BaseConstants.FEMALE).equals(one.getSex())) {
				femaleIdSet.add(one.getId());
			}
		}
		showClassList.stream().forEach(e -> {
			int maleTmp = 0;
			int femaleTmp = 0;
			if (CollectionUtils.isNotEmpty(e.getStudentList())) {
				for (String studentId : e.getStudentList()) {
					if (maleIdSet.contains(studentId)) {
						maleTmp++;
					}
					if (femaleIdSet.contains(studentId)) {
						femaleTmp++;
					}
				}
			}
			e.setBoyCount(maleTmp);
			e.setGirlCount(femaleTmp);
		});

		// 获取选课数据
		Map<String,Set<String>> subjectChosenMap = new HashMap<>();
		Map<String,Set<String>> studentChosenMap = new HashMap<>();
		Map<String,Set<String>> studentChosenBMap = new HashMap<>();
		makeStudentChooseResult(divide, choiceSubjectIds, subjectChosenMap, studentChosenMap, studentChosenBMap, planType);

		List<NewGkClassBatch> batchClassList = classBatchService.findbyBatchAndSubjectId(divideId, batch, subjectId);
		batchClassList = batchClassList.stream().filter(e -> Objects.equals(planType, e.getSubjectType())).collect(Collectors.toList());

		Set<String> studentIds = new HashSet<>();

		// 混合班数据
		for (NewGkClassBatch one : batchClassList) {
			List<String> subjectIdsTmp = Arrays.asList(one.getSubjectIds().split(","));
			if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(planType)) {
				subjectIdsTmp = new ArrayList<>(CollectionUtils.subtract(choiceSubjectIds, subjectIdsTmp));
			}
			NewGkDivideClass classTmp = classMap.get(one.getDivideClassId());
			if (classTmp == null || NewGkElectiveConstant.CLASS_TYPE_2.equals(classTmp.getClassType()) || CollectionUtils.isEmpty(classTmp.getStudentList())) {
				continue;
			}
			/*Set<String> studentSetFirst = null;
			if (subjectIdsTmp.size() > 0) {
				studentSetFirst = subjectChosenMap.get(subjectIdsTmp.get(0));
			}
			Set<String> studentSetSecond = null;
			if (subjectIdsTmp.size() > 1) {
				studentSetSecond = subjectChosenMap.get(subjectIdsTmp.get(1));
			}
			Set<String> studentSetThird = null;
			if (subjectIdsTmp.size() > 2) {
				studentSetThird = subjectChosenMap.get(subjectIdsTmp.get(2));
			}
			Set<String> studentSetFourth = null;
			if (subjectIdsTmp.size() > 3) {
				studentSetFourth = subjectChosenMap.get(subjectIdsTmp.get(3));
			}*/

			for (String studentId : classTmp.getStudentList()) {
				/*if ((studentSetFirst != null && !studentSetFirst.contains(studentId))
						|| (studentSetSecond != null && !studentSetSecond.contains(studentId))
						|| (studentSetThird != null && !studentSetThird.contains(studentId))
						|| (studentSetFourth != null && !studentSetFourth.contains(studentId))) {
					continue;
				}*/

				boolean flag = false;
				for (String sub : subjectIdsTmp) {
					if (!subjectChosenMap.get(sub).contains(studentId)) {
						flag = true;
						break;
					}
				}
				if (flag) {
					continue;
				}
				studentIds.add(studentId);
			}
		}

		/*Set<String> subjectIdSetTmp;
		for (NewGkDivideClass one : combinationClassList) {
			if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType) && StringUtils.isNotBlank(one.getSubjectIds())) {
				StringBuilder subjectNames = new StringBuilder();
				for (String subjectIdTmp : one.getSubjectIds().split(",")) {
					subjectNames.append(courseMap.get(subjectIdTmp).getSubjectName() + ",");
				}
				one.setSubNames(subjectNames.substring(0, subjectNames.length() - 1));
			}
            if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(planType) && StringUtils.isNotBlank(one.getSubjectIdsB())) {
                StringBuilder subjectNames = new StringBuilder();
                for (String subjectIdTmp : one.getSubjectIdsB().split(",")) {
                    subjectNames.append(courseMap.get(subjectIdTmp).getSubjectName() + ",");
                }
                one.setSubNames(subjectNames.substring(0, subjectNames.length() - 1));
            }
			// 一科或两科固定
			if (NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(one.getSubjectType()) || NewGkElectiveConstant.SUBJTCT_TYPE_1.equals(one.getSubjectType())) {
				if (one.getSubjectIds().indexOf(subjectId) > -1) {
					continue;
				}
				if (CollectionUtils.isNotEmpty(one.getStudentList())) {
					for (String studentId : one.getStudentList()) {
						subjectIdSetTmp = studentChosenMap.get(studentId);
						if (subjectIdSetTmp.contains(subjectId)) {
							studentIds.add(studentId);
						}
					}
				}
			}
		}*/

		List<NewGkDivideClass> independentTeachClassList = new ArrayList<>();
		Map<String, List<NewGkDivideClass>> combinationToTeachClassMap = new HashMap<>();
		for (NewGkDivideClass one : allTeachClassList) {
			if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType) && StringUtils.isNotBlank(one.getRelateId()) && classMap.get(one.getRelateId()).getSubjectIds() != null && classMap.get(one.getRelateId()).getSubjectIds().indexOf(one.getSubjectIds()) > -1) {
				if (combinationToTeachClassMap.get(one.getRelateId()) == null) {
					combinationToTeachClassMap.put(one.getRelateId(), new ArrayList<>());
				}
				StringBuilder stringBuilder = new StringBuilder();
				if (CollectionUtils.isNotEmpty(one.getStudentList())) {
					for (String student : one.getStudentList()) {
						if (classMap.get(one.getRelateId()).getStudentList() != null && classMap.get(one.getRelateId()).getStudentList().indexOf(student) > -1) {
							continue;
						}
						stringBuilder.append("," + "\"" + student + "\"");
					}
					if (stringBuilder.length() > 0) {
						one.setStuIdStr(stringBuilder.substring(1));
					} else {
						one.setStuIdStr("");
					}
				} else {
					one.setStuIdStr("");
				}
				one.setRelateName(courseMap.get(one.getSubjectIds()).getSubjectName());
				// 将值修改为需要走班的学生数目
				// one.setStudentCount(CollectionUtils.intersection(studentIds, one.getStudentList()).size());
				// 若该教学班对应该时间点该科目，放到首位
				if (Objects.equals(one.getBatch(), batch) && classMap.get(one.getRelateId()) != null) {
					if (Objects.equals(one.getSubjectIds(), subjectId)) {
						classMap.get(one.getRelateId()).setRelateId(one.getId());
					} else {
						// 代表该批次点下已存在其他科目的合班，不能再合班
						classMap.get(one.getRelateId()).setClassType("1");
					}
					combinationToTeachClassMap.get(one.getRelateId()).add(0, one);
				} else {
					// 不同批次点下相同科目的合班
					if (Objects.equals(subjectId, one.getSubjectIds())) {
						classMap.get(one.getRelateId()).setClassType("1");
					}
					combinationToTeachClassMap.get(one.getRelateId()).add(one);
				}
			} else if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(planType) && StringUtils.isNotBlank(one.getRelateId()) && classMap.get(one.getRelateId()).getSubjectIdsB() != null && classMap.get(one.getRelateId()).getSubjectIdsB().indexOf(one.getSubjectIds()) > -1) {
				if (combinationToTeachClassMap.get(one.getRelateId()) == null) {
					combinationToTeachClassMap.put(one.getRelateId(), new ArrayList<>());
				}
				StringBuilder stringBuilder = new StringBuilder();
				if (CollectionUtils.isNotEmpty(one.getStudentList())) {
					for (String student : one.getStudentList()) {
						if (classMap.get(one.getRelateId()).getStudentList() != null && classMap.get(one.getRelateId()).getStudentList().indexOf(student) > -1) {
							continue;
						}
						stringBuilder.append("," + "\"" + student + "\"");
					}
					if (stringBuilder.length() > 0) {
						one.setStuIdStr(stringBuilder.substring(1));
					} else {
						one.setStuIdStr("");
					}
				} else {
					one.setStuIdStr("");
				}
				one.setRelateName(courseMap.get(one.getSubjectIds()).getSubjectName());
				// 将值修改为需要走班的学生数目
				// one.setStudentCount(CollectionUtils.intersection(studentIds, one.getStudentList()).size());
				// 若该教学班对应该时间点，放到首位
				if (Objects.equals(one.getBatch(), batch) && classMap.get(one.getRelateId()) != null) {
					if (Objects.equals(one.getSubjectIds(), subjectId)) {
						classMap.get(one.getRelateId()).setRelateId(one.getId());
					} else {
						// 代表该批次点下已存在其他科目的合班，不能再合班
						classMap.get(one.getRelateId()).setClassType("1");
					}
					combinationToTeachClassMap.get(one.getRelateId()).add(0, one);
				} else {
					// 不同批次点下相同科目的合班
					if (Objects.equals(subjectId, one.getSubjectIds())) {
						classMap.get(one.getRelateId()).setClassType("1");
					}
					combinationToTeachClassMap.get(one.getRelateId()).add(one);
				}
			} else {
				if (Objects.equals(one.getBatch(), batch) && Objects.equals(one.getSubjectIds(), subjectId)) {
					StringBuilder stringBuilder = new StringBuilder();
					if (CollectionUtils.isNotEmpty(one.getStudentList())) {
						for (String student : one.getStudentList()) {
							stringBuilder.append("," + "\"" + student + "\"");
						}
						one.setStuIdStr(stringBuilder.substring(1));
					} else {
						one.setStuIdStr("");
					}
					independentTeachClassList.add(one);
				}
			}
		}
		
		map.put("combinationClassList", showClassList);
		map.put("independentTeachClassList", independentTeachClassList);
		map.put("combinationToTeachClassMap", combinationToTeachClassMap);

		Set<String> teachClassStudentIdSet = teachClassList.stream().flatMap(e -> e.getStudentList().stream()).collect(Collectors.toSet());
		List<Student> students = SUtils.dt(studentRemoteService.findPartStudByGradeId(unitId, null, null, studentIds.toArray(new String[0])), new TR<List<Student>>() {});
		List<Clazz> baseClassList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(divide.getUnitId(), divide.getGradeId()), Clazz.class);
		List<StudentResultDto> studentResultDtoList = makeStudentDto(divide, course, students, classList, baseClassList, courseMap, studentChosenMap);
		List<StudentResultDto> unSolveStudentList = new ArrayList<>();
		List<StudentResultDto> solveStudentList = new ArrayList<>();
		for (StudentResultDto one : studentResultDtoList) {
			if (teachClassStudentIdSet.contains(one.getStudentId())) {
				solveStudentList.add(one);
			} else {
				unSolveStudentList.add(one);
			}
		}

		map.put("solveStudentList", solveStudentList);
		map.put("unSolveStudentList", unSolveStudentList);
		map.put("courseList", courseList);
		map.put("batch", batch);
		map.put("subjectId", subjectId);
		map.put("divide", divide);
		map.put("planType", planType);
		if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(planType)) {
			map.put("batchCount", divide.getBatchCountTypea());
		} else {
			map.put("batchCount", divide.getBatchCountTypeb());
		}
		return "/newgkelective/floatingPlan/teachClassStudentSet.ftl";
	}

    @RequestMapping("/floatingPlan/schedulingEdit/page")
    public String schedulingEdit(@PathVariable String divideId, String subjectType, String batch, String subjectIds, String stuIdStr, ModelMap map) {
        NewGkDivide newDivide = newGkDivideService.findById(divideId);
        if (newDivide == null) {
            return errorFtl(map, "分班方案不存在");
        }
        map.put("divideId", divideId);

        Set<String> subjectIdSet = new HashSet<>();
        String[] subjectIdArr = subjectIds.split(",");
        for (String subject : subjectIdArr) {
            if (StringUtils.isNotBlank(subject)) {
                subjectIdSet.add(subject);
            }
        }

        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdSet.toArray(new String[0])), new TR<List<Course>>() {});
        Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
		String teachClassName = "";
		for (String subject : subjectIdArr) {
			if (StringUtils.isNotBlank(subject)) {
				teachClassName += courseMap.get(subject).getSubjectName();
			}
		}
		if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
			teachClassName += "选";
		} else {
			teachClassName += "学";
		}

		List<NewGkDivideClass> teachClassList = newGkDivideClassService.findClassBySubjectIdsWithMaster(newDivide.getUnitId(), divideId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_2, subjectIds, false);
        int k = 1;
        if (CollectionUtils.isNotEmpty(teachClassList)) {
            List<String> teachClassNameList = EntityUtils.getList(teachClassList, NewGkDivideClass::getClassName);
            while (true) {
                if (!teachClassNameList.contains(teachClassName + k + "班")) {
                    break;
                }
                k++;
            }
        }
        map.put("className", teachClassName + k + "班");
        map.put("subjectType", subjectType);
        map.put("batch", batch);
        map.put("subjectIds", subjectIds);
        map.put("divideId", divideId);
        map.put("stuIdStr", stuIdStr);
        return "/newgkelective/floatingPlan/schedulingEdit.ftl";
    }

	@ResponseBody
	@RequestMapping("/floatingPlan/saveClass")
	public String saveGroupClass(@PathVariable String divideId, NewGkDivideClass newGkDivideClass) {
		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			return error("分班方案不存在");
		}
		if (StringUtils.isBlank(newGkDivideClass.getId())) {
			newGkDivideClass.setId(UuidUtils.generateUuid());
		}
		NewGkDivideClass combinationClass = null;
		if (StringUtils.isNotBlank(newGkDivideClass.getRelateId())) {
		    combinationClass = newGkDivideClassService.findById(getLoginInfo().getUnitId(), newGkDivideClass.getRelateId(), true);
		    if (combinationClass == null) {
                return error("相关联的组合班不存在！");
            }
        }
		if (StringUtils.isBlank(newGkDivideClass.getSubjectIds())) {
			return error("数据错误！");
		}
		Course course = SUtils.dc(courseRemoteService.findOneById(newGkDivideClass.getSubjectIds()), Course.class);
		// 验证名字是否重复
		List<NewGkDivideClass> groupClassList = newGkDivideClassService
				.findClassBySubjectIdsWithMaster(newDivide.getUnitId(), divideId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1,
						newGkDivideClass.getClassType(), newGkDivideClass.getSubjectIds(), false);
		if (CollectionUtils.isNotEmpty(groupClassList)) {
			List<String> groupNameList = EntityUtils.getList(groupClassList, NewGkDivideClass::getClassName);
			if (groupNameList.contains(newGkDivideClass.getClassName())) {
				return error("班级名称重复！");
			}
		}
		List<NewGkClassStudent> insertStudentList = new ArrayList<>();
		NewGkClassStudent teachClassStudent;
		if (StringUtils.isNotBlank(newGkDivideClass.getStuIdStr())) {
			String[] studentIdArr = newGkDivideClass.getStuIdStr().split(",");
			for (String tmp : studentIdArr) {
				teachClassStudent = initClassStudent(newDivide.getUnitId(), divideId, newGkDivideClass.getId(), tmp);
				insertStudentList.add(teachClassStudent);
			}
		}
		// 若与组合班关联，将组合班的学生也加入该班级
        if (StringUtils.isNotBlank(newGkDivideClass.getRelateId()) && combinationClass.getStudentList() != null) {
        	Set<String> openSubjectIdSet = EntityUtils.getSet(newGkOpenSubjectService.findByDivideIdAndSubjectTypeIn(divideId, new String[] {newGkDivideClass.getSubjectType()}), NewGkOpenSubject::getSubjectId);
        	Map<String, Set<String>> subjectChosenMap = new HashMap<>();
        	Map<String, Set<String>> studentChosenMap = new HashMap<>();
        	makeStudentChooseResult(newDivide, EntityUtils.getSet(newGkChoRelationService.findChooseSubject(newDivide.getChoiceId(), newDivide.getUnitId()), Course::getId), subjectChosenMap, subjectChosenMap, studentChosenMap, newGkDivideClass.getSubjectType());
        	Set<String> studentIdSet = subjectChosenMap.get(newGkDivideClass.getSubjectIds());
            for (String studentId : combinationClass.getStudentList()) {
            	if (studentIdSet.contains(studentId)) {
					teachClassStudent = initClassStudent(newDivide.getUnitId(), divideId, newGkDivideClass.getId(), studentId);
					insertStudentList.add(teachClassStudent);
				}
            }
        }
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", true);
		jsonObject.put("code", "00");
		jsonObject.put("msg", "保存成功");
		jsonObject.put("id", newGkDivideClass.getId());
		jsonObject.put("relateId", newGkDivideClass.getId());
		if (StringUtil.isBlank(newGkDivideClass.getRelateId())) {
			jsonObject.put("className", newGkDivideClass.getClassName());
		} else {
			if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(newGkDivideClass.getSubjectType())) {
				jsonObject.put("className", NewGkElectiveConstant.EXAM_BATCH.get(newGkDivideClass.getBatch()) + "(" + course.getSubjectName() + "-<em class=\"stu-count\">" + insertStudentList.size() + "</em>)");
			} else {
				jsonObject.put("className", NewGkElectiveConstant.STUDY_BATCH.get(newGkDivideClass.getBatch()) + "(" + course.getSubjectName() + "-<em class=\"stu-count\">" + insertStudentList.size() + "</em>)");
			}
		}
		newGkDivideClass.setSubjectType(newGkDivideClass.getSubjectType());
		newGkDivideClass.setBatch(newGkDivideClass.getBatch());
		newGkDivideClass.setDivideId(divideId);
		newGkDivideClass.setCreationTime(new Date());
		newGkDivideClass.setModifyTime(new Date());
		newGkDivideClass.setIsHand(NewGkElectiveConstant.IS_HAND_1);
		newGkDivideClass.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
		List<NewGkDivideClass> insertClassList = new ArrayList<>();
		insertClassList.add(newGkDivideClass);
		try {
			// 考虑班级学生重复：理论上新增班级 不会出现重复
			newGkDivideClassService.saveAllList(null, null,
					null, insertClassList, insertStudentList, false);
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！");
		}
		return jsonObject.toJSONString();
	}

	@ResponseBody
	@RequestMapping("/floatingPlan/teachClassSaveStu")
	public String teachClassStudentSave(@PathVariable String divideId, String teachClassId, String studentIds, String append, ModelMap map) {

		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			return errorFtl(map, "分班方案不存在");
		}
		if (StringUtils.isNotBlank(teachClassId)) {
			NewGkDivideClass divideClass = newGkDivideClassService.findById(newDivide.getUnitId(), teachClassId, false);
			if (divideClass == null || (!divideClass.getDivideId().equals(divideId))) {
				return error("该班级不存在！");
			}
			String subjectIds = divideClass.getSubjectIds();
			Set<String> subjectIdSet = new HashSet<>();
			String[] subjectIdArr = subjectIds.split(",");
			for (String tmp : subjectIdArr) {
				if (StringUtils.isNotBlank(tmp)) {
					subjectIdSet.add(tmp);
				}
			}
			Set<String> studentIdSet = new HashSet<>();
			if (StringUtils.isNotBlank(studentIds)) {
				String[] arr = studentIds.split(",");
				if (arr.length > 0) {
					// 判断当前
					String error = checkGroupStu(newDivide, arr, subjectIdSet);
					if (StringUtils.isNotBlank(error)) {
						return error(error);
					}
				}
				for (int i = 0; i < arr.length; i++) {
					if (StringUtils.isNotBlank(arr[i])) {
						studentIdSet.add(arr[i]);
					}
				}
			}
			List<NewGkClassStudent> insertStudentList = new ArrayList<>();
			NewGkClassStudent groupClassStudent;
			for (String tmp : studentIdSet) {
				groupClassStudent = initClassStudent(newDivide.getUnitId(), newDivide.getId(), teachClassId, tmp);
				insertStudentList.add(groupClassStudent);
			}
			List<NewGkDivideClass> updateClassList = new ArrayList<>();
			updateClassList.add(divideClass);

			try {
				if (BaseConstants.ONE_STR.equals(append)) {
					newGkClassStudentService.saveAllList(insertStudentList);
				} else {
					newGkDivideClassService.saveAllList(newDivide.getUnitId(), newDivide.getId(),
							new String[]{teachClassId}, updateClassList, insertStudentList, false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return error("保存失败！");
			}
		} else {
			return error("该班级不存在！");
		}
		return success("保存成功！");
	}

	@ResponseBody
	@RequestMapping("/floatingPlan/autoOpenClass")
	public String autoOpenClassByClassNum(@PathVariable String divideId, String subjectType, String batch, String subjectId, int openNum, String stuIds) {

		if (StringUtils.isBlank(subjectId)) {
			return error("没有选中组合");
		}
		if (StringUtils.isBlank(stuIds)) {
			return error("未选择学生");
		}
		if (openNum <= 0) {
			return error("开设班级数应为正整数");
		}

		NewGkDivide newDivide = newGkDivideService.findById(divideId);
		if (newDivide == null) {
			return error("分班方案不存在");
		}

		String unitId = getLoginInfo().getUnitId();

		Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
		String teachClassName;
		if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
			teachClassName = course.getSubjectName() + "选";
		} else {
			teachClassName = course.getSubjectName() + "学";
		}
		List<NewGkDivideClass> teachClassList = newGkDivideClassService.findClassBySubjectIds(newDivide.getUnitId(), divideId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_2, subjectId, false);
		int k = 1;
		if (CollectionUtils.isNotEmpty(teachClassList)) {
			List<String> teachClassNameList = EntityUtils.getList(teachClassList, NewGkDivideClass::getClassName);
			while (true) {
				if (!teachClassNameList.contains(teachClassName + k + "班")) {
					break;
				}
				k++;
			}
		}

		// 新增班级
		List<NewGkClassStudent> insertStudentList = new ArrayList<>();
		List<NewGkDivideClass> insertClassList = new ArrayList<>();
		NewGkDivideClass newGkDivideClass;
		NewGkClassStudent newGkClassStudent;

		for (int i = 0; i < openNum; i++) {
			int tmp = k + i;
			newGkDivideClass = initNewGkDivideClass(divideId, subjectId, NewGkElectiveConstant.CLASS_TYPE_2);
			newGkDivideClass.setBatch(batch);
			newGkDivideClass.setSubjectType(subjectType);
			newGkDivideClass.setClassName(teachClassName + tmp + "班");
			insertClassList.add(newGkDivideClass);
		}
		int index = 0;
		for (String studentId : stuIds.split(",")) {
			newGkClassStudent = initClassStudent(unitId, divideId, insertClassList.get(index++).getId(), studentId);
			insertStudentList.add(newGkClassStudent);
			if (index == openNum) {
				index = 0;
			}
		}

		try {
			newGkDivideClassService.saveAllList(null, null,
					null, insertClassList, insertStudentList, false);
		} catch (Exception e) {
			e.printStackTrace();
			return error("分班失败！");
		}
		return success("分班成功！");
	}

	@ResponseBody
	@RequestMapping("/floatingPlan/deleteTeachClass")
	public String deleteManualClass(String classIds) {
		if(StringUtils.isBlank(classIds)) {
			return error("参数丢失");
		}
		NewGkDivideClass divideClass = newGkDivideClassService.findOne(classIds);
		if (divideClass == null) {
			return error("所选班级不存在");
		}
		// 走班安排产生的教学班删除限制，暂时放开
		/*if (StringUtils.isNotBlank(divideClass.getRelateId())) {
			NewGkDivideClass combinationClass = newGkDivideClassService.findOne(divideClass.getRelateId());
			if (combinationClass == null) {
				// 一般不会为空
				return error("error");
			}
			if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(divideClass.getSubjectType()) && combinationClass.getSubjectIds().indexOf(divideClass.getSubjectIds()) < 0) {
				return error("走班安排产生的教学班无法在这里删除");
			}
			if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(divideClass.getSubjectType()) && combinationClass.getSubjectIdsB().indexOf(divideClass.getSubjectIds()) < 0) {
				return error("走班安排产生的教学班无法在这里删除");
			}
		}*/
		try {
			newGkDivideClassService.deleteById(this.getLoginInfo().getUnitId(), divideClass.getDivideId(), classIds);
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		return returnSuccess();
	}

	@ResponseBody
	@RequestMapping("/floatingPlan/deleteTeachClassStudent")
	public String deleteTeachClassStudent(String teachClassId, String studentId) {
		if(StringUtils.isBlank(teachClassId) || StringUtils.isBlank(studentId)) {
			return error("参数丢失");
		}
		try {
			newGkClassStudentService.deleteByClassIdAndStuIdIn(teachClassId, new String[] {studentId});
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		return returnSuccess();
	}

	@RequestMapping("/floatingPlan/showTeachClassStudent")
	public String showTeachClassStudent(@PathVariable String divideId, String teachClassId, ModelMap map) {
		NewGkDivide divide = newGkDivideService.findById(divideId);
		String unitId = getLoginInfo().getUnitId();
		NewGkDivideClass newGkDivideClass = newGkDivideClassService.findById(unitId, teachClassId, true);
		List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeIn(divideId, new String[] {newGkDivideClass.getSubjectType()});
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(unitId), Course.class);
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
		Map<String, Set<String>> subjectChosenMap = new HashMap<>();
		Map<String, Set<String>> studentChosenMap = new HashMap<>();
        makeStudentChooseResult(divide, EntityUtils.getSet(newGkChoRelationService.findChooseSubject(divide.getChoiceId(), divide.getUnitId()), Course::getId), subjectChosenMap, studentChosenMap, studentChosenMap, newGkDivideClass.getSubjectType());
		List<Student> studentList = studentRemoteService.findListObjectByIds(newGkDivideClass.getStudentList().toArray(new String[0]));
		List<NewGkDivideClass> classList = newGkDivideClassService.findByDivideIdAndClassType(getLoginInfo().getUnitId(), divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_1}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<Clazz> baseClassList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(divide.getUnitId(), divide.getGradeId()), Clazz.class);
		List<StudentResultDto> studentResultDtoList = makeStudentDto(divide, courseMap.get(newGkDivideClass.getSubjectIds()), studentList, classList, baseClassList, courseMap, studentChosenMap);
		map.put("teachClassId", teachClassId);
		map.put("subjectType", newGkDivideClass.getSubjectType());
		map.put("studentList", studentResultDtoList);
		return "/newgkelective/floatingPlan/showTeachClassStudent.ftl";
	}

	@RequestMapping("/floatingPlan/renameTeachClass")
	public String renameTeachClass(@PathVariable String divideId, String teachClassId, ModelMap map) {
		String unitId = getLoginInfo().getUnitId();
		NewGkDivideClass newGkDivideClass = newGkDivideClassService.findById(unitId, teachClassId, false);
		map.put("teachClassId", teachClassId);
		map.put("teachClassName", newGkDivideClass.getClassName());
		map.put("subjectType", newGkDivideClass.getSubjectType());
		return "/newgkelective/floatingPlan/teachClassRename.ftl";
	}

	@RequestMapping("/floatingPlan/renameTeachClassSave")
	@ResponseBody
	public String renameTeachClassSave(@PathVariable String divideId, String teachClassId, String teachClassName) {
		String unitId = getLoginInfo().getUnitId();
		if (StringUtils.isBlank(teachClassName)) {
			return error("班级名称不能为空");
		}
		if (StringUtils.isBlank(divideId) || StringUtils.isBlank(teachClassId) || StringUtils.isBlank(teachClassName)) {
			return error("参数缺失");
		}
		teachClassName=StringUtils.trim(teachClassName);
		List<NewGkDivideClass> list = newGkDivideClassService.findListBy(new String[] {"divideId","className"}, new String[] {divideId,teachClassName});
		NewGkDivideClass newGkDivideClass = newGkDivideClassService.findOne(teachClassId);
		if(newGkDivideClass==null) {
			return error("班级已经不存在");
		}
		if(CollectionUtils.isNotEmpty(list)) {
			List<NewGkDivideClass> list2 = list.stream().filter(e->e.getClassType().equals(newGkDivideClass.getClassType()))
			.filter(e->!e.getId().equals(newGkDivideClass.getId())).collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(list2)) {
				return error("班级名称重复");
			}
		}
		newGkDivideClass.setClassName(teachClassName);
		try {
			newGkDivideClassService.save(newGkDivideClass);
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		return returnSuccess();
	}

	private String checkGroupStu(NewGkDivide divide, String[] studentIdArr, Set<String> subjectIdSet) {
		List<NewGkChoResult> resultList = makeChoByBak(divide, Arrays.asList(studentIdArr));
		// 组装学生选课数据
		List<StudentResultDto> stuSubjectList = resultToDto(studentIdArr, resultList);
		if (CollectionUtils.isEmpty(resultList)) {
			return "保存失败，所选的学生的选课记录已经不存在！";
		}
		Set<String> errorStudentId = new HashSet<>();
		for (StudentResultDto one : stuSubjectList) {
			if (CollectionUtils.isEmpty(one.getResultList())) {
				errorStudentId.add(one.getStudentId());
				continue;
			}
			Set<String> chooseSubjectIdSet = EntityUtils.getSet(one.getResultList(), NewGkChoResult::getSubjectId);
			if (CollectionUtils.isEmpty(chooseSubjectIdSet)) {
				errorStudentId.add(one.getStudentId());
				continue;
			}
			if (CollectionUtils.union(chooseSubjectIdSet, subjectIdSet).size() != chooseSubjectIdSet.size()) {
				errorStudentId.add(one.getStudentId());
				continue;
			}
		}
		String errorStr = "";
		if (errorStudentId.size() > 0) {
			List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(errorStudentId.toArray(new String[] {})),
					new TR<List<Student>>() {});
			for (Student tmp : studentList) {
				errorStr = errorStr + "、" + tmp.getStudentName();
			}
			if (StringUtils.isNotBlank(errorStr)) {
				errorStr = errorStr.substring(1);
			}
			errorStr = "保存失败，其中有" + errorStr.substring(1) + "学生的选课组合与该班级科目组合不一致！";
		}
		return errorStr;
	}

	private List<StudentResultDto> resultToDto(String[] studentIdArr, List<NewGkChoResult> resultList) {
		Map<String, List<String>> studentIdToSubjectListMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(resultList)) {
			for (NewGkChoResult result : resultList) {
				if (!studentIdToSubjectListMap.containsKey(result.getStudentId())) {
					studentIdToSubjectListMap.put(result.getStudentId(), new ArrayList<>());
				}
				studentIdToSubjectListMap.get(result.getStudentId()).add(result.getSubjectId());
			}
		}
		List<StudentResultDto> returnList = new ArrayList<>();
		StudentResultDto studentResultDto;
		NewGkChoResult newGkChoResult;
		for (String studentId : studentIdArr) {
			studentResultDto = new StudentResultDto();
			studentResultDto.setStudentId(studentId);
			studentResultDto.setResultList(new ArrayList<>());
			if (studentIdToSubjectListMap.containsKey(studentId)) {
				for (String ss : studentIdToSubjectListMap.get(studentId)) {
					newGkChoResult = new NewGkChoResult();
					newGkChoResult.setSubjectId(ss);
					studentResultDto.getResultList().add(newGkChoResult);
				}
			}
		}
		return returnList;
	}

	/**
	 * @param divide
	 * @param subjectChosenMap K:科目 V：学生
	 * @param studentChosenMap K:学生 V：科目
	 */
	private void makeStudentChooseResult(NewGkDivide divide, Set<String> openSubjectIdSet, Map<String,Set<String>> subjectChosenMap, Map<String,Set<String>> studentChosenMap, Map<String,Set<String>> studentChosenBMap, String subjectType) {
		List<NewGkChoResult> chosenList = makeChoByBak(divide, null);
		
		for (NewGkChoResult result : chosenList) {
            if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
                if(!subjectChosenMap.containsKey(result.getSubjectId())) {
                    subjectChosenMap.put(result.getSubjectId(), new TreeSet<>());
                }
                subjectChosenMap.get(result.getSubjectId()).add(result.getStudentId());
            }
            if(!studentChosenMap.containsKey(result.getStudentId())) {
				studentChosenMap.put(result.getStudentId(), new TreeSet<>());
			}
			studentChosenMap.get(result.getStudentId()).add(result.getSubjectId());
		}
		if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subjectType)) {
		    // 取补集
		    for (String student : studentChosenMap.keySet()) {
		        studentChosenBMap.put(student, new TreeSet<>(CollectionUtils.subtract(openSubjectIdSet, studentChosenMap.get(student))));
            }
		    for (Map.Entry<String, Set<String>> entry : studentChosenBMap.entrySet()) {
		        for (String subject : entry.getValue()) {
		            if (subjectChosenMap.get(subject) == null) {
		                subjectChosenMap.put(subject, new TreeSet<>());
                    }
		            subjectChosenMap.get(subject).add(entry.getKey());
                }
            }
        }
	}

	/**
	 * @param combinationClassList 两科以及一科固定的班级，包括学生数据
	 * @param studentChosenMap 学生选课结果
	 * @return studentSetBySubjectIdAndClassId Map<classId, Map<subjectId, Set<studentId>>>
	 */
	private Map<String,Map<String,Set<String>>> makeStudentCombination(List<NewGkDivideClass> combinationClassList, Map<String,Set<String>> studentChosenMap, String subjectType) {
		Set<String> setTmp;
		Map<String, Map<String, Set<String>>> studentSetBySubjectIdAndClassId = new HashMap<>();
		if (CollectionUtils.isEmpty(combinationClassList)) {
			return studentSetBySubjectIdAndClassId;
		}
		for (NewGkDivideClass one : combinationClassList) {
			if (CollectionUtils.isNotEmpty(one.getStudentList())) {
				for (String studentId : one.getStudentList()) {
					setTmp = studentChosenMap.get(studentId);
					if (CollectionUtils.isEmpty(setTmp)) {
						continue;
					}
					for (String subjectId : setTmp) {
						if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType) && one.getSubjectIds().indexOf(subjectId) > -1) {
							continue;
						}
						if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subjectType) && one.getSubjectIdsB().indexOf(subjectId) > -1) {
						    continue;
                        }
						if (!studentSetBySubjectIdAndClassId.containsKey(one.getId())) {
							studentSetBySubjectIdAndClassId.put(one.getId(), new HashMap<>());
						}
						Map<String, Set<String>> map = studentSetBySubjectIdAndClassId.get(one.getId());
						if (!map.containsKey(subjectId)) {
							map.put(subjectId, new HashSet<>());
						}
						map.get(subjectId).add(studentId);
					}
				}
			}
		}
		return studentSetBySubjectIdAndClassId;
	}

	private List<StudentResultDto> makeStudentDto(NewGkDivide divide, Course course, List<Student> studentList,
								  List<NewGkDivideClass> classList, List<Clazz> baseClassList, Map<String, Course> courseMap, Map<String, Set<String>> studentChosenMap) {

		// 学生 -> 班级名称
		Map<String, String> studentClassMap = new HashMap<>();
		if (classList != null) {
			for (NewGkDivideClass clazz : classList) {
				if (NewGkElectiveConstant.CLASS_TYPE_1.equals(clazz.getClassType())) {
					clazz.getStudentList().forEach(e -> studentClassMap.put(e, clazz.getClassName()));
				}
			}
		}
		if (MapUtils.isEmpty(studentClassMap)) {
			Map<String, String> classNameTmp = new HashMap<>();
			for (Clazz clazz : baseClassList) {
				classNameTmp.put(clazz.getId(), clazz.getClassNameDynamic() == null ? clazz.getClassName() : clazz.getClassNameDynamic());
			}
			for (Student student : studentList) {
				studentClassMap.put(student.getId(), classNameTmp.get(student.getClassId()));
			}
		}

		List<Course> ysyCourses = SUtils.dt(courseRemoteService.findByCodesYSY(getLoginInfo().getUnitId()),Course.class);

		// 成绩
		String mcodeId = ColumnInfoUtils.getColumnInfo(Student.class, "sex").getMcodeId();
		Map<String, McodeDetail> codeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId(mcodeId), new TypeReference<Map<String, McodeDetail>>() {});
		Map<String, Map<String, Float>> stuScoreMap = getScoreMap(divide);
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
			if (scoreMap == null) {
				scoreMap = new HashMap<>();
			}
			for (Course one : ysyCourses) {
				scoreTmp = scoreMap.get(one.getId());
				ysyScore += (scoreTmp == null ? Float.valueOf(0.0f) : scoreTmp);
			}

			dto.getSubjectScore().put("YSY", ysyScore);
			if (course != null) {
				dto.getSubjectScore().put(course.getId(), scoreMap.get(course.getId()));
			}

			if (course != null) {
				dto.getSubjectScore().put("TOTAL", ysyScore + (scoreMap.get(course.getId()) == null ? 0.0f : scoreMap.get(course.getId())));
			}

			String className = studentClassMap.get(stu.getId());
			if (className == null) {
				dto.setClassName("未知");
			} else {
				dto.setClassName(className);
			}
			if (studentChosenMap != null && studentChosenMap.size() > 0 && studentChosenMap.get(stu.getId()) != null) {
				String chosenName = studentChosenMap.get(stu.getId()).stream().map(e -> courseMap.get(e).getShortName()).collect(Collectors.joining(""));
				dto.setChooseSubjects(chosenName);
			}
			dtoList.add(dto);
		}
		dtoList.sort(new Comparator<StudentResultDto>() {
			@Override
			public int compare(StudentResultDto o1, StudentResultDto o2) {
                if (o1.getStudentCode() == null || o2.getStudentCode() == null) {
                    return 0;
                }
                return o1.getStudentCode().compareTo(o2.getStudentCode());
            }
		});
		return dtoList;
	}

	private Map<String, Map<String, Float>> getScoreMap(NewGkDivide divide) {
		List<NewGkScoreResult> scoreList = newGkScoreResultService.findListByReferScoreId(divide.getUnitId(), divide.getReferScoreId(), true);
		Map<String, Map<String, Float>> stuScoreMap = new HashMap<>();
		for (NewGkScoreResult one : scoreList) {
			String studentId = one.getStudentId();
			if (!stuScoreMap.containsKey(one.getStudentId())) {
				stuScoreMap.put(one.getStudentId(), new HashMap<>());
			}
			stuScoreMap.get(studentId).put(one.getSubjectId(), one.getScore());
		}
		return stuScoreMap;
	}
	
	private Map<String, NewGkConditionDto> makeChoGroupMap(Map<String, List<String>> stuChoMap) {
		NewGkConditionDto dto;
		Map<String,NewGkConditionDto> dtoMap = new HashMap<>();
		for (String stuId : stuChoMap.keySet()) {
			List<String> subIds = stuChoMap.get(stuId);
			Collections.sort(subIds);
			String subIdstr = subIds.stream().collect(Collectors.joining(","));
			
			dto = dtoMap.get(subIdstr);
			if(dto == null) {
				dto = new NewGkConditionDto();
				dto.setSubjectIdstr(subIdstr);
				dto.setSubjectIds(new HashSet<>(subIds));
				dto.setSumNum(0);
				dtoMap.put(subIdstr, dto);
			}
			dto.getStuIds().add(stuId);
			dto.setSumNum(dto.getSumNum()+1);
		}
		return dtoMap;
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
}
