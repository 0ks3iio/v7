package net.zdsoft.newgkelective.data.optaplanner.remote.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.zdsoft.newgkelective.data.optaplanner.constants.NKSelectionConstants;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourse;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourseSection;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGDay;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGPeriod;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGRoom;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGSectionLecture;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGStudentCourseSchedule;
import net.zdsoft.newgkelective.data.optaplanner.remote.service.ICGLectureSolverRemoteService;
import net.zdsoft.newgkelective.data.optaplanner.util.FixSchedulingReference;
import net.zdsoft.newgkelective.data.optaplanner.util.NKArrangeReentrantLock;
import net.zdsoft.newgkelective.data.optaplanner.util.NKRedisUtils;
import net.zdsoft.newgkelective.data.optaplanner.util.ScheduleUtils;
import net.zdsoft.newgkelective.data.optaplanner.util.SchedulingSolutionValidator;

@Service("iCGLectureSolverRemoteService")
public class CGLectureSolverRemoteServiceImpl implements ICGLectureSolverRemoteService{
	private SolverFactory<CGStudentCourseSchedule> schedulingSolverFactoryPart = SolverFactory.createFromXmlResource("businessconf/solverv3/scheduling/studentCourseSchedulingSolverConfig_part.xml");
	private SolverFactory<CGStudentCourseSchedule> schedulingSolverFactoryFull = SolverFactory.createFromXmlResource("businessconf/solverv3/scheduling/studentCourseSchedulingSolverConfig_full.xml");
	private SolverFactory<CGStudentCourseSchedule> schedulingSolverFactoryChangeLess = SolverFactory.createFromXmlResource("businessconf/solverv3/scheduling/studentCourseSchedulingSolverConfig_changeLess.xml");
	
	private static final Logger log = LoggerFactory.getLogger(CGLectureSolverRemoteServiceImpl.class);
	
