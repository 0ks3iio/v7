package net.zdsoft.newgkelective.data.optaplanner.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import net.zdsoft.echarts.common.StringUtils;

public class SimpleValidator {

	public static void main(String[] args) {
		String filePath = "C:\\Users\\user\\Desktop\\simpleSchedule数据文件\\json\\";
		List<String> fileNames = new ArrayList<>();
//		String fileName = "";
//		fileName = "山东邹平一中2018-7-25 18-21-48"+".xml";
//		fileNames.add(fileName);
		
		String[] list = new File(filePath).list();
		fileNames = Arrays.asList(list);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		
		try {
			for (String s : fileNames) {
				if(!s.endsWith("json"))
					continue;

				SimpleCourseSchedulerInput inputValue = objectMapper.readValue(new File(filePath, s), SimpleCourseSchedulerInput.class);
				checkValidity(inputValue);
				System.out.println(s+" 检验结束\n\n");
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void checkValidity(SimpleCourseSchedulerInput inputValue) {
		//  {CourseID, CourseType, subjectId}
		List<List<String>> courseList = inputValue.getCourseList();
		//，{CourseID, SectionID, LectureID, RoomID, 单周TeacherID, 双周TeacherID}
		List<List<String>> lectureList = inputValue.getLectureList();
		// <sectionID，classId,subjectId,subjectType>
		List<List<String>> sectionList = inputValue.getSectionList();
		// {PeriodID, DayIndex, TimeSlotIndex, AM|PM}, DayIndex: 0..6,
		List<List<String>> periodList = inputValue.getPeriodList();
		
		// {LectureID, PeriodID}
		List<List<String>> lectureFixedList = inputValue.getLectureFixedList();
		//<lectureID,subjectId,subjectType>
		List<List<String>> coupleList = inputValue.getCoupleList();
		//联排课：{LectureID, timeSlotCount}, 目前只支持 timeSlotCount==2
		List<List<String>> lectureTwinList = inputValue.getLectureTwinList();
		 //相同老师引起的冲突：{LectureID1, LectureID2}
		List<List<String>> lectureBinaryConflictConstraintList = inputValue.getLectureBinaryConflictConstraintList();
		//{LectureID, PeriodIds, Level: 0..5}， PeriodIds:多个periodId连接 用 "|" 连接。 Level:0表示禁排，1~5表示喜好程度。隐含了 domainList
		List<List<String>> lecturePreferredPeriodConstraintList = inputValue.getLecturePreferredPeriodConstraintList();

		// 同排课, 一行一组，必须排在同一个时间点：{LectureID1, LectureID2, ...}
		List<List<String>> lectureSamePeriodConstraintList = inputValue.getLectureSamePeriodConstraintList();

		// 互斥老师：{TeacherID1, TeacherID2, NonOverlappingCount}
		List<List<String>> teacherNonOverlappingCountList = inputValue.getTeacherNonOverlappingCountList();
		
		// 同一个行政班的2个课，不能排在同一个半天的2个时间点，{LectureID1, LectureID2}
		List<List<String>> lectureNotSameHalfDayConstraintList = inputValue.getLectureNotSameHalfDayConstraintList();

		// 同一个行政班的2个课，不能排在同一天的2个时间点，{LectureID1, LectureID2}
		List<List<String>> lectureNotSameDayConstraintList = inputValue.getLectureNotSameDayConstraintList();

		// 同一个行政班的2个课，不能排在连续的2个时间点，{LectureID1, LectureID2}
		List<List<String>> lectureNotNextConstraintList = inputValue.getLectureNotNextConstraintList();
		
		// 教师教学进度一致
		//sectionIDstr: 多个sectionID使用 | 连接，表示老师带的这几个班级需要教学进度一致
		// <teacehrID,sectionIDstr>  sectionIDstr
		List<List<String>> teacherPlanList = inputValue.getTeacherPlanList();
		
		Set<String> lecIds = lectureList.stream().map(e->e.get(2)).collect(Collectors.toSet());
		Set<String> sectionIds = sectionList.stream().map(e->e.get(0)).collect(Collectors.toSet());
		Set<String> periodCodes = periodList.stream().map(e->e.get(0)).collect(Collectors.toSet());
		Set<String> courseIds = courseList.stream().map(e->e.get(0)).collect(Collectors.toSet());
		// subjectId-subjectType
		Set<String> courseCodes = courseList.stream().map(e->e.get(2)+"-"+e.get(1)).collect(Collectors.toSet());
		Set<String> tids = new HashSet<>();
		
		Set<String> courseIdInlec = new HashSet<>();
		Set<String> sectionIdInlec = new HashSet<>();
		for (List<String> lec : lectureList) {
			String courseId = lec.get(0);
			String sec = lec.get(1);
			if(!courseIds.contains(courseId)) {
				System.out.println("lectureId:"+lec.get(2)+" -courseId not found");
			}
			courseIdInlec.add(courseId);
			if(!sectionIds.contains(sec)) {
				System.out.println("lectureId:"+lec.get(2)+" -sectionId not found");
			}
			sectionIdInlec.add(sec);
			if(StringUtils.isNotBlank(lec.get(4))) {
				tids.add(lec.get(4));
			}else {
				System.out.println("tid1 not found in lec:"+lec.get(2));
			}
			if(StringUtils.isNotBlank(lec.get(5))) {
				tids.add(lec.get(5));
			}else {
				System.out.println("tid2 not found in lec:"+lec.get(2));
			}
		}
		for (List<String> sec : sectionList) {
			String courseCode = sec.get(2)+"-"+sec.get(3);
			if(!courseCodes.contains(courseCode)) {
				System.out.println("section:"+sec.get(0)+" -courseCode:"+courseCode+" not found");
			}
			if(!sectionIdInlec.contains(sec.get(0))) {
				System.out.println("section:"+sec.get(0)+" not found in lecturesection");
			}
		}
		for (List<String> course : courseList) {
			if(!courseIdInlec.contains(course.get(0))) {
				System.out.println("courseId:"+ course.get(0) +" not found in lecturecourse");
			}
		}
		
		Map<String, List<String>> collect = periodList.stream()
				.collect(Collectors.groupingBy(e->e.get(1),Collectors.mapping(e->e.get(2), Collectors.toList())));
		boolean allMatch = collect.values().stream()
				.allMatch(e->isContinuous(e));
		if(!allMatch) {
			System.out.println("period timeslot 不连续");
		}
		
		
		for (List<String> ent : lectureFixedList) {
			String lecId = ent.get(0);
			String pdId = ent.get(1);
			if(!lecIds.contains(lecId)) {
				System.out.println("lecId:"+lecId+" not found in lectureFixedList");
			}
			if(!periodCodes.contains(pdId)) {
				System.out.println("pdId:"+pdId+" not found in lectureFixedList");
			}
		}
		for (List<String> ent : coupleList) {
			String lecId = ent.get(0);
			String courseCode = ent.get(1)+"-"+ent.get(2);
			if(!courseCodes.contains(courseCode)) {
				System.out.println(" -courseCode:"+courseCode+" not found in coupleList");
			}
			if(!lecIds.contains(lecId)) {
				System.out.println("lecId:"+lecId+" not found in coupleList");
			}
		}
		for (List<String> ent : lectureTwinList) {
			String lecId = ent.get(0);
			if(!lecIds.contains(lecId)) {
				System.out.println("lecId:"+lecId+" not found in lectureTwinList");
			}
		}
		for (List<String> ent : lectureBinaryConflictConstraintList) {
			String lecId = ent.get(0);
			String lecId2 = ent.get(1);
			if(!lecIds.contains(lecId)) {
				System.out.println("lecId:"+lecId+" not found in lectureBinaryConflictConstraintList");
			}
			if(!lecIds.contains(lecId2)) {
				System.out.println("lecId2:"+lecId2+" not found in lectureBinaryConflictConstraintList");
			}
		}
		for (List<String> ent : lecturePreferredPeriodConstraintList) {
			String lecId = ent.get(0);
			String[] periodCodesT = ent.get(1).split("\\|");
			if(!lecIds.contains(lecId)) {
				System.out.println("lecId:"+lecId+" not found in lecturePreferredPeriodConstraintList");
			}
			if(StringUtils.isBlank(ent.get(1)) || periodCodesT.length<1) {
				System.out.println("periodS is blank in lecturePreferredPeriodConstraintList");
				continue;
			}
			for (String pc : periodCodesT) {
				if(!periodCodes.contains(pc)) {
					System.out.println("periodCode:"+pc+" not found in lecturePreferredPeriodConstraintList;lecId:"+lecId);
				}
			}
		}
		
		for (List<String> ent : lectureSamePeriodConstraintList) {
			for (String lecId : ent) {
				if(!lecIds.contains(lecId)) {
					System.out.println("lecId:"+lecId+" not found in lectureSamePeriodConstraintList");
				}
			}
		}
		for (List<String> ent : teacherNonOverlappingCountList) {
			if(!tids.contains(ent.get(0))) {
				System.out.println("tid:"+ent.get(0)+" not found in teacherNonOverlappingCountList");
			}
			if(!tids.contains(ent.get(1))) {
				System.out.println("tid2:"+ent.get(1)+" not found in teacherNonOverlappingCountList");
			}
		}
		for (List<String> ent : lectureNotSameHalfDayConstraintList) {
			String lecId = ent.get(0);
			String lecId2 = ent.get(1);
			if(!lecIds.contains(lecId)) {
				System.out.println("lecId:"+lecId+" not found in lectureNotSameHalfDayConstraintList");
			}
			if(!lecIds.contains(lecId2)) {
				System.out.println("lecId2:"+lecId2+" not found in lectureNotSameHalfDayConstraintList");
			}
		}
		for (List<String> ent : lectureNotSameDayConstraintList) {
			String lecId = ent.get(0);
			String lecId2 = ent.get(1);
			if(!lecIds.contains(lecId)) {
				System.out.println("lecId:"+lecId+" not found in lectureNotSameDayConstraintList");
			}
			if(!lecIds.contains(lecId2)) {
				System.out.println("lecId2:"+lecId2+" not found in lectureNotSameDayConstraintList");
			}
		}
		for (List<String> ent : lectureNotNextConstraintList) {
			String lecId = ent.get(0);
			String lecId2 = ent.get(1);
			if(!lecIds.contains(lecId)) {
				System.out.println("lecId:"+lecId+" not found in lectureNotNextConstraintList");
			}
			if(!lecIds.contains(lecId2)) {
				System.out.println("lecId2:"+lecId2+" not found in lectureNotNextConstraintList");
			}
		}
		for (List<String> ent : teacherPlanList) {
			String tid = ent.get(0);
			String sectionIdStr = ent.get(1);
			if(!tids.contains(tid)) {
				System.out.println("tid:"+tid+" not found in teacherPlanList");
			}
			String[] sectionIdsT = sectionIdStr.split("\\|");
			if(StringUtils.isBlank(sectionIdStr) || sectionIdsT.length<1) {
				System.out.println("sectionIdStr is blank in teacherPlanList;tid:"+tid);
				continue;
			}
			for (String secId : sectionIdsT) {
				if(!sectionIds.contains(secId)) {
					System.out.println("sectionId not found in teacherPlanList;tid:"+tid);
				}
			}
		}
	}

	private static boolean isContinuous(List<String> list) {
		List<Integer> collect = list.stream().map(e->Integer.parseInt(e)).sorted().collect(Collectors.toList());
		
		int pre = -1;
		for (Integer now : collect) {
			if(pre == -1) {
				pre = now;
				continue;
			}
			if((now - pre) != 1) {
				return false;
			}
			pre = now;
		}
		return true;
	}
	
}
