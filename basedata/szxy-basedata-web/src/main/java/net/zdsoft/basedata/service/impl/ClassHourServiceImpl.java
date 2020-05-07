package net.zdsoft.basedata.service.impl;

import net.zdsoft.basedata.constant.BaseConstants;

import net.zdsoft.basedata.dao.ClassHourDao;
import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.dto.TeachClassSearchDto;
import net.zdsoft.basedata.entity.ClassHour;
import net.zdsoft.basedata.entity.ClassHourEx;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.service.ClassHourExService;
import net.zdsoft.basedata.service.ClassHourService;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.basedata.service.TeachClassStuService;
import net.zdsoft.basedata.service.TeachPlaceService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.ArrayUtil;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
@Service("classHourService")
public class ClassHourServiceImpl extends BaseServiceImpl<ClassHour, String> implements ClassHourService{
	
	@Autowired
	private ClassHourDao classHourDao;
	
	@Autowired
	private CourseScheduleService courseScheduleService;
	@Autowired
	private ClassService classService;
	@Autowired
	private TeachClassService teachClassService;
	@Autowired
	private TeachClassStuService teachClassStuService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private TeachPlaceService teachPlaceService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ClassHourExService classHourExService;
	
	@Override
	protected BaseJpaRepositoryDao<ClassHour, String> getJpaDao() {
		return classHourDao;
	}

	@Override
	protected Class<ClassHour> getEntityClass() {
		return ClassHour.class;
	}


	@Override
	public List<ClassHour> findListByUnitId(String acadyear, String semester, String unitId,String gradeId,boolean isMakeTime) {
		List<ClassHour> list=new ArrayList<>();
		if(StringUtils.isNotBlank(gradeId)) {
			list=classHourDao.findListByGraded(acadyear,semester,unitId,gradeId);
		}else {
			list=classHourDao.findListByUnitId(acadyear,semester,unitId);
		}
		if(isMakeTime && CollectionUtils.isNotEmpty(list)) {
			String[] hourId=EntityUtils.getList(list, e->e.getId()).toArray(new String[0]);
			List<ClassHourEx> exList = classHourExService.findByClassHourIdIn(hourId);
			if(CollectionUtils.isNotEmpty(exList)) {
				Map<String,List<String>> timeMap=new HashMap<>();
				for(ClassHourEx c:exList) {
					if(!timeMap.containsKey(c.getClassHourId())) {
						timeMap.put(c.getClassHourId(), new ArrayList<>());
					}
					timeMap.get(c.getClassHourId()).add(c.getDayOfWeek()+"_"+c.getPeriodInterval()+"_"+c.getPeriod());
				}
				for(ClassHour h:list) {
					if(timeMap.containsKey(h.getId())) {
						h.setTimeList(timeMap.get(h.getId()));
					}
				}
			}
			
		}
		return list;
	}
	
	

