package net.zdsoft.newgkelective.data.optaplanner.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourse;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourseSection;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGDay;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGPeriod;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGRoom;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGRoomConstraint;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGSectionConflict;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGSectionLecture;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGStudent;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGStudentCourseSchedule;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGTeacher;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGUnavailablePeriodPenaltyHard;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGUnavailablePeriodPenaltySoft;

public class SchedulingSolutionValidator {
	
	private List<CGStudent> studentList;
	private List<CGCourse>  courseList;
	private List<CGPeriod>  periodList;
	private List<CGRoom>    roomList;
	private List<CGTeacher> teacherList;
	
	private List<CGCourseSection> 	courseSectionList;
	
	private List<CGSectionLecture> 	lectureList;
	
	private List<CGRoomConstraint> 	roomConstraintList; //算法中通过限定domain的方式实现                     
	@XStreamOmitField
	private List<CGSectionConflict>	sectionConflictList;
	private List<CGUnavailablePeriodPenaltySoft> softUnavailableList;
	private List<CGUnavailablePeriodPenaltyHard> hardUnavailableList;
	
	public boolean validate(CGStudentCourseSchedule solution){
		// 取出数据
		studentList = solution.getStudentList();
		courseList = solution.getCourseList();
		periodList = solution.getPeriodList();
		roomList = solution.getRoomList();
		teacherList = Optional.ofNullable(solution.getTeacherList()).orElse(new ArrayList<CGTeacher>());
		courseSectionList = solution.getCourseSectionList();
		lectureList = solution.getLectureList();
		roomConstraintList = Optional.ofNullable(solution.getRoomConstraintList()).orElse(new ArrayList<CGRoomConstraint>());
		sectionConflictList = Optional.ofNullable(solution.getSectionConflictList()).orElse(new ArrayList<CGSectionConflict>());
		softUnavailableList = Optional.ofNullable(solution.getSoftUnavailableList()).orElse(new ArrayList<CGUnavailablePeriodPenaltySoft>());
		hardUnavailableList = Optional.ofNullable(solution.getHardUnavailableList()).orElse(new ArrayList<CGUnavailablePeriodPenaltyHard>());
		
		HashSet<CGCourseSection> sectionSet = new HashSet<>(courseSectionList);
		HashSet<CGSectionLecture> lectureSet = new HashSet<>(lectureList);
		HashSet<CGPeriod> periodSet = new HashSet<>(periodList);
		HashSet<CGRoom> roomSet = new HashSet<>(roomList);
		HashSet<CGCourse> courseSet = new HashSet<>(courseList);
		
		
		Map<CGCourse, Integer> courseCountTimeMap = new HashMap<>();  // 每周上几次课
		for (CGCourseSection cgCourseSection : courseSectionList) {
			if(!courseCountTimeMap.containsKey(cgCourseSection.getCourse())){
				courseCountTimeMap.put(cgCourseSection.getCourse(), cgCourseSection.getLectureCount());
			}
		}
		
		boolean success = true;
		// 1.先输出数据 总数量
//		System.out.println("本次排课各个数据量如下：");
//		System.out.println("学生:\t"+studentList.size()+"个");
//		System.out.println("课程:\t"+courseList.size()+"种，详情：\n");
//		System.out.println("\n教师:\t"+teacherList.size()+"个");
//		System.out.println("班级:\t"+courseSectionList.size()+"个");
//		System.out.println("教室:\t"+roomList.size()+"个");
//		System.out.println("一周可用时间点:\t"+periodList.size()+"个");
//		System.out.println("学校本学期一周最多有:\t"+lectureList.size()+"个课堂");
//		System.out.println("强时间约束：\t"+hardUnavailableList.size()+"个");
//		System.out.println("弱时间约束：\t"+softUnavailableList.size()+"个");
//		System.out.println("教室约束：\t"+roomConstraintList.size()+"个");
		
		// 2. 逐一验证
		// 2.1 period LectureList 验证
		Set<CGDay> cgDaySet = new HashSet<>();
		for (CGPeriod cgPeriod : periodList) {
			if(CollectionUtils.isNotEmpty(cgPeriod.getLectureList())){
				for (CGSectionLecture cgLecture : cgPeriod.getLectureList()) {
					if(!cgLecture.getPeriod().equals(cgPeriod)){
						System.out.println("CGDay： ERROR-- day:"+cgPeriod.getDay().getDayIndex()+" timeslot:"
								+ cgPeriod.getTimeslot().getTimeslotIndex()+" 与其所包含的 lecture不匹配");
						success = false;
					}
					if(!lectureList.contains(cgLecture)){
						System.out.println("CGDay： ERROR--:"+cgPeriod.getDay().getDayIndex()+" timeslot:"
								+ cgPeriod.getTimeslot().getTimeslotIndex()+"其包含的lectureist不在总的lectureist之内");
						success = false;
					}				
				}
			}
			cgDaySet.add(cgPeriod.getDay());
		}
		for (CGDay cgDay : cgDaySet) {
			if(CollectionUtils.isNotEmpty(cgDay.getPeriodList())){
				for (CGPeriod cgPeriod : cgDay.getPeriodList()) {
					if(!cgPeriod.getDay().equals(cgDay)){
						System.out.println("CGDay： ERROR--:"+cgDay.getDayIndex()+"与其所包含的 Period不匹配");
						success = false;
					}
					if(!periodSet.contains(cgPeriod)){
						System.out.println("CGDay： ERROR--:"+cgDay.getDayIndex()+"其包含的periodlist不在总的periodList之内");
						success = false;
					}
				}
			}
		}
		
		// 2.3 CGCourseSection
		for (CGCourseSection cgCourseSection : courseSectionList) {
			CGCourse course = cgCourseSection.getCourse();
			List<CGSectionLecture> lectureList2 = cgCourseSection.getLectureList();
			for (CGSectionLecture cgSectionLecture : lectureList2) {
				if(!cgSectionLecture.getCourse().equals(course)){
					System.out.println("CGCourseSection： ERROR--班级包含的lecture对应的科目 与此班的科目不匹配");
					success = false;
				}
				if(!cgSectionLecture.getCourseSection().equals(cgCourseSection)){
					System.out.println("CGCourseSection： ERROR--班级包含的lecture对应的教学班 与此班不匹配");
					success = false;
				}
				if(!lectureSet.contains(cgSectionLecture)){
					System.out.println("CGCourseSection： ERROR--班级"+cgCourseSection.getSectionCode()+"包含的lecture 不在总的lectureList之中");
					success = false;
				}
			}
		}
		
		// 2.4 CGSectionLecture
		for (CGSectionLecture cgLecture : lectureList) {
			CGCourse course = cgLecture.getCourse();
			CGCourseSection courseSection = cgLecture.getCourseSection();
			List<CGSectionLecture> sectionLectureList = cgLecture.getSectionLectureList();
			
			if(!courseSection.getCourse().equals(course)){
				System.out.println("CGSectionLecture： ERROR--lecture 的课程和 指定的班级 对应的课程 不同 ");
				success = false;
			}
			if(!cgLecture.getPeriodList().contains(cgLecture.getPeriod())){
				System.out.println("CGSectionLecture： ERROR--lecture 的 时间 不在指定 范围内 (periodList)");
				success = false;
			}
			
			for (CGSectionLecture cgSectionLecture : sectionLectureList) {
				if(!cgSectionLecture.getCourseSection().equals(courseSection)){
					System.out.println("CGSectionLecture： ERROR--本次课所属的所有教学课（sectionLectureList） 不包含当前班级(courseSection)");
					success = false;
				}
			}
			
			//验证 这里的对象 和外层对象 是否是引用关系
			if(!periodSet.containsAll(cgLecture.getPeriodList())){
				System.out.println("CGSectionLecture： ERROR--本次课所属的所有时间点 不在总的时间点之内");
				success = false;
			}
			
			if(!courseSet.contains(cgLecture.getCourse())){
				System.out.println("CGSectionLecture： ERROR--本次课所属的课程 不在总课程之内");
				success = false;
			}
			if(!sectionSet.contains(cgLecture.getCourseSection())){
				System.out.println("CGSectionLecture： ERROR--本次课所属的班级  不在总的班级之内");
				success = false;
			}
			if(!lectureSet.containsAll(sectionLectureList)){
				System.out.println("CGSectionLecture： ERROR--本次课所属的所有教学课 不在总的lectureList 之内");
				success = false;
			}
		}
		
		if(success){
			if(ScheduleUtils.isDevMod()) {
				System.out.println("\n验证通过！");
			}
		}else{
				System.out.println("\n验证失败！");
		}
		
		return success;
	}
}
