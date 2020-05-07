package net.zdsoft.diathesis.data.service;

import java.util.List;
import java.util.Map;

public interface DiathesisRoleService {
	
	/**
	 * @param unitId
	 * @param userId
	 * @return 管理员：1;  年级老师 2     ;  班主任 :3     导师: 4
	 *   values:   管理员:roleIds    年级老师:gradeIds    班主任:classIds  导师 : stuIds
	 */

	public Map<String,List<String>> findRoleByUserId(String unitId,String userId);
	
}
