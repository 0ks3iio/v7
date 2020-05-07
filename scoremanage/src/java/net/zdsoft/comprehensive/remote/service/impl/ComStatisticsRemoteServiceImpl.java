package net.zdsoft.comprehensive.remote.service.impl;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.comprehensive.constant.CompreStatisticsConstants;
import net.zdsoft.comprehensive.entity.CompreParameterInfo;
import net.zdsoft.comprehensive.entity.CompreScore;
import net.zdsoft.comprehensive.entity.CompreStatistics;
import net.zdsoft.comprehensive.remote.service.ComStatisticsRemoteService;
import net.zdsoft.comprehensive.service.CompreParamInfoService;
import net.zdsoft.comprehensive.service.CompreScoreService;
import net.zdsoft.comprehensive.service.CompreStatisticsService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.ScoreInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("comStatisticsRemoteService")
public class ComStatisticsRemoteServiceImpl implements ComStatisticsRemoteService{
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
    @Autowired
    private CompreScoreService compreScoreService;
    @Autowired
    private CompreParamInfoService compreParamInfoService;
	@Autowired
	private CompreStatisticsService compreStatisticsService;
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private ScoreInfoService scoreInfoService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	/**
	 * * "XNXQ";//学年学期
	 * "XKCJ";//学科成绩
	 * "YYBS";//英语笔试
	 * "YYKS";//英语口试
	 * "TYCJ";//体育成绩
	 */
	public static final String key_1="XNXQ";
	public static final String key_2="XKCJ";
	public static final String key_3="YYBS";
	public static final String key_4="YYKS";
	public static final String key_5="TYCJ";
	/**
	 * 最大值
	"XKCJ.MAX.NUMBER";//学科成绩
	"YYBS.MAX.NUMBER";//英语笔试
	"YYKS.MAX.NUMBER";//英语口试
	"TYCJ.MAX.NUMBER";//体育成绩
	*/
	public static final String key_22="XKCJ.MAX.NUMBER";
	public static final String key_33="YYBS.MAX.NUMBER";
	public static final String key_44="YYKS.MAX.NUMBER";
	public static final String key_55="TYCJ.MAX.NUMBER";
	
	public static final Map<String,String> keyByTypeMap=new HashMap<String, String>();
	public static final Map<String,String> maxkeyByKeyMap=new HashMap<String, String>();
	
	static{
		keyByTypeMap.put(CompreStatisticsConstants.TYPE_OVERALL,key_2);
		keyByTypeMap.put(CompreStatisticsConstants.TYPE_ENGLISH,key_3);
		keyByTypeMap.put(CompreStatisticsConstants.TYPE_ENG_SPEAK,key_4);
		keyByTypeMap.put(CompreStatisticsConstants.TYPE_ENG_SPEAK,key_5);
		maxkeyByKeyMap.put(key_2, key_22);
		maxkeyByKeyMap.put(key_3, key_33);
		maxkeyByKeyMap.put(key_4, key_44);
		maxkeyByKeyMap.put(key_5, key_55);
	}

