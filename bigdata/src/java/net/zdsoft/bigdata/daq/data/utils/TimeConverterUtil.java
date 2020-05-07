package net.zdsoft.bigdata.daq.data.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeConverterUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(TimeConverterUtil.class);

	/**
	 * 函数功能描述:UTC时间转本地时间格式
	 * 
	 * @param utcTime
	 *            UTC时间
	 * @param utcTimePatten
	 *            UTC时间格式
	 * @param localTimePatten
	 *            本地时间格式
	 * @return 本地时间格式的时间 eg:utc2Local("2017-06-14 09:37:50.788+08:00",
	 *         "yyyy-MM-dd HH:mm:ss.SSSXXX", "yyyy-MM-dd HH:mm:ss.SSS")
	 */
	public static String utc2Local(String utcTime, String utcTimePatten,
			String localTimePatten) {
		SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));// 时区定义并进行时间获取
		Date gpsUTCDate = null;
		try {
			gpsUTCDate = utcFormater.parse(utcTime);
		} catch (ParseException e) {
			e.printStackTrace();
			return utcTime;
		}
		SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
		localFormater.setTimeZone(TimeZone.getDefault());
		String localTime = localFormater.format(gpsUTCDate.getTime());
		return localTime;
	}

	/**
	 * 函数功能描述:UTC时间转本地时间格式
	 * 
	 * @param utcTime
	 *            UTC时间
	 * @param localTimePattern
	 *            本地时间格式(要转换的本地时间格式)
	 * @return 本地时间格式的时间
	 */
	public static String utc2Local(String utcTime, String localTimePattern) {
		String utcTimePattern = "yyyy-MM-dd";
		String subTime = utcTime.substring(10);// UTC时间格式以 yyyy-MM-dd
												// 开头,将utc时间的前10位截取掉,之后是含有多时区时间格式信息的数据

		// 处理当后缀为:+8:00时,转换为:+08:00 或 -8:00转换为-08:00
		if (subTime.indexOf("+") != -1) {
			subTime = changeUtcSuffix(subTime, "+");
		}
		if (subTime.indexOf("-") != -1) {
			subTime = changeUtcSuffix(subTime, "-");
		}
		utcTime = utcTime.substring(0, 10) + subTime;

		// 依据传入函数的utc时间,得到对应的utc时间格式
		// 步骤一:处理 T
		if (utcTime.indexOf("T") != -1) {
			utcTimePattern = utcTimePattern + "'T'";
		}

		// 步骤二:处理毫秒SSS
		if (utcTime.indexOf(".") != -1) {
			utcTimePattern = utcTimePattern + " HH:mm:ss.SSS";
		} else {
			utcTimePattern = utcTimePattern + " HH:mm:ss";
		}

		// 步骤三:处理时区问题
		if (subTime.indexOf("+") != -1 || subTime.indexOf("-") != -1) {
			utcTimePattern = utcTimePattern + "XXX";
		} else if (subTime.indexOf("Z") != -1) {
			utcTimePattern = utcTimePattern + "'Z'";
		}

		if ("yyyy-MM-dd HH:mm:ss".equals(utcTimePattern)
				|| "yyyy-MM-dd HH:mm:ss.SSS".equals(utcTimePattern)) {
			return utcTime;
		}

		SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePattern);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date gpsUtcDate = null;
		try {
			gpsUtcDate = utcFormater.parse(utcTime);
		} catch (Exception e) {
			logger.error("utcTime converter localTime failed!!!", e);
			return utcTime;
		}
		SimpleDateFormat localFormater = new SimpleDateFormat(localTimePattern);
		localFormater.setTimeZone(TimeZone.getDefault());
		String localTime = localFormater.format(gpsUtcDate.getTime());
		return localTime;
	}

	/**
	 * 函数功能描述:修改时间格式后缀 函数使用场景:处理当后缀为:+8:00时,转换为:+08:00 或 -8:00转换为-08:00
	 * 
	 * @param subTime
	 * @param sign
	 * @return
	 */
	private static String changeUtcSuffix(String subTime, String sign) {
		String timeSuffix = null;
		String[] splitTimeArrayOne = subTime.split("\\" + sign);
		String[] splitTimeArrayTwo = splitTimeArrayOne[1].split(":");
		if (splitTimeArrayTwo[0].length() < 2) {
			timeSuffix = "+" + "0" + splitTimeArrayTwo[0] + ":"
					+ splitTimeArrayTwo[1];
			subTime = splitTimeArrayOne[0] + timeSuffix;
			return subTime;
		}
		return subTime;
	}

	/**
	 * 函数功能描述:获取本地时区的表示(比如:第八区-->+08:00)
	 * 
	 * @return
	 */
	public static String getTimeZoneByNumExpress() {
		Calendar cal = Calendar.getInstance();
		TimeZone timeZone = cal.getTimeZone();
		int rawOffset = timeZone.getRawOffset();
		int timeZoneByNumExpress = rawOffset / 3600 / 1000;
		String timeZoneByNumExpressStr = "";
		if (timeZoneByNumExpress > 0 && timeZoneByNumExpress < 10) {
			timeZoneByNumExpressStr = "+" + "0" + timeZoneByNumExpress + ":"
					+ "00";
		} else if (timeZoneByNumExpress >= 10) {
			timeZoneByNumExpressStr = "+" + timeZoneByNumExpress + ":" + "00";
		} else if (timeZoneByNumExpress > -10 && timeZoneByNumExpress < 0) {
			timeZoneByNumExpress = Math.abs(timeZoneByNumExpress);
			timeZoneByNumExpressStr = "-" + "0" + timeZoneByNumExpress + ":"
					+ "00";
		} else if (timeZoneByNumExpress <= -10) {
			timeZoneByNumExpress = Math.abs(timeZoneByNumExpress);
			timeZoneByNumExpressStr = "-" + timeZoneByNumExpress + ":" + "00";
		} else {
			timeZoneByNumExpressStr = "Z";
		}
		return timeZoneByNumExpressStr;
	}

	public static void main(String[] args) {
		// String
		// result=utc2Local("2017-06-14 09:37:50.788+08:00","yyyy-MM-dd HH:mm:ss.SSSXXX",
		// "yyyy-MM-dd HH:mm:ss.SSS");
		// System.out.println(result);

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = format.parse("2013-03-20 17:47:09");
			System.out.println(date);
			// 转换为UTC时间
			System.out.println(date.getTime() / 1000);
			// UTC转换为日期
			System.out.println(format.format(new Date(
					Long.valueOf(1363837989) * 1000)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
