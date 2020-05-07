package net.zdsoft.stuwork.data.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.dao.DyAllCheckDao;
import net.zdsoft.stuwork.data.dto.AllCheckDto;
import net.zdsoft.stuwork.data.entity.DyAllCheck;
import net.zdsoft.stuwork.data.entity.DyClassOtherCheck;
import net.zdsoft.stuwork.data.entity.DyClassstatWeek;
import net.zdsoft.stuwork.data.entity.DyDormStatResult;
import net.zdsoft.stuwork.data.service.DyAllCheckService;
import net.zdsoft.stuwork.data.service.DyClassOtherCheckService;
import net.zdsoft.stuwork.data.service.DyClassstatWeekService;
import net.zdsoft.stuwork.data.service.DyDormStatResultService;
import net.zdsoft.stuwork.data.service.DyStuPunishmentService;

@Service("dyAllCheckService")
public class DyAllCheckServiceImpl extends BaseServiceImpl<DyAllCheck, String> implements DyAllCheckService{

	@Autowired
	private DyAllCheckDao dyAllCheckDao;
	@Autowired
	private DyClassstatWeekService dyClassstatWeekService;
	@Autowired
	private DyDormStatResultService dyDormStatResultService;
	@Autowired
	private DyStuPunishmentService dyStuPunishmentService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private DyClassOtherCheckService dyClassOtherCheckService;
	
