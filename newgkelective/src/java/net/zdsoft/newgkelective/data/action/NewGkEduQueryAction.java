package net.zdsoft.newgkelective.data.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.NewGkChoResultDto;
import net.zdsoft.newgkelective.data.dto.NewGkConditionDto;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkChoiceService;
import net.zdsoft.newgkelective.data.utils.CombineAlgorithmInt;

/**
 * 
 * @author weixh
 * @since 2018年3月30日 下午2:37:27
 */
@RequestMapping("/newgkelective")
@Controller
public class NewGkEduQueryAction extends BaseAction {
	@Autowired
	private NewGkChoiceService newGkChoiceService;
	@Autowired
	private NewGkChoResultService newGkChoResultService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	
	private String currentAcadyear;
	private Map<String, Integer> courseOrders = new HashMap<String, Integer>();
	private String nowUnitId;
	
	@ControllerInfo("教育局查看选课情况首页")
	@RequestMapping("/choiceResult/query/index/page")
	public String index(HttpServletRequest request, ModelMap map) {
		String unitName = StringUtils.trimToEmpty(request.getParameter("searchUnitName"));
		LoginInfo li = getLoginInfo();
		String unitId = li.getUnitId();
		map.put("queryUnitId", unitId);
		map.put("queryUnitClass", li.getUnitClass());
		Unit su = Unit.dc(unitRemoteService.findOneById(unitId));
		List<Unit> units = Unit.dt(unitRemoteService.findByUnderlingUnits("%"+unitName+"%", su.getUnionCode()+"%"));
		if(units == null) {
			units = new ArrayList<Unit>();
		}
		if (units.size() > 1) {
			Collections.sort(units, new Comparator<Unit>() {

				@Override
				public int compare(Unit o1, Unit o2) {
					if(o1.getUnitClass()!=null && o2.getUnitClass()!=null &&!o1.getUnitClass().equals(o2.getUnitClass())) {
						return o1.getUnitClass()-o2.getUnitClass();
					}
					if(o1.getUnionCode()!=null && o2.getUnionCode()!=null && !o1.getUnionCode().equals(o2.getUnionCode())) {
						return o1.getUnionCode().compareTo(o2.getUnionCode());
					}
					return StringUtils.trimToEmpty(o1.getDisplayOrder())
							.compareTo(StringUtils.trimToEmpty(o2.getDisplayOrder()));
				}
			});
		}
		map.put("units", units);
		map.put("searchUnitName", unitName);
		return "/newgkelective/result/eduChoiceResultIndex.ftl";
	}
	
	@ControllerInfo("教育局查看选课情况首页")
	@RequestMapping("/choiceResult/query/data/page")
	public String dataPage(String queryUnitId, HttpServletRequest request, ModelMap map) {
		Unit nowUnit = Unit.dc(unitRemoteService.findOneById(queryUnitId));
		nowUnitId = queryUnitId;
		int queryUnitClass = nowUnit.getUnitClass();
		String gradeCode = request.getParameter("gradeCode");
//		RedisUtils.del("REDIS.NEWGKCHOICERESULT.NUM."+queryUnitId+gradeCode);// TODO
		currentAcadyear = null; 
		Semester cs = Semester.dc(semesterRemoteService.getCurrentSemester(1));
		if(cs != null) {
			currentAcadyear = cs.getAcadyear();
		}
		NewGkChoResultDto dto = getUnitData(queryUnitId, gradeCode, queryUnitClass);
		if(queryUnitClass == Constant.CLASS_SCH) {
			NewGkChoResultDto edto = getUnitData(nowUnit.getParentId(), gradeCode, Constant.CLASS_EDU);
			dto = dealSchData(dto, edto);
		}
		map.put("dto", dto);
		map.put("queryUnitId", queryUnitId);
		if(queryUnitClass == Constant.CLASS_SCH) {
			return "/newgkelective/result/schChoiceResultData.ftl";
		}
		return "/newgkelective/result/eduChoiceResultData.ftl";
	}
	
