package net.zdsoft.gkelective.data.action;

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

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.gkelective.data.action.optaplanner.convert.ArrangeDtoConverter;
import net.zdsoft.gkelective.data.action.optaplanner.domain.ArrangeCapacityRange;
import net.zdsoft.gkelective.data.action.optaplanner.domain.ArrangeClass;
import net.zdsoft.gkelective.data.action.optaplanner.domain.ArrangeStudent;
import net.zdsoft.gkelective.data.action.optaplanner.listener.SolverListener;
import net.zdsoft.gkelective.data.action.optaplanner.solver.ArrangeSingleSolver;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dto.GkConditionDto;
import net.zdsoft.gkelective.data.dto.Room;
import net.zdsoft.gkelective.data.dto.StudentSubjectDto;
import net.zdsoft.gkelective.data.entity.GkAllocation;
import net.zdsoft.gkelective.data.entity.GkBatch;
import net.zdsoft.gkelective.data.entity.GkGroupClass;
import net.zdsoft.gkelective.data.entity.GkGroupClassStu;
import net.zdsoft.gkelective.data.entity.GkRounds;
import net.zdsoft.gkelective.data.entity.GkSubject;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.entity.GkTeachClassEx;
import net.zdsoft.gkelective.data.entity.GkTeachClassStore;
import net.zdsoft.gkelective.data.entity.GkTeachClassStuStore;
import net.zdsoft.gkelective.data.service.GkAllocationService;
import net.zdsoft.gkelective.data.service.GkBatchService;
import net.zdsoft.gkelective.data.service.GkConditionService;
import net.zdsoft.gkelective.data.service.GkGroupClassService;
import net.zdsoft.gkelective.data.service.GkRelationshipService;
import net.zdsoft.gkelective.data.service.GkResultService;
import net.zdsoft.gkelective.data.service.GkRoundsService;
import net.zdsoft.gkelective.data.service.GkSubjectArrangeService;
import net.zdsoft.gkelective.data.service.GkSubjectService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 排班
 * @author zhouyz
 * 第一步：组合班先排 里面的学生都是组合里的（必须） 排完后，
 * 人工操作： 1.组合多余的可以选择塞入本组合的教室（导致教室人数增多） ； 
 * 2.或解散这些多余组合人员，使其变长单科，进入下一步单科排班
 *
 */
@Controller
@RequestMapping("/gkelective/{roundsId}")
public class GkPaibanAction extends BaseAction{
	@Autowired
	private GkRoundsService gkRoundsService;
	@Autowired
	private GkSubjectArrangeService gkSubjectArrangeService;
	@Autowired
	private GkSubjectService gkSubjectService;
	
	@Autowired
	private GkConditionService gkConditionService;
	@Autowired
	private GkResultService gkResultService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private GkRelationshipService gkRelationshipService;
	@Autowired
	private GkBatchService gkBatchService;
	@Autowired
	private GkGroupClassService gkGroupClassService;
	@Autowired
	private GkAllocationService gkAllocationService;
	
	// 性别
	private static final String ARRANGE_TYPE_XINGBIE = "sex";
	// 班级
	private static final String ARRANGE_TYPE_BANJI = "class";
	//平均分
	private static final String ARRANGE_TYPE_SCORE = "score";
	
	/**
	 * 返回算法排列顺序
	 * @param unitId
	 * @return
	 */
	private String[] findFrist(String arrangeId){
		List<GkAllocation> list = gkAllocationService.findByArrangeIdIsUsing(arrangeId);
		if(CollectionUtils.isNotEmpty(list)){
			List<String> types = EntityUtils.getList(list, "type");
			return types.toArray(new String[0]);
		}
		return null;
	}
	/**
	 * 该轮次下走班科目
	 * @param roundsId
	 * @return
	 */
	public String[] findAllSubject(String roundsId){
		List<GkSubject> list = gkSubjectService.findByRoundsId(roundsId,GkElectveConstants.USE_TRUE );
		if(CollectionUtils.isNotEmpty(list)){
			List<String> subjectIds = EntityUtils.getList(list, "subjectId");
			return subjectIds.toArray(new String[0]);
		}else{
			return null;
		}
	}
	
	/**
	 * subjectTeacherMap 科目对应教师  <subjectId,teacherIds>
	 * @param arrangeId
	 */
//    private Map<String,Set<String>> initSubjectIdTeachers(String roundsId) {
//        Map<String,Set<String>> subjectTeacherMap=new HashMap<String, Set<String>>();
//        List<GkSubject> list = gkSubjectService.findByRoundsId(roundsId,GkElectveConstants.USE_TRUE );
//		if(CollectionUtils.isNotEmpty(list)){
//			List<String> ids = EntityUtils.getList(list, "id");
//			Map<String, GkSubject> map = EntityUtils.getMap(list, "id");
//			//科目关联教师
//        	List<GkRelationship> relist = gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_01,ids.toArray(new String[]{}));
//        	if(CollectionUtils.isNotEmpty(relist)){
//         		for(GkRelationship ship:relist){
//         			GkSubject ex = map.get(ship.getPrimaryId());
//         			if(ex==null){
//         				continue;
//         			}
//         			if(!subjectTeacherMap.containsKey(ex.getSubjectId())){
//         				subjectTeacherMap.put(ex.getSubjectId(), new HashSet<String>());
//         			}
//         			subjectTeacherMap.get(ex.getSubjectId()).add(ship.getRelationshipTargetId());
//             	}
//         	}
//		}
//        return subjectTeacherMap;
//    }
    /**
     * 按成绩
     * @param temp
     */
    private void onlysortStuScore(List<StudentSubjectDto> temp){
    	if(CollectionUtils.isNotEmpty(temp)){
    		Collections.sort(temp, new Comparator<StudentSubjectDto>() {
     			@Override
     			public int compare(StudentSubjectDto o1, StudentSubjectDto o2) {
     				double cc = o2.getAvgScore()-o1.getAvgScore();
     				if(cc>0){
     					return 1;
     				}else if(cc<0){
     					return -1;
     				}else{
     					return o1.getClassId().compareTo(o2.getClassId());
     				}
     			}
    		});
    	}
    }
    
    /**
     * 按成绩 首尾首尾取值
     * @param temp
     */
//	private void sortStuScore(List<StudentSubjectDto> temp){
//		onlysortStuScore(temp);
//		if(CollectionUtils.isNotEmpty(temp)){
//			List<StudentSubjectDto> newTemp=new ArrayList<StudentSubjectDto>();
//			int size = temp.size();
//			for(int i=0;i<size;i++){
//				if(i<size-i-1){
//					newTemp.add(temp.get(i));
//					newTemp.add(temp.get(size-i-1));
//				}else if(i==size-i-1){
//					newTemp.add(temp.get(i));
//				}else{
//					break;
//				}
//			}
//			temp=newTemp;
//		}
//	}
	
	/**
	 * 求平均分
	 */
	public double avagTeachClass(Map<String,StudentSubjectDto> dtoMap,List<String> stuList,String subjectId){
		 double sum=0;
		 int length=stuList.size();
		 if(length==0){
			 return 0;
		 }
		 StudentSubjectDto dto=null;
		 Map<String, Double> scoreMap=null;
		 for(String stuId:stuList){
			 if(dtoMap.containsKey(stuId)){
				dto = dtoMap.get(stuId);
				scoreMap = dto.getScoreMap();
				if(scoreMap!=null && scoreMap.containsKey(subjectId)){
					sum=sum+scoreMap.get(subjectId);
				}
			 }
		 }
		 return sum/length;
	}
	
	/**----------------------------------组合开始---------------------------------------------**/
	/**
	 * 组合排班
	 * 1:先排预排班级
	 * 2:组合排班
	 */
	@ResponseBody
	@RequestMapping("/openClassArrange/groupResult/save")
	@ControllerInfo(value = "组合开班")
//	public String groupMain(@PathVariable String roundsId){
//		try{
//			GkRounds round = gkRoundsService.findRoundById(roundsId);
//			if(round==null){
//				return error("该选课系统对应这个轮次不存在");
//			}
//			GkSubjectArrange gkArrange=gkSubjectArrangeService.findArrangeById(round.getSubjectArrangeId());
//			if(gkArrange==null){
//				return error("该选课系统不存在");
//			}
//			String[] subject_all = findAllSubject(roundsId);
//			if(subject_all==null || subject_all.length<=0){
//				return error("该选课系统还没有设置走班科目");
//			}
//			String openClass = round.getOpenClass();
//			if(StringUtils.isBlank(openClass)){
//				return error("是否开学考班没有设置");
//			}
//			String[] sortType = findFrist(gkArrange.getId());
//			long start = System.currentTimeMillis();
//			if(GkElectveConstants.TRUE_STR.equals(openClass)){
//				//开AB
//				processCombineAB(subject_all,null,gkArrange,round,sortType);
//			}else{
//				// subjectNum是否需要判断（只排A 需要判断）
//				int subjectNum = gkArrange.getSubjectNum()==null?0:gkArrange.getSubjectNum();
//				if(subjectNum==0){
//					return error("该选课系统选课数为0,不符合排班要求，请修改");
//				}
//				//只开选考DOTO
//				processCombineAB(subject_all,subjectNum,gkArrange,round,sortType);
//			}
//			long end = System.currentTimeMillis();
//			System.out.println("组合排班总耗时：" + (end-start)/1000 + "s");
//		}catch (Exception e) {
//			e.printStackTrace();
//			return error("保存失败！"+e.getMessage());
//		}
//        return success("组合开班成功");
//	}
	
	/**
	 * 获取所有组合学生(需要组合开班的学生)
	 * @param subject_all 走班课程
	 * @param arrangeId
	 * @param studentIds 已安排学生
	 * @param sortType 排序
	 * @param combineds//用于学生循环判断所选组合
	 * @param combineSubjectCountMap //用于学生排序，根据性别均衡要求，学生排序（男女交叉排序）根据组合内比例与组合班级人数限制
	 * @param combinedMap//只用于取组合名
	 * @return
	 * * 
	 */
//    private List<StudentSubjectDto> initCombineStudents(String[] subject_all, String arrangeId,Set<String> studentIds,
//    		String[] sortType,List<Combined> combineds,Map<String, Integer> combineSubjectCountMap,Map<String, Combined> combinedMap,
//    		List<StudentSubjectDto> allstudents) {
//    	//学生选课结果
//    	List<StudentSubjectDto> returnList=new ArrayList<StudentSubjectDto>();
//        if(CollectionUtils.isNotEmpty(allstudents)){
//         	for(StudentSubjectDto dto:allstudents){
//         		if(studentIds.contains(dto.getStuId())){
//         			continue;
//         		}
//         		dto.setAllSubjectIds(Arrays.asList(subject_all));
//	     		 for (Combined combined : combineds) {
//	     			 //组合中是没有null 但是学生这边存放的选的科目不放null
//	                  if (CollectionUtils.intersection(dto.getChooseSubjectIds(), combined.getSubjectIds()).size() ==
//	                          CollectionUtils.union(dto.getChooseSubjectIds(), combined.getSubjectIds()).size()) {
//	                 	 dto.setCombined(combined);
//	                 	 //求3门平均分
//	                 	 Map<String, Double> scoreMap = dto.getScoreMap();
//	                 	 if(scoreMap==null || scoreMap.size()<=0){
//	                 		 dto.setAvgScore(0);
//	                 	 }else{
//	                 		double sum=0;
//		             		 for(String key:scoreMap.keySet()){
//		             			Double dd = scoreMap.get(key);
//		             			if(dd!=null){
//		             				sum=sum+dd;
//		             			}
//		             		 }
//		             		dto.setAvgScore(sum/combined.getSubjectIds().size());
//	                 	 }
//	                 	 returnList.add(dto);
//	                 	 break;
//	                  }
//	              }
//         	}
//         }
//         Collections.sort(returnList, new Comparator<StudentSubjectDto>() {
// 			@Override
// 			public int compare(StudentSubjectDto o1, StudentSubjectDto o2) {
// 				if (o1.getCombined() != null && o2.getCombined() != null) {
// 					if (StringUtils.equals(o1.getCombined().getId(), o2.getCombined().getId())) {
// 						return o1.getClassId().compareTo(o2.getClassId());
// 					} else {
// 						return o1.getCombined().getId().compareTo(o2.getCombined().getId());
// 					}
// 				} else if (o1.getCombined() != null) {
// 					return -1;
// 				} else if (o2.getCombined() != null) {
// 					return 1;
// 				}
// 				return 0;
// 			}
// 		});
//         
//        if(sortType!=null && sortType.length>0){
//        	if(ARRANGE_TYPE_XINGBIE.equals(sortType[0])){
//        		//返回值(男女间隔)
//        		List<StudentSubjectDto> returnList2 = new ArrayList<StudentSubjectDto>();
//        		// 性别
//            	Map<String, List<StudentSubjectDto>[]> combinedGenderMap = new HashMap<String, List<StudentSubjectDto>[]>();
//            	List<StudentSubjectDto>[] studentsTempArray = null;
//    			List<StudentSubjectDto> studentsMaleTemp;//男
//    			List<StudentSubjectDto> studentsFeMaleTemp;//女
//    			for (StudentSubjectDto student2 : returnList) {
//    				if (combinedGenderMap.containsKey(student2.getCombined().getId())) {
//    					studentsTempArray = combinedGenderMap.get(student2.getCombined().getId());
//    					studentsMaleTemp = studentsTempArray[0];
//    					studentsFeMaleTemp = studentsTempArray[1];
//    				} else {
//    					studentsTempArray = new List[2];
//    					studentsMaleTemp = new ArrayList<StudentSubjectDto>();
//    					studentsFeMaleTemp = new ArrayList<StudentSubjectDto>();
//    					studentsTempArray[0] = studentsMaleTemp;
//    					studentsTempArray[1] = studentsFeMaleTemp;
//    				}
//    				if (student2.getSex() == GkElectveConstants.MALE) {
//    					// 男
//    					studentsMaleTemp.add(student2);
//    				} else {
//    					studentsFeMaleTemp.add(student2);
//    				}
//    				combinedGenderMap.put(student2.getCombined().getId(), studentsTempArray);
//    			}	
//    			boolean isScore=false;//是否按成绩
//    			
//        		if(sortType.length>1){
//        			//按性别
//        			if(ARRANGE_TYPE_SCORE.equals(sortType[1])){
//        				isScore=true;
//                	}else if(ARRANGE_TYPE_BANJI.equals(sortType[1])){
//                		//按班级---略
//                	}
//        		}
//        		//按性别
//    			String combinedId = null;
//    			int roomLimit = 0;//组合班级人数限制
//    			int femaleCount = 0;
//    			int maleCount = 0;
//    			int maleStartIndex = 0;
//    			int femaleStartIndex = 0;
//    			boolean maleNoLeft = false;
//    			boolean femaleNoLeft = false;
//    			Combined combined=null; 
//    			for (Map.Entry<String, List<StudentSubjectDto>[]> entry : combinedGenderMap.entrySet()) {
//    				combinedId = entry.getKey();
//    				studentsTempArray = entry.getValue();
//    				combined = combinedMap.get(combinedId);
//    				if (!combineSubjectCountMap.containsKey(combinedId)) {
//    		           throw new RuntimeException("没有设置组合班数量 ,组合：" + combined.getCombinedName()==null?"":combined.getCombinedName());
//    		        }
//    		        //每个组合班设置成多少人
//    				roomLimit = combineSubjectCountMap.get(combinedId);
//    	            if (roomLimit==0) {
//    	               throw new RuntimeException("组合班数量为0,组合：" + combined.getCombinedName()==null?"":combined.getCombinedName());
//    	            }
//    				studentsMaleTemp = studentsTempArray[0];//组合内男生人数
//    				studentsFeMaleTemp = studentsTempArray[1];//组合内女人数
//    				
//    				if(isScore){
//    					sortStuScore(studentsMaleTemp);
//    					sortStuScore(studentsFeMaleTemp);
//    				}
//    				//组合排班 除啦最后一个班级外其他可能满员
//    				femaleCount = roomLimit * studentsFeMaleTemp.size() / (studentsMaleTemp.size() + studentsFeMaleTemp.size());//满员班级女生人数
//    				maleCount = roomLimit - femaleCount;//满员班级男生人数
//    				//比例算出几:0
//    				if(femaleCount<=0){
//    					femaleCount=1;
//    					maleCount=maleCount-1;
//    				}
//    				if(maleCount<=0){
//    					maleCount=1;
//    				}
//    				maleStartIndex = 0;
//    				femaleStartIndex = 0;
//    				maleNoLeft = false;
//    				femaleNoLeft = false;
//    				while (true) {
//    					if (maleStartIndex < studentsMaleTemp.size()) {
//    						if (maleStartIndex + maleCount < studentsMaleTemp.size()) {
//    							returnList2.addAll(subList(studentsMaleTemp, maleStartIndex, maleStartIndex + maleCount));
//    						} else {
//    							returnList2.addAll(subList(studentsMaleTemp, maleStartIndex));
//    							maleNoLeft = true;
//    						}
//    						maleStartIndex += maleCount;
//    					} else {
//    						maleNoLeft = true;
//    					}
//    					if (femaleStartIndex < studentsFeMaleTemp.size()) {
//    						if (femaleStartIndex + femaleCount < studentsFeMaleTemp.size()) {
//    							returnList2.addAll(subList(studentsFeMaleTemp, femaleStartIndex, femaleStartIndex + femaleCount));
//    						} else {
//    							returnList2.addAll(subList(studentsFeMaleTemp, femaleStartIndex));
//    							femaleNoLeft = true;
//    						}
//    						femaleStartIndex += femaleCount;
//    					} else {
//    						femaleNoLeft = true;
//    					}
//    					if (maleNoLeft && femaleNoLeft) {
//    						break;
//    					}
//    				}
//    			}
//        		return returnList2;
//        	}else if(ARRANGE_TYPE_SCORE.equals(sortType[0])){
//        		//如果下面有性别排序则先不管
//        		//按成绩
//        		sortStuScore(returnList);
//        		return returnList;
//        	}else if(ARRANGE_TYPE_BANJI.equals(sortType[0])){
//        		//如果下面有性别,成绩排序则先不管
//        		//按班级---上面排序已经排啦
//        		
//        	}
//        	return returnList;
//        }
//        return returnList;
//    }
	
