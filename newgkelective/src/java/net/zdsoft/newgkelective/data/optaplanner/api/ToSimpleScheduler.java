package net.zdsoft.newgkelective.data.optaplanner.api;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGConstants;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourse;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourse.CourseType;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourseIntervalConstraint;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourseSection;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGLectureConflict;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGMeanwhileClassGroup;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGPeriod;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGRoom;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGSectionLecture;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGStudentCourseSchedule;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGTeacher;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGUnavailablePeriodPenaltyHard;
import net.zdsoft.newgkelective.data.optaplanner.dto.CGInputData;
import net.zdsoft.newgkelective.data.optaplanner.util.DtoToData;
import net.zdsoft.newgkelective.data.optaplanner.util.XmlUtils;
import reactor.core.Fuseable.SynchronousSubscription;
public class ToSimpleScheduler {
	private final static String DELIMITER = "|";

	private String arrayId;
	
	// 需要算法给出时间点的Lecture, {CourseID, SectionID, LectureID, RoomID, TeacherID}
	// 如果是单双周的课，{CourseID, SectionID, LectureID, RoomID, 单周TeacherID, 双周TeacherID}
	private List<List<String>> 	lectureList;  //Entities

	// 所有可用的时间点, {PeriodID, DayIndex, TimeSlotIndex, AM|PM}, DayIndex: 0..6,
	// TimeSlotIndex: 1..10, AM==0, PM==1
	private List<List<String>>	newPeriodList;	  //Facts

	// 课程分组：(O)语数外，(A)选课，(B)学课，(X)其它， {CourseID, CourseType, subjectId}
	private List<List<String>>		courseList;

	//Section标记信息
	//<sectionID，classId,subjectId,subjectType>
	private List<List<String>>		sectionList;
	
	//单双周标记信息，记录 合成的lecture隐藏的一门科目
	//<lectureID,subjectId,subjectType>
	private List<List<String>> coupleList;
	
	// 预排课：已经排好了，不能动了， {LectureID, PeriodID}
	List<List<String>> lectureFixedList;
	
	// 联排课：{LectureID, timeSlotCount}, 目前只支持 timeSlotCount==2
	private List<List<String>> lectureTwinList;
	
	// 相同老师引起的冲突：{LectureID1, LectureID2}
	private List<List<String>> lectureBinaryConflictConstraintList;
	
	// {LectureID, PeriodIds, Level: 0..5}， PeriodIds:多个periodId连接 用 "|" 连接。 Level:0表示禁排，1~5表示喜好程度。隐含了 domainList
	private List<List<String>> lecturePreferredPeriodConstraintList;
	
	// 同排课, 一行一组，必须排在同一个时间点：{LectureID1, LectureID2, ...}
	private List<List<String>> lectureSamePeriodConstraintList;

	// 互斥老师：{TeacherID1, TeacherID2, NonOverlappingCount}
	private List<List<String>> teacherNonOverlappingCountList;
	
	// 同一个行政班的2个课，不能排在同一个半天的2个时间点，{LectureID1, LectureID2}
	private List<List<String>> lectureNotSameHalfDayConstraintList;

	// 同一个行政班的2个课，不能排在同一天的2个时间点，{LectureID1, LectureID2}
	private List<List<String>> lectureNotSameDayConstraintList;

	// 同一个行政班的2个课，不能排在连续的2个时间点，{LectureID1, LectureID2}
	private List<List<String>> lectureNotNextConstraintList;
	
	// 教师教学进度一致
	//sectionIDstr: 多个sectionID使用 | 连接，表示老师带的这几个班级需要教学进度一致
	// <teacehrID,sectionIDstr>  sectionIDstr
	private List<List<String>> teacherPlanList;
	
