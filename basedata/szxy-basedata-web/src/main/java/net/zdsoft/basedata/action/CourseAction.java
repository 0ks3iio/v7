package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.CourseTypeDto;
import net.zdsoft.basedata.dto.SectionDto;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseType;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.CourseTypeService;
import net.zdsoft.basedata.service.SchoolService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.ColumnInfoUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.TypeReference;

@Controller
@RequestMapping("/basedata")
public class CourseAction extends BaseAction{
	
	@Autowired
	private CourseService courseService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private CourseTypeService courseTypeService;
	@Autowired
	private SystemIniRemoteService systemIniRemoteService;


    @RequestMapping("/course/index/page")
    @ControllerInfo(value = "课程库入口")
    public String showIndex(ModelMap map, HttpSession httpSession) {
    	//判断是否具有修改权限
		String unitId = getLoginInfo().getUnitId();
		Unit unit = unitService.findOne(unitId);
		boolean isDj = Constant.GUID_ZERO.equals(unit.getParentId());
        map.put("isDj", isDj);
        return "/basedata/course/courseIndex.ftl";
    }
    
    /**
     * 根据单位id获取课程列表，如果是单位是学校则获取本校和教育局的课程。
     * 如果是教育局 只获取教育局的课程
     * @return
     */
    @RequestMapping("/unit/course/head/page")
    @ControllerInfo("显示课程头部")
    public String showCourseHead(String type, ModelMap map) {
    	String unitId=getLoginInfo().getUnitId();
    	Unit unit = unitService.findOne(unitId);
		//教育局--显示顶级
		map.put("isEDU", 1==unit.getUnitClass());
    	List<CourseType> courseTypeList = null;
    	if(BaseConstants.SUBJECT_TYPE_BX.equals(type)){
    		courseTypeList = courseTypeService.findByType(BaseConstants.SUBJECT_TYPE_BX);
    	}else if(BaseConstants.SUBJECT_TYPE_XX.equals(type)){
			courseTypeList = courseTypeService.findByTypes(new String[]{BaseConstants.SUBJECT_TYPE_BX,BaseConstants.SUBJECT_TYPE_XX});
    	}else if(BaseConstants.SUBJECT_TYPE_VIRTUAL.equals(type)){
    		courseTypeList = new ArrayList<>();
    		courseTypeList.add(courseTypeService.findOne(BaseConstants.VIRTUAL_COURSE_TYPE));
    	}
    	map.put("type", type);
    	map.put("unitId", unitId);
    	map.put("courseTypeList", courseTypeList);
    	return "/basedata/course/courseHead.ftl";
    }
    /**
     * 根据单位id获取课程列表，如果是单位是学校则获取本校和教育局的课程。
     * 如果是教育局 只获取教育局的课程
     * @return
     */
    @RequestMapping("/unit/course/list/page")
    @ControllerInfo("显示课程列表")
    public String showCourses(String type, ModelMap map,String searchType,String searchName,String courseSource, String xxType) {
    	String unitId=getLoginInfo().getUnitId();
    	Unit unit = unitService.findOne(unitId);
    	String section = "";
    	if(unit.getUnitClass()==2){
			//如果是学校，只获取学校对应学段的课程
			List<SectionDto> sectionDtos = getAllSectionByUnit(unitId);
			String[] sectionArray = EntityUtils.getSet(sectionDtos, SectionDto::getSectionValue).toArray(new String[0]);
			Arrays.sort(sectionArray);
			section = StringUtils.join(sectionArray,",");
    	}
        if(StringUtils.isNotBlank(searchName)){
        	searchName =searchName.trim();
        }
        String[] types = new String[]{type};
        if(BaseConstants.SUBJECT_TYPE_XX.equals(type)){
        	if(StringUtils.isBlank(xxType)){
        		types = new String[]{BaseConstants.SUBJECT_TYPE_XX, BaseConstants.SUBJECT_TYPE_XX_4, BaseConstants.SUBJECT_TYPE_XX_5, BaseConstants.SUBJECT_TYPE_XX_6};
        	}else{
        		types = new String[]{xxType};
        	}
		}
        List<Course> courseList = courseService.getListByConditionWithMaster(unitId,types,searchType,searchName,section,courseSource,null);
        courseService.makeOtherParm(courseList);

        // 若课程设置的学段不在学校设置的学段范围内则舍去
		if (unit.getUnitClass()==2 && CollectionUtils.isNotEmpty(courseList)) {
			String sectionMcode = ColumnInfoUtils.getColumnInfo(Course.class, "section").getMcodeId();
			Map<String, McodeDetail> sectionMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId(sectionMcode),new TypeReference<Map<String,McodeDetail>>(){});
			for (Course course : courseList) {
				String[] sectionArray;
				if(StringUtils.isBlank(course.getSection())){
					//如果section为空的情况，将其设为空串
					course.setSectionName("");
					continue;
				}
				sectionArray = course.getSection().split(",");

				//拼接字符串
				String sectionNames = "";
				for (String sub : sectionArray) {
					if(sectionMap.get(sub) == null || section.indexOf(sub) < 0){
						continue;
					}
					sectionNames += "," + sectionMap.get(sub).getMcodeContent();
				}
				if(sectionNames.startsWith(",")){
					sectionNames = sectionNames.substring(1);
				}

				course.setSectionName(sectionNames);
			}
		}
		