	/**
	 * 由，隔开的字符串转化成set
	 * @param subjectIds 以，隔开
	 * @return
	 */
//	private Set<String> makeStr(String subjectIds){
//		String[] subjectArr = subjectIds.split(",");
//		Set<String> set=new HashSet<String>();
//		for(String s:subjectArr){
//			if(StringUtils.isNotBlank(s)){
//				set.add(s);
//			}
//		}
//		return set;
//	}

	
	/**
     * 给AB都走班组合班级科目排序（排批次）
     * @param subject_all 从小到大的顺序
     * 1,2,3,4,5,6,7  7,1,2,3,4,5，6
     * @param subjectNum
     * @param subjectClassCountMap
     * @return
     */
//    private List<String> makeGroupToSortSubjectA(String[] subject_all,int subjectNum,
//    		Map<String, Integer[]> subjectClassCountMap){
//    	//开AB班  subject_all列表值一样
//    	if(subjectClassCountMap.containsKey(subject_all[0])){
//    		Integer[] classCountArray = subjectClassCountMap.get(subject_all[0]);
//        	//最大位置
//    		String[] reMixedSubjectIds = new String[subjectNum];
//    		int maxIndex=0;
//    		for(int i=1;i<classCountArray.length;i++){
//    			if(classCountArray[i]>=classCountArray[maxIndex]){
//    				maxIndex=i;
//    			}
//    		}
//    		int j=0;
//    		for(int k=maxIndex+1;k<subjectNum;k++){
//				reMixedSubjectIds[k]=subject_all[j];
//				j++;
//			}
//    		for(int k=0;k<=maxIndex;k++){
//    			reMixedSubjectIds[k]=subject_all[j];
//    			j++;
//    		}
//    		return Arrays.asList(reMixedSubjectIds);
//    	}else{
//    		return Arrays.asList(subject_all);
//    	}
//		
//    }
    
    
    /**
     * 给组合班级科目排序（排批次）
     * @param subject_all
     * @param subjectNum
     * @param subjectClassCountMap
     * @return
     */
//    private List<String> makeGroupToSortSubject(String[] subject_all,int subjectNum,
//    		Map<String, Integer[]> subjectClassCountMap){
//    	String[] reMixedSubjectIds = new String[subjectNum];
//		List<Integer> usedIndexs = new ArrayList<Integer>();
//		List<String> unUsedSubjectIds = new ArrayList<String>();
//		Integer[] classCountArray;
//		int minIndex=0;
//		for (String subjectId : subject_all) {
//			if (subjectClassCountMap.containsKey(subjectId)) {
//				classCountArray = subjectClassCountMap.get(subjectId);
//				minIndex = smallestIndex(ArrayUtils.toPrimitive(classCountArray), usedIndexs);
//				reMixedSubjectIds[minIndex] = subjectId;
//				usedIndexs.add(minIndex);
//			} else {
//				unUsedSubjectIds.add(subjectId);
//			}
//		}
//		if (usedIndexs.size() != subject_all.length) {
//			Iterator<String> iterator = unUsedSubjectIds.iterator();
//			for (int i = 0; i < subjectNum; i++) {
//				if (!usedIndexs.contains(i) && iterator.hasNext()) {
//					reMixedSubjectIds[i] = iterator.next();
//					usedIndexs.add(i);
//				}
//			}
//		}
//		return Arrays.asList(reMixedSubjectIds);
//		
//    }
    /**
     * 初始化教学班
     * @param gkArrange
     * @param round
     * @param subjectId
     * @return
     */
    private GkTeachClassStore makeNewTeachClass(GkSubjectArrange gkArrange,GkRounds round,String subjectId){
    	//教学班
    	GkTeachClassStore teachClass = new GkTeachClassStore();
    	teachClass.setRoundsId(round.getId());
//		teachClass.setAcadyear(round.getAcadyear());
//		teachClass.setSemester(round.getSemester());
		teachClass.setId(UuidUtils.generateUuid());
		teachClass.setSubjectId(subjectId);
		teachClass.setUnitId(gkArrange.getUnitId());
//		teachClass.setIsDeleted(GkElectveConstants.USE_FALSE);
		//默认7选3
//    	teachClass.setClassType(TeachClass.CLASS_TYPE_SEVEN);
    	teachClass.setGradeId(gkArrange.getGradeId());
    	teachClass.setCreationTime(new Date());
    	teachClass.setModifyTime(new Date());
//    	teachClass.setIsUsing(GkElectveConstants.TRUE_STR);
    	return teachClass;
    }
    /**
     * 初始化批次
     * @param round
     * @param groupClassId
     * @return
     */
    private GkBatch makeNewGkBatch(GkRounds round,String groupClassId){
    	//----------批次
    	GkBatch bath = new GkBatch();
    	bath.setCreationTime(new Date());
    	bath.setModifyTime(new Date());
    	bath.setId(UuidUtils.generateUuid());
    	bath.setRoundsId(round.getId());
    	bath.setGroupClassId(groupClassId);
    	return bath;
    }
	/**
     * 获取组合各种值
     * @param combineds 组合列表
     * @param combinedMap 组合map(key:id)
     * @param combineSubjectCountMap 组合班级每班人数限制
     */
//	private void initCombined(String roundIds,List<Combined> combineds,Map<String, Combined> combinedMap,
//			Map<String, Integer> combineSubjectCountMap) {
//		//组合排班
//		Combined combined=null;
//		List<String> subjectIdsList=null;
//		List<GkConditionDto> gkConditionDtoList = gkConditionService.findByGkConditionDtos(roundIds, GkElectveConstants.GKCONDITION_GROUP_1);
//		if(CollectionUtils.isNotEmpty(gkConditionDtoList)){
//			for(GkConditionDto dto:gkConditionDtoList){
//				if(dto.getSubjectIds()==null ||dto.getSubjectIds().size()<=0){
//					//以防gkConditionDtoList取到的数据不属于组合数据
//					continue;
//				}
//				combined=new Combined();
//				combined.setId(dto.getId());
//				subjectIdsList = new ArrayList<String>(dto.getSubjectIds());
//				//对subjectIdsList排序
//				Collections.sort(subjectIdsList);
//				//当只有两门走班的时候 将第三门设置为null
//				//addNull(subjectIdsList,subjectNum);
//				combined.setSubjectIds(subjectIdsList);
//				combined.setCombinedName(dto.getConditionName());
//				combineds.add(combined);
//				combinedMap.put(combined.getId(), combined);
//				combineSubjectCountMap.put(combined.getId(), dto.getNum());
//			}
//		}
//		
//	}
	
	 /**
     * 处理组合 AB  教学班老师默认32个0 先不插入课程表
     * subject_all为空 subjectNum>0//只开A
     * subject_all不为空 subjectNum null//开AB
     */
//    private void processCombineAB(String[] subject_all,Integer subjectNum,GkSubjectArrange gkArrange,GkRounds round,String[] sortType) {
//    	boolean isAB=true;
//    	if(subjectNum!=null){
//    		isAB=false;
//    	}else{
//    		subjectNum=subject_all.length;
//    	}
//    	List<GkBatch> insertBath=new ArrayList<GkBatch>();
//    	List<TeachClass> insertTeachClass=new ArrayList<TeachClass>();
//    	List<TeachClassStu> insertTeachClassStu=new ArrayList<TeachClassStu>();
//    	List<GkTeachClassEx> insertAvg=new ArrayList<GkTeachClassEx>();
//    	List<GkGroupClass> insertGroupClass=new ArrayList<GkGroupClass>();
//    	List<GkGroupClassStu> insertGroupClassStu=new ArrayList<GkGroupClassStu>();
//    	GkGroupClass gkGroupClass=null;
//    	GkGroupClassStu gkGroupClassStu=null;
//    	GkBatch bath=null;
//    	GkTeachClassEx avgEx=null;
//    	TeachClass teachClass=null;
//    	TeachClassStu teachClassStu=null;
//    	List<TeachClassStu> stuList=null;
//    	//所有高中7选3科目
//    	List<Course> courseList = SUtils.dt(courseRemoteService.findByBaseCourseCodes(GkElectveConstants.SUBJECT_TYPES), new TR<List<Course>>() {});
//    	Map<String, Course> subjectNameMap = new HashMap<String,Course>();
//    	if(CollectionUtils.isNotEmpty(courseList)){
//    		subjectNameMap=EntityUtils.getMap(courseList, "id");
//        }
//    	
//    	
//    	//所有学生
//    	List<StudentSubjectDto> allstudents=gkResultService.findAllStudentSubjectDto(gkArrange.getId(),null);
//    	if(CollectionUtils.isEmpty(allstudents)){
//    		throw new RuntimeException("没有学生选课数据！");
//    	}
//    	Map<String,StudentSubjectDto> alldtoMap=EntityUtils.getMap(allstudents, "stuId");
//    	
//    	//已排批次，教师
////    	Map<Integer,Set<String>> bathTeacherMap=new HashMap<Integer, Set<String>>();
//    	//根据上课时间 取课程表对应批次已排的学生
//    	
//    	
//    	
//    	//已排学生
//    	Set<String> studentIds=new HashSet<String>();
//    	//预设是科目对应教师  <subjectId,teacherIds>用于随机分配老师到各个教学班
////        Map<String,Set<String>> subjectTeacherMap=initSubjectIdTeachers(round.getId());
//        //场地先不排
//    	//预排班级
//    	List<GkGroupDto> dto = gkGroupClassService.findGkGroupDtoByRoundsId(round.getId(), null);
//    	//预排组合班级名称
//    	Set<String> groupNames=new HashSet<String>();
//    	List<String> allSubjectIds=null;//最终组合班级科目顺序
//    	//已排科目每个批次的班级数
//		Map<String, Integer[]> subjectClassCountMap = new HashMap<String, Integer[]>();
//		Integer[] classCountArray;
//		Set<String> subject_all_set=new HashSet<String>();
//		for(String a:subject_all){
//			subject_all_set.add(a);
//		}
//		/***---------------------预排班级开始-------------------------***/
//    	if(CollectionUtils.isNotEmpty(dto)){
//    		//预排班级 科目 批次 老师 场地
//    		List<GkGroupClass> groupList=null;
//    		Set<String> chooseSubject=null;
//    		for(GkGroupDto tt:dto){
//    			groupList = tt.getGkGroupClassList();
//    			//组合科目
//    			String subjectIds = tt.getSubjectIds();
//    			chooseSubject = makeStr(subjectIds);
//    			if(CollectionUtils.isNotEmpty(groupList)){
//    				for(GkGroupClass g:groupList){
//    					if(CollectionUtils.isEmpty(g.getStuIdList())){
//    						continue;
//    					}
//    					groupNames.add(g.getGroupName());
//    					if(isAB){
//    						allSubjectIds = makeGroupToSortSubjectA(subject_all,subjectNum,subjectClassCountMap);
//    					}else{
//    						if(chooseSubject==null || chooseSubject.size()<=0){
//    							continue;
//    						}
//    						//去除不走班
//    						List<String> canArr=new ArrayList<String>();
//    						for(String choose:chooseSubject){
//    							if(subject_all_set.contains(choose)){
//    								canArr.add(choose);
//    							}
//    						}
//    						if(canArr.size()<=0){
//    							continue;
//    						}
//    						addNull(canArr, subjectNum);
//    						allSubjectIds = makeGroupToSortSubject(canArr.toArray(new String[0]),subjectNum,subjectClassCountMap);
//    					}
//    					int i=0;
//    					for(String suu:allSubjectIds){
//    						if(suu==null){
//    							i++;
//    							continue;
//    						}
//    						//教学班
//    						teachClass=makeNewTeachClass(gkArrange,round,suu);
//    						//批次
//    						bath=makeNewGkBatch(round,g.getId());
//    						bath.setBatch(i+1);
//    		            	bath.setTeachClassId(teachClass.getId());
//    		            	teachClass.setTeacherId(BaseConstants.ZERO_GUID);
//    		            	//随机分配教学班教师
////    		            	if(subjectTeacherMap.containsKey(suu)){
////    		            		Set<String> teacherIdSet = subjectTeacherMap.get(suu);
////    		            		String teacherId=null;
////    		            		for(String tId:teacherIdSet){
////    		            			if(bathTeacherMap.containsKey(bath.getBatch())){
////    		        					//批次老师
////    		        					Set<String> batchTeaIds = bathTeacherMap.get(bath.getBatch());
////    		        					if(batchTeaIds==null || !batchTeaIds.contains(tId)){
////    		        						teacherId=tId;
////    		        						break;
////    		        					}
////    		        				}else{
////    		        					bathTeacherMap.put(bath.getBatch(), new HashSet<String>());
////    		        					teacherId=tId;
////    		        					break;
////    		        				}
////    		            		}
////    		        			if(teacherId!=null){
////    		        				teachClass.setTeacherId(teacherId);
////    		        				bathTeacherMap.get(bath.getBatch()).add(teacherId);
////    		        			}
////    		            	}
//    		            	if(chooseSubject.contains(suu)){
//    		            		bath.setClassType(BaseConstants.GKELECTIVE_GKTYPE_A);
//    		            		teachClass.setName(g.getGroupName()+(subjectNameMap.get(suu).getSubjectName())+BaseConstants.GKELECTIVE_GKTYPE_A);
//    		            	}else{
//    		            		bath.setClassType(BaseConstants.GKELECTIVE_GKTYPE_B);
//    		            		teachClass.setName(g.getGroupName()+(subjectNameMap.get(suu).getSubjectName())+BaseConstants.GKELECTIVE_GKTYPE_B);
//    		            	}
//    		            	//教学班对应学生
//    		            	List<String> stuIds = g.getStuIdList();
//    		            	double avg=0;
//    		            	avgEx=new GkTeachClassEx();
//    		            	if(CollectionUtils.isNotEmpty(stuIds)){
//    		            		stuList=makeStuToClassTeach(stuIds,teachClass.getId());
//    		            		//求平均分
//    		            		avg=avagTeachClass(alldtoMap, stuIds, teachClass.getCourseId());
//    		            	}
//    		            	avgEx.setAverageScore(avg);
//    		            	avgEx.setTeachClassId(teachClass.getId());
//    		            	avgEx.setRoundsId(round.getId());
//    		            	avgEx.setId(UuidUtils.generateUuid());
//    		            	insertAvg.add(avgEx);
//    		            	insertBath.add(bath);
//    		            	insertTeachClass.add(teachClass);
//    		            	if(CollectionUtils.isNotEmpty(stuList)){
//    		            		insertTeachClassStu.addAll(stuList);
//    		            	}
//    		            	
//    		       
//    		            	// 计算科目班级数
//    						if (subjectClassCountMap.containsKey(suu)) {
//    							classCountArray = subjectClassCountMap.get(suu);
//    						} else {
//    							classCountArray = ArrayUtils.toObject(new int[subjectNum]);
//    						}
//    						classCountArray[i] = classCountArray[i] + 1;
//    						subjectClassCountMap.put(suu, classCountArray);
//    						i++;
//    					}
//    					if(CollectionUtils.isNotEmpty(g.getStuIdList())){
//    						studentIds.addAll(g.getStuIdList());
//		            	}
//    				}
//    			}
//    		}
//    	}
//    	/***---------------------预排班级结束-------------------------***/
//    	
//    	/*--------------------组合信息--------------------------*/
//    	List<Combined> combineds=new ArrayList<Combined>();//组合班（其中subjectIds 根据subjectId排序）
//        Map<String, Combined> combinedMap = new HashMap<String, Combined>();// key:combinedId
//    	Map<String, Integer> combineSubjectCountMap = new HashMap<String, Integer>();//组合班级人数限制
//        initCombined(round.getId(),combineds,combinedMap,combineSubjectCountMap);
//        
//        /*---------------------组合学生信息------------------------*/
//        List<StudentSubjectDto> students=new ArrayList<StudentSubjectDto>();
//        try{
//        	students = initCombineStudents(subject_all, gkArrange.getId(), studentIds, sortType, 
//        			combineds, combineSubjectCountMap, combinedMap,allstudents);
//        }catch (Exception e) {
//			e.printStackTrace();
//		}
//        //key:combinedId 组合内的学生
//        Map<String, List<StudentSubjectDto>> combinedStudentListMap = new HashMap<String, List<StudentSubjectDto>>();
//        List<StudentSubjectDto> studentList = null;
//        String combinedId = null;
//        for (StudentSubjectDto student : students) {
//            if (student.getCombined() != null) {
//                combinedId = student.getCombined().getId();
//                if (combinedStudentListMap.containsKey(combinedId)) {
//                	studentList =  combinedStudentListMap.get(combinedId);
//                } else {
//                	studentList = new ArrayList<StudentSubjectDto>();
//                }
//                studentList.add(student);
//                combinedStudentListMap.put(student.getCombined().getId(), studentList);
//            } else {
//                throw new RuntimeException("学生不是组合班学生：" + student.getStuName()==null?"":student.getStuName());
//            }
//        }
//        //每个组合班设置成多少人
//        int combinedPerClass = 0;
//        Combined combined = null;
//        // 每个组合共有多少人
//        int combinedCount = 0;
//        int indexStart = 0;
//        List<String> subj=null;
//        for (Map.Entry<String, List<StudentSubjectDto>> entry : combinedStudentListMap.entrySet()) {
//        	combinedId = entry.getKey();
//        	studentList = entry.getValue();
//        	combined = combinedMap.get(combinedId);
//        	allSubjectIds = Arrays.asList(subject_all);
//        	if (!combineSubjectCountMap.containsKey(combinedId)) {
//                throw new RuntimeException("没有设置组合班数量 ,组合：" + combined.getCombinedName()==null?"":combined.getCombinedName());
//            }
//            //每个组合班设置成多少人
//            combinedPerClass = combineSubjectCountMap.get(combinedId);
//            if (combinedPerClass==0) {
//            	throw new RuntimeException("组合班数量为0,组合：" + combined.getCombinedName()==null?"":combined.getCombinedName());
//            }
//            // 每个组合共有多少人
//            combinedCount = studentList.size();
//            if (combinedCount < 1) {
//                continue;
//            }
//            indexStart = 0;
//            boolean flag=false;
//            subj = combined.getSubjectIds();
//            String subjectIdsStr=makeListToStr(subj);
//            String shortName=makeListToStrName(subj,subjectNameMap);
//            int k=1;//用于组合班级名称
//            while(true){
//            	List<StudentSubjectDto> dtoStuList=new ArrayList<StudentSubjectDto>();
//            	if (indexStart < combinedCount) {
//					if (indexStart + combinedPerClass < combinedCount) {
//						dtoStuList.addAll(subList(studentList, indexStart, indexStart + combinedPerClass));
//					} else {
//						dtoStuList.addAll(subList(studentList, indexStart));
//						flag=true;
//					}
//					indexStart += combinedPerClass;
//				} else {
//					break;
//				}
//            	if(CollectionUtils.isNotEmpty(dtoStuList)){
//            		//新增预排班
//                	gkGroupClass=new GkGroupClass();
//                	gkGroupClass.setId(UuidUtils.generateUuid());
//                	while(true){
//                		if(!groupNames.contains(shortName+k+"班")){
//                			gkGroupClass.setGroupName(shortName+k+"班");
//                    		break;
//                    	}
//                		k++;
//                	}
//                	gkGroupClass.setGroupType(GkElectveConstants.USER_AUTO);
//                	gkGroupClass.setRoundsId(round.getId());
//                	gkGroupClass.setSubjectIds(subjectIdsStr);
//                	insertGroupClass.add(gkGroupClass);
//                	for(StudentSubjectDto ddd:dtoStuList){
//                		gkGroupClassStu = new GkGroupClassStu();
//                		gkGroupClassStu.setId(UuidUtils.generateUuid());
//                		gkGroupClassStu.setStudentId(ddd.getStuId());
//                		gkGroupClassStu.setGroupClassId(gkGroupClass.getId());
//                		insertGroupClassStu.add(gkGroupClassStu);
//                	}
//                	groupNames.add(gkGroupClass.getGroupName());
//                	
//                	//组装班级
//                	if(isAB){
//						allSubjectIds = makeGroupToSortSubjectA(subject_all,subjectNum,subjectClassCountMap);
//					}else{
//						if(CollectionUtils.isEmpty(subj)){
//							continue;
//						}
//						//去除不走班
//						List<String> canArr=new ArrayList<String>();
//						for(String choose:subj){
//							if(subject_all_set.contains(choose)){
//								canArr.add(choose);
//							}
//						}
//						if(canArr.size()<=0){
//							continue;
//						}
//						addNull(canArr, subjectNum);
//						allSubjectIds = makeGroupToSortSubject(canArr.toArray(new String[0]),subjectNum,subjectClassCountMap);
//					}
//					int i=0;
//					for(String suu:allSubjectIds){
//						if(suu==null){
//							i++;
//							continue;
//						}
//						//教学班
//						teachClass=makeNewTeachClass(gkArrange,round,suu);
//						//批次
//						bath=makeNewGkBatch(round,gkGroupClass.getId());
//						bath.setBatch(i+1);
//		            	bath.setTeachClassId(teachClass.getId());
//		            	teachClass.setTeacherId(BaseConstants.ZERO_GUID);
//		            	//随机分配教学班教师
////		            	if(subjectTeacherMap.containsKey(suu)){
////		            		Set<String> teacherIdSet = subjectTeacherMap.get(suu);
////		            		String teacherId=null;
////		            		for(String tId:teacherIdSet){
////		            			if(bathTeacherMap.containsKey(bath.getBatch())){
////		        					//批次老师
////		        					Set<String> batchTeaIds = bathTeacherMap.get(bath.getBatch());
////		        					if(batchTeaIds==null || !batchTeaIds.contains(tId)){
////		        						teacherId=tId;
////		        						break;
////		        					}
////		        				}else{
////		        					bathTeacherMap.put(bath.getBatch(), new HashSet<String>());
////		        					teacherId=tId;
////		        					break;
////		        				}
////		            		}
////		        			if(teacherId!=null){
////		        				teachClass.setTeacherId(teacherId);
////		        				bathTeacherMap.get(bath.getBatch()).add(teacherId);
////		        			}
////		            	}
//		            	if(subj.contains(suu)){
//		            		bath.setClassType(BaseConstants.GKELECTIVE_GKTYPE_A);
//		            		teachClass.setName(gkGroupClass.getGroupName()+(subjectNameMap.get(suu).getSubjectName())+BaseConstants.GKELECTIVE_GKTYPE_A);
//		            	}else{
//		            		bath.setClassType(BaseConstants.GKELECTIVE_GKTYPE_B);
//		            		teachClass.setName(gkGroupClass.getGroupName()+(subjectNameMap.get(suu).getSubjectName())+BaseConstants.GKELECTIVE_GKTYPE_B);
//		            	}
//		            	//教学班对应学生
//		            	List<String> stuIds = new ArrayList<String>();
//		            	for(StudentSubjectDto ddd:dtoStuList){
//		            		stuIds.add(ddd.getStuId());
//		            		teachClassStu=new TeachClassStu();
//		            		teachClassStu.setClassId(teachClass.getId());
//		            		teachClassStu.setId(UuidUtils.generateUuid());
//		            		teachClassStu.setCreationTime(new Date());
//		            		teachClassStu.setModifyTime(new Date());
//		            		teachClassStu.setIsDeleted(GkElectveConstants.USE_FALSE);
//		            		teachClassStu.setStudentId(ddd.getStuId());
//		            		insertTeachClassStu.add(teachClassStu);
//	                	}
//		            	double avg=0;
//		            	avgEx=new GkTeachClassEx();
//		            	if(CollectionUtils.isNotEmpty(stuIds)){
//		            		//求平均分
//		            		avg=avagTeachClass(alldtoMap, stuIds, teachClass.getCourseId());
//		            	}
//		            	avgEx.setAverageScore(avg);
//		            	avgEx.setTeachClassId(teachClass.getId());
//		            	avgEx.setRoundsId(round.getId());
//		            	avgEx.setId(UuidUtils.generateUuid());
//		            	insertAvg.add(avgEx);
//		            	
//		            	
//		            	insertBath.add(bath);
//		            	insertTeachClass.add(teachClass);
//		            	
//		            	// 计算科目班级数
//						if (subjectClassCountMap.containsKey(suu)) {
//							classCountArray = subjectClassCountMap.get(suu);
//						} else {
//							classCountArray = ArrayUtils.toObject(new int[subjectNum]);
//						}
//						classCountArray[i] = classCountArray[i] + 1;
//						subjectClassCountMap.put(suu, classCountArray);
//						i++;
//					}
//            	}
//            
//            	if(flag){
//            		break;
//            	}
//            }
//        }
//        long start = System.currentTimeMillis();
//        //新增到数据库
//        gkBatchService.saveBatchs(round,gkArrange,insertGroupClassStu, insertGroupClass, insertBath, insertTeachClass, insertTeachClassStu,insertAvg,true);
//        long end = System.currentTimeMillis();
//        System.out.println("新增到数据库耗时：" + (end-start)/1000 + "s");
//    }
	
