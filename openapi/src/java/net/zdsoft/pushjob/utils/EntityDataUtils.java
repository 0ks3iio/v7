package net.zdsoft.pushjob.utils;

import java.io.IOException;

import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.pushjob.entity.BasePushJob;

/**
 * 得到对应的基础数据
 * @author yangsj
 *
 */
public class EntityDataUtils {

	private String getDate(BasePushJob job){
		try {
			if(job != null){
//				String url = BaseUrlUtils.getUrl(job);
//				return UrlUtils.get(url, new String());
			}
		} catch (Exception e) {
			e.printStackTrace();
//			log.error("调用接口获取登录用户信息失败：--------" + e.getMessage());
		}
		return null;
	}
	
	
}
