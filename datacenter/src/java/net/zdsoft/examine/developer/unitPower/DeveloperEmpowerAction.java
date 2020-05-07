//package net.zdsoft.system.action.openapi.unitPower;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//import net.zdsoft.base.entity.eis.DeveloperPower;
//import net.zdsoft.framework.action.BaseAction;
//import net.zdsoft.framework.annotation.ControllerInfo;
//import net.zdsoft.framework.utils.EntityUtils;
//import net.zdsoft.framework.utils.SUtils;
//import net.zdsoft.framework.utils.UuidUtils;
//import net.zdsoft.remote.openapi.remote.service.DeveloperPowerRemoteService;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.alibaba.fastjson.JSONObject;
//
//@Controller
//@RequestMapping("/system/developer/empower")
//public class DeveloperEmpowerAction extends BaseAction {
//	
//	@Autowired
//    private DeveloperPowerRemoteService developerPowerRemoteService;
////    @Autowired
////    private UnitRemoteService unitRemoteService;
//    
//	@ResponseBody
//	@RequestMapping("/addUnit")
//	@ControllerInfo("单位授权")
//	public String empowerAdd(String unitIds,String developerId){
//		try {
//			List<String> addUids = new ArrayList<>();
//			if(StringUtils.isNotBlank(unitIds) && StringUtils.isNotBlank(developerId)) {
//				List<DeveloperPower> developerPowers = SUtils.dt(developerPowerRemoteService.findByDeveloperIdAndUnitIdIn(developerId,unitIds.split(",")),
//						DeveloperPower.class);
//				List<String> uids = EntityUtils.getList(developerPowers, DeveloperPower::getUnitId);
//				String[] aStrings = unitIds.split(",");
//				for (String uid : aStrings) {
//					addUids.add(uid);
//				}
//				addUids.removeAll(uids);
//				if(CollectionUtils.isNotEmpty(addUids)) {
//					List<DeveloperPower> addDP = new ArrayList<>();
//					addUids.forEach(uid->{
//						DeveloperPower developerPower = new DeveloperPower();
//						developerPower.setId(UuidUtils.generateUuid());
//						developerPower.setUnitId(uid);
//						developerPower.setDeveloperId(developerId);
//						addDP.add(developerPower);
//					});
//					developerPowerRemoteService.saveAll(addDP.toArray(new DeveloperPower[0]));
//				}
//			}
//		} catch (Exception e) {
//			return error("添加失败");
//		}
//		return success("添加成功");
//	}
//    
//    @ResponseBody
//	@RequestMapping("/deleteUnit")
//	@ControllerInfo("删除单位")
//	public String empowerDelete(@RequestBody String param,String developerId){
//		try {
//			JSONObject jsonObject = SUtils.dc(param, JSONObject.class);
//    		List<String>  unitIdList = JSONObject.parseArray(jsonObject.getString("unitIds"), String.class);
//    		developerPowerRemoteService.deleteByDeveloperIdAndUnitIdIn(developerId,unitIdList.toArray(new String[unitIdList.size()]));
//		} catch (Exception e) {
//			return error("删除失败");
//		}
//		return success("删除成功");
//	}
//    
//	@RequestMapping("/findUnit")
//	@ControllerInfo("查找单位")
//	public String empowerDelete(ModelMap map,String developerId){
//			//查找已经授权的单位列表
//        List<DeveloperPower> developerPowers = SUtils.dt(developerPowerRemoteService.findByDeveloperId(developerId), 
//        		DeveloperPower.class);
//        if(CollectionUtils.isNotEmpty(developerPowers)) {
//        	Set<String> uids = EntityUtils.getSet(developerPowers, DeveloperPower::getUnitId);
////        	List<Unit> allUnits = SUtils.dt(unitRemoteService.findListByIds(uids.toArray(new String[uids.size()]))
////        			, Unit.class);
//        	map.put("empowerUnits", null);
//        }
//        map.put("developerId", developerId);
//		return "/openapi/system/developer/developerPower.ftl";
//	}
//
//}
