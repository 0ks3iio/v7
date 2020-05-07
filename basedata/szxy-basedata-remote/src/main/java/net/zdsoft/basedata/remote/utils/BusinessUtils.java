/* 
 * @(#)BusinessUtils.java    Created on Sep 19, 2010
 * Copyright (c) 2010 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.remote.utils;

import net.zdsoft.basedata.entity.Unit;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 业务工具类
 * 
 * @author zhaosf
 * @version $Revision: 1.0 $, $Date: Sep 19, 2010 7:59:07 PM $
 */
public class BusinessUtils {
	private static final Logger log = LoggerFactory
			.getLogger(BusinessUtils.class);

	private static final DateFormat[] ACCEPT_DATE_FORMATS = {
			new SimpleDateFormat("yyyy/MM/dd"),
			new SimpleDateFormat("yyyy/M/d"),
			new SimpleDateFormat("yyyy-MM-dd"),
			new SimpleDateFormat("yyyy-M-d"), new SimpleDateFormat("yyyyMMdd") };

	static String[] valCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4",
			"3", "2" };
	static String[] wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3",
			"7", "9", "10", "5", "8", "4", "2" };

//	private static List<String> listOfRegionCode;

	/**
	 * 生成临时身份证号（三个参数都可以为null）,最后一位是T
	 * 
	 * @param regionCode6位
	 *            （310010）
	 * @param birth
	 * @param sex
	 *            1位（1或2）
	 * @return
	 */
//	public static String generateIdentityCard(String regionCode, Date birthday,
//			String sex) {
//		String birth = BusinessUtils.date2String(birthday, "yyyyMMdd");
//		return generateIdentityCard(regionCode, birth, sex);
//	}

	/**
	 * 自动生成临时身份证
	 * 
	 * @param regionCode
	 *            行政区划码，6位，可以为空（自动从行政区划表中随机取数）
	 * @param birth
	 *            可以为空
	 * @param sex
	 *            可以为空
	 * @param verifyCode
	 *            如果为空，表示按照身份证规则，自动生成校验位
	 * @return
	 */
