package net.zdsoft.diathesis.data.action;

import com.alibaba.fastjson.JSON;
import net.zdsoft.basedata.entity.CustomRole;
import net.zdsoft.basedata.entity.CustomRoleUser;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.CustomRoleRemoteService;
import net.zdsoft.basedata.remote.service.CustomRoleUserRemoteService;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.service.DiathesisProjectExService;
import net.zdsoft.diathesis.data.service.DiathesisProjectService;
import net.zdsoft.diathesis.data.service.DiathesisSetService;
import net.zdsoft.diathesis.data.vo.DiathesisRoleUserVo;
import net.zdsoft.diathesis.data.vo.DiathesisRoleUsersVo;
import net.zdsoft.diathesis.data.vo.DiathesisRoleVo;
import net.zdsoft.diathesis.data.vo.TreeNodeVo;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.PinyinUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author: panlf
 * @Date: 2019/4/11 14:40
 */
@RestController
@RequestMapping("/diathesis/user")
public class DiathesisUserAction extends BaseAction {
    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private DeptRemoteService deptRemoteService;
    @Autowired
    private CustomRoleRemoteService customRoleRemoteService;
    @Autowired
    private CustomRoleUserRemoteService customRoleUserRemoteService;
    @Autowired
    private DiathesisSetService diathesisSetService;
    @Autowired
    private DiathesisProjectService diathesisProjectService;
    @Autowired
    private DiathesisProjectExService diathesisProjectExService;
    /**
     * 获得该单位下的部门树结构
     *
     * @return
     */
    @GetMapping("/getDepts")
    public String getDepts() {
        String unitId = getLoginInfo().getUnitId();
        List<Dept> deptList = SUtils.dt(deptRemoteService.findByUnitId(unitId), Dept.class);
        if (deptList == null || deptList.size() == 0) return error("没找到该单位的部门信息");
        List<TreeNodeVo> deptTree = deptList.stream().map(x -> {
            TreeNodeVo vo = new TreeNodeVo();
            vo.setId(x.getId());
            vo.setParentId(x.getParentId());
            vo.setName(x.getDeptName());
            return vo;
        }).collect(Collectors.toList());
        return Json.toJSONString(deptTree);
    }

    /**
     * 获得部门下的 用户
     *
     * @param deptId
     * @return
     */
    @GetMapping("/getUserByDeptId")
    public String getUserByDeptId(String deptId) {
        if (StringUtils.isBlank(deptId)) return error("部门id不能为空");
        List<User> userList = SUtils.dt(userRemoteService.findByDeptId(deptId, null), User.class);
        return Json.toJSONString(userList.stream().map(x -> {
            DiathesisRoleUserVo user = new DiathesisRoleUserVo();
            user.setUserId(x.getId());
            user.setUsername(x.getRealName());
            return user;
        }).collect(Collectors.toList()));
    }

