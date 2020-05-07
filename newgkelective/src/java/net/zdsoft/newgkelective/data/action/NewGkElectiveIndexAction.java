package net.zdsoft.newgkelective.data.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.GradeTeaching;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.GradeTeachingRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.NewGkChoiceDto;
import net.zdsoft.newgkelective.data.dto.NewGkDivideDto;
import net.zdsoft.newgkelective.data.dto.NewGkItemDto;
import net.zdsoft.newgkelective.data.dto.PageInfo;
import net.zdsoft.newgkelective.data.entity.NewGkArray;
import net.zdsoft.newgkelective.data.entity.NewGkArrayItem;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkOpenSubject;
import net.zdsoft.newgkelective.data.entity.NewGkReferScore;
import net.zdsoft.newgkelective.data.service.NewGkArrayItemService;
import net.zdsoft.newgkelective.data.service.NewGkArrayService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkChoiceService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkOpenSubjectService;
import net.zdsoft.newgkelective.data.service.NewGkReferScoreService;

@Controller
@RequestMapping("/newgkelective")
public class NewGkElectiveIndexAction extends NewGkRoleCommonAction {
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private NewGkDivideService newGkDivideService;
	@Autowired
	private NewGkChoResultService newGkChoResultService;
	@Autowired
	private NewGkChoiceService newGkChoiceService;
	@Autowired
	private NewGkReferScoreService newGkReferScoreService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private NewGkArrayItemService newGkArrayItemService;
	@Autowired
	private NewGkArrayService newGkArrayService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private GradeTeachingRemoteService gradeTeachingRemoteService;
	@Autowired
	private NewGkOpenSubjectService newGkOpenSubjectService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	
	@RequestMapping("/index/page")
	@ControllerInfo(value = "7选3index")
	public String index(ModelMap map) {
		return "/newgkelective/arrange/index.ftl";
	}

	@RequestMapping("/index/list/page")
	@ControllerInfo(value = "显示list")
	public String indexList(ModelMap map) {
		LoginInfo loginInfo = getLoginInfo();
		String schoolId = loginInfo.getUnitId();
		
		// 默认只取高中
//		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolIdAndSection(schoolId,NewGkElectiveConstant.SECTION_3),new TR<List<Grade>>() {});
		List<Grade> gradeList = gradeRemoteService.findListObjectBy(new String[] {"schoolId", "isDeleted", "section", "isGraduate"}, new Object[] {schoolId, 0, NewGkElectiveConstant.SECTION_3, "0"} ); 
		Collections.sort(gradeList, (x,y)->y.getOpenAcadyear().compareTo(x.getOpenAcadyear()));
		map.put("gradeList", gradeList);
		
		Map<String, List<String>> roleMap = findRoleByUserId(schoolId, loginInfo.getUserId());
		List<String> gradeIdList = EntityUtils.getList(gradeList, Grade::getId);
		if(MapUtils.isNotEmpty(roleMap)){
			if(roleMap.containsKey(ROLE_GRADE)){
				List<String> collect = roleMap.get(ROLE_GRADE).stream().filter(e->gradeIdList.contains(e)).collect(Collectors.toList());
				if(CollectionUtils.isNotEmpty(collect)){
					roleMap.put(ROLE_GRADE, collect);
				}else{
					roleMap.remove(ROLE_GRADE);
				}
			}
			if(roleMap.containsKey(ROLE_CLASS)){
				List<String> classIdList = roleMap.get(ROLE_CLASS);
				List<Clazz> clazzList = SUtils.dt(classRemoteService.findListByIds(classIdList.toArray(new String[0])),Clazz.class);
				Set<String> gradeIdSet = EntityUtils.getSet(clazzList, Clazz::getGradeId);
				List<String> collect = gradeIdSet.stream().filter(e->gradeIdList.contains(e)).collect(Collectors.toList());
				if(CollectionUtils.isNotEmpty(collect)){
					roleMap.put(ROLE_CLASS, collect);
				}else{
					roleMap.remove(ROLE_CLASS);
				}
			}
		}
		
		map.put("roleMap", roleMap);
		
		return "/newgkelective/arrange/indexList.ftl";
	}
	
	/**
	 * 选课页面
	 */
	@RequestMapping("/{gradeId}/goChoice/index/page")
	public String showChoiceIndex(@PathVariable("gradeId") String gradeId, PageInfo pageInfo,
			@RequestParam(name="choiceId",required=false) String choiceId, String isMaster,
			ModelMap map) {
		//权限控制
		LoginInfo loginInfo = getLoginInfo();
		Map<String, List<String>> roleMap = findRoleByUserId(loginInfo.getUnitId(), loginInfo.getUserId());
		List<String> classIdList = null;
		if(MapUtils.isNotEmpty(roleMap) 
				&& !( roleMap.containsKey(ROLE_GRADE) && roleMap.get(ROLE_GRADE).contains(gradeId))  
				&& roleMap.containsKey(ROLE_CLASS)){
			map.put("isClassRole", true);
			classIdList = roleMap.get(ROLE_CLASS);
			List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(loginInfo.getUnitId(), gradeId),Clazz.class);
			List<String> thisGradeClass = EntityUtils.getList(classList, Clazz::getId);
			classIdList = classIdList.stream().filter(e->thisGradeClass.contains(e)).collect(Collectors.toList());
		}
		
		//固定每页3条记录
		pageInfo.setPageSize(3);
		
		map.put("gradeId", gradeId);
		List<NewGkChoiceDto> newGkChoiceDtoList;
		
		if(StringUtils.isNotBlank(choiceId)) {
			NewGkChoice choice;
			List<NewGkChoice> newGkChoiceList;
			if(Constant.IS_TRUE_Str.equals(isMaster)){
				choice = newGkChoiceService.findOneWithMaster(choiceId);
				newGkChoiceList = newGkChoiceService.findListByGradeIdWithMaster(gradeId);
			}else{
				choice = newGkChoiceService.findOne(choiceId);
				newGkChoiceList = newGkChoiceService.findListByGradeId(gradeId);
			}
			newGkChoiceDtoList=newGkChoiceService.makeChoiceChart(gradeId, Arrays.asList(choice),classIdList);
			map.put("allList", newGkChoiceList);
			map.put("choiceId",choiceId);
		}else {
			// 封装
			newGkChoiceDtoList = getChoiceChartByGradeId(gradeId, pageInfo, isMaster, classIdList, map);
			pageInfo.makeShowCount();
			map.put("pageInfo", pageInfo);
		}

		map.put("newGkChoiceDtoList", newGkChoiceDtoList);
		
