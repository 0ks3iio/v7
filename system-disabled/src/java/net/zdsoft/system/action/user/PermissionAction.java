package net.zdsoft.system.action.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.ColumnInfoEntity;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Specifications;
import net.zdsoft.framework.utils.ColumnInfoUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.Permission;
import net.zdsoft.system.entity.server.SubSystem;
import net.zdsoft.system.service.server.PermissionService;
import net.zdsoft.system.service.server.SubsystemService;

@Controller
@RequestMapping("/basedata")
public class PermissionAction extends BaseAction {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private SubsystemService subsystemService;
    @Autowired
    private UnitRemoteService unitRemoteService;

    @RequestMapping("/permission/index/page")
    public String showIndexPage() {
        return "/basedata/permission/permissionIndex.ftl";
    }

    @RequestMapping("/permission/list/page")
    public String showListPage(HttpSession httpSession, ModelMap map, String parentId, String subSystemCode) {
        String unitId = getLoginInfo(httpSession).getUnitId();
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        Integer unitClass = unit.getUnitClass();
        List<Permission> permissions;
        if (StringUtils.isNotBlank(parentId)) {
            Specifications<Permission> ps = new Specifications<Permission>();
            ps.addEq("parentId", parentId).addEq("type", unitClass);
            permissions = permissionService.findAll(ps.getSpecification());
        }
        else if (StringUtils.isNotBlank(subSystemCode)) {
            Specifications<Permission> ps = new Specifications<Permission>();
            ps.addEq("subsystemCode", subSystemCode).addEq("type", unitClass);
            permissions = permissionService.findAll(ps.getSpecification());
        }
        else {
            // Specifications<Permission> ps = new Specifications<Permission>();
            // ps.addEq("type", unitClass);
            // ps.addEq("subsystemCode", "archive");
            // permissions = permissionService.findAll(ps.getSpecification());
            permissions = new ArrayList<Permission>();
        }
        map.put("permissions", permissions);
        return "/basedata/permission/permissionList.ftl";
    }

    @ResponseBody
    @RequestMapping("/permission/ztree")
    public String showZtree(HttpSession httpSession) {
        String unitId = getLoginInfo(httpSession).getUnitId();
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        Integer unitClass = unit.getUnitClass();
        List<Permission> os = permissionService.findAll(new Specifications<Permission>().addEq("type", unitClass)
                .getSpecification());

        JSONArray array = new JSONArray();
        Json json = new Json();
        json.put("pId", "");
        json.put("type", "all");
        json.put("id", "00");
        json.put("name", "所有权限");
        json.put("title", "所有权限");
        json.put("open", true); // 此节点默认展开
        json.put("url", "");
        array.add(json);

        List<SubSystem> systems = subsystemService.findAll();
        for (SubSystem system : systems) {
            json = new Json();
            json.put("pId", "00");
            json.put("permissionId", "");
            json.put("type", "system");
            json.put("id", system.getCode());
            json.put("name", system.getName());
            json.put("title", system.getName());
            json.put("item.url", system.getUrl());
            array.add(json);
        }
        for (Permission o : os) {
            json = new Json();
            String parentId = o.getParentId();
            if (StringUtils.isBlank(parentId)) {
                parentId = o.getSubsystemCode();
            }
            json.put("pId", parentId);
            json.put("type", o.getType());
            json.put("id", o.getId());
            json.put("name", o.getName());
            json.put("title", o.getName());
            json.put("item.url", o.getUrl());
            array.add(json);
        }
        return success(JSONUtils.toJSONString(array));
    }

