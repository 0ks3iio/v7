package net.zdsoft.newgkelective.data.optaplanner.util;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGCourseSection;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGRoom;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGSectionLecture;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGStudent;
import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGStudentCourseSchedule;
import net.zdsoft.newgkelective.data.optaplanner.dto.CGInputData;

public class ScheduleUtils {
	
	private static boolean isDevMod = false;
	
	public static boolean isDevMod() {
		if(Evn.isDevModel()) {
			isDevMod = true;
		}
		return isDevMod;
	}
	
	
	/**
	 * 生成返回结果 对象模型
	 * @param bestSchedulingSolution
	 * @param fileName
	 * @return
	 */
	public static CGInputData generateReturnResult(CGStudentCourseSchedule bestSchedulingSolution) {

		List<CGSectionLecture> lectureList = bestSchedulingSolution.getLectureList();
		
		CGInputData cgInputData = new CGInputData();
		cgInputData.setLectureList(lectureList);
		cgInputData.setArrayId(bestSchedulingSolution.getArrayId());
		
		return cgInputData;
	}
	
	/**
	 * 验证排课结果
	 * @param bestSchedulingSolution
	 * @return
	 */
	public static boolean validateResultNew(CGStudentCourseSchedule bestSchedulingSolution, boolean isDev) {
		List<CGSectionLecture> lectureList = bestSchedulingSolution.getLectureList();
		
		int conflictCount = 0;
		int roomConflict = 0;
		int teacherConflict = 0;
		int changeNum = 0;
		// 检验是否还有冲突 ：具有相同学生的班级 他们的时间点是否 重复
		for (int i=0;i<lectureList.size();i++) {
			CGSectionLecture ll = lectureList.get(i);
			for (int j=i+1;j<lectureList.size();j++) {
				CGSectionLecture rl = lectureList.get(j);
				Set<CGStudent> conflict = checkSectionsConflict(ll.getCourseSection(), rl.getCourseSection());
				boolean samePeriod = ll.getPeriod().equals(rl.getPeriod());
				int weekType = ll.getIsBiWeekly() +  rl.getIsBiWeekly();
				
				if(conflict.size()>0 && samePeriod && weekType != 0)
					conflictCount++;
				
				boolean combineFlag = (ll.getCourseSection().getCombineFlag() == null || ll.getCourseSection().getCombineFlag()!=0) 
						&& (rl.getCourseSection().getCombineFlag() == null || rl.getCourseSection().getCombineFlag()!=0);
				boolean sameRoom = ll.getRoom().equals(rl.getRoom());
				boolean needRoom = !CGRoom.NO_ROOM_CODE.equals(ll.getRoom().getCode());
				if(combineFlag && needRoom && sameRoom && samePeriod && weekType !=0)
					roomConflict++;
				
				if(combineFlag && ll.getTeacherCode() != null) {
					boolean sameTeacher = ll.getTeacherCode().equals(rl.getTeacherCode());
					if(sameTeacher && samePeriod && weekType != 0)
						teacherConflict++;
				}
			}
			
			if(ll.getPeriodBak() != null && ll.getPeriod().compareTo(ll.getPeriodBak())!=0) {
				changeNum++;
			}
		}
		
		if(isDev) {
			System.out.println("时间冲突数："+conflictCount);
			System.out.println("教室冲突数："+roomConflict);
			System.out.println("教师冲突数："+teacherConflict);
			System.out.println("时间点改变："+ changeNum);
			System.out.println("时间点改变："+ 1.0*changeNum/lectureList.size()*100+"%");
		}
		
		conflictCount += roomConflict;
		return conflictCount == 0?true:false;
	}

	private static Set<CGStudent> checkSectionsConflict(CGCourseSection leftSection, CGCourseSection rightSection) {
		Set<CGStudent> studentSet1 = leftSection.getStudentList().stream().collect(Collectors.toSet());
		Set<CGStudent> studentSet2 = rightSection.getStudentList().stream().collect(Collectors.toSet());
		
		return Sets.intersection(studentSet1, studentSet2);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getRandomOne(Collection<T> list) {
		int range = list.size();
		T one = (T)list.toArray()[(int)(Math.random() * range)];
		return one;
	}

}
