package net.zdsoft.scoremanage.data.action;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.exammanage.remote.service.ExamManageRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dto.ExamInfoSearchDto;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.JoinexamschInfo;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.data.service.ClassInfoService;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.ScoreInfoService;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Controller
@RequestMapping("/scoremanage")
public class ExamInfoAction extends BaseAction {
	
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private ScoreInfoService scoreInfoService;
	@Autowired
	private UnitRemoteService unitService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private SubjectInfoService courseInfoService;
	@Autowired
	private ClassInfoService classInfoService;
	@Autowired
	private SemesterRemoteService semesterService;
	@Autowired
	private CourseRemoteService courseService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private ExamManageRemoteService examManageRemoteService;

	@RequestMapping("/examInfo/index/page")
    @ControllerInfo(value = "考试信息设置", operationName="进入【考试信息】模块")
    public String showIndex(String searchAcadyear, String searchSemester, String searchType, ModelMap map, HttpSession httpSession) {
        List<String> acadyearList = SUtils.dt(semesterService.findAcadeyearList(), new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
		LoginInfo info = getLoginInfo(httpSession);
		String unitId = info.getUnitId();
		Semester semester = SUtils.dc(semesterService.getCurrentSemester(1,unitId), Semester.class);
		map.put("acadyearList", acadyearList);
		map.put("semester", semester);
		Unit unit = SUtils.dc(unitService.findOneById(unitId), Unit.class);
		map.put("unitClass", unit.getUnitClass());
		
		/*ExamInfoSearchDto searchDto = new ExamInfoSearchDto();
		List<ExamInfo> examInfoList = new ArrayList<ExamInfo>();
		if(StringUtils.isBlank(searchAcadyear)){
			searchAcadyear = semester.getAcadyear();
		}
		if(StringUtils.isBlank(searchSemester)){
			searchSemester = String.valueOf(semester.getSemester());
		}
		if(2 == unit.getUnitClass() && StringUtils.isBlank(searchType)){
			searchType = "2";
		}else if(1 == unit.getUnitClass() && StringUtils.isBlank(searchType)){
			searchType = "1";
		}
		searchDto.setSearchAcadyear(searchAcadyear);
		searchDto.setSearchSemester(searchSemester);
		searchDto.setSearchType(searchType);
		examInfoList = examInfoService.findExamInfoList(unitId,searchDto,null);
		map.put("examInfoList", examInfoList);*/
        return "/scoremanage/examInfo/examInfoIndex.ftl";
    }
	
	@RequestMapping("/examInfo/edit/page")
    @ControllerInfo(value = "新增或修改考试", operationName="新增/编辑考试信息")
    public String showExamInfo(String id,ExamInfoSearchDto searchDto,ModelMap map, HttpSession httpSession) {
		ExamInfo examInfo = null;
		LoginInfo info = getLoginInfo(httpSession);
		String unitId = info.getUnitId();
		Unit unit = SUtils.dc(unitService.findOneById(unitId), Unit.class);
		map.put("isEdu", unit.getUnitClass() != Unit.UNIT_CLASS_SCHOOL ?true:false);
		map.put("canEdit", true);
		if(StringUtils.isNotBlank(id)){
			examInfo = examInfoService.findExamInfoOne(id);
			//List<ClassInfo> findByExamInfoId = classInfoService.findByExamInfoId(id);
			//if(CollectionUtils.isNotEmpty(findByExamInfoId)){
				//map.put("canEdit", false);
			//}
		}else{
			examInfo = new ExamInfo();
			examInfo.setAcadyear(searchDto.getSearchAcadyear());
			examInfo.setSemester(searchDto.getSearchSemester());
			examInfo.setUnitId(unitId);
			McodeDetail mcodeDetail = SUtils.dc(mcodeRemoteService.findByMcodeAndThisId("DM-XQ", searchDto.getSearchSemester()), McodeDetail.class);
			//McodeSetting.newInstance().getMcode("DM-XQ", searchDto.getSearchSemester())
			examInfo.setExamName(searchDto.getSearchAcadyear()+mcodeDetail.getMcodeContent());
			Map<String, String> hMap = new HashMap<String, String>();
			hMap.put(unitId, unitId);
			examInfo.setLkxzSelectMap(hMap);
		}
		map.put("examInfo", examInfo);
		List<Unit> findAll = null;
        if(unit.getUnitClass() != Unit.UNIT_CLASS_SCHOOL){
        	//教育局
        	map.put("tklxMap", ScoreDataConstants.eduTklx);
        	findAll = SUtils.dt(unitService.findDirectUnits(unit.getId(), Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>(){});
        }else{
        	//学校
        	map.put("tklxMap", ScoreDataConstants.schTklx);
        	findAll = SUtils.dt(unitService.findDirectUnits(unit.getParentId(), Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>(){});
        }
        map.put("unitClass", unit.getUnitClass());
        map.put("unitList", findAll);
        //考试类型
        List<McodeDetail> findByMcodeId = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-KSLB"), new TR<List<McodeDetail>>(){});
        
        map.put("kslbList", findByMcodeId);
        //年级
		List<Grade> gradeList = new ArrayList<Grade>();
		//0,1,2,3,9
		Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-RKXD-0","DM-RKXD-1","DM-RKXD-2","DM-RKXD-3","DM-RKXD-9"}), new TR<Map<String, Map<String,McodeDetail>>>(){});
		if(unit.getUnitClass()==2){
			//学校
			School school = SUtils.dc(schoolRemoteService.findOneById(unitId), School.class);
			gradeList = getSchGradeList(findMapMapByMcodeIds,school);
			
		}else{
			//教育局所有学段
			List<McodeDetail> mcodelist = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-JYJXZ"), new TR<List<McodeDetail>>(){});
			gradeList=getEduGradeList(mcodelist,findMapMapByMcodeIds);
		}
		map.put("gradeList", gradeList);
		
        return "/scoremanage/examInfo/examInfoAdd.ftl";
    }
	
	private List<Grade> getSchGradeList(Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds,School school) {
		List<Grade> gradeList = new ArrayList<Grade>();
		String sections = school.getSections();
		if(StringUtils.isNotBlank(sections)){
			String[] sectionArr = sections.split(",");
			Integer yearLength = 0;
			Map<String, McodeDetail> map = null;
			for(String ss:sectionArr){
				int section = Integer.parseInt(ss);
				switch(section){
					case 0:
						yearLength = school.getInfantYear();
						map = findMapMapByMcodeIds.get("DM-RKXD-0");
						break;
					case 1:
						yearLength = school.getGradeYear();
						map = findMapMapByMcodeIds.get("DM-RKXD-1");
						break;
					case 2:
						yearLength = school.getJuniorYear();
						map = findMapMapByMcodeIds.get("DM-RKXD-2");
						break;
					case 3:
						yearLength = school.getSeniorYear();
						map = findMapMapByMcodeIds.get("DM-RKXD-3");
						break;
					case 9:
						yearLength = school.getSeniorYear();
						map = findMapMapByMcodeIds.get("DM-RKXD-9");
						break;
					default:
						yearLength=0;
						break;
				}
				if(yearLength==null || yearLength==0){
					continue;
				}
				for (int j = 0; j < yearLength; j++) {
					int grade = j + 1;
					Grade dto = new Grade();
					dto.setGradeCode(section+""+grade);
					if(map!=null && map.containsKey(grade+"")){
						dto.setGradeName(map.get(grade+"").getMcodeContent());
					}
					gradeList.add(dto);
				}
			}
		}
		Collections.sort(gradeList, new Comparator<Grade>() {
			public int compare(Grade o1, Grade o2) {
				return (o1.getGradeCode().compareToIgnoreCase(o2.getGradeCode()));
			}
		});
		return gradeList;
	}
	private List<Grade> getEduGradeList(List<McodeDetail> mcodelist,Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds){
		List<Grade> gradeList = new ArrayList<Grade>();
		// 取教育局学制微代码信息
		for (int i = 0; i < mcodelist.size(); i++) {
			McodeDetail detail =  mcodelist.get(i);
			int section = Integer.parseInt(detail.getThisId());
			String thisId=detail.getThisId();
			Map<String, McodeDetail> mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-"+thisId);
			if(mcodeMap==null || mcodeMap.size()<=0){
				continue;
			}
			int nz = Integer.parseInt(detail.getMcodeContent());// 年制
			for (int j = 0; j < nz; j++) {
				int grade = j + 1;
				Grade dto = new Grade();
				dto.setGradeCode(section+""+grade);
				if(mcodeMap.containsKey(grade+"")){
					dto.setGradeName(mcodeMap.get(grade+"").getMcodeContent());
				}
				gradeList.add(dto);
			}
		}
		Collections.sort(gradeList, new Comparator<Grade>() {
			public int compare(Grade o1, Grade o2) {
				return (o1.getGradeCode().compareToIgnoreCase(o2.getGradeCode()));
			}
		});
		return gradeList;
	}
	
	@RequestMapping("/examInfo/list/page")
    @ControllerInfo("考试列表")
    public String showExamInfoPage(String searchAcadyear, String searchSemester, String searchType, ModelMap map, HttpServletRequest request, HttpSession httpSession){
		ExamInfoSearchDto searchDto = new ExamInfoSearchDto();
		List<ExamInfo> examInfoList = new ArrayList<ExamInfo>();
		LoginInfo info = getLoginInfo(httpSession);
		String unitId = info.getUnitId();
		searchDto.setSearchAcadyear(searchAcadyear);
		searchDto.setSearchSemester(searchSemester);
		searchDto.setSearchType(searchType);
		Pagination page = createPagination();
		examInfoList = examInfoService.findExamInfoList(unitId,searchDto,page);

		map.put("searchType", searchType);
        map.put("Pagination", page);

        sendPagination(request, map, page);
          
		map.put("examInfoList", examInfoList);
		return "/scoremanage/examInfo/examInfoList.ftl";
    }
	
	@ResponseBody
	@RequestMapping("/examInfo/list")
	@ControllerInfo("查看单位内考试数据")
	public String showExamInfoBy(ExamInfoSearchDto searchDto, ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession sesion) {
		LoginInfo loginInfo = getLoginInfo(sesion);
		String unitId = loginInfo.getUnitId();
		Pagination page = createPaginationJqGrid(request);
		List<ExamInfo> examInfoList = new ArrayList<ExamInfo>();
		examInfoList = examInfoService.findExamInfoList(unitId,searchDto,page);
		return returnJqGridData(page, examInfoList);
	}
	
	@ResponseBody
	@RequestMapping("/examInfo/findList")
	@ControllerInfo(ignoreLog=1, value="获取考试数据")
	public String showFindExamInfoList(ExamInfoSearchDto searchDto, ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession sesion) {
		LoginInfo loginInfo = getLoginInfo(sesion);
		String unitId = loginInfo.getUnitId();
		List<ExamInfo> examInfoList = new ArrayList<ExamInfo>();
		examInfoList = examInfoService.findExamInfoList(unitId,searchDto,null);
		makeRangeCode(examInfoList);
		return Json.toJSONString(examInfoList,SerializerFeature.DisableCircularReferenceDetect);
	}
	@ResponseBody
	@RequestMapping("/examInfo/findByExamId")
	@ControllerInfo("获取某个考试数据")
	public String findByExamId(String examId) {
		ExamInfo examInfo = examInfoService.findOne(examId);
		if(examInfo!=null){
			//组装年级code名称
			List<ExamInfo> infoList=new ArrayList<ExamInfo>();
			infoList.add(examInfo);
			makeRangeCode(infoList);
			return Json.toJSONString(examInfo);
		}else{
			return null;
		}
	}
	
	private Map<String,String> getAllgradeCode(){
		String rediskey="gradeCodeNameMap";
		Map<String,String> returnMap = RedisUtils.getObject(rediskey, RedisUtils.TIME_HALF_HOUR, new TypeReference<Map<String,String>>(){},new RedisInterface<Map<String,String>>() {

			@Override
			public Map<String, String> queryData() {
				Map<String,String> returnMap=new HashMap<String,String>();
				Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-RKXD-0","DM-RKXD-1","DM-RKXD-2","DM-RKXD-3","DM-RKXD-9"}), new TR<Map<String, Map<String,McodeDetail>>>(){});
				if(findMapMapByMcodeIds!=null && findMapMapByMcodeIds.size()>0){
					for(Entry<String, Map<String, McodeDetail>> item:findMapMapByMcodeIds.entrySet()){
						String key = item.getKey();
						String kk="";
						if(key.startsWith("DM-RKXD-")){
							kk=key.substring(8);
						}
						Map<String, McodeDetail> valueMap = item.getValue();
						if(valueMap!=null && valueMap.size()>0){
							for(Entry<String, McodeDetail> item1:valueMap.entrySet()){
								String key1 = item1.getKey();
								McodeDetail value = item1.getValue();
								returnMap.put(kk+key1, value.getMcodeContent());
							}
						}
					}
				}
				return returnMap;
			}
		});
		
		return returnMap;
	}
	
	private void makeRangeCode(List<ExamInfo> examInfoList){
		if(CollectionUtils.isNotEmpty(examInfoList)){
			Map<String, String> returnMap = getAllgradeCode();
			for(ExamInfo e:examInfoList){
				if(StringUtils.isNotBlank(e.getRanges())){
					String[] rangeArr=null;
					if(e.getRanges().indexOf(",")>=0){
						rangeArr=e.getRanges().split(",");
					}else{
						rangeArr=new String[]{e.getRanges()};
					}
					//排序
					Arrays.sort(rangeArr);
					List<String[]> ll=new ArrayList<String[]>();
					for(String s:rangeArr){
						if(returnMap.containsKey(s)){
							ll.add(new String[]{s,returnMap.get(s)});
						}
					}
					e.setRangeCodeName(ll);
				}
			}
		}
	}
	
	
	@ResponseBody
	@RequestMapping("/examInfo/findSubjectList")
	@ControllerInfo("获取考试下科目")
	public String showFindExamInfoSubjectList(ExamInfoSearchDto searchDto, ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession sesion) {
		List<SubjectInfo> infoList = courseInfoService.findByExamIdIn(null, new String[]{searchDto.getSearchExamId()});
		Set<String> subjectIds = new HashSet<String>();
		for (SubjectInfo item : infoList) {
			subjectIds.add(item.getSubjectId());
		}
		List<Course> courses = SUtils.dt(courseService.findListByIds(subjectIds.toArray(new String[0])), new TR<List<Course>>(){});
		return Json.toJSONString(courses,SerializerFeature.DisableCircularReferenceDetect);
	}
	
	@ResponseBody
    @RequestMapping("/examInfo/save")
    @ControllerInfo(value = "保存考试", operationName="保存考试信息" )
    public String doSaveExam(ExamInfo examInfo) {
		try{
			//examInfo.setExamUeType("1");
			if(StringUtils.isBlank(examInfo.getId())){
				examInfo.setId(UuidUtils.generateUuid());
				//考试编号 当前年度+6位流水号
				Calendar calendar=Calendar.getInstance();
				int year = calendar.get(Calendar.YEAR);
				List<String> findExamCodeMax = examInfoService.findExamCodeMax();
				if(findExamCodeMax.size()>0){
					String string = findExamCodeMax.get(0);
					if(string.substring(0, 4).equals(String.valueOf(year))){
						Integer intValue = Integer.valueOf(string.substring(4, string.length()))+1;
						DecimalFormat countFormat=new DecimalFormat("000000");
						String strValue = countFormat.format(intValue);
						examInfo.setExamCode(year+strValue);
					}else{
						examInfo.setExamCode(year+"000001");
					}
				}else{
					examInfo.setExamCode(year+"000001");
				}
			}else{
				ExamInfo examInfoOld = examInfoService.findOne(examInfo.getId());
				EntityUtils.copyProperties(examInfo, examInfoOld, true);
				examInfo = examInfoOld;
			}
			//处理校校联考
			List<JoinexamschInfo> joinexamschInfoAddList = new ArrayList<JoinexamschInfo>();
			if(ScoreDataConstants.TKLX_3.equals(examInfo.getExamUeType())){
				JoinexamschInfo joinexamschInfo = null;
				for(String item : examInfo.getLkxzSelect()){
					joinexamschInfo = new JoinexamschInfo();
					joinexamschInfo.setExamInfoId(examInfo.getId());
					joinexamschInfo.setSchoolId(item);
					joinexamschInfoAddList.add(joinexamschInfo);
				}
			}
			examInfoService.saveExamInfoOne(examInfo,joinexamschInfoAddList);
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
    }
	
	@ResponseBody
	@RequestMapping("/examInfo/delete")
	@ControllerInfo(value="删除考试：{id}", operationName="删除考试信息")
	public String doDeleteExamInfo(String id, ModelMap map, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		try{
			List<SubjectInfo> findByExamIdIn = courseInfoService.findByExamIdIn(null, id);
			if(CollectionUtils.isNotEmpty(findByExamIdIn)){
				return error("已被考试科目引用无法删除！");
			}
			examInfoService.deleteAllIsDeleted(id);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return success("删除成功");
	}
	@ResponseBody
	@RequestMapping("/examInfo/synch")
	@ControllerInfo(value="同步考试内容 （包括成绩信息）：{id}", operationName="同步考试信息")
	public String doSynchExamInfo(String id, ModelMap map, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		try{
			ExamInfo examInfo = examInfoService.findOne(id);
			List<ScoreInfo> scoreList=scoreInfoService.findByExamId(id);
			List<SubjectInfo> subInfoList=courseInfoService.findByExamId(id);
			JSONObject json=new JSONObject();
			json.put("exam", examInfo);
			json.put("scoreList", scoreList);
			json.put("subInfoList", subInfoList);
			examManageRemoteService.doSynch(json.toJSONString());
			examInfo.setHaveSynch("1");
			examInfoService.save(examInfo);
		}catch(Exception e){
			e.printStackTrace();
			return error("同步失败！"+e.getMessage());
		}
		return success("同步成功");
	}
}