	/**
	 * 组装组合班名称
	 * @param subj
	 * @param subjectNameMap
	 * @return
	 */
	private String makeListToStrName(List<String> subj,
			Map<String, Course> subjectNameMap) {
		Collections.sort(subj);
		String returnStr="";
		for(String s:subj){
			Course c = subjectNameMap.get(s);
			if(c!=null){
				returnStr=returnStr+c.getShortName();
			}
		}
		return returnStr;
	}

	/**
	 * 将集合转换成字符串以，隔开
	 * @param subj
	 * @return
	 */
//	private String makeListToStr(List<String> subj) {
//		Collections.sort(subj);
//		String returnStr="";
//		for(String s:subj){
//			returnStr=returnStr+","+s;
//		}
//		return returnStr.substring(1);
//	}

	/**
	 * 初始化教学班下学生
	 * @param stuIds
	 * @param teachClassId
	 * @return
	 */
//	private List<TeachClassStu> makeStuToClassTeach(List<String> stuIds, String teachClassId) {
//		TeachClassStu s=null;
//		List<TeachClassStu> returnList=new ArrayList<TeachClassStu>();
//		for(String i:stuIds){
//			s=new TeachClassStu();
//			s.setClassId(teachClassId);
//			s.setId(UuidUtils.generateUuid());
//			s.setCreationTime(new Date());
//			s.setModifyTime(new Date());
//			s.setIsDeleted(GkElectveConstants.USE_FALSE);
//			s.setStudentId(i);
//			returnList.add(s);
//		}
//		return returnList;
//	}
	
	
	/***********---------------------------单科开班开始-------------------------------***********/
	
	
	@ResponseBody
    @RequestMapping("/openClassArrange/singleResult/cut")
	@ControllerInfo(value = "取消单科开班")
    public String cutSingleArrange(@PathVariable String roundsId) {
    	try{
			GkRounds round = gkRoundsService.findRoundById(roundsId);
			if(round==null){
				return error("该选课系统对应这个轮次不存在");
			}
			GkSubjectArrange gkArrange=gkSubjectArrangeService.findArrangeById(round.getSubjectArrangeId());
			if(gkArrange==null){
				return error("该选课系统不存在");
			}
			if(!(isNowArrange(roundsId)) && round.getStep()>=GkElectveConstants.STEP_5){
				//直接下一步
	    		return success("no");
		    }
			ArrangeSingleSolver.stopSolver(roundsId+"A");
			ArrangeSingleSolver.stopSolver(roundsId+"B");
			RedisUtils.del(new String[]{round.getId()+"A_ok",round.getId()+"B_ok"});
			//删除已经保存到数据的数据
			gkBatchService.deleteByRoundsIdAndType(roundsId,GkElectveConstants.GKCONDITION_SINGLE_0);
    	
    	}catch (Exception e) {
			e.printStackTrace();
			return error("取消失败！"+e.getMessage());
		}
       return success("开班取消成功");
    }
	 @ResponseBody
    @RequestMapping("/openClassArrange/singleResult/check")
	@ControllerInfo(value = "是否开班单科开班")
    public String singleCheck(@PathVariable String roundsId) {
		GkRounds round = gkRoundsService.findRoundById(roundsId);
		if(round==null){
			return error("该选课系统对应这个轮次不存在");
		}
		GkSubjectArrange gkArrange=gkSubjectArrangeService.findArrangeById(round.getSubjectArrangeId());
		if(gkArrange==null){
			return error("该选课系统不存在");
		}
		if(isNowArrange(roundsId)){
			return success("now");
	    }else{
	    	if(round.getStep()>=GkElectveConstants.STEP_5){
	    		RedisUtils.del(new String[]{round.getId()+"A_ok",round.getId()+"B_ok"});
	    		//直接下一步
	    		return success("end");
	    	}
//	    	ab
    		String aIndex = RedisUtils.get(round.getId()+"A_ok");
    		String bIndex = RedisUtils.get(round.getId()+"B_ok");
    		if("1".equals(aIndex) && "1".equals(bIndex)){
    			gkRoundsService.updateStep(GkElectveConstants.STEP_5, roundsId);
    			RedisUtils.del(new String[]{round.getId()+"A_ok",round.getId()+"B_ok"});
    			return success("end");
    		}
    		return success("continue");
	    }
    }
	
    @ResponseBody
    @RequestMapping("/openClassArrange/singleResult/save")
	@ControllerInfo(value = "单科开班")
    public String singleMain(@PathVariable String roundsId) {
    	String mess="";
    	try{
			GkRounds round = gkRoundsService.findRoundById(roundsId);
			if(round==null){
				return error("该选课系统对应这个轮次不存在");
			}
			GkSubjectArrange gkArrange=gkSubjectArrangeService.findArrangeById(round.getSubjectArrangeId());
			if(gkArrange==null){
				return error("该选课系统不存在");
			}
			if(isNowArrange(roundsId)){
				return success("now");
		    }else{
		    	if(round.getStep()>=GkElectveConstants.STEP_5){
		    		RedisUtils.del(new String[]{round.getId()+"A_ok",round.getId()+"B_ok"});
		    		//直接下一步
		    		return success("end");
		    	}
//		    	ab
	    		String aIndex = RedisUtils.get(round.getId()+"A_ok");
	    		String bIndex = RedisUtils.get(round.getId()+"B_ok");
	    		if("1".equals(aIndex) && "1".equals(bIndex)){
	    			gkRoundsService.updateStep(GkElectveConstants.STEP_5, roundsId);
	    			RedisUtils.del(new String[]{round.getId()+"A_ok",round.getId()+"B_ok"});
	    			return success("end");
	    		}
		    }
			
			String[] subject_all = findAllSubject(roundsId);
			if(subject_all==null || subject_all.length<=0){
				return error("该选课系统还没有设置走班科目");
			}
			
			String openClass = round.getOpenClass();
			if(StringUtils.isBlank(openClass)){
				return error("是否开学考班没有设置");
			}
			
			String[] sortType = findFrist(gkArrange.getId());
			int subjectNum = gkArrange.getSubjectNum()==null?0:gkArrange.getSubjectNum();
			if(subjectNum==0){
				return error("该选课系统选课数为0,不符合排班要求，请修改");
			}
			if(GkElectveConstants.TRUE_STR.equals(round.getOpenTwo())){
				//纯2+x A默认第1批次
				if(GkElectveConstants.TRUE_STR.equals(openClass)){
					
					//开AB
					if(round.getBatchCountA()!=1){
						return error("2+x模式，设置选考批次时间只能为1个批次");
					}
					if(round.getBatchCountB()==null || round.getBatchCountB()==0 || round.getBatchCountB()<(subject_all.length-subjectNum)){
						return error("设置学考批次时间不能小于总走班数量减去选课数量");
					}
					mess=processSingleABTwo(subject_all,round.getBatchCountA(),round.getBatchCountB(),gkArrange,round, sortType);
					
				}else{
					
					//只开选考DOTO
					mess=processSingleATwo(subject_all, round.getBatchCountA(), gkArrange, round, sortType);
				}
			}else{
				if(GkElectveConstants.TRUE_STR.equals(openClass)){
					//开AB
					if(round.getBatchCountA()==null || round.getBatchCountA()==0 || round.getBatchCountA()<subjectNum){
						return error("设置选考批次时间不能小于选课数量");
					}
					if(round.getBatchCountB()==null || round.getBatchCountB()==0 || round.getBatchCountB()<(subject_all.length-subjectNum)){
						return error("设置学考批次时间不能小于总走班数量减去选课数量");
					}
					mess=processSingleAB(subject_all,round.getBatchCountA(),round.getBatchCountB(),gkArrange,round, sortType);
					
				}else{
					// subjectNum是否需要判断（只排A 需要判断）
					
					if(round.getBatchCountA()==null || round.getBatchCountA()==0 || round.getBatchCountA()<subjectNum){
						return error("设置选考批次时间不能小于选课数量");
					}
					//只开选考DOTO
					mess=processSingleA(subject_all, round.getBatchCountA(), gkArrange, round, sortType);
				}
			}
			
			
			
			
			
			

		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		if("yes".equals(mess)){
			//"没有学生需要单科开班！"
			return success("end");
		}
		return success("now");
       // return success("单科排班成功");
    }
    
    public String  processSingleA(String[] subject_all, int subjectNum,final GkSubjectArrange gkArrange, final GkRounds round, String[] sortType){
    	//SingleNewArrange singArrange=new SingleNewArrange();
    	//所有高中7选3科目
    	List<Course> courseList = SUtils.dt(courseRemoteService.findByBaseCourseCodes(BaseConstants.SUBJECT_73), new TR<List<Course>>() {});
    	Map<String, Course> subjectNameMapTmp = new HashMap<String,Course>();
    	if(CollectionUtils.isNotEmpty(courseList)){
    	    subjectNameMapTmp=EntityUtils.getMap(courseList, "id");
        }
    	final Map<String, Course> subjectNameMap = subjectNameMapTmp;
    	
    	//学生选课结果---所有
        List<StudentSubjectDto> allstudents=gkResultService.findAllStudentSubjectDto(gkArrange.getId(),null);
        if(CollectionUtils.isEmpty(allstudents)){
    		throw new RuntimeException("没有学生选课数据！");
    	}
        final Map<String,StudentSubjectDto> alldtoMap=EntityUtils.getMap(allstudents, "stuId");
    	
    	
        List<StudentSubjectDto> allstudentsA=copyList(allstudents);
        
        Map<String, ArrangeCapacityRange> subjectIdTypeCapacityRangeMap=findSingleMaxMinNum(round.getId());
        //A
    	List<StudentSubjectDto> studentsA=null;

    	try{
    		studentsA = initSingleStudentsA1(true, subjectNum, subject_all, gkArrange, round.getId(), allstudentsA);
        }catch (Exception e) {
 			e.printStackTrace();
 			throw new RuntimeException(e.getMessage());
 		}
    	if(CollectionUtils.isEmpty(studentsA)){
    		return "yes";
    	}
    	// 2+x的人组成的班级
        
        List<ArrangeClass> arrangeClassAList = new ArrayList<ArrangeClass>();
        ArrangeClass class1 = null;
        
        List<GkGroupClass> grouplist = gkGroupClassService.findByRoundsIdType(round.getId(),GkElectveConstants.GROUP_TYPE_2);
        if(CollectionUtils.isNotEmpty(grouplist)){
        	for(GkGroupClass gg:grouplist){
        		if(CollectionUtils.isEmpty(gg.getStuIdList())){
                	continue;
                }
        		class1 = new ArrangeClass();
                class1.setClassId(gg.getId());
                
                class1.setStudentIds(new HashSet<String>(Arrays.asList(gg.getStuIdList().toArray(new String[0]))));
                
                String subjectIds = gg.getSubjectIds();
                String[] choosedArr = subjectIds.split(",");
                class1.setChoosedSubjects(new HashSet<String>(Arrays.asList(choosedArr)));
                arrangeClassAList.add(class1);
        	}
        }
    	
    	//排A
	    List<ArrangeStudent> arrangeStudentAList = ArrangeDtoConverter.convertToArrangeStudent(studentsA);
	    ArrangeSingleSolver solverA = new ArrangeSingleSolver(subjectNum, subjectNum, arrangeStudentAList, arrangeClassAList, round.getId()+"A");
	     
	    solverA.addListener(new SolverListener() {
	         @Override
	         public void solveStarted() {
	         }
	         @Override
	         public void solveFinished(Map<String, List<Room>>[] bottleArray, Map<String, Set<Integer>> classIdAdditionalSubjectIndexSetMap) {
	             long start = System.currentTimeMillis();
	             makeA(alldtoMap,gkArrange,round,bottleArray,subjectNameMap,classIdAdditionalSubjectIndexSetMap,true);
	             long end = System.currentTimeMillis();
	             System.out.println("makeAAAAA耗时：" + (end-start)/1000 + "s");
	         }
	         @Override
	         public void onError(Exception e) {
	         	System.out.println(e.getMessage());
	         }
	         @Override
	         public void solveCancelled() {
	         }
	   });
	   solverA.solve(subjectIdTypeCapacityRangeMap);
		return null;
    }
    
    
    //只安排2+x
    public String  processSingleABTwo(String[] subject_all, int subjectNumA,int subjectNumB, final GkSubjectArrange gkArrange, final GkRounds round, String[] sortType){
    	//所有高中7选3科目
    	List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subject_all), new TR<List<Course>>() {});
    	Map<String, Course> subjectNameMapTmp = new HashMap<String,Course>();
    	if(CollectionUtils.isNotEmpty(courseList)){
    	    subjectNameMapTmp=EntityUtils.getMap(courseList, "id");
        }
    	final Map<String, Course> subjectNameMap = subjectNameMapTmp;
    	
    	//学生选课结果---所有
        List<StudentSubjectDto> allstudents=gkResultService.findAllStudentSubjectDto(gkArrange.getId(),null);
        if(CollectionUtils.isEmpty(allstudents)){
    		throw new RuntimeException("没有学生选课数据！");
    	}
        final Map<String,StudentSubjectDto> alldtoMap=EntityUtils.getMap(allstudents, "stuId");
    	
    	
        List<StudentSubjectDto> allstudentsA=copyList(allstudents);
        List<StudentSubjectDto> allstudentsB=copyList(allstudents);
        
        Map<String, ArrangeCapacityRange> subjectIdTypeCapacityRangeMap=findSingleMaxMinNum(round.getId());
        //A
    	List<StudentSubjectDto> studentsA=null;

    	try{
    		studentsA = initSingleStudentsA1(true, subjectNumA, subject_all, gkArrange, round.getId(), allstudentsA);
        }catch (Exception e) {
 			e.printStackTrace();
 			throw new RuntimeException(e.getMessage());
 		}
    	
    	//B
    	List<StudentSubjectDto> studentsB=null;
    	try{
    		studentsB = initSingleStudentsB1(true, subjectNumB, subject_all, gkArrange,round.getId(), allstudentsB);
        }catch (Exception e) {
 			e.printStackTrace();
 			throw new RuntimeException(e.getMessage());
 		}
    	if(CollectionUtils.isEmpty(studentsB) && CollectionUtils.isEmpty(studentsA)){
    		return "yes";
    	}
    	//只留2+x
//        List<StudentSubjectDto> studentsA1=new ArrayList<StudentSubjectDto>();
        List<StudentSubjectDto> studentsB1=new ArrayList<StudentSubjectDto>();
        Map<String, StudentSubjectDto> aMap = EntityUtils.getMap(studentsA,"stuId");
        Map<String, StudentSubjectDto> bMap=EntityUtils.getMap(studentsB,"stuId");
    	// 2+x的人组成的班级
    	List<GkGroupClass> grouplist = gkGroupClassService.findByRoundsIdType(round.getId(),GkElectveConstants.GROUP_TYPE_2);
        Map<String,Set<String>> stuBySubjectId=new HashMap<String,Set<String>>();
    	
