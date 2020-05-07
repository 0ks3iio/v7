package net.zdsoft.syncdata.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.zdsoft.framework.utils.RedisUtils;

import org.apache.commons.lang3.StringUtils;

public class SyncTimeUtils {

	public static String getModifyTime(String key) {
		String time = RedisUtils.get(key);
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		return time;
	}
	
	public static SimpleDateFormat getDataFormat() {
		return new SimpleDateFormat("yyyyMMddHHmmss");
	}
	
	public static SimpleDateFormat getDataFormat(String dataFormat) {
		return new SimpleDateFormat(dataFormat);
	}
	
	public static String getEndModifyTime(String time, SimpleDateFormat sdf,
			Date modifyTime) {
		String gxsj = sdf.format(modifyTime);
		return getEndModifyTime(time,gxsj);
	}
	
	public static String getEndModifyTime(String time, String gxsj){
		if (StringUtils.isNotBlank(gxsj) && time.compareTo(gxsj) < 0) {
			time = gxsj;
		}
		return time;
	}
}
