package net.zdsoft.desktop.utils;

/**
 * @author ke_shen@126.com
 * @since 2018/1/29 上午10:09
 */
public class NumberUtils {

	/** 安全的拆箱方法，防止自动拆箱NPE问题 */
	public static int safeUnpack(Integer integer, int defaultValue) {
		if (integer == null) {
			return defaultValue;
		}
		return integer;
	}

	public static long safeUnpack(Long value, long defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static double safeUnpack(Double value, double defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static float safeUnpack(Float value, float defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
}