    /**
     * 按名字搜索 该单位下的用户
     *
     * @param username
     * @return
     */
    @GetMapping("/getUserByUsername")
    public String getUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return error("username不能为空");
        }
        String unitId = getLoginInfo().getUnitId();
        List<User> userList = SUtils.dt(userRemoteService.findByUnitIdAndLikeRealName(unitId, "%" + username + "%"), User.class);
        if (userList == null || userList.size()==0){
            return error("没有这个名字的用户");
        }
        String[] deptIds = userList.stream().map(x -> x.getDeptId()).toArray(String[]::new);

        //部门map<id,name>
        Map<String, String> deptMap = SUtils.dt(deptRemoteService.findListByIds(deptIds), Dept.class).stream()
                .collect(Collectors.toMap(x -> x.getId(), x -> x.getDeptName()));

        List<DiathesisRoleUserVo> userListVo = userList.stream().map(x -> {
            DiathesisRoleUserVo user = new DiathesisRoleUserVo();
            user.setUserId(x.getId());
            user.setUsername(x.getRealName());
            user.setDeptId(x.getDeptId());
            user.setDeptName(deptMap.get(x.getDeptId()));
            return user;
        }).collect(Collectors.toList());
        return Json.toJSONString(userListVo);

    }


    //新增角色
    @PostMapping("/addRole")
    public String addRole(@RequestBody DiathesisRoleVo roleVo) {

        try {
            String roleName = roleVo.getRoleName();
            if (StringUtils.isBlank(roleName)) {
                return error("角色名称不能为空");
            }
            if(!Pattern.matches("[a-zA-Z0-9\\u4E00-\\u9FA5]*",roleName)){
                return error("角色名字只能包含,中文,英文,数字");
            }
            String unitId = getLoginInfo().getUnitId();
            String roleCode;
            //本子系统 subsystems 为 78,
            List<CustomRole> roleList = SUtils.dt(customRoleRemoteService.findListBy(new String[]{"unitId","subsystems"},new String[]{unitId,"78,"}), CustomRole.class);
            for (CustomRole x : roleList) {
                if (x.getRoleName().equals(roleName)) {
                    return error("角色名字已经存在");
                }
            }
            List<String> codeList = roleList.stream().map(x->x.getRoleCode()).collect(Collectors.toList());
            String pinyin = PinyinUtils.toHanyuPinyin(roleName, true);
            if(pinyin.length()>8){
                pinyin=pinyin.substring(0,8);
            }
            roleCode = "diathesis_" + pinyin;
            int i = 0;
            while (true) {
                String t = i == 0 ? "" : (i < 9 ? "0" + i : "" + i);
                if (codeList.contains(roleCode+t)) {
                    i++;
                } else {
                    roleCode += t;
                    break;
                }
            }
            Integer sortNum=roleList.stream().map(x->Integer.valueOf(x.getOrderId())).max((x,y)->x-y).get()+1;
            CustomRole customRole = createNewCustomRole(unitId, roleName, roleCode, "" + sortNum);
            customRoleRemoteService.save(customRole);
            return success("角色新增成功");
        } catch (Exception e) {
            return error("角色新增失败");
        }
    }

    //更新成员
    @PostMapping("/addUsers")
    public String addUsers(@RequestBody DiathesisRoleUsersVo userVo) {
        try {

            List<String> userIds = userVo.getUserIds();
            String roleId = userVo.getRoleId();
            String unitId = getLoginInfo().getUnitId();

            List<User> users = SUtils.dt(userRemoteService.findListByIds(userIds.toArray(new String[0])), User.class);
            for (User u:users){
                if(!u.getUnitId().equals(unitId)){
                    return error("不能有非本单位的用户");
                }
            }
            if (users.size() != userIds.size()) return error("有非法的用户id");
            CustomRole roleExit = SUtils.dc(customRoleRemoteService.findOneById(roleId), CustomRole.class);
            if (roleExit == null || !roleExit.getUnitId().equals(unitId)) return error("不存在该组");
            //删除原有成员
            diathesisProjectService.updateRoleUser(roleId,userIds);
            return success("添加成功");
        } catch (Exception e) {
            return error("添加失败");
        }

    }

    //编辑角色名称
    @PostMapping("/editRoleName")
    public String editRoleName(@RequestBody DiathesisRoleVo roleVo) {
        try {
            String roleName = roleVo.getRoleName();
            String roleId = roleVo.getRoleId();
            String unitId = getLoginInfo().getUnitId();
            if(StringUtils.isBlank(roleId) ){
                return error("角色id不能为空");
            }
            if(StringUtils.isBlank(roleName)){
                return error("角色名字不能为空");
            }
            if(roleName.length()>20){
                return error("角色名字最长为20个字");
            }
            if(!Pattern.matches("[a-zA-Z0-9\\u4E00-\\u9FA5]*",roleName)){
                return error("角色名字只能包含,中文,英文,数字");
            }
            //本子系统 subsystems 为 78,
            List<CustomRole> customExist = SUtils.dt(customRoleRemoteService.findListBy(new String[]{"unitId", "subsystems"}, new String[]{unitId, "78,"}), CustomRole.class);
            boolean roleIdExit=false;
            for (CustomRole c:customExist){
                if(c.getId().equals(roleId)){
                    if (DiathesisConstant.ROLE_CODE_LIST.contains(c.getRoleCode())) {
                        return error("学生本人,班主任,年纪组长,导师 此四个角色不可修改名字");
                    }
                    roleIdExit=true;
                }else if(c.getRoleName().equals(roleName)){
                    return error("角色名字已存在");
                }
            }
            if(!roleIdExit){
                return error("不存在该角色");
            }
            CustomRole role = new CustomRole();
            role.setRoleName(roleName);
            customRoleRemoteService.update(role, roleId, new String[]{"roleName"});
            return success("编辑成功");
        } catch (Exception e) {
            return error("服务器异常");
        }

    }

    /**
     * 获得角色组和下面的用户
     *
     * @return
     */
    @GetMapping("/getAllRole")
    public String getAllRole() {
        String unitId = getLoginInfo().getUnitId();

        List<CustomRole> roleList = SUtils.dt(customRoleRemoteService.findListBy(new String[]{"unitId","subsystems"},new String[]{unitId,"78,"}), CustomRole.class);
        if (roleList == null || roleList.size() == 0) {
            return error("请先初始化全局设置!");
        } else {
            List<String> roleIds = roleList.stream().map(x -> x.getId()).collect(Collectors.toList());
            List<CustomRoleUser> roleUserList = SUtils.dt(customRoleUserRemoteService.findListByIn("roleId", roleIds.toArray(new String[0])), CustomRoleUser.class);
            List<String> userIds = roleUserList.stream().map(x -> x.getUserId()).distinct().collect(Collectors.toList());
            Map<String, String> userMap = SUtils.dt(userRemoteService.findListByIds(userIds.toArray(new String[0])), User.class).stream().collect(Collectors.toMap(x -> x.getId(), x -> x.getRealName()));

            //分组 map
            Map<String, List<DiathesisRoleUserVo>> roleUserGroupMap = roleUserList.stream().map(x -> {
                DiathesisRoleUserVo user = new DiathesisRoleUserVo();
                user.setUserId(x.getUserId());
                user.setUsername(userMap.get(x.getUserId()));
                user.setRoleId(x.getRoleId());
                return user;
            }).collect(Collectors.groupingBy(x -> x.getRoleId()));

            List<DiathesisRoleVo> roleGroup = roleList.stream().filter(x->!DiathesisConstant.ROLE_CODE_LIST.contains(x.getRoleCode())).sorted((x,y)->{
                Integer xOrder = Integer.valueOf(x.getOrderId());
                Integer yOrder = Integer.valueOf(y.getOrderId());
                if(xOrder==null)return -1;
                if(yOrder==null)return 1;
                return xOrder>yOrder?1:-1;
            }).map(x -> {
                DiathesisRoleVo vo = new DiathesisRoleVo();
                vo.setRoleId(x.getId());
                vo.setRoleName(x.getRoleName());
                vo.setUser(roleUserGroupMap.get(x.getId()));
                return vo;
            }).collect(Collectors.toList());
            return JSON.toJSONString(roleGroup);
        }
    }

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    @RequestMapping("/deleteRole")
    public String deleteRole(String roleId) {
        try {
            if (StringUtils.isBlank(roleId)) return error("角色id不可为空");
            CustomRole customRole = SUtils.dc(customRoleRemoteService.findOneById(roleId), CustomRole.class);
            if (customRole == null) return error("不存在该角色");
            String roleCode = customRole.getRoleCode();
            if (DiathesisConstant.ROLE_CODE_LIST.contains(roleCode)) {
                return error("学生本人,班主任,年级组长,导师 此四个角色不可删除");
            }
            String unitId = getLoginInfo().getUnitId();

            boolean exit= diathesisProjectExService.existInputTypesByUnitIdAndRoleCode(unitId,roleCode);
           // boolean exit = diathesisSetService.exitByAndUnitIdRoleCode(unitId, roleCode);
            if(exit){
                return error("不能删除正在被使用的角色");
            }
            diathesisSetService.deleteRole(unitId,roleId,roleCode);
            return success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return error("删除失败");
        }
    }

    public CustomRole createNewCustomRole(String unitId, String roleName, String roleCode, String orderId) {
        CustomRole role = new CustomRole();
        role.setId(UuidUtils.generateUuid());
        role.setUnitId(unitId);
        role.setRoleName(roleName);
        role.setRoleCode(roleCode);
        //当前子系统都默认78,
        role.setSubsystems("78,");
        role.setOrderId(orderId);
        role.setType("0");
        return role;
    }

}