		return "/newgkelective/choice/choiceIndex.ftl";
	}

	/**
	 * 分班页面
	 */
	@RequestMapping("/{gradeId}/goDivide/index/page")
	public String showDivideIndex(@PathVariable("gradeId") String gradeId,
			@RequestParam(name="divideId",required=false) String divideId, PageInfo pageInfo,
			ModelMap map) {
		//新增后返回首页 删除后刷新首页 所以需要直接查主表
		map.put("gradeId", gradeId);
		//右上角查询列表
		List<NewGkDivide> divideList = newGkDivideService.findByGradeIdWithMaster(
				getLoginInfo().getUnitId(), gradeId,null);
		map.put("allList", divideList);
		List<NewGkDivideDto> dtos = null;
		if(StringUtils.isNotBlank(divideId)) {
			Map<String, NewGkDivide> divideMap=new HashMap<>();
			if(CollectionUtils.isNotEmpty(divideList)) {
				divideMap=EntityUtils.getMap(divideList, NewGkDivide::getId);
			}
			NewGkDivide divide = divideMap.get(divideId);
			if(divide!=null && NewGkElectiveConstant.IF_0.equals(divide.getIsDeleted()+"")) {
				dtos =newGkDivideService.makeDivideItem(Arrays.asList(divide));
				map.put("divideId", divideId);
			}
		}
		if(dtos == null) {
			pageInfo.setItemsNum(divideList.size());
			pageInfo.refresh();
			pageInfo.makeShowCount();
			//[2,5,10,15]
			List<Integer> pageList = new ArrayList<>();
			pageList.add(2);
			pageList.add(5);
			pageList.add(10);
			pageList.add(15);
			pageInfo.setPageList(pageList);
			divideList = divideList.subList(pageInfo.getStartIndex()-1, pageInfo.getEndIndex());
			dtos =newGkDivideService.makeDivideItem(divideList);
		}
		map.put("dtos", dtos);

		// 技术拆分
        List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitCourseCodes(getLoginInfo().getUnitId(), new String[]{"3037"}), Course.class);
        if (CollectionUtils.isNotEmpty(courseList)) {
            map.put("technologyId", courseList.get(0).getId());
        }
        return "/newgkelective/divide/divideIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/{gradeId}/goDivide/saveName")
	@ControllerInfo(value="修改名称")
	public String saveDivideName(@PathVariable String gradeId, String divideId, String divideName) {
		List<NewGkDivide> divideList = newGkDivideService.findByGradeId(
				getLoginInfo().getUnitId(), gradeId, null);
		
		if("create".equals(divideId)) {
			for(NewGkDivide ch : divideList) {
				if(ch.getDivideName().equals(divideName)) {
					return error("该名称已被其他记录使用！");
				}
			}
		}else {
			if(CollectionUtils.isNotEmpty(divideList)) {
				NewGkDivide toCh = null;
				for(NewGkDivide ch : divideList) {
					if(ch.getId().equals(divideId)) {
						toCh = ch;
						//toCh.setBatchCountTypea(3);
					} else if(ch.getDivideName().equals(divideName)) {
						return error("该名称已被其他记录使用！");
					}
				}
				if(toCh == null) {
					return error("分班记录不存在或已被删除！");
				}
				toCh.setDivideName(divideName);
				toCh.setModifyTime(new Date());
				newGkDivideService.save(toCh);
			} else {
				return error("分班记录不存在或已被删除！");
			}
		}
		
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/{gradeId}/goDivide/setDefault")
	@ControllerInfo(value="置为默认")
	public String setDefault(@PathVariable String gradeId, String divideId,String stat) {
		if("0".equals(stat)){
			//置为默认
			List<NewGkDivide> divideList = newGkDivideService.findByGradeId(
					getLoginInfo().getUnitId(), gradeId, null);
			if(CollectionUtils.isNotEmpty(divideList)) {
				boolean hasDefault = false;
				List<NewGkDivide> toSave = new ArrayList<NewGkDivide>();
				for(NewGkDivide ch : divideList) {
					if(ch.getIsDeleted() == 1
							|| (!StringUtils.equals(ch.getId(), divideId) && ch.getIsDefault() == 0)) {
						continue;
					}
					
					if(StringUtils.equals(ch.getId(), divideId)) {
						ch.setIsDefault(1);
						hasDefault = true;
					} else {
						ch.setIsDefault(0);
					}
					ch.setModifyTime(new Date());
					toSave.add(ch);
				}
				if (hasDefault && toSave.size() > 0) {
					newGkDivideService.saveAll(divideList.toArray(new NewGkDivide[0]));
					return success("操作成功！");
				}
			}
			return error("没有可置为默认的分班记录！");
		}else if("1".equals(stat)){
			//取消默认
			NewGkDivide ch=newGkDivideService.findById(divideId);
			if(ch!=null){
				ch.setIsDefault(0);
//				ch.setStat("0");
				ch.setModifyTime(new Date());
				newGkDivideService.save(ch);
				return success("操作成功！");
			}
			
			return error("没有可取消默认的分班记录！");
		}else{
			return error("操作失败！");
		}
	}

	/**
	 * 新增分班方案
	 */
	@RequestMapping("/{gradeId}/goDivide/addDivide/page")
	@ControllerInfo(value = "新增分班方案")
	public String addDivideIndex(@PathVariable("gradeId") String gradeId, ModelMap map ) {
		String unitId=getLoginInfo().getUnitId();
		//不显示具体成绩
		List<NewGkReferScore> referScoreList = newGkReferScoreService.findListByGradeId(unitId, gradeId,false,false);
//		List<NewGkReferScore> referScoreList = newGkReferScoreService.findListByGradeId(unitId, gradeId,true,false);
		//开班科目
		NewGkDivide newGkDivide=new NewGkDivide();
		// 不封装选课结果图表
		List<NewGkChoice> newGkChoiceList = newGkChoiceService.findListByGradeId(gradeId);
		// 封装选课结果图表
		List<NewGkChoiceDto> newGkChoiceDtoList = getChoiceChartByGradeId(gradeId,null,null,null,null);
		map.put("newGkChoiceList", newGkChoiceList);
		//遗留默认选中项
		if(CollectionUtils.isNotEmpty(newGkChoiceDtoList)){
			String ccid = null;
			for(NewGkChoiceDto dto : newGkChoiceDtoList) {
				if(dto.isDefault()) {
					ccid = dto.getChoiceId();
					break;
				}
			}
			if(StringUtils.isEmpty(ccid)) {
				ccid = newGkChoiceDtoList.get(0).getChoiceId();
			}
			map.put("chooseChiceId", ccid);
		}
		
		newGkDivide.setGradeId(gradeId);
		map.put("newGkDivide", newGkDivide);
//		map.put("choiceList",choiceList);
		map.put("referScoreList",referScoreList);
		
		//用于筛选行政班科目
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){}) ;
		Semester semester=null;
        if(CollectionUtils.isNotEmpty(acadyearList)){
            semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2),Semester.class);
           
		}
        if(semester==null){
        	semester=new Semester();
        }
        map.put("semester", semester);
        map.put("acadyearList", acadyearList);
		
		
		
		//所有行政班科目---文理
		List<Course> xzbCourseList1 = findByGradeId(unitId, gradeId);
		List<Course> xzbCourseList=new ArrayList<Course>();
		if(CollectionUtils.isNotEmpty(xzbCourseList1)){//过滤下32个零的特殊数据
			for(Course ent: xzbCourseList1){
				if(!BaseConstants.ZERO_GUID.equals(ent.getCourseTypeId()))
					xzbCourseList.add(ent);
			}
		}
		map.put("xzbCourseList", xzbCourseList);
		
		//文科
		List<Course> wkCourseList =new ArrayList<Course>();
		Set<String> whSet = BaseConstants.SDZ_73;
		//理科
		List<Course> lkCourseList =new ArrayList<Course>();
		Set<String> lhSet = BaseConstants.WHS_73;
		//语数英
		List<Course> ysyCourseList =new ArrayList<Course>();
		List<String> ysylist = Arrays.asList(BaseConstants.SUBJECT_TYPES_YSY);
		for(Course c:xzbCourseList){
			if(whSet.contains(c.getSubjectCode())){
				wkCourseList.add(c);
			}else if(lhSet.contains(c.getSubjectCode())){
				lkCourseList.add(c);
			}else if(ysylist.contains(c.getSubjectCode())){
				ysyCourseList.add(c);
			}
		}
		map.put("wkCourseList", wkCourseList);
		map.put("lkCourseList", lkCourseList);
		map.put("ysyCourseList", ysyCourseList);
		
		int maxTimes=newGkDivideService.findMaxByGradeId(unitId, gradeId,false);
		
		//获取学年学期信息
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);	
		String divideName = semester.getAcadyear()+"学年"+grade.getGradeName()+
				"第"+semester.getSemester()+"学期分班方案"+(maxTimes+1);
		map.put("divideName", divideName);
		return "/newgkelective/divide/divideAdd1.ftl";
	}

	@ResponseBody
    @RequestMapping("/goDivide/chosenSubjectByChoiceId")
    @ControllerInfo(value = "查询科目信息")
	public String findSubjectByChoiceId(String choiceId){
		JSONObject obj=new JSONObject();
		obj.put("flag", true);
		List<Course> courseList=newGkChoRelationService.findChooseSubject(choiceId, getLoginInfo().getUnitId());
		//判断有没有物理，历史
		List<Course> courseWl=new ArrayList<>();
		Course cc=new Course();
		if(courseList.size()>6) {
			//不是3+1+2
			obj.put("flag", false);
			obj.put("msg", "该选课不是3+1+2(物理历史二选1)模式");
			obj.put("courseList", courseList);
			return obj.toJSONString();
		}else {
			for(Course c:courseList) {
				if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(c.getSubjectCode())) {
					cc=new Course();
					cc.setId(c.getId());
					cc.setSubjectName(c.getSubjectName());
					courseWl.add(cc);
				}else if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(c.getSubjectCode())) {
					cc=new Course();
					cc.setId(c.getId());
					cc.setSubjectName(c.getSubjectName());
					courseWl.add(cc);
				}
			}
			if(courseWl.size()!=2) {
				//不是物理历史的3+1+2obj.put("flag", false);
				obj.put("flag", false);
				obj.put("msg", "该选课不是3+1+2(物理历史二选1)模式");
				obj.put("courseList", courseList);
				return obj.toJSONString();
			}
		}
		obj.put("twoCourseList", courseWl);
		obj.put("courseList", courseList);
		return obj.toJSONString();
	}
	
	
	@ResponseBody
    @RequestMapping("/goDivide/chosenSubjectByAcadyear")
    @ControllerInfo(value = "查询行政班科目信息")
	public List<Course> chosenSubjectByAcadyear(String acadyear,String semester,String gradeId){
		List<GradeTeaching> gradeCourseList = SUtils.dt(gradeTeachingRemoteService.findBySearchList(getLoginInfo().getUnitId(), acadyear, semester, gradeId,Integer.parseInt(BaseConstants.SUBJECT_TYPE_BX)),new TR<List<GradeTeaching>>(){});
		List<Course> courseList=new ArrayList<Course>();
		if(CollectionUtils.isNotEmpty(gradeCourseList)){
			Set<String> subjectIdSet = EntityUtils.getSet(gradeCourseList, GradeTeaching::getSubjectId);
			//只取必修课
			courseList=SUtils.dt(courseRemoteService.findListByIds(subjectIdSet.toArray(new String[]{})),new TR<List<Course>>(){});
		}
		return courseList;
	}
	
	/**
	 * 查询所有科目
	 * @param 
	 * @return
	 */
	private List<Course> findByGradeId(String unitId,String gradeId){
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
		if(grade==null){
			return new ArrayList<Course>();
		}
		//只取必修课
		List<Course> courseList=SUtils.dt(courseRemoteService.findByUnitIdAndTypeAndLikeSection(unitId,BaseConstants.SUBJECT_TYPE_BX,NewGkElectiveConstant.SECTION_3+""),new TR<List<Course>>(){});
		return courseList;
	}
	
	@ResponseBody
    @RequestMapping("/goDivide/save")
    @ControllerInfo(value = "保存分班方案")
	public String saveDivide(NewGkDivideDto dto){
		NewGkDivide ent = dto.getEnt();
		String unitId = getLoginInfo().getUnitId();
		boolean flag=false;
		NewGkDivide oldDivide=null;
		if(StringUtils.isNotBlank(ent.getId())){
			oldDivide = newGkDivideService.findById(ent.getId());
		}
		if(oldDivide==null){
			//取得数据库最大值
			int maxTimes=newGkDivideService.findMaxByGradeId(unitId, ent.getGradeId(),false);
			
			String divideName = "";
			if(StringUtils.isBlank(ent.getDivideName())) {
				//获取学年学期信息
				String semesterJson = semesterRemoteService.getCurrentSemester(2, unitId);
				Semester semester = SUtils.dc(semesterJson, Semester.class);
				Grade grade = SUtils.dc(gradeRemoteService.findOneById(ent.getGradeId()), Grade.class);	
				divideName = semester.getAcadyear()+"学年"+grade.getGradeName()+
						"第"+semester.getSemester()+"学期分班方案"+(maxTimes+1);
			}else {
				divideName = ent.getDivideName();
			}
			
			flag=true;
			ent.setId(UuidUtils.generateUuid());
			ent.setCreationTime(new Date());
			ent.setModifyTime(new Date());
			ent.setUnitId(unitId);
			ent.setTimes(maxTimes+1);
			ent.setDivideName(divideName);
		}else{
			oldDivide.setModifyTime(new Date());
			oldDivide.setGalleryful(ent.getGalleryful());
			oldDivide.setMaxGalleryful(ent.getMaxGalleryful());
			ent=oldDivide;
		}
		
		if(StringUtils.isNotBlank(dto.getReferScoreId())){
			ent.setReferScoreId(dto.getReferScoreId());
		}
		ent.setOpenType(dto.getOpenType());
		ent.setChoiceId(dto.getChoiceId());
		ent.setStat(NewGkElectiveConstant.IF_0);
		ent.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
		
		List<NewGkOpenSubject> openSubjectList=new ArrayList<NewGkOpenSubject>();
		int asubjectNum=0;
		int bsubjectNum=0;
		
		if(NewGkElectiveConstant.DIVIDE_TYPE_09.equals(ent.getOpenType()) || NewGkElectiveConstant.DIVIDE_TYPE_10.equals(ent.getOpenType())) {
			if(StringUtils.isNotBlank(dto.getRecombination()) && NewGkElectiveConstant.IF_1.equals(dto.getRecombination())) {
				if(NewGkElectiveConstant.DIVIDE_TYPE_09.equals(ent.getOpenType())) {
					ent.setOpenType(NewGkElectiveConstant.DIVIDE_TYPE_11);
				}else {
					ent.setOpenType(NewGkElectiveConstant.DIVIDE_TYPE_12);
				}
			}
			//默认增加历史物理
			List<Course> courseList=newGkChoRelationService.findChooseSubject(ent.getChoiceId(), getLoginInfo().getUnitId());
			//判断有没有物理，历史
			List<Course> courseWl=new ArrayList<>();
			if(courseList.size()>6) {
				//不是3+1+2
				return returnError("保存失败！", "该选课不是3+1+2(物理历史二选1)模式");
			}else {
				for(Course c:courseList) {
					if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(c.getSubjectCode())) {
						courseWl.add(c);
					}else if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(c.getSubjectCode())) {
						courseWl.add(c);
					}
				}
				if(courseWl.size()!=2) {
					//不是物理历史的3+1+2obj.put("flag", false);
					return returnError("保存失败！", "该选课不是3+1+2(物理历史二选1)模式");
				}
				for(Course cc:courseWl) {
					if(dto.getCourseA().indexOf(cc.getId())==-1) {
						dto.setCourseA(dto.getCourseA()+","+cc.getId());
					}
					if(dto.getCourseB().indexOf(cc.getId())==-1){
						dto.setCourseB(dto.getCourseB()+","+cc.getId());
					}
				}
			}
		}
		
		
		if(!(NewGkElectiveConstant.DIVIDE_TYPE_03.equals(ent.getOpenType())
				|| NewGkElectiveConstant.DIVIDE_TYPE_04.equals(ent.getOpenType()))){
			
			initSubject(dto.getCourseA(), ent.getId(), NewGkElectiveConstant.SUBJECT_TYPE_A, NewGkElectiveConstant.DIVIDE_GROUP_1, openSubjectList);
			asubjectNum=openSubjectList.size();
			initSubject(dto.getCourseB(), ent.getId(), NewGkElectiveConstant.SUBJECT_TYPE_B, NewGkElectiveConstant.DIVIDE_GROUP_1, openSubjectList);
			bsubjectNum=openSubjectList.size()-asubjectNum;
			initSubject(dto.getCourseO(), ent.getId(), NewGkElectiveConstant.SUBJECT_TYPE_O, NewGkElectiveConstant.DIVIDE_GROUP_1, openSubjectList);
		}
