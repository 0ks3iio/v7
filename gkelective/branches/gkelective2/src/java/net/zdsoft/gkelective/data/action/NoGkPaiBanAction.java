package net.zdsoft.gkelective.data.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/gkelective2/{roundsId}")
public class NoGkPaiBanAction extends BaseAction{
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
	private GkAllocationService gkAllocationService;
	
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
		teachClass.setId(UuidUtils.generateUuid());
		teachClass.setSubjectId(subjectId);
		teachClass.setUnitId(gkArrange.getUnitId());
		//默认7选3
    	teachClass.setGradeId(gkArrange.getGradeId());
    	teachClass.setCreationTime(new Date());
    	teachClass.setModifyTime(new Date());
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

	/***********---------------------------混合单科开班开始-------------------------------***********/
	
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
	    	}else{
	    		//ab
	    		String aIndex = RedisUtils.get(round.getId()+"A_ok");
	    		String bIndex = RedisUtils.get(round.getId()+"B_ok");
	    		if("1".equals(aIndex) && "1".equals(bIndex)){
	    			gkRoundsService.updateStep(GkElectveConstants.STEP_5, roundsId);
	    			RedisUtils.del(new String[]{round.getId()+"A_ok",round.getId()+"B_ok"});
	    			return success("end");
	    		}
	    	}
	    	return success("continue");
	   }
    }
	
	
	private boolean isNowArrange(String roundsId){
		if(ArrangeSingleSolver.isSolverIdRunning(roundsId+"A") || ArrangeSingleSolver.isSolverIdRunning(roundsId+"B")){
			return true;
		}else{
			return false;
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
				//只开选考
				if(round.getBatchCountA()==null || round.getBatchCountA()==0 || round.getBatchCountA()<subjectNum){
					return error("设置选考批次时间不能小于选课数量");
				}
				mess=processSingleA(subject_all,round.getBatchCountA(),gkArrange,round, sortType);
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
    
    /**
     * 全混 只排A
     * @param subject_all
     * @param subjectNumA A排课批次数
     * @param gkArrange
     * @param round
     * @param sortType
     * @return
     */
    public String  processSingleA(String[] subject_all, int subjectNum, final GkSubjectArrange gkArrange, final GkRounds round, String[] sortType){
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
    	List<StudentSubjectDto> students=null;
    	try{
        	students = initSingleStudentsA(subjectNum,subject_all,allstudents);
        }catch (Exception e) {
 			e.printStackTrace();
 			throw new RuntimeException(e.getMessage());
 		}
    	if(CollectionUtils.isEmpty(students)){
    		return "yes";
    	}
    
        Map<String, ArrangeCapacityRange> subjectIdTypeCapacityRangeMap=findSingleMaxMinNum(round.getId());
       
        // 2+x的人组成的班级
        
        List<ArrangeClass> arrangeClassList = new ArrayList<ArrangeClass>();
       
        List<ArrangeStudent> arrangeStudentList = ArrangeDtoConverter.convertToArrangeStudent(students);
        ArrangeSingleSolver solver = new ArrangeSingleSolver(subjectNum, subjectNum, arrangeStudentList, arrangeClassList, round.getId()+"A");
        
        solver.addListener(new SolverListener() {
            @Override
            public void solveStarted() {
            }
            @Override
            public void solveFinished(Map<String, List<Room>>[] bottleArray, Map<String, Set<Integer>> classIdAdditionalSubjectIndexSetMap) {
                long start = System.currentTimeMillis();
                makeA(alldtoMap,gkArrange,round,bottleArray,subjectNameMap,classIdAdditionalSubjectIndexSetMap,true);
                long end = System.currentTimeMillis();
                System.out.println("makeA耗时：" + (end-start)/1000 + "s");
            }
            @Override
            public void onError(Exception e) {
            	System.out.println(e.getMessage());
            }
            @Override
            public void solveCancelled() {
            }
        });
        solver.solve(subjectIdTypeCapacityRangeMap);
		return null;
    } 
    
    
    /**
     * AB分开排课 全混
     * @param subject_all
     * @param subjectNumA A放在多少个批次下
     * @param subjectNumB B放在多少个批次下
     * @param gkArrange
     * @param round
     * @param sortType
     * @return
     */
    public String  processSingleAB(String[] subject_all, int subjectNumA,int subjectNumB, final GkSubjectArrange gkArrange, final GkRounds round, String[] sortType){
    	//所有高中7选3科目
    	List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()), new TR<List<Course>>() {});
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
    		studentsA = initSingleStudentsA(subjectNumA, subject_all, allstudentsA);
        }catch (Exception e) {
 			e.printStackTrace();
 			throw new RuntimeException(e.getMessage());
 		}
    	//B
    	List<StudentSubjectDto> studentsB=null;
    	try{
    		studentsB = initSingleStudentsB(subjectNumB, subject_all, allstudentsB);
        }catch (Exception e) {
 			e.printStackTrace();
 			throw new RuntimeException(e.getMessage());
 		}
    	if(CollectionUtils.isEmpty(studentsB) && CollectionUtils.isEmpty(studentsA)){
    		return "yes";
    	}
    	if(CollectionUtils.isNotEmpty(studentsA)){
    		//排A
        	List<ArrangeClass> arrangeClassAList = new ArrayList<ArrangeClass>();
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
    	               makeB(alldtoMap,gkArrange,round,bottleArray,subjectNameMap,classIdAdditionalSubjectIndexSetMap,subjectNumA,flag);
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
    		 RedisUtils.set(round.getId()+"B_ok", "1");
    	}
	   
      return null;
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

		
    private void makeA(Map<String,StudentSubjectDto> alldtoMap,GkSubjectArrange gkArrange,GkRounds round,Map<String, List<Room>>[] roomCombineArray,Map<String, Course> subjectNameMap,Map<String, Set<Integer>> classIdAdditionalSubjectIndexSetMap,boolean updateStep){
    	boolean flag=jadge(roomCombineArray);
		//需要插入数据库
        List<GkBatch> gkBatchList = new ArrayList<GkBatch>();
        GkBatch gkBatch=null;
        GkTeachClassStore teachClass = null;
        List<GkTeachClassStore> teachClassList = new ArrayList<GkTeachClassStore>();
        GkTeachClassStuStore teachClassStu=null;
        List<GkTeachClassStuStore> teachClassStuList = new ArrayList<GkTeachClassStuStore>();
        List<GkTeachClassEx> insertAvg=new ArrayList<GkTeachClassEx>();
        GkTeachClassEx avgEx=null;
        if(!flag){
			//没有要保存的数据
			
		}else{
	        //教师数据先不放
			//<科目id,班级数> 单科(物理A1班，物理A2班) 
	        //<科目id,班级数> 单科(物理B1班，物理B2班) 
	        Map<String,Integer> subjectNumAMap=new HashMap<String, Integer>();
	        Map<String,Integer> subjectNumBMap=new HashMap<String, Integer>();
		    
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
		            	//批次
		            	gkBatch=makeNewGkBatch(round, BaseConstants.ZERO_GUID);
		            	gkBatch.setBatch(i+1);
		            	//教学班
		            	
		            	teachClass=makeNewTeachClass(gkArrange, round, subjectId);
        				if(BaseConstants.SUBJECT_TYPE_A.equals(room.getType())){
        					if(!subjectNumAMap.containsKey(subjectId)){
    		            		subjectNumAMap.put(subjectId, 1);
    		            	}
        					int subNum = subjectNumAMap.get(subjectId);
        					teachClass.setClassName(subjectNameMap.get(subjectId).getSubjectName()+room.getType()+subNum+(StringUtils.isNotBlank(room.getLevel())?room.getLevel():"")+"班");
    		            	subjectNumAMap.put(subjectId, subNum+1);
        				}else{
        					if(!subjectNumBMap.containsKey(subjectId)){
        						subjectNumBMap.put(subjectId, 1);
    		            	}
        					int subNum = subjectNumBMap.get(subjectId);
        					teachClass.setClassName(subjectNameMap.get(subjectId).getSubjectName()+room.getType()+subNum+(StringUtils.isNotBlank(room.getLevel())?room.getLevel():"")+"班");
        					subjectNumBMap.put(subjectId, subNum+1);
        				}
		            	
			    		gkBatch.setClassType(room.getType());
			    		gkBatch.setTeachClassId(teachClass.getId());	
			    		//教学班下学生
			    		List<String> stuIds = new ArrayList<String>();
			    		for( StudentSubjectDto item:room.getStudentList()){
			    			teachClassStu=new GkTeachClassStuStore();
			    			teachClassStu.setId(UuidUtils.generateUuid());
			    			teachClassStu.setGkClassId(teachClass.getId());
			    			teachClassStu.setStudentId(item.getStuId());
			    			teachClassStuList.add(teachClassStu);
			    			stuIds.add(item.getStuId());
			    		}
			    		double avg=0;
		            	avgEx=new GkTeachClassEx();
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
			    		teachClassList.add(teachClass);
		            }
	            }
		       
	        }
	        
		}
        long start = System.currentTimeMillis();
        gkBatchService.saveBatchsBySubjectIdType("A",round,gkBatchList, teachClassList, teachClassStuList,insertAvg, updateStep);
		
		long end = System.currentTimeMillis();
		System.out.println("saveBatchs耗时：" + (end-start)/1000 + "s");
    }
    private void makeB(Map<String,StudentSubjectDto> alldtoMap,GkSubjectArrange gkArrange,GkRounds round,Map<String, List<Room>>[] roomCombineArray,Map<String, Course> subjectNameMap,Map<String, Set<Integer>> classIdAdditionalSubjectIndexSetMap,int bathA,boolean updateStep){
    	boolean flag=jadge(roomCombineArray);
		//需要插入数据库
        List<GkBatch> gkBatchList = new ArrayList<GkBatch>();
        GkBatch gkBatch=null;
        GkTeachClassStore teachClass = null;
        List<GkTeachClassStore> teachClassList = new ArrayList<GkTeachClassStore>();
        GkTeachClassStuStore teachClassStu=null;
        List<GkTeachClassStuStore> teachClassStuList = new ArrayList<GkTeachClassStuStore>();
        List<GkTeachClassEx> insertAvg=new ArrayList<GkTeachClassEx>();
        GkTeachClassEx avgEx=null;
        if(!flag){
			//没有要保存的数据
			
		}else{
	        //教师数据先不放
			//<科目id,班级数> 单科(物理A1班，物理A2班) 
	        //<科目id,班级数> 单科(物理B1班，物理B2班) 
	        Map<String,Integer> subjectNumAMap=new HashMap<String, Integer>();
	        Map<String,Integer> subjectNumBMap=new HashMap<String, Integer>();
		    
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
		            	//批次
		            	gkBatch=makeNewGkBatch(round, BaseConstants.ZERO_GUID);
		            	gkBatch.setBatch(i+bathA+1);
		            	//教学班
		            	
		            	teachClass=makeNewTeachClass(gkArrange, round, subjectId);
        				if(BaseConstants.SUBJECT_TYPE_A.equals(room.getType())){
        					if(!subjectNumAMap.containsKey(subjectId)){
    		            		subjectNumAMap.put(subjectId, 1);
    		            	}
        					int subNum = subjectNumAMap.get(subjectId);
        					teachClass.setClassName(subjectNameMap.get(subjectId).getSubjectName()+room.getType()+subNum+(StringUtils.isNotBlank(room.getLevel())?room.getLevel():"")+"班");
    		            	subjectNumAMap.put(subjectId, subNum+1);
        				}else{
        					if(!subjectNumBMap.containsKey(subjectId)){
        						subjectNumBMap.put(subjectId, 1);
    		            	}
        					int subNum = subjectNumBMap.get(subjectId);
        					teachClass.setClassName(subjectNameMap.get(subjectId).getSubjectName()+room.getType()+subNum+(StringUtils.isNotBlank(room.getLevel())?room.getLevel():"")+"班");
        					subjectNumBMap.put(subjectId, subNum+1);
        				}
		            	
//			            	teachClass.setTeacherId(BaseConstants.ZERO_GUID);
			    		gkBatch.setClassType(room.getType());
			    		gkBatch.setTeachClassId(teachClass.getId());	
			    		//教学班下学生
			    		List<String> stuIds = new ArrayList<String>();
			    		for( StudentSubjectDto item:room.getStudentList()){
			    			teachClassStu=new GkTeachClassStuStore();
			    			teachClassStu.setId(UuidUtils.generateUuid());
			    			teachClassStu.setGkClassId(teachClass.getId());
			    			teachClassStu.setStudentId(item.getStuId());
			    			teachClassStuList.add(teachClassStu);
			    			stuIds.add(item.getStuId());
			    		}
			    		double avg=0;
		            	avgEx=new GkTeachClassEx();
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
			    		teachClassList.add(teachClass);
		            }
	            }
		       
	        }
	        
		}
        List<GkGroupClass> updateGroupClass=new ArrayList<GkGroupClass>();
        long start = System.currentTimeMillis();
		gkBatchService.saveBatchsBySubjectIdType("B",round,gkBatchList, teachClassList, teachClassStuList,insertAvg, updateStep);
		long end = System.currentTimeMillis();
		System.out.println("saveBatchs耗时：" + (end-start)/1000 + "s");
    }
	    
	    
    /**
     * 组装A 科目
     * @param subjectNum 几个批次时间放A
     * @param subject_all
     * @param allStudents
     * @return
     */
    private List<StudentSubjectDto> initSingleStudentsA(int subjectNum,String[] subject_all,List<StudentSubjectDto> allStudents) {
        List<StudentSubjectDto> returnstudents = new ArrayList<StudentSubjectDto>();
        if(CollectionUtils.isEmpty(allStudents)){
        	return returnstudents;
        }
        List<String> alls=null;
        String[] subject_all_self=null;
        if(CollectionUtils.isNotEmpty(allStudents)){
        	for(StudentSubjectDto dto:allStudents){
        		//需要排的
        		alls=new ArrayList<String>();
        		if(dto.getChooseSubjectIds().size()<=0){
        			//查的是选课表 所以选课为空的可能性没有
        			continue;
        		}
        		//去除不走班科目
    			subject_all_self = moveSubject(dto.getChooseSubjectIds(),subject_all);
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
//	                	 Map<String, Double> scoreMap = dto.getScoreMap();
//	                	 if(scoreMap==null || scoreMap.size()<=0){
//	                		 dto.setAvgScore(0);
//	                	 }else{
//	                		double sum=0;
//		             		 for(String key:scoreMap.keySet()){
//		             			Double dd = scoreMap.get(key);
//		             			if(dto.getChooseSubjectIds().contains(key)){
//		             				if(dd!=null){
//			             				sum=sum+dd;
//			             			}
//		             			}
//		             		 }
//		             		dto.setAvgScore(sum/subjectNum);
//	                	 }
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
    
    
    /**
     * 组装B 科目
     * @param subjectNum 几个批次时间放B
     * @param subject_all
     * @param allStudents
     * @return
     */
    private List<StudentSubjectDto> initSingleStudentsB(int subjectNum,String[] subject_all,List<StudentSubjectDto> allStudents) {
        List<StudentSubjectDto> returnstudents = new ArrayList<StudentSubjectDto>();
        if(CollectionUtils.isEmpty(allStudents)){
        	return returnstudents;
        }
        List<String> alls=null;
        if(CollectionUtils.isNotEmpty(allStudents)){
        	for(StudentSubjectDto dto:allStudents){
        		//需要排的
        		alls=new ArrayList<String>();
        		if(dto.getChooseSubjectIds().size()<=0){
        			//查的是选课表 所以选课为空的可能性没有
        			continue;
        		}
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
//	                	 Map<String, Double> scoreMap = dto.getScoreMap();
//	                	 if(scoreMap==null || scoreMap.size()<=0){
//	                		 dto.setAvgScore(0);
//	                	 }else{
//	                		double sum=0;
//		             		 for(String key:scoreMap.keySet()){
//		             			Double dd = scoreMap.get(key);
//		             			if(dto.getChooseSubjectIds().contains(key)){
//		             				if(dd!=null){
//			             				sum=sum+dd;
//			             			}
//		             			}
//		             		 }
//		             		dto.setAvgScore(sum/subjectNum);
//	                	 }
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
	
}

