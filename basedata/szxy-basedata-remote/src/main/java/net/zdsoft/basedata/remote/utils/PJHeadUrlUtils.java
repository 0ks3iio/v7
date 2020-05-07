package net.zdsoft.basedata.remote.utils;

import org.apache.commons.lang3.StringUtils;


/**
 * @author yangsj 2017-8-2下午4:03:17
 * 
 *         7.0桌面拼接头像地址
 */
public class PJHeadUrlUtils {


	// 拼接头像url
	private static String getHeadUrl(String avaString, String fileUrl) {
		String avatarUrl;
		if (StringUtils.endsWith(fileUrl, "/")) {
			avatarUrl = fileUrl + "store/" + avaString;
		} else {
			avatarUrl = fileUrl + "/store/" + avaString;
		}
		return avatarUrl;
	}

	/**
	 * @param headUrl
	 * @param avatarUrl
	 *            得到展示的头像url
	 */
	public static String getShowAvatarUrl(String headUrl, String avatarUrl, String fileUrl) {
	    if (!StringUtils.startsWith(avatarUrl, "http")) {
			return getHeadUrl(avatarUrl, fileUrl);
		}
	    return avatarUrl;
	}
}