	/**
	 * 修复学处理
	 */
	public void setRecoverStuScore(String[] studentIds,Map<String,Integer> stuYearMap,String acadyear){
		List<CompreStatistics> compreStatisticsList=compreStatisticsService.findByStudentIds(studentIds);
		if(CollectionUtils.isNotEmpty(compreStatisticsList)){
			int nowYear= Integer.parseInt(acadyear.split("-")[0]);
			//直接获取统计后的数据
			Map<String,List<CompreStatistics>> compListMap=compreStatisticsList.stream().collect(Collectors.groupingBy(CompreStatistics::getStudentId));
			List<CompreStatistics> lastList=new ArrayList<>();//可以一个list保存 暂时分开为后续修改准备
			List<CompreStatistics> oldList=new ArrayList<>();
			CompreStatistics last=null;
			for(Map.Entry<String, List<CompreStatistics>> entry:compListMap.entrySet()){
				int year=stuYearMap.get(entry.getKey());
				List<CompreStatistics> inList=compListMap.get(entry.getKey());
				for(CompreStatistics com:inList){
					//处理改学生休学前的老数据
					if(StringUtils.isNotBlank(com.getAcadyear()) && Integer.parseInt(com.getAcadyear().split("-")[0])<=nowYear){
						last=new CompreStatistics();
						last.setId(UuidUtils.generateUuid());
						last.setUnitId(com.getUnitId());
						last.setAcadyear(getAcadyear(com.getAcadyear(),year));//加上对应的休学年份
						last.setSemester(com.getSemester());
						last.setScore(com.getScore());
						last.setScore2(com.getScore2());
						last.setStudentId(com.getStudentId());
						last.setClassId(com.getClassId());
						last.setRanking(com.getRanking());
						last.setSplit(com.getSplit());
						last.setType(com.getType());
						lastList.add(last);
						//老数据暂时保留
						com.setAcadyear(getAcadyear(com.getAcadyear(),-10));
						oldList.add(com);
					}
				}
			}
			if(CollectionUtils.isNotEmpty(lastList)){
				compreStatisticsService.saveAll(lastList.toArray(new CompreStatistics[0]));
			}
			if(CollectionUtils.isNotEmpty(oldList)){
				compreStatisticsService.saveAll(oldList.toArray(new CompreStatistics[0]));
			}
		}
	}
	public String getAcadyear(String acadyear,int l){
		return (Integer.parseInt(acadyear.split("-")[0])+l)+"-"+(Integer.parseInt(acadyear.split("-")[1])+l);
	}
	@Override
	public Map<String, String[]> findStatisticsByStudentId(String studentId,Map<String,Integer> maxValueMap) {
		Student student = SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
		if(student==null){
			return null;
		}
		Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()),Clazz.class);
		if(clazz==null){
			return null;
		}
		List<String> acadyear=new ArrayList<String>();
		List<String> semester=new ArrayList<String>();
		findAcadyearSemesterByGradeId(clazz.getGradeId(), acadyear, semester, true);
		if(CollectionUtils.isEmpty(acadyear)){
			return null;
		}
		//根据学生id
		List<CompreStatistics> list=compreStatisticsService.findByStudentIdAcadyear(student.getId(),acadyear.toArray(new String[]{}));
		//key:学年学期Type value[3]
		Map<String,String[]> scoreMap=new HashMap<String, String[]>();
		//key type
		Map<String,Float> zongFMap=new HashMap<String, Float>();
		if(CollectionUtils.isNotEmpty(list)){
			for(CompreStatistics com:list){
				String key=com.getAcadyear()+"_"+com.getSemester();
				if(!semester.contains(key)){
					continue;
				}
				key=key+"_"+com.getType();
				if(!scoreMap.containsKey(key)){
					scoreMap.put(key, new String[3]);
				}
				String[] arr = scoreMap.get(key);
				if(CompreStatisticsConstants.TYPE_GYM.equals(com.getType())){
					arr[0]=com.getScore()==null?"":""+(com.getScore());
					arr[1]=com.getScore2()==null?"":""+com.getScore2();
					//为了页面展示没有小数  这个值需要整数
					arr[2]=com.getSplit()==null?"":""+Math.round(com.getSplit());
					if("2".equals(com.getSemester())){
						//第二学期才记录总分
						if(!zongFMap.containsKey(com.getType())){
							zongFMap.put(com.getType(), 0f);
						}
						float score = zongFMap.get(com.getType())+(com.getSplit()==null?0:com.getSplit());
						zongFMap.put(com.getType(), score);
					}
					
				}else{
					if(CompreStatisticsConstants.TYPE_ENG_SPEAK.equals(com.getType())){
						//为了页面展示没有小数  这个值需要整数
						arr[0]=com.getScore()==null?"":""+com.getScore();
						arr[2]=com.getSplit()==null?"":""+(Math.round(com.getSplit()));
					}else{
						arr[0]=com.getScore()==null?"":""+com.getScore();
						arr[2]=com.getSplit()==null?"":""+com.getSplit();
					}
					
					arr[1]=com.getRanking()==null?"":""+com.getRanking();
					
					if(!zongFMap.containsKey(com.getType())){
						zongFMap.put(com.getType(), 0f);
					}
					
					float score = zongFMap.get(com.getType())+(com.getSplit()==null?0:com.getSplit());
					zongFMap.put(com.getType(), score);
				}
			}
		}
		
		Map<String, String[]> returnMap = new HashMap<String, String[]>();
		returnMap.put(key_1,new String[semester.size()]);
		String[] ll = returnMap.get(key_1);
		for(int i=0;i<semester.size();i++){
			String[] ss = semester.get(i).split("_");
			ll[i]=ss[0]+"第"+(ss[1].equals("1")?"一":"二")+"学期";
		}
		makeByType(key_2, CompreStatisticsConstants.TYPE_OVERALL, semester, returnMap, zongFMap, scoreMap,maxValueMap);
		makeByType(key_3, CompreStatisticsConstants.TYPE_ENGLISH, semester, returnMap, zongFMap, scoreMap,maxValueMap);
		makeByType(key_4, CompreStatisticsConstants.TYPE_ENG_SPEAK, semester, returnMap, zongFMap, scoreMap,maxValueMap);
		
		returnMap.put(key_5,new String[15]);
		ll = returnMap.get(key_5);
		int yy=-1;
		if(maxkeyByKeyMap.containsKey(key_5) && maxValueMap.containsKey(maxkeyByKeyMap.get(key_5))){
			yy=maxValueMap.get(maxkeyByKeyMap.get(key_5));
		}
		//第一格
		if(zongFMap.containsKey(CompreStatisticsConstants.TYPE_GYM)){
			if(yy>0 && zongFMap.get(CompreStatisticsConstants.TYPE_GYM)>yy){
				ll[0]=yy+"";
			}else{
				ll[0]=zongFMap.get(CompreStatisticsConstants.TYPE_GYM)+"";
			}
		}else{
			ll[0]="";
		}
		int j=1;
		for(int i=0;i<acadyear.size();i++){
			if(i==0){
				//初三下
				String key=acadyear.get(i)+"_2_"+CompreStatisticsConstants.TYPE_GYM;
				ll[j++]=acadyear.get(i)+"学年";
				if(scoreMap.containsKey(key)){
					String[] arr1 = scoreMap.get(key);
					ll[j++]=arr1[0];
					ll[j++]=arr1[1];
				}else{
					ll[j++]="";
					ll[j++]="";
				}
			}else if(i==acadyear.size()-1){
				//高三上没有体育
				ll[j++]=acadyear.get(i)+"学年";
				ll[j++]="";
				ll[j++]="";
			}else{
				String key=acadyear.get(i)+"_2_"+CompreStatisticsConstants.TYPE_GYM;
				ll[j++]=acadyear.get(i)+"学年";
				if(scoreMap.containsKey(key)){
					String[] arr1 = scoreMap.get(key);
					ll[j++]=arr1[0];
					ll[j++]=arr1[1];
					ll[j++]=arr1[2];
				}else{
					String key1=acadyear.get(i)+"_1_"+CompreStatisticsConstants.TYPE_GYM;
					if(scoreMap.containsKey(key1)){
						String[] arr1 = scoreMap.get(key1);
						ll[j++]=arr1[0];
						ll[j++]=arr1[1];
						ll[j++]=arr1[2];
					}else{
						ll[j++]="";
						ll[j++]="";
						ll[j++]="";
					}
				}
			}
			if(j>=15){
				break;
			}
		}
		return returnMap;	
	}
	
	private void makeByType(String keyType,String type,List<String> semester,Map<String, String[]> map,Map<String,Float> zongFMap,
			Map<String,String[]> scoreMap,Map<String,Integer> maxValueMap){
		map.put(keyType,new String[semester.size()*3+1]);
		String[] ll = map.get(keyType);
		//第一格
		int yy=-1;
		if(maxkeyByKeyMap.containsKey(keyType) && maxValueMap.containsKey(maxkeyByKeyMap.get(keyType))){
			yy=maxValueMap.get(maxkeyByKeyMap.get(keyType));
		}
		if(zongFMap.containsKey(type)){
			if(yy>0 && zongFMap.get(type)>yy){
				ll[0]=yy+"";
			}else{
				ll[0]=zongFMap.get(type)+"";
			}
		}else{
			ll[0]="";
		}
		int j=1;
		for(int i=0;i<semester.size();i++){
			String key=semester.get(i)+"_"+type;
			if(scoreMap.containsKey(key)){
				String[] arr1 = scoreMap.get(key);
				ll[j++]=arr1[0];
				ll[j++]=arr1[1];
				ll[j++]=arr1[2];
			}else{
				ll[j++]="";
				ll[j++]="";
				ll[j++]="";
			}
		}
	}

	@Override
	public Map<String, String> findStatisticsByUnitId(String unitId,Map<String,Integer> maxValueMap, Map<String, Boolean> showMap) {
		//1:根据单位 拿到高中年级
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId, new Integer[]{3,9}),new TR<List<Grade>>(){});
		if(CollectionUtils.isEmpty(gradeList)){
			return null;
		}
		//各年级 key:gradeId
		Map<String,List<String>> acadyearMap=new HashMap<String, List<String>>();
		Map<String,List<String>> semesterMap=new HashMap<String, List<String>>();
		Set<String> acadyearSet=new HashSet<String>();
		Set<String> gradeIds=new HashSet<String>();
		for(Grade g:gradeList){
			List<String> acadyear=new ArrayList<String>();
			List<String> semester=new ArrayList<String>();
			boolean isShow = showMap.containsKey(g.getId())&&showMap.get(g.getId());
			findAcadyearSemesterByGradeId(g.getId(), acadyear, semester, isShow);
			if(CollectionUtils.isNotEmpty(acadyear)){
				acadyearMap.put(g.getId(), acadyear);
				semesterMap.put(g.getId(), semester);
				acadyearSet.addAll(acadyear);
				gradeIds.add(g.getId());
			}else{
				//没有数据
			}
		}
		if(acadyearSet.size()<=0){
			return null;
		}
		//防止班级重组 原来保存统计表中班级id已经无用
		//key:gradeId value:studentIds 
		Map<String,Set<String>> studentByGradeId=new HashMap<String, Set<String>>();

		makeStuInGrade(studentByGradeId, gradeIds.toArray(new String[]{}));
		if(studentByGradeId.size()<=0){
			return null;
		}
		List<CompreStatistics> list=compreStatisticsService.findByUnitIdAcadyear(unitId,acadyearSet.toArray(new String[]{}));
		if(CollectionUtils.isEmpty(list)){
			return new HashMap<String, String>();
		}		
		//key:studentId
		Map<String,List<CompreStatistics>> scoreByStudentIdMap=new HashMap<String, List<CompreStatistics>>();
		for(CompreStatistics com:list){
			if(!scoreByStudentIdMap.containsKey(com.getStudentId())){
				scoreByStudentIdMap.put(com.getStudentId(), new ArrayList<CompreStatistics>());
			}
			scoreByStudentIdMap.get(com.getStudentId()).add(com);
		}
		Map<String,String> zongscoreMap=new HashMap<String, String>();
		
		for(String key:studentByGradeId.keySet()){
			List<String> acadyear = acadyearMap.get(key);
			List<String> semester = semesterMap.get(key);
			Set<String> set = studentByGradeId.get(key);
			for(String stuId:set){
				if(!scoreByStudentIdMap.containsKey(stuId)){
					zongscoreMap.put(stuId, "");
					continue;
				}
				List<CompreStatistics> llist = scoreByStudentIdMap.get(stuId);
				if(CollectionUtils.isEmpty(llist)){
					zongscoreMap.put(stuId, "");
					continue;
				}
				String sss = makeScore(llist, acadyear, semester, maxValueMap);
				zongscoreMap.put(stuId, sss);
			}
		}

		return zongscoreMap;
	}
	/**
	 * 计算总分
	 * @return
	 */
	private String makeScore(List<CompreStatistics> llist,List<String> acadyear,List<String> semester,Map<String,Integer> maxValueMap ){
		//计算
		Map<String,Float> zongFMap=new HashMap<String, Float>();
		for(CompreStatistics com:llist){
			String key=com.getAcadyear()+"_"+com.getSemester();
			if(!semester.contains(key)){
				continue;
			}
			key=key+"_"+com.getType();
			if(CompreStatisticsConstants.TYPE_GYM.equals(com.getType())){
				if("2".equals(com.getSemester())){
					//第二学期才记录总分
					if(!zongFMap.containsKey(com.getType())){
						zongFMap.put(com.getType(), 0f);
					}
					float score = zongFMap.get(com.getType())+(com.getSplit()==null?0:com.getSplit());
					zongFMap.put(com.getType(), score);
				}
				
			}else{
				if(!zongFMap.containsKey(com.getType())){
					zongFMap.put(com.getType(), 0f);
				}
				float score = zongFMap.get(com.getType())+(com.getSplit()==null?0:com.getSplit());
				zongFMap.put(com.getType(), score);
			}
		}
		if(zongFMap.size()<=0){
			return "";
		}
		Float ff=0f;
		
		for(String key:zongFMap.keySet()){
			int yy=-1;
			if(maxkeyByKeyMap.containsKey(keyByTypeMap.get(key))){
				if(maxValueMap.containsKey(maxkeyByKeyMap.get(keyByTypeMap.get(key)))){
					yy=maxValueMap.get(maxkeyByKeyMap.get(keyByTypeMap.get(key)));
				}
			}
			
			if(yy>0 && zongFMap.get(key)>yy){
				ff=ff+yy;
			}else{
				ff=ff+zongFMap.get(key);
			}
		}
		return ff+"";
	}
	
	private void makeStuInGrade(Map<String, Set<String>> studentByGradeId,String[] gradeIds){
		//key:classId value:gradeId
		Map<String,String> classIdByGradeId=new HashMap<String, String>();
		List<Clazz> classList = SUtils.dt(classRemoteService.findByInGradeIds(gradeIds),new TR<List<Clazz>>(){});
		if(CollectionUtils.isEmpty(classList)){
			return;
		}
		Set<String> classIds = EntityUtils.getSet(classList, "id");
		List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[]{})),new TR<List<Student>>(){});
		if(CollectionUtils.isEmpty(studentList)){
			return;
		}
		
		for(Clazz clazz:classList){
			classIdByGradeId.put(clazz.getId(), clazz.getGradeId());
		}
		for(Student student:studentList){
			if(!classIdByGradeId.containsKey(student.getClassId())){
				continue;
			}
			if(!studentByGradeId.containsKey(classIdByGradeId.get(student.getClassId()))){
				studentByGradeId.put(classIdByGradeId.get(student.getClassId()), new HashSet<String>());
			}
			studentByGradeId.get(classIdByGradeId.get(student.getClassId())).add(student.getId());
		}
	}
	
	/**
	 * 获取该年级在初三下到高三上
	 * @param gradeId
	 * @param acadyear
	 * @param semester key:2014-2015_1(2014-2015学年第一学期)
	 */
	private void findAcadyearSemesterByGradeId(String gradeId,List<String> acadyear,List<String>semester, boolean isShow){
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId),Grade.class);
		if(grade!=null && (grade.getSection()==3 || grade.getSection()==9)){
			//高三
			String openAcadyear = grade.getOpenAcadyear();
			String[] splitArr = openAcadyear.split("-");
			//初三
			int start = Integer.parseInt(splitArr[0])-1;
			int end=Integer.parseInt(splitArr[1])-1;
			//初三下
            if(isShow){
                acadyear.add(start+"-"+end);
                semester.add(start+"-"+end+"_2");
            }
			//高一高二上下
			start =start+1;
			end=end+1;
			acadyear.add(start+"-"+end);
			semester.add(start+"-"+end+"_1");
			semester.add(start+"-"+end+"_2");
			start =start+1;
			end=end+1;
			acadyear.add(start+"-"+end);
			semester.add(start+"-"+end+"_1");
			semester.add(start+"-"+end+"_2");
			//高三上
			start =start+1;
			end=end+1;
			acadyear.add(start+"-"+end);
			semester.add(start+"-"+end+"_1");
		}else{
			return;
		}
	}

	@Override
	public Map<String, String> findXKStatisticsByStudentId(String studentId) {
		Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        List<CompreParameterInfo> compreParameterInfoList = compreParamInfoService.findByInfoKey(student.getSchoolId(), CompreStatisticsConstants.INFO_KEY_5);
        Map<String, Float> compreParamInfoMap = EntityUtils.getMap(compreParameterInfoList, CompreParameterInfo::getGradeScore, CompreParameterInfo::getScore);
        List<CompreScore> compreScoreList = compreScoreService.findByStudentIdAndType(studentId, CompreStatisticsConstants.TYPE_XK);
        Map<String, String> resultMap = new HashMap<>();
        Float total = Float.valueOf(0.0f);
        for (CompreScore one : compreScoreList) {
            Float score = compreParamInfoMap.get(one.getScore());
            if (score == null) {
                score = Float.valueOf(0.0f);
            }
            total += score;
            resultMap.put(one.getSubjectId(), one.getScore());
        }
        resultMap.put(BaseConstants.ZERO_GUID, total.toString());
		return resultMap;
	}

	@Override
	public Map<String, String> findXKStatisticsByUnitId(String unitId) {
		Map<String, String> resultMap = new HashMap<>();
		Map<String, List<CompreScore>> studentIdToCompreScore = new HashMap<>();
        List<CompreParameterInfo> compreParameterInfoList = compreParamInfoService.findByInfoKey(unitId, CompreStatisticsConstants.INFO_KEY_5);
        Map<String, Float> compreParamInfoMap = EntityUtils.getMap(compreParameterInfoList, CompreParameterInfo::getGradeScore, CompreParameterInfo::getScore);
		List<CompreScore> compreScoreList = compreScoreService.findByUnitIdAndType(unitId, CompreStatisticsConstants.TYPE_XK);
		for (CompreScore one : compreScoreList) {
		    if (CollectionUtils.isEmpty(studentIdToCompreScore.get(one.getStudentId()))) {
		        List<CompreScore> tmp = new ArrayList<>();
		        tmp.add(one);
		        studentIdToCompreScore.put(one.getStudentId(), tmp);
            } else {
                studentIdToCompreScore.get(one.getStudentId()).add(one);
            }
        }
        for (Map.Entry<String, List<CompreScore>> one : studentIdToCompreScore.entrySet()) {
            Float total = 0.0f;
            List<CompreScore> tmp = one.getValue();
            for (CompreScore compreScore : tmp) {
                Float score = compreParamInfoMap.get(compreScore.getScore());
                if (score == null) {
                    score = Float.valueOf(0.0f);
                }
                total += score;
            }
            resultMap.put(one.getKey(), total.toString());
        }
		return resultMap;
	}

	@Override
	public Map<String, Map<String, String[]>> findStatisticsByStudentIds(String classId, String[] studentIds, Map<String, Integer> maxValueMap) {
		Map<String, Map<String, String[]>> map = new HashMap<String, Map<String,String[]>>();
		Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
		List<String> acadyear=new ArrayList<String>();
		List<String> semester=new ArrayList<String>();
		findAcadyearSemesterByGradeId(clazz.getGradeId(), acadyear, semester, true);
		if(CollectionUtils.isEmpty(acadyear)){
			return map;
		}
		//根据学生id
		List<CompreStatistics> statisticsList=compreStatisticsService.findByStudentIdsAcadyear(studentIds,acadyear.toArray(new String[]{}));
		Map<String, List<CompreStatistics>> listMap = EntityUtils.getListMap(statisticsList, CompreStatistics::getStudentId, Function.identity());
		for (String studentId : studentIds) {
			List<CompreStatistics> list = listMap.get(studentId);
			//key:学年学期Type value[3]
			Map<String,String[]> scoreMap=new HashMap<String, String[]>();
			//key type
			Map<String,Float> zongFMap=new HashMap<String, Float>();
			if(CollectionUtils.isNotEmpty(list)){
				for(CompreStatistics com:list){
					String key=com.getAcadyear()+"_"+com.getSemester();
					if(!semester.contains(key)){
						continue;
					}
					key=key+"_"+com.getType();
					if(!scoreMap.containsKey(key)){
						scoreMap.put(key, new String[3]);
					}
					String[] arr = scoreMap.get(key);
					if(CompreStatisticsConstants.TYPE_GYM.equals(com.getType())){
						arr[0]=com.getScore()==null?"":""+(com.getScore());
						arr[1]=com.getScore2()==null?"":""+com.getScore2();
						//为了页面展示没有小数  这个值需要整数
						arr[2]=com.getSplit()==null?"":""+Math.round(com.getSplit());
						if("2".equals(com.getSemester())){
							//第二学期才记录总分
							if(!zongFMap.containsKey(com.getType())){
								zongFMap.put(com.getType(), 0f);
							}
							float score = zongFMap.get(com.getType())+(com.getSplit()==null?0:com.getSplit());
							zongFMap.put(com.getType(), score);
						}
						
					}else{
						if(CompreStatisticsConstants.TYPE_ENG_SPEAK.equals(com.getType())){
							//为了页面展示没有小数  这个值需要整数
							arr[0]=com.getScore()==null?"":""+com.getScore();
							arr[2]=com.getSplit()==null?"":""+(Math.round(com.getSplit()));
						}else{
							arr[0]=com.getScore()==null?"":""+com.getScore();
							arr[2]=com.getSplit()==null?"":""+com.getSplit();
						}
						
						arr[1]=com.getRanking()==null?"":""+com.getRanking();
						
						if(!zongFMap.containsKey(com.getType())){
							zongFMap.put(com.getType(), 0f);
						}
						
						float score = zongFMap.get(com.getType())+(com.getSplit()==null?0:com.getSplit());
						zongFMap.put(com.getType(), score);
					}
				}
			}
			
			Map<String, String[]> returnMap = new HashMap<String, String[]>();
			returnMap.put(key_1,new String[semester.size()]);
			String[] ll = returnMap.get(key_1);
			for(int i=0;i<semester.size();i++){
				String[] ss = semester.get(i).split("_");
				ll[i]=ss[0]+"第"+(ss[1].equals("1")?"一":"二")+"学期";
			}
			makeByType(key_2, CompreStatisticsConstants.TYPE_OVERALL, semester, returnMap, zongFMap, scoreMap,maxValueMap);
			makeByType(key_3, CompreStatisticsConstants.TYPE_ENGLISH, semester, returnMap, zongFMap, scoreMap,maxValueMap);
			makeByType(key_4, CompreStatisticsConstants.TYPE_ENG_SPEAK, semester, returnMap, zongFMap, scoreMap,maxValueMap);
			
			returnMap.put(key_5,new String[15]);
			ll = returnMap.get(key_5);
			int yy=-1;
			if(maxkeyByKeyMap.containsKey(key_5) && maxValueMap.containsKey(maxkeyByKeyMap.get(key_5))){
				yy=maxValueMap.get(maxkeyByKeyMap.get(key_5));
			}
			//第一格
			if(zongFMap.containsKey(CompreStatisticsConstants.TYPE_GYM)){
				if(yy>0 && zongFMap.get(CompreStatisticsConstants.TYPE_GYM)>yy){
					ll[0]=yy+"";
				}else{
					ll[0]=zongFMap.get(CompreStatisticsConstants.TYPE_GYM)+"";
				}
			}else{
				ll[0]="";
			}
			int j=1;
			for(int i=0;i<acadyear.size();i++){
				if(i==0){
					//初三下
					String key=acadyear.get(i)+"_2_"+CompreStatisticsConstants.TYPE_GYM;
					ll[j++]=acadyear.get(i)+"学年";
					if(scoreMap.containsKey(key)){
						String[] arr1 = scoreMap.get(key);
						ll[j++]=arr1[0];
						ll[j++]=arr1[1];
					}else{
						ll[j++]="";
						ll[j++]="";
					}
				}else if(i==acadyear.size()-1){
					//高三上没有体育
					ll[j++]=acadyear.get(i)+"学年";
					ll[j++]="";
					ll[j++]="";
				}else{
					String key=acadyear.get(i)+"_2_"+CompreStatisticsConstants.TYPE_GYM;
					ll[j++]=acadyear.get(i)+"学年";
					if(scoreMap.containsKey(key)){
						String[] arr1 = scoreMap.get(key);
						ll[j++]=arr1[0];
						ll[j++]=arr1[1];
						ll[j++]=arr1[2];
					}else{
						String key1=acadyear.get(i)+"_1_"+CompreStatisticsConstants.TYPE_GYM;
						if(scoreMap.containsKey(key1)){
							String[] arr1 = scoreMap.get(key1);
							ll[j++]=arr1[0];
							ll[j++]=arr1[1];
							ll[j++]=arr1[2];
						}else{
							ll[j++]="";
							ll[j++]="";
							ll[j++]="";
						}
					}
				}
				if(j>=15){
					break;
				}
			}
			map.put(studentId, returnMap);
		}
		return map;
	}

	@Override
	public Map<String, Map<String, String>> findXKStatisticsByStudentIds(String unitId, String[] studentIds) {
		Map<String, Map<String, String>> map = new HashMap<String, Map<String,String>>();
        List<CompreParameterInfo> compreParameterInfoList = compreParamInfoService.findByInfoKey(unitId, CompreStatisticsConstants.INFO_KEY_5);
        Map<String, Float> compreParamInfoMap = EntityUtils.getMap(compreParameterInfoList, CompreParameterInfo::getGradeScore, CompreParameterInfo::getScore);
        List<CompreScore> list = compreScoreService.findByStudentIdsAndType(studentIds, CompreStatisticsConstants.TYPE_XK);
        Map<String, List<CompreScore>> listMap = EntityUtils.getListMap(list, CompreScore::getStudentId, Function.identity());
        for (String studentId : studentIds) {
        	Map<String, String> resultMap = new HashMap<>();
        	Float total = Float.valueOf(0.0f);
        	List<CompreScore> compreScoreList = listMap.get(studentId);
        	if(CollectionUtils.isNotEmpty(compreScoreList)){
        		for (CompreScore one : compreScoreList) {
        			Float score = compreParamInfoMap.get(one.getScore());
        			if (score == null) {
        				score = Float.valueOf(0.0f);
        			}
        			total += score;
        			resultMap.put(one.getSubjectId(), one.getScore());
        		}
        	}
        	resultMap.put(BaseConstants.ZERO_GUID, total.toString());
        	map.put(studentId, resultMap);
		}
		return map;
	}

	@Override
	public Map<String, Map<String, String[]>> findXxhzByStudentIds(String unitId, String acadyear, String semester, String gradeId, String[] studentIds) {
		Map<String, Map<String, String[]>> map = new HashMap<String, Map<String,String[]>>();
		List<String> acadyearList=new ArrayList<String>();
		List<String> semesterList=new ArrayList<String>();
		findAcadyearSemesterByGradeId(gradeId, acadyearList, semesterList, true);
		if(CollectionUtils.isEmpty(semesterList)||!semesterList.contains(acadyear+"_"+semester)){
			return map;
		}
		//根据学生id
		List<CompreStatistics> statisticsList=compreStatisticsService.findByAcaSemStudentIds(acadyear,semester,studentIds);
		statisticsList = statisticsList.stream().filter(e->!CompreStatisticsConstants.TYPE_GYM.equals(e.getType())).collect(Collectors.toList());
		Map<String, List<CompreStatistics>> listMap = EntityUtils.getListMap(statisticsList, CompreStatistics::getStudentId, Function.identity());
		Map<String, List<ScoreInfo>> scoreInfoMap = new HashMap<String, List<ScoreInfo>>();
		List<CompreParameterInfo> compreParameterInfoList = compreParamInfoService.findByInfoKey(unitId,CompreStatisticsConstants.INFO_KEY_5);
		Map<String, Float> compreParamInfoMap = EntityUtils.getMap(compreParameterInfoList, CompreParameterInfo::getGradeScore, CompreParameterInfo::getScore);
		List<ExamInfo> examList = examInfoService.findExamInfoListAll(unitId, acadyear, semester, gradeId);
		examList = examList.stream().filter(e->CompreStatisticsConstants.TYPE_XK.equals(e.getExamType())).collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(examList)){
			Collections.sort(examList, (x,y)->{
				return y.getCreationTime().compareTo(x.getCreationTime());
			});
			ExamInfo exam = examList.get(0);
        	List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitCourseCodes(unitId, CompreStatisticsConstants.HW_CODE_10.toArray(new String[] {})),new TR<List<Course>>(){});
			List<ScoreInfo> scoreList = scoreInfoService.findByExamIdAndSubIdsAndStudentIds(unitId, new String[]{exam.getId()}, EntityUtils.getList(courseList, Course::getId).toArray(new String[0]), studentIds);
			scoreInfoMap = EntityUtils.getListMap(scoreList, ScoreInfo::getStudentId, Function.identity());
		}
		
		for (String studentId : studentIds) {
			List<CompreStatistics> list = listMap.get(studentId);
			//key:type value[3]
			Map<String,String[]> scoreMap=new HashMap<String, String[]>();
			if(CollectionUtils.isNotEmpty(list)){
				for(CompreStatistics com:list){
					if(!scoreMap.containsKey(com.getType())){
						scoreMap.put(com.getType(), new String[3]);
					}
					String[] arr = scoreMap.get(com.getType());
					if(CompreStatisticsConstants.TYPE_ENG_SPEAK.equals(com.getType())){
						//为了页面展示没有小数  这个值需要整数
						arr[0]=com.getScore()==null?"":""+com.getScore();
						arr[2]=com.getSplit()==null?"":""+(Math.round(com.getSplit()));
					}else{
						arr[0]=com.getScore()==null?"":""+com.getScore();
						arr[2]=com.getSplit()==null?"":""+com.getSplit();
					}
					
					arr[1]=com.getRanking()==null?"":""+com.getRanking();
				}
			}
			List<ScoreInfo> scoreList = scoreInfoMap.get(studentId);
			if(CollectionUtils.isNotEmpty(scoreList)){
				Float total = Float.valueOf(0.0f);
				for (ScoreInfo one : scoreList) {
					Float score = compreParamInfoMap.get(one.getScore());
					if (score == null) {
						score = Float.valueOf(0.0f);
					}
					total += score;
				}
				scoreMap.put("4", new String[]{total+""});
			}else{
				scoreMap.put("4", new String[]{""});
			}
			map.put(studentId, scoreMap);
		}
		return map;
	}

	@Override
	public Map<String, String[]> findTyhzByStudentIds(String unitId, String acadyear, String semester, String gradeId, String[] studentIds) {
		Map<String, String[]> map = new HashMap<String, String[]>();
		List<String> acadyearList=new ArrayList<String>();
		List<String> semesterList=new ArrayList<String>();
		findAcadyearSemesterByGradeId(gradeId, acadyearList, semesterList, true);
		if(CollectionUtils.isEmpty(semesterList)||!semesterList.contains(acadyear+"_"+semester)){
			return map;
		}
		//根据学生id
		List<CompreStatistics> statisticsList=compreStatisticsService.findByAcaSemStudentIds(acadyear,semester,studentIds);
		statisticsList = statisticsList.stream().filter(e->CompreStatisticsConstants.TYPE_GYM.equals(e.getType())).collect(Collectors.toList());
		Map<String, CompreStatistics> listMap = EntityUtils.getMap(statisticsList, CompreStatistics::getStudentId, Function.identity());
		for (String studentId : studentIds) {
			CompreStatistics com = listMap.get(studentId);
			String[] arr = new String[2];
			if(com!=null){
				if("1".equals(semester)){
					arr[0]=com.getScore()==null?"":""+(com.getScore());
				}else{
					arr[0]=com.getScore2()==null?"":""+com.getScore2();
				}
				//为了页面展示没有小数  这个值需要整数
				if(StringUtils.isNotBlank(arr[0])){
					if((acadyear+"_"+semester).equals(semesterList.get(0))){
						arr[1] = "" + Math.round(Float.parseFloat(arr[0])/100*4);
					}else{
						arr[1] = "" + Math.round(Float.parseFloat(arr[0])/100*7.5);
					}
				}else{
					arr[1] = "";
				}
			}
			map.put(studentId, arr);
		}
		return map;
	}

	@Override
	public Map<String, String> findYybsByGradeMap(Map<String, List<String>> studentByGradeId, int maxValue) {
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findListByIds(studentByGradeId.keySet().toArray(new String[0])),new TR<List<Grade>>(){});
		if(CollectionUtils.isEmpty(gradeList)){
			return null;
		}
		//各年级 key:gradeId
		Map<String,List<String>> acadyearMap=new HashMap<String, List<String>>();
		Map<String,List<String>> semesterMap=new HashMap<String, List<String>>();
		Set<String> acadyearSet=new HashSet<String>();
		Set<String> gradeIds=new HashSet<String>();
		List<String> studentIds = new ArrayList<>();
		for(Grade g:gradeList){
			studentIds.addAll(studentByGradeId.get(g.getId()));
			List<String> acadyear=new ArrayList<String>();
			List<String> semester=new ArrayList<String>();
			findAcadyearSemesterByGradeId(g.getId(), acadyear, semester, true);
			if(CollectionUtils.isNotEmpty(acadyear)){
				acadyearMap.put(g.getId(), acadyear);
				semesterMap.put(g.getId(), semester);
				acadyearSet.addAll(acadyear);
				gradeIds.add(g.getId());
			}else{
				//没有数据
			}
		}
		if(acadyearSet.size()<=0){
			return null;
		}
		List<CompreStatistics> list = compreStatisticsService.findByTypeStudentIdsAcadyear(CompreStatisticsConstants.TYPE_ENGLISH, studentIds.toArray(new String[0]), acadyearSet.toArray(new String[0]));
		if(CollectionUtils.isEmpty(list)){
			return new HashMap<String, String>();
		}
		//key:studentId
		Map<String,List<CompreStatistics>> scoreByStudentIdMap=new HashMap<String, List<CompreStatistics>>();
		for(CompreStatistics com:list){
			if(!scoreByStudentIdMap.containsKey(com.getStudentId())){
				scoreByStudentIdMap.put(com.getStudentId(), new ArrayList<CompreStatistics>());
			}
			scoreByStudentIdMap.get(com.getStudentId()).add(com);
		}
		Map<String,String> zongscoreMap=new HashMap<String, String>();

		for(String gradeId:studentByGradeId.keySet()){
			List<String> semester = semesterMap.get(gradeId);
			List<String> list1 = studentByGradeId.get(gradeId);
			for(String stuId:list1){
				float score = 0f;
				if(scoreByStudentIdMap.containsKey(stuId)){
					for (CompreStatistics s : scoreByStudentIdMap.get(stuId)) {
						String key=s.getAcadyear()+"_"+s.getSemester();
						if(!semester.contains(key)){
							continue;
						}
						if(s.getSplit()!=null){
							score+=s.getSplit();
						}
					}
					if(maxValue>0 && score>maxValue){
						score=maxValue;
					}
				}
				zongscoreMap.put(stuId, score+"");
			}
		}
		return zongscoreMap;
	}

}
