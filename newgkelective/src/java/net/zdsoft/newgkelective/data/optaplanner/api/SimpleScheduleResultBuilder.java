package net.zdsoft.newgkelective.data.optaplanner.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourse;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourseSection;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGDay;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGPeriod;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGRoom;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGSectionLecture;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGTeacher;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGTimeslot;
import net.zdsoft.newgkelective.data.optaplanner.dto.CGInputData;
import net.zdsoft.newgkelective.data.optaplanner.util.XmlUtils;

public class SimpleScheduleResultBuilder {

	public static void main(String[] args) throws FileNotFoundException {
		SimpleScheduleResultBuilder builder = new SimpleScheduleResultBuilder();
		
		
		File file = new File("C:\\Users\\user\\Desktop\\simpleSchedule数据文件\\json\\"
				+"郴州一中2019-3-6 14-23-04.json");
		FileInputStream fs = new FileInputStream(file);
		CGInputData result = builder.build(fs);
		
		
		String unitName = file.getName().replaceAll("(\\d{4}-\\d+-\\d+ \\d+-\\d+-\\d+)|(\\.json)", "");
		XmlUtils.toXMLFile(result, "C:\\Users\\user\\Desktop\\xml排课结果\\", 
				unitName//.replaceAll(".json", "")
				+new Date().toLocaleString().replaceAll(":", "-")+".xml");
		System.out.println(unitName+"\t排课结果解析完毕");
	}
	
	public CGInputData build(InputStream inputStream) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		
		CGInputData result = null;
		String exMsg = null;
		try {
			SimpleCourseSchedulerInput simpleSchedule = objectMapper.readValue(inputStream, SimpleCourseSchedulerInput.class);
			result = build(simpleSchedule);
		} catch (JsonParseException e) {
			e.printStackTrace();
			exMsg = e.getMessage()+"";
		} catch (JsonMappingException e) {
			e.printStackTrace();
			exMsg = e.getMessage()+"";
		} catch (IOException e) {
			e.printStackTrace();
			exMsg = e.getMessage()+"";
		}
		
		if(StringUtils.isNotBlank(exMsg))
			throw new RuntimeException("排课结果解析异常："+exMsg);
			
