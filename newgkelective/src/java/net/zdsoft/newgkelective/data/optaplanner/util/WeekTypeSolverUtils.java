package net.zdsoft.newgkelective.data.optaplanner.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;

import net.zdsoft.newgkelective.data.optaplanner.domain.biweekly.Constants;
import net.zdsoft.newgkelective.data.optaplanner.domain.biweekly.Section;
import net.zdsoft.newgkelective.data.optaplanner.domain.biweekly.WeeklySolution;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourse;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGSectionLecture;

public class WeekTypeSolverUtils {
	private static SolverFactory<WeeklySolution> solverFactory = SolverFactory
			.createFromXmlResource("businessconf/solverv3/biweekly/weeklySolverConfig.xml");

	public static void main(String[] args) {
//		CGStudentCourseSchedule schedule = NKTest2.getScheduleFromRowDataXML("山东邹平一中 体育10禁.xml");
//		List<CGCourse> courseList = schedule.getCourseList();
//		List<CGSectionLecture> lectureList = schedule.getLectureList();
//		
//		resetWeekType(courseList, lectureList);
	}

	public static void resetWeekType(List<CGCourse> courseList,List<CGSectionLecture> lectureList) {
		boolean log = false;
		
		WeeklySolution solution = toWeeklySolution(courseList, lectureList);
		
		Solver<WeeklySolution> solver = solverFactory.buildSolver();
		if(log) {
			solver.addEventListener(new SolverEventListener<WeeklySolution>() {
				public void bestSolutionChanged(BestSolutionChangedEvent<WeeklySolution> event) {
					System.out.println("hard[0]: " + event.getNewBestSolution().getScore().getHardScore(0) + 
							"  soft[0]: " + event.getNewBestSolution().getScore().getSoftScore(0));
				}
			});
		}
		
		long st = System.currentTimeMillis();
		
		WeeklySolution bestSolution = solver.solve(solution);
		
		long end = System.currentTimeMillis();
		
		if (log) {
			System.out.println("current solver task finish,spend time: " + (end - st) / 1000 + "s");
			bestSolution.printResult();
		}
		
		// 将结果导入 原ScheduleSolution
		inportToScheduleSolution(bestSolution,lectureList);
	}

	private static void inportToScheduleSolution(WeeklySolution bestSolution, List<CGSectionLecture> lectureList) {
		List<Section> sectionList = bestSolution.getSectionList();
		Map<Integer, CGSectionLecture> lectureMap = lectureList.stream().collect(Collectors.toMap(CGSectionLecture::getId, Function.identity()));
		
		for (Section section : sectionList) {
			CGSectionLecture lecture = lectureMap.get(section.getId());
			
			if(lecture == null)
				continue;
			lecture.setIsBiWeekly(section.getIsBiweekly());
		}
	}

	private static WeeklySolution toWeeklySolution(List<CGCourse> courseList,List<CGSectionLecture> lectureList) {
		lectureList = lectureList.stream().filter(e->e.getIsBiWeekly() != CGCourse.WEEK_TYPE_NORMAL).collect(Collectors.toList());
		List<Section> sectionList = new ArrayList<>();
		Section section = null;
		int id = 0;
		for (CGSectionLecture le : lectureList) {
			section = new Section();
			
			section.setId(le.getId());
			section.setIsBiweekly(le.getIsBiWeekly());
			section.setOldId(le.getCourseSection().getOldId());
			section.setTeacherCode(le.getTeacherCode());
			if(le.getCourseSection().getIsBiweekly() != CGCourse.WEEK_TYPE_NORMAL){
				section.setIsBiweekly(le.getCourseSection().getIsBiweekly());
				section.setPinned(true);
			}
			
			sectionList.add(section);
		}
		
		Map<Integer, Integer> coupleWeekIdMap = makeCoupleInf(courseList, lectureList);
		
		WeeklySolution solution = new WeeklySolution();
		solution.setSectionList(sectionList);
		solution.setWeekTypeRange(Arrays.asList(CGCourse.WEEK_TYPE_ODD,CGCourse.WEEK_TYPE_EVEN));
		Constants constants = new Constants();
		constants.setId(id);
		constants.setCoupleWeekIdMap(coupleWeekIdMap);
		solution.setConstants(constants);
		
		return solution;
	}

	private static Map<Integer, Integer> makeCoupleInf(List<CGCourse> courseList, List<CGSectionLecture> lectureList) {
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
		
		Map<String, List<CGSectionLecture>> biLectureMap = lectureList.stream()
				.collect(Collectors.groupingBy(e->e.getCourseSection().getOldId()));
		Map<Integer,Integer> coupleWeekIdMap = new HashMap<>();
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
					l.setIsBiWeekly(CGCourse.WEEK_TYPE_EVEN);
					cgSectionLecture.setIsBiWeekly(CGCourse.WEEK_TYPE_ODD);
					coupleWeekIdMap.put(l.getId(), cgSectionLecture.getId());
				}
			}
		}
		return coupleWeekIdMap;
	}
}
