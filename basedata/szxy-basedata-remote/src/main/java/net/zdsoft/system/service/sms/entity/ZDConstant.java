package net.zdsoft.system.service.sms.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ZDConstant implements Serializable {
    private static final long serialVersionUID = 3238516359422242423L;
    public static final String DEFAULT_USER_ID = "00000000000000000000000000000000";
    public static final String SMS_RECEIVE_PARAMETER_NAME = "jsonStr";
    public static final String SMS_RECEIVE_RESULT_PARAMETER_NAME = "batchId";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
    /**
     * 短信平台发送地址
     */
    public static final String SMS_SERVER_URL_POSTFIX = "/smsplatform/smsclient/receivesms.action";
    /**
     * 短信平台接收地址
     */
    public static final String SMS_RECEIVE_RESULT_POSTFIX = "/smsplatform/smsclient/sendresult.action";

    public static final String JSON_RESULT_DESCRIPTION = "description";
    public static final String JSON_RESULT_BATCHID = "batchId";
    public static final String JSON_RESULT_RECEIVESTATE = "receive_state";
    public static final String JSON_RESULT_CODE = "code";
    public static final String JSON_RESULT_CODE_00 = "00";
    public static final String JSON_RESULT_CODE_01 = "01";
    public static final String JSON_RESULT_CODE_02 = "02";
    public static final String JSON_RESULT_CODE_03 = "03";
    public static final String JSON_RESULT_CODE_04 = "04";
    public static final String JSON_RESULT_CODE_05 = "05";
    public static final String JSON_RESULT_CODE_06 = "06";
    public static final String JSON_RESULT_CODE_07 = "07";
    public static final String JSON_RESULT_CODE_08 = "08";
    public static final String JSON_RESULT_CODE_09 = "09";
    public static final String JSON_RESULT_CODE_10 = "10";
    public static final String JSON_RESULT_CODE_99 = "99";
    public static final Map<String, String> resultDescriptionMap = new HashMap<String, String>();
    static {
        resultDescriptionMap.put(JSON_RESULT_CODE_00, "短信服务器接收成功！");
        resultDescriptionMap.put(JSON_RESULT_CODE_01, "数据安全性验证失败，可能被修改过！");
        resultDescriptionMap.put(JSON_RESULT_CODE_02, "账户校验失败！");
        resultDescriptionMap.put(JSON_RESULT_CODE_03, "取不到有效的短信信息！");
        resultDescriptionMap.put(JSON_RESULT_CODE_04, "取不到有效的接收人信息！");
        resultDescriptionMap.put(JSON_RESULT_CODE_05, "取不到有效的发送人信息！");
        resultDescriptionMap.put(JSON_RESULT_CODE_06, "短信不是从有效的服务器名单中发送！");
        resultDescriptionMap.put(JSON_RESULT_CODE_07, "服务器信息维护不准确，请联系短信平台管理员！");
        resultDescriptionMap.put(JSON_RESULT_CODE_08, "存在不合法字符！");
        resultDescriptionMap.put(JSON_RESULT_CODE_09, "接收人号码不符合过滤规则！");
        resultDescriptionMap.put(JSON_RESULT_CODE_10, "接收人的号码，号码段未知！");
        resultDescriptionMap.put(JSON_RESULT_CODE_99, "短信服务器发生异常，请联系管理员！");
    }

    /**
     * json节点
     */
    public static final String JSON_ELEMENT_AUTHORIZE = "authorize";
    public static final String JSON_ELEMENT_SMSDETAIL = "smsDetail";
    public static final String JSON_ELEMENT_SENDUSER = "sendUser";
    public static final String JSON_ELEMENT_RECEIVEUSER = "receiveUser";

    /**
     * json字段
     */
    public static final String JSON_OBJECT_TICKET = "ticket";
    public static final String JSON_OBJECT_SECURITYCODE = "securityCode";
    public static final String JSON_OBJECT_LOGINNAME = "loginName";
    public static final String JSON_OBJECT_PASSWORD = "password";
    public static final String JSON_OBJECT_MSG = "msg";
    public static final String JSON_OBJECT_ISSYNC = "isSync";
    public static final String JSON_OBJECT_SENDTIME = "sendTime";
    public static final String JSON_OBJECT_BATCHID = "batchId";
    public static final String JSON_OBJECT_SENDUSERID = "sendUserId";
    public static final String JSON_OBJECT_SENDUSERNAME = "sendUsername";
    public static final String JSON_OBJECT_SENDUNITID = "sendUnitId";
    public static final String JSON_OBJECT_SENDUNITNAME = "sendUnitName";
    public static final String JSON_OBJECT_RECEIVEUSERID = "receiveUserId";
    public static final String JSON_OBJECT_RECEIVEUSERNAME = "receiveUsername";
    public static final String JSON_OBJECT_RECEIVEUNITID = "receiveUnitId";
    public static final String JSON_OBJECT_RECEIVEUNITNAME = "receiveUnitName";
    public static final String JSON_OBJECT_PHONE = "phone";

    public static final int SMS_STATE_WAIT_TO_GATE = 1; // 等待发送
    public static final int SMS_STATE_HAVE_TO_GATE = 2; // 已经到网关
    public static final int SMS_STATE_SEND_FAILD = 3; // 发送失败
    public static final int SMS_STATE_HAVE_RECEIVED = 4; // 对方手机已经接收
    public static final int SMS_STATE_HAVE_OVERTIME = 5; // 已经过期（超过24小时还未发送）

    /**
     * 单位短信计费类型
     */
    public static final int FEE_TYPE_TRY = 0; //试用   
    public static final int FEE_TYPE_FREE = 1; //免费
    public static final int FEE_TYPE_PAY = 2; //收费
    
    public static final String SMSSERVER_CONNECT_ERROR_MSG = "通讯服务器连接不上，请重新注册新的帐户与验证码或与万朋网络联系！";
}
