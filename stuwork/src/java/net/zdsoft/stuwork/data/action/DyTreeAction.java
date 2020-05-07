package net.zdsoft.stuwork.data.action;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.stuwork.data.service.DyTreeService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

@Controller
@RequestMapping("/stuwork")
public class DyTreeAction extends BaseAction{

	@Autowired
	private DyTreeService dyTreeService;
	
    /**
     * 本单位年级-有权限的班级
     * 
     * @param unitId
     * @return
     */
    @ResponseBody
    @RequestMapping("/tree/gradeClassForSchoolInsetTree/page")
    @ControllerInfo("ztree-本单位年级-班级")
    public String gradeClassForSchoolInsetTree(HttpSession httpSession) {
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
        String userId = loginInfo.getUserId();
        JSONArray jsonArray = dyTreeService.gradeClassForSchoolInsetZTree(unitId,userId);
        return Json.toJSONString(jsonArray);
    }
    
    /**
     * 本单位年级-有权限班级-学生
     * 
     * @param unitId
     * @return
     */
    @ResponseBody
    @RequestMapping("/tree/gradeClassStudentForSchoolInsetTree/page")
    @ControllerInfo("ztree-本单位年级-班级-学生")
    public String gradeClassStudentForSchoolInsetTree(HttpSession httpSession) {
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
        String userId = loginInfo.getUserId();
        JSONArray jsonArray = dyTreeService.gradeClassStudentForSchoolInsetZTree(unitId,userId);
        return Json.toJSONString(jsonArray);
    }
	
}
