package net.zdsoft.career.data.action;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.career.data.constant.CarConstants;
import net.zdsoft.career.data.entity.CarResourceCenter;
import net.zdsoft.career.data.service.CarResourceCenterService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/career/resourcecenter")
public class CarResourceCenterAction extends BaseAction{
	
	@Autowired
	private CarResourceCenterService carResourceCenterService;
	
	@RequestMapping("/indexpage/resourcetab")
	@ControllerInfo("资源Tab")
	public String showResourcetab(){
		return "/career/resourcecenter/indexpage/carResourceTab.ftl";
	}
	
	@RequestMapping("/indexpage/resourcelist")
	@ControllerInfo("资源List")
	public String showResourceList(Integer resourceType, ModelMap map, HttpServletRequest request){
		Pagination page = createPagination();
		List<CarResourceCenter> carResourceList = carResourceCenterService.findByResourceType(resourceType,
				CarConstants.CAR_RESOURCE_TYPE_0,null,true,page);
		map.put("carResourceList", carResourceList);
		sendPagination(request, map, page);
		return "/career/resourcecenter/indexpage/carResourceList.ftl";
	}
	
	@RequestMapping("/indexpage/resourceindex")
	@ControllerInfo("资源Index")
	public String showResourceIndex(String resourceId, ModelMap map){
		CarResourceCenter carResourceCenter = carResourceCenterService.findOneById(resourceId,false);
		map.put("carResourceCenter", carResourceCenter);
		return "/career/resourcecenter/indexpage/carResourceIndex.ftl";
	}
	
	@RequestMapping("/managepage/resourcetab")
	@ControllerInfo("后台资源管理Tab")
	public String showManResourceTab(){
		return "/career/resourcecenter/managepage/carResourceTab.ftl";
	}
	
	@RequestMapping("/managepage/resourcelist")
	@ControllerInfo("后台资源管理List")
	public String showManResourceList(Integer resourceType, Integer type,String titleName, ModelMap map, HttpServletRequest request){
		Pagination page = createPagination();
		try {
			titleName=URLDecoder.decode(titleName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List<CarResourceCenter> carResourceList = carResourceCenterService.findByResourceType(resourceType,type,titleName,false,page);
		map.put("carResourceList", carResourceList);
		sendPagination(request, map, page);
		return "/career/resourcecenter/managepage/carResourceList.ftl";
	}
	
	@RequestMapping("/managepage/resourceadd")
	@ControllerInfo("后台资源管理Add或编辑")
	public String showManAddResource(String resourceId,Integer resourceType, ModelMap map){
		CarResourceCenter carResource = null;
		if (StringUtils.isNotBlank(resourceId)) {
			carResource = carResourceCenterService.findOneById(resourceId,true);
		} else {
			carResource = new CarResourceCenter();
			carResource.setResourceType(resourceType);
		}
		String photoDirId = UuidUtils.generateUuid();
		String videoDirId = UuidUtils.generateUuid();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		String photoPath = ymd + "\\" + File.separator + photoDirId;
		String videoPath = ymd + "\\" + File.separator + videoDirId;
		map.put("photoDirId", photoDirId);
		map.put("photoPath", photoPath);
		map.put("videoDirId", videoDirId);
		map.put("videoPath", videoPath);
		map.put("carResource", carResource);
		return "/career/resourcecenter/managepage/carResourceIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/managepage/resourcedelete")
	@ControllerInfo("后台资源管理delete")
	public String deleteResource(String resourceIds,ModelMap map){
		try {
			carResourceCenterService.deleteByIds(resourceIds);
		} catch (Exception e) {
			e.printStackTrace();
			return error("删除失败！");
		}
		return success("删除成功");
	}
	
	@ResponseBody
	@RequestMapping("/managepage/saveresource")
	@ControllerInfo("保存资源")
	public String resourceSave(CarResourceCenter carResource,String pictureArray,String videoArray){
		try{
			carResourceCenterService.saveResource(carResource,pictureArray,videoArray);
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
}	