	@Override
	public String saveChange(CourseScheduleDto searchDto, String acadyear, String semester, String hourId,
			String time1, String time2) {
		//1、前期数据简单校验
		JSONObject json = new JSONObject();
		json.put("success", true);
		String unitId=searchDto.getSchoolId();
		String gradeId=searchDto.getGradeId();
		ClassHour classHour = this.findOne(hourId);
		if(classHour==null) {
			json.put("success", false);
			json.put("errorTitle", "数据更改");
			json.put("errorHead", "走班课程");
			json.put("errorContent", "数据已调整，请重新刷新后操作");
			return json.toString();
		}
		List<ClassHourEx> exList = classHourExService.findByClassHourIdIn(new String[] {hourId});
		if(CollectionUtils.isEmpty(exList)) {
			json.put("success", false);
			json.put("errorTitle", "数据更改");
			json.put("errorHead", "走班课程");
			json.put("errorContent", "时间点改动,请重新刷新后操作");
			return json.toString();
		}
		ClassHourEx ex1=null;
		ClassHourEx ex2=null;
		for(ClassHourEx ex:exList) {
			if((ex.getDayOfWeek()+"_"+ex.getPeriodInterval()+"_"+ex.getPeriod()).equals(time1)) {
				ex1=ex;
			}else if((ex.getDayOfWeek()+"_"+ex.getPeriodInterval()+"_"+ex.getPeriod()).equals(time2)) {
				ex2=ex;
			}
		}
		if(ex1==ex2) {
			json.put("success", false);
			json.put("errorTitle", "数据更改");
			json.put("errorHead", "走班课程");
			json.put("errorContent", "时间点改动,请重新刷新后操作");
			return json.toString();
		}
		if(ex1!=null && ex2!=null) {
			json.put("success", false);
			json.put("errorTitle", "数据更改");
			json.put("errorHead", "走班课程");
			json.put("errorContent", "时间点改动,请重新刷新后操作");
			return json.toString();
		}
		//从页面看 调整时原来有时间跟一个空白调整
		//课表冲突验证
		//真实的主动调整时间
		ClassHourEx relEx=ex1==null?ex2:ex1;
		//被动调整时间
		String reltime=time1.equals(relEx.getDayOfWeek()+"_"+relEx.getPeriodInterval()+"_"+relEx.getPeriod())?time2:time1;
		
		//查当前年级下  这两个时间点所有课表
		CourseScheduleDto dto=new CourseScheduleDto();
		dto.setAcadyear(acadyear);
		dto.setSchoolId(unitId);
		dto.setSemester(Integer.parseInt(semester));
		//包括临界点
		if(searchDto.getDayOfWeek1()==0) {
			dto.setWeekOfWorktime1(searchDto.getWeekOfWorktime1());
			dto.setDayOfWeek1(searchDto.getDayOfWeek1());
			dto.setWeekOfWorktime2(searchDto.getWeekOfWorktime1()+1);
			dto.setDayOfWeek2(6);
		}else {
			dto.setWeekOfWorktime1(searchDto.getWeekOfWorktime1());
			dto.setDayOfWeek1(searchDto.getDayOfWeek1());
			dto.setWeekOfWorktime2(searchDto.getWeekOfWorktime1()+2);
			dto.setDayOfWeek2(searchDto.getDayOfWeek1()-1);
		}
		dto.setXN(true);
		//包括虚拟(虚拟时间之后根据classId范围统一将某个时间点修改到另外时间点)--当前单位--取两周数据
		//当前单位 意味着除去当前年级其他年级数据也按照这两周数据进行复制到后面 --原因判断教师与场地冲突考虑到跨年级
		List<CourseSchedule> list = courseScheduleService.findByTimes(dto, new String[] {time1,time2});
		
		List<Clazz> clazzList = classService.findByGradeId(gradeId);
		Map<String, Clazz> classMap = EntityUtils.getMap(clazzList, e->e.getId());
		List<TeachClass> teachClassList = teachClassService.findBySearch(unitId, acadyear, semester, gradeId);
		
		
		//虚拟课程本身限制的行政班---课表虚拟课程+班级id
		Set<String> allClazzXz1=new HashSet<>();
		//主动调整 虚拟课程+班级id
		Set<String> allSubjectClassId1=new HashSet<>();
		if(StringUtils.isNotBlank(classHour.getClassIds())) {
			allClazzXz1.addAll(Arrays.asList(classHour.getClassIds().split(",")));
		}else {
			//无限制 年级下班级
			allClazzXz1.addAll(EntityUtils.getSet(clazzList, e->e.getId()));
		}
		for(String s:allClazzXz1) {
			//限制的
			allSubjectClassId1.add(classHour.getSubjectId()+"_"+s);
		}
		
		//被动调整---reltime包括这个allClazzXz的虚拟课程时间--从结果设置处出发
		//虚拟课程本身限制的行政班---课表虚拟课程+班级id
		Set<String> allClazzXz2=new HashSet<>();
		//主动调整 虚拟课程+班级id
		Set<String> allSubjectClassId2=new HashSet<>();
		List<ClassHour> classHourAllList = findListByUnitId(acadyear, semester, unitId, gradeId, true);
		List<ClassHour> needMoveList=new ArrayList<>();
		for(ClassHour c:classHourAllList) {
			if(c.getId().equals(hourId)) {
				continue;
			}
			if(!c.getTimeList().contains(reltime)) {
				continue;
			}
			if(StringUtils.isNotBlank(c.getClassIds())) {
				//跟主动调整有无交集
				boolean isNo=true;
				for(String s:allClazzXz1) {
					if(c.getClassIds().contains(s)) {
						isNo=false;
						break;
					}
				}
				if(!isNo) {
					needMoveList.add(c);
				}
			}else {
				needMoveList.add(c);
			}
		}
		Set<String> rightHourIds=new HashSet<>();
		if(CollectionUtils.isNotEmpty(needMoveList)) {
			rightHourIds = EntityUtils.getSet(needMoveList, e->e.getId());
			for(ClassHour h:needMoveList) {
				Set<String> ss=new HashSet<>();
				if(StringUtils.isNotBlank(h.getClassIds())) {
					ss.addAll(Arrays.asList(h.getClassIds().split(",")));
				}else {
					ss.addAll(EntityUtils.getSet(clazzList, e->e.getId()));
				}
				for(String s:ss) {
					allSubjectClassId2.add(h.getSubjectId()+"_"+s);
				}
				allClazzXz2.addAll(ss);
			}
			
		}
		
		Set<String> allClassIds=new HashSet<>();//当前年级下所有班级(包括教学班)
		allClassIds.addAll(EntityUtils.getSet(clazzList, e->e.getId()));
		
		Map<String,TeachClass> teachClassMap=new HashMap<>();
		Set<String> relaTeachClassIds1=new HashSet<>();//主动关联调整教学班
		Set<String> relaTeachClassIds2=new HashSet<>();//被动关联调整教学班
		if(CollectionUtils.isNotEmpty(teachClassList)) {
			for(TeachClass t:teachClassList) {
				allClassIds.add(t.getId());
				teachClassMap.put(t.getId(), t);
				if(StringUtils.isNotBlank(t.getRelaCourseId())) {
					if(t.getRelaCourseId().equals(relEx.getClassHourId())) {
						//主动关联调整
						relaTeachClassIds1.add(t.getId());
					}else if(rightHourIds.contains(t.getRelaCourseId())) {
						//被动关联调整
						relaTeachClassIds2.add(t.getId());
					}
				}
			}
		}
		
		//查询这个教学班下的所有该年级的学生所在的行政班id--真正上使用到的行政班数据
//		Set<String> classIdByT1=new HashSet<>();
//		Set<String> classIdByT2=new HashSet<>();
		//获取年级对应班级的所有学生
		List<Student> studentList = studentService.findByGradeId(gradeId);
		Map<String, Student> stuMap = EntityUtils.getMap(studentList, e->e.getId());
//		Set<String> relaTeachClassIds=new HashSet<>();
//		relaTeachClassIds.addAll(relaTeachClassIds1);
//		relaTeachClassIds.addAll(relaTeachClassIds2);
//		if(CollectionUtils.isNotEmpty(relaTeachClassIds)) {
//			List<TeachClassStu> teachClassStuList222 = teachClassStuService.findByClassIds(relaTeachClassIds.toArray(new String[] {}));
//			if(CollectionUtils.isNotEmpty(teachClassStuList222)) {
//				for(TeachClassStu ts:teachClassStuList222) {
//					if(!stuMap.containsKey(ts.getStudentId())) {
//						continue;
//					}
//					if(relaTeachClassIds1.contains(ts.getClassId())) {
//						classIdByT1.add(stuMap.get(ts.getStudentId()).getClassId());
//					}else if(relaTeachClassIds2.contains(ts.getClassId())) {
//						classIdByT2.add(stuMap.get(ts.getStudentId()).getClassId());
//					}
//				}
//				
//			}
//		}
			
//		//如果这两节课假设可以移动 最终这两节课的所有课程时间
		List<CourseSchedule> notMoveList=new ArrayList<>();//---第一周完整数据+第二周的单双周的数据
		String[] arr1=new String[] {relEx.getDayOfWeek()+"",relEx.getPeriodInterval(),relEx.getPeriod()+""};
		String[] arr2=reltime.split("_");//被动时间
		String exTime=relEx.getDayOfWeek()+"_"+relEx.getPeriodInterval()+"_"+relEx.getPeriod();
		for(CourseSchedule c:list) {
			if(c.getWeekOfWorktime()<=dto.getWeekOfWorktime2()-1 && c.getDayOfWeek()<=dto.getDayOfWeek2()) {
				//当前这一周的
			}else {
				//不是当前周--下一周只拿当前单双周数据
				if(c.getWeekType()!=3 && c.getClassType()==1) {
					//下一周的单双周--对应的是在调整班级中的，时间也要进行调整
					//该年级下有存在对应教学班的学生所在行政班对应时间都可以移动
					/**
					 * doto 关联班级  不限情况 取所有班级 如果该班级根本用不到这个时间点
					 * allClazzXz1（不限情况 取所有班级） -----classIdByT1（不限情况 取教学班真实对应行政班）
					 */
					if(allClazzXz1.contains(c.getClassId()) || allClazzXz2.contains(c.getClassId())) {
						//time1--->time2
						String kk=c.getDayOfWeek()+"_"+c.getPeriodInterval()+"_"+c.getPeriod();
						if(exTime.equals(kk)) {
							//主动
							c.setDayOfWeek(Integer.parseInt(arr2[0]));
							c.setPeriodInterval(arr2[1]);
							c.setPeriod(Integer.parseInt(arr2[2]));
							notMoveList.add(c);
							continue;
						}else if(reltime.equals(kk)) {
							c.setDayOfWeek(Integer.parseInt(arr1[0]));
							c.setPeriodInterval(arr1[1]);
							c.setPeriod(Integer.parseInt(arr1[2]));
							notMoveList.add(c);
							continue;
						}else {
							notMoveList.add(c);
						}
						
					}else {
						notMoveList.add(c);
					}
				}
				continue;
			}
			
			if(!allClassIds.contains(c.getClassId())) {
				//不是该年级下数据
				notMoveList.add(c);
				continue;
			}
			if(c.getClassType()==1) {
				if(c.getSubjectType().equals(BaseConstants.SUBJECT_TYPE_VIRTUAL)) {
					//虚拟课程调整
					if(allSubjectClassId1.contains(c.getSubjectId()+"_"+c.getClassId())
							|| allSubjectClassId2.contains(c.getSubjectId()+"_"+c.getClassId())) {
						String kk=c.getDayOfWeek()+"_"+c.getPeriodInterval()+"_"+c.getPeriod();
						if(exTime.equals(kk)) {
							//主动
							c.setDayOfWeek(Integer.parseInt(arr2[0]));
							c.setPeriodInterval(arr2[1]);
							c.setPeriod(Integer.parseInt(arr2[2]));
							notMoveList.add(c);
							continue;
						}else if(reltime.equals(kk)) {
							c.setDayOfWeek(Integer.parseInt(arr1[0]));
							c.setPeriodInterval(arr1[1]);
							c.setPeriod(Integer.parseInt(arr1[2]));
							notMoveList.add(c);
							continue;
						}else {
							notMoveList.add(c);
						}
					}else {
						notMoveList.add(c);
					}
				}else {
					//该年级下有存在对应教学班的学生所在行政班对应时间都可以移动
					if(allClazzXz1.contains(c.getClassId()) || allClazzXz2.contains(c.getClassId())) {
						//time1--->time2
						String kk=c.getDayOfWeek()+"_"+c.getPeriodInterval()+"_"+c.getPeriod();
						if(exTime.equals(kk)) {
							//主动
							c.setDayOfWeek(Integer.parseInt(arr2[0]));
							c.setPeriodInterval(arr2[1]);
							c.setPeriod(Integer.parseInt(arr2[2]));
							notMoveList.add(c);
							continue;
						}else if(reltime.equals(kk)) {
							c.setDayOfWeek(Integer.parseInt(arr1[0]));
							c.setPeriodInterval(arr1[1]);
							c.setPeriod(Integer.parseInt(arr1[2]));
							notMoveList.add(c);
							continue;
						}else {
							notMoveList.add(c);
						}
						
					}else {
						notMoveList.add(c);
					}
				}
			}else {
				
				String kk=c.getDayOfWeek()+"_"+c.getPeriodInterval()+"_"+c.getPeriod();
				if(relaTeachClassIds1.contains(c.getClassId())) {
					if(exTime.equals(kk)) {
						//主动
						c.setDayOfWeek(Integer.parseInt(arr2[0]));
						c.setPeriodInterval(arr2[1]);
						c.setPeriod(Integer.parseInt(arr2[2]));
						notMoveList.add(c);
						continue;
					}else if(reltime.equals(kk)) {
						c.setDayOfWeek(Integer.parseInt(arr1[0]));
						c.setPeriodInterval(arr1[1]);
						c.setPeriod(Integer.parseInt(arr1[2]));
						notMoveList.add(c);
						continue;
					}else {
						notMoveList.add(c);
					}
					continue;
				}
				
				if(relaTeachClassIds2.contains(c.getClassId())) {
					if(exTime.equals(kk)) {
						//主动
						c.setDayOfWeek(Integer.parseInt(arr2[0]));
						c.setPeriodInterval(arr2[1]);
						c.setPeriod(Integer.parseInt(arr2[2]));
						notMoveList.add(c);
						continue;
					}else if(reltime.equals(kk)) {
						c.setDayOfWeek(Integer.parseInt(arr1[0]));
						c.setPeriodInterval(arr1[1]);
						c.setPeriod(Integer.parseInt(arr1[2]));
						notMoveList.add(c);
						continue;
					}else {
						notMoveList.add(c);
					}
					continue;
				}
				notMoveList.add(c);
				
			}
		}
		
		
		//验证 notMoveList冲突
//		//正常数据存在单周也存在双周
		Map<String,Set<String>> placeMap1=new HashMap<>();//单周
		Map<String,Set<String>> placeMap2=new HashMap<>();//双周
		Set<String> samePlace=new HashSet<>();
		
		Map<String,Set<String>> teacherMap1=new HashMap<>();
		Map<String,Set<String>> teacherMap2=new HashMap<>();
		Set<String> sameTeacher=new HashSet<>();
		
		Map<String,Set<String>> classMap1=new HashMap<>();
		Map<String,Set<String>> classMap2=new HashMap<>();
		Set<String> sameClazz=new HashSet<>();
		
		Set<String> tcId=new HashSet<>();//该年级下的时间内的教学班ids
		//验证教师，场地冲突----如果那个时间点本身存在重复 那么那个地方安排不进去
		for(CourseSchedule item:notMoveList) {
			if(item.getClassType()==1 && item.getSubjectType().equals(BaseConstants.SUBJECT_TYPE_VIRTUAL)) {
				continue;
			}
			String key=item.getDayOfWeek()+"_"+item.getPeriodInterval()+"_"+item.getPeriod();
			
			//1:场地
			if(StringUtils.isNotBlank(item.getPlaceId())) {
				if(item.getWeekType()==1) {
					makeSameOrMap(key, placeMap1, item.getPlaceId(), samePlace);
				}else if(item.getWeekType()==2){
					makeSameOrMap(key, placeMap2, item.getPlaceId(), samePlace);
				}else {
					makeSameOrMap(key, placeMap1, item.getPlaceId(), samePlace);
					makeSameOrMap(key, placeMap2, item.getPlaceId(), samePlace);
				}
			}
			//2:教师
			if(StringUtils.isNotBlank(item.getTeacherId()) && !BaseConstants.ZERO_GUID.equals(item.getTeacherId())){
				if(item.getWeekType()==1) {
					makeSameOrMap(key, teacherMap1, item.getTeacherId(), sameTeacher);
				}else if(item.getWeekType()==2){
					makeSameOrMap(key, teacherMap2, item.getTeacherId(), sameTeacher);
				}else {
					makeSameOrMap(key, teacherMap1, item.getTeacherId(), sameTeacher);
					makeSameOrMap(key, teacherMap2, item.getTeacherId(), sameTeacher);
				}
			}
			
			if(!allClassIds.contains(item.getClassId())) {
				//不是该年级数据--不验证学生冲突
				continue;
			}
			if(item.getClassType()!=1) {
				tcId.add(item.getClassId());
			}
			if(item.getWeekType()==1) {
				makeSameOrMap(key, classMap1, item.getClassId(), sameClazz);
			}else if(item.getWeekType()==2){
				makeSameOrMap(key, classMap2, item.getClassId(), sameClazz);
			}else {
				makeSameOrMap(key, classMap1, item.getClassId(), sameClazz);
				makeSameOrMap(key, classMap2, item.getClassId(), sameClazz);
			}
		}
		if(CollectionUtils.isNotEmpty(samePlace)) {
			List<TeachPlace> placeList = teachPlaceService.findListByIdIn(samePlace.toArray(new String[] {}));
			String ss = ArrayUtil.print(EntityUtils.getSet(placeList, e->e.getPlaceName()).toArray(new String[] {}));
			json.put("success", false);
			json.put("errorTitle", "时间冲突");
			json.put("errorHead", "场地");
			json.put("errorContent", ss==null?"":ss);
			return json.toString();
		}
		if(CollectionUtils.isNotEmpty(sameTeacher)) {
			List<Teacher> teacherList = teacherService.findListByIdIn(sameTeacher.toArray(new String[] {}));
			String ss = ArrayUtil.print(EntityUtils.getSet(teacherList, e->e.getTeacherName()).toArray(new String[] {}));
			json.put("success", false);
			json.put("errorTitle", "时间冲突");
			json.put("errorHead", "教师");
			json.put("errorContent", ss==null?"":ss);
			return json.toString();
		}
		
		if(CollectionUtils.isNotEmpty(sameClazz)) {
			//同班级有超过一节课在同一时间
			List<String> s1=new ArrayList<>();
			List<String> s2=new ArrayList<>();
			for(String s:sameClazz) {
				if(classMap.containsKey(s)) {
					s1.add(classMap.get(s).getClassName());
				}else if(teachClassMap.containsKey(s)) {
					s2.add(teachClassMap.get(s).getName());
				}
			}
			
			String ss1 ="";
			if(CollectionUtils.isNotEmpty(s1)) {
				ss1 = ArrayUtil.print(s1.toArray(new String[] {}));
			}
			String ss2="";
			if(CollectionUtils.isNotEmpty(s2)) {
				ss2 = ArrayUtil.print(s2.toArray(new String[] {}));
			}
			String ss="";
			if(StringUtils.isNotBlank(ss1)) {
				ss="行政班:"+ss1;
			}
			if(StringUtils.isNotBlank(ss2)) {
				if(StringUtils.isNotBlank(ss)) {
					ss=ss+";教学班:"+ss2;
				}else {
					ss="教学班:"+ss2;
				}
			}
			if(StringUtils.isNotBlank(ss)) {
				json.put("success", false);
				json.put("errorTitle", "时间冲突");
				json.put("errorHead", "班级");
				json.put("errorContent", ss==null?"":ss);
				return json.toString();
			}
		}
		
		//验证学生冲突--只考虑牵扯到该年级的所有班级--主要教学班与行政班或者教学班与教学班冲突 否则行政班与行政班 之前的班级冲突已经解决啦
		if(CollectionUtils.isNotEmpty(tcId)) {
			//获取年级对应班级的所有学生
//			List<Student> studentList = studentService.findByGradeId(gradeId);
//			Map<String, Student> stuMap = EntityUtils.getMap(studentList, e->e.getId());
			//key:classId  value:studentId
			Map<String, List<String>> stuClazzMap = EntityUtils.getListMap(studentList,Student::getClassId , e->e.getId());
			
			List<TeachClassStu> teachClassStuList = teachClassStuService.findByClassIds(tcId.toArray(new String[] {}));
			if(CollectionUtils.isNotEmpty(teachClassStuList)) {
				Map<String,Set<String>> stuTeachMap=new HashMap<>();//教学班下的学生
				for(TeachClassStu t:teachClassStuList) {
					//过滤掉非该年级的学生
					if(!stuMap.containsKey(t.getStudentId())) {
						continue;
					}
					if(!stuTeachMap.containsKey(t.getClassId())) {
						stuTeachMap.put(t.getClassId(), new HashSet<>());
					}
					stuTeachMap.get(t.getClassId()).add(t.getStudentId());
					String pId = teachClassMap.get(t.getClassId()).getParentId();
					if(StringUtils.isNotBlank(pId)) {
						if(!stuTeachMap.containsKey(pId)) {
							stuTeachMap.put(pId, new HashSet<>());
						}
						stuTeachMap.get(pId).add(t.getStudentId());
					}
				}
				//单周冲突
				for(Entry<String, Set<String>> ii:classMap1.entrySet()) {
					Set<String> set1 = ii.getValue();//该时间下所有班级
					Set<String> stuIds=new HashSet<>();
					for(String s:set1) {
						if(CollectionUtils.isNotEmpty(stuClazzMap.get(s))) {
							//stuIds 与stuClazzMap.get(s) 有没有交集--有 就重复啦
							if(CollectionUtils.isNotEmpty(stuIds)) {
								Set<String> sameSet = (Set<String>) CollectionUtils.intersection(stuIds, stuClazzMap.get(s)).stream().collect(Collectors.toSet());
								if(CollectionUtils.isNotEmpty(sameSet)) {
									List<String> s1=new ArrayList<>();
									for(String sa:sameSet) {
										s1.add(stuMap.get(sa).getStudentName());
									}
									json.put("success", false);
									json.put("errorTitle", "时间冲突");
									json.put("errorHead", "学生");
									json.put("errorContent", ArrayUtil.print(s1.toArray(new String[] {})));
									return json.toString();
								}
							}
							stuIds.addAll(stuClazzMap.get(s));
							
						}else if(CollectionUtils.isNotEmpty(stuClazzMap.get(s))) {
							if(CollectionUtils.isNotEmpty(stuIds)) {
								Set<String> sameSet = (Set<String>) CollectionUtils.intersection(stuIds, stuClazzMap.get(s)).stream().collect(Collectors.toSet());
								if(CollectionUtils.isNotEmpty(sameSet)) {
									List<String> s1=new ArrayList<>();
									for(String sa:sameSet) {
										s1.add(stuMap.get(sa).getStudentName());
									}
									json.put("success", false);
									json.put("errorTitle", "时间冲突");
									json.put("errorHead", "学生");
									json.put("errorContent", ArrayUtil.print(s1.toArray(new String[] {})));
									return json.toString();
								}
							}else {
								stuIds.addAll(stuClazzMap.get(s));
							}
						}else {
							continue;
						}
					}
				}
				//双周冲突
				for(Entry<String, Set<String>> ii:classMap2.entrySet()) {
					Set<String> set1 = ii.getValue();//该时间下所有班级
					Set<String> stuIds=new HashSet<>();
					for(String s:set1) {
						if(CollectionUtils.isNotEmpty(stuClazzMap.get(s))) {
							//stuIds 与stuClazzMap.get(s) 有没有交集--有 就重复啦
							if(CollectionUtils.isNotEmpty(stuIds)) {
								Set<String> sameSet = (Set<String>) CollectionUtils.intersection(stuIds, stuClazzMap.get(s)).stream().collect(Collectors.toSet());
								if(CollectionUtils.isNotEmpty(sameSet)) {
									List<String> s1=new ArrayList<>();
									for(String sa:sameSet) {
										s1.add(stuMap.get(sa).getStudentName());
									}
									json.put("success", false);
									json.put("errorTitle", "时间冲突");
									json.put("errorHead", "学生");
									json.put("errorContent", ArrayUtil.print(s1.toArray(new String[] {})));
									return json.toString();
								}
							}
							stuIds.addAll(stuClazzMap.get(s));
							
						}else if(CollectionUtils.isNotEmpty(stuClazzMap.get(s))) {
							if(CollectionUtils.isNotEmpty(stuIds)) {
								Set<String> sameSet = (Set<String>) CollectionUtils.intersection(stuIds, stuClazzMap.get(s)).stream().collect(Collectors.toSet());
								if(CollectionUtils.isNotEmpty(sameSet)) {
									List<String> s1=new ArrayList<>();
									for(String sa:sameSet) {
										s1.add(stuMap.get(sa).getStudentName());
									}
									json.put("success", false);
									json.put("errorTitle", "时间冲突");
									json.put("errorHead", "学生");
									json.put("errorContent", ArrayUtil.print(s1.toArray(new String[] {})));
									return json.toString();
								}
							}else {
								stuIds.addAll(stuClazzMap.get(s));
							}
						}else {
							continue;
						}
					}
				}
				
				

			}
		}
		
		//删除当前单位及以后这两节课
		CourseScheduleDto delDto=new CourseScheduleDto();
		delDto.setAcadyear(acadyear);
		delDto.setSchoolId(unitId);
		delDto.setSemester(Integer.parseInt(semester));
		delDto.setWeekOfWorktime1(searchDto.getWeekOfWorktime1());
		delDto.setDayOfWeek1(searchDto.getDayOfWeek1());
		//delDto.setClassIds(delClassid.toArray(new String[] {}));
		delDto.setTimeArr(new String[] {time1,time2});
		courseScheduleService.deleteByClassId(delDto);
		//复制删除当前单位这两节课课表到当前及以后
		List<CourseSchedule> insertList = new ArrayList<>();
		//复制数据
		for(CourseSchedule item:notMoveList) {
			 List<CourseSchedule> ll = makeCourseScheduleList(item,searchDto.getWeekOfWorktime1(), searchDto.getWeekOfWorktime2(), searchDto.getDayOfWeek1(), searchDto.getDayOfWeek2());
			 if(CollectionUtils.isNotEmpty(ll)) {
				 insertList.addAll(ll);
			 }
		}
		courseScheduleService.saveAll(insertList.toArray(new CourseSchedule[] {}));
		List<ClassHourEx> updateExList=new ArrayList<>();
		relEx.setDayOfWeek(Integer.parseInt(arr2[0]));
		relEx.setPeriodInterval(arr2[1]);
		relEx.setPeriod(Integer.parseInt(arr2[2]));
		relEx.setModifyTime(new Date());
		updateExList.add(relEx);
		//其他关联课程时间点
		Set<String> delHourIds=new HashSet<>();
		ClassHourEx exDto=new ClassHourEx();
		if(CollectionUtils.isNotEmpty(needMoveList)) {
			for(ClassHour s:needMoveList) {
				if(CollectionUtils.isNotEmpty(s.getTimeList())) {
					if(s.getTimeList().contains(exTime) || s.getTimeList().contains(reltime)) {
						delHourIds.add(s.getId());
						for(String ss:s.getTimeList()) {
							exDto=new ClassHourEx();
							exDto.setId(UuidUtils.generateUuid());
							exDto.setClassHourId(s.getId());
							exDto.setCreationTime(new Date());
							exDto.setModifyTime(new Date());
							exDto.setIsDeleted(0);
							if(ss.equals(exTime)) {
								exDto.setDayOfWeek(Integer.parseInt(arr2[0]));
								exDto.setPeriodInterval(arr2[1]);
								exDto.setPeriod(Integer.parseInt(arr2[2]));
							}else if(ss.equals(reltime)) {
								exDto.setDayOfWeek(Integer.parseInt(arr1[0]));
								exDto.setPeriodInterval(arr1[1]);
								exDto.setPeriod(Integer.parseInt(arr1[2]));
							}else {
								String[] aa = ss.split("_");
								exDto.setDayOfWeek(Integer.parseInt(aa[0]));
								exDto.setPeriodInterval(aa[1]);
								exDto.setPeriod(Integer.parseInt(aa[2]));
							}
							updateExList.add(exDto);
						}
					}
				}
				
				
			}
		}
		if(CollectionUtils.isNotEmpty(delHourIds)) {
			classHourExService.deleteUpdateByClassHourIdIn(delHourIds.toArray(new String[0]));
		}
		classHourExService.saveAll(updateExList.toArray(new ClassHourEx[] {}));
		return json.toString();
	}
	
