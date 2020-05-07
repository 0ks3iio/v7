package net.zdsoft.teacherasess.data.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.exammanage.data.dto.EmExamInfoSearchDto;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.service.EmExamInfoService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.teacherasess.data.dto.GroupDto;
import net.zdsoft.teacherasess.data.dto.TeaConvertDto;
import net.zdsoft.teacherasess.data.dto.TeaConvertExamDto;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvert;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvertResult;
import net.zdsoft.teacherasess.data.service.TeacherasessConvertResultService;
import net.zdsoft.teacherasess.data.service.TeacherasessConvertService;

@Controller
@RequestMapping("/teacherasess/convert")
public class TeacherasessConvertAction extends BaseAction {
	@Autowired
	private TeacherasessConvertService teacherasessConvertService;
	@Autowired
	private TeacherasessConvertResultService teacherasessConvertResultService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private EmExamInfoService emExamInfoService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	
	@RequestMapping("/showResult/page")
	@ControllerInfo(value = "查看结果")
	public String showResult(String convertId,String subjectId,ModelMap map){
		TeaConvertDto dto = teacherasessConvertService.findDtoByConvertId(convertId);
		map.put("dto", dto);
		//得到语数英+7选3的单科
		String unitId = getLoginInfo().getUnitId();
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73YSY(unitId), new TR<List<Course>>() {});
		Course allCourse = new Course();
		allCourse.setId(BaseConstants.ZERO_GUID);
		allCourse.setSubjectName("总分");
		courseList.add(allCourse);
		map.put("courseList", courseList);
		if(StringUtils.isBlank(subjectId)) {
			subjectId = courseList.get(0).getId();
		}
		map.put("subjectId",subjectId);
		return "/teacherasess/convert/showResult.ftl";
	}
	
	@RequestMapping("/showResult/list/page")
	@ControllerInfo(value = "查看结果List")
	public String showListPage(String convertId, String subjectId,ModelMap map,HttpServletRequest request){
		String unitId=getLoginInfo().getUnitId();
		map.put("unitId", unitId);
		Pagination page = createPagination();
		List<TeacherasessConvertResult> resultList = teacherasessConvertResultService.findListByConvertId(convertId,subjectId,page);
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(unitId), new TR<List<Course>>() {});
		if(CollectionUtils.isNotEmpty(courseList)){
			if(courseList.stream().map(Course::getId).collect(Collectors.toSet()).contains(subjectId)){
				map.put("is73Sub", true);
			}
		}
		TeacherasessConvert convert = teacherasessConvertService.findOne(convertId);
		if(StringUtils.isNotBlank(convert.getXuankaoType())&&StringUtils.equals(convert.getXuankaoType(), "1")){
			map.put("isShow", true);
		}else{
			map.put("isShow", false);
		}
		map.put("resultList",resultList);
		map.put("Pagination", page);
		sendPagination(request, map, page);
		return "/teacherasess/convert/convertResult.ftl";
	}
	@RequestMapping("/showResult/export")
	@ControllerInfo(value = "导出结果List")
	public void exportResult(String convertId, String subjectId,ModelMap map,HttpServletRequest request){
		
		TeaConvertDto dto = teacherasessConvertService.findDtoByConvertId(convertId);
		List<TeacherasessConvertResult> resultList = teacherasessConvertResultService.findListByConvertId(convertId,subjectId,null);
		
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		if(CollectionUtils.isNotEmpty(resultList)){
			Map<String,String> valueMap = null;
			for(TeacherasessConvertResult result:resultList){
				valueMap=new HashMap<String, String>();
				valueMap.put("学号", result.getStudentCode());
				valueMap.put("姓名",result.getStudentName());
				valueMap.put("行政班",result.getClassName());
				BigDecimal  b=new  BigDecimal(result.getScore());  
				valueMap.put("折算分",b.setScale(1,  BigDecimal.ROUND_HALF_UP).toString());
				valueMap.put("年级排名",result.getRank()+"");
				recordList.add(valueMap);
			}
		}
		List<String> tis = new ArrayList<String>();
		tis.add("学号");
		tis.add("姓名");
		tis.add("行政班");
		tis.add("折算分");
		tis.add("年级排名");
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		sheetName2RecordListMap.put("exportResult",recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put("exportResult", tis);
		ExportUtils exportUtils = ExportUtils.newInstance();
		
		exportUtils.exportXLSFile(dto.getAcadyear()+"学年"+dto.getGradeName()+"年级"+dto.getConvertName()+"折算总分导出", titleMap, sheetName2RecordListMap, getResponse());
		
	}
	
	@ResponseBody
	@RequestMapping("/doDel")
	@ControllerInfo(value = "统计")
	public String doDel(String convertId,ModelMap map){
		try {
			TeacherasessConvert convert = teacherasessConvertService.findOne(convertId);
			convert.setIsDeleted(1);
			teacherasessConvertService.save(convert);
		}catch (Exception e) {
			return error("操作失败");
		}
		return success("操作成功");
	}
	
	@ResponseBody
	@RequestMapping("/stat")
	@ControllerInfo(value = "统计")
	public String classStat(final String convertId,ModelMap map){
		//先判断所选周次是否已经结束，如果没结束则不能统计
		final String unitId = getLoginInfo().getUnitId();
		final String key = unitId+"_"+convertId;
		final String typekey = unitId+"_"+convertId+"_type";
		if(RedisUtils.get(key)==null){
			RedisUtils.set(typekey,"校验");
			RedisUtils.set(key,"start");
		}else{
			JSONObject jsonObject=new JSONObject();;
			jsonObject.put("type", RedisUtils.get(key));
			if("success".equals(RedisUtils.get(key)) || "error".equals(RedisUtils.get(key))){
				RedisUtils.del(typekey);
				RedisUtils.del(key);
			}
			return JSON.toJSONString(jsonObject);
		}
		new Thread(new Runnable(){  
			public void run(){
				Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
				try {
					TeacherasessConvert convert = teacherasessConvertService.findOneWithMaster(convertId);
					convert.setStatus("1");
					teacherasessConvertService.save(convert);
					teacherasessConvertResultService.statResult(convertId, semester.getAcadyear(), semester.getSemester()+"");
					RedisUtils.set(key,"success");
				}catch (Exception e) {
					RedisUtils.set(key,"error");
				}
				return;
			}
		}).start();
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("type", RedisUtils.get(key));
		if("success".equals(RedisUtils.get(key)) || "error".equals(RedisUtils.get(key))){
			RedisUtils.del(typekey);
			RedisUtils.del(key);
		}
		return success("操作成功");
	}
	
	@ResponseBody
    @RequestMapping("/save")
    @ControllerInfo(value = "保存考试")
    public String doSaveExam(TeaConvertDto dto,String type,ModelMap map) {
		try{
			if(StringUtils.isBlank(dto.getConvertName())) {
				return error("方案名称为空");
			}
			if(CollectionUtils.isEmpty(dto.getExamDtos())) {
				return error("请选择考试");
			}
			List<TeaConvertExamDto> examDtos = dto.getExamDtos();
			for(int i =0;i<examDtos.size();i++) {
				if(StringUtils.isBlank(examDtos.get(i).getAcadyear())){
					examDtos.remove(i);
					i--;
				}
			}
			if(CollectionUtils.isEmpty(examDtos)) {
				return error("请选择考试");
			}
			float scale = 0f;
			Set<String> examIds = EntityUtils.getSet(examDtos, TeaConvertExamDto::getExamId);
			if(examIds.size() != examDtos.size()) {
				return error("所选考试有重复");
			}
			
			for (TeaConvertExamDto e : examDtos) {
				if(StringUtils.isBlank(e.getExamId()) || StringUtils.isBlank(e.getScale())) {
					return error("考试或占比为空");
				}
				scale = scale + NumberUtils.toFloat(e.getScale());
			}
			if(scale!=100) {
				return error("占比之和必须为100%");
			}
			if(examIds.size()==3){
				List<GroupDto> groupList = dto.getGroupList();
				if(CollectionUtils.isEmpty(groupList) || StringUtils.isBlank(groupList.get(0).getExamId1())){
					return error("请设置组合比例");
				}
				for(GroupDto group:groupList){
					scale=NumberUtils.toFloat(group.getScale1())+NumberUtils.toFloat(group.getScale2());
					if(scale!=100) {
						return error(group.getGroupName()+"的占比之和必须为100%");
					}
				}
			}else{
				dto.setGroupList(null);
			}
			
			String unitId=getLoginInfo().getUnitId();
			Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
	    	String acadyear = semester.getAcadyear();
			dto.setUnitId(unitId);
			dto.setAcadyear(acadyear);
			dto.setStatus(type);
			String convertId = teacherasessConvertService.saveDto(dto);
			if(StringUtils.equals(type, "2")) {
				classStat(convertId,map);
			}
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
    }
	
	@RequestMapping("/addConvert/page")
	@ControllerInfo(value = "新增折算分方案")
	public String addConvert(ModelMap map){
		String unitId=getLoginInfo().getUnitId();
		map.put("unitId", unitId);
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
    	String acadyear = semester.getAcadyear();
    	List<Grade> grades = SUtils.dt(gradeRemoteService.findBySchoolId(unitId), new TR<List<Grade>>() {});
    	map.put("acadyear", acadyear);
    	map.put("grades",grades);
		return "/teacherasess/convert/convertAdd.ftl";
	}
	//计算可以选择的学年
	@ResponseBody
	@RequestMapping("/queryAcadyear")
	public String queryAcadyer(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		String gradeId =  request.getParameter("gradeId");
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
		int le = NumberUtils.toInt(grade.getGradeCode().substring(1));
		String openAcadyear = grade.getOpenAcadyear();
		List<String> list = new ArrayList<>();
		int ac1 = NumberUtils.toInt(openAcadyear.split("-")[0]);
		int ac2 = NumberUtils.toInt(openAcadyear.split("-")[1]);
		for(int i=le-1;0<=i;i--) {
			int ac3 = ac1 + i;
			int ac4 = ac2 + i;
			list.add(ac3+"-"+ac4);
		}
		json.put("list", list);
		return json.toJSONString();
	}
	//查询考试
	@ResponseBody
	@RequestMapping("/queryExam")
	public String queryExam(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		String unitId=getLoginInfo().getUnitId();
		String gradeId =  request.getParameter("gradeId");
		String acadyear =  request.getParameter("acadyear");
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
		int section = grade.getSection();
		String openAcadyear = grade.getOpenAcadyear();
		int ac1 = NumberUtils.toInt(openAcadyear.split("-")[0]);
		int ac2 = NumberUtils.toInt(acadyear.split("-")[1]);
		String gradeCode = section+""+(ac2-ac1);
		EmExamInfoSearchDto searchDto = new EmExamInfoSearchDto();
		searchDto.setSearchAcadyear(acadyear);
		searchDto.setSearchGradeCode(gradeCode);
		List<EmExamInfo> examlist = emExamInfoService.findExamList(null, unitId, searchDto, false);
		List<JSONObject> infolist = new ArrayList<>();
		for (EmExamInfo e : examlist) {
			JSONObject info = new JSONObject();
			info.put("id", e.getId());
			info.put("examCode", e.getExamCode());
			info.put("name", e.getExamName());
			infolist.add(info);
		}
		json.put("list", infolist);
		return json.toJSONString();
	}
	@RequestMapping("/index/page")
	@ControllerInfo(value = "折算分方案")
	public String showConList(HttpServletRequest request,ModelMap map){
		String unitId=getLoginInfo().getUnitId();
		map.put("unitId", unitId);
		//学年学期
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        map.put("acadyearList", acadyearList);
        String acadyear = request.getParameter("acadyear");
        if(StringUtils.isBlank(acadyear)) {
        	Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
        	acadyear = semester.getAcadyear();
        }
        map.put("acadyear", acadyear);
		List<TeaConvertDto> dtolist = teacherasessConvertService.findDtoListByAcadyearWithMaster(unitId,acadyear);
		map.put("dtolist", dtolist);
		List<String> ids = new ArrayList<>();
		for (TeaConvertDto dto : dtolist) {
			if(StringUtils.equals(dto.getStatus(), "1")) {
				ids.add(dto.getConvertId());
			}
		}
		map.put("ids", ids);
		return "/teacherasess/convert/convertIndex.ftl";
	}
	
	
	
}
