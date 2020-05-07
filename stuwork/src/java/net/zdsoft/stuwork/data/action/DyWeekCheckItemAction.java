package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItem;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItemDay;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItemRole;
import net.zdsoft.stuwork.data.entity.DyWeekCheckRoleUser;
import net.zdsoft.stuwork.data.service.DyWeekCheckItemDayService;
import net.zdsoft.stuwork.data.service.DyWeekCheckItemRoleService;
import net.zdsoft.stuwork.data.service.DyWeekCheckItemService;
import net.zdsoft.stuwork.data.service.DyWeekCheckResultService;
import net.zdsoft.stuwork.data.service.DyWeekCheckRoleUserService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/stuwork")
public class DyWeekCheckItemAction extends BaseAction{
	@Autowired
	private DyWeekCheckItemService dyWeekCheckItemService;
	@Autowired
	private DyWeekCheckRoleUserService dyWeekCheckRoleUserService;
	@Autowired
	private DyWeekCheckItemDayService dyWeekCheckItemDayService;
	@Autowired
	private DyWeekCheckItemRoleService dyWeekCheckItemRoleService;
	@Autowired
	private DyWeekCheckResultService dyWeekCheckResultService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@RequestMapping("/checkweek/itemEdit/page")
	@ControllerInfo(value = "考核项列表")
	public String itemList(HttpServletRequest request,ModelMap map, HttpSession httpSession) {
		LoginInfo info = getLoginInfo();
		String unitId = info.getUnitId();
		String userId = info.getUserId();
		//判断是否是总管理员
		DyWeekCheckRoleUser role = dyWeekCheckRoleUserService.findByRoleTypeAndUser(unitId,DyWeekCheckRoleUser.CHECK_ADMIN, userId);
		if(role == null){
			map.put("hasAdmin", "0");
		}else{
			map.put("hasAdmin", "1");
		}
		List<DyWeekCheckItem> items = dyWeekCheckItemService.findBySchoolId(unitId);
		map.put("items", items);
		return "/stuwork/weekCheck/itemEdit/itemAdmin.ftl";
	}
	
