package net.zdsoft.newgkelective.data.optaplanner.constants;

import java.util.concurrent.TimeUnit;

public class NKSelectionConstants {

	public static final String TYPE_A = "A";
	public static final String TYPE_B = "B";
	
	// 限制时间
	public static final long SPENT_LIMIT_MIN = 60;
	public static final long REDIS_LIMIT_HOUR = 3;
	public static final long REDIS_LIMIT_MINUTE = 5;
	public static final TimeUnit UNIT_HOUR = TimeUnit.HOURS;
	public static final TimeUnit UNIT_MINUTE = TimeUnit.MINUTES;
	
	public static final String STOP_SOLVER_KEY = "gkelective.stopSolver.";
	
	/**
	 * 按照总分成绩分层
	 */
	public static final String DIVIDE_BY_ALL_SCORE = "2";
	
	public static final String[] SUBJECT_TYPES_YSY =new String[]{
			"3007",//语文
			"3008",//数学
			"3009"//英语
	};
}
