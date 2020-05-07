package net.zdsoft.newgkelective.data.action;

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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachBuilding;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.TeachBuildingRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.GkPlaceDto;
import net.zdsoft.newgkelective.data.dto.NewGkBuildingDto;
import net.zdsoft.newgkelective.data.dto.NewGkChoResultDto;
import net.zdsoft.newgkelective.data.dto.NewGkConditionDto;
import net.zdsoft.newgkelective.data.dto.NewGkItemDto;
import net.zdsoft.newgkelective.data.dto.NewGkPlaceItemDto;
import net.zdsoft.newgkelective.data.dto.NewGkSubjectTimeDto;
import net.zdsoft.newgkelective.data.dto.StudentResultDto;
import net.zdsoft.newgkelective.data.entity.NewGKStudentRangeEx;
import net.zdsoft.newgkelective.data.entity.NewGkArray;
import net.zdsoft.newgkelective.data.entity.NewGkArrayItem;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkOpenSubject;
import net.zdsoft.newgkelective.data.entity.NewGkPlaceItem;
import net.zdsoft.newgkelective.data.entity.NewGkReferScore;
import net.zdsoft.newgkelective.data.entity.NewGkScoreResult;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkplaceArrange;
import net.zdsoft.newgkelective.data.service.NewGKStudentRangeExService;
import net.zdsoft.newgkelective.data.service.NewGkArrayItemService;
import net.zdsoft.newgkelective.data.service.NewGkArrayService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkPlaceItemService;
import net.zdsoft.newgkelective.data.service.NewGkReferScoreService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkplaceArrangeService;
import net.zdsoft.newgkelective.data.utils.CombineAlgorithmInt;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;


/**
 *	分班
 */
@Controller
@RequestMapping("/newgkelective/{divideId}")
public class NewGkElectiveDivideAction extends NewGkElectiveDivideCommonAction{

	
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private NewGkArrayItemService newGkArrayItemService;
	@Autowired
	private NewGkSubjectTimeService newGkSubjectTimeService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private TeachBuildingRemoteService teachBuildingRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private NewGkplaceArrangeService newGkplaceArrangeService;
	@Autowired
	private NewGkArrayService newGkArrayService;
	@Autowired
	private NewGkPlaceItemService newGkPlaceItemService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private NewGkReferScoreService newGkReferScoreService;	
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private NewGKStudentRangeExService newGKStudentRangeExService;
	

