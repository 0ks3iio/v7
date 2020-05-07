package net.zdsoft.desktop.action.docking.custom;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.constant.custom.GanSuConstant;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.desktop.action.docking.SimpleLoginAction;
import net.zdsoft.desktop.utils.GanSuSecurityUtil;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ServletUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/homepage")
public class GanSuSimpleLoginAction extends SimpleLoginAction{
	
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	protected UnitRemoteService unitRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	
	@RequestMapping("/remote/openapi/gs/index")
	public String gcIndex(HttpServletRequest request, ModelMap map) {
		if (getSession() != null && (ServletUtils.getLoginInfo(getSession())) != null) {
			return "redirect:" + getNativeIndexUrl();
		}
		return "redirect:/homepage/remote/openapi/gs/doLoginGs";
	}
	
	@RequestMapping("/remote/openapi/gs/doLoginGs")
	public void doLoginGs(HttpServletRequest request,HttpServletResponse response, ModelMap map) {
		if("samlsso".equalsIgnoreCase(GanSuConstant.GS_SSO_TYPE)){
			doLogin(request, response);
		}else{
//			lamsso
		}
	}
	
	
	@RequestMapping("/remote/openapi/gs/login")
	public String loginForXingYun(HttpServletRequest request, ModelMap map) {
		try {
			/******************获取IDP登录认证成功后返回值*****************/
	        String SMAL = request.getParameter(GanSuConstant.GS_SAML_RESPONSE);
	        if (StringUtils.isBlank(SMAL)) { //如返回值为空，则进行跳转错误页面处理
	            //处理错误页面
	            log.error("SAMLResponse已失效：-------------");
				return promptFlt(map,"SAMLResponse已失效");
	        }
	        /***************获取IDP登录认证成功后返回的userId*********/
	        String userId = GanSuSecurityUtil.jiemi(SMAL).split("\\|")[0];
	        String userType = request.getParameter("userType");
	        String updateTime = request.getParameter("updateTime");
	        String saveURL = "/homepage/remote/openapi/gs/saveUser?userId=" + userId + "&userType=" + userType + "&updateTime=" + updateTime;
	        Date updateTime1 = doChangeUpdateTime(updateTime);
	        if(!StringUtils.startsWith(userId, GanSuConstant.GS_BEFORE_USERNAME_VALUE)) {
	        	userId = GanSuConstant.GS_BEFORE_USERNAME_VALUE + userId;
			}
	        User user = SUtils.dc(userRemoteService.findByUsername(userId), User.class);
	        if(user != null && user.getModifyTime().compareTo(updateTime1) != -1) {
				String backUrl = "/homepage/remote/openapi/gs/loginEis?uid=" + Base64.getEncoder().encodeToString(user.getUsername().getBytes());
				String logoutResult = logoutBeforeUsername(user.getUsername(), backUrl);
				if(StringUtils.isNotBlank(logoutResult)){	
					return logoutResult;
				}
				return loginUserName(user, null, "1", map);
	 	    }else {
	 			return "redirect:" + saveURL;
	 	    }
		} catch (Exception e) {
			 log.error("数据登陆报错：-------------",e);
			return promptFlt(map,"登录失败，数据解密异常");
		}
	}
	
	@RequestMapping(value = "/remote/openapi/gs/saveUser")
	public String remoteGSsaveUser(HttpServletRequest request,String userId, String userType,String updateTime, ModelMap map) {
		try {
			String userInfo = getUserInfo(userId,userType);
			System.out.println("userInfo ------" + userInfo);
			UserInfo info = getJsonData(userInfo);
			if(info == null){
				return promptFlt(map,"获取到的用户信息为空");
			}
			if("1".equals(info.getUserClass()) || "21".equals(info.getUserClass()) || "2".equals(info.getUserClass())){
				doSaveTeacherInfo(info,updateTime);
			}else if ("22".equals(info.getUserClass())){
				doSaveStudentInfo(info, updateTime);
			}
			if(!StringUtils.startsWith(userId, GanSuConstant.GS_BEFORE_USERNAME_VALUE)) {
				userId = GanSuConstant.GS_BEFORE_USERNAME_VALUE + userId;
			}
			User user = SUtils.dc(userRemoteService.findByUsername(userId), User.class);
			if(user != null){
				return loginUserName(user, null, "1", map);
			}else{
				return promptFlt(map,"登录的用户不存在");
			}
		} catch (IOException e) {
			return promptFlt(map,"登录失败，数据获取异常");
		}
	}
	
