package net.zdsoft.newgkelective.data.action;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ArrayUtil;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.NewGkChoResultDto;
import net.zdsoft.newgkelective.data.dto.NewGkConditionDto;
import net.zdsoft.newgkelective.data.dto.NewGkReportBaseDto;
import net.zdsoft.newgkelective.data.dto.NewGkUnitMake;
import net.zdsoft.newgkelective.data.dto.PageInfo;
import net.zdsoft.newgkelective.data.dto.ReportDivideSaveDto;
import net.zdsoft.newgkelective.data.dto.ReportUnitDto;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkOpenSubject;
import net.zdsoft.newgkelective.data.entity.NewGkReport;
import net.zdsoft.newgkelective.data.entity.NewGkReportBase;
import net.zdsoft.newgkelective.data.entity.NewGkReportChose;
import net.zdsoft.newgkelective.data.entity.NewGkReportDivide;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;
import net.zdsoft.newgkelective.data.entity.NewGkplaceArrange;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkChoiceService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkOpenSubjectService;
import net.zdsoft.newgkelective.data.service.NewGkReportBaseService;
import net.zdsoft.newgkelective.data.service.NewGkReportChoseService;
import net.zdsoft.newgkelective.data.service.NewGkReportDivideService;
import net.zdsoft.newgkelective.data.service.NewGkReportService;
import net.zdsoft.newgkelective.data.service.NewGkTeacherPlanService;
import net.zdsoft.newgkelective.data.service.NewGkplaceArrangeService;
import net.zdsoft.newgkelective.data.utils.CombineAlgorithmInt;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.TypeReference;


@Controller
@RequestMapping("/newgkelective")
public class NewGkReportAction extends BaseAction{
	
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkReportService newGkReportService;
	@Autowired
	private NewGkReportChoseService newGkReportChoseService;
	@Autowired
    private NewGkChoiceService newGkChoiceService;
	@Autowired
    private StudentRemoteService studentRemoteService;
	@Autowired
    private NewGkChoRelationService newGkChoRelationService;
	@Autowired
    private NewGkChoResultService newGkChoResultService;
	@Autowired
	private NewGkReportBaseService newGkReportBaseService;
    @Autowired
	private NewGkReportDivideService newGkReportDivideService;
	@Autowired
	private NewGkTeacherPlanService newGkTeacherPlanService;
	@Autowired
	private NewGkplaceArrangeService newGkplaceArrangeService;
	@Autowired
	private NewGkDivideService newGkDivideService;
	@Autowired
	private NewGkDivideClassService newGkDivideClassService;
	@Autowired
	private NewGkOpenSubjectService newGkOpenSubjectService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	
	
	@RequestMapping("/report/query/index/page")
	@ControllerInfo(value = "查看首页")
	public String index(String type,ModelMap map) {
		//1:选课 2：分班 3：资源
		if("1".equals(type) || "2".equals(type) || "3".equals(type)) {
			map.put("type", type);
			return "/newgkelective/reportEdu/reportEduHead.ftl";
		}else {
			return errorFtl(map, "路径不存在");
		}
	}
	/**
	 * 查出年级列表
	 * 上报中的所有年级+3个当前年级
	 * @param newSemester
	 * @return
	 */
	public List<String[]> findGradeList(Semester newSemester){
		List<String> list = newGkReportService.findAllOpenAcadyear();
		List<Integer> yearList=new ArrayList<>();
		if(CollectionUtils.isNotEmpty(list)) {
			for(String s:list) {
				int ss1 = Integer.parseInt(s.split("-")[0]);
				yearList.add(ss1);
			}
		}
		//默认高中
		String newAcadyear = newSemester.getAcadyear();
		int year=Integer.parseInt(newAcadyear.split("-")[0]);
		int year1=year-1;
		int year2=year-2;
		if(!yearList.contains(year)) {
			yearList.add(year);
		}
		if(!yearList.contains(year1)) {
			yearList.add(year1);
		}
		if(!yearList.contains(year2)) {
			yearList.add(year2);
		}
		//倒序
		List<String[]> gradeCodeList=new ArrayList<>();
		Collections.sort(yearList);
		Collections.reverse(yearList);
		for(Integer ii:yearList) {
			if(ii==year) {
				gradeCodeList.add(new String[] {ii+"-"+(ii+1),ii+"级(现高一)"});
				continue;
			}
			if(ii==year1) {
				gradeCodeList.add(new String[] {ii+"-"+(ii+1),ii+"级(现高二)"});
				continue;
			}
			if(ii==year2) {
				gradeCodeList.add(new String[] {ii+"-"+(ii+1),ii+"级(现高三)"});
				continue;
			}
			gradeCodeList.add(new String[] {ii+"-"+(ii+1),ii+"级"});
		}
		return gradeCodeList;
	}
	
	
	@RequestMapping("/edu/itemList/page")
	public String itemList(String type,ModelMap map) {
		map.put("type", type);
		Semester newSemester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
		if(newSemester==null) {
			return errorFtl(map, "学年学期数据有误，请联系管理员");
		}
		List<String[]> gradeCodeList = findGradeList(newSemester);
		map.put("gradeCodeList", gradeCodeList);
		map.put("unitId", getLoginInfo().getUnitId());
		return "/newgkelective/reportEdu/reportEduIndex.ftl";
	}
	
	@RequestMapping("/edu/unittree/page")
	@ControllerInfo(value = "单位树结构")
	public String findUnitTree(String type,String gradeYear,ModelMap map) {
		String unitId=getLoginInfo().getUnitId();
		List<NewGkUnitMake> dtoList = findUnitList(unitId);
		String[] unitIds = EntityUtils.getList(dtoList, e->e.getUnit().getId()).toArray(new String[0]);
		Map<String,List<NewGkUnitMake>> unitMakeMap=new HashMap<>();
		Set<String> reportUnitIdSet=new HashSet<>();
		List<NewGkReport> filterList = newGkReportService.findListBy(unitIds, gradeYear);
		if("1".equals(type)){
			filterList = filterList.stream().filter(e->e.getIsChosen()==1).collect(Collectors.toList());
		}else if("2".equals(type)){
			filterList = filterList.stream().filter(e->e.getIsDivide()==1).collect(Collectors.toList());
		}else if("3".equals(type)){
			filterList = filterList.stream().filter(e->e.getIsBasic()==1).collect(Collectors.toList());
		}
		if(CollectionUtils.isNotEmpty(filterList)) {
			reportUnitIdSet=EntityUtils.getSet(filterList, NewGkReport::getUnitId);
		}
		int max=1;
		for(NewGkUnitMake u:dtoList) {
			if(!unitMakeMap.containsKey(String.valueOf(u.getIndex()))) {
				unitMakeMap.put(String.valueOf(u.getIndex()), new ArrayList<>());
			}
			unitMakeMap.get(String.valueOf(u.getIndex())).add(u);
			if(max<u.getIndex()) {
				max=u.getIndex();
			}
			if(reportUnitIdSet.contains(u.getUnit().getId())) {
				u.setReport(true);
			}
		}
		//先教育局后学校
		for(Entry<String, List<NewGkUnitMake>> kk:unitMakeMap.entrySet()) {
			if(CollectionUtils.isNotEmpty(kk.getValue())) {
				Collections.sort(kk.getValue(), new Comparator<NewGkUnitMake>() {

					@Override
					public int compare(NewGkUnitMake o1, NewGkUnitMake o2) {
						if(o2.getUnit().getUnitClass()==null) {
							return -1;
						}
						if(o1.getUnit().getUnitClass()==null) {
							return 0;
						}
						return o1.getUnit().getUnitClass()-o2.getUnit().getUnitClass();
					}
				});
			}
		}
		map.put("unitMakeMap", unitMakeMap);
		map.put("maxIndex", max);
		map.put("type", type);
		return "/newgkelective/reportEdu/reportUnitTree.ftl";
	}
	@RequestMapping("/edu/noreport/page")
	public String findNoReport(String gradeYear,String type,ModelMap map) {
		String unitId = getLoginInfo().getUnitId();
		List<Unit> unitList=findEduListByEduUnitId(unitId);
		Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
		map.put("unitName", unit.getUnitName());
		map.put("unitList", unitList);
		map.put("gradeYear", gradeYear);
		map.put("type", type);
		return "/newgkelective/reportEdu/noReportUnit.ftl";
	}
	@RequestMapping("/edu/noreportList/page")
	public String findNoReportList(String unitId,String gradeYear,String type,PageInfo pageInfo,ModelMap map) {
		//分页
		if(StringUtils.isBlank(unitId)) {
			//全部
			unitId=getLoginInfo().getUnitId();
		}
		List<Unit> unitList=findSchoolListByEduUnitId(unitId);
		Set<String> reportUnitIdSet=new HashSet<>();
		boolean isShowTime=false;
		Map<String, NewGkReport> reportMap=new HashMap<>();
		if(CollectionUtils.isNotEmpty(unitList)) {
			String[] unitIds = EntityUtils.getList(unitList, Unit::getId).toArray(new String[0]);
			if("1".equals(type)) {
				//选课
				List<NewGkReport> filterList =newGkReportService.findListBy(unitIds,gradeYear);
				filterList = filterList.stream().filter(e->e.getIsChosen()==1).collect(Collectors.toList());
				if(CollectionUtils.isNotEmpty(filterList)) {
					reportUnitIdSet=EntityUtils.getSet(filterList, NewGkReport::getUnitId);
				}
			}else if("2".equals(type) || "3".equals(type)) {
				//分班
				List<NewGkReport> filterList =newGkReportService.findListBy(unitIds,gradeYear);
				if("2".equals(type)){
					filterList = filterList.stream().filter(e->e.getIsDivide()==1).collect(Collectors.toList());
				}else{
					filterList = filterList.stream().filter(e->e.getIsBasic()==1).collect(Collectors.toList());
					
				}
				reportUnitIdSet=EntityUtils.getSet(filterList, NewGkReport::getUnitId);
				reportMap = EntityUtils.getMap(filterList,NewGkReport::getUnitId);
				isShowTime=true;
			}else {
				return errorFtl(map, "参数不对");
			}
		}
		List<Unit> returnList=new ArrayList<>();
		for(Unit u:unitList) {
			if(reportUnitIdSet.contains(u.getId())) {
				continue;
			}
			returnList.add(u);
		}
		//分页
		int allNum=returnList.size();
		pageInfo=initPage(pageInfo, allNum);
		map.put("allNum", allNum);
		returnList = returnList.stream().skip(pageInfo.getStartIndex()-1)
				.limit(pageInfo.getPageSize())
				.collect(Collectors.toList());
		map.put("pageInfo", pageInfo);
		
		if(CollectionUtils.isNotEmpty(returnList)) {
			Set<String> set = EntityUtils.getSet(returnList, e->e.getId());
			List<School> schoolList = SUtils.dt(schoolRemoteService.findListByIds(set.toArray(new String[] {})), School.class);
			Map<String,School> schoolMap=new HashMap<>();
			if(CollectionUtils.isNotEmpty(schoolList)) {
				schoolMap=EntityUtils.getMap(schoolList, School::getId);
			}
			map.put("unitDtoList", makeUnitDto(returnList,reportMap,schoolMap));
		}else {
			map.put("unitDtoList", new ArrayList<ReportUnitDto>());
		}
		map.put("type", type);
		map.put("isShowTime", isShowTime);
		map.put("gradeYear", gradeYear);
		return "/newgkelective/reportEdu/noReportUnitList.ftl";
	}
	
	public List<ReportUnitDto> makeUnitDto(List<Unit> unitList,Map<String, NewGkReport> reportMap,Map<String,School> schoolMap){
		List<ReportUnitDto> list=new ArrayList<>();
		Set<String> unitIds = EntityUtils.getSet(unitList, Unit::getParentId);
		List<Unit> parentUnits = SUtils.dt(unitRemoteService.findListByIds(unitIds.toArray(new String[] {})), Unit.class);
		Map<String, Unit> unitMap = EntityUtils.getMap(parentUnits, Unit::getId);
		ReportUnitDto e;
		NewGkReport report;
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		for(Unit u:unitList) {
			e=new ReportUnitDto();
			if(unitMap.containsKey(u.getParentId())) {
				e.setParentUnitName(unitMap.get(u.getParentId()).getUnitName());
			}
			if(schoolMap.containsKey(u.getId())) {
				School school = schoolMap.get(u.getId());
				if(StringUtils.isNotBlank(school.getSchoolmaster())) {
					e.setSchoolLeader(school.getSchoolmaster());
				}
				if(StringUtils.isNotBlank(school.getLinkPhone())) {
					e.setTelephoneNumber(school.getLinkPhone());
				}
			}
			if(StringUtils.isBlank(e.getSchoolLeader())) {
				e.setSchoolLeader(u.getUnitHeader());
			}
			if(StringUtils.isBlank(e.getTelephoneNumber())) {
				e.setSchoolLeader(u.getMobilePhone());
			}
			e.setSchoolName(u.getUnitName());
			//2018-10-10  10:10  ~  2018-10-10  10:10
			if(reportMap.containsKey(u.getId())) {
				report = reportMap.get(u.getId());
				e.setChooseTime(f.format(report.getChooseStartTime())+"~"+f.format(report.getChooseEndTime()));
			}
			list.add(e);
		}
		return list;
	}
	@RequestMapping("/edu/loadRightContent/page")
	public String loadRightByUnit(String unitId,String type,String gradeYear,ModelMap map) {
		if("1".equals(type)) {
			return findChoseByUnitId(unitId, gradeYear, map);
		}else if("2".equals(type)){
			return findDivideByUnitId(unitId, gradeYear,null, map);
		}else if("3".equals(type)) {
			return baseItemIndex(unitId, gradeYear, map);
		}
		return errorFtl(map, "资源不存在");
	}
	
