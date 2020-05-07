package net.zdsoft.remote.openapi.action.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.RhKeyUnit;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.enums.UserOwnerTypeCode;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.RhKeyUnitRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.action.OpenApiBaseAction;
import net.zdsoft.remote.openapi.constant.OpenApiConstants;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

@Controller
@RequestMapping(value = { "/remote/openapi", "/openapi" })
public class RemoteRonghouAction extends OpenApiBaseAction{
	private Logger log = Logger.getLogger(RemoteRonghouAction.class);
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private ClassTeachingRemoteService classTeachingRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private RhKeyUnitRemoteService rhKeyUnitRemoteService;
	/**
	 * 教师用户信息
	 */
	@RequestMapping(value="/rh/user" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getRonghouUser(String ukey,Integer pageIndex) {
		JSONObject json = new JSONObject();
		if(pageIndex==null || pageIndex<=0) {
			json.put("resultMsg", "pageIndex参数传递错误！");
			return json.toJSONString();
		}
		List<String> unitIds = findUnitIdByukey(ukey);
		
		json.put("result", OpenApiConstants.REMOTE_JSON_RESULT_CODE_ERROR);
		if(CollectionUtils.isEmpty(unitIds)){
			json.put("resultMsg", "没有设置单位范围，请联系万朋研发人员！");
			return json.toJSONString();
		}
		//固定每次1000条
		Pagination page = createPagination();
		page.setPageSize(1000);
		//page.setPageSize(100);
		page.setPageIndex(pageIndex);
		try {
			List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findByUnitIdIn(unitIds.toArray(new String[] {}),SUtils.s(page)), new TR<List<Teacher>>() {});
			JSONArray data = new JSONArray();
			if(CollectionUtils.isEmpty(teacherList)) {
				json.put("result", OpenApiConstants.REMOTE_JSON_RESULT_CODE_SUCCESS);
				json.put("data", data);
				json.put("resultMsg", "调用成功！");
				json.put("dataCount", 0);
				return json.toJSONString();
			}
			List<Unit> unitList = SUtils.dt(unitRemoteService.findListByIds(unitIds.toArray(new String[] {})), new TR<List<Unit>>() {});
			Map<String,String> unitMap=EntityUtils.getMap(unitList, e->e.getId(),e->e.getUnitName());
			
			JSONObject json1=null;
			List<String> ids = EntityUtils.getList(teacherList, e->e.getId());
			//升序只获取最新一条
			List<ClassTeaching> tsList = SUtils.dt(classTeachingRemoteService.findClassTeachingListByTeacherId(null, ids.toArray(new String[] {})), new TR<List<ClassTeaching>>() {});
			Map<String,String[]>tsgMap=new HashMap<String, String[]>();
			if(CollectionUtils.isNotEmpty(tsList)){
				Set<String> claids = EntityUtils.getSet(tsList, e->e.getClassId());
				Set<String> subids = EntityUtils.getSet(tsList, e->e.getSubjectId());
				List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subids.toArray(new String[0])),new TR<List<Course>>() {});
				Map<String,String> subMap=EntityUtils.getMap(courseList, e->e.getId(),e->e.getSubjectName());
				List<Clazz> clazzList = SUtils.dt(classRemoteService.findByIn("id",claids.toArray(new String[] {})), new TR<List<Clazz>>() {});
				Set<String> graids = EntityUtils.getSet(clazzList, e->e.getGradeId());
				
				Map<String,String> clagraMap=EntityUtils.getMap(clazzList, e->e.getId(),e->e.getGradeId());
				List<Grade> gradeList = SUtils.dt(gradeRemoteService.findListByIds(graids.toArray(new String[] {})), new TR<List<Grade>>() {});
				Map<String,String> gradeMap=EntityUtils.getMap(gradeList, e->e.getId(),e->e.getGradeName());
				
				for(ClassTeaching t:tsList){
					String subname = subMap.get(t.getSubjectId());
					if(StringUtils.isNotBlank(subname)){
						String[] strs=new String[2];
						strs[0]=subname;
						strs[1]= gradeMap.get(clagraMap.get(t.getClassId()));
						tsgMap.put(t.getTeacherId(), strs);
					}
				}
			}
			
			
			
			List<User> userlist = SUtils.dt(userRemoteService.findByOwnerType(unitIds.toArray(new String[] {}), UserOwnerTypeCode.TEACHER), new TR<List<User>>() {});
			Map<String,String> userMap=EntityUtils.getMap(userlist, e->e.getOwnerId(),e->e.getUsername());
			
