package net.zdsoft.newgkelective.data.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.framework.utils.*;
import net.zdsoft.newgkelective.data.optaplanner.dto.CGInputData;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.TreeNodeDto;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.NewGkDivideDto;
import net.zdsoft.newgkelective.data.dto.NkStudentCourseDto;
import net.zdsoft.newgkelective.data.dto.StudentResultDto;
import net.zdsoft.newgkelective.data.entity.NewGkArray;
import net.zdsoft.newgkelective.data.entity.NewGkArrayItem;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;
import net.zdsoft.newgkelective.data.entity.NewGkCourseHeap;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTeacher;
import net.zdsoft.newgkelective.data.entity.NewGkTimetable;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableOther;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableTeacher;
import net.zdsoft.newgkelective.data.optaplanner.solver.CGForLectureSolver;
import net.zdsoft.newgkelective.data.service.NewGkArrayItemService;
import net.zdsoft.newgkelective.data.service.NewGkArrayService;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkClassStudentService;
import net.zdsoft.newgkelective.data.service.NewGkCourseHeapService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkElectiveArrayComputeService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTeacherService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableOtherService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableTeacherService;
/**
 *	排课
 */
@Controller
@RequestMapping("/newgkelective/{arrayId}")
public class NewGkElectiveArrayAction extends BaseAction{
	
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkClassStudentService newGkClassStudentService;
	@Autowired
	private NewGkTimetableOtherService newGkTimetableOtherService;
	@Autowired
	private NewGkArrayService newGkArrayService;
	@Autowired
	private NewGkDivideService newGkDivideService;
	@Autowired
	private NewGkDivideClassService newGkDivideClassService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private NewGkTimetableService newGkTimetableService;
	@Autowired
	private NewGkArrayItemService newGkArrayItemService;
	@Autowired
	private NewGkChoResultService newGkChoResultService;
	@Autowired
	private NewGkCourseHeapService newGkCourseHeapService;
	@Autowired
	private NewGkSubjectTeacherService newGkSubjectTeacherService;
	@Autowired
	private NewGkTimetableTeacherService newGkTimetableTeacherService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private NewGkElectiveArrayComputeService arrayComputeService;
	
	/**
	 * 7选3 排课算法
	 * @param arrayId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/arrayLesson/autoArrayLessonNoTeacher")
	@ControllerInfo(value = "没有教师自动排课")
	public String autoArrayLessonNoTeacherV4(@PathVariable final String arrayId) {
		JSONObject on = new JSONObject();
		final String key = NewGkElectiveConstant.ARRAY_LESSON+"_"+arrayId;
		final String key1 = NewGkElectiveConstant.ARRAY_LESSON+"_"+arrayId+"_mess";	
		//进行中
		if(newGkArrayService.checkIsArrayIng(arrayId)){
			RedisUtils.set(key,"start");
			RedisUtils.set(key1, "进行中");
			on.put("stat",RedisUtils.get(key));
			on.put("message", RedisUtils.get(key1));
			return on.toJSONString();
		}
		//没有锁
		if(RedisUtils.get(key)==null){
			RedisUtils.set(key,"start");
			RedisUtils.set(key1, "进行中");
		}else{
			on.put("stat",RedisUtils.get(key));
			on.put("message", RedisUtils.get(key1));
			//再次进来排课 清除消息
			if("success".equals(RedisUtils.get(key)) || "error".equals(RedisUtils.get(key))){
				RedisUtils.del(new String[]{key,key1});
			}
			return on.toJSONString();
		}	
		
		NewGkArray newGkArray = newGkArrayService.findOneWithMaster(arrayId);
		if(newGkArray==null){
			makeJsonMsg(on, key, key1, "该方案不存在");
			return on.toJSONString();
		}
		
		String divideId = newGkArray.getDivideId();
		
		// 取出学生分班结果
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		if(divide==null){
			makeJsonMsg(on, key, key1, "此排课使用的分班方案不存在");
			return on.toJSONString();
		}
		
		try {
			if (NewGkElectiveConstant.DIVIDE_TYPE_01.equals(divide.getOpenType())
					|| NewGkElectiveConstant.DIVIDE_TYPE_02.equals(divide.getOpenType())
					|| NewGkElectiveConstant.DIVIDE_TYPE_05.equals(divide.getOpenType())
					|| NewGkElectiveConstant.DIVIDE_TYPE_06.equals(divide.getOpenType())
					|| NewGkElectiveConstant.DIVIDE_TYPE_07.equals(divide.getOpenType())
					|| NewGkElectiveConstant.DIVIDE_TYPE_08.equals(divide.getOpenType())
					|| NewGkElectiveConstant.DIVIDE_TYPE_09.equals(divide.getOpenType())
					|| NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())
					|| NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divide.getOpenType())
					|| NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType())
					) {
				CGInputData data = arrayComputeService.computeArray3F7(key, key1, newGkArray);
				arrayComputeService.dealMutilArray(Arrays.asList(data),newGkArray,key,key1);
			}else {
				throw new RuntimeException("不存在的分班模式:"+divide.getOpenType());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			makeJsonMsg(on, key, key1, "数据异常:\n"+e1.getMessage());
			return on.toJSONString();
		}
		
		on.put("stat",RedisUtils.get(key));
		on.put("message", RedisUtils.get(key1));
		return on.toJSONString();
	}
	

	private void makeJsonMsg(JSONObject on, String key, String key1, String msg) {
		RedisUtils.set(key,"error");
		RedisUtils.set(key1, msg);
		on.put("stat","error");
		if(msg.length()>100) {
			msg = msg.substring(100)+" ..";
		}
		on.put("message", msg);
	}

	/**
	 * 新的分班
	 * @param arrayId
	 */
	public void initDivideClass(String arrayId){
		NewGkArray newGkArray = newGkArrayService.findById(arrayId);
		String divideId = newGkArray.getDivideId();
		
		// 取出学生选课结果
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		Integer galleryful = divide.getGalleryful();
		String choiceId = divide.getChoiceId();
		List<NewGkChoResult> choiceResultList = newGkChoResultService.findByChoiceIdAndKindType(divide.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,choiceId);
		
		// 求出选了某门课的学生集合
		Map<String, Set<String>> subject1StudentMap = choiceResultList.stream().collect(Collectors.groupingBy(NewGkChoResult::getSubjectId, 
				Collectors.mapping(NewGkChoResult::getStudentId, Collectors.toSet())));
		Set<String> subjectSet = subject1StudentMap.keySet();
		List<String> subjectList = new ArrayList<>(subjectSet);
		// key:subjectId ,V:studentId
		Map<String, Set<String>> subject3StudentMap = new HashMap<String, Set<String>>();  
		for (int i=0;i<subjectSet.size();i++) {
			for(int j=i+1;j<subjectSet.size();j++){
				for(int k=j+1;i<subjectSet.size();k++){
					String tmp = subjectList.get(i)+""+subjectList.get(j)+""+subjectList.get(k);
					SetView<String> intersection1 = Sets.intersection(subject1StudentMap.get(subjectList.get(i)), subject1StudentMap.get(subjectList.get(j)));
					SetView<String> intersection2 = Sets.intersection(intersection1, subject1StudentMap.get(subjectList.get(k)));
					
					subject3StudentMap.put(tmp, intersection2);
				}
			}
		}
		
		// 开班
		for (Entry<String, Set<String>> entry : subject3StudentMap.entrySet()) {
			String subject3Id = entry.getKey();
			
			Set<String> stuIdSet = entry.getValue();
			int size = stuIdSet.size();
			int classNum = 0;
			int overNum = 0;
			if(size%galleryful == 0){
				classNum = size/galleryful;
			}else{
				classNum = size/galleryful + 1;
				overNum = size - size/classNum*classNum;
			}
			for (int i=0;i<classNum;i++) {
				overNum--;
				// 对每门课都开教学班
				for (String subjectId : subjectList) {
					if(subject3Id.contains(subjectId)){
					}
				}
				
			}
		}
	}
	
	private boolean checkTime(String[] classIds,Map<String, List<String>> timeByClassId){
		Set<String> timeSet=new HashSet<String>();
		int timesize=0;
		for(String t:classIds){
			if(timeByClassId.containsKey(t)){
				timeSet.addAll(timeByClassId.get(t));
				timesize=timesize+timeByClassId.get(t).size();
			}
		}
		if(timesize>timeSet.size()){
			//冲突
			return true;
		}
		return false;
	}

