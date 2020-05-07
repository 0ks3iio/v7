package net.zdsoft.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期时间工具类
 * 
 * @author liangxiao
 * @author huangwj
 * @value $Revision: 1.23 $, $Date: 2008/07/31 11:22:34 $
 */
public final class DateUtils {

	private static final int[] DAY_OF_MONTH = new int[] { 31, 28, 31, 30, 31,
			30, 31, 31, 30, 31, 30, 31 };

	/**
	 * 取得指定天数后的时间
	 * 
	 * @param date
	 *            基准时间
	 * @param dayAmount
	 *            指定天数，允许为负数
	 * @return 指定天数后的时间
	 */
	public static Date addDay(Date date, int dayAmount) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, dayAmount);
		return calendar.getTime();
	}

	/**
	 * 取得指定小时数后的时间
	 * 
	 * @param date
	 *            基准时间
	 * @param hourAmount
	 *            指定小时数，允许为负数
	 * @return 指定小时数后的时间
	 */
	public static Date addHour(Date date, int hourAmount) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, hourAmount);
		return calendar.getTime();
	}

	/**
	 * 取得指定分钟数后的时间
	 * 
	 * @param date
	 *            基准时间
	 * @param minuteAmount
	 *            指定分钟数，允许为负数
	 * @return 指定分钟数后的时间
	 */
	public static Date addMinute(Date date, int minuteAmount) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minuteAmount);
		return calendar.getTime();
	}
	
	/**
	 * 取得指定秒数后的时间
	 * @param date
	 * @param secondAmount
	 * @return
	 */
	public static Date addSecond(Date date, int secondAmount) {
		if (date == null) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, secondAmount);
		return calendar.getTime();
	}

	/**
	 * 比较两日期对象中的小时和分钟部分的大小.
	 * 
	 * @param date
	 *            日期对象1, 如果为 <code>null</code> 会以当前时间的日期对象代替
	 * @param anotherDate
	 *            日期对象2, 如果为 <code>null</code> 会以当前时间的日期对象代替
	 * @return 如果日期对象1大于日期对象2, 则返回大于0的数; 反之返回小于0的数; 如果两日期对象相等, 则返回0.
	 */
	public static int compareHourAndMinute(Date date, Date anotherDate) {
		if (date == null) {
			date = new Date();
		}

		if (anotherDate == null) {
			anotherDate = new Date();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hourOfDay1 = cal.get(Calendar.HOUR_OF_DAY);
		int minute1 = cal.get(Calendar.MINUTE);

		cal.setTime(anotherDate);
		int hourOfDay2 = cal.get(Calendar.HOUR_OF_DAY);
		int minute2 = cal.get(Calendar.MINUTE);

		if (hourOfDay1 > hourOfDay2) {
			return 1;
		} else if (hourOfDay1 == hourOfDay2) {
			// 小时相等就比较分钟
			return minute1 > minute2 ? 1 : (minute1 == minute2 ? 0 : -1);
		} else {
			return -1;
		}
	}

	/**
	 * 比较两日期对象的大小, 忽略秒, 只精确到分钟.
	 * 
	 * @param date
	 *            日期对象1, 如果为 <code>null</code> 会以当前时间的日期对象代替
	 * @param anotherDate
	 *            日期对象2, 如果为 <code>null</code> 会以当前时间的日期对象代替
	 * @return 如果日期对象1大于日期对象2, 则返回大于0的数; 反之返回小于0的数; 如果两日期对象相等, 则返回0.
	 */
	public static int compareIgnoreSecond(Date date, Date anotherDate) {
		if (date == null) {
			date = new Date();
		}

		if (anotherDate == null) {
			anotherDate = new Date();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		date = cal.getTime();

		cal.setTime(anotherDate);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		anotherDate = cal.getTime();

		return date.compareTo(anotherDate);
	}

	/**
	 * 比较两日期对象的大小, 忽略秒，分钟, 只精确到天.
	 * 
	 * @param date
	 *            日期对象1, 如果为 <code>null</code> 会以当前时间的日期对象代替
	 * @param anotherDate
	 *            日期对象2, 如果为 <code>null</code> 会以当前时间的日期对象代替
	 * @return 如果日期对象1大于日期对象2, 则返回大于0的数; 反之返回小于0的数; 如果两日期对象相等, 则返回0.
	 */
	public static int compareForDay(Date date, Date anotherDate) {
		if (date == null) {
			date = new Date();
		}

		if (anotherDate == null) {
			anotherDate = new Date();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		date = cal.getTime();

		cal.setTime(anotherDate);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		anotherDate = cal.getTime();

		return date.compareTo(anotherDate);
	}

	/**
	 * 取得当前时间的字符串表示，格式为2006-01-10 20:56:30.756
	 * 
	 * @return 当前时间的字符串表示
	 */
	public static String currentDate2String() {
		return date2String(new Date());
	}

	/**
	 * 取得当前时间的字符串表示，格式为2006-01-10
	 * 
	 * @return 当前时间的字符串表示
	 */
	public static String currentDate2StringByDay() {
		return date2StringByDay(new Date());
	}

	/**
	 * 取得今天的最后一个时刻
	 * 
	 * @return 今天的最后一个时刻
	 */
	public static Date currentEndDate() {
		return getEndDate(new Date());
	}

	/**
	 * 取得今天的第一个时刻
	 * 
	 * @return 今天的第一个时刻
	 */
	public static Date currentStartDate() {
		return getStartDate(new Date());
	}

	/**
	 * 得到当前 小时
	 * 
	 * @return
	 */
	public static int currentHour() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 得到当前 分钟
	 * 
	 * @return
	 */
	public static int currentMinute() {
		return Calendar.getInstance().get(Calendar.MINUTE);
	}

	/**
	 * 把时间转换成字符串，格式为2006-01-10 20:56:30.756
	 * 
	 * @param date
	 *            时间
	 * @return 时间字符串
	 */
	public static String date2String(Date date) {
		return date2String(date, "yyyy-MM-dd HH:mm:ss.SSS");
	}

	/**
	 * 按照指定格式把时间转换成字符串，格式的写法类似yyyy-MM-dd HH:mm:ss.SSS
	 * 
	 * @param date
	 *            时间
	 * @param pattern
	 *            格式
	 * @return 时间字符串
	 */
	public static String date2String(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		return (new SimpleDateFormat(pattern)).format(date);
	}

	/**
	 * 把时间转换成字符串，格式为2006-01-10
	 * 
	 * @param date
	 *            时间
	 * @return 时间字符串
	 */
	public static String date2StringByDay(Date date) {
		return date2String(date, "yyyy-MM-dd");
	}

	/**
	 * 把时间转换成字符串，格式为2006-01-10 20:56
	 * 
	 * @param date
	 *            时间
	 * @return 时间字符串
	 */
	public static String date2StringByMinute(Date date) {
		return date2String(date, "yyyy-MM-dd HH:mm");
	}

	/**
	 * 把时间转换成字符串，格式为2006-01-10 20:56:30
	 * 
	 * @param date
	 *            时间
	 * @return 时间字符串
	 */
	public static String date2StringBySecond(Date date) {
		return date2String(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 根据某星期几的英文名称来获取该星期几的中文数. <br>
	 * e.g. <li>monday -> 一</li> <li>sunday -> 日</li>
	 * 
	 * @param englishWeekName
	 *            星期的英文名称
	 * @return 星期的中文数
	 */
	public static String getChineseWeekNumber(String englishWeekName) {
		if ("monday".equalsIgnoreCase(englishWeekName)) {
			return "一";
		}

		if ("tuesday".equalsIgnoreCase(englishWeekName)) {
			return "二";
		}

		if ("wednesday".equalsIgnoreCase(englishWeekName)) {
			return "三";
		}

		if ("thursday".equalsIgnoreCase(englishWeekName)) {
			return "四";
		}

		if ("friday".equalsIgnoreCase(englishWeekName)) {
			return "五";
		}

		if ("saturday".equalsIgnoreCase(englishWeekName)) {
			return "六";
		}

		if ("sunday".equalsIgnoreCase(englishWeekName)) {
			return "日";
		}

		return null;
	}

	/**
	 * 根据指定的年, 月, 日等参数获取日期对象.
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param date
	 *            日
	 * @return 对应的日期对象
	 */
	public static Date getDate(int year, int month, int date) {
		return getDate(year, month, date, 0, 0);
	}

	/**
	 * 根据指定的年, 月, 日, 时, 分等参数获取日期对象.
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param date
	 *            日
	 * @param hourOfDay
	 *            时(24小时制)
	 * @param minute
	 *            分
	 * @return 对应的日期对象
	 */
	public static Date getDate(int year, int month, int date, int hourOfDay,
			int minute) {
		return getDate(year, month, date, hourOfDay, minute, 0);
	}

	/**
	 * 根据指定的年, 月, 日, 时, 分, 秒等参数获取日期对象.
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param date
	 *            日
	 * @param hourOfDay
	 *            时(24小时制)
	 * @param minute
	 *            分
	 * @param second
	 *            秒
	 * @return 对应的日期对象
	 */
	public static Date getDate(int year, int month, int date, int hourOfDay,
			int minute, int second) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, date, hourOfDay, minute, second);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	/**
	 * 取得某个日期是星期几，星期日是1，依此类推
	 * 
	 * @param date
	 *            日期
	 * @return 星期几
	 */
	public static int getDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 取得某个日期是一个月中的第几天
	 * 
	 * @param date
	 *            日期
	 * @return 几号
	 */
	public static int getDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 取得某个日期是一年中的第几天
	 * 
	 * @param date
	 *            日期
	 * @return 几号
	 */
	public static int getDayOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 获取某天的结束时间, e.g. 2005-10-01 23:59:59.999
	 * 
	 * @param date
	 *            日期对象
	 * @return 该天的结束时间
	 */
	public static Date getEndDate(Date date) {

		if (date == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);

		return cal.getTime();
	}

	/**
	 * 取得一个月最多的天数
	 * 
	 * @param year
	 *            年份
	 * @param month
	 *            月份，0表示1月，依此类推
	 * @return 最多的天数
	 */
	public static int getMaxDayOfMonth(int year, int month) {
		if (month == 1 && isLeapYear(year)) {
			return 29;
		}
		return DAY_OF_MONTH[month];
	}

	/**
	 * 得到指定日期的下一天
	 * 
	 * @param date
	 *            日期对象
	 * @return 同一时间的下一天的日期对象
	 */
	public static Date getNextDay(Date date) {
		return addDay(date, 1);
	}

	/**
	 * 获取某天的起始时间, e.g. 2005-10-01 00:00:00.000
	 * 
	 * @param date
	 *            日期对象
	 * @return 该天的起始时间
	 */
	public static Date getStartDate(Date date) {
		if (date == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	/**
	 * 根据日期对象来获取日期中的时间(HH:mm:ss).
	 * 
	 * @param date
	 *            日期对象
	 * @return 时间字符串, 格式为: HH:mm:ss
	 */
	public static String getTime(Date date) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(date);
	}

	/**
	 * 根据日期对象来获取日期中的时间(HH:mm).
	 * 
	 * @param date
	 *            日期对象
	 * @return 时间字符串, 格式为: HH:mm
	 */
	public static String getTimeIgnoreSecond(Date date) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(date);
	}

	/**
	 * 判断是否是闰年
	 * 
	 * @param year
	 *            年份
	 * @return 是true，否则false
	 */
	public static boolean isLeapYear(int year) {
		Calendar calendar = Calendar.getInstance();
		return ((GregorianCalendar) calendar).isLeapYear(year);
	}

	/**
	 * 把字符串转换成日期，格式为2006-01-10
	 * 
	 * @param str
	 *            字符串
	 * @return 日期
	 */
	public static Date string2Date(String str) {
		return string2Date(str, "yyyy-MM-dd");
	}

	/**
	 * 按照指定的格式把字符串转换成时间，格式的写法类似yyyy-MM-dd HH:mm:ss.SSS
	 * 
	 * @param str
	 *            字符串
	 * @param pattern
	 *            格式
	 * @return 时间
	 */
	public static Date string2Date(String str, String pattern) {
		if (Validators.isEmpty(str)) {
			return null;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = dateFormat.parse(str);
		} catch (ParseException e) {
			// ignore
		}
		return date;
	}

	/**
	 * getWeekBeginAndEndDate
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getWeekBeginAndEndDate(Date date, String pattern) {
		Date monday = getMondayOfWeek(date);
		Date sunday = getSundayOfWeek(date);
		return date2String(monday, pattern) + " - "
				+ date2String(sunday, pattern);
	}

	/**
	 * 根据日期取得对应周周一日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMondayOfWeek(Date date) {
		Calendar monday = Calendar.getInstance();
		monday.setTime(date);
		monday.setFirstDayOfWeek(Calendar.MONDAY);
		monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return monday.getTime();
	}

	/**
	 * 根据日期取得对应周周日日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getSundayOfWeek(Date date) {
		Calendar sunday = Calendar.getInstance();
		sunday.setTime(date);
		sunday.setFirstDayOfWeek(Calendar.MONDAY);
		sunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return sunday.getTime();
	}

	/**
	 * 
	 * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
	 * 
	 * @param date
	 * @return
	 */
	public static String getSeason(Date date, boolean showYear) {

		String season = StringUtils.EMPTY;

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		switch (month) {
		case Calendar.JANUARY:
		case Calendar.FEBRUARY:
		case Calendar.MARCH:
			if (showYear)
				season = year + "Q1";
			else
				season = "Q1";
			break;
		case Calendar.APRIL:
		case Calendar.MAY:
		case Calendar.JUNE:
			if (showYear)
				season = year + "Q2";
			else
				season = "Q2";
			break;
		case Calendar.JULY:
		case Calendar.AUGUST:
		case Calendar.SEPTEMBER:
			if (showYear)
				season = year + "Q3";
			else
				season = "Q3";
			break;
		case Calendar.OCTOBER:
		case Calendar.NOVEMBER:
		case Calendar.DECEMBER:
			if (showYear)
				season = year + "Q4";
			else
				season = "Q4";
			break;
		default:
			break;
		}
		return season;
	}

	/**
	 * 把字符串转换成日期，格式为2006-01-10 20:56:30
	 * 
	 * @param str
	 *            字符串
	 * @return 日期
	 */
	public static Date string2DateTime(String str) {
		return string2Date(str, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 取得一年中的第几周
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	private DateUtils() {
	}

	/**
	 * 取得在全年中的周次，默认星期一为周的第一天
	 * 
	 * @param date
	 * @param sundayIsFirst
	 *            星期日是否为每周第一天
	 * @return
	 */
	public static int getWeek(Date date, boolean sundayIsFirst) {
		Calendar c = org.apache.commons.lang3.time.DateUtils.toCalendar(date);
		int t = c.get(Calendar.DAY_OF_WEEK);
		int inf = 0;
		if (t == Calendar.SUNDAY) {
			inf = sundayIsFirst ? 0 : -1;
		}
		if (c.get(Calendar.YEAR) != org.apache.commons.lang3.time.DateUtils
				.toCalendar(date).get(Calendar.YEAR)) {
			c.add(Calendar.DAY_OF_YEAR, -7);
			int week = c.get(Calendar.WEEK_OF_YEAR);
			return week + inf;
		} else {
			int week = c.get(Calendar.WEEK_OF_YEAR);
			return week + inf;
		}
	}

	/**
	 * 根据指定日期开始的为第一周，取得周次，默认星期一为周的第一天
	 * 
	 * @param date
	 * @param sundayIsFirst
	 *            星期日是否为每周第一天
	 * @param baseDate
	 *            基于此日期的周次
	 * @return
	 */
	public static int getWeek(Date date, boolean sundayIsFirst, Date baseDate) {
		Calendar cs = org.apache.commons.lang3.time.DateUtils
				.toCalendar(baseDate);
		Calendar ce = org.apache.commons.lang3.time.DateUtils.toCalendar(date);
		int yearS = cs.get(Calendar.YEAR);
		int yearE = ce.get(Calendar.YEAR);
		if (yearS == yearE)
			return getWeek(ce.getTime(), sundayIsFirst)
					- getWeek(cs.getTime(), sundayIsFirst) + 1;
		else {
			int totalDays = 0;
			for (int i = yearS; i <= yearE; i++) {
				int totalDaysThisYear = 0;
				if (i % 400 == 0 || i % 4 == 0) {
					totalDaysThisYear = 366;
				} else {
					totalDaysThisYear = 365;
				}

				if (i == yearS) {
					totalDays += totalDaysThisYear
							- cs.get(Calendar.DAY_OF_YEAR) + 1;
				} else if (i == yearE) {
					totalDays += ce.get(Calendar.DAY_OF_YEAR);
				} else {
					totalDays += totalDaysThisYear;
				}
			}

			int weeks = 0;
			int beginDay = org.apache.commons.lang3.time.DateUtils.toCalendar(
					baseDate).get(Calendar.DAY_OF_WEEK);
			int endDay = org.apache.commons.lang3.time.DateUtils.toCalendar(
					date).get(Calendar.DAY_OF_WEEK);
			if (sundayIsFirst) {
				if (endDay == Calendar.SUNDAY) {
					weeks++;
				}
			} else {
				if (beginDay == Calendar.SUNDAY) {
					weeks++;
				}
			}

			// totalDays --;
			weeks += totalDays / 7;
			return weeks == 0 ? 1 : weeks;

		}
	}

	/**
	 * 是否有失效的日期格式
	 */
	public static boolean isDate(String str) {
		if (StringUtils.isEmpty(str) || str.length() > 10) {
			return false;
		}

		String[] items = null;
		if (str.indexOf("-") > 0) {
			items = str.split("-");
		} else if (str.indexOf("/") > 0) {
			items = str.split("/");
		}

		if (items == null)
			return false;

		if (items.length != 3) {
			return false;
		}

		if (!net.zdsoft.framework.utils.StringUtils.isNumber(items[0], 1900,
				3000)
				|| !net.zdsoft.framework.utils.StringUtils.isNumber(items[1],
						1, 12)) {
			return false;
		}

		int year = Integer.parseInt(items[0]);
		int month = Integer.parseInt(items[1]);

		return net.zdsoft.framework.utils.StringUtils.isNumber(items[2], 1,
				DateUtils.getMaxDayOfMonth(year, month - 1));
	}

	/*
	 * 将时间戳转换为时间
	 */
	public static String stampToDate(String s) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}

	/*
	 * 将时间戳转换为时间
	 */
	public static String getTimeSection(String s) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}

	/**
	 * 根据小时判断是否为上午、中午、下午、晚上、夜间
	 * 
	 * @param hour
	 * @return
	 */
	public static String getDuringDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);

		if (hour >= 6 && hour < 11) {
			return "上午";
		}
		if (hour >= 11 && hour < 14) {
			return "中午";
		}
		if (hour >= 14 && hour < 18) {
			return "下午";
		}
		if (hour >= 18 && hour < 22) {
			return "晚上";
		}
		return "夜间";
	}

	public static String getHours(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY) + "";
	}

	/**
	 * 根据日期获得星期的方法
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekOfDate(Date date) {
		String[] weekDaysName = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
				"星期六" };
		// String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return weekDaysName[intWeek];
	}

	// 由出生日期获得年龄
	public static String getAge(Date birthDay) throws Exception {
		if (birthDay == null)
			return "未知";
		Calendar cal = Calendar.getInstance();
		if (cal.before(birthDay)) {
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth)
					age--;
			} else {
				age--;
			}
		}
		return String.valueOf(age);
	}

	public static void main(String[] args) throws ParseException {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// Date oldD = sdf.parse("2017-07-02");
		// Date nowD = sdf.parse("2017-07-31");
		// System.out.println(getWeek(nowD, false, oldD));
		System.out.println(new Date());
	}
}
