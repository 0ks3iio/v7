package net.zdsoft.studevelop.data.action;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.entity.StuDevelopPunishment;
import net.zdsoft.studevelop.data.entity.StuDevelopRewards;
import net.zdsoft.studevelop.data.service.StuDevelopPunishmentService;
import net.zdsoft.studevelop.data.service.StuDevelopRewardsService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/studevelop")
public class StuDevelopRewardsAndPunishAction extends CommonAuthAction{	
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StuDevelopRewardsService stuDevelopRewardsService;
	@Autowired
	private StuDevelopPunishmentService stuDevelopPunishmentService;
	@Autowired
	private StudentRemoteService studenteRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	
	@RequestMapping("/rewardsRecord/index/page")
    @ControllerInfo(value = "奖惩登记tab")
	public String rewardsAndpunishAdmin(ModelMap map){
		return "/studevelop/record/rewardsAndPunishAdmin.ftl";
	}

	@RequestMapping("/rewardsRecord/toClassTree")
	@ControllerInfo(value = "toClassTree")
	public String toClassTree(ModelMap map,String acadyear,String semester){
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		return "/studevelop/record/recordClassTreeIndex.ftl";
	}
	@ResponseBody
	@RequestMapping("/rewardsRecord/studentList")
	public List<Student> studentList(String classId){
		return SUtils.dt(studenteRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>(){});
	}
	
	@ResponseBody
	@RequestMapping("/rewardsRecord/clsList")
	public List<Clazz> clsList(String acayear){
		return SUtils.dt(classRemoteService.findByIdCurAcadyear(getLoginInfo().getUnitId(),acayear), new TR<List<Clazz>>() {});
	}
	
