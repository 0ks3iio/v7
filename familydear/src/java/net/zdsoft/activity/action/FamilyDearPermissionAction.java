package net.zdsoft.activity.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.zdsoft.activity.entity.FamilyDearPermission;
import net.zdsoft.activity.service.FamilyDearPermissionService;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/familydear/permissionSet")
public class FamilyDearPermissionAction extends BaseAction{

	@Autowired
    private FamilyDearPermissionService familyDearPermissionService;
    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @RequestMapping("/index/page")
    public String stuDevelopPermissionAdmin(){
        return "/familydear/permission/familydearPermissionAdmin.ftl";
    }
    @RequestMapping("/list")
    public String familydearPermissionList(String permissionType ,
                                           ModelMap map){
        String unitId = getLoginInfo().getUnitId();
        List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByUnitId(unitId), Teacher.class);
        Map<String,String> teacherNameMap = teachers.stream().collect(Collectors.toMap(Teacher::getId,Teacher::getTeacherName));

        List<FamilyDearPermission> permissionList = familyDearPermissionService.getFamilyDearPermissionList(unitId, permissionType);
        FamilyDearPermission permission = new FamilyDearPermission();
        if(CollectionUtils.isNotEmpty(permissionList)){
            permission = permissionList.get(0);
            if(StringUtils.isNotEmpty(permission.getUserIds())){
                String[] ids = permission.getUserIds().split(",");
                List<User>  userList = userRemoteService.findListObjectByIds(ids);
                Map<String,String> userTeaIdMap = new HashMap<>();
                for (User user : userList) {
                    userTeaIdMap.put(user.getId() , user.getOwnerId());

                }
                StringBuffer buffer = new StringBuffer();
                for (String id : ids) {
                    String teacherName = teacherNameMap.get(userTeaIdMap.get(id));
                    buffer.append(teacherName + ",");
                }
                permission.setUserNames(buffer.substring(0,buffer.length()-1));
            }
        }else{
            permission.setUnitId(unitId);
            permission.setPermissionType(permissionType);
        }
        map.put("permission",permission);
        return "/familydear/permission/familydearPermission.ftl";
    }
    @ResponseBody
    @RequestMapping("/save")
    public String familydearPermissionSave(FamilyDearPermission permission){
        try{
            if(StringUtils.isEmpty(permission.getId())){
                permission.setId(UuidUtils.generateUuid());
                permission.setCreationTime(new Date());
            }else{
                permission.setModifyTime(new Date());
            }
            familyDearPermissionService.save(permission);
        }catch (Exception e) {
            e.printStackTrace();
            return error("保存失败！"+e.getMessage());
        }
        return success("保存成功");
    }
}