	/**
	 * 获取单位数据
	 * @param unitId 单位id
	 * @param gradeCode 年级code
	 * @param unitClass 单位分类
	 * @return
	 */
	private NewGkChoResultDto getUnitData(String unitId, String gradeCode, int unitClass) {
		NewGkChoResultDto dto = RedisUtils.getObject("REDIS.NEWGKCHOICERESULT.NUM."+unitId+gradeCode, RedisUtils.TIME_HALF_HOUR, 
				NewGkChoResultDto.class, new RedisInterface<NewGkChoResultDto>() {
					@Override
					public NewGkChoResultDto queryData() {
						if(unitClass == Constant.CLASS_SCH) {
							return getSchData(unitId, gradeCode);
						}
						return getEduData(unitId, gradeCode);
					}
		});
		return dto;
	}
	
	/**
	 * 整理学校页面显示数据
	 * @param dto 学校dto
	 * @param edto 上级教育局dto
	 * @return
	 */
	private NewGkChoResultDto dealSchData(NewGkChoResultDto dto, NewGkChoResultDto edto) {
		Set<String> subjectNames=new HashSet<String>();
        if (CollectionUtils.isNotEmpty(dto.getSubNames())) {
			subjectNames.addAll(dto.getSubNames());
		}
        if (CollectionUtils.isNotEmpty(edto.getSubNames())) {
			subjectNames.addAll(edto.getSubNames());
		}
        Set<String> subjectNames3=new HashSet<String>();
        if (CollectionUtils.isNotEmpty(dto.getSubName3())) {
        	subjectNames3.addAll(dto.getSubName3());
		}
        if (CollectionUtils.isNotEmpty(edto.getSubName3())) {
        	subjectNames3.addAll(edto.getSubName3());
		}
        Map<String, String> snId = new HashMap<String, String>();
        if (MapUtils.isNotEmpty(dto.getSubId())) {
        	snId.putAll(dto.getSubId());
		}
        if (MapUtils.isNotEmpty(edto.getSubId())) {
        	snId.putAll(edto.getSubId());
		}
        Map<String, String> snId3 = new HashMap<String, String>();
        if (MapUtils.isNotEmpty(dto.getSubId3())) {
        	snId3.putAll(dto.getSubId3());
		}
        if (MapUtils.isNotEmpty(edto.getSubId3())) {
        	snId3.putAll(edto.getSubId3());
		}
        int chNum = dto.getChooseStu();
        Map<String, Integer> snum = dto.getSubNums();
        Map<String, Integer> snum3 = dto.getSubNums3();
		
        NewGkChoResultDto dataDto = dataToJson(chNum, subjectNames, snum, snId, subjectNames3, snum3, snId3);
		dto.setJsonStringData1(dataDto.getJsonStringData1());
		dto.setJsonStringDataStr1(dataDto.getJsonStringDataStr1());
		dto.setEduJsonStringDataStr1(edto.getJsonStringDataStr1());
        
		dto.setJsonStringData3(dataDto.getJsonStringData3());
		dto.setJsonStringDataStr3(dataDto.getJsonStringDataStr3());
		dto.setEduJsonStringDataStr3(edto.getJsonStringDataStr3());
		return dto;
	}
	