    	if(CollectionUtils.isNotEmpty(grouplist)){
        	for(GkGroupClass gg:grouplist){
        		if(CollectionUtils.isEmpty(gg.getStuIdList())){
                	continue;
                }
        		List<String> subIds = Arrays.asList(gg.getSubjectIds().split(","));
        		for(String s:gg.getStuIdList()){
        			if(bMap.containsKey(s)){
        				studentsB1.add(bMap.get(s));
        			}
        			if(aMap.containsKey(s)){
        				List<String> allSubjectId = aMap.get(s).getAllSubjectIds();
        				for(String ss:allSubjectId){
        					if(!subIds.contains(ss)){
        						if(!stuBySubjectId.containsKey(ss)){
        							stuBySubjectId.put(ss, new HashSet<String>());
        						}
        						stuBySubjectId.get(ss).add(s);
        					}
        				}
        			}
        		}
        	}
        }
        
        
       
        
       if(CollectionUtils.isNotEmpty(studentsB1)){
		   //排B
		   List<ArrangeClass> arrangeClassBList = new ArrayList<ArrangeClass>();
	       List<ArrangeStudent> arrangeStudentBList = ArrangeDtoConverter.convertToArrangeStudent(studentsB1);
	       ArrangeSingleSolver solverB = new ArrangeSingleSolver(subjectNumB, subjectNumB, arrangeStudentBList, arrangeClassBList, round.getId()+"B");
	       
		   solverB.addListener(new SolverListener() {
	           @Override
	           public void solveStarted() {
	           }
	           @Override
	           public void solveFinished(Map<String, List<Room>>[] bottleArray, Map<String, Set<Integer>> classIdAdditionalSubjectIndexSetMap) {
	               long start = System.currentTimeMillis();
	               boolean flag=false;
	               makeBTwo(alldtoMap, gkArrange, round, bottleArray, subjectNameMap, classIdAdditionalSubjectIndexSetMap, flag, subjectNumA,subject_all);
	               RedisUtils.set(round.getId()+"B_ok", "1");
	               long end = System.currentTimeMillis();
	               System.out.println("makeBBBBBBB耗时：" + (end-start)/1000 + "s");
	               start = System.currentTimeMillis();
	               end = System.currentTimeMillis();
	               if(stuBySubjectId!=null && stuBySubjectId.size()>0){
	            	   makeATwo(1,round, gkArrange, stuBySubjectId, subjectIdTypeCapacityRangeMap, true, alldtoMap);
	            	   System.out.println("makeAAAAAAA耗时：" + (end-start)/1000 + "s");
	               }
	               
	              
	           }
	           @Override
	           public void onError(Exception e) {
	           	System.out.println(e.getMessage());
	           }
	           @Override
	           public void solveCancelled() {
	           }
		   });
		   solverB.solve(subjectIdTypeCapacityRangeMap);
       }else{
    	   //不存在B
//    	   boolean flag=false;
//           makeB(alldtoMap, gkArrange, round, null, subjectNameMap, new HashMap<String, Set<Integer>>(), flag, subjectNumA,subject_all);
           RedisUtils.set(round.getId()+"B_ok", "1"); 
           long start = System.currentTimeMillis();
           long end = System.currentTimeMillis();
           if(stuBySubjectId!=null && stuBySubjectId.size()>0){
        	   makeATwo(1,round, gkArrange, stuBySubjectId, subjectIdTypeCapacityRangeMap, true, alldtoMap);
        	   System.out.println("makeAAAAAAA耗时：" + (end-start)/1000 + "s");
           }
       }
		return null;
    }
    public String  processSingleATwo(String[] subject_all, int subjectNumA,final GkSubjectArrange gkArrange, final GkRounds round, String[] sortType){
    	//所有高中7选3科目
    	List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subject_all), new TR<List<Course>>() {});
    	Map<String, Course> subjectNameMapTmp = new HashMap<String,Course>();
    	if(CollectionUtils.isNotEmpty(courseList)){
    	    subjectNameMapTmp=EntityUtils.getMap(courseList, "id");
        }
    	final Map<String, Course> subjectNameMap = subjectNameMapTmp;
    	
    	//学生选课结果---所有
        List<StudentSubjectDto> allstudents=gkResultService.findAllStudentSubjectDto(gkArrange.getId(),null);
        if(CollectionUtils.isEmpty(allstudents)){
    		throw new RuntimeException("没有学生选课数据！");
    	}
        final Map<String,StudentSubjectDto> alldtoMap=EntityUtils.getMap(allstudents, "stuId");
    	
        Map<String, ArrangeCapacityRange> subjectIdTypeCapacityRangeMap=findSingleMaxMinNum(round.getId());
        //A
    	List<StudentSubjectDto> studentsA=null;

    	try{
    		studentsA = initSingleStudentsA1(true, subjectNumA, subject_all, gkArrange, round.getId(), allstudents);
        }catch (Exception e) {
 			e.printStackTrace();
 			throw new RuntimeException(e.getMessage());
 		}
    	
    	//只留2+x
        Map<String, StudentSubjectDto> aMap = EntityUtils.getMap(studentsA,"stuId");
    	// 2+x的人组成的班级
    	List<GkGroupClass> grouplist = gkGroupClassService.findByRoundsIdType(round.getId(),GkElectveConstants.GROUP_TYPE_2);
        Map<String,Set<String>> stuBySubjectId=new HashMap<String,Set<String>>();//2+x
    	
    	if(CollectionUtils.isNotEmpty(grouplist)){
        	for(GkGroupClass gg:grouplist){
        		if(CollectionUtils.isEmpty(gg.getStuIdList())){
                	continue;
                }
        		List<String> subIds = Arrays.asList(gg.getSubjectIds().split(","));
        		for(String s:gg.getStuIdList()){
        			if(aMap.containsKey(s)){
        				List<String> allSubjectId = aMap.get(s).getAllSubjectIds();
        				for(String ss:allSubjectId){
        					if(!subIds.contains(ss)){
        						if(!stuBySubjectId.containsKey(ss)){
        							stuBySubjectId.put(ss, new HashSet<String>());
        						}
        						stuBySubjectId.get(ss).add(s);
        					}
        				}
        			}
        		}
        	}
        }
        
        RedisUtils.set(round.getId()+"B_ok", "1"); 
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        if(stuBySubjectId!=null && stuBySubjectId.size()>0){
     	   makeATwo(1,round, gkArrange, stuBySubjectId, subjectIdTypeCapacityRangeMap, true, alldtoMap);
     	   System.out.println("makeAAAAAAA耗时：" + (end-start)/1000 + "s");
        }
       
		return null;
    }
    /**
     * 
     * @param bath 2+x中x固定批次
     * @param round
     * @param gkArrange
     * @param stuBySubjectId
     * @param subjectIdTypeCapacityRangeMap
     * @param isUpdateStep
     * @param alldtoMap
     * @param grouplist
     */
    private void makeATwo(int bath,GkRounds round,GkSubjectArrange gkArrange,Map<String,Set<String>> stuBySubjectId,
    		 Map<String, ArrangeCapacityRange> subjectIdTypeCapacityRangeMap,boolean isUpdateStep, Map<String,StudentSubjectDto> alldtoMap){
    	//需要插入数据库
        List<GkBatch> gkBatchList = new ArrayList<GkBatch>();
        List<GkTeachClassStore> teachClassList = new ArrayList<GkTeachClassStore>();
        List<GkTeachClassStuStore> teachClassStuList = new ArrayList<GkTeachClassStuStore>();
        List<GkTeachClassEx> insertAvg=new ArrayList<GkTeachClassEx>();
        Map<String,Integer> subjectNumAMap=new HashMap<String, Integer>();
        Map<String,Integer> subjectNumBMap=new HashMap<String, Integer>();
        Set<String> subjectIds = stuBySubjectId.keySet();
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{})), new TR<List<Course>>() {});
    	Map<String, Course> subjectNameMap = new HashMap<String,Course>();
    	if(CollectionUtils.isNotEmpty(courseList)){
    		subjectNameMap=EntityUtils.getMap(courseList, "id");
        }
    	
        for(Entry<String, Set<String>> item:stuBySubjectId.entrySet()){
  		   String subId=item.getKey();
  		   //班级数不能为0
  		   int openClassNum = subjectIdTypeCapacityRangeMap.get(subId+"A").getClassNum();
  		   Set<String> stuId = item.getValue();
  		   //将stuId分成openClassNum 个班
  		   if(openClassNum<=0){
  			   //开一个班
  			 toTeachCls(round, gkArrange, subId, "A", Arrays.asList(stuId.toArray(new String[]{})),
         			subjectNumAMap, subjectNumBMap, bath, subjectNameMap, teachClassStuList, alldtoMap, 
         			insertAvg, gkBatchList,teachClassList,BaseConstants.ZERO_GUID);
  		   }else{
  			   //先随机分配
  			   //学生整体
  			   List<StudentSubjectDto> allstuSubjectList=new ArrayList<StudentSubjectDto>();
  			   for(String s:stuId){
  				  StudentSubjectDto tt = alldtoMap.get(s);
  				  //按成绩平均分
  				  if(tt.getScoreMap()!=null && tt.getScoreMap().containsKey(subId)){
  					 tt.setAvgScore(tt.getScoreMap().get(subId));
  				  }
  				  allstuSubjectList.add(tt);
  			   }
  			   List<String>[] array = new List[openClassNum];
	 			for(int i=0;i<openClassNum;i++){
	 				array[i]=new ArrayList<String>();
	 			}
  			   //按成绩
  			   onlysortStuScore(allstuSubjectList);
			   openClass(openClassNum, allstuSubjectList, array);
			   for(int i=0;i<array.length;i++){
					if(CollectionUtils.isNotEmpty(array[i])){
						//学生id
						List<String> ll = array[i];
						 toTeachCls(round, gkArrange, subId, "A", Arrays.asList(ll.toArray(new String[]{})),
				         			subjectNumAMap, subjectNumBMap, bath, subjectNameMap, teachClassStuList, alldtoMap, 
				         			insertAvg, gkBatchList,teachClassList,BaseConstants.ZERO_GUID);
					}
			   }
  		   }
  	   }

        long start = System.currentTimeMillis();
        gkBatchService.saveBatchsBySubjectIdType("A", round, gkBatchList, teachClassList, teachClassStuList, insertAvg, isUpdateStep);
		long end = System.currentTimeMillis();
		System.out.println("saveBatchs耗时：" + (end-start)/1000 + "s");
    }
    
    
    
    
    
    public String  processSingleAB(String[] subject_all, int subjectNumA,int subjectNumB, final GkSubjectArrange gkArrange, final GkRounds round, String[] sortType){
    	//SingleNewArrange singArrange=new SingleNewArrange();
    	//所有高中7选3科目
    	List<Course> courseList = SUtils.dt(courseRemoteService.findByBaseCourseCodes(BaseConstants.SUBJECT_73), new TR<List<Course>>() {});
    	Map<String, Course> subjectNameMapTmp = new HashMap<String,Course>();
    	if(CollectionUtils.isNotEmpty(courseList)){
    	    subjectNameMapTmp=EntityUtils.getMap(courseList, "id");
        }
    	final Map<String, Course> subjectNameMap = subjectNameMapTmp;
    	
    	//学生选课结果---所有
        List<StudentSubjectDto> allstudents=gkResultService.findAllStudentSubjectDto(gkArrange.getId(),null);
        if(CollectionUtils.isEmpty(allstudents)){
    		throw new RuntimeException("没有学生选课数据！");
    	}
        final Map<String,StudentSubjectDto> alldtoMap=EntityUtils.getMap(allstudents, "stuId");
    	
    	
        List<StudentSubjectDto> allstudentsA=copyList(allstudents);
        List<StudentSubjectDto> allstudentsB=copyList(allstudents);
        
        Map<String, ArrangeCapacityRange> subjectIdTypeCapacityRangeMap=findSingleMaxMinNum(round.getId());
        //A
    	List<StudentSubjectDto> studentsA=null;

    	try{
    		studentsA = initSingleStudentsA1(true, subjectNumA, subject_all, gkArrange, round.getId(), allstudentsA);
        }catch (Exception e) {
 			e.printStackTrace();
 			throw new RuntimeException(e.getMessage());
 		}
    	
    	//B
    	List<StudentSubjectDto> studentsB=null;
    	try{
    		studentsB = initSingleStudentsB1(true, subjectNumB, subject_all, gkArrange,round.getId(), allstudentsB);
        }catch (Exception e) {
 			e.printStackTrace();
 			throw new RuntimeException(e.getMessage());
 		}
    	if(CollectionUtils.isEmpty(studentsB) && CollectionUtils.isEmpty(studentsA)){
    		return "yes";
    	}
    	
    	// 2+x的人组成的班级
        
        List<ArrangeClass> arrangeClassAList = new ArrayList<ArrangeClass>();
        ArrangeClass class1 = null;
        
        List<GkGroupClass> grouplist = gkGroupClassService.findByRoundsIdType(round.getId(),GkElectveConstants.GROUP_TYPE_2);
        if(CollectionUtils.isNotEmpty(grouplist)){
        	for(GkGroupClass gg:grouplist){
        		if(CollectionUtils.isEmpty(gg.getStuIdList())){
                	continue;
                }
        		class1 = new ArrangeClass();
                class1.setClassId(gg.getId());
                
                class1.setStudentIds(new HashSet<String>(Arrays.asList(gg.getStuIdList().toArray(new String[0]))));
                
                String subjectIds = gg.getSubjectIds();
                String[] choosedArr = subjectIds.split(",");
                class1.setChoosedSubjects(new HashSet<String>(Arrays.asList(choosedArr)));
                arrangeClassAList.add(class1);
        	}
        }
        if(CollectionUtils.isNotEmpty(studentsA)){
		    List<ArrangeStudent> arrangeStudentAList = ArrangeDtoConverter.convertToArrangeStudent(studentsA);
		    ArrangeSingleSolver solverA = new ArrangeSingleSolver(subjectNumA, subjectNumA, arrangeStudentAList, arrangeClassAList, round.getId()+"A");
		     
		    solverA.addListener(new SolverListener() {
		         @Override
		         public void solveStarted() {
		         }
		         @Override
		         public void solveFinished(Map<String, List<Room>>[] bottleArray, Map<String, Set<Integer>> classIdAdditionalSubjectIndexSetMap) {
		             long start = System.currentTimeMillis();
		             boolean flag=false;
		             if("1".equals(RedisUtils.get(round.getId()+"B"))){
		            	 flag=true;
		             }
		             makeA(alldtoMap,gkArrange,round,bottleArray,subjectNameMap,classIdAdditionalSubjectIndexSetMap,flag);
		             RedisUtils.set(round.getId()+"A_ok", "1");
		             long end = System.currentTimeMillis();
		             System.out.println("makeAAAAA耗时：" + (end-start)/1000 + "s");
		         }
		         @Override
		         public void onError(Exception e) {
		         	System.out.println(e.getMessage());
		         }
		         @Override
		         public void solveCancelled() {
		         }
		   });
		   solverA.solve(subjectIdTypeCapacityRangeMap);
       }else{
    	   boolean flag=false;
           if("1".equals(RedisUtils.get(round.getId()+"B"))){
          	 flag=true;
           }
    	   makeA(alldtoMap,gkArrange,round,null,subjectNameMap,new HashMap<String, Set<Integer>>(),flag);
           RedisUtils.set(round.getId()+"A_ok", "1");
       }
        
       if(CollectionUtils.isNotEmpty(studentsB)){
		   //排B
		   List<ArrangeClass> arrangeClassBList = new ArrayList<ArrangeClass>();
	       List<ArrangeStudent> arrangeStudentBList = ArrangeDtoConverter.convertToArrangeStudent(studentsB);
	       ArrangeSingleSolver solverB = new ArrangeSingleSolver(subjectNumB, subjectNumB, arrangeStudentBList, arrangeClassBList, round.getId()+"B");
	       
		   solverB.addListener(new SolverListener() {
	           @Override
	           public void solveStarted() {
	           }
	           @Override
	           public void solveFinished(Map<String, List<Room>>[] bottleArray, Map<String, Set<Integer>> classIdAdditionalSubjectIndexSetMap) {
	               long start = System.currentTimeMillis();
	               boolean flag=false;
	               if("1".equals(RedisUtils.get(round.getId()+"A"))){
	            	 flag=true;
	               }
	               makeB(alldtoMap, gkArrange, round, bottleArray, subjectNameMap, classIdAdditionalSubjectIndexSetMap, flag, subjectNumA,subject_all);
	               RedisUtils.set(round.getId()+"B_ok", "1");
	               long end = System.currentTimeMillis();
	               System.out.println("makeBBBBBBB耗时：" + (end-start)/1000 + "s");
	           }
	           @Override
	           public void onError(Exception e) {
	           	System.out.println(e.getMessage());
	           }
	           @Override
	           public void solveCancelled() {
	           }
		   });
		   solverB.solve(subjectIdTypeCapacityRangeMap);
       }else{
    	   boolean flag=false;
           if("1".equals(RedisUtils.get(round.getId()+"A"))){
        	 flag=true;
           }
           makeB(alldtoMap, gkArrange, round, null, subjectNameMap, new HashMap<String, Set<Integer>>(), flag, subjectNumA,subject_all);
           RedisUtils.set(round.getId()+"B_ok", "1"); 
       }
		return null;
    }
    
    
    private List<StudentSubjectDto> initSingleStudentsA1(boolean isClass,int subjectNum,String[] subject_all,
    		GkSubjectArrange gkArrange,String roundsId,List<StudentSubjectDto> allStudents) {
        List<StudentSubjectDto> returnstudents = new ArrayList<StudentSubjectDto>();
        List<String> arrangeStuId=new ArrayList<String>();
        if(CollectionUtils.isEmpty(allStudents)){
        	return returnstudents;
        }
        if(isClass){
        	//没有安排到组合班级的学生排除
        	//排除3+0
        	Map<String, StudentSubjectDto> map = EntityUtils.getMap(allStudents, "stuId");
        	List<GkGroupClass> allgrouplist = gkGroupClassService.findByRoundsIdType(roundsId,null);
        	
        	List<GkGroupClass> grouplist2=new ArrayList<GkGroupClass>();//混合
        	
//        	List<GkGroupClass> grouplist1 = gkGroupClassService.findByRoundsIdType(roundsId,GkElectveConstants.GROUP_TYPE_2);
            if(CollectionUtils.isNotEmpty(allgrouplist)){
            	for(GkGroupClass gg:allgrouplist){
            		if(GkElectveConstants.GROUP_TYPE_3.equals(gg.getGroupType())){
            			grouplist2.add(gg);
            			continue;
            		}
            		boolean flag=false;
            		if(GkElectveConstants.GROUP_TYPE_2.equals(gg.getGroupType())){
            			flag=true;
            		}
            		String[] subs = gg.getSubjectIds().split(",");
            		if(CollectionUtils.isNotEmpty(gg.getStuIdList())){
            			for(String s:gg.getStuIdList()){
            				if(map.containsKey(s)){
            					StudentSubjectDto sss = map.get(s);
            					if(sss.getChooseSubjectIds().size()<=0){
            	        			//查的是选课表 所以选课为空的可能性没有
            						throw new RuntimeException("学生选课数据有所调整，导致手动排班组合中，学生数据有错误！");
            	        		}else{
        	        				if(!(CollectionUtils.union(sss.getChooseSubjectIds(), Arrays.asList(subs)).size()==sss.getChooseSubjectIds().size())){
        	        					throw new RuntimeException("学生选课数据有所调整，导致手动排班组合中，学生数据有错误！");
        	        				}
            	        		}
            				}else{
            					throw new RuntimeException("学生选课数据有所调整，导致手动排班组合中，学生数据有错误！");
            				}
            				if(flag){
            					arrangeStuId.add(s);
            				}
            			}
                	}
            	}
            	
            }
            if(CollectionUtils.isNotEmpty(grouplist2)){
            	for(GkGroupClass gg:grouplist2){
            		if(CollectionUtils.isNotEmpty(gg.getStuIdList())){
            			arrangeStuId.addAll(gg.getStuIdList());
                	}
            	}
            	
            }
        }
        
        //String stuId=null;
        List<String> alls=null;
        String[] subject_all_self=null;
        if(CollectionUtils.isNotEmpty(allStudents)){
        	for(StudentSubjectDto dto:allStudents){
        		if(isClass && !arrangeStuId.contains(dto.getStuId())){
        			continue;
        		}
        		//需要排的
        		alls=new ArrayList<String>();
        		//stuId = dto.getStuId();
        		if(dto.getChooseSubjectIds().size()<=0){
        			//查的是选课表 所以选课为空的可能性没有
        			continue;
        		}
        		
        		//去除不走班科目
    			subject_all_self = moveSubject(dto.getChooseSubjectIds(),subject_all);
        		
    			//只开A
    			if(subject_all_self==null || subject_all_self.length<=0){
    				continue;
    			}
        		
        		for(String subject:subject_all_self){
        			alls.add(subject);
        		}
        		
        		if(CollectionUtils.isNotEmpty(alls)){
        			//不够批次+null
        			addNull(alls, subjectNum);
        			dto.setCombined(null);
        			dto.setAllSubjectIds(alls);
        			//计算平均分
        			//求3门平均分
//                	 Map<String, Double> scoreMap = dto.getScoreMap();
//                	 if(scoreMap==null || scoreMap.size()<=0){
//                		 dto.setAvgScore(0);
//                	 }else{
//                		double sum=0;
//	             		 for(String key:scoreMap.keySet()){
//	             			Double dd = scoreMap.get(key);
//	             			if(dto.getChooseSubjectIds().contains(key)){
//	             				if(dd!=null){
//		             				sum=sum+dd;
//		             			}
//	             			}
//	             		 }
//	             		dto.setAvgScore(sum/subjectNum);
//                	 }
        			returnstudents.add(dto);
        		}
        	}
        }
        if(CollectionUtils.isNotEmpty(returnstudents)){
        	 Collections.sort(returnstudents, new Comparator<StudentSubjectDto>() {
     			@Override
     			public int compare(StudentSubjectDto o1, StudentSubjectDto o2) {
     				return o1.getClassId().compareTo(o2.getClassId());
     			}
     		});
        }else{
        	return returnstudents;
        }
        return returnstudents;
    }
    
    private List<StudentSubjectDto> initSingleStudentsB1(boolean isClass,int subjectNum,String[] subject_all,
    		GkSubjectArrange gkArrange,String roundsId,List<StudentSubjectDto> allStudents) {
        List<StudentSubjectDto> returnstudents = new ArrayList<StudentSubjectDto>();
        List<String> arrangeStuId=new ArrayList<String>();
        if(CollectionUtils.isEmpty(allStudents)){
        	return returnstudents;
        }
        if(isClass){
        	//没有安排到组合班级的学生排除
        	//排除3+0
        	Map<String, StudentSubjectDto> map = EntityUtils.getMap(allStudents, "stuId");
        	List<GkGroupClass> allgrouplist = gkGroupClassService.findByRoundsIdType(roundsId,null);
        	
        	List<GkGroupClass> grouplist2=new ArrayList<GkGroupClass>();//混合
        	
//        	List<GkGroupClass> grouplist1 = gkGroupClassService.findByRoundsIdType(roundsId,GkElectveConstants.GROUP_TYPE_2);
            if(CollectionUtils.isNotEmpty(allgrouplist)){
            	for(GkGroupClass gg:allgrouplist){
            		if(GkElectveConstants.GROUP_TYPE_3.equals(gg.getGroupType())){
            			grouplist2.add(gg);
            			continue;
            		}
            		boolean flag=false;
            		if(GkElectveConstants.GROUP_TYPE_2.equals(gg.getGroupType())){
            			flag=true;
            		}
            		String[] subs = gg.getSubjectIds().split(",");
            		if(CollectionUtils.isNotEmpty(gg.getStuIdList())){
            			for(String s:gg.getStuIdList()){
            				if(map.containsKey(s)){
            					StudentSubjectDto sss = map.get(s);
            					if(sss.getChooseSubjectIds().size()<=0){
            	        			//查的是选课表 所以选课为空的可能性没有
            						throw new RuntimeException("学生选课数据有所调整，导致手动排班组合中，学生数据有错误！");
            	        		}else{
        	        				if(!(CollectionUtils.union(sss.getChooseSubjectIds(), Arrays.asList(subs)).size()==sss.getChooseSubjectIds().size())){
        	        					throw new RuntimeException("学生选课数据有所调整，导致手动排班组合中，学生数据有错误！");
        	        				}
            	        		}
            				}else{
            					throw new RuntimeException("学生选课数据有所调整，导致手动排班组合中，学生数据有错误！");
            				}
            				if(flag){
            					arrangeStuId.add(s);
            				}
            			}
                	}
            	}
            	
            }
            if(CollectionUtils.isNotEmpty(grouplist2)){
            	for(GkGroupClass gg:grouplist2){
            		if(CollectionUtils.isNotEmpty(gg.getStuIdList())){
            			arrangeStuId.addAll(gg.getStuIdList());
                	}
            	}
            	
            }
        }
        
        //String stuId=null;
        List<String> alls=null;
//        String[] subject_all_self=null;
        if(CollectionUtils.isNotEmpty(allStudents)){
        	for(StudentSubjectDto dto:allStudents){
        		if(isClass && !arrangeStuId.contains(dto.getStuId())){
        			continue;
        		}
        		//需要排的
        		alls=new ArrayList<String>();
        		//stuId = dto.getStuId();
        		if(dto.getChooseSubjectIds().size()<=0){
        			//查的是选课表 所以选课为空的可能性没有
        			continue;
        		}
        		
        		//去除不走班科目
//    			subject_all_self = moveSubject(dto.getChooseSubjectIds(),subject_all);

        		for(String subject:subject_all){
        			if(dto.getChooseSubjectIds().contains(subject)){
        				continue;
        			}
        			alls.add(subject);
        		}
        		
        		if(CollectionUtils.isNotEmpty(alls)){
        			//不够批次+null
        			addNull(alls, subjectNum);
        			dto.setCombined(null);
        			dto.setAllSubjectIds(alls);
        			//计算平均分
        			//求3门平均分
//                	 Map<String, Double> scoreMap = dto.getScoreMap();
//                	 if(scoreMap==null || scoreMap.size()<=0){
//                		 dto.setAvgScore(0);
//                	 }else{
//                		double sum=0;
//	             		 for(String key:scoreMap.keySet()){
//	             			Double dd = scoreMap.get(key);
//	             			if(dto.getChooseSubjectIds().contains(key)){
//	             				if(dd!=null){
//		             				sum=sum+dd;
//		             			}
//	             			}
//	             		 }
//	             		dto.setAvgScore(sum/subjectNum);
//                	 }
        			returnstudents.add(dto);
        		}
        	}
        }
        if(CollectionUtils.isNotEmpty(returnstudents)){
        	 Collections.sort(returnstudents, new Comparator<StudentSubjectDto>() {
     			@Override
     			public int compare(StudentSubjectDto o1, StudentSubjectDto o2) {
     				return o1.getClassId().compareTo(o2.getClassId());
     			}
     		});
        }else{
        	return returnstudents;
        }
        return returnstudents;
    }
    
    

    private Map<String, ArrangeCapacityRange> findSingleMaxMinNum(String roundsId) {
    	Map<String, ArrangeCapacityRange> returnMap=new HashMap<String, ArrangeCapacityRange>();
    	ArrangeCapacityRange capacityRange = null;
    	List<GkConditionDto> condList = gkConditionService.findByGkConditionDtos(roundsId,GkElectveConstants.GKCONDITION_SINGLE_0);
    	if(CollectionUtils.isNotEmpty(condList)){
    		for(GkConditionDto dto:condList){
    			//单科的科目就一个
    			capacityRange = new ArrangeCapacityRange();
    			Set<String> subjectIds =dto.getSubjectIds();
    			//单科排班 subjectIds最多只有一个值
    			if(subjectIds==null || subjectIds.size()!=1){
    				//这种属于数据问题，先不予考虑
    				continue;
    			}
    			capacityRange.setClassNum(dto.getClaNum()==null?0:dto.getClaNum());
    			capacityRange.setMaxCapacity(dto.getMaxNum());
    			capacityRange.setMinCapacity(dto.getNum());
    			returnMap.put(dto.getSubjectIds().toArray(new String[0])[0]+dto.getGkType(), capacityRange);
    		}
    	}
    		
		return returnMap;
	}
	//    public void processSingleAB(boolean isAB,String[] subject_all, int subjectNum,
