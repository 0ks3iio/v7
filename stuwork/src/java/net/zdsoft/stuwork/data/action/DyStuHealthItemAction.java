package net.zdsoft.stuwork.data.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.entity.DyStuHealthItem;
import net.zdsoft.stuwork.data.service.DyStuHealthItemService;
import net.zdsoft.stuwork.data.service.DyStuHealthProjectItemService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/stuwork/health")
public class DyStuHealthItemAction extends BaseAction{
	@Autowired
	private DyStuHealthItemService dyStuHealthItemService;
	@Autowired
	private DyStuHealthProjectItemService dyStuHealthProjectItemService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	
	@RequestMapping("/item/index")
	@ControllerInfo(value = "指标设置index")
	public String showIndex(ModelMap map){
		return "/stuwork/health/item/healthItemIndex.ftl";
	}
	@RequestMapping("/project/list")
	@ControllerInfo(value = "学期指标引用list")
	public String projectList(ModelMap map,HttpServletRequest request){
		String acadyear=request.getParameter("acadyear");
		String semester=request.getParameter("semester");
		if(StringUtils.isBlank(acadyear)){
			Semester se=Semester.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()));
			if(se==null){
				return error("当前时间不在学年学期范围内");
			}
			acadyear=se.getAcadyear();
			semester=se.getSemester()+"";
		}
		
		List<DyStuHealthItem> itemList=dyStuHealthItemService.findBySemester(getLoginInfo().getUnitId(), acadyear, semester);
		
		List<String> acadyearList=SUtils.dt(semesterRemoteService.findAcadeyearList(),new TR<List<String>>(){});
		map.put("acadyearList", acadyearList);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("itemList", itemList);
		return "/stuwork/health/item/healthProjectList.ftl";
	}
	@ResponseBody
	@RequestMapping("/project/saveProject")
	@ControllerInfo(value = "saveProject")
	public String saveProject(ModelMap map,HttpServletRequest request){
		try{
			String ids=request.getParameter("ids");
			String message=dyStuHealthProjectItemService.saveProject(getLoginInfo().getUnitId(),StringUtils.isNotBlank(ids)?ids.split(","):new String[]{},
														request.getParameter("acadyear"), request.getParameter("semester"));
			if(!message.equals("success")){
				return error(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@RequestMapping("/item/list")
	@ControllerInfo(value = "指标设置list")
	public String showList(ModelMap map){
		
		List<DyStuHealthItem> itemList=dyStuHealthItemService.findByUnitId(getLoginInfo().getUnitId());
		map.put("itemList", itemList);
		return "/stuwork/health/item/healthItemList.ftl";
	}
	
	@RequestMapping("/item/edit")
	@ControllerInfo(value = "指标设置edit")
	public String editItem(ModelMap map,String id){
		DyStuHealthItem  item=null;
		if(StringUtils.isNotBlank(id)){
			item=dyStuHealthItemService.findOne(id);
		}
		if(item==null){
			item=new DyStuHealthItem();
			List<DyStuHealthItem> itemList=dyStuHealthItemService.findByUnitId(getLoginInfo().getUnitId());
			if(CollectionUtils.isNotEmpty(itemList)){
				item.setOrderId(itemList.get(itemList.size()-1).getOrderId()+1);
			}else{
				item.setOrderId(1);
			}
		}
		map.put("item", item);
		return "/stuwork/health/item/healthItemEdit.ftl";
	}
	@ResponseBody
	@RequestMapping("/item/save")
	@ControllerInfo(value = "指标设置save")
	public String saveItem(ModelMap map,DyStuHealthItem item){
		try{
			List<DyStuHealthItem> itemList=dyStuHealthItemService.findbyItemName(getLoginInfo().getUnitId(),item.getItemName());
			boolean flag=CollectionUtils.isNotEmpty(itemList);
			if(StringUtils.isNotBlank(item.getId())){
				if(flag){
					for(DyStuHealthItem everitem:itemList){
						if(!everitem.getId().equals(item.getId())){
							return error("名称不能重复");
						}
					}
				}
				dyStuHealthItemService.update(item,item.getId(),new String[]{"orderId","itemName","itemUnit"});
			}else{
				if(flag){
					return error("名称不能重复");
				}
				item.setId(UuidUtils.generateUuid());
				item.setUnitId(getLoginInfo().getUnitId());
				dyStuHealthItemService.save(item);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	@ResponseBody
	@RequestMapping("/item/delete")
	@ControllerInfo(value = "指标设置delete")
	public String deleteItem(ModelMap map,String id){
		try{
			if(CollectionUtils.isNotEmpty(dyStuHealthProjectItemService.findByItemId(getLoginInfo().getUnitId(), id))){
				return error("此指标已被引用,无法删除");
			}
			dyStuHealthItemService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
}