//	public static String generateIdentityCardWithVerifyCode(String regionCode,
//			String birth, String sex, String verifyCode) {
//		if (StringUtils.isBlank(regionCode)
//				|| !NumberUtils.isNumber(regionCode)
//				|| regionCode.length() != 6) {
//			int count = getAreaCodeListFromDB().size();
//			regionCode = getAreaCodeListFromDB()
//					.get(RandomUtils.nextInt(count));
//			// 因为现在数据库中存在有字母的行政区划码，所以我们一直取到都是数字的为止
//			while (!NumberUtils.isNumber(regionCode))
//				regionCode = getAreaCodeListFromDB().get(
//						RandomUtils.nextInt(count));
//		}
//
//		if (StringUtils.isNotBlank(birth)) {
//			Date birthday = string2Date(birth);
//			if (birthday != null) {
//				if (birthday.after(new Date())) {
//					birth = null;
//				} else {
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//					birth = sdf.format(birthday);
//				}
//			} else
//				birth = null;
//		}
//
//		if (StringUtils.isBlank(birth)) {
//			Calendar c = Calendar.getInstance();
//			int yearStart = 2005;
//			int yearEnd = c.get(Calendar.YEAR);
//			int inc = RandomUtils.nextInt(yearEnd - yearStart);
//			c.set(Calendar.YEAR, yearStart + inc);
//			int month = RandomUtils.nextInt(12);
//			c.set(Calendar.MONDAY, month);
//			int day = 0;
//			// 二月份
//			if (month != 1) {
//				day = RandomUtils.nextInt(30) + 1;
//			} else {
//				day = RandomUtils.nextInt(28) + 1;
//			}
//			c.set(Calendar.DAY_OF_MONTH, day);
//			if (StringUtils.isBlank(regionCode)) {
//				int count = getAreaCodeListFromDB().size();
//				regionCode = getAreaCodeListFromDB().get(
//						RandomUtils.nextInt(count));
//			}
//			birth = c.get(Calendar.YEAR)
//					+ StringUtils.leftPad("" + (c.get(Calendar.MONTH) + 1), 2,
//							"0")
//					+ StringUtils.leftPad("" + c.get(Calendar.DAY_OF_MONTH), 2,
//							"0");
//		}
//
//		if (StringUtils.isBlank(sex)) {
//			sex = "" + RandomUtils.nextInt(2);
//		}
//
//		StringBuffer identityCard = new StringBuffer(regionCode);
//		identityCard.append(birth);
//		Random random = new Random();
//		// 生成两位随机顺序号
//		identityCard.append(random.nextInt(10));
//		identityCard.append(random.nextInt(10));
//		boolean cyc = true;
//		while (cyc) {
//			int s = random.nextInt(10);
//			int iSex = NumberUtils.toInt(sex);
//			iSex = iSex % 2;
//			if (s % 2 == iSex) {
//				cyc = false;
//				sex = "" + s;
//			}
//		}
//
//		identityCard.append(sex);
//		if (StringUtils.isNotBlank(verifyCode))
//			identityCard.append(verifyCode);
//		else
//			identityCard.append(getVerifyCode(identityCard.toString()));
//		return identityCard.toString();
//	}

	public static String getVerifyCode(String identityCardPrefix) {
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi
					+ Integer.parseInt(String.valueOf(identityCardPrefix
							.charAt(i))) * Integer.parseInt(wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		return valCodeArr[modValue].toUpperCase();
	}

	/**
	 * 生成临时身份证号（三个参数都可以为null）,最后一位是T
	 * 
	 * @param regionCode6位
	 *            （310010）
	 * @param birth
	 *            8位（20101111）
	 * @param sex
	 *            1位（1或2）
	 * @return
	 */
//	public static String generateIdentityCard(String regionCode, String birth,
//			String sex) {
//		return generateIdentityCardWithVerifyCode(regionCode, birth, sex, "T");
//	}

	/**
	 * 从身份证中取出生日期
	 * 
	 * @param identityCard
	 * @return
	 */
	public static String getDateStrFromIdentityNo(String identityCard) {
		if (identityCard == null) {
			return null;
		}
		String year;
		String month;
		String day;
		String dateStr;
		identityCard = identityCard.trim();
		if (identityCard.length() == 15) {
			year = "19" + identityCard.substring(6, 8);
			month = identityCard.substring(8, 10);
			day = identityCard.substring(10, 12);
			dateStr = year + "-" + month + "-" + day;
		} else if (identityCard.length() == 18) {
			year = identityCard.substring(6, 10);
			month = identityCard.substring(10, 12);
			day = identityCard.substring(12, 14);
			dateStr = year + "-" + month + "-" + day;
		} else {
			return null;
		}
		if (!isDate(dateStr))
			return null;
		return dateStr;
	}

	/**
	 * 计算单位的行政级别
	 * 
	 * @param regionCode
	 * @param unitClass
	 * @param isCountry
	 *            是否乡镇
	 * @return
	 */
	public static Integer getUnitRegionLevel(String regionCode, int unitClass,
			boolean isCountry) {
		if (StringUtils.isBlank(regionCode)
				|| regionCode.length() != 6) {
			return null;
		}
		String compareString = "00";
		String s1 = regionCode.substring(0, 2);
		String s2 = regionCode.substring(2, 4);
		String s3 = regionCode.substring(4, 6);
		if (compareString.equals(s1))
			return null;
		if (compareString.equals(s2)) {
			if (unitClass == Unit.UNIT_CLASS_EDU)
				return 2;
			else
				return 3;
		}
		if (compareString.equals(s3)) {
			if (unitClass == Unit.UNIT_CLASS_EDU)
				return 3;
			else
				return 4;
		}
		if (unitClass == Unit.UNIT_CLASS_EDU)
			if (isCountry)
				return 5;
			else
				return 4;
		else if (isCountry)
			return 6;
		else
			return 5;
	}

	/**
	 * 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，
	 * 三位数字顺序码和一位数字校验码。
	 * 
	 * 2、地址码(前六位数） 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。
	 * 
	 * 3、出生日期码（第七位至十四位） 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
	 * 
	 * 4、顺序码（第十五位至十七位）
	 * 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配给女性。
	 * 
	 * 5、校验码（第十八位数） （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16
	 * ，先对前17位数字的权求和 Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6
	 * 3 7 9 10 5 8 4 2 （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7
	 * 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
	 * 
	 * 所以我们就可以大致写一个函数来校验是否正确了。
	 */

	/**
	 * ======================================================================
	 * 功能：身份证的有效验证
	 * 
	 * @param IDStr
	 *            身份证号
	 * @param isStrict
	 *            如果不严格校验，如下两类也可以通过（临时身份证号17位数字+T 、校验位不作校验）
	 * @return 返回验证结果，没有内容返回，则为正确
	 * @throws ParseException
	 */
	public static String validateIdentityCard(String IDStr, boolean isStrict) {

		// GetAreaCodeFromDB

		if (StringUtils.isBlank(IDStr))
			return "";

		IDStr = StringUtils.trim(StringUtils.upperCase(IDStr));
		String errorInformation = "";

		// String[] Checker = {"1","9","8","7","6","5","4","3","2","1","1"};
		String ai = "";

		// ================ 号码的长度 15位或18位 ================
		if (IDStr.length() != 15 && IDStr.length() != 18) {
			errorInformation += "身份证号码长度应该为15位或18位; ";
			return errorInformation;
		}
		if (!IDStr.matches("(^\\d{15}$)|(^\\d{17}[TXx0-9]$)")) {
			errorInformation += "身份证格式不对";
			return errorInformation;
		}

		// =======================(end)========================

		// ================ 数字 除最后以为都为数字 ================
		if (IDStr.length() == 18) {
			ai = IDStr.substring(0, 17);
		} else if (IDStr.length() == 15) {
			ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
		}
		if (NumberUtils.isNumber(ai) == false) {
			errorInformation += "身份证无效：15位号码都应为数字; 18位号码除最后一位外，都应为数字; ";
			log.info("[identity card validator]identity card should for the digital when the length is 15, else, the length is 18, In addition to the last one, the others should be for the digital.");
			return errorInformation;
		}
		// =======================(end)========================

		// ================ 出生年月是否有效 ================
		String strYear = ai.substring(6, 10);// 年份
		String strMonth = ai.substring(10, 12);// 月份
		String strDay = ai.substring(12, 14);// 月份

		if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
			errorInformation += "身份证无效：生日无效; ";
			return errorInformation;
		}

		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(
							strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
				errorInformation += "身份证无效：生日不在有效范围; ";
				return errorInformation;
			}
		} catch (NumberFormatException e) {
			errorInformation += "身份证无效：生日不在有效范围; ";
			log.info("[identity card validator]Birthday is not an effective framework");
		} catch (ParseException e) {
			errorInformation += "身份证无效：生日不在有效范围; ";
			log.info("[identity card validator]Birthday is not an effective framework");
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			errorInformation += "身份证无效：月份无效; ";
			log.info("[identity card validator]Invalid month");
			return errorInformation;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			errorInformation += "身份证无效：日期无效";
			log.info("[identity card validator]invalid day");
			return errorInformation;
		}
		// =====================(end)=====================

		// ================ 地区码时候有效 ================
		// if (!getAreaCodeFromDB().contains(ai.substring(0, 6))) {
		// errorInformation += "地区编码错误; ";
		// return errorInformation;
		// }
		// ==============================================

		// ================ 判断最后一位的值 ================
		// 如果不严格校验，则忽略校验位
		if (!isStrict) {
			return "";
		}

		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi
					+ Integer.parseInt(String.valueOf(ai.charAt(i)))
					* Integer.parseInt(wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = valCodeArr[modValue];
		ai = ai + strVerifyCode;

		if (IDStr.length() == 18) {
			if (ai.equalsIgnoreCase(IDStr) == false) {
				errorInformation += "身份证无效：最后一位字母错误; ";
				log.info("[identity card validator]invalid identity card, the last letter is wrong!");
				return errorInformation;
			}
		} else {
			log.info("[identity card validator] no:" + ai);
			return "";
		}
		// =====================(end)=====================
		return "";
	}

	/**
	 * 验证不同类型的 证件格式是否正确
	 * @param cardType 证件类型
	 * @param identityCard 证件号
	 * @param isStrict 是否严格验证 如果不严格校验，如下两类也可以通过（临时身份证号17位数字+T 、校验位不作校验）
	 * @return
	 */
	public static String validateTypeIdentityCard(String cardType, String identityCard,boolean isStrict){
		if ("1".equals(cardType)) {
			final String tistr = validateIdentityCard(identityCard,isStrict);
			if (StringUtils.isNotBlank(tistr)) {
				return tistr;
			}
		} else if ("6".equals(cardType)) {
			final String reg = "^[0-9A-Z]{1,2}[0-9]{6}\\([0-9A-Z]\\)$";
			if (!Pattern.matches(reg, identityCard)) {
				return "证件号格式不对";
			}
		} else if ("7".equals(cardType)) {
			final String reg = "^[157][0-9]{6}\\([0-9]\\)$";
			if (!Pattern.matches(reg, identityCard)) {
				return "证件号格式不对";
			}
		} else if ("8".equals(cardType)) {
			final String reg = "^[0-9]{8}$";
			if (!Pattern.matches(reg, identityCard)) {
				return "证件号格式不对";
			}
		}

		return null;
	}
	/**
	 * ======================================================================
	 * 功能：身份证的有效验证 同时验证生日和性别
	 * 
	 * @param IDStr
	 *            身份证号
	 * @param birthDay
	 *            2010-10-10
	 * @param IDStr
	 *            1位数字
	 * @param isStrict
	 *            如果不严格校验，如下两类也可以通过（临时身份证号17位数字+T 、校验位不作校验）
	 * @return 返回验证结果，没有内容返回，则为正确
	 * @throws ParseException
	 */
	public static String validateIdentityCard(String IDStr, String birthDay,
			int sex, boolean isStrict) {
		String errorInformation = "";
		String[] valCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",
				"3", "2" };
		String[] wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
				"9", "10", "5", "8", "4", "2" };
		// String[] Checker = {"1","9","8","7","6","5","4","3","2","1","1"};
		String ai = "";

		// ================ 号码的长度 15位或18位 ================
		if (IDStr.length() != 15 && IDStr.length() != 18) {
			errorInformation += "身份证号码长度应该为15位或18位; ";
			log.info("[identity card validator]the length of the identity card is not 15 or 18!");
			return errorInformation;
		}
		// =======================(end)========================

		// ================ 数字 除最后以为都为数字 ================
		if (IDStr.length() == 18) {
			ai = IDStr.substring(0, 17);
		} else if (IDStr.length() == 15) {
			ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
		}
		if (NumberUtils.isNumber(ai) == false) {
			errorInformation += "15位号码都应为数字; 18位号码除最后一位外，都应为数字; ";
			log.info("[identity card validator]identity card should for the digital when the length is 15, else, the length is 18, In addition to the last one, the others should be for the digital.");
			return errorInformation;
		}
		// =======================(end)========================
		// ================ 出生年月是否有效 ================
		String strYear = ai.substring(6, 10);// 年份
		String strMonth = ai.substring(10, 12);// 月份
		String strDay = ai.substring(12, 14);// 月份
		String strSex = ai.substring(16, 17);
		if (!DateUtils.isSameDay(string2Date(strYear + "-" + strMonth + "-"
				+ strDay), string2Date(birthDay))) {
			errorInformation += "身份证号和出生日期不匹配; ";
			log.info("[identity card validator]birthday is not a valid");
			return errorInformation;
		}
		int fixedSex = Integer.valueOf(strSex) % 2;
		if (fixedSex == 0) {
			fixedSex = 2;
		}
		if (sex != fixedSex) {
			errorInformation += "身份证号和性别不匹配; ";
			log.info("[identity card validator]birthday is not a valid");
			return errorInformation;
		}

		if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
			errorInformation += "生日无效; ";
			log.info("[identity card validator]birthday is not a valid");
			return errorInformation;
		}

		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(
							strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
				errorInformation += "生日不在有效范围; ";
				log.info("[identity card validator]Birthday is not an effective framework");
				return errorInformation;
			}
		} catch (NumberFormatException e) {
			errorInformation += "生日不在有效范围; ";
			log.info("[identity card validator]Birthday is not an effective framework");
		} catch (ParseException e) {
			errorInformation += "生日不在有效范围; ";
			log.info("[identity card validator]Birthday is not an effective framework");
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			errorInformation += "月份无效; ";
			log.info("[identity card validator]Invalid month");
			return errorInformation;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			errorInformation += "日期无效";
			log.info("[identity card validator]invalid day");
			return errorInformation;
		}
		// =====================(end)=====================

		// ================ 地区码时候有效 ================
		// Map<String, String> h = GetAreaCodeMapFromDB();
		// if (h.get(ai.substring(0, 2)) == null) {
		// errorInformation += "地区编码错误; ";
		// log.error("region code error!");
		// return errorInformation;
		// }
		// ==============================================

		// ================ 判断最后一位的值 ================
		// 如果不严格校验，则忽略校验位
		if (!isStrict) {
			return "";
		}

		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi
					+ Integer.parseInt(String.valueOf(ai.charAt(i)))
					* Integer.parseInt(wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = valCodeArr[modValue];
		ai = ai + strVerifyCode;

		if (IDStr.length() == 18) {
			if (ai.equalsIgnoreCase(IDStr) == false) {
				errorInformation += "身份证无效，最后一位字母错误; ";
				log.info("[identity card validator]invalid identity card, the last letter is wrong!");
				return errorInformation;
			}
		} else {
			log.info("[identity card validator] no:" + ai);
			return "";
		}
		// =====================(end)=====================
		// log.info("[identity card validator] area:" + h.get(ai.substring(0,
		// 2)));
		return "";
	}

	/**
	 * 严格校验身份证号
	 * 
	 * @param identityCard
	 * @return
	 */
	public static String validateIdentityCard(String identityCard) {
		return validateIdentityCard(identityCard, true);
	}