//			GkSubjectArrange gkArrange, GkRounds round, String[] sortType) {
//    	Set<String> subjectSet=new HashSet<String>();
//    	//所有高中7选3科目
//    	List<Course> courseList = SUtils.dt(courseRemoteService.findByBaseCourseCodes(GkElectveConstants.SUBJECT_TYPES), new TR<List<Course>>() {});
//    	Map<String, Course> subjectNameMap = new HashMap<String,Course>();
//    	if(CollectionUtils.isNotEmpty(courseList)){
//    		subjectNameMap=EntityUtils.getMap(courseList, "id");
//        }
//    	//学生选课结果---所有
//        List<StudentSubjectDto> allstudents=gkResultService.findAllStudentSubjectDto(gkArrange.getId(),null);
//        if(CollectionUtils.isEmpty(allstudents)){
//    		throw new RuntimeException("没有学生选课数据！");
//    	}
//        Map<String,StudentSubjectDto> alldtoMap=EntityUtils.getMap(allstudents, "stuId");
//    	List<StudentSubjectDto> students=null;
//    	 try{
//         	students = initSingleStudents(isAB,subjectNum,subject_all,gkArrange,round.getId(),sortType,allstudents);
//         }catch (Exception e) {
// 			e.printStackTrace();
// 		}
//    	Set<String> chooseSubjectIds=null;
//    	List<String> allSub=null;
//    	
//    	// 选科目的总人数
//    	Map<String, Integer> subjectStudentCountAMap = new HashMap<String, Integer>();
//    	Map<String, Integer> subjectStudentCountBMap = new HashMap<String, Integer>();
//    	for(StudentSubjectDto dto:students){
//    		chooseSubjectIds = dto.getChooseSubjectIds();//包括不走班没有null
//    		allSub = dto.getAllSubjectIds();//需要排的课程 包括null
//    		subjectSet.addAll(allSub);
//    		for(String s:allSub){
//    			if(s==null){
//    				continue;
//    			}
//				if(chooseSubjectIds.contains(s)){
//					//选考
//					if(!subjectStudentCountAMap.containsKey(s)){
//						subjectStudentCountAMap.put(s, 0);
//	        		}
//					subjectStudentCountAMap.put(s, subjectStudentCountAMap.get(s)+1);
//				}else{
//					if(isAB){
//						//学考
//						if(!subjectStudentCountBMap.containsKey(s)){
//							subjectStudentCountBMap.put(s, 0);
//		        		}
//						subjectStudentCountBMap.put(s, subjectStudentCountBMap.get(s)+1);
//					
//					}
//				}
//			}
//    	}
//    	if(subjectSet.size()<=0){
//    		throw new RuntimeException("没有科目需要排班！");
//    	}
//    	
//    	//单科参数  key:subject value:班级人数限制
//    	//单科设置每班人数  后面组装数据用到
//		Map<String,Integer> subjectClaNumAMap=new HashMap<String, Integer>();
//		Map<String,Integer> subjectClaNumBMap=new HashMap<String, Integer>();
//        getSingleSubjectClassNum(round.getId(),subjectClaNumAMap,subjectClaNumBMap);
//        
//    	String subjectId=null;
//    	//科目要开班班级数(包括A,B)
//    	Map<String, Integer> subjectRoomCountMap = new HashMap<String, Integer>();
//		Integer stuCount;
//		for (Map.Entry<String, Integer> totalEntry : subjectStudentCountAMap.entrySet()) {
//			subjectId = totalEntry.getKey();
//			stuCount = totalEntry.getValue();
//			if (!subjectClaNumAMap.containsKey(subjectId) || subjectClaNumAMap.get(subjectId) == 0) {
//				throw new RuntimeException("没有设置选考单科每班人数,科目: " + subjectNameMap.get(subjectId).getSubjectName());
//			}
//			subjectRoomCountMap.put(subjectId, (stuCount - 1) / subjectClaNumAMap.get(subjectId) + 1);
//		}
//		if(isAB){
//			for (Map.Entry<String, Integer> totalEntry : subjectStudentCountBMap.entrySet()) {
//				subjectId = totalEntry.getKey();
//				stuCount = totalEntry.getValue();
//				if (!subjectClaNumBMap.containsKey(subjectId) || subjectClaNumBMap.get(subjectId) == 0) {
//					throw new RuntimeException("没有设置学考单科每班人数,科目: " + subjectNameMap.get(subjectId).getSubjectName());
//				}
//				if(subjectRoomCountMap.containsKey(subjectId)){
//					subjectRoomCountMap.put(subjectId, subjectRoomCountMap.get(subjectId)+(stuCount - 1) / subjectClaNumBMap.get(subjectId) + 1);
//				}else{
//					subjectRoomCountMap.put(subjectId, (stuCount - 1) / subjectClaNumBMap.get(subjectId) + 1);
//				}
//			}
//		}
//		
//		// 平衡批次课程 科目批次 8= 3+3+2
//		Map<String, Integer[]> subjectBatchBalanceMap = getSuitableSubjectBatchArray(subjectRoomCountMap,subjectNum);
//				
//		Map<String, List<Room>>[] bottleArray = singleIteration(isAB,students,subjectClaNumAMap,subjectClaNumBMap,subjectNum);
//
//		Map<String, List<Move>> subjectMoveListMap = null;
//		String[] subjectArr;
//		if(!isAB){
//			subjectArr=subjectSet.toArray(new String[0]);
//		}else{
//			subjectArr=subject_all;
//		}
//		for (int i = 0; i < ITERATION; i++) {
//			subjectMoveListMap = select(subjectNum,bottleArray, subjectBatchBalanceMap,subjectArr);
//			cross(subjectMoveListMap,subjectArr);
//			bottleArray = singleIteration(isAB,students,subjectClaNumAMap,subjectClaNumBMap,subjectNum);
//		}
//		
//		//组装并保存到数据库
//        make(alldtoMap,gkArrange,round,bottleArray,subjectNameMap);
//		
//	}
//    private void make(Map<String,StudentSubjectDto> alldtoMap,GkSubjectArrange gkArrange,GkRounds round,Map<String, List<Room>>[] roomCombineArray,Map<String, Course> subjectNameMap,Map<String, Set<Integer>> classIdAdditionalSubjectIndexSetMap){
//    	boolean flag=jadge(roomCombineArray);
//		//需要插入数据库
//        List<GkBatch> gkBatchList = new ArrayList<GkBatch>();
//        GkBatch gkBatch=null;
//        GkTeachClassStore teachClass = null;
//        List<GkTeachClassStore> teachClassList = new ArrayList<GkTeachClassStore>();
//        GkTeachClassStuStore teachClassStu=null;
//        List<GkTeachClassStuStore> teachClassStuList = new ArrayList<GkTeachClassStuStore>();
//        List<GkTeachClassEx> insertAvg=new ArrayList<GkTeachClassEx>();
//        GkTeachClassEx avgEx=null;
//        if(!flag){
//			//没有要保存的数据
//			
//		}else{
//	        //教师数据先不放
//			//<科目id,班级数> 单科(物理A1班，物理A2班) 
//	        //<科目id,班级数> 单科(物理B1班，物理B2班) 
//	        Map<String,Integer> subjectNumAMap=new HashMap<String, Integer>();
//	        Map<String,Integer> subjectNumBMap=new HashMap<String, Integer>();
//		    
//	        String subjectId = null;
//		    List<Room> roomList = null;
//		    for (int i = 0; i < roomCombineArray.length; i++) {
//		        for (Map.Entry<String, List<Room>> cellBottleEntry : roomCombineArray[i].entrySet()) {
//		            subjectId = cellBottleEntry.getKey();
//		            roomList = cellBottleEntry.getValue();
//		            if(CollectionUtils.isEmpty(roomList)){
//		            	continue;
//		            }
//		            for (Room room : roomList) {
//		            	List<StudentSubjectDto> studentList = room.getStudentList();
//		            	if(CollectionUtils.isEmpty(studentList)){
//		            		continue;
//		            	}
//		            	//批次
//		            	gkBatch=makeNewGkBatch(round, BaseConstants.ZERO_GUID);
//		            	gkBatch.setBatch(i+1);
//		            	//教学班
//		            	
//		            	teachClass=makeNewTeachClass(gkArrange, round, subjectId);
//        				if(BaseConstants.GKELECTIVE_GKTYPE_A.equals(room.getType())){
//        					if(!subjectNumAMap.containsKey(subjectId)){
//    		            		subjectNumAMap.put(subjectId, 1);
//    		            	}
//        					int subNum = subjectNumAMap.get(subjectId);
//        					teachClass.setClassName(subjectNameMap.get(subjectId).getSubjectName()+room.getType()+subNum+(StringUtils.isNotBlank(room.getLevel())?room.getLevel():"")+"班");
//    		            	subjectNumAMap.put(subjectId, subNum+1);
//        				}else{
//        					if(!subjectNumBMap.containsKey(subjectId)){
//        						subjectNumBMap.put(subjectId, 1);
//    		            	}
//        					int subNum = subjectNumBMap.get(subjectId);
//        					teachClass.setClassName(subjectNameMap.get(subjectId).getSubjectName()+room.getType()+subNum+(StringUtils.isNotBlank(room.getLevel())?room.getLevel():"")+"班");
//        					subjectNumBMap.put(subjectId, subNum+1);
//        				}
//		            	
////		            	teachClass.setTeacherId(BaseConstants.ZERO_GUID);
//			    		gkBatch.setClassType(room.getType());
//			    		gkBatch.setTeachClassId(teachClass.getId());	
//			    		//教学班下学生
//			    		List<String> stuIds = new ArrayList<String>();
//			    		for( StudentSubjectDto item:room.getStudentList()){
//			    			teachClassStu=new GkTeachClassStuStore();
//			    			teachClassStu.setId(UuidUtils.generateUuid());
//			    			teachClassStu.setGkClassId(teachClass.getId());
//			    			teachClassStu.setStudentId(item.getStuId());
////			    			teachClassStu.setCreationTime(new Date());
////			    			teachClassStu.setModifyTime(new Date());
////			    			teachClassStu.setIsDeleted(GkElectveConstants.USE_FALSE);
//			    			teachClassStuList.add(teachClassStu);
//			    			stuIds.add(item.getStuId());
//			    		}
//			    		double avg=0;
//		            	avgEx=new GkTeachClassEx();
//		            	if(CollectionUtils.isNotEmpty(stuIds)){
//		            		//求平均分
//		            		avg=avagTeachClass(alldtoMap, stuIds, teachClass.getSubjectId());
//		            	}
//		            	avgEx.setAverageScore(avg);
//		            	avgEx.setTeachClassId(teachClass.getId());
//		            	avgEx.setRoundsId(round.getId());
//		            	avgEx.setId(UuidUtils.generateUuid());
//		            	insertAvg.add(avgEx);
//			    		gkBatchList.add(gkBatch);
//			    		teachClassList.add(teachClass);
//		            }
//	            }
//		       
//	        }
//	        
//		}
//        List<GkGroupClass> updateGroupClass=new ArrayList<GkGroupClass>();
//        List<GkGroupClass> glist = gkGroupClassService.findByRoundsId(round.getId());
//        if(classIdAdditionalSubjectIndexSetMap.size()>0 && CollectionUtils.isNotEmpty(glist)){
//        	for(GkGroupClass g:glist){
//        		if(classIdAdditionalSubjectIndexSetMap.containsKey(g.getId())){
//        			Set<Integer> set = classIdAdditionalSubjectIndexSetMap.get(g.getId());
//        			if(set.size()>0){
//        				String s="";
//        				for(Integer ss:set){
//        					s=s+","+(ss+1);
//        				}
//        				s=s.substring(1);
//        				g.setBatch(s);
//        				updateGroupClass.add(g);
//        			}
//        			
//        		}
//        	}
//        }
//        
//        
//        
//        long start = System.currentTimeMillis();
//		gkBatchService.saveBatchs(round,gkArrange, null, updateGroupClass, gkBatchList, teachClassList, teachClassStuList,insertAvg, false);
//		long end = System.currentTimeMillis();
//		System.out.println("saveBatchs耗时：" + (end-start)/1000 + "s");
//    }
    //只考虑2+x遗留的B
    private void makeBTwo(Map<String,StudentSubjectDto> alldtoMap,GkSubjectArrange gkArrange,GkRounds round,Map<String, List<Room>>[] roomCombineArray,Map<String, Course> subjectNameMap,
    		Map<String, Set<Integer>> classIdAdditionalSubjectIndexSetMap,boolean isUpdateStep,int bachA,String[] subject_all){
    	boolean flag=false;
    	if(roomCombineArray!=null){
    		flag=jadge(roomCombineArray);
    	}
		//需要插入数据库
        List<GkBatch> gkBatchList = new ArrayList<GkBatch>();
        List<GkTeachClassStore> teachClassList = new ArrayList<GkTeachClassStore>();
        List<GkTeachClassStuStore> teachClassStuList = new ArrayList<GkTeachClassStuStore>();
        List<GkTeachClassEx> insertAvg=new ArrayList<GkTeachClassEx>();
        //<科目id,班级数> 单科(物理A1班，物理A2班) 
        //<科目id,班级数> 单科(物理B1班，物理B2班) 
        Map<String,Integer> subjectNumAMap=new HashMap<String, Integer>();
        Map<String,Integer> subjectNumBMap=new HashMap<String, Integer>();
        Map<String,Map<Integer,Integer>> subjectBathNumMap=new HashMap<String,Map<Integer,Integer>>();
        if(!flag){
			//没有要保存的数据
			
		}else{
	        
	        String subjectId = null;
		    List<Room> roomList = null;
		    for (int i = 0; i < roomCombineArray.length; i++) {
		        for (Map.Entry<String, List<Room>> cellBottleEntry : roomCombineArray[i].entrySet()) {
		            subjectId = cellBottleEntry.getKey();
		            roomList = cellBottleEntry.getValue();
		            if(CollectionUtils.isEmpty(roomList)){
		            	continue;
		            }
		            for (Room room : roomList) {
		            	List<StudentSubjectDto> studentList = room.getStudentList();
		            	if(CollectionUtils.isEmpty(studentList)){
		            		continue;
		            	}
		            	Set<String> stuIdSet = EntityUtils.getSet(room.getStudentList(), "stuId");
		            	Map<Integer, Integer> itemMap=subjectBathNumMap.get(room.getSubjectId());
						if(itemMap==null){
							itemMap=new HashMap<Integer,Integer> ();
							subjectBathNumMap.put(room.getSubjectId(), itemMap)	;
		            	}
						if(itemMap.containsKey(i+1)){
							itemMap.put(i+1, itemMap.get(i+1)+1);
						}else{
							itemMap.put(i+1, 1);
						}
		            	toTeachCls(round, gkArrange, subjectId, room.getType(), Arrays.asList(stuIdSet.toArray(new String[]{})), subjectNumAMap, subjectNumBMap, i+bachA+1, subjectNameMap, teachClassStuList, alldtoMap, insertAvg, gkBatchList,teachClassList,BaseConstants.ZERO_GUID);
		            }
	            }
		       
	        }
		   
	        
		}
        long start = System.currentTimeMillis();
        gkBatchService.saveBatchsBySubjectIdType("B", round, gkBatchList, teachClassList, teachClassStuList, insertAvg, isUpdateStep);
		long end = System.currentTimeMillis();
		System.out.println("saveBatchs耗时：" + (end-start)/1000 + "s");
    }
    
    private void makeB(Map<String,StudentSubjectDto> alldtoMap,GkSubjectArrange gkArrange,GkRounds round,Map<String, List<Room>>[] roomCombineArray,Map<String, Course> subjectNameMap,
    		Map<String, Set<Integer>> classIdAdditionalSubjectIndexSetMap,boolean isUpdateStep,int bachA,String[] subject_all){
    	boolean flag=false;
    	if(roomCombineArray!=null){
    		flag=jadge(roomCombineArray);
    	}
		//需要插入数据库
        List<GkBatch> gkBatchList = new ArrayList<GkBatch>();
        List<GkTeachClassStore> teachClassList = new ArrayList<GkTeachClassStore>();
        List<GkTeachClassStuStore> teachClassStuList = new ArrayList<GkTeachClassStuStore>();
        List<GkTeachClassEx> insertAvg=new ArrayList<GkTeachClassEx>();
        //<科目id,班级数> 单科(物理A1班，物理A2班) 
        //<科目id,班级数> 单科(物理B1班，物理B2班) 
        Map<String,Integer> subjectNumAMap=new HashMap<String, Integer>();
        Map<String,Integer> subjectNumBMap=new HashMap<String, Integer>();
        Map<String,Map<Integer,Integer>> subjectBathNumMap=new HashMap<String,Map<Integer,Integer>>();
        if(!flag){
			//没有要保存的数据
			
		}else{
	        
	        String subjectId = null;
		    List<Room> roomList = null;
		    for (int i = 0; i < roomCombineArray.length; i++) {
		        for (Map.Entry<String, List<Room>> cellBottleEntry : roomCombineArray[i].entrySet()) {
		            subjectId = cellBottleEntry.getKey();
		            roomList = cellBottleEntry.getValue();
		            if(CollectionUtils.isEmpty(roomList)){
		            	continue;
		            }
		            for (Room room : roomList) {
		            	List<StudentSubjectDto> studentList = room.getStudentList();
		            	if(CollectionUtils.isEmpty(studentList)){
		            		continue;
		            	}
		            	Set<String> stuIdSet = EntityUtils.getSet(room.getStudentList(), "stuId");
		            	Map<Integer, Integer> itemMap=subjectBathNumMap.get(room.getSubjectId());
						if(itemMap==null){
							itemMap=new HashMap<Integer,Integer> ();
							subjectBathNumMap.put(room.getSubjectId(), itemMap)	;
		            	}
						if(itemMap.containsKey(i+1)){
							itemMap.put(i+1, itemMap.get(i+1)+1);
						}else{
							itemMap.put(i+1, 1);
						}
		            	toTeachCls(round, gkArrange, subjectId, room.getType(), Arrays.asList(stuIdSet.toArray(new String[]{})), subjectNumAMap, subjectNumBMap, i+bachA+1, subjectNameMap, teachClassStuList, alldtoMap, insertAvg, gkBatchList,teachClassList,BaseConstants.ZERO_GUID);
		            }
	            }
		       
	        }
		   
	        
		}
        List<GkGroupClass> glist = gkGroupClassService.findByRoundsIdType(round.getId(),null);
        if(CollectionUtils.isNotEmpty(glist)){
        	for(GkGroupClass g:glist){
        		if(GkGroupClass.GROUP_TYPE_1.equals(g.getGroupType())){
        			if(StringUtils.isNotBlank(g.getSubjectIds())){
        				List<String> noChooseSubjectId=new ArrayList<String>();
        				List<String> subids = Arrays.asList(g.getSubjectIds().split(","));
        				for(String s:subject_all){
            				if(subids.contains(s)){
            					continue;
            				}
            				noChooseSubjectId.add(s);
            			}
        				Set<Integer> set =new HashSet<Integer>();
        				for(int k=0;k<round.getBatchCountB();k++){
    						set.add(k);
    					}
    					if(set.size() < noChooseSubjectId.size()){
    						//不应该存在
            				continue;
            			}
    					Set<Integer> noSet =new HashSet<Integer>();
    					for(int j=0;j<noChooseSubjectId.size();j++){
    						int ii=findMin(noSet,set,subjectBathNumMap.get(noChooseSubjectId.get(j)));
    						if(ii<0){
    							//未找到
    							continue;
    						}
    						toTeachCls(round, gkArrange, noChooseSubjectId.get(j), "B", g.getStuIdList(), subjectNumAMap, subjectNumBMap, ii+bachA, subjectNameMap, teachClassStuList, alldtoMap, insertAvg, gkBatchList,teachClassList,g.getId());
    						noSet.add(ii);
    						
    						Map<Integer, Integer> itemMap=subjectBathNumMap.get(noChooseSubjectId.get(j));
    						if(itemMap==null){
    							itemMap=new HashMap<Integer,Integer> ();
    							subjectBathNumMap.put(noChooseSubjectId.get(j), itemMap)	;
    		            	}
    						if(itemMap.containsKey(ii)){
    							itemMap.put(ii, itemMap.get(ii)+1);
    						}else{
    							itemMap.put(ii, 1);
    						}
    					}
    				}
        		}
        		
        	}
        }
        
       
        long start = System.currentTimeMillis();
        gkBatchService.saveBatchsBySubjectIdType("B", round, gkBatchList, teachClassList, teachClassStuList, insertAvg, isUpdateStep);
		long end = System.currentTimeMillis();
		System.out.println("saveBatchs耗时：" + (end-start)/1000 + "s");
    }
    
    public void toTeachCls(GkRounds round,GkSubjectArrange gkArrange,String subjectId,String subtype,
    		List<String> stuids,Map<String,Integer> subjectNumAMap, Map<String,Integer> subjectNumBMap,int i,
    		Map<String, Course> subjectNameMap,List<GkTeachClassStuStore> teachClassStuList,
    		Map<String,StudentSubjectDto> alldtoMap,
    		List<GkTeachClassEx> insertAvg,List<GkBatch> gkBatchList,List<GkTeachClassStore> teachClassList,
    		String groupClassId){//批次
    	GkBatch gkBatch =makeNewGkBatch(round, groupClassId);
    	gkBatch.setBatch(i);
    	//教学班
    	
    	GkTeachClassStore teachClass = makeNewTeachClass(gkArrange, round, subjectId);
		if(BaseConstants.SUBJECT_TYPE_A.equals(subtype)){
			if(!subjectNumAMap.containsKey(subjectId)){
        		subjectNumAMap.put(subjectId, 1);
        	}
			int subNum = subjectNumAMap.get(subjectId);
			teachClass.setClassName(subjectNameMap.get(subjectId).getSubjectName()+subtype+subNum+"班");
        	subjectNumAMap.put(subjectId, subNum+1);
		}else{
			if(!subjectNumBMap.containsKey(subjectId)){
				subjectNumBMap.put(subjectId, 1);
        	}
			int subNum = subjectNumBMap.get(subjectId);
			teachClass.setClassName(subjectNameMap.get(subjectId).getSubjectName()+subtype+subNum+"班");
			subjectNumBMap.put(subjectId, subNum+1);
		}
		teachClassList.add(teachClass);
    	
//    	teachClass.setTeacherId(BaseConstants.ZERO_GUID);
		gkBatch.setClassType(subtype);
		gkBatch.setTeachClassId(teachClass.getId());	
		//教学班下学生
		List<String> stuIds = new ArrayList<String>();
		for(String stuid:stuids){
			GkTeachClassStuStore teachClassStu = new GkTeachClassStuStore();
			teachClassStu.setId(UuidUtils.generateUuid());
			teachClassStu.setGkClassId(teachClass.getId());
			teachClassStu.setStudentId(stuid);
			teachClassStuList.add(teachClassStu);
			stuIds.add(stuid);
		}
		double avg=0;
    	GkTeachClassEx avgEx = new GkTeachClassEx();
    	if(CollectionUtils.isNotEmpty(stuIds)){
    		//求平均分
    		avg=avagTeachClass(alldtoMap, stuIds, teachClass.getSubjectId());
    	}
    	avgEx.setAverageScore(avg);
    	avgEx.setTeachClassId(teachClass.getId());
    	avgEx.setRoundsId(round.getId());
    	avgEx.setId(UuidUtils.generateUuid());
    	insertAvg.add(avgEx);
		gkBatchList.add(gkBatch);
		
    }
    
    private void makeA(Map<String,StudentSubjectDto> alldtoMap,
    		GkSubjectArrange gkArrange,GkRounds round,
    		Map<String, List<Room>>[] roomCombineArray,Map<String, Course> subjectNameMap,
    		Map<String, Set<Integer>> classIdAdditionalSubjectIndexSetMap,boolean isUpdateStep){
    	boolean flag=false;
    	if(roomCombineArray!=null){
    		flag=jadge(roomCombineArray);
    	}
		//需要插入数据库
        List<GkBatch> gkBatchList = new ArrayList<GkBatch>();
        List<GkTeachClassStore> teachClassList = new ArrayList<GkTeachClassStore>();
        List<GkTeachClassStuStore> teachClassStuList = new ArrayList<GkTeachClassStuStore>();
        List<GkTeachClassEx> insertAvg=new ArrayList<GkTeachClassEx>();
        //<科目id,班级数> 单科(物理A1班，物理A2班) 
        //<科目id,班级数> 单科(物理B1班，物理B2班) 
        Map<String,Integer> subjectNumAMap=new HashMap<String, Integer>();
        Map<String,Integer> subjectNumBMap=new HashMap<String, Integer>();
        //
        Map<String,Map<Integer,Integer>> subjectBathNumMap=new HashMap<String,Map<Integer,Integer>>();
        if(!flag){
			//没有要保存的数据
			
		}else{
	        //教师数据先不放
	        String subjectId = null;
		    List<Room> roomList = null;
		    for (int i = 0; i < roomCombineArray.length; i++) {
		        for (Map.Entry<String, List<Room>> cellBottleEntry : roomCombineArray[i].entrySet()) {
		            subjectId = cellBottleEntry.getKey();
		            roomList = cellBottleEntry.getValue();
		            if(CollectionUtils.isEmpty(roomList)){
		            	continue;
		            }
		            for (Room room : roomList) {
		            	List<StudentSubjectDto> studentList = room.getStudentList();
		            	if(CollectionUtils.isEmpty(studentList)){
		            		continue;
		            	}
		            	Set<String> stuIdSet = EntityUtils.getSet(room.getStudentList(), "stuId");
		            	
		            	toTeachCls(round, gkArrange, subjectId, room.getType(), Arrays.asList(stuIdSet.toArray(new String[]{})),
		            			subjectNumAMap, subjectNumBMap, i+1, subjectNameMap, teachClassStuList, alldtoMap, 
		            			insertAvg, gkBatchList,teachClassList,BaseConstants.ZERO_GUID);
		            	
		            	Map<Integer, Integer> itemMap=subjectBathNumMap.get(room.getSubjectId());
						if(itemMap==null){
							itemMap=new HashMap<Integer,Integer> ();
							subjectBathNumMap.put(room.getSubjectId(), itemMap)	;
		            	}
						if(itemMap.containsKey(i+1)){
							itemMap.put(i+1, itemMap.get(i+1)+1);
						}else{
							itemMap.put(i+1, 1);
						}
		            	
		            }
	            }
	        }
		}
        List<GkGroupClass> glist = gkGroupClassService.findByRoundsIdType(round.getId(),null);
        if(CollectionUtils.isNotEmpty(glist)){
        	for(GkGroupClass g:glist){
        		if(GkGroupClass.GROUP_TYPE_2.equals(g.getGroupType())){
        			if(classIdAdditionalSubjectIndexSetMap!=null && classIdAdditionalSubjectIndexSetMap.containsKey(g.getId())){
            			Set<Integer> set = classIdAdditionalSubjectIndexSetMap.get(g.getId());
            			
            			Set<Integer> noSet=new HashSet<Integer>();
        				if(StringUtils.isNotBlank(g.getSubjectIds())){
        					String[] subids = g.getSubjectIds().split(",");
        					if(set.size() < subids.length){
        						//不应该存在
                				continue;
                			}
        					for(int j=0;j<subids.length;j++){
        						int ii=findMin(noSet,set,subjectBathNumMap.get(subids[j]));
        						if(ii<0){
        							//未找到
        							continue;
        						}
        						toTeachCls(round, gkArrange, subids[j], "A", g.getStuIdList(), subjectNumAMap, subjectNumBMap, ii, subjectNameMap, teachClassStuList, alldtoMap, insertAvg, gkBatchList,teachClassList,g.getId());
        						noSet.add(ii);
        						Map<Integer, Integer> itemMap=subjectBathNumMap.get(subids[j]);
        						if(itemMap==null){
        							itemMap=new HashMap<Integer,Integer> ();
        							subjectBathNumMap.put(subids[j], itemMap)	;
        		            	}
        						if(itemMap.containsKey(ii)){
        							itemMap.put(ii, itemMap.get(ii)+1);
        						}else{
        							itemMap.put(ii, 1);
        						}
        					}
        				}
            		}
        		}else if(GkGroupClass.GROUP_TYPE_1.equals(g.getGroupType())){
        			//asubjectNum  1-subids.length
        			Set<Integer> noSet=new HashSet<Integer>();
        			if(StringUtils.isNotBlank(g.getSubjectIds())){
    					String[] subids = g.getSubjectIds().split(",");
    					Set<Integer> set =new HashSet<Integer>();
    					for(int k=0;k<round.getBatchCountA();k++){
    						set.add(k);
    					}
    					if(set.size() < subids.length){
    						//不应该存在
            				continue;
            			}
    					for(int j=0;j<subids.length;j++){
    						int ii=findMin(noSet,set,subjectBathNumMap.get(subids[j]));
    						if(ii<0){
    							//未找到
    							continue;
    						}
    						toTeachCls(round, gkArrange, subids[j], "A", g.getStuIdList(), subjectNumAMap, subjectNumBMap, ii, subjectNameMap, teachClassStuList, alldtoMap, insertAvg, gkBatchList,teachClassList,g.getId());
    						noSet.add(ii);
    						Map<Integer, Integer> itemMap=subjectBathNumMap.get(subids[j]);
    						if(itemMap==null){
    							itemMap=new HashMap<Integer,Integer> ();
    							subjectBathNumMap.put(subids[j], itemMap);
    		            	}
    						if(itemMap.containsKey(ii)){
    							itemMap.put(ii, itemMap.get(ii)+1);
    						}else{
    							itemMap.put(ii, 1);
    						}
    					}
    				}
        		}
        		
        	}
        }
        
       
        long start = System.currentTimeMillis();
        gkBatchService.saveBatchsBySubjectIdType("A", round, gkBatchList, teachClassList, teachClassStuList, insertAvg, isUpdateStep);
		long end = System.currentTimeMillis();
		System.out.println("saveBatchs耗时：" + (end-start)/1000 + "s");
    }
    
    /**
	 * 
	 * @param subjectMoveListMap
	 *            多余的cell需要移动
	 * @param bottleArray
	 *            已经剥离多余的cell
	 */
