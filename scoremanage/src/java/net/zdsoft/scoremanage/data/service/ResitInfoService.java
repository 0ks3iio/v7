package net.zdsoft.scoremanage.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.scoremanage.data.entity.ResitInfo;

public interface ResitInfoService extends BaseService<ResitInfo, String>{
	
	void deleteResitInfoBy(String unitId, String acadyear, String semester, String examId, String gradeId);

	List<ResitInfo> listResitInfoBy(String unitId, String acadyear, String semester, String examId, String gradeId);
}