	private void makeSameOrMap(String key,Map<String,Set<String>> itemMap,String valueid,Set<String> sameSet) {
		if(!itemMap.containsKey(key)) {
			itemMap.put(key, new HashSet<>());
			itemMap.get(key).add(valueid);
		}else {
			if(itemMap.get(key).contains(valueid)) {
				//冲突
				sameSet.add(valueid);
			}else {
				itemMap.get(key).add(valueid);
			}
		}
	}
	
	private List<CourseSchedule> makeCourseScheduleList(CourseSchedule courseSchedule,int weekOfWorktime1,int weekOfWorktime2,
			int dayOfWeek1,int dayOfWeek2){
		List<CourseSchedule> insertList=new ArrayList<CourseSchedule>();
		CourseSchedule insertCourse=null;
		int dayOfWeek=courseSchedule.getDayOfWeek();
		for(int i=weekOfWorktime1;i<=weekOfWorktime2;i++){
			//每一周
			if(i==weekOfWorktime1){
				if(dayOfWeek<dayOfWeek1){
					continue;
				}
			}else if(i==weekOfWorktime2){
				if(dayOfWeek>dayOfWeek2){
					continue;
				}
				
			}
			if(courseSchedule.getWeekType()==1) {
				//单周数据
				if(i%2==0) {
					//双周不复制
					continue;
				}
			}
			if(courseSchedule.getWeekType()==2) {
				//双周数据
				if(i%2==1) {
					//单周不复制
					continue;
				}
			}
			insertCourse=new CourseSchedule();
			insertCourse=EntityUtils.copyProperties(courseSchedule, insertCourse);
			insertCourse.setWeekOfWorktime(i);
			insertCourse.setCreationTime(new Date());
			insertCourse.setModifyTime(new Date());
			insertCourse.setId(UuidUtils.generateUuid());			
			insertList.add(insertCourse);
		}
		return insertList;
	}