//	private  void cross(Map<String, List<Move>> subjectMoveListMap,String[] subjectArr) {
//		List<Move> moveList = null;
//		Room cell = null;
//		List<StudentSubjectDto> studentList = null;
//
//		Set<String> stuIdExchangeSet = new HashSet<String>();
//		for (String subjectId : subjectArr) {
//			if (!subjectMoveListMap.containsKey(subjectId)) {
//				continue;
//			}
//			moveList = subjectMoveListMap.get(subjectId);
//			for (Move move : moveList) {
//				cell = move.getRoom();
//				if (cell == null) {
//					continue;
//				}
//				studentList = cell.getStudentList();
//				for (StudentSubjectDto student : studentList) {
//					if (!stuIdExchangeSet.contains(student.getStuId() + "_" + move.getFrom())
//							&& !stuIdExchangeSet.contains(student.getStuId() + "_" + move.getTo())) {
//						Collections.swap(student.getAllSubjectIds(), move.getFrom(), move.getTo());
//						stuIdExchangeSet.add(student.getStuId() + "_" + move.getFrom());
//						stuIdExchangeSet.add(student.getStuId() + "_" + move.getTo());
//					}
//				}
//			}
//		}
//	}
    
    /**
	 * 
	 * @param bottleArray
	 * @param subjectSet
	 * @param subjectBatchBalanceMap
	 * @param subject_all 所排的科目
	 * @return
	 */
//	private Map<String, List<Move>> select(int subjectNum,Map<String, List<Room>>[] bottleArray, Map<String, Integer[]> subjectBatchBalanceMap,String[] subjectArr) {
//		// 实际批次课程 8 ，0， 0； 7，1，0； 6，1，1 / 1,0,0 ; 2,1,1 / 1,1,0 ; 1,1,1
//		Map<String, Integer[]> subjectBatchCountMap = new HashMap<String, Integer[]>();
//
//		Map<String, List<Room>> cellBottle = null;
//		List<Room> cellList = null;
//		Integer[] subjectCountArray = null;
//		
//		
//		for (String subjectId : subjectArr) {
//			for (int i = 0; i < subjectNum; i++) {
//				cellBottle = bottleArray[i];
//				cellList = cellBottle.get(subjectId);
//				if (!subjectBatchCountMap.containsKey(subjectId)) {
//					subjectCountArray = new Integer[subjectNum];
//					subjectBatchCountMap.put(subjectId, subjectCountArray);
//				} else {
//					subjectCountArray = subjectBatchCountMap.get(subjectId);
//				}
//				if (cellList == null) {
//					subjectCountArray[i] = 0;
//				} else {
//					subjectCountArray[i] = cellList.size();
//				}
//			}
//		}
//		Integer[] balanceBatchArray = null;
//		String subjectId = null;
//		List<Move> moveList = null;
//		Map<String, List<Move>> subjectMoveListMap = new HashMap<String, List<Move>>();
//
//		Set<String> involvedSubjects = new HashSet<String>();
//		List<StudentSubjectDto> students = null;
//		for (Map.Entry<String, Integer[]> subjectBatchBalanceEntry : subjectBatchBalanceMap.entrySet()) {
//			subjectId = subjectBatchBalanceEntry.getKey();
//
//			if (!involvedSubjects.contains(subjectId)) {
//				balanceBatchArray = subjectBatchBalanceEntry.getValue();
//				subjectCountArray = subjectBatchCountMap.get(subjectId);
//				moveList = getMoveList(balanceBatchArray, subjectCountArray, bottleArray, subjectId,subjectNum);
//				subjectMoveListMap.put(subjectId, moveList);
//
//				for (Move move : moveList) {
//					if (move.getRoom() != null) {
//						students = move.getRoom().getStudentList();
//						for (StudentSubjectDto stu : students) {
//							involvedSubjects.add(stu.getAllSubjectIds().get(move.getFrom()));
//							involvedSubjects.add(stu.getAllSubjectIds().get(move.getTo()));
//						}
//					}
//				}
//			}
//		}
//		return subjectMoveListMap;
//	}
	
	// 实际批次课程 8 ，0， 0； 7，1，0； 6，1，1 / 1,0,0 ; 1,1,1 / 1,1,0 ; 1,1,1 / 1,0,1 ;
			// 1,1,0 / 0,0,1 ; 1,1,0
