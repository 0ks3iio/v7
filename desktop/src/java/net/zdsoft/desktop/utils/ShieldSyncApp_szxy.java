package net.zdsoft.desktop.utils;

import com.iflytek.fsp.shield.java.sdk.constant.HttpConstant;
import com.iflytek.fsp.shield.java.sdk.constant.SdkConstant;
import com.iflytek.fsp.shield.java.sdk.enums.Method;
import com.iflytek.fsp.shield.java.sdk.enums.ParamPosition;
import com.iflytek.fsp.shield.java.sdk.http.ApiClient;
import com.iflytek.fsp.shield.java.sdk.http.BaseApp;
import com.iflytek.fsp.shield.java.sdk.model.ApiRequest;
import com.iflytek.fsp.shield.java.sdk.model.ApiResponse;
import com.iflytek.fsp.shield.java.sdk.model.ApiSignStrategy;
import com.iflytek.fsp.shield.java.sdk.model.ResultInfo;

public class ShieldSyncApp_szxy extends BaseApp {

    public ShieldSyncApp_szxy() {
        this.apiClient = new ApiClient();
        this.apiClient.init();
        this.appId = "d33dcde2d83f4bd28be484a281ed6c41";
        this.appSecret = "a7621c88e2466d104517a834bb4467f4";
        this.host = "openapi.ahjygl.gov.cn";
        this.httpPort = 80;
        this.httpsPort = 443;
        this.stage = "RELEASE";
        this.equipmentNo = "XXX";
        this.signStrategyUrl = "/getSignStrategy";
        this.tokenUrl = "/getTokenUrl";
        this.publicKey = "305C300D06092A864886F70D0101010500034B00304802410087BCCE08487F2AD237FD7C2B2A5E6310598EB8CB4FCEBF591A38714D7A91D97C98CE1BA002A9ECB75B4A27850918C823B8C9F800272A4A95A45134CA2E79E9EF0203010001";
//        this.icloudlockEnabled = false;//关闭云锁验证
    }

  /**
    * 初始化，获取服务签名策略
    */
    private void initSignStrategy(ApiRequest apiRequest){
        //获取服务安全策略信息
        ApiSignStrategy signStrategy = super.getSignStrategy(apiRequest.getPath());
        //判断是否需要token校验
        if(null!=signStrategy && "token".equals(signStrategy.getSignType())){
            //从本地缓存获取token信息,如果本地缓存存在token信息，验证本地缓存token的有效次数，
            //1.如果验证通过，token次数-1，回写到本地缓存；
            //2.如果验证不通过，从新获取token信息，并写到本地缓存。

            //从token服务获取token信息
            ResultInfo resultInfo = super.getTokenInfo(signStrategy);
            if(null!=resultInfo && SdkConstant.SUCCESS.equals(resultInfo.getCode())) {
                apiRequest.setTokenValue(resultInfo.getData().getTokenValue());
                //apiRequest.getHeaders().put(SdkConstant.AUTH_EQUIPMENTNO,equipmentNo);
            }else{
                System.err.println("获取token信息失败");
            }
        }
    }

  
    /**
    * Version:201901091138075778
    */
    public ApiResponse listOrgClassBySchool(String schoolId, String param) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listOrgClassBySchool", SdkConstant.AUTH_TYPE_DEFAULT, "cd89ff99e0d542d8ba117b1b66b66526");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("schoolId", schoolId, ParamPosition.FORM, true);
        
