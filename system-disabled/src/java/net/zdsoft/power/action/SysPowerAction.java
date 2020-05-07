package net.zdsoft.power.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.power.entity.SysPower;
import net.zdsoft.power.service.SysPowerService;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.remote.service.ServerRemoteService;

/**
 * @author yangsj  2018年6月7日下午2:15:27
 */
@Controller
@RequestMapping("/system/power")
public class SysPowerAction extends BaseAction {

	@Autowired
	private SysPowerService sysPowerService;
	@Autowired
	private ServerRemoteService serverRemoteService;
	@RequestMapping("/showPowerList/page")
    @ControllerInfo("查看权限列表")
	public String showPowerList(ModelMap map){
		LoginInfo info = getLoginInfo();
		List<Server> serverList = SUtils.dt(serverRemoteService.findByOwnerTypeAndUnitIdAndUnitClass(info.getOwnerType(), info.getUnitId(), info.getUnitClass()), Server.class);
		map.put("serverList", serverList);
    	return "/system/power/pw/powerIndex.ftl";
	}
	
	@ResponseBody
    @ControllerInfo("更新权限信息")
    @RequestMapping("/updatePower")
    public String updatePower(String powerId,String powerName,String value,String description) {
        try {
        	SysPower sysPower = sysPowerService.findOne(powerId);
        	if(sysPower != null) {
        		sysPower.setPowerName(powerName);
        		sysPower.setValue(value);
        		sysPower.setDescription(description);
        	}else {
        		return error("没有这个权限！");
        	}
        	sysPowerService.save(sysPower);
		} catch (Exception e) {
			return error("更新失败！"+e.getMessage());
		}
        return success("更新成功");
    }
}