//	private List<Move> getMoveList(Integer[] base, Integer[] test, Map<String, List<Room>>[] bottleArray, String subjectId,int subjectNum) {
//		int[] a = new int[base.length];
//		for (int i = 0; i < base.length; i++) {
//			a[i] = test[i] - base[i];
//		}
//
//		int count = 0;
//		Map<Integer, Integer> batchFromMap = new HashMap<Integer, Integer>();
//		List<Move> moveList = new ArrayList<Move>();
//		boolean isSet = false;
//		for (int i = 0; i < a.length;) {
//			if (a[i] < 0) {
//				// borrow from other
//				isSet = false;
//				for (int j = 0; j < a.length; j++) {
//					if (a[j] > 0) {
//						Move move = new Move();
//						move.setFrom(j);
//						move.setTo(i);
//						moveList.add(move);
//
//						if (batchFromMap.containsKey(move.getFrom())) {
//							count = batchFromMap.get(move.getFrom());
//						} else {
//							count = 0;
//						}
//						count++;
//						batchFromMap.put(move.getFrom(), count);
//
//						isSet = true;
//						a[i] = a[i] + 1;
//						a[j] = a[j] - 1;
//						break;
//					}
//				}
//				if (!isSet) {
//					break;
//				}
//			} else {
//				i++;
//			}
//		}
//
//		Map<String, List<Room>> cellBottle = null;
//		List<Room> cellList = null;
//		Map<Integer, Set<Room>> toBeRemoved = new HashMap<Integer, Set<Room>>();
//		for (int i = 0; i < subjectNum; i++) {
//			if (batchFromMap.containsKey(i)) {
//				count = batchFromMap.get(i);
//				cellBottle = bottleArray[i];
//				cellList = cellBottle.get(subjectId);
//
//				if (cellList != null) {
//					toBeRemoved.put(i, new HashSet<Room>(subList(cellList, cellList.size() - count)));
//					cellBottle.put(subjectId, subList(cellList, 0, cellList.size() - count));
//				}
//			}
//		}
//
//		Set<Room> cellSet = null;
//		for (Move move : moveList) {
//			if (toBeRemoved.containsKey(move.getFrom())) {
//				cellSet = toBeRemoved.get(move.getFrom());
//				if (cellSet.size() > 0) {
//					move.setRoom(cellSet.iterator().next());
//					cellSet.remove(move.getRoom());
//					if (cellSet.size() == 0) {
//						toBeRemoved.remove(move.getFrom());
//					}
//				}
//			}
//		}
//		return moveList;
//	}
    /**
     * 初始数据
     * @param students
     * @param singleSubjectCountAMap 班级人数限制
     * @param singleSubjectCountBMap 班级人数限制
     * @param subjectNum 批次数
     * @return
     */
//    private  Map<String, List<Room>>[] singleIteration(boolean isAB,List<StudentSubjectDto> students,Map<String, Integer> singleSubjectCountAMap,
//    		Map<String, Integer> singleSubjectCountBMap,int subjectNum) {
//		Map<String, List<Room>>[] cellBottleArray = new HashMap[subjectNum];
//		for (int i = 0; i<subjectNum; i++) {
//			cellBottleArray[i] = new HashMap<String, List<Room>>();
//		}
//		Map<String, List<Room>> nowBottle = null;
//		// 一个科目
//		List<Room> roomsBySubject = null;
//		List<StudentSubjectDto> studentInCellList = null;
//		int cellNumIndex = 1;
//		for (StudentSubjectDto student : students) {
//			int batch = 0;
//			for (String subjectId : student.getAllSubjectIds()) {
//				//取得大于等于batch的值中且不再student.getNoBathSet()中的最小值
//				batch=smallestIndex(batch,student.getNoBathSet());
//				if(subjectId==null){
//					continue;
//				}
//				if(!isAB){
//					if(!student.getChooseSubjectIds().contains(subjectId)){
//						continue;
//					}
//				}
//				
//				nowBottle = cellBottleArray[batch];
//				if (nowBottle == null) {
//					nowBottle = new HashMap<String, List<Room>>();
//					cellBottleArray[batch] = nowBottle;
//				}
//
//				roomsBySubject = nowBottle.get(subjectId);
//				if (roomsBySubject == null) {
//					roomsBySubject = new ArrayList<Room>();
//					nowBottle.put(subjectId, roomsBySubject);
//				}
//				boolean isPut = false;
//				for (Room room : roomsBySubject) {
//					studentInCellList = room.getStudentList();
//					if (studentInCellList == null) {
//						studentInCellList = new ArrayList<StudentSubjectDto>();
//						room.setStudentList(studentInCellList);
//					}
//					
//					if((BaseConstants.GKELECTIVE_GKTYPE_A.equals(room.getType()) 
//							&& student.getChooseSubjectIds().contains(room.getSubjectId()) 
//						&& room.getStudentList().size() < singleSubjectCountAMap.get(subjectId))
//						||(BaseConstants.GKELECTIVE_GKTYPE_B.equals(room.getType()) 
//							&& !student.getChooseSubjectIds().contains(room.getSubjectId()) 
//						&& room.getStudentList().size() < singleSubjectCountBMap.get(subjectId)
//						)){
//						studentInCellList.add(student);
//						isPut = true;
//						break;
//					}
//					
//				}
//				if (!isPut) {
//					Room room = new Room();
//					room.setBatch(batch);
//					room.setSubjectId(subjectId);
//					room.setNumber(cellNumIndex);
//					studentInCellList = new ArrayList<StudentSubjectDto>();
//					studentInCellList.add(student);
//					if (student.getChooseSubjectIds().contains(subjectId)) {
//						room.setType(BaseConstants.GKELECTIVE_GKTYPE_A); // 选考
//					} else {
//						room.setType(BaseConstants.GKELECTIVE_GKTYPE_B); // 学考
//					}
//					room.setStudentList(studentInCellList);
//					roomsBySubject.add(room);
//					cellNumIndex++;
//				}
//				batch++;
//			}
//		}
//		return cellBottleArray;
//	}
    
    
    /**
     * 平衡批次课程 科目批次 8= 3+3+2
     * @param subjectTotalMap
     * @return
     */
//    private Map<String, Integer[]> getSuitableSubjectBatchArray(Map<String, Integer> subjectTotalMap,int subjectNum) {
//		String subjectId = null;
//		int total = 0;
//		Map<String, Integer[]> subjectBatchCountMap = new HashMap<String, Integer[]>();
//		// 每批次的总课程数
//		for (Map.Entry<String, Integer> subjectTotalEntry : subjectTotalMap.entrySet()) {
//			subjectId = subjectTotalEntry.getKey();
//			total = subjectTotalEntry.getValue();
//			subjectBatchCountMap.put(subjectId, ArrayUtils.toObject(getBatchArray(subjectNum, total)));
//		}
//		return subjectBatchCountMap;
//	}
    /**
	 * 8分3份 = 3，3，2
	 * 
	 * @param total
	 * @return
	 */
//	private int[] getBatchArray( int subjectNum, int total) {
//		// 余数
//		int remainder = 0;
//		// 商
//		int quotient = 0;
//		// 每批次的总课程数
//		int[] alreadySet = new int[subjectNum];
//		quotient = total / subjectNum;
//		remainder = total % subjectNum;
//		// 一门课的每批次的课程数
//		Integer[] batchArray = new Integer[subjectNum];
//		for (int i = 0; i < subjectNum; i++) {
//			batchArray[i] = quotient;
//		}
//		int index = 0;
//		if (remainder > 0) {
//			index = smallestIndex(alreadySet);
//			while (remainder > 0) {
//				batchArray[(index + subjectNum) % subjectNum] = batchArray[(index + subjectNum) % subjectNum] + 1;
//				remainder--;
//				index++;
//			}
//		}
//		for (int i = 0; i < subjectNum; i++) {
//			alreadySet[i] = alreadySet[i] + batchArray[i];
//		}
//		return alreadySet;
//	}
    
    
//    private List<StudentSubjectDto> initSingleStudents2(boolean isClass,boolean isAB,int subjectNum,String[] subject_all,
//    		GkSubjectArrange gkArrange,String roundsId,List<StudentSubjectDto> allStudents) {
//        List<StudentSubjectDto> returnstudents = new ArrayList<StudentSubjectDto>();
//        List<String> arrangeStuId=new ArrayList<String>();
//        if(CollectionUtils.isEmpty(allStudents)){
//        	return returnstudents;
//        }
//        if(isClass){
//        	//没有安排到组合班级的学生排除
//        	//排除3+0
//        	Map<String, StudentSubjectDto> map = EntityUtils.getMap(allStudents, "stuId");
//        	List<GkGroupClass> allgrouplist = gkGroupClassService.findByRoundsIdType(roundsId,null);
//        	
//        	List<GkGroupClass> grouplist2=new ArrayList<GkGroupClass>();//混合
//        	
////        	List<GkGroupClass> grouplist1 = gkGroupClassService.findByRoundsIdType(roundsId,GkElectveConstants.GROUP_TYPE_2);
//            if(CollectionUtils.isNotEmpty(allgrouplist)){
//            	for(GkGroupClass gg:allgrouplist){
//            		if(GkElectveConstants.GROUP_TYPE_3.equals(gg.getGroupType())){
//            			grouplist2.add(gg);
//            			continue;
//            		}
//            		boolean flag=false;
//            		if(GkElectveConstants.GROUP_TYPE_2.equals(gg.getGroupType())){
//            			flag=true;
//            		}
//            		String[] subs = gg.getSubjectIds().split(",");
//            		if(CollectionUtils.isNotEmpty(gg.getStuIdList())){
//            			for(String s:gg.getStuIdList()){
//            				if(map.containsKey(s)){
//            					StudentSubjectDto sss = map.get(s);
//            					if(sss.getChooseSubjectIds().size()<=0){
//            	        			//查的是选课表 所以选课为空的可能性没有
//            						throw new RuntimeException("学生选课数据有所调整，导致手动排班组合中，学生数据有错误！");
//            	        		}else{
//        	        				if(!(CollectionUtils.union(sss.getChooseSubjectIds(), Arrays.asList(subs)).size()==sss.getChooseSubjectIds().size())){
//        	        					throw new RuntimeException("学生选课数据有所调整，导致手动排班组合中，学生数据有错误！");
//        	        				}
//            	        		}
//            				}else{
//            					throw new RuntimeException("学生选课数据有所调整，导致手动排班组合中，学生数据有错误！");
//            				}
//            				if(flag){
//            					arrangeStuId.add(s);
//            				}
//            			}
//            			//arrangeStuId.addAll(gg.getStuIdList());
//                	}
//            	}
//            	
//            }
//            //List<GkGroupClass> grouplist2 = gkGroupClassService.findByRoundsIdType(roundsId,GkElectveConstants.GROUP_TYPE_3);
//            if(CollectionUtils.isNotEmpty(grouplist2)){
//            	for(GkGroupClass gg:grouplist2){
//            		if(CollectionUtils.isNotEmpty(gg.getStuIdList())){
//            			arrangeStuId.addAll(gg.getStuIdList());
//                	}
//            	}
//            	
//            }
//        }
//        
//        //String stuId=null;
//        List<String> alls=null;
//        String[] subject_all_self=null;
//        if(CollectionUtils.isNotEmpty(allStudents)){
//        	for(StudentSubjectDto dto:allStudents){
//        		if(isClass && !arrangeStuId.contains(dto.getStuId())){
//        			continue;
//        		}
//        		//需要排的
//        		alls=new ArrayList<String>();
//        		//stuId = dto.getStuId();
//        		if(dto.getChooseSubjectIds().size()<=0){
//        			//查的是选课表 所以选课为空的可能性没有
//        			continue;
//        		}
//        		
//        		//去除不走班科目
//    			subject_all_self = moveSubject(dto.getChooseSubjectIds(),subject_all);
////    			if(subject_all_self==null || subject_all_self.length<=0){
////    				dto.setChooseSubjectIds(new HashSet<String>());
////    			}else{
////    				dto.setChooseSubjectIds(new HashSet<String>(Arrays.asList(subject_all_self)));
////    			}
//        		if(!isAB){
//        			//只开A
//        			if(subject_all_self==null || subject_all_self.length<=0){
//        				continue;
//        			}
//        		}else{
//        			subject_all_self=subject_all;
//        		}
//        		for(String subject:subject_all_self){
//        			//TODO===
////        			if(dto.getChooseSubjectIds().contains(subject)){
////        				continue;
////        			}
//        			//===
//        			alls.add(subject);
//        		}
//        		
//        		if(CollectionUtils.isNotEmpty(alls)){
//        			//不够批次+null
//        			addNull(alls, subjectNum);
//        			dto.setCombined(null);
//        			dto.setAllSubjectIds(alls);
//        			//计算平均分
//        			//求3门平均分
////                	 Map<String, Double> scoreMap = dto.getScoreMap();
////                	 if(scoreMap==null || scoreMap.size()<=0){
////                		 dto.setAvgScore(0);
////                	 }else{
////                		double sum=0;
////	             		 for(String key:scoreMap.keySet()){
////	             			Double dd = scoreMap.get(key);
////	             			if(dto.getChooseSubjectIds().contains(key)){
////	             				if(dd!=null){
////		             				sum=sum+dd;
////		             			}
////	             			}
////	             		 }
////	             		dto.setAvgScore(sum/subjectNum);
////                	 }
//        			returnstudents.add(dto);
//        		}
//        	}
//        }
//        if(CollectionUtils.isNotEmpty(returnstudents)){
//        	 Collections.sort(returnstudents, new Comparator<StudentSubjectDto>() {
//     			@Override
//     			public int compare(StudentSubjectDto o1, StudentSubjectDto o2) {
//     				return o1.getClassId().compareTo(o2.getClassId());
//     			}
//     		});
//        }else{
//        	return returnstudents;
//        }
//        return returnstudents;
//    }
	/**
     * 获取单科学生（总共学生，排除已经排的学生）
     * @param isAB 是否AB都开  false:只开A
     * @param subjectNum 批次数
     * @param subject_all 走班科目
     * @param gkArrange
     * @param roundsId
     * @param sortType
     * @return
     */
//    private List<StudentSubjectDto> initSingleStudents(boolean isAB,int subjectNum,String[] subject_all,
//    		GkSubjectArrange gkArrange,String roundsId,String[] sortType,List<StudentSubjectDto> allStudents) {
//        List<StudentSubjectDto> returnstudents = new ArrayList<StudentSubjectDto>();
//        //已排学生---单科前就是组合 所以只要存在就说明已经排啦
//        Map<String, String> stu_class_bath = gkResultService.findCombineStudentMap(roundsId);
//        String stuId=null;
//        List<String> alls=null;
//        Set<Integer> noBathSet=null;
//        String[] subject_all_self=null;
//        if(CollectionUtils.isNotEmpty(allStudents)){
//        	for(StudentSubjectDto dto:allStudents){
//        		//不能排的批次
//        		noBathSet=new HashSet<Integer>();
//        		//需要排的
//        		alls=new ArrayList<String>();
//        		stuId = dto.getStuId();
//        		if(dto.getChooseSubjectIds().size()<=0){
//        			//查的是选课表 所以选课为空的可能性没有
//        			continue;
//        		}
//        		if(!isAB){
//        			//只开A
//        			//去除不走班科目
//        			subject_all_self = moveSubject(dto.getChooseSubjectIds(),subject_all);
//        			if(subject_all_self==null || subject_all_self.length<=0){
//        				continue;
//        			}
//        		}else{
//        			subject_all_self=subject_all;
//        		}
//        		for(String subject:subject_all_self){
//        			if(!stu_class_bath.containsKey(stuId+"_"+subject)){
//        				alls.add(subject);
//        			}else{
//        				int a=Integer.parseInt(stu_class_bath.get(stuId+"_"+subject).split("_")[1]);
//        				noBathSet.add(a-1);
//        			}
//        		}
//        		if(CollectionUtils.isNotEmpty(alls)){
//        			//不够批次+null
//        			addNull(alls, subjectNum);
//        			dto.setCombined(null);
//        			dto.setAllSubjectIds(alls);
//        			//dto.setNoBathSet(noBathSet);
//        			//计算平均分
//        			//求3门平均分
//                	 Map<String, Double> scoreMap = dto.getScoreMap();
//                	 if(scoreMap==null || scoreMap.size()<=0){
//                		 dto.setAvgScore(0);
//                	 }else{
//                		double sum=0;
//	             		 for(String key:scoreMap.keySet()){
//	             			Double dd = scoreMap.get(key);
//	             			if(dto.getChooseSubjectIds().contains(key)){
//	             				if(dd!=null){
//		             				sum=sum+dd;
//		             			}
//	             			}
//	             		 }
//	             		dto.setAvgScore(sum/subjectNum);
//                	 }
//        			returnstudents.add(dto);
//        		}
//        	}
//        }
//        if(CollectionUtils.isNotEmpty(returnstudents)){
//        	 Collections.sort(returnstudents, new Comparator<StudentSubjectDto>() {
//     			@Override
//     			public int compare(StudentSubjectDto o1, StudentSubjectDto o2) {
//     				return o1.getClassId().compareTo(o2.getClassId());
//     			}
//     		});
//        }else{
//        	return returnstudents;
//        }
        
        
private int findMin(Set<Integer> noSet, Set<Integer> set,
			Map<Integer, Integer> map) {
		int minCount=-1;
		int min=-1;
		for(Integer ff:set){
			int hh=ff+1;
			if(noSet.contains(hh)){
				continue;
			}
			
			if(map!=null && map.containsKey(hh)){
				if(minCount<0){
					minCount=map.get(hh);
					min=hh;
				}else{
					if(map.get(hh)<minCount){
						minCount=map.get(hh);
						min=hh;
					}
				}
			}else{
				if(minCount<0){
					minCount=0;
					min=hh;
				}
			}
		}
		return min;
	}
	//        if(sortType!=null && sortType.length>0){