	@Override
	public List<ClassHour> findBySubjectIds(String acadyear, String semester,String unitId, String gradeId, String[] subjectIds) {
		if(subjectIds==null || subjectIds.length==0) {
			return findListByUnitId(acadyear, semester, unitId, gradeId,false);
		}
		return classHourDao.findBySubjectIds(acadyear, semester, unitId, gradeId, subjectIds);
	}

	@Override
	public String deleteBySubjectIdTime(CourseScheduleDto searchDto, String acadyear, String semester, String hourId,
			String time1) {
		JSONObject json = new JSONObject();
		json.put("success", true);
		String unitId=searchDto.getSchoolId();
		String gradeId=searchDto.getGradeId();
		ClassHour classHour = this.findOne(hourId);
		if(classHour==null) {
			json.put("success", false);
			json.put("errorTitle", "数据更改");
			json.put("errorHead", "走班课程");
			json.put("errorContent", "数据已调整，请重新刷新后操作");
			return json.toString();
		}
		List<ClassHourEx> exList = classHourExService.findByClassHourIdIn(new String[] {hourId});
		if(CollectionUtils.isEmpty(exList)) {
			json.put("success", false);
			json.put("errorTitle", "数据更改");
			json.put("errorHead", "走班课程");
			json.put("errorContent", "时间点改动,请重新刷新后操作");
			return json.toString();
		}
		ClassHourEx hourex=null;
		for(ClassHourEx c:exList) {
			if((c.getDayOfWeek()+"_"+c.getPeriodInterval()+"_"+c.getPeriod()).equals(time1)) {
				hourex=c;
			}
		}
		if(hourex==null) {
			json.put("success", false);
			json.put("errorTitle", "数据更改");
			json.put("errorHead", "走班课程");
			json.put("errorContent", "时间点改动,请重新刷新后操作");
			return json.toString();
		}
		
		
		//删除该年级行政班+该年级关联courseId1的教学班
		CourseScheduleDto dto=new CourseScheduleDto();
		dto.setAcadyear(acadyear);
		dto.setSchoolId(unitId);
		dto.setSemester(Integer.parseInt(semester));
		dto.setWeekOfWorktime1(searchDto.getWeekOfWorktime1());
		dto.setDayOfWeek1(searchDto.getDayOfWeek1());
		dto.setTimeArr(new String[] {time1});
		
		Set<String> delClassid=new HashSet<>();
		if(StringUtils.isNotBlank(classHour.getClassIds())) {
			delClassid.addAll(Arrays.asList(classHour.getClassIds().split(",")));
		}else {
			List<Clazz> clazzList = classService.findByGradeId(gradeId);
			delClassid = EntityUtils.getSet(clazzList, e->e.getId());
		}
		TeachClassSearchDto teachSearchDto=new TeachClassSearchDto();
		teachSearchDto.setUnitId(unitId);
		teachSearchDto.setAcadyearSearch(acadyear);
		teachSearchDto.setSemesterSearch(semester);
		teachSearchDto.setClassType(BaseConstants.SUBJECT_TYPE_BX);
		teachSearchDto.setGradeIds(gradeId);
		//从原来虚拟科目改为虚拟科目时间设置主表id
		teachSearchDto.setRelaCourseId(hourId);
		List<TeachClass> teachClassList = teachClassService.findListByDtoWithMaster(teachSearchDto);
		if(CollectionUtils.isNotEmpty(teachClassList)) {
			delClassid.addAll(EntityUtils.getSet(teachClassList, e->e.getId()));
		}
		if(CollectionUtils.isNotEmpty(delClassid)) {
			dto.setClassIds(delClassid.toArray(new String[] {}));
			dto.setXN(true);
			dto.setXnSubjectId(classHour.getSubjectId());
			courseScheduleService.deleteByClassId(dto);
		}
		classHourExService.deleteUpdateById(hourex.getId());
		return json.toString();
	}
	
