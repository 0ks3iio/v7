package net.zdsoft.newgkelective.data.optaplanner.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourse;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourseSection;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGPeriod;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGSectionLecture;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGStudentCourseSchedule;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGTeacher;
import net.zdsoft.newgkelective.data.optaplanner.dto.CGInputData;

public class ToSolution {
	private CGStudentCourseSchedule schedule;
	public static void main(String[] args) {
		try {
			Class<Evn> forName = (Class<Evn>)Class.forName("net.zdsoft.framework.config.Evn");
			System.out.println(forName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public CGInputData make(CGStudentCourseSchedule schedule) {
		this.schedule = schedule;
		List<CGSectionLecture> lectureEntityList = schedule.getLectureList();
		List<CGCourse> courseList = schedule.getCourseList();
		List<CGCourseSection> courseSectionList = schedule.getCourseSectionList();
		List<CGPeriod> periodList = schedule.getPeriodList();
		
		
		Map<String, CGSectionLecture> binateLectures = makeBinateLectures(lectureEntityList, courseList, courseSectionList);
		
		//*******************
		
		
		
		
		//*******************
		ScheduleUtils.validateResultNew(schedule, Evn.isDevModel());
		return ScheduleUtils.generateReturnResult(schedule);
	}

	private Map<String, CGSectionLecture> makeBinateLectures(List<CGSectionLecture> lectureEntityList, List<CGCourse> courseList,
			List<CGCourseSection> courseSectionList) {
		Set<CGCourse> biCourses = courseList.stream().filter(e->e.getIsBiWeekly() != 2).collect(Collectors.toSet());
		Map<CGTeacher, List<CGCourseSection>> teacheClassMap = courseSectionList.stream()
				.filter(e->biCourses.contains(e.getCourse()))
				.collect(Collectors.groupingBy(e->e.getTeacher()));
		
		for (List<CGCourseSection> sectionList : teacheClassMap.values()) {
			int i=0;
			for (CGCourseSection cgCourseSection : sectionList) {
				if(i%2 == 0) {
					cgCourseSection.getLectureList().forEach(e->e.setIsBiWeekly(1));
				}else {
					cgCourseSection.getLectureList().forEach(e->e.setIsBiWeekly(-1));
				}
				i++;
			}
		}
		
		Map<CGCourse, List<CGSectionLecture>> biCourseLectureMap = lectureEntityList.stream()
				.filter(e->biCourses.contains(e.getCourse()))
				.collect(Collectors.groupingBy(e->e.getCourse()));
		
		Iterator<CGCourse> iterator = new ArrayList<>(biCourseLectureMap.keySet()).stream()
				.sorted((x,y)->-biCourseLectureMap.get(x).size()+biCourseLectureMap.get(y).size()).iterator();
		CGCourse next;
		CGCourse next2;
		List<CGSectionLecture> list;
		List<CGSectionLecture> list2;
		// key:放进排课算法里面作为合并课进行排课的 lecture  V:被合并 不参与排课的lecture
		Map<CGSectionLecture,CGSectionLecture> binateLectures = new HashMap<>();
		// key:被合并 不参与排课的lecture  lecture  V:放进排课算法里面作为合并课进行排课的
		Map<CGSectionLecture,CGSectionLecture> binateLectures2 = new HashMap<>();
		
		while(iterator.hasNext()) {
			next = iterator.next();
			list = biCourseLectureMap.get(next);
			if(iterator.hasNext()) {
				next2 = iterator.next();
				list2 = biCourseLectureMap.get(next2);
				
				for (int i=0;i<list.size();i++) {
					for (int j=0;j<list2.size();j++) {
					// 必须是同一个班级的 进行合成课 
						if(list.get(i).getCourseSection().getOldId().equals(list2.get(j).getCourseSection().getOldId())
								&& !binateLectures.containsKey(list.get(i))
								&& !binateLectures2.containsKey(list2.get(j))) {
							
							binateLectures.put(list.get(i), list2.get(j));
							binateLectures2.put(list2.get(j), list.get(i));
							
							System.out.println(i+" -- "+j);
							break;
						}
					}
				}
			}
		}
		Map<String, CGSectionLecture> binateStrLectures = new HashMap<>();
		for (CGSectionLecture bl : binateLectures.keySet()) {
			binateStrLectures.put(bl.getId()+"", binateLectures.get(bl));
		}
		return binateStrLectures;
	}
}