	/**
     * LAM登录
     * */
//    public void executeLam(HttpServletRequest req,
//    		HttpServletResponse servletResponse, FilterChain filterChain)
//            throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) servletRequest;
//        HttpSession session = req.getSession(false);
//        if (session == null) {
//            session = req.getSession(true);
//        }
//        Object userLoginInfo = session.getAttribute("UserLoginInfo");
//        if (userLoginInfo == null) {
//            String authUser = req.getHeader("LAM_AUTHN_USER_ID");
//            if (authUser != null) {
//                //解码Base64编码的header值
//                authUser = LAMAuthClient.decodeHeaderValue(authUser);
//                // HTTP header的值为"USERID;SIGNATURE;EXPIRATION"
//                String[] headers = authUser.split(";");
//
//                if(!LAMAuthClient.isHeaderValid(headers[2])){
//                    throw new RuntimeException("HTTP头中用户相关信息已经失效");
//                }
//                boolean result = LAMAuthClient.validateLAMHttpHeader(headers[0], headers[2], headers[1], "public_key.dat");
//                System.out.println("laml" + result);
//                if(!result){
//                    throw new RuntimeException("HTTP头中用户相关信息校验失败");
//                }
//                /**
//                 * 在此添加业务系统登录逻辑实现代码，为当前请求用户(headers[0])授权
//                 *
//                 * */
//            }
//        }
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
	
	private UserInfo getJsonData(String userXML) {
		UserInfo info = null;
		try {
			Document document =  DocumentHelper.parseText(userXML);
			Element bookStore = document.getRootElement();
			Iterator<?> it = bookStore.elementIterator("user");
			while (it.hasNext()) {
				Element userElement = (Element) it.next();
				String userId = userElement.elementTextTrim("userId");   
				String realName = userElement.elementTextTrim("userName");
				String parentId = userElement.elementTextTrim("parentId");
				String userClass = userElement.elementTextTrim("userClass");
				if(StringUtils.isBlank(userId) || StringUtils.isBlank(realName) || StringUtils.isBlank(parentId) 
						|| StringUtils.isBlank(userClass)){
					continue;
				}
				Unit unit   = getUnit(parentId,userClass);
				if(unit == null){
					continue;
				}
				Integer userType = getUserType(userClass);
				Integer ownerType = getOwnerType(userClass);
				String gradeId  = userElement.elementTextTrim("ssnjId");
                String gradeName = userElement.elementTextTrim("ssnj");
                String classId  =  userElement.elementTextTrim("ssbjId");
                String className = userElement.elementTextTrim("ssbj");
				if(StringUtils.isNotBlank(gradeId))
					gradeId = doChangeId(gradeId);
				if(StringUtils.isNotBlank(classId))
					classId = doChangeId(classId);
				info = new UserInfo();
				info.setUserId(userId);
				info.setRealName(realName);
				info.setParentId(parentId);
				info.setUserClass(userClass);
				info.setUserType(userType);
				info.setOwnerType(ownerType);
				info.setGradeId(gradeId);
				info.setGradeName(gradeName);
				info.setClassId(classId);
				info.setClassName(className);
				info.setUnit(unit);
			}	
		}catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return info;
	}
	
	
	class UserInfo{
		private String userId;
		private String realName;
		private String parentId;
		private String userClass;
		private String gradeId;
		private String gradeName;
		private String classId;
		private String className;
		private Integer userType;
		private Integer ownerType;
		private Unit    unit;
		public Unit getUnit() {
			return unit;
		}
		public void setUnit(Unit unit) {
			this.unit = unit;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getRealName() {
			return realName;
		}
		public void setRealName(String realName) {
			this.realName = realName;
		}
		public String getParentId() {
			return parentId;
		}
		public void setParentId(String parentId) {
			this.parentId = parentId;
		}
		public String getUserClass() {
			return userClass;
		}
		public void setUserClass(String userClass) {
			this.userClass = userClass;
		}
		public String getGradeId() {
			return gradeId;
		}
		public void setGradeId(String gradeId) {
			this.gradeId = gradeId;
		}
		public String getGradeName() {
			return gradeName;
		}
		public void setGradeName(String gradeName) {
			this.gradeName = gradeName;
		}
		public String getClassId() {
			return classId;
		}
		public void setClassId(String classId) {
			this.classId = classId;
		}
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}
		public Integer getUserType() {
			return userType;
		}
		public void setUserType(Integer userType) {
			this.userType = userType;
		}
		public Integer getOwnerType() {
			return ownerType;
		}
		public void setOwnerType(Integer ownerType) {
			this.ownerType = ownerType;
		}
	}