			for(Teacher t:teacherList) {
				json1=new JSONObject();
				json1.put("wpid", t.getId());
				json1.put("schoolName", unitMap.get(t.getUnitId())==null?"":unitMap.get(t.getUnitId()));
				json1.put("teacherName", t.getTeacherName());
				json1.put("telphone", userMap.get(t.getId())==null?"":userMap.get(t.getId()));
				String[] strs=tsgMap.get(t.getId());
				if(ArrayUtils.isNotEmpty(strs)){
					json1.put("subjectName", strs[0]==null?"":strs[0]);
					json1.put("gradeName", strs[1]==null?"":strs[1]);
				}else{
					json1.put("subjectName", "");
					json1.put("gradeName", "");
				}
				
				data.add(json1);
			}
			json.put("data", data);
			json.put("resultMsg", "调用成功！");
			json.put("result", OpenApiConstants.REMOTE_JSON_RESULT_CODE_SUCCESS);
			json.put("dataCount", data.size());
			return json.toJSONString();
		}catch (Exception e) {
			e.printStackTrace();
			log.error("容厚调用用户接口参数：ukey="+ukey+"&pageIndex="+pageIndex+",调用失败");
			log.error(e.getMessage());
			json.put("resultMsg", "调用失败！");
			json.put("dataCount", 0);
			return json.toJSONString();
		}
	}
	/**
	 * 学生信息
	 * @param ukey
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/rh/student", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getRonghouStudent(String ukey,Integer pageIndex) {
		JSONObject json = new JSONObject();
		if(pageIndex==null || pageIndex<=0) {
			json.put("resultMsg", "pageIndex参数传递错误！");
			return json.toJSONString();
		}
		if(StringUtils.isBlank(ukey)) {
			json.put("resultMsg", "ukey没有传值！");
			return json.toJSONString();
		}
		List<String> unitIds = findUnitIdByukey(ukey);
		
		json.put("result", OpenApiConstants.REMOTE_JSON_RESULT_CODE_ERROR);
		if(CollectionUtils.isEmpty(unitIds)){
			json.put("resultMsg", "没有设置单位范围，请联系万朋研发人员！");
			return json.toJSONString();
		}
		//固定每次1000条
		Pagination page = createPagination();
		page.setPageSize(1000);
		//page.setPageSize(100);
		page.setPageIndex(pageIndex);
		try {
			JSONArray data = new JSONArray();
			List<Student> studentList = SUtils.dt(studentRemoteService.findBySchoolIdIn(unitIds.toArray(new String[] {}),SUtils.s(page)), new TR<List<Student>>() {});
			if(CollectionUtils.isEmpty(studentList)) {
				json.put("result", OpenApiConstants.REMOTE_JSON_RESULT_CODE_SUCCESS);
				json.put("data", data);
				json.put("resultMsg", "调用成功！");
				json.put("dataCount", 0);
				return json.toJSONString();
			}
			List<Unit> unitList = SUtils.dt(unitRemoteService.findListByIds(unitIds.toArray(new String[] {})), new TR<List<Unit>>() {});
			Map<String,String> unitMap=EntityUtils.getMap(unitList, e->e.getId(),e->e.getUnitName());
			List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdIn(unitIds.toArray(new String[] {})), new TR<List<Clazz>>() {});
			Map<String,Clazz> clazzMap=EntityUtils.getMap(clazzList, e->e.getId());
			
			List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdsIn(unitIds.toArray(new String[] {})), new TR<List<Grade>>() {});
			Map<String,String> gradeMap=EntityUtils.getMap(gradeList, e->e.getId(),e->e.getGradeName());
			
			JSONObject json1=null;
			//DM-BJWLLX 班级文理类型
			Map<String, McodeDetail> mcodeMap=SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-BJWLLX"), new TR<Map<String,McodeDetail>>(){});
			Integer ii=null;
			List<User> userlist = SUtils.dt(userRemoteService.findByOwnerType(unitIds.toArray(new String[] {}), UserOwnerTypeCode.STUDENT), new TR<List<User>>() {});
			Map<String,String> userMap=EntityUtils.getMap(userlist, e->e.getOwnerId(),e->e.getUsername());
			
			for(Student t:studentList) {
				json1=new JSONObject();
				json1.put("wpid", t.getId());
				json1.put("studentName", t.getStudentName());
				json1.put("schoolName", unitMap.get(t.getSchoolId())==null?"":unitMap.get(t.getSchoolId()));
				json1.put("studentCode", t.getStudentCode()==null?"":t.getStudentCode());
				if(clazzMap.containsKey(t.getClassId())) {
					json1.put("className", clazzMap.get(t.getClassId()).getClassName());
					if(gradeMap.containsKey(clazzMap.get(t.getClassId()).getGradeId())) {
						json1.put("gradeName", gradeMap.get(clazzMap.get(t.getClassId()).getGradeId()));
					}else {
						json1.put("gradeName", "");
					}
					ii=clazzMap.get(t.getClassId()).getArtScienceType();
					if(ii==null) {
						json1.put("classType", "不区分");
					}else {
						if(mcodeMap.containsKey(ii+"")) {
							json1.put("classType", mcodeMap.get(ii+"").getMcodeContent());
						}else {
							json1.put("classType", "不区分");
						}
					}
					//json1.put("examCode",t.getStudentCode()==null?"":t.getStudentCode());
				}else {
					json1.put("className", "");
					json1.put("gradeName", "");
					json1.put("classType", "不区分");
				}
				if(userMap.containsKey(t.getId())) {
					json1.put("username", userMap.get(t.getId()));
				}else {
					json1.put("username", "");
				}
				
				
				data.add(json1);
			}
			json.put("data", data);
			json.put("resultMsg", "调用成功！");
			json.put("result", OpenApiConstants.REMOTE_JSON_RESULT_CODE_SUCCESS);
			json.put("dataCount", data.size());
			return json.toJSONString();
		}catch (Exception e) {
			e.printStackTrace();
			log.error("容厚调用用户接口参数：ukey="+ukey+"&pageIndex="+pageIndex+",调用失败");
			log.error(e.getMessage());
			json.put("resultMsg", "调用失败！");
			json.put("dataCount", 0);
			return json.toJSONString();
		}
	}
	
	/**
	 * 科目信息
	 * @param ukey
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/rh/subject", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getRonghouSubject(String ukey) {
		JSONObject json = new JSONObject();
		if(StringUtils.isBlank(ukey)) {
			json.put("resultMsg", "ukey没有传值！");
			return json.toJSONString();
		}
		List<String> unitIds = findUnitIdByukey(ukey);
		
		json.put("result", OpenApiConstants.REMOTE_JSON_RESULT_CODE_ERROR);
		if(CollectionUtils.isEmpty(unitIds)){
			json.put("resultMsg", "没有设置单位范围，请联系万朋研发人员！");
			return json.toJSONString();
		}
		JSONArray data = new JSONArray();
		JSONObject json1=null;
		//只获取顶级单位（32个0） 以及学校单位
		Unit topUnit = SUtils.dc(unitRemoteService.findTopUnit(null), Unit.class);
		Set<String> subjectNames=new HashSet<>();
		List<Course> courseList=null;
		try {
			if(topUnit!=null) {
				courseList = SUtils.dt(courseRemoteService.findByUnitIdIn(new String[] {topUnit.getId()}),new TR<List<Course>>() {});
				if(CollectionUtils.isNotEmpty(courseList)) {
					for(Course c:courseList) {
						if(subjectNames.contains(c.getSubjectName())) {
							continue;
						}
						json1=new JSONObject();
						json1.put("wpid", c.getId());
						json1.put("subjectNa,e", c.getSubjectName());
						subjectNames.add(c.getSubjectName());
						data.add(json1);
					}
				}
				
			}
			courseList = SUtils.dt(courseRemoteService.findByUnitIdIn(unitIds.toArray(new String[] {})),new TR<List<Course>>() {});
			if(CollectionUtils.isNotEmpty(courseList)) {
				for(Course c:courseList) {
					if(subjectNames.contains(c.getSubjectName())) {
						continue;
					}
					json1=new JSONObject();
					json1.put("wpid", c.getId());
					json1.put("subjectName", c.getSubjectName());
					subjectNames.add(c.getSubjectName());
					data.add(json1);
				}
			}
			json.put("data", data);
			json.put("resultMsg", "调用成功！");
			json.put("result", OpenApiConstants.REMOTE_JSON_RESULT_CODE_SUCCESS);
			json.put("dataCount", data.size());
			return json.toJSONString();
		}catch (Exception e) {
			e.printStackTrace();
			log.error("容厚调用科目接口参数：ukey="+ukey+",调用失败");
			log.error(e.getMessage());
			json.put("resultMsg", "调用失败！");
			json.put("dataCount", 0);
			return json.toJSONString();
		}
	}

	
	public List<String> findUnitIdByukey(String ukey){
		List<RhKeyUnit> list = SUtils.dt(rhKeyUnitRemoteService.findByUkey(ukey),new TR<List<RhKeyUnit>>() {});
		if(CollectionUtils.isNotEmpty(list)) {
			return EntityUtils.getList(list, e->e.getUnitId());
		}
		return new ArrayList<String>();
	}
	@RequestMapping("/rh/test1")
	public String test() {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		int count=0;
		int ii=1;
		while(true) {
				
			JSONObject jsonObject = new JSONObject();
			HttpEntity<String> requestEntity = new HttpEntity<String>(jsonObject.toJSONString(), headers);
			ResponseEntity<String> responseBody = restTemplate.exchange("http://192.168.0.223:8085/openapi/rh/student?ticketKey=15531358038655974960028700782040&ukey=1234&pageIndex="+ii, HttpMethod.GET, requestEntity, String.class);
			JSONObject json = Json.parseObject(responseBody.getBody());
			if(!Objects.equals(OpenApiConstants.REMOTE_JSON_RESULT_CODE_SUCCESS, json.get("result"))) {
				break;
			}
			if((int)json.get("dataCount")<=0) {
				break;
			}
			count=count+(int) json.get("dataCount");
			//System.out.println(json.get("dataCount"));
			ii++;
		}
		System.out.println("总共："+count);
		return "/desktop/login/login.ftl";
	}
}
