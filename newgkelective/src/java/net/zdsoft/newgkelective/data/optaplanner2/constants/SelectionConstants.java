package net.zdsoft.newgkelective.data.optaplanner2.constants;

import java.util.concurrent.TimeUnit;

public class SelectionConstants {

	public static final String TYPE_A = "A";
	public static final String TYPE_B = "B";
	
	// 限制时间
	public static final long SPENT_LIMIT_MIN = 60;
	public static final long REDIS_LIMIT_HOUR = 3;
	public static final TimeUnit UNIT_HOUR = TimeUnit.HOURS;
	
}
