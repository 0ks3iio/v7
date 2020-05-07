package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.dto.TreeNodeDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.TreeService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

@Controller
@RequestMapping("/basedata")
public class TreeAction extends RoleCommonAction {

    @Autowired
    private TreeService treeService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;

    // 年级
    /**
     * 本单位年级
     * 
     * @param unitId
     * @return
     */
    @ResponseBody
    @RequestMapping("/tree/gradeForSchoolInsetTree/page")
    @ControllerInfo("ztree-本单位年级")
    public String gradeForSchoolInsetTree(HttpSession httpSession) {
        Set<String> unitIds = new HashSet<String>();
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
        unitIds.add(unitId);
        JSONArray jsonArray = treeService.gradeForSchoolInsetZTree(unitIds.toArray(new String[0]));
        return Json.toJSONString(jsonArray);
    }

    /**
     * 直属单位年级
     * 
     * @param unitId
     * @return
     */
    @ResponseBody
    @RequestMapping("/tree/gradeForUnitInsetTree/page")
    @ControllerInfo("ztree-直属单位年级")
    public String gradeForUnitInsetTree(HttpSession httpSession) {
        Set<String> unitIds = new HashSet<String>();
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
        unitIds.add(unitId);
        JSONArray jsonArray = treeService.gradeForUnitInsetZTree(unitIds.toArray(new String[0]));
        return Json.toJSONString(jsonArray);
    }

    // 班级
    /**
     * 本单位年级-班级
     * 
     * @param unitId
     * @return
     */
    @ResponseBody
    @RequestMapping("/tree/gradeClassForSchoolInsetTree/page")
    @ControllerInfo("ztree-本单位年级-班级")
    public String gradeClassForSchoolInsetTree(String sections,String gradeCodes,HttpSession httpSession) {
        Set<String> unitIds = new HashSet<String>();
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
        unitIds.add(unitId);
//        String sections = request.getParameter("sections");
//        String gradeCodes = request.getParameter("gradeCodes");
        if(StringUtils.isNotBlank(sections) || StringUtils.isNotBlank(gradeCodes)){
        	JSONArray jsonArray = treeService.gradeClassForSchoolInsetZTree(sections,gradeCodes,unitIds.toArray(new String[0]));
        	return Json.toJSONString(jsonArray);
        }else{
        	JSONArray jsonArray = treeService.gradeClassForSchoolInsetZTree(unitIds.toArray(new String[0]));
        	return Json.toJSONString(jsonArray);
        }
    }

