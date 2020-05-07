package net.zdsoft.newstusys.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.newstusys.constants.BaseStudentConstants;
import net.zdsoft.newstusys.service.BaseStudentService;

/**
 * 
 * @author weixh
 * @since 2018年3月7日 上午10:16:27
 */
@Controller
@RequestMapping("/newstusys/sch/student")
public class NewStusysPhotoAction extends BaseAction {
	@Autowired
	private BaseStudentService baseStudentService;
	
	@RequestMapping("/photoimportadmin")
	public String index(ModelMap map) {
		map.put("key", getLoginInfo().getUnitId()+BaseStudentConstants.PICTURE_FILEPATH);
		return "/newstusys/sch/student/photoimport/photoImport.ftl";
	}
	
	@ResponseBody
	@ControllerInfo("校验保存图片")
	@RequestMapping("/photoimport/checkSave")
	public String saveFromDir(ModelMap map, HttpServletRequest request){
		try {
			String unitId = getLoginInfo().getUnitId();
			baseStudentService.saveStudentPics(unitId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error("照片更新失败！");
		}
		return success("照片更新成功！");
	}
	
}