	public void stopRemoteSolver(String currentSolverId) {}

	
	public CGStudentCourseSchedule startIterate(
			CGStudentCourseSchedule schedule, String currentSolverId2, Long time) throws Exception {
		List<Solver<?>> solverList = new ArrayList<Solver<?>>();
		
		
		CGStudentCourseSchedule bestSolution = null;
		Solver<CGStudentCourseSchedule> schedulingSolverPart = null;
		Solver<CGStudentCourseSchedule> schedulingSolverFull = null;
		Solver<CGStudentCourseSchedule> schedulingSolverLess = null;
		if(schedule.isChangeLess()) {
			schedulingSolverLess = schedulingSolverFactoryChangeLess.buildSolver();
			solverList.add(schedulingSolverLess);
		}else {
			schedulingSolverPart = schedulingSolverFactoryPart.buildSolver();
			schedulingSolverFull = schedulingSolverFactoryFull.buildSolver();
			solverList.add(schedulingSolverPart);
			solverList.add(schedulingSolverFull);
		}
		
		//一个监控线程，判断该服务是否被发起停止，如果有停止请求，则关闭slover
		log.info("排课监控线程 即将启动：");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					log.info("	排课监控线程启动");
					while (true) {
						
						String stopCurrSolverId = NKRedisUtils
								.get(NKSelectionConstants.STOP_SOLVER_KEY + currentSolverId2);
						if (StringUtils.isNotBlank(stopCurrSolverId) || !NKArrangeReentrantLock.getLock().isKeyLocked(currentSolverId2)) {
							if(StringUtils.isNotBlank(stopCurrSolverId)) {
								log.warn("	收到断开排课信号:"+stopCurrSolverId);
								NKRedisUtils.del(NKSelectionConstants.STOP_SOLVER_KEY + currentSolverId2);
							}else {
								log.warn("	此次排课已经结束，停止监控:"+currentSolverId2);
							}
							// 已经 发起断开排课操作
							if (CollectionUtils.isNotEmpty(solverList)) {
								for (Solver<?> solver : solverList) {
									if (!solver.isTerminateEarly()) {
										solver.terminateEarly();
									}
								}
								log.warn("	排课停止成功："+currentSolverId2);
							}else {
								log.error("	solvers 为空："+currentSolverId2);
							}
							if (NKArrangeReentrantLock.getLock().isKeyLocked(currentSolverId2)) {
								NKArrangeReentrantLock.getLock().unLock(currentSolverId2);
							}
							return;
						}
						NKArrangeReentrantLock.getLock().resetTTL(currentSolverId2);
						log.info("	reset 监控线程 TTL"+currentSolverId2);
						Thread.sleep(5000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		if(schedule.isChangeLess()) {
			bestSolution = computeChangeLess(currentSolverId2, schedule, 
					schedulingSolverLess,time);
		}else {
			bestSolution = compute3For7(currentSolverId2, schedule, 
					schedulingSolverPart,schedulingSolverFull,time);
		}
		
		return bestSolution;
	}
	
	private CGStudentCourseSchedule computeChangeLess(String currentSolverId2, CGStudentCourseSchedule schedule,
			Solver<CGStudentCourseSchedule> schedulingSolverLess, Long time) throws Exception {
		schedule.getLectureList().stream().peek(e -> e.setPeriodBak(e.getPeriod()))
				.filter(e -> !e.getCourseSection().isFixed()).forEach(e -> e.setMoveable(false));
		
		return iterateCalculate(currentSolverId2, schedule, schedulingSolverLess, time, 1);
	}


	/**
	 * 循环计算，将这一次的计算结果作为下一次的计算输入参数。直到找到最好的解决方案或者 循环次数用尽
	 * @param schedulingSolution 解决方案结果类
	 * @param schedulingSolver 处理引擎
	 * @param sectioningSolver 循环计算的次数
	 * @return
	 * @throws Exception 
	 */
	private CGStudentCourseSchedule iterateCalculate(String currentSolverId2, CGStudentCourseSchedule schedulingSolution, Solver<CGStudentCourseSchedule> schedulingSolver,
			Long time, int number) throws Exception {
		if (time != null) {
			printScoreChange(schedulingSolver);
		}
		
		//if stop
		
		
		CGStudentCourseSchedule bestSchedulingSolution;
		
		
		schedulingSolution.calculateCourseConflictList();
		
		boolean validate = new SchedulingSolutionValidator().validate(schedulingSolution);
		if(!validate){
			throw new Exception("校验失败！");
		}
		//从缓存中获取此id是否被停止
		if (!NKArrangeReentrantLock.getLock().isKeyLocked(currentSolverId2)) {
			// 中途取消
			if(time != null) {
				System.out.println("中途取消");
			}else {
				log.warn("排课被中断："+currentSolverId2);
			}
			return null;
		}
		
		bestSchedulingSolution = schedulingSolver.solve(schedulingSolution);
		FixSchedulingReference.fix(bestSchedulingSolution);
		
		
		//从缓存中获取此id是否被停止
		if (!NKArrangeReentrantLock.getLock().isKeyLocked(currentSolverId2)) {
			// 中途取消
			if(time != null) {
				System.out.println("中途取消");
			}else {
				log.warn("排课被中断："+currentSolverId2);
			}
			return null;
		}
		
		number--;
		if(number == 0){
			return bestSchedulingSolution;
		}
		
		return iterateCalculate(currentSolverId2, schedulingSolution, schedulingSolver, time, number);
	}
	
	private CGStudentCourseSchedule compute3For7(String currentSolverId, CGStudentCourseSchedule schedulingSolution,
			Solver<CGStudentCourseSchedule> partSolver, Solver<CGStudentCourseSchedule> fullSolver, Long time) throws Exception {
		
		List<CGCourseSection> sectionList = schedulingSolution.getCourseSectionList();
		List<CGSectionLecture> lectureList = schedulingSolution.getLectureList();
		List<CGPeriod> periodList = schedulingSolution.getPeriodList();
		
		// 备份domain
		Map<Integer,List<String>> lectureDomainMap = new HashMap<>();
		for (CGSectionLecture le : lectureList) {
			List<String> codes = le.getPeriodList().stream().map(e->e.getPeriodCode()).collect(Collectors.toList());
			lectureDomainMap.put(le.getId(), codes);
		}
		
		Map<CGCourseSection,List<CGSectionLecture>> sectionLectureMap = lectureList.stream()
				.collect(Collectors.groupingBy(CGSectionLecture::getCourseSection));
		Map<String,List<CGSectionLecture>> oldIdLectureMap = lectureList.stream()
				.collect(Collectors.groupingBy(l->l.getCourseSection().getOldId()));
		
		List<CGCourseSection> xzbSectionListT = sectionList.stream().filter(e -> e.isFixed())
				.sorted((x, y) -> x.getClassName().compareTo(y.getClassName())).collect(Collectors.toList());
		List<String> xzbOldIdList = xzbSectionListT.stream()
				.map(e -> e.getOldId()).distinct()
				.sorted((x,y)->(oldIdLectureMap.get(x).size() - oldIdLectureMap.get(y).size()))
				.collect(Collectors.toList());
		Map<String, List<CGCourseSection>> classIdSectionsMap = xzbSectionListT.stream().collect(Collectors.groupingBy(CGCourseSection::getOldId));
		
		Set<CGCourseSection> batchSectionList = new HashSet<>(sectionList); 
		batchSectionList.removeAll(xzbSectionListT);
		sectionList.clear();
		sectionList.addAll(batchSectionList);
		
		lectureList.clear();
		batchSectionList.forEach(e -> lectureList.addAll(sectionLectureMap.get(e)));
		Map<CGPeriod, List<CGSectionLecture>> periodLectureMap = lectureList.stream().collect(Collectors.groupingBy(CGSectionLecture::getPeriod));
		periodList.forEach(e->{
			List<CGSectionLecture> lectureList2 = e.getLectureList();
			lectureList2.clear();
			if(CollectionUtils.isEmpty(periodLectureMap.get(e))) {
				return;
			}
			lectureList2.addAll(periodLectureMap.get(e));
		});
		
//		CGStudentCourseSchedule bestSolution = partSolver.startIterate(schedule, currentSolverId, currentTimeMillis);
		CGStudentCourseSchedule bestSolution = schedulingSolution;
		
		Iterator<String> iterator = xzbOldIdList.iterator();
		int splitSize = (int) Math.ceil(1.0*classIdSectionsMap.size()/6);
		if(splitSize > 6)
			splitSize = 6;
//		splitSize = 4;
		long st = System.currentTimeMillis();
		int count = 1;
		while (iterator.hasNext()) {
			if (bestSolution == null) {
				return bestSolution;
			}
			addOneMoreXzb(sectionLectureMap, classIdSectionsMap, bestSolution, iterator, splitSize);
			
			bestSolution = iterateCalculate(currentSolverId, bestSolution, partSolver, time, 1);
			if(time != null) {
				System.out.println("第"+(count++)+"次小排课结束,小排共耗时:"+ (System.currentTimeMillis()-st)/1000 +"s");
			}
//			boolean isSuccess = ScheduleUtils.validateResultNew(bestSolution,time != null);
		}
		
		// --------------------------------------------
		if (bestSolution == null || bestSolution.getScore() == null) {
			return bestSolution;
		}
		
		// 如果hard冲突已经解决，不再继续排课
		BendableScore score = bestSolution.getScore();
		if(score != null) {
			boolean hc = Arrays.stream(score.getHardScores()).allMatch(e->e >= 0);
			int[] softScores = score.getSoftScores();
			if(hc && softScores[0]>=0 && softScores[1]>-100) {
				return bestSolution;
			}
		}
		
		fixSolutionEntity(bestSolution);
		// 最后进行一次  /恢复lecture domain
		Map<String, CGPeriod> periodMap = bestSolution.getPeriodList().stream().collect(Collectors.toMap(e->e.getPeriodCode(), e->e));
		bestSolution.getLectureList().stream().filter(e->e.getCourseSection().isFixed()).forEach(e->{
			e.setMoveable(true);
			List<String> list = lectureDomainMap.get(e.getId());
			List<CGPeriod> domain = list.stream().filter(l->periodMap.containsKey(l)).map(l->periodMap.get(l)).collect(Collectors.toList());
			if(domain.size()!= list.size())
				throw new RuntimeException("恢复domain失败 domain 大小不一致");
			e.setPeriodList(domain);
		});
		log.warn("最后一次排课开始:"+currentSolverId);
		bestSolution = iterateCalculate(currentSolverId, bestSolution, fullSolver, time, 1);
		
		return bestSolution;
	}

	/**
	 * 将上一次排课结果固定 并且添加下一（几）个行政班
	 * @param sectionLectureMap
	 * @param classIdSectionsMap
	 * @param bestSolution
	 * @param iterator
	 * @param splitSize
	 * @return 
	 */
	private String addOneMoreXzb(Map<CGCourseSection, List<CGSectionLecture>> sectionLectureMap,
			Map<String, List<CGCourseSection>> classIdSectionsMap, CGStudentCourseSchedule bestSolution,
			Iterator<String> iterator, int splitSize) {
		List<CGSectionLecture> newLectures;
		// 将结果固定
		bestSolution.getLectureList().forEach(e -> {
			e.setMoveable(false);
		});

		// 一组行政班
		String oldId = iterator.next();
		List<CGCourseSection> list = new ArrayList<>(classIdSectionsMap.get(oldId));
		int i = 1;
		while (iterator.hasNext() && i < splitSize) {
			list.addAll(classIdSectionsMap.get(iterator.next()));
			i++;
		}
		if(ScheduleUtils.isDevMod()) {
			// 打印 这一轮参与排课的行政班信息
			list.stream().map(e->e.getClassName()).distinct().forEach(e->System.out.print(e+" "));
			System.out.println();
		}
		// 将行政班 课程  设置为可以活动
		newLectures = list.stream().flatMap(e -> sectionLectureMap.get(e).stream()).collect(Collectors.toList());
		newLectures.stream().filter(e->e.getCourse().isFixedSubject()).forEach(e->e.setMoveable(false));
		// 添加 section
		bestSolution.getCourseSectionList().addAll(list);

		// 添加 lecture
		bestSolution.getLectureList().addAll(newLectures);

		// 更新引用
		fixSolutionEntity(bestSolution);
		
		// 将教师已安排课程时间 作为下一轮排课的禁排时间
		Map<String, List<CGSectionLecture>> teacherLectureMap = bestSolution.getLectureList().stream()
				.filter(e -> !newLectures.contains(e) && e.getCourseSection().getTeacher()!=null && e.getIsBiWeekly() == CGCourse.WEEK_TYPE_NORMAL)
				.collect(Collectors.groupingBy(e -> e.getTeacherCode()));
		for (CGSectionLecture le : newLectures) {
			if(le.getTeacherCode()!=null && teacherLectureMap.containsKey(le.getTeacherCode())) {
				Set<CGPeriod> periods = teacherLectureMap.get(le.getTeacherCode()).stream().map(e->e.getPeriod()).collect(Collectors.toSet());
				List<CGPeriod> collect = le.getPeriodList().stream().filter(e->!periods.contains(e)).collect(Collectors.toList());
				if(CollectionUtils.isNotEmpty(collect)) {
					le.setPeriodList(collect);
					le.getPeriod().getLectureList().remove(le);
					
					CGPeriod randomOne = getRandomOne(le.getPeriodList());
					randomOne.getLectureList().add(le);
					le.setPeriod(randomOne);
				}
			}
		}
		
		return oldId;
	}
	
	private static <T> T getRandomOne(List<T> list) {
		int range = list.size();
		T one = list.get((int)(Math.random() * range));
		return one;
	}
	
	private void fixSolutionEntity(CGStudentCourseSchedule bestSolution) {
		List<CGPeriod> periodList = bestSolution.getPeriodList();
		List<CGSectionLecture> lectureList = bestSolution.getLectureList();
		List<CGCourseSection> courseSectionList = bestSolution.getCourseSectionList();
		List<CGRoom> roomList = bestSolution.getRoomList();
		
		Map<String, List<CGSectionLecture>> periodLectureMap2 = lectureList.stream()
				.collect(Collectors.groupingBy(e->e.getPeriod().getPeriodCode()));
		// 重新 更新period引用的lectures
		for (CGPeriod period : periodList) {
			List<CGSectionLecture> lectureList2 = period.getLectureList();
			lectureList2.clear();
			if (CollectionUtils.isEmpty(periodLectureMap2.get(period.getPeriodCode()))) {
				continue;
			}
			lectureList2.addAll(periodLectureMap2.get(period.getPeriodCode()));
		}
		
		
		Set<CGDay> daySet = periodList.stream().map(e->e.getDay()).collect(Collectors.toSet());
		
		Map<String, CGPeriod> periodMap = new HashMap<>();
		for (CGPeriod period : periodList) {
			periodMap.put(period.getPeriodCode(), period);
		}
		Map<String, CGSectionLecture> lectureMap = new HashMap<>();
		for (CGSectionLecture lecture : lectureList) {
			lectureMap.put(lecture.getId()+"", lecture);
		}
		Map<String, CGCourseSection> sectionMap = new HashMap<>();
		for (CGCourseSection section : courseSectionList) {
			sectionMap.put(section.getId()+"", section);
		}
		Map<String, CGRoom> roomMap = new HashMap<>();
		for (CGRoom room : roomList) {
			roomMap.put(room.getCode(), room);
		}
		
		// fix days
		for (CGDay cgDay : daySet) {
			List<CGPeriod> periodList2 = cgDay.getPeriodList().stream().map(e->periodMap.get(e.getPeriodCode())).collect(Collectors.toList());
			cgDay.setPeriodList(periodList2);
		}
		
		// fix section
		for (CGCourseSection section : courseSectionList) {
			List<CGSectionLecture> lectureList2 = section.getLectureList().stream()
					.map(e->lectureMap.get(e.getId()+""))
					.collect(Collectors.toList());
			section.setLectureList(lectureList2);
		}
		// fix lecture
		for (CGSectionLecture lecture : lectureList) {
			CGCourseSection cgCourseSection = sectionMap.get(lecture.getCourseSection().getId()+"");
			lecture.setCourseSection(cgCourseSection);
			
			List<CGSectionLecture> lectures = lecture.getSectionLectureList().stream().map(e->lectureMap.get(e.getId()+"")).collect(Collectors.toList());
			lecture.setSectionLectureList(lectures);
			
			List<CGPeriod> periodListT = lecture.getPeriodList().stream().map(e->periodMap.get(e.getPeriodCode())).collect(Collectors.toList());
			lecture.setPeriodList(periodListT);
			
			CGPeriod cgPeriod = periodMap.get(lecture.getPeriod().getPeriodCode());
			lecture.setPeriod(cgPeriod);
			
			CGRoom cgRoom = roomMap.get(lecture.getRoom().getCode());
			lecture.setRoom(cgRoom);
		}
		
	}
	private void printScoreChange(
			Solver<CGStudentCourseSchedule> schedulingSolver) {
		schedulingSolver.addEventListener(new SolverEventListener<CGStudentCourseSchedule>() {

			@Override
			public void bestSolutionChanged(
					BestSolutionChangedEvent<CGStudentCourseSchedule> event) {
				System.out.println("hard[0]: " + event.getNewBestSolution().getScore().getHardScore(0) + 
   			         "  hard[1]: " + event.getNewBestSolution().getScore().getHardScore(1) +
   			         "  hard[2]: " + event.getNewBestSolution().getScore().getHardScore(2) +
   			         "  hard[3]: " + event.getNewBestSolution().getScore().getHardScore(3) +
   			         "  soft[0]: " + event.getNewBestSolution().getScore().getSoftScore(0) +
   			         "  soft[1]: " + event.getNewBestSolution().getScore().getSoftScore(1) +
   			         "  soft[2]: " + event.getNewBestSolution().getScore().getSoftScore(2));
				
//				CGInputData cgInputData = ScheduleUtils.generateReturnResult(event.getNewBestSolution());
//				XmlUtils.toXMLFile(cgInputData, "C:\\Users\\user\\Desktop\\",
//						"排课结果1 "+new Date().toLocaleString().replaceAll(":", "-")+".xml");
//				System.out.println("--------- 输出完毕 -------");
			}
		});
		
	}
}
