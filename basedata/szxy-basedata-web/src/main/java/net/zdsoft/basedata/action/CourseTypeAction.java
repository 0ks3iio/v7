package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseType;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.CourseTypeService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/basedata")
public class CourseTypeAction extends BaseAction{
	@Autowired
	private UnitService unitService;
	@Autowired
	private CourseTypeService courseTypeService;
	@Autowired
	private CourseService courseService;
	
	@RequestMapping("/subject/head/page")
	public String showSubjectHead(ModelMap map){
		//判断是否具有修改权限
		String unitId = getLoginInfo().getUnitId();
		Unit unit = unitService.findOne(unitId);
		boolean canEdit = Constant.GUID_ZERO.equals(unit.getParentId());
    	map.put("canEdit", canEdit);
    	map.put("type", BaseConstants.TYPE_COURSE_DISCIPLINE);
		return "/basedata/course/subjectHead.ftl";
	}
	
	@RequestMapping("/subject/list/page")
	public String showSubjectList(String searchName, Integer pageIndex,ModelMap map){
		//判断是否具有修改权限
		String unitId = getLoginInfo().getUnitId();
		Unit unit = unitService.findOne(unitId);
		boolean canEdit = Constant.GUID_ZERO.equals(unit.getParentId());
		if(StringUtils.isBlank(searchName)) {
			searchName="";
		}
		List<CourseType> subjectList = courseTypeService.findByNameAndTypeWithPage(searchName,BaseConstants.SUBJECT_TYPE_BX,null);
		map.put("subjectList", subjectList);
		map.put("canEdit", canEdit);
		return "/basedata/course/subjectList.ftl";
	}
	
	/**
	 * 后台再次校验当前单位是否有权限进行修改操作，只有顶级教育局才可以进行修改
	 * @param unitId
	 * @return
	 */
	@RequestMapping("/subject/checkPower")
	@ResponseBody
	public String checkPower(){
		String unitId=getLoginInfo().getUnitId();
		Unit unit = unitService.findOne(unitId);
		if(BaseConstants.ZERO_GUID.equals(unit.getParentId())){
			return returnSuccess();
		}
		return returnError();
	}
	
	@RequestMapping("/subject/detail")
	public String getDetail(String subjectId,ModelMap map){
		CourseType subject=null;
		if(StringUtils.isNotBlank(subjectId)) {
			subject = courseTypeService.findOne(subjectId);
			if(subject==null){
				return errorFtl(map, "对象已不存在！");
			}
		}else {
			subject=new CourseType();
		}
		map.put("subject",subject);
		return "/basedata/course/subjectEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/subject/save")
	public String addSubject(CourseType subject){
		//检查学科名称 和学科编号是否空
		String mess = checkNull(subject);
		if(StringUtils.isNotEmpty(mess)){
			return error(mess);
		}
		//检查学科名称和学科编码的唯一性
		mess = courseTypeService.checkUnique(subject);
		if(StringUtils.isNotEmpty(mess)){
			return error(mess);
		}
		//保存
		if(StringUtils.isEmpty(subject.getId())){
			//新增操作
			subject.setId(UuidUtils.generateUuid());
			subject.setIsDeleted(Constant.IS_DELETED_FALSE);
			subject.setType(BaseConstants.SUBJECT_TYPE_BX);
		}else{
			//修改操作
			CourseType courseType = courseTypeService.findOne(subject.getId());
			courseType.setName(subject.getName());
			subject = courseType;
		}
		courseTypeService.save(subject);
		return returnSuccess();
	}
	
	private String checkNull(CourseType subject) {
		String mess="";
		if(StringUtils.isBlank(subject.getName())){
			mess = mess + "学科名称必填,";
		}
		if(StringUtils.isBlank(subject.getId()) && StringUtils.isBlank(subject.getCode())){
			mess = mess + "学科编号必填,";
		}
		if(StringUtils.isNotBlank(mess)){
			mess=mess+ "保存失败";
		}
		return mess;
	}

	@ResponseBody
	@RequestMapping("/subject/deletes")
	public String deleteSubjects(@RequestParam(value = "ids[]")String[] ids){
    	//检查权限
    	String unitId = getLoginInfo().getUnitId();
    	//模拟有权限的教育局
    	//unitId = "402896E94700810F0147008B2FF30010";
    	Unit unit = unitService.findOne(unitId);
		if(!BaseConstants.ZERO_GUID.equals(unit.getParentId())){
			return error("您没有权限进行此操作");
		}
		boolean noexists = false; //学科不存在
		String hasCourse = ""; //学科已经开了课程
		String mess = "";
		
    	//检查学科是否还存在
		List<String> idList = new ArrayList<>();
		List<CourseType> subjectList = courseTypeService.findListByIdIn(ids);
		List<Course> courseList = courseService.findByUnitIdAndCourseTypeIdIn(unitId,ids);
		Set<String> hasCourseTypeSet = EntityUtils.getSet(courseList, Course::getCourseTypeId);
    	for (CourseType subject : subjectList) {
			if(subject.getIsDeleted().equals(Constant.IS_DELETED_TRUE)){
				if(noexists==false){
					noexists = true;
					mess = ",部分学科已经不存在";
				}
			}else if(hasCourseTypeSet.contains(subject.getId())){
				hasCourse = hasCourse + ","+ subject.getName();
			}else{
				idList.add(subject.getId());
			}
		}
    	if(StringUtils.isNotBlank(hasCourse)){
    		mess = mess + hasCourse + "已经开设了课程，请先删除对应课程";
    	}
    	if(mess.startsWith(",")){
    		mess = mess.substring(1);
    	}
    	if(CollectionUtils.isEmpty(idList)){
    		return error(mess);
    	}
    	
    	try {
    		courseTypeService.updateIsDeleteds(idList.toArray(new String[]{}));
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("删除失败！", e.getMessage());
		}
		
		return success(mess);
	}
	
}