	private void saveStudent(String studentId,NewGkArray array,List<String> classIds){
		//查找学生所在班级
		List<NewGkClassStudent> deleList = newGkClassStudentService.findListByDivideStudentId(array.getId(),  new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2},new String[]{studentId},NewGkElectiveConstant.CLASS_SOURCE_TYPE2);
		Set<String> delIds = EntityUtils.getSet(deleList, "id");
		List<NewGkClassStudent> insertList=new ArrayList<NewGkClassStudent>();
		NewGkClassStudent stu=null;
		for(String s: classIds){
			stu=new NewGkClassStudent();
			stu.setId(UuidUtils.generateUuid());
			stu.setClassId(s);
			stu.setStudentId(studentId);
			stu.setCreationTime(new Date());
			stu.setModifyTime(new Date());
			stu.setDivideId(array.getId());
			stu.setUnitId(array.getUnitId());
			insertList.add(stu);
		}
		newGkClassStudentService.saveAndDel(delIds.toArray(new String[]{}),insertList.toArray(new NewGkClassStudent[]{}));
	} 
	
	
	private void saveStudents(String[] studentIds,NewGkArray gkArray,Map<String,List<String>> classIdsByStuId){
		//查找学生所在班级
		List<NewGkClassStudent> deleList = newGkClassStudentService.findListByDivideStudentId(gkArray.getId(),  new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2},studentIds,NewGkElectiveConstant.CLASS_SOURCE_TYPE2);
		Set<String> delIds = EntityUtils.getSet(deleList, "id");
		List<NewGkClassStudent> insertList=new ArrayList<NewGkClassStudent>();
		NewGkClassStudent stu=null;
		for(String studentId:studentIds){
			List<String> classIds = classIdsByStuId.get(studentId);
			for(String s: classIds){
				stu=new NewGkClassStudent();
				stu.setId(UuidUtils.generateUuid());
				stu.setClassId(s);
				stu.setStudentId(studentId);
				stu.setCreationTime(new Date());
				stu.setModifyTime(new Date());
				stu.setDivideId(gkArray.getId());
				stu.setUnitId(gkArray.getUnitId());
				insertList.add(stu);
			}
		}
		
		newGkClassStudentService.saveAndDel(delIds.toArray(new String[]{}),insertList.toArray(new NewGkClassStudent[]{}));
	}
	

	@ResponseBody
    @RequestMapping("/arrayLesson/cutArrayLesson")
	@ControllerInfo(value = "取消排班")
    public String cutArrayLesson(@PathVariable String arrayId) {
    	try{
    		NewGkArray newGkArray = newGkArrayService.findOne(arrayId);
     		if(newGkArray==null){
    			return error("该方案不存在");
     		}
     		NewGkDivide newGkDivide = newGkDivideService.findById(newGkArray.getDivideId());
    		if(newGkDivide==null){
    			return error("该使用的分班方案不存在");
     		}
    		
    		if(newGkArrayService.checkIsArrayIng(arrayId)) {
    			CGForLectureSolver.stopSolver(arrayId);
    		}else {
    			final String key = NewGkElectiveConstant.ARRAY_LESSON+"_"+arrayId;
    			if(!(RedisUtils.get(key)!=null && "start".equals(RedisUtils.get(key)))){
    				//直接下一步
    				return success("此次排课已经结束！");
    			}
    		}
    	}catch (Exception e) {
			e.printStackTrace();
			return error("取消失败！"+e.getMessage());
		}
       return success("排课取消成功");
    }
	
	
	@RequestMapping("/arraySet/pageIndex")
    @ControllerInfo(value = "查看排课设置")
	public String showArrayItem(@PathVariable String arrayId,ModelMap map){
		NewGkArray newArray = newGkArrayService.findOne(arrayId);
    	if(newArray==null){
    		return errorFtl(map, "排课方案不存在");
    	}
    	NewGkDivide newDivide = newGkDivideService.findById(newArray.getDivideId());
    	if(newDivide!=null){
    	    map.put("newDivide",newDivide);
    		List<NewGkDivide> divideList=new ArrayList<NewGkDivide>();
    		divideList.add(newDivide);
    		//提示
    		List<NewGkDivideDto> list = newGkDivideService.makeDivideItem(divideList);
    		if(CollectionUtils.isNotEmpty(list)){
    			map.put("divideDto", list.get(0));
    		}
    	}

    	Set<String> set=new HashSet<String>();
    	set.add(newArray.getLessonArrangeId());
    	set.add(newArray.getPlaceArrangeId());
    	List<NewGkArrayItem> list=newGkArrayItemService.findListByIdIn(set.toArray(new String[]{}));
		Map<String,NewGkArrayItem> itemMap=new HashMap<String,NewGkArrayItem>();
    	if(CollectionUtils.isNotEmpty(list)){
    		List<NewGkArrayItem> itemList=null;
			for(NewGkArrayItem item:list){
				itemList = new ArrayList<NewGkArrayItem>();
				itemList.add(item);
				if(NewGkElectiveConstant.ARRANGE_TYPE_01.contains(item.getDivideType())){
					newGkArrayItemService.makeDtoData(NewGkElectiveConstant.ARRANGE_TYPE_01,newArray.getGradeId(), itemList);
					itemMap.put(NewGkElectiveConstant.ARRANGE_TYPE_01, item);
				}else if(NewGkElectiveConstant.ARRANGE_TYPE_03.contains(item.getDivideType())){
					newGkArrayItemService.makeDtoData(NewGkElectiveConstant.ARRANGE_TYPE_03,newArray.getGradeId(), itemList);
					itemMap.put(NewGkElectiveConstant.ARRANGE_TYPE_03, item);
				}else if(NewGkElectiveConstant.ARRANGE_TYPE_04.contains(item.getDivideType())){
					newGkArrayItemService.makeDtoData(NewGkElectiveConstant.ARRANGE_TYPE_04, newArray.getGradeId(),itemList);
					itemMap.put(NewGkElectiveConstant.ARRANGE_TYPE_04, item);
				}else if(NewGkElectiveConstant.ARRANGE_TYPE_02.contains(item.getDivideType())){
					newGkArrayItemService.makeDtoData(NewGkElectiveConstant.ARRANGE_TYPE_02, newArray.getGradeId(),itemList);
					itemMap.put(NewGkElectiveConstant.ARRANGE_TYPE_02, item);
				}
			}
			
		}
    	map.put("itemMap", itemMap);

		map.put("newArray", newArray);
		return "/newgkelective/array/arrayShowSet.ftl";
    	
	}
	
	/**
	 * 教师冲突解决完成后调用分堆
	 * @param arrayId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/arrayLesson/autoArrangeSameClass")
	@ControllerInfo(value = "自动给班级课程分堆")
	public String autoArrangeSameClass(@PathVariable String arrayId){
		NewGkArray newGkArray = newGkArrayService.findById(arrayId);
		if(newGkArray==null){
			return error("该方案不存在");
		}
		int conNumber = newGkTimetableService.getConflictNum(arrayId, new ArrayList<>());
		if(conNumber>0){
			return error("冲突未解决完成");
		}
		
		String mess = newGkArrayService.autoArraySameClass(newGkArray.getUnitId(), arrayId);
    	if(StringUtils.isNotBlank(mess)){
    		return error(mess);
    	}
    	
    	return success("自动分堆成功");
	}
	
	/**
	 * 目前用不到了
	 * @param arrayId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/arrayLesson/autoTeacher")
	@ControllerInfo(value = "自动安排教师")
	@Deprecated
	public String autoTeacher(@PathVariable String arrayId){
    	//根据保存的教师数据
		List<NewGkCourseHeap> heapList=newGkCourseHeapService.findByArrayId(arrayId);
		if(CollectionUtils.isEmpty(heapList)){
			//没有课程数据
			return error("没有需要设置的教师的课程");
		}
		
		//DOTO
		
		List<NewGkSubjectTeacher> subjectTeacherlist = newGkSubjectTeacherService.findByArrayId(arrayId);
		if(CollectionUtils.isEmpty(subjectTeacherlist)){
			//没有课程数据
			return error("没有需要设置的教师的课程");
		}
		
	   	//防止老师有重复，需要安排的时候判断老师上课时间是否冲突
		String unitId = getLoginInfo().getUnitId();
		List<NewGkTimetable> tablelist = newGkTimetableService.findByArrayId(unitId, arrayId);
		if(CollectionUtils.isEmpty(tablelist)){
			//没有课程数据
			return error("没有需要设置的教师的课程");
		}
		//组装上课时间数据 判断上课时间冲突 这个暂时老师那边一个老师教一个科目限制
//			Map<String,Set<String>> timeByTeacher=new HashMap<String,Set<String>>();
//			newGkTimetableService.makeTime(tablelist);
		Map<String, NewGkTimetable> tableMap = EntityUtils.getMap(tablelist,"id");
			
		
		Set<String> subjectIds=new HashSet<String>();
		//key:subject 堆值 数量   Set<String>:subjectType_timetableId
		Map<String,Map<Integer,Set<String>>> tableIdBySubject=new HashMap<String,Map<Integer,Set<String>>>();
		for(NewGkCourseHeap heap:heapList){
			if(!tableMap.containsKey(heap.getTimetableId())){
				continue;
			}
			NewGkTimetable table = tableMap.get(heap.getTimetableId());
			
			Map<Integer,Set<String>> itemmap=null;
			if(!tableIdBySubject.containsKey(heap.getSubjectId())){
				itemmap= new HashMap<Integer,Set<String>>();
				tableIdBySubject.put(heap.getSubjectId(), itemmap);
			}else{
				itemmap=tableIdBySubject.get(heap.getSubjectId());
			}
			if(!itemmap.containsKey(heap.getHeapNum())){
				itemmap.put(heap.getHeapNum(), new HashSet<String>());
			}
			itemmap.get(heap.getHeapNum()).add(table.getSubjectType()+"_"+heap.getTimetableId());
			subjectIds.add(heap.getSubjectId());
		}
		
		//根据教师 跟分堆结果
		Map<String,Set<String>> teachetIdBySubject=new HashMap<String,Set<String>>();
		for(NewGkSubjectTeacher t:subjectTeacherlist){
			if(!teachetIdBySubject.containsKey(t.getSubjectId())){
				teachetIdBySubject.put(t.getSubjectId(), new HashSet<String>());
			}
			teachetIdBySubject.get(t.getSubjectId()).add(t.getTeacherId());
		}
		
		List<Course>clist=courseRemoteService.findListObjectByIds(subjectIds.toArray(new String[0]));
		Map<String,Course> coumap = EntityUtils.getMap(clist, "id");
		
		Map<Integer, Set<String>> allResult=null;
		List<NewGkTimetableTeacher> insertList = new ArrayList<NewGkTimetableTeacher>();
		NewGkTimetableTeacher tt=null;
		for(String subjectid:subjectIds){
			Map<Integer, Set<String>> map = tableIdBySubject.get(subjectid);
			if(!teachetIdBySubject.containsKey(subjectid)){
				Course c=coumap.get(subjectid);
				if(!(BaseConstants.ZERO_GUID.equals(c.getCourseTypeId()))){
					return error("科目老师设置不完全");
				}
			}
			//根据老师人数 
			Set<String> ss = teachetIdBySubject.get(subjectid);
			if(CollectionUtils.isEmpty(ss)){
				continue;
			}
			String[] tttIds = ss.toArray(new String[]{});
			
			allResult = arrangeTeacherNums(map, ss.size());
			//一个堆一个老师 
			int i=0,sum=tttIds.length;
			for(Entry<Integer, Set<String>> all:allResult.entrySet() ){
				if(i<sum){
					String ttId=tttIds[i];
					Set<String> subjectTypetabled = all.getValue();
					/**
					 * 安排老师
					 */
					for(String s:subjectTypetabled){
						String[] arr = s.split("_");
						if(!tableMap.containsKey(arr[1])){
							continue;
						}
						if(StringUtils.isNotBlank(ttId)){
							tt=new NewGkTimetableTeacher();
							tt.setTimetableId(arr[1]);
							tt.setId(UuidUtils.generateUuid());
							tt.setTeacherId(ttId);
							insertList.add(tt);
						}
					}
					
					i++;
				}
			}
		}
		try{
			newGkTimetableTeacherService.saveOrDel(arrayId,insertList,tableMap.keySet().toArray(new String[]{}));
		}catch(Exception e){
			e.printStackTrace();
			return error("自动分配老师失败");
		}
		
		return success("自动分配老师成功");
    	
	}
	/**
	 * 目前已有分堆数目  适当扩大到num堆
	 * @param heapByHeapNum
	 * @param num
	 * @return
	 */
	public Map<Integer,Set<String>> arrangeTeacherNums(Map<Integer,Set<String>> heapByHeapNum,int num){
		Map<Integer,Set<String>> returnMap=new HashMap<Integer,Set<String>>();
		//堆 教师数
		Map<Integer,Integer> teachNumByHeapNum=new HashMap<Integer,Integer>();
		//堆 班级数
		Map<Integer,Integer> classNumByHeapNum=new HashMap<Integer,Integer>();
		int i=0;
		//默认每一堆放一个老师
		for(Entry<Integer, Set<String>> item :heapByHeapNum.entrySet()){
			teachNumByHeapNum.put(item.getKey(), 1);
			classNumByHeapNum.put(item.getKey(), item.getValue().size());
			i++;
		}
		//剩余老师
		int leftTeacherNum=num-i;
		while(leftTeacherNum>0){
			Integer[] maxHeapNum=makeMax(teachNumByHeapNum,classNumByHeapNum);
			if(maxHeapNum[1]<=1){
				break;
			}else{
				teachNumByHeapNum.put(maxHeapNum[0], teachNumByHeapNum.get(maxHeapNum[0])+1);
				leftTeacherNum--;
			}
		}
		int j=0;//分组的组号从0开始
		for(Entry<Integer, Set<String>> item :heapByHeapNum.entrySet()){
			Set<String> ss=item.getValue();
			//均匀一个个  A B O排序
			String[] tIds = ss.toArray(new String[]{});//班级科目
			//排序 tttIds 类似A_id B_id O_id
			Arrays.sort(tIds);
			int tNum=teachNumByHeapNum.get(item.getKey());//总的堆
			
			for(int yy=0;yy<tIds.length;yy++){
				int xx=yy+1;
				int remainder = xx%tNum;
				if(!returnMap.containsKey(j+remainder)){
					returnMap.put(j+remainder, new HashSet<String>());
				}
				returnMap.get(j+remainder).add(tIds[yy]);
			}
			j=returnMap.size();
		}
		
		return returnMap;
	}
	
	
	
	
	/**
	 * 堆 最大平均数
	 * @param teachNumByHeapNum
	 * @param classNumByHeapNum
	 * @return
	 */
	private static Integer[] makeMax(Map<Integer, Integer> teachNumByHeapNum,
			Map<Integer, Integer> classNumByHeapNum) {
		Integer m=0;
		int avg=-1;
		float avgNum=0;
		for(Entry<Integer, Integer> item :classNumByHeapNum.entrySet()){
			Integer key = item.getKey();
			int left = item.getValue()%teachNumByHeapNum.get(key);
			
			int avgItem=item.getValue()/teachNumByHeapNum.get(key);
			float avgNum1=(float)item.getValue()/teachNumByHeapNum.get(key);
			if(left>0){
				avgItem=avgItem+1;
			}
			if(avgNum1>avgNum){
				m=key;
				avg=avgItem;
				avgNum=avgNum1;
			}
		}

		return new Integer[]{m,avg};
	}
	
	/**
	 * 最终验证冲突
	 */
	@ResponseBody
	@RequestMapping("/arrayLesson/checkAllTeacher")
	@ControllerInfo(value = "最终验证教师")
	public String checkAllTeacher(@PathVariable String arrayId){
		
		String msg = newGkArrayService.checkAllTeacherConflict(arrayId);
		if(StringUtils.isNotBlank(msg)) return error(msg);
		
    	try{
    		newGkArrayService.updateStatById(NewGkElectiveConstant.IF_1, arrayId);
    	}catch(Exception e){
    		e.printStackTrace();
    		return error("修改排课状态失败！");
    	}
		return success("修改排课状态成功！");
	}


	/**    以下处理冲突操作                    */
	@RequestMapping("/arrayLesson/conflictIndex/page")
    @ControllerInfo(value = "冲突index")
	public String conflictIndex(@PathVariable String arrayId, String useMaster, ModelMap map){
		NewGkArray newGkArray = newGkArrayService.findById(arrayId);
		if(newGkArray==null){
			return errorFtl(map, "该排课方案不存在");
		}
		
		ArrayList<TreeNodeDto> dtoList = new ArrayList<>();
		int conNumber = 0;
		if(Objects.equals(useMaster, "1")) {
			conNumber = newGkTimetableService.getConflictNumWithMaster(arrayId, dtoList);
		}else {
			conNumber = newGkTimetableService.getConflictNum(arrayId, dtoList);
		}
		
		map.put("treeData", Json.toJSONString(dtoList));
		map.put("conNumber", conNumber);
		map.put("gradeId", newGkArray.getGradeId());
		map.put("arrangeType", newGkArray.getArrangeType());
    	return "newgkelective/arrayResult/arrayConflictIndex.ftl";
	}

	
	@RequestMapping("/arrayLesson/conflictList/page")
	@ControllerInfo(value = "冲突list")
	public String conflictList(@PathVariable String arrayId,String classId,String subjectId,ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		List<NewGkTimetable> timeList = newGkTimetableService.findByArrayId(unitId, arrayId);
		if(CollectionUtils.isNotEmpty(timeList)){
			Set<String> timeIds=new HashSet<String>();
			String timeId="";
			Set<String> timeIdsOfClassId=new HashSet<String>();
			for(NewGkTimetable time:timeList){
//				subjectIds.add(time.getSubjectId());
//				classIds.add(time.getClassId());
				timeIds.add(time.getId());
//				timeMap.put(time.getId(),time.getClassId());
				if(time.getSubjectId().equals(subjectId)&& time.getClassId().equals(classId)){
					timeId=time.getId();
				}
				if(time.getClassId().equals(classId)){
					timeIdsOfClassId.add(time.getId());
				}
			}
			Course course=SUtils.dc(courseRemoteService.findOneById(subjectId),Course.class);
			
			List<NewGkTimetableOther>  timeOtherList=newGkTimetableOtherService.findByArrayId(getLoginInfo().getUnitId(),arrayId);
    		
			//key- placeId+坐标点      value-timeids暂定
			Map<String,List<NewGkTimetableOther>> lastPlaceMap=new HashMap<String,List<NewGkTimetableOther>>();
			//key- studentId+坐标点      value-timeids暂定
			Map<String,List<NewGkTimetableOther>> lastStudentMap=new HashMap<String,List<NewGkTimetableOther>>();
			newGkTimetableService.getIndex(arrayId, null, null, null, lastPlaceMap, lastStudentMap);
			//选中的科目班级对应的时间轴
			List<NewGkTimetableOther> thisOtherList=new ArrayList<NewGkTimetableOther>();
			for(NewGkTimetableOther other:timeOtherList){
				/*if(timeIdsOfClassId.contains(other.getTimetableId())){
					thisOtherList.add(other);
				}*/
				if(timeId.equals(other.getTimetableId())){
					thisOtherList.add(other);
				}
			}
			/*String AM=NewGkElectiveConstant.AM_PERIOD;
			String PM="2";*/
			NewGkArray array=newGkArrayService.findOne(arrayId);
			if(array==null) return errorFtl(map, "排课方案不存在");
			Grade grade=SUtils.dc(gradeRemoteService.findOneById(array.getGradeId()),Grade.class);
			if(grade==null) return errorFtl(map, "年级不存在");
			int mmNumber=grade.getMornPeriods();
			int amNumber=grade.getAmLessonCount();
			int pmNumber=grade.getPmLessonCount();
			int nightNumber=grade.getNightLessonCount();
			int allNum=amNumber+pmNumber+nightNumber;
			//获取 早中晚 上课节数
            Map<String, Integer> intervalMap = new LinkedHashMap<String, Integer>();
            intervalMap.put(BaseConstants.PERIOD_INTERVAL_1, mmNumber);//早上
            intervalMap.put(BaseConstants.PERIOD_INTERVAL_2, amNumber);//上午
            intervalMap.put(BaseConstants.PERIOD_INTERVAL_3 , pmNumber);//下午
            intervalMap.put(BaseConstants.PERIOD_INTERVAL_4 , nightNumber);//晚上
            
            Map<String,List<NewGkTimetableOther>> pMap=new HashMap<String,List<NewGkTimetableOther>>();
            for(NewGkTimetableOther other:thisOtherList){
            	int period=other.getPeriod();
            	String periodInterval=other.getPeriodInterval();
            	period=getPeriod(period, periodInterval, intervalMap);
            	if(!pMap.containsKey(period+"")){
            		pMap.put(period+"", new ArrayList<NewGkTimetableOther>());
            	}
            	pMap.get(period+"").add(other);
            }
            getPMap(pMap, lastStudentMap, timeId, intervalMap);
            getPMap(pMap, lastPlaceMap, timeId, intervalMap);
            
            JSONObject values = new JSONObject();
            for (int i = 1; i < allNum+1; i++) {
            	JSONObject json = new JSONObject();
            	List<NewGkTimetableOther> other = pMap.get(i + "");
            	if (other == null)
                    continue;
            	for (NewGkTimetableOther t : other) {
                    String weekday = String.valueOf(t.getDayOfWeek() + 1);
                    json.put("subjectName" + weekday, course.getSubjectName());
                    json.put("placeId" + weekday, t.getPlaceId());
                    json.put("otherId" + weekday, t.getId());
                    json.put("dayOfWeek" + weekday, t.getDayOfWeek());
                    json.put("periodInterval" + weekday, t.getPeriodInterval());
                    json.put("firstsdWeek" + weekday, t.getFirstsdWeek());
                    json.put("period" + weekday, t.getPeriod());
                    json.put("haveConflict" + weekday, t.isHaveConflict());
//                    String teacherid="";
//                    if(teachMap.get(t.getTimetableId())!=null){
//                    	teacherid = teachMap.get(t.getTimetableId()).getTeacherId();
//                    }
//                    json.put("teacherId" + weekday, teacherid);
                }
                values.put("p" + i, json);
            }
            map.put("values", values);
            map.put("timeId", timeId);
            map.put("allNum", allNum);
            map.put("intervalMap", intervalMap);
            
            Integer mmCount = grade.getMornPeriods();
    		Integer amCount = grade.getAmLessonCount();
    		Integer pmCount = grade.getPmLessonCount();
    		Integer nightCount = grade.getNightLessonCount();
    	
    		Map<String,Integer> piMap = new LinkedHashMap<>();
    		Function<Integer,Integer> fun = e-> e!=null?e:0;
    		piMap.put(BaseConstants.PERIOD_INTERVAL_1, fun.apply(mmCount));
    		piMap.put(BaseConstants.PERIOD_INTERVAL_2, fun.apply(amCount));
    		piMap.put(BaseConstants.PERIOD_INTERVAL_3, fun.apply(pmCount));
    		piMap.put(BaseConstants.PERIOD_INTERVAL_4, fun.apply(nightCount));
//            Map<String, Integer> piMap = getIntervalMap(grade);
            map.put("weekDays", grade.getWeekDays());
            map.put("piMap", piMap);
            map.put("intervalNameMap", BaseConstants.PERIOD_INTERVAL_Map2);
            map.put("dayOfWeekMap", BaseConstants.dayOfWeekMap2);
		}
		
		
		map.put("arrayId", arrayId);
		map.put("classId", classId);
		return "newgkelective/arrayResult/arrayConflictList.ftl";
	}
	@RequestMapping("/arrayLesson/checkSaveOther/page")
	@ControllerInfo(value = "冲突checkSaveOther")
	@ResponseBody
	public String checkSaveOther(@PathVariable String arrayId,HttpServletRequest request,ModelMap map){
		try {
			String otherStr=request.getParameter("otherStr");
			String[] otherIdAndPlaceId=request.getParameter("otherIdAndPlaceId").split(",");
			String classId=request.getParameter("classId");
			//String timeId=request.getParameter("timeId");
			//String otherId=otherIdAndPlaceId[0];
			String placeId="";
			if(otherIdAndPlaceId!=null &&otherIdAndPlaceId.length>1){
				placeId=otherIdAndPlaceId[1];
			}
			
			int lastIndexOf = otherStr.lastIndexOf(',');
			String timeStr = otherStr.substring(0, lastIndexOf);
			int weekType = Integer.parseInt(otherStr.substring(lastIndexOf+1));
			
			Map<String,NewGkTimetable> timetableMap=new HashMap<String,NewGkTimetable>();
	    	//key- placeId+坐标点      value-timeids暂定
			Map<String,List<NewGkTimetableOther>> lastPlaceMap=new HashMap<String,List<NewGkTimetableOther>>();
			//key- studentId+坐标点      value-timeids暂定
			Map<String,List<NewGkTimetableOther>> lastStudentMap=new HashMap<String,List<NewGkTimetableOther>>();
			//key- teacherid+坐标点      value-timeids暂定
			//Map<String,List<NewGkTimetableOther>> lastTeacheridtMap=new HashMap<String,List<NewGkTimetableOther>>();
			
			newGkTimetableService.getIndex(arrayId, null, null, timetableMap, lastPlaceMap, lastStudentMap);//,lastTeacheridtMap);
			
//			List<NewGkClassStudent> classStudentList=newGkClassStudentService.findListByClassIds(new String[]{classId});
			List<NewGkClassStudent> classStudentList = newGkClassStudentService.findListBy("classId", classId);
			
			if(CollectionUtils.isNotEmpty(classStudentList)){
				for(NewGkClassStudent classStudent:classStudentList){
					if(lastStudentMap.containsKey(classStudent.getStudentId()+","+timeStr)){
						List<NewGkTimetableOther> list = lastStudentMap.get(classStudent.getStudentId()+","+timeStr);
						for (NewGkTimetableOther to : list) {
							if(to.getFirstsdWeek() + weekType != 3) {
								return returnError("-1", "引起学生冲突");
							}
						}
					}
				}
			}
			if(StringUtils.isNotBlank(placeId) && lastPlaceMap.containsKey(placeId+","+timeStr)){
				List<NewGkTimetableOther> list = lastPlaceMap.get(placeId+","+timeStr);
				for (NewGkTimetableOther to : list) {
					if(to.getFirstsdWeek() + weekType != 3) {
						return returnError("-1", "引起场地冲突");
					}
				}
			}
//			if(otherIdAndPlaceId.length==3){
//				if(lastTeacheridtMap.containsKey(otherIdAndPlaceId[2]+","+otherStr)){
//					return returnError("-1", "引起教师冲突");
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	@RequestMapping("/arrayLesson/justSaveOther/page")
	@ControllerInfo(value = "冲突justSaveOther")
	@ResponseBody
	public String justSaveOther(@PathVariable String arrayId,HttpServletRequest request,ModelMap map){
		try {
			String otherStr=request.getParameter("otherStr");
			String[] otherIdAndPlaceId=request.getParameter("otherIdAndPlaceId").split(",");
			String timeId=request.getParameter("timeId");
			String otherId=otherIdAndPlaceId[0];
			String placeId="";
			if(otherIdAndPlaceId!=null &&otherIdAndPlaceId.length>1){
				placeId=otherIdAndPlaceId[1];
			}
			newGkTimetableOtherService.saveOne(getLoginInfo().getUnitId(), otherStr, placeId, timeId,otherId);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	/**
	 * 设置冲突的坐标点  字段haveConflict为true
	 * @param pMap
	 * @param lastMap
	 * @param timeId
	 * @param intervalMap
	 */
	public void getPMap( Map<String,List<NewGkTimetableOther>> pMap,Map<String,List<NewGkTimetableOther>> lastMap,
			String timeId,Map<String, Integer> intervalMap){
		for(Entry<String,List<NewGkTimetableOther>> entry:lastMap.entrySet()){
        	//String studentId=entry.getKey();
        	List<NewGkTimetableOther> inList=entry.getValue();
        	if(inList.size()>1){
        		for(NewGkTimetableOther other:inList){
        			if(other.getTimetableId().equals(timeId)){
        				int period=other.getPeriod();
        				period=getPeriod(period, other.getPeriodInterval(), intervalMap);
        				List<NewGkTimetableOther> list=pMap.get(period+"");
        				if(CollectionUtils.isNotEmpty(list)){
        					for(NewGkTimetableOther other2:list){
        						if(other2.getDayOfWeek().equals(other.getDayOfWeek())){
        							other2.setHaveConflict(true);
        						}
        					}
        				}
        				pMap.put(period+"", list);
        			}
        		}
        	}
        }
	}
    
    
    
   
    
	/**
	 * 获取实际的节次
	 * @param period
	 * @param periodInterval
	 * @param intervalMap
	 * @return
	 */
	public int getPeriod(int period,String periodInterval,Map<String, Integer> intervalMap){
		if(periodInterval.equals(BaseConstants.PERIOD_INTERVAL_2)){
			if(intervalMap.get(periodInterval)==0){
//        		continue;
        	}else{
        		period += intervalMap.get(BaseConstants.PERIOD_INTERVAL_1);
        	}
    	}else if(periodInterval.equals(BaseConstants.PERIOD_INTERVAL_3)){
    		if(intervalMap.get(BaseConstants.PERIOD_INTERVAL_3)==0){
        		//continue;
        	}else{
        		period=period+intervalMap.get(BaseConstants.PERIOD_INTERVAL_1)+intervalMap.get(BaseConstants.PERIOD_INTERVAL_2);
        	}
    	}else if(periodInterval.equals(BaseConstants.PERIOD_INTERVAL_4)){
    		if(intervalMap.get(BaseConstants.PERIOD_INTERVAL_4)==0){
        		//continue;
        	}else{
        		period=period +intervalMap.get(BaseConstants.PERIOD_INTERVAL_1)+intervalMap.get(BaseConstants.PERIOD_INTERVAL_3)+intervalMap.get(BaseConstants.PERIOD_INTERVAL_2);
        	}
    	}
		return period;
	}
	
	@ResponseBody
	@RequestMapping("/array/delete/page")
    @ControllerInfo(value = "删除界面")
	public String showDelete(@PathVariable("arrayId")String arrayId,ModelMap map){
		try{
			newGkArrayService.deleteById(this.getLoginInfo().getUnitId(), arrayId);
		}catch (Exception e) {
	         e.printStackTrace();
	         return returnError("操作失败！", e.getMessage());
	     }
		 return success("操作成功！");

	}

    @RequestMapping("/arrayStudent/conflictIndex/page")
    @ControllerInfo(value = "学生时间冲突")
    public String arrayStudentIndex(@PathVariable final String arrayId,ModelMap map) {
    	//取得所有数据 查询学生时间冲突
    	Set<String> stuIds=new HashSet<String>();
    	String unitId = getLoginInfo().getUnitId();
    	List<NewGkTimetable> timeList = newGkTimetableService.findByArrayId(unitId, arrayId);
    	Map<String,List<String>> timeByClassId=new HashMap<String,List<String>>();
    	Set<String> classIds=new HashSet<String>();
    	if(CollectionUtils.isNotEmpty(timeList)){
    		newGkTimetableService.makeTime(unitId, arrayId, timeList);
    		for(NewGkTimetable n:timeList){
    			classIds.add(n.getClassId());
    			if(!timeByClassId.containsKey(n.getClassId())){
    				timeByClassId.put(n.getClassId(), new ArrayList<String>());
    			}
    			if(CollectionUtils.isNotEmpty(n.getTimeList())){
    				for (NewGkTimetableOther other : n.getTimeList()) {
						String timetr=other.getDayOfWeek()+"_"+other.getPeriodInterval()+"_"+other.getPeriod();
						//一个时间点重复上同样的课  时间list也放两份
						timeByClassId.get(n.getClassId()).add(timetr);
    				}
    			}
    		}
    	}
    	
    	Map<String,Set<String>> classIdsByClassId=new HashMap<String,Set<String>>();
    	if(classIds.size()>0){
    		makeStuToClass(classIds.toArray(new String[]{}),classIdsByClassId);
    	}
    	if(classIdsByClassId.size()>0){
    		for(Entry<String, Set<String>> item:classIdsByClassId.entrySet()){
    			String studentId=item.getKey();
    			Set<String> classIdes = item.getValue();
    			
    			List<String> allTimes=new ArrayList<String>();
    			Set<String> setTimes=new HashSet<String>();
    			for (String string : classIdes) {
    				if(timeByClassId.containsKey(string)){
    					allTimes.addAll(timeByClassId.get(string));
        			}
				}
    			if(CollectionUtils.isNotEmpty(allTimes)){
    				setTimes.addAll(allTimes);
    				if(setTimes.size()<allTimes.size()){
    					stuIds.add(studentId);
    				}
    			}
    		}
    	}
    	List<Student> stuList=new ArrayList<Student>();
    	if(stuIds.size()>0){
    		stuList=SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[]{})), new TR<List<Student>>(){});
    	}
    	map.put("arrayId", arrayId);
    	map.put("stuList", stuList);
    	NewGkArray gkArray = newGkArrayService.findById(arrayId);
		String divideId = gkArray.getDivideId();
		NewGkDivide gkDivide = newGkDivideService.findById(divideId);
		//最大值+5
		int maxClassNum=gkDivide.getGalleryful()+gkDivide.getMaxGalleryful()+5;
    	map.put("classNum", maxClassNum);
    	map.put("divideId", divideId);
    	return "/newgkelective/arrayConflict/conflictStudentIndex.ftl";
    }

	private void makeStuToClass(String[] divideClassIds,
			Map<String, Set<String>> classIdsByStudent) {
		List<NewGkClassStudent> list = newGkClassStudentService.findListByIn("classId",divideClassIds);
		if(CollectionUtils.isNotEmpty(list)){
			for(NewGkClassStudent stu:list){
				if(!classIdsByStudent.containsKey(stu.getStudentId())){
					classIdsByStudent.put(stu.getStudentId(), new HashSet<String>());
				}
				classIdsByStudent.get(stu.getStudentId()).add(stu.getClassId());
			}
		}
	
	}
	@RequestMapping("/arrayStudent/conflictList/page")
    @ControllerInfo(value = "学生冲突点")
    public String arrayStudentList(@PathVariable final String arrayId,String studentId,ModelMap map) {
		NewGkArray gkArray = newGkArrayService.findById(arrayId);
		
		//查找学生所在班级
//		String divideId = gkArray.getDivideId();
		NewGkDivide gkDivide = newGkDivideService.findById(gkArray.getDivideId());
		List<NewGkClassStudent> classIdStus=newGkClassStudentService.findListByDivideStudentId(arrayId,new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2},new String[]{studentId},NewGkElectiveConstant.CLASS_SOURCE_TYPE2);
		Set<String> classIds = EntityUtils.getSet(classIdStus, "classId");
		List<NewGkTimetable> timeList=new ArrayList<NewGkTimetable>();
		timeList =newGkTimetableService.findByArrayId(gkArray.getUnitId(), arrayId);
		
		Set<String> allTime=new HashSet<String>();
		Map<String,String> sameMap=new HashMap<String,String>();
		NkStudentCourseDto stuCourseDto=new NkStudentCourseDto();
		//行政班
		Map<String,Map<String,List<NewGkTimetableOther>>> xzbSubjectTime=new HashMap<String,Map<String,List<NewGkTimetableOther>>>();
		//教学班
		Map<String,Map<String,List<NewGkTimetableOther>>> jxbSubjectTime=new HashMap<String,Map<String,List<NewGkTimetableOther>>>();
		Set<String> subjectIds=new HashSet<String>();
		if(CollectionUtils.isNotEmpty(timeList)){
			newGkTimetableService.makeTime(gkArray.getUnitId(), arrayId, timeList);
			for(NewGkTimetable newGkTimetable:timeList){
				//所有数据
				if(NewGkElectiveConstant.SUBJECT_TYPE_O.equals(newGkTimetable.getSubjectType())){
					if(!xzbSubjectTime.containsKey(newGkTimetable.getClassId())){
						xzbSubjectTime.put(newGkTimetable.getClassId(), new HashMap<String,List<NewGkTimetableOther>>());
					}
					xzbSubjectTime.get(newGkTimetable.getClassId()).put(newGkTimetable.getSubjectId(), newGkTimetable.getTimeList());
				}else{
					if(!jxbSubjectTime.containsKey(newGkTimetable.getClassId())){
						jxbSubjectTime.put(newGkTimetable.getClassId(), new HashMap<String,List<NewGkTimetableOther>>());
					}
					jxbSubjectTime.get(newGkTimetable.getClassId()).put(newGkTimetable.getSubjectId()+"_"+newGkTimetable.getSubjectType(), newGkTimetable.getTimeList());
				}
				
				if(classIds.contains(newGkTimetable.getClassId())){
					subjectIds.add(newGkTimetable.getSubjectId());
					if(CollectionUtils.isNotEmpty(newGkTimetable.getTimeList())){
						for (NewGkTimetableOther other : newGkTimetable.getTimeList()) {
							String timetr=other.getDayOfWeek()+"_"+other.getPeriodInterval()+"_"+other.getPeriod();
							if(allTime.contains(timetr)){
								sameMap.put(timetr, timetr);
							}else{
								allTime.add(timetr);
							}
	    				}
	    			}
				}
				
			}
		}
		
		stuCourseDto.setXzbSubjectTime(xzbSubjectTime);
		stuCourseDto.setJxbSubjectTime(jxbSubjectTime);
		Map<String,String> courseNameMap=SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
		for(String key:courseNameMap.keySet()){
			courseNameMap.put(key+"_"+NewGkElectiveConstant.SUBJECT_TYPE_A, courseNameMap.get(key));
			courseNameMap.put(key+"_"+NewGkElectiveConstant.SUBJECT_TYPE_B, courseNameMap.get(key));
		}
		map.put("courseNameMap",courseNameMap);
		
		List<NewGkDivideClass> list = newGkDivideClassService.findByDivideIdAndClassType(gkArray.getUnitId(), arrayId, new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2},false, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
		List<NewGkDivideClass> searchXzbList=new ArrayList<NewGkDivideClass>();
		Map<String,List<NewGkDivideClass>> searchJxbList=new HashMap<String,List<NewGkDivideClass>>();
		List<NewGkDivideClass> jxbList=new ArrayList<NewGkDivideClass>();
		for(NewGkDivideClass clazz:list){
			if(NewGkElectiveConstant.CLASS_TYPE_1.equals(clazz.getClassType())){
				if(classIds.contains(clazz.getId())){
					stuCourseDto.setXzbId(clazz.getId());
					stuCourseDto.setXzbName(clazz.getClassName());
				}
				searchXzbList.add(clazz);
			}else{
				if(!searchJxbList.containsKey(clazz.getSubjectIds()+"_"+clazz.getSubjectType())){
					searchJxbList.put(clazz.getSubjectIds()+"_"+clazz.getSubjectType(), new ArrayList<NewGkDivideClass>());
				}
				if(classIds.contains(clazz.getId())){
					jxbList.add(clazz);
				}
				searchJxbList.get(clazz.getSubjectIds()+"_"+clazz.getSubjectType()).add(clazz);
			}
		}
		stuCourseDto.setSearchJxbList(searchJxbList);
		stuCourseDto.setSearchXzbList(searchXzbList);
		stuCourseDto.setJxbList(jxbList);
		//行政班重组   行政班数据也能动
		boolean isCongZu=(NewGkElectiveConstant.DIVIDE_TYPE_02.equals(gkDivide.getOpenType()) 
				|| NewGkElectiveConstant.DIVIDE_TYPE_06.equals(gkDivide.getOpenType()))?true:false;
		map.put("stuCourseDto", stuCourseDto);
		map.put("isCongZu", isCongZu);
		map.put("sameMap", sameMap);
	    //科目  班级名称 上课时间 （红色 时间冲突）该科目其他班级列表
		map.put("studentId", studentId);
		map.put("arrayId", arrayId);
	    return "/newgkelective/arrayConflict/conflictStudentList.ftl";
	}
	@ResponseBody
	@RequestMapping("/arrayStudent/saveStudentAllClass")
    @ControllerInfo(value = "保存学生班级")
	public String saveStudentAllClass(@PathVariable("arrayId")String arrayId,String studentId,String xzbId,String jxbIds,ModelMap map){
		try{
			String unitId = getLoginInfo().getUnitId();
			Set<String> classIds=new HashSet<String>();
			classIds.add(xzbId);
			String[] jxbIdArr = jxbIds.split(",");
			for (String string : jxbIdArr) {
				classIds.add(string);
			}
			Set<String> allTime=new HashSet<String>();
			Set<String> sameMap=new HashSet<String>();
			//取得classIds 课表 判断时间有没有冲突
			List<NewGkTimetable> list = newGkTimetableService.findByArrayIdAndClassIds(unitId, arrayId, classIds.toArray(new String[]{}));
			if(CollectionUtils.isNotEmpty(list)){
				newGkTimetableService.makeTime(unitId, arrayId, list);
				for(NewGkTimetable newGkTimetable:list){
					if(CollectionUtils.isNotEmpty(newGkTimetable.getTimeList())){
						for (NewGkTimetableOther other : newGkTimetable.getTimeList()) {
							String timetr=other.getDayOfWeek()+"_"+other.getPeriodInterval()+"_"+other.getPeriod();
							if(allTime.contains(timetr)){
								sameMap.add(other.getTimeTr());
							}else{
								allTime.add(timetr);
							}
	    				}
	    			}
				}
			}
			if(sameMap.size()>0){
				String mess="时间冲突：";
				mess=mess+ArrayUtil.print(sameMap.toArray(new String[]{}));
				return error(mess);
			}else{
				NewGkArray gkArray = newGkArrayService.findById(arrayId);
				//查找学生所在班级
				String divideId = gkArray.getDivideId();
				List<NewGkClassStudent> deleList = newGkClassStudentService.findListByDivideStudentId(divideId,  new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2},new String[]{studentId},NewGkElectiveConstant.CLASS_SOURCE_TYPE2);
				Set<String> delIds = EntityUtils.getSet(deleList, "id");
				List<NewGkClassStudent> insertList=new ArrayList<NewGkClassStudent>();
				NewGkClassStudent stu=null;
				for(String s: classIds){
					stu=new NewGkClassStudent();
					stu.setId(UuidUtils.generateUuid());
					stu.setClassId(s);
					stu.setStudentId(studentId);
					stu.setCreationTime(new Date());
					stu.setModifyTime(new Date());
					stu.setDivideId(gkArray.getId());
					stu.setUnitId(gkArray.getUnitId());
					insertList.add(stu);
				}
				newGkClassStudentService.saveAndDel(delIds.toArray(new String[]{}),insertList.toArray(new NewGkClassStudent[]{}));
			}
		}catch (Exception e) {
	         e.printStackTrace();
	         return returnError("操作失败！", e.getMessage());
	     }
		 return success("操作成功！");

	}   
	
	
	@RequestMapping("/arrayStudent/sameList/page")
    @ControllerInfo(value = "学生可以移动的所有组合")
    public String sameChooseStudentList(@PathVariable final String arrayId,String studentId,ModelMap map) {
		NewGkArray gkArray = newGkArrayService.findById(arrayId);
		//查找学生所在班级
//		String divideId = gkArray.getDivideId();
		NewGkDivide gkDivide = newGkDivideService.findById(gkArray.getDivideId());
		//行政班重组   行政班数据也能动
		boolean isCongZu=(NewGkElectiveConstant.DIVIDE_TYPE_02.equals(gkDivide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_06.equals(gkDivide.getOpenType()))?true:false;
		isCongZu=true;
		//页面最后值
		Map<String,List<NewGkDivideClass>> returnList=new HashMap<String,List<NewGkDivideClass>>();
		//同学生
		String choiceId = gkDivide.getChoiceId();
//		//找到某个学生的学选课数据
		List<NewGkChoResult> list = newGkChoResultService.findByChoiceIdAndStudentIdAndKindType(gkArray.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,choiceId, studentId);
		Set<String> subjectIds = EntityUtils.getSet(list, "subjectId");
//		//同组合数据
		List<StudentResultDto> result = newGkChoResultService.findGradeIdList(gkArray.getUnitId(),gkArray.getGradeId(),choiceId,ArrayUtil.print(subjectIds.toArray(new String[]{})));
//		
//		//同组合学生数据
		Map<String,List<String>> classIdsByStudentId=new HashMap<String,List<String>>();
		Map<String,Set<String>> xzbStuByClassId=new HashMap<String,Set<String>>();
		String xzbId="";
//		//组合数
//				
		if(CollectionUtils.isNotEmpty(result)){
			Set<String> studentIds = EntityUtils.getSet(result, "studentId");
			
			List<NewGkClassStudent> classIdStus=newGkClassStudentService.findListByDivideStudentId(arrayId,new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2},studentIds.toArray(new String[]{}),NewGkElectiveConstant.CLASS_SOURCE_TYPE2);
			Set<String> ids=EntityUtils.getSet(classIdStus, "classId");
			List<NewGkDivideClass> divideClassList = newGkDivideClassService.findListByIds(ids.toArray(new String[]{}));
			newGkDivideClassService.makeStuNum(gkArray.getUnitId(),arrayId, divideClassList);
			//人数
			Map<String, NewGkDivideClass> divideClassMap = EntityUtils.getMap(divideClassList, "id");
			for(NewGkClassStudent s:classIdStus){
				if(!classIdsByStudentId.containsKey(s.getStudentId())){
					classIdsByStudentId.put(s.getStudentId(), new ArrayList<String>());
				}
				classIdsByStudentId.get(s.getStudentId()).add(s.getClassId());
				if(NewGkElectiveConstant.CLASS_TYPE_1.equals(divideClassMap.get(s.getClassId()).getClassType())){
					if(!xzbStuByClassId.containsKey(s.getClassId())){
						xzbStuByClassId.put(s.getClassId(), new HashSet<String>());
					}
					xzbStuByClassId.get(s.getClassId()).add(s.getStudentId());
					if(s.getStudentId().equals(studentId)){
						xzbId=s.getClassId();
					}
				}
			}
			List<List<String>> classGroup=new ArrayList<List<String>>();
			Set<String> sameClassId=new HashSet<String>();
			Set<String> allClassIds=new HashSet<String>();
			for(Entry<String, List<String>> item:classIdsByStudentId.entrySet()){
				String key = item.getKey();
				List<String> value = item.getValue();
				if(!isCongZu){
					if(!xzbStuByClassId.get(xzbId).contains(key)){
						//不是同班同学
						continue;
					}
				}
				String[] sortValue = value.toArray(new String[]{});
				Arrays.sort(sortValue);
				String sortIds = ArrayUtil.print(sortValue);
				if(sameClassId.contains(sortIds)){
					continue;
				}
				sameClassId.add(sortIds);
				classGroup.add(value);
				allClassIds.addAll(value);
			}
			if(classGroup.size()>0){
//				//时间是否数据
				List<NewGkTimetable> timetable = newGkTimetableService.findByArrayIdAndClassIds(gkArray.getUnitId(), arrayId, allClassIds.toArray(new String[]{}));
				newGkTimetableService.makeTime(gkArray.getUnitId(), arrayId, timetable);
//				//classId 
				Map<String,List<NewGkTimetableOther>> classTime=new HashMap<String,List<NewGkTimetableOther>>();
				for(NewGkTimetable newGkTimetable:timetable){
					if(!classTime.containsKey(newGkTimetable.getClassId())){
						classTime.put(newGkTimetable.getClassId(), new ArrayList<NewGkTimetableOther>());
					}
					classTime.get(newGkTimetable.getClassId()).addAll(newGkTimetable.getTimeList());
				}
//				//判断时间重复
				boolean ff=false;
				for(List<String> ll:classGroup){
					ff=false;
					Set<String> allTime=new HashSet<String>();
					List<NewGkDivideClass> dd=new ArrayList<NewGkDivideClass>();
					for(String t:ll){
						List<NewGkTimetableOther> tttList = classTime.get(t);
						if(CollectionUtils.isNotEmpty(tttList)){
							for (NewGkTimetableOther other : tttList) {
								String timetr=other.getDayOfWeek()+"_"+other.getPeriodInterval()+"_"+other.getPeriod();
								if(allTime.contains(timetr)){
									ff=true;
									break;
								}else{
									allTime.add(timetr);
								}
		    				}
							
		    			}
						if(ff){
							break;
						}
						dd.add(divideClassMap.get(t));
					}
					if(ff){
						continue;
					}
					//排序
					Collections.sort(dd, new Comparator<NewGkDivideClass>(){

						@Override
						public int compare(NewGkDivideClass o1,
								NewGkDivideClass o2) {
							if(!o2.getClassType().equals(o1.getClassType())){
								 return o2.getClassType().compareTo(o1.getClassType());
							}
							if(StringUtils.isNotBlank(o2.getSubjectType()) 
									&& StringUtils.isNotBlank(o1.getSubjectType()) ){
								return o2.getSubjectType().compareTo(o1.getSubjectType());
							}
							return 0;
						}
						
					});
					
					String[] sortValue = ll.toArray(new String[]{});
					Arrays.sort(sortValue);
					String sortIds = ArrayUtil.print(sortValue);
					returnList.put(sortIds, dd);
				}
				
				
			}
			
			
		}
		map.put("factList", returnList);
		map.put("studentId", studentId);
		map.put("isCongZu",isCongZu);
	    return "/newgkelective/arrayConflict/conflictSameList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/arrayStudent/saveBySameStudent")
    @ControllerInfo(value = "保存学生班级")
	public String saveBySameStudent(@PathVariable("arrayId")String arrayId,String studentId,String classIdstr,ModelMap map){
		try{
			Set<String> classIds=new HashSet<String>();
			String[] classIdArr = classIdstr.split(",");
			for (String string : classIdArr) {
				classIds.add(string);
			}
			NewGkArray gkArray = newGkArrayService.findById(arrayId);
			//查找学生所在班级
			String divideId = gkArray.getDivideId();
			List<NewGkClassStudent> deleList = newGkClassStudentService.findListByDivideStudentId(divideId,  new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2},new String[]{studentId},NewGkElectiveConstant.CLASS_SOURCE_TYPE2);
			Set<String> delIds = EntityUtils.getSet(deleList, "id");
			List<NewGkClassStudent> insertList=new ArrayList<NewGkClassStudent>();
			NewGkClassStudent stu=null;
			for(String s: classIds){
				stu=new NewGkClassStudent();
				stu.setId(UuidUtils.generateUuid());
				stu.setClassId(s);
				stu.setStudentId(studentId);
				stu.setCreationTime(new Date());
				stu.setModifyTime(new Date());
				stu.setDivideId(gkArray.getId());
				stu.setUnitId(gkArray.getUnitId());
				insertList.add(stu);
			}
			newGkClassStudentService.saveAndDel(delIds.toArray(new String[]{}),insertList.toArray(new NewGkClassStudent[]{}));
		}catch (Exception e) {
	         e.printStackTrace();
	         return returnError("操作失败！", e.getMessage());
	     }
		 return success("操作成功！");

	}   
	
	@ResponseBody
	@RequestMapping("/arrayStudent/saveAllBySameStudent")
    @ControllerInfo(value = "保存学生班级")
	public String saveAllSame(@PathVariable("arrayId")String arrayId){
		try{
			makeConfinct2(arrayId);
		}catch (Exception e) {
	         e.printStackTrace();
	         return returnError("操作失败！", e.getMessage());
	     }
		 return success("操作成功！");

	}   
	/**
	 * 每一种选课组合 可选择的教学班组合数据  key:subjectIdABs
	 * @param subjectIdABs 例如 '物理A,化学A,历史A,生物B,地理B,政治B'
	 * @param subjectIdTypeMap 例如物理A下所有班级
	 * @param timeByClassId
	 * @return
	 */
	//subjectA,subjectB
	private Map<String,List<String>> makeJXBList(Set<String> subjectIdABs,Map<String,Set<String>> subjectIdTypeMap,Map<String,List<String>> timeByClassId){
		Map<String,List<String>> map=new HashMap<String,List<String>>();
		for(String s:subjectIdABs){
			String[] subjectIdsType = s.split(",");
			List<String> returnlist=new ArrayList<String>();
			for(String ss:subjectIdsType){
				returnlist=convent(returnlist, subjectIdTypeMap.get(ss),timeByClassId);
				if(CollectionUtils.isEmpty(returnlist)){
					//没有找到
					break;
				}
			}
			map.put(s, returnlist);
		}
		return map;
	}
	
	
	
	
	/**
	 * 每一种选课组合 可选择的教学班+行政班组合数据 key:jxbMap.key
	 * @param jxbMap 每一种选课组合 可选择的教学班组合数据
	 * @param timeByClassId 班级的上课时间点
	 * @param xzbIds
	 * @param isCongzu
	 * @param xzbId
	 * @return
	 */
	private Map<String,List<String>> makeXZBJXB(Map<String, List<String>> jxbMap,Map<String,List<String>> timeByClassId,Set<String> xzbIds,boolean isCongzu,String xzbId){
		Map<String,List<String>> map=new HashMap<String,List<String>>();
		Set<String> canChooseXzbId=new HashSet<String>();
		if(isCongzu){
			canChooseXzbId.addAll(xzbIds);
		}else{
			canChooseXzbId.add(xzbId);
		}
		//重组
		for(Entry<String, List<String>> list:jxbMap.entrySet()){
			String key = list.getKey();
			List<String> valueList = list.getValue();
			if(CollectionUtils.isNotEmpty(valueList)){
				valueList=convent(valueList,canChooseXzbId,timeByClassId);
				map.put(key,valueList);
			}else{
				map.put(key, new ArrayList<String>());
			}
		}
		return map;
	}
	/**
	 * 迭代 排列组合  例如'物理A1班,化学A1班,地理B1班'
	 * @param list  例如 '物理A1班,化学A1班'
	 * @param set   例如  地理B所有班
	 * @param timeByClassId
	 * @return
	 */
	private List<String> convent(List<String> list,Set<String> set,Map<String, List<String>> timeByClassId){
		List<String> returnList=new ArrayList<String>();
		if(CollectionUtils.isEmpty(list)){
			returnList.addAll(set);
		}else{
			for(String s:set){
				for(String s1:list){
					String classStr = s1+","+s;
					String[] classArr = classStr.split(",");
					if(checkTime(classArr,timeByClassId)){
						continue;
					}
					returnList.add(classStr);
				}
			}
		}
		return returnList;
	}
	/**
	 * 组装班级上课时间数据
	 * @param timeList
	 * @param timeByClassId
	 */
	private void makeClassTimes(String unitId, String arrayId,List<NewGkTimetable> timeList,Map<String,List<String>> timeByClassId){
		//组装时间数据
		newGkTimetableService.makeTime(unitId, arrayId, timeList);
		for(NewGkTimetable n:timeList){
			if(!timeByClassId.containsKey(n.getClassId())){
				timeByClassId.put(n.getClassId(), new ArrayList<String>());
			}
			if(CollectionUtils.isNotEmpty(n.getTimeList())){
				for (NewGkTimetableOther other : n.getTimeList()) {
					String timetr=other.getDayOfWeek()+"_"+other.getPeriodInterval()+"_"+other.getPeriod();
					//一个时间点重复上同样的课  时间list也放两份
					timeByClassId.get(n.getClassId()).add(timetr);
				}
			}
		}
	}
	/**
	 * @param divideClassList 整体数据
	 * 
	 * @param divideClassMap 
	 * @param stuNumByClassId 班级学生数量
	 * @param allClassIdByStudentId 学生所在班级 包括教学班 行政班
	 * @param xzbClassIdByStudentId  key studentId value xzbId
	 * @param jxbSubjectByStudentId  key:studentId value subjectId+subjectType
	 * @param classIdBySubject  key 32个0 行政班  subjectId+subjectType(教学班)
	 */
	private void makeDivideClassItem(List<NewGkDivideClass> divideClassList,Map<String,NewGkDivideClass> divideClassMap,Map<String,Integer> stuNumByClassId,
			Map<String,Set<String>> allClassIdByStudentId,Map<String,String> xzbClassIdByStudentId,
			Map<String,Set<String>> jxbSubjectByStudentId,
			Map<String,Set<String>> classIdBySubject){
		//选择同组合 防止选课数据不一致 直接根据教学班来判断同组合
		for(NewGkDivideClass divide:divideClassList){
			divideClassMap.put(divide.getId(), divide);
			stuNumByClassId.put(divide.getId(), divide.getStudentList().size());
			boolean isClassType1=NewGkElectiveConstant.CLASS_TYPE_1.equals(divide.getClassType());
			if(isClassType1){
				if(!classIdBySubject.containsKey(BaseConstants.ZERO_GUID)){
					classIdBySubject.put(BaseConstants.ZERO_GUID, new HashSet<String>());
				}
				classIdBySubject.get(BaseConstants.ZERO_GUID).add(divide.getId());
			}else{
				String key=divide.getSubjectIds()+divide.getSubjectType();
				if(!classIdBySubject.containsKey(key)){
					classIdBySubject.put(key, new HashSet<String>());
				}
				classIdBySubject.get(key).add(divide.getId());
			}
			for(String ss:divide.getStudentList()){
				if(isClassType1){
					xzbClassIdByStudentId.put(ss, divide.getId());
				}else{
					if(!jxbSubjectByStudentId.containsKey(ss)){
						jxbSubjectByStudentId.put(ss, new HashSet<String>());
					}
					jxbSubjectByStudentId.get(ss).add(divide.getSubjectIds()+divide.getSubjectType());	
				}
				if(!allClassIdByStudentId.containsKey(ss)){
					allClassIdByStudentId.put(ss, new HashSet<String>());
				}
				allClassIdByStudentId.get(ss).add(divide.getId());
			}
		}
				
	}
	
	/**
	 * 根据同学生选课结果  同化学生教学班 行政班
	 * @param arrayId
	 * @return
	 */
	private boolean makeConfinct2(String arrayId) {
		NewGkArray gkArray = newGkArrayService.findById(arrayId);
		//所有上课数据
		List<NewGkTimetable> timeList = newGkTimetableService.findByArrayId(gkArray.getUnitId(),arrayId);
		if(CollectionUtils.isEmpty(timeList)){
			//没有数据
			return false;
		}
		/**
		 * key:班级  value 上课时间
		 */
		Map<String,List<String>> timeByClassId=new HashMap<String,List<String>>();
		makeClassTimes(gkArray.getUnitId(),arrayId,timeList, timeByClassId);
		/**
		 * 分班的班级数据
		 */
		
//		String divideId = gkArray.getDivideId();
		NewGkDivide gkDivide = newGkDivideService.findById(gkArray.getDivideId());
		boolean isCongZu=(NewGkElectiveConstant.DIVIDE_TYPE_02.equals(gkDivide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_06.equals(gkDivide.getOpenType()))?true:false;
		
		String[] classTypes=new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2};
		List<NewGkDivideClass> divideClassList = newGkDivideClassService.findByDivideIdAndClassType(gkDivide.getUnitId(),arrayId,classTypes,true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);

		Map<String,NewGkDivideClass> divideClassMap=new HashMap<String,NewGkDivideClass>();
		//班级学生数量
		Map<String,Integer> stuNumByClassId=new HashMap<String,Integer>();
		//学生所在班级 包括教学班 行政班
 		Map<String,Set<String>> allClassIdByStudentId=new HashMap<String,Set<String>>();
		//key studentId value xzbId
 		Map<String,String> xzbClassIdByStudentId=new HashMap<String,String>();
 		//key:studentId value subjectId+subjectType
		Map<String,Set<String>> jxbSubjectByStudentId=new HashMap<String,Set<String>>();
		//key 32个0 行政班  subjectId+subjectType(教学班)
		Map<String,Set<String>> classIdBySubject=new HashMap<String,Set<String>>();
		
		makeDivideClassItem(divideClassList, divideClassMap, stuNumByClassId,
				allClassIdByStudentId, xzbClassIdByStudentId, 
				jxbSubjectByStudentId, classIdBySubject);
		Set<String> xzbIds=classIdBySubject.get(BaseConstants.ZERO_GUID);
		//所有选课组合
		Set<String> subjectIdABs=new HashSet<String>();
		//key:学生id value:组合
		Map<String,String> subjectIdsBystudentId=new HashMap<String,String>();
		for(Entry<String, Set<String>> item:jxbSubjectByStudentId.entrySet()){
			String stuId = item.getKey();
			Set<String> set = item.getValue();
			String[] arr = set.toArray(new String[]{});
			Arrays.sort(arr);
			String subjectIds=ArrayUtil.print(arr);
			subjectIdABs.add(subjectIds);
			subjectIdsBystudentId.put(stuId, subjectIds);
		}
		
		//key:选课组合 value:教学班时间不冲突组合
		Map<String, List<String>> jxbGroupMap = makeJXBList(subjectIdABs, classIdBySubject, timeByClassId);
		
		
		//取得所有冲突学生
		Set<String> errorstuIds=new HashSet<String>();
		if(allClassIdByStudentId.size()>0){
    		for(Entry<String, Set<String>> item:allClassIdByStudentId.entrySet()){
    			String studentId=item.getKey();
    			Set<String> classIdes = item.getValue();
    			Set<String> setTimes=new HashSet<String>();
    			int allSize=0;
    			for (String string : classIdes) {
    				if(timeByClassId.containsKey(string)){
    					setTimes.addAll(timeByClassId.get(string));
    					allSize=allSize+timeByClassId.get(string).size();
        			}
				}
    			if(CollectionUtils.isNotEmpty(setTimes)){
    				setTimes.addAll(setTimes);
    				if(setTimes.size()<allSize){
    					errorstuIds.add(studentId);
    				}
    			}
    		}
    	}
		//最大值+0,+1,+2,+3,+4,+5
		int galleryfulClassNum=gkDivide.getGalleryful()+gkDivide.getMaxGalleryful();
		for(int i=0;i<=5;i++){
			int maxClassNum=galleryfulClassNum+i;
			while(true){
				if(errorstuIds.size()>0){
					boolean f=false;
					Set<String> removeStuIds = new HashSet<String>();
					for(String s:errorstuIds){
						//针对一个学生
						//先判断这个学生是否还有冲突
						Set<String> allTimes=new HashSet<String>();
						int timesize=0;
		    			Set<String> oldClassIds = allClassIdByStudentId.get(s);
		    			for (String string : oldClassIds) {
		    				if(timeByClassId.containsKey(string)){
		    					allTimes.addAll(timeByClassId.get(string));
		    					timesize=timesize+timeByClassId.get(string).size();
		        			}
						}
		    			if(CollectionUtils.isNotEmpty(allTimes)){
		    				if(timesize>allTimes.size()){
		    					
		    					//所有情况 排列组合
		    					String xzbId=xzbClassIdByStudentId.get(s);
		    					String jxbneedsubject = subjectIdsBystudentId.get(s);
		    					if(CollectionUtils.isEmpty(jxbGroupMap.get(jxbneedsubject))){
		    						System.out.println("没有可调整的组合班级："+s);
		    						continue;
		    					}
		    					Map<String, List<String>> oneSubjects = new HashMap<String,List<String>>();
		    					oneSubjects.put(jxbneedsubject, new ArrayList<String>());
		    					List<String> jxblist = jxbGroupMap.get(jxbneedsubject);
		    					oneSubjects.get(jxbneedsubject).addAll(jxblist);
		    					Map<String, List<String>> oneMap = makeXZBJXB(oneSubjects, timeByClassId, xzbIds, isCongZu, xzbId);
		    					if(CollectionUtils.isEmpty(oneMap.get(jxbneedsubject))){
		    						System.out.println("没有可调整的组合班级："+s);
		    						continue;
		    					}
		    					//value classIds 以，隔开
		    					List<String> allCanMoveList = oneMap.get(jxbneedsubject);
		    					//value classId
		    					List<String> newClassId=findBest(allCanMoveList, stuNumByClassId, maxClassNum,oldClassIds);
		    					if(CollectionUtils.isEmpty(newClassId)){
		    						System.out.println("没有找到班级人数所在班级在"+maxClassNum+"以下："+s);
		    						continue;
		    					}
		    					
		    					try{
		    						saveStudent(s, gkArray, newClassId);
		    						System.out.println("解决冲突成功学生：id"+s);
		    					}catch(Exception e){
		    						//保存失败
		    						System.out.println("解决冲突失败学生：id"+s);
		    						continue;
		    					}
		    					//保存
		    					//学生数据
		    					for(String oldId:oldClassIds){
		    						stuNumByClassId.put(oldId, stuNumByClassId.get(oldId)-1);
		    					}
		    					allClassIdByStudentId.put(s, new HashSet<String>());
		    					for(String newId:newClassId){
		    						stuNumByClassId.put(newId, stuNumByClassId.get(newId)+1);
		    						allClassIdByStudentId.get(s).add(newId);
		    						if(NewGkElectiveConstant.CLASS_TYPE_1.equals(divideClassMap.get(newId).getClassType())){
		    							xzbClassIdByStudentId.put(s, newId);
		    						}
		    					}
		    					removeStuIds.add(s);
		    					if(!f){
		    						f=true;
		    					}
								continue;
		    				}else{
		    					//没有冲突
		    					removeStuIds.add(s);
		    					if(!f){
		    						f=true;
		    					}
								continue;
		    				}
		    			}
						
						
					}
					if(f){
						errorstuIds.removeAll(removeStuIds);
						//有解决某个学生冲突
						continue;
					}else{
						//没有可调整的学生
						break;
					}
				}else{
					//没有冲突
					break;
				}
			}
		}
		
    	if(errorstuIds.size()>0){
    		return false;
    	}else{
    		return true;
    	}
   
	}
	/**
	 * 找到班级数量不超过maxClassNum 且取得其中班级数最小的组合
	 * @param allClassIdsList
	 * @param stuNumByClassId
	 * @param maxClassNum
	 * @param oldClassIds (移动到原来相同班级，不考虑人数变化)
	 * @return 
	 */
	private List<String> findBest(List<String> allClassIdsList,Map<String,Integer> stuNumByClassId,int maxClassNum,Set<String> oldClassIds){
		int min=-1;
		List<String> chooseList=new ArrayList<String>();
		for(String classIds:allClassIdsList){
			List<String> ll = Arrays.asList(classIds.split(","));
			int max1=0;
			int min1=0;
			for(String t:ll){
				if(oldClassIds.contains(t)){
					//不考虑人数
					continue;
				}
				if(max1==0 && min1==0){
					max1=stuNumByClassId.get(t);
					min1=stuNumByClassId.get(t);
				}else{
					if(stuNumByClassId.get(t)>max1){
						max1=stuNumByClassId.get(t);
					}else if(stuNumByClassId.get(t)<min1){
						min1=stuNumByClassId.get(t);
					}
				}
			}
			if(max1>=maxClassNum){
				continue;
			}

			if(min<0){
				min=min1;
				chooseList=ll;
			}else if(min1<min){
				min=min1;
				chooseList=ll;
			}
		}
		return chooseList;
	}
	
	@ResponseBody
	@RequestMapping("/arrayStudent/balanceClass")
    @ControllerInfo(value = "平衡班级人数")
	public String balanceClass(@PathVariable String arrayId,String fromClassId,String toClassId,int moveNum){
//		fromClassId="40504037403181708312903344240415";
//		// 
//		toClassId="40504076914856910326177552946990";
//		moveNum=8;
		System.out.println("移动学生");
		if(moveNum<=0){
			return success("没有可以移动的学生");
		}
		
		NewGkArray gkArray = newGkArrayService.findById(arrayId);
		//所有上课数据
		List<NewGkTimetable> timeList = newGkTimetableService.findByArrayId(gkArray.getUnitId(), arrayId);
		if(CollectionUtils.isEmpty(timeList)){
			//没有数据
			return success("没有可以移动的学生");
		}
		/**
		 * key:班级  value 上课时间
		 */
		Map<String,List<String>> timeByClassId=new HashMap<String,List<String>>();
		makeClassTimes(gkArray.getUnitId(),arrayId,timeList, timeByClassId);
		
//		String divideId = gkArray.getDivideId();
		NewGkDivide gkDivide = newGkDivideService.findById(gkArray.getDivideId());
		boolean isCongZu= (NewGkElectiveConstant.DIVIDE_TYPE_02.equals(gkDivide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_06.equals(gkDivide.getOpenType()))?true:false;
		
		String[] classTypes=new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2};
		List<NewGkDivideClass> divideClassList = newGkDivideClassService.findByDivideIdAndClassType(gkDivide.getUnitId(),arrayId,classTypes,true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);

		Map<String,NewGkDivideClass> divideClassMap=new HashMap<String,NewGkDivideClass>();
		//班级学生数量
		Map<String,Integer> stuNumByClassId=new HashMap<String,Integer>();
		//学生所在班级 包括教学班 行政班
 		Map<String,Set<String>> allClassIdByStudentId=new HashMap<String,Set<String>>();
		//key studentId value xzbId
 		Map<String,String> xzbClassIdByStudentId=new HashMap<String,String>();
 		//key:studentId value subjectId+subjectType
		Map<String,Set<String>> jxbSubjectByStudentId=new HashMap<String,Set<String>>();
		//key 32个0 行政班  subjectId+subjectType(教学班)
		Map<String,Set<String>> classIdBySubject=new HashMap<String,Set<String>>();
		
		makeDivideClassItem(divideClassList, divideClassMap, stuNumByClassId,
				allClassIdByStudentId, xzbClassIdByStudentId, 
				jxbSubjectByStudentId, classIdBySubject);
		Set<String> xzbIds=classIdBySubject.get(BaseConstants.ZERO_GUID);
		//所有选课组合
		Set<String> subjectIdABs=new HashSet<String>();
		//key:学生id value:组合
		Map<String,String> subjectIdsBystudentId=new HashMap<String,String>();
		for(Entry<String, Set<String>> item:jxbSubjectByStudentId.entrySet()){
			String stuId = item.getKey();
			Set<String> set = item.getValue();
			String[] arr = set.toArray(new String[]{});
			Arrays.sort(arr);
			String subjectIds=ArrayUtil.print(arr);
			subjectIdABs.add(subjectIds);
			subjectIdsBystudentId.put(stuId, subjectIds);
		}
		//key:选课组合 value:教学班时间不冲突组合
		Map<String, List<String>> jxbGroupMap = makeJXBList(subjectIdABs, classIdBySubject, timeByClassId);
		

		//toClassId 与 fromClassId 同一类的
		//fromClassId班级下的学生 且不在toClassId
		//是否重组  如果不重组  行政班id 跟 toClassId 时间不能冲突
		List<String> studentIdList = findListCanEdit(toClassId, fromClassId, isCongZu, xzbIds, timeByClassId, allClassIdByStudentId);
		if(CollectionUtils.isEmpty(studentIdList)){
			return success("没有可以移动的学生");
		}
		//找到每个学生最佳的移动list--暂时不考虑一个学生调动之后改变班级人数
		//从最佳的移动list  找到一个不是toClassId 且不是oldClassIds的一个最小值
		Map<String,List<String>> newClassIds=new HashMap<String,List<String>>();
		Map<String,Integer> minMoveByStuId=new HashMap<String,Integer>();
		List<Integer> countMin=new ArrayList<Integer>();
		
		for(String stuId:studentIdList){
			System.out.println("学生：-====="+stuId);
			Set<String> needClassIds=new HashSet<String>();
			needClassIds.add(toClassId);
			String xzbId=xzbClassIdByStudentId.get(stuId);
			if(!isCongZu){
				needClassIds.add(xzbId);
			}
			//找到学生含有needClassIds的组合
			//所有情况 排列组合
			
			String jxbneedsubject = subjectIdsBystudentId.get(stuId);
			if(CollectionUtils.isEmpty(jxbGroupMap.get(jxbneedsubject))){
				System.out.println("没有可调整的组合班级1："+stuId);
				continue;
			}
			Map<String, List<String>> oneSubjects = new HashMap<String,List<String>>();
			oneSubjects.put(jxbneedsubject, new ArrayList<String>());
			List<String> jxblist = jxbGroupMap.get(jxbneedsubject);
			oneSubjects.get(jxbneedsubject).addAll(jxblist);
			Map<String, List<String>> oneMap = makeXZBJXB(oneSubjects, timeByClassId, xzbIds, isCongZu, xzbId);
			if(CollectionUtils.isEmpty(oneMap.get(jxbneedsubject))){
				System.out.println("没有可调整的组合班级2："+stuId);
				continue;
			}
			List<String> oneGroups = oneMap.get(jxbneedsubject);
			List<String> canGroups = new ArrayList<String>();
			for(String gg:oneGroups){
				boolean isCut=false;
				for(String s:needClassIds){
					if(gg.indexOf(s)<0){
						isCut=true;
						break;
					}
				}
				if(isCut){
					continue;
				}
				canGroups.add(gg);
			}
			if(CollectionUtils.isEmpty(canGroups)){
				System.out.println("没有可调整的组合班级3："+stuId);
				continue;
			}
			Set<String> oldClassIds = allClassIdByStudentId.get(stuId);
			//暂时不考虑人数限制  直接定义不大于65
			List<String> ll = findBest(canGroups, stuNumByClassId, 63, oldClassIds);
			int min1=1000;
			for(String t:ll){
				if(needClassIds.contains(t)){
					//不考虑固定班级人数
					continue;
				}
				if(oldClassIds.contains(t)){
					//不考虑原来班级人数 
					continue;
				}
				if(stuNumByClassId.get(t)<min1){
					min1=stuNumByClassId.get(t);
				}
			}
			minMoveByStuId.put(stuId, min1);
			countMin.add(min1);
			newClassIds.put(stuId, ll);
		}
		System.out.println(newClassIds.size());
		if(newClassIds.size()<=0){
			return success("没有可以移动的学生");
		}
		//取得minMoveByStuId值最小的moveNum个学生
		List<String> chooseStuList=findMoveNumStudents(countMin, minMoveByStuId, moveNum);
		
		try{
			saveStudents(chooseStuList.toArray(new String[]{}), gkArray, newClassIds);
			return success("成功移动"+chooseStuList.size()+"个学生");
					
		}catch(Exception e){
			//保存失败
			return success("移动失败");
		}

	}
	
	private List<String> findMoveNumStudents(List<Integer> countMin,Map<String,Integer> minMoveByStuId,int moveNum){
		List<String> returnList=new ArrayList<String>();
		if(minMoveByStuId.size()<=moveNum){
			String[] strr = minMoveByStuId.keySet().toArray(new String[]{});
			returnList=Arrays.asList(strr);
			return returnList;
		}else{
			//countMin排序 取得第moveNum个值
			Integer[] countArr = countMin.toArray(new Integer[]{});
			Arrays.sort(countArr);
			Integer avg = countArr[moveNum-1];
			for(Entry<String, Integer> item:minMoveByStuId.entrySet()){
				if(item.getValue()<=avg){
					returnList.add(item.getKey());
				}
				if(returnList.size()>=moveNum){
					break;
				}
			}
			return returnList;
		}
	}

	private List<String> findListCanEdit(String toClassId,String fromClassId,boolean isCongzu,Set<String> xzbIds,
			Map<String,List<String>> timeByClassId,Map<String,Set<String>> allClassIdByStudentId){
		List<String> stuList=new ArrayList<String>();
		Set<String> sameZxbIds=new HashSet<String>();
		if(!isCongzu){
			//与classId 时间不冲突的行政班下的学生
			sameZxbIds=findNoErrorByClassId(toClassId, timeByClassId, xzbIds);
			if(CollectionUtils.isEmpty(sameZxbIds)){
				return new ArrayList<String>();
			}
		}
		Set<String> jxbIds=new HashSet<String>();
		jxbIds.add(fromClassId);
		//不在classId 但在jxbIds也在sameZxbIds
		for(Entry<String, Set<String>> item:allClassIdByStudentId.entrySet()){
			String studentId=item.getKey();
			Set<String> stuClassIds = item.getValue();
			if(stuClassIds.contains(toClassId)){
				continue;
			}
			if(!isCongzu){
				//stuClassIds 在sameZxbIds
				if(!jiaoji(stuClassIds, sameZxbIds)){
					continue;
				}
			}
			
			if(!jiaoji(stuClassIds,jxbIds)){
				continue;
			}
			stuList.add(studentId);
		}
		return stuList;
	}
	/**
	 * 判断set1，set2是否有交集
	 * @param set1
	 * @param set2
	 * @return
	 */
	private boolean jiaoji(Set<String> set1,Set<String> set2){
		Set<String> ss=new HashSet<String>();
		ss.addAll(set1);
		ss.addAll(set2);
		if(ss.size()<set1.size()+set2.size()){
			return true;
		}
		return false;
	}
	
	private Set<String> findNoErrorByClassId(String classId,Map<String,List<String>> timeByClassId,Set<String> xzbId){
		Set<String> set=new HashSet<String>(); 
		List<String> cccList = timeByClassId.get(classId);
		for(String s:xzbId){
			Set<String> ss=new HashSet<String>();
			ss.addAll(cccList);
			List<String> cccList1 = timeByClassId.get(s);
			ss.addAll(cccList1);
			int ii=cccList.size()+cccList1.size();
			if(ss.size()<ii){
				//冲突
				continue;
			}
			set.add(s);
		}
		return set;
	}

	
	@RequestMapping("/arrayConflict/subjectBalanceIndex")
    @ControllerInfo(value = "")
    public String showBalanceIndex(@PathVariable String arrayId,String subjectId,ModelMap map) {
    	
    	List<NewGkDivideClass> divideClassList = newGkDivideClassService.findByDivideIdAndClassType(this.getLoginInfo().getUnitId(), arrayId, new String[]{NewGkElectiveConstant.CLASS_TYPE_2},true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
    	Set<String> subjectIdSet = EntityUtils.getSet(divideClassList, "subjectIds");
    	if(StringUtils.isBlank(subjectId)){
    		subjectId = subjectIdSet.toArray(new String[0])[0];
    	}
    	List<NewGkDivideClass> allClassList = new ArrayList<NewGkDivideClass>();
    	for (NewGkDivideClass divideClass : divideClassList) {
    		if(subjectId.equals(divideClass.getSubjectIds())){
    			allClassList.add(divideClass);
    		}
		}
    	List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdSet.toArray(new String[0])),Course.class);
    	Collections.sort(allClassList, new Comparator<NewGkDivideClass>() {
			@Override
			public int compare(NewGkDivideClass o1, NewGkDivideClass o2) {
				if(o1.getSubjectType().equals(o2.getSubjectType())){
					return o2.getStudentList().size()-o1.getStudentList().size();
				}else{
					return o2.getSubjectType().compareTo(o1.getSubjectType());
				}
				
			}
		});
    	for (NewGkDivideClass divideClass : allClassList) {
    		divideClass.setClassName(divideClass.getClassName()+"_"+divideClass.getStudentList().size());;
		}
    	
    	map.put("subjectId", subjectId);
    	map.put("courseList",courseList);
    	map.put("allClassList", allClassList);
    	map.put("arrayId", arrayId);
		return "/newgkelective/arrayConflict/divideClassSubjectId.ftl";
    }
    
    @RequestMapping("/arrayConflict/subjectBalanceList")
    @ControllerInfo(value = "")
    public String showSubjectBalanceList(@PathVariable String arrayId,String subjectId,String classId,ModelMap map) {
    	
    	List<NewGkDivideClass> divideClassList = newGkDivideClassService.findByDivideIdAndClassType(this.getLoginInfo().getUnitId(), arrayId, new String[]{NewGkElectiveConstant.CLASS_TYPE_2},true, NewGkElectiveConstant.CLASS_SOURCE_TYPE2, false);
    	List<NewGkDivideClass> allClassList = new ArrayList<NewGkDivideClass>();
    	for (NewGkDivideClass divideClass : divideClassList) {
    		if(subjectId.equals(divideClass.getSubjectIds())){
    			allClassList.add(divideClass);
    		}
		}
    	Collections.sort(allClassList, new Comparator<NewGkDivideClass>() {
			@Override
			public int compare(NewGkDivideClass o1, NewGkDivideClass o2) {
				if(o1.getSubjectType().equals(o2.getSubjectType())){
					return o2.getStudentList().size()-o1.getStudentList().size();
				}else{
					return o2.getSubjectType().compareTo(o1.getSubjectType());
				}
			}
		});
    	Set<String> subjectIdSet = EntityUtils.getSet(divideClassList, "subjectIds");
    	List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdSet.toArray(new String[0])),Course.class);
    	
    	NewGkDivideClass divideCla = newGkDivideClassService.findOne(classId);
    	
    	List<NewGkDivideClass> newClassList = new ArrayList<NewGkDivideClass>();
    	for (NewGkDivideClass divideClass : allClassList) {
    		divideClass.setClassName(divideClass.getClassName()+"_"+divideClass.getStudentList().size());
    		if(divideClass.getSubjectType().equals(divideCla.getSubjectType())){
    			if(divideClass.getId().equals(divideCla.getId())){
    				continue;
    			}
    			newClassList.add(divideClass);
    		}
		}
    	Collections.reverse(newClassList);
    	
    	if(newClassList.size()==0){
    		newClassList.add(divideCla);
    	}
    	map.put("newClassId", newClassList.get(0).getId());
    	map.put("subjectId", subjectId);
    	map.put("courseList",courseList);
    	map.put("classId", classId);
    	map.put("allClassList", allClassList);
    	map.put("newClassList", newClassList);
    	map.put("arrayId", arrayId);
		return "/newgkelective/arrayConflict/divideClassSubjectId.ftl";
    }

    @RequestMapping("/array/exportExcp")
    @ResponseBody
    public void exportExcp(@PathVariable String arrayId, HttpServletResponse resp) {
    	String key_msg = NewGkElectiveConstant.ARRAY_LESSON+"_"+arrayId+"_mess";	
    	String errorMsg = RedisUtils.get(key_msg);
    	
    	NewGkArray array = newGkArrayService.findOne(arrayId);
    	
    	resp.setHeader("Cache-Control", "");
        resp.setContentType("application/data;charset=UTF-8");
        String fileName = array.getArrayName()+"_排课异常信息.txt";
		fileName = Optional.ofNullable(UrlUtils.encode(fileName, "UTF-8")).orElse(fileName);

		resp.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		Writer writer = null;
		try {
			writer = resp.getWriter();
			int off = 0;
			int len = 4096;
			for(;off < errorMsg.length();off+=len) {
				if(off+len > errorMsg.length()) {
					writer.write(errorMsg, off, errorMsg.length() - off);
				}else {
					writer.write(errorMsg, off, len);
				}
			}
			writer.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}finally {
			if(writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
		
    }
}
