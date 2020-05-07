package net.zdsoft.newgkelective.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.entity.TeachGroup;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.TeachGroupRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.NewGkTeacherPlanDto;
import net.zdsoft.newgkelective.data.entity.NewGkArrayItem;
import net.zdsoft.newgkelective.data.entity.NewGkOpenSubject;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;
import net.zdsoft.newgkelective.data.service.NewGkArrayItemService;
import net.zdsoft.newgkelective.data.service.NewGkOpenSubjectService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanService;

@Controller
@RequestMapping("/newgkelective/{divideId}")
public class NewGkDivideTeacherAction extends BaseAction {

	@Autowired
	private NewGkOpenSubjectService newGkOpenSubjectService;
	@Autowired
	private NewGkArrayItemService newGkArrayItemService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkTeacherPlanService newGkTeacherPlanService;
	@Autowired
	private TeachGroupRemoteService teachGroupRemoteService;
	

	@RequestMapping("/teacherArrange/index/page")
	@ControllerInfo(value = "教师首页")
	public String showTeacherArrangeList(@PathVariable String divideId,String gradeId,String arrayId, ModelMap map) {
		map.put("divideId", divideId);
	 	map.put("gradeId", gradeId);
	 	map.put("arrayId", arrayId);
		List<NewGkArrayItem> itemList =  newGkArrayItemService.findByDivideId(divideId,new String[]{NewGkElectiveConstant.ARRANGE_TYPE_02});
		if(CollectionUtils.isEmpty(itemList)){
			return "/newgkelective/array/teacherIndex1.ftl";
		}
		//创建的方案（方案包含科目，科目中教师列表）
		Map<String,NewGkTeacherPlanDto> dtoMap = new HashMap<String,NewGkTeacherPlanDto>();
		for (NewGkArrayItem item : itemList) {
			NewGkTeacherPlanDto dto = new NewGkTeacherPlanDto();
			dto.setItemId(item.getId());
			dto.setItemName(item.getItemName());
			dto.setCreationTime(item.getCreationTime());
			dtoMap.put(item.getId(),dto);
		}
		
		
		List<String> itemIdList = EntityUtils.getList(itemList, "id");
		List<NewGkTeacherPlan> teacherPlanList = newGkTeacherPlanService.findByArrayItemIds(itemIdList.toArray(new String[0]),true);
		if(CollectionUtils.isNotEmpty(teacherPlanList)){
			Set<String> subjectIds = EntityUtils.getSet(teacherPlanList, "subjectId");
			Map<String, String> courseNameMap = getCourseNameMap(subjectIds);
			for (NewGkTeacherPlan ent : teacherPlanList) {
				List<NewGkTeacherPlan> inList = dtoMap.get(ent.getArrayItemId()).getSubjectList();
				if(inList==null){
					inList = new ArrayList<NewGkTeacherPlan>();
					dtoMap.get(ent.getArrayItemId()).setSubjectList(inList);
				}
				ent.setSubjectName(courseNameMap.get(ent.getSubjectId()));
				inList.add(ent);
			}
		}
		
		map.put("dtoMap", dtoMap);
		return "/newgkelective/array/teacherIndex1.ftl";
	}
	
	@RequestMapping("/teacherArrange/add")
    @ControllerInfo(value = "新增课时安排")
    public String doAdd(@PathVariable("divideId") String divideId,String gradeId,String arrayId,ModelMap map) {
    	List<NewGkTeacherPlan> teacherPlanList=new ArrayList<NewGkTeacherPlan>();
    	map.put("divideId", divideId);
	 	map.put("gradeId", gradeId);
		map.put("teacherPlanList", teacherPlanList);
		map.put("arrayId", arrayId);
    	return "/newgkelective/array/teacherAdd.ftl";
	}
    
    @RequestMapping("/teacherArrange/update")
    @ControllerInfo(value = "修改课时安排")
    public String doEdit(@PathVariable("divideId") String divideId,String gradeId,String itemId,String arrayId,ModelMap map) {

		
    	return "/newgkelective/array/teacherAdd.ftl";
	}
    
    @ResponseBody
    @RequestMapping("/teacherArrange/save")
    @ControllerInfo(value = "保存课时安排")
    public String doSave(@PathVariable("divideId") String divideId, NewGkTeacherPlanDto dto) {
    	
    	try {
    		//新增
    		if(StringUtils.isBlank(dto.getItemId())){
    		//更新
    		}else{
    		}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
    	return returnSuccess();
	}
	
	private Map<String, String> getCourseNameMap(Set<String> subjectIds) {
		return SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
	}
	
	@RequestMapping("/teacherChose/index/page")
    @ControllerInfo(value = "老师安排首页")
    public String showTeacherChoseList(@PathVariable String divideId,String gradeId,ModelMap map) {
		//根据分班id获开设科目
		List<NewGkOpenSubject> newGkOpenSubjectList = newGkOpenSubjectService.findByDivideId(divideId);
		
		List<String> subjectIdList = EntityUtils.getList(newGkOpenSubjectList, "subjectId");
		
		// 科目Id 科目名称 封装下拉框
		Map<String, String> courseNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIdList.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
		
		map.put("courseNameMap", courseNameMap);
		map.put("divideId", divideId);
		return "/newgkelective/array/teacherChoseIndex.ftl";
	}

	@RequestMapping("/teacherGroup/index/page")
	@ControllerInfo("教研组首页")
	public String showTeacherGroup(@PathVariable String divideId,HttpServletRequest request,ModelMap map) {
		//根据subjectID获取教研组
		String subjectId = request.getParameter("subjectId");
		Map<String,String> teacherGroup = new HashMap<String,String>();
		//单位id
		String schoolId = getLoginInfo().getUnitId();
		//根据单位id获取教研组信息
		List<TeachGroup> baseTeacherGroupList =  SUtils.dt(teachGroupRemoteService.findBySchoolId(schoolId),TeachGroup.class);
		for (TeachGroup baseTeachGroup : baseTeacherGroupList) {
			String subId = baseTeachGroup.getSubjectId();
			String[] subjectIds = subId.split(",");
			for (String tempSubId : subjectIds) {
				if(tempSubId.equals(subjectId)) {
					teacherGroup.put(baseTeachGroup.getId(), baseTeachGroup.getTeachGroupName());
				}
			}
		}
		map.put("teacherGroupMap", teacherGroup);
		return "/newgkelective/array/teacherGroupIndex.ftl";
	}
	
	@RequestMapping("/teacher/index/page")
	@ControllerInfo("教师首页")
	public String showTeacher(@PathVariable String divideId,HttpServletRequest request,ModelMap map) {
		//根据 groupID获取老师
		return "/newgkelective/array/teacherGroupIndex.ftl";
	}
}
