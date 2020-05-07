package net.zdsoft.newgkelective.data.optaplanner.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourseSection;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGDay;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGPeriod;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGRoomConstraint;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGSectionLecture;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGStudentCourseSchedule;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGUnavailablePeriodPenaltyHard;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGUnavailablePeriodPenaltySoft;

public class FixSchedulingReference {
	private static Logger logger = LoggerFactory.getLogger(FixSchedulingReference.class);
	
	public static void fix(CGStudentCourseSchedule schedulingSolution){
		List<CGPeriod> periodList = schedulingSolution.getPeriodList();
		List<CGSectionLecture> lectureList = schedulingSolution.getLectureList();
		List<CGCourseSection> courseSectionList = schedulingSolution.getCourseSectionList();
		List<CGRoomConstraint> roomConstraintList = schedulingSolution.getRoomConstraintList();
		List<CGUnavailablePeriodPenaltyHard> hardUnavailableList = schedulingSolution.getHardUnavailableList();
		List<CGUnavailablePeriodPenaltySoft> softUnavailableList = schedulingSolution.getSoftUnavailableList();
		
		Map<Integer, CGSectionLecture> lectureMap = getMap(lectureList);
		Map<String, CGPeriod> periodMap =  getPeriodMap(periodList);
		
		//1 lecture
		for (CGPeriod cgPeriod : periodList) {
			List<CGSectionLecture> lectureList2 = cgPeriod.getLectureList();
			List<CGSectionLecture> collect = lectureList2.stream().filter(e->lectureMap.containsKey(e.getId()))
					.map(e->lectureMap.get(e.getId()))
					.collect(Collectors.toList());
			cgPeriod.setLectureList(collect);
			if(collect.size() != lectureList2.size()) {
				logger.error("error occur in lectures in period 1");
			}
		}
		for (CGCourseSection cgSection : courseSectionList) {
			List<CGSectionLecture> lectureList2 = cgSection.getLectureList();
			List<CGSectionLecture> collect = lectureList2.stream().filter(e->lectureMap.containsKey(e.getId()))
					.map(e->lectureMap.get(e.getId()))
					.collect(Collectors.toList());
			cgSection.setLectureList(collect);
			if(collect.size() != lectureList2.size()) {
				logger.error("error occur in lectures in section 2");
			}
		}
		for (CGRoomConstraint cgRoomConstraint : roomConstraintList) {
			CGSectionLecture newSectionLecture = lectureMap.get(cgRoomConstraint.getLecture().getId());
			if(newSectionLecture == null){
				logger.error("error occur in cgRoomConstraint 3 ;根据id"+cgRoomConstraint.getLecture().getId()+"未找到 lecure");
				continue;
			}
			cgRoomConstraint.setLecture(newSectionLecture);
		}
		fixcgHardPeriodPenalty(hardUnavailableList, lectureMap,periodMap);
		fixcgSoftPeriodPenalty(softUnavailableList, lectureMap,periodMap);
		
		//2 Day -> Period
		Set<CGDay> daySet = periodList.stream().map(p->p.getDay()).collect(Collectors.toSet());
		for (CGDay cgDay : daySet) {
			List<CGPeriod> periodList2 = cgDay.getPeriodList();
			
			List<CGPeriod> collect = periodList2.stream().filter(e->periodMap.containsKey(e.getPeriodCode()))
					.map(e->periodMap.get(e.getPeriodCode()))
					.collect(Collectors.toList());
			if(collect.size() != periodList2.size()) {
				logger.error("error occur in periods in day 8");
				continue;
			}
			cgDay.setPeriodList(collect);
		}
	}

	private static void fixcgHardPeriodPenalty(
			List<CGUnavailablePeriodPenaltyHard> hardUnavailableList,
			Map<Integer, CGSectionLecture> lectureMap, Map<String, CGPeriod> periodMap) {
		for (CGUnavailablePeriodPenaltyHard cgPeriodPenalty : hardUnavailableList) {
			CGSectionLecture newSectionLecture = lectureMap.get(cgPeriodPenalty.getSectionLecture().getId());
			if(newSectionLecture == null){
				logger.error("根据id"+cgPeriodPenalty.getSectionLecture().getId()+"找不到对应lecure");
				continue;
			}
			cgPeriodPenalty.setSectionLecture(newSectionLecture);
			cgPeriodPenalty.setPeriod(periodMap.get(cgPeriodPenalty.getPeriod().getPeriodCode()));
		}
	}
	private static void fixcgSoftPeriodPenalty(
			List<CGUnavailablePeriodPenaltySoft> hardUnavailableList,
			Map<Integer, CGSectionLecture> lectureMap, Map<String, CGPeriod> periodMap) {
		for (CGUnavailablePeriodPenaltySoft cgPeriodPenalty : hardUnavailableList) {
			CGSectionLecture newSectionLecture = lectureMap.get(cgPeriodPenalty.getSectionLecture().getId());
			if(newSectionLecture == null){
				logger.error("根据id"+cgPeriodPenalty.getSectionLecture().getId()+"找不到对应lecure 6");
				continue;
			}
			cgPeriodPenalty.setSectionLecture(newSectionLecture);
			cgPeriodPenalty.setPeriod(periodMap.get(cgPeriodPenalty.getPeriod().getPeriodCode()));
		}
	}
	
	private static Map<String, CGPeriod> getPeriodMap(List<CGPeriod> periodList) {
		Map<String, CGPeriod> map = new HashMap<>();
		for (CGPeriod cgPeriod : periodList) {
			map.put(cgPeriod.getPeriodCode(), cgPeriod);
		}
		return map;
	}

	private static Map<Integer,CGSectionLecture> getMap(List<CGSectionLecture> list){
		Map<Integer, CGSectionLecture> map = new HashMap<>();
		for (CGSectionLecture cgSectionLecture : list) {
			map.put(cgSectionLecture.getId(), cgSectionLecture);
		}
		return map;
	}
}
