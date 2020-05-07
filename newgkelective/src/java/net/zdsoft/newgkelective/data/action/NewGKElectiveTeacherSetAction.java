package net.zdsoft.newgkelective.data.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.TeachGroup;
import net.zdsoft.basedata.entity.TeachGroupEx;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.TeachGroupExRemoteService;
import net.zdsoft.basedata.remote.service.TeachGroupRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.ChoiceTeacherDto;
import net.zdsoft.newgkelective.data.dto.NewGkSubjectTeacherDto;
import net.zdsoft.newgkelective.data.dto.SubjectInfo;
import net.zdsoft.newgkelective.data.entity.NewGkArrayItem;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkClassCombineRelation;
import net.zdsoft.newgkelective.data.entity.NewGkClassSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTime;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTimeEx;
import net.zdsoft.newgkelective.data.entity.NewGkOpenSubject;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlanEx;
import net.zdsoft.newgkelective.data.service.NewGkArrayItemService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkClassCombineRelationService;
import net.zdsoft.newgkelective.data.service.NewGkClassSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeExService;
import net.zdsoft.newgkelective.data.service.NewGkLessonTimeService;
import net.zdsoft.newgkelective.data.service.NewGkOpenSubjectService;
import net.zdsoft.newgkelective.data.service.NewGkSubjectTimeService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanExService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

/**
 * Created by Administrator on 2018/3/7.
 */
@Controller
@RequestMapping("/newgkelective/{divideId}")
public class NewGKElectiveTeacherSetAction extends BaseAction {
    @Autowired
    private NewGkDivideService newGkDivideService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
	private TeachGroupRemoteService teachGroupRemoteService;
	@Autowired
	private TeachGroupExRemoteService teachGroupExRemoteService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private NewGkArrayItemService newGkArrayItemService;
    @Autowired
    private NewGkTeacherPlanService newGkTeacherPlanService;
    @Autowired
    private NewGkOpenSubjectService newGkOpenSubjectService;
    @Autowired
    private NewGkDivideClassService newGkDivideClassService;
    @Autowired
    private NewGkTeacherPlanExService newGkTeacherPlanExService;
    @Autowired
	private NewGkLessonTimeService newGkLessonTimeService;
	@Autowired
	private NewGkLessonTimeExService newGkLessonTimeExService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private NewGkSubjectTimeService newGkSubjectTimeService;
	@Autowired
	private NewGkClassSubjectTimeService newGkClassSubjectTimeService;
	@Autowired
	private NewGkClassCombineRelationService newGkClassCombineRelationService;
    
    @RequestMapping("/teacherArray/index/page")
    @ControllerInfo(value = "教师安排首页")
    public String showTeacherArrayList(@PathVariable String divideId,String gradeId , HttpServletRequest request , String arrayId, ModelMap map){
        map.put("divideId", divideId);
        map.put("gradeId", gradeId);
        map.put("arrayId", arrayId);
        List<NewGkArrayItem> itemList =  newGkArrayItemService.findByDivideId(divideId,new String[]{NewGkElectiveConstant.ARRANGE_TYPE_02});
        Map<String , NewGkSubjectTeacherDto> subjectTeacherDtoMap = new HashMap<>();
        for(NewGkArrayItem item : itemList){
            NewGkSubjectTeacherDto dto = new NewGkSubjectTeacherDto();
            dto.setItemId(item.getId());
            dto.setItemName(item.getItemName());
            dto.setCreationTime(item.getCreationTime());
            subjectTeacherDtoMap.put(item.getId() , dto);
        }
        List<String> itemIdList = EntityUtils.getList(itemList,"id");
        List<NewGkTeacherPlan> teacherPlanList = newGkTeacherPlanService.findByArrayItemIds(itemIdList.toArray(new String[0]) ,true);
        if(CollectionUtils.isNotEmpty(teacherPlanList)){
            Set<String> subjectIds = EntityUtils.getSet(teacherPlanList , "subjectId");
            Map<String ,String> courseMap = getCourseNameMap(subjectIds);
            for(NewGkTeacherPlan plan : teacherPlanList){
                List<NewGkTeacherPlan> planList = subjectTeacherDtoMap.get(plan.getArrayItemId()).getTeacherPlanList();
                if(planList == null){
                    planList = new ArrayList<>();
                    subjectTeacherDtoMap.get(plan.getArrayItemId()).setTeacherPlanList(planList);
                }
                plan.setSubjectName(courseMap.get(plan.getSubjectId()));
                planList.add(plan);
            }
        }
        map.put("subjectTypeO", NewGkElectiveConstant.SUBJECT_TYPE_O);
        map.put("dtoMap", subjectTeacherDtoMap);
        return "/newgkelective/teacherArrange/teacherIndex.ftl";
    }
    private Map<String, String> getCourseNameMap(Set<String> subjectIds) {
        return SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
    }
    @RequestMapping("/subjectTeacherArrange/add")
    @ControllerInfo(value = "新增教师安排")
    public String doAdd(@PathVariable("divideId") String divideId,String gradeId,String arrayId, String itemId ,
    		String selectTeacher,String courseId, String useMaster, ModelMap map) {
    	List<NewGkTeacherPlan> planList = new ArrayList<NewGkTeacherPlan>();
    	if(StringUtils.isNotEmpty(itemId)){
    		if(Objects.equals(useMaster, "1")) {
    			planList = newGkTeacherPlanService.findByArrayItemIdsWithMaster(new String[]{itemId}, true);
    		}else {
    			planList = newGkTeacherPlanService.findByArrayItemIds(new String[]{itemId}, true);
    		}
    	}
    	
    	if(CollectionUtils.isNotEmpty(planList)&&!"1".equals(selectTeacher)){
    		return doChooseTeacherClassXzb(divideId, gradeId, arrayId, itemId, courseId, null,useMaster,planList, map);
    	}
    	map.put("selectTeacher", selectTeacher);
        map.put("arrayId", arrayId);
        map.put("courseId", courseId);
        String gradeName = "";
        map.put("gradeName", gradeName);
        map.put("gradeId", gradeId);
        map.put("divideId", divideId);
        map.put("itemId" ,itemId);
        
        List<NewGkSubjectTime> oldSujectTimeList = newGkSubjectTimeService.findByArrayItemId(itemId);
        Set<String> subjectIds = EntityUtils.getSet(oldSujectTimeList, e->e.getSubjectId());
//        List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService.findByDivideId(divideId);
        Map<String,List<String>> subjectIdToTeachGroupId = new HashMap<String, List<String>>();
        Map<String,List<String>> subjectIdToTeacherId = new HashMap<String, List<String>>();
//        Set<String> subjectIds = EntityUtils.getSet(openSubjectList, "subjectId");
        for (String string : subjectIds) {
            subjectIdToTeachGroupId.put(string, new ArrayList<String>());
            subjectIdToTeacherId.put(string, new ArrayList<String>());
        }
        if(CollectionUtils.isEmpty(subjectIds)) {
        	List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService.findByDivideId(divideId);
        	subjectIds = EntityUtils.getSet(openSubjectList,e->e.getSubjectId());
        	
        }
        
        List<Course> courseList= new ArrayList<>();
        if(CollectionUtils.isNotEmpty(subjectIds)) {
        	courseList=SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIds.toArray(new String[0])),Course.class);
        }
        Map<String, String> subjectIdToSubjectName=EntityUtils.getMap(courseList, Course::getId, Course::getSubjectName);
    	for(Course cs : courseList) {
			if(cs.getOrderId() == null) {
				cs.setOrderId(Integer.MAX_VALUE);
			}
		}
		Collections.sort(courseList, (x,y) -> x.getOrderId().compareTo(y.getOrderId()));
       
