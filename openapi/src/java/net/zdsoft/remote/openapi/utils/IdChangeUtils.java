package net.zdsoft.remote.openapi.utils;

import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.lang3.StringUtils;

public class IdChangeUtils {
	
	/**
	 * id 为null，返回32位的guid
	 * id 不足32位，左侧补0 到 32位
	 * id 是32位的，就直接返回
	 * @param id
	 * @return
	 */
	public static String get32Id(String id) {
		if(StringUtils.isBlank(id)){
			return UuidUtils.generateUuid();
		}
		return StringUtils.leftPad(id, 32, "0");
	}

}
