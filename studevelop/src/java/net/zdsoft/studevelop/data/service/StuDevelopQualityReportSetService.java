package net.zdsoft.studevelop.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StuDevelopQualityReportSet;

public interface StuDevelopQualityReportSetService extends BaseService<StuDevelopQualityReportSet, String>{

	public StuDevelopQualityReportSet findByAll(String unitId, String acadyear, String semester, int section);
	
	public void save(StuDevelopQualityReportSet stuDevelopQualityReportSet);
}
