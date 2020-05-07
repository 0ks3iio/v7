package net.zdsoft.scoremanage.data.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.serializer.SerializerFeature;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.scoremanage.data.dto.ExamInfoSearchDto;
import net.zdsoft.scoremanage.data.dto.ScoreConversionSaveDto;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.ScoreConversion;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.ScoreConversionService;

@Controller
@RequestMapping("/scoremanage")
public class ScoreConversionAction extends BaseAction{
	
	@Autowired
	private SemesterRemoteService semesterService;
	@Autowired
	private ScoreConversionService scoreConversionService;
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	
	@RequestMapping("/hierarchy/index/page")
    @ControllerInfo(value = "等级比例设置index")
    public String showIndex(ModelMap map, HttpSession httpSession) {
		List<String> acadyearList = SUtils.dt(semesterService.findAcadeyearList(), new TR<List<String>>(){}); 
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId=loginInfo.getUnitId();
		map.put("unitId", unitId);
		Semester semester = SUtils.dc(semesterService.getCurrentSemester(2), Semester.class);
		String acadyearSearch = semester.getAcadyear();
		String semesterSearch = semester.getSemester()+"";
		map.put("acadyearList", acadyearList);
		map.put("acadyearSearch", acadyearSearch);
		map.put("semesterSearch", semesterSearch);
		Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
		map.put("unitClass", unit.getUnitClass());
		return "/scoremanage/hierarchy/hierarchyInfoIndex.ftl";
	}
	
	
	@ResponseBody
	@RequestMapping("/hierarchy/findList")
	@ControllerInfo("获取考试数据")
	public String showFindExamInfoList(ExamInfoSearchDto searchDto,HttpSession httpSession) {
		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId = loginInfo.getUnitId();
		List<ExamInfo> examList = new ArrayList<ExamInfo>();
		examList = examInfoService.findExamInfoList(unitId,searchDto,null);
		return Json.toJSONString(examList,SerializerFeature.DisableCircularReferenceDetect);
	}
	
	@RequestMapping("/hierarchy/showList/page")
	@ControllerInfo(value = "等级比例设置List")
	public String showList(String examId,ModelMap map, HttpSession httpSession) {
		List<ScoreConversion> scoreConversionList=new ArrayList<ScoreConversion>();
		if(StringUtils.isNotBlank(examId)){
			scoreConversionList = scoreConversionService.findListByExamId(examId,true);
		}
		map.put("scoreConversionList", scoreConversionList);
		map.put("examId", examId);
		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId = loginInfo.getUnitId();
		//权限
		if(StringUtils.isNotBlank(examId)){
			ExamInfo exam = examInfoService.findOne(examId);
			if(exam==null){
				map.put("canEdit", false);
			}else{
				if(exam.getUnitId().equals(unitId)){
					map.put("canEdit", true);
				}else{
					map.put("canEdit", false);
				}
			}
		}else{
			map.put("canEdit", false);
		}
		return "/scoremanage/hierarchy/hierarchyInfoList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/hierarchy/save")
	@ControllerInfo("等级比例设置save")
	public String doSaveList(ScoreConversionSaveDto saveDto) {
		try{
			List<ScoreConversion> saveList = new ArrayList<ScoreConversion>();
			List<ScoreConversion> scoreConversionList = saveDto.getScoreConversionList();
			Set<Float> scoreSet=new HashSet<Float>(); 
			if(CollectionUtils.isEmpty(scoreConversionList)){
				return error("没有需要保存的数据！");
			}
			float count=0.0f;
			for (ScoreConversion item : scoreConversionList) {
				if(item==null){
					continue;
				}
				if(item.getBalance()==null || item.getScore()==null){
					continue;
				}
				if(item.getBalance()>100){
					return error("等级比例不能大于100%！");
				}
				if(scoreSet.contains(item.getScore())){
					return error(item.getScore()+"分比例有多条记录！");
				}
				scoreSet.add(item.getScore());
				count=count+item.getBalance();
				saveList.add(item);
			}
			if(CollectionUtils.isEmpty(saveList)){
				return error("没有需要保存的数据！");
			}
			if(count>100){
				return error("等级比例总和不能大于100%,目前总和为"+count+"%");
			}
			if(CollectionUtils.isNotEmpty(saveList)){
				scoreConversionService.saveAll(saveList.toArray(new ScoreConversion[0]),new String[]{saveList.get(0).getExamId()});
			}else{
				return error("没有需要保存的数据！");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	
	@RequestMapping("/hierarchy/copySet/page")
	@ControllerInfo(value = "复用")
	public String copySetIndex(String examId,ModelMap map) {
		map.put("examId", examId);
		List<String> acadyearList = SUtils.dt(semesterService.findAcadeyearList(), new TR<List<String>>(){}); 
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
		Semester semester = SUtils.dc(semesterService.getCurrentSemester(2), Semester.class);
		String acadyearSearch = semester.getAcadyear();
		String semesterSearch = semester.getSemester()+"";
		map.put("acadyearList", acadyearList);
		map.put("acadyear", acadyearSearch);
		map.put("semester", semesterSearch);
		return "/scoremanage/hierarchy/hierarchyCopySetIndex.ftl";
	}
	
	
	@ResponseBody
	@RequestMapping("/hierarchy/findList2")
	@ControllerInfo("获取7选3本单位设置的考试数据")
	public String showFindExamInfoList2(ExamInfoSearchDto searchDto,HttpSession httpSession) {
		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId = loginInfo.getUnitId();
		List<ExamInfo> examList = new ArrayList<ExamInfo>();
		searchDto.setSearchType("1");//本单位
		//本单位创建数据
		examList = examInfoService.findExamInfoList(unitId,searchDto,null);
		return Json.toJSONString(examList,SerializerFeature.DisableCircularReferenceDetect);
	}
	
	@ResponseBody
	@RequestMapping("/hierarchy/copySetSave")
	@ControllerInfo("等级比例设置save")
	//copyExamIds 前端传过来的是111,1111(字符串型)
	public String copySetSave(String[] copyExamIds,String examId) {
		try{
			if(StringUtils.isBlank(examId) ||copyExamIds==null || copyExamIds.length<=0){
				return error("没有需要保存的数据！");
			}
			List<ScoreConversion> scoreConversionList = scoreConversionService.findListByExamId(examId,false);
			
			List<ScoreConversion> insertList=new ArrayList<ScoreConversion>();
			ScoreConversion scoreConversion=null;
			if(CollectionUtils.isNotEmpty(scoreConversionList)){
				for(String e:copyExamIds){
					for (ScoreConversion item : scoreConversionList) {
						scoreConversion=new ScoreConversion();
						scoreConversion.setBalance(item.getBalance());
						scoreConversion.setExamId(e);
						scoreConversion.setScore(item.getScore());
						insertList.add(scoreConversion);
					}
				}
			}
			scoreConversionService.saveAll(insertList.toArray(new ScoreConversion[0]),copyExamIds);
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	
	@ResponseBody
	@RequestMapping("/hierarchy/delete")
	@ControllerInfo("等级比例设置delete")
	public String doDeleteCourseInfo(String[] id) {
		try{
			scoreConversionService.deleteAllByIds(id);
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
}
