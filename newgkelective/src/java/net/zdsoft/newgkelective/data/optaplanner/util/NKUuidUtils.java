/*
 * Created on 2004-8-19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.zdsoft.newgkelective.data.optaplanner.util;

import java.net.InetAddress;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

public class NKUuidUtils {

	private static final int IP;
	static {
		int ipadd;
		try {
			ipadd = toInt(InetAddress.getLocalHost().getAddress());
		} catch (Exception e) {
			ipadd = 0;
		}
		IP = ipadd;
	}
	private static short counter = (short) 0;

	/**
	 * 构造方法
	 */
	public NKUuidUtils() {
	}

	/**
	 * 生成16进制表达的字符串UUID
	 * 
	 * @return
	 */
	public static String generateUuid() {
		String time = String.valueOf(System.nanoTime());
		String count = String.valueOf(getCount());
		return new StringBuffer(32).append(time).append(count).append(RandomStringUtils.randomNumeric(32 - time.length() - count.length())).toString();
	}

	/**
	 * Unique in a millisecond for this JVM instance (unless there are >
	 * Short.MAX_VALUE instances created in a millisecond)
	 */
	private static short getCount() {
		synchronized (NKUuidUtils.class) {
			if (counter < 0)
				counter = 0;
			return counter++;
		}
	}

	/**
	 * Unique in a local network
	 */
	private static int getIP() {
		return IP;
	}

	private static int toInt(byte[] bytes) {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
		}
		return result;
	}

	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		for(int i = 0; i < 100000; i ++)
			generateUuid();
		System.out.println(System.currentTimeMillis() - time);
	}

}