	public SimpleCourseSchedulerInput make(CGInputData input) {
		transfer(input);
		
		SimpleCourseSchedulerInput simpleScheduler = new SimpleCourseSchedulerInput();
		simpleScheduler.setArrayId(arrayId);
		simpleScheduler.setLectureList(lectureList);
		simpleScheduler.setPeriodList(newPeriodList);
		simpleScheduler.setCourseList(courseList);
		simpleScheduler.setSectionList(sectionList);
		simpleScheduler.setCoupleList(coupleList);
		simpleScheduler.setLectureFixedList(lectureFixedList);
		simpleScheduler.setLectureTwinList(lectureTwinList);
		simpleScheduler.setLectureBinaryConflictConstraintList(lectureBinaryConflictConstraintList);
		simpleScheduler.setLecturePreferredPeriodConstraintList(lecturePreferredPeriodConstraintList);
		simpleScheduler.setLectureSamePeriodConstraintList(lectureSamePeriodConstraintList);
		simpleScheduler.setTeacherNonOverlappingCountList(teacherNonOverlappingCountList);
		
		// 暂时没有
		simpleScheduler.setLectureNotNextConstraintList(lectureNotNextConstraintList);
		simpleScheduler.setLectureNotSameDayConstraintList(lectureNotSameHalfDayConstraintList);
		simpleScheduler.setLectureNotSameHalfDayConstraintList(lectureNotSameHalfDayConstraintList);
		
		simpleScheduler.setTeacherPlanList(teacherPlanList);
		
		return simpleScheduler;
	}
	
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		String filePath = "C:\\Users\\user\\Desktop\\simpleSchedule数据文件\\";
		List<String> fileNames = new ArrayList<>();
		String fileName = "";
//		fileName = "邹平一中2018-7-3 15-01-46"+".xml";
//		fileName = "莱州一中2018-7-6 11-49-17"+".xml";
//		fileName = "首师大附中2018-7-6 12-40-40"+".xml";
//		fileName = "北京二十中2018-7-6 12-00-53"+".xml";
//		fileName = "厦门第六中学2018-7-6 14-09-32"+".xml";
//		fileName = "青岛市十六中2018-7-6 14-20-20"+".xml";
//		fileName = "泰安二中2018-7-6 14-14-05"+".xml";
//		fileName = "山东邹平一中2018-7-25 18-21-48"+".xml";
//		fileNames.add(fileName);
		