	/**
     * 跳转IDP端进行登录授权
     *
     * @param request
     * @param response
     */
    public void doLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            StringBuffer curUrl = request.getRequestURL();// 获取当前url
            request.getSession().setAttribute("RelayState", curUrl.toString());// 保存当前url，用于IDP端登录后，自动跳转到该url
            String loginSamlUrl = GanSuConstant.GS_LOGIN_SAML_URL; // 获取saml单点登录认证url
            response.setHeader(
                    "P3P",
                    "CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
            String AssertionConsumerServiceURL = "http://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath() + "/homepage/remote/openapi/gs/login";// 设置sp端对应的登录授权url（用与在IDP端成功认证后，跳转到sp端进行登录授权）
            StringBuffer form = new StringBuffer();
            response.setContentType("text/html");
            form.append(
                     "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>loading...</title></head><body onload='document.forms[0].submit()'><form action=\"")
                    .append(loginSamlUrl)
                    .append("\" method=\"post\"><input type=\"hidden\" name=\"SAMLRequest\" value=\"")
                    .append(AssertionConsumerServiceURL)
                    .append("\"></form></body></html>");
            out.write(form.toString());
            out.flush();
            out.close();
        } catch (Exception ex) {
            log.error("saml 调整登录失败!" + ex);
        }
    }
	
	@SuppressWarnings("deprecation")
	public static String getUserInfo(String userId,String userType) throws IOException {
        //token
        String token = getToken();
        System.out.println("token--------------" + token);
        long ctime = System.currentTimeMillis();
        String param = userId + "|" + ctime + "|" + userType + "|" + GanSuConstant.GS_MSID;
        System.out.println("token--------------" + param);
        param = GanSuSecurityUtil.jiami(param);
        param = URLEncoder.encode(param);
        System.out.println("param--------------" + param);
        Map<String,String> paramMap = Maps.newHashMap();
        paramMap.put("param",param);
        paramMap.put("client_id",GanSuConstant.GS_APPID);
        paramMap.put("access_token",token);
        String result = UrlUtils.readContent(GanSuConstant.GS_CALL_URL,paramMap,Boolean.TRUE);
        JSONObject obj = JSONObject.parseObject(result);
        String userXml = obj.getString("result");
        return GanSuSecurityUtil.jiemi(userXml);
    }
	

    //获取token
    public static String getToken() {
    	String token = null;
    	Map<String,String> paramMap = Maps.newHashMap();
    	paramMap.put("client_id",GanSuConstant.GS_APPID);
    	paramMap.put("client_secret",GanSuConstant.GS_CLIENT_SECRET);
    	paramMap.put("grant_type","client_credentials");
        JSONObject obj;
		try {
			String result = UrlUtils.post(GanSuConstant.GS_TOKEN_URL, paramMap);
			obj = JSONObject.parseObject(result);
			token = obj.getString("access_token");
		} catch (IOException e) {
			log.error("获取token失败 ----" + e.getMessage());
			return null;
		}
        return token;
    }
    /**
     * 得到用户信息 
     * @param userXml
     * @return
     */
    private void doSaveTeacherInfo(UserInfo info,String updateTime){
    	List<Teacher> saveTeacherList = new ArrayList<>();
		List<User> saveUserList = new ArrayList<>();
		Date modifyTime = doChangeUpdateTime(updateTime);
    	try {
            String userId = info.getUserId();   
            String realName = info.getRealName();
            Unit unit   = info.getUnit();
            Integer userType = info.getUserType();
            Integer ownerType = info.getOwnerType();
            if(!StringUtils.startsWith(userId, GanSuConstant.GS_BEFORE_USERNAME_VALUE)) {
	        	userId = GanSuConstant.GS_BEFORE_USERNAME_VALUE + userId;
			}
            User user = SUtils.dc(userRemoteService.findByUsername(userId), User.class);
            if(user != null ){
            	Teacher teacher = SUtils.dc(teacherRemoteService.findOneById(user.getOwnerId()), Teacher.class);
            	teacher.setUnitId(unit.getId());
            	teacher.setTeacherName(realName);
            	teacher.setModifyTime(modifyTime);
            	saveTeacherList.add(teacher);
            	user.setRealName(realName);
            	user.setUnitId(unit.getId());
            	user.setModifyTime(modifyTime);
            	saveUserList.add(user);
            }else{
            	Teacher t = new Teacher();
            	t.setId(UuidUtils.generateUuid());
				t.setCreationTime(new Date());
				t.setUnitId(unit.getId());
				t.setTeacherName(realName);
				String deptId = getDeptId(unit.getId());
				t.setDeptId(deptId);
				t.setModifyTime(modifyTime);
				saveTeacherList.add(t);
            	
				User u = new User();
				u.setId(UuidUtils.generateUuid());
				u.setPassword(getDefaultPwd());
				u.setOwnerType(ownerType);
				u.setUsername(userId);
			    u.setOwnerId(t.getId());
			    u.setUnitId(unit.getId());
			    u.setRealName(t.getTeacherName());
			    u.setUserType(userType);
			    u.setDeptId(t.getDeptId());
			    u.setModifyTime(modifyTime);
			    saveUserList.add(u);
            }
          //进行保存教师和学生数据
    		System.out.println("数据同步到教师的数据：--------" +  saveTeacherList.size() + "同步用户的数据是：------" + saveUserList.size());
    		if(CollectionUtils.isNotEmpty(saveTeacherList)){
    			if(CollectionUtils.isNotEmpty(saveUserList)){
    				baseSyncSaveService.saveTeacherAndUser(saveTeacherList.toArray(new Teacher[saveTeacherList.size()]),saveUserList.toArray(new User[saveUserList.size()]));
    			}else{
    				baseSyncSaveService.saveTeacher(saveTeacherList.toArray(new Teacher[saveTeacherList.size()]));
    			}
    		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	private String getDeptId(String id) {
		// TODO Auto-generated method stub
		List<Dept> dept = SUtils.dt(deptRemoteService.findByUnitId(id), Dept.class);
		for (Dept d : dept) {
			if(d.getIsDefault() != null &&  1 == d.getIsDefault()){
				return d.getId();
			}
		}
		return Unit.TOP_UNIT_GUID;
	}

	private void doSaveStudentInfo(UserInfo info, String updateTime) {
		// TODO Auto-generated method stub
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
		if (semester == null) {
			log.error("------ 请先维护学年学期 -------");
			return;
		}
		List<Student> saveStudentList = new ArrayList<>();
		List<User> saveUserList = new ArrayList<>();
		List<Clazz>  saveClazzList = new ArrayList<>();
		List<Grade> saveGradeList = new ArrayList<>();
    	try {
                //封装教师
	    		String userId = info.getUserId();   
	            String realName = info.getRealName();
	            String parentId = info.getParentId();
	            String userClass = info.getUserClass();
                String gradeId  =  info.getGradeId();
                String gradeName = info.getGradeName();
                String classId  =  info.getClassId();
                String className = info.getClassName();
                if(StringUtils.isBlank(userId) || StringUtils.isBlank(realName) || StringUtils.isBlank(parentId) 
                		|| StringUtils.isBlank(userClass) || StringUtils.isBlank(gradeId) || StringUtils.isBlank(gradeName)
                		|| StringUtils.isBlank(classId) || StringUtils.isBlank(className)){
                	return;
                }
                Unit unit   = info.getUnit();
                gradeId = doChangeId(gradeId);
                classId = doChangeId(classId);
                String section = getSection(gradeName);
                String acadyear = getAcadYear(gradeName);
                Integer schoolingLength = (Integer.valueOf(section) == 1 ? 6 : 3);
                
                //年级名称的转化
				Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
				if(clazz != null){
					clazz.setModifyTime(new Date());
				}else if (clazz == null){
					clazz = new Clazz();
					clazz.setId(classId);
					clazz.setCreationTime(new Date());
				}
				clazz.setSchoolId(unit.getId());
				clazz.setClassName(className);
				clazz.setSection(Integer.valueOf(section));
				clazz.setSchoolingLength(schoolingLength);
				clazz.setAcadyear(acadyear);
				//班号我们自己生成
				String classCodePrefix = StringUtils.substring(clazz.getAcadyear(), 0, 4)
						+ StringUtils.leftPad(clazz.getSection() + "", 2, "0");
				Long max = RedisUtils
						.incrby("syncdata.gansu.class.code.max." + clazz.getSchoolId() + classCodePrefix, 1);
				clazz.setClassCode(classCodePrefix + StringUtils.leftPad("" + max, 2, "0"));
				
				 //---得到gradeCode
                int year = NumberUtils.toInt(StringUtils.substring(clazz.getAcadyear(), 0, 4));
				int year2 = NumberUtils.toInt(StringUtils.substring(semester.getAcadyear(), 0, 4));
				if(semester.getSemester() == 2){
					year2 = NumberUtils.toInt(StringUtils.substring(semester.getAcadyear(), 5));
				}
                Integer yearNum = null;
				String monthTime = "0910";  //班级的新建时间暂定是9月1号
				String nowCreatime = new SimpleDateFormat("MMdd").format(new Date());
				if(NumberUtils.toInt(nowCreatime) > NumberUtils.toInt(monthTime)){
					year2++;
				}
				if (year2 > (year + schoolingLength)) {
					clazz.setGraduateDate(new Date());
					yearNum = 6;
				} else {
					yearNum = year2 - year;
					if(yearNum == 0 ){
						yearNum = 1;
					}
					clazz.setIsGraduate(0);
				}
				String gradeCode = ""+clazz.getSection() + yearNum;
                // ----结束 
//			-----------封装年级数据
				Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
				if(grade != null){
					grade.setModifyTime(clazz.getModifyTime());
				}else{
					grade = new Grade();
					grade.setId(gradeId);
					grade.setCreationTime(clazz.getCreationTime());
					grade.setEventSource(1);
					grade.setAmLessonCount(5);
					grade.setPmLessonCount(4);
					grade.setNightLessonCount(3);
					grade.setSubschoolId(clazz.getCampusId());
					grade.setIsGraduate(0);
					grade.setGradeCode(gradeCode);
				}
				grade.setSchoolId(clazz.getSchoolId());
				grade.setSection(clazz.getSection());
				grade.setSchoolingLength(clazz.getSchoolingLength());
				grade.setGradeName(gradeName);
				grade.setOpenAcadyear(clazz.getAcadyear());
				saveGradeList.add(grade);
				clazz.setGradeId(gradeId);
				saveClazzList.add(clazz);
				// ----封装学生 
				if(!StringUtils.startsWith(userId, GanSuConstant.GS_BEFORE_USERNAME_VALUE)) {
    	        	userId = GanSuConstant.GS_BEFORE_USERNAME_VALUE + userId;
    			}
                User user = SUtils.dc(userRemoteService.findByUsername(userId), User.class);
                Student student;
                if(user != null){
                	student = SUtils.dc(studentRemoteService.findOneById(user.getOwnerId()), Student.class);
                	if(student == null){
                		student = new Student();
                    	student.setId(UuidUtils.generateUuid());
                	}
                }else{
                	student = new Student();
                	student.setId(UuidUtils.generateUuid());
                	user = new User();
                	user.setId(UuidUtils.generateUuid());
                	user.setOwnerId(student.getId());
                	user.setPassword(getDefaultPwd());
                	user.setOwnerType(User.OWNER_TYPE_STUDENT);
					user.setUserType(User.USER_TYPE_COMMON_USER);
					user.setUsername(userId);
                }
                student.setSchoolId(clazz.getSchoolId());
                student.setClassId(clazz.getId());
                student.setStudentName(realName);
                student.setStudentCode("0000");
                saveStudentList.add(student);
                
                user.setOwnerId(student.getId());
                user.setUnitId(clazz.getSchoolId());
                user.setRealName(realName);
                saveUserList.add(user);
            //进行保存数据
			if(CollectionUtils.isNotEmpty(saveClazzList)) {
				baseSyncSaveService.saveClass(saveClazzList.toArray(new Clazz[saveClazzList.size()]));
			}
			if (CollectionUtils.isNotEmpty(saveGradeList)) {
				baseSyncSaveService.saveGrade(saveGradeList.toArray(new Grade[saveGradeList.size()]));
			}
			//进行保存教师和学生数据
			if(CollectionUtils.isNotEmpty(saveStudentList)){
				if(CollectionUtils.isNotEmpty(saveUserList)){
					baseSyncSaveService.saveStudentAndUser(saveStudentList.toArray(new Student[saveStudentList.size()]),saveUserList.toArray(new User[saveUserList.size()]));
				}else{
					baseSyncSaveService.saveStudent(saveStudentList.toArray(new Student[saveStudentList.size()]));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	/**
	 * 得到班级的开班学年
	 * @param gradeName
	 * @return
	 */
	private String getAcadYear(String gradeName) {
	    gradeName=gradeName.trim();
	    String startAcadyear="";
	    for(int i=0;i<gradeName.length();i++){
		    if(gradeName.charAt(i)>=48 && gradeName.charAt(i)<=57){
		    	startAcadyear+=gradeName.charAt(i);
		     }
	    }
	    String endAcadyear = String.valueOf( NumberUtils.toInt(startAcadyear.substring(0, 4)) + 1 );
		return (startAcadyear + "-" + endAcadyear);
	}

	/**
	 * 根据年级名称得到 学段
	 * @param gradeName
	 * @return
	 */
	 private String getSection(String gradeName) {
		return gradeName.contains("小学") ? "1" : gradeName.contains("初中") ? "2" : "3";
	 }
	 

	private Date doChangeUpdateTime(String updateTime) {
		 if(StringUtils.isNotBlank(updateTime)){
			 long lt = new Long(updateTime);
			 if(updateTime.length() == 10){
				 lt = lt * 1000;
			 }
			 return new Date(lt);
		 }else{
			 return new Date();
		 }
	}
    
    private Integer getOwnerType(String userClass) {
		return (userClass.equals(GanSuConstant.GS_EDU_ADMIN_TEACHER) || userClass.equals(GanSuConstant.GS_SCH_TEACHER)
				|| userClass.equals(GanSuConstant.GS_SCH_ADMIN_TEACHER))
				? User.OWNER_TYPE_TEACHER : User.OWNER_TYPE_STUDENT;
	}

	private int getUserType(String userClass) {
		// TODO Auto-generated method stub
		return userClass.equals("1") ? User.USER_TYPE_UNIT_ADMIN : User.USER_TYPE_COMMON_USER;
	}

	/**
     * 得到单位的id
     * @param parentId
     * @return
     */
    private Unit getUnit(String parentId, String unitClass) {
    	if(StringUtils.isBlank(parentId)){
    		parentId = "62";
    	}
    	if("1" .equals(unitClass)){
    		parentId = getUnionCode(parentId);
    		List<Unit> units = SUtils.dt(unitRemoteService.findByUniCode(parentId), Unit.class);
    		return CollectionUtils.isNotEmpty(units) ? units.get(0) : null;
    	}else {
    		School school = SUtils.dc(schoolRemoteService.findByCode(parentId), School.class);
    		if(school != null){
    			return SUtils.dc(unitRemoteService.findOneById(school.getId()), Unit.class);
    		}
    	}
    	return null;
	}

    
    private String getUnionCode(String parentId) {
		// TODO Auto-generated method stub
    	String unionCode;
    	String regionCode = StringUtils.substring(parentId,0,6);
    	int index = regionCode.indexOf("00");
    	int num =0;
        if (parentId.length() > 6) {
           String code = parentId.substring(6);
            num = NumberUtils.toInt(code);
        }
        if (num  == 0) {
            if (index == -1 || index %2 !=0) {
            	unionCode = regionCode;
            } else {
            	unionCode = regionCode.substring(0, index);
            }
        }else{
        	unionCode = regionCode + StringUtils.leftPad(String.valueOf(num), 6, "0");
        }
		return unionCode;
	}
    
    public static void main(String[] args) {
    	String ssString = "hDZgiAfwaBA8YYSR3sUiHQ==";
    	System.out.println(GanSuSecurityUtil.jiemi(ssString));
    	String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(1546272000000L);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        System.out.println(res);
    	
    	
	}
}
