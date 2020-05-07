package net.zdsoft.comprehensive.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.comprehensive.constant.CompreStatisticsConstants;
import net.zdsoft.comprehensive.dto.CompreParameterInfoDto;
import net.zdsoft.comprehensive.entity.CompreInfo;
import net.zdsoft.comprehensive.entity.CompreParameterInfo;
import net.zdsoft.comprehensive.entity.CompreScore;
import net.zdsoft.comprehensive.entity.CompreStatistics;
import net.zdsoft.comprehensive.service.CompreInfoService;
import net.zdsoft.comprehensive.service.CompreParamInfoService;
import net.zdsoft.comprehensive.service.CompreScoreService;
import net.zdsoft.comprehensive.service.CompreStatisticsService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

@Controller
@RequestMapping("/comprehensive")
public class CompreStatisticsAction extends BaseAction{
	private Logger logger = Logger.getLogger(CompreStatisticsAction.class);
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private CompreInfoService compreInfoService;
	@Autowired
	private CompreScoreService compreScoreService;
	@Autowired
	private CompreParamInfoService compreParamInfoService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private CompreStatisticsService compreStatisticsService;
	
	
	@RequestMapping("/qualityScoreStat/index/page")
    @ControllerInfo(value = "综合素质统计")
	public String showStatIndex(ModelMap map){
		//年级id
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(getLoginInfo().getUnitId()), new TR<List<Grade>>(){});
		//学年学期
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        map.put("gradeList", gradeList);
        return "/comprehensive/quality/qualityScoreIndex.ftl";
	}

	
	/**

	 * 初始化所有学生4种成绩
	 * @param insertList
	 * @param statisticsBystudentId
	 * @param studentList
	 */
	private void toMakeStudentScore(String acadyear,String semester,List<CompreStatistics> insertList,Map<String,CompreStatistics> statisticsBystudentId,List<Student> studentList){
		CompreStatistics dto;
		String[] types=new String[] {CompreStatisticsConstants.TYPE_OVERALL,CompreStatisticsConstants.TYPE_ENGLISH,CompreStatisticsConstants.TYPE_ENG_SPEAK,CompreStatisticsConstants.TYPE_GYM};
		for(Student s:studentList){
			for(String type:types){
				dto=toMakeOne(acadyear, semester, s, type);
				statisticsBystudentId.put(s.getId()+"_"+dto.getType(), dto);
				insertList.add(dto);
			}
		}
	}
	
	private CompreStatistics toMakeOne(String acadyear,String semester,Student student,String type){
		CompreStatistics dto=new CompreStatistics();
		dto.setId(UuidUtils.generateUuid());
		dto.setUnitId(student.getSchoolId());
		dto.setAcadyear(acadyear);
		dto.setClassId(student.getClassId());
		dto.setSemester(semester);
		dto.setStudentId(student.getId());
		dto.setType(type);
		return dto;
	}
	
	private void toMakeRankStatByType(String type,List<CompreScore> scorelist,
			Map<String,CompreStatistics> statisticsBystudentId,Set<String> allStuId,Map<String,CompreParameterInfoDto> parameterDto){
		Set<String> arrangeStuIds=new HashSet<String>();//有成绩学生
		CompreStatistics dto;
		for(CompreScore score:scorelist){
			String key=score.getStudentId()+"_"+type;
			if(!statisticsBystudentId.containsKey(key)){
				continue;
			}
			dto=statisticsBystudentId.get(key);
			dto.setScore(score.getToScore());
			dto.setRanking(score.getRanking());
			Float splitScore=0f;
			for(String mapkey:parameterDto.keySet()){
				CompreParameterInfoDto parmDto = parameterDto.get(mapkey);
				if(dto.getRanking()==null){
					splitScore=null;
					break;
				}else{
					if(parmDto.getLowNum()<dto.getRanking() && dto.getRanking()<=parmDto.getUpNum()){
						if(splitScore<parmDto.getScore()){
							splitScore=parmDto.getScore();
						}
					}
				}
			}
			dto.setSplit(splitScore);
			arrangeStuIds.add(score.getStudentId());
		}
		Set<String> allStuIds = new HashSet<String>();
		allStuIds.addAll(allStuId);
		allStuIds.removeAll(arrangeStuIds);
		if(allStuIds.size()>0){
			//剩余学生没有成绩 默认排名：arrangeStuIds+1
			int ranking = arrangeStuIds.size()+1;
			for(String ss:allStuIds){
				String key=ss+"_"+CompreStatisticsConstants.TYPE_OVERALL;
				if(!statisticsBystudentId.containsKey(key)){
					continue;
				}
				dto=statisticsBystudentId.get(key);
				dto.setScore(0f);//默认0
				dto.setRanking(ranking);
				float splitScore=0;
				for(String mapkey:parameterDto.keySet()){
					CompreParameterInfoDto parmDto = parameterDto.get(mapkey);
					if(parmDto.getLowNum()<dto.getRanking() && dto.getRanking()>=parmDto.getUpNum()){
						if(splitScore>parmDto.getScore()){
							splitScore=parmDto.getScore();
						}
					}
				}
				dto.setSplit(splitScore);
			}
		}
	}
	
	@ResponseBody
	@RequestMapping("/qualityScoreStat/scoreStat")
	@ControllerInfo(value = "综合素质统计数据")
	public String scoreStat(final String gradeId,final String acadyear,final String semester,ModelMap map){
		
		final String unitId = getLoginInfo().getUnitId();
		final String key = unitId+"_"+gradeId+"_"+acadyear+"_"+semester;
		final String messKey = key+"_mess";
		
		if(RedisUtils.get(key)==null){
			//是不是已经有统计结果
			if(checkStat(unitId, acadyear, semester, gradeId)){
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("type", "result");
				jsonObject.put("mess", "已经有统计结果！");
				return  JSON.toJSONString(jsonObject);
			}
			RedisUtils.set(key,"start");
			RedisUtils.set(messKey, "开始计算！");
		}else{		
			JSONObject jsonObject=new JSONObject();;
			jsonObject.put("type", RedisUtils.get(key));
			jsonObject.put("mess",RedisUtils.get(messKey));
			if("success".equals(RedisUtils.get(key)) || "error".equals(RedisUtils.get(key))){
				RedisUtils.del(key);
				RedisUtils.del(messKey);
			}
			return JSON.toJSONString(jsonObject);
		}
				
		new Thread(new Runnable(){  
		     public void run(){  
		    	logger.info("综合素质开始计算...");
		    	RedisUtils.set(messKey,"综合素质开始计算...");
		    	Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
				if(grade==null){
					RedisUtils.set(messKey,"年级数据不存在！");
					RedisUtils.set(key, "error");
					return;
				}
				if(grade.getIsGraduate()==1) {
					RedisUtils.set(messKey,"年级数据已经毕业，不用计算！");
					RedisUtils.set(key, "error");
					return;
				}
				//查询总评是不是有数据  年级与学年学期
				CompreInfo compreInfo = compreInfoService.findByGradeId(grade.getId(),acadyear,semester);
				if(compreInfo==null){
					RedisUtils.set(messKey,"总评设置数据不存在，请先去总评成绩设置模块！");
					RedisUtils.set(key, "error");
					return;
				}
				
				String gradeCodeTime=compreInfo.getGradeCode();//--311
				
				//***************所有统计学生整体********************//*
				//当前年级情况获取所有学生
				List<Student> studentList=SUtils.dt(studentRemoteService.findByGradeIds(grade.getId()),new TR<List<Student>>(){});
				if(CollectionUtils.isEmpty(studentList)){
					RedisUtils.set(messKey,"年级下没有学生数据！");
					RedisUtils.set(key, "error");
					return;
				}
				
				Set<String> allStuIds=new HashSet<String>();
				allStuIds=EntityUtils.getSet(studentList, e->e.getId());
				
				int studentNum = studentList.size();
				CompreStatistics dto;
				
				/***************所有统计需要的参数  总分与英语    口试与体育直接常量定值********************/
				
				//统计时的学科综合素质parkey
				String parkey=gradeCodeTime;
				//英语
				String englishParkey=CompreStatisticsConstants.INFO_KEY_2;
				//统计参数 名次范围给成绩
				List<CompreParameterInfo> parmInfoList= compreParamInfoService.findByInfoKey(unitId,new String[]{parkey,englishParkey});
				Map<String,Map<String,CompreParameterInfoDto>> parameterDtoMap=new LinkedHashMap<String,Map<String,CompreParameterInfoDto>>();
				Map<String, CompreParameterInfoDto> parameterDto;
				
				if(CollectionUtils.isEmpty(parmInfoList)){
					RedisUtils.set(messKey,"没有设置学科成绩折分或者英语成绩折分，请先去设置！");
					RedisUtils.set(key, "error");
					return;
				}else{
					/**key:parkey key:名次段   排名开始与结尾   英语 总评**/
					for(CompreParameterInfo info:parmInfoList){
						String key = info.getInfoKey();
						if(!parameterDtoMap.containsKey(key)){
							parameterDtoMap.put(key, new LinkedHashMap<String, CompreParameterInfoDto>());
						}
						parameterDto=parameterDtoMap.get(key);
						CompreParameterInfoDto item = new CompreParameterInfoDto();
						double mcPrefix = (double) (studentNum*1.0*info.getMcPrefix()/100.0);
						int lowInt =(int) Math.ceil(mcPrefix);
						double mcSuffix = (double) (studentNum*1.0*info.getMcSuffix()/100.0);
						int upInt =(int) Math.ceil(mcSuffix);
						item.setLowNum(lowInt);
						item.setUpNum(upInt);
						item.setScore(info.getScore());
						item.setKeyName(item.getLowNum()+"_"+item.getUpNum());
						parameterDto.put(item.getKeyName(), item);
						
					}
				}
				
				//获取所有成绩---如果某个学生总评排名未找到 就算最后一名
				List<CompreScore> scoreList = compreScoreService.findByCompreInfoId(compreInfo.getId(),true);
				if(CollectionUtils.isEmpty(scoreList)) {
					RedisUtils.set(messKey,"没有成绩！");
					RedisUtils.set(key, "error");
					return;
				}
				Map<String, List<CompreScore>> scoreMap = EntityUtils.getListMap(scoreList, CompreScore::getScoremanageType, e->e);

				//统计结果
				List<CompreStatistics> insertList=new ArrayList<CompreStatistics>();
				//key:学生id_type
				Map<String,CompreStatistics> statisticsBystudentId=new HashMap<String, CompreStatistics>();
				//初始化
				toMakeStudentScore(acadyear, semester,insertList,statisticsBystudentId, studentList);
				
				Set<String> finallyType=new HashSet<>();
				
				List<CompreScore> scoreList1= scoreMap.get(CompreStatisticsConstants.TYPE_OVERALL);
				if(CollectionUtils.isNotEmpty(scoreList1)){
					parameterDto=parameterDtoMap.get(parkey);
					if(parameterDto==null){
						parameterDto=new HashMap<String, CompreParameterInfoDto>();
					}
					toMakeRankStatByType(CompreStatisticsConstants.TYPE_OVERALL, scoreList1, statisticsBystudentId, allStuIds, parameterDto);
					finallyType.add(CompreStatisticsConstants.TYPE_OVERALL);
				}else {
					RedisUtils.set(messKey,"学科成绩未找到，请先去统计学科总评总分！");
					RedisUtils.set(key, "warning");
					logger.info("学科成绩未找到");
				}
				
				//2、英语
				List<CompreScore> scoreList2 = scoreMap.get(CompreStatisticsConstants.TYPE_ENGLISH);
				if(CollectionUtils.isNotEmpty(scoreList2)) {
					logger.info("英语成绩统计");
					parameterDto=parameterDtoMap.get(englishParkey);
					if(parameterDto==null){
						parameterDto=new HashMap<String, CompreParameterInfoDto>();
					}
					toMakeRankStatByType(CompreStatisticsConstants.TYPE_ENGLISH, scoreList2, statisticsBystudentId, allStuIds, parameterDto);
					finallyType.add(CompreStatisticsConstants.TYPE_ENGLISH);
				}else {
					RedisUtils.set(messKey,"英语成绩未找到，请先去设置！");
					RedisUtils.set(key, "warning");
					logger.info("英语成绩未找到");
				}
				//3、口试
				List<CompreScore> scoreList3 = scoreMap.get(CompreStatisticsConstants.TYPE_ENG_SPEAK);
				//暂时默认那边成绩是10分制的---建议保存原始成绩建议按10分制过来
				if(CollectionUtils.isNotEmpty(scoreList3)) {
					logger.info("口试成绩统计");
					float data = CompreStatisticsConstants.ENGLISH_SPEAK;
					for(CompreScore score:scoreList3){
						String key=score.getStudentId()+"_"+CompreStatisticsConstants.TYPE_ENG_SPEAK;
						if(!statisticsBystudentId.containsKey(key)){
							continue;
						}
						dto=statisticsBystudentId.get(key);
						dto.setScore(score.getToScore());
						dto.setRanking(score.getRanking());
						if(score.getToScore()!=null){
							int ff=Math.round(dto.getScore()*data);
							dto.setSplit((float)ff);
						}
					}
					finallyType.add(CompreStatisticsConstants.TYPE_ENG_SPEAK);
				}else {
					RedisUtils.set(messKey,"口试成绩未找到，请先去设置！");
					RedisUtils.set(key, "warning");
					logger.info("口试成绩未找到");
					
				}
				
				//4、体育
				List<CompreScore> scoreList4 = scoreMap.get(CompreStatisticsConstants.TYPE_GYM);
				Float allAvgScore;
				//232
				if(CompreStatisticsConstants.THIRD_LOWER.equals(parkey)){
					logger.info("体育成绩统计");
					allAvgScore=CompreStatisticsConstants.GYM_2;
					if(CollectionUtils.isNotEmpty(scoreList4)){
						//初三下
						for(CompreScore score:scoreList4){
							String key=score.getStudentId()+"_"+CompreStatisticsConstants.TYPE_GYM;
							if(!statisticsBystudentId.containsKey(key)){
								continue;
							}
							dto=statisticsBystudentId.get(key);
							dto.setScore(score.getToScore());
							if(allAvgScore!=null){
								//四舍五入取整
								int ff=Math.round(dto.getScore()*allAvgScore/100);
								dto.setSplit((float)ff);
							}
						}
						finallyType.add(CompreStatisticsConstants.TYPE_GYM);
					}else {
						RedisUtils.set(messKey,"体育成绩未找到，请先去设置！");
						RedisUtils.set(key, "warning");
						logger.info("体育成绩未找到");
					}
					
					
					
				}else if(CompreStatisticsConstants.SENIOR_ONE_LOWER.equals(parkey) || CompreStatisticsConstants.SENIOR_TWO_LOWER.equals(parkey)){
					logger.info("体育成绩统计");
					//311 321
					//高一、高二 上学期 直接拿实际成绩
					if(CollectionUtils.isNotEmpty(scoreList4)){
						//初三下
						for(CompreScore score:scoreList4){
							String key=score.getStudentId()+"_"+CompreStatisticsConstants.TYPE_GYM;
							if(!statisticsBystudentId.containsKey(key)){
								continue;
							}
							dto=statisticsBystudentId.get(key);
							dto.setScore(score.getToScore());
						}
						finallyType.add(CompreStatisticsConstants.TYPE_GYM);
					}else {
						RedisUtils.set(messKey,"体育成绩未找到，请先去设置！");
						RedisUtils.set(key, "warning");
						logger.info("体育成绩未找到");
					}
					
				}else if(CompreStatisticsConstants.SENIOR_ONE_UPPER.equals(parkey) || CompreStatisticsConstants.SENIOR_TWO_UPPER.equals(parkey)){
					logger.info("体育成绩统计");
					allAvgScore=CompreStatisticsConstants.GYM_3;
					//312 322
					//拿上学期统计的数据 与本学期数据
					//上学期统计数据
					List<CompreStatistics> oldList=compreStatisticsService.findByTimeStudentId(unitId,acadyear,"1",CompreStatisticsConstants.TYPE_GYM,allStuIds.toArray(new String[]{}));
					if(CollectionUtils.isNotEmpty(oldList)){
						for(CompreStatistics statscore:oldList){
							String key=statscore.getStudentId()+"_"+CompreStatisticsConstants.TYPE_GYM;
							if(!statisticsBystudentId.containsKey(key)){
								continue;
							}
							dto=statisticsBystudentId.get(key);
							dto.setScore(statscore.getScore());
						}
					}
					if(CollectionUtils.isNotEmpty(scoreList4)) {
						for(CompreScore score:scoreList4){
							String key=score.getStudentId()+"_"+CompreStatisticsConstants.TYPE_GYM;
							if(!statisticsBystudentId.containsKey(key)){
								continue;
							}
							dto=statisticsBystudentId.get(key);
							dto.setScore2(score.getToScore());
							if(allAvgScore!=null){
								float splitScore=((dto.getScore()==null?0f:dto.getScore())+(dto.getScore2()==null?0f:dto.getScore2()))*15/200;
								//四舍五入取整
								int ff=Math.round(splitScore);
								dto.setSplit((float)ff);
							}
							
						}
						finallyType.add(CompreStatisticsConstants.TYPE_GYM);
					}else {
						RedisUtils.set(messKey,"体育成绩未找到，请先去设置！");
						RedisUtils.set(key, "warning");
						logger.info("体育成绩未找到");
					}
					
					
				}else if(CompreStatisticsConstants.THIRD_UPPER.equals(parkey)){
					//高三上没有体育 331
					
				}
				if(CollectionUtils.isEmpty(finallyType)) {
					RedisUtils.set(messKey,"未找到成绩！");
	    			RedisUtils.set(key,"error");
	    			return;
				}
				List<CompreStatistics> finallyInsertList=new ArrayList<CompreStatistics>();
				Map<String, List<CompreStatistics>> listMap = EntityUtils.getListMap(insertList, CompreStatistics::getType, e->e);
				for(String s:finallyType) {
					if(listMap.containsKey(s)) {
						finallyInsertList.addAll(listMap.get(s));
					}
				}
				try{
					if(CollectionUtils.isNotEmpty(insertList)){
						compreStatisticsService.saveAll(unitId,acadyear,semester,allStuIds.toArray(new String[]{}),finallyType.toArray(new String[] {}),finallyInsertList);
					}
				}catch (Exception e) {
					e.printStackTrace();
					RedisUtils.set(messKey,"计算失败！");
	    			RedisUtils.set(key,"error");
	    			logger.info("计算失败！");
	    			return;
				}
	            String warningMess="";
	            if(!finallyType.contains(CompreStatisticsConstants.TYPE_OVERALL)) {
	            	warningMess=warningMess+"、学科成绩";
	            }
	            if(!finallyType.contains(CompreStatisticsConstants.TYPE_ENGLISH)) {
	            	warningMess=warningMess+"、英语";
	            }
	            if(!finallyType.contains(CompreStatisticsConstants.TYPE_ENG_SPEAK)) {
	            	warningMess=warningMess+"、口试";
	            }
	            if(!finallyType.contains(CompreStatisticsConstants.TYPE_GYM)) {
	            	warningMess=warningMess+"、体育";
	            }
	            if(StringUtils.isNotBlank(warningMess)) {
	            	warningMess="其中"+warningMess.substring(1)+",综合素质未统计。";
	            }
            	RedisUtils.set(messKey,"计算成功！"+warningMess);
    			RedisUtils.set(key,"success");
    			logger.info("计算成功！"+warningMess);
    			return;
	         }
	     }).start();   
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("type", RedisUtils.get(key));
		jsonObject.put("mess", RedisUtils.get(messKey));
		if("success".equals(RedisUtils.get(key)) || "error".equals(RedisUtils.get(key))){
			RedisUtils.del(key);
			RedisUtils.del(messKey);
		}
		return  JSON.toJSONString(jsonObject);
	}
	
	@ResponseBody
	@RequestMapping("/qualityScoreStat/removeStat")
    @ControllerInfo(value = "删除综合素质统计数据")
	public String removeStat(final String gradeId,final String acadyear,final String semester,ModelMap map){
		try {
			//当前年级情况获取所有学生
			List<Student> studentList=SUtils.dt(studentRemoteService.findByGradeIds(gradeId),new TR<List<Student>>(){});
			if(CollectionUtils.isEmpty(studentList)){
				return success("");
			}
			Set<String> allStuIds = EntityUtils.getSet(studentList, e->e.getId());
			compreStatisticsService.deleteByStudentIds(getLoginInfo().getUnitId(), acadyear, semester, null,allStuIds.toArray(new String[]{}));
		} catch (Exception e) {
			e.printStackTrace();
			return error("删除失败");
		}
		return success("");
	}
	
	private boolean checkStat(String unitId,String acadyear,String semester,String gradeId){
		List<Student> studentList=SUtils.dt(studentRemoteService.findByGradeIds(gradeId),new TR<List<Student>>(){});
		if(CollectionUtils.isEmpty(studentList)){
			return false;
		}
		Set<String> allStuIds = EntityUtils.getSet(studentList, e->e.getId());
		List<CompreStatistics> oldList=compreStatisticsService.findByTimeStudentId(unitId,acadyear,semester,null,allStuIds.toArray(new String[]{}));
		if(CollectionUtils.isNotEmpty(oldList)){
			return true;
		}else{
			return false;
		}
	}
}