//        Map<String, String> subjectIdToSubjectName = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<Map<String, String>>(){});

        Map<String, List<String>> selectSubjectTeacherMap = new HashMap<>();
        for(NewGkTeacherPlan plan : planList){
            selectSubjectTeacherMap.put(plan.getSubjectId(),plan.getExTeacherIdList());
        }

        String unitId = getLoginInfo().getUnitId();
        List<TeachGroup> baseTeachGroupList = new ArrayList<>();
        Set<String> selectTgxKeys = new HashSet<>();
        if(CollectionUtils.isNotEmpty(subjectIds)) {
        	baseTeachGroupList = SUtils.dt(teachGroupRemoteService.findBySchoolIdAndSubjectIdIn(unitId,subjectIds.toArray(new String[0])),TeachGroup.class);
        	
        	for (TeachGroup baseTeachGroup : baseTeachGroupList) {
        		String subjectId = baseTeachGroup.getSubjectId();
        		if(subjectIdToTeachGroupId.containsKey(subjectId)){
        			subjectIdToTeachGroupId.get(subjectId).add(baseTeachGroup.getId());
        		}
        	}
        	Map<String,List<TeachGroup>> teachGroSubIds = EntityUtils.getListMap(baseTeachGroupList, TeachGroup::getSubjectId,e->e);
        	Map<String,TeachGroup> needNameCouIds = new HashMap<>();
        	for (String subId : selectSubjectTeacherMap.keySet()) {
        		List<String> tid = selectSubjectTeacherMap.get(subId);
        		if(CollectionUtils.isEmpty(tid)) 
        			continue;
        		if(!teachGroSubIds.containsKey(subId)) {
        			TeachGroup tg = new TeachGroup();
        			tg.setId(UuidUtils.generateUuid());
        			tg.setSubjectId(subId);
//        			tg.setTeachGroupName(teachGroupName);
        			needNameCouIds.put(subId, tg);
        			baseTeachGroupList.add(tg);
        			if(subjectIdToTeachGroupId.containsKey(subId)){
        				subjectIdToTeachGroupId.get(subId).add(tg.getId());
        			}
        			teachGroSubIds.put(subId, new ArrayList<>());
        			teachGroSubIds.get(subId).add(tg);
        		}
        		tid.stream().forEach(tid2->{
        			teachGroSubIds.get(subId).stream().forEach(teaGrp->selectTgxKeys.add(teaGrp.getId()+"-"+tid2));
        		});
        	}
        	Map<String, String> cnMap = SUtils.dt(courseRemoteService.findPartCouByIds(needNameCouIds.keySet().toArray(new String[0])), new TypeReference<Map<String,String>>() {});
        	needNameCouIds.values().stream().forEach(e->e.setTeachGroupName(cnMap.get(e.getSubjectId())+"组"));
        	
        }
        
        Set<String> baseTeachGroupIdSet = EntityUtils.getSet(baseTeachGroupList, e->e.getId());
        Map<String, String> teachGroupIdToName = EntityUtils.getMap(baseTeachGroupList, TeachGroup::getId, TeachGroup::getTeachGroupName);
        List<TeachGroupEx> baseTeachGroupExList = SUtils.dt(teachGroupExRemoteService.findByTeachGroupId(baseTeachGroupIdSet.toArray(new String[0])),TeachGroupEx.class);
        Set<String> tgxKeys = EntityUtils.getSet(baseTeachGroupExList, e->e.getTeachGroupId()+"-"+e.getTeacherId());
        selectTgxKeys.stream().filter(e->!tgxKeys.contains(e)).forEach(e->{
        	String[] split = e.split("-");
        	String teaGpId =  split[0];
        	String tId =  split[1];
        	TeachGroupEx ge = new TeachGroupEx();
        	ge.setTeacherId(tId);
        	ge.setTeachGroupId(teaGpId);
        	baseTeachGroupExList.add(ge);
        });
        
		Map<String, List<String>> teachGroupIdToTeacherId = EntityUtils.getListMap(baseTeachGroupExList, "teachGroupId", "teacherId");
        Map<String, List<String>> teacherIdToTeachGroupId = EntityUtils.getListMap(baseTeachGroupExList, "teacherId", "teachGroupId");
        
        Map<String, String> teacherIdToTeachGroupString = new HashMap<>();
        for (Map.Entry<String, List<String>> one : teacherIdToTeachGroupId.entrySet()) {
            teacherIdToTeachGroupString.put(one.getKey(), StringUtils.join(one.getValue(), ','));
        }
        map.put("teachGroupIdToName", teachGroupIdToName);
        map.put("teacherIdToTeachGroupId", teacherIdToTeachGroupString);
        map.put("subjectIdToTeachGroupId", subjectIdToTeachGroupId);

        for(Map.Entry<String,List<String>> entry : subjectIdToTeachGroupId.entrySet()) {
            String subjectId = entry.getKey();
            //teacherGroupId集合
            List<String> teacherGroupId = entry.getValue();
            for (String tgId : teacherGroupId) {
                List<String> teacherIds = teachGroupIdToTeacherId.get(tgId);
                if(CollectionUtils.isEmpty(teacherIds)){
                	continue;
                }
                for (String teacherId : teacherIds) {
                    subjectIdToTeacherId.get(subjectId).add(teacherId);
                }
            }
        }

        List<NewGkDivideClass> divideClassList = newGkDivideClassService.findByDivideIdAndClassType(unitId,divideId ,new String[]{NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2},false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
        NewGkDivide divide = newGkDivideService.findOne(divideId);
        boolean isXzbArray = NewGkElectiveConstant.DIVIDE_TYPE_07.equals(divide.getOpenType());
        Map<String , List<NewGkDivideClass>> divideClassMap = new HashMap<>();
        divideClassMap.put(NewGkElectiveConstant.SUBJECT_ID_XING_ZHENG, new ArrayList<>());
        for(NewGkDivideClass divideClass : divideClassList){
        	if(isXzbArray) {
        		if(NewGkElectiveConstant.CLASS_TYPE_1.equals(divideClass.getClassType())) {
        			if(StringUtils.isNotBlank(divideClass.getSubjectIds())) {
        				String[] split = divideClass.getSubjectIds().split(",");
        				for (String key : split) {
        					List<NewGkDivideClass> list = divideClassMap.get(key);
                    		if(list == null){
                    			list = new ArrayList<>();
                    			divideClassMap.put(key , list);
                    		}
                    		list.add(divideClass);
						}
        			}
        			divideClassMap.get(NewGkElectiveConstant.SUBJECT_ID_XING_ZHENG).add(divideClass);
        		}else {
        			String key = divideClass.getSubjectIds();
        			List<NewGkDivideClass> list = divideClassMap.get(key);
            		if(list == null){
            			list = new ArrayList<>();
            			divideClassMap.put(key , list);
            		}
            		list.add(divideClass);
        		}
        	}else {
        		String key = divideClass.getSubjectIds();
        		if(StringUtils.isEmpty(key)){
        			key = NewGkElectiveConstant.SUBJECT_ID_XING_ZHENG;
        		}
        		List<NewGkDivideClass> list = divideClassMap.get(key);
        		if(list == null){
        			list = new ArrayList<>();
        			divideClassMap.put(key , list);
        		}
        		list.add(divideClass);
        	}
        }

        Set<String> teacherIdSet = new HashSet<String>();
        Map<String,SubjectInfo> subjectInfoMap = new HashMap<String,SubjectInfo>();
        for(Map.Entry<String,List<String>> entry : subjectIdToTeacherId.entrySet()) {
            String subjectId = entry.getKey();
            subjectInfoMap.put(subjectId, new SubjectInfo());
            //设置subjectId
            subjectInfoMap.get(subjectId).setSubjectId(subjectId);
            //设置subjectName
            subjectInfoMap.get(subjectId).setSubjectName(subjectIdToSubjectName.get(subjectId));
            // 设置classNumber
            List<NewGkDivideClass> list = divideClassMap.get(subjectId);
            if(list == null){
                list = divideClassMap.get(NewGkElectiveConstant.SUBJECT_ID_XING_ZHENG);
            }
            if(CollectionUtils.isNotEmpty(list)){
            	 subjectInfoMap.get(subjectId).setClassNumber(list.size());
                 //设置teacherNumber
            	 if(list.size()%2==0){
            		 subjectInfoMap.get(subjectId).setTeacherNumber(list.size()/2);
            	 }else{
            		 subjectInfoMap.get(subjectId).setTeacherNumber(list.size()/2+1);
            	 }
                 
            }else{
            	subjectInfoMap.get(subjectId).setClassNumber(0);
                //设置teacherNumber
                subjectInfoMap.get(subjectId).setTeacherNumber(0);
            }
           
            List<String> teacherIds = entry.getValue();
            List<String> selectTeacherIds = selectSubjectTeacherMap.get(subjectId);
            Set<String> allTid = new HashSet<>();
            if(selectTeacherIds != null)
            	allTid.addAll(selectTeacherIds);
            allTid.addAll(teacherIds);
            for (String teacherId : allTid) {
                teacherIdSet.add(teacherId);
                subjectInfoMap.get(subjectId).getTeacherIdAndState().put(teacherId, "0");
            }
            //改变已经选择老师的状态
            
            int i = 0;
            if(selectTeacherIds != null && selectTeacherIds.size() > 0) {
                for (String teacherId : selectTeacherIds) {
                	//已经保存过 老师数据 后来在教研组删除这个老师造成的bug
                	if(allTid.contains(teacherId)) {
                		subjectInfoMap.get(subjectId).getTeacherIdAndState().put(teacherId, "1");
                        i++;
                	}
                    
                }
            }
            subjectInfoMap.get(subjectId).setSelectTeacherNumber(i);
        }
        //有序
        Map<String,SubjectInfo> subjectInfoMap2 = new LinkedHashMap<String, SubjectInfo>();
        //根据科目排序
        for(Course c:courseList) {
        	if(subjectInfoMap.containsKey(c.getId())) {
        		subjectInfoMap2.put(c.getId(), subjectInfoMap.get(c.getId()));
        	}
        }

        Map<String,String> teacherIdToTeacherName = SUtils.dt(teacherRemoteService.findPartByTeacher(teacherIdSet.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
        subjectInfoMap2.values().forEach(e->{
        	Iterator<String> it = e.getTeacherIdAndState().keySet().iterator();
        	while(it.hasNext()) {
        		String next = it.next();
        		if(!teacherIdToTeacherName.containsKey(next)) {
        			it.remove();
        		}
        	}
        });
        map.put("teacherIdToTeacherName", teacherIdToTeacherName);

        map.put("subjectInfoMap", subjectInfoMap2);

        map.put("teacherNumber", teacherIdSet.size() + "");


        return "/newgkelective/teacherArrange/teacherSetIndex.ftl";
    }
    
    @ResponseBody
    @RequestMapping("/subjectTeacherArrange/save")
    @ControllerInfo(value = "保存教师安排")
    public String doSave(@PathVariable("divideId") String divideId, String arrayId, NewGkSubjectTeacherDto subjectTeacherDto) {
    	String arrayItemId;
    	try{
            List<NewGkTeacherPlan> planList = subjectTeacherDto.getTeacherPlanList();
            if(CollectionUtils.isEmpty(planList)){
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("请选择老师 再进行下一步！"));
            }
            //为了返回新建的id
            arrayItemId = newGkTeacherPlanService.saveAddUpList(subjectTeacherDto.getItemId(),subjectTeacherDto.getTeacherPlanList(),divideId, arrayId,this.getLoginInfo().getUnitId());
        } catch (Exception e) {
        	e.printStackTrace();
	        return returnError();
	    }
	    return  Json.toJSONString(new ResultDto().setSuccess(true).setCode("00").setMsg("操作成功！").setBusinessValue(arrayItemId));
    }

    @RequestMapping("/teacherClass/selected/tab")
    @ControllerInfo(value = "选择老师对应的班级Tab页")
    public String selectTeacherClassTab(@PathVariable("divideId") String divideId ,String gradeId  , String arrayId , String arrayItemId ,ModelMap map ) {
		map.put("divideId" ,divideId);
		map.put("gradeId" , gradeId);
		map.put("arrayId" , arrayId);
		map.put("arrayItemId",arrayItemId);
    	return "/newgkelective/teacherArrange/teacherTab.ftl";
    }
    
    @RequestMapping("/getTeacherList/page")
    @ControllerInfo(value = "获取老师列表")
    public String getTeacherList(String arrayItemId,String teacherIds,String teacherPlanId,ModelMap map){
    	List<String> teacherIdList = newGkTeacherPlanExService.findByTeacherPlanId(teacherPlanId);
    	List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(teacherIdList.toArray(new String[0])),Teacher.class);
    	Map<String, String> teacherNameMap = EntityUtils.getMap(teacherList, "id","teacherName");
    	List<ChoiceTeacherDto> dtoList = new ArrayList<ChoiceTeacherDto>();
    	ChoiceTeacherDto dto = null;
    	for (String teacherId : teacherIdList) {
			dto = new ChoiceTeacherDto();
			dto.setTeacherId(teacherId);
			dto.setTeacherName(teacherNameMap.get(teacherId));
			if(StringUtils.isBlank(teacherIds) || teacherIds.contains(teacherId)){
				dto.setState("1");
			}else{
				dto.setState("0");
			}
			dtoList.add(dto);
		}
    	map.put("teacherPlanId", teacherPlanId);
    	map.put("choiceTeacherDtoList", dtoList);
    	return "/newgkelective/teacherArrange/getTeacherList.ftl";
    }
    //注释垃圾代码20190416