	@Override
	public DyAllCheck getAllCheckBy(String unitId,String acadyear,String semester,int week,String classId){
		return dyAllCheckDao.getAllCheckBy(unitId, acadyear, semester, week, classId);
	}
	@Override
	public  List<AllCheckDto> getListBy(String unitId,String acadyear,String semester,int section,int allWeek){
		List<DyAllCheck> checkList=dyAllCheckDao.getListBy(unitId, acadyear, semester,section);
		List<AllCheckDto> dtoList=new ArrayList<AllCheckDto>();
		if(CollectionUtils.isNotEmpty(checkList)){
			Map<Integer,List<DyAllCheck>> weekOfcheckMap=new HashMap<Integer, List<DyAllCheck>>();
			for(DyAllCheck check:checkList){
				if(!weekOfcheckMap.containsKey(check.getWeek())){
					weekOfcheckMap.put(check.getWeek(), new ArrayList<DyAllCheck>());
				}
				weekOfcheckMap.get(check.getWeek()).add(check);
			}
			for(int i=1;i<=allWeek;i++){
				List<DyAllCheck> inList=weekOfcheckMap.get(i);
				Map<String,DyAllCheck> inMap=EntityUtils.getMap(inList, "classId");
				getDto(dtoList, inMap, "第"+i+"周");
			}
			//本学期
			Map<String,List<DyAllCheck>> checkMap=new HashMap<String,List<DyAllCheck>>();
			if(CollectionUtils.isNotEmpty(checkList)){
				for(DyAllCheck check:checkList){
					if(!checkMap.containsKey(check.getClassId())){
						checkMap.put(check.getClassId(), new ArrayList<DyAllCheck>());
					}
					checkMap.get(check.getClassId()).add(check);
				}
			}
			List<DyAllCheck> nowSeList=new ArrayList<DyAllCheck>();
			DyAllCheck nowSeCheck=null;
			DecimalFormat decimalFormat=new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足1位,会以0补足.
			for(Entry<String,List<DyAllCheck>> entry:checkMap.entrySet()){
				nowSeCheck=new DyAllCheck();
				List<DyAllCheck> classCheckList=entry.getValue();
				if(CollectionUtils.isNotEmpty(classCheckList)){
					float score=0f;
					for(DyAllCheck classCheck:classCheckList){
						score+=classCheck.getAllScore();
					}
					String p=decimalFormat.format(score);//format 返回的是字符串
					nowSeCheck.setAllScore(NumberUtils.toFloat(p));
//					nowSeCheck.setAllScore(score);
				}
				nowSeCheck.setClassId(entry.getKey());
				nowSeList.add(nowSeCheck);
			}
			Collections.sort(nowSeList, new Comparator<DyAllCheck>() {
				@Override
				public int compare(DyAllCheck o1, DyAllCheck o2) {
					if(o2.getAllScore()<o1.getAllScore()) {
						return -1;
					}else if(o2.getAllScore()>o1.getAllScore()) {
						return 1;
					}else {
						return 0;
					}
				}
			});
			for(int i=0;i<nowSeList.size();i++){
				DyAllCheck check=nowSeList.get(i);
				if(i>0 && ((int)(check.getAllScore()*10))==((int)(nowSeList.get(i-1).getAllScore()*10))){
					check.setAllRank(nowSeList.get(i-1).getAllRank());
				}else{
					check.setAllRank(i+1);
				}
			}
//			Collections.sort(nowSeList, new Comparator<DyAllCheck>() {
//				@Override
//				public int compare(DyAllCheck o1, DyAllCheck o2) {
//					return ((int)o2.getAllScore()*10)-((int)o1.getAllScore()*10);
//				}
//			});
//			for(int i=0;i<nowSeList.size();i++){
//				DyAllCheck check=nowSeList.get(i);
//				if(i>0 && ((int)check.getAllScore()*10)==((int)nowSeList.get(i-1).getAllScore()*10)){
//					check.setAllRank(nowSeList.get(i-1).getAllRank());
//				}else{
//					check.setAllRank(i+1);
//				}
//			}
			Map<String,DyAllCheck> inMap=EntityUtils.getMap(nowSeList, "classId");
			getDto(dtoList, inMap, "本学期");
		}
		return dtoList;
	}
	public List<AllCheckDto>  getDto(List<AllCheckDto> dtoList,Map<String,DyAllCheck> inMap,String weekStr){
		AllCheckDto dto1=new AllCheckDto();
		dto1.setWeekStr(weekStr);
		dto1.setName("小计");
		dto1.setInMap(inMap);
		dtoList.add(dto1);
		
		AllCheckDto dto2=new AllCheckDto();
		dto2.setWeekStr(weekStr);
		dto2.setName("排名");
		dto2.setInMap(inMap);
		dtoList.add(dto2);
		return dtoList;
	}
	@Override
	public void saveStat(String unitId,String acadyear,String semester,int week){
		//获取每个班级的卫生纪律总分
		Map<String,DyClassstatWeek> classStatMap=EntityUtils.getMap(dyClassstatWeekService.findBySchoolIdAndAcadyearAndSemesterAndWeek(unitId, acadyear, semester, week),DyClassstatWeek::getClassId);
		//获取寝室考勤数据
		List<DyDormStatResult> resultList=dyDormStatResultService.getStatNotClassId(unitId, acadyear, semester, week);
		Map<String,List<DyDormStatResult>> resultListMap=new HashMap<String,List<DyDormStatResult>>();
		for(DyDormStatResult result:resultList){
			if(!resultListMap.containsKey(result.getClassId())){
				resultListMap.put(result.getClassId(), new ArrayList<DyDormStatResult>());
			}
			resultListMap.get(result.getClassId()).add(result);
		}
		//获取违纪分数
		Map<String, Float> punishMap=dyStuPunishmentService.findMapBy(unitId, acadyear, semester, week);
		//班级其他考核分
		List<DyClassOtherCheck> otherList=dyClassOtherCheckService.findByClassIdAndWeek(null, week, acadyear, Integer.parseInt(semester));
		Map<String,List<DyClassOtherCheck>> otherMap=new HashMap<String,List<DyClassOtherCheck>>();
		for(DyClassOtherCheck other:otherList){
			if(!otherMap.containsKey(other.getClassId())){
				otherMap.put(other.getClassId(), new ArrayList<DyClassOtherCheck>());
			}
			otherMap.get(other.getClassId()).add(other);
		}
		
		//School sch = SUtils.dc(schoolRemoteService.findById(unitId), School.class);
		//String[] sections = sch.getSections().split(",");
		//取到这个学校所有的有效班级 
		//该方法注释有误，取学年下未毕业的班级（包括预毕业班）
		List<Clazz> clazzs = SUtils.dt(classRemoteService.findBySchoolIdCurAcadyear(unitId, acadyear), new TR<List<Clazz>>(){}); 
		Map<String,Integer> classIdOfSeMap=EntityUtils.getMap(clazzs, "id", "section");
		Set<String> clsIds = EntityUtils.getSet(clazzs, "id");
		List<DyAllCheck> insertList=new ArrayList<DyAllCheck>();
		DyAllCheck allCheck=null;
		for(String classId:clsIds){
			allCheck=new DyAllCheck();
			allCheck.setSchoolId(unitId);
			allCheck.setAcadyear(acadyear);
			allCheck.setSemester(semester);
			allCheck.setClassId(classId);
			allCheck.setWeek(week);
			allCheck.setSection(classIdOfSeMap.get(classId));
			DyClassstatWeek classStat=classStatMap.get(classId);
			if(classStat!=null){
				if(classStat.getIsHealthExcellen()){
					allCheck.setHealthExcellentScore(2f);//卫生奖励分
				}
				allCheck.setHealthScore(classStat.getHealthScore());
				if(classStat.getIsDisciplineExcellen()){
					allCheck.setDisExcellentScore(2f);//纪律奖励分
				}
				allCheck.setDisciplineScore(classStat.getDisciplineScore());//纪律总分
			}
			setProperty(allCheck, resultListMap.get(classId));//寝室得分、文明寝室奖励分
			Float studentDecScore=punishMap.get(classId);
			allCheck.setStudentDecScore(studentDecScore==null?0f:studentDecScore);//个人扣分
			List<DyClassOtherCheck> inOtherList=otherMap.get(classId);
			if(CollectionUtils.isNotEmpty(inOtherList)){
				float otherScore=0f;
				for(DyClassOtherCheck inOther:inOtherList){
					otherScore+=inOther.getScore();
				}
				allCheck.setOtherScorer(otherScore);//班级其他考核分
			}
			insertList.add(allCheck);
			
			setAllScore(allCheck);
		}
		dyAllCheckDao.deleteBy(unitId, acadyear, semester, week);
		
		Map<Integer,List<DyAllCheck>> insertMap=new HashMap<Integer,List<DyAllCheck>>();
		if(CollectionUtils.isNotEmpty(insertList)){
			List<DyAllCheck> insertListLast=new ArrayList<DyAllCheck>();
			for(DyAllCheck insert:insertList){
				if(!insertMap.containsKey(insert.getSection())){
					insertMap.put(insert.getSection(), new ArrayList<DyAllCheck>());
				}
				insertMap.get(insert.getSection()).add(insert);
			}
			for(Entry<Integer,List<DyAllCheck>> entry:insertMap.entrySet()){
				List<DyAllCheck> oneInsertList=entry.getValue();
				Collections.sort(oneInsertList, new Comparator<DyAllCheck>() {
					@Override
					public int compare(DyAllCheck o1, DyAllCheck o2) {
						if(o2.getAllScore()<o1.getAllScore()) {
							return -1;
						}else if(o2.getAllScore()>o1.getAllScore()) {
							return 1;
						}else {
							return 0;
						}
//						return (int)((o2.getAllScore()-o1.getAllScore())*10);
//						return ((int)o2.getAllScore()*10)-((int)o1.getAllScore()*10);
					}
				});
				for(int i=0;i<oneInsertList.size();i++){
					DyAllCheck check=oneInsertList.get(i);
					if(i>0 && ((int)(check.getAllScore()*10))==((int)(oneInsertList.get(i-1).getAllScore()*10))){
						check.setAllRank(oneInsertList.get(i-1).getAllRank());
					}else{
						check.setAllRank(i+1);
					}
					insertListLast.add(check);
				}
			}
			dyAllCheckDao.saveAll(checkSave(insertListLast.toArray(new DyAllCheck[0])));
		}
	}
	//综合素质分 = （文明分+文明奖励分+记录分+记录奖励分）*0.5+（寝室分+文明寝室分）*0.2-学生个人扣分*0.1+班级其它考核得分
	public void setAllScore(DyAllCheck allCheck){
		double allScore=((allCheck.getHealthScore()+allCheck.getHealthExcellentScore()+allCheck.getDisciplineScore()+allCheck.getDisExcellentScore())*0.5+
			(allCheck.getDormScore()+allCheck.getDormExcellentScore())*0.2-allCheck.getStudentDecScore()*0.1+allCheck.getOtherScorer());
		float price=(float)allScore;
//		float num=(float)(Math.round(price*10)/10);
		DecimalFormat decimalFormat=new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足1位,会以0补足.
		String p=decimalFormat.format(price);//format 返回的是字符串
		allCheck.setAllScore(NumberUtils.toFloat(p));
	}
	
	public void setProperty(DyAllCheck allCheck,List<DyDormStatResult> resultList){
		Float scoreAverage=new Float(0);
		Float rewardAverage=new Float(0);
		if(CollectionUtils.isNotEmpty(resultList)){
			int lengtgh=resultList.size();
			for(DyDormStatResult stat:resultList){
				scoreAverage+=stat.getScoreALL();
				rewardAverage+=stat.getRewardAll();
			}
			scoreAverage=scoreAverage/lengtgh;
			rewardAverage=rewardAverage/lengtgh;
		}
		allCheck.setDormScore(scoreAverage);
		allCheck.setDormExcellentScore(rewardAverage);
	}
	
	@Override
	protected BaseJpaRepositoryDao<DyAllCheck, String> getJpaDao() {
		return dyAllCheckDao;
	}

	@Override
	protected Class<DyAllCheck> getEntityClass() {
		return DyAllCheck.class;
	}

}
