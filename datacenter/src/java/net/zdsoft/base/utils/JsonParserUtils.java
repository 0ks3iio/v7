package net.zdsoft.base.utils;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.EncryptAES;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.openapi.savedata.constant.BaseSaveConstant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 公用的json类型数据解析
 * 
 * @author chenlw
 *
 */
public class JsonParserUtils {

	// 进行数据的转化
	public static String getSaveData(String jsonStr, String ticketKey) {
		try {
			if (getIsEnableSecurity()) {
				JSONObject jsonObject = JSON.parseObject(jsonStr);
				jsonStr = jsonObject.getString("data");
				jsonStr = EncryptAES.aesDecryptBase64(jsonStr, ticketKey);
			}
			return jsonStr;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 是否加密传输
	 * 
	 * @return
	 */
	public static Boolean getIsEnableSecurity() {
		if (StringUtils.isNotBlank(Evn
				.getString(BaseSaveConstant.BASE_SYN_ENABLE_SECURITY))
				&& "true".equalsIgnoreCase(Evn
						.getString(BaseSaveConstant.BASE_SYN_ENABLE_SECURITY))) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
