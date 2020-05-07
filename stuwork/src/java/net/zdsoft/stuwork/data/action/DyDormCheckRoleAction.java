package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.dto.DormSearchDto;
import net.zdsoft.stuwork.data.entity.DyDormBuilding;
import net.zdsoft.stuwork.data.entity.DyDormCheckRole;
import net.zdsoft.stuwork.data.service.DyDormBuildingService;
import net.zdsoft.stuwork.data.service.DyDormCheckRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.TypeReference;

@Controller
@RequestMapping("/stuwork")
public class DyDormCheckRoleAction extends BaseAction{
	@Autowired
	private DyDormBuildingService dyDormBuildingService;
	@Autowired
	private DyDormCheckRoleService dyDormCheckRoleService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	
	@InitBinder
	public void initBinder(WebDataBinder wb) throws Exception {
		wb.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
	}
	
	@RequestMapping("/dorm/checkRole/index/page")
    @ControllerInfo(value = "index")
    public String showIndex(ModelMap map,HttpServletRequest request, HttpSession httpSession) {
		/*String acadyearStr=request.getParameter("acadyearStr");
		String semesterStr=request.getParameter("semesterStr");
		List<String> acadyearList=semesterService.findAcadeyearList();
		if(StringUtils.isBlank(acadyearStr)){
			Semester semester=semesterService.getCurrentSemester(1);
			acadyearStr=semester.getAcadyear();
			semesterStr=String.valueOf(semester.getSemester());
		}*/
		Semester semester=SUtils.dt(semesterRemoteService.getCurrentSemester
				(0,getLoginInfo().getUnitId()),new TypeReference<Semester>(){});
		if(semester==null){
			return errorFtl(map, "当前时间不在学年学期范围内");
		}
		String acadyear=semester.getAcadyear();
		String semesterStr=String.valueOf(semester.getSemester());
		List<DyDormBuilding> buildingList=dyDormBuildingService.getDormBuildingsByUnitId(getLoginInfo().getUnitId(),acadyear,semesterStr);
		
		map.put("buildingList", buildingList);
		//map.put("acadyearList", acadyearList);
		map.put("acadyear", acadyear);
		map.put("semesterStr", semesterStr);
		return "/stuwork/dorm/checkRole/dyDormCheckRoleIndex.ftl";
	}
	@RequestMapping("/dorm/checkRole/save")
	@ResponseBody
	@ControllerInfo(value = "获取最近的数据", parameter = "{ids}")
	public String saveCheckRole(String ids,DormSearchDto dormDto) {
		String[] userIds = ids.split(",");
		String acadyear=dormDto.getAcadyear();
		String semesterStr=dormDto.getSemesterStr();
		String schoolId=getLoginInfo().getUnitId();
		String buildingId=dormDto.getSearchBuildId();
		
		dormDto.setUnitId(schoolId);
		dyDormCheckRoleService.deleteBy(dormDto);
		
		List<DyDormCheckRole> roleList=new ArrayList<DyDormCheckRole>();
		for (int i = 0; i < userIds.length; i++) {
			DyDormCheckRole role=new DyDormCheckRole();
			role.setId(UuidUtils.generateUuid());
			role.setSchoolId(schoolId);
			role.setUserId(userIds[i]);
			role.setBuildingId(buildingId);
			role.setAcadyear(acadyear);
			role.setSemester(semesterStr);
			roleList.add(role);
			RedisUtils.addDataToList("user-popup-recent-"+ getLoginInfo().getUserId(), userIds[i], 12);
		}
		dyDormCheckRoleService.saveAll(roleList.toArray(new DyDormCheckRole[0]));
		return "";
	}
	/*@RequestMapping("/dorm/checkRole/edit/page")
    @ControllerInfo(value = "edit")
	public String editCheckRole(ModelMap map,DyDormCheckRole checkRole) {
		List<User> userList=userService.findListBy(new String[]{"unitId","ownerType"},new String[]{getLoginInfo().getUnitId(),"2"});
		map.put("checkRole", checkRole);
		map.put("userList", userList);
		return "/stuwork/dorm/checkRole/dyDormCheckRoleUser.ftl";
	}
	 @ResponseBody
    @RequestMapping("/dorm/checkRole/save11")
	@ControllerInfo("寝室楼save")
    public String saveCheckRole(DyDormCheckRole checkRole,ModelMap map) {
		try{
			checkRole.setSchoolId(getLoginInfo().getUnitId());
			List<DyDormCheckRole> checkRoleList=dyDormCheckRoleService.findListBy(new String[]{"userId","acadyear","semester","buildingId"},new String[]{checkRole.getUserId(),
					checkRole.getAcadyear(),checkRole.getSemester(),checkRole.getBuildingId()});
			//List<DyDormCheckRole> checkRoleList=dyDormCheckRoleService.findListBy("userId", checkRole.getUserId());
			if(CollectionUtils.isNotEmpty(checkRoleList)){
				returnSuccess();
			}
			checkRole.setId(UuidUtils.generateUuid());
			dyDormCheckRoleService.save(checkRole);
		}catch(ControllerException e){
			return error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
    }*/
	@ResponseBody
	@RequestMapping("/dorm/checkRole/delete")
	@ControllerInfo("寝室楼save")
	public String deleteCheckRole(String  id,ModelMap map) {
		try{
			dyDormCheckRoleService.delete(id);
		}catch(ControllerException e){
			return error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
}
