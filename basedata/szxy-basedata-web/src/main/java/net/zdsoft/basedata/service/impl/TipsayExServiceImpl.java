package net.zdsoft.basedata.service.impl;

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
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.constant.TipsayConstants;
import net.zdsoft.basedata.dao.TipsayExDao;
import net.zdsoft.basedata.dto.TimetableChangeDto;
import net.zdsoft.basedata.entity.Adjusted;
import net.zdsoft.basedata.entity.AdjustedDetail;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Tipsay;
import net.zdsoft.basedata.entity.TipsayEx;
import net.zdsoft.basedata.service.AdjustedDetailService;
import net.zdsoft.basedata.service.AdjustedService;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.DateInfoService;
import net.zdsoft.basedata.service.DeptService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.basedata.service.TipsayExService;
import net.zdsoft.basedata.service.TipsayService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
@Service("tipsayExService")
public class TipsayExServiceImpl extends BaseServiceImpl<TipsayEx, String>
	implements TipsayExService{
	@Autowired
	private TipsayExDao tipsayExDao;
	@Autowired
	private TipsayService tipsayService;
	@Autowired
	private AdjustedService adjustedService;
	@Autowired
	private AdjustedDetailService adjustedDetailService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private DeptService deptService;
	@Autowired
	private ClassService classService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private TeachClassService teachClassService;
	@Autowired
	private DateInfoService dateInfoService;
	@Autowired
	private CourseScheduleService courseScheduleService;
	
	@Override
	protected BaseJpaRepositoryDao<TipsayEx, String> getJpaDao() {
		return tipsayExDao;
	}

	@Override
	protected Class<TipsayEx> getEntityClass() {
		return TipsayEx.class;
	}

	@Override
	public List<TimetableChangeDto> findDtoListByMonthCondition(String schoolId, String acadyear, Integer semester, String[] dates, String teacherName) {
		List<TimetableChangeDto> dtoList = new ArrayList<TimetableChangeDto>();
		String[] teacherIds = null;
		List<Teacher> teacherList = new ArrayList<Teacher>();
		if(StringUtils.isNotBlank(teacherName)){
			teacherList = teacherService.findListByTeacherName(schoolId, teacherName);
			if(CollectionUtils.isEmpty(teacherList)){
				return dtoList;
			}
			teacherIds = EntityUtils.getList(teacherList, Teacher::getId).toArray(new String[0]);
		}
		List<Tipsay> tipsayList = tipsayService.findByCondition(schoolId,acadyear,semester,dates,null,teacherIds);
		if(CollectionUtils.isEmpty(tipsayList)){
			return dtoList;
		}
		Set<String> teacherIdSet = new HashSet<String>();
		for (Tipsay tipsay : tipsayList) {
			teacherIdSet.add(tipsay.getNewTeacherId());
			teacherIdSet.add(tipsay.getTeacherId());
			if(StringUtils.isNotBlank(tipsay.getTeacherExIds())){
				teacherIdSet.addAll(Arrays.asList(tipsay.getTeacherExIds().split(",")));
			}
		}
		if(StringUtils.isNotBlank(teacherName)){
			teacherList = teacherList.parallelStream().filter(e->teacherIdSet.contains(e.getId())).collect(Collectors.toList());
		}else{
			teacherList = teacherService.findListByIds(teacherIdSet.toArray(new String[0]));
		}
		Map<String, Dept> deptMap = deptService.findMapByIdIn(EntityUtils.getSet(teacherList, Teacher::getDeptId).toArray(new String[0]));
		TimetableChangeDto dto = null;
		for (Teacher teacher : teacherList) {
			dto = new TimetableChangeDto();
			dto.setDeptId(teacher.getDeptId());
			Dept dept = deptMap.get(teacher.getDeptId());
			dto.setDeptName(dept==null?"":dept.getDeptName());
			if(dept!=null && dept.getDisplayOrder()!=null){
				dto.setDeptOrder(dept.getDisplayOrder());
			}
			dto.setTeacherId(teacher.getId());
			dto.setTeacherName(teacher.getTeacherName());
			dto.setTeacherOrder(teacher.getDisplayOrder());
			for (Tipsay tipsay : tipsayList) {
				if(tipsay.getNewTeacherId().equals(teacher.getId())){
					if(TipsayConstants.TYPE_1.equals(tipsay.getType())){
						dto.setTakeNum(dto.getTakeNum()+1);
					}else if(TipsayConstants.TYPE_2.equals(tipsay.getType())){
						dto.setManNum(dto.getManNum()+1);
					}
				}
				if(tipsay.getTeacherId().equals(teacher.getId()) || (StringUtils.isNotBlank(tipsay.getTeacherExIds())&& tipsay.getTeacherExIds().contains(teacher.getId()))){
					if(TipsayConstants.TYPE_1.equals(tipsay.getType())){
						dto.setBeTakeNum(dto.getBeTakeNum()+1);
					}else if(TipsayConstants.TYPE_2.equals(tipsay.getType())){
						dto.setBeManNum(dto.getBeManNum()+1);
					}
				}
			}
			dtoList.add(dto);
		}
		
		Collections.sort(dtoList,new Comparator<TimetableChangeDto>() {

			@Override
			public int compare(TimetableChangeDto o1, TimetableChangeDto o2) {
				if(o1.getDeptOrder()==null || o2.getDeptOrder()==null){
					return 0;
				}
				if(o1.getDeptOrder()==o2.getDeptOrder()){
					if(o1.getDeptName()==null || o2.getDeptName()==null){
						return 0;
					}
					if(o1.getDeptName().equals(o2.getDeptName())){
						if(o1.getTeacherOrder()==null || o2.getTeacherOrder()==null){
							return 0;
						}
						if(o1.getTeacherOrder()==o2.getTeacherOrder()){
							if(o1.getTeacherName()==null || o2.getTeacherName()==null){
								return 0;
							}
							return o1.getTeacherName().compareTo(o2.getTeacherName());
						}
						return o1.getTeacherOrder()-o2.getTeacherOrder();
					}
					return o1.getDeptName().compareTo(o2.getDeptName());
				}
				return o1.getDeptOrder()-o2.getDeptOrder();
			}
		});
		
		return dtoList;
	}

	@Override
	public List<TimetableChangeDto> findDtoListByDayCondition(String schoolId, String acadyear, Integer semester, String[] dates, String type, Map<String,String> dateWeekMap) {
		List<TimetableChangeDto> dtoList = new ArrayList<TimetableChangeDto>();
		Set<String> classIdSet = new HashSet<String>();
		Set<String> teacherIdSet = new HashSet<String>();
		Set<String> subjectIdSet = new HashSet<String>();
		Set<String> adjustedIdSet = new HashSet<String>();
		Set<String> adjustedDetailIdSet = new HashSet<String>();
		TimetableChangeDto dto = null;
		List<Tipsay> tipsayList = null;
		List<AdjustedDetail> adjustedDetailList = null;
		Map<String,String> adjustingMap = null;
		Map<String,Adjusted> adjustedMap = null;
		Map<String, AdjustedDetail> adjustedDetailMap = null;
		if(StringUtils.isBlank(type) || "1".equals(type) || "2".equals(type)){
			tipsayList = tipsayService.findByCondition(schoolId,acadyear,semester,dates,type,null);
			for (Tipsay tipsay : tipsayList) {
				classIdSet.add(tipsay.getClassId());
				teacherIdSet.add(tipsay.getTeacherId());
				teacherIdSet.add(tipsay.getNewTeacherId());
				if(StringUtils.isNotBlank(tipsay.getTeacherExIds())){
					teacherIdSet.addAll(Arrays.asList(tipsay.getTeacherExIds().split(",")));
				}
				subjectIdSet.add(tipsay.getSubjectId());
			}
		}
		if(StringUtils.isBlank(type) || "3".equals(type)){
			adjustedDetailList = adjustedDetailService.findByCondition(schoolId, acadyear, semester, dates, null);
			for (AdjustedDetail adjustedDetail : adjustedDetailList) {
				adjustedIdSet.add(adjustedDetail.getAdjustedId());
			}
			List<Adjusted> adjustedList = adjustedService.findListByIds(adjustedIdSet.toArray(new String[adjustedIdSet.size()]));
			adjustingMap = adjustedDetailService.findMapByAdjustedIdIn(adjustedIdSet.toArray(new String[0]));
			adjustedMap = EntityUtils.getMap(adjustedList, Adjusted::getId);
			adjustedDetailIdSet.addAll(adjustingMap.keySet());
			List<AdjustedDetail> adjustedDetailList2 = adjustedDetailService.findListByIds(adjustedDetailIdSet.toArray(new String[adjustedDetailIdSet.size()]));
			for (AdjustedDetail adjustedDetail : adjustedDetailList2) {
				classIdSet.add(adjustedDetail.getClassId());
				teacherIdSet.add(adjustedDetail.getTeacherId());
				if(StringUtils.isNotBlank(adjustedDetail.getTeacherExIds())){
					teacherIdSet.addAll(Arrays.asList(adjustedDetail.getTeacherExIds().split(",")));
				}
				subjectIdSet.add(adjustedDetail.getSubjectId());
			}
			adjustedDetailMap = EntityUtils.getMap(adjustedDetailList2, AdjustedDetail::getId);
		}
		List<Teacher> teacherList = teacherService.findListByIds(teacherIdSet.toArray(new String[teacherIdSet.size()]));
		Map<String, String> teacherNameMap = EntityUtils.getMap(teacherList, Teacher::getId,Teacher::getTeacherName);
		List<Clazz> classList = classService.findListByIds(classIdSet.toArray(new String[classIdSet.size()]));
		List<TeachClass> teachClassList = teachClassService.findListByIds(classIdSet.toArray(new String[classIdSet.size()]));
		Map<String, String> classNameMap = EntityUtils.getMap(classList, Clazz::getId, Clazz::getClassNameDynamic);
		classNameMap.putAll(EntityUtils.getMap(teachClassList, TeachClass::getId, TeachClass::getName));
		List<Course> subjectList = courseService.findListByIds(subjectIdSet.toArray(new String[subjectIdSet.size()]));
		Map<String, String> subjectNameMap = EntityUtils.getMap(subjectList, Course::getId,Course::getSubjectName);
		if(CollectionUtils.isNotEmpty(adjustedDetailList)){
			for (AdjustedDetail adjustedDetail : adjustedDetailList) {
				AdjustedDetail item = adjustedDetailMap.get(adjustingMap.get(adjustedDetail.getId()));
				if(item==null || StringUtils.isBlank(item.getClassId())){
					continue;
				}
				dto = new TimetableChangeDto();
				dto.setClassId(item.getClassId());
				dto.setClassName(classNameMap.containsKey(item.getClassId())?classNameMap.get(item.getClassId()):"");
				String teacherName = teacherNameMap.containsKey(item.getTeacherId())?teacherNameMap.get(item.getTeacherId()):"";
				if(StringUtils.isNotBlank(item.getTeacherExIds())){
					String[] teacherExIds = item.getTeacherExIds().split(",");
					for (String id : teacherExIds) {
						if(teacherNameMap.containsKey(id)){
							teacherName+=","+teacherNameMap.get(id);
						}
					}
				}
				dto.setSubjectName(subjectNameMap.containsKey(item.getSubjectId())?subjectNameMap.get(item.getSubjectId()):"");
				dto.setTeacherName(teacherName);
				dto.setSearchDate(dateWeekMap.get(adjustedDetail.getWeekOfWorktime()+"_"+adjustedDetail.getDayOfWeek())+BaseConstants.dayOfWeekMap2.get(adjustedDetail.getDayOfWeek().toString()));
				StringBuilder changeStr = new StringBuilder();
				changeStr.append("原");
				changeStr.append(item.getWeekOfWorktime()).append("周 ");
				changeStr.append(BaseConstants.dayOfWeekMap2.get(item.getDayOfWeek().toString()));
				changeStr.append(BaseConstants.PERIOD_INTERVAL_Map.get(item.getPeriodInterval()));
				changeStr.append(" 第");
				changeStr.append(item.getPeriod()).append("节 ");
				changeStr.append(dto.getSubjectName()).append("课 调至");
				changeStr.append(adjustedDetail.getWeekOfWorktime()).append("周 ");
				changeStr.append(BaseConstants.dayOfWeekMap2.get(adjustedDetail.getDayOfWeek().toString()));
				changeStr.append(BaseConstants.PERIOD_INTERVAL_Map.get(adjustedDetail.getPeriodInterval()));
				changeStr.append(" 第");
				changeStr.append(adjustedDetail.getPeriod()).append("节课");
				dto.setChangeStr(changeStr.toString());
				dto.setType("调课");
				if(adjustedMap.containsKey(adjustedDetail.getAdjustedId())&&adjustedMap.get(adjustedDetail.getAdjustedId()).getRemark()!=null){
					dto.setRemark(adjustedMap.get(adjustedDetail.getAdjustedId()).getRemark());
				}
				dtoList.add(dto);
			}
		}
		if(CollectionUtils.isNotEmpty(tipsayList)){
			for (Tipsay tipsay : tipsayList) {
				dto = new TimetableChangeDto();
				dto.setClassId(tipsay.getClassId());
				dto.setClassName(classNameMap.containsKey(tipsay.getClassId())?classNameMap.get(tipsay.getClassId()):"");
				dto.setTeacherId(tipsay.getTeacherId());
				dto.setTeacherName(teacherNameMap.containsKey(tipsay.getNewTeacherId())?teacherNameMap.get(tipsay.getNewTeacherId()):"");
				dto.setSearchDate(dateWeekMap.get(tipsay.getWeekOfWorktime()+"_"+tipsay.getDayOfWeek())+BaseConstants.dayOfWeekMap2.get(tipsay.getDayOfWeek().toString()));
				StringBuilder changeStr = new StringBuilder();
				if(TipsayConstants.TYPE_1.equals(tipsay.getType())){
					changeStr.append("代");
					dto.setType("代课");
				}else if(TipsayConstants.TYPE_2.equals(tipsay.getType())){
					changeStr.append("管");
					dto.setType("管课");
				}
				changeStr.append(BaseConstants.PERIOD_INTERVAL_Map.get(tipsay.getPeriodInterval()));
				changeStr.append("第");
				changeStr.append(tipsay.getPeriod()).append("节");
				if(subjectNameMap.containsKey(tipsay.getSubjectId())){
					changeStr.append(subjectNameMap.get(tipsay.getSubjectId()));
				}
				changeStr.append("课");
				dto.setChangeStr(changeStr.toString());
				dto.setRemark(tipsay.getRemark());
				dtoList.add(dto);
			}
		}
		return dtoList;
	}

    @Override
    public void deleteByTipsayIds(String[] adjustedIds) {
    	if(adjustedIds == null || adjustedIds.length ==0)
    		return;
	    tipsayExDao.deleteByTipsayIds(adjustedIds);
    }

    @Override
    public void updateAuditorIdAndStateByAdjustId(String adjustedId, String state, String teacherId) {
        tipsayExDao.updateAuditorIdAndStateByAdjustId(adjustedId, state, teacherId);
    }

	@Override
	public void deleteByTeacherIds(String... teacherIds) {
		adjustedDetailService.deleteByTeacherIds(teacherIds);
		//tipsayService.updateByTeacherId(teacherIds);
	}

	@Override
	public void deleteByClassIds(String... classIds) {
//		tipsayExDao.deleteByClassIds(classIds);
//		tipsayService.deleteByClassIds(classIds);
//		adjustedService.deleteByClassId(classIds);
		adjustedDetailService.deleteByClassIds(classIds);
	}

	@Override
	public void deleteBySubjectIds(String... subjectIds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String findByAcadyearAndSemesterAndTeacherId(String acadyear, Integer semester, String teacherId) {
		int courseNum = 0;
		int takeNum = 0;
		int beTakeNum = 0;
		int adjustNum = 0;
		Teacher teacher = teacherService.findOne(teacherId);
		Date now = new Date();
		Date startDate = DateUtils.getMondayOfWeek(now);
		Date endDate = DateUtils.getSundayOfWeek(now);
		List<CourseSchedule> csList = courseScheduleService.findCourseScheduleListByPerId(acadyear, semester, DateUtils.date2String(startDate,"yyyyMMdd"), DateUtils.date2String(endDate,"yyyyMMdd"), teacherId, "1");
		if(CollectionUtils.isNotEmpty(csList)){
			courseNum = csList.size();
		}
		List<DateInfo> infoList = new ArrayList<DateInfo>();
		List<DateInfo> dateInfoList = dateInfoService.findByAcadyearAndSemester(teacher.getUnitId(), acadyear, semester);
		for(DateInfo item:dateInfoList){
    		if(DateUtils.compareForDay(item.getInfoDate(),startDate)>=0 && DateUtils.compareForDay(endDate, item.getInfoDate())>=0){
    			infoList.add(item);
    		}
    	}
		List<String> dateList=new ArrayList<String>();
		if(CollectionUtils.isNotEmpty(infoList)){
    		for(DateInfo dateInfo:infoList){
    			dateList.add(dateInfo.getWeek()+"_"+(dateInfo.getWeekday()-1));
    		}

    		List<Tipsay> tipsayList = tipsayService.findByCondition(teacher.getUnitId(),acadyear,semester,dateList.toArray(new String[dateList.size()]),null,new String[]{teacherId});
			for (Tipsay tipsay : tipsayList) {
				if(tipsay.getNewTeacherId().equals(teacher.getId())){
					takeNum++;
				}
				if(tipsay.getTeacherId().equals(teacher.getId()) || (StringUtils.isNotBlank(tipsay.getTeacherExIds())&& tipsay.getTeacherExIds().contains(teacher.getId()))){
					beTakeNum++;
				}
			}
			
			List<AdjustedDetail> adjustedDetailList = adjustedDetailService.findByCondition(teacher.getUnitId(), acadyear, semester
					,dateList.toArray(new String[dateList.size()]), new String[]{teacherId});
			if(CollectionUtils.isNotEmpty(adjustedDetailList)){
				adjustNum = adjustedDetailList.size();
			}
    	}	
		Json json = new Json();
		json.put("courseNum", courseNum);
		json.put("takeNum", takeNum);
		json.put("beTakeNum", beTakeNum);
		json.put("adjustNum", adjustNum);
			
		return json.toJSONString();
	}

	@Override
	public List<TimetableChangeDto> findDtoListByExportCondition(String unitId, String acadyear, Integer semester, String[] adjustedIds) {
		List<TimetableChangeDto> dtoList = new ArrayList<TimetableChangeDto>();
		
		List<DateInfo> dateInfoList = dateInfoService.findByAcadyearAndSemester(unitId, acadyear, semester);
		Map<String, String> dateInfoMap = new HashMap<String, String>();
		for(DateInfo dateInfo:dateInfoList){
			dateInfoMap.put(dateInfo.getWeek()+"_"+(dateInfo.getWeekday()), DateUtils.date2String(dateInfo.getInfoDate(), " MM 月 dd 日 "));
		}
		List<AdjustedDetail> adjustedDetailList = adjustedDetailService.findListByAdjustedIds(adjustedIds);
		Map<String, List<AdjustedDetail>> adjustedMap = EntityUtils.getListMap(adjustedDetailList, AdjustedDetail::getAdjustedId, Function.identity());
		Set<String> classIdSet = new HashSet<String>();
		Set<String> teacherIdSet = new HashSet<String>();
		Set<String> subjectIdSet = new HashSet<String>();
		for (AdjustedDetail adjustedDetail : adjustedDetailList) {
			classIdSet.add(adjustedDetail.getClassId());
			teacherIdSet.add(adjustedDetail.getTeacherId());
			if(StringUtils.isNotBlank(adjustedDetail.getTeacherExIds())){
				teacherIdSet.addAll(Arrays.asList(adjustedDetail.getTeacherExIds().split(",")));
			}
			subjectIdSet.add(adjustedDetail.getSubjectId());
		}
		List<Clazz> classList = classService.findListByIds(classIdSet.toArray(new String[classIdSet.size()]));
		Map<String, String> classTeacherMap = EntityUtils.getMap(
				classList.stream().filter(e->StringUtils.isNotBlank(e.getTeacherId())).collect(Collectors.toList()), Clazz::getId, Clazz::getTeacherId);
		teacherIdSet.addAll(classTeacherMap.values());
		List<TeachClass> teachClassList = teachClassService.findListByIds(classIdSet.toArray(new String[classIdSet.size()]));
		Map<String, String> classNameMap = EntityUtils.getMap(classList, Clazz::getId, Clazz::getClassNameDynamic);
		classNameMap.putAll(EntityUtils.getMap(teachClassList, TeachClass::getId, TeachClass::getName));
		List<Teacher> teacherList = teacherService.findListByIds(teacherIdSet.toArray(new String[teacherIdSet.size()]));
		Map<String, String> teacherNameMap = EntityUtils.getMap(teacherList, Teacher::getId,Teacher::getTeacherName);
		List<Course> subjectList = courseService.findListByIds(subjectIdSet.toArray(new String[subjectIdSet.size()]));
		Map<String, String> subjectNameMap = EntityUtils.getMap(subjectList, Course::getId,Course::getSubjectName);
		if(MapUtils.isNotEmpty(adjustedMap)){
			Map<String, TimetableChangeDto> dtoMap1 = new HashMap<String, TimetableChangeDto>();//存放教师,key-teacherId
			Map<String, TimetableChangeDto> dtoMap2 = new HashMap<String, TimetableChangeDto>();//存放班主任,key-classId
			
			TimetableChangeDto dto = null;
			for (Entry<String, List<AdjustedDetail>> entry : adjustedMap.entrySet()) {
				if(entry.getValue().size()!=2){
					continue;
				}
				AdjustedDetail item1 = entry.getValue().get(0);
				AdjustedDetail item2 = entry.getValue().get(1);
				String time1 = dateInfoMap.get(item1.getWeekOfWorktime()+"_"+(item1.getDayOfWeek()+1))+"("+dayOfWeekMap.get(item1.getDayOfWeek().toString())+")";//11月05日（星期五）
				String time2 = dateInfoMap.get(item2.getWeekOfWorktime()+"_"+(item2.getDayOfWeek()+1))+"("+dayOfWeekMap.get(item2.getDayOfWeek().toString())+")";//11月05日（星期五）
				String period1 = " "+BaseConstants.PERIOD_INTERVAL_Map.get(item1.getPeriodInterval())+" 第 "+item1.getPeriod()+" 节";
				String period2 = " "+BaseConstants.PERIOD_INTERVAL_Map.get(item2.getPeriodInterval())+" 第 "+item2.getPeriod()+" 节";
				String subjectName1 = (subjectNameMap.containsKey(item1.getSubjectId())?" "+subjectNameMap.get(item1.getSubjectId())+" ":" 自习 ")+"课";
				String subjectName2 = (subjectNameMap.containsKey(item2.getSubjectId())?" "+subjectNameMap.get(item2.getSubjectId())+" ":" 自习 ")+"课";
				String className1 = classNameMap.containsKey(item1.getClassId())?classNameMap.get(item1.getClassId()):null;
				String className2 = classNameMap.containsKey(item1.getClassId())?classNameMap.get(item2.getClassId()):null;
				
				if(className1 !=null){
					String content = time1+"， "+className1+" 的"+period1+subjectName1+"调至"+time2+period2;
					//老师
					if(StringUtils.isNotBlank(item1.getTeacherId()) && teacherNameMap.containsKey(item1.getTeacherId())){
						dto = dtoMap1.get(item1.getTeacherId());
						if(dto==null){
							dto = new TimetableChangeDto();
							dto.setType("1");
							dto.setTeacherName(teacherNameMap.get(item1.getTeacherId()));
							dtoMap1.put(item1.getTeacherId(), dto);
						}
						dto.getAdjustedList().add(content);
					}
					//助教老师
					if(StringUtils.isNotBlank(item1.getTeacherExIds())){
						String[] teacherExIds = item1.getTeacherExIds().split(",");
						for (String id : teacherExIds) {
							if(teacherNameMap.containsKey(id)){
								dto = dtoMap1.get(id);
								if(dto==null){
									dto = new TimetableChangeDto();
									dto.setType("1");
									dto.setTeacherName(teacherNameMap.get(id));
									dtoMap1.put(id, dto);
								}
								dto.getAdjustedList().add(content);
							}
						}
					}
					//班主任
					if(classTeacherMap.containsKey(item1.getClassId())){
						String teacherId = classTeacherMap.get(item1.getClassId());
						if(teacherNameMap.containsKey(teacherId)){
							String content2 = time1+"，"+period1+subjectName1+"改为"+subjectName2;
							dto = dtoMap2.get(item1.getClassId());
							if(dto==null){
								dto = new TimetableChangeDto();
								dto.setType("2");
								dto.setTeacherName(classNameMap.get(item1.getClassId())+" 班主任 "+teacherNameMap.get(teacherId));
								dtoMap2.put(item1.getClassId(), dto);
							}
							dto.getAdjustedList().add(content2);
						}
					}
				}
				if(className2 !=null){
					String content = time2+"， "+className2+" 的"+period2+subjectName2+"调至"+time1+period1;
					//老师
					if(StringUtils.isNotBlank(item2.getTeacherId()) && teacherNameMap.containsKey(item2.getTeacherId())){
						dto = dtoMap1.get(item2.getTeacherId());
						if(dto==null){
							dto = new TimetableChangeDto();
							dto.setType("1");
							dto.setTeacherName(teacherNameMap.get(item2.getTeacherId()));
							dtoMap1.put(item2.getTeacherId(), dto);
						}
						dto.getAdjustedList().add(content);
					}
					//助教老师
					if(StringUtils.isNotBlank(item2.getTeacherExIds())){
						String[] teacherExIds = item2.getTeacherExIds().split(",");
						for (String id : teacherExIds) {
							if(teacherNameMap.containsKey(id)){
								dto = dtoMap1.get(id);
								if(dto==null){
									dto = new TimetableChangeDto();
									dto.setType("1");
									dto.setTeacherName(teacherNameMap.get(id));
									dtoMap1.put(id, dto);
								}
								dto.getAdjustedList().add(content);
							}
						}
					}
					//班主任
					if(classTeacherMap.containsKey(item2.getClassId())){
						String teacherId = classTeacherMap.get(item2.getClassId());
						if(teacherNameMap.containsKey(teacherId)){
							String content2 = time2+"，"+period2+subjectName2+"改为"+subjectName1;
							dto = dtoMap2.get(item2.getClassId());
							if(dto==null){
								dto = new TimetableChangeDto();
								dto.setType("2");
								dto.setTeacherName(classNameMap.get(item2.getClassId())+" 班主任 "+teacherNameMap.get(teacherId));
								dtoMap2.put(item2.getClassId(), dto);
							}
							dto.getAdjustedList().add(content2);
						}
					}
				}
			}
			if(MapUtils.isNotEmpty(dtoMap1)){
				dtoList.addAll(dtoMap1.values());
			}
			if(MapUtils.isNotEmpty(dtoMap2)){
				dtoList.addAll(dtoMap2.values());
			}
		}
		
		return dtoList;
	}
	
	public static final Map<String,String> dayOfWeekMap=new HashMap<String,String>();
	static{
		dayOfWeekMap.put("0", "星期 一 ");
		dayOfWeekMap.put("1", "星期 二 ");
		dayOfWeekMap.put("2", "星期 三 ");
		dayOfWeekMap.put("3", "星期 四 ");
		dayOfWeekMap.put("4", "星期 五 ");
		dayOfWeekMap.put("5", "星期 六 ");
		dayOfWeekMap.put("6", "星期 天 ");
		
	}

}