//    @RequestMapping("/teacherClass/choosedJxb")
//    @ControllerInfo(value = "选择老师对应的班级-走班2")
//    public String doChooseTeacherClassJxb(@PathVariable("divideId") String divideId ,String gradeId  , String arrayId , String arrayItemId , ModelMap map ) {
//    	map.put("divideId" ,divideId);
//		map.put("gradeId" , gradeId);
//		map.put("arrayId" , arrayId);
//		map.put("arrayItemId",arrayItemId);
//		List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeIn(divideId, new String[]{NewGkElectiveConstant.SUBJECT_TYPE_A,NewGkElectiveConstant.SUBJECT_TYPE_B});
//		Set<String> openSubjectIdSet = EntityUtils.getSet(openSubjectList, NewGkOpenSubject::getSubjectId);
//		List<NewGkTeacherPlan> teacherPlanList = newGkTeacherPlanService.findByArrayItemIdAndSubjectIdIn(arrayItemId,openSubjectIdSet.toArray(new String[0]),false);
//		Map<String, String> subjectPlanMap = EntityUtils.getMap(teacherPlanList, NewGkTeacherPlan::getSubjectId,NewGkTeacherPlan::getId);
//		String unitId =  getLoginInfo().getUnitId();
//		if(MapUtils.isNotEmpty(subjectPlanMap)){
//			List<String> teacherIdList = null;
//			String teacherId = null;
//			List<String> teacherNameList = null;
//			List<SubjectTeacherDto> dtoList = new ArrayList<SubjectTeacherDto>();
//			SubjectTeacherDto dto = null;
//			
//			List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findByUnitId(unitId),Teacher.class);
//			Map<String, String> teacherNameMap = EntityUtils.getMap(teacherList, Teacher::getId,Teacher::getTeacherName);
//			List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectPlanMap.keySet().toArray(new String[0])),Course.class);
//			Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId,Course::getSubjectName);
//			List<NewGkChoRelation> choRelationList = newGkChoRelationService.findByChoiceIdsAndObjectType(unitId,subjectPlanMap.values().toArray(new String[0]), NewGkElectiveConstant.CHOICE_TYPE_06);
//			Map<String,List<String>> planTeacherMap = new HashMap<String, List<String>>();
//			for (NewGkChoRelation choRelation : choRelationList) {
//				if(!planTeacherMap.containsKey(choRelation.getChoiceId())){
//					planTeacherMap.put(choRelation.getChoiceId(), new ArrayList<String>());
//				}
//				planTeacherMap.get(choRelation.getChoiceId()).add(choRelation.getObjectValue());
//			}
//			for (Entry<String, String> entry : subjectPlanMap.entrySet()) {
//				dto = new SubjectTeacherDto();
//				dto.setTeacherPlanId(entry.getValue());
//				dto.setSubjectId(entry.getKey());
//				dto.setSubjectName(courseNameMap.get(entry.getKey()));
//				teacherIdList = planTeacherMap.get(entry.getValue());
//				if(CollectionUtils.isNotEmpty(teacherIdList)){
//					teacherId = "";
//					teacherNameList = new ArrayList<String>();
//					for (String tid : teacherIdList) {
//						teacherId += ","+tid;
//						teacherNameList.add(teacherNameMap.get(tid));
//					}
//					dto.setTeacherIds(teacherId.substring(1));
//					dto.setTeacherNameList(teacherNameList);
//				}
//				dtoList.add(dto);
//			}
//			List<NewGkDivideClass> divideClassList = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId, new String[]{NewGkElectiveConstant.CLASS_TYPE_2}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
//			Map<String,Map<String,Integer>> subjectBatchMap = new HashMap<>();
//			for (NewGkDivideClass divideClass : divideClassList) {
//				if(divideClass.getBatch()==null){
//					continue;
//				}
//				if(!subjectBatchMap.containsKey(divideClass.getSubjectIds())){
//					subjectBatchMap.put(divideClass.getSubjectIds(), new HashMap<String, Integer>());
//				}
//				if(!subjectBatchMap.get(divideClass.getSubjectIds()).containsKey(divideClass.getSubjectType()+divideClass.getBatch())){
//					subjectBatchMap.get(divideClass.getSubjectIds()).put(divideClass.getSubjectType()+divideClass.getBatch(), 1);
//				}else{
//					int num = subjectBatchMap.get(divideClass.getSubjectIds()).get(divideClass.getSubjectType()+divideClass.getBatch());
//					num++;
//					subjectBatchMap.get(divideClass.getSubjectIds()).put(divideClass.getSubjectType()+divideClass.getBatch(),num);
//				}
//			}
//			Map<String,Integer> subjectNumMap = new HashMap<String, Integer>();
//			for (Entry<String, Map<String, Integer>> entry : subjectBatchMap.entrySet()) {
//				subjectNumMap.put(entry.getKey(),Collections.max(entry.getValue().values()));
//			}
//			
//			for (SubjectTeacherDto inDto : dtoList) {
//				inDto.setMinNum(subjectNumMap.get(inDto.getSubjectId()));
//			}
//			map.put("dtoList", dtoList);
//		}
//		
//        return "/newgkelective/teacherArrange/selectTeacherClass2.ftl";
//    }
    
    @RequestMapping("/teacherClass/choosedXzb")
    @ControllerInfo(value = "选择老师对应的班级-行政班和走班1")
    public String doChooseTeacherClassXzb(@PathVariable("divideId") String divideId ,String gradeId, 
    		String arrayId , String arrayItemId , String courseId , String type , String useMaster,
    		List<NewGkTeacherPlan> teacherPlanList, ModelMap map ) {
    	map.put("divideId" ,divideId);
        map.put("gradeId" , gradeId);
        map.put("arrayId" , arrayId);
        map.put("arrayItemId",arrayItemId);
        map.put("type", type);//type:1-行政班;2-教学班. 不在区分行政班教学班，一起设置 by20180504
//        List<NewGkTeacherPlan> teacherPlanList = null;
//        if(Objects.equals(useMaster, "1")) {
//        	teacherPlanList = newGkTeacherPlanService.findByArrayItemIdsWithMaster(new String[]{arrayItemId} ,true);
//        }else {
//        	teacherPlanList = newGkTeacherPlanService.findByArrayItemIds(new String[]{arrayItemId} ,true);
//        }
       
       	List<NewGkSubjectTime> sts = newGkSubjectTimeService.findByArrayItemId(arrayItemId);
       	Set<String> subjectSet = EntityUtils.getSet(sts, e->e.getSubjectId());
        Iterator<NewGkTeacherPlan> iterator = teacherPlanList.iterator();
        while(iterator.hasNext()){
        	if(!subjectSet.contains(iterator.next().getSubjectId())){
        		iterator.remove();
        	}
        }
        //教师基础数据
        List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findByUnitId(getLoginInfo().getUnitId()) ,Teacher.class);
        Map<String , String> teacherNameMap = EntityUtils.getMap(teacherList,"id","teacherName");
        
        Map<String ,List<NewGkTeacherPlanEx> > subjectTeacherPlanExMap = new HashMap<>();
        List<NewGkTeacherPlanEx> dels= new ArrayList<>();
		for(NewGkTeacherPlan plan : teacherPlanList){
            List<NewGkTeacherPlanEx> planExList = plan.getTeacherPlanExList();
            if(CollectionUtils.isNotEmpty(planExList)){
            	 Iterator<NewGkTeacherPlanEx> iter1 = planExList.iterator();  
                 while(iter1.hasNext()){
                	 NewGkTeacherPlanEx ex = iter1.next();
                	 if(ex == null) {
                		 iter1.remove();
                		 continue;
                	 }
                	 String teacherName = teacherNameMap.get(ex.getTeacherId());
                     if(StringUtils.isNotBlank(teacherName)){
                     	ex.setTeacherName(teacherName);
                     }else{
                     	dels.add(ex);
                     	iter1.remove();
                     }
                 }
            }
            subjectTeacherPlanExMap.put(plan.getSubjectId(),plan.getTeacherPlanExList());
        }
        map.put("subjectTeacherPlanExMap" ,subjectTeacherPlanExMap);
        Set<String> subjectIds = EntityUtils.getSet(teacherPlanList,"subjectId");
        
        if(CollectionUtils.isNotEmpty(dels)){
        	newGkTeacherPlanExService.deleteAll(dels.toArray(new NewGkTeacherPlanEx[0]));
        }
        /** 取科目名称 */
        List<Course> cls = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>(){});
        if(StringUtils.isNotBlank(courseId) && cls.parallelStream().filter(e->e.getId().equals(courseId)).count()>0){
        	map.put("courseId", courseId);
        }
        cls.sort(new Comparator<Course>() {
            @Override
            public int compare(Course o1, Course o2) {
                if (o1.getOrderId() != null && o2.getOrderId() != null) {
                    return o1.getOrderId().compareTo(o2.getOrderId());
                } else {
                    return 0;
                }
            }
        });
        map.put("courseList" ,cls);
        map.put("useMaster", useMaster);
        return "/newgkelective/teacherArrange/selectTeacherClass.ftl";
    }
    
   
    
    @RequestMapping("/teacherClass/selected")
    @ControllerInfo(value = "选择老师对应的班级")
    public String doSelectTeacherClass(@PathVariable("divideId") String divideId ,String gradeId  , 
			String arrayId, String arrayItemId, String courseId, String useMaster, ModelMap map) {
		map.put("divideId", divideId);
		map.put("gradeId", gradeId);
		map.put("arrayId", arrayId);
		map.put("arrayItemId", arrayItemId);
		if (StringUtils.isNotBlank(courseId)) {
			map.put("courseId", courseId);
		}else{
			return errorFtl(map, "参数丢失");
		}
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		LoginInfo info = getLoginInfo();
		// 科目老师数据整理
		List<NewGkTeacherPlan> teacherPlanList = null;
		if(Objects.equals(useMaster, "1")) {
			teacherPlanList = newGkTeacherPlanService.findByArrayItemIdAndSubjectIdInWithMaster(arrayItemId,
					new String[] { courseId }, true);
		}else {
			teacherPlanList = newGkTeacherPlanService.findByArrayItemIdAndSubjectIdIn(arrayItemId,
					new String[] { courseId }, true);
		}
		
		
		List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findByUnitId(info.getUnitId()),
				Teacher.class);
		Map<String, String> teacherNameMap = EntityUtils.getMap(teacherList, Teacher::getId, Teacher::getTeacherName);
		List<NewGkTeacherPlanEx> dels = new ArrayList<NewGkTeacherPlanEx>();
		// 科目详情
		List<NewGkTeacherPlanEx> planExList = new ArrayList<NewGkTeacherPlanEx>();
		// 已经安排的班级集合
		Set<String> hasSelCids = new HashSet<String>();
		Set<String> tids = new HashSet<String>();
		for (NewGkTeacherPlan plan : teacherPlanList) {
			List<NewGkTeacherPlanEx> exList = plan.getTeacherPlanExList();
			if (CollectionUtils.isNotEmpty(exList)) {
				Iterator<NewGkTeacherPlanEx> iter1 = exList.iterator();
				while (iter1.hasNext()) {
					NewGkTeacherPlanEx ex = iter1.next();
					if (ex == null) {
						iter1.remove();
						continue;
					}
					String teacherName = teacherNameMap.get(ex.getTeacherId());
					if (StringUtils.isNotBlank(teacherName)) {
						ex.setTeacherName(teacherName);
						tids.add(ex.getTeacherId());
						if (StringUtils.isNotEmpty(ex.getClassIds())) {
							String[] ids = ex.getClassIds().split(",");
							ex.setClassIdList(Arrays.asList(ids));
							for (String id : ids) {
								hasSelCids.add(id);
							}
						}
					} else {
						dels.add(ex);
						iter1.remove();
					}
				}
			}
			planExList = plan.getTeacherPlanExList();
		}
		if (CollectionUtils.isNotEmpty(dels)) {
			newGkTeacherPlanExService.deleteAll(dels.toArray(new NewGkTeacherPlanEx[0]));
		}

		List<NewGkDivideClass> divideClassList = newGkDivideClassService.findByDivideIdAndClassType(info.getUnitId(),
				divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_1, NewGkElectiveConstant.CLASS_TYPE_2,
						NewGkElectiveConstant.CLASS_TYPE_3,NewGkElectiveConstant.CLASS_TYPE_4},
				false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, true);
		List<NewGkDivideClass> xzbList = divideClassList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType())).collect(Collectors.toList());
		List<NewGkDivideClass> jxbList = divideClassList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType()))
				.filter(e->StringUtils.isNotBlank(e.getBatch())).collect(Collectors.toList());
		List<NewGkDivideClass> hbList = divideClassList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_3.equals(e.getClassType())).collect(Collectors.toList());
		List<NewGkDivideClass> fakeXzbList = divideClassList.stream().filter(e->NewGkElectiveConstant.CLASS_TYPE_4.equals(e.getClassType())).collect(Collectors.toList());
		
		Map<String, NewGkDivideClass> divideClassIdMap = EntityUtils.getMap(divideClassList, NewGkDivideClass::getId);
		
		List<String[]> batchKeys = new ArrayList<String[]>();// 左边班级分组
		
		// 获取课程的周课时
		List<NewGkSubjectTime> sts = newGkSubjectTimeService.findByArrayItemId(arrayItemId);
		Map<String, Integer> stMap = new HashMap<String, Integer>();
		boolean isXzbSub = false;
		if(CollectionUtils.isNotEmpty(sts)) {
			for(NewGkSubjectTime ti : sts) {
				if(StringUtils.equals(ti.getSubjectId(), courseId) && ti.getPeriod() != null) {
					stMap.put(ti.getSubjectId()+ti.getSubjectType(), ti.getPeriod());
				}
				if(courseId.equals(ti.getSubjectId()) && NewGkElectiveConstant.SUBJECT_TYPE_O.equals(ti.getSubjectType())) {
					isXzbSub = true;
                    if(Objects.equals(NewGkElectiveConstant.IF_INT_1,ti.getFollowZhb())){
                        xzbList = fakeXzbList;
                    }
				}
			}
		}
		Map<String,List<NewGkDivideClass>> bathClassMap = new HashMap<>();
		List<NewGkDivideClass> xzbs = new ArrayList<>();
		// 某行政班 在 此科目上课时的 科目类型 选/学
		Map<String,String> classSubjectTypes = new HashMap<>();
		if(isXzbSub) {
			xzbs = xzbList;
			xzbs.forEach(e->classSubjectTypes.put(e.getId(), NewGkElectiveConstant.SUBJECT_TYPE_O));
		}else if(NewGkElectiveConstant.DIVIDE_TYPE_01.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_02.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_05.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_06.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_07.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_08.equals(divide.getOpenType())
//				|| NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())
				){
			// 教学班科目 
			Map<String, List<String[]>> xzbSubjects = newGkDivideClassService.findXzbSubjects(divide.getUnitId(), divideId, arrayItemId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, 
					EntityUtils.getList(xzbList, e->e.getId()));
			processXzb(courseId, divideClassIdMap, xzbs, classSubjectTypes, xzbSubjects);
			processJxbs(courseId, hasSelCids, jxbList, batchKeys, bathClassMap);
		}else if(NewGkElectiveConstant.DIVIDE_TYPE_09.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divide.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType())) {
			// 3+1+2  单科分层/组合固定
			List<Course> course2List = SUtils.dt(courseRemoteService.findWuliLiShi(divide.getUnitId()), Course.class);
			List<String> courseId2List = EntityUtils.getList(course2List, Course::getId);
			boolean isWuliLishi = courseId2List.contains(courseId);
			
			String followType = divide.getFollowType();
			if(NewGkElectiveConstant.DIVIDE_TYPE_09.equals(divide.getOpenType())) {
				// 单科分层 重组 /不重组 物理历史 独立走班
				processJxbs(courseId, hasSelCids, jxbList, batchKeys, bathClassMap);
			}else if(NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divide.getOpenType())) {
				// 不重组 物理历史 跟随行政班
				if(isWuliLishi && (followType.contains(NewGkElectiveConstant.FOLLER_TYPE_A2) 
						|| followType.contains(NewGkElectiveConstant.FOLLER_TYPE_B2))) {
					processClassType3(divideClassList, courseId, xzbs, followType,classSubjectTypes);
				}
				processJxbs(courseId, hasSelCids, jxbList, batchKeys, bathClassMap);
			}else if(NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())) {
				// 固定重组;获取伪行政班
				Map<String, List<String[]>> xzbSubjects = newGkDivideClassService.findXzbSubjects(divide.getUnitId(), divideId, arrayItemId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, 
						EntityUtils.getList(xzbList, e->e.getId()));
				processXzb(courseId, divideClassIdMap, xzbs, classSubjectTypes, xzbSubjects);
				if(!isWuliLishi) {
					Map<String, List<String[]>> fakeXzbSubjects = newGkDivideClassService.findFakeXzbSubjects(divide.getUnitId(), divideId, arrayItemId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, 
							EntityUtils.getList(fakeXzbList, e->e.getId()));
					processXzb(courseId, divideClassIdMap, xzbs, classSubjectTypes, fakeXzbSubjects);
				}
				map.put("isFakeXzb", true);
//				xzbs = xzbList.stream().filter(e->e.getSubjectIds().contains(courseId)).collect(Collectors.toList());
//				processJxbs(courseId, hasSelCids, jxbList, batchKeys, bathClassMap);
			}else if(NewGkElectiveConstant.DIVIDE_TYPE_12.equals(divide.getOpenType())) {
				// 固定不重组
				if(isWuliLishi) {
					processClassType3(divideClassList, courseId, xzbs, followType,classSubjectTypes);
				}else {
					Map<String, List<String[]>> fakeXzbSubjects = newGkDivideClassService.findFakeXzbSubjects(divide.getUnitId(), divideId, arrayItemId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, 
							EntityUtils.getList(fakeXzbList, e->e.getId()));
					processXzb(courseId, divideClassIdMap, xzbs, classSubjectTypes, fakeXzbSubjects);
				}
//				processJxbs(courseId, hasSelCids, jxbList, batchKeys, bathClassMap);
				map.put("isFakeXzb", true);
			}
			
		}
		if(NewGkElectiveConstant.DIVIDE_TYPE_07.equals(divide.getOpenType())) {
			// 行政班排课 重置 batcKeys 和 batchClassMap
			Set<String> virtualCourseIds = batchKeys.stream().map(e->e[2]).collect(Collectors.toSet());
			Map<String, String> subNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(virtualCourseIds.toArray(new String[0])),
					new TypeReference<Map<String,String>>() {});
			int count=1;
			Map<String,String> batchMap = new HashMap<>();
			for (String[] bk : batchKeys) {
				if(!"xingzheng".equals(bk[1])) {
					bk[3] = subNameMap.get(bk[2]);
					batchMap.put(bk[2], String.valueOf(count++));
					bk[2] = batchMap.get(bk[2]);
				}
			}
			map.put("batchMap", batchMap);
		}
		xzbs.forEach(e->e.setSubjectType(""));
		//batchKeys[A_1,A,1,选考1] [xingzheng_,xingzheng,"",行政班] 
		if (xzbs.size() > 0 && !bathClassMap.containsKey(NewGkElectiveConstant.SUBJECT_ID_XING_ZHENG+"_")) {
			batchKeys.add(0, new String[] { NewGkElectiveConstant.SUBJECT_ID_XING_ZHENG+"_",NewGkElectiveConstant.SUBJECT_ID_XING_ZHENG,"", "行政班" });
		}
		if (xzbs.size() > 0) {
			bathClassMap.put(NewGkElectiveConstant.SUBJECT_ID_XING_ZHENG+"_", xzbs.stream().filter(e->!hasSelCids.contains(e.getId())).collect(Collectors.toList()));
		}
		
		List<NewGkClassSubjectTime> subjectTimeList = newGkClassSubjectTimeService.findByArrayItemIdAndClassIdIn(info.getUnitId(),
				arrayItemId,null,new String[] {courseId}, null);
		// 禁排时间显示
		List<NewGkLessonTime> times = newGkLessonTimeService.findByItemIdObjectId(arrayItemId,
				tids.toArray(new String[0]), new String[] { NewGkElectiveConstant.LIMIT_TEACHER_2 },
				true);
		Map<String,List<String>> mutxTidMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(times)) {
			Map<String, String> sids = EntityUtils.getMap(times, NewGkLessonTime::getId, NewGkLessonTime::getObjectId);
			// 互斥教师 
			List<NewGkChoRelation> res = newGkChoRelationService.findByChoiceIdsAndObjectType(info.getUnitId(), sids.keySet().toArray(new String[0]), NewGkElectiveConstant.CHOICE_TYPE_07);
			if(CollectionUtils.isNotEmpty(res)) {
				List<String> sb;
				for(NewGkChoRelation re : res) {
					String tid = sids.get(re.getChoiceId());
					sb = mutxTidMap.get(tid);
					if(sb == null) {
						sb = new ArrayList<>();
						mutxTidMap.put(tid, sb);
					} 
					sb.add(re.getObjectValue());
				}
			}
		}
		Map<String, Integer> csPeriodMap = EntityUtils.getMap(subjectTimeList, e->e.getClassId()+e.getSubjectId(),e->e.getPeriod());
		Map<String, Integer> stPeriodMap = EntityUtils.getMap(sts, e->e.getSubjectId()+e.getSubjectType(),e->e.getPeriod());
		Map<String,Integer> teaNoTimeMap = times.stream().filter(e->e.getTimesList()!=null)
				.collect(Collectors.toMap(e->e.getObjectId(), 
						e->(int)e.getTimesList().stream().map(t->t.getDayOfWeek()+"_"+t.getPeriodInterval()+"_"+t.getPeriod()).count()));
		//为每个班级 设置课时数
		List<String> allCids = bathClassMap.values().stream().flatMap(e->e.stream()).map(e->e.getId()).collect(Collectors.toList());
		allCids.addAll(hasSelCids);
		
		for (String cid : allCids) {
			NewGkDivideClass dc = divideClassIdMap.get(cid);
			if(dc == null) {
				continue;
			}
			String clsId = dc.getId();
			int classtime = 0;
			if(NewGkElectiveConstant.CLASS_TYPE_2.equals(dc.getClassType())) {
				Integer integer = stPeriodMap.get(dc.getSubjectIds()+dc.getSubjectType());
				if(integer != null) {
					classtime = integer;
				}
			}else {
				// 行政班
				if(csPeriodMap.containsKey(clsId+courseId)) {
					classtime += csPeriodMap.get(clsId+courseId);
				}else if(classSubjectTypes.containsKey(clsId)){
					String subType = classSubjectTypes.get(clsId);
					if(stPeriodMap.containsKey(courseId+ subType)) {
						classtime = stPeriodMap.get(courseId+ subType);
					}
				}
			}
			dc.setClassNum(classtime);
		}
		
		for (NewGkTeacherPlanEx ex : planExList) {
			List<String> classIdList = ex.getClassIdList();
			// 禁排时间
			ex.setNoTimeStr("0");
			if(teaNoTimeMap.containsKey(ex.getTeacherId())) {
				ex.setNoTimeStr(String.valueOf(teaNoTimeMap.get(ex.getTeacherId())));
			}
			// 互斥教师
			List<String> mutexIds = mutxTidMap.get(ex.getTeacherId());
			if(CollectionUtils.isNotEmpty(mutexIds)) {
				mutexIds = mutexIds.stream().filter(e->teacherNameMap.containsKey(e))
						.collect(Collectors.toList());
				String mutexNames = mutexIds.stream()
						.map(e->teacherNameMap.get(e))
						.collect(Collectors.joining(","));
				
				ex.setMutexTeaIds(String.join(",", mutexIds));
				ex.setMutexTeaIdList(mutexIds);
				
				ex.setMutexTeaNames(mutexNames);
				//ex.setMutexNum(mutexIds.size());
			}
			if(CollectionUtils.isNotEmpty(classIdList)) {
				// 周课时
				int count = 0;
				for (String clsId : classIdList) {
					NewGkDivideClass dc = divideClassIdMap.get(clsId);
					
					if(dc.getClassNum() !=null) {
						count += dc.getClassNum();
					}
				}
				ex.setWeekTime(String.valueOf(count));
			}
		}
	    // 排序
		bathClassMap.values().forEach(e->e.sort((x,y)->{
			if(x.getClassType() != null && y.getClassType() != null && x.getClassType().compareTo(y.getClassType()) != 0) {
				return x.getClassType().compareTo(y.getClassType());
			}
			if(x.getOrderId() != null && y.getOrderId() != null && x.getOrderId().compareTo(y.getOrderId()) != 0) {
				return x.getOrderId().compareTo(y.getOrderId());
			}else if(x.getClassName() != null) {
				return x.getClassName().compareTo(Optional.ofNullable(y.getClassName()).orElse(""));
			}
			return 0;
		}));
		batchKeys.sort((x,y)->{
			if(x[1].equals(NewGkElectiveConstant.SUBJECT_ID_XING_ZHENG)) {
				return 1;
			}else if(y[1].equals(NewGkElectiveConstant.SUBJECT_ID_XING_ZHENG)) {
				return -1;
			}
			int a = x[1].compareTo(y[1]);
			if(a!=0) {
				return a;
			}
			
			return x[2].compareTo(y[2]);
		});
		
		
		map.put("xzCls", NewGkElectiveConstant.SUBJECT_ID_XING_ZHENG+"_");
		map.put("batchKeys", batchKeys);
		map.put("bathClassMap", bathClassMap);// 具体教学班信息
		map.put("divideClassIdMap", divideClassIdMap);
		
		// TODO 固定教学班课程 老师
		
		// map.put("subjectTeacherPlanExMap" ,subjectTeacherPlanExMap);
		map.put("planExList", planExList);
		map.put("teacherNameMap", teacherNameMap);
		map.put("subjectId", courseId);
		map.put("isXzbDivide", NewGkElectiveConstant.DIVIDE_TYPE_07.equals(divide.getOpenType()));

		return "/newgkelective/teacherArrange/selectTeacherClassList2.ftl";
	}
    
	private void processXzb(String courseId, Map<String, NewGkDivideClass> divideClassIdMap,
			List<NewGkDivideClass> xzbs, Map<String, String> classSubjectTypes,
			Map<String, List<String[]>> xzbSubjects) {
		for (String xzbId : xzbSubjects.keySet()) {
			List<String[]> list = xzbSubjects.get(xzbId);
			Optional<String[]> subIdType = list.stream().filter(e->courseId.equals(e[0])).findFirst();
			if(subIdType.isPresent() 
					&& divideClassIdMap.containsKey(xzbId)) {
				xzbs.add(divideClassIdMap.get(xzbId));
				classSubjectTypes.put(xzbId, subIdType.get()[1]);
			}
		}
	}
	private void processClassType3(List<NewGkDivideClass> divideClassList,
			String courseId, List<NewGkDivideClass> xzbs, String followType, Map<String, String> classSubjectTypes) {
		List<NewGkDivideClass> hbList = divideClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_3.equals(e.getClassType()))
				.collect(Collectors.toList());
		for (NewGkDivideClass dc : hbList) {
			if(dc.getSubjectIds().equals(courseId) && followType.contains(NewGkElectiveConstant.FOLLER_TYPE_A2)) {
				classSubjectTypes.put(dc.getId(), NewGkElectiveConstant.SUBJECT_TYPE_A);
				xzbs.add(dc);
			}else if(dc.getSubjectIdsB().equals(courseId) && followType.contains(NewGkElectiveConstant.FOLLER_TYPE_B2)){
				classSubjectTypes.put(dc.getId(), NewGkElectiveConstant.SUBJECT_TYPE_B);
				xzbs.add(dc);
			}
		}
	}
	private void processJxbs(String courseId, Set<String> hasSelCids, List<NewGkDivideClass> jxbList,
			List<String[]> batchKeys, Map<String, List<NewGkDivideClass>> bathClassMap) {
		for (NewGkDivideClass jxb : jxbList) {
			if(Objects.equals(courseId, jxb.getSubjectIds())) {
				jxb.setBatch(StringUtils.trimToEmpty(jxb.getBatch()));
				if("null".equals(jxb.getBatch())) {
					jxb.setBatch("");
				}
				String key = jxb.getSubjectType() + "_" + jxb.getBatch();
				String name = NewGkElectiveConstant.SUBJECT_TYPE_A.equals(jxb.getSubjectType()) ? "选考" : "学考";
				if (!bathClassMap.containsKey(key)) {
					bathClassMap.put(key, new ArrayList<NewGkDivideClass>());
					batchKeys.add(new String[] { key,jxb.getSubjectType(),jxb.getBatch(), name + jxb.getBatch() });
				}
				if(!hasSelCids.contains(jxb.getId())) {
					bathClassMap.get(key).add(jxb);
				}
			}
		}
	}
    
    /**
     * 获取排课时间显示
     * @param ex
     * @return
     */
    @SuppressWarnings("unused")
	private String getTimeStr(NewGkLessonTimeEx ex) {
    	StringBuilder sb = new StringBuilder();
		String[] weekJson = {"一","二","三","四","五","六","日"};
		String weekday = weekJson[ex.getDayOfWeek()];
		String period = ex.getPeriod()==null?"":ex.getPeriod().intValue()+"";
		if("1".equals(ex.getPeriodInterval())){
			sb.append("周"+weekday+"早自习"+"第"+period+"节");
		}else if("2".equals(ex.getPeriodInterval())){
			sb.append("周"+weekday+"上午"+"第"+period+"节");
		}else if("3".equals(ex.getPeriodInterval())){
			sb.append("周"+weekday+"下午"+"第"+period+"节");
		}else if("4".equals(ex.getPeriodInterval())){
			sb.append("周"+weekday+"晚自习"+"第"+period+"节");
		}else if("9".equals(ex.getPeriodInterval())){
			sb.append("周"+weekday+"特殊时间"+"第"+period+"节");
		}
    	return sb.toString();
    }
    
    @ResponseBody
    @RequestMapping("/subjectTeacherArrange/saveClass")
    @ControllerInfo(value = "保存老师选择的班级")
    public String saveTeacherSelectClass( String itemId , String arrayId, String subjectId, NewGkTeacherPlan newGkTeacherPlan) {
        try{
    		String unitId = getLoginInfo().getUnitId();
            List<NewGkTeacherPlanEx> planExList = newGkTeacherPlan.getTeacherPlanExList();
            if(CollectionUtils.isNotEmpty(planExList)){
            	if(StringUtils.isNotBlank(subjectId)){//不是基础条件保存
            		//合班和同排冲突判断
            		List<NewGkClassCombineRelation> relationList = newGkClassCombineRelationService.findByArrayItemId(unitId, itemId);
            		relationList = relationList.stream().filter(e->e.getClassSubjectIds().contains(subjectId)).collect(Collectors.toList());
            		Map<String, List<String>> claIdMap = new HashMap<String, List<String>>();
            		Map<String, List<String>> claSubMap = new HashMap<String, List<String>>();
            		for (NewGkClassCombineRelation rela : relationList) {
            			String classSubjectIds = rela.getClassSubjectIds();
            			String[] csIdArr = classSubjectIds.split(",");
            			if(NewGkElectiveConstant.COMBINE_TYPE_1.equals(rela.getType())) {
            				//合班
            				String claId1 = csIdArr[0].substring(0, csIdArr[0].indexOf("-"));
            				String claId2 = csIdArr[1].substring(0, csIdArr[1].indexOf("-"));
            				if(!claIdMap.containsKey(claId1)){
            					claIdMap.put(claId1, new ArrayList<String>());
            				}
            				if(!claIdMap.containsKey(claId2)){
            					claIdMap.put(claId2, new ArrayList<String>());
            				}
            				claIdMap.get(claId1).add(claId2);
            				claIdMap.get(claId2).add(claId1);
            			}else if(NewGkElectiveConstant.COMBINE_TYPE_2.equals(rela.getType())) {
            				//同时排课
            				String claId1 = csIdArr[0].substring(0, csIdArr[0].indexOf("-"));
            				String claId2 = csIdArr[1].substring(0, csIdArr[1].indexOf("-"));
            				if(csIdArr[0].contains(subjectId)){
            					if(!claSubMap.containsKey(claId1)){
            						claSubMap.put(claId1, new ArrayList<String>());
            					}
            					claSubMap.get(claId1).add(csIdArr[1]);
            				}
            				if(csIdArr[1].contains(subjectId)){
            					if(!claSubMap.containsKey(claId2)){
            						claSubMap.put(claId2, new ArrayList<String>());
            					}
            					claSubMap.get(claId2).add(csIdArr[0]);
            				}
            			}
            		}
            		
            		// 获取所有教师 安排信息
            		List<NewGkTeacherPlan> teacherPlanList = newGkTeacherPlanService.findByArrayItemIds(
            				new String[]{itemId}, true);
            		Map<String,String> classSubjTeacherMap = new HashMap<>();
            		for (NewGkTeacherPlan tp : teacherPlanList) {
            			if(subjectId.equals(tp.getSubjectId())){
            				continue;
            			}
            			List<NewGkTeacherPlanEx> teacherPlanExList = tp.getTeacherPlanExList();
            			if(CollectionUtils.isEmpty(teacherPlanExList)){
            				continue;
            			}
            			for (NewGkTeacherPlanEx tpe : teacherPlanExList) {
            				String teacherId = tpe.getTeacherId();
            				String classIds = tpe.getClassIds();
            				if(StringUtils.isNotBlank(classIds)){
            					String[] classIdArr = classIds.split(",");
            					for (String cid : classIdArr) {
            						String key = cid+"-"+tp.getSubjectId();
            						classSubjTeacherMap.put(key, teacherId);
            					}
            				}
            			}
            		}
            		
            		Map<String, String> claTeaMap = new HashMap<String, String>();
            		Iterator<NewGkTeacherPlanEx> exIt = planExList.iterator();
            		while(exIt.hasNext()){
            			NewGkTeacherPlanEx ex = exIt.next();
            			if(ex == null) {
            				exIt.remove();
            				continue;
            			}
            			if(StringUtils.isNotBlank(ex.getClassIds())){
            				String[] claIdArr = ex.getClassIds().split(",");
            				for (String claId : claIdArr) {
            					claTeaMap.put(claId, ex.getTeacherId());
            					classSubjTeacherMap.put(claId+"-"+subjectId, ex.getTeacherId());
            				}
            			}
            		}
            		for (Entry<String, String> entry :  claTeaMap.entrySet()) {
            			String cid = entry.getKey();
            			if(claIdMap.containsKey(cid)){
            				for (String claId : claIdMap.get(cid)) {
            					if(StringUtils.isNotBlank(claTeaMap.get(claId))&& !entry.getValue().equals(claTeaMap.get(claId))){
            						List<NewGkDivideClass> divideClassList = newGkDivideClassService.findListByIdIn(new String[]{claId,cid});
            						return error(divideClassList.get(0).getClassName()+"和"+divideClassList.get(1).getClassName()+"存在合班操作，任课老师必须相同");
            					}
            				}
            			}
            			if(claSubMap.containsKey(cid)){
            				for (String claSubId : claSubMap.get(cid)) {
            					Set<String> keySet = classSubjTeacherMap.keySet();
            					for (String key : keySet) {
            						if(claSubId.contains(key)&& entry.getValue().equals(classSubjTeacherMap.get(key))){
            							List<NewGkDivideClass> divideClassList = newGkDivideClassService.findListByIdIn(new String[]{claSubId.substring(0, claSubId.indexOf("-")),cid});
            							return error(divideClassList.get(0).getClassName()+"和"+divideClassList.get(1).getClassName()+"存在同时排课操作，任课老师不能相同");
            						}
            					}
            				}
            			}
            		}
            	}
            	
            	newGkTeacherPlanExService.saveExs(unitId, arrayId, subjectId, itemId, planExList);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return returnError("保存失败！", e.getMessage());
        }
        return success("保存成功！");
    }
    
    @RequestMapping("/teacher/updateExisted")
    @ResponseBody
    public String updateTeacherExisted(@PathVariable("divideId") String divideId, String arrayItemId) {
    	
    	// 获取这些 教师 当前年级 现在开始到学期末 的课程 所在时间点
    	NewGkDivide divide = newGkDivideService.findOne(divideId);
    	String gradeId = divide.getGradeId();
    	
    	String unitId = getLoginInfo().getUnitId();
    	
    	try {
			newGkLessonTimeService.updateTeacherExisted(unitId,gradeId,arrayItemId);
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
    	
    	
    	return returnSuccess();
    }
    @RequestMapping("/teacher/updateTeacherGroupTime")
    @ResponseBody
    public String updateTeacherGroupTime(@PathVariable("divideId") String divideId, String arrayItemId) {
    	
    	// 获取这些 教师 当前年级 现在开始到学期末 的课程 所在时间点
    	NewGkDivide divide = newGkDivideService.findOne(divideId);
    	String gradeId = divide.getGradeId();
    	
    	String unitId = getLoginInfo().getUnitId();
    	
    	try {
    		newGkLessonTimeService.updateTeacherGroupTime(unitId,gradeId,arrayItemId);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return error(e.getMessage());
    	}
    	
    	
    	return returnSuccess();
    }
    
    @InitBinder
	public void initBinder(WebDataBinder wb) throws Exception {
		wb.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
	}
}
