package net.zdsoft.stuwork.data.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.dao.DyClassstatWeekDao;
import net.zdsoft.stuwork.data.dto.DyClassStatListDto;
import net.zdsoft.stuwork.data.dto.DyWeekCheckResultDto;
import net.zdsoft.stuwork.data.entity.DyClassstatWeek;
import net.zdsoft.stuwork.data.entity.DyCourseRecord;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItem;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItemRole;
import net.zdsoft.stuwork.data.entity.DyWeekCheckResult;
import net.zdsoft.stuwork.data.entity.DyWeekCheckResultSubmit;
import net.zdsoft.stuwork.data.service.DyClassstatWeekService;
import net.zdsoft.stuwork.data.service.DyCourseRecordService;
import net.zdsoft.stuwork.data.service.DyCourseStudentRecordService;
import net.zdsoft.stuwork.data.service.DyWeekCheckItemService;
import net.zdsoft.stuwork.data.service.DyWeekCheckResultService;
import net.zdsoft.stuwork.data.service.DyWeekCheckResultSubmitService;
@Service("dyClassstatWeekService")
public class DyClassstatWeekServiceImpl extends BaseServiceImpl<DyClassstatWeek, String> implements DyClassstatWeekService{
    @Autowired
	private DyClassstatWeekDao dyClassstatWeekDao;
    @Autowired
    private DyCourseStudentRecordService dyCourseStudentRecordService;
    @Autowired
    private DyWeekCheckResultService dyWeekCheckResultService;
    @Autowired
    private DyWeekCheckItemService dyWeekCheckItemService;
    @Autowired
    private DyWeekCheckResultSubmitService dyWeekCheckResultSubmitService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private DateInfoRemoteService dateInfoRemoteService;
	@Override
	protected BaseJpaRepositoryDao<DyClassstatWeek, String> getJpaDao() {
		return dyClassstatWeekDao;
	}

	@Override
	protected Class<DyClassstatWeek> getEntityClass() {
		return DyClassstatWeek.class;
	}
	
	@Override
	public List<DyClassstatWeek> findBySchoolIdAndAcadyearAndSemesterAndWeek(
			String schoolId, String acadyear, String semester, int week) {
		return dyClassstatWeekDao.findBySchoolIdAndAcadyearAndSemesterAndWeek(schoolId,acadyear,semester, week);
	}
	@Override
	public DyClassstatWeek findBySchoolIdAndAcadyearAndSemesterAndClassIdAndWeek(
			String schoolId, String acadyear, String semester, String classId, int week) {
		return dyClassstatWeekDao.findBySchoolIdAndAcadyearAndSemesterAndClassIdAndWeek(schoolId,acadyear,semester,classId, week);
	}

