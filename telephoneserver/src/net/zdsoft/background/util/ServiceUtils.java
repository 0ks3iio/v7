package net.zdsoft.background.util;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import net.zdsoft.background.common.ServiceLocator;
import net.zdsoft.keel.util.UUIDUtils;
import net.zdsoft.keel.util.Validators;
import net.zdsoft.message.client.MessageClient;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public final class ServiceUtils {
	 private static Logger logger = Logger.getLogger(ServiceUtils.class);
	  private static NumberFormat numberFormat = NumberFormat.getInstance();
//	  private static final long TIME_OF_ONE_DAY = 86400000L;
//	  private static final long TIME_OF_ONE_HOUR = 3600000L;
//	  private static final long TIME_OF_ONE_MINUTE = 60000L;
	  private static String mobileRegexString = "(1(3[4-9]|4[7]|5[012789]|8[2378])\\d{8})|(0001\\d{8})";
	  private static String unicomRegexString = "(1(3[0-2]|5[56]|8[56])\\d{8})|(0002\\d{8})";
	  private static String telecomRegexString = "(?!00|015|013)(0\\d{9,11})|(0003\\d{8})|(1(33|53|89|80)\\d{8})";
//	  private static final int SMS_SINGLE_LENGTH = 70;
//	  private static final int SMS_MULTIPLE_LENGTH = 65;
	  
	  public static int dateCompare(Date date1, Date date2)
	  {
	    long dateRange = date1.getTime() - date2.getTime();
	    return (int)(dateRange / 86400000L);
	  }
	  
	  public static int hourCompare(Date date1, Date date2)
	  {
	    long dateRange = date1.getTime() - date2.getTime();
	    return (int)(dateRange / 3600000L);
	  }
	  
	  public static int minuteCompare(Date date1, Date date2)
	  {
	    long dateRange = date1.getTime() - date2.getTime();
	    return (int)(dateRange / 60000L);
	  }
	  
	  public static int getMobileType(String mobile)
	  {
	    if (Pattern.matches(mobileRegexString, mobile)) {
	      return 1;
	    }
	    if (Pattern.matches(unicomRegexString, mobile)) {
	      return 2;
	    }
	    if (Pattern.matches(telecomRegexString, mobile)) {
	      return 3;
	    }
	    return 0;
	  }
	  
	  @Deprecated
	  public static int getMobileTypeOld(String mobile)
	  {
	    if ((!Validators.isEmpty(mobile)) && (mobile.length() > 3))
	    {
	      int type = 0;
	      try
	      {
	        type = Integer.parseInt(StringUtils.left(mobile, 3));
	      }
	      catch (NumberFormatException e)
	      {
	        logger.error("获取手机类型错误,手机号码为" + mobile);
	      }
	      if (((type >= 134) && (type < 140)) || ((type >= 158) && (type < 160))) {
	        return 1;
	      }
	      if (type == 150) {
	        return 1;
	      }
	      if (type == 151) {
	        return 1;
	      }
	      if (type == 157) {
	        return 1;
	      }
	      if ((type >= 130) && (type < 134)) {
	        return 2;
	      }
	      if ((type == 153) || (type == 156) || (type == 155)) {
	        return 2;
	      }
	      if (type == 0)
	      {
	        if (mobile.startsWith("0001")) {
	          return 1;
	        }
	        if (mobile.startsWith("0002")) {
	          return 2;
	        }
	      }
	    }
	    return 3;
	  }
	  
	  public static String[] splitString(String source, int[] lengthArray)
	  {
	    int lastIndex = 0;
	    int length = source.length();
	    String[] result = new String[lengthArray.length];
	    for (int i = 0; i < lengthArray.length; i++)
	    {
	      if ((lengthArray[i] != 0) && (lastIndex + lengthArray[i] <= length))
	      {
	        result[i] = source.substring(lastIndex, lastIndex + lengthArray[i]);
	      }
	      else
	      {
	        result[i] = source.substring(lastIndex);
	        break;
	      }
	      lastIndex += lengthArray[i];
	    }
	    return result;
	  }
	  
	  public static String createId()
	  {
	    return UUIDUtils.newId();
	  }
	  
	  public static void sendManagerMessage(MessageClient messageClient, String message)
	  {
	    try
	    {
	      if (message.indexOf("java.sql.SQLException: JZ0R2") != -1)
	      {
	        logger.error("Ignore warning message [java.sql.SQLException: JZ0R2] " + message);
	        return;
	      }
	      List<String> phones = ServiceLocator.getInstance().getWatchManagerPhones();
	      if (phones.size() < 1) {
	        return;
	      }
	      for (Iterator<String> iter = phones.iterator(); iter.hasNext();)
	      {
	        String mobile = (String)iter.next();
	        sendWarningMessageNew(messageClient, mobile, InetAddress.getLocalHost().getHostName() + message);
	      }
	    }
	    catch (Exception e)
	    {
	      logger.error("sendManagerMessage ERROR", e);
	    }
	  }
	  
	  public static void sendWarningMessage(String mobile, String message)
	  {
	    ServiceLocator.getInstance().getSysWatchMsgClient().sendMessage(
	      ServiceLocator.getInstance().getWarningSmsSpCommName(), 
	      "11" + createId() + new StringBuilder(String.valueOf(mobile)).append("            ").toString().substring(0, 12) + "                     " + 
	      message);
	    logger.warn("sendWarningMessage: " + mobile + "," + message);
	  }
	  
	  public static void sendWarningMessageNew(MessageClient messageClient, String mobile, String message)
	  {
	    String commUserMt = "sp_6466@zjmobile.zdsoft";
	    
	    StringBuffer sb = new StringBuffer();
	    sb.append("0034watchManager                  ");
	    sb.append(createId());
	    sb.append("1000002");
	    sb.append(mobile).append("       ");
	    sb.append("106575258166         1H         3        1");
	    sb.append(message);
	    
	    messageClient.sendMessage(commUserMt, new String(sb));
	  }
	  
	  public static int getSmsPageCount(int messageLength)
	  {
	    return messageLength <= 70 ? 1 : 1 + (messageLength - 1) / 65;
	  }
	  
	  public static String formatNumber(double number, int length, int fractionDigits)
	  {
	    numberFormat.setGroupingUsed(false);
	    numberFormat.setMinimumIntegerDigits(length);
	    numberFormat.setMaximumFractionDigits(fractionDigits);
	    numberFormat.setMinimumFractionDigits(fractionDigits);
	    return numberFormat.format(number);
	  }
	  
	  public static String encryptByMD5(String str)
	    throws NoSuchAlgorithmException
	  {
	    if (str == null) {
	      return null;
	    }
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(str.getBytes());
	    byte[] b = md.digest();
	    StringBuffer hexString = new StringBuffer("");
	    for (int i = 0; i < b.length; i++) {
	      hexString.append(StringUtils.leftPad(Integer.toHexString(b[i] & 0xFF), 2, "0"));
	    }
	    return hexString.toString();
	  }
	  
	  public static String toHexString(byte[] obj)
	  {
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < obj.length; i++)
	    {
	      int ch = obj[i];
	      if (ch < 0) {
	        ch += 256;
	      }
	      String str = Integer.toString(ch, 16);
	      if (str.length() == 1) {
	        sb.append("0");
	      }
	      sb.append(str);
	    }
	    return sb.toString().toUpperCase();
	  }
	  
	  public static boolean isChinese(String content)
	  {
	    byte[] result = null;
	    try
	    {
	      result = content.getBytes("GBK");
	    }
	    catch (UnsupportedEncodingException e)
	    {
	      logger.error("Unsupported Encoding :" + e);
	    }
	    for (int i = 0; i < result.length; i++) {
	      if (result[i] < 0) {
	        return true;
	      }
	    }
	    return false;
	  }
	  
	  public static List<String> pagination(String content, int encoding)
	  {
	    List<String> list = new ArrayList<String>();
	    
	    boolean isChinese = false;
	    if ((encoding == 8) || (encoding == 15)) {
	      isChinese = true;
	    }
	    int len = content.length();
	    if ((isChinese) && (len <= 70))
	    {
	      list.add(content);
	      return list;
	    }
	    if ((!isChinese) && (len <= 120))
	    {
	      list.add(content);
	      return list;
	    }
	    int pageLength = 115;
	    if (isChinese)
	    {
	      if (len > 585) {
	        pageLength = 63;
	      } else {
	        pageLength = 65;
	      }
	    }
	    else if (len > 1035) {
	      pageLength = 113;
	    } else {
	      pageLength = 115;
	    }
	    int pageCount = len / pageLength;
	    if (pageCount * pageLength != len) {
	      pageCount++;
	    }
	    int startPage = 1;
	    int startIndex = 0;
	    while (len - startIndex >= pageLength)
	    {
	      StringBuffer sb = new StringBuffer();
	      sb.append(content.substring(startIndex, startIndex + pageLength));
	      sb.append("(");
	      sb.append(startPage);
	      sb.append("/");
	      sb.append(pageCount);
	      sb.append(")");
	      list.add(sb.toString());
	      
	      startPage++;
	      startIndex = (startPage - 1) * pageLength;
	    }
	    if (len - startIndex > 0)
	    {
	      StringBuffer sb = new StringBuffer();
	      sb.append(content.substring(startIndex, len));
	      sb.append("(");
	      sb.append(startPage);
	      sb.append("/");
	      sb.append(pageCount);
	      sb.append(")");
	      list.add(sb.toString());
	    }
	    return list;
	  }
	  
	  public static String getMobileRegexString()
	  {
	    return mobileRegexString;
	  }
	  
	  public static void setMobileRegexString(String mobileRegexString)
	  {
	    mobileRegexString = mobileRegexString;
	  }
	  
	  public static String getUnicomRegexString()
	  {
	    return unicomRegexString;
	  }
	  
	  public static void setUnicomRegexString(String unicomRegexString)
	  {
	    unicomRegexString = unicomRegexString;
	  }
	  
	  public static String getTelecomRegexString()
	  {
	    return telecomRegexString;
	  }
	  
	  public static void setTelecomRegexString(String telecomRegexString)
	  {
	    telecomRegexString = telecomRegexString;
	  }
	  
	  public static boolean isTrue(String value)
	  {
	    String v = (value == null) || ("".equals(value.trim())) ? "false" : value;
	    return ("true".equals(v.toLowerCase())) || ("1".equals(v));
	  }
	  
	  public static int numberFormat(String num, int defaultNum)
	  {
	    if (StringUtils.isNumeric(num)) {
	      return Integer.parseInt(num);
	    }
	    return defaultNum;
	  }
}
