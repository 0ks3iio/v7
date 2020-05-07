package net.zdsoft.newgkelective.data.optaplanner.solver;

import java.util.*;
import java.util.stream.Collectors;

import net.zdsoft.newgkelective.data.optaplanner.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.newgkelective.data.optaplanner.constants.NKSelectionConstants;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGClass;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourse;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGPeriod;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGRoom;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGStudentCourseSchedule;
import net.zdsoft.newgkelective.data.optaplanner.dto.CGInputData;
import net.zdsoft.newgkelective.data.optaplanner.listener.CGSolverListener;
import net.zdsoft.newgkelective.data.optaplanner.remote.service.ICGLectureSolverRemoteService;

@Service
public class CGForLectureSolver {
	private static final Logger logger = LoggerFactory.getLogger(CGForLectureSolver.class);
	
	private List<CGSolverListener> listenerList = new ArrayList<CGSolverListener>();
	private Long currentTimeMillis;
	private String currentSolverId;
//	@Autowired(required=false)
//	private ICGLectureSolverRemoteService iCGLectureSolverRemoteService;
//	public CGForLectureSolver() {
//		if(iCGLectureSolverRemoteService == null)
//			iCGLectureSolverRemoteService = Evn.getBean("iCGLectureSolverRemoteService");
//	}

	
	/**
	 * 添加观察者
	 * 
	 * @param listener
	 */
	public void addListener(CGSolverListener listener) {
		this.listenerList.add(listener);
	}

	/**
	 * 取消(停止)排课
	 * 
	 * @param curSolId
	 */
	public static void stopSolver(String curSolId) {
		synchronized (CGForLectureSolver.class) {
			NKRedisUtils.set(NKSelectionConstants.STOP_SOLVER_KEY+curSolId, curSolId);
			NKArrangeReentrantLock.getLock().removeKey(curSolId);
		}
	}

	/**
	 * check是否在排
	 * 
	 * @param curSolId
	 * @return true 正在排
	 */
	public static boolean isSolverIdRunning(String curSolId) {
		return NKArrangeReentrantLock.getLock().isKeyLocked(curSolId);
	}

	/**
	 * 开课参数，对接需要添加观察者调用addListener方法
	 * 
	 * @param inputCf
	 *            参数配置
	 * @param subjectList
	 *            所有科目（新高考科目+行政班科目）
	 * @param courseList
	 *            所有课程（新高考课程+行政班课程）
	 * @param resultClass
	 *            教学班和行政班数据
	 * @param curSolId
	 *            这个值决定走不走线程，锁的id
	 * @param currTime
	 *            对接传null
	 * @return
	 */
	public void solve(CGInputData cGInputData, Long currTime) {
		
		currentTimeMillis = currTime;
		currentSolverId = cGInputData.getArrayId();
		
		try {
			if (StringUtils.isNotBlank(currentSolverId) && !NKArrangeReentrantLock.getLock().tryLock(currentSolverId)) {
				return;
			}
			
			
//			XmlUtils.toXMLFile(cGInputData, "C:\\Users\\user\\Desktop\\排课初始数据\\", cGInputData.getUnitName()
//						+new Date().toLocaleString().replaceAll(":", "-")+".xml");
//			int io = 1/0;
			
			businessProcess(cGInputData);
		} catch (Exception e) {
			e.printStackTrace();
			for (CGSolverListener listener : listenerList) {
				listener.onError(e);
			}
			
			if (StringUtils.isNotBlank(currentSolverId))
				NKArrangeReentrantLock.getLock().unLock(currentSolverId);
		}
	}
	