//	private static RegionRemoteService regionRemoteService;

	/**
	 * 从数据库中取出省级行政区划
	 * 
	 * @return
	 */
//	private static List<String> getAreaCodeListFromDB() {
//		if (listOfRegionCode != null) {
//			return listOfRegionCode;
//		}
//
//		listOfRegionCode = new ArrayList<String>();
//		if (null == regionRemoteService) {
//			regionRemoteService = (RegionRemoteService) Evn
//					.getBean("regionService");
//		}
//		Map<String, String> map = new HashMap<String, String>();//regionRemoteService.getRegionFullCodeMap();
//		Set<String> set = map.keySet();
//		for (String key : set)
//			listOfRegionCode.add(key);
//		return listOfRegionCode;
//	}

	/**
	 * 从数据库中取出省级行政区划
	 * 
	 * @return
	 */
//	private static Map<String, String> GetAreaCodeMapFromDB() {
//		if (null == regionRemoteService) {
//			regionRemoteService = (RegionRemoteService) Evn
//					.getBean("regionService");
//		}
//		List<Region> list = new ArrayList<Region>();//regionRemoteService.getSubRegionsBy2(); TODO
//		Map<String, String> map = new HashMap<String, String>();
//		for (Region region : list) {
//			map.put(region.getRegionCode().trim(), region.getRegionName());
//		}
//		return map;
//	}

	/**
	 * 日期转为字符型格式
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String date2String(Date date, String pattern) {
		if (date == null)
			return null;
		try {
			SimpleDateFormat sf = new SimpleDateFormat(pattern);
			return sf.format(date);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 日期转为字符型格式， 默认格式 yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String date2String(Date date) {
		return date2String(date, "yyyy-MM-dd");
	}

	/**
	 * 字符型日期转为日期格式
	 * 
	 * @param dateS
	 * @return
	 */
	public static Date string2Date(String dateS) {
		Date date = null;
		for (DateFormat format : ACCEPT_DATE_FORMATS) {
			try {
				date = format.parse(dateS);
				break;
			} catch (Exception e) {
				continue;
			}
		}
		return date;
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
		Calendar c = DateUtils.toCalendar(date);
		int t = c.get(Calendar.DAY_OF_WEEK);
		int inf = 0;
		if (t == Calendar.SUNDAY) {
			inf = sundayIsFirst ? 0 : -1;
		}
		if (c.get(Calendar.YEAR) != DateUtils.toCalendar(date).get(
				Calendar.YEAR)) {
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
		Calendar cs = DateUtils.toCalendar(baseDate);
		Calendar ce = DateUtils.toCalendar(date);
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
			int beginDay = DateUtils.toCalendar(baseDate).get(
					Calendar.DAY_OF_WEEK);
			int endDay = DateUtils.toCalendar(date).get(Calendar.DAY_OF_WEEK);
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
			// int lw = totalDays % 7;
			// if(lw + beginDay - 1 > 7){
			// return weeks + 2;
			// }
			// else{
			// return weeks + 1;
			// }
			return weeks == 0 ? 1 : weeks;

		}
	}

	/**
	 * 周次的第一天
	 * 
	 * @param week
	 *            周次
	 * @param sundayIsFirst
	 *            星期日是否为每周第一天
	 * @param baseDate
	 *            week是基于此日期开始的周次
	 * @return
	 */
	public static Date getFirstDateByWeek(int week, boolean sundayIsFirst,
			Date baseDate) {
		Calendar c = DateUtils.toCalendar(baseDate);
		c.add(Calendar.DAY_OF_YEAR, (week - 1) * 7);
		if (sundayIsFirst) {
			c.add(Calendar.DAY_OF_YEAR,
					Calendar.SUNDAY - c.get(Calendar.DAY_OF_WEEK));
		} else {
			if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				c.add(Calendar.DAY_OF_YEAR, -6);
			} else {
				c.add(Calendar.DAY_OF_YEAR,
						Calendar.MONDAY - c.get(Calendar.DAY_OF_WEEK));
			}
		}
		return c.getTime();
	}

	/**
	 * 周次的第一天
	 * 
	 * @param week
	 *            周次
	 * @param sundayIsFirst
	 *            星期日是否为每周第一天
	 * @return
	 */
	public static Date getFirstDateByWeek(int week, boolean sundayIsFirst) {
		return getFirstDateByWeek(week, sundayIsFirst, new Date());
	}

	/**
	 * 指定周次，工作日的第一天
	 * 
	 * @param week
	 * @return
	 */
	public static Date getFirstWorkDateByWeek(int week) {
		return getFirstDateByWeek(week, false);
	}

	/**
	 * 指定周次，工作日的第一天
	 * 
	 * @param week
	 * @param baseDate
	 *            week是基于此日期开始的周次
	 * @return
	 */
	public static Date getFirstWorkDateByWeek(int week, Date baseDate) {
		return getFirstDateByWeek(week, false, baseDate);
	}

	/**
	 * 指定周次，工作日的第一天
	 * 
	 * @param week
	 * @param baseDate
	 *            week是基于此日期开始的周次
	 * @return
	 */
	public static Date getFirstWorkDateByWeek(int week, boolean sundayIsFirst,
			Date baseDate) {
		return getFirstDateByWeek(week, sundayIsFirst, baseDate);
	}

	/**
	 * 周次的最后一天
	 * 
	 * @param week
	 *            周次
	 * @param sundayIsFirst
	 *            星期日是否为每周第一天
	 * @param baseDate
	 *            week是基于此日期开始的周次
	 * @return
	 */
	public static Date getLastDateByWeek(int week, boolean sundayIsFirst,
			Date baseDate) {
		Date date = getFirstDateByWeek(week, sundayIsFirst, baseDate);
		Calendar c = DateUtils.toCalendar(date);
		c.add(Calendar.DAY_OF_YEAR, 6);
		return c.getTime();
	}

	/**
	 * 周次的最后一天
	 * 
	 * @param week
	 *            周次
	 * @param sundayIsFirst
	 *            星期日是否为每周第一天
	 * @return
	 */
	public static Date getLastDateByWeek(int week, boolean sundayIsFirst) {
		Date date = getFirstDateByWeek(week, sundayIsFirst);
		Calendar c = DateUtils.toCalendar(date);
		c.add(Calendar.DAY_OF_YEAR, 6);
		return c.getTime();
	}

	/**
	 * 指定周次，工作日的最后一天
	 * 
	 * @param week
	 * @return
	 */
	public static Date getLastWorkDateByWeek(int week, boolean sundayIsFirst,
			Date baseDate) {
		Date date = getFirstWorkDateByWeek(week, sundayIsFirst, baseDate);
		Calendar c = DateUtils.toCalendar(date);
		c.add(Calendar.DAY_OF_YEAR, 4);
		return c.getTime();
	}

	/**
	 * 指定周次，工作日的最后一天
	 * 
	 * @param week
	 * @return
	 */
	public static Date getLastWorkDateByWeek(int week, Date baseDate) {
		Date date = getFirstWorkDateByWeek(week, baseDate);
		Calendar c = DateUtils.toCalendar(date);
		c.add(Calendar.DAY_OF_YEAR, 4);
		return c.getTime();
	}

	/**
	 * 指定周次，工作日的最后一天
	 * 
	 * @param week
	 * @return
	 */
	public static Date getLastWorkDateByWeek(int week) {
		return getLastWorkDateByWeek(week, new Date());
	}

	/**
	 * 根据出生日期得到年龄
	 * 
	 * @param birthday
	 * @return 出生日期大于当前，返回-1
	 */
	public static int getAgeByBirthday(Date birthday) {
		int age = -1;
		if (birthday == null) {
			return age;
		}
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthday)) { // 出生日期在未来
			return age;
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthday);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		age = yearNow - yearBirth;
		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				age--;
			}
		}
		return age;
	}

	/**
	 * 根据出生年和当前年获取年龄
	 * @param birthday
	 * @return
	 * @author huy
	 * @date 2014-10-30下午03:47:26
	 */
	public static int getAgeByBirthYear(Date birthday){
		int age = -1;
		if(birthday == null){
			return age;
		}
		Calendar cal = Calendar.getInstance();
        if (cal.before(birthday)) { // 出生日期在未来
            return age; 
        }
        int yearNow = cal.get(Calendar.YEAR);
        cal.setTime(birthday); 
        int yearBirth = cal.get(Calendar.YEAR);
        return yearNow - yearBirth;
	}
	
	/**
	 * 根据出生日期和指定年获取生日
	 * @param birthday
	 * @param year
	 * @return
	 * @author huy
	 * @date 2015-7-21下午04:42:55
	 */
	public static int getAgeByYear(Date birthday,int year){
		int age = -1;
		if(birthday == null){
			return age;
		}
		Calendar cal = Calendar.getInstance();
        if (cal.before(birthday)) { // 出生日期在未来
            return age; 
        }
        cal.setTime(birthday); 
        int yearBirth = cal.get(Calendar.YEAR);
        return year - yearBirth;
	}
	
	/**
	 * 以9月为分界获取年龄
	 * @param birthday
	 * @return
	 * @author huy
	 * @date 2015-7-21下午04:44:33
	 */
	public static int getAgeBy9Mon(Date birthday,int year) {
		int age = -1;
		if (birthday == null) {
			return age;
		}
		Calendar cal = Calendar.getInstance();
		if (cal.before(birthday)) { // 出生日期在未来
			return age;
		}
		int yearNow = year;
		int monthNow = 9;
		cal.setTime(birthday);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH)+1;
		age = yearNow - yearBirth;
		if (monthNow <=monthBirth) {
			age--;
		}
		return age;
	}
	
	public static List<String> getNumList(Integer size){
		ArrayList<String> list = new ArrayList<>();
		if(size<0)
			return list;
		for(int i=0;i<size;i++){
			list.add(i+1+"");
		}
		return list;
	}


	private static boolean isDate(String str) {
		if (StringUtils.isBlank(str) || str.length() > 10) {
			return false;
		}

		List<String> listOfPatten = new ArrayList<String>();
		listOfPatten.add("yyyy-MM-dd");
		listOfPatten.add("yyyy/MM/dd");
		listOfPatten.add("yyyy/M/d");
		listOfPatten.add("yyyy-M-d");
		listOfPatten.add("yyyyMMdd");

		try {
			DateUtils.parseDateStrictly(str, listOfPatten.toArray(new String[0]));
			return true;
		}
		catch (ParseException e) {
		}

		return false;
	}
}
