package net.zdsoft.scoremanage.data.action;

import com.alibaba.fastjson.JSON;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.constant.HwConstants;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dto.HwPlanSetDto;
import net.zdsoft.scoremanage.data.dto.SubjectDto;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.HwPlan;
import net.zdsoft.scoremanage.data.entity.HwPlanSet;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.data.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 杭外 科目分数统计
 */
@Controller
@RequestMapping("/scoremanage/planSet")
public class HwPlanSetAction extends BaseAction{
	private static final Logger logger = LoggerFactory
			.getLogger(HwPlanSetAction.class);

	@Autowired
	private HwPlanService hwPlanService;
	@Autowired
	private HwPlanSetService hwPlanSetService;
	@Autowired
	private HwStatisExService hwStatisExService;
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private SubjectInfoService subjectInfoService;
	@Autowired
	private GradeRemoteService gradeRemoteService;


	@RequestMapping("/index/page")
	public String index(){
		return "/scoremanage/planSet/planSetIndex.ftl";
	}

	/**
	 *  0 在校生  1: 离校和毕业
	 * @param type
	 * @return
	 */
	@RequestMapping("/getAllGrade")
	@ResponseBody
	public String getAllGrade(Integer type){
		String unitId = getLoginInfo().getUnitId();
		List<Grade> gradeList=new ArrayList<>();
		Json result = new Json();
		List<Json> showList=new ArrayList<>();
		if (type==Student.STUDENT_LEAVE){
			//离校
			gradeList = SUtils.dt(gradeRemoteService.findListBy(new String[]{"schoolId", "isGraduate", "isDeleted"}, new String[]{unitId, String.valueOf(Grade.GRADUATED), "0"}), Grade.class);
		}else {
			//在校
			gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId), Grade.class);
		}
		if (CollectionUtils.isEmpty(gradeList)){
			result.put("code","-1");
			return JSON.toJSONString(result);
		}
		List<HwPlan> planList = hwPlanService.findLastPlanListByGradeIdIn(EntityUtils.getArray(gradeList, x -> x.getId(), String[]::new));
		if (CollectionUtils.isEmpty(planList)){
			result.put("code","-1");
			return JSON.toJSONString(result);
		}
		//key1: gradeId    key2: acadyear    set: semester
		Map<String, Map<String, Set<String>>> map = planList.stream().collect(Collectors.groupingBy(x -> x.getGradeId(), Collectors.groupingBy(x -> x.getAcadyear(), Collectors.mapping(x -> x.getSemester(), Collectors.toSet()))));

		Set<String> gradeSet = EntityUtils.getSet(planList, x -> x.getGradeId());
		showList=gradeList.stream().filter(x->gradeSet.contains(x.getId())).map(x->{
			Json json = new Json();
			json.put("gradeId",x.getId());
			String name="";
			if(type==Student.STUDENT_LEAVE){
				name = ""+(Integer.parseInt(x.getOpenAcadyear().substring(0, 4)) + x.getSchoolingLength());
                if(BaseConstants.SECTION_HIGH_SCHOOL.equals(x.getSection())){
                    name+="届(高中)";
                }else if(BaseConstants.SECTION_JUNIOR.equals(x.getSection())){
                    name+="届(初中)";
                }else if(BaseConstants.SECTION_PRIMARY.equals(x.getSection())){
                    name+="届(小学)";
                }else if(BaseConstants.SECTION_COLLEGE.equals(x.getSection())){
                    name+="届(剑桥高中)";
                }
			}else{
				name=x.getGradeName();
			}

			json.put("name",name);
			return json;
		}).collect(Collectors.toList());
		result.put("gradeList",showList);
		result.put("map",map);
		result.put("code","00");
		return JSON.toJSONString(result);
	}

	/**
	 *  获得方案列表,包含所有的科目设置
	 * @param gradeId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	@GetMapping("/getPlanList")
	@ResponseBody
	public String getPlanList(String gradeId,String acadyear,String semester){
		if(StringUtils.isBlank(gradeId)){
			return error("参数错误");
		}
		Json data = new Json();
		List<Json> result;
		String unitId = getLoginInfo().getUnitId();
		List<HwPlan> planList = hwPlanService.findListByGradeIdAcadyearAndSemester(unitId, gradeId, acadyear, semester);
		if (CollectionUtils.isEmpty(planList)){
			data.put("code","-1");
			return JSON.toJSONString(data);
		}
		String[] planIds = EntityUtils.getArray(planList, x -> x.getId(), String[]::new);
		String[] examIds = EntityUtils.getArray(planList, x -> x.getExamId(), String[]::new);
		Map<String, String> planIdAndIdMap = EntityUtils.getMap(planList, x -> x.getExamId(), x -> x.getId());


		List<SubjectDto> subList = hwStatisExService.findObjectListByPlanIdIn(unitId,planIds);
        if (CollectionUtils.isEmpty(subList)){
            data.put("code","-1");
            return JSON.toJSONString(data);
        }


		//是否等第  ScoreDataConstants.ACHI_GRADE
		List<SubjectInfo> subjectInfoList= subjectInfoService.findByExamIdIn(unitId, examIds);
		if (CollectionUtils.isEmpty(subjectInfoList)){
			data.put("code","-1");
			return JSON.toJSONString(data);
		}
		Map<String, String> subjectInputTypeMap=new HashMap<>();
		for (SubjectInfo x : subjectInfoList) {
			subjectInputTypeMap.put(planIdAndIdMap.get(x.getExamId())+"_"+x.getSubjectId(),x.getInputType());
		}
		Map<String, List<String>> inputTypeListMap = EntityUtils.getListMap(subjectInfoList, x -> x.getExamId(), x -> x.getInputType());

		subList.stream().map(x->x.getHwPlanId()).distinct().forEach(x->{
			List<String> list = inputTypeListMap.get(x);
			if (CollectionUtils.isNotEmpty(list)){
				if (list.contains(ScoreDataConstants.ACHI_SCORE)){
					subjectInputTypeMap.put(HwConstants.SUM_SCORE_RANK,ScoreDataConstants.ACHI_SCORE);
				}
			}
		});

		subList.addAll(subList.stream().map(x->x.getHwPlanId()).distinct().map(x-> SubjectDto.getSumAndRank(x)).collect(Collectors.toList()));

        //已经设置了的
        List<HwPlanSet> planSetList = hwPlanSetService.findListByIn("hwPlanId", planIds);
        Set<String> planSet = EntityUtils.getSet(planSetList, x -> x.getHwPlanId());

        //成绩类型
        Map<String, String> scoreTypeMap = planSetList.stream().collect(Collectors.toMap(x -> x.getHwPlanId() + "_" + x.getObjKey(), x -> x.getObjVal()));
        Map<String, List<Json>> planMap = subList.stream().collect(Collectors.groupingBy(x -> x.getHwPlanId(), Collectors.mapping(x -> {
            Json json = new Json();
            json.put("subId", x.getObjKey());
            json.put("subName", x.getSubjectName());
            json.put("inputType",StringUtils.defaultString(subjectInputTypeMap.get(x.getHwPlanId()+"_"+x.getObjKey()),ScoreDataConstants.ACHI_SCORE));
            json.put("planId", x.getHwPlanId());
            String type = scoreTypeMap.get(x.getHwPlanId() + "_" + x.getObjKey());
            if (StringUtils.isNotBlank(type)) {
                json.put("type", type);
                json.put("isUsing", 1);
            } else {
                json.put("type", HwConstants.SCORE_TYPE_EXAM);
                json.put("isUsing", 0);
            }
            return json;
        }, Collectors.toList())));

        result= planList.stream().map(x -> {
			Json json = new Json();
			json.put("planId", x.getId());
			json.put("examId", x.getExamId());
			json.put("examName", x.getExamName());
			json.put("isUsing", planSet.contains(x.getId()) ? 1 : 0);
			if(CollectionUtils.isEmpty(planMap.get(x.getId()))){
				json.put("subList",new ArrayList<>());
			}else{
				json.put("subList",planMap.get(x.getId()));
			}
			return json;
		}).collect(Collectors.toList());
		data.put("code","00");
		data.put("result",result);
		return data.toJSONString();
	}


	@PostMapping("/saveSetting")
	@ResponseBody
	public String saveSetting(@RequestBody @Valid HwPlanSetDto setDto, Errors errors){
		if(errors.hasFieldErrors()){
			return error(errors.getFieldError().getDefaultMessage());
		}
		try {
			List<SubjectDto> subList = setDto.getSubList();
            Map<String, HwPlanSet> oldData=new HashMap<>();
            Set<String> set = EntityUtils.getSet(subList, x -> x.getHwPlanId());
			List<String> delIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(set)) {
                List<HwPlanSet> setList = hwPlanSetService.findListByIn("hwPlanId", set.toArray(new String[0]));
                if (CollectionUtils.isNotEmpty(setList)) {
                    oldData = EntityUtils.getMap(setList, x -> x.getHwPlanId() + "_" + x.getObjKey(), x -> x);
                    delIds=EntityUtils.getList(setList,x->x.getId());
                }
            }

            List<HwPlanSet> saveList = new ArrayList<>();
			for (SubjectDto dto : subList) {
				String key=dto.getHwPlanId()+"_"+dto.getObjKey();
				HwPlanSet entity =oldData.get(key);
				if (entity==null){
					entity=new HwPlanSet();
					entity.setId(UuidUtils.generateUuid());
					entity.setUnitId(getLoginInfo().getUnitId());
					entity.setCreationTime(new Date());
					entity.setHwPlanId(dto.getHwPlanId());
					entity.setObjKey(dto.getObjKey());
				}else{
				    delIds.remove(entity.getId());
                }
				entity.setModifyTime(new Date());
				entity.setOperator(getLoginInfo().getRealName());
				entity.setObjVal(dto.getScoreType());
				saveList.add(entity);
			}
            hwPlanSetService.saveAndDelete(saveList,delIds.toArray(new String[0]),setDto.getDelPlanIds());
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		return success("保存成功");
	}
}