	@RequestMapping("/checkweek/itemEdit/edit/page")
	@ControllerInfo(value = "考核项管理--考核项编辑")
	public String itemEdit(HttpServletRequest request,ModelMap map, HttpSession httpSession) {
		String itemId = request.getParameter("itemId");
		if(StringUtils.isBlank(itemId)){
			DyWeekCheckItem item = new DyWeekCheckItem();
			List<DyWeekCheckItem> items = dyWeekCheckItemService.findBySchoolId(this.getLoginInfo().getUnitId()); 
			if(CollectionUtils.isNotEmpty(items)){
				item.setOrderId(items.get(items.size()-1).getOrderId() + 1);
			}else{
				item.setOrderId(1);
			}
			item.setSchoolId(getLoginInfo().getUnitId());
			map.put("item", item);
			List<DyWeekCheckItemDay> daylist = new ArrayList<DyWeekCheckItemDay>();
			String[] dayNames = new String[]{"","周一","周二","周三","周四","周五","周六","周日"};
			DyWeekCheckItemDay d = null;
			for(int i=1;i<8;i++){
				d = new DyWeekCheckItemDay();
				d.setDay(i);
				d.setDayName(dayNames[i]);
				d.setHasSelect("0");
				daylist.add(d);
			}
			map.put("daylist", daylist);
			List<DyWeekCheckItemRole> rolelist = new ArrayList<DyWeekCheckItemRole>();
			String[] roleNames = new String[]{"值周干部","值周班","学生处","保卫处","年级组","体育老师","卫生检查"};
			String[] roleTypes = new String[]{"02","03","04","05","06","07","08"};
			DyWeekCheckItemRole r =null;
			for(int i = 0;i<roleTypes.length;i++){
				r = new DyWeekCheckItemRole();
				r.setRoleName(roleNames[i]);
				r.setRoleType(roleTypes[i]);
				r.setHasSelect("0");
				rolelist.add(r);
			}
			map.put("rolelist", rolelist);
		}else{
			DyWeekCheckItem item = dyWeekCheckItemService.findOne(itemId);
			List<DyWeekCheckItemDay> days = dyWeekCheckItemDayService.findByItemIds(new String[]{itemId});
			List<DyWeekCheckItemRole> roles = dyWeekCheckItemRoleService.findByItemIds(new String[]{itemId});
			if(CollectionUtils.isNotEmpty(days))
				item.setItemDays(days);
			if(CollectionUtils.isNotEmpty(roles))
				item.setItemRoles(roles);
			map.put("item", item);
			List<DyWeekCheckItemDay> daylist = new ArrayList<DyWeekCheckItemDay>();
			String[] dayNames = new String[]{"","周一","周二","周三","周四","周五","周六","周日"};
			DyWeekCheckItemDay d = null;
			for(int i=1;i<8;i++){
				d = new DyWeekCheckItemDay();
				d.setDay(i);
				d.setDayName(dayNames[i]);
				d.setHasSelect("0");
				for(DyWeekCheckItemDay day : days){
					if(day.getDay() == i){
						d.setHasSelect("1");
					}
				}
				daylist.add(d);
			}
			map.put("daylist", daylist);
			List<DyWeekCheckItemRole> rolelist = new ArrayList<DyWeekCheckItemRole>();
			String[] roleNames = new String[]{"值周干部","值周班","学生处","保卫处","年级组","体育老师","卫生检查"};
			String[] roleTypes = new String[]{"02","03","04","05","06","07","08"};
			DyWeekCheckItemRole r =null;
			for(int i = 0;i<roleTypes.length;i++){
				r = new DyWeekCheckItemRole();
				r.setRoleName(roleNames[i]);
				r.setRoleType(roleTypes[i]);
				r.setHasSelect("0");
				for(DyWeekCheckItemRole role : roles){
					if(StringUtils.equals(role.getRoleType(),roleTypes[i])){
						r.setHasSelect("1");
					}
				}
				rolelist.add(r);
			}
			map.put("rolelist", rolelist);
		}
		return "/stuwork/weekCheck/itemEdit/itemEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/checkweek/itemEdit/deleteItem")
    @ControllerInfo(value = "删除考核项")
	public String deleteItem(String itemId, ModelMap map){
		if(StringUtils.isBlank(itemId)){
			return error("数据错误!");
		}
		//判断考核项是否被引用
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()),Semester.class);
		String acadyear = semester.getAcadyear();
		String se = semester.getSemester()+"";
		boolean checkUse = dyWeekCheckResultService.checkUseItem(getLoginInfo().getUnitId(),itemId, acadyear, se);
		if(checkUse){
			return error("该考核项在本学期已经被使用，不能删除！");
		}
		dyWeekCheckItemService.deleteItem(itemId);
		return success("操作成功!");
	}
	
	@ResponseBody
	@RequestMapping("/checkweek/itemEdit/save")
    @ControllerInfo(value = "保存考核项")
	public String saveCheckTeacher(String roleIds,String dayIds,DyWeekCheckItem item, ModelMap map){
		try {
			if(item == null){
				return error("无效数据!");
			}
			if(StringUtils.isNotBlank(roleIds)){
				String[] roleTypes = roleIds.split(",");
				List<DyWeekCheckItemRole> roles = new ArrayList<DyWeekCheckItemRole>();
				DyWeekCheckItemRole r = null;
				for(String roleType : roleTypes){
					r = new DyWeekCheckItemRole();
					r.setRoleType(roleType);
					r.setSchoolId(getLoginInfo().getUnitId());
					roles.add(r);
				}
				item.setItemRoles(roles);
			}else{
				return error("请选择至少一项可录入角色！");
			}
			if(item.getHasTotalScore() == 1){
				if(item.getTotalScore() < 1f){
					return error("总分值不能为小于1的整数！");
				}
			}
			if(StringUtils.isNotBlank(dayIds)){
				String[] days = dayIds.split(",");
				List<DyWeekCheckItemDay> daylist = new ArrayList<DyWeekCheckItemDay>();
				DyWeekCheckItemDay d = null;
				for(String day : days){
					d = new DyWeekCheckItemDay();
					d.setDay(NumberUtils.toInt(day));
					d.setSchoolId(getLoginInfo().getUnitId());
					daylist.add(d);
				}
				item.setItemDays(daylist);
			}else{
				return error("请选择至少一项考核时间！");
			}
			dyWeekCheckItemService.saveItem(item);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("保存失败！", e.getMessage());
		}
		return success("操作成功！");
	}
}
