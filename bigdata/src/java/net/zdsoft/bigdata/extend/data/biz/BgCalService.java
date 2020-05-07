package net.zdsoft.bigdata.extend.data.biz;

import java.util.Map;

import net.zdsoft.bigdata.extend.data.entity.UserProfile;
import net.zdsoft.framework.entity.Json;

public interface BgCalService {

	/**
	 * 后台用戶标签计算
	 * 
	 * @param profileCode
	 * @return
	 */
	public boolean dealUserTagCal(String profileCode);
	
	public boolean dealUserTagCalDetail(UserProfile up,Map<String,Json> jsonMap,BgCalStorage bcs);

	/**
	 * 后台预警计算
	 * 
	 * @param projectId
	 * @return
	 */
	public boolean dealWarningCal(String projectId);

	
}