	private Map<String, String> getCourseNameMap(Set<String> subjectIds) {
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
		Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId,Course::getSubjectName);
		return courseNameMap;
	}
    
    
    /**
     * 初始化课时数据
     * @param divideId
     * @param groupType
     * @param itemId
     * @return
     */
    private List<NewGkSubjectTime> initSubjectTimeList(String divideId, String groupType, String itemId) {
    	List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService.findByDivideIdAndGroupType(divideId, groupType);
    	List<NewGkSubjectTime> subjectTimeList = new ArrayList<NewGkSubjectTime>();
    	if(CollectionUtils.isNotEmpty(openSubjectList)){
    		Set<String> subjectIds = EntityUtils.getSet(openSubjectList, NewGkOpenSubject::getSubjectId);
    		Map<String, String> courseNameMap=getCourseNameMap(subjectIds);
    		NewGkSubjectTime item=null;
    		for(NewGkOpenSubject oo:openSubjectList){
    			item=new NewGkSubjectTime();
    			item.setSubjectId(oo.getSubjectId());
    			item.setSubjectType(oo.getSubjectType());
//    			item.setGroupType(oo.getGroupType());
    			item.setArrayItemId(itemId);
    			item.setWeekRowPeriod(2);
    			String courseName="";
    			if(courseNameMap.containsKey(oo.getSubjectId())){
    				courseName=courseNameMap.get(oo.getSubjectId());
    			}
    			if(NewGkElectiveConstant.DIVIDE_GROUP_1.equals(oo.getGroupType()) 
    					&& !(NewGkElectiveConstant.SUBJECT_TYPE_O.contains(oo.getSubjectType())
    					|| NewGkElectiveConstant.SUBJECT_TYPE_J.contains(oo.getSubjectType()))){
    				courseName=courseName+oo.getSubjectType();
    			}
        		item.setSubjectName(courseName);
    			subjectTimeList.add(item);
    		}
    	}
    	return subjectTimeList;
    }
    
    @RequestMapping("/subjectTimeArrange/update")
    @ControllerInfo(value = "修改课时安排")
    public String doEdit(@PathVariable("divideId") String divideId, String gradeId, String itemId, 
    		String arrayId, String groupType, ModelMap map) {
    	List<NewGkSubjectTime> subjectTimeList;
		if (StringUtils.isEmpty(groupType)) {
			subjectTimeList = newGkSubjectTimeService.findListBy("arrayItemId", itemId);
		} else {
			subjectTimeList = newGkSubjectTimeService.findByArrayItemIdAndGroupType(itemId, groupType);
		}
		if(CollectionUtils.isNotEmpty(subjectTimeList)){
    		Set<String> subjectIds = EntityUtils.getSet(subjectTimeList, NewGkSubjectTime::getSubjectId);
    		Map<String, String> courseNameMap=getCourseNameMap(subjectIds);
    		for(NewGkSubjectTime t:subjectTimeList){
    			String courseName="";
    			if(courseNameMap.containsKey(t.getSubjectId())){
    				courseName=courseNameMap.get(t.getSubjectId());
    			}
    			if(!NewGkElectiveConstant.SUBJECT_TYPE_O.contains(t.getSubjectType())){
    				courseName=courseName+t.getSubjectType();
    			}
        		t.setSubjectName(courseName);
        	}
    	} else {
        	subjectTimeList = initSubjectTimeList(divideId, groupType, itemId);
    	}
    	
    	map.put("divideId", divideId);
	 	map.put("gradeId", gradeId);
	 	map.put("itemId", itemId);
		map.put("subjectTimeList", subjectTimeList);
		map.put("arrayId", arrayId);
		map.put("groupType", groupType);
    	return "/newgkelective/array/subjectTimeAdd.ftl";
	}
    
    @ResponseBody
    @RequestMapping("/subjectTimeArrange/save")
    @ControllerInfo(value = "保存课时安排")
    public String doSave(@PathVariable("divideId") String divideId, NewGkSubjectTimeDto subjectTimeDto) {
    	String itemId = subjectTimeDto.getItemId();
    	try {
    		//新增
    		if(StringUtils.isBlank(subjectTimeDto.getItemId())){
    			itemId = newGkSubjectTimeService.saveList(divideId,subjectTimeDto.getSubjectTimeList());
    		//更新
    		}else{
    			newGkSubjectTimeService.updateList(subjectTimeDto.getSubjectTimeList());
    		}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
    	return returnSuccess(itemId, "操作成功！");
	}
	    
    
    
	@ResponseBody
    @RequestMapping("/divideClass/delete")
    @ControllerInfo(value = "删除方案结果")
    public String doDelete(@PathVariable String divideId) {
		 try{
			 if(checkAutoTwo(divideId) || isNowDivide(divideId)) {
				 return error("该方案正在分班中，不能删除！");
			 }
			 //是否已经被引用
			 if(checkUseNow(divideId)){
				 return error("该方案被引用，不能删除！");
			 }
			 //调整到一个事物中
			 newGkDivideService.deleteDivide(this.getLoginInfo().getUnitId(), divideId);
		 }catch (Exception e) {
	         e.printStackTrace();
	         return returnError("操作失败！", e.getMessage());
	     }
		 return success("操作成功！");
	}
	/**
	 * 是否已被引用
	 * @param divideId
	 * @return
	 */
	private boolean checkUseNow(String divideId){
		List<NewGkArray> list=newGkArrayService.findByDivideId(divideId);
		if(CollectionUtils.isNotEmpty(list)){
			return true;
		}
		return false;
	}
	
	
//	private void makeLocalStudentLevel(Map<String, NKStudent> stuMap, List<String> subjectIds,
//			Map<String, NKCourse> nkCourseMap, List<NKTeachClass> fineClassList, Set<String> stuIds, NewGkDivideEx divideEx) {
//		String classSumNum = divideEx.getClassSumNum();
//		String[] split = classSumNum.split(";");
//		Map<String, List<String[]>> levelMap = Arrays.stream(split).map(e->e.split(":")).collect(Collectors.groupingBy(e->e[0]));
//		
//		String[] levels = new String[] {NewGkElectiveConstant.SUBJECT_LEVEL_A,NewGkElectiveConstant.SUBJECT_LEVEL_B
//				,NewGkElectiveConstant.SUBJECT_LEVEL_C};
//		
//		List<NKStudent> liStuList = stuIds.stream().map(e->stuMap.get(e)).collect(Collectors.toList());
//		if(NewGkElectiveConstant.DIVIDE_BY_ALL_SCORE.equals(divideEx.getHierarchyScore())) {
//			// 按照总成绩 分班
//			int start = 0;
//			int end = start;
//			Collections.sort(liStuList, (e1,e2)->{
//				Float score2 = e2.getAvgScore();
//				Float score1 = e1.getAvgScore();
//				
//				return score2.compareTo(score1);
//			});
//			
//			Boolean isWen = false;
//			Boolean isLi = false;
//			if(divideEx.getSubjectType().equals(NewGkElectiveConstant.SUBJECT_TYPE_J)) {
//				if(NewGkElectiveConstant.GROUP_TYPE_2.equals(divideEx.getGroupType())) {
//					isLi = true;
//				}else if(NewGkElectiveConstant.GROUP_TYPE_3.equals(divideEx.getGroupType())) {
//					isWen = true;
//				}
//			}
//			NKTeachClass nkTeachClass;
//			Map<NKCourse, Integer> classIndex = new HashMap<>(); 
//			for (String level : levels) {
//				
//				// A:几个班:人数:
//				String[] strings = levelMap.get(level).get(0);
//				String exp = strings[2];
//				end += Integer.parseInt(exp);
//				if(NewGkElectiveConstant.SUBJECT_LEVEL_C.equals(level)) {
//					end = liStuList.size();
//				}
//				List<NKStudent> subList = liStuList.subList(start, end);
//				start = end;
//				if(CollectionUtils.isEmpty(subList)) {
//					// 这一层人数为0，则不再分班
//					continue;
//				}
//				
//				int st = 0;
//				int end2 = 0;
//				int classNum = Integer.parseInt(strings[1]);
//				if(classNum == 0) {
//					continue;
//				}
//				int classStuCount = subList.size()/classNum;
//				int remain = subList.size()%classNum;
//				for (int i=0;i<classNum;i++) {
//					end2 += classStuCount + (remain-- <= 0?0:1);
//					List<NKStudent> subList2 = subList.subList(st, end2);
//					
//					for (String subj : subjectIds) {
//						NKCourse nkCourse = nkCourseMap.get(subj+level);
//						Integer index = classIndex.get(nkCourse);
//						if(index == null) {
//							index = 1;
//							classIndex.put(nkCourse, index);
//						}
//						classIndex.put(nkCourse, index+1);
//						
//						nkTeachClass = new NKTeachClass(subList2, nkCourse,index,divideEx.getGroupType());
//						nkTeachClass.setSubjectType(divideEx.getSubjectType());
//						nkTeachClass.setIsLi(isLi);
//						nkTeachClass.setIsWen(isWen);
//						fineClassList.add(nkTeachClass);
//						
//						subList2.stream().forEach(e->e.setSingleSubjectLevel(subj,level));
//					}
//					st = end2;
//				}
//				
//			}
//			return;
//		}
// 		for (String openSubject : subjectIds) {
//			Collections.sort(liStuList, (e1,e2)->{
//				Float score2 = e2.getScoreInfo().get(openSubject);
//				Float score1 = e1.getScoreInfo().get(openSubject);
//				
//				if(score2 == null) {
//					score2 = (float)0.0;
//				}
//				if(score1 == null) {
//					score1 = (float)0.0;
//				}
//				return score2.compareTo(score1);
//			});
//			
//			int start = 0;
//			int end = start;
//			for (String level : levels) {
//				String exp = levelMap.get(level).get(0)[2];
//				end += Integer.parseInt(exp);
//				if(NewGkElectiveConstant.SUBJECT_LEVEL_C.equals(level)) {
//					end = liStuList.size();
//				}
//				List<NKStudent> subList = liStuList.subList(start, end);
//				subList.stream().forEach(e->e.setSingleSubjectLevel(openSubject,level));
//				start = end;
//			}
//		}
//	}

	public Map<String, NewGkItemDto> getNewGkItemDtoMap(Set<String> arrayItemIdSet, List<NewGkplaceArrange> newGkplaceArrangeList){
		Map<String, NewGkItemDto> newGkItemDtoMap = new HashMap<String, NewGkItemDto>();
		Set<String> placeIdSet = new HashSet<String>();
		for(NewGkplaceArrange item : newGkplaceArrangeList){
			placeIdSet.add(item.getPlaceId());
		}
		Map<String, String> teachPlaceTypeMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(placeIdSet)){
			List<TeachPlace> teachPlaceList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceList(placeIdSet.toArray(new String[0])), new TR<List<TeachPlace>>(){});
			if(CollectionUtils.isEmpty(teachPlaceList)){
				return newGkItemDtoMap;
			}
			for(TeachPlace item : teachPlaceList){
		    	String[] typeArr = item.getPlaceType().split(",");
		    	String type = "";
		    	if(typeArr.length>0){
		    		type = typeArr[0];
		    	}else{
		    		type = item.getPlaceType();
		    	}
		    	teachPlaceTypeMap.put(item.getId(), type);
		    }
		}
		List<McodeDetail> mcodeDetailList = SUtils.dt(mcodeRemoteService.findByMcodeIds(new String[]{"DM-CDLX"}), new TR<List<McodeDetail>>(){});
		Map<String, String> placeTypeNameMap = new HashMap<String, String>();
		for(McodeDetail item : mcodeDetailList){
			placeTypeNameMap.put(item.getThisId(), item.getMcodeContent());
		}
		Map<String, Set<String>> typeMap = new HashMap<String, Set<String>>();
		Map<String, List<String>> placeIdMap = new HashMap<String, List<String>>();
		for(String arrayItemId : arrayItemIdSet){
			Set<String> typeSet = new HashSet<String>();
			List<String> placeIdList = new ArrayList<String>();
			for(NewGkplaceArrange item : newGkplaceArrangeList){
				if(arrayItemId.equals(item.getArrayItemId())){
					typeSet.add(teachPlaceTypeMap.get(item.getPlaceId()));
					placeIdList.add(item.getPlaceId());
				}
			}
			typeMap.put(arrayItemId, typeSet);
			placeIdMap.put(arrayItemId, placeIdList);
		}
		for(String arrayItemId : arrayItemIdSet){
			NewGkItemDto newGkItemDto = new NewGkItemDto();
			Set<String> typeSet = typeMap.get(arrayItemId);
			List<String> placeIdList = placeIdMap.get(arrayItemId);
			String[] typeArr = new String[typeSet.size()];
			String[] numArr = new String[typeSet.size()];
			int p = 0;
			for(String type : typeSet){
				int i = 0;
				typeArr[p] = placeTypeNameMap.get(type);
				for(String placeId : placeIdList){
					if(type.equals(teachPlaceTypeMap.get(placeId))){
						i++;
					}
				}
				numArr[p] = String.valueOf(i);
				p++;
			}
			newGkItemDto.setTypeName(typeArr);
			newGkItemDto.setNum(numArr);
			newGkItemDtoMap.put(arrayItemId, newGkItemDto);
		}
		return newGkItemDtoMap;
	}
		
	@RequestMapping("/placeArrange/index/page")
	public String arrayItemHead(@PathVariable String divideId,String arrayId, ModelMap map){
		List<NewGkArrayItem> newGkArrayItemList = new ArrayList<NewGkArrayItem>();
		newGkArrayItemList = newGkArrayItemService.findByDivideId(divideId, new String[]{NewGkElectiveConstant.ARRANGE_TYPE_01});
		NewGkDivide newGkDivide = newGkDivideService.findById(divideId);
		Set<String> arrayItemIdSet = new HashSet<String>();
		for(NewGkArrayItem item : newGkArrayItemList){
			arrayItemIdSet.add(item.getId());
		}
		if(CollectionUtils.isNotEmpty(arrayItemIdSet)){
			List<NewGkplaceArrange> newGkplaceArrangeList = newGkplaceArrangeService.findByArrayItemIds(arrayItemIdSet.toArray(new String[0]));
			List<NewGkPlaceItem> newGkPlaceItemList = newGkPlaceItemService.findAll();
			Set<String> newGkPlaceItemIdSet = EntityUtils.getSet(newGkPlaceItemList, NewGkPlaceItem::getArrayItemId);
			for(NewGkArrayItem item1 : newGkArrayItemList){
		    	int i = 0;
		    	for(NewGkplaceArrange item2 : newGkplaceArrangeList){
		    		if(item1.getId().equals(item2.getArrayItemId())){
		    			item1.setCountPlace(i);
		    			i++;
		    		}
		    	}
		    	item1.setCountPlace(i);
		    	item1.setGalleryful(String.valueOf(newGkDivide.getGalleryful())+"+"+String.valueOf(newGkDivide.getMaxGalleryful()));
		        if(!newGkPlaceItemIdSet.contains(item1.getId())){
		        	item1.setTs(true);//还没保存具体场地安排，给出提示
		        }
			}
		    Map<String, NewGkItemDto> newGkItemDtoMap = getNewGkItemDtoMap(arrayItemIdSet, newGkplaceArrangeList);
		    for(NewGkArrayItem item : newGkArrayItemList){
		    	item.setNewGkItemDto(newGkItemDtoMap.get(item.getId()));
		    }
		}		
		Collections.sort(newGkArrayItemList,new Comparator<NewGkArrayItem>(){
			public int compare(NewGkArrayItem arg0, NewGkArrayItem arg1) {
				return arg0.getItemName().compareTo(arg1.getItemName());
			}
	    });
		map.put("newGkArrayItemList", newGkArrayItemList);
		map.put("divideId", divideId);
		map.put("gradeId", newGkDivide.getGradeId());
		map.put("countArrang", newGkArrayItemList.size());
		map.put("arrayId", arrayId);
		return "/newgkelective/placeArrange/placeArrange.ftl";
	}
	
	public List<NewGkBuildingDto> getNewGkBuildingDtoList(List<TeachPlace> teachPlaceAllList, List<TeachPlace> noBuildingIdPalceList, Set<String> buildingIdSet){
		List<TeachPlace> haveBuildingIdPalceList = new ArrayList<TeachPlace>();//有buildingId的场地
		teachPlaceAllList.removeAll(noBuildingIdPalceList);
		haveBuildingIdPalceList = teachPlaceAllList;
		Map<String,String> buildingMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(buildingIdSet)){
			buildingMap = SUtils.dt(teachBuildingRemoteService.findTeachBuildMap(buildingIdSet.toArray(new String[]{})),new TR<Map<String,String>>(){});
		}
		List<NewGkBuildingDto> newGkBuildingDtoList = new ArrayList<NewGkBuildingDto>();
		for(String buildingId : buildingIdSet){
			Set<Integer> floorSet = new HashSet<Integer>();
			NewGkBuildingDto newGkBuildingDto = new NewGkBuildingDto();
			for(TeachPlace place : haveBuildingIdPalceList){
				if(buildingId.equals(place.getTeachBuildingId())){
					floorSet.add(place.getFloorNumber()==null?0:place.getFloorNumber());
				}
			}
			List<Integer> floorNumList = new ArrayList<Integer>();
			for(int floorNum : floorSet){
				floorNumList.add(floorNum);
			}
			Collections.sort(floorNumList,new Comparator<Integer>(){
				public int compare(Integer arg0, Integer arg1) {
					return arg0.compareTo(arg1);
				}
		     });
			newGkBuildingDto.setBuildingId(buildingId);
			newGkBuildingDto.setBuildingName(buildingMap.get(buildingId));
			newGkBuildingDto.setFoorNumList(floorNumList);
			newGkBuildingDtoList.add(newGkBuildingDto);
		}
		Collections.sort(newGkBuildingDtoList,new Comparator<NewGkBuildingDto>(){
			public int compare(NewGkBuildingDto arg0, NewGkBuildingDto arg1) {
				return arg0.getBuildingName().compareTo(arg1.getBuildingName());
			}
	    });
		return newGkBuildingDtoList;
	}
	
	@RequestMapping("/placeArrange/eidt")
	public String ArrayItemEdit(@PathVariable String divideId, String arrayId,String arrayItemId,
			HttpServletRequest request, ModelMap map){
		List<TeachPlace> teachPlaceList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceListByType(getLoginInfo().getUnitId(), null), new TR<List<TeachPlace>>(){});
		Set<String> buildingIdSet = new HashSet<String>();
		List<TeachPlace> noBuildingIdPalceList = new ArrayList<TeachPlace>();//没有buildingId的场地
		for(TeachPlace place : teachPlaceList){
			if(StringUtils.isBlank(place.getTeachBuildingId())){
				noBuildingIdPalceList.add(place);
			}else{
				buildingIdSet.add(place.getTeachBuildingId());
			}
		}
		List<TeachPlace> haveBuildingIdPalceList = new ArrayList<TeachPlace>();//有buildingId的场地
		teachPlaceList.removeAll(noBuildingIdPalceList);
		haveBuildingIdPalceList = teachPlaceList;
		List<NewGkBuildingDto> newGkBuildingDtoList = getNewGkBuildingDtoList(teachPlaceList, noBuildingIdPalceList, buildingIdSet);
		//获取已选的
		String itemName = "";
		NewGkDivide divide = newGkDivideService.findById(divideId);
		if(StringUtils.isNotBlank(arrayItemId)){
			List<NewGkplaceArrange> newGkplaceArrangeList = newGkplaceArrangeService.findByArrayItemId(arrayItemId);
			Set<String> choisePlaceSet = new HashSet<String>();
			for(NewGkplaceArrange item : newGkplaceArrangeList){
				choisePlaceSet.add(item.getPlaceId());
			}
			List<TeachPlace> teachPlaceChoiseList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceList(choisePlaceSet.toArray(new String[0])), new TR<List<TeachPlace>>(){});
			Set<String> buildingIdSet2 = new HashSet<String>();
			List<TeachPlace> noBuildingIdPalceList2 = new ArrayList<TeachPlace>();//没有buildingId的场地
			for(TeachPlace place : teachPlaceChoiseList){
				if(StringUtils.isBlank(place.getTeachBuildingId())){
					noBuildingIdPalceList2.add(place);
				}else{
					buildingIdSet2.add(place.getTeachBuildingId());
				}
			}
			List<TeachPlace> haveBuildingIdPalceList2 = new ArrayList<TeachPlace>();//有buildingId的场地
			teachPlaceChoiseList.removeAll(noBuildingIdPalceList2);
			haveBuildingIdPalceList2 = teachPlaceChoiseList;
			List<NewGkBuildingDto> newGkBuildingDtoList2 = getNewGkBuildingDtoList(teachPlaceChoiseList, noBuildingIdPalceList2, buildingIdSet2);
			map.put("noBuildingIdPalceList2", noBuildingIdPalceList2);
			map.put("haveBuildingIdPalceList2", haveBuildingIdPalceList2);
			map.put("newGkBuildingDtoList2", newGkBuildingDtoList2);
			map.put("arrayItemId", arrayItemId);
			Map<String, String> checkMap = new HashMap<String, String>();
			for(TeachPlace teachPlace1 : haveBuildingIdPalceList){
				for(TeachPlace teachPlace2 : haveBuildingIdPalceList2){
					if(teachPlace1.getId().equals(teachPlace2.getId())){
						checkMap.put(teachPlace1.getId(), "1");

					}
				}
			}
			for(TeachPlace teachPlace1 : noBuildingIdPalceList){
				for(TeachPlace teachPlace2 : noBuildingIdPalceList2){
					if(teachPlace1.getId().equals(teachPlace2.getId())){
						checkMap.put(teachPlace1.getId(), "1");
					}
				}
			}
			map.put("checkMap", checkMap);
			itemName = newGkArrayItemService.findOne(arrayItemId).getItemName();

			Set<String> hasPlaceIds = new HashSet<String>();
			List<NewGkPlaceItem> pitems = newGkPlaceItemService.findByArrayItemId(arrayItemId);
			if(CollectionUtils.isNotEmpty(pitems)) {
				hasPlaceIds.addAll(EntityUtils.getSet(pitems, NewGkPlaceItem::getPlaceId));
			}
			map.put("hasPlaceIds", StringUtils.join(hasPlaceIds.toArray(new String[0]),","));
		}else{
			List<NewGkArrayItem> newGkArrayItemList = newGkArrayItemService.findByDivideId(divideId, new String[]{NewGkElectiveConstant.ARRANGE_TYPE_01});
			//获取学年、学期、年级等信息
			String semesterJson = semesterRemoteService.getCurrentSemester(2, divide.getUnitId());
			Semester semester = SUtils.dc(semesterJson, Semester.class);
			Grade grade = SUtils.dc(gradeRemoteService.findOneById(divide.getGradeId()), Grade.class);

			if(CollectionUtils.isEmpty(newGkArrayItemList)){
				//itemName = "教室方案1";
				itemName = semester.getAcadyear()+"学年"+grade.getGradeName()+"第"+semester.getSemester()+"学期教室方案1";
			}else{
				Integer maxTimes = newGkArrayItemList.stream().map(e->e.getTimes()).max(Integer::compare).orElse(0);
				//itemName = "教室方案"+String.valueOf(newGkArrayItemList.size()+1);
				itemName = semester.getAcadyear()+"学年"+grade.getGradeName()+"第"+semester.getSemester()+"学期教室方案"
						+String.valueOf(maxTimes+1);
			}
		}

		List<NewGkDivideClass> newGkDivideClassList = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divideId, new String[]{NewGkElectiveConstant.CLASS_TYPE_1, NewGkElectiveConstant.CLASS_TYPE_2},false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<NewGkDivideClass> xzGkDivideClassList =  newGkDivideClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType())).collect(Collectors.toList());
		newGkDivideClassList.removeAll(xzGkDivideClassList);
		int jxbSize = 0;
		if (CollectionUtils.isNotEmpty(newGkDivideClassList)) {
			List<NewGkDivideClass> jxbGkDivideClassList = newGkDivideClassList;
			Map<String, List<NewGkDivideClass>> jxbMap = jxbGkDivideClassList.stream()
					.collect(Collectors.groupingBy(e -> e.getSubjectType() + "," + e.getBatch()));
			Iterator<Entry<String, List<NewGkDivideClass>>> jit = jxbMap.entrySet().iterator();
			while(jit.hasNext()) {
				List<NewGkDivideClass> dcs = jit.next().getValue();
				if(CollectionUtils.isNotEmpty(dcs)) {
					jxbSize = Math.max(jxbSize, dcs.size());
				}
			}
		}
		int cc = xzGkDivideClassList.size();
		map.put("classCount", cc);
		map.put("exCount", (jxbSize>cc)?(jxbSize-cc):0);
		map.put("itemName", itemName);
		map.put("noBuildingIdPalceList", noBuildingIdPalceList);
		map.put("haveBuildingIdPalceList", haveBuildingIdPalceList);
		map.put("newGkBuildingDtoList", newGkBuildingDtoList);
		map.put("divideId", divideId);
		map.put("countPlace", noBuildingIdPalceList.size()+haveBuildingIdPalceList.size());
		map.put("arrayId", arrayId);
		map.put("gradeId", divide.getGradeId());
		map.put("lessArrayId", StringUtils.trimToEmpty(request.getParameter("lessArrayId")));
		map.put("plArrayId", StringUtils.trimToEmpty(request.getParameter("plArrayId")));
		return "/newgkelective/placeArrange/placeArrangeEdit2.ftl";
	}

    @RequestMapping("/placeArrange/new")
    public String ArrayItemNew(@PathVariable String divideId, String arrayId,String arrayItemId,
                                HttpServletRequest request, ModelMap map){
	    NewGkDivide divide = newGkDivideService.findById(divideId);
        if(divide==null){
            return errorFtl(map, "分班方案不存在");
        }
        List<NewGkplaceArrange> list = newGkplaceArrangeService.findByArrayItemId(divide.getGradeId());
		if(StringUtils.isBlank(arrayItemId)) {
			arrayItemId= UuidUtils.generateUuid();
		}
		for (NewGkplaceArrange one : list) {
			one.setId(UuidUtils.generateUuid());
			one.setArrayItemId(arrayItemId);
			one.setModifyTime(new Date());
		}
		newGkplaceArrangeService.savePlaceArrang(arrayItemId, true, divide, list);
		boolean isNext=false;
        if(NewGkElectiveConstant.DIVIDE_TYPE_01.equals(divide.getOpenType())
                || NewGkElectiveConstant.DIVIDE_TYPE_02.equals(divide.getOpenType())
                || NewGkElectiveConstant.DIVIDE_TYPE_05.equals(divide.getOpenType())
                || NewGkElectiveConstant.DIVIDE_TYPE_06.equals(divide.getOpenType())
                || NewGkElectiveConstant.DIVIDE_TYPE_08.equals(divide.getOpenType())
                || NewGkElectiveConstant.DIVIDE_TYPE_09.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType())
                ){
            isNext=true;
        }
        map.put("gradeId", divide.getGradeId());
        map.put("isNext", isNext);
        map.put("lessArrayId", StringUtils.trimToEmpty(request.getParameter("lessArrayId")));
        map.put("plArrayId", StringUtils.trimToEmpty(request.getParameter("plArrayId")));
        toPlaceArrangeList2(divideId, arrayId, arrayItemId,"1", map,divide.getOpenType(),false);
        return "/newgkelective/placeArrange/placeArrangeList3.ftl";
    }

    @ResponseBody
    @RequestMapping("/placeArrange/{arrayItemId}/placeSetSave")
    public String savePlaceSet(@PathVariable("arrayItemId") String arrayItemId, String placeIdsTemp, String placeNamesTemp) {
        try {
        	NewGkplaceArrange[] all = null;
        	String[] placeIdArr = null;
        	if(StringUtils.isNotBlank(placeIdsTemp)) {
        		placeIdArr = placeIdsTemp.split(",");
        		all = new NewGkplaceArrange[placeIdArr.length];
        		for(int i = 0; i < placeIdArr.length; i++){
        			NewGkplaceArrange newGkplaceArrange = new NewGkplaceArrange();
        			newGkplaceArrange.setId(UuidUtils.generateUuid());
        			newGkplaceArrange.setArrayItemId(arrayItemId);
        			newGkplaceArrange.setCreationTime(new Date());
        			newGkplaceArrange.setModifyTime(new Date());
        			newGkplaceArrange.setOrderId(i);
        			newGkplaceArrange.setPlaceId(placeIdArr[i]);
        			all[i]=newGkplaceArrange;
        		}
        	}
            
            newGkplaceArrangeService.savePlaceArrangeModify(arrayItemId,all,placeIdArr);
        } catch (RuntimeException re) {
            return error(re.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return error("保存失败！");
        }
        return success("保存成功！");
    }
	
	@RequestMapping("/placeArrange/list")
	public String arrayItemList(@PathVariable String divideId, String fromSolve, String arrayId, String arrayItemId, String useMaster, 
			HttpServletRequest request, ModelMap map){
		NewGkDivide divide = newGkDivideService.findById(divideId);
		map.put("fromSolve", fromSolve);
		if(divide==null){
			return errorFtl(map, "分班方案不存在");
		}
		boolean isNext=false;
		if(NewGkElectiveConstant.DIVIDE_TYPE_01.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_02.equals(divide.getOpenType()) 
				|| NewGkElectiveConstant.DIVIDE_TYPE_05.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_06.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_08.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_09.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType())
				){
			isNext=true;
		}
		map.put("gradeId", divide.getGradeId());
		map.put("isNext", isNext);
		map.put("lessArrayId", StringUtils.trimToEmpty(request.getParameter("lessArrayId")));
		map.put("plArrayId", StringUtils.trimToEmpty(request.getParameter("plArrayId")));
		toPlaceArrangeList2(divideId, arrayId, arrayItemId, useMaster, map,divide.getOpenType(),false);
		
		return "/newgkelective/placeArrange/placeArrangeList3.ftl";	
			
	}

	private void toPlaceArrangeList2(String divideId, String arrayId, String arrayItemId, String useMaster, ModelMap map, String divideType, boolean isFakeXzb) {
		NewGkArrayItem newGkArrayItem = newGkArrayItemService.findOneWithMaster(arrayItemId);
		//分班数据
		String[] classTypes = new String[]{NewGkElectiveConstant.CLASS_TYPE_1};
		if(isFakeXzb) {
			classTypes = new String[]{NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_4};
		}else if(NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divideType)
				|| NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divideType)) {
			classTypes = new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_3};
		}
		List<NewGkDivideClass> divideClassList = newGkDivideClassService.findByDivideIdAndClassType(this.getLoginInfo().getUnitId(), divideId, 
				classTypes,false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<NewGkDivideClass> xzbClassList = new ArrayList<>();
		Set<String> pure3Cids = new HashSet<>();
		if(isFakeXzb){
			xzbClassList = divideClassList.stream()
					.filter(e->NewGkElectiveConstant.CLASS_TYPE_4.equals(e.getClassType()))
					.collect(Collectors.toList());

			Map<String, NewGkDivideClass> classMap = EntityUtils.getMap(divideClassList, e -> e.getId());
			Set<String> pure3Cids2 = divideClassList.stream()
					.filter(e -> NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType())
							&& classMap.containsKey(e.getRelateId())
							&& NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(classMap.get(e.getRelateId()).getSubjectType()))
					.map(e -> e.getId())
					.collect(Collectors.toSet());
			pure3Cids.addAll(pure3Cids2);
		}else{
			xzbClassList = divideClassList.stream()
					.filter(e->NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType()))
					.collect(Collectors.toList());
		}
		List<NewGkDivideClass> zhbClassList = divideClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_3.equals(e.getClassType()))
				.collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(zhbClassList)) {
			Map<String, List<NewGkDivideClass>> listMap = EntityUtils.getListMap(zhbClassList, NewGkDivideClass::getParentId,e->e);
			for (String parentId : listMap.keySet()) {
				List<NewGkDivideClass> dcList = listMap.get(parentId);
				if(dcList.size()>1) {
					// 保存 行政班 场地安排时，将组合班的场地同步 保存
					xzbClassList.add(dcList.get(1));
				}
				
			}
		}
		
		//场地数据
		List<NewGkplaceArrange> newGkplaceArrangeList = null;
		List<NewGkPlaceItem> newGkPlaceItemList = null;
		String[] types = new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_3};
		if(isFakeXzb) {
			types = new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_4};
		}
		if(Objects.equals(useMaster, "1")) {
			newGkplaceArrangeList = newGkplaceArrangeService.findByArrayItemIdWithMaster(arrayItemId);
			newGkPlaceItemList = newGkPlaceItemService.findByArrayItemIdAndTypeInWithMaster(arrayItemId, new String[] {NewGkElectiveConstant.CLASS_TYPE_1});
		}else {
			newGkplaceArrangeList = newGkplaceArrangeService.findByArrayItemId(arrayItemId);
			newGkPlaceItemList = newGkPlaceItemService.findByArrayItemIdAndTypeIn(arrayItemId, types);
		}
		//newGkplaceArrangeList.sort((ord1,ord2) -> ord2.getOrderId().compareTo(ord1.getOrderId()));
		
		String unitId = getLoginInfo().getUnitId();
		List<TeachPlace> teachPlaceListAll = SUtils.dt(teachPlaceRemoteService.findTeachPlaceListByType(unitId, null), TeachPlace.class);
		List<TeachBuilding> buildListAll =  SUtils.dt(teachBuildingRemoteService.findTeachBuildListByUnitId(unitId),TeachBuilding.class);
		TeachBuilding noBuilding = new TeachBuilding();
		final String noBuildingId = "NO_BUILDING";
		noBuilding.setId(noBuildingId);
		noBuilding.setBuildingName("无楼层");
		teachPlaceListAll.stream().filter(e->StringUtils.isBlank(e.getTeachBuildingId())).forEach(e->e.setTeachBuildingId(noBuildingId));
		
		Map<String, List<TeachPlace>> placeByBuildingMap = EntityUtils.getListMap(teachPlaceListAll, TeachPlace::getTeachBuildingId, e->e);
		if(placeByBuildingMap.containsKey(noBuildingId)) {
			buildListAll.add(noBuilding);
		}
		Map<String, String> buildNameMap = EntityUtils.getMap(buildListAll, TeachBuilding::getId,TeachBuilding::getBuildingName);
		teachPlaceListAll.stream().filter(e->buildNameMap.containsKey(e.getTeachBuildingId()))
				.forEach(e->e.setTeachBuildingName(buildNameMap.get(e.getTeachBuildingId())));
		map.put("buildList", buildListAll);
		map.put("placeByBuildingMap", placeByBuildingMap);
		//TODO 
		
