package net.zdsoft.newgkelective.data.optaplanner.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;

import net.zdsoft.framework.utils.Objects;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGClass;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGConstants;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourse;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourse.CourseType;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourseIntervalConstraint;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourseSection;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGDay;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGLectureConflict;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGMeanwhileClassGroup;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGNoMeanwhileClassGroup;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGPeriod;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGRoom;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGRoomConstraint;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGSectionConflict;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGSectionLecture;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGStudent;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGStudentCourseSchedule;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGTeacher;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGTimeslot;
import net.zdsoft.newgkelective.data.optaplanner.dto.CGInputData;
import net.zdsoft.newgkelective.data.optaplanner.dto.NKPeriod;
import net.zdsoft.newgkelective.data.optaplanner.dto.NKRoom;

public class DtoToData {
	
	public static CGStudentCourseSchedule transfer(CGInputData input){
		
		List<CGCourse> courseList = input.getCourseList();
//		courseList = courseList.stream().filter(e->e.getMinWorkingDaySize()>0).collect(Collectors.toList());
		List<CGTeacher> teacherList = input.getTeacherList();
		Integer maxSectionStudentCount = input.getMaxSectionStudentCount();
		Integer margin = input.getMargin();
		int maxTimeslotIndex = input.getMaxTimeslotIndex();
		int mmCount = input.getMmCount();
		int amCount = input.getAmCount();
		int pmCount = input.getPmCount();
		int nightCount = input.getNightCount();
		
		List<NKPeriod> periodList = input.getPeriodList();
		List<NKRoom> roomList = input.getRoomList();
		List<CGClass> classList = input.getClassList();
		List<CGStudent> studentList = input.getStudentList();
		Map<String, List<NKPeriod>> originExistedTable = input.getExistedTable();
		Map<String, List<NKPeriod>> rawFixedLectures = input.getFixedLectures();
		
		String arrayId = input.getArrayId();
		Map<CGClass, NKRoom> xzbRoomConstrains = input.getXzbRoomConstrains();
		
		Map<CGClass, Set<NKPeriod>> doClassPeriods = input.getDoClassPeriods();
		Map<CGClass, Set<NKPeriod>> noClassPeriods = input.getNoClassPeriods();
		
		Map<CGTeacher, Set<CGTeacher>> mutexTeacherMap = Optional.ofNullable(input.getMutexTeacherMap()).orElse(new HashMap<>());
		Map<CGCourse, Map<CGCourse,String>> noAttachCourseMap = input.getNoAttachCourseMap();
		Map<CGCourse, List<NKPeriod>> subjectCoupleTimeMap = input.getSubjectCoupleTimeMap();
		
		List<Set<String>> combineClassList = input.getCombineClassList();
		List<Set<String>> meanwhileClassList = input.getMeanwhileClassList();
		List<String[]> noMeanWhileClassGroups = input.getNoMeanWhileClassGroups();
		if(noMeanWhileClassGroups == null){
			noMeanWhileClassGroups = new ArrayList<>();
		}
		
		
		int idNum = 0;
		
		Set<NKPeriod> periodsSet = doClassPeriods.values().stream().flatMap(e->e.stream()).collect(Collectors.toSet());
		periodsSet.addAll(periodList);
		// 装配时空 数据
		Map<NKPeriod, CGPeriod> oldToNewTime = new HashMap<NKPeriod, CGPeriod>();
		List<CGPeriod> allCGPeriod = new ArrayList<CGPeriod>(); 
		List<CGPeriod> availablePeriod = new ArrayList<CGPeriod>(); 
		Map<Integer, CGDay> indexDayMap = new HashMap<Integer, CGDay>();
		Map<Integer, CGTimeslot> indexTimeslotMap = new HashMap<Integer, CGTimeslot>();
		for (NKPeriod nkPeriod : periodsSet) {
			CGPeriod cgPeriod = new CGPeriod();
			
			CGDay cgDay = indexDayMap.get(nkPeriod.getDayIndex());
			if(cgDay == null){
				cgDay = new CGDay();
				cgDay.setDayIndex(nkPeriod.getDayIndex());
				indexDayMap.put(nkPeriod.getDayIndex(), cgDay);
			}
			List<CGPeriod> periodList2 = cgDay.getPeriodList();
			if(periodList2 == null){
				periodList2 = new ArrayList<CGPeriod>();
				cgDay.setPeriodList(periodList2);
			}
			periodList2.add(cgPeriod);
			
			CGTimeslot cgTimeslot = indexTimeslotMap.get(nkPeriod.getTimeslotIndex());
			if(cgTimeslot == null){
				cgTimeslot = new CGTimeslot();
				cgTimeslot.setTimeslotIndex(nkPeriod.getTimeslotIndex());
				cgTimeslot.setPeriodInterval(nkPeriod.getPeriodInterval());
				indexTimeslotMap.put(nkPeriod.getTimeslotIndex(), cgTimeslot);
			}
			
			cgPeriod.setDay(cgDay);
			cgPeriod.setTimeslot(cgTimeslot);
			cgPeriod.setExtra(nkPeriod.isExtra());
			
			if(!cgPeriod.isExtra()) availablePeriod.add(cgPeriod);
			allCGPeriod.add(cgPeriod);
			oldToNewTime.put(nkPeriod, cgPeriod);
		}
		
		List<CGRoom> cgRoomList = new ArrayList<CGRoom>();
		Map<NKRoom, CGRoom> oldToNewRoomMap = new HashMap<NKRoom, CGRoom>();
		for (NKRoom nkRoom : roomList) {
			CGRoom cgRoom = new CGRoom();
			cgRoom.setCapacity(nkRoom.getCapacity());
			cgRoom.setCode(nkRoom.getRoomId());
			
			cgRoomList.add(cgRoom);
			oldToNewRoomMap.put(nkRoom, cgRoom);
		}
		
		
		// 获取学生 和 班级
		List<CGCourseSection> courseSectionList = new ArrayList<CGCourseSection>();
		Map<CGCourse, List<CGCourseSection>> courseToSectionMap = new HashMap<CGCourse, List<CGCourseSection>>();
		Map<CGStudent, Set<CGCourseSection>> stuJxbListMap = new HashMap<CGStudent, Set<CGCourseSection>>();
		Map<CGClass, CGCourseSection> oldClassToNewMap = new HashMap<>();
		Map<CGCourseSection, List<CGStudent>> sectionStudentMap = new HashMap<>();
		Map<CGStudent, Map<CGCourse, CGCourseSection>> stuToSectionMap = new HashMap<CGStudent, Map<CGCourse, CGCourseSection>>();
		Map<Integer,String> sectionIdToName = new HashMap<>();
		for (CGClass cgClass : classList) {
			// 0课时的不组班
			if(cgClass.getLectureCount() < 1){
				continue;
			}			
			//装配班级
			CGCourseSection cgCourseSection = new CGCourseSection();
			cgCourseSection.setId(idNum++);
			cgCourseSection.setOldId(cgClass.getOldId());
			cgCourseSection.setCourse(cgClass.getCourse());
			cgCourseSection.setFixed(cgClass.isFixed());
			cgCourseSection.setIsBiweekly(cgClass.getIsBiWeekly());
//			cgCourseSection.setSectionAvgStudentCount(classStudentCountLimit);
			cgCourseSection.setSectionIndex(cgClass.getJxbIndex());
			cgCourseSection.setTeacher(cgClass.getTeacher());
			cgCourseSection.setSectionAvgStudentCount(cgClass.getAvgStudentCount());
			cgCourseSection.setClassName(cgClass.getClassName());
			cgCourseSection.setPure3(cgClass.isPure3());
			cgCourseSection.setLectureCount(cgClass.getLectureCount());
			cgCourseSection.setParentId(cgClass.getParentId());
			cgCourseSection.setStudentList(cgClass.getStudentList());
			
			courseSectionList.add(cgCourseSection);
			oldClassToNewMap.put(cgClass, cgCourseSection);
			
			
			// 装配 courseToSectionMap
			List<CGCourseSection> sectionList = courseToSectionMap.get(cgClass.getCourse());
			if(sectionList == null){
				sectionList = new ArrayList<CGCourseSection>();
				courseToSectionMap.put(cgClass.getCourse(), sectionList);
			}
			sectionList.add(cgCourseSection);
			
			for (CGStudent cgStu : cgClass.getStudentList()) {
				Set<CGCourseSection> courseSectionSet = stuJxbListMap.get(cgStu);
				if(courseSectionSet == null){
					courseSectionSet = new HashSet<CGCourseSection>();
					stuJxbListMap.put(cgStu, courseSectionSet);
				}
				courseSectionSet.add(cgCourseSection);
			}
			
			//
			sectionStudentMap.put(cgCourseSection, cgClass.getStudentList());
			
			// stuToSectionMap
			for (CGStudent cgStu : cgClass.getStudentList()) {
				Map<CGCourse, CGCourseSection> map = stuToSectionMap.get(cgStu);
				if(map == null){
					map = new HashMap<CGCourse, CGCourseSection>();
					stuToSectionMap.put(cgStu, map);
				}
				map.put(cgClass.getCourse(), cgCourseSection);
			}
			
			//
			sectionIdToName.put(cgCourseSection.getId(), cgClass.getClassName());
		}
		
		Map<CGCourseSection, CGRoom> newXzbRoomConstrainsMap = freshXzbRoomConstrains(xzbRoomConstrains, oldToNewRoomMap, oldClassToNewMap,input);
		
		Map<CGCourseSection, Set<CGPeriod>> cgDoClassPeriods = freshClassPeriods(doClassPeriods, oldToNewTime,
				oldClassToNewMap);
		Map<CGCourseSection, Set<CGPeriod>> cgNoClassPeriods = freshClassPeriods(noClassPeriods, oldToNewTime,
				oldClassToNewMap);
		Map<CGCourse, Set<CGPeriod>> cgSubjectCoupleTimeMap = freshCoursePeriods(subjectCoupleTimeMap, oldToNewTime,input);
		Map<CGCourseSection, Set<CGPeriod>> existedTable = freshExistedTable(originExistedTable, oldToNewTime,courseSectionList,input);
		Map<CGCourseSection, Set<CGPeriod>> fiexedTable = freshExistedTable(rawFixedLectures, oldToNewTime,courseSectionList,input);

		// 验证 行政班 使用场地 是否相同
		Map<String,HashSet<String>> roomUseSectionMap = new HashMap<>();
		courseSectionList.stream().filter(e->e.isFixed()&&newXzbRoomConstrainsMap.containsKey(e)).forEach(e->{
			roomUseSectionMap.computeIfAbsent(newXzbRoomConstrainsMap.get(e).getCode(),l->new HashSet<>()).add(e.getOldId());
		});
		boolean b = roomUseSectionMap.values().stream().anyMatch(e -> e.size() > 1);
		if(b){
			input.appendMsg("请确保每个行政班有一个独占的场地");
		}

		List<CGRoom> nullRoomList = new ArrayList<>();
		CGRoom nullRoom = new CGRoom();
		nullRoom.setCode(CGRoom.NO_ROOM_CODE);
		nullRoomList.add(nullRoom);
		boolean hasNotNeedRoom = false;
		
		final int workDayCount = (int)availablePeriod.stream().map(e->e.getDay().getDayIndex()).distinct().count();
		
		// 对于周课时大于 上课天数的  行政班  课程默认连排; 
		// 取消这个条件 19-07-01
//		courseList.stream().filter(e -> "O".equals(e.getSubjectType()) && !e.isFixedSubject())
//				.filter(e -> (e.getMinWorkingDaySize() - workDayCount) > (e.getTimes() == null ? 0 : e.getTimes()))
//				.forEach(e -> e.setTimes(e.getMinWorkingDaySize() - workDayCount));
		
		
		// 每个时间段 最后一节课的index
		Set<Integer> endIndexSet = new HashSet<>();
		endIndexSet.add(mmCount);
		endIndexSet.add(mmCount+amCount);
		endIndexSet.add(mmCount+amCount+pmCount);
		endIndexSet.add(mmCount+amCount+pmCount+nightCount);
		
		// 3.装配  Lecture
		
		String sectionName = null;
		CGPeriod cgPeriod = null;
		List<CGSectionLecture> sameAllLectureList = null;
		List<CGSectionLecture> allLecture = new ArrayList<CGSectionLecture>();
		List<CGRoomConstraint> roomConstraintList = new ArrayList<CGRoomConstraint>();
		Map<CGTeacher, List<CGSectionLecture>> teacherLectureMap = new HashMap<>();
		Map<String,Integer> periodProgressMap = new HashMap<>();  // 记录某个被拆分的班级 子班级占用了那些时间点
		for (CGCourseSection courseSection : courseSectionList) {
			CGCourse course = courseSection.getCourse();
			sectionName = sectionIdToName.get(courseSection.getId());
			sameAllLectureList = new ArrayList<>();
			courseSection.setLectureList(sameAllLectureList);
			int lectureCount = courseSection.getLectureCount();
			Set<CGPeriod> fixedPeriods = fiexedTable.get(courseSection);
			
			Set<CGPeriod> coupleTimes = Optional.ofNullable(cgSubjectCoupleTimeMap.get(course)).orElse(new HashSet<>());
			final List<CGPeriod> localPeroidList = getLocalPeriodList(
					availablePeriod, cgDoClassPeriods, cgNoClassPeriods,
					courseSection);
			if(localPeroidList.size() < lectureCount) {
//				throw new RuntimeException(sectionName+" "+course.getCode()+"有"+lectureCount+"个课时，"
//						+ "只有"+localPeroidList.size()+"个可安排时间点");
				input.appendMsg(sectionName+" "+course.getCode()+"有"+lectureCount+"个课时，"
						+ "只有"+localPeroidList.size()+"个可安排时间点");
				continue;
			}
			
			CGRoom room = newXzbRoomConstrainsMap.get(courseSection);
			if(room == null) {
				input.appendMsg(sectionName+" "+course.getCode()+"没有安排场地");
				continue;
			}
			//是否需要场地
			if(!Objects.equals(course.getNeedRoom(), 1)) {
				room = nullRoom;
				courseSection.setNeedRoom(0);
				hasNotNeedRoom = true;
			}else if(room.getCode().equals(CGRoom.NO_ROOM_CODE)) {
				courseSection.setNeedRoom(0);
			}
			
			Integer times = course.getTimes();
			// 加了 连排以后的算法 使用，暂时注释
			if(times != null){
				// 算法改变 连排不再需要合成课程
//				lectureCount = (lectureCount - times*2) + times;
			}else{
				times = 0;
			}
			// 获取 预排课程的 时间
			Set<CGPeriod> existedP = existedTable.get(courseSection);
			Iterator<CGPeriod> iterator = null;
			if(existedP != null) iterator = existedP.iterator(); 
			if(existedP!=null && times > 0) {
				int coupleCount = getCoupleCount(existedP);
				times = times - coupleCount;
				if(times <0) times = 0;
			}
			
			//
			if(fiexedTable.size()>0) {
				if(iterator!=null) {
					times -= (lectureCount - existedP.size());  // 如果已经安排的课程中没有连排，并且 还有未安排的课程，则认为 将用未安排的课程来实现连排
					lectureCount = existedP.size();
					courseSection.setLectureCount(lectureCount);
				}
				else
					continue; // 没有次 课程的预排课程
			}
			
			
			Integer count = 0;
			if(!courseSection.isFixed() && StringUtils.isNotBlank(courseSection.getParentId())) {
				count = periodProgressMap.get(courseSection.getParentId());
				if(count == null) {
					count = 0;
					periodProgressMap.put(courseSection.getParentId(), count);
				}
				periodProgressMap.put(courseSection.getParentId(), count+lectureCount);
			}
			for (int i = 0; i < lectureCount; i++) {
				if(fiexedTable.size()>0 && (iterator==null || !iterator.hasNext())) 
					break;
				
				CGSectionLecture lecture = new CGSectionLecture();
				lecture.setCourseSection(courseSection);
				lecture.setCourse(course);
				lecture.setIsBiWeekly(i==0?courseSection.getRealWeekType():CGCourse.WEEK_TYPE_NORMAL);
				lecture.setId(idNum++);
//				lecture.setMoveable(false);
				
				lecture.setTimeSlotCount(1);
				// 连排
				if(i+1 <= times && (existedP == null || i+1 <= (lectureCount - existedP.size()))){
					List<CGPeriod> localPeroidList2 = new ArrayList<>(localPeroidList);
					if(course.getCoupleTimeLimit().equals("1")) {
						// 指定连排时间
						localPeroidList2 = Sets.intersection(new HashSet<>(localPeroidList), coupleTimes).stream().collect(Collectors.toList());
					}
					Set<String> periodCodeSet = localPeroidList2.stream().map(e->e.getPeriodCode()).collect(Collectors.toSet());
					localPeroidList2 = localPeroidList2.stream().filter(e->{
						String periodCode = e.getOffSetPeriodCode(1);
						
						if(endIndexSet.contains(e.getTimeslot().getTimeslotIndex())) {
							return false;
						}
						if(!periodCodeSet.contains(periodCode)){
							return false;
						}
						return true;
					}).collect(Collectors.toList());
					if(localPeroidList2.size() < times) {
						input.appendMsg(sectionName+"-"+course.getCode()+"没有足够的连排时间");
						continue;
					}
					
					
					lecture.setTimeSlotCount(2);
					lecture.setPeriodList(localPeroidList2);
					
					if(fiexedTable.size()>0) {
						cgPeriod = iterator.next();
						if(!localPeroidList2.contains(cgPeriod))
							cgPeriod = getRandomOne(lecture.getPeriodList());
					}else
						// 指定一个variable
						cgPeriod = getRandomOne(lecture.getPeriodList());
					
					lecture.setPeriod(cgPeriod);
				}else{
					
					if(iterator!=null && iterator.hasNext()) {
						// 课表中已经保存的课程
						cgPeriod = iterator.next();
						if(fiexedTable!=null && fiexedTable.size()>0) {
							lecture.setPeriodList(new ArrayList<>(localPeroidList));
						}else {
							lecture.setPeriodList(new ArrayList<>(Arrays.asList(cgPeriod)));
						}
						lecture.setPeriod(cgPeriod);
					}else {
						lecture.setPeriodList(new ArrayList<>(localPeroidList));
						
						if(!courseSection.isFixed()) { // 教学班
							int index = i + count;
							cgPeriod = lecture.getPeriodList().get(index);
							lecture.setPeriodList(new ArrayList<>(Arrays.asList(cgPeriod)));
						}else {  // 此课程 在行政班上课
							cgPeriod = getRandomOne(lecture.getPeriodList());
						}
						
						lecture.setPeriod(cgPeriod);
					}
				}
				
				if(fixedPeriods!=null && fixedPeriods.contains(cgPeriod)) {
					lecture.setMoveable(false);
				}
				
				lecture.getPeriod().getLectureList().add(lecture);
				
				
				// 设置 room
				lecture.setRoom(room);
				lecture.setRoomList(Arrays.asList(room));
				
				lecture.setSectionLectureList(sameAllLectureList);
				sameAllLectureList.add(lecture);
				allLecture.add(lecture);
			}
//			courseSection.setLectureList(sameAllLectureList);
			
			if(courseSection.getTeacher() == null) {
				continue;
			}
			if(!teacherLectureMap.containsKey(courseSection.getTeacher())) {
				teacherLectureMap.put(courseSection.getTeacher(), new ArrayList<>());
			}
			teacherLectureMap.get(courseSection.getTeacher()).addAll(sameAllLectureList);
		}
		courseSectionList = courseSectionList.stream().filter(e->e.getLectureList().size()>0).collect(Collectors.toList());
		if(hasNotNeedRoom) {
			cgRoomList.add(nullRoom);
		}
		
		// 将course 中的minWorkingDaySize设置
		for (CGCourseSection section : courseSectionList) {
			int lectureCount = section.getLectureCount();
			lectureCount -= Optional.ofNullable(section.getCourse().getTimes()).orElse(0);
			int sectionWorkDay =  (int) section.getLectureList().stream().flatMap(e->e.getPeriodList().stream()).map(e->e.getDay()).distinct().count();
			// 计算每个section可以安排课程的天数 作为minWorkingDaySize;不再考虑workDayCount
			section.setMinWorkingDaySize(Math.min(lectureCount, sectionWorkDay));
		}
		
		
		//10.重新设置单双周
		// 先筛选配对课程
		boolean hasBiweek = courseList.stream().anyMatch(c->c.getIsBiWeekly() != CGCourse.WEEK_TYPE_NORMAL);
		if(hasBiweek && resetWeekType(courseList, allLecture)) {
			WeekTypeSolverUtils.resetWeekType(courseList, allLecture);
		}
		
		// 7. 时间约束
//		List<CGUnavailablePeriodPenaltyHard> hardPeriodPenalty = new ArrayList<CGUnavailablePeriodPenaltyHard>();
		
		// 8.给教师赋予id
		for (CGTeacher cgTeacher : Optional.ofNullable(teacherList).orElse(new ArrayList<CGTeacher>())) {
			cgTeacher.setId(idNum++);
		}
		
		//一周内2个课程默认至少间隔1天，但不要一节安排在第一天，一节安排在最后一天。
		
		Set<CGCourse> work2CourseSet = courseList.stream().filter(e->e.getMinWorkingDaySize() == 2)
				.collect(Collectors.toSet());
		for (CGCourse cgCourse : work2CourseSet) {
			Map<CGCourse, String> map = noAttachCourseMap.get(cgCourse);
			if(map == null) {
				map = new HashMap<>();
				noAttachCourseMap.put(cgCourse, map);
			}
			if(map.get(cgCourse) == null) {
				map.put(cgCourse, "03");
			}
		}
		
		
		// 周课时为1的小课程默认同一天不开课  ;界面上一旦设置了某门科目的不排课，这门课就不再行使默认设置
		List<CGCourse> workOneCourseSet = courseList.stream().filter(e->e.getIsBiWeekly()==2 && e.getMinWorkingDaySize() == 1)
				.collect(Collectors.toList());
		for (int i=0;i<workOneCourseSet.size();i++) {
			for (int j=i+1;j<workOneCourseSet.size();j++) {
				CGCourse cgCourse1 = workOneCourseSet.get(i);
				CGCourse cgCourse2 = workOneCourseSet.get(j);
				
				if(noAttachCourseMap.containsKey(cgCourse1) && noAttachCourseMap.get(cgCourse1).containsKey(cgCourse2)) {
					continue;
				}
				if(noAttachCourseMap.containsKey(cgCourse2) && noAttachCourseMap.get(cgCourse2).containsKey(cgCourse1)) {
					continue;
				}
				
				Map<CGCourse, String> map = noAttachCourseMap.get(cgCourse1);
				if(map == null) {
					map = new HashMap<>();
					noAttachCourseMap.put(cgCourse1, map);
				}
				map.put(cgCourse2, "05");  // 不在同一天
			}
		}
		
		
		
//		CGCourseIntervalConstraint constrain2;
		// 9. 不联排 课程
		List<CGCourseIntervalConstraint> intervalConsList = new ArrayList<>();
		CGCourseIntervalConstraint constrain;
		List<CGSectionConflict> conflictSectionList = calculateCourseConflictList(courseSectionList);
		for (CGCourse course : noAttachCourseMap.keySet()) {
			
			Map<CGCourse, String> noAttachCourses = noAttachCourseMap.get(course);
			
			for (CGCourse cgCourse2 : noAttachCourses.keySet()) {
				String type = noAttachCourses.get(cgCourse2);
				Set<CGSectionConflict> conflicts = conflictSectionList.stream().filter(e->{
					if(e.getLeftCourseSection().getCourse().equals(course) && e.getRightCourseSection().getCourse().equals(cgCourse2)) {
						return true;
					}
					if(e.getRightCourseSection().getCourse().equals(course) && e.getLeftCourseSection().getCourse().equals(cgCourse2)) {
						return true;
					}
					return false;
				}).collect(Collectors.toSet());
				if(conflicts.size() > 0) {
					for (CGSectionConflict cgSectionConflict : conflicts) {
						constrain = new CGCourseIntervalConstraint(cgSectionConflict.getLeftCourseSection(), 
								cgSectionConflict.getRightCourseSection(), typeToInterval(type));
						constrain.setId(idNum++);
						intervalConsList.add(constrain);
					}
				}
			}
		}
		
		// 10. 互斥教师
		List<CGSectionLecture> lectures1;
		List<CGSectionLecture> lectures2;
		CGLectureConflict lectureT;
		int tCount1 = 0;
		int tCount2 = 0;
		List<CGLectureConflict> lectureConflictList = new ArrayList<>();
		for (CGTeacher teacher : mutexTeacherMap.keySet()) {
			Set<CGTeacher> set = mutexTeacherMap.get(teacher);
			lectures1 = teacherLectureMap.get(teacher);
			if(CollectionUtils.isEmpty(lectures1)) {
				continue;
			}
			tCount1 = lectures1.stream().map(e->e.getTimeSlotCount()).reduce((x,y)->x+y).orElse(0);
			if(teacher.getMutexNum() > tCount1) {
				input.appendMsg("老师"+teacher.getTeacherName()+"有"+tCount1+"节课 却有"+teacher.getMutexNum()+"个互斥课时，请调整");
				continue;
			}
			
			
			for (CGTeacher cgTeacher2 : set) {
				lectures2 = teacherLectureMap.get(cgTeacher2);
				if(CollectionUtils.isEmpty(lectures2)) {
					continue;
				}
				
				tCount2 = lectures2.stream().map(e->e.getTimeSlotCount()).reduce((x,y)->x+y).orElse(0);
				if(teacher.getMutexNum() > tCount2) {
					input.appendMsg("老师"+cgTeacher2.getTeacherName()+"有"+tCount2+"节课 却有"+teacher.getMutexNum()+"个互斥课时，请调整");
					continue;
				}
				for(int i=0;i<teacher.getMutexNum();i++){
					if(i >= lectures1.size() || i >= lectures2.size()) {
						continue;
					}
					int lectureId1 = lectures1.get(i).getId();
					for (CGSectionLecture lecture : lectures2) {
						int lectureId2 = lecture.getId();
						
						lectureT = new CGLectureConflict(lectureId1, lectureId2, -1);
						lectureConflictList.add(lectureT);
						lectureT.setId(idNum++);
					}
				}
			}
		}
		
		
		// 所有的 className  和 科目
		Map<String,String> classNameMap = new HashMap<>();
		for (CGCourseSection sec : courseSectionList) {
			if(!classNameMap.containsKey(sec.getOldId())) {
				classNameMap.put(sec.getOldId(), sec.getClassName());
			}
		}
		Map<String,String> subjectNameMap = new HashMap<>();
		for (CGCourse cc : courseList) {
			if(!subjectNameMap.containsKey(cc.getSubjectId())) {
				subjectNameMap.put(cc.getSubjectId(), cc.getCode().replace("A", "").replace("B", ""));
			}
		}
		Function<String,String> codeToName = code->{
			String[] split = code.split("-");
			String cn = Optional.ofNullable(classNameMap.get(split[0])).orElse(split[0]);
			String sn = Optional.ofNullable(subjectNameMap.get(split[1])).orElse(split[1]);
			return cn+"-"+sn;
		};
		
		Map<String,CGCourseSection> sectionCodeMap = courseSectionList.stream().collect(Collectors.toMap(
				e->e.getOldId()+"-"+e.getCourse().getSubjectId()+"-"+e.getCourse().getSubjectType(),Function.identity()
				));
		// 合班 、同时排课
		List<CGMeanwhileClassGroup> groupList = new ArrayList<>();
		CGMeanwhileClassGroup gp = null;
		for (Set<String> cs : meanwhileClassList) {
			Set<Integer> collect = cs.stream().filter(c->sectionCodeMap.containsKey(c)).map(e->sectionCodeMap.get(e).getId()).collect(Collectors.toSet());
			gp = new CGMeanwhileClassGroup();
			gp.setClassSubjectCodeGroup(collect);
			
			if(collect.size() != cs.size()) {
				String errorCode = cs.stream().filter(c->!sectionCodeMap.containsKey(c)).findFirst().get();
				input.appendMsg("同时排课数据异常，找不到某些班级-科目code:"+codeToName.apply(errorCode));
				continue;
			}
			groupList.add(gp);
		}
		Map<String,List<Set<Integer>>> teacherCombClassMap = new HashMap<>();
		for (Set<String> cs : combineClassList) {
			Set<CGCourseSection> sections = cs.stream().filter(c->sectionCodeMap.containsKey(c)).map(e->sectionCodeMap.get(e)).collect(Collectors.toSet());
			Set<Integer> collect = sections.stream().map(e->e.getId()).collect(Collectors.toSet());
			gp = new CGMeanwhileClassGroup();
			gp.setClassSubjectCodeGroup(collect);
			
			if(collect.size() != cs.size()) {
				String errorCode = cs.stream().filter(c->!sectionCodeMap.containsKey(c)).findFirst().get();
				input.appendMsg("合班数据异常，找不到某些班级-科目code:"+codeToName.apply(errorCode));
				continue;
			}
			
			List<CGCourseSection> collect2 = sections.stream().filter(e->e.getTeacher()!=null).collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(collect2)) {
				String tCode = collect2.get(0).getTeacher().getCode();
				List<Set<Integer>> list = teacherCombClassMap.get(tCode);
				if(list == null) {
					list = new ArrayList<>();
					teacherCombClassMap.put(tCode, list);
				}
				list.add(collect2.stream().map(e->e.getId()).collect(Collectors.toSet()));
			}
			
			
			CGCourseSection theOne = sections.stream().max((x,y)->Integer.compare(x.getLectureCount(), y.getLectureCount())).get();
			theOne.setCombineFlag(1);
			sections.stream().filter(e->e!=theOne).forEach(e->e.setCombineFlag(0));
			groupList.add(gp);
		}
		// 不能同时排课
		List<CGNoMeanwhileClassGroup> cgNoMeanWhileClassGroupList = noMeanWhileClassGroups.stream()
				.map(e->Arrays.stream(e).collect(Collectors.toSet()))
				.map(e->new CGNoMeanwhileClassGroup(e)).collect(Collectors.toList());
		
		
		// 检查 老师 的 课时数是否超出 可排课时间
		StringBuilder sb = new StringBuilder();
		Map<CGTeacher, List<CGCourseSection>> teacherSectionMap = courseSectionList.stream().filter(e->e.getTeacher()!=null)
				.collect(Collectors.groupingBy(e->e.getTeacher()));
		for (CGTeacher tea : teacherSectionMap.keySet()) {
			List<CGCourseSection> secList = teacherSectionMap.get(tea);
			List<CGSectionLecture> list = secList.stream().flatMap(e->e.getLectureList().stream()).collect(Collectors.toList());
			Double totalLec = secList.stream()
					.map(e->e.getCourse().getIsBiWeekly()==CGCourse.WEEK_TYPE_NORMAL
							?e.getLectureCount():e.getLectureCount()-0.5)
					.reduce((x,y)->x+y).orElse(0d);
			List<Set<Integer>> combineList = teacherCombClassMap.get(tea.getCode());
			if(CollectionUtils.isNotEmpty(combineList)) {
				// 存在合班时 教师 课程数量的计算
				Set<Integer> allCombineIds = combineList.stream().flatMap(e->e.stream()).collect(Collectors.toSet());
				Double otherLecs = list.stream().filter(e->!allCombineIds.contains(e.getCourseSection().getId()))
						.map(e->e.getIsBiWeekly()==CGCourse.WEEK_TYPE_NORMAL?1:0.5)
						.reduce((x,y)->x+y).orElse(0d);
				Map<CGCourseSection, List<CGSectionLecture>> combiSecMap = list.stream().filter(e->allCombineIds.contains(e.getCourseSection().getId()))
						.collect(Collectors.groupingBy(CGSectionLecture::getCourseSection));
				
				double combCount = 0;
				for (Set<Integer> gp1 : combineList) {
					double max = 0;
					for (CGCourseSection sec : combiSecMap.keySet()) {
						if(!gp1.contains(sec.getId())) {
							continue;
						}
						double lectureCount = sec.getLectureCount();
						if(sec.getCourse().getIsBiWeekly() != CGCourse.WEEK_TYPE_NORMAL) {
							lectureCount -= 0.5;
						}
						if(max < lectureCount) {
							max = lectureCount;
						}
					}
					combCount += max;
				}
				totalLec = otherLecs + combCount;
			}
			
			double periodNum = list.stream().flatMap(e->e.getPeriodList().stream()).distinct().count();
			
			if(totalLec > periodNum) {
				sb.append("\t教师:").append(tea.getTeacherName()).append("的上课时间不足,至少:").append((int)Math.ceil(totalLec)).append("\n");
			}
		}
		if(sb.length()>0) {
			input.appendMsg(sb.toString());
//			throw new RuntimeException(sb.toString());
		}
		
		
		CGConstants cgConstants = new CGConstants();
		cgConstants.setSectionSizeMargin(margin);
		cgConstants.setSectionSizeMean(maxSectionStudentCount);
		cgConstants.setAmCount(input.getAmCount());
		cgConstants.setPmCount(input.getPmCount());
		cgConstants.setMaxTimeslotIndex(maxTimeslotIndex);
		cgConstants.setWorkDayCount(workDayCount);
		
