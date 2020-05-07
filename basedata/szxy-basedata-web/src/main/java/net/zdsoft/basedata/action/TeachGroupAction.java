package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.TeachGroupDto;
import net.zdsoft.basedata.dto.TeacherDto;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.TeachGroup;
import net.zdsoft.basedata.entity.TeachGroupEx;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.TeachGroupExService;
import net.zdsoft.basedata.service.TeachGroupService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/basedata")
public class TeachGroupAction extends BaseAction{
	@Autowired
	private TeachGroupService teachGroupService;
	@Autowired
	private TeachGroupExService teachGroupExService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
    private SchoolRemoteService schoolRemoteService;
	
	@RequestMapping("/teachgroup/index/page")
	public String showTeachGroupIndex(ModelMap map) {
		return "/basedata/teachGroup/teachGroupIndex.ftl";
	}
	
	@RequestMapping("/teachgroup/list/index/page")
	public String showTeachGroupListIndex(String withMaster, ModelMap map) {
		String unitId = getLoginInfo().getUnitId();
		List<String> subjectIds = new ArrayList<String>();
        List<TeachGroup> teachGroupList = null;
        if ("0".equals(withMaster)) {
            teachGroupList = teachGroupService.findBySchoolId(unitId);
        } else {
            teachGroupList = teachGroupService.findBySchoolIdWithMaster(unitId);
        }
        for (TeachGroup teachGroup : teachGroupList) {
			String subjectId = teachGroup.getSubjectId();
			String[] tempSubjectId = subjectId.split(",");
			subjectIds.add(tempSubjectId[0]);
		}
		List<String> teachGroupIds = EntityUtils.getList(teachGroupList, "id");
		
		List<Course> courseList = courseService.findListByIdIn(subjectIds.toArray(new String[subjectIds.size()]));
		Map<String, String> subjectIdToSubjectNameMap = EntityUtils.getMap(courseList, "id", "subjectName");

        List<TeachGroupEx> teachGroupExList = null;
        if ("0".equals(withMaster)) {
            teachGroupExList = teachGroupExService.findByTeachGroupId(teachGroupIds.toArray(new String[teachGroupIds.size()]));
        } else {
            teachGroupExList = teachGroupExService.findByTeachGroupIdWithMaster(teachGroupIds.toArray(new String[teachGroupIds.size()]));
        }
        Set<String> teacherIds = EntityUtils.getSet(teachGroupExList, "teacherId");
		List<Teacher> teacherList = teacherService.findListByIdIn(teacherIds.toArray(new String[teacherIds.size()]));
		Map<String, String> teacherIdToTeacherNameMap = teacherList.stream().filter(e->Objects.equals(0, e.getIsDeleted())).collect(Collectors.toMap(e->e.getId(), e->e.getTeacherName()));
//		Map<String, String> teacherIdToTeacherNameMap = EntityUtils.getMap(teacherList, "id", "teacherName");
		Map<String, List<TeachGroupEx>> teacherGroupIdToTeacherGroupExList = new HashMap<String, List<TeachGroupEx>>();
		
		for (TeachGroupEx teachGroupEx : teachGroupExList) {
			String teacherGroupId = teachGroupEx.getTeachGroupId();
			if(!teacherGroupIdToTeacherGroupExList.containsKey(teacherGroupId)){
				teacherGroupIdToTeacherGroupExList.put(teacherGroupId, new ArrayList<TeachGroupEx>());
			}
			teacherGroupIdToTeacherGroupExList.get(teacherGroupId).add(teachGroupEx);
		}
		
		List<TeachGroupEx> delExList = new ArrayList<>();
		List<TeachGroupDto> teachGroupDtoList = new ArrayList<TeachGroupDto>();
		for (TeachGroup teachGroup : teachGroupList) {
			String teachGroupId = teachGroup.getId();
			String subjecId = teachGroup.getSubjectId();
			String[] subjecIds = subjecId.split(",");
			String tempSubjectId = subjecIds[0];
			TeachGroupDto teachGroupDto = new TeachGroupDto();
			teachGroupDto.setSubjectId(tempSubjectId);
			teachGroupDto.setTeachGroupId(teachGroupId);
			teachGroupDto.setTeachGroupName(teachGroup.getTeachGroupName());
			teachGroupDto.setSubjectName(subjectIdToSubjectNameMap.get(tempSubjectId));
			teachGroupDto.setOrderId(teachGroup.getOrderId());
			List<TeachGroupEx> teacherGroupExList = new ArrayList<TeachGroupEx>();
			List<TeacherDto> mainTeacherList = new ArrayList<TeacherDto>();
			List<TeacherDto> memberTeacherList = new ArrayList<TeacherDto>();
			if(teacherGroupIdToTeacherGroupExList.containsKey(teachGroupId)) {
				teacherGroupExList = teacherGroupIdToTeacherGroupExList.get(teachGroupId);
			}
			if(teacherGroupExList.size() == 0) {
				teachGroupDto.setMainTeacherList(mainTeacherList);
				teachGroupDto.setMemberTeacherList(memberTeacherList);
				teachGroupDtoList.add(teachGroupDto);
				continue;
			}
			for (TeachGroupEx teachGroupEx : teacherGroupExList) {
				if(!teacherIdToTeacherNameMap.containsKey(teachGroupEx.getTeacherId())) {
					delExList.add(teachGroupEx);
					continue;
				}
				if(teachGroupEx.getType() == 1) {
					TeacherDto teacherDto = new TeacherDto();
					teacherDto.setTeacherId(teachGroupEx.getTeacherId());
					teacherDto.setTeacherName(teacherIdToTeacherNameMap.get(teachGroupEx.getTeacherId()));
					mainTeacherList.add(teacherDto);
				}else {
					TeacherDto teacherDto = new TeacherDto();
					teacherDto.setTeacherId(teachGroupEx.getTeacherId());
					teacherDto.setTeacherName(teacherIdToTeacherNameMap.get(teachGroupEx.getTeacherId()));
					memberTeacherList.add(teacherDto);
				}
			}
			teachGroupDto.setMainTeacherList(mainTeacherList);
			teachGroupDto.setMemberTeacherList(memberTeacherList);
			teachGroupDtoList.add(teachGroupDto);
		}
		
		if(CollectionUtils.isNotEmpty(delExList)) {
			teachGroupExService.deleteAll(delExList.toArray(new TeachGroupEx[0]));
		}
		map.put("teachGroupDtoList", teachGroupDtoList);
		return "/basedata/teachGroup/teachGroupList.ftl";
	}
	