//        	if(ARRANGE_TYPE_XINGBIE.equals(sortType[0])){
//        		//返回值(男女间隔) 2017年6月14日 暂时写死
////        		int roomLimit = gkArrange.getClaNum();
//        		int roomLimit = 50;
//    			if(roomLimit<=0){
//    				throw new RuntimeException("没有每班默认人数");
//    			}
//    			List<StudentSubjectDto> studentsMaleTemp;
//    			List<StudentSubjectDto> studentsFeMaleTemp;
//    			studentsMaleTemp = new ArrayList<StudentSubjectDto>();
//    			studentsFeMaleTemp = new ArrayList<StudentSubjectDto>();
//    			for (StudentSubjectDto student2 : returnstudents) {
//    				if (student2.getSex() == GkElectveConstants.MALE) {
//    					// 男
//    					studentsMaleTemp.add(student2);
//    				} else {
//    					studentsFeMaleTemp.add(student2);
//    				}
//    			}
//    			boolean isScore=false;//是否按成绩
//    			
//        		if(sortType.length>1){
//        			//按性别
//        			if(ARRANGE_TYPE_SCORE.equals(sortType[1])){
//        				isScore=true;
//                	}else if(ARRANGE_TYPE_BANJI.equals(sortType[1])){
//                		//按班级---略
//                	}
//        		}
//        		//按性别
//        		if(isScore){
//        			sortStuScore(studentsMaleTemp);
//        			sortStuScore(studentsFeMaleTemp);
//        		}
//        	
//    			List<StudentSubjectDto> returnstudents2 = new ArrayList<StudentSubjectDto>();
//    			int femaleCount = 0;
//    			int maleCount = 0;
//
//    			int maleStartIndex = 0;
//    			int femaleStartIndex = 0;
//    			boolean maleNoLeft = false;
//    			boolean femaleNoLeft = false;
//    			femaleCount = roomLimit * studentsFeMaleTemp.size() / (studentsMaleTemp.size() + studentsFeMaleTemp.size());
//    			maleCount = roomLimit - femaleCount;
//    			//比例算出几:0
//    			if(femaleCount<=0){
//    				femaleCount=1;
//    				maleCount=maleCount-1;
//    			}
//    			if(maleCount<=0){
//    				maleCount=1;
//    			}
//    			maleStartIndex = 0;
//    			femaleStartIndex = 0;
//    			maleNoLeft = false;
//    			femaleNoLeft = false;
//    			while (true) {
//    				if (maleStartIndex < studentsMaleTemp.size()) {
//    					if (maleStartIndex + maleCount < studentsMaleTemp.size()) {
//    						returnstudents2.addAll(subList(studentsMaleTemp, maleStartIndex, maleStartIndex + maleCount));
//    					} else {
//    						returnstudents2.addAll(subList(studentsMaleTemp, maleStartIndex));
//    						maleNoLeft = true;
//    					}
//    					maleStartIndex += maleCount;
//    				} else {
//    					maleNoLeft = true;
//    				}
//    				if (femaleStartIndex < studentsFeMaleTemp.size()) {
//    					if (femaleStartIndex + femaleCount < studentsFeMaleTemp.size()) {
//    						returnstudents2.addAll(subList(studentsFeMaleTemp, femaleStartIndex, femaleStartIndex + femaleCount));
//    					} else {
//    						returnstudents2.addAll(subList(studentsFeMaleTemp, femaleStartIndex));
//    						femaleNoLeft = true;
//    					}
//    					femaleStartIndex += femaleCount;
//    				} else {
//    					femaleNoLeft = true;
//    				}
//    				if (maleNoLeft && femaleNoLeft) {
//    					break;
//    				}
//    			}
//        		return returnstudents2;
//        	}else if(ARRANGE_TYPE_SCORE.equals(sortType[0])){
//        		//如果下面有性别排序则先不管
//        		//按成绩
//        		sortStuScore(returnstudents);
//        		return returnstudents;
//        	}else if(ARRANGE_TYPE_BANJI.equals(sortType[0])){
//        		//如果下面有性别,成绩排序则先不管
//        		//按班级---上面排序已经排啦
//        		
//        	}
//        	return returnstudents;
//        } 
//        return returnstudents;
//    }
    /**
     * 去除不在subject_all的chooseSubjectIds
     * @param chooseSubjectIds
     * @param subject_all
     * @return
     */
    private String[] moveSubject(Set<String> chooseSubjectIds,
			String[] subject_all) {
		List<String> r=new ArrayList<String>();
		List<String> l = Arrays.asList(subject_all);
		for(String s:chooseSubjectIds){
			if(l.contains(s)){
				r.add(s);
			}
		}
		if(CollectionUtils.isNotEmpty(r)){
			return r.toArray(new String[0]);
		}
		return null;
	}
	/**
     * 获得本次选课 单科开班的各科目的人数限制
     * @param subjectClaNumAMap 单科开班的各科目的人数限制A
     * @param subjectClaNumBMap 单科开班的各科目的人数限制B
     * @param singleSubjectComIdAMap单科开班 subjectId与组合id一一对应 (null 则不需要对这个对象赋值)A
     * @param singleSubjectComIdBMap单科开班 subjectId与组合id一一对应 (null 则不需要对这个对象赋值)B
     * @return
     */
//    private Map<String,Integer> getSingleSubjectClassNum(
//    		String roundsId,
//    		Map<String,Integer> subjectClaNumAMap,
//    		Map<String,Integer> subjectClaNumBMap){
//    	List<GkConditionDto> condList = gkConditionService.findByGkConditionDtos(roundsId,GkElectveConstants.GKCONDITION_SINGLE_0);
//    	Map<String,Integer> subjectClaNumMap=new HashMap<String, Integer>();
//    	if(CollectionUtils.isNotEmpty(condList)){
//    		for(GkConditionDto gk:condList){
//    			if(BaseConstants.GKELECTIVE_GKTYPE_A.equals(gk.getGkType())){
//    				Set<String> subjectIds = gk.getSubjectIds();
//        			//单科排班 subjectIds最多只有一个值
//        			if(subjectIds==null || subjectIds.size()!=1){
//        				//这种属于数据问题，先不予考虑
//        				continue;
//        			}
//        			subjectClaNumAMap.put(gk.getSubjectIds().toArray(new String[0])[0], gk.getNum());
//    			}else if(BaseConstants.GKELECTIVE_GKTYPE_B.equals(gk.getGkType())){
//    				Set<String> subjectIds = gk.getSubjectIds();
//        			//单科排班 subjectIds最多只有一个值
//        			if(subjectIds==null || subjectIds.size()!=1){
//        				//这种属于数据问题，先不予考虑
//        				continue;
//        			}
//        			subjectClaNumBMap.put(gk.getSubjectIds().toArray(new String[0])[0], gk.getNum());
//    			}
//    			
//    		}
//    	}
//    	return subjectClaNumMap;
//    } 
    
    /**
	 * 判断roomCombineArray是不是为空 是返回true
	 * @param roomCombineArray
	 * @return
	 */
	private boolean jadge(Map<String, List<Room>>[] roomCombineArray){
		//如果room没有
        boolean flag=false;
        if(roomCombineArray==null || roomCombineArray.length==0){
        	flag=false;
        }else{
        	for (int i = 0; i < roomCombineArray.length; i++) {
	        	Map<String, List<Room>> arrMap = roomCombineArray[i];
	        	if(arrMap==null || arrMap.size()<=0){
	        		continue;
	        	}
	        	for(String key:arrMap.keySet()){
	        		if(CollectionUtils.isNotEmpty(arrMap.get(key))){
	        			flag=true;
	        			break;
	        		}
	        	}
	        	if(flag){
	        		break;
	        	}
	        }
        }
        return flag;
	}
    
    
 
    /**
     * 不够填null
     * @param subjectIds
     * @param subjectNum
     */
    private void addNull(List<String> subjectIdsList, int subjectNum) {
    	if(subjectIdsList.size()<subjectNum){
			for(int i=subjectIdsList.size();i<subjectNum;i++){
				subjectIdsList.add(null);
			}
		}
	}
    /**
	 * 
	 * @param list
	 * @param startIndex
	 *            inclusive
	 * @return
	 */
//	private static <T> List<T> subList(List<T> list, int startIndex) {
//		if (list == null) {
//			throw new RuntimeException("list cannot be null!");
//		}
//		if (startIndex > list.size()) {
//			throw new IndexOutOfBoundsException("start index out of size!");
//		}
//
//		List<T> newList = new ArrayList<T>();
//		for (int i = startIndex; i < list.size(); i++) {
//			newList.add(list.get(i));
//		}
//		return newList;
//	}

	/**
	 * 
	 * @param list
	 * @param startIndex
	 *            inclusive
	 * @param endIndex
	 *            exclusive
	 * @return
	 */
//	private static <T> List<T> subList(List<T> list, int startIndex, int endIndex) {
//		if (list == null) {
//			throw new RuntimeException("list cannot be null!");
//		}
//		if (startIndex > list.size()) {
//			throw new IndexOutOfBoundsException("start index out of size!");
//		}
//		if (endIndex > list.size()) {
//			throw new IndexOutOfBoundsException("end index out of size!");
//		}
//		List<T> newList = new ArrayList<T>();
//		for (int i = startIndex; i < endIndex; i++) {
//			newList.add(list.get(i));
//		}
//		return newList;
//	}
//	// 最小的索引
//	private static int smallestIndex(int[] array) {
//		int index = 0;
//		int smallest = array[0];
//		for (int i = 1; i < array.length; i++) {
//			if (array[i] < smallest) {
//				smallest = array[i];
//				index = i;
//			}
//		}
//		return index;
//	}
	// 最小的索引
//	private static int smallestIndex(int[] array, List<Integer> notInIndexs) {
//		int index = 0;
//		if (array.length == notInIndexs.size()) {
//			return index;
//		}
//		Collections.sort(notInIndexs);
//		for (Integer integer : notInIndexs) {
//			if (integer != index) {
//				break;
//			}
//			index++;
//		}
//		int smallest = array[index];
//
//		for (int i = index + 1; i < array.length; i++) {
//			if (array[i] < smallest && !notInIndexs.contains(i)) {
//				smallest = array[i];
//				index = i;
//			}
//		}
//		return index;
//	}
//	private static int smallestIndex(int index, Set<Integer> notInIndexs) {
//		if(notInIndexs==null || notInIndexs.size()<0){
//			return index;
//		}
//		while(true){
//			if(!notInIndexs.contains(index)){
//				break;
//			}
//			index++;
//		}
//		return index;
//	}
	
//	@ResponseBody
//	@RequestMapping("/openClassArrange/classteacher/save")
//	@ControllerInfo(value = "分配教师，保存到课程表")
//	public String makeTeacher(@PathVariable String roundsId){
//		Map<String, Set<String>> subjectteacher = initSubjectIdTeachers(roundsId);
//		try{
//			GkRounds round = gkRoundsService.findRoundById(roundsId);
//			if(round==null){
//				return error("该选课系统对应这个轮次不存在");
//			}
//			GkSubjectArrange gkArrange=gkSubjectArrangeService.findArrangeById(round.getSubjectArrangeId());
//			if(gkArrange==null){
//				return error("该选课系统不存在");
//			}
//			String errorStr =gkBatchService.saveAllotTeacher(round,gkArrange,subjectteacher);
//			if(StringUtils.isNotBlank(errorStr)){
//				return error(errorStr);
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//			return returnError("自动分配教师失败！", e.getMessage());
//		}
//		return success("自动分配教师成功");
//	}
	
	
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/openClassArrange/autoOpenClass")
    @ControllerInfo(value = "自动分班")
	public String autoOpenClass(@PathVariable String roundsId,String subjectIds,int openNum,String[] stuids,ModelMap map){
		try {
			if(StringUtils.isBlank(subjectIds)){
				return error("没有选中组合");
			}
			if(openNum<=0){
				return error("开设班级数应为正整数");
			}
			
			Set<String> subjectIdSet=new HashSet<String>();
			String[] subjectIdArr = subjectIds.split(",");
			for(String s1:subjectIdArr){
				if(StringUtils.isNotBlank(s1)){
					subjectIdSet.add(s1);
				}
			}
			GkRounds rounds=gkRoundsService.findRoundById(roundsId);
			if(rounds==null){
				addErrorFtlOperation(map, "返回", "/gkelective/arrange/list/page","#showList");
		        return errorFtl(map, "未找到对应7选3系统或应轮次！");
			}
			GkSubjectArrange gkSubjectArrange = gkSubjectArrangeService.findArrangeById(rounds.getSubjectArrangeId());
			if(gkSubjectArrange == null){
				addErrorFtlOperation(map, "返回", "/gkelective/arrange/list/page","#showList");
		        return errorFtl(map, "未找到对应7选3系统或应轮次！");
			}
			List<Course> courseList = SUtils.dt(
					courseRemoteService.findListByIds(subjectIdSet.toArray(new String[0])),
					new TR<List<Course>>() {});
			Map<String,Course> subjectNameMap=EntityUtils.getMap(courseList, "id");
			//组合名称
			String shortName;
			if(GkElectveConstants.GUID_ZERO.equals(subjectIds)){
				shortName = "混合";
			}else{
				shortName=makeListToStrName(new ArrayList<String>(subjectIdSet),subjectNameMap);
			}
			
			List<StudentSubjectDto> allstuSubjectList;
			//查询某种组合的学生结果
			if(stuids!=null && stuids.length>0){
				allstuSubjectList = gkResultService.findStudentSubjectDto(rounds.getSubjectArrangeId(),GkElectveConstants.GUID_ZERO.equals(subjectIds)?null:subjectIdSet,stuids);
			}else{
				allstuSubjectList = gkResultService.findAllStudentSubjectDto(rounds.getSubjectArrangeId(),GkElectveConstants.GUID_ZERO.equals(subjectIds)?null:subjectIdSet);
			}
			
			List<StudentSubjectDto> stuSubjectList=new ArrayList<StudentSubjectDto>();
			List<String> stuList=null;
			Set<String> noStuId=new HashSet<String>();
			Set<String> groupNames=new HashSet<String>();
			if(CollectionUtils.isNotEmpty(allstuSubjectList)){
				List<GkGroupClass> groupClassList=gkGroupClassService.findGkGroupClassBySubjectIds(null,roundsId);
				if(CollectionUtils.isNotEmpty(groupClassList)){
					for(GkGroupClass g:groupClassList){
						groupNames.add(g.getGroupName());
						stuList = g.getStuIdList();
						if(CollectionUtils.isNotEmpty(stuList)){
							noStuId.addAll(stuList);
						}
					}
					for(StudentSubjectDto s:allstuSubjectList){
						if(!noStuId.contains(s.getStuId())){
							stuSubjectList.add(s);
						}
					}
				}else{
					stuSubjectList=allstuSubjectList;
				}
			}
			
			if(CollectionUtils.isEmpty(stuSubjectList)){
				return error("没有学生需要分班");
			}
			
			String[] sortType = findFrist(gkSubjectArrange.getId());
			//学生(每班学生)
			List<String>[] array = new List[openNum];
			for(int i=0;i<openNum;i++){
				array[i]=new ArrayList<String>();
			}
			//默认按班级排序
			Collections.sort(stuSubjectList, new Comparator<StudentSubjectDto>() {
	 			@Override
	 			public int compare(StudentSubjectDto o1, StudentSubjectDto o2) {
	 				return o1.getClassId().compareTo(o2.getClassId());
	 			}
	 		});
			for(StudentSubjectDto dto:stuSubjectList){
				//求3门总分
	        	Map<String, Double> scoreMap = dto.getScoreMap();
	        	if(scoreMap==null || scoreMap.size()<=0){
	        		 dto.setAvgScore(0);
	        	 }else{
	        		 Set<String> choose = dto.getChooseSubjectIds();
	        		 double sum=0;
	        		 for(String key:scoreMap.keySet()){
	        			 if(choose.contains(key)){
	        				 Double dd = scoreMap.get(key);
	 	        			if(dd!=null){
	 	        				sum=sum+dd;
	 	        			}
	        			 }
	        		 }
	        		dto.setAvgScore(sum);
	        	 }
			}
			
			if(sortType!=null && sortType.length>0){
				if(ARRANGE_TYPE_XINGBIE.equals(sortType[0])){
					// 性别
	        		List<StudentSubjectDto> studentsMaleTemp=new ArrayList<StudentSubjectDto>();//男
	    			List<StudentSubjectDto> studentsFeMaleTemp=new ArrayList<StudentSubjectDto>();//女
	    			for (StudentSubjectDto student2 : stuSubjectList) {
	    				if (student2.getSex() == GkElectveConstants.MALE) {
	    					// 男
	    					studentsMaleTemp.add(student2);
	    				} else {
	    					studentsFeMaleTemp.add(student2);
	    				}
	    			}
	    			
	    			if(sortType.length>1){
	        			//按性别
	        			if(ARRANGE_TYPE_SCORE.equals(sortType[1])){
	        				onlysortStuScore(studentsMaleTemp);
	        				onlysortStuScore(studentsFeMaleTemp);
	                	}else if(ARRANGE_TYPE_BANJI.equals(sortType[1])){
	                		//按班级---略
	                		
	                	}
	        		}
	    			openClass(openNum, studentsMaleTemp, array);
    				//array
    				int max=0;
    				int min=0;
    				for(int i=1;i<openNum;i++){
    					if(array[max].size()<array[i].size()){
    						max=i;
    					}
    					if(array[min].size()>array[i].size()){
    						min=i;
    					}
    				}
    				if(min<max){
    					//取得最大值的第一个位置
    					int findFirstMax=max;
    					for(int i=0;i<openNum;i++){
        					if(array[max].size()==array[i].size()){
        						findFirstMax=i;
        						break;
        					}
        				}
    					//顺序
    					List<String>[] newArray = new List[openNum];
    					int j=0;
    					for(int i=findFirstMax-1;i>=0;i--){
    						newArray[j]=array[i];
    						j++;
    					}
    					for(int i=openNum-1;i>=findFirstMax;i--){
    						newArray[j]=array[i];
    						j++;
    					}
    					array=newArray;
    					openClass( openNum, studentsFeMaleTemp, array);
    				}else{
    					//顺序
    					List<String>[] newArray = new List[openNum];
    					int j=0;
    					for(int i=min;i<openNum;i++){
    						newArray[j]=array[i];
    						j++;
    					}
    					for(int i=0;i<min;i++){
    						newArray[j]=array[i];
    						j++;
    					}
    					array=newArray;
    					openClass( openNum, studentsFeMaleTemp, array);
    				}
	    			
				}else if(ARRANGE_TYPE_SCORE.equals(sortType[0])){
	        		//如果下面有性别排序则先不管
	        		//按成绩
					onlysortStuScore(stuSubjectList);
					openClass(openNum, stuSubjectList, array);
				}else{
					openClass(openNum, stuSubjectList, array);
				}
			}else{
				openClass( openNum, stuSubjectList, array);
			}
			GkGroupClass gkGroupClass=null;
			List<GkGroupClass> insertGroupClass=new ArrayList<GkGroupClass>();
			GkGroupClassStu gkGroupClassStu=null;
			List<GkGroupClassStu> insertGroupClassStu=new ArrayList<GkGroupClassStu>();
			int k=1;
			String[] split = subjectIds.split(",");
			for(int i=0;i<array.length;i++){
				if(CollectionUtils.isNotEmpty(array[i])){
					//新增预排班
                	gkGroupClass=new GkGroupClass();
                	gkGroupClass.setId(UuidUtils.generateUuid());
                	while(true){
                		if(!groupNames.contains(shortName+k+"班")){
                			gkGroupClass.setGroupName(shortName+k+"班");
                    		break;
                    	}
                		k++;
                	}
                	if(split.length == 3){
        				gkGroupClass.setGroupType(GkElectveConstants.GROUP_TYPE_1);
        			}else if(split.length == 2){
        				gkGroupClass.setGroupType(GkElectveConstants.GROUP_TYPE_2);
        			}else if(split.length == 1){
        				gkGroupClass.setGroupType(GkElectveConstants.GROUP_TYPE_3);
        			}
                	gkGroupClass.setRoundsId(roundsId);
                	gkGroupClass.setSubjectIds(subjectIds);
                	insertGroupClass.add(gkGroupClass);
                	for(String stuId:array[i]){
                		gkGroupClassStu = new GkGroupClassStu();
                		gkGroupClassStu.setId(UuidUtils.generateUuid());
                		gkGroupClassStu.setStudentId(stuId);
                		gkGroupClassStu.setGroupClassId(gkGroupClass.getId());
                		insertGroupClassStu.add(gkGroupClassStu);
                	}
                	groupNames.add(gkGroupClass.getGroupName());
				}
			}
			gkGroupClassService.saveGroup(insertGroupClass,insertGroupClassStu);
			

		} catch (Exception e) {
			e.printStackTrace();
			return error("分班失败！");
		}
		return success("分班成功！");
	}
	/**
	 * 
	 * @param index 开始位置
	 * @param classNum
	 * @param temp
	 * @param array
	 * return 下一次开始位置
	 */
	private void openClass(int classNum,List<StudentSubjectDto> temp,List<String>[] array){
		boolean flag=true;//顺排：true,逆排 ：false
		int index=0;
		if(CollectionUtils.isNotEmpty(temp)){
			for(int i=0;i<temp.size();i++){
				if(flag){
					array[index].add(temp.get(i).getStuId());
					index++;
					if(index==classNum){
						flag=false;
					}
				}else{
					index--;
					array[index].add(temp.get(i).getStuId());
					if(index==0){
						flag=true;
					}
				}
			}	
		}
	}
	
	
	/**
     * 复制
     * @param allstudents
     * @return
     */
    private List<StudentSubjectDto> copyList(List<StudentSubjectDto> allstudents) {
    	List<StudentSubjectDto> returnList=new ArrayList<StudentSubjectDto>();
    	StudentSubjectDto dto1=null;
    	for(StudentSubjectDto dto:allstudents){
    		dto1=new StudentSubjectDto();
    		EntityUtils.copyProperties(dto, dto1);
    		returnList.add(dto1);
    	}
		return returnList;
	}
    private boolean isNowArrange(String roundsId){
		if(ArrangeSingleSolver.isSolverIdRunning(roundsId+"A") || ArrangeSingleSolver.isSolverIdRunning(roundsId+"B")){
			return true;
		}else{
			return false;
		}
	}
}