	public DyClassstatWeek findBySchoolIdAndAcadyearAndSemesterAndClassIdAndDate(
			String schoolId, String acadyear, String semester, String classId, Date date) {
		if(date == null) {
			date = new Date();
		}
		DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(schoolId, acadyear, NumberUtils.toInt(semester), date),DateInfo.class);
		if(dateInfo == null || dateInfo.getWeek() < 2) {
			//不存在上周则直接为null
			return null;
		}
		int week = dateInfo.getWeek()-1;
		return dyClassstatWeekDao.findBySchoolIdAndAcadyearAndSemesterAndClassIdAndWeek(schoolId,acadyear,semester,classId, week);
	}
	@Override
	public void statByUnitId(String unitId, String acadyear, String semester,
			int week) {
		//TODO
		School sch = SUtils.dc(schoolRemoteService.findOneById(unitId), School.class);
		String[] sections = sch.getSections().split(",");
		//取到这个学校所有的有效班级 
		//该方法注释有误，取学年下未毕业的班级（包括预毕业班）
		List<Clazz> clazzs = SUtils.dt(classRemoteService.findBySchoolIdCurAcadyear(unitId, acadyear), new TR<List<Clazz>>(){}); 
		Set<String> clsIds = EntityUtils.getSet(clazzs, Clazz::getId);
		//key:section value: List<Class>
		Map<String,Set<String>> clsMap = new HashMap<String, Set<String>>();
		for(Clazz c : clazzs){
			if(clsMap.containsKey(c.getSection()+"")){
				clsMap.get(c.getSection()+"").add(c.getId());
			}else{
				clsMap.put(c.getSection()+"", new HashSet<String>());
				clsMap.get(c.getSection()+"").add(c.getId());
			}
		}
		//取到班级的上课日志信息
//		Map<String,Float> stuRecordMap = dyCourseStudentRecordService.findByClassIdAndWeek(
//				unitId,acadyear,semester,clsIds.toArray(new String[0]),week);
		
		List<DyCourseRecord> records = dyCourseRecordService.findListByRecordClassIds(unitId, acadyear, semester, week, clsIds.toArray(new String[0]));
		Map<String,Float> stuRecordMap = new HashMap<>();
		for(DyCourseRecord re : records){
			if(!stuRecordMap.containsKey(re.getClassId()+","+re.getType()+","+re.getDay())){
				stuRecordMap.put(re.getClassId()+","+re.getType()+","+re.getDay(), 0f);
			}
			stuRecordMap.put(re.getClassId()+","+re.getType()+","+re.getDay(), 
					stuRecordMap.get(re.getClassId()+","+re.getType()+","+re.getDay()) +re.getScore());
		}
		
		//再取到值周考核数据
		List<DyWeekCheckResult> resultlist = dyWeekCheckResultService.findByWeekAndInClassId(
				unitId,acadyear,semester,week,clsIds.toArray(new String[0]));
		//获得考核项信息
		List<DyWeekCheckItem> items = dyWeekCheckItemService.findBySchoolId(unitId);
		List<DyWeekCheckResultSubmit> sublist = dyWeekCheckResultSubmitService.findByWeek(unitId,acadyear,semester,week);
		//key:roleType+","+day
		Map<String,DyWeekCheckResultSubmit> subMap = new HashMap<String, DyWeekCheckResultSubmit>();
		for(DyWeekCheckResultSubmit sub : sublist){
			subMap.put(sub.getRoleType()+","+sub.getDay(), sub);
		}
		
		//用来放各个班级一周的考核总分
		Map<String,Float> healthMap = new HashMap<String, Float>();
		Map<String,Float> disciplineMap = new HashMap<String, Float>();
		float healthScore = 0f;
		float disciplineScore = 0f;
		for(String classId : clsIds){
			//组装各个班级的考核数据
			healthScore = 0f;
			disciplineScore = 0f;
			//先计算上课日志的分数,上课日志的分数记录到纪律总分中
			for(int i = 1;i<3;i++){
				for(int day = 1;day < 8;day++){
					if(stuRecordMap.containsKey(classId+","+i+","+day)){
						disciplineScore = disciplineScore + stuRecordMap.get(classId+","+i+","+day);
					}
				}
			}
			//计算出每一个项目在每一天，每一个考核角色的扣分情况
			for(DyWeekCheckItem item : items){
				for(int day = 1;day < 8;day++){
					//先判断该天有没有该考核项，没有计0分
					if(!item.getDays().contains(day+"")){
						continue;
					}
					//判断：如果所有考核角色都没有提交 则该考核项计0分
					float dayScore = item.getTotalScore();
					boolean roleAllNoSubmint = true;
					for(DyWeekCheckItemRole itemRole : item.getItemRoles()){
						for(DyWeekCheckResult result : resultlist){
							//如果有扣分则减去
							if(StringUtils.equals(item.getId(), result.getItemId()) && StringUtils.equals(result.getClassId(), classId)
									&& StringUtils.equals(itemRole.getRoleType(), result.getRoleType())
									&&result.getDay() == day){
								roleAllNoSubmint = false;
								 dayScore = dayScore- result.getScore();
							}
						}
						//如果结果表中找不到就去提交表中查，如果提交表中有当天改角色的提交记录，
						//则表明该角色对这个班级的这个item不扣分
						if(roleAllNoSubmint){
							if(subMap.containsKey(itemRole.getRoleType()+","+day)){
								roleAllNoSubmint = false;
							}
						}
					}
					if(!roleAllNoSubmint){
						//先将item总分先记录进对应的一周总分项
						if(item.getType() == 1){
							healthScore = healthScore + dayScore;
						}else{
							disciplineScore =disciplineScore +  dayScore;
						}
					}
				}
			}
			healthMap.put(classId, healthScore);
			disciplineMap.put(classId, disciplineScore);
		}
		//组装数据
		List<DyClassstatWeek> statlist = new ArrayList<DyClassstatWeek>();
		DyClassstatWeek statWeek = null;
		for(String clsId : clsIds){
			statWeek = new DyClassstatWeek();
			statWeek.setAcadyear(acadyear);
			statWeek.setSemester(semester);
			statWeek.setClassId(clsId);
			statWeek.setSchoolId(unitId);
			statWeek.setWeek(week);
			if(healthMap.containsKey(clsId))
				statWeek.setHealthScore(healthMap.get(clsId));
			else
				statWeek.setHealthScore(0f);
			if(disciplineMap.containsKey(clsId))
				statWeek.setDisciplineScore(disciplineMap.get(clsId));
			else
				statWeek.setDisciplineScore(0f);
			statlist.add(statWeek);
		}
		//TODO
		//分学段 -- 计算纪律排名
		//先按照分数从高到底排序
		Collections.sort(statlist, new Comparator<DyClassstatWeek>() {
			@Override
			public int compare(DyClassstatWeek o1, DyClassstatWeek o2) {
				if(o1 == null) {
					return 1;
				}
				if(o2 == null) {
					return -1;
				}
				if(o1.getDisciplineScore()>o2.getDisciplineScore()){
					return -1;
				}else if(o1.getDisciplineScore() == o2.getDisciplineScore()) {
					return 0;
				}else{
					return 1;
				}
			}
		});
		for(String sec : sections){
			Set<String> classIds = clsMap.get(sec);
			if(CollectionUtils.isEmpty(classIds)){
				continue;
			}
			int disciplineRank = 0;
			float disciplineRankScore  =0f;
			for(DyClassstatWeek stat : statlist){
				if(!classIds.contains(stat.getClassId())){
					continue;
				}
				stat.setSection(sec);
				if(disciplineRank == 0){
					disciplineRank = 1;
				}else{
					if(stat.getDisciplineScore() < disciplineRankScore){
						disciplineRank ++;
					}
				}
				disciplineRankScore = stat.getDisciplineScore();
				stat.setDisciplineRank(disciplineRank);
			}
			
			//计算可以发放多少面小红旗
			//规则：排名总数（不是班级数，是最低的名次）*30%,不满1取排名第一的班级，大于1则取排名在这个值之前的班级
			float disciplineExcellenRank = disciplineRank * 0.3f;
			for(DyClassstatWeek stat : statlist){
				if(!classIds.contains(stat.getClassId())){
					continue;
				}
				stat.setIsDisciplineExcellen(false);
				if(disciplineExcellenRank < 1){
					if(stat.getDisciplineRank() == 1){
						stat.setIsDisciplineExcellen(true);
					}
				}else{
					if(stat.getDisciplineRank() <= disciplineExcellenRank){
						stat.setIsDisciplineExcellen(true);
					}
				}
			}
		}
		
		//计算卫生排名
		Collections.sort(statlist, new Comparator<DyClassstatWeek>() {
			@Override
			public int compare(DyClassstatWeek o1, DyClassstatWeek o2) {
				if(o1.getHealthScore()>o2.getHealthScore()){
					return -1;
				}else{
					return 1;
				}
			}
		});
		for(String sec : sections){
			Set<String> classIds = clsMap.get(sec);
			if(CollectionUtils.isEmpty(classIds)){
				continue;
			}
			int healthRank = 0;
			float healthRankScore = 0f;
			for(DyClassstatWeek stat : statlist){
				if(!classIds.contains(stat.getClassId())){
					continue;
				}
				stat.setSection(sec);
				if(healthRank == 0){
					healthRank = 1;
				}else{
					if(stat.getHealthScore() < healthRankScore){
						healthRank ++;
					}
				}
				healthRankScore = stat.getHealthScore();
				stat.setHealthRank(healthRank);
			}
			float healthExcellenRank = healthRank * 0.3f;
			for(DyClassstatWeek stat : statlist){
				if(!classIds.contains(stat.getClassId())){
					continue;
				}
				stat.setIsHealthExcellen(false);
				if(healthExcellenRank < 1f){
					if(stat.getHealthRank() == 1){
						stat.setIsHealthExcellen(true);
					}
				}else{
					if(stat.getHealthRank()<= healthExcellenRank){
						stat.setIsHealthExcellen(true);
					}
				}
			}
		}
		
		//先删除之前本周的统计结果,再保存新的统计结果
		dyClassstatWeekDao.deleteByWeek(unitId,acadyear,semester,week);
		if(CollectionUtils.isNotEmpty(statlist)){
			DyClassstatWeek[] stats = statlist.toArray(new DyClassstatWeek[0]);
			checkSave(stats);
			saveAll(stats);
		}
	}
	@Autowired
	private DyCourseRecordService dyCourseRecordService;
	
	@Override
	public List<DyClassStatListDto> findClassTableDto(String unitId,
			String acadyear, String semester, String classId, int week) {
		List<DyClassStatListDto> dtolist = new ArrayList<DyClassStatListDto>();
		//先取到班级日志数据
//		Map<String,Float> stuRecordMap = dyCourseStudentRecordService.findByClassIdAndWeek(unitId,acadyear,semester,new String[]{classId},week);
		List<DyCourseRecord> records = dyCourseRecordService.findListByRecordClassId(unitId, acadyear, semester, week, -1, "1", classId);
		Map<String,Float> stuRecordMap = new HashMap<>();
		for(DyCourseRecord re : records){
			if(!stuRecordMap.containsKey(re.getClassId()+","+re.getType()+","+re.getDay())){
				stuRecordMap.put(re.getClassId()+","+re.getType()+","+re.getDay(), 0f);
			}
			stuRecordMap.put(re.getClassId()+","+re.getType()+","+re.getDay(), 
					stuRecordMap.get(re.getClassId()+","+re.getType()+","+re.getDay()) +re.getScore());
		}
		List<DyCourseRecord> records2 = dyCourseRecordService.findListByRecordClassId(unitId, acadyear, semester, week, -1, "2", classId);
		for(DyCourseRecord re : records2){
			if(!stuRecordMap.containsKey(re.getClassId()+","+re.getType()+","+re.getDay())){
				stuRecordMap.put(re.getClassId()+","+re.getType()+","+re.getDay(), 0f);
			}
			stuRecordMap.put(re.getClassId()+","+re.getType()+","+re.getDay(), 
					stuRecordMap.get(re.getClassId()+","+re.getType()+","+re.getDay()) +re.getScore());
		}
		Map<String,String> stuRecordRemarkMap = dyCourseStudentRecordService.findByClassIdAndWeekRemark(unitId, acadyear, semester, new String[]{classId}, week);
		//再取到值周考核数据
		List<DyWeekCheckResult> resultlist = dyWeekCheckResultService.findByWeekAndInClassId(unitId,acadyear,semester,week,new String[]{classId});
		List<DyWeekCheckResultSubmit> sublist = dyWeekCheckResultSubmitService.findByWeek(unitId,acadyear,semester,week);
		//key:roleType+","+day
		Map<String,DyWeekCheckResultSubmit> subMap = new HashMap<String, DyWeekCheckResultSubmit>();
		for(DyWeekCheckResultSubmit sub : sublist){
			subMap.put(sub.getRoleType()+","+sub.getDay(), sub);
		}
		//获得考核项信息
		List<DyWeekCheckItem>items = dyWeekCheckItemService.findBySchoolId(unitId);
		if(CollectionUtils.isEmpty(items)){
			return dtolist;
		}
		float dayScore = 0f;
		DyWeekCheckResultDto dto = null;
		DyClassStatListDto listDto  =null;
		for(int i = 1;i<3;i++){
			listDto = new DyClassStatListDto();
			if(i==1 ){
				listDto.setItemName("上课日志");
			}else{
				listDto.setItemName("晚自习日志");
			}
			listDto.setItemType("3");
			for(int day = 1;day < 8;day++){
				dto = new DyWeekCheckResultDto();
				dto.setDay(day);
				if(stuRecordMap.containsKey(classId+","+i+","+day)){
					dto.setScore(stuRecordMap.get(classId+","+i+","+day));
				}else{
					dto.setUnCheck(true);
				}
				if(stuRecordRemarkMap.containsKey(classId+","+i+","+day)){
					dto.setRemark(stuRecordRemarkMap.get(classId+","+i+","+day));
				}
				listDto.getDtos().add(dto);
			}
			dtolist.add(listDto);
		}
		boolean roleAllNoSubmint = true;//一个项目是否所有角色都没有提交
		boolean roleSub = false;//一个角色是否提交
		for(DyWeekCheckItem item : items){
			listDto = new DyClassStatListDto();
			listDto.setItemId(item.getId());
			listDto.setItemName(item.getItemName());
			listDto.setItemType(item.getType()+"");
			for(int day = 1;day < 8;day++){
				dto = new DyWeekCheckResultDto();
				dto.setItemId(item.getId());
				dto.setItemName(item.getItemName());
				dto.setItemOrder(item.getOrderId());
				dto.setWeek(week);
				dto.setDay(day);
				dto.setItemType(item.getType());
				if(item.getHasTotalScore() == 1){
					dayScore = item.getTotalScore();
				}else{
					dayScore = 0f;
				}
				//TODO
				if(!item.getDays().contains(day+"")){
					dto.setUnCheck(true);
				}else{
					dto.setUnCheck(false);
					roleAllNoSubmint = true;
					for(DyWeekCheckItemRole itemRole : item.getItemRoles()){
						String remark="";
						roleSub = false;
						//先去结果表中找角色的扣分情况，并计算总分
						for(DyWeekCheckResult result : resultlist){
							if(StringUtils.equals(item.getId(), result.getItemId()) 
									&& StringUtils.equals(itemRole.getRoleType(), result.getRoleType())
									&&result.getDay() == day){
								roleSub = true;
								dayScore = dayScore - result.getScore();
								result.setRoleName(itemRole.getRoleName());
								dto.getResult().add(result);
							}
							if(StringUtils.equals(item.getId(), result.getItemId()) 
									&&result.getDay() == day){
								if(null!=result.getRemark()){									
									remark=remark+result.getRemark()+",";
								}
							}
						}
						if(StringUtils.isNotBlank(remark)){
							dto.setRemark("（备注："+remark.substring(0, remark.length()-1)+"）");
						}
						//如果结果表中找不到就去提交表中查，如果提交表中有当天改角色的提交记录，
						//则表明该角色对这个班级的这个item不扣分
						if(!roleSub){
							if(!subMap.containsKey(itemRole.getRoleType()+","+day)){
								dto.getUnSubRole().add(itemRole.getRoleName());//记录下没有提交的角色
							}else{
								roleAllNoSubmint = false;
							}
						}else{
							roleAllNoSubmint = false;
						}
					}
					dto.setScore(dayScore);
					
					if(roleAllNoSubmint){
						dto.setAllUnSubmint(true);
					}else{
						dto.setAllUnSubmint(false);
					}
				}
				listDto.getDtos().add(dto);
			}
			dtolist.add(listDto);
		}
		return dtolist;
	}

	@Override
	public List<DyClassstatWeek> findRankingList(String schoolId, String acadyear, String semester, Integer[] weeks,
			String[] classIds) {
		// TODO Auto-generated method stub
		return dyClassstatWeekDao.findRankingList(schoolId,acadyear,semester,weeks,classIds);
	}
}