	@RequestMapping("/teachgroup/add/index/page")
	@ControllerInfo(ignoreLog=ControllerInfo.LOG_FORCE_IGNORE)
	public String showTeacherGroupAddIndex(HttpServletRequest request, ModelMap map) {
		String currentSection = "";
		String teachGroupId = request.getParameter("teachGroupId");
		TeachGroupDto teachGroupDto = new TeachGroupDto();
		String unitId = getLoginInfo().getUnitId();
		Unit unit = SUtils.dc(unitRemoteService.findTopUnit(unitId),Unit.class);
		School school = SUtils.dc(schoolRemoteService.findOneById(unitId), School.class);
		String[] sections = school.getSections().split(",");
		
		if(StringUtils.isNotEmpty(teachGroupId)){
			TeachGroup teacherGroup = teachGroupService.findOneBy("id", teachGroupId);
			teachGroupDto.setTeachGroupId(teachGroupId);
			teachGroupDto.setTeachGroupName(teacherGroup.getTeachGroupName());
			teachGroupDto.setOrderId(teacherGroup.getOrderId());
			String subjectId = teacherGroup.getSubjectId();
			String[] subjectIds = subjectId.split(",");
			String tempSubjectId = subjectIds[0];
			Course course = courseService.findOneBy("id", tempSubjectId);
			teachGroupDto.setSubjectId(tempSubjectId);
			if(course != null) {
				if(StringUtils.isEmpty(course.getSubjectName())) {
					String subjectName = course.getSubjectName();
					teachGroupDto.setSubjectName(subjectName);
				}
				if(StringUtils.isNotBlank(course.getSection())){
					for(String s1:sections) {
						if(course.getSection().indexOf(s1)>-1) {
							currentSection=s1;
							break;
						}
					}
				}
			}
			List<String> teacherGroupIds = new ArrayList<String>();
			teacherGroupIds.add(teachGroupId);
			List<TeachGroupEx> teachGroupExList = teachGroupExService.findByTeachGroupId(teacherGroupIds.toArray(new String[teacherGroupIds.size()]));
			Set<String> teacherIdList = EntityUtils.getSet(teachGroupExList, e->e.getTeacherId());
			List<Teacher> teacherList = teacherService.findListByIdIn(teacherIdList.toArray(new String[teacherIdList.size()]));
			Map<String, String> teacherIdToTeacherNameMap = EntityUtils.getMap(teacherList, "id", "teacherName");
			StringBuffer  mainTeacherIds = new StringBuffer();
			StringBuffer  mainTeacherNames = new StringBuffer();
			StringBuffer  memberTeacherIds = new StringBuffer();
			StringBuffer  memberTeacherNames = new StringBuffer();
			for (TeachGroupEx teachGroupEx : teachGroupExList) {
				if(teachGroupEx.getType() == 1) {
					if(mainTeacherIds.length()>0) {
						mainTeacherIds.append(",");
						mainTeacherNames.append(",");
					}
					mainTeacherIds.append(teachGroupEx.getTeacherId());
					mainTeacherNames.append(teacherIdToTeacherNameMap.get(teachGroupEx.getTeacherId()));
				}else {
					if(memberTeacherIds.length()>0) {
						memberTeacherIds.append(",");
						memberTeacherNames.append(",");
					}
					memberTeacherIds.append(teachGroupEx.getTeacherId());
					memberTeacherNames.append(teacherIdToTeacherNameMap.get(teachGroupEx.getTeacherId()));
				}
			}
			String mainTeacherId = mainTeacherIds.toString();
			String mainTeacherName = mainTeacherNames.toString();
			String memberTeacherId = memberTeacherIds.toString();
			String memberTeacherName = memberTeacherNames.toString();
			
			teachGroupDto.setMainTeacherIds(mainTeacherId);
			teachGroupDto.setMainTeacherNames(mainTeacherName);
			teachGroupDto.setMemberTeacherIds(memberTeacherId);
			teachGroupDto.setMemberTeacherName(memberTeacherName);
		}else {
			//取最大值
			Integer maxOrder=teachGroupService.findMaxOrder(unitId);
			if(maxOrder==null) {
				maxOrder=1;
			}else if(maxOrder.intValue()<9999) {
				maxOrder=maxOrder+1;
			}
			teachGroupDto.setOrderId(maxOrder);
		}
		List<String> unitIds = new ArrayList<String>();
		
		
		List<String> secList = new ArrayList<>();
		for (String one : sections) {
		    secList.add(one);
        }
		if(StringUtils.isBlank(currentSection)){
			currentSection = sections[0];
		}
		String topUnitId = unit.getId();
		unitIds.add(unitId);
		unitIds.add(topUnitId);
		
		List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitIdIn(unitIds.toArray(new String[unitIds.size()]), Integer.parseInt(BaseConstants.SUBJECT_TYPE_BX), sections),new TR<List<Course>>() {});
		for (int i = 0; i < courseList.size(); i++) {
			if(courseList.get(i).getIsUsing() == 0) {
				courseList.remove(i);
				i--;
			} else {
			    //将分隔符换为空格，追加到前端复选框class中便于后期操作
			    String sectionTemp = courseList.get(i).getSection();
			    if(StringUtils.isNotBlank(sectionTemp))
			    	sectionTemp = sectionTemp.replace(",", " ");
			    courseList.get(i).setSection(sectionTemp==null?"":sectionTemp);
            }
		}
		teachGroupDto.setCourseList(courseList);
		map.put("secList", secList);
		map.put("teachGroupDto", teachGroupDto);
		map.put("currentSection", currentSection);
		return "/basedata/teachGroup/teachGroupAddIndex.ftl";
	}
	
	@RequestMapping("/teachgroup/save")
	@ResponseBody
	public String doTeachGroupSave(TeachGroupDto teachGroupDto) {
		TeachGroup teachGroup = new TeachGroup();
		String unitId = getLoginInfo().getUnitId();
		String schoolId =getLoginInfo().getUnitId();
		String teachGroupId = teachGroupDto.getTeachGroupId();
		String teachGroupName = teachGroupDto.getTeachGroupName();
		String subjectId = teachGroupDto.getSubjectId();
		Course course = courseService.findOneBy("id", subjectId);
		String memberTeacherId = teachGroupDto.getMemberTeacherIds();
		String mainTeacherId = teachGroupDto.getMainTeacherIds();
		String[] mainTeacherIds = mainTeacherId.split(",");
		String[] memberTeacherIds = memberTeacherId.split(",");
		/*
		if(mainTeacherIds.length > 0 && memberTeacherIds.length > 0) {
			for (String mainTeaId : mainTeacherIds) {
				for(String memTeaId : memberTeacherIds) {
					if(mainTeaId.equals(memTeaId)) {
						return error("一个教师在一个教研组中不能同时为负责人和成员");
					}
				}
			}
		}
		*/
		String tempTeachGroupName = teachGroupName;
		if(!StringUtils.isNotEmpty(teachGroupName)) {
			if(course != null) {
				tempTeachGroupName = course.getSubjectName() + "组";
			}
		}
		//System.out.println("tempTeachGroupName:" + tempTeachGroupName);
		List<TeachGroup> teachGroupList = teachGroupService.findBySchoolId(unitId);
		for (TeachGroup teachGroup2 : teachGroupList) {
			if(teachGroup2.getTeachGroupName().equals(tempTeachGroupName)) {
				if(!teachGroupId.equals(teachGroup2.getId())) {
					if(StringUtils.isNotEmpty(teachGroupName)) {
						return error("教研组名称已存在，请修改");
					}else {
						return error("自动生成的教研组名称已存在，请输入教研组名称");
					}
				}
			}
		}
		
		if(StringUtils.isNotBlank(teachGroupId)){
			//System.out.println("StringUtils.isNotBlank(teachGroupId)");
			teachGroup = teachGroupService.findOneBy("id", teachGroupId);
			teachGroup.setSubjectId(subjectId);
			teachGroup.setModifyTime(new Date());
			teachGroup.setOrderId(teachGroupDto.getOrderId());
		}else {
			teachGroupId = UuidUtils.generateUuid();
			teachGroup.setSubjectId(subjectId);
			teachGroup.setId(teachGroupId);
			teachGroup.setSchoolId(schoolId);
			teachGroup.setCreationTime(new Date());
			teachGroup.setModifyTime(new Date());
			teachGroup.setIsDeleted(0);
			teachGroup.setOrderId(teachGroupDto.getOrderId());
		}
		if(StringUtils.isNotEmpty(teachGroupName)) {
			teachGroup.setTeachGroupName(teachGroupName);
		}else {
			if(course != null) {
				teachGroup.setTeachGroupName(course.getSubjectName() + "组");
			}
		}
		List<TeachGroupEx> teachGroupExList = new ArrayList<TeachGroupEx>();
		for (String mainTeaId : mainTeacherIds) {
			TeachGroupEx teachGroupEx = new TeachGroupEx();
			teachGroupEx.setId(UuidUtils.generateUuid());
			teachGroupEx.setTeacherId(mainTeaId);
			teachGroupEx.setTeachGroupId(teachGroupId);
			teachGroupEx.setType(1);
			teachGroupExList.add(teachGroupEx);
		}
		
		for(String memTeaId : memberTeacherIds) {
			TeachGroupEx teachGroupEx = new TeachGroupEx();
			teachGroupEx.setId(UuidUtils.generateUuid());
			teachGroupEx.setTeacherId(memTeaId);
			teachGroupEx.setTeachGroupId(teachGroupId);
			teachGroupEx.setType(0);
			teachGroupExList.add(teachGroupEx);
		}
		
		try {
			teachGroupService.deleteAndSave(teachGroupId,teachGroup,teachGroupExList);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@RequestMapping("/teachGroup/delete")
	@ResponseBody
	public String doDeleteTeacherGroup(String teachGroupId) {
		List<String> teacherGroupIds = new ArrayList<String>();
		String[] teachGroupIds = teachGroupId.split(",");
		for (String string : teachGroupIds) {
			teacherGroupIds.add(string);
		}
		try {
			teachGroupService.delete(teachGroupIds);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
}
