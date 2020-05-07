package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ArrayUtil;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.dto.DyNightSchedulingDto;
import net.zdsoft.stuwork.data.entity.DyNightScheduling;
import net.zdsoft.stuwork.data.service.DyNightSchedulingService;

/**
 * @author yangsj  2017年11月30日下午4:33:33
 */
@Controller
@RequestMapping("/stuwork/night") 
public class DyNightSchedulingAction extends BaseAction{

	@Autowired
	private DyNightSchedulingService dyNightSchedulingService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
    private TeacherRemoteService teacherRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	
	@RequestMapping("/scheduling/index/page")
    @ControllerInfo(value = "")
	public String nightSchedulingIndex(ModelMap map){
		return "/stuwork/courserecord/nightScheduling/nightSchedulingIndex.ftl";
	}
	
	@RequestMapping("/scheduling/head/page")
    @ControllerInfo(value = "")
	public String nightSchedulingHead(ModelMap map){
		//查找当前学校的学段
        List<String> tis = new ArrayList<String>();
        String sections = schoolRemoteService.findSectionsById(getLoginInfo().getUnitId());
        if(StringUtils.isBlank(sections)) {
        	return errorFtl(map,"请先维护该学校的学段");
        }else {
        	String[] se = sections.split(",");
        	tis =  Arrays.asList(se); 
        }
        map.put("tis", tis);
		map.put("nowDate", new Date());
		return "/stuwork/courserecord/nightScheduling/nightSchedulingHead.ftl";
	}
	
	@RequestMapping("/scheduling/list/page")
    @ControllerInfo(value = "")
	public String nightSchedulingList(Date queryDate,String section, ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId), Semester.class);
        if(se == null) {
        	return errorFtl(map, "当前时间不在学年学期内，无法维护！");
        }
        DateInfo dateInfo =  SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), se.getAcadyear(), se.getSemester(), queryDate), DateInfo.class);
		if(dateInfo == null){
			return errorFtl(map, "当前时间不在学年学期内，无法维护！");
		}
		List<Clazz> classList = Clazz.dt(classRemoteService.findBySchoolIdAcadyear(unitId, se.getAcadyear()));
//		//根据班级排序
		 Collections.sort(classList, new Comparator<Clazz>() {
		     @Override
		     public int compare(Clazz o1, Clazz o2) {
				 if(o1.getSection() != o2.getSection()) {
					 return o1.getSection() - o2.getSection();
				 }
				 if(!o1.getAcadyear().equals(o2.getAcadyear())) {
					 return o2.getAcadyear().compareTo(o1.getAcadyear());
				 }
		    	 return o1.getClassCode().compareTo(o2.getClassCode());
		     }
		 });
		 //过滤掉不是本学段的
		 classList = classList.stream().filter(e->String.valueOf(e.getSection()).equals(section)).collect(Collectors.toList());
		List<DyNightScheduling> listDNS = dyNightSchedulingService.findByUnitIdAndNightTime(getLoginInfo().getUnitId(),queryDate);
		Map<String,DyNightScheduling> classIdMap = EntityUtils.getMap(listDNS, DyNightScheduling::getClassId);
		Set<String> teacherIds = EntityUtils.getSet(listDNS, DyNightScheduling::getTeacherId);
		
		List<Teacher> teacherList=SUtils.dt(teacherRemoteService.findListByIds(ArrayUtil.toArray(teacherIds)), new TR<List<Teacher>>(){});
		//key - teacher
		Map<String,Teacher> teacherMap = EntityUtils.getMap(teacherList, Teacher::getId); 
		
		List<DyNightSchedulingDto> listDNSD = new ArrayList<DyNightSchedulingDto>();
        for (Clazz clazz : classList) {
        	DyNightSchedulingDto dyNightSchedulingDto = new DyNightSchedulingDto();
        	dyNightSchedulingDto.setClazz(clazz);
        	String teacherId =  classIdMap.isEmpty() || classIdMap.get(clazz.getId()) == null ?"":classIdMap.get(clazz.getId()).getTeacherId();
        	dyNightSchedulingDto.setTeacherId(teacherId);
        	dyNightSchedulingDto.setTeacherName(teacherMap.isEmpty() || teacherMap.get(teacherId) == null?"":teacherMap.get(teacherId).getTeacherName());
        	listDNSD.add(dyNightSchedulingDto);
		}
		
		map.put("listDNSD", listDNSD);
		return "/stuwork/courserecord/nightScheduling/nightSchedulingList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/scheduling/savetea")
	public String resultTeaSaveStu(String teacherId,String classId,Date queryDate,ModelMap map){
		try{
			if(StringUtils.isBlank(teacherId)){
				return error("保存失败,教师Id为空！");
			}
			DyNightScheduling dyNightScheduling = dyNightSchedulingService.findByTeaIdAndClaIdAndTime(getLoginInfo().getUnitId(),classId,queryDate);
			if(dyNightScheduling != null){
				dyNightScheduling.setTeacherId(teacherId);
			}else {
				dyNightScheduling = new DyNightScheduling();
				dyNightScheduling.setId(UuidUtils.generateUuid());
				dyNightScheduling.setClassId(classId);
				dyNightScheduling.setTeacherId(teacherId);
				dyNightScheduling.setUnitId(getLoginInfo().getUnitId());
				dyNightScheduling.setNightTime(queryDate);
			}
			dyNightSchedulingService.save(dyNightScheduling);
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
		
	}
	
	@ResponseBody
	@RequestMapping("/scheduling/deletetea")
	public String deleteTea(String clazzId,Date queryDate,ModelMap map){
		try{
			DyNightScheduling dyNightScheduling = dyNightSchedulingService.findByTeaIdAndClaIdAndTime(getLoginInfo().getUnitId(),clazzId,queryDate);
		    if(dyNightScheduling != null)
			 dyNightSchedulingService.delete(dyNightScheduling.getId());
		}catch (Exception e) {
			return error("删除失败！"+e.getMessage());
		}
		return success("删除成功");
	}
	
	@ResponseBody
	@RequestMapping("/scheduling/saveClazzs")
	@ControllerInfo("批量保存")
	public String resultTeaSaveStus(Date queryDate,String teacherId,@RequestBody String clazzs){
		try {
			JSONObject jsonObject = SUtils.dc(clazzs, JSONObject.class);    	
			String classIds = jsonObject.getString("classId");
			List<String> clazzList = SUtils.dt(classIds,String.class);
			if(CollectionUtils.isEmpty(clazzList)) {
				return error("请选择班级");
			}
			clazzList.remove("");
			//先删除，再添加
			dyNightSchedulingService.deleteByNightTimeAndClazzs(queryDate,clazzList.toArray(new String[clazzList.size()]));
			
			List<DyNightScheduling> dyNightList = new ArrayList<DyNightScheduling>();
			if(CollectionUtils.isNotEmpty(clazzList)) {
				for (String classId : clazzList) {
					DyNightScheduling dyNightScheduling = new DyNightScheduling();
					dyNightScheduling.setId(UuidUtils.generateUuid());
					dyNightScheduling.setClassId(classId);
					dyNightScheduling.setTeacherId(teacherId);
					dyNightScheduling.setUnitId(getLoginInfo().getUnitId());
					dyNightScheduling.setNightTime(queryDate);
					dyNightList.add(dyNightScheduling);
				}
				dyNightSchedulingService.saveAll(EntityUtils.toArray(dyNightList,DyNightScheduling.class));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return error("添加失败");
		}
		return success("添加成功");
	}
}