	/**
	 * 组装json数据
	 * @param chNum 已选人数
	 * @param sns 单科名称
	 * @param snum 单科人数
	 * @param snId 
	 * @param sns3 三科名称
	 * @param snum3 三科人数
	 * @param snId3 
	 * @return
	 */
	private NewGkChoResultDto dataToJson(int chNum, Set<String> sns, Map<String, Integer> snum,
			Map<String, String> snId, Set<String> sns3, Map<String, Integer> snum3, Map<String, String> snId3) {
		NewGkChoResultDto dto = new NewGkChoResultDto();
		if(snum == null) {
			snum = new HashMap<String, Integer>();
		}
		if(snum3 == null) {
			snum3 = new HashMap<String, Integer>();
		}
		List<String> subjectNames=new ArrayList<String>();
		if(CollectionUtils.isNotEmpty(sns)) {
			subjectNames.addAll(sns);
		}
		if (subjectNames.size() > 0) {
			if(MapUtils.isEmpty(courseOrders)) {
				List<Course> css = SUtils.dt(courseRemoteService.findByCodes73(nowUnitId), new TR<List<Course>>() {});
				for(Course cs : css) {
					if(cs.getOrderId() == null) {
						cs.setOrderId(Integer.MAX_VALUE);
					}
					courseOrders.put(cs.getSubjectName(), cs.getOrderId());
				}
			}
			Integer maxInt = new Integer(Integer.MAX_VALUE);
			Collections.sort(subjectNames,
					(x, y) -> (courseOrders.containsKey(y) ? courseOrders.get(y) : maxInt)
							.compareTo((courseOrders.containsKey(x) ? courseOrders.get(x)
									: maxInt)));
			boolean hasId = MapUtils.isNotEmpty(snId);
			
			JSONObject json = new JSONObject();
			json.put("legendData", subjectNames.toArray(new String[0]));
			JSONArray jsonArr = new JSONArray();
			JSONObject json1 = null;
			JSONArray jsonArr1 = new JSONArray();
			JSONObject json11 = null;
			for (String name : subjectNames) {
				Integer value = snum.get(name);
				if (value == null) {
					value = 0;
				}
				String sid = "";
				if(hasId && snId.containsKey(name)) {
					sid = snId.get(name);
				}
				
				json1 = new JSONObject();
				json1.put("value", value);
				json1.put("name", name);
				json1.put("subjectId", sid);
				jsonArr.add(json1);

				json11 = new JSONObject();
				if (chNum > 0) {
					json11.put("value", StringUtils.formatDecimal(value * 100.0 / chNum, 0, 0));
				} else {
					json11.put("value", 0);
				}
				json11.put("name", name);
				json11.put("subjectId", sid);
				jsonArr1.add(json11);
			}
			json.put("loadingData", jsonArr);
			json.put("loadingPerData", jsonArr1);
			dto.setJsonStringData1(json);
			String jsonStringData1 = json.toString();
			dto.setJsonStringDataStr1(jsonStringData1);
		}
		
		subjectNames.clear();
		subjectNames.addAll(sns3);
		if (subjectNames.size() > 0) {
			boolean hasId3 = MapUtils.isNotEmpty(snId3);
			Integer minInt = 0;
			// 按人数降序排
			final Map<String, Integer> snum4 = new HashMap<String, Integer>();
			snum4.putAll(snum3);
			Collections.sort(subjectNames, (x, y) -> ((snum4.containsKey(x) ? snum4.get(x) : minInt)
					.compareTo(snum4.containsKey(y) ? snum4.get(y) : minInt)));
			
			JSONObject jjson = new JSONObject();
			JSONArray jsonArr2 = new JSONArray();
			JSONObject json2 = null;
			JSONArray jsonArr22 = new JSONArray();
			JSONObject json22 = null;
			for (String name : subjectNames) {
				Integer value = snum3.get(name);
				if (value == null) {
					value = 0;
				}
				String sid = "";
				if(hasId3 && snId3.containsKey(name)) {
					sid = snId3.get(name);
				}
				
				json2 = new JSONObject();
				json2.put("value", value);
				json2.put("name", name);
				json2.put("subjectId", sid);
				jsonArr2.add(json2);

				json22 = new JSONObject();
				if (chNum > 0) {
					json22.put("value", StringUtils.formatDecimal(value * 100.0 / chNum, 0, 0));
				} else {
					json22.put("value", 0);
				}
				json22.put("name", name);
				json22.put("subjectId", sid);
				jsonArr22.add(json22);
			}
			
			jjson.put("loadingPerData", jsonArr22);
			jjson.put("legendData", subjectNames.toArray(new String[0]));
			jjson.put("loadingData", jsonArr2);
			dto.setJsonStringData3(jjson);
			String jsonStringData3 = jjson.toString();
			dto.setJsonStringDataStr3(jsonStringData3);
		}
		return dto;
	}
	
