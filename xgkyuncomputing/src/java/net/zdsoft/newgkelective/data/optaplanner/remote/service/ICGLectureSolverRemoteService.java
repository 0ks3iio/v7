package net.zdsoft.newgkelective.data.optaplanner.remote.service;

import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGStudentCourseSchedule;

public interface ICGLectureSolverRemoteService {

	
//	public void init();
	
	public void stopRemoteSolver(String currentSolverId) ;

//	public boolean solverExistsBySolveId(
//			String stopCurrSolverId) ;

	public CGStudentCourseSchedule startIterate(
			CGStudentCourseSchedule schedule, String currentSolverId2, Long time) throws Exception;
	
}