    /**
     * 本单位年级-班级-学生
     * 
     * @param unitId
     * @return
     */
    @ResponseBody
    @RequestMapping("/tree/gradeClassStudentForSchoolInsetTree/page")
    @ControllerInfo("ztree-本单位年级-班级-学生")
    public String gradeClassStudentForSchoolInsetTree(String isRole, HttpSession httpSession) {
        Set<String> unitIds = new HashSet<String>();
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
        unitIds.add(unitId);
        JSONArray jsonArray = new JsonArray();
        if(Constant.IS_TRUE_Str.equals(isRole)){
        	if(isAdmin(unitId, loginInfo.getUserId())){
        		jsonArray = treeService.gradeClassStudentForSchoolInsetZTree(unitIds.toArray(new String[0]));
        	}else{
        		String teacherId = loginInfo.getOwnerId();
        		Set<String> classIds = new HashSet<String>();
        		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId),Grade.class);
        		List<String> gradeIds=new ArrayList<>();
				List<String> gradeIds1=new ArrayList<>();
				for(Grade g:gradeList) {
					gradeIds.add(g.getId());
					if(teacherId.equals(g.getTeacherId())) {
						gradeIds1.add(g.getId());
					}
				}
				List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdInGradeIds(unitId,gradeIds.toArray(new String[] {})), Clazz.class);
				for (Clazz clazz : classList) {
					if(gradeIds1.contains(clazz.getGradeId())){
						classIds.add(clazz.getId());
					}else{
						if(teacherId.equals(clazz.getTeacherId())||teacherId.equals(clazz.getViceTeacherId())){
							classIds.add(clazz.getId());
						}
					}
				}
				if(classIds.size()>0){
					jsonArray = treeService.gradeClassStudentForClazzInsetZTree(classIds.toArray(new String[classIds.size()]));
				}
        	}
        }else{
        	jsonArray = treeService.gradeClassStudentForSchoolInsetZTree(unitIds.toArray(new String[0]));
        }
        return Json.toJSONString(jsonArray);
    }
    
    /**
     * 本单位-用户类型-用户
     * 
     * @param unitId
     * @return
     */
    @ResponseBody
    @RequestMapping("/tree/ownerTypeUserForUnitInsetTree/page")
    @ControllerInfo("ztree-本单位-用户类型-用户")
    public String ownerTypeUserForUnitInsetTree(HttpSession httpSession) {
//        Set<String> unitIds = new HashSet<String>();
        LoginInfo loginInfo = getLoginInfo(httpSession);
//        String unitId = loginInfo.getUnitId();
//        unitIds.add(unitId);
        JSONArray jsonArray = treeService.ownerTypeUserForUnitInsetTree(loginInfo.getUnitId());
        return Json.toJSONString(jsonArray);
    }
    
    /**
     * 角色类型-角色
     * 
     * @param unitId
     * @return
     */
    @ResponseBody
    @RequestMapping("/tree/typeServerRoleForUserInsetTree/page")
    @ControllerInfo("ztree-本单位-用户类型-用户")
    public String typeServerRoleForUserInsetTree(HttpSession httpSession) {
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
        Integer ownerType = loginInfo.getOwnerType();
        Integer unitClass = loginInfo.getUnitClass();
        JSONArray jsonArray = treeService.typeServerRoleForUserInsetTree(unitId,ownerType,unitClass);
        return Json.toJSONString(jsonArray);
    }

    /**
     * 直属单位年级-班级
     * 
     * @param unitId
     * @return
     */
    @ResponseBody
    @RequestMapping("/tree/gradeClassForUnitInsetTree/page")
    @ControllerInfo("ztree-直属单位年级-班级")
    public String gradeClassForUnitInsetTree(HttpSession httpSession) {
        Set<String> unitIds = new HashSet<String>();
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
        unitIds.add(unitId);
        JSONArray jsonArray = treeService.gradeClassForUnitInsetZTree(unitIds.toArray(new String[0]));
        return Json.toJSONString(jsonArray);
    }

    // 单位
    /**
     * 直属单位
     * 
     * @param unitId
     * @return
     */
    @ResponseBody
    @RequestMapping("/tree/unitForDirectInsetTree/page")
    @ControllerInfo("ztree-直属单位")
    public String unitForDirectInsetTree(boolean isSchool, HttpSession httpSession) {
        Set<String> unitIds = new HashSet<String>();
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
        unitIds.add(unitId);
        JSONArray jsonArray = treeService.unitForDirectInsetZTree(isSchool, unitIds.toArray(new String[0]));
        return Json.toJSONString(jsonArray);
    }
    
    /**
     * 所有下属单位
     * 
     * @param unitId
     * @return
     */
    @ResponseBody
    @RequestMapping("/tree/unitForSubInsetTree/page")
    @ControllerInfo("ztree-下属单位")
    public String unitForSubInsetTree(HttpSession httpSession) {
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
        
        JSONArray jsonArr = new JSONArray();
		Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        List<Unit> unitList = SUtils.dt(unitRemoteService.findByUniCode(unit.getUnionCode()+"%"), new TR<List<Unit>>(){});
        Map<String, List<Unit>> unitMap = new HashMap<String, List<Unit>>();
        for (Unit item : unitList) {
            List<Unit> list = unitMap.get(item.getParentId());
            if (list == null) {
                list = new ArrayList<Unit>();
                unitMap.put(item.getParentId(), list);
            }
            list.add(item);
        }
        
        for (Map.Entry<String, List<Unit>> entry : unitMap.entrySet()) {
            String key = entry.getKey();
            List<Unit> value = entry.getValue();
            findUnitZTreeJson(key, true, jsonArr, value);
        }
        return Json.toJSONString(jsonArr);
    }
    
    public void findUnitZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<Unit> list) {
        if (StringUtils.isBlank(pId)) {
            pId = "";
        }
        TreeNodeDto treeNodeDto = null;
        for (Unit unit : list) {
            treeNodeDto = new TreeNodeDto();
            treeNodeDto.setpId(pId);
            treeNodeDto.setId(unit.getId());
            treeNodeDto.setName(unit.getUnitName());
            treeNodeDto.setTitle(unit.getUnitName());
            treeNodeDto.setOpen(isOpen);
            if (Unit.UNIT_CLASS_SCHOOL == unit.getUnitClass()) {
                treeNodeDto.setType("school");
            }
            else {
                treeNodeDto.setType("edu");
            }
            jsonArr.add(JSON.toJSON(treeNodeDto));
        }
    }

    // 部门
    /**
     * 本单位部门
     * 
     * @param unitId
     * @return
     */
    @ResponseBody
    @RequestMapping("/tree/deptForUnitInsetTree/page")
    @ControllerInfo("ztree-本单位部门")
    public String deptForUnitInsetTree(HttpSession httpSession) {
        Set<String> unitIds = new HashSet<String>();
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
        unitIds.add(unitId);
        JSONArray jsonArray = treeService.deptForUnitInsetZTree(unitIds.toArray(new String[0]));
        return Json.toJSONString(jsonArray);
    }

    /**
     * 本单位部门 - 教师
     * 
     * @param unitId
     * @return
     */
    @ResponseBody
    @RequestMapping("/tree/deptTeacherForUnitInsetTree/page")
    @ControllerInfo("ztree-本单位部门-教师")
    public String deptTeacherForUnitInsetTree(HttpSession httpSession) {
        Set<String> unitIds = new HashSet<String>();
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
        unitIds.add(unitId);
        JSONArray jsonArray = treeService.deptTeacherForUnitInsetZTree(unitIds.toArray(new String[0]));
        return Json.toJSONString(jsonArray);
    }

    /**
     * 直属单位部门
     * 
     * @param unitId
     * @return
     */
    @ResponseBody
    @RequestMapping("/tree/deptForDirectUnitInsetTree/page")
    @ControllerInfo("ztree-直属单位部门")
    public String deptForDirectUnitInsetTree(HttpSession httpSession) {
        Set<String> unitIds = new HashSet<String>();
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
        unitIds.add(unitId);
        JSONArray jsonArray = treeService.deptForDirectUnitInsetZTree(unitIds.toArray(new String[0]));
        return Json.toJSONString(jsonArray);
    }
    /**
     * 
     * @param httpSession
     * @return
     */
    @ResponseBody
    @RequestMapping("/tree/placeForBuildingTree/page")
    @ControllerInfo("ztree-教学楼-场地")
    public String placeForBuildingTree(HttpSession httpSession) {
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
        JSONArray jsonArray = treeService.placeForBuildingTree(unitId);
        return Json.toJSONString(jsonArray);
    }
    
    
    @ResponseBody
    @RequestMapping("/tree/teacherForGroupTree/page")
    @ControllerInfo("ztree-教研组-教师")
    public String teacherForGroupTree(String isRole, HttpSession httpSession) {
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
        JSONArray jsonArray;
        if(Constant.IS_TRUE_Str.equals(isRole)){//是否根据教务权限来展示
        	if(isAdmin(unitId,loginInfo.getUserId())){
        		jsonArray = treeService.teacherForGroupTree(unitId, null);
        	}else{
        		jsonArray = treeService.teacherForGroupTree(unitId, loginInfo.getOwnerId());
        	}
        }else{
        	jsonArray = treeService.teacherForGroupTree(unitId, null);
        }
        return Json.toJSONString(jsonArray);
    }
    
}