		String[] list = new File(filePath).list();
		fileNames = Arrays.asList(list);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		
		for (String s : fileNames) {
			if(!s.endsWith("xml"))
				continue;
			if(true || s.contains("数字校园")) {
				CGInputData input = XmlUtils.toBeanFromFile(filePath+s, CGInputData.class);
				SimpleCourseSchedulerInput simpleSchedule = new ToSimpleScheduler().make(input);
				SimpleValidator.checkValidity(simpleSchedule);
				//	写出去
//				objectMapper.writeValue(new File("C:\\Users\\user\\Desktop\\simpleSchedule数据文件\\json\\"
//						+input.getUnitName()+ new Date().toLocaleString().replaceAll(":", "-")
//						+".json"), simpleSchedule); //IOException
				System.out.println(s+" 转换结束");
			}else {
				continue;
			}
			
//			System.out.println(s+" 转换结束");
//			if(simpleSchedule == null) {
//				
//			}
		}
	}
	
	private void transfer(CGInputData inputData) {
		CGStudentCourseSchedule input = DtoToData.transfer(inputData);
		
		List<CGSectionLecture> lectureEntityList = input.getLectureList();
		List<CGPeriod> periodEntityList = input.getPeriodList();
		List<CGTeacher> teacherList = input.getTeacherList();
		List<CGCourseSection> courseSectionList = input.getCourseSectionList();
		List<CGCourse> courseEntityList = input.getCourseList();
		List<CGMeanwhileClassGroup> meanwhileGroups = input.getMeanwhileClassGroup();
		CGConstants constants = input.getcGConstants();
		// 
		Map<CGCourse,List<CGPeriod>> preferedCoursePeriodMap = new HashMap<>();
		//TODO 手动为 preferedMap  放进一些数据
		List<CGPeriod> amPList = periodEntityList.stream()
				.filter(e->e.getTimeslot().getTimeslotIndex()<=4).collect(Collectors.toList());
		courseEntityList.stream()
			.filter(e -> e.getType().equals(CourseType.COMPULSORY_COURSE))  // 语数外 尽量在上午上课
			.forEach(e -> {
				preferedCoursePeriodMap.put(e, new ArrayList<>(amPList));
			});
		//TODO 手动为 一些 节次数只有 3 到4 的老师的课 尽量放在下午上课
		List<CGPeriod> pmPList = periodEntityList.stream()
				.filter(e->e.getTimeslot().getPeriodInterval().compareTo("3")>=0).collect(Collectors.toList());
		Map<CGCourseSection,List<CGPeriod>> preferedTeacherPeriodMap = new HashMap<>();
		courseSectionList.stream().filter(e->e.getLectureCount()<5&&e.getLectureCount()>2).forEach(se->{
			preferedTeacherPeriodMap.put(se, new ArrayList<>(pmPList));
		});
		
		// TODO 不联排课程 02 不再同半天 05 不在同一天
		Map<CGCourse, Map<CGCourse, String>> noAttachCourseMap = inputData.getNoAttachCourseMap();
		
		
		arrayId = input.getArrayId();
		//1.periodlist
		newPeriodList = periodEntityList.stream().map(e->{
			//{PeriodID, DayIndex, TimeSlotIndex, AM|PM} AM==0, PM==1
			List<String> period = new ArrayList<>(3);
			period.add(e.getSimpleCode());
			period.add(e.getDay().getDayIndex()+"");
			period.add(e.getTimeslot().getTimeslotIndex()+"");
			period.add(e.getTimeslot().getPeriodInterval());
			
			return period;
		}).collect(Collectors.toList());
		
		//2. course
		courseList = courseEntityList.stream().map(e->{
			//课程分组：(O)语数外，(A)选课，(B)学课，(X)其它， {CourseID, CourseType, subjectId}
			List<String> course = new ArrayList<>(3);
			course.add(e.getSimpleCode());
			//TODO X  除语数外的其他课
			course.add(e.getSimpleSubjectType());
			course.add(e.getSubjectId());
			course.add(e.getNeedRoom()+"");
			
			return course;
		}).collect(Collectors.toList());
		
		// 3 .sectionList
		sectionList = courseSectionList.stream().filter(e->e.getLectureList().size()>0).map(e->{
			//<sectionID，classId,subjectId,subjectType>
			List<String> cs = new ArrayList<>(3);
			cs.add(e.getId()+"");
			cs.add(e.getOldId());
			cs.add(e.getCourse().getSubjectId());
			cs.add(e.getCourse().getSimpleSubjectType());
			
			return cs;
		}).collect(Collectors.toList());
		
		//4. lecturelist, domainList
		Function<CGCourse,String> cFun = c->c.getSubjectId()+"-"+c.getSubjectType();
		Map<String, CGCourse> courseSubjectMap = courseEntityList.stream()
				.collect(Collectors.toMap(c->cFun.apply(c), Function.identity()));
		Map<CGCourse, CGCourse> isBiWeeklyMap = new HashMap<>();
		for (CGCourse cc : courseEntityList) {
			if(cc.getIsBiWeekly() != CGCourse.WEEK_TYPE_NORMAL && cc.getIsBiweeklyCourse() != null) {
				CGCourse cgCourse = courseSubjectMap.get(cc.getIsBiweeklyCourse());
				if(cgCourse != null) {
					isBiWeeklyMap.put(cc, cgCourse);
				}
			}
		}
		
		Map<String, List<CGSectionLecture>> biLectureMap = lectureEntityList.stream().filter(e->e.getIsBiWeekly()!=CGCourse.WEEK_TYPE_NORMAL)
				.collect(Collectors.groupingBy(e->e.getCourseSection().getOldId()));
		Map<Integer,CGSectionLecture> coupleLecMap = new HashMap<>();
		for (List<CGSectionLecture> ls : biLectureMap.values()) {
			// 先搞配对课程
			Map<CGCourse, CGSectionLecture> collect = ls.stream()
					.filter(c->StringUtils.isNotBlank(c.getCourse().getIsBiweeklyCourse()))
					.collect(Collectors.toMap(l->l.getCourse(),Function.identity()));
			List<CGSectionLecture> lctureListT = new ArrayList<>(ls);
			for (CGCourse courseT : collect.keySet()) {
				CGSectionLecture l = collect.get(courseT);
				if(!lctureListT.contains(l)) {
					// 去重
					continue;
				}
				CGCourse cgCourse = isBiWeeklyMap.get(courseT);
				if(cgCourse ==  null) {
					continue;
				}
				CGSectionLecture cgSectionLecture = collect.get(cgCourse);
				if(cgSectionLecture != null) {
					//TODO 
					if(l.getIsBiWeekly() == CGCourse.WEEK_TYPE_ODD) {
						coupleLecMap.put(l.getId(), cgSectionLecture);
					}else {
						coupleLecMap.put(cgSectionLecture.getId(), l);
					}
					lctureListT.remove(l);
					lctureListT.remove(cgSectionLecture);
				}
			}
			
		}
		
		
		
		Map<String, List<CGSectionLecture>> tCodeMap = lectureEntityList.stream().filter(e->StringUtils.isNotBlank(e.getTeacherCode()))
				.collect(Collectors.groupingBy(CGSectionLecture::getTeacherCode));
		lecturePreferredPeriodConstraintList = new ArrayList<>();
		lectureBinaryConflictConstraintList = new ArrayList<>();
		lectureFixedList = new ArrayList<>();
		lectureList = new ArrayList<>();
		lectureTwinList = new ArrayList<>();
		coupleList = new ArrayList<>();
		Set<String> courseIds = new HashSet<>();
		final String FAKE_TID_PREFIX = "FAKE_TID_";
		int fakeId = 1;
		for (CGCourseSection section : courseSectionList) {
			
			List<CGSectionLecture> sectionLec = section.getLectureList();
			int c = sectionLec.size();
			for (int i=0;i<c;i++) {
				CGSectionLecture lec = sectionLec.get(i);
				
				//1. 
				//{CourseID, SectionID, LectureID, RoomID, isBiweekly, 单周TeacherID, 双周TeacherID}
				List<String> le = new ArrayList<>();
				le.add(lec.getCourse().getSimpleCode());
				le.add(lec.getCourseSection().getId()+"");
				le.add(lec.getId()+"");
				le.add(lec.getRoomCode());
				courseIds.add(lec.getCourse().getSimpleCode());
				//domain 可排的形式体现 lecturePreferredPeriodConstraintList
//				for(CGPeriod p:lec.getPeriodList()) {
//					List<String> domain = new ArrayList<>();
//					domain.add(lec.getId()+"");
//					domain.add(p.getSimpleCode());
//					domain.add("0");
//					lecturePreferredPeriodConstraintList.add(domain);
//				}
				
				// prefer domain。 在prefer domain中 出现的 就不会在 domain中出现了
				Set<CGPeriod> domainEns = new HashSet<>(lec.getPeriodList());
				List<String> domain = new ArrayList<>();
//				Map<String, List<String>> preferedMap = new HashMap<>();
				List<CGPeriod> preferedPs = new ArrayList<>();
				if(preferedTeacherPeriodMap.containsKey(section)) {  // 教师 时间 偏好
					String preferPCs = preferedTeacherPeriodMap.get(section).stream().filter(p->domainEns.contains(p))
							.map(e->e.getSimpleCode())
							.collect(Collectors.joining(DELIMITER));
					//TODO domain为空的情况
					if(StringUtils.isNotBlank(preferPCs)) {
						domain = new ArrayList<>();
						domain.add(lec.getId()+"");
						domain.add(preferPCs);
						domain.add("5");
						lecturePreferredPeriodConstraintList.add(domain);
						preferedPs.addAll(preferedTeacherPeriodMap.get(section));
					}
				}
				// 这样写 老师时间偏好 和 课程时间偏好 就只能有一个 有效了 
				if(preferedCoursePeriodMap.containsKey(lec.getCourse())) {  // 课程 时间偏好
					String preferPCs = preferedCoursePeriodMap.get(lec.getCourse()).stream()
							.filter(p->!preferedPs.contains(p)&&domainEns.contains(p))
							.map(e->e.getSimpleCode())
							.collect(Collectors.joining(DELIMITER));
					if(StringUtils.isNotBlank(preferPCs)) {
						domain = new ArrayList<>();
						domain.add(lec.getId()+"");
						domain.add(preferPCs);
						domain.add((lec.getCourse().getPriority()+1)+"");
						lecturePreferredPeriodConstraintList.add(domain);
						preferedPs.addAll(preferedCoursePeriodMap.get(lec.getCourse()));
					}
				}
				
				
				
				//domain  lecturePreferredPeriodConstraintList
				String domainPCs = domainEns.stream().filter(p->!preferedPs.contains(p))
					.map(e->e.getSimpleCode())
					.collect(Collectors.joining(DELIMITER));
				if(StringUtils.isNotBlank(domainPCs)) {
					domain = new ArrayList<>();
					domain.add(lec.getId()+"");
					domain.add(domainPCs);
					domain.add("1");
					lecturePreferredPeriodConstraintList.add(domain);
				}
//				for (CGPeriod p : periodEntityList) {
//					if(domainEns.contains(p))
//						continue;
//					// 禁排的形式 体现 domain  比可排要大一些
//					List<String> domain = new ArrayList<>();
//					domain.add(lec.getId()+"");
//					domain.add(p.getSimpleCode());
//					domain.add("0");
//					lecturePreferredPeriodConstraintList.add(domain);
//				}
				
				
				// 单双周老师
				if(coupleLecMap.containsKey(lec.getId())) {
					CGSectionLecture resLec = coupleLecMap.get(lec.getId());
					// 单周在前
					if(lec.getIsBiWeekly() == CGCourse.WEEK_TYPE_ODD) {
						le.add(lec.getTeacherCode()!=null?lec.getTeacherCode():"");
						le.add(resLec.getTeacherCode()!=null?resLec.getTeacherCode():"");
					}else {
						le.add(resLec.getTeacherCode()!=null?resLec.getTeacherCode():"");
						le.add(lec.getTeacherCode()!=null?lec.getTeacherCode():"");
					}
				}else {
					String tc = lec.getTeacherCode()!=null?lec.getTeacherCode():"";
					le.add(tc);
					le.add(tc);
				}
				if(StringUtils.isBlank(le.get(le.size()-1))) {
					le.set(le.size()-1, FAKE_TID_PREFIX+ fakeId++);
				}
				if(StringUtils.isBlank(le.get(le.size()-2))) {
					le.set(le.size()-2, FAKE_TID_PREFIX+ fakeId++);
				}
				lectureList.add(le);
				
				//2. lectureFixedList
				if(lec.getPeriodList().size()==1) {
					List<String> fixedLec = new ArrayList<>();
					fixedLec.add(lec.getId()+"");
					fixedLec.add(lec.getPeriod().getSimpleCode());
					
					lectureFixedList.add(fixedLec);
				}
				
				//3. 连排 lectureTwinList
				if(lec.getTimeSlotCount() == 2) {
					List<String> twin = new ArrayList<>();
					twin.add(lec.getId()+"");
					twin.add(2+"");
					
					lectureTwinList.add(twin);
					//TODO 连排不再隐藏课程
//					c--;
				}
				
			}
			
		}
		
		// 过滤掉无用的course
		courseList = courseList.stream().filter(e->courseIds.contains(e.get(0))).collect(Collectors.toList());
		
		// 4. noAttachCourseMap 不联排课程  02 不在同半天 05 不在同一天 01 不连续
		Map<String, List<CGSectionLecture>> oldIdLecMap = lectureEntityList.stream()
				.filter(e->e.getCourseSection().isFixed())
				.collect(Collectors.groupingBy(e->e.getCourseSection().getOldId()));
		lectureNotSameHalfDayConstraintList = new ArrayList<>();
		lectureNotSameDayConstraintList = new ArrayList<>();
		lectureNotNextConstraintList = new ArrayList<>();
		List<String> constraint;
		for (CGCourse course : noAttachCourseMap.keySet()) {
			Map<CGCourse, String> resMap = noAttachCourseMap.get(course);
			
			for (CGCourse c2 : resMap.keySet()) {
				String type = resMap.get(c2);
				
				for (List<CGSectionLecture> lecs : oldIdLecMap.values()) {
					List<CGSectionLecture> collect1 = lecs.stream().filter(e->e.getCourse().equals(course)).collect(Collectors.toList());
					List<CGSectionLecture> collect2 = lecs.stream().filter(e->e.getCourse().equals(c2)).collect(Collectors.toList());
					if(collect1.size() <1|| collect2.size()<1)
						continue;
					int size1 = collect1.size();
					int size2 = collect2.size();
					
					for (int j=0;j<(size1);j++) {
						CGSectionLecture l1 = collect1.get(j);
						for (int k=0;k<(size2);k++) {
							CGSectionLecture l2 = collect2.get(k);
							if(l1.getId() == l2.getId())
								continue;
							int l1Id = l1.getId();
							int l2Id = l2.getId();
							
							constraint = new ArrayList<>();
							constraint.add(l1Id+"");
							constraint.add(l2Id+"");
							if(type.equals("01")) {
								lectureNotNextConstraintList.add(constraint);
							}else if(type.equals("02")) {
								lectureNotSameHalfDayConstraintList.add(constraint);
							}else if(type.equals("05")) {
								lectureNotSameDayConstraintList.add(constraint);
							}
						}
					}
				}
				
			}
			
			
		}
		
		//5. lectureSamePeriodConstraintList 同排课
		Map<Integer, CGCourseSection> sectionIdMap = courseSectionList.stream().collect(Collectors.toMap(CGCourseSection::getId,e->e));
		lectureSamePeriodConstraintList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(meanwhileGroups)) {
			for (CGMeanwhileClassGroup gp : meanwhileGroups) {
				List<CGCourseSection> collect = gp.getClassSubjectCodeGroup().stream().map(e->sectionIdMap.get(e)).collect(Collectors.toList());
				
				List<String> sameP = new ArrayList<>();
				Integer count = collect.stream().map(e->e.getLectureList().size()-Optional.ofNullable(e.getCourse().getTimes()).orElse(0)).min(Integer::compareTo).get();
				for(int i=0;i<count;i++) {
					for (CGCourseSection sc : collect) {
						// 将被隐藏的课 转换为 显示的课
						Integer lecId = sc.getLectureList().get(i).getId();
//						if(reverseCoupMap.containsKey(lecId)) {
//							lecId = reverseCoupMap.get(lecId);
//						}
						
						sameP.add(lecId+"");
					}
				}
				lectureSamePeriodConstraintList.add(sameP);
			}
		}
		
		//6. 教师互斥 teacherNonOverlappingCountList
		Set<String> tcs = lectureEntityList.stream().filter(e->e.getTeacherCode()!=null).map(e->e.getTeacherCode()).collect(Collectors.toSet());
		Map<CGTeacher, Set<CGTeacher>> mutexTeacherMap = inputData.getMutexTeacherMap();
		teacherNonOverlappingCountList = new ArrayList<>();
		//TODO null
		if(mutexTeacherMap!= null && mutexTeacherMap.size()>0) {
			for (CGTeacher t : mutexTeacherMap.keySet()) {
				if(!tcs.contains(t.getCode()) ) {
					System.out.println("教师没有课程");
					continue;
				}
				Set<CGTeacher> set = mutexTeacherMap.get(t);
				for (CGTeacher cgTeacher : set) {
					List<String> mutex = new ArrayList<>();
					mutex.add(t.getCode());
					mutex.add(cgTeacher.getCode());
					
					if(!tcs.contains(cgTeacher.getCode())) {
						System.out.println("教师没有课程");
						continue;
					}
					if(tCodeMap.get(cgTeacher.getCode()).size()<t.getMutexNum())
						mutex.add(tCodeMap.get(cgTeacher.getCode()).size()+"");
					else
						mutex.add(t.getMutexNum()+"");
					
					teacherNonOverlappingCountList.add(mutex);
				}
			}
		}
		
		//TODO 7. 教师教学进度一致
		int limit = 0;
		Map<CGTeacher, List<CGCourseSection>> teacherSectionMap = courseSectionList.stream()
				.filter(e->e.getTeacher()!=null && e.getLectureCount()>=limit)
				.collect(Collectors.groupingBy(e->e.getTeacher()));
		teacherPlanList = new ArrayList<>();
		for (CGTeacher t : teacherSectionMap.keySet()) {
			List<CGCourseSection> list = teacherSectionMap.get(t);
			if(list.size()<2)
				continue;
			
			Map<CGCourse, List<CGCourseSection>> courseSectionMap = list.stream().collect(Collectors.groupingBy(e->e.getCourse()));
			for (List<CGCourseSection> scs : courseSectionMap.values()) {
				if(scs.size()<2)
					continue;
				Map<Integer, List<CGCourseSection>> map = scs.stream().collect(Collectors.groupingBy(e->e.getLectureCount()));
				for (List<CGCourseSection> cs : map.values()) {
					if(cs.size()<2)
						continue;
					
					List<String> tp = new ArrayList<>();
					tp.add(t.getCode());
					String sectionstr = cs.stream().map(e->String.valueOf(e.getId())).collect(Collectors.joining(DELIMITER));
					tp.add(sectionstr);
					teacherPlanList.add(tp);
				}
				
			}
		}
		
	}
	
	/**
	 * 根据范围 创建一个list，左闭右开
	 * @param start
	 * @param end
	 * @return
	 */
	private static List<Integer> range(int start, int end) {
		List<Integer> list = new ArrayList<>();
		for (int i=start;i<end;i++) {
			list.add(i);
		}
		return list;
	}
	
}