	@Override
	public void deleteByIds(String[] ids) {
		classHourExService.deleteUpdateByClassHourIdIn(ids);
		classHourDao.deleteUpdateByIdIn(ids);
	}

	@Override
	public List<ClassHour> makeClassNames(List<ClassHour> hourList) {
		List<ClassHour> allHourList=new ArrayList<>();
		if(CollectionUtils.isNotEmpty(hourList)) {
			Set<String> ids=new HashSet<>();
			//一般不限班级 放在最前面 classIds
			List<ClassHour> hasClassList=new ArrayList<>();
 			for(ClassHour c:hourList) {
 				if(StringUtils.isNotBlank(c.getClassIds())) {
 					List<String> list = Arrays.asList(c.getClassIds().split(","));
 					ids.addAll(list);
 					hasClassList.add(c);
 				}else {
 					c.setClassNames("不限班级");
 					allHourList.add(c);
 				}
 			}
 			List<Clazz> clazzList=new ArrayList<>();
 			if(CollectionUtils.isNotEmpty(ids)) {
 				clazzList = classService.findListByIds(ids.toArray(new String[0]));
 				//排序
 				clazzList.sort((x,y)-> {
 					return x.getClassCode().compareTo(y.getClassCode());
 				});
 			}
 			for(ClassHour c:hasClassList) {
 				List<Clazz> cList = clazzList.stream().filter(e->c.getClassIds().contains(e.getId())).collect(Collectors.toList());
 				if(CollectionUtils.isNotEmpty(cList)) {
 					String oo="";
 					for(Clazz cc:cList) {
 						oo=oo+"+"+cc.getClassName();
 					}
 					c.setClassNames(oo.substring(1));
 					allHourList.add(c);
 				}else {
 					//过滤班级数据有误
 					//c.setClassNames("班级数据有误");
 					//allHourList.add(c);
 				}
 			}
		}
		return allHourList;
	}

}