    @RequestMapping("/permission/add/page")
    public String showAddPage(Permission permission, ModelMap map, HttpSession httpSession) {
        String unitId = getLoginInfo(httpSession).getUnitId();
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        Permission p = new Permission();
        String parentId = permission.getParentId();
        Permission parent = permissionService.findOne(parentId);
        if (parent != null) {
            p.setParentId(parentId);
            p.setSubsystemCode(parent.getSubsystemCode());
            p.setType(parent.getType());
        }
        else {
            p.setSubsystemCode(permission.getSubsystemCode());

        }
        map.put("permission", p);
        map.put("fields", ColumnInfoUtils.getEntityFiledNames(Permission.class));
        Map<String, ColumnInfoEntity> columnInfo = ColumnInfoUtils.getColumnInfos(Permission.class);
        ColumnInfoEntity columnInfoEntity = columnInfo.get("parentId");
        columnInfoEntity.setReadonly(true);
        Specifications<Permission> sp = new Specifications<Permission>().addEq("type", unit.getUnitClass());
        if (StringUtils.isBlank(parentId)) {
            columnInfoEntity.setVselect(new String[] { ":系统直属" });
        }
        else if (!parentId.matches("^[0-9A-F]{32}$")) {
            Permission p2 = permissionService.findOne(sp.addEq("extendId", parentId).getSpecification());
            if (p2 != null) {
                columnInfoEntity.setVselect(new String[] { p2.getId() + ":" + p2.getName() });
            }
        }
        else {
            List<Permission> ps = permissionService.findAll(sp.getSpecification());
            List<String> ss = new ArrayList<String>();
            for (Permission p1 : ps) {
                ss.add(p1.getId() + ":" + p1.getName());
            }
            columnInfoEntity.setVselect(ss.toArray(new String[0]));
        }
        map.put("columnInfo", columnInfo);
        return "/basedata/permission/permissionAdd.ftl";
    }

    @RequestMapping("/permission/{id}/detail/page")
    public String showDetailPage(@PathVariable String id, ModelMap map, HttpSession httpSession) {
        String unitId = getLoginInfo(httpSession).getUnitId();
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        Permission permission = permissionService.findOne(id);
        map.put("permission", permission);
        map.put("fields", ColumnInfoUtils.getEntityFiledNames(Permission.class));
        Map<String, ColumnInfoEntity> columnInfo = ColumnInfoUtils.getColumnInfos(Permission.class);
        ColumnInfoEntity columnInfoEntity = columnInfo.get("parentId");
        Specifications<Permission> sp = new Specifications<Permission>().addEq("type", unit.getUnitClass());
        String parentId = permission.getParentId();
        if (StringUtils.isBlank(parentId)) {
            columnInfoEntity.setVselect(new String[] { ":系统直属" });
        }
        else {
            List<Permission> ps = permissionService.findAll(sp.getSpecification());
            List<String> ss = new ArrayList<String>();
            for (Permission p : ps) {
                ss.add(p.getId() + ":" + p.getName());
            }
            columnInfoEntity.setVselect(ss.toArray(new String[0]));
        }

        map.put("columnInfo", columnInfo);
        return "/basedata/permission/permissionDetail.ftl";
    }

    @ResponseBody
    @RequestMapping("/permission/save")
    public String doSave(@RequestBody Permission permission) {
        permission.setId(UuidUtils.generateUuid());
        permissionService.saveAllEntitys(permission);
        return success("保存成功！");
    }

    @ResponseBody
    @RequestMapping("/permission/update")
    @ControllerInfo(value = "修改权限")
    public String doUpdate(@RequestBody Permission permission) {
        String id = permission.getId();
        Permission p = permissionService.findOne(id);
        if (p != null && p.getType() != permission.getType()) {
            Specifications<Permission> sp = new Specifications<Permission>();
            sp.addEq("parentId", id);
            long count = permissionService.count(sp.getSpecification(false));
            if (count > 0) {
                return error("存在下级权限，不能修改类型！");
            }
        }
        permissionService.saveAllEntitys(permission);
        return success("保存成功！");
    }

    @ResponseBody
    @RequestMapping("/permission/{id}/delete")
    @ControllerInfo(value = "删除权限")
    public String doDelete(@PathVariable String id) {
        Specifications<Permission> sp = new Specifications<Permission>();
        sp.addEq("parentId", id);
        long count = permissionService.count(sp.getSpecification(false));
        if (count > 0) {
            return error("存在下级权限，不能删除！");
        }
        permissionService.deleteAllByIds(id);
        return success("删除成功！");
    }
}
