package net.zdsoft.base.utils;

import java.util.Collection;
import java.util.Locale;

import net.zdsoft.base.exception.CheckException;
import net.zdsoft.base.exception.NoPermissionException;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.MessageSource;



/**
 * 校验工具类
 * 
 *
 */
public class CheckUtil {
	
	private CheckUtil() {
	    throw new IllegalStateException("Utility class");
	}
	private static MessageSource resources;

	public static void setResources(MessageSource resources) {
		CheckUtil.resources = resources;
	}

	public static void check(boolean condition, String msgKey, Object... args) {
		if (!condition) {
			fail(msgKey, args);
		}
	}

	public static void notEmpty(String str, String msgKey, Object... args) {
		if (str == null || str.isEmpty()) {
			fail(msgKey, args);
		}
	}

	public static void notNull(Object obj, String msgKey, Object... args) {
		if (obj == null) {
			fail(msgKey, args);
		}
	}
	public static void checkMaxLen(String str, String msgKey,int len, Object... args) {
		if (str!=null && str.length()>len) {
			fail(msgKey, args);
		}
	}
	
	public static void notEmpty(Collection<?> obj, String msgKey, Object... args) {
		if (CollectionUtils.isEmpty(obj)) {
			fail(msgKey, args);
		}
	}
	
	public static void permissionCheck(Object obj) {
		if (obj == null) {
			noPermission();
		}
	}
	public static void permissionCheck(String str) {
		if (str == null || str.isEmpty()) {
			noPermission();
		}
	}

	private static void fail(String msgKey, Object... args) {
		throw new CheckException(resources.getMessage(msgKey, args,new Locale("zh")));
	}
	
	public static void noPermission() {
		throw new NoPermissionException("无权限.");
	}
}