		CGStudentCourseSchedule schedule = new CGStudentCourseSchedule();
		schedule.setCourseList(courseList);
		schedule.setCourseSectionList(courseSectionList);
		schedule.setStudentList(studentList);
		schedule.setLectureList(allLecture);
		schedule.setPeriodList(allCGPeriod);
		schedule.setRoomConstraintList(roomConstraintList);
		schedule.setRoomList(cgRoomList);
		schedule.setTeacherList(teacherList);
		schedule.setArrayId(arrayId);
		schedule.setcGConstants(cgConstants);
		schedule.setLectureConflictList(lectureConflictList);
		schedule.setIntervalConstrainList(intervalConsList);
		schedule.setMeanwhileClassGroup(groupList);
		schedule.setNoMeanwhileClassGroup(cgNoMeanWhileClassGroupList);
		
		schedule.setHardUnavailableList(new ArrayList<>());
		schedule.setSoftUnavailableList(new ArrayList<>());
		schedule.setChangeLess(rawFixedLectures!=null);
		
		return schedule;
	}
	
	private static Map<CGCourseSection, Set<CGPeriod>> freshExistedTable(Map<String, List<NKPeriod>> originExistedTable,
			Map<NKPeriod, CGPeriod> oldToNewTime, List<CGCourseSection> courseSectionList, CGInputData input) {
		Map<CGCourseSection, Set<CGPeriod>> map = new HashMap<>();
		if(originExistedTable == null)
			return map;
		Map<String, CGCourseSection> codeSectionMap = courseSectionList.stream()
				.collect(Collectors.toMap(e -> e.getOldId() + "-" + e.getCourse().getSubjectId(), Function.identity()));
		for (String classSubjectCode : originExistedTable.keySet()) {
			List<NKPeriod> list = originExistedTable.get(classSubjectCode);
			CGCourseSection cgCourseSection = codeSectionMap.get(classSubjectCode);
			if(cgCourseSection == null) {
				// classId-subjectId
				input.appendMsg("无法获取对应班级，班级代码:"+classSubjectCode);
//				throw new RuntimeException("无法获取对应班级，班级代码:"+classSubjectCode);
			}
			Set<CGPeriod> collect = list.stream().filter(e->oldToNewTime.containsKey(e)).map(e->oldToNewTime.get(e)).collect(Collectors.toSet());
			if(collect.size() < list.size()) {
				NKPeriod nkPeriod = list.stream().filter(e->!oldToNewTime.containsKey(e)).findFirst().get();
				input.appendMsg("无法获取部分时间对象:"+nkPeriod.getDayIndex()+"_"+nkPeriod.getPeriodInterval()+"_"+nkPeriod.getOtherIndex()+" 等");
				throw new RuntimeException("无法获取部分时间对象");
			}
			
			map.put(cgCourseSection, collect);
		}
		
		return map;
	}

	/**
	 * 合理分配单双周
	 * @param courseList
	 * @param allLecture
	 * @return
	 */
	private static boolean resetWeekType(List<CGCourse> courseList, List<CGSectionLecture> allLecture) {
		Function<CGCourse,String> cFun = c->c.getCode();
		Map<String, CGCourse> courseSubjectMap = courseList.stream()
				.collect(Collectors.toMap(c->cFun.apply(c), Function.identity()));
		Map<CGCourse, CGCourse> isBiWeeklyMap = new HashMap<>();
		for (CGCourse cc : courseList) {
			if(cc.getIsBiWeekly() != CGCourse.WEEK_TYPE_NORMAL && cc.getIsBiweeklyCourse() != null) {
				CGCourse cgCourse = courseSubjectMap.get(cc.getIsBiweeklyCourse());
				if(cgCourse != null) {
					isBiWeeklyMap.put(cc, cgCourse);
				}
			}
		}
		
		long index0 = 0;
		long index = 0;
		boolean needSolver = false;
		Map<String, List<CGSectionLecture>> biLectureMap = allLecture.stream().filter(e->e.getIsBiWeekly()!=CGCourse.WEEK_TYPE_NORMAL)
				.collect(Collectors.groupingBy(e->e.getCourseSection().getOldId()));
		for (List<CGSectionLecture> ls : biLectureMap.values()) {
			// 先搞配对课程
			Map<CGCourse, CGSectionLecture> collect = ls.stream()
					.filter(c->StringUtils.isNotBlank(c.getCourse().getIsBiweeklyCourse()))
					.collect(Collectors.toMap(l->l.getCourse(),Function.identity()));
			List<CGSectionLecture> lctureListT = new ArrayList<>(ls);
			for (CGCourse courseT : collect.keySet()) {
				CGSectionLecture l = collect.get(courseT);
				if(l.getCourseSection().getIsBiweekly() != CGCourse.WEEK_TYPE_NORMAL){
					lctureListT.remove(l);
				}
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
					needSolver = true;
					if(index0%2 == 0) {
						l.setIsBiWeekly(CGCourse.WEEK_TYPE_EVEN);
						cgSectionLecture.setIsBiWeekly(CGCourse.WEEK_TYPE_ODD);
					}else {
						l.setIsBiWeekly(CGCourse.WEEK_TYPE_ODD);
						cgSectionLecture.setIsBiWeekly(CGCourse.WEEK_TYPE_EVEN);
					}
					
					lctureListT.remove(l);
					lctureListT.remove(cgSectionLecture);
					index0++;
				}
			}
			if(collect.size()%4 == 0) {
				index0++;
			}
			
			// 剩余没有配对的课程
			for (CGSectionLecture cgSectionLecture : lctureListT) {
				needSolver = true;
				if(index%2 == 0) {
					cgSectionLecture.setIsBiWeekly(1);
				}else {
					cgSectionLecture.setIsBiWeekly(-1);
				}
				index++;
			}
			if(ls.size()%2 == 0) {
				index++;
			}
		}

		return needSolver;
	}
	
	private static Float typeToInterval(String type) {
		switch(type) {
		case "01": return 0.1f;
		case "02": return 0.5f;
		case "03": return 2f;
		case "04": return 3f;
		case "05": return 1f;
		default: return 0f;
		}
	}
	private static Map<CGCourse, Set<CGPeriod>> freshCoursePeriods(Map<CGCourse, List<NKPeriod>> subjectCoupleTimeMap,
			Map<NKPeriod, CGPeriod> oldToNewTime, CGInputData input) {
		// 
		Map<CGCourse, Set<CGPeriod>> map = new HashMap<>();
		for (CGCourse c : subjectCoupleTimeMap.keySet()) {
			List<NKPeriod> list = subjectCoupleTimeMap.get(c);
			
			if(!"1".equals(c.getCoupleTimeLimit())) {
				continue;
			}
			if(CollectionUtils.isEmpty(list) || list.size()<2) {
				input.appendMsg(c.getCode()+"连排时间点太少");
			}
			Set<CGPeriod> collect = list.stream().map(p->oldToNewTime.get(p)).collect(Collectors.toSet());
			map.put(c, collect);
		}
		return map;
	}
	

	@SuppressWarnings("unchecked")
	private static <T> T getRandomOne(Collection<T> list) {
		int range = list.size();
		T one = (T)list.toArray()[(int)(Math.random() * range)];
		return one;
	}

	private static Map<CGCourseSection, Set<CGPeriod>> freshClassPeriods(Map<CGClass, Set<NKPeriod>> doClassPeriods,
			Map<NKPeriod, CGPeriod> oldToNewTime, Map<CGClass, CGCourseSection> oldClassToNewMap) {
		Map<CGCourseSection, Set<CGPeriod>> cgDoClassPeriods = new HashMap<>();
		for (CGClass cgclass : doClassPeriods.keySet()) {
			Set<NKPeriod> set = doClassPeriods.get(cgclass);
			
			CGCourseSection cgSection = oldClassToNewMap.get(cgclass);
			Set<CGPeriod> newSet = set.stream().map(e->oldToNewTime.get(e)).collect(Collectors.toSet());
			
			cgDoClassPeriods.put(cgSection, newSet);
		}
		return cgDoClassPeriods;
	}

	@SuppressWarnings("unused")
	private static void manulMakePeriods(List<CGCourse> courseList, List<NKPeriod> periodList,
			List<NKPeriod> nkliSharePeriods, List<NKPeriod> nkwenSharePeriods,
			Map<CGCourse, List<NKPeriod>> doCoursePeriods) {
		List<NKPeriod>  jxbPeriodList = periodList.stream().filter(e->e.getTimeslotIndex()<=7 || 
				(e.getTimeslotIndex() <= 8 && e.getDayIndex() <= 1))
				.collect(Collectors.toList());
		List<NKPeriod> xzbPeriodList = new ArrayList<>(periodList);
		xzbPeriodList.removeAll(jxbPeriodList);
		
		doCoursePeriods.clear();
		courseList.stream().forEach(e->{
			if(e.getType().equals(CourseType.COMPULSORY_COURSE)) {
				doCoursePeriods.put(e, xzbPeriodList);
			}else {
				doCoursePeriods.put(e, jxbPeriodList);
			}
		});
		nkliSharePeriods.clear();
		nkwenSharePeriods.clear();
	}


	private static List<CGPeriod> getLocalPeriodList(
			List<CGPeriod> availablePeriod, Map<CGCourseSection, Set<CGPeriod>> cgDoClassPeriods,
			Map<CGCourseSection, Set<CGPeriod>> cgNoClassPeriods, CGCourseSection courseSection) {
		List<CGPeriod> coursePeroidList = new ArrayList<CGPeriod>(availablePeriod);
		if(cgDoClassPeriods.size() > 0){
			
			coursePeroidList.clear();
			Set<CGPeriod> listT = cgDoClassPeriods.get(courseSection);
			if(CollectionUtils.isNotEmpty(listT)) {
				coursePeroidList.addAll(listT);
			}
		}
		
		if(cgNoClassPeriods.size() > 0){
			if(CollectionUtils.isNotEmpty(cgNoClassPeriods.get(courseSection))){
				coursePeroidList.removeAll(cgNoClassPeriods.get(courseSection));
			}
		}
		return coursePeroidList;
	}

	private static Map<CGCourseSection, CGRoom> freshXzbRoomConstrains(
			Map<CGClass, NKRoom> xzbRoomConstrains,
			Map<NKRoom, CGRoom> oldToNewRoomMap, Map<CGClass, CGCourseSection> oldClassToNewMap, CGInputData input) {
		Map<CGCourseSection, CGRoom> newXzbRoomConstraintMap = new HashMap<>();
		if (xzbRoomConstrains == null) {
			return newXzbRoomConstraintMap;
		}
		for (Entry<CGClass, NKRoom> entry : xzbRoomConstrains.entrySet()) {
			CGClass cgClass = entry.getKey();
			NKRoom nkRoom = entry.getValue();

			CGCourseSection cgCourseSection = oldClassToNewMap.get(cgClass);
			CGRoom cgRoom = oldToNewRoomMap.get(nkRoom);
			if(cgCourseSection != null && cgRoom != null) {
				newXzbRoomConstraintMap.put(cgCourseSection, cgRoom);
			}else {
				input.appendMsg("未知错误 1，"+DtoToData.class+" freshXzbRoomConstrains()");
			}
		}

		return newXzbRoomConstraintMap;
	}

	public static List<CGSectionConflict> calculateCourseConflictList(List<CGCourseSection> courseSectionList) {
        List<CGSectionConflict> sectionConflictList = new ArrayList<CGSectionConflict>();
        for (CGCourseSection leftSection : courseSectionList) {
            for (CGCourseSection rightSection : courseSectionList) {
                if (leftSection.getId() <= rightSection.getId()) {
                	
                	if (checkSectionsConflict(leftSection, rightSection)) 
                		sectionConflictList.add(new CGSectionConflict(leftSection, rightSection, 1));
                }
            }
        }
        
        return sectionConflictList;
    }
	
	private static boolean checkSectionsConflict(CGCourseSection section1, CGCourseSection section2) {
		
		Set<CGStudent> studentSet1 = section1.getStudentList().stream().collect(Collectors.toSet());
		Set<CGStudent> studentSet2 = section2.getStudentList().stream().collect(Collectors.toSet());
		
		return Sets.intersection(studentSet1, studentSet2).size() > 0;
	}
	
	private static int getCoupleCount(Set<CGPeriod> periodSet) {
		Map<CGDay, List<CGPeriod>> dayPMap = periodSet.stream().collect(Collectors.groupingBy(CGPeriod::getDay));
		int count = 0;
		for (List<CGPeriod> pl : dayPMap.values()) {
			if(pl.size()<2)
				continue;
			Collections.sort(pl);
			
			int now = 0;
			int next = 1;
			while(next<pl.size()) {
				int nowIndex = pl.get(now).getTimeslot().getTimeslotIndex();
				int nextIndex = pl.get(next).getTimeslot().getTimeslotIndex();
				if(nowIndex+1 == nextIndex) {
					count++;
					now = next+1;
				}else{
					now++;
				}
				next = now +1;
			}
		}
		
		
		return count;
	}
}