        apiRequest.addParam("param", param, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201901292116012336
    */
    public ApiResponse pageBook(String bookQuery, String res_key, String res_secret) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/pageBook", SdkConstant.AUTH_TYPE_DEFAULT, "f38cef92ee3b427ab21337bddb627364");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("bookQuery", bookQuery, ParamPosition.FORM, false);
        
        apiRequest.addParam("res_key", res_key, ParamPosition.FORM, true);
        
        apiRequest.addParam("res_secret", res_secret, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201811082301279621
    */
    public ApiResponse listOrgClassByTeacher(String teacherId, String teachingCycleId, String param) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listOrgClassByTeacher", SdkConstant.AUTH_TYPE_DEFAULT, "cd89ff99e0d542d8ba117b1b66b66526");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("teacherId", teacherId, ParamPosition.FORM, false);
        
        apiRequest.addParam("teachingCycleId", teachingCycleId, ParamPosition.FORM, false);
        
        apiRequest.addParam("param", param, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201812120217508783
    */
    public ApiResponse getOrgerById(String userId) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/getOrgerById", SdkConstant.AUTH_TYPE_DEFAULT, "ed8bfb091b9749729626d5d1f855a821");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("userId", userId, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201812120216094779
    */
    public ApiResponse getSchool(String schoolId) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/getSchool", SdkConstant.AUTH_TYPE_DEFAULT, "6656e651811c40daa377fc3997af5ac9");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("schoolId", schoolId, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201811161700349392
    */
    public ApiResponse listOrgClassByStudent(String studentId, String param) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listOrgClassByStudent", SdkConstant.AUTH_TYPE_DEFAULT, "cd89ff99e0d542d8ba117b1b66b66526");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("studentId", studentId, ParamPosition.FORM, false);
        
        apiRequest.addParam("param", param, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201811082343576455
    */
    public ApiResponse listAllPhaseInSchool(String schoolId) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listAllPhaseInSchool", SdkConstant.AUTH_TYPE_DEFAULT, "6656e651811c40daa377fc3997af5ac9");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("schoolId", schoolId, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201903282354325788
    */
    public ApiResponse listTermByAcademicYear(String schoolId, String academicYearId, String param) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listTermByAcademicYear", SdkConstant.AUTH_TYPE_DEFAULT, "994d12e576f84c56a6b0d300e44e1fa5");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("schoolId", schoolId, ParamPosition.FORM, false);
        
        apiRequest.addParam("academicYearId", academicYearId, ParamPosition.FORM, false);
        
        apiRequest.addParam("param", param, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201812120122299333
    */
    public ApiResponse getOrgStudentById(String userId) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/getOrgStudentById", SdkConstant.AUTH_TYPE_DEFAULT, "f467f4d6806843b48ecc9394217ff110");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("userId", userId, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201906011118562095
    */
    public ApiResponse treeType() {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/treeType", SdkConstant.AUTH_TYPE_DEFAULT, "f38cef92ee3b427ab21337bddb627364");
        //initSignStrategy(apiRequest);
        
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201901071006488596
    */
    public ApiResponse listAllPhaseGradeInSchool(String schoolId) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listAllPhaseGradeInSchool", SdkConstant.AUTH_TYPE_DEFAULT, "6656e651811c40daa377fc3997af5ac9");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("schoolId", schoolId, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201902161537181910
    */
    public ApiResponse getCurrentTeachingCycleInSchool1(String schoolId) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/getCurrentTeachingCycleInSchool1", SdkConstant.AUTH_TYPE_DEFAULT, "f055e0a5d63e4bfc98463d34ea8ec8bb");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("schoolId", schoolId, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201811082304163601
    */
    public ApiResponse listUserOrgClassSubjectByUserId(String schoolId, String userId, String param) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listUserOrgClassSubjectByUserId", SdkConstant.AUTH_TYPE_DEFAULT, "5eca0ae6176d451c8491cee5768f3e1f");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("schoolId", schoolId, ParamPosition.FORM, false);
        
        apiRequest.addParam("userId", userId, ParamPosition.FORM, false);
        
        apiRequest.addParam("param", param, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201903291541086151
    */
    public ApiResponse getEdition(String code, String res_key, String res_secret) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/getEdition", SdkConstant.AUTH_TYPE_DEFAULT, "f38cef92ee3b427ab21337bddb627364");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("code", code, ParamPosition.FORM, false);
        
        apiRequest.addParam("res_key", res_key, ParamPosition.FORM, true);
        
        apiRequest.addParam("res_secret", res_secret, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201812120124428049
    */
    public ApiResponse getClassBaseById(String id) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/getClassBaseById", SdkConstant.AUTH_TYPE_DEFAULT, "cd89ff99e0d542d8ba117b1b66b66526");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("id", id, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201901292118421746
    */
    public ApiResponse getPreviewUrl(String resId, String userId, String res_key, String res_secret) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/getPreviewUrl", SdkConstant.AUTH_TYPE_DEFAULT, "ce57ad10b26240428288d15cd88e19f3");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("resId", resId, ParamPosition.FORM, true);
        
        apiRequest.addParam("userId", userId, ParamPosition.FORM, true);
        
        apiRequest.addParam("res_key", res_key, ParamPosition.FORM, true);
        
        apiRequest.addParam("res_secret", res_secret, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201903121125423526
    */
    public ApiResponse getSchoolNoByStudentInSchool(String userId, String schoolId) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/getSchoolNoByStudentInSchool", SdkConstant.AUTH_TYPE_DEFAULT, "2f51ef9cba2f43d88f42b0435f1ccd47");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("userId", userId, ParamPosition.FORM, true);
        
        apiRequest.addParam("schoolId", schoolId, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201901291015188663
    */
    public ApiResponse countStudentInOrgClass(String classId) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/countStudentInOrgClass", SdkConstant.AUTH_TYPE_DEFAULT, "94a5afc625254dae8f6d9a3ec3b92b35");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("classId", classId, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201903111745145652
    */
    public ApiResponse getAreaById(String id) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/getAreaById", SdkConstant.AUTH_TYPE_DEFAULT, "6f0674c29ff2475691d4364ea5028a07");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("id", id, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201901292115036212
    */
    public ApiResponse listSubject(String res_key, String res_secret, String subjectQuery) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listSubject", SdkConstant.AUTH_TYPE_DEFAULT, "f38cef92ee3b427ab21337bddb627364");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("res_key", res_key, ParamPosition.FORM, true);
        
        apiRequest.addParam("res_secret", res_secret, ParamPosition.FORM, true);
        
        apiRequest.addParam("subjectQuery", subjectQuery, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201901292126361058
    */
    public ApiResponse getResourceUrl(String resId, String userId, String res_key, String res_secret) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/getResourceUrl", SdkConstant.AUTH_TYPE_DEFAULT, "ce57ad10b26240428288d15cd88e19f3");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("resId", resId, ParamPosition.FORM, true);
        
        apiRequest.addParam("userId", userId, ParamPosition.FORM, false);
        
        apiRequest.addParam("res_key", res_key, ParamPosition.FORM, true);
        
        apiRequest.addParam("res_secret", res_secret, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201903291612005297
    */
    public ApiResponse listGrade(String gradeQuery, String res_key, String res_secret) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listGrade", SdkConstant.AUTH_TYPE_DEFAULT, "f38cef92ee3b427ab21337bddb627364");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("gradeQuery", gradeQuery, ParamPosition.FORM, true);
        
        apiRequest.addParam("res_key", res_key, ParamPosition.FORM, true);
        
        apiRequest.addParam("res_secret", res_secret, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201811082343369481
    */
    public ApiResponse listEduSystem() {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listEduSystem", SdkConstant.AUTH_TYPE_DEFAULT, "81d357851d6b4cef8c18f461cea88b3d");
        //initSignStrategy(apiRequest);
        
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201901070956544838
    */
    public ApiResponse listDepartmentInSchool(String schoolId, String param) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listDepartmentInSchool", SdkConstant.AUTH_TYPE_DEFAULT, "1be66879dfd44fc8b4195fe92e1ced4e");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("schoolId", schoolId, ParamPosition.FORM, true);
        
        apiRequest.addParam("param", param, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201901071001087099
    */
    public ApiResponse listOrgClassByPhaseGradeInSchool(String schoolId, String phaseCode, String gradeCode, String param) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listOrgClassByPhaseGradeInSchool", SdkConstant.AUTH_TYPE_DEFAULT, "cd89ff99e0d542d8ba117b1b66b66526");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("schoolId", schoolId, ParamPosition.FORM, true);
        
        apiRequest.addParam("phaseCode", phaseCode, ParamPosition.FORM, true);
        
        apiRequest.addParam("gradeCode", gradeCode, ParamPosition.FORM, true);
        
        apiRequest.addParam("param", param, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201903291604306090
    */
    public ApiResponse getGrade(String code, String res_key, String res_secret) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/getGrade", SdkConstant.AUTH_TYPE_DEFAULT, "f38cef92ee3b427ab21337bddb627364");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("code", code, ParamPosition.FORM, true);
        
        apiRequest.addParam("res_key", res_key, ParamPosition.FORM, true);
        
        apiRequest.addParam("res_secret", res_secret, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201811082344007167
    */
    public ApiResponse listSchoolByUser(String userId) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listSchoolByUser", SdkConstant.AUTH_TYPE_DEFAULT, "6656e651811c40daa377fc3997af5ac9");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("userId", userId, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201811151918092333
    */
    public ApiResponse countOrgTeacherInSchool(String schoolId) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/countOrgTeacherInSchool", SdkConstant.AUTH_TYPE_DEFAULT, "94a5afc625254dae8f6d9a3ec3b92b35");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("schoolId", schoolId, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201903111851106965
    */
    public ApiResponse listAllAreaByParentId(String parentId, String param) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listAllAreaByParentId", SdkConstant.AUTH_TYPE_DEFAULT, "6f0674c29ff2475691d4364ea5028a07");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("parentId", parentId, ParamPosition.FORM, false);
        
        apiRequest.addParam("param", param, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201901071018466258
    */
    public ApiResponse listOrgStudentInOrgClass(String classId, String param) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listOrgStudentInOrgClass", SdkConstant.AUTH_TYPE_DEFAULT, "f467f4d6806843b48ecc9394217ff110");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("classId", classId, ParamPosition.FORM, true);
        
        apiRequest.addParam("param", param, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201905252034311851
    */
    public ApiResponse addClassReport(String userId, String listUrl) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.GET, "/addClassReport", SdkConstant.AUTH_TYPE_DEFAULT, "c55c2d6dc1024e9b8d70ba95bfc0d66d");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("userId", userId, ParamPosition.QUERY, false);
        
        apiRequest.addParam("listUrl", listUrl, ParamPosition.QUERY, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201901041607124463
    */
    public ApiResponse listUserInDepartment(String departmentId, String param) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listUserInDepartment", SdkConstant.AUTH_TYPE_DEFAULT, "5eca0ae6176d451c8491cee5768f3e1f");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("departmentId", departmentId, ParamPosition.FORM, true);
        
        apiRequest.addParam("param", param, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201903121125392500
    */
    public ApiResponse getTeacherStaffNoInSchool(String schoolId, String teacherId) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/getTeacherStaffNoInSchool", SdkConstant.AUTH_TYPE_DEFAULT, "2f51ef9cba2f43d88f42b0435f1ccd47");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("schoolId", schoolId, ParamPosition.FORM, true);
        
        apiRequest.addParam("teacherId", teacherId, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201903291556373109
    */
    public ApiResponse getSubject(String code, String res_key, String res_secret) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/getSubject", SdkConstant.AUTH_TYPE_DEFAULT, "f38cef92ee3b427ab21337bddb627364");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("code", code, ParamPosition.FORM, true);
        
        apiRequest.addParam("res_key", res_key, ParamPosition.FORM, true);
        
        apiRequest.addParam("res_secret", res_secret, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201903291545513380
    */
    public ApiResponse addResource(String resourceForm, String res_key, String res_secret) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/addResource", SdkConstant.AUTH_TYPE_DEFAULT, "ce57ad10b26240428288d15cd88e19f3");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("resourceForm", resourceForm, ParamPosition.FORM, false);
        
        apiRequest.addParam("res_key", res_key, ParamPosition.FORM, false);
        
        apiRequest.addParam("res_secret", res_secret, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201904241559112488
    */
    public ApiResponse listRoleByUserId(String userId) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listRoleByUserId", SdkConstant.AUTH_TYPE_DEFAULT, "c8226864616a4261b85e4f45b21bb549");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("userId", userId, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201901292115106504
    */
    public ApiResponse queryResource(String resourceQuery, String res_key, String res_secret) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/queryResource", SdkConstant.AUTH_TYPE_DEFAULT, "ce57ad10b26240428288d15cd88e19f3");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("resourceQuery", resourceQuery, ParamPosition.FORM, false);
        
        apiRequest.addParam("res_key", res_key, ParamPosition.FORM, true);
        
        apiRequest.addParam("res_secret", res_secret, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201811261358228797
    */
    public ApiResponse getCurrentAcademicYearInSchool(String schoolId) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/getCurrentAcademicYearInSchool", SdkConstant.AUTH_TYPE_DEFAULT, "f46c1e332eaa4fefa45a8000bc2d89ad");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("schoolId", schoolId, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201903291602227851
    */
    public ApiResponse getPhase(String code, String res_key, String res_secret) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/getPhase", SdkConstant.AUTH_TYPE_DEFAULT, "f38cef92ee3b427ab21337bddb627364");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("code", code, ParamPosition.FORM, false);
        
        apiRequest.addParam("res_key", res_key, ParamPosition.FORM, true);
        
        apiRequest.addParam("res_secret", res_secret, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201902161354341887
    */
    public ApiResponse listUserByCycleRoleInOrgClass(String classId, String roleEnName, String teachingCycleId, String param) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listUserByCycleRoleInOrgClass", SdkConstant.AUTH_TYPE_DEFAULT, "5eca0ae6176d451c8491cee5768f3e1f");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("classId", classId, ParamPosition.FORM, true);
        
        apiRequest.addParam("roleEnName", roleEnName, ParamPosition.FORM, true);
        
        apiRequest.addParam("teachingCycleId", teachingCycleId, ParamPosition.FORM, true);
        
        apiRequest.addParam("param", param, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201901292116152890
    */
    public ApiResponse listUnit(String bookCode, String res_key, String res_secret) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listUnit", SdkConstant.AUTH_TYPE_DEFAULT, "f38cef92ee3b427ab21337bddb627364");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("bookCode", bookCode, ParamPosition.FORM, false);
        
        apiRequest.addParam("res_key", res_key, ParamPosition.FORM, true);
        
        apiRequest.addParam("res_secret", res_secret, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201901071004247336
    */
    public ApiResponse listDepartmentByUserId(String userId, String param) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listDepartmentByUserId", SdkConstant.AUTH_TYPE_DEFAULT, "1be66879dfd44fc8b4195fe92e1ced4e");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("userId", userId, ParamPosition.FORM, true);
        
        apiRequest.addParam("param", param, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201903292048407383
    */
    public ApiResponse listVolume(String res_key, String res_secret, String volumeQuery) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listVolume", SdkConstant.AUTH_TYPE_DEFAULT, "f38cef92ee3b427ab21337bddb627364");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("res_key", res_key, ParamPosition.FORM, false);
        
        apiRequest.addParam("res_secret", res_secret, ParamPosition.FORM, false);
        
        apiRequest.addParam("volumeQuery", volumeQuery, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201811082344274862
    */
    public ApiResponse getUserByUserId(String userId) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/getUserByUserId", SdkConstant.AUTH_TYPE_DEFAULT, "5eca0ae6176d451c8491cee5768f3e1f");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("userId", userId, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201811200950029358
    */
    public ApiResponse getAreaByCode(String code) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/getAreaByCode", SdkConstant.AUTH_TYPE_DEFAULT, "6f0674c29ff2475691d4364ea5028a07");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("code", code, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201901292115498041
    */
    public ApiResponse listEdition(String EditionQuery, String res_key, String res_secret) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listEdition", SdkConstant.AUTH_TYPE_DEFAULT, "f38cef92ee3b427ab21337bddb627364");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("EditionQuery", EditionQuery, ParamPosition.FORM, false);
        
        apiRequest.addParam("res_key", res_key, ParamPosition.FORM, true);
        
        apiRequest.addParam("res_secret", res_secret, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201905252101544993
    */
    public ApiResponse getBook(String bookCode, String res_key, String res_secret) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/getBook", SdkConstant.AUTH_TYPE_DEFAULT, "f38cef92ee3b427ab21337bddb627364");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("bookCode", bookCode, ParamPosition.FORM, false);
        
        apiRequest.addParam("res_key", res_key, ParamPosition.FORM, true);
        
        apiRequest.addParam("res_secret", res_secret, ParamPosition.FORM, true);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201906031012374563
    */
    public ApiResponse receiveResourceFeed(String stuId, String teacherId, String title, String resourceId, String type, String classId) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/receiveResourceFeed", SdkConstant.AUTH_TYPE_DEFAULT, "c55c2d6dc1024e9b8d70ba95bfc0d66d");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("stuId", stuId, ParamPosition.FORM, false);
        
        apiRequest.addParam("teacherId", teacherId, ParamPosition.FORM, false);
        
        apiRequest.addParam("title", title, ParamPosition.FORM, false);
        
        apiRequest.addParam("resourceId", resourceId, ParamPosition.FORM, false);
        
        apiRequest.addParam("type", type, ParamPosition.FORM, false);
        
        apiRequest.addParam("classId", classId, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201811151929357594
    */
    public ApiResponse listOrgTeacherInSchool(String schoolId, String param) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listOrgTeacherInSchool", SdkConstant.AUTH_TYPE_DEFAULT, "5eca0ae6176d451c8491cee5768f3e1f");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("schoolId", schoolId, ParamPosition.FORM, false);
        
        apiRequest.addParam("param", param, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
    /**
    * Version:201903291546599498
    */
    public ApiResponse listAllPhase(String res_key, String res_secret) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/listAllPhase", SdkConstant.AUTH_TYPE_DEFAULT, "f38cef92ee3b427ab21337bddb627364");
        //initSignStrategy(apiRequest);
        
        
        apiRequest.addParam("res_key", res_key, ParamPosition.FORM, false);
        
        apiRequest.addParam("res_secret", res_secret, ParamPosition.FORM, false);
        
        return syncInvoke(apiRequest);
    }
   
};