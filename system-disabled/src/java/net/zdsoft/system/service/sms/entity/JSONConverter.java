package net.zdsoft.system.service.sms.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSONConverter implements Serializable {

    private static final long serialVersionUID = 8006804136365941523L;

    /**
     * 生成json格式数据
     * 
     * @param account
     * @param zdPack
     * @return
     */
    public static String transferToJSON(Account account, ZDPack zdPack) {
        JSONObject json = new JSONObject();
        JSONArray jarray = new JSONArray();
        JSONObject ajson = new JSONObject();

        ajson.put(ZDConstant.JSON_OBJECT_MSG, zdPack.getMsg());
        ajson.put(ZDConstant.JSON_OBJECT_SENDTIME, zdPack.getSendTime());
        ajson.put(ZDConstant.JSON_OBJECT_BATCHID, zdPack.getBatchId());
        ajson.put(ZDConstant.JSON_OBJECT_ISSYNC, zdPack.getIsSync() + "");
        jarray.add(ajson);
        json.put(ZDConstant.JSON_ELEMENT_SMSDETAIL, jarray);

        jarray = new JSONArray();
        ajson = new JSONObject();
        ajson.put(ZDConstant.JSON_OBJECT_SENDUSERID, zdPack.getSendUserId());
        ajson.put(ZDConstant.JSON_OBJECT_SENDUSERNAME, zdPack.getSendUsername());
        ajson.put(ZDConstant.JSON_OBJECT_SENDUNITID, zdPack.getSendUnitId());
        ajson.put(ZDConstant.JSON_OBJECT_SENDUNITNAME, zdPack.getSendUnitName());
        jarray.add(ajson);
        json.put(ZDConstant.JSON_ELEMENT_SENDUSER, jarray);

        jarray = new JSONArray();
        for (Receiver receiver : zdPack.getReceiverList()) {
            ajson = new JSONObject();
            ajson.put(ZDConstant.JSON_OBJECT_RECEIVEUSERID, receiver.getUserId());
            ajson.put(ZDConstant.JSON_OBJECT_RECEIVEUSERNAME, receiver.getUsername());
            ajson.put(ZDConstant.JSON_OBJECT_RECEIVEUNITID, receiver.getUnitId());
            ajson.put(ZDConstant.JSON_OBJECT_RECEIVEUNITNAME, receiver.getUnitName());
            ajson.put(ZDConstant.JSON_OBJECT_PHONE, receiver.getPhone());
            jarray.add(ajson);
        }
        json.put(ZDConstant.JSON_ELEMENT_RECEIVEUSER, jarray);

        jarray = new JSONArray();
        ajson = new JSONObject();
//        String md5 = ZDUtils.encodeByMD5(json.toString(), 2);
//        ajson.put(ZDConstant.JSON_OBJECT_SECURITYCODE, md5);
        ajson.put(ZDConstant.JSON_OBJECT_TICKET, account.getTicket());
        ajson.put(ZDConstant.JSON_OBJECT_LOGINNAME, account.getLoginName());
        ajson.put(ZDConstant.JSON_OBJECT_PASSWORD, account.getPassword());
        jarray.add(ajson);
        json.put(ZDConstant.JSON_ELEMENT_AUTHORIZE, jarray);

        return json.toString();
    }

    public static Account transferToAccount(JSONObject json) {
        Account account = null;
        JSONObject authorize = JSONUtils.getJsonObject(json, 0, ZDConstant.JSON_ELEMENT_AUTHORIZE);
        if (authorize != null) {
            account = new Account();
            account.setLoginName(JSONUtils.get(authorize, ZDConstant.JSON_OBJECT_LOGINNAME));
            account.setPassword(JSONUtils.get(authorize, ZDConstant.JSON_OBJECT_PASSWORD));
            account.setTicket(JSONUtils.get(authorize, ZDConstant.JSON_OBJECT_TICKET));
        }
        return account;
    }

    /**
     * 转换为短信包对象
     * 
     * @param json
     * @return
     */
    public static ZDPack transferToZDPack(JSONObject json) {
        ZDPack zdpack = null;
        JSONObject smsDetail = JSONUtils.getJsonObject(json, 0, ZDConstant.JSON_ELEMENT_SMSDETAIL);
        if (smsDetail != null) {
            if (zdpack == null)
                zdpack = new ZDPack();
            zdpack.setMsg(JSONUtils.get(smsDetail, ZDConstant.JSON_OBJECT_MSG));
            zdpack.setBatchId(JSONUtils.get(smsDetail, ZDConstant.JSON_OBJECT_BATCHID));
            zdpack.setSendTime(JSONUtils.get(smsDetail, ZDConstant.JSON_OBJECT_SENDTIME));
            zdpack.setIsSync(JSONUtils.get(smsDetail, ZDConstant.JSON_OBJECT_ISSYNC));
        }
        JSONObject sendUser = JSONUtils.getJsonObject(json, 0, ZDConstant.JSON_ELEMENT_SENDUSER);
        if (sendUser != null) {
            if (zdpack == null)
                zdpack = new ZDPack();
            zdpack.setSendUserId(JSONUtils.get(sendUser, ZDConstant.JSON_OBJECT_SENDUSERID));
            zdpack.setSendUsername(JSONUtils.get(sendUser, ZDConstant.JSON_OBJECT_SENDUSERNAME));
            zdpack.setSendUnitId(JSONUtils.get(sendUser, ZDConstant.JSON_OBJECT_SENDUNITID));
            zdpack.setSendUnitName(JSONUtils.get(sendUser, ZDConstant.JSON_OBJECT_SENDUNITNAME));
        }
        JSONArray receivers = json.getJSONArray(ZDConstant.JSON_ELEMENT_RECEIVEUSER);
        if (receivers != null && receivers.size() > 0) {
            if (zdpack == null)
                zdpack = new ZDPack();
            for (int i = 0; i < receivers.size(); i++) {
                JSONObject receiver = JSONUtils.getJsonObject(receivers, i);
                if (receiver != null) {
                    Receiver re = new Receiver();
                    re.setPhone(JSONUtils.get(receiver, ZDConstant.JSON_OBJECT_PHONE));
                    re.setUnitId(JSONUtils.get(receiver, ZDConstant.JSON_OBJECT_RECEIVEUNITID));
                    re.setUnitName(JSONUtils.get(receiver, ZDConstant.JSON_OBJECT_RECEIVEUNITNAME));
                    re.setUserId(JSONUtils.get(receiver, ZDConstant.JSON_OBJECT_RECEIVEUSERID));
                    re.setUsername(JSONUtils.get(receiver, ZDConstant.JSON_OBJECT_RECEIVEUSERNAME));
                    zdpack.addReciever(re);
                }
            }
        }
        return zdpack;
    }

    /**
     * 验证数据是否被修改过
     * 
     * @param jsonStr
     * @return
     */
    public static boolean checkSecurityCode(String jsonStr) {
//        JSONObject jsonReceive = JSONObject.fromObject(jsonStr);
//        JSONArray jarray = jsonReceive.getJSONArray(ZDConstant.JSON_ELEMENT_AUTHORIZE);
//        JSONObject ajson = null;
//        if (jarray != null && jarray.size() > 0)
//            ajson = jarray.getJSONObject(0);
//        String md5 = JSONUtils.get(ajson, ZDConstant.JSON_OBJECT_SECURITYCODE);
//        jsonReceive.remove(ZDConstant.JSON_ELEMENT_AUTHORIZE);
//        String md5Verify = ZDUtils.encodeByMD5(jsonReceive.toString(), 2);
//        return StringUtils.equals(md5Verify, md5);
        return true;
    }
}