	private void businessProcess(CGInputData cGInputData) {
		long stTime = System.currentTimeMillis();
		
		CGStudentCourseSchedule schedule = DtoToData.transfer(cGInputData);
		
		Set<CGPeriod> periodList = schedule.getPeriodList().stream().filter(e->!e.isExtra()).collect(Collectors.toSet());
		List<CGRoom> roomList = schedule.getRoomList();
		
		double skdNum = schedule.getLectureList().stream().map(e->e.getIsBiWeekly()!=2?0.5:1).reduce((x,y)->x+y).orElse(0d);
		
		double allSkdNum = periodList.size() * roomList.size();
		if (allSkdNum < skdNum) {
			cGInputData.appendMsg("时空点（一周上课时间点数量*教室数量）数量不够，目前共有：" + allSkdNum + "，至少需要：" + skdNum+"。建议：增加上课时间点或者教室数量");
		}
		double bfb = 1.0*(allSkdNum-skdNum)/allSkdNum*100;
		bfb = (int)(bfb*100)/100.0;
		
		Double maxLectureCount = null;
		if(cGInputData.getClassList().stream().anyMatch(e->!e.isFixed())) {
			maxLectureCount = cGInputData.getStudentList().stream()
					.map(e->e.getLectureCount()).max(Double::compare).orElse(0d);
		}else {
			Map<String,Double> clsLecMap = new HashMap<>();
			for (CGClass cgClass : cGInputData.getClassList()) {
				Double aDouble = clsLecMap.get(cgClass.getOldId());
				if(aDouble == null){
					clsLecMap.put(cgClass.getOldId(),aDouble = 0d);
				}
				if(cgClass.getCourse().getIsBiWeekly() == CGCourse.WEEK_TYPE_NORMAL){
					clsLecMap.put(cgClass.getOldId(),aDouble + cgClass.getLectureCount());
				}else{
					clsLecMap.put(cgClass.getOldId(),aDouble + cgClass.getLectureCount() - 0.5);
				}
			}
			maxLectureCount = clsLecMap.values().stream().max(Double::compareTo).orElse(0d);
		}
		if(maxLectureCount <= 0.0) {
			cGInputData.appendMsg("学生没有课要上，请至少维护一门课目");
		}
		if (periodList.size() < maxLectureCount) {
			cGInputData.appendMsg("时间点数量不够，至少需要：" + maxLectureCount+"个时间点。建议：增加上课时间点");
		}
		if(cGInputData.hasMsg()) {
			throw new RuntimeException(cGInputData.getMsgString());
		}
		// 课程已经安排完毕
		boolean allMatch = schedule.getLectureList().stream().allMatch(e->e.getPeriodList().size()<=1);
		if(allMatch) {
			CGInputData cGInputData2 = ScheduleUtils.generateReturnResult(schedule);
			
			for (CGSolverListener listener : listenerList) {
				listener.solveFinished(cGInputData2, true);
			}
			if (StringUtils.isNotBlank(currentSolverId))
				NKArrangeReentrantLock.getLock().unLock(currentSolverId);
			logger.warn("课程已经安排完毕 无需自动排课:"+currentSolverId);
			return;
		}
		if(currentTimeMillis != null) {
			System.out.println("currentSolverId:" + currentSolverId);
			System.out.println("Start Time:" + new Date(stTime).toLocaleString());
			System.out.println("总时空点数量：" + allSkdNum + "，至少需要：" + skdNum);
			System.out.println("时空点空闲百分比：" + bfb + "%" );
			System.out.println("总时间点："+periodList.size()+"  至少需要:"+maxLectureCount);
			System.out.println("时间点空闲百分比："+(periodList.size()-maxLectureCount)*1.0/periodList.size()*100+"%");
		}
		
		if (StringUtils.isNotBlank(currentSolverId) && !NKArrangeReentrantLock.getLock().isKeyLocked(currentSolverId)) {
			return;
		}
		
		if (StringUtils.isNotBlank(currentSolverId)) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					startPlanner(schedule, cGInputData, stTime);
				}
			});
			thread.start();
		} else {
			throw new RuntimeException("排课发生异常，请联系管理员");
		}
	}

//	private void startPlanner(CGStudentCourseSchedule schedule, CGInputData cgInputData, long stTime) {
//		try {
//			CGStudentCourseSchedule bestSolution = iCGLectureSolverRemoteService.startIterate(schedule, currentSolverId, currentTimeMillis);
//
//
//			if (bestSolution == null) {
//				return;
//			}
//			long enTime = System.currentTimeMillis();
//			if (currentTimeMillis != null) {
//				System.out.println("总计运行时间：" + ((int) ((enTime - stTime) / 1000)) + "秒 ");
//			}
//
//			boolean isSuccess = ScheduleUtils.validateResultNew(bestSolution,currentTimeMillis != null);
//
//			cgInputData = ScheduleUtils.generateReturnResult(bestSolution);
////			XmlUtils.toXMLFile(cgInputData, "C:\\Users\\user\\Desktop\\xml排课结果\\",
////					"排课结果1 "+new Date().toLocaleString().replaceAll(":", "-")+".xml");
//
//			for (CGSolverListener listener : listenerList) {
//				listener.solveFinished(cgInputData, isSuccess);
//			}
//			if (currentTimeMillis != null) {
//				System.out.println("OK");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			for (CGSolverListener listener : listenerList) {
//				listener.onError(e);
//			}
//		} finally {
//			if (StringUtils.isNotBlank(currentSolverId))
//				NKArrangeReentrantLock.getLock().unLock(currentSolverId);
//		}
//	}

}