        map.put("courseList", courseList);
        map.put("type", type);
        map.put("unitId", unitId);

		if (BaseConstants.SUBJECT_TYPE_VIRTUAL.equals(type)) {
			return "/basedata/course/virtualCourseList.ftl";
		}
        return "/basedata/course/courseList.ftl";
    }

    @RequestMapping("/course/add/page")
	@ControllerInfo(value="新增课程", ignoreLog=ControllerInfo.LOG_FORCE_IGNORE)
	public String showCourseAdd(ModelMap map, String type, String courseId) {
    	String unitId = getLoginInfo().getUnitId();
    	Unit unit = unitService.findOne(unitId);
    	if(unit.getUnitClass()==2){
    		
    	}else if(unit.getUnitClass()==1 ){
    		//教育局--显示顶级
    		map.put("EDU", true);
    	}
    	
    	map.put("isHw", isHw());
    	Course course = null;
    	if(StringUtils.isBlank(courseId)){
    		//没有Id 这是添加操作
    		course=new Course();
    		course.setSubjectType(unit.getUnitClass()+"");
    		//默认数据库最大值加1
    		course.setOrderId(courseService.findMaxOrderId(unitId)+1);
    	}else{
    		//有id 这是编辑操作
    		course = courseService.findOne(courseId);
    		
    		//先检查权限
    		if(!course.getUnitId().equals(unitId)){
        		return error("您没有权限进行此操作！");
        	}
    		
    		type=course.getType();
    	}
		//查询可能的课程类型
		String typeVal=type; //转换下原因目前就是456获取1课程模块
		ArrayList<CourseTypeDto> courseTypeDtos = new ArrayList<>();

    	if(BaseConstants.SUBJECT_TYPE_XX_4.equals(type) || BaseConstants.SUBJECT_TYPE_XX_5.equals(type) ||
				BaseConstants.SUBJECT_TYPE_XX_6.equals(type) ){
    		typeVal= "1";
		}
		List<CourseType> subjectList = courseTypeService.findByType(typeVal);
		for (CourseType subject : subjectList) {
			CourseTypeDto courseTypeDto = new CourseTypeDto(subject.getId(), subject.getName());
			courseTypeDtos.add(courseTypeDto);
		}
		//获取学段
		List<SectionDto> sectionDtos = getAllSectionByUnit(unitId);
		boolean editColor=false;
		if(unit.getUnitClass()==1 && BaseConstants.SUBJECT_TYPE_BX.equals(type)) {
			editColor=true;
		}
		map.put("course", course);
		map.put("courseTypeDtos", courseTypeDtos);
		map.put("sectionDtos", sectionDtos);
		map.put("success", true);
		map.put("editColor", editColor);
		map.put("type", type);

		if (BaseConstants.SUBJECT_TYPE_VIRTUAL.equals(type)) {
			return "/basedata/course/virtualCourseEdit.ftl";
		}
		return "/basedata/course/courseEdit.ftl";
	}
    
    @ResponseBody
    @RequestMapping("/course/getCourseTypeList")
	@ControllerInfo(value="获取课程类型")
	public List<CourseTypeDto> getCourseTypeList(ModelMap map, String type) {
		//查询可能的课程类型
		ArrayList<CourseTypeDto> courseTypeDtos = new ArrayList<>();
		String[] types;
		if(StringUtils.isBlank(type)){
			types = new String[]{BaseConstants.SUBJECT_TYPE_XX, BaseConstants.SUBJECT_TYPE_BX};
		}else if(BaseConstants.SUBJECT_TYPE_XX_4.equals(type) || BaseConstants.SUBJECT_TYPE_XX_5.equals(type) ||
				BaseConstants.SUBJECT_TYPE_XX_6.equals(type) ){
			types = new String[]{BaseConstants.SUBJECT_TYPE_BX};
		}else{
			types = new String[]{BaseConstants.SUBJECT_TYPE_XX};
		}
		List<CourseType> subjectList = courseTypeService.findByTypes(types);
		for (CourseType subject : subjectList) {
			CourseTypeDto courseTypeDto = new CourseTypeDto(subject.getId(), subject.getName());
			courseTypeDtos.add(courseTypeDto);
		}
		return courseTypeDtos;
	}

	public List<SectionDto> getAllSectionByUnit(String unitId) {
		List<SectionDto> sectionDtos = new ArrayList<>();
		String sectionMcode = ColumnInfoUtils.getColumnInfo(Course.class, "section").getMcodeId();
		Map<String, McodeDetail> sectionMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId(sectionMcode),new TypeReference<Map<String,McodeDetail>>(){});
		School school = schoolService.findOne(unitId);
		if(school==null || school.getSections()==null){
			for (Entry<String, McodeDetail> entry : sectionMap.entrySet()) {
				SectionDto sectionDto = new SectionDto(entry.getValue().getMcodeContent(), entry.getKey());
				sectionDtos.add(sectionDto);
			}
		}else{
			String[] sectionArray = school.getSections().split(",");
			for (String temp : sectionArray) {
				//解决报错问题 杭外有一个9的参数
				if(!sectionMap.containsKey(temp)){
					continue;
				}
				SectionDto sectionDto = new SectionDto(sectionMap.get(temp).getMcodeContent(), temp);
				sectionDtos.add(sectionDto);
			}
		}
		return sectionDtos;
	}
    
    @ResponseBody
	@RequestMapping("/course/save")
	@ControllerInfo("保存新增课程")
	public String doAddSave(Course course, String sections, ModelMap map) {
    	//设置是否启用
    	if(course.getIsUsing()==null||"".equals(course.getIsUsing())){
    		course.setIsUsing(0);
    	}
		try {
			String mess = "";
			//检查课程名称和课程码的唯一性
			String unitId = getLoginInfo().getUnitId();
			course.setUnitId(unitId);
			if(StringUtils.isBlank(course.getId())){
				mess = courseService.checkUnique(course,"add",unitId);
			}else{
				mess = courseService.checkUnique(course,"update",unitId);
			}
			if(StringUtils.isNotEmpty(mess)){
				return error(mess);
			}
			if(StringUtils.isBlank(course.getShortName())){
				String sectionMcode = ColumnInfoUtils.getColumnInfo(Course.class, "section").getMcodeId();
				McodeDetail sec = SUtils.dt(mcodeRemoteService.findByMcodeAndThisId(sectionMcode, course.getSection()), new TypeReference<McodeDetail>(){});
				if(sec!=null && StringUtils.isNotBlank(course.getSubjectName())){
					course.setShortName(sec.getMcodeContent()+course.getSubjectName());
				}
			}
			if(StringUtils.isBlank(course.getId())){
				//添加课程
				course.setId(UuidUtils.generateUuid());
				course.setIsDeleted(Constant.IS_DELETED_FALSE);
				//如果排序号为空，设置默认值
				if(course.getOrderId() == null){
					course.setOrderId(courseService.findMaxOrderId(unitId)+1);
				}
				if(isHw()){
					course.setSection(sections);
				}
				if (BaseConstants.VIRTUAL_COURSE_TYPE.equals(course.getCourseTypeId())) {
					List<SectionDto> sectionDtos = getAllSectionByUnit(unitId);
					String[] sectionArray = EntityUtils.getSet(sectionDtos, SectionDto::getSectionValue).toArray(new String[0]);
					Arrays.sort(sectionArray);
					course.setSection(StringUtils.join(sectionArray,","));
				}
			}else{
				//修改课程数据
				Course oldCourse = courseService.findOne(course.getId());
				if(isHw()){
					oldCourse.setSection(sections);
				}else{
					oldCourse.setSection(course.getSection());
				}
				oldCourse.setSubjectName(course.getSubjectName());
				oldCourse.setShortName(course.getShortName());
				oldCourse.setCourseTypeId(course.getCourseTypeId());
				oldCourse.setInitCredit(course.getInitCredit());
				oldCourse.setFullMark(course.getFullMark());
				oldCourse.setInitPassMark(course.getInitPassMark());
				oldCourse.setIsUsing(course.getIsUsing());
				oldCourse.setBgColor(course.getBgColor());
				oldCourse.setSubjectCode(course.getSubjectCode());
				oldCourse.setTotalHours(course.getTotalHours());
//				oldCourse.setType(course.getType());type页面禁止修改
				//如果排序号不为空，设置新值
				if(course.getOrderId() != null){
					oldCourse.setOrderId(course.getOrderId());
				}
				course = oldCourse;
			}
			
			courseService.saveAllEntitys(course);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("保存失败！", e.getMessage());
		}

		return returnSuccess();
	}
    
    private boolean isHw(){
		return BaseConstants.DEPLOY_HANGWAI.equals(systemIniRemoteService.findValue("SYSTEM.DEPLOY.REGION"));
	}
    
    @ResponseBody
	@RequestMapping("/course/deletes")
	@ControllerInfo("批量删除课程")
	public String doDeletes(@RequestParam(value = "ids[]")String[] ids, ModelMap map, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		try {
			courseService.updateIsDeleteds(ids);
			return success("删除成功");
		} catch (RuntimeException e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
//    	List<Course> courseList = courseService.findListByIdIn(ids);
//    	//找出所有使用这些课程的班级
//    	OpenTeachingSearchDto dto = new OpenTeachingSearchDto();
//    	dto.setSubjectIds(ids);
//    	dto.setIsDeleted(Constant.IS_DELETED_FALSE);
//    	List<GradeTeaching> gradeTeachingList = gradeTeachingService.findBySearch(dto);
//    	Set<String> isUsingSubjectIds = EntityUtils.getSet(gradeTeachingList, "subjectId");
//    	//可删除的
//    	Set<String> delids = new HashSet<String>();
//    	boolean flag1=false;//是否已删除或者不存在
//    	boolean flag2=false;//是否没有权限
//    	LoginInfo loginInfo = getLoginInfo(session);
//    	String unitId = loginInfo.getUnitId();
//    	StringBuffer isUsingCourse = new StringBuffer();
//    	for(Course c:courseList){
//    		if(c==null || c.getIsDeleted()==Constant.IS_DELETED_TRUE){
//        		//已经软删啦或者不存在
//    			if(!flag1){
//    				flag1=true;
//    			}
//        	}else if(!c.getUnitId().equals(unitId)){
//        		//不是本单位的课程，不能删除
//    			if(!flag2){
//    				flag2=true;
//    			}
//        	 }else if(isUsingSubjectIds.contains(c.getId())){
//        		 //已经被开课，不能删除
//        		 isUsingCourse.append(",").append(c.getSubjectName());
//        	 }else{
//        		 delids.add(c.getId());
//        	 }
//    	}
//    	String mess="";
//    	
//    	if(delids==null || delids.size()<=0){
//    		//mess="没有可删除的课程,删除失败";
//    		if(flag1){
//    			mess=mess+",部分课程已经不存在";
//    		}
//    		if(flag2){
//    			mess=mess+",部分课程您没有权限进行删除操作";
//    		}
//    		if(isUsingCourse.length()>0){
//    			mess=mess+isUsingCourse+"已经被使用，不能删除";
//    		}
//    		
//    		if(mess.startsWith(",")){
//    			mess = mess.substring(1);
//    		}
//    		return error(mess);
//    	}
//    	
//    	try {
//    		courseService.updateIsDeleteds(delids.toArray(new String[]{}));
//    		mess="删除成功";
//    		if(flag1){
//    			mess=mess+",部分课程已经不存在";
//    		}
//    		if(flag2){
//    			mess=mess+",部分课程您没有权限进行删除操作";
//    		}
//    		if(isUsingCourse.length()>0){
//    			mess=mess+isUsingCourse+"已经被使用，不能删除";
//    		}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return returnError("删除失败！", e.getMessage());
//		}
//    	
//    	if(mess.startsWith(",")){
//			mess = mess.substring(1);
//		}
//		return success(mess);
	}
}
