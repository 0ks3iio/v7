package net.zdsoft.gkelective.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dto.ChosenSubjectSearchDto;
import net.zdsoft.gkelective.data.dto.GkRuleSaveDto;
import net.zdsoft.gkelective.data.dto.GkStuScoreDto;
import net.zdsoft.gkelective.data.entity.GkAllocation;
import net.zdsoft.gkelective.data.entity.GkRelationship;
import net.zdsoft.gkelective.data.entity.GkStuRemark;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.service.GkAllocationService;
import net.zdsoft.gkelective.data.service.GkRelationshipService;
import net.zdsoft.gkelective.data.service.GkStuRemarkService;
import net.zdsoft.gkelective.data.service.GkSubjectArrangeService;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.remote.service.ExamInfoRemoteService;
import net.zdsoft.scoremanage.remote.service.SubjectInfoRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

@Controller
@RequestMapping("/gkelective/{arrangeId}")
public class GkElectiveBasisSetAction extends BaseAction{
	
	@Autowired
    private GkSubjectArrangeService gkSubjectArrangeService;
	@Autowired
    private ClassRemoteService classRemoteService;
	@Autowired
	private GkStuRemarkService gkStuRemarkService;
	@Autowired
	private GkRelationshipService gkRelationshipService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private SemesterRemoteService semesterService;
	@Autowired
	private ExamInfoRemoteService examInfoRemoteService;
	@Autowired
	private SubjectInfoRemoteService subjectInfoRemoteService;
	@Autowired
	private GkAllocationService gkAllocationService;
	
	@RequestMapping("/basisSet/index/page")
    @ControllerInfo(value = "基础设置index")
    public String showIndex(@PathVariable final String arrangeId,ModelMap map, HttpSession httpSession) {
		map.put("arrangeId", arrangeId);
		GkSubjectArrange gkArrange = gkSubjectArrangeService.findArrangeById(arrangeId);
        map.put("gkSubArr", gkArrange);
		return "/gkelective/basisSet/basisSetIndex.ftl";
	}
	
	@RequestMapping("/basisSet/score/index/page")
	@ControllerInfo(value = "基础设置-成绩设置index")
	public String showScoreIndex(@PathVariable final String arrangeId,ModelMap map, HttpSession httpSession) {
//		final GkSubjectArrange gkArrange = RedisUtils.getObject(GkElectveConstants.GK_ARRANGE_KEY+arrangeId, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<GkSubjectArrange>(){}, new RedisInterface<GkSubjectArrange>(){
//			@Override
//			public GkSubjectArrange queryData() {
//				return gkSubjectArrangeService.findArrangeById(arrangeId);
//			}
//		});
		final GkSubjectArrange gkArrange =gkSubjectArrangeService.findArrangeById(arrangeId);
        List<Clazz> clazzList = RedisUtils.getObject(GkElectveConstants.GRADE_CLASS_LIST_KEY+gkArrange.getGradeId(), RedisUtils.TIME_FIVE_MINUTES, new TypeReference<List<Clazz>>(){}, new RedisInterface<List<Clazz>>(){
			@Override
			public List<Clazz> queryData() {
				return SUtils.dt(classRemoteService.findBySchoolIdGradeId(gkArrange.getUnitId(),gkArrange.getGradeId()),new TR<List<Clazz>>() {});
			}
        });
        map.put("gkArrange", gkArrange);
        map.put("clazzList", clazzList);
		return "/gkelective/basisSet/basisSetScoreIndex.ftl";
	}
	
