package net.zdsoft.gkelective.data.action;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.SortUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.gkelective.data.action.optaplanner.solver.ArrangeSingleSolver;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dto.ChosenSubjectSearchDto;
import net.zdsoft.gkelective.data.dto.ClassChangeDetailDto;
import net.zdsoft.gkelective.data.dto.ClassChangeDto;
import net.zdsoft.gkelective.data.dto.GkArrangeGroupResultDto;
import net.zdsoft.gkelective.data.dto.GkConditionDto;
import net.zdsoft.gkelective.data.dto.GkGroupDto;
import net.zdsoft.gkelective.data.dto.GkStuExceptionDto;
import net.zdsoft.gkelective.data.dto.GkStuScoreDto;
import net.zdsoft.gkelective.data.dto.GoClassSearchDto;
import net.zdsoft.gkelective.data.dto.StuSubChangeDto;
import net.zdsoft.gkelective.data.dto.StudentSubjectDto;
import net.zdsoft.gkelective.data.entity.GkBatch;
import net.zdsoft.gkelective.data.entity.GkGroupClass;
import net.zdsoft.gkelective.data.entity.GkGroupClassStu;
import net.zdsoft.gkelective.data.entity.GkRelationship;
import net.zdsoft.gkelective.data.entity.GkResult;
import net.zdsoft.gkelective.data.entity.GkRounds;
import net.zdsoft.gkelective.data.entity.GkStuRemark;
import net.zdsoft.gkelective.data.entity.GkSubject;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.entity.GkTeachClassEx;
import net.zdsoft.gkelective.data.entity.GkTeachClassStore;
import net.zdsoft.gkelective.data.entity.GkTeachClassStuStore;
import net.zdsoft.gkelective.data.entity.GkTeachPlacePlan;
import net.zdsoft.gkelective.data.service.GkBatchService;
import net.zdsoft.gkelective.data.service.GkClassChangeService;
import net.zdsoft.gkelective.data.service.GkConditionService;
import net.zdsoft.gkelective.data.service.GkGroupClassService;
import net.zdsoft.gkelective.data.service.GkGroupClassStuService;
import net.zdsoft.gkelective.data.service.GkRelationshipService;
import net.zdsoft.gkelective.data.service.GkResultService;
import net.zdsoft.gkelective.data.service.GkRoundsService;
import net.zdsoft.gkelective.data.service.GkStuRemarkService;
import net.zdsoft.gkelective.data.service.GkSubjectArrangeService;
import net.zdsoft.gkelective.data.service.GkSubjectService;
import net.zdsoft.gkelective.data.service.GkTeachClassExService;
import net.zdsoft.gkelective.data.service.GkTeachClassStoreService;
import net.zdsoft.gkelective.data.service.GkTeachPlacePlanService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

/**
 * 开班安排
 * @author weixh
 * @since 2017-2-13 下午2:47:35
 */
@Controller
@RequestMapping("/gkelective/{roundsId}")
public class GkElectiveOpenClassArrangeAction extends BaseAction {
	
	@InitBinder
	public void initBinder(WebDataBinder wb) throws Exception {
		wb.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
	}
	
	private static final Logger logger = LoggerFactory
			.getLogger(GkElectiveOpenClassArrangeAction.class);
	@Autowired
	private GkSubjectArrangeService gkSubjectArrangeService;
	@Autowired
	private GkRoundsService gkRoundsService;
	@Autowired
	private GkSubjectService gkSubjectService;
	@Autowired
	private GkConditionService gkConditionService;
	@Autowired
	private GkGroupClassService gkGroupClassService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private GkResultService gkResultService;
	@Autowired
	private GkBatchService gkBatchService;
//	@Autowired
//	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private GkGroupClassStuService gkGroupClassStuService;
//	@Autowired
//	private GkTimetableLimitArrangService gkTimetableLimitArrangService;
//	@Autowired
//	private TeachClassStuRemoteService teachClassStuRemoteService;
//	@Autowired
//	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
//	@Autowired
//	private CourseScheduleRemoteService courseScheduleRemoteService;
//	@Autowired
//	private GradeRemoteService gradeRemoteService;
	@Autowired
	private GkTeachClassExService gkTeachClassExService;
	@Autowired
	private GkClassChangeService gkClassChangeService;
	@Autowired
	private GkTeachClassStoreService gkTeachClassStoreService;
	@Autowired
	private GkRelationshipService gkRelationshipService;
	@Autowired
	private GkStuRemarkService gkStuRemarkService;
	@Autowired
	private GkTeachPlacePlanService gkTeachPlacePlanService;
	
	@RequestMapping("/openClassArrange/index/page")
	@ControllerInfo(value = "开班安排index")
	public String showIndex(@PathVariable String roundsId,String arrangeId,ModelMap map){
		//去开班前
		GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		if(rounds==null){
			addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangeRounds/index/page","#showList");
	        return errorFtl(map, "未找到对应轮次！");
		}
		GkSubjectArrange ar = gkSubjectArrangeService.findArrangeById(rounds.getSubjectArrangeId());
		if(ar == null){
			addErrorFtlOperation(map, "返回", "/gkelective/arrange/list/page","#showList");
	        return errorFtl(map, "未找到对应7选3系统！");
		}
		map.put("gkSubArr", ar);
		arrangeId = ar.getId();
		Date now = new Date();
		if(ar.getLimitedTime() != null
				&& !ar.getLimitedTime().before(now)){
			addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangeRounds/index/page","#showList");
	        return errorFtl(map, "学生选课还没结束，还不能进行开班！");
		}
		int num=gkSubjectService.findSubNumByRoundsIdTeachModel(roundsId,GkElectveConstants.USE_TRUE);
		if(num < 1){
			addErrorFtlOperation(map, "返回", "/gkelective/"+arrangeId+"/arrangeRounds/index/page","#showList");
	        return errorFtl(map, "没有设置开班课程！");
		}
		map.put("roundsId", roundsId);
		map.put("arrangeId", arrangeId);
		map.put("rounds", rounds);
		return "/gkelectiveys/openClassArrange/openClassArrangeIndex.ftl";
	}
	/**
	 * 将set排序，并转换成以，隔开的字符串
	 * @param set
	 * @return
	 */
	private String keySort(Set<String> set){
		List<String> l=new ArrayList<String>(set);
		Collections.sort(l);
		String s2="";
		for(String s1:l){
			s2=s2+","+s1;
		}
		s2=s2.substring(1);
		return s2;
	}
	//组合 两两组合
	private List<String> keySort2(Set<String> set){
		if(set.size()<=2){
			//无需两两组合
			return null;
		}
		List<String> returnList=new ArrayList<String>();
		List<String> l=new ArrayList<String>(set);
		Collections.sort(l);
		for(int i=0;i<l.size()-1;i++){
			for(int j=i+1;j<l.size();j++){
				returnList.add(l.get(i)+","+l.get(j));
			}
		}
		return returnList;
	}
	
	
	private String nameSet(Map<String,Course> courseMap,String ids){
		String[] s=ids.split(",");
		String returnS="";
		for(String s1:s){
			returnS=returnS+StringUtils.trimToEmpty(courseMap.get(s1)==null?"":courseMap.get(s1).getShortName());// 简称
		}
		return returnS;
	}
	
	@RequestMapping("/openClassArrange/perArrange/list/page")
	@ControllerInfo(value = "开班安排-手动排班")
	public String showperArrangeList(@PathVariable String roundsId,ModelMap map){
		//去开班前
		GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		if(rounds==null){
			addErrorFtlOperation(map, "返回", "/gkelective/arrange/list/page","#showList");
	        return errorFtl(map, "未找到对应7选3系统或应轮次！");
		}
		map.put("rounds", rounds);
		GkSubjectArrange gkSubjectArrange = gkSubjectArrangeService.findArrangeById(rounds.getSubjectArrangeId());
		if(gkSubjectArrange == null){
			addErrorFtlOperation(map, "返回", "/gkelective/arrange/list/page","#showList");
	        return errorFtl(map, "未找到对应7选3系统或应轮次！");
		}
		
		List<GkSubject> gkSubjectList = gkSubjectService.findByRoundsId(roundsId,null);
		if(CollectionUtils.isEmpty(gkSubjectList)){
			addErrorFtlOperation(map, "返回", "/gkelective/"+rounds.getId()+"/arrangeRounds/index/page","#showList");
	        return errorFtl(map, "没有设置开班课程！");
		}
		//先默认全部
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()), new TR<List<Course>>() {});
		Map<String,Course> courseMap=EntityUtils.getMap(courseList,"id");
		List<StudentSubjectDto> stuSubjectList = gkResultService.findAllStudentSubjectDto(rounds.getSubjectArrangeId(),null);
		if(CollectionUtils.isEmpty(stuSubjectList)){
			addErrorFtlOperation(map, "返回", "/gkelective/"+rounds.getSubjectArrangeId()+"/arrangeRounds/index/page","#showList");
	        return errorFtl(map, "还没有学生选课信息！");
		}
		boolean flag = findGroupClass(roundsId, map, gkSubjectArrange, courseMap, stuSubjectList);
		map.put("roundIds",roundsId);

//		if(rounds.getStep()==GkElectveConstants.STEP_1 && checkNewRound(roundsId, rounds.getSubjectArrangeId())){
		if(rounds.getStep() < GkElectveConstants.STEP_4){
			map.put("isCanEdit", true);
		}else{
			map.put("isCanEdit", false);
		}