//		else{
//			initSubject(dto.getCourselk(), ent.getId(), NewGkElectiveConstant.SUBJECT_TYPE_A, NewGkElectiveConstant.DIVIDE_GROUP_2, openSubjectList);
//			
//			initSubject(dto.getCoursewk(), ent.getId(), NewGkElectiveConstant.SUBJECT_TYPE_A, NewGkElectiveConstant.DIVIDE_GROUP_3, openSubjectList);
//			
//			initSubject(dto.getCourselO(), ent.getId(), NewGkElectiveConstant.SUBJECT_TYPE_O, NewGkElectiveConstant.DIVIDE_GROUP_2, openSubjectList);
//			
//			initSubject(dto.getCoursewO(), ent.getId(), NewGkElectiveConstant.SUBJECT_TYPE_O, NewGkElectiveConstant.DIVIDE_GROUP_3, openSubjectList);
//
//			if(NewGkElectiveConstant.DIVIDE_TYPE_03.equals(ent.getOpenType())){
//				//语数外独立dto.getCourselkysy()与dto.getCoursewkysy()暂时一致 只有语数英 理论上所以只需保存一个
//				initSubject(dto.getCourselkysy(), ent.getId(), NewGkElectiveConstant.SUBJECT_TYPE_J, NewGkElectiveConstant.DIVIDE_GROUP_4, openSubjectList);
//			}else{
//				initSubject(dto.getCourselkysy(), ent.getId(), NewGkElectiveConstant.SUBJECT_TYPE_J, NewGkElectiveConstant.DIVIDE_GROUP_2, openSubjectList);
//				
//				initSubject(dto.getCoursewkysy(), ent.getId(), NewGkElectiveConstant.SUBJECT_TYPE_J, NewGkElectiveConstant.DIVIDE_GROUP_3, openSubjectList);
//				
//			}
//			
//		}
		if(flag) {
			//新增
			NewGkChoice choice = newGkChoiceService.findById(ent.getChoiceId());
			int anum=3;
			int bnum=4;

			if(choice!=null) {
				int abath=choice.getChooseNum();
				List<String> list1 = newGkChoRelationService.findByChoiceIdAndObjectType(unitId, choice.getId(), NewGkElectiveConstant.CHOICE_TYPE_01);
				int allbath=list1.size();
				int bbath=allbath-abath;
				//一般情况A abath B bbath
				if(NewGkElectiveConstant.DIVIDE_TYPE_01.equals(ent.getOpenType())) {
					anum=1;
				}else {
					if(asubjectNum>0 && asubjectNum<abath) {
						anum=asubjectNum;
					}else {
						anum=abath;
					}
				}
				
				if(bbath>0) {
					if( bsubjectNum>0 && bsubjectNum < bbath) {
						bnum=bsubjectNum;
					}else {
						bnum=bbath;
					}
				}
				ent.setBatchCountTypea(anum);
				ent.setBatchCountTypeb(bnum);;
			}
		}
		if(NewGkElectiveConstant.DIVIDE_TYPE_12.equals(ent.getOpenType())) {
			//行政班上课合并班级模式，物理，历史
			ent.setFollowType(NewGkElectiveConstant.FOLLER_TYPE_A2+","+NewGkElectiveConstant.FOLLER_TYPE_B2);
		}
		
		// 取出选课数据，删除已经不存在的学生
		List<Student> gradeStuList = SUtils.dt(studentRemoteService.findPartStudByGradeId(unitId, ent.getGradeId(),null, null),Student.class);
		Set<String> gradeStuIds = EntityUtils.getSet(gradeStuList, Student::getId);
		Set<String> choiceStuIdList = newGkChoResultService.findSetByChoiceIdAndKindType(ent.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,ent.getChoiceId());
		// 要删除的学生ID
		List<String> delIds = choiceStuIdList.stream().filter(e->!gradeStuIds.contains(e)).collect(Collectors.toList());
		
		
		try{
 			newGkDivideService.saveDivide(ent,openSubjectList,flag);
 			newGkChoResultService.deleteByStudentIds(unitId, ent.getChoiceId(), delIds.toArray(new String[0]));
		}catch (Exception e) {
	        e.printStackTrace();
	        return returnError("保存失败！", e.getMessage());
	    }
		return success(ent.getId()==null?"success":ent.getId());
	}
	
	private void initSubject(String courseIds,String divideId,String subjectType,String groupType,List<NewGkOpenSubject> openSubjectList){
		NewGkOpenSubject openSubject=null;
		if(StringUtils.isNotBlank(courseIds)){
			String[] courseId=courseIds.split(",");
			for(String s:courseId){
				openSubject = toMake(subjectType,groupType);
				openSubject.setDivideId(divideId);
				openSubject.setSubjectId(s);
				openSubjectList.add(openSubject);
			}
		}
		
	}
	
	private NewGkOpenSubject toMake(String subjectType,String groupType){
		NewGkOpenSubject openSubject = new NewGkOpenSubject();
		openSubject.setCreationTime(new Date());
		openSubject.setId(UuidUtils.generateUuid());
		openSubject.setModifyTime(new Date());
		if(StringUtils.isNotBlank(subjectType)){
			openSubject.setSubjectType(subjectType);
		}
		openSubject.setGroupType(groupType);
		return openSubject;
	}
	
	
	/**
	 * 排课页面
	 */
	@RequestMapping("/{gradeId}/goArrange/index/page")
	public String showArrangeIndex(@PathVariable("gradeId") String gradeId,
			@RequestParam(name="arrayId",required=false) String arrayId, String useMaster, PageInfo pageInfo, ModelMap map) {
		map.put("gradeId", gradeId);
		List<NewGkArray> allList = null;
		if(Objects.equals(useMaster,"1")) {
			allList = newGkArrayService.findByGradeIdWithMaster(getLoginInfo().getUnitId(),gradeId,null,NewGkElectiveConstant.ARRANGE_SEVEN);
		}else {
			allList = newGkArrayService.findByGradeId(getLoginInfo().getUnitId(),gradeId,null,NewGkElectiveConstant.ARRANGE_SEVEN);
		}
		boolean makeCril=false;
		map.put("allList", allList);
		
		List<NewGkArray> arrayList = null;
		if(StringUtils.isNotBlank(arrayId)) {
			NewGkArray array;
			if(Objects.equals(useMaster,"1")) {
				array = newGkArrayService.findOneWithMaster(arrayId);
			}else {
				array = newGkArrayService.findOne(arrayId);
			}
			
			if(NewGkElectiveConstant.IF_0.equals(array.getIsDeleted()+"")) {
				arrayList = Arrays.asList(array);
				map.put("arrayId", arrayId);
			}
		}
		if(arrayList == null){
			allList.sort(new Comparator<NewGkArray>() {
				@Override
				public int compare(NewGkArray o1, NewGkArray o2) {
					int b1 = Optional.ofNullable(o2.getIsDefault()).orElse(0) - Optional.ofNullable(o1.getIsDefault()).orElse(0);
					if(b1!=0)
						return b1;
					int b2 = Optional.ofNullable(o2.getTimes()).orElse(0) - Optional.ofNullable(o1.getTimes()).orElse(0);
					if(b2!=0)
						return b2;
					if(o2.getCreationTime() != null)
						return o2.getCreationTime().compareTo(o1.getCreationTime());
					else 
						return -1;
				}
			});
			
			pageInfo.setItemsNum(allList.size());
			pageInfo.refresh();
			pageInfo.makeShowCount();
			//[2,5,10,15]
			List<Integer> pageList = new ArrayList<>();
			pageList.add(2);
			pageList.add(5);
			pageList.add(10);
			pageList.add(15);
			pageInfo.setPageList(pageList);
			arrayList = allList.subList(pageInfo.getStartIndex()-1, pageInfo.getEndIndex());
		}

		Set<String> divideIds=EntityUtils.getSet(arrayList, NewGkArray::getDivideId);
		Map<String, NewGkDivide>divMap= newGkDivideService.findMapByIdIn(divideIds.toArray(new String[0]));
		for(NewGkArray array:arrayList){
			String key = NewGkElectiveConstant.ARRAY_LESSON+"_"+array.getId();
			String key1 = NewGkElectiveConstant.ARRAY_LESSON+"_"+array.getId()+"_mess";	
			if(true || "0".equals(array.getStat())) {
				if(newGkArrayService.checkIsArrayIng(array.getId())){
					array.setNow(true);
					if(!makeCril){
						makeCril=true;
					}
				}else{
					array.setNow(false);
					//去除不必要的redis
					if("error".equals(RedisUtils.get(key))){
						String errorMsg = RedisUtils.get(key1);
						if(errorMsg.length()>100) {
							errorMsg = errorMsg.substring(0, 100)+" ..";
						}
						array.setErrorMess(errorMsg);
					}else if("success".equals(RedisUtils.get(key))){
						RedisUtils.del(key,key1);
					}
				}
			}
			NewGkDivide divnet= divMap.get(array.getDivideId());
			if(divnet!=null)
				array.setOpenType(divnet.getOpenType());
			array.setArrangeType(NewGkElectiveConstant.ARRANGE_SEVEN);
		}
		

		
		map.put("arrayList", arrayList);
		map.put("makeCril", makeCril);
		return "/newgkelective/array/arrayIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/{gradeId}/goArrange/saveName")
	@ControllerInfo(value="修改名称")
	public String saveArrayName(@PathVariable String gradeId, String arrayId, String arrayName) {
		List<NewGkArray> arrayList = newGkArrayService.findByGradeId(getLoginInfo().getUnitId(),gradeId,null,NewGkElectiveConstant.ARRANGE_SEVEN);
		if("create".equals(arrayId)) {
			for(NewGkArray ch : arrayList) {
				if(ch.getArrayName().equals(arrayName)) {
					return error("该名称已被其他记录使用！");
				}
			}
		}else {
			if(CollectionUtils.isNotEmpty(arrayList)) {
				NewGkArray toCh = null;
				for(NewGkArray ch : arrayList) {
					if(ch.getId().equals(arrayId)) {
						toCh = ch;
					} else if(ch.getArrayName().equals(arrayName)) {
						return error("该名称已被其他记录使用！");
					}
				}
				if(toCh == null) {
					return error("排记录不存在或已被删除！");
				}
				toCh.setArrayName(arrayName);
				toCh.setModifyTime(new Date());
				newGkArrayService.save(toCh);
			} else {
				return error("排课记录不存在或已被删除！");
			}
		}
		
		return returnSuccess();
	}

	/**
	 * 新增排课方案
	 */
	@RequestMapping("/{gradeId}/goArrange/addArray/page")
	public String addArrangeIndex(@PathVariable("gradeId") String gradeId,String divideId,
			HttpServletRequest request, ModelMap map) {
		String unitId=getLoginInfo().getUnitId();
		//获取完成的方案
		List<NewGkDivide> divideList = newGkDivideService.findByGradeId(unitId, gradeId,NewGkElectiveConstant.IF_1);		
		//List<NewGkDivideDto> dtos = makeDivideItem(divideList,gradeId);
		List<NewGkDivideDto> dtos = newGkDivideService.makeDivideItem(divideList);
		map.put("divideDtoList", dtos);
		
		NewGkArray newGkArray=new NewGkArray();
		newGkArray.setGradeId(gradeId);
		if(StringUtils.isBlank(divideId)){
			//取得默认值
			if(CollectionUtils.isNotEmpty(divideList)){
				for(NewGkDivide d:divideList){
					if(d.getIsDefault()==1){
						divideId=d.getId();
						break;
					}
				}
				if(StringUtils.isBlank(divideId)){
					divideId=divideList.get(0).getId();
				}
			}
		}
		newGkArray.setDivideId(divideId);
		newGkArray.setLessonArrangeId(StringUtils.trimToEmpty(request.getParameter("lessArrayId")));
		newGkArray.setPlaceArrangeId(StringUtils.trimToEmpty(request.getParameter("plArrayId")));
		map.put("newGkArray", newGkArray);
		
		//取得数据库最大值
		int maxTimes=newGkArrayService.findMaxByGradeId(getLoginInfo().getUnitId(), gradeId,NewGkElectiveConstant.ARRANGE_SEVEN);
		//获取学年学期 年级信息
		String semesterJson = semesterRemoteService.getCurrentSemester(2, getLoginInfo().getUnitId());
		Semester semester = SUtils.dc(semesterJson, Semester.class);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);	
		String arrayName = semester.getAcadyear()+"学年"+grade.getGradeName()+"第"+
					semester.getSemester()+"学期排课方案"+(maxTimes+1);
		
		map.put("gradeId", gradeId);
		map.put("arrayName", arrayName);
		return "/newgkelective/array/arrayAdd2.ftl";
	}
	
	/**
	 * 编辑排课方案
	 */
	@RequestMapping("/{gradeId}/goArrange/editArray/page")
	public String editArrayIndex(@PathVariable("gradeId") String gradeId,String arrayId,
			ModelMap map) {
		
		NewGkArray newGkArray = newGkArrayService.findById(arrayId);
		if(newGkArray==null){
			return errorFtl(map, "排课方案不存在");
		}
		map.put("newGkArray", newGkArray);
		
		String unitId=getLoginInfo().getUnitId();
		//获取完成的方案
		List<NewGkDivide> divideList = newGkDivideService.findByGradeId(unitId, gradeId,NewGkElectiveConstant.IF_1);		
//		List<NewGkDivideDto> dtos = makeDivideItem(divideList,gradeId);
		List<NewGkDivideDto> dtos = newGkDivideService.makeDivideItem(divideList);
		map.put("divideDtoList", dtos);
		map.put("arrayName", newGkArray.getArrayName());
		
		return "/newgkelective/array/arrayAdd2.ftl";
	}
	
