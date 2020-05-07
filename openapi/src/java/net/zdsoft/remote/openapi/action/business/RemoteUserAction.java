/*
 * @(#)UserAction.java    Created on 2017年3月28日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.action.business;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.utils.RemoteCallUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.Validators;

/**
 * 用户信息修改
 *
 * @author yeqi
 * @version $Revision: 1.0 $, $Date: 2017年3月28日 下午3:57:23 $
 */
@Controller
@RequestMapping(value = { "/remote/openapi", "/openapi" })
public class RemoteUserAction {

    private static final String JSON_PARAM = "param";

    private static final String RESPONSE_CODE = "code";
    private static final String RESPONSE_MSG = "msg";
    private static final String CODE_SUCCESS = "00";// 修改成功
    private static final String CODE_NO_ACCOUNT = "01";// accountId对应的数字校园用户不存在
    private static final String CODE_OTHER_FAILURE = "02";// accountId对应的数字校园用户不存在

    @Autowired
    private UserRemoteService userRemoteService;

    /**
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/user/updateUserInfo")
    public String updateUserInfo(String param) {
        Map<String, String> valueMap = getParamValueMap(param, new String[] { "accountId", "password", "phone",
                "avatarUrl" });
        String accountId = valueMap.get("accountId");
        String password = valueMap.get("password");// 更改密码
        String phone = valueMap.get("phone");// 更改手机号
        String avatarUrl = valueMap.get("avatarUrl");// 更改头像
        if (Validators.isEmpty(accountId)) {
            return SUtils.s(result(CODE_NO_ACCOUNT));
        }
        else {
            User user = SUtils.dc(userRemoteService.findByAccountId(accountId), User.class);
            if (user != null) {
                if (!Validators.isEmpty(password)) {
                    try {
                        userRemoteService.updatePasswordByAccountId(password, new Date(), accountId);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        return SUtils.s(result(CODE_OTHER_FAILURE));
                    }
                }
                else if (!Validators.isEmpty(phone)) {
                    try {
                        userRemoteService.updatePhoneById(user.getId(), phone, new Date(), user.getOwnerId(),
                                user.getOwnerType(), accountId);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        return SUtils.s(result(CODE_OTHER_FAILURE));
                    }
                }
                else if (!Validators.isEmpty(avatarUrl)) {
                    userRemoteService.updateAvatarUrlById(avatarUrl, new Date(), user.getId());
                }
            }
        }
        return SUtils.s(result(CODE_SUCCESS));
    }

    private Map<String, String> getParamValueMap(String param, String[] names) {
        Map<String, String> map = new HashMap<String, String>();
        if (names != null) {
            JSONObject object = getJson(param);
            for (String name : names) {
                if (object.containsKey(name)) {
                    map.put(name, object.getString(name));
                }
            }
        }
        return map;
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

    private JSONObject result(String code) {
        JSONObject jsonj = new JSONObject();
        jsonj.put(RESPONSE_CODE, code);
        return jsonj;
    }

}