		return result;
	}
	public CGInputData build(SimpleCourseSchedulerInput simpleSchedule) {
		
		// 课程分组：(O)语数外，(A)选课，(B)学课，(X)其它， {CourseID, CourseType, subjectId, needRoom}
		List<List<String>> courseList = simpleSchedule.getCourseList();
		//<sectionID，classId,subjectId,subjectType>
		List<List<String>> sectionList = simpleSchedule.getSectionList();
		// 需要算法给出时间点的Lecture, {CourseID, SectionID, LectureID, RoomID, TeacherID}
		// 如果是单双周的课，{CourseID, SectionID, LectureID, RoomID, 单周TeacherID, 双周TeacherID}
		List<List<String>> lectureList = simpleSchedule.getLectureList();
		//单双周标记信息，记录 合成的lecture隐藏的一门科目
		//<lectureID,subjectId,subjectType, teacherId>
		List<List<String>> coupleList = simpleSchedule.getCoupleList();
		// 联排课：{LectureID, timeSlotCount}, 目前只支持 timeSlotCount==2
		List<List<String>> lectureTwinList = simpleSchedule.getLectureTwinList();
		// {PeriodID, DayIndex, TimeSlotIndex, AM|PM},
		List<List<String>> periodList = simpleSchedule.getPeriodList();
		 //{LectureID, PeriodID}
		List<List<String>> lectureFixedList = simpleSchedule.getLectureFixedList();
		
		
		//1. courseList
		List<CGCourse> courseEntityList = new ArrayList<>();
		CGCourse cgCourse;
		for (List<String> courseItem : courseList) {
			cgCourse = new CGCourse();
			
			cgCourse.setSubjectType(courseItem.get(1));
			cgCourse.setSubjectId(courseItem.get(2));
			if(cgCourse.getSubjectType().equals("X")) {
				cgCourse.setSubjectType("O");
			}
			cgCourse.setCode(cgCourse.getSubjectId()+cgCourse.getSubjectType());
			if(courseItem.size()>3) {
				cgCourse.setNeedRoom(Integer.parseInt(courseItem.get(3)));
			}else {
				cgCourse.setNeedRoom(1);
			}
			courseEntityList.add(cgCourse);
		}
		
		//2. periodList
		Map<String,CGPeriod> periodEntityMap = new HashMap<>();
		Map<String,CGDay> dayEntityMap = new HashMap<>();
		Map<String,CGTimeslot> timeslotEntityMap = new HashMap<>();
		
		CGPeriod period;
		//{PeriodID, DayIndex, TimeSlotIndex, AM|PM},
		for (List<String> pItem : periodList) {
			period = new CGPeriod();
			CGDay day = dayEntityMap.get(pItem.get(1));
			if(day == null) {
				day = new CGDay();
				day.setDayIndex(Integer.parseInt(pItem.get(1)));
//				day.setPeriodList(new ArrayList<>());
				dayEntityMap.put(day.getDayIndex()+"", day);
			}
			CGTimeslot slot = timeslotEntityMap.get(pItem.get(2));
			if(slot == null) {
				slot = new CGTimeslot();
				slot.setTimeslotIndex(Integer.parseInt(pItem.get(2)));
				slot.setPeriodInterval(pItem.get(3));
				timeslotEntityMap.put(slot.getTimeslotIndex()+"", slot);
			}
			
//			day.getPeriodList().add(period);
			period.setDay(day);
			period.setTimeslot(slot);
			
			periodEntityMap.put(period.getSimpleCode(), period);
		}

		// 3. fixed lecture time: lectureFixedList
		//<lectureId, periodID>
		Map<Integer, CGPeriod> lectureTimeMap = new HashMap<>();
		for (List<String> fix : lectureFixedList) {
			CGPeriod cgPeriod = periodEntityMap.get(fix.get(1));
			if(cgPeriod == null)
				throw new RuntimeException("找不到 时间："+fix.get(1));
			lectureTimeMap.put(Integer.parseInt(fix.get(0)), cgPeriod);
		}
		
		// #. 单双周 TODO
		Map<String, CGCourse> coruseCodeMap = courseEntityList.stream().collect(Collectors.toMap(CGCourse::getCode, e->e));
		Map<Integer, CGCourse> coupleMap = new HashMap<>();
		for (List<String> couple : coupleList) {
			if(couple.get(2).equals("X"))
				couple.set(2, "O");
			CGCourse cgCourse2 = coruseCodeMap.get(couple.get(1)+couple.get(2));
			if(cgCourse2 == null) {
				cgCourse2 = new CGCourse();
				cgCourse2.setSubjectId(couple.get(1));
				cgCourse2.setSubjectType(couple.get(2));
				cgCourse2.setCode(cgCourse2.getSubjectId()+cgCourse2.getSubjectType());
				courseEntityList.add(cgCourse2);
				coruseCodeMap.put(cgCourse2.getCode(), cgCourse2);
			}
			
			coupleMap.put(Integer.parseInt(couple.get(0)), cgCourse2);
		}
		
		
		//2. sectionList
		// <sectionID，classId,subjectId,subjectType>
		List<CGCourseSection> sectionEntityList = new ArrayList<>();
		CGCourseSection section;
		for (List<String> sectionItem : sectionList) {
			section = new CGCourseSection();
			
			section.setId(Integer.parseInt(sectionItem.get(0)));
			section.setOldId(sectionItem.get(1));
			
			if(sectionItem.get(3).equals("X")) {
				sectionItem.set(3, "O");
			}
			CGCourse cgCourse2 = coruseCodeMap.get(sectionItem.get(2)+sectionItem.get(3));
			if(cgCourse2==null)
				throw new RuntimeException("排课结果解析找不到 课程："+sectionItem.get(2)+sectionItem.get(3));
			section.setCourse(cgCourse2);
			section.setLectureList(new ArrayList<>());
			
			sectionEntityList.add(section);
		}
		
		
		//3. lectureList
		// 需要算法给出时间点的Lecture, {CourseID, SectionID, LectureID, RoomID, TeacherID}
		// 如果是单双周的课，{CourseID, SectionID, LectureID, RoomID, 单周TeacherID, 双周TeacherID}
		Map<String, CGCourseSection> sectionMap = sectionEntityList.stream().collect(Collectors.toMap(e->e.getId()+"", e->e));
		Map<String, CGCourseSection> sectionCodeMap = sectionEntityList.stream().collect(Collectors.toMap(e->e.getCourseSectionCode()+"", e->e));
		Set<Integer> twinLectures = lectureTwinList.stream().map(e->Integer.parseInt(e.get(0))).collect(Collectors.toSet());
		
		List<CGSectionLecture> lectureEntityList = new ArrayList<>();
		Map<String,CGRoom> roomEntityMap = new HashMap<>();
		Map<String,CGTeacher> teacehrEntityMap = new HashMap<>();
		
		CGSectionLecture lecture;
		CGSectionLecture lecture2;
		CGRoom room; 
		CGTeacher teacehr;
		for (List<String> lectureItem : lectureList) {
			lecture = new CGSectionLecture();
			// section course
			lecture.setId(Integer.parseInt(lectureItem.get(2)));
			lecture.setCourseSection(sectionMap.get(lectureItem.get(1)));
			lecture.setCourse(lecture.getCourseSection().getCourse());
			lecture.setTimeSlotCount(1);
			// room 
			if(StringUtils.isNotBlank(lectureItem.get(3))) {
				room = roomEntityMap.get(lectureItem.get(3));
				if(room == null) {
					room = new CGRoom();
					room.setCode(lectureItem.get(3));
					roomEntityMap.put(room.getCode(), room);
				}
				lecture.setRoom(room);
			}
			
			// teacher
			if(StringUtils.isNotBlank(lectureItem.get(4))) {
				teacehr = teacehrEntityMap.get(lectureItem.get(4));
				if(teacehr == null) {
					teacehr = new CGTeacher();
					teacehr.setCode(lectureItem.get(4));
					teacehrEntityMap.put(teacehr.getCode(), teacehr);
				}
				lecture.getCourseSection().setTeacher(teacehr);
			}
			lecture.setIsBiWeekly(CGCourse.WEEK_TYPE_NORMAL);
			//lecture time
			CGPeriod cgPeriod = lectureTimeMap.get(lecture.getId());
			lecture.setPeriod(cgPeriod);
			lectureEntityList.add(lecture);
			if(lectureItem.size() >= 6) {
				lecture.setIsBiWeekly(CGCourse.WEEK_TYPE_ODD);
				//TODO 
				CGCourse cgCourse2 = coupleMap.get(lecture.getId());
				if(cgCourse2 != null)
					lecture.getCourse().setIsBiweeklyCourse(cgCourse2.getCode());
				else 
					throw new RuntimeException("找不到 隐藏的单双周");

				CGCourseSection section2 = sectionCodeMap.get(lecture.getCourseSection().getOldId()+"-"+cgCourse2.getSimpleCode());
				if(section2 == null) {
					section2 = new CGCourseSection();
					section2.setOldId(lecture.getCourseSection().getOldId());
					section2.setCourse(cgCourse2);	
					section2.setLectureList(new ArrayList<>());
					sectionEntityList.add(section2);
					sectionCodeMap.put(section2.getCourseSectionCode(), section2);
					
//					String tcode = coupleTeaMap.get(lecture.getId());
					String tcode = lectureItem.get(5);
					if(StringUtils.isNotBlank(tcode)) {
						CGTeacher cgTeacher = teacehrEntityMap.get(tcode);
						if(cgTeacher == null) {
							cgTeacher = new CGTeacher();
							cgTeacher.setCode(tcode);
							teacehrEntityMap.put(tcode, cgTeacher);
						}
						section2.setTeacher(cgTeacher);
					}
				}
				// 被合成的那一节课 缺少老师和 场地
				lecture2 = new CGSectionLecture();
				lecture2.setTimeSlotCount(1);
				lecture2.setCourseSection(section2);
				lecture2.setPeriod(lecture.getPeriod());  //TODO
				lecture2.setRoom(lecture.getRoom());
				lecture2.setIsBiWeekly(CGCourse.WEEK_TYPE_EVEN);
				
				lectureEntityList.add(lecture2);
				// 被合成的单双周
//				teacehr = teacehrEntityMap.get(lectureItem.get(5));
//				if(teacehr == null) {
//					teacehr = new CGTeacher();
//					teacehr.setCode(lectureItem.get(5));
//					teacehrEntityMap.put(teacehr.getCode(), teacehr);
//				}
//				lecture.getCourseSection().setTeacher(teacehr);
			}
			
			// 连排
			if(twinLectures.contains(lecture.getId())) {
				lecture.setTimeSlotCount(2);
				
				CGSectionLecture l2 = new CGSectionLecture();
				l2.setCourseSection(lecture.getCourseSection());
				l2.setCourse(lecture.getCourse());
				l2.setRoom(lecture.getRoom());
//				l2.setPeriod(periodEntityMap.get(lecture.getPeriod().getSimpleOffSetCode(1)));
//				if(l2.getPeriod()==null)
//					throw new RuntimeException("periodCode:"+lecture.getPeriod().getSimpleOffSetCode(1)+"没有对象");
				l2.setIsBiWeekly(CGCourse.WEEK_TYPE_NORMAL);
				
				lectureEntityList.add(l2);
			}
		}
		
		lectureEntityList.forEach(e->e.getCourseSection().getLectureList().add(e));
		
		/* 结果 */
		CGInputData resultData =  new CGInputData();
		resultData.setLectureList(lectureEntityList);
		resultData.setSectionList(sectionEntityList);
		resultData.setArrayId(simpleSchedule.getArrayId());
		
		return resultData;
	}
}
