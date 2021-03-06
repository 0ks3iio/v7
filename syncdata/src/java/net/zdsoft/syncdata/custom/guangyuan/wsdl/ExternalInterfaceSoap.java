
package net.zdsoft.syncdata.custom.guangyuan.wsdl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "ExternalInterfaceSoap", targetNamespace = "cq168webshare")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface ExternalInterfaceSoap {


    /**
     * 
     * @param myHeader
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "CheckLogin", action = "cq168webshare/CheckLogin")
    @WebResult(name = "CheckLoginResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "CheckLogin", targetNamespace = "cq168webshare", className = "cq168webshare.CheckLogin")
    @ResponseWrapper(localName = "CheckLoginResponse", targetNamespace = "cq168webshare", className = "cq168webshare.CheckLoginResponse")
    public String checkLogin(
        @WebParam(name = "myHeader", targetNamespace = "cq168webshare")
        MySoapHeader myHeader);

    /**
     * 
     * @param passWord
     * @param userName
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "CheckLoginUser", action = "cq168webshare/CheckLoginUser")
    @WebResult(name = "CheckLoginUserResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "CheckLoginUser", targetNamespace = "cq168webshare", className = "cq168webshare.CheckLoginUser")
    @ResponseWrapper(localName = "CheckLoginUserResponse", targetNamespace = "cq168webshare", className = "cq168webshare.CheckLoginUserResponse")
    public String checkLoginUser(
        @WebParam(name = "UserName", targetNamespace = "cq168webshare")
        String userName,
        @WebParam(name = "PassWord", targetNamespace = "cq168webshare")
        String passWord);

    /**
     * 
     * @param moduleNo
     * @param deptNo
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetAllAddData", action = "cq168webshare/GetAllAddData")
    @WebResult(name = "GetAllAddDataResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "GetAllAddData", targetNamespace = "cq168webshare", className = "cq168webshare.GetAllAddData")
    @ResponseWrapper(localName = "GetAllAddDataResponse", targetNamespace = "cq168webshare", className = "cq168webshare.GetAllAddDataResponse")
    public String getAllAddData(
        @WebParam(name = "deptNo", targetNamespace = "cq168webshare")
        String deptNo,
        @WebParam(name = "moduleNo", targetNamespace = "cq168webshare")
        String moduleNo,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param moduleNo
     * @param deptNo
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetAllUpdateData", action = "cq168webshare/GetAllUpdateData")
    @WebResult(name = "GetAllUpdateDataResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "GetAllUpdateData", targetNamespace = "cq168webshare", className = "cq168webshare.GetAllUpdateData")
    @ResponseWrapper(localName = "GetAllUpdateDataResponse", targetNamespace = "cq168webshare", className = "cq168webshare.GetAllUpdateDataResponse")
    public String getAllUpdateData(
        @WebParam(name = "deptNo", targetNamespace = "cq168webshare")
        String deptNo,
        @WebParam(name = "moduleNo", targetNamespace = "cq168webshare")
        String moduleNo,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param moduleNo
     * @param deptNo
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetAllDelData", action = "cq168webshare/GetAllDelData")
    @WebResult(name = "GetAllDelDataResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "GetAllDelData", targetNamespace = "cq168webshare", className = "cq168webshare.GetAllDelData")
    @ResponseWrapper(localName = "GetAllDelDataResponse", targetNamespace = "cq168webshare", className = "cq168webshare.GetAllDelDataResponse")
    public String getAllDelData(
        @WebParam(name = "deptNo", targetNamespace = "cq168webshare")
        String deptNo,
        @WebParam(name = "moduleNo", targetNamespace = "cq168webshare")
        String moduleNo,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param fieldName
     * @param moduleNo
     * @param fieldValue
     * @param deptNo
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetTableData", action = "cq168webshare/GetTableData")
    @WebResult(name = "GetTableDataResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "GetTableData", targetNamespace = "cq168webshare", className = "cq168webshare.GetTableData")
    @ResponseWrapper(localName = "GetTableDataResponse", targetNamespace = "cq168webshare", className = "cq168webshare.GetTableDataResponse")
    public String getTableData(
        @WebParam(name = "deptNo", targetNamespace = "cq168webshare")
        String deptNo,
        @WebParam(name = "moduleNo", targetNamespace = "cq168webshare")
        String moduleNo,
        @WebParam(name = "fieldName", targetNamespace = "cq168webshare")
        String fieldName,
        @WebParam(name = "fieldValue", targetNamespace = "cq168webshare")
        String fieldValue,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param id
     * @param deptNo
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "SynchroAddState", action = "cq168webshare/SynchroAddState")
    @WebResult(name = "SynchroAddStateResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "SynchroAddState", targetNamespace = "cq168webshare", className = "cq168webshare.SynchroAddState")
    @ResponseWrapper(localName = "SynchroAddStateResponse", targetNamespace = "cq168webshare", className = "cq168webshare.SynchroAddStateResponse")
    public String synchroAddState(
        @WebParam(name = "deptNo", targetNamespace = "cq168webshare")
        String deptNo,
        @WebParam(name = "ID", targetNamespace = "cq168webshare")
        String id,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param id
     * @param deptNo
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "SynchroUpdateState", action = "cq168webshare/SynchroUpdateState")
    @WebResult(name = "SynchroUpdateStateResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "SynchroUpdateState", targetNamespace = "cq168webshare", className = "cq168webshare.SynchroUpdateState")
    @ResponseWrapper(localName = "SynchroUpdateStateResponse", targetNamespace = "cq168webshare", className = "cq168webshare.SynchroUpdateStateResponse")
    public String synchroUpdateState(
        @WebParam(name = "deptNo", targetNamespace = "cq168webshare")
        String deptNo,
        @WebParam(name = "ID", targetNamespace = "cq168webshare")
        String id,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param id
     * @param deptNo
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "SynchroDelState", action = "cq168webshare/SynchroDelState")
    @WebResult(name = "SynchroDelStateResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "SynchroDelState", targetNamespace = "cq168webshare", className = "cq168webshare.SynchroDelState")
    @ResponseWrapper(localName = "SynchroDelStateResponse", targetNamespace = "cq168webshare", className = "cq168webshare.SynchroDelStateResponse")
    public String synchroDelState(
        @WebParam(name = "deptNo", targetNamespace = "cq168webshare")
        String deptNo,
        @WebParam(name = "ID", targetNamespace = "cq168webshare")
        String id,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param moduleNo
     * @param deptNo
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetSpecifyTable", action = "cq168webshare/GetSpecifyTable")
    @WebResult(name = "GetSpecifyTableResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "GetSpecifyTable", targetNamespace = "cq168webshare", className = "cq168webshare.GetSpecifyTable")
    @ResponseWrapper(localName = "GetSpecifyTableResponse", targetNamespace = "cq168webshare", className = "cq168webshare.GetSpecifyTableResponse")
    public String getSpecifyTable(
        @WebParam(name = "deptNo", targetNamespace = "cq168webshare")
        String deptNo,
        @WebParam(name = "moduleNo", targetNamespace = "cq168webshare")
        String moduleNo,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param moduleNo
     * @param pagesize
     * @param page
     * @param deptNo
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetSpecifyTables", action = "cq168webshare/GetSpecifyTables")
    @WebResult(name = "GetSpecifyTablesResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "GetSpecifyTables", targetNamespace = "cq168webshare", className = "cq168webshare.GetSpecifyTables")
    @ResponseWrapper(localName = "GetSpecifyTablesResponse", targetNamespace = "cq168webshare", className = "cq168webshare.GetSpecifyTablesResponse")
    public String getSpecifyTables(
        @WebParam(name = "deptNo", targetNamespace = "cq168webshare")
        String deptNo,
        @WebParam(name = "moduleNo", targetNamespace = "cq168webshare")
        String moduleNo,
        @WebParam(name = "page", targetNamespace = "cq168webshare")
        int page,
        @WebParam(name = "pagesize", targetNamespace = "cq168webshare")
        int pagesize,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param moduleNo
     * @param pagesize
     * @param isall
     * @param page
     * @param deptNo
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetSpecifyTableISALL", action = "cq168webshare/GetSpecifyTableISALL")
    @WebResult(name = "GetSpecifyTableISALLResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "GetSpecifyTableISALL", targetNamespace = "cq168webshare", className = "cq168webshare.GetSpecifyTableISALL")
    @ResponseWrapper(localName = "GetSpecifyTableISALLResponse", targetNamespace = "cq168webshare", className = "cq168webshare.GetSpecifyTableISALLResponse")
    public String getSpecifyTableISALL(
        @WebParam(name = "deptNo", targetNamespace = "cq168webshare")
        String deptNo,
        @WebParam(name = "moduleNo", targetNamespace = "cq168webshare")
        String moduleNo,
        @WebParam(name = "page", targetNamespace = "cq168webshare")
        int page,
        @WebParam(name = "pagesize", targetNamespace = "cq168webshare")
        int pagesize,
        @WebParam(name = "isall", targetNamespace = "cq168webshare")
        boolean isall,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param loginid
     * @param pwd
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetUserInfo", action = "cq168webshare/GetUserInfo")
    @WebResult(name = "GetUserInfoResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "GetUserInfo", targetNamespace = "cq168webshare", className = "cq168webshare.GetUserInfo")
    @ResponseWrapper(localName = "GetUserInfoResponse", targetNamespace = "cq168webshare", className = "cq168webshare.GetUserInfoResponse")
    public String getUserInfo(
        @WebParam(name = "loginid", targetNamespace = "cq168webshare")
        String loginid,
        @WebParam(name = "pwd", targetNamespace = "cq168webshare")
        String pwd,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param strClearText
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "EncryptString", action = "cq168webshare/EncryptString")
    @WebResult(name = "EncryptStringResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "EncryptString", targetNamespace = "cq168webshare", className = "cq168webshare.EncryptString")
    @ResponseWrapper(localName = "EncryptStringResponse", targetNamespace = "cq168webshare", className = "cq168webshare.EncryptStringResponse")
    public String encryptString(
        @WebParam(name = "strClearText", targetNamespace = "cq168webshare")
        String strClearText,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param strCipherText
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "DecryptString", action = "cq168webshare/DecryptString")
    @WebResult(name = "DecryptStringResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "DecryptString", targetNamespace = "cq168webshare", className = "cq168webshare.DecryptString")
    @ResponseWrapper(localName = "DecryptStringResponse", targetNamespace = "cq168webshare", className = "cq168webshare.DecryptStringResponse")
    public String decryptString(
        @WebParam(name = "strCipherText", targetNamespace = "cq168webshare")
        String strCipherText,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param moduleNo
     * @param type
     * @param value
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "NoticeData", action = "cq168webshare/NoticeData")
    @WebResult(name = "NoticeDataResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "NoticeData", targetNamespace = "cq168webshare", className = "cq168webshare.NoticeData")
    @ResponseWrapper(localName = "NoticeDataResponse", targetNamespace = "cq168webshare", className = "cq168webshare.NoticeDataResponse")
    public String noticeData(
        @WebParam(name = "moduleNo", targetNamespace = "cq168webshare")
        String moduleNo,
        @WebParam(name = "Value", targetNamespace = "cq168webshare")
        String value,
        @WebParam(name = "type", targetNamespace = "cq168webshare")
        int type,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param moduleNo
     * @param value
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetServerData", action = "cq168webshare/GetServerData")
    @WebResult(name = "GetServerDataResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "GetServerData", targetNamespace = "cq168webshare", className = "cq168webshare.GetServerData")
    @ResponseWrapper(localName = "GetServerDataResponse", targetNamespace = "cq168webshare", className = "cq168webshare.GetServerDataResponse")
    public String getServerData(
        @WebParam(name = "moduleNo", targetNamespace = "cq168webshare")
        String moduleNo,
        @WebParam(name = "Value", targetNamespace = "cq168webshare")
        String value,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetTableInfo", action = "cq168webshare/GetTableInfo")
    @WebResult(name = "GetTableInfoResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "GetTableInfo", targetNamespace = "cq168webshare", className = "cq168webshare.GetTableInfo")
    @ResponseWrapper(localName = "GetTableInfoResponse", targetNamespace = "cq168webshare", className = "cq168webshare.GetTableInfoResponse")
    public String getTableInfo(
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param getContentTableResult
     * @param moduleNo
     * @param pagesize
     * @param isall
     * @param page
     * @param strmeg
     * @param deptNo
     * @param token
     */
    @WebMethod(operationName = "GetContentTable", action = "cq168webshare/GetContentTable")
    @RequestWrapper(localName = "GetContentTable", targetNamespace = "cq168webshare", className = "cq168webshare.GetContentTable")
    @ResponseWrapper(localName = "GetContentTableResponse", targetNamespace = "cq168webshare", className = "cq168webshare.GetContentTableResponse")
    public void getContentTable(
        @WebParam(name = "deptNo", targetNamespace = "cq168webshare")
        String deptNo,
        @WebParam(name = "moduleNo", targetNamespace = "cq168webshare")
        String moduleNo,
        @WebParam(name = "page", targetNamespace = "cq168webshare")
        int page,
        @WebParam(name = "pagesize", targetNamespace = "cq168webshare")
        int pagesize,
        @WebParam(name = "isall", targetNamespace = "cq168webshare")
        boolean isall,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token,
        @WebParam(name = "GetContentTableResult", targetNamespace = "cq168webshare", mode = WebParam.Mode.OUT)
        Holder<net.zdsoft.syncdata.custom.guangyuan.wsdl.GetContentTableResponse.GetContentTableResult> getContentTableResult,
        @WebParam(name = "strmeg", targetNamespace = "cq168webshare", mode = WebParam.Mode.OUT)
        Holder<String> strmeg);

    /**
     * 
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetBasicsTable", action = "cq168webshare/GetBasicsTable")
    @WebResult(name = "GetBasicsTableResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "GetBasicsTable", targetNamespace = "cq168webshare", className = "cq168webshare.GetBasicsTable")
    @ResponseWrapper(localName = "GetBasicsTableResponse", targetNamespace = "cq168webshare", className = "cq168webshare.GetBasicsTableResponse")
    public String getBasicsTable(
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param tablename
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetBasicsTableField", action = "cq168webshare/GetBasicsTableField")
    @WebResult(name = "GetBasicsTableFieldResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "GetBasicsTableField", targetNamespace = "cq168webshare", className = "cq168webshare.GetBasicsTableField")
    @ResponseWrapper(localName = "GetBasicsTableFieldResponse", targetNamespace = "cq168webshare", className = "cq168webshare.GetBasicsTableFieldResponse")
    public String getBasicsTableField(
        @WebParam(name = "tablename", targetNamespace = "cq168webshare")
        String tablename,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param pagesize
     * @param page
     * @param tablename
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetBasicsData", action = "cq168webshare/GetBasicsData")
    @WebResult(name = "GetBasicsDataResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "GetBasicsData", targetNamespace = "cq168webshare", className = "cq168webshare.GetBasicsData")
    @ResponseWrapper(localName = "GetBasicsDataResponse", targetNamespace = "cq168webshare", className = "cq168webshare.GetBasicsDataResponse")
    public String getBasicsData(
        @WebParam(name = "page", targetNamespace = "cq168webshare")
        int page,
        @WebParam(name = "pagesize", targetNamespace = "cq168webshare")
        int pagesize,
        @WebParam(name = "tablename", targetNamespace = "cq168webshare")
        String tablename,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param json
     * @param type
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "SynchroUnit", action = "cq168webshare/SynchroUnit")
    @WebResult(name = "SynchroUnitResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "SynchroUnit", targetNamespace = "cq168webshare", className = "cq168webshare.SynchroUnit")
    @ResponseWrapper(localName = "SynchroUnitResponse", targetNamespace = "cq168webshare", className = "cq168webshare.SynchroUnitResponse")
    public String synchroUnit(
        @WebParam(name = "json", targetNamespace = "cq168webshare")
        String json,
        @WebParam(name = "type", targetNamespace = "cq168webshare")
        String type,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

    /**
     * 
     * @param json
     * @param type
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "SynchroUser", action = "cq168webshare/SynchroUser")
    @WebResult(name = "SynchroUserResult", targetNamespace = "cq168webshare")
    @RequestWrapper(localName = "SynchroUser", targetNamespace = "cq168webshare", className = "cq168webshare.SynchroUser")
    @ResponseWrapper(localName = "SynchroUserResponse", targetNamespace = "cq168webshare", className = "cq168webshare.SynchroUserResponse")
    public String synchroUser(
        @WebParam(name = "json", targetNamespace = "cq168webshare")
        String json,
        @WebParam(name = "type", targetNamespace = "cq168webshare")
        String type,
        @WebParam(name = "Token", targetNamespace = "cq168webshare")
        String token);

}