	@RequestMapping("/edu/compareChoose/page")
	@ControllerInfo(value = "选课对比")
	public String compareChoose(String compareIds,String gradeYear,ModelMap map) {
		String[] arr=compareIds.split(",");
		List<Unit> unitList = SUtils.dt(unitRemoteService.findListByIds(arr), Unit.class);
		if(unitList.size()!=2) {
			return errorFtl(map, "单位数据有误");
		}
		Map<String, Unit> unitMap = EntityUtils.getMap(unitList, Unit::getId);
		//key:总人数 32个0 key:subjectId key:subjectIds
		Map<String,Integer> dataContentNum1Map1=new HashMap<>();
		statChooseResultByUnit(unitMap.get(arr[0]), gradeYear, dataContentNum1Map1);
		Map<String,Integer> dataContentNum1Map2=new HashMap<>();
		statChooseResultByUnit(unitMap.get(arr[1]), gradeYear, dataContentNum1Map2);
		
		String[] names=new String[] {unitMap.get(arr[0]).getUnitName(),unitMap.get(arr[1]).getUnitName()};
		map.put("names", names);
		map.put("compareIdArr", arr);
		List<String> course1=new ArrayList<>();
		Map<String,List<String[]>> chooseCourse1=new HashMap<>();

		for(String sid:arr) {
			chooseCourse1.put(sid, new ArrayList<>());
		}
		
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()), Course.class);
		List<Course> returnCourseList=new ArrayList<>();
		int allStu1=dataContentNum1Map1.get(BaseConstants.ZERO_GUID)==null?0:dataContentNum1Map1.get(BaseConstants.ZERO_GUID);
		int allStu2=dataContentNum1Map2.get(BaseConstants.ZERO_GUID)==null?0:dataContentNum1Map2.get(BaseConstants.ZERO_GUID);
		DecimalFormat df = new DecimalFormat("0.00");
		List<Integer> maxSingleList=new ArrayList<>();
		for(Course c:courseList) {
			//技术
			if(c.getSubjectCode().equals("3037")) {
				//如果技术人数大于0 显示技术 上报中含技术
				if(!dataContentNum1Map1.containsKey(c.getId()) && !dataContentNum1Map2.containsKey(c.getId())) {
					continue;
				}
			}
			course1.add(c.getSubjectName());
			int num1=0;
			if(dataContentNum1Map1.containsKey(c.getId())) {
				num1=dataContentNum1Map1.get(c.getId());
			}
			if(allStu1==0) {
				chooseCourse1.get(arr[0]).add(new String[] {"--","--"});
			}else {
				double ff = num1*100.0/allStu1;
				String value = df.format(ff);
				chooseCourse1.get(arr[0]).add(new String[] {""+num1,value+"%"});
			}
			
			int num2=0;
			if(dataContentNum1Map2.containsKey(c.getId())) {
				num2=dataContentNum1Map2.get(c.getId());
			}
			if(allStu2==0) {
				chooseCourse1.get(arr[1]).add(new String[] {"--","--"});
			}else {
				double ff = num2*100.0/allStu2;
				String value = df.format(ff);
				chooseCourse1.get(arr[1]).add(new String[] {""+num2,value+"%"});
			}
			if(num1>num2) {
				maxSingleList.add(0);
			}else if(num1<num2) {
				maxSingleList.add(1);
			}else {
				maxSingleList.add(-1);
			}
			returnCourseList.add(c);
		}
		
		if(CollectionUtils.isEmpty(course1)) {
			return errorFtl(map, "上报数据不对，请联系管理员");
		}
		map.put("courses1", course1);
		map.put("chooseCourse1", chooseCourse1);
		map.put("maxSingleList", maxSingleList);
		
		//根据returnCourseList 组装所有三科组合
		Integer[] cSize = new Integer[returnCourseList.size()];
        for(int i = 0;i < returnCourseList.size();i++){
        	cSize[i] = i;
        }
        CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize,3);
        Integer[][] result = combineAlgorithm.getResutl();
    	List<List<String>> chooseCourse2=new ArrayList<>();
    	List<String> itemList ;
    	List<Integer> maxThreeList=new ArrayList<>();
        for(int i = 0; i < result.length; i++) {
			String ids = "";
			String shortNames = "";
			for(int j = 0; j <result[i].length; j++) {
				if(j == 0) {
					ids += returnCourseList.get(result[i][j]).getId();
				}else {
					ids = ids + ',' + returnCourseList.get(result[i][j]).getId();
				}
				shortNames += returnCourseList.get(result[i][j]).getShortName();
			}
			itemList= new ArrayList<String>();
			itemList.add(shortNames);
			String[] arr1 = ids.split(",");
			Arrays.sort(arr1);
			ids=ArrayUtil.print(arr1);
			
			int num1=0;
			if(dataContentNum1Map1.containsKey(ids)) {
				num1=dataContentNum1Map1.get(ids);
			}
			if(allStu1==0) {
				itemList.add("--");
				itemList.add("--");
			}else {
				double ff = num1*100.0/allStu1;
				String value = df.format(ff);
				itemList.add(""+num1);
				itemList.add(value+"%");
			}
			
			int num2=0;
			if(dataContentNum1Map2.containsKey(ids)) {
				num2=dataContentNum1Map2.get(ids);
			}
			if(allStu2==0) {
				itemList.add("--");
				itemList.add("--");
			}else {
				double ff = num2*100.0/allStu2;
				String value = df.format(ff);
				itemList.add(""+num2);
				itemList.add(value+"%");
			}
			chooseCourse2.add(itemList);
			if(num1>num2) {
				maxThreeList.add(0);
			}else if(num1<num2) {
				maxThreeList.add(1);
			}else {
				maxThreeList.add(-1);
			}
        }
        map.put("maxThreeList", maxThreeList);
        map.put("chooseCourse2", chooseCourse2);
				
		return "/newgkelective/reportEdu/reportChoseCompare.ftl";
	}
	/**
	 * 
	 * @param unitId
	 * @param gradeYear
	 * @param dataContentNumMap key:总人数 32个0  key:subjectId key:subjectIds
	 */
	public void statChooseResultByUnit(Unit unit,String gradeYear,Map<String,Integer> dataContentNumMap) {
		List<NewGkReportChose> list = new ArrayList<>();
		if(Objects.equals(unit.getUnitClass(), 2)) {
			//学校
			List<NewGkReport> reportList = newGkReportService.findListBy(new String[] {"unitId","openAcadyear"}, new String[] {unit.getId(),gradeYear});
			if(CollectionUtils.isEmpty(reportList)) {
				return;
			}
			NewGkReport report=reportList.get(0);
			list = newGkReportChoseService.findListBy("reportId", report.getId());
		}else {
			//教育局
			List<Unit> schoolList = findSchoolListByEduUnitId(unit.getId());
			if(CollectionUtils.isNotEmpty(schoolList)){
				String[] unitIds = EntityUtils.getList(schoolList, Unit::getId).toArray(new String[0]);
				List<NewGkReport> reportList = newGkReportService.findListBy(unitIds,gradeYear);
				reportList = reportList.stream().filter(e->e.getIsChosen()==1).collect(Collectors.toList());
				if(CollectionUtils.isNotEmpty(reportList)){
					Set<String> reportIds = EntityUtils.getSet(reportList, e->e.getId());
					list =newGkReportChoseService.findListByIn("reportId", reportIds.toArray(new String[] {}));
				}
			}
		}
		if(CollectionUtils.isEmpty(list)) {
			return;
		}
		String key;
		for(NewGkReportChose cc:list) {
			//总人数 01， 已选 02 ，未选 03   ，3科  04  ， 单科  05
			if(NewGkElectiveConstant.REPORT_CHOSE_TYPE_01.equals(cc.getDataType())) {
				continue;
			}
			if(NewGkElectiveConstant.REPORT_CHOSE_TYPE_02.equals(cc.getDataType()) || NewGkElectiveConstant.REPORT_CHOSE_TYPE_03.equals(cc.getDataType())) {
				key=BaseConstants.ZERO_GUID;
			}else if(NewGkElectiveConstant.REPORT_CHOSE_TYPE_04.equals(cc.getDataType())) {
				String[] arr1 = cc.getDataKeys().split(",");
				Arrays.sort(arr1);
				key=ArrayUtil.print(arr1);
				
			}else {
				key=cc.getDataKeys();
			}
			if(!dataContentNumMap.containsKey(key)) {
				dataContentNumMap.put(key, 0);
			}
			dataContentNumMap.put(key, dataContentNumMap.get(key)+cc.getBoyNumber()+cc.getGirlNumber());
		}
	}
	
	public String findChoseByUnitId(String unitId,String gradeYear,ModelMap map) {
		Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
		if(unit==null) {
			return errorFtl(map, "该单位数据未找到，有问题请联系管理员");
		}
		map.put("unitId", unit.getId());
		map.put("unitName", unit.getUnitName());
		map.put("typeName", "选课");
		//总数
		List<String> dataContent1=new ArrayList<>();
		dataContent1.add("已选");
		dataContent1.add("未选");
		//先男生，后女生
		List<Integer[]> dataNum1=new ArrayList<>();
		//2科
		List<String> dataContent2=new ArrayList<>();
		List<Integer[]> dataNum2=new ArrayList<>();
		//3课
		List<String> dataContent3=new ArrayList<>();
		List<Integer[]> dataNum3=new ArrayList<>();
		
		//key:1:已选 2:未选
		Map<String,Integer[]> dataContentNum1Map=new HashMap<>();
		//key:subjectId
		Map<String,Integer[]> dataContentNum2Map=new HashMap<>();
		//key:subjectIds
		Map<String,Integer[]> dataContentNum3Map=new HashMap<>();
		if(Objects.equals(unit.getUnitClass(), 2)) {
			//学校
			School school = SUtils.dc(schoolRemoteService.findOneById(unitId), School.class);
			if(school!=null) {
				if(StringUtils.isNotBlank(school.getSchoolmaster())) {
					unit.setUnitHeader(school.getSchoolmaster());
				}
				if(StringUtils.isNotBlank(school.getLinkPhone())) {
					unit.setMobilePhone(school.getLinkPhone());
				}
			}
			map.put("unit", unit);
			map.put("isEdu", false);
			List<NewGkReport> reportList = newGkReportService.findListBy(new String[] {"unitId","openAcadyear"}, new String[] {unitId,gradeYear});
			if(CollectionUtils.isEmpty(reportList)) {
				return "/newgkelective/reportEdu/reportNone.ftl";
			}
			NewGkReport report=reportList.get(0);
			if(report.getIsChosen()==0) {
				return "/newgkelective/reportEdu/reportNone.ftl";
			}
			map.put("startTime",report.getChooseStartTime());
			map.put("endTime",report.getChooseEndTime());
			
			List<NewGkReportChose> list = newGkReportChoseService.findListBy("reportId", report.getId());
		
			int allStuNum=0;
			int girlNum=0;
			int boyNum=0;
			for(NewGkReportChose cc:list) {
				//总人数 01， 已选 02 ，未选 03   ，3科  04  ， 单科  05
				if(NewGkElectiveConstant.REPORT_CHOSE_TYPE_01.equals(cc.getDataType())) {
					boyNum=cc.getBoyNumber();
					girlNum=cc.getGirlNumber();
					allStuNum=boyNum+girlNum;
				}else if(NewGkElectiveConstant.REPORT_CHOSE_TYPE_02.equals(cc.getDataType())) {
					dataContentNum1Map.put("1", new Integer[] {cc.getBoyNumber(),cc.getGirlNumber()});
				}else if(NewGkElectiveConstant.REPORT_CHOSE_TYPE_03.equals(cc.getDataType())) {
					dataContentNum1Map.put("2", new Integer[] {cc.getBoyNumber(),cc.getGirlNumber()});
				}else if(NewGkElectiveConstant.REPORT_CHOSE_TYPE_04.equals(cc.getDataType())) {
					String[] arr = cc.getDataKeys().split(",");
					Arrays.sort(arr);
					dataContentNum3Map.put(ArrayUtil.print(arr), new Integer[] {cc.getBoyNumber(),cc.getGirlNumber()});
				}else {
					dataContentNum2Map.put(cc.getDataKeys(), new Integer[] {cc.getBoyNumber(),cc.getGirlNumber()});
				}
			}
			map.put("allStuNum", allStuNum);
			//学校信息
			map.put("boyNum",boyNum);
			map.put("girlNum",girlNum);
		}else {
			map.put("unit", unit);
			map.put("isEdu", true);
			//教育局
			List<Unit> schoolList = findSchoolListByEduUnitId(unitId);
			if(CollectionUtils.isEmpty(schoolList)) {
				return "/newgkelective/reportEdu/reportNone.ftl";
			}
			Set<String> allIds = EntityUtils.getSet(schoolList, e->e.getId());
			List<NewGkReport> reportList = newGkReportService.findListBy(allIds.toArray(new String[allIds.size()]),gradeYear);
			reportList = reportList.stream().filter(e->e.getIsChosen()==1).collect(Collectors.toList());
			if(CollectionUtils.isEmpty(reportList)) {
				return "/newgkelective/reportEdu/reportNone.ftl";
			}
			Set<String> ids = EntityUtils.getSet(reportList, e->e.getUnitId());
			//查询上报的学校
			map.put("reportNum",ids.size());
			map.put("noReportNum",allIds.size()-ids.size());
			
			Set<String> reportIds = EntityUtils.getSet(reportList, e->e.getId());
			//先统计上传的人数
			List<NewGkReportChose> list =newGkReportChoseService.findListByIn("reportId", reportIds.toArray(new String[] {}));
			for(NewGkReportChose cc:list) {
				
				//总人数 01， 已选 02 ，未选 03   ，3科  04  ， 单科  05
				if(NewGkElectiveConstant.REPORT_CHOSE_TYPE_01.equals(cc.getDataType())) {
					continue;
				}
				if(NewGkElectiveConstant.REPORT_CHOSE_TYPE_02.equals(cc.getDataType())) {
					Integer[] arr = dataContentNum1Map.get("1");
					if(arr==null) {
						dataContentNum1Map.put("1", new Integer[] {cc.getBoyNumber(),cc.getGirlNumber()});
					}else {
						arr[0]=arr[0]+cc.getBoyNumber();
						arr[0]=arr[0]+cc.getGirlNumber();
					}
					
				}else if(NewGkElectiveConstant.REPORT_CHOSE_TYPE_03.equals(cc.getDataType())) {
					Integer[] arr = dataContentNum1Map.get("2");
					if(arr==null) {
						dataContentNum1Map.put("2", new Integer[] {cc.getBoyNumber(),cc.getGirlNumber()});
					}else {
						arr[0]=arr[0]+cc.getBoyNumber();
						arr[0]=arr[0]+cc.getGirlNumber();
					}
					dataContentNum1Map.put("2", new Integer[] {cc.getBoyNumber(),cc.getGirlNumber()});
				}else if(NewGkElectiveConstant.REPORT_CHOSE_TYPE_04.equals(cc.getDataType())) {
					String[] arr1 = cc.getDataKeys().split(",");
					Arrays.sort(arr1);
					String key=ArrayUtil.print(arr1);
					Integer[] arr = dataContentNum3Map.get(key);
					if(arr==null) {
						dataContentNum3Map.put(key, new Integer[] {cc.getBoyNumber(),cc.getGirlNumber()});
					}else {
						arr[0]=arr[0]+cc.getBoyNumber();
						arr[0]=arr[0]+cc.getGirlNumber();
					}
				}else {
					Integer[] arr = dataContentNum2Map.get(cc.getDataKeys());
					if(arr==null) {
						dataContentNum2Map.put(cc.getDataKeys(), new Integer[] {cc.getBoyNumber(),cc.getGirlNumber()});
					}else {
						arr[0]=arr[0]+cc.getBoyNumber();
						arr[0]=arr[0]+cc.getGirlNumber();
					}
				}
			}
		}

		//单科
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(unitId), Course.class);
		List<Course> returnCourseList=new ArrayList<>();
		for(Course c:courseList) {
			//技术
			if(c.getSubjectCode().equals("3037")) {
				//如果技术人数大于0 显示技术 上报中含技术
				if(!dataContentNum2Map.containsKey(c.getId())) {
					continue;
				}
			}
			dataContent2.add(c.getSubjectName());
			Integer[] arr = dataContentNum2Map.get(c.getId());
			if(arr==null) {
				dataNum2.add(new Integer[] {0,0});
			}else {
				dataNum2.add(arr);
			}
			returnCourseList.add(c);
		}
		
		
		//根据returnCourseList 组装所有三科组合
		Integer[] cSize = new Integer[returnCourseList.size()];
        for(int i = 0;i < returnCourseList.size();i++){
        	cSize[i] = i;
        }
        CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize,3);
        Integer[][] result = combineAlgorithm.getResutl();
        for(int i = 0; i < result.length; i++) {
			String ids = "";
			String shortNames = "";
			for(int j = 0; j <result[i].length; j++) {
				if(j == 0) {
					ids += returnCourseList.get(result[i][j]).getId();
				}else {
					ids = ids + ',' + returnCourseList.get(result[i][j]).getId();
				}
				shortNames += returnCourseList.get(result[i][j]).getShortName();
			}
			dataContent3.add(shortNames);
			String[] arr1 = ids.split(",");
			Arrays.sort(arr1);
			Integer[] arr = dataContentNum3Map.get(ArrayUtil.print(arr1));
			if(arr==null) {
				dataNum3.add(new Integer[] {0,0});
			}else {
				dataNum3.add(arr);
			}
        }
        
        map.put("dataContent1", dataContent1);
        dataNum1.add(dataContentNum1Map.get("1"));
        dataNum1.add(dataContentNum1Map.get("2"));
        map.put("dataNum1", dataNum1);
        map.put("dataContent2", dataContent2);
        map.put("dataNum2", dataNum2);
        map.put("dataContent3", dataContent3);
        map.put("dataNum3", dataNum3);
        map.put("gradeYear", gradeYear);
		return "/newgkelective/reportEdu/reportChoseItem.ftl";
	}

	@RequestMapping("/edu/subjectHead/page")
	public String showSubjectHead(String unitId,String gradeYear,ModelMap map) {
		//包括已经上报学校数量
		List<Course> courseList = showAllCourseList(unitId, gradeYear, map);
		if(CollectionUtils.isEmpty(courseList)) {
			return errorFtl(map, "科目数据有误，请联系管理员");
		}
		map.put("gradeYear", gradeYear);
		List<String[]> searchList=new ArrayList<>();
		for(Course c:courseList) {
			searchList.add(new String[] {c.getId(),c.getSubjectName()});
		}
		map.put("searchList", searchList);
		map.put("searchType","1");
		map.put("subjectIds", searchList.get(0)[0]);
		return "/newgkelective/reportEdu/reportItemHead.ftl";
	}
	
	
	@RequestMapping("/edu/subjectsHead/page")
	public String showSubjectsHead(String unitId,String gradeYear,ModelMap map) {
		List<Course> courseList = showAllCourseList(unitId, gradeYear, map);
		if(CollectionUtils.isEmpty(courseList)) {
			return errorFtl(map, "科目数据有误，请联系管理员");
		}
		map.put("gradeYear", gradeYear);
		List<String[]> searchList=new ArrayList<>();
		Integer[] cSize = new Integer[courseList.size()];
        for(int i = 0;i < courseList.size();i++){
        	cSize[i] = i;
        }
        CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize,3);
        Integer[][] result = combineAlgorithm.getResutl();
        for(int i = 0; i < result.length; i++) {
			String ids = "";
			String shortNames = "";
			for(int j = 0; j <result[i].length; j++) {
				if(j == 0) {
					ids += courseList.get(result[i][j]).getId();
				}else {
					ids = ids + ',' + courseList.get(result[i][j]).getId();
				}
				shortNames += courseList.get(result[i][j]).getShortName();
			}
			String[] arr1 = ids.split(",");
			Arrays.sort(arr1);
			searchList.add(new String[] {ArrayUtil.print(arr1),shortNames});
        }
        map.put("searchList", searchList);
        map.put("searchType","3");
        map.put("unitId", unitId);
        map.put("subjectIds", searchList.get(0)[0]);
		return "/newgkelective/reportEdu/reportItemHead.ftl";
		
	}
	
	public List<Course> showAllCourseList(String unitId,String gradeYear,ModelMap map){
		Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
		map.put("unitId", unit.getId());
		map.put("unitName", unit.getUnitName());
		List<Unit> childUnitList = findSchoolListByEduUnitId(unitId);
		Set<String> subjectIds=new HashSet<>();
		int reportNum=0;
		if(CollectionUtils.isNotEmpty(childUnitList)) {
			String[] unitIds = EntityUtils.getList(childUnitList, Unit::getId).toArray(new String[childUnitList.size()]);
			List<NewGkReport> reportList = newGkReportService.findListBy(unitIds,gradeYear);
			reportList = reportList.stream().filter(e->e.getIsChosen()==1).collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(reportList)){
				reportNum=reportList.size();
				Set<String> reportIds = EntityUtils.getSet(reportList, e->e.getId());
				List<NewGkReportChose> list =newGkReportChoseService.findListByIn("reportId", reportIds.toArray(new String[] {}));
				if(CollectionUtils.isNotEmpty(list)) {
					subjectIds=list.stream().filter(e->e.getDataType().equals(NewGkElectiveConstant.REPORT_CHOSE_TYPE_05)).map(e->e.getDataKeys()).filter(Objects::nonNull).collect(Collectors.toSet());
				}
			}
				
		}
		
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(unitId), Course.class);
		List<Course> returnCourseList=new ArrayList<>();
		for(Course c:courseList) {
			//技术
			if(c.getSubjectCode().equals("3037")) {
				if(!subjectIds.contains(c.getId())) {
					continue;
				}
			}
			returnCourseList.add(c);
		}
		map.put("reportNum", reportNum);
		return returnCourseList;
	}
	@RequestMapping("/edu/subjectsResultList/page")
	public String showAllSchoolItem(String unitId,String gradeYear,String subjectIds,PageInfo pageInfo,ModelMap map) {
		if(StringUtils.isBlank(subjectIds)) {
			return errorFtl(map, "参数有误，请联系管理员");
		}
		List<Unit> childUnitList = findSchoolListByEduUnitId(unitId);
		String[] unitIds = EntityUtils.getList(childUnitList, Unit::getId).toArray(new String[childUnitList.size()]);
		List<NewGkReport> reportList = newGkReportService.findListBy(unitIds,gradeYear);
		reportList = reportList.stream().filter(e->e.getIsChosen()==1).collect(Collectors.toList());
		map.put("unitId", unitId);
		map.put("gradeYear", gradeYear);
		map.put("subjectIds", subjectIds);
		List<ReportUnitDto> unitDtoList=new ArrayList<>();
		if(CollectionUtils.isNotEmpty(reportList)) {
			String type="05";
			if(subjectIds.indexOf(",")>-1) {
				type="04";
			}
			Map<String,Integer[]> chooseByUnit=new HashMap<>();
			Map<String,Integer> chooseAllByUnit=new HashMap<>();
			Set<String> ids = EntityUtils.getSet(reportList, e->e.getId());
			List<NewGkReportChose> list =newGkReportChoseService.findListByIn("reportId", ids.toArray(new String[] {}));
			for(NewGkReportChose item:list) {
				if(item.getDataType().equals("01")) {
					chooseAllByUnit.put(item.getReportId(), item.getBoyNumber()+item.getGirlNumber());
				}else if(item.getDataType().equals(type)) {
					if(subjectIds.equals(item.getDataKeys())) {
						chooseByUnit.put(item.getReportId(), new Integer[] {item.getBoyNumber(),item.getGirlNumber()});
					}
				}
			}
			Map<String, Unit> unitMap = EntityUtils.getMap(childUnitList, e->e.getId());
			ReportUnitDto unitDto;
			//加分页
			int allNum=reportList.size();
			pageInfo=initPage(pageInfo, allNum);
			map.put("allNum", allNum);
			reportList = reportList.stream().skip(pageInfo.getStartIndex()-1)
					.limit(pageInfo.getPageSize())
					.collect(Collectors.toList());
			map.put("pageInfo", pageInfo);
			
			for(NewGkReport dto:reportList) {
				unitDto=new ReportUnitDto();
				unitDto.setSchoolName(unitMap.get(dto.getUnitId()).getUnitName());
				if(chooseAllByUnit.containsKey(dto.getId()) && chooseAllByUnit.get(dto.getId())>0) {
					unitDto.setAllStudentNum(chooseAllByUnit.get(dto.getId()));
					
					if(chooseByUnit.containsKey(dto.getId())) {
						unitDto.setBoyStudentNum(chooseByUnit.get(dto.getId())[0]);
						unitDto.setGirlStudentNum(chooseByUnit.get(dto.getId())[1]);
						double radio = (chooseByUnit.get(dto.getId())[0]+chooseByUnit.get(dto.getId())[1])*100.0/unitDto.getAllStudentNum();
						//保留两位
						BigDecimal b = new BigDecimal(radio);  
						double f1 =  b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
						unitDto.setRatioNum(f1);
					}else {
						unitDto.setAllStudentNum(0);
						unitDto.setBoyStudentNum(0);
						unitDto.setGirlStudentNum(0);
						unitDto.setRatioNum(0.0);
					}
				}else {
					unitDto.setAllStudentNum(0);
					unitDto.setBoyStudentNum(0);
					unitDto.setGirlStudentNum(0);
					unitDto.setRatioNum(0.0);
				}
				unitDtoList.add(unitDto);
				
			}
		}
		map.put("unitDtoList", unitDtoList);
		
		return "/newgkelective/reportEdu/reportItemList.ftl";
	}
	
	public PageInfo initPage(PageInfo pageInfo,int dataSize) {
		if(pageInfo==null) {
			pageInfo=new PageInfo();
		}
		pageInfo.setItemsNum(dataSize);
		if(pageInfo.getPageIndex()==0) {
			pageInfo.setPageIndex(1);
		}
		//[10,20,30,50,100,150]
		List<Integer> pageList =pageList();
		pageInfo.setPageList(pageList);
		pageInfo.refresh();
		pageInfo.makeShowCount();
		return pageInfo;
	}
	/**
	 * 分页参数
	 * [10,20,30,50,100,150]
	 * @return
	 */
	public List<Integer> pageList(){
		List<Integer> pageList = new ArrayList<>();
		pageList.add(10);
		pageList.add(20);
		pageList.add(30);
		pageList.add(50);
		pageList.add(100);
		pageList.add(150);
		return pageList;
	}
	
	/**
	 * 资源统计导航页
	 * @param unitId
	 * @param gradeYear
	 * @param map
	 * @return
	 */
	public String baseItemIndex(String unitId, String gradeYear, ModelMap map) {
		Unit unit = unitRemoteService.findOneObjectById(unitId);
		if(Objects.equals(2, unit.getUnitClass())) {
			//TODO 学校 详细信息 界面
			return baseResDetail(unit, gradeYear, map);
		}
		
		List<Unit> unitList = findSchoolListByEduUnitId(unitId);
		
		// 负责人:unitHeader  联系电话：mobilePhone
		Set<String> uIds = EntityUtils.getSet(unitList, Unit::getId);
		//
		List<NewGkReport> reportList = newGkReportService.findListBy(uIds.toArray(new String[0]), gradeYear);
		long count = reportList.stream().filter(e->e.getIsBasic()==1).count();
		if(count<1) {
			map.put("unitName", unit.getUnitName());
			map.put("typeName", "排课资源");
			map.put("isEdu", true);
			return "/newgkelective/reportEdu/reportNone.ftl";
		}
		
		map.put("unitId", unitId);
		map.put("gradeYear", gradeYear);
		return "/newgkelective/reportEdu/reportBaseIndex.ftl";
	}
	/**
	 * 资源统计-场地资源
	 * @param unitId
	 * @param gradeYear
	 * @param map
	 * @return
	 */
	@RequestMapping("/edu/baseItem/placeRes")
	public String basePlaceRes(String unitId, String gradeYear, PageInfo pageInfo, ModelMap map) {
		List<Unit> unitList = findSchoolListByEduUnitId(unitId);
		
		Map<String,String> parentIdMap = EntityUtils.getMap(unitList, Unit::getId,Unit::getParentId);
		List<Unit> puList = unitRemoteService.findListObjectByIds(parentIdMap.values().toArray(new String[0]));
		unitList.stream().map(e->e.getId());
		Map<String, Unit> unitMap = Stream.concat(unitList.stream(), puList.stream()).collect(Collectors.toMap(Unit::getId, e->e));
		// 负责人:unitHeader  联系电话：mobilePhone
		Set<String> uIds = EntityUtils.getSet(unitList, Unit::getId);
		List<School> schoolList = SUtils.dt(schoolRemoteService.findListByIds(uIds.toArray(new String[0])), School.class);
		Map<String,School> schoolMap=new HashMap<>();
		if(CollectionUtils.isNotEmpty(schoolList)) {
			schoolMap=EntityUtils.getMap(schoolList, School::getId);
		}
		//
		List<NewGkReport> reportList = newGkReportService.findListBy(uIds.toArray(new String[0]), gradeYear);
		List<NewGkReport> basicReportList = reportList.stream().filter(e->e.getIsBasic()==1).collect(Collectors.toList());
		pageInfo=initPage(pageInfo, basicReportList.size());
//		pageInfo.setItemsNum(basicReportList.size());
//		pageInfo.refresh();
//		pageInfo.makeShowCount();
		
		basicReportList = basicReportList.subList(pageInfo.getStartIndex()-1, pageInfo.getEndIndex());
		
		Set<String> repIds = EntityUtils.getSet(basicReportList, NewGkReport::getId);
		List<NewGkReportBase> baseList =  newGkReportBaseService.findByReportIdAndType(repIds.toArray(new String[0]),new String[] {"02"});
		Map<String, NewGkReportBase> baseMap = EntityUtils.getMap(baseList, NewGkReportBase::getReportId);
		
		NewGkReportBaseDto dto = null;
		List<NewGkReportBaseDto> dtoList = new ArrayList<>();
		for (NewGkReport rept : basicReportList) {
			dto = new NewGkReportBaseDto();
			Unit unit = unitMap.get(rept.getUnitId());
			String puId = parentIdMap.get(rept.getUnitId());
			Unit punit = unitMap.get(puId);
			NewGkReportBase newGkReportBase = baseMap.get(rept.getId());
			
			dto.setUnitName(unit.getUnitName());
			dto.setParentName(punit.getUnitName());
			dto.setPlaceNum(newGkReportBase.getNum());
			
			dto.setUnitHeader(unit.getUnitHeader());
			dto.setTel(unit.getLinkPhone());
			if(dto.getTel()==null) {
				dto.setTel(unit.getLinkPhone());
			}
			if(schoolMap.containsKey(rept.getUnitId())) {
				School sch = schoolMap.get(rept.getUnitId());
				if(StringUtils.isNotBlank(sch.getSchoolmaster()))
					dto.setUnitHeader(sch.getSchoolmaster());
				if(StringUtils.isNotBlank(sch.getLinkPhone()))
					dto.setTel(sch.getLinkPhone());
			}
			
			if(rept.getIsDivide() == 1)
				dto.setXzbNum(rept.getXzbNumber());
			dtoList.add(dto);
		}
		
		
		map.put("pageInfo", pageInfo);
		map.put("dtoList", dtoList);
		map.put("unitId", unitId);
		return "/newgkelective/reportEdu/reportPlaceRes.ftl";
	}
	
	/**
	 * 资源统计-教师资源
	 * @param unitId
	 * @param gradeYear
	 * @param map
	 * @return
	 */
	@RequestMapping("/edu/baseItem/teacherRes")
	public String baseTeacherRes(String unitId, String gradeYear, String subjectId, PageInfo pageInfo, ModelMap map) {

		List<Unit> unitList = findSchoolListByEduUnitId(unitId);
		
		Map<String,String> parentIdMap = EntityUtils.getMap(unitList, Unit::getId,Unit::getParentId);
		List<Unit> puList = unitRemoteService.findListObjectByIds(parentIdMap.values().toArray(new String[0]));
		unitList.stream().map(e->e.getId());
		Map<String, Unit> unitNameMap = Stream.concat(unitList.stream(), puList.stream()).collect(Collectors.toMap(Unit::getId, e->e));
		// 负责人:unitHeader  联系电话：mobilePhone
		Set<String> uIds = EntityUtils.getSet(unitList, Unit::getId);
		//
		List<NewGkReport> reportList = newGkReportService.findListBy(uIds.toArray(new String[0]), gradeYear);
		List<NewGkReport> basicReportList = reportList.stream().filter(e->e.getIsBasic()==1).collect(Collectors.toList());
		pageInfo=initPage(pageInfo, basicReportList.size());
//		pageInfo.setItemsNum(basicReportList.size());
//		pageInfo.refresh();
//		pageInfo.makeShowCount();
		basicReportList = basicReportList.subList(pageInfo.getStartIndex()-1, pageInfo.getEndIndex());
		
		Set<String> repIds = EntityUtils.getSet(basicReportList, NewGkReport::getId);
		List<NewGkReportBase> baseList =  newGkReportBaseService.findByReportIdAndType(repIds.toArray(new String[0]),new String[] {"01"});
		// 获取新高考科目
		List<Course> xgkCourseList = SUtils.dt(courseRemoteService.findByCodes73(unitId), Course.class);
		if(StringUtils.isBlank(subjectId)) {
			subjectId = xgkCourseList.get(0).getId();
		}
		final String fsubj = subjectId;
		baseList = baseList.stream().filter(e->fsubj.equals(e.getKeyId())).collect(Collectors.toList());
		Map<String, NewGkReportBase> baseMap = EntityUtils.getMap(baseList, NewGkReportBase::getReportId);
		// 获取单位中 指定科目 的学生数量 和 教学班数量
		List<NewGkReportChose> repChoseList = newGkReportChoseService.findByReportIdAndType(repIds.toArray(new String[0]), "05", subjectId);
		List<NewGkReportDivide> repDivList = newGkReportDivideService.findByReportIdAnd(repIds.toArray(new String[0]), subjectId);
		Map<String, Integer> repStuMap = repChoseList.stream().collect(Collectors.toMap(NewGkReportChose::getReportId,
				e->e.getBoyNumber()+e.getGirlNumber()));
		Map<String, Optional<Integer>> repJxbMap = repDivList.stream().collect(
				Collectors.groupingBy(NewGkReportDivide::getReportId, 
						Collectors.mapping(NewGkReportDivide::getJxbNumber, Collectors.reducing((x,y)->x+y))));
		
		NewGkReportBaseDto dto = null;
		List<NewGkReportBaseDto> dtoList = new ArrayList<>();
		for (NewGkReport rept : basicReportList) {
			dto = new NewGkReportBaseDto();
			Unit unit = unitNameMap.get(rept.getUnitId());
			String puId = parentIdMap.get(rept.getUnitId());
			Unit punit = unitNameMap.get(puId);
			NewGkReportBase newGkReportBase = baseMap.get(rept.getId());
			
			dto.setUnitName(unit.getUnitName());
			dto.setParentName(punit.getUnitName());
			dto.setTeacherNum(newGkReportBase.getNum());
			dto.setUnitHeader(unit.getUnitHeader());
			if(rept.getIsChosen()==1)
				dto.setStuNum(repStuMap.get(rept.getId())==null?0:repStuMap.get(rept.getId()));
			if(rept.getIsDivide()==1 ) {
				Optional<Integer> optional = repJxbMap.get(rept.getId());				
				if(optional == null) {
					dto.setJxbNum(0);
				}else {
					dto.setJxbNum(optional.get());
				}
			}
			dtoList.add(dto);
		}
		
		map.put("unitId", unitId);
		map.put("subjectId", subjectId);
		map.put("gradeYear", gradeYear);
		map.put("dtoList", dtoList);
		map.put("xgkCourseList", xgkCourseList);
		map.put("pageInfo", pageInfo);
		
		return "/newgkelective/reportEdu/reportTeacherRes.ftl";
	}
	/**
	 * 资源统计-学校
	 * @param unit
	 * @param gradeYear
	 * @param map
	 * @return
	 */
	public String baseResDetail(Unit unit, String gradeYear, ModelMap map) {
		List<NewGkReport> reportList = newGkReportService.findListBy(new String[] {unit.getId()}, gradeYear);
		if(CollectionUtils.isEmpty(reportList) || Objects.equals(0, reportList.get(0).getIsBasic())) {
			//TODO 未上报
			map.put("unit", unit);
			map.put("unitName", unit.getUnitName());
			map.put("typeName", "排课资源");
			map.put("isEdu", false);
			School school = SUtils.dc(schoolRemoteService.findOneById(unit.getId()), School.class);
			if(school!=null) {
				if(StringUtils.isNotBlank(school.getSchoolmaster())) {
					unit.setUnitHeader(school.getSchoolmaster());
				}
				if(StringUtils.isNotBlank(school.getLinkPhone())) {
					unit.setMobilePhone(school.getLinkPhone());
				}
			}
			map.put("unit", unit);
			return "/newgkelective/reportEdu/reportNone.ftl";
		}
		NewGkReport report = reportList.get(0);
		int placeNum = 0;
		List<NewGkReportBase> baseList = newGkReportBaseService.findByReportIdAndType(new String[] {report.getId()}, null);
		Set<String> cids = baseList.stream().filter(e->"01".equals(e.getDataKeyType())).map(e->e.getKeyId()).collect(Collectors.toSet());
		Map<String, String> courseNameMap = SUtils.deserialize(courseRemoteService.findPartCouByIds(cids.toArray(new String[0])), new TypeReference<Map<String,String>>() {});
		
		
		//学生数包括A+B--取分班上报数据
		Map<String, Integer> repStuMap=new HashMap<>();
		Map<String, Integer> repJxbMap=new HashMap<>();
		List<NewGkReportDivide> list = newGkReportDivideService.findListBy("reportId", report.getId());
		if(CollectionUtils.isNotEmpty(list)){
			for(NewGkReportDivide f:list) {
				if(!repStuMap.containsKey(f.getSubjectId())) {
					repStuMap.put(f.getSubjectId(), f.getStudentNumber());
					repJxbMap.put(f.getSubjectId(), f.getXzbNumber()+f.getJxbNumber());
				}else {
					repStuMap.put(f.getSubjectId(), repStuMap.get(f.getSubjectId())+f.getStudentNumber());
					repJxbMap.put(f.getSubjectId(), repJxbMap.get(f.getSubjectId())+f.getXzbNumber()+f.getJxbNumber());
				}
			}
		}
//		List<NewGkReportChose> repChoseList = newGkReportChoseService.findByReportIdAndType(new String[] {report.getId()}, "05", null);
//		List<NewGkReportDivide> repDivList = newGkReportDivideService.findByReportIdAnd(new String[] {report.getId()}, null);
//		Map<String, Integer> repStuMap = repChoseList.stream().collect(Collectors.toMap(NewGkReportChose::getDataKeys,
//				e->e.getBoyNumber()+e.getGirlNumber()));
//		Map<String, Optional<Integer>> repJxbMap = repDivList.stream().collect(
//				Collectors.groupingBy(NewGkReportDivide::getSubjectId, 
//						Collectors.mapping(NewGkReportDivide::getJxbNumber, Collectors.reducing((x,y)->x+y))));
//		
		NewGkReportBaseDto dto = null;
		List<NewGkReportBaseDto> dtoList = new ArrayList<>();
		for (NewGkReportBase base : baseList) {
			if("02".equals(base.getDataKeyType())) {
				placeNum = base.getNum();
				continue;
			}
			dto = new NewGkReportBaseDto();
			dto.setSubjectName(courseNameMap.get(base.getKeyId()));
			dto.setTeacherNum(base.getNum());
			// 指定科目的 学生 和 教学班数量
			if(repStuMap.containsKey(base.getKeyId())) {
				dto.setStuNum(repStuMap.get(base.getKeyId()));
			}
			if(repJxbMap.containsKey(base.getKeyId())) {
				dto.setJxbNum(repJxbMap.get(base.getKeyId()));
			}else {
				dto.setJxbNum(null);
			}
//			if(report.getIsChosen()==1) 
//				dto.setStuNum(repStuMap.get(base.getKeyId()));
//			if(report.getIsDivide()==1) {
//				if(repJxbMap.containsKey(base.getKeyId())) {
//					dto.setJxbNum(repJxbMap.get(base.getKeyId()).get());
//				}else {
//					dto.setJxbNum(null);
//				}
//			}
			dtoList.add(dto);
		}
		
		map.put("stuNum", report.getNoStunumber()+report.getTwoStunumber()+report.getThreeStunumber());
		map.put("placeNum", placeNum);
		map.put("xzbNum", report.getXzbNumber());
		map.put("jxbNum", report.getJxbNumber());
		
		map.put("dtoList", dtoList);
		return "/newgkelective/reportEdu/reportBaseDetail.ftl";
	}
	
	@RequestMapping("/edu/upload/index")
	public String uploadIndex(String gradeId, ModelMap map) {
		
		map.put("gradeId", gradeId);
		return "/newgkelective/reportEdu/reportUploadIndex.ftl";
	}
	
	@RequestMapping("/edu/baseItem/upload/page")
	public String uploadBase(String gradeId, String useMaster, Boolean isNew, ModelMap map) {
		Grade grade = gradeRemoteService.findOneObjectById(gradeId);
		//1 是否已经上传
		List<NewGkReport> repList = null;
		if(Objects.equals(useMaster, "1")) {
			repList = newGkReportService.findListByWithMaster(new String[] {grade.getSchoolId()}, grade.getOpenAcadyear());
		}else {
			repList = newGkReportService.findListBy(new String[] {grade.getSchoolId()}, grade.getOpenAcadyear());
		}
		List<NewGkReportBaseDto> baseList = new ArrayList<>();
		List<Course> xgkCourseList = null;
		NewGkReportBaseDto dto = null;
		int placeNum = 0;
		if(isNew==null)
			isNew = false;
		if(!isNew && CollectionUtils.isNotEmpty(repList) && 1 == repList.get(0).getIsBasic()) {
			isNew = false;
			
			// 已经上传过
			NewGkReport report = repList.get(0);
			List<NewGkReportBase> repBaseList = null;
			if(Objects.equals(useMaster, "1"))
				repBaseList = newGkReportBaseService.findByReportIdAndTypeWithMaster(new String[] {report.getId()}, null);
			else
				repBaseList = newGkReportBaseService.findByReportIdAndType(new String[] {report.getId()}, null);
			Set<String> cids = EntityUtils.getSet(repBaseList, NewGkReportBase::getKeyId);
			Map<String, String> courseNameMap = SUtils.deserialize(courseRemoteService.findPartCouByIds(cids.toArray(new String[0])), new TypeReference<Map<String,String>>() {});
			xgkCourseList = new ArrayList<>();
			Course c = null;
			for (NewGkReportBase base : repBaseList) {
				if("02".equals(base.getDataKeyType())) {
					placeNum = base.getNum();
					continue;
				}
				dto = new NewGkReportBaseDto();
				dto.setSubjectName(courseNameMap.get(base.getKeyId()));
				dto.setTeacherNum(base.getNum());
				dto.setSubjectId(base.getKeyId());
				baseList.add(dto);
				
				c = new Course();
				c.setId(base.getKeyId());
				c.setSubjectName(dto.getSubjectName());
				xgkCourseList.add(c);
			}
		}else {
			isNew = true;
			// 未上传
			String unitId = getLoginInfo().getUnitId();
			List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(unitId), Course.class);
			Set<String> cids = EntityUtils.getSet(courseList, Course::getId);
			//从基础条件获取 教师数量信息
			 List<NewGkTeacherPlan> teacherPlanList = newGkTeacherPlanService
	                    .findByArrayItemIdAndSubjectIdIn(gradeId, cids.toArray(new String[0]), true);
			 Map<String, Integer> teacherCountMap = teacherPlanList.stream().collect(Collectors.toMap(NewGkTeacherPlan::getSubjectId, e->e.getExTeacherIdList().size()));
			 
			for (Course course : courseList) {
				dto = new NewGkReportBaseDto();
				dto.setSubjectId(course.getId());
				dto.setSubjectName(course.getSubjectName());
				// 从基础条件里面获取
				dto.setTeacherNum(0);
				if(teacherCountMap.containsKey(course.getId())) {
					dto.setTeacherNum(teacherCountMap.get(course.getId()));
				}
				baseList.add(dto);
			}
			xgkCourseList = courseList;
			// 场地数量从基础条件里面获取
			List<NewGkplaceArrange> list = newGkplaceArrangeService.findByArrayItemId(gradeId);
			if(list != null)
				placeNum = list.size();
		}
		
		
		map.put("isNew", isNew);
		map.put("xgkCourseList", xgkCourseList);
		map.put("placeNum", placeNum);
		map.put("baseList", baseList);
		map.put("grade", grade);
		return "/newgkelective/reportEdu/uploadBase.ftl";
	}
	
	@RequestMapping("/edu/baseItem/upload/save")
	@ResponseBody
	public String uploadBaseSave(String gradeId, Integer placeNum, NewGkReportBaseDto baseDto) {
		List<NewGkReportBase> dtoList = baseDto.getBaseList();
		Grade grade = gradeRemoteService.findOneObjectById(gradeId);
		List<NewGkReport> repList = newGkReportService.findListBy(new String[] {grade.getSchoolId()}, grade.getOpenAcadyear());
		NewGkReport newGkReport = null;
		Date now = new Date();
		if(CollectionUtils.isNotEmpty(repList)) {
			newGkReport = repList.get(0);
			newGkReport.setIsBasic(1);
			newGkReport.setOperator(getLoginInfo().getOwnerId());
			newGkReport.setModifyTime(now);
		}else {
			newGkReport = new NewGkReport();
			newGkReport.setId(UuidUtils.generateUuid());
			newGkReport.setUnitId(grade.getSchoolId());
			newGkReport.setGradeId(gradeId);
			newGkReport.setOpenAcadyear(grade.getOpenAcadyear());
			newGkReport.setOperator(getLoginInfo().getOwnerId());
			newGkReport.setIsBasic(1);
			newGkReport.setIsChosen(0);
			newGkReport.setIsDivide(0);
			newGkReport.setCreationTime(now);
			newGkReport.setModifyTime(now);
		}
		
		List<NewGkReportBase> baseList = new ArrayList<>();
		for (NewGkReportBase base : dtoList) {
			//TODO 
			base.setId(UuidUtils.generateUuid());
			base.setUnitId(grade.getSchoolId());
			base.setReportId(newGkReport.getId());
			base.setDataKeyType("01");
			base.setCreationTime(now);
			base.setModifyTime(now);
			baseList.add(base);
		}
		
		NewGkReportBase base = new NewGkReportBase();
		base.setId(UuidUtils.generateUuid());
		base.setUnitId(grade.getSchoolId());
		base.setReportId(newGkReport.getId());
		base.setDataKeyType("02");
		base.setKeyId(BaseConstants.ZERO_GUID);
		base.setNum(placeNum);
		base.setCreationTime(now);
		base.setModifyTime(now);
		baseList.add(base);
		
		try {
			newGkReportService.saveAllBaseList(newGkReport,baseList);
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage()+"");
		}
		
		return success("上报成功");
	}
	
	@ControllerInfo("教育局端查看分班统计数据")
	@RequestMapping("/edu/divideItem/page")
	public String findDivideByUnitId(String unitId, String gradeYear, PageInfo pageInfo, ModelMap map) {
		Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
		map.put("gradeYear", gradeYear);
		map.put("unitName", unit.getUnitName());
		map.put("unit", unit);
		map.put("typeName", "分班");
		if(unit.getUnitClass()==2) {
			//学校
			map.put("isEdu", false);
			School school = SUtils.dc(schoolRemoteService.findOneById(unitId), School.class);
			if(school!=null) {
				if(StringUtils.isNotBlank(school.getSchoolmaster())) {
					unit.setUnitHeader(school.getSchoolmaster());
				}
				if(StringUtils.isNotBlank(school.getLinkPhone())) {
					unit.setMobilePhone(school.getLinkPhone());
				}
			}
			List<NewGkReport> reportList = newGkReportService.findListBy(new String[] {"unitId","openAcadyear"}, new String[] {unitId,gradeYear});
			if(CollectionUtils.isEmpty(reportList)) {
				return "/newgkelective/reportEdu/reportNone.ftl";
			}
			NewGkReport report=reportList.get(0);
			if(report.getIsDivide()==0) {
				return "/newgkelective/reportEdu/reportNone.ftl";
			}
			map.put("report", report);
			List<NewGkReportDivide> list = newGkReportDivideService.findListBy("reportId", report.getId());
			if(CollectionUtils.isNotEmpty(list)){
				Set<String> subjectIds = EntityUtils.getSet(list, NewGkReportDivide::getSubjectId);
				List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[subjectIds.size()])),Course.class);
				Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId, Course::getSubjectName);
				map.put("courseNameMap", courseNameMap);
				Map<String, List<NewGkReportDivide>> listMap = EntityUtils.getListMap(list, NewGkReportDivide::getSubjectType, Function.identity());
				if(listMap.containsKey(NewGkElectiveConstant.SUBJECT_TYPE_A)){
					map.put("aList", listMap.get(NewGkElectiveConstant.SUBJECT_TYPE_A));
				}
				if(listMap.containsKey(NewGkElectiveConstant.SUBJECT_TYPE_B)){
					map.put("bList", listMap.get(NewGkElectiveConstant.SUBJECT_TYPE_B));
				}
			}
			
			return "/newgkelective/reportEdu/reportDivideSchItem.ftl";
		}else {
			//教育局
			map.put("isEdu", true);
			List<Unit> schoolList = findSchoolListByEduUnitId(unitId);
			if(CollectionUtils.isEmpty(schoolList)) {
				return "/newgkelective/reportEdu/reportNone.ftl";
			}
			String[] unitIds = EntityUtils.getList(schoolList, Unit::getId).toArray(new String[schoolList.size()]);
			List<NewGkReport> reportList = newGkReportService.findListBy(unitIds,gradeYear);
			reportList = reportList.stream().filter(e->e.getIsDivide()==1).collect(Collectors.toList());
			if(CollectionUtils.isEmpty(reportList)) {
				return "/newgkelective/reportEdu/reportNone.ftl";
			}
			Map<String ,String> schoolNameMap = EntityUtils.getMap(schoolList, Unit::getId, Unit::getUnitName);
			Set<String> allIds = EntityUtils.getSet(schoolList, e->e.getId());
			//查询上报的学校
			map.put("reportNum",reportList.size());
			map.put("noReportNum",allIds.size()-reportList.size());
			map.put("schoolNameMap", schoolNameMap);
			
			
			//学生走班人数
			int threeStunumber=0;
			int twoStunumber=0;
			int noStunumber=0;
			for (NewGkReport report : reportList) {
				threeStunumber+=report.getThreeStunumber();
				twoStunumber+=report.getTwoStunumber();
				noStunumber+=report.getNoStunumber();
			}
			map.put("threeStunumber", threeStunumber);
			map.put("twoStunumber", twoStunumber);
			map.put("noStunumber", noStunumber);
			
			//分班人数和未分班人数
			int divideStunumber = threeStunumber+twoStunumber+noStunumber;
			List<String> reportIds = EntityUtils.getList(reportList, NewGkReport::getId);
			List<NewGkReportChose> choseList =newGkReportChoseService.findListByIn("reportId", reportIds.toArray(new String[] {}));
			int allStunumber = choseList.stream().filter(e->"01".equals(e.getDataType())).mapToInt(e->e.getBoyNumber()+e.getGirlNumber()).sum();
			int noDivideStunumber = allStunumber-divideStunumber;
			if(noDivideStunumber>0){
				map.put("noDivideStunumber", noDivideStunumber);
			}else{
				map.put("noDivideStunumber", 0);
			}
			map.put("divideStunumber", divideStunumber);
			//分页
			if(pageInfo==null) {
				pageInfo=new PageInfo();
			}
			int allNum=reportList.size();
			pageInfo.setItemsNum(allNum);
			if(pageInfo.getPageIndex()==0) {
				pageInfo.setPageIndex(1);
			}
			//[10,20,30,50,100,150]
			List<Integer> pageList = new ArrayList<>();
			pageList.add(10);
			pageList.add(20);
			pageList.add(30);
			pageList.add(50);
			pageList.add(100);
			pageList.add(150);
			pageInfo.setPageList(pageList);
			pageInfo.refresh();
			pageInfo.makeShowCount();
			map.put("allNum", allNum);
			
			reportList = reportList.stream().sorted((e1,e2)->schoolNameMap.get(e1.getUnitId())
					.compareTo(schoolNameMap.get(e2.getUnitId()))).skip(pageInfo.getStartIndex()-1)
					.limit(pageInfo.getPageSize()).collect(Collectors.toList());
			
			map.put("pageInfo", pageInfo);
			map.put("reportList", reportList);
			
			return "/newgkelective/reportEdu/reportDivideEduItem.ftl";
		}

	}
	
	@ControllerInfo("学校端上传分班数据首页")
	@RequestMapping("/report/divide/index/page")
	public String showDivideList(String gradeId, String toEdit, String isMaster, ModelMap map) {
		map.put("gradeId", gradeId);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId),Grade.class);
		List<NewGkReport> reportList = null;
		if(Constant.IS_TRUE_Str.equals(isMaster))
			reportList = newGkReportService.findListByWithMaster(new String[] {grade.getSchoolId()}, grade.getOpenAcadyear());
		else
			reportList = newGkReportService.findListBy(new String[] {grade.getSchoolId()}, grade.getOpenAcadyear());
			
		if(CollectionUtils.isNotEmpty(reportList) && Constant.IS_TRUE==reportList.get(0).getIsDivide() && !Constant.IS_TRUE_Str.equals(toEdit)){
			NewGkReport report = reportList.get(0);
			map.put("report", report);
			List<NewGkReportDivide> divideList;
			if(Constant.IS_TRUE_Str.equals(isMaster)){
				divideList = newGkReportDivideService.findListByWithMaster("reportId", report.getId());
			}else{
				divideList = newGkReportDivideService.findListBy("reportId", report.getId());
			}
			if(CollectionUtils.isNotEmpty(divideList)){
				Set<String> subjectIds = EntityUtils.getSet(divideList, NewGkReportDivide::getSubjectId);
				List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[subjectIds.size()])),Course.class);
				Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId, Course::getSubjectName);
				map.put("courseNameMap", courseNameMap);
				Map<String, List<NewGkReportDivide>> divideMap = divideList.stream().collect(Collectors.groupingBy(NewGkReportDivide::getSubjectType));
				if(divideMap.containsKey(NewGkElectiveConstant.SUBJECT_TYPE_A)){
					map.put("aDivideList", divideMap.get(NewGkElectiveConstant.SUBJECT_TYPE_A));
				}
				if(divideMap.containsKey(NewGkElectiveConstant.SUBJECT_TYPE_B)){
					map.put("bDivideList", divideMap.get(NewGkElectiveConstant.SUBJECT_TYPE_B));
				}
			}
			map.put("notEdit", true);
			return "/newgkelective/reportEdu/divideReportDetail.ftl";
		}else{
			List<NewGkDivide> divideList = newGkDivideService.findByGradeId(
					getLoginInfo().getUnitId(), gradeId,null);
			map.put("allList", divideList);
			return "/newgkelective/reportEdu/divideReport.ftl";
		}
	}
	
	
	@ControllerInfo("学校端上传分班数据详情页")
	@RequestMapping("/report/divide/detail/page")
	public String findDivideByDivideId(String gradeId, String divideId, ModelMap map) {
		map.put("gradeId", gradeId);
		NewGkDivide divide = newGkDivideService.findById(divideId);
        List<NewGkDivideClass> divideClassList = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divide.getId(),
                    new String[]{}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<NewGkDivideClass> jxbClassList = divideClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())).collect(Collectors.toList());
		
		List<NewGkOpenSubject> subjectByDivideList = newGkOpenSubjectService.findByDivideId(divide.getId());
		Set<String> aSubjectId=new HashSet<String>();
		Set<String> bSubjectId=new HashSet<String>();
		Set<String> subjectIds=new HashSet<String>();
		for(NewGkOpenSubject open:subjectByDivideList){
			subjectIds.add(open.getSubjectId());
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(open.getSubjectType())){
				aSubjectId.add(open.getSubjectId());			
			}else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(open.getSubjectType())){
				bSubjectId.add(open.getSubjectId());
			}
		}
		
		// 统计某一科目所有的班级 包括 教学班和行政班
		Map<String, List<NewGkDivideClass>> subjectClassMap = jxbClassList.stream().collect(Collectors.groupingBy(e->e.getSubjectIds()));
		Map<String,Map<String, List<NewGkDivideClass>>> subjectIdTypeClassMap = new HashMap<>();
		for (String subjectId : subjectClassMap.keySet()) {
			List<NewGkDivideClass> list = subjectClassMap.get(subjectId);
			List<NewGkDivideClass> aList = list.stream().filter(e->NewGkElectiveConstant.SUBJECT_TYPE_A.equals(e.getSubjectType())).collect(Collectors.toList());
			List<NewGkDivideClass> bList = list.stream().filter(e->NewGkElectiveConstant.SUBJECT_TYPE_B.equals(e.getSubjectType())).collect(Collectors.toList());
			Map<String, List<NewGkDivideClass>> map2 = new HashMap<>();
			map2.put(NewGkElectiveConstant.SUBJECT_TYPE_A, aList);
			map2.put(NewGkElectiveConstant.SUBJECT_TYPE_B, bList);
			subjectIdTypeClassMap.put(subjectId, map2);
		}
		
		
		Map<String, NewGkDivideClass> divideClassMap = EntityUtils.getMap(divideClassList, NewGkDivideClass::getId);
		Map<String, List<NewGkDivideClass>> relatedJxbClassMap = jxbClassList.stream()
				.filter(e->StringUtils.isNotBlank(e.getRelateId()))
				.collect(Collectors.groupingBy(e->e.getRelateId()));
		NewGkDivideClass zhbClass;
		Set<String> setT;
		Set<String> relatedSubjectIds = null;
		int threeStunumber=0;
		int twoStunumber=0;
		int noStunumber=0;
		for (NewGkDivideClass clazz : divideClassList) {
			if(NewGkElectiveConstant.CLASS_TYPE_1.equals(clazz.getClassType()) && StringUtils.isNotBlank(clazz.getRelateId())
					&& divideClassMap.containsKey(clazz.getRelateId())) {
				// 组合班
				zhbClass = divideClassMap.get(clazz.getRelateId());
				String[] subjectIdsT = zhbClass.getSubjectIds().split(",");
				if(subjectIdsT.length > 0 && !BaseConstants.ZERO_GUID.equals(subjectIdsT[0])) {
					List<String> subjectIdsTList = Arrays.asList(subjectIdsT);
					setT = Arrays.stream(subjectIdsT).filter(e->aSubjectId.contains(e))
							.collect(Collectors.toSet());//组合班关联的选考班科目id
					
					relatedSubjectIds = null;
					if(subjectIdsT.length > 2) {
						setT.addAll(bSubjectId.stream()
								.filter(e->!Arrays.asList(subjectIdsT).contains(e))
								.collect(Collectors.toSet()));//未关联的学考班科目id
						threeStunumber+=clazz.getStudentList().size();
					}else{
						twoStunumber+=clazz.getStudentList().size();
					}
					List<NewGkDivideClass> relatedJxbs = relatedJxbClassMap.get(zhbClass.getId());
					if(CollectionUtils.isNotEmpty(relatedJxbs)) {
						relatedSubjectIds = EntityUtils.getSet(relatedJxbs, NewGkDivideClass::getSubjectIds);
					}
					for (String subjectId : setT) {
						if(CollectionUtils.isNotEmpty(relatedSubjectIds) && relatedSubjectIds.contains(subjectId)) {
							continue;
						}
						if(!subjectIdTypeClassMap.containsKey(subjectId)) {
							subjectIdTypeClassMap.put(subjectId, new HashMap<>());
							subjectIdTypeClassMap.get(subjectId).put(NewGkElectiveConstant.SUBJECT_TYPE_A, new ArrayList<>());
							subjectIdTypeClassMap.get(subjectId).put(NewGkElectiveConstant.SUBJECT_TYPE_B, new ArrayList<>());
						}
						if(subjectIdsTList.contains(subjectId)) {
							subjectIdTypeClassMap.get(subjectId).get(NewGkElectiveConstant.SUBJECT_TYPE_A).add(clazz);
						}else if(!subjectIdsTList.contains(subjectId)){
							subjectIdTypeClassMap.get(subjectId).get(NewGkElectiveConstant.SUBJECT_TYPE_B).add(clazz);
						}
					}
				}else{
					noStunumber+=clazz.getStudentList().size();
				}
			}
		}
		
		Set<String> subjectIdArr = subjectIdTypeClassMap.keySet();
		Map<String, String> coureNameMap = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIdArr.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
		map.put("courseNameMap", coureNameMap);
		
		List<NewGkReportDivide> aDivideList = new ArrayList<>();
		List<NewGkReportDivide> bDivideList = new ArrayList<>();
		NewGkReportDivide aDivide;
		NewGkReportDivide bDivide;
		
		for (String subjectIds2 : subjectIdTypeClassMap.keySet()) {
			List<NewGkDivideClass> alist = subjectIdTypeClassMap.get(subjectIds2).get(NewGkElectiveConstant.SUBJECT_TYPE_A);
			List<NewGkDivideClass> ajList = alist.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_2)).collect(Collectors.toList());
			
			List<NewGkDivideClass> blist = subjectIdTypeClassMap.get(subjectIds2).get(NewGkElectiveConstant.SUBJECT_TYPE_B);
			List<NewGkDivideClass> bjList = blist.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_2)).collect(Collectors.toList());
			
			Integer aStuNum = (int) alist.stream().flatMap(e->e.getStudentList().stream()).distinct().count();
			Integer bStuNum = (int) blist.stream().flatMap(e->e.getStudentList().stream()).distinct().count();
			
			aDivide = new NewGkReportDivide();
			aDivide.setSubjectId(subjectIds2);
			aDivide.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
			aDivide.setXzbNumber(alist.size()-ajList.size());
			aDivide.setJxbNumber(ajList.size());
			aDivide.setStudentNumber(aStuNum);
			
			bDivide = new NewGkReportDivide();
			bDivide.setSubjectId(subjectIds2);
			bDivide.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_B);
			bDivide.setXzbNumber(blist.size()-bjList.size());
			bDivide.setJxbNumber(bjList.size());
			bDivide.setStudentNumber(bStuNum);
			
			aDivideList.add(aDivide);
			bDivideList.add(bDivide);
		}
		aDivideList = aDivideList.stream().sorted(Comparator.comparingInt(NewGkReportDivide::getStudentNumber).reversed()).collect(Collectors.toList());
		bDivideList = bDivideList.stream().sorted(Comparator.comparingInt(NewGkReportDivide::getStudentNumber).reversed()).collect(Collectors.toList());
		map.put("aDivideList", aDivideList);
		map.put("bDivideList", bDivideList);
		
		NewGkReport report = new NewGkReport();
		List<NewGkDivideClass> xzbClassList = divideClassList.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_1)).collect(Collectors.toList());
		report.setXzbNumber(xzbClassList.size());
		report.setJxbNumber(jxbClassList.size());
		if(NewGkElectiveConstant.DIVIDE_TYPE_05.equals(divide.getOpenType())){
			report.setThreeStunumber(0);
			report.setTwoStunumber(0);
			report.setNoStunumber(xzbClassList.stream().map(e->e.getStudentList().size()).reduce((e1,e2)->e1+e2).orElse(0));
		}else{
			report.setThreeStunumber(threeStunumber);
			report.setTwoStunumber(twoStunumber);
			report.setNoStunumber(noStunumber);
		}
		map.put("report", report);
		return "/newgkelective/reportEdu/divideReportDetail.ftl";
	}
	

	@ResponseBody
	@ControllerInfo("学校端上传分班数据保存")
	@RequestMapping("/report/divide/save")
	public String saveDivideReport(String gradeId, NewGkReport report, ReportDivideSaveDto dto, ModelMap map) {
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId),Grade.class);
		List<NewGkReport> reportList = newGkReportService.findListBy(new String[] {grade.getSchoolId()}, grade.getOpenAcadyear());
		if(CollectionUtils.isNotEmpty(reportList)){
			NewGkReport newGkReport = reportList.get(0);
			newGkReport.setIsDivide(Constant.IS_TRUE);
			newGkReport.setXzbNumber(report.getXzbNumber());
			newGkReport.setJxbNumber(report.getJxbNumber());
			newGkReport.setThreeStunumber(report.getThreeStunumber());
			newGkReport.setTwoStunumber(report.getTwoStunumber());
			newGkReport.setNoStunumber(report.getNoStunumber());
			newGkReport.setModifyTime(new Date());
			report = newGkReport;
		}else{
			report.setId(UuidUtils.generateUuid());
			report.setUnitId(grade.getSchoolId());
			report.setOpenAcadyear(grade.getOpenAcadyear());
			report.setGradeId(gradeId);
			report.setIsDivide(Constant.IS_TRUE);
			report.setCreationTime(new Date());
			report.setModifyTime(new Date());
		}
		
		List<NewGkReportDivide> divideList = dto.getDivideList();
		for (NewGkReportDivide divide : divideList) {
			divide.setId(UuidUtils.generateUuid());
			divide.setReportId(report.getId());
			divide.setUnitId(grade.getSchoolId());
			divide.setCreationTime(new Date());
			divide.setModifyTime(new Date());
		}
		try {
			newGkReportDivideService.saveAndDelete(report, divideList);
		} catch (Exception e) {
			e.printStackTrace();
			return error("上报失败！");
		}
		return success("上报成功！");
	}
	
	public String baseIndex(ModelMap map) {
		return "/newgkelective/reportEdu/reportBaseIndex.ftl";
	}
	
	
	
	/**
	 * 单位树结构 缓存1天
	 * @param unitId
	 * @return
	 */
	public List<NewGkUnitMake> findUnitList(String unitId) {
		//某个单位下所有下属单位
		List<NewGkUnitMake> returnlist = RedisUtils.getObject("REDIS.UNIT." + unitId, RedisUtils.TIME_ONE_HOUR ,new TypeReference<List<NewGkUnitMake>>(){}, new RedisInterface<List<NewGkUnitMake>>(){
			@Override
			public List<NewGkUnitMake> queryData() {
				Unit unit = unitRemoteService.findOneObjectById(unitId);
				List<NewGkUnitMake> lists=new ArrayList<>();
				List<Unit> list = SUtils.dt(unitRemoteService.findUnionCodeSectionList(unit.getUnionCode(), String.valueOf(BaseConstants.SECTION_HIGH_SCHOOL), true,true), Unit.class);
				
				if(CollectionUtils.isEmpty(list)) {
					//单位数据不存在
					return lists;
				}
				lists=sortDep(list, unit);
				return lists;
			}
			
			 
			private  List<NewGkUnitMake>  sortDep(List<Unit> orgs, Unit root){				
				NewGkUnitMake treesort = makeNewUnitMake(root);		
				sort(orgs,treesort);
				
				List<NewGkUnitMake> list = new ArrayList<>();		
				addToList(treesort,list,-1);		
				return list;			
			}
			private  void sort(List<Unit> orgs, NewGkUnitMake treeSort){		 
				//遍历数据，将节点置于父节点类的list中                
				for(Unit dep : orgs){			
					if(dep.getParentId().equals(treeSort.getUnit().getId())){				
						NewGkUnitMake tree = makeNewUnitMake(dep);				
						treeSort.getChildUnitList().add(tree);				
						sort(orgs,tree);			
					}		
				}	
			}
			private void addToList(NewGkUnitMake treesort, List<NewGkUnitMake> list,int index) {	
				index++;
				treesort.setIndex(index);
				list.add(treesort);
				if(!treesort.getChildUnitList().isEmpty()){			
					for(NewGkUnitMake t : treesort.getChildUnitList()){			
						addToList(t, list,index);			
					}		
				}	
			}
		
				
			
			private NewGkUnitMake makeNewUnitMake(Unit unit) {
				NewGkUnitMake unitMake = new NewGkUnitMake();
				unitMake.setUnit(unit);
				unitMake.setChildUnitList(new ArrayList<>());
				if(Objects.equals(2,unit.getUnitClass())) {
					unitMake.setEdu(false);
				}else {
					unitMake.setEdu(true);
				}
				unitMake.setIndex(0);
				return unitMake;
			}
		});
		return returnlist;
	}

	/**
	 * 查询某个单位下所有学校
	 * @param unitId
	 * @return
	 */
	public List<Unit> findSchoolListByEduUnitId(String unitId){
		
		List<Unit> returnlist = RedisUtils.getObject("REDIS.UNIT.SCHOOL" + unitId, RedisUtils.TIME_ONE_HOUR , new TypeReference<List<Unit>>(){}, new RedisInterface<List<Unit>>(){
			@Override
			public List<Unit> queryData() {
				Unit unit = unitRemoteService.findOneObjectById(unitId);
				List<Unit> list = SUtils.dt(unitRemoteService.findUnionCodeSectionList(unit.getUnionCode(), String.valueOf(BaseConstants.SECTION_HIGH_SCHOOL), false,true), Unit.class);
				if(CollectionUtils.isEmpty(list)) {
					//单位数据不存在
					return new ArrayList<>();
				}
				//排序
				if(CollectionUtils.isNotEmpty(list)) {
					Collections.sort(list, new Comparator<Unit>() {

						@Override
						public int compare(Unit o1, Unit o2) {
							if(StringUtils.isBlank(o2.getUnionCode())) {
								return 0;
							}
							if(StringUtils.isBlank(o1.getUnionCode())) {
								return -1;
							}
							
							return o1.getUnionCode().compareTo(o2.getUnionCode());
						}
						
					});
				}
				
				return list;
			}
		});
		return returnlist;
	}
	/**
	 * 单位下的所有教育局 包括自己
	 * @param unitId
	 * @return
	 */
	public List<Unit> findEduListByEduUnitId(String unitId){
		List<Unit> returnlist = RedisUtils.getObject("REDIS.UNIT.EDU" + unitId, RedisUtils.TIME_ONE_HOUR , new TypeReference<List<Unit>>(){}, new RedisInterface<List<Unit>>(){
			@Override
			public List<Unit> queryData() {
				Unit unit = unitRemoteService.findOneObjectById(unitId);
				List<Unit> list = SUtils.dt(unitRemoteService.findUnionCodeSectionList(unit.getUnionCode(), String.valueOf(BaseConstants.SECTION_HIGH_SCHOOL), true,false), Unit.class);
				if(CollectionUtils.isEmpty(list)) {
					//单位数据不存在
					return new ArrayList<Unit>();
				}
				//排序
				if(CollectionUtils.isNotEmpty(list)) {
					Collections.sort(list, new Comparator<Unit>() {

						@Override
						public int compare(Unit o1, Unit o2) {
							if(StringUtils.isBlank(o2.getUnionCode())) {
								return 0;
							}
							if(StringUtils.isBlank(o1.getUnionCode())) {
								return -1;
							}
							
							return o1.getUnionCode().compareTo(o2.getUnionCode());
						}
						
					});
				}
				
				return list;
			}
		});
		return returnlist;
	}
	

	@RequestMapping("/report/choice/index/page")
	public String reportChoiceIndex(String gradeId, String reReport, String withMaster, ModelMap map) {
	    map.put("gradeId", gradeId);
        // 查询历史数据
        NewGkReport newGkReport = newGkReportService.findOneChoseByGradeId(getLoginInfo().getUnitId(), gradeId);
        if (newGkReport != null && !"1".equals(reReport)) {
            map.put("notReport", false);

            NewGkReportChose total = null;
            if ("1".equals(withMaster)) {
                total = newGkReportChoseService.findByReportIdAndTypeWithMaster(new String[]{newGkReport.getId()}, NewGkElectiveConstant.REPORT_CHOSE_TYPE_01, null).get(0);
            } else {
                total = newGkReportChoseService.findByReportIdAndType(new String[]{newGkReport.getId()}, NewGkElectiveConstant.REPORT_CHOSE_TYPE_01, null).get(0);
            }
            map.put("total", total.getGirlNumber() + total.getBoyNumber());
            map.put("maleTotal", total.getBoyNumber());
            map.put("femaleTotal", total.getGirlNumber());

            List<NewGkConditionDto> newConditionList = new ArrayList<>();
            List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()), new TR<List<Course>>(){});
            Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId, Course::getShortName);
            StringBuffer stringBuffer;
            List<NewGkReportChose> newGkReportChoseList = null;
            if ("1".equals(withMaster)) {
                newGkReportChoseList = newGkReportChoseService.findByReportIdAndTypeWithMaster(new String[]{newGkReport.getId()}, NewGkElectiveConstant.REPORT_CHOSE_TYPE_04, null);
            } else {
                newGkReportChoseList = newGkReportChoseService.findByReportIdAndType(new String[]{newGkReport.getId()}, NewGkElectiveConstant.REPORT_CHOSE_TYPE_04, null);
            }
            for (NewGkReportChose one : newGkReportChoseList) {
                stringBuffer = new StringBuffer(3);
                for (String courseId : one.getDataKeys().split(",")) {
                    stringBuffer.append(courseNameMap.get(courseId));
                }
                NewGkConditionDto newGkConditionDto = new NewGkConditionDto();
                newGkConditionDto.setSubShortNames(stringBuffer.toString());
                newGkConditionDto.setSumNum(one.getBoyNumber() + one.getGirlNumber());
                Map<String, Float> tmp = new HashMap<>();
                tmp.put("male", Float.valueOf(one.getBoyNumber()));
                tmp.put("female", Float.valueOf(one.getGirlNumber()));
                newGkConditionDto.setScoreMap(tmp);
                newConditionList.add(newGkConditionDto);
            }
            map.put("subjectsList", newConditionList);
            return "/newgkelective/choice/choiceReportDetail.ftl";
        }
        List<NewGkChoice> newGkChoiceList = newGkChoiceService.findListByGradeId(gradeId);
        map.put("allList", newGkChoiceList);
	    return "/newgkelective/choice/choiceReport.ftl";
    }

    @RequestMapping("/report/choice/detail/page")
    public String reportChoiceDetail(String gradeId, String choiceId, ModelMap map) {
	    map.put("gradeId", gradeId);
	    map.put("choiceId", choiceId);

        NewGkChoice newChoice = newGkChoiceService.findOne(choiceId);
        if (newChoice == null) {
            return errorFtl(map, "未进行任何选课");
        }

        List<Student> studentList = SUtils.dt(studentRemoteService.findPartStudByGradeId(getLoginInfo().getUnitId(), gradeId,null, null),new TR<List<Student>>(){});

        // 获取选考科目
        Map<String, List<NewGkChoRelation>> NewGkChoRelationAllMap = newGkChoRelationService.findByChoiceIdAndObjectTypeIn(newChoice.getUnitId(), choiceId, new String[]{NewGkElectiveConstant.CHOICE_TYPE_01,
                NewGkElectiveConstant.CHOICE_TYPE_04, NewGkElectiveConstant.CHOICE_TYPE_02});

        List<NewGkChoRelation> newGkChoRelationList = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01);
        List<String> courseIds = EntityUtils.getList(newGkChoRelationList, NewGkChoRelation::getObjectValue);
        if(CollectionUtils.isEmpty(courseIds)) {
            return errorFtl(map, "选课没有维护选课科目");
        }
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])), new TR<List<Course>>(){});

        //不参与选课排课人员
        List<NewGkChoRelation> notvals = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_04);
        if(CollectionUtils.isNotEmpty(notvals)){
            List<String> studentIds = EntityUtils.getList(studentList, Student::getId);
            Iterator<NewGkChoRelation> iterator = notvals.iterator();
            while(iterator.hasNext()){
                NewGkChoRelation cho = iterator.next();
                if(!studentIds.contains(cho.getObjectValue())){
                    iterator.remove();
                }
            }
        }
        //获取禁选组合
        List<NewGkChoRelation> notvals2 = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_02);
        List<String> objectValues = null;
        if (CollectionUtils.isNotEmpty(notvals2)) {
            objectValues = EntityUtils.getList(notvals2, NewGkChoRelation::getObjectValue);
        }
        //获取所有的选课结果
        List<NewGkChoResult> resultList = newGkChoResultService.findByKindTypeAndChoiceIdAndStudentIds(newChoice.getUnitId(), NewGkElectiveConstant.KIND_TYPE_01, choiceId, EntityUtils.getList(studentList, Student::getId).toArray(new String[0]));
        //已选学生ids
        Set<String> chosenStuIds = new HashSet<>();
        NewGkChoResultDto dto;
        //获取学生对应的选课ids
        Map<String,NewGkChoResultDto> dtoMap = new HashMap<>();
        Map<String, Integer> courseIdCountMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(resultList)){
            for(NewGkChoResult result : resultList){
                chosenStuIds.add(result.getStudentId());
                if(!dtoMap.containsKey(result.getStudentId())){
                    dto = new NewGkChoResultDto();
                    dto.setChooseSubjectIds(new HashSet<>());
                    dto.setStudentId(result.getStudentId());
                    dtoMap.put(result.getStudentId(), dto);
                }
                dtoMap.get(result.getStudentId()).getChooseSubjectIds().add(result.getSubjectId());

                Integer integer = courseIdCountMap.get(result.getSubjectId());
                if(integer == null){
                    integer = 0;
                }
                courseIdCountMap.put(result.getSubjectId(), ++integer);
            }
        }
        Map<String, Integer> courseNameCountMap = new HashMap<>();
        for(Course course:courseList){
            Integer integer = courseIdCountMap.get(course.getId());
            if(integer == null) integer = 0;
            courseNameCountMap.put(course.getSubjectName(), integer);
        }

        //各科目选择结果
        courseNameCountMap = sortByValue(courseNameCountMap);
        //计算组合
        Integer[] cSize = new Integer[courseList.size()];
        for(int i = 0;i < courseList.size();i++){
            cSize[i] = i;
        }

        //三科目选择结果
        CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize,3);
        Integer[][] result = combineAlgorithm.getResutl();
        List<NewGkConditionDto> newConditionList3 = newGkChoiceService.findSubRes(courseList, dtoMap, result, 3);
        //新增过滤筛选组合不推荐课程
        if(CollectionUtils.isNotEmpty(objectValues)){
            for(String subjectIdstr : objectValues){
                String[] subjectIds = subjectIdstr.split(",");
                if (subjectIds.length == 3) {
                    for(NewGkConditionDto newCondition : newConditionList3){
                        Set<String> subs = newCondition.getSubjectIds();
                        if(subs.contains(subjectIds[0]) && subs.contains(subjectIds[1]) && subs.contains(subjectIds[2])){
                            newCondition.setLimitSubject(true);
                        }
                    }
                }
            }
        }
        int maleTotal = 0;
        int femaleTotal = 0;
        int total = 0;
        studentList = studentList.stream().filter(e -> Integer.valueOf(1).equals(e.getSex())).collect(Collectors.toList());
        Set<String> maleStudentIds = EntityUtils.getSet(studentList, Student::getId);
        for (NewGkConditionDto one : newConditionList3) {
            int male = 0;
            int female = 0;
            for (String stu : one.getStuIds()) {
                if (maleStudentIds.contains(stu)) {
                    male++;
                    maleTotal++;
                } else {
                    female++;
                    femaleTotal++;
                }
                total++;
            }
            Map<String, Float> sexMap = new HashMap<>();
            sexMap.put("male", new Float(male));
            sexMap.put("female", new Float(female));
            one.setScoreMap(sexMap);
        }
        map.put("total", total);
        map.put("maleTotal", maleTotal);
        map.put("femaleTotal", femaleTotal);
        map.put("subjectsList", newConditionList3);
	    return "/newgkelective/choice/choiceReportDetail.ftl";
    }

    @RequestMapping("/report/choice/upload")
    @ResponseBody
    public String reportChoiceUpload(String gradeId, String choiceId) {
	    String unitId = getLoginInfo().getUnitId();
	    Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);

        NewGkChoice newChoice=newGkChoiceService.findOne(choiceId);
        NewGkReport newGkReport = newGkReportService.findOneByGradeId(unitId, gradeId);
        if (newGkReport == null) {
            newGkReport = new NewGkReport();
            newGkReport.setId(UuidUtils.generateUuid());
            newGkReport.setGradeId(gradeId);
            newGkReport.setUnitId(unitId);
            newGkReport.setChooseStartTime(newChoice.getStartTime());
            newGkReport.setChooseEndTime(newChoice.getEndTime());
            newGkReport.setIsChosen(1);
            newGkReport.setOperator(getLoginInfo().getOwnerId());
            newGkReport.setCreationTime(new Date());
            newGkReport.setModifyTime(new Date());
            newGkReport.setOpenAcadyear(grade.getOpenAcadyear());
        } else {
            newGkReport.setIsChosen(1);
            newGkReport.setChooseStartTime(newChoice.getStartTime());
            newGkReport.setChooseEndTime(newChoice.getEndTime());
            newGkReport.setOperator(getLoginInfo().getOwnerId());
            newGkReport.setModifyTime(new Date());
        }

        List<Student> studentList = SUtils.dt(studentRemoteService.findPartStudByGradeId(newChoice.getUnitId(), newChoice.getGradeId(),null, null),new TR<List<Student>>(){});
        Set<String> maleStudentIds = studentList.stream().filter(e -> Integer.valueOf(1).equals(e.getSex())).map(Student::getId).collect(Collectors.toSet());

        List<NewGkReportChose> newGkReportChoseList = new ArrayList<>();
        // 总人数
        NewGkReportChose totalNewGkReportChose = new NewGkReportChose();
        totalNewGkReportChose.setId(UuidUtils.generateUuid());
        totalNewGkReportChose.setUnitId(unitId);
        totalNewGkReportChose.setReportId(newGkReport.getId());
        totalNewGkReportChose.setBoyNumber(maleStudentIds.size());
        totalNewGkReportChose.setGirlNumber(studentList.size() - maleStudentIds.size());
        totalNewGkReportChose.setDataType(NewGkElectiveConstant.REPORT_CHOSE_TYPE_01);
        totalNewGkReportChose.setDataKeys(Constant.GUID_ZERO);
        totalNewGkReportChose.setCreationTime(new Date());
        totalNewGkReportChose.setModifyTime(new Date());
        newGkReportChoseList.add(totalNewGkReportChose);

        //获取选考科目
        Map<String, List<NewGkChoRelation>> NewGkChoRelationAllMap = newGkChoRelationService.findByChoiceIdAndObjectTypeIn(newChoice.getUnitId(), choiceId, new String[]{NewGkElectiveConstant.CHOICE_TYPE_01,
                NewGkElectiveConstant.CHOICE_TYPE_04, NewGkElectiveConstant.CHOICE_TYPE_02});

        List<NewGkChoRelation> newGkChoRelationList = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01);
        List<String> courseIds = EntityUtils.getList(newGkChoRelationList, NewGkChoRelation::getObjectValue);
        if(CollectionUtils.isEmpty(courseIds)) {
            return error("选课没有维护选课科目");
        }
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])), new TR<List<Course>>(){});

        //不参与选课排课人员
        List<NewGkChoRelation> notvals = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_04);
        if(CollectionUtils.isNotEmpty(notvals)){
            List<String> studentIds = EntityUtils.getList(studentList, Student::getId);
            Iterator<NewGkChoRelation> iterator = notvals.iterator();
            while(iterator.hasNext()){
                NewGkChoRelation cho = iterator.next();
                if(!studentIds.contains(cho.getObjectValue())){
                    iterator.remove();
                }
            }
        }
        //获取禁选组合
        List<NewGkChoRelation> notvals2 = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_02);
        List<String> objectValues = null;
        if (CollectionUtils.isNotEmpty(notvals2)) {
            objectValues = EntityUtils.getList(notvals2, NewGkChoRelation::getObjectValue);
        }
        //获取所有的选课结果
        List<NewGkChoResult> resultList = newGkChoResultService.findByKindTypeAndChoiceIdAndStudentIds(newChoice.getUnitId(), NewGkElectiveConstant.KIND_TYPE_01, choiceId, EntityUtils.getList(studentList, Student::getId).toArray(new String[0]));
        if (CollectionUtils.isEmpty(resultList)) {
            return error("该次选课暂无已选学生，无需上报");
        }

        //已选学生ids
        Set<String> chosenStuIds = new HashSet<>();
        NewGkChoResultDto dto;
        //获取学生对应的选课ids
        Map<String,NewGkChoResultDto> dtoMap = new HashMap<>();
        Map<String, Integer> courseIdCountMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(resultList)){
            for(NewGkChoResult result : resultList){
                chosenStuIds.add(result.getStudentId());
                if(!dtoMap.containsKey(result.getStudentId())){
                    dto = new NewGkChoResultDto();
                    dto.setChooseSubjectIds(new HashSet<>());
                    dto.setStudentId(result.getStudentId());
                    dtoMap.put(result.getStudentId(), dto);
                }
                dtoMap.get(result.getStudentId()).getChooseSubjectIds().add(result.getSubjectId());

                if (maleStudentIds.contains(result.getStudentId())) {
                    Integer integer = courseIdCountMap.get("M" + result.getSubjectId());
                    if(integer == null){
                        integer = 0;
                    }
                    courseIdCountMap.put("M" + result.getSubjectId(), ++integer);
                } else {
                    Integer integer = courseIdCountMap.get("F" + result.getSubjectId());
                    if(integer == null){
                        integer = 0;
                    }
                    courseIdCountMap.put("F" + result.getSubjectId(), ++integer);
                }
            }
        }

        // 已选学生
        int chosenMale = (int)chosenStuIds.stream().filter(e -> maleStudentIds.contains(e)).count();
        NewGkReportChose chosenNewGkReportChose = new NewGkReportChose();
        chosenNewGkReportChose.setId(UuidUtils.generateUuid());
        chosenNewGkReportChose.setUnitId(unitId);
        chosenNewGkReportChose.setReportId(newGkReport.getId());
        chosenNewGkReportChose.setBoyNumber(chosenMale);
        chosenNewGkReportChose.setGirlNumber(chosenStuIds.size() - chosenMale);
        chosenNewGkReportChose.setDataType(NewGkElectiveConstant.REPORT_CHOSE_TYPE_02);
        chosenNewGkReportChose.setDataKeys(Constant.GUID_ZERO);
        chosenNewGkReportChose.setCreationTime(new Date());
        chosenNewGkReportChose.setModifyTime(new Date());
        newGkReportChoseList.add(chosenNewGkReportChose);

        // 未选学生
        Set<String> nonChosenStuIds = studentList.stream().filter(e -> !chosenStuIds.contains(e.getId())).map(Student::getId).collect(Collectors.toSet());
        chosenMale = (int)nonChosenStuIds.stream().filter(e -> maleStudentIds.contains(e)).count();
        NewGkReportChose nonChosenNewGkReportChose = new NewGkReportChose();
        nonChosenNewGkReportChose.setId(UuidUtils.generateUuid());
        nonChosenNewGkReportChose.setUnitId(unitId);
        nonChosenNewGkReportChose.setReportId(newGkReport.getId());
        nonChosenNewGkReportChose.setBoyNumber(chosenMale);
        nonChosenNewGkReportChose.setGirlNumber(nonChosenStuIds.size() - chosenMale);
        nonChosenNewGkReportChose.setDataType(NewGkElectiveConstant.REPORT_CHOSE_TYPE_03);
        nonChosenNewGkReportChose.setDataKeys(Constant.GUID_ZERO);
        nonChosenNewGkReportChose.setCreationTime(new Date());
        nonChosenNewGkReportChose.setModifyTime(new Date());
        newGkReportChoseList.add(nonChosenNewGkReportChose);

        Map<String, Integer> courseNameCountMap = new HashMap<>();
        for(Course course:courseList){
            Integer integer = 0;
            integer += (courseIdCountMap.get("M" + course.getId()) == null ? 0 : courseIdCountMap.get("M" + course.getId()));
            integer += (courseIdCountMap.get("F" + course.getId()) == null ? 0 : courseIdCountMap.get("F" + course.getId()));
            courseNameCountMap.put(course.getSubjectName(), integer);

            // 单科选择人数
            NewGkReportChose courseNewGkReportChose = new NewGkReportChose();
            courseNewGkReportChose.setId(UuidUtils.generateUuid());
            courseNewGkReportChose.setUnitId(unitId);
            courseNewGkReportChose.setReportId(newGkReport.getId());
            courseNewGkReportChose.setBoyNumber(courseIdCountMap.get("M" + course.getId()) == null ? 0 : courseIdCountMap.get("M" + course.getId()));
            courseNewGkReportChose.setGirlNumber(courseIdCountMap.get("F" + course.getId()) == null ? 0 : courseIdCountMap.get("F" + course.getId()));
            courseNewGkReportChose.setDataType(NewGkElectiveConstant.REPORT_CHOSE_TYPE_05);
            courseNewGkReportChose.setDataKeys(course.getId());
            courseNewGkReportChose.setCreationTime(new Date());
            courseNewGkReportChose.setModifyTime(new Date());
            newGkReportChoseList.add(courseNewGkReportChose);
        }

        //各科目选择结果
        courseNameCountMap = sortByValue(courseNameCountMap);
        //计算组合
        Integer[] cSize = new Integer[courseList.size()];
        for(int i = 0;i < courseList.size();i++){
            cSize[i] = i;
        }

        //三科目选择结果
        CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize,3);
        Integer[][] result = combineAlgorithm.getResutl();
        List<NewGkConditionDto> newConditionList3 = newGkChoiceService.findSubRes(courseList, dtoMap, result, 3);
        //新增过滤筛选组合不推荐课程
        if(CollectionUtils.isNotEmpty(objectValues)){
            for(String subjectIdstr : objectValues){
                String[] subjectIds = subjectIdstr.split(",");
                if (subjectIds.length == 3) {
                    for(NewGkConditionDto newCondition : newConditionList3){
                        Set<String> subs = newCondition.getSubjectIds();
                        if(subs.contains(subjectIds[0]) && subs.contains(subjectIds[1]) && subs.contains(subjectIds[2])){
                            newCondition.setLimitSubject(true);
                        }
                    }
                }
            }
        }
        for (NewGkConditionDto one : newConditionList3) {
            int male = 0;
            int female = 0;
            for (String stu : one.getStuIds()) {
                if (maleStudentIds.contains(stu)) {
                    male++;
                } else {
                    female++;
                }
            }

            // 三科组合
            if (one.getSumNum() > 0) {
                NewGkReportChose threeCourse = new NewGkReportChose();
                threeCourse.setId(UuidUtils.generateUuid());
                threeCourse.setUnitId(unitId);
                threeCourse.setReportId(newGkReport.getId());
                threeCourse.setBoyNumber(male);
                threeCourse.setGirlNumber(female);
                threeCourse.setDataType(NewGkElectiveConstant.REPORT_CHOSE_TYPE_04);
                threeCourse.setDataKeys(one.getSubjectIdstr());
                threeCourse.setCreationTime(new Date());
                threeCourse.setModifyTime(new Date());
                newGkReportChoseList.add(threeCourse);
            }
        }

        try {
            newGkReportService.saveAllChoseList(newGkReport, newGkReportChoseList);
        } catch (Exception e) {
            e.printStackTrace();
            return error("上报失败");
        }
        return success("上报完成");
    }

    private Map<String, Integer> sortByValue(Map<String, Integer> map) {
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });
        Map<String, Integer> result = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