	@RequestMapping("/rewardsRecord/head")
    @ControllerInfo(value = "奖惩登记head")
	public String rewardsAndpunishHead(String tabType, ModelMap map){
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()), Semester.class);
        List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(getLoginInfo().getUnitId(),semester.getAcadyear()), new TR<List<Clazz>>() {});
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        map.put("tabType", tabType);
        map.put("classList", classList);
        map.put("isAdmin", isAdmin(StuDevelopConstant.PERMISSION_TYPE_REPORT));
		return "/studevelop/record/rewardsAndPunishHead.ftl";
	}
	
	@RequestMapping("/rewardsRecord/rewardsAdd")
    @ControllerInfo(value = "奖励新增页面")
	public String rewardsAdd(String acadyear, String semester, String studentId, ModelMap map){
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("stuid", studentId);
		map.put("schid", getLoginInfo().getUnitId());
		map.put("type", "1");
		return "/studevelop/record/rewardsEdit.ftl";
	}
	
	@RequestMapping("/rewardsRecord/rewardsEdit")
    @ControllerInfo(value = "奖励修改页面")
	public String rewardsEdit(String id,ModelMap map){
		StuDevelopRewards stuDevelopRewards = stuDevelopRewardsService.findById(id);
		map.put("stuDevelopRewards", stuDevelopRewards);
		map.put("type", "2");
		return "/studevelop/record/rewardsEdit.ftl";
	}
	
	@ResponseBody
    @RequestMapping("/rewardsRecord/rewardsSave")
    @ControllerInfo(value = "保存奖励")
	public String rewardsSave(StuDevelopRewards stuDevelopRewards){
		try{
			if(StringUtils.isBlank(stuDevelopRewards.getId())){
				stuDevelopRewards.setId(UuidUtils.generateUuid());
			}
			stuDevelopRewards.setUpdatestamp(System.currentTimeMillis());
			stuDevelopRewardsService.save(stuDevelopRewards);
	    }catch(Exception e){	    	
	 	    e.printStackTrace();
		    return returnError();
	    }
	    return returnSuccess();
	}
	
	@ResponseBody
    @RequestMapping("/rewardsRecord/rewardsDelete")
    @ControllerInfo(value = "删除奖励信息")
	public String rewardsDelete(String id){
		try{
			stuDevelopRewardsService.delete(id);
	    }catch(Exception e){	    	
	 	    e.printStackTrace();
		    return returnError();
	    }
	    return returnSuccess();
	}
	
	@RequestMapping("/rewardsRecord/rewardList")
    @ControllerInfo(value = "奖励list")
	public String rewardsList(String queryAcadyear, String querySemester, String classId, String rewardslevel,String tabType, String studentId, HttpServletRequest request, ModelMap map){
		Pagination page = createPagination();
		List<StuDevelopRewards> stuDevelopRewardsList = stuDevelopRewardsService.findListByAcaAndSemAndUnitId(queryAcadyear, querySemester, getLoginInfo().getUnitId(), classId, studentId, rewardslevel, page);
		Set<String> stuIdSet = new HashSet<String>();
		for(StuDevelopRewards item : stuDevelopRewardsList){
			stuIdSet.add(item.getStuid());
		}
		List<Student> stuList = SUtils.dt(studenteRemoteService.findListByIds(stuIdSet.toArray(new String[0])), new TR<List<Student>>() {});
		Map<String,String> stuMap = new HashMap<String,String>();
		for(Student stu : stuList){
			stuMap.put(stu.getId(), stu.getStudentName());
		}
		for(StuDevelopRewards item : stuDevelopRewardsList){
			item.setStuName(stuMap.get(item.getStuid()));
		}
		
		map.put("Pagination", page);
        sendPagination(request, map, page);
		map.put("tabType", tabType);
		map.put("stuDevelopRewardsList", stuDevelopRewardsList);
		return "/studevelop/record/rewardsAndPunishList.ftl";
	}
	
	@RequestMapping("/rewardsRecord/punishAdd")
    @ControllerInfo(value = "惩处新增页面")
	public String punishAdd(String acadyear, String semester, String studentId, ModelMap map){
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("stuid", studentId);
		map.put("schid", getLoginInfo().getUnitId());
		map.put("operateUserId", getLoginInfo().getOwnerId());
		String teacherName = SUtils.dc(teacherRemoteService.findOneById(getLoginInfo().getOwnerId()), Teacher.class).getTeacherName();
		map.put("operateUserName", teacherName);
		map.put("type", "1");
		return "/studevelop/record/punishEdit.ftl";
	}
	
	@RequestMapping("/rewardsRecord/punishEdit")
    @ControllerInfo(value = "惩处修改页面")
	public String punishEdit(String id, ModelMap map){
		StuDevelopPunishment stuDevelopPunishment = stuDevelopPunishmentService.findById(id);
		String teacherName = SUtils.dc(teacherRemoteService.findOneById(stuDevelopPunishment.getOperateUserId()), Teacher.class).getTeacherName();
		map.put("operateUserName", teacherName);
		map.put("operateUserId", stuDevelopPunishment.getOperateUserId());
		map.put("stuDevelopPunishment", stuDevelopPunishment);
		map.put("type", "2");
		return "/studevelop/record/punishEdit.ftl";
	}
	
	@RequestMapping("/rewardsRecord/punishList")
    @ControllerInfo(value = "惩处list")
	public String punishList(String queryAcadyear, String querySemester, String classId, String punishtype,String tabType, String studentId, HttpServletRequest request, ModelMap map){
		Pagination page = createPagination();
		List<StuDevelopPunishment> stuDevelopPunishmentList = stuDevelopPunishmentService.findListByAll(queryAcadyear, querySemester, getLoginInfo().getUnitId(), classId, studentId, punishtype, page);
		Set<String> stuIdSet = new HashSet<String>();
		for(StuDevelopPunishment item : stuDevelopPunishmentList){
			stuIdSet.add(item.getStuid());
		}
		List<Student> stuList = SUtils.dt(studenteRemoteService.findListByIds(stuIdSet.toArray(new String[0])), new TR<List<Student>>() {});
		Map<String,String> stuMap = new HashMap<String,String>();
		for(Student stu : stuList){
			stuMap.put(stu.getId(), stu.getStudentName());
		}
		for(StuDevelopPunishment item : stuDevelopPunishmentList){
			item.setStuName(stuMap.get(item.getStuid()));
		}
        sendPagination(request, map, page);
		map.put("tabType", tabType);
		map.put("stuDevelopPunishmentList", stuDevelopPunishmentList);
		return "/studevelop/record/rewardsAndPunishList.ftl";
	}
	
	@ResponseBody
    @RequestMapping("/rewardsRecord/punishSave")
    @ControllerInfo(value = "保存惩处信息")
	public String punishSave(StuDevelopPunishment stuDevelopPunishment){
		try{
			if(StringUtils.isBlank(stuDevelopPunishment.getId())){
				stuDevelopPunishment.setId(UuidUtils.generateUuid());
			}
			stuDevelopPunishmentService.save(stuDevelopPunishment);
	    }catch(Exception e){	    	
	 	    e.printStackTrace();
		    return returnError();
	    }
	    return returnSuccess();
	}
	
	@ResponseBody
    @RequestMapping("/rewardsRecord/punishDelete")
    @ControllerInfo(value = "删除惩处信息")
	public String punishDelete(String id){
		try{
			stuDevelopPunishmentService.delete(id);
	    }catch(Exception e){	    	
	 	    e.printStackTrace();
		    return returnError();
	    }
	    return returnSuccess();
	}
}
