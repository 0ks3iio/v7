/*
 * @(#)AddressBookAction.java    Created on 2017年3月1日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.action.weike;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.dto.GroupItemsDto;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Group;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.GroupItemsRemoteService;
import net.zdsoft.basedata.remote.service.GroupRemoteService;
import net.zdsoft.basedata.remote.service.TeacherDutyRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.utils.RemoteCallUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.remote.openapi.dto.UserDto;
import net.zdsoft.system.entity.mcode.McodeDetail;

/**
 * 通讯录中调用的接口
 *
 * @author yeqi
 * @version $Revision: 1.0 $, $Date: 2017年3月1日 下午1:46:24 $
 */
@Controller
@RequestMapping(value = { "/remote/openapi", "/openapi" })
public class AddressBookAction {

    protected static final Logger logger = LoggerFactory.getLogger(AddressBookAction.class);

    @Autowired
    private DeptRemoteService deptRemoteService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private GroupRemoteService groupRemoteService;
    @Autowired
    private GroupItemsRemoteService groupItemsRemoteService;
    @Autowired
    private TeacherDutyRemoteService teacherDutyRemoteService;
    @Autowired
    private UnitRemoteService unitRemoteService;

    /**
     * 单位部门列表
     *
     * @param remoteParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listDept")
    public String queryUnitDept(String remoteParam) {
        String unitId = this.getParamValue(remoteParam, "unitId");
        if (Validators.isEmpty(unitId)) {
            return encodeErrorMsg("缺少参数信息");
        }
        try {
            String result = deptRemoteService.findByUnitId(unitId);
            List<Dept> resultList = SUtils.dt(result, new TypeReference<List<Dept>>() {
            });
            return encodeSuccessResultList(resultList);
        }
        catch (Exception ex) {
            return encodeErrorMsg("调用远程接口失败");
        }
    }

    /**
     * 部门用户列表
     *
     * @param remoteParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listDeptUser")
    public String queryDeptUser(String remoteParam) {
        String deptId = this.getParamValue(remoteParam, "deptId");
        String leader = this.getParamValue(remoteParam, "leader");
        if (Validators.isEmpty(deptId)) {
            return encodeErrorMsg("缺少参数信息");
        }
        try {
            String result = userRemoteService.findByDeptId(deptId, leader);
            List<UserDto> userList = SUtils.dt(result, new TypeReference<List<UserDto>>() {
            });
            return encodeSuccessResultList(userList);
        }
        catch (Exception ex) {
            return encodeErrorMsg("调用远程接口失败");
        }
    }

    /**
     * 组类型下的组列表
     *
     * @param remoteParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listGroup")
    public String queryGroup(String remoteParam) {
        String unitId = this.getParamValue(remoteParam, "unitId");
        String types = this.getParamValue(remoteParam, "types");
        String userId = this.getParamValue(remoteParam, "userId");
        if (Validators.isEmpty(unitId) || Validators.isEmpty(types) || Validators.isEmpty(userId)) {
            return encodeErrorMsg("缺少参数信息");
        }
        String[] strArr = types.split(","); // 然后使用split方法将字符串拆解到字符串数组中
        Integer[] intArr = new Integer[strArr.length]; // 定义一个长度与上述的字符串数组长度相通的整型数组
        for (int a = 0; a < strArr.length; a++) {
            intArr[a] = Integer.valueOf(strArr[a]); // 然后遍历字符串数组，使用包装类Integer的valueOf方法将字符串转为整型
        }
        try {
            String result = groupRemoteService.findByUnitIdAndType(unitId, intArr, userId);
            List<Group> resultList = SUtils.dt(result, new TypeReference<List<Group>>() {
            });
            return encodeSuccessResultList(resultList);
        }
        catch (Exception ex) {
            return encodeErrorMsg("调用远程接口失败");
        }
    }

    /**
     * 组成员列表
     *
     * @param remoteParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listGroupItems")
    public String queryGroupItems(String remoteParam) {
        String groupId = this.getParamValue(remoteParam, "groupId");
        String typestr = this.getParamValue(remoteParam, "type");
        if (Validators.isEmpty(groupId) || Validators.isEmpty(typestr)) {
            return encodeErrorMsg("缺少参数信息");
        }
        Integer type = Integer.parseInt(typestr);
        try {
            String result = groupItemsRemoteService.findByGroupIdAndType(groupId, type);
            List<GroupItemsDto> resultList = SUtils.dt(result, new TypeReference<List<GroupItemsDto>>() {
            });
            return encodeSuccessResultList(resultList);
        }
        catch (Exception ex) {
            return encodeErrorMsg("调用远程接口失败");
        }
    }

    /**
     * 单位职务列表
     *
     * @param remoteParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listUnitDuty")
    public String queryUnitDuty(String remoteParam) {
        String unitId = this.getParamValue(remoteParam, "unitId");
        if (Validators.isEmpty(unitId)) {
            return encodeErrorMsg("缺少参数信息");
        }
        try {
            String result = teacherDutyRemoteService.findDutysByUnitId(unitId);
            List<McodeDetail> resultList = SUtils.dt(result, new TypeReference<List<McodeDetail>>() {
            });
            return encodeSuccessResultList(resultList);
        }
        catch (Exception ex) {
            return encodeErrorMsg("调用远程接口失败");
        }
    }

    /**
     * 职务成员列表
     *
     * @param remoteParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listDutyUser")
    public String queryDutyUser(String remoteParam) {
        String unitId = this.getParamValue(remoteParam, "unitId");
        String dutyCode = this.getParamValue(remoteParam, "dutyCode");
        if (Validators.isEmpty(unitId) || Validators.isEmpty(dutyCode)) {
            return encodeErrorMsg("缺少参数信息");
        }
        try {
            String result = teacherDutyRemoteService.findDutysByUnitIdAndDuty(unitId, dutyCode);
            List<UserDto> resultList = SUtils.dt(result, new TypeReference<List<UserDto>>() {
            });
            return encodeSuccessResultList(resultList);
        }
        catch (Exception ex) {
            return encodeErrorMsg("调用远程接口失败");
        }
    }

    /**
     * 查看下级单位（不传上级id则显示顶级）
     *
     * @param remoteParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listUnits")
    public String queryUnit(String remoteParam) {
        String unitId = this.getParamValue(remoteParam, "unitId");
        if (Validators.isEmpty(unitId)) {
            unitId = "00000000000000000000000000000000";
        }
        try {
            String result = unitRemoteService.findByParentId(unitId);
            List<Unit> resultList = SUtils.dt(result, new TypeReference<List<Unit>>() {
            });
            return encodeSuccessResultList(resultList);
        }
        catch (Exception ex) {
            return encodeErrorMsg("调用远程接口失败");
        }
    }

    /**
     * 教师查询
     * 
     * @param remoteParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listTeacher")
    public String queryTeacher(String remoteParam) {
        String realName = this.getParamValue(remoteParam, "realName");
        String pageIndex = this.getParamValue(remoteParam, "pageIndex");
        if (Validators.isEmpty(realName) || Validators.isEmpty(pageIndex)) {
            return encodeErrorMsg("缺少参数信息");
        }
        try {
            String result = userRemoteService.findByRealName(realName, pageIndex);
            List<UserDto> resultList = SUtils.dt(result, new TypeReference<List<UserDto>>() {
            });
            return encodeSuccessResultList(resultList);
        }
        catch (Exception ex) {
            return encodeErrorMsg("调用远程接口失败");
        }
    }

    private String getParamValue(String remoteParam, String name) {
        if (getJsonParam(remoteParam).containsKey(name)) {
            return getJsonParam(remoteParam).getString(name);
        }
        else {
            return "";
        }
    }

    private JSONObject getJsonParam(String remoteParam) {
        JSONObject jsonv = getJson(remoteParam);
        JSONObject jsonParam = null;
        if (jsonv.containsKey(RemoteCallUtils.JSON_PARAM)) {
            jsonParam = jsonv.getJSONObject(RemoteCallUtils.JSON_PARAM);
        }
        else {
            jsonParam = new JSONObject();
        }
        return jsonParam;
    }

    /**
     * 取得返回的原始参数
     *
     * @return
     */
    public JSONObject getJson(String remoteParam) {
        String param = RemoteCallUtils.decode(remoteParam);
        JSONObject json = null;
        if (StringUtils.isBlank(param)) {
            return new JSONObject();
        }
        try {
            json = JSONObject.parseObject(param);
            return json;
        }
        catch (Exception e) {
            return new JSONObject();
        }
    }

    private String encodeErrorMsg(String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug("error before encode:" + SUtils.s(RemoteCallUtils.convertError(msg)));
        }
        return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError(msg)));
    }

    private String encodeSuccessResultList(Object os) {
        if (logger.isDebugEnabled()) {
            logger.debug("before encode:" + SUtils.s(RemoteCallUtils.convertJsons(os)));
        }
        return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertJsons(os)));
    }

}