	/**
	 * 教育局数据
	 * @param eduId
	 * @param gradeCode
	 * @return
	 */
	private NewGkChoResultDto getEduData(String eduId, String gradeCode) {
		NewGkChoResultDto dto = new NewGkChoResultDto();
		List<Unit> units = Unit.dt(unitRemoteService.findDirectUnits(eduId, Constant.CLASS_SCH));
//		units.add(Unit.dc(unitRemoteService.findById("2ED9CFC8AA38432D81C6370A16731CC9")));
		if(CollectionUtils.isEmpty(units)) {
			return dto;
		}
		dto.setSchNum(units.size());
		List<NewGkChoResultDto> dtos = new ArrayList<NewGkChoResultDto>();
		for(Unit sch : units) {
			dtos.add(getUnitData(sch.getId(), gradeCode, Constant.CLASS_SCH));
		}
		int stuNum = 0;
		int chNum = 0;
		int mn = 0;
		int fn = 0;
		Set<String> sns = new HashSet<String>();
		Set<String> sns3 = new HashSet<String>();
        Map<String, Integer> snum = new HashMap<String, Integer>();
        Map<String, Integer> snum3 = new HashMap<String, Integer>();
        Map<String, String> courseIdMap = new HashMap<String, String>();
        Map<String, String> courseId3Map = new HashMap<String, String>();
		for(NewGkChoResultDto tt : dtos) {
			if (CollectionUtils.isNotEmpty(tt.getSubNames())) {
				sns.addAll(tt.getSubNames());
			}
			if(MapUtils.isNotEmpty(tt.getSubNums())) {
				Iterator<Entry<String, Integer>> it1 = tt.getSubNums().entrySet().iterator();
				while(it1.hasNext()) {
					Entry<String, Integer> en = it1.next();
					if(!snum.containsKey(en.getKey())) {
						snum.put(en.getKey(), en.getValue());
					} else {
						snum.put(en.getKey(), snum.get(en.getKey()) + en.getValue());
					}
				}
			}
			if (CollectionUtils.isNotEmpty(tt.getSubName3())) {
				sns3.addAll(tt.getSubName3());
			}
			if(MapUtils.isNotEmpty(tt.getSubNums3())) {
				Iterator<Entry<String, Integer>> it1 = tt.getSubNums3().entrySet().iterator();
				while(it1.hasNext()) {
					Entry<String, Integer> en = it1.next();
					if(!snum3.containsKey(en.getKey())) {
						snum3.put(en.getKey(), en.getValue());
					} else {
						snum3.put(en.getKey(), snum3.get(en.getKey()) + en.getValue());
					}
				}
			}
			if(MapUtils.isNotEmpty(dto.getSubId())) {
				courseIdMap.putAll(dto.getSubId());
			}
			if(MapUtils.isNotEmpty(dto.getSubId3())) {
				courseId3Map.putAll(dto.getSubId3());
			}
			
			stuNum += tt.getAllStu();
			chNum += tt.getChooseStu();
			mn += tt.getMaleNum();
			fn += tt.getFemaleNum();
		}
		dto.setMaleNum(mn);
		dto.setFemaleNum(fn);
		dto.setAllStu(stuNum);
		dto.setChooseStu(chNum);
		dto.setSubNames(sns);
		dto.setSubName3(sns3);
		dto.setSubNums(snum);
		dto.setSubNums3(snum3);
		
        NewGkChoResultDto dataDto = dataToJson(chNum, sns, snum, courseIdMap, sns3, snum3, courseId3Map);
		dto.setJsonStringData1(dataDto.getJsonStringData1());
		dto.setJsonStringDataStr1(dataDto.getJsonStringDataStr1());
		dto.setJsonStringData3(dataDto.getJsonStringData3());
		dto.setJsonStringDataStr3(dataDto.getJsonStringDataStr3());
		
		return dto;
	}
	