	@RequestMapping("/basisSet/score/list/page")
	@ControllerInfo(value = "基础设置-成绩设置list")
	public String showScoreList(@PathVariable final String arrangeId,ChosenSubjectSearchDto searchDto,ModelMap map,HttpServletRequest request, HttpSession httpSession) {
		Pagination page = createPagination();
		map.put("arrangeId", arrangeId);
		map.put("classId", searchDto.getSearchClassId());
		List<Course> coursesList = new ArrayList<Course>();
//		if(GkStuRemark.TYPE_SCORE.equals(searchDto.getSearchScoreType())){
			coursesList = RedisUtils.getObject(GkElectveConstants.GK_OPENSUBJECT_KEY+arrangeId, RedisUtils.TIME_ONE_MINUTE, new TypeReference<List<Course>>(){}, new RedisInterface<List<Course>>(){
				@Override
				public List<Course> queryData() {
					List<GkRelationship> findByTypePrimaryIdIn = gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_03,arrangeId);
					Set<String> subjectIds = EntityUtils.getSet(findByTypePrimaryIdIn, "relationshipTargetId");
					return SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
				}
			});
//		}else if(GkStuRemark.TYPE_SCORE_YSY.equals(searchDto.getSearchScoreType())){
			Course course = new Course();
			course.setId(GkStuRemark.YSY_SUBID);
			course.setSubjectName(GkStuRemark.YSY_SUBNAME);
			coursesList.add(course);
//		}
		map.put("coursesList", coursesList);
		List<GkStuScoreDto> gkStuScoreDtoList = gkStuRemarkService.findStuScoreDtoList(arrangeId,searchDto, page);
		map.put("gkStuScoreDtoList", gkStuScoreDtoList);
		sendPagination(request, map, page);
		return "/gkelective/basisSet/basisSetScoreList.ftl";
	}
	@RequestMapping("/basisSet/score/edit/page")
	@ControllerInfo(value = "基础设置-成绩设置edit")
	public String showScoreEdit(@PathVariable final String arrangeId,ModelMap map,HttpServletRequest request, HttpSession httpSession) {
		map.put("arrangeId", arrangeId);
		List<String> acadyearList = SUtils.dt(semesterService.findAcadeyearList(), new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        Semester semester = SUtils.dc(semesterService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
		return "/gkelective/basisSet/basisSetScoreEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/basisSet/score/findExamList")
	@ControllerInfo("基础设置-成绩设置获取考试数据")
	public String showFindExamInfoList(@PathVariable final String arrangeId,String acadyear,String semester, ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession sesion) {
		GkSubjectArrange gkArrange = RedisUtils.getObject(GkElectveConstants.GK_ARRANGE_KEY+arrangeId, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<GkSubjectArrange>(){}, new RedisInterface<GkSubjectArrange>(){
			@Override
			public GkSubjectArrange queryData() {
				return gkSubjectArrangeService.findArrangeById(arrangeId);
			}
		});
		List<ExamInfo> examInfoList = SUtils.dt(examInfoRemoteService.findExamInfoListAll(gkArrange.getUnitId(), acadyear, semester, gkArrange.getGradeId()), new TR<List<ExamInfo>>(){});
		return Json.toJSONString(examInfoList,SerializerFeature.DisableCircularReferenceDetect);
	}
	
	@ResponseBody
	@RequestMapping("/basisSet/score/findSubjectList")
	@ControllerInfo("基础设置-成绩设置获取考试下科目")
	public String showFindExamInfoSubjectList(@PathVariable final String arrangeId,String examId, ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession sesion) {
		GkSubjectArrange gkArrange = RedisUtils.getObject(GkElectveConstants.GK_ARRANGE_KEY+arrangeId, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<GkSubjectArrange>(){}, new RedisInterface<GkSubjectArrange>(){
			@Override
			public GkSubjectArrange queryData() {
				return gkSubjectArrangeService.findArrangeById(arrangeId);
			}
		});
		List<SubjectInfo> infoList = SUtils.dt(subjectInfoRemoteService.findByExamIdGradeId(examId, gkArrange.getGradeId()), new TR<List<SubjectInfo>>(){});
		Set<String> subjectIds = new HashSet<String>();
		for (SubjectInfo item : infoList) {
			subjectIds.add(item.getSubjectId());
		}
		List<Course> courses = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIds.toArray(new String[0])), new TR<List<Course>>(){});
		List<Course> coursesList = RedisUtils.getObject(GkElectveConstants.GK_OPENSUBJECT_KEY+arrangeId, RedisUtils.TIME_ONE_MINUTE, new TypeReference<List<Course>>(){}, new RedisInterface<List<Course>>(){
			@Override
			public List<Course> queryData() {
				List<GkRelationship> findByTypePrimaryIdIn = gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_03,arrangeId);
				Set<String> subjectIds = EntityUtils.getSet(findByTypePrimaryIdIn, "relationshipTargetId");
				return SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
			}
		});
		Set<String> openSubjectIds = EntityUtils.getSet(coursesList, "id");
		List<Course> resultList = new ArrayList<Course>();
		Set<String> subCodes = EntityUtils.getSet(courses, "subjectCode");
		CollectionUtils.addAll(subCodes, BaseConstants.SUBJECT_TYPES_YSY);
		//subCodes.retainAll(GkElectveConstants.SUBJECT_YSY_CODES);
		//判断是否包括了语数英三门科
		if(BaseConstants.SUBJECT_TYPES_YSY.length == subCodes.size()){
			Course course = new Course();
			course.setId(GkStuRemark.YSY_SUBID);
			course.setSubjectName(GkStuRemark.YSY_SUBNAME);
			resultList.add(course);
		}
		for (Course course : courses) {
			if(openSubjectIds.contains(course.getId())){
				resultList.add(course);
			}
		}
		return Json.toJSONString(resultList,SerializerFeature.DisableCircularReferenceDetect);
	}
	
	@ResponseBody
    @RequestMapping("/basisSet/score/save")
	@ControllerInfo("基础设置-成绩设置获取考试下科目save")
    public String doSaveExam(@PathVariable String arrangeId,String searchExamId,String[] subjectIds) {
		try{
			if(StringUtils.isEmpty(searchExamId)){
				return error("考试不能为空");
			}
			if(ArrayUtils.isEmpty(subjectIds)){
				return error("未选择科目");
			}
			gkStuRemarkService.saveStuScore(this.getLoginInfo().getUnitId(),arrangeId,searchExamId,subjectIds);
		}catch(ControllerException e){
			return error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
    }
	
	@RequestMapping("/basisSet/rule/index/page")
	@ControllerInfo(value = "基础设置-规则设置index")
	public String showRuleIndex(@PathVariable final String arrangeId,ModelMap map, HttpSession httpSession) {
		List<GkAllocation> findAllocationList = gkAllocationService.findAllocationList(arrangeId,true,true);
		for (GkAllocation gkAllocation : findAllocationList) {
			gkAllocation.setSubjectArrangeId(arrangeId);
		}
//		final GkSubjectArrange gkArrange = RedisUtils.getObject(GkElectveConstants.GK_ARRANGE_KEY+arrangeId, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<GkSubjectArrange>(){}, new RedisInterface<GkSubjectArrange>(){
//			@Override
//			public GkSubjectArrange queryData() {
//				return gkSubjectArrangeService.findArrangeById(arrangeId);
//			}
//		});
		GkSubjectArrange gkArrange=gkSubjectArrangeService.findArrangeById(arrangeId);
        map.put("gkArrange", gkArrange);
		map.put("allocationList", findAllocationList);
		return "/gkelective/basisSet/basisSetRuleIndex.ftl";
	}
	
	@ResponseBody
    @RequestMapping("/basisSet/rule/save")
	@ControllerInfo("基础设置-规则设置save")
    public String doSaveRule(@PathVariable String arrangeId,GkRuleSaveDto gkRuleSaveDto) {
		try{
			if(CollectionUtils.isEmpty(gkRuleSaveDto.getAllocationList())){
				return error("没有需要保存的数据");
			}
			List<GkAllocation> findAllocationList = gkAllocationService.findAllocationList(Constant.GUID_ZERO,true,false);
			Map<String,GkAllocation> linMap = new HashMap<String,GkAllocation>();
			for (GkAllocation item : findAllocationList) {
				linMap.put(item.getType(), item);
			}
			for (GkAllocation item : gkRuleSaveDto.getAllocationList()) {
				item.setSubjectArrangeId(arrangeId);
				item.setName(linMap.get(item.getType()).getName());
			}
			gkAllocationService.saveBatch(gkRuleSaveDto.getAllocationList());
		}catch(ControllerException e){
			return error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
    }
	
}