//		map.put("hasNew", checkNewRound(roundsId, rounds.getSubjectArrangeId()));
		map.put("isArrange", isNowArrange(roundsId));
		map.put("haserror", flag);
		List<GkTeachPlacePlan> findByRoundId = gkTeachPlacePlanService.findByRoundId(roundsId);
		if(CollectionUtils.isNotEmpty(findByRoundId)){
			map.put("isCanRestart", false);
		}else{
			map.put("isCanRestart", true);
		}
		return "/gkelectiveys/openClassArrange/schedulingMainList.ftl";
	}
	
	@RequestMapping("/openClassArrange/perArrange/headSelectList/page")
	@ControllerInfo(value = "开班安排-手动排班修改头部")
	public String showHeadSelectList(@PathVariable String roundsId,ModelMap map){
		//去开班前
		GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		if(rounds==null){
			addErrorFtlOperation(map, "返回", "/gkelective/arrange/list/page","#showList");
			return errorFtl(map, "未找到对应7选3系统或应轮次！");
		}
		GkSubjectArrange gkSubjectArrange = gkSubjectArrangeService.findArrangeById(rounds.getSubjectArrangeId());
		if(gkSubjectArrange == null){
			addErrorFtlOperation(map, "返回", "/gkelective/arrange/list/page","#showList");
			return errorFtl(map, "未找到对应7选3系统或应轮次！");
		}
		
		List<GkSubject> gkSubjectList = gkSubjectService.findByRoundsId(roundsId,null);
		if(CollectionUtils.isEmpty(gkSubjectList)){
			addErrorFtlOperation(map, "返回", "/gkelective/"+rounds.getId()+"/arrangeRounds/index/page","#showList");
			return errorFtl(map, "没有设置开班课程！");
		}
		//先默认全部
		List<Course> courseList = SUtils.dt(courseRemoteService.findByBaseCourseCodes(BaseConstants.SUBJECT_73), new TR<List<Course>>() {});
		Map<String,Course> courseMap=EntityUtils.getMap(courseList,"id");
		List<StudentSubjectDto> stuSubjectList = gkResultService.findAllStudentSubjectDto(rounds.getSubjectArrangeId(),null);
		if(CollectionUtils.isEmpty(stuSubjectList)){
			addErrorFtlOperation(map, "返回", "/gkelective/"+rounds.getSubjectArrangeId()+"/arrangeRounds/index/page","#showList");
			return errorFtl(map, "还没有学生选课信息！");
		}
		boolean flag = findGroupClass(roundsId, map, gkSubjectArrange, courseMap, stuSubjectList);
		map.put("roundIds",roundsId);
		
		map.put("haserror", flag);
		return "/gkelectiveys/openClassArrange/schedulingHeadSelectList.ftl";
	}

	@SuppressWarnings("unchecked")
	public boolean findGroupClass(String roundsId, ModelMap map, GkSubjectArrange gkSubjectArrange, Map<String, Course> courseMap,
			List<StudentSubjectDto> stuSubjectList) {
		boolean flag=false;//是否有异常
		//已有班级
		List<GkGroupDto> oldgDtoList=gkGroupClassService.findGkGroupDtoByRoundsId(roundsId,null);
		
		//3门组合
		List<GkGroupDto> gDtoList=new ArrayList<GkGroupDto>();
		Map<String, GkGroupDto> gDtomap=new HashMap<String, GkGroupDto>();
		Map<String,Set<String>> subjectIdsMap=new HashMap<String, Set<String>>();//同组合下学生
		
		//2门组合
		List<GkGroupDto> gDtoList2=new ArrayList<GkGroupDto>();
		Map<String, GkGroupDto> gDtomap2=new HashMap<String, GkGroupDto>();
		Map<String,Set<String>> subjectIdsMap2=new HashMap<String, Set<String>>();//同组合下学生
		
		//混合组合
		List<GkGroupDto> gDtoList3=new ArrayList<GkGroupDto>();
		Map<String, GkGroupDto> gDtomap3=new HashMap<String, GkGroupDto>();
		Map<String,Set<String>> subjectIdsMap3=new HashMap<String, Set<String>>();//同组合下学生
		
		GkGroupDto g=new GkGroupDto();
		String guidZero = GkElectveConstants.GUID_ZERO;
		for(StudentSubjectDto d:stuSubjectList){
			//选择满3门才算组合
			if(d.getChooseSubjectIds().size()==gkSubjectArrange.getSubjectNum()){
				String ids = keySort(d.getChooseSubjectIds());
				//3门组合
				if(!gDtomap.containsKey(ids)){
					g=new GkGroupDto();
					g.setSubjectIds(ids);
					g.setConditionName(nameSet(courseMap,ids));
					g.setGkGroupClassList(new ArrayList<GkGroupClass>());
//					g.setFemaleNumber(0);
//					g.setMaleNumber(0);
					g.setAllNumber(1);
//					if(GkElectveConstants.FEMALE==d.getSex()){
//						g.setFemaleNumber(1);
//        			}else{
//        				g.setMaleNumber(1);
//        			}
					gDtoList.add(g);
					gDtomap.put(ids, g);
					subjectIdsMap.put(ids, new HashSet<String>());
					subjectIdsMap.get(ids).add(d.getStuId());
				}else{
					subjectIdsMap.get(ids).add(d.getStuId());
					g=gDtomap.get(ids);
					g.setAllNumber(g.getAllNumber()+1);
//					if(GkElectveConstants.FEMALE==d.getSex()){
//						g.setFemaleNumber(g.getFemaleNumber()+1);
//        			}else{
//        				g.setMaleNumber(g.getMaleNumber()+1);
//        			}
				}
				
				//两门组合--3中组合
				List<String> twoGroup=keySort2(d.getChooseSubjectIds());
				if(CollectionUtils.isNotEmpty(twoGroup)){
					for(String key:twoGroup){
						if(!gDtomap2.containsKey(key)){
							g=new GkGroupDto();
							g.setSubjectIds(key);
							g.setConditionName(nameSet(courseMap,key));
							g.setGkGroupClassList(new ArrayList<GkGroupClass>());
							g.setAllNumber(1);
							gDtoList2.add(g);
							gDtomap2.put(key, g);
							subjectIdsMap2.put(key, new HashSet<String>());
							subjectIdsMap2.get(key).add(d.getStuId());
						}else{
							subjectIdsMap2.get(key).add(d.getStuId());
							g=gDtomap2.get(key);
							g.setAllNumber(g.getAllNumber()+1);
						}
					}
				}
				
				if(!gDtomap3.containsKey(guidZero)){
					g=new GkGroupDto();
					g.setSubjectIds(guidZero);
					g.setConditionName("混合");
					g.setGkGroupClassList(new ArrayList<GkGroupClass>());
					g.setAllNumber(1);
					gDtoList3.add(g);
					gDtomap3.put(guidZero, g);
					subjectIdsMap3.put(guidZero, new HashSet<String>());
					subjectIdsMap3.get(guidZero).add(d.getStuId());
				}else{
					subjectIdsMap3.get(guidZero).add(d.getStuId());
					g=gDtomap3.get(guidZero);
					g.setAllNumber(g.getAllNumber()+1);
				}
			}
		}
		Set<String> arrangeStuId=new HashSet<String>();//已经排的学生
		if(CollectionUtils.isNotEmpty(oldgDtoList)){
			List<GkGroupClass> gc=null;
			Set<String> stusIds=null;
			for(GkGroupDto dd:oldgDtoList){
				if(gDtomap.containsKey(dd.getSubjectIds())){
					gDtomap.get(dd.getSubjectIds()).getGkGroupClassList().addAll(dd.getGkGroupClassList());
					gc = dd.getGkGroupClassList();
					stusIds = subjectIdsMap.get(dd.getSubjectIds());
					if(CollectionUtils.isNotEmpty(gc)){
						for(GkGroupClass gg:gc){
							if(CollectionUtils.isNotEmpty(gg.getStuIdList())){
								arrangeStuId.addAll(gg.getStuIdList());
								List<String> stuIdnow=(List<String>)CollectionUtils.intersection(gg.getStuIdList(),stusIds);
								if(stuIdnow.size()!=gg.getStuIdList().size()){
									gg.setNotexists(GkElectveConstants.USE_TRUE);
									if(!flag){
										flag=true;
									}
									break;
								}
								
							}
						}
					}
					
				}else if(gDtomap2.containsKey(dd.getSubjectIds())){
					gDtomap2.get(dd.getSubjectIds()).getGkGroupClassList().addAll(dd.getGkGroupClassList());
					gc = dd.getGkGroupClassList();
					stusIds = subjectIdsMap2.get(dd.getSubjectIds());
					if(CollectionUtils.isNotEmpty(gc)){
						for(GkGroupClass gg:gc){
							if(CollectionUtils.isNotEmpty(gg.getStuIdList())){
								arrangeStuId.addAll(gg.getStuIdList());
								List<String> stuIdnow=(List<String>)CollectionUtils.intersection(gg.getStuIdList(),stusIds);
								if(stuIdnow.size()!=gg.getStuIdList().size()){
									gg.setNotexists(GkElectveConstants.USE_TRUE);
									if(!flag){
										flag=true;
									}
									break;
								}
							}
						}
					}
					
				}else if(gDtomap3.containsKey(dd.getSubjectIds())){
					gDtomap3.get(dd.getSubjectIds()).getGkGroupClassList().addAll(dd.getGkGroupClassList());
					gc = dd.getGkGroupClassList();
					stusIds = subjectIdsMap3.get(dd.getSubjectIds());
					if(CollectionUtils.isNotEmpty(gc)){
						for(GkGroupClass gg:gc){
							if(CollectionUtils.isNotEmpty(gg.getStuIdList())){
								arrangeStuId.addAll(gg.getStuIdList());
							}
						}
					}
					
				}else{
					//这种组合已经不存在啦
					dd.setConditionName(nameSet(courseMap,dd.getSubjectIds()));
					if(!flag){
						flag=true;
					}
					dd.setNotexists(GkElectveConstants.USE_TRUE);
					String[] zz = dd.getSubjectIds().split(",");
					if(zz==null || zz.length<=1){
						continue;//只包括一个科目 那就是混合 32个0  这个问题不存在
					}
					if(zz.length==2){
						//两门的
						gDtoList2.add(dd);
						gDtomap2.put(dd.getSubjectIds(), dd);
					}else{
						//3门的
						gDtoList.add(dd);
						gDtomap.put(dd.getSubjectIds(), dd);
					}
					gc = dd.getGkGroupClassList();
					if(CollectionUtils.isNotEmpty(gc)){
						for(GkGroupClass gg:gc){
							if(CollectionUtils.isNotEmpty(gg.getStuIdList())){
								arrangeStuId.addAll(gg.getStuIdList());
							}
						}
					}
				}
				
			}
		}
		if(CollectionUtils.isNotEmpty(gDtoList)){
			for(GkGroupDto dd:gDtoList){
				Set<String> stuIds = subjectIdsMap.get(dd.getSubjectIds());
				if(stuIds!=null){
					//取得除去 arrangeStuId中剩下的学生
					stuIds.removeAll(arrangeStuId);
					dd.setLeftNumber(stuIds.size());
				}
				
			}
		}
		if(CollectionUtils.isNotEmpty(gDtoList2)){
			for(GkGroupDto dd:gDtoList2){
				Set<String> stuIds = subjectIdsMap2.get(dd.getSubjectIds());
				if(stuIds!=null){
					//取得除去 arrangeStuId中剩下的学生
					stuIds.removeAll(arrangeStuId);
					dd.setLeftNumber(stuIds.size());
				}
				
			}
		}
		if(CollectionUtils.isNotEmpty(gDtoList3)){
			for(GkGroupDto dd:gDtoList3){
				Set<String> stuIds = subjectIdsMap3.get(dd.getSubjectIds());
				//取得除去 arrangeStuId中剩下的学生
				stuIds.removeAll(arrangeStuId);
				dd.setLeftNumber(stuIds.size());
			}
		}
		//根据总人数排序
		SortUtils.DESC(gDtoList,"allNumber");
		map.put("gDtoList",gDtoList);
		SortUtils.DESC(gDtoList2,"allNumber");
		map.put("gDtoList2",gDtoList2);
		map.put("gDto", gDtoList3.get(0));
		return flag;
	}
	@RequestMapping("/openClassArrange/perArrange/scheduling/page")
	@ControllerInfo(value = "开班安排-手动排班调整")
	public String showSchedulingList(@PathVariable String roundsId,String subjectIds,String groupClassId, ModelMap map){
		//去开班前
		GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		if(rounds==null){
			addErrorFtlOperation(map, "返回", "/gkelective/arrange/list/page","#showList");
	        return errorFtl(map, "未找到对应7选3系统或应轮次！");
		}
		GkSubjectArrange gkSubjectArrange = gkSubjectArrangeService.findArrangeById(rounds.getSubjectArrangeId());
		if(gkSubjectArrange == null){
			addErrorFtlOperation(map, "返回", "/gkelective/arrange/list/page","#showList");
	        return errorFtl(map, "未找到对应7选3系统或应轮次！");
		}
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()), new TR<List<Course>>() {});
		Map<String,Course> courseMap=EntityUtils.getMap(courseList,"id");
		Map<String,String> groupMap=new LinkedHashMap<String, String>();
		Map<String,String> groupMap1=new LinkedHashMap<String, String>();
		
		List<StudentSubjectDto> stuSubjectList = gkResultService.findAllStudentSubjectDto(rounds.getSubjectArrangeId(),null);
		if(CollectionUtils.isNotEmpty(stuSubjectList)){
			for(StudentSubjectDto d:stuSubjectList){
				//选择满3门才算组合
				if(d.getChooseSubjectIds().size()==gkSubjectArrange.getSubjectNum()){
					String ids = keySort(d.getChooseSubjectIds());
					if(StringUtils.isBlank(subjectIds)){
						subjectIds=ids;
					}
					if(!groupMap.containsKey(ids)){
						groupMap.put(ids, nameSet(courseMap,ids));
					}
					List<String> idsList = keySort2(d.getChooseSubjectIds());
					if(CollectionUtils.isNotEmpty(idsList)){
						for(String s:idsList){
							if(!groupMap1.containsKey(s)){
								groupMap1.put(s, nameSet(courseMap,s));
							}
						}
					}
				}
			}
			for(String key:groupMap1.keySet()){
				groupMap.put(key, groupMap1.get(key));
			}
		}else{
			addErrorFtlOperation(map, "返回", "/gkelective/"+rounds.getSubjectArrangeId()+"/arrangeRounds/index/page","#showList");
	        return errorFtl(map, "还没有学生选课信息！");
		}

		if(StringUtils.isBlank(subjectIds)){
			addErrorFtlOperation(map, "返回", "/gkelective/"+rounds.getId()+"/openClassArrange/perArrange/list/page","#groupList");
	        return errorFtl(map, "没有组合可以调整！");
		}
		if(isNowArrange(roundsId)){
			addErrorFtlOperation(map, "返回", "/gkelective/"+rounds.getId()+"/openClassArrange/perArrange/list/page","#groupList");
	        return errorFtl(map, "单科正在排班中，不能进行调整！");
		}
		groupMap.put(GkElectveConstants.GUID_ZERO, "混合");
		map.put("groupMap", groupMap);
		map.put("subjectIds", subjectIds);
		//左边
		List<GkGroupClass> groupClassList=new ArrayList<GkGroupClass>();
		if(StringUtils.isNotBlank(subjectIds)){
			groupClassList=gkGroupClassService.findGkGroupClassBySubjectIds(subjectIds,roundsId);
		}
		map.put("groupClassList", groupClassList);
		map.put("groupClassId", groupClassId);
		map.put("roundIds",roundsId);
		return "/gkelectiveys/openClassArrange/schedulingIndex.ftl";
	}
	/**
	 * 返回男生人数
	 * @param stuSubjectList
	 * @param courseList
	 * @param avgMap 各科平均分
	 * @return
	 */
	private int countStu(List<StudentSubjectDto> stuSubjectList,List<Course> courseList,Map<String,Double> avgMap,Map<String,Double> allScoreMap){
		int length=stuSubjectList.size();
		for(Course c:courseList){
			allScoreMap.put(c.getId(), 0.0);
			avgMap.put(c.getId(), 0.0);
		}
		if(length==0){
			return 0;
		}
		int manCount=0;
		for(StudentSubjectDto stu:stuSubjectList){
			if(GkElectveConstants.MALE==stu.getSex()){
				manCount=manCount+1;
			}
			Map<String, Double> scoreMap = stu.getScoreMap();
			if(scoreMap.size()>0){
				for(String subjectId:scoreMap.keySet()){
					if(avgMap.containsKey(subjectId)){
						allScoreMap.put(subjectId, allScoreMap.get(subjectId)+scoreMap.get(subjectId));
					}
				}
			}
		}
		for(String subjectId:avgMap.keySet()){
			double avg = allScoreMap.get(subjectId)/length;
			BigDecimal  b  = new BigDecimal(avg);
			avg=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
			avgMap.put(subjectId, avg);
		}
		return manCount;
		
	}
	@ResponseBody
	@RequestMapping("/openClassArrange/perArrange/findGroupClass")	
	@ControllerInfo("查询组合班级")
	public String findGroupClass(@PathVariable String roundsId,String subjectIds){
		JSONArray jsonArr=new JSONArray();
		JSONObject jsonObj=null;
		if(StringUtils.isBlank(subjectIds)){
			return jsonArr.toJSONString();
		}
		List<GkGroupClass> groupClassList = gkGroupClassService.findGkGroupClassBySubjectIds(subjectIds,roundsId);
		if(CollectionUtils.isNotEmpty(groupClassList)){
			for(GkGroupClass g:groupClassList){
				jsonObj = new JSONObject();
				jsonObj.put("id", g.getId());
				jsonObj.put("name", g.getGroupName());
				jsonArr.add(jsonObj);
			}
		}else{
			return jsonArr.toJSONString();
		}
		return jsonArr.toJSONString();
	}
	
	@RequestMapping("/openClassArrange/perArrange/schedulingLeft/page")
	@ControllerInfo(value = "开班安排-手动排班调整-左边")	
	public String loadSchedulingLeft(@PathVariable String roundsId,String subjectIds,ModelMap map){
		Set<String> subjectIdSet=new HashSet<String>();
		String[] subjectIdArr = subjectIds.split(",");
		for(String s1:subjectIdArr){
			if(StringUtils.isNotBlank(s1)){
				subjectIdSet.add(s1);
			}
		}
		GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		if(rounds==null){
			addErrorFtlOperation(map, "返回", "/gkelective/arrange/list/page","#showList");
	        return errorFtl(map, "未找到对应7选3系统或应轮次！");
		}
		GkSubjectArrange gkSubjectArrange = gkSubjectArrangeService.findArrangeById(rounds.getSubjectArrangeId());
		if(gkSubjectArrange == null){
			addErrorFtlOperation(map, "返回", "/gkelective/arrange/list/page","#showList");
	        return errorFtl(map, "未找到对应7选3系统或应轮次！");
		}
		if(StringUtils.isBlank(subjectIds)){
			addErrorFtlOperation(map, "返回", "/gkelective/"+rounds.getId()+"/openClassArrange/perArrange/list/page","#groupList");
	        return errorFtl(map, "没有选择组合可以调整！");
		}
		List<Course> courseList = SUtils.dt(
				courseRemoteService.findListByIds(subjectIdSet.toArray(new String[0])),
				new TR<List<Course>>() {});
		map.put("courseList", courseList);
		Course course = new Course();
		course.setId(GkStuRemark.YSY_SUBID);
		course.setSubjectName(GkStuRemark.YSY_SUBNAME);
		courseList.add(course);
		//查询某种组合的学生结果
		List<StudentSubjectDto> allstuSubjectList = null;
		if(GkElectveConstants.GUID_ZERO.equals(subjectIds)){
			//32个0为混合
			allstuSubjectList = gkResultService.findAllStudentSubjectDto(rounds.getSubjectArrangeId(),null);
		}else{
			allstuSubjectList = gkResultService.findAllStudentSubjectDto(rounds.getSubjectArrangeId(),subjectIdSet);
		}
		List<StudentSubjectDto> stuSubjectList=new ArrayList<StudentSubjectDto>();
		List<String> stuList=null;
		Set<String> noStuId=new HashSet<String>();
		if(CollectionUtils.isNotEmpty(allstuSubjectList)){
			List<GkGroupClass> groupClassList=gkGroupClassService.findGkGroupClassBySubjectIds(null,roundsId);
			if(CollectionUtils.isNotEmpty(groupClassList)){
				for(GkGroupClass g:groupClassList){
					stuList = g.getStuIdList();
					if(CollectionUtils.isNotEmpty(stuList)){
						noStuId.addAll(stuList);
					}
				}
				for(StudentSubjectDto s:allstuSubjectList){
					if(!noStuId.contains(s.getStuId())){
						stuSubjectList.add(s);
					}
				}
			}else{
				stuSubjectList=allstuSubjectList;
			}
		}
		int maxCount=stuSubjectList.size();
		Map<String,Double> avgMap=new HashMap<String,Double>();//平均分
		Map<String,Double> allScoreMap=new HashMap<String,Double>();//总分
		int manCount = countStu(stuSubjectList, courseList, avgMap,allScoreMap);
		int womanCount=maxCount-manCount;
		map.put("maxCount", maxCount);
		map.put("avgMap", avgMap);
		map.put("manCount", manCount);
		map.put("womanCount", womanCount);
		map.put("allScoreMap", allScoreMap);
		
		map.put("stuSubjectList", stuSubjectList);
		
		map.put("rightOrLeft", "left");
		
		map.put("roundsId", roundsId);
		map.put("subjectIds", subjectIds);
//		Map<String,String> stuIdMap=new HashMap<String,String>();
//		if(StringUtils.isNotBlank(stuIdStr)){
//			String[] strIdArr = stuIdStr.split(",");
//			for(String s:strIdArr){
//				stuIdMap.put(s, s);
//			}
//		}
//		map.put("stuIdMap", stuIdMap);
		return "/gkelectiveys/openClassArrange/schedulingList.ftl";
	}
	@RequestMapping("/openClassArrange/perArrange/schedulingRight/page")
	@ControllerInfo(value = "开班安排-手动排班调整-右边")	
	public String loadSchedulingRight(@PathVariable String roundsId,String subjectIds,String groupClassId, ModelMap map){
		GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		if(rounds==null){
			addErrorFtlOperation(map, "返回", "/gkelective/arrange/list/page","#showList");
	        return errorFtl(map, "未找到对应7选3系统或应轮次！");
		}
		if(StringUtils.isBlank(subjectIds)){
			addErrorFtlOperation(map, "返回", "/gkelective/"+rounds.getId()+"/openClassArrange/perArrange/list/page","#groupList");
	        return errorFtl(map, "没有选择组合可以调整！");
		}
		Set<String> subjectIdSet=new HashSet<String>();
		if(!GkElectveConstants.GUID_ZERO.equals(subjectIds)){
			String[] subjectIdArr = subjectIds.split(",");
			for(String s1:subjectIdArr){
				if(StringUtils.isNotBlank(s1)){
					subjectIdSet.add(s1);
				}
			}
		}
		List<Course> courseList = SUtils.dt(
				courseRemoteService.findListByIds(subjectIdSet.toArray(new String[0])),
				new TR<List<Course>>() {});
		map.put("courseList", courseList);
		Course course = new Course();
		course.setId(GkStuRemark.YSY_SUBID);
		course.setSubjectName(GkStuRemark.YSY_SUBNAME);
		courseList.add(course);
		List<StudentSubjectDto> stuSubjectList=new ArrayList<StudentSubjectDto>();
		if(StringUtils.isNotBlank(groupClassId)){
			GkGroupClass group = gkGroupClassService.findById(groupClassId);
			if(group==null){
				addErrorFtlOperation(map, "返回", "/gkelective/"+rounds.getId()+"/openClassArrange/perArrange/list/page","#groupList");
		        return errorFtl(map, "未找到选择的班级！");
			}
			List<StudentSubjectDto> allstuSubjectList = gkResultService.findAllStudentSubjectDto(rounds.getSubjectArrangeId(),GkElectveConstants.GUID_ZERO.equals(subjectIds)?null:subjectIdSet);
			List<String> stuList = group.getStuIdList();
			List<String> errorStuId=new ArrayList<String>();
			if(CollectionUtils.isNotEmpty(stuList)){
				if(CollectionUtils.isNotEmpty(allstuSubjectList)){
					Map<String, StudentSubjectDto> stuMap = EntityUtils.getMap(allstuSubjectList, "stuId");
					for(String s:stuList){
						if(stuMap.containsKey(s)){
							stuSubjectList.add(stuMap.get(s));
						}else{
							errorStuId.add(s);
						}
					}
					
				}else{
					//该组合下没有学生  那么该班级下数据都不对
				}
			}
		}
		int maxCount=stuSubjectList.size();
		Map<String,Double> avgMap=new HashMap<String,Double>();//平均分
		Map<String,Double> allScoreMap=new HashMap<String,Double>();//总分
		int manCount = countStu(stuSubjectList, courseList, avgMap,allScoreMap);
		int womanCount=maxCount-manCount;
		map.put("maxCount", maxCount);
		map.put("avgMap", avgMap);
		map.put("manCount", manCount);
		map.put("womanCount", womanCount);
		map.put("allScoreMap", allScoreMap);
		map.put("stuSubjectList", stuSubjectList);
		map.put("groupClassId", groupClassId);
		map.put("rightOrLeft", "right");
		return "/gkelectiveys/openClassArrange/schedulingList.ftl";
	}
	@RequestMapping("/openClassArrange/perArrange/showStu/page")
	@ControllerInfo(value = "开班安排-查看")
	public String showStu(@PathVariable String roundsId,String subjectIds,String groupClassId, ModelMap map){
		//去开班前
		GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		if(rounds==null){
			addErrorFtlOperation(map, "返回", "/gkelective/arrange/list/page","#showList");
	        return errorFtl(map, "未找到对应7选3系统或应轮次！");
		}
		GkSubjectArrange gkSubjectArrange = gkSubjectArrangeService.findArrangeById(rounds.getSubjectArrangeId());
		if(gkSubjectArrange == null){
			addErrorFtlOperation(map, "返回", "/gkelective/arrange/list/page","#showList");
	        return errorFtl(map, "未找到对应7选3系统或应轮次！");
		}
		List<GkGroupClass> groupClassList = gkGroupClassService.findGkGroupClssList(roundsId, subjectIds, null);
		map.put("groupClassList", groupClassList);
		map.put("groupClassId", groupClassId);
		map.put("roundsId", roundsId);
		map.put("subjectIds", subjectIds);
		
		return "/gkelectiveys/openClassArrange/schedulingShowIndex.ftl";	
	}
	@RequestMapping("/openClassArrange/perArrange/schedulingOne/page")
	@ControllerInfo(value = "开班安排-查看详细")	
	public String loadOneTable(@PathVariable String roundsId,String groupClassId, ModelMap map){
		GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		if(rounds==null){
			addErrorFtlOperation(map, "返回", "/gkelective/arrange/list/page","#showList");
	        return errorFtl(map, "未找到对应7选3系统或应轮次！");
		}
		GkGroupClass group = gkGroupClassService.findById(groupClassId);
		map.put("groupClassId", groupClassId);
		if(group==null || StringUtils.isBlank(group.getSubjectIds())){
			map.put("maxCount", 0);
			map.put("manRatio", 0);
			map.put("womanRatio", 0);
			map.put("avgMap", null);
			map.put("stuSubjectList", null);
			map.put("courseList", null);
			
		}else{
			List<String> stuIds=group.getStuIdList();
			if(CollectionUtils.isNotEmpty(stuIds)){
				String subjectIds = group.getSubjectIds();
				Set<String> subjectIdSet=new HashSet<String>();
				String[] subjectIdArr = subjectIds.split(",");
				for(String s1:subjectIdArr){
					if(StringUtils.isNotBlank(s1)){
						subjectIdSet.add(s1);
					}
				}
				List<Course> courseList = SUtils.dt(
						courseRemoteService.findListByIds(subjectIdSet.toArray(new String[0])),
						new TR<List<Course>>() {});
				Course course = new Course();
				course.setId(GkStuRemark.YSY_SUBID);
				course.setSubjectName(GkStuRemark.YSY_SUBNAME);
				courseList.add(course);
				map.put("courseList", courseList);
				List<StudentSubjectDto> stuSubjectList = gkResultService.findStudentSubjectDtoByStudent(rounds.getSubjectArrangeId(),stuIds);
				int maxCount=stuSubjectList.size();
				double manRatio=0;
				double womanRatio=0;
				Map<String,Double> avgMap=new HashMap<String,Double>();//平均分
				Map<String,Double> allScoreMap=new HashMap<String,Double>();//总分
				int manCount = countStu(stuSubjectList, courseList, avgMap,allScoreMap);
				if(maxCount!=0){
					manRatio=manCount*100.0/maxCount;
					BigDecimal  b  = new BigDecimal(manRatio);
					manRatio  = b.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();  
					womanRatio=100-manRatio;
				}
				map.put("maxCount", maxCount);
				map.put("manRatio", manRatio);
				map.put("womanRatio", womanRatio);
				map.put("avgMap", avgMap);
				map.put("stuSubjectList", stuSubjectList);
			}else{
				map.put("maxCount", 0);
				map.put("manRatio", 0);
				map.put("womanRatio", 0);
				map.put("avgMap", null);
				map.put("stuSubjectList", null);
				map.put("courseList", null);
			}
		}
		return "/gkelectiveys/openClassArrange/schedulingList1.ftl";
	}
	
	@RequestMapping("/openClassArrange/perArrange/schedulingEdit/page")
	@ControllerInfo(value = "开班安排-手动排班调整-新增班级")	
	public String schedulingEdit(@PathVariable String roundsId,String subjectIds,String stuIdStr,ModelMap map){
		
		if(isNowArrange(roundsId)){
	        return errorFtl(map, "单科正在排班中，不能进行调整！");
		}
		final GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		if(rounds==null){
	        return errorFtl(map, "未找到对应7选3系统或应轮次！");
		}
		map.put("rounds", rounds);
		if(GkElectveConstants.TRUE_STR.equals(rounds.getOpenClassType())){
			GkSubjectArrange gkArrange = RedisUtils.getObject(GkElectveConstants.GK_ARRANGE_KEY+rounds.getSubjectArrangeId(), RedisUtils.TIME_FIVE_MINUTES, new TypeReference<GkSubjectArrange>(){}, new RedisInterface<GkSubjectArrange>(){
				@Override
				public GkSubjectArrange queryData() {
					return gkSubjectArrangeService.findArrangeById(rounds.getSubjectArrangeId());
				}
			});
	        final String gid=gkArrange.getGradeId();
			List<Clazz> clazzList = RedisUtils.getObject(GkElectveConstants.GRADE_CLASS_LIST_KEY+gid, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<List<Clazz>>(){}, new RedisInterface<List<Clazz>>(){
				@Override
				public List<Clazz> queryData() {
					return SUtils.dt(classRemoteService.findBySchoolIdGradeId(gkArrange.getUnitId(),gid),new TR<List<Clazz>>() {});
				}
			});
			//排除掉已经使用的
			List<GkGroupClass> findByRoundsId = gkGroupClassService.findByRoundsId(roundsId);
			Set<String> useClassIds = new HashSet<String>();
			for (GkGroupClass gkGroupClass : findByRoundsId) {
				if(StringUtils.isNotBlank(gkGroupClass.getClassId())){
					useClassIds.add(gkGroupClass.getClassId());
				}
			}
			List<Clazz> removeClazzList = new ArrayList<Clazz>();
			for (Clazz clazz : clazzList) {
				if(useClassIds.contains(clazz.getId())){
					removeClazzList.add(clazz);
				}
			}
			clazzList.removeAll(removeClazzList);
			map.put("clazzList", clazzList);
		}
		Set<String> subjectIdSet=new HashSet<String>();
		String[] subjectIdArr = subjectIds.split(",");
		for(String s1:subjectIdArr){
			if(StringUtils.isNotBlank(s1)){
				subjectIdSet.add(s1);
			}
		}
		List<Course> courseList = SUtils.dt(
				courseRemoteService.findListByIds(subjectIdSet.toArray(new String[0])),
				new TR<List<Course>>() {});
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, "id");
		List<GkGroupClass> groupClassList=gkGroupClassService.findGkGroupClassBySubjectIds(subjectIds,roundsId);
		String groupName;
		if(GkElectveConstants.GUID_ZERO.equals(subjectIds)){
			groupName = "混合";
		}else{
			groupName = nameSet(courseMap, subjectIds);
		}
		GkGroupClass gkGroupClass= new GkGroupClass();
		gkGroupClass.setSubjectIds(subjectIds);
		int k=1;
		if(CollectionUtils.isNotEmpty(groupClassList)){
			List<String> groupNameList = EntityUtils.getList(groupClassList, "groupName");
			while(true){
				if(!groupNameList.contains(groupName+k+"班")){
					break;
				}
				k++;
			}
		}
		gkGroupClass.setGroupName(groupName+k+"班");
		map.put("gkGroupClass", gkGroupClass);
		map.put("subjectIds", subjectIds);
		map.put("roundsId", roundsId);
		map.put("stuIdStr", stuIdStr);
		return "/gkelectiveys/openClassArrange/schedulingEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/openClassArrange/perArrange/schedulingEdit/save")
    @ControllerInfo(value = "保存班级名称")
	public String saveGroup(@PathVariable String roundsId,GkGroupClass gkGroupClass,ModelMap map){
		try {
			if(isNowArrange(roundsId)){
				return error("单科正在排班中，不能进行操作");
			}
			if(StringUtils.isBlank(gkGroupClass.getId())){
				gkGroupClass.setId(UuidUtils.generateUuid());
			}
			//验证名字是否重复
			List<GkGroupClass> groupClassList=gkGroupClassService.findGkGroupClassBySubjectIds(gkGroupClass.getSubjectIds(),roundsId);
			if(CollectionUtils.isNotEmpty(groupClassList)){
				List<String> groupNameList = EntityUtils.getList(groupClassList, "groupName");
				if(groupNameList.contains(gkGroupClass.getGroupName())){
					return error("班级名称重复！");
				}
			}
			if(StringUtils.isBlank(gkGroupClass.getSubjectIds())){
				return error("数据错误！");
			}
			String[] split = gkGroupClass.getSubjectIds().split(",");
			if(split.length == 3){
				gkGroupClass.setGroupType(GkElectveConstants.GROUP_TYPE_1);
			}else if(split.length == 2){
				gkGroupClass.setGroupType(GkElectveConstants.GROUP_TYPE_2);
			}else if(split.length == 1){
				gkGroupClass.setGroupType(GkElectveConstants.GROUP_TYPE_3);
			}
			
			List<GkGroupClassStu> insertList=new ArrayList<GkGroupClassStu>();
			List<GkGroupClass> ginsertList=new ArrayList<GkGroupClass>();
			ginsertList.add(gkGroupClass);
			GkGroupClassStu gkGroupClassStu=null;
			if(StringUtils.isNotBlank(gkGroupClass.getStuIdStr())){
				String[] strIdArr = gkGroupClass.getStuIdStr().split(",");
				for(String s:strIdArr){
					gkGroupClassStu=new GkGroupClassStu();
					gkGroupClassStu.setGroupClassId(gkGroupClass.getId());
					gkGroupClassStu.setStudentId(s);
					gkGroupClassStu.setId(UuidUtils.generateUuid());
					insertList.add(gkGroupClassStu);
				}
			}
			//考虑班级学生重复：理论上新增班级 不会出现重复
			gkGroupClassService.saveGroup(ginsertList,insertList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return error("保存失败！");
		}
		return success("保存成功！");
	}
	
	private String checkGroupStu(String subjectArrangeId,String[] stuId,Set<String> subjectIdSet){
		List<StudentSubjectDto> stuSubjectList = gkResultService.findStudentSubjectDtoByStuId(subjectArrangeId,stuId);
		if(CollectionUtils.isEmpty(stuSubjectList)){
			return "保存失败，所选的学生的选课记录已经不存在！";
		}
		String errorStr="";
		for(StudentSubjectDto dto:stuSubjectList){
			if(dto.getChooseSubjectIds().size()<=0){
				errorStr=errorStr+"、"+dto.getStuName();
				continue;
			}
			if(CollectionUtils.union(dto.getChooseSubjectIds(),subjectIdSet).size()==dto.getChooseSubjectIds().size()){
				
			}else if(!(subjectIdSet.size() == 1 && subjectIdSet.contains(GkElectveConstants.GUID_ZERO))){
				errorStr=errorStr+"、"+dto.getStuName();
				continue;
			}
		}
		if(StringUtils.isNotBlank(errorStr)){
			errorStr="保存失败，其中有"+errorStr.substring(1)+"学生的选课组合与该班级科目组合不一致！";
		}
		return errorStr;
	}
	
	@ResponseBody
	@RequestMapping("/openClassArrange/groupClass/save")
    @ControllerInfo(value = "保存班级学生")
	public String groupClassSave(@PathVariable String roundsId,String groupClassId,String stuId,ModelMap map){
		try {
			GkRounds rounds=gkRoundsService.findRoundById(roundsId);
			if(rounds==null){
				return error("该轮次已不存在！");
			}
			if(isNowArrange(roundsId)){
				return error("单科排班正在进行中，不能进行操作");
			}
			if(StringUtils.isNotBlank(groupClassId)){
				GkGroupClass gk = gkGroupClassService.findById(groupClassId);
				if(gk==null || (!gk.getRoundsId().equals(roundsId))){
					return error("该轮次下班级不存在！");
				}
				String subjectIds = gk.getSubjectIds();
				Set<String> subjectIdSet=new HashSet<String>();
				String[] subjectIdArr = subjectIds.split(",");
				for(String s1:subjectIdArr){
					if(StringUtils.isNotBlank(s1)){
						subjectIdSet.add(s1);
					}
				}
				Set<String> stuIds=new HashSet<String>();
				if(StringUtils.isNotBlank(stuId)){
					String[] arr = stuId.split(",");
					if(arr.length>0){
						//判断当前
						String error=checkGroupStu(rounds.getSubjectArrangeId(), arr, subjectIdSet);
						if(StringUtils.isNotBlank(error)){
							return error(error);
						}
					}
					for(int i=0;i<arr.length;i++){
						if(StringUtils.isNotBlank(arr[i])){
							stuIds.add(arr[i]);
						}
					}
					
				}
				gkGroupClassStuService.saveStuList(stuIds,groupClassId);
				
			}else{
				return error("该轮次下班级不存在！");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return error("保存失败！");
		}
		return success("保存成功！");
	}
	
	
	@ResponseBody
	@RequestMapping("/openClassArrange/moveGroup")
    @ControllerInfo(value = "解散")
	public String moveGroup(@PathVariable String roundsId,String subjectIds,String groupId,ModelMap map){
		try {
			if(isNowArrange(roundsId)){
				return error("单科正在排班中，不能进行操作！");
			}
			if(StringUtils.isNotBlank(groupId)){
				gkGroupClassService.deleteById(groupId);
			}else if(StringUtils.isNotBlank(subjectIds)){
				gkGroupClassService.deleteBySubjectIds(subjectIds,roundsId);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return error("解散数据失败！");
		}
		return success("解散数据成功！");
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/openClassArrange/clearNotPerArrange")
    @ControllerInfo(value = "清除不符合数据")
	public String clearNotGroup(@PathVariable String roundsId,ModelMap map){
		try {
			//去开班前
			GkRounds rounds=gkRoundsService.findRoundById(roundsId);
			if(rounds==null){
				return error("未找到对应7选3系统或应轮次！");
			}
			GkSubjectArrange gkSubjectArrange = gkSubjectArrangeService.findArrangeById(rounds.getSubjectArrangeId());
			if(gkSubjectArrange == null){
				return error("未找到对应7选3系统或应轮次！");
			}
			
			List<StudentSubjectDto> stuSubjectList = gkResultService.findAllStudentSubjectDto(rounds.getSubjectArrangeId(),null);
			//已有班级
			List<GkGroupDto> oldgDtoList=gkGroupClassService.findGkGroupDtoByRoundsId(roundsId,null);
			if(CollectionUtils.isEmpty(oldgDtoList)){
				return success("清除数据成功！");
			}
			Set<String> delIds=new HashSet<String>();
			Set<String> groupIds=null;
			if(CollectionUtils.isEmpty(stuSubjectList)){
				//删除所有组合信息
				for(GkGroupDto g:oldgDtoList){
					if(CollectionUtils.isNotEmpty(g.getGkGroupClassList())){
						List<GkGroupClass> groupClass = g.getGkGroupClassList();
						groupIds = EntityUtils.getSet(groupClass, "id");
						delIds.addAll(groupIds);
					}
				}
				
				if(delIds.size()>0){
					gkGroupClassService.deleteById(delIds.toArray(new String[]{}));
				}
			}else{
				List<GkGroupClass> updateGroup=new ArrayList<GkGroupClass>();
				Map<String,Set<String>> subjectIdsMap=new HashMap<String, Set<String>>();//同组合下学生(包括2门)
				for(StudentSubjectDto d:stuSubjectList){
					//选择满3门才算组合
					if(d.getChooseSubjectIds().size()==gkSubjectArrange.getSubjectNum()){
						String ids = keySort(d.getChooseSubjectIds());
						if(!subjectIdsMap.containsKey(ids)){
							subjectIdsMap.put(ids, new HashSet<String>());
							
						}
						subjectIdsMap.get(ids).add(d.getStuId());
						List<String> idsList = keySort2(d.getChooseSubjectIds());
						if(CollectionUtils.isNotEmpty(idsList)){
							for(String key:idsList){
								if(!subjectIdsMap.containsKey(key)){
									subjectIdsMap.put(key, new HashSet<String>());
									
								}
								subjectIdsMap.get(key).add(d.getStuId());
							}
						}
					}
				}
				List<GkGroupClass> gc=null;
				Set<String> stusIds=null;
				for(GkGroupDto dd:oldgDtoList){
					for(GkGroupDto g:oldgDtoList){
						if(CollectionUtils.isNotEmpty(g.getGkGroupClassList())){
							gc = g.getGkGroupClassList();
							if(subjectIdsMap.containsKey(dd.getSubjectIds())){
								stusIds=subjectIdsMap.get(dd.getSubjectIds());
								for(GkGroupClass gcc:gc){
									if(CollectionUtils.isNotEmpty(gcc.getStuIdList())){
										//交集
										List<String> stuIdnow=(List<String>) CollectionUtils.intersection(gcc.getStuIdList(),stusIds);
										if(stuIdnow.size()!=gcc.getStuIdList().size()){
											//修改、
											if(CollectionUtils.isNotEmpty(stuIdnow)){
												gcc.setStuIdList(stuIdnow);
												updateGroup.add(gcc);
											}else{
												//当前为空
												delIds.add(gcc.getId());
											}
											
											break;
										}
									}else{
										//当前为空
										delIds.add(gcc.getId());
									}
								}
							}else{
								groupIds = EntityUtils.getSet(gc, "id");
								delIds.addAll(groupIds);
							}
						}
					}
				}
				if(delIds.size()>0 || updateGroup.size()>0){
					gkGroupClassService.update(delIds,updateGroup);
				}
			
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return error("清除数据失败！");
		}
		return success("清除数据成功！");
	}
	
	@ResponseBody
	@RequestMapping("/openClassArrange/checkPerArrange")
    @ControllerInfo(value = "验证预排班级内学生")
	public String checkPerArrange(@PathVariable String roundsId,ModelMap map){
		try {
			GkRounds rounds=gkRoundsService.findRoundById(roundsId);
			if(rounds==null){
				return error("当前轮次已不存在！");
			}
			GkSubjectArrange ar = gkSubjectArrangeService.findArrangeById(rounds.getSubjectArrangeId());
			if(ar == null){
				return error("选课项目不存在！");
			}
			List<StudentSubjectDto> stuSubjectList = gkResultService.findAllStudentSubjectDto(rounds.getSubjectArrangeId(),null);
			if(CollectionUtils.isEmpty(stuSubjectList)){
				return error("没有学生选课数据！");
			}
			Set<String> gIds=new HashSet<String>();//班级下人数为0
			Map<String,String> stuNameMap=new HashMap<String, String>();
			//已有班级
			List<GkGroupDto> dtoList=gkGroupClassService.findGkGroupDtoByRoundsId(roundsId,null);
			//key:组合subjectIds(排序)
			Map<String,Set<String>> subjectIdsMap=new HashMap<String, Set<String>>();//同组合下学生
			for(StudentSubjectDto d:stuSubjectList){
				stuNameMap.put(d.getStuId(), d.getStuName());
				if(d.getChooseSubjectIds().size()>0){
					String ids = keySort(d.getChooseSubjectIds());
					if(!subjectIdsMap.containsKey(ids)){
						subjectIdsMap.put(ids, new HashSet<String>());
						subjectIdsMap.get(ids).add(d.getStuId());
					}else{
						subjectIdsMap.get(ids).add(d.getStuId());
					}
					List<String> idsList = keySort2(d.getChooseSubjectIds());
					if(CollectionUtils.isNotEmpty(idsList)){
						for(String key:idsList){
							if(!subjectIdsMap.containsKey(key)){
								subjectIdsMap.put(key, new HashSet<String>());
							}
							subjectIdsMap.get(key).add(d.getStuId());
						}
					}
				}
			}
			String errorStr="";
			if(CollectionUtils.isNotEmpty(dtoList)){
				Set<String> stusIds=null;
				for(GkGroupDto dd:dtoList){
					if(subjectIdsMap.containsKey(dd.getSubjectIds())){
						if(CollectionUtils.isNotEmpty(dd.getGkGroupClassList())){
							stusIds = subjectIdsMap.get(dd.getSubjectIds());
							for(GkGroupClass cc:dd.getGkGroupClassList()){
								if(CollectionUtils.isNotEmpty(cc.getStuIdList())){
									//交集
									if(!(CollectionUtils.intersection(cc.getStuIdList(),stusIds).size()==cc.getStuIdList().size())){
										errorStr=errorStr+","+cc.getGroupName();
									}
								}else{
									gIds.add(cc.getId());
								}
							}
						}
					
					}else{
						//改组合不存在
						if(CollectionUtils.isNotEmpty(dd.getGkGroupClassList())){
							for(GkGroupClass cc:dd.getGkGroupClassList()){
								if(CollectionUtils.isNotEmpty(cc.getStuIdList())){
									errorStr=errorStr+","+cc.getGroupName();
								}else{
									gIds.add(cc.getId());
								}
							}
						}
					}
				}
			}
			if(StringUtils.isNotBlank(errorStr)){
				errorStr=errorStr.substring(1);
				return error("验证预排班级数据失败！在"+errorStr+"中存在学生选课数据与班级的组合科目信息不对应");
			}else{
				//判断学生是否有在超过两个班级
				Map<String, String> stumap = checkStuClassNum(dtoList);
				if(stumap!=null && stumap.size()>0){
					for(String s:stumap.keySet()){
						if(stuNameMap.containsKey(s) && StringUtils.isNotBlank(stumap.get(s))){
							errorStr=errorStr+","+stuNameMap.get(s)+"("+stumap.get(s)+")";
						}
					}
					return error("验证预排班级数据失败！存在"+errorStr+"学生安排在超过一个的组合班级！");
				}
			}
			//当进入组合开班时 将该项目锁啦  使得学生选课数据不变
			if(GkElectveConstants.USE_TRUE!=ar.getIsLock()){
				ar.setIsLock(GkElectveConstants.USE_TRUE);
				ar.setModifyTime(new Date());
				gkSubjectArrangeService.save(ar);
			}
			gkRoundsService.updateStep(GkElectveConstants.STEP_2, roundsId);
			if(gIds.size()>0){
				return success("存在"+gIds.size()+"个组合班级下学生数量为0！");
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return error("验证预排班级数据失败！");
		}
		//return success("验证预排班级数据成功！");
		return success("");
	}
	
	 /**
	 * 查询学生在多个班级的学生id
	 * @param roundsId
	 * @return Map  key:stuId  value:学生所在组合班级name,
	 */
	private Map<String,String> checkStuClassNum(List<GkGroupDto> list){
		if(CollectionUtils.isEmpty(list)){
			return new HashMap<String, String>();
		}
		Map<String,String> returnMap= new HashMap<String, String>();
		Map<String,Set<String>> tempMap= new HashMap<String, Set<String>>();
		Map<String,String>  groupNameMap=new HashMap<String, String>();
		for(GkGroupDto g:list){
			List<GkGroupClass> ll = g.getGkGroupClassList();
			if(CollectionUtils.isNotEmpty(ll)){
				for(GkGroupClass gg:ll){
					List<String> stuList = gg.getStuIdList();
					if(CollectionUtils.isNotEmpty(stuList)){
						groupNameMap.put(gg.getId(), gg.getGroupName());
						for(String s:stuList){
							if(!tempMap.containsKey(s)){
								tempMap.put(s, new HashSet<String>());
							}
							tempMap.get(s).add(gg.getId());
						}
					}
					
					
				}
			}
		}
		if(tempMap.size()>0){
			for(String key:tempMap.keySet()){
				if(tempMap.get(key).size()>1){
					Set<String> set = tempMap.get(key);
					String e="";
					for(String id:set){
						e=e+","+groupNameMap.get(id);
					}
					e=e.substring(1);
					returnMap.put(key, e);
				}
			}
		}
		return returnMap;
	}
	
	/**
	 * 当轮次步骤结束后 单科 组合这边不存在重新开班 只有预排处增加重新安排功能
	 * @param roundsId
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/openClassArrange/moveAllArrange")
	@ControllerInfo(value = "重新安排开班-清除条件数据(整个轮次重新安排)")
	public String refreshOpen(@PathVariable String roundsId,ModelMap map){
		try {
			GkRounds rounds=gkRoundsService.findRoundById(roundsId);
			if(rounds==null){
				return error("当前轮次已不存在！");
			}
			GkSubjectArrange ar = gkSubjectArrangeService.findArrangeById(rounds.getSubjectArrangeId());
			if(ar == null){
				return error("选课项目不存在！");
			}
			if(isNowArrange(roundsId)){
				return error("当前时间正在单科开班中，不能进行重新开班！");
			}
			gkConditionService.deleteAllArrange(roundsId,ar);
			RedisUtils.del(new String[]{roundsId+"A_ok",roundsId+"B_ok"});
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return error("重新开班清除数据失败！");
		}
		return success("重新开班清除数据成功！");
	}
	
	@RequestMapping("/openClassArrange/group/resultList/page")
	@ControllerInfo(value = "开班安排-进入手动开班结果")
	public String showGroupResultList(@PathVariable String roundsId, ModelMap map){
		map.put("roundsId", roundsId);
		GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		if(rounds.getStep() < GkElectveConstants.STEP_2){
			//改变状态
			rounds.setStep(GkElectveConstants.STEP_2);
			gkRoundsService.save(rounds);
		}
		map.put("rounds", rounds);
		findGroupList(roundsId, map);
		return "/gkelectiveys/openClassArrange/groupResultList.ftl";
	}

	public List<GkGroupClass> findGroupList(String roundsId, ModelMap map) {
		List<GkGroupClass> findByRoundsId = gkGroupClassService.findByRoundsId(roundsId);
		Map<String, GkGroupClass> groupClaMap = EntityUtils.getMap(findByRoundsId, "id");
		Set<String> classIds = new HashSet<String>();
		for (GkGroupClass item : findByRoundsId) {
			if(StringUtils.isNotBlank(item.getClassId()))
				classIds.add(item.getClassId());
		}
		Map<String, Clazz> clazzMap = new HashMap<String, Clazz>();
		if(CollectionUtils.isNotEmpty(classIds)){
			List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[0])),new TR<List<Clazz>>() {});
			clazzMap = EntityUtils.getMap(classList, "id");
		}
		Set<String> groupClassIds = EntityUtils.getSet(findByRoundsId, "id");
		Map<String, List<String>> groupClaStu = gkGroupClassStuService.findByGroupClassIdIn(groupClassIds.toArray(new String[0]));
		Set<String> stuIds = new HashSet<String>();
		for(Map.Entry<String, List<String>> entry : groupClaStu.entrySet()){
			stuIds.addAll(entry.getValue());
		}
		List<Student> stuList = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])),new TR<List<Student>>(){});
		Map<String, Student> stuMap = EntityUtils.getMap(stuList, "id");
		int manNum;
		int wManNum;
		List<String> linStrList = null;
		Student stu = null;
		GkGroupClass groupClass = null;
		Clazz clazz = null;
		for(Map.Entry<String, List<String>> entry : groupClaStu.entrySet()){
			groupClass = groupClaMap.get(entry.getKey());
			linStrList = entry.getValue();
			groupClass.setNumber(linStrList.size());
			manNum = 0;
			wManNum = 0;
			for (String stuId : linStrList) {
				stu = stuMap.get(stuId);
				if(stu != null && stu.getSex() != null){
					if(stu.getSex() == GkElectveConstants.MALE){
						manNum++;
					}else if(stu.getSex() == GkElectveConstants.FEMALE){
						wManNum++;
					}
				}
			}
			groupClass.setManNumber(manNum);
			groupClass.setWomanNumber(wManNum);
			if(StringUtils.isNotBlank(groupClass.getClassId())){
				clazz = clazzMap.get(groupClass.getClassId());
				if(clazz!=null){
					groupClass.setClassName(clazz.getClassNameDynamic());
				}
			}
		}
		map.put("groupClassList", findByRoundsId);
		return findByRoundsId;
	}
	
	@ResponseBody
	@RequestMapping("/openClassArrange/group/delete")
	@ControllerInfo(value = "开班安排-进入手动排班结果-组合班解散")
	public String deleteGroup(@PathVariable String roundsId, String groupClassId, ModelMap map){
		try {
			if(isNowArrange(roundsId)){
				return error("正在开班计算中，不能操作！");
			}
			gkBatchService.deleteByGroupClassId(roundsId,groupClassId, getLoginInfo().getUnitId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return error("解散失败！");
		}
		return success("解散成功！");
	}
	
	@RequestMapping("/openClassArrange/group/detail/page")
	@ControllerInfo(value = "手动开班结果-查看班级学生")
	public String showGroupDetail(@PathVariable String roundsId, String groupId,ModelMap map){
		map.put("roundsId", roundsId);
		map.put("groupId", groupId);
		GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		map.put("rounds", rounds);
		GkGroupClass groupClas = gkGroupClassService.findById(groupId);
		if(groupClas == null){
			addErrorFtlOperation(map, "返回", "/gkelective/"+roundsId+"/openClassArrange/group/resultList/page","#groupList");
			return errorFtl(map, "组合班已不存在！");
		}
		map.put("groupClass", groupClas);
		map.put("className", groupClas.getGroupName());
		List<Student> stuList = new ArrayList<Student>();
		if(CollectionUtils.isNotEmpty(groupClas.getStuIdList())){
			stuList = SUtils.dt(studentRemoteService.findListByIds(groupClas.getStuIdList().toArray(new String[0])),new TR<List<Student>>(){});
		}
		map.put("stuList", stuList);
		List<GkGroupClass> findGkGroupClassBySubjectIds = gkGroupClassService.findGkGroupClassBySubjectIds(groupClas.getSubjectIds(), roundsId);
		List<GkGroupClass> finList = new ArrayList<GkGroupClass>();
		for (GkGroupClass item : findGkGroupClassBySubjectIds) {
			if(!groupId.equals(item.getId()))
				finList.add(item);
		}
		map.put("groupList", finList);
		return "/gkelectiveys/openClassArrange/groupResultDetailList.ftl";
	}
	
	@RequestMapping("/openClassArrange/single/detail/page")
	@ControllerInfo(value = "全部开班结果-查看单科班级学生")
	public String showSingleDetail(@PathVariable String roundsId,String batchId, String teachClassId,ModelMap map){
		map.put("roundsId", roundsId);
		map.put("batchId", batchId);
		map.put("teachClassId", teachClassId);
		GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		map.put("rounds", rounds);
		GkBatch gkBatch = gkBatchService.findOne(batchId);
		GkTeachClassStore teachClass = gkTeachClassStoreService.findOne(teachClassId);
		if(gkBatch == null || teachClass == null){
			return errorFtl(map, "班级已不存在！");
		}
		map.put("gkBatch", gkBatch);
		map.put("className", teachClass.getClassName());
		List<Student> stuList = new ArrayList<Student>();
		List<GkTeachClassStuStore> findByClassIds = gkTeachClassStoreService.findByClassIds(new String[]{teachClassId});
		if(CollectionUtils.isNotEmpty(findByClassIds)){
			stuList = SUtils.dt(studentRemoteService.findListByIds(EntityUtils.getSet(findByClassIds, "studentId").toArray(new String[0])),new TR<List<Student>>(){});
		}
		map.put("stuList", stuList);
		List<GkBatch> findGkBatchList = gkBatchService.findGkBatchList(roundsId, gkBatch.getBatch(), gkBatch.getClassType());
		Set<String> teachClassIds = EntityUtils.getSet(findGkBatchList, "teachClassId");
		List<GkTeachClassStore> teaCls = gkTeachClassStoreService.findListByIds(teachClassIds.toArray(new String[0]));
		Map<String, GkTeachClassStore> teachClassMap = EntityUtils.getMap(teaCls, "id");
		List<GkBatch> gkBatchList = new ArrayList<GkBatch>();
		//过滤自己和科目不同的
		GkTeachClassStore gkTeachClassStore = null;
		for (GkBatch item : findGkBatchList) {
			gkTeachClassStore = teachClassMap.get(item.getTeachClassId());
			item.setClassName(gkTeachClassStore.getClassName());
			item.setSubjectId(gkTeachClassStore.getSubjectId());
			if(teachClass.getId().equals(item.getTeachClassId()) || !teachClass.getSubjectId().equals(teachClassMap.get(item.getTeachClassId()).getSubjectId())){
				continue;
			}
			gkBatchList.add(item);
		}
		map.put("gkBatchList", gkBatchList);
		return "/gkelectiveys/openClassArrange/singleResultDetailList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/openClassArrange/student/delete")
    @ControllerInfo(value = "手动开班结果-查看班级学生-学生脱离")
	public String deleteStu(@PathVariable String roundsId, String oldGroupId, String stuId, ModelMap map){
		try {
			if(isNowArrange(roundsId)){
				return error("正在开班计算中，不能操作！");
			}
			GkGroupClass findById = gkGroupClassService.findById(oldGroupId);
			if(findById == null){
				return error("组合班已不存在！");
			}
			if(StringUtils.isNotBlank(stuId)){
				gkGroupClassStuService.deleteStu(new String[]{oldGroupId}, stuId.split(","));
			}else{
				return error("没有需要操作的数据！");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return error("学生脱离失败！");
		}
		return success("学生脱离成功！");
	}
	
	@ResponseBody
	@RequestMapping("/openClassArrange/student/move")
	@ControllerInfo(value = "手动开班结果-查看班级学生-学生移动")
	public String moveStu(@PathVariable String roundsId, String oldClassId,String newClassId,String stuId, ModelMap map){
		try {
			if(isNowArrange(roundsId)){
				return error("正在开班计算中，不能操作！");
			}
			GkRounds rounds=gkRoundsService.findRoundById(roundsId);
			if(StringUtils.isNotBlank(stuId)){
				gkClassChangeService.saveClassChange(rounds.getSubjectArrangeId(), roundsId, getLoginInfo().getUserId(), oldClassId, newClassId, "", stuId);
			}else{
				return error("没有需要操作的数据！");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@RequestMapping("/openClassArrange/group/unArrange/page")
	@ControllerInfo(value = "开班安排-进入未安排学生")
	public String showUnArrangeList(@PathVariable String roundsId, ModelMap map){
		map.put("roundsId", roundsId);
		final GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		if(rounds.getStep() < GkElectveConstants.STEP_3){
			//改变状态
			rounds.setStep(GkElectveConstants.STEP_3);
			gkRoundsService.save(rounds);
		}
		map.put("rounds", rounds);
		if(GkElectveConstants.TRUE_STR.equals(rounds.getOpenClassType())){
			//开设行政班--进入未安排学生
			GkSubjectArrange gkArrange = RedisUtils.getObject(GkElectveConstants.GK_ARRANGE_KEY+rounds.getSubjectArrangeId(), RedisUtils.TIME_FIVE_MINUTES, new TypeReference<GkSubjectArrange>(){}, new RedisInterface<GkSubjectArrange>(){
				@Override
				public GkSubjectArrange queryData() {
					return gkSubjectArrangeService.findArrangeById(rounds.getSubjectArrangeId());
				}
			});
			final String gid=gkArrange.getGradeId();
			List<Clazz> clazzList = RedisUtils.getObject(GkElectveConstants.GRADE_CLASS_LIST_KEY+gid, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<List<Clazz>>(){}, new RedisInterface<List<Clazz>>(){
				@Override
				public List<Clazz> queryData() {
					return SUtils.dt(classRemoteService.findBySchoolIdGradeId(gkArrange.getUnitId(),gid),new TR<List<Clazz>>() {});
				}
			});
			map.put("clazzList", clazzList);
			return "/gkelectiveys/openClassArrange/notArrangeStuIndex.ftl";
		}else{
			addErrorFtlOperation(map, "返回", "/gkelective/"+rounds.getSubjectArrangeId()+"/arrangeRounds/index/page","#showList");
			return errorFtl(map, "该轮次状态不正确！");
		}
	}
	
	@RequestMapping("/openClassArrange/group/singleList/page")
	@ControllerInfo(value = "开班安排-单科自动排班")
	public String showSingleList(@PathVariable String roundsId, ModelMap map){
		map.put("roundsId", roundsId);
		final GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		if(rounds.getStep() < GkElectveConstants.STEP_4){
			//改变状态
			rounds.setStep(GkElectveConstants.STEP_4);
			gkRoundsService.save(rounds);
		}
		map.put("rounds", rounds);
		if(GkElectveConstants.TRUE_STR.equals(rounds.getOpenClassType())){
		}else{
		}
		List<GkTeachPlacePlan> findByRoundId = gkTeachPlacePlanService.findByRoundId(roundsId);
		if(CollectionUtils.isNotEmpty(findByRoundId)){
			map.put("isCanRestart", false);
		}else{
			map.put("isCanRestart", true);
		}
		//不开设行政班--进入单科自动排班
		List<GkConditionDto> conditions = gkConditionService.findByRoundsIdAndTypeWithDefault(roundsId, GkElectveConstants.GKCONDITION_SINGLE_0, rounds.getOpenClassType());
		// 已有开班结果
		int singleNum = gkBatchService.findByRoundsId(roundsId, GkElectveConstants.GKCONDITION_SINGLE_0).size();
		boolean canEdit = true;
		if(singleNum > 0){
			canEdit = false;
		}
		boolean needStudy = GkElectveConstants.TRUE_STR.equals(rounds.getOpenClass());
		map.put("needStudy", needStudy);
		// 学考数据
		if(CollectionUtils.isNotEmpty(conditions)){
			List<GkConditionDto> conditionBs = new ArrayList<GkConditionDto>();
			Iterator<GkConditionDto> cit = conditions.iterator();
			while(cit.hasNext()){
				GkConditionDto con = cit.next();
				if(BaseConstants.SUBJECT_TYPE_A.equals(con.getGkType())){
					conditionBs.add(con);
					cit.remove();
				}
			}
			map.put("gkConditionsBList", conditionBs);
		}
		map.put("canEdit", canEdit);
		map.put("gkConditionsList", conditions);
		map.put("type", GkElectveConstants.GKCONDITION_SINGLE_0);
//			map.put("hasNew", checkNewRound(roundsId, rounds.getSubjectArrangeId()));
		map.put("hasnowpaike", isNowArrange(roundsId));
		if("0".equals(rounds.getOpenClassType())){
			map.put("isCom", false);
		}else{
			map.put("isCom", true);
		}
		
		return "/gkelectiveys/openClassArrange/singleArrangeList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/openClassArrange/delete")
    @ControllerInfo(value = "重新安排开班-清除条件数据")
	public String refreshOpen(@PathVariable String roundsId, String type, ModelMap map){
		try {
			GkRounds rounds=gkRoundsService.findRoundById(roundsId);
			if(rounds==null){
				return error("当前轮次已不存在！");
			}
			map.put("rounds", rounds);
			GkSubjectArrange ar = gkSubjectArrangeService.findArrangeById(rounds.getSubjectArrangeId());
			if(ar == null){
				return error("选课项目不存在！");
			}
//			if(!checkNewRound(roundsId, ar.getId())){
//				return error("该轮次不是当前轮次，不能进行重新开班！");
//			}
			if(isNowArrange(roundsId)){
				return error("当前时间正在单科开班中，不能进行重新开班！");
		    }
			gkConditionService.deleteAllConditions(roundsId, type);
			RedisUtils.del(new String[]{roundsId+"A_ok",roundsId+"B_ok"});
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return error("重新开班清除数据失败！");
		}
		return success("重新开班清除数据成功！");
	}
	
	@RequestMapping("/openClassArrange/group/notArrangeStuList/page")
	@ControllerInfo(value = "开班安排-进入未安排学生-列表")
	public String showNotArrangeStuList(@PathVariable String roundsId,ChosenSubjectSearchDto searchDto, ModelMap map){
		map.put("roundsId", roundsId);
		final GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		map.put("rounds", rounds);
		List<GkGroupClass> groupClaList = gkGroupClassService.findByRoundsId(roundsId);
		Set<String> stuIds = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(groupClaList)){
			Set<String> groupClaIds = EntityUtils.getSet(groupClaList, "id");
			Map<String, List<String>> groupClaStu = gkGroupClassStuService.findByGroupClassIdIn(groupClaIds.toArray(new String[0]));
			for(Map.Entry<String, List<String>> entry : groupClaStu.entrySet()){
				stuIds.addAll(entry.getValue());
			}
		}
		List<GkResult> gkResultList = gkResultService.findGkResult(rounds.getSubjectArrangeId(), searchDto, null, stuIds);
		List<GkResult> gkResultAllList = gkResultService.findGkResult(rounds.getSubjectArrangeId(), new ChosenSubjectSearchDto(), null, stuIds);
		map.put("unAllNum", gkResultAllList.size());
		GkSubjectArrange gkArrange = RedisUtils.getObject(GkElectveConstants.GK_ARRANGE_KEY+rounds.getSubjectArrangeId(), RedisUtils.TIME_FIVE_MINUTES, new TypeReference<GkSubjectArrange>(){}, new RedisInterface<GkSubjectArrange>(){
			@Override
			public GkSubjectArrange queryData() {
				return gkSubjectArrangeService.findArrangeById(rounds.getSubjectArrangeId());
			}
		});
        final String gid=gkArrange.getGradeId();
        List<Clazz> clazzList = RedisUtils.getObject(GkElectveConstants.GRADE_CLASS_LIST_KEY+gid, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<List<Clazz>>(){}, new RedisInterface<List<Clazz>>(){
			@Override
			public List<Clazz> queryData() {
				return SUtils.dt(classRemoteService.findBySchoolIdGradeId(gkArrange.getUnitId(),gid),new TR<List<Clazz>>() {});
			}
        });
        Set<String> classIds = EntityUtils.getSet(clazzList, "id");
        Map<String, Clazz> classMap = EntityUtils.getMap(clazzList, "id");
        Clazz clazz = null;
        for (GkResult gkr : gkResultList) {
        	clazz = classMap.get(gkr.getClassId());
        	if(clazz!=null)
        		gkr.setClassName(clazz != null ? clazz.getClassNameDynamic() : "");
        }
		map.put("gkResultList", gkResultList);
		map.put("isChosenCount", gkResultList.size());
		map.put("arrangeId", rounds.getSubjectArrangeId());
		List<GkResult> unGkResultList = gkResultService.findUnChosenGkResult(rounds.getSubjectArrangeId(), searchDto, null, true, classIds.toArray(new String[0]));
		map.put("unChosenCount", unGkResultList.size());
		return "/gkelectiveys/openClassArrange/notArrangeStuList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/openClassArrange/save")
    @ControllerInfo(value = "保存开班条件数据")
	public String saveArrange(@PathVariable String roundsId,GkConditionDto saveDto,String type, ModelMap map){
		List<GkConditionDto> dtos = saveDto.getDtos();
		if(CollectionUtils.isEmpty(dtos)){
			return success("没有可保存的数据");
		}
		if(isNowArrange(roundsId)){
			return error("正在单科开班中，不能操作！");
		}
		try {
			GkRounds rounds=gkRoundsService.findRoundById(roundsId);
			rounds.setBatchCountA(saveDto.getBatchCountA());
			rounds.setBatchCountB(saveDto.getBatchCountB());
			gkRoundsService.save(rounds);
			
			gkConditionService.saveConditions(dtos, type);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return error("保存失败！");
		}
		return success("保存成功！");
	}
	
	@RequestMapping("/openClassArrange/group/allList/page")
	@ControllerInfo(value = "开班安排-进入全部开班结果")
	public String showAllList(@PathVariable String roundsId, ModelMap map){
		map.put("roundsId", roundsId);
		GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		if(rounds.getStep() < GkElectveConstants.STEP_5){
			//改变状态
			rounds.setStep(GkElectveConstants.STEP_5);
			gkRoundsService.save(rounds);
		}
		map.put("rounds", rounds);
		map.put("arrangeId", rounds.getSubjectArrangeId());
		if(GkElectveConstants.TRUE_STR.equals(rounds.getOpenClassType())){
			//开设行政班
			return "/gkelectiveys/openClassArrange/claAllResIndex.ftl";
		}else{
			//不开设行政班
			return "/gkelectiveys/openClassArrange/teaClaAllResultIndex.ftl";
		}
	}
	
	@RequestMapping("/openClassArrange/group/class/newClassList/page")
	@ControllerInfo(value = "开班安排-进入全部开班结果-组合班结果")
	public String showNewClassList(@PathVariable String roundsId, ModelMap map){
		map.put("roundsId", roundsId);
		GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		map.put("rounds", rounds);
		findGroupList(roundsId, map);
		Set<String> courseIds = new HashSet<String>();
		List<GkSubject> findGkSubjectList = gkSubjectService.findByRoundsId(roundsId,GkSubject.TEACH_TYPE1);
		for (GkSubject gkSubject : findGkSubjectList) {
			courseIds.add(gkSubject.getSubjectId());
		}
		return "/gkelectiveys/openClassArrange/claAllResNewClaList.ftl";
	}
	
	@RequestMapping("/openClassArrange/list/statistics/page")
	@ControllerInfo(value = "全部开班结果-走班班级统计")
	public String showStatistics(@PathVariable String roundsId,ModelMap map, HttpSession httpSession) {
		map.put("roundsId", roundsId);
		GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		List<GkBatch> findBatchList = gkBatchService.findBatchList(roundsId,null);
		Set<String> teachClassIds = EntityUtils.getSet(findBatchList, "teachClassId");
		Map<String, GkTeachClassStore> teachClassMap = new HashMap<String, GkTeachClassStore>();
		Map<String, List<String>> stuClassIdsMap = new HashMap<String, List<String>>();//key:student values:classIds 学生对应的教学班 
		Map<String, Set<String>> stuSubIdsMap = new HashMap<String, Set<String>>();//key:student values:subjectIds 学生对应的教学班的科目
		List<GkSubject> findGkSubjectList = gkSubjectService.findByRoundsId(roundsId, 1);
		Set<String> courseIds = EntityUtils.getSet(findGkSubjectList, "subjectId");
		List<Course> dt = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])),new TR<List<Course>>(){});
		Map<String, Course> courseMap = EntityUtils.getMap(dt, "id");
		if(teachClassIds.size() > 0){
			List<GkTeachClassStore> teaClslist = gkTeachClassStoreService.findListByIds(teachClassIds.toArray(new String[0]));
			teachClassMap = EntityUtils.getMap(teaClslist, "id");
			//学生对应的教学班
			stuClassIdsMap = gkTeachClassStoreService.findMapWithStuIdByClassIds(teachClassIds.toArray(new String[0]));
		}
		List<String> linStrList = null;
		Set<String> linStrSet = null;
		GkTeachClassStore gkTeachClassStore = null;
		for(Map.Entry<String, List<String>> entry : stuClassIdsMap.entrySet()){
			linStrList = entry.getValue();
			linStrSet = new HashSet<String>();
			for (String classId : linStrList) {
				gkTeachClassStore = teachClassMap.get(classId);
				linStrSet.add(gkTeachClassStore.getSubjectId());
			}
			stuSubIdsMap.put(entry.getKey(), linStrSet);
		}
		//学生选课
		List<StudentSubjectDto> findAllStudentSubjectDto  = gkResultService.findAllStudentSubjectDto(rounds.getSubjectArrangeId(), null);
		List<GkGroupClassStu> findGkGroupClassStuList = gkGroupClassStuService.findGkGroupClassStuList(roundsId,null);
		//学生所在组合班
		Map<String, GkGroupClassStu> stuGroupClassMap = EntityUtils.getMap(findGkGroupClassStuList, "studentId");
		List<GkGroupClass> findByRoundsId = gkGroupClassService.findByRoundsId(roundsId);
		Map<String, GkGroupClass> groupClassMap = EntityUtils.getMap(findByRoundsId, "id");
		//计算学生需要走班的是否已全部安排
		List<GkStuExceptionDto> gkStuExceptionDtoList = new ArrayList<GkStuExceptionDto>();
		Set<String> chooseSubjectIds = null;
		Set<String> noGoClassSubIds = null;
		GkGroupClassStu gkGroupClassStu = null;
		GkGroupClass gkGroupClass = null;
		GkStuExceptionDto gkStuExceptionDto = null;
		//是否重组行政班
		boolean isOpenClassType = rounds.getOpenClassType().equals(GkElectveConstants.TRUE_STR)?true:false;
		//是否开学考班
		boolean isOpenClass = rounds.getOpenClass().equals(GkElectveConstants.TRUE_STR)?true:false;;
		String[] split = null;
		Course course = null;
		for (StudentSubjectDto item : findAllStudentSubjectDto) {
			gkStuExceptionDto = new GkStuExceptionDto();
			gkStuExceptionDto.setStuName(item.getStuName());
			gkStuExceptionDto.setStuCode(item.getStuCode());
			//学生选的科目--选考科目
			chooseSubjectIds = item.getChooseSubjectIds();
			linStrSet = stuSubIdsMap.get(item.getStuId());
			if(isOpenClassType){
				//重组行政班情况
				gkGroupClassStu = stuGroupClassMap.get(item.getStuId());
				if(gkGroupClassStu != null){
					gkGroupClass = groupClassMap.get(gkGroupClassStu.getGroupClassId());
					if(gkGroupClass != null){
						noGoClassSubIds = new HashSet<String>();
						split = gkGroupClass.getSubjectIds().split(",");//学生不用走班的科目
						if(split.length == 3){
							continue;
						}
						for (String string : split) {
							noGoClassSubIds.add(string);
						}
						//courseIds 都在noGoClassSubIds中
						if(CollectionUtils.union(noGoClassSubIds, courseIds).size()<=noGoClassSubIds.size()){
							continue;
						}
						if(linStrSet==null){
							gkStuExceptionDto.setSuccess(false);
							gkStuExceptionDto.setMsg("有科目未安排！");
						}else{
							for(String couId : courseIds){
								if(noGoClassSubIds.contains(couId)){//过滤掉不用走班的
									continue;
								}
								if(!isOpenClass){
									//不开学考班
									if(chooseSubjectIds.contains(couId)){
										if(!linStrSet.contains(couId)){
											gkStuExceptionDto.setSuccess(false);
											course = courseMap.get(couId);
											if(course != null){
												gkStuExceptionDto.setMsg(course.getSubjectName()+"科目未安排！");
											}else{
												gkStuExceptionDto.setMsg("有科目未安排！");
											}
										}
									}else{
										continue;
									}
								}else{
									//开学考班
									if(!linStrSet.contains(couId)){
										gkStuExceptionDto.setSuccess(false);
										course = courseMap.get(couId);
										if(course != null){
											gkStuExceptionDto.setMsg(course.getSubjectName()+"科目未安排！");
										}else{
											gkStuExceptionDto.setMsg("有科目未安排！");
										}
									}
								}
							}
						}
						
					}else{
						gkStuExceptionDto.setSuccess(false);
						gkStuExceptionDto.setMsg("所在组合班未找到！");
					}
				}else{
					gkStuExceptionDto.setSuccess(false);
					gkStuExceptionDto.setMsg("未分配到组合班！");
				}
			}else{
				//教学班
				if(CollectionUtils.isEmpty(linStrSet)){
					gkStuExceptionDto.setSuccess(false);
					gkStuExceptionDto.setMsg("未分配到任何教学班！");
				}else{
					for(String couId : courseIds){
						if(!isOpenClass){
							//不开学考班
							if(chooseSubjectIds.contains(couId)){
								if(!linStrSet.contains(couId)){
									gkStuExceptionDto.setSuccess(false);
									course = courseMap.get(couId);
									if(course != null){
										gkStuExceptionDto.setMsg(course.getSubjectName()+"科目未安排！");
									}else{
										gkStuExceptionDto.setMsg("有科目未安排！");
									}
								}
							}else{
								continue;
							}
						}else{
							//开学考班
							if(!linStrSet.contains(couId)){
								gkStuExceptionDto.setSuccess(false);
								course = courseMap.get(couId);
								if(course != null){
									gkStuExceptionDto.setMsg(course.getSubjectName()+"科目未安排！");
								}else{
									gkStuExceptionDto.setMsg("有科目未安排！");
								}
							}
						}
					}
				}
			}
			if(!gkStuExceptionDto.isSuccess())
				gkStuExceptionDtoList.add(gkStuExceptionDto);
		}
		map.put("stuNumber", findAllStudentSubjectDto.size());
		map.put("gkStuExceptionDtoList", gkStuExceptionDtoList);
		//根据msg组装结果
		Map<String,String> returnStr=new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(gkStuExceptionDtoList)){
			for(GkStuExceptionDto dto:gkStuExceptionDtoList){
				if(StringUtils.isNotBlank(dto.getMsg())){
					String key = dto.getMsg().substring(0, dto.getMsg().length()-1);
					String str = dto.getStuName()+(dto.getStuCode()==null?"":dto.getStuCode());
					if(returnStr.containsKey(key)){
						returnStr.put(key,returnStr.get(key)+","+str);
					}else{
						returnStr.put(key,str);
					}
				}
			}
		}
		map.put("returnStrMap", returnStr);
		
		Map<String,List<GkBatch>> gkBatchMap1 = new TreeMap<String, List<GkBatch>>();//key subjectName+classType
		Map<String,List<GkBatch>> gkBatchMap2 = new TreeMap<String, List<GkBatch>>();//key 批次+batch
		GkTeachClassStore teachClass = null;
		List<GkBatch> linList = null;
		for (GkBatch gkBatch : findBatchList) {
			teachClass = teachClassMap.get(gkBatch.getTeachClassId());
			course = courseMap.get(teachClass.getSubjectId());
			linList = gkBatchMap1.get(course.getSubjectName()+gkBatch.getClassType());
			if(linList == null){
				linList = new ArrayList<GkBatch>();
				gkBatchMap1.put(course.getSubjectName()+gkBatch.getClassType(),linList);
			}
			linList.add(gkBatch);
		}
		for (GkBatch gkBatch : findBatchList) {
			linList = gkBatchMap2.get(BaseConstants.PC_KC+gkBatch.getBatch());
			if(linList == null){
				linList = new ArrayList<GkBatch>();
				gkBatchMap2.put(BaseConstants.PC_KC+gkBatch.getBatch(),linList);
			}
			linList.add(gkBatch);
		}
		JSONObject json=new JSONObject();
		json.put("legendData", gkBatchMap1.keySet().toArray(new String[0]));
		JSONArray jsonArr=new JSONArray();
		JSONObject json2=null;
		for(Map.Entry<String,List<GkBatch>> entry : gkBatchMap1.entrySet()){
			json2=new JSONObject();
			json2.put("value", entry.getValue().size());
			json2.put("name", entry.getKey());
			jsonArr.add(json2);
		}
		json.put("loadingData", jsonArr);
		String jsonStringData=json.toString();
		map.put("jsonStringData1", jsonStringData);
		
		json=new JSONObject();
		json.put("legendData", gkBatchMap2.keySet().toArray(new String[0]));
		jsonArr=new JSONArray();
		for(Map.Entry<String,List<GkBatch>> entry : gkBatchMap2.entrySet()){
			json2=new JSONObject();
			json2.put("value", entry.getValue().size());
			json2.put("name", entry.getKey());
			jsonArr.add(json2);
		}
		json.put("loadingData", jsonArr);
		jsonStringData=json.toString();
		map.put("jsonStringData2", jsonStringData);
		
		Map<String,Integer> gkBatchMapByBatch = new LinkedHashMap<String, Integer>();
		Map<String,Integer> gkBatchMapByCourse = new LinkedHashMap<String, Integer>();
		Integer count = 0;
		//批次形式
		for(GkBatch item : findBatchList){
			teachClass = teachClassMap.get(item.getTeachClassId());
			count = gkBatchMapByBatch.get(BaseConstants.PC_KC+item.getBatch());
			if(count == null){
				gkBatchMapByBatch.put(BaseConstants.PC_KC+item.getBatch(), 1);
			}else{
				gkBatchMapByBatch.put(BaseConstants.PC_KC+item.getBatch(), count+1);
			}
		}
		//科目形式
		Map<String,Map<String,Integer>> subBatchMap = new HashMap<String, Map<String,Integer>>();
		Map<String,Integer> linMap = null;
		for(GkBatch item : findBatchList){
			teachClass = teachClassMap.get(item.getTeachClassId());
			course = courseMap.get(teachClass.getSubjectId());
			linMap = subBatchMap.get(course.getSubjectName());
			if(linMap == null){
				linMap = new HashMap<String, Integer>();
				subBatchMap.put(course.getSubjectName(), linMap);
			}
			count = linMap.get(BaseConstants.PC_KC+item.getBatch());
			if(count == null){
				linMap.put(BaseConstants.PC_KC+item.getBatch(), 1);
			}else{
				linMap.put(BaseConstants.PC_KC+item.getBatch(), count+1);
			}
		}
		for(Map.Entry<String,Map<String,Integer>> entry : subBatchMap.entrySet()){
			linMap = entry.getValue();
			count = 0;
			for(Map.Entry<String,Integer> item : linMap.entrySet()){
				if(item.getValue() > count){
					count = item.getValue();
				}
			}
			gkBatchMapByCourse.put(entry.getKey(), count);
		}
		map.put("gkBatchMapByCourse", gkBatchMapByCourse);
		map.put("gkBatchMapByBatch", gkBatchMapByBatch);
		return "/gkelectiveys/openClassArrange/arrangeStatisticsList.ftl";
	}
	
	@RequestMapping("/openClassArrange/group/class/needGoClassIndex/page")
	@ControllerInfo(value = "开班安排-进入全部开班结果-走班班级index")
	public String showNeedGoClassIndex(@PathVariable String roundsId, ModelMap map){
		map.put("roundsId", roundsId);
		GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		map.put("rounds", rounds);
		Set<String> courseIds = new HashSet<String>();
		List<GkSubject> findGkSubjectList = gkSubjectService.findByRoundsId(roundsId,GkSubject.TEACH_TYPE1);
		for (GkSubject gkSubject : findGkSubjectList) {
			courseIds.add(gkSubject.getSubjectId());
		}
		List<Course> coursesList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])), new TR<List<Course>>(){});
		int batchSize = 0;
		if(GkElectveConstants.TRUE_STR.equals(rounds.getOpenClass())){
			//batchSize = findGkSubjectList.size();
			batchSize = rounds.getBatchCountB()+rounds.getBatchCountA();
		}else{
			batchSize = rounds.getBatchCountA() ;
		}
		map.put("coursesList", coursesList);
		map.put("batchSize", batchSize);
		
		return "/gkelectiveys/openClassArrange/claAllResNeedGoClaIndex.ftl";
	}
	
	@RequestMapping("/openClassArrange/group/class/needGoClassList/page")
	@ControllerInfo(value = "开班安排-进入全部开班结果-走班班级List")
	public String showNeedGoClassList(@PathVariable String roundsId,GoClassSearchDto goClassSearchDto, ModelMap map){
		map.put("roundsId", roundsId);
		GkRounds rounds = gkRoundsService.findRoundById(roundsId);
		map.put("rounds", rounds);
		map.put("searchViewTypeRedio", goClassSearchDto.getSearchViewTypeRedio());
		Map<String,List<GkBatch>> gkBatchMapByBatch = new LinkedHashMap<String, List<GkBatch>>();
		Map<String,List<GkBatch>> gkBatchMapByCourse = new LinkedHashMap<String, List<GkBatch>>();
		List<GkBatch> findGkBatchList = gkBatchService.findGkBatchList(roundsId, (StringUtils.isNotBlank(goClassSearchDto.getSearchBatch())?Integer.valueOf(goClassSearchDto.getSearchBatch()):null), goClassSearchDto.getSearchGkType());
		Set<String> teachClassIds = EntityUtils.getSet(findGkBatchList, "teachClassId");
		Map<String, GkTeachClassStore> teaClsMap = new HashMap<String, GkTeachClassStore>();//找教学班
		Map<String, GkTeachClassEx> teaClsExMap = new HashMap<String, GkTeachClassEx>();//找教学班辅助信息
		Map<String, Student> stuMap = new HashMap<String, Student>();//找学生
		Map<String, Course> couMap = new HashMap<String, Course>();//找科目
		Map<String, Set<String>> teaClsStuMap = new HashMap<String, Set<String>>();//教学班对应学生
		Set<String> linStr = null;
		if(CollectionUtils.isNotEmpty(teachClassIds)){
			List<GkTeachClassStore> teaClsList = gkTeachClassStoreService.findListByIds(teachClassIds.toArray(new String[0]));
			List<GkTeachClassEx> findGkTeachClassExList = gkTeachClassExService.findGkTeachClassExList(roundsId, teachClassIds.toArray(new String[0]));
			teaClsExMap = EntityUtils.getMap(findGkTeachClassExList, "teachClassId");
			teaClsMap = EntityUtils.getMap(teaClsList, "id");
			Set<String> courseIds = EntityUtils.getSet(teaClsList, "subjectId");
			List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])),new TR<List<Course>>(){});
			couMap = EntityUtils.getMap(courseList, "id");
			Map<String, List<String>> studentTeachClassMap = gkTeachClassStoreService.findMapWithStuIdByClassIds(teachClassIds.toArray(new String[0]));
			List<String> value = null;
			for(Map.Entry<String, List<String>> entry : studentTeachClassMap.entrySet()){
				value = entry.getValue();
				for (String clsId : value) {
					linStr = teaClsStuMap.get(clsId);
					if(linStr == null){
						linStr = new HashSet<String>();
						teaClsStuMap.put(clsId, linStr);
					}
					linStr.add(entry.getKey());
				}
			}
			List<Student> stuList = SUtils.dt(studentRemoteService.findListByIds(studentTeachClassMap.keySet().toArray(new String[0])),new TR<List<Student>>(){});
			stuMap = EntityUtils.getMap(stuList, "id");
		}
		Set<String> courseIds = new HashSet<String>();
		if(StringUtils.isNotBlank(goClassSearchDto.getSearchCourseId())){
			courseIds.add(goClassSearchDto.getSearchCourseId());
		}
		List<GkBatch> linGkBatchList = null;
		int manNum;
		int wManNum;
		Student stu = null;
		GkTeachClassStore teachClass = null;
		Course course = null;
		GkTeachClassEx gkTeachClassEx = null;
		
		if("1".equals(goClassSearchDto.getSearchViewTypeRedio())){
			//按批次查看
			for(GkBatch item : findGkBatchList){
				teachClass = teaClsMap.get(item.getTeachClassId());
				if(teachClass == null || (courseIds.size() > 0 && !courseIds.contains(teachClass.getSubjectId()))){
					continue;
				}
				item.setClassName(teachClass.getClassName());
				gkTeachClassEx = teaClsExMap.get(item.getTeachClassId());
				item.setAverageScore(gkTeachClassEx.getAverageScore());
				course = couMap.get(teachClass.getSubjectId());
				item.setSubjectName(course.getSubjectName());
				item.setSubjectId(teachClass.getSubjectId());
				linGkBatchList = gkBatchMapByBatch.get(BaseConstants.PC_KC+item.getBatch());
				if(linGkBatchList == null){
					linGkBatchList = new ArrayList<GkBatch>();
					gkBatchMapByBatch.put(BaseConstants.PC_KC+item.getBatch(), linGkBatchList);
				}
				linGkBatchList.add(item);
				linStr = teaClsStuMap.get(item.getTeachClassId());
				if(CollectionUtils.isNotEmpty(linStr)){
					item.setNumber(linStr.size());
					manNum = 0;
					wManNum = 0;
					for (String stuId : linStr) {
						stu = stuMap.get(stuId);
						if(stu != null && stu.getSex() != null){
							if(stu.getSex() == GkElectveConstants.MALE){
								manNum++;
							}else if(stu.getSex() == GkElectveConstants.FEMALE){
								wManNum++;
							}
						}
					}
					item.setManNumber(manNum);
					item.setWomanNumber(wManNum);
				}
			}
			map.put("gkBatchMap", gkBatchMapByBatch);
		}else if("2".equals(goClassSearchDto.getSearchViewTypeRedio())){
			//按科目查看
			for(GkBatch item : findGkBatchList){
				teachClass = teaClsMap.get(item.getTeachClassId());
				if(teachClass == null || (courseIds.size() > 0 && !courseIds.contains(teachClass.getSubjectId()))){
					continue;
				}
				item.setClassName(teachClass.getClassName());
				item.setSubjectId(teachClass.getSubjectId());
				gkTeachClassEx = teaClsExMap.get(item.getTeachClassId());
				item.setAverageScore(gkTeachClassEx.getAverageScore());
				course = couMap.get(teachClass.getSubjectId());
				linGkBatchList = gkBatchMapByCourse.get(course.getSubjectName());
				if(linGkBatchList == null){
					linGkBatchList = new ArrayList<GkBatch>();
					gkBatchMapByCourse.put(course.getSubjectName(), linGkBatchList);
				}
				linGkBatchList.add(item);
				linStr = teaClsStuMap.get(item.getTeachClassId());
				if(CollectionUtils.isNotEmpty(linStr)){
					item.setNumber(linStr.size());
					manNum = 0;
					wManNum = 0;
					for (String stuId : linStr) {
						stu = stuMap.get(stuId);
						if(stu != null && stu.getSex() != null){
							if(stu.getSex() == GkElectveConstants.MALE){
								manNum++;
							}else if(stu.getSex() == GkElectveConstants.FEMALE){
								wManNum++;
							}
						}
					}
					item.setManNumber(manNum);
					item.setWomanNumber(wManNum);
				}
			}
			map.put("gkBatchMap", gkBatchMapByCourse);
		}
		
		//排序
		if(gkBatchMapByCourse.size()>0){
			for(Entry<String, List<GkBatch>> bb:gkBatchMapByCourse.entrySet()){
				List<GkBatch> list = bb.getValue();
				if(CollectionUtils.isNotEmpty(list)){
					Collections.sort(list, new Comparator<GkBatch>() {

						@Override
						public int compare(GkBatch o1, GkBatch o2) {
							if(o1.getClassType().equals(o2.getClassType())){
								return o1.getSubjectId().compareTo(o2.getSubjectId());
							}else{
								return o1.getClassType().compareTo(o2.getClassType());
							}

						}
						
					});
				}
				
			}
		}
		if(gkBatchMapByBatch.size()>0){
			for(Entry<String, List<GkBatch>> bb:gkBatchMapByBatch.entrySet()){
				List<GkBatch> list = bb.getValue();
				if(CollectionUtils.isNotEmpty(list)){
					Collections.sort(list, new Comparator<GkBatch>() {

						@Override
						public int compare(GkBatch o1, GkBatch o2) {
							if(o1.getClassType().equals(o2.getClassType())){
								return o1.getSubjectId().compareTo(o2.getSubjectId());
							}else{
								return o1.getClassType().compareTo(o2.getClassType());
							}

						}
						
					});
				}
				
			}
		}
		//TODO
//		ChosenSubjectSearchDto dto = new ChosenSubjectSearchDto();
//		List<GkStuScoreDto> findStuScoreDtoList = gkStuRemarkService.findStuScoreDtoList(rounds.getSubjectArrangeId(), dto, null);
//		Map<String,GkStuScoreDto> dtoMap = new HashMap<String,GkStuScoreDto>();
//		for (GkStuScoreDto gkStuScoreDto : findStuScoreDtoList) {
//			dtoMap.put(gkStuScoreDto.getStudent().getId(), gkStuScoreDto);
//		}
//		Map<String, GkTeachClassEx> findByGkRoundId = gkTeachClassExService.findByGkRoundId(roundsId);
//		List<GkTeachClassStore> findByRoundsId = gkTeachClassStoreService.findByRoundsId(roundsId);
//		List<GkTeachClassEx> updateList = new ArrayList<GkTeachClassEx>();
//		for (GkTeachClassStore gkTeachClassStore : findByRoundsId) {
//			List<String> stuList = gkTeachClassStore.getStuList();
//			double dd = 0;
//			for (String string : stuList) {
//				Double double1 = dtoMap.get(string).getSubjectScore().get(Constant.GUID_ONE);
//				if(double1!=null){
//					dd+=double1.doubleValue();
//				}
//			}
//			GkTeachClassEx gkTeachClassEx2 = findByGkRoundId.get(gkTeachClassStore.getId());
//			System.out.println(gkTeachClassEx2.getId()+":"+dd/stuList.size());
//			gkTeachClassEx2.setAverageScore(dd/stuList.size());
//			updateList.add(gkTeachClassEx2);
//		}
//		gkTeachClassExService.saveAll(updateList.toArray(new GkTeachClassEx[0]));
//		
//		
//		System.out.println(11111);
		return "/gkelectiveys/openClassArrange/claAllResNeedGoClaList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/openClassArrange/single/classDelete")
    @ControllerInfo(value = "走班班级--删除班级和批次")
	public String classDelete(@PathVariable String roundsId,String batchId,String teachClassId, ModelMap map){
		if(isNowArrange(roundsId)){
			return error("正在单科开班中，不能操作！");
		}
		try {
			List<GkTeachClassStuStore> findByClassIds = gkTeachClassStoreService.findByClassIds(new String[]{teachClassId});
			if(CollectionUtils.isNotEmpty(findByClassIds)){
				return error("删除失败，此班级还存在学生！");
			}
			gkBatchService.deleteById(batchId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();
	}
	
	@RequestMapping("/openClassArrange/list/class/page")
    @ControllerInfo(value = "全部开班结果-学生走班结果list")
    public String showClassList(@PathVariable String roundsId,ModelMap map,HttpServletRequest request, HttpSession httpSession) {
		String classId = request.getParameter("classId");
		GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		map.put("rounds", rounds);
		GkSubjectArrange arrange = gkSubjectArrangeService.findArrangeById(rounds.getSubjectArrangeId());
		String gradeId = arrange.getGradeId();
		String unitId = arrange.getUnitId();
		List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, gradeId),new TR<List<Clazz>>(){});
		Map<String,Clazz> clazzMap =EntityUtils.getMap(clazzList, "id");
		Clazz clazz;
		if(StringUtils.isBlank(classId) || !clazzMap.containsKey(classId)){
			clazz = clazzList.get(0);
			classId = clazz.getId();
		}else{
			clazz = clazzMap.get(classId);
		}
		List<GkArrangeGroupResultDto> dtolist = gkResultService.findClassDtoList(roundsId, classId);
		List<GkSubject> findGkSubjectList = gkSubjectService.findByRoundsId(roundsId,GkSubject.TEACH_TYPE1);
		Set<String> subjectIds = EntityUtils.getSet(findGkSubjectList, "subjectId");
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
		map.put("courseList", courseList);
		int num=findGkSubjectList.size();
		map.put("roundsId", roundsId);
		map.put("dtolist", dtolist);
		map.put("clazzList", clazzList);
		map.put("classId", classId);
		map.put("stuNum", dtolist.size());
		map.put("tabNum", num);
		if(StringUtils.isBlank(clazz.getTeacherId())){
			map.put("teacherName", "无");
		}else{
			map.put("teacherName", SUtils.dt(teacherRemoteService.findOneById(clazz.getTeacherId()),new TR<Teacher>(){}).getTeacherName());
		}
		return "/gkelectiveys/openClassArrange/arrangeClassResultList.ftl";
	}
	
	@RequestMapping("/openClassArrange/list/classChange/page")
	@ControllerInfo(value = "学生调班page")
	public String showClassChange(@PathVariable String roundsId,ModelMap map,HttpServletRequest request, HttpSession httpSession) {
		map.put("roundsId", roundsId);
		List<GkBatch> findBatchList = gkBatchService.findBatchList(roundsId, null);
		Map<String, GkBatch> gkBatchMap = EntityUtils.getMap(findBatchList, "teachClassId");
		Set<String> teachClassIds = EntityUtils.getSet(findBatchList, "teachClassId");
		List<GkTeachClassStore> teaClslist = gkTeachClassStoreService.findListByIds(teachClassIds.toArray(new String[0]));
		List<ClassChangeDto> classChangeDtoList = new ArrayList<ClassChangeDto>();
		ClassChangeDto classChangeDto = null;
		//组合组合班暂时屏蔽
		List<GkGroupClass> findGroupClassList = gkGroupClassService.findByRoundsId(roundsId);
		for (GkGroupClass item : findGroupClassList) {
			if(!GkGroupClass.GROUP_TYPE_1.equals(item.getGroupType())){
				//过滤不是三科组合班
				continue;
			}
			classChangeDto = new ClassChangeDto();
			classChangeDto.setClassId(item.getId());
			classChangeDto.setName(item.getGroupName());
			classChangeDto.setType(ClassChangeDto.TYPE_1);
			classChangeDto.setSubjectIds(item.getSubjectIds());
			classChangeDtoList.add(classChangeDto);
		}
		//教学班
		for (GkTeachClassStore item : teaClslist) {
			classChangeDto = new ClassChangeDto();
			classChangeDto.setClassId(item.getId());
			classChangeDto.setName(item.getClassName());
			classChangeDto.setType(ClassChangeDto.TYPE_2);
			classChangeDto.setSubjectIds(item.getSubjectId());
			classChangeDto.setBatch(gkBatchMap.get(item.getId()).getBatch());
			classChangeDto.setClassType(gkBatchMap.get(item.getId()).getClassType());
			classChangeDtoList.add(classChangeDto);
		}
		map.put("classDtoList", classChangeDtoList);
		final GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		GkSubjectArrange gkArrange = RedisUtils.getObject(GkElectveConstants.GK_ARRANGE_KEY+rounds.getSubjectArrangeId(), RedisUtils.TIME_FIVE_MINUTES, new TypeReference<GkSubjectArrange>(){}, new RedisInterface<GkSubjectArrange>(){
			@Override
			public GkSubjectArrange queryData() {
				return gkSubjectArrangeService.findArrangeById(rounds.getSubjectArrangeId());
			}
		});
        List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(gkArrange.getUnitId(),gkArrange.getGradeId()),new TR<List<Clazz>>() {});
        Set<String> clazzIds = EntityUtils.getSet(clazzList, "id");
        List<Student> stuList = new ArrayList<Student>();
        if(CollectionUtils.isNotEmpty(clazzIds)){
        	stuList = SUtils.dt(studentRemoteService.findByClassIds(clazzIds.toArray(new String[0])),new TR<List<Student>>(){});
        }
        map.put("stuList", stuList);
		return "/gkelectiveys/openClassArrange/arrangeClassChangeIndex.ftl";
	}
	@RequestMapping("/openClassArrange/list/classChange/classList/page")
	@ControllerInfo(value = "学生调班班级列表page")
	public String showChangeClassList(@PathVariable String roundsId,ModelMap map,HttpServletRequest request, HttpSession httpSession) {
		map.put("roundsId", roundsId);
		List<GkBatch> findBatchList = gkBatchService.findBatchList(roundsId, null);
		Map<String, GkBatch> gkBatchMap = EntityUtils.getMap(findBatchList, "teachClassId");
		//Set<String> teachClassIds = EntityUtils.getSet(findBatchList, "teachClassId");
		
		List<GkTeachClassStore> teaClslist=gkTeachClassStoreService.findByRoundsId(roundsId);
		//List<GkTeachClassStore> teaClslist = gkTeachClassStoreService.findByIds(teachClassIds.toArray(new String[0]));
		List<ClassChangeDto> classChangeDtoList = new ArrayList<ClassChangeDto>();
		ClassChangeDto classChangeDto = null;
		List<GkGroupClass> findGroupClassList = gkGroupClassService.findByRoundsId(roundsId);
		if(CollectionUtils.isNotEmpty(findGroupClassList)){
			Set<String> gId = EntityUtils.getSet(findGroupClassList, "id");
			Map<String, List<String>> findByGroupClassIdIn = gkGroupClassStuService.findByGroupClassIdIn(gId.toArray(new String[]{}));
			//组合班展示屏蔽
			for (GkGroupClass item : findGroupClassList) {
				if(!GkGroupClass.GROUP_TYPE_1.equals(item.getGroupType())){
					//过滤不是三科组合班
					continue;
				}
				classChangeDto = new ClassChangeDto();
				classChangeDto.setClassId(item.getId());
				classChangeDto.setName(item.getGroupName());
				classChangeDto.setType(ClassChangeDto.TYPE_1);
				classChangeDto.setSubjectIds(item.getSubjectIds());
				if(findByGroupClassIdIn.containsKey(item.getId())
						&& CollectionUtils.isNotEmpty(findByGroupClassIdIn.get(item.getId()))){
					classChangeDto.setNum(findByGroupClassIdIn.get(item.getId()).size());
				}else{
					classChangeDto.setNum(0);
				}
				classChangeDtoList.add(classChangeDto);
			}
		}
		if(CollectionUtils.isNotEmpty(teaClslist)){
			//教学班
			for (GkTeachClassStore item : teaClslist) {
				if(!gkBatchMap.containsKey(item.getId())){
					continue;
				}
				classChangeDto = new ClassChangeDto();
				classChangeDto.setClassId(item.getId());
				classChangeDto.setName(item.getClassName());
				classChangeDto.setType(ClassChangeDto.TYPE_2);
				classChangeDto.setSubjectIds(item.getSubjectId());
				classChangeDto.setBatch(gkBatchMap.get(item.getId()).getBatch());
				classChangeDto.setClassType(gkBatchMap.get(item.getId()).getClassType());
				if(CollectionUtils.isNotEmpty(item.getStuList())){
					classChangeDto.setNum(item.getStuList().size());
				}else{
					classChangeDto.setNum(0);
				}
				classChangeDtoList.add(classChangeDto);
			}
		}
		//按人数 递减
		if(CollectionUtils.isNotEmpty(classChangeDtoList)){
			Collections.sort(classChangeDtoList, new Comparator<ClassChangeDto>() {

				@Override
				public int compare(ClassChangeDto o1, ClassChangeDto o2) {
					
					if(StringUtils.equals(o1.getType(), o2.getType())){
						return o2.getNum()-o1.getNum();
					}else{
						return o2.getType().compareTo(o1.getType());
					}
				}
				
			});
		}
		map.put("classDtoList", classChangeDtoList);
		
		return "/gkelectiveys/openClassArrange/arrangeChangeClassList.ftl";
	}
	
	
	@RequestMapping("/openClassArrange/list/classChangeList/page")
	@ControllerInfo(value = "学生调班page")
	public String showClassChangeList(@PathVariable String roundsId,String classSelect,ModelMap map,HttpServletRequest request, HttpSession httpSession) {
		map.put("roundsId", roundsId);
		GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		map.put("rounds", rounds);
		//type#classId#subjectIds#batch#classType
		String[] split = classSelect.split("#");
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(split[2].split(",")),new TR<List<Course>>(){});
		Course course = new Course();
		course.setId(GkStuRemark.YSY_SUBID);
		course.setSubjectName(GkStuRemark.YSY_SUBNAME);
		courseList.add(course);
		map.put("courseList", courseList);
		Set<String> studentIds = new HashSet<String>();
		Set<String> teachClassIds = new HashSet<String>();
		if(ClassChangeDto.TYPE_1.equals(split[0])){
			//groupclass
			//2017年6月9日调整--组合班下没有教学班的概念
			Map<String, List<String>> findByGroupClassIdIn = gkGroupClassStuService.findByGroupClassIdIn(split[1]);
			if(findByGroupClassIdIn.get(split[1])!=null){
				studentIds.addAll(findByGroupClassIdIn.get(split[1]));
			}
		}else{
			//teachclass
			List<GkTeachClassStuStore> tcsList = gkTeachClassStoreService.findByClassIds(new String[]{split[1]});
			studentIds = EntityUtils.getSet(tcsList, "studentId");
			teachClassIds.add(split[1]);
		}
		List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])),new TR<List<Student>>(){});
		map.put("studentList", studentList);
		//增加语数英
		String subs = split[2]+","+GkStuRemark.YSY_SUBID;
		List<GkStuRemark> findStuScoreList = gkStuRemarkService.findStuScoreList(rounds.getSubjectArrangeId(),subs.split(","),studentIds.toArray(new String[0]));
		Map<String,List<GkStuRemark>> stuScoreMap = new HashMap<String, List<GkStuRemark>>();//key studentId
		List<GkStuRemark> linStuRemList = null;
		for (GkStuRemark item : findStuScoreList) {
			linStuRemList = stuScoreMap.get(item.getStudentId());
			if(linStuRemList == null){
				linStuRemList = new ArrayList<GkStuRemark>();
				stuScoreMap.put(item.getStudentId(), linStuRemList);
			}
			linStuRemList.add(item);
		}
		ClassChangeDetailDto classChangeDetailDto = new ClassChangeDetailDto();
		classChangeDetailDto.setClassId(split[1]);
		//总人数
		int allNum = studentList.size();
		classChangeDetailDto.setAllCountNum(allNum);
		int mCountNum = 0;
		int wCountNum = 0;
		Map<String,Double> linMap = null;
		for (Student student : studentList) {
			if(student.getSex() == null){
			}else if(student.getSex() == 1){
				mCountNum++;
			}else if(student.getSex() == 2){
				wCountNum++;
			}
			linMap = classChangeDetailDto.getStudentCourseScore().get(student.getId());
			if(linMap == null){
				linMap = new HashMap<String, Double>();
				classChangeDetailDto.getStudentCourseScore().put(student.getId(), linMap);
			}
			linStuRemList = stuScoreMap.get(student.getId());
			if(linStuRemList!=null)
			for (GkStuRemark gkStuRemark : linStuRemList) {
				linMap.put(gkStuRemark.getSubjectId(), gkStuRemark.getScore());
			}
		}
		//科目平均分
		if(allNum > 0){
			Double scoreLin;
			Double douLin;
			for(Course item : courseList){
				scoreLin = 0.0;
				for(Student stu : studentList){
					linMap = classChangeDetailDto.getStudentCourseScore().get(stu.getId());
					if(linMap!=null){
						douLin = linMap.get(item.getId());
						if(douLin!=null){
							scoreLin+=douLin;
						}
					}
				}
				classChangeDetailDto.getCourseScore().put(item.getId(), formatDouble(scoreLin/allNum));
			}
		}
		//男生人数
		classChangeDetailDto.setmCountNum(mCountNum);
		//女生人数
		classChangeDetailDto.setwCountNum(wCountNum);
		map.put("classChangeDetailDto", classChangeDetailDto);
		return "/gkelectiveys/openClassArrange/arrangeClassChangeList.ftl";
	}
	
	/**
     * 保留两位小数，四舍五入
     * @param d
     * @return
     */
	private double formatDouble(double dou) {
        return (double)Math.round(dou*100)/100;
    }
	
	@ResponseBody
    @RequestMapping("/openClassArrange/list/classChangeList/findLeftClass")
    @ControllerInfo("学生调班findLeftClass")
    public String showFindLeftClass(@PathVariable String roundsId,String searchSelectType,String searchStuCon,ModelMap map,HttpServletRequest request, HttpSession httpSession) {
		JSONArray jsonArr=new JSONArray();
		JSONObject jsonObj=null;
		if(StringUtils.isBlank(searchStuCon)){
			return jsonArr.toJSONString();
		}
//		final GkRounds rounds=gkRoundsService.findRoundById(roundsId);
//		final GkSubjectArrange gkArrange = RedisUtils.getObject(GkElectveConstants.GK_ARRANGE_KEY+rounds.getSubjectArrangeId(), RedisUtils.TIME_FIVE_MINUTES, new TypeReference<GkSubjectArrange>(){}, new RedisInterface<GkSubjectArrange>(){
//			@Override
//			public GkSubjectArrange queryData() {
//				return gkSubjectArrangeService.findArrangeById(rounds.getSubjectArrangeId());
//			}
//		});
//		 List<Clazz> clazzList = RedisUtils.getObject(GkElectveConstants.GRADE_CLASS_LIST_KEY+gkArrange.getGradeId(), RedisUtils.TIME_FIVE_MINUTES, new TypeReference<List<Clazz>>(){}, new RedisInterface<List<Clazz>>(){
//			@Override
//			public List<Clazz> queryData() {
//				return SUtils.dt(classRemoteService.findByGradeIdSortAll(gkArrange.getGradeId()),new TR<List<Clazz>>() {});
//			}
//        });
//        Set<String> classIds = EntityUtils.getSet(clazzList, "id");
//        Student searchStudent = new Student();
//        //防止注入
//		String linStr = searchStuCon.replaceAll("%", "");
//        if("2".equals(searchSelectType)){
//        	searchStudent.setStudentName(linStr);//全字匹配
//        }else{
//        	searchStudent.setStudentCode(linStr);//全字匹配
//        }
//        //可能会有重名学生
//        List<Student> studentList = Student.dt(studentRemoteService.findByIdsClaIdLikeStuCodeNames(gkArrange.getUnitId(),
//     		null, classIds.toArray(new String[0]), Json.toJSONString(searchStudent), null));
//        if(CollectionUtils.isEmpty(studentList)){
//        	return jsonArr.toJSONString();
//        }
//        Set<String> studentIds = EntityUtils.getSet(studentList, "id");
        List<GkTeachClassStuStore> teachClassStuList = gkTeachClassStoreService.findByStuIds(roundsId,new String[]{searchStuCon});
        Set<String> teachClassIds = new HashSet<String>();
        for (GkTeachClassStuStore item : teachClassStuList) {
        	teachClassIds.add(item.getGkClassId());
        }
        List<GkBatch> findByClassIds = gkBatchService.findByClassIds(roundsId,teachClassIds.toArray(new String[0]));
        Set<String> roundTeachClassIds = EntityUtils.getSet(findByClassIds, "teachClassId");
        List<GkGroupClassStu> findGkGroupClassStuList = gkGroupClassStuService.findGkGroupClassStuList(roundsId, new String[]{searchStuCon});
        Set<String> groupClsIds = EntityUtils.getSet(findGkGroupClassStuList, "groupClassId");
        List<GkGroupClass> findGkGroupClssList = gkGroupClassService.findListByIdIn(groupClsIds.toArray(new String[0]));
    	for (GkGroupClass gkGroupClass : findGkGroupClssList) {
    		if(!GkGroupClass.GROUP_TYPE_1.equals(gkGroupClass.getGroupType())){
    			//过滤不是三科组合班
    			continue;
    		}
			jsonObj = new JSONObject();
			jsonObj.put("type", ClassChangeDto.TYPE_1);
			jsonObj.put("classId", gkGroupClass.getId());
			jsonObj.put("subjectIds", gkGroupClass.getSubjectIds());
			jsonObj.put("batch", "");
			jsonObj.put("name", gkGroupClass.getGroupName());
			jsonObj.put("classType", "");
			jsonArr.add(jsonObj);
		}
        List<GkTeachClassStore> teaCls = gkTeachClassStoreService.findListByIds(roundTeachClassIds.toArray(new String[0]));
        Map<String, GkTeachClassStore> teachClassMap = EntityUtils.getMap(teaCls, "id");
        for (GkBatch gkBatch : findByClassIds) {
			jsonObj = new JSONObject();
			jsonObj.put("type", ClassChangeDto.TYPE_2);
			jsonObj.put("classId", gkBatch.getTeachClassId());
			jsonObj.put("subjectIds", teachClassMap.get(gkBatch.getTeachClassId()).getSubjectId());
			jsonObj.put("batch", gkBatch.getBatch());
			jsonObj.put("name", teachClassMap.get(gkBatch.getTeachClassId()).getClassName());
			jsonObj.put("classType", gkBatch.getClassType());
			jsonArr.add(jsonObj);
		}
		return jsonArr.toJSONString();
    }
	@ResponseBody
	@RequestMapping("/openClassArrange/list/classChangeList/findClass")
	@ControllerInfo("学生调班findClass")
	public String showFindClass(@PathVariable String roundsId,String classSelect) {
		//type#classId#subjectIds#batch#classType
		String[] split = classSelect.split("#");
		JSONArray jsonArr=new JSONArray();
		JSONObject jsonObj=null;
		if(ClassChangeDto.TYPE_1.equals(split[0])){
			//groupclass--获取同组合班
			List<GkGroupClass> findGkGroupClssList = gkGroupClassService.findGkGroupClssList(roundsId,split[2],split[1]);
			for (GkGroupClass gkGroupClass : findGkGroupClssList) {
				if(!GkGroupClass.GROUP_TYPE_1.equals(gkGroupClass.getGroupType())){
	    			//过滤不是三科组合班
	    			continue;
	    		}
				jsonObj = new JSONObject();
				jsonObj.put("type", ClassChangeDto.TYPE_1);
				jsonObj.put("classId", gkGroupClass.getId());
				jsonObj.put("subjectIds", gkGroupClass.getSubjectIds());
				jsonObj.put("batch", "");
				jsonObj.put("name", gkGroupClass.getGroupName());
				jsonObj.put("classType", "");
				jsonArr.add(jsonObj);
			}
		}else{
			//teachclass--获取同批次同科目
			List<GkBatch> findGkBatchList = gkBatchService.findGkBatchList(roundsId, Integer.valueOf(split[3]), split[4]);
			Set<String> teachClassIds = EntityUtils.getSet(findGkBatchList, "teachClassId");
			List<GkTeachClassStore> teaCls = gkTeachClassStoreService.findListByIds(teachClassIds.toArray(new String[0]));
			Map<String, GkTeachClassStore> teachClassMap = EntityUtils.getMap(teaCls, "id");
			List<GkBatch> finList = new ArrayList<GkBatch>();
			//过滤自己和科目不同的
			for (GkBatch gkBatch : findGkBatchList) {
				if(split[1].equals(gkBatch.getTeachClassId()) || !split[2].equals(teachClassMap.get(gkBatch.getTeachClassId()).getSubjectId())){
					continue;
				}
				finList.add(gkBatch);
			}
			for (GkBatch gkBatch : finList) {
				jsonObj = new JSONObject();
				jsonObj.put("type", ClassChangeDto.TYPE_2);
				jsonObj.put("classId", gkBatch.getTeachClassId());
				jsonObj.put("subjectIds", teachClassMap.get(gkBatch.getTeachClassId()).getSubjectId());
				jsonObj.put("batch", gkBatch.getBatch());
				jsonObj.put("name", teachClassMap.get(gkBatch.getTeachClassId()).getClassName());
				jsonObj.put("classType", gkBatch.getClassType());
				jsonArr.add(jsonObj);
			}
		}
		return jsonArr.toJSONString();
	}
	
	@ResponseBody
    @RequestMapping("/openClassArrange/list/classChangeList/save")
	@ControllerInfo("学生调班save")
    public String doSaveExam(@PathVariable String roundsId,
    		String leftClassSelect,String rightClassSelect,String leftAddStu,String rightAddStu) {
		try{
			if(isNowArrange(roundsId)){
				return error("正在单科开班中，不能进行操作");
			}
			GkRounds rounds=gkRoundsService.findRoundById(roundsId);
			gkClassChangeService.saveClassChange(rounds.getSubjectArrangeId(),roundsId,getLoginInfo().getUserId(),leftClassSelect, rightClassSelect, leftAddStu, rightAddStu);
		}catch(ControllerException e){
			return error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
    }
	
	@RequestMapping("/openClassArrange/list/stuSubChange/page")
	@ControllerInfo(value = "学生选课调整page")
	public String showStuSubChangeIndex(@PathVariable String roundsId,ModelMap map,HttpServletRequest request, HttpSession httpSession) {
		map.put("roundsId", roundsId);
		final GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		map.put("rounds", rounds);
		//新高考科目缓存5分钟
        List<Course> coursesList = RedisUtils.getObject(GkElectveConstants.GK_OPENSUBJECT_KEY+rounds.getSubjectArrangeId(), RedisUtils.TIME_ONE_MINUTE, new TypeReference<List<Course>>(){}, new RedisInterface<List<Course>>(){
			@Override
			public List<Course> queryData() {
				List<GkRelationship> findByTypePrimaryIdIn = gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_03,rounds.getSubjectArrangeId());
				Set<String> subjectIds = EntityUtils.getSet(findByTypePrimaryIdIn, "relationshipTargetId");
				return SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
			}
		});
        map.put("coursesList", coursesList);
        GkSubjectArrange gkArrange = RedisUtils.getObject(GkElectveConstants.GK_ARRANGE_KEY+rounds.getSubjectArrangeId(), RedisUtils.TIME_FIVE_MINUTES, new TypeReference<GkSubjectArrange>(){}, new RedisInterface<GkSubjectArrange>(){
			@Override
			public GkSubjectArrange queryData() {
				return gkSubjectArrangeService.findArrangeById(rounds.getSubjectArrangeId());
			}
		});
        List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(gkArrange.getUnitId(),gkArrange.getGradeId()),new TR<List<Clazz>>() {});
        Set<String> clazzIds = EntityUtils.getSet(clazzList, "id");
        List<Student> stuList = new ArrayList<Student>();
        if(CollectionUtils.isNotEmpty(clazzIds)){
        	stuList = SUtils.dt(studentRemoteService.findByClassIds(clazzIds.toArray(new String[0])),new TR<List<Student>>(){});
        }
        map.put("stuList", stuList);
        
        List<ClassChangeDto> classChangeDtoList = new ArrayList<ClassChangeDto>();
        ClassChangeDto dto;
        //组合班
        List<GkGroupClass> gkGroupList = gkGroupClassService.findByRoundsId(roundsId);
        if(CollectionUtils.isNotEmpty(gkGroupList)){
        	for(GkGroupClass g:gkGroupList){
        		dto=new ClassChangeDto();
        		dto.setClassId(g.getId());
        		dto.setType(ClassChangeDto.TYPE_1);
        		dto.setName(g.getGroupName());
        		classChangeDtoList.add(dto);
        	}
        }
        //教学班
		List<GkTeachClassStore> teaClslist=gkTeachClassStoreService.findByRoundsId(roundsId);
		if(CollectionUtils.isNotEmpty(teaClslist)){
			for(GkTeachClassStore g:teaClslist){
				dto=new ClassChangeDto();
        		dto.setClassId(g.getId());
        		dto.setType(ClassChangeDto.TYPE_2);
        		dto.setName(g.getClassName());
        		classChangeDtoList.add(dto);
			}
		}
		
		map.put("classDtoList", classChangeDtoList);

		return "/gkelectiveys/openClassArrange/arrangeStuChangeIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/openClassArrange/list/stuSubChange/findStudent")
	@ControllerInfo("学生选课调整findStudent")
	public String showFindStudent(@PathVariable String roundsId,String stuId,ModelMap map,HttpServletRequest request, HttpSession httpSession) {
		JSONObject json = new JSONObject();
		final GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		map.put("rounds", rounds);
		Student student = SUtils.dt(studentRemoteService.findOneById(stuId),new TR<Student>(){});
		if(student == null){
			json.put("success", false);
			return json.toJSONString();
		}
		json.put("success", true);
		
		List<String> findCoursesByStuId = gkResultService.findCoursesByStuId(student.getId(), rounds.getSubjectArrangeId());
		if(CollectionUtils.isNotEmpty(findCoursesByStuId)){
			String subIds = StringUtils.join(findCoursesByStuId.toArray(new String[0]), ",");
			json.put("stuSubIds", subIds);
		}else{
			json.put("stuSubIds", "");
		}
		return json.toJSONString();
	}
	
	
	@ResponseBody
    @RequestMapping("/openClassArrange/list/stuSubChange/findStuByTeachClassId")
    @ControllerInfo("学生调班findStuByTeachClassId")
    public String findStuByTeachClassId(@PathVariable String roundsId,String searchClassCon,ModelMap map,HttpServletRequest request, HttpSession httpSession) {
		JSONArray jsonArr=new JSONArray();
		JSONObject jsonObj=null;
		if(StringUtils.isBlank(searchClassCon)){
			return jsonArr.toJSONString();
		}
		String[] arr = searchClassCon.split("#");
		Set<String> studentIds=new HashSet<String>();
		if(ClassChangeDto.TYPE_2.equals(arr[1])){
			List<GkTeachClassStuStore> tcsList = gkTeachClassStoreService.findByClassIds(new String[]{arr[0]});
			if(CollectionUtils.isNotEmpty(tcsList)){
				studentIds = EntityUtils.getSet(tcsList, "studentId");
			}
			
		}else{
			GkGroupClass gg = gkGroupClassService.findById(arr[0]);
			if(gg!=null && CollectionUtils.isNotEmpty(gg.getStuIdList())){
				studentIds.addAll(gg.getStuIdList());
			}
		}
		if(studentIds.size()>0){
			List<Student> stuList = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])),new TR<List<Student>>(){});
			if(CollectionUtils.isEmpty(stuList)){
				return jsonArr.toJSONString();
			}
			for(Student s:stuList){
				jsonObj = new JSONObject();
				jsonObj.put("id", s.getId());
				jsonObj.put("studentCode", s.getStudentCode()==null?"无学号":s.getStudentCode());
				jsonObj.put("studentName", s.getStudentName()==null?"":s.getStudentName());
				jsonArr.add(jsonObj);
			}
		}
		return jsonArr.toJSONString();
	}
	
	@RequestMapping("/openClassArrange/list/stuSubChange/showList")
	@ControllerInfo(value = "学生选课调整page")
	public String showStuSubChangeShowList(@PathVariable String roundsId,String searchSubjectIds,String stuId,ModelMap map,HttpServletRequest request, HttpSession httpSession) {
		map.put("roundsId", roundsId);
		GkRounds rounds=gkRoundsService.findRoundById(roundsId);
		map.put("rounds", rounds);
		//三科的组合班形式
		List<GkGroupClass> findGkGroupClassList = gkGroupClassService.findGkGroupClassList(roundsId,searchSubjectIds.split(","));
//		Map<String,GkGroupClass> groupClaMap = EntityUtils.getMap(findGkGroupClassList, "id");
//		Set<String> groupClassIds = EntityUtils.getSet(findGkGroupClassList, "id");
		
		List<GkGroupClassStu> findGkGroupClassStuList = gkGroupClassStuService.findGkGroupClassStuList(roundsId, null);
		Map<String,Integer> groupClassStuNumMap = new HashMap<String, Integer>();
		Integer stuNum;
		for (GkGroupClassStu gkGroupClassStu : findGkGroupClassStuList) {
			stuNum = groupClassStuNumMap.get(gkGroupClassStu.getGroupClassId());
			if(stuNum == null){
				groupClassStuNumMap.put(gkGroupClassStu.getGroupClassId(), 1);
			}else{
				groupClassStuNumMap.put(gkGroupClassStu.getGroupClassId(), stuNum+1);
			}
		}
		
		for (GkGroupClass gkGroupClass : findGkGroupClassList) {
			stuNum = groupClassStuNumMap.get(gkGroupClass.getId());
			if(stuNum!=null){
				gkGroupClass.setNumber(stuNum);
			}
		}
		
//		Map<String, List<String>> groupClaStu = gkGroupClassStuService.findByGroupClassIdIn(groupClassIds.toArray(new String[0]));
//		List<String> linStrList = null;
//		GkGroupClass groupClass = null;
//		for(Map.Entry<String, List<String>> entry : groupClaStu.entrySet()){
//			groupClass = groupClaMap.get(entry.getKey());
//			linStrList = entry.getValue();
//			groupClass.setNumber(linStrList.size());
//		}
		map.put("finList", findGkGroupClassList);
		//单科班形式，如果是开行政班还要显示所在混合班或者2+X班级
		String[] split = searchSubjectIds.split(",");
		Set<String> subIds = new HashSet<String>();
		for (String string : split) {
			subIds.add(string);
		}
		//获取同样是选三门课的学生，找这些学生所在的班级
		List<StudentSubjectDto> findAllStudentSubjectDto = gkResultService.findAllStudentSubjectDto(rounds.getSubjectArrangeId(), subIds);
		List<StuSubChangeDto> dtoList = new ArrayList<StuSubChangeDto>();
		if(CollectionUtils.isNotEmpty(findAllStudentSubjectDto)){
			Set<String> stuIds = EntityUtils.getSet(findAllStudentSubjectDto, "stuId");
			List<GkBatch> findBatchList = gkBatchService.findBatchList(roundsId, null);
			//教学班所在批次
			Map<String, GkBatch> batMap = EntityUtils.getMap(findBatchList, "teachClassId");
			Set<String> teachClassIds = EntityUtils.getSet(findBatchList, "teachClassId");
			List<GkTeachClassStore> teaClslist = gkTeachClassStoreService.findListByIds(teachClassIds.toArray(new String[0]));
			List<GkTeachClassStuStore> teachClassStus = gkTeachClassStoreService.findByClassIds(teachClassIds.toArray(new String[0]));
			Map<String,Integer> teachClassStuNumMap = new HashMap<String, Integer>();//teachClassId 教学班人数map
			for (GkTeachClassStuStore item : teachClassStus) {
				Integer integer = teachClassStuNumMap.get(item.getGkClassId());
				if(integer == null){
					teachClassStuNumMap.put(item.getGkClassId(), 1);
				}else{
					teachClassStuNumMap.put(item.getGkClassId(), integer+1);
				}
			}
			Map<String, GkTeachClassStore> teaClsMap = EntityUtils.getMap(teaClslist, "id");
			
			//学生所在组合班
			Map<String, GkGroupClassStu> groClaStuMap = EntityUtils.getMap(findGkGroupClassStuList, "studentId");
			Set<String> groClaIds = EntityUtils.getSet(findGkGroupClassStuList, "groupClassId");
			List<GkGroupClass> findByIdIn = gkGroupClassService.findListByIdIn(groClaIds.toArray(new String[0]));
			Map<String,GkGroupClass> groClaMap = EntityUtils.getMap(findByIdIn, "id");
			//学生所在教学班
			Map<String, List<String>> stuTeachClassIds = gkTeachClassStoreService.findMapWithStuIdByClassIds(teachClassIds.toArray(new String[0]));
			GkBatch gkBatch = null;
			GkTeachClassStore teachClass = null;
			List<GkBatch> linBatList = null;
			Set<String> linStrSet = new HashSet<String>();
			StringBuffer sbf = null;
			StuSubChangeDto stuSubChangeDto = null;
			GkGroupClassStu groClaStuLin = null;
			GkGroupClass groClaLin = null;
			List<String> linStrList = null;
			for (String stuIdItem : stuIds) {
				//找每个学生所在班级，可能情况：3+0，2+X，全单科（混合班）
				linStrList = stuTeachClassIds.get(stuIdItem);
				if(linStrList!=null && linStrList.size() > 0){//排除3+0
					linBatList = new ArrayList<GkBatch>();
					for (String tcsId : linStrList) {
						gkBatch = batMap.get(tcsId);
						linBatList.add(gkBatch);
					}
					Collections.sort(linBatList, new Comparator<GkBatch>() {
						public int compare(GkBatch arg0, GkBatch arg1) {
							return arg0.getBatch()-arg1.getBatch();
						}
					});
					stuSubChangeDto = new StuSubChangeDto(); 
					groClaStuLin = groClaStuMap.get(stuIdItem);
					if(groClaStuLin != null){
						stuSubChangeDto.setGroupClassId(groClaStuLin.getGroupClassId());
						groClaLin = groClaMap.get(groClaStuLin.getGroupClassId());
						if(groClaLin!=null){
							stuNum = groupClassStuNumMap.get(groClaLin.getId());
							stuSubChangeDto.setGroupClassName(groClaLin.getGroupName()+"<br>"+(stuNum!=null?stuNum:0)+"人");
						}else{
							stuSubChangeDto.setGroupClassName("未找到");
						}
					}
					sbf = new StringBuffer();
					for (GkBatch item : linBatList) {
						teachClass = teaClsMap.get(item.getTeachClassId());
						if(StringUtils.isNotBlank(sbf)){
							sbf.append(","+teachClass.getId());
						}else{
							sbf.append(teachClass.getId());
						}
						stuSubChangeDto.getClassIdsSet().add(teachClass.getId());
						stuSubChangeDto.getClassNames().add(BaseConstants.PC_KC+item.getBatch()+"<br>"+teachClass.getClassName()+"<br>"+(teachClassStuNumMap.get(teachClass.getId())!=null?teachClassStuNumMap.get(teachClass.getId()):0)+"人");
					}
					//排除重复
					if(!linStrSet.contains(sbf.toString())){
						stuSubChangeDto.setClassIds(sbf.toString());
						linStrSet.add(sbf.toString());
						dtoList.add(stuSubChangeDto);
					}
				}
			}
		}
		
		if(CollectionUtils.isNotEmpty(dtoList)){
			Collections.sort(dtoList, new Comparator<StuSubChangeDto>() {

				@Override
				public int compare(StuSubChangeDto o1, StuSubChangeDto o2) {
					if(o1.getGroupClassId()==null){
						return -1;
					}
					if(o2.getGroupClassId()==null){
						return -1;
					}
					return o1.getGroupClassId().compareTo(o2.getGroupClassId());
				}
			});
		}
		map.put("dtolist", dtoList);
		return "/gkelectiveys/openClassArrange/arrangeStuChangeList.ftl";
	}
	
	@ResponseBody
    @RequestMapping("/openClassArrange/list/stuSubChange/save")
	@ControllerInfo("学生选课调整save")
    public String doSaveStuSubChangeSave(@PathVariable String roundsId,
    		String stuId,String searchClassType,String chosenClassIds,String searchSubjectIds) {
		try{
			GkRounds rounds=gkRoundsService.findRoundById(roundsId);
			gkClassChangeService.saveStuSubChange(getLoginInfo().getUnitId(), rounds.getSubjectArrangeId(), roundsId, stuId, searchClassType, chosenClassIds, searchSubjectIds);
		}catch(ControllerException e){
			return error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
    }	
	
	private boolean isNowArrange(String roundsId){
		if(ArrangeSingleSolver.isSolverIdRunning(roundsId+"A") || ArrangeSingleSolver.isSolverIdRunning(roundsId+"B")){
			return true;
		}else{
			return false;
		}
	}
}