	/**
	 * 学校数据
	 * @param schoolId
	 * @param gradeCode
	 * @return
	 */
	private NewGkChoResultDto getSchData(String schoolId, String gradeCode) {
		NewGkChoResultDto dto = new NewGkChoResultDto();
		String grstrs;
		if (StringUtils.isNotEmpty(currentAcadyear)) {
			int gradeInt = NumberUtils.toInt(gradeCode.substring(1));
			String[] years = currentAcadyear.split("-");
			int year = NumberUtils.toInt(years[1])-gradeInt;
			grstrs = gradeRemoteService.findBySchidSectionAcadyear(schoolId, year + "-" + (year+1),
					new Integer[] { BaseConstants.SECTION_HIGH_SCHOOL });
		} else {
			grstrs = gradeRemoteService.findByUnitIdAndGradeCode(schoolId, new String[] {gradeCode});
		}
		
		List<Grade> grades = SUtils.dt(grstrs, new TR<List<Grade>>() {});
		if(CollectionUtils.isEmpty(grades)) {
			return dto;
		}
		String gradeId = grades.get(0).getId();
		List<Student> studentList=SUtils.dt(studentRemoteService.findPartStudByGradeId(schoolId, gradeId,null, null),new TR<List<Student>>(){});
		dto.setAllStu(studentList.size());
		NewGkChoice choice = newGkChoiceService.findDefaultByGradeId(gradeId);
		if(choice == null) {
			List<NewGkChoice> gcs = newGkChoiceService.findListByGradeId(gradeId);
			if(CollectionUtils.isEmpty(gcs)) {
				return dto;
			}
			choice = gcs.get(0);
		}
		String choiceId = choice.getId();
		dto.setChoiceId(choiceId);
		Map<String, Student> stuSexMap = EntityUtils.getMap(studentList, Student::getId);
		//获取选考科目
		Map<String, List<NewGkChoRelation>> NewGkChoRelationAllMap= newGkChoRelationService.findByChoiceIdAndObjectTypeIn(schoolId,choiceId,new String[]{NewGkElectiveConstant.CHOICE_TYPE_01
				,NewGkElectiveConstant.CHOICE_TYPE_04});
		List<NewGkChoRelation> newGkChoRelationList = NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_01);
		List<String> courseIds = EntityUtils.getList(newGkChoRelationList, NewGkChoRelation::getObjectValue);
		if(CollectionUtils.isEmpty(courseIds)) {
			return dto;
		}
		List<Course> courseList = SUtils.dt(courseRemoteService.orderCourse(courseRemoteService.findListByIds(courseIds.toArray(new String[0]))), new TR<List<Course>>(){});
		