//		Set<String> choisePlaceSet = new HashSet<String>();
		Set<String> placeIds = EntityUtils.getSet(newGkplaceArrangeList, NewGkplaceArrange::getPlaceId);
//		List<TeachPlace> teachPlaceList = teachPlaceRemoteService.findListObjectByIds(placeIds.toArray(new String[0]));
		List<TeachPlace> teachPlaceList = teachPlaceListAll.stream().filter(e->placeIds.contains(e.getId())).collect(Collectors.toList());
		Map<String, TeachPlace> teachPlaceMap =new LinkedHashMap<>();
		for(TeachPlace t:teachPlaceList) {
			teachPlaceMap.put(t.getId(), t);
		}
//		Map<String, TeachPlace> teachPlaceMap = EntityUtils.getMap(teachPlaceList,e->e.getId());
		Set<String> buildIds=EntityUtils.getSet(teachPlaceList,e->e.getTeachBuildingId());
		Map<String, TeachBuilding> teachBuildMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(buildIds)) {
			List<TeachBuilding> buildList = buildListAll.stream().filter(e->buildIds.contains(e.getId())).collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(buildList)) {
				teachBuildMap=EntityUtils.getMap(buildList,e->e.getId());
			}
		}
		StringBuffer placeIdsTemp = new StringBuffer();
		StringBuffer placeNamesTemp = new StringBuffer();
		for(TeachPlace t:teachPlaceList) {
            placeIdsTemp.append(t.getId() + ",");
            placeNamesTemp.append(t.getPlaceName() + ",");
			if(StringUtils.isNotBlank(t.getTeachBuildingId()) && teachBuildMap.containsKey(t.getTeachBuildingId())) {
				t.setTeachBuildingName(teachBuildMap.get(t.getTeachBuildingId()).getBuildingName());
			}
		}

		Map<String,TeachPlace> classPlaceMap = new HashMap<String, TeachPlace>();

		Map<String,NewGkPlaceItem> classPlaceItem=new HashMap<String,NewGkPlaceItem>();
		List<NewGkPlaceItem> usedPlaceItems = newGkPlaceItemList;
		Set<String> purePlaceIds = new HashSet<>();
		if(isFakeXzb){
			usedPlaceItems = newGkPlaceItemList.stream()
					.filter(e -> NewGkElectiveConstant.CLASS_TYPE_4.equals(e.getType())).collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(pure3Cids)){
				purePlaceIds = newGkPlaceItemList.stream()
						.filter(e -> NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getType())
								&& pure3Cids.contains(e.getObjectId()))
						.map(e->e.getPlaceId()).collect(Collectors.toSet());
			}
		}
		//不再过滤
		if(CollectionUtils.isNotEmpty(usedPlaceItems)){
			Set<String> used = new HashSet<>();
			for(NewGkPlaceItem item:usedPlaceItems){
				//班级
				classPlaceItem.put(item.getObjectId(), item);
				classPlaceMap.put(item.getObjectId(), teachPlaceMap.get(item.getPlaceId()));
				used.add(item.getPlaceId());
			}
			used.forEach(e->teachPlaceMap.remove(e));
		}
		List<TeachPlace> remainTeachPlaceList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(newGkplaceArrangeList)){
			for(NewGkplaceArrange ent :newGkplaceArrangeList){
				if(isFakeXzb && purePlaceIds.contains(ent.getPlaceId())){
					//排除 三科组合班
					continue;
				}
				TeachPlace ent1  = teachPlaceMap.get(ent.getPlaceId());
				if(ent1!=null){
					remainTeachPlaceList.add(ent1);
				}
			}
		}
		if(classPlaceItem.size()>0){
			for(NewGkDivideClass item : xzbClassList){
				if(classPlaceItem.containsKey(item.getId())){
					item.setPlaceIds(classPlaceItem.get(item.getId()).getPlaceId());
				}
			}
		}
		
		map.put("placeIdsTemp", placeIdsTemp.length() != 0 ? placeIdsTemp.substring(0, placeIdsTemp.length() - 1) : "");
		map.put("placeNamesTemp", placeNamesTemp.length() != 0 ? placeNamesTemp.substring(0, placeNamesTemp.length() - 1) : "");
		map.put("divideClassList", xzbClassList);
		map.put("classPlaceMap", classPlaceMap);
		map.put("teachPlaceList", remainTeachPlaceList);
		map.put("arrayItemId", arrayItemId);
		map.put("divideId", divideId);
		map.put("newGkArrayItem", newGkArrayItem);
		map.put("arrayId", arrayId);
	}
	
	/**
	 * 排课特征-保存教师方案-行政班
	 * @param divideId
	 * @param newGkPlaceItemDto
	 * @param arrayItemId
	 * @param arrayId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/placeArrange/savePlaceArrangeItem")
	public String savePlaceArrangeItem(@PathVariable String divideId, NewGkPlaceItemDto newGkPlaceItemDto, String arrayItemId, String arrayId){
		try{
			List<NewGkPlaceItem> xzbPlaceItemList = newGkPlaceItemDto.getNewGkPlaceItemList1();
			List<NewGkPlaceItem> newGkPlaceItemList2 = newGkPlaceItemDto.getNewGkPlaceItemList2();
			if(CollectionUtils.isEmpty(xzbPlaceItemList)) {
				return error("没有班级数据");
			}
			
			String[] otherClassTypes = new String[] {NewGkElectiveConstant.CLASS_TYPE_2,NewGkElectiveConstant.CLASS_TYPE_4};
			if(NewGkElectiveConstant.CLASS_TYPE_4.equals(xzbPlaceItemList.get(0).getType())) {
				otherClassTypes = new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_3};
			}
			
			List<NewGkPlaceItem> otherPlaceItemList = newGkPlaceItemService.findByArrayItemIdAndTypeIn(arrayItemId,
					otherClassTypes);
			if(CollectionUtils.isNotEmpty(otherPlaceItemList)){
				//return success("已经安排教学班的场地，不能保存，直接进入下一步");
			}
			if(CollectionUtils.isEmpty(xzbPlaceItemList)){
				xzbPlaceItemList = new ArrayList<NewGkPlaceItem>();
			}
			if(CollectionUtils.isEmpty(newGkPlaceItemList2)){
				newGkPlaceItemList2 = new ArrayList<NewGkPlaceItem>();
			}
			Set<String> placeIdSet = new HashSet<String>();
			for(NewGkPlaceItem item : xzbPlaceItemList){
				
				item.setId(UuidUtils.generateUuid());
				item.setArrayItemId(arrayItemId);
				placeIdSet.add(item.getPlaceId());
			}
			if(placeIdSet.size()<xzbPlaceItemList.size()){
				return error("不同行政班不能指定同一个教室，请核对！");
			}
			
			NewGkDivide divdie = newGkDivideService.findOne(divideId);
			List<NewGkPlaceItem> zhbPlaceItemList = new ArrayList<>();
			if(NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divdie.getOpenType())
					|| NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divdie.getOpenType())) {
				List<NewGkDivideClass> divideClassList = newGkDivideClassService.findByDivideIdAndClassType(this.getLoginInfo().getUnitId(), divideId, 
						new String[] {NewGkElectiveConstant.CLASS_TYPE_3},false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
				Set<String> classIds = EntityUtils.getSet(divideClassList, NewGkDivideClass::getId);
				Set<String> selZhbIds = xzbPlaceItemList.stream().map(e->e.getObjectId()).filter(e->classIds.contains(e)).collect(Collectors.toSet());
				Map<String, List<NewGkDivideClass>> parentIdMap = EntityUtils.getListMap(divideClassList, NewGkDivideClass::getParentId, e->e);
				for (NewGkPlaceItem item : xzbPlaceItemList) {
					String classId = item.getObjectId();
					if(!selZhbIds.contains(classId)) {
						// 跟随行政班上课的   物理 历史 合成班级
						List<NewGkDivideClass> list = parentIdMap.get(classId);
						if(CollectionUtils.isEmpty(list)) {
							continue;
						}
						List<NewGkDivideClass> aList = list.stream()
								.filter(e->!selZhbIds.contains(e.getId()))
								.collect(Collectors.toList());
						aList.stream().filter(e->!selZhbIds.contains(e.getId())).forEach(e->{
							NewGkPlaceItem newItem = EntityUtils.copyProperties(item, NewGkPlaceItem.class);
							newItem.setId(UuidUtils.generateUuid());
							newItem.setObjectId(e.getId());
							newItem.setType(e.getClassType());
							zhbPlaceItemList.add(newItem);
						});
					}
				}
			}
			
			List<NewGkPlaceItem> newGkPlaceItemList = new ArrayList<NewGkPlaceItem>();
			if(CollectionUtils.isNotEmpty(otherPlaceItemList)) {
				//防止页面场地调整
				newGkPlaceItemList=otherPlaceItemList;
			}else {
				for(NewGkPlaceItem item : newGkPlaceItemList2){
					//NewGkPlaceItem newGkPlaceItem = new NewGkPlaceItem();
					item.setId(UuidUtils.generateUuid());
					item.setArrayItemId(arrayItemId);
					//if(StringUtils.isNotBlank(item.getPlaceIds())){
						//newGkPlaceItem = item;
					newGkPlaceItemList.add(item);
					//}
					
					//将教学班部分关联的数据同时也安排
				}
			}
			
			xzbPlaceItemList.addAll(zhbPlaceItemList);
			newGkPlaceItemService.save(xzbPlaceItemList, newGkPlaceItemList, arrayId, arrayItemId);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/placeArrange/placeArrangeSave")
	public String savePlaceArrang(@PathVariable String divideId, String arrayItemId, String placeIds, ModelMap map){
		try{
			 NewGkDivide divide = newGkDivideService.findById(divideId);
	        if(divide==null){
	            return errorFtl(map, "分班方案不存在");
	        }
			
			String[] placeIdArr = placeIds.split(",");
			List<TeachPlace> teachPlaceList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceList(placeIdArr), new TR<List<TeachPlace>>(){});
			Map<String, Integer> classNumberMap = new HashMap<String, Integer>();
			for(TeachPlace place : teachPlaceList){
				classNumberMap.put(place.getId(), place.getClassNumber());
			}
			List<NewGkplaceArrange> newGkplaceArrangeList = new ArrayList<NewGkplaceArrange>();
			if(StringUtils.isBlank(arrayItemId)){
				arrayItemId = UuidUtils.generateUuid();
			}
			for(String placeId : placeIdArr){
				NewGkplaceArrange newGkplaceArrange = new NewGkplaceArrange();
				newGkplaceArrange.setId(UuidUtils.generateUuid());
				newGkplaceArrange.setPlaceId(placeId);
				newGkplaceArrange.setCreationTime(new Date());
				newGkplaceArrange.setModifyTime(new Date());
				newGkplaceArrange.setArrayItemId(arrayItemId);
				newGkplaceArrangeList.add(newGkplaceArrange);
			}
			newGkplaceArrangeService.savePlaceArrang(arrayItemId, false, divide, newGkplaceArrangeList);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return jobSuccess(arrayItemId,"操作成功！");
	}
	
	@ResponseBody
	@RequestMapping("/placeArrange/placeArrangeDeleteOne")
	public String placeArrangeDeleteOne(@PathVariable String divideId, String arrayItemId, String placeId){
		try{
			newGkplaceArrangeService.deleteByItemIdAndPlaceId(arrayItemId, placeId);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();		
	}
	
	@ResponseBody
	@RequestMapping("/placeArrange/placeArrangeDelete")
	public String placeArrangeDelete(@PathVariable String divideId, String arrayItemId){
		try{
			List<NewGkArray> newGkArrayList =  newGkArrayService.findListBy("placeArrangeId", arrayItemId);
			 boolean hasRefer = newGkArrayList.stream().anyMatch(e->Integer.valueOf(NewGkElectiveConstant.IF_INT_0).equals(e.getIsDeleted()));
			 if(hasRefer){
				 return error("该教室方案被引用，不能删除！");
			 }
			newGkArrayItemService.deleteById(this.getLoginInfo().getUnitId(),arrayItemId);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();		
	}
	
	 
	 @RequestMapping("/divideClass/resultClassEdit")
	 public String resultClassEdit(@PathVariable String divideId,HttpServletRequest request,ModelMap map){
		 String classType=request.getParameter("classType");
		 NewGkDivide divide=newGkDivideService.findById(divideId);
		 if(divide==null){
			 return errorFtl(map, "分班方案不存在");
		 }
		 map.put("classType", classType);
		 int galleryful=divide.getGalleryful();
		 List<NewGkDivideClass> divideClassList=newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(),divideId, new String[]{classType},false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		 /*List<String> divieClassIds=EntityUtils.getList(divideClassList, "id"); 
		 Set<String> chosenStuIds=newGkClassStudentService.findSetByClassIds(divieClassIds.toArray(new String[0]));*/
		 if(NewGkElectiveConstant.CLASS_TYPE_1.equals(classType)){//行政班
			 int allStudentNum=Integer.parseInt(request.getParameter("allStudentNum"));
			 int chosenClassNum=divideClassList.size();
			 int nowStudentNum=allStudentNum-chosenClassNum*galleryful;		 
			 if(nowStudentNum<0) nowStudentNum=0;
			 
			 map.put("maxClassNum", Math.ceil((double)(nowStudentNum)/galleryful));
			 return "newgkelective/divide/divideResultClassEdit.ftl";
		 }else if(NewGkElectiveConstant.CLASS_TYPE_0.equals(classType)){//3科组合
			NewGkChoice newChoice=newGkChoiceService.findOne(divide.getChoiceId());//选课
			if(newChoice==null) return errorFtl(map, "找不到选课");
			Map<String,List<NewGkDivideClass>> classListMap=new HashMap<String,List<NewGkDivideClass>>();
			for(NewGkDivideClass divideClass:divideClassList){
				List<NewGkDivideClass> inList=classListMap.get(divideClass.getSubjectIds());
				if(CollectionUtils.isEmpty(inList)){
					inList=new ArrayList<NewGkDivideClass>();
				}
				inList.add(divideClass);
				classListMap.put(divideClass.getSubjectIds(), inList);
			}
			
			List<NewGkConditionDto>  newDtoList=getNewDtoList(divide.getChoiceId());
			if(CollectionUtils.isNotEmpty(newDtoList)){
				for(NewGkConditionDto newDto:newDtoList){
					List<NewGkDivideClass> inList=classListMap.get(newDto.getSubjectIdstr());
					int chosenClassNum=CollectionUtils.isNotEmpty(inList)?inList.size():0;
					int nowStudentNum=newDto.getSumNum()-chosenClassNum*galleryful;
					if(nowStudentNum<0) nowStudentNum=0;
					
					newDto.setClassNum((int)Math.ceil((double)(nowStudentNum)/galleryful));
				}
			}
			map.put("newDtoList", newDtoList);
		    return "newgkelective/divide/divideResultClassEdit.ftl";
		 }
		 return errorFtl(map, "班级类型有误");
	 }
	 @RequestMapping("/divideClass/resultClassDeatil")
	 public String resultClassDeatil(@PathVariable String divideId,String divideClassId,String groupType,ModelMap map){
		 NewGkDivideClass divideClass=newGkDivideClassService.findOne(divideClassId);
		 List<NewGkDivideClass> classList=newGkDivideClassService.findByDivideIdAndClassType(this.getLoginInfo().getUnitId(), divideId, new String[]{divideClass.getClassType()},false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		 
		 List<NewGkConditionDto> dtoList=newGkClassStudentService.findClassDetail(divideClassId,divideId);
		 NewGkDivide divide=newGkDivideService.findOne(divideId);
		 if(divide==null) return errorFtl(map,"分班方案不存在");
		 
		 map.put("state", divide.getStat());
		 map.put("dtoList", dtoList);
		 map.put("classList", classList);
		 map.put("divideClass", divideClass);
		 map.put("groupType", groupType);
		 return "newgkelective/divide/divideResultClassDetail.ftl";
	 }
	 @ResponseBody
	 @RequestMapping("/divideClass/saveClass")
	 public String saveClass(@PathVariable String divideId,NewGkDivideClass newGkDivideClass,ModelMap map){
		String divideClassId="";
		 try {
			divideClassId=newGkDivideClassService.saveByHand(this.getLoginInfo().getUnitId(), newGkDivideClass);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess("00", divideClassId);
	 }
	 
	 @ResponseBody
	 @RequestMapping("/divideClass/saveClassStudent")
	 public String saveClassStudent(@PathVariable String divideId,String divideClassId,String studentIdstr,ModelMap map){
		
		 if(StringUtils.isBlank(studentIdstr)){
			 newGkClassStudentService.deleteByClassIdIn(this.getLoginInfo().getUnitId(), divideId, new String[]{divideClassId});
			 return returnSuccess();
		 }
		 String[] studentIds=studentIdstr.split(",");
		 NewGkDivide divide=newGkDivideService.findOne(divideId);
		 if(divide==null) {
			 return errorFtl(map, "分班不存在");
		 }
		 int num=divide.getGalleryful()+divide.getMaxGalleryful();
		 if(studentIds.length>num){
			 return returnError("-1","学生人数不能多于班级最大人数:"+num);
		 }
		 NewGkClassStudent classStudent;
		 List<NewGkClassStudent> classStudentList=new ArrayList<NewGkClassStudent>();
		 for(String studentId:studentIds){
			 classStudent=initClassStudent(divide.getUnitId(), divide.getId(), divideClassId, studentId);
			 classStudentList.add(classStudent);
		 }
		try {
			String[] divideClassIds=null;
			if(StringUtils.isNotBlank(divideClassId)) {
				divideClassIds=new String[] {divideClassId};
			}
			 newGkClassStudentService.saveOrSaveList(divide.getId(),divideId, divideClassIds, classStudentList);
			 
		 } catch (Exception e) {
			 e.printStackTrace();
			 return returnError();
		 }
		 return returnSuccess();
	 }
	 @ResponseBody
	 @RequestMapping("/divideClass/deleteDivideClass")
	 public String deleteDivideClass(@PathVariable String divideId,String divideClassId,ModelMap map){
		 try {
			 newGkDivideClassService.deleteById(this.getLoginInfo().getUnitId(), divideId, divideClassId);
		 } catch (Exception e) {
			 e.printStackTrace();
			 return returnError();
		 }
		 return returnSuccess();
	 }
	 
	 @RequestMapping("/divideClass/toStudentIndex")
	 public String toStudentIndex(@PathVariable String divideId,String classType,String divideClassId,String isback,ModelMap map){
		 NewGkDivide divide=newGkDivideService.findById(divideId);
		 if(divide==null){
			 return errorFtl(map, "分班方案不存在");
		 }
		 map.put("classType", classType);
		 //查分班結果
		 List<NewGkDivideClass> allDivideClassList=newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divideId, new String[]{classType},false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		 if(CollectionUtils.isEmpty(allDivideClassList)) 
			 return "newgkelective/divide/divideResultStuIndex.ftl";
		 if(NewGkElectiveConstant.CLASS_TYPE_1.equals(classType)){//行政班
			 List<Clazz> classList=SUtils.dt(classRemoteService.findBySchoolIdGradeId(divide.getUnitId(),divide.getGradeId()),new TR<List<Clazz>>(){});
			 
			 map.put("classId", CollectionUtils.isNotEmpty(classList)?classList.get(0).getId():"");
			 map.put("classList", classList);
			 if(StringUtils.isBlank(divideClassId)){
				 map.put("divideClassId",allDivideClassList.get(0).getId());
			 }else{
				 map.put("divideClassId", divideClassId);
			 }
			 map.put("divideClassList", allDivideClassList);
		 }else if(NewGkElectiveConstant.CLASS_TYPE_0.equals(classType)){//3科组合
			 List<NewGkConditionDto> newDtoList=new ArrayList<NewGkConditionDto>();//getNewDtoList(divide.getChoiceId());
			 Map<String,String> testMap=new HashMap<String,String>();
			 //获取有分班班级的 3科组合
			 NewGkConditionDto dto=null;
			 for(NewGkDivideClass allDivideClass:allDivideClassList){
				 String inSubjectIdstr=allDivideClass.getSubjectIds();
				 if(!testMap.containsKey(inSubjectIdstr)){
					 dto=new NewGkConditionDto();
					 dto.setSubShortNames(allDivideClass.getClassName().substring(0, 3));
					 dto.setSubjectIdstr(inSubjectIdstr);
					 newDtoList.add(dto);
				 }
				 testMap.put(inSubjectIdstr, "one");
			 }
			 
			 String subjectIdstr="";
			 if(StringUtils.isNotBlank(divideClassId)){
				 NewGkDivideClass divideClass=newGkDivideClassService.findOne(divideClassId);
				 subjectIdstr=divideClass.getSubjectIds();
			 }else{
				 if(CollectionUtils.isNotEmpty(newDtoList)){
					 subjectIdstr=newDtoList.get(0).getSubjectIdstr();
				 }
			 }
			 List<NewGkDivideClass> divideClassList=new ArrayList<NewGkDivideClass>();
			 for(NewGkDivideClass allDivideClass:allDivideClassList){
				 if(subjectIdstr.equals(allDivideClass.getSubjectIds())){
					 divideClassList.add(allDivideClass);
				 }
			 }
			 map.put("subjectIdstr", subjectIdstr);
			 map.put("newDtoList",newDtoList);
			 if(StringUtils.isBlank(divideClassId)){
				 map.put("divideClassId", CollectionUtils.isNotEmpty(divideClassList)?divideClassList.get(0).getId():"");
			 }else{
//				 map.put("isbackResultList", "true");
				 map.put("divideClassId", divideClassId);
			 }
			 map.put("divideClassList", divideClassList);
		 }
		 if(StringUtils.isNotBlank(isback)&&isback.equals("true")){
			 map.put("isbackResultList", "true");
		 }
		 return "newgkelective/divide/divideResultStuIndex.ftl";
	 }
	 @RequestMapping("/divideClass/loadRight")
	 public String loadRight(@PathVariable String divideId,HttpServletRequest request,ModelMap map){
		 String divideClassId=request.getParameter("divideClassId");
		 String classType=request.getParameter("classType");
		 String unitId = getLoginInfo().getUnitId();
		 NewGkDivide divide=newGkDivideService.findById(divideId);
		 if(divide==null) return errorFtl(map, "分班设置不存在");
		 Set<String> studentIds=newGkClassStudentService.findSetByClassIds(divide.getUnitId(),divideId, new String[]{divideClassId});
		 List<Student> studentList=SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])),new TR<List<Student>>(){});
		 
		 List<NewGkScoreResult> scoreList=newGkScoreResultService.findListByReferScoreId(unitId, divide.getReferScoreId());
		//key-studentId      里面的key courserId对应分数
		 Map<String,Map<String,Float>> stuScoreMap=new HashMap<String,Map<String,Float>>();
		 List<Course> courseList=null;
		 List<String> ysySubIds=null;
		 if(NewGkElectiveConstant.CLASS_TYPE_1.equals(classType)){//行政班
			 setStuScoreMap(stuScoreMap, scoreList, null);
			 courseList=SUtils.dt(courseRemoteService.findByCodesYSY(getLoginInfo().getUnitId()),new TR<List<Course>>(){});
		 }else{
			 NewGkDivideClass divideClass=newGkDivideClassService.findOne(divideClassId);
			 String[] subjectIds=divideClass!=null?divideClass.getSubjectIds().split(","):new String[]{};
			 ysySubIds=EntityUtils.getList(SUtils.dt(courseRemoteService.findByCodesYSY(unitId),new TR<List<Course>>(){}),Course::getId);
			 setStuScoreMap(stuScoreMap, scoreList,ysySubIds);
			 courseList=SUtils.dt(courseRemoteService.findListByIds(subjectIds),new TR<List<Course>>(){});
			 Course course=new Course();
			 course.setId(NewGkElectiveConstant.YSY_SUBID);
			 course.setSubjectName(NewGkElectiveConstant.YSY_SUBNAME);
			 courseList.add(course);
		 }
		 List<NewGkConditionDto> lastList=new ArrayList<NewGkConditionDto>();
		 int manNum=0;
		 if(CollectionUtils.isNotEmpty(studentList)){
			 for(Student stu:studentList){
				 NewGkConditionDto dto=new NewGkConditionDto(); 
				 dto.setStudentId(stu.getId());
				 dto.setStudentName(stu.getStudentName());
				 if(stu.getSex()==1){//男
					 manNum++;
				 }
				 dto.setSex(stu.getSex());
				 Map<String,Float> scoreMap=stuScoreMap.get(stu.getId());
				 dto.setScoreMap(scoreMap==null?new HashMap<String,Float>():scoreMap);
				 lastList.add(dto);
			 }
		 }
		 //平均分
		 Map<String,Float> avgMap=new HashMap<String,Float>();
		 Map<String,Float> allMap=new HashMap<String,Float>();
		 int maxCount=getAvgAllMap(avgMap, allMap, lastList, scoreList,ysySubIds);
		 
		 map.put("courseList", courseList);
		 map.put("lastList", lastList);
		 
		 map.put("maxCount", maxCount);
		 map.put("manCount", manNum);
		 map.put("womanCount", maxCount-manNum);
		 map.put("avgMap", avgMap);
		 map.put("allMap", allMap);
		 map.put("rightOrLeft", "right");
		 return "newgkelective/divide/divideResultStuLeftList.ftl";
	 }
	 @RequestMapping("/divideClass/loadLeft")
	 public String loadLeft(@PathVariable String divideId,HttpServletRequest request,ModelMap map){
		 String classType=request.getParameter("classType");
		 NewGkDivide divide=newGkDivideService.findById(divideId);
		 if(divide==null) return errorFtl(map, "分班设置不存在");
		 List<NewGkDivideClass> allDivideClassList=newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divideId, new String[]{classType},false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		 String[] divideClassIds=EntityUtils.getList(allDivideClassList,NewGkDivideClass::getId).toArray(new String[0]);
 		 Set<String> studentIds=newGkClassStudentService.findSetByClassIds(divide.getUnitId(),divideId, divideClassIds);
		 //选课学生
		 //Set<String> chosenStudentIds=newGkChoResultService.findSetByChoiceId(divide.getChoiceId());
		 
		 List<NewGkScoreResult> scoreList=newGkScoreResultService.findListByReferScoreId(divide.getUnitId(), divide.getReferScoreId());
		 //key-studentId      里面的key courserId对应分数
		 Map<String,Map<String,Float>> stuScoreMap=new HashMap<String,Map<String,Float>>();
		 List<NewGkConditionDto> lastList=new ArrayList<NewGkConditionDto>();
		 List<Student> studentList=null;
		 List<Course> courseList=null;
		 List<String> ysySubIds=null;
		 if(NewGkElectiveConstant.CLASS_TYPE_1.equals(classType)){//行政班
			 setStuScoreMap(stuScoreMap, scoreList, null);
			 Map<String, List<NewGkChoRelation>> NewGkChoRelationAllMap= newGkChoRelationService.findByChoiceIdAndObjectTypeIn(divide.getUnitId(),divide.getChoiceId(),new String[]{NewGkElectiveConstant.CHOICE_TYPE_04});
			 List<NewGkChoRelation>  relationList=NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_04);
			 List<String> values=EntityUtils.getList(relationList, NewGkChoRelation::getObjectValue);
			 courseList=SUtils.dt(courseRemoteService.findByCodesYSY(divide.getUnitId()),new TR<List<Course>>(){});
			 String classId=request.getParameter("classId");
			 List<Student> studentList1=SUtils.dt(studentRemoteService.findByClassIds(classId),new TR<List<Student>>(){});
			 studentList=new ArrayList<Student>();
			 for(Student stu:studentList1){
				 if(!values.contains(stu.getId())){
					 studentList.add(stu);
				 }
			 }
		 }else{
			 String subjectIdstr=request.getParameter("subjectIdstr");
			 String[] subjectIds=subjectIdstr.split(",");
			 Set<String> subjectIdSet=new HashSet<String>();
			 CollectionUtils.addAll(subjectIdSet, subjectIds);
			 ysySubIds=EntityUtils.getList(SUtils.dt(courseRemoteService.findByCodesYSY(getLoginInfo().getUnitId()),new TR<List<Course>>(){}),Course::getId);
			 setStuScoreMap(stuScoreMap, scoreList, ysySubIds);
			 int num=subjectIds.length;
			 courseList=SUtils.dt(courseRemoteService.findListByIds(subjectIds),new TR<List<Course>>(){});
			 Course course=new Course();
			 course.setId(NewGkElectiveConstant.YSY_SUBID);
			 course.setSubjectName(NewGkElectiveConstant.YSY_SUBNAME);
			 courseList.add(course);
			 
			 Map<String, Set<String>> stuIdOfSubIdsMap=newGkChoResultService.findMapByChoiceIdAndKindType(divide.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,divide.getChoiceId());
			 Set<String> leftStudentIds=new HashSet<String>();
			 for(Entry<String, Set<String>> entry:stuIdOfSubIdsMap.entrySet()){
				 if(CollectionUtils.intersection(entry.getValue(),subjectIdSet).size()==num && !studentIds.contains(entry.getKey())){
					 leftStudentIds.add(entry.getKey());
				 }
			 }
			 studentList=SUtils.dt(studentRemoteService.findListByIds(leftStudentIds.toArray(new String[0])),new TR<List<Student>>(){});
		 }
		 int manNum=0;
		 if(CollectionUtils.isNotEmpty(studentList)){
			 for(Student stu:studentList){
				 if(!studentIds.contains(stu.getId()) ){//&& chosenStudentIds.contains(stu.getId())
					 NewGkConditionDto dto=new NewGkConditionDto(); 
					 dto.setStudentId(stu.getId());
					 dto.setStudentName(stu.getStudentName());
					 if(stu.getSex()==1){//男
						 manNum++;
					 }
					 dto.setSex(stu.getSex());
					 Map<String,Float> scoreMap=stuScoreMap.get(stu.getId());
					 dto.setScoreMap(scoreMap==null?new HashMap<String,Float>():scoreMap);
					 lastList.add(dto);
				 }
			 }
		 }
		 //平均分
		 Map<String,Float> avgMap=new HashMap<String,Float>();
		 Map<String,Float> allMap=new HashMap<String,Float>();
		 int maxCount=getAvgAllMap(avgMap, allMap, lastList, scoreList,ysySubIds);
		 
		 map.put("courseList", courseList);
		 map.put("lastList", lastList);
		 
		 map.put("maxCount", maxCount);
		 map.put("manCount", manNum);
		 map.put("womanCount", maxCount-manNum);
		 map.put("avgMap", avgMap);
		 map.put("allMap", allMap);
		 map.put("rightOrLeft", "left");
		 return "newgkelective/divide/divideResultStuLeftList.ftl";
	 }
	 /**
	  * 获取//key-studentId      里面的key courserId对应分数  .有语数英的情况
	  * @param stuScoreMap
	  * @param scoreList
	  * @param ysySubIds
	  */
	 public void setStuScoreMap(Map<String,Map<String,Float>> stuScoreMap,List<NewGkScoreResult> scoreList,List<String> ysySubIds){
		 boolean flag=CollectionUtils.isNotEmpty(ysySubIds);
		 if(CollectionUtils.isNotEmpty(scoreList)){
			 for(NewGkScoreResult score:scoreList){
				 //if(chosenStudentIds.contains(score.getStudentId())){
					 Map<String,Float> scoreMap=stuScoreMap.get(score.getStudentId());
					 if(scoreMap==null){
						 scoreMap=new HashMap<String,Float>();
					 }
					 if(flag){
						 if(ysySubIds.contains(score.getSubjectId())){
							 Float f=scoreMap.get(NewGkElectiveConstant.YSY_SUBID);
							 if(f==null){
								 f=0f;
							 }
							 scoreMap.put(NewGkElectiveConstant.YSY_SUBID, f+score.getScore());
						 }else{
							 scoreMap.put(score.getSubjectId(),score.getScore());
						 }
					 }else{
						 scoreMap.put(score.getSubjectId(),score.getScore());
					 }
					 stuScoreMap.put(score.getStudentId(), scoreMap);
				 //}
			 }
		 }
	 }
	 /**
	  * 获取平均分 总分
	  * @param avgMap
	  * @param allMap
	  * @param lastList
	  * @param scoreList
	  * @return
	  */
	 public int getAvgAllMap(Map<String,Float> avgMap,Map<String,Float> allMap,List<NewGkConditionDto> lastList,
			 List<NewGkScoreResult> scoreList,List<String> ysySubIds){
		 List<String> studentIds=EntityUtils.getList(lastList, NewGkConditionDto::getStudentId);
		 boolean flag=CollectionUtils.isNotEmpty(ysySubIds);
		 int maxCount=lastList.size();
		 if(CollectionUtils.isNotEmpty(scoreList)){
			 for(NewGkScoreResult score:scoreList){
				 if(studentIds.contains(score.getStudentId())){
					 if(flag && ysySubIds.contains(score.getSubjectId())){
						 score.setSubjectId(NewGkElectiveConstant.YSY_SUBID);
					 }
					 Float allScore=allMap.get(score.getSubjectId());
					 if(allScore==null) allScore=0f;
					 allMap.put(score.getSubjectId(), allScore+score.getScore());
				 }
			 }
			 for(String subjectId:allMap.keySet()){
				Float avg=0f;
				if(maxCount>0){
					avg = allMap.get(subjectId)/maxCount;
					BigDecimal  b  = new BigDecimal(avg);
					avg=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();  
				}
				avgMap.put(subjectId, avg);
			 }
		 }
		 return maxCount;
	 }
	 @ResponseBody
	 @RequestMapping("/divideClass/findDivideClass")
	 @ControllerInfo("查询组合班级")
	 public String findDivideClass(@PathVariable String divideId,String subjectIdstr,String classType,ModelMap map){
		 JSONArray jsonArray=new JSONArray();
		 JSONObject json=null;
		 //3科分班班级
		 List<NewGkDivideClass> divideClassList=getDivideClassList(newGkDivideClassService.findByDivideIdAndClassType(this.getLoginInfo().getUnitId(), divideId, new String[]{classType},false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false), subjectIdstr);
		 if(CollectionUtils.isNotEmpty(divideClassList)){
			 for(NewGkDivideClass divideClass:divideClassList){
				 json=new JSONObject();
				 json.put("id", divideClass.getId());
				 json.put("className", divideClass.getClassName());
				 jsonArray.add(json);
			 }
		 }
		 return jsonArray.toJSONString();
	 }
	 /**
	  * 获取3科组合每个组合对应的分班结果
	  * @param allDivideClassList
	  * @param subjectIdstr
	  * @return
	  */
	 public List<NewGkDivideClass> getDivideClassList(List<NewGkDivideClass> allDivideClassList,String subjectIdstr){
		 List<NewGkDivideClass> divideClassList=new ArrayList<NewGkDivideClass>();
		 if(CollectionUtils.isNotEmpty(allDivideClassList)){
			 for(NewGkDivideClass allDivideClass:allDivideClassList){
				 if(subjectIdstr.equals(allDivideClass.getSubjectIds())){
					 divideClassList.add(allDivideClass);
				 }
			 }
		 }
		 return divideClassList;
	 }
	 /**
	  * 获取3科组合list
	  * @param choiceId
	  * @param galleryful
	  * @return
	  */
	 public List<NewGkConditionDto> getNewDtoList(String choiceId){
		//選課結果
		List<NewGkChoResult>  resultList=newGkChoResultService.findByChoiceIdAndKindType(getLoginInfo().getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,choiceId);
		List<Course> courseList= SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()),new TR<List<Course>>(){});
		Collections.sort(courseList,new Comparator<Course>() {
			@Override
			public int compare(Course o1, Course o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		
		//计算组合
        Integer[] cSize = new Integer[courseList.size()];
        for(int i = 0;i < courseList.size();i++){
        	cSize[i] = i;
        }
       //获取学生对应的选课ids
		Map<String,NewGkChoResultDto> dtoMap=new HashMap<String,NewGkChoResultDto>();
		if(CollectionUtils.isNotEmpty(resultList)){
			NewGkChoResultDto dto=null;
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
		//三科目选择结果
        CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize,3);
        Integer[][] result = combineAlgorithm.getResutl();
        List<NewGkConditionDto> newConditionList3=newGkChoiceService.findSubRes(courseList, dtoMap, result, 3);
        List<NewGkConditionDto> newDtoList=new ArrayList<NewGkConditionDto>();
		if(CollectionUtils.isNotEmpty(newConditionList3)){
			for(NewGkConditionDto newDto:newConditionList3){
				if(newDto.getSumNum()>0){
					newDtoList.add(newDto);
				}
			}
		}
		return newDtoList;
	 }

	@RequestMapping("/divideClass/showItem")
    @ControllerInfo(value = "查看分班设置")
	public String showArrayItem(@PathVariable String divideId,ModelMap map){
		
    	NewGkDivide newDivide = newGkDivideService.findById(divideId);
    	if(newDivide==null){
    		return errorFtl(map, "分班方案不存在");
    	}
    	//选课情况 
		NewGkChoice newGkChoice = newGkChoiceService.findById(newDivide.getChoiceId());
		map.put("newGkChoice", newGkChoice);
		
		List<NewGkOpenSubject> openlist = newGkOpenSubjectService.findByDivideId(divideId);
		List<String> courseAList=new ArrayList<String>();
		List<String> courseBList=new ArrayList<String>();
		List<String> courseOList=new ArrayList<String>();
		List<String> courseLAList=new ArrayList<String>();
		List<String> courseLJList=new ArrayList<String>();
		List<String> courseLOList=new ArrayList<String>();
		List<String> courseWAList=new ArrayList<String>();
		List<String> courseWJList=new ArrayList<String>();
		List<String> courseWOList=new ArrayList<String>();
		
		if(CollectionUtils.isNotEmpty(openlist)){
			Set<String> subjectIds = EntityUtils.getSet(openlist, NewGkOpenSubject::getSubjectId);
			List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
			Map<String,Course> courseMap=EntityUtils.getMap(courseList, e->e.getId());
			for(NewGkOpenSubject sub:openlist){
				if(!courseMap.containsKey(sub.getSubjectId())){
					continue;
				}
				if(NewGkElectiveConstant.DIVIDE_GROUP_1.equals(sub.getGroupType())){
					if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(sub.getSubjectType())){
						courseAList.add(courseMap.get(sub.getSubjectId()).getSubjectName());
					}else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(sub.getSubjectType())){
						courseBList.add(courseMap.get(sub.getSubjectId()).getSubjectName());
					}else{
						courseOList.add(courseMap.get(sub.getSubjectId()).getSubjectName());
					}
				}
			}
			//如果是文理的
//			if(NewGkElectiveConstant.DIVIDE_TYPE_03.equals(newDivide.getOpenType())
//					|| NewGkElectiveConstant.DIVIDE_TYPE_04.equals(newDivide.getOpenType())){
//				List<NewGkDivideExDto> dtoList = makeTwoSet(newDivide, openlist, courseMap);
//				map.put("dtoList", dtoList);
//			}else {
				//参数设置--科目以及人数范围(学生数量暂时不考虑)
				
				List<NewGKStudentRangeEx> allExList = newGKStudentRangeExService.findByDivideId(divideId);
				
				if(CollectionUtils.isNotEmpty(allExList)) {
					Collections.sort(allExList, new Comparator<NewGKStudentRangeEx>() {
						@Override
						public int compare(NewGKStudentRangeEx o1, NewGKStudentRangeEx o2) {
							if(o1.getSubjectType().equals(o2.getSubjectType())) {
								if(o1.getSubjectId().equals(o2.getSubjectId())) {
									if(StringUtils.isNotBlank(o1.getRange())) {
										return o1.getRange().compareTo(o2.getRange());
									}
								}else {
									return o1.getSubjectId().compareTo(o2.getSubjectId());
								}
							}else {
								return o1.getSubjectType().compareTo(o2.getSubjectType());
							}
							
							return 0;
						}
					});
					
					
					//选考
					List<NewGKStudentRangeEx> aList=new ArrayList<>();
					//学考
					List<NewGKStudentRangeEx> bList=new ArrayList<>();
					for(NewGKStudentRangeEx ex:allExList) {
						if(!courseMap.containsKey(ex.getSubjectId())) {
							continue;
						}
						ex.setSubjectName(courseMap.get(ex.getSubjectId()).getSubjectName());
						if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(ex.getSubjectType())) {
							if(StringUtils.isNotBlank(ex.getRange())) {
								ex.setSubjectName(ex.getSubjectName()+ex.getRange());
							}
							aList.add(ex);
						}else {
							bList.add(ex);
						}
					}
					map.put("aList", aList);
					map.put("bList", bList);
				}
					
//			}
			
		}
		NewGkReferScore referScore=null;
		if(StringUtils.isNotBlank(newDivide.getReferScoreId())){
			referScore =newGkReferScoreService.findById(newDivide.getReferScoreId(),false,null);
			//referScore =newGkReferScoreService.findById(newDivide.getReferScoreId(),true);
		}
		
		Collections.sort(courseAList);
		Collections.sort(courseBList);
		
		map.put("referScore", referScore);
		map.put("courseAList", courseAList);
		map.put("courseBList", courseBList);
		map.put("courseOList", courseOList);
		map.put("courseLAList", courseLAList);
		map.put("courseLJList", courseLJList);
		map.put("courseLOList", courseLOList);
		map.put("courseWAList", courseWAList);
		map.put("courseWJList", courseWJList);
		map.put("courseWOList", courseWOList);
		map.put("newGkDivide", newDivide);
		
		return "/newgkelective/divide/divideShowSet.ftl";
    	
	}
 
	public String checkStudentChoose(NewGkChoice newGkChoice,Map<String,Set<String>> subjectIdsByGroup){
		List<StudentResultDto> list = findByChoiceSubjectIds(newGkChoice, null);
		if(CollectionUtils.isEmpty(list)){
			return "学生选课数据为空";
		}
		if(subjectIdsByGroup.size()==0){
			return "存在学生选课数据不属于文理范围";
		}
		for (StudentResultDto studentResultDto : list) {
			if(CollectionUtils.isNotEmpty(studentResultDto.getResultList())){
				Set<String> chooseSet = EntityUtils.getSet(studentResultDto.getResultList(), NewGkChoResult::getSubjectId);
				boolean flag=false;
				for(Entry<String, Set<String>> item:subjectIdsByGroup.entrySet()){
					Set<String> set = item.getValue();
					 if(checkSameSet(chooseSet,set)){
						 flag=true;
						 break;
					 }
				}
				if(!flag){
					return "存在学生选课数据不属于文理范围";
				}
			}else{
//				return "存在学生选课数据不属于文理范围";
			}
		}
		return null;
	}
	
	private boolean checkSameSet(Set<String> sub1,Set<String> sub2){
		if(sub1.size()!=sub2.size()){
			return false;
		}
		Set<String> set=new HashSet<String>();
		set.addAll(sub1);
		set.addAll(sub2);
		if(set.size()!=sub1.size()){
			return false;
		}
		return true;
	}
	
	
	@RequestMapping("/placeArrange/listByBath")
	public String arrayItemListByBath(@PathVariable String divideId, String arrayId, String arrayItemId, 
			HttpServletRequest request, ModelMap map){
		NewGkDivide divide = newGkDivideService.findById(divideId);
		if(divide==null){
			return errorFtl(map, "分班方案不存在");
		}
		map.put("lessArrayId", StringUtils.trimToEmpty(request.getParameter("lessArrayId")));
		map.put("plArrayId", StringUtils.trimToEmpty(request.getParameter("plArrayId")));
		map.put("gradeId", divide.getGradeId());
		if(NewGkElectiveConstant.DIVIDE_TYPE_01.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_02.equals(divide.getOpenType()) 
				|| NewGkElectiveConstant.DIVIDE_TYPE_05.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_06.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_08.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_09.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divide.getOpenType())){
			return toMakeRelate2(divideId,arrayItemId,arrayId,map);
		}else if(NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType())) {
			// 组合固定
//			map.put("fromSolve", fromSolve);
			
			boolean isNext=false;
			map.put("gradeId", divide.getGradeId());
			map.put("isFakeXzb", true);
			map.put("isNext", isNext);
			map.put("lessArrayId", StringUtils.trimToEmpty(request.getParameter("lessArrayId")));
			map.put("plArrayId", StringUtils.trimToEmpty(request.getParameter("plArrayId")));
			toPlaceArrangeList2(divideId, arrayId, arrayItemId, null, map,divide.getOpenType(),true);
			
			return "/newgkelective/placeArrange/placeArrangeList3.ftl";	
		}else{
			return errorFtl(map, "没有需要设置");
		}
	}
	
	@ResponseBody
	@RequestMapping("/placeArrange/saveByBath")
	public String savePlaceArrangeItemByBath(@PathVariable String divideId, String arrayId, NewGkPlaceItemDto newGkPlaceItemDto, String arrayItemId){
		try{
			List<NewGkPlaceItem> newGkPlaceItemList1 = newGkPlaceItemDto.getNewGkPlaceItemList1();
			if(CollectionUtils.isEmpty(newGkPlaceItemList1)){
				return error("没有需要保存的数据！");
			}
			List<NewGkPlaceItem> newGkPlaceItemList=new ArrayList<NewGkPlaceItem>();
			NewGkPlaceItem dto=new NewGkPlaceItem();
			for(NewGkPlaceItem item : newGkPlaceItemList1){
				if(StringUtils.isNotBlank(item.getObjectIds())){
					String[] obj = item.getObjectIds().split(",");
					for(String o:obj){
						dto=new NewGkPlaceItem();
						dto.setId(UuidUtils.generateUuid());
						dto.setArrayItemId(arrayItemId);
						dto.setObjectId(o);
						dto.setType(NewGkElectiveConstant.CLASS_TYPE_2);
						dto.setPlaceId(item.getPlaceId());
						newGkPlaceItemList.add(dto);
					}
				}
				if(StringUtils.isNotBlank(item.getObjectIds2())){
					String[] obj = item.getObjectIds2().split(",");
					for(String o:obj){
						dto=new NewGkPlaceItem();
						dto.setId(UuidUtils.generateUuid());
						dto.setArrayItemId(arrayItemId);
						dto.setObjectId(o);
						dto.setType(NewGkElectiveConstant.CLASS_TYPE_2);
						dto.setPlaceId(item.getPlaceId());
						newGkPlaceItemList.add(dto);
					}
				}
			}
			// 将被拆分班级的场地应用到 拆分出来的班级上去
			Map<String, String> placeMap = EntityUtils.getMap(newGkPlaceItemList, NewGkPlaceItem::getObjectId,NewGkPlaceItem::getPlaceId);
			String unitId = getLoginInfo().getUnitId();
			List<NewGkDivideClass> classList = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId,
					new String[] { NewGkElectiveConstant.CLASS_TYPE_2 }, false,
					NewGkElectiveConstant.CLASS_SOURCE_TYPE1, true);
			Map<String, List<String>> familyMap = classList.stream().filter(e->e.getParentId()!=null).collect(Collectors.groupingBy(NewGkDivideClass::getParentId,
					Collectors.mapping(NewGkDivideClass::getId, Collectors.toList())));
			for (String paCid : familyMap.keySet()) {
				List<String> list = familyMap.get(paCid);
				if(placeMap.containsKey(paCid)) {
					String pId = placeMap.get(paCid);
					for (String chCid : list) {
						dto=new NewGkPlaceItem();
						dto.setId(UuidUtils.generateUuid());
						dto.setArrayItemId(arrayItemId);
						dto.setObjectId(chCid);
						dto.setType(NewGkElectiveConstant.CLASS_TYPE_2);
						dto.setPlaceId(pId);
						newGkPlaceItemList.add(dto);
					}
				}
			}
			
			newGkPlaceItemService.saveByType(newGkPlaceItemList, arrayId, arrayItemId, NewGkElectiveConstant.CLASS_TYPE_2);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@ResponseBody
	@RequestMapping("/placeArrange/deleteByBath")
	public String deletePlaceArrangeItemByBath(@PathVariable String divideId, String arrayItemId, String arrayId){
		try{
			newGkPlaceItemService.deleteByArrayItemIdAndType(arrayId, arrayItemId, NewGkElectiveConstant.CLASS_TYPE_2);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return success("操作成功");
	}
	
	@ResponseBody
	@RequestMapping("/placeArrange/deleteByAll")
	public String deletePlaceArrangeItemByAll(@PathVariable String divideId, String arrayItemId, String arrayId, String placeType){
		try{
			newGkPlaceItemService.deleteByArrayItemIdAndType(arrayId, arrayItemId, placeType);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return success("操作成功");
	}
	
	
	@ResponseBody
    @RequestMapping("/divideClass/copy")
    @ControllerInfo(value = "复制分班参数")
    public String doCopy(@PathVariable String divideId) {
		 try{
			 NewGkDivide divide = newGkDivideService.findById(divideId);
			 if(divide==null){
				return error("分班方案不存在，请刷新重新操作!");
			 }
			 newGkDivideService.saveCopyDivide(divide); 
		 }catch (Exception e) {
	         e.printStackTrace();
	         return returnError("操作失败！", e.getMessage());
	     }
		 return success("操作成功！");
	}
	
	private String toMakeRelate2(String divideId,String arrayItemId,String arrayId,ModelMap map) {
		NewGkArrayItem newGkArrayItem = newGkArrayItemService.findOne(arrayItemId);
		String unitId=this.getLoginInfo().getUnitId();
		//具体内容
		List<NewGkDivideClass> divideClazzList = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId, 
				new String[]{NewGkElectiveConstant.CLASS_TYPE_1, NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_2,
						NewGkElectiveConstant.CLASS_TYPE_3},
				false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		//教学班行政班增加学生信息
		List<NewGkDivideClass> xzbAndJxbClazzList=divideClazzList.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_1) 
				|| e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_2)
				|| e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_3))
				.collect(Collectors.toList());
		newGkDivideClassService.toMakeStudentList(unitId, divideId, xzbAndJxbClazzList);
		
		Map<String, List<NewGkDivideClass>> xzbHbMap = divideClazzList.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_3))
				.collect(Collectors.groupingBy(NewGkDivideClass::getParentId));
		//key:studentId value:xzbId
		Map<String,String> xzbIdByStudentId=new HashMap<>();
		Map<String, NewGkDivideClass> classMap=new HashMap<>();
		//key:zhbId value xzbId
		Map<String,String> xzbIdByrelateMap=new HashMap<>();
		//key:zhbId value jxbId
		Map<String,Set<String>> jxbIdByrelateMap=new HashMap<>();
		List<NewGkDivideClass> zhbClazzList=new ArrayList<>();
		
		List<NewGkDivideClass> jxbdivideClazzList=new ArrayList<NewGkDivideClass>();
		List<NewGkDivideClass> xzbdivideClazzList=new ArrayList<NewGkDivideClass>();
		Set<String> xzbIds=new HashSet<String>();
		
		Set<String> aBath=new HashSet<>();
		Set<String> bBath=new HashSet<>();
		
		for(NewGkDivideClass clazz:divideClazzList) {
			classMap.put(clazz.getId(), clazz);
			if(NewGkElectiveConstant.CLASS_TYPE_1.equals(clazz.getClassType())) {
				if(StringUtils.isNotBlank(clazz.getRelateId())) {
					xzbIdByrelateMap.put(clazz.getRelateId(), clazz.getId());
				}
				xzbIds.add(clazz.getId());
				if(CollectionUtils.isNotEmpty(clazz.getStudentList())) {
					for(String st:clazz.getStudentList()) {
						xzbIdByStudentId.put(st, clazz.getId());
					}
				}
				xzbdivideClazzList.add(clazz);
				List<NewGkDivideClass> list = xzbHbMap.get(clazz.getId());
				if(list != null) {
					if(list.size()>1) {
						xzbIds.add(list.get(1).getId());
						xzbdivideClazzList.add(list.get(1));
					}
					list.forEach(e->xzbIds.add(e.getId()));
				}
			}else if(NewGkElectiveConstant.CLASS_TYPE_2.equals(clazz.getClassType()) && StringUtils.isNotBlank(clazz.getBatch())) {
				if(StringUtils.isNotBlank(clazz.getRelateId())) {
					if(!jxbIdByrelateMap.containsKey(clazz.getRelateId())) {
						jxbIdByrelateMap.put(clazz.getRelateId(), new HashSet<>());
					}
					jxbIdByrelateMap.get(clazz.getRelateId()).add(clazz.getId());
				}
				if(clazz.getSubjectType().equals(NewGkElectiveConstant.SUBJECT_TYPE_A)) {
					aBath.add(clazz.getBatch());
				}else if(clazz.getSubjectType().equals(NewGkElectiveConstant.SUBJECT_TYPE_B)) {
					bBath.add(clazz.getBatch());
				}
				jxbdivideClazzList.add(clazz);
			}else if(NewGkElectiveConstant.CLASS_TYPE_0.equals(clazz.getClassType())){
				zhbClazzList.add(clazz);
			}
		}
		
		//
		Set<String> allBatchs = new HashSet<>();
		Map<String,Set<String>> stuBatchMap = new HashMap<>();
		for (NewGkDivideClass jxb : jxbdivideClazzList) {
			String batchStr = jxb.getSubjectType()+"_"+jxb.getBatch();
			allBatchs.add(batchStr);
			if(CollectionUtils.isNotEmpty(jxb.getStudentList())) {
				for (String stuId : jxb.getStudentList()) {
					Set<String> batchs = stuBatchMap.get(stuId);
					if(batchs == null) {
						batchs = new HashSet<>();
						stuBatchMap.put(stuId, batchs);
					}
					batchs.add(batchStr);
				}
			}
		}
		
		//过滤可使用的行政班场地		
		Set<String> allNotArrange=new HashSet<>();//行政班所在场地完全不能被使用--页面不展示
		//key:xzbId,key:A/B value 批次
		Map<String,Map<String,Set<String>>> notArrangeList=new HashMap<>();
		// TODO 行政班  可以安排的 批次
		for(NewGkDivideClass xzb:xzbdivideClazzList) {
			Set<String> floatingBatchs = xzb.getStudentList().stream().filter(e->stuBatchMap.containsKey(e))
					.flatMap(e->stuBatchMap.get(e).stream()).collect(Collectors.toSet());
			List<String> freeBatcs = allBatchs.stream().filter(e->!floatingBatchs.contains(e)).collect(Collectors.toList());
			Map<String,Set<String>> innerMap = new HashMap<>();
			for (String batchStr : freeBatcs) {
				String[] split = batchStr.split("_");
				String subjectType = split[0];
				String batchNum = split[1];
				Set<String> nums = innerMap.get(subjectType);
				if(nums == null) {
					nums = new HashSet<>();
					innerMap.put(subjectType, nums);
				}
				nums.add(batchNum);
			}
			if(innerMap.size()>0) {
				notArrangeList.put(xzb.getId(), innerMap);
			}
			if(CollectionUtils.isEmpty(floatingBatchs)) {
				allNotArrange.add(xzb.getId());
			}
		}
		
		//场地所有数据
		List<NewGkplaceArrange> newGkplaceArrangeList = newGkplaceArrangeService.findByArrayItemId(arrayItemId);
		Set<String> choisePlaceSet = new HashSet<String>();
		for(NewGkplaceArrange item : newGkplaceArrangeList){
			choisePlaceSet.add(item.getPlaceId());
		}
		
		//场地基础数据
		Map<String,String> placeNameKap = SUtils.dt(teachPlaceRemoteService.findTeachPlaceMapByAttr(choisePlaceSet.toArray(new String[0]), "placeName"),
				new TR<Map<String,String>>(){});
		
		//班级已安排场地
		List<NewGkPlaceItem> newGkPlaceItemList = newGkPlaceItemService.findByArrayItemIdWithMaster(arrayItemId);
		//key:classId
		Map<String,NewGkPlaceItem> classPlaceItem=new HashMap<String,NewGkPlaceItem>();//包括行政班教学班
		Set<String> oldArrangeArrange=new HashSet<>();//已经保存的教学班以及行政班id
		//key:placeId
		Map<String,Set<String>> classIdByPlaceId=new HashMap<>();
		//不再过滤
		if(CollectionUtils.isNotEmpty(newGkPlaceItemList)){
			for(NewGkPlaceItem item:newGkPlaceItemList){
				classPlaceItem.put(item.getObjectId(), item);
//				if(!NewGkElectiveConstant.CLASS_TYPE_2.equals(item.getType())) {
//					continue;
//				}
				//班级
				oldArrangeArrange.add(item.getObjectId());
				if(!classIdByPlaceId.containsKey(item.getPlaceId())) {
					classIdByPlaceId.put(item.getPlaceId(), new HashSet<>());
				}
				classIdByPlaceId.get(item.getPlaceId()).add(item.getObjectId());
			}
		}
		
		//左边班级分组
		//[A1,A,1,选考1]
		List<String[]> batchKeys=new ArrayList<String[]>();
		Map<String,List<NewGkDivideClass>> bathClassMap=new HashMap<String, List<NewGkDivideClass>>();
		List<String[]> stuCountByClassId;
		Map<String,Integer> stuNumByClassId;
		for(NewGkDivideClass n:jxbdivideClazzList){
			String key=n.getSubjectType()+"_"+n.getBatch();
			String name=NewGkElectiveConstant.SUBJECT_TYPE_A.equals(n.getSubjectType())?"选考":"学考";
			if(!bathClassMap.containsKey(key)){
				bathClassMap.put(key, new ArrayList<NewGkDivideClass>());
				batchKeys.add(new String[]{key,n.getSubjectType(),n.getBatch(),name+n.getBatch()});
			}
			//学生信息
			stuCountByClassId = new ArrayList<String[]>();
			if(CollectionUtils.isNotEmpty(n.getStudentList())) {
				stuNumByClassId=new HashMap<>();
				for(String st:n.getStudentList()) {
					String xzbId = xzbIdByStudentId.get(st);
					if(StringUtils.isNotBlank(xzbId)) {
						if(stuNumByClassId.containsKey(xzbId)) {
							stuNumByClassId.put(xzbId, stuNumByClassId.get(xzbId)+1);
						}else {
							stuNumByClassId.put(xzbId, 1);
						}
					}
				}
				if(stuNumByClassId.size()>0) {
					for(Entry<String, Integer> iitem:stuNumByClassId.entrySet()) {
						String[] arr=new String[] {classMap.get(iitem.getKey()).getClassName(),iitem.getValue().toString(),iitem.getKey()};
						stuCountByClassId.add(arr);
					}
					//从大到小排序
					Collections.sort(stuCountByClassId, new Comparator<String[]>() {

						@Override
						public int compare(String[] o1, String[] o2) {
							if(Integer.parseInt(o1[1])==Integer.parseInt(o2[1])) {
								return o1[0].compareTo(o2[0]);
							}
							return Integer.parseInt(o2[1])-Integer.parseInt(o1[1]);
						}
					});
				}
				
			}
			n.setStuCountByClassId(stuCountByClassId);
			
			if(!oldArrangeArrange.contains(n.getId())) {
				bathClassMap.get(key).add(n);
			}
		}
				
		//排序
		if(CollectionUtils.isNotEmpty(batchKeys)){
			Collections.sort(batchKeys, new Comparator<String[]>() {
				@Override
				public int compare(String[] o1, String[] o2) {
					if(o1 == null || o2 == null) {
						return 0;
					}
					return o1[0].compareTo(o2[0]);
				}
			});
		}
		//右边数据
		List<GkPlaceDto> dtoList=new ArrayList<GkPlaceDto>();
		GkPlaceDto dto;
		Set<String> xzbPlaceIds=new HashSet<String>();//行政班用掉的场地
		Map<String,GkPlaceDto> dtoMap=new HashMap<String,GkPlaceDto>();
		for(NewGkDivideClass ss:xzbdivideClazzList){
			NewGkPlaceItem xzbPlace = classPlaceItem.get(ss.getId());
			if(xzbPlace==null){
				return errorFtl(map, "存在行政班没有安排场地");
			}
			xzbPlaceIds.add(xzbPlace.getPlaceId());
			if(dtoMap.containsKey(xzbPlace.getPlaceId())){
				return errorFtl(map, "存在某场地安排多个行政班");
			}
			if(allNotArrange.contains(ss.getId())) {
				//3+0场地不能使用
				continue;
			}
			String noBath="";//A_1,A_2
			String nameBath="";
			if(notArrangeList.containsKey(ss.getId())) {
				Map<String, Set<String>> notMap = notArrangeList.get(ss.getId());
				
				if(CollectionUtils.isNotEmpty(notMap.get("A"))) {
					for(String sss:notMap.get("A")){
						noBath=noBath+",A_"+sss;
						nameBath=nameBath+"、选考"+sss;
					}
					
				}
				if(CollectionUtils.isNotEmpty(notMap.get("B"))) {
					for(String sss:notMap.get("B")){
						noBath=noBath+",B_"+sss;
						nameBath=nameBath+"、学考"+sss;
					}
				}
				if(StringUtils.isNotBlank(noBath)) {
					noBath=noBath.substring(1);
					nameBath=nameBath.substring(1)+"下的教学班不能使用本场地。";
				}
			}
			dto=new GkPlaceDto();
			dto.setXzbId(ss.getId());
			dto.setXzbClassName(ss.getClassName());
			dto.setPlaceId(xzbPlace.getPlaceId());
			if(!placeNameKap.containsKey(xzbPlace.getPlaceId())){
				return errorFtl(map, "存在某行政班安排的场地在设定范围之外，请重新操作");
			}
			dto.setPlaceName(placeNameKap.get(xzbPlace.getPlaceId()));
			dto.setRemake(nameBath);
			dto.setNoCanArrangeBath(noBath);
			dto.setJxbClass(new ArrayList<NewGkDivideClass>());
			for(String cc:classIdByPlaceId.get(xzbPlace.getPlaceId())) {
				if(xzbIds.contains(cc)) {
					continue;
				}
				if(classMap.containsKey(cc)) {
					dto.getJxbClass().add(classMap.get(cc));
				}
				
			}
			dtoMap.put(xzbPlace.getPlaceId(), dto);
			dtoList.add(dto);
		}
		//空教室
		for(String p:choisePlaceSet){
			if(!xzbPlaceIds.contains(p)){
				dto=new GkPlaceDto();
				dto.setNoCanArrangeBath("");
				dto.setPlaceId(p);
				dto.setPlaceName(placeNameKap.get(p));
				dto.setRemake("备用教室，暂时没有安排作为行政班场地");
				dto.setJxbClass(new ArrayList<NewGkDivideClass>());
				if(CollectionUtils.isNotEmpty(classIdByPlaceId.get(p))) {
					for(String cc:classIdByPlaceId.get(p)) {
						if(xzbIds.contains(cc)) {
							continue;
						}
						if(classMap.containsKey(cc)) {
							dto.getJxbClass().add(classMap.get(cc));
						}
						
					}
				}
				
				dtoMap.put(p, dto);
				dtoList.add(dto);
			}
		}
		Collections.sort(dtoList, new Comparator<GkPlaceDto>() {
			@Override
			public int compare(GkPlaceDto o1, GkPlaceDto o2) {
				if(StringUtils.isNotBlank(o2.getXzbClassName()) && StringUtils.isBlank(o1.getXzbClassName())) {
					//按行政班数据
					return 1;
				}else if(StringUtils.isNotBlank(o1.getXzbClassName()) && StringUtils.isBlank(o2.getXzbClassName())){
					return -1;
				}if(StringUtils.isNotBlank(o1.getXzbClassName()) && StringUtils.isNotBlank(o2.getXzbClassName())){
					return o1.getXzbClassName().compareTo(o2.getXzbClassName());
				}else{
					return o1.getPlaceName().compareTo(o2.getPlaceName());
				}
			}
		});
		
		map.put("arrayId", arrayId);
		map.put("batchKeys", batchKeys);
		map.put("dtoList", dtoList);//右边场地列表
		map.put("bathClassMap", bathClassMap);//具体教学班信息
		map.put("divideId", divideId);
		map.put("newGkArrayItem", newGkArrayItem);
		map.put("dtoList", dtoList);
		//批次