//	private List<NewGkDivideDto> makeDivideItem(List<NewGkDivide> divideList,String gradeId){
//		List<NewGkDivideDto> dtos = new ArrayList<NewGkDivideDto>();
//	
//		if (CollectionUtils.isNotEmpty(divideList)) {
//			int sum=0;
//			List<Student> studentList=SUtils.dt(studentRemoteService.findByGradeId(gradeId),new TR<List<Student>>(){});
//			if(CollectionUtils.isNotEmpty(studentList))
//				sum= studentList.size();
//			
//			// 如有值填充好对象页面值dto
//			Map<String, Integer> xknumMap = new HashMap<String, Integer>();
//			// 各次选课人数
//			xknumMap = newGkChoResultService.getCountMapByGradeIdAndKindType(gradeId,NewGkElectiveConstant.KIND_TYPE_01);
//			List<NewGkDivideClass> divideClassList=newGkDivideClassService.findByGradeId(gradeId,NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
//			Map<String,List<NewGkChoRelation>> newGkChoRelationMap=  newGkChoRelationService.findByGradeId(gradeId);
//			List<NewGkChoRelation> notvals = newGkChoRelationMap.get(NewGkElectiveConstant.CHOICE_TYPE_04);
//			//去掉人数
//			Map<String, Integer> notvalMap=new HashMap<String, Integer>();
//			if(CollectionUtils.isNotEmpty(notvals)){
//				for(NewGkChoRelation ent:notvals){
//					Integer num = notvalMap.get(ent.getChoiceId());
//					if(num==null){
//						num=0;
//					}
//					notvalMap.put(ent.getChoiceId(), num+1);
//				}
//			}
//			//key 分班id   value 是分班的班级ids
//			Map<String,Set<String>> divideIdOfClaId=new HashMap<String,Set<String>>();
//			Set<String> divideClassIds=new HashSet<String>();
//			if(CollectionUtils.isNotEmpty(divideClassList)){
//				for(NewGkDivideClass divideClass:divideClassList){
//					divideClassIds.add(divideClass.getId());
//					
//					Set<String> divideClassIdSet=divideIdOfClaId.get(divideClass.getDivideId());
//					if(CollectionUtils.isEmpty(divideClassIdSet)){
//						divideClassIdSet=new HashSet<String>();
//					}
//					divideClassIdSet.add(divideClass.getId());
//					divideIdOfClaId.put(divideClass.getDivideId(), divideClassIdSet);
//				}
//			}
//			Map<String, List<String>> divideIdOfStuMap=newGkClassStudentService.findMapByClassIds(divideClassIds.toArray(new String[0]));
//			
//			for (NewGkDivide ent : divideList) {
//				NewGkDivideDto dto = new NewGkDivideDto();
//				dto.setEnt(ent);
//				Set<String> divideStudentSet=new HashSet<String>();//已安排学生ids
//				int courseNumber=xknumMap.get(ent.getChoiceId())==null?0:xknumMap.get(ent.getChoiceId());
//				dto.setCourseNumber(courseNumber);
//				Set<String> divideClassIdSet=divideIdOfClaId.get(ent.getId());//每个分班方案对应的分班班级
//				if(CollectionUtils.isNotEmpty(divideClassIdSet)){
//					dto.setClassSum(divideClassIdSet.size());
//					for(String divideClassId:divideClassIdSet){//获取每个分班方案对应的已安排的学生数
//						List<String> studentIds=divideIdOfStuMap.get(divideClassId);
//						if(CollectionUtils.isNotEmpty(studentIds)){
//							divideStudentSet.addAll(studentIds);
//						}
//					}
//				}
//				dto.setDivideStudentNum(divideStudentSet.size());
//				Integer num = notvalMap.get(ent.getChoiceId());
//				if(num!=null){
//					dto.setNoDivideStudentNum(sum-divideStudentSet.size()-num);
//				}else {
//					dto.setNoDivideStudentNum(sum-divideStudentSet.size());
//				}
//				dto.setMiniMumClassSize(ent.getGalleryful());
//				dto.setMaxiMumClassSize(ent.getGalleryful()+ent.getMaxGalleryful());
//				dto.setHaveDivideIng(isNowDivide(ent.getId()));
//				dtos.add(dto);
//			}
//		}
//		return dtos;
//	}
	
	
	
	
	@ResponseBody
    @RequestMapping("/goArrange/chosenItemByDivideId")
    @ControllerInfo(value = "查询教师场地等信息")
	public String chosenItemByDivideId(String divideId, String useMaster){
		NewGkDivide newGkDivide = newGkDivideService.findById(divideId);
		List<NewGkArrayItem> itemList=null;
		if(Objects.equals(useMaster, "1"))
			itemList=newGkArrayItemService.findByDivideIdWithMaster(divideId, 
					new String[] {NewGkElectiveConstant.ARRANGE_TYPE_01, NewGkElectiveConstant.ARRANGE_TYPE_04});
		else
			itemList=newGkArrayItemService.findByDivideId(divideId, 
					new String[] {NewGkElectiveConstant.ARRANGE_TYPE_01, NewGkElectiveConstant.ARRANGE_TYPE_04});
			
		JSONObject json=new JSONObject();
		JSONArray jsonArr=new JSONArray();
		if(CollectionUtils.isNotEmpty(itemList)){
			Map<String,List<NewGkArrayItem>> itemByType=new HashMap<String, List<NewGkArrayItem>>();
			for(NewGkArrayItem ll:itemList){
				//只要01，04
				if(NewGkElectiveConstant.ARRANGE_TYPE_01.equals(ll.getDivideType()) || NewGkElectiveConstant.ARRANGE_TYPE_04.equals(ll.getDivideType())){
					if(!itemByType.containsKey(ll.getDivideType())){
						itemByType.put(ll.getDivideType(), new ArrayList<NewGkArrayItem>());
					}
					itemByType.get(ll.getDivideType()).add(ll);
				}
			}
			
			if(itemByType.containsKey(NewGkElectiveConstant.ARRANGE_TYPE_01)){
				//教室
				List<NewGkArrayItem> item1=itemByType.get(NewGkElectiveConstant.ARRANGE_TYPE_01);
				if(CollectionUtils.isNotEmpty(item1)){
					newGkArrayItemService.makeDtoData(NewGkElectiveConstant.ARRANGE_TYPE_01,newGkDivide.getGradeId(), item1);
				}
			}
			if(itemByType.containsKey(NewGkElectiveConstant.ARRANGE_TYPE_02)){
				//教师
				List<NewGkArrayItem> item2=itemByType.get(NewGkElectiveConstant.ARRANGE_TYPE_02);
				if(CollectionUtils.isNotEmpty(item2)){
					newGkArrayItemService.makeDtoData(NewGkElectiveConstant.ARRANGE_TYPE_02, newGkDivide.getGradeId(),item2);
				}
			}
			if(itemByType.containsKey(NewGkElectiveConstant.ARRANGE_TYPE_03)){
				//周课时
				List<NewGkArrayItem> item3=itemByType.get(NewGkElectiveConstant.ARRANGE_TYPE_03);
				if(CollectionUtils.isNotEmpty(item3)){
					newGkArrayItemService.makeDtoData(NewGkElectiveConstant.ARRANGE_TYPE_03, newGkDivide.getGradeId(),item3);
				}
			}
			if(itemByType.containsKey(NewGkElectiveConstant.ARRANGE_TYPE_04)){
				//上课时间
				List<NewGkArrayItem> item4=itemByType.get(NewGkElectiveConstant.ARRANGE_TYPE_04);
				if(CollectionUtils.isNotEmpty(item4)){
					newGkArrayItemService.makeDtoData(NewGkElectiveConstant.ARRANGE_TYPE_04,newGkDivide.getGradeId(), item4);
				}
			}
			
			JSONObject json1=new JSONObject();
			JSONArray jsonArr1=new JSONArray();
			JSONObject json2=new JSONObject();
			JSONArray jsonArr2=new JSONArray();
			JSONObject json3=new JSONObject();
			NewGkItemDto itemDto=null;
			SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date creatTime=null;
			for (Map.Entry<String, List<NewGkArrayItem>> entry:itemByType.entrySet()) {
				json1=new JSONObject();
				json1.put("type",entry.getKey());
				jsonArr1=new JSONArray();
				for(NewGkArrayItem item:entry.getValue()){
					//移除没有数据的
					if(item.getNewGkItemDto()==null){
						continue;
					}
					itemDto = item.getNewGkItemDto();
					if(itemDto.getNum()==null || itemDto.getNum().length<=0){
						continue;
					}
					json2=new JSONObject();
					json2.put("id", item.getId());
					json2.put("name", item.getItemName());
					creatTime = item.getCreationTime();
					json2.put("creatTime", creatTime==null?"":formatter.format(creatTime));
					/**
					 * doto 数据详情
					 */
					String[] numStr = itemDto.getNum();
					String[] typeName = itemDto.getTypeName();
					jsonArr2=new JSONArray();
					for(int i=0;i<numStr.length;i++){
						json3=new JSONObject();
						json3.put("num", numStr[i]);
						json3.put("name", typeName[i]);
						jsonArr2.add(json3);
					}
					json2.put("itemValue", jsonArr2);
					
					if(StringUtils.isNotBlank(item.getGalleryful())) {
						json2.put("norange", item.getGalleryful());
					}else {
						json2.put("norange", "");
					}
					jsonArr1.add(json2);
				}
				json1.put("list", jsonArr1);
				jsonArr.add(json1);
			} 
		}
		json.put("value", jsonArr);
		return json.toJSONString();
	}
	
	@ResponseBody
    @RequestMapping("/goArrayLesson/save")
    @ControllerInfo(value = "保存排课方案")
	public String saveArrayLesson(NewGkArray dto){
		NewGkArray addDto=null;
		NewGkArray old=null;
		if(StringUtils.isNotBlank(dto.getId())){
			old = newGkArrayService.findOne(dto.getId());
		}
		if(old!=null){
			addDto=old;
			addDto.setArrayName(dto.getArrayName());
		}else{
			addDto=new NewGkArray();
			addDto.setId(UuidUtils.generateUuid());
			addDto.setCreationTime(new Date());
			//取得数据库最大值
			int maxTimes=newGkArrayService.findMaxByGradeId(getLoginInfo().getUnitId(), dto.getGradeId(),NewGkElectiveConstant.ARRANGE_SEVEN);
			addDto.setTimes(maxTimes+1);
			
			String arrayName = "";
			if(StringUtils.isBlank(dto.getArrayName())) {
				//获取学年学期 年级信息
				String semesterJson = semesterRemoteService.getCurrentSemester(2, getLoginInfo().getUnitId());
				Semester semester = SUtils.dc(semesterJson, Semester.class);
				Grade grade = SUtils.dc(gradeRemoteService.findOneById(dto.getGradeId()), Grade.class);	
				arrayName = semester.getAcadyear()+"学年"+grade.getGradeName()+"第"+
						semester.getSemester()+"学期排课方案"+addDto.getTimes();
			}else {
				arrayName = dto.getArrayName();
			}
			
			addDto.setArrayName(arrayName);
		}
		if(addDto.getArrayName().getBytes().length > 80){
			return error("名称长度不能超过80. 每个汉字长度为2，其他长度为1");
		}
		addDto.setUnitId(getLoginInfo().getUnitId());
		addDto.setGradeId(dto.getGradeId());
		addDto.setDivideId(dto.getDivideId());
		addDto.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
		addDto.setLessonArrangeId(dto.getLessonArrangeId());
		addDto.setModifyTime(new Date());
		addDto.setPlaceArrangeId(dto.getPlaceArrangeId());
		addDto.setStat(NewGkElectiveConstant.IF_0);
		addDto.setArrangeType(NewGkElectiveConstant.ARRANGE_SEVEN);
		try{
			newGkArrayService.saveArray(addDto);
//			newGkTimetableService.updatePreTimetable(addDto.getId(),null);
		}catch (Exception e) {
	        e.printStackTrace();
	        return returnError("保存失败！", e.getMessage()+"");  // 避免前端提示框显示上一次的结果
	    }
		return success(addDto.getId()==null?"success":addDto.getId());
	}

	private List<NewGkChoiceDto> getChoiceChartByGradeId(String gradeId, PageInfo pageInfo, String isMaster, List<String> classIdList, ModelMap map) {
		
		List<NewGkChoiceDto> newGkChoiceDtoList = new ArrayList<NewGkChoiceDto>();
		// 查询当前年级所有发布且没有删除的选课
		List<NewGkChoice> newGkChoiceList;
		if(Constant.IS_TRUE_Str.equals(isMaster)){
			newGkChoiceList = newGkChoiceService.findListByGradeIdWithMaster(gradeId);
		}else{
			newGkChoiceList = newGkChoiceService.findListByGradeId(gradeId);
		}
		if(newGkChoiceList.size() == 0) {
			return newGkChoiceDtoList;
		}
		
		if(pageInfo != null) {
			map.put("allList", newGkChoiceList);
			pageInfo.setItemsNum(newGkChoiceList.size());
			pageInfo.refresh();
			
			newGkChoiceList = newGkChoiceList.stream()
					.skip(pageInfo.getStartIndex()-1)
					.limit(pageInfo.getPageSize())
					.collect(Collectors.toList());
			
		}
		
		newGkChoiceDtoList=newGkChoiceService.makeChoiceChart(gradeId, newGkChoiceList,classIdList);
		return newGkChoiceDtoList;
	}
	 /**
	  * 是否正在分班中
	  * @return
	  */
	 @SuppressWarnings("unused")
	private boolean isNowDivide(String  divideId){
		 final String key = NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId;
			
		 if(RedisUtils.get(key)!=null && "start".equals(RedisUtils.get(key))){
			 return true;
		 }
		 return false;
	 }
	 
	 
	@RequestMapping("/{gradeId}/divide/combineDivide")
	public String combineDivide(@PathVariable String gradeId, ModelMap map) {
		List<NewGkChoice> choiceList = newGkChoiceService.findListByGradeId(gradeId);
		List<NewGkDivide> allDividelist = newGkDivideService.findByGradeId(getLoginInfo().getUnitId(), gradeId, NewGkElectiveConstant.IF_1);
		//只考虑7选3模式合并
		List<NewGkDivide> dividelist=new ArrayList<>();
		if(CollectionUtils.isNotEmpty(allDividelist)) {
			for(NewGkDivide n:allDividelist) {
				if(NewGkElectiveConstant.DIVIDE_TYPE_03.equals(n.getOpenType())
						|| NewGkElectiveConstant.DIVIDE_TYPE_04.equals(n.getOpenType())
						|| NewGkElectiveConstant.DIVIDE_TYPE_09.equals(n.getOpenType())
						|| NewGkElectiveConstant.DIVIDE_TYPE_10.equals(n.getOpenType())
						|| NewGkElectiveConstant.DIVIDE_TYPE_11.equals(n.getOpenType())
						|| NewGkElectiveConstant.DIVIDE_TYPE_12.equals(n.getOpenType())) {
					continue;
				}
				dividelist.add(n);
			}
		}
		map.put("dividelist", dividelist);
		map.put("choiceList", choiceList);
		map.put("gradeId", gradeId);
		return "/newgkelective/divide/divideCombine.ftl";
	}
	@ResponseBody
	@RequestMapping("/{gradeId}/divide/saveCombine")
	public String saveCombine(@PathVariable String gradeId, String divideId1,String divideId2,String choiceId,ModelMap map) {
		Map<String, NewGkDivide> divideMap = newGkDivideService.findMapByIdIn(new String[] {divideId1,divideId2});
		NewGkDivide divideOne=null;
		NewGkDivide divideTwo=null;
		if(!divideMap.containsKey(divideId1)) {
			return error("分班1方案已经不存在");
		}
		if(!divideMap.containsKey(divideId2)) {
			return error("分班2方案已经不存在");
		}
		divideOne=divideMap.get(divideId1);
		divideTwo=divideMap.get(divideId2);
		if(!NewGkElectiveConstant.IF_1.equals(divideOne.getStat())) {
			return error("分班1方案处于未完成状态");
		}
		if(!NewGkElectiveConstant.IF_1.equals(divideTwo.getStat())) {
			return error("分班2方案处于未完成状态");
		}
		NewGkChoice choice = newGkChoiceService.findOne(choiceId);
		if(choice==null) {
			return error("选课方案方案不存在");
		}
		if(divideOne.getChoiceId().equals(divideTwo.getChoiceId())) {
			return error("不能选择两个同选课的分班方案");
		}
		/** 
		 * 选课方案中数据正好是分班方案1与分班方案2的选课集合  分班方案1与分班方案2 学生不能重复 而且设置上课科目需要一致
		 */
		//两个分班方案的设置上课科目
		List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService.findByDivideIdIn(new String[] {divideId1,divideId2});
		Map<String,Set<String>> openSubjectMap=new HashMap<>();
		if(CollectionUtils.isNotEmpty(openSubjectList)) {
			for(NewGkOpenSubject open:openSubjectList) {
				if(!openSubjectMap.containsKey(open.getDivideId())) {
					openSubjectMap.put(open.getDivideId(), new HashSet<>());
				}
				openSubjectMap.get(open.getDivideId()).add(open.getSubjectId()+"_"+open.getSubjectType());
			}
			int allSubject=0;
			Set<String> ss=new HashSet<>();
			for(Entry<String, Set<String>> item:openSubjectMap.entrySet()) {
				if(CollectionUtils.isEmpty(item.getValue())) {
					return error("不能合并，两个分班方案的开设科目不一致");
				}
				if(allSubject==0) {
					allSubject=item.getValue().size();
					ss.addAll(item.getValue());
				}else {
					ss.addAll(item.getValue());
					if(ss.size()>allSubject) {
						return error("不能合并，两个分班方案的开设科目不一致");
					}
				}
			}
		}

		//A的选课学生数据+B选课学生数据=C选课学生数据
		//A选课结果+B选课结果=C最终选课结果
		//1、A的学生与B的学生不重复
		//2、选择各科目的学生一样A+B=C
		List<NewGkChoResult> choicelist = newGkChoResultService.findByGradeIdAndKindType(choice.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,new String[] {choice.getId(),divideOne.getChoiceId(),divideTwo.getChoiceId()});
		//key:choiceId value studentId
		Map<String,Set<String>> studentIdByChoice=new HashMap<>();
		//key1:subjectId key2:choiceId value studentId
		Map<String,Map<String,Set<String>>> subjectByChoice=new HashMap<>();
		if(CollectionUtils.isEmpty(choicelist)) {
			return error("选课数据为空");
		}
		for(NewGkChoResult rr:choicelist) {
			Set<String> stuSet = studentIdByChoice.get(rr.getChoiceId());
			if(stuSet==null) {
				stuSet=new HashSet<>();
				studentIdByChoice.put(rr.getChoiceId(), stuSet);
			}
			stuSet.add(rr.getStudentId());
			Map<String, Set<String>> stuMap = subjectByChoice.get(rr.getSubjectId());
			if(stuMap==null) {
				stuMap=new HashMap<>();
				subjectByChoice.put(rr.getSubjectId(), stuMap);
			}
			Set<String> set = stuMap.get(rr.getChoiceId());
			if(set==null) {
				set=new HashSet<>();
				stuMap.put(rr.getChoiceId(), set);
			}
			set.add(rr.getStudentId());
		}
		
		//组合下学生
		Set<String> com=new HashSet<>();
		int size=0;
		//选中的选课结果
		Set<String> cho=new HashSet<>();
		for(Entry<String, Set<String>> item:studentIdByChoice.entrySet()) {
			if(choice.getId().equals(item.getKey())) {
				cho.addAll(item.getValue());
			}else {
				size=size+item.getValue().size();
				com.addAll(item.getValue());
				if(com.size()!=size) {
					return error("合并分班方案下的选课结果学生有重复");
				}
			}
		}
		if(cho.size()!=com.size()) {
			return error("选课方案是由分班1和分班2对应的选课合并得到的");
		}
		com.addAll(cho);
		if(com.size()!=size) {
			return error("选课方案是由分班1和分班2对应的选课合并得到的");
		}
		//循环所有学生 学生数据是否一致
		for(Entry<String, Map<String, Set<String>>> item:subjectByChoice.entrySet()) {
			Map<String, Set<String>> choicesubjectStuId = item.getValue();
			Set<String> cho1 = new HashSet<>();
			Set<String> com1=new HashSet<>();
			int size1=0;
			for(Entry<String, Set<String>> item1:choicesubjectStuId.entrySet()) {
				if(choice.getId().equals(item1.getKey())) {
					cho1.addAll(item1.getValue());
				}else {
					size1=size1+item1.getValue().size();
					com1.addAll(item1.getValue());
					if(com1.size()!=size1) {
						return error("合并分班方案下的选课结果学生有重复");
					}
				}
			}
			if(cho1.size()!=com1.size()) {
				return error("选课方案是由分班1和分班2对应的选课合并得到的");
			}
			com1.addAll(cho1);
			if(com1.size()!=size1) {
				return error("选课方案是由分班1和分班2对应的选课合并得到的");
			}
		}
		
		//复制divideOne中包括的数据
		try {
			newGkDivideService.saveCombineDivide(divideOne, divideTwo, choice);
		}catch (Exception e) {
			e.printStackTrace();
			return error("合并失败");
		}
		return success("");
	}
	
	
	@ResponseBody
	@RequestMapping("/{gradeId}/divide/saveCombineList")
	public String saveCombineList(@PathVariable String gradeId, @RequestParam("divideIds[]") String[] divideIds,String choiceId,ModelMap map) {
		Map<String, NewGkDivide> divideMap = newGkDivideService.findMapByIdIn(divideIds);
		NewGkDivide divideOne=null;
		List<NewGkDivide> list=new ArrayList<NewGkDivide>();
		for(String s:divideIds) {
			if(!divideMap.containsKey(s)) {
				return error("部分方案已经不存在");
			}
			divideOne=divideMap.get(s);
			if(!NewGkElectiveConstant.IF_1.equals(divideOne.getStat())) {
				return error("部分方案处于未完成状态");
			}
			list.add(divideOne);
		}
		
		NewGkChoice choice = newGkChoiceService.findOne(choiceId);
		if(choice==null) {
			return error("选课方案方案不存在");
		}
		/** 
		 * DOTO  选课方案中数据正好是分班方案1与分班方案2的选课集合  分班方案1与分班方案2 学生不能重复 而且设置上课科目需要一致
		 */
	
		//复制divideOne中包括的数据
		try {
			newGkDivideService.saveCombineDivideList(list ,choice);
		}catch (Exception e) {
			e.printStackTrace();
			return error("合并失败");
		}
		return success("");
	}
	
}
	