		//不参与选课排课人员
		List<NewGkChoRelation> notvals= NewGkChoRelationAllMap.get(NewGkElectiveConstant.CHOICE_TYPE_04);
		//获取所有的选课结果
		List<NewGkChoResult>  resultList=newGkChoResultService.findByChoiceIdAndKindType(schoolId,NewGkElectiveConstant.KIND_TYPE_01,choiceId);
		//已选学生ids
		Set<String> chosenStuIds=new HashSet<String>();
		//获取学生对应的选课ids
		Map<String,NewGkChoResultDto> dtoMap=new HashMap<String,NewGkChoResultDto>();
		Map<String, Integer> courseIdCountMap= new HashMap<String, Integer>();
		Set<String> ms = new HashSet<String>();
		Set<String> fs = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(resultList)){
			NewGkChoResultDto temp=new NewGkChoResultDto();
			for(NewGkChoResult result:resultList){
				if(!stuSexMap.containsKey(result.getStudentId())) {
					continue;
				}
				chosenStuIds.add(result.getStudentId());
				Integer sex = stuSexMap.get(result.getStudentId()).getSex();
				if(sex == null) {
					sex = 0;
				}
				if(sex == BaseConstants.MALE) {
					ms.add(result.getStudentId());
				} else if(sex == BaseConstants.FEMALE) {
					fs.add(result.getStudentId());
				}
				if(!dtoMap.containsKey(result.getStudentId())){
					temp = new NewGkChoResultDto();
					temp.setChooseSubjectIds(new HashSet<String>());
					temp.setStudentId(result.getStudentId());
	                dtoMap.put(result.getStudentId(), temp);
	            }
	            dtoMap.get(result.getStudentId()).getChooseSubjectIds().add(result.getSubjectId());
	            
	            Integer integer = courseIdCountMap.get(result.getSubjectId());
		    	if(integer == null){
		    		integer = 0;
		    	}
		    	courseIdCountMap.put(result.getSubjectId(), ++integer);
			}
		}
		dto.setMaleNum(ms.size());
		dto.setFemaleNum(fs.size());
		Map<String, Integer> courseNameCountMap = new HashMap<String, Integer>();
		int order=0;
		Map<String, String> courseIdMap = new HashMap<String, String>();
		for(Course course : courseList){
			courseIdMap.put(course.getSubjectName(), course.getId());
//			System.out.println(course.getSubjectName()+"=="+course.getOrderId()+"="+order);
			courseOrders.put(course.getSubjectName(), order++);
			Integer integer = courseIdCountMap.get(course.getId());
			if(integer==null) integer=0;
			courseNameCountMap.put(course.getSubjectName(), integer);
		}
		
		int val=0;
        if(CollectionUtils.isNotEmpty(notvals)){
        	val = notvals.size();
        }
		dto.setChooseStu(chosenStuIds.size());
        dto.setNotChooseStu(studentList.size()-chosenStuIds.size()-val);
        int csn = dto.getChooseStu(); 
        if(csn == 0) {
        	csn = 1;
        }
        
        List<String> subjectNames=new ArrayList<String>();
        subjectNames.addAll(courseNameCountMap.keySet());
        Set<String> sns = new HashSet<String>();
        sns.addAll(subjectNames);
        dto.setSubNames(sns);
        dto.setSubId(courseIdMap);
        Map<String, Integer> snum = new HashMap<String, Integer>();
		for(String name:subjectNames){
			Integer value=courseNameCountMap.get(name);
			if(value == null) {
				value=0;
			}
			
			Integer sn = snum.get(name);
			if(sn == null) {
				sn = 0;
			}
			sn+= value;
			snum.put(name, sn);
		}
		dto.setSubNums(snum);
		
        //计算组合
        Integer[] cSize = new Integer[courseList.size()];
        for(int i = 0;i < courseList.size();i++){
        	cSize[i] = i;
        }
        
        //三科目选择结果
        CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize,3);
        Integer[][] result = combineAlgorithm.getResutl();
        List<NewGkConditionDto> newConditionList3=newGkChoiceService.findSubRes(courseList, dtoMap, result, 3);
		Set<String> sns3 = new HashSet<String>();
		Map<String, Integer> snum3 = new HashMap<String, Integer>();
		Map<String, String> snId3 = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(newConditionList3)){
			for(int i=newConditionList3.size()-1;i>=0;i--){
				NewGkConditionDto newDto=newConditionList3.get(i);
				sns3.add(newDto.getSubShortNames());
				
				if(!snId3.containsKey(newDto.getSubShortNames())) {
					snId3.put(newDto.getSubShortNames(), newDto.getSubjectIdstr());
				}
				
				Integer sn = snum3.get(newDto.getSubShortNames());
				if(sn == null) {
					sn = 0;
				}
				sn+= newDto.getSumNum();
				snum3.put(newDto.getSubShortNames(), sn);
			}
		}
		dto.setSubName3(sns3);
		dto.setSubNums3(snum3);
		dto.setSubId3(snId3);
		return dto;
	}
	
	/**
	 * 按值降序排序
	 */
	public static Map<String, Integer> sortByValue(Map<String, Integer> map) {
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
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