//		return "/newgkelective/placeArrange/placeBathList.ftl";	
		return "/newgkelective/placeArrange/placeBathList1.ftl";
	}

	@RequestMapping("/divide/splitSubject")
    public String divideSplitSubject(@PathVariable String divideId, String gradeId, ModelMap map) {
        String unitId = getLoginInfo().getUnitId();

        // 返回历史拆班信息
	    List<NewGkChoRelation> newGkChoRelationList = newGkChoRelationService.findByChoiceIdsAndObjectType(unitId, new String[]{divideId}, NewGkElectiveConstant.CHOICE_TYPE_09);
        if (newGkChoRelationList.size() == 1) {
            map.put("firstSubjectId", newGkChoRelationList.get(0).getObjectValue());
        }
        if (newGkChoRelationList.size() == 2) {
            map.put("firstSubjectId", newGkChoRelationList.get(0).getObjectValue());
            map.put("secondSubjectId", newGkChoRelationList.get(1).getObjectValue());
        }

        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
	    List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitIdAndTypeAndLikeSection(unitId, BaseConstants.SUBJECT_TYPE_BX, grade.getSection().toString()), new TR<List<Course>>() {});
	    List<NewGkDivideClass> newGkDivideClassList = newGkDivideClassService.findByDivideIdIn(new String[]{divideId});
	    Set<String> courseIdSet = EntityUtils.getSet(newGkDivideClassList, NewGkDivideClass::getSubjectIds);
	    courseList = courseList.stream().filter(e -> !courseIdSet.contains(e.getId())).collect(Collectors.toList());
	    map.put("divideId", divideId);
	    map.put("courseList", courseList);
	    return "/newgkelective/divide/divideSplitSubject.ftl";
    }

    @RequestMapping("/divide/saveSplit")
    @ResponseBody
    public String saveSubjectSplit(@PathVariable String divideId, String splitSubjectCode, String firstSubjectId, String secondSubjectId) {
        List<NewGkArray> newGkArrayList = newGkArrayService.findByDivideId(divideId);
        if (CollectionUtils.isNotEmpty(newGkArrayList)) {
            return error("该分班方案已被排课引用，无法拆分");
        }

	    List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitCourseCodes(getLoginInfo().getUnitId(), new String[]{"3037"}), Course.class);
        String splitSubjectId = courseList.get(0).getId();

        String unitId = getLoginInfo().getUnitId();
	    Map<String, String> courseIdToNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(new String[]{splitSubjectId, firstSubjectId, secondSubjectId}), new TR<Map<String, String>>(){});
	    List<NewGkDivideClass> newGkDivideClassList = newGkDivideClassService
                .findByDivideIdAndClassTypeAndSubjectIds(unitId, divideId, NewGkElectiveConstant.CLASS_TYPE_2, splitSubjectId, true);
        List<NewGkDivideClass> splitDivideClassList = new ArrayList<>();
        List<NewGkClassStudent> newGkClassStudentList = new ArrayList<>();
        List<NewGkChoRelation> newGkChoRelationList = new ArrayList<>();
	    for (NewGkDivideClass one : newGkDivideClassList) {
            NewGkDivideClass firstNewGkDivideClass = newGkDivideClassClone(one);
            firstNewGkDivideClass.setSubjectIds(firstSubjectId);
            firstNewGkDivideClass.setClassName(one.getClassName().replace(courseIdToNameMap.get(one.getSubjectIds()), courseIdToNameMap.get(firstSubjectId)) + "（拆分）");
            splitDivideClassList.add(firstNewGkDivideClass);
            if (CollectionUtils.isNotEmpty(firstNewGkDivideClass.getStudentList())) {
                for (String studentId : firstNewGkDivideClass.getStudentList()) {
                    NewGkClassStudent newGkClassStudent = new NewGkClassStudent();
                    newGkClassStudent.setId(UuidUtils.generateUuid());
                    newGkClassStudent.setUnitId(unitId);
                    newGkClassStudent.setClassId(firstNewGkDivideClass.getId());
                    newGkClassStudent.setDivideId(divideId);
                    newGkClassStudent.setStudentId(studentId);
                    newGkClassStudent.setCreationTime(new Date());
                    newGkClassStudent.setModifyTime(new Date());
                    newGkClassStudentList.add(newGkClassStudent);
                }
            }

            NewGkDivideClass secondNewGkDivideClass = newGkDivideClassClone(one);
            secondNewGkDivideClass.setSubjectIds(secondSubjectId);
            secondNewGkDivideClass.setClassName(one.getClassName().replace(courseIdToNameMap.get(one.getSubjectIds()), courseIdToNameMap.get(secondSubjectId)) + "（拆分）");
            splitDivideClassList.add(secondNewGkDivideClass);
            if (CollectionUtils.isNotEmpty(secondNewGkDivideClass.getStudentList())) {
                for (String studentId : secondNewGkDivideClass.getStudentList()) {
                    NewGkClassStudent newGkClassStudent = new NewGkClassStudent();
                    newGkClassStudent.setId(UuidUtils.generateUuid());
                    newGkClassStudent.setUnitId(unitId);
                    newGkClassStudent.setClassId(secondNewGkDivideClass.getId());
                    newGkClassStudent.setDivideId(divideId);
                    newGkClassStudent.setStudentId(studentId);
                    newGkClassStudent.setCreationTime(new Date());
                    newGkClassStudent.setModifyTime(new Date());
                    newGkClassStudentList.add(newGkClassStudent);
                }
            }
        }
        NewGkChoRelation newGkChoRelation = new NewGkChoRelation();
        newGkChoRelation.setId(UuidUtils.generateUuid());
        newGkChoRelation.setChoiceId(divideId);
        newGkChoRelation.setUnitId(unitId);
        newGkChoRelation.setObjectType(NewGkElectiveConstant.CHOICE_TYPE_09);
        newGkChoRelation.setObjectValue(firstSubjectId);
        newGkChoRelation.setObjectTypeVal(splitSubjectId);
        newGkChoRelation.setCreationTime(new Date());
        newGkChoRelation.setModifyTime(new Date());
        newGkChoRelationList.add(newGkChoRelation);
        newGkChoRelation = new NewGkChoRelation();
        newGkChoRelation.setId(UuidUtils.generateUuid());
        newGkChoRelation.setChoiceId(divideId);
        newGkChoRelation.setUnitId(unitId);
        newGkChoRelation.setObjectType(NewGkElectiveConstant.CHOICE_TYPE_09);
        newGkChoRelation.setObjectValue(secondSubjectId);
        newGkChoRelation.setObjectTypeVal(splitSubjectId);
        newGkChoRelation.setCreationTime(new Date());
        newGkChoRelation.setModifyTime(new Date());
        newGkChoRelationList.add(newGkChoRelation);
        try {
            newGkDivideClassService.saveAllSplit(unitId, divideId, splitSubjectId, splitDivideClassList, newGkClassStudentList, newGkChoRelationList);
        } catch (Exception e) {
            e.printStackTrace();
            return error("拆分失败");
        }
        return success("拆分完毕");
    }

    private NewGkDivideClass newGkDivideClassClone(NewGkDivideClass origin) {
	    NewGkDivideClass newGkDivideClass = new NewGkDivideClass();
	    newGkDivideClass.setId(UuidUtils.generateUuid());
	    newGkDivideClass.setParentId(origin.getId());
	    newGkDivideClass.setCreationTime(new Date());
	    newGkDivideClass.setModifyTime(new Date());
	    newGkDivideClass.setBatch(origin.getBatch());
	    newGkDivideClass.setSubjectType(origin.getSubjectType());
	    newGkDivideClass.setIsHand(origin.getIsHand());
	    newGkDivideClass.setSourceType(origin.getSourceType());
	    newGkDivideClass.setDivideId(origin.getDivideId());
	    newGkDivideClass.setClassType(origin.getClassType());
	    newGkDivideClass.setRelateId(origin.getRelateId());
	    newGkDivideClass.setStudentCount(origin.getStudentCount());
	    newGkDivideClass.setBestType(origin.getBestType());
	    newGkDivideClass.setOrderId(origin.getOrderId());
	    newGkDivideClass.setOldClassId(origin.getOldClassId());
	    newGkDivideClass.setStudentList(origin.getStudentList());
	    newGkDivideClass.setRelateName(origin.getRelateName());
	    newGkDivideClass.setBoyCount(origin.getBoyCount());
	    newGkDivideClass.setClassNum(origin.getClassNum());
	    newGkDivideClass.setGirlCount(origin.getGirlCount());
	    newGkDivideClass.setNotexists(origin.getNotexists());
	    newGkDivideClass.setOldDivideClassId(origin.getOldDivideClassId());
	    newGkDivideClass.setNewDtoList(origin.getNewDtoList());
	    newGkDivideClass.setPlaceIds(origin.getPlaceIds());
	    newGkDivideClass.setStuCountByClassId(origin.getStuCountByClassId());
	    newGkDivideClass.setPlaceName(origin.getPlaceName());
	    newGkDivideClass.setStuIdStr(origin.getStuIdStr());
	    return newGkDivideClass;
    }
    
    
